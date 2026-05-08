---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: 0VO5EV13=I9W
app_number: hcdm
app_name: 薪酬管理
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcdm_salarystandard — 薪酬标准表

**表单编码**: `hcdm_salarystandard`  
**表单ID**: `0VXIC/9K1431`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_salarystandard（薪酬标准表） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_salarystd` | 主表 · 46 列 |
| `t_hcdm_salarystditem` | 分录表 · 6 列 |
| `t_hcdm_intervalps` | 分录表 · 4 列 |
| `t_hcdm_contrastps` | 分录表 · 3 列 |
| `t_hcdm_appliedrange` | 分录表 · 3 列 |
| `t_hcdm_stdsumdata` | 分录表 · 3 列 |
| `t_hcdm_contrastsumdata` | 分录表 · 4 列 |
| `t_hcdm_stdgradeentry` | 分录表 · 3 列 |
| `t_hcdm_stdrankentry` | 分录表 · 3 列 |
| `t_hcdm_salarystd_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcdm_salarystd.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcdm_salarystd_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcdm_salarystd.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcdm_salarystd.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_salarystd.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcdm_salarystd.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_salarystd.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_salarystd.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcdm_salarystd.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hcdm_salarystd.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hcdm_salarystd.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hcdm_salarystd.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hcdm_salarystd.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hcdm_salarystd.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hcdm_salarystd.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hcdm_salarystd.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hcdm_salarystd_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcdm_salarystd_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcdm_salarystd.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcdm_salarystd.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcdm_salarystd.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hcdm_salarystd.fboid |  |  |
| iscurrentversion | 当前生效数据 | CheckBoxField | t_hcdm_salarystd.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hcdm_salarystd.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hcdm_salarystd.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hcdm_salarystd.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hcdm_salarystd.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hcdm_salarystd.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hcdm_salarystd.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hcdm_salarystd.fhisversion |  |  |
| type | 标准表类型 | ComboField | t_hcdm_salarystd.ftype | ✓ |  |

## 实体: salarystditem（定调薪项目表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| itemindex | 项目序号 | IntegerField | t_hcdm_salarystditem.fitemindex |  |  |
| salaryitem | 定调薪项目/辅助项目 | BasedataField | t_hcdm_salarystditem.fsalaryitemid |  | hsbs_standarditemtpl |
| itemisusesalarycount | 启用薪点 | CheckBoxField | t_hcdm_salarystditem.fitemisusesalarycount |  |  |
| entryboid | 分录BOID | BigIntField | t_hcdm_stdrankentry.fentryboid |  |  |
| itemusespecialrank | 辅助档 | MulBasedataField | — |  |  |
| userankrange | 薪档范围 | ComboField | t_hcdm_salarystditem.fuserankrange |  |  |
| itemuserank | 薪档 | MulBasedataField | — |  |  |
| itemisusespecialrank | 启用辅助档 | CheckBoxField | t_hcdm_salarystditem.fitemisusespecialrank |  |  |

## 实体: intervalps（区间属性设置表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| intervalpsdisplayname | 区间属性显示名称 | TextField | t_hcdm_intervalps.fintervalpsdisplayname |  |  |
| intervalpsindex | 区间顺序号 | IntegerField | t_hcdm_intervalps.fintervalpsindex |  |  |
| intervalpsname | 区间属性名称 | ComboField | t_hcdm_intervalps.fintervalpsname |  |  |
| entryboid4 | 分录BOID | BigIntField | — |  |  |

## 实体: contrastps（对照属性设置表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| pspropindex | 属性设置序号 | IntegerField | t_hcdm_contrastps.fpspropindex |  |  |
| pscontrastpropconf | 对照属性配置ID | BasedataField | t_hcdm_contrastps.fpscontrastpropconfid |  | hcdm_contrastpropconf |
| entryboid5 | 分录BOID | BigIntField | — |  |  |
| salary_grade_sort | 薪等排序 | ComboField | — |  |  |
| salary_rank_sort | 薪级排序 | ComboField | — |  |  |
| show_summation | 显示合计 | CheckBoxField | — |  |  |
| show_salary_count | 仅显示薪点 | CheckBoxField | — |  |  |
| tempcontrastps | 上次选择的对照属性 | MulBasedataField | — |  |  |

## 实体: displayset（显示设置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| displayproperty | 属性 | TextField | — |  |  |
| entryboid8 | 分录BOID | BigIntField | — |  |  |
| displaypropertyval | 属性值 | IntegerField | — |  |  |
| show_rank_gap | 显示等差 | CheckBoxField | — |  |  |
| show_variation_gap | 显示档差 | CheckBoxField | — |  |  |
| jobscm | 职位体系方案： | BasedataField | t_hcdm_salarystd.fjobscmid |  | hbjm_jobscmhr |
| contrastitem | 对照薪酬项目 | TextField | — |  |  |
| radiofield | 薪等薪档 | RadioField | — |  |  |
| calcmethod | 计算方式 | RadioGroupField | t_hcdm_salarystd.fcalcmethod |  |  |
| radiofield1 | 中位值、幅宽 | RadioField | — |  |  |
| radiofield2 | 中位值、档差 | RadioField | — |  |  |
| radiofield3 | 其他 | RadioField | — |  |  |

