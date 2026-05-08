# hcf_rsmhobby — 拟入职人员特长及爱好

**表单编码**: `hcf_rsmhobby`  
**表单ID**: `1OP2H/PFIXU5`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_rsmhobby（拟入职人员特长及爱好） [BaseEntity]

- **数据库表**: `t_hcf_rsmhobby`  

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
| interest | 爱好 | MuliLangTextField | finterest |  |  |
| hobby | 特长 | MuliLangTextField | fhobby |  |  |
| letter | 评级/证书 | MuliLangTextField | fletter |  |  |

