---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2WQD=V5DU=7Q
app_number: hies
app_name: HR导入导出管理
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hies_multientry_tpl — 单据体导入模板配置

**表单编码**: `hies_multientry_tpl`  
**表单ID**: `4HJW=A+H7O+4`  
**归属**: HR基础服务云 / HR导入导出管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hies_multientry_tpl（单据体导入模板配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hies_mentrytplconf` | 主表 · 34 列 |
| `t_hies_mentrytpluser` | 分录表 · 3 列 |
| `t_hies_mentrytplrole` | 分录表 · 4 列 |
| `t_hies_mentrytplorg` | 分录表 · 6 列 |
| `t_hies_mentrytplroleorg` | 分录表 · 5 列 |
| `t_hies_mentrytplfieldconf` | 分录表 · 19 列 |
| `t_hies_mentrycondfieldcon` | 分录表 · 3 列 |
| `t_hies_multientrylist` | 分录表 · 2 列 |
| `t_hies_mentrytplconf_l` | 多语言表 · 4 列 |
| `t_hies_mentrytplfieldconf_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hies_mentrytplconf.fnumber | ✓ |  |
| name | 模板名称 | MuliLangTextField | t_hies_mentrytplconf_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hies_mentrytplconf.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hies_mentrytplconf.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hies_mentrytplconf.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hies_mentrytplconf.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hies_mentrytplconf.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hies_mentrytplconf.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hies_mentrytplconf.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hies_mentrytplconf_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hies_mentrytplconf_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hies_mentrytplconf.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hies_mentrytplconf.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hies_mentrytplconf.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hies_mentrytplconf.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| tmpltype | 模板类型 | ComboField | t_hies_mentrytplconf.ftmpltype | ✓ |  |
| entity | 实体 | BasedataField | t_hies_mentrytplconf.fentityid | ✓ | hbp_entityobject |
| orgfield | 创建组织 | OrgField | t_hies_mentrytplconf.forgfield |  | bos_org |
| instruction | 模板使用说明 | MuliLangTextField | t_hies_mentrytplconf_l.finstruction |  |  |
| allocationpolicy | 分配策略 | ComboField | t_hies_mentrytplconf.fallocationpolicy | ✓ |  |
| applyscope | 模板适用范围 | MulComboField | t_hies_mentrytplconf.fapplyscope |  |  |

## 实体: userlist（单据体模板用户） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| user | 人员 | BasedataField | t_hies_mentrytpluser.fuserid |  | bos_user |
| personnumber | 工号 | BasedataPropField | t_hies_mentrytpluser.fpersonnumber |  |  |
| username | 姓名 | BasedataPropField | t_hies_mentrytpluser.fusername |  |  |
| position | 岗位 | TextField | — |  |  |
| userorg | 行政组织 | TextField | — |  |  |
| company | 公司 | TextField | — |  |  |

## 实体: rolelist（单据体模板角色） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| role | 角色 | BasedataField | t_hies_mentrytplrole.froleid |  | perm_role |
| rolenumber | 编码 | BasedataPropField | t_hies_mentrytplrole.frolenumber |  |  |
| rolename | 名称 | BasedataPropField | t_hies_mentrytplrole.frolename |  |  |
| roleremark | 描述 | BasedataPropField | t_hies_mentrytplrole.froleremark |  |  |

## 实体: orglist（单据体模板组织） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| org | 行政组织 | HRAdminOrgField | t_hies_mentrytplroleorg.forgid |  | haos_adminorghrf7 |
| orgnumber | 行政组织编码 | BasedataPropField | t_hies_mentrytplorg.forgnumber |  |  |
| orgname | 行政组织名称 | BasedataPropField | t_hies_mentrytplorg.forgname |  |  |
| isincludesuborg | 包含下级 | CheckBoxField | t_hies_mentrytplorg.fisincludesuborg |  |  |
| orgtype | 行政组织类型 | BasedataPropField | t_hies_mentrytplorg.forgtype |  |  |
| companyname | 所属公司 | BasedataPropField | t_hies_mentrytplorg.fcompanyname |  |  |

## 实体: orgrolelist（单据体模板组织角色） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| orgrole | 角色编码 | BasedataField | t_hies_mentrytplroleorg.forgroleid | ✓ | perm_role |
| orgrolename | 角色名称 | BasedataPropField | t_hies_mentrytplroleorg.forgrolename |  |  |
| orgrolenumber | 行政组织编码 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| roleorgname | 行政组织名称 | BasedataPropField | t_hies_mentrytplroleorg.froleorgname |  |  |
| isincludesuborgrole | 包含下级 | CheckBoxField | — |  |  |
| tplscope | 模板适用范围 | ComboField | t_hies_mentrytplconf.ftplscope |  |  |
| custompageval | 自定义页面 | TextField | — |  |  |
| custompagename | 自定义页面 | TextField | — |  |  |

