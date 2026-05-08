# hrptmc_permrule — 分析对象数据控权规则

**表单编码**: `hrptmc_permrule`  
**表单ID**: `3MRLCON6Z2G3`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_permrule（分析对象数据控权规则） [BaseEntity]

- **数据库表**: `t_hrptmc_permrule`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| anobj | 分析对象 | BasedataField | fanobjid |  | hrptmc_analyseobject |
| anobjfield | 分析对象字段 | BasedataField | fanobjfieldid |  | hrptmc_anobjqueryfield |
| permobj | 参照控权对象 | BasedataField | fpermobjid |  | hbp_entityobject |
| permfield | 参照控权字段 | TextField | fpermfield |  |  |
| index | 排序号 | IntegerField | findex |  |  |

