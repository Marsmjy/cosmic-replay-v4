---
source: openapi_runtime
extracted_at: 2026-04-28
extractor: build_standard_metadata_md_from_openapi.py
app_id: 217WYC/L9U7E
app_number: homs
app_name: 组织管理
cloud_number: ODC
cloud_name: 组织发展云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# homs_orgdifftemp — ERP组织同步工具

**表单编码**: `homs_orgdifftemp`  
**表单ID**: `2HK5M7UI51++`  
**归属**: 组织发展云 / 组织管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: homs_orgdifftemp（ERP组织同步工具） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_homs_orgdifftemp` | 主表 · 37 列 |
| `t_homs_orgdifftemp_l` | 多语言表 · 7 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 行政组织编码 | TextField | t_homs_orgdifftemp.fnumber | ✓ |  |
| name | 行政组织名称 | MuliLangTextField | t_homs_orgdifftemp_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_homs_orgdifftemp.fstatus |  |  |
| creator | 创建人 | CreaterField | t_homs_orgdifftemp.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_homs_orgdifftemp.fmodifierid |  | bos_user |
| enable | 启用HR | BillStatusField | t_homs_orgdifftemp.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_homs_orgdifftemp.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_homs_orgdifftemp.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_homs_orgdifftemp.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_homs_orgdifftemp_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_homs_orgdifftemp_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_homs_orgdifftemp.findex | ✓ |  |
| issyspreset | 系统预置 | CheckBoxField | t_homs_orgdifftemp.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_homs_orgdifftemp.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_homs_orgdifftemp.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| industrytype | 行业类别 | BasedataField | t_homs_orgdifftemp.findustrytypeid |  | hbss_industrytype |
| positioning | 定位 | MuliLangTextField | t_homs_orgdifftemp_l.fpositioning |  |  |
| mainduty | 主要职责 | MuliLangTextField | t_homs_orgdifftemp_l.fmainduty |  |  |
| parentorg | 上级行政组织 | BasedataField | — | ✓ | homs_parentorgdifftemp |
| adminorgtype | 行政组织类型 | BasedataField | t_homs_orgdifftemp.fadminorgtypeid | ✓ | haos_adminorgtype |
| establishmentdate | 成立日期 | DateField | t_homs_orgdifftemp.festablishmentdate |  |  |
| corporateorg | 法律实体 | BasedataField | t_homs_orgdifftemp.fcorporateorgid |  | hbss_lawentity |
| tobedisableflag | 待停用 | CheckBoxField | t_homs_orgdifftemp.ftobedisableflag |  |  |
| adminorglayer | 管理层级 | BasedataField | t_homs_orgdifftemp.fadminorglayerid |  | haos_adminorglayer |
| adminorgfunction | 行政组织职能 | BasedataField | t_homs_orgdifftemp.fadminorgfunctionid |  | haos_adminorgfunction |
| companyarea | 国家地区 | BasedataField | t_homs_orgdifftemp.fcompanyareaid |  | bd_country |
| city | 所在城市 | BasedataField | t_homs_orgdifftemp.fcityid |  | bd_admindivision |
| workplace | 工作地 | BasedataField | t_homs_orgdifftemp.fworkplaceid |  | hbss_workplace |
| detailaddress | 详细地址 | MuliLangTextField | — |  |  |
| tobedisabledate | 待停用时间 | DateField | t_homs_orgdifftemp.ftobedisabledate |  |  |
| changedescription | 启用失败原因 | MuliLangTextField | t_homs_orgdifftemp_l.fchangedescription |  |  |
| boid | boid | BigIntField | — |  |  |
| structnumber | 结构编码 | TextField | t_homs_orgdifftemp.fstructnumber |  |  |
| bsed | 生效日期 | DateField | t_homs_orgdifftemp.fbsed | ✓ |  |
| bsled | 失效时间 | DateField | — |  |  |
| ischange | 是否发生变更 | ComboField | t_homs_orgdifftemp.fischange |  |  |
| integrationdate | 集成时间 | DateTimeField | t_homs_orgdifftemp.fintegrationdate |  |  |
| isfreeze | 封存 | CheckBoxField | t_homs_orgdifftemp.fisfreeze |  |  |
| org | 组织体系管理组织 | BasedataField | t_homs_orgdifftemp.forgid | ✓ | bos_org |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_homs_orgdifftemp（主表） | 29 |
| t_homs_orgdifftemp_l | 6 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
