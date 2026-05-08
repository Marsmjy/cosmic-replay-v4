---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 19KOKMMSZFZJ
app_number: wtbd
app_name: 工时假勤基础资料
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtbd_takecardrule — 取卡规则

**表单编码**: `wtbd_takecardrule`  
**表单ID**: `2+IRTTACPOFP`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_takecardrule（取卡规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_tcardrule` | 主表 · 53 列 |
| `t_wtbd_tcardruleentry` | 分录表 · 10 列 |
| `t_wtbd_tcardrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_tcardrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_tcardrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_tcardrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_tcardrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_tcardrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_tcardrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_tcardrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_tcardrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_tcardrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_tcardrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_tcardrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_tcardrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_tcardrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_tcardrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_tcardrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_tcardrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_tcardrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_tcardrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_tcardrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_tcardrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_tcardrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtbd_tcardrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtbd_tcardrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtbd_tcardrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtbd_tcardrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtbd_tcardrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtbd_tcardrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtbd_tcardrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtbd_tcardrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtbd_tcardrule.fhisversion |  |  |
| takecardconfig | 取卡配置 | BasedataField | t_wtbd_tcardrule.ftakecardconfigid |  | wtbd_takecardconfig |

## 实体: wtbd_tcardruleentry（取卡范围单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| timeseq | 时段次序 | IntegerField | t_wtbd_tcardruleentry.ftimeseq | ✓ |  |
| sbeforescope | 时段开始前范围 | IntegerField | t_wtbd_tcardruleentry.fsbeforescope |  |  |
| safterscope | 时段开始后范围 | IntegerField | t_wtbd_tcardruleentry.fsafterscope |  |  |
| stakecardrule | 时段开始取卡方式 | TextField | t_wtbd_tcardruleentry.fstakecardrule |  |  |
| ebeforescope | 时段结束前范围 | IntegerField | t_wtbd_tcardruleentry.febeforescope |  |  |
| eafterscope | 时段结束后范围 | IntegerField | t_wtbd_tcardruleentry.feafterscope |  |  |
| etakecardrule | 时段结束取卡方式 | TextField | t_wtbd_tcardruleentry.fetakecardrule |  |  |
| stakecardsamelen | 时段开始最近卡点距离一致时 | TextField | t_wtbd_tcardruleentry.fstakecardsamelen |  |  |
| etakecardsamelen | 时段结束最近卡点距离一致时 | TextField | t_wtbd_tcardruleentry.fetakecardsamelen |  |  |
| entryboid | 分录BOID | BigIntField | t_wtbd_tcardruleentry.fentryboid |  |  |
| tconfigdes | 取卡规则描述 | TextAreaField | t_wtbd_tcardrule.ftconfigdes |  |  |
| offmode | OFF班无时段取卡方式 | ComboField | t_wtbd_tcardrule.foffmode |  |  |
| takecardstarttime | 取卡开始时间 | TimeField | t_wtbd_tcardrule.ftakecardstarttime | ✓ |  |
| takecardendtime | 取卡结束时间 | TimeField | t_wtbd_tcardrule.ftakecardendtime | ✓ |  |
| classtimenum | 应出勤时段 | ComboField | t_wtbd_tcardrule.fclasstimenum |  |  |
| classgetnum | 取卡数 | ComboField | t_wtbd_tcardrule.fclassgetnum |  |  |
| takecardshape | 取卡图示 | TextAreaField | t_wtbd_tcardrule.ftakecardshape |  |  |
| isoff | 班次无计划时段 | CheckBoxField | t_wtbd_tcardrule.fisoff |  |  |
| iscardshare | 卡点共用 | CheckBoxField | t_wtbd_tcardrule.fiscardshare |  |  |
| iscontinuecard | 连续班次卡点自动填充 | CheckBoxField | t_wtbd_tcardrule.fiscontinuecard |  |  |
| isotapplytakecard | 按加班单取卡 | CheckBoxField | t_wtbd_tcardrule.fisotapplytakecard |  |  |
| isvaapplytakecard | 按休假单取卡 | CheckBoxField | t_wtbd_tcardrule.fisvaapplytakecard |  |  |
| intersectionrule | 取卡交集规则 | CheckBoxField | t_wtbd_tcardrule.fintersectionrule |  |  |
| istpapplytakecard | 按出差单取卡 | CheckBoxField | t_wtbd_tcardrule.fistpapplytakecard |  |  |
| isrestcardshare | 休息时段卡点共用 | CheckBoxField | t_wtbd_tcardrule.fisrestcardshare |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_tcardrule（主表） | 43 |
| t_wtbd_tcardrule_l | 3 |
| t_wtbd_tcardruleentry | 10 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
