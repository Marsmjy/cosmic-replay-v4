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

# hies_diaetplconf — 导入导出模板配置

**表单编码**: `hies_diaetplconf`  
**表单ID**: `2U7/COE5DFAH`  
**归属**: HR基础服务云 / HR导入导出管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hies_diaetplconf（导入导出模板配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hies_diaetplconf` | 主表 · 38 列 |
| `t_hies_diaetpluser` | 分录表 · 3 列 |
| `t_hies_diaetplrole` | 分录表 · 4 列 |
| `t_hies_diaetplorg` | 分录表 · 6 列 |
| `t_hies_diaetplroleorg` | 分录表 · 5 列 |
| `t_hies_diaetplfieldconf` | 分录表 · 19 列 |
| `t_hies_diaetplentity` | 分录表 · 6 列 |
| `t_hies_diaecondfieldconf` | 分录表 · 3 列 |
| `t_hies_tplsheetconf` | 分录表 · 2 列 |
| `t_hies_diaetplconf_l` | 多语言表 · 5 列 |
| `t_hies_diaetplfieldconf_l` | 多语言表 · 3 列 |
| `t_hies_diaetplentity_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hies_diaetplconf.fnumber | ✓ |  |
| name | 模板名称 | MuliLangTextField | t_hies_diaetplconf_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hies_diaetplconf.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hies_diaetplconf.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hies_diaetplconf.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hies_diaetplconf.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hies_diaetplconf.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hies_diaetplconf.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hies_diaetplconf.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hies_diaetplconf_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hies_diaetplconf_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hies_diaetplconf.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hies_diaetplconf.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hies_diaetplconf.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hies_diaetplconf.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| entitytype | 实体类型 | ComboField | t_hies_diaetplconf.fentitytype | ✓ |  |
| tmpltype | 模板类型 | ComboField | t_hies_diaetplconf.ftmpltype | ✓ |  |
| entity | 实体 | BasedataField | t_hies_diaetplconf.fentityid | ✓ | hbp_entityobject |
| orgfield | 创建组织 | OrgField | t_hies_diaetplconf.forgfield |  | bos_org |
| instruction | 模板使用说明 | MuliLangTextField | t_hies_diaetplconf_l.finstruction |  |  |
| allocationpolicy | 分配策略 | ComboField | t_hies_diaetplconf.fallocationpolicy | ✓ |  |
| applyscope | 模板适用范围 | MulComboField | t_hies_diaetplconf.fapplyscope |  |  |

## 实体: userlist（用户列表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| user | 人员 | BasedataField | t_hies_diaetpluser.fuserid |  | bos_user |
| personnumber | 工号 | BasedataPropField | t_hies_diaetpluser.fpersonnumber |  |  |
| username | 姓名 | BasedataPropField | t_hies_diaetpluser.fusername |  |  |
| position | 岗位 | TextField | — |  |  |
| userorg | 行政组织 | TextField | — |  |  |
| company | 公司 | TextField | — |  |  |

## 实体: rolelist（角色列表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| role | 角色 | BasedataField | t_hies_diaetplrole.froleid |  | perm_role |
| rolenumber | 编码 | BasedataPropField | t_hies_diaetplrole.frolenumber |  |  |
| rolename | 名称 | BasedataPropField | t_hies_diaetplrole.frolename |  |  |
| roleremark | 描述 | BasedataPropField | t_hies_diaetplrole.froleremark |  |  |

## 实体: orglist（组织列表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| org | 行政组织 | HRAdminOrgField | t_hies_diaetplroleorg.forgid |  | haos_adminorghrf7 |
| orgnumber | 行政组织编码 | BasedataPropField | t_hies_diaetplorg.forgnumber |  |  |
| orgname | 行政组织名称 | BasedataPropField | t_hies_diaetplorg.forgname |  |  |
| isincludesuborg | 包含下级 | CheckBoxField | t_hies_diaetplorg.fisincludesuborg |  |  |
| orgtype | 行政组织类型 | BasedataPropField | t_hies_diaetplorg.forgtype |  |  |
| companyname | 所属公司 | BasedataPropField | t_hies_diaetplorg.fcompanyname |  |  |

