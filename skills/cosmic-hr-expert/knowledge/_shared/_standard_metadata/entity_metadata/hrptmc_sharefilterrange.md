# hrptmc_sharefilterrange — 分享报表筛选器选择范围

**表单编码**: `hrptmc_sharefilterrange`  
**表单ID**: `48PB=T9ETJ93`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_sharefilterrange（分享报表筛选器选择范围） [BaseEntity]

- **数据库表**: `t_hrptmc_sharefilterrange`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| sharerecord | 分享记录 | BasedataField | fsharerecord |  | hrptmc_sharerecord |
| fieldalias | 筛选器字段别名 | TextField | ffieldalias |  |  |
| filtertype | 筛选器字段类型 | ComboField | ffiltertype |  |  |
| filterrange | 筛选器可选范围 | TextField | ffilterrange |  |  |
| filterdefault | 筛选器默认值 | TextField | ffilterdefault |  |  |
| startdatedefault | 日期筛选器默认开始时间 | TextField | fstartdatedefault |  |  |
| enddatedefault | 日期筛选器默认结束时间 | TextField | fenddatedefault |  |  |
| datefiltertype | 日期筛选器默认值类型 | ComboField | fdatefiltertype |  |  |
| datefiltertext | 日期文本默认值 | TextField | fdatefiltertext |  |  |
| opt | 数值筛选器比较符 | ComboField | fopt |  |  |
| userdissche | 用户显示方案 | BasedataField | fuserdisscheid |  | hrptmc_userdispscm |
| containsub | 行政组织包含下级 | CheckBoxField | fcontainsub |  |  |
| adminorglevel | 行政组织层级 | TextField | fadminorglevel |  |  |

