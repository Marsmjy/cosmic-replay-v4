# 扩展点全图 · HR 角色管理列表 (hrcs_rolelist)

> **状态**: 🟢 基于反编译 2 类 + main_form.xml + plugins.json 实证
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

> 📌 **本文档作用**：列出 ISV 在本场景能挂的所有扩展点（按生命周期方法分组），并给出每个扩展点的常见用途、风险等级、推荐挂载方式。

---

## 一、标品已挂插件全清单（实证 plugins.json）

| # | 类别 | ClassName | 父类 | Jar | 生命周期方法 | ISV 可继承 |
|---|---|---|---|---|---|---|
| 1 | editable | `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin` | `HRDynamicFormBasePlugin` | hrmp-hrcs-formplugin-1.0.jar | `preOpenForm` | ❌ 不要继承 · 直接复用 |
| 2 | editable | `kd.hr.hrcs.formplugin.web.perm.role.HRRoleListPlugin` | `HRStandardTreeList` | hrmp-hrcs-formplugin-1.0.jar | 7 个生命周期方法 | ❌ 不要继承 · 场景专属类 (PR-001) |

⚠️ **本场景没有 OP 插件** —— 所有"操作"都被 FormPlugin 的 `beforeDoOperation` 通过 `args.setCancel(true)` 绕过了 OP 链。这意味着：
- ISV 不能挂"标品 OP 之前/之后"
- ISV 必须挂 ListPlugin 的 `beforeDoOperation` 抢拦
- 详见 06_customization_solutions.md CS-03/04/05

---

## 二、按生命周期方法分组（执行顺序 = 注册顺序）

### 2.1 preOpenForm （标品 1 个 · ISV 可加）

**执行顺序**：
1. `HRAdminStrictPlugin` ⭐ HR 领域管理员准入闸 · 11 hrcs 表单共用 · 非 HR 管理员直接拒
   - 实证：`HRAdminStrictPlugin.java` L29-L37 + L39-L53
   - 例外：`ListShowParameter.isLookUp() == true` 时跳过准入

**ISV 可挂**：✅
- 用途：在标品准入闸**之后**做更细粒度的页面级控制（如"分公司 A 的管理员只能进角色管理 · 不能进动态授权方案"）
- 风险：⚠️ 高 —— 设错让所有人进不去 · 业务大故障
- 推荐：继承 `HRDynamicFormBasePlugin`（不继承 `HRAdminStrictPlugin`）· 实现 preOpenForm · 调 `super.preOpenForm`（先让标品判断）后再加自己

### 2.2 createTreeListView (标品 1 个 · ISV 慎挂)

**执行顺序**：
1. `HRRoleListPlugin.createTreeListView` (L304-L307) → 替换 default 为 `CommonTreeListView(perm_rolegroup)`

**ISV 可挂**：⚠️ 慎
- 用途：替换列表视图的物理实现（如自定义渲染逻辑、自定义列宽）
- 风险：🔴 高 · 替换错会让整个列表坏
- 推荐：99% 不需要 ·  改 UI 优先在元数据上调

### 2.3 beforeBindData (标品 1 个 · ISV 可加)

**执行顺序**：
1. `HRRoleListPlugin.beforeBindData` (L355-L358) → updateSearch (设 SearchAp 提示文)

**ISV 可挂**：✅
- 用途：在数据绑定前调整 UI（如根据用户角色隐藏某些列、设置默认过滤值）
- 推荐：继承 `AbstractListPlugin` · 实现 beforeBindData · super 调用后追加

### 2.4 initTreeToolbar (标品 1 个 · ISV 可加)

**执行顺序**：
1. `HRRoleListPlugin.initTreeToolbar` (L309-L313) → 显示 btnnew/btnedit/btndel · 隐藏 iscontainnow/iscontainlower

**ISV 可挂**：✅
- 用途：调整树工具栏按钮可见性 / 增加自定义工具栏按钮
- 推荐：继承 `AbstractListPlugin` 或 `HRStandardTreeList`

### 2.5 setFilter (标品 1 个 · ISV 必须叠加 · CS-06 主战场)

