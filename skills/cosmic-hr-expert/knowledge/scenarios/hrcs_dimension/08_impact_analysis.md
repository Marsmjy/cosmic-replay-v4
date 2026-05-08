# 变更影响面 · 维度管理 (hrcs_dimension)

> **状态**: 🟢 基于反编译 4 类（DimensionNewEdit/List/DeleteOp/HRAdminStrictPlugin）+ scene_doc.json 33 字段 + 11_upstream_downstream_logic.md
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、字段变更影响面（按字段分级）

### 1.1 主表字段（27 个 · 改前必看）

| 字段 | 改了影响 | 安全等级 |
|---|---|---|
| `id` | 🔴 主键 · 改了所有引用全断（hrcs_entityctrl/hrcs_dynascheme/hrcs_datarule 都通过 id 关联 dimension） | 红区（不改） |
| `datasource` | 🔴 模式开关 · 改了和标品 4 路联动（DimensionNewEdit.showEnumCtrl）冲突 | 红区（isvCanModify=false 标品锁死） |
| `showtype` | 🔴 显示类型 · 改了 UI 完全不一致 · 已被引用的角色清单可能失效 | 红区（isvCanModify=false 标品锁死） |
| `entitytype` | 🟠 业务对象 · 改了下游 hrcs_entityctrl 的 entityFieldMap 全乱 · 反查角色清单失效 | 黄区（受控 · 标品 ON 字段 · 但有规则约束） |
| `hrbu` | 🟠 职能类型 · datasource=hrbu 时改了 · 下游权限切分基础失效 | 黄区 |
| `org_classify` | 🟠 多选组织分类 · 改了 t_hrcs_dimorgclass 中间表数据 · datasource=orgteam 时影响 admin 组织切分 | 黄区 |
| `authtype` | 🟠 控权类型 · 改了下游 hrcs_dynaschemerole 的 customenable / custominfo 行为 | 黄区 |
| `enable` | 🟡 使用状态 · 改了下游引用是否纳入新分配（标品 disable 文案明确"已有不影响"） | 黄区 |
| `status` | 🟡 数据状态 · 跟工作流 op 关联（A/B/C） | 黄区 |
| `name` / `simplename` / `description` | 🟢 显示名 · 影响 UI / BI 报表 · 但标品同名校验缺（CS-03 补） | 绿区 |
| `index` | 🟢 排序号 · 影响列表排序 | 绿区 |
| `isadminorg` / `isorg` | 🟡 标志位 · 改了下游业务区分（如"是否行政组织维度"影响 hrcs_userrolerelat 查询） | 黄区 |
| `creator` / `modifier` / `createtime` / `modifytime` / `masterid` | 🔴 系统派生（CreaterField/ModifierField 等 · scene_doc.json `autoComputed=true`） | 红区（不改） |
| `issyspreset` | 🔴 系统预置标记 · 改了等于"伪装预置维度"·破坏一致性 · PR-007 锁死 | 红区（不改 · isvCanModify=false） |
| `disabler` / `disabledate` | 🔴 派生 · disable 自动写 | 红区（autoComputed） |
| `initdatasource` / `orinumber` / `oristatus` / `oriname` | 🔴 出厂值族 · 出厂数据还原用 · 不要手改 | 红区（autoComputed） |

### 1.2 分录字段

| 分录 → 字段 | 改了影响 |
|---|---|
| `entry` 整行删（datasource=enum 时） | 该枚举值丢失 · 已被引用的下游 hrcs_dynascheme.condition 内嵌该值会失效 |
| `entry.value` 改 | 🔴 标品 setEnable(false) 已锁定（已被引用时） · 改了引用方失败 · 后端虽未拦但 UI 拒绝 |
| `entry.displayvalue` 改 | 🟢 仅显示名 · 不影响下游 · 但 save 时会触发 checkEnumChange 二次确认 |
| `entry.entryindex` 改 | 🟢 顺序号 · 影响 UI 列表排序 |
| `entry.enumdescription` 改 | 🟢 备注 · 不影响 |
| `org_classify` 整行加/删 | 🟡 中间表 t_hrcs_dimorgclass 改 · 影响 datasource=orgteam 类型的组织切分 |
| `ctrlentry` 改（虚拟分录） | 🟢 不落库 · 改了无意义（仅 UI 反查显示） |

---