## 实体: orgrolelist（组织角色列表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| orgrole | 角色编码 | BasedataField | t_hies_diaetplroleorg.forgroleid | ✓ | perm_role |
| orgrolename | 角色名称 | BasedataPropField | t_hies_diaetplroleorg.forgrolename |  |  |
| orgrolenumber | 行政组织编码 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| roleorgname | 行政组织名称 | BasedataPropField | t_hies_diaetplroleorg.froleorgname |  |  |
| isincludesuborgrole | 包含下级 | CheckBoxField | — |  |  |
| tplscope | 模板适用范围 | ComboField | t_hies_diaetplconf.ftplscope |  |  |
| custompageval | 自定义页面 | TextField | — |  |  |
| custompagename | 自定义页面 | TextField | — |  |  |

## 实体: tpltreeentryentity（树形单据体） [TreeEntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| childentity | 实体FID | TextField | — |  |  |
| tplentityname | 实体名称 | TextField | — |  |  |
| fieldnumber | 字段编码 | TextField | t_hies_diaetplfieldconf.ffieldnumber |  |  |
| entitydescription | 显示名称 | MuliLangTextField | t_hies_diaetplfieldconf_l.fentitydescription |  |  |
| displayname | 显示名称 | TextField | — |  |  |
| isimport | 是否导入 | CheckBoxField | t_hies_diaetplfieldconf.fisimport |  |  |
| ismustinput | 是否必录 | CheckBoxField | t_hies_diaetplfieldconf.fismustinput |  |  |
| isdownloadcond | 作为下载条件 | CheckBoxField | t_hies_diaetplfieldconf.fisdownloadcond |  |  |
| iscondgetdata | 根据下载条件带出数据 | CheckBoxField | t_hies_diaetplfieldconf.fiscondgetdata |  |  |
| fieldimportdesc | 字段导入说明 | MuliLangTextField | t_hies_diaetplfieldconf_l.ffieldimportdesc |  |  |
| isfield | 是否字段 | CheckBoxField | t_hies_diaetplfieldconf.fisfield |  |  |
| imptattr | 导入属性 | ComboField | t_hies_diaetplfieldconf.fimptattr |  |  |
| exptattr | 导出属性 | TextField | t_hies_diaetplfieldconf.fexptattr |  |  |
| issheet | 带数据Sheet | ComboField | t_hies_diaetplfieldconf.fissheet |  |  |
| defvalname | 字段默认值 | TextField | — |  |  |
| defvalprop | 字段默认值 | TextField | t_hies_diaetplfieldconf.fdefvalprop |  |  |
| iscusfield | 是否自定义字段 | CheckBoxField | t_hies_diaetplfieldconf.fiscusfield |  |  |
| ischecked | 是否勾选 | CheckBoxField | t_hies_diaetplfieldconf.fischecked |  |  |
| fieldname | 字段名称 | MuliLangTextField | t_hies_diaetplfieldconf_l.ffieldname |  |  |
| cusfieldid | 自定义字段主键 | BigIntField | t_hies_diaetplfieldconf.fcusfieldid |  |  |
| sheetbdfields | Sheet字段设置 | TextField | t_hies_diaetplfieldconf.fsheetbdfields |  |  |
| sheetbdfieldnames | Sheet字段设置 | TextField | — |  |  |
| isvirfield | 是否虚字段 | CheckBoxField | t_hies_diaetplfieldconf.fisvirfield |  |  |
| importtype | 导入类型 | ComboField | t_hies_diaetplconf.fimporttype |  |  |
| enabledowncond | 启用下载条件(废弃) | ComboField | t_hies_diaetplconf.fenabledowncond |  |  |
| mainentityuniqueval | 需更新记录识别字段 | MulComboField | t_hies_diaetplconf.fmainentityuniqueval |  |  |
| plugin | 插件 | TextField | t_hies_diaetplconf.fplugin |  |  |

