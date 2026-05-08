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

# hcdm_candsetsalact — 候选人定薪协作

**表单编码**: `hcdm_candsetsalact`  
**表单ID**: `37695S72M184`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_candsetsalact（候选人定薪协作） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_candsetsalact` | 主表 · 10 列 |
| `t_hcdm_candactentry` | 分录表 · 6 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hcdm_candsetsalact.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hcdm_candsetsalact.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hcdm_candsetsalact.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_candsetsalact.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| activitybase | 活动基本信息 | BasedataField | t_hcdm_candsetsalact.factivitybaseid |  | hcdm_activitybase |
| candsetsalapplid | 申请单ID | BigIntField | t_hcdm_candsetsalact.fcandsetsalapplid |  |  |
| onbrdinfonum | 入职办理单 | TextField | t_hcdm_candsetsalact.fonbrdinfonum |  |  |
| candsetsalapplnum | 申请单编号 | TextField | t_hcdm_candsetsalact.fcandsetsalapplnum |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| candidate | 候选人 | BasedataField | t_hcdm_candactentry.fcandidateid |  | hcf_candidate |
| offernumber | Offer编号 | TextField | t_hcdm_candactentry.foffernumber |  |  |
| isoffersetsal | Offer中已定薪 | CheckBoxField | t_hcdm_candactentry.fisoffersetsal |  |  |
| recruittype | 招聘类型 | BasedataField | t_hcdm_candactentry.frecruittypeid |  | hbss_recrutyp |
| avatar | 头像 | PictureField | t_hcdm_candactentry.favatar |  |  |
| offerid | offerid | BigIntField | t_hcdm_candactentry.fofferid |  |  |
| iscandsetsal | 需定薪 | CheckBoxField | t_hcdm_candsetsalact.fiscandsetsal |  |  |
| onbrdinfoid | 入职办理单ID | BigIntField | t_hcdm_candsetsalact.fonbrdinfoid |  |  |
| candsetsalapplstatus | 申请单状态 | BillStatusField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_candsetsalact（主表） | 10 |
| t_hcdm_candactentry | 6 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 2 |
