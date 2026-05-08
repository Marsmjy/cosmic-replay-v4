---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 33EUPRZ1Q202
app_number: hcss
app_name: 员工薪酬服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcss_appliyreason — 收入证明用途

**表单编码**: `hcss_appliyreason`  
**表单ID**: `31Y8CUOFBHN6`  
**归属**: 薪酬福利云 / 员工薪酬服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcss_appliyreason（收入证明用途） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcss_proofreason` | 主表 · 23 列 |
| `t_hcss_proofreason_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcss_proofreason.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcss_proofreason_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcss_proofreason.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcss_proofreason.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcss_proofreason.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcss_proofreason.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcss_proofreason.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcss_proofreason.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcss_proofreason.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hcss_proofreason.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hcss_proofreason.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hcss_proofreason.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hcss_proofreason.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hcss_proofreason.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hcss_proofreason.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hcss_proofreason.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hcss_proofreason_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcss_proofreason_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcss_proofreason.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcss_proofreason.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcss_proofreason.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcss_proofreason（主表） | 18 |
| t_hcss_proofreason_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
