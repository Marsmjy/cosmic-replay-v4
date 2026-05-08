# 数据流转 · 矩阵组织维护（haos_structure）

> **状态**：🟢 基于 7 个反编译类（StructureListPlugin / AbstractBUListPlugin / StructProjectRepository / OrgPermHelper / SystemParamHelper / StructureEditPlugin / StructProjectBUListPlugin）实证抽取
> **confidence**：verified

## 1. 总览图

```
                         ┌────────────────────────────────────┐
                         │  请求层（前端 / OpenAPI）           │
                         └────────────────────────────────────┘
                                       │
              setFilter / save / delete / audit / disable / enable
                                       │
                                       ▼
┌────────────────────────────────────────────────────────────────┐
│ 表单插件层（FormPlugin · 4 个）                                │
│  · CodeRulePlugin（编码规则 UI 提示）                           │
│  · HRBaseDataTplEdit（HR 基础资料编辑模板 · afterBindData/...）│
│  · StructureEditPlugin（薄壳 13 行 · 继承 HRDataBaseEdit 兜底）│
│  · HRHiesButtonSwitchPlugin（HIES 按钮开关）                   │
└────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
┌────────────────────────────────────────────────────────────────┐
│ 列表插件层（ListPlugin · 5 个）                                │
│  · HRBaseDataTplList（基础资料列表模板）                        │
│  · HRBasedataLogList（操作日志列表）                            │
│  · BdVersionListPlugin（版本列表）                              │
│  · BaseDataNameVersionListPlugin（名称版本）                    │
│  · StructureListPlugin（核心 215 行 · setFilter+hyperLinkClick │
│    +beforeDoOperation · 含组织权限校验）                       │
│  · StructProjectBUListPlugin（左树插件 · 继承 AbstractBUListPlugin）│
└────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
┌────────────────────────────────────────────────────────────────┐
│ 操作插件层（OP · 6 个 · save/delete/audit/disable/enable...）  │
│  · CodeRuleOp（编码规则 OP）                                   │
│  · BdVersionSaveServicePlugin（基础资料版本管理 RowKey=1）      │
│  · HRBaseDataStatusOp（状态管理 RowKey=2）                     │
│  · HRBaseDataLogOp（日志 RowKey=3 · 几乎所有 opKey 都挂）       │
│  · HRBaseDataEnableOp（启用态管理 RowKey=4）                    │
│  · HRBaseOriginalOp（原始值记录 RowKey=5）                      │
└────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
┌────────────────────────────────────────────────────────────────┐
│ 业务仓储层（Repository · haos.business 域）                     │
│  · StructProjectRepository extends HAOSBaseRepository           │
│    （167 行 · 真正的业务深度 · CRUD 入口）                      │
│  · OrgPermHelper（66 行 · 组织权限计算）                        │
│  · SystemParamHelper（87 行 · creatorhaspermission/staffpastmonthmodify/...）│
└────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
┌────────────────────────────────────────────────────────────────┐
│ 数据访问层（HRBaseServiceHelper / serviceHelper）              │
│  · query / queryOne / queryOriginalArray / save                 │
│  · 跨域调用 HRMServiceHelper.invokeHRMPService(hrcs, ...)       │
└────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
┌────────────────────────────────────────────────────────────────┐
│ 物理表 · t_haos_structproject + t_haos_structproject_l         │
└────────────────────────────────────────────────────────────────┘
```

## 2. 读路径 · 列表加载（核心场景）

### 2.1 setFilter 链（源码 `StructureListPlugin.java:96-131`）

