# cosmic-replay-v4 性能架构深度分析报告

## 执行摘要

本报告针对 cosmic-replay-v4 项目进行六大维度的性能瓶颈分析，识别出**15个关键性能问题**，并提出分级优化方案。核心问题包括：用例加载无缓存、串行执行模型、SSE推送效率低、HTTP连接无复用、前端渲染性能瓶颈和内存管理不足。

---

## 1. 用例加载性能分析

### 1.1 当前实现分析

**文件位置**: `lib/webui/server.py:311-332` (`api_list_cases`)

```python
@APP.get("/api/cases")
def api_list_cases():
    items = []
    for p in list_case_files():  # 每次扫描目录
        name = case_name_from_path(p)
        meta = {...}
        try:
            case = load_yaml(p)  # 每次解析YAML
            if isinstance(case, dict):
                meta["step_count"] = len(case.get("steps", []))
                # ...
        except Exception as e:
            meta["parse_error"] = str(e)
        items.append(meta)
    return items
```

### 1.2 性能瓶颈识别

| 问题 | 严重度 | 影响 |
|------|--------|------|
| 每次GET /api/cases重新扫描目录 | P0 | O(n) I/O操作，用例多时延迟显著 |
| 每次请求解析所有YAML文件 | P0 | CPU密集，大文件(62KB)解析耗时100ms+ |
| 无缓存机制 | P0 | 重复计算，资源浪费 |
| 同步阻塞IO | P1 | 阻塞事件循环，影响并发 |

### 1.3 典型负载测算

```
3个用例: 44步 + 36步 + 209步
YAML文件大小: 11KB + 62KB + 10KB

当前耗时估算:
- 目录扫描: ~5ms
- YAML解析(同步): 
  - 11KB: ~20ms
  - 62KB: ~100ms (主要瓶颈)
  - 10KB: ~18ms
- 总计: ~143ms (单次请求)

如果用例数增长到50个(假设平均20KB):
- 预估耗时: 50 * 25ms = 1.25s
```

### 1.4 优化方案

#### 方案A: 内存缓存 + 文件监控 (推荐)

