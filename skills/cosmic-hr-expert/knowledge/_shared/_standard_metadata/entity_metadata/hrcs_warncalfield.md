# hrcs_warncalfield — HR预警计算字段

**表单编码**: `hrcs_warncalfield`  
**表单ID**: `3UZ3BSIMQE2L`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warncalfield（HR预警计算字段） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_warncalfield` | BaseEntity | 主表 |
| `t_hrcs_warncalreffield` | EntryEntity | 引用字段 |
| `t_hrcs_warncalpluginparam` | EntryEntity | 插件服务入参映射 |

### 字段列表 — t_hrcs_warncalfield（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_warncalfield.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_warncalfield.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_warncalfield.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_warncalfield.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_warncalfield.finitdatasource |  |  |
| index | 排序号 | IntegerField | t_hrcs_warncalfield.findex |  |  |
| number | 编码 | TextField | t_hrcs_warncalfield.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_warncalfield.fname |  |  |
| type | 类型 | ComboField | t_hrcs_warncalfield.ftype |  |  |
| valuetype | 字段值类型 | ComboField | t_hrcs_warncalfield.fvaluetype |  |  |
| controltype | 控件类型 | ComboField | t_hrcs_warncalfield.fcontroltype |  |  |
| expr | 表达式（用于执行） | TextField | t_hrcs_warncalfield.fexpr |  |  |
| viewexpr | 表达式（用于展示） | TextField | t_hrcs_warncalfield.fviewexpr |  |  |
| source | 字段来源 | ComboField | t_hrcs_warncalfield.fsource |  |  |
| order | 执行优先级 | IntegerField | t_hrcs_warncalfield.forder |  |  |
| isselected | 是否选中 | CheckBoxField | t_hrcs_warncalfield.fisselected |  |  |
| calmethod | 计算方式 | ComboField | t_hrcs_warncalfield.fcalmethod |  |  |
| hidefield | 是否隐藏 | CheckBoxField | t_hrcs_warncalfield.fhidefield |  |  |
| sourceid | 引用来源id | BigIntField | t_hrcs_warncalfield.fsourceid |  |  |
| serviceclass | 服务类 | TextField | t_hrcs_warncalfield.fserviceclass |  |  |
| functionradio | 函数配置 | RadioField | — |  |  |
| confway | 配置方式按钮组 | RadioGroupField | t_hrcs_warncalfield.fconfway |  |  |
| pluginradio | 插件配置 | RadioField | — |  |  |
| pluginservice | 插件服务 | BasedataField | t_hrcs_warncalfield.fpluginservice |  | hrcs_warnpluginservice |
| reffieldentry | 引用字段 | EntryEntity | → t_hrcs_warncalreffield |  |  |
| pluginparams | 插件服务入参映射 | EntryEntity | → t_hrcs_warncalpluginparam |  |  |

### 字段列表 — t_hrcs_warncalreffield（引用字段·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| reftype | 引用字段类型 | ComboField | t_hrcs_warncalreffield.freftype |  |  |
| reffieldalias | 引用实体字段别名 | TextField | t_hrcs_warncalreffield.freffieldalias |  |  |
| refcalfield | 引用计算字段 | BasedataField | t_hrcs_warncalreffield.frefcalfieldid |  | hrcs_warncalfield |

### 字段列表 — t_hrcs_warncalpluginparam（插件服务入参映射·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| paramnumber | 参数编码 | TextField | t_hrcs_warncalpluginparam.fparamnumber |  |  |
| inparamfieldalias | 入参字段alias | TextField | t_hrcs_warncalpluginparam.finparamfieldalias |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_warncalfield（主表） | 24 |
| t_hrcs_warncalreffield（引用字段） | 3 |
| t_hrcs_warncalpluginparam（插件服务入参映射） | 2 |
| 无数据库列 | 2 |

