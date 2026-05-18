# cosmic-replay UI/UX Audit Report

Date: 2026-05-18

## Scope

本次审计聚焦 Web UI 的主题美观性、易用性和企业级自动化工具的可扩展体验，不改变 HAR 解析、YAML 生成和用例执行主链路。

## Industry Benchmarks

参考方向：

- Ant Design：企业级系统应强调清晰的数据录入、及时反馈和可恢复错误。
  - https://ant.design/docs/spec/data-entry/
  - https://3x.ant.design/docs/spec/feedback
- IBM Carbon：企业工具需要把可访问性作为基础能力，确保感知、操作和理解一致。
  - https://carbondesignsystem.com/guidelines/accessibility/overview/
- Microsoft Fluent 2：清晰的信息结构、层级和响应式布局能帮助用户在不同屏幕上理解关系并完成决策。
  - https://fluent2.microsoft.design/accessibility
  - https://fluent2.microsoft.design/layout
- GitLab Pajamas：空状态、引导和告警应提供明确下一步动作，而不是只展示状态。
  - https://design.gitlab.com/patterns/empty-states
  - https://design.gitlab.com/components/alert/
- NN/g 可用性启发式：系统状态可见、错误预防、识别优于记忆、帮助用户诊断并恢复错误，是复杂工作流产品的核心要求。
  - https://www.nngroup.com/articles/ten-usability-heuristics/

## Findings

### What Already Works

- 品牌方向明确：暗色宇宙主题与“replay / automation”工具定位匹配。
- 主路径已清晰：导入 HAR、确认变量/环境字段、生成 YAML、执行验证已被压缩成直接流程。
- 错误恢复能力已具备：执行失败后已有失败位置、根因和安全修复重跑入口。
- 数据密度适合企业内部工具：用例列表、批量运行、日志和执行报告都偏高密度，符合测试/自动化场景。

### Main Gaps

- 响应式基础不足：窄屏下导航、统计卡片和表格会挤压或产生页面级横向溢出。
- 主题实现分散：`index.html` 内联样式和 `app.css` token 并存，部分亮色主题选择器没有命中 `body.theme-light`。
- 交互可访问性可增强：键盘焦点缺少统一可见样式，动效没有遵循 `prefers-reduced-motion`。
- 视觉系统还未组件化：按钮、状态卡、表格、告警、空状态仍大量依赖局部 Tailwind class，后续维护成本会升高。
- 外部资源依赖存在风险：Chart.js 使用外部 CDN，本地/内网环境可能影响报告图表加载。

## Implemented In This Pass

- 修正亮色主题背景选择器，确保 `body.theme-light` 生效。
- 统一 body 背景层次，保留暗色品牌感，同时让亮色主题有对应背景。
- 优化顶部导航在窄屏下的换行与间距。
- Dashboard 统计卡片在移动端从 4 列变为 2 列。
- 搜索/排序栏在移动端改为纵向堆叠，避免挤压。
- 用例列表、批量运行表格和报告详情表格使用局部横向滚动容器，避免页面整体溢出。
- 用例详情、日志详情、报告图表区域增加响应式单列/双列布局。
- 增加 `focus-visible` 样式，改善键盘操作可见性。
- 增加 `prefers-reduced-motion` 支持，减少动效对敏感用户的影响。
- 建立第一版轻量设计系统 class：`btn`、`status-card`、`status-icon`、`status-badge`、`table-shell`。
- 将 Dashboard 高频按钮、执行成功/失败状态卡、主用例表格迁移到轻量设计系统 class。
- 将 Web UI 的 Chart.js 从外部 CDN 改为本地 `/static/chart.umd.min.js`。
- 将导出 HTML 报告所需的 Tailwind runtime 与 Chart.js 内嵌，保证报告离线可打开。

## Recommended Next Steps

1. 继续扩大轻量设计系统覆盖面：把设置页、日志页、报告弹窗中的按钮和状态徽标逐步迁移。
2. 抽象状态组件：成功、失败、需确认、可优化、未知风险统一为一套 `status-card` 语义。
3. 重构表格体验：桌面端增加 sticky header、行状态徽标和批量操作浮层。
4. 增加 UI smoke tests：覆盖 dashboard、HAR preview、case detail failure、batch report、mobile viewport。
5. 继续减少 emoji 依赖：关键动作和状态逐步改为一致的 SVG/icon class，保留少量品牌化点缀。

## Verification

- Desktop dashboard smoke：通过。
- Mobile dashboard smoke：页面级横向滚动已消除，表格在局部容器内滚动。
- HAR preview smoke：企业 HAR 可进入预览，`描述` 字段识别为 `test_description`。
- Report exporter unit smoke：导出 HTML 不再引用 CDN，Chart.js 内嵌可用。
- Unit/core tests：192 passed。
- HAR regression impact：8 samples, changed 0, overall impact none。
