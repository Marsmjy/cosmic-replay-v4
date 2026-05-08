# 10 异常诊断 · hrcs_permlog（HR 权限日志）

> 异常分两类：1) 共性陷阱（来自 cosmic_realworld_traps）· 2) permlog 专属陷阱（反编译实证）

---

## 1. 共性陷阱（cosmic_realworld_traps 摘录）

### 1.1 buildmeta_traps（建元数据）

参考 `knowledge/cosmic_realworld_traps/buildmeta_traps.md`

| 陷阱 | 表现 | 根因 | 解药 |
|---|---|---|---|
| 不传 parentId 兜底 | buildMeta 不报错 · 但实际继承走了 bos 基础资料默认模板 | 苍穹默认兜底 · 不显式校验 | permlog 扩展元数据必须显式 parentId = hrcs_permlog 实体 ID |
| 字段类型 74 值枚举 | modifyMeta add 静默走 TextField | 参数名错（dataType vs fieldType）| 用 fieldType/name/columnName 三参数对应 |
| EmployeeField 等 HR SDK 字段类型 | OpenAPI buildMeta 报错 | OpenAPI 不识别 HR 扩展类型 | 改用 BasedataField + 引用 hrpi_employee 等 |

### 1.2 addrule_traps（加规则）

参考 `knowledge/cosmic_realworld_traps/addrule_traps.md`

| 陷阱 | 表现 | 解药 |
|---|---|---|
| ActionType PascalCase | 规则不生效 | "ChangeProperty" 而非 "changeproperty" |
| preCondition `==''` | 规则报错 | 用 `is empty` 或 `length == 0` |
| 老规则没清理就 add | 规则重复 · 双触发 | 先 deleteRule 旧的 |

### 1.3 modifymeta_traps（改元数据）

参考 `knowledge/cosmic_realworld_traps/modifymeta_traps.md`

| 陷阱 | 表现 | 解药 |
|---|---|---|
| formId 错（标品 vs ISV）| 报权限错误或静默成功但没生效 | 必须用自己拥有的 formId |
| EmbedFormAp 假成功 | OpenAPI 返回成功 · 实际布局没变 | EmbedFormAp 需要 Java 插件配合 |
| ops 格式错 | 部分操作失败 | 严格按 add/remove/modify 的 schema |

---

## 2. permlog 专属陷阱（PermLogListPlugin / HRAdminStrictPlugin 实证）

### 2.1 List 永远只显示已处理日志（FP_SF5）

| 维度 | 详情 |
|---|---|
| 表现 | 用户问"未处理日志去哪了" |
| 根因 | PermLogListPlugin.setFilter L272 强制 `setCustomQFilters([new QFilter("hashandle", "=", "1")])` |
| 实证 | PermLogListPlugin.java L272-L273 |
| 排查 | 检查 SQL · WHERE 子句必含 fhashandle = '1' |
| 解药 | CS-02 解除 hashandle 强制过滤（仅授权角色）|

### 2.2 hashandle 值是字符串 '1' 不是 boolean true

| 维度 | 详情 |
|---|---|
| 表现 | ISV 直接 `new QFilter("hashandle", "=", true)` 查不到 |
| 根因 | CheckBoxField 标品兼容旧版序列化 · 存的是 '1'/'0' 字符串 |
| 实证 | PermLogListPlugin.java L272 `new QFilter("hashandle", "=", "1")` 引号包裹 |
| 解药 | 用字符串 "1"/"0" · 或更稳健 `Boolean.TRUE` 让 ORM 自适配 |

### 2.3 processlog 多选静默拒绝

| 维度 | 详情 |
|---|---|
| 表现 | 用户多选 N 条点处理日志 · 出英文提示 'please selected 1 row.' · 看不懂 |
| 根因 | PermLogListPlugin.beforeDoOperation L106-L111 单选校验 · 提示英文未 i18n |
| 实证 | PermLogListPlugin.java L107 `coll.size() > 1` |
| 解药 | ISV 并列挂插件 · super 之前拦 processlog · 改提示文本 + 支持批量循环 |

### 2.4 logtype.handlerclass 缺失静默 continue

