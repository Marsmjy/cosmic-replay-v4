# hbpm_chgrecorddetail — 岗位变动明细详情分录

**表单编码**: `hbpm_chgrecorddetail`  
**表单ID**: `2W/6SLLQV+JH`  
**归属**: HR基础服务云 / HR基础岗位  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbpm_chgrecorddetail（岗位变动明细详情分录） [BaseEntity]

- **数据库表**: `t_hbpm_chgrecorddetail`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| changeoperate | 变动操作 | BasedataField | fchangeoperateid |  | hbpm_changeoperate |
| changentity | 变动实体标识 | TextField | fchangeentity |  |  |
| propkey | 变动实体字段标识 | TextField | fchangekey |  |  |
| aftervalue | 变动后 | TextField | fafterchange |  |  |
| beforevalue | 变动前 | TextField | fbeforechange |  |  |
| parent | 父实体ID | BigIntField | fentryid |  |  |

