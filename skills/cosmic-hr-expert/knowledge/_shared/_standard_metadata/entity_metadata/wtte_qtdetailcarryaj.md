---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1C8H4/N38LCY
app_number: wtte
app_name: 考勤核算
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtte_qtdetailcarryaj — 定额明细结转

**表单编码**: `wtte_qtdetailcarryaj`  
**表单ID**: `3H3IHSA38ATW`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_qtdetailcarryaj（定额明细结转） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_qtdetailcarryaj` | 主表 · 41 列 |
| `t_wtte_qtdetailcdrecd` | 分录表 · 32 列 |
| `t_wtte_qtdetailcarryaj_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtte_qtdetailcarryaj.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtte_qtdetailcarryaj_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtte_qtdetailcarryaj.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtte_qtdetailcarryaj.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtte_qtdetailcarryaj.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtte_qtdetailcarryaj.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtte_qtdetailcarryaj.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtte_qtdetailcarryaj.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtte_qtdetailcarryaj.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtte_qtdetailcarryaj_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtte_qtdetailcarryaj_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtte_qtdetailcarryaj.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtte_qtdetailcarryaj.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtte_qtdetailcarryaj.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtte_qtdetailcarryaj.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| org | 考勤管理组织 | OrgField | t_wtte_qtdetailcarryaj.forgid | ✓ | bos_org |
| cdstartdate | 开始日期等于 | ComboField | t_wtte_qtdetailcarryaj.fcdstartdate | ✓ |  |
| cdsdelay | 开始日期可顺延 | ComboField | t_wtte_qtdetailcarryaj.fcdsdelay | ✓ |  |
| scdsdelayvalue | 时长 | IntegerField | — |  |  |
| cdsdelayvalue | 浮动时长-db隐藏 | IntegerField | t_wtte_qtdetailcarryaj.fcdsdelayvalue |  |  |
| cdsdelayunit | 单位 | ComboField | t_wtte_qtdetailcarryaj.fcdsdelayunit | ✓ |  |
| cdenddate | 结束日期等于 | ComboField | t_wtte_qtdetailcarryaj.fcdenddate | ✓ |  |
| cdedelay | 结束日期可顺延 | ComboField | t_wtte_qtdetailcarryaj.fcdedelay | ✓ |  |
| scdedelayvalue | 时长 | IntegerField | — |  |  |
| cdedelayvalue | 浮动时长-db隐藏 | IntegerField | t_wtte_qtdetailcarryaj.fcdedelayvalue |  |  |
| cdedelayunit | 单位 | ComboField | t_wtte_qtdetailcarryaj.fcdedelayunit | ✓ |  |
| pastduration | 过期时长 | BasedataField | t_wtte_qtdetailcarryaj.fpastdurationid | ✓ | wtbd_attitem |
| cdduration | 结转时长 | BasedataField | t_wtte_qtdetailcarryaj.fcddurationid | ✓ | wtbd_attitem |
| cdpastduration | 过期时长 | BasedataField | t_wtte_qtdetailcarryaj.fcdpastdurationid | ✓ | wtbd_attitem |
| cdmode | 结转方式 | ComboField | t_wtte_qtdetailcarryaj.fcdmode | ✓ |  |

