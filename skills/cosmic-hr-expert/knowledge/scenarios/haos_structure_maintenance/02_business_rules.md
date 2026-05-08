# 业务规则 · 矩阵组织维护（haos_structure）

> **状态**：🟢 基于 30 opKey + 7 反编译类 + main_form.xml 实证
> **confidence**：verified

## 1. 业务规则总分类

矩阵组织维护场景的业务规则可以分四层：

| 层 | 数量 | 实现位置 | 强度 |
|---|---|---|---|
| L1 字段必填规则 | 2 | scene_doc.json `required=true` | 平台保证 |
| L2 唯一性规则 | 4 | save/submit opKey GroupFieldUnique Validator | 平台保证 |
| L3 状态约束规则 | 7 | disable/enable/delete/audit/unaudit Validator | 平台保证 |
| L4 业务硬规则 | 5 | StructureListPlugin / StructProjectRepository 源码内 | 标品 Java 实现 |

## 2. L1 字段必填规则

### R-01 · 根组织必填（rootorg）

- **字段**：`rootorg` (HRAdminOrgField)
- **来源**：`scene_doc.json fields[20].required = true`
- **校验时机**：save / submit Validator MustInput (`6096194600001fac` / `RS=E9QE25UN`)
- **失败行为**：阻断保存 · "请输入根组织"
- **业务理由**：矩阵架构必须有起始挂载点（源码 `StructureListPlugin.java:148` 在列表点详情时也校验 · 双重保险）

### R-02 · 创建组织必填（org）

- **字段**：`org` (OrgField → bos_org)
- **来源**：`scene_doc.json fields[19].required = true`
- **校验时机**：save / submit Validator MustInput
- **默认值**：`OrgPermHelper.getHRPermOrg().getHasPermOrgs()` 首选 BU
- **失败行为**：阻断保存
- **业务理由**：基础资料标准 · 决定数据归属哪个 BU

## 3. L2 唯一性规则

### R-03 · 编码全局唯一

- **字段**：`number`
- **校验类型**：GroupFieldUnique
- **Validator id**：`2+R3Y9FBSJ51`（save opKey）+ `424b895300015bac`（submit opKey）+ `2f5773ca0001b5ac`（submitandnew）
- **失败行为**：阻断保存 · "编码已存在"
- **隔离粒度**：默认全局（不分租户 / 不分 BU）
- **改为租户内唯一**：参考 admin_org CS-05 模式 · 加自定义 Validator

### R-04 · 名称全局唯一

- **字段**：`name`
- **校验类型**：GroupFieldUnique
- **Validator id**：`2+R3ZR7WI4N2`（save）+ `2DDGGGIVWM1U`（submit）
- **失败行为**：阻断保存

> 注意：唯一性是基于物理表 `t_haos_structproject` · 而该表同时存 haos_structure 和 haos_structproject 两个 form 的数据 · 因此 `name` 唯一性是**跨双 form 的全局唯一**（如果建一个 haos_structproject 母本叫 "测试矩阵A" · 再建一个 haos_structure 实例叫 "测试矩阵A" 也会冲突）。

## 4. L3 状态约束规则

### R-05 · 删除前必须禁用

- **opKey**：delete
- **Validator id**：`f789ca66000000ac`
- **触发条件**：`enable != '0'`（即数据是启用态）
- **失败提示**：**"数据已经禁用，不能删除"**（这条提示有歧义 · 实际是"数据未禁用 · 不能删除"的反向表达 · 标品 ResId 设计如此）
- **修复路径**：先 disable · 再 delete

### R-06 · 删除前下游引用校验

- **opKey**：delete
- **Validator id**：`2+RE4J37K857`（`hrbddeletevalidator`）
- **触发条件**：被下游引用（如 haos_orgstructlist 还有数据挂在该矩阵下）
- **失败提示**：列出引用方 · 阻断
- **修复路径**：先清下游引用 · 再 delete

### R-07 · 审核前必须草稿态

- **opKey**：audit
- **Validator id**：`1cc0054f000018ac` + `f2843bab0000bfac`
- **触发条件**：`status != 'A'`（不是草稿态）
- **失败行为**：阻断
- **状态流转**：A → C（直接审核）或 A → B → C（先提交再审核）

### R-08 · 反审核前必须已审核态

- **opKey**：unaudit
- **Validator id**：`1cc0054f000019ac` + `f2843bab0000c0ac`
- **触发条件**：`status != 'C'`
- **失败行为**：阻断 · 反审核回到 A

### R-09 · 禁用前状态有效

