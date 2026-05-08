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

# hbss_appbusinesstype — 应用与业务类型关系配置

**表单编码**: `hbss_appbusinesstype`  
**表单ID**: `0RUF0XSVJUUP`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hbss_appbusinesstype（应用与业务类型关系配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hbss_appbusinesstype` | 主表 · 17 列 |
| `t_hbss_appbusinesstype_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbss_appbusinesstype.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hbss_appbusinesstype_l.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbss_appbusinesstype.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbss_appbusinesstype.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbss_appbusinesstype.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbss_appbusinesstype.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbss_appbusinesstype.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbss_appbusinesstype.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbss_appbusinesstype.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hbss_appbusinesstype_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbss_appbusinesstype_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hbss_appbusinesstype.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbss_appbusinesstype.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbss_appbusinesstype.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbss_appbusinesstype.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| app | 应用 | BasedataField | t_hbss_appbusinesstype.fappid | ✓ | bos_devportal_bizapp |
| businesstype | 所属业务类型 | BasedataField | t_hbss_appbusinesstype.fbusinesstypeid | ✓ | hbss_bussinessfield |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_appbusinesstype（主表） | 14 |
| t_hbss_appbusinesstype_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
