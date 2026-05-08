---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2WQD=V5DU=7Q
app_number: hies
app_name: HR导入导出管理
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hies_diaesysparam — 系统默认参数

**表单编码**: `hies_diaesysparam`  
**表单ID**: `2VJEJHS2NGKF`  
**归属**: HR基础服务云 / HR导入导出管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hies_diaesysparam（系统默认参数） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hies_diaesysparam` | 主表 · 13 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hies_diaesysparam.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hies_diaesysparam.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hies_diaesysparam.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hies_diaesysparam.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| setnullparam | 置空处理参数 | ComboField | t_hies_diaesysparam.fsetnullparam | ✓ |  |
| maxshownum | 基础资料下拉值上限 | ComboField | t_hies_diaesysparam.fmaxshownum | ✓ |  |
| importdisable | 引用基础资料为禁用时可导入 | CheckBoxField | t_hies_diaesysparam.fimportdisable |  |  |
| imptname | “导出”名称替换 | ComboField | t_hies_diaesysparam.fimptname |  |  |
| exptname | “导入”名称替换 | ComboField | t_hies_diaesysparam.fexptname |  |  |
| isimptfillpentry | 导入时填写上级分录 | CheckBoxField | t_hies_diaesysparam.fisimptfillpentry |  |  |
| isexptautofillpentry | 导出时自动填写上级分录 | CheckBoxField | t_hies_diaesysparam.fisexptautofillpentry |  |  |
| globalfieldstyle | 全局样式 | TextField | t_hies_diaesysparam.fglobalfieldstyle |  |  |
| plugin | 插件 | TextField | t_hies_diaesysparam.fplugin |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hies_diaesysparam（主表） | 13 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
