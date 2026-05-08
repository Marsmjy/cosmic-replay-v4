# 异常诊断 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于反编译实证 + 用户报错样本 + 平台规则
> **confidence**：verified

## 1. 异常码 / ResId 表（来自反编译实证）

| ResId | 中文 | 反编译位置 | 触发条件 | 处理建议 |
|---|---|---|---|---|
| `OrgStructProjectPermTreeListPlugin_1` | 当前单据已在其他页签中打开 · 请关闭后重试或重新登录 | `StructProjectEditPlugin.beforeDoOperation` L228（hrmp-haos-formplugin）| 修改方案时 MutexHelper.require 抢锁失败 · 同方案在其它页签打开 | 用户：关闭其它页签 / 重新登录 · ISV：不要尝试改互斥锁逻辑（平台层）|
| `StructListPlugin_1` | 删除成功 | `StructProjectListPlugin.afterDeleteOperation` L194（hrmp-haos-formplugin）| 删除操作 · OperateOption 没有 TITLE/MESSAGE/SHOW_CONFIRM 变量时的默认成功提示 | 业务消息 · 不算异常 |

## 2. StructProjectValidator 内部校验失败（推测）

`StructProjectValidator` 在 SaveOp / DisableOp / EnableOp 三个 OP 都注册（共用）· 反编译没追到 validator 内部代码 · 推测含：

| 推测项 | 推测内容 | 处理建议 |
|---|---|---|
| 必填字段校验 | name / number / org / rootorg / roottype / effdt 是否齐全 | 标品自动校验 · 用户填齐即可 |
| roottype 与必填关系 | roottype=1 时 rootorg 必填 / roottype=2 时 rootnumber 等必填 | 跟随 propertyChanged 联动校验 |
| enable 状态校验 | enable=10（启用中）时不能再 enable | 等 BdVersion 完成切换 |
| disable 时 `STRUCT_PROJECT_MANAGE` 预置母本不能禁用 | issyspreset='1' 的方案不让禁用 | 业务硬规则 · 不应改 |

## 3. StructProjectDeleteValidator 校验失败

`StructProjectDeleteOP.onAddValidators` L37 注册 · 反编译没追到内部 · 推测含：

| 推测项 | 触发条件 | 处理建议 |
|---|---|---|
| 数据状态校验 | enable 不为 10 时不能删（实际跳过 · 不报错）| 业务方需先 disable + 重新 enable 走中间态 |
| 系统预置保护 | `issyspreset='1'` 的预置母本不能删 | 业务硬规则 |

## 4. ISV 自建 Validator 错误信息（CS-02 / CS-04 / CS-07）

### 4.1 CS-02 · roottype 变更校验失败

| 错误信息 | 含义 | 用户处理 |
|---|---|---|
| 方案根组织类型已被以下 haos_structure 实例引用 · 修改会改写下挂组织树 · 请先下线相关实例：[名称列表] | ISV 加的反向引用校验 · 已派生实例时禁切换 roottype | 用户先在 haos_structure 列表禁用 / 删除相关实例 · 然后再来改 roottype |

### 4.2 CS-04 · 删除反向引用校验失败

| 错误信息 | 含义 | 用户处理 |
|---|---|---|
| 方案被以下 haos_structure 实例引用 · 不允许删除：[名称列表] · 请先删除这些实例或解除引用关系 | ISV 加的反向引用校验 · 反查 haos_structure | 用户先解除实例对方案的引用 / 删除实例 · 然后再删方案 |

### 4.3 CS-07 · 启用前置校验失败

| 错误信息 | 含义 | 用户处理 |
|---|---|---|
| 方案生效日期晚于今天（YYYY-MM-DD）· 请先调整 effdt 后再启用 | ISV 加的"effdt 不晚于今天"校验 | 用户调整 effdt 字段 · 改成今天或之前 |
| 启用方案前必须填写关联部门 | ISV 加的"${ISV_FLAG}_relateddept 必填" | 用户填写关联部门字段 |

## 5. 共性陷阱（来自 platform_rules.json + cosmic_realworld_traps）

### 5.1 buildmeta_traps（加字段时的坑）

