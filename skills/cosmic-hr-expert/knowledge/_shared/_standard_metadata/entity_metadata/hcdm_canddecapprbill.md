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

# hcdm_canddecapprbill — 候选人定薪申请单

**表单编码**: `hcdm_canddecapprbill`  
**表单ID**: `4UNM7VFVJSTP`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_canddecapprbill（候选人定薪申请单） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_canddecapprbill` | 主表 · 60 列 |
| `t_hcdm_canddecapprent` | 分录表 · 27 列 |
| `t_hcdm_canddecapprbill_l` | 多语言表 · 1 列 |
| `t_hcdm_canddecapprent_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_hcdm_canddecapprbill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_hcdm_canddecapprbill.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_hcdm_canddecapprbill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_canddecapprbill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_hcdm_canddecapprbill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_hcdm_canddecapprbill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_canddecapprbill.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_canddecapprbill.fcreatetime |  |  |
| barcode | 条形码 | TextField | t_hcdm_canddecapprbill.fbarcode |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| isexistsworkflow | 有工作流 | CheckBoxField | t_hcdm_canddecapprbill.fisexistsworkflow |  |  |
| inputdevicetype | 输入设备 | TextField | t_hcdm_canddecapprbill.finputdevicetype |  |  |
| auditstatus | 审批状态 | BillStatusField | t_hcdm_canddecapprbill.fauditstatus |  |  |
| org | 薪酬管理组织 | OrgField | t_hcdm_canddecapprbill.forgid | ✓ | bos_org |
| salaryadjrsn | 定薪类型 | BasedataField | t_hcdm_canddecapprbill.fsalaryadjrsnid | ✓ | hsbs_salaryadjustrsn |
| country | 薪酬管理属地 | BasedataField | t_hcdm_canddecapprbill.fcountryid | ✓ | bd_country |
| candsetsalact | 候选人定薪活动 | BasedataField | t_hcdm_canddecapprbill.fcandsetsalactid |  | hcdm_candsetsalact |
| isdraft | 草稿状态 | CheckBoxField | t_hcdm_canddecapprbill.fisdraft |  |  |
| isbotp | 来自botp | CheckBoxField | — |  |  |

