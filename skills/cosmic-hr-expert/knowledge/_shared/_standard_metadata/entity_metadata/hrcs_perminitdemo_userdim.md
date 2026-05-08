# hrcs_perminitdemo_userdim — 权限初始化用户维度值样例

**表单编码**: `hrcs_perminitdemo_userdim`  
**表单ID**: `35R0+5L7IQOA`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_perminitdemo_userdim（权限初始化用户维度值样例） [BaseEntity]

- **数据库表**: `t_hrcs_pinitdemouserdim`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| usernumber | 用户工号 | TextField | fusernumber |  |  |
| username | 用户名字 | MuliLangTextField | fusername |  |  |
| fileorgnumber | 档案组织编码 | TextField | ffileorgnumber |  |  |
| fileorgname | 档案组织名字 | MuliLangTextField | ffileorgname |  |  |
| rolenumber | 角色编码 | TextField | frolenumber |  |  |
| rolename | 角色名字 | MuliLangTextField | frolename |  |  |
| validstart | 有效开始日期 | DateField | fdatefield |  |  |
| validend | 有效结束日期 | DateField | fvalidend |  |  |
| orgrange | 组织范围 | MuliLangTextField | forgrange |  |  |
| dim_adminorg | 行政组织 | MuliLangTextField | fdimadminorg |  |  |
| dim_projectteam | 项目团队 | MuliLangTextField | fdimprojectteam |  |  |
| dim_salarygroup | 薪资核算组 | MuliLangTextField | fdimsalarygroup |  |  |
| dim_countryarea | 国家地区 | MuliLangTextField | fdimcountryarea |  |  |
| sceneid | 场景ID | IntegerField | fsceneid |  |  |
| scenename | 场景名 | MuliLangTextField | fscenename |  |  |
| scenedesc | 场景描述 | MuliLangTextField | fscenedesc |  |  |
| usernametip | 用户名字tip | MuliLangTextField | fusernametip |  |  |
| usernumbertip | 用户工号tip | MuliLangTextField | fusernumbertip |  |  |
| fileorgnumbertip | 档案组织编码tip | MuliLangTextField | ffileorgnumbertip |  |  |
| fileorgnametip | 档案组织名字tip | MuliLangTextField | ffileorgnametip |  |  |
| rolenumbertip | 角色编码tip | MuliLangTextField | frolenumbertip |  |  |
| rolenametip | 角色名字tip | MuliLangTextField | frolenametip |  |  |
| validstarttip | 有效开始日期tip | MuliLangTextField | fvalidstarttip |  |  |
| validendtip | 有效结束日期tip | MuliLangTextField | fvalidendtip |  |  |
| customenable | 数据范围属性 | MuliLangTextField | fcustomenable |  |  |
| bucafunc | 职能类型 | MuliLangTextField | fbucafunc |  |  |
| customenabletip | 数据范围属性tip | MuliLangTextField | fcustomenabletip |  |  |
| bucafunctip | 职能类型tip | MuliLangTextField | fbucafunctip |  |  |
| orgrangetip | 组织范围tip | MuliLangTextField | forgrangetip |  |  |
| dim_adminorgtip | 行政组织tip | MuliLangTextField | fdimadminorgtip |  |  |
| dim_projectteamtip | 项目团队tip | MuliLangTextField | fdimprojectteamtip |  |  |
| dim_salarygrouptip | 薪资核算组tip | MuliLangTextField | fdimsalarygrouptip |  |  |
| dim_countryareatip | 国家地区tip | MuliLangTextField | fdimcountryareatip |  |  |
| index | 排序号 | IntegerField | findex |  |  |

