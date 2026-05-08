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

# wtp_qtoverdraw — 定额透支配置

**表单编码**: `wtp_qtoverdraw`  
**表单ID**: `33W2MKFPNT81`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtoverdraw（定额透支配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtoverdraw` | 主表 · 40 列 |
| `t_wtp_qtoverdraw_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_qtoverdraw.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_qtoverdraw_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_qtoverdraw.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_qtoverdraw.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_qtoverdraw.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_qtoverdraw.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_qtoverdraw.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtoverdraw.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_qtoverdraw.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_qtoverdraw.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_qtoverdraw.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_qtoverdraw.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_qtoverdraw.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_qtoverdraw.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_qtoverdraw.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_qtoverdraw.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_qtoverdraw_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_qtoverdraw_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_qtoverdraw.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_qtoverdraw.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_qtoverdraw.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_qtoverdraw.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_qtoverdraw.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_qtoverdraw.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_qtoverdraw.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_qtoverdraw.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_qtoverdraw.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_qtoverdraw.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_qtoverdraw.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_qtoverdraw.fhisversion |  |  |
| overdrawtype | 透支方式 | ComboField | t_wtp_qtoverdraw.foverdrawtype | ✓ |  |
| roundrule | 舍入规则 | BasedataField | t_wtp_qtoverdraw.froundruleid |  | wtbd_roundrule |
| rawodvalue | 标准透支时长 | BasedataField | t_wtp_qtoverdraw.frawodvalueid | ✓ | wtbd_attitem |
| odvalue | 透支时长 | BasedataField | t_wtp_qtoverdraw.fodvalueid | ✓ | wtbd_attitem |
| value | 数值-db隐藏 | DecimalField | — |  |  |
| percent | 比例(%)-db隐藏 | DecimalField | t_wtp_qtoverdraw.fpercent |  |  |
| svalue | 值 | DecimalField | — |  |  |
| spercent | 比例(%) | DecimalField | — |  |  |
| canodvalue | 可透支时长 | BasedataField | t_wtp_qtoverdraw.fcanodvalueid | ✓ | wtbd_attitem |
| odinvalivalue | 透支失效时长 | BasedataField | t_wtp_qtoverdraw.fodinvalivalue |  | wtbd_attitem |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtoverdraw（主表） | 34 |
| t_wtp_qtoverdraw_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