**执行顺序**：
1. `HRRoleListPlugin.setFilter` (L367-L372) → 加 `id IN viewableRoles(currUserId)` 行级权限闸

**ISV 可挂**：✅ ⭐ 高频
- 用途：叠加自己的过滤（如"角色分组管理员只看自己组"）
- 风险：🔴 高 · 设错会让管理员看不见自己的角色
- 推荐：详见 CS-06

### 2.6 filterContainerInit (标品 1 个)

**执行顺序**：
1. `HRRoleListPlugin.filterContainerInit` (L407-L415) → 默认 `perm_role.enable=1`

**ISV 可挂**：✅
- 用途：设置过滤面板默认值
- 推荐：继承 `AbstractListPlugin`

### 2.7 filterContainerBeforeF7Select (标品 1 个)

**执行顺序**：
1. `HRRoleListPlugin.filterContainerBeforeF7Select` (L418-L432) → group F7 二级过滤限 hrcs_rolegrp 注册过的

**ISV 可挂**：✅
- 用途：调整过滤面板里的 F7 候选范围
- 推荐：继承 `AbstractListPlugin`

### 2.8 beforeItemClick (标品 1 个 · ISV 必须 RowKey 抢前)

**执行顺序**：
1. `HRRoleListPlugin.beforeItemClick` (L829-L861) → 12 个工具栏按钮校验

**ISV 可挂**：✅ ⭐ 高频
- 用途：自定义按钮点击前校验（如自定义【批量赋权】按钮 · CS-07）
- 风险：⚠️ 中 · RowKey 必须 < 标品 让自己先跑
- 推荐：详见 CS-07

### 2.9 beforeDoOperation (标品 1 个 · ISV 必须 RowKey 抢前 · CS-03/04/05/07 主战场)

**执行顺序**：
1. `HRRoleListPlugin.beforeDoOperation` (L989-L1003) + `handleBeforeOperatetionEvent` (L1012-L1072) → 12 opKey 入口

**ISV 可挂**：✅ ⭐ ⭐ 最高频
- 用途：拦截删除/启用/禁用/导入/导出/复制等所有 opKey 做前置校验或自定义流程
- 风险：🔴 高 · RowKey 必须排好 · 否则被标品 setCancel 跳过
- 推荐：详见 CS-03 (高风险删除/禁用) / CS-04 (ISV 下游引用) / CS-05 (BEC 事件) / CS-07 (批量赋权)

### 2.10 afterDoOperation (标品 1 个 · ISV 可加)

**执行顺序**：
1. `HRRoleListPlugin.afterDoOperation` (L1005-L1009) → 清 `itemKey` PageCache

**ISV 可挂**：✅
- 用途：操作后做后置（写日志、发 BEC 等）
- 风险：⚠️ 注意 · 标品 clearSelection 已经清掉选中行 · ISV 自己的 afterDoOperation 拿不到 selectedRows · 必须先在 beforeDoOperation 阶段 PageCache 暂存
- 推荐：详见 CS-05

### 2.11 itemClick (标品 1 个)

**执行顺序**：
1. `HRRoleListPlugin.itemClick` (L1101-L1112) → refresh 按钮处理

**ISV 可挂**：✅
- 用途：自定义按钮的 itemClick 主体逻辑（不是 beforeItemClick 校验阶段）
- 推荐：继承 `AbstractListPlugin`

### 2.12 closedCallBack (标品 1 个)

**执行顺序**：
1. `HRRoleListPlugin.closedCallBack` (L1115-L1120) → 处理 `exportUrl` actionId 回调

**ISV 可挂**：✅
- 用途：处理自己 showForm 出去后的回调
- 推荐：继承 `AbstractListPlugin`

### 2.13 registerListener (标品 1 个)

**执行顺序**：
1. `HRRoleListPlugin.registerListener` (L1603-L1609) → 注册 TreeNodeQueryListener / ListRowClickListener

**ISV 可挂**：✅
- 用途：注册自己的事件监听器
- 推荐：继承 `AbstractListPlugin`

### 2.14 beforeCreateListDataProvider (标品 1 个 · ISV 慎挂)

