# hrcs_workingplanallrange — 工作日历适用范围

**表单编码**: `hrcs_workingplanallrange`  
**表单ID**: `23J2C=89YS09`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_workingplanallrange（工作日历适用范围） [BaseEntity]

- **数据库表**: `t_hrcs_workingplanallrang`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| workplace | 工作地 | MulBasedataField | t_hrcs_workingplanplace（子表） |  |  |
| locationall | 全部 | CheckBoxField | flocationall |  |  |
| orgteam | 组织团队 | HRMulAdminOrgField | t_hrcs_workingplanteam（子表） |  |  |
| orgteamall | 全部 | CheckBoxField | forgteamall |  |  |
| legalpersionorg | 法人机构 | MulBasedataField | t_hrcs_workingplanlegal（子表） |  |  |
| legalorgall | 全部 | CheckBoxField | flegalorgall |  |  |
| wpid | 工作日计划ID | BigIntField | fwpid |  |  |

