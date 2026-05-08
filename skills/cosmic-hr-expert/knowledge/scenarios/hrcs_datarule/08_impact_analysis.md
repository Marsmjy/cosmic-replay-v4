# 变更影响面 · 数据规则 (hrcs_datarule)

> **状态**: 🟢 基于 24 plugin 实抓 + 21 字段 scene_doc.json + 42 opKey rules_chain_all.json + 3 类反编译实证
> **confidence**: verified（所有字段影响均来自反编译实证的读写路径 + OP 链分析）

---

## 一、字段变更影响面（按 minefield 红/黄/绿分级）

### 1.1 红雷区字段（12 个 · 系统/平台维护 · 改必崩）

| 字段 | physicalColumn | 改了影响 | 安全等级 |
|---|---|---|---|
| `creator` | `fcreatorid` | CreaterField · BOS L0 平台维护 · 手改破坏审计链 | 红区（不改） |
| `modifier` | `fmodifierid` | ModifierField · BOS L0 平台维护 · 手改破坏审计链 | 红区（不改） |
| `createtime` | `fcreatetime` | CreateDateField · 手改时间戳不一致 · 排序/列表错乱 | 红区（不改） |
| `modifytime` | `fmodifytime` | ModifyDateField · 手改影响乐观锁判断（CS-03 用） | 红区（不改） |
| `masterid` | `fmasterid` | MasterIdField · 平台主数据内码 · 手改导致实体查不到 | 红区（不改） |
| `issyspreset` | `fissyspreset` | 系统预置标志 · PR-007 保护 · 改后系统预置规则可被误删/误改 | 红区（不改） |
| `disabler` | `FDisablerID` | 禁用人 · disable OP 自动维护 · 手改破坏禁用追溯 | 红区（不改） |
| `disabledate` | `FDisableDate` | 禁用时间 · disable OP 自动维护 · 手改破坏禁用追溯 | 红区（不改） |
| `initdatasource` | `finitdatasource` | 数据来源（出厂值）· 平台维护 · 手改影响数据源追溯 | 红区（不改） |
| `orinumber` | `forinumber` | 出厂编码 · 标品出厂数据 · 改了跟出厂数据不一致 | 红区（不改） |
| `oristatus` | `foristatus` | 出厂数据编辑状态 · 标品维护 | 红区（不改） |
| `oriname` | `foriname` | 出厂名称 · 标品出厂数据 · 手改破坏出厂溯源 | 红区（不改） |

### 1.2 黄雷区字段（2 个 · 变更级联下游 · 慎改）

| 字段 | physicalColumn | 改了影响 | 安全等级 |
|---|---|---|---|
| `status` | `fstatus` | 数据状态（draft/saved/audit/submitted）· 改后权限链消费端可能立即跳过或纳入本规则 · 影响范围取决于本规则被多少方案/角色引用 | 黄区（受控） |
| `enable` | `fenable` | 使用状态（0/1）· 标品权限链查询只取 `status=audit AND enable=1` 的规则 · 改 enable=0 会让规则从权限链立即消失 · 所有引用本规则的方案/角色同时失权 | 黄区（受控） |

**status/enable 联合判定公式**（标品权限链消费端）：`ruleEffective = (status == "audit") AND (enable == "1")`

### 1.3 核心字段（2 个 · L3 场景定制 · 改前必须懂业务）

| 字段 | physicalColumn | 改了影响 | 安全等级 |
|---|---|---|---|
| `entitynum` | `fentitynum` | **业务对象引用（BasedataField → bos_entityobject）** · 创建后只读（HRDataRuleEditPlugin#afterLoadData 永久 setEnable(false)）· 切换会清空 rule 字段 + 重建 FilterGrid（HRDataRuleEditPlugin#propertyChanged）· 如果绕过 FormPlugin 直接 DB 改 → rule 字段的 FilterCondition 引用的字段可能在新业务对象上不存在 → 权限链消费端 FilterBuilder.buildFilter 抛异常 | 黄区（受控 · 创建后禁止改） |
| `rule` | `frule` | **规则核心载体（TextField 存 FilterCondition.toJsonString）** · 改写了规则条件 · 权限链消费端下次查询时加载新条件 · 影响所有命中该规则的用户的可见数据范围 · 直接手改 JSON 字符串绕过 FilterBuilder 校验 → 可能引用不存在的字段 → 权限链崩 | 黄区（受控 · 必须走 FilterGrid 控件 + FormPlugin serialize） |

### 1.4 绿区字段（5 个 · 业务字段 · 可改影响小）

