# hbp_formula_unittest — 计算公式单元测试元数据

**表单编码**: `hbp_formula_unittest`  
**表单ID**: `2A9HVRVHOMYS`  
**归属**: HR基础服务云 / HR基础平台  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbp_formula_unittest（计算公式单元测试元数据） [BaseEntity]

- **数据库表**: `t_hbp_formula_unittest`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | findex |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| boid | 业务ID | BigIntField | fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | ffirstbsed |  |  |
| bsed | 生效日期 | DateField | fbsed |  |  |
| bsled | 失效日期 | DateField | fbsled |  |  |
| changedescription | 变更说明 | TextField | fchangedescription |  |  |
| hisversion | 版本号 | TextField | fhisversion |  |  |
| resultitem | 结果参数 | BasedataField | — | ✓ | hbp_calresultitem |
| originalexp | 公式内容 | TextAreaField | foriginalexp |  |  |
| executeexp | 可执行表达式 | TextAreaField | fexecuteexp |  |  |
| dependentfunc | 依赖的函数Unicode集合 | TextAreaField | fdependentfunc |  |  |
| isdraft | 是否为草稿 | CheckBoxField | fisdraft |  |  |
| dependentcalitem | 依赖的计算项目Unicode集合 | TextAreaField | fdependentcalitem |  |  |
| dependentcustitem | 依赖的自定义项目Unicode集合 | TextAreaField | fdependentcustitem |  |  |
| dependentcalitemforfunc | 函数依赖计算项目Unicode集合 | TextAreaField | fdependentcalitemforfunc |  |  |
| resultitemcategory | 计算结果项目分类 | TextField | fresultitemcategory |  |  |
| resultitemuniquecode | 计算结果唯一编码 | TextField | fresultitemuniquecode |  |  |
| resultitemdatatype | 计算结果数据类型 | TextField | fresultitemdatatype |  |  |
| resultitemdatalength | 计算结果数据长度 | IntegerField | fresultitemdatalength |  |  |
| resultitemscale | 计算结果数据精度 | IntegerField | fresultitemscale |  |  |
| uniquecodeexp | 关键字表达式 | TextAreaField | funiquecodeexp |  |  |
| dependentcalitemfordg | 数据分级依赖的计算项目Unicode集合 | TextAreaField | fdependentcalitemfordg |  |  |
| dependentdatagrade | 依赖的数据分级Unicode集合 | TextAreaField | fdependentdatagrade |  |  |
| dependentbasedata | 依赖的基础资料UniqueCode集合 | TextField | fdependentbasedata |  |  |
| dependentenum | 依赖的枚举UniqueCode集合 | TextField | fdependentenum |  |  |
| exportitem | 额外输出的项目Unicode集合 | TextField | — |  |  |
| resultitemid | 结果参数 | TextField | fresultitemid |  |  |

