# hrcs_bgtaskregister — 悬浮球任务注册

**表单编码**: `hrcs_bgtaskregister`  
**表单ID**: `2LYUB=K/+C3J`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_bgtaskregister（悬浮球任务注册） [BaseEntity]

- **数据库表**: `t_hrcs_bgtaskregister`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| bizcloud | 所属云 | BasedataField | fbizcloudid | ✓ | bos_devportal_bizcloud |
| bizapp | 所属应用 | BasedataField | fbizappid | ✓ | bos_devportal_bizapp |
| taskclassname | 调度任务执行类 | TextField | ftaskclassname | ✓ |  |
| clickclassname | 页面操作执行类 | TextField | fclickclassname | ✓ |  |
| timeout | 超时时间 | IntegerField | ftimeout |  |  |
| needresume | 是否需要恢复(已废弃，使用平台恢复功能) | CheckBoxField | fneedresume |  |  |
| callbackclassname | 回调方法执行类 | TextField | fcallbackclassname |  |  |
| actionid | 回调方法动作标识 | TextField | factionid |  |  |

