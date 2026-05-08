# 影响分析 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 基于反编译 + 跨场景关联
> **数据源**: PermfilesListPlugin / HRAdminStrictPlugin / 23 opKey · 2026-04-28

---

## ⭐ 一、本场景定制对全系统的潜在影响

### 1.1 缓存层影响（最大敏感）

`HRPermCacheMgr.clearAllCache()` 是**全系统级**缓存清理 · 不只是清当前用户的：

| 操作 | 何时触发 clearAllCache | 影响范围 |
|---|---|---|
| save / enable / disable / delete | PermFilesSaveOp 内部（推断 · jar 缺失） | 全 hrcs 用户的鉴权缓存瞬时失效 |
| btn_clearcache 手动按钮 | itemClick L505-L512 | 全 hrcs 用户 + 管理员组缓存 |
| ISV 自定义 OP 后置 | 如果 ISV 也加 clearAllCache | 重复清理 · 性能损耗（无功能问题） |

⚠️ **风险**：如果 ISV 在 `afterDoOperation`（FormPlugin 阶段 · 不在事务里）再次清缓存 · 每次 disable 操作就有 2 次 RPC 到 Redis · 大批量场景（500+ 行）会有性能抖动。

### 1.2 数据隔离影响（admingroup level 决定可见范围）

`setFilter` L1030-L1037 标品逻辑：
- admingroup level == -1（无 admingroup）→ 加 org 限制（仅自己的）
- admingroup level > 2（中下级管理员）→ 加 org 限制
- admingroup level <= 2（顶级 / 二级管理员）→ 不加 org 限制（看全部）

⚠️ **ISV 改 setFilter 时（CS-06）注意**：
- 不要 clear()/reset → 抹掉标品 admingroup 限制 · 越权
- 必须 super 后 add → ISV 限制叠加在标品之上
- 顶级管理员（level==-1 / <=2）的处理 → ISV 可选择继承标品规则或独立判断

### 1.3 角色绑定影响（disable 级联）

```java
// 反编译 setEnable L767-L781
if (HRStringUtils.equals(PERFILE_STATUS_0, enable)) {
    RoleServiceHelper.disablePermfile(idList);  // ⚠️ 级联失效 hrcs_userrolerelat
}
```

⚠️ **disable 一个档案 → 自动清空它的所有角色绑定**。如果客户的业务流是"管理员先把档案 disable 一段时间 · 再 enable 回来" · enable 时不会自动恢复角色 · 用户必须手动重新分配。

### 1.4 异步任务影响（syncperm / exportuserperm）

| 任务 | 单实例约束 | 数据规模 | 风险 |
|---|---|---|---|
| SyncPermFilesTask | 同时只能跑 1 个（HRAppCache.syncPermFilesTaskId） | 千级员工·分钟级 | 大批量场景占用 1 个调度 worker 较长时间 |
| PermFilesExportTask | 同时多个可并发（按 permFileIds） | 几十到几百行 | OK |

⚠️ ISV 不要自起同名 taskClassname · 否则可能跟标品任务冲突。

---

## 二、跨 form 影响矩阵（11 hrcs 表单 · 共用资源）

| 共用资源 | 涉及表单 | 改动影响 |
|---|---|---|
| `HRAdminStrictPlugin` | hrcs 11 个表单（admin/userpermfile/role/...） | 改 1 处全坏 · ISV 不要继承 |
| `HRPermCacheMgr` 全局缓存 | 所有 hrcs 鉴权 | 任何 disable / btn_clearcache 都清空 |
| `hrcs_userrolerelat` | dynascheme / userpermfile / role 都写它 | sourcetype 字段必须正确填（"4"=动态方案 · 默认/其他=本场景 · 待业务确认） |
| `hrcs_permfilegrp` | userpermfile / batchgroup / permfilegrptree 都引用 | 加字段需评估对所有引用方的兼容 |
| `RoleServiceHelper` | dynascheme / userpermfile 都调 disablePermfile / showRoleF7 等 | 修改 helper 逻辑跨场景生效 |

---

## 三、ISV 字段加在哪个层 · 影响哪些场景

| ISV 字段位置 | 影响场景 |
|---|---|
| 加在 `hrcs_userpermfile`（CS-01） | 列表 / 编辑表单 / save 链 / disable 链 / hrcs_userassignrole 弹窗（如有引用） |
| 加在 `hrcs_permfilegrp` | 树节点 / 分组管理表单 / batchgroup 弹窗 |
| 加在 `hrcs_permfilegrpmember`（CS-07） | batchgroup 后写入 |
| 加在 `hrcs_userrolerelat` | dynascheme / copyperm / 角色 F7 |

⚠️ **加在 hrcs_userrolerelat 的字段 · 要兼顾 dynascheme 场景** —— 动态方案分配出来的角色（sourcetype="4"）可能不会自动填 ISV 字段 · 需要 ISV 在 dynascheme 场景的 audit 阶段也加补写逻辑。

---

## 四、平台升级影响

| 升级类型 | 风险 |
|---|---|
| 标品加新字段到 hrcs_userpermfile | 低 · 不影响 ISV |
| 标品改 PermfilesListPlugin 工具栏分发 | 中 · 如果 ISV 拦了 bar_xxx 而标品改了语义 · ISV 可能错误执行 |
| 标品改 PermFilesSaveOp 内部 | 高 · 但 ISV 不继承（PR-001）不直接受影响 |
| 标品改 setFilter admingroup 逻辑 | 高 · ISV 的 super 后追加可能与标品新逻辑冲突（如标品新加了 customenable 限制） |
| 苍穹 BEC eventNumber 清理 | 中 · ISV 配置的 eventNumber 在开发平台被误删 → 事件丢失 |

