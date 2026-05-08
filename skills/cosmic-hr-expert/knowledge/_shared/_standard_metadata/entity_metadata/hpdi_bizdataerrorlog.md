---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 0NXW1VOPH+QV
app_number: hpdi
app_name: 薪资数据集成
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hpdi_bizdataerrorlog — 业务数据提报异常记录

**表单编码**: `hpdi_bizdataerrorlog`  
**表单ID**: `25R5AXVGRAM4`  
**归属**: 薪酬福利云 / 薪资数据集成  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hpdi_bizdataerrorlog（业务数据提报异常记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hpdi_bizdataerrorlog` | 主表 · 13 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hpdi_bizdataerrorlog.fcreatorid |  | bos_user |
| billno | 单据编号 | TextField | t_hpdi_bizdataerrorlog.fbillno |  |  |
| operatestage | 操作阶段 | ComboField | t_hpdi_bizdataerrorlog.foperatestage |  |  |
| bizitemgroup | 业务数据模板 | BasedataField | t_hpdi_bizdataerrorlog.fbizitemgroupid |  | hsbs_bizitemgroup |
| operatetime | 操作时间 | CreateDateField | t_hpdi_bizdataerrorlog.foperatetime |  |  |
| notpassauditnum | 数据审批不通过 | IntegerField | t_hpdi_bizdataerrorlog.fnotpassauditnum |  |  |
| operatenum | 操作条目数量 | IntegerField | t_hpdi_bizdataerrorlog.foperatenum |  |  |
| overinputnum | 超过输入次数 | IntegerField | t_hpdi_bizdataerrorlog.foverinputnum |  |  |
| notintimewindownum | 不在窗口期内 | IntegerField | t_hpdi_bizdataerrorlog.fnotintimewindownum |  |  |
| othernum | 其他 | IntegerField | t_hpdi_bizdataerrorlog.fothernum |  |  |
| totalnum | 合计 | IntegerField | t_hpdi_bizdataerrorlog.ftotalnum |  |  |
| adminorg | 行政组织 | HRAdminOrgField | t_hpdi_bizdataerrorlog.fadminorgid |  | haos_adminorghrf7 |
| matchfailnum | 自动赋值薪资核算组失败 | IntegerField | t_hpdi_bizdataerrorlog.fmatchfailnum |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hpdi_bizdataerrorlog（主表） | 13 |
