# hrss_schobjentityrel — 搜索对象实体关联关系

**表单编码**: `hrss_schobjentityrel`  
**表单ID**: `3TYSVLA9DZB5`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_schobjentityrel（搜索对象实体关联关系） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrss_schobjrelation` | BaseEntity | 主表 |
| `t_hrss_schobjrelcon` | EntryEntity |  |

### 字段列表 — t_hrss_schobjrelation（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrss_schobjrelation.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrss_schobjrelation.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrss_schobjrelation.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrss_schobjrelation.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrss_schobjrelation.finitdatasource |  |  |
| entityid | 实体ID | BasedataField | t_hrss_schobjrelation.fentityid |  | hrss_schobjjoinentity |
| jointype | 关联类型 | ComboField | t_hrss_schobjrelation.fjointype |  |  |
| joinentityid | 关联实体ID | BasedataField | t_hrss_schobjrelation.fjoinentityid |  | hrss_schobjjoinentity |
| leftprop | 左字段 | TextField | t_hrss_schobjrelation.fleftprop |  |  |
| comparetype | 比较条件 | ComboField | t_hrss_schobjrelation.fcomparetype |  |  |
| rightprop | 右字段 | TextField | t_hrss_schobjrelation.frightprop |  |  |
| rightproptype | 右字段类型 | ComboField | t_hrss_schobjrelation.frightproptype |  |  |
| rightpropval | 右字段值 | TextField | t_hrss_schobjrelation.frightpropval |  |  |
| logictype | 逻辑条件 | ComboField | t_hrss_schobjrelation.flogictype |  |  |
| searchobj | 搜索对象 | BasedataField | t_hrss_schobjrelation.fsearchobjid |  | hrss_searchobject |
|  |  | EntryEntity | → t_hrss_schobjrelcon |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrss_schobjrelation（主表） | 15 |

