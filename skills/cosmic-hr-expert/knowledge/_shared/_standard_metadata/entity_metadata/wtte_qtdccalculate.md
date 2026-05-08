---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1C8H4/N38LCY
app_number: wtte
app_name: 考勤核算
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtte_qtdccalculate — 定额扣减

**表单编码**: `wtte_qtdccalculate`  
**表单ID**: `3UDY4D9CXC=0`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_qtdccalculate（定额扣减） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_qtdccalculate` | 主表 · 16 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtte_qtdccalculate.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtte_qtdccalculate.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtte_qtdccalculate.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtte_qtdccalculate.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| version | 任务号 | TextField | t_wtte_qtdccalculate.fversion |  |  |
| begindate | 核算开始日期 | DateField | t_wtte_qtdccalculate.fbegindate |  |  |
| enddate | 核算结束日期 | DateField | t_wtte_qtdccalculate.fenddate |  |  |
| taskid | 核算任务 | BasedataField | t_wtte_qtdccalculate.ftaskid |  | wtte_qttietask |
| savestep | 存储核算步骤 | CheckBoxField | t_wtte_qtdccalculate.fsavestep |  |  |
| cost | 耗时 | TextField | t_wtte_qtdccalculate.fcost |  |  |
| totaskpage | 占位 | TextField | — |  |  |
| calstatus | 核算状态 | ComboField | t_wtte_qtdccalculate.fcalstatus |  |  |
| tietaskstatus | 任务状态 | ComboField | t_wtte_qtdccalculate.ftietaskstatus |  |  |
| planid | 核算方案 | BasedataField | t_wtte_qtdccalculate.fplanid |  | wtp_accountplan |
| org | 考勤管理组织 | OrgField | t_wtte_qtdccalculate.forgid |  | bos_org |
| dyqttype | 动态生成-定额类型 | MulBasedataField | — |  |  |
| qttype | 固定生成-定额类型 | MulBasedataField | — |  |  |
| tieplanid | 单据核算方案 | BasedataField | t_wtte_qtdccalculate.ftieplanid |  | wtp_accountplan |
| computingbillid | 核算单据ID | BigIntField | t_wtte_qtdccalculate.fcomputingbillid |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_qtdccalculate（主表） | 16 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
