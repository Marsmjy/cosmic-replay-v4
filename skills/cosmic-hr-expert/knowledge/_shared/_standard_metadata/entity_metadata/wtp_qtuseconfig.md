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

# wtp_qtuseconfig — 定额使用配置

**表单编码**: `wtp_qtuseconfig`  
**表单ID**: `33T56+4VN+YH`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtuseconfig（定额使用配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtuseconfig` | 主表 · 44 列 |
| `t_wtp_qtuseconfig_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_qtuseconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_qtuseconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_qtuseconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_qtuseconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_qtuseconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_qtuseconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_qtuseconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtuseconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_qtuseconfig.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_qtuseconfig.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_qtuseconfig.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_qtuseconfig.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_qtuseconfig.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_qtuseconfig.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_qtuseconfig.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_qtuseconfig.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_qtuseconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_qtuseconfig_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_qtuseconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_qtuseconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_qtuseconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_qtuseconfig.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_qtuseconfig.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_qtuseconfig.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_qtuseconfig.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_qtuseconfig.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_qtuseconfig.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_qtuseconfig.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_qtuseconfig.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_qtuseconfig.fhisversion |  |  |
| usestartdate | 使用开始日期等于 | ComboField | t_wtp_qtuseconfig.fusestartdate | ✓ |  |
| useenddate | 使用结束日期等于 | ComboField | t_wtp_qtuseconfig.fuseenddate | ✓ |  |
| startlimit | 特定日期前不可用 | BasedataField | t_wtp_qtuseconfig.fstartlimitid |  | wtbd_cycrefdate |
| endlimit | 特定日期后不可用 | BasedataField | t_wtp_qtuseconfig.fendlimitid |  | wtbd_cycrefdate |
| startdelay | 浮动时长-db隐藏 | IntegerField | t_wtp_qtuseconfig.fstartdelay |  |  |
| enddelay | 浮动时长-db隐藏 | IntegerField | t_wtp_qtuseconfig.fenddelay |  |  |
| startdelayunit | 单位 | ComboField | t_wtp_qtuseconfig.fstartdelayunit | ✓ |  |
| enddelayunit | 单位 | ComboField | t_wtp_qtuseconfig.fenddelayunit | ✓ |  |
| freeze | 冻结时长 | BasedataField | t_wtp_qtuseconfig.ffreezeid | ✓ | wtbd_attitem |
| sstartdelay | 时长 | IntegerField | — |  |  |
| senddelay | 时长 | IntegerField | — |  |  |
| useduration | 使用时长 | BasedataField | t_wtp_qtuseconfig.fusedurationid | ✓ | wtbd_attitem |
| sdelay | 使用开始日期可延期 | ComboField | t_wtp_qtuseconfig.fsdelay | ✓ |  |
| edelay | 使用结束日期可延期 | ComboField | t_wtp_qtuseconfig.fedelay | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtuseconfig（主表） | 39 |
| t_wtp_qtuseconfig_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
