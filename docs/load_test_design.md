# cosmic-replay-v2 压测设计方案

## 1. 压测目标与范围

### 1.1 测试目标

1. **性能基线建立**: 确定系统在正常负载下的性能指标
2. **瓶颈识别**: 发现系统在高负载下的性能瓶颈
3. **容量规划**: 确定系统能支持的最大并发用户数和用例数
4. **稳定性验证**: 验证长时间运行的稳定性
5. **优化效果验证**: 对比优化前后的性能差异

### 1.2 测试范围

| 模块 | 测试场景 | 优先级 |
|------|----------|--------|
| 用例加载API | GET /api/cases | P0 |
| 用例执行引擎 | 单用例/批量执行 | P0 |
| SSE推送 | 实时事件流 | P0 |
| HTTP连接池 | 并发请求 | P1 |
| 前端渲染 | 大列表渲染 | P1 |
| 内存管理 | 长期运行稳定性 | P1 |

### 1.3 测试环境

```yaml
# 压测环境配置
hardware:
  cpu: 4核
  memory: 8GB
  disk: SSD
  
software:
  os: Linux/macOS
  python: 3.11+
  node: 18+ (前端压测)

network:
  latency: <10ms (内网)
  bandwidth: 1Gbps

target_service:
  cosmic_base_url: ${COSMIC_BASE_URL}
  auth: 环境变量注入
```

---

## 2. 压测工具选型

### 2.1 后端压测工具

#### locust (推荐)

```python
# scripts/loadtest/locustfile.py

from locust import HttpUser, task, between, events
import json
import time

class CosmicReplayUser(HttpUser):
    """cosmic-replay 压测用户"""
    
    wait_time = between(1, 3)
    host = "http://127.0.0.1:8765"
    
    def on_start(self):
        """用户开始时执行"""
        self.client.get("/api/info")
    
    @task(10)
    def list_cases(self):
        """测试用例列表API"""
        with self.client.get("/api/cases", catch_response=True) as resp:
            if resp.status_code == 200:
                data = resp.json()
                if len(data) >= 0:
                    resp.success()
                else:
                    resp.failure("Empty cases list")
            else:
                resp.failure(f"Status {resp.status_code}")
    
    @task(5)
    def get_case_yaml(self):
        """测试用例YAML获取"""
        # 先获取用例列表
        resp = self.client.get("/api/cases")
        cases = resp.json()
        if cases:
            case_name = cases[0]["name"]
            self.client.get(f"/api/cases/{case_name}/yaml")
    
    @task(3)
    def run_single_case(self):
        """测试单用例执行"""
        # 获取用例列表
        resp = self.client.get("/api/cases")
        cases = resp.json()
        if not cases:
            return
        
        case_name = cases[0]["name"]
        
        # 触发执行
        run_resp = self.client.post(
            f"/api/cases/{case_name}/run",
            json={"env_id": "sit"}
        )
        if run_resp.status_code != 200:
            return
        
        run_id = run_resp.json()["run_id"]
        
        # SSE订阅(模拟)
        # 注意: locust对SSE支持有限，此处仅验证连接建立
        self.client.get(f"/api/runs/{run_id}/events")

    @task(1)
    def batch_execute(self):
        """测试批量执行"""
        resp = self.client.get("/api/cases")
        cases = resp.json()
        if len(cases) < 2:
            return
        
        # 创建批量任务
        task_resp = self.client.post("/api/tasks", json={
            "case_names": [c["name"] for c in cases[:3]],
            "env_id": "sit",
            "name": "压测批量执行"
        })
        
        if task_resp.status_code == 200:
            task_id = task_resp.json()["task_id"]
            # 启动任务
            self.client.post(f"/api/tasks/{task_id}/start")


class HeavyUser(HttpUser):
    """重负载用户 - 测试高并发"""
    
    wait_time = between(0.1, 0.5)
    weight = 1
    
    @task
    def stress_list_cases(self):
        """高频访问用例列表"""
        self.client.get("/api/cases")


# 自定义事件监听
@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    print("压测开始...")

@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    print("压测结束.")
```

