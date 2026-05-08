---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHLXNRR9KPZ
app_number: hsbs
app_name: 薪酬基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsbs_attintegmapscmnew — 考勤集成映射方案

**表单编码**: `hsbs_attintegmapscmnew`  
**表单ID**: `3W2S1XS6ZZ7Z`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_attintegmapscmnew（考勤集成映射方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_attintegmapscmnew` | 主表 · 26 列 |
| `t_hsbs_attsumitement` | 分录表 · 3 列 |
| `t_hsbs_attdetailitement` | 分录表 · 3 列 |
| `t_hsbs_attintegmapscmnew_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_attintegmapscmnew.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_attintegmapscmnew_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_attintegmapscmnew.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_attintegmapscmnew.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_attintegmapscmnew.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_attintegmapscmnew.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_attintegmapscmnew.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_attintegmapscmnew.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_attintegmapscmnew.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_attintegmapscmnew_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_attintegmapscmnew_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_attintegmapscmnew.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_attintegmapscmnew.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_attintegmapscmnew.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_attintegmapscmnew.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsbs_attintegmapscmnew.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsbs_attintegmapscmnew.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsbs_attintegmapscmnew.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsbs_attintegmapscmnew.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsbs_attintegmapscmnew.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsbs_attintegmapscmnew.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsbs_attintegmapscmnew.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsbs_attintegmapscmnew.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsbs_attintegmapscmnew.fhisversion |  |  |
| org | 算发薪管理组织 | OrgField | t_hsbs_attintegmapscmnew.forgid | ✓ | bos_org |
| itemscope | 集成项目范围 | ComboField | t_hsbs_attintegmapscmnew.fitemscope | ✓ |  |

## 实体: sumentryentity（汇总项目映射） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sumattitemnumber | 考勤项目编码 | BasedataPropField | — |  |  |
| sumattitem | 考勤项目名称 | BasedataField | — | ✓ | wtbd_attitem |
| sumattitemdatatype | 数据类型 | BasedataPropField | — |  |  |
| sumattbizitemname | 考勤业务项目名称 | BasedataPropField | — |  |  |
| sumattbizitem | 考勤业务项目编码 | BasedataField | — | ✓ | hsbs_attbizitem |
| sumdatatype | 数据类型 | BasedataPropField | — |  |  |
| entryboidsum | 汇总项目分录boid | BigIntField | — |  |  |

## 实体: detailentryentity（明细项目映射） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| detailattitemnumber | 考勤项目编码 | BasedataPropField | — |  |  |
| detailattitem | 考勤项目名称 | BasedataField | — | ✓ | wtbd_attitem |
| detailattitemdatatype | 数据类型 | BasedataPropField | — |  |  |
| detailattbizitemname | 考勤业务项目名称 | BasedataPropField | — |  |  |
| detailattbizitem | 考勤业务项目编码 | BasedataField | — | ✓ | hsbs_attbizitem |
| detaildatatype | 数据类型 | BasedataPropField | — |  |  |
| entryboiddetail | 汇总项目分录boid | BigIntField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_attintegmapscmnew（主表） | 23 |
| t_hsbs_attintegmapscmnew_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 18 |