```
1. super.setFilter(event)                          // HRDataBaseList 父类预置过滤
   ↓
2. issyspreset='0' OR id=STRUCT_PROJECT_MANAGE     // L98-99 排除系统预置（保留 1010 母本）
   ↓
3. enable in ['1','0']                             // L101 启用 + 禁用 都可见
   ↓
4. OrgPermHelper.getHRPermOrg(false)               // L102 拿用户的组织权限
   ↓                                                 调 PermissionServiceHelper.getAllPermOrgs
                                                     · viewType=21（HR 组织视图）
                                                     · appId=homs · entityNumber=haos_adminorgdetail
                                                     · permId=47150e89000000ac（view 权限项）
   ↓
5. SystemParamHelper.getBatchOrgParameter(orgs, "creatorhaspermission")
                                                   // L107 批量查每个组织的"创建人是否有权限"参数
   ↓
6. 过滤 paramEnableOrgSet                          // L109-114 只保留 creatorhaspermission=true 的组织
   ↓
7. QFilter("org.id" in paramEnableOrgSet AND "creator.id" = currUserId)
   ↓                                                 // L116 创建人 + 创建组织参数双过滤
8. helper.queryOriginalArray("id", filter)         // L118 查到 creator 是当前用户的所有 structIds
   ↓
9. HRMServiceHelper.invokeHRMPService("hrcs", "IHRCSBizDataPermissionService",
       "getUserStructProjectsF7",
       new Object[]{currUserId, appId, "haos_structure", "47150e89000000ac", "rootorg", null})
   ↓                                                 // L122 跨域调 hrcs（HR 业务数据权限）
10. 合并 structIds                                 // L124-128 自己创建的 + 授权可见的
    ↓
11. event.getQFilters().add("id" in structIds)     // L129 final 过滤
    ↓
12. event.setOrderBy("enable desc, number asc")    // L130
```

> 关键洞察：列表查询有**两层组织权限闸**——一层是 `getHRPermOrg`（基础组织权限）· 另一层是 `getUserStructProjectsF7`（结构化方案权限）· 跨域调 hrcs 模块的 `IHRCSBizDataPermissionService`。ISV 扩展时如果绕过这两层做"我自己加 QFilter" · 必须知道这两层的语义并明确选择是"叠加" 还是"替换"。

### 2.2 列表点击 → 详情链（源码 `StructureListPlugin.java:133-157`）

```
billListHyperLinkClick(args)
   │
   ├── fieldName ∈ {name, cardlistcolumnap, cardlistcolumnap1}?
   │       ↓ 是
   ├── PermissionServiceHelper.checkPermission(userId, appId,
   │       "haos_structure", "3F/95X2VSZ=1")        // L141 检查"维护架构"权限项
   │       ↓ 无权限
   │       └── showErrorNotification("无矩阵组织维护的维护架构权限")  // L143
   │           setCancel(true) · 阻断
   │
   ├── HRBaseServiceHelper.queryOne("org, creator, id, enable, rootorg",
   │       new QFilter("id", "=", currentRow.primaryKeyValue))  // L147
   │
   ├── rootorg == null?
   │       ↓ 是
   │       └── showErrorNotification("当前矩阵架构无根组织 · 请先维护根组织")  // L148
   │           setCancel(true) · 阻断
   │
   └── showStructListPage(primaryKeyValue)              // L154
           │
           └── 跳到 haos_orgstructlist（formId）+ haos_structorgdetail（billFormId）
               · pageId = "haos_orgstructlist_" + pkId + "_" + mainPageId
               · customParam: custom_parent_f7_prop=boid · struct_project_ids=[pkId]
               · caption = StructProjectRepository.queryByPk("id,name", pkId).name
```

## 3. 仓储层调用 · StructProjectRepository（源码 167 行）

| 方法 | 入参 | 用途 | 关键过滤 |
|---|---|---|---|
| `queryAllStructProject(selectField)` | 字段名 | 查启用 + 禁用全量方案 | `enabledFilter().or(disabledFilter())` |
| `queryEnablingByIds(ids)` | id 集合 | 查正在启用中（enable=10）的方案 | `id in ids AND enable='10'` |
| `queryStructProjectByOtClassify(field, classifies)` | otclassify 集合 | 按团队分类筛 | 加 `otclassify in classifies` 条件 |
| `getUserStructProject(showAllArea)` | 是否含全领域 | 给 F7 用 · 拿当前用户可见方案 | 调 `createUserStructProjectFilter` + 跨域调 `getUserStructProjectsF7` · 把 1010L 强制提到首位 |
| `createUserStructProjectFilterNewChart(showAllArea)` | 是否含全领域 | 给"新版组织图表"F7 用 | 调 `getUserStructProjectsF7` 但绑 `homs_orgchart_new` 实体 |
| `queryOneByStructProjectId(props, id)` | id | 主键查单条 | `id = ?` |
| `queryByStructProjectIds(props, ids)` | id 集合 | 主键批量查 | `id in ids` |
| `queryAllStructArrBySyncorg()` | - | 查"启用 + 同步组织"标志的方案 | `enable='1' AND issyncorg='1'` order by `number asc` |
| `queryStructNullEffdt()` | - | 查 effdt 为空的脏数据（数据补丁用）| `effdt is null` |

