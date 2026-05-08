# 上下游联动 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟢 基于反编译 7 类 + scene_doc.json 56 字段 + DynaAuthSchemeServiceHelper 调用图
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、上下游全景图

```
                      ┌──────────────────────────────────────────┐
                      │  上游基础资料                              │
                      │  ─────────────                           │
                      │  perm_admingroup    （HR 管理员组）       │
                      │  perm_role          （平台角色）          │
                      │  hrcs_role          （中台角色）          │
                      │  hrcs_dynaauthobject（授权对象 · 人员）   │
                      │  hpfs_chgcategory   （变动类型）          │
                      │  hbjm_jobhr         （职位 · 用于规则）  │
                      │  haos_adminorg      （行政组织 · 用于规则）│
                      │  hbpm_position      （岗位 · 用于规则）  │
                      └─────────────┬────────────────────────────┘
                                    │ F7 / 关联引用
                                    ↓
                      ┌──────────────────────────────────────────┐
                      │  hrcs_dynascheme （主场景 · 本文档）       │
                      │                                          │
                      │  主表 t_hrcs_dynascheme                   │
                      │  ├ assignactionentry → t_hrcs_dynaschasgnactent│
                      │  ├ cancelactionentry → t_hrcs_dynaschcclactent│
                      │  ├ roleentry         → t_hrcs_dynaschemerole │
                      │  └ search_*  6 子表（反查派生）           │
                      └─────────────┬────────────────────────────┘
                                    │
              ┌─────────────────────┼─────────────────────┐
              │                     │                     │
              ↓ save 灌库          │ delete 级联删除       │ audit 重算
              │                     │                     │
       ┌──────────────────┐  ┌─────────────────────┐  ┌──────────────────────┐
       │ 反查派生表（6）    │  │ 配置下游表（5）       │  │ 用户角色绑定表        │
       │ ─────────────    │  │ ──────────────       │  │ ──────────────       │
       │ search_param     │  │ hrcs_dynaschemerange │  │ hrcs_userrolerelat   │
       │ search_adminorg  │  │ hrcs_dynaschorg      │  │ （sourcetype=4）      │
       │ search_pos       │  │ hrcs_dynaschdimgrp   │  │                      │
       │ search_job       │  │ hrcs_dynaschdatarule │  │ 由权限重算任务异步     │
       │ search_actiona   │  │ hrcs_dynaschfield    │  │ 展开 dynascheme +    │
       │ search_actionc   │  │                      │  │ 命中条件的员工 · 写绑定│
       └──────────────────┘  └─────────────────────┘  └──────────────────────┘
```

---

## 二、上游引用清单（dynascheme 依赖谁）

| 上游实体 | dynascheme 字段 | 关系 | 失败影响 |
|---|---|---|---|
| `perm_admingroup` | `admingroup` | F7 引用（domain=hr 过滤 · L347-L349） | 选不到 admingroup · 无法新建 |
| `perm_role` | `roleentry.role` | F7 引用（HRRolePermHelper.showRoleF7 L546） | 选不到角色 · 无法 addrole |
| `hrcs_role` | `roleentry.hrcsrole` | RoleDBServiceHelper.loadHrRoleDyn 反查（L273） | 中台角色为空 · 不影响主功能 |
| `hrcs_dynaauthobject` | `assignpersonitem` / `cancelpersonitem` | F7 引用 | 选不到授权对象 · 但 isOnly1010 特殊兼容 |
| `hpfs_chgcategory` | `assignactype` / `cancelactype` | F7 引用 + 去重过滤（L337-L344） | 选不到变动类型 · 无法填分录 |
| `hbjm_jobhr` / `haos_adminorg` / `hbpm_position` 等 | `condition` 规则参数中的引用 | DecisionSet JSON 内部引用 | 规则配置时 PermFilter 控件支持 |

### 2.1 上游变动对 dynascheme 的影响

