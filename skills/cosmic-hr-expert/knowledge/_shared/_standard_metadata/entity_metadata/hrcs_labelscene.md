# hrcs_labelscene — 标签场景

**表单编码**: `hrcs_labelscene`  
**表单ID**: `2X5VK1KWHU1G`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_labelscene（标签场景） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_labelscene` | BaseEntity | 主表 |
| `t_hrcs_lblscenelbl` | EntryEntity | 关联标签 |
| `t_hrcs_lblsceneobjectids` | MulEmployeeField子表 | 打标对象 |

### 字段列表 — t_hrcs_labelscene（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_labelscene.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_labelscene.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_labelscene.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_labelscene.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_labelscene.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_labelscene.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_labelscene.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_labelscene.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_labelscene.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_labelscene.fsimplename |  |  |
| description | 场景描述 | MuliLangTextField | t_hrcs_labelscene.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_labelscene.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_labelscene.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_labelscene.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_labelscene.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_labelscene.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_labelscene.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_labelscene.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_labelscene.foriname |  |  |
| bizappid | 所属应用 | BasedataField | t_hrcs_labelscene.fbizappid | ✓ | hbp_devportal_bizapp |
| lblobjectids | 打标对象 | MulBasedataField | t_hrcs_lblsceneobjectids（子表） |  |  |
| projectability | 项目能力 | TextField | t_hrcs_labelscene.fprojectability |  |  |
| labelshow | 标签展示 | CheckBoxField | — |  |  |
| labelobject | 关联打标对象 | BasedataField | — | ✓ | hrcs_labelobject |
| entryentity | 关联标签 | EntryEntity | → t_hrcs_lblscenelbl |  |  |

### 字段列表 — t_hrcs_lblscenelbl（关联标签·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| labelobjectname | 打标对象 | TextField | — |  |  |
| dimension | 打标维度 | TextField | — |  |  |
| labelmethod | 打标方式 | TextField | — |  |  |
| effectiveperiod | 策略有效期 | DateRangeField | — |  |  |
| bizindex | 排序号 | IntegerField | t_hrcs_lblscenelbl.fbizindex |  |  |
| label | 标签 | BasedataField | t_hrcs_lblscenelbl.flabelid |  | hrcs_label |
| bizlabel | 支持业务页面打标 | CheckBoxField | t_hrcs_lblscenelbl.fbizlabel |  |  |
| labelvalue | 标签值 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_labelscene（主表） | 24 |
| t_hrcs_lblscenelbl（关联标签） | 8 |
| t_hrcs_lblsceneobjectids（MulEmployeeField子表） | 1 |
| 无数据库列 | 7 |

