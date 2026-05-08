# hrcs_activitygroupins — 活动组实例

**表单编码**: `hrcs_activitygroupins`  
**表单ID**: `38F6U0=Y2N1+`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_activitygroupins（活动组实例） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_activitygroupins` | BaseEntity | 主表 |
| `t_hrcs_activigpinsentity` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_activitygroupins（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_activitygroupins.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_activitygroupins.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_activitygroupins.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_activitygroupins.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_activitygroupins.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_activitygroupins.finitdatasource |  |  |
| bizbillid | 业务单据 | BigIntField | t_hrcs_activitygroupins.fbizbillid |  |  |
| activitygroupid | 活动分组 | BigIntField | t_hrcs_activitygroupins.factivitygroupid |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrcs_activigpinsentity |  |  |

### 字段列表 — t_hrcs_activigpinsentity（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| activity | 活动 | BasedataField | t_hrcs_activigpinsentity.factivityid | ✓ | hrcs_activity |
| activitytype | 活动类型 | ComboField | t_hrcs_activigpinsentity.factivitytype |  |  |
| status | 活动实例状态 | ComboField | t_hrcs_activigpinsentity.fstatus |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_activitygroupins（主表） | 8 |
| t_hrcs_activigpinsentity（单据体） | 3 |