| 坑 | 说明 | 来源 |
|---|---|---|
| `fieldType` 必须是 74 值枚举之一 | OpenAPI 不支持 EmployeeField / HRAdminOrgField 等 HR SDK 扩展字段 · 用 BasedataField + refEntity 替代 | `kb_cosmic_buildmeta_traps.md` |
| 字段 key 必须以 ISV 简码前缀（如 ${ISV_FLAG}_）| 不带前缀会被标品升级覆盖 | 同上 |
| fieldName 列名超 25 字符 | 数据库建表失败 · 让平台默认 `f + key.lowercase()` 生成 | 同上 |
| modifyMeta add 参数名 | 是 `fieldType/name/columnName` · 不是 `dataType/displayName` · 传错静默走 TextField | `modifymeta_param_names_and_hr_sdk_limits.md` |

### 5.2 addrule_traps（加规则时的坑）

| 坑 | 说明 | 来源 |
|---|---|---|
| ActionType 是 PascalCase | 不是 camelCase · 不是 snake_case | `kb_cosmic_addrule_traps.md` |
| preCondition 不能用 `==''` | 必须用 `enable=='10'` 标准 EL 比较 | 同上 |
| 坏规则清理 | addRule 失败留下脏数据时清理套路 | 同上 |

### 5.3 modifymeta_traps（改元数据时的坑）

| 坑 | 说明 | 来源 |
|---|---|---|
| formId 错位 | 用户 ID 不是 formId · 必须用 `haos_structproject` 而不是 `3BPVOPG05AFA` | `kb_cosmic_modifymeta_traps.md` |
| EmbedFormAp 假成功 | OpenAPI add EmbedFormAp 经常静默失败 · 推荐用 Java 插件 + ISV 扩展元数据 | 同上 |
| ops 格式 | 必须 array of object · 不是 single object | 同上 |

### 5.4 BEC 假发布陷阱（本场景特别注意）

| 坑 | 说明 |
|---|---|
| 本场景标品**没**走标准 BEC | grep 14 类 0 命中 EventServiceHelper · 套用 hjm 的 triggerEventSubscribeJobs 模式假装"我看到了"是错的 |
| eventNumber 没在业务事件中心配置 | IEventService.triggerEventSubscribeJobs 调用静默失败 · 不报错 |
| variables 塞 DynamicObject | 消息体爆 · 订阅方反序列化失败 · 改用轻量标识 + id 自查 |

### 5.5 BdVersion 历史一致性陷阱

| 坑 | 说明 |
|---|---|
| 直接 SQL 改 t_haos_structproject | 绕过 BdVersion · 历史不一致 · 必须走 HRBaseServiceHelper.save |
| enable=10 中间态 | BdVersion 切换过程的中间状态 · 不要在这个状态做删除（标品会跳过 enable≠10 的行 · 但 enable=10 行真删）|

### 5.6 共用物理表陷阱（本场景独有）

| 坑 | 说明 | 处理 |
|---|---|---|
| 加字段污染 haos_structure 表单 | ISV 直接 modifyMeta haos_structproject 而不走 ISV 扩展元数据 · 物理列共用 · haos_structure 表单也显示 | 走 CS-06 form 元数据隔离 |
| `otclassify=1010L` 业务硬编码 | StructProjectSaveOp L103 + haos_structure 列表过滤都依赖此值 · 改成其它值会让数据"消失"（被过滤）| 不要硬编码 · 用常量类引用 |
| `roottype` 切换破坏下挂组织 | 标品不校验下游引用 · 整批改写 OrgStruct | CS-02 防御性扩展 |

### 5.7 互斥锁陷阱

| 坑 | 说明 | 处理 |
|---|---|---|
| 互斥锁未释放 | 表单异常关闭 / LB Crash 时锁未 release | 平台 mutex 超时回收（默认 30 分钟）· 业务方等待或重新登录 |
| pageCache 残留 | edit_struct_clock 残留 false · 阻断后续 modify | StructProjectEditPlugin.openOperationPage L303 在 save 时清 pageCache · 异常时可能不清 · 用户重启表单 |

## 6. 性能陷阱

| 陷阱 | 说明 | 处理 |
|---|---|---|
| `OrgPermHelper.getHRPermOrg(boolean)` 调用频繁 | 每次列表 / F7 都调一次 · 大量用户场景下成瓶颈 | 利用 pageCache `org_perm_result`（StructProjectListPlugin 已实现 · 仅 set Filter 时复用 · 重新进入页面会再调）|
| 跨域调 hrcs `getUserStructProjectsF7` | 网络抖动时增加列表加载延时 | 标品有三重判空兜底（详见 haos_structure_maintenance/03 第 5 节）|
| BatchAdminOrgNewOpService.execute 跨表写 | 派生根组织 + OrgStruct + OrgTeamCoopRel + 派单 · 单条 100-300ms | 不要在循环里反复调 · 用批量入参 |
| Validator 跨表查 N+1 | 不批量 IN 查询 · 循环单条查 | 批量 IN（CS-02 / CS-04 的代码框架已用 IN）|

