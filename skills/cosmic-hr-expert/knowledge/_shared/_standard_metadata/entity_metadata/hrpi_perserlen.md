# hrpi_perserlen — 服务年限

**表单编码**: `hrpi_perserlen`  
**表单ID**: `15AZ4444SCI6`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_perserlen（服务年限） [BaseEntity]

- **数据库表**: `t_hrpi_perserlen`  

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
| startdate | 司龄开始日期 | DateField | fstartdate | ✓ |  |
| enddate | 司龄结束日期 | DateField | fenddate | ✓ |  |
| assignment | 组织分配 | BasedataField | fassignmentid | ✓ | hrpi_assignment |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |
| firstjoincomdate | 首次加入集团日期 | DateField | ffirstjoincomdate | ✓ |  |
| joincomdate | 本次加入集团日期 | DateField | fjoincomdate | ✓ |  |
| adjustcomtime | 调整集团服务年限 | DecimalField | fadjustcomtime |  |  |
| comsercount | 集团服务年限 | DecimalField | fcomsercount |  |  |
| joinworktime | 参加工作日期 | DateField | fjoinworktime |  |  |
| adjustworktime | 调整工作年限 | DecimalField | fadjustworktime |  |  |
| workyear | 参加工作年限 | DecimalField | fworkyear |  |  |
| socialworkage | 工龄年限 | DecimalField | fsocialworkage |  |  |
| adjustworkage | 调整工龄 | DecimalField | fadjustworkage |  |  |
| comserviceadjyear | 调整司龄年限 | DecimalField | fcomserviceadjyear |  |  |
| comserviceyear | 司龄服务年限 | DecimalField | fcomserviceyear |  |  |
| iscontinueserviceyear | 延续司龄 | CheckBoxField | fiscontinueserviceyear |  |  |

