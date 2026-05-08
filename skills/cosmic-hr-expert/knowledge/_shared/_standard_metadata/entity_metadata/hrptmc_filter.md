# hrptmc_filter — 筛选器设置

**表单编码**: `hrptmc_filter`  
**表单ID**: `2VUH2N8NV7P4`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_filter（筛选器设置） [BaseEntity]

- **数据库表**: `t_hrptmc_filter`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| radiofield_required | 必选 | RadioField | — |  |  |
| ismust | 单选按钮组 | RadioGroupField | fismust |  |  |
| radiofield_norequired | 非必选 | RadioField | — |  |  |
| datetype | 筛选器方式按钮组 | RadioGroupField | fdatetype |  |  |
| radiofield_scope | 范围选择 | RadioField | — |  |  |
| basedataismul | 基础资料筛选器方式按钮组 | RadioGroupField | fbasedataismul |  |  |
| radiofield_single | 单选 | RadioField | — |  |  |
| radiofield_mul | 多选 | RadioField | — |  |  |
| filtertype | 筛选器类型 | ComboField | ffiltertype |  |  |
| hisdate | 生效日期选择类型 | ComboField | fhisdate |  |  |
| enable | 显示禁用按钮组 | RadioGroupField | fenable |  |  |
| radiofield_enable | 是 | RadioField | — |  |  |
| radiofield_disenable | 否 | RadioField | — |  |  |
| radiofield_day | 单日选择 | RadioField | — |  |  |
| name | 多语言文本 | MuliLangTextField | fname |  |  |
| hisscope | 数据范围按钮组 | RadioGroupField | fhisscope |  |  |
| currentdata_scope | 当前数据 | RadioField | — |  |  |
| alldata_scope | 所有数据 | RadioField | — |  |  |
| orglevel | 组织层级按钮组 | RadioGroupField | forglevel |  |  |
| orglevel_yes | 支持 | RadioField | — |  |  |
| orglevel_no | 不支持 | RadioField | — |  |  |
| suborg | 统计包含下级按钮组 | RadioGroupField | fsuborg |  |  |
| suborg_yes | 包含 | RadioField | — |  |  |
| suborg_cus | 自定义 | RadioField | — |  |  |
| suborg_no | 不包含 | RadioField | — |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| rptmanage | 报表管理 | BasedataField | frptmanageid |  | hrptmc_reportmanage |
| index | 排序号 | IntegerField | findex |  |  |
| anobjfield | 分析对象字段 | BasedataField | fanobjfieldid |  | hrptmc_anobjqueryfield |
| groupdate | 组合日期按钮组 | RadioGroupField | fgroupdate |  |  |
| yes | 是 | RadioField | — |  |  |
| no | 否 | RadioField | — |  |  |
| enddate | 历史时段查询-结束日期 | BasedataField | fenddate |  | hrptmc_anobjqueryfield |
| begindate | 历史时段查询-开始日期 | BasedataField | fbegindate |  | hrptmc_anobjqueryfield |
| textdefaultvalue | 文本筛选默认值 | TextField | ftextdefaultvalue |  |  |
| textfilterrange | 文本筛选范围 | TextField | ftextfilterrange |  |  |
| datefiltertype | 日期筛选默认值类型 | TextField | fdatefiltertype |  |  |
| datefiltertext | 日期筛选默认值文本 | TextField | fdatefiltertext |  |  |
| daterangefield | 日期范围 | DateRangeField | — |  |  |
| bddefaultvalue | 基础资料默认值 | TextField | fbddefaultvalue |  |  |
| bdfilterrange | 基础资料筛选范围 | TextField | fbdfilterrange |  |  |
| quickscope | 快捷区间 | MulComboField | fquickscope |  |  |
| splitdate | 日期拆分字段 | BasedataField | fsplitdateid |  | hrptmc_splitdate |
| groupfield | 分组赋值字段 | BasedataField | fgroupfieldid |  | hrptmc_anobjgroupfield |
| isgroupfield | 是否分组赋值字段 | CheckBoxField | fisgroupfield |  |  |
| defnum2 | 小数2 | DecimalField | — |  |  |
| opt | 下拉列表4 | ComboField | fopt |  |  |
| defnum1 | 小数1 | DecimalField | — |  |  |
| truevalue | 勾选内容 | MuliLangTextField | ftruevalue |  |  |
| falsevalue | 不勾选内容 | MuliLangTextField | ffalsevalue |  |  |
| booleandefval | 布尔类型默认值 | ComboField | fbooleandefval |  |  |
| searchmode | 搜索模式按钮组 | RadioGroupField | fsearchmode |  |  |
| fuzzy | 模糊搜索 | RadioField | — |  |  |
| precise | 精确搜索 | RadioField | — |  |  |
| begindate_0 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_0 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| mulgroupdate | 日期组合查询 | TextField | fmulgroupdate |  |  |
| groupdatecount | 组合字段个数 | IntegerField | — |  |  |
| groupdateentity_0 | 查询实体 | ComboField | — |  |  |
| groupdateentity_1 | 查询实体 | ComboField | — |  |  |
| begindate_1 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_1 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_ | 查询实体 | ComboField | — |  |  |
| begindate_ | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_ | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_2 | 查询实体 | ComboField | — |  |  |
| begindate_2 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_2 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_3 | 查询实体 | ComboField | — |  |  |
| begindate_3 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_3 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_4 | 查询实体 | ComboField | — |  |  |
| begindate_4 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_4 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_5 | 查询实体 | ComboField | — |  |  |
| begindate_5 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_5 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_6 | 查询实体 | ComboField | — |  |  |
| begindate_6 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_6 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_7 | 查询实体 | ComboField | — |  |  |
| begindate_7 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_7 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_8 | 查询实体 | ComboField | — |  |  |
| begindate_8 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_8 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_9 | 查询实体 | ComboField | — |  |  |
| begindate_9 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_9 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_10 | 查询实体 | ComboField | — |  |  |
| begindate_10 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_10 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_11 | 查询实体 | ComboField | — |  |  |
| begindate_11 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_11 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_12 | 查询实体 | ComboField | — |  |  |
| begindate_12 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_12 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_13 | 查询实体 | ComboField | — |  |  |
| begindate_13 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_13 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_14 | 查询实体 | ComboField | — |  |  |
| begindate_14 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_14 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_15 | 查询实体 | ComboField | — |  |  |
| begindate_15 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_15 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_16 | 查询实体 | ComboField | — |  |  |
| begindate_16 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_16 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_17 | 查询实体 | ComboField | — |  |  |
| begindate_17 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_17 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_18 | 查询实体 | ComboField | — |  |  |
| begindate_18 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_18 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| groupdateentity_19 | 查询实体 | ComboField | — |  |  |
| begindate_19 | 历史时段查询-开始日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| enddate_19 | 历史时段查询-结束日期 | BasedataField | — |  | hrptmc_anobjqueryfield |
| hisdata_scope | 历史数据 | RadioField | — |  |  |
| datescope | 范围选择 | MulComboField | fdatescope |  |  |