#### 压测启动脚本

```bash
#!/bin/bash
# scripts/loadtest/run_locust.sh

# 快速测试 - 验证功能
locust -f scripts/loadtest/locustfile.py \
    --headless \
    --users 10 \
    --spawn-rate 5 \
    --run-time 60s \
    --host http://127.0.0.1:8765 \
    --html reports/locust_quick.html

# 负载测试 - 模拟正常负载
locust -f scripts/loadtest/locustfile.py \
    --headless \
    --users 50 \
    --spawn-rate 10 \
    --run-time 300s \
    --host http://127.0.0.1:8765 \
    --html reports/locust_load.html

# 压力测试 - 寻找极限
locust -f scripts/loadtest/locustfile.py \
    --headless \
    --users 200 \
    --spawn-rate 20 \
    --run-time 600s \
    --host http://127.0.0.1:8765 \
    --html reports/locust_stress.html

# 阶梯测试 - 逐步增加负载
locust -f scripts/loadtest/locustfile.py \
    --headless \
    --users 100 \
    --spawn-rate 2 \
    --run-time 900s \
    --host http://127.0.0.1:8765 \
    --step-time 60s \
    --step-users 10 \
    --html reports/locust_step.html
```

### 2.2 SSE压测工具

```python
# scripts/loadtest/sse_stress.py

import asyncio
import aiohttp
import time
from dataclasses import dataclass
from typing import Optional
import argparse

@dataclass
class SSEMetrics:
    connect_time: float = 0
    first_event_time: float = 0
    event_count: int = 0
    total_time: float = 0
    errors: int = 0

async def test_sse_connection(session: aiohttp.ClientSession, 
                               base_url: str, 
                               run_id: str,
                               metrics: SSEMetrics) -> None:
    """测试单个SSE连接"""
    url = f"{base_url}/api/runs/{run_id}/events"
    
    start = time.time()
    try:
        async with session.get(url) as resp:
            metrics.connect_time = time.time() - start
            
            if resp.status != 200:
                metrics.errors += 1
                return
            
            async for line in resp.content:
                if line:
                    if metrics.first_event_time == 0:
                        metrics.first_event_time = time.time() - start
                    metrics.event_count += 1
                    
    except Exception as e:
        metrics.errors += 1
    finally:
        metrics.total_time = time.time() - start

async def run_sse_stress(base_url: str, 
                         concurrent: int = 10,
                         duration: int = 60):
    """SSE压测主函数"""
    metrics_list = [SSEMetrics() for _ in range(concurrent)]
    
    # 先创建一个执行任务
    async with aiohttp.ClientSession() as session:
        # 获取用例列表
        async with session.get(f"{base_url}/api/cases") as resp:
            cases = await resp.json()
        
        if not cases:
            print("没有可用的用例")
            return
        
        case_name = cases[0]["name"]
        
        # 触发执行
        async with session.post(
            f"{base_url}/api/cases/{case_name}/run",
            json={"env_id": "sit"}
        ) as resp:
            run_data = await resp.json()
            run_id = run_data["run_id"]
        
        print(f"开始SSE压测: {concurrent}并发, run_id={run_id}")
        
        # 并发连接SSE
        tasks = [
            test_sse_connection(session, base_url, run_id, m)
            for m in metrics_list
        ]
        
        await asyncio.gather(*tasks)
    
    # 统计结果
    print("\n===== SSE压测结果 =====")
    print(f"并发数: {concurrent}")
    print(f"成功率: {sum(1 for m in metrics_list if m.errors == 0) / concurrent * 100:.1f}%")
    print(f"平均连接时间: {sum(m.connect_time for m in metrics_list) / concurrent * 1000:.1f}ms")
    
    first_events = [m.first_event_time for m in metrics_list if m.first_event_time > 0]
    if first_events:
        print(f"首事件延迟(平均): {sum(first_events) / len(first_events) * 1000:.1f}ms")
        print(f"首事件延迟(P99): {sorted(first_events)[int(len(first_events) * 0.99)] * 1000:.1f}ms")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="SSE压测工具")
    parser.add_argument("--host", default="http://127.0.0.1:8765")
    parser.add_argument("--concurrent", type=int, default=10)
    parser.add_argument("--duration", type=int, default=60)
    args = parser.parse_args()
    
    asyncio.run(run_sse_stress(args.host, args.concurrent, args.duration))
```

