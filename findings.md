# Findings

## Architecture

- 项目主链路为 `HAR -> lib/har_extractor.py -> YAML case -> lib/runner.py -> lib/replay.py -> 苍穹 batchInvokeAction.do`
- `skills/cosmic-replay-overview/skill.md` 明确指出变量体系为 A/B/C 三档，`runner.py` 负责执行期变量解析，`field_resolver.py` 负责基础资料跨环境解析
- `skills/cosmic-replay-troubleshooter/SKILL.md` 提供 pageId 三层防护、`target_forms` 机制和常见失效模式，尤其强调 L2/L3 pageId、save 空返回、变量漏识别等问题

## Investigation Targets

- `lib/har_extractor.py`: HAR 解析、变量识别、YAML 生成规则
- `lib/runner.py`: YAML 解析、变量替换、pick_fields 注入、执行与断言
- `lib/replay.py`: pageId 生命周期、open/load/invoke 行为
- `lib/field_resolver.py`: 基础资料/环境相关字段解析逻辑
- `cases/`, `har_uploads/`, `logs/`, `scripts/`: 现有样例与验证脚本

## Root Causes

- `岗位信息维护-新增一个岗位`
  - HAR 中没有显式 `adminorg` 赋值动作，真实值藏在 `click_tblnew.post_data.treeview.focus`
  - 生成 YAML 时 `posorientation/posduty/posstandard` 的多语言数字文本被 YAML 反序列化成整数，执行期触发 `Integer -> String` 类型错误
- `基础资料-用人单位`
  - 浏览器录制时 `load_enterprise_2` 返回 `createorg` 默认值；API 回放时同一步骤返回 `createorg=null` + `showFieldTips`
  - `createorg` 是 `MainOrgProp`，不能用通用 `pick_basedata` 方式补；实测 `update_fields(createorg='100000')` 可以通过
- `新增入职0512测试`
  - 失败已不属于 HAR 解析链路问题，而是固定邮箱值导致数据重复
  - `peremail` 之前未被纳入动态变量识别

## Implemented Fixes

- `lib/har_extractor.py`
  - 保留“列表/树 -> 卡片”上下文桥接步骤，并将桥接动作从 `optional` 提升为必执行
  - 为缺失的上下文字段新增统一补偿逻辑 `_inject_context_field_steps()`
  - 从 `treeview.focus` 自动补组织类字段（如 `adminorg`）
  - 从 `commonSearch` 默认值自动补 `MainOrgProp` 字段（如 `createorg`）
  - 将 `treeview.focus`、`createorg`、`useorg` 纳入 `pick_fields`
  - 为邮箱字段增加变量化支持
  - 多语言数字文本始终按字符串输出 YAML
- `lib/runner.py`
  - `pick_*` 覆盖不再只作用于 `pick_basedata`，也能覆盖上下文补偿生成的 `update_fields`

## Verification

- 单元回归：`pytest -q tests/unit/test_har_extractor_regressions.py` 通过（6/6）
- 实跑验证：基于当前 `build_yaml_case()` 重新生成后，`har_uploads/` 中 8 个 HAR 已全部成功执行
- 关键失败样例转绿：
  - `基础资料-用人单位`
  - `岗位信息维护-新增一个岗位`
  - `新增入职0512测试`

## Stage 2 Findings

- 金蝶基础资料在线查询存在两类常见返回：
  - 列表数据形态：`rows + dataindex`
  - 控件回填形态：`setLookUpListValue` 中的 `data + columns`
- 独立导入期解析不能只 `init_root/open_form`；`getLookUpList` 依赖目标表单的 `loadData` 初始化，否则会出现“页面未初始化或者已经过期”并返回空候选。
- 运行期自动解析更安全，因为用例已经走到真实表单上下文；导入期一键解析需要从 HAR 中抽取同表单首个 `loadData` 做轻量预热。
- 自动解析必须保持保守策略：只有 `resolved` 才覆盖 `value_id`；`ambiguous/not_found/error` 都保留 HAR 原值并提示人工确认，避免破坏已成功 YAML。
- 在线复测样例：`岗位信息维护-新增一个岗位` 的 `pick_adminorg_id`、`pick_changedesc_id`、`pick_positiontype_id` 均可在 `/api/env-fields/resolve` 中精确解析。

