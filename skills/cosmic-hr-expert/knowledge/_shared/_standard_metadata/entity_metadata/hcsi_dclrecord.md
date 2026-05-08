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

# hcsi_dclrecord — 增减员申报表

**表单编码**: `hcsi_dclrecord`  
**表单ID**: `2UUH+2B=Z7Y+`  
**归属**: 薪酬福利云 / 中国社保  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcsi_dclrecord（增减员申报表） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcsi_dclrecord` | 主表 · 17 列 |
| `t_hcsi_dclrecord_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hcsi_dclrecord.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hcsi_dclrecord.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hcsi_dclrecord.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hcsi_dclrecord.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| number | 编码 | TextField | t_hcsi_dclrecord.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcsi_dclrecord_l.fname | ✓ |  |
| insuredcompany | 实际参保单位 | BasedataField | t_hcsi_dclrecord.finsuredcompanyid |  | sitbs_welfarepayer |
| period | 社保期间 | BasedataField | t_hcsi_dclrecord.fperiodid |  | sitbs_sinsurperiod |
| modifytype | 变动类型 | ComboField | t_hcsi_dclrecord.fmodifytype |  |  |
| haswelfaretype | 是否指定险种 | ComboField | t_hcsi_dclrecord.fhaswelfaretype |  |  |
| insurtypemul | 指定险种 | MulBasedataField | — |  |  |
| personcount | 人数 | IntegerField | t_hcsi_dclrecord.fpersoncount |  |  |
| dclstatus | 申报状态 | ComboField | t_hcsi_dclrecord.fdclstatus |  |  |
| lockstatus | 锁定状态 | ComboField | t_hcsi_dclrecord.flockstatus |  |  |
| sinsurdclrule | 申报名单规则 | BasedataField | t_hcsi_dclrecord.fsinsurdclruleid |  | hcsi_sinsurdclrule |
| dclbusinessname | 申报业务名称 | TextField | t_hcsi_dclrecord.fdclbusinessname |  |  |
| sinsurdclrulev | 申报名单规则版本 | BasedataField | t_hcsi_dclrecord.fsinsurdclrulevid |  | hcsi_sinsurdclrule |
| dcldisplayscmv | 申报名单显示方案 | BasedataField | t_hcsi_dclrecord.fdcldisplayscmvid |  | hcsi_dcldisplayscm |
| mulinsuranceitem | 险种项目集合 | MulBasedataField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcsi_dclrecord（主表） | 16 |
| t_hcsi_dclrecord_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