```python
# lib/webui/case_cache.py

import time
import threading
from pathlib import Path
from typing import Optional
from dataclasses import dataclass
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler

@dataclass
class CaseMeta:
    name: str
    file_path: Path
    mtime: float
    size: int
    display_name: str = ""
    description: str = ""
    tags: list = None
    step_count: int = 0
    main_form_id: str = ""
    parsed_at: float = 0
    
    def to_dict(self) -> dict:
        return {
            "name": self.name,
            "file": str(self.file_path),
            "size": self.size,
            "mtime": self.mtime,
            "display_name": self.display_name,
            "description": self.description,
            "tags": self.tags or [],
            "step_count": self.step_count,
            "main_form_id": self.main_form_id,
        }

class CaseCache:
    """用例元数据缓存，支持文件变更热更新"""
    
    def __init__(self, cases_dir: Path, refresh_interval: float = 30.0):
        self.cases_dir = cases_dir
        self.refresh_interval = refresh_interval
        self._cache: dict[str, CaseMeta] = {}
        self._lock = threading.RLock()
        self._last_refresh = 0
        self._observer: Optional[Observer] = None
        
    def start_watch(self):
        """启动文件监控"""
        if self._observer:
            return
        handler = _CaseFileHandler(self)
        self._observer = Observer()
        self._observer.schedule(handler, str(self.cases_dir), recursive=True)
        self._observer.start()
    
    def get_all(self, force_refresh: bool = False) -> list[dict]:
        """获取所有用例元数据"""
        now = time.time()
        
        with self._lock:
            # 惰性刷新策略
            need_refresh = (
                force_refresh or 
                not self._cache or 
                now - self._last_refresh > self.refresh_interval
            )
            
            if need_refresh:
                self._refresh()
                self._last_refresh = now
            
            return [m.to_dict() for m in self._cache.values()]
    
    def invalidate(self, name: str):
        """使单个用例缓存失效"""
        with self._lock:
            self._cache.pop(name, None)
    
    def _refresh(self):
        """刷新缓存"""
        yaml_files = sorted(self.cases_dir.rglob("*.yaml"))
        
        for p in yaml_files:
            name = self._name_from_path(p)
            mtime = p.stat().st_mtime
            
            # 检查是否需要更新
            cached = self._cache.get(name)
            if cached and cached.mtime == mtime:
                continue  # 未变更，跳过解析
            
            # 解析YAML
            meta = self._parse_case(p, name)
            meta.mtime = mtime
            self._cache[name] = meta
        
        # 清理已删除的用例
        current_names = {self._name_from_path(p) for p in yaml_files}
        for name in list(self._cache.keys()):
            if name not in current_names:
                del self._cache[name]
    
    def _parse_case(self, path: Path, name: str) -> CaseMeta:
        """解析单个用例"""
        from lib.runner import load_yaml
        
        meta = CaseMeta(
            name=name,
            file_path=path,
            mtime=0,
            size=path.stat().st_size,
        )
        
        try:
            case = load_yaml(path)
            if isinstance(case, dict):
                meta.display_name = case.get("name", name)
                meta.description = case.get("description", "")
                meta.tags = case.get("tags", [])
                meta.step_count = len(case.get("steps", []))
                meta.main_form_id = case.get("main_form_id", "")
        except Exception as e:
            meta.parse_error = str(e)
        
        meta.parsed_at = time.time()
        return meta
    
    def _name_from_path(self, path: Path) -> str:
        try:
            rel = path.relative_to(self.cases_dir)
            return str(rel).replace("\\", "/").replace(".yaml", "")
        except ValueError:
            return path.stem

class _CaseFileHandler(FileSystemEventHandler):
    def __init__(self, cache: CaseCache):
        self.cache = cache
    
    def on_modified(self, event):
        if event.src_path.endswith('.yaml'):
            name = self.cache._name_from_path(Path(event.src_path))
            self.cache.invalidate(name)
    
    def on_deleted(self, event):
        if event.src_path.endswith('.yaml'):
            name = self.cache._name_from_path(Path(event.src_path))
            self.cache.invalidate(name)
```

#### 方案B: 异步后台刷新

```python
import asyncio

class AsyncCaseCache:
    """异步刷新的用例缓存"""
    
    def __init__(self, refresh_interval: float = 60.0):
        self._cache = {}
        self._refresh_interval = refresh_interval
        self._task: Optional[asyncio.Task] = None
    
    async def start(self):
        self._task = asyncio.create_task(self._refresh_loop())
    
    async def _refresh_loop(self):
        while True:
            try:
                await self._refresh_cache()
            except Exception as e:
                log.error(f"Cache refresh failed: {e}")
            await asyncio.sleep(self._refresh_interval)
    
    async def _refresh_cache(self):
        # 使用 run_in_executor 避免阻塞
        loop = asyncio.get_event_loop()
        await loop.run_in_executor(None, self._sync_refresh)
```

### 1.5 预期优化效果

| 指标 | 优化前 | 优化后 | 提升 |
|------|--------|--------|------|
| 首次请求延迟 | ~150ms | ~150ms | 无变化(需解析) |
| 后续请求延迟 | ~150ms | <5ms | **30x** |
| 内存占用 | 0 | ~500KB (50用例) | 可接受 |
| CPU利用率 | 高峰时100% | <5% | **20x** |

---

## 2. 执行并发模型分析

### 2.1 当前实现

**文件位置**: `lib/webui/server.py:460-476` (单用例) / `708-765` (批量)

```python
# 单用例执行 - 后台线程
def worker():
    try:
        run_case(case, on_event=sess.emit)
    except Exception as e:
        sess.emit("case_error", {"error": str(e)})
    finally:
        sess.close()

t = threading.Thread(target=worker, daemon=True)
t.start()

# 批量执行 - 串行循环
def worker():
    for case_name in task.case_names:  # 串行!
        case = load_yaml(p)
        run_case(case, on_event=capture_event)  # 同步阻塞
```

### 2.2 性能瓶颈识别