| 维度 | 详情 |
|---|---|
| 表现 | 用户点处理日志 → 没反应 · 没提示 · 没日志 |
| 根因 | PermLogListPlugin.beforeDoOperation L120 `if (!logTypeMap.containsKey(...)) continue;` 静默跳过 |
| 实证 | PermLogListPlugin.java L120 |
| 排查 | 后端 LOG 没 ERROR · 但 hashandle 一直是 0 |
| 解药 | ISV 排查 hrcs_permlogtype 基础资料 · 检查 handlerclass 字段是否填 · 是否 FQN 正确 |

### 2.5 反射调用异常被吞

| 维度 | 详情 |
|---|---|
| 表现 | 用户点处理日志 → 弹 "exception:..." · 用户不知道是啥 |
| 根因 | PermLogListPlugin.beforeDoOperation L125-L130 catch 通用 Exception |
| 实证 | PermLogListPlugin.java L125-L130 |
| 排查 | 后端 LOG · 看 ClassNotFoundException / NoSuchMethodException / InvocationTargetException 哪个 |
| 解药 | ISV 升级前批量校验 hrcs_permlogtype.handlerclass · ServiceLoader.load 测试 |

### 2.6 详情子页路由 fall through

| 维度 | 详情 |
|---|---|
| 表现 | 用户点 number 列超链 · 没跳转 · 像按钮失效 |
| 根因 | logtype.number 不在 4 路集合内（standard 1010-4020）· FP_HL2 4 路都不命中 · 默认行为 |
| 实证 | PermLogListPlugin.java L186-L198 |
| 解药 | ISV 加新 logtype 编码时同步扩 4 路集合（并列挂插件）· 或自挂 billListHyperLinkClick 路由 |

### 2.7 SessionManager 防重开失效

| 维度 | 详情 |
|---|---|
| 表现 | 用户多次点同一行 number · 开了多个详情页 |
| 根因 | newPageId = `getView().getPageId() + "showForm" + pkId` · 主页面切换会重置 pageId · session 缓存失效 |
| 实证 | PermLogListPlugin.java L173-L181 |
| 解药 | 客户接受这个行为 · 不修复（标品设计）|

### 2.8 详情页强制只读

| 维度 | 详情 |
|---|---|
| 表现 | 用户问"为什么改不了"  |
| 根因 | PermLogListPlugin.showDetail L209 `setStatus(OperationStatus.VIEW)` |
| 实证 | PermLogListPlugin.java L209 |
| 解药 | 这是设计 · 日志记录不允许编辑 · 客户教育 |

### 2.9 HRAdminStrictPlugin 拒绝消息英文 i18n

| 维度 | 详情 |
|---|---|
| 表现 | 中文环境用户看到中文 "您无法访问该功能，因为您不是HR领域管理员。" · 但日志里是 Unicode 转义 |
| 根因 | HRAdminStrictPlugin.java L45 `您无法...` 是 Java 源码层的 Unicode 字面量 · 实际渲染正确 |
| 实证 | HRAdminStrictPlugin.java L45 |
| 解药 | 不是 bug · 反编译看到的 unicode 是源码格式 |

### 2.10 ListShowParameter 非 lookUp 模式被拒

| 维度 | 详情 |
|---|---|
| 表现 | 别的表单 F7 引用 hrcs_permlog 弹窗被准入闸拒 |
| 根因 | ListShowParameter.isLookUp() 必须为 true 才豁免 · 客户手动构造 ShowParameter 没设 |
| 实证 | HRAdminStrictPlugin.java L33-L36 |
| 解药 | 用 BasedataEdit 控件让框架自动设 · 或显式 setLookUp(true) |

---

## 3. 性能陷阱

### 3.1 13 张物理表 LEFT JOIN

| 表现 | 根因 | 解药 |
|---|---|---|
| 列表加载 > 5s | 13 表 JOIN | 必传 operationtime 时间窗 ≤ 90 天 / 在 (hashandle, operationtime DESC) 加复合索引 |
| 导出 50 万行卡死 | 同上 + 解除 hashandle 过滤 | CS-02 时按角色判定 · 普通管理员仍只看已处理 |

