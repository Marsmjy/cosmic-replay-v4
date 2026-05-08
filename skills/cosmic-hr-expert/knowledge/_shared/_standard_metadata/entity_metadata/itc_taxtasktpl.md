---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 17/+CT1QBPNP
app_number: itc
app_name: 中国个税
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# itc_taxtasktpl — 个税任务模板

**表单编码**: `itc_taxtasktpl`  
**表单ID**: `4B5Z6SN0VQJQ`  
**归属**: 薪酬福利云 / 中国个税  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: itc_taxtasktpl（个税任务模板） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_itc_taxtasktpl` | 主表 · 39 列 |
| `t_itc_taxtasktplcategory` | 分录表 · 3 列 |
| `t_itc_taxtasktpltaxitem` | 分录表 · 3 列 |
| `t_itc_taxtasktplunitent` | 分录表 · 2 列 |
| `t_itc_taxtasktpl_l` | 多语言表 · 6 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_itc_taxtasktpl.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_itc_taxtasktpl_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_itc_taxtasktpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_itc_taxtasktpl.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_itc_taxtasktpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_itc_taxtasktpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_itc_taxtasktpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_itc_taxtasktpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_itc_taxtasktpl.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_itc_taxtasktpl_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_itc_taxtasktpl_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_itc_taxtasktpl.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_itc_taxtasktpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_itc_taxtasktpl.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_itc_taxtasktpl.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_itc_taxtasktpl.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_itc_taxtasktpl.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_itc_taxtasktpl.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_itc_taxtasktpl.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_itc_taxtasktpl.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_itc_taxtasktpl.fbsed |  |  |
| bsled | 失效日期 | DateField | t_itc_taxtasktpl.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_itc_taxtasktpl.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_itc_taxtasktpl.fhisversion |  |  |
| taxtasktype | 个税任务类别 | ComboField | t_itc_taxtasktpl.ftaxtasktype | ✓ |  |
| org | 个税管理组织 | OrgField | t_itc_taxtasktpl.forgid | ✓ | bos_org |
| chargeperson | 模板负责人 | UserField | t_itc_taxtasktpl.fchargepersonid | ✓ | bos_user |
| isneeddeclare | 人员报送状态检测 | CheckBoxField | t_itc_taxtasktpl.fisneeddeclare |  |  |
| isexistforeigncur | 个税源数据有外币 | CheckBoxField | t_itc_taxtasktpl.fisexistforeigncur |  |  |
| exratetable | 汇率表 | BasedataField | t_itc_taxtasktpl.fexratetableid |  | bd_exratetable |
| foreigncurrency | 来源外币币种 | MulBasedataField | — |  |  |
| nameprefix | 名称前缀 | MuliLangTextField | t_itc_taxtasktpl_l.fnameprefix | ✓ |  |
| namebody | 名称主体 | ComboField | t_itc_taxtasktpl.fnamebody | ✓ |  |
| namesuffix | 名称后缀 | MuliLangTextField | t_itc_taxtasktpl_l.fnamesuffix | ✓ |  |
| namesample | 名称生成样例 | TextField | — |  |  |

## 实体: taxtasktplcategory（个税种类分录表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| taxcategory | 个税种类 | BasedataField | t_itc_taxtasktplcategory.ftaxcategoryid |  | sitbs_taxcategory |
| basedatapropfield7 | 编码 | BasedataPropField | — |  |  |
| basedatapropfield4 | 名称 | BasedataPropField | — |  |  |
| calrule | 计算规则 | BasedataField | t_itc_taxtasktplcategory.fcalruleid |  | sitbs_calrule |
| entryboidcatg | 分录业务ID | BigIntField | t_itc_taxtasktplcategory.fentryboidcatg |  |  |
| taxitem | 个税项目 | BasedataField | t_itc_taxtasktpltaxitem.ftaxitemid |  | sitbs_taxitem |
| basedatapropfield6 | 编码 | BasedataPropField | — |  |  |
| basedatapropfield5 | 名称 | BasedataPropField | — |  |  |
| splitalgo | 返回核算数据拆分方法 | BasedataField | t_itc_taxtasktpltaxitem.fsplitalgoid |  | sitbs_splitalgo |
| entryboidtaxitem | 分录业务ID | BigIntField | t_itc_taxtasktpltaxitem.fentryboidtaxitem |  |  |
| tracker | 任务跟踪人 | MulBasedataField | — |  |  |

## 实体: taxtasktplunitent（纳税单位分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| taxunit | 纳税单位 | BasedataField | t_itc_taxtasktplunitent.ftaxunitid |  | hbss_taxunit |
| basedatapropfield3 | 编码 | BasedataPropField | — |  |  |
| basedatapropfield | 名称 | BasedataPropField | — |  |  |
| basedatapropfield1 | 纳税人识别号 | BasedataPropField | — |  |  |
| basedatapropfield2 | 纳税地 | BasedataPropField | — |  |  |
| entryboidtaxunit | 分录业务ID | BigIntField | t_itc_taxtasktplunitent.fentryboidtaxunit |  |  |
| schconfig | 任务创建调度计划 | BasedataField | t_itc_taxtasktpl.fschconfigid | ✓ | sitbs_schplancfg |
| schedudateop | 调度日期 | ComboField | t_itc_taxtasktpl.fschedudateop | ✓ |  |
| notifyway | 通知方式 | ComboField | t_itc_taxtasktpl.fnotifyway |  |  |
| successnotifyobj | 调度成功通知对象 | ComboField | t_itc_taxtasktpl.fsuccessnotifyobj |  |  |

## 实体: expsceneentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| expscene | 异常场景 | ComboField | — |  |  |
| notifyobj | 通知对象 | ComboField | — | ✓ |  |
| notifytpl | 通知模板 | TextField | — |  |  |
| msgtemplate | 消息模板 | BasedataField | — |  | msg_template |
| entryboid | 分录业务ID | BigIntField | — |  |  |
| namecustom | 自定义名称 | MuliLangTextField | t_itc_taxtasktpl_l.fnamecustom |  |  |
| changestatus | 变更状态 | ComboField | t_itc_taxtasktpl.fchangestatus |  |  |
| taxtasktplunit | 纳税单位 | MulBasedataField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_itc_taxtasktpl（主表） | 33 |
| t_itc_taxtasktpl_l | 6 |
| t_itc_taxtasktplcategory | 3 |
| t_itc_taxtasktpltaxitem | 3 |
| t_itc_taxtasktplunitent | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 21 |
