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

# hsbs_resultfetchconfig — 薪酬结果取数配置

**表单编码**: `hsbs_resultfetchconfig`  
**表单ID**: `24KW==KOBCBO`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_resultfetchconfig（薪酬结果取数配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_resultfetchconfig` | 主表 · 28 列 |
| `t_hsbs_resultitementry` | 分录表 · 10 列 |
| `t_hsbs_resultfetchconfig_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_resultfetchconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_resultfetchconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_resultfetchconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_resultfetchconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_resultfetchconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_resultfetchconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_resultfetchconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_resultfetchconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_resultfetchconfig.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_resultfetchconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_resultfetchconfig_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_resultfetchconfig.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_resultfetchconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_resultfetchconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_resultfetchconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsbs_resultfetchconfig.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsbs_resultfetchconfig.fcountryid |  | bd_country |
| selectlevel | 查询对象级别 | ComboField | t_hsbs_resultfetchconfig.fselectlevel | ✓ |  |

## 实体: calitemmatchentry（计算项目映射取数项目） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| itemtype | 项目类型 | ComboField | t_hsbs_resultitementry.fitemtype | ✓ |  |
| matchfetchitem | 取数项目 | BasedataField | t_hsbs_resultitementry.fmatchfetchitemid | ✓ | hsbs_fetchitem |
| fetchitementry | 类型-取数项目 | BasedataField | — |  | hsbs_fetchitem |
| salaryitementry | 类型-薪酬项目 | BasedataField | — |  | hsbs_salaryitem |
| supportitementry | 类型-支持项目 | BasedataField | — |  | hsbs_supportitem |
| bizitementry | 类型-业务项目 | BasedataField | — |  | hsbs_bizitem |
| itemdatatype | 数据类型 | ComboField | t_hsbs_resultitementry.fitemdatatype |  |  |
| handlestrategy | 数据处理策略 | ComboField | t_hsbs_resultitementry.fhandlestrategy |  |  |
| strategy | 数据处理策略 | TextField | — | ✓ |  |
| currencytype | 币种取数策略 | ComboField | t_hsbs_resultitementry.fcurrencytype |  |  |
| scenelimit | 薪资核算场景限制 | ComboField | t_hsbs_resultfetchconfig.fscenelimit |  |  |
| calscene | 指定薪资核算场景 | MulBasedataField | — |  |  |
| datematch | 日期匹配依据 | ComboField | t_hsbs_resultfetchconfig.fdatematch |  |  |
| startitemcode | 取数起始日期唯一编码 | TextField | t_hsbs_resultfetchconfig.fstartitemcode |  |  |
| enditemcode | 取数截止日期唯一编码 | TextField | t_hsbs_resultfetchconfig.fenditemcode |  |  |
| datematchmethod | 日期匹配方法 | ComboField | t_hsbs_resultfetchconfig.fdatematchmethod | ✓ |  |
| preperiodcount | 前序期间个数 | IntegerField | t_hsbs_resultfetchconfig.fpreperiodcount |  |  |
| startitem | 取数起始日期 | BasedataField | t_hsbs_resultfetchconfig.fstartitemid |  | hsbs_fetchitem |
| enditem | 取数截止日期 | BasedataField | t_hsbs_resultfetchconfig.fenditemid |  | hsbs_fetchitem |
| iscontainretroresult | 包含回溯计算结果 | CheckBoxField | t_hsbs_resultfetchconfig.fiscontainretroresult |  |  |
| iscontainretrovalue | 包含回溯结转值 | CheckBoxField | t_hsbs_resultfetchconfig.fiscontainretrovalue |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_resultfetchconfig（主表） | 25 |
| t_hsbs_resultfetchconfig_l | 3 |
| t_hsbs_resultitementry | 5 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
