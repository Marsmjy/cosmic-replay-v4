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

# hies_entityencryptconf — 导出实体加密参数设置

**表单编码**: `hies_entityencryptconf`  
**表单ID**: `33C=5WN5PQ7V`  
**归属**: HR基础服务云 / HR导入导出管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hies_entityencryptconf（导出实体加密参数设置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hies_entityencryptconf` | 主表 · 17 列 |
| `t_hies_entityencryptconf_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hies_entityencryptconf.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hies_entityencryptconf_l.fname |  |  |
| status | 数据状态 | BillStatusField | t_hies_entityencryptconf.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hies_entityencryptconf.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hies_entityencryptconf.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hies_entityencryptconf.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hies_entityencryptconf.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hies_entityencryptconf.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hies_entityencryptconf.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hies_entityencryptconf_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hies_entityencryptconf_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hies_entityencryptconf.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hies_entityencryptconf.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hies_entityencryptconf.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hies_entityencryptconf.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| entity | 加密实体 | BasedataField | t_hies_entityencryptconf.fentityid | ✓ | hbp_entityobject |
| appname | 所属应用 | TextField | — |  |  |
| bizcloudname | 业务领域 | TextField | — |  |  |
| app | 所属应用 | BasedataField | t_hies_entityencryptconf.fappid |  | hbp_devportal_bizapp |
| cloudname | 所属业务领域 | BasedataPropField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hies_entityencryptconf（主表） | 14 |
| t_hies_entityencryptconf_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