| 问题 | 严重度 | 影响 |
|------|--------|------|
| 批量用例串行执行 | P0 | N个用例耗时 = 单个耗时 * N |
| 线程模型无限制 | P1 | 大批量可能创建过多线程 |
| 无任务队列 | P1 | 无法管理和调度执行 |
| 无并行度控制 | P1 | 资源竞争，可能压垮目标服务 |

### 2.3 并发模型设计

#### 方案A: 线程池 + 任务队列 (推荐)

```python
# lib/webui/executor.py

import threading
import queue
from concurrent.futures import ThreadPoolExecutor
from dataclasses import dataclass
from typing import Callable, Optional
from enum import Enum

class TaskPriority(Enum):
    HIGH = 0    # 手动触发
    NORMAL = 1  # 批量执行
    LOW = 2     # 定时任务

@dataclass
class ExecutionTask:
    task_id: str
    case_name: str
    env_id: str
    priority: TaskPriority
    callback: Callable
    created_at: float

class CaseExecutor:
    """用例执行调度器"""
    
    def __init__(self, max_workers: int = 4, max_queue_size: int = 100):
        self.max_workers = max_workers
        self.executor = ThreadPoolExecutor(
            max_workers=max_workers,
            thread_name_prefix="case_worker"
        )
        self._task_queue: queue.PriorityQueue = queue.PriorityQueue(maxsize=max_queue_size)
        self._active_tasks: dict[str, threading.Thread] = {}
        self._lock = threading.Lock()
        self._shutdown = False
        
    def submit(self, case_name: str, env_id: str, 
               callback: Callable, priority: TaskPriority = TaskPriority.NORMAL) -> str:
        """提交执行任务"""
        task_id = f"task_{int(time.time()*1000)}_{uuid.uuid4().hex[:6]}"
        
        task = ExecutionTask(
            task_id=task_id,
            case_name=case_name,
            env_id=env_id,
            priority=priority,
            callback=callback,
            created_at=time.time(),
        )
        
        # 优先级队列: (priority.value, created_at, task)
        self._task_queue.put((priority.value, task.created_at, task))
        return task_id
    
    def submit_batch(self, case_names: list[str], env_id: str,
                     progress_callback: Callable) -> str:
        """批量提交"""
        task_id = f"batch_{uuid.uuid4().hex[:8]}"
        
        # 创建批量任务包装器
        def batch_wrapper():
            results = []
            for i, name in enumerate(case_names):
                if self._shutdown:
                    break
                result = self._execute_single(name, env_id)
                results.append(result)
                progress_callback(i + 1, len(case_names), result)
            return results
        
        self.executor.submit(batch_wrapper)
        return task_id
    
    def _execute_single(self, case_name: str, env_id: str) -> dict:
        """执行单个用例"""
        # ... 执行逻辑
        pass
    
    def get_queue_size(self) -> int:
        return self._task_queue.qsize()
    
    def get_active_count(self) -> int:
        return len(self._active_tasks)
    
    def shutdown(self, wait: bool = True):
        self._shutdown = True
        self.executor.shutdown(wait=wait)

# 全局执行器
EXECUTOR = CaseExecutor(max_workers=4)
```

#### 方案B: asyncio 协程并发 (高阶优化)

```python
import asyncio
from typing import AsyncGenerator

class AsyncCaseRunner:
    """异步用例执行器"""
    
    def __init__(self, max_concurrent: int = 3):
        self.semaphore = asyncio.Semaphore(max_concurrent)
    
    async def run_case_async(self, case: dict, on_event: Callable) -> RunResult:
        """异步执行用例"""
        async with self.semaphore:
            # 将同步的 run_case 放入线程池执行
            loop = asyncio.get_event_loop()
            return await loop.run_in_executor(
                None,
                lambda: run_case(case, on_event)
            )
    
    async def run_batch_async(self, cases: list[dict], 
                               on_progress: Callable) -> AsyncGenerator:
        """批量并发执行"""
        tasks = [
            self.run_case_async(case, on_progress)
            for case in cases
        ]
        
        for coro in asyncio.as_completed(tasks):
            result = await coro
            yield result
```

### 2.4 并发配置建议

