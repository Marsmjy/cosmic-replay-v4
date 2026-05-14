# 架构详解

## 目录结构

```
cosmic-replay-v4/
├── lib/                    # 核心业务逻辑
│   ├── replay.py          # 回放引擎（协议层）
│   ├── runner.py          # 用例执行器（调度层）
│   ├── har_extractor.py   # HAR→YAML 转换
│   ├── diagnoser.py       # 响应诊断
│   ├── advisor.py         # 修复建议
│   ├── config.py          # 配置管理
│   ├── cosmic_login.py    # 登录模块
│   ├── field_resolver.py  # 基础资料跨环境解析
│   ├── kb_loader.py       # 知识库加载
│   ├── task_manager.py    # 任务管理
│   ├── session_manager.py # 会话管理
│   ├── db/                # SQLite 持久化
│   ├── webui/             # Web UI
│   │   ├── server.py      # FastAPI 服务
│   │   ├── routes/        # API 路由模块
│   │   ├── log_store.py   # 执行日志
│   │   └── static/        # 前端资源
│   ├── security/          # 安全模块
│   └── monitoring/        # 监控模块
├── cases/                 # YAML 测试用例
├── config/                # 运行时配置（git忽略）
│   ├── webui.yaml         # UI 偏好
│   └── envs/              # 环境配置（sit.yaml等）
├── har_uploads/           # HAR 文件存档
├── skills/                # AI Agent Skills
├── tests/                 # 测试套件
├── logs/                  # 执行日志
└── deploy/                # 部署配置
```

## PageId 四层跃迁详解

苍穹平台的表单操作依赖 pageId 确定上下文，这是本项目最复杂的设计。

### 层级定义

| 层级 | 格式 | 来源 | 生命周期 | 用途 |
|------|------|------|---------|------|
| L0 | `root{32hex}` | `init_root()` → getConfig(home_page) | 整个会话 | 会话根 |
| L1 | `{32hex}` | `open_portal()` → getConfig | 打开门户后 | 门户操作 |
| L2 | `{menuId}root{32hex}` | menuItemClick 后计算 | 菜单切换后 | 列表/表单操作 |
| L3 | `{32hex}` | `open_form()` → getConfig | 打开表单后 | 独立表单 |

### 跃迁流程图（菜单→新增→保存）

```
步骤1: open_portal(bos_portal_myapp_new)
  → getConfig → 返回 L1 pageId: {32hex}
  → 缓存: page_ids["bos_portal_myapp_new"] = L1

步骤2: invoke(bos_portal_myapp_new, menuItemClick)
  → 用 L1 pageId 发请求
  → 响应 harvest: 扫描 addVirtualTab 指令
  → 提取 app_id 对应的 pageId → _pending_by_app["haos"] = L2
  → 或用公式计算: L2 = f"{menuId}root{root_base_id}"

步骤3: invoke(target_form, addnew)
  → pageId 查找优先级:
    1. 显式指定 > 2. _pending_by_app[app_id] > 3. page_ids[form_id] > 4. root
  → 用 L2 pageId 发请求

步骤4: invoke(target_form, save)
  → 保存成功后: page_ids.pop(form_id) ← pageId 失效
  → 除非 keep_page=true（连续新增场景）
```

### PageId 管理关键逻辑（replay.py）

- `page_ids: dict[str, str]` - 表单级缓存
- `_pending_by_app: dict[str, str]` - 按 app_id 的待消费 pageId
- `_harvest_page_ids(response)` - 从响应中收割新 pageId
- 安全网：如果目标 form 没有有效 pageId，runner 会自动 open_form 补偿

## 变量三档识别系统

### A档：唯一键（必须变量化）

触发条件：`field_key in UNIQUE_KEY_HINTS`

```python
UNIQUE_KEY_HINTS = {
    "number", "code", "name", "fullname", "simplename",
    "empnumber", "certificatenumber", "phone", "mobile", ...
}
```

处理：生成 `${vars.test_<field_key>}` + 在 vars 段声明随机值模板

### B档：环境相关（保留字面量，面板可改）

触发条件：`field_key in ENV_RELATED_FIELDS or ENUM_FIELDS`

```python
ENV_RELATED_FIELDS = {"org", "parentorg", "adminorg", "position", "country", ...}
ENUM_FIELDS = {"gender", "certificatetype", "enable", "relationship", ...}
```

处理：生成 `pick_fields` 段，前端面板展示可编辑

### C档：响应回传（跨 step 引用）

如 processInstId、fid、pkValue 等从响应中提取，用 `${capture.step_id}` 引用

## 苍穹协议核心

### batchInvokeAction.do

```
POST /form/batchInvokeAction.do
Headers:
  cqappid: <app_id>
  Cookie: <session_cookie>
Body (x-www-form-urlencoded):
  pageId=<page_id>
  &params=[{"key":"..","methodName":"..","args":[..],"postData":[{},[]]}]
  &sign=<signature>  (SIT环境可省略)
```

### 常见 ac 值

| ac | 含义 | tier |
|----|------|------|
| menuItemClick | 菜单导航 | core |
| addnew | 新增记录 | core |
| save / saveandeffect | 保存 | core |
| submit / submitandeffect | 提交 | core |
| delete | 删除 | core |
| getLookUpList | UI联动查询 | ui_reaction |
| clientCallBack | 前端回调 | noise |

## SSE 事件流

执行过程的事件序列：
```
case_start → login_start → login_ok → session_ready
  → step_start → step_ok (循环每个step)
  → assertion_ok / assertion_fail
  → fixes_ready (失败时)
  → case_done
```

每个事件是 JSON 对象，通过 EventSource 推送到前端。
