# hrpi_fertilityinfo — 生育信息

**表单编码**: `hrpi_fertilityinfo`  
**表单ID**: `1V747XRLVAK9`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_fertilityinfo（生育信息） [BaseEntity]

- **数据库表**: `t_hrpi_fertilityinfo`  

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
| birthday | 出生日期 | DateField | fbirthday |  |  |
| childrennumber | 本次生育胎儿数 | IntegerField | fchildrennumber |  |  |
| batchno | 批次号 | TextField | fbatchno |  |  |
| procreatmode | 生育方式 | BasedataField | fprocreatmode |  | hbss_procreatmode |

