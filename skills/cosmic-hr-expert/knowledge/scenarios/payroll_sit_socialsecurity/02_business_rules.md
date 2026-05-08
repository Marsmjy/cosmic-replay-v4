# payroll_sit_socialsecurity · 业务规则

> **数据源**：deep_scan_audit.md B/C/D/E/F 段
> **日期**：2026-05-06

## R-01 · 字段写入规则（核心业务行为）

## B · 字段写入操作（set 模式）

⚡ **待精修**：补每个字段的业务含义 / 状态机说明。

| 字段 | 写入位置 | 写值类型 | 业务含义 |
|---|---|---|---|
| `${ISV_FLAG}_base_endowment_e1` | SocialSecurityPersonFilterFormPlugin.java:251 | `socialMedical` | — |
| `${ISV_FLAG}_base_housing_e1` | SocialSecurityPersonFilterFormPlugin.java:266 | `reserveFund` | — |
| `${ISV_FLAG}_base_injury_e1` | SocialSecurityPersonFilterFormPlugin.java:254 | `socialMedical` | — |
| `${ISV_FLAG}_base_maternity_e1` | SocialSecurityPersonFilterFormPlugin.java:255 | `socialMedical` | — |
| `${ISV_FLAG}_base_medica_e1` | SocialSecurityPersonFilterFormPlugin.java:256 | `socialMedical` | — |
| `${ISV_FLAG}_base_medical_e1` | SocialSecurityPersonFilterFormPlugin.java:252 | `socialMedical` | — |
| `${ISV_FLAG}_endowment_e` | SocialSecurityPersonFilterFormPlugin.java:219 | `socialMedical` | — |
| `${ISV_FLAG}_housing_e` | SocialSecurityPersonFilterFormPlugin.java:235 | `reserveFund` | — |
| `${ISV_FLAG}_industrial_injury_e` | SocialSecurityPersonFilterFormPlugin.java:222 | `socialMedical` | — |
| `${ISV_FLAG}_maternity_e` | SocialSecurityPersonFilterFormPlugin.java:223 | `socialMedical` | — |
| `${ISV_FLAG}_medical_e` | SocialSecurityPersonFilterFormPlugin.java:220 | `socialMedical` | — |
| `${ISV_FLAG}_pay_file` | SocialSecurityPayOperationPlugin.java:146 等 2 处 | `ids.get(companyId` | — |
| `${ISV_FLAG}_radix` | SocialSecurityPersonFilterFormPlugin.java:226 等 2 处 | `socialMedical` | — |
| `${ISV_FLAG}_radix_base` | SocialSecurityPersonFilterFormPlugin.java:257 等 2 处 | `socialMedical` | — |
| `${ISV_FLAG}_radix_table` | SinsurfileHelper.java:339 等 3 处 | `bill.getLong("tdkw_radix_table.id"` | — |
| `${ISV_FLAG}_supply_medica_e` | SocialSecurityPersonFilterFormPlugin.java:225 | `socialMedical` | — |
| `${ISV_FLAG}_unemployment_e` | SocialSecurityPersonFilterFormPlugin.java:221 | `socialMedical` | — |
| `${ISV_FLAG}_unemployment_e1` | SocialSecurityPersonFilterFormPlugin.java:253 | `socialMedical` | — |

## R-02 · 字段读取规则（核心业务读）

## C · 字段读取操作（get 模式）

| 字段 | 读取位置（前 3 处） | 业务含义|
|---|---|---|
| `assignment` | SinsurfileHelper.java:371 | — |
| `billno` | SocialSecurityPayFormPlugin.java:53 | — |
| `billstatus` | SocialSecurityPaybillListPlugin.java:96 | — |
| `boid` | SocialSecurityPayClickNumberPlugin.java:65 | — |
| `createorg` | SocialSecurityFundFormPlugin.java:224 | — |
| `createtime` | SocialSecurityPayFormPlugin.java:64 | — |
| `creator` | SocialSecurityPayFormPlugin.java:63 | — |
| `employee` | SocialSecurityPayFormPlugin.java:691, SocialSecurityPayFormPlugin.java:733 | — |
| `empposorgrel` | SinsurfileHelper.java:369, SocialSecurityPayFormPlugin.java:730 | — |
| `empstage` | SocialSecurityPayFormPlugin.java:732 | — |
| `entryentity` | SinsurfileHelper.java:101, SinsurfileHelper.java:414, SinsurfileHelper.java:572 等 4 处 | — |
| `errorCode` | SinsurfileHelper.java:562 | — |
| `headsculpture` | SocialSecurityPayFormPlugin.java:721, SocialSecurityPayFormPlugin.java:725 | — |
| `id` | SinsurfileHelper.java:148, SinsurfileHelper.java:436, SinsurfileHelper.java:536 等 37 处 | — |
| `insured` | SinsurfileHelper.java:208, SinsurfileHelper.java:271 | — |
| `insurtype` | SocialSecurityPayValidator.java:71 | — |
| `joblevel` | SocialSecurityPersonFilterFormPlugin.java:197 | — |
| `message` | SinsurfileHelper.java:562 | — |
| `mulsinsurstd` | SinsurfileHelper.java:465, SocialSecurityPayFormPlugin.java:843, SocialSecurityPayValidator.java:55 等 4 处 | — |
| `name` | SinsurfileHelper.java:300, SinsurfileHelper.java:407, SinsurfileHelper.java:610 等 5 处 | — |

