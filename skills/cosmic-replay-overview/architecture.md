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
│   ├── component_registry.py # HAR 组件处理器注册表
│   ├── har_regression.py  # HAR 回归样本与影响分级
│   ├── agent_evidence.py  # AI 修复证据包
│   ├── report_exporter.py # 批量 HTML 报告导出
│   ├── kb_loader.py       # 知识库加载
│   ├── task_manager.py    # 批量任务、验收报告、入库证据
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

### 智能文本变量

触发条件：`description` / `desc` / `remark` / `memo` / `note` / `comment` / `changedescription` / `changedesc`，或实时元数据、知识库识别为可编辑文本字段。

处理：生成 `${vars.test_description}` / `${vars.test_remark}` 等变量。此类字段不一定追加随机后缀，但必须从 HAR 硬编码值抽到 `vars`，方便导入后统一编辑。

### B档：环境相关（保留字面量，面板可改）

触发条件：`field_key in ENV_RELATED_FIELDS or ENUM_FIELDS`

```python
ENV_RELATED_FIELDS = {"org", "parentorg", "adminorg", "position", "country", ...}
ENUM_FIELDS = {"gender", "certificatetype", "enable", "relationship", ...}
```

处理：生成 `pick_fields` 段，前端面板展示可编辑

### 元数据增强链路

HAR 导入时，如果 Web UI 已选择并登录环境，会通过 `MetadataResolver` 调用 `/metadata/getEntityType.do?entityId=<form_id>` 获取实时字段标签、类型、必填、基础资料引用等信息。离线时回落到 `cosmic-hr-expert` 知识库，其中 `kb_loader` 同时支持：
- `knowledge/scenarios/<form_id>/scene_doc_lite.json`
- `knowledge/_shared/_standard_metadata/entity_metadata/<form_id>.md`

### 环境字段运行时优先级

`pick_fields` 不是普通展示字段，而是跨环境执行前会被 runner 注入到请求体里的运行期输入。

优先级从高到低：

| 来源 | YAML 标记 | 行为 |
|------|----------|------|
| 用户手工维护值 | `manual_override: true` + `auto_resolve: false` | 直接使用 YAML 中的 value，不再按名称解析 |
| 当前环境自动解析 | `auto_resolve: true` + `value_name` | 通过 `FieldResolver` 按当前环境查基础资料/枚举内码 |
| HAR 原始值 | 无解析信息或解析失败 | 保留录制时的原始值 |

关键风险：如果用户在预览页把“行政组织类型”从 `1020` 改成 `1010`，但 YAML 仍保留旧 `value_name` 且 `auto_resolve=true`，运行时会按旧名称重新解析回 `1020`。因此前端保存手工值时必须设置 `manual_override=true` 并关闭 `auto_resolve`。

### C档：响应回传（跨 step 引用）

如 processInstId、fid、pkValue 等从响应中提取，用 `${capture.step_id}` 引用

## HAR 导入到 YAML 的验收链路

导入 HAR 后，不应直接以“能生成 YAML”为成功标准，而是先产出可解释的预览：

```
HAR 上传
  → har_extractor 过滤噪声请求
  → MetadataResolver 在线补字段语义
  → cosmic-hr-expert 离线知识库兜底
  → component_registry 给 preview step 打标签
  → 变量识别：智能变量 / 环境字段 / 响应捕获
  → 预览页人工确认或修正
  → 生成 YAML
```

组件注册表当前定位是“雷达层”，不直接改写主解析链路。它负责标记已覆盖组件、未知组件和风险等级，方便新 HAR 出现解析遗漏时快速定位，不影响已成功生成的 YAML。

## YAML 到执行的关键链路

```
runner.py 加载 YAML
  → 读取 vars / pick_fields / target_forms
  → 登录并创建 root pageId
  → 按 step 执行 open_form / invoke / assert
  → FieldResolver 注入环境字段
  → replay.py 维护 pageId 状态机
  → diagnoser/advisor 生成失败原因与修复建议
  → task_manager 汇总批量报告
```

执行 PASS 只能说明协议步骤没有抛错，不等价于业务数据一定入库。批量报告必须结合写库证据判断是否可交付。

## 批量验收报告与入库证据

`task_manager.py` 会在批量执行后补充验收维度：

| 状态 | 触发条件 | 面向用户的结论 |
|------|----------|---------------|
| `verified` | 独立只读查询按本次运行业务键精确命中 | 已自动验证业务结果 |
| `manual_verified` | YAML 中存在 `write_verification.manual_confirmed` 且本次 PASS | 用户已确认入库 |
| `unverified` | 本次 PASS，但保存响应为空或缺少明确写入证据 | 需要补断言、修 pageId，或人工确认 |
| `failed` | 保存、提交、断言或执行步骤失败 | 按失败分析修复 |
| `not_applicable` | 没有识别到写库步骤 | 普通非写库用例 |

用户点击“人工确认已入库”后，系统会写回 YAML：

```yaml
write_verification:
  manual_confirmed: true
  confirmed_at: "..."
  confirmed_by: "manual"
  reason: "..."
```

后续同一用例如果执行 PASS 且只有“保存响应为空”这类证据不足问题，会显示 `manual_verified`，不再进入 AI 诊断队列。注意：人工确认不是篡改执行结果，它只是把“业务数据已由人确认”的证据记录下来。

## AI 修复证据链

当报告判断 `next_action=ai_agent` 时，Web UI 会提供“让AI修复”按钮，而不是只展示技术链接。按钮复制的提示词包含：

- 用例名、任务 ID、环境名
- 报告中的失败或未验证原因
- `/api/tasks/{task_id}/agent-evidence/{case_name}` 证据包链接
- 必读技能：`cosmic-replay-troubleshooter`、`cosmic-replay-overview`
- 修复护栏：不得为了 PASS 删除关键 step、断言、`target_forms` 或环境字段
- 必跑测试命令

证据包由 `agent_evidence.py` 生成，面向 AI/研发，不要求普通使用者阅读。它聚合 YAML、执行事件、批量报告上下文和排障入口，让 Agent 能复现“解析遗漏、pageId 链路错误、PASS 但未入库”等问题。

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
