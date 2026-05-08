# hbss_scoreinterval — 评分间隔

**表单编码**: `hbss_scoreinterval`  
**表单ID**: `4NUB1KW74BGT`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_scoreinterval（评分间隔） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbss_scoreinterval` | BaseEntity | 主表 |
| `t_hbss_scoreentry` | EntryEntity | 单据体 |

### 字段列表 — t_hbss_scoreinterval（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hbss_scoreinterval.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hbss_scoreinterval.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hbss_scoreinterval.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hbss_scoreinterval.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hbss_scoreinterval.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_hbss_scoreinterval.finitdatasource |  |  |
| name | 名称 | MuliLangTextField | t_hbss_scoreinterval.fname | ✓ |  |
| gears | 档位数 | IntegerField | t_hbss_scoreinterval.fgears | ✓ |  |
| minvalue | 最小值 | DecimalField | t_hbss_scoreinterval.fminvalue | ✓ |  |
| maxvalue | 最大值 | DecimalField | t_hbss_scoreinterval.fmaxvalue | ✓ |  |
| scoreinfo | 分数间隔信息 | TextField | t_hbss_scoreinterval.fscoreinfo |  |  |
| number | 编码 | TextField | t_hbss_scoreinterval.fnumber |  |  |
| entryentity | 单据体 | EntryEntity | → t_hbss_scoreentry |  |  |

### 字段列表 — t_hbss_scoreentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| score | 分值 | DecimalField | t_hbss_scoreentry.fscore | ✓ |  |
| statement | 分值说明 | MuliLangTextField | t_hbss_scoreentry.fstatement |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_scoreinterval（主表） | 12 |
| t_hbss_scoreentry（单据体） | 2 |

