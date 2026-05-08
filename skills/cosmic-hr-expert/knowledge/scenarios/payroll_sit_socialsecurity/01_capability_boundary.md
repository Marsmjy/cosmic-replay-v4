# payroll_sit_socialsecurity · 能力边界

> **资产名**：社保管理
> **数据源**：deep_scan_inventory.md / deep_scan_audit.md A/G 段

## 场景定位

| 项 | 值 |
|---|---|
| 所属云 / 应用 | payroll / sit |
| 主 form | `sit_socialsecurity` |
| ISV 包前缀 | `tdkw.swc.sit.other` |
| Java 业务文件数 | 16 |

## 类清单（继承自苍穹白名单基类）

- `SinsurfileHelper` (SinsurfileHelper.java) extends `(独立)`
- `SITEntityConstants` (SITEntityConstants.java) extends `(独立)`
- `SocialSecurityFundCommon` (SocialSecurityFundCommon.java) extends `(独立)`
- `SocialSecurityFundFormPlugin` (SocialSecurityFundFormPlugin.java) extends `AbstractBillPlugIn`
- `SocialSecurityFundListPlugin` (SocialSecurityFundListPlugin.java) extends `AbstractListPlugin`
- `SocialSecurityPaybillListPlugin` (SocialSecurityPaybillListPlugin.java) extends `AbstractListPlugin`
- `SocialSecurityPayClickNumberPlugin` (SocialSecurityPayClickNumberPlugin.java) extends `AbstractFormPlugin`
- `SocialSecurityPayFormPlugin` (SocialSecurityPayFormPlugin.java) extends `AbstractBillPlugIn`
- `SocialSecurityPayOperationPlugin` (SocialSecurityPayOperationPlugin.java) extends `AbstractOperationServicePlugIn`
- `SocialSecurityPayValidator` (SocialSecurityPayValidator.java) extends `AbstractValidator`
- `SocialSecurityPersonFilterFormPlugin` (SocialSecurityPersonFilterFormPlugin.java) extends `AbstractBillPlugIn`
- `OperateResultCheck` (OperateResultCheck.java) extends `(独立)`
- `PersonOrgUtil` (PersonOrgUtil.java) extends `(独立)`
- `StringHelper` (StringHelper.java) extends `(独立)`
- `TypeConvertUtil` (TypeConvertUtil.java) extends `(独立)`

## 涉及 form 清单（来自真扫）

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `bos_org` | SocialSecurityPersonFilterFormPlugin.java:56, PersonOrgUtil.java:18, PersonOrgUtil.java:33 | — |
| `haos_adminorghr` | PersonOrgUtil.java:22, PersonOrgUtil.java:28 | — |
| `hcsi_sibase` | SinsurfileHelper.java:187, SinsurfileHelper.java:254, SinsurfileHelper.java:310 等 | — |
| `hcsi_sinsurfile` | SocialSecurityPaybillListPlugin.java:58, SocialSecurityPayClickNumberPlugin.java:44, SocialSecurityPayFormPlugin.java:475 等 | — |
| `hrpi_empjobrel` | SocialSecurityPayFormPlugin.java:700, SocialSecurityPersonFilterFormPlugin.java:170 | — |
| `hrpi_employee` | SocialSecurityPayFormPlugin.java:715 | — |
| `hrpi_empposorgrel` | SocialSecurityPayFormPlugin.java:78, SocialSecurityPayFormPlugin.java:644 | — |
| `hrpi_percre` | SocialSecurityPayFormPlugin.java:659 | — |
| `hsbs_empentrel` | SocialSecurityPayFormPlugin.java:737 | — |
| `sitbs_insuranceprop` | SinsurfileHelper.java:548 | — |
| `sitbs_sinsurstd` | SocialSecurityPayValidator.java:63 | — |
| `sitbs_welfaretype` | SinsurfileHelper.java:533 | — |
| `tdkw_payment_service_type` | SocialSecurityPaybillListPlugin.java:76 | — |
| `tdkw_social_security` | SocialSecurityPaybillListPlugin.java:67, SocialSecurityPaybillListPlugin.java:149, SocialSecurityPayFormPlugin.java:535 | — |
| `tdkw_social_security_fund` | SocialSecurityPersonFilterFormPlugin.java:178 | — |

## 边界（脑补防火墙）

## G · 关键否定型事实（脑补防火墙）

⚡ **待精修**：人工补"代码里没看到的功能"·防 LLM 脑补：

- ❌ "<待人工补>·如：'本资产没用调度任务'"
- ❌ "<待人工补>·如：'本资产没发 BEC 跨云事件'"

## SDK 白名单合规

详见 [`deep_scan_forbidden_check.md`](../../../<skills-dir>/cosmic-hr-expert/dcs_regression/passed/case_012_sit_socialsecurity/deep_scan_forbidden_check.md)·走苍穹白名单基类·无内部 API 调用。

## 关联资产 / 模板

- 资产复刻：[`_assets/`](../../_assets/)
- Phase D 真扫：[`dcs_regression/`](../../../dcs_regression/)