## 实体: entryentity（定薪明细信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| standarditem | 定调薪项目 | BasedataField | t_hcdm_canddecapprent.fstandarditemid | ✓ | hsbs_standarditem |
| salarygrade | 薪等 | BasedataField | t_hcdm_canddecapprent.fsalarygradeid |  | hsbs_salarygrade |
| salaryrank | 薪档 | BasedataField | t_hcdm_canddecapprent.fsalaryrankid |  | hsbs_salaryrank |
| currency | 币种 | CurrencyField | — | ✓ | bd_currency |
| amount | 金额 | AmountField | t_hcdm_canddecapprent.famount | ✓ |  |
| overstandardtype | 超标准 | ComboField | t_hcdm_canddecapprent.foverstandardtype |  |  |
| remark | 备注 | MuliLangTextField | t_hcdm_canddecapprent_l.fremark |  |  |
| issend | 试用期全额发放 | ComboField | t_hcdm_canddecapprent.fissend | ✓ |  |
| startdate | 生效日期 | DateField | t_hcdm_canddecapprent.fstartdate | ✓ |  |
| enddate | 失效日期 | DateField | t_hcdm_canddecapprent.fenddate |  |  |
| frequency | 频度 | BasedataField | t_hcdm_canddecapprent.ffrequencyid | ✓ | hsbs_calfrequency |
| salarystdv | 薪酬标准表 | BasedataField | t_hcdm_canddecapprent.fsalarystdvid |  | hcdm_salarystandard |
| stdscmv | 薪酬体系版本 | BasedataField | t_hcdm_canddecapprent.fstdscmvid |  | hcdm_stdscm |
| isfromoffer | 来自offer | CheckBoxField | t_hcdm_canddecapprent.fisfromoffer |  |  |
| graderankrange | 薪等薪档区间 | TextField | t_hcdm_canddecapprent.fgraderankrange |  |  |
| intervalmin | 薪酬标准金额Min | AmountField | t_hcdm_canddecapprent.fintervalmin |  |  |
| stdmiddlevalue | 标准中位值 | AmountField | t_hcdm_canddecapprent.fstdmiddlevalue |  |  |
| intervalmax | 薪酬标准金额Max | AmountField | t_hcdm_canddecapprent.fintervalmax |  |  |
| matchstrategy | 匹配策略 | BasedataField | t_hcdm_canddecapprent.fmatchstrategyid |  | hcdm_stdmatchstrategy |
| matchtype | 匹配类型 | ComboField | t_hcdm_canddecapprent.fmatchtype |  |  |
| isusesalaryrank | 启用薪档 | CheckBoxField | t_hcdm_canddecapprent.fisusesalaryrank |  |  |
| graderankshow | 薪等薪档区间 | MuliLangTextField | t_hcdm_canddecapprent_l.fgraderankshow |  |  |
| excesscontrol | 超标准控制 | ComboField | t_hcdm_canddecapprent.fexcesscontrol |  |  |
| stdcurrency | 标准表币种 | BasedataField | t_hcdm_canddecapprent.fstdcurrencyid |  | bd_currency |
| stdcoefficientv | 薪酬标准系数版本 | MulBasedataField | — |  |  |
| coefficientvalue | 系数值 | DecimalField | t_hcdm_canddecapprent.fcoefficientvalue |  |  |
| adjfile | 定调薪档案 | BasedataField | t_hcdm_canddecapprbill.fadjfileid |  | hcdm_adjfileinfo |
| candidate | 候选人 | BasedataField | t_hcdm_canddecapprbill.fcandidateid |  | hcf_candidate |
| avatar | 头像 | PictureField | t_hcdm_canddecapprbill.favatar |  |  |
| empgroup | 定调薪档案分组 | BasedataField | t_hcdm_canddecapprbill.fempgroupid | ✓ | hbss_empgroup |
| manageadminorg | 管理部门 | HRAdminOrgField | t_hcdm_canddecapprbill.fmanageadminorgid | ✓ | haos_adminorghrf7 |
| escrowstaff | 代管员工 | CheckBoxField | t_hcdm_canddecapprbill.fescrowstaff |  |  |
| salarystructure | 薪酬结构 | BasedataField | t_hcdm_canddecapprbill.fsalarystructureid | ✓ | hcdm_salaystructure |
| stdscm | 薪酬体系 | BasedataField | t_hcdm_canddecapprbill.fstdscmid | ✓ | hcdm_stdscm |
| deceffectdate | 默认定薪生效日期 | DateField | t_hcdm_canddecapprbill.fdeceffectdate | ✓ |  |
| recruittype | 招聘类型 | BasedataField | t_hcdm_canddecapprbill.frecruittypeid |  | hbss_recrutyp |
| joblevel | 职级 | BasedataField | t_hcdm_canddecapprbill.fjoblevelid |  | hbjm_joblevelhr |
| jobgrade | 职等 | BasedataField | t_hcdm_canddecapprbill.fjobgradeid |  | hbjm_jobgradehr |
| job | 职位 | BasedataField | t_hcdm_canddecapprbill.fjobid |  | hbjm_jobhr |
| jobscm | 职位体系方案 | BasedataField | t_hcdm_canddecapprbill.fjobscmid |  | hbjm_jobscmhr |
| jobseq | 职位序列 | BasedataField | t_hcdm_canddecapprbill.fjobseqid |  | hbjm_jobseqhr |
| jobfamily | 职位族 | BasedataField | t_hcdm_canddecapprbill.fjobfamilyid |  | hbjm_jobfamilyhr |
| postype | 任职类型 | BasedataField | t_hcdm_canddecapprbill.fpostypeid |  | hbss_postype |
| laborreltype | 用工关系类型 | BasedataField | t_hcdm_canddecapprbill.flaborreltypeid |  | hbss_laborreltype |
| laborrelstatus | 用工关系状态 | BasedataField | t_hcdm_canddecapprbill.flaborrelstatusid |  | hbss_laborrelstatus |
| nationality | 国籍 | BasedataField | t_hcdm_canddecapprbill.fnationalityid |  | hbss_nationality |
| schooltype | 院校特性 | MulBasedataField | — |  |  |
| diploma | 最高学历 | BasedataField | t_hcdm_canddecapprbill.fdiplomaid |  | hbss_diploma |
| ocpqual | 主要职业资格 | BasedataField | t_hcdm_canddecapprbill.focpqualid |  | hbss_ocpqual |
| pocpquallevel | 主要职业资格等级 | BasedataField | t_hcdm_canddecapprbill.fpocpquallevelid |  | hbss_ocpquallevel |
| empname | 姓名 | MuliLangTextField | t_hcdm_canddecapprbill_l.fempname |  |  |
| empnumber | 工号 | TextField | t_hcdm_canddecapprbill.fempnumber |  |  |
| adminorg | 行政组织 | HRAdminOrgField | t_hcdm_canddecapprbill.fadminorgid |  | haos_adminorghrf7 |
| company | 所属公司 | HRAdminOrgField | t_hcdm_canddecapprbill.fcompanyid |  | haos_adminorghrf7 |
| position | 岗位 | HRPositionField | — |  | hbpm_positionhrf7 |
| contrworkloc | 协议工作地 | BasedataField | t_hcdm_canddecapprbill.fcontrworklocid |  | hbss_workplace |
| adminorgtype | 组织类型 | BasedataField | t_hcdm_canddecapprbill.fadminorgtypeid |  | haos_adminorgtype |
| entrydate | 入职日期 | DateField | t_hcdm_canddecapprbill.fentrydate |  |  |
| syncfilestatus | 同步档案状态 | ComboField | t_hcdm_canddecapprbill.fsyncfilestatus |  |  |
| jobclass | 职位类 | BasedataField | t_hcdm_canddecapprbill.fjobclassid |  | hbjm_jobclasshr |
| assignment | 组织分配号 | BasedataField | t_hcdm_canddecapprbill.fassignmentid |  | hsbs_assignment |
| onbrdinfo | 候选人 | BasedataField | t_hcdm_canddecapprbill.fonbrdinfoid | ✓ | hom_onbrdbillbase |
| industrytype | 行业类别 | BasedataField | t_hcdm_canddecapprbill.findustrytypeid |  | hbss_industrytype |
| baselocation | 常驻工作地 | BasedataField | t_hcdm_canddecapprbill.fbaselocationid |  | hbss_workplace |
| synchstatus | 入职同步结果 | ComboField | t_hcdm_canddecapprbill.fsynchstatus |  |  |
| billtype | 显示方案 | BillTypeField | — | ✓ | bos_billtype |
| offerid | offerId | BigIntField | t_hcdm_canddecapprbill.fofferid |  |  |
| empposorgrel | 任职经历 | BasedataField | t_hcdm_canddecapprbill.fempposorgrelid |  | hsbs_empposorgrel |
| createdate | 创建时间 | DateTimeField | — |  |  |
| admindivision | 组织所在城市 | BasedataField | t_hcdm_canddecapprbill.fadmindivisionid |  | bd_admindivision |
| workplace | 工作地所在城市 | BasedataField | t_hcdm_canddecapprbill.fworkplaceid |  | bd_admindivision |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_canddecapprbill（主表） | 57 |
| t_hcdm_canddecapprbill_l | 1 |
| t_hcdm_canddecapprent | 22 |
| t_hcdm_canddecapprent_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
