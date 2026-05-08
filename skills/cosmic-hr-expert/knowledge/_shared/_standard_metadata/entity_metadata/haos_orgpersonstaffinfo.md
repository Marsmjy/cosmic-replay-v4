# haos_orgpersonstaffinfo — 占编员工维度信息

**表单编码**: `haos_orgpersonstaffinfo`  
**表单ID**: `2NQDPZ59+IK1`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_orgpersonstaffinfo（占编员工维度信息） [BaseEntity]

- **数据库表**: `t_haos_staffdimperson`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| candidate | 候选人 | BasedataField | fcandidateid |  | hcf_candidate |
| orgteam | 组织团队 | HRAdminOrgField | forgteamid |  | haos_adminorghrf7 |
| position | 岗位 | HRPositionField | fpositionid |  | hbpm_positionhrf7 |
| job | 职位 | BasedataField | fjobid |  | hbjm_jobhr |
| joblevel | 职级 | BasedataField | fjoblevelid |  | hbjm_joblevelhr |
| leffdt | 失效日期 | DateField | fleffdt |  |  |
| effdt | 生效日期 | DateField | feffdt |  |  |
| personstaffinfo | 占编员工信息内码 | BasedataField | fid |  | haos_personstaffinfo |
| status | 状态 | ComboField | fstatus |  |  |
| laborreltype | 用工关系类型 | BasedataField | flaborreltypeid |  | hbss_laborreltype |
| flexdimvalue | 弹性域维度值 | TextAreaField | fflexdimvalue |  |  |
| empposorgrel | 任职经历 | BasedataField | fempposorgrelid |  | hrpi_empposorgrel |
| employee | 员工 | BasedataField | femployeeid |  | hrpi_employee |

