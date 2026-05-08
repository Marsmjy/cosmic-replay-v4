# hrcs_activityins — 活动任务实例

**表单编码**: `hrcs_activityins`  
**表单ID**: `2+U7C=LT8+LM`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_activityins（活动任务实例） [BaseEntity]

- **数据库表**: `t_hrcs_activityins`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| applier | 发起人 | UserField | fapplierid | ✓ | bos_user |
| bizbillid | 主业务单据ID值 | TextField | fbizbillid | ✓ |  |
| biznum | 主业务单据编号 | TextField | fbiznum | ✓ |  |
| bizkey | 主业务单据标识 | TextField | fbizkey | ✓ |  |
| actthemestr | 任务主题 | MuliLangTextField | factthemestr |  |  |
| taskstatus | 任务状态 | ComboField | ftaskstatus |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| activity | 活动 | BasedataField | factivityid | ✓ | hrcs_activity |
| handletime | 实际处理时间 | DateTimeField | fhandletime |  |  |
| handlers | 处理人 | MulBasedataField | t_hrcs_actinshandler（子表） |  |  |
| actscheme | 活动方案 | BasedataField | factschemeid | ✓ | hrcs_activityscheme |
| schemeversion | 活动方案版本 | TextField | fschemeversion |  |  |
| wfcurtaskid | 工作流当前任务ID | BigIntField | fwfcurtaskid |  |  |
| wfprocessinsid | 工作流实例ID | BigIntField | fwfprocessinsid |  |  |
| wfprocessdefinitionid | 工作流流程定义ID | BigIntField | fwfprocessdefinitionid |  |  |
| schemenumber | 活动方案编码 | TextField | fshcemenumber |  |  |
| bindbizbillid | 任务关联的单据ID | TextField | fbindbizbillid |  |  |
| bindbiznum | 任务关联的单据编号 | TextField | fbindbiznum |  |  |
| bindbizkey | 任务关联的单据标识 | TextField | fbindbizkey |  |  |
| bindinglayoutid | 绑定页面 | TextField | fbindinglayoutid |  |  |
| wfnode | 工作流节点 | TextField | fwfnodeid |  |  |
| isabandon | 是否废弃 | CheckBoxField | fisabandon |  |  |
| globaltaskid | (工作流)统一任务中心的任务id | BigIntField | fglobaltaskid |  |  |
| ssctaskid | (共享)共享任务中心的任务id | BigIntField | fssctaskid |  |  |
| taskswitch | 任务处理设置 | ComboField | ftaskswitch |  |  |

