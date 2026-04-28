# cosmic-replay-v2 运维方案设计

## 一、部署方案增强

### 1.1 优化后的Dockerfile

```dockerfile
# cosmic-replay v2 - Production Docker Image
# 多阶段构建 + 安全加固

# ===== 构建阶段 =====
FROM python:3.11-slim AS builder

WORKDIR /build

# 安装构建依赖
RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    && rm -rf /var/lib/apt/lists/*

# 复制依赖文件
COPY requirements.txt .

# 创建虚拟环境并安装依赖
RUN python -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

# ===== 运行阶段 =====
FROM python:3.11-slim AS runtime

# 安全标签
LABEL maintainer="Mars"
LABEL version="2.0.0"
LABEL description="cosmic-replay HAR回放自动化测试工具"
LABEL org.opencontainers.image.source="https://github.com/xxx/cosmic-replay-v2"

# 创建非root用户
RUN groupadd -r cosmic --gid=1000 && \
    useradd -r -g cosmic --uid=1000 --home-dir=/app --shell=/sbin/nologin cosmic

# 安装运行时依赖
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/* \
    && apt-get clean

# 从构建阶段复制虚拟环境
COPY --from=builder /opt/venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

# 设置工作目录
WORKDIR /app

# 复制应用代码
COPY --chown=cosmic:cosmic . .

# 创建必要目录
RUN mkdir -p /app/cases /app/logs /app/config/envs /app/config/hars \
    && chown -R cosmic:cosmic /app

# 安全环境变量
ENV PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    COSMIC_LOGIN_SCRIPT=/app/cosmic_login.py \
    TZ=Asia/Shanghai

# 暴露端口
EXPOSE 8766

# 切换非root用户
USER cosmic

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8766/api/health || exit 1

# 启动命令
CMD ["python", "-m", "lib.webui.server", "--port", "8766", "--host", "0.0.0.0", "--no-browser"]
```

### 1.2 增强的docker-compose.yml

```yaml
# cosmic-replay v2 - Production Docker Compose
version: "3.8"

services:
  cosmic-replay-v2:
    build:
      context: .
      dockerfile: Dockerfile
      target: runtime
    image: cosmic-replay-v2:${VERSION:-latest}
    container_name: cosmic-replay-v2
    restart: unless-stopped
    
    # 资源限制
    deploy:
      resources:
        limits:
          cpus: '2.0'
          memory: 2G
        reservations:
          cpus: '0.5'
          memory: 512M
    
    ports:
      - "${PORT:-8766}:8766"
    
    volumes:
      - ./cases:/app/cases:ro
      - ./config:/app/config:ro
      - cosmic-logs:/app/logs
      - cosmic-hars:/app/har_uploads
    
    environment:
      - COSMIC_LOGIN_SCRIPT=/app/cosmic_login.py
      - TZ=Asia/Shanghai
      # 敏感配置通过环境变量注入
      - COSMIC_USERNAME=${COSMIC_USERNAME:-}
      - COSMIC_PASSWORD=${COSMIC_PASSWORD:-}
      # 日志配置
      - LOG_LEVEL=${LOG_LEVEL:-info}
      - LOG_MAX_SIZE_MB=100
      - LOG_RETENTION_DAYS=30
    
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8766/api/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 10s
    
    # 安全配置
    security_opt:
      - no-new-privileges:true
    read_only: false
    cap_drop:
      - ALL
    
    # 日志配置
    logging:
      driver: "json-file"
      options:
        max-size: "100m"
        max-file: "3"
        labels: "app,environment"
        tag: "{{.Name}}/{{.ID}}"
    
    labels:
      - "app=cosmic-replay"
      - "environment=${ENVIRONMENT:-production}"
      - "version=${VERSION:-latest}"

  # Prometheus监控
  prometheus:
    image: prom/prometheus:v2.45.0
    container_name: cosmic-prometheus
    restart: unless-stopped
    ports:
      - "9090:9090"
    volumes:
      - ./deploy/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml:ro
      - prometheus-data:/prometheus
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--storage.tsdb.retention.time=15d'
      - '--web.enable-lifecycle'
    profiles:
      - monitoring

  # Grafana可视化
  grafana:
    image: grafana/grafana:10.0.0
    container_name: cosmic-grafana
    restart: unless-stopped
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_USER=${GRAFANA_ADMIN:-admin}
      - GF_SECURITY_ADMIN_PASSWORD=${GRAFANA_PASSWORD:-admin}
      - GF_USERS_ALLOW_SIGN_UP=false
    volumes:
      - ./deploy/grafana/provisioning:/etc/grafana/provisioning:ro
      - grafana-data:/var/lib/grafana
    depends_on:
      - prometheus
    profiles:
      - monitoring

  # Alertmanager告警
  alertmanager:
    image: prom/alertmanager:v0.25.0
    container_name: cosmic-alertmanager
    restart: unless-stopped
    ports:
      - "9093:9093"
    volumes:
      - ./deploy/alertmanager/alertmanager.yml:/etc/alertmanager/alertmanager.yml:ro
      - alertmanager-data:/alertmanager
    profiles:
      - monitoring

volumes:
  cosmic-logs:
  cosmic-hars:
  prometheus-data:
  grafana-data:
  alertmanager-data:

networks:
  default:
    name: cosmic-replay-network
    driver: bridge
```

