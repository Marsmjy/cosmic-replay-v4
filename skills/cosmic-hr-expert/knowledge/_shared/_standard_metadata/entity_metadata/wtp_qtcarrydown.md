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

# wtp_qtcarrydown — 定额结转配置

**表单编码**: `wtp_qtcarrydown`  
**表单ID**: `33W2IXW89LEA`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtcarrydown（定额结转配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtcarrydown` | 主表 · 71 列 |
| `t_wtp_qtcarrydown_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_qtcarrydown.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_qtcarrydown_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_qtcarrydown.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_qtcarrydown.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_qtcarrydown.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_qtcarrydown.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_qtcarrydown.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtcarrydown.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_qtcarrydown.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_qtcarrydown.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_qtcarrydown.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_qtcarrydown.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_qtcarrydown.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_qtcarrydown.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_qtcarrydown.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_qtcarrydown.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_qtcarrydown_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_qtcarrydown_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_qtcarrydown.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_qtcarrydown.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_qtcarrydown.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_qtcarrydown.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_qtcarrydown.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_qtcarrydown.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_qtcarrydown.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_qtcarrydown.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_qtcarrydown.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_qtcarrydown.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_qtcarrydown.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_qtcarrydown.fhisversion |  |  |
| cdmode | 结转方式 | ComboField | t_wtp_qtcarrydown.fcdmode | ✓ |  |
| pastduration | 过期时长 | BasedataField | t_wtp_qtcarrydown.fpastdurationid | ✓ | wtbd_attitem |
| cdstartdate | 开始日期等于 | ComboField | t_wtp_qtcarrydown.fcdstartdate | ✓ |  |
| cdsdelayvalue | 浮动时长-db隐藏 | IntegerField | t_wtp_qtcarrydown.fcdsdelayvalue |  |  |
| cdsdelayunit | 单位 | ComboField | t_wtp_qtcarrydown.fcdsdelayunit | ✓ |  |
| cdenddate | 结束日期等于 | ComboField | t_wtp_qtcarrydown.fcdenddate | ✓ |  |
| cdedelayvalue | 浮动时长-db隐藏 | IntegerField | t_wtp_qtcarrydown.fcdedelayvalue |  |  |
| cdedelayunit | 单位 | ComboField | t_wtp_qtcarrydown.fcdedelayunit | ✓ |  |
| cdduration | 结转时长 | BasedataField | t_wtp_qtcarrydown.fcddurationid | ✓ | wtbd_attitem |
| cdpastpercent | 过期比例(%)-隐藏 | DecimalField | t_wtp_qtcarrydown.fcdpastpercent |  |  |
| cdvalue | 数值-db隐藏 | DecimalField | t_wtp_qtcarrydown.fcdvalue |  |  |
| cdpercent | 比例(%) | DecimalField | t_wtp_qtcarrydown.fcdpercent | ✓ |  |
| cdpastduration | 过期时长 | BasedataField | t_wtp_qtcarrydown.fcdpastdurationid | ✓ | wtbd_attitem |
| scdsdelayvalue | 时长 | IntegerField | — |  |  |
| scdedelayvalue | 时长 | IntegerField | — |  |  |
| scdvalue | 值 | DecimalField | — |  |  |
| cdsdelay | 开始日期可顺延 | ComboField | t_wtp_qtcarrydown.fcdsdelay | ✓ |  |
| cdedelay | 结束日期可顺延 | ComboField | t_wtp_qtcarrydown.fcdedelay | ✓ |  |
| dpvestday | 归属日期 | ComboField | t_wtp_qtcarrydown.fdpvestday | ✓ |  |
| dpvdelay | 归属日期可顺延 | ComboField | t_wtp_qtcarrydown.fdpvdelay |  |  |
| sdpvdelayvalue | 时长 | IntegerField | — |  |  |
| dpvdelayvalue | 浮动时长-db隐藏 | IntegerField | t_wtp_qtcarrydown.fdpvdelayvalue |  |  |
| dpvdelayunit | 单位 | ComboField | t_wtp_qtcarrydown.fdpvdelayunit | ✓ |  |
| departduration | 离职时长 | BasedataField | t_wtp_qtcarrydown.fdepartduration | ✓ | wtbd_attitem |
| cdvestday | 归属日期 | ComboField | t_wtp_qtcarrydown.fcdvestday | ✓ |  |
| cdvdelay | 归属日期可顺延 | ComboField | t_wtp_qtcarrydown.fcdvdelay | ✓ |  |
| scdvdelayvalue | 时长 | IntegerField | — |  |  |
| cdvdelayvalue | 浮动时长-db隐藏 | IntegerField | t_wtp_qtcarrydown.fcdvdelayvalue |  |  |
| cdvdelayunit | 单位 | ComboField | t_wtp_qtcarrydown.fcdvdelayunit | ✓ |  |
| cdsalaryduration | 结转至薪资时长 | BasedataField | t_wtp_qtcarrydown.fcdsalaryduration | ✓ | wtbd_attitem |
| offsetmode | 冲抵方式 | ComboField | t_wtp_qtcarrydown.foffsetmode | ✓ |  |
| offsetseq | 冲抵顺序 | ComboField | t_wtp_qtcarrydown.foffsetseq | ✓ |  |
| rqmode | 剩余额度处理方式 | ComboField | t_wtp_qtcarrydown.frqmode | ✓ |  |
| iscross | 跨考勤管理组织结算 | ComboField | t_wtp_qtcarrydown.fiscross | ✓ |  |
| ccdtype | 部分结算数值 | ComboField | t_wtp_qtcarrydown.fccdtype | ✓ |  |
| ccdmode | 结算方式 | ComboField | t_wtp_qtcarrydown.fccdmode | ✓ |  |
| crqmode | 剩余额度处理方式 | ComboField | t_wtp_qtcarrydown.fcrqmode | ✓ |  |
| sccdvalue | 值 | DecimalField | — |  |  |
| sccdpercent | 比例(%) | DecimalField | — |  |  |
| ccdvestday | 归属日期 | ComboField | t_wtp_qtcarrydown.fccdvestday | ✓ |  |
| ccdvalue | 数值-db隐藏 | DecimalField | t_wtp_qtcarrydown.fccdvalue |  |  |
| slmentattitem | 跨考勤管理组织结算时长 | BasedataField | t_wtp_qtcarrydown.fslmentattitem | ✓ | wtbd_attitem |
| dpmode | 离职时长处理方式 | ComboField | t_wtp_qtcarrydown.fdpmode | ✓ |  |
| dpexpiremode | 离职时长到期处理 | ComboField | t_wtp_qtcarrydown.fdpexpiremode | ✓ |  |
| dpexpireduration | 离职失效时长 | BasedataField | t_wtp_qtcarrydown.fdpexpireduration | ✓ | wtbd_attitem |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtcarrydown（主表） | 65 |
| t_wtp_qtcarrydown_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 13 |
