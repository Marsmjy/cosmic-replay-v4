# hrcs_coordprichgrecord — 协作应用优先处理变动记录

**表单编码**: `hrcs_coordprichgrecord`  
**表单ID**: `4YBFZLWFZ/C+`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_coordprichgrecord（协作应用优先处理变动记录） [BaseEntity]

- **数据库表**: `t_hrcs_coordprichgrecord`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| chgrecordid | 事务变动记录ID | BigIntField | fchgrecordid |  |  |
| index | 顺序号 | IntegerField | findex |  |  |
| scenenumber | 场景 | TextField | fscenenumber |  |  |
| coordappid | 协作应用 | TextField | fcoordappid |  |  |
| employeeid | 员工ID | BigIntField | femployeeid |  |  |

