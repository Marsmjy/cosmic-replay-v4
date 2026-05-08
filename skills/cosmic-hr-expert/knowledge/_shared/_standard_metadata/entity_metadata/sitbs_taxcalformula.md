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

# sitbs_taxcalformula — 计算公式

**表单编码**: `sitbs_taxcalformula`  
**表单ID**: `28976E9Y=2QJ`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_taxcalformula（计算公式） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_taxcalformula` | 主表 · 37 列 |
| `t_sitbs_taxcalformula_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_taxcalformula.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_taxcalformula_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_taxcalformula.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_taxcalformula.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_taxcalformula.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_taxcalformula.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_taxcalformula.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_taxcalformula.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_taxcalformula.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_sitbs_taxcalformula.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_sitbs_taxcalformula.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_sitbs_taxcalformula.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_sitbs_taxcalformula.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_sitbs_taxcalformula.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_sitbs_taxcalformula.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_sitbs_taxcalformula.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_taxcalformula_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_taxcalformula_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_taxcalformula.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_taxcalformula.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_taxcalformula.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_sitbs_taxcalformula.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_sitbs_taxcalformula.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_sitbs_taxcalformula.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_sitbs_taxcalformula.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_sitbs_taxcalformula.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_sitbs_taxcalformula.fbsed |  |  |
| bsled | 失效日期 | DateField | t_sitbs_taxcalformula.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_sitbs_taxcalformula.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_sitbs_taxcalformula.fhisversion |  |  |
| resultitem | 个税项目 | BasedataField | t_sitbs_taxcalformula.fresultitemid | ✓ | sitbs_taxcalresult |
| originalexp | 公式内容 | TextAreaField | — |  |  |
| executeexp | 可执行表达式 | TextAreaField | — |  |  |
| dependentfunc | 依赖的函数Unicode集合 | TextAreaField | — |  |  |
| isdraft | 是否为草稿 | CheckBoxField | t_sitbs_taxcalformula.fisdraft |  |  |
| dependentcalitem | 依赖的计算项目Unicode集合 | TextAreaField | — |  |  |
| dependentcustitem | 依赖的自定义项目Unicode集合 | TextAreaField | — |  |  |
| dependentcalitemforfunc | 函数依赖计算项目Unicode集合 | TextAreaField | — |  |  |
| resultitemcategory | 计算结果项目分类 | TextField | — |  |  |
| resultitemuniquecode | 计算结果唯一编码 | TextField | — |  |  |
| resultitemdatatype | 计算结果数据类型 | TextField | — |  |  |
| resultitemdatalength | 计算结果数据长度 | IntegerField | — |  |  |
| resultitemscale | 计算结果数据精度 | IntegerField | — |  |  |
| uniquecodeexp | 关键字表达式 | TextAreaField | — |  |  |
| dependentcalitemfordg | 数据分级依赖的计算项目Unicode集合 | TextAreaField | — |  |  |
| dependentdatagrade | 依赖的数据分级Unicode集合 | TextAreaField | — |  |  |
| dependentbasedata | 依赖的基础资料UniqueCode集合 | TextField | — |  |  |
| dependentenum | 依赖的枚举UniqueCode集合 | TextField | — |  |  |
| exportitem | 额外输出的项目Unicode集合 | TextField | — |  |  |
| resultitemid | 结果参数 | TextField | t_sitbs_taxcalformula.fresultitemid |  |  |
| countrytype | 国家/地区类型 | ComboField | t_sitbs_taxcalformula.fcountrytype | ✓ |  |
| country | 国家/地区 | BasedataField | t_sitbs_taxcalformula.fcountryid | ✓ | bd_country |
| taxcategory | 个税种类 | BasedataField | t_sitbs_taxcalformula.ftaxcategoryid | ✓ | sitbs_taxcategory |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_taxcalformula（主表） | 33 |
| t_sitbs_taxcalformula_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 23 |
