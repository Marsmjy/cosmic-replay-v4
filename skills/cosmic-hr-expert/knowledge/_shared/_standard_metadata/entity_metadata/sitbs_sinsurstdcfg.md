---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 17/+7RIW4SCJ
app_number: sitbs
app_name: 社保个税基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# sitbs_sinsurstdcfg — 险种参数设置

**表单编码**: `sitbs_sinsurstdcfg`  
**表单ID**: `2HSH3JM59WYE`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_sinsurstdcfg（险种参数设置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_sinsurstdcfg` | 主表 · 27 列 |
| `t_sitbs_sinsurstdcfgrent` | 分录表 · 6 列 |
| `t_sitbs_sinsurstdcfgdent` | 分录表 · 7 列 |
| `t_sitbs_sinsurstdcfg_l` | 多语言表 · 3 列 |
| `t_sitbs_sinsurstdcfgdent_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_sinsurstdcfg.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_sinsurstdcfg_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_sinsurstdcfg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_sinsurstdcfg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_sinsurstdcfg.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_sinsurstdcfg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_sinsurstdcfg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_sinsurstdcfg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_sinsurstdcfg.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_sinsurstdcfg_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_sinsurstdcfg_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_sitbs_sinsurstdcfg.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_sinsurstdcfg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_sinsurstdcfg.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_sinsurstdcfg.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_sitbs_sinsurstdcfg.fboid |  |  |
| iscurrentversion | 是否当前版本 | CheckBoxField | t_sitbs_sinsurstdcfg.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_sitbs_sinsurstdcfg.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_sitbs_sinsurstdcfg.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_sitbs_sinsurstdcfg.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_sitbs_sinsurstdcfg.fbsed |  |  |
| bsled | 失效日期 | DateField | t_sitbs_sinsurstdcfg.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_sitbs_sinsurstdcfg.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_sitbs_sinsurstdcfg.fhisversion |  |  |
| country | 国家/地区 | BasedataField | t_sitbs_sinsurstdcfg.fcountryid | ✓ | bd_country |
| insurtype | 险种 | BasedataField | t_sitbs_sinsurstdcfg.finsurtypeid | ✓ | sitbs_welfaretype |

## 实体: rentryentity（结果参数） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| insuritemnum | 险种项目编码 | BasedataPropField | — |  |  |
| insuritem | 险种项目名称 | BasedataField | t_sitbs_sinsurstdcfgrent.finsuritemid | ✓ | sitbs_insuranceitem |
| isuseroundtype | 维护舍位方式 | ComboField | t_sitbs_sinsurstdcfgrent.fisuseroundtype |  |  |
| datasrc | 数据来源 | ComboField | t_sitbs_sinsurstdcfgrent.fdatasrc |  |  |
| calrule | 计算规则 | ComboField | t_sitbs_sinsurstdcfgrent.fcalrule |  |  |
| entryboidren | 分录业务id | BigIntField | — |  |  |

## 实体: dentryentity（标准维度） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryboid | 分录业务id | BigIntField | t_sitbs_sinsurstdcfgdent.fentryboid |  |  |
| paramsrc | 数据来源 | ComboField | t_sitbs_sinsurstdcfgdent.fparamsrc |  |  |
| sinsurstddim | 标准维度名称 | BasedataField | t_sitbs_sinsurstdcfgdent.fsinsurstddimid |  | sitbs_sinsurstddim |
| srcfieldname | 数据来源字段 | BasedataPropField | — |  |  |
| srcentity | 数据来源实体 | BasedataPropField | — |  |  |
| stddimnumber | 标准维度编码 | BasedataPropField | — |  |  |
| ispayinmultiplearea | 允许多地缴纳 | CheckBoxField | t_sitbs_sinsurstdcfg.fispayinmultiplearea |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_sinsurstdcfg（主表） | 24 |
| t_sitbs_sinsurstdcfg_l | 3 |
| t_sitbs_sinsurstdcfgdent | 3 |
| t_sitbs_sinsurstdcfgrent | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
