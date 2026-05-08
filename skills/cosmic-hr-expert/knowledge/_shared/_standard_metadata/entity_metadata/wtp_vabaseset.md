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

# wtp_vabaseset — 休假基础配置

**表单编码**: `wtp_vabaseset`  
**表单ID**: `22X3JMOWEGTF`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_vabaseset（休假基础配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_vabaseset` | 主表 · 63 列 |
| `t_wtp_vabasesetbreast` | 分录表 · 4 列 |
| `t_wtp_vabasesettentry` | 分录表 · 2 列 |
| `t_wtp_vabaseset_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_vabaseset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_vabaseset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_vabaseset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_vabaseset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_vabaseset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_vabaseset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_vabaseset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_vabaseset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_vabaseset.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_vabaseset.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_vabaseset.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_vabaseset.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_vabaseset.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_vabaseset.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_vabaseset.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_vabaseset.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_vabaseset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_vabaseset_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_vabaseset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_vabaseset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_vabaseset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_vabaseset.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_vabaseset.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_vabaseset.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_vabaseset.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_vabaseset.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_vabaseset.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_vabaseset.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_vabaseset.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_vabaseset.fhisversion |  |  |
| isreasonrequire | 休假原因必填 | CheckBoxField | t_wtp_vabaseset.fisreasonrequire |  |  |
| startdatecheck | 开始日期校验 | ComboField | t_wtp_vabaseset.fstartdatecheck | ✓ |  |
| enddatecheck | 结束日期校验 | ComboField | t_wtp_vabaseset.fenddatecheck | ✓ |  |
| unit | 计量单位 | ComboField | t_wtp_vabaseset.funit | ✓ |  |
| enclosurelimit | 附件限制 | ComboField | t_wtp_vabaseset.fenclosurelimit | ✓ |  |
| excessval_real | 超出额度(存放值) | IntegerField | — |  |  |
| isrepair | 补提限制 | CheckBoxField | t_wtp_vabaseset.fisrepair |  |  |
| repairperiod | 可补提周期 | ComboField | t_wtp_vabaseset.frepairperiod | ✓ |  |
| maxrepairtime_real | 补提最大期限(存放值) | IntegerField | — |  |  |
| aheadperiod | 需提前周期 | ComboField | t_wtp_vabaseset.faheadperiod | ✓ |  |
| maxaheadtime_real | 预提最大期限(存放值) | IntegerField | — |  |  |
| isahead | 最早预提限制 | CheckBoxField | t_wtp_vabaseset.fisahead |  |  |
| minholidaytime | 最小休假时长 | DecimalField | t_wtp_vabaseset.fminholidaytime |  |  |
| excessval | 超出额度 | IntegerField | t_wtp_vabaseset.fexcessval |  |  |
| maxaheadtime | 期限 | IntegerField | t_wtp_vabaseset.fmaxaheadtime |  |  |
| maxrepairtime | 期限 | IntegerField | t_wtp_vabaseset.fmaxrepairtime |  |  |
| isspecialholiday | 特殊假 | CheckBoxField | t_wtp_vabaseset.fisspecialholiday |  |  |
| specialdaytype | 特殊假类型 | ComboField | t_wtp_vabaseset.fspecialdaytype | ✓ |  |

## 实体: wtp_vabasesetbreast（哺乳假配置分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| babynum | 本次胞胎数 | IntegerField | t_wtp_vabasesetbreast.fbabynum |  |  |
| offtimeunit | 单位 | ComboField | t_wtp_vabasesetbreast.fofftimeunit |  |  |
| entryboid | 分录BOID | BigIntField | t_wtp_vabasesettentry.fentryboid |  |  |
| offtimeperday | 每日可休时长 | DecimalField | t_wtp_vabasesetbreast.fofftimeperday | ✓ |  |
| breastdaytype | 哺乳假休假方式 | MulComboField | — |  |  |
| breastdaytypedata | 哺乳假休假方式 | MulBasedataField | — | ✓ |  |
| daterangecondition | 日期范围 | TextAreaField | t_wtp_vabaseset.fdaterangecondition |  |  |
| maxholidaytime | 最大休假时长 | DecimalField | t_wtp_vabaseset.fmaxholidaytime |  |  |
| timecalctype | 休假时长计算方式 | ComboField | t_wtp_vabaseset.ftimecalctype | ✓ |  |
| halfdaytype | 半天分割方式 | ComboField | t_wtp_vabaseset.fhalfdaytype | ✓ |  |
| isaheadmax | 最晚预提限制 | CheckBoxField | t_wtp_vabaseset.fisaheadmax |  |  |
| aheadmaxunit | 可提前周期 | ComboField | t_wtp_vabaseset.faheadmaxunit | ✓ |  |
| ischeckpersoninfo | 校验员工信息 | CheckBoxField | t_wtp_vabaseset.fischeckpersoninfo |  |  |
| personinfo | 员工信息字段 | MulComboField | — | ✓ |  |
| personcondition | 条件逻辑 | ComboField | — | ✓ |  |
| custominfotips | 自定义提醒文案 | MuliLangTextField | t_wtp_vabaseset_l.fcustominfotips |  |  |
| personexpression | 员工信息字段表达式 | TextField | t_wtp_vabaseset.fpersonexpression |  |  |
| iscontainovertime | 班内加班时段计入申请时长 | CheckBoxField | t_wtp_vabaseset.fiscontainovertime |  |  |
| aheadmaxvalfield | 期限 | IntegerField | — |  |  |
| isallowoverlap | 允许休假类型重叠 | CheckBoxField | t_wtp_vabaseset.fisallowoverlap |  |  |

## 实体: vatypeentry（允许休假类型重叠分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| vacationtype | 名称 | BasedataField | t_wtp_vabasesettentry.fvacationtypeid | ✓ | wtbd_vacationtype |
| basedatapropfield | 编码 | BasedataPropField | — |  |  |
| basedatapropfield1 | 创建组织 | BasedataPropField | — |  |  |
| basedatapropfield2 | 描述 | BasedataPropField | — |  |  |
| entryboidvatypeentry | 分录BOID | BigIntField | — |  |  |
| overlapvatype | 允许重叠休假类型F7 | BasedataField | — |  | wtbd_vacationtype |
| isapplyspan | 控制单位时长 | CheckBoxField | t_wtp_vabaseset.fisapplyspan |  |  |
| applyspan | 单位时长 | ComboField | t_wtp_vabaseset.fapplyspan | ✓ |  |
| applyspanday | 单位时长-天 | ComboField | — |  |  |
| applyspanhour | 单位时长-时分 | ComboField | — |  |  |
| ruledate | 规则控件日期 | DateField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_vabaseset（主表） | 54 |
| t_wtp_vabaseset_l | 4 |
| t_wtp_vabasesetbreast | 3 |
| t_wtp_vabasesettentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 22 |
