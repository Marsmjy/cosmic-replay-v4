# hrcs_roleinitdemo_dr — 角色初始化角色数据范围样例

**表单编码**: `hrcs_roleinitdemo_dr`  
**表单ID**: `3FXGFAKRBZ8X`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_roleinitdemo_dr（角色初始化角色数据范围样例） [BaseEntity]

- **数据库表**: `t_hrcs_roleinitdemo_dr`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| sceneid | 场景ID | IntegerField | fsceneid |  |  |
| scenename | 场景名 | MuliLangTextField | fscenename |  |  |
| scenedesc | 场景描述 | MuliLangTextField | fscenedesc |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| rolenumber | 角色编码 | TextField | frolenumber |  |  |
| rolename | 角色名字 | MuliLangTextField | frolename |  |  |
| bucafuncname | 职能类型 | TextField | fbucafuncname |  |  |
| orgrange | 业务组织范围 | MuliLangTextField | forgrange |  |  |
| adminorg | 行政架构 | MuliLangTextField | fadminorg |  |  |
| projectteam | 项目团队架构 | MuliLangTextField | fprojectteam |  |  |
| salarygroup | 薪资核算组 | MuliLangTextField | fsalarygroup |  |  |
| countryarea | 国家地区 | MuliLangTextField | fcountryarea |  |  |
| servicetpl | 业务数据模板 | MuliLangTextField | fservicetpl |  |  |
| orgrangetip | 业务组织范围tip | MuliLangTextField | forgrangetip |  |  |
| adminorgtip | 行政架构tip | MuliLangTextField | fadminorgtip |  |  |

