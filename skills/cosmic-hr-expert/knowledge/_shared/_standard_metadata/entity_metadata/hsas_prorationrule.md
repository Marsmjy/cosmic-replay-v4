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

# hsas_prorationrule — 分段折算规则

**表单编码**: `hsas_prorationrule`  
**表单ID**: `13Q63/UOC/J7`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_prorationrule（分段折算规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_prorationrule` | 主表 · 42 列 |
| `t_hsas_prorationrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_prorationrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_prorationrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_prorationrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_prorationrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_prorationrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_prorationrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_prorationrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_prorationrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_prorationrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_prorationrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_prorationrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_prorationrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_prorationrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_prorationrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_prorationrule.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_hsas_prorationrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_prorationrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_prorationrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_prorationrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_prorationrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_prorationrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_prorationrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_prorationrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_prorationrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_prorationrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_prorationrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_prorationrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_prorationrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_prorationrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_prorationrule.fhisversion |  |  |
| numerator | 分子公式 | BasedataField | t_hsas_prorationrule.fnumeratorid | ✓ | hsas_formula |
| denominator | 分母公式 | BasedataField | t_hsas_prorationrule.fdenominatorid | ✓ | hsas_formula |
| dataprecision | 折算占比精度 | BasedataField | t_hsas_prorationrule.fdataprecisionid | ✓ | hsbs_dataprecision |
| dataround | 精度尾差处理 | BasedataField | t_hsas_prorationrule.fdataroundid | ✓ | hsbs_dataround |
| generalenname | 通用英文名 | TextField | t_hsas_prorationrule.fgeneralenname |  |  |
| vid | 当前数据历史版本(旧历史模型字段，时间仓促暂时不删) | BigIntField | t_hsas_prorationrule.fvid |  |  |
| mvid | 当前数据变更版本(旧历史模型字段，时间仓促暂时不删) | BigIntField | t_hsas_prorationrule.fmvid |  |  |
| minbsed | 最早生效日期(旧历史模型字段，时间仓促暂时不删) | DateField | t_hsas_prorationrule.fminbsed |  |  |
| country | 国家/地区 | BasedataField | t_hsas_prorationrule.fcountryid |  | bd_country |
| areatype | 国家/地区类型 | ComboField | t_hsas_prorationrule.fareatype | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_prorationrule（主表） | 37 |
| t_hsas_prorationrule_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
