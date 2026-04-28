"""
监控指标导出模块
集成Prometheus指标导出功能
"""

from prometheus_client import Counter, Histogram, Gauge, Info, generate_latest
from prometheus_client import CONTENT_TYPE_LATEST, CollectorRegistry
from fastapi import Response
import time
import threading


# ===== 创建独立的Registry =====
REGISTRY = CollectorRegistry()


# ===== 应用信息 =====
APP_INFO = Info(
    'cosmic_replay_app',
    'Application information',
    registry=REGISTRY
)
APP_INFO.info({
    'version': '2.0.0',
    'service': 'cosmic-replay-v2'
})


# ===== HTTP请求指标 =====
HTTP_REQUESTS_TOTAL = Counter(
    'cosmic_replay_http_requests_total',
    'Total HTTP requests',
    ['method', 'endpoint', 'status'],
    registry=REGISTRY
)

HTTP_REQUEST_DURATION = Histogram(
    'cosmic_replay_http_request_duration_seconds',
    'HTTP request latency',
    ['method', 'endpoint'],
    buckets=[0.01, 0.05, 0.1, 0.5, 1.0, 2.0, 5.0, 10.0],
    registry=REGISTRY
)

HTTP_REQUESTS_IN_PROGRESS = Gauge(
    'cosmic_replay_http_requests_in_progress',
    'HTTP requests currently in progress',
    ['method', 'endpoint'],
    registry=REGISTRY
)


# ===== 用例执行指标 =====
CASE_RUN_TOTAL = Counter(
    'cosmic_replay_case_runs_total',
    'Total case executions',
    ['case_name', 'env', 'status'],
    registry=REGISTRY
)

CASE_RUN_DURATION = Histogram(
    'cosmic_replay_case_run_duration_seconds',
    'Case execution duration',
    ['case_name', 'env'],
    buckets=[1.0, 5.0, 10.0, 30.0, 60.0, 120.0, 300.0, 600.0],
    registry=REGISTRY
)

CASE_STEP_TOTAL = Counter(
    'cosmic_replay_case_steps_total',
    'Total step executions',
    ['case_name', 'step_type', 'status'],
    registry=REGISTRY
)

CASE_ACTIVE_RUNS = Gauge(
    'cosmic_replay_active_runs',
    'Currently active case runs',
    registry=REGISTRY
)


# ===== 队列指标 =====
QUEUE_SIZE = Gauge(
    'cosmic_replay_queue_size',
    'Current task queue size',
    registry=REGISTRY
)

QUEUE_WAIT_TIME = Histogram(
    'cosmic_replay_queue_wait_seconds',
    'Task queue wait time',
    buckets=[0.1, 0.5, 1.0, 5.0, 10.0, 30.0, 60.0],
    registry=REGISTRY
)


# ===== 系统资源指标 =====
CONNECTION_POOL_SIZE = Gauge(
    'cosmic_replay_connection_pool_size',
    'HTTP connection pool size',
    registry=REGISTRY
)

MEMORY_USAGE_BYTES = Gauge(
    'cosmic_replay_memory_usage_bytes',
    'Process memory usage',
    registry=REGISTRY
)


class MetricsCollector:
    """指标收集器"""
    
    def __init__(self):
        self._lock = threading.Lock()
    
    def record_http_request(self, method: str, endpoint: str, status: int, duration: float):
        """记录HTTP请求"""
        with self._lock:
            HTTP_REQUESTS_TOTAL.labels(method=method, endpoint=endpoint, status=status).inc()
            HTTP_REQUEST_DURATION.labels(method=method, endpoint=endpoint).observe(duration)
    
    def start_http_request(self, method: str, endpoint: str):
        """开始HTTP请求"""
        HTTP_REQUESTS_IN_PROGRESS.labels(method=method, endpoint=endpoint).inc()
    
    def end_http_request(self, method: str, endpoint: str):
        """结束HTTP请求"""
        HTTP_REQUESTS_IN_PROGRESS.labels(method=method, endpoint=endpoint).dec()
    
    def record_case_run(self, case_name: str, env: str, status: str, duration: float):
        """记录用例执行"""
        with self._lock:
            CASE_RUN_TOTAL.labels(case_name=case_name, env=env, status=status).inc()
            CASE_RUN_DURATION.labels(case_name=case_name, env=env).observe(duration)
    
    def record_step(self, case_name: str, step_type: str, status: str):
        """记录步骤执行"""
        CASE_STEP_TOTAL.labels(case_name=case_name, step_type=step_type, status=status).inc()
    
    def inc_active_runs(self):
        """增加活跃执行数"""
        CASE_ACTIVE_RUNS.inc()
    
    def dec_active_runs(self):
        """减少活跃执行数"""
        CASE_ACTIVE_RUNS.dec()
    
    def set_queue_size(self, size: int):
        """设置队列大小"""
        QUEUE_SIZE.set(size)
    
    def update_memory_usage(self, bytes_used: int):
        """更新内存使用"""
        MEMORY_USAGE_BYTES.set(bytes_used)


# 全局收集器
metrics_collector = MetricsCollector()


def get_metrics_response() -> Response:
    """返回Prometheus指标响应"""
    return Response(
        content=generate_latest(REGISTRY),
        media_type=CONTENT_TYPE_LATEST
    )


class MetricsMiddleware:
    """FastAPI中间件 - 自动收集HTTP请求指标"""
    
    def __init__(self, app):
        self.app = app
    
    async def __call__(self, scope, receive, send):
        if scope['type'] != 'http':
            await self.app(scope, receive, send)
            return
        
        # 排除指标和健康检查端点
        path = scope['path']
        if path in ['/metrics', '/api/health', '/health']:
            await self.app(scope, receive, send)
            return
        
        method = scope['method']
        start_time = time.time()
        status_code = 500
        
        metrics_collector.start_http_request(method, path)
        
        async def send_wrapper(message):
            nonlocal status_code
            if message['type'] == 'response.start':
                status_code = message['status']
            await send(message)
        
        try:
            await self.app(scope, receive, send_wrapper)
        finally:
            duration = time.time() - start_time
            metrics_collector.record_http_request(method, path, status_code, duration)
            metrics_collector.end_http_request(method, path)
