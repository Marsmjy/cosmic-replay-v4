# hrptmc_publishmenu — 报表发布菜单

**表单编码**: `hrptmc_publishmenu`  
**表单ID**: `2XYX+9H6VAO0`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_publishmenu（报表发布菜单） [BaseEntity]

- **数据库表**: `t_hrptmc_publishmenu`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| menu | 菜单id | TextField | fmenuid |  |  |
| menuapp | 菜单所在应用id | TextField | fmenuappid |  |  |
| reportmanage | 报表管理 | BasedataField | freportmanageid |  | hrptmc_reportmanage |
| appsrc | 发布应用来源 | ComboField | fappsrc |  |  |

