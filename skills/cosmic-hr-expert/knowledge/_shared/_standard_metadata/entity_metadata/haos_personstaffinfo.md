# haos_personstaffinfo — 占编员工信息

**表单编码**: `haos_personstaffinfo`  
**表单ID**: `2NQCH2IUH6JR`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_personstaffinfo（占编员工信息） [BaseEntity]

- **数据库表**: `t_haos_staffperson`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| candidate | 候选人 | BasedataField | fcandidateid |  | hcf_candidate |
| employee | 员工 | BasedataField | femployeeid |  | hrpi_employee |
| leffdt | 失效日期 | DateField | fleffdt |  |  |
| effdt | 生效日期 | DateField | feffdt |  |  |
| status | 状态 | ComboField | fstatus |  |  |

