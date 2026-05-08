---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 0VO5EV13=I9W
app_number: hcdm
app_name: 薪酬管理
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcdm_decadjconfirm — 定调薪确认

**表单编码**: `hcdm_decadjconfirm`  
**表单ID**: `4ZMPAZ79UKCW`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_decadjconfirm（定调薪确认） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_decadjconfirm` | 主表 · 46 列 |
| `t_hcdm_decadjconfirm_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcdm_decadjconfirm.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hcdm_decadjconfirm_l.fname |  |  |
| status | 数据状态 | BillStatusField | t_hcdm_decadjconfirm.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcdm_decadjconfirm.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_decadjconfirm.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcdm_decadjconfirm.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_decadjconfirm.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_decadjconfirm.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcdm_decadjconfirm.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hcdm_decadjconfirm_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcdm_decadjconfirm_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hcdm_decadjconfirm.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcdm_decadjconfirm.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcdm_decadjconfirm.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcdm_decadjconfirm.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| bizbillno | 单据编号 | TextField | t_hcdm_decadjconfirm.fbizbillno | ✓ |  |
| bizbillentity | 单据实体 | TextField | t_hcdm_decadjconfirm.fbizbillentity | ✓ |  |
| bizbillid | 单据id | BigIntField | t_hcdm_decadjconfirm.fbizbillid | ✓ |  |
| employee | 员工 | BasedataField | t_hcdm_decadjconfirm.femployeeid | ✓ | hsbs_employee |
| adjfile | 定调薪档案 | BasedataField | t_hcdm_decadjconfirm.fadjfileid | ✓ | hcdm_adjfileinfo |
| confirmstatus | 定调薪确认状态 | ComboField | t_hcdm_decadjconfirm.fconfirmstatus |  |  |
| confirmtype | 确认方式 | ComboField | t_hcdm_decadjconfirm.fconfirmtype |  |  |
| sendstatus | 定调薪确认发送状态 | ComboField | t_hcdm_decadjconfirm.fsendstatus |  |  |
| confirmtime | 确认/放弃时间 | DateTimeField | t_hcdm_decadjconfirm.fconfirmtime |  |  |
| lastsendtime | 最新发送时间 | DateTimeField | t_hcdm_decadjconfirm.flastsendtime |  |  |
| attachmentnum | 附件数量 | IntegerField | t_hcdm_decadjconfirm.fattachmentnum |  |  |
| sendnum | 催办次数 | IntegerField | t_hcdm_decadjconfirm.fsendnum |  |  |
| expirytime | 链接失效时间 | DateTimeField | t_hcdm_decadjconfirm.fexpirytime |  |  |
| adminorg | 行政组织 | HRAdminOrgField | t_hcdm_decadjconfirm.fadminorgid | ✓ | haos_adminorghrf7 |
| manageadminorg | 管理部门 | HRAdminOrgField | t_hcdm_decadjconfirm.fmanageadminorgid | ✓ | haos_adminorghrf7 |
| empgroup | 定调薪档案分组 | BasedataField | t_hcdm_decadjconfirm.fempgroupid | ✓ | hbss_empgroup |
| confirmtpl | 定调薪确认模板 | BasedataField | t_hcdm_decadjconfirm.fconfirmtplid |  | hcdm_adjconfirmtpl |
| msgtpl | 消息通知模板 | BasedataField | t_hcdm_decadjconfirm.fmsgtplid |  | msg_template |
| buttonselect | 定调薪确认页面显示按钮 | MulComboField | t_hcdm_decadjconfirm.fbuttonselect |  |  |
| content | 调薪确认内容 | TextAreaField | t_hcdm_decadjconfirm.fcontent |  |  |
| position | 岗位 | HRPositionField | — |  | hbpm_positionhrf7 |
| job | 职位 | BasedataField | t_hcdm_decadjconfirm.fjobid |  | hbjm_jobhr |
| org | 薪酬管理组织 | OrgField | t_hcdm_decadjconfirm.forgid | ✓ | bos_org |
| country | 薪酬管理属地 | BasedataField | t_hcdm_decadjconfirm.fcountryid | ✓ | bd_country |
| joblevel | 职级 | BasedataField | t_hcdm_decadjconfirm.fjoblevelid |  | hbjm_joblevelhr |
| jobgrade | 职等 | BasedataField | t_hcdm_decadjconfirm.fjobgradeid |  | hbjm_jobgradehr |
| bsed | 生效日期 | DateField | t_hcdm_decadjconfirm.fbsed | ✓ |  |
| salaryadjrsn | 定调薪类型 | BasedataField | t_hcdm_decadjconfirm.fsalaryadjrsnid | ✓ | hsbs_salaryadjustrsn |
| passwordtype | 定调薪确认登录使用密码 | ComboField | t_hcdm_decadjconfirm.fpasswordtype |  |  |
| synstatus | 同步状态 | ComboField | t_hcdm_decadjconfirm.fsynstatus | ✓ |  |
| transcondition | 定调薪数据生效时机 | MulComboField | t_hcdm_decadjconfirm.ftranscondition |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_decadjconfirm（主表） | 42 |
| t_hcdm_decadjconfirm_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 5 |