**执行顺序**：
1. `HRRoleListPlugin.beforeCreateListDataProvider` (L1762-L1764) → 注入 `HRRoleProvider` 自定义数据源

**ISV 可挂**：⚠️ 慎
- 用途：替换列表的数据查询逻辑
- 风险：🔴 极高 · 替换后整个列表数据来源都改了 · 标品的 viewableRoles 闸/group 过滤可能失效
- 推荐：99% 不需要 · 改字段优先 ISV 扩展元数据

### 2.15 billListHyperLinkClick (标品 1 个)

**执行顺序**：
1. `HRRoleListPlugin.billListHyperLinkClick` (L959-L986) → 点击行跳转 hrcs_modifyrole

**ISV 可挂**：✅
- 用途：自定义"点击行的跳转目标"
- 推荐：继承 `AbstractListPlugin`

---

## 三、所有可挂的 BarItem (按钮)

| 按钮 | 来源 | 标品行为 | ISV 可改 |
|---|---|---|---|
| 树工具栏 `btnnew` | 标品 | 弹窗 hrcs_rolegrp 添加分组 | ⚠️ 重写后果不可预知 |
| 树工具栏 `btnedit` | 标品 | 弹窗 hrcs_rolegrp 编辑分组 | ⚠️ 同上 |
| 树工具栏 `btndel` | 标品 | doDeleteGroup 删分组 | ⚠️ 同上 |
| `refresh` | 标品 | clearSelection + refresh | ✅ 可叠加自己的刷新逻辑 |
| `new` | 标品 | 跳 hrcs_newrole · groupId=当前节点 | ⚠️ 业务上慎改 |
| `delete` | 标品 | 23 SQL 级联（详 CS-04） | ⚠️ 在 beforeDoOperation 抢前 |
| `bar_enable` (启用) | 标品 | 同步 BizRoleComRole 启用 | ⚠️ 在 beforeDoOperation 抢前 |
| `bar_disable` (禁用) | 标品 | 同 enable 逆向 | ⚠️ 在 beforeDoOperation 抢前 |
| `bar_assignmember` (分配成员) | 标品 | 跳 hrcs_modifyrole + useroperation=assignmember | ✅ 可叠加 ISV 入参 |
| `btn_allocprem` (分配权限) | 标品 | 跳 hrcs_modifyrole | ✅ |
| `btn_batchgroup` (批量分组) | 标品 | 至少选 1 行 · 跳子页面 | ✅ |
| `bar_copy` (option) | 标品 | 单选 · 跳 hrcs_modifyrole + copy=1 | ⚠️ 单选限制 |
| `bar_initrole` | 标品 | 跳 hrcs_perminitrecord (角色权限初始化) | ✅ |
| `inituserperm` | 标品 | 跳 hrcs_perminitrecord (用户权限初始化) | ✅ |
| `bar_exportrole` | 标品 | 跳 hrcs_exportperm + RoleExportTask | ✅ |
| `exportroleperm` | 标品 | 跳 hrcs_exportperm + RoleUserExportTask | ✅ |
| `baritem_alteruserperm` | 标品 | 单选 · 跳"修改用户权限"页 | ✅ |
| `${ISV_FLAG}_*` (ISV 自定义) | ISV | (自定义) | ✅ ISV 完全控制 |

⚠️ **常见误解**：
- `bar_enable` / `bar_disable` 的 itemKey 跟 opKey 不一样
- itemKey="bar_enable" · opKey="enable"（HRRoleListPlugin.beforeDoOperation case "bar_enable"）
- ISV 抢前用 `args.getOperationKey()` 拿 opKey · 用 `pageCache.get("itemKey")` 拿 itemKey

---

## 四、扩展点风险等级速查

