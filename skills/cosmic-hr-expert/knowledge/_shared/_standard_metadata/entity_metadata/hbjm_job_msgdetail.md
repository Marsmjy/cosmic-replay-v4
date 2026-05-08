# hbjm_job_msgdetail — 职位消息明细

**表单编码**: `hbjm_job_msgdetail`  
**表单ID**: `52N4QVV0QFK2`  
**归属**: HR基础服务云 / HR基础职位  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbjm_job_msgdetail（职位消息明细） [BaseEntity]

- **数据库表**: `t_hbjm_job_msgdetail`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| bo | 职位bo | BasedataField | fboid |  | hbjm_jobhr |
| beforeversion | 变更前版本 | HisModelBasedataField | fbeforeversionid |  | hbjm_jobhr |
| afterversion | 变更后版本 | HisModelBasedataField | fafterversionid |  | hbjm_jobhr |
| sendstate | 发送状态 | ComboField | fsendstate |  |  |
| traceid | traceid | TextField | ftraceid |  |  |
| index | 顺序号(同批次) | IntegerField | findex |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |

