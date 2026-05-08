# 11 上下游联动 · hrcs_permlog（HR 权限日志）

> permlog 是被动接收上游写入 + 提供只读查询的视图 · 上下游关系特别清晰
> 关键："上游"是写入方（多个 hrcs 编辑场景）· "下游"是消费方（List 视图 / HIES 导出 / ISV 自建审计）

---

## 1. 上游（写入方）

### 1.1 hrcs 编辑场景全集

permlog 接收以下 hrcs 编辑场景的日志写入：

| 上游场景 | logtype 编码段 | 触发 opKey | 写入路径 |
|---|---|---|---|
| hrcs_role 角色编辑 | 1010-1040 | save/audit/disable | OP.endOperation 调 PermLogServiceHelper |
| hrcs_userrole 用户角色 | 1050/1060 | save/disable | 同上 |
| hrcs_userpermfile 用户权限档案 | 2010-2095 | save/audit/effect | 同上 |
| hrcs_datarule 数据规则 | 3015 | save/audit | 同上 |
| hrcs_dynascheme 动态授权方案 | 1010 等共用 | confirmchange/save/audit | 同上 |
| 业务对象维度映射场景 | 4010-4020 | save/audit | 同上 |

### 1.2 上游写入 API

主要 helper 类（**实证调用**）：
- `kd.hr.hrcs.bussiness.servicehelper.perm.log.PermLogServiceHelper`
- `kd.hr.hrcs.bussiness.servicehelper.perm.log.PermLogTaskServiceHelper`

ISV 写自建编辑场景的日志 · 也应用这些 helper · 不要绕过

### 1.3 上游写入时机

| 阶段 | 写入策略 | 一致性 |
|---|---|---|
| `endOperationTransaction` | 跟主业务同事务 · 主业务回滚则日志一起回滚 | **强一致**（推荐） |
| `afterExecuteOperationTransaction` | 主事务已提交后写 · 写日志失败不回滚主业务 | 弱一致（容忍丢日志）|

标品 hrcs 多用 endOperation · 保强一致

### 1.4 上游 BEC 发布（CS-04 触发点）

如果 ISV 要 CS-04 推送日志到外部 SOC · 必须在上游编辑场景 OP 的 afterExecuteOperationTransaction 阶段挂 BEC 发布插件

⚠ 实施前必须 grep 上游标品反编译看是否已发 BEC（避免双发 · feedback_bec_3layer_async_publish.md）

---

## 2. 下游（消费方）

### 2.1 List 视图（最高频消费）

```
PermLogListPlugin.setFilter
        |
        | hashandle='1' 强制 + 4 路 OR + 用户过滤
        v
ORM JOIN 13 表
        v
List 渲染 + packageData 阶段（ISV 可挂脱敏）
```

### 2.2 4 个详情子页（被超链路由触发）

| logtype.number 集合 | 详情 formId | 业务含义 |
|---|---|---|
| {1010-1040} | hrcs_permlog_role | 角色变更详情 |
| {1050/1060/2010-2095} | hrcs_permlog_userperm | 用户权限详情 |
| {3015} | hrcs_permlog_datarule | 数据规则详情 |
| {4010-4020} | hrcs_permlog_bodimmapping | 维度映射详情 |

### 2.3 归档子页

`hrcs_permlogarchive_set` · 配置归档周期 · 跟 hrcs_permlog 解耦（独立子页面）

### 2.4 HIES 导出

| opKey | 用途 |
|---|---|
| export_from_list_hr | 按列表当前过滤导出（**实际高频**）|
| export_from_expttpl_hr | 按预定义导出模板导出 |
| export_from_impttpl_hr | 按导入模板格式导出 |
| show_export_record_hr | 查看导出记录 |

⚠ 导出范围跟随 setCustomQFilters · CS-02 解除 hashandle 过滤会同步影响导出

### 2.5 processlog 反射调用 → handler

```
PermLogListPlugin.beforeDoOperation processlog
        |
        | 反射 Class.forName(handlerclass).newInstance().doHandler(logId)
        v
hrcs_permlogtype 基础资料配置的 handler 类
        |
        v
handler 自定义业务（推送告警/翻转 hashandle/写自有审计表）
```

### 2.6 ISV 自建消费方（潜在）

- ISV 跨场景日志归集（CS-05）· 通过 BEC 桥接同步到 ISV 聚合表
- ISV 跨表查询服务（CS-06）· 直接 QueryServiceHelper 查 hrcs_permlog
- ISV 归档调度任务（CS-07）· 按策略归档/清理

---

## 3. 跨 HR 子域联动

### 3.1 与 hrpi（人事）联动

| 联动点 | 同步/异步 | 失败策略 |
|---|---|---|
| 员工权限变更（用户授权场景）→ permlog 写入 | 同步（OP endOperation）| 主事务回滚连带日志回滚 |
| permlog 显示 operator/influuser 的 user 信息 → 反查 bos_user | 列表读取时同步查 | 用户已停用仍显示 user 名称 |
| ISV 想根据员工档案过滤日志 | 通过 permfile 关联 hrcs_userpermfile · 再到 hrpi_employee | JOIN 路径长 · 性能优化 |