## 实体: appliedrange（应用范围） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rangename | 应用范围 | TextField | — |  |  |
| rangeid | 应用范围 | TextField | t_hcdm_appliedrange.frangeid |  |  |
| rangetype | 应用范围类型 | ComboField | t_hcdm_appliedrange.frangetype |  |  |
| entryboid7 | 分录BOID | BigIntField | — |  |  |

## 实体: appliedrangeshow（应用范围） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rangenameshow | 应用范围 | TextField | — |  |  |
| rangetypeshow | 应用范围类型 | ComboField | — |  |  |
| entryboid9 | 分录BOoID | BigIntField | — |  |  |
| rangesum | 总览 | TextField | — |  |  |
| salarygradecount | 设置薪等数合计 | IntegerField | — |  |  |
| salaryrankcount | 设置薪档数合计 | IntegerField | — |  |  |
| salarycountamount | 薪点金额 | DecimalField | t_hcdm_salarystd.fsalarycountamount |  |  |
| isusesalaryrank | 薪档 | CheckBoxField | t_hcdm_salarystd.fisusesalaryrank |  |  |
| isusesalarycount | 使用薪点值 | CheckBoxField | t_hcdm_salarystd.fisusesalarycount |  |  |
| basesum | 总览 | TextField | — |  |  |
| generalenname | 通用英文名 | TextField | t_hcdm_salarystd.fgeneralenname |  |  |
| country | 国家/地区 | BasedataField | t_hcdm_salarystd.fcountryid | ✓ | bd_country |
| currency | 币种 | BasedataField | t_hcdm_salarystd.fcurrencyid | ✓ | bd_currency |
| adminorg | 行政组织 | HRAdminOrgField | t_hcdm_salarystd.fadminorgid |  | haos_adminorghrf7 |
| monetaryunit | 单位 | ComboField | t_hcdm_salarystd.fmonetaryunit | ✓ |  |
| frequency | 频度 | BasedataField | t_hcdm_salarystd.ffrequencyid | ✓ | hsbs_calfrequency |
| curpage | 当前页数 | StepperField | — |  |  |
| tempitems | 上次选择的项目 | MulBasedataField | — |  |  |
| salarycountitem | 薪点应用项目 | TextField | — |  |  |
| salaryrankitem | 薪级应用项目 | TextField | — |  |  |
| salarygradenum | 薪等数量 | TextField | — |  |  |
| salaryranknum | 薪级数量 | TextField | — |  |  |
| amtprecision | 精度 | BasedataField | t_hcdm_salarystd.famtprecision |  | hsbs_dataprecision |

## 实体: salarystddata_a（薪酬标准数据表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| salarystditemid_a | 项目标识 | BigIntField | — |  |  |
| combofield1 | 关键字段 | ComboField | — |  |  |
| entryboid21 | 分录BOID | BigIntField | — |  |  |
| stddata | 数据 | TextField | — |  |  |
| graderankseqmap | 薪等薪档序号映射 | TextField | t_hcdm_salarystd.fgraderankseqmap |  |  |

## 实体: contrastsumdata（对照数据表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sumrowindex | 所在行号 | IntegerField | — |  |  |
| entryboid61 | 分录BOID | BigIntField | — |  |  |
| standardpropvalue | 标准属性值 | TextField | t_hcdm_contrastsumdata.fstandardpropvalue |  |  |
| graderankrange | 薪等薪档范围 | TextField | t_hcdm_contrastsumdata.fgraderankrange |  |  |

## 实体: gradeentry（薪等表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| gradeentryindex | 顺序号 | IntegerField | — |  |  |
| entryboidgrade | 分录BOID | BigIntField | — |  |  |
| grade | 薪等 | BasedataField | t_hcdm_stdgradeentry.fgradeid |  | hsbs_salarygrade |

## 实体: rankentry（薪档表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rankentryindex | 薪档顺序号 | IntegerField | — |  |  |
| entryboidrank | 分录BOID | BigIntField | — |  |  |
| rank | 薪档 | BasedataField | t_hcdm_stdrankentry.frankid |  | hsbs_salaryrank |
| stditemperm | 定调薪项目 | BasedataField | — |  | hsbs_standarditem |
| stdgradeperm | 薪等 | BasedataField | — |  | hsbs_salarygrade |
| stdrankperm | 薪档 | BasedataField | — |  | hsbs_salaryrank |
| tempgrades | 上次选择的薪等 | MulBasedataField | — |  |  |
| tempranks | 上次选择的薪档 | MulBasedataField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_salarystd（主表） | 41 |
| t_hcdm_appliedrange | 2 |
| t_hcdm_contrastps | 2 |
| t_hcdm_contrastsumdata | 2 |
| t_hcdm_intervalps | 3 |
| t_hcdm_salarystd_l | 3 |
| t_hcdm_salarystditem | 5 |
| t_hcdm_stdgradeentry | 1 |
| t_hcdm_stdrankentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 55 |
