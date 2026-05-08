# hrcs_label — 标签

**表单编码**: `hrcs_label`  
**表单ID**: `2Y0T1H8RQE3Y`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_label（标签） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_label` | BaseEntity | 主表 |
| `t_hrcs_labelobjectrelnew` | EntryEntity | 打标维度 |
| `t_hrcs_labelvaluenew` | EntryEntity | 标签值域 |

### 字段列表 — t_hrcs_label（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_label.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_label.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_label.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_label.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_label.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_label.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_label.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_label.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_label.fmasterid |  |  |
| group | 标签分类 | GroupField | — |  | hrcs_labelgroup |
| disabler | 禁用人 | UserField | t_hrcs_label.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_label.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_label.finitdatasource |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_label.fissyspreset |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_label.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_label.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_label.foriname |  |  |
| type | 标签类型 | ComboField | t_hrcs_label.ftype | ✓ |  |
| description | 描述 | MuliLangTextField | t_hrcs_label.fdescription |  |  |
| labelobjects | 打标对象 | TextField | — |  |  |
| labelvalues | 标签值 | TextField | — |  |  |
| labelobject | 打标对象 | BasedataField | t_hrcs_label.flabelobjectid | ✓ | hrcs_labelobject |
| brmscene | 场景 | BasedataField | t_hrcs_label.fbrmsceneid |  | brm_scene |
| labelobj | 关联打标对象 | MulBasedataField | — | ✓ |  |
| easy | 规则配置 | RadioField | — |  |  |
| plugin | 插件配置 | RadioField | — |  |  |
| configtype | 配置模式 | RadioGroupField | t_hrcs_label.fconfigtype |  |  |
| entryentityrange | 打标维度 | EntryEntity | → t_hrcs_labelobjectrelnew |  |  |
| entryentitylabelvalue | 标签值域 | EntryEntity | → t_hrcs_labelvaluenew |  |  |

### 字段列表 — t_hrcs_labelvaluenew（标签值域·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| labelvalue | 标签值规则名称 | MuliLangTextField | t_hrcs_labelvaluenew.fvalue | ✓ |  |
| labelvaluedesc | 标签值规则释义 | MuliLangTextField | t_hrcs_labelvaluenew.fdescription | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_label（主表） | 27 |
| t_hrcs_labelvaluenew（标签值域） | 2 |
| 无数据库列 | 6 |

