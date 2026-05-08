---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 17/+7RIW4SCJ
app_number: sitbs
app_name: 社保个税基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# sitbs_sinsurstd — 参保标准

**表单编码**: `sitbs_sinsurstd`  
**表单ID**: `2LL4=YGCX2UN`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_sinsurstd（参保标准） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_sinsurstd` | 主表 · 35 列 |
| `t_sitbs_sinsurstdent` | 分录表 · 5 列 |
| `t_sitbs_sinsurstd_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_sinsurstd.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_sinsurstd_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_sinsurstd.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_sinsurstd.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_sinsurstd.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_sinsurstd.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_sinsurstd.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_sinsurstd.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_sinsurstd.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_sitbs_sinsurstd.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_sitbs_sinsurstd.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_sitbs_sinsurstd.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_sitbs_sinsurstd.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_sitbs_sinsurstd.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_sitbs_sinsurstd.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_sitbs_sinsurstd.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_sinsurstd_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_sinsurstd_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_sinsurstd.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_sinsurstd.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_sinsurstd.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_sitbs_sinsurstd.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_sitbs_sinsurstd.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_sitbs_sinsurstd.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_sitbs_sinsurstd.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_sitbs_sinsurstd.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_sitbs_sinsurstd.fbsed |  |  |
| bsled | 失效日期 | DateField | t_sitbs_sinsurstd.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_sitbs_sinsurstd.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_sitbs_sinsurstd.fhisversion |  |  |
| generalenname | 通用英文名 | TextField | t_sitbs_sinsurstd.fgeneralenname |  |  |
| country | 国家/地区 | BasedataField | t_sitbs_sinsurstd.fcountryid | ✓ | bd_country |
| insurarea | 参保地 | BasedataField | t_sitbs_sinsurstd.finsurareaid | ✓ | sitbs_placeofwelfare |

## 实体: stdentryentity（参保标准分录表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| payrollscenename | 编码 | BasedataPropField | — |  |  |
| insurtype | 险种 | BasedataField | t_sitbs_sinsurstdent.finsurtypeid |  | sitbs_welfaretype |
| entryboid | 分录业务id | BigIntField | t_sitbs_sinsurstdent.fentryboid |  |  |
| isstddim | 是否启用标准维度 | CheckBoxField | — |  |  |
| mulstddim | 标准维度 | MulBasedataField | — |  |  |
| mulstddimrs | 参保标准维度结果 | MulBasedataField | — |  |  |
| isstddimdy | 启用标准维度 | CheckBoxField | — |  |  |
| mulstddimdy | 标准维度 | MulBasedataField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_sinsurstd（主表） | 30 |
| t_sitbs_sinsurstd_l | 3 |
| t_sitbs_sinsurstdent | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 12 |