### 2.3 前端压测工具

```javascript
// scripts/loadtest/frontend_stress.js

const puppeteer = require('puppeteer');
const { performance } = require('perf_hooks');

async function measurePageLoad(url) {
    const browser = await puppeteer.launch({
        headless: 'new',
        args: ['--no-sandbox']
    });
    
    const page = await browser.newPage();
    
    // 收集性能指标
    await page.evaluateOnNewDocument(() => {
        window.performanceMetrics = {};
    });
    
    const start = performance.now();
    await page.goto(url, { waitUntil: 'networkidle0' });
    const loadTime = performance.now() - start;
    
    // 获取性能指标
    const metrics = await page.metrics();
    
    // 测试列表渲染
    await page.waitForSelector('table tbody tr');
    const renderTime = performance.now() - start;
    
    await browser.close();
    
    return {
        loadTime,
        renderTime,
        ...metrics
    };
}

async function stressTest(url, iterations = 10) {
    const results = [];
    
    for (let i = 0; i < iterations; i++) {
        console.log(`迭代 ${i + 1}/${iterations}`);
        const metrics = await measurePageLoad(url);
        results.push(metrics);
        
        // 等待一下避免过载
        await new Promise(r => setTimeout(r, 1000));
    }
    
    // 计算统计
    const avgLoad = results.reduce((s, r) => s + r.loadTime, 0) / results.length;
    const avgRender = results.reduce((s, r) => s + r.renderTime, 0) / results.length;
    
    console.log('\n===== 前端压测结果 =====');
    console.log(`平均加载时间: ${avgLoad.toFixed(0)}ms`);
    console.log(`平均渲染时间: ${avgRender.toFixed(0)}ms`);
    console.log(`P99加载时间: ${results.sort((a, b) => a.loadTime - b.loadTime)[Math.floor(results.length * 0.99)].loadTime.toFixed(0)}ms`);
}

// 运行压测
stressTest('http://127.0.0.1:8765/', 20);
```

---

## 3. 压测场景设计

### 3.1 用例加载API压测

```yaml
# scripts/loadtest/scenarios/case_loading.yaml

name: 用例加载API压测
description: 测试GET /api/cases的性能

scenarios:
  - name: 冷启动测试
    description: 服务启动后首次请求
    steps:
      - action: restart_service
      - action: wait
        duration: 5s
      - action: http_request
        method: GET
        path: /api/cases
        expect:
          status: 200
          latency_p99: <500ms
    repeat: 5

  - name: 缓存命中测试
    description: 连续请求验证缓存效果
    steps:
      - action: http_request
        method: GET
        path: /api/cases
      - action: wait
        duration: 100ms
      - action: http_request
        method: GET
        path: /api/cases
        expect:
          latency_p99: <50ms  # 缓存命中应该很快
    repeat: 10

  - name: 并发读取测试
    description: 测试并发请求性能
    steps:
      - action: concurrent_requests
        count: 20
        method: GET
        path: /api/cases
        expect:
          success_rate: ">99%"
          latency_p99: "<500ms"
    repeat: 5

  - name: 大用例集测试
    description: 测试大量用例时的性能
    setup:
      - action: create_cases
        count: 100
    steps:
      - action: http_request
        method: GET
        path: /api/cases
        expect:
          latency_p99: <1000ms
    teardown:
      - action: delete_created_cases
```

### 3.2 执行引擎压测

