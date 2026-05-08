# payroll_sit_socialsecurity · 数据流

> **数据源**：deep_scan_audit.md B/C/H 段

## 数据写入流向

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

## 数据读取流向

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

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
