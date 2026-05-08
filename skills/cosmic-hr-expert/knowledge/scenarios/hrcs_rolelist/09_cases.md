# 参考案例 · HR 角色管理列表 (hrcs_rolelist)

> **状态**: 🟢 基于反编译 + scene_doc + 同应用 hrcs_dynascheme 类比
> **confidence**: distilled（实战案例提炼自代码模式 · 历史项目 GitLab 路径待业务专家补充）
> **数据源**: CFR 反编译 + 同应用对比 (2026-04-28)

> 📌 **本文档作用**：列出 HR 角色管理列表场景的典型 ISV 实战案例 · 帮助 Claude / 开发者理解"什么样的需求该用什么 CS"。

---

## 一、案例索引（按需求类型）

| 案例 | CS 关联 | 业务类型 | 复杂度 |
|---|---|---|---|
| Case-01 角色画像分类（高级管理 / 中层管理 / 业务操作） | CS-01 | 元数据 + 列表列 | 🟢 低 |
| Case-02 角色等级随分组联动（避免漏填） | CS-01 + CS-02 | 元数据 + FormPlugin | 🟡 中 |
| Case-03 高风险角色 HR 经理双签 | CS-01 + CS-03 | ListPlugin 抢拦 | 🟠 高 |
| Case-04 ISV 审批流引用反查 | CS-01 + CS-04 | ListPlugin 抢拦 + ISV 表 | 🟠 高 |
| Case-05 角色启停 LDAP 同步 | CS-05 | BEC 发布 | 🟡 中 |
| Case-06 分组管理员只见自己组角色 | CS-06 | ListPlugin setFilter | 🔴 极高（行级权限） |
| Case-07 入职旺季批量赋权 Excel 导入 | CS-07 | ListPlugin + 自建表单 | 🟠 高 |

---

## 二、Case-01 · 角色画像分类（最简单·最常做）

### 客户背景

某保险集团 HR 项目 · 有 800+ 角色 · 散落在 50 多个分组里 · BI 想看"按角色等级划分的分布" → 标品 perm_role 没有"等级"字段。

### 需求

- 给每个角色加 1 个"角色等级"字段（高/中/低）
- 列表显示
- 编辑时可填
- BI 报表可读

### 落地方案（CS-01）

详见 `06_customization_solutions.md` CS-01。

**关键步骤**：
1. 建 ISV 扩展元数据 `${ISV_FLAG}_perm_role_ext` 继承 `perm_role`
2. 加 `${ISV_FLAG}_rolelevel` ComboField
3. 在 hrcs_rolelist 列表加列 `perm_role.${ISV_FLAG}_rolelevel`
4. 在 hrcs_modifyrole 元数据上也加该字段（让编辑时可填）

### 工时估计

- 元数据修改：0.5 人日
- 测试 + 验收：0.5 人日
- 总计：1 人日

### 历史 GitLab（待补）

```
[待业务专家补充] gitlab.kingdee.com/customer-x/hr-perm-customization/MR-001 · 2025-Q1
```

---

## 三、Case-02 · 角色等级随分组联动

### 客户背景

Case-01 落地后 · HR 反馈"管理员经常忘填等级 / 填错等级" · 因为他们已经按等级分了组（高级管理组下的角色都该是"高"）· 希望系统自动联动减负担。

### 需求

- 用户选"角色分组" → 自动设置"角色等级"（与分组一致）
- 用户能手动覆盖（不强制）

### 落地方案（CS-01 + CS-02）

详见 `06_customization_solutions.md` CS-02。

**关键步骤**：
1. 在 `hrcs_rolegrp` 元数据上加 `${ISV_FLAG}_levelmark` 字段（每个分组的等级标签）
2. 写 ISV FormPlugin `TdkwRoleLevelAutoFillPlugin` 监听 `group` 字段 propertyChanged
3. 挂载到 `hrcs_newrole` + `hrcs_modifyrole`（不挂列表 · 列表没 propertyChanged）
4. PR-004 死循环防护用 beginInit/endInit

### 工时估计

- 元数据修改：0.5 人日
- FormPlugin 编码：0.5 人日
- 测试 + 验收：0.5 人日
- 总计：1.5 人日

### 历史 GitLab（待补）

```
[待业务专家补充]
```

---

## 四、Case-03 · 高风险角色 HR 经理双签

### 客户背景

某金融集团 IT 治理要求"超管类角色 / 数据敏感类角色"的禁用必须有 HR 经理审批 · 防止"管理员误操作"。

### 需求

- 给角色加 risktag 字段（数据敏感 / 超管权限 / 审计相关 · 多选）
- 当用户尝试禁用 / 删除"高风险"角色时 · 弹"HR 经理双签"流程
- 双签通过后才允许禁用 / 删除

