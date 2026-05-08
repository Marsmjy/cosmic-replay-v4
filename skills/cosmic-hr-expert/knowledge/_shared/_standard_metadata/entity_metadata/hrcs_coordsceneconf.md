# hrcs_coordsceneconf — 协作场景应用配置

**表单编码**: `hrcs_coordsceneconf`  
**表单ID**: `4YDOY0ZC4U+A`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_coordsceneconf（协作场景应用配置） [BaseEntity]

- **数据库表**: `t_hrcs_coordsceneconf`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| scene | 场景 | BasedataField | fsceneid |  | brm_scene |
| coordapp | 协作应用 | BasedataField | fcoordappid |  | bos_devportal_bizapp |
| startdate | 开始时间 | DateTimeField | fstartdate |  |  |
| coordservice | 协作服务名 | TextField | fcoordservice |  |  |
| ruleparamtype | 规则参数实体 | BasedataField | fruleparamtypeid |  | bos_objecttype |
| chgrecordtype | 事务变动记录类型 | BasedataField | fchgrecordtypeid |  | bos_objecttype |
| coordextregister | 增强插件注册器 | TextField | fcoordextregister |  |  |
| failmsg | 失败消息 | TextAreaField | ffailmsg |  |  |
| exbillgroups | 排除核定单类型 | MulComboField | — |  |  |
| exdetailentities | 不关注变动明细实体（信息变更） | MulBasedataField | — |  |  |
| latestpulldate | 最近拉取时间 | DateTimeField | flatestpulldate |  |  |
| batchsize | 分批阈值 | IntegerField | fbatchsize |  |  |