## 实体: tpltreeentryentity（单据体字段） [TreeEntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| childentity | 实体FID | TextField | — |  |  |
| tplentityname | 实体名称 | TextField | — |  |  |
| fieldnumber | 字段编码 | TextField | t_hies_mentrytplfieldconf.ffieldnumber |  |  |
| entitydescription | 显示名称 | MuliLangTextField | t_hies_mentrytplfieldconf_l.fentitydescription |  |  |
| displayname | 显示名称 | TextField | — |  |  |
| isimport | 是否导入 | CheckBoxField | t_hies_mentrytplfieldconf.fisimport |  |  |
| ismustinput | 是否必录 | CheckBoxField | t_hies_mentrytplfieldconf.fismustinput |  |  |
| isdownloadcond | 作为下载条件 | CheckBoxField | t_hies_mentrytplfieldconf.fisdownloadcond |  |  |
| iscondgetdata | 根据下载条件带出数据 | CheckBoxField | t_hies_mentrytplfieldconf.fiscondgetdata |  |  |
| fieldimportdesc | 字段导入说明 | MuliLangTextField | t_hies_mentrytplfieldconf_l.ffieldimportdesc |  |  |
| isfield | 是否字段 | CheckBoxField | t_hies_mentrytplfieldconf.fisfield |  |  |
| imptattr | 导入属性 | ComboField | t_hies_mentrytplfieldconf.fimptattr |  |  |
| exptattr | 导出属性 | TextField | t_hies_mentrytplfieldconf.fexptattr |  |  |
| issheet | 带数据Sheet | ComboField | t_hies_mentrytplfieldconf.fissheet |  |  |
| defvalname | 字段默认值 | TextField | — |  |  |
| defvalprop | 字段默认值 | TextField | t_hies_mentrytplfieldconf.fdefvalprop |  |  |
| iscusfield | 是否自定义字段 | CheckBoxField | t_hies_mentrytplfieldconf.fiscusfield |  |  |
| ischecked | 是否勾选 | CheckBoxField | t_hies_mentrytplfieldconf.fischecked |  |  |
| fieldname | 字段名称 | MuliLangTextField | t_hies_mentrytplfieldconf_l.ffieldname |  |  |
| cusfieldid | 自定义字段主键 | BigIntField | t_hies_mentrytplfieldconf.fcusfieldid |  |  |
| sheetbdfields | Sheet字段设置 | TextField | t_hies_mentrytplfieldconf.fsheetbdfields |  |  |
| sheetbdfieldnames | Sheet字段设置 | TextField | — |  |  |
| entrykey | 单据体key | TextField | t_hies_mentrytplfieldconf.fentrykey |  |  |
| importtype | 导入类型 | ComboField | t_hies_mentrytplconf.fimporttype |  |  |
| enabledowncond | 启用下载条件 | ComboField | t_hies_mentrytplconf.fenabledowncond |  |  |
| plugin | 插件 | TextField | t_hies_mentrytplconf.fplugin |  |  |
| fieldmerge | 字段合并 | TextField | t_hies_mentrytplconf.ffieldmerge |  |  |
| fieldstyle | 字段样式 | TextField | t_hies_mentrytplconf.ffieldstyle |  |  |

## 实体: condentryentity（单据体模板下载条件） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| condnumber | 编码 | TextField | t_hies_mentrycondfieldcon.fcondnumber |  |  |
| condname | 名称 | TextField | — |  |  |
| downconditionval | 选择数据下载条件 | TextField | t_hies_mentrycondfieldcon.fdownconditionval |  |  |
| downconditionname | 选择数据下载条件 | TextField | — |  |  |
| condentitynumber | 实体编码 | TextField | t_hies_mentrycondfieldcon.fcondentitynumber |  |  |
| conditionfield | 条件字段 | TextField | — |  |  |
| metaversion | 元数据版本 | TextField | t_hies_mentrytplconf.fmetaversion |  |  |
| source | 模板来源 | ComboField | t_hies_mentrytplconf.fsource |  |  |
| hidelogotypelineflag | 隐藏标识行 | CheckBoxField | t_hies_mentrytplconf.fhidelogotypelineflag |  |  |

## 实体: entrylist（单据体列表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryuniqueval | 需更新记录识别字段 | TextField | t_hies_multientrylist.fentryuniqueval |  |  |
| entryuniquename | 需更新记录识别字段 | TextField | — | ✓ |  |
| entrynumber | 单据体标识 | TextField | t_hies_multientrylist.fentrynumber |  |  |
| entryname | 单据体名称 | TextField | — |  |  |
| entrytype | 单据体 | MulComboField | t_hies_mentrytplconf.fentrytype | ✓ |  |
| enablepersonid | 是否开启员工系统ID导入/导出 | CheckBoxField | t_hies_mentrytplconf.fenablepersonid |  |  |
| sourcetplid | 源模板id | BigIntField | t_hies_mentrytplconf.fsourcetplid |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hies_mentrytplconf（主表） | 29 |
| t_hies_mentrycondfieldcon | 3 |
| t_hies_mentrytplconf_l | 4 |
| t_hies_mentrytplfieldconf | 15 |
| t_hies_mentrytplfieldconf_l | 3 |
| t_hies_mentrytplorg | 5 |
| t_hies_mentrytplrole | 4 |
| t_hies_mentrytplroleorg | 4 |
| t_hies_mentrytpluser | 3 |
| t_hies_multientrylist | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 21 |
