---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1IMTC4ANI0KA
app_number: brm
app_name: 业务规则管理
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# brm_policy_edit — 策略管理

**表单编码**: `brm_policy_edit`  
**表单ID**: `1IMVVZKCUDNX`  
**归属**: HR基础服务云 / 业务规则管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: brm_policy_edit（策略管理） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_brm_policy` | 主表 · 27 列 |
| `t_brm_policyscope` | 分录表 · 2 列 |
| `t_brm_drlfilter` | 分录表 · 10 列 |
| `t_brm_policy_l` | 多语言表 · 3 列 |
| `t_brm_drlfilter_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 策略编码 | TextField | t_brm_drlfilter.fnumber | ✓ |  |
| name | 策略名称 | MuliLangTextField | t_brm_drlfilter_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_brm_policy.fstatus |  |  |
| creator | 创建人 | CreaterField | t_brm_policy.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_brm_policy.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_brm_drlfilter.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_brm_policy.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_brm_policy.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_brm_policy.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_brm_policy_l.fsimplename |  |  |
| description | 策略描述 | MuliLangTextField | t_brm_drlfilter_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_brm_policy.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_brm_policy.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_brm_policy.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_brm_policy.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| bizappid | 所属应用 | BasedataField | t_brm_drlfilter.fbizappid | ✓ | hbp_devportal_bizapp |
| policymode | 策略模式 | ComboField | t_brm_policy.fpolicymode | ✓ |  |
| policytype | 策略分类 | ComboField | t_brm_policy.fpolicytype | ✓ |  |
| scene | 所属场景 | BasedataField | t_brm_drlfilter.fsceneid | ✓ | brm_scene |
| retrundefault | 返回默认结果 | CheckBoxField | t_brm_policy.fretrundefault |  |  |

## 实体: entrybulist（业务单元分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entitybu | 组织基础资料 | BasedataField | t_brm_policyscope.fentitybuid |  | bos_org |
| bunumber | 编码 | BasedataPropField | — |  |  |
| buname | 组织 | BasedataPropField | — |  |  |
| containssub | 包含下级 | CheckBoxField | t_brm_policyscope.fcontainssub |  |  |

## 实体: entryrulelist（规则信息分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rulenumber | 规则编码 | TextField | — |  |  |
| ruleorder | 优先级 | IntegerField | t_brm_drlfilter.fruleorder |  |  |
| rulebizapp | 所属应用 | BasedataField | — |  | bos_devportal_bizapp |
| rulescene | 所属场景 | BasedataField | — |  | brm_scene |
| templatenumber | 模板编码 | TextField | t_brm_drlfilter.ftemplatenumber |  |  |
| ruleenable | 使用状态 | CheckBoxField | — |  |  |
| filtercondition | 条件 | TextField | — |  |  |
| filterresult | 结果 | TextField | — |  |  |
| rulename | 规则名称 | MuliLangTextField | — |  |  |
| ruledescription | 规则描述 | MuliLangTextField | — |  |  |
| adminorg | 行政组织 | HRMulAdminOrgField | — |  |  |
| enablecombo | 使用状态 | ComboField | — |  |  |
| createbu | 创建组织 | OrgField | t_brm_policy.fcreatebuid | ✓ | bos_org |
| results | 默认返回结果 | TextField | t_brm_drlfilter.fresults |  |  |
| rulesize | 规则 | TextField | — |  |  |
| rostercondition | 特殊名单条件 | TextField | t_brm_policy.frostercondition |  |  |
| rosterresult | 特殊名单结果 | TextField | t_brm_policy.frosterresult |  |  |
| enablelist | 启用特殊名单 | CheckBoxField | t_brm_policy.fenablelist |  |  |
| enableminfirst | 启用组织明细优先 | CheckBoxField | t_brm_policy.fenableminfirst |  |  |
| orgparam | 行政组织参数 | ComboField | t_brm_policy.forgparam |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_brm_policy（主表） | 19 |
| t_brm_drlfilter | 7 |
| t_brm_drlfilter_l | 2 |
| t_brm_policy_l | 1 |
| t_brm_policyscope | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
