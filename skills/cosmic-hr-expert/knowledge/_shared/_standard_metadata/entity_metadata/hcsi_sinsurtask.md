---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2AXKDRPJUQ77
app_number: hcsi
app_name: 中国社保
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcsi_sinsurtask — 社保计算

**表单编码**: `hcsi_sinsurtask`  
**表单ID**: `2LKE9O86746K`  
**归属**: 薪酬福利云 / 中国社保  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcsi_sinsurtask（社保计算） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcsi_sinsurtask` | 主表 · 12 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hcsi_sinsurtask.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hcsi_sinsurtask.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hcsi_sinsurtask.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hcsi_sinsurtask.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| number | 社保计算编码 | TextField | t_hcsi_sinsurtask.fnumber |  |  |
| sinsurperiod | 社保期间 | BasedataField | t_hcsi_sinsurtask.fsinsurperiodid |  | sitbs_sinsurperiod |
| welfarepayer | 社保计算单位 | BasedataField | t_hcsi_sinsurtask.fwelfarepayerid |  | sitbs_welfarepayer |
| taskstatus | 计算状态 | ComboField | t_hcsi_sinsurtask.ftaskstatus |  |  |
| personcount | 总人数 | IntegerField | t_hcsi_sinsurtask.fpersoncount |  |  |
| mulsinsurstd | 参保标准 | MulBasedataField | — |  |  |
| org | 社保公积金管理组织 | OrgField | t_hcsi_sinsurtask.forgid | ✓ | bos_org |
| welfaretype | 计算险种 | MulBasedataField | — |  |  |
| pushstatus | 推送状态 | ComboField | t_hcsi_sinsurtask.fpushstatus |  |  |
| pushcount | 已推送人数 | IntegerField | t_hcsi_sinsurtask.fpushcount |  |  |
| actualwelfarepayer | 实际参保单位 | MulBasedataField | — |  |  |
| theorywelfarepayer | 理论参保单位 | MulBasedataField | — |  |  |
| mulsinsurstdcfg | 险种参数设置 | MulBasedataField | — |  |  |
| mulinsuranceitem | 险种项目 | MulBasedataField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcsi_sinsurtask（主表） | 12 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
