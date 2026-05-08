# haos_orgusestaffdetail — 组织占编明细

**表单编码**: `haos_orgusestaffdetail`  
**表单ID**: `2NTWDCYO5Q=L`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_orgusestaffdetail（组织占编明细） [BaseEntity]

- **数据库表**: `t_haos_orgusestaff`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| useorgdetail | 使用组织明细 | BasedataField | fentryid |  | haos_useorgdetail |
| personstaffinfo | 占编员工信息 | BasedataField | fid |  | haos_personstaffinfo |
| status | 状态 | ComboField | fstatus |  |  |
| effdt | 生效日期 | DateField | feffdt |  |  |
| leffdt | 失效日期 | DateField | fleffdt |  |  |
| stafftype | 占编类型 | ComboField | ftype |  |  |
| useorg | 使用组织 | HRAdminOrgField | fuseorg |  | haos_adminorghrf7 |
| bo | 业务id | BigIntField | fboid |  |  |
| empposorgrel | 任职经历 | BasedataField | fempposorgrelid |  | hrpi_empposorgrel |

