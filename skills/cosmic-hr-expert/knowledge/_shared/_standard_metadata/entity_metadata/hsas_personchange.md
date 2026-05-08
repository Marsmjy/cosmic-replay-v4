---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_personchange — 人员变动记录

**表单编码**: `hsas_personchange`  
**表单ID**: `345HYQ3P4MKC`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_personchange（人员变动记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_personchange` | 主表 · 14 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hsas_personchange.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hsas_personchange.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hsas_personchange.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hsas_personchange.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| number | 记录号 | TextField | t_hsas_personchange.fnumber | ✓ |  |
| changereason | 变动原因 | BasedataField | t_hsas_personchange.fchangereasonid | ✓ | hsbs_changereason |
| changedate | 变动日期 | DateField | t_hsas_personchange.fchangedate | ✓ |  |
| source | 来源 | ComboField | t_hsas_personchange.fsource | ✓ |  |
| status | 状态 | ComboField | t_hsas_personchange.fstatus | ✓ |  |
| generatedseg | 已生成分段事件 | CheckBoxField | t_hsas_personchange.fgeneratedseg |  |  |
| action | 关联业务操作 | BasedataField | — |  | hbss_action |
| salaryfile | 薪资档案 | HisModelBasedataField | — | ✓ | hsas_salaryfile |
| errormsg | 分段详细信息 | TextField | t_hsas_personchange.ferrormsg |  |  |
| generatedstatus | 分段事件生成状态 | ComboField | t_hsas_personchange.fgeneratedstatus |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_personchange（主表） | 12 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
