# hrcs_warnsceneentityrel — 预警对象实体关联关系

**表单编码**: `hrcs_warnsceneentityrel`  
**表单ID**: `3V/YCZCAOP1=`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnsceneentityrel（预警对象实体关联关系） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_warnobjectrelation` | BaseEntity | 主表 |
| `t_hrcs_warnobjectrelcon` | EntryEntity |  |

### 字段列表 — t_hrcs_warnobjectrelation（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_warnobjectrelation.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_warnobjectrelation.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_warnobjectrelation.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_warnobjectrelation.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_warnobjectrelation.finitdatasource |  |  |
| entityid | 实体ID | BasedataField | t_hrcs_warnobjectrelation.fentityid |  | hrcs_warnscenejoinentity |
| jointype | 关联类型 | ComboField | t_hrcs_warnobjectrelation.fjointype |  |  |
| joinentityid | 关联实体ID | BasedataField | t_hrcs_warnobjectrelation.fjoinentityid |  | hrcs_warnscenejoinentity |
| leftprop | 左字段 | TextField | t_hrcs_warnobjectrelation.fleftprop |  |  |
| comparetype | 比较条件 | ComboField | t_hrcs_warnobjectrelation.fcomparetype |  |  |
| rightprop | 右字段 | TextField | t_hrcs_warnobjectrelation.frightprop |  |  |
| rightproptype | 右字段类型 | ComboField | t_hrcs_warnobjectrelation.frightproptype |  |  |
| rightpropval | 右字段值 | TextField | t_hrcs_warnobjectrelation.frightpropval |  |  |
| logictype | 逻辑条件 | ComboField | t_hrcs_warnobjectrelation.flogictype |  |  |
| source | 字段来源 | ComboField | t_hrcs_warnobjectrelation.fsource |  |  |
| sourceid | 引用来源id | BigIntField | t_hrcs_warnobjectrelation.fsourceid |  |  |
|  |  | EntryEntity | → t_hrcs_warnobjectrelcon |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_warnobjectrelation（主表） | 16 |