## 二、操作影响面（按 opKey 分组）

### 2.1 高影响 op（红区）

| opKey | 动作 | 影响范围 | 回滚方式 |
|---|---|---|---|
| `delete` | 删除维度 + 级联清子表 entry/dimorgclass | 永久丢维度数据 + 下游 hrcs_entityctrl/dynascheme.condition/datarule 等悬空引用（DimensionDeleteValidator 应阻断悬空 · 但检查不全则失败） | ❌ 不可回滚（彻底删） |
| 改 datasource | 模式切换（如 enum → basedata） | DimensionNewEdit.showEnumCtrl 走完整 4 路联动 · 必填字段全变 · entry 子表清空 | 🟡 走 unaudit 后改 · 但已发布的下游引用已经按老 datasource 解读 · 改完后下游配置全失效 |
| 改 entitytype（datasource=basedata 时） | 业务对象切换 | 反查 hrcs_entityctrl.entityFieldMap 全变 · UI 上 ctrlentry 显示完全不一样 · 已配权限映射全失效 | 🟡 unaudit 改 · 下游 entityctrl 需手工同步 |

### 2.2 中影响 op（黄区）

| opKey | 动作 | 影响范围 |
|---|---|---|
| `disable` | 禁用维度 | 标品文案"已有的角色维度的数据权限不受影响" · 但 ISV 自建严格模式下（CS-03 disable 方向）会阻断 |
| `enable` | 启用维度 | 立即纳入下次 dynascheme 重算 |
| `submit` | 提交（A → B） | 等待审核 · 不影响生产（status 字段隔离） |
| `audit` | 审核（B → C） | 立即生效 · 触发下游 dynascheme 重算（如 ISV 挂了 audit 后置 OP · CS-05/06） |
| `unaudit` | 反审核（C → A） | 暂停下游引用 · 但已绑定的 hrcs_userrolerelat 不会立即清（标品保守） |

### 2.3 低影响 op（绿区）

| opKey | 动作 | 影响范围 |
|---|---|---|
| `view` | 查看 | 0 数据写 |
| `refresh` | 刷新 | 0 数据写 |
| `copy` | 复制 | 新建一条 · 不影响原有数据 |
| `namehistory` / `namehistoryview` / `logview` / `viewonelog` | 查日志 | 0 数据写 |
| `mobtoolbarselect` / `mobtoolbarcancel` | 移动端选择/取消 | 0 数据写 |

---

## 三、对下游的影响（dimension 改动 → 下游连锁）

### 3.1 改 dimension.id（不该发生 · 但平台支持迁移）

→ **完全断链**：
- hrcs_entityctrl.entryentity.dimension 引用断
- hrcs_dynascheme.condition JSON 内嵌 dimensionId 断
- hrcs_datarule.dimension 引用断
- hrcs_dynaschemerole.dimension 引用断（HisModel · 历史版本全断）

### 3.2 改 dimension.datasource（罕见 · 标品锁死 isvCanModify=false）

→ 表面看 datasource 改不了 · 但**如果 ISV 通过元数据 modify 强行改了**：
- DimensionNewEdit.showEnumCtrl 走新 datasource 路径 · UI 全变
- entry 子表是否还要保留：basedata/hrbu/orgteam 路径下平台 deleteEntryData 清掉 · 老数据丢
- 下游 hrcs_dynascheme.condition 内嵌的 datasource-specific 参数引用全失效（如 enum 路径下引用 entry.value · 改成 basedata 后 value 没了）

### 3.3 改 dimension.entry.value（datasource=enum 时）

→ 标品 setEnable(false) UI 锁定 · 但 ISV 强改后端：
- hrcs_dynascheme.condition JSON 内嵌的 value 失效
- hrcs_userrolerelat 已分配权限的"按 value 切分" 失败（如分配了"P5 高级"的角色 · 改成"P5 senior"后绑定记录不变 · 但 BI 查询失效）

### 3.4 disable dimension

→ 标品文案"已有不影响"：
- 下次 dynascheme 重算不再用此维度
- 已绑定的 hrcs_userrolerelat 保留（不清）
- 已配的 hrcs_dynascheme.condition 仍然引用（但运行时根据 dimension.enable 跳过）
- 已配的 hrcs_entityctrl 仍然挂（但 dimension UI 不可见）

