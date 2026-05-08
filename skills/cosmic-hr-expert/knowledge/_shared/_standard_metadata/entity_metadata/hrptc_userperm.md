---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2VKJ94YEM7AU
app_number: hrptmc
app_name: HR报表管理中心
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hrptc_userperm — 用户授权管理

**表单编码**: `hrptc_userperm`  
**表单ID**: `49B+O3UALHKZ`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hrptc_userperm（用户授权管理） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hrptc_userperm` | 主表 · 7 列 |
| `t_hrptc_fielddisableentry` | 分录表 · 2 列 |
| `t_hrptc_rptpermdata` | 分录表 · 13 列 |
| `t_hrptc_selectreportentry` | 分录表 · 7 列 |
| `t_hrptc_selectreportgroup` | 分录表 · 1 列 |
| `t_hrptc_userperm_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptc_userperm.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptc_userperm.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptc_userperm.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptc_userperm.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hrptc_userperm_l.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| user | 用户 | UserField | t_hrptc_selectreportentry.fuserid | ✓ | bos_user |
| usernumber | 工号 | BasedataPropField | — |  |  |
| userphone | 手机号码 | BasedataPropField | — |  |  |
| useremail | 邮箱 | BasedataPropField | — |  |  |
| status | 权限档案状态 | ComboField | t_hrptc_userperm.fstatus |  |  |
| userposition | 主职职位 | TextField | — |  |  |

## 实体: fielddisableentry（维度失效分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| disablefield | 失效字段 | BasedataField | t_hrptc_fielddisableentry.fdisablefieldid |  | hrptmc_anobjqueryfield |
| disablefieldreport | 失效字段所在报表 | BasedataField | t_hrptc_fielddisableentry.fdisablefieldreportid |  | hrptmc_reportmanage |

## 实体: rptpermdataentry（数据范围） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| permuser | 权限用户 | UserField | — |  | bos_user |
| report | 权限报表 | BasedataField | t_hrptc_rptpermdata.freportid |  | hrptmc_reportmanage |
| queryfield | 查询字段 | BasedataField | t_hrptc_rptpermdata.fqueryfieldid |  | hrptmc_anobjqueryfield |
| ismerge | 是否合并授权 | CheckBoxField | t_hrptc_rptpermdata.fismerge |  |  |
| startdate | 生效日期 | DateField | t_hrptc_selectreportentry.fstartdate |  |  |
| enddate | 失效日期 | DateField | t_hrptc_selectreportentry.fenddate |  |  |
| data | 权限数据json | TextField | t_hrptc_rptpermdata.fdata |  |  |
| reportperm | 权限报表 | BasedataField | t_hrptc_selectreportentry.freportpermid |  | hrptc_rptalocperm |
| permgroup | 权限分组标识 | TextField | t_hrptc_rptpermdata.fpermgroup |  |  |
| adminorgstruct | 行政组织架构 | BasedataField | t_hrptc_rptpermdata.fadminorgstructid |  | haos_structproject |
| mustinput | 是否必填 | CheckBoxField | — |  |  |
| nolimit | 是否不限 | CheckBoxField | t_hrptc_rptpermdata.fnolimit |  |  |
| modifyuser | 管理员 | UserField | — |  | bos_user |
| includesub | 包含下级 | CheckBoxField | t_hrptc_rptpermdata.fincludesub |  |  |

## 实体: selectreportentry（已选报表分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| selectreport | 已选报表 | BasedataField | t_hrptc_selectreportentry.fselectreportid |  | hrptmc_reportmanage |
| selectreportismerge | 已选报表是否合并授权 | CheckBoxField | t_hrptc_selectreportentry.fselectreportismerge |  |  |
| selectuser | 已选用户 | UserField | — |  | bos_user |
| selectstartdate | 已选开始日期 | DateField | — |  |  |
| selectenddate | 已选结束日期 | DateField | — |  |  |
| selectadminuser | 已选管理员 | UserField | — |  | bos_user |
| selectreportperm | 权限报表 | BasedataField | — |  | hrptc_rptalocperm |
| selectreportgroupkey | 文本3 | TextField | t_hrptc_selectreportgroup.fselectreportgroupkey |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptc_userperm（主表） | 5 |
| t_hrptc_fielddisableentry | 2 |
| t_hrptc_rptpermdata | 8 |
| t_hrptc_selectreportentry | 6 |
| t_hrptc_selectreportgroup | 1 |
| t_hrptc_userperm_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 13 |