> 设计模式：单例 · 通过 `StructProjectInstance.access$000()` 拿单例（源码 L43-45）
> 父类：`HAOSBaseRepository`（haos 业务域基础仓储 · 应该在 hrmp-haos-business jar 中 · 本次反编译未抓）

## 4. 写路径 · save / disable / enable

### 4.1 save 链（来自 `_auto_operations.md` save opKey 6 个 plugin）

```
                                                save (opKey)
                                                    │
        ┌───────────────────────────────────────────┼───────────────────────────────────────────┐
        │                                           │                                           │
        ▼                                           ▼                                           ▼
   onAddValidators                       beforeExecuteOperationTransaction       afterExecuteOperationTransaction
   (4 个 validation 注册)                 (5 个 OP 顺序执行)                      (HRBaseDataLogOp)
                                                    │
        ┌─────────────┬─────────────┬─────────────┬─────────────┬─────────────┐
        ▼             ▼             ▼             ▼             ▼             ▼
   CodeRuleOp     BdVersion-     HRBaseData-   HRBaseData-   HRBaseData-   HRBaseOri-
   (无 RowKey)    SaveService    StatusOp      LogOp         EnableOp      ginalOp
                  Plugin (1)     (2)           (3)           (4)           (5)

      生成 number   写主表 +     管理状态字段   日志        管理 enable    记录原值
                   多语言子表    A/B/C 流转                 字段
                   t_haos_       (StatusFieldId
                   structproject  =AkId5S4yTs)
                   _l 同步
```

> Validations（save opKey 4 条 · 来自 `_auto_operations.md`）:
> 1. `MustInput · 6096194600001fac` — 字段值合规性校验（必填）
> 2. `FormValidate · 1VRALXJOVNKD` — 合法性校验（disabled · 但配置存在）
> 3. `GroupFieldUnique · 2+R3Y9FBSJ51` — **编码（number）唯一性校验**
> 4. `GroupFieldUnique · 2+R3ZR7WI4N2` — **名称（name）唯一性校验**

### 4.2 delete 链（3 个 OP）

```
delete → CodeRuleDeleteOp（无 RowKey · 删编码占用）
       → HRBaseDataStatusOp（RowKey=1 · 状态校验）
       → HRBaseDataLogOp（RowKey=2 · 写删除日志）
```

> Validations（delete 3 条）:
> 1. `FormValidate · 1cc0054f000016ac` — disabled
> 2. `FormValidate · f789ca66000000ac` — **"数据已经禁用，不能删除"**（业务硬规则 · 必须先反禁用）
> 3. `hrbddeletevalidator · 2+RE4J37K857` — **HR 基础资料删除校验**（HBP 通用 · 检查下游引用）

### 4.3 disable / enable 链

两条链都只挂 `HRBaseDataLogOp` · 真正的状态切换由 `parameter.Value` 字段写入 enable 字段：
- disable: `parameter.Value="0", StatusFieldId=ac5Y5Dax1q`（写 enable=0）
- enable: `parameter.Value="1", StatusFieldId=ac5Y5Dax1q`（写 enable=1）

> 注意：源码 `StructProjectRepository.java:53` 的 `queryEnablingByIds` 用 `enable='10'`（正在启用中）· 这是 BdVersion 启用过程的中间态 · 完成后会变 `enable='1'`。

## 5. 跨域服务调用