## Stage 3 Findings

- 组件插件化适合先从诊断层切入，而不是立即重构 `har_extractor.py` 主链路；先打标签和统计覆盖率，可以快速定位新 HAR 的未知协议点，同时不影响历史 YAML 生成。
- 当前注册表已覆盖常见金蝶组件：
  - 表单打开/加载
  - 字段更新
  - 基础资料选择与查询
  - 树上下文和树导航
  - 列表桥接导航
  - 门户/应用导航
  - 保存/提交/审核
  - 新增/修改态
  - 弹窗确认
  - 分录/表格组件
  - 后台任务/侧边栏
  - 用户偏好/首页设置
  - 业务模型结构操作
- 8 个基准 HAR 扫描结果均为 `unsupported=0`；部分样例仍有较多 `partial`，主要来自门户导航、分录表格、弹窗确认、通用低风险动作，这些是后续迁移为专用 handler 的优先队列。
- 质量评分已接入组件雷达，新增 HAR 若出现未知 ac/method，会在导入预览阶段暴露为 compatibility 风险，而不是等执行失败后才定位。

## Stage 4 Findings

- 自动修复必须区分“可建议”和“可应用”：
  - 导航 apphome/侧栏失败可以安全 optional，但主业务表单不能自动 optional。
  - 唯一值重复可以安全追加随机后缀，但已有 `${rand}` 或 `${timestamp}` 时不重复修改。
  - 必填字段缺失只有在能推断字段 key、主表单 app_id 和安全值时才一键插入。
  - 基础资料缺失如果没有明确 `value_id`，只展示计划，不一键应用。
- 结构化修复计划比 YAML 片段更适合前端和 API：
  - `operation` 描述修复动作。
  - `target` 描述修改位置。
  - `payload` 描述写入内容。
  - `safe_to_apply` 决定是否展示一键应用按钮。
- 第一版修复闭环已覆盖三类高频问题：
  - `navigation_service_unavailable -> mark_step_optional`
  - `business_duplicate / duplicate -> refresh_unique_var`
  - `missing_required -> insert_missing_field`
- 更宽的本地单元测试曾暴露历史兼容缺口：
  - HAR 字段分类 helper 只存在于内部闭包，测试和外部诊断无法复用。
  - 轻量 YAML 解析、变量引用空白、旧格式错误 action、日志 store 元信息、凭证环境变量覆盖存在边界不一致。
  - 已补齐后 `tests/unit tests/test_core.py` 183 条通过，可作为第五阶段回归样本库的基础门禁。

## Stage 5 Prework Findings

- `基础资料-用人单位zaa` 的漏识别不是执行器问题，而是导入期变量抽取问题：
  - HAR 保存动作 `click_9.post_data` 中包含 `description={"zh_CN":"aaaaaa","zh_TW":"aaaaaa"}`。
  - 旧逻辑只对唯一字段白名单处理保存脏数据，普通文本字段不会进入 `maybe_var()`。
  - 结果是 YAML 中直接写死 `"aaaaaa"`，新导入类似 HAR 会重复出现同类问题。
- `/feature_sit_hrpro/metadata/getEntityType.do?entityId=` 原本已通过 `MetadataResolver` 接在 preview/extract 后端，但变量检测没有接收 `meta_resolver`，因此在线字段类型只能增强标签和基础资料信息，不能增强变量分类。
- `cosmic-hr-expert` 原本通过 `kb_loader` 使用 `scenarios/<form_id>`，但 `hbss_enterprise` 只有共享标准实体元数据文件，没有独立 scenario 目录，导致 `main_form_not_in_kb` 误报且字段标签兜底不足。
- 标准化后的字段信息优先级应为：
  - 实时元数据 `MetadataResolver(getEntityType.do)`：当前环境最新，优先用于字段类型/标签。
  - 项目知识库 `cosmic-hr-expert/scenarios`：场景级规则、字段分类和业务菜单。
  - 共享实体元数据 `cosmic-hr-expert/_shared/_standard_metadata/entity_metadata`：覆盖无 scenario 的标准 HR 实体。
  - 静态全局字段标签和 key 启发式：最后兜底。