⚠️ 每次苍穹版本升级（HR 8.0 → 9.0 等）后 · 必须重跑反编译 + diff PermfilesListPlugin · 关注：
1. 工具栏 itemKey 列表是否变（bar_new / btn_initdata / btn_copyperm 等）
2. setFilter 逻辑是否变（admingroup level 阈值）
3. closedCallBack actionId 列表是否变

---

## 五、性能影响 · ISV 代码热点位置

### 5.1 setFilter 是性能关键

每次列表打开 / 树节点点击 / 过滤器变更 · 都触发 setFilter（**高频**）。ISV 在这层加 QFilter 必须：

| 反模式 | 推荐 |
|---|---|
| 在 setFilter 里查数据库（如查"当前用户负责的项目"） | 缓存到 PageCache · setFilter 直接用缓存 |
| 在 setFilter 里发 RPC（如调外部 BPM） | 绝对禁止 · 会卡死列表 |
| 加 not in (...大集合...) QFilter | 改用 in (...) 反向过滤 · DB 索引利用更好 |

### 5.2 propertyChanged 是中频

字段联动每次用户改 user/org 等都触发。要：
- beginInit / endInit 防死循环（PR-004）
- 不要每次 propertyChanged 都查 DB · 用 PageCache 或 customParam

### 5.3 onAddValidators 是低频但事务内

每次保存触发一次。Validator 内 query 必须在事务内做（看到一致快照）· 但不要在 Validator 里发外部 RPC。

---

## 六、鉴权影响（与企业 SSO / 苍穹账号体系）

`HRAdminStrictPlugin.preOpenForm` 双闸：
1. `(isAdmin OR isCosmic) AND isHrAdmin()`
2. 失败弹"您无法访问该功能，因为您不是HR领域管理员。"

⚠️ ISV 项目里如果用了"项目专员"等非 HR 域角色 · 这些角色不能打开 hrcs_permfilelist。如果业务真要这些角色看一些子集 · 必须：
- 给这些角色加 admingroup（HR 域）
- 或开发独立列表（非 hrcs_permfilelist）· 用同样的 hrcs_userpermfile 数据源

---

## 七、影响汇总表（场景级 · 给 ISV 项目经理用）

| 影响维度 | 高 / 中 / 低 | 说明 |
|---|---|---|
| 性能 | 中 | 缓存清理频繁 / setFilter 高频 |
| 数据一致性 | 中 | clearAllCache 不在事务内 · 有不一致窗口 |
| 跨场景污染 | 高 | 共用 HRAdminStrictPlugin / HRPermCacheMgr · 改一处全坏 |
| 标品升级兼容 | 中 | 工具栏 itemKey 是潜在变化点 |
| ISV 自定义字段维护 | 低 | isvAccessable=true 后基本稳定 |
| BEC 集成（CS-05） | 高 | eventNumber 必须预配置 · 自接 MQ 反模式 |
| 异步任务 | 中 | syncperm 单实例约束 · 大批量耗时 |

---

## 八、回归测试矩阵（ISV 改后必跑）

| 场景 | 步骤 | 期望 |
|---|---|---|
| 顶级管理员开列表 | 用 isAdmin 用户登录 · 打开 hrcs_permfilelist | 看到全部档案 |
| 中级管理员开列表 | admingroup level=2 用户 | 看到自己组织的档案 |
| 低级 / 无 admingroup | 普通员工 | 弹"您不是HR领域管理员" |
| save 唯一性（CS-03） | 重复建 (user, org) | addErrorMessage |
| disable 级联 | disable N 行 | hrcs_userrolerelat 对应 N×M 行失效 |
| disable 后 enable | 同档案再 enable | permfileenable=1 · 但角色绑定不恢复 |
| copy 跨用户 | A 复制到 B | hrcs_userrolerelat 加 N 行（每个角色 1 行） |
| batchgroup | 选 N 档案 + M 分组 | hrcs_permfilegrpmember 加 N×M 行 |
| syncperm | 选员工类型 | 后台任务 · 完成后 newCount/updCount |
| BEC 发布（CS-05） | disable 1 行 | 订阅方 handleEvent 触发 |
| ISV 字段（CS-01） | 编辑表单填 ISV 字段 · 保存 | 字段成功落 hrcs_userpermfile |
| 列表 ISV 过滤（CS-06） | 项目经理只看自己项目 | 过滤生效 · 顶级管理员仍全看 |

---

## 九、应急回滚方案

如果 ISV 定制上线后发现问题：

| 问题 | 回滚步骤 |
|---|---|
| ISV OP 阻断了所有 save | 在开发平台禁用该 ISV OP · 不需要重启 |
| ISV ListPlugin 列表打不开 | 同上 · 禁用 ISV ListPlugin |
| ISV BEC 发不出 / 太多 | 在开发平台禁用 eventNumber 订阅 |
| ISV 字段值错乱 | 用 SQL 直接 UPDATE · 别走 OP（避免触发链路） |
| 缓存出现不一致 | 工具栏【清空缓存】（btn_clearcache） · 手动 clearAllCache |

⚠️ **不要在生产直接删 ISV 插件类**（jar）· 必须先从开发平台禁用 · 等下次发版再删类。
