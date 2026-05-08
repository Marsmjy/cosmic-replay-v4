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

# hsbs_insurproscheme — 社保项目映射方案

**表单编码**: `hsbs_insurproscheme`  
**表单ID**: `2LYY8C04446W`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_insurproscheme（社保项目映射方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_insurproscheme` | 主表 · 26 列 |
| `t_hsbs_insurproschemeent` | 分录表 · 5 列 |
| `t_hsbs_insurproscheme_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_insurproscheme.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_insurproscheme_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_insurproscheme.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_insurproscheme.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_insurproscheme.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_insurproscheme.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_insurproscheme.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_insurproscheme.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_insurproscheme.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_insurproscheme_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_insurproscheme_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_insurproscheme.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_insurproscheme.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_insurproscheme.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_insurproscheme.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsbs_insurproscheme.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsbs_insurproscheme.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsbs_insurproscheme.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsbs_insurproscheme.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsbs_insurproscheme.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsbs_insurproscheme.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsbs_insurproscheme.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsbs_insurproscheme.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsbs_insurproscheme.fhisversion |  |  |

## 实体: entityentity（社保项目映射） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryboid | 分录业务ID | BigIntField | t_hsbs_insurproschemeent.fentryboid |  |  |
| insuranceitem | 险种项目 | BigIntField | — | ✓ |  |
| groupname | 险种 | TextField | — |  |  |
| insuranceitemnumber | 险种项目编码 | TextField | — |  |  |
| insuranceitemname | 险种项目名称 | TextField | — |  |  |
| dealmethod | 处理方式 | ComboField | t_hsbs_insurproschemeent.fdealmethod | ✓ |  |
| salaryitem | 薪酬项目名称 | BasedataField | t_hsbs_insurproschemeent.fsalaryitemid | ✓ | hsbs_salaryitem |
| basedatapropfield | 薪酬项目编码 | BasedataPropField | — |  |  |
| withholdtype | 代扣方式 | ComboField | t_hsbs_insurproschemeent.fwithholdtype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsbs_insurproscheme.fcountryid | ✓ | bd_country |
| org | 算发薪管理组织 | OrgField | t_hsbs_insurproscheme.forgid | ✓ | bos_org |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_insurproscheme（主表） | 23 |
| t_hsbs_insurproscheme_l | 3 |
| t_hsbs_insurproschemeent | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
