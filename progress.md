# Progress Log

## 2026-05-15

- 确认仓库内存在 `skills/cosmic-replay-troubleshooter/SKILL.md` 与 `skills/cosmic-replay-overview/skill.md`
- 阅读 README、overview、architecture、troubleshooter，建立基础链路认知
- 初始化 `task_plan.md`、`findings.md`、`progress.md` 作为本轮分析与修复的持久记录
- 修复 `lib/har_extractor.py` 的主流程问题：
  - 保留无 portal 场景下的列表/树上下文桥接
  - 避免错误注入 `open_form(main_form)`
  - 多语言纯数字 YAML 强制字符串化
- 进一步实现上下文字段自动补偿：
  - `treeview.focus -> pick_adminorg_ctx`
  - `commonSearch/useorg.id -> fill_createorg_ctx`
  - `treeview.focus` / `createorg` / `useorg` 同步纳入 `pick_fields`
- 扩展变量识别：
  - `peremail/email` 自动抽取为 `${vars.test_email}`
- 修改 `lib/runner.py`：
  - `pick_*` 配置可覆盖 `update_fields` 中的上下文补偿字段
- 新增回归测试 `tests/unit/test_har_extractor_regressions.py`
  - 当前共 6 条回归用例，全部通过
- 使用当前代码重新生成并执行 `har_uploads/` 中全部 8 个 HAR
  - 结果：8/8 成功执行

## 2026-05-18

- 完成第二阶段“环境字段自动解析中心”：
  - `lib/field_resolver.py` 增加结构化解析结果、候选项解析、环境级本地缓存
  - 支持 `rows/dataindex` 与 `setLookUpListValue(data/columns)` 两种金蝶查询返回
  - `lib/har_extractor.py` 在 `pick_fields` 中输出 `form_id/app_id/auto_resolve/resolve_status`
  - `lib/runner.py` 在执行 `pick_basedata` 前按当前环境自动解析，并只在成功时覆盖 `value_id`
  - `lib/webui/server.py` 增加 `/api/env-fields/resolve`，支持通过 HAR 首个 `loadData` 初始化表单上下文后批量解析
  - `lib/webui/static/index.html` 增加“一键解析当前环境”按钮和解析状态展示
- 验证结果：
  - `pytest -q tests/unit/test_env_field_resolution.py tests/unit/test_har_extractor_regressions.py tests/unit/test_quality_and_failure_analysis.py`：19 passed
  - `python3 -m py_compile lib/field_resolver.py lib/runner.py lib/har_extractor.py lib/har_quality.py lib/failure_analysis.py lib/webui/server.py`：通过
  - Web UI 已重启到 `127.0.0.1:8768`
  - `/api/har/preview` 对岗位 HAR 返回 `metadata_status=online`
  - `/api/env-fields/resolve` 对岗位 HAR 前 3 个字段返回精确解析
  - 8 个基准 HAR 离线 YAML 生成均可解析，未发现兼容性回归

## 2026-05-18 第三阶段

- 完成“组件处理器注册表 / 组件雷达”：
  - 新增 `lib/component_registry.py`
  - `preview_har()` 输出 `components` 报告，并为每个 step 增加组件标签
  - `assess_preview_quality()` 接入组件覆盖率、未知组件和部分支持组件风险
  - Web UI HAR 导入页新增“组件雷达”卡片和 step 组件标签
- 已覆盖并登记的处理器：
  - 表单打开、表单加载、字段更新、基础资料选择、地理级联、基础资料查询
  - 树上下文、树导航、列表导航、门户导航
  - 保存/提交/审核、新增/修改态、弹窗确认、分录表格、后台任务
  - 业务模型结构操作、用户偏好设置、通用低风险动作
- 发现并处理：
  - `业务模型添加一个基础资料附表` 暴露 `addsonlogicentity` 与 `ac=updateValue/method=click`
  - `新增入职0512测试` 暴露 `saveSetting`
  - 已补充 handler，8 个基准 HAR 的 `unsupported_steps` 均为 0
- 验证结果：
  - `pytest -q tests/unit/test_component_registry.py tests/unit/test_env_field_resolution.py tests/unit/test_har_extractor_regressions.py tests/unit/test_quality_and_failure_analysis.py`：23 passed
  - 8 个基准 HAR 组件覆盖率扫描：全部 `unsupported=0`

## 2026-05-18 第四阶段

- 完成“自动修复闭环”第一版：
  - 新增 `lib/repair_planner.py`
  - runner 失败时除 `failure_analysis` 和 `fixes` 外，额外生成 `repair_plan`
  - Web UI 运行结果页新增“自动修复计划”，支持一键应用安全补丁
  - 后端新增 `/api/cases/{name}/repairs/plan` 和 `/api/cases/{name}/repairs/apply`
