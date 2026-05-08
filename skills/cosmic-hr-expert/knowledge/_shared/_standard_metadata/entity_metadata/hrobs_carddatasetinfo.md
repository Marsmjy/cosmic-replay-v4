---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2W6FZY1I61I+
app_number: hrobs
app_name: HR运营基础服务
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hrobs_carddatasetinfo — 数据集基本信息

**表单编码**: `hrobs_carddatasetinfo`  
**表单ID**: `51HM1P19GPWT`  
**归属**: HR基础服务云 / HR运营基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hrobs_carddatasetinfo（数据集基本信息） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hrobs_datasetconfig` | 主表 · 15 列 |
| `t_hrobs_datasetconfig_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrobs_datasetconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hrobs_datasetconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hrobs_datasetconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrobs_datasetconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrobs_datasetconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrobs_datasetconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrobs_datasetconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrobs_datasetconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrobs_datasetconfig.fmasterid |  |  |
| wbgroup | 所属领域 | BasedataField | t_hrobs_datasetconfig.fwbgroupid | ✓ | hrobs_wbgroup |
| description | 描述 | MuliLangTextField | t_hrobs_datasetconfig_l.fdescription |  |  |
| issyspreset | 系统预置 | ComboField | t_hrobs_datasetconfig.fissyspreset |  |  |
| binddatainfo | 数据ID | TextField | — |  |  |
| dstype | 数据集类型 | BasedataField | t_hrobs_datasetconfig.fdstypeid | ✓ | hrobs_datasettype |
| basedatainfotext | 数据ID文本 | TextField | — |  |  |
| autocreated | 自动创建 | ComboField | t_hrobs_datasetconfig.fautocreated |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrobs_datasetconfig（主表） | 12 |
| t_hrobs_datasetconfig_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 2 |
