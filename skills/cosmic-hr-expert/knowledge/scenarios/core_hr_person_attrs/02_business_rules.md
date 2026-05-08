# core_hr_person_attrs · 业务规则

> **聚合场景**：人员属性聚合（个人/家庭/联系/证件/履历记录 11 实体）（11 个子实体）
> **生成时间**：2026-04-30
> **方法**：从 `_shared/_standard_metadata/entity_metadata/<entity>.md` 提取每子实体真字段元数据 → 标品 HRBaseDataTplEdit 共性规则 + 子实体特有约束

## 一、共性规则（HRBaseDataTplEdit 标品模板 · 12 实体共用）

本聚合场景所有子实体均继承 `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` 标品模板，自带规则：

| 规则 ID | 触发点 | 行为 | ISV 是否可改 |
|---|---|---|:---:|
| BR_TPL_1 | 表单加载 (afterBindData) | 自动加载基础资料元数据 + 渲染字段 | ❌ 标品 |
| BR_TPL_2 | save 操作 | 触发 CodeRuleOp 自动生成 number 字段（按编码规则） | ❌ 标品 · 规则在元数据里配置 |
| BR_TPL_3 | save 操作 | HRBaseDataStatusOp · 设置 status 字段（A/B/C 状态机） | ❌ 标品 |
| BR_TPL_4 | save 操作 | HRBaseOriginalOp · 维护 orinumber/oriname/oristatus 出厂数据 | ❌ 标品 |
| BR_TPL_5 | enable / disable | HRBaseDataEnableOp · 维护 enable 字段 + disabledate/disabler | ❌ 标品 |
| BR_TPL_6 | save 操作 | HRBaseDataLogOp · 写变更日志（按 HRBaseDataConfigUtil 配置启用） | ❌ 标品 |

> ⚠️ ISV 不应继承 HRBaseDataTplEdit · 应**并列挂** `HRDataBaseEdit` 实现自定义逻辑（PR-001）

## 二、子实体特有规则（按字段提取）

### `hrpi_partymember` · 党员信息

**字段总数**：24

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_fertilityinfo` · 生育信息

**字段总数**：14

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（4）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `procreatmode` (生育方式) → `hbss_procreatmode` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_perhobby` · 特长及爱好

**字段总数**：13

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_peraddress` · 人员地址

**字段总数**：14

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（5）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `addresstype` (地址类型) → `hbss_addresstype` · `BasedataField`
- `countrycode` (国家/地区) → `bd_country` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_percontact` · 联系方式

**字段总数**：28

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_perregion` · 区域信息

**字段总数**：16

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（7）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `politicalstatus` (政治面貌) → `hbss_politicalstatus` · `BasedataField`
- `party` (所属党派) → `hbss_party` · `BasedataField`
- `religion` (宗教) → `hbss_religion` · `BasedataField`
- `regresidencenature` (户口性质) → `hbss_category` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_percre` · 证件信息

**字段总数**：33

**必填字段（3）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query
- `credentialstype` (证件类型) · `BasedataField` → ref hbss_credentialstype
- `number` (证件号码) · `TextField`

**引用基础资料（8）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `credentialstype` (证件类型) → `hbss_credentialstype` · `BasedataField`
- `countrycode` (国家码) → `bd_country` · `BasedataField`
- `nationality` (国籍) → `hbss_nationality` · `BasedataField`
- `gender` (性别) → `hbss_sex` · `BasedataField`
- `folk` (民族) → `hbss_flok` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `number`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_familymemb` · 家庭成员

**字段总数**：21

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（6）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `familymembship` (家庭成员关系) → `hbss_familymemberrel` · `BasedataField`
- `country` (国家/地区) → `bd_country` · `BasedataField`
- `politicalstatus` (政治面貌) → `hbss_politicalstatus` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `name`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_emrgcontact` · 紧急联系人

**字段总数**：15

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（5）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `country` (国家/地区) → `bd_country` · `BasedataField`
- `emergcontactype` (紧急联系人类型) → `hbss_emergcontactype` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_perfresult` · 绩效结果

**字段总数**：22

**必填字段（2）**：
- `assignment` (组织分配) · `BasedataField` → ref hrpi_assignment
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（7）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `assignment` (组织分配) → `hrpi_assignment` · `BasedataField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `empposrel` (任职经历) → `hrpi_empposorgrel` · `BasedataField`
- `rulescore` (评分分制) → `hbss_scoresystem` · `BasedataField`
- `perflevel` (绩效等级) → `hbss_perflevel` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `number`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_perrprecord` · 奖惩记录

**字段总数**：27

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（6）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `type` (奖惩类别) → `hbss_rewpnmtype` · `BasedataField`
- `level` (奖惩级别) → `hbss_rewpnmlevel` · `BasedataField`
- `currency` (币别) → `bd_currency` · `CurrencyField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

## 三、关键约束（共 12 实体）

| 约束 | 适用实体 | 来源 |
|---|---|---|
| `number` 唯一性 | 全部 12 实体 | 标品 CodeRuleOp + 元数据 UniqueValidation |
| `enable` 默认值 = '1'（启用） | 全部 12 实体 | HRBaseDataEnableOp |
| `status` 状态机 (A 暂存 → B 待审核 → C 已审核) | 全部 12 实体 | HRBaseDataStatusOp |
| `disabler` / `disabledate` 自动维护 | 全部 12 实体 | HRBaseDataEnableOp |

---

**精修元数据**：
- 生成器：`scripts/polish_aggregate_scene.py`
- 数据源：12 子实体的 `_shared/_standard_metadata/entity_metadata/<entity>.md`
- 标品共性来自 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` / `HRBaseDataEnableOp` / `HRBaseOriginalOp` / `HRBaseDataLogOp` 反编译实证