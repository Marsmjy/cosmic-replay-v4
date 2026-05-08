# 05 数据流转 · hrcs_permlog（HR 权限日志）

> permlog **不是 HisModel** · 没有 t_*_his 历史表 · 没有 boid/iscurrentversion · 没有 PR-008/PR-009 时序约束
> 它是普通日志单据：上游写一次 + List 读多次 · 跨 13 张物理表 JOIN

---

## 1. 数据落地链路

### 1.1 写入路径（permlog 不在 hrcs_permlog 表单内写 · 由上游编辑场景写）

```
上游编辑场景 (hrcs_userrole/hrcs_role/hrcs_datarule/hrcs_dynascheme 等)
    | OP 插件 endOperation/afterExecute 阶段
    v
PermLogServiceHelper / PermLogTaskServiceHelper (hrcs 业务 helper)
    | 1. 构造 t_hrcs_permlog 主表行
    |    - logtype = 1010 / 1050 / 3015 / 4010 等编码（按业务类型）
    |    - operator = RequestContext.getCurrUserId()
    |    - operationtime = NOW()
    |    - hashandle = 0  (默认未处理)
    |    - influusernumber = 计算受影响用户数
    |    - beforeopdata / afteropdata = JSON.toJsonString(变更前/后实体)
    | 2. 构造分录子表行（按 logtype 决定写哪些子表）
    |    - 角色变更 -> rolefunc/roledim/rolefield/roleopen/influuser
    |    - 用户授权 -> influuser
    |    - 数据规则 -> rangedr/rangebddr
    |    - 维度映射 -> influrole/rangebiz
    | 3. SaveServiceHelper.save 或 OperationServiceHelper.executeOperate
    v
t_hrcs_permlog 主表 + 12 子表（JOIN 写）
    + t_hrcs_permlog_l (description 多语言)
```

> **关键**：写 permlog 是上游编辑场景"OP 插件"的副产品 · 跟 hrcs_permlog 表单的 save opKey **解耦**。hrcs_permlog 自己的 save opKey 在标品里**实际 0 调用**（rules_chain_all.json save · 没业务）。

### 1.2 读取路径

#### 1.2.1 List 查询（高频）

```
PermLogListPlugin.setFilter
    | 用户输入 + 平台默认 customQFilters
    v
QFilter:
   - hashandle = '1'  (FP_SF5 强制)
   - 用户过滤（可能 4 路 OR 拆解）
    v
ORM 拼 SQL:
   SELECT main.fid, main.foperatorid, main.flogtypeid,
          main.foperationtime, main.fhashandle,
          baseinfo.fchangefield, baseinfo.fbeforedata,
          rolefunc.fbizappid, rolefunc.fpermitemid,
          influuser.fpermfileid, ...
   FROM t_hrcs_permlog main
   LEFT JOIN t_hrcs_permlogbaseinfo baseinfo ON ...
   LEFT JOIN t_hrcs_permlogrolefunc rolefunc ON ...
   LEFT JOIN t_hrcs_permloginfluuser influuser ON ...
   ... (按需 JOIN 12 张子表)
   WHERE main.fhashandle = '1' AND ...
   ORDER BY main.foperationtime DESC
```

#### 1.2.2 单条详情查询

```
PermLogListPlugin.showDetail
    | pkId 主键
    v
HRBaseServiceHelper("hrcs_permlog").queryOne("logtype", pkId)
    | 取 logtype 字段
    v
按 logtype.number 路由到 4 个 detail formId
```

#### 1.2.3 processlog 反射调用

```
PermLogListPlugin.beforeDoOperation
    | logId
    v
PermLogServiceHelper.getPermLog(logId)  -> 查整条 permlog 实体
    + PermLogTaskServiceHelper.getAllPermLogType()  -> 查所有 logtype 基础资料
    | 取 handlerclass 字符串
    v
反射:
   Class.forName(handlerclass)
   .newInstance()
   .getMethod("doHandler", long.class)
   .invoke(instance, logId)
    | handler 内部:
    |   - 查 permlog 行
    |   - 处理业务（推送下游/告警/状态翻转）
    |   - 更新 hashandle = 1（一般）
    |   - 自己负责事务
    v
返回 Object 结果（标品代码忽略 · 只关心异常）
```

---

## 2. 事务边界

### 2.1 写入事务（上游场景控制）

- 上游编辑场景的 OP 插件在 `endOperationTransaction` 或 `afterExecuteOperationTransaction` 触发写日志
- **endOperation**：和主业务同事务 · 主业务回滚则日志一起回滚（**推荐**）
- **afterExecute**：主事务已提交后 · 写日志失败不回滚主业务（容忍丢日志）
- 标品 hrcs 多用 endOperation · ISV 跟随

