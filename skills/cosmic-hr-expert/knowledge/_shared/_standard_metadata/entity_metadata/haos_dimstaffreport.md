# haos_dimstaffreport — 编制报表-维度编制

**表单编码**: `haos_dimstaffreport`  
**表单ID**: `426L02UOB7SN`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_dimstaffreport（编制报表-维度编制） [BaseEntity]

- **数据库表**: `t_haos_dimstaffreport`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| staffid | 编制信息维护id | BigIntField | fstaffid |  |  |
| year | 编制年份 | DateTimeField | fyear |  |  |
| month | 编制月份 | DateTimeField | fmonth |  |  |
| adminorg | 行政组织 | HRAdminOrgField | fadminorgid |  | haos_adminorghrf7 |
| staffcount | 编制人数 | IntegerField | fstaffcount |  |  |
| position | 岗位 | HRPositionField | fpositionid |  | hbpm_positionhrf7 |
| job | 职位 | BasedataField | fjobid |  | hbjm_jobhr |
| laborreltype | 用工关系类型 | BasedataField | flaborreltypeid |  | hbss_laborreltype |
| staffdimension | 编制维度 | BasedataField | fstaffdimensionid |  | haos_dynamicdimension |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |

