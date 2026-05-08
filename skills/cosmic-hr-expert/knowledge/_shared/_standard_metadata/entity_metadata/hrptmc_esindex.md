# hrptmc_esindex — es索引

**表单编码**: `hrptmc_esindex`  
**表单ID**: `48P9JW=6B6Z+`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_esindex（es索引） [BaseEntity]

- **数据库表**: `t_hrptmc_esindex`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 生成时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| status | 索引状态 | ComboField | fstatus |  |  |
| basedataesindex | 基础资料索引 | TextField | fbasedataesindex |  |  |

