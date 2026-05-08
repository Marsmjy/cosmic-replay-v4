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

# hies_switchregistry — 新旧导入导出切换管理

**表单编码**: `hies_switchregistry`  
**表单ID**: `3YSLVH=AUI+6`  
**归属**: HR基础服务云 / HR导入导出管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hies_switchregistry（新旧导入导出切换管理） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hies_switchregistry` | 主表 · 24 列 |
| `t_hies_switchregistry_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hies_switchregistry.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hies_switchregistry_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hies_switchregistry.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hies_switchregistry.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hies_switchregistry.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hies_switchregistry.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hies_switchregistry.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hies_switchregistry.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hies_switchregistry.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hies_switchregistry_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hies_switchregistry_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hies_switchregistry.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hies_switchregistry.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hies_switchregistry.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hies_switchregistry.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hies_switchregistry.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| entity | 业务对象 | BasedataField | t_hies_switchregistry.fentityid | ✓ | bos_formmeta |
| bizapp | 所属应用 | BasedataField | — | ✓ | hbp_devportal_bizapp |
| oldop | 旧版按钮标识 | TextField | t_hies_switchregistry.foldop | ✓ |  |
| newop | 新版按钮标识 | TextField | t_hies_switchregistry.fnewop | ✓ |  |
| optype | 操作类型 | ComboField | t_hies_switchregistry.foptype | ✓ |  |
| permitem | 对应权限项 | TextField | t_hies_switchregistry.fpermitem |  |  |
| enablestatus | 状态 | ComboField | t_hies_switchregistry.fenablestatus |  |  |
| apistatus | 接口数据编辑状态 | ComboField | t_hies_switchregistry.fapistatus |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hies_switchregistry（主表） | 20 |
| t_hies_switchregistry_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
