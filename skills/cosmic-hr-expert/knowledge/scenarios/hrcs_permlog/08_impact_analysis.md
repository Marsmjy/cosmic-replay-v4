# 08 变更影响面 · hrcs_permlog（HR 权限日志）

> permlog 是被动接收上游写入 + 提供只读查询的视图 · 影响面分析关键看：
> 1. 改 permlog 自己 → 影响什么
> 2. 改上游编辑场景（写日志路径） → 影响 permlog 什么
> 3. 改 permlog 元数据 / setFilter → 影响哪些下游消费者

---

## 1. 改本场景 permlog 会影响哪些下游

### 1.1 List 视图过滤变更（CS-02 解除 hashandle 强制过滤）

| 改什么 | 影响范围 | 级联动作 |
|---|---|---|
| 解除 setCustomQFilters hashandle='1' | List 主页面所有用户看到未处理日志 | 容量翻倍 · 显示性能下降 |
| HIES 导出 export_from_list_hr | 跟随 customQFilters · 导出范围扩大 | 客户认知"导出文件变大"会问 |
| HIES 导出 export_from_expttpl_hr | 同上 | 同上 |
| ISV 自建跨表查询（CS-06）| 不一定影响 · 看是否复用 List 过滤 | 建议独立 service 不复用 |

### 1.2 字段元数据变更

permlog **不能直接 modifyMeta** · 只能建 ISV 扩展元数据。如果误操作（违规直接改）：

| 改什么 | 影响范围 |
|---|---|
| 删主表字段 | 4 个详情子页（hrcs_permlog_role/userperm/datarule/bodimmapping）布局崩 |
| 改 hashandle 默认值 | 不影响（setFilter 强制 customQFilters）· 但客户感觉"配置不生效" |
| 加新分录子表 | 列表 SQL JOIN 变大 · 性能下降 |

### 1.3 反射 handlerclass 变更

| 改什么 | 影响范围 |
|---|---|
| 改 hrcs_permlogtype.handlerclass | processlog 反射调用走新 handler · 业务逻辑变 |
| 删除某 logtype | 已有 permlog 数据成"孤儿"（logtype.id 引用失效）· 4 路超链路由可能 fall through |
| 加新 logtype 编码不在 4 集合内 | 超链点击没反应（FP_HL2 4 路都不命中）|

### 1.4 准入规则变更

| 改什么 | 影响范围 |
|---|---|
| 删 HRAdminStrictPlugin | **11 hrcs 场景共用** · 全部失去准入闸 · 安全风险 |
| 改 isHrAdmin 实现 | HR 领域管理员判定变 · 可能误拒/误放 |
| 删 ListShowParameter.isLookUp 豁免 | 别的表单 F7 引用 hrcs_permlog 全部被拒 · 跨场景级联坏 |

---

## 2. 改上游编辑场景会影响 permlog 什么

### 2.1 上游 OP 插件改写日志写入逻辑

```
hrcs_userrole 编辑场景 OP 插件
        |
        | endOperationTransaction 调 PermLogServiceHelper
        v
t_hrcs_permlog 写入
```

| 改什么（上游） | 影响 permlog |
|---|---|
| 上游 OP 不再调 PermLogServiceHelper | permlog 列表少一个来源 · 历史趋势看不到 |
| 上游 OP 改 logtype 编码 | List 4 路 OR 超链路由可能 fall through |
| 上游 OP 改 operator 来源（用 ServiceContext 而非 RequestContext）| permlog operator 字段值不一致 |
| 上游 OP 漏写 influusernumber | List 影响用户数列展示空 |

### 2.2 上游编辑场景 BEC 发布变更（CS-04 桥接相关）

| 改什么 | 影响 permlog |
|---|---|
| 上游加 BEC 发布 | ISV CS-04 订阅可正常接收 · 不影响 permlog 自己 |
| 上游标品已发 BEC + ISV 又发 | 双发风暴 · ISV 订阅插件收 2 次 · 必须幂等（参考 feedback_bec_3layer_async_publish.md）|

---

## 3. 改 permlog 元数据 / setFilter 影响下游消费者

### 3.1 4 个详情子页

| 改什么 | 影响 |
|---|---|
| 改主表字段 key | 详情子页 4 个 formId 都要同步改 · 否则布局错位 |
| 改 logtype 编码集 | PermLogListPlugin.showDetail 4 路分支 fall through · 详情打不开 |
| 详情子页 OperationStatus.VIEW 强制只读 | 客户问"为什么改不了" → 解释这是设计 |

### 3.2 hrcs_permlogarchive_set 子页

| 改什么 | 影响 |
|---|---|
| 子页归档配置改字段 | hrcs_permlog 自己不感知 · 但 ISV 调度任务（CS-07）可能要适配 |
| 删除子页 | donothing_archiveset 弹页 fall · showLogSetting 报错 |

### 3.3 HIES 导出/导入消费者

| 改什么 | 影响 |
|---|---|
| 改主表字段名 | 导出模板（导出模板基础资料配的字段）失效 · 导出报错 |
| 加新字段 | 导出模板要重新选列 · 否则不导出新字段 |

### 3.4 跨场景查询消费者（ISV CS-06 等）

| 改什么 | 影响 |
|---|---|
| 改 t_hrcs_permlog 物理表名 | 所有 SQL 直查 break |
| 改子分录前缀（baseinfo_/rolefunc_ 等）| ISV 自定义查询 break |

---

## 4. 跨模块 / 跨场景影响清单（重点）

