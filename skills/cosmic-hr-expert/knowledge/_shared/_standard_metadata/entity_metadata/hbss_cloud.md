# hbss_cloud — HR领域云

**表单编码**: `hbss_cloud`  
**表单ID**: `2/BQZ+XCJYVV`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_cloud（HR领域云） [BaseEntity]

- **数据库表**: `t_hbss_cloud`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| cloud | 云 | BasedataField | fcloudid | ✓ | bos_devportal_bizcloud |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| index | 排序号 | IntegerField | findex |  |  |

