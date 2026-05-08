# hrcs_userpermfile — 用户权限档案

**表单编码**: `hrcs_userpermfile`  
**表单ID**: `0SFXP64OI6BW`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_userpermfile（用户权限档案） [BaseEntity]

- **数据库表**: `t_hbss_permfiles`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| permfilegrpmember | 档案组成员 | BasedataField | — |  | hrcs_permfilegrpmember |
| org | HR管理组织 | OrgField | forgid | ✓ | bos_org |
| user | 用户 | UserField | fuserid | ✓ | bos_user |
| permfileenable | 档案状态 | ComboField | fenable | ✓ |  |
| department | 主职部门 | OrgField | — |  | bos_org |
| importflag | 导入标识 | TextField | — |  |  |
| permfilegrp | 用户组 | BasedataField | — |  | hrcs_permfilegrp |
| position | 主职职位 | TextField | — |  |  |