| 上游变动 | 对 dynascheme 影响 | 处理方式 |
|---|---|---|
| `perm_admingroup` 删除一个 admingroup | dynascheme 上引用该 admingroup 的方案变成"悬空" | 标品没自动级联（perm_admingroup 是 HR 平台基础资料 · 删除前一般有它自己的下游校验） |
| `perm_role` 禁用一个角色 | roleentry 引用该角色的方案不立即变 · 但下次重算时该角色绑定不生效 | ISV 可加 hrcs_dynascheme.roleentry 的 setFilter 过滤已禁用角色（CS-07 套路） |
| `hrcs_role` 删除一个中台角色 | hrcsrole 字段变成"找不到"· F7 显示问题 | RoleDBServiceHelper.loadHrRoleDyn 返回 null · UI 显示空 · 不报错 |
| `hpfs_chgcategory` 改 number | dynascheme 引用 ID · 不影响（标品基础资料用 id 关联） | - |
| `hbjm_jobhr` 等 condition 引用对象 改 boid | 不会发生（boid 永不变）· 改 id 也不影响 dynascheme（dynascheme 内的 condition 存的是 boid） | - |

---

## 三、下游联动清单（dynascheme 影响谁）

### 3.1 同事务下游（save / submit / audit / confirmchange 立即写）

#### t_hrcs_dynaschemerole（roleentry 分录）
- 谁写：DynaAuthSchemeSaveSubmitOp / DynaAuthSchemeAuditOp / DynaAuthSchemeConfirmChangeOp · 调 `DynaSchemeRoleAssignServiceHelper.saveRoleEntry`
- 关联：scheme.id (save) / scheme.sourceVid (audit) / scheme.boid + bgVid (confirmchange 双写)
- 同事务

#### t_hrcs_dynasearchparam / search_adminorg / search_pos / search_job / search_actiona / search_actionc（6 张反查表）
- 谁写：DynaAuthSchemePlugin.beforeDoOperation · 调 `DynaAuthSchemeServiceHelper.resolveRuleConfigToSearch + resolveSceneToSearch`
- 时机：save / submit / confirmchange 时 · `condition` 不空才写
- 数据：condition JSON 反向解析的扁平索引
- 同事务

### 3.2 列表 afterDoOperation 下游（delete 级联）

由 `DynaAuthSchemeListPlugin.afterDoOperation` (L168-L183) 在 delete 后清理：

| 下游表 | 含义 | 关联键 |
|---|---|---|
| `hrcs_dynaschemerange` | 方案权限范围（按 admingroup 划定） | `scheme = pkId` |
| `hrcs_dynaschorg` | 方案组织 | `scheme = pkId` |
| `hrcs_dynaschdimgrp` | 方案维度组 | `scheme = pkId` |
| `hrcs_dynaschdatarule` | 方案数据规则 | `scheme = pkId` |
| `hrcs_dynaschfield` | 方案字段 | `scheme = pkId` |

⚠️ delete 不清 `hrcs_dynaschemerole`（标品 BdVersionDeleteOp 通过分录归属 belongTo 自动级联）。

⚠️ delete **不清** `hrcs_userrolerelat`（重要！）· 这是 ISV 必须补的能力（CS-04 阻断 + 自定义清理 OP）

### 3.3 异步下游（标品任务驱动）

```
方案 audit 通过 / confirmchange 完成
   ↓
[标品异步任务] 权限重算
   ↓
对每条命中条件的员工 ↓
  写入 hrcs_userrolerelat：
  - source = scheme.boid
  - sourcetype = "4"  （动态方案分配）
  - role = roleentry.role
  - status = 1（启用）
   ↓
[运行时] 用户登录 · 查询权限
   ↓
取 hrcs_userrolerelat 计算用户最终权限集
   ↓
影响菜单可见性 / 数据权限 / 操作权限
```

**重算任务的具体名字 / 频率 / 实现** —— 标品内部 · 反编译里没看到 task 类（DynaAuthSchemeServiceHelper 等 helper 是同步入口）。**业务侧应去苍穹后台调度查 task 注册表**确认。

---

## 四、跨模块联动（HR 全域）

