---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 0VO5EV13=I9W
app_number: hcdm
app_name: 薪酬管理
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcdm_coefficient — 薪酬标准系数

**表单编码**: `hcdm_coefficient`  
**表单ID**: `15R1E9S5XVBA`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_coefficient（薪酬标准系数） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_coefficient` | 主表 · 28 列 |
| `t_hcdm_coefficient_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcdm_coefficient.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcdm_coefficient_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcdm_coefficient.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcdm_coefficient.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_coefficient.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcdm_coefficient.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_coefficient.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_coefficient.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcdm_coefficient.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hcdm_coefficient_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcdm_coefficient_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hcdm_coefficient.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcdm_coefficient.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcdm_coefficient.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcdm_coefficient.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hcdm_coefficient.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hcdm_coefficient.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hcdm_coefficient.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hcdm_coefficient.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hcdm_coefficient.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hcdm_coefficient.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hcdm_coefficient.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hcdm_coefficient.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hcdm_coefficient.fhisversion |  |  |
| coefficienttab | 薪酬标准系数表 | BasedataField | t_hcdm_coefficient.fcoefficienttabid |  | hcdm_coefficienttab |
| coefficientvalue | 系数值 | DecimalField | t_hcdm_coefficient.fcoefficientvalue | ✓ |  |
| coefficienttabname | 薪酬标准系数表名称 | BasedataPropField | — |  |  |
| coefficientdim | 系数维度 | BasedataPropField | — |  |  |
| dimvalue | 系数维度值 | ItemClassField | — | ✓ |  |
| dimclass | 系数维度 | ItemClassTypeField | t_hcdm_coefficient.fdimclass |  |  |
| isincludesub | 包含下级 | CheckBoxField | t_hcdm_coefficient.fisincludesub |  |  |
| hbss_workplace | 行政区划 | BasedataField | — |  | hbss_workplace |
| haos_adminorghrf7 | 行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_coefficient（主表） | 25 |
| t_hcdm_coefficient_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
