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

# wtp_qtformulaset — 定额公式配置

**表单编码**: `wtp_qtformulaset`  
**表单ID**: `410TM4JY+WS3`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtformulaset（定额公式配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtformulaset` | 主表 · 52 列 |
| `t_wtp_qtformulaset_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_qtformulaset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_qtformulaset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_qtformulaset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_qtformulaset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_qtformulaset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_qtformulaset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_qtformulaset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtformulaset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_qtformulaset.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_qtformulaset.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_qtformulaset.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_qtformulaset.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_qtformulaset.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_qtformulaset.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_qtformulaset.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_qtformulaset.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_qtformulaset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_qtformulaset_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_qtformulaset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_qtformulaset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_qtformulaset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_qtformulaset.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_qtformulaset.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_qtformulaset.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_qtformulaset.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_qtformulaset.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_qtformulaset.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_qtformulaset.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_qtformulaset.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_qtformulaset.fhisversion |  |  |
| resultitem | 结果项目 | BasedataField | t_wtp_qtformulaset.fresultitemid |  | hbp_calresultitem |
| originalexp | 公式内容 | TextAreaField | t_wtp_qtformulaset.foriginalexp |  |  |
| executeexp | 可执行表达式 | TextAreaField | t_wtp_qtformulaset.fexecuteexp |  |  |
| dependentfunc | 依赖的函数Unicode集合 | TextAreaField | t_wtp_qtformulaset.fdependentfunc |  |  |
| isdraft | 是否为草稿 | CheckBoxField | t_wtp_qtformulaset.fisdraft |  |  |
| dependentcalitem | 依赖的计算项目Unicode集合 | TextAreaField | t_wtp_qtformulaset.fdependentcalitem |  |  |
| dependentcustitem | 依赖的自定义项目Unicode集合 | TextAreaField | t_wtp_qtformulaset.fdependentcustitem |  |  |
| dependentcalitemforfunc | 函数依赖计算项目Unicode集合 | TextAreaField | t_wtp_qtformulaset.fdependentcalitemforfunc |  |  |
| resultitemcategory | 计算结果项目分类 | TextField | t_wtp_qtformulaset.fresultitemcategory |  |  |
| resultitemuniquecode | 计算结果唯一编码 | TextField | t_wtp_qtformulaset.fresultitemuniquecode |  |  |
| resultitemdatatype | 计算结果数据类型 | TextField | t_wtp_qtformulaset.fresultitemdatatype |  |  |
| resultitemdatalength | 计算结果数据长度 | IntegerField | t_wtp_qtformulaset.fresultitemdatalength |  |  |
| resultitemscale | 计算结果数据精度 | IntegerField | t_wtp_qtformulaset.fresultitemscale |  |  |
| uniquecodeexp | 关键字表达式 | TextAreaField | t_wtp_qtformulaset.funiquecodeexp |  |  |
| dependentcalitemfordg | 数据分级依赖的计算项目Unicode集合 | TextAreaField | t_wtp_qtformulaset.fdependentcalitemfordg |  |  |
| dependentdatagrade | 依赖的数据分级Unicode集合 | TextAreaField | t_wtp_qtformulaset.fdependentdatagrade |  |  |
| dependentbasedata | 依赖的基础资料UniqueCode集合 | TextField | t_wtp_qtformulaset.fdependentbasedata |  |  |
| dependentenum | 依赖的枚举UniqueCode集合 | TextField | t_wtp_qtformulaset.fdependentenum |  |  |
| exportitem | 额外输出的项目Unicode集合 | TextField | t_wtp_qtformulaset.fexportitem |  |  |
| resultitemid | 结果参数 | TextField | t_wtp_qtformulaset.fresultitemid |  |  |
| attitem | 考勤项目 | BasedataField | t_wtp_qtformulaset.fattitemid |  | wtbd_attitem |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtformulaset（主表） | 48 |
| t_wtp_qtformulaset_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
