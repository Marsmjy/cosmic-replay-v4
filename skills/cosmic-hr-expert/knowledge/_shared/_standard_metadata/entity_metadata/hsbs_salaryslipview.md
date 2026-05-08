---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHLXNRR9KPZ
app_number: hsbs
app_name: 薪酬基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsbs_salaryslipview — 工资条显示方案

**表单编码**: `hsbs_salaryslipview`  
**表单ID**: `0LI/E/6+/1JF`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_salaryslipview（工资条显示方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_salaryslipview` | 主表 · 50 列 |
| `t_hsbs_salslipviewent` | 分录表 · 8 列 |
| `t_hsbs_salslipviewdetail` | 分录表 · 7 列 |
| `t_hsbs_salaryslipview_l` | 多语言表 · 5 列 |
| `t_hsbs_salslipviewent_l` | 多语言表 · 2 列 |
| `t_hsbs_salslipviewdetail_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_salaryslipview.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_salaryslipview_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_salaryslipview.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_salaryslipview.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_salaryslipview.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_salaryslipview.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_salaryslipview.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_salaryslipview.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_salaryslipview.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_salaryslipview.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_salaryslipview.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_salaryslipview.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_salaryslipview.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_salaryslipview.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_salaryslipview.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsbs_salaryslipview.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_salaryslipview_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_salaryslipview_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_salaryslipview.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_salaryslipview.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_salaryslipview.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsbs_salaryslipview.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsbs_salaryslipview.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsbs_salaryslipview.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsbs_salaryslipview.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsbs_salaryslipview.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsbs_salaryslipview.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsbs_salaryslipview.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsbs_salaryslipview.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsbs_salaryslipview.fhisversion |  |  |
| issendbyauto | 自助端发布 | CheckBoxField | t_hsbs_salaryslipview.fissendbyauto |  |  |
| issendbymail | 邮件发布 | CheckBoxField | t_hsbs_salaryslipview.fissendbymail |  |  |

## 实体: entryentity（工资条项目分组） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| groupname | 薪酬项目系统分类 | MuliLangTextField | t_hsbs_salslipviewent_l.fgroupname |  |  |
| salaryitemtype | 薪酬项目系统分类 | BasedataField | t_hsbs_salslipviewent.fsalaryitemtypeid |  | hsbs_salaryitemtype |
| groupdisplayname | 显示名称 | MuliLangTextField | t_hsbs_salslipviewent_l.fgroupdisplayname | ✓ |  |
| isautoendsummary | 自助端显示汇总 | CheckBoxField | t_hsbs_salslipviewent.fisautoendsummary |  |  |
| itemcount | 项目数 | IntegerField | t_hsbs_salslipviewent.fitemcount |  |  |
| isautoendtopshow | 自助端顶部显示 | CheckBoxField | t_hsbs_salslipviewent.fisautoendtopshow |  |  |
| entryboid | 分录业务ID | BigIntField | t_hsbs_salslipviewent.fentryboid |  |  |
| salaryitem | 薪酬项目(废弃) | BasedataField | t_hsbs_salslipviewdetail.fsalaryitemid |  | hsbs_salaryitem |
| itemdisplayname | 显示名称 | MuliLangTextField | t_hsbs_salslipviewdetail_l.fitemdisplayname | ✓ |  |
| iszerodisplay | 数据为0时显示 | CheckBoxField | t_hsbs_salslipviewdetail.fiszerodisplay |  |  |
| isemptydisplay | 数据为空时显示 | CheckBoxField | t_hsbs_salslipviewdetail.fisemptydisplay |  |  |
| datatype | 数据类型 | BasedataPropField | — |  |  |
| entryboiddetail | 子分录业务ID | BigIntField | t_hsbs_salslipviewdetail.fentryboiddetail |  |  |
| item | 项目编码 | ItemClassField | — |  |  |
| itemcategory | 项目类别 | ItemClassTypeField | t_hsbs_salslipviewdetail.fitemcategory |  |  |
| itemdatatype | 数据类型 | BasedataField | — |  | hsbs_datatype |
| mailsender | 发送人邮箱 | BasedataField | t_hsbs_salaryslipview.fmailsenderid |  | hsbs_mailsender |
| emailtheme | 邮件主题 | TextField | t_hsbs_salaryslipview.femailtheme |  |  |
| emailcontent | 邮件正文 | LargeTextField | t_hsbs_salaryslipview.femailcontent |  |  |
| country | 国家/地区 | BasedataField | t_hsbs_salaryslipview.fcountryid | ✓ | bd_country |
| isneedconfirm | 工资条确认 | CheckBoxField | t_hsbs_salaryslipview.fisneedconfirm |  |  |
| currencytype | 明细币种展示 | ComboField | t_hsbs_salaryslipview.fcurrencytype | ✓ |  |
| isshowpayflow | 显示发放明细 | CheckBoxField | t_hsbs_salaryslipview.fisshowpayflow |  |  |
| autoendnote | 备注 | MuliLangTextField | t_hsbs_salaryslipview_l.fautoendnote |  |  |
| defaulttitle | 工资条默认标题 | MuliLangTextField | t_hsbs_salaryslipview_l.fdefaulttitle |  |  |
| verifymode | 验证方式 | ComboField | t_hsbs_salaryslipview.fverifymode |  |  |
| queryscope | 查询范围 | ComboField | t_hsbs_salaryslipview.fqueryscope | ✓ |  |
| validduration | 查询时长 | IntegerField | t_hsbs_salaryslipview.fvalidduration |  |  |
| isunlimited | 不限 | CheckBoxField | t_hsbs_salaryslipview.fisunlimited |  |  |
| queryduration | 查询时长 | IntegerField | — |  |  |
| msgtemplate | 消息模板 | MulBasedataField | — |  |  |
| ispushnotify | 消息提醒 | CheckBoxField | t_hsbs_salaryslipview.fispushnotify |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_salaryslipview（主表） | 41 |
| t_hsbs_salaryslipview_l | 5 |
| t_hsbs_salslipviewdetail | 5 |
| t_hsbs_salslipviewdetail_l | 1 |
| t_hsbs_salslipviewent | 5 |
| t_hsbs_salslipviewent_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
