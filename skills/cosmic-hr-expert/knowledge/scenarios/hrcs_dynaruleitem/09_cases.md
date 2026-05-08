# 参考案例 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于反编译 3 类实证 + 7 个 CS 方案推导的典型扩展案例
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 案例 1 · 加"参数分类"字段并联动默认值（CS-01 + CS-02 组合）

### 客户场景

某大型制造企业 HR 部门配置了 80+ 条规则参数项。管理员反馈列表太长，需要按"组织维度 / 人员维度 / 岗位维度"分类过滤。

### 实施要点

1. **modifyMeta add field**：给 `hrcs_dynaruleitem` 加 ComboField `${ISV_FLAG}_paramcategory`（3 个枚举值）
2. **FormPlugin propertyChanged**：监听 `datatype` 切换 —— org 自动设"组织维度"、enum 不显示参数分类
3. **ListPlugin setFilter**：默认只显示"组织维度"分类（最常用）
4. **列表模板加列**：在 hrcs_dynaruleitem 列表模板加 `${ISV_FLAG}_paramcategory` 列

### 标品联动注意事项

- `datatype` 字段 `isvCanModify=false` —— 不能改它的必填属性，只能新加 ISV 字段
- 标品 `propertyChanged` 只处理 entitytype 锁值，不动 ISV 字段 —— ISV 并列挂不会冲突
- 本场景非 HisModel —— 字段加在 `t_hrcs_dynaruleitem` 主表，不需要考虑 boid 版本隔离

---

## 案例 2 · 枚举子表加"枚举值描述"列（CS-06 + CS-02 组合）

### 客户场景

HR 管理员配置"部门类型"枚举值（value=`SALES`, displayvalue=`销售部门`）后，业务方问"SALES 和 MARKETING 有什么区别？"。需要在枚举子表加一列长文本描述。

### 实施要点

1. **modifyMeta add field to entryentity**：给分录子表加 TextField `${ISV_FLAG}_enumdesc`（长度 500 · 非必填）
2. **不需要加 Validator** —— 这是纯展示字段，不影响下游 dynascheme.condition
3. **列表显示**：在 entryentity 网格里加 `${ISV_FLAG}_enumdesc` 列

### 注意

- 标品 `FP_ABD4` 只锁已有行的 `value` 列 —— ISV 新加的 `${ISV_FLAG}_enumdesc` 列始终可编辑
- 不要动 `value` 列的锁（setEnable(false)）—— 那是保护 dynascheme 引用脱钩的红线

---

## 案例 3 · 微服务取值类参数项的 BEC 通知（CS-05）

### 客户场景

企业有 30 条 `valsourcetype=2`（微服务取值）的规则参数项。微服务部署在独立的 k8s 集群中。当规则参数项的 `mserviceclass` 被修改后，需要通知微服务集群刷新本地配置缓存。

### 实施要点

1. **自建 OP**：继承 `HRDataBaseOp`，覆盖 save opKey
2. **afterExecute 发 BEC**：`IEventService.triggerEventSubscribeJobs("hrcs", "isv_dynaruleitem_mservice_changed", evt)`
3. **事件体**：`paramId + oldMserviceClass + newMserviceClass + opType`
4. **订阅方**：独立微服务实现 `IEventServicePlugin`，收到事件后刷新 `ConcurrentHashMap` 缓存

### 重要前提

- 标品 hrcs_dynaruleitem **0 处 BEC 发布**（grep 反编译 3 类全 0 命中）—— ISV 必须自建发布方
- 事件号必须 ISV 自定义（如 `isv_dynaruleitem_mservice_changed`）——不可与标品事件号冲突
- `afterExecute` 在事务提交后执行 —— 可以安全反查已落库的最新数据

---

## 案例 4 · ISV 自建表引用规则参数项 · 删除阻断（CS-04）

### 客户场景

ISV 开发了"方案审批工作流"模块，其中 `t_isv_approval_config` 表有字段 `fparam_id` 引用 `t_hrcs_dynaruleitem.fid`。当管理员尝试删除某条规则参数项时，需要同时检查标品 dynascheme 引用 + ISV 自建表引用。

### 实施要点

1. **自建 OP**：继承 `HRDataBaseOp`，覆盖 delete opKey
2. **onAddValidators 注册 Validator**：查询 `SELECT COUNT(*) FROM t_isv_approval_config WHERE fparam_id = ?`
3. **并行执行**：标品 `DynaItemDelValidator` + ISV Validator **并列执行** —— 任一报错都阻断删除

### 注意

- `DynaItemDelValidator` 没有 @SdkPublic —— **不能继承它**，只能并列注册自己的
- 如果还需要在**删除枚举行（deleteentry）**时检查，必须在 FormPlugin 的 `beforeDoOperation` 里做（不走 OP 链）

---

## 案例 5 · 规则参数项列表过滤优化（CS-07）

### 客户场景

HR 管理员日常只需要查看 datatype=bd（基础资料类型）的参数项。目前列表显示全部 80+ 条，需要滚动多页。

### 实施要点

1. **自建 ListPlugin**：继承 `HRDataBaseList`（非场景专属类 —— dynaruleitem 没有场景专属 ListPlugin）
2. **setFilter 默认过滤**：`evt.getQFilters().add(new QFilter("datatype", "=", "bd"))`
3. **加快速切换按钮**：在列表工具栏加 3 个按钮（全部 / 基础资料 / 组织 / 枚举）—— 每个按钮触发不同 filter

### 注意

- 标品 `HRBaseDataCommonList` 是 hrcs 11 表单共用列表插件 —— ISV ListPlugin **并列挂**，不能移除标品插件
- 列表过滤用 `getQFilters().add`（叠加）—— 不要用 `setFilter`（覆盖标品过滤）

---

## 案例 6 · 预置参数项的数据修复（特殊场景）

### 客户场景

系统升级后，发现某条 `issyspreset=true` 的标品参数项的 `entitytype` 字段在 DB 中变成了 null（疑似升级脚本漏写）。但因为 `issyspreset=true` 触发 FP_ABD3 强制 VIEW，UI 上无法编辑。

### 处理方式

1. **确认是标品 bug**（不是 ISV 改坏的）
2. **DB 直改**（唯一可行方式 —— UI 强制 VIEW 锁死）：`UPDATE t_hrcs_dynaruleitem SET fentitytype = 'haos_adminorghrf7' WHERE fissyspreset = true AND fentitytype IS NULL`
3. **不建议 ISV 自建绕过 VP_ABD3 的插件** —— 预置数据保护是设计意图

### 教训

- 预置参数项的字段值修复只能走 DB 脚本 —— 这是标品的设计保护
- ISV 永远不要试图绕过 `issyspreset` 保护
