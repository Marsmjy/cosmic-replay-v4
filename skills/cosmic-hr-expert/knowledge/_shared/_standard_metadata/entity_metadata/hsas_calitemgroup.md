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

# hsas_calitemgroup — 计算规则模板

**表单编码**: `hsas_calitemgroup`  
**表单ID**: `017RK0C7YKE=`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_calitemgroup（计算规则模板） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_calitemgroup` | 主表 · 35 列 |
| `t_hsas_calitemgroupentry` | 分录表 · 4 列 |
| `t_hsas_calitemgroup_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_calitemgroup.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_calitemgroup_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_calitemgroup.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_calitemgroup.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_calitemgroup.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_calitemgroup.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_calitemgroup.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_calitemgroup.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_calitemgroup.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_calitemgroup.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_calitemgroup.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_calitemgroup.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_calitemgroup.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_calitemgroup.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_calitemgroup.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_hsas_calitemgroup.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_calitemgroup_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_calitemgroup_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_calitemgroup.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_calitemgroup.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_calitemgroup.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_calitemgroup.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_calitemgroup.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_calitemgroup.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_calitemgroup.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_calitemgroup.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_calitemgroup.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_calitemgroup.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_calitemgroup.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_calitemgroup.fhisversion |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| datasource | 数据来源 | ComboField | t_hsas_calitemgroupentry.fdatasource | ✓ |  |
| calindex | 计算顺序 | IntegerField | — |  |  |
| salaryitem | 薪酬项目名称 | BasedataField | t_hsas_calitemgroupentry.fsalaryitemid | ✓ | hsbs_salaryitem |
| salaryitemispayout | 实发项目 | BasedataPropField | — |  |  |
| salaryitemnumber | 薪酬项目编码 | BasedataPropField | — |  |  |
| formulanumber | 计算公式编码 | BasedataPropField | — |  |  |
| calblock | 计算区段 | BasedataPropField | — |  |  |
| taxtag | 个税标签 | BasedataPropField | — |  |  |
| salaryitemtype | 薪酬项目系统分类 | BasedataPropField | — |  |  |
| entryboid | 分录业务ID | BigIntField | t_hsas_calitemgroupentry.fentryboid |  |  |
| formula | 计算公式名称 | BasedataField | t_hsas_calitemgroupentry.fformulaid |  | hsas_formula |
| generalenname | 通用英文名 | TextField | t_hsas_calitemgroup.fgeneralenname |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsas_calitemgroup.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_calitemgroup.fcountryid |  | bd_country |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_calitemgroup（主表） | 30 |
| t_hsas_calitemgroup_l | 3 |
| t_hsas_calitemgroupentry | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 13 |