```yaml
# scripts/loadtest/scenarios/execution.yaml

name: 用例执行引擎压测
description: 测试用例执行的并发性能

scenarios:
  - name: 单用例执行延迟
    description: 测试单个用例执行时间
    steps:
      - action: run_case
        case: 入职一个人并检查人员列表这个人入职无误
        env: sit
        expect:
          duration: <60s
          success: true
    repeat: 3

  - name: 并发执行测试
    description: 测试多个用例并发执行
    steps:
      - action: run_cases_concurrent
        cases:
          - 入职一个人并检查人员列表这个人入职无误
          - 新增一条行政组织
          - 业务模型扩展一个二开基础资料附表
        concurrent: 3
        env: sit
        expect:
          all_success: true
          max_duration: <300s
    repeat: 2

  - name: 批量执行吞吐测试
    description: 测试批量执行的吞吐量
    steps:
      - action: create_batch_task
        cases:
          - 入职一个人并检查人员列表这个人入职无误
          - 新增一条行政组织
        repeat: 5  # 重复5次
        env: sit
      - action: wait_task_complete
        timeout: 600s
      - action: check_results
        expect:
          throughput: ">6 cases/hour"  # 目标吞吐量
    repeat: 1

  - name: 长时间稳定性测试
    description: 测试长时间运行的稳定性
    steps:
      - action: run_case_loop
        case: 入职一个人并检查人员列表这个人入职无误
        iterations: 50
        interval: 30s
        env: sit
        expect:
          success_rate: ">95%"
          memory_growth: "<100MB"
    repeat: 1
```

### 3.3 SSE推送压测

```yaml
# scripts/loadtest/scenarios/sse.yaml

name: SSE推送性能压测
description: 测试实时事件推送的性能

scenarios:
  - name: 单连接延迟测试
    description: 测试单个SSE连接的事件延迟
    steps:
      - action: start_case_run
        case: 入职一个人并检查人员列表这个人入职无误
      - action: connect_sse
        expect:
          first_event_latency: <100ms
          event_order_correct: true
    repeat: 5

  - name: 多连接并发测试
    description: 测试多个客户端同时订阅
    steps:
      - action: start_case_run
        case: 业务模型扩展一个二开基础资料附表  # 长用例
      - action: connect_sse_concurrent
        count: 20
        expect:
          all_connected: true
          max_first_event_latency: <500ms
    repeat: 3

  - name: 长连接稳定性测试
    description: 测试SSE长连接稳定性
    steps:
      - action: connect_sse
        duration: 300s  # 5分钟
        expect:
          keepalive_received: true
          no_disconnect: true
    repeat: 2

  - name: 断线重连测试
    description: 测试客户端断线重连
    steps:
      - action: connect_sse
      - action: wait
        duration: 10s
      - action: disconnect
      - action: wait
        duration: 2s
      - action: reconnect
        expect:
          reconnect_success: true
          event_continuity: true
    repeat: 3
```

### 3.4 内存稳定性压测

```yaml
# scripts/loadtest/scenarios/memory.yaml

name: 内存稳定性压测
description: 测试内存使用和泄漏

scenarios:
  - name: 内存增长测试
    description: 监控执行过程中的内存增长
    steps:
      - action: record_memory
        checkpoint: start
      - action: run_cases
        cases:
          - 入职一个人并检查人员列表这个人入职无误
          - 新增一条行政组织
          - 业务模型扩展一个二开基础资料附表
        iterations: 10
      - action: record_memory
        checkpoint: end
      - action: check_memory_growth
        expect:
          growth: "<100MB"
    repeat: 3

  - name: 内存泄漏检测
    description: 长期运行检测内存泄漏
    steps:
      - action: start_memory_monitor
        interval: 10s
      - action: run_case_loop
        case: 入职一个人并检查人员列表这个人入职无误
        iterations: 100
        interval: 5s
      - action: stop_memory_monitor
      - action: analyze_memory_trend
        expect:
          trend: "stable"  # 内存应该稳定，不持续增长
    repeat: 1

  - name: 大响应处理测试
    description: 测试大响应对象的内存管理
    setup:
      - action: create_case_with_large_response
        response_size: 1MB
    steps:
      - action: run_case
        case: large_response_case
      - action: check_memory
        expect:
          peak: "<200MB"
          after_gc: "<50MB"
    teardown:
      - action: delete_case
        case: large_response_case
```

