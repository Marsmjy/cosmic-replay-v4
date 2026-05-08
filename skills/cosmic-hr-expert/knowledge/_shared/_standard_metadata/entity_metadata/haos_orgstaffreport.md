# haos_orgstaffreport — 编制报表-组织编制

**表单编码**: `haos_orgstaffreport`  
**表单ID**: `426KZUQHHDNK`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_orgstaffreport（编制报表-组织编制） [BaseEntity]

- **数据库表**: `t_haos_orgstaffreport`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| staffid | 编制信息维护id | BigIntField | fstaffid |  |  |
| year | 编制年份 | DateTimeField | fyear |  |  |
| month | 编制月份 | DateTimeField | fmonth |  |  |
| adminorg | 行政组织 | HRAdminOrgField | fadminorgid |  | haos_adminorghrf7 |
| staffcount | 直属编制人数 | IntegerField | fstaffcount |  |  |
| staffcountwithsub | 含下级编制人数 | IntegerField | fstaffcountwithsub |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |

