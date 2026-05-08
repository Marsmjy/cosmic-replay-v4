# hrcs_labelpolicyrule — 打标策略规则

**表单编码**: `hrcs_labelpolicyrule`  
**表单ID**: `2YNWH5H6DSTT`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_labelpolicyrule（打标策略规则） [BaseEntity]

- **数据库表**: `t_hrcs_labelpolicyrule`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| labelpolicy | 打标策略 | BasedataField | flabelpolicyid |  | hrcs_lblstrategy |
| label | 标签 | BasedataField | flabelid |  | hrcs_label |
| labelvalue | 标签值 | BasedataField | flabelvalueid |  | hrcs_labelvalue |
| conditions | 规则条件 | TextField | fconditions |  |  |
| brmrule | 规则引擎规则 | BasedataField | fbrmruleid |  | brm_ruledesign |

