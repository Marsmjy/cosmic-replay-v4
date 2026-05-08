# hbjm_jobhr — 职位

**表单编码**: `hbjm_jobhr`  
**表单ID**: `/IJRHGRN5RVY`  
**归属**: HR基础服务云 / HR基础职位  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbjm_jobhr（职位） [BaseEntity]

### 物理表（垂直拆分表）

| 表名 | 说明 |
|------|------|
| `t_hbjm_job` | 主表 |
| `t_hbjm_job_i` | 拆分表 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 职位编码 | TextField | t_hbjm_job.fnumber |  |  |
| name | 职位名称 | MuliLangTextField | t_hbjm_job.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbjm_job.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbjm_job.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbjm_job.fmodifierid |  | bos_user |
| enable | 业务状态 | BillStatusField | t_hbjm_job.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbjm_job.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbjm_job.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbjm_job.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_hbjm_job.findex |  |  |
| simplename | 职位简称 | MuliLangTextField | t_hbjm_job.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbjm_job.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbjm_job.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbjm_job.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbjm_job.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbjm_job.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbjm_job.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbjm_job.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbjm_job.foriname |  |  |
| boid | 业务ID | BigIntField | t_hbjm_job.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hbjm_job.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hbjm_job.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hbjm_job.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hbjm_job.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hbjm_job.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hbjm_job.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hbjm_job.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hbjm_job.fhisversion |  |  |
| lowjoblevel | 最低职级 | BasedataField | t_hbjm_job.flowjoblevelid |  | hbjm_joblevelhr |
| highjoblevel | 最高职级 | BasedataField | t_hbjm_job.fhighjoblevelid |  | hbjm_joblevelhr |
| lowjobgrade | 最低职等 | BasedataField | t_hbjm_job.flowjobgradeid |  | hbjm_jobgradehr |
| highjobgrade | 最高职等 | BasedataField | t_hbjm_job.fhighjobgradeid |  | hbjm_jobgradehr |
| jobtype | 职位类别 | BasedataField | t_hbjm_job.fjobtypeid |  | hbjm_jobtype |
| depcytype | 属地员工类别(废弃) | BasedataField | t_hbjm_job.fdepcytypeid |  | hbss_depcytype |
| diplomareq | 学历要求 | BasedataField | t_hbjm_job.fdiplomareqid |  | hbss_diploma |
| agereq | 年龄要求 | MuliLangTextField | t_hbjm_job.fagereq |  |  |
| knowledgereq | 知识要求 | MuliLangTextField | t_hbjm_job.fknowledgereq |  |  |
| skillreq | 技能要求 | MuliLangTextField | t_hbjm_job.fskillreq |  |  |
| abilityreq | 能力要求 | MuliLangTextField | t_hbjm_job.fabilityreq |  |  |
| experiencereq | 经验要求 | MuliLangTextField | t_hbjm_job.fexperiencereq |  |  |
| joblevelrang | 职级范围 | TextField | — |  |  |
| jobgraderang | 职等范围 | TextField | — |  |  |
| jobseq | 职位序列 | HisModelBasedataField | t_hbjm_job.fjobseqid | ✓ | hbjm_jobseqhr |
| jobfamily | 职位族 | HisModelBasedataField | t_hbjm_job.fjobfamilyid |  | hbjm_jobfamilyhr |
| jobclass | 职位类 | HisModelBasedataField | t_hbjm_job.fjobclassid |  | hbjm_jobclasshr |
| joborientation | 定位 | MuliLangTextField | t_hbjm_job.fjoborientation |  |  |
| jobduty | 主要职责 | MuliLangTextField | t_hbjm_job.fjobduty |  |  |
| jobstandard | 衡量标准 | MuliLangTextField | t_hbjm_job.fjobstandard |  |  |
| highjoblevelname | 最高职级 | TextField | — |  |  |
| lowjoblevelname | 最低职级 | TextField | — |  |  |
| lowjobgradename | 最低职等 | TextField | — |  |  |
| highjobgradename | 最高职等 | TextField | — |  |  |
| jobgradescm | 职等方案 | BasedataField | t_hbjm_job.fjobgradescmid |  | hbjm_jobgradescmhr |
| joblevelscm | 职级方案 | BasedataField | t_hbjm_job.fjoblevelscmid |  | hbjm_joblevelscmhr |
| jobscm | 职位体系方案 | MulBasedataField | t_hbjm_jobscmmul（子表） |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | t_hbjm_job.fsourcesyskey |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbjm_job（主表） | 49 |
| 无数据库列（RadioField/废弃/虚拟） | 14 |
| 独立子表 t_hbjm_jobscmmul | 1 |

