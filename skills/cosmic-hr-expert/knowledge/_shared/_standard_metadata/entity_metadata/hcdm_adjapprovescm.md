---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 0VO5EV13=I9W
app_number: hcdm
app_name: 薪酬管理
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcdm_adjapprovescm — 定调薪方案

**表单编码**: `hcdm_adjapprovescm`  
**表单ID**: `2827B3I02BPO`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_adjapprovescm（定调薪方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_adjapprovescm` | 主表 · 44 列 |
| `t_hcdm_adjapprovescm_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcdm_adjapprovescm.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcdm_adjapprovescm_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcdm_adjapprovescm.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcdm_adjapprovescm.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_adjapprovescm.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcdm_adjapprovescm.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_adjapprovescm.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_adjapprovescm.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcdm_adjapprovescm.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hcdm_adjapprovescm.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hcdm_adjapprovescm.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hcdm_adjapprovescm.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hcdm_adjapprovescm.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hcdm_adjapprovescm.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hcdm_adjapprovescm.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hcdm_adjapprovescm.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hcdm_adjapprovescm_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcdm_adjapprovescm_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcdm_adjapprovescm.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcdm_adjapprovescm.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcdm_adjapprovescm.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hcdm_adjapprovescm.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hcdm_adjapprovescm.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hcdm_adjapprovescm.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hcdm_adjapprovescm.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hcdm_adjapprovescm.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hcdm_adjapprovescm.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hcdm_adjapprovescm.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hcdm_adjapprovescm.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hcdm_adjapprovescm.fhisversion |  |  |
| adjscmbsed | 生效日期 | DateField | t_hcdm_adjapprovescm.fadjscmbsed | ✓ |  |
| isapprwf | 启用审批 | CheckBoxField | t_hcdm_adjapprovescm.fisapprwf |  |  |
| apprwf | 调薪审批流 | BasedataField | — |  | wf_model |
| enableconfirm | 启用定调薪确认 | CheckBoxField | t_hcdm_adjapprovescm.fenableconfirm |  |  |
| adjconfirmtpl | 调薪确认模板 | BasedataField | t_hcdm_adjapprovescm.fadjconfirmtplid |  | hcdm_adjconfirmtpl |
| passwordtype | 定调薪确认登录使用密码 | ComboField | t_hcdm_adjapprovescm.fpasswordtype |  |  |
| country | 国家/地区 | BasedataField | t_hcdm_adjapprovescm.fcountryid | ✓ | bd_country |
| transcondition | 定调薪确认生效时机 | MulComboField | t_hcdm_adjapprovescm.ftranscondition |  |  |
| datasyncondition | 定调薪数据生效时机 | ComboField | t_hcdm_adjapprovescm.fdatasyncondition | ✓ |  |
| decconfirmtpl | 定薪确认模板 | BasedataField | t_hcdm_adjapprovescm.fdecconfirmtplid |  | hcdm_adjconfirmtpl |
| apprtype | 审批方式 | MulComboField | t_hcdm_adjapprovescm.fapprtype | ✓ |  |
| confirmapplyscope | 定调薪确认应用于 | ComboField | t_hcdm_adjapprovescm.fconfirmapplyscope |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_adjapprovescm（主表） | 38 |
| t_hcdm_adjapprovescm_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