### 1.3 Kubernetes部署配置

```yaml
# deploy/k8s/namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: cosmic-replay
  labels:
    app: cosmic-replay
    environment: production

---
# deploy/k8s/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: cosmic-replay-config
  namespace: cosmic-replay
data:
  webui.yaml: |
    webui:
      port: 8766
      host: 0.0.0.0
      open_browser: false
      default_env: prod
    logging:
      level: info
      log_dir: /app/logs
    paths:
      cases_dir: /app/cases
      har_upload_dir: /app/har_uploads

---
# deploy/k8s/secret.yaml
apiVersion: v1
kind: Secret
metadata:
  name: cosmic-replay-credentials
  namespace: cosmic-replay
type: Opaque
stringData:
  COSMIC_USERNAME: "prod-user"
  COSMIC_PASSWORD: "prod-password"
  # 生产环境建议使用外部Secret管理(如Vault)

---
# deploy/k8s/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cosmic-replay-v2
  namespace: cosmic-replay
  labels:
    app: cosmic-replay
    version: v2.0.0
spec:
  replicas: 2
  selector:
    matchLabels:
      app: cosmic-replay
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  template:
    metadata:
      labels:
        app: cosmic-replay
        version: v2.0.0
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "8766"
        prometheus.io/path: "/metrics"
    spec:
      serviceAccountName: cosmic-replay
      securityContext:
        runAsNonRoot: true
        runAsUser: 1000
        fsGroup: 1000
      containers:
        - name: cosmic-replay
          image: cosmic-replay-v2:v2.0.0
          imagePullPolicy: IfNotPresent
          ports:
            - name: http
              containerPort: 8766
              protocol: TCP
          env:
            - name: TZ
              value: "Asia/Shanghai"
            - name: LOG_LEVEL
              value: "info"
            - name: COSMIC_USERNAME
              valueFrom:
                secretKeyRef:
                  name: cosmic-replay-credentials
                  key: COSMIC_USERNAME
            - name: COSMIC_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: cosmic-replay-credentials
                  key: COSMIC_PASSWORD
          envFrom:
            - configMapRef:
                name: cosmic-replay-config
          resources:
            requests:
              cpu: "500m"
              memory: "512Mi"
            limits:
              cpu: "2000m"
              memory: "2Gi"
          livenessProbe:
            httpGet:
              path: /api/health
              port: 8766
            initialDelaySeconds: 10
            periodSeconds: 30
            timeoutSeconds: 10
            failureThreshold: 3
          readinessProbe:
            httpGet:
              path: /api/health
              port: 8766
            initialDelaySeconds: 5
            periodSeconds: 10
            timeoutSeconds: 5
            failureThreshold: 3
          volumeMounts:
            - name: cases
              mountPath: /app/cases
              readOnly: true
            - name: logs
              mountPath: /app/logs
            - name: config
              mountPath: /app/config
              readOnly: true
          securityContext:
            allowPrivilegeEscalation: false
            readOnlyRootFilesystem: true
            capabilities:
              drop:
                - ALL
      volumes:
        - name: cases
          configMap:
            name: cosmic-replay-cases
        - name: config
          configMap:
            name: cosmic-replay-config
        - name: logs
          emptyDir: {}
      terminationGracePeriodSeconds: 30
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
            - weight: 100
              podAffinityTerm:
                labelSelector:
                  matchLabels:
                    app: cosmic-replay
                topologyKey: kubernetes.io/hostname

---
# deploy/k8s/service.yaml
apiVersion: v1
kind: Service
metadata:
  name: cosmic-replay
  namespace: cosmic-replay
  labels:
    app: cosmic-replay
spec:
  type: ClusterIP
  ports:
    - name: http
      port: 8766
      targetPort: 8766
      protocol: TCP
  selector:
    app: cosmic-replay

---
# deploy/k8s/ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: cosmic-replay-ingress
  namespace: cosmic-replay
  annotations:
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
    nginx.ingress.kubernetes.io/proxy-body-size: "50m"
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
spec:
  ingressClassName: nginx
  tls:
    - hosts:
        - cosmic-replay.example.com
      secretName: cosmic-replay-tls
  rules:
    - host: cosmic-replay.example.com
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: cosmic-replay
                port:
                  number: 8766

---
# deploy/k8s/hpa.yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: cosmic-replay-hpa
  namespace: cosmic-replay
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: cosmic-replay-v2
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
    - type: Resource
      resource:
        name: memory
        target:
          type: Utilization
          averageUtilization: 80
```

