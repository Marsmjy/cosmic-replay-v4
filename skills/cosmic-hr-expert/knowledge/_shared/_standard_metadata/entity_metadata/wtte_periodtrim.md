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

# wtte_periodtrim — 期间汇总调整

**表单编码**: `wtte_periodtrim`  
**表单ID**: `31Z/8H6PKW5=`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_periodtrim（期间汇总调整） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_periodtrim` | 主表 · 18 列 |
| `t_wtte_periodtrimentry` | 分录表 · 5 列 |
| `t_wtte_periodtrim_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtte_periodtrim.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtte_periodtrim_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtte_periodtrim.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtte_periodtrim.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtte_periodtrim.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtte_periodtrim.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtte_periodtrim.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtte_periodtrim.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtte_periodtrim.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtte_periodtrim_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtte_periodtrim_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtte_periodtrim.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtte_periodtrim.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtte_periodtrim.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtte_periodtrim.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| org | 考勤管理组织 | OrgField | t_wtte_periodtrim.forgid | ✓ | bos_org |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attfilebase | 姓名 | BasedataField | t_wtte_periodtrimentry.fattfilebaseid | ✓ | wtp_attfilebase |
| daterangefield | 日期范围0 | DateRangeField | — | ✓ |  |
| itemunit | 单位 | ComboField | t_wtte_periodtrimentry.fitemunit | ✓ |  |
| adminorg | 行政组织 | BasedataPropField | — |  |  |
| affiliateadminorg | 挂靠行政组织 | BasedataPropField | — |  |  |
| empgroup | 考勤档案分组 | BasedataPropField | — |  |  |
| dependency | 国家/地区 | BasedataPropField | — |  |  |
| agreedlocation | 协议工作地 | BasedataPropField | — |  |  |
| workplace | 考勤地点 | BasedataPropField | — |  |  |
| position | 岗位 | BasedataPropField | — |  |  |
| job | 职位 | BasedataPropField | — |  |  |
| attfileorg | 考勤管理组织 | BasedataPropField | — |  |  |
| company | 所属公司 | BasedataPropField | — |  |  |
| attitem | 考勤项目 | MulBasedataField | — | ✓ |  |
| value | 调整值 | DecimalField | t_wtte_periodtrimentry.fvalue | ✓ |  |
| attpersonnumber | 工号 | BasedataPropField | — |  |  |
| attfilenumber | 档案编号 | BasedataPropField | — |  |  |
| failreason | 失败原因 | TextField | — |  |  |
| datefield | 日期范围 | DateField | — |  |  |
| revisionstartdate | 开始日期 | DateField | — |  |  |
| revisionenddate | 结束日期 | DateField | — |  |  |
| revisiondate | 调整日期 | DateField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_periodtrim（主表） | 13 |
| t_wtte_periodtrim_l | 3 |
| t_wtte_periodtrimentry | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 23 |
