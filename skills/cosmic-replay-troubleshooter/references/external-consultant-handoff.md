# 外部顾问 / AI Agent 交接指南

## 使用场景

当 Cosmic Replay 项目、skill 包或 AI 修复证据包交给外部顾问、Qoder Work、Codex、Kiro、WorkBuddy 等 AI IDE 使用时，先阅读本文件。目标是让新 Agent 在没有历史对话记忆的情况下，也能正确理解项目能力、边界和排障顺序。

## 已固化的关键经验

1. Cosmic Replay 的核心价值是：HAR 导入 → 变量识别 → YAML 生成 → 用例执行 → 入库验收 → AI 修复证据包。
2. 金蝶苍穹 pageId 是服务端模型上下文，不只是请求参数。很多默认值、联动值、树节点状态和锁定字段状态保存在 pageId 对应的服务端模型里。
3. 新 HAR 执行失败时，优先对比 HAR 原始 pageId 链路与回放 pageId 链路，再判断变量遗漏、环境字段覆盖、异步等待、断言盲区或业务校验。
4. 不要一开始硬补 `save.post_data`。只有确认 pageId 链路正确后，才进入字段解析和业务补偿。
5. 环境相关字段必须允许用户维护。即使是录制时默认带出的字段，例如上级行政组织、组织、行政组织类型，也可能跨环境需要修改。
6. `pick_` 类型字段优先展示业务编码或名称，内部保留 `value_id` 给执行器使用；用户不应被迫维护长整数内码。
7. PASS 不是交付标准。若保存响应为空或缺少写库 token，应标记为入库未验证，需要补断言、人工确认或 AI 诊断。
8. 手工确认已入库是用户事实确认，应写入 YAML，后续同用例 PASS 时不再提示 AI 诊断；但它不等同于系统自动回查。
9. AI 修复必须保持已通过样本兼容，不得更新 HAR baseline 掩盖规则回归。

## 已团队验证的场景

当前回归基线包含 10 类 HAR 样本：

| 编号 | 场景 | 环境 | 验证状态 |
| --- | --- | --- | --- |
| 01 | HR 基础服务云新增一条用工关系基础资料 | SIT | 已验证 |
| 02 | 业务模型添加一个基础资料附表 | SIT | 已验证 |
| 03 | 新增一条行政组织 | SIT | 已验证 |
| 04 | 新增入职 | SIT | 已验证 |
| 05 | 入职申请到确认入职 | SIT | 已验证 |
| 06 | 基础资料-用人单位 | SIT | 已验证 |
| 07 | 基础资料-新增国籍 | SIT | 已验证 |
| 08 | 岗位信息维护-新增一个岗位 | SIT | 已验证 |
| 09 | 金蝶 HR-行政组织新增 | UAT 真实环境 | 已验证 |
| 10 | UA 提报保存（含子弹窗明细补录） | UAT 真实环境 | 已验证 |

回归命令：

```bash
./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff
```

## 当前支持

- 支持 HAR 导入并生成 YAML 用例。
- 支持智能用例变量识别，例如编码、名称、描述、手机号、邮箱、日期等。
- 支持环境相关字段识别与可编辑维护，包括基础资料、枚举、组织、行政组织、岗位等。
- 支持在线元数据增强：`/metadata/getEntityType.do?entityId=...` 用于增强字段标签与字段类型判断。
- 支持 `cosmic-hr-expert` 知识库辅助 HR 场景字段识别。
- 支持 pageId L0/L1/L2/L3 链路诊断和 `preserve_l2_page` 保留策略。
- 支持批量执行报告、入库状态判断、人工确认已入库。
- 支持 AI 修复证据包和“复制 AI 修复指令”。

## 当前不支持或需要谨慎

- 不支持直接从 OpenAPI/Swagger 文档生成苍穹回放用例；当前主入口是 HAR。
- 不保证未覆盖的新苍穹组件一次通过；需要通过组件注册表和 HAR 回归补规则。
- 不保证所有 PASS 都自动有入库证据；保存响应为空时仍需回查断言或人工确认。
- 不应把业务校验错误一概当成失败。有些 HAR 录制中出现的校验弹窗本身就是验证点，应生成预期通知断言。
- 不应把环境字段全部自动解析为长整数内码展示给用户；界面应展示可读编码/名称。
- 不应在没有证据的情况下修改已通过 YAML 或更新 baseline。

## 新 Agent 排障顺序

1. 阅读 `skills/cosmic-replay-overview/SKILL.md` 理解项目架构。
2. 阅读 `skills/cosmic-replay-troubleshooter/SKILL.md` 和本文件。
3. 若问题涉及 pageId，阅读 `references/pageid-chain-debugging.md`。
4. 若问题涉及断言或 PASS 但未入库，阅读 `references/assertion-blindspots.md`。
5. 若问题涉及 HR 字段、实体、表单标签，阅读 `skills/cosmic-hr-expert/SKILL.md` 和相关 knowledge。
6. 打开 AI 证据包，先查看 YAML、run events、failed events、pageid_trace、write_status。
7. 只做最小补丁，优先修 YAML；确认是通用规则缺陷时再改代码。
8. 必跑：

```bash
./venv/bin/python -m pytest -q tests/unit/test_env_field_resolution.py tests/unit/test_quality_and_failure_analysis.py tests/unit/test_runner.py tests/unit/test_har_extractor_regressions.py tests/unit/test_agent_evidence.py
./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff
```

## 禁止动作

- 禁止删除 `menuItemClick`、`target_forms`、`pick_fields`、`no_save_failure` 来让用例变绿。
- 禁止把主保存/提交步骤标为 optional 来绕过失败。
- 禁止硬补 `save.post_data` 替代 pageId 链路修复。
- 禁止在无入库证据时声称用例已交付。
- 禁止把 `manual_verified` 当成系统自动验证。
- 禁止泄露或提交真实环境账号、密码、HAR 原文、cookie、token。

## 顾问交付建议

交给顾问时只外发：

- 项目仓库或脱敏 zip。
- `skills/` 下三个 skill：`cosmic-replay-overview`、`cosmic-replay-troubleshooter`、`cosmic-hr-expert`。
- 外发 HTML 使用手册。
- demo 初始提示词和 AI 修复提示词模板。

不要外发：

- `config/envs/*.yaml` 中的真实账号信息。
- `.env`、`data/cosmic_replay.db`、真实 HAR、cookie、token。
