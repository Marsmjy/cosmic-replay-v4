---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_approvebilltpl — 薪资审批单模板

**表单编码**: `hsas_approvebilltpl`  
**表单ID**: `17+WT2BQMB=O`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_approvebilltpl（薪资审批单模板） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_approvebilltpl` | 主表 · 36 列 |
| `t_hsas_approvesurvey` | 分录表 · 8 列 |
| `t_hsas_approvespclent` | 分录表 · 3 列 |
| `t_hsas_approvedefent` | 分录表 · 4 列 |
| `t_hsas_approveschent` | 分录表 · 9 列 |
| `t_hsas_approveapvent` | 分录表 · 4 列 |
| `t_hsas_approveageent` | 分录表 · 4 列 |
| `t_hsas_approvebilltpl_l` | 多语言表 · 3 列 |
| `t_hsas_approvesurvey_l` | 多语言表 · 2 列 |
| `t_hsas_approveschent_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_approvebilltpl.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_approvebilltpl_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_approvebilltpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_approvebilltpl.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_approvebilltpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_approvebilltpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_approvebilltpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_approvebilltpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_approvebilltpl.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_approvebilltpl.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_approvebilltpl.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_approvebilltpl.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_approvebilltpl.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_approvebilltpl.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_approvebilltpl.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_approvebilltpl.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_approvebilltpl_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_approvebilltpl_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_approvebilltpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_approvebilltpl.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_approvebilltpl.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_approvebilltpl.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_approvebilltpl.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_approvebilltpl.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsas_approvebilltpl.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_approvebilltpl.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_approvebilltpl.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_approvebilltpl.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_approvebilltpl.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_approvebilltpl.fhisversion |  |  |

## 实体: overviewentryentity（概览单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldvalue | 取值字段 | TextField | t_hsas_approvesurvey.ffieldvalue | ✓ |  |
| isenable | 是否启用 | CheckBoxField | t_hsas_approvesurvey.fisenable |  |  |
| datasource | 数据来源 | ComboField | t_hsas_approvesurvey.fdatasource |  |  |
| valuetype | 取值方式 | ComboField | t_hsas_approvesurvey.fvaluetype | ✓ |  |
| fieldtype | 字段类型 | ComboField | t_hsas_approvesurvey.ffieldtype |  |  |
| selectfield | 选择字段 | TextField | t_hsas_approvesurvey.fselectfield |  |  |
| displayname | 显示名称 | MuliLangTextField | t_hsas_approvesurvey_l.fdisplayname | ✓ |  |
| remark | 备注 | MuliLangTextField | t_hsas_approvesurvey_l.fremark |  |  |
| entryboid | 分录BOID | BigIntField | t_hsas_approveageent.fentryboid |  |  |
| issharescheme | 设为共享 | CheckBoxField | t_hsas_approvebilltpl.fissharescheme |  |  |

## 实体: specialentryentity（特殊规则单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| approveschemeset | 审批人视图显示设置 | TextField | — |  |  |
| agentschemeset | 代办人视图显示设置 | TextField | — |  |  |
| issame | 代办与审批人视图是否一致 | CheckBoxField | t_hsas_approvespclent.fissame |  |  |
| condition | 条件项 | TextAreaField | t_hsas_approvespclent.fcondition |  |  |
| executeseq | 执行顺序 | IntegerField | — |  |  |
| conditiontxt | 条件项(审批人) | TextAreaField | — |  |  |
| entryboid1 | 分录BOID | BigIntField | — |  |  |
| apvspecialschename | 审批人特殊规则审批视图名称 | TextField | t_hsas_approveapvent.fapvspecialschename |  |  |
| apvspecialschentry | 审批人审批视图分录ID | BigIntField | — |  |  |
| entryboid4 | 分录BOID | BigIntField | — |  |  |
| apvspecialschentryboid | 审批人审批视图分录BOID | BigIntField | t_hsas_approveapvent.fapvspecialschentryboid |  |  |
| agespecialschename | 代办人特殊规则审批视图名称 | TextField | t_hsas_approveageent.fagespecialschename |  |  |
| agespecialschentry | 代办人审批视图分录ID | BigIntField | — |  |  |
| entryboid5 | 分录BOID | BigIntField | — |  |  |
| agespecialschentryboid | 代办人审批视图分录BOID | BigIntField | t_hsas_approveageent.fagespecialschentryboid |  |  |
| curpage | 当前页数 | StepperField | — |  |  |

## 实体: defaultruleentity（默认规则单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| defaultschemename | 视图名称 | TextField | t_hsas_approvedefent.fdefaultschemename |  |  |
| defaultschemeentry | 默认审批视图分录ID | BigIntField | — |  |  |
| entryboid2 | 分录BOID | BigIntField | — |  |  |
| defaultschemeentryboid | 默认审批视图分录BOID | BigIntField | t_hsas_approvedefent.fdefaultschemeentryboid |  |  |
| iseffect | 是否生效 | CheckBoxField | t_hsas_approvebilltpl.fiseffect |  |  |

## 实体: schemeentryentity（审批视图分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| referreport | 引用报表 | ComboField | t_hsas_approveschent.freferreport |  |  |
| reportscheme | 引用报表显示方案 | BasedataField | t_hsas_approveschent.freportschemeid |  | hsas_salaryrptdisplayschm |
| isdisplayzeroitem | 是否显示为零项目 | CheckBoxField | t_hsas_approveschent.fisdisplayzeroitem |  |  |
| isdisplayemptyitem | 是否显示为空项目 | CheckBoxField | t_hsas_approveschent.fisdisplayemptyitem |  |  |
| isdisplaytotal | 是否显示总计行 | CheckBoxField | t_hsas_approveschent.fisdisplaytotal |  |  |
| sumtype | 小计 | ComboField | t_hsas_approveschent.fsumtype |  |  |
| schemename | 视图名称 | MuliLangTextField | t_hsas_approveschent_l.fschemename |  |  |
| approvepaydetail | 审批单发放明细 | TextAreaField | t_hsas_approveschent.fapprovepaydetail |  |  |
| reportschemename | 显示方案 | TextAreaField | — |  |  |
| entryboid3 | 分录boid | BigIntField | — |  |  |
| referreportname | 报表 | ComboField | — |  |  |
| copynum | 复制次数 | IntegerField | t_hsas_approvebilltpl.fcopynum |  |  |
| isonhold | 包含停缓发人员 | CheckBoxField | t_hsas_approvebilltpl.fisonhold |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_approvebilltpl（主表） | 31 |
| t_hsas_approveageent | 3 |
| t_hsas_approveapvent | 2 |
| t_hsas_approvebilltpl_l | 3 |
| t_hsas_approvedefent | 2 |
| t_hsas_approveschent | 7 |
| t_hsas_approveschent_l | 1 |
| t_hsas_approvespclent | 2 |
| t_hsas_approvesurvey | 6 |
| t_hsas_approvesurvey_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 21 |
