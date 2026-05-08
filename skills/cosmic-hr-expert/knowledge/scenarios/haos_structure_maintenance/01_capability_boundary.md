# 能力边界 · 矩阵组织维护（haos_structure）

> **状态**：🟢 基于 30 opKey + 7 反编译类 + main_form.xml 实证
> **confidence**：verified

## 1. 场景定位

**菜单路径**：行政组织维护 / 行政组织维护 / 结构化组织维护
**form 中文**：矩阵组织维护（注：菜单中文叫"结构化组织维护" · form name 实际是"矩阵组织维护" · 写文档时两个名都要提）
**formNumber**：`haos_structure`
**菜单 menuId**：`1728553566500670464`
**入口应用**：HR基础组织（haos · `bizappId=W11R1282DJK`）

> 这是**矩阵组织实例的运营画面** · 不是结构化方案的设计画面（设计在 `haos_structproject`）。两个 form 共用 `t_haos_structproject` 物理表 · 用 `otclassify` 字段区分（haos_structure 实例 = 1010L · haos_structproject 母本 ≠ 1010）。

## 2. 能干啥（已覆盖能力）

### 2.1 矩阵组织 CRUD

| 能力 | opKey | 标品 OP 数 | Validator 数 |
|---|---|---|---|
| 新建矩阵组织（选根组织 + 选依赖方案 + 配置开关）| `new` + `save` | 6 | 4 |
| 修改已有矩阵组织 | `modify` + `save` | 6 | 4 |
| 复制现有矩阵组织 | `copy` + `save` | 6 | 4 |
| 删除矩阵组织 | `delete` | 3 | 3 |
| 查看矩阵组织详情 | `view` | 0 | 0 |

### 2.2 状态流转

| 能力 | opKey | 状态变化 |
|---|---|---|
| 提交矩阵组织 | `submit` | status A → B（已提交）|
| 审核矩阵组织 | `audit` | status A/B → C（已审核）|
| 反审核 | `unaudit` | status C → A |
| 撤销提交 | `unsubmit` | status B → A |
| 启用矩阵组织（用于查询行政组织树）| `enable` | enable 0 → 1 |
| 禁用矩阵组织（不参与查询）| `disable` | enable 1 → 0 |

> 状态字段：`status` BillStatusField（`StatusFieldId=AkId5S4yTs`）+ `enable` BillStatusField（`StatusFieldId=ac5Y5Dax1q`）

### 2.3 数据访问能力（基础资料标准）

| 能力 | opKey | 来源 |
|---|---|---|
| 列表查看（含权限过滤）| - | `StructureListPlugin.setFilter` 双 BU 闸 + 跨域 hrcs 授权 |
| F7 选择（其它表引用本表时）| - | 走 `BdVersionListPlugin` + `BaseDataNameVersionListPlugin` |
| 操作日志查看 | `logview / viewonelog` | `HRBaseDataLogOp` 写日志 + `HRBasedataLogList` 显示 |
| 列表导入 / 导出（HIES）| - | `HRBaseDataImportEdit` + `HRHiesButtonSwitchPlugin` 提供 HIES 按钮 |

### 2.4 维护下挂组织（核心业务能力）

| 能力 | opKey | 跳转路径 |
|---|---|---|
| 维护矩阵下的组织挂载 | `maintainframework` / 列表行 `vectorap` 图标 / 点 name 链接 | 跳到 `haos_orgstructlist` 列表（formId）+ `haos_structorgdetail` 详情（billFormId）· 详见源码 `StructureListPlugin.java:159-174` |

### 2.5 编码规则

| 能力 | 来源 |
|---|---|
| number 走业务侧编码规则自动生成 | save / submit 链 RowKey=- 是 `CodeRuleOp`（业务侧在【编码规则基础资料】配 · PR-006）|

## 3. 不能干啥（已知限制）

### 3.1 ⚠ 标品没实现的功能

