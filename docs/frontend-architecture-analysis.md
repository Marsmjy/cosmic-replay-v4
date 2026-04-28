# cosmic-replay 前端架构分析报告

> 分析文件：`lib/webui/static/index.html` (2035 行，92KB)  
> 技术栈：Alpine.js + Tailwind CSS (CDN 版本)

---

## 一、视图/页面概览

| 视图名称 | 状态变量 | 功能描述 |
|---------|---------|---------|
| **Dashboard** | `view='dashboard'` | 用例库主页面，列表展示所有测试用例 |
| **用例详情** | `view='case_detail'` | 单个用例的详情、执行、YAML编辑 |
| **批量运行** | `view='batch'` | 批量选选用例并顺序执行 |
| **HAR 导入** | `view='import_har'` | 3步向导：上传→预览→生成 |
| **配置页** | `view='settings'` | WebUI 配置 + 环境管理 |
| **日志页** | `view='logs'` | 服务日志流 + 执行历史 |

---

## 二、状态管理结构

### 2.1 核心全局状态 (Alpine.js `x-data`)

```javascript
view: 'dashboard',           // 当前视图
toast: '',                  // Toast 消息

// 数据层
cases: [],                  // 用例列表 [{name, display_name, description, main_form_id, step_count, tags, mtime}]
envs: [],                   // 环境列表 [{id, name, base_url, credentials, basedata, ...}]
webuiPrefs: {},             // WebUI 配置（只读）
webuiDraft: {},             // WebUI 配置（编辑草稿）
currentEnv: '',             // 当前选中环境 ID
lastResults: {},            // 用例名 → 'pass' | 'fail' | 'running'

// 用例详情状态
currentCase: null,          // 当前打开的用例对象
yamlSource: '',             // YAML 源码（编辑器内容）
yamlDirty: false,           // YAML 是否有未保存修改
phases: [],                 // 执行阶段列表 [{id, label, detail, status, duration_ms, errors, response}]
summary: null,              // 执行摘要 {passed, step_ok, step_count, duration_s, ...}
fixes: [],                  // 修复建议列表
running: false,             // 是否正在执行
selectedStep: null,         // 当前选中步骤 ID
resolved_vars: [],          // 解析后的变量实际值
sse: null,                  // SSE 连接对象

// 活跃 run 登记表（跨页面保持）
activeRuns: {},             // caseName → {runId, phases, summary, fixes, running, sse, resolved_vars}

// HAR 向导状态
harStep: 1,                 // 步骤：1上传 → 2预览 → 3生成
harUploading: false,
harPreview: null,           // {main_form_id, tier_counts, steps, detected_vars}
harFile: null,              // 临时文件路径
harCaseName: '',            // 用例命名
harVarConfig: [],           // 变量配置面板 [{name, category, template, enabled}]
dropHover: false,           // 拖拽悬停状态

// Dashboard 搜索/排序/批量
caseSearch: '',             // 搜索关键字
caseSort: 'name',           // 排序字段：name | mtime | status | steps
caseSortDir: 'asc',         // 排序方向
dashSelected: new Set(),    // 批量选择的用例名集合

// 用例详情 Tab
detailTab: 'result',        // result | vars | yaml | steps

// 批量运行状态
batchSelected: new Set(),   // 选中的用例名集合
batchRunning: false,        // 是否正在批量执行
batchDone: false,           // 批量是否完成
batchCurrent: 0,
batchTotal: 0,
batchProgress: 0,
batchStats: {pass: 0, fail: 0},
batchResults: {},           // 用例名 → 结果状态
batchErrors: {},            // 用例名 → 错误信息

// 配置页状态
settingsTab: 'general',     // general | envs
envDraft: {},               // 环境编辑草稿 {envId: {...}}
envShowPwd: {},             // 密码显示状态
envExpanded: {},            // 环境展开状态
newEnvDialog: false,
newEnvId: '',
newEnvClone: '',

// 日志页状态
logsTab: 'server',          // server | history
logs: [],                   // 日志条目数组
logLevel: '',               // 过滤级别
logSearch: '',              // 搜索关键字
logAutoScroll: true,
logsSSE: null,              // 日志流订阅
errorBadgeCount: 0,         // 未读错误计数
runHistory: [],             // 执行历史列表
selectedRunId: '',
selectedRunEvents: [],
```

### 2.2 状态流向图

