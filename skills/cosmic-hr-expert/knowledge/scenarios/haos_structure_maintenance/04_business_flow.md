# 业务流转 · 矩阵组织维护（haos_structure）

> **状态**：🟢 基于 30 opKey + 7 反编译类实证
> **confidence**：verified

## 1. 业务全景

矩阵组织（haos_structure）的核心业务可以概括为：

> "在已有的某个**结构化方案（haos_structproject 母本）**之上 · 选定一个**根组织（rootorg）**和**组织团队分类（otclassify=1010L）** · 配置出一份**矩阵组织视图实例**（含是否含虚拟组织 / 是否自定义 / 是否应用全领域等开关）· 启用后下游能用此视图查行政组织树。"

### 1.1 与同域场景的位置关系

```
                    HR 基础组织（haos）应用
                        │
        ┌───────────────┼─────────────────────┐
        ▼               ▼                     ▼
  haos_adminorg    haos_structproject    haos_structure（本场景）
   行政组织主表     结构化方案母本         矩阵组织实例
   (HisModel)      (BaseFormModel)      (BaseFormModel)
   ↑                                          │
   │                                          ▼
   └─────────── rootorg 引用 ────────── 实例选定的根组织
                                              │
                                              ▼
                                   haos_othorgstruct（其它视图结果）
                                   haos_adminorgstruct（行政组织树视图）
```

## 2. 30 个 opKey 全景分类

> 数据来源：`_auto_operations.md`（30 opKey · 含 plugins / validations / 日志开关）

### 2.1 业务核心 opKey（10 个 · 全部走 OP 链 · 全部带日志）

| # | opKey | 类型 | OP 数 | Validator 数 | 业务含义 |
|---|---|---|---|---|---|
| 1 | `new` | new | 0 | 0 | 新增（仅打开表单 · 无业务逻辑）|
| 2 | `modify` | modify | 0 | 0 | 修改（仅打开表单）|
| 3 | `view` | view | 0 | 0 | 查看 |
| 4 | **`save`** | save | **6** | 4 | 保存（核心写）|
| 5 | **`delete`** | delete | 3 | 3 | 删除 |
| 6 | **`audit`** | audit | 1 | 3 | 审核（A→C 状态流转）|
| 7 | `unaudit` | unaudit | 1 | 3 | 反审核（C→A）|
| 8 | **`disable`** | disable | 1 | 2 | 禁用（enable 1→0）|
| 9 | **`enable`** | enable | 1 | 1 | 启用（enable 0→1）|
| 10 | **`submit`** | submit | 5 | 4 | 提交（A→B · 走 BdVersion 写历史）|

### 2.2 衍生 opKey（5 个）

| # | opKey | 类型 | 业务含义 |
|---|---|---|---|
| 11 | `unsubmit` | unsubmit | 撤销提交（B→A）|
| 12 | `submitandnew` | submitandnew | 提交并新增（连续录入）|
| 13 | `saveandnew` | saveandnew | 保存并新增 |
| 14 | `copy` | copy | 复制（main_form.xml `bar_copy` 下拉项 OperationKey=copy）|
| 15 | `returndata` | returndata | 返回数据（F7 选择回填）|

### 2.3 UI 辅助 opKey（10 个 · 无业务逻辑）

| # | opKey | 用途 |
|---|---|---|
| 16-17 | `close / refresh` | 关闭页面 / 刷新列表 |
| 18 | `option` | 选项设置 |
| 19-22 | `first / previous / next / last` | 分页导航（基础资料模板内置）|
| 23-24 | `logview / viewonelog` | 日志查看 |
| 25 | `vectorap` | 列表行右侧"维护架构"图标按钮（源码 `StructureListPlugin.java:198`）|

### 2.4 子分录 opKey（4 个 · 基础资料模板兜底 · 本表实际无 EntryEntity）

| # | opKey | 状态 |
|---|---|---|
| 26-29 | `newentry / deleteentry / previousentry / nextentry` | 都绑 `EntryId=4bpsQ3kQLk` · 这是平台模板默认 · 本场景无业务子分录所以不会被实际触发 |

### 2.5 业务自定义 opKey（1 个）

| # | opKey | operationType | 业务含义 |
|---|---|---|---|
| 30 | **`maintainframework`** | donothing | **维护架构** · 跳到 haos_orgstructlist 列表页（详见 §3.3）|

## 3. 业务流程详解

### 3.1 主流程 · 创建矩阵组织（最高频）