---

## 二、监控告警体系

### 2.1 Prometheus指标导出

```python
# lib/monitoring/metrics.py
"""Prometheus指标导出模块"""

from prometheus_client import Counter, Histogram, Gauge, Info, generate_latest
from prometheus_client import CONTENT_TYPE_LATEST
from fastapi import Response
import time

# ===== 应用信息 =====
APP_INFO = Info(
    'cosmic_replay_app',
    'Application information'
)
APP_INFO.info({
    'version': '2.0.0',
    'service': 'cosmic-replay-v2'
})

# ===== 请求指标 =====
REQUEST_COUNT = Counter(
    'cosmic_replay_http_requests_total',
    'Total HTTP requests',
    ['method', 'endpoint', 'status']
)

REQUEST_LATENCY = Histogram(
    'cosmic_replay_http_request_duration_seconds',
    'HTTP request latency',
    ['method', 'endpoint'],
    buckets=[0.01, 0.05, 0.1, 0.5, 1.0, 2.0, 5.0, 10.0]
)

# ===== 用例执行指标 =====
CASE_RUN_TOTAL = Counter(
    'cosmic_replay_case_runs_total',
    'Total case executions',
    ['case_name', 'env', 'status']
)

CASE_RUN_DURATION = Histogram(
    'cosmic_replay_case_run_duration_seconds',
    'Case execution duration',
    ['case_name', 'env'],
    buckets=[1.0, 5.0, 10.0, 30.0, 60.0, 120.0, 300.0, 600.0]
)

CASE_STEP_TOTAL = Counter(
    'cosmic_replay_case_steps_total',
    'Total step executions',
    ['case_name', 'step_type', 'status']
)

# ===== 连接池指标 =====
DB_CONNECTIONS = Gauge(
    'cosmic_replay_db_connections',
    'Active database connections',
    ['pool']
)

HTTP_POOL_CONNECTIONS = Gauge(
    'cosmic_replay_http_pool_connections',
    'Active HTTP pool connections'
)

# ===== 系统指标 =====
ACTIVE_RUNS = Gauge(
    'cosmic_replay_active_runs',
    'Currently active case runs'
)

QUEUE_SIZE = Gauge(
    'cosmic_replay_queue_size',
    'Task queue size'
)


def get_metrics_response():
    """返回Prometheus指标响应"""
    return Response(
        content=generate_latest(),
        media_type=CONTENT_TYPE_LATEST
    )


class MetricsMiddleware:
    """FastAPI中间件 - 自动收集请求指标"""
    
    def __init__(self, app):
        self.app = app
    
    async def __call__(self, scope, receive, send):
        if scope['type'] != 'http':
            await self.app(scope, receive, send)
            return
        
        method = scope['method']
        path = scope['path']
        
        # 排除指标端点自身
        if path == '/metrics':
            await self.app(scope, receive, send)
            return
        
        start_time = time.time()
        status_code = 500
        
        async def send_wrapper(message):
            nonlocal status_code
            if message['type'] == 'response.start':
                status_code = message['status']
            await send(message)
        
        try:
            await self.app(scope, receive, send_wrapper)
        finally:
            duration = time.time() - start_time
            REQUEST_COUNT.labels(method=method, endpoint=path, status=status_code).inc()
            REQUEST_LATENCY.labels(method=method, endpoint=path).observe(duration)
```

### 2.2 Prometheus配置

```yaml
# deploy/prometheus/prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s
  external_labels:
    monitor: 'cosmic-replay'
    environment: 'production'

rule_files:
  - /etc/prometheus/rules/*.yml

alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093

scrape_configs:
  - job_name: 'cosmic-replay'
    static_configs:
      - targets: ['cosmic-replay-v2:8766']
    metrics_path: '/metrics'
    scrape_interval: 10s

  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']
```

### 2.3 告警规则

