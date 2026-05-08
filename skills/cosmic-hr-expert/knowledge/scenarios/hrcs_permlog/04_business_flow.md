# 04 业务流转 · hrcs_permlog（HR 权限日志）

> permlog 是**只读日志视图** · 没有传统单据"草稿→提交→审核→生效"状态机
> 它的"流转"是**日志生成业务流**：上游编辑场景 → 写日志 → permlog 视图 → processlog 反射处理

---

## 1. 核心定位 · permlog 是日志聚合视图

permlog 不是业务执行场景 · 而是**审计追溯视图**：

- 上游：所有 hrcs_* 权限编辑场景（用户授权/角色配置/数据规则/动态方案等）执行后 · **直接调** `PermLogServiceHelper` 等业务 helper · 把变更前后值序列化成 JSON · insert 到 t_hrcs_permlog 主表 + 12 张子分录表
- 中游：t_hrcs_permlog 表数据沉淀
- 下游 1：hrcs_permlog 列表 · setFilter 强制 hashandle='1' · 用户审计查询
- 下游 2：processlog 操作 · 反射调 logtype.handlerclass.doHandler(logId) · 由 handler 自己处理（一般是状态翻转 hashandle 0→1 + 推送到下游审计平台/告警等）
- 下游 3：归档设置（donothing_archiveset）· 配置周期清理/归档规则

---

## 2. "状态字段"一览（permlog 没有 billstatus）

| 字段 | 含义 | 默认值 | 流转 |
|---|---|---|---|
| `hashandle` | 是否已处理 | 0（未处理） | processlog 操作后 → handlerclass.doHandler 内部 set 1 |
| `influusernumber` | 影响用户数 | 计算字段 | 写日志时统计填入 · 不变 |
| `clienttype` | 客户端类型 | web/mobile/openapi | 写入时确定 · 不变 |

**关键点**：permlog 没有 `billstatus` / `enable` / `auditstatus` 这些标准单据状态字段 · 因为它**不是业务单据** · 是日志记录。

---

## 3. 日志生成业务流（跨场景）

### 3.1 角色变更类（logtype.number ∈ {1010-1040}）

```
hrcs_role 角色编辑（save/audit/disable）
        |
        | OP 插件触发（在其他 hrcs_role 场景中）
        v
PermLogServiceHelper.recordXxxLog(...)
        |
        | insert
        v
t_hrcs_permlog 主表 (logtype=1010等)
        + t_hrcs_permlogbaseinfo (变更字段)
        + t_hrcs_permlogrolefunc (功能权限项变更)
        + t_hrcs_permlogroledim (维度变更)
        + t_hrcs_permlograngefield (字段范围变更)
        + t_hrcs_permlogroleopen (公开管理员变更)
        + t_hrcs_permlograngedr (数据规则变更)
        + t_hrcs_permloginfluuser (受影响用户)
```

详情子页：`hrcs_permlog_role`（PermLogListPlugin.showDetail FP_HL2）

### 3.2 用户权限类（logtype.number ∈ {1050, 1060, 2010-2095}）

```
hrcs_userrole / hrcs_userpermfile 用户授权场景
        |
        v
PermLogServiceHelper.record... (insert with logtype=1050等)
        |
        v
t_hrcs_permlog + t_hrcs_permloginfluuser (受影响用户分录)
```

详情子页：`hrcs_permlog_userperm`

### 3.3 数据规则类（logtype.number = 3015）

```
hrcs_datarule 数据规则编辑
        |
        v
t_hrcs_permlog + t_hrcs_permlograngedr/rangebddr
```

详情子页：`hrcs_permlog_datarule`

### 3.4 业务对象维度映射类（logtype.number ∈ {4010, 4015, 4020}）

```
业务对象维度映射变更
        |
        v
t_hrcs_permlog + t_hrcs_permloginflurole/rangebiz
```

详情子页：`hrcs_permlog_bodimmapping`

---

## 4. processlog 处理日志业务流（PermLogListPlugin 实证）

