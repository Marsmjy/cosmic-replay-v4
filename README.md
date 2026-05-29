# COSMIC REPLAY v4

> 苍穹表单协议回放自动化测试工具 — HAR 录制一键生成可执行 YAML 用例

[![Python](https://img.shields.io/badge/Python-3.10+-blue.svg)](https://python.org)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 项目定位

上传浏览器 HAR → 自动生成 YAML 测试用例 → 直接运行验证业务流 → 批量报告判断是否可交付。

苍穹平台没有开放运行期业务数据 OpenAPI，纯手工回归测试成本高。本工具直接回放 `batchInvokeAction.do` 协议，3-5 秒一条用例。

---

## 两步上手

### 第一步：部署项目

```bash
# 1. 解压 / 克隆
git clone https://github.com/Marsmjy/cosmic-replay-v4.git
cd cosmic-replay-v4

# 2. 装依赖（Python 3.10+ 必须）
pip install -r requirements.txt

# 3. 启动
./start.sh

# 4. 浏览器打开
open http://127.0.0.1:8768
```

启动后上传 HAR 即可导入生成用例。如果要**执行用例**，需要先配置苍穹账号：

```bash
cp .env.example .env
# 编辑 .env 填入：
#   COSMIC_USERNAME=你的账号
#   COSMIC_PASSWORD=你的密码
#   COSMIC_DATACENTER_ID=你的数据中心ID
```

### 第二步：录 HAR → 导入 → 运行

```
浏览器开苍穹 → F12 → Network → 勾选 Preserve log
      ↓
完整操作一遍业务（进入菜单 → 新增 → 填字段 → 保存）
      ↓
右键 Network 列表 → Save All As HAR
      ↓
Web UI → HAR 导入 → 选择刚保存的 HAR
      ↓
确认智能变量和环境字段 → 生成 YAML
      ↓
点执行 / 批量执行 → 查看验收报告
```

---

## 使用主流程

### 1. HAR 导入验收

导入 HAR 后，页面会先给出“导入验收结论”：

- 是否能生成 YAML
- 识别到哪些智能用例变量，例如编号、名称、描述、手机号、邮箱
- 识别到哪些环境相关字段，例如组织、行政组织、职位、枚举/基础资料
- 是否存在未知组件或需要人工确认的字段

环境相关字段可以在预览页直接修改。手工修改后，系统会把该字段标记为人工值，后续运行不会再被自动解析缓存覆盖。

### 2. YAML 生成与用例详情

点击生成 YAML 后，变量面板中的“智能用例变量”和“环境相关字段”以 YAML 为准展示。如果你在预览页改过环境字段，生成后的用例详情会保持一致。

环境字段保存规则：

- 自动解析值：适合跨环境按名称重新解析基础资料内码。
- 手工维护值：适合你明确知道目标内码，例如把行政组织类型从 `1020` 改成 `1010`。
- 手工值优先级最高，运行期不会再被 `auto_resolve` 覆盖。

### 3. 执行与批量验收报告

执行完成后，报告不只看 PASS/FAIL，还会判断“入库证据”：

| 状态 | 含义 | 下一步 |
|------|------|--------|
| 已验证 | 保存/提交响应包含主键、成功 token，或已有后置查询回读证据 | 可交付 |
| 人工确认 | 用户已确认该用例真实入库 | 后续同用例 PASS 时不再提示 AI |
| 未验证 | 用例 PASS，但缺少明确写入证据，例如保存响应为空 | 补断言、修 pageId，或让 AI 修复 |
| 失败 | 保存/提交或断言失败 | 查看失败分析和修复计划 |
| 不适用 | 未识别到保存/提交类写库步骤 | 按普通流程查看 |

如果你已在环境中确认数据真实入库，可以点击“人工确认已入库”。系统会把确认写入当前 YAML 的 `write_verification`，后续同一用例执行 PASS 且仅缺少自动入库证据时，会显示“人工确认”，不再提示 AI 诊断。

### 4. AI 修复入口

当报告显示“需 AI 诊断”时，点击“让AI修复”会复制一段完整修复指令，里面包含：

- 用例名、任务 ID、环境
- 问题原因
- 技术证据包链接
- 修复要求和安全护栏
- 必跑测试命令

把复制内容发给 Codex/AI Agent 即可。技术证据包主要给 AI/研发使用，普通使用者不需要手工阅读。

---

## 给 AI Agent 用

项目自带排故 Skill 包，直接加载即可获得排故能力：

```bash
# Claude Code
claude --load skills/cosmic-replay-troubleshooter

# 安装到 QoderWork
cp -r skills/cosmic-replay-troubleshooter ~/.qoderwork/skills/

# 安装到 Cursor
cp skills/cosmic-replay-troubleshooter/SKILL.md .cursor/rules/
```

Skill 包含完整的故障因果链和修复方案（变量识别漏报 / 环境字段覆盖 / pageId 追踪断裂 / save 不落库 / PASS 但入库未验证等），AI 读完后能自动诊断并给出最小补丁。

批量报告中的“让AI修复”会生成一段可直接交给 Agent 的提示词，并附带 `/api/tasks/{task_id}/agent-evidence/{case_name}` 证据包。

---

## 常见踩坑

| 问题 | 原因 | 解决 |
|------|------|------|
| `TypeError: unsupported operand type(s) for` | Python < 3.10 | 升级到 Python 3.10+ |
| `RSA 加密库不可用` | 缺 pycryptodome | `pip install pycryptodome` |
| `找不到 cosmic-login skill` | 环境变量未设 | 用 `./start.sh` 启动（自动处理），或手动 `export COSMIC_LOGIN_SCRIPT=项目路径/lib/cosmic_login.py` |
| save 返回空 `[]` 且 PASS | 缺少自动入库证据，可能是 pageId 链路或断言盲区 | 先确认数据是否真实入库；已入库可点“人工确认已入库”，未入库点“让AI修复” |
| `XXX 已存在` | 编码/名称没随机化 | 检查 YAML 的 vars 段是否用了 `${rand:N}` |
| PASS 但数据未入库 | 断言或 pageId 链路有问题 | 查看批量报告“入库证据”，让 AI 按证据包修复 |
| 预览页改了环境字段，运行时又变回旧值 | 旧版本会被 auto_resolve 缓存覆盖 | 升级到当前版本；手工修改字段会自动关闭 auto_resolve |

---

## 目录结构

```
cosmic-replay-v4/
├── lib/              # 核心逻辑（变量识别/执行引擎/苍穹 API 封装）
│   ├── har_extractor.py      # HAR解析 + 变量识别核心
│   ├── runner.py             # 执行引擎（三层防护架构）
│   ├── replay.py             # 苍穹协议回放API
│   ├── field_resolver.py     # 环境相关字段跨环境解析
│   ├── component_registry.py # HAR 组件雷达
│   ├── har_regression.py     # 10类HAR回归影响报告（8个SIT + 2个UAT）
│   ├── task_manager.py       # 批量任务与验收报告
│   ├── agent_evidence.py     # AI修复证据包
│   ├── report_exporter.py    # 离线HTML报告导出
│   ├── cosmic_login.py       # 苍穹RSA登录
│   └── webui/                # Web UI 服务
├── cases/            # YAML 测试用例资产
├── skills/           # AI Agent Skill 包
│   ├── cosmic-hr-expert/          # HR领域知识库（558场景）
│   ├── cosmic-replay-overview/    # 项目概览
│   └── cosmic-replay-troubleshooter/  # 排故诊断指南
├── config/           # 环境配置
│   └── envs/         # 多环境配置文件
├── deploy/           # 监控部署配置（Prometheus/Alertmanager）
├── tests/            # 单元 & 集成测试
├── scripts/          # 工具脚本
├── .env.example      # 环境变量模板
├── requirements.txt  # Python 依赖
└── _start_webui.py   # 启动入口
```

---

## 顾问交付包

如果需要把完整项目交给外部顾问或 AI Agent，本地生成脱敏交付包：

```bash
./venv/bin/python scripts/package_consultant_handoff.py
```

脚本会输出 `dist/cosmic-replay-consultant-handoff-YYYYMMDD.zip`，并自动排除 `.git`、`venv`、`data`、`logs`、`har_uploads`、真实环境配置、数据库、HAR、cookie/token 和旧压缩包等不应外发的内容。

---

## Playwright 菜单/表单探索（实验能力）

用于让 AI Agent 在真实金蝶环境里做“只读探索”：登录、打开首页、采集菜单候选、捕获 `batchInvokeAction.do` / 元数据请求，为后续自然语言生成 YAML、HAR 模板补全和 pageId 链路分析提供知识。

首次使用需要安装浏览器内核：

```bash
./venv/bin/pip install -r requirements.txt
./venv/bin/python -m playwright install chromium
```

默认只采集，不点击任何菜单：

```bash
./venv/bin/python scripts/playwright_discover.py --env sit
```

围绕某个应用做只读探索并录制本地 HAR：

```bash
./venv/bin/python scripts/playwright_discover.py --env sit --app-keyword 薪酬福利云 --record-har
```

探索器会先尝试直接识别目标应用；若首页没有文字入口，会只读打开左上角“全部应用”入口，并在“搜索应用/表单”中尝试目标关键词及安全变体（如“薪酬”“薪资”“福利”）。如果当前账号看不到目标应用，报告会记录为目标应用不可见，而不是误判为 HAR 解析失败。

报告中会额外输出 `app_tree`，用于沉淀“云应用 → 子应用”的近似树。例如薪酬福利云会识别薪酬管理、薪资核算、薪资数据集成、薪酬成本、工资条、员工薪酬服务、薪酬基础服务、中国社保等入口，后续再按 Level 0/1/2 分级采集具体菜单和 HAR 样本。

只读展开子应用菜单：

```bash
./venv/bin/python scripts/playwright_discover.py \
  --env sit \
  --app-keyword 薪酬福利云 \
  --drilldown-apps '薪酬管理,薪资核算,薪资数据集成,薪酬成本,工资条,员工薪酬服务,薪酬基础服务,中国社保' \
  --record-har
```

该命令只展开应用菜单，不点击具体业务菜单；报告中的 `subapp_explorations` 会记录每个子应用下的菜单候选、风险等级、`getMenuData` 网络摘要和脱敏 pageId 片段。

显式打开少量低风险业务菜单列表页，采集真正的 `menuItemClick/loadData` 链路：

```bash
./venv/bin/python scripts/playwright_discover.py \
  --env sit \
  --app-keyword 薪酬福利云 \
  --open-menu-samples '薪资数据集成:业务数据提报,薪资核算:计薪人员' \
  --record-har
```

`--open-menu-samples` 只接受 `子应用:菜单` 格式，并会拦截新增、保存、提交、删除、审核、导入、上传以及中高风险菜单。默认不启用，必须显式指定。

### 受控写入 Smoke

当你明确允许生成测试数据时，推荐先复用已验证 HAR/YAML runner，而不是让 Playwright 直接猜字段：

```bash
./venv/bin/python scripts/write_smoke_run.py \
  --env uat \
  --case cases/UA提报保存.yaml \
  --confirm-write YES_GENERATE_TEST_DATA \
  --var test_description=CRPLY_WRITE_${timestamp}
```

写入 smoke 会从 `config/envs/<env>.yaml` 读取环境信息，不打印账号密码；执行证据写入 `tmp/write_smoke_runs/`，该目录不入 Git。脚本只接受包含保存/提交/确认动作的 YAML，并要求显式 `--confirm-write`，避免误写库。注意使用与 HAR/YAML 匹配的环境；例如 UA 提报保存样本是 UAT 链路，跨到 SIT 会因为组织/模板内部值不同而在保存前校验失败。

### 深链路 Playwright 录制

更深层的新增、保存、提交、审核探索使用动作计划 JSON。动作计划只允许操作 `CRPLY_` 前缀测试数据；保存/新增/填写必须传 `YES_GENERATE_TEST_DATA`，提交/审核还必须额外传 `YES_SUBMIT_OR_AUDIT_TEST_DATA`。删除、反审核、导入、上传、批量等动作当前硬拦截。

```json
{
  "name": "salary-deep-sample",
  "owned_test_data": true,
  "test_prefix": "CRPLY_DEEP",
  "actions": [
    {"type": "click_text", "text": "新增"},
    {"type": "fill_text", "label": "名称", "value": "CRPLY_DEEP_${timestamp}"},
    {"type": "wait_for_selector", "selector": ".kd-modal-container"},
    {"type": "snapshot_controls", "selector": "body", "text_filter": "规则分组", "limit": 40},
    {"type": "click_selector", "selector": ".kd-btn-primary", "risk": "write"},
    {"type": "click_at", "x": 1060, "y": 678, "risk": "write"},
    {"type": "fill_at", "x": 520, "y": 240, "value": "中国", "press": "Enter"},
    {"type": "press", "selector": "input[role=combobox]", "key": "Enter"},
    {"type": "click_text", "text": "保存"},
    {"type": "click_text", "text": "提交"}
  ]
}
```

动作计划支持 `${timestamp}`、`${today}`、`${rand:N}` 占位符。`snapshot_controls` 是只读动作，会把可见按钮、输入框、role、placeholder、文本和 selector 线索写入本地探索报告；`click_selector`、`click_at`、`fill_at`、`press`、`select_option` 默认按写操作处理，必须显式传 `--confirm-write YES_GENERATE_TEST_DATA`；只有确认是纯 UI 展开/切页时，才允许给点击动作标 `risk: read`。这类 selector/坐标能力主要用于录制“规则分组/常用筛选”、F7 弹窗、明细表格等文本定位不稳定的组件。

```bash
./venv/bin/python scripts/playwright_discover.py \
  --env sit \
  --app-keyword 薪酬福利云 \
  --open-menu-samples '薪资数据集成:业务数据提报' \
  --record-har \
  --deep-action-plan tmp/deep_action_plan.json \
  --confirm-write YES_GENERATE_TEST_DATA \
  --confirm-workflow YES_SUBMIT_OR_AUDIT_TEST_DATA

./venv/bin/python scripts/playwright_discover.py \
  --env sit \
  --app-keyword 薪酬福利云 \
  --open-menu-samples '薪资核算:薪资核算场景' \
  --record-har \
  --deep-action-plan tests/fixtures/deep_chain_factory/salary_calc_scene_common_filter_snapshot_plan.json \
  --confirm-write YES_GENERATE_TEST_DATA \
  --output tmp/playwright_discovery/salary_calc_scene_common_filter_snapshot.json

./venv/bin/python scripts/har_chain_probe.py \
  --manifest tests/fixtures/har_regression/manifest.json \
  --output tests/fixtures/har_regression/chain_experience_catalog.json
```

推荐闭环：Playwright 录 HAR → `scripts/har_chain_probe.py` 生成链路画像 → HAR 导入生成 YAML → `scripts/write_smoke_run.py` 受控执行 → `scripts/deep_chain_pipeline.py` 生成场景报告 → 稳定样本加入 HAR 回归 baseline 或深链路经验库。

查看当前深链路场景工厂进展：

```bash
./venv/bin/python scripts/deep_chain_pipeline.py status
```

生成下一批真实探索样本扩容计划（只输出 L0/L1/L2 风险分级和推荐命令，不执行写库）：

```bash
./venv/bin/python scripts/deep_chain_pipeline.py expansion-plan --limit 5
```

为某个样本生成脱敏闭环报告（原始 HAR 和 smoke 证据仍只放在 `tmp/`，不提交）：

```bash
./venv/bin/python scripts/deep_chain_pipeline.py scenario-report \
  --scenario-id salary_item_new_validation \
  --smoke-evidence tmp/write_smoke_runs/deep_salary_item_fixture_20260528.json
```

为某个 YAML 单独生成后置回查计划：

```bash
./venv/bin/python scripts/deep_chain_pipeline.py readback-plan \
  --case tests/fixtures/deep_chain_factory/salary_calc_scene_protocol_save.yaml
```

报告会统一输出：当前成熟度、HAR 链路风险、YAML smoke 结果、失败分类、入库验证策略。若保存响应只有“保存成功”但没有主键，系统会建议按编码/名称/描述等业务键做后置回查；`readback-plan` 会给出推荐查询表单、首选业务键、兜底业务键和安全边界。不能把 PASS 直接等同于真实入库。

也可以把新 HAR/YAML 与已闭环样本做脱敏经验匹配：

```bash
./venv/bin/python scripts/deep_chain_pipeline.py match-experience \
  --case cases/demo.yaml \
  --har tmp/playwright_hars/demo.har
```

`match-experience` 只比较 `form_id/app_id`、pageId 链路特征、lookup/showForm/write anchor 等结构信息，不读取或提交业务值、cookie、token。AI 证据包也会附带 `experience_matches`，便于失败时优先复用相似样本的 pageId、F7、子弹窗和入库回查经验。

下一阶段闭环可直接使用 `run-scenario` 编排。默认只做 HAR 画像、YAML 生成/复用、经验匹配、入库回查计划和报告，不会写库：

```bash
./venv/bin/python scripts/deep_chain_pipeline.py run-scenario \
  --scenario-id salary_calc_scene_rule_group_blocked \
  --har tmp/playwright_hars/new_sample.har \
  --case-output tmp/deep_chain_pipeline/new_sample.yaml \
  --output tmp/deep_chain_pipeline/new_sample_report.json
```

确认测试数据安全后，才允许显式执行写入 smoke：

```bash
./venv/bin/python scripts/deep_chain_pipeline.py run-scenario \
  --scenario-id salary_calc_scene_rule_group_blocked \
  --case tmp/deep_chain_pipeline/new_sample.yaml \
  --run-smoke \
  --confirm-write YES_GENERATE_TEST_DATA \
  --output tmp/deep_chain_pipeline/new_sample_report.json
```

`run-scenario` 会输出当前状态、生成/复用的 YAML、smoke 证据、是否可沉淀 baseline、下一步建议。写库仍由 `scripts/write_smoke_run.py` 执行，必须显式确认，避免自动流水线误点保存、提交或审核。

`readback-plan` 输出的 `suggested_assertion` 可以复制到 YAML `assertions` 中，例如：

```yaml
assertions:
  - type: readback_by_business_key
    form_id: hsas_payrollscene
    app_id: hsas
    field_key: number
    value: ${vars.test_number}
```

该断言只做 `commonSearch`/已有回查响应校验，不会新增、保存或提交。断言通过后，批量报告会把这条用例计为“入库已验证”。

在 Web UI 导入 HAR 时，如果系统识别到稳定业务键，会默认勾选“生成时附加入库回查断言”；命令行也可使用：

```bash
./venv/bin/python -m lib.har_extractor extract input.har -o cases/demo.yaml --with-readback-assertions
```

当前已沉淀的薪酬福利云深链路经验在 `tests/fixtures/deep_chain_factory/catalog.json`：

- `薪资数据集成 / 业务数据提报`：UAT 写入 smoke 通过，覆盖主表、选人弹窗、子弹窗明细和保存。
- `薪资核算 / 薪酬项目类别`：SIT 写入 smoke 通过，覆盖菜单 L2、新增 L3、`treeview.focus`、`taglevel` lookup 预热和弹窗 `btnsave` 保存。
- `薪资核算 / 薪酬项目`：SIT 写入 smoke 通过，覆盖菜单 L2、新增 L3、`salaryitemtype` lookup 自动解析、`ispayoutitem` 枚举和标准 `bar_save` 保存。
- `薪资核算 / 薪资期间`：SIT 写入 smoke 通过，覆盖半月频度、起始规则、`advcontoolbarap/newentry` 分录增行、日期分录和标准 `bar_save` 保存。
- `薪资核算 / 薪资核算组`：SIT 写入 smoke 通过，覆盖 `country/currency/exratetable` 三个必填基础资料字段的 lookup 预热和标准 `bar_save` 保存。
- `薪资核算 / 薪资回溯原因`：SIT 写入 smoke 通过，覆盖 `bos_list` 的 `billFormId` 别名绑定、L2 列表到 L3 编辑态切换、描述字段变量化和标准 `bar_save` 保存。
- `薪资核算 / 薪资核算场景`：SIT 写入 smoke 通过，覆盖 `country -> labelap4 -> hsas_salarycalcstyle F7 -> select_f7_list_row -> btnok`，确认响应回填 `groupcontent/entryentity` 后标准 `bar_save` 保存成功；仅补选 `callistrule` 不会生成常用筛选行，不能通过硬补 save 或删除断言绕过。
- `中国社保 / 社保体系`：当前账号下未发现可写新增入口，按只读/需人工确认处理。

经验原则：Playwright 更适合采集真实菜单、新增、弹窗和 pageId 链路；真正验证“可回放、可入库”应落到 YAML runner。若 Playwright 原生填输入框没有形成 `updateValue/save` 请求，不要误判为 HAR 解析问题，应改为用采集到的 L2/L3 链路生成协议 YAML，再用 `write_smoke_run.py` 验证。

如果要试探性打开少量安全菜单，可以显式指定点击数量：

```bash
./venv/bin/python scripts/playwright_discover.py --env sit --max-menu-clicks 5
```

安全边界：

- 默认 `--max-menu-clicks 0`，不会点击菜单，更不会写库。
- 即使开启菜单点击，也会拦截“新增、保存、提交、删除、审核、导入、上传、确定、确认”等写入/高风险动作。
- 探索报告写到 `tmp/playwright_discovery/`；原始 HAR 写到 `tmp/playwright_hars/`，HAR 可能包含 cookie/token，目录已被 `.gitignore` 排除，不能提交或外发。
- 该能力用于补知识和生成候选 YAML，不替代现有 HAR/YAML 回放和 HAR 回归门禁。

---

## 排故索引

| 症状 | 排查方向 |
|------|---------|
| 变量没识别 / 硬编码值 | 模式 A — 检查 `UNIQUE_KEY_HINTS` / `ENV_RELATED_FIELDS` |
| 环境字段手工值未生效 | 检查 `pick_fields` 是否为 `manual_override=true` 且 `auto_resolve=false` |
| pageId 不对 / 404 | 模式 B — 检查 `menuItemClick` 步骤 / `_pending_by_app` 链路 |
| save 返回空 `[]` | 模式 B-4 — `L2 pageId` 屏蔽了 `_pending_by_app` |
| PASS 但入库未验证 | 模式 G — 查看批量报告入库证据；必要时生成 AI 证据包 |
| 启动时报找不到脚本 | 模式 D — `_find_login_script()` / `_load_dotenv()` |

完整排故见 `skills/cosmic-replay-troubleshooter/SKILL.md` 和 `skills/cosmic-replay-troubleshooter/references/pageid-chain-debugging.md`。
