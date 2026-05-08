# hrptmc_bizsortentity — 业务排序实体信息

**表单编码**: `hrptmc_bizsortentity`  
**表单ID**: `36J7UPTJZ2IL`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_bizsortentity（业务排序实体信息） [BaseEntity]

- **数据库表**: `t_hrptmc_bizsortentity`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| sortentity | 排序实体 | TextField | fsortentity |  |  |
| sorttable | 排序表 | TextField | fsorttable |  |  |
| dbroutekey | 数据库路由 | TextField | fdbroutekey |  |  |
| sorttype | 排序类型 | ComboField | fsorttype |  |  |
| bizcode | 业务云或应用编码 | TextField | fbizcode |  |  |

