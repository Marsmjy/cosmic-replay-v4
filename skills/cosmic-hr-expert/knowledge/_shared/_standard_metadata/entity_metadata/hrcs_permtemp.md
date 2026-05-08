# hrcs_permtemp — 权限模板导入配置

**表单编码**: `hrcs_permtemp`  
**表单ID**: `19OR06QETS9K`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_permtemp（权限模板导入配置） [BaseEntity]

- **数据库表**: `t_hrcs_permtemp`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| number | 模板编码 | TextField | fnumber | ✓ |  |
| handlerclass | 模板处理类 | TextField | fhandlerclass | ✓ |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |

