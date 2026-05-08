---
source: openapi_runtime
extracted_at: 2026-04-25
extractor: build_standard_metadata_md_from_openapi.py
app_id: 217WYC/L9U7E
app_number: homs
app_name: 组织管理
cloud_number: ODC
cloud_name: 组织发展云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# homs_orgbatchchgbill — 组织调整申请

**表单编码**: `homs_orgbatchchgbill`  
**表单ID**: `2=4500XUBSRK`  
**归属**: 组织发展云 / 组织管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: homs_orgbatchchgbill（组织调整申请） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_homs_orgchgbill` | 主表 · 22 列 |
| `t_homs_orgchgbillentry` | 分录表 · 20 列 |
| `t_homs_orgchgbill_l` | 多语言表 · 3 列 |
| `t_homs_orgchgbillentry_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_homs_orgchgbill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_homs_orgchgbill.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_homs_orgchgbill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_homs_orgchgbill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_homs_orgchgbill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_homs_orgchgbill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_homs_orgchgbill.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_homs_orgchgbill.fcreatetime |  |  |
| org | 组织体系管理组织 | OrgField | t_homs_orgchgbill.forgid | ✓ | bos_org |
| barcode | 条形码 | TextField | — |  |  |
| inputdevicetype | 输入设备 | TextField | — |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_homs_orgchgbill.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_homs_orgchgbill.fauditstatus | ✓ |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | t_homs_orgchgbill.feventeffectdate |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_homs_orgchgbill.fissubmit |  |  |

## 实体: entryentity_add（单据体-新增组织） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| add_adminorgtype | 行政组织类型 | BasedataField | — | ✓ | haos_adminorgtype |
| add_corporateorg | 法律实体 | BasedataField | — |  | hbss_lawentity |
| add_changescene | 变动场景 | BasedataField | — | ✓ | haos_changescene |
| add_number | 行政组织编码 | TextField | — |  |  |
| add_name | 行政组织名称 | MuliLangTextField | — | ✓ |  |
| add_simplename | 简称 | MuliLangTextField | — |  |  |
| add_parentorg | 上级行政组织 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| add_establishmentdate | 成立日期 | DateField | — |  |  |
| add_adminorglayer | 管理层级 | BasedataField | — |  | haos_adminorglayer |
| add_adminorgfunction | 行政组织职能 | BasedataField | — |  | haos_adminorgfunction |
| add_companyarea | 国家/地区 | BasedataField | — |  | bd_country |
| add_city | 所在城市 | BasedataField | — |  | bd_admindivision |
| add_workplace | 工作地 | BasedataField | — |  | hbss_workplace |
| add_parentorg_name | 上级组织全称 | MuliLangTextField | — |  |  |
| add_adminorg | 行政组织 | BigIntField | — |  |  |
| add_changereason | 变动原因 | BasedataField | — |  | haos_orgchangereason |
| add_changetype | 变动类型 | BasedataField | — |  | haos_orgchangetype |
| add_org | 组织体系管理组织 | OrgField | — | ✓ | bos_org |
| add_changedescription | 变动说明 | TextField | — |  |  |
| add_industrytype | 行业类别 | BasedataField | — |  | hbss_industrytype |
| add_detailaddress | 详细地址 | MuliLangTextField | — |  |  |
| add_description | 描述 | MuliLangTextField | — |  |  |
| add_index | 排序号 | IntegerField | — |  |  |
| add_positioning | 定位 | MuliLangTextField | — |  |  |
| add_mainduty | 主要职责 | MuliLangTextField | — |  |  |

## 实体: entryentity_parent（单据体-调整上级） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| parent_simplename | 简称 | MuliLangTextField | — |  |  |
| parent_parentorg | 调整后上级行政组织 | BasedataField | — | ✓ | haos_adminorgbatch |
| parent_establishmentdate | 成立日期 | DateField | — |  |  |
| parent_tobedisableflag | 待停用 | CheckBoxField | — |  |  |
| parent_changescene | 变动场景 | BasedataField | — | ✓ | haos_changescene |
| parent_adminorglayer | 管理层级 | BasedataField | — |  | haos_adminorglayer |
| parent_adminorgfunction | 行政组织职能 | BasedataField | — |  | haos_adminorgfunction |
| parent_companyarea | 国家/地区 | BasedataField | — |  | bd_country |
| parent_city | 所在城市 | BasedataField | — |  | bd_admindivision |
| parent_name | 调整后行政组织名称 | MuliLangTextField | — | ✓ |  |
| parent_changereason | 变动原因 | BasedataField | — |  | haos_orgchangereason |
| parent_changetype | 变动类型 | BasedataField | — |  | haos_orgchangetype |
| parent_workplace | 工作地 | BasedataField | — |  | hbss_workplace |
| parent_parentorg_name | 调整后上级行政组织全称 | MuliLangTextField | — |  |  |
| parent_oriparentorg_name | 原上级行政组织全称 | TextAreaField | — |  |  |
| parent_number | 调整后行政组织编码 | TextField | — | ✓ |  |
| parent_oriparentorg | 原上级行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| parent_adminorg | 行政组织 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| parent_adminorgtype | 行政组织类型 | BasedataField | — | ✓ | haos_adminorgtype |
| parent_corporateorg | 法律实体 | BasedataField | — |  | hbss_lawentity |
| parent_org | 组织体系管理组织 | OrgField | — | ✓ | bos_org |
| parent_changedescription | 变动说明 | TextField | — |  |  |
| parent_industrytype | 行业类别 | BasedataField | — |  | hbss_industrytype |
| parent_detailaddress | 详细地址 | MuliLangTextField | — |  |  |
| parent_description | 描述 | MuliLangTextField | — |  |  |
| parent_index | 排序号 | IntegerField | — |  |  |
| parent_positioning | 定位 | MuliLangTextField | — |  |  |
| parent_mainduty | 主要职责 | MuliLangTextField | — |  |  |

