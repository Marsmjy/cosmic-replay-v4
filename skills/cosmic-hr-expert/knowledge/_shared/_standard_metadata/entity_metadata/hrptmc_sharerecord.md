# hrptmc_sharerecord — 分享记录

**表单编码**: `hrptmc_sharerecord`  
**表单ID**: `48+EX4DS0OFG`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_sharerecord（分享记录） [BaseEntity]

- **数据库表**: `t_hrptmc_sharerecord`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 分享时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| shareuser | 分享人 | UserField | fshareuserid |  | bos_user |
| receiveuser | 接收人 | MulBasedataField | t_hrptmc_receiveuser（子表） |  |  |
| onlyqueryresult | 仅分享查询结果 | CheckBoxField | fonlyqueryresult |  |  |
| expiration | 分享有效期至 | DateField | fexpiration |  |  |
| noticetype | 通知方式 | ComboField | fnoticetype |  |  |
| starttime | 实际开始时间 | DateTimeField | fstarttime |  |  |
| endtime | 实际结束时间 | DateTimeField | fendtime |  |  |
| sharesource | 分享来源 | ComboField | fsharesource |  |  |
| runstatus | 运行状态 | ComboField | frunstatus |  |  |
| reportsubscribe | 报表订阅 | BasedataField | freportsubscribeid |  | hrptmc_subscriberecord |
| userpermfilter | 用户权限过滤条件json | TextField | fuserpermfilter |  |  |
| sharestatus | 分享状态 | ComboField | fsharestatus |  |  |
| esindex | es索引 | BasedataField | fesindexid |  | hrptmc_esindex |
| content | 消息内容 | MuliLangTextField | fcontent |  |  |
| count | 已同步数量 | IntegerField | fcount |  |  |
| traceid | traceId | TextField | ftraceid |  |  |
| errormsg | 错误信息 | TextField | ferrormsg |  |  |
| f7qfilter | F7权限过滤QFilter | TextField | ff7qfilter |  |  |
| dataqfilter | 数据权限过滤QFilter | TextField | fdataqfilter |  |  |
| reportfilter | 报表筛选器过滤条件json | TextField | freportfilter |  |  |
| report | 报表id | BigIntField | freportid |  |  |
| reportnumber | 报表编码 | TextField | freportnumber |  |  |
| reportname | 报表名称 | MuliLangTextField | freportname |  |  |
| admorgf7qfilter | 行政组织F7权限QFitler | TextField | fadmorgf7qfilter |  |  |

