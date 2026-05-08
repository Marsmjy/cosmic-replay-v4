# hrss_userlastsearchkey — 用户最近搜索关键词

**表单编码**: `hrss_userlastsearchkey`  
**表单ID**: `40HN1F5YPQ/7`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_userlastsearchkey（用户最近搜索关键词） [BaseEntity]

- **数据库表**: `t_hrss_userlastsearchkey`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| user | 用户 | UserField | fuserid |  | bos_user |
| searchtime | 搜索时间 | DateTimeField | fsearchtime |  |  |
| searchscene | 搜索场景 | BasedataField | fsearchsceneid |  | hrss_searchscene |
| searchpage | 搜索页面 | BasedataField | fsearchpageid |  | hrss_searchconfig |
| searchkey | 搜索关键词 | TextField | fsearchkey |  |  |

