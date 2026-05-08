# hrcs_esconfig — ES环境配置

**表单编码**: `hrcs_esconfig`  
**表单ID**: `2R2FVDAI8DPD`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_esconfig（ES环境配置） [BaseEntity]

- **数据库表**: `t_hrcs_esconfig`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| ip | IP地址 | TextField | fip | ✓ |  |
| username | 用户名 | TextField | fusername | ✓ |  |
| password | 密码 | TextField | fpassword | ✓ |  |
| port | 端口号 | IntegerField | fport | ✓ |  |

