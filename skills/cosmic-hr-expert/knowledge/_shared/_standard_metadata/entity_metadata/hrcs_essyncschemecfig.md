# hrcs_essyncschemecfig — ES同步方案配置

**表单编码**: `hrcs_essyncschemecfig`  
**表单ID**: `2MV1KXZUVE0A`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_essyncschemecfig（ES同步方案配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_essyncschemeconf` | BaseEntity | 主表 |
| `t_hrcs_esschemedetail` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_essyncschemeconf（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_essyncschemeconf.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_essyncschemeconf.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_essyncschemeconf.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_essyncschemeconf.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_essyncschemeconf.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_essyncschemeconf.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_essyncschemeconf.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_essyncschemeconf.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_essyncschemeconf.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_essyncschemeconf.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_essyncschemeconf.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_essyncschemeconf.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_essyncschemeconf.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_essyncschemeconf.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_essyncschemeconf.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_essyncschemeconf.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_essyncschemeconf.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_essyncschemeconf.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_essyncschemeconf.foriname |  |  |
| queryentity | 查询实体 | BasedataField | t_hrcs_essyncschemeconf.fqueryentityid | ✓ | hrcs_querydynsourcelist |
| bizapplytype | 应用业务类型 | ComboField | — | ✓ |  |
| relationtype | 实体关系模型 | ComboField | t_hrcs_essyncschemeconf.frelationtype |  |  |
| onetomanyentity | 1：N子实体名 | ComboField | t_hrcs_essyncschemeconf.fonetomanyentity |  |  |
| initstate | 初始化状态 | ComboField | t_hrcs_essyncschemeconf.finitstate | ✓ |  |
| version | 上线版本 | IntegerField | t_hrcs_essyncschemeconf.fversion |  |  |
| queryrelationenrtry | 单据体 | EntryEntity | → t_hrcs_esschemedetail |  |  |

### 字段列表 — t_hrcs_esschemedetail（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| parententityalias | 父实体(存值) | TextField | t_hrcs_esschemedetail.fparententityalias |  |  |
| parententityaliastxt | 父实体 | TextField | — |  |  |
| childentityalias | 子实体(存值) | TextField | t_hrcs_esschemedetail.fchildentityalias |  |  |
| childentityaliastxt | 子实体 | TextField | — |  |  |
| modeltype | 数据建模类型 | ComboField | t_hrcs_esschemedetail.fmodeltype | ✓ |  |
| arrayfiled | 数组字段(存值) | TextField | t_hrcs_esschemedetail.farrayfiled |  |  |
| arrayfiledtxt | 数组字段 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_essyncschemeconf（主表） | 25 |
| t_hrcs_esschemedetail（单据体） | 7 |
| 无数据库列 | 4 |

