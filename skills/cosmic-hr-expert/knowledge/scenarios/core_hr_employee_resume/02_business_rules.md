# core_hr_employee_resume · 业务规则

> **聚合场景**：员工履历聚合（教育/工作/技能/职称/培训/导师等 12 实体）（12 个子实体）
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

### `hrpi_pereduexp` · 教育经历

**字段总数**：33

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（10）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `education` (学历) → `hbss_diploma` · `BasedataField`
- `edunature` (学历性质) → `hbss_diplomatype` · `BasedataField`
- `degree` (学位) → `hbss_degree` · `BasedataField`
- `authedegree` (学位授予国家/地区) → `bd_country` · `BasedataField`
- `collegecountry` (院校所在国家/地区) → `bd_country` · `BasedataField`
- `graduateschool` (毕业院校) → `hbss_college` · `HisModelBasedataField`
- `certtype` (证书类型) → `hbss_educerttype` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_preworkexp` · 前工作经历

**字段总数**：34

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（8）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `empcountry` (前雇主所属国家/地区) → `bd_country` · `BasedataField`
- `jobcityid` (工作城市) → `bd_admindivision` · `CityField`
- `trade` (所属行业) → `hbss_industrytype` · `BasedataField`
- `businesstypeid` (企业性质) → `hbss_empnature` · `BasedataField`
- `companyscale` (公司规模) → `hbss_companyscale` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_empproexp` · 项目经历

**字段总数**：30

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（7）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `area` (项目地区) → `bd_country` · `BasedataField`
- `projecttype` (项目类型) → `hbss_projecttype` · `BasedataField`
- `companynature` (企业性质) → `hbss_empnature` · `BasedataField`
- `industry` (所属行业) → `hbss_industrytype` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `name`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_perlgability` · 语言技能

**字段总数**：25

**必填字段（2）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query
- `language` (语言种类) · `BasedataField` → ref hbss_languagetype

**引用基础资料（9）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `language` (语言种类) → `hbss_languagetype` · `BasedataField`
- `languagecert` (语言证书) → `hbss_languagecert` · `BasedataField`
- `listen` (听) → `hbss_familiarity` · `BasedataField`
- `speak` (说) → `hbss_familiarity` · `BasedataField`
- `read` (读) → `hbss_familiarity` · `BasedataField`
- `write` (写) → `hbss_familiarity` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `name`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_perocpqual` · 职业资格

**字段总数**：23

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（6）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `qualification` (职业资格) → `hbss_ocpqual` · `BasedataField`
- `qualevel` (职业资格等级) → `hbss_ocpquallevel` · `BasedataField`
- `issuednation` (颁发国家/地区) → `bd_country` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `name`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_perpractqual` · 执业资格

**字段总数**：20

**必填字段（2）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query
- `qualification` (执业资格) · `BasedataField` → ref hbss_operationqual

**引用基础资料（6）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `qualification` (执业资格) → `hbss_operationqual` · `BasedataField`
- `qualevel` (执业资格等级) → `hbss_ocpquallevel` · `BasedataField`
- `issuednation` (颁发国家/地区) → `bd_country` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_perprotitle` · 职称信息

**字段总数**：22

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（5）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `professional` (职称) → `hbss_protitle` · `BasedataField`
- `prolevel` (职称级别) → `hbss_protitlelevel` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_emptrainfile` · 培训经历

**字段总数**：28

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（6）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `traintype` (培训类别) → `hbss_traintype` · `BasedataField`
- `trainmode` (培训方式) → `hbss_trainmode` · `BasedataField`
- `currency` (币种) → `bd_currency` · `CurrencyField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `name`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_rsmpatinv` · 专利发明

**字段总数**：21

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（6）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `patentcategoryid` (专利类别) → `hbss_patentscategory` · `BasedataField`
- `patentstatusid` (专利状态) → `hbss_patentstatus` · `BasedataField`
- `country` (所属国家/地区) → `bd_country` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_rsmproskl` · 专业技能

**字段总数**：12

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（4）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `familiarityid` (掌握程度) → `hbss_familiarity` · `BasedataField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`, `name`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_empstage` · 雇佣阶段

**字段总数**：12

**必填字段（3）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query
- `entrydate` (入职日期) · `DateField`
- `enddate` (结束日期) · `DateField`

**引用基础资料（3）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`

**标品公共字段**：`creator`, `createtime`, `modifier`, `modifytime`

> ⚠️ 这些字段由 HRBaseDataTplEdit 标品维护 · ISV **不可改值**（除特殊场景）

### `hrpi_emptutor` · 导师

**字段总数**：15

**必填字段（1）**：
- `employee` (员工) · `EmployeeField` → ref hrpi_employeenewf7query

**引用基础资料（5）**：
- `creator` (创建人) → `bos_user` · `CreaterField`
- `modifier` (修改人) → `bos_user` · `ModifierField`
- `employee` (员工) → `hrpi_employeenewf7query` · `EmployeeField`
- `tutor` (导师) → `hrpi_employeenewf7query` · `EmployeeField`
- `empstage` (雇佣阶段) → `hrpi_empstage` · `BasedataField`

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