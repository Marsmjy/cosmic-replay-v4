# haos_remainstafflist — 组织编制使用情况

**表单编码**: `haos_remainstafflist`  
**表单ID**: `2ZXK3QBM208=`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_remainstafflist（组织编制使用情况） [BaseEntity]

- **数据库表**: `t_haos_emptyremainstaff`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| adminorg | 行政组织 | HRAdminOrgField | fadminorgid |  | haos_adminorghrf7 |
| stafftype | 空余编制类型 | ComboField | fstafftype |  |  |
| staffcount | 编制人数 | IntegerField | fstaffcount |  |  |
| occupycount | 占编人数 | IntegerField | foccupycount |  |  |
| remaincount | 空余编制 | IntegerField | fremaincount |  |  |
| parentorglongname | 行政组织长名称 | TextField | fparentorglongname |  |  |
| intranreleasestaffcount | 流程中待释放人数 | IntegerField | fintranreleasestaffcount |  |  |
| intranholdstaffcount | 流程中占编人数 | IntegerField | fintranholdstaffcount |  |  |
| intranentrystaffcount | 在途占用（入职） | IntegerField | fintranentrystaffcount |  |  |
| intraninstaffcount | 在途占用（调入） | IntegerField | fintraninstaffcount |  |  |
| intranoutstaffcount | 在途释放（调出） | IntegerField | fintranoutstaffcount |  |  |
| intrandepartstaffcount | 在途释放（离职） | IntegerField | fintrandepartstaffcount |  |  |