```yaml
# deploy/prometheus/rules/alerts.yml
groups:
  - name: cosmic-replay-alerts
    rules:
      # 服务可用性告警
      - alert: ServiceDown
        expr: up{job="cosmic-replay"} == 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "cosmic-replay服务不可用"
          description: "服务已宕机超过1分钟，请立即检查"

      # 高错误率告警
      - alert: HighErrorRate
        expr: |
          sum(rate(cosmic_replay_http_requests_total{status=~"5.."}[5m])) 
          / sum(rate(cosmic_replay_http_requests_total[5m])) > 0.05
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "HTTP错误率过高"
          description: "5xx错误率超过5%，当前: {{ $value | humanizePercentage }}"

      # 用例失败率高
      - alert: HighCaseFailureRate
        expr: |
          sum(rate(cosmic_replay_case_runs_total{status="failed"}[10m]))
          / sum(rate(cosmic_replay_case_runs_total[10m])) > 0.1
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "用例执行失败率过高"
          description: "失败率超过10%，请检查环境状态"

      # 响应时间过长
      - alert: SlowResponseTime
        expr: |
          histogram_quantile(0.95, 
            sum(rate(cosmic_replay_http_request_duration_seconds_bucket[5m])) by (le)
          ) > 2.0
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "响应时间过长"
          description: "P95响应时间超过2秒"

      # 内存使用过高
      - alert: HighMemoryUsage
        expr: |
          container_memory_usage_bytes{name="cosmic-replay-v2"} 
          / container_spec_memory_limit_bytes{name="cosmic-replay-v2"} > 0.9
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "内存使用过高"
          description: "容器内存使用超过90%"

      # 执行队列积压
      - alert: ExecutionQueueBacklog
        expr: cosmic_replay_queue_size > 50
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "执行队列积压"
          description: "当前队列大小: {{ $value }}"
```

### 2.4 Alertmanager配置

```yaml
# deploy/alertmanager/alertmanager.yml
global:
  resolve_timeout: 5m
  smtp_smarthost: 'smtp.example.com:587'
  smtp_from: 'alertmanager@example.com'
  smtp_auth_username: 'alertmanager@example.com'
  smtp_auth_password: '{{ .Values.smtpPassword }}'

templates:
  - '/etc/alertmanager/templates/*.tmpl'

route:
  group_by: ['alertname', 'severity']
  group_wait: 30s
  group_interval: 5m
  repeat_interval: 4h
  receiver: 'default'
  routes:
    - match:
        severity: critical
      receiver: 'critical'
      continue: true
    - match:
        severity: warning
      receiver: 'warning'

receivers:
  - name: 'default'
    webhook_configs:
      - url: 'http://webhook.example.com/alert'
        send_resolved: true

  - name: 'critical'
    email_configs:
      - to: 'ops-team@example.com'
        headers:
          Subject: '[CRITICAL] cosmic-replay告警'
        html: '{{ template "email.default.html" . }}'
    slack_configs:
      - api_url: '{{ .Values.slackWebhookUrl }}'
        channel: '#ops-alerts'
        send_resolved: true

  - name: 'warning'
    email_configs:
      - to: 'dev-team@example.com'
        headers:
          Subject: '[WARNING] cosmic-replay告警'
```

---

## 三、日志收集分析方案

### 3.1 结构化日志配置

```python
# lib/logging_config.py
"""结构化日志配置"""

import logging
import json
import sys
from datetime import datetime
from pathlib import Path


class StructuredFormatter(logging.Formatter):
    """结构化日志格式化器"""
    
    def format(self, record):
        log_entry = {
            'timestamp': datetime.utcnow().isoformat() + 'Z',
            'level': record.levelname,
            'logger': record.name,
            'message': record.getMessage(),
            'module': record.module,
            'function': record.funcName,
            'line': record.lineno,
        }
        
        # 添加异常信息
        if record.exc_info:
            log_entry['exception'] = self.formatException(record.exc_info)
        
        # 添加额外字段
        for key, value in record.__dict__.items():
            if key not in ('name', 'msg', 'args', 'created', 'filename', 
                          'pathname', 'module', 'exc_info', 'exc_text',
                          'stack_info', 'lineno', 'funcName', 'levelname',
                          'levelno', 'msecs', 'message'):
                log_entry[key] = value
        
        return json.dumps(log_entry, ensure_ascii=False)


class SensitiveDataFilter(logging.Filter):
    """敏感数据过滤器"""
    
    SENSITIVE_FIELDS = [
        'password', 'passwd', 'pwd', 'secret', 'token', 
        'api_key', 'apikey', 'credential', 'auth'
    ]
    
    def filter(self, record):
        msg = record.getMessage()
        # 简单替换敏感信息
        for field in self.SENSITIVE_FIELDS:
            # 匹配 password=xxx 或 "password": "xxx"
            import re
            msg = re.sub(
                rf'({field}["\s]*[=:]["\s]*)[^\s,"\'\}}]+',
                rf'\1***MASKED***',
                msg,
                flags=re.IGNORECASE
            )
        record.msg = msg
        record.args = ()
        return True


def setup_logging(log_level='INFO', log_dir='./logs', structured=True):
    """配置应用日志"""
    
    log_path = Path(log_dir)
    log_path.mkdir(parents=True, exist_ok=True)
    
    # 根日志器
    root_logger = logging.getLogger()
    root_logger.setLevel(getattr(logging, log_level))
    
    # 清除现有handlers
    root_logger.handlers.clear()
    
    # 控制台输出
    console_handler = logging.StreamHandler(sys.stdout)
    if structured:
        console_handler.setFormatter(StructuredFormatter())
    else:
        console_handler.setFormatter(logging.Formatter(
            '%(asctime)s [%(levelname)s] %(name)s: %(message)s'
        ))
    root_logger.addHandler(console_handler)
    
    # 文件输出（JSON格式）
    file_handler = logging.FileHandler(
        log_path / 'application.jsonl',
        encoding='utf-8'
    )
    file_handler.setFormatter(StructuredFormatter())
    root_logger.addHandler(file_handler)
    
    # 敏感数据过滤器
    sensitive_filter = SensitiveDataFilter()
    for handler in root_logger.handlers:
        handler.addFilter(sensitive_filter)
    
    return root_logger
```

