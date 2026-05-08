---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 198IF7HLNV46
app_number: lcs
app_name: 人力成本基础服务
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# lcs_coststru — 人力成本维度组合

**表单编码**: `lcs_coststru`  
**表单ID**: `19L88BAPICUN`  
**归属**: HR基础服务云 / 人力成本基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: lcs_coststru（人力成本维度组合） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_lcs_coststru` | 主表 · 26 列 |
| `t_lcs_coststruentry` | 分录表 · 4 列 |
| `t_lcs_coststrudetail` | 分录表 · 2 列 |
| `t_lcs_coststrudefdetail` | 分录表 · 2 列 |
| `t_lcs_coststru_l` | 多语言表 · 4 列 |
| `t_lcs_coststruentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_lcs_coststru.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_lcs_coststru_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_lcs_coststru.fstatus |  |  |
| creator | 创建人 | CreaterField | t_lcs_coststru.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_lcs_coststru.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_lcs_coststru.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_lcs_coststru.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_lcs_coststru.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_lcs_coststru.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_lcs_coststru.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_lcs_coststru.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_lcs_coststru.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_lcs_coststru.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_lcs_coststru.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_lcs_coststru.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_lcs_coststru.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_lcs_coststru_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_lcs_coststru_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_lcs_coststru.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_lcs_coststru.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_lcs_coststru.fdisabledate |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| displayname | 显示名称 | MuliLangTextField | t_lcs_coststruentry_l.fdisplayname |  |  |
| costbiztype | 业务类型 | BasedataField | t_lcs_coststru.fcostbiztypeid | ✓ | lcs_costbiztype |

## 实体: dimensionentry（核算维度） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| costdimension | 核算维度 | BasedataField | t_lcs_coststruentry.fcostdimensionid | ✓ | lcs_costdimension |
| basedatapropfield | 名称 | BasedataPropField | — |  |  |
| basedatapropfield1 | 编码 | BasedataPropField | — |  |  |
| dimensiondisplayname | 显示名称 | MuliLangTextField | — |  |  |
| isrequired | 是否必填 | CheckBoxField | t_lcs_coststruentry.fisrequired |  |  |
| storageset | 存储设置 | BasedataField | t_lcs_coststruentry.fstorageset | ✓ | lcs_segement |
| costbizobj | 业务成本对象 | BasedataField | t_lcs_coststrudefdetail.fcostbizobjid |  | lcs_costbizobj |
| adaptationrule | 适配规则 | TextAreaField | t_lcs_coststrudetail.fadaptationrule |  |  |
| defcostbizobj | 业务成本对象 | BasedataField | — |  | lcs_costbizobj |
| defaultrule | 默认规则 | TextAreaField | t_lcs_coststrudefdetail.fdefaultrule |  |  |
| curpage | 当前页数 | StepperField | — |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| initstatus | 初始化状态 | ComboField | — |  |  |
| initbatch | 初始化批次 | BigIntField | — |  |  |
| alldimensionname | 所有维度显示名称 | TextField | t_lcs_coststru.falldimensionname |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_lcs_coststru（主表） | 20 |
| t_lcs_coststru_l | 3 |
| t_lcs_coststrudefdetail | 2 |
| t_lcs_coststrudetail | 1 |
| t_lcs_coststruentry | 3 |
| t_lcs_coststruentry_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 13 |
