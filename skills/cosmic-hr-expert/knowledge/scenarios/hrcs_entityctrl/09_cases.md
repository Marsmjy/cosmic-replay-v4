# 参考案例 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于 06_customization_solutions.md CS-01 ~ CS-07 + 标品反编译实证回填的"等价案例骨架"
> **confidence**: scaffold（实际客户项目代码路径 / 时间需要专家补 GitLab 链接）
> **数据源**: 反编译 + CS 推导 (2026-04-28)

---

## 案例索引

下表是从 06_customization_solutions.md 7 个 CS 推导出的"客户可能的真实需求模板"。**Claude 接到客户需求时 · 优先把需求映射到下面的案例 · 引用对应的 CS 答案**。GitLab 路径待业务侧专家根据真实项目补。

| 案例 | 业务背景 | 对应 CS | GitLab 路径 | 项目时间 |
|---|---|---|---|---|
| CASE-A | 业务对象映射加"映射用途分类"字段 | CS-01 | `<TODO 待补>` | `<TODO>` |
| CASE-B | 选了"组织相关业务对象"自动带出推荐维度 | CS-02 | `<TODO 待补>` | `<TODO>` |
| CASE-C | 防止同一字段在分录里配两次 | CS-03 | `<TODO 待补>` | `<TODO>` |
| CASE-D | 删除映射前查 hrcs_datarule 引用 | CS-04 | `<TODO 待补>` | `<TODO>` |
| CASE-E | 映射变更后通知 OA 系统刷新缓存 | CS-05 | `<TODO 待补>` | `<TODO>` |
| CASE-F | 接口直推批量增行时强制走 ID.genLongId | CS-06 | `<TODO 待补>` | `<TODO>` |
| CASE-G | TreeList 按 admingroup 过滤可见映射 | CS-07 | `<TODO 待补>` | `<TODO>` |

---

## CASE-A · 业务对象映射加"映射用途分类"字段

### 客户痛点（虚拟 · 等待真实客户场景填充）

某 HR 部门在配业务对象维度映射时 · 想用"标签"区分不同用途的映射 —— "重要业务对象"用"严控"标签 · "试运行"业务对象用"宽控"标签 · BI 团队需要按标签出统计报表。

### 解决方案

按 **CS-01** 加 `${ISV_FLAG}_remarktag`（MulComboField 多选 · 4 个枚举值）字段到主实体 `hrcs_entityctrl`。

### 关键事实

- 字段加完 · 标品 OP 不读不写它 · 完全 ISV 自治
- 字段值不进 hrcs_roledimension（只是分类标签 · 没业务含义）
- BI 报表 SELECT t_hrcs_entityctrl 时直接拿 `${ISV_FLAG}_remarktag` 列

### 验证步骤

1. 用 `cosmic_devportal_client` 跑 modifyMeta add field
2. 跑 getFormSchema("hrcs_entityctrl") 二次验证 · 看 fields 里有 `${ISV_FLAG}_remarktag`
3. 进 web 表单 · 看是否出现"映射用途分类" 多选下拉
4. 选几个值 · 保存 · SQL 查 t_hrcs_entityctrl WHERE id = X 看 ${ISV_FLAG}_remarktag 列值
5. 跑 BI SELECT 验证标签过滤可用

---

## CASE-B · 选了"组织相关业务对象"自动带出推荐维度（联动）

### 客户痛点（虚拟）

新员工不熟业务 · 配映射时不知道"haos_adminorg 应该用哪个维度"。希望根据业务对象类型自动带出推荐维度 · 减少人工选择错误。

### 解决方案

按 **CS-02** 自建 FormPlugin 挂 `hrcs_entityctrl` · `propertyChanged(entitytype)` 时根据 entitytype.modeltype / entitytype.number 推断 · setValue 自建辅助字段 `${ISV_FLAG}_recommendeddim`。同时在维度 F7 时**附加** 二级过滤（不要覆盖标品 6 分支）。

### 关键事实

- 必须 begin/endInit 包裹 setValue 防死循环（PR-004）
- 不能 `setValue("dimension", ...)` —— dimension 在分录子表 · 不是主表字段
- 不能继承 EntityCtrlEdit · 必须 extends HRDataBaseEdit（PR-001）