### 3.2 Loki集成配置

```yaml
# deploy/loki/promtail-config.yml
server:
  http_listen_port: 9080
  grpc_listen_port: 0

positions:
  filename: /var/log/positions.yaml

clients:
  - url: http://loki:3100/loki/api/v1/push

scrape_configs:
  - job_name: cosmic-replay
    static_configs:
      - targets:
          - localhost
        labels:
          job: cosmic-replay
          app: cosmic-replay-v2
          __path__: /var/log/cosmic-replay/*.jsonl
    pipeline_stages:
      - json:
          expressions:
            timestamp: timestamp
            level: level
            logger: logger
            message: message
      - labels:
          level:
          logger:
      - timestamp:
          source: timestamp
          format: RFC3339Nano
```

---

## 四、配置管理方案

### 4.1 外部配置管理

```yaml
# config/envs/production.yaml
env:
  name: Production
  base_url: ${env:COSMIC_BASE_URL}
  datacenter_id: ${env:COSMIC_DATACENTER_ID}

credentials:
  # 从环境变量读取，不存储明文
  username_env: COSMIC_USERNAME
  password_env: COSMIC_PASSWORD

basedata: {}

runtime:
  sign_required: true
  timeout: 60
  login_retries: 3

# 安全配置
security:
  auth_enabled: true
  rate_limit: 100
  session_timeout: 3600
```

### 4.2 配置校验器

```python
# lib/config_validator.py
"""配置校验模块"""

from dataclasses import dataclass
from typing import List, Optional
import re


@dataclass
class ValidationError:
    field: str
    message: str
    severity: str  # 'error' | 'warning'


class ConfigValidator:
    """配置校验器"""
    
    def __init__(self):
        self.errors: List[ValidationError] = []
    
    def validate_env_config(self, config: dict) -> List[ValidationError]:
        """校验环境配置"""
        self.errors = []
        
        # 必填字段
        self._check_required(config, 'env.name', '环境名称')
        self._check_required(config, 'env.base_url', '服务地址')
        
        # URL格式
        base_url = config.get('env', {}).get('base_url', '')
        if base_url and not self._is_valid_url(base_url):
            self.errors.append(ValidationError(
                field='env.base_url',
                message='URL格式无效',
                severity='error'
            ))
        
        # 凭证检查
        creds = config.get('credentials', {})
        if not creds.get('username') and not creds.get('username_env'):
            self.errors.append(ValidationError(
                field='credentials.username',
                message='缺少用户名配置',
                severity='warning'
            ))
        
        # 明文密码警告
        if creds.get('password') and not creds.get('password_env'):
            self.errors.append(ValidationError(
                field='credentials.password',
                message='建议使用环境变量存储密码',
                severity='warning'
            ))
        
        # 超时范围
        timeout = config.get('runtime', {}).get('timeout', 30)
        if timeout < 5 or timeout > 300:
            self.errors.append(ValidationError(
                field='runtime.timeout',
                message='超时时间应在5-300秒之间',
                severity='warning'
            ))
        
        return self.errors
    
    def _check_required(self, config: dict, path: str, name: str):
        """检查必填字段"""
        keys = path.split('.')
        value = config
        for key in keys:
            value = value.get(key, {}) if isinstance(value, dict) else {}
        
        if not value:
            self.errors.append(ValidationError(
                field=path,
                message=f'{name}不能为空',
                severity='error'
            ))
    
    def _is_valid_url(self, url: str) -> bool:
        """URL格式校验"""
        pattern = r'^https?://[\w\-.]+(:\d+)?(/[\w\-./]*)?$'
        return bool(re.match(pattern, url))
```

---

## 五、备份恢复方案

### 5.1 自动备份脚本

