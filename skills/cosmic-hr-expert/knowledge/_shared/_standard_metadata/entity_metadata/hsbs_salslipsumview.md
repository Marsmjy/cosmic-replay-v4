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

# hsbs_salslipsumview — 工资条汇总方案

**表单编码**: `hsbs_salslipsumview`  
**表单ID**: `2XTDA6F/+GHC`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_salslipsumview（工资条汇总方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_salslipsumview` | 主表 · 26 列 |
| `t_hsbs_salarysumviewent` | 分录表 · 3 列 |
| `t_hsbs_salsumviewdetail` | 分录表 · 4 列 |
| `t_hsbs_salslipsumview_l` | 多语言表 · 3 列 |
| `t_hsbs_salarysumviewent_l` | 多语言表 · 1 列 |
| `t_hsbs_salsumviewdetail_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_salslipsumview.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_salslipsumview_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_salslipsumview.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_salslipsumview.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_salslipsumview.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_salslipsumview.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_salslipsumview.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_salslipsumview.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_salslipsumview.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_salslipsumview.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_salslipsumview.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_salslipsumview.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_salslipsumview.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_salslipsumview.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_salslipsumview.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsbs_salslipsumview.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_salslipsumview_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_salslipsumview_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_salslipsumview.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_salslipsumview.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_salslipsumview.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| country | 国家/地区 | BasedataField | t_hsbs_salslipsumview.fcountryid | ✓ | bd_country |
| isaudit | 是否审核过 | CheckBoxField | t_hsbs_salslipsumview.fisaudit |  |  |
| isgroup | 是否分组 | CheckBoxField | t_hsbs_salslipsumview.fisgroup |  |  |

## 实体: entryentity（薪酬项目分组） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| salaryitemtype | 分组名称 | BasedataField | t_hsbs_salarysumviewent.fsalaryitemtypeid |  | hsbs_salaryitemtype |
| groupdisplayname | 显示名称 | MuliLangTextField | t_hsbs_salsumviewdetail_l.fgroupdisplayname | ✓ |  |
| itemcount | 项目数 | IntegerField | t_hsbs_salarysumviewent.fitemcount |  |  |
| salaryitem | 薪酬项目名称 | BasedataField | t_hsbs_salsumviewdetail.fsalaryitemid |  | hsbs_salaryitem |
| subgroupdisplayname | 显示名称 | MuliLangTextField | — | ✓ |  |
| iszerodisplay | 数据为0时显示 | CheckBoxField | t_hsbs_salsumviewdetail.fiszerodisplay |  |  |
| isemptydisplay | 数据为空时显示 | CheckBoxField | t_hsbs_salsumviewdetail.fisemptydisplay |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_salslipsumview（主表） | 21 |
| t_hsbs_salarysumviewent | 2 |
| t_hsbs_salslipsumview_l | 3 |
| t_hsbs_salsumviewdetail | 3 |
| t_hsbs_salsumviewdetail_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
