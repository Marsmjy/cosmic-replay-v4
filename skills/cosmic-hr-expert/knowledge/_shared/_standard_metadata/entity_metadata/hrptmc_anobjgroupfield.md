# hrptmc_anobjgroupfield — 分析对象分组赋值

**表单编码**: `hrptmc_anobjgroupfield`  
**表单ID**: `3NFMXIK76=RL`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_anobjgroupfield（分析对象分组赋值） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_angroupfield` | BaseEntity | 主表 |
| `t_hrptmc_groupitem` | EntryEntity | 分组 |

### 字段列表 — t_hrptmc_angroupfield（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_angroupfield.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_angroupfield.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_angroupfield.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_angroupfield.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_angroupfield.finitdatasource |  |  |
| name | 名称 | MuliLangTextField | t_hrptmc_angroupfield.fname |  |  |
| number | 编码 | TextField | t_hrptmc_angroupfield.fnumber |  |  |
| anobjfield | 参照实体字段 | BasedataField | t_hrptmc_angroupfield.fanobjfield |  | hrptmc_anobjqueryfield |
| anobj | 分析对象 | BasedataField | t_hrptmc_angroupfield.fanobjid |  | hrptmc_analyseobject |
| type | 参照字段类型 | ComboField | t_hrptmc_angroupfield.ftype |  |  |
| anobjcalfield | 参照计算字段 | BasedataField | t_hrptmc_angroupfield.fanobjcalfield |  | hrptmc_calculatefield |
| index | 排序号 | IntegerField | t_hrptmc_angroupfield.findex |  |  |
| group | 分组 | EntryEntity | → t_hrptmc_groupitem |  |  |

### 字段列表 — t_hrptmc_groupitem（分组·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| itemname | 分组项名称 | MuliLangTextField | t_hrptmc_groupitem.fitemname |  |  |
| itemnumber | 分组项编码 | TextField | t_hrptmc_groupitem.fitemnumber |  |  |
| itemcondition | 分组项条件 | TextField | t_hrptmc_groupitem.fitemcondition |  |  |
| isungroupitem | 是否未分组项 | CheckBoxField | t_hrptmc_groupitem.fisungroupitem |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_angroupfield（主表） | 12 |
| t_hrptmc_groupitem（分组） | 4 |

