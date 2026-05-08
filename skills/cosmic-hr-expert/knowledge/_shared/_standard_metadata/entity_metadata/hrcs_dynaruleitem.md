# hrcs_dynaruleitem — 规则参数项

**表单编码**: `hrcs_dynaruleitem`  
**表单ID**: `4ZH=9/UKW/1/`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dynaruleitem（规则参数项） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_dynaruleitem` | BaseEntity | 主表 |
| `t_hrcs_dynaruleitemenum` | EntryEntity | 枚举值单据体 |

### 字段列表 — t_hrcs_dynaruleitem（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_dynaruleitem.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_dynaruleitem.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_dynaruleitem.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_dynaruleitem.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_dynaruleitem.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_dynaruleitem.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_dynaruleitem.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_dynaruleitem.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_dynaruleitem.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_dynaruleitem.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_dynaruleitem.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_dynaruleitem.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_dynaruleitem.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_dynaruleitem.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_dynaruleitem.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_dynaruleitem.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_dynaruleitem.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_dynaruleitem.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_dynaruleitem.foriname |  |  |
| datatype | 类型 | ComboField | t_hrcs_dynaruleitem.fdatatype | ✓ |  |
| entitytype | 基础资料类型 | BasedataField | t_hrcs_dynaruleitem.fentitytype | ✓ | bos_entityobject |
| relatpropkey | 属性 | TextField | t_hrcs_dynaruleitem.frelatpropkey |  |  |
| isrelatparam | 关联参数项 | CheckBoxField | t_hrcs_dynaruleitem.fisrelatparam |  |  |
| relatruleparam | 主规则参数项 | BasedataField | t_hrcs_dynaruleitem.frelatruleparamid | ✓ | hrcs_dynaruleitem |
| valsourcetype | 值来源类型 | ComboField | t_hrcs_dynaruleitem.fvalsourcetype | ✓ |  |
| sourceentitytype | 值来源实体 | BasedataField | t_hrcs_dynaruleitem.fsourceentitytype | ✓ | bos_entityobject |
| sourcepropkey | 值来源属性 | TextField | t_hrcs_dynaruleitem.fsourcepropkey |  |  |
| mserviceapp | 微服务所在应用 | BasedataField | t_hrcs_dynaruleitem.fmserviceappid | ✓ | bos_devportal_bizapp |
| mserviceclass | 微服务类 | TextField | t_hrcs_dynaruleitem.fmserviceclass | ✓ |  |
| relatpropname | 属性 | TextField | — | ✓ |  |
| sourcepropname | 值来源属性 | TextField | — | ✓ |  |
| entryentity | 枚举值单据体 | EntryEntity | → t_hrcs_dynaruleitemenum |  |  |

### 字段列表 — t_hrcs_dynaruleitemenum（枚举值单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| value | 枚举值 | TextField | t_hrcs_dynaruleitemenum.fvalue | ✓ |  |
| displayvalue | 枚举名称 | MuliLangTextField | t_hrcs_dynaruleitemenum.fdisplayvalue | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_dynaruleitem（主表） | 31 |
| t_hrcs_dynaruleitemenum（枚举值单据体） | 2 |
| 无数据库列 | 2 |