| 场景 | max_workers | 说明 |
|------|-------------|------|
| 单机测试 | 2-4 | 避免压垮目标服务 |
| 回归测试 | 4-8 | 充分利用多核 |
| 性能测试 | 10-20 | 需监控目标服务 |

---

## 3. SSE推送效率分析

### 3.1 当前实现

**文件位置**: `lib/webui/server.py:479-506`

```python
@APP.get("/api/runs/{run_id}/events")
async def api_sse_events(run_id: str, request: Request):
    async def event_gen():
        while True:
            if await request.is_disconnected():
                break
            try:
                evt = sess.queue.get_nowait()  # 非阻塞
            except queue.Empty:
                await asyncio.sleep(1.0)  # 1秒轮询!
                yield ": keepalive\n\n"
                continue
            # ...
    return StreamingResponse(event_gen(), media_type="text/event-stream")
```

### 3.2 性能瓶颈识别

| 问题 | 严重度 | 影响 |
|------|--------|------|
| 1秒固定轮询间隔 | P0 | 事件延迟最高1秒 |
| 无事件时浪费CPU | P1 | 空连接仍消耗资源 |
| queue.Queue 非异步友好 | P1 | 阻塞异步事件循环 |
| 大响应截断不够智能 | P2 | 8KB硬截断可能丢失关键信息 |

### 3.3 优化方案

#### 方案A: asyncio.Queue + 事件通知 (推荐)

```python
# lib/webui/async_event_bus.py

import asyncio
from typing import Any, Optional
from dataclasses import dataclass
from datetime import datetime

@dataclass
class SSEEvent:
    type: str
    data: dict
    timestamp: float

class AsyncEventBus:
    """异步事件总线，支持高效SSE推送"""
    
    def __init__(self, max_queue_size: int = 100):
        self._subscribers: dict[str, asyncio.Queue] = {}
        self._lock = asyncio.Lock()
        self._max_queue_size = max_queue_size
    
    async def subscribe(self, run_id: str) -> asyncio.Queue:
        """订阅事件流"""
        async with self._lock:
            q = asyncio.Queue(maxsize=self._max_queue_size)
            self._subscribers[run_id] = q
            return q
    
    async def unsubscribe(self, run_id: str):
        """取消订阅"""
        async with self._lock:
            self._subscribers.pop(run_id, None)
    
    async def publish(self, run_id: str, event_type: str, data: dict):
        """发布事件"""
        q = self._subscribers.get(run_id)
        if q:
            event = SSEEvent(
                type=event_type,
                data=data,
                timestamp=time.time()
            )
            try:
                q.put_nowait(event)
            except asyncio.QueueFull:
                # 队列满，丢弃旧事件
                try:
                    q.get_nowait()
                    q.put_nowait(event)
                except:
                    pass
    
    def publish_sync(self, run_id: str, event_type: str, data: dict):
        """同步发布（从线程调用）"""
        asyncio.run_coroutine_threadsafe(
            self.publish(run_id, event_type, data),
            asyncio.get_event_loop()
        )

# 全局事件总线
EVENT_BUS = AsyncEventBus()
```

#### 方案B: 智能响应截断

```python
def smart_truncate_response(resp: Any, max_len: int = 8000) -> Any:
    """智能截断响应，保留关键信息"""
    if resp is None:
        return None
    
    try:
        s = json.dumps(resp, ensure_ascii=False)
        if len(s) <= max_len:
            return resp
        
        # 对于大响应，提取关键结构
        if isinstance(resp, list):
            # 只保留前N个元素
            truncated = resp[:10]
            return {
                "_truncated": True,
                "_total": len(resp),
                "_preview": truncated,
                "_hint": "仅显示前10条"
            }
        elif isinstance(resp, dict):
            # 保留顶层键和部分值
            preview = {}
            for k, v in list(resp.items())[:20]:
                if isinstance(v, str) and len(v) > 200:
                    preview[k] = v[:200] + "..."
                elif isinstance(v, list) and len(v) > 10:
                    preview[k] = {"_count": len(v), "_sample": v[:3]}
                else:
                    preview[k] = v
            return preview
        
        return {"_truncated": True, "_length": len(s), "_preview": s[:max_len]}
    except Exception:
        return resp
```

