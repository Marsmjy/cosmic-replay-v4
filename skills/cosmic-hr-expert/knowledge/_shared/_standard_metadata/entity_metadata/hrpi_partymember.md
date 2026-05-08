# hrpi_partymember — 党员信息

**表单编码**: `hrpi_partymember`  
**表单ID**: `2ZXIMMX=7I78`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_partymember（党员信息） [BaseEntity]

- **数据库表**: `t_hrpi_partymember`  

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
| joinpartytime | 入党时间 | DateField | fjoinpartytime |  |  |
| recommender | 入党介绍人 | TextField | frecommender |  |  |
| partybranch | 所在党支部 | MuliLangTextField | fpartybranch |  |  |
| duty | 所任职务 | MuliLangTextField | fduty |  |  |
| regulartime | 转正日期 | DateField | fregulartime |  |  |
| regularsituation | 转正情况 | MuliLangTextField | fregularsituation |  |  |
| joinpartybranch | 入党时所在党支部 | MuliLangTextField | fjoinpartybranch |  |  |
| growtype | 发展类型 | MuliLangTextField | fgrowtype |  |  |
| partystanding | 党龄 | DecimalField | fpartystanding |  |  |
| partyduesbase | 缴纳党费基数 | DecimalField | fpartyduesbase |  |  |
| organization | 组织所在单位 | MuliLangTextField | forganization |  |  |
| filemanageunit | 档案管理单位 | MuliLangTextField | ffilemanageunit |  |  |
| stopflow | 已停止流动 | CheckBoxField | fstopflow |  |  |
| prepartytime | 预备党员转正日期 | DateField | fprepartytime |  |  |
| joinleaguetime | 入团时间 | DateField | fjoinleaguetime |  |  |

