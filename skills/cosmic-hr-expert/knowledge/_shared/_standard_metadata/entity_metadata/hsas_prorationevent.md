---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_prorationevent — 人员分段事件

**表单编码**: `hsas_prorationevent`  
**表单ID**: `3516L+AN/E9K`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_prorationevent（人员分段事件） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_prorationevent` | 主表 · 14 列 |
| `t_hsas_prorationevent_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hsas_prorationevent.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hsas_prorationevent.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hsas_prorationevent.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hsas_prorationevent.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hsas_prorationevent_l.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| salaryfile | 薪资档案 | BasedataField | t_hsas_prorationevent.fsalaryfileid | ✓ | hsas_salaryfile |
| prorationdate | 分段日期 | DateField | t_hsas_prorationevent.fprorationdate | ✓ |  |
| prorationgenrule | 分段事件生成规则 | BasedataField | t_hsas_prorationevent.fprorationgenruleid | ✓ | hsas_prorationgenrule |
| changereason | 变动原因 | BasedataField | t_hsas_prorationevent.fchangereasonid | ✓ | hsbs_changereason |
| prorationtype | 分段类型 | ComboField | t_hsas_prorationevent.fprorationtype | ✓ |  |
| salaryitem | 分段项目 | MulBasedataField | — | ✓ |  |
| source | 事件来源 | ComboField | t_hsas_prorationevent.fsource |  |  |
| eventstatus | 事件状态 | ComboField | t_hsas_prorationevent.feventstatus |  |  |
| referencestatus | 引用状态 | ComboField | t_hsas_prorationevent.freferencestatus |  |  |
| personchange | 变动记录 | BigIntField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_prorationevent（主表） | 12 |
| t_hsas_prorationevent_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
