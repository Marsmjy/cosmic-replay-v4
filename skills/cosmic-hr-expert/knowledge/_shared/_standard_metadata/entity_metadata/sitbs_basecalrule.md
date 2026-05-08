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

# sitbs_basecalrule — 基数计算规则

**表单编码**: `sitbs_basecalrule`  
**表单ID**: `4/2OPRLEK022`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_basecalrule（基数计算规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_basecalrule` | 主表 · 33 列 |
| `t_sitbs_basecalruleent` | 分录表 · 3 列 |
| `t_sitbs_basecalrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_basecalrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_basecalrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_basecalrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_basecalrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_basecalrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_basecalrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_basecalrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_basecalrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_basecalrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_sitbs_basecalrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_sitbs_basecalrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_sitbs_basecalrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_sitbs_basecalrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_sitbs_basecalrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_sitbs_basecalrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_sitbs_basecalrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_basecalrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_basecalrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_basecalrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_basecalrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_basecalrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_sitbs_basecalrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_sitbs_basecalrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_sitbs_basecalrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_sitbs_basecalrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_sitbs_basecalrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_sitbs_basecalrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_sitbs_basecalrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_sitbs_basecalrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_sitbs_basecalrule.fhisversion |  |  |
| country | 国家/地区 | BasedataField | t_sitbs_basecalrule.fcountryid | ✓ | bd_country |

## 实体: entryentity（计算规则设置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| insuritem | 险种项目 | BasedataField | t_sitbs_basecalruleent.finsuritemid | ✓ | sitbs_insuranceitem |
| entryboid | 分录业务id | BigIntField | t_sitbs_basecalruleent.fentryboid |  |  |
| calformula | 计算公式 | BasedataField | t_sitbs_basecalruleent.fcalformulaid | ✓ | sitbs_sinsurformula |
| fseq | 计算顺序 | IntegerField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_basecalrule（主表） | 28 |
| t_sitbs_basecalrule_l | 3 |
| t_sitbs_basecalruleent | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
