# hbss_supplier — 供应商

**表单编码**: `hbss_supplier`  
**表单ID**: `0=6TE0O3GFAU`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_supplier（供应商） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbss_supplier` | BaseEntity | 主表 |
| `t_hbss_suppllinkman` | EntryEntity | 单据体 |
| `t_hbss_supserviscope` | MulEmployeeField子表 | 服务地理范围 |
| `t_hbss_servicertypeifield` | MulEmployeeField子表 | 服务类型 |

### 字段列表 — t_hbss_supplier（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbss_supplier.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbss_supplier.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbss_supplier.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbss_supplier.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbss_supplier.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbss_supplier.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbss_supplier.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbss_supplier.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbss_supplier.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hbss_supplier.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbss_supplier.fdescription |  |  |
| index | 排序号 | IntegerField | t_hbss_supplier.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbss_supplier.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbss_supplier.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbss_supplier.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbss_supplier.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbss_supplier.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbss_supplier.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbss_supplier.foriname |  |  |
| picturefield | 图片 | PictureField | t_hbss_supplier.fpicturefield |  |  |
| syssupplier | 对应系统供应商 | BasedataField | t_hbss_supplier.fsyssupplierid |  | bd_bizpartner |
| supplierlevel | 供应商等级 | BasedataField | t_hbss_supplier.fsupplierlevelid | ✓ | hbss_supplierlevel |
| sereffectdate | 服务生效日期 | DateField | t_hbss_supplier.fsereffectdate | ✓ |  |
| serlosedate | 服务失效日期 | DateField | t_hbss_supplier.fserlosedate |  |  |
| servicestatus | 服务状态 | ComboField | t_hbss_supplier.fservicestatus | ✓ |  |
| isblacklist | 黑名单 | CheckBoxField | t_hbss_supplier.fisblacklist |  |  |
| blacklistevent | 黑名单事件 | TextAreaField | t_hbss_supplier.fblacklistevent |  |  |
| mainproduct | 主要产品及服务 | TextAreaField | t_hbss_supplier.fmainproduct |  |  |
| supserviscope | 服务地理范围 | MulBasedataField | t_hbss_supserviscope（子表） |  |  |
| daterange | 服务有效期 | DateRangeField | — |  |  |
| servicetype | 服务类型 | MulBasedataField | t_hbss_servicertypeifield（子表） | ✓ |  |
| entryentity | 单据体 | EntryEntity | → t_hbss_suppllinkman |  |  |

### 字段列表 — t_hbss_suppllinkman（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| contactname | 姓名 | MuliLangTextField | t_hbss_suppllinkman.fcontactname | ✓ |  |
| duty | 职务 | MuliLangTextField | t_hbss_suppllinkman.fduty |  |  |
| landline | 座机号码 | TextField | t_hbss_suppllinkman.flandline |  |  |
| email | 电子邮箱 | TextField | t_hbss_suppllinkman.femail |  |  |
| issyscontact | 默认联系人 | CheckBoxField | t_hbss_suppllinkman.fissyscontact | ✓ |  |
| mobile | 手机号码 | TelephoneField | t_hbss_suppllinkman.fmobile | ✓ |  |
| phone | 座机号码 | TextField | t_hbss_suppllinkman.fphone |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_supplier（主表） | 31 |
| t_hbss_suppllinkman（单据体） | 7 |
| t_hbss_supserviscope（MulEmployeeField子表） | 1 |
| t_hbss_servicertypeifield（MulEmployeeField子表） | 1 |
| 无数据库列 | 1 |

