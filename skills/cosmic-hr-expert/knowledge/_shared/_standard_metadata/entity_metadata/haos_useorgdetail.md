# haos_useorgdetail — 使用组织明细

**表单编码**: `haos_useorgdetail`  
**表单ID**: `2IXRXZ37HO/M`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_useorgdetail（使用组织明细） [BaseEntity]

- **数据库表**: `t_haos_useorgdetail`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| staff | 编制规划内码 | BasedataField | fid |  | haos_staff |
| vid | 历史最新版本 | BasedataField | fvid |  | haos_useorgdetailhis |
| dutyorg | 所属责任组织 | HRAdminOrgField | fdutyorgid |  | haos_adminorghrf7 |
| controlstrategy | 默认控制策略 | ComboField | fcontrolstrategy |  |  |
| elasticcontrol | 弹性控编方式 | ComboField | felasticcontrol |  |  |
| elasticcount | 弹性比例/人数 | IntegerField | felasticcount |  |  |
| leffdt | 失效日期 | DateField | fleffdt |  |  |
| effdt | 生效日期 | DateField | feffdt |  |  |
| datastatus | 数据状态 | ComboField | fdatastatus |  |  |
| sequence | 分录顺序号 | IntegerField | fseq |  |  |
| staffdimension | 编制维度 | MulBasedataField | t_haos_uosdimension（子表） |  |  |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| pid | 上级主键 | BigIntField | fparententryid |  |  |
| useorg | 使用组织 | HRAdminOrgField | fuseorgid |  | haos_adminorghrf7 |
| useorgbo | 使用组织BO | BigIntField | fuseorgboid |  |  |
| bo | 业务id | BigIntField | fboid |  |  |
| halfyearstaff2 | 下半年编制 | IntegerField | fhalfyearstaff2 |  |  |
| yearstaff | 年编制 | IntegerField | fyearstaff |  |  |
| halfyearstaff1 | 上半年编制 | IntegerField | fhalfyearstaff1 |  |  |
| quarterstaff4 | 第四季度编制 | IntegerField | fquarterstaff4 |  |  |
| quarterstaff1 | 第一季度编制 | IntegerField | fquarterstaff1 |  |  |
| quarterstaff2 | 第二季度编制 | IntegerField | fquarterstaff2 |  |  |
| quarterstaff3 | 第三季度编制 | IntegerField | fquarterstaff3 |  |  |
| monthstaff4 | 四月份编制 | IntegerField | fmonthstaff4 |  |  |
| monthstaff1 | 一月份编制 | IntegerField | fmonthstaff1 |  |  |
| monthstaff2 | 二月份编制 | IntegerField | fmonthstaff2 |  |  |
| monthstaff3 | 三月份编制 | IntegerField | fmonthstaff3 |  |  |
| monthstaff12 | 十二月份编制 | IntegerField | fmonthstaff12 |  |  |
| monthstaff5 | 五月份编制 | IntegerField | fmonthstaff5 |  |  |
| monthstaff6 | 六月份编制 | IntegerField | fmonthstaff6 |  |  |
| monthstaff7 | 七月份编制 | IntegerField | fmonthstaff7 |  |  |
| monthstaff8 | 八月份编制 | IntegerField | fmonthstaff8 |  |  |
| monthstaff9 | 九月份编制 | IntegerField | fmonthstaff9 |  |  |
| monthstaff10 | 十月份编制 | IntegerField | fmonthstaff10 |  |  |
| monthstaff11 | 十一月份编制 | IntegerField | fmonthstaff11 |  |  |

