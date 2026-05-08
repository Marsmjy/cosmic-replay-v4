# hrcs_labelparam — 标签关联因子

**表单编码**: `hrcs_labelparam`  
**表单ID**: `2X2TLQOWGVSS`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_labelparam（标签关联因子） [BaseEntity]

- **数据库表**: `t_hrcs_labelparam`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| param | 标签关联因子 | BasedataField | fparamid |  | hrcs_lblobjectfield |
| label | 标签 | BasedataField | flabelid |  | hrcs_label |
| labelvalue | 标签值 | BasedataField | flabelvalue |  | hrcs_labelvalue |
| labelobject | 打签对象 | BasedataField | flabelobjectid |  | hrcs_labelobject |
| brmscene | 规则引擎场景 | BasedataField | fbrmsceneid |  | brm_scene |
| brmiputparam | 规则引擎入参 | BasedataField | fbrmiputparamid |  | brm_sceneinput |
| source | 引用源类型 | ComboField | fsource |  |  |

