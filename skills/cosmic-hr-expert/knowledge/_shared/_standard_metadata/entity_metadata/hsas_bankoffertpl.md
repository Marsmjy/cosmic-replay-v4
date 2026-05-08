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

# hsas_bankoffertpl — 银行报盘模板

**表单编码**: `hsas_bankoffertpl`  
**表单ID**: `1JPJ71LIATQJ`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_bankoffertpl（银行报盘模板） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_bankoffertpl` | 主表 · 37 列 |
| `t_hsas_bankofferetent` | 分录表 · 4 列 |
| `t_hsas_bankofferdent` | 分录表 · 11 列 |
| `t_hsas_bankofferhcent` | 分录表 · 7 列 |
| `t_hsas_bankoffertpl_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_bankoffertpl.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_bankoffertpl_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_bankoffertpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_bankoffertpl.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_bankoffertpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_bankoffertpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_bankoffertpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_bankoffertpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_bankoffertpl.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_bankoffertpl_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_bankoffertpl_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_bankoffertpl.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_bankoffertpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_bankoffertpl.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_bankoffertpl.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_bankoffertpl.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_bankoffertpl.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_bankoffertpl.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsas_bankoffertpl.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_bankoffertpl.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_bankoffertpl.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_bankoffertpl.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_bankoffertpl.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_bankoffertpl.fhisversion |  |  |
| filetype | 文件类型 | ComboField | t_hsas_bankoffertpl.ffiletype | ✓ |  |
| separator | 分隔符 | TextField | — |  |  |
| isshowseq | 序号列 | CheckBoxField | t_hsas_bankoffertpl.fisshowseq |  |  |
| limitcontent | 文件大小限制方式 | ComboField | t_hsas_bankoffertpl.flimitcontent |  |  |

## 实体: exceltitleent（excel表头） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| headrow | 表头行 | TextField | t_hsas_bankofferetent.fheadrow |  |  |
| isempty | 是否为空 | CheckBoxField | t_hsas_bankofferetent.fisempty | ✓ |  |
| headcontent | 表头内容 | TextField | t_hsas_bankofferetent.fheadcontent |  |  |
| entryboid | 分录BOID | BigIntField | t_hsas_bankofferhcent.fentryboid |  |  |
| headtype | 表头类型 | ComboField | t_hsas_bankofferhcent.fheadtype | ✓ |  |
| subheadcontent | 表头内容 | TextField | t_hsas_bankofferhcent.fsubheadcontent | ✓ |  |
| fieldshowtype | 字段显示格式 | ComboField | t_hsas_bankofferhcent.ffieldshowtype |  |  |
| alignment | 对齐方式 | ComboField | t_hsas_bankofferhcent.falignment | ✓ |  |
| collocation | 列位置 | TextField | t_hsas_bankofferhcent.fcollocation | ✓ |  |
| selectfield | 选择字段 | TextField | t_hsas_bankofferhcent.fselectfield |  |  |
| entryboiddetail | 分录BOID | BigIntField | — |  |  |

## 实体: detailent（报盘明细） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldsource | 字段来源 | ComboField | t_hsas_bankofferdent.ffieldsource | ✓ |  |
| fieldvalue | 字段取值 | TextField | t_hsas_bankofferdent.ffieldvalue |  |  |
| fieldselect | 选择字段 | TextField | t_hsas_bankofferdent.ffieldselect |  |  |
| fieldlength | 字段长度 | IntegerField | t_hsas_bankofferdent.ffieldlength |  |  |
| fixway | 字段长度不足补齐方式 | ComboField | t_hsas_bankofferdent.ffixway |  |  |
| fixcontent | 补齐内容 | TextField | t_hsas_bankofferdent.ffixcontent |  |  |
| isshowthousandth | 显示千分位 | CheckBoxField | t_hsas_bankofferdent.fisshowthousandth |  |  |
| fieldname | 字段名称 | TextField | t_hsas_bankofferdent.ffieldname | ✓ |  |
| isnull | 是否允许为空 | CheckBoxField | t_hsas_bankofferdent.fisnull |  |  |
| entryboid2 | 分录BOID | BigIntField | — |  |  |
| fieldquerytable | 选择字段所属查询配置 | TextField | t_hsas_bankofferdent.ffieldquerytable |  |  |
| limitline | 单个文件行数上限 | IntegerField | t_hsas_bankoffertpl.flimitline |  |  |
| txthead | txt表头内容 | TextAreaField | — |  |  |
| limitamount | 单个文件金额上限 | DecimalField | t_hsas_bankoffertpl.flimitamount |  |  |
| separatedropdown | 间隔方式 | ComboField | — |  |  |
| separateways | 间隔方式 | TextField | — |  |  |
| countrytype | 国家/地区类型 | ComboField | t_hsas_bankoffertpl.fcountrytype | ✓ |  |
| countryid | 国家/地区 | BasedataField | t_hsas_bankoffertpl.fcountryid |  | bd_country |
| banktype | 银行类型 | ComboField | t_hsas_bankoffertpl.fbanktype | ✓ |  |
| bankcgsettingid | 指定银行 | BasedataField | t_hsas_bankoffertpl.fbankcgsettingid |  | bd_bankcgsetting |
| currencyid | 适用币种 | BasedataField | t_hsas_bankoffertpl.fcurrencyid | ✓ | bd_currency |
| filename | 文件名 | TextField | t_hsas_bankoffertpl.ffilename | ✓ |  |
| isshowhead | 显示表头 | CheckBoxField | t_hsas_bankoffertpl.fisshowhead |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_bankoffertpl（主表） | 33 |
| t_hsas_bankofferdent | 10 |
| t_hsas_bankofferetent | 3 |
| t_hsas_bankofferhcent | 7 |
| t_hsas_bankoffertpl_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
