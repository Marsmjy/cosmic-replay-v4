---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1C8H4/N38LCY
app_number: wtte
app_name: 考勤核算
cloud_number: WTC
cloud_name: 工时假勤云
model_type: ReportFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtte_recordsumreport — 考勤记录-汇总项目

**表单编码**: `wtte_recordsumreport`  
**表单ID**: `23/U9VL=552+`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_recordsumreport（考勤记录-汇总项目） [ReportEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| con_orginitem | 原始考勤项目 | ComboField | — |  |  |
| con_value | 值 | ComboField | — |  |  |
| attitemnumber | 考勤项目编码 | BasedataPropField | — |  |  |
| attitemname | 考勤项目名称 | BasedataField | — |  | wtbd_attitem |
| valuelong | 值 | TextField | — |  |  |
| unit | 单位 | BasedataPropField | — |  |  |
| attperiod | 考勤期间 | BasedataField | — |  | wtp_attperiodentry |
| ascriptionyear | 期间所属年份 | BasedataPropField | — |  |  |
| ascriptionmonth | 期间所属月份 | BasedataPropField | — |  |  |
| isorginattitem | 原始考勤项目 | CheckBoxField | — |  |  |
| con_attperiod | 考勤期间 | BasedataField | — |  | wtp_attperiodentry |
| orgid | 考勤管理组织 | OrgField | — |  | bos_org |
| companyvid | 所属公司 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| departmentvid | 行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| positionvid | 岗位 | HRPositionField | — |  | hbpm_positionhrf7 |
| jobvid | 职位 | BasedataField | — |  | hbjm_jobhr |
| mode | 考勤方式 | BasedataPropField | — |  |  |
| calculatedate | 操作时间 | DateTimeField | — |  |  |
| creator | 创建人 | CreaterField | — |  | bos_user |
| createtime | 创建时间 | CreateDateField | — |  |  |
| con_attperson | 人员 | MulBasedataField | — |  |  |
| con_affiliateadminorg | 挂靠行政组织 | HRMulAdminOrgField | — |  |  |
| showdetails | 明细项目 | TextField | — |  |  |
| showsumhistory | 查看历史 | TextField | — |  |  |
| con_adminorg | 行政组织 | HRMulAdminOrgField | — |  |  |
| con_empgroup | 考勤档案分组 | MulBasedataField | — |  |  |
| affiliateadminorgvid | 挂靠行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| empgroup | 考勤档案分组 | BasedataField | — |  | hbss_empgroup |
| dependency | 国家/地区 | BasedataField | — |  | bd_country |
| agreedworkplace | 协议工作地 | BasedataField | — |  | hbss_workplace |
| workplace | 考勤地点 | BasedataField | — |  | hbss_workplace |
| reckoner | 操作人 | BasedataField | — |  | bos_user |
| perperiodbegindate | 期间开始日期 | DateField | — |  |  |
| perperiodenddate | 期间结束日期 | DateField | — |  |  |
| attfilevid | 考勤档案版本 | BasedataField | — |  | wtp_attfilebase |
| attitemvid | 考勤项目版本ID | BasedataField | — |  | wtbd_attitem |
| con_org | 考勤管理组织 | OrgField | — | ✓ | bos_org |
| dataaccuracy | 精度 | IntegerField | — |  |  |
| con_minvalue | 最小值 | DecimalField | — |  |  |
| con_maxvalue | 最大值 | DecimalField | — |  |  |
| con_attitem | 考勤项目 | MulBasedataField | — |  |  |
| con_owndate | 归属日期 | DateRangeField | — |  |  |
| con_attmain | 主表 | BasedataField | — |  | wtte_atttotalbase |
| attfilenum | 档案编号 | BasedataPropField | — |  |  |
| textname | 姓名 | TextField | — |  |  |
| empnumber | 工号 | TextField | — |  |  |
| valuestring | 值 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 47 |
