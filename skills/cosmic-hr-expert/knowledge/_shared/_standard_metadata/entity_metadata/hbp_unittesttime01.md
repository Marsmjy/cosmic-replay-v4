# hbp_unittesttime01 — 时间轴单测（不间断不重叠）

**表单编码**: `hbp_unittesttime01`  
**表单ID**: `50UQZ5=7GTUS`  
**归属**: HR基础服务云 / HR基础平台  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbp_unittesttime01（时间轴单测（不间断不重叠）） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbp_unittesttime01` | BaseEntity | 主表 |
| `t_hbp_uttimeentry` | EntryEntity | 单据体 |
| `t_hbp_uttsubentry` | SubEntryEntity | 子单据体 |

### 字段列表 — t_hbp_unittesttime01（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hbp_unittesttime01.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hbp_unittesttime01.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hbp_unittesttime01.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hbp_unittesttime01.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hbp_unittesttime01.finitdatasource |  |  |
| isdeleted | 是否已删除 | CheckBoxField | — |  |  |
| iscurrentdata | 是否当前数据 | CheckBoxField | t_hbp_unittesttime01.fiscurrentdata |  |  |
| startdate | 开始日期 | DateField | t_hbp_unittesttime01.fstartdate | ✓ |  |
| enddate | 结束日期 | DateField | t_hbp_unittesttime01.fenddate | ✓ |  |
| number | 编码 | TextField | t_hbp_unittesttime01.fnumber |  |  |
| college | 高等院校 | BasedataField | t_hbp_unittesttime01.fcollege |  | hbss_college |
| timeline01 | 时间轴测试01 | BasedataField | t_hbp_unittesttime01.ftimeline01 |  | hbp_unittesttime01 |
| name | 名称 | MuliLangTextField | t_hbp_unittesttime01.fname |  |  |
| entryentity | 单据体 | EntryEntity | → t_hbp_uttimeentry |  |  |

### 字段列表 — t_hbp_uttimeentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| textfield | 文本1 | TextField | t_hbp_uttimeentry.ftextfield |  |  |
| integerfield | 整数 | IntegerField | t_hbp_uttimeentry.fintegerfield |  |  |
| subentryentity | 子单据体 | SubEntryEntity | → t_hbp_uttsubentry |  |  |

### 字段列表 — t_hbp_uttsubentry（子单据体·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| textfield1 | 文本2 | TextField | t_hbp_uttsubentry.ftextfield1 |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbp_unittesttime01（主表） | 13 |
| t_hbp_uttimeentry（单据体） | 2 |
| t_hbp_uttsubentry（子单据体） | 1 |
| 无数据库列 | 1 |

