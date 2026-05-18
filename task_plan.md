# HAR Parsing Optimization Plan

## Goal

全面梳理并优化 cosmic-replay 的 HAR 导入链路，确保从 HAR 文件解析智能用例变量和环境相关字段变量，到生成 YAML、执行用例、数据入库的全流程稳定可复现，并且不破坏既有成功用例。

## Phases

| Phase | Status | Description |
|---|---|---|
| 1 | completed | 阅读项目概览、排故技能、核心代码与现有样例，建立端到端链路图 |
| 2 | completed | 识别 8 种 HAR 导入类型、成功/失败样例与失败根因 |
| 3 | completed | 设计并实现 HAR 变量解析、YAML 生成与兼容性优化 |
| 4 | completed | 通过单测、脚本和样例回放验证修复效果与回归兼容性 |
| 5 | completed | 输出完整分析报告、问题诊断方法、修复步骤和预防措施 |
| 6 | completed | 第二阶段：实现环境字段自动解析中心、导入期一键解析、运行期安全兜底与缓存 |
| 7 | completed | 第三阶段：建立组件处理器注册表、组件覆盖率雷达和未知组件风险提示 |
| 8 | completed | 第四阶段：建立自动修复计划，支持用户确认后一键应用安全 YAML 补丁 |
| 9 | completed | 第五阶段前置增强：文本字段变量识别、实时元数据参与变量解析、cosmic-hr-expert 共享实体元数据兜底 |
| 10 | completed | 第六阶段：建立 8 类 HAR 回归样本库和规则变更影响报告门禁 |
| 11 | completed | 第七阶段：用户侧产品化收口，优化导入验收结论、直接生成 YAML 和失败后修复重跑体验 |
| 12 | completed | 第八阶段：整体 UI/UX 行业标准审计与低风险响应式、主题、可访问性基础优化 |

## Constraints

- 不能影响之前已成功解析并生成的 YAML 正常执行
- 需要覆盖 HAR 导入 -> 变量解析 -> YAML 生成 -> 用例执行 -> 数据入库链路
- 需要特别关注“岗位信息维护-新增一个岗位”和“基础资料-用人单位”两个失败样例

## Resolution

- 8 种 HAR 导入类型以 `har_uploads/` 中的 8 个样例为准，并与 `cases/` 中同名 YAML 一一对应
- 两个失败样例已定位并修复：
  - `岗位信息维护-新增一个岗位`：缺少 `adminorg` 上下文字段补偿 + 多语言数字字符串被 YAML 误转整数
  - `基础资料-用人单位`：`createorg(MainOrgProp)` 在浏览器自动带出，但 API 回放缺失，需要按上下文补偿
- `新增入职0512测试` 的剩余失败属于数据碰撞（邮箱重复），已通过邮箱变量化修复
- 第二阶段已完成环境字段自动解析中心：
  - `FieldResolver` 返回结构化解析结果，支持 resolved / ambiguous / not_found / error / skipped
  - 支持金蝶 `rows/dataindex` 与 `setLookUpListValue(data/columns)` 两类查询响应
  - 导入预览的 `pick_fields` 带 `form_id/app_id/auto_resolve/resolve_status`
  - Web UI 提供“一键解析当前环境”能力，后端使用 HAR `loadData` 初始化表单上下文
  - 运行期只在解析成功时覆盖 `value_id`，失败时保留原值，保证历史 YAML 兼容
  - 成功解析结果写入本地 `data/env_field_cache.json`，并已忽略版本管理
- 第三阶段已完成组件雷达：
  - `lib/component_registry.py` 提供金蝶组件处理器注册表
  - HAR preview 返回 `components.summary/handlers/unsupported/steps`
  - 每个 preview step 增加 `component/component_handler/component_support`
  - 导入质量评分接入组件覆盖率、未知组件和部分支持组件风险
  - Web UI 展示组件覆盖率、主要组件和未覆盖步骤
  - 8 个基准 HAR 组件雷达均达到 `unsupported=0`
- 第四阶段已完成自动修复闭环第一版：
  - `lib/repair_planner.py` 将失败归因和 advisor 建议转换为结构化修复计划
  - 支持三类安全补丁：导航步骤 optional、唯一变量随机化、必填字段插入
  - runner 在失败时推送 `repair_plan`
  - Web UI 展示“自动修复计划”，仅 `safe_to_apply=true` 的补丁可一键应用
  - 后端提供 `/api/cases/{name}/repairs/plan` 和 `/api/cases/{name}/repairs/apply`
  - 应用补丁前会生成 `.yaml.bak` 本地备份
  - 同步补齐基础兼容回归项，当前本地单元测试 `tests/unit tests/test_core.py` 共 183 条通过
