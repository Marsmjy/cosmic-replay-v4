# hrptmc_virtualentity — 虚拟对象

**表单编码**: `hrptmc_virtualentity`  
**表单ID**: `33G0W90+R3X+`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_virtualentity（虚拟对象） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_virtualentity` | BaseEntity | 主表 |
| `t_hrptmc_virtualfield` | EntryEntity | 字段信息 |
| `t_hrptmc_virtualscene` | EntryEntity | 场景 |

### 字段列表 — t_hrptmc_virtualentity（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrptmc_virtualentity.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrptmc_virtualentity.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrptmc_virtualentity.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrptmc_virtualentity.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrptmc_virtualentity.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrptmc_virtualentity.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrptmc_virtualentity.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_virtualentity.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrptmc_virtualentity.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrptmc_virtualentity.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrptmc_virtualentity.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrptmc_virtualentity.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrptmc_virtualentity.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrptmc_virtualentity.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrptmc_virtualentity.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_virtualentity.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrptmc_virtualentity.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrptmc_virtualentity.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrptmc_virtualentity.foriname |  |  |
| businessfield | 所属业务领域（废弃） | BasedataField | t_hrptmc_virtualentity.fbusinessfield |  | hbss_hrbusinessfield |
| fieldname | 名称 | MuliLangTextField | t_hrptmc_virtualentity.ffieldname | ✓ |  |
| fieldnumber | 编码 | TextField | t_hrptmc_virtualentity.ffieldnumber | ✓ |  |
| fieldpath | 属性全路径 | TextField | t_hrptmc_virtualentity.ffieldpath |  |  |
| iscommonfield | 是否可解析 | CheckBoxField | t_hrptmc_virtualentity.fiscommonfield |  |  |
| complextype | 复杂类型 | ComboField | t_hrptmc_virtualentity.fcomplextype | ✓ |  |
| fieldvaluetype | 字段值类型 | ComboField | t_hrptmc_virtualentity.ffieldvaluetype | ✓ |  |
| controltype | 控件类型 | ComboField | t_hrptmc_virtualentity.fcontroltype | ✓ |  |
| group | 字段分组 | BasedataField | t_hrptmc_virtualentity.fgroup |  | hrptmc_virtualfieldgroup |
| classpath | 虚拟对象处理类 | BasedataField | t_hrptmc_virtualentity.fclasspath | ✓ | hrptmc_virtentityclass |
| path | 虚实体处理类全路径(废弃) | TextField | t_hrptmc_virtualentity.fpath |  |  |
| cloud | 归属业务云 | BasedataField | t_hrptmc_virtualentity.fcloudid | ✓ | bos_devportal_bizcloud |
| objecttype | 对象类型 | ComboField | t_hrptmc_virtualentity.fobjecttype |  |  |
| virtualfields | 字段信息 | EntryEntity | → t_hrptmc_virtualfield |  |  |
| virtualscene | 场景 | EntryEntity | → t_hrptmc_virtualscene |  |  |

### 字段列表 — t_hrptmc_virtualscene（场景·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| scenename | 场景名称 | MuliLangTextField | t_hrptmc_virtualscene.fscenename |  |  |
| issupport | 是否支持 | CheckBoxField | t_hrptmc_virtualscene.fissupport |  |  |
| senenumber | 场景编码 | TextField | t_hrptmc_virtualscene.fsenenumber |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_virtualentity（主表） | 32 |
| t_hrptmc_virtualscene（场景） | 3 |

