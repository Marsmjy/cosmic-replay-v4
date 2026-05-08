# hrcs_labelobjectrel — 标签打标对象关联关系

**表单编码**: `hrcs_labelobjectrel`  
**表单ID**: `2YD=0JC6RA1N`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_labelobjectrel（标签打标对象关联关系） [BaseEntity]

- **数据库表**: `t_hrcs_labelobjectrelnew`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| labelobject | 打标对象 | BasedataField | flabelobjectid |  | hrcs_labelobject |
| brmscene | 规则引擎场景 | BasedataField | fbrmsceneid |  | brm_scene |
| label | 标签 | BasedataField | fid |  | hrcs_label |
| index | 排序号 | IntegerField | fseq |  |  |
| labelvalue | 标签值 | TextField | — |  |  |

