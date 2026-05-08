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

# hsas_datagrade — 数据分级

**表单编码**: `hsas_datagrade`  
**表单ID**: `1KIMIO/NFAGM`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_datagrade（数据分级） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_grade` | 主表 · 40 列 |
| `t_hsas_gradedataent` | 分录表 · 5 列 |
| `t_hsas_gradecondition` | 分录表 · 7 列 |
| `t_hsas_graderesult` | 分录表 · 8 列 |
| `t_hsas_grade_l` | 多语言表 · 3 列 |
| `t_hsas_gradecondition_l` | 多语言表 · 1 列 |
| `t_hsas_graderesult_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_grade.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_graderesult_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_grade.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_grade.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_grade.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_grade.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_grade.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_grade.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_grade.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_grade.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_grade.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_grade.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_grade.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_grade.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_grade.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_grade.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_grade_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_grade_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_grade.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_grade.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_grade.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_grade.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_grade.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_grade.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_grade.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_grade.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_grade.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_grade.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_grade.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_grade.fhisversion |  |  |
| beyondpolicy | 使用超出上下限处理策略 | ComboField | t_hsas_grade.fbeyondpolicy | ✓ |  |
| dataround | 精度尾差处理 | BasedataField | t_hsas_grade.fdataroundid |  | hsbs_dataround |
| failpolice | 匹配失败处理策略 | ComboField | t_hsas_grade.ffailpolice | ✓ |  |

## 实体: dataentry（分录数据存储分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rownum | 行号 | IntegerField | t_hsas_gradedataent.frownum |  |  |
| fieldid | 属性列编码 | TextField | t_hsas_gradedataent.ffieldid |  |  |
| value | 数据值 | TextField | t_hsas_gradedataent.fvalue |  |  |
| fieldname | 列名 | TextField | t_hsas_gradedataent.ffieldname |  |  |
| entryboiddata | 分录数据存储业务ID | BigIntField | t_hsas_gradedataent.fentryboiddata |  |  |

## 实体: conditionentryidentify（条件分录属性） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| conditionitemname | 条件名称 | MuliLangTextField | — | ✓ |  |
| conditionvaltype | 数据类型 | BasedataField | — | ✓ | hsbs_datatype |
| conditionaccuracy | 数据精度 | ComboField | — |  |  |
| conditionlength | 数据长度 | IntegerField | — |  |  |
| conditionid | 条件ID | TextField | — |  |  |
| conditioncurrency | 币种 | BasedataField | t_hsas_gradecondition.fconditioncurrency |  | bd_currency |
| entryboid | 分录业务ID | BigIntField | t_hsas_gradecondition.fentryboid |  |  |

## 实体: resultentryidentify（结果分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| resultitemname | 结果名称 | MuliLangTextField | — | ✓ |  |
| resultvaltype | 数据类型 | BasedataField | — | ✓ | hsbs_datatype |
| resultaccuracy | 数据精度 | ComboField | — |  |  |
| resultlength | 数据长度 | IntegerField | — |  |  |
| resultdefaultval | 结果默认值 | TextField | — |  |  |
| resultid | 结果ID | TextField | — |  |  |
| resultcurrency | 币种 | BasedataField | t_hsas_graderesult.fresultcurrency |  | bd_currency |
| entryboidresult | 结果分录业务ID | BigIntField | t_hsas_graderesult.fentryboidresult |  |  |
| executeexp | 可执行表达式 | TextAreaField | t_hsas_grade.fexecuteexp |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsas_grade.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_grade.fcountryid |  | bd_country |
| uniquecode | 唯一编码 | TextField | t_hsas_grade.funiquecode |  |  |
| matchmethod | 匹配方法 | ComboField | t_hsas_grade.fmatchmethod | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_grade（主表） | 35 |
| t_hsas_grade_l | 2 |
| t_hsas_gradecondition | 2 |
| t_hsas_gradedataent | 5 |
| t_hsas_graderesult | 2 |
| t_hsas_graderesult_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
