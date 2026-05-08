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

# hsas_calstepcfg — 算发薪向导方案

**表单编码**: `hsas_calstepcfg`  
**表单ID**: `4G4C6D5DGZI1`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_calstepcfg（算发薪向导方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_calstepcfg` | 主表 · 23 列 |
| `t_hsas_stepdefpageent` | 分录表 · 2 列 |
| `t_hsas_stepcfgtreeent` | 分录表 · 18 列 |
| `t_hsas_calstepcfg_l` | 多语言表 · 3 列 |
| `t_hsas_stepcfgtreeent_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_calstepcfg.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_calstepcfg_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_calstepcfg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_calstepcfg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_calstepcfg.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_calstepcfg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_calstepcfg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_calstepcfg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_calstepcfg.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_calstepcfg.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_calstepcfg.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_calstepcfg.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_calstepcfg.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_calstepcfg.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_calstepcfg.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_calstepcfg.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_calstepcfg_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_calstepcfg_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_calstepcfg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_calstepcfg.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_calstepcfg.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| page | 跳转页面 | BasedataField | — | ✓ | hsas_stepregistry |
| commonfiltercolumns | 指定常用过滤条件 | MulComboField | — |  |  |
| btns | 指定显示按钮 | TextField | — |  |  |
| iscalpersonfiltershow | 基于核算名单过滤数据 | CheckBoxField | — |  |  |
| calpersonfieldshow | 核算名单查询字段 | TextField | — | ✓ |  |
| calpersontargetshow | 跳转页面查询字段 | TextField | — | ✓ |  |
| istaskfiltershow | 基于核算任务过滤数据 | CheckBoxField | — |  |  |
| taskfieldshow | 核算任务查询字段 | TextField | — | ✓ |  |
| tasktargetshow | 跳转页面查询字段 | TextField | — | ✓ |  |
| isperiodfiltershow | 基于薪资期间过滤数据 | CheckBoxField | — |  |  |
| periodstartfieldshow | 开始日期字段 | ComboField | — | ✓ |  |
| periodendfieldshow | 结束日期字段 | ComboField | — | ✓ |  |
| periodtargetshow | 跳转页面查询字段 | TextField | — | ✓ |  |
| filterpluginshow | 自定义过滤 | BasedataField | — |  | hsas_stepfilterplugin |

## 实体: stepdefpageent（默认跳转规则配置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| caltaskstatus | 薪资核算任务.任务状态 | MulComboField | t_hsas_stepdefpageent.fcaltaskstatus | ✓ |  |
| skipstepdetail | 跳转步骤 | TextField | — | ✓ |  |
| stepentid | 跳转步骤 | BigIntField | t_hsas_stepdefpageent.fstepentid | ✓ |  |

## 实体: cfgtreeent（算发薪向导） [TreeEntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| stepname | 向导步骤名称 | MuliLangTextField | t_hsas_stepcfgtreeent_l.fstepname |  |  |
| substepname | 子步骤名称 | MuliLangTextField | t_hsas_stepcfgtreeent_l.fsubstepname |  |  |
| stepdesc | 说明 | MuliLangTextField | t_hsas_stepcfgtreeent_l.fstepdesc |  |  |
| entity | 跳转页面 | BasedataField | t_hsas_stepcfgtreeent.fentityid |  | hsas_stepregistry |
| comfilterctls | 指定常用过滤条件 | TextField | t_hsas_stepcfgtreeent.fcomfilterctls |  |  |
| buttons | 指定显示按钮 | TextField | t_hsas_stepcfgtreeent.fbuttons |  |  |
| filtercondition | 过滤控件 | TextAreaField | t_hsas_stepcfgtreeent.ffiltercondition |  |  |
| filterplugin | 自定义过滤 | BasedataField | t_hsas_stepcfgtreeent.ffilterpluginid |  | hsas_stepfilterplugin |
| iscalpersonfilter | 基于核算名单过滤数据 | CheckBoxField | t_hsas_stepcfgtreeent.fiscalpersonfilter |  |  |
| istaskfilter | 基于核算任务过滤数据 | CheckBoxField | t_hsas_stepcfgtreeent.fistaskfilter |  |  |
| isperiodfilter | 基于薪资期间过滤数据 | CheckBoxField | t_hsas_stepcfgtreeent.fisperiodfilter |  |  |
| calpersonfield | 核算名单查询字段 | TextField | t_hsas_stepcfgtreeent.fcalpersonfield |  |  |
| calpersontarget | 核算名单对应跳转页面查询字段 | TextField | t_hsas_stepcfgtreeent.fcalpersontarget |  |  |
| taskfield | 核算任务查询字段 | TextField | — |  |  |
| tasktarget | 核算任务对应跳转页面查询字段 | TextField | — |  |  |
| periodstartfield | 开始日期字段 | TextField | t_hsas_stepcfgtreeent.fperiodstartfield |  |  |
| periodendfield | 结束日期字段 | TextField | t_hsas_stepcfgtreeent.fperiodendfield |  |  |
| periodtarget | 薪资期间对应跳转页面查询字段 | TextField | t_hsas_stepcfgtreeent.fperiodtarget |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_calstepcfg（主表） | 18 |
| t_hsas_calstepcfg_l | 3 |
| t_hsas_stepcfgtreeent | 13 |
| t_hsas_stepcfgtreeent_l | 3 |
| t_hsas_stepdefpageent | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 23 |
