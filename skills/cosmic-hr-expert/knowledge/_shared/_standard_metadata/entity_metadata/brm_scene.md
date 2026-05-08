# brm_scene — 场景管理

**表单编码**: `brm_scene`  
**表单ID**: `1IMW4OF8QQ/L`  
**归属**: HR基础服务云 / 业务规则管理  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: brm_scene（场景管理） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_brm_scene` | BaseEntity | 主表 |
| `t_brm_sceneinput` | EntryEntity | 输入参数 |
| `t_brm_sceneoutput` | EntryEntity | 输出参数 |

### 字段列表 — t_brm_scene（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 场景编码 | TextField | t_brm_scene.fnumber |  |  |
| name | 场景名称 | MuliLangTextField | t_brm_scene.fname |  |  |
| status | 数据状态 | BillStatusField | t_brm_scene.fstatus |  |  |
| creator | 创建人 | CreaterField | t_brm_scene.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_brm_scene.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_brm_scene.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_brm_scene.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_brm_scene.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_brm_scene.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_brm_scene.fsimplename |  |  |
| description | 场景描述 | MuliLangTextField | t_brm_scene.fdescription |  |  |
| index | 排序号 | IntegerField | t_brm_scene.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_brm_scene.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_brm_scene.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_brm_scene.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_brm_scene.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_brm_scene.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_brm_scene.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_brm_scene.foriname |  |  |
| bizappid | 所属应用 | BasedataField | t_brm_scene.fbizappid | ✓ | hbp_devportal_bizapp |
| iseditscene | 场景是否允许编辑 | CheckBoxField | t_brm_scene.fiseditscene |  |  |
| iseditrule | 允许在规则中心编辑规则 | CheckBoxField | t_brm_scene.fiseditrule |  |  |
| sceneinputparams | 输入参数 | EntryEntity | → t_brm_sceneinput |  |  |
| sceneoutputparams | 输出参数 | EntryEntity | → t_brm_sceneoutput |  |  |

### 字段列表 — t_brm_sceneinput（输入参数·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| inputnumber | 参数标识 | TextField | t_brm_sceneinput.fnumber |  |  |
| inputparamstype | 参数类型 | ComboField | t_brm_sceneinput.fparamstype |  |  |
| inputobject | 业务对象/基础资料 | BasedataField | t_brm_sceneinput.fparamsobject |  | bos_entityobject |
| inputname | 参数名称 | MuliLangTextField | t_brm_sceneinput.fname |  |  |
| inputcombo | 下拉项 | TextField | t_brm_sceneinput.fcombofield |  |  |
| inputmultiple | 允许多选 | ComboField | t_brm_sceneinput.fmultiple |  |  |
| inputdateformat | 掩码 | ComboField | t_brm_sceneinput.fdateformat |  |  |
| inputdynprop | 业务对象属性 | TextField | t_brm_sceneinput.fdynprop |  |  |
| inputissyspreset | 是否系统预置 | CheckBoxField | t_brm_sceneinput.fissyspreset |  |  |
| inputpresetisedit | 预置是否已更新 | CheckBoxField | t_brm_sceneinput.fpresetisedit |  |  |
| inputtreelv | 业务对象属性层级 | IntegerField | t_brm_sceneinput.ftreelv |  |  |

### 字段列表 — t_brm_sceneoutput（输出参数·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| outputnumber | 参数标识 | TextField | t_brm_sceneoutput.fnumber |  |  |
| outputparamstype | 参数类型 | ComboField | t_brm_sceneoutput.fparamstype |  |  |
| outputobject | 业务对象/基础资料 | BasedataField | t_brm_sceneoutput.fparamsobject |  | bos_entityobject |
| outputname | 参数名称 | MuliLangTextField | t_brm_sceneoutput.fname |  |  |
| outputmultiple | 允许多选 | ComboField | t_brm_sceneoutput.fmultiple |  |  |
| outputcombo | 下拉项 | TextField | t_brm_sceneoutput.fcombofield |  |  |
| outputdateformat | 掩码 | ComboField | t_brm_sceneoutput.fdateformat |  |  |
| outputdynprop | 字段 | TextField | t_brm_sceneoutput.fdynprop |  |  |
| outputissyspreset | 是否系统预置 | CheckBoxField | t_brm_sceneoutput.fissyspreset |  |  |
| outputpresetisedit | 预置是否已更新 | CheckBoxField | t_brm_sceneoutput.fpresetisedit |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_brm_scene（主表） | 22 |
| t_brm_sceneinput（输入参数） | 11 |
| t_brm_sceneoutput（输出参数） | 10 |