### 3.4 预期优化效果

| 指标 | 优化前 | 优化后 | 提升 |
|------|--------|--------|------|
| 事件延迟 | 最高1秒 | <10ms | **100x** |
| 空连接CPU占用 | 持续轮询 | 0 | 显著 |
| 内存占用(100连接) | ~10MB | ~2MB | **5x** |

---

## 4. HTTP连接管理分析

### 4.1 当前实现

**文件位置**: `lib/replay.py:188-206`

```python
class CosmicFormReplay:
    def __init__(self, session: CosmicSession, ...):
        self.http = requests.Session()  # 每个实例一个Session
        self.http.verify = False

    def _post(self, path: str, body_urlenc: str, ...):
        r = self.http.post(url, data=body_urlenc, headers=headers, timeout=self.timeout)
        return r
```

### 4.2 性能瓶颈识别

| 问题 | 严重度 | 影响 |
|------|--------|------|
| 每个用例创建新Session | P0 | TCP连接无法复用 |
| 无连接池配置 | P1 | 默认连接数限制 |
| SSL验证禁用不安全 | P2 | 安全风险 |
| 无连接超时重试 | P1 | 网络抖动时失败 |

### 4.3 优化方案

#### 方案A: 全局连接池 (推荐)

```python
# lib/http_pool.py

import requests
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry
from typing import Optional

class HTTPConnectionPool:
    """全局HTTP连接池管理"""
    
    _instance: Optional["HTTPConnectionPool"] = None
    
    def __init__(self, 
                 pool_connections: int = 10,
                 pool_maxsize: int = 20,
                 max_retries: int = 3):
        self.pool_connections = pool_connections
        self.pool_maxsize = pool_maxsize
        
        # 创建带重试策略的Adapter
        retry_strategy = Retry(
            total=max_retries,
            backoff_factor=0.5,
            status_forcelist=[429, 500, 502, 503, 504],
            allowed_methods=["HEAD", "GET", "OPTIONS", "POST"]
        )
        
        self.adapter = HTTPAdapter(
            pool_connections=pool_connections,
            pool_maxsize=pool_maxsize,
            max_retries=retry_strategy
        )
        
        # 全局Session
        self.session = requests.Session()
        self.session.mount("http://", self.adapter)
        self.session.mount("https://", self.adapter)
    
    @classmethod
    def get_instance(cls) -> "HTTPConnectionPool":
        if cls._instance is None:
            cls._instance = cls()
        return cls._instance
    
    def create_replay_session(self, cosmic_session: "CosmicSession") -> requests.Session:
        """为单个回放创建独立Session（共享连接池）"""
        session = requests.Session()
        session.mount("http://", self.adapter)
        session.mount("https://", self.adapter)
        session.verify = False  # 按需配置
        return session

# 全局连接池
HTTP_POOL = HTTPConnectionPool.get_instance()
```

#### 方案B: 异步HTTP客户端 (高阶优化)

```python
import httpx
import asyncio

class AsyncHTTPClient:
    """异步HTTP客户端"""
    
    def __init__(self, max_connections: int = 20):
        self.client = httpx.AsyncClient(
            limits=httpx.Limits(
                max_connections=max_connections,
                max_keepalive_connections=10,
                keepalive_expiry=30.0
            ),
            timeout=httpx.Timeout(30.0, connect=5.0),
            verify=False
        )
    
    async def post(self, url: str, **kwargs):
        return await self.client.post(url, **kwargs)
    
    async def close(self):
        await self.client.aclose()
```

### 4.4 预期优化效果

| 指标 | 优化前 | 优化后 | 提升 |
|------|--------|--------|------|
| TCP连接数(10用例) | 10+ | 1-3 | **3-10x** |
| 连接建立延迟 | ~50ms/请求 | <5ms | **10x** |
| 内存占用 | 较高 | 较低 | 中等 |
| 重试成功率 | 0% | 80%+ | 显著 |

---

## 5. 前端渲染性能分析