```
用户列表选中 1 条未处理日志（实际 List 已强制 hashandle=1 · ISV 解除过滤后才能选未处理）
        |
        | 点击"处理日志"按钮 (opKey=processlog · opType=donothing)
        v
PermLogListPlugin.beforeDoOperation
   1. 单选校验: coll.size() > 1 -> showError "please selected 1 row." setCancel
   2. 加载 logtype 全集: PermLogTaskServiceHelper.getAllPermLogType()
   3. 构建 logTypeMap<id, handlerclass>
   4. for row in selected:
        a. logId = row.getPrimaryKeyValue()
        b. permLogDyn = PermLogServiceHelper.getPermLog(logId)
        c. logtypeId = permLogDyn.logtype.id
        d. if logtypeId not in map: continue  (不报错 · 静默忽略)
        e. clazzName = map.get(logtypeId)
        f. invoke(clazzName, "doHandler", logId)
              -> Class.forName(clazzName)
              -> newInstance()
              -> getMethod("doHandler", long).invoke(instance, logId)
        g. catch Exception:
              LOGGER.error
              showError "exception:" + msg
              setCancel(true)
              return
        h. ok: showSuccess "success"
```

**关键点**：

- `handlerclass` 是 `hrcs_permlogtype` 基础资料的字段 · 存 FQN 字符串
- 每种 logtype 独立挂自己的 handler · 解耦扩展
- handler 必须 public 无参构造 + public Object doHandler(long logId)
- handler 内部负责事务（标品 List FormPlugin 不进事务）
- 错误提示**英文未 i18n**（标品代码 'please selected 1 row.' / 'exception:'）

---

## 5. 归档设置业务流（donothing_archiveset）

```
点击"归档设置" (opKey=donothing_archiveset · opType=donothing)
        |
        v
平台触发 donothing 操作（不进事务 · 直接成功）
        |
        v
PermLogListPlugin.afterDoOperation
   switch(opKey)
     case "donothing_archiveset":
       if (operationResult != null && operationResult.isSuccess()):
          showLogSetting()
              -> FormShowParameter para
              -> openStyle.setShowType(ShowType.Modal)  // 阻塞主页面
              -> setFormId("hrcs_permlogarchive_set")
              -> getView().showForm(para)
        |
        v
hrcs_permlogarchive_set 子页面（独立配置归档规则）· 跟 hrcs_permlog 解耦
```

---

## 6. 详情子页业务流（4 路 formId 路由）

```
List 上点击 number 列超链 (或 influusernumber 列)
        |
        v
PermLogListPlugin.billListHyperLinkClick
   - if "number".equals(fieldName): showDetail(args, isInfluusernumber=false)
   - if "influusernumber".equals(fieldName): showDetail(args, isInfluusernumber=true)
        |
        v
showDetail(args, isInfluusernumber)
   1. pkId = list.getFocusRowPkId()
   2. newPageId = view.getPageId() + "showForm" + pkId
   3. 防重开: SessionManager.get(newPageId) -> if pageId 存在: activate 旧页 · 不开新页
   4. 不存在: HRBaseServiceHelper("hrcs_permlog").queryOne("logtype", pkId)
        | 取 logtype.number
        v
   5. 4 路分支:
       a. roleLogType.contains(num)        -> formId = "hrcs_permlog_role"        + isInfluusernumber 透传
       b. userPermLogType.contains(num)    -> formId = "hrcs_permlog_userperm"   + isInfluusernumber=false (强制)
       c. dataRuleLogType.contains(num)    -> formId = "hrcs_permlog_datarule"   + isInfluusernumber 透传
       d. boDimMappingLogType.contains(num)-> formId = "hrcs_permlog_bodimmapping" + isInfluusernumber 透传
       e. 都不命中: 无操作（默认列表行展开行为 · 未 setCancel）
        |
        v
   6. 子页 FormShowParameter:
       - showType = MainNewTabPage（新主页签）
       - status = OperationStatus.VIEW (强制只读)
       - showTitle = true
       - hasRight = true
       - caption = ResManager.loadKDString("%s日志详情") format(logType.name)
       - customParam: caption / pkId / isInfluusernumber / logType / ismergerows=false
       - pageId = newPageId（注册到 SessionManager 防下次重开）
```

---

## 7. setFilter 业务流（List 列表查询主战场）