| 字段 | physicalColumn | 改了影响 |
|---|---|---|
| `number` | `fnumber` | 规则编码 · 影响列表排序/搜索 · 如果有下游系统按 number 硬编码引用则影响（不推荐） |
| `name` | `fname` | 规则名称（多语言）· 影响 UI 显示 / BI 报表 |
| `simplename` | `fsimplename` | 简称 · 影响列表列显示 |
| `description` | `fdescription` | 描述 · 纯展示 · 不影响业务 |
| `index` | `findex` | 排序号 · 影响列表排序 · 不影响权限 |

---

## 二、操作影响面（按 opKey 分组 · 基于 rules_chain_all.json 8 核心 opKey 富化）

### 2.1 高影响 op（红区 · 直接影响权限链）

| opKey | 动作 | OP 插件链 | 影响范围 | 回滚方式 |
|---|---|---|---|---|
| `delete` | 删除规则 | HRBaseDataStatusOp → CodeRuleDeleteOp → BdVersionSaveServicePlugin → HRBaseDataLogOp | 规则永久删除 · 引用本规则的 hrcs_dynascheme / hrcs_role 方案"指空" · 方案里的 permfilter 引用失效（不会级联删除方案） | 不可回滚（彻底删）· 只能重新建规则 + 重新挂到方案 |
| `audit` | 审核（status → C） | HRBaseDataStatusOp → HRBaseOriginalOp → HRBaseDataLogOp | 规则进入权限链生效 · 所有引用本规则的方案/角色立即生效 · 但标品 audit OP 不清缓存（实证）→ 下次访问规则才会重载 | unaudit 反审核可回滚 |
| `unaudit` | 反审核（status → B） | HRBaseDataStatusOp → HRBaseOriginalOp → HRBaseDataLogOp | 规则退出权限链 · 引用本规则的方案/角色失效 · 但标品 unaudit OP 不清缓存 | 重新 audit 可恢复 |
| `disable` | 禁用（enable → 0） | HRBaseDataEnableOp → HRBaseDataLogOp | 规则从权限链移除（等价软删除）· 同时写 disabler + disabledate · 标品 disable OP 不清缓存 | enable 重新启用可恢复 |
| `enable` | 启用（enable → 1） | HRBaseDataEnableOp → HRBaseDataLogOp | 规则重新进入权限链 · 清空 disabler / disabledate | disable 可回滚 |

### 2.2 中影响 op（黄区 · 影响规则状态但不直接改变权限）

| opKey | 动作 | OP 插件链 | 影响范围 |
|---|---|---|---|
| `save` | 保存规则 | FormPlugin HRDataRuleEditPlugin#doSave → OP HRDataRuleSaveOp(onAddValidators+beforeExecute+afterExecute) → HRBaseDataStatusOp → HRBaseDataLogOp → HRBaseDataEnableOp → HRBaseOriginalOp | rule 字段更新 + 权限缓存清（BS_HR_PERM_DATA_RULE / BS_HR_PERM_BD_DATA_RULE）+ 通知权限链（PermNotifyService.notifyByDataRule）+ 写 DataRuleLog（modify 模式）· 权限链立即生效 |
| `submit` | 提交（status → D） | HRBaseDataStatusOp → HRBaseOriginalOp → HRBaseDataLogOp | 规则进入"已提交"流程态 · 不等于生效（必须 audit 后才生效） |
| `unsubmit` | 撤销提交（status → A） | HRBaseDataStatusOp → HRBaseOriginalOp → HRBaseDataLogOp | 撤销提交 · 回到 draft 状态 |

### 2.3 低影响 op（绿区 · 不影响权限）

| opKey 分组 | 示例 | 影响 |
|---|---|---|
| new / modify / view | 新建/修改/查看表单 | 仅 UI 态 · 不进权限链 |
| copy / refresh / close | 复制/刷新/关闭 | 标品基础资料通用 · 不影响业务 |
| first / previous / next / last | 列表翻页 | 纯 UI |
| importdata_hr 系列 6 个 | 导入数据 | 批量建规则 · 每条建完后走 save OP 链 · 按需审核才生效 |
| exportlist 系列 4 个 | 导出 | 只读 · 不影响业务 |
| submitandnew / saveandnew | 保存并新建 | 同 save · 注意 save 才会序列化 rule |
| mobtoolbarselect / mobtoolbarcancel | 移动端选择 | 纯 UI |
| namehistory / namehistoryview / viewonelog / logview | 日志查看（donothing 路由） | 只读 · 不影响业务 |

---

