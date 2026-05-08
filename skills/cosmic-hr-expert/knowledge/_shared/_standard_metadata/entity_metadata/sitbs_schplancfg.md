---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 17/+7RIW4SCJ
app_number: sitbs
app_name: 社保个税基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# sitbs_schplancfg — 调度计划配置

**表单编码**: `sitbs_schplancfg`  
**表单ID**: `4AQ9KIOGS74E`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_schplancfg（调度计划配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_schplancfg` | 主表 · 19 列 |
| `t_sitbs_schplancfg_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_schplancfg.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_schplancfg_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_schplancfg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_schplancfg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_schplancfg.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_schplancfg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_schplancfg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_schplancfg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_schplancfg.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_schplancfg_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_schplancfg_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_sitbs_schplancfg.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_schplancfg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_schplancfg.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_schplancfg.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| repeatmode | 重复时间单位 | ComboField | — | ✓ |  |
| cyclenum | 重复周期 | IntegerField | — | ✓ |  |
| plan | cron表达式 | TextField | — | ✓ |  |
| combdorw | 按日期或星期 | ComboField | — |  |  |
| radiogroupfield1 | 单选按钮组 | RadioGroupField | — |  |  |
| ckone | 01 | CheckBoxField | — |  |  |
| cktwo | 02 | CheckBoxField | — |  |  |
| ckthree | 03 | CheckBoxField | — |  |  |
| ckfour | 04 | CheckBoxField | — |  |  |
| ckfive | 05 | CheckBoxField | — |  |  |
| cksix | 06 | CheckBoxField | — |  |  |
| ckseven | 07 | CheckBoxField | — |  |  |
| ckeight | 08 | CheckBoxField | — |  |  |
| cknine | 09 | CheckBoxField | — |  |  |
| ckten | 10 | CheckBoxField | — |  |  |
| ckeleven | 11 | CheckBoxField | — |  |  |
| cktwelve | 12 | CheckBoxField | — |  |  |
| ckthirteen | 13 | CheckBoxField | — |  |  |
| ckfourteen | 14 | CheckBoxField | — |  |  |
| ckfifteen | 15 | CheckBoxField | — |  |  |
| cksixteen | 16 | CheckBoxField | — |  |  |
| ckseventeen | 17 | CheckBoxField | — |  |  |
| ckeighteen | 18 | CheckBoxField | — |  |  |
| cknineteen | 19 | CheckBoxField | — |  |  |
| cktwenty | 20 | CheckBoxField | — |  |  |
| cktwentyone | 21 | CheckBoxField | — |  |  |
| cktwentytwo | 22 | CheckBoxField | — |  |  |
| cktwentythree | 23 | CheckBoxField | — |  |  |
| cktwentyfour | 24 | CheckBoxField | — |  |  |
| cktwentyfive | 25 | CheckBoxField | — |  |  |
| cktwentysix | 26 | CheckBoxField | — |  |  |
| cktwentyseven | 27 | CheckBoxField | — |  |  |
| cktwentyeight | 28 | CheckBoxField | — |  |  |
| cktwentynine | 29 | CheckBoxField | — |  |  |
| ckthirty | 30 | CheckBoxField | — |  |  |
| ckthirtyone | 31 | CheckBoxField | — |  |  |
| cklastday | 最后一天 | CheckBoxField | — |  |  |
| comnobyweek | 第几个 | ComboField | — |  |  |
| comweekbyweek | 星期几 | ComboField | — |  |  |
| ckjan | 一月 | CheckBoxField | — |  |  |
| ckfeb | 二月 | CheckBoxField | — |  |  |
| ckmar | 三月 | CheckBoxField | — |  |  |
| ckapr | 四月 | CheckBoxField | — |  |  |
| ckmay | 五月 | CheckBoxField | — |  |  |
| ckjun | 六月 | CheckBoxField | — |  |  |
| ckjul | 七月 | CheckBoxField | — |  |  |
| ckaug | 八月 | CheckBoxField | — |  |  |
| cksep | 九月 | CheckBoxField | — |  |  |
| ckoct | 十月 | CheckBoxField | — |  |  |
| cknov | 十一月 | CheckBoxField | — |  |  |
| ckdec | 十二月 | CheckBoxField | — |  |  |
| comnobymonth | 第几个 | ComboField | — |  |  |
| comweekbymonth | 星期几 | ComboField | — |  |  |
| cksun | 星期日 | CheckBoxField | — |  |  |
| ckmon | 星期一 | CheckBoxField | — |  |  |
| cktues | 星期二 | CheckBoxField | — |  |  |
| ckwed | 星期三 | CheckBoxField | — |  |  |
| ckthur | 星期四 | CheckBoxField | — |  |  |
| ckfri | 星期五 | CheckBoxField | — |  |  |
| cksat | 星期六 | CheckBoxField | — |  |  |
| ckbyweek | 星期 | CheckBoxField | — |  |  |
| comno | 第几个 | ComboField | — |  |  |
| comweek | 星期几 | ComboField | — |  |  |
| ckhour_00 | 00 | CheckBoxField | — |  |  |
| ckhour_01 | 01 | CheckBoxField | — |  |  |
| ckhour_02 | 02 | CheckBoxField | — |  |  |
| ckhour_03 | 03 | CheckBoxField | — |  |  |
| ckhour_04 | 04 | CheckBoxField | — |  |  |
| ckhour_05 | 05 | CheckBoxField | — |  |  |
| ckhour_06 | 06 | CheckBoxField | — |  |  |
| ckhour_07 | 07 | CheckBoxField | — |  |  |
| ckhour_08 | 08 | CheckBoxField | — |  |  |
| ckhour_09 | 09 | CheckBoxField | — |  |  |
| ckhour_10 | 10 | CheckBoxField | — |  |  |
| ckhour_11 | 11 | CheckBoxField | — |  |  |
| ckhour_12 | 12 | CheckBoxField | — |  |  |
| ckhour_13 | 13 | CheckBoxField | — |  |  |
| ckhour_14 | 14 | CheckBoxField | — |  |  |
| ckhour_15 | 15 | CheckBoxField | — |  |  |
| ckhour_16 | 16 | CheckBoxField | — |  |  |
| ckhour_17 | 17 | CheckBoxField | — |  |  |
| ckhour_18 | 18 | CheckBoxField | — |  |  |
| ckhour_19 | 19 | CheckBoxField | — |  |  |
| ckhour_20 | 20 | CheckBoxField | — |  |  |
| ckhour_21 | 21 | CheckBoxField | — |  |  |
| ckhour_22 | 22 | CheckBoxField | — |  |  |
| ckhour_23 | 23 | CheckBoxField | — |  |  |
| txtdesc | 调度计划示例 | TextAreaField | — |  |  |
| useobj | 适用对象 | BasedataField | t_sitbs_schplancfg.fuseobjid | ✓ | sitbs_schuseobj |
| starttime | 生效时间 | DateTimeField | t_sitbs_schplancfg.fstarttime | ✓ |  |
| endtime | 失效时间 | DateTimeField | t_sitbs_schplancfg.fendtime | ✓ |  |
| schedule | 调度计划 | TextField | — |  |  |
| refstatus | 引用状态 | ComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_schplancfg（主表） | 15 |
| t_sitbs_schplancfg_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 94 |
