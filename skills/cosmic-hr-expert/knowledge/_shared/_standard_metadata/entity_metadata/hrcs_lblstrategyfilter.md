# hrcs_lblstrategyfilter — 打标策略打标范围

**表单编码**: `hrcs_lblstrategyfilter`  
**表单ID**: `2WNBCF0HEYDH`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_lblstrategyfilter（打标策略打标范围） [BaseEntity]

- **数据库表**: `t_hrcs_lblpolicyfilter`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| lblstrategy | 打标策略 | BasedataField | flblpolicyid |  | hrcs_lblstrategy |
| value | 范围值 | TextField | fvalue |  |  |
| lblfactor | 因子 | BasedataField | flblfactorid |  | hrcs_lblobjectfield |
| fieldkey | 圈定字段标识（实体+字段编码） | TextField | ffieldkey |  |  |
| hasfilter | 不限 | CheckBoxField | fhasfilter |  |  |

