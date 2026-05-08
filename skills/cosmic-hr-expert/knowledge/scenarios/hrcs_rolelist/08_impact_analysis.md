# 变更影响面 · HR 角色管理列表 (hrcs_rolelist)

> **状态**: 🟢 基于反编译 + main_form.xml + 23 SQL 级联清理路径实证
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

> 📌 **本文档作用**：列出在本场景做 ISV 改动时，会影响哪些下游模块、哪些数据表、哪些操作行为。

---

## 一、改动本场景"会"影响的下游表/模块

### 1.1 直接级联表（删除时·标品自动清理 · 23 SQL）

| 表 | 触发时机 | 操作 | 失败影响 |
|---|---|---|---|
| `perm_roleperm` | delete | DELETE WHERE roleid IN (...) | 角色功能权限丢失 → 可补 |
| `perm_fieldperm` / `perm_rolefieldperm` | delete | DELETE WHERE id/role IN (...) | 字段权限丢失 → 可补 |
| `perm_dataperm` / `perm_roledataperm` | delete | DELETE WHERE id/role IN (...) | 数据权限丢失 → 可补 |
| `perm_userrole` | delete | DELETE WHERE role IN (...) | 用户角色绑定丢失 ⚠️ 用户失权 |
| `perm_role` | delete | DELETE WHERE id IN (...) | 角色物理删 · 不可逆 |
| `hrcs_userrolerelat` / `hrcs_userrole` | delete | DELETE WHERE role / userrolerealt IN (...) | 用户-角色关联丢失 ⚠️ 同上 |
| `hrcs_userdatarule` 等 | delete | 6 张表 DELETE | HR 用户数据规则丢失 |
| `hrcs_role` / `hrcs_roledatarule` 等 | delete | 7 张表 DELETE | HR 角色配置丢失 |
| `hrcs_rolefield` / `hrcs_roleopenscope` / `hrcs_roleassignscope` | delete | 3 张表 DELETE | HR 角色字段/范围配置丢失 |
| `hrcs_roledimension` / `hrcs_roledimgroup` | delete | 2 张表 DELETE | HR 角色维度配置丢失 |
| `T_Perm_BizRoleComRole` | enable / disable | 同步 FEnable | 业务角色复用关系状态错位 |

→ **删除一个角色相当于删除"角色 + 配置 + 用户关联 + 业务关系" 4 大类** · 不可逆。

### 1.2 反查依赖表（标品已查 · 阻止删除 · 详见 CS-04）

| 表 | 反查时机 | 拦截行为 | 标品代码位置 |
|---|---|---|---|
| `hrcs_dynascheme.roleentry.role` | delete checkCanDel | 引用了 → "动态授权方案"的"角色"字段引用了此数据 → 拦截 | `getRefrencedRoles` L1555-L1568 |

⚠️ **ISV 自建反查表（如 CS-04 的 `${ISV_FLAG}_role_workflow` / `${ISV_FLAG}_external_sync`）也要在此清单内自己加** —— 标品不会自动帮 ISV 表反查。

### 1.3 跨场景跳转（点击/操作的下游）

| 跳转目标 | 触发 opKey | 行为 |
|---|---|---|
| `hrcs_modifyrole` | 行 hyperLink / `bar_assignmember` / `bar_copy` / `btn_allocprem` | 角色权限矩阵编辑 |
| `hrcs_newrole` | `new` | 新建角色简单表单 |
| `hrcs_perminitrecord` | `bar_initrole` / `inituserperm` | 权限初始化任务列表 |
| `hrcs_exportperm` | `bar_exportrole` / `exportroleperm` | 导出权限任务页 |
| `hrcs_rolegrp` | 树工具栏 `btnnew` / `btnedit` | 角色分组维护 |

---

## 二、改动本场景"会被"影响的上游

### 2.1 上游配置数据

| 表 | 用途 | 改了会怎样 |
|---|---|---|
| `hrcs_admingrp` | 管理员组（canOperateRole 闸） | 用户管理员组变化 → 列表 viewableRoles 变 → UI 显示行变 |
| `hrcs_rolegrp` | HR 角色分组（树左侧） | 加/删分组 → 树节点显示变 |
| `perm_admingrp` | BOS 管理员组 | 同上 |
| `perm_rolegroup` | BOS 角色分组 | createTreeListView 用它做 ENTITYTYPE_BOSROLEGRP |

### 2.2 上游业务逻辑

