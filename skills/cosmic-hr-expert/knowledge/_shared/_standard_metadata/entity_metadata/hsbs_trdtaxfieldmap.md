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

# hsbs_trdtaxfieldmap — 第三方个税字段映射方案

**表单编码**: `hsbs_trdtaxfieldmap`  
**表单ID**: `5EFA+0C4MWXH`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_trdtaxfieldmap（第三方个税字段映射方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_trdtaxfieldmap` | 主表 · 25 列 |
| `t_hsbs_trdtaxfieldmapent` | 分录表 · 4 列 |
| `t_hsbs_trdtaxfieldmap_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_trdtaxfieldmap.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_trdtaxfieldmap_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_trdtaxfieldmap.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_trdtaxfieldmap.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_trdtaxfieldmap.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_trdtaxfieldmap.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_trdtaxfieldmap.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_trdtaxfieldmap.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_trdtaxfieldmap.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_trdtaxfieldmap_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_trdtaxfieldmap_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_trdtaxfieldmap.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_trdtaxfieldmap.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_trdtaxfieldmap.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_trdtaxfieldmap.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsbs_trdtaxfieldmap.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsbs_trdtaxfieldmap.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsbs_trdtaxfieldmap.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsbs_trdtaxfieldmap.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsbs_trdtaxfieldmap.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsbs_trdtaxfieldmap.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsbs_trdtaxfieldmap.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsbs_trdtaxfieldmap.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsbs_trdtaxfieldmap.fhisversion |  |  |
| trdlogo | 第三方标识 | BasedataField | t_hsbs_trdtaxfieldmap.ftrdlogoid | ✓ | hsbs_trdlogo |

## 实体: taxmapentry（字段映射） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| salaryitem | 薪酬项目 | BasedataField | t_hsbs_trdtaxfieldmapent.fsalaryitemid | ✓ | hsbs_salaryitem |
| datatypename | 数据类型 | BasedataPropField | — |  |  |
| entryboid | 分录BOID | BigIntField | t_hsbs_trdtaxfieldmapent.fentryboid |  |  |
| trdtaxfield | 个税字段名称 | BasedataField | t_hsbs_trdtaxfieldmapent.ftrdtaxfieldid |  | hsbs_trdtaxfield |
| taxcategory | 个税种类 | BasedataField | t_hsbs_trdtaxfieldmapent.ftaxcategoryid |  | hsbs_taxcategory |
| trdtaxfieldnumber | 个税字段编码 | BasedataPropField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_trdtaxfieldmap（主表） | 22 |
| t_hsbs_trdtaxfieldmap_l | 3 |
| t_hsbs_trdtaxfieldmapent | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