```
用户在筛选条件容器输入 → 触发 setFilter 事件
        |
        v
PermLogListPlugin.setFilter(SetFilterEvent arg)
   1. 取 arg.getQFilters() · 遍历每个 QFilter:
        a. property == "1": quickSearch 模式
            - 用户的 value 用 # 分两段（关键字#字段集）
            - 取 split[1] 当字段集
            - 在 ImmutableSet quickSearchFieldConds (9 字段) 基础上 add 用户字段 + remove excludeQuickSearchFieldConds (3 字段)
            - String.join(",") 重设 value
        b. property == "permfile.user.name": 4 路 OR 拆解
            - clone 4 份 -> warpQFilters 改写 property:
                permfile.user.name / permfile.user.number /
                influuserentry.influuser_permfile.user.name / .number
            - qFilter.or(cloneQFilter) 把 4 份并到原条件
        c. property == "permfile.id": 3 路 OR 拆解
            - permfile.user.id / influuserentry.influuser_permfile.id / .user.id
        d. property == "rolename": 3 路 OR 拆解
            - rolenumber / influroleentry.influrole_rolenumber / .influrole_rolename
   2. 末尾强制 setCustomQFilters([new QFilter("hashandle", "=", "1")])
        | 列表硬过滤已处理日志
        v
        ORM 拼 SQL · JOIN 13 张表 · WHERE hashandle='1' AND 用户其他过滤
```

---

## 8. 审批链 / 状态机

**permlog 没有审批链** · 不是业务单据 · 不需要工作流。

如果客户提"我要给权限日志加审批"：

- 真实需求多半是"权限变更日志要走主管复核才记录" → 改造点不在 permlog 自己 · 而在**上游编辑场景**（如 hrcs_userrole / hrcs_role / hrcs_dynascheme 等）走工作流
- permlog 是变更**事后**沉淀 · 加审批等于反过来

---

## 9. 流程角色 vs 触发点

| 角色 | 操作 | 触发点 |
|---|---|---|
| **HR 领域管理员** | 打开 permlog 列表查询 | preOpenForm 三闸（HRAdminStrictPlugin）|
| 系统超管 / 苍穹账号 | 同上（豁免领域管理员校验） | 同上 |
| HR 领域管理员 | 选中日志 + 点击"处理日志" | beforeDoOperation processlog |
| HR 领域管理员 | 点击"归档设置" | afterDoOperation donothing_archiveset |
| HR 领域管理员 | 点击 number 超链查看详情 | billListHyperLinkClick |
| 上游编辑场景插件（系统） | 写入日志 | OP 插件直接 insert（绕过 hrcs_permlog 的 save 流程） |

---

## 10. 已知陷阱

| # | 陷阱 | 影响 |
|---|---|---|
| 1 | List 永远只显示 hashandle=1 已处理日志（FP_SF5） | 客户问"未处理日志去哪了" → 解释强制过滤 |
| 2 | processlog 报错信息硬编码英文 | 多语言客户体验差 · ISV 可并列挂插件改写 |
| 3 | processlog 反射调用失败被 catch · 显示英文 'exception:' | 同上 |
| 4 | logtype.handlerclass 不存在 → 静默 continue（不报错） | 客户感觉没反应 · 排查难 · 加日志 |
| 5 | 详情子页 OperationStatus.VIEW 强制只读 | 不能编辑日志（这是设计预期 · 不要绕） |
| 6 | quickSearch 字段集**值用 # 分两段**（不是 ,） | ISV 自定义快速搜索时容易写错协议 |
| 7 | setFilter 4 路 OR 用 SerializationUtils.clone 深拷贝 | 否则 nest 引用共享 · 互相覆盖 |
| 8 | 13 张物理表 JOIN · 大数据量查询慢 | 时间窗口必须传（建议 ≤ 90 天）|

---

## 11. 引用文件

- `_decompiled/scenarios/hrcs_permlog/PermLogListPlugin.java` · 全部业务流来源
- `form_lifecycle_rules.json` · 14 条规则
- `rules_chain_all.json` · 13 opKey
- `_shared/platform_rules.json` · PR-001/PR-010 ISV 边界