| 业务规则 | 影响 |
|---|---|
| `HRRolePermHelper.queryViewableRoles(uid)` | 行级过滤的依据 · 这个函数的逻辑变了 → 列表显示变 |
| `HRRolePermHelper.queryUserAdminGroups(uid)` | canOperateRole 闸的依据 · 函数逻辑变 → 删除/启用/禁用的可操作行变 |
| `HRRolePermHelper.queryUserAdminGroupInfos(uid)` | 同上 · 返回 Map<groupId, longnumber> |
| `PermissionServiceHelper.isAdminUser(uid)` | preOpenForm 准入闸 · 平台级 BOS 函数 · 不应改 |
| `HRAdminService.isHrAdmin()` | 同上 · HR 域 · 不应改 |
| `RoleServiceHelper.getRoleMembers(rolesIds)` | checkCanDel 用 · 决定 memberCount · 改了会让"启用中且有成员的角色仍被删除"风险变高 |

---

## 三、ISV 改动 → 影响传播路径

### 3.1 加字段 (CS-01)

```
ISV 加字段（perm_role 子实体）
  ↓
影响列表显示（hrcs_rolelist 加列引用 perm_role.${ISV_FLAG}_xxx）
  ↓
影响 hrcs_modifyrole / hrcs_newrole 编辑表单（也引用同字段）
  ↓
影响 hrcs_dynascheme.roleentry F7 弹窗（看到角色列表时也带字段）
  ↓
影响导出 (bar_exportrole / exportroleperm)
  ↓
影响 BI 报表 / 第三方系统读 perm_role 视图
```

### 3.2 加 setFilter 叠加 (CS-06)

```
ISV 加 setFilter
  ↓
影响列表行可见性（hrcs_rolelist）
  ↓
影响导出（bar_exportrole 导出的也是过滤后数据）
  ↓
影响 F7 弹窗的 isLookUp 模式（其他场景用 hrcs_rolelist F7 选角色时·过滤也生效）⚠️
  ↓
⚠️ 其他场景用 hrcs_rolelist 做 F7 选择 · 设错过滤会让其他场景选不到角色
```

### 3.3 加 BEC 发布 (CS-05)

```
ISV 在本场景加 BEC 发布（enable/disable）
  ↓
影响订阅 BEC 的 ISV 自建插件（必须先 portal 注册 eventNumber）
  ↓
影响第三方 LDAP 同步 / 审计模块（如有订阅）
  ↓
不影响标品（标品 0 处发 · 不会冲突）
```

### 3.4 加删除前置 (CS-04)

```
ISV 在 beforeDoOperation 抢前拦
  ↓
不影响标品 checkCanDel 链（顺序：ISV 先拦 → 通过后才到标品反查 dynascheme）
  ↓
不影响标品 23 SQL 级联（一旦通过 ISV + 标品双重校验 · 走标品逻辑）
  ↓
影响用户体验（用户先看 ISV 提示 · 解决后再看标品提示 · 两层）
```

---

## 四、不能改的"硬约束"清单（改了出大事）

| 硬约束 | 风险 | 实证位置 |
|---|---|---|
| `setFilter` 用 super 替换标品 viewableRoles 闸 | 🔴 数据泄露 · 管理员看到不该看的角色 | HRRoleListPlugin.setFilter L367 |
| 删除链塞自己的 SQL 跳过标品 doDeleteHrRole | 🔴 数据不一致 · 23 表清不干净留脏数据 | HRRoleListPlugin.doDeleteHrRole L617 |
| 直接改 `perm_role` / `hrcs_role` 元数据加字段 | 🔴 ISV 隔离破坏 · 标品升级覆盖 | `isv_ownership_redline.md` |
| 把 QueryListModel 改成 BillFormModel | 🔴 元数据架构变更 · 不可逆 | main_form.xml ModelType |
| 在 BEC 异常时 throw 上去 | 🔴 标品 enable/disable 跟着失败 | CS-05 踩坑 |
| 删除 `T_Perm_BizRoleComRole` 同步逻辑 | 🔴 业务角色复用关系状态错位 | doDisableRole L1401 |
| 改 `HRAdminStrictPlugin` 的 isHrAdmin 闸 | 🔴 准入控制失效 | HRAdminStrictPlugin.java L48 |

---

## 五、生产事故案例（参考·非本场景实战）

### 5.1 类似案例：`haos_adminorgdetail` 改了 setFilter（参考）

某 ISV 在 haos_adminorgdetail 上改了 setFilter · 原意是"给当前管理员只显示自己的组" · 但是没注意 super.setFilter 已经加了"跨域过滤" · 自己的 QFilter add 在 super 之前 → super.setFilter 替换掉了自己的 → 用户实际看到所有组的数据 · 数据泄露事故。

**教训**：本场景的 setFilter 必须 super 之后 add（CS-06 已强调）。

### 5.2 类似案例：在 OP 阶段做 BEC 发布

