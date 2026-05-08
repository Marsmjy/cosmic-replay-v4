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

# hsbs_sinsbsfetchcfg — 社保基数取数配置

**表单编码**: `hsbs_sinsbsfetchcfg`  
**表单ID**: `4/2=IM4A+2N+`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_sinsbsfetchcfg（社保基数取数配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_sinsbsftcfg` | 主表 · 38 列 |
| `t_hsbs_sinsbsftcfg_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_sinsbsftcfg.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_sinsbsftcfg_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_sinsbsftcfg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_sinsbsftcfg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_sinsbsftcfg.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_sinsbsftcfg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_sinsbsftcfg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_sinsbsftcfg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_sinsbsftcfg.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_sinsbsftcfg.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_sinsbsftcfg.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_sinsbsftcfg.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_sinsbsftcfg.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_sinsbsftcfg.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_sinsbsftcfg.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsbs_sinsbsftcfg.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_sinsbsftcfg_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_sinsbsftcfg_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_sinsbsftcfg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_sinsbsftcfg.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_sinsbsftcfg.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsbs_sinsbsftcfg.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsbs_sinsbsftcfg.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsbs_sinsbsftcfg.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsbs_sinsbsftcfg.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsbs_sinsbsftcfg.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsbs_sinsbsftcfg.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsbs_sinsbsftcfg.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsbs_sinsbsftcfg.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsbs_sinsbsftcfg.fhisversion |  |  |
| country | 国家/地区 | BasedataField | t_hsbs_sinsbsftcfg.fcountryid | ✓ | bd_country |
| fetchsrc | 取值来源 | ComboField | t_hsbs_sinsbsftcfg.ffetchsrc | ✓ |  |
| querytime | 查询时间 | ComboField | t_hsbs_sinsbsftcfg.fquerytime | ✓ |  |
| accumulator | 薪资结果累加器 | BasedataField | t_hsbs_sinsbsftcfg.faccumulatorid | ✓ | hsas_accumulator |
| standarditem | 定调薪项目 | BasedataField | t_hsbs_sinsbsftcfg.fstandarditemid | ✓ | hsbs_standarditem |
| basefetchitem | 基数取值项目 | BigIntField | — |  |  |
| basefetchitemname | 基数取值项目 | TextField | — | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_sinsbsftcfg（主表） | 32 |
| t_hsbs_sinsbsftcfg_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
