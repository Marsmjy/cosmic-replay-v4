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

# pcs_costitemcfg — 薪酬项目成本设置

**表单编码**: `pcs_costitemcfg`  
**表单ID**: `1B64YSDTCJ0+`  
**归属**: 薪酬福利云 / 薪酬成本  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: pcs_costitemcfg（薪酬项目成本设置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_pcs_costcfg` | 主表 · 39 列 |
| `t_pcs_costcfgent` | 分录表 · 4 列 |
| `t_pcs_costcfgtypeent` | 分录表 · 4 列 |
| `t_pcs_costcfg_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 薪酬项目编码 | TextField | t_pcs_costcfg.fnumber |  |  |
| name | 薪酬项目名称 | MuliLangTextField | t_pcs_costcfg_l.fname |  |  |
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
| source | 薪酬项目 | BasedataField | t_pcs_costcfg.fsourceid |  | hsbs_salaryitem |
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
| salaryitemtype | 薪酬项目系统分类 | BasedataPropField | — |  |  |
| basedataid2 | 分组条件 | MulBasedataField | — |  |  |
| basedataid3 | 分组条件 | HRMulAdminOrgField | — |  |  |
| basedataid4 | 分组条件 | MulBasedataField | — |  |  |
| basedataid5 | 分组条件 | MulBasedataField | — |  |  |
| basedataid8 | 分组条件 | HRMulAdminOrgField | — |  |  |
| basedataid9 | 分组条件 | HRMulAdminOrgField | — |  |  |
| basedataid10 | 分组条件 | HRMulPositionField | — |  |  |
| basedataid11 | 分组条件 | MulBasedataField | — |  |  |
| qualityname | 成本资格名称 | MuliLangTextField | t_pcs_costcfg_l.fqualityname |  |  |
| qualitynumber | 成本资格编码 | TextField | t_pcs_costcfg.fqualitynumber |  |  |
| basedataid1 | 分组条件 | MulBasedataField | — |  |  |
| basedataid6 | 分组条件 | CheckBoxField | — |  |  |
| qualitydim | 资格分组维度 | BasedataField | t_pcs_costcfg.fqualitydimid |  | pcs_costqualitydim |
| parentid | 上级节点ID | BigIntField | t_pcs_costcfg.fparentid |  |  |
| salaryitemname | 薪酬项目名称 | BasedataPropField | t_pcs_costcfg.fsalaryitemname |  |  |
| salaryitemnumber | 薪酬项目编码 | BasedataPropField | t_pcs_costcfg.fsalaryitemnumber |  |  |
| groupvalue | 分组维度具体基础资料ID | TextField | t_pcs_costcfg.fgroupvalue |  |  |
| basedataid7 | 分组条件 | MulBasedataField | — |  |  |
| iscostcfgtype | 是否有优先成本设置 | CheckBoxField | t_pcs_costcfg.fiscostcfgtype |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_pcs_costcfg（主表） | 34 |
| t_pcs_costcfg_l | 4 |
| t_pcs_costcfgent | 2 |
| t_pcs_costcfgtypeent | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 20 |
