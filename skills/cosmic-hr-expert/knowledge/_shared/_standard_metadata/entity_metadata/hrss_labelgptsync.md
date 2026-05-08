# hrss_labelgptsync — 标签GPT知识库同步

**表单编码**: `hrss_labelgptsync`  
**表单ID**: `3W6X972QDK08`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_labelgptsync（标签GPT知识库同步） [BaseEntity]

- **数据库表**: `t_hrss_labelgptsync`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| starttime | 开始时间 | DateTimeField | fstarttime |  |  |
| endtime | 结束时间 | DateTimeField | fendtime |  |  |
| gptsynstatus | 同步状态 | ComboField | fgptsynstatus |  |  |
| gptsyncount | 同步数量 | IntegerField | fgptsyncount |  |  |
| fileurl | 标签文件URL | TextField | ffileurl |  |  |
| gairepoid | 知识库 | BasedataField | fgairepoid |  | gai_repo_info |
| gairepostatus | 知识库状态 | ComboField | fgairepostatus |  |  |