### 落地方案（CS-01 + CS-03）

详见 `06_customization_solutions.md` CS-03。

**关键步骤**：
1. 用 CS-01 加 risktag 字段
2. 写 `TdkwRoleHighRiskValidatePlugin` 抢前拦
3. 拦截后跳转自建审批流表单 `${ISV_FLAG}_managersign`
4. 审批流通过后 · 重新触发 enable/disable

### 工时估计

- 元数据修改：0.5 人日
- 自建审批流表单 + 流程设计：3 人日
- ListPlugin 编码：1 人日
- 双签业务逻辑（多签 / 超时 / 撤回）：2 人日
- 测试 + 验收：1 人日
- 总计：7.5 人日

### 历史 GitLab（待补）

```
[待业务专家补充]
```

---

## 五、Case-04 · ISV 审批流引用反查

### 客户背景

Case-03 上线后 · 客户 IT 部又有了一套自建的"权限审批流"系统（基于自建表 ${ISV_FLAG}_role_workflow）· 以及一套外部 LDAP 同步系统（${ISV_FLAG}_external_sync）· 两个系统都引用了 perm_role.id。HR 反馈"删除角色时 · 这两个系统的配置变成空指向 · 后续报错"。

### 需求

- 删除角色前 · 反查这 2 张 ISV 表
- 任一表有"未停用"的引用 → 拦截删除 + 提示用户

### 落地方案（CS-04）

详见 `06_customization_solutions.md` CS-04。

**关键步骤**：
1. 写 `TdkwIsvRoleDownstreamCheckPlugin` 抢前拦
2. 在 beforeDoOperation 查 2 张 ISV 表
3. 任一表有"占用"记录 → setCancel + 提示
4. 不需要继承 HRRoleListPlugin · 也不需要改标品 getRefrencedRoles

### 工时估计

- ListPlugin 编码：1 人日
- 给 ISV 表加索引（roleId/target_role）：0.5 人日
- 测试 + 验收：1 人日
- 总计：2.5 人日

### 历史 GitLab（待补）

```
[待业务专家补充]
```

---

## 六、Case-05 · 角色启停 LDAP 同步（BEC 模式）

### 客户背景

某外资集团有 LDAP 系统对接 · 当 HR 在苍穹禁用一个角色 · 希望同步到 LDAP 对应的"角色组"也禁用 · 防止用户在 SSO 后还能用旧权限。

### 需求

- 当用户启用 / 禁用角色 · 通过 BEC 发事件
- 订阅方（LDAP 同步插件）自动同步到 LDAP

### 落地方案（CS-05 · 涉及 PR-011）

详见 `06_customization_solutions.md` CS-05。

**关键步骤**：
1. 在 portal 注册 `${ISV_FLAG}_event_role_enable` / `${ISV_FLAG}_event_role_disable` 事件号
2. 写 `TdkwRoleStatusEventPublisherPlugin` 监听 afterDoOperation · 发 BEC
3. 写订阅方 `TdkwLdapSyncEventSubscriberPlugin` 实现 IEventServicePlugin · 调 LDAP API
4. 异常处理：BEC 失败不污染本场景标品流程

### 工时估计

- 注册事件号 + 发布 ListPlugin：1 人日
- 订阅方实现 + LDAP API 集成：3 人日
- 测试（含 LDAP 联调）：2 人日
- 总计：6 人日

### 历史 GitLab（待补）

```
[待业务专家补充]
```

---

## 七、Case-06 · 分组管理员只见自己组角色

### 客户背景

某大型集团 · 5 个 BU · 每个 BU 有 10+ HR 管理员 · 每人只管自己 BU 的角色 · 看到全集团角色信息**违反数据安全合规**。需要"分组管理员"角色。

### 需求

- 当用户是"分组管理员"（自建 ${ISV_FLAG}_user_rolegrp_admin 表标记）时 · 只看到自己管的分组下的角色
- 顶级 HR 管理员仍看全部
- 同时不影响标品 viewableRoles 闸

### 落地方案（CS-06）

详见 `06_customization_solutions.md` CS-06。

**关键步骤**：
1. 建 ISV 表 `${ISV_FLAG}_user_rolegrp_admin` (user / rolegrp / scope_type=GROUP_ADMIN)
2. 写 `TdkwGroupAdminListFilterPlugin` 实现 setFilter 叠加
3. 顶级 HR 管理员 isTopLevelAdmin 跳过叠加
4. 5 用户级别测试（顶级管理员 / 普通管理员 / 分组管理员 / 非管理员 / 跨组管理员）

