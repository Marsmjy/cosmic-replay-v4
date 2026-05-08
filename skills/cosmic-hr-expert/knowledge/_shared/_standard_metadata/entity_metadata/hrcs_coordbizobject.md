# hrcs_coordbizobject — 协作业务对象

**表单编码**: `hrcs_coordbizobject`  
**表单ID**: `4YEVMAR6Q7A1`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_coordbizobject（协作业务对象） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_coordbizobject` | BaseEntity | 主表 |
| `t_hrcs_coordbizfieldgrp` | EntryEntity | 字段分组单据体 |
| `（虚拟分录）` | EntryEntity | 业务单元分录 |
| `t_hrcs_coordbizfield` | SubEntryEntity | 字段子单据体 |
| `t_hrcs_coordbizfieldtars` | MulEmployeeField子表 | 可选指标 |

### 字段列表 — t_hrcs_coordbizobject（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_coordbizobject.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_coordbizobject.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_coordbizobject.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_coordbizobject.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_coordbizobject.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_coordbizobject.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_coordbizobject.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_coordbizobject.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_coordbizobject.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_coordbizobject.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_coordbizobject.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_coordbizobject.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_coordbizobject.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_coordbizobject.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_coordbizobject.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_coordbizobject.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_coordbizobject.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_coordbizobject.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_coordbizobject.foriname |  |  |
| objectype | 业务对象 | BasedataField | t_hrcs_coordbizobject.fobjectypeid | ✓ | bos_objecttype |
| bizapp | 所属应用 | BasedataField | t_hrcs_coordbizobject.fbizappid | ✓ | bos_devportal_bizapp |
| coordservice | 协作服务名 | TextField | t_hrcs_coordbizobject.fcoordservice | ✓ |  |
| scene | 所属场景 | BasedataField | t_hrcs_coordbizobject.fsceneid | ✓ | brm_scene |
| coordapp | 协作应用 | BasedataField | t_hrcs_coordbizobject.fcoordappid | ✓ | bos_devportal_bizapp |
| verifbilltype | 核定单实体 | BasedataField | t_hrcs_coordbizobject.fverifbilltypeid | ✓ | bos_objecttype |
| bizextregister | 业务增强插件注册器 | TextField | t_hrcs_coordbizobject.fbizextregister |  |  |
| versioncovertype | 版本覆盖策略 | ComboField | t_hrcs_coordbizobject.fversioncovertype |  |  |
| defaultdealway | 默认处理类型 | ComboField | t_hrcs_coordbizobject.fdefaultdealway |  |  |
| bussinessfield | 业务类型 | BasedataField | t_hrcs_coordbizobject.fbussinessfieldid |  | hbss_bussinessfield |
| orgfield | 组织 | OrgField | t_hrcs_coordbizobject.forgfield |  | bos_org |
| policyscene | 策略所属场景 | BasedataField | t_hrcs_coordbizobject.fpolicyscene | ✓ | brm_scene |
| paramnumberpre | 场景出参标识前缀 | TextField | t_hrcs_coordbizobject.fparamnumberpre |  |  |
| fieldgroupentry | 字段分组单据体 | EntryEntity | → t_hrcs_coordbizfieldgrp |  |  |
| entrybulist | 业务单元分录 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — t_hrcs_coordbizfieldgrp（字段分组单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| groupname | 业务对象 | MuliLangTextField | t_hrcs_coordbizfieldgrp.fgroupname |  |  |
| groupnumber | 分组编码 | TextField | t_hrcs_coordbizfieldgrp.fgroupnumber |  |  |
| grouptype | 分组类型 | ComboField | t_hrcs_coordbizfieldgrp.fgrouptype |  |  |
| groupstatus | 分组数据状态 | BillStatusField | t_hrcs_coordbizfieldgrp.fgroupstatus |  |  |
| groupenable | 分组使用状态 | BillStatusField | t_hrcs_coordbizfieldgrp.fgroupenable |  |  |
| alias | 别名 | TextField | t_hrcs_coordbizfieldgrp.falias |  |  |
| basedata | 基础资料 | BasedataField | t_hrcs_coordbizfieldgrp.fbasedataid |  | hbp_entityobject |
| excludefields | 排除字段 | TextField | t_hrcs_coordbizfieldgrp.fexcludefields |  |  |
| groupissyspreset | 系统预置 | ComboField | t_hrcs_coordbizfieldgrp.fissyspreset |  |  |
| fieldentry | 字段子单据体 | SubEntryEntity | → t_hrcs_coordbizfield |  |  |

### 字段列表 — （业务单元分录·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — t_hrcs_coordbizfield（字段子单据体·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldnumber | 字段编码 | TextField | t_hrcs_coordbizfield.ffieldnumber |  |  |
| fieldname | 字段名称 | MuliLangTextField | t_hrcs_coordbizfield.ffieldname |  |  |
| fieldtype | 字段类型 | ComboField | t_hrcs_coordbizfield.ffieldtype |  |  |
| ismustinput | 必填 | CheckBoxField | t_hrcs_coordbizfield.fismustinput |  |  |
| iskeepold | 支持保持原值 | CheckBoxField | t_hrcs_coordbizfield.fiskeepold |  |  |
| mustinputgroup | 必填分组 | TextField | t_hrcs_coordbizfield.fmustinputgroup |  |  |
| targets | 可选指标 | MulBasedataField | t_hrcs_coordbizfieldtars（子表） |  |  |
| fieldenable | 字段使用状态 | BillStatusField | t_hrcs_coordbizfield.ffieldenable |  |  |
| fieldstatus | 分组数据状态 | BillStatusField | t_hrcs_coordbizfield.ffieldstatus |  |  |
| tips | 值规则配置说明 | MuliLangTextField | t_hrcs_coordbizfield.ftips |  |  |
| keepoldtips | 保持旧值配置说明 | MuliLangTextField | t_hrcs_coordbizfield.fkeepoldtips |  |  |
| isfieldgroup | 是否字段组 | CheckBoxField | t_hrcs_coordbizfield.fisfieldgroup |  |  |
| realfields | 具体字段编码 | TextField | t_hrcs_coordbizfield.frealfields |  |  |
| fieldalias | 字段别名 | TextField | t_hrcs_coordbizfield.ffieldalias |  |  |
| issyspresetfield | 系统预置 | ComboField | t_hrcs_coordbizfield.fissyspreset |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_coordbizobject（主表） | 32 |
| t_hrcs_coordbizfieldgrp（字段分组单据体） | 9 |
| （业务单元分录） | 2 |
| t_hrcs_coordbizfield（字段子单据体） | 15 |
| t_hrcs_coordbizfieldtars（MulEmployeeField子表） | 1 |