---

## 4. 压测执行脚本

### 4.1 一键压测脚本

```bash
#!/bin/bash
# scripts/loadtest/run_all.sh

set -e

BASE_URL="${BASE_URL:-http://127.0.0.1:8765}"
REPORT_DIR="reports/loadtest_$(date +%Y%m%d_%H%M%S)"
mkdir -p "$REPORT_DIR"

echo "========================================="
echo "cosmic-replay-v2 压测套件"
echo "目标: $BASE_URL"
echo "报告目录: $REPORT_DIR"
echo "========================================="

# 1. 环境检查
echo "[1/6] 检查环境..."
python -c "import locust" || pip install locust
curl -s "$BASE_URL/api/health" || {
    echo "错误: 服务未启动或不可访问"
    exit 1
}

# 2. 用例加载API压测
echo "[2/6] 用例加载API压测..."
locust -f scripts/loadtest/locustfile.py \
    --headless \
    --users 50 \
    --spawn-rate 10 \
    --run-time 120s \
    --host "$BASE_URL" \
    --html "$REPORT_DIR/case_loading.html" \
    --only-summary \
    CosmicReplayUser

# 3. 执行引擎压测
echo "[3/6] 执行引擎压测..."
python scripts/loadtest/execution_stress.py \
    --host "$BASE_URL" \
    --report "$REPORT_DIR/execution.json"

# 4. SSE压测
echo "[4/6] SSE推送压测..."
python scripts/loadtest/sse_stress.py \
    --host "$BASE_URL" \
    --concurrent 10 \
    --report "$REPORT_DIR/sse.json"

# 5. 内存稳定性测试
echo "[5/6] 内存稳定性测试..."
python scripts/loadtest/memory_monitor.py \
    --host "$BASE_URL" \
    --duration 300 \
    --report "$REPORT_DIR/memory.json"

# 6. 生成汇总报告
echo "[6/6] 生成汇总报告..."
python scripts/loadtest/generate_report.py \
    --input "$REPORT_DIR" \
    --output "$REPORT_DIR/summary.html"

echo "========================================="
echo "压测完成! 报告位于: $REPORT_DIR"
echo "========================================="
```

### 4.2 执行引擎压测脚本