- 第五阶段前置增强已完成：
  - `description/remark/memo/note/comment` 等文本字段可从 `update_fields`、保存按钮 `post_data`、分录脏数据中抽取为智能变量
  - `detect_var_placeholders(..., meta_resolver=...)` 已接入实时元数据，导入预览和 YAML 生成共享同一解析增强链路
  - `kb_loader` 已读取 `skills/cosmic-hr-expert/knowledge/_shared/_standard_metadata/entity_metadata/*.md`
  - `hbss_enterprise` 这类无独立 scenario 的实体也能获得字段标签/类型，`描述` 字段可稳定识别为 `test_description`
  - `cosmic-replay-overview` 与 `cosmic-replay-troubleshooter` 已补充变量遗漏定位和修复指引
- 第六阶段已完成回归样本库：
  - `tests/fixtures/har_regression/manifest.json` 固化 8 类 HAR 样本清单
  - `tests/fixtures/har_regression/baselines/*.json` 保存无敏感值结构基线
  - `lib/har_regression.py` 可生成基线、对比当前解析结果、输出影响等级
  - `scripts/har_regression_report.py compare --fail-on-diff` 可作为解析规则变更门禁
  - 单测覆盖值脱敏、差异分级、8 类样本基线一致性和企业 `test_description` 变量保留
- 第七阶段已完成用户侧 UI 收口：
  - HAR 预览页新增“导入验收结论”，默认只展示能否生成、变量/环境字段/未知组件摘要和下一步动作
  - 质量评分、组件雷达、原始步骤列表默认折叠到“高级诊断”
  - HAR 预览页可直接确认用例名并生成 YAML，保留“只改名称”入口
  - 执行结果页新增成功证据和失败后的“下一步建议”
  - 安全修复支持“应用并重跑”和“应用全部安全修复并重跑”
  - 修复批量任务详情隐藏面板初始化时的 Alpine 空对象警告，并补充 favicon 避免无关 404
- 第八阶段已完成 UI/UX 基础优化：
  - 新增 `UI_UX_AUDIT_REPORT.md`，记录行业参考、当前差距和后续路线
  - 修复亮色主题 `body.theme-light` 命中问题，统一暗/亮背景层次
  - Dashboard、用例详情、批量运行、日志详情和报告弹窗增加响应式布局
  - 表格改为局部横向滚动，避免移动端页面整体溢出
  - 增加 `focus-visible` 与 `prefers-reduced-motion` 基础可访问性支持

## Errors Encountered

| Error | Attempt | Resolution |
|---|---|---|
| `java.lang.Integer cannot be cast to java.lang.String` | 复现岗位用例后检查生成 YAML | 对多语言纯数字文本强制加引号，保留为字符串 |
| `请按要求填写“行政组织”` | 复现岗位新增流程并检查 `treeview.focus` | 从 `addnew/new` 上下文自动补 `pick_adminorg_ctx` |
| `请按要求填写“创建组织”` | 抓取企业新增 `loadData` 响应并试验多种注入方式 | 对 `MainOrgProp(createorg)` 自动补 `update_fields(createorg=<context id>)` |
| `邮箱重复` | 复现入职新增流程 | 将 `peremail` 识别为动态变量 `${vars.test_email}` |
| 导入期环境字段解析返回 `not_found` | 复测 `/api/env-fields/resolve` | 发现 `getLookUpList` 依赖表单初始化；改为使用 HAR 首个 `loadData` 预热表单，并补充 `setLookUpListValue` 响应解析 |
| 组件雷达对 2 个基准 HAR 报 unsupported | 扫描 8 个 HAR 的组件覆盖率 | 补充 `addsonlogicentity`、`ac=updateValue`、`saveSetting` 处理器，8 个基准 HAR 未覆盖步骤清零 |
| 自动修复可能误改业务语义 | 设计第四阶段补丁应用策略 | 只有明确定位目标且 `safe_to_apply=true` 的补丁可一键应用；基础资料缺失但无 value_id 时只提示不应用 |
| `基础资料-用人单位zaa` 的 `description/描述` 未变量化 | 检查生成 YAML 和 `getEntityType.do`/知识库链路 | 将实时元数据传入变量检测，并让 `kb_loader` 读取 cosmic-hr-expert 共享实体元数据；保存 `post_data` 不再只处理唯一字段 |