```bash
#!/bin/bash
# scripts/backup.sh
# cosmic-replay-v2 备份脚本

set -e

# 配置
BACKUP_DIR=${BACKUP_DIR:-"/data/backups"}
RETENTION_DAYS=${RETENTION_DAYS:-30}
APP_DIR="/app"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_NAME="cosmic-replay-${DATE}"

# 创建备份目录
mkdir -p "${BACKUP_DIR}"

echo "开始备份: ${DATE}"

# 备份数据目录
tar -czf "${BACKUP_DIR}/${BACKUP_NAME}-data.tar.gz" \
    -C "${APP_DIR}" \
    cases/ \
    config/ \
    --exclude='*.log' \
    --exclude='*.pyc'

# 备份日志（压缩）
if [ -d "${APP_DIR}/logs" ]; then
    tar -czf "${BACKUP_DIR}/${BACKUP_NAME}-logs.tar.gz" \
        -C "${APP_DIR}" logs/
fi

# 计算校验和
sha256sum "${BACKUP_DIR}"/*.tar.gz > "${BACKUP_DIR}/${BACKUP_NAME}.sha256"

# 上传到远程存储 (支持S3/OSS等)
if [ -n "${S3_BUCKET}" ]; then
    aws s3 sync "${BACKUP_DIR}/${BACKUP_NAME}"* "s3://${S3_BUCKET}/backups/"
fi

# 清理旧备份
find "${BACKUP_DIR}" -name "cosmic-replay-*.tar.gz" -mtime +${RETENTION_DAYS} -delete
find "${BACKUP_DIR}" -name "cosmic-replay-*.sha256" -mtime +${RETENTION_DAYS} -delete

echo "备份完成: ${BACKUP_NAME}"
```

### 5.2 恢复脚本

```bash
#!/bin/bash
# scripts/restore.sh
# cosmic-replay-v2 恢复脚本

set -e

BACKUP_FILE=${1:-}
APP_DIR=${APP_DIR:-"/app"}

if [ -z "${BACKUP_FILE}" ]; then
    echo "用法: $0 <backup_file.tar.gz>"
    echo "可用备份:"
    ls -lh /data/backups/*.tar.gz 2>/dev/null || echo "无可用备份"
    exit 1
fi

echo "警告: 恢复将覆盖现有数据!"
read -p "确认继续? (y/N): " confirm
if [ "${confirm}" != "y" ]; then
    echo "已取消"
    exit 0
fi

# 校验文件完整性
if [ -f "${BACKUP_FILE}.sha256" ]; then
    echo "校验备份完整性..."
    sha256sum -c "${BACKUP_FILE}.sha256" || {
        echo "备份文件校验失败!"
        exit 1
    }
fi

# 备份当前数据
CURRENT_BACKUP="/tmp/cosmic-replay-$(date +%s).tar.gz"
echo "备份当前数据到: ${CURRENT_BACKUP}"
tar -czf "${CURRENT_BACKUP}" -C "${APP_DIR}" cases/ config/ 2>/dev/null || true

# 恢复数据
echo "恢复数据..."
tar -xzf "${BACKUP_FILE}" -C "${APP_DIR}"

echo "恢复完成"
echo "如需回滚: tar -xzf ${CURRENT_BACKUP} -C ${APP_DIR}"
```

### 5.3 KubernetesCronJob备份

```yaml
# deploy/k8s/backup-cronjob.yaml
apiVersion: batch/v1
kind: CronJob
metadata:
  name: cosmic-replay-backup
  namespace: cosmic-replay
spec:
  schedule: "0 2 * * *"  # 每天凌晨2点
  concurrencyPolicy: Forbid
  successfulJobsHistoryLimit: 7
  failedJobsHistoryLimit: 3
  jobTemplate:
    spec:
      template:
        spec:
          serviceAccountName: cosmic-replay-backup
          containers:
            - name: backup
              image: cosmic-replay-v2:v2.0.0
              command:
                - /bin/bash
                - /app/scripts/backup.sh
              env:
                - name: BACKUP_DIR
                  value: "/backups"
                - name: S3_BUCKET
                  valueFrom:
                    secretKeyRef:
                      name: backup-config
                      key: s3-bucket
              volumeMounts:
                - name: data
                  mountPath: /app
                  readOnly: true
                - name: backups
                  mountPath: /backups
          volumes:
            - name: data
              persistentVolumeClaim:
                claimName: cosmic-replay-data
            - name: backups
              persistentVolumeClaim:
                claimName: cosmic-replay-backups
          restartPolicy: OnFailure
```

---

## 六、安全加固方案

### 6.1 API认证中间件

