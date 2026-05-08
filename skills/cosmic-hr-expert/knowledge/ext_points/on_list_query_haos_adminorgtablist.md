# 🔌 onListQuery@haos_adminorgtablist

> **类型**: 扩展点独立档案（列表视图扩展点）
> **confidence**: verified

---

## 基本信息

| 属性 | 值 |
|---|---|
| **ID** | `ep_on_list_query_haos_adminorgtablist` |
| **助记名** | `onListQuery@haos_adminorgtablist` |
| **宿主**: | `haos_adminorgtablist` (行政组织列表视图) |
| **时机**: | 列表查询请求发出前 |
| **接口**: | 列表插件（继承 `AbstractListPlugin`，重写 `setFilter(SetFilterEvent e)`） |
| **事务阶段**: | 无事务（只读） |
| **覆盖风险** | 中 |

---

## 入参详情（列表插件通用）

苍穹列表扩展通过继承 `AbstractListPlugin` 并重写列表事件方法实现。常用切入点：

- **查询前追加过滤条件**：重写 `setFilter(SetFilterEvent e)`，通过 `e.getQFilters()` 追加 `QFilter`。
- **查询前改排序**：重写 `setFilter(SetFilterEvent e)`，通过 `e.setOrderBy(String)`。
- **行按钮点击**：`itemClick(ItemClickEvent e)`。
- **超链接点击**：`billListHyperLinkClick(HyperLinkClickArgs e)`。
- **新建行初始化**：`afterCreateNewData(EventObject e)`。
- **当前操作人**：`RequestContext.get().getUserId()`。

> ⚠️ 上面是苍穹列表扩展的真实能力点。`IListQueryPlugin.onListQuery(ListQueryContext)` 在平台中**不存在**，历史知识里出现过请一律以本表为准。

---

## 标品插件

### 1. OrgListAuthFilterPlugin
- **作用**: 按用户管辖权限过滤组织列表
- **建议**: 继承扩展权限模型，不要替换

### 2. OrgListStatusFilterPlugin
- **作用**: 默认只显示 `status = "在用"` 的组织
- **建议**: 按业务需要可替换（如要显示全部）

---

## 推荐模式

### 模式 1 · 追加过滤条件（最常见）

**扩展入口坐标**
- 绑定表单：`haos_adminorgtablist`（行政组织列表视图）
- 绑定事件：列表视图的 `setFilter` 扩展事件（无 opKey，属于列表插件）
- 推荐父类：`AbstractListPlugin`
- 关键重写方法：
  - `setFilter(SetFilterEvent e)` — 追加 `QFilter` 到 `e.getQFilters()`
  - 通过 `RequestContext.get().getUserId()` 拿当前操作人

**业务意图**：列表查询前，在标品权限过滤的基础上，**追加**一条"只看当前用户所在部门"的过滤条件，缩小用户可见数据范围。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorgtablist`
2. 选择【注册插件】→ 新增插件类
3. 保存 → 部署生效

> ⚠️ 列表插件没有 opKey，直接挂在列表表单上；**只追加**过滤条件，不要清空 `e.getQFilters()`，否则会丢失标品权限 / 状态过滤。

### 模式 2 · 替换默认排序

**扩展入口坐标**
- 绑定表单：`haos_adminorgtablist`
- 推荐父类：`AbstractListPlugin`
- 关键重写方法：
  - `setFilter(SetFilterEvent e)` → `e.setOrderBy("createdate DESC")`

**业务意图**：把列表默认排序从标品的排序（一般是 longnumber）改为按"创建时间倒序"，便于"最新建的组织排在最前"的交付场景。

> ⚠️ 如同时需要保留其他排序条件，请拼接后再整体 setOrderBy，不要只覆盖单一列。

---

## 禁止模式

### ⛔ 禁止 1: 清空或替换 e.getQFilters()
**错误做法**：在 `setFilter` 里把 `e.getQFilters()` 清空或用全新的列表覆盖。

**后果**：标品的权限过滤 / 状态过滤（`status=在用`）会被完全丢失，用户看到本不该看的组织。

**正确做法**：只 `add` 一条新的 `QFilter` 进去，绝不清空。

### ⛔ 禁止 2: 在 setFilter 里做写操作
`setFilter` 是**查询前的只读**回调，不要在此 INSERT / UPDATE，否则会被计入列表查询耗时且事务语义不清。写操作请在对应业务操作的 Op 类中处理。

---

## 代码模板（扩展入口骨架）

**扩展入口坐标**
- 绑定表单：`haos_adminorgtablist`
- 推荐父类：`AbstractListPlugin`
- 关键重写方法：
  - `setFilter(SetFilterEvent e)` — 查询前追加 `QFilter` / 改排序
  - （可选）`afterCreateNewData(EventObject e)` — 列表新建按钮触发时初始化默认字段

**业务意图**：列表扩展的通用骨架。登记到列表表单后，在 `setFilter` 中按当前用户拿到可见范围的组织 ID 集合，追加一条 `QFilter.in("id", ...)` 到 `e.getQFilters()`。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorgtablist`
2. 选择【注册插件】→ 新增插件类（`targetType = LIST_FORM`）
3. 保存 → 部署生效

> ⚠️ 不要写成"继承某个假想的 OrgListAuthFilterPlugin"，平台只暴露 `AbstractListPlugin` + `setFilter` 事件作为官方扩展入口。

---

## 性能建议

- 追加的过滤条件**应使用索引字段**（如 id / number / parentorg）
- 避免在扩展点里调用**昂贵的外部查询**（如 RPC）
- 大数据集场景用 `ctx.getPageSize()` 控制分页

---

## 相关

- [haos_adminorgtablist 实体档案](../entities/haos_adminorgtablist.md)
- [CS-06 · 列表按数据权限动态隐藏](../scenarios/admin_org_quick_maintenance/06_customization_solutions.md#cs-06)

---

**📌 来源**: `anchors.md` + 实测
