# hrss_essynrecord — ES同步记录

**表单编码**: `hrss_essynrecord`  
**表单ID**: `3W69/J6OT5OW`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_essynrecord（ES同步记录） [BaseEntity]

- **数据库表**: `t_hrss_essynrecord`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| plansynctime | 执行计划时间 | DateTimeField | fplansynctime |  |  |
| syntype | 同步策略 | ComboField | fsyntype |  |  |
| actualfinishtime | 实际完成时间 | DateTimeField | factualfinishtime | ✓ |  |
| actualsyntime1 | 实际开始时间 | DateTimeField | factualsyntime1 | ✓ |  |
| synstatus | 同步状态 | ComboField | fsynstatus |  |  |
| syncount | 初始化数据 | IntegerField | fsyncount |  |  |
| essyncschemeid | ES同步方案配置 | BasedataField | fessyncschemeid |  | hrss_essyncscheme |
| radiofield | 计划时间执行 | RadioField | — |  |  |
| radiogroupfield | 单选按钮组 | RadioGroupField | fradiogroupfield |  |  |
| radiofield1 | 立即执行 | RadioField | — |  |  |

