# 能力边界 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟢 基于反编译 7 类 + opkeys_index.json 60 opKey + scene_doc.json 56 字段
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、场景定位

`hrcs_dynascheme`（动态授权方案）位于**HR 通用服务（hrcs）/ 权限管理 / 动态授权方案**菜单 · 是 HR 权限体系的"批量发放/取消角色"运营工具。区别于"静态权限分配"（管理员手动一对一绑定）：

- **静态分配**：`hrcs_userrolerelat`（手动赋权）· `hrcs_orgrole`（按组织继承赋权）等
- **动态分配**：`hrcs_dynascheme`（按规则批量赋权 · 跟随员工属性变动自动调整）· **本场景**

**主表单**：`hrcs_dynascheme`（formNumber 与 entityNumber 同名）
**ModelType**：BillFormModel（含审核流 + HisModel 时序）
**菜单 ID**：见 `probe_snapshot.json.menu`
**应用**：HR 通用服务（hrcs / 217...）

---

## 二、✅ verified（已覆盖能力）

### 2.1 标品支持的核心动作（60 opKey · 详见 opkeys_index.json）

| 类别 | opKey 清单 |
|---|---|
| **基础 CRUD** | `new` / `modify` / `view` / `save` / `delete` / `copy` / `refresh` |
| **工作流** | `submit` / `unsubmit` / `audit` / `unaudit` |
| **状态管理** | `enable` / `disable` |
| **业务变更（HisModel）** | `confirmchange` / `change` / `hisversioninfo` / `hiscopy` / `newhisversion` / `revise` / `reviserecord` / `versionchangecompare` / `showallversion` |
| **dynascheme 特有** | `setadminrange` / `assignrecord` / `audithisconfirmchange` / `checkroledetails` / `addrole` |
| **分录操作** | `newassignentry` / `deleteassignentry` / `newcancelentry` / `deletecancelentry` / `deleteroleentry` |
| **导入导出 HIES** | `importdata_hr` / `show_import_record_hr` / `export_from_list_hr` / `export_from_impttpl_hr` / `export_from_expttpl_hr` / `show_export_record_hr` |
| **导入导出 (legacy)** | `importdata` / `importdetails` / `importtemplatelist` / `exportlist` / `exportlistbyselectfields` / `exportlist_expt` / `exportdetails` / `importexport_userset` |
| **导航 + 日志** | `first` / `previous` / `next` / `last` / `option` / `submitandnew` / `saveandnew` / `close` / `returndata` / `logview` / `viewonelog` / `namehistory` / `namehistoryview` / `mobtoolbarselect` / `mobtoolbarcancel` |

### 2.2 标品自带规则

- **HR 域管理员准入闸**：HRAdminStrictPlugin 在 preOpenForm 拒绝非 HR 管理员（11 hrcs 表单共用）
- **当前用户管理员组自动带出**：新建方案时 admingroup 自动填第一个用户管理员组
- **F7 admingroup 双闸过滤**：只显示用户范围内 + HR 域的 admingroup
- **F7 变动类型去重**：assignactype / cancelactype 已用过的剔除
- **authaction 切换二次确认**：已配规则的方案切换 authaction 弹清规则确认
- **save/submit/confirmchange 规则校验**：DecisionSet JSON 格式 + authaction=3 必有规则
- **删除级联清理**：自动清理 5 张下游配置表（hrcs_dynaschemerange / dynaschorg / dynaschdimgrp / dynaschdatarule / dynaschfield）
- **HisModel 时序**：审核/变更自动生成新版本（boid 不变 · id 递进 · sourcevid 链式追溯）

### 2.3 标品自带集成点

- **下游灌库**：save 时自动写 6 张反查派生表（search_param / search_adminorg / search_pos / search_job / search_assignaction / search_cancelaction）
- **下游用户角色绑定**：审核通过后由 hrcs 权限重算任务展开方案 → 落 `hrcs_userrolerelat`（sourcetype=4）
- **changeTips 提示链**：confirmchange/audit 触发 `DynaAuthSchemeServiceHelper.showChangeTips` 弹"会影响 X 人"

---

## 三、🟡 likely（标品支持 · 待业务确认）

- 复制方案：`copy` opKey 走 `DynaAuthSchemeListPlugin.beforeDoOperation` (L93-L99) · 自动加"-复制"后缀 + 设 sourceSchemeId · 进入新建表单后从 sourceSchemeId 加载分录
- 历史变更确认：`audithisconfirmchange` opKey 用于"列表批量审核了一批历史变更" 的后置提示
- 角色范围属性子表单：`hrcs_dyscassignroledetail` 是 addrole 跳转的子表单 · 用户填角色成员范围属性后回填 customenable + custominfo
- 移动端分录工具栏：`mobtoolbarselect` / `mobtoolbarcancel` 暗示**支持移动端**（HisModelMobileListPlugin 也实证）

---

## 四、⚠️ unverified（需专家确认）