- 支持的修复操作：
  - `mark_step_optional`：非主导航服务失败时，将对应步骤标记 optional
  - `refresh_unique_var`：唯一字段重复时，为变量追加 `${rand:6}`
  - `insert_missing_field`：必填字段缺失时，在保存前插入 `update_fields` 或 `pick_basedata`
- 安全策略：
  - 只有 `safe_to_apply=true` 才能从前端一键应用
  - 基础资料字段没有明确 value_id 时不自动应用
  - 应用前写 `.yaml.bak` 本地备份
- 验证结果：
  - `pytest -q tests/unit tests/test_core.py`：183 passed
  - `python3 -m py_compile lib/repair_planner.py lib/runner.py lib/webui/server.py lib/advisor.py lib/har_extractor.py lib/replay.py lib/config.py lib/webui/log_store.py`：通过
  - `/api/cases/{name}/repairs/plan` 与 `/api/cases/{name}/repairs/apply` 临时用例烟测通过
- 同步补齐历史单测兼容项：
  - 轻量 YAML 解析 `None`
  - `${ vars.name }` 类空白引用解析
  - 旧格式 `showErrMsg.args`
  - `Credentials.username_env/password_env`
  - `LogStore.buffer_size`
  - HAR 字段分类 helper 的测试入口

## 2026-05-18 第五阶段前置增强

- 修复 `基础资料-用人单位zaa` 暴露的文本字段变量遗漏：
  - `description/描述` 可从保存按钮 `post_data` 中抽取为 `${vars.test_description}`
  - 同类 `remark/memo/note/comment/changedesc` 字段纳入文本变量识别
  - 保存、点击、分录新增携带的脏字段统一走变量检测，不再只处理唯一字段
- 打通实时元数据与变量检测：
  - `build_yaml_case()` 与 `preview_har()` 将 `meta_resolver` 传入 `detect_var_placeholders()`
  - `kb_loader.classify_field()` 可在变量分类时读取 `/metadata/getEntityType.do?entityId=` 返回的字段类型
- 增强 `cosmic-hr-expert` 离线知识库使用：
  - `kb_loader` 现在扫描 `_shared/_standard_metadata/entity_metadata/*.md`
  - `hbss_enterprise` 没有独立 scenario 时仍可读取字段标签、类型和数据库字段
  - `main_form_not_in_kb` 对 `hbss_enterprise` 这类已存在共享实体元数据的表单不再误报
- 更新项目技能文档：
  - `cosmic-replay-overview` 补充文本变量、元数据增强链路和定位步骤
  - `cosmic-replay-troubleshooter` 新增“HAR 导入变量遗漏”修复类型
- 本地 ignored 用例 `cases/基础资料-用人单位zaa.yaml` 已定点修补 `test_description`，保留原有 `pick_fields` 环境配置。

## 2026-05-18 第六阶段

- 完成“回归样本库 / 影响报告”第一版：
  - 新增 `lib/har_regression.py`
  - 新增 `scripts/har_regression_report.py`
  - 新增 `tests/fixtures/har_regression/manifest.json`
  - 新增 8 个结构基线 `tests/fixtures/har_regression/baselines/*.json`
- 基线设计原则：
  - HAR 原文继续不入库，避免敏感信息泄露
  - 不保存 YAML 业务实值，只保存变量名、字段 key、value shape、步骤签名、质量评分和组件覆盖率
  - 变更影响分为 `none / info / review / breaking`
  - `main_form_id`、步骤结构、未知组件、阻塞质量项按高风险处理
- 新增测试覆盖：
  - 摘要不会泄露变量值、基础资料 value_id/value_name、字段填写值
  - 主表单变化识别为 breaking
  - 新增变量识别为 review
  - 本地存在 8 个 ignored HAR 时，当前解析结果必须与基线一致
  - 企业样本必须持续保留 `test_description`
- 验证：
  - `./venv/bin/python scripts/har_regression_report.py snapshot --update-baseline`
  - `./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff`：8/8 OK，changed=0
  - `pytest -q tests/unit/test_har_regression.py`：5 passed
  - `pytest -q tests/unit tests/test_core.py`：191 passed
  - `python -m py_compile lib/har_regression.py scripts/har_regression_report.py ...`：通过

## 2026-05-18 第七阶段

- 按用户视角完成 UI 产品化收口：
  - 导入 HAR 后先显示“导入验收结论”，回答“是否可生成 YAML / 是否需确认环境字段 / 是否有未知组件”
  - 预览页支持直接编辑用例名并生成 YAML，不再必须进入第三步
  - 质量评分、组件雷达、主表单/step 统计和原始步骤列表改为默认折叠的“高级诊断”
  - 变量配置和环境字段配置保留在主路径，避免隐藏用户真正需要确认的内容
