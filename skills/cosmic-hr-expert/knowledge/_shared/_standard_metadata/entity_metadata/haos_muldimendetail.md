# haos_muldimendetail — 多维控编明细

**表单编码**: `haos_muldimendetail`  
**表单ID**: `2IXS84C67LVF`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_muldimendetail（多维控编明细） [BaseEntity]

- **数据库表**: `t_haos_muldimendetail`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| vid | 历史最新版本 | BasedataField | fvid |  | haos_muldimendetailhis |
| staff | 编制规划内码 | BasedataField | fid |  | haos_staff |
| useorg | 使用组织 | HRAdminOrgField | fuseorgid |  | haos_adminorghrf7 |
| controlstrategy | 默认控制策略 | ComboField | fcontrolstrategy |  |  |
| elasticcontrol | 弹性控编方式 | ComboField | felasticcontrol |  |  |
| elasticcount | 弹性比例/人数 | IntegerField | felasticcount |  |  |
| leffdt | 失效日期 | DateField | fleffdt |  |  |
| effdt | 生效日期 | DateField | feffdt |  |  |
| datastatus | 数据状态 | ComboField | fdatastatus |  |  |
| sequence | 分录顺序号 | IntegerField | fseq |  |  |
| orgteam | 组织团队 | HRAdminOrgField | forgteamid |  | haos_adminorghrf7 |
| position | 岗位 | HRPositionField | fpositionid |  | hbpm_positionhrf7 |
| job | 职位 | BasedataField | fjobid |  | hbjm_jobhr |
| joblevel | 职级 | BasedataField | fjoblevelid |  | hbjm_joblevelhr |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| laborreltype | 用工关系类型 | BasedataField | flaborreltypeid |  | hbss_laborreltype |
| bo | 业务id | BigIntField | fboid |  |  |
| yearstaff | 年编制 | IntegerField | fyearstaff |  |  |
| halfyearstaff1 | 上半年编制 | IntegerField | fhalfyearstaff1 |  |  |
| halfyearstaff2 | 下半年编制 | IntegerField | fhalfyearstaff2 |  |  |
| quarterstaff1 | 第一季度编制 | IntegerField | fquarterstaff1 |  |  |
| quarterstaff2 | 第二季度编制 | IntegerField | fquarterstaff2 |  |  |
| quarterstaff3 | 第三季度编制 | IntegerField | fquarterstaff3 |  |  |
| quarterstaff4 | 第四季度编制 | IntegerField | fquarterstaff4 |  |  |
| monthstaff1 | 一月份编制 | IntegerField | fmonthstaff1 |  |  |
| monthstaff2 | 二月份编制 | IntegerField | fmonthstaff2 |  |  |
| monthstaff3 | 三月份编制 | IntegerField | fmonthstaff3 |  |  |
| monthstaff4 | 四月份编制 | IntegerField | fmonthstaff4 |  |  |
| monthstaff5 | 五月份编制 | IntegerField | fmonthstaff5 |  |  |
| monthstaff6 | 六月份编制 | IntegerField | fmonthstaff6 |  |  |
| monthstaff7 | 七月份编制 | IntegerField | fmonthstaff7 |  |  |
| monthstaff8 | 八月份编制 | IntegerField | fmonthstaff8 |  |  |
| monthstaff9 | 九月份编制 | IntegerField | fmonthstaff9 |  |  |
| monthstaff10 | 十月份编制 | IntegerField | fmonthstaff10 |  |  |
| monthstaff11 | 十一月份编制 | IntegerField | fmonthstaff11 |  |  |
| monthstaff12 | 十二月份编制 | IntegerField | fmonthstaff12 |  |  |
| entrydimtype | 编制维度 | BasedataField | fentrydimtypeid |  | haos_dynamicdimension |
| flexdim1 | 维度 | FlexField | fflexdim1 |  |  |
| flexdim2 | 维度 | FlexField | fflexdim2 |  |  |
| flexdim3 | 维度 | FlexField | fflexdim3 |  |  |
| flexdim4 | 维度 | FlexField | fflexdim4 |  |  |
| flexdim5 | 维度 | FlexField | fflexdim5 |  |  |
| flexdim6 | 维度 | FlexField | fflexdim6 |  |  |
| flexdim7 | 维度 | FlexField | fflexdim7 |  |  |
| flexdim8 | 维度 | FlexField | fflexdim8 |  |  |
| flexdim9 | 维度 | FlexField | fflexdim9 |  |  |
| flexdim10 | 维度 | FlexField | fflexdim10 |  |  |
| flexdim11 | 维度 | FlexField | fflexdim11 |  |  |
| flexdim12 | 维度 | FlexField | fflexdim12 |  |  |
| flexdim13 | 维度 | FlexField | fflexdim13 |  |  |
| flexdim14 | 维度 | FlexField | fflexdim14 |  |  |
| flexdim15 | 维度 | FlexField | fflexdim15 |  |  |
| flexdim16 | 维度 | FlexField | fflexdim16 |  |  |
| flexdim17 | 维度 | FlexField | fflexdim17 |  |  |
| flexdim18 | 维度 | FlexField | fflexdim18 |  |  |
| flexdim19 | 维度 | FlexField | fflexdim19 |  |  |
| flexdim20 | 维度 | FlexField | fflexdim20 |  |  |

