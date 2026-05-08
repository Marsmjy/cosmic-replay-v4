# haos_othemproleorgrel — 其他形态组织-人员角色关系

**表单编码**: `haos_othemproleorgrel`  
**表单ID**: `5COAPP7SHYGF`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_othemproleorgrel（其他形态组织-人员角色关系） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_haos_othemproleorgrel` | BaseEntity | 主表 |
| `（虚拟分录）` | EntryEntity | 其他任职信息 |

### 字段列表 — t_haos_othemproleorgrel（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_haos_othemproleorgrel.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_haos_othemproleorgrel.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_haos_othemproleorgrel.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_haos_othemproleorgrel.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_haos_othemproleorgrel.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_haos_othemproleorgrel.finitdatasource |  |  |
| number | 编码 | TextField | t_haos_othemproleorgrel.fnumber |  |  |
| businessstatus | 业务状态 | ComboField | t_haos_othemproleorgrel.fbusinessstatus |  |  |
| otherrole | 角色 | BasedataField | t_haos_othemproleorgrel.fotherroleid | ✓ | haos_othrole |
| ischarge | 是否主负责人 | CheckBoxField | t_haos_othemproleorgrel.fischarge |  |  |
| startdate | 任职开始日期 | DateField | t_haos_othemproleorgrel.fstartdate | ✓ |  |
| enddate | 任职结束日期 | DateField | t_haos_othemproleorgrel.fenddate |  |  |
| index | 排序号 | IntegerField | t_haos_othemproleorgrel.findex |  |  |
| phone | 联系方式 | TextField | t_haos_othemproleorgrel.fphone |  |  |
| isinemployee | 是否企业内员工 | CheckBoxField | t_haos_othemproleorgrel.fisinemployee |  |  |
| employee | 人员 | EmployeeField | t_haos_othemproleorgrel.femployee | ✓ | hrpi_employeenewf7query |
| adminorg | 所属组织 | BasedataField | t_haos_othemproleorgrel.fadminorgid | ✓ | haos_othadminorg |
| gender | 性别 | QueryPropField | — |  |  |
| entryentity | 其他任职信息 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — （其他任职信息·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_othemproleorgrel（主表） | 18 |
| （其他任职信息） | 8 |
| 无数据库列 | 9 |

