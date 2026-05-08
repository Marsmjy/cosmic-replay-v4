# hrss_searchwtgrade — 权重等级

**表单编码**: `hrss_searchwtgrade`  
**表单ID**: `2A=DQV1DIJDL`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_searchwtgrade（权重等级） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrss_searchwtgrade` | BaseEntity | 主表 |
| `t_hrss_searchwgentry` | EntryEntity | 权重等级 |

### 字段列表 — t_hrss_searchwtgrade（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrss_searchwtgrade.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrss_searchwtgrade.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrss_searchwtgrade.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrss_searchwtgrade.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrss_searchwtgrade.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrss_searchwtgrade.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrss_searchwtgrade.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrss_searchwtgrade.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrss_searchwtgrade.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrss_searchwtgrade.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrss_searchwtgrade.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrss_searchwtgrade.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrss_searchwtgrade.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrss_searchwtgrade.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrss_searchwtgrade.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrss_searchwtgrade.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrss_searchwtgrade.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrss_searchwtgrade.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrss_searchwtgrade.foriname |  |  |
| usescene | 搜索场景 | BasedataField | t_hrss_searchwtgrade.fsceneid | ✓ | hrss_searchscene |
| weight_entry | 权重等级 | EntryEntity | → t_hrss_searchwgentry |  |  |

### 字段列表 — t_hrss_searchwgentry（权重等级·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| weightname | 权重等级 | MuliLangTextField | t_hrss_searchwgentry.fweightname | ✓ |  |
| weightvalue | 计分数值 | DecimalField | t_hrss_searchwgentry.fweightvalue | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrss_searchwtgrade（主表） | 20 |
| t_hrss_searchwgentry（权重等级） | 2 |

