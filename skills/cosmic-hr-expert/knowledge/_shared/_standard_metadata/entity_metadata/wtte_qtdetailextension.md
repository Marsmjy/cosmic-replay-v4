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

# wtte_qtdetailextension — 定额明细延期

**表单编码**: `wtte_qtdetailextension`  
**表单ID**: `3FMU=+65=4HF`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_qtdetailextension（定额明细延期） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_qtdetailextension` | 主表 · 34 列 |
| `t_wtte_qtdetailextension_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtte_qtdetailextension.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtte_qtdetailextension_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtte_qtdetailextension.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtte_qtdetailextension.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtte_qtdetailextension.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtte_qtdetailextension.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtte_qtdetailextension.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtte_qtdetailextension.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtte_qtdetailextension.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtte_qtdetailextension_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtte_qtdetailextension_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtte_qtdetailextension.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtte_qtdetailextension.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtte_qtdetailextension.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtte_qtdetailextension.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| org | 考勤管理组织 | OrgField | t_wtte_qtdetailextension.forgid | ✓ | bos_org |
| attfileid | 选择人员 | BasedataField | t_wtte_qtdetailextension.fattfileid | ✓ | wtp_attfilebase |
| qttypeid | 定额类型 | BasedataField | t_wtte_qtdetailextension.fqttypeid | ✓ | wtp_qttype |
| delaymethod | 延期方向 | ComboField | t_wtte_qtdetailextension.fdelaymethod | ✓ |  |
| periodcircleid | 期间循环配置 | BasedataField | t_wtte_qtdetailextension.fperiodcircleid |  | wtbd_cycset |
| periodnum | 期数 | IntegerField | t_wtte_qtdetailextension.fperiodnum |  |  |
| source | 来源 | ComboField | t_wtte_qtdetailextension.fsource |  |  |
| crossday | 跨阶日期 | DateField | t_wtte_qtdetailextension.fcrossday |  |  |
| delayusedate | 延期方式 | ComboField | t_wtte_qtdetailextension.fdelayusedate | ✓ |  |
| delayvalue | 延期值 | IntegerField | t_wtte_qtdetailextension.fdelayvalue | ✓ |  |
| delayunit | 延期单位 | ComboField | t_wtte_qtdetailextension.fdelayunit | ✓ |  |
| genstartdate | 开始日期 | DateField | t_wtte_qtdetailextension.fgenstartdate |  |  |
| genenddate | 结束日期 | DateField | t_wtte_qtdetailextension.fgenenddate |  |  |
| usestartdate | 使用开始日期 | DateField | t_wtte_qtdetailextension.fusestartdate |  |  |
| useenddate | 使用结束日期 | DateField | t_wtte_qtdetailextension.fuseenddate |  |  |
| detailnum | 定额明细编码 | TextField | — |  |  |
| queryyear | 选择范围 | DateField | t_wtte_qtdetailextension.fqueryyear | ✓ |  |
| querymonth | 选择范围 | DateField | t_wtte_qtdetailextension.fquerymonth | ✓ |  |
| qtdetailid | 调整明细记录 | BasedataField | t_wtte_qtdetailextension.fqtdetailid | ✓ | wtp_qtlinedetail |
| qtdetailscope | 时间范围 | ComboField | t_wtte_qtdetailextension.fqtdetailscope | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_qtdetailextension（主表） | 31 |
| t_wtte_qtdetailextension_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 5 |