### 验证步骤

1. 部署自建 FormPlugin
2. 进表单 · 选"haos_adminorgdetail" 业务对象 · 看 ${ISV_FLAG}_recommendeddim 自动变 "管理域"
3. 选"hrpi_person" 业务对象 · 看 ${ISV_FLAG}_recommendeddim 自动变 "职位序列"
4. 在分录某行点 dimension F7 · 看是否有"客户业务线分组" 维度（标品 6 分支 + ISV 二级过滤共同生效）

---

## CASE-C · 防止同一字段在分录里配两次（save 校验）

### 客户痛点（虚拟）

业务对象映射的分录子表 · 如果用户手滑给"hrpi_person.org" 字段配了两次（一次配维度 A · 一次配维度 B）· 标品没拦截 —— 但运行时下游会取第一个还是第二个？规则不明确。

### 解决方案

按 **CS-03** 自建 OP 挂 save · `onAddValidators` 注册 `TdkwPropKeyDupValidator` · 在 validate 中遍历分录 · 用 HashSet 检查 propkey 重复 · 重复则 `addErrorMessage(row, "字段 X 出现多次 · 请保留唯一一行")`。

### 关键事实

- 必须挂 OP（不是 FormPlugin）· 才能拦住列表批量 save / 接口直推
- 用 addErrorMessage 行级错误 · 不要抛 KDBizException 整体失败
- propkey 大小写敏感（如 "ID" / "boid" 是大写 · 业务字段是小写）· 比较前确认是否 toLowerCase

### 验证步骤

1. 部署自建 OP
2. 进表单 · 加分录"hrpi_person.org + 维度 A" + "hrpi_person.org + 维度 B"
3. 点保存 · 看到错误 "字段 [hrpi_person.org] 在分录中出现多次 · 请保留唯一一行配置"
4. 删一行 · 再保存 · 成功
5. 接口直推同样数据 · 验证 OP 也能拦住

---

## CASE-D · 删除映射前查 hrcs_datarule 引用（防误删）

### 客户痛点（虚拟）

某次客户管理员误删了 "hrpi_person 业务对象映射"· 导致依赖此映射的 5 个数据规则失效 · 运行时静默 skip · 业务方反馈"权限规则莫名失灵"· 排查 1 周才定位到根因。

### 解决方案

按 **CS-04** 自建 OP 挂 delete · `onAddValidators` 注册 `TdkwDownstreamRefValidator` · 反查 `hrcs_datarule` + `hrcs_dynaformctrl` · 命中则 addErrorMessage("数据规则 X 引用此映射 · 请先清理")。

### 关键事实

- 标品 `EntityCtrlDelOp` 只联动清 hrcs_roledimension · 不查 datarule / dynaformctrl
- 反查用 HRBaseServiceHelper.queryOriginalArray 性能好（只取需要的列）
- 行级错误 addErrorMessage · 批量删时支持部分通过部分失败

### 验证步骤

1. 配一条 hrcs_datarule · 引用某个 entitytype
2. 部署自建 OP
3. 列表选这个 entitytype 的映射 · 点删除 · 看到错误"数据规则 [X] 引用业务对象 [Y] · 请先清理"
4. 删 datarule 后 · 重试删映射 · 成功

---

## CASE-E · 映射变更后通知 OA 系统刷新缓存（BEC）

### 客户痛点（虚拟）

OA 系统接了 hrcs 权限做"待办权限校验"· 缓存了"hrcs_entityctrl 当前所有映射"。每次 hrcs 改了映射 · OA 缓存 30 分钟才自动刷新（轮询）· 期间出现"权限不准"窗口。

### 解决方案

按 **CS-05** 自建 OP 挂 save / delete · `afterExecuteOperationTransaction` 调 `IEventService.triggerEventSubscribeJobs` 发 BEC 事件 · OA 端订阅消费立即刷新缓存。

### 关键事实

