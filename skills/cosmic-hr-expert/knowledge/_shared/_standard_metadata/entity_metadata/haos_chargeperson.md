# haos_chargeperson — 部门负责人

**表单编码**: `haos_chargeperson`  
**表单ID**: `4V9J2/6AQJJH`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_chargeperson（部门负责人） [BaseEntity]

- **数据库表**: `t_haos_chargeperson`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| leffdt | 取消日期 | DateField | fleffdt | ✓ |  |
| effdt | 生效日期 | DateField | feffdt | ✓ |  |
| datastatus | 负责人状态 | ComboField | fdatastatus |  |  |
| empposorgrel | 负责人 | BasedataField | fempposorgrelid |  | hrpi_empposorgrel |
| changesource | 变动来源 | BasedataField | fchangesourceid |  | haos_changesource |
| isagencycharge | 是否代理负责人 | ComboField | fisagencycharge |  |  |
| adminorg | 行政组织 | HRAdminOrgField | fadminorgid |  | haos_adminorghrf7 |
| open_info | 查看详情 | ComboField | — |  |  |
| employee | 人员 | BasedataField | iscurrentversion |  | hrpi_employee |
| position | 岗位 | HRPositionField | iscurrentversion |  | hbpm_positionhrf7 |
| job | 职位 | BasedataField | iscurrentversion |  | hbjm_jobhr |
| removetip | 组织名称 | TextField | — |  |  |
| explain | 备注 | MuliLangTextField | fexplain |  |  |
| depadminorg | 任职部门行政组织 | HRAdminOrgField | iscurrentversion |  | haos_adminorghrf7 |
| ismain | 是否主要负责人 | ComboField | fismain | ✓ |  |
| mulemployee | 人员 | MulEmployeeField | — | ✓ |  |
| headsculpture | 头像 | PictureField | — |  |  |