## 实体: entityrelation（子实体列表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rentity | 实体名称 | BasedataField | t_hies_diaetplentity.frentityid |  | bos_entityobject |
| entitynumber | 实体编码 | BasedataPropField | t_hies_diaetplentity.fentitynumber |  |  |
| entityuniqueval | 需更新记录识别字段 | TextField | t_hies_diaetplentity.fentityuniqueval |  |  |
| entityuniquename | 需更新记录识别字段 | TextField | — |  |  |
| relationleftpropname | 本实体字段 | TextField | — |  |  |
| relationleftprop | 本实体字段 | TextField | t_hies_diaetplentity.frelationleftprop |  |  |
| relationcondition | 条件 | TextField | — |  |  |
| relationrightpropname | 主实体字段 | TextField | — |  |  |
| relationrightprop | 主实体字段 | TextField | t_hies_diaetplentity.frelationrightprop |  |  |
| rentityalias | 实体别名 | MuliLangTextField | t_hies_diaetplentity_l.frentityalias |  |  |
| fieldmerge | 字段合并 | TextField | t_hies_diaetplconf.ffieldmerge |  |  |
| fieldstyle | 字段样式 | TextField | t_hies_diaetplconf.ffieldstyle |  |  |

## 实体: condentryentity（单据体222） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| condnumber | 编码 | TextField | t_hies_diaecondfieldconf.fcondnumber |  |  |
| condname | 名称 | TextField | — |  |  |
| downconditionval | 选择数据下载条件 | TextField | t_hies_diaecondfieldconf.fdownconditionval |  |  |
| downconditionname | 选择数据下载条件 | TextField | — |  |  |
| condentitynumber | 实体编码 | TextField | t_hies_diaecondfieldconf.fcondentitynumber |  |  |
| conditionfield | 条件字段 | TextField | — |  |  |
| attachmentfield | 附件 | AttachmentField | — |  |  |
| metaversion | 元数据版本 | TextField | t_hies_diaetplconf.fmetaversion |  |  |
| source | 模板来源 | ComboField | t_hies_diaetplconf.fsource |  |  |
| tplgenmode | 模板生成方式 | ComboField | t_hies_diaetplconf.ftplgenmode |  |  |

## 实体: entryentity（导入设置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sheetname | sheet页名称 | TextField | t_hies_tplsheetconf.fsheetname |  |  |
| sheetreadrow | 数据导入行 | IntegerField | t_hies_tplsheetconf.fsheetreadrow | ✓ |  |
| queryentity | 实体 | BasedataField | t_hies_diaetplconf.fqueryentityid | ✓ | hbp_entityobject |
| hidelogotypelineflag | 隐藏标识行 | CheckBoxField | t_hies_diaetplconf.fhidelogotypelineflag |  |  |
| sourcetplid | 源模板id | BigIntField | t_hies_diaetplconf.fsourcetplid |  |  |
| mainentityalias | 主实体别名 | MuliLangTextField | t_hies_diaetplconf_l.fmainentityalias |  |  |
| enablepersonid | 是否开启员工系统ID导入/导出 | CheckBoxField | t_hies_diaetplconf.fenablepersonid |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hies_diaetplconf（主表） | 32 |
| t_hies_diaecondfieldconf | 3 |
| t_hies_diaetplconf_l | 5 |
| t_hies_diaetplentity | 5 |
| t_hies_diaetplentity_l | 1 |
| t_hies_diaetplfieldconf | 15 |
| t_hies_diaetplfieldconf_l | 3 |
| t_hies_diaetplorg | 5 |
| t_hies_diaetplrole | 4 |
| t_hies_diaetplroleorg | 4 |
| t_hies_diaetpluser | 3 |
| t_hies_tplsheetconf | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 24 |
