# hrss_essyncscheme — ES同步任务管理

**表单编码**: `hrss_essyncscheme`  
**表单ID**: `3VG8LIFIMMIU`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_essyncscheme（ES同步任务管理） [BaseEntity]

- **数据库表**: `t_hrss_essyncscheme`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| searchobj | 搜索对象 | BasedataField | fsearchobjid |  | hrss_searchobject |
| essynstatus | 执行状态 | ComboField | fessynstatus |  |  |
| isinit | 是否完成初始化 | TextField | fisinit |  |  |