- 标品 entityctrl **没发 BEC**（grep 实证 0 处命中）· ISV 必须自建发布方
- eventNumber `${ISV_FLAG}_entityctrl_changed` 必须先在【开发平台 / 业务事件管理】预配
- 在 `afterExecuteOperationTransaction` 阶段（PR-010 阶段 9 · 主事务已提交）发布 · 不要在 `endOperationTransaction`（事务可能没提交）
- variables 用 `entityNumber`（业务对象编号 · 跨环境稳定）作业务键 · 不用 `entityCtrlId`（物理主键 · 跨环境不一致）—— 因为本场景非 HisModel · 没 boid

### 验证步骤

1. 在【开发平台 / 业务事件管理】注册 `${ISV_FLAG}_entityctrl_changed` eventNumber
2. 部署自建 OP（绑 save 和 delete 两个 opKey）
3. 改一条映射 · 看后台 BEC 日志 "BEC 发布成功 · entityCtrlId=X entityNumber=Y op=save"
4. OA 端订阅插件收到 KDBizEvent · variables 含 entityNumber · 立即刷缓存

---

## CASE-F · 接口直推批量增行时强制走 ID.genLongId（PR-005）

### 客户痛点（虚拟）

客户做了"按 id 范围分库" 的自定义路由（id ∈ [1000, 9999] 走主库 · id ∈ [10000, 99999] 走副库）。苍穹 ORM 自动生成的分录 id 是 19 位 Snowflake · 全部命中副库 · 主库永远没数据。要求 ISV 自建 OP 用更小的 id（业务自定义生成 · 但仍需保证唯一）。

### 解决方案

按 **CS-06** 自建 OP 挂 save · `beforeExecuteOperationTransaction` 阶段遍历 `dataEntities[].entryentity` · 给 id == 0 的行调 `ID.genLongId()`（Snowflake · 19 位）—— 但客户可以自己实现一个 `customIdGen()` 替换 · 保证 id 落入主库范围。

### 关键事实

- 必须在标品 `EntityControlSaveOp.beginOperationTransaction` 之前执行（按 PR-002 · ISV 扩展 plugin 可排标品前）
- OP 用 entity.set / entry.set（PR-003 · 不用 getModel）
- 不要用 UUID / currentTimeMillis · 用 ID.genLongId

### 验证步骤

1. 部署自建 OP
2. 接口直推一批分录（id 全为 0）· 看 SQL 落库的 id 是否走自建生成器
3. 看分库路由是否命中预期分库
4. 验证一致性：删除 / 修改时仍能按这些 id 找到行

---

## CASE-G · TreeList 按 admingroup 过滤可见映射（数据隔离）

### 客户痛点（虚拟）

客户有 5 个 HR 区域分公司 · 每个区域有自己的管理员组（admingroup）· 都用同一套 hrcs_entityctrl。区域 A 管理员看到了区域 B 配的映射 · 引发"为什么我没配的映射在我列表里"投诉。

### 解决方案

按 **CS-07** 自建 ListPlugin 挂 hrcs_entityctrl 列表（**继承 `AbstractTreeListPlugin`** · 因为标品是 TreeList）· 在 `setFilter` **and** 加自建 QFilter `bizapp IN (用户能看的 appId 列表)`。

### 关键事实

- 必须继承 `AbstractTreeListPlugin`（不是 `AbstractListPlugin`）
- 不要继承 `EntityCtrlTreeListPlugin`（PR-001）
- 不要 setFilter().clear() · 必须 and 进现有 filter（标品 entitytype.number is not null 不能丢）
- 不能加 `iscurrentversion=true` 过滤（本场景非 HisModel · 加了报错）
- 不能加 `enable=true` 过滤（本场景没有 enable 字段）

### 验证步骤

1. 部署自建 ListPlugin
2. 用区域 A 管理员账号登录 · 进列表 · 只看到 appId IN (区域 A appIds) 的映射
3. 用区域 B 管理员账号登录 · 进列表 · 只看到 appId IN (区域 B appIds) 的映射
4. 用超管账号登录 · 进列表 · 不限制（看到全部）

---

## AI 自动索引（骨架）

```bash
# 跑一次 search_historical_customizations.py 可填案例 GitLab 路径
python scripts/search_historical_customizations.py --scenario hrcs_entityctrl
```

⚠️ 当前未运行 · 待客户项目代码沉淀。Claude 应基于上述 7 个 CASE 框架先回答 · 待真实项目反馈后再升级 confidence: real_deploy。
