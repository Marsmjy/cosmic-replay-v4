# HAR 导入到执行闭环验证报告（2026-05-18）

## 结论

本轮按真实 Web API 完成 8 类 HAR 的端到端验证：`HAR 上传预览 -> 变量解析 -> YAML 生成 -> 用例执行 -> 保存/写库断言`。

最终结果：8/8 通过。首轮有 1 类遇到一次 `HTTP 502` 瞬态协议错误，重试后通过；执行层已补充 `HTTP 502/503/504` 协议级重试保护，降低后续偶发网关错误对全流程的影响。

## 8 类执行结果

| # | HAR 类型 | 成功 run_id | 步骤 | 断言 | 结果 | 备注 |
|---|---|---:|---:|---:|---|---|
| 1 | HR基础服务云新增一条用工关系基础资料 | `033547223a97` | 19/19 | 2/2 | 通过 | 保存响应无错误 |
| 2 | 业务模型添加一个基础资料附表 | `bf229fc16ef2` | 209/209 | 2/2 | 通过 | 首轮 `fill_value` 遇到 HTTP 502，重试通过 |
| 3 | 新增一条行政组织 | `423cf8556130` | 37/37 | 2/2 | 通过 | 已解决 `homs_apphome` 导航阻断 |
| 4 | 新增入职0512测试 | `fca9221c4a13` | 107/107 | 2/2 | 通过 | 流程启动/确认无错误 |
| 5 | 入职申请到确认入职 | `c3e18c0a3354` | 44/44 | 2/2 | 通过 | 流程启动/确认无错误 |
| 6 | 基础资料-用人单位 | `b8a2abfde26d` | 12/12 | 2/2 | 通过 | 已解决 `createorg` 上下文字段 |
| 7 | 基础资料-新增国籍 | `73c63c8e3194` | 15/15 | 2/2 | 通过 | 保存响应无错误 |
| 8 | 岗位信息维护-新增一个岗位 | `15dfb7f8e3e2` | 26/26 | 2/2 | 通过 | 已解决 `adminorg` 上下文字段和 `homs_apphome` 导航阻断 |

说明：每个通过用例均包含 `no_save_failure` 与 `no_error_actions` 两类断言通过，表示保存/流程动作未返回字段级错误、操作失败 action 或苍穹错误 action。

## 已落地优化

1. 保留列表/树到主表单的上下文桥接链路，避免导入时裁剪掉 `refresh -> entryRowClick -> hyperLinkClick/loadData` 这类隐式上下文。
2. 自动补偿上下文字段：`hbpm_positionhr.adminorg` 使用 `pick_basedata`，`hbss_enterprise.createorg` 使用 `update_fields`，并进入 `pick_fields` 配置面板。
3. 变量识别增强：邮箱字段（如 `peremail`）自动抽成 `${vars.test_email}`；多语言字段中的纯数字字符串保持 YAML 字符串，避免 `11111` 被解析成整数。
4. 非主业务导航表单降级：`*_apphome`、快捷卡片、后台任务侧栏等导航/装饰步骤标记为 `optional`，不再阻断可独立打开和保存的主业务表单。
5. `pick_fields` 覆盖增强：环境字段配置不仅能覆盖 `pick_basedata`，也能覆盖上下文补偿产生的 `update_fields`。
6. 执行安全网增强：对 `HTTP 502/503/504`、网关超时、连接中断等协议级瞬态错误执行重试、重开表单、预热 `loadData`。

## 标准诊断流程

1. 先看导入预览：确认 `main_form_id`、`vars`、`pick_fields`、`target_form/target_forms` 是否完整。
2. 再看 YAML 主链路：业务主表单、保存/提交/启动流程步骤必须非 optional；导航卡片、apphome、侧栏可 optional。
3. 运行失败先按错误分类：
   - `AppIdName(...)服务未发现`：判断该表单是主业务表单还是导航表单。导航表单应 optional；主业务表单需要保留菜单 target_form 链路或检查环境服务。
   - `字段必填/数据已存在`：检查变量抽取和 `pick_fields` 环境值。
   - `页面未初始化/过期`：检查 pageId 链路、`target_forms`、runner 三层防护。
   - `HTTP 502/503/504`：按瞬态协议错误重试。
4. 最后确认写库：以 `no_save_failure` 和 `no_error_actions` 断言为准；必要时再结合业务库或页面查询做二次验证。

## 预防措施

1. 新增 HAR 类型时，优先补回归测试到 `tests/unit/test_har_extractor_regressions.py`，覆盖变量、上下文字段、optional 边界。
2. 不静态插入额外 `loadData` 作为修复手段，优先依赖 runner 的 pageId 三层防护。
3. 主业务表单失败不能简单标 optional；只有非主表单导航/装饰动作可以降级。
4. 环境相关字段统一进入 `pick_fields`，避免把组织、职位、法人、国家地区等 ID 写死在业务步骤里。
5. 每次优化后至少跑两层验证：单元回归测试 + API 级导入/执行闭环。

## 本轮验证命令

```bash
pytest -q tests/unit/test_har_extractor_regressions.py
python3 -m py_compile lib/runner.py lib/har_extractor.py
```

API 级验证原始明细属于本地临时产物，不再提交到版本库；仓库只保留本报告中的摘要结论。
