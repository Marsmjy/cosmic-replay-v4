---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 0VO5EV13=I9W
app_number: hcdm
app_name: 薪酬管理
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcdm_applybill — 员工定调薪申请单

**表单编码**: `hcdm_applybill`  
**表单ID**: `4VTM+MO1=8WS`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_applybill（员工定调薪申请单） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_applybill` | 主表 · 32 列 |
| `t_hcdm_applybillent` | 分录表 · 40 列 |
| `t_hcdm_applybill_l` | 多语言表 · 2 列 |
| `t_hcdm_applybillent_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_hcdm_applybill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_hcdm_applybill.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_hcdm_applybill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_applybill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_hcdm_applybill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_hcdm_applybill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_applybill.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_applybill.fcreatetime |  |  |
| org | 薪酬管理组织 | OrgField | t_hcdm_applybill.forgid | ✓ | bos_org |
| barcode | 条形码 | TextField | t_hcdm_applybill.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_hcdm_applybill.finputdevicetype |  |  |
| isexistsworkflow | 存在工作流 | CheckBoxField | t_hcdm_applybill.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_hcdm_applybill.fauditstatus | ✓ |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | t_hcdm_applybill.feventeffectdate |  |  |
| issubmit | 进行过提交(废弃) | CheckBoxField | t_hcdm_applybill.fissubmit |  |  |
| datasource | 申请单数据来源 | ComboField | t_hcdm_applybill.fdatasource |  |  |
| billtype | 定调薪明细字段显示方案 | BillTypeField | — | ✓ | bos_billtype |
| tempeffectivedate | 临时生效日期字段 | DateField | — |  |  |
| adjfileinfoperm | 申请单档案控权字段 | BasedataField | — |  | hcdm_adjfileinfo |
| billname | 单据名称 | MuliLangTextField | t_hcdm_applybill_l.fbillname | ✓ |  |
| totalperson | 定调薪人数 | IntegerField | t_hcdm_applybill.ftotalperson |  |  |
| billid | 单据id | BigIntField | — |  |  |
| confirmperson | 已确认人数 | IntegerField | t_hcdm_applybill.fconfirmperson |  |  |
| salaryadjrsn | 定调薪类型 | BasedataField | t_hcdm_applybill.fsalaryadjrsnid | ✓ | hsbs_salaryadjustrsn |
| effectivedate | 默认生效日期 | DateField | t_hcdm_applybill.feffectivedate | ✓ |  |
| exchangeratedate | 汇率日期 | DateField | t_hcdm_applybill.fexchangeratedate | ✓ |  |
| description | 描述 | MuliLangTextField | t_hcdm_applybill_l.fdescription |  |  |
| exctable | 汇率表 | BasedataField | t_hcdm_applybill.fexctableid | ✓ | bd_exratetable |
| billcurrency | 默认币种 | CurrencyField | — | ✓ | bd_currency |
| salaryadjscm | 定调薪方案 | BasedataField | t_hcdm_applybill.fsalaryadjscmid |  | hcdm_adjapprovescm |
| notifytype | 消息通知方式 | BasedataPropField | — |  |  |
| buttonselect | 定调薪确认页面显示按钮 | MulComboField | t_hcdm_applybill.fbuttonselect |  |  |
| msgtpl | 消息通知模板 | BasedataField | t_hcdm_applybill.fmsgtplid |  | msg_template |
| decadjconfirmtpl | 定调薪确认模板 | BasedataField | t_hcdm_applybill.fdecadjconfirmtplid |  | hcdm_adjconfirmtpl |

