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

# hsas_taxdeduct — 专项附加扣除数据

**表单编码**: `hsas_taxdeduct`  
**表单ID**: `5HI6J1NK4DOS`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_taxdeduct（专项附加扣除数据） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_taxdeduct` | 主表 · 14 列 |
| `t_hsas_taxdeductent` | 分录表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hsas_taxdeduct.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hsas_taxdeduct.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hsas_taxdeduct.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hsas_taxdeduct.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| employee | 工号 | BasedataField | t_hsas_taxdeduct.femployeeid |  | hsbs_employee |
| name | 姓名 | BasedataPropField | — |  |  |
| crdtype | 证件类型 | TextField | t_hsas_taxdeduct.fcrdtype |  |  |
| crdno | 证件号码 | TextField | t_hsas_taxdeduct.fcrdno |  |  |

## 实体: entryentity（专项附加扣除数据） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| salaryitem | 薪酬项目 | BasedataField | t_hsas_taxdeductent.fsalaryitemid |  | hsbs_salaryitem |
| itemnumber | 薪酬项目编码 | BasedataPropField | — |  |  |
| itemname | 薪酬项目名称 | BasedataPropField | — |  |  |
| salaryitemtype | 薪酬项目系统分类 | BasedataPropField | — |  |  |
| taxcategory | 个税种类 | BasedataPropField | — |  |  |
| valueview | 值 | TextField | — | ✓ |  |
| value | 值 | DecimalField | t_hsas_taxdeductent.fvalue |  |  |
| taxstartdate | 税款所属期起 | DateField | t_hsas_taxdeduct.ftaxstartdate |  |  |
| taxenddate | 税款所属期止 | DateField | t_hsas_taxdeduct.ftaxenddate |  |  |
| taxunit | 纳税单位 | BasedataField | t_hsas_taxdeduct.ftaxunitid |  | hbss_taxunit |
| taxpayertype | 纳税人类型 | ComboField | t_hsas_taxdeduct.ftaxpayertype |  |  |
| adminorg | 行政组织 | HRAdminOrgField | t_hsas_taxdeduct.fadminorgid |  | haos_adminorghrf7 |
| isused | 使用状态 | ComboField | t_hsas_taxdeduct.fisused |  |  |
| belongperiod | 税款所属年月 | TextField | t_hsas_taxdeduct.fbelongperiod |  |  |
| usedetail | 使用详情 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_taxdeduct（主表） | 14 |
| t_hsas_taxdeductent | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
