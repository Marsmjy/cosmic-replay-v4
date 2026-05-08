# haos_othorgstruct — 其他形态-组织结构

**表单编码**: `haos_othorgstruct`  
**表单ID**: `5COC9SL+BXKM`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_othorgstruct（其他形态-组织结构） [BaseEntity]

- **数据库表**: `t_haos_adminstruct`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| isdeleted | 是否已删除 | CheckBoxField | — |  |  |
| iscurrentdata | 是否当前数据 | CheckBoxField | fiscurrentdata |  |  |
| startdate | 开始日期 | DateField | fstartdate | ✓ |  |
| enddate | 结束日期 | DateField | fenddate | ✓ |  |
| level | 级次 | IntegerField | flevel |  |  |
| isleaf | 是否叶子 | CheckBoxField | fisleaf |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| adminorg | 组织 | HRAdminOrgField | fadminorgid |  | haos_adminorghrf7 |
| structlongnumber | 组织上下级结构长编码 | TextField | fstructlongnumber |  |  |
| parentorg | 上级组织 | HRAdminOrgField | fparentid |  | haos_adminorghrf7 |
| structproject | 架构方案 | BasedataField | fstructprojectid |  | haos_structure |
| isroot | 是否根节点 | CheckBoxField | fisroot |  |  |
| otclassify | 组织分类 | BasedataField | fotclassifyid |  | haos_otclassify |
| sortcode | 排序码 | TextField | fsortcode |  |  |