### 4.1 影响 11 hrcs 场景（HRAdminStrictPlugin 共用）

参见 _shared/_decompiled/HRAdminStrictPlugin.java（其他场景也用）：
- hrcs_permlog
- hrcs_dynascheme
- hrcs_userrole
- hrcs_role
- hrcs_userpermfile
- hrcs_datarule
- hrcs_dimension
- hrcs_permitem
- hrcs_admingroup
- hrcs_bizapp
- hrcs_entitytype

⚠ 改 HRAdminStrictPlugin 等于改 11 个场景 · 不要轻易动

### 4.2 影响 hrcs_permlogtype 基础资料消费者

- PermLogListPlugin.beforeDoOperation processlog（反射调用）
- PermLogListPlugin.showDetail（4 路超链路由）
- 可能其他 ISV 扩展 handler

### 4.3 影响 BEC 业务事件订阅链（如 ISV CS-04 接入）

- ISV 自建订阅插件
- 外部 SOC / Splunk 系统

---

## 5. 生产事故案例（推断 · 基于代码风险点）

### 5.1 案例 1 · setFilter 4 路 OR 误用导致全表扫

**场景**：ISV 加自定义快速搜索字段 · 没遵守 quickSearch 协议（property='1' value 用 # 分两段）· 导致 setFilter 直接走 super 默认

**影响**：用户输入关键字 → 全表 LIKE 模糊匹配 → 数据库 13 张表 LEFT JOIN 性能塌方 → 列表加载超时

**根因**：ISV 没看 PermLogListPlugin.setFilter L222-L242 实证 · 误以为标品自动支持

**修复**：按 FP_SF1 协议注入字段集 · value 用 #/  分隔正确

### 5.2 案例 2 · 解除 hashandle 强制过滤后 HIES 导出爆量

**场景**：CS-02 解除 hashandle 过滤 · 默认看全部日志 · 客户用 export_from_list_hr 导出

**影响**：标品时一天 1000 条已处理日志 · 解除后是 50000 条全量日志 · 导出 50 万行 Excel · 客户机器卡死

**根因**：导出跟随 customQFilters · ISV 没考虑

**修复**：CS-02 ISV 角色判定 · 仅 IT 安全审计员豁免 · 普通 HR 管理员仍只能看已处理

### 5.3 案例 3 · 反射 handler 抛 ClassNotFoundException 无日志告警

**场景**：客户升级苍穹 · 删除了 ISV 自建 handler 类 · hrcs_permlogtype 基础资料里 handlerclass 字段还指向旧类

**影响**：用户点 processlog → catch ClassNotFoundException → showError "exception: ..." → setCancel · **没人监控** · 用户反馈"系统坏了" · 但没日志告警

**根因**：标品反射调用失败仅 LOGGER.error 不触发告警通知

**修复**：ISV 在 PermLogListPlugin **之前**挂插件 · 拦 processlog 时先 ServiceLoader 验证 handler 存在 · 缺失主动告警

### 5.4 案例 4 · 上游 OP 双发 BEC（标品 + ISV 一起发）

**场景**：苍穹某版本标品上游已经发了 BEC（hrcs_userrole_changed）· ISV 不知情 · CS-04 在同一阶段又发一次

**影响**：订阅方收到两次同样事件 · SOC 端日志重复 / 告警频次翻倍

**根因**：ISV 没扫上游反编译看是否标品已发 BEC

**修复**：动手前**必须** grep 上游 Service + *MsgTask · 参考 feedback_bec_3layer_async_publish.md

---

## 6. 改动前 checklist

### 改 permlog 元数据/字段前

- [ ] 是不是标品 formId · 是 → 走 ISV 扩展元数据 · 否 → 直接 modifyMeta（前提是 ISV 拥有）
- [ ] 改字段会不会破坏 4 个详情子页布局
- [ ] 字段命名遵守 _l 多语言/`f<key>id` 物理列规则
- [ ] 改 hashandle 默认值 · 实际不生效（setFilter 强制覆盖）

### 改 setFilter 前

- [ ] super.setFilter 必须先调
- [ ] removeIf hashandle 严格 property 匹配
- [ ] 必须保留 4 路 OR 拆解逻辑（permfile/rolename）
- [ ] 验证 HIES 导出范围跟随影响

### 改上游 OP 前（写日志路径）

- [ ] 是用 endOperation（强一致）还是 afterExecute（容忍丢日志）· 业务级别决定
- [ ] PermLogServiceHelper / PermLogTaskServiceHelper 不要绕过 · 直接 SaveServiceHelper.save 会丢冗余字段
- [ ] BEC 发不发 · 先 grep 标品反编译

### 改 hrcs_permlogtype 基础资料前

- [ ] handlerclass FQN 有没有 ServiceLoader 验证 ClassLoader 能加载
- [ ] 编码不要复用 1010-4020 标品段 · ISV 用 9000+
- [ ] 加新编码后 4 路超链路由要对应（要么扩 4 集合 · 要么不挂详情）

---

## 7. 引用文件

- `_decompiled/scenarios/hrcs_permlog/PermLogListPlugin.java` · 实证
- `_decompiled/scenarios/hrcs_permlog/HRAdminStrictPlugin.java` · 跨场景影响
- `06_customization_solutions.md` · 7 个 CS 的影响面
- `feedback_bec_3layer_async_publish.md` · BEC 双发陷阱
- `_shared/platform_rules.json` PR-001/010/011
