# hrcs_labelvalue — 标签值

**表单编码**: `hrcs_labelvalue`  
**表单ID**: `2WT0+=Z+QR/M`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_labelvalue（标签值） [BaseEntity]

- **数据库表**: `t_hrcs_labelvaluenew`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| label | 标签 | BasedataField | fid |  | hrcs_label |
| value | 标签值 | MuliLangTextField | fvalue |  |  |
| description | 标签值描述 | MuliLangTextField | fdescription |  |  |
| initbatch | 初始化批次 | BigIntField | — |  |  |
| initstatus | 初始化状态 | TextField | — |  |  |