```
[1] 用户点新增（new）
       │
       ▼
[2] 表单打开 · 默认 org=当前用户首选 BU（来自 OrgPermHelper.getHRPermOrg() L102）
    · otclassify 默认应填 1010L（业务硬编码 · 区分母本/实例）
       │
       ▼
[3] 用户选根组织（rootorg → haos_adminorghrf7 F7）
       │
       │ 必填校验：MustInput Validator (id=6096194600001fac · save opKey)
       │
       ▼
[4] 用户选依赖架构方案（relyonstructproject → haos_structproject F7）
       │
       │ F7 数据来源：StructProjectRepository.getUserStructProject(showAllArea)
       │ · 跨域调 hrcs.IHRCSBizDataPermissionService.getUserStructProjectsF7
       │ · 强制把 1010L 提到首位
       │
       ▼
[5] 用户填其它字段（isincludevirtualorg / iscustomorg / istoallareas）
       │
       ▼
[6] 用户点保存（save opKey · 6 个 OP 顺序执行）
       │
       ├── CodeRuleOp · 生成 number（如果业务侧配了编码规则）
       ├── BdVersionSaveServicePlugin · 写主表 + 多语言子表 + 版本历史
       ├── HRBaseDataStatusOp · status='A'（草稿态）
       ├── HRBaseDataLogOp · 写操作日志
       ├── HRBaseDataEnableOp · enable='1'（默认启用）
       └── HRBaseOriginalOp · 记录原值
       │
       ▼
[7] 数据落到 t_haos_structproject（otclassify=1010L 行）
    + t_haos_structproject_l 多语言子表
       │
       ▼
[8] 列表刷新 · 看到新增的矩阵组织（status='A' · enable='1'）
```

### 3.2 状态机

```
                            new
                             │
                             ▼
                      ┌──────────────┐
                      │  '' / null   │ （表单初始状态）
                      └──────────────┘
                             │
                          save
                             │
                             ▼
                      ┌──────────────┐
                      │  status = A  │ ───────┐
                      │  (草稿)      │        │
                      └──────────────┘        │
                             │                │ disable (enable 1→0)
                          submit              │
                             │                ▼
                             ▼            ┌──────────────┐
                      ┌──────────────┐    │ enable = 0   │
                      │  status = B  │    │  (禁用 ·     │
                      │  (已提交)    │    │   不可删除)  │
                      └──────────────┘    └──────────────┘
                             │                │
                       audit / unsubmit       │ enable
                             │                ▼
                             ▼            ┌──────────────┐
                      ┌──────────────┐    │ enable = 1   │
                      │  status = C  │    │  (启用)      │
                      │  (已审核)    │    └──────────────┘
                      └──────────────┘
                             │
                          unaudit
                             │
                             ▼
                          status = A
                             │
                          delete (要求 enable='0' 才允许)
                             │
                             ▼
                         [已删除]
```

> 状态字段：
> - `status` BillStatusField · `parameter.StatusFieldId=AkId5S4yTs`（save/audit/unaudit/submit/unsubmit）
> - `enable` BillStatusField · `parameter.StatusFieldId=ac5Y5Dax1q`（disable/enable）

> 关键约束：
> - **删除前必须 disable** —— 来自 delete opKey 的 Validator `f789ca66000000ac` "数据已经禁用，不能删除"
> - **审核基础**是 status='A' 的草稿态 · 如果业务方先 submit 再 audit · 那是 A→B→C 两步走

### 3.3 维护架构子流程（business-specific · 矩阵组织独有）

源码 `StructureListPlugin.java:159-174` 的 `showStructListPage`：

```
用户在矩阵组织列表点 name 链接 OR 点行右侧"维护架构"图标（vectorap opKey）
   │
   ▼
billListHyperLinkClick / beforeDoOperation('vectorap')
   │
   ├── PermissionServiceHelper.checkPermission(... "haos_structure", "3F/95X2VSZ=1")
   │       ↓ 无权限
   │       └── 提示"无矩阵组织维护的维护架构权限 · 请联系管理员" 阻断
   │
   ├── HRBaseServiceHelper.queryOne(...).getDynamicObject("rootorg") == null
   │       ↓ 是
   │       └── 提示"当前矩阵架构无根组织 · 请前往'组织管理>行政组织维护>矩阵组织设置'维护其根组织信息后再进行架构维护" 阻断
   │
   ▼
showStructListPage(structureId)
   │
   ├── ListShowParameter
   │     setFormId("haos_orgstructlist")
   │     setBillFormId("haos_structorgdetail")
   │     setPageId("haos_orgstructlist_" + structureId + "_" + mainPageId)
   │     setCustomParam("custom_parent_f7_prop", "boid")
   │     setCustomParam("struct_project_ids", JsonString([structureId]))
   │     setCaption(StructProjectRepository.queryByPk("id,name", id).name)
   │
   ▼
跳到 haos_orgstructlist 列表页（按 struct_project_ids 过滤）
   · 用户在那里维护具体的"组织挂在该矩阵下哪个层级"
   · 实际数据落到 haos_orgstruct / haos_structorgdetail
```

> 这是矩阵组织维护的"实操入口"——主列表只是"管理矩阵骨架"· 真正的"在矩阵下挂组织"动作在 `haos_orgstructlist` 子列表页完成。