- **opKey**：disable
- **Validator id**：`f2843bab0000baac` + `f2843bab0000bbac`
- **触发条件**：`enable != '1'`（已经是禁用态再禁用）
- **失败行为**：阻断

### R-10 · 启用前状态有效

- **opKey**：enable
- **Validator id**：`f2843bab0000bcac`
- **触发条件**：`enable != '0'`
- **失败行为**：阻断

### R-11 · 撤销提交前状态有效

- **opKey**：unsubmit
- **Validator id**：`3bdba4470000d3ac` + `f789ca66000001ac`
- **触发条件**：`status != 'B'`（不是已提交态）

## 5. L4 业务硬规则（Java 源码内）

### R-12 · "维护架构"权限项校验（业务自定义权限）

- **位置**：`StructureListPlugin.java:141, 203`
- **触发**：`billListHyperLinkClick`（点 name 链接）+ `beforeDoOperation('vectorap')`（点行右侧"维护架构"图标）
- **检查**：`PermissionServiceHelper.checkPermission(userId, appId, "haos_structure", "3F/95X2VSZ=1")`
- **失败 ResId**：`StructureListPlugin_0`（"无矩阵组织维护的维护架构权限，请联系管理员"）
- **业务意义**：单纯的 view 权限可以看列表 · 但要点进去维护下挂组织 · 需要专门的 `3F/95X2VSZ=1` 权限项

### R-13 · 矩阵组织必须有 rootorg 才能维护架构

- **位置**：`StructureListPlugin.java:147-150, 208-210`
- **触发**：列表点详情 / vectorap
- **检查**：`structure.getDynamicObject("rootorg") == null`
- **失败 ResId**：`StructureListPlugin_1`（"当前矩阵架构无根组织，请前往'组织管理>行政组织维护>矩阵组织设置'维护其根组织信息后再进行架构维护"）
- **业务意义**：根组织是架构挂载点 · 没根组织 · 维护无意义

### R-14 · 列表只显示创建人为当前用户的方案（双 BU 闸）

- **位置**：`StructureListPlugin.setFilter` 源码 `StructureListPlugin.java:104-131`
- **逻辑**：
  ```
  step 1: 拿用户 HR 组织权限 hasPermOrgs
  step 2: 在 hasPermOrgs 范围内 · 查每个组织的 creatorhaspermission 应用参数
  step 3: 只保留 creatorhaspermission=true 的组织 → paramEnableOrgSet
  step 4: 过滤"在 paramEnableOrgSet 中的 org + 创建人是 currUserId"的方案
  step 5: 跨域调 hrcs 拿"授权可见"的方案 ID 集合
  step 6: union 上面两个集合
  ```
- **业务意义**：HR 不能看到别人创建的方案 · 除非通过 hrcs 授权
- **应用参数**：`creatorhaspermission`（在 SystemParamHelper.java:26 定义）· 业务侧在【应用】→【参数】配置可改

### R-15 · 列表默认排除系统预置（保留 1010L 母本）

- **位置**：`StructureListPlugin.setFilter` 源码 L98-100
- **过滤**：`(issyspreset='0' OR id=STRUCT_PROJECT_MANAGE) AND enable in ['1','0']`
- **业务意义**：默认 hidden 系统预置方案 · 但保留 `STRUCT_PROJECT_MANAGE` 这条（业务方实际可见的"主"母本）

### R-16 · 矩阵组织实例的 otclassify 必须是 1010L

- **位置**：`StructProjectRepository.createUserStructProjectFilter` 源码 L92
- **过滤**：`new QFilter("otclassify", "=", 1010L)`
- **业务意义**：F7 选矩阵组织时 · 后端只返 otclassify=1010L 的数据 · 这是**矩阵组织的身份标记**
- **ISV 注意**：写自定义 Validator 时不要把 otclassify 改成其它值 · 改了就不再是矩阵组织 · 会被这个 QFilter 过滤掉看不见

## 6. 字段联动规则（main_form.xml + 源码反推）

> 标品代码层面**没有**字段联动逻辑（StructureEditPlugin 仅 13 行 · 全靠继承 HRDataBaseEdit 兜底；HRBaseDataTplEdit 是 HR 基础资料通用模板 · 也没针对本场景的字段联动）。

### 隐式联动（业务规则推断）

