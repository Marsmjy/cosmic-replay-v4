# 参考案例 · 权限初始化

> **状态**: 🟢 已补全（2026-04-28 基于场景反编译实证 + 业务语义）
> **数据源**: `form_lifecycle_rules.json` + `scene_doc.json` + `curated_sdk.json`

## 🟢 业务场景总览

权限初始化是苍穹 HR 权限管理中"批量授权"的入口场景。它解决的核心痛点是：HR 管理员在新系统上线或组织调整时，需要一次性给大量用户/角色分配权限，而不是在权限中心逐个配置。本场景支持两种模式：

- **userrole 模式**：按用户维度批量初始化 — 指定一批用户，给他们分配角色 + 维度范围 + 数据规则 + 基础资料范围 + 字段权限
- **role 模式**：按角色维度批量初始化 — 指定一批角色，给每个角色配置功能权限 + 维度 + 数据范围 + 字段权限

两个模式共用主表单 `hrcs_perminitrecord`，通过 `inittype` 字段区分。

## 🟢 案例 1：新系统上线 — 批量导入 500 用户权限

**场景**：客户新购苍穹 HR 系统，需要给 500 个员工分配不同角色和维度权限。逐个在权限中心配置太慢。

**操作流程**：
1. 新建权限初始化任务，inittype=userrole（默认）
2. 下载用户权限模板（dlusertemp opKey → RecordExcelWriter 生成 Excel）
3. 在 Excel 中填写：用户、角色、维度值、基础资料范围、字段权限
4. 导入数据（importdata opKey）→ PermInitRecordEdit.beforeDoOperation 拦截：解析 Excel 回填到 userdimentry/userdataruleentry/userbdentry/userfieldentry
5. 确认数据无误后点击"完成初始化"（finishinit opKey）→ PermInitFinishValidateService 四维校验 → 通过后 UserPermInitConvertService.convertRecord 落库
6. dealstatus 变为 1（已完成）

**涉及 opKeys**：new → dlusertemp → userimport → finishinit

**ISV 定制点**：导入前加自定义校验（如检查 Excel 中的用户是否都在在职状态）

## 🟢 案例 2：业务部门调整 — 批量更新角色功能权限

**场景**：财务部门新增报销审批流程，需要用 role 模式批量给"财务专员"角色添加新功能权限项。

**操作流程**：
1. 新建权限初始化任务，inittype=role（列表过滤或手工切换）
2. 下载角色模板（dlroletemp opKey → RoleRecordExcelWriter 生成 Excel）
3. 在 Excel 中填写：角色、功能权限项、维度、数据范围、字段权限
4. 导入角色数据（roleimport opKey）→ 回填到 rolebaseentry/rolefuncentry/roledimentry/roledataentry/rolefieldentry
5. 确认无误后点"完成初始化"→ PermRoleInitFinishValidateService 五维校验（比 userrole 多一个功能权限维度）→ 通过后 PermRoleInitService.initRole 落库

**涉及 opKeys**：new → roleimport → finishinit

**ISV 定制点**：完成初始化后自动发审批单（如角色权限变更需要部门负责人审批）

## 🟢 案例 3：已有数据二次导入（覆盖前二次确认）

**场景**：用户已经导入了一批数据并保存（dealstatus=0），发现数据有误，想重新导入覆盖。此时标品会弹二次确认。

**操作流程**：
1. 在已有数据的任务上点"用户导入"（userimport opKey）
2. PermInitRecordEdit.beforeDoOperation 检测到 userdimentry 已有数据行 → 弹 showConfirm("导入将清空已有的数据 是否导入")
3. 用户确认 → reimport 回调清空 4 张 user 子分录（deleteOne 物理删除）
4. 重新解析 Excel → 回填新数据

**ISV 定制点**：在 reimport 前加 ISV 自定义确认逻辑（如也清空 ISV 扩展分录）

## 🟢 案例 4：导出权限初始化记录（按模板格式）

**场景**：HR 管理员需要把已有权限初始化记录导出为 Excel，供审计或迁移使用。

**操作流程**：
1. 在列表页选中初始化记录
2. 按导入模板导出（export_from_impttpl_hr）→ 下载 RoleRecordExcelWriter/RecordExcelWriter 生成的 Excel
3. 根据 inittype 决定导出 userrole 格式（多 sheet：用户维度/数据规则/基础资料/字段）还是 role 格式（多 sheet：角色/功能/维度/数据/字段）
4. 异步任务完成后，SessionManager 通知前端下载 URL

**涉及 opKeys**：export_from_impttpl_hr / export_from_list_hr / export_from_expttpl_hr

## 🟢 案例 5：未完成任务的自动清理

**场景**：用户新建了权限初始化任务，导入了部分数据但中途关闭了页面（dealstatus=0）。下次再打开同一任务名时，标品自动清理上次的脏数据。

**操作流程**：
1. 第一次：新建任务名"2024Q1权限初始化"→ 导入部分数据 → 关闭页面（dealstatus=0，脏数据残留在子表）
2. 第二次：新建同名任务 → checkTaskName 检测到同名 dealstatus=0 记录 → helper.deleteOne 物理清理 → 允许重新开始
3. 若名称为空 → FieldTip("任务名不能为空") 红色阻断

**ISV 注意事项**：checkTaskName 的 deleteOne 是物理删除且不可逆，ISV 加任务名规则时不要绕过标品清理逻辑。

## 🟢 自动化用例索引

| 用例 ID | 名称 | HAR 文件 | 覆盖路径 |
|---|---|---|---|
| HCS-PI-001 | 新建 userrole 任务并完成初始化 | — | new → save → userimport → finishinit |
| HCS-PI-002 | 新建 role 任务并完成初始化 | — | new → save → roleimport → finishinit |
| HCS-PI-003 | 重名任务清理 | — | new(name=A,save) → close → new(name=A) → checkTaskName 清理 |
| HCS-PI-004 | 导入清空二次确认 | — | new → userimport(save) → userimport(confirm) |
| HCS-PI-005 | 导出 userrole 格式 Excel | — | 选中记录 → export_from_impttpl_hr |
