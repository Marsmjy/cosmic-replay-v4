# haos_othroletpl — 其他形态组织-角色库

**表单编码**: `haos_othroletpl`  
**表单ID**: `5CO=QHBW/YH9`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_othroletpl（其他形态组织-角色库） [BaseEntity]

- **数据库表**: `t_haos_othroletpl`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| number | 编码 | TextField | fnumber | ✓ |  |
| index | 排序码 | IntegerField | findex |  |  |
| structtype | 所属架构类型 | BasedataField | fstructtypeid |  | haos_structtype |
| name | 名称 | MuliLangTextField | fname | ✓ |  |

