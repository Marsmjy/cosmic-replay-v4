# hrcs_promptcontent — 提示语内容

**表单编码**: `hrcs_promptcontent`  
**表单ID**: `1GZPOZCX=NA7`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_promptcontent（提示语内容） [BaseEntity]

- **数据库表**: `t_hrcs_promptcontent`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| prompt | 提示语 | BigIntField | fpromptid |  |  |
| contenttext | 自定义文案(纯文本) | TextField | fcontenttext |  |  |
| locale | 语言环境 | BasedataField | flocaleid |  | inte_language |
| syscontenttext | 系统文案(纯文本) | TextField | fsyscontenttext |  |  |
| promptcontent | 自定义文案 | TextField | fpromptcontent |  |  |
| syscontent | 系统文案 | TextField | fsyscontent |  |  |

