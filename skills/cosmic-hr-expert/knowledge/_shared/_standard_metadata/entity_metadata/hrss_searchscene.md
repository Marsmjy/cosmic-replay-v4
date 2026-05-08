# hrss_searchscene — 搜索场景

**表单编码**: `hrss_searchscene`  
**表单ID**: `2KL6Z7YBH6MJ`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_searchscene（搜索场景） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrss_searchscenecfg` | BaseEntity | 主表 |
| `t_hrss_sceneentity` | EntryEntity | 单据体 |
| `t_hrss_searchproject` | EntryEntity | 精准搜索项目单据体 |
| `t_hrss_secondfilter` | EntryEntity | 二次过滤条件单据体 |
| `t_hrss_searchpage` | MulEmployeeField子表 | 前端业务搜索页面 |

### 字段列表 — t_hrss_searchscenecfg（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrss_searchscenecfg.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrss_searchscenecfg.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrss_searchscenecfg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrss_searchscenecfg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrss_searchscenecfg.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrss_searchscenecfg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrss_searchscenecfg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrss_searchscenecfg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrss_searchscenecfg.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrss_searchscenecfg.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrss_searchscenecfg.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrss_searchscenecfg.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrss_searchscenecfg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrss_searchscenecfg.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrss_searchscenecfg.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrss_searchscenecfg.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrss_searchscenecfg.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrss_searchscenecfg.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrss_searchscenecfg.foriname |  |  |
| searchcategory | 默认搜索分类 | ComboField | t_hrss_searchscenecfg.fsearchcategory | ✓ |  |
| searchkeytype | 默认搜索条件间关系 | ComboField | t_hrss_searchscenecfg.fsearchkeytype | ✓ |  |
| app | 所属应用 | BasedataField | t_hrss_searchscenecfg.fappid | ✓ | hbp_devportal_bizapp |
| searchobj | 搜索对象 | BasedataField | t_hrss_searchscenecfg.fsearchobjid | ✓ | hrss_searchobject |
| gptprompt | GPT提示词 | BasedataField | t_hrss_searchscenecfg.fgptpromptid |  | gai_prompt |
| copywriting | 默认搜索提示文案 | MuliLangTextField | t_hrss_searchscenecfg.fcopywriting | ✓ |  |
| searchpage | 前端业务搜索页面 | MulBasedataField | t_hrss_searchpage（子表） |  |  |
| filtergrpenable | 二次过滤条件启用分组 | CheckBoxField | t_hrss_searchscenecfg.ffiltergrpenable |  |  |
| sceneentryentity | 单据体 | EntryEntity | → t_hrss_sceneentity |  |  |
| searchentryentity | 精准搜索项目单据体 | EntryEntity | → t_hrss_searchproject |  |  |
| filterentryentity | 二次过滤条件单据体 | EntryEntity | → t_hrss_secondfilter |  |  |

### 字段列表 — t_hrss_sceneentity（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| viewname | 前端显示名 | MuliLangTextField | t_hrss_sceneentity.fviewname |  |  |
| entitytype | 基础资料 | BasedataField | — |  | hbp_entityobject |
| queryfield | 搜索对象查询字段 | BasedataField | t_hrss_sceneentity.fqueryfield |  | hrss_schobjqueryfield |
| fieldnumber | 字段标识 | TextField | — |  |  |
| fieldtext | 字段名称 | MuliLangTextField | — |  |  |
| fieldsource | 字段来源 | ComboField | t_hrss_sceneentity.ffieldsource |  |  |
| label | 标签 | BasedataField | t_hrss_sceneentity.flabel |  | hrcs_label |
| searchfieldtype | 字段类型 | ComboField | — |  |  |
| searchobjentityid | 所属实体 | BasedataField | t_hrss_sceneentity.fsearchobjentityid |  | hrss_schobjjoinentity |

### 字段列表 — t_hrss_searchproject（精准搜索项目单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| searchdisplayname | 搜索范围名称 | MuliLangTextField | t_hrss_searchproject.fsearchdisplayname |  |  |
| searchinfotype | 搜索范围类型 | ComboField | t_hrss_searchproject.fsearchinfotype |  |  |
| entityorfield | 实体/字段 | MuliLangTextField | — |  |  |
| searchmode | 搜索方式 | MulComboField | t_hrss_searchproject.fsearchmode |  |  |
| rangenumber | 标识 | TextField | t_hrss_searchproject.frangenumber |  |  |
| inputprompt | 默认搜索提示文案 | MuliLangTextField | t_hrss_searchproject.finputprompt |  |  |

### 字段列表 — t_hrss_secondfilter（二次过滤条件单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| searchobjfield | 字段 | BasedataField | t_hrss_secondfilter.fsearchobjfieldid |  | hrss_schobjqueryfield |
| fieldtype | 字段类型 | ComboField | t_hrss_secondfilter.ffieldtype |  |  |
| filterdisplayname | 显示名 | MuliLangTextField | t_hrss_secondfilter.ffilterdisplayname |  |  |
| commonfilter | 常用过滤条件 | CheckBoxField | t_hrss_secondfilter.fcommonfilter |  |  |
| customfilter | 自定义过滤项 | BasedataField | t_hrss_secondfilter.fcustomfilterid |  | hrss_customfilter |
| fieldname | 字段 | TextField | — |  |  |
| labelfilterid | 标签 | BasedataField | t_hrss_secondfilter.flabelfilterid |  | hrcs_label |
| labeltype | 标签类型 | ComboField | t_hrss_secondfilter.flabeltype |  |  |
| labelentity | 标签所属实体 | TextField | t_hrss_secondfilter.flabelentity |  |  |
| labeldefaultview | 默认平铺展示 | TextField | — |  |  |
| labeldefaultviewid | 默认展示标签id | TextField | t_hrss_secondfilter.flabeldefaultviewid |  |  |
| group | 所属分组 | BasedataField | t_hrss_secondfilter.fgroup |  | hrss_scenefiltergroup |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrss_searchscenecfg（主表） | 27 |
| t_hrss_sceneentity（单据体） | 9 |
| t_hrss_searchproject（精准搜索项目单据体） | 6 |
| t_hrss_secondfilter（二次过滤条件单据体） | 12 |
| t_hrss_searchpage（MulEmployeeField子表） | 1 |
| 无数据库列 | 7 |

