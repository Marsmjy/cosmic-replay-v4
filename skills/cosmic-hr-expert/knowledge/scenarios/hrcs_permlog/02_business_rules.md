# 02 业务规则 · hrcs_permlog（HR 权限日志）

> permlog 没有 formRule / bizRule（listRules 实抓 0 条）· 全部业务规则**写在插件代码里**
> 数据源：PermLogListPlugin.java + HRAdminStrictPlugin.java + form_lifecycle_rules.json

---

## 1. 业务规则全景

permlog 是只读日志视图 · 业务规则集中在：

1. **权限准入** · HRAdminStrictPlugin · preOpenForm 三闸
2. **List 强制过滤** · PermLogListPlugin.setFilter · hashandle='1'
3. **快速搜索字段集** · setFilter property='1' 协议
4. **跨表 OR 拆解** · permfile/rolename 多路展开
5. **超链路由** · 4 个 logtype 编码集 → 4 个详情 formId
6. **processlog 反射** · 单选 + handlerclass 调用 + 异常兜底
7. **归档子页** · donothing_archiveset 弹 Modal
8. **F7 引用豁免** · ListShowParameter.isLookUp 跳过权限闸

---

## 2. 准入规则（HRAdminStrictPlugin · preOpenForm）

### 2.1 三闸串行

| 闸 | 检查 | 通过条件 |
|---|---|---|
| 闸 0 | ListShowParameter.isLookUp() | true → 直接放行（F7 弹窗豁免）|
| 闸 1 | PermissionServiceHelper.isAdminUser(uid) | true 系统超管 → 进闸 3 |
| 闸 2 | PermCommonUtil.isCosmicUser(uid) | true 苍穹账号 → 进闸 3 |
| 闸 3 | HRAdminService.isHrAdmin() | true HR 领域管理员 → 放行 |
| 拒绝 | 闸 1+2 都 false 或 闸 3 false | setCancel + 提示"您无法访问该功能，因为您不是HR领域管理员。" |

### 2.2 实证（HRAdminStrictPlugin.java L29-L52）

- 闸 0：第 33 行 `if (fsp instanceof ListShowParameter && ((ListShowParameter)fsp).isLookUp()) return;`
- 闸 1+2：第 41-47 行 `boolean isAdmin = PermissionServiceHelper.isAdminUser(userId); boolean isCosmic = PermCommonUtil.isCosmicUser((Long)userId); if (!isAdmin & !isCosmic) { e.setCancel(true); ... }`
- 闸 3：第 48-52 行 `if (!HRAdminStrictPlugin.isHrAdmin()) { e.setCancel(true); ... }`

> **i18n**：拒绝消息 key = `HRAdminStrictPlugin_0` · resource = `hrmp-hrcs-formplugin`

### 2.3 ISV 想给非 HR 管理员开放只读权限

❌ 不要直接 modifyMeta 删 HRAdminStrictPlugin · 这是标品共用 11 hrcs 场景的安全闸 · 删了影响其他场景

✅ 在 ISV 扩展元数据上注册自己的 ListShowParameter 显示参数 · 设 lookUp=true（豁免闸）但要权衡安全风险 · 或新建只读视图绕开 hrcs_permlog 主表单

---

## 3. List 强制过滤规则（FP_SF5）

### 3.1 hashandle='1' 强制

```java
ArrayList customQFilterList = Lists.newArrayList(new QFilter[]{new QFilter("hashandle", "=", "1")});
arg.setCustomQFilters(customQFilterList);
```

| 维度 | 值 |
|---|---|
| 字段 | `hashandle` |
| 比较符 | `=` |
| 值 | **字符串 '1'**（不是 boolean true · 标品兼容旧版 CheckBoxField 序列化）|
| 应用 | List 永远只显示已处理日志 |

### 3.2 影响范围

- ✅ List 主页面所有用户（含超管）都被过滤
- ✅ HIES 导出（export_from_list_hr / export_from_expttpl_hr）跟随 customQFilters 限制范围
- ❌ 上游编辑场景写日志时不影响（写入路径独立 · 直接 insert）

### 3.3 ISV 想看未处理日志

参见 `06_customization_solutions.md` CS-02 列表过滤定制

---

## 4. 快速搜索字段集协议（FP_SF1）

### 4.1 协议格式

```
property = "1"
value = "<关键字>#<字段集 用 , 分隔>"
```

### 4.2 标品扩展规则

| 步骤 | 动作 |
|---|---|
| 1 | 解析 value · split("#") 必须 length=2 · 否则 continue |
| 2 | 取 split[1] 当用户字段集 |
| 3 | LinkedHashSet conds = new LinkedHashSet(quickSearchFieldConds) // 9 内置字段 |
| 4 | conds.addAll(value.split(",")) // 加用户字段（注意是 , 不是 #）|
| 5 | conds.removeIf(excludeQuickSearchFieldConds::contains) // 去 3 个排除字段 |
| 6 | qFilter.\_\_setValue(String.join(",", conds)) // 重设回 value |

### 4.3 quickSearchFieldConds（9 个内置字段 · 实证 L95）

```
operator.username
operator.number
permfile.user.name
permfile.user.number
influuserentry.influuser_permfile.user.name
influuserentry.influuser_permfile.user.number
rolenumber
influroleentry.influrole_rolenumber
influroleentry.influrole_rolename
```

### 4.4 excludeQuickSearchFieldConds（3 个排除字段 · 实证 L96）

```
operator.username
permfile.user.name
influuserentry.influuser_permfile.user.name
```

> **逻辑**：内置 + 排除有重叠 · 净加入 6 字段 · 因为 username/name 这 3 个会绕过 user 主表索引性能差

