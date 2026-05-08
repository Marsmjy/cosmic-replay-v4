---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FOLRY18YW
app_number: wtp
app_name: 工时假勤规则
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtp_qtformularule — 定额公式规则

**表单编码**: `wtp_qtformularule`  
**表单ID**: `410TPEYU//MI`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtformularule（定额公式规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtformularule` | 主表 · 33 列 |
| `t_wtp_qtformularuleentry` | 分录表 · 5 列 |
| `t_wtp_qtformularule_l` | 多语言表 · 3 列 |
| `t_wtp_qtformularuleentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_qtformularule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_qtformularule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_qtformularule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_qtformularule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_qtformularule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_qtformularule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_qtformularule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtformularule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_qtformularule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_qtformularule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_qtformularule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_qtformularule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_qtformularule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_qtformularule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_qtformularule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_qtformularule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_qtformularule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_qtformularule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_qtformularule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_qtformularule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_qtformularule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_qtformularule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_qtformularule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_qtformularule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_qtformularule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_qtformularule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_qtformularule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_qtformularule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_qtformularule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_qtformularule.fhisversion |  |  |

## 实体: entryentity（公式规则分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| confname | 名称 | BasedataPropField | — |  |  |
| formulaconfig | 编码 | BasedataField | t_wtp_qtformularuleentry.fformulaconfigid |  | wtp_qtformulaset |
| execordertext | 步骤内执行顺序 | TextField | — |  |  |
| confdesc | 描述 | BasedataPropField | — |  |  |
| remark | 备注 | MuliLangTextField | t_wtp_qtformularuleentry_l.fremark |  |  |
| execorder | 执行顺序 | IntegerField | t_wtp_qtformularuleentry.fexecorder |  |  |
| accstep | 关联核算步骤 | BasedataField | t_wtp_qtformularuleentry.faccstepid |  | wtp_accountsteps |
| entryboid | 分录boid | BigIntField | t_wtp_qtformularuleentry.fentryboid |  |  |
| confattr | 考勤项目 | TextField | — |  |  |
| accountplan | 核算方案 | BasedataField | t_wtp_qtformularule.faccountplanid | ✓ | wtp_accountplan |
| formulaconfigf7 | 公式配置F7 | MulBasedataField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtformularule（主表） | 28 |
| t_wtp_qtformularule_l | 3 |
| t_wtp_qtformularuleentry | 4 |
| t_wtp_qtformularuleentry_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
