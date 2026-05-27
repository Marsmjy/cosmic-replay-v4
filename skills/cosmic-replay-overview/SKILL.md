---
name: cosmic-replay-overview
description: Cosmic Replay 项目架构、模块职责、HAR 导入、YAML 生成、执行引擎、pageId、环境字段、批量报告和 AI 修复证据包总览。Use when an AI Agent needs to understand or modify the cosmic-replay project, onboard into the codebase, diagnose architecture-level behavior, or prepare consultant handoff.
---

# Cosmic Replay v4 - 项目概览 Skill

## 触发条件
当 AI Agent 需要理解 cosmic-replay-v4 项目架构、修改代码、排查问题时加载此 Skill。

## 项目一句话定位
金蝶苍穹平台的 HAR 录制 → YAML 用例 → 自动回放 → 批量验收报告 的端到端自动化测试工具。目标不是只跑出 PASS，而是判断用例是否可交付、是否真实入库、是否需要 AI 修复。

## 技术栈
- 后端：FastAPI + Uvicorn (Python 3.10+)
- 前端：Alpine.js + Tailwind CSS（无构建工具，单 HTML 文件）
- 协议：苍穹 batchInvokeAction.do
- 持久化：SQLite (data/cosmic_replay.db)
- 部署：Docker Compose / 本地 start.sh

## 核心文件索引（改代码前先定位）

