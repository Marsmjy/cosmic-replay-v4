# haos_staffflex_bd — 编制弹性域纵表

**表单编码**: `haos_staffflex_bd`  
**表单ID**: `4H0KPEZ28H9W`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_staffflex_bd（编制弹性域纵表） [BaseEntity]

- **数据库表**: `t_haos_staffflex_bd`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| hg | 横表 | BasedataField | fid | ✓ | haos_staffflex |
| auxproptype | 辅助属性类型 | TextField | fflexfield | ✓ |  |
| auxpropval | 辅助属性值 | BigIntField | fvalue |  |  |

