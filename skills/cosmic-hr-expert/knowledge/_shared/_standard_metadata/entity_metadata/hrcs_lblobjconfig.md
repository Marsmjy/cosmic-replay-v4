# hrcs_lblobjconfig — 能力配置

**表单编码**: `hrcs_lblobjconfig`  
**表单ID**: `2VGC47UA84G4`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_lblobjconfig（能力配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_lblobjconfig` | BaseEntity | 主表 |
| `t_hrcs_lblobjdispentry` | EntryEntity | 命中结果查看 |
| `t_hrcs_lblobjcondentry` | EntryEntity | 规则打标范围 |
| `t_hrcs_lblobjmnlentry` | EntryEntity | 手动打标范围 |

### 字段列表 — t_hrcs_lblobjconfig（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_lblobjconfig.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_lblobjconfig.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_lblobjconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_lblobjconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_lblobjconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_lblobjconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_lblobjconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_lblobjconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_lblobjconfig.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_lblobjconfig.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_lblobjconfig.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_lblobjconfig.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_lblobjconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_lblobjconfig.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_lblobjconfig.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_lblobjconfig.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_lblobjconfig.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_lblobjconfig.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_lblobjconfig.foriname |  |  |
| labelobjectid | 打标对象 | BasedataField | t_hrcs_lblobjconfig.flabelobjectid |  | hrcs_labelobject |
| manuallabel | 手动打标 | CheckBoxField | t_hrcs_lblobjconfig.fmanuallabel |  |  |
| rulelabel | 规则打标 | CheckBoxField | t_hrcs_lblobjconfig.frulelabel |  |  |
| displayentryentity | 命中结果查看 | EntryEntity | → t_hrcs_lblobjdispentry |  |  |
| conditionentryentity | 规则打标范围 | EntryEntity | → t_hrcs_lblobjcondentry |  |  |
| manualentryentity | 手动打标范围 | EntryEntity | → t_hrcs_lblobjmnlentry |  |  |

### 字段列表 — t_hrcs_lblobjdispentry（命中结果查看·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| isdefault | 是否预置 | TextField | t_hrcs_lblobjdispentry.fisdefault |  |  |
| dispcolumnid | 字段名称 | BasedataField | t_hrcs_lblobjdispentry.fdispcolumnid |  | hrcs_lblobjectfield |
| dispcolumnshow | 显示名称 | MuliLangTextField | t_hrcs_lblobjdispentry.fdispcolumnshow |  |  |
| displayfieldnumber | 字段编码 | TextField | t_hrcs_lblobjdispentry.ffieldnumber |  |  |
| displayid | 字段ID | BigIntField | t_hrcs_lblobjdispentry.ffieldid |  |  |
| displaydisplayname | 显示名称 | MuliLangTextField | t_hrcs_lblobjdispentry.fdisplayname |  |  |
| displayentitynumber | 所属业务对象编码 | TextField | t_hrcs_lblobjdispentry.fentitynumber |  |  |
| displayentityname | 所属业务对象 | MuliLangTextField | — |  |  |
| displayfieldname | 字段名称 | MuliLangTextField | t_hrcs_lblobjdispentry.ffieldname |  |  |
| displayentitynumberalias | 所属业务对象编码别名 | TextField | t_hrcs_lblobjdispentry.fentitynumberalias |  |  |

### 字段列表 — t_hrcs_lblobjcondentry（规则打标范围·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| condcolumnid | 字段名 | BasedataField | t_hrcs_lblobjcondentry.fcondcolumnid |  | hrcs_lblobjectfield |
| condcolumndesc | 描述 | MuliLangTextField | t_hrcs_lblobjcondentry.fcondcolumndesc |  |  |
| rulefieldnumber | 字段编码 | TextField | t_hrcs_lblobjcondentry.ffieldnumber |  |  |
| ruledisplayname | 显示名称 | MuliLangTextField | t_hrcs_lblobjcondentry.fdisplayname |  |  |
| ruleid | 字段ID | IntegerField | t_hrcs_lblobjcondentry.ffieldid |  |  |
| ruleentitynumber | 所属业务对象编码 | TextField | t_hrcs_lblobjcondentry.fentitynumber |  |  |
| ruleentityname | 所属业务对象 | MuliLangTextField | — |  |  |
| rulefieldname | 字段名称 | MuliLangTextField | t_hrcs_lblobjcondentry.ffieldname |  |  |
| ruleentitynumberalias | 所属业务对象编码别名 | TextField | t_hrcs_lblobjcondentry.fentitynumberalias |  |  |

### 字段列表 — t_hrcs_lblobjmnlentry（手动打标范围·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| manualfieldnumber | 字段编码 | TextField | t_hrcs_lblobjmnlentry.ffieldnumber |  |  |
| manualsearchfield | 作为搜索字段 | CheckBoxField | t_hrcs_lblobjmnlentry.fsearchfield |  |  |
| manualid | 字段ID | IntegerField | t_hrcs_lblobjmnlentry.ffieldid |  |  |
| manualentitynumber | 所属业务对象编码 | TextField | t_hrcs_lblobjmnlentry.fentitynumber |  |  |
| manualdisplayname | 显示名称 | MuliLangTextField | t_hrcs_lblobjmnlentry.fdisplayname |  |  |
| manualfieldname | 字段名称 | MuliLangTextField | t_hrcs_lblobjmnlentry.ffieldname |  |  |
| manualentityname | 所属业务对象 | MuliLangTextField | — |  |  |
| manualentitynumberalias | 所属业务对象编码别名 | TextField | t_hrcs_lblobjmnlentry.fentitynumberalias |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_lblobjconfig（主表） | 22 |
| t_hrcs_lblobjdispentry（命中结果查看） | 10 |
| t_hrcs_lblobjcondentry（规则打标范围） | 9 |
| t_hrcs_lblobjmnlentry（手动打标范围） | 8 |
| 无数据库列 | 3 |