```python
# scripts/loadtest/execution_stress.py

import argparse
import json
import time
import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
from dataclasses import dataclass
from typing import Optional
import statistics

@dataclass
class ExecutionResult:
    case_name: str
    success: bool
    duration_s: float
    error: Optional[str] = None

def run_single_case(base_url: str, case_name: str, env_id: str) -> ExecutionResult:
    """执行单个用例"""
    start = time.time()
    
    try:
        # 触发执行
        resp = requests.post(
            f"{base_url}/api/cases/{case_name}/run",
            json={"env_id": env_id},
            timeout=10
        )
        resp.raise_for_status()
        run_id = resp.json()["run_id"]
        
        # 等待完成 (简化: 轮询)
        for _ in range(300):  # 最多5分钟
            time.sleep(1)
            history = requests.get(f"{base_url}/api/history").json()
            for record in history:
                if record.get("run_id") == run_id and record.get("passed") is not None:
                    return ExecutionResult(
                        case_name=case_name,
                        success=record["passed"],
                        duration_s=time.time() - start
                    )
        
        return ExecutionResult(
            case_name=case_name,
            success=False,
            duration_s=time.time() - start,
            error="Timeout waiting for completion"
        )
        
    except Exception as e:
        return ExecutionResult(
            case_name=case_name,
            success=False,
            duration_s=time.time() - start,
            error=str(e)
        )

def run_stress_test(base_url: str, 
                    cases: list[str], 
                    env_id: str,
                    concurrent: int,
                    iterations: int) -> dict:
    """运行压测"""
    results = []
    
    # 获取可用用例
    if not cases:
        resp = requests.get(f"{base_url}/api/cases")
        all_cases = [c["name"] for c in resp.json()]
        cases = all_cases[:3]  # 取前3个
    
    print(f"用例: {cases}")
    print(f"并发数: {concurrent}")
    print(f"迭代次数: {iterations}")
    
    with ThreadPoolExecutor(max_workers=concurrent) as executor:
        futures = []
        for i in range(iterations):
            for case in cases:
                futures.append(
                    executor.submit(run_single_case, base_url, case, env_id)
                )
        
        for i, future in enumerate(as_completed(futures)):
            result = future.result()
            results.append(result)
            print(f"完成 {i+1}/{len(futures)}: {result.case_name} - "
                  f"{'PASS' if result.success else 'FAIL'} ({result.duration_s:.1f}s)")
    
    # 统计
    durations = [r.duration_s for r in results]
    success_count = sum(1 for r in results if r.success)
    
    return {
        "total": len(results),
        "success": success_count,
        "fail": len(results) - success_count,
        "success_rate": success_count / len(results) * 100,
        "duration": {
            "avg": statistics.mean(durations),
            "min": min(durations),
            "max": max(durations),
            "p50": statistics.median(durations),
            "p99": sorted(durations)[int(len(durations) * 0.99)] if len(durations) > 10 else max(durations),
        },
        "throughput": len(results) / sum(durations) * 3600,  # cases/hour
    }

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--host", default="http://127.0.0.1:8765")
    parser.add_argument("--env", default="sit")
    parser.add_argument("--concurrent", type=int, default=3)
    parser.add_argument("--iterations", type=int, default=5)
    parser.add_argument("--report", default="execution_report.json")
    args = parser.parse_args()
    
    print("===== 执行引擎压测 =====")
    
    report = run_stress_test(
        base_url=args.host,
        cases=[],
        env_id=args.env,
        concurrent=args.concurrent,
        iterations=args.iterations
    )
    
    with open(args.report, "w") as f:
        json.dump(report, f, indent=2)
    
    print("\n===== 压测结果 =====")
    print(f"总执行数: {report['total']}")
    print(f"成功率: {report['success_rate']:.1f}%")
    print(f"平均耗时: {report['duration']['avg']:.1f}s")
    print(f"P99耗时: {report['duration']['p99']:.1f}s")
    print(f"吞吐量: {report['throughput']:.1f} cases/hour")
    print(f"报告已保存: {args.report}")

if __name__ == "__main__":
    main()
```

### 4.3 内存监控脚本