| 功能 | 为什么不能 | 解决路径 |
|---|---|---|
| 在矩阵组织实例上直接改方案结构 | 那是 `haos_structproject` 母本的事（修改母本会影响所有依赖它的实例）| 改去 `haos_structproject` 操作 · 不在本场景 |
| 跨方案对比 | 标品列表只支持单方案查看 | 自定义报表 / 业务方按需开发 |
| 矩阵组织变更后自动通知下游 | 标品**未实现** BEC 事件发布（`StructProjectRepository.java` 没调 `EventServiceHelper.triggerEventSubscribeJobs`）| 不存在 · 不要套 hjm BEC 模式（除非真要 · 可参考 admin_org CS-04 但要确认本场景是否真发了事件）|
| 字段联动（如选 relyonstructproject 自动带出 roottype）| 标品 `StructureEditPlugin` 仅 13 行 · 没字段联动 | 走 ISV CS-03 加自定义 propertyChanged 联动 |
| 审批工作流 | 本 form 不挂工作流（基础资料 BaseFormModel 不是 BillFormModel · 不天然走审批）| 标品定位"基础资料" · 走 audit/submit 状态切换 · 不走审批流 · 业务方真要可走 OPM 审批中心独立配 |
| 历史版本回溯 | BdVersion 只管"当前版本"和"历史版本"两态 · 没"按时间点回溯任意一天"能力 | 看 BdVersionListPlugin 提供有限的版本切换 · 全量回溯需 BI 报表 |

### 3.2 平台限制

| 限制 | 说明 |
|---|---|
| `haos_structure` 不能再增删主表字段（标品锁定）| `IsvSign` 已签 · ISV 加字段必须走【ISV 扩展元数据】机制（不能直接 modifyMeta 改 haos_structure 主表）· 详见 isv_ownership_redline.md |
| 物理表 `t_haos_structproject` 不能直接 SQL 改 | 必须走苍穹 `HRBaseServiceHelper.save` · 否则 BdVersion 历史不一致 |
| `otclassify` 不应改成非 1010L | 改了就被 `StructProjectRepository.createUserStructProjectFilter` 过滤掉看不见 |

### 3.3 与同应用其他场景的边界

| 不在本场景内的功能 | 应该去 |
|---|---|
| 编辑结构化方案母本（增删字段、调结构）| `haos_structproject` 场景 |
| 行政组织树本身的维护（建组织 / 改上级 / 停用）| `admin_org_quick_maintenance` 场景 / 调整申请单 `homs_orgbatchchgbill` |
| 在矩阵下具体挂哪些组织 | `haos_orgstructlist` + `haos_structorgdetail`（通过 maintainframework 跳进去）|
| 行政组织视图计算 | 标品 OPM 异步任务（结果落到 `haos_othorgstruct / haos_adminorgstruct` · ISV 不直接操作）|
| 矩阵组织的查询 API（外部系统集成）| 走 OpenAPI · `haos_structure` 实体的 `query` 接口 |

## 4. 边界条件与前置依赖

### 4.1 创建矩阵组织前 · 必须存在

1. 至少一个 `haos_structproject` 母本（"依赖架构方案"字段）
2. 至少一个 `haos_adminorg` 行政组织作为根组织（"根组织"字段必填 R-01）
3. 当前用户在某个 BU 下有 `creatorhaspermission=true` 应用参数（否则虽然能保存 · 但保存后自己也看不到列表 · R-14）
4. 当前用户在 hrcs 模块有可见某些方案的授权（否则只能看自己创建的）

### 4.2 维护架构（点详情）前 · 必须满足

1. 矩阵组织必须有 rootorg（R-13 · 否则阻断）
2. 当前用户必须有 `3F/95X2VSZ=1` 权限项（"维护架构"权限 · R-12 · 不是普通 view 权限）

### 4.3 删除矩阵组织前 · 必须满足

1. enable='0'（已禁用 · R-05）
2. 没有下游引用（R-06 hrbddeletevalidator）

## 5. 性能边界

