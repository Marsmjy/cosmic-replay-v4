# hrpi_empcadre — 最高干部身份信息

**表单编码**: `hrpi_empcadre`  
**表单ID**: `3KVJ+EQQ4=NA`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_empcadre（最高干部身份信息） [BaseEntity]

- **数据库表**: `t_hrpi_empcadre`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| isdeleted | 已删除 | CheckBoxField | — |  |  |
| iscurrentdata | 当前数据 | CheckBoxField | fiscurrentdata |  |  |
| startdate | 开始日期 | DateField | fstartdate | ✓ |  |
| enddate | 结束日期 | DateField | fenddate | ✓ |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| iscadre | 干部 | CheckBoxField | fiscadre |  |  |
| cadretype | 干部类别 | BasedataField | fcadretypeid | ✓ | hbss_cadrecategory |
| appremoverel | 任免经历 | BasedataField | fappremoverelid |  | hrpi_appointremoverel |
| empposorgrel | 任职经历 | BasedataField | fempposorgrelid |  | hrpi_empposorgrel |

