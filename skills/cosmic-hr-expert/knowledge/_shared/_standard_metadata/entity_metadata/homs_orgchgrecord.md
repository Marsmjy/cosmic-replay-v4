---
source: openapi_runtime
extracted_at: 2026-04-27
extractor: build_standard_metadata_md_from_openapi.py
app_id: 217WYC/L9U7E
app_number: homs
app_name: 组织管理
cloud_number: ODC
cloud_name: 组织发展云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# homs_orgchgrecord — 组织变动明细查询

**表单编码**: `homs_orgchgrecord`  
**表单ID**: `2POTG6QTABRL`  
**归属**: 组织发展云 / 组织管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: homs_orgchgrecord（组织变动明细查询） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_homs_orgchgrecord` | 主表 · 6 列 |
| `t_homs_orgchgentry` | 分录表 · 10 列 |
| `t_homs_orgchgdetail` | 分录表 · 5 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_homs_orgchgrecord.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_homs_orgchgrecord.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_homs_orgchgrecord.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_homs_orgchgrecord.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_homs_orgchgrecord.finitdatasource |  |  |
| adminorg | 行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| searchdate | 生效日期 | DateField | — |  |  |
| searchchangescene | 变动场景 | BasedataField | — |  | haos_changescene |

## 实体: orgchgentry（组织变动记录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| chgeffecttime | 变动生效日期 | DateField | t_homs_orgchgentry.fchgeffecttime |  |  |
| changescene | 变动场景 | BasedataField | t_homs_orgchgentry.fchangesceneid |  | haos_changescene |
| changetype | 变动类型 | BasedataField | t_homs_orgchgentry.fchangetypeid |  | haos_orgchangetype |
| changereason | 变动原因 | BasedataField | t_homs_orgchgentry.fchangereasonid |  | haos_orgchangereason |
| operator | 变动人 | UserField | t_homs_orgchgentry.foperatorid |  | bos_user |
| operationtime | 操作日期 | DateTimeField | t_homs_orgchgentry.foperationtime |  |  |
| chgbill | 单据 | BasedataField | t_homs_orgchgentry.fchgbillid |  | homs_orgchgbill |
| orgentry | 申请单分录ID | BigIntField | — |  |  |
| afterchgorg | 变更后组织 | BigIntField | — |  |  |
| mergesplitview | 合并/拆分概览 | TextField | — |  |  |
| changedescription | 变动说明 | TextField | t_homs_orgchgentry.fchangedescription |  |  |
| chgentitynumber | 变动实体 | TextField | t_homs_orgchgdetail.fchgentitynumber |  |  |
| chgpageelement | 变动实体页面标识 | TextField | t_homs_orgchgdetail.fchgpageelement |  |  |
| beforechgentity | 变更前实体 | BigIntField | — |  |  |
| coopreltype | 协作类型 | BigIntField | — |  |  |
| afterchgentity | 变更后实体 | BigIntField | — |  |  |
| beforevalue | 变动前 | TextField | — |  |  |
| aftervalue | 变动后 | TextField | — |  |  |
| changefield | 变动字段 | TextField | — |  |  |
| searchbillno | 单据编号 | TextField | — |  |  |
| searchdispatchno | 发文编号 | TextField | — |  |  |
| searchdispatchname | 发文名称 | TextField | — |  |  |
| seatchsimple | 简称 | TextField | — |  |  |
| parentlongname | 父组织长名称 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_homs_orgchgrecord（主表） | 5 |
| t_homs_orgchgdetail | 2 |
| t_homs_orgchgentry | 8 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