### 工时估计

- 元数据 + 业务表：1 人日
- ListPlugin 编码：1 人日
- 5 用户级别测试：2 人日
- 总计：4 人日

### 历史 GitLab（待补）

```
[待业务专家补充]
```

⚠️ **风险特别提醒**：本案例是**行级权限定制** · 设错会导致数据泄露 · 必须做严格的 5 用户级别测试。

---

## 八、Case-07 · 入职旺季批量赋权 Excel 导入

### 客户背景

某零售集团 9 月入职高峰 · 一次入 50 个店长 · 每个店长要赋 5 个角色（店长 / 库存查看 / 销售管理 / 报表查看 / 投诉处理）。标品的 bar_assignmember 单选 · 操作 250 次 · 太慢。

### 需求

- 加工具栏按钮"批量赋权(Excel)"
- 选中 N 个角色 · 上传 Excel（user_number / role_number 两列） · 后台批量绑定

### 落地方案（CS-07）

详见 `06_customization_solutions.md` CS-07。

**关键步骤**：
1. 元数据上加 BarItem `${ISV_FLAG}_batchassignmember`
2. 写 `TdkwBatchAssignMemberEntryPlugin` ListPlugin · 校验 + 跳转
3. 自建上传表单 `${ISV_FLAG}_role_batchassign_upload` + 上传 OP（用 OperationServiceHelper.executeOperate("save", "hrcs_userrolerelat", arr) 走标品保存链）
4. 分批：200 行/批 · TX.required + 异常 markRollback

### 工时估计

- 元数据按钮：0.5 人日
- 入口 ListPlugin：0.5 人日
- 上传表单 + Excel 解析 + 批量保存：3 人日
- 测试（含异常 + 性能）：1.5 人日
- 总计：5.5 人日

### 历史 GitLab（待补）

```
[待业务专家补充]
```

---

## 九、案例的"反模式"清单（绝对不要做的）

| 反模式 | 出错案例 | 正确做法 |
|---|---|---|
| 直接 INSERT hrcs_userrolerelat 跳过标品 | Case-07 早期版本：bar_assignmember 入口跳过 OP 链 → 没写日志 → 半年审计时发现 | 走 OperationServiceHelper.executeOperate("save", ...) |
| 在 OP 阶段做 BEC（其实本场景没 OP） | 误把"OP 阶段发 BEC" 套用本场景 → 报 OP 不存在错误 | 必须 FormPlugin afterDoOperation |
| 继承 HRRoleListPlugin | Case-03 早期版本：继承导致升级时 super.beforeDoOperation 签名变了 → 编译失败 | 并列挂 AbstractListPlugin |
| 改 perm_role 元数据加字段 | Case-01 早期版本：直接改 perm_role → 平台 ISV 归属拦截 | ISV 扩展继承 perm_role |
| setFilter 用 super 替换 | Case-06 早期版本：把标品过滤替换 → 用户看到不该看的角色 | 必须 super 后 add 不替换 |
| BEC 异常时 throw | Case-05 早期版本：BEC 失败时未 catch → 标品启用失败 → 用户投诉 | 必须 catch + log |
| 删除链塞自己的 SQL | Case-04 早期版本：在 setCancel 后跑 SQL 想清 ISV 表 → 实际没跑 | 在 ListPlugin afterDoOperation 监听 itemKey="delete" 后做 |

---

## 十、案例选择决策树（给 Claude / IDEA 用）

```
用户描述包含 "加字段 / 角色属性 / 标签" → CS-01 (Case-01)
用户描述包含 "联动 / 自动填 / 切换分组带出" → CS-02 (Case-02)
用户描述包含 "高风险 / 双签 / 审批" + (删除 / 禁用) → CS-03 (Case-03)
用户描述包含 "ISV 表引用 / 自建配置 / 反查依赖" + (删除) → CS-04 (Case-04)
用户描述包含 "LDAP / 外部系统 / 通知 / 同步" → CS-05 (Case-05)
用户描述包含 "分组管理员 / 只看自己 / 数据隔离 / 行级权限" → CS-06 (Case-06)
用户描述包含 "批量 / Excel / 入职 / 赋权" → CS-07 (Case-07)
其他需求 → 先看 01_capability_boundary.md 第六节速查表 → 再回 06 找对应 CS
```

---

## 十一、AI 自动索引（待跑脚本）

```bash
# 跑一次以下脚本可自动从 GitLab 拉历史项目案例
python scripts/search_historical_customizations.py --scenario hrcs_rolelist
```

输出会按"客户 / 需求摘要 / GitLab 路径 / 时间"格式填回本文件。
