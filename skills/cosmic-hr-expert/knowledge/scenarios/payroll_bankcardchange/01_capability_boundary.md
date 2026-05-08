# payroll_bankcardchange · 能力边界

> **资产名**：银行卡变更
> **数据源**：deep_scan_inventory.md / deep_scan_audit.md A/G 段

## 场景定位

| 项 | 值 |
|---|---|
| 所属云 / 应用 | payroll / hsas |
| 主 form | `swc_changebankcardbill` |
| ISV 包前缀 | `tdkw.z8njs.swc.hsasext` |
| Java 业务文件数 | 4 |

## 类清单（继承自苍穹白名单基类）

- `AppflgConstant` (AppflgConstant.java) extends `(独立)`
- `ChangeBankCardBillPlugin` (ChangeBankCardBillPlugin.java) extends `AbstractMobBillPlugIn`
- `ChangeBankCardOperationPlugin` (ChangeBankCardOperationPlugin.java) extends `AbstractOperationServicePlugIn`
- `AcctModifyBankByPayDetailOperatePlugin` (AcctModifyBankByPayDetailOperatePlugin.java) extends `AbstractOperationServicePlugIn`

## 涉及 form 清单（来自真扫）

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `hbss_bankpurpose` | ChangeBankCardBillPlugin.java:95 | — |
| `hbss_payrollacrelation` | ChangeBankCardBillPlugin.java:90 | — |
| `hrpi_employee` | ChangeBankCardOperationPlugin.java:76 | — |
| `hrpi_perbankcard` | ChangeBankCardOperationPlugin.java:110, ChangeBankCardOperationPlugin.java:154, ChangeBankCardOperationPlugin.java:176 等 | — |
| `hrpi_person` | ChangeBankCardOperationPlugin.java:70 | — |
| `hsas_acctmodifybill` | AcctModifyBankByPayDetailOperatePlugin.java:36 | — |
| `hsas_paydetail` | AcctModifyBankByPayDetailOperatePlugin.java:50 | — |

## 边界（脑补防火墙）

## G · 关键否定型事实（脑补防火墙）

⚡ **待精修**：人工补"代码里没看到的功能"·防 LLM 脑补：

- ❌ "<待人工补>·如：'本资产没用调度任务'"
- ❌ "<待人工补>·如：'本资产没发 BEC 跨云事件'"

## SDK 白名单合规

详见 [`deep_scan_forbidden_check.md`](../../../<skills-dir>/cosmic-hr-expert/dcs_regression/passed/case_005_bankcardchange/deep_scan_forbidden_check.md)·走苍穹白名单基类·无内部 API 调用。

## 关联资产 / 模板

- 资产复刻：[`_assets/`](../../_assets/)
- Phase D 真扫：[`dcs_regression/`](../../../dcs_regression/)
