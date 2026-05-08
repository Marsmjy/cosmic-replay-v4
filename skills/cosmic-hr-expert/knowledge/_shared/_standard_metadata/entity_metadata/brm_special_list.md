---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1IMTC4ANI0KA
app_number: brm
app_name: 业务规则管理
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# brm_special_list — 名单管理

**表单编码**: `brm_special_list`  
**表单ID**: `1S5KNZXRZGVG`  
**归属**: HR基础服务云 / 业务规则管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: brm_special_list（名单管理） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_brm_special_list` | 主表 · 19 列 |
| `t_brm_list_string` | 分录表 · 2 列 |
| `t_brm_list_person` | 分录表 · 1 列 |
| `t_brm_list_org` | 分录表 · 1 列 |
| `t_brm_list_emp` | 分录表 · 1 列 |
| `t_brm_special_list_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_brm_special_list.fnumber | ✓ |  |
| name | 名单名称 | MuliLangTextField | t_brm_special_list_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_brm_special_list.fstatus |  |  |
| creator | 创建人 | CreaterField | t_brm_special_list.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_brm_special_list.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_brm_special_list.fenable | ✓ |  |
| createtime | 创建时间 | CreateDateField | t_brm_special_list.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_brm_special_list.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_brm_special_list.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_brm_special_list_l.fsimplename |  |  |
| description | 名单描述 | MuliLangTextField | t_brm_special_list_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_brm_special_list.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_brm_special_list.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_brm_special_list.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_brm_special_list.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| bu | 创建组织 | OrgField | t_brm_special_list.fbuid | ✓ | bos_org |
| listcategory | 名单类别 | ComboField | t_brm_special_list.flistcategory |  |  |
| listtype | 名单类型 | ComboField | t_brm_special_list.flisttype | ✓ |  |
| listenble | 使用状态 | ComboField | — |  |  |
| applicationscope | 适用范围 | ComboField | t_brm_special_list.fapplicationscope |  |  |

## 实体: list_string_param（字符串） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| liststringname | 名单 | TextField | t_brm_list_string.fliststringname | ✓ |  |
| stringdescription | 描述 | TextField | t_brm_list_string.fstringdescription |  |  |

## 实体: brm_list_person（系统人员） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entityperson | 姓名 | BasedataField | t_brm_list_person.fentitypersonid | ✓ | bos_user |
| entitypersonpicture | 头像 | BasedataPropField | — |  |  |
| entitypersonphone | 手机号码 | BasedataPropField | — |  |  |
| entitypersonsex | 性别 | BasedataPropField | — |  |  |
| persondepartment | 所属部门 | TextField | — |  |  |
| personnumber | 员工工号 | TextField | — |  |  |

## 实体: brm_list_org（行政组织） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entityorg | 行政组织名称 | HRAdminOrgField | t_brm_list_org.fentityorgid | ✓ | haos_adminorghrf7 |
| entityorgnumber | 行政组织编码 | BasedataPropField | — |  |  |
| entityorgcomment | 描述 | BasedataPropField | — |  |  |

## 实体: brm_list_emp（计薪人员） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entitypersonpicture1 | 头像 | BasedataPropField | — |  |  |
| entityemp | 姓名 | BasedataField | t_brm_list_emp.fentityempid | ✓ | hsbs_employee |
| empnumber | 员工工号 | TextField | — |  |  |
| empdepartment | 所属部门 | TextField | — |  |  |
| entitypersonphone1 | 手机号码 | BasedataPropField | — |  |  |
| entitypersonsex1 | 性别 | BasedataPropField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_brm_special_list（主表） | 16 |
| t_brm_list_emp | 1 |
| t_brm_list_org | 1 |
| t_brm_list_person | 1 |
| t_brm_list_string | 2 |
| t_brm_special_list_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