| 触发字段 | 影响字段 | 说明 |
|---|---|---|
| `rootorg` 选定 | `rootnumber / rootname / rooteffdt / rootdescription` 自动同步 | 标品反规范化字段 · 通常在 save 阶段冗余写入 · 列表显示用 |
| `roottype` 选定 | `rootorg` F7 过滤可选项 | 应该在 F7 弹出前过滤（具体逻辑标品没暴露 · 是否走 BeforeF7Select 待真发验证）|
| `relyonstructproject` 选定 | (业务期望)：`isincludevirtualorg / iscustomorg / istoallareas` 应当从母本带出默认值 | 标品**没观测到**这个联动 · 如果业务方要 · 走 ISV CS-03 |
| `iscustomorg=true` | `relyonstructproject` 应允许为空（自定义不依赖母本）| 业务期望 · 标品**未实现**这种约束 |

> ⚠ 这些联动**未在标品代码中实证** · 业务方真实需要时按 ISV CS-03（字段联动定制）落地 · 不要假设标品已实现。

## 7. 数据归属与可见性矩阵

| 角色 | view 列表 | view 详情 | save | maintainframework |
|---|---|---|---|---|
| 系统管理员（hasAllOrgPerm=true）| 全可见 | 全可见 | 可建在任意 BU | 需 `3F/95X2VSZ=1` 权限项 |
| HR · 自己创建的方案 | 可见（R-14 step 4）| 可 | 可（在 hasPermOrg + creatorhaspermission=true 的 BU）| 同上 |
| HR · hrcs 授权方案 | 可见（R-14 step 5）| 可 | 不可（仅可见）| 同上 |
| 普通员工 | 不可见（默认无 view 权限）| - | - | - |

## 8. 应用参数（System App Params · homs 应用）

> 数据源：`SystemParamHelper.java` 4 个常量

| 参数键 | 类型 | 默认值 | 影响 |
|---|---|---|---|
| `creatorhaspermission` | Boolean | true（`getCreatorHasPermission` L36 返回 true 当参数为 null）| **R-14 列表过滤**：决定该 BU 下"创建人对自己创建的方案是否有可见权"。false 时即使是创建者也看不到 |
| `staffpastmonthmodify` | Boolean | false | 是否允许修改"上月"的员工数据（与本场景无直接关系 · 但 SystemParamHelper 共享）|
| `allowonposdisable` | Boolean | false | 在岗状态下是否允许禁用（与本场景弱相关）|
| `choosechargeperson` | Boolean | false | 是否选择负责人（与本场景弱相关）|

> ISV 注意：`creatorhaspermission` 是 homs 应用参数 · 业务方在【应用管理】→【homs】→【参数】里配 · ISV 不要直接读它做反向逻辑（参数语义可能客户调）· 应通过 `SystemParamHelper.getCreatorHasPermission(buOrgId)` 调用。

## 9. 跨场景规则关联

| 上游规则源 | 影响本场景 |
|---|---|
| `haos_structproject` 母本启用/禁用 | 影响本场景的 `relyonstructproject` 字段 F7 可选项（StructProjectRepository 查时带 enable filter）|
| `haos_adminorg` 启用/禁用 | 影响 `rootorg` 字段 F7 可选项（HRAdminOrgField 默认带 enable=1 过滤）|
| `hrcs.IHRCSBizDataPermissionService` 授权变更 | 实时影响 R-14 列表可见性（无缓存 · 每次 setFilter 都跨域调）|
| 应用参数 `creatorhaspermission` 改 | 实时影响 R-14（cache 在 SystemParamServiceHelper 一级 · 改后需重新登录或手动刷缓存）|

## 10. 给 ISV 扩展决策的规则边界

### 可以加的业务规则
- 字段必填扩展（如 ISV 加自定义字段后 · 在 onAddValidators 加 MustInput）
- 唯一性扩展（如租户内唯一 · 见 admin_org CS-05 模式）
- 业务校验扩展（如"必须先有 X 才能 disable"）
- 字段联动（如 CS-03 选 relyonstructproject 自动带出 roottype）

### 不应该改的标品规则
- R-12（维护架构权限项）—— 标品权限模型 · 改了影响审计
- R-13（rootorg 必填校验在 list 点击时）—— 标品双重保险
- R-14（双 BU 闸列表过滤）—— 标品权限模型 · 改了违反数据隔离
- R-15（默认排除系统预置）—— 标品平台逻辑
- R-16（otclassify=1010L 是矩阵组织身份）—— 标品架构铁律

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructureEditPlugin -->

## chgaction 实证补充（StructureEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.structures.StructureEditPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructureEditPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructureEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin -->

## chgaction 实证补充（StructProjectBUListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructureListPlugin -->

## chgaction 实证补充（StructureListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.structures.StructureListPlugin`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructureListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructureListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->
