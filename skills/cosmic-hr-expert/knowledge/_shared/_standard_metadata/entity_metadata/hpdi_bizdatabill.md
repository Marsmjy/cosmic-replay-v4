---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 0NXW1VOPH+QV
app_number: hpdi
app_name: 薪资数据集成
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hpdi_bizdatabill — 业务数据提报

**表单编码**: `hpdi_bizdatabill`  
**表单ID**: `2+63VW+9PECA`  
**归属**: 薪酬福利云 / 薪资数据集成  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hpdi_bizdatabill（业务数据提报） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hpdi_bizdatabill` | 主表 · 30 列 |
| `t_hpdi_bizdatabill_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_hpdi_bizdatabill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_hpdi_bizdatabill.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_hpdi_bizdatabill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hpdi_bizdatabill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_hpdi_bizdatabill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_hpdi_bizdatabill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hpdi_bizdatabill.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_hpdi_bizdatabill.fcreatetime |  |  |
| barcode | 条形码 | TextField | t_hpdi_bizdatabill.fbarcode |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | t_hpdi_bizdatabill.feventeffectdate |  |  |
| isexistsworkflow | 是否有工作流 | CheckBoxField | t_hpdi_bizdatabill.fisexistsworkflow |  |  |
| inputdevicetype | 输入设备 | TextField | t_hpdi_bizdatabill.finputdevicetype |  |  |
| auditstatus | 审批状态 | BillStatusField | t_hpdi_bizdatabill.fauditstatus |  |  |
| adminorg | 行政组织 | HRAdminOrgField | t_hpdi_bizdatabill.fadminorgid | ✓ | haos_adminorghrf7 |
| bizitemgroup | 业务数据模板 | BasedataField | t_hpdi_bizdatabill.fbizitemgroupid | ✓ | hsbs_bizitemgroup |
| datastatus | 业务数据提报状态 | ComboField | t_hpdi_bizdatabill.fdatastatus |  |  |
| billtype | 单据类型 | ComboField | t_hpdi_bizdatabill.fbilltype |  |  |
| description | 备注 | MuliLangTextField | t_hpdi_bizdatabill_l.fdescription |  |  |
| bizdataobjrule | 业务数据对象规则 | BasedataField | t_hpdi_bizdatabill.fbizdataobjruleid |  | hsbs_bizobjruleconf |
| personcount | 总人数 | IntegerField | t_hpdi_bizdatabill.fpersoncount |  |  |
| bizitemcount | 业务项目数 | IntegerField | t_hpdi_bizdatabill.fbizitemcount |  |  |
| bizdatacount | 业务数据条目数 | IntegerField | t_hpdi_bizdatabill.fbizdatacount |  |  |
| summarycreator | 创建人 | TextField | — |  |  |
| bizdataruleversion | 业务对象规则版本 | TextField | t_hpdi_bizdatabill.fbizdataruleversion |  |  |
| bizdatafailcount | 提报失败条目数 | IntegerField | t_hpdi_bizdatabill.fbizdatafailcount |  |  |
| billname | 单据名称 | MuliLangTextField | t_hpdi_bizdatabill_l.fbillname | ✓ |  |
| calperiod | 薪资期间 | BasedataField | t_hpdi_bizdatabill.fcalperiodid | ✓ | hsbs_calperiod |
| handletype | 提报类型 | ComboField | t_hpdi_bizdatabill.fhandletype | ✓ |  |
| countdown | 推送算薪倒计时 | TextField | — |  |  |

## 实体: entryentity（业务数据） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| org | 算发薪管理组织 | OrgField | t_hpdi_bizdatabill.forgid | ✓ | bos_org |
| mulbizitem | 业务项目范围 | MulBasedataField | — |  |  |
| transsalarystatus | 业务数据推送状态 | ComboField | t_hpdi_bizdatabill.ftranssalarystatus |  |  |
| ischange | 分录状态是否变更 | CheckBoxField | t_hpdi_bizdatabill.fischange |  |  |
| summarycreatetime | 创建时间 | DateTimeField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hpdi_bizdatabill（主表） | 28 |
| t_hpdi_bizdatabill_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
