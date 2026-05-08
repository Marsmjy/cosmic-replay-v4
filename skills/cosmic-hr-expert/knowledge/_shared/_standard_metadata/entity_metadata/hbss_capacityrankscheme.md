# hbss_capacityrankscheme — 能力等级方案

**表单编码**: `hbss_capacityrankscheme`  
**表单ID**: `2FQ1Q3HWR4=M`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_capacityrankscheme（能力等级方案） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbss_caprankscheme` | BaseEntity | 主表 |
| `t_hbss_caprankinfoentry` | EntryEntity | 单据体 |

### 字段列表 — t_hbss_caprankscheme（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbss_caprankscheme.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbss_caprankscheme.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbss_caprankscheme.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbss_caprankscheme.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbss_caprankscheme.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbss_caprankscheme.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbss_caprankscheme.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbss_caprankscheme.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbss_caprankscheme.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_hbss_caprankscheme.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hbss_caprankscheme.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbss_caprankscheme.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbss_caprankscheme.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbss_caprankscheme.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbss_caprankscheme.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbss_caprankscheme.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbss_caprankscheme.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbss_caprankscheme.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbss_caprankscheme.foriname |  |  |
| rankinfo | 等级信息 | MuliLangTextField | t_hbss_caprankscheme.frankinfo |  |  |
| entryentity | 单据体 | EntryEntity | → t_hbss_caprankinfoentry |  |  |

### 字段列表 — t_hbss_caprankinfoentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entrynumber | 等级编码 | TextField | t_hbss_caprankinfoentry.fnumber | ✓ |  |
| order | 顺序码 | IntegerField | t_hbss_caprankinfoentry.forder |  |  |
| entryname | 等级名称 | MuliLangTextField | t_hbss_caprankinfoentry.fname | ✓ |  |
| entrydescription | 描述 | MuliLangTextField | t_hbss_caprankinfoentry.fdescription |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_caprankscheme（主表） | 28 |
| t_hbss_caprankinfoentry（单据体） | 4 |
| 无数据库列 | 8 |