| 模块 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| **hrpi（人事）** | dynascheme.condition 规则可能引用员工字段（员工.职级 / 员工.工作城市等）· 员工岗位变动会触发权限重算 | 异步 | 重算失败标品有补偿任务 |
| **hbjm（职位）** | dynascheme.condition 可引用 hbjm_jobhr 的字段 · 职位历史版本变动会触发重算 | 异步 | 同上 |
| **haos（组织）** | dynascheme.condition 可引用行政组织字段 · 组织变动会触发重算 | 异步 | 同上 |
| **hbpm（岗位）** | dynascheme.condition 可引用岗位字段 | 异步 | 同上 |
| **薪酬 / 考勤 / 绩效** | 不直接联动 · 但这些子域的"权限页面"会用 hrcs_userrolerelat 决定可见性 | 异步 | 权限校验失败 → 用户拒绝访问 |
| **OA / 外部系统** | 标品**不发 BEC 事件** · 外部不能监听方案变更 | - | ISV 必须自建 BEC 发布方（CS-05） |

---

## 五、BEC 业务事件中心（标品状态）

按 `feedback_bec_3layer_async_publish.md` + `feedback_bec_mode_per_scene_verify.md` 铁律 · 写本节前已 grep：

```
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hrcs_dynascheme/

→ 0 处命中（在 7 个反编译类里）
```

**BEC 模式判定**：⛔ **标品没发 BEC**

| 实证维度 | 状态 |
|---|---|
| 主 OP `DynaAuthSchemeOp.java` | 0 处 BEC 调用 |
| save/submit OP `DynaAuthSchemeSaveSubmitOp.java` | 0 处 BEC 调用 |
| audit OP `DynaAuthSchemeAuditOp.java` | 0 处 BEC 调用 |
| confirmchange OP `DynaAuthSchemeConfirmChangeOp.java` | 0 处 BEC 调用 |
| FormPlugin `DynaAuthSchemePlugin.java` | 0 处 BEC 调用 |
| ListPlugin `DynaAuthSchemeListPlugin.java` | 0 处 BEC 调用 |
| 准入闸 `HRAdminStrictPlugin.java` | 0 处 BEC 调用 |

→ 与 `haos_structure_maintenance` 同属"标品没发 BEC"组 · 与 `hjm_jobhr_maintenance`（3 层异步发布）和 `homs_orgbatchchgbill_maintenance`（sch_task 派单）模式不同

→ ISV 实施时 · 如果业务需要"方案变更通知下游" · 必须 ISV 自建 BEC 发布方（CS-05）· 不能假设标品已发

---

## 六、上下游联动的 PR 引用

| 联动点 | 应遵循的 PR |
|---|---|
| 下游引用 boid 而非 id | PR-009（boid 是业务维度 · id 是版本维度） |
| 时序查询带 iscurrentversion | PR-008（HisModel 当前版本必带） |
| 同事务写 ISV 自建表 | PR-010 阶段 8（endOperationTransaction） |
| 异步发外部事件 | PR-010 阶段 9 + PR-011（afterExecuteOperationTransaction + IEventService） |
| 跨表 N+1 优化 | PR-数据 API（QueryServiceHelper.queryDataSet） |

---

## 七、上下游联动的 ISV 扩展点

```
我要在哪里加扩展？
  ↓
1. 规则命中后给员工额外做点事（如发邮件）
   → ISV 自建 BEC 发布方（CS-05）+ 订阅方收到事件后做事
  ↓
2. 删除/禁用方案前查下游绑定
   → ISV 自建 Validator 挂 onAddValidators（CS-04）
  ↓
3. confirmchange 后写 ISV 审计日志
   → ISV 自建 OP 挂 endOperationTransaction（CS-06）
  ↓
4. 列表按事业部过滤
   → ISV 自建 ListPlugin 挂 setFilter（CS-07）
  ↓
5. 加新的下游配置表（如 ISV 自建"方案标签关联表"）
   → ISV 自建表 + 挂 dynascheme save/audit OP 在 endOperationTransaction 阶段写
  ↓
6. 监听上游基础资料变化（perm_role 删除时给 dynascheme 提醒）
   → ISV 自建 OP 挂 perm_role 的 delete · 反查 dynascheme 哪些方案引用了它
```

