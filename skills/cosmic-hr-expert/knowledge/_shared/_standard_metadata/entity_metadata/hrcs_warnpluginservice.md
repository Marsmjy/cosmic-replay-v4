# hrcs_warnpluginservice — 预警插件服务

**表单编码**: `hrcs_warnpluginservice`  
**表单ID**: `3XZVXLXAJP4T`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnpluginservice（预警插件服务） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_warnpluginservice` | BaseEntity | 主表 |
| `t_hrcs_warnpgparamentry` | EntryEntity | 参数信息分录 |

### 字段列表 — t_hrcs_warnpluginservice（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_warnpluginservice.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_warnpluginservice.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_warnpluginservice.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_warnpluginservice.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_warnpluginservice.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_warnpluginservice.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_warnpluginservice.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_warnpluginservice.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_warnpluginservice.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_warnpluginservice.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_warnpluginservice.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_warnpluginservice.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_warnpluginservice.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_warnpluginservice.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_warnpluginservice.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_warnpluginservice.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_warnpluginservice.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_warnpluginservice.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_warnpluginservice.foriname |  |  |
| bizcloud | 所属业务云 | BasedataField | t_hrcs_warnpluginservice.fbizcloudid |  | bos_devportal_bizcloud |
| bizapp | 所属应用 | BasedataField | t_hrcs_warnpluginservice.fbizappid | ✓ | hbp_devportal_bizapp |
| servicepath | 服务路径 | TextField | t_hrcs_warnpluginservice.fservicepath | ✓ |  |
| ismservice | 是否微服务 | CheckBoxField | t_hrcs_warnpluginservice.fismservice |  |  |
| datatype | 插件结果输出类型 | ComboField | t_hrcs_warnpluginservice.fdatatype | ✓ |  |
| paramentry | 参数信息分录 | EntryEntity | → t_hrcs_warnpgparamentry |  |  |

### 字段列表 — t_hrcs_warnpgparamentry（参数信息分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| paramnumber | 参数编码 | TextField | t_hrcs_warnpgparamentry.fparamnumber | ✓ |  |
| paramname | 参数名称 | MuliLangTextField | t_hrcs_warnpgparamentry.fparamname | ✓ |  |
| mustinput | 是否必填 | CheckBoxField | t_hrcs_warnpgparamentry.fmustinput |  |  |
| paramtype | 参数类型 | ComboField | t_hrcs_warnpgparamentry.fparamtype | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_warnpluginservice（主表） | 24 |
| t_hrcs_warnpgparamentry（参数信息分录） | 4 |

