---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FOLRY18YW
app_number: wtp
app_name: 工时假勤规则
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtp_formulaset — 考勤公式配置

**表单编码**: `wtp_formulaset`  
**表单ID**: `3QREN0+AHH6+`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_formulaset（考勤公式配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_formulaset` | 主表 · 52 列 |
| `t_wtp_formulaset_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_formulaset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_formulaset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_formulaset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_formulaset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_formulaset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_formulaset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_formulaset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_formulaset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_formulaset.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_formulaset.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_formulaset.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_formulaset.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_formulaset.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_formulaset.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_formulaset.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_formulaset.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_formulaset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_formulaset_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_formulaset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_formulaset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_formulaset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_formulaset.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_formulaset.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_formulaset.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_formulaset.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_formulaset.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_formulaset.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_formulaset.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_formulaset.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_formulaset.fhisversion |  |  |
| resultitem | 结果项目 | BasedataField | t_wtp_formulaset.fresultitemid |  | hbp_calresultitem |
| originalexp | 公式内容 | TextAreaField | t_wtp_formulaset.foriginalexp |  |  |
| executeexp | 可执行表达式 | TextAreaField | t_wtp_formulaset.fexecuteexp |  |  |
| dependentfunc | 依赖的函数Unicode集合 | TextAreaField | t_wtp_formulaset.fdependentfunc |  |  |
| isdraft | 是否为草稿 | CheckBoxField | t_wtp_formulaset.fisdraft |  |  |
| dependentcalitem | 依赖的计算项目Unicode集合 | TextAreaField | t_wtp_formulaset.fdependentcalitem |  |  |
| dependentcustitem | 依赖的自定义项目Unicode集合 | TextAreaField | t_wtp_formulaset.fdependentcustitem |  |  |
| dependentcalitemforfunc | 函数依赖计算项目Unicode集合 | TextAreaField | t_wtp_formulaset.fdependentcalitemforfunc |  |  |
| resultitemcategory | 计算结果项目分类 | TextField | t_wtp_formulaset.fresultitemcategory |  |  |
| resultitemuniquecode | 计算结果唯一编码 | TextField | t_wtp_formulaset.fresultitemuniquecode |  |  |
| resultitemdatatype | 计算结果数据类型 | TextField | t_wtp_formulaset.fresultitemdatatype |  |  |
| resultitemdatalength | 计算结果数据长度 | IntegerField | t_wtp_formulaset.fresultitemdatalength |  |  |
| resultitemscale | 计算结果数据精度 | IntegerField | t_wtp_formulaset.fresultitemscale |  |  |
| uniquecodeexp | 关键字表达式 | TextAreaField | t_wtp_formulaset.funiquecodeexp |  |  |
| dependentcalitemfordg | 数据分级依赖的计算项目Unicode集合 | TextAreaField | t_wtp_formulaset.fdependentcalitemfordg |  |  |
| dependentdatagrade | 依赖的数据分级Unicode集合 | TextAreaField | t_wtp_formulaset.fdependentdatagrade |  |  |
| dependentbasedata | 依赖的基础资料UniqueCode集合 | TextField | t_wtp_formulaset.fdependentbasedata |  |  |
| dependentenum | 依赖的枚举UniqueCode集合 | TextField | t_wtp_formulaset.fdependentenum |  |  |
| exportitem | 额外输出的项目Unicode集合 | TextField | t_wtp_formulaset.fexportitem |  |  |
| resultitemid | 结果参数 | TextField | t_wtp_formulaset.fresultitemid |  |  |
| attitem | 考勤项目 | BasedataField | t_wtp_formulaset.fattitemid | ✓ | wtbd_attitem |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_formulaset（主表） | 48 |
| t_wtp_formulaset_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
