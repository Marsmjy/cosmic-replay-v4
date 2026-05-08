# hbss_scoresystem — 评分分制

**表单编码**: `hbss_scoresystem`  
**表单ID**: `2PWB17A4CCKA`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_scoresystem（评分分制） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbss_scoresystem` | BaseEntity | 主表 |
| `t_hbss_scoresystemtag` | EntryEntity | 评分标签 |
| `t_hbss_systeminterval` | EntryEntity | 评分间隔分录 |

### 字段列表 — t_hbss_scoresystem（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbss_scoresystem.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbss_scoresystem.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbss_scoresystem.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbss_scoresystem.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbss_scoresystem.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbss_scoresystem.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbss_scoresystem.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbss_scoresystem.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbss_scoresystem.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_hbss_scoresystem.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hbss_scoresystem.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbss_scoresystem.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbss_scoresystem.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbss_scoresystem.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbss_scoresystem.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbss_scoresystem.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbss_scoresystem.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbss_scoresystem.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbss_scoresystem.foriname |  |  |
| maxscore | 基准最高分 | DecimalField | t_hbss_scoresystem.fmaxscore | ✓ |  |
| minscore | 基准最低分 | DecimalField | t_hbss_scoresystem.fminscore | ✓ |  |
| intervalbase64 | 评分间隔缓存 | LargeTextField | — |  |  |
| scoreintvalenable | 启用评分间隔 | CheckBoxField | t_hbss_scoresystem.fscoreintvalenable |  |  |
| tagentryentity | 评分标签 | EntryEntity | → t_hbss_scoresystemtag |  |  |
| systeminterval | 评分间隔分录 | EntryEntity | → t_hbss_systeminterval |  |  |

### 字段列表 — t_hbss_scoresystemtag（评分标签·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| isdefault | 是否作为默认评分 | CheckBoxField | t_hbss_scoresystemtag.fisdefault |  |  |
| tag | 标签 | MuliLangTextField | t_hbss_scoresystemtag.ftag | ✓ |  |
| score | 评分 | DecimalField | t_hbss_scoresystemtag.fscore | ✓ |  |
| comment | 评语 | MuliLangTextField | t_hbss_scoresystemtag.fcomment |  |  |

### 字段列表 — t_hbss_systeminterval（评分间隔分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| scoreinterval | 评分间隔 | BasedataField | t_hbss_systeminterval.fscoreinterval |  | hbss_scoreinterval |
| intervalnameshow | 评分间隔 | TextField | — |  |  |
| scoreinfoshow | 分数间隔信息 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_scoresystem（主表） | 31 |
| t_hbss_scoresystemtag（评分标签） | 4 |
| t_hbss_systeminterval（评分间隔分录） | 3 |
| 无数据库列 | 11 |

