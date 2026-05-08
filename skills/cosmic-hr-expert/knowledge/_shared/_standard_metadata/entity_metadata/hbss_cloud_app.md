# hbss_cloud_app — HR云与应用

**表单编码**: `hbss_cloud_app`  
**表单ID**: `2GZN386W4NN9`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_cloud_app（HR云与应用） [BaseEntity]

- **数据库表**: `t_hbss_cloudapp`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| app | 应用 | BasedataField | fappid | ✓ | hbp_devportal_bizapp |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| appname | 应用名称 | MuliLangTextField | fappname |  |  |