## 实体: qtdetailcdrecd（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| qtdetailid | 定额明细 | BasedataField | t_wtte_qtdetailcdrecd.fqtdetailid | ✓ | wtp_qtlinedetail |
| attfilevid | 档案 | BasedataField | t_wtte_qtdetailcdrecd.fattfilevid |  | wtp_attfilebase |
| detailjoib | 职位 | BasedataPropField | — |  |  |
| detailcmp | 所属公司 | BasedataPropField | — |  |  |
| detailposition | 岗位 | BasedataPropField | — |  |  |
| qttype | 定额名称 | BasedataField | t_wtte_qtdetailcdrecd.fqttypeid |  | wtp_qttype |
| freezevalue | 冻结时长 | DecimalField | t_wtte_qtdetailcdrecd.ffreezevalue |  |  |
| ownvalue | 享有时长 | DecimalField | t_wtte_qtdetailcdrecd.fownvalue |  |  |
| pastvalue | 失效时长 | DecimalField | t_wtte_qtdetailcdrecd.fpastvalue |  |  |
| usedvalue | 已用时长 | DecimalField | t_wtte_qtdetailcdrecd.fusedvalue |  |  |
| useodvalue | 透支时长 | DecimalField | t_wtte_qtdetailcdrecd.fuseodvalue |  |  |
| cdedvalue | 结转时长 | DecimalField | t_wtte_qtdetailcdrecd.fcdedvalue |  |  |
| genstartdate | 生成开始日期 | DateField | t_wtte_qtdetailcdrecd.fgenstartdate |  |  |
| genenddate | 生成结束日期 | DateField | t_wtte_qtdetailcdrecd.fgenenddate |  |  |
| usestartdate | 使用开始日期 | DateField | t_wtte_qtdetailcdrecd.fusestartdate |  |  |
| crossday | 跨阶时间 | DateField | t_wtte_qtdetailcdrecd.fcrossday |  |  |
| useenddate | 使用结束日期 | DateField | t_wtte_qtdetailcdrecd.fuseenddate |  |  |
| periodcircleid | 期间循环配置 | BasedataField | t_wtte_qtdetailcdrecd.fperiodcircleid |  | wtbd_cycset |
| periodnum | 期数 | IntegerField | t_wtte_qtdetailcdrecd.fperiodnum |  |  |
| source | 来源 | ComboField | t_wtte_qtdetailcdrecd.fsource |  |  |
| attfilepersonnum | 工号 | BasedataPropField | — |  |  |
| attfileadminorg | 挂靠行政组织 | BasedataPropField | — |  |  |
| adminorg | 行政组织 | BasedataPropField | — |  |  |
| empgroup | 考勤档案分组 | BasedataPropField | — |  |  |
| dependency | 国家/地区 | BasedataPropField | — |  |  |
| workplace | 考勤地点 | BasedataPropField | — |  |  |
| agreedlocation | 协议工作地 | BasedataPropField | — |  |  |
| qttypeunit | 单位 | BasedataPropField | — |  |  |
| usablevalue | 可用时长 | DecimalField | t_wtte_qtdetailcdrecd.fusablevalue |  |  |
| canbeodvalue | 可透支时长 | DecimalField | t_wtte_qtdetailcdrecd.fcanbeodvalue |  |  |
| attfilenum | 档案编号 | BasedataPropField | — |  |  |
| detailorg | 考勤管理组织 | BasedataPropField | — |  |  |
| attfilename | 姓名 | BasedataPropField | — |  |  |
| scdvalue | 值 | DecimalField | — |  |  |
| cdvalue | 数值-db隐藏 | DecimalField | t_wtte_qtdetailcarryaj.fcdvalue |  |  |
| cdpercent | 比例(%) | DecimalField | t_wtte_qtdetailcarryaj.fcdpercent |  |  |
| cdpastpercent | 过期比例(%)-隐藏 | DecimalField | t_wtte_qtdetailcarryaj.fcdpastpercent |  |  |
| cdvestday | 归属日期 | ComboField | t_wtte_qtdetailcarryaj.fcdvestday | ✓ |  |
| cdvdelay | 归属日期可顺延 | ComboField | t_wtte_qtdetailcarryaj.fcdvdelay | ✓ |  |
| scdvdelayvalue | 时长 | IntegerField | — |  |  |
| cdvdelayvalue | 浮动时长-db隐藏 | IntegerField | t_wtte_qtdetailcarryaj.fcdvdelayvalue |  |  |
| cdvdelayunit | 单位 | ComboField | t_wtte_qtdetailcarryaj.fcdvdelayunit | ✓ |  |
| bsed | 生效日期 | DateField | — |  |  |
| attfileid | 选择人员 | BasedataField | t_wtte_qtdetailcarryaj.fattfileid | ✓ | wtp_attfilebase |
| qttypeid | 定额类型 | BasedataField | t_wtte_qtdetailcdrecd.fqttypeid | ✓ | wtp_qttype |
| queryyear | 选择范围 | DateField | t_wtte_qtdetailcarryaj.fqueryyear | ✓ |  |
| querymonth | 选择范围 | DateField | t_wtte_qtdetailcarryaj.fquerymonth | ✓ |  |
| qtdetailidview | 调整明细记录 | BasedataField | — |  | wtp_qtlinedetail |
| qtdetailscope | 时间范围 | ComboField | t_wtte_qtdetailcarryaj.fqtdetailscope | ✓ |  |
| rqmode | 剩余额度处理方式 | ComboField | t_wtte_qtdetailcarryaj.frqmode | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_qtdetailcarryaj（主表） | 37 |
| t_wtte_qtdetailcarryaj_l | 3 |
| t_wtte_qtdetailcdrecd | 20 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 24 |