### 2.2 读取事务（permlog 自己）

- List setFilter / showDetail / billListHyperLinkClick 全部**不进事务**（FormPlugin 阶段）
- processlog 反射调用 handler · handler 自己负责事务

### 2.3 processlog 处理事务

- handler.doHandler(logId) 自定 TXR · 标品代码不开事务 · ISV 实现 handler 时自己用 `TXHandle txh = TX.requiresNew()` 包裹 · 失败回滚

---

## 3. 历史表写入时机

**permlog 没有 t_hrcs_permlog_his 时序表**（grep `iscurrentversion|HisModel|boid` 0 命中）

- 不像 haos_adminorg / hbjm_jobhr / hrpi_empjobrel 这些 HisModel 实体有 _his 表
- permlog 只有"当前最新一份"概念 · 一条日志一行 · 不再修改（理论上 hashandle 由 0 翻 1 是唯一修改点）
- **不要假设 permlog 有历史版本** · 直接 select 主表即可

---

## 4. 失败回滚策略

### 4.1 写入失败

| 场景 | 行为 |
|---|---|
| 上游主业务 commit 后写日志失败 | 主业务保留 · 日志丢失（afterExecute 模式）|
| 上游主业务 endOperation 阶段写日志失败 | 主业务回滚 · 日志一起回滚（endOperation 模式 · 保一致）|
| 上游主业务成功 + 日志成功 + 影响用户子表插入失败 | 整条 permlog 回滚 · 主业务也回滚（同事务）|

### 4.2 processlog 处理失败

| 场景 | 行为 |
|---|---|
| logtype.handlerclass 字段为空 | logTypeMap.containsKey 走 false · continue 跳过（不报错）· **客户感觉没反应** |
| handlerclass 不存在的类 | Class.forName 抛 ClassNotFoundException · 被 catch · showError "exception:..." · setCancel(true) |
| handlerclass 类无 doHandler(long) 方法 | NoSuchMethodException · 同上 |
| handler.doHandler 内部抛异常 | InvocationTargetException · 同上 |
| 多选 processlog（标品禁止）| beforeDoOperation 单选校验 · setCancel(true) · 提示 'please selected 1 row.' |

---

## 5. ISV 数据流扩展点

### 5.1 字段隐藏（非超管不可见敏感日志字段）

```
ISV 并列挂 ListPlugin · 实现 afterCreateNewData 或 afterBindData
    if !RequestContext.get().isAdmin():
        getView().setEnable(false, "beforeopdata", "afteropdata")
        // 或 setVisible(false, ...)
```

### 5.2 跨场景日志归集

```
ISV 并列挂 hrcs_role / hrcs_userrole 等编辑场景的 OP 插件
    afterExecuteOperationTransaction:
        // 1. 标品已写 t_hrcs_permlog（在 endOperation 内）
        // 2. ISV 把日志再推到自建审计平台 / Kafka
        IEventService.triggerEventSubscribeJobs("hrcs", "isv_permlog_emit", msg, vars)
        // 3. ISV 自建订阅插件接收 -> 推到外部审计系统
```

### 5.3 解除 hashandle 强制过滤

```
ISV 并列挂 ListPlugin (顺序在 PermLogListPlugin 之后)
    setFilter:
        // 标品已 setCustomQFilters([hashandle='1'])
        arg.setCustomQFilters(new ArrayList())  // 清空
        // 然后按 ISV 业务再加自定义过滤
        // 注意：会影响 export_from_list_hr / export_from_expttpl_hr 导出范围
```

---

## 6. 性能要点

| 链路 | 性能瓶颈 | 优化建议 |
|---|---|---|
| List setFilter 4 路 OR 拆解 | 多个 OR 子查询并行 | 必须传 operationtime 时间窗（≤ 90 天）|
| 13 张表 LEFT JOIN | JOIN 数量大 | 在 t_hrcs_permlog(operationtime, hashandle) 加复合索引 |
| beforeopdata/afteropdata 大文本 | TextField 跨网络传 | List 列里不要展示 · 详情页才取 |
| description 多语言 _l 表 | 跨语言查询慢 | 同 hashandle 索引带上语言 |
| processlog 反射调用 | Class.forName 走 ClassLoader 链 | handler 类加载缓存 |

---

## 7. 引用文件

- `_decompiled/scenarios/hrcs_permlog/PermLogListPlugin.java` · 数据访问主链
- `scene_doc.json` · 13 物理表清单
- `form_lifecycle_rules.json` FP_SF5 · setCustomQFilters 实证
- `rules_chain_all.json` processlog · 反射调用链
