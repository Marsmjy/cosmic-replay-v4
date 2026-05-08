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

# pcs_costallocation — 分摊结构计算任务

**表单编码**: `pcs_costallocation`  
**表单ID**: `1H89X10TIF26`  
**归属**: 薪酬福利云 / 薪酬成本  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: pcs_costallocation（分摊结构计算任务） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_pcs_costallocation` | 主表 · 24 列 |
| `t_pcs_costallocation_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_pcs_costallocation.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_pcs_costallocation_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_pcs_costallocation.fstatus |  |  |
| creator | 创建人 | CreaterField | t_pcs_costallocation.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_pcs_costallocation.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_pcs_costallocation.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_pcs_costallocation.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_pcs_costallocation.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_pcs_costallocation.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_pcs_costallocation_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_pcs_costallocation_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_pcs_costallocation.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_pcs_costallocation.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_pcs_costallocation.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_pcs_costallocation.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| viewdetail | 查看图标 | MulComboField | t_pcs_costallocation.fviewdetail |  |  |
| caltask | 核算任务 | BasedataField | t_pcs_costallocation.fcaltaskid |  | hsas_calpayrolltask |
| calpayrolldate | 薪资所属年月 | BasedataPropField | — |  |  |
| salarystartdate | 薪资起始日期 | BasedataPropField | — |  |  |
| salaryenddate | 薪资结束日期 | BasedataPropField | — |  |  |
| caltaskpersonnum | 核算任务计薪人数 | IntegerField | t_pcs_costallocation.fcaltaskpersonnum |  |  |
| costtaskstatus | 成本任务状态 | ComboField | t_pcs_costallocation.fcosttaskstatus |  |  |
| costfilenum | 已生成分摊结构人数 | IntegerField | t_pcs_costallocation.fcostfilenum |  |  |
| period | 期间 | BasedataField | t_pcs_costallocation.fperiodid |  | hsbs_calperiod |
| hrorg | 算发薪管理组织 | OrgField | — |  | bos_org |
| costadapter | 人力成本维度方案 | BasedataField | t_pcs_costallocation.fcostadapterid |  | lcs_costadaption |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_pcs_costallocation（主表） | 19 |
| t_pcs_costallocation_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