---

## 八、上下游引用速查表（生产前必看）

```
扩展 dynascheme 时务必检查：

[ ] 关联的下游表有哪些？
    答：roleentry / 6 反查表 / 5 配置表 / hrcs_userrolerelat

[ ] 关联键用 id 还是 boid？
    答：内部分录用 entryboid · 跨表用 boid（PR-009）

[ ] 标品发 BEC 吗？
    答：不发（grep 0 处）· ISV 想发自己来（CS-05）

[ ] save/audit/confirmchange 哪个 op 走 saveRoleEntry？
    答：save/submit DynaAuthSchemeSaveSubmitOp（id 维度）
       audit DynaAuthSchemeAuditOp（sourceVid 维度）
       confirmchange DynaAuthSchemeConfirmChangeOp（boid + bgVid 双写）

[ ] delete 会自动清下游吗？
    答：清 5 张配置表 · 不清 hrcs_userrolerelat · 不清 hrcs_dynaschemerole（后者由 BdVersionDeleteOp 级联）

[ ] disable 会清下游绑定吗？
    答：不清 · 等下次重算

[ ] 上游 perm_admingroup 删除会级联到 dynascheme 吗？
    答：标品没级联 · 业务侧需手动检查
```

---

## 九、上下游变更影响公式

```
上游基础资料变更 (admingroup / role / hrcsrole / dynaauthobject / chgcategory)
   ↓ ISV 加事前 Validator 拦
   ↓
dynascheme 主表变更（save / submit / audit / disable / enable / delete / confirmchange）
   ↓ 同事务写：roleentry + 6 反查表 + 5 配置表
   ↓ 异步：权限重算任务展开 → hrcs_userrolerelat
   ↓
hrcs_userrolerelat 变更
   ↓ 用户登录 / 查权限
   ↓
影响菜单 / 数据权限 / 操作权限
```

→ 这条链路任意一段断都可能导致权限丢失。ISV 实施前应该：
1. 看本文档的"下游联动清单"确认改动会影响哪些表
2. 先在测试环境完整跑一遍 audit + confirmchange + delete · 确认下游联动符合预期
3. 加监控埋点（用 LogFactory.getLog · 记 boid + opKey + 时间戳）便于事故复盘

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json` · 更新时间 2026-04-29
> 本 form（`hrcs_dynascheme`，所属 HR 基础服务云）引用了其他云的 **2** 个底座实体：

### ⬆️ 考勤云（`attendance`）2 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `assignactype` | 变动类型 | BasedataField | `hpfs_chgcategory` | — |
| `cancelactype` | 变动类型 | BasedataField | `hpfs_chgcategory` | — |

> ⚠️ ISV 扩展须知（ADR-009）：
> - 上游底座实体是**标品字典**，原则上不可改字段（参各上游场景的 06_customization_solutions.md）
> - 引用方式（fieldType / refEntity）由本 form 元数据控制；本 form 改 ref 字段值用 `setValue` 即可
> - 修改前必须读对应上游场景的 11_upstream_downstream_logic.md，确认上游 ISV 扩展规则

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 01 总论）

> 本场景属 HR 基础服务云（hr_hrmp）· 业务语义参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md)
> - 跨云元规则：金字塔决策方法论 + 11 大特殊解决方案
> - 6 大可继承通用模板（hbp_bd_tpl_all / hbp_bd_timelinemintpl 等）
> - HR 通用 SDK 服务 16 个（HisModelServiceHelper / TimelineServiceHelper / RuleEngineServiceHelper 等）
> - 历史模型 vs 时间轴的 6 模板 + 字段差异

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/hr_hrmp_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：1 个本场景实体 · 共 10 处引用 · 其中 0 处跨云。

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
