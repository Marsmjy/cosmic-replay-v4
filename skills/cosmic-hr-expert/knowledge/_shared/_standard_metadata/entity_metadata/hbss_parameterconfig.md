---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: XYRL3+A8Z+Z
app_number: hbss
app_name: HR基础服务
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hbss_parameterconfig — HR基础资料参数配置

**表单编码**: `hbss_parameterconfig`  
**表单ID**: `1ZKD40O+IDII`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hbss_parameterconfig（HR基础资料参数配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hbss_parameterconfig` | 主表 · 12 列 |
| `t_hbss_parameterconfig_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hbss_parameterconfig.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hbss_parameterconfig.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hbss_parameterconfig.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hbss_parameterconfig.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hbss_parameterconfig_l.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| app | 所属应用 | BasedataField | t_hbss_parameterconfig.fappid | ✓ | bos_devportal_bizapp |
| basedatafield | 基础资料 | BasedataField | t_hbss_parameterconfig.fbasedatafield | ✓ | hbp_entityobject |
| enablestatus | 默认使用状态 | ComboField | t_hbss_parameterconfig.fenablestatus | ✓ |  |
| auditcheck | 需审核 | CheckBoxField | — |  |  |
| changecheck | 记录操作日志 | CheckBoxField | — |  |  |
| isopen | 参数类型 | ComboField | t_hbss_parameterconfig.fisopen |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbss_parameterconfig.fissyspreset |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_parameterconfig（主表） | 9 |
| t_hbss_parameterconfig_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