## R-03 · 平台 op 调用（标品 op 复用）

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## R-04 · 查询过滤规则（QFilter 复合条件）

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `billstatus` | `QCP.in` | SocialSecurityPayFormPlugin.java:529 |
| `createorg.number` | `QCP.equals` | SocialSecurityFundCommon.java:49 |
| `datastatus` | `QCP.equals` | SocialSecurityFundCommon.java:47, SocialSecurityPayFormPlugin.java:549, SocialSecurityPayFormPlugin.java:668 |
| `employee.empnumber` | `QCP.equals` | SocialSecurityPayFormPlugin.java:546 |
| `employee.number` | `QCP.in` | SocialSecurityPayOperationPlugin.java:110 |
| `employee` | `QCP.equals` | SocialSecurityPayFormPlugin.java:74, SocialSecurityPayFormPlugin.java:641, SocialSecurityPayFormPlugin.java:658 |
| `empstagelatestrecord` | `QCP.equals` | SocialSecurityPayFormPlugin.java:735 |
| `empstage` | `QCP.equals` | SocialSecurityPayFormPlugin.java:736 |
| `enable` | `QCP.equals` | SocialSecurityFundCommon.java:46, SocialSecurityPayOperationPlugin.java:112, PersonOrgUtil.java:32 |
| `enddate` | `QCP.large_equals` | SocialSecurityPayFormPlugin.java:75, SocialSecurityPayFormPlugin.java:642, SocialSecurityPersonFilterFormPlugin.java:168 |
| `id` | `QCP.equals` | SocialSecurityPayFormPlugin.java:473, SocialSecurityPayFormPlugin.java:712, SocialSecurityPersonFilterFormPlugin.java:177 |
| `id` | `QCP.in` | SocialSecurityPaybillListPlugin.java:66, SocialSecurityPayValidator.java:65 |
| `id` | `QCP.not_equals` | SocialSecurityPayFormPlugin.java:531 |
| `id` | `QCP.not_in` | SocialSecurityFundFormPlugin.java:113 |
| `insured` | `QCP.equals` | SinsurfileHelper.java:186, SinsurfileHelper.java:253, SinsurfileHelper.java:309 |
| `iscurrentdata` | `QCP.equals` | SocialSecurityPayFormPlugin.java:76, SocialSecurityPayFormPlugin.java:699, SocialSecurityPersonFilterFormPlugin.java:169 |
| `iscurrentversion` | `QCP.equals` | SocialSecurityFundCommon.java:48, SocialSecurityPayFormPlugin.java:548, SocialSecurityPayFormPlugin.java:667 |
| `isescrowstaff` | `QCP.equals` | SocialSecurityPaybillListPlugin.java:56, SocialSecurityPayFormPlugin.java:547, SocialSecurityPayOperationPlugin.java:113 |
| `isprimary` | `QCP.equals` | SocialSecurityPayFormPlugin.java:77, SocialSecurityPayFormPlugin.java:643 |
| `number` | `QCP.equals` | SocialSecurityPersonFilterFormPlugin.java:56, PersonOrgUtil.java:20, PersonOrgUtil.java:30 |
| `number` | `QCP.in` | SocialSecurityPaybillListPlugin.java:75 |
| `org` | `QCP.equals` | SocialSecurityPersonFilterFormPlugin.java:74 |
| `sinsurfile` | `QCP.equals` | SinsurfileHelper.java:185, SinsurfileHelper.java:252, SinsurfileHelper.java:308 |
| `sinsurfile` | `QCP.in` | SocialSecurityPayFormPlugin.java:560 |
| `sinsurstatus` | `QCP.equals` | SocialSecurityPayFormPlugin.java:474, SocialSecurityPayFormPlugin.java:545 |

## R-05 · 业务规则推断（待业务释义确认）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：
- `insured` 可能取值: 0, 1
- `status` 可能取值: E

## 关联

- 真扫源：deep_scan_audit.md（与本场景同根的 case 目录下）
