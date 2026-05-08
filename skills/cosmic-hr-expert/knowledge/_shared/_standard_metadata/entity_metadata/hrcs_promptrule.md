# hrcs_promptrule — 提示语映射

**表单编码**: `hrcs_promptrule`  
**表单ID**: `1GZPP9KGFCN=`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_promptrule（提示语映射） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_promptrule` | BaseEntity | 主表 |
| `t_hrcs_promptruleentry` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_promptrule（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_promptrule.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_promptrule.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_promptrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_promptrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_promptrule.fmodifierid |  | bos_user |
| enable | 状态 | BillStatusField | t_hrcs_promptrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_promptrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_promptrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_promptrule.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_promptrule.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_promptrule.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_promptrule.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_promptrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_promptrule.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_promptrule.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_promptrule.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_promptrule.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_promptrule.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_promptrule.foriname |  |  |
| businessobject | 页面 | BasedataField | t_hrcs_promptrule.fbusinessobject | ✓ | hbp_entityobject |
| bonumber | 业务对象长编码 | TextField | t_hrcs_promptrule.fbonumber |  |  |
| controlnumber | 控件标识 | TextField | t_hrcs_promptrule.fcontrolnumber |  |  |
| controledit | 控件 | TextField | — | ✓ |  |
| controlname | 控件名称 | MuliLangTextField | t_hrcs_promptrule.fcontrolname |  |  |
| controltype | 控件类型 | MuliLangTextField | t_hrcs_promptrule.fcontroltype |  |  |
| entity | 业务对象 | BasedataField | t_hrcs_promptrule.fentity |  | bos_entitymeta |
| entryentity | 单据体 | EntryEntity | → t_hrcs_promptruleentry |  |  |

### 字段列表 — t_hrcs_promptruleentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryprompt | 提示语 | BasedataField | t_hrcs_promptruleentry.fentryprompt |  | hrcs_prompt |
| entrydescription | 描述 | MuliLangTextField | t_hrcs_promptruleentry.fentrydescription |  |  |
| entryrule | 规则 | TextField | t_hrcs_promptruleentry.fentryrule |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_promptrule（主表） | 26 |
| t_hrcs_promptruleentry（单据体） | 3 |
| 无数据库列 | 1 |

