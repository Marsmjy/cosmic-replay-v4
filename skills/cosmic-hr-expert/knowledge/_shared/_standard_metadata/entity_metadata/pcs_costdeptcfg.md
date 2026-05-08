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

# pcs_costdeptcfg — 组织薪酬成本设置

**表单编码**: `pcs_costdeptcfg`  
**表单ID**: `1B6P842KIBC+`  
**归属**: 薪酬福利云 / 薪酬成本  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: pcs_costdeptcfg（组织薪酬成本设置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_pcs_costcfg` | 主表 · 32 列 |
| `t_pcs_costcfgent` | 分录表 · 4 列 |
| `t_pcs_costcfgtypeent` | 分录表 · 4 列 |
| `t_pcs_costcfg_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_pcs_costcfg.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_pcs_costcfg_l.fname |  |  |
| status | 数据状态 | BillStatusField | t_pcs_costcfg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_pcs_costcfg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_pcs_costcfg.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_pcs_costcfg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_pcs_costcfg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_pcs_costcfg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_pcs_costcfg.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_pcs_costcfg_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_pcs_costcfg_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_pcs_costcfg.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_pcs_costcfg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_pcs_costcfg.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_pcs_costcfg.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_pcs_costcfg.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_pcs_costcfg.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_pcs_costcfg.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_pcs_costcfg.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_pcs_costcfg.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_pcs_costcfg.fbsed |  |  |
| bsled | 失效日期 | DateField | t_pcs_costcfg.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_pcs_costcfg.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_pcs_costcfg.fhisversion |  |  |

## 实体: costcfgentryentity（成本设置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| coststrucfgvalue | 人力成本维度组合值 | TextField | t_pcs_costcfgent.fcoststrucfgvalue |  |  |
| costsegstore | 分段存储信息 | BasedataField | t_pcs_costcfgtypeent.fcostsegstoreid |  | pcs_costsegstore |
| entryboid | 分录业务ID | BigIntField | t_pcs_costcfgent.fentryboid |  |  |
| calcostproportion | 百分比(%) | DecimalField | — |  |  |
| sourcetype | 人力成本业务对象 | BasedataField | t_pcs_costcfg.fsourcetypeid |  | lcs_costbizobj |
| source | 行政组织 | BasedataField | t_pcs_costcfg.fsourceid |  | haos_adminorghrf7 |
| cardtypename | 卡片类型名称 | TextField | t_pcs_costcfg.fcardtypename |  |  |
| createorg | 算发薪管理组织 | OrgField | t_pcs_costcfg.fcreateorgid | ✓ | bos_org |
| costadapter | 人力成本维度方案 | BasedataField | t_pcs_costcfg.fcostadapterid |  | lcs_costadaption |
| coststru | 多维核算结构 | BasedataField | t_pcs_costcfg.fcoststruid |  | lcs_coststru |
| alldimensionname | 人力成本维度组合 | BasedataPropField | — |  |  |

## 实体: costcfgtypeentryentity（成本类型设置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| coststrutypecfgvalue | 人力成本维度组合值 | TextField | t_pcs_costcfgtypeent.fcoststrutypecfgvalue |  |  |
| costtypesegstore | 分段存储类型信息 | BasedataField | — |  | pcs_costsegstore |
| hidetypekeyandvalue | 隐藏成本类型维度组合值 | TextField | — |  |  |
| entryboidtype | 分录业务ID | BigIntField | t_pcs_costcfgtypeent.fentryboidtype |  |  |
| costcfgtypeproportion | 百分比(%) | DecimalField | t_pcs_costcfgtypeent.fcostcfgtypeproportion |  |  |
| adminorgtypename | 行政组织名称 | BasedataPropField | — |  |  |
| adminorgtypenumber | 行政组织编码 | BasedataPropField | — |  |  |
| adminorgtypesim | 行政组织简称 | BasedataPropField | — |  |  |
| admindepttype | 行政组织类型 | BasedataPropField | — |  |  |
| iscostcfgtype | 是否有兜底成本设置 | CheckBoxField | t_pcs_costcfg.fiscostcfgtype |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_pcs_costcfg（主表） | 28 |
| t_pcs_costcfg_l | 3 |
| t_pcs_costcfgent | 2 |
| t_pcs_costcfgtypeent | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 12 |