## 4. 关键操作的状态前置条件

| 操作 | 前置条件 | 校验来源 |
|---|---|---|
| save | 必填字段填齐（rootorg）+ 编码唯一 + 名称唯一 | save 4 Validators |
| submit | 必填 + 唯一性 | submit 4 Validators |
| audit | status='A' 或 'B' · 不能在已审核（C）上重复 audit | audit Validator `1cc0054f000018ac / f2843bab0000bfac` |
| unaudit | status='C' | unaudit Validator |
| disable | enable='1' 且 status='A/B/C' 都可 | disable 2 Validators |
| enable | enable='0' | enable 1 Validator |
| delete | enable='0' + 无下游引用 | delete 3 Validators（特别是 `hrbddeletevalidator`）|

## 5. 频次估算（典型客户）

| 操作 | 频次 | 说明 |
|---|---|---|
| save / submit / audit | 低（每个公司初次配置时密集 · 之后季度级别）| 矩阵组织是稳态结构 · 不像员工数据高频 |
| disable / enable | 低 | 业务调整时偶发 |
| view（列表查看）| 高 | HR / 业务高频读 |
| maintainframework | 中 | 矩阵下挂组织时频繁点 |
| delete | 极低 | 推荐用 disable 代替 |

## 6. 与上游 / 下游的协作关系

### 上游
- `haos_structproject`（结构化方案母本）—— `relyonstructproject` 字段引用 · 矩阵实例必须基于某个母本
- `haos_adminorghrf7`（行政组织 F7）—— `rootorg` 字段引用 · 决定矩阵架构的"挂载点"
- `bos_org`（基础资料 BU）—— `org` 字段引用 · 决定矩阵的"创建组织"
- `bos_user`（用户）—— `creator/modifier/disabler` 字段
- 跨域 `hrcs` —— 通过 `IHRCSBizDataPermissionService.getUserStructProjectsF7` 拿用户授权可见的方案集

### 下游
- `haos_othorgstruct`（其它组织视图 · 矩阵组织视图计算结果）—— ISV 不直接操作 · 是异步任务的产物
- `haos_adminorgstruct`（行政组织树）—— 在 `OrgPermHelper.resetPermOrgResultWithSubWithDate` 中按 `structProjectId` 反查（源码 `OrgPermHelper.java:58`）
- `haos_orgstructlist`（矩阵下挂组织的列表）—— `maintainframework` 跳到该 form
- `haos_structorgdetail`（矩阵组织详情）—— 列表的 billFormId

## 7. 异常分支

| 触发条件 | 异常码 | 标品行为 | 来源 |
|---|---|---|---|
| 列表点详情但无"维护架构"权限项 `3F/95X2VSZ=1` | `StructureListPlugin_0` | showErrorNotification + setCancel(true) | `StructureListPlugin.java:143, 205` |
| 列表点详情但 rootorg 为空 | `StructureListPlugin_1` | showErrorNotification + setCancel(true) | `StructureListPlugin.java:148, 209` |
| 删除时 enable='0'（已禁用）| `f789ca66000000ac` | 阻断 | delete Validator |
| 删除时下游有引用 | `2+RE4J37K857`（hrbddeletevalidator）| 阻断 · 列出引用方 | HRBP 通用 |
| 编码重复 | `2+R3Y9FBSJ51` | 阻断（GroupFieldUnique）| save Validator |
| 名称重复 | `2+R3ZR7WI4N2` | 阻断（GroupFieldUnique）| save Validator |
| 跨域调 hrcs 失败 | (兜底)| 容错三重判空：`permResult != null && !isHasAllStruct() && !authorizedStructs.isEmpty()` | `StructureListPlugin.java:124` |

## 8. 业务约束总结（给 ISV 扩展决策用）

1. 矩阵组织实例**必须**有 rootorg · 否则点详情时阻断（标品已实现）
2. 矩阵组织实例**必须**有 org（创建组织）· save 时必填校验
3. `otclassify` 默认应填 1010L · 业务方不应改这个值（改了就不再是"矩阵组织"了 · 会被 `StructProjectRepository.createUserStructProjectFilter` 过滤掉看不见）
4. 删除前**必须**先禁用（标品已实现 · ISV 不要绕过）
5. 编码 / 名称**全局唯一**（默认 GroupFieldUnique · 不分租户 · 改成租户内唯一参考 admin_org CS-05）
6. 矩阵组织维护属于"低频强一致"业务 · 不需要 BEC 异步通知（标品也没发事件 · 不要套 hjm/admin_org 的 BEC 模式）
7. 列表权限是**双 BU 闸 + 双方案权限闸**（创建组织有权限 + creatorhaspermission=true · 自己创建的 OR 跨域 hrcs 授权可见）· ISV 写 setFilter 扩展时要明确"叠加" 还是"替换"
