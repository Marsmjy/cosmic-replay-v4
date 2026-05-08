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

# wtte_quotadetailmdfy — 定额明细调整

**表单编码**: `wtte_quotadetailmdfy`  
**表单ID**: `3FM9VTS8LWI0`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_quotadetailmdfy（定额明细调整） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_quotadetailmdfy` | 主表 · 33 列 |
| `t_wtte_quotadetailmdfy_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtte_quotadetailmdfy.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtte_quotadetailmdfy_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtte_quotadetailmdfy.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtte_quotadetailmdfy.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtte_quotadetailmdfy.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtte_quotadetailmdfy.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtte_quotadetailmdfy.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtte_quotadetailmdfy.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtte_quotadetailmdfy.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtte_quotadetailmdfy_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtte_quotadetailmdfy_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtte_quotadetailmdfy.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtte_quotadetailmdfy.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtte_quotadetailmdfy.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtte_quotadetailmdfy.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| org | 考勤管理组织 | OrgField | t_wtte_quotadetailmdfy.forgid | ✓ | bos_org |
| attfileid | 选择人员 | BasedataField | t_wtte_quotadetailmdfy.fattfileid | ✓ | wtp_attfilebase |
| qttypeid | 定额类型 | BasedataField | t_wtte_quotadetailmdfy.fqttypeid | ✓ | wtp_qttype |
| queryyear | 选择范围 | DateField | t_wtte_quotadetailmdfy.fqueryyear | ✓ |  |
| querymonth | 选择范围 | DateField | t_wtte_quotadetailmdfy.fquerymonth | ✓ |  |
| qtdetailid | 调整明细记录 | BasedataField | t_wtte_quotadetailmdfy.fqtdetailid | ✓ | wtp_qtlinedetail |
| mdfymethod | 调整方式 | ComboField | t_wtte_quotadetailmdfy.fmdfymethod | ✓ |  |
| periodcircleid | 期间循环配置 | BasedataField | t_wtte_quotadetailmdfy.fperiodcircleid |  | wtbd_cycset |
| periodnum | 期数 | IntegerField | t_wtte_quotadetailmdfy.fperiodnum |  |  |
| source | 来源 | ComboField | t_wtte_quotadetailmdfy.fsource |  |  |
| crossday | 跨阶日期 | DateField | t_wtte_quotadetailmdfy.fcrossday |  |  |
| mdfyvalue | 数值 | DecimalField | t_wtte_quotadetailmdfy.fmdfyvalue | ✓ |  |
| genstartdate | 开始日期 | DateField | t_wtte_quotadetailmdfy.fgenstartdate |  |  |
| genenddate | 结束日期 | DateField | t_wtte_quotadetailmdfy.fgenenddate |  |  |
| usestartdate | 使用开始日期 | DateField | t_wtte_quotadetailmdfy.fusestartdate |  |  |
| useenddate | 使用结束日期 | DateField | t_wtte_quotadetailmdfy.fuseenddate |  |  |
| detailnum | 定额明细编码 | TextField | — |  |  |
| isdpconvert | 参与离职折算 | CheckBoxField | t_wtte_quotadetailmdfy.fisdpconvert |  |  |
| qtdetailscope | 时间范围 | ComboField | t_wtte_quotadetailmdfy.fqtdetailscope | ✓ |  |
| unit | 单位 | ComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_quotadetailmdfy（主表） | 30 |
| t_wtte_quotadetailmdfy_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
