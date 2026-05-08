# hrcs_presetchange — 预置数据变动登记表

**表单编码**: `hrcs_presetchange`  
**表单ID**: `3YAV5DD47X9H`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_presetchange（预置数据变动登记表） [BaseEntity]

- **数据库表**: `t_hrcs_presetchange`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| basedata | 基础资料编码 | TextField | fbasedata |  |  |
| bdid | 基础资料数据ID | BigIntField | fbdid |  |  |
| ischange | 是否改动过 | TextField | fischange |  |  |
| basedataname | 基础资料名称 | TextField | fbasedataname |  |  |
| bdnumber | 基础资料数据编码 | TextField | fbdnumber |  |  |
| bdname | 基础资料数据名称 | TextField | fbdname |  |  |

