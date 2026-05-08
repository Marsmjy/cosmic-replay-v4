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

# wtbd_cycdateset — 参照日期配置

**表单编码**: `wtbd_cycdateset`  
**表单ID**: `26D97FEZ=P1R`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_cycdateset（参照日期配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_cycdateset` | 主表 · 26 列 |
| `t_wtbd_cycdateset_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_cycdateset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_cycdateset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_cycdateset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_cycdateset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_cycdateset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_cycdateset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_cycdateset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_cycdateset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_cycdateset.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_cycdateset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_cycdateset_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtbd_cycdateset.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_cycdateset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_cycdateset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_cycdateset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtbd_cycdateset.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtbd_cycdateset.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtbd_cycdateset_l.foriname |  |  |
| adjusttype | 调整方式 | ComboField | t_wtbd_cycdateset.fadjusttype | ✓ |  |
| adjustmonth | 调整到月份 | ComboField | t_wtbd_cycdateset.fadjustmonth | ✓ |  |
| distanceequal | 距离一致时 | ComboField | t_wtbd_cycdateset.fdistanceequal | ✓ |  |
| adjustdate | 调整到日 | ComboField | t_wtbd_cycdateset.fadjustdate | ✓ |  |
| adjustdic | 调整方向 | ComboField | t_wtbd_cycdateset.fadjustdic | ✓ |  |
| adjustunit | 调整单位 | ComboField | t_wtbd_cycdateset.fadjustunit | ✓ |  |
| adjustvalue | 调整数值 | IntegerField | t_wtbd_cycdateset.fadjustvalue | ✓ |  |
| cycrefdate | 参照日期 | BasedataField | t_wtbd_cycdateset.fcycrefdateid | ✓ | wtbd_cycrefdate |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_cycdateset（主表） | 22 |
| t_wtbd_cycdateset_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