## 三、配置层变更影响面（不改代码 · 纯改配置）

### 3.1 改 FilterCondition（不改 rule 字段的 JSON · 通过 FilterGrid 控件改）

**影响**：权限链消费端下次查询时加载新条件 → 改变用户可见的数据范围。

**关键约束**：
- 必须走 FilterGrid 控件 → FormPlugin doSave → FilterBuilder 校验 → 合法才能保存
- 不要直接 DB 改 frule 列 → 绕过 FilterBuilder 校验 → 规则可能引用不存在的字段
- save 后缓存在 afterExecute 清（仅 isChange=true 时）→ 权限链立即感知

### 3.2 给同一 entitynum 配多条规则

**影响**：多条规则共存 · 权限链按"业务方案"组合使用 → 权限层叠加可能产生意外的"缩权"或"扩权"效果。

**关键**：hrcs_dynascheme 的 permfilter 可引用多条规则（AND/OR 嵌套） · 删除任一条都不影响其他 · 但方案里的 permfilter 会"指空"。

### 3.3 导入批量建规则

**影响**：批量建 N 条规则 · 每条走后 save OP 链（校验 + 序列化 + 清缓存 + 通知）· 100 条约 5-10 秒 · 期间缓存被反复清 · 权限链可能短暂抖动。

---

## 四、代码层变更影响面（ISV 改了自定义扩展）

### 4.1 改了 ISV FormPlugin（如 CS-02 加字段联动）

| 改什么 | 影响 | 测试重点 |
|---|---|---|
| 加新 FormPlugin 并列挂 | 不影响标品 FormPlugin（HRDataRuleEditPlugin 仍在链上） | 验证两个插件均触发 · 顺序正确 |
| propertyChanged 里 setValue 其他字段 | 可能死循环（PR-004） | 验证 beginInit/endInit 包裹 |
| beforeF7Select 加 QFilter | 可能让 entitynum F7 过严 · 用户选不到业务对象 | 验证 QFilter 不跟标品 iscurrentversion=1 冲突（叠加而非替换） |

### 4.2 改了 ISV OP（如 CS-03 加 Validator）

| 改什么 | 影响 | 测试重点 |
|---|---|---|
| onAddValidators 加新 Validator | 跟标品 Validator 并列执行 · 任一报错都阻断 | 验证 ISV Validator 不拦截正常的 save |
| afterExecute 加缓存清理 | 权限链缓存清更频繁 · 可能产生缓存击穿 | 验证缓存清理 key prefix 不跟标品 BS_HR_PERM_* 冲突 |
| afterExecute 加 DataRuleLog | 日志量增大 · 但影响小 | 验证 dataRuleLogInit 失败不影响主事务（try-catch 包裹） |

### 4.3 改了 ISV BEC 发布（如 CS-05）

**影响**：
- afterExecute 阶段发 BEC · 主事务已提交 · BEC 失败不回滚
- 订阅方异步处理可能存在延迟（≥100ms）
- 新增事件号 ${ISV_FLAG}_hrcs_datarule_changed 需先在【业务事件管理】预配置

---

## 五、生产事故案例（基于实证分析 · 非真实生产事故 · 但都是实证发现的潜在风险）

### 5.1 案例 A · 数据规则被删除后方案"指空"

**场景**：管理员进 hrcs_datarule 列表 · 选中一条规则点删除 · 该规则正在被 3 个 hrcs_dynascheme 方案引用。

**发生了**：标品 delete OP 不会查下游引用（实证）· 删除成功 · 但 3 个方案的 permfilter 中 datarule 引用失效 → 方案里的"按规则过滤"条件变成空条件 → 可能授权超过预期范围的数据。

**为什么标品没拦**：hrcs_datarule 跟 hrcs_dynascheme 之间没有外键级联约束 · 删除规则不级联删除方案的 permfilter 条目 · 方案也不感知规则已被删。

**修复建议**：CS-04 自建 DeleteValidator · 删除前查 hrcs_dynascheme.permfilter + hrcs_role 子表是否引用本规则 ID。

### 5.2 案例 B · audit 后规则不立即生效

**场景**：管理员新建规则 → 保存 → 审核 · 立即用其他用户登录看被限制的 form 列表 · 发现还能看到规则之外的数据。

**根因**：标品 audit OP（HRBaseDataStatusOp）只改 status 字段 · **不清缓存**（实证：只有 HRDataRuleSaveOp.afterExecute 调了 HRPermCacheMgr.clearCache + PermNotifyService.notifyByDataRule · 而且只有 isChange=true 时才清）。

