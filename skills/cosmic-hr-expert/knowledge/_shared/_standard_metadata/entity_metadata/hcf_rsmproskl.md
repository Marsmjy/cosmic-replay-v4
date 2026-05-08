# hcf_rsmproskl — 拟入职人员专业技能

**表单编码**: `hcf_rsmproskl`  
**表单ID**: `1OPJT6M04N9=`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_rsmproskl（拟入职人员专业技能） [BaseEntity]

- **数据库表**: `t_hcf_rsmproskl`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| candidate | 拟入职人员 | BasedataField | fcandidateid |  | hcf_candidate |
| name | 技能名称 | MuliLangTextField | fname |  |  |
| familiarityid | 掌握程度 | BasedataField | familiarityid |  | hbss_familiarity |
| duration | 掌握时长（月） | DecimalField | fduration |  |  |

