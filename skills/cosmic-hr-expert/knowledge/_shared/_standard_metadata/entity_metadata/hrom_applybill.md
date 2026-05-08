---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 3+JJD4IUAS56
app_number: hrom
app_name: HR运营管理
cloud_number: HROS
cloud_name: HR运营服务云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hrom_applybill — HR服务申请基础模板

**表单编码**: `hrom_applybill`  
**表单ID**: `51U/E4YZZXIA`  
**归属**: HR运营服务云 / HR运营管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hrom_applybill（HR服务申请基础模板） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hrom_applybill` | 主表 · 21 列 |
| `t_hrom_applybill_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_hrom_applybill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_hrom_applybill.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_hrom_applybill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrom_applybill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_hrom_applybill.fauditorid |  | bos_user |
| auditdate | 审核时间 | DateTimeField | t_hrom_applybill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrom_applybill.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_hrom_applybill.fcreatetime |  |  |
| org | HR管理组织 | OrgField | t_hrom_applybill.forgid | ✓ | bos_org |
| barcode | 条形码 | TextField | t_hrom_applybill.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_hrom_applybill.finputdevicetype |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_hrom_applybill.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_hrom_applybill.fauditstatus | ✓ |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | — |  |  |
| billtype | 单据类型 | BillTypeField | — | ✓ | bos_billtype |
| applycontent | 申请内容 | MuliLangTextField | t_hrom_applybill_l.fapplycontent |  |  |
| applytype | 服务申请类型 | ComboField | t_hrom_applybill.fapplytype |  |  |
| applicant | 申请人 | UserField | t_hrom_applybill.fapplicantid |  | bos_user |
| applicantno | 申请人工号 | BasedataPropField | — |  |  |
| positionhr | 岗位 | BasedataField | — |  | bos_position |
| applytime | 申请日期 | DateField | t_hrom_applybill.fapplytime |  |  |
| sourcetype | 单据来源类型 | ComboField | t_hrom_applybill.fsourcetype |  |  |
| company | 所属公司 | BasedataField | — |  | bos_adminorg |
| applydept | 申请部门 | BasedataField | t_hrom_applybill.fapplydeptid |  | haos_adminorghrf7 |
| applierpositionv | 职位 | MuliLangTextField | — |  |  |
| dept | 任职部门 | BasedataField | t_hrom_applybill.fdeptid |  | bos_adminorg |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrom_applybill（主表） | 19 |
| t_hrom_applybill_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
