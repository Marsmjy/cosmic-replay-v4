---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 19KOKMMSZFZJ
app_number: wtbd
app_name: 工时假勤基础资料
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtbd_cycset — 期间循环配置

**表单编码**: `wtbd_cycset`  
**表单ID**: `26MX+MOUQDLC`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_cycset（期间循环配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_cycset` | 主表 · 34 列 |
| `t_wtbd_cycset_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_cycset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_cycset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_cycset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_cycset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_cycset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_cycset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_cycset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_cycset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_cycset.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_cycset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_cycset_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtbd_cycset.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_cycset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_cycset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_cycset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtbd_cycset.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtbd_cycset.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtbd_cycset_l.foriname |  |  |
| cycrefdate | 参照日期配置 | BasedataField | t_wtbd_cycset.fcycrefdateid |  | wtbd_cycdateset |
| prostartdate | 期间开始日期 | ComboField | t_wtbd_cycset.fprostartdate | ✓ |  |
| selfdate | 自定义日期 | DateField | t_wtbd_cycset.fselfdate |  |  |
| proenddate | 期间结束日期 | DateField | t_wtbd_cycset.fproenddate |  |  |
| cyctype | 循环类型 | ComboField | t_wtbd_cycset.fcyctype | ✓ |  |
| cycunit | 循环单位 | ComboField | t_wtbd_cycset.fcycunit | ✓ |  |
| iscyctodata | 是否循环到指定日 | CheckBoxField | t_wtbd_cycset.fiscyctodata |  |  |
| cycselec | 循环结束到指定日 | ComboField | t_wtbd_cycset.fcycselec |  |  |
| unit | 单位 | ComboField | t_wtbd_cycset.funit |  |  |
| ischeckcycass | 是否选择循环归属 | CheckBoxField | t_wtbd_cycset.fischeckcycass |  |  |
| attachtype | 归属方式 | ComboField | t_wtbd_cycset.fattachtype |  |  |
| attachunit | 归属单位 | ComboField | t_wtbd_cycset.fattachunit |  |  |
| cycvalues | 循环数值 | TextField | t_wtbd_cycset.fcycvalues | ✓ |  |
| cyccount | 循环次数 | IntegerField | t_wtbd_cycset.fcyccount | ✓ |  |
| gapint | 间隔数值 | TextField | t_wtbd_cycset.fgapint |  |  |
| cycdate | 循环结束到指定日 | DateField | t_wtbd_cycset.fcycdate |  |  |
| attfile | 考勤档案 | BasedataField | — |  | wtp_attfilebase |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_cycset（主表） | 30 |
| t_wtbd_cycset_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 2 |