→ ISV 严格模式（CS-03 disable 方向）：阻断 disable · 直到所有下游解除引用。

### 3.5 delete dimension

→ DimensionDeleteValidator 内置反查（标品反编译没读到具体）+ ISV CS-04 反查 4 表：
- 任一引用 → 阻断 delete
- 全部无引用 → 删 · 物理表彻底清

### 3.6 audit dimension

→ status A → C：
- 标品 OP 链只 HRBaseDataLogOp 写日志
- ISV CS-05 挂 audit afterExecuteOperationTransaction → 发 BEC `${ISV_FLAG}_dimension_audited`
- ISV CS-06 挂同一时点 → 写自建审计日志 + 同步 BI 物化表

---

## 四、改动反向影响（下游变了 → 对 dimension 的影响）

### 4.1 hrcs_entityctrl.entryentity 增删

→ DimensionNewEdit.afterBindData 反查 entryentity · ctrlentry 自动重灌：
- 新增引用 → ctrlentry 显示新行
- 删除引用 → ctrlentry 显示行减少

### 4.2 hrcs_dynascheme.condition 改了 dimension 引用

→ 不影响 dimension 表自身 · 只影响 dynascheme 配置。

### 4.3 EntityCtrlServiceHelper.getRoles 反查发现新角色绑定

→ DimensionNewEdit.showEnumCtrl 进 enum 路径时检查角色 count > 0：
- count > 0 → entry value 字段 setEnable(false) UI 锁定
- count 增加 → 后续打开 dimension UI 仍然锁

→ ISV 不要绕过此检查（绕了 = 改了破坏标品的 enum 修改保护）。

---

## 五、典型生产事故案例（推断 + 行业经验）

### 案例 1 · 改 datasource 引发下游全失效

**场景**：客户运维通过 OData / IDEA 插件直接 `update dimension set datasource = 'basedata' where id = 100`（绕过标品 UI · 因为 isvCanModify=false 标品 UI 不让改）

**后果**：
- DimensionNewEdit.afterBindData 拿到 datasource=basedata 走新路径 · 但 entry 子表数据还在（平台 update 不级联清）
- showEnumCtrl 走 basedata 路径 · `deleteEntryData("entry")`（如果用户后续打开了 UI 编辑）
- 已配的 hrcs_dynascheme.condition 内嵌 enum entry.value 引用失效 · 运行时报错"找不到枚举值"

**预防**：通过元数据正规渠道（modifyMeta + 走 DimensionNewEdit.checkEnumChange 二次确认）改 · 不要直接 SQL update 锁死字段。

### 案例 2 · 改 entry.value 没二次确认导致下游失效

**场景**：客户运维直接 `update t_hrcs_dimensionenum set fvalue = 'P5_senior' where fdisplayvalue = 'P5 高级'`

**后果**：
- DimensionNewEdit.checkEnumChange 在 UI save 时会拦 · 但直接 SQL 没拦
- hrcs_dynascheme.condition 内嵌的 `{"value":"P5","dimension":100}` 找不到对应枚举值
- 下游权限切分失效 · 已分配的 hrcs_userrolerelat 仍存在（不清）但 BI 查不出
- 用户感知：BI 看板显示"P5 高级"角色 0 人 · 实际后台 hrcs_userrolerelat 还有 N 人 · 数据不一致

**预防**：用 dimension 表单 UI 改 · 走 checkEnumChange 二次确认 + 走 hadConfirm 缓存 · 让标品的"调整枚举值会影响角色维度·确定修改吗？"流程走完。

### 案例 3 · ISV 自建 OP 加 audit 后置同步 · 没用 PR-005 ID

**场景**：ISV 自建 `${ISV_FLAG}_dimension_audlog` 审计日志 · 但用 `UUID.randomUUID().toString()` 当 id

**后果**：
- 高并发同时审核 N 条 dimension（罕见但可能） · UUID 不冲（safe）
- 但跨苍穹分布式集群 · UUID 占用 36 字符 vs ID.genLongId() 19 位 · 索引性能差
- 生产事故：BI 系统下游解析时假设苍穹的 id 是 long · UUID 解析失败

**预防**：用 `kd.bos.id.ID.genLongId()`（PR-005）。CS-06 已强制。

### 案例 4 · ISV 没在【业务事件管理】预配置 eventNumber