- `cosmic-replay-troubleshooter` 应覆盖变量遗漏问题，因为这类问题往往不是立即执行报错，而是二次执行数据重复、固定描述污染或跨环境执行失败；需要在导入预览阶段就能定位。

## Stage 6 Findings

- 直接把完整生成 YAML 当作回归基线不合适：
  - YAML 中可能包含录制环境的基础资料内码、组织名、测试人名等敏感或环境相关值。
  - 完整 YAML diff 噪声较大，不利于快速判断“解析规则变化是否危险”。
- 更稳的基线格式是“结构摘要”：
  - 变量只记录变量名、标签和值形态，不记录实际模板前缀或字面量。
  - 基础资料只记录字段 key、标签、环境敏感级别、解析状态和值形态，不记录真实 `value_id/value_name`。
  - 步骤只记录 id/type/form/ac/method/key、optional、字段 key 和值形态。
  - preview 只记录质量评分、风险 code、组件覆盖率和 handler 列表。
- 影响分级比简单 diff 更有用：
  - `breaking`：主表单、步骤结构、阻塞质量项、未知组件等可能影响执行的变化。
  - `review`：变量、环境字段、断言等需要人工确认但不一定失败的变化。
  - `info`：HAR 源哈希等来源信息变化。
- 当前 8 个基准 HAR 已形成第一版基线，`compare --fail-on-diff` 可作为后续解析规则变更前的轻量门禁；真实环境写库试跑仍作为高风险变更后的二级验证。

## Stage 7 Findings

- 用户主路径不应该暴露太多工程诊断：
  - 质量评分、组件雷达、影响报告对研发有价值，但普通用户只需要知道能否生成 YAML、是否需要确认环境字段、执行失败后如何修。
  - 因此导入页应默认展示“验收结论 + 变量/环境字段配置 + 生成 YAML”，把质量/组件/步骤明细折叠到高级诊断。
- 当前系统能力已经完整，主要欠缺的是“失败路径的行动按钮”：
  - 原来 failure_analysis、fixes、repair_plan 都在页面上，但用户需要自己理解每块含义。
  - 新交互把失败位置、根因和“应用安全修复并重跑”放到最上面，降低排障成本。
- 前端隐藏区域仍会执行 Alpine 表达式：
  - `x-show` 只控制可见性，不阻止内部表达式求值。
  - 对依赖 nullable 对象的大块详情页，应使用 `template x-if` 防止初始化噪声。

## Stage 8 Findings

- 行业标准更关注“复杂度被结构化”，而不是单纯视觉炫酷：
  - 对 cosmic-replay 这类 HAR 自动化工具，首要体验指标是主路径清晰、状态可见、错误可恢复、跨环境配置可理解。
  - 当前宇宙主题方向可以保留，但应逐步沉淀成 token 和组件，而不是散落在内联 Tailwind class 与局部 CSS 中。
- 移动端/窄屏是当前 UI 的明显短板：
  - 之前 Dashboard 表格和 4 列统计会挤压视口，导致页面级横向溢出。
  - 修复后页面宽度稳定，表格在局部容器内横向滚动，符合企业数据表移动端的常见处理方式。
- 亮色主题存在选择器未命中问题：
  - `body` 自身带 `theme-light` class 时，`.theme-light body` 不会匹配。
  - 应使用 `body.theme-light`，并继续把主题变量集中到 CSS token。
- 下一阶段最值得做的是设计系统化：
  - 抽象按钮、状态卡、表格、空状态、告警和修复建议组件。
  - 把 Chart.js 等外部资源本地化，减少内网环境不稳定因素。
- 设计系统化可以渐进迁移，不需要一次性重写页面：
  - 先建立通用 CSS class，再从 Dashboard 主路径、执行结果状态卡和表格容器这些高频区域开始替换。
  - 这样能降低视觉维护成本，同时避免影响 HAR 解析和执行链路。
- 导出报告需要比 Web UI 更强的离线能力：
  - Web UI 可通过 `/static` 加载本地资源。
  - 导出的 HTML 文件离开服务后仍要能打开，因此 Tailwind runtime 和 Chart.js 更适合内嵌。
