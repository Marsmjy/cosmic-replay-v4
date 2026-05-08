# hrptmc_anobjsidebar — 分析对象数据加工侧边栏

**表单编码**: `hrptmc_anobjsidebar`  
**表单ID**: `3NFM956HKGKZ`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_anobjsidebar（分析对象数据加工侧边栏） [BaseEntity]

- **数据库表**: `t_hrptmc_anobjsidebar`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| type | 侧边栏类型 | ComboField | ftype |  |  |
| index | 顺序号 | IntegerField | findex |  |  |
| anobj | 分析对象 | BasedataField | fanobjid |  | hrptmc_analyseobject |
| groupfield | 分组赋值字段 | BasedataField | fgroupfield |  | hrptmc_anobjgroupfield |

