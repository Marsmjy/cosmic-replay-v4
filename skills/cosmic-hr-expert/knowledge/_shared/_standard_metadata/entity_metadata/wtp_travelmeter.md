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

# wtp_travelmeter — 出差基础配置

**表单编码**: `wtp_travelmeter`  
**表单ID**: `1HD9V/4KCIFD`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_travelmeter（出差基础配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_tripbaseset` | 主表 · 50 列 |
| `t_wtp_tripbaseset_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_tripbaseset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_tripbaseset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_tripbaseset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_tripbaseset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_tripbaseset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_tripbaseset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_tripbaseset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_tripbaseset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_tripbaseset.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_tripbaseset.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_tripbaseset.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_tripbaseset.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_tripbaseset.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_tripbaseset.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_tripbaseset.findex |  |  |
| simplename | 简称 | MuliLangTextField | — |  |  |
| description | 描述 | MuliLangTextField | t_wtp_tripbaseset_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_tripbaseset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_tripbaseset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_tripbaseset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_tripbaseset.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_tripbaseset.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_tripbaseset.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_tripbaseset.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_tripbaseset.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_tripbaseset.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_tripbaseset.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_tripbaseset.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_tripbaseset.fhisversion |  |  |
| auditbegindate | 开始日期校验 | ComboField | t_wtp_tripbaseset.fauditbegindate | ✓ |  |
| auditenddate | 结束日期校验 | ComboField | t_wtp_tripbaseset.fauditenddate | ✓ |  |
| unit | 计量单位 | ComboField | t_wtp_tripbaseset.funit | ✓ |  |
| isadvanceapple | 最早预提限制 | CheckBoxField | t_wtp_tripbaseset.fisadvanceapple |  |  |
| advancenumber | 期限 | IntegerField | t_wtp_tripbaseset.fadvancenumber |  |  |
| isvehicle | 交通工具 | CheckBoxField | t_wtp_tripbaseset.fisvehicle |  |  |
| isreasonrequired | 出差原因 | CheckBoxField | t_wtp_tripbaseset.fisreasonrequired |  |  |
| isplace | 出差地 | CheckBoxField | t_wtp_tripbaseset.fisplace |  |  |
| travelminnumber | 最小出差时长 | DecimalField | t_wtp_tripbaseset.ftravelminnumber |  |  |
| advanceunit | 需提前周期 | ComboField | t_wtp_tripbaseset.fadvanceunit |  |  |
| isafterapple | 补提限制 | CheckBoxField | t_wtp_tripbaseset.fisafterapple |  |  |
| afterunit | 可补提周期 | ComboField | t_wtp_tripbaseset.fafterunit |  |  |
| afternumber | 期限 | IntegerField | t_wtp_tripbaseset.fafternumber |  |  |
| isneedenclosure | 附件 | CheckBoxField | t_wtp_tripbaseset.fisneedenclosure |  |  |
| daterangecondition | 日期范围 | TextAreaField | t_wtp_tripbaseset.fdaterangecondition |  |  |
| islateadvanceapple | 最晚预提限制 | CheckBoxField | t_wtp_tripbaseset.fislateadvanceapple |  |  |
| lateadvancenumber | 期限 | IntegerField | t_wtp_tripbaseset.flateadvancenumber |  |  |
| lateadvanceunit | 可提前周期 | ComboField | t_wtp_tripbaseset.flateadvanceunit |  |  |
| travelmaxnumber | 最大出差时长 | DecimalField | t_wtp_tripbaseset.ftravelmaxnumber |  |  |
| isshiftot | 班内加班时段计入申请时长 | CheckBoxField | t_wtp_tripbaseset.fisshiftot |  |  |
| isapplyspan | 控制单位时长 | CheckBoxField | t_wtp_tripbaseset.fisapplyspan |  |  |
| applyspanday | 单位时长-天 | ComboField | — |  |  |
| applyspanhour | 单位时长-时分 | ComboField | — |  |  |
| applyspan | 单位时长 | ComboField | t_wtp_tripbaseset.fapplyspan | ✓ |  |
| ruledate | 规则控件日期 | DateField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_tripbaseset（主表） | 48 |
| t_wtp_tripbaseset_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
