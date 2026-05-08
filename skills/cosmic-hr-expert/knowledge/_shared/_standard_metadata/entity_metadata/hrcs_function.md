# hrcs_function — 函数配置

**表单编码**: `hrcs_function`  
**表单ID**: `23IY21QP7M=I`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_function（函数配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_function` | BaseEntity | 主表 |
| `t_hrcs_functionparams` | EntryEntity | 参数 |
| `t_hrcs_functionimport` | EntryEntity | 导入依赖包代码分录 |

### 字段列表 — t_hrcs_function（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_function.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_function.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_function.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_function.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_function.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_function.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_function.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_function.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_function.fmasterid |  |  |
| group | 函数分类 | GroupField | — |  | hrcs_functiontype |
| disabler | 禁用人 | UserField | t_hrcs_function.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_function.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_function.finitdatasource |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_function.fissyspreset |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_function.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_function.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_function.foriname |  |  |
| example | 函数示例 | MuliLangTextField | t_hrcs_function.fexample | ✓ |  |
| define | 函数定义 | TextField | t_hrcs_function.fdefine | ✓ |  |
| funcdatatype | 返回值类型 | ComboField | t_hrcs_function.ffuncdatatype | ✓ |  |
| description | 功能描述 | MuliLangTextField | t_hrcs_function.fdescription |  |  |
| uniquecode | 唯一编码 | TextField | t_hrcs_function.funiquecode |  |  |
| funcexp | 函数体 | TextAreaField | t_hrcs_function.ffuncexp |  |  |
| funsource | 函数来源 | ComboField | t_hrcs_function.ffunsource |  |  |
| funkey | 函数关键词 | TextField | t_hrcs_function.ffunkey | ✓ |  |
| isvariableparam | 是否可变参数 | CheckBoxField | t_hrcs_function.fisvariableparam |  |  |
| params | 参数 | EntryEntity | → t_hrcs_functionparams |  |  |
| importentry | 导入依赖包代码分录 | EntryEntity | → t_hrcs_functionimport |  |  |

### 字段列表 — t_hrcs_functionparams（参数·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| paramdatatype | 参数类型 | ComboField | t_hrcs_functionparams.fparamdatatype | ✓ |  |
| paramname | 参数名 | MuliLangTextField | t_hrcs_functionparams.fparamname | ✓ |  |
| paramdesc | 参数说明 | MuliLangTextField | t_hrcs_functionparams.fparamdesc |  |  |

### 字段列表 — t_hrcs_functionimport（导入依赖包代码分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| importcode | 包名 | TextField | t_hrcs_functionimport.fimportcode | ✓ |  |
| importpackagedesc | 说明 | MuliLangTextField | t_hrcs_functionimport.fimportpackagedesc |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_function（主表） | 26 |
| t_hrcs_functionparams（参数） | 3 |
| t_hrcs_functionimport（导入依赖包代码分录） | 2 |
| 无数据库列 | 1 |