---

## 5. 跨表 OR 拆解规则（FP_SF2/SF3/SF4）

### 5.1 permfile.user.name 触发 → 4 路 OR

```
原 QFilter (property=permfile.user.name) OR
   clone1 (rewrite property → permfile.user.name)
   clone2 (rewrite property → permfile.user.number)
   clone3 (rewrite property → influuserentry.influuser_permfile.user.name)
   clone4 (rewrite property → influuserentry.influuser_permfile.user.number)
```

### 5.2 permfile.id 触发 → 3 路 OR

```
原 OR
   permfile.user.id
   influuserentry.influuser_permfile.id
   influuserentry.influuser_permfile.user.id
```

### 5.3 rolename 触发 → 3 路 OR

```
原 OR
   rolenumber
   influroleentry.influrole_rolenumber
   influroleentry.influrole_rolename
```

### 5.4 warpQFilters 私有方法（FP_SF6）

处理 BasedataField 多级路径（3 段或 4 段）的关键工具：
- 3 段：`permfile.user.name`
- 4 段：`influuserentry.influuser_permfile.user.name`

---

## 6. 超链路由规则（FP_HL2）

### 6.1 4 路 formId 分支

| logtype.number 编码集 | formId | isInfluusernumber 透传 |
|---|---|---|
| ROLE_LOG_TYPE = {1010,1015,1020,1025,1030,1035,1040} | hrcs_permlog_role | 透传 |
| USER_PERM_LOG_TYPE = {1050,1060,2010,2015,2020,2030,2090,2095,2060,2065,2066} | hrcs_permlog_userperm | **强制 false**（标品逻辑）|
| DATA_RULE_LOG_TYPE = {3015} | hrcs_permlog_datarule | 透传 |
| BO_DIM_MAPPING_LOG_TYPE = {4010,4015,4020} | hrcs_permlog_bodimmapping | 透传 |

### 6.2 防重开机制（FP_HL3）

- newPageId = `getView().getPageId() + "showForm" + pkId`
- SessionManager 注册：第一次 put + show · 第二次 get → activate 旧页

### 6.3 详情子页参数

- 强制 `OperationStatus.VIEW`（只读）
- ShowType.MainNewTabPage（新主页签）
- caption 用 `ResManager.loadKDString("%s日志详情", "PermLogListPlugin_0", "hrmp-hrcs-formplugin")` format(logType.name)

---

## 7. processlog 业务规则（FP_BDO1/BDO2）

| 规则 | 检查 | 失败处理 |
|---|---|---|
| 单选 | `coll.size() > 1` | showError "please selected 1 row." + setCancel |
| logtype.handlerclass 必须配置 | `logTypeMap.containsKey(logtypeId)` | continue 跳过（**静默忽略 · 不报错**）|
| handlerclass 反射可调 | Class.forName + getMethod("doHandler", long).invoke | 任意异常 catch · showError "exception:" + msg + setCancel |

### 7.1 i18n 缺失

- "please selected 1 row." · 标品**英文硬编码** · ISV 可改造
- "exception:" · 同上
- "success" · 同上

---

## 8. 归档设置规则（FP_ADO1）

| 步骤 | 动作 |
|---|---|
| 1 | opKey = donothing_archiveset · 进入 afterDoOperation |
| 2 | 检查 operationResult != null && operationResult.isSuccess() |
| 3 | 调 showLogSetting() |
| 4 | 弹 hrcs_permlogarchive_set 子页 · ShowType.Modal（阻塞主页）|

---

## 9. F7 引用豁免规则（FP_PO1）

```java
if (fsp instanceof ListShowParameter && ((ListShowParameter)fsp).isLookUp()) {
    return;  // 豁免准入 · 不调 showMesIfUserIsNotAdmin
}
```

**业务含义**：当 hrcs_permlog 作为 F7 基础资料被别的表单引用弹出选数据时 · 不能因为当前用户不是 HR 管理员就拒掉 · 否则全平台 F7 引用都坏

---

## 10. 跨模块约束

### 10.1 上游写日志的约束

- 上游编辑场景必须用 `PermLogServiceHelper / PermLogTaskServiceHelper` 写日志
- ❌ 不要直接 `SaveServiceHelper.save("hrcs_permlog", obj)` 绕过 helper · 否则 logtype 关联/operationtime 等元数据可能不一致

### 10.2 hrcs_permlogtype 基础资料约束

- 必须先在 hrcs_permlogtype 基础资料里**预配置** logtype 编码 + handlerclass FQN
- ISV 加新 logtype 时必须改 hrcs_permlogtype 数据 · 不是改代码

### 10.3 ListShowParameter F7 引用约束

- 别的表单 F7 引用 hrcs_permlog 必须设 isLookUp=true · 否则被准入闸拒

---

## 11. 跟其他 hrcs 场景的差异

| 维度 | hrcs_permlog | 其他 hrcs（dynascheme/userrole 等）|
|---|---|---|
| formRule/bizRule | 0 | 多 |
| OP 业务插件 | 0 | 多 |
| 业务规则载体 | List FormPlugin（only setFilter/HyperLink/processlog）| OP 插件 + Validator + propertyChanged |
| 数据写入 | **不在 permlog 表单内** · 由上游写 | 表单 save/audit |

---

## 12. 引用文件

- `_decompiled/scenarios/hrcs_permlog/PermLogListPlugin.java` · 业务规则全部源码
- `_decompiled/scenarios/hrcs_permlog/HRAdminStrictPlugin.java` · 准入规则
- `form_lifecycle_rules.json` · 14 条 FP 规则
- `_auto_rules.md` · listRules 实抓 0 条
