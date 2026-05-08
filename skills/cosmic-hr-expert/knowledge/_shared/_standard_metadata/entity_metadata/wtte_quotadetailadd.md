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

# wtte_quotadetailadd — 定额明细新增

**表单编码**: `wtte_quotadetailadd`  
**表单ID**: `3F9AB3F22HD6`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_quotadetailadd（定额明细新增） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_quotadetailadd` | 主表 · 17 列 |
| `t_wtte_qtdetailaddentry` | 分录表 · 22 列 |
| `t_wtte_quotadetailadd_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtte_quotadetailadd.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtte_quotadetailadd_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtte_quotadetailadd.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtte_quotadetailadd.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtte_quotadetailadd.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtte_quotadetailadd.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtte_quotadetailadd.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtte_quotadetailadd.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtte_quotadetailadd.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtte_quotadetailadd_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtte_quotadetailadd_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtte_quotadetailadd.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtte_quotadetailadd.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtte_quotadetailadd.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtte_quotadetailadd.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| org | 考勤管理组织 | OrgField | t_wtte_qtdetailaddentry.forg | ✓ | bos_org |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attfilebase | 姓名 | BasedataField | — | ✓ | wtp_attfilebase |
| attpersonnumber | 工号 | BasedataPropField | — |  |  |
| attfilenumber | 档案编号 | BasedataPropField | — |  |  |
| attfileorg | 考勤管理组织 | BasedataPropField | — |  |  |
| company | 所属公司 | BasedataPropField | t_wtte_qtdetailaddentry.fcompany |  |  |
| adminorg | 行政组织 | BasedataPropField | t_wtte_qtdetailaddentry.fadminorg |  |  |
| affiliateadminorg | 挂靠行政组织 | BasedataPropField | t_wtte_qtdetailaddentry.faffiliateadminorg |  |  |
| empgroup | 考勤档案分组 | BasedataPropField | t_wtte_qtdetailaddentry.fempgroup |  |  |
| dependency | 国家/地区 | BasedataPropField | t_wtte_qtdetailaddentry.fdependency |  |  |
| agreedlocation | 协议工作地 | BasedataPropField | t_wtte_qtdetailaddentry.fagreedlocation |  |  |
| workplace | 考勤地点 | BasedataPropField | t_wtte_qtdetailaddentry.fworkplace |  |  |
| position | 岗位 | BasedataPropField | t_wtte_qtdetailaddentry.fposition |  |  |
| job | 职位 | BasedataPropField | t_wtte_qtdetailaddentry.fjob |  |  |
| atttag | 考勤标识 | BasedataPropField | — |  |  |
| value | 数量 | DecimalField | t_wtte_qtdetailaddentry.fvalue | ✓ |  |
| attendstatus | 考勤状态 | BasedataPropField | — |  |  |
| attfilestartdate | 档案开始日期 | BasedataPropField | — |  |  |
| attfileenddate | 档案结束日期 | BasedataPropField | — |  |  |
| quotatype | 定额类型 | BasedataField | t_wtte_qtdetailaddentry.fquotatypeid | ✓ | wtp_qttype |
| genestartdate | 生成开始日期 | DateField | t_wtte_qtdetailaddentry.fgenestartdate | ✓ |  |
| basedatapropfield | 单位 | BasedataPropField | t_wtte_qtdetailaddentry.fbasedatapropfield |  |  |
| useenddate | 使用结束日期 | DateField | t_wtte_qtdetailaddentry.fuseenddate |  |  |
| usestartdate | 使用开始日期 | DateField | t_wtte_qtdetailaddentry.fusestartdate |  |  |
| isdpconvert | 参与离职折算 | CheckBoxField | t_wtte_quotadetailadd.fisdpconvert |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_quotadetailadd（主表） | 13 |
| t_wtte_qtdetailaddentry | 16 |
| t_wtte_quotadetailadd_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 12 |
