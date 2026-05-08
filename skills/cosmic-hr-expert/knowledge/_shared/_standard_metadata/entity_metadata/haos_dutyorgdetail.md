# haos_dutyorgdetail — 责任组织明细

**表单编码**: `haos_dutyorgdetail`  
**表单ID**: `2IXTMAKH6JGS`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_dutyorgdetail（责任组织明细） [BaseEntity]

- **数据库表**: `t_haos_dutyorgdetail`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| vid | 历史最新版本 | BasedataField | fvid |  | haos_dutyorgdetailhis |
| staff | 编制规划内码 | BasedataField | fid |  | haos_staff |
| dutyorg | 责任组织 | HRAdminOrgField | fdutyorgid |  | haos_adminorghrf7 |
| staffcount | 编制人数 | IntegerField | fstaffcount |  |  |
| distributable | 可分配人数 | IntegerField | fdistributable |  |  |
| controlstrategy | 默认控制策略 | ComboField | fcontrolstrategy |  |  |
| elasticcontrol | 弹性控编方式 | ComboField | felasticcontrol |  |  |
| elasticcount | 弹性比例/人数 | IntegerField | felasticcount |  |  |
| leffdt | 失效日期 | DateField | fleffdt |  |  |
| effdt | 生效日期 | DateField | feffdt |  |  |
| datastatus | 数据状态 | ComboField | fdatastatus |  |  |
| sequence | 分录顺序号 | IntegerField | fseq |  |  |
| staffdimension | 编制维度 | MulBasedataField | t_haos_dosdimension（子表） |  |  |
| enable | 使用状态 | BillStatusField | fenable |  |  |

