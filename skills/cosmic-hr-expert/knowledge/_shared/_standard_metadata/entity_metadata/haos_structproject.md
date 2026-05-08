---
source: openapi_runtime
extracted_at: 2026-04-27
extractor: build_standard_metadata_md_from_openapi.py
app_id: W11R1282DJK
app_number: haos
app_name: HR基础组织
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# haos_structproject — 矩阵组织设置

**表单编码**: `haos_structproject`  
**表单ID**: `3BPVOPG05AFA`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: haos_structproject（矩阵组织设置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_haos_structproject` | 主表 · 26 列 |
| `t_haos_structproject_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_haos_structproject.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_haos_structproject_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_haos_structproject.fstatus |  |  |
| creator | 创建人 | CreaterField | t_haos_structproject.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_haos_structproject.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_haos_structproject.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_haos_structproject.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_haos_structproject.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_haos_structproject.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_haos_structproject_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_haos_structproject_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_haos_structproject.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_haos_structproject.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_haos_structproject.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_haos_structproject.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| org | 创建组织 | OrgField | t_haos_structproject.forgid | ✓ | bos_org |
| rootorg | 根组织 | HRAdminOrgField | t_haos_structproject.frootorgid | ✓ | haos_adminorghrf7 |
| roottype | 根组织类型 | ComboField | t_haos_structproject.froottype | ✓ |  |
| isincludevirtualorg | 允许设置虚拟组织 | CheckBoxField | t_haos_structproject.fisincludevirtualorg |  |  |
| iscustomorg | 是否是自定义组织 | CheckBoxField | t_haos_structproject.fiscustomorg |  |  |
| relyonstructproject | 依赖架构方案 | BasedataField | t_haos_structproject.frelyonstructprojectid |  | haos_structproject |
| rootnumber | 组织编码 | TextField | — | ✓ |  |
| rootname | 组织名称 | MuliLangTextField | — | ✓ |  |
| rooteffdt | 成立日期 | DateField | — | ✓ |  |
| rootdescription | 描述 | MuliLangTextField | — |  |  |
| otclassify | 组织团队分类 | BasedataField | t_haos_structproject.fotclassifyid |  | haos_otclassify |
| effdt | 生效日期 | DateField | t_haos_structproject.feffdt | ✓ |  |
| orgorg | 组织体系管理组织 | OrgField | — |  | bos_org |
| istoallareas | 是否应用全领域 | CheckBoxField | t_haos_structproject.fistoallareas |  |  |
| isprimary | 是否主架构方案 | CheckBoxField | t_haos_structproject.fisprimary |  |  |
| issyncorg | 维护行政组织时需同步维护该矩阵组织信息 | CheckBoxField | t_haos_structproject.fissyncorg |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_structproject（主表） | 23 |
| t_haos_structproject_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
