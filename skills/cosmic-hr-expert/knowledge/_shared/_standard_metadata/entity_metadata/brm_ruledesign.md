# brm_ruledesign — 规则设计

**表单编码**: `brm_ruledesign`  
**表单ID**: `1IMW1KY9951/`  
**归属**: HR基础服务云 / 业务规则管理  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: brm_ruledesign（规则设计） [BaseEntity]

- **数据库表**: `t_brm_drlfilter`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 规则编码 | TextField | fnumber |  |  |
| name | 规则名称 | MuliLangTextField | fname |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| description | 规则描述 | MuliLangTextField | fdescription |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| ruledate | 触发日期操作的控件（勿删） | DateField | — |  |  |
| ruleorder | 优先级 | IntegerField | fruleorder |  |  |
| templatenumber | 模板编码 | TextField | ftemplatenumber |  |  |
| policy | 所属策略 | BasedataField | fid |  | brm_policy_edit |
| bizapp | 所属应用 | BasedataField | fbizappid | ✓ | bos_devportal_bizapp |
| scene | 所属场景 | BasedataField | fsceneid | ✓ | brm_scene |
| policyname | 所属策略 | TextField | — |  |  |
| ruleenable | 使用状态 | ComboField | — |  |  |
| conditions | 条件 | TextField | fconditions |  |  |
| results | 结果 | TextField | fresults |  |  |
| adminorg | 行政组织 | HRMulAdminOrgField | t_brm_ruleadminorg（子表） |  |  |

