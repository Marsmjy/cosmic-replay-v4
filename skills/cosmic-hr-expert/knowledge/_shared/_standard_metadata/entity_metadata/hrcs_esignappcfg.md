# hrcs_esignappcfg — 应用配置

**表单编码**: `hrcs_esignappcfg`  
**表单ID**: `3ECU5A2RW1RT`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_esignappcfg（应用配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_esignappcfg` | BaseEntity | 主表 |
| `t_hrcs_esignappcfgentry` | EntryEntity | 自定义参数 |

### 字段列表 — t_hrcs_esignappcfg（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_esignappcfg.fnumber |  |  |
| name | 应用名称 | MuliLangTextField | t_hrcs_esignappcfg.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_esignappcfg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_esignappcfg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_esignappcfg.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_esignappcfg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_esignappcfg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_esignappcfg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_esignappcfg.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_esignappcfg.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_esignappcfg.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_esignappcfg.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_esignappcfg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_esignappcfg.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_esignappcfg.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_esignappcfg.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_esignappcfg.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_esignappcfg.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_esignappcfg.foriname |  |  |
| corporate | 应用所属企业 | BasedataField | t_hrcs_esignappcfg.fcorporate | ✓ | hbss_lawentity |
| appid | AppID | TextField | t_hrcs_esignappcfg.fappid | ✓ |  |
| appsecret | AppSecret | TextField | t_hrcs_esignappcfg.fappsecret | ✓ |  |
| serverurl | ServerUrl | TextField | t_hrcs_esignappcfg.fserverurl | ✓ |  |
| esignsp | 电子签服务商 | BasedataField | t_hrcs_esignappcfg.fesignsp |  | hrcs_esignspmgr |
| temprelspid | 临时厂商ID | TextField | t_hrcs_esignappcfg.ftempspid |  |  |
| thirdcorpid | 第三方企业ID | TextField | t_hrcs_esignappcfg.fthirdcorpid | ✓ |  |
| appsecreten | 加密appsecret | TextField | t_hrcs_esignappcfg.fappsecreten |  |  |
| entryentity | 自定义参数 | EntryEntity | → t_hrcs_esignappcfgentry |  |  |

### 字段列表 — t_hrcs_esignappcfgentry（自定义参数·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| key | key | TextField | t_hrcs_esignappcfgentry.fkey | ✓ |  |
| desc | 描述 | MuliLangTextField | t_hrcs_esignappcfgentry.fdesc |  |  |
| value | value | TextField | t_hrcs_esignappcfgentry.fvalue | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_esignappcfg（主表） | 27 |
| t_hrcs_esignappcfgentry（自定义参数） | 3 |

