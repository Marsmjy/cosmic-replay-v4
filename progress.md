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
