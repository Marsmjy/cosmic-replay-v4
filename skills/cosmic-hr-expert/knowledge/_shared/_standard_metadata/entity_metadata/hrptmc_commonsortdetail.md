# hrptmc_commonsortdetail — 报表通用排序详情

**表单编码**: `hrptmc_commonsortdetail`  
**表单ID**: `37+KJJE812YT`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_commonsortdetail（报表通用排序详情） [BaseEntity]

- **数据库表**: `t_hrptmc_commonsortdet`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| string | 字符型 | TextField | — |  |  |
| long | 长整数 | BigIntField | — |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| entitysortfieldid | 排序字段 | BigIntField | — |  |  |

