# hrcs_coordbizfieldgrp — 业务协作字段分组

**表单编码**: `hrcs_coordbizfieldgrp`  
**表单ID**: `4YF1951P812Z`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_coordbizfieldgrp（业务协作字段分组） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_coordbizfieldgrp` | BaseEntity | 主表 |
| `t_hrcs_coordbizfield` | EntryEntity | 单据体 |
| `t_hrcs_coordbizfieldtars` | MulEmployeeField子表 | 可选指标 |

### 字段列表 — t_hrcs_coordbizfieldgrp（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_coordbizfieldgrp.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_coordbizfieldgrp.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_coordbizfieldgrp.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_coordbizfieldgrp.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_coordbizfieldgrp.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_coordbizfieldgrp.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_coordbizfieldgrp.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_coordbizfieldgrp.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_coordbizfieldgrp.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_coordbizfieldgrp.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_coordbizfieldgrp.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_coordbizfieldgrp.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_coordbizfieldgrp.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_coordbizfieldgrp.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_coordbizfieldgrp.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_coordbizfieldgrp.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_coordbizfieldgrp.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_coordbizfieldgrp.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_coordbizfieldgrp.foriname |  |  |
| grouptype | 分组类型 | ComboField | t_hrcs_coordbizfieldgrp.fgrouptype | ✓ |  |
| coordbizobject | 业务对象 | BasedataField | t_hrcs_coordbizfieldgrp.fid | ✓ | hrcs_coordbizobject |
| alias | 别名 | TextField | t_hrcs_coordbizfieldgrp.falias |  |  |
| basedata | 基础资料 | BasedataField | t_hrcs_coordbizfieldgrp.fbasedataid |  | hbp_entityobject |
| excludefields | 排除字段 | TextField | t_hrcs_coordbizfieldgrp.fexcludefields |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrcs_coordbizfield |  |  |

### 字段列表 — t_hrcs_coordbizfield（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldnumber | 字段编码 | TextField | t_hrcs_coordbizfield.ffieldnumber |  |  |
| fieldname | 字段名称 | MuliLangTextField | t_hrcs_coordbizfield.ffieldname |  |  |
| fieldtype | 字段类型 | ComboField | t_hrcs_coordbizfield.ffieldtype |  |  |
| ismustinput | 是否必录 | CheckBoxField | t_hrcs_coordbizfield.fismustinput |  |  |
| iskeepold | 支持保持旧值 | CheckBoxField | t_hrcs_coordbizfield.fiskeepold |  |  |
| mustinputgroup | 必填分组 | TextField | t_hrcs_coordbizfield.fmustinputgroup |  |  |
| targets | 可选指标 | MulBasedataField | t_hrcs_coordbizfieldtars（子表） |  |  |
| fieldenable | 字段使用状态 | BillStatusField | t_hrcs_coordbizfield.ffieldenable |  |  |
| fieldstatus | 分组数据状态 | BillStatusField | t_hrcs_coordbizfield.ffieldstatus |  |  |
| tips | 规则配置说明 | MuliLangTextField | t_hrcs_coordbizfield.ftips |  |  |
| isfieldgroup | 是否字段组 | CheckBoxField | t_hrcs_coordbizfield.fisfieldgroup |  |  |
| realfields | 具体字段编码 | TextField | t_hrcs_coordbizfield.frealfields |  |  |
| fieldalias | 字段别名 | TextField | t_hrcs_coordbizfield.ffieldalias |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_coordbizfieldgrp（主表） | 24 |
| t_hrcs_coordbizfield（单据体） | 13 |
| t_hrcs_coordbizfieldtars（MulEmployeeField子表） | 1 |

