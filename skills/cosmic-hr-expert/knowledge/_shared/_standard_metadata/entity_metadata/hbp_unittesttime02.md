# hbp_unittesttime02 — 时间轴单测（可间断不重叠）

**表单编码**: `hbp_unittesttime02`  
**表单ID**: `50URVQ7CB=LM`  
**归属**: HR基础服务云 / HR基础平台  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbp_unittesttime02（时间轴单测（可间断不重叠）） [BaseEntity]

- **数据库表**: `t_hbp_unittesttime02`  

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
| number | 编码 | TextField | fnumber |  |  |

