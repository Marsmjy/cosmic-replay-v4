# hrss_essyntimerecord — es同步时间记录

**表单编码**: `hrss_essyntimerecord`  
**表单ID**: `3YZ3S4BJFSW+`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_essyntimerecord（es同步时间记录） [BaseEntity]

- **数据库表**: `t_hrss_essyntimerecord`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| searchobjid | 搜索对象id | BigIntField | fsearchobjid |  |  |
| entitynumber | 实体编码 | TextField | fentitynumber |  |  |
| lastupdatetime | 上次更新时间 | DateTimeField | flastupdatetime |  |  |