## 实体: entryentity_info（单据体-变更信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| info_adminorg | 行政组织 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| info_name | 调整后行政组织名称 | MuliLangTextField | — | ✓ |  |
| info_simplename | 简称 | MuliLangTextField | — |  |  |
| info_number | 调整后行政组织编码 | TextField | — | ✓ |  |
| info_establishmentdate | 成立日期 | DateField | — |  |  |
| info_tobedisableflag | 待停用 | CheckBoxField | — |  |  |
| info_changescene | 变动场景 | BasedataField | — | ✓ | haos_changescene |
| info_adminorglayer | 管理层级 | BasedataField | — |  | haos_adminorglayer |
| info_adminorgfunction | 行政组织职能 | BasedataField | — |  | haos_adminorgfunction |
| info_companyarea | 国家/地区 | BasedataField | — |  | bd_country |
| info_city | 所在城市 | BasedataField | — |  | bd_admindivision |
| info_workplace | 工作地 | BasedataField | — |  | hbss_workplace |
| info_changetype | 变动类型 | BasedataField | — | ✓ | haos_orgchangetype |
| info_changereason | 变动原因 | BasedataField | — |  | haos_orgchangereason |
| info_oriparentorg_name | 上级行政组织全称 | TextAreaField | — |  |  |
| info_parentorg | 上级行政组织 | BasedataField | — |  | haos_adminorgbatch |
| info_corporateorg | 法律实体 | BasedataField | — |  | hbss_lawentity |
| info_adminorgtype | 行政组织类型 | BasedataField | — | ✓ | haos_adminorgtype |
| info_org | 组织体系管理组织 | OrgField | — | ✓ | bos_org |
| info_changedescription | 变动说明 | TextField | — |  |  |
| info_industrytype | 行业类别 | BasedataField | — |  | hbss_industrytype |
| info_detailaddress | 详细地址 | MuliLangTextField | — |  |  |
| info_description | 描述 | MuliLangTextField | — |  |  |
| info_index | 排序号 | IntegerField | — |  |  |
| info_positioning | 定位 | MuliLangTextField | — |  |  |
| info_mainduty | 主要职责 | MuliLangTextField | — |  |  |

## 实体: entryentity_disable（单据体-停用组织） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| disable_adminorg | 行政组织 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| disable_adminorgtype | 行政组织类型 | BasedataField | — |  | haos_adminorgtype |
| disable_changereason | 变动原因 | BasedataField | — |  | haos_orgchangereason |
| disable_changescene | 变动场景 | BasedataField | — | ✓ | haos_changescene |
| disable_tobedisableflag | 待停用 | CheckBoxField | — |  |  |
| disable_changetype | 变动类型 | BasedataField | — | ✓ | haos_orgchangetype |
| disable_oriparentorg_name | 上级行政组织全称 | TextAreaField | — |  |  |
| disable_parentorg | 上级行政组织 | BasedataField | — |  | haos_adminorgbatch |
| disable_org | 组织体系管理组织 | OrgField | — | ✓ | bos_org |
| disable_changedescription | 变动说明 | TextField | — |  |  |
| istodiagram | 是否查看组织架构图 | CheckBoxField | t_homs_orgchgbill.fistodiagram |  |  |
| dispatchnumber | 发文编号 | TextField | t_homs_orgchgbill.fdispatchnumber |  |  |
| effdt | 组织调整生效日期 | DateField | t_homs_orgchgbill.feffdt | ✓ |  |
| description | 描述 | MuliLangTextField | t_homs_orgchgbill_l.fdescription |  |  |
| disorg | 签发组织 | HRMulAdminOrgField | — |  |  |
| dispatchname | 发文名称 | MuliLangTextField | t_homs_orgchgbill_l.fdispatchname |  |  |