### 3.2 与 perm（权限）联动

| 联动点 | 详情 |
|---|---|
| permlog 引用 perm_role · perm_permitem · perm_admingroup | BasedataField 引用 · 一致性由 perm 域负责 |
| 角色删除 → permlog 中引用 fall through | 标品没强制约束 · 可能成为孤儿引用 |
| 数据规则删除 → permlog.datarule 引用 fall through | 同上 |

### 3.3 与 bos（基础平台）联动

| 联动点 | 详情 |
|---|---|
| permlog 引用 bos_user / bos_org_biz / bos_devportal_bizapp / bos_entityobject | 跨平台 BasedataField |
| 用户停用 · 历史日志保留（不级联）| 标品行为 · ISV 不可改 |
| 苍穹升级 · bos_user 字段调整 | permlog 跟随 bos_user 元数据 · 一般无感 |

### 3.4 与考勤/薪酬域联动

permlog 不直接联动这些域 · 因为它是**HR 通用服务/权限管理**模块的日志视图。但有间接：

- 考勤/薪酬域的角色权限变更（在 hrcs_role 编辑） → 写 permlog
- ISV 想做"按域过滤日志"（CS-02）· 在 logtype 编码集映射

---

## 4. ISV 自建场景接入 permlog 的标准模式

### 4.1 ISV 自建编辑场景 → 写日志到 permlog

```
ISV 自建编辑场景 OP 插件
        |
        | endOperationTransaction
        v
PermLogServiceHelper.recordXxxLog(logtype=ISV 自定义编码 9001+, ...)
        v
t_hrcs_permlog 写入
```

注意：
- ISV 自定义 logtype 编码必须先在 hrcs_permlogtype 基础资料注册
- handlerclass 必须 ISV 自建类 · 在 jar 里能 Class.forName
- 写入时填 operator / operationtime / hashandle=0 / influusernumber

### 4.2 ISV 想从 permlog 拉数据 → 跨表查询服务

参考 CS-06 · 使用 QueryServiceHelper.queryDataSet · 时间窗 + JOIN 拼

### 4.3 ISV 想把 permlog 推到外部 → BEC 桥接

参考 CS-04

---

## 5. 同步 vs 异步联动总览

| 链路 | 同步 / 异步 | 失败策略 |
|---|---|---|
| 上游 OP endOperation → permlog 写入 | **同步**（同事务）| 主业务回滚 |
| 上游 OP afterExecute → permlog 写入 | 异步（事务外）| 容忍丢日志 |
| permlog List 读取 | 同步 | 拒绝（HRAdminStrictPlugin 拒）|
| processlog → 反射调 handler | 同步（FormPlugin 阶段）| handler 自负责 |
| ISV CS-04 BEC 推送 SOC | 异步（BEC 框架）| 重试 + 容错 |
| ISV CS-07 调度任务归档 | 异步（每天 02:00）| 重试 |
| ISV CS-05 跨场景日志归集 | 异步（BEC）| 重试 |

---

## 6. 失败回滚策略

### 6.1 主业务 + permlog 写入失败（同事务）

```
事务开始
  -> hrcs_userrole 主业务 update
  -> permlog insert (PermLogServiceHelper)
  -> 任意一步失败
事务回滚 → 主业务 + permlog 同时回滚
```

### 6.2 主业务成功 + permlog 写入失败（afterExecute 异步）

```
主业务 commit
  -> permlog insert
  -> 失败抛异常
LOG.error · 主业务保留 · 日志丢失（接受这个风险）
```

### 6.3 BEC 推送失败

```
afterExecute · BEC.triggerEventSubscribeJobs 失败
  -> 框架重试机制（默认 3 次）
  -> 仍失败 · 落 BEC 失败队列 · 客户运维介入
```

---

## 7. permlog 在整个 HR 域的位置

```
[业务编辑层]                      [日志层]                    [审计层]
hrcs_role         --[写]-->                                 --[查]-->
hrcs_userrole     --[写]-->     hrcs_permlog                --[导出]-->  HIES 导出
hrcs_userpermfile --[写]-->     (单视图聚合 13 表)          --[BEC]-->   ISV SOC
hrcs_datarule     --[写]-->                                 --[处理]--> processlog handler
hrcs_dynascheme   --[写]-->
ISV 自建场景      --[写]-->

         |                                                    |
         | (BEC 桥接 · CS-04/CS-05)                           |
         +<-------- ISV 自建审计聚合 isv_xxx_unifiedauditlog -+
```

---

## 8. 引用文件

- `_decompiled/scenarios/hrcs_permlog/PermLogListPlugin.java`
- `_decompiled/scenarios/hrcs_permlog/HRAdminStrictPlugin.java`
- `06_customization_solutions.md` CS-04/05/06/07
- `_shared/platform_rules.json` PR-001/010/011
- `feedback_bec_3layer_async_publish.md`

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

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

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
