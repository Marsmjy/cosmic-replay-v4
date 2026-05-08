# hrpi_rsmpatinv — 专利发明

**表单编码**: `hrpi_rsmpatinv`  
**表单ID**: `1WBLHXLECOIB`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_rsmpatinv（专利发明） [BaseEntity]

- **数据库表**: `t_hrpi_rsmpatinv`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| issingle | 单行显示 | CheckBoxField | fissingle |  |  |
| patentname | 专利名称 | MuliLangTextField | fpatentname |  |  |
| patentcategoryid | 专利类别 | BasedataField | fpatentcategoryid |  | hbss_patentscategory |
| applicationdate | 申请日期 | DateField | fapplicationdate |  |  |
| ptstartdate | 专利期限开始日期 | DateField | fptstartdate |  |  |
| ptendingdate | 专利期限结束日期 | DateField | fptendingdate |  |  |
| patentstatusid | 专利状态 | BasedataField | fpatentstatusid |  | hbss_patentstatus |
| patentdesc | 专利描述 | MuliLangTextField | fpatentdesc |  |  |
| country | 所属国家/地区 | BasedataField | fcountryid |  | bd_country |
| patentnumber | 专利号 | TextField | fpatentnumber |  |  |
| patenturl | 专利URL | TextField | fpatenturl |  |  |
| inventor | 发明人 | MuliLangTextField | finventor |  |  |