```python
# scripts/loadtest/memory_monitor.py

import argparse
import json
import time
import requests
import psutil
import threading
from dataclasses import dataclass
from typing import Optional

@dataclass
class MemorySnapshot:
    timestamp: float
    rss_mb: float
    vms_mb: float
    percent: float
    active_runs: int

class MemoryMonitor:
    def __init__(self, base_url: str, interval: float = 5.0):
        self.base_url = base_url
        self.interval = interval
        self.snapshots: list[MemorySnapshot] = []
        self._stop_event = threading.Event()
        self._thread: Optional[threading.Thread] = None
    
    def start(self):
        self._thread = threading.Thread(target=self._monitor_loop, daemon=True)
        self._thread.start()
    
    def stop(self):
        self._stop_event.set()
        if self._thread:
            self._thread.join()
    
    def _monitor_loop(self):
        process = psutil.Process()
        
        while not self._stop_event.is_set():
            try:
                mem_info = process.memory_info()
                
                # 获取活跃运行数
                resp = requests.get(f"{self.base_url}/api/runs")
                active_runs = len(resp.json()) if resp.status_code == 200 else 0
                
                snapshot = MemorySnapshot(
                    timestamp=time.time(),
                    rss_mb=mem_info.rss / 1024 / 1024,
                    vms_mb=mem_info.vms / 1024 / 1024,
                    percent=process.memory_percent(),
                    active_runs=active_runs
                )
                self.snapshots.append(snapshot)
                
            except Exception as e:
                print(f"监控错误: {e}")
            
            time.sleep(self.interval)
    
    def get_report(self) -> dict:
        if not self.snapshots:
            return {}
        
        rss_values = [s.rss_mb for s in self.snapshots]
        
        return {
            "duration_s": self.snapshots[-1].timestamp - self.snapshots[0].timestamp,
            "snapshots": len(self.snapshots),
            "memory": {
                "start_mb": rss_values[0],
                "end_mb": rss_values[-1],
                "min_mb": min(rss_values),
                "max_mb": max(rss_values),
                "avg_mb": sum(rss_values) / len(rss_values),
                "growth_mb": rss_values[-1] - rss_values[0],
            }
        }

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--host", default="http://127.0.0.1:8765")
    parser.add_argument("--duration", type=int, default=300)
    parser.add_argument("--report", default="memory_report.json")
    args = parser.parse_args()
    
    print(f"===== 内存监控 (持续{args.duration}秒) =====")
    
    monitor = MemoryMonitor(args.host)
    monitor.start()
    
    try:
        time.sleep(args.duration)
    except KeyboardInterrupt:
        print("\n提前终止")
    
    monitor.stop()
    report = monitor.get_report()
    
    with open(args.report, "w") as f:
        json.dump(report, f, indent=2)
    
    print("\n===== 监控结果 =====")
    print(f"监控时长: {report['duration_s']:.0f}秒")
    print(f"内存起始: {report['memory']['start_mb']:.1f}MB")
    print(f"内存结束: {report['memory']['end_mb']:.1f}MB")
    print(f"内存增长: {report['memory']['growth_mb']:.1f}MB")
    print(f"内存峰值: {report['memory']['max_mb']:.1f}MB")
    
    # 判断是否存在内存泄漏风险
    growth_rate = report['memory']['growth_mb'] / (report['duration_s'] / 60)
    if growth_rate > 10:  # 每分钟增长超过10MB
        print(f"\n警告: 内存增长过快 ({growth_rate:.1f}MB/min)，可能存在内存泄漏!")
    else:
        print(f"\n内存增长正常 ({growth_rate:.1f}MB/min)")

if __name__ == "__main__":
    main()
```

---

## 5. 结果分析与报告

### 5.1 关键指标定义

| 指标 | 定义 | 目标值 |
|------|------|--------|
| P50延迟 | 50%请求的响应时间 | <100ms |
| P99延迟 | 99%请求的响应时间 | <500ms |
| 成功率 | 成功请求数/总请求数 | >99% |
| 吞吐量 | 单位时间处理的用例数 | >10 cases/hour |
| 内存增长 | 单位时间内存增量 | <5MB/hour |
| 错误率 | 失败请求数/总请求数 | <1% |

### 5.2 性能基线

```yaml
# 压测通过的基线标准

api_response:
  cases_list:
    p50: 50ms
    p99: 200ms
    success_rate: 99.9%
  
execution:
  single_case:
    p50: 30s
    p99: 60s
    success_rate: 95%
  
  batch_10_cases:
    total_time: <300s
    throughput: ">12 cases/hour"

sse:
  first_event_latency:
    p50: 50ms
    p99: 200ms
  
  connection_success_rate: 99%

memory:
  stable_growth: "<5MB/hour"
  peak_usage: "<500MB"
  gc_overhead: "<5%"
```

### 5.3 报告模板

