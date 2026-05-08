# hrss_syncparam — 数据同步参数

**表单编码**: `hrss_syncparam`  
**表单ID**: `44V0UQ40NZ2L`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_syncparam（数据同步参数） [BaseEntity]

- **数据库表**: `t_hrss_syncparam`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| relloadsize | 单表最大查询数 | IntegerField | frelloadsize | ✓ |  |
| joinloadsize | 关联最大查询数 | IntegerField | fjoinloadsize | ✓ |  |
| contextsplitsize | 拆分关联实体阈值 | IntegerField | fcontextsplitsize | ✓ |  |
| searchobj | 搜索对象 | BasedataField | fsearchobj | ✓ | hrss_searchobject |

