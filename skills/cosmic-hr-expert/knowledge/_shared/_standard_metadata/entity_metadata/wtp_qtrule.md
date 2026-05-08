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

# wtp_qtrule — 定额规则

**表单编码**: `wtp_qtrule`  
**表单ID**: `33W2A7R8IRA1`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtrule（定额规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtrule` | 主表 · 32 列 |
| `t_wtp_qtruleentry` | 分录表 · 12 列 |
| `t_wtp_qtrule_l` | 多语言表 · 3 列 |
| `t_wtp_qtruleentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_qtrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_qtrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_qtrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_qtrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_qtrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_qtrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_qtrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_qtrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_qtrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_qtrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_qtrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_qtrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_qtrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_qtrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_qtrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_qtrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_qtrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_qtrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_qtrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_qtrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_qtrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_qtrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_qtrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_qtrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_qtrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_qtrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_qtrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_qtrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_qtrule.fhisversion |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| qtuseconfig | 定额使用配置 | BasedataField | t_wtp_qtruleentry.fqtuseconfigid |  | wtp_qtuseconfig |
| qtgenconfig | 定额生成配置 | BasedataField | t_wtp_qtruleentry.fqtgenconfigid |  | wtp_qtgenconfig |
| useqc | 使用限定条件 | BasedataField | t_wtp_qtruleentry.fuseqcid |  | wtp_qtqualification |
| qtoverdraw | 定额透支配置 | BasedataField | t_wtp_qtruleentry.fqtoverdrawid |  | wtp_qtoverdraw |
| overqc | 透支限定条件 | BasedataField | t_wtp_qtruleentry.foverqcid |  | wtp_qtqualification |
| qtcarrydown | 定额结转配置 | BasedataField | t_wtp_qtruleentry.fqtcarrydownid |  | wtp_qtcarrydown |
| carryqc | 结转限定条件 | BasedataField | t_wtp_qtruleentry.fcarryqcid |  | wtp_qtqualification |
| toconvert | 离职折算 | BasedataField | t_wtp_qtruleentry.ftoconvertid |  | wtp_qtturnoverconver |
| inconvert | 入职折算 | BasedataField | t_wtp_qtruleentry.finconvertid |  | wtp_qtinconversion |
| qttype | 定额名称 | BasedataField | t_wtp_qtruleentry.fqttypeid |  | wtp_qttype |
| entryboid | 分录BOID | BigIntField | t_wtp_qtruleentry.fentryboid |  |  |
| ruledesc | 描述 | MuliLangTextField | t_wtp_qtruleentry_l.fruledesc |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtrule（主表） | 27 |
| t_wtp_qtrule_l | 3 |
| t_wtp_qtruleentry | 11 |
| t_wtp_qtruleentry_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
