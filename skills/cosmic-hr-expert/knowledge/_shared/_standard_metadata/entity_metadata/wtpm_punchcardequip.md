---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FSW4YM0ZO
app_number: wtpm
app_name: 打卡管理
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtpm_punchcardequip — 打卡设备

**表单编码**: `wtpm_punchcardequip`  
**表单ID**: `2O349=PD5U+/`  
**归属**: 工时假勤云 / 打卡管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtpm_punchcardequip（打卡设备） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtpm_punchcardequip` | 主表 · 28 列 |
| `t_wtpm_punchcardequip_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 识别码 | TextField | t_wtpm_punchcardequip.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtpm_punchcardequip_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtpm_punchcardequip.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtpm_punchcardequip.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtpm_punchcardequip.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtpm_punchcardequip.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtpm_punchcardequip.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtpm_punchcardequip.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtpm_punchcardequip.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtpm_punchcardequip.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtpm_punchcardequip.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtpm_punchcardequip.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtpm_punchcardequip.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtpm_punchcardequip.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtpm_punchcardequip.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtpm_punchcardequip.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtpm_punchcardequip_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtpm_punchcardequip_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtpm_punchcardequip.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtpm_punchcardequip.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtpm_punchcardequip.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| atteplace | 考勤地点 | BasedataField | t_wtpm_punchcardequip.fatteplaceid |  | hbss_workplace |
| signsource | 打卡来源 | BasedataField | t_wtpm_punchcardequip.fsignsourceid | ✓ | wtbd_signsource |
| timezone | 时区 | BasedataField | t_wtpm_punchcardequip.ftimezoneid | ✓ | inte_timezone |
| dependency | 国家/地区 | BasedataField | t_wtpm_punchcardequip.fdependencyid |  | bd_country |
| address | 设备位置 | MuliLangTextField | t_wtpm_punchcardequip_l.faddress |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtpm_punchcardequip（主表） | 22 |
| t_wtpm_punchcardequip_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
