# hrcs_promptimport — 提示语导入实体

**表单编码**: `hrcs_promptimport`  
**表单ID**: `1MXN2RCIBTUO`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_promptimport（提示语导入实体） [BaseEntity]

- **数据库表**: `t_hrcs_promptimport`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | fname | ✓ |  |
| businessobject | 实体 | BasedataField | fbusinessobject |  | hbp_entityobject |
| app | 应用 | BasedataField | fapp |  | bos_devportal_bizapp |
| cloud | 云 | BasedataField | fcloud |  | bos_devportal_bizcloud |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| promptcontent | 提示语内容文本 | TextField | fpromptcontent |  |  |
| locale | 语言环境 | BasedataField | flocale |  | inte_language |
| enable | 状态 | BillStatusField | fenable |  |  |
| syscontent | 系统文案 | RadioField | — |  |  |
| selectcontent | 单选按钮组 | RadioGroupField | fselectcontent |  |  |
| diycontent | 自定义文案 | RadioField | — |  |  |

