# hrcs_coordstrategylog — 协作处理策略日志表

**表单编码**: `hrcs_coordstrategylog`  
**表单ID**: `51YE3FK9CS21`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_coordstrategylog（协作处理策略日志表） [BaseEntity]

- **数据库表**: `t_hrcs_coordstrategylog`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| coordstrategy | 协作规则策略 | BasedataField | fcoordstrategyid |  | hrcs_coordstrategy |
| rulejson | 规则JSON | TextField | frulejson |  |  |
| basejson | 基本信息JSON | TextField | fbasejson |  |  |