| 文件 | 行数 | 职责 |
|------|------|------|
| `lib/replay.py` | ~784 | 回放引擎核心：PageId 四层管理、batchInvokeAction 协议封装 |
| `lib/runner.py` | ~1450 | YAML 用例执行器：三层防护、变量解析、Step Handlers、断言、SSE 推送 |
| `lib/har_extractor.py` | ~2749 | HAR→YAML 转换：变量三档识别、动作分类、反模式检测 |
| `lib/webui/server.py` | ~1450 | FastAPI 后端：15+ API 端点、SSE 实时事件流 |
| `lib/webui/static/index.html` | ~5117 | 前端 UI（宇宙主题）：用例管理/执行/HAR导入 |
| `lib/diagnoser.py` | ~126 | 响应诊断：从苍穹响应提取结构化错误 |
| `lib/advisor.py` | ~448 | 修复建议：错误分析 + YAML 补丁生成 |
| `lib/config.py` | ~346 | 两层配置：webui.yaml + envs/*.yaml |
| `lib/cosmic_login.py` | ~490 | 苍穹登录：RSA 加密 + 多重兜底 |
| `lib/kb_loader.py` | ~363 | 知识库懒加载：场景元数据 + shared entity_metadata 字段分类 |
| `lib/field_resolver.py` | - | 基础资料跨环境解析 |
| `lib/component_registry.py` | - | HAR 组件处理器注册表：给 preview step 打标签、统计未知组件风险 |
| `lib/har_regression.py` | - | 10 类 HAR 结构基线、影响分级和回归门禁（8 个 SIT + 2 个 UAT） |
| `lib/task_manager.py` | - | 任务管理、批量报告、入库证据和行动队列 |
| `lib/agent_evidence.py` | - | AI Agent 修复证据包：YAML、运行事件、报告上下文、技能护栏 |
| `lib/report_exporter.py` | - | HTML 报告导出，内嵌离线图表资源 |
| `lib/db/dao.py` | - | 数据访问对象（SQLite） |
| `lib/playwright_explorer.py` | - | Playwright 只读探索器：登录、菜单候选、表单/接口/pageId 摘要采集 |
| `lib/playwright_deep_actions.py` | - | 深层 Playwright 动作计划风险校验：新增/保存/提交/审核测试数据护栏 |
| `lib/har_chain_probe.py` | - | HAR 原始链路探测：lookup 预热、showForm/billFormId、默认上下文、写库锚点经验画像 |
| `scripts/playwright_discover.py` | - | Playwright 探索 CLI，默认不点击、不写库，输出到 `tmp/playwright_discovery/` |
| `scripts/har_chain_probe.py` | - | 从 HAR 或回归 manifest 生成脱敏链路经验库 |

## 模块调用链

```
Web UI (server.py + index.html)
    ↓ API 调用
har_extractor.py (HAR 预览/变量识别/组件雷达/YAML生成)
    ↓ 生成 YAML
runner.py (执行引擎: 三层防护 + Step Handlers + 断言 + 环境字段注入)
    ↓ 步骤分发
replay.py (协议层: PageId 状态机) ← diagnoser.py ← advisor.py
    ↓ HTTP
苍穹平台 batchInvokeAction.do
    ↓ 执行事件
task_manager.py (批量验收报告: PASS/FAIL + 入库证据 + 下一步)
```

Playwright 探索器是辅助知识采集层，不替代 HAR/YAML 回放：

```
scripts/playwright_discover.py --env sit
    ↓ 复用 cosmic_login 登录并注入浏览器 Cookie
lib/playwright_explorer.py
    ↓ 只读打开首页 / 可搜索目标应用 / 采集菜单候选 / 记录 batchInvokeAction 与 HAR pageId 摘要
tmp/playwright_discovery/*.json
    ↓ 为自然语言生成 YAML、pageId 经验库和 HAR 模板补全提供上下文
```

探索报告关键字段：
- `app_tree`：从全部应用入口抽取“云应用 -> 子应用”近似树，薪酬福利云类入口需识别薪酬管理、薪资核算、薪资数据集成、薪酬成本、工资条、员工薪酬服务、薪酬基础服务、中国社保等。
- `subapp_explorations`：只读点击子应用后抽取右侧业务菜单候选，记录风险等级、网络增量和菜单目录 pageId 摘要。
- `menu_sample_explorations`：仅当显式传入 `--open-menu-samples 子应用:菜单` 时打开低风险业务菜单列表页，用于采集 `menuItemClick/loadData` 的真实 pageId 链路。
- `har_summary.pageid_trace`：只保留脱敏 URL、formId/appId、ac/method、pageId 类型和片段，用于分析 L0/L1/L2/L3 链路。
- `target_app_status`：记录目标应用是否可见、是否通过应用搜索命中、是否需要降级为匹配入口，不把“目标不可见”误判成 HAR 解析失败。

常用命令：

```
scripts/playwright_discover.py --env sit --app-keyword 薪酬福利云 --record-har
scripts/playwright_discover.py --env sit --app-keyword 薪酬福利云 --drilldown-apps '薪酬管理,薪资核算,薪资数据集成,薪酬成本,工资条,员工薪酬服务,薪酬基础服务,中国社保' --record-har
scripts/playwright_discover.py --env sit --app-keyword 薪酬福利云 --open-menu-samples '薪资数据集成:业务数据提报,薪资核算:计薪人员' --record-har
scripts/playwright_discover.py --env sit --app-keyword 薪酬福利云 --open-menu-samples '薪资数据集成:业务数据提报' --record-har --deep-action-plan tmp/deep_action_plan.json --confirm-write YES_GENERATE_TEST_DATA --confirm-workflow YES_SUBMIT_OR_AUDIT_TEST_DATA
scripts/har_chain_probe.py --manifest tests/fixtures/har_regression/manifest.json --output tests/fixtures/har_regression/chain_experience_catalog.json
scripts/write_smoke_run.py --env uat --case cases/UA提报保存.yaml --confirm-write YES_GENERATE_TEST_DATA --var test_description=CRPLY_WRITE_${timestamp}
```

安全原则：默认 `--max-menu-clicks 0`，不会点击菜单；即使开启少量菜单点击，也会拦截新增、保存、提交、删除、审核、导入、上传、确定/确认等写入或高风险动作。深层动作计划只允许操作 `CRPLY_` 前缀测试数据；保存/新增/填写必须传 `YES_GENERATE_TEST_DATA`，提交/审核还必须传 `YES_SUBMIT_OR_AUDIT_TEST_DATA`。写入阶段优先使用已生成 YAML 的 `scripts/write_smoke_run.py`，且必须显式传入 `--confirm-write YES_GENERATE_TEST_DATA`。必须使用与 HAR/YAML 匹配的环境，不能把 UAT 内部模板/组织值硬搬到 SIT 写库。原始 HAR、写入事件、截图只能保存在 `tmp/` 等 ignored 目录，不能提交 Git；可提交的只能是脱敏结构摘要、规则、测试和文档。

## 核心设计决策

### 1. PageId 四层跃迁
苍穹表单协议的 pageId 不是全局唯一，而是分层的：
- L0 `root{32hex}` - 会话根（全局）
- L1 `{32hex}` - 门户级（open_portal 返回）
- L2 `{menuId}root{32hex}` - 菜单级（menuItemClick 后计算）
- L3 `{32hex}` - 表单级（getConfig 返回）

详见 architecture.md。

### 2. 变量三档识别（HAR→YAML）
- A档（必变）：number/code/name → 变量化 `${vars.test_number}`
- 智能文本变量：description/remark/memo/note → 变量化 `${vars.test_description}` 等，便于导入后编辑
- B档（环境相关）：org/position/adminorg/枚举/基础资料 → 进入 `pick_fields`，前端面板可改
- C档（响应回传）：pkValue/processInstId → 跨 step 引用
- 增强来源：在线 `MetadataResolver(getEntityType.do)` + `skills/cosmic-hr-expert/knowledge/_shared/_standard_metadata/entity_metadata`

### 3. 环境字段优先级
环境字段的权威来源按优先级排列：
- 用户手工维护值：`manual_override=true`，`auto_resolve=false`，运行期不再被缓存覆盖。
- 在线自动解析：`FieldResolver` 按当前环境和 `value_name` 解析基础资料/枚举内码。
- HAR 原始值：解析失败或未开启自动解析时保留。

手工修改环境字段时必须清理陈旧 `value_name` 或关闭 `auto_resolve`，否则会出现“UI/YAML 是 1010，运行期按旧名称解析回 1020”的问题。

### 4. SSE 实时推送
执行过程通过 Server-Sent Events 流式推送：
case_start → login_ok → session_ready → step_start/step_ok → assertion_ok → env_fields_resolved → case_done

### 5. invoke 三层防护架构
执行 invoke 时有三层自动保护：
- 预验证（_validate_pageid_before_invoke）：首次使用 form 时检查 pageId 有效性
- auto-open 补偿：每步执行前检查 pageId 存在性
- 安全网重试（invoke-retry）：可重试错误自动恢复（pop→open→loadData→重试）

详见 skills/cosmic-replay-troubleshooter/SKILL.md 第一章。

### 6. 批量验收报告
批量报告不只统计 PASS/FAIL，还会给出交付判断：

| 状态 | 含义 |
|------|------|
| `verified` | 系统检测到保存主键、成功 token 或后置查询回读证据 |
| `manual_verified` | 用户点击“人工确认已入库”，确认写入当前 YAML |
| `unverified` | 用例 PASS，但保存响应为空或缺少写入 token |
| `failed` | 保存/提交/断言失败 |
| `not_applicable` | 未识别写库步骤 |

`manual_verified` 只代表用户确认，不等价于系统自动验证；但后续同一用例 PASS 时不再进入 AI 修复队列。

### 7. AI 修复证据包
当 `next_action=ai_agent`，Web UI 的“让AI修复”会复制指令，并附带：
- `/api/tasks/{task_id}/agent-evidence/{case_name}`
- 当前 YAML
- 最近运行事件和失败事件
- 批量报告上下文
- `cosmic-replay-troubleshooter`、pageId、断言盲区、HR 知识库路径
- 安全护栏：不得删除 `menuItemClick`、`target_forms`、`pick_fields`、`no_save_failure` 来绕过问题

## 改代码前必读清单
1. 改 replay.py → 先理解 PageId 四层跃迁规则
2. 改 har_extractor.py → 先看 AC_TIER 分类 + UNIQUE_KEY_HINTS 变量集
3. 改 runner.py → Step Handler 是 @step_handler 装饰器注册式
4. 改 server.py → SSE 事件与前端 EventSource 对应
5. 新增断言 → 用 @assertion_handler 装饰器注册
6. 改 runner.py 安全网 → 先理解三层防护架构（troubleshooter skill 第一章）
7. 改 har_extractor.py 规则 → 不要静态插入 loadData（Rule 14 教训）
8. 改环境字段 → 手工值必须优先于 `auto_resolve`，并补 `tests/unit/test_env_field_resolution.py`
9. 改批量报告 → 同步 `task_manager.py`、Web UI、`report_exporter.py` 和 `tests/unit/test_task_report_acceptance.py`
10. 改 AI 修复入口 → 同步 `agent_evidence.py` 和 `cosmic-replay-troubleshooter`
11. 外发给顾问或新 AI Agent → 同步阅读 `skills/cosmic-replay-troubleshooter/references/external-consultant-handoff.md`，不要依赖历史对话记忆。

## 快速定位问题
| 症状 | 去哪看 |
|------|--------|
| 登录失败 | lib/cosmic_login.py + config/envs/*.yaml |
| pageId 404/过期 | lib/replay.py 的 page_ids 缓存和 _pending_by_app |
| 保存报错 | lib/diagnoser.py + lib/advisor.py |
| HAR 转换变量遗漏 | lib/har_extractor.py 的 `_classify_key_heuristic` / `_TEXT_VARIABLE_KEYS` / MetadataResolver |
| 环境字段改了但运行没生效 | YAML `pick_fields` 是否 `manual_override=true` 且 `auto_resolve=false`；runner `_apply_pick_fields` |
| PASS 但报告入库未验证 | task_manager.py `infer_write_status()`；保存响应是否为空；是否需要后置查询断言 |
| 人工确认后仍提示 AI | YAML `write_verification.manual_confirmed` 是否写入；批量执行是否加载了最新 YAML |
| AI 修复入口不清楚 | Web UI `copyAgentRepairPrompt()` 与 `/agent-evidence/` endpoint |
| 前端不刷新 | lib/webui/static/index.html 的 Alpine.js 响应式 |
| 用例格式错误 | 参考 cases/新增一条行政组织.yaml |
| invoke 重试/安全网 | lib/runner.py 的 _RETRYABLE_ERRORS + invoke-retry 循环 |
| target_forms 缺失 | lib/har_extractor.py 规则13 (行 2013-2069) |

## 推荐验证命令

```bash
./venv/bin/python -m pytest -q tests/unit tests/test_core.py
./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff
node --check /tmp/cosmic_index_full_script.js
```

前端脚本检查可先用以下命令提取 `index.html` 中的 Alpine 脚本：

```bash
node - <<'NODE'
const fs = require('fs');
const html = fs.readFileSync('lib/webui/static/index.html', 'utf8');
const scripts = [...html.matchAll(/<script(?:\s[^>]*)?>([\s\S]*?)<\/script>/g)]
  .map(m => m[1].trim()).filter(Boolean);
const target = scripts.find(s => s.includes('function app()'));
fs.writeFileSync('/tmp/cosmic_index_full_script.js', target);
NODE
node --check /tmp/cosmic_index_full_script.js
```
