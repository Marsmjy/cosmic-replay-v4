# hrpi_ssctaskhis — HR库已完成共享任务模板

**表单编码**: `hrpi_ssctaskhis`  
**表单ID**: `49=WPL72UNV4`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_ssctaskhis（HR库已完成共享任务模板） [BillEntity]

- **数据库表**: `t_hrpi_ssctaskhis`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | fbillno |  |  |
| billstatus | 单据状态 | BillStatusField | fbillstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | fauditorid |  | bos_user |
| auditdate | 审核时间 | DateTimeField | fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| imagenumber | 影像编码 | TextField | — |  |  |
| imageok | 影像是否OK | ComboField | — |  |  |
| assignid | 工作流任务 | TextField | — |  |  |
| info | 消息 | TextField | — |  |  |
| procinstid | 流程实例 | TextField | — |  |  |
| reverseoprt | 反向操作 | TextField | — |  |  |
| oprt | 执行操作 | TextField | — |  |  |
| itemid | 驳回节点Id | TextField | — |  |  |
| sourcetaskid | 原任务id | BigIntField | — |  |  |
| reformperson | 整改人 | UserField | — |  | bos_user |
| orignalperson | 原质检人 | UserField | — |  | bos_user |
| qualityresult | 质检结果 | ComboField | — |  |  |
| qualitystate | 质检状态（弃用） | ComboField | — |  |  |
| qualitysamplelibrary | 质检样本库 | BasedataField | — |  | task_qualitysamplelibrary |
| qualitychecktime | 检验时间 | DateTimeField | — |  |  |
| autoprocessforcheck | 审批类型 | ComboField | — |  |  |
| billid | 单据ID | TextField | — |  |  |
| sysbillid | 内部单据ID | IntegerField | — |  |  |
| billnumber | 单据编码 | TextField | — |  |  |
| tasktypeid | 任务类型 | BasedataField | — |  | task_tasktype |
| billtype | 业务单据 | BasedataField | — |  | task_taskbill |
| orgid | 组织 | OrgField | — |  | bos_org |
| sscid | 共享中心 | OrgField | — |  | bos_org |
| source | 来源 | ComboField | — |  |  |
| sourcetype | 来源类型 | ComboField | — |  |  |
| subject | 主题 | TextField | — |  |  |
| extenderp | 外部系统 | BasedataField | — |  | bas_extenderp |
| pooltype | 任务池类型（处理环节） | ComboField | — |  |  |
| state | 任务状态 | ComboField | — |  |  |
| tasklevelid | 任务优先级 | BasedataField | — |  | task_tasklevel |
| oldtaskstate | 任务原状态 | TextField | — |  |  |
| coefficient | 任务量系数 | DecimalField | — |  |  |
| createruleid | 任务创建规则 | BasedataField | — |  | task_taskbill_child |
| recyclestate | 回收后状态 | TextField | — |  |  |
| multistate | 多级处理状态 | ComboField | — |  |  |
| expirestate | 超期状态 | ComboField | — |  |  |
| level | 级次 | IntegerField | — |  |  |
| bizdata | 业务数据 | LargeTextField | — |  |  |
| apprevalmessage | 审批意见 | TextAreaField | — |  |  |
| innermsg | 内部说明 | TextField | — |  |  |
| unpassreasonid | 批退原因 | BasedataField | — |  | task_withdrawal |
| unpassreasondesc | 批退原因描述 | LargeTextField | — |  |  |
| flagmsg | 关注说明 | TextField | — |  |  |
| flowbackstgid | 打回策略 | BasedataField | — |  | task_tasktype |
| autoprocess | 自动审批 | CheckBoxField | — |  |  |
| hasallocated | 是否分配过 | CheckBoxField | — |  |  |
| allocatecount | 分配次数 | IntegerField | — |  |  |
| islastaudit | 是否终审 | CheckBoxField | — |  |  |
| personid | 处理人 | BasedataField | — |  | bos_user |
| consignerid | 委托人 | BasedataField | — |  | task_tasktype |
| applytime | 提单时间 | DateTimeField | — |  |  |
| taskcreatetime | 任务创建时间 | DateTimeField | — |  |  |
| completetime | 完成时间 | DateTimeField | — |  |  |
| costwaittime | 当前处理人耗时 | DecimalField | — |  |  |
| resttime | 剩余时间 | DecimalField | — |  |  |
| receivetime | 接收时间 | DateTimeField | — |  |  |
| waittime | 等待时间 | DecimalField | — |  |  |
| rescanwaittime | 退回重扫等待时间 | DecimalField | — |  |  |
| pausewaittime | 暂挂等待时间 | DecimalField | — |  |  |
| iscalculated | 是否计算过信用 | CheckBoxField | — |  |  |
| ishandled | 是否是处理过 | CheckBoxField | — |  |  |
| orglongnumber | 组织长编码 | TextField | — |  |  |
| usergroup | 用户组 | BasedataField | — |  | task_usergroup |
| decisionitem | 操作类型 | ComboField | — |  |  |
| decisionitemnew | 决策项 | BasedataField | — |  | task_decisionitem |
| pendingopinion | 暂挂原因 | TextAreaField | — |  |  |
| rescanopinion | 退扫原因 | TextAreaField | — |  |  |
| voucherno | 凭证号 | TextField | — |  |  |
| voucherstat | 凭证状态 | TextField | — |  |  |
| predictdistime | 预计分配时间 | DateTimeField | — |  |  |
| matchrule | 分配规则 | BasedataField | — |  | task_disrule |
| autoprocessresult | 智能机器人审核结果 | ComboField | — |  |  |
| firstreceivetime | 首次接收时间 | DateTimeField | — |  |  |
| firstcostwaittime | 任务处理耗时 | DecimalField | — |  |  |
| billlongid | 单据ID | BigIntField | — |  |  |
| operatorid | 干预人 | BasedataField | — |  | bos_user |
| predictlevel | 风险预测等级 | ComboField | — |  |  |
| predictvalue | 风险预测分数 | DecimalField | — |  |  |
| decisionvalue | 决策项值（弃用） | TextField | — |  |  |
| imageuploadtime | 影像上传时间 | DateTimeField | — |  |  |
| statelabel | 状态标签 | MulComboField | — |  |  |
| rejperson | 批退人 | UserField | — |  | bos_user |

