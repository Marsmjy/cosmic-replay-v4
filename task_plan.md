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

## Errors Encountered

| Error | Attempt | Resolution |
|---|---|---|
| `java.lang.Integer cannot be cast to java.lang.String` | 复现岗位用例后检查生成 YAML | 对多语言纯数字文本强制加引号，保留为字符串 |
| `请按要求填写“行政组织”` | 复现岗位新增流程并检查 `treeview.focus` | 从 `addnew/new` 上下文自动补 `pick_adminorg_ctx` |
| `请按要求填写“创建组织”` | 抓取企业新增 `loadData` 响应并试验多种注入方式 | 对 `MainOrgProp(createorg)` 自动补 `update_fields(createorg=<context id>)` |
| `邮箱重复` | 复现入职新增流程 | 将 `peremail` 识别为动态变量 `${vars.test_email}` |
| 导入期环境字段解析返回 `not_found` | 复测 `/api/env-fields/resolve` | 发现 `getLookUpList` 依赖表单初始化；改为使用 HAR 首个 `loadData` 预热表单，并补充 `setLookUpListValue` 响应解析 |
| 组件雷达对 2 个基准 HAR 报 unsupported | 扫描 8 个 HAR 的组件覆盖率 | 补充 `addsonlogicentity`、`ac=updateValue`、`saveSetting` 处理器，8 个基准 HAR 未覆盖步骤清零 |