### 5.1 当前实现问题

**文件位置**: `lib/webui/static/index.html` (2873行，127KB)

```html
<!-- 单文件，内联所有JS -->
<script defer src="/static/alpine.js"></script>  <!-- 46KB -->
<script src="/static/tailwind.js"></script>      <!-- 407KB! -->
```

### 5.2 性能瓶颈识别

| 问题 | 严重度 | 影响 |
|------|--------|------|
| Tailwind.js 407KB巨大 | P0 | 首屏加载慢 |
| 单HTML文件2873行 | P1 | DOM解析慢 |
| Alpine.js响应式开销 | P1 | 大列表渲染卡顿 |
| 无虚拟滚动 | P1 | 长列表内存/性能问题 |
| 无代码分割 | P1 | 首屏JS过大 |

### 5.3 优化方案

#### 方案A: 构建优化 (推荐)

```javascript
// 1. 使用Tailwind CLI生成精简CSS
// tailwind.config.js
module.exports = {
  content: ["./lib/webui/static/**/*.html"],
  theme: { extend: {} },
  plugins: [],
}

// 执行: npx tailwindcss -o ./static/css/tailwind.min.css --minify
// 预期: 407KB → ~20KB (95%减少)

// 2. 代码分割
// app.js (拆分后的入口)
import { createApp } from 'alpinejs'
import { dashboardModule } from './modules/dashboard.js'
import { editorModule } from './modules/editor.js'

// 3. 懒加载
const batchModule = () => import('./modules/batch.js')
```

#### 方案B: 虚拟滚动

```javascript
// 虚拟列表实现
class VirtualList {
  constructor(container, itemHeight, renderItem) {
    this.container = container
    this.itemHeight = itemHeight
    this.renderItem = renderItem
    this.visibleStart = 0
    this.visibleEnd = 0
  }
  
  render(items) {
    const scrollTop = this.container.scrollTop
    const viewportHeight = this.container.clientHeight
    
    this.visibleStart = Math.floor(scrollTop / this.itemHeight)
    this.visibleEnd = Math.min(
      items.length,
      this.visibleStart + Math.ceil(viewportHeight / this.itemHeight) + 2
    )
    
    // 只渲染可见项
    const fragment = document.createDocumentFragment()
    for (let i = this.visibleStart; i < this.visibleEnd; i++) {
      const el = this.renderItem(items[i], i)
      el.style.transform = `translateY(${i * this.itemHeight}px)`
      fragment.appendChild(el)
    }
    
    this.container.innerHTML = ''
    this.container.appendChild(fragment)
  }
}
```

### 5.4 预期优化效果

| 指标 | 优化前 | 优化后 | 提升 |
|------|--------|--------|------|
| 首屏JS体积 | 453KB | ~50KB | **9x** |
| 首屏加载时间 | ~2s | ~400ms | **5x** |
| 100用例渲染 | ~300ms | ~50ms | **6x** |
| 内存占用 | ~50MB | ~20MB | **2.5x** |

---

## 6. 内存使用分析

### 6.1 当前实现问题

**文件位置**: 多处

```python
# 1. 全局RUNS字典无清理
RUNS: dict[str, RunSession] = {}  # 无限增长!

# 2. ExecutionHistory固定100条
EXECUTION_HISTORY = ExecutionHistory(100)

# 3. LOG_STORE缓冲500条
LOG_STORE = LogStore(_log_dir_path, buffer_size=500)

# 4. 大响应对象未释放
self.last_response = resp  # 可能很大
ctx["response_history"] = []  # 累积所有响应
```

### 6.2 性能瓶颈识别

| 问题 | 严重度 | 影响 |
|------|--------|------|
| RUNS无限增长 | P0 | 内存泄漏 |
| 响应历史累积 | P0 | 大用例内存爆炸 |
| 无内存监控 | P1 | 无法预警 |
| Python GC压力 | P1 | 频繁GC暂停 |

### 6.3 优化方案

#### 方案A: 内存管理增强