```python
# lib/security/auth.py
"""API认证模块"""

import os
import time
import hashlib
import secrets
from typing import Optional
from fastapi import HTTPException, Request, Depends
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from dataclasses import dataclass
from datetime import datetime, timedelta


@dataclass
class User:
    id: str
    name: str
    roles: list[str]
    api_key_hash: str
    created_at: datetime
    expires_at: Optional[datetime] = None


class AuthManager:
    """认证管理器"""
    
    def __init__(self):
        self._api_keys: dict[str, User] = {}
        self._session_tokens: dict[str, tuple[User, float]] = {}
        self._rate_limits: dict[str, list[float]] = {}
        self._load_keys()
    
    def _load_keys(self):
        """从环境变量或配置加载API Key"""
        # 管理员Key (从环境变量)
        admin_key = os.environ.get('ADMIN_API_KEY')
        if admin_key:
            key_hash = self._hash_key(admin_key)
            self._api_keys[key_hash] = User(
                id='admin',
                name='Administrator',
                roles=['admin', 'read', 'write'],
                api_key_hash=key_hash,
                created_at=datetime.now()
            )
    
    def _hash_key(self, key: str) -> str:
        """哈希API Key"""
        return hashlib.sha256(key.encode()).hexdigest()
    
    def verify_key(self, key: str) -> Optional[User]:
        """验证API Key"""
        key_hash = self._hash_key(key)
        return self._api_keys.get(key_hash)
    
    def create_session(self, user: User) -> str:
        """创建会话Token"""
        token = secrets.token_urlsafe(32)
        self._session_tokens[token] = (user, time.time() + 3600)  # 1小时
        return token
    
    def verify_session(self, token: str) -> Optional[User]:
        """验证会话Token"""
        if token not in self._session_tokens:
            return None
        user, expires = self._session_tokens[token]
        if time.time() > expires:
            del self._session_tokens[token]
            return None
        return user
    
    def check_rate_limit(self, client_id: str, limit: int = 100, window: int = 60) -> bool:
        """检查速率限制"""
        now = time.time()
        cutoff = now - window
        
        # 清理旧记录
        self._rate_limits[client_id] = [
            t for t in self._rate_limits.get(client_id, [])
            if t > cutoff
        ]
        
        if len(self._rate_limits[client_id]) >= limit:
            return False
        
        self._rate_limits[client_id].append(now)
        return True


# 全局认证管理器
auth_manager = AuthManager()

# Bearer认证方案
security = HTTPBearer(auto_error=False)


async def get_current_user(
    request: Request,
    credentials: Optional[HTTPAuthorizationCredentials] = Depends(security)
) -> User:
    """获取当前用户依赖"""
    
    # 获取客户端ID (IP + User-Agent)
    client_id = f"{request.client.host}:{request.headers.get('user-agent', '')}"
    
    # 速率限制检查
    if not auth_manager.check_rate_limit(client_id):
        raise HTTPException(
            status_code=429,
            detail="请求过于频繁，请稍后重试"
        )
    
    # 无认证信息
    if credentials is None:
        raise HTTPException(
            status_code=401,
            detail="缺少认证信息",
            headers={"WWW-Authenticate": "Bearer"}
        )
    
    # 验证Token
    user = auth_manager.verify_session(credentials.credentials)
    if user is None:
        # 尝试作为API Key验证
        user = auth_manager.verify_key(credentials.credentials)
    
    if user is None:
        raise HTTPException(
            status_code=401,
            detail="无效的认证凭据"
        )
    
    return user


def require_roles(*roles: str):
    """角色检查装饰器"""
    async def role_checker(user: User = Depends(get_current_user)):
        if not any(role in user.roles for role in roles):
            raise HTTPException(
                status_code=403,
                detail="权限不足"
            )
        return user
    return role_checker
```

### 6.2 安全头中间件

```python
# lib/security/headers.py
"""安全头中间件"""

from fastapi import Request
from starlette.middleware.base import BaseHTTPMiddleware


class SecurityHeadersMiddleware(BaseHTTPMiddleware):
    """添加安全响应头"""
    
    async def dispatch(self, request: Request, call_next):
        response = await call_next(request)
        
        # 安全相关响应头
        response.headers['X-Content-Type-Options'] = 'nosniff'
        response.headers['X-Frame-Options'] = 'DENY'
        response.headers['X-XSS-Protection'] = '1; mode=block'
        response.headers['Referrer-Policy'] = 'strict-origin-when-cross-origin'
        response.headers['Permissions-Policy'] = 'geolocation=(), microphone=(), camera=()'
        
        # CSP (生产环境需要调整)
        response.headers['Content-Security-Policy'] = (
            "default-src 'self'; "
            "script-src 'self' 'unsafe-inline'; "
            "style-src 'self' 'unsafe-inline'; "
            "img-src 'self' data:; "
            "connect-src 'self'"
        )
        
        # HSTS (需要HTTPS)
        # response.headers['Strict-Transport-Security'] = 'max-age=31536000; includeSubDomains'
        
        return response
```

