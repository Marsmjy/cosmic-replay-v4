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

# hrobs_appconfig — HR服务应用

**表单编码**: `hrobs_appconfig`  
**表单ID**: `2WMT/EC17I+8`  
**归属**: HR基础服务云 / HR运营基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hrobs_appconfig（HR服务应用） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hrobs_appconfig` | 主表 · 22 列 |
| `t_hrobs_appconfigdetail` | 分录表 · 8 列 |
| `t_hrobs_appconfig_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrobs_appconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hrobs_appconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hrobs_appconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrobs_appconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrobs_appconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrobs_appconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrobs_appconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrobs_appconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrobs_appconfig.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrobs_appconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrobs_appconfig_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrobs_appconfig.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrobs_appconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrobs_appconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrobs_appconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_hrobs_appconfig.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrobs_appconfig.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrobs_appconfig_l.foriname |  |  |
| appgroup | 应用分组 | BasedataField | t_hrobs_appconfig.fappgroupid | ✓ | hrobs_appgroup |
| appsource | 应用来源 | ComboField | t_hrobs_appconfig.fappsource | ✓ |  |
| systemicon | 应用系统图标 | TextField | t_hrobs_appconfig.fsystemicon |  |  |
| fcustomicon | 应用自定义图标 | PictureField | — |  |  |

## 实体: entryentity（应用配置详情） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| terminal | 服务处理终端 | ComboField | t_hrobs_appconfigdetail.fterminal |  |  |
| url | 跳转地址 | TextField | t_hrobs_appconfigdetail.furl |  |  |
| businessobjecttype | 对象类型 | ComboField | t_hrobs_appconfigdetail.fbusinessobjecttype |  |  |
| params | 应用参数 | TextAreaField | t_hrobs_appconfigdetail.fparams |  |  |
| params_tag | 应用参数 | TextAreaField | t_hrobs_appconfigdetail.fparams_tag |  |  |
| remark | 描述 | TextField | t_hrobs_appconfigdetail.fremark |  |  |
| form | 处理页面 | BasedataField | t_hrobs_appconfigdetail.fformid |  | bos_formmeta |
| allterminal | 所属终端 | MuliLangTextField | — |  |  |
| bizcloud | 所属云 | MuliLangTextField | — |  |  |
| bizapp | 所属应用 | MuliLangTextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrobs_appconfig（主表） | 17 |
| t_hrobs_appconfig_l | 4 |
| t_hrobs_appconfigdetail | 7 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 5 |