某 ISV 在 hjm_jobhr 加 BEC 时挂在 endOperationTransaction 阶段 · 而不是 afterExecuteOperationTransaction · 主事务还没提交就发了事件 → 接收方查不到数据 → 业务故障。

**教训**：本场景没 OP · 但 ISV 在 FormPlugin afterDoOperation 发 BEC 时也要确保**业务动作已生效**（标品 saveServiceHelper.save 已完成）· 详 CS-05。

### 5.3 类似案例：删除链塞自己的 SQL

某客户 ISV 在标品 doDeleteHrRole 完成后跑了一段裸 SQL 想"额外清理 ISV 自建的 user-role-extra 表" · 但是放错位置了（在 setCancel(true) 之后）· 导致 SQL 实际没跑 · ISV 表残留脏数据 · 半年后才被发现。

**教训**：不要在 doDeleteHrRole 后加裸 SQL · 应该在自己的 ListPlugin afterDoOperation 监听 itemKey="delete" 后做（且必须实证 selectedRows 是否还在 / 不在则用 PageCache 兜底）。

---

## 六、改动前的 Pre-Check 清单（自查）

ISV 在改本场景前，必须过一遍：

- [ ] 改的是 ISV 扩展元数据 · 不是 perm_role / hrcs_role 本身（PR-001 / `isv_ownership_redline.md`）
- [ ] ListPlugin 的 RowKey 排在 HRRoleListPlugin 之前（如果要抢前）
- [ ] setFilter 用 add 而不是 set / replace
- [ ] beforeDoOperation 拦截后写日志（addLogNoOpKey 跟标品风格一致）
- [ ] BEC 在 portal 上预先注册 eventNumber
- [ ] 异常 catch 不抛 · log 即可（不污染标品流程）
- [ ] selectedRows 用法实证（有些时候已被 clearSelection · 必须 PageCache 兜底）
- [ ] 性能：批量 ≥ 200 行的查询 · 加索引；批量 ≥ 500 行的写 · 分批
- [ ] 跑 cosmic-replay 测试用例验证「删/启/禁」3 大动作
- [ ] CS-04 / CS-06 涉及行级权限 / 反查 · 必须找业务侧做"看得见就该看见 · 看不见就不该看见"的 5 用户级别测试

---

## 七、跟 SCM-meta-grade 跨场景影响（与 dynascheme / userrolerelat 联动）

| 改本场景 | 是否影响 dynascheme | 是否影响 hrcs_userrolerelat |
|---|---|---|
| 加字段（CS-01） | ⚠️ 间接（dynascheme.roleentry 选角色 F7 时看到新字段） | ⚠️ 间接（同上） |
| 加 setFilter 叠加（CS-06） | ⚠️ 影响（dynascheme 的角色 F7 用 hrcs_rolelist 做下拉 · 过滤生效） | ❌ 不影响（直接的 user-role 关联表，不用 hrcs_rolelist 当 F7） |
| 加删除前置（CS-04） | ❌ 不影响（dynascheme 的反查由它自己处理） | ❌ 不影响 |
| 加 BEC 发布（CS-05） | ❌ 不影响标品 dynascheme | ❌ 不影响标品 hrcs_userrolerelat |
| 加批量赋权（CS-07） | ❌ 不影响 | ✅ 直接影响（写入 hrcs_userrolerelat） |

→ 本场景的改动**主要影响下游 hrcs_modifyrole / hrcs_dynascheme F7 / 导出系统**。

---

## 八、推荐验收测试清单

ISV 在本场景做完改动后，推荐跑 6 类验收：

1. **行级权限测试**（CS-06 改了 setFilter 必跑）：5 个角色 · 4 个用户（顶级管理员 / 分组管理员 / 普通管理员 / 非管理员） · 验证每个用户看到的列表行
2. **删除链测试**（CS-03 / CS-04 改了 beforeDoOperation 必跑）：a) 预置角色删 · b) 被 dynascheme 引用角色删 · c) 启用中角色删 · d) ISV 自建表引用角色删
3. **启用/禁用测试**（CS-03 改了必跑）：a) 高风险角色启用/禁用 · b) 普通角色启用/禁用 · c) 已启/已禁重复操作
4. **复制测试**（如修改了 bar_copy）：a) 启用中复制（应被拦） · b) 禁用复制 · c) 多选复制（应被拦）
5. **BEC 测试**（CS-05 加了必跑）：a) portal 是否注册 · b) trigger 是否成功 · c) 订阅方是否收到 · d) 异常时标品是否不受影响
6. **导入导出测试**（CS-07 加了必跑）：a) Excel 上传 5 行 · b) 上传 1000 行（性能） · c) 上传含错误数据（user 不存在）的整批回滚