| 项 | 限制 |
|---|---|
| 单次列表查询 | 跨域调 hrcs `getUserStructProjectsF7` · 性能取决于授权方案数 · 无明显瓶颈（业务方案级别通常 < 100）|
| 单次保存 | 6 OP 顺序执行 · 含 BdVersion 写历史 · 单条 < 100ms |
| F7 查询 | `StructProjectRepository.getUserStructProject` 查全量启用 + 禁用方案 · 数据量小不必担心 |
| 列表导入（HIES）| HRHiesButtonSwitchPlugin 控制 · 受标品 HIES 限制（默认单批 1000 条）|

## 6. 数据规模边界

| 项 | 典型规模 |
|---|---|
| 单租户矩阵组织实例数 | 10-50（业务方案稳态结构）|
| 物理表 `t_haos_structproject` 总行数（含母本+实例）| < 200 |
| 多语言子表行数 | 实例数 × 业务用语言数 |

## 7. 与版本演进相关

| 版本 | 行为 |
|---|---|
| 8.x（当前抓取版本）| 双 form 共用一表（otclassify 区分）· 跨域调 hrcs |
| 历史早期 | 可能 haos_structure 是独立表 · 后期重构合并到 t_haos_structproject（具体演进史不在抓取范围 · 业务方真要查 follow up 苍穹官方升级日志）|

> ISV 扩展时不要假设字段稳定性 · 标品升级可能调整 otclassify 业务编码（如改成 string 枚举）· 写代码时用常量类引用而不是硬编码 1010L。

## 8. 同族场景能力对照

| 场景 | 能干啥 | 不能干啥 |
|---|---|---|
| **本场景**（haos_structure）| 矩阵组织实例 CRUD + 维护下挂 | 不能改母本结构 / 不能改行政组织本身 |
| haos_structproject | 结构化方案母本 CRUD + 设计字段 | 不能直接挂组织（要建实例后操作）|
| admin_org_quick_maintenance | 行政组织 CRUD + 上级变更 + 历史版本回溯（HisModel）| 不能改矩阵架构 |
| haos_orgstructlist | 在矩阵下挂组织 / 调整层级 | 不能建矩阵组织 / 改方案 |

## 9. 给 ISV 扩展的开发坐标

| 能扩展什么 | 在哪扩展 |
|---|---|
| 加自定义字段 | ISV 扩展元数据（不能直接 modifyMeta 改 haos_structure 主表 · 要建 ISV 扩展元数据继承 haos_structure）|
| 加业务校验 | save / submit / disable / delete 的 onAddValidators 阶段 · 并列挂新 Validator |
| 加字段联动 | 新 FormPlugin extends HRDataBaseEdit · 重写 propertyChanged · 注意 PR-004 死循环防护 |
| 改列表权限过滤 | 新 ListPlugin extends HRDataBaseList · 重写 setFilter · 与标品 StructureListPlugin 并列跑（顺序累加 QFilter）|
| 改列表显示 | 新 ListPlugin · 重写 beforePackageData |
| 加自定义按钮跳转 | 配置 BarItemAp 新 OperationKey + 新 OP / FormPlugin 实现 |
| 共用物理表的 ISV 字段 | 必须确认是否要影响 haos_structproject · 推荐用 issyspreset/otclassify 区分（参考 hbpm 的 isstandardpos 模式）|

## 10. 高频 CS（能力 → CS 速查）

| 业务需求 | 推荐 CS | 复杂度 |
|---|---|---|
| 加自定义字段（如"主管行政区域"）| CS-01 | 低 |
| 加保存校验（"根组织必须是公司类型"）| CS-02 | 低 |
| 字段联动（"选方案自动带出根组织类型"）| CS-03 | 中 |
| 启用前置校验（"必须有依赖方案才能启用"）| CS-04 | 中 |
| 列表权限定制 | CS-07 | 中 |
| 共用物理表场景的扩展隔离 | CS-06 | 高（需理解 otclassify 区分键）|