## 7. 业务异常追溯路径

### 7.1 用户报"我保存方案后下挂组织变了"

排查：
1. 检查 modify 阶段 roottype 是否变化（StructProjectEditPlugin.afterBindData 看 db 旧值）
2. 检查 effdt 是否变化
3. 进入 StructProjectSaveOp.saveStructProjectAndRootOrg L131-180 调试
4. 看是否走"非同 roottype + 非同 effdt"分支 · 调 OtherStructService

修复：加 CS-02 校验 + 业务方培训"roottype 切换的影响"

### 7.2 用户报"我删除方案后实例打开报错"

排查：
1. 反查 haos_adminorg_msgdetail 看派单消息
2. 反查 haos_structure.relyonstructproject 看悬挂引用
3. 看 audit log 哪个用户什么时候删的方案

修复：加 CS-04 校验 · 历史悬挂数据需手动修复 · 重新绑定实例的 relyonstructproject 字段

### 7.3 用户报"我打开表单转圈不出来"

排查：
1. 看 StructProjectEditPlugin.beforeDoOperation 的互斥锁日志
2. 看 OperateOption 是否有 modifyClockStr 残留
3. 看 t_bos_mutex 表是否有残留锁记录

修复：用户重新登录 / 等待 mutex 超时 · ISV 不应改互斥锁逻辑

### 7.4 用户报"我加了 BEC 发布方但订阅方收不到"

排查：
1. 检查业务事件中心是否配 eventNumber `${ISV_FLAG}_haos_structproject_changed`
2. 检查订阅配置是否绑了 IEventServicePlugin 实现类
3. 看发布方日志是否有 IEventService.triggerEventSubscribeJobs 调用错误
4. 看订阅方日志是否有 handleEvent 调用

修复：
- 配 eventNumber + 订阅
- 订阅方做幂等处理
- 检查 RPC 服务发现是否正常

## 8. ISV 测试自检清单

实施扩展后必跑：

| 测试项 | 验证方式 |
|---|---|
| 加字段不污染 haos_structure | `client.get_form_schema("haos_structure")` 不含 ISV 字段 |
| roottype 变更校验生效 | 派生实例 → 改 roottype 保存 → 必报错阻断 |
| 删除反向引用校验生效 | 创建实例 → 删除方案 → 必报错阻断 |
| 启用前置校验生效 | effdt 设未来 → enable → 必报错阻断 |
| BEC 发布方生效 | 业务事件中心查 EventLog · 确认事件已派 |
| BEC 订阅方生效 | 订阅方日志含 eventId 处理记录 |
| 互斥锁机制不破坏 | 多用户同时编辑同方案 · 一个能编辑 / 其它阻断 |
| 列表过滤过滤行为不变 | 全权限 / 部分权限用户都能正确看到方案列表 |

## 9. 关联知识

- 共性陷阱：[`../../cosmic_realworld_traps/buildmeta_traps.md`](../../cosmic_realworld_traps/buildmeta_traps.md)
- 共性陷阱：[`../../cosmic_realworld_traps/addrule_traps.md`](../../cosmic_realworld_traps/addrule_traps.md)
- 共性陷阱：[`../../cosmic_realworld_traps/modifymeta_traps.md`](../../cosmic_realworld_traps/modifymeta_traps.md)
- 平台规则：[`../../_shared/platform_rules.json`](../../_shared/platform_rules.json)
- 配套场景异常：[`../haos_structure_maintenance/10_exceptions.md`](../haos_structure_maintenance/10_exceptions.md)
- BEC 规范：`memory/feedback_bec_3layer_async_publish.md`（标品 BEC 可能走 3 层异步 OP→Service→*MsgTask）

## 10. 来源追溯

- ResId：StructProjectEditPlugin.beforeDoOperation L228 + StructProjectListPlugin.afterDeleteOperation L194
- BEC 缺失实证：grep 14 反编译类 `triggerEventSubscribe / IEventService / EventServiceHelper` 全 0 命中
- 派单实证：AdminChangeMsgService.handleChangeMsg L113-123 走 sch_task + JobClient.dispatch
- 互斥锁：StructProjectEditPlugin.beforeDoOperation L217-232 + openOperationPage L298-303