```html
<!-- reports/template.html -->
<!DOCTYPE html>
<html>
<head>
    <title>cosmic-replay 压测报告</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <h1>cosmic-replay-v2 压测报告</h1>
    <p>生成时间: {{ timestamp }}</p>
    
    <h2>1. 执行摘要</h2>
    <table>
        <tr><th>指标</th><th>目标</th><th>实际</th><th>状态</th></tr>
        <tr><td>API响应时间(P99)</td><td><200ms</td><td>{{ api_p99 }}ms</td><td>{{ status }}</td></tr>
        <tr><td>执行成功率</td><td>>95%</td><td>{{ success_rate }}%</td><td>{{ status }}</td></tr>
        <tr><td>吞吐量</td><td>>10 cases/h</td><td>{{ throughput }} cases/h</td><td>{{ status }}</td></tr>
        <tr><td>内存增长</td><td><5MB/h</td><td>{{ mem_growth }} MB/h</td><td>{{ status }}</td></tr>
    </table>
    
    <h2>2. 详细结果</h2>
    <!-- 图表和数据 -->
</body>
</html>
```

---

## 6. 持续集成

### 6.1 CI配置

```yaml
# .github/workflows/performance_test.yml

name: Performance Test

on:
  schedule:
    - cron: '0 2 * * 0'  # 每周日凌晨2点
  workflow_dispatch:  # 手动触发

jobs:
  performance-test:
    runs-on: ubuntu-latest
    
    services:
      cosmic-replay:
        image: cosmic-replay:latest
        ports:
          - 8765:8765
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Setup Python
        uses: actions/setup-python@v5
        with:
          python-version: '3.11'
      
      - name: Install dependencies
        run: |
          pip install -r requirements.txt
          pip install locust psutil aiohttp
      
      - name: Wait for service
        run: |
          for i in {1..30}; do
            curl -s http://localhost:8765/api/health && break
            sleep 1
          done
      
      - name: Run performance tests
        run: |
          chmod +x scripts/loadtest/run_all.sh
          ./scripts/loadtest/run_all.sh
        env:
          BASE_URL: http://localhost:8765
      
      - name: Upload reports
        uses: actions/upload-artifact@v4
        with:
          name: performance-reports
          path: reports/
      
      - name: Check thresholds
        run: |
          python scripts/loadtest/check_thresholds.py \
            --report reports/summary.json \
            --threshold thresholds.yaml
```

### 6.2 阈值检查

```python
# scripts/loadtest/check_thresholds.py

import json
import yaml
import argparse
import sys

def check_thresholds(report_path: str, threshold_path: str) -> bool:
    """检查性能指标是否达到阈值"""
    
    with open(report_path) as f:
        report = json.load(f)
    
    with open(threshold_path) as f:
        thresholds = yaml.safe_load(f)
    
    passed = True
    
    # 检查API响应时间
    if report.get("api_p99", 999) > thresholds["api"]["p99_max_ms"]:
        print(f"FAIL: API P99 {report['api_p99']}ms > {thresholds['api']['p99_max_ms']}ms")
        passed = False
    
    # 检查成功率
    if report.get("success_rate", 0) < thresholds["execution"]["success_rate_min"]:
        print(f"FAIL: Success rate {report['success_rate']}% < {thresholds['execution']['success_rate_min']}%")
        passed = False
    
    # 检查吞吐量
    if report.get("throughput", 0) < thresholds["execution"]["throughput_min"]:
        print(f"FAIL: Throughput {report['throughput']} < {thresholds['execution']['throughput_min']}")
        passed = False
    
    # 检查内存
    if report.get("mem_growth_rate", 999) > thresholds["memory"]["growth_max_mb_per_hour"]:
        print(f"FAIL: Memory growth {report['mem_growth_rate']} MB/h > {thresholds['memory']['growth_max_mb_per_hour']} MB/h")
        passed = False
    
    return passed

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--report", required=True)
    parser.add_argument("--threshold", required=True)
    args = parser.parse_args()
    
    if check_thresholds(args.report, args.threshold):
        print("All thresholds passed!")
        sys.exit(0)
    else:
        print("Some thresholds failed!")
        sys.exit(1)
```

---

*文档版本: 1.0*
*创建时间: 2026-04-28*
*作者: 性能架构师*