| 扩展点 | 风险 | ISV 频率 | 建议 |
|---|---|---|---|
| preOpenForm | ⚠️ 高（影响整页可达） | 🟡 中 | super 调 + 加自己 |
| setFilter | 🔴 高（影响行级权限） | 🟢 高 ⭐ | 必须叠加 · 不替换 |
| beforeItemClick | ⚠️ 中（按钮校验） | 🟢 高 ⭐ | RowKey 抢前 |
| beforeDoOperation | 🔴 高（核心拦截） | 🟢 高 ⭐⭐ | RowKey 抢前 |
| afterDoOperation | 🟡 中（后置） | 🟢 高 | PageCache 兜底 |
| createTreeListView | 🔴 极高 | 🔴 极低 | 99% 不需要 |
| beforeCreateListDataProvider | 🔴 极高 | 🔴 极低 | 99% 不需要 |
| 元数据加字段 | 🟡 中 | 🟢 高 ⭐ | ISV 扩展继承 |
| 元数据加按钮 | 🟢 低 | 🟢 高 | 直接 add_bar_item |
| 元数据加列 | 🟢 低 | 🟢 高 | path = perm_role.${ISV_FLAG}_xxx |

---

## 五、ISV 推荐挂载模板

### 5.1 ISV ListPlugin 模板（最常用 · 90% CS 都用）

```java
package com.${ISV_FLAG}.hr.role.formplugin;

import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.control.events.BeforeItemClickEvent;

public class TdkwYourListPlugin extends AbstractListPlugin {

    @Override
    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);
        // 在标品 viewableRoles 之后叠加自己的 QFilter
    }

    @Override
    public void beforeItemClick(BeforeItemClickEvent e) {
        super.beforeItemClick(e);
        // 自定义按钮点击前校验
    }

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {
        super.beforeDoOperation(e);
        // 标品 opKey / itemKey 抢前拦截
    }

    @Override
    public void afterDoOperation(AfterDoOperationEventArgs e) {
        super.afterDoOperation(e);
        // 后置（注意 selectedRows 可能已清空）
    }
}
```

### 5.2 ISV FormPlugin 模板（用在 hrcs_newrole / hrcs_modifyrole · 不本场景）

```java
package com.${ISV_FLAG}.hr.role.formplugin;

import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;

public class TdkwYourFormPlugin extends AbstractFormPlugin {
    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        // PR-004 死循环防护
        // this.getModel().beginInit(); ... endInit();
    }
}
```

---

## 六、ISV 不要继承 · 必须并列挂的类清单（PR-001 严格执行）

| 不要继承 | 推荐替代基类 | 原因 |
|---|---|---|
| `HRRoleListPlugin` | `AbstractListPlugin` 或 `HRStandardTreeList` | 场景专属 final 类（L260 · `public final class`）· 标品升级会改方法签名 |
| `HRAdminStrictPlugin` | 直接复用即可（已挂） / 或继承 `HRDynamicFormBasePlugin` | hrcs 11 表单共用 · 没有可扩展点 · 静态方法 isHrAdmin 可直接调 |
| `CommonTreeListView` | `AbstractTreeListView` | 树视图实现类 · 不暴露扩展接口 |
| `HRRoleListPlugin$HRRoleProvider` | `IListDataProvider` 接口 | 内部类 · ISV 不要尝试模仿（反编译可能不完整） |

→ 详见 `cosmic_hr_sdk_whitelist_audit.md` HR SDK 白名单 + `feedback_knowledge_code_samples_must_be_real.md` 代码示例必须实证。

---

## 七、Top 3 ISV 最常覆盖扩展点

1. **`HRRoleListPlugin.beforeDoOperation` 抢前**（CS-03 / CS-04 / CS-05 / CS-07） —— 因为本场景没 OP 链 · 所有定制必须挂这里
2. **`HRRoleListPlugin.setFilter` 叠加**（CS-06） —— 行级权限定制的唯一入口
3. **元数据加字段（不是覆盖）**（CS-01） —— ISV 扩展继承 perm_role · 不动标品

---

## 八、与其他场景对比

| 场景 | OP 插件数 | FormPlugin 数 | ISV 抢前次数 |
|---|---|---|---|
| `hrcs_rolelist` (本场景) | **0** | 2 | 高（必须） |
| `hrcs_dynascheme` | 4 | 3 | 低（OP 阶段更多扩展点） |
| `haos_adminorgdetail` | 6+ | 5+ | 低 |

→ 本场景因为没 OP · ISV 必须靠"FormPlugin 抢前"做事 · 这跟其他场景的扩展模式不同。