### 3.2 beforeopdata / afteropdata LIKE 模糊

| 表现 | 根因 | 解药 |
|---|---|---|
| ISV 加全文搜索这两个字段 · 全表扫 | TextField 存 JSON 串 · LIKE 没索引 | 改造结构化字段 / 引入 ElasticSearch / 不允许这种查询 |

### 3.3 description 多语言查询慢

| 表现 | 根因 | 解药 |
|---|---|---|
| 跨语言用户查询慢 | _l 表 JOIN | 优先用其他字段定位 · description 仅展示 |

---

## 4. ISV 边界陷阱

### 4.1 直接 modifyMeta 标品 formId

| 表现 | 根因 | 解药 |
|---|---|---|
| OpenAPI modifyMeta 报权限错 / 静默不生效 | hrcs_permlog 是标品 ISV 不能改 | 必须建 ISV 扩展元数据（PR-001 / isv_ownership_redline） |

### 4.2 继承 PermLogListPlugin

| 表现 | 根因 | 解药 |
|---|---|---|
| 编译 / 运行时报错 / 升级即坏 | PermLogListPlugin 是 final · 不可继承 | 并列挂插件 |

### 4.3 继承 @SdkInternal 类

| 表现 | 根因 | 解药 |
|---|---|---|
| 苍穹升级后类签名变 · ISV 编译失败 | @SdkInternal 是平台内部 · 跨版本不稳定 | 用白名单内的 HRDataBaseList/Edit/Op |

---

## 5. BEC 陷阱

### 5.1 双发风暴

| 表现 | 根因 | 解药 |
|---|---|---|
| 订阅方收到两次相同事件 | 上游标品已发 BEC + ISV 又发 | 实施前 grep 上游 Service + *MsgTask · 参考 feedback_bec_3layer_async_publish.md |

### 5.2 vars 体积超限

| 表现 | 根因 | 解药 |
|---|---|---|
| BEC 发布失败 / 订阅消费慢 | vars 塞了 DynamicObject 全字段 | 只塞 logId / opType / minimal 字段 · 订阅方反查（PR-011）|

### 5.3 eventNumber 未注册

| 表现 | 根因 | 解药 |
|---|---|---|
| triggerEventSubscribeJobs 无效 / 不抛错 | eventNumber 必须先在开发平台注册 | 配置时先去开发平台 → 业务事件管理建好 |

---

## 6. 排查决策树

```
问题：列表打不开
  -> HRAdminStrictPlugin 三闸拒？ 看后端 LOG · 或刷新 URL 加 ?lookUp=true 测试
  -> 准入通过但白屏？ 看 setFilter 报错（4 路 OR 拆解抛异常）
  -> 13 表 JOIN 性能？ EXPLAIN SQL · 加时间窗

问题：处理日志没反应
  -> 多选了？ 提示 "please selected 1 row."
  -> handlerclass 没配？ 静默 continue · 后端 LOG 无错
  -> handlerclass 配错类？ catch Exception · 提示 "exception:..." · 看后端 stack trace

问题：详情子页打不开
  -> 不是 4 路集合内 logtype？ FP_HL2 fall through
  -> 同 pkId 已开页？ activate 旧页（不开新页）
  -> 强制 VIEW 不让编辑？ 这是设计

问题：导出文件比预期大
  -> ISV CS-02 解除了 hashandle 过滤？
  -> 时间窗没传 / 太大？

问题：BEC 发了但订阅没收
  -> eventNumber 没注册到开发平台？
  -> 订阅插件没绑到事件？
  -> handleEvent 抛异常被框架重试？看后端 LOG
```

---

## 7. 引用文件

- `_decompiled/scenarios/hrcs_permlog/PermLogListPlugin.java`
- `_decompiled/scenarios/hrcs_permlog/HRAdminStrictPlugin.java`
- `cosmic_realworld_traps/buildmeta_traps.md`
- `cosmic_realworld_traps/addrule_traps.md`
- `cosmic_realworld_traps/modifymeta_traps.md`
- `feedback_bec_3layer_async_publish.md`
- `_shared/platform_rules.json` PR-001/010/011
