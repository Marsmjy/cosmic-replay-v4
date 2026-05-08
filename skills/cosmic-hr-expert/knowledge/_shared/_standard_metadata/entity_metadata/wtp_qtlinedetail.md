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

# wtp_qtlinedetail — 定额明细

**表单编码**: `wtp_qtlinedetail`  
**表单ID**: `379/NUFWLW5F`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtlinedetail（定额明细） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtlinedetail` | 主表 · 50 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtp_qtlinedetail.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtp_qtlinedetail.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtp_qtlinedetail.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtlinedetail.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| qttype | 定额类型 | BasedataField | t_wtp_qtlinedetail.fqttypeid | ✓ | wtp_qttype |
| attfilebo | 考勤档案 | BasedataField | t_wtp_qtlinedetail.fattfileboid | ✓ | wtp_attfilebase |
| periodcircleid | 期间循环配置 | BasedataField | t_wtp_qtlinedetail.fperiodcircleid |  | wtbd_cycset |
| periodnum | 期数 | IntegerField | t_wtp_qtlinedetail.fperiodnum |  |  |
| source | 来源 | ComboField | t_wtp_qtlinedetail.fsource | ✓ |  |
| genvalue | 标准生成-固定 | DecimalField | t_wtp_qtlinedetail.fgenvalue |  |  |
| ownvalue | 享有-固定 | DecimalField | t_wtp_qtlinedetail.fownvalue |  |  |
| ownodvalue | 享有透支-固定 | DecimalField | t_wtp_qtlinedetail.fownodvalue |  |  |
| usablevalue | 可用时长 | DecimalField | t_wtp_qtlinedetail.fusablevalue |  |  |
| freezevalue | 冻结时长 | DecimalField | t_wtp_qtlinedetail.ffreezevalue |  |  |
| usedvalue | 已用时长 | DecimalField | t_wtp_qtlinedetail.fusedvalue |  |  |
| useodvalue | 已透支时长 | DecimalField | t_wtp_qtlinedetail.fuseodvalue |  |  |
| canbeodvalue | 可透支时长 | DecimalField | t_wtp_qtlinedetail.fcanbeodvalue |  |  |
| nousegenvalue | 超额生成不可用-固定 | DecimalField | t_wtp_qtlinedetail.fnousegenvalue |  |  |
| balance | 冲抵-固定 | DecimalField | t_wtp_qtlinedetail.fbalance |  |  |
| genstartdate | 开始日期 | DateField | t_wtp_qtlinedetail.fgenstartdate | ✓ |  |
| genenddate | 结束日期 | DateField | t_wtp_qtlinedetail.fgenenddate | ✓ |  |
| usestartdate | 使用开始日期 | DateField | t_wtp_qtlinedetail.fusestartdate | ✓ |  |
| useenddate | 使用结束日期 | DateField | t_wtp_qtlinedetail.fuseenddate | ✓ |  |
| attfileid | 考勤档案版本 | BasedataField | t_wtp_qtlinedetail.fattfileid |  | wtp_attfilebase |
| genvalueid | 标准生成项目 | BasedataField | t_wtp_qtlinedetail.fgenvalueid |  | wtte_quotadetail |
| ownvalueid | 享有项目 | BasedataField | t_wtp_qtlinedetail.fownvalueid |  | wtte_quotadetail |
| ownodvalueid | 透支享有项目 | BasedataField | t_wtp_qtlinedetail.fownodvalueid |  | wtte_quotadetail |
| nousegenvalueid | 超额生成不可用项目 | BasedataField | t_wtp_qtlinedetail.fnousegenvalueid |  | wtte_quotadetail |
| freezevalueid | 冻结项目 | BasedataField | t_wtp_qtlinedetail.ffreezevalueid |  | wtte_quotadetail |
| balanceid | 冲抵项目 | BasedataField | t_wtp_qtlinedetail.fbalanceid |  | wtte_quotadetail |
| usedvalueid | 已用项目 | BasedataField | t_wtp_qtlinedetail.fusedvalueid |  | wtte_quotadetail |
| useodvalueid | 已透支项目 | BasedataField | t_wtp_qtlinedetail.fuseodvalueid |  | wtte_quotadetail |
| canbeodvalueid | 可透支项目 | BasedataField | t_wtp_qtlinedetail.fcanbeodvalueid |  | wtte_quotadetail |
| usablevalueid | 可用项目 | BasedataField | t_wtp_qtlinedetail.fusablevalueid |  | wtte_quotadetail |
| org | 考勤管理组织 | OrgField | t_wtp_qtlinedetail.forgid | ✓ | bos_org |
| frozenodvalue | 冻结透支时长 | DecimalField | t_wtp_qtlinedetail.ffrozenodvalue |  |  |
| name | 名称 | TextField | — |  |  |
| qtdetailaddid | 定额明细新增 | BasedataField | t_wtp_qtlinedetail.fqtdetailaddid |  | wtte_quotadetailadd |
| busstatus | 状态 | ComboField | t_wtp_qtlinedetail.fbusstatus |  |  |
| detailsourceid | 明细来源 | BasedataField | t_wtp_qtlinedetail.fdetailsourceid |  | wtp_qtlinedetail |
| number | 编码 | TextField | t_wtp_qtlinedetail.fnumber |  |  |
| invalidvalue | 失效时长 | DecimalField | t_wtp_qtlinedetail.finvalidvalue |  |  |
| invalidvalueid | 失效项目 | BasedataField | t_wtp_qtlinedetail.finvalidvalueid |  | wtte_quotadetail |
| blsuseodvalue | 已抵已透支时长-固定 | DecimalField | t_wtp_qtlinedetail.fblsuseodvalue |  |  |
| isdpconvert | 以调整后的结果参与离职折算 | CheckBoxField | t_wtp_qtlinedetail.fisdpconvert |  |  |
| employeeid | 员工id | BasedataField | t_wtp_qtlinedetail.femployeeid |  | hrpi_employee |
| gencondition | 定额生成条件 | TextField | t_wtp_qtlinedetail.fgencondition |  |  |
| usedate | 使用日期 | DateField | — |  |  |
| fmcdinvalidvalue | 公式结转失效时长 | DecimalField | t_wtp_qtlinedetail.ffmcdinvalidvalue |  |  |
| fmcdinvalidvalueid | 公式结转失效时长项目 | BasedataField | t_wtp_qtlinedetail.ffmcdinvalidvalueid |  | wtte_quotadetail |
| carrydownrecord | 结转记录 | BasedataField | t_wtp_qtlinedetail.fcarrydownrecordid |  | wtte_qtcarrydownrecord |
| invalidodvalue | 失效透支时长 | DecimalField | t_wtp_qtlinedetail.finvalidodvalue |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtlinedetail（主表） | 50 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
