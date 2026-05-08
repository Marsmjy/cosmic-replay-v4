# hrpi_perprotitle — 职称信息

**表单编码**: `hrpi_perprotitle`  
**表单ID**: `15BJTMZ+WPM7`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_perprotitle（职称信息） [BaseEntity]

- **数据库表**: `t_hrpi_perprotitle`  

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
| professional | 职称 | BasedataField | fprofessionalid |  | hbss_protitle |
| prolevel | 职称级别 | BasedataField | fprolevelid |  | hbss_protitlelevel |
| awardtime | 授予日期 | DateField | fawardtime |  |  |
| ishigh | 最高职称 | CheckBoxField | fishigh |  |  |
| iscompany | 公司聘任职称 | CheckBoxField | fiscompany |  |  |
| firsttime | 第一次复审日期 | DateField | ffirsttime |  |  |
| secondtime | 第二次复审日期 | DateField | fsecondtime |  |  |
| details | 职称内容 | MuliLangTextField | fdetails |  |  |
| unit | 评定单位 | MuliLangTextField | funit |  |  |
| office | 发证机关 | MuliLangTextField | foffice |  |  |
| certiicateid | 证书编号 | MuliLangTextField | fcertiicateid |  |  |
| approvnum | 批准文号 | MuliLangTextField | fapprovnum |  |  |

