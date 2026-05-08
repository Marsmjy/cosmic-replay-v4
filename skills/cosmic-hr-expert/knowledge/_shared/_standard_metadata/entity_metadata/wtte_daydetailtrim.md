---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1C8H4/N38LCY
app_number: wtte
app_name: 考勤核算
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtte_daydetailtrim — 日明细调整

**表单编码**: `wtte_daydetailtrim`  
**表单ID**: `31E72SQENZD5`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_daydetailtrim（日明细调整） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_daydetailtrim` | 主表 · 18 列 |
| `t_wtte_daydetrimentry` | 分录表 · 17 列 |
| `t_wtte_daydetailtrim_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtte_daydetailtrim.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtte_daydetailtrim_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtte_daydetailtrim.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtte_daydetailtrim.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtte_daydetailtrim.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtte_daydetailtrim.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtte_daydetailtrim.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtte_daydetailtrim.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtte_daydetailtrim.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtte_daydetailtrim_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtte_daydetailtrim_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtte_daydetailtrim.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtte_daydetailtrim.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtte_daydetailtrim.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtte_daydetailtrim.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| org | 考勤管理组织 | OrgField | t_wtte_daydetrimentry.forg | ✓ | bos_org |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attfilebase | 姓名 | BasedataField | t_wtte_daydetrimentry.fattfilebaseid | ✓ | wtp_attfilebase |
| attpersonnumber | 工号 | BasedataPropField | — |  |  |
| attfilenumber | 档案编号 | BasedataPropField | — |  |  |
| attfileorg | 考勤管理组织 | BasedataPropField | — |  |  |
| company | 所属公司 | BasedataPropField | t_wtte_daydetrimentry.fcompany |  |  |
| adminorg | 行政组织 | BasedataPropField | t_wtte_daydetrimentry.fadminorg |  |  |
| affiliateadminorg | 挂靠行政组织 | BasedataPropField | t_wtte_daydetrimentry.faffiliateadminorg |  |  |
| empgroup | 考勤档案分组 | BasedataPropField | t_wtte_daydetrimentry.fempgroup |  |  |
| dependency | 国家/地区 | BasedataPropField | t_wtte_daydetrimentry.fdependency |  |  |
| agreedlocation | 协议工作地 | BasedataPropField | t_wtte_daydetrimentry.fagreedlocation |  |  |
| workplace | 考勤地点 | BasedataPropField | t_wtte_daydetrimentry.fworkplace |  |  |
| position | 岗位 | BasedataPropField | t_wtte_daydetrimentry.fposition |  |  |
| job | 职位 | BasedataPropField | t_wtte_daydetrimentry.fjob |  |  |
| daterangefield | 日期范围 | DateRangeField | — | ✓ |  |
| attitem | 考勤项目 | MulBasedataField | — | ✓ |  |
| value | 调整值 | DecimalField | t_wtte_daydetrimentry.fvalue | ✓ |  |
| itemunit | 单位 | ComboField | t_wtte_daydetrimentry.fitemunit | ✓ |  |
| failreason | 失败原因 | TextField | — |  |  |
| revisionstartdate | 开始日期 | DateField | — |  |  |
| revisionenddate | 结束日期 | DateField | — |  |  |
| revisiondate | 调整日期 | DateField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_daydetailtrim（主表） | 12 |
| t_wtte_daydetailtrim_l | 3 |
| t_wtte_daydetrimentry | 13 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 13 |
