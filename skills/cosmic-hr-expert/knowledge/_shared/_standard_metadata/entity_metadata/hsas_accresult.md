---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_accresult — 累加结果

**表单编码**: `hsas_accresult`  
**表单ID**: `2+/PECO9EVSE`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_accresult（累加结果） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_accresult` | 主表 · 21 列 |
| `t_hsas_accresultadjust` | 分录表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hsas_accresult.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hsas_accresult.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hsas_accresult.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hsas_accresult.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| salaryfile | 档案编号 | BasedataField | t_hsas_accresult.fsalaryfileid |  | hsas_salaryfile |
| acc | 累加器 | BasedataField | t_hsas_accresult.faccid | ✓ | hsas_accumulator |
| instancenum | 累加实例号 | IntegerField | t_hsas_accresult.finstancenum | ✓ |  |
| startdate | 累加开始日期 | DateField | t_hsas_accresult.fstartdate | ✓ |  |
| enddate | 累加结束日期 | DateField | t_hsas_accresult.fenddate | ✓ |  |
| dimension1 | 累加维度1 | TextField | — |  |  |
| dimension2 | 累加维度2 | TextField | — |  |  |
| dimension3 | 累加维度3 | TextField | — |  |  |
| dimension4 | 累加维度4 | TextField | — |  |  |
| dimension5 | 累加维度5 | TextField | — |  |  |
| currency | 币种 | CurrencyField | — |  | bd_currency |
| resultvalue | 累加结果值 | DecimalField | t_hsas_accresult.fresultvalue | ✓ |  |
| employee | 计薪人员 | BasedataField | t_hsas_accresult.femployeeid |  | hsbs_employee |

## 实体: adjustentry（调整累加结果） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| adjustamount | 调整值 | DecimalField | t_hsas_accresultadjust.fadjustamount | ✓ |  |
| adjustdescript | 调整原因 | TextField | t_hsas_accresultadjust.fadjustdescript | ✓ |  |
| adjustperson | 调整人 | UserField | t_hsas_accresultadjust.fadjustpersonid |  | bos_user |
| adjusttime | 调整时间 | DateTimeField | t_hsas_accresultadjust.fadjusttime |  |  |
| adjustpersonnumber | 调整人工号 | BasedataPropField | — |  |  |
| isadjust | 已调整 | ComboField | t_hsas_accresult.fisadjust |  |  |
| periodtype | 累加频度 | BasedataPropField | — |  |  |
| bsedstrategy | 累加生效策略 | BasedataPropField | — |  |  |
| accdimension | 累加维度 | BasedataPropField | — |  |  |
| showresultval | 累加结果值 | TextField | t_hsas_accresult.fshowresultval |  |  |
| calperiodid | 期间信息ID | BigIntField | t_hsas_accresult.fcalperiodid |  |  |
| globalperson | 全球员工ID | BigIntField | — |  |  |

## 实体: accdetailentry（累加明细单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| filenumber | 档案编号 | TextField | — |  |  |
| caltask | 核算任务名称 | BasedataField | — |  | hsas_calpayrolltask |
| caltasknumber | 核算任务编码 | BasedataPropField | — |  |  |
| period | 期间 | BasedataPropField | — |  |  |
| periodstartdate | 期间开始日期 | BasedataPropField | — |  |  |
| periodenddate | 期间结束日期 | BasedataPropField | — |  |  |
| periodpaydate | 预计支付日期 | BasedataPropField | — |  |  |
| accdetailcurrency | 币种 | CurrencyField | — |  | bd_currency |
| initvalue | 初始值 | TextField | — |  |  |
| currentvalue | 当前值 | TextField | — |  |  |
| accdetailresultval | 结果值 | TextField | — |  |  |
| accdetailidex | 顺序号 | IntegerField | — |  |  |
| option | 操作 | TextField | — |  |  |
| calpersonid | 核算名单ID | BigIntField | — |  |  |
| accdetailid | 累加明细ID | BigIntField | — |  |  |
| scale | 精度 | IntegerField | — |  |  |
| empnumber | 工号 | TextField | — |  |  |
| name | 人员姓名 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_accresult（主表） | 14 |
| t_hsas_accresultadjust | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 30 |