- 优化执行结果页：
  - 执行成功时展示“已通过执行验证”和 run_id
  - 执行失败时前置展示失败位置、自动归因根因和下一步动作
  - 自动修复计划新增“应用并重跑”
  - 失败总览新增“应用 N 条安全修复并重跑”
- 顺手修复 UI 初始化噪声：
  - 批量任务详情区从 `x-show` 改为 `x-if`，避免 `taskDetail=null` 时 Alpine 表达式误报
  - 增加 favicon 指向 `/static/logo.svg`，避免浏览器默认请求 `/favicon.ico` 产生无关 404
- 验证：
  - Playwright + 本机 Chrome 导入企业 HAR 前端烟测：验收结论、生成 YAML、高级诊断、变量、环境字段均可见，无相关 console error
  - Playwright 模拟执行失败状态：失败位置、下一步建议、应用安全修复并重跑均可见，无 console error

## 2026-05-18 第八阶段

- 完成整体 UI/UX 行业标准审计：
  - 对照 Ant Design、IBM Carbon、Microsoft Fluent、GitLab Pajamas、NN/g 可用性启发式
  - 新增 `UI_UX_AUDIT_REPORT.md`，沉淀当前 UI 优势、主要差距和后续优化路线
- 完成低风险 UI 基础优化：
  - 修复亮色主题 `body.theme-light` 背景选择器
  - 统一暗/亮主题背景层次，保持现有宇宙品牌风格
  - Header、Dashboard、用例详情、批量运行、日志详情、报告弹窗增加响应式布局
  - 表格改为局部横向滚动容器，移动端不再产生页面级横向滚动
  - 增加键盘 `focus-visible` 和 `prefers-reduced-motion` 支持
- 落实设计系统化与离线资源建议：
  - 新增本地 `chart.umd.min.js`，Web UI 不再依赖 jsDelivr
  - 导出 HTML 报告内嵌本地 Tailwind runtime 与 Chart.js，离线打开不丢图表
  - 新增第一版轻量设计系统 class：按钮、状态卡、状态图标、状态徽标、表格容器
  - Dashboard 高频按钮、主用例表格、执行成功/失败状态卡已迁移到统一 class
- 验证：
  - Desktop dashboard Playwright smoke：通过
  - Mobile dashboard Playwright smoke：页面级横向滚动消除，表格局部滚动
  - HAR preview Playwright smoke：企业 HAR 可预览，`描述` 字段识别为 `test_description`
  - `pytest -q tests/unit tests/test_core.py`：192 passed
  - `scripts/har_regression_report.py compare --fail-on-diff`：8 samples, changed 0
  - `tests/unit/test_report_exporter.py`：覆盖导出报告无 CDN 引用

## 2026-05-19 第九阶段

- 完成批量执行报告验收化：
  - `ExecutionReport` 增加 `acceptance` 和 `action_queues`
  - `CaseResult` 增加断言、失败归因、修复计划、环境字段、入库状态、入库证据、下一步动作和 AI 原因
  - 报告生成时自动识别 `write_status=verified/unverified/not_applicable/failed`
  - 执行 PASS 但保存/提交响应为空或缺少写入信号时，进入 `ai_agent` 队列，不再被误判为完全交付
- 优化批量任务事件采集：
  - `step_start/step_ok/step_fail` 写入阶段明细
  - `assertion_ok/assertion_fail` 写入断言结果
  - `failure_analysis/fixes_ready/env_fields_resolved` 写入报告上下文
- 优化批量报告 UI：
  - 报告弹窗新增“验收结论”
  - 新增自动修复、人工确认、AI Agent 三类行动队列
  - 用例明细新增“入库证据”“下一步”和“AI 证据包”
- 优化导出报告：
  - HTML 报告同步展示验收结论、入库证据和下一步动作
  - 保持离线资源内嵌，不引入 CDN 回归
- 建立 AI Agent 修复证据包：
  - 新增 `lib/agent_evidence.py`
  - 新增 `/api/tasks/{task_id}/agent-evidence/{case_name}`
  - 证据包包含 YAML、最近运行事件、失败事件、报告上下文、推荐技能、修复护栏和期望输出格式
- 优化 `cosmic-replay-troubleshooter`：
  - 新增“执行 PASS 但入库未验证（假成功）”排障类型
  - 新增“AI Agent 修复升级协议”
  - 明确 AI 修复不得删除 pageId 链路、不得放宽断言、不得更新回归基线掩盖问题
- 验证：
  - `./venv/bin/python -m pytest -q tests/unit tests/test_core.py`：196 passed
  - `./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff`：8 samples, changed 0
