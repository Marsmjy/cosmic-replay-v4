---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1C8H4/N38LCY
app_number: wtte
app_name: 考勤核算
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtte_revisionhis — 调整历史

**表单编码**: `wtte_revisionhis`  
**表单ID**: `325Z+O7VX5/=`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_revisionhis（调整历史） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_revisionallhis` | 主表 · 15 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtte_revisionallhis.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtte_revisionallhis.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtte_revisionallhis.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtte_revisionallhis.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| attfilebase | 姓名 | BasedataField | t_wtte_revisionallhis.fattfilebaseid |  | wtp_attfilebase |
| revisionentryid | 记录修正单据体 | BigIntField | — |  |  |
| daterangefield | 日期范围 | DateRangeField | — |  |  |
| adjusttype | 处理方式 | ComboField | t_wtte_revisionallhis.fadjusttype |  |  |
| attitem | 考勤项目 | BasedataField | t_wtte_revisionallhis.fattitemid |  | wtbd_attitem |
| value | 调整值 | DecimalField | t_wtte_revisionallhis.fvalue |  |  |
| datastatus | 版本状态 | ComboField | t_wtte_revisionallhis.fdatastatus |  |  |
| attitemto | 转移至项目 | BasedataField | t_wtte_revisionallhis.fattitemtoid |  | wtbd_attitem |
| revisiontype | 调整类型 | ComboField | t_wtte_revisionallhis.frevisiontype |  |  |
| org | 考勤管理组织 | OrgField | t_wtte_revisionallhis.forgid |  | bos_org |
| revisiondate | 调整日期 | DateField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_revisionallhis（主表） | 12 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
