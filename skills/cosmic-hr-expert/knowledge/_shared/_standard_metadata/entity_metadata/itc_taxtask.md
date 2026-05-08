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

# itc_taxtask — 个税任务

**表单编码**: `itc_taxtask`  
**表单ID**: `1J9=RPZUWWZE`  
**归属**: 薪酬福利云 / 中国个税  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: itc_taxtask（个税任务） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_itc_taxtask` | 主表 · 34 列 |
| `t_itc_taxtaskunitentry` | 分录表 · 3 列 |
| `t_itc_taxtaskcatgentry` | 分录表 · 4 列 |
| `t_itc_taxtaskitemdetail` | 分录表 · 2 列 |
| `t_itc_taxtask_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_itc_taxtask.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_itc_taxtask_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_itc_taxtask.fstatus |  |  |
| creator | 创建人 | CreaterField | t_itc_taxtask.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_itc_taxtask.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_itc_taxtask.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_itc_taxtask.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_itc_taxtask.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_itc_taxtask.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_itc_taxtask_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_itc_taxtask_l.fdescription |  |  |
| index | 顺序号 | IntegerField | t_itc_taxtask.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_itc_taxtask.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_itc_taxtask.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_itc_taxtask.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |

## 实体: taxtaskunitentry（纳税单位分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| taxunit | 纳税单位 | BasedataField | t_itc_taxtaskunitentry.ftaxunitid |  | hbss_taxunit |
| basedatapropfield3 | 编码 | BasedataPropField | — |  |  |
| basedatapropfield | 名称 | BasedataPropField | — |  |  |
| basedatapropfield1 | 纳税人识别号 | BasedataPropField | — |  |  |
| basedatapropfield2 | 纳税地 | BasedataPropField | — |  |  |
| declarestatus | 标记申报状态 | ComboField | t_itc_taxtaskunitentry.fdeclarestatus |  |  |
| taxfilenum | 个税档案数量 | IntegerField | t_itc_taxtaskunitentry.ftaxfilenum |  |  |

## 实体: taxtaskcatgentry（个税种类分录表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| taxcategory | 个税种类 | BasedataField | t_itc_taxtaskcatgentry.ftaxcategoryid |  | sitbs_taxcategory |
| basedatapropfield4 | 名称 | BasedataPropField | — |  |  |
| taxdatanum | 个税数据数量 | IntegerField | t_itc_taxtaskcatgentry.ftaxdatanum |  |  |
| basedatapropfield7 | 编码 | BasedataPropField | — |  |  |
| calrule | 计算规则版本 | HisModelBasedataField | — |  | sitbs_calrule |
| calrulebo | 计算规则 | BasedataField | t_itc_taxtaskcatgentry.fcalruleboid |  | sitbs_calrule |
| taxitem | 个税项目 | BasedataField | t_itc_taxtaskitemdetail.ftaxitemid |  | sitbs_taxitem |
| basedatapropfield6 | 编码 | BasedataPropField | — |  |  |
| basedatapropfield5 | 名称 | BasedataPropField | — |  |  |
| splitalgo | 返回核算数据拆分方法 | BasedataField | t_itc_taxtaskitemdetail.fsplitalgoid |  | sitbs_splitalgo |
| createorg | 个税管理组织 | OrgField | t_itc_taxtask.fcreateorgid | ✓ | bos_org |
| adminorg1 | 行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| taxtasktype | 个税任务类别 | ComboField | t_itc_taxtask.ftaxtasktype | ✓ |  |
| taxperiod | 个税期间 | BasedataField | t_itc_taxtask.ftaxperiodid | ✓ | sitbs_taxperiod |
| taxstartandenddate | 个税起止日期 | DateRangeField | — | ✓ |  |
| declaredate | 申报起止日期 | DateRangeField | — | ✓ |  |
| generalenname | 通用英文名 | TextField | t_itc_taxtask.fgeneralenname |  |  |
| tracker | 跟踪人 | MulBasedataField | — |  |  |
| isexisttaxrecord | 是否有个税记录 | CheckBoxField | t_itc_taxtask.fisexisttaxrecord |  |  |
| taxpersoncount | 申报人数 | IntegerField | t_itc_taxtask.ftaxpersoncount |  |  |
| taskstatus | 任务状态 | ComboField | t_itc_taxtask.ftaskstatus |  |  |
| taxunitcol | 列表纳税单位 | MulBasedataField | — |  |  |
| taxunitsearch | 纳税单位 | BasedataField | — |  | hbss_taxunit |
| endstatus | 结束状态 | ComboField | t_itc_taxtask.fendstatus |  |  |
| exratedate | 汇率日期 | DateField | t_itc_taxtask.fexratedate |  |  |
| foreigncurrency | 来源外币币种 | MulBasedataField | — |  |  |
| isexistforeigncur | 个税源数据有外币 | CheckBoxField | t_itc_taxtask.fisexistforeigncur |  |  |
| exratetable | 汇率表 | BasedataField | t_itc_taxtask.fexratetableid |  | bd_exratetable |
| isneeddeclare | 人员报送状态检测 | CheckBoxField | t_itc_taxtask.fisneeddeclare |  |  |
| watchdog | 守护者实例id | BasedataField | t_itc_taxtask.fwatchdogid |  | sitbs_watchdog |
| lockuuid | 锁UUID | TextField | t_itc_taxtask.flockuuid |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_itc_taxtask（主表） | 26 |
| t_itc_taxtask_l | 3 |
| t_itc_taxtaskcatgentry | 3 |
| t_itc_taxtaskitemdetail | 2 |
| t_itc_taxtaskunitentry | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 20 |
