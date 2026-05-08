---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_caltasknewtpl — 核算任务模板

**表单编码**: `hsas_caltasknewtpl`  
**表单ID**: `10EF93EXZ/DL`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_caltasknewtpl（核算任务模板） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_caltasknewtpl` | 主表 · 43 列 |
| `t_hsas_caltasknewtpl_l` | 多语言表 · 5 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_caltasknewtpl.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_caltasknewtpl_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_caltasknewtpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_caltasknewtpl.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_caltasknewtpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_caltasknewtpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_caltasknewtpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_caltasknewtpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_caltasknewtpl.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_caltasknewtpl_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_caltasknewtpl_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_caltasknewtpl.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_caltasknewtpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_caltasknewtpl.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_caltasknewtpl.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_caltasknewtpl.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_caltasknewtpl.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_caltasknewtpl.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_caltasknewtpl.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_caltasknewtpl.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_caltasknewtpl.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_caltasknewtpl.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_caltasknewtpl.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_caltasknewtpl.fhisversion |  |  |
| operation | 调度日期加减操作 | ComboField | t_hsas_caltasknewtpl.foperation | ✓ |  |
| operationdays | 调度日期加减天数 | ComboField | t_hsas_caltasknewtpl.foperationdays | ✓ |  |
| scheduledesc | 任务创建调度计划 | TextField | t_hsas_caltasknewtpl.fscheduledesc |  |  |
| notifyway | 通知方式 | ComboField | t_hsas_caltasknewtpl.fnotifyway | ✓ |  |
| notifyconfig | 任务异常通知场景及对象 | TextAreaField | t_hsas_caltasknewtpl.fnotifyconfig |  |  |
| schedule | 调度计划 | TextField | — |  |  |
| payrollgrp | 薪资核算组 | BasedataField | t_hsas_caltasknewtpl.fpayrollgrpid | ✓ | hsas_payrollgrp |
| payrollscene | 薪资核算场景 | BasedataField | t_hsas_caltasknewtpl.fpayrollsceneid | ✓ | hsas_payrollscene |
| tracker | 任务跟踪人 | BasedataField | t_hsas_caltasknewtpl.ftrackerid | ✓ | bos_user |
| isrelatedperm | 关联跟踪人权限 | CheckBoxField | t_hsas_caltasknewtpl.fisrelatedperm |  |  |
| nameprefix | 名称前缀 | MuliLangTextField | t_hsas_caltasknewtpl_l.fnameprefix | ✓ |  |
| namebody | 名称主体 | ComboField | t_hsas_caltasknewtpl.fnamebody | ✓ |  |
| namesuffix | 名称后缀 | MuliLangTextField | t_hsas_caltasknewtpl_l.fnamesuffix | ✓ |  |
| namesample | 名称生成样例 | TextField | — |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| expscene | 异常场景 | ComboField | — |  |  |
| notifyobj | 通知对象 | ComboField | — | ✓ |  |
| msgtemplate | 消息模板 | BasedataField | — |  | msg_template |
| entryboid | 分录业务ID | BigIntField | — |  |  |
| basedatapropfield | 模板内容 | BasedataPropField | — |  |  |
| successnotifyobj | 调度成功通知对象 | ComboField | t_hsas_caltasknewtpl.fsuccessnotifyobj | ✓ |  |
| org | 算发薪管理组织 | OrgField | t_hsas_caltasknewtpl.forgid | ✓ | bos_org |
| generalenname | 通用英文名 | TextField | t_hsas_caltasknewtpl.fgeneralenname |  |  |
| chargeperson | 模板负责人 | UserField | t_hsas_caltasknewtpl.fchargepersonid | ✓ | bos_user |
| changestatus | 变更状态 | ComboField | t_hsas_caltasknewtpl.fchangestatus |  |  |
| tasktype | 任务类型 | ComboField | t_hsas_caltasknewtpl.ftasktype | ✓ |  |
| schconfig | 任务创建调度计划 | BasedataField | t_hsas_caltasknewtpl.fschconfigid | ✓ | hsbs_schplancfg |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_caltasknewtpl（主表） | 38 |
| t_hsas_caltasknewtpl_l | 5 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
