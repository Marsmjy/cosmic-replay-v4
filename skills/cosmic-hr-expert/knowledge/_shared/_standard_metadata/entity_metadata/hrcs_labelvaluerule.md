# hrcs_labelvaluerule — 标签值规则

**表单编码**: `hrcs_labelvaluerule`  
**表单ID**: `2X4WO=BF+SAP`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_labelvaluerule（标签值规则） [BaseEntity]

- **数据库表**: `t_hrcs_labelvaluerule`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| expression | 表达式展示文本 | TextField | fexpression |  |  |
| analysisexpression | 解析表达式 | TextField | fanalysisexpression |  |  |
| labelvalue | 标签值 | BasedataField | flabelvalueid |  | hrcs_labelvalue |
| labelobject | 打标对象 | BasedataField | flabelobjectid |  | hrcs_labelobject |
| brmtarget | 规则引擎指标 | BasedataField | fbrmtargetid |  | brm_target |
| service | 标签值规则服务类配置 | TextField | fservice |  |  |