```
┌─────────────────────────────────────────────────────────────────┐
│                        全局状态 (Alpine reactive)                │
├─────────────────────────────────────────────────────────────────┤
│  cases[] ◄───── loadCases() ◄───── GET /api/cases              │
│  envs[]  ◄───── loadEnvs()  ◄───── GET /api/config?mask=false   │
│  lastResults {} ◄─── SSE events (case_done)                    │
│  activeRuns {} ◄──── SSE events (所有执行事件)                   │
│  logs[] ◄──── SSE /api/logs/stream (后台持续推送)               │
└─────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│                      视图层 (x-show, x-for)                     │
├─────────────────────────────────────────────────────────────────┤
│  view='dashboard' → filteredCases() → 表格渲染                  │
│  view='case_detail' → currentCase + phases → 执行进度面板       │
│  view='batch' → cases + batchSelected → 批量选择表格             │
│  view='logs' → filteredLogs() → 日志流显示                      │
└─────────────────────────────────────────────────────────────────┘
```

---

## 三、SSE 事件处理逻辑

### 3.1 事件订阅架构

```javascript
// 单用例执行订阅
subscribeRun(runId, caseName) {
  const es = new EventSource(`/api/runs/${runId}/events`);
  record.sse = es;
  
  // 事件处理后更新 activeRuns[caseName] 中的状态
  // 由于 phases 是引用，UI 自动响应
}
```

### 3.2 事件类型与处理

| 事件名 | 数据结构 | 状态更新 |
|--------|---------|---------|
| `login_start` | `{username, base_url}` | 添加 login 阶段，status='running' |
| `login_ok` | `{user_id}` | 更新 login 阶段，status='ok' |
| `session_ready` | `{root_page_id, resolved_vars}` | 添加 session 阶段，保存解析变量 |
| `step_start` | `{id, detail, resolved_request}` | 添加步骤阶段，status='running' |
| `step_ok` | `{id, duration_ms, response}` | 更新步骤状态='ok'，保存响应 |
| `step_fail` | `{id, duration_ms, errors, response}` | 更新步骤状态='fail' |
| `assertion_ok` | `{type, msg}` | 添加断言阶段，status='ok' |
| `assertion_fail` | `{type, msg}` | 添加断言阶段，status='fail' |
| `fixes_ready` | `{fixes: [...]}` | 更新修复建议列表 |
| `case_done` | `{passed, step_ok, step_count, ...}` | 标记完成，更新 lastResults |
| `case_error` | `{error}` | 显示错误 Toast |
| `close` | - | 清理 SSE 连接 |

### 3.3 多订阅管理（activeRuns）

```javascript
activeRuns[caseName] = {
  runId: 'xxx',
  phases: [],      // 与 this.phases 共享引用
  summary: null,
  fixes: [],
  running: true,
  sse: EventSource,
  resolved_vars: []
}
```

**关键设计**：
- 离开详情页不销毁 SSE，后台持续更新
- 返回详情页时通过 `hydrateFromActive()` 恢复状态
- 避免重复订阅同一 runId

### 3.4 日志 SSE 订阅

```javascript
startLogsSubscription() {
  const es = new EventSource('/api/logs/stream');
  es.addEventListener('log', (ev) => {
    this.logs.push(JSON.parse(ev.data));
    if (entry.level === 'error') this.errorBadgeCount++;  // 角标计数
  });
}
```

---

## 四、UI 组件和交互模式

### 4.1 通用组件模式

| 模式 | 实现 | 用例 |
|------|------|------|
| **Tab 切换** | `:class="tab==='x' ? 'active' : ''"` | 详情页、配置页、日志页 |
| **状态指示器** | 彩色圆点 + pulse 动画 | 用例状态、步骤状态 |
| **进度条** | `width: ${percent}%` + 颜色动态 | 执行进度、批量进度 |
| **表格选择** | `Set` + checkbox + `@change` | Dashboard 批量、批量运行 |
| **模态对话框** | `fixed inset-0 bg-black/60` + `@click.stop` | 新建环境 |
| **Toast 提示** | `fixed bottom-6 right-6` + `x-transition` | 操作反馈 |
| **拖拽上传** | `dragover` + `drop` + dropHover 类 | HAR 导入 |
| **展开/收起** | `envExpanded[id]` 三元表达式 | 环境配置折叠 |

### 4.2 Dashboard 交互

```
┌─────────────────────────────────────────────────────────────┐
│  + 导入 HAR 新建用例                                         │
├─────────────────────────────────────────────────────────────┤
│  [搜索框]  [排序选择]  [排序方向]                            │
│  已选 N 项  [全选] [取消] [批量删除]                         │
├─────────────────────────────────────────────────────────────┤
│  ☐ | 状态 | 用例名 | 主表单 | 步骤数 | 标签 | 操作          │
│  ──── 响应式 hover 行高亮 ───────────────────────────────   │
│  [打开] [▶ 运行] [✖删除]                                    │
└─────────────────────────────────────────────────────────────┘
```

