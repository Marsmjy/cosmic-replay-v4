---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_paynodegrp — 时间窗口模板

**表单编码**: `hsas_paynodegrp`  
**表单ID**: `2+=87WQM28PJ`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_paynodegrp（时间窗口模板） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_paynodegrp` | 主表 · 37 列 |
| `t_hsas_paynodegrpent` | 分录表 · 9 列 |
| `t_hsas_paynodegrp_l` | 多语言表 · 4 列 |
| `t_hsas_paynodegrpent_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_paynodegrpent.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_paynodegrpent_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_paynodegrpent.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_paynodegrp.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_paynodegrp.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_paynodegrpent.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_paynodegrp.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_paynodegrp.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_paynodegrp.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_paynodegrp.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_paynodegrp.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_paynodegrp.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_paynodegrp.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_paynodegrp.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_paynodegrp.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_paynodegrp.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_paynodegrp_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_paynodegrp_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_paynodegrp.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_paynodegrp.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_paynodegrp.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_paynodegrp.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_paynodegrp.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_paynodegrp.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_paynodegrp.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_paynodegrp.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_paynodegrp.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_paynodegrp.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_paynodegrp.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_paynodegrp.fhisversion |  |  |
| paynoderule | 时间规则基准 | ComboField | t_hsas_paynodegrp.fpaynoderule | ✓ |  |
| calperiodtype | 期间类型 | BasedataField | t_hsas_paynodegrp.fcalperiodtypeid | ✓ | hsbs_calperiodtype |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| nodenumber | 时间节点编码 | TextField | — |  |  |
| starttimecfg | 开始时间 | TextField | t_hsas_paynodegrpent.fstarttimecfg |  |  |
| endtimecfg | 截止时间 | TextField | t_hsas_paynodegrpent.fendtimecfg |  |  |
| enab | 使用状态 | BillStatusField | — |  |  |
| stat | 数据状态 | BillStatusField | — |  |  |
| starttimebox | 启用 | CheckBoxField | — |  |  |
| starttimeoperation | 操作 | TextField | — |  |  |
| workcalday | 日期规则 | TextField | — |  |  |
| holidayway | 休息日规则 | TextField | — |  |  |
| endtimeoperate | 操作 | TextField | — |  |  |
| endworkcalday | 日期规则 | TextField | — |  |  |
| endholidayway | 休息日规则 | TextField | — |  |  |
| calperiodway | 日期偏移设置 | TextField | — |  |  |
| endcalperiodway | 日期偏移设置 | TextField | — |  |  |
| calperiodfield | 时间锚点 | ComboField | — |  |  |
| endcalperiodfield | 时间锚点 | ComboField | — |  |  |
| workrule | 日历规则对应期间 | ComboField | — |  |  |
| endworkrule | 日历规则对应期间 | ComboField | — |  |  |
| entryboid | 分录业务ID | BigIntField | t_hsas_paynodegrpent.fentryboid |  |  |
| desc | 描述 | TextField | — |  |  |
| specifictime | 开始时间 | TextField | — |  |  |
| endspecifictime | 截止时间 | TextField | — |  |  |
| nodename | 时间节点名称 | MuliLangTextField | — | ✓ |  |
| showname | 显示名称 | MuliLangTextField | t_hsas_paynodegrp_l.fshowname |  |  |
| workplan | 公共日历 | QueryField | — | ✓ | hrcs_workingplanquery |
| country | 国家/地区 | BasedataField | t_hsas_paynodegrp.fcountryid | ✓ | bd_country |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_paynodegrp（主表） | 27 |
| t_hsas_paynodegrp_l | 3 |
| t_hsas_paynodegrpent | 6 |
| t_hsas_paynodegrpent_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 27 |