```python
# lib/webui/memory_manager.py

import gc
import weakref
from typing import Optional
from dataclasses import dataclass
import threading

@dataclass
class MemoryStats:
    used_mb: float
    available_mb: float
    percent: float

class MemoryManager:
    """内存管理器"""
    
    def __init__(self, 
                 max_runs: int = 50,
                 max_response_history: int = 10,
                 warning_threshold_mb: float = 500):
        self.max_runs = max_runs
        self.max_response_history = max_response_history
        self.warning_threshold = warning_threshold_mb
        self._lock = threading.Lock()
        
    def cleanup_runs(self, runs: dict):
        """清理过期RUNS"""
        with self._lock:
            if len(runs) > self.max_runs:
                # 按时间排序，删除最旧的
                sorted_ids = sorted(
                    runs.keys(),
                    key=lambda k: runs[k].started_at
                )
                for rid in sorted_ids[:len(runs) - self.max_runs]:
                    sess = runs.pop(rid, None)
                    if sess:
                        sess.queue = None  # 释放队列
    
    def truncate_response_history(self, ctx: dict):
        """截断响应历史"""
        history = ctx.get("response_history", [])
        if len(history) > self.max_response_history:
            ctx["response_history"] = history[-self.max_response_history:]
    
    def get_memory_stats(self) -> MemoryStats:
        """获取内存统计"""
        import psutil
        process = psutil.Process()
        mem_info = process.memory_info()
        
        return MemoryStats(
            used_mb=mem_info.rss / 1024 / 1024,
            available_mb=psutil.virtual_memory().available / 1024 / 1024,
            percent=process.memory_percent()
        )
    
    def check_and_gc(self) -> bool:
        """检查内存并触发GC"""
        stats = self.get_memory_stats()
        if stats.used_mb > self.warning_threshold:
            gc.collect()
            return True
        return False

# 响应对象弱引用
class WeakResponse:
    """弱引用响应包装器"""
    def __init__(self, response):
        self._ref = weakref.ref(response)
    
    @property
    def data(self):
        return self._ref()
```

#### 方案B: 对象池

```python
from queue import Queue

class ResponsePool:
    """响应对象池"""
    
    def __init__(self, max_size: int = 100):
        self._pool: Queue = Queue(maxsize=max_size)
    
    def acquire(self) -> dict:
        try:
            return self._pool.get_nowait()
        except:
            return {}
    
    def release(self, obj: dict):
        obj.clear()  # 清空内容
        try:
            self._pool.put_nowait(obj)
        except:
            pass  # 池满，丢弃
```

### 6.4 预期优化效果

| 指标 | 优化前 | 优化后 | 提升 |
|------|--------|--------|------|
| 长期运行内存增长 | 无限 | 稳定 | **关键** |
| 大用例内存峰值 | ~500MB | ~100MB | **5x** |
| GC暂停频率 | 高 | 低 | **3x** |

---

## 7. 综合优化实施路线图

### Phase 1: 快速见效 (1-2周)

1. **用例缓存** - P0优先级，效果显著
2. **HTTP连接池** - P0优先级，改动小
3. **RUNS清理** - P0优先级，修复内存泄漏

### Phase 2: 架构优化 (2-4周)

4. **SSE异步化** - P0优先级，提升用户体验
5. **并发执行器** - P0优先级，提升吞吐
6. **前端优化** - P1优先级，首屏性能

### Phase 3: 深度优化 (4-8周)

7. **异步重构** - P1优先级，高并发支持
8. **内存管理** - P1优先级，稳定性
9. **监控完善** - P2优先级，可观测性

---

## 8. 性能基准建议

### 目标SLA

| 指标 | 目标值 | 当前值 |
|------|--------|--------|
| /api/cases 响应时间 | <100ms (缓存命中) | ~150ms |
| /api/cases 响应时间 | <200ms (缓存未命中) | ~150ms |
| 单用例执行延迟(44步) | <30s | 待测 |
| 批量执行(3用例) | <90s | 待测 |
| SSE事件延迟 | <50ms | ~1s |
| 首屏加载 | <1s | ~2s |
| 内存占用(稳定) | <200MB | 待测 |

---

*报告生成时间: 2026-04-28*
*作者: 性能架构师*
