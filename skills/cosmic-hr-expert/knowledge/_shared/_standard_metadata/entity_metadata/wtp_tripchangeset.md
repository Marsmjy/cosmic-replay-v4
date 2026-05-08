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

# wtp_tripchangeset — 出差变更配置

**表单编码**: `wtp_tripchangeset`  
**表单ID**: `29YC1849WANO`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_tripchangeset（出差变更配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_tripchangeset` | 主表 · 36 列 |
| `t_wtp_triptypeentry` | 分录表 · 2 列 |
| `t_wtp_tripchangeset_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_tripchangeset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_tripchangeset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_tripchangeset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_tripchangeset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_tripchangeset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_tripchangeset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_tripchangeset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_tripchangeset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_tripchangeset.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_tripchangeset.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_tripchangeset.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_tripchangeset.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_tripchangeset.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_tripchangeset.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_tripchangeset.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_tripchangeset.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_tripchangeset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_tripchangeset_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_tripchangeset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_tripchangeset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_tripchangeset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_tripchangeset.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_tripchangeset.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_tripchangeset.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_tripchangeset.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_tripchangeset.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_tripchangeset.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_tripchangeset.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_tripchangeset.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_tripchangeset.fhisversion |  |  |
| bgtimetype | 时间变更方式 | ComboField | t_wtp_tripchangeset.fbgtimetype |  |  |
| istypechange | 限制变更出差类型 | CheckBoxField | t_wtp_tripchangeset.fistypechange |  |  |

## 实体: traveltypes（可变更类型分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| traveltype | 编码 | BasedataField | t_wtp_triptypeentry.ftraveltypeid |  | wtbd_traveltype |
| basedatapropfield | 名称 | BasedataPropField | — |  |  |
| basedatapropfield1 | 创建组织 | BasedataPropField | — |  |  |
| basedatapropfield2 | 是否系统预置 | BasedataPropField | — |  |  |
| basedatapropfield3 | 使用状态 | BasedataPropField | — |  |  |
| basedatapropfield4 | 控制策略 | BasedataPropField | — |  |  |
| entryboid | 分录BOID | BigIntField | t_wtp_triptypeentry.fentryboid |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldname | 字段名 | TextField | — |  |  |
| fielddesc | 说明 | TextField | — |  |  |
| fieldstatus | 状态 | CheckBoxField | — |  |  |
| entryboid2 | 分录BOID2 | BigIntField | — |  |  |
| isvehicle | 交通工具 | CheckBoxField | t_wtp_tripchangeset.fisvehicle |  |  |
| isdestination | 出差地 | CheckBoxField | t_wtp_tripchangeset.fisdestination |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_tripchangeset（主表） | 31 |
| t_wtp_tripchangeset_l | 3 |
| t_wtp_triptypeentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 15 |
