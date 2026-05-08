---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHLXNRR9KPZ
app_number: hsbs
app_name: 薪酬基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsbs_attintegmapscm — 考勤集成映射方案

**表单编码**: `hsbs_attintegmapscm`  
**表单ID**: `2MK58C4NA4I4`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_attintegmapscm（考勤集成映射方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_attintegmapscm` | 主表 · 20 列 |
| `t_hsbs_attintegmapscment` | 分录表 · 2 列 |
| `t_hsbs_attintegmapscm_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_attintegmapscm.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_attintegmapscm_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_attintegmapscm.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_attintegmapscm.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_attintegmapscm.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_attintegmapscm.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_attintegmapscm.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_attintegmapscm.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_attintegmapscm.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_attintegmapscm_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_attintegmapscm_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_attintegmapscm.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_attintegmapscm.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_attintegmapscm.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_attintegmapscm.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| attperiod | 考勤周期 | BasedataField | t_hsbs_attintegmapscm.fattperiodid | ✓ | wtp_attperiod |
| calperiodtype | 薪资期间类型 | BasedataField | t_hsbs_attintegmapscm.fcalperiodtypeid | ✓ | hsbs_calperiodtype |
| matchtype | 匹配方式 | ComboField | t_hsbs_attintegmapscm.fmatchtype | ✓ |  |

## 实体: entryentity（要素项映射） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attitem | 考勤项目名称 | BasedataField | t_hsbs_attintegmapscment.fattitemid |  | wtbd_attitem |
| attitemnumber | 考勤项目编码 | BasedataPropField | — |  |  |
| attitemdatatype | 数据类型 | BasedataPropField | — |  |  |
| bizitemnumber | 业务项目编码 | BasedataPropField | — |  |  |
| bizitemdatatype | 数据类型 | BasedataPropField | — |  |  |
| bizitem | 业务项目名称 | BasedataField | t_hsbs_attintegmapscment.fbizitemid | ✓ | hsbs_bizitem |
| country | 国家/地区 | BasedataField | t_hsbs_attintegmapscm.fcountryid | ✓ | bd_country |
| org | 算发薪管理组织 | OrgField | t_hsbs_attintegmapscm.forgid | ✓ | bos_org |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_attintegmapscm（主表） | 17 |
| t_hsbs_attintegmapscm_l | 3 |
| t_hsbs_attintegmapscment | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