### 6.3 审计日志

```python
# lib/security/audit.py
"""审计日志模块"""

import json
import time
from datetime import datetime
from pathlib import Path
from typing import Any, Optional
from dataclasses import dataclass, asdict
import threading


@dataclass
class AuditEvent:
    timestamp: str
    event_type: str
    user: str
    action: str
    resource: str
    ip_address: str
    user_agent: str
    details: dict
    result: str  # 'success' | 'failure'


class AuditLogger:
    """审计日志记录器"""
    
    def __init__(self, log_dir: str = './logs/audit'):
        self.log_dir = Path(log_dir)
        self.log_dir.mkdir(parents=True, exist_ok=True)
        self._lock = threading.Lock()
    
    def log(
        self,
        event_type: str,
        user: str,
        action: str,
        resource: str,
        ip_address: str,
        user_agent: str,
        details: dict = None,
        result: str = 'success'
    ):
        """记录审计事件"""
        event = AuditEvent(
            timestamp=datetime.utcnow().isoformat() + 'Z',
            event_type=event_type,
            user=user,
            action=action,
            resource=resource,
            ip_address=ip_address,
            user_agent=user_agent,
            details=details or {},
            result=result
        )
        
        with self._lock:
            # 写入当天文件
            log_file = self.log_dir / f"audit-{datetime.now().strftime('%Y%m%d')}.jsonl"
            with open(log_file, 'a', encoding='utf-8') as f:
                f.write(json.dumps(asdict(event), ensure_ascii=False) + '\n')
    
    def log_case_run(self, user: str, case_name: str, env: str, ip: str, result: str):
        """记录用例执行"""
        self.log(
            event_type='case_run',
            user=user,
            action='execute',
            resource=f'case:{case_name}',
            ip_address=ip,
            user_agent='',
            details={'env': env},
            result=result
        )
    
    def log_config_change(self, user: str, config_type: str, config_id: str, changes: dict, ip: str):
        """记录配置变更"""
        self.log(
            event_type='config_change',
            user=user,
            action='update',
            resource=f'{config_type}:{config_id}',
            ip_address=ip,
            user_agent='',
            details={'changes': changes},
            result='success'
        )


# 全局审计日志器
audit_logger = AuditLogger()
```

---

## 七、灾备与高可用

### 7.1 多区域部署架构

```
                    ┌─────────────────────────────────────────────────────┐
                    │                    DNS / Route53                     │
                    │              cosmic-replay.example.com               │
                    └─────────────────────────────────────────────────────┘
                                              │
                    ┌─────────────────────────┼─────────────────────────┐
                    │                         │                         │
                    ▼                         ▼                         ▼
            ┌───────────────┐         ┌───────────────┐         ┌───────────────┐
            │   Region A    │         │   Region B    │         │   Region C    │
            │  (Primary)    │         │  (Standby)    │         │  (Standby)    │
            ├───────────────┤         ├───────────────┤         ├───────────────┤
            │  Ingress      │         │  Ingress      │         │  Ingress      │
            │  LoadBalancer │         │  LoadBalancer │         │  LoadBalancer │
            ├───────────────┤         ├───────────────┤         ├───────────────┤
            │  cosmic-replay│         │  cosmic-replay│         │  cosmic-replay│
            │  (2 replicas) │         │  (1 replica)  │         │  (1 replica)  │
            ├───────────────┤         ├───────────────┤         ├───────────────┤
            │  PostgreSQL   │◄───────►│  PostgreSQL   │◄───────►│  PostgreSQL   │
            │  (Primary)    │  Sync   │  (Replica)    │  Async  │  (Replica)    │
            ├───────────────┤         ├───────────────┤         ├───────────────┤
            │  MinIO/S3     │◄───────►│  MinIO/S3     │◄───────►│  MinIO/S3     │
            │  (Primary)    │  Repl   │  (Replica)    │  Repl   │  (Replica)    │
            └───────────────┘         └───────────────┘         └───────────────┘
```

### 7.2 故障切换流程

```yaml
# deploy/k8s/failover-config.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: failover-config
  namespace: cosmic-replay
data:
  FAIL_OVER_ENABLED: "true"
  PRIMARY_REGION: "region-a"
  STANDBY_REGIONS: "region-b,region-c"
  HEALTH_CHECK_INTERVAL: "10s"
  FAILOVER_THRESHOLD: "3"  # 连续3次失败触发切换
  MANUAL_APPROVAL: "true"  # 需要人工确认
```

---

*文档版本: 1.0*
*最后更新: 2026-04-28*
