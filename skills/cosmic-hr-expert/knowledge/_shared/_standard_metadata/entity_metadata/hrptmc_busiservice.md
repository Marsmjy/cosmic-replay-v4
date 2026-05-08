# hrptmc_busiservice — 业务服务

**表单编码**: `hrptmc_busiservice`  
**表单ID**: `2WJCH56/Z46X`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_busiservice（业务服务） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_busiservice` | BaseEntity | 主表 |
| `t_hrptmc_busisrvparam` | EntryEntity | 参数信息 |

### 字段列表 — t_hrptmc_busiservice（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 服务编码 | TextField | t_hrptmc_busiservice.fnumber |  |  |
| name | 服务名称 | MuliLangTextField | t_hrptmc_busiservice.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrptmc_busiservice.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrptmc_busiservice.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrptmc_busiservice.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrptmc_busiservice.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrptmc_busiservice.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_busiservice.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrptmc_busiservice.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrptmc_busiservice.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrptmc_busiservice.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrptmc_busiservice.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrptmc_busiservice.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrptmc_busiservice.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrptmc_busiservice.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_busiservice.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrptmc_busiservice.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrptmc_busiservice.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrptmc_busiservice.foriname |  |  |
| app | 所属应用 | BasedataField | t_hrptmc_busiservice.fappid | ✓ | hbp_devportal_bizapp |
| classfullname | 全路径类名 | TextField | t_hrptmc_busiservice.fclassfullname | ✓ |  |
| methodname | 方法名称 | TextField | t_hrptmc_busiservice.fmethodname | ✓ |  |
| entryentity | 参数信息 | EntryEntity | → t_hrptmc_busisrvparam |  |  |

### 字段列表 — t_hrptmc_busisrvparam（参数信息·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| paramnumber | 参数编码 | TextField | t_hrptmc_busisrvparam.fnumber |  |  |
| paramname | 参数名称 | MuliLangTextField | t_hrptmc_busisrvparam.fname |  |  |
| paramtype | 参数类型 | ComboField | t_hrptmc_busisrvparam.ftype |  |  |
| isrequired | 是否必传 | CheckBoxField | t_hrptmc_busisrvparam.fisrequired |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_busiservice（主表） | 22 |
| t_hrptmc_busisrvparam（参数信息） | 4 |