### 4.3 用例详情页交互

```
┌─────────────────────────────────────────────────────────────┐
│  ← 返回  [用例名]                          [▶ 运行/运行中...] │
├─────────────────────────────────────────────────────────────┤
│  进度条: ████████░░ 80%  正在执行: [当前步骤名]              │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌──────────────────────────────────────┐  │
│  │ 执行步骤    │  │ [运行结果] [变量面板] [YAML] [步骤导航] │  │
│  │ ✓ 登录      │  │ ──────────────────────────────────────│  │
│  │ ✓ 会话就绪  │  │ 执行结果/PASS 提示                      │  │
│  │ ● step:xxx  │  │ 变量实际值面板                          │  │
│  │ ○ step:yyy  │  │ 修复建议面板                            │  │
│  │ ...         │  │ 步骤详情（响应数据/错误）                 │  │
│  └─────────────┘  └──────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### 4.4 HAR 导入向导（3 步骤）

```
Step 1: 上传
  ┌─────────────────────────────────────┐
  │  拖 HAR 文件到此处                   │
  │  或 [选择文件]                       │
  └─────────────────────────────────────┘

Step 2: 预览
  ┌────────────┬────────────┬────────────┬────────────┐
  │ 主表单: xxx │ 核心: N    │ UI联动: M  │ 噪声: K    │
  └────────────┴────────────┴────────────┴────────────┘
  [变量配置面板]
  ☐ 编码 ${vars.code} [模板值]
  ☑ 名称 ${vars.name} [模板值]
  
  [步骤预览列表]
  ● core: step_id
  ○ ui_reaction: step_id
  ○ noise: step_id (过滤)

Step 3: 生成
  用例名: [____________]
  将生成: cases/<name>.yaml
  [✓ 生成 YAML]
```

### 4.5 批量运行交互

```
┌─────────────────────────────────────────────────────────────┐
│  批量运行                    [全选] [全不选] [▶ 运行选中 (N)] │
├────────────────████████████░░░░─────────────────────────────┤
│  ████████████████░░░░░░░░░░░░░░░░░░  3/10                   │
│  ✓ 5 通过  ✗ 1 失败  ⋯ 4 待执行                              │
├─────────────────────────────────────────────────────────────┤
│  ☐ | 状态 | 用例 | 结果                                     │
│  ☑ |    ✓ | case1 |                                         │
│  ☑ |    ✗ | case2 | 2 步失败 / 1 断言失败                    │
│  ☑ |    ● | case3 | (running)                               │
└─────────────────────────────────────────────────────────────┘
```

### 4.6 日志页交互

```
[服务日志] [执行历史]
┌────────────────────────────────────────────────────────────┐
│ [全部][info][warn][error]  [搜索...]  ☑自动滚动  [清屏]     │
├────────────────────────────────────────────────────────────┤
│ 09:41:23  INFO  [api] 成功加载 3 条用例                      │
│ 09:41:25  WARN  [runner] step 超时重试                       │
│ 09:41:30  ERROR [runner] 断言失败: xxx ← 高亮红底           │
└────────────────────────────────────────────────────────────┘

执行历史 Tab:
┌────────────┐  ┌───────────────────────────────────┐
│ ✓ case1    │  │ 事件流详情                         │
│ ✗ case2    │  │ 09:41:20 case_start {...}         │
│ ? case3    │  │ 09:41:21 step_ok {...}             │
└────────────┘  └───────────────────────────────────┘
```

---

## 五、样式和布局

### 5.1 CSS 架构

```html
<!-- 外部依赖（CDN） -->
<script src="/static/tailwind.js"></script>  <!-- Tailwind CSS -->
<script defer src="/static/alpine.js"></script>  <!-- Alpine.js -->

