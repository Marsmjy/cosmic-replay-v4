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

# homs_cardstyle_new — 卡片样式设置

**表单编码**: `homs_cardstyle_new`  
**表单ID**: `3TROVUOAL0HU`  
**归属**: 组织发展云 / 组织管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: homs_cardstyle_new（卡片样式设置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_homs_cardstyle` | 主表 · 31 列 |
| `t_homs_cardstyleorgentry` | 分录表 · 2 列 |
| `t_homs_cardstyleentry` | 分录表 · 12 列 |
| `t_homs_colorschemaentry` | 分录表 · 4 列 |
| `t_homs_cardstyle_l` | 多语言表 · 4 列 |
| `t_homs_cardstyleentry_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_homs_cardstyle.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_homs_cardstyle_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_homs_cardstyle.fstatus |  |  |
| creator | 创建人 | CreaterField | t_homs_cardstyle.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_homs_cardstyle.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_homs_cardstyle.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_homs_cardstyle.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_homs_cardstyle.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_homs_cardstyle.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_homs_cardstyle_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_homs_cardstyle_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_homs_cardstyle.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_homs_cardstyle.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_homs_cardstyle.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_homs_cardstyle.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_homs_cardstyle.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_homs_cardstyle.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_homs_cardstyle_l.foriname |  |  |

## 实体: orgentryentity（适用组织单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| adminorgid | 组织名称 | HRAdminOrgField | t_homs_cardstyleorgentry.fadminorgid | ✓ | haos_adminorghrf7 |
| iscontainslower | 包含下级 | CheckBoxField | t_homs_cardstyleorgentry.fiscontainslower |  |  |
| orgnumber | 组织编码 | TextField | — |  |  |
| adminorgnumber | 组织编码 | TextField | — |  |  |

## 实体: cardentryentity（卡片字段单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| cardcontentid | 已选字段 | BasedataField | t_homs_cardstyleentry.fcardcontentid |  | homs_cardconfig |
| aftercontent | 后置单位 | MuliLangTextField | t_homs_cardstyleentry_l.faftercontent |  |  |
| beforecontent | 前置单位 | MuliLangTextField | t_homs_cardstyleentry_l.fbeforecontent |  |  |
| wordsize | 字号 | ComboField | t_homs_cardstyleentry.fwordsize |  |  |
| rows | 行数 | ComboField | t_homs_cardstyleentry.frows |  |  |
| bold | 是否粗体 | CheckBoxField | t_homs_cardstyleentry.fbold |  |  |
| xcoordinate | 横坐标 | IntegerField | t_homs_cardstyleentry.fxcoordinate |  |  |
| ycoordinate | 纵坐标 | IntegerField | t_homs_cardstyleentry.fycoordinate |  |  |
| wide | 宽 | IntegerField | t_homs_cardstyleentry.fwide |  |  |
| high | 高 | IntegerField | t_homs_cardstyleentry.fhigh |  |  |
| fontjustification | 字体对齐 | TextField | t_homs_cardstyleentry.ffontjustification |  |  |
| isdefault | 是否为默认字段 | ComboField | t_homs_cardstyleentry.fisdefault |  |  |
| contentadaptation | 内容自适应 | CheckBoxField | t_homs_cardstyle.fcontentadaptation |  |  |
| adminorg | 所属组织 | HRAdminOrgField | t_homs_cardstyleorgentry.fadminorgid | ✓ | haos_adminorghrf7 |
| displayfield | 显示字段 | MulBasedataField | — |  |  |
| carddimension | 卡片维度 | BasedataField | t_homs_cardstyle.fcarddimensionid | ✓ | homs_carddimension |
| operate | 自定义控件当前操作 | TextField | — |  |  |
| usage | 使用用途 | ComboField | t_homs_cardstyle.fusage |  |  |
| backgroundcolor | 背景色 | TextField | t_homs_cardstyle.fbackgroundcolor |  |  |
| bordercolor | 边框颜色 | TextField | t_homs_cardstyle.fbordercolor |  |  |
| topbordercolor | 上边框颜色 | TextField | t_homs_cardstyle.ftopbordercolor |  |  |
| borderlinestype | 边框线条类型 | ComboField | t_homs_cardstyle.fborderlinestype |  |  |
| cardstyle | 卡片样式 | LargeTextField | t_homs_cardstyle.fcardstyle |  |  |
| cardstyleflag | 卡片样式类型 | ComboField | t_homs_cardstyle.fcardstyleflag |  |  |
| cardstylestdlibid | 卡片样式模板 | BasedataField | t_homs_cardstyle.fcardstylestdlibid |  | homs_cardstylestdlib |

## 实体: colorschemaentry（配色方案分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| color | 配色 | TextField | — |  |  |
| colorcode | 不可见颜色字段 | TextField | t_homs_colorschemaentry.fcolorcode |  |  |
| policy | 不可见策略字段 | BasedataField | t_homs_colorschemaentry.fpolicyid |  | brm_policy_edit |
| rule | 规则 | LargeTextField | — |  |  |
| conditions | 条件 | LargeTextField | t_homs_colorschemaentry.fconditions |  |  |
| colorschemadimension | 显示维度 | BasedataField | — |  | homs_cardconfig |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_homs_cardstyle（主表） | 24 |
| t_homs_cardstyle_l | 4 |
| t_homs_cardstyleentry | 10 |
| t_homs_cardstyleentry_l | 2 |
| t_homs_cardstyleorgentry | 3 |
| t_homs_colorschemaentry | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
