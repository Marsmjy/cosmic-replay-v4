# hrss_searchweight — 排序权重配置

**表单编码**: `hrss_searchweight`  
**表单ID**: `2KS=AXFL0=8G`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_searchweight（排序权重配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrss_searchweightconf` | BaseEntity | 主表 |
| `t_hrss_searchweightentry` | EntryEntity | AI词性 |
| `t_hrss_searchwesubentry` | SubEntryEntity | 搜索结果排序权重设置 |

### 字段列表 — t_hrss_searchweightconf（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrss_searchweightconf.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrss_searchweightconf.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrss_searchweightconf.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrss_searchweightconf.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrss_searchweightconf.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrss_searchweightconf.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrss_searchweightconf.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrss_searchweightconf.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrss_searchweightconf.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrss_searchweightconf.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrss_searchweightconf.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrss_searchweightconf.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrss_searchweightconf.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrss_searchweightconf.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrss_searchweightconf.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrss_searchweightconf.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrss_searchweightconf.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrss_searchweightconf.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrss_searchweightconf.foriname |  |  |
| usescene | 搜索场景 | BasedataField | t_hrss_searchweightconf.fsceneid | ✓ | hrss_searchscene |
| entryentity | AI词性 | EntryEntity | → t_hrss_searchweightentry |  |  |

### 字段列表 — t_hrss_searchweightentry（AI词性·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| aiwordcategory | AI词性 | BasedataField | t_hrss_searchweightentry.faiwordcategory |  | hrss_aiwordcategory |
| aiwordcategorytext | AI词性名称 | TextField | t_hrss_searchweightentry.faiwordcategorytext |  |  |
| isdefault | 是否默认搜索方案 | TextField | t_hrss_searchweightentry.fisdefault |  |  |
| subentryentity | 搜索结果排序权重设置 | SubEntryEntity | → t_hrss_searchwesubentry |  |  |

### 字段列表 — t_hrss_searchwesubentry（搜索结果排序权重设置·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| grade | 权重等级 | BasedataField | t_hrss_searchwesubentry.fgrade | ✓ | hrss_searchwgentries |
| searchobjentityid | 所属业务对象 | BasedataField | t_hrss_searchwesubentry.fsearchobjentityid | ✓ | hrss_schobjjoinentity |
| fieldname | 属性字段 | MuliLangTextField | t_hrss_searchwesubentry.ffieldname |  |  |
| fieldid | 字段标识 | TextField | t_hrss_searchwesubentry.ffieldid |  |  |
| fieldnamecopy | 属性字段 | TextField | t_hrss_searchwesubentry.ffieldnamecopy |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrss_searchweightconf（主表） | 20 |
| t_hrss_searchweightentry（AI词性） | 3 |
| t_hrss_searchwesubentry（搜索结果排序权重设置） | 5 |