## 实体: entryentity_all（单据体-所有分录数据） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| adminorgtype | 行政组织类型 | BasedataField | t_homs_orgchgbillentry.fadminorgtypeid |  | haos_adminorgtype |
| changetype | 变动类型 | BasedataField | t_homs_orgchgbillentry.fchangetypeid |  | haos_orgchangetype |
| number | 行政组织编码 | TextField | t_homs_orgchgbillentry.fnumber |  |  |
| name | 行政组织名称 | MuliLangTextField | t_homs_orgchgbillentry_l.fname |  |  |
| simplename | 简称 | MuliLangTextField | t_homs_orgchgbillentry_l.fsimplename |  |  |
| corporateorg | 法律实体 | BasedataField | t_homs_orgchgbillentry.fcorporateorgid |  | hbss_lawentity |
| changescene | 变动场景 | BasedataField | t_homs_orgchgbillentry.fchangesceneid |  | haos_changescene |
| parentorg | 上级行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| establishmentdate | 成立日期 | DateField | t_homs_orgchgbillentry.festablishmentdate |  |  |
| adminorglayer | 管理层级 | BasedataField | t_homs_orgchgbillentry.fadminorglayerid |  | haos_adminorglayer |
| adminorgfunction | 行政组织职能 | BasedataField | t_homs_orgchgbillentry.fadminorgfunctionid |  | haos_adminorgfunction |
| companyarea | 国家/地区 | BasedataField | t_homs_orgchgbillentry.fcompanyareaid |  | bd_country |
| city | 所在城市 | BasedataField | t_homs_orgchgbillentry.fcityid |  | bd_admindivision |
| workplace | 工作地 | BasedataField | t_homs_orgchgbillentry.fworkplaceid |  | hbss_workplace |
| changereason | 变动原因 | BasedataField | t_homs_orgchgbillentry.fchangereasonid |  | haos_orgchangereason |
| oriparentorg | 原上级行政组织 | HRAdminOrgField | t_homs_orgchgbillentry.foriparentorgid |  | haos_adminorghrf7 |
| adminorgid | 行政组织 | HRAdminOrgField | t_homs_orgchgbillentry.fadminorgid |  | haos_adminorghrf7 |
| changedescription | 变动说明 | TextField | t_homs_orgchgbillentry.fchangedescription |  |  |
| tobedisableflag | 待停用 | CheckBoxField | t_homs_orgchgbillentry.ftobedisableflag |  |  |
| billtype | 单据类型 | TextField | t_homs_orgchgbill.fbilltype |  |  |
| addcount | 新增组织 | TextField | — |  |  |
| parentcount | 调整上级 | TextField | — |  |  |
| infocount | 变更信息 | TextField | — |  |  |
| disablecount | 停用组织 | TextField | — |  |  |
| display_chgorg | 仅显示调整组织 | CheckBoxField | — |  |  |
| mergecount | 合并组织 | TextField | — |  |  |
| splitcount | 拆分组织 | TextField | — |  |  |

## 实体: entryentity_merge（单据体-合并组织） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| to_merge_org | 合并前组织 | HRMulAdminOrgField | — | ✓ |  |
| merge_target_org | 合并后组织 | BasedataField | — | ✓ | haos_adminorgbatch |
| merge_changescene | 变动场景 | BasedataField | — | ✓ | haos_changescene |
| merge_changereason | 变动原因 | BasedataField | — |  | haos_orgchangereason |
| merge_origin_id | 合并组织ID | BigIntField | — |  |  |
| merge_changetype | 变动类型 | BasedataField | — | ✓ | haos_orgchangetype |
| merge_changedescription | 变动说明 | TextField | — |  |  |

## 实体: entryentity_split（单据体-拆分组织） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| to_split_org | 拆分前组织 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| split_target_org | 拆分后组织 | MulBasedataField | — | ✓ |  |
| split_changescene | 变动场景 | BasedataField | — | ✓ | haos_changescene |
| split_changereason | 变动原因 | BasedataField | — |  | haos_orgchangereason |
| split_origin_id | 拆分组织ID | BigIntField | — |  |  |
| split_changetype | 变动类型 | BasedataField | — | ✓ | haos_orgchangetype |
| split_changedescription | 变动说明 | TextField | — |  |  |
| terminationdetail | 终止说明 | MuliLangTextField | t_homs_orgchgbill_l.fterminationdetail |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_homs_orgchgbill（主表） | 17 |
| t_homs_orgchgbill_l | 3 |
| t_homs_orgchgbillentry | 16 |
| t_homs_orgchgbillentry_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 114 |
