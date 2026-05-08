# hrcs_rolegrp — 角色分组

**表单编码**: `hrcs_rolegrp`  
**表单ID**: `0SXB7BI4DEH4`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_rolegrp（角色分组） [BaseEntity]

- **数据库表**: `t_hbss_permrolegrp`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | fname | ✓ |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| disabler | 禁用人 | UserField | fdisablerid |  | bos_user |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | fdisabledate |  |  |
| level | 级次 | IntegerField | — |  |  |
| fullname | 全称 | MuliLangTextField | — |  |  |
| isleaf | 是否叶子 | CheckBoxField | — |  |  |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| longnumber | 长编码 | TextField | — |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |
| parent | 上级角色分组 | BasedataField | — |  | perm_rolegroup |

