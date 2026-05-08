---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2VKJ94YEM7AU
app_number: hrptmc
app_name: HR报表管理中心
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hrptc_rptdimmap — 报表控权维度配置

**表单编码**: `hrptc_rptdimmap`  
**表单ID**: `49P17/WQXJAI`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hrptc_rptdimmap（报表控权维度配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hrptc_rptdimmap` | 主表 · 6 列 |
| `t_hrptc_dimfield` | 分录表 · 7 列 |
| `t_hrptc_rptdimmap_l` | 多语言表 · 1 列 |
| `t_hrptc_dimfield_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptc_rptdimmap.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptc_rptdimmap.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptc_rptdimmap.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptc_rptdimmap.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hrptc_dimfield_l.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| rptmanage | 报表 | BasedataField | t_hrptc_rptdimmap.frptmanageid | ✓ | hrptmc_reportmanage |

## 实体: entryentity（维度映射） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| propkey | 字段 | TextField | t_hrptc_dimfield.fpropkey |  |  |
| propname | 字段名称 | MuliLangTextField | — |  |  |
| aoqfield | 分析对象查询字段 | BasedataField | t_hrptc_dimfield.faoqfieldid |  | hrptmc_anobjqueryfield |
| ismust | 是否必选 | CheckBoxField | t_hrptc_dimfield.fismust |  |  |
| propdescription | 描述 | MuliLangTextField | — |  |  |
| parententity | 父基础资料 | BasedataField | t_hrptc_dimfield.fparententityid |  | bos_entityobject |
| number | 字段 | TextField | t_hrptc_dimfield.fnumber |  |  |
| entity | 基础资料 | BasedataField | t_hrptc_dimfield.fentityid |  | bos_entityobject |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptc_rptdimmap（主表） | 5 |
| t_hrptc_dimfield | 6 |
| t_hrptc_dimfield_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
