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

# hrptmc_analyseobject — 分析对象

**表单编码**: `hrptmc_analyseobject`  
**表单ID**: `2URYQLRQPLA1`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hrptmc_analyseobject（分析对象） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hrptmc_analysisobject` | 主表 · 20 列 |
| `t_hrptmc_analysisobject_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_analysisobject.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_analysisobject.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_analysisobject.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_analysisobject.fmodifytime |  |  |
| description | 对象描述 | MuliLangTextField | t_hrptmc_analysisobject_l.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| number | 对象编码 | TextField | t_hrptmc_analysisobject.fnumber | ✓ |  |
| name | 对象名称 | MuliLangTextField | t_hrptmc_analysisobject_l.fname | ✓ |  |
| datafilter | 数据过滤规则 | TextField | t_hrptmc_analysisobject.fdatafilter |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrptmc_analysisobject.fissyspreset |  |  |
| storeentity | 落地实体 | TextField | t_hrptmc_analysisobject.fstoreentity |  |  |
| isv | 开发商标识 | TextField | t_hrptmc_analysisobject.fisv |  |  |
| version | 版本号 | IntegerField | t_hrptmc_analysisobject.fversion |  |  |
| enable | 状态 | BillStatusField | t_hrptmc_analysisobject.fenable |  |  |
| queryscheme | 取数方案 | ComboField | t_hrptmc_analysisobject.fqueryscheme |  |  |
| isvirtualentity | 是否虚实体对象 | CheckBoxField | t_hrptmc_analysisobject.fisvirtualentity |  |  |
| pivotdimval | 转置维度值 | TextField | t_hrptmc_analysisobject.fpivotdimval |  |  |
| pivotindex | 被转置的指标 | TextField | t_hrptmc_analysisobject.fpivotindex |  |  |
| sidebar | 侧边栏顺序 | TextField | t_hrptmc_analysisobject.fsidebar |  |  |
| pivotdim | 需要转置的列 | ComboField | t_hrptmc_analysisobject.fpivotdim |  |  |
| objecttype | 对象类型 | ComboField | t_hrptmc_analysisobject.fobjecttype |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_analysisobject（主表） | 18 |
| t_hrptmc_analysisobject_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