**发生时序**：
1. 用户 save 规则（此时 isChange=true → afterExecute 清缓存 + 通知）
2. 用户 audit 规则（标品 OP 不清缓存 → 权限链还在用老缓存）
3. 缓存 TTL 到期后（或下次有人 save 其他规则时）权限链重载 → 规则才生效

**修复建议**：CS-03 自建 AuditOp · 在 afterExecute 调 HRPermCacheMgr.clearCache + PermNotifyService.notifyByDataRule。

### 5.3 案例 C · 直接 SQL 改 frule 列绕过 FilterBuilder 校验

**场景**：运维人员直接 UPDATE t_hrcs_datarule SET frule = '{"filterRow":[...]}' WHERE fid = 12345。

**后果**：FilterCondition JSON 引用了不存在的字段 · 权限链消费端 FilterBuilder.buildFilter 抛 KDBizException → 该 entitynum 的业务对象列表完全打不开。

**为什么危险**：FormPlugin 的 doSave 会调 FilterBuilder 校验 · SQL 直写绕过了这一步 · 标品没有"在查询时也校验"的机制 · 导致权限链崩。

**修复**：DB 回滚 frule 列到修改前的值 · 或通过界面重新保存一次（走 FilterGrid + FormPlugin 正常路径）。

### 5.4 案例 D · 并发改同一规则 · 后写覆盖前写

**场景**：两位 HR 管理员同时打开同一规则编辑 · A 改了 FilterCondition 加一条 · B 改 FilterCondition 删一条 · A 先保存 → B 后保存 → B 把 A 的改动覆盖了。

**根因**：hrcs_datarule 不是 HisModel 时序基础资料 · 没有 version / boid 字段 · HRDataRuleSaveOp.compareFilterCondition 用 HRBaseServiceHelper.queryOne 查老值但不加锁 · last-writer-wins。

**修复建议**：CS-03 自建 Validator · 用 modifytime 做乐观锁（标品目前没做）。

---

## 六、版本升级影响面（苍穹 hrmp 升级前必检）

| 检查项 | 风险 | 说明 |
|---|---|---|
| hrcs_datarule formId 是否变 | 中 | 21 字段 physicalTable 变 → ISV 扩展字段丢失 · 需重新 modifyMeta |
| HRDataRuleEditPlugin 类名/父类 | 高 | 标品改 HRDataRuleEditPlugin 签名 → ISV 不要继承它（PR-001）· 并列挂不受影响 |
| HRDataRuleSaveOp 类名/父类 | 高 | 同上 · ISV 并列挂 HRDataBaseOp 不受影响 |
| FilterCondition / FilterBuilder 公开 API | 中 | 平台核心类 · 变了影响权限链消费端和 rule 序列化 |
| HRPermCacheMgr.clearCache 签名 | 中 | 变了影响 ISV CS-03 补的缓存清理 · 编译报错 |
| PermNotifyService.notifyByDataRule(Long) | 中 | 同上 |
| DataRuleLogServiceHelper 方法签名 | 低 | 变了影响 ISV CS-03 补的日志 · 编译报错 |
| HisModelServiceHelper.isInheritHisModelTemplate | 低 | @SdkPublic · 较稳定 |

---

## 七、影响面速查表（给 ISV 改代码前 30 秒看）

| 我想改... | 影响范围 | 影响人数 | 回滚难度 | 建议先看 |
|---|---|---|---|---|
| 加 ISV 字段（modifyMeta） | 仅该表单 | 0（只显示） | 低（删字段即可） | CS-01 |
| 加 FormPlugin 联动 | 仅该表单 | 0（只 UI） | 低（卸载插件） | CS-02 |
| 加 Validator 校验 | 对应 opKey | 该 opKey 的所有用户 | 低（卸 Validator） | CS-03 |
| 加 删除/禁用前查引用 | delete / disable op | 所有规则的管理员 | 低（卸 Validator） | CS-04 |
| 加 BEC 发布 | 对应 opKey | 订阅 BEC 的下游系统 | 中（发布和订阅都要回滚） | CS-05 |
| 加分录子表 | 该表单 + 权限链消费端 | 取决于是否参与权限计算 | 中（子表数据 + modifyMeta） | CS-06 |
| 改标品 FormPlugin 逻辑 | ❌ 不推荐（PR-001） | – | – | 07_ext_points |
| 直接 DB 改 frule 列 | 权限链消费端 FilterBuilder 崩 | 该 entitynum 所有用户 | 高（找原值恢复） | 本文 5.3 |
