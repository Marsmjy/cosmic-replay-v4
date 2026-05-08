# hbss_hrbu_hitfrequency — HR管理组织命中次数

**表单编码**: `hbss_hrbu_hitfrequency`  
**表单ID**: `4VD4=ZFDO69H`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_hrbu_hitfrequency（HR管理组织命中次数） [BaseEntity]

- **数据库表**: `t_hbss_hrbu_hitfrequency`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| entitynumber | 实体编码 | TextField | fentitynumber |  |  |
| orgfields | 组织字段 | TextField | forgfields |  |  |
| count | 命中次数 | IntegerField | fcount |  |  |
| viewid | 视图id | BigIntField | fviewid |  |  |

