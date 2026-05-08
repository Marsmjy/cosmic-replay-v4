# hrcs_dynamsgdealtrace — 动态权限消息处理跟踪

**表单编码**: `hrcs_dynamsgdealtrace`  
**表单ID**: `46/VAGJZ5D28`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dynamsgdealtrace（动态权限消息处理跟踪） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_dynamsgdealtrace` | BaseEntity | 主表 |
| `t_hrcs_dynamsgdealscheme` | EntryEntity | 方案匹配分录 |
| `t_hrcs_dynamsgdealcal` | EntryEntity | 规则参数计算分录 |
| `t_hrcs_dynamsgdealparams` | MulEmployeeField子表 | 规则参数 |
| `t_hrcs_dynamsgdealtrace_r` | MulEmployeeField子表 | 变动记录 |

### 字段列表 — t_hrcs_dynamsgdealtrace（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_dynamsgdealtrace.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_dynamsgdealtrace.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_dynamsgdealtrace.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_dynamsgdealtrace.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_dynamsgdealtrace.finitdatasource |  |  |
| message | 业务事件 | BasedataField | t_hrcs_dynamsgdealtrace.fmessageid |  | evt_event |
| personinfos | 对应的人员信息 | TextField | t_hrcs_dynamsgdealtrace.fpersoninfos |  |  |
| eventcontent | 消息内容 | TextField | t_hrcs_dynamsgdealtrace.feventcontent |  |  |
| traceid | traceid | TextField | t_hrcs_dynamsgdealtrace.ftraceid |  |  |
| dealresult | 处理结果描述 | TextField | t_hrcs_dynamsgdealtrace.fdealresult |  |  |
| dealstatus | 处理结果状态 | ComboField | t_hrcs_dynamsgdealtrace.fdealstatus |  |  |
| dealstarttime | 处理开始时间 | DateTimeField | t_hrcs_dynamsgdealtrace.fdealstarttime |  |  |
| subscriber | 订阅事件 | BasedataField | t_hrcs_dynamsgdealtrace.fsubscriberid |  | evt_subscription |
| chgaction | 变动操作 | BasedataField | t_hrcs_dynamsgdealtrace.fchgactionid |  | hpfs_chgaction |
| chgevent | 变动大类 | BasedataField | t_hrcs_dynamsgdealtrace.fchgeventid |  | hpfs_chgevent |
| chgrecords | 变动记录 | MulBasedataField | t_hrcs_dynamsgdealtrace_r（子表） |  |  |
| chgcategory | 变动类型 | BasedataField | t_hrcs_dynamsgdealtrace.fchgcategoryid |  | hpfs_chgcategory |
| schemeentry | 方案匹配分录 | EntryEntity | → t_hrcs_dynamsgdealscheme |  |  |
| ruleparamcalentry | 规则参数计算分录 | EntryEntity | → t_hrcs_dynamsgdealcal |  |  |

### 字段列表 — t_hrcs_dynamsgdealscheme（方案匹配分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| scheme | 方案 | BasedataField | t_hrcs_dynamsgdealscheme.fschemeid |  | hrcs_dynascheme |
| assign_ruleparamval | 分配_规则参数值 | TextField | t_hrcs_dynamsgdealscheme.fassignruleparamval |  |  |
| assign_matchresult | 分配_匹配返回结果 | TextField | t_hrcs_dynamsgdealscheme.fassignmatchresult |  |  |
| assign_matchedperson | 分配_符合人员 | TextField | t_hrcs_dynamsgdealscheme.fassignmatchedperson |  |  |
| assign_person | 待分配_人员 | TextField | t_hrcs_dynamsgdealscheme.fassignperson |  |  |
| assign_permfile | 分配_权限档案 | TextField | t_hrcs_dynamsgdealscheme.fassignpermfile |  |  |
| cancel_person | 待取消_人员 | TextField | t_hrcs_dynamsgdealscheme.fcancelperson |  |  |
| cancel_ruleparamval | 取消_规则参数值 | TextField | t_hrcs_dynamsgdealscheme.fcancelruleparamval |  |  |
| cancel_nomatcheduser | 取消_不符合用户 | TextField | t_hrcs_dynamsgdealscheme.fcancelnomatcheduser |  |  |
| cancel_relats | 取消_用户角色关联 | TextField | t_hrcs_dynamsgdealscheme.fcancelrelats |  |  |
| assign_dealedperson | 分配_处理过的人员 | TextField | t_hrcs_dynamsgdealscheme.fassigndealedperson |  |  |
| cancel_dealedperson | 取消_处理过的人员 | TextField | t_hrcs_dynamsgdealscheme.fcanceldealedperson |  |  |
| cancel_matchresult | 取消_匹配返回结果 | TextField | t_hrcs_dynamsgdealscheme.fcancelmatchresult |  |  |
| conditionsize | 条件长度 | IntegerField | t_hrcs_dynamsgdealscheme.fconditionsize |  |  |

### 字段列表 — t_hrcs_dynamsgdealcal（规则参数计算分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| calmode | 计算方式 | ComboField | t_hrcs_dynamsgdealcal.fcalmode |  |  |
| appnumber | 应用编码 | TextField | t_hrcs_dynamsgdealcal.fappnumber |  |  |
| mserviceclass | 微服务类名 | TextField | t_hrcs_dynamsgdealcal.fmserviceclass |  |  |
| mservicemethod | 微服务方法 | TextField | t_hrcs_dynamsgdealcal.fmservicemethod |  |  |
| apiinparam | 入参 | TextField | t_hrcs_dynamsgdealcal.finparam |  |  |
| apiresult | api调用结果 | TextField | t_hrcs_dynamsgdealcal.fapiresult |  |  |
| ruleparamval | 规则参数值 | TextField | t_hrcs_dynamsgdealcal.fruleparamval |  |  |
| calparam_scheme | 方案 | BasedataField | t_hrcs_dynamsgdealcal.fschemeid |  | hrcs_dynascheme |
| multiruleparam | 规则参数 | MulBasedataField | t_hrcs_dynamsgdealparams（子表） |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_dynamsgdealtrace（主表） | 17 |
| t_hrcs_dynamsgdealscheme（方案匹配分录） | 14 |
| t_hrcs_dynamsgdealcal（规则参数计算分录） | 9 |
| t_hrcs_dynamsgdealparams（MulEmployeeField子表） | 1 |
| t_hrcs_dynamsgdealtrace_r（MulEmployeeField子表） | 1 |