| 调用 | 来源 | 用途 |
|---|---|---|
| `HRMServiceHelper.invokeHRMPService("hrcs", "IHRCSBizDataPermissionService", "getUserStructProjectsF7", ...)` | `StructureListPlugin.java:122` + `StructProjectRepository.java:88,105` | 跨域调 hrcs 拿用户授权可见的 structproject id 集合 |
| `PermissionServiceHelper.getAllPermOrgs(...)` | `OrgPermHelper.java:43-47` | 平台权限服务 · 拿组织权限结果 `HasPermOrgResult` |
| `PermissionServiceHelper.checkPermission(userId, appId, "haos_structure", "3F/95X2VSZ=1")` | `StructureListPlugin.java:141, 203` | 检查"维护架构"权限项 |
| `SystemParamServiceHelper.loadBatchAppParameterByOrgFromCache(...)` | `SystemParamHelper.java:45` | 批量查 homs 应用在多组织下的参数（creatorhaspermission 等）|
| `AppMetadataCache.getAppInfo("homs")` | `SystemParamHelper.java:32, 40, 68, 75` | 拿 homs 应用元信息（系统参数所属应用）|

> 关键 fact：`OrgPermHelper.HOMS_APP = "homs"` · 本场景虽然 form 在 haos 域 · 但**权限校验绑的是 homs 应用**（`OrgPermHelper.java:32`）· ISV 写权限相关扩展时要按 homs 应用拿权限项。

## 6. 事务边界

| 阶段 | 事务范围 |
|---|---|
| `onAddValidators` | 事务前 · Validator 注册阶段 · 不进事务 |
| `beforeExecuteOperationTransaction` | 事务前 · 抛异常阻断 |
| 实际写库（`SaveServiceHelper.save` / `BusinessDataServiceHelper.delete`） | 进入事务 · 多个表落库一致性 |
| `afterExecuteOperationTransaction` | 事务**已提交** · 此处发外部事件安全（PR-010）|
| `t_haos_structproject` 主表 + `t_haos_structproject_l` 多语言子表 | 同一事务 · 由 `BdVersionSaveServicePlugin` 协同写入 |

## 7. 失败回滚策略

- `beforeExecuteOperationTransaction` 抛 `KDBizException` → 整个 save 事务回滚
- 推荐用 `addErrorMessage(entity, msg)` 而非 `throw` · 可逐行定位错误（标品 `HRBaseDataStatusOp` 在 save 链就是这种模式）
- 跨域调用（`HRMServiceHelper.invokeHRMPService("hrcs", ...)`）的网络异常 · 标品代码用 try-catch 兜底（参考源码 `StructureListPlugin.java:122-128` 容错逻辑：`permResult != null && !isHasAllStruct() && !authorizedStructs.isEmpty()` 三重判空）

## 8. 缓存与状态

| 缓存键 | 类型 | 用途 |
|---|---|---|
| `org_perm_result` | PageCache（List 维度）| `AbstractBUListPlugin.java:50-54` 缓存当前用户的 `HasPermOrgResult` 序列化字符串 · 一次列表会话不重复查 |
| `first` | PageCache | `AbstractBUListPlugin.filterContainerInit` 标记是否首次进入 · 用来重置 org.name 默认值 |

## 9. 关键发现摘要（给 ISV 扩展决策用）

1. **本场景没观测到主动 BEC 事件发布** —— 不要套 hjm 的 BEC 三层异步模式（`StructProjectRepository.java` 全文无 `EventServiceHelper` 调用）
2. **真正的业务深度在 `kd.hr.haos.business` 域** · 不在 `formplugin` 域（StructureEditPlugin 仅 13 行 · 真正的 167 行 Repository 在 business 域）
3. **跨域权限校验绑的是 `homs` 应用** · 不是 `haos` 应用 · 这是历史决定（haos 数据沉淀到 homs 应用做权限管控 · 见 `OrgPermHelper.HOMS_APP = "homs"`）
4. **创建组织（`org` 字段）+ 创建人（`creator` 字段）双 BU 过滤** —— 列表只能看到"在我管辖 BU 下创建 + 我创建"的方案 · 这跟标准基础资料"全 BU 共享"模式不同
5. **`relyonstructproject` 字段是矩阵实例的数据依赖锚点** · ISV 加联动时（如 CS-03）要监听这个字段