<!-- 自定义样式（约 20 行） -->
<style>
  /* 基础样式 */
  html, body { height: 100% }
  body { font-family: -apple-system, ... }
  
  /* 滚动条 */
  .scrollbar-thin::-webkit-scrollbar { width: 8px }
  
  /* 动画 */
  .fadein { animation: fadein .3s ease-out }
  .pulse-dot { animation: pulse 1.5s infinite }
  
  /* 拖拽悬停 */
  .drop-hover { background: rgba(14,165,233,.08); border-color: #0ea5e9 }
</style>
```

### 5.2 设计系统

| 元素 | Tailwind 类 | 说明 |
|------|-------------|------|
| 背景色 | `bg-slate-900` / `bg-slate-950` | 深色主题 |
| 边框 | `border-slate-800` | 统一边框色 |
| 文字 | `text-slate-400`（次要） / `text-slate-200`（主要） | 层级分明 |
| 强调色 | `text-sky-400`（主操作） / `text-emerald-400`（成功） / `text-rose-400`（失败） | 语义化 |
| 表单输入 | `bg-slate-950 border-slate-700 rounded px-3 py-2` | 一致的输入框样式 |
| 按钮 | `bg-sky-600 hover:bg-sky-500 text-white px-4 py-2 rounded` | 主操作按钮 |
| 卡片 | `bg-slate-900 border border-slate-800 rounded-lg` | 内容容器 |

### 5.3 布局结构

```
┌──────────────────────────────────────────────────────────────┐
│ header.sticky.top-0.z-10                                     │
│ ├─ Logo + 版本号                                             │
│ ├─ nav: [用例] [批量运行] [导入HAR] [日志]                    │
│ ├─ 环境选择器 + 凭证状态指示                                  │
│ └─ ⚙ 设置按钮                                               │
├──────────────────────────────────────────────────────────────┤
│ main.max-w-7xl.mx-auto.px-6.py-6                             │
│ └─ section.fadein (x-show="view==='xxx'")                    │
│     └─ 内容区域                                              │
└──────────────────────────────────────────────────────────────┘
```

### 5.4 响应式设计

- 最大宽度：`max-w-7xl`（约 1280px）
- 不支持移动端响应式（桌面优先设计）
- 表格横向滚动：`overflow-x-auto`
- 内容区纵向滚动：`max-h-[calc(100vh-260px)] overflow-y-auto`

---

## 六、API 接口清单

| 接口 | 方法 | 用途 |
|------|------|------|
| `/api/cases` | GET | 获取用例列表 |
| `/api/cases/{name}` | DELETE | 删除用例 |
| `/api/cases/{name}/yaml` | GET/PUT | 获取/保存用例 YAML |
| `/api/cases/{name}/run` | POST | 触发用例执行 |
| `/api/cases/batch_delete` | POST | 批量删除用例 |
| `/api/runs/{runId}/events` | GET (SSE) | 执行事件流 |
| `/api/config` | GET | 获取配置（含凭证掩码） |
| `/api/config?mask=false` | GET | 获取完整配置 |
| `/api/config/webui` | PUT | 保存 WebUI 配置 |
| `/api/envs/{id}` | PUT/DELETE | 保存/删除环境 |
| `/api/har/preview` | POST | 预览 HAR 文件 |
| `/api/har/extract` | POST | 从 HAR 生成用例 |
| `/api/logs/stream` | GET (SSE) | 实时日志流 |
| `/api/run_history` | GET | 执行历史列表 |
| `/api/run_history/{runId}` | GET | 单次执行详情 |

---

## 七、关键技术特点

### 7.1 单文件架构
- 2035 行全部在单个 HTML 文件
- 无构建工具，直接 CDN 引入依赖
- 适合简单项目，但不利于维护扩展

### 7.2 Alpine.js 响应式
- 使用 `x-data`、`x-model`、`x-show`、`x-for` 指令
- 无需虚拟 DOM，直接 DOM 绑定
- 状态变更自动触发 UI 更新

### 7.3 SSE 实时通信
- 用例执行进度实时推送
- 服务日志实时流式推送
- 支持多订阅并存（activeRuns 管理）

### 7.4 智能状态持久化
- 离开详情页保留执行状态（activeRuns 登记表）
- 返回页自动恢复，避免数据丢失
- 支持后台执行时浏览其他页面

### 7.5 YAML 内嵌编辑器
- 行号显示（动态计算）
- 变量面板与 YAML 双向绑定
- 步骤导航定位到 YAML 行

---

## 八、潜在改进建议

1. **组件拆分**：将各视图拆分为独立组件文件
2. **状态管理**：考虑 Pinia 或 Zustand 替代 Alpine 状态
3. **TypeScript**：添加类型定义，提升代码健壮性
4. **构建工具**：引入 Vite/Webpack，支持热更新和代码分割
5. **错误边界**：增加全局错误捕获和友好的错误页面
6. **国际化**：抽取文本为 i18n 配置文件

---

*报告生成时间：2026-04-28*  
*分析工具：Hermes Agent*