## 实体: applybillent（定调薪明细信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| adjfile | 定调薪档案编号 | BasedataField | t_hcdm_applybillent.fadjfileid |  | hcdm_adjfileinfo |
| adjfilev | 定调薪档案版本 | BasedataField | t_hcdm_applybillent.fadjfilevid |  | hcdm_adjfileinfo |
| stdscmv | 薪酬体系版本 | BasedataField | t_hcdm_applybillent.fstdscmvid |  | hcdm_stdscm |
| salarystructure | 薪酬结构 | BasedataField | t_hcdm_applybillent.fsalarystructureid |  | hcdm_salaystructure |
| country | 薪酬管理属地 | BasedataField | t_hcdm_applybillent.fcountryid |  | bd_country |
| empgroup | 定调薪档案分组 | BasedataField | t_hcdm_applybillent.fempgroupid |  | hbss_empgroup |
| amount | 金额 | AmountField | t_hcdm_applybillent.famount | ✓ |  |
| overstandardtype | 超标准 | ComboField | — |  |  |
| frequency | 频度 | BasedataField | t_hcdm_applybillent.ffrequencyid | ✓ | hsbs_calfrequency |
| salarygrade | 薪等 | BasedataField | t_hcdm_applybillent.fsalarygradeid |  | hsbs_salarygrade |
| salaryrank | 薪档 | BasedataField | t_hcdm_applybillent.fsalaryrankid |  | hsbs_salaryrank |
| salarystdv | 薪酬标准表版本 | BasedataField | — |  | hcdm_salarystandard |
| intervalmin | 薪酬标准金额Min | AmountField | — |  |  |
| intervalmax | 薪酬标准金额Max | AmountField | — |  |  |
| graderankrange | 薪等薪档区间 | TextField | — |  |  |
| stdamount | 薪酬标准金额 | AmountField | — |  |  |
| excesscontrol | 超标准控制 | ComboField | — |  |  |
| matchstrategy | 匹配策略 | BasedataField | — |  | hcdm_stdmatchstrategy |
| stdmiddlevalue | 标准中位值 | AmountField | — |  |  |
| presalarystdv | 上一次薪酬标准表版本 | BasedataField | — |  | hcdm_salarystandard |
| presalarygrade | 上一次薪等 | BasedataField | — |  | hsbs_salarygrade |
| presalaryrank | 上一次薪档 | BasedataField | — |  | hsbs_salaryrank |
| precurrency | 上一次币种 | CurrencyField | — |  | bd_currency |
| currency | 币种 | CurrencyField | — | ✓ | bd_currency |
| hcdmorg | 薪酬管理组织 | OrgField | t_hcdm_applybillent.fhcdmorgid |  | bos_org |
| pregraderankshow | 上一次薪等薪档区间 | TextField | t_hcdm_applybillent.fpregraderankshow |  |  |
| preamount | 上一次金额 | AmountField | — |  |  |
| company | 所属公司 | HRAdminOrgField | t_hcdm_applybillent.fcompanyid |  | haos_adminorghrf7 |
| adminorg | 行政组织 | HRAdminOrgField | t_hcdm_applybillent.fadminorgid |  | haos_adminorghrf7 |
| job | 职位 | BasedataField | t_hcdm_applybillent.fjobid |  | hbjm_jobhr |
| position | 岗位 | HRPositionField | — |  | hbpm_positionhrf7 |
| joblevel | 职级 | BasedataField | t_hcdm_applybillent.fjoblevelid |  | hbjm_joblevelhr |
| jobgrade | 职等 | BasedataField | t_hcdm_applybillent.fjobgradeid |  | hbjm_jobgradehr |
| laborreltype | 用工关系类型 | BasedataField | — |  | hbss_laborreltype |
| laborrelstatus | 用工关系状态 | BasedataField | t_hcdm_applybillent.flaborrelstatusid |  | hbss_laborrelstatus |
| baselocation | 常驻工作地 | BasedataField | — |  | hbss_workplace |
| contrworkloc | 协议工作地 | BasedataField | — |  | hbss_workplace |
| entrydate | 入职日期 | DateField | — |  |  |
| realregulardate | 转正日期  | DateField | — |  |  |
| jobscm | 职位体系方案 | BasedataField | — |  | hbjm_jobscmhr |
| jobseq | 职位序列 | BasedataField | — |  | hbjm_jobseqhr |
| jobfamily | 职位族 | BasedataField | — |  | hbjm_jobfamilyhr |
| jobclass | 职位类 | BasedataField | — |  | hbjm_jobclasshr |
| postype | 任职类型 | BasedataField | — |  | hbss_postype |
| adminorgtype | 行政组织类型 | BasedataField | — |  | haos_adminorgtype |
| admindivision | 组织所在城市 | BasedataField | — |  | bd_admindivision |
| workplace | 工作地所在城市 | BasedataField | — |  | bd_admindivision |
| industrytype | 行业类别 | BasedataField | — |  | hbss_industrytype |
| operationequal | 主要执业资格 | BasedataField | — |  | hbss_operationqual |
| eocpquallevel | 主要执业资格等级 | BasedataField | — |  | hbss_ocpquallevel |
| protitle | 最高职称 | BasedataField | t_hcdm_applybillent.fprotitleid |  | hbss_protitle |
| protitlelevel | 最高职称级别 | BasedataField | — |  | hbss_protitlelevel |
| protitletype | 最高职称类别 | BasedataField | — |  | hbss_protitletype |
| pocpquallevel | 主要职业资格等级 | BasedataField | — |  | hbss_ocpquallevel |
| ocpqual | 主要职业资格 | BasedataField | — |  | hbss_ocpqual |
| diploma | 最高学历 | BasedataField | — |  | hbss_diploma |
| schooltype | 最高学历院校特性 | MulBasedataField | — |  |  |
| religion | 宗教 | BasedataField | — |  | hbss_religion |
| nationality | 国籍 | BasedataField | t_hcdm_applybillent.fnationalityid |  | hbss_nationality |
| employee | 员工 | BasedataField | t_hcdm_applybillent.femployeeid |  | hsbs_employee |
| assignment | 组织分配号 | BasedataField | t_hcdm_applybillent.fassignmentid |  | hsbs_assignment |
| synstatus | 同步状态 | ComboField | t_hcdm_applybillent.fsynstatus |  |  |
| startdate | 生效日期 | DateField | t_hcdm_applybillent.fstartdate | ✓ |  |
| enddate | 失效日期 | DateField | t_hcdm_applybillent.fenddate |  |  |
| isvalidate | 校验状态 | CheckBoxField | t_hcdm_applybillent.fisvalidate |  |  |
| issend | 试用期全额发放 | ComboField | — |  |  |
| standarditem | 定调薪项目 | BasedataField | t_hcdm_applybillent.fstandarditemid | ✓ | hsbs_standarditem |
| isinpermscope | 权限范围 | CheckBoxField | — |  |  |
| exratevalue | 汇率值 | DecimalField | t_hcdm_applybillent.fexratevalue |  |  |
| quotetype | 换算方式 | CheckBoxField | — |  |  |
| prefrequency | 上一次频度 | BasedataField | — |  | hsbs_calfrequency |
| matchtype | 匹配类型 | ComboField | — |  |  |
| isusesalaryrank | 启用薪档 | CheckBoxField | — |  |  |
| stdcurrency | 标准表币种 | CurrencyField | — |  | bd_currency |
| graderankshow | 薪等薪档区间 | MuliLangTextField | t_hcdm_applybillent_l.fgraderankshow |  |  |
| remark | 备注 | MuliLangTextField | t_hcdm_applybillent_l.fremark |  |  |
| reason | 调薪原因 | MuliLangTextField | t_hcdm_applybillent_l.freason |  |  |
| manageadminorg | 管理部门 | HRAdminOrgField | t_hcdm_applybillent.fmanageadminorgid |  | haos_adminorghrf7 |
| empname | 姓名 | BasedataPropField | — |  |  |
| empnumber | 工号 | BasedataPropField | — |  |  |
| stdcoefficientv | 薪酬标准系数版本 | MulBasedataField | — |  |  |
| coefficientvalue | 系数值 | DecimalField | t_hcdm_applybillent.fcoefficientvalue |  |  |
| entsalaryadjscm | 定调薪方案 | BasedataField | t_hcdm_applybillent.fentsalaryadjscmid |  | hcdm_adjapprovescm |
| billcountry | 国家/地区 | BasedataField | t_hcdm_applybill.fbillcountryid | ✓ | bd_country |
| stditem | 默认定调薪项目 | MulBasedataField | — | ✓ |  |
| isdraft | 草稿状态 | CheckBoxField | t_hcdm_applybill.fisdraft |  |  |
| createdate | 创建时间 | DateTimeField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_applybill（主表） | 28 |
| t_hcdm_applybill_l | 2 |
| t_hcdm_applybillent | 31 |
| t_hcdm_applybillent_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 57 |