**场景**：CS-05 跟着写 `svc.triggerEventSubscribeJobs("hrcs", "${ISV_FLAG}_dimension_audited", msg, vars)` · 但没在开发平台事件管理预配置

**后果**：
- BEC 静默忽略 · 不报错
- 下游 BI 看板没收到事件 · 维度数据不更新
- 排查难（无错误日志）

**预防**：先在【开发平台】→【业务事件管理】配置 eventNumber · 测试 BEC 平台日志确认收到 · 再上线（PR-011 prerequisite）。

### 案例 5 · 反查 dynascheme.condition 没带 iscurrentversion

**场景**：CS-04 删除前反查 dynascheme.condition LIKE "%dimensionId%"

**后果**：
- 历史版本（iscurrentversion=false 的 N 个旧版本）也被反查到
- 实际下游只引用当前版本 · 误报阻断
- 用户体验：明明 dynascheme 已经改了不再引用此 dimension · 但删除阻断 · 投诉

**预防**：反查 HisModel 类下游必带 `iscurrentversion=true` 过滤（PR-008）· CS-04 已强制。

### 案例 6 · 自建 ListPlugin 误继承 DimensionList

**场景**：ISV 想加列表权限过滤 · 直接 `extends DimensionList`

**后果**：
- 标品 DimensionList.beforeItemClick 处理 tbldisable · disable_conform callBackId
- ISV 类继承后没改这部分 · 但 confirmCallBack 路径里 super.confirmCallBack(messageBoxClosedEvent) 会调标品逻辑
- 标品升级把 callBackId 改成 "disable_conform_v2" · ISV 还在用 "disable_conform" · 二次确认弹了但点 Yes 不调 disable opKey
- 用户感知：列表禁用按钮"看似工作但不生效"

**预防**：PR-001 · 继承 HRDataBaseList · 不继承 DimensionList。CS-07 已强制。

---

## 六、变更级联快查表

| 改动 | 立即影响 | 异步影响 | 长期影响 |
|---|---|---|---|
| 改 dimension.id | hrcs_entityctrl/dynascheme/datarule 引用断 | hrcs_userrolerelat 已分配绑定查询失败 | 全链路数据不可用 |
| 改 dimension.datasource | UI 联动重置 | entry 数据可能丢失 | 下游 condition 引用全失效 |
| 改 dimension.entry.value | UI 不让（已锁） · SQL 改了引用断 | hrcs_dynascheme 运行时报错 | BI 查询失效 |
| 改 dimension.enable=0 | 不影响已分配（标品） | 下次 dynascheme 重算跳过 | 累计影响下次招聘新员工 |
| 改 dimension.name | 仅显示 | BI 报表标题变（同步） | - |
| 删 dimension | 阻断（DimensionDeleteValidator）/ 物理删 | 下游级联清（如 ISV 挂了 delete after） | 不可回滚 |
| 加 ISV 字段 | 表结构变 · 平台部署 | 后续 dimension 行新建会有此字段 · 老行 NULL | 长期影响多版本兼容 |

---

## 七、回滚策略速查

| 操作 | 标品支持回滚 | 推荐回滚方式 |
|---|---|---|
| save | ✅（事务回滚） | 直接 unsubmit / unaudit 后重填 |
| audit | ✅（unaudit） | 走 unaudit |
| delete | ❌（彻底删） | 不可回滚 · 必须从备份还原 |
| disable | ✅（enable） | 走 enable |
| 改 entry.value | ⚠ UI 锁了改不了 · ISV 严格模式应阻断 | 没方便回滚 · 改前必须确认 |

---

## 八、监控建议（生产级）

ISV 实施后建议监控以下指标：
1. **DimensionDeleteValidator 阻断率**（标品 + ISV CS-04 累计）→ 反映 dimension 跟下游耦合度
2. **DimensionNewEdit.checkEnumChange 二次确认弹出次数** → 反映 enum 改动频率
3. **hrcs_userrolerelat sourcetype=4（动态分配）的下游绑定数变化** → 反映 audit/disable 后实际影响面
4. **ISV BEC `${ISV_FLAG}_dimension_audited` 发布失败率**（CS-05）→ 反映平台 BEC 健康度
5. **`${ISV_FLAG}_dimension_audlog` 自建审计日志写入延迟**（CS-06）→ 反映 ISV OP 性能

→ 加监控可大幅降低 dimension 改动引发的下游事故率。
