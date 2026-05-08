---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 17/+7RIW4SCJ
app_number: sitbs
app_name: 社保个税基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# sitbs_sinsurstddim — 标准维度

**表单编码**: `sitbs_sinsurstddim`  
**表单ID**: `4GZA0FXPEL6A`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_sinsurstddim（标准维度） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_sinsurstddim` | 主表 · 19 列 |
| `t_sitbs_sinsurstddim_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_sinsurstddim.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_sinsurstddim_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_sinsurstddim.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_sinsurstddim.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_sinsurstddim.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_sinsurstddim.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_sinsurstddim.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_sinsurstddim.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_sinsurstddim.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_sinsurstddim_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_sinsurstddim_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_sitbs_sinsurstddim.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_sinsurstddim.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_sinsurstddim.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_sinsurstddim.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| srcentity | 数据来源实体 | BasedataField | t_sitbs_sinsurstddim.fsrcentityid | ✓ | bos_entityobject |
| fieldtype | 字段类型 | ComboField | t_sitbs_sinsurstddim.ffieldtype | ✓ |  |
| srcfieldentity | 数据来源所属实体 | TextField | t_sitbs_sinsurstddim.fsrcfieldentity |  |  |
| sidimfield | 数据来源字段 | BasedataField | t_sitbs_sinsurstddim.fsidimfieldid | ✓ | sitbs_sidimfield |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_sinsurstddim（主表） | 16 |
| t_sitbs_sinsurstddim_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
