# hrss_customfilter — 自定义过滤项

**表单编码**: `hrss_customfilter`  
**表单ID**: `3U=ELGYH4J5X`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_customfilter（自定义过滤项） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrss_customfilter` | BaseEntity | 主表 |
| `t_hrss_filteritem` | EntryEntity | 单据体 |

### 字段列表 — t_hrss_customfilter（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrss_customfilter.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrss_customfilter.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrss_customfilter.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrss_customfilter.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrss_customfilter.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrss_customfilter.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrss_customfilter.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrss_customfilter.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrss_customfilter.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrss_customfilter.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrss_customfilter.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrss_customfilter.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrss_customfilter.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrss_customfilter.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrss_customfilter.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrss_customfilter.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrss_customfilter.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrss_customfilter.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrss_customfilter.foriname |  |  |
| type | 适用类型 | ComboField | t_hrss_customfilter.ftype | ✓ |  |
| basedata | 基础资料 | BasedataField | t_hrss_customfilter.fbasedata |  | hbp_entityobject |
| entryentity | 单据体 | EntryEntity | → t_hrss_filteritem |  |  |

### 字段列表 — t_hrss_filteritem（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| filternumber | 编码 | TextField | t_hrss_filteritem.fnumber |  |  |
| filterrule | 过滤规则 | TextField | t_hrss_filteritem.ffilterrule |  |  |
| filtername | 名称 | MuliLangTextField | t_hrss_filteritem.fname |  |  |
| filtertype | 过滤方式 | ComboField | t_hrss_filteritem.ffiltertype |  |  |
| filterdescription | 描述 | MuliLangTextField | t_hrss_filteritem.fdescription |  |  |
| displayrule | 过滤规则 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrss_customfilter（主表） | 21 |
| t_hrss_filteritem（单据体） | 6 |
| 无数据库列 | 1 |

