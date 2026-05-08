# 变更影响面 · 权限初始化

> **状态**: 🟢 已补全（2026-04-28 基于场景反编译实证）
> **数据源**: `form_lifecycle_rules.json` + `scene_doc.json` + `curated_sdk.json`

## 🟢 改动本场景会影响哪些下游

### 核心表依赖链（21 张物理表）

| 层级 | 表 | 用途 | 变更影响 |
|---|---|---|---|
| 主表 | `t_hrcs_pinitrecord` | 权限初始化任务主记录 | 加字段影响所有表单/导入导出/excel模板 |
| userrole 子表 | `t_hrcs_pinituserdim` | 用户维度值分录 | 字段变更影响 userimport 解析映射 + finishinit 校验 |
| userrole 子表 | `t_hrcs_pinituserdr` | 用户数据规则分录 | 同上 |
| userrole 子表 | `t_hrcs_pinituserbd` | 用户基础资料范围分录 | 同上 |
| userrole 子表 | `t_hrcs_pinituserfield` | 用户字段权限分录 | 同上 |
| userrole 子表 | `t_hrcs_pinituserdimval` | 用户维度值 | 同上 |
| userrole 子表 | `t_hrcs_pinituserorg` | 用户组织范围 | 同上 |
| role 子表 | `t_hrcs_pinitrolebase` | 角色基本信息分录 | 字段变更影响 roleimport 解析映射 + finishinit 校验 |
| role 子表 | `t_hrcs_pinitrolefunc` | 角色功能权限分录 | 同上 |
| role 子表 | `t_hrcs_pinitroledim` | 角色维度分录 | 同上 |
| role 子表 | `t_hrcs_pinitroledata` | 角色数据范围分录 | 同上 |
| role 子表 | `t_hrcs_pinitrolefield` | 角色字段权限分录 | 同上 |
| role 子表 | `t_hrcs_pinitrolefunccol` | 角色功能权限列错误 | 仅展示用，变更无级联 |
| role 子表 | `t_hrcs_pinitrolefuncrow` | 角色功能权限行错误 | 仅展示用，变更无级联 |
| role 子表 | `t_hrcs_pinitroledimrow` | 角色维度行错误 | 仅展示用，变更无级联 |
| role 子表 | `t_hrcs_pinitroledatarow` | 角色数据行错误 | 仅展示用，变更无级联 |
| role 子表 | `t_hrcs_pinitroledataval` | 角色数据范围值 | 同上 |
| role 子表 | `t_hrcs_pinitdrpermitem` | 数据规则权限项 | 同上 |
| role 子表 | `t_hrcs_pinitscopeview` | 范围查看 | 同上 |
| role 子表 | `t_hrcs_pinitscopeedit` | 范围编辑 | 同上 |
| role 子表 | `t_hrcs_pinitroleorg` | 角色组织范围 | 同上 |

### finishinit 落地目标表（写入下游真正的权限表）

finishinit 成功后，UserPermInitConvertService.convertRecord(recordId) 或 PermRoleInitService.initRole(recordId) 会将初始化记录转换成真正的权限数据落地到：

| 下游表（推测） | 所属模块 | 写入内容 |
|---|---|---|
| 用户角色关联表 | 权限中心 | userrole 模式的用户←→角色绑定 |
| 用户数据规则表 | 权限中心 | userrole 模式的数据范围规则 |
| 角色功能权限表 | 权限中心 | role 模式的功能权限分配 |
| 角色维度表 | 权限中心 | role 模式的维度授权 |
| 角色数据范围表 | 权限中心 | role 模式的数据范围授权 |
| 角色字段权限表 | 权限中心 | role 模式的字段级权限 |

### 字段变更影响矩阵

| 改什么 | 影响范围 | 级联动作 |
|---|---|---|
| 主表字段（如加 ext 字段） | 所有导出模板/导入模板需更新 | 重新下载 Excel 模板；已导入的历史数据不受影响 |
| inittype 枚举值（userrole/role） | PermInitRecordEdit.beforeBindData 路由 | 修改会破坏双模分支，FP_BBD1 路由失效 |
| dealstatus 字段语义 | 已完成单据判断（FP_BBD3 隐藏按钮 / formRule 360DI5ICICIJ） | 改动影响"已完成"逻辑，可能导致已完成的单据可被再次修改 |
| includesub 数据范围策略 | finishinit 校验 + 下游权限表 | 必填字段，改动会导致已有初始化任务的权限范围不一致 |
| userfieldentry/rolefieldentry 的 propKey 规则 | paintErrMarkCol / initRoleFdEntry 中文名回填 | 若 propKey 格式变了，EntityMetadataCache 查不到对应中文名，分录显示机器码 |
| 子分录物理表结构（加列/改类型） | userimport/roleimport Excel 解析 + finishinit 校验 | Excel 模板需重新生成；已存的导入数据可能校验不通过 |

## 🟢 改插件代码的影响

| 改什么 | 影响 |
|---|---|
| PermInitRecordEdit.checkTaskName 同名清理逻辑 | 所有 userimport/roleimport/save 操作都调用 checkTaskName；改动会全局影响任务名校验 |
| PermInitRecordEdit.beforeClosed 脏数据清理 | 仅影响手动关闭且 dealstatus=0 的单据；改动不当会留垃圾数据 |
| PermInitRecordDeleteOp delete 物理删除 | 删除不可逆；加删除前校验需在 onAddValidators 或 beforeExecuteOperationTransaction |
| HRAdminStrictPlugin 管理员门禁 | 仅 HR 管理员 + 苍穹管理员可用该场景；改动影响全场景准入 |

## BEC 事件发布

本场景标品 **没有发布 BEC 业务事件**（CS-05 实证：`curated_sdk.json` bec 分类注明"标品 0 处发布"）。ISV 如需要权限初始化完成后触发下游事件，需在 ext PermInitRecordDeleteOp.afterExecuteOperationTransaction 中自行调 `IEventService.triggerEventSubscribeJobs`。

## 生产注意事项

- **幂等性**：finishinit 成功后 dealstatus 变为 1，再次点 finishinit 按钮已被 FP_BBD3 隐藏，不会重复落库。但若 ISV 绕过 UI 直接调 OpenAPI batchInvokeAction 重复提交，会导致 UserPermInitConvertService 或 PermRoleInitService 被重复调用，产生重复权限数据。
- **事务边界**：save 和 finishinit 在不同的事务中。save 提交后 dealstatus=0（未完成），finishinit 提交后 dealstatus=1（已完成）。若 finishinit 中途失败，权限表可能半写（部分用户维度授权成功、部分失败），不会自动回滚 save 前的状态。
- **导出大文件**：PermInitRecordList 的导出走异步调度任务（sch_task），导出 URL 通过 taskInfo.getMap() 携带。大文件导出可能超时，ISV 加导出逻辑需考虑异步模式。
