# hrcs_lblentityrelation — 标签实体关联关系

**表单编码**: `hrcs_lblentityrelation`  
**表单ID**: `2VK06PP=HP6O`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_lblentityrelation（标签实体关联关系） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_lblentityrelation` | BaseEntity | 主表 |
| `t_hrcs_lblentityrelentry` | EntryEntity |  |

### 字段列表 — t_hrcs_lblentityrelation（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_lblentityrelation.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_lblentityrelation.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_lblentityrelation.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_lblentityrelation.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_lblentityrelation.finitdatasource |  |  |
| entityid | 左关联实体ID | BasedataField | t_hrcs_lblentityrelation.fentityid |  | hrcs_lbljoinentity |
| jointype | 关联类型 | ComboField | t_hrcs_lblentityrelation.fjointype |  |  |
| joinentityid | 右关联实体ID | BasedataField | t_hrcs_lblentityrelation.fjoinentityid |  | hrcs_lbljoinentity |
| leftprop | 左字段 | TextField | t_hrcs_lblentityrelation.fleftprop |  |  |
| comparetype | 比较条件 | ComboField | t_hrcs_lblentityrelation.fcomparetype |  |  |
| rightprop | 右字段 | TextField | t_hrcs_lblentityrelation.frightprop |  |  |
| rightproptype | 右字段类型 | ComboField | t_hrcs_lblentityrelation.frightproptype |  |  |
| rightpropval | 右字段值 | TextField | t_hrcs_lblentityrelation.frightpropval |  |  |
| logictype | 逻辑条件 | ComboField | t_hrcs_lblentityrelation.flogictype |  |  |
| labelobject | 打标对象 | BasedataField | t_hrcs_lblentityrelation.flabelobjectid |  | hrcs_labelobject |
| radiofield | 1:1 | RadioField | — |  |  |
| relationtype | 关系类型 | RadioGroupField | t_hrcs_lblentityrelation.frelationtype |  |  |
| radiofield1 | N:1 | RadioField | — |  |  |
| radiofield2 | 1:N | RadioField | — |  |  |
| displayname | 关联对象显示名称 | MuliLangTextField | t_hrcs_lblentityrelation.fdisplayname |  |  |
|  |  | EntryEntity | → t_hrcs_lblentityrelentry |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_lblentityrelation（主表） | 20 |
| 无数据库列 | 3 |

