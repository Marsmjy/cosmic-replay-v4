---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1ANC8T4UC434
app_number: pcs
app_name: 薪酬成本
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# pcs_splitbillrule — 分配单拆单规则

**表单编码**: `pcs_splitbillrule`  
**表单ID**: `1UNOSP2BTMYR`  
**归属**: 薪酬福利云 / 薪酬成本  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: pcs_splitbillrule（分配单拆单规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_pcs_splitbillrule` | 主表 · 21 列 |
| `t_pcs_splitbillrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_pcs_splitbillrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_pcs_splitbillrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_pcs_splitbillrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_pcs_splitbillrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_pcs_splitbillrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_pcs_splitbillrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_pcs_splitbillrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_pcs_splitbillrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_pcs_splitbillrule.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_pcs_splitbillrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_pcs_splitbillrule_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_pcs_splitbillrule.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_pcs_splitbillrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_pcs_splitbillrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_pcs_splitbillrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| costadapter | 人力成本维度方案 | BasedataField | t_pcs_splitbillrule.fcostadapterid | ✓ | lcs_costadaption |
| costdimensionmul | 人力成本维度 | MulBasedataField | — |  |  |
| org | 算发薪管理组织 | OrgField | t_pcs_splitbillrule.forgid | ✓ | bos_org |
| isaudit | 是否审核过 | CheckBoxField | t_pcs_splitbillrule.fisaudit |  |  |
| perioddatetype | 薪资期间 | ComboField | t_pcs_splitbillrule.fperioddatetype |  |  |
| postingdatesource | 记账日期取值来源 | ComboField | t_pcs_splitbillrule.fpostingdatesource |  |  |
| costdimension | 记账组织取值来源 | BasedataField | t_pcs_splitbillrule.fcostdimensionid |  | lcs_costdimension |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_pcs_splitbillrule（主表） | 18 |
| t_pcs_splitbillrule_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 5 |
