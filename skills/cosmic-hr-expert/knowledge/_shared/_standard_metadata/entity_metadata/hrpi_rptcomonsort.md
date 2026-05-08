# hrpi_rptcomonsort — 人员报表通用排序

**表单编码**: `hrpi_rptcomonsort`  
**表单ID**: `3D4CBLNC1D0V`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_rptcomonsort（人员报表通用排序） [BaseEntity]

- **数据库表**: `t_hrpi_rptcomonsort`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| string | 字符型 | TextField | — |  |  |
| long | 长整数 | BigIntField | — |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| entitysortfieldid | 排序字段 | BigIntField | — |  |  |

