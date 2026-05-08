# hrcs_bgtaskrecord — 悬浮球任务记录

**表单编码**: `hrcs_bgtaskrecord`  
**表单ID**: `2LYVSO6ED3Y0`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_bgtaskrecord（悬浮球任务记录） [BaseEntity]

- **数据库表**: `t_hrcs_bgtaskrecord`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| bgtaskregister | 悬浮球任务注册信息 | BasedataField | fbgtaskregisterid |  | hrcs_bgtaskregister |
| taskstatus | 任务状态 | ComboField | ftaskstatus |  |  |
| isconfirm | 已确认 | CheckBoxField | fisconfirm |  |  |
| jobforminfo | 任务作业信息 | TextField | fjobforminfo |  |  |
| taskid | 执行任务 | TextField | ftaskid |  |  |
| isbackground | 是否后台运行 | CheckBoxField | fisbackground |  |  |
| starttime | 开始时间 | DateTimeField | fstarttime |  |  |
| endtime | 结束时间 | DateTimeField | fendtime |  |  |