- [ ] 已审核方案变更后 · 旧版本数据是否保留？（HisModel 默认保留 · 但有无清理任务？）
- [ ] disable 一个方案后 · 已分配的角色绑定是否立即清理？（看 hrcs_userrolerelat 行为）
- [ ] 多个方案绑同一角色 · 同一员工命中多个方案时 · 优先级按什么排？（method/quality 待业务侧确认）
- [ ] 一个用户在多个 admingroup 时 · F7 显示哪个？（当前代码取 `userAdminGroups.iterator().next()` · 不可预测）
- [ ] 自定义事件 · 比如客户希望"员工司龄超过 N 年自动加 X 角色" 是否在标品规则引擎可配？还是必须做 ISV 扩展？

---

## 五、不覆盖（已知限制）

### 5.1 业务限制

- **不发 BEC 事件**：grep `triggerEventSubscribe / IEventService / EventServiceHelper` 在反编译 0 处命中（详见 06_customization_solutions.md CS-05）。如客户需要"方案变更通知下游系统" · 只能 ISV 自建 BEC 发布方
- **列表批量 confirmchange 不支持**：`DynaAuthSchemeConfirmChangeOp.endOperationTransaction` 抛 `KDBizException("not support")`
- **setadminrange / assignrecord 列表只允许选 1 条**：multi-select 会被 setCancel
- **role 字段不可改**：`scene_doc.json` `t_hrcs_dynaschemerole.role.isvCanModify = false`
- **customenable 字段不可改**：`scene_doc.json` `t_hrcs_dynaschemerole.customenable.isvCanModify = false`
- **issyspreset 标记的预置方案不能改 number**（PR-007）

### 5.2 平台限制

- **OpenAPI 不支持注册 ISV 扩展元数据 add field**（PLATFORM_OPENAPI_GAPS.md P0-1 · 平台需补齐）· ISV 加字段必须走开发平台 IDEA 插件或 Web UI
- **modifyMeta add field 不支持 ISV 归属判定**（PLATFORM_OPENAPI_GAPS.md P0-2）· 跨 ISV 归属的字段会被相互覆盖
- **HRMulPositionField / HRMulAdminOrgField / MulHisModelBasedataField 不能通过 OpenAPI buildMeta 创建**（kb_cosmic_buildmeta_traps.md）· OpenAPI 视作 BasedataField 兜底
- **EmployeeField 不支持**：标品 hrcs_dynascheme 不用 EmployeeField · 但 ISV 想加员工字段必须走 hrpi_person + Java 插件而非 EmployeeField

---

## 六、扩展能力总结

| 扩展场景 | 是否支持 | 详见 |
|---|---|---|
| 加自定义业务字段（如"方案标签"） | ✅ 完全支持 | CS-01 |
| 字段联动（admingroup 改了带出默认值） | ✅ 完全支持（FormPlugin propertyChanged） | CS-02 |
| save/submit 前置业务校验（如"方案规则不能空"） | ✅ 完全支持（自建 Validator + onAddValidators） | CS-03 |
| 删除前查下游引用 | ✅ 完全支持（自建 Validator + 反查 hrcs_userrolerelat / hrcs_dynaschemerange / hrcs_dynaschemerole） | CS-04 |
| 变更/审核后通知下游（BEC 发布方） | ⚠ 需 ISV 自建发布方（标品没发） | CS-05 |
| confirmchange 后定制下游同步 | ✅ 完全支持（自建 OP 挂 confirmchange afterExecuteOperationTransaction） | CS-06 |
| 列表过滤定制（按部门/标签过滤） | ✅ 完全支持（继承 HRDataBaseList · 不继承场景专属类） | CS-07 |
| 替换 PermFilter 控件（自定义规则编辑器） | ❌ 不推荐 · 平台标品控件 · 改了风险大 | - |
| 修改主表 boid/id/sourcevid 字段 | ❌ 不允许（HisModel 内部 · 改了直接挂） | - |

---

## 七、版本兼容性

- 反编译版本：HRMP **8.0**（hrmp-hrcs-formplugin-1.0.jar / hrmp-hrcs-opplugin-1.0.jar）
- 苍穹平台：8.0+（PermFilter / IClientViewProxy.invokeControlMethod 等控件 API 8.0 已稳定）
- 待验证：2024R1 / 2025 版本是否有 opKey/字段差异（建议每年 Q1 重跑 probe）
- HisModel 模板：`hbp_histimeseqtpl` · 与 hjm_jobhr / haos_adminorg 共用同一时序父模板

---

## 八、与其他场景的关联

| 关联场景 | 关系 | 备注 |
|---|---|---|
| `hrcs_userrolerelat` | 下游写表 | 方案分配/取消的最终落点（sourcetype=4） |
| `hrcs_role` / `perm_role` | 上游引用（roleentry.role / hrcsrole） | 中台角色 + 平台角色 |
| `perm_admingroup` | 上游引用（admingroup） | HR 域管理员组 |
| `hrcs_dynaauthobject` | 上游引用（assignpersonitem / cancelpersonitem） | 授权对象（人员）基础资料 |
| `hpfs_chgcategory` | 上游引用（assignactype / cancelactype） | 变动类型 |
| `hrcs_dyscassignroledetail` | 子页面 | addrole 跳转 · 填角色范围属性 |
| `hrcs_dynaschemerange / dynaschorg / dynaschdimgrp / dynaschdatarule / dynaschfield` | 下游配置 5 表 | delete 时级联清理 |

详见 `11_upstream_downstream_logic.md`。
