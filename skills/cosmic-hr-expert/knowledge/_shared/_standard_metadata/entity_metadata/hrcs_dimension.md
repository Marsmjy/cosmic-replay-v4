# hrcs_dimension — 维度

**表单编码**: `hrcs_dimension`  
**表单ID**: `1=7V+OW3JBX3`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dimension（维度） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_dimension` | BaseEntity | 主表 |
| `t_hrcs_dimensionenum` | EntryEntity | 枚举值 |
| `（虚拟分录）` | EntryEntity | 控权业务对象 |
| `t_hrcs_dimorgclass` | MulEmployeeField子表 | 多选组织分类 |

### 字段列表 — t_hrcs_dimension（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_dimension.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_dimension.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_dimension.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_dimension.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_dimension.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_dimension.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_dimension.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_dimension.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_dimension.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_dimension.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_dimension.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_dimension.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_dimension.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_dimension.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_dimension.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_dimension.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_dimension.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_dimension.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_dimension.foriname |  |  |
| datasource | 类型 | ComboField | t_hrcs_dimension.fdatasource | ✓ |  |
| entitytype | 业务对象 | BasedataField | t_hrcs_dimension.fentitytypeid |  | bos_entityobject |
| authtype | 控权类型 | ComboField | t_hrcs_dimension.fauthtype |  |  |
| isadminorg | 是否行政组织维度 | CheckBoxField | t_hrcs_dimension.fisadminorg |  |  |
| isorg | 是否组织维度 | CheckBoxField | t_hrcs_dimension.fisorg |  |  |
| showtype | 显示类型 | ComboField | t_hrcs_dimension.fshowtype | ✓ |  |
| hrbu | 职能类型 | BasedataField | t_hrcs_dimension.fbucafuncid |  | hbss_hrbucafunc |
| org_classify | 多选组织分类 | MulBasedataField | t_hrcs_dimorgclass（子表） |  |  |
| entry | 枚举值 | EntryEntity | → t_hrcs_dimensionenum |  |  |
| ctrlentry | 控权业务对象 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — t_hrcs_dimensionenum（枚举值·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| value | 枚举值 | TextField | t_hrcs_dimensionenum.fvalue |  |  |
| displayvalue | 枚举名称 | MuliLangTextField | t_hrcs_dimensionenum.fdisplayvalue |  |  |
| entryindex | 顺序 | IntegerField | t_hrcs_dimensionenum.findex |  |  |
| enumdescription | 备注 | MuliLangTextField | t_hrcs_dimensionenum.fdescription |  |  |

### 字段列表 — （控权业务对象·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_dimension（主表） | 27 |
| t_hrcs_dimensionenum（枚举值） | 4 |
| （控权业务对象） | 7 |
| t_hrcs_dimorgclass（MulEmployeeField子表） | 1 |
| 无数据库列 | 2 |

