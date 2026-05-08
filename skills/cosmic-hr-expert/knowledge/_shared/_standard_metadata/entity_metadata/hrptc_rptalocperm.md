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

# hrptc_rptalocperm — 报表授权管理

**表单编码**: `hrptc_rptalocperm`  
**表单ID**: `49W+DPHHHD2V`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hrptc_rptalocperm（报表授权管理） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hrptc_rptalocperm` | 主表 · 6 列 |
| `t_hrptc_rptpermdata` | 分录表 · 13 列 |
| `t_hrptc_selectreportentry` | 分录表 · 7 列 |
| `t_hrptc_rptalocperm_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptc_rptalocperm.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptc_rptalocperm.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptc_rptalocperm.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptc_rptalocperm.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| rptmanage | 报表 | BasedataField | t_hrptc_rptalocperm.frptmanageid |  | hrptmc_reportmanage |
| number | 编码 | BasedataPropField | — |  |  |
| name | 名称 | BasedataPropField | — |  |  |
| cloud | 归属业务领域 | BasedataPropField | — |  |  |
| description | 描述 | MuliLangTextField | t_hrptc_rptalocperm_l.fdescription |  |  |
| publishpath | 发布位置 | TextField | — |  |  |

## 实体: rptpermdataentry（数据范围） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| user | 用户 | UserField | t_hrptc_selectreportentry.fuserid |  | bos_user |
| report | 报表 | BasedataField | t_hrptc_rptpermdata.freportid |  | hrptmc_reportmanage |
| queryfield | 查询字段 | BasedataField | t_hrptc_rptpermdata.fqueryfieldid |  | hrptmc_anobjqueryfield |
| ismerge | 是否合并授权 | CheckBoxField | t_hrptc_rptpermdata.fismerge |  |  |
| startdate | 有效开始时间 | DateField | t_hrptc_selectreportentry.fstartdate |  |  |
| enddate | 有效结束时间 | DateField | t_hrptc_selectreportentry.fenddate |  |  |
| data | 权限数据json | TextField | t_hrptc_rptpermdata.fdata |  |  |
| userperm | 权限用户 | BasedataField | t_hrptc_selectreportentry.fuserpermid |  | hrptc_userperm |
| permgroup | 权限分组标识 | TextField | t_hrptc_rptpermdata.fpermgroup |  |  |
| adminorgstruct | 行政组织架构 | BasedataField | t_hrptc_rptpermdata.fadminorgstructid |  | haos_structproject |
| mustinput | 是否必填 | CheckBoxField | — |  |  |
| nolimit | 是否不限 | CheckBoxField | t_hrptc_rptpermdata.fnolimit |  |  |
| modifyuser | 管理员 | UserField | — |  | bos_user |
| includesub | 包含下级 | CheckBoxField | t_hrptc_rptpermdata.fincludesub |  |  |

## 实体: entryentity22（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dispicture22 | 头像 | BasedataPropField | — |  |  |
| disusername22 | 姓名 | BasedataPropField | — |  |  |
| disnumber22 | 工号 | BasedataPropField | — |  |  |
| disstartdate22 | 有效开始时间 | DateField | — |  |  |
| disenddate22 | 有效结束时间 | DateField | — |  |  |
| disuser22 | 用户 | UserField | — |  | bos_user |
| dismodifyuser22 | 管理员 | UserField | — |  | bos_user |
| rptdescription | 描述 | BasedataPropField | — |  |  |

## 实体: entryentity（授权管理） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| disreport | 已选报表 | BasedataField | — |  | hrptmc_reportmanage |
| disrptismerge | 已选报表是否合并授权 | CheckBoxField | — |  |  |
| disuser | 已选用户 | UserField | — |  | bos_user |
| disstartdate | 有效开始时间 | DateField | — |  |  |
| disenddate | 有效结束时间 | DateField | — |  |  |
| dismodifyuser | 管理员 | UserField | t_hrptc_rptpermdata.fdismodifyuserid |  | bos_user |
| dispicture | 头像 | BasedataPropField | — |  |  |
| disusername | 姓名 | BasedataPropField | — |  |  |
| disnumber | 工号 | BasedataPropField | — |  |  |
| disuserperm | 权限用户 | UserField | — |  | bos_user |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptc_rptalocperm（主表） | 5 |
| t_hrptc_rptalocperm_l | 1 |
| t_hrptc_rptpermdata | 9 |
| t_hrptc_selectreportentry | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 24 |
