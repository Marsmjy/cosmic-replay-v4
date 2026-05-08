# 业务流转 · 权限初始化 (hrcs_perminitrecord)

> **状态**: 🟢 基于 `_auto_plugin_semantics.md` (4 反编译类) + `rules_chain_all.json` (66 opKey) + `form_lifecycle_rules.json`
> **confidence**: verified
> **数据源**: CFR 反编译 PermInitRecordEdit/PermInitRecordList/PermInitRecordDeleteOp/HRAdminStrictPlugin (2026-04-28)

---

## ⚡ Developer Quick Reference

| 问 | 答 |
|---|---|
| 有审批工作流？ | ❌ 无（save/finishinit 一步到位） |
| 草稿态如何定义？ | dealstatus=0 · 列表不可见 · 关闭页自动删 |
| finishinit 两种模式？ | userrole: 四维校验 → UserPermInitConvertService.convertRecord; role: 五维校验 → PermRoleInitService.initRole |
| reimport 清多少张表？ | userrole: 4 张子分录; role: 5 张子分录 |
| 列表智能激活？ | 已开 ADDNEW 同类型 tab → 激活而不新建 |

---

## 一、权限初始化不是"工作流单据" · 是"批量导入工具（带落库）"

`hrcs_perminitrecord` 没有真实的审批工作流（opkeys_index.json 66 opKey 里有 submit/audit/unaudit 但 thin）。它是一个**批量导入 + 校验 + 落库**一体化工具：

- ✅ 新建草稿 → userimport/roleimport 导入数据 → finishinit 校验+落库 → dealstatus=1 已完成
- ✅ 已完成态 → 只读（FP_BBD3 隐藏 finishiinit 按钮）
- ✅ 草稿态 → 关闭自动物理删（FP_BC1 beforeClosed）
- ✅ 持久化态 → delete 物理删（PermInitDeleteValidator 前置校验）

**对比维度**：

| 场景 | 是否有审批工作流 | 是否有 HisModel | 是否有 BEC | 双模路由 |
|---|---|---|---|---|
| `hrcs_perminitrecord`（本场景） | ❌ 否 | ❌ 否 | ❌ 否 | ✅ userrole/role |
| `hrcs_permrelat` | ❌ 否 | ❌ 否 | ❌ 否 | ❌ 否 |
| `hrcs_dynascheme` | ✅ submit/audit | ✅ 是 | ❌ 否 | ✅ changescene |

⚠ **CS 设计警示**：不要套用 hjm_jobhr 3 层异步发 BEC 的模式给本场景 · 本场景不发 BEC（PR-011 ISV 自建路径 CS-05）。

---

## 二、入口流程图（菜单 → 列表 → 详情）

```
HR基础服务 / HR权限管理 / 权限初始化
                ↓
  preOpenForm
  └── HRAdminStrictPlugin（HR 域准入闸 · F7 lookUp 直接放行）
       ├── 第一闸: isLookUp() → 直接放行
       ├── 第二闸: !isAdminUser && !isCosmicUser → setCancel
       └── 第三闸: !HRAdminService.isHrAdmin() → setCancel
                ↓ 通过三闸
  ListView (hrcs_perminitrecord 列表 · BillFormModel)
  └── setFilter: dealstatus = 1（只显示已完成）
  └── beforeShowBill: 回填 inittype 到 customParam
                ↓ 用户操作
     ┌────────────┬────────────┬──────────────┬──────────────────┐
  [inituserrole] [initrole] [修改/查看] [download] [模板下载]
       ↓            ↓           ↓             ↓             ↓
  新建用户权限初始化  新建角色初始化   打开已完成记录   导出Excel    异步任务
```

---

## 三、核心流程一：userrole 模式（用户权限初始化）

### 3.1 完整时序

```
列表点击【初始化用户权限】(inituserrole)
  ↓
PermInitRecordList.beforeDoOperation(inituserrole)  [FP_LBDO1]
  ├── SessionManager key = pageId + "showFormuserrole" 检查已有 ADDNEW tab
  ├── 有 → service.addAction(activate, pageId) 激活
  └── 无 → showUserInitDetail()
        └── BillShowParameter
              ├── setFormId("hrcs_perminitrecord")
              ├── setStatus(ADDNEW)
              └── setCustomParam(inittype, "userrole")
  ↓
PermInitRecordEdit.beforeBindData  [FP_BBD1 + FP_BBD2]
  ├── recordId empty（新建）→ setValue(inittype, USERROLE)
  └── showDynaDimForm(recordId) → 嵌入 hrcs_dynadim 子表单
  ↓
用户填写任务名 + 点击【用户导入】(userimport)
  ↓
PermInitRecordEdit.beforeDoOperation(userimport)  [FP_BDO1 + FP_BDO2]
  ├── checkTaskName(name, dataEntity, orm, helper)
  │     ├── name 空 → FieldTip 红色阻断
  │     ├── 同名 dealstatus=1 → "任务名重复" 阻断
  │     └── 同名 dealstatus=0 → helper.deleteOne 自动清理
  ├── 已有 userdimentry 行？
  │     └── showConfirm("导入将清空已有的数据 是否导入") → args.setCancel(true)
  └── 无数据 → 跳转 hrcs_permimportstart 子页面
  ↓
用户在 hrcs_permimportstart 完成导入
  ↓
PermInitRecordEdit.closedCallBack(actionId = "importFinish")  [FP_CB1]
  ├── updateView(userdimentry)
  ├── updateView(userdataruleentry)
  ├── invokeOperation(refresh)
  ├── setFlexVisible(initType) → 显示分录 tab
  └── showDynaDimForm(record.id)
  ↓
用户点击【完成初始化】(finishinit)
  ↓
PermInitRecordEdit.beforeDoOperation(finishinit)  [FP_BDO6 + FP_BDO7 + FP_BDO8 + FP_BDO4]
  ├── FP_BDO6: 无数据 → 拒绝
  ├── FP_BDO8: name 空 → 拒绝 + FieldTip
  ├── FP_BDO7: dataChanged → invokeOperation(save) 隐式保存
  └── FP_BDO4: PermInitFinishValidateService.clickFinishValidate(recordId)
        ├── errArr = [userRoleErr, dateRuleErr, bdDataRuleErr, fieldErr]
        ├── sum(errArr) > 0 → showForm("hrcs_perminitcheckresult") 弹检查结果
        └── sum(errArr) = 0 → UserPermInitConvertService.convertRecord(recordId)
              → setValue(dealstatus, 1) → invokeOperation(save)
```

---

## 四、核心流程二：role 模式（角色权限初始化）

### 4.1 完整时序

```
列表点击【初始化角色权限】(initrole)
  ↓
PermInitRecordList.beforeDoOperation(initrole)  [FP_LBDO2]
  ├── SessionManager key = pageId + "showFormrole" 检查已有 ADDNEW tab
  ├── 有 → service.addAction(activate, pageId) 激活
  └── 无 → showRoleInitDetail()
        └── BillShowParameter
              ├── setFormId("hrcs_perminitrecord")
              ├── setStatus(ADDNEW)
              └── setCustomParam(inittype, "role")
  ↓
PermInitRecordEdit.beforeBindData  [FP_BBD1 + FP_BBD2]
  ├── recordId empty → setValue(inittype, ROLE)
  ├── showRoleFuncForm(recordId) → 嵌入 hrcs_permroleinitfunc (tabpageap5)
  ├── showRoleDimForm(recordId) → 嵌入 hrcs_permroleinitdim (tabpageap6)
  ├── showRoleDrForm(recordId) → 嵌入 hrcs_permroleinitdr (tabpageap7)
  └── initRoleFdEntry(recordId) → 回填 rolefieldentry 中文名
  ↓
用户填写任务名 + 点击【角色导入】(roleimport)
  ↓
PermInitRecordEdit.beforeDoOperation(roleimport)  [FP_BDO1 + FP_BDO2]
  ├── checkTaskName（同 userrole 流程）
  ├── 已有 rolebaseentry 行？
  │     └── confirmCallBack(reimport, Yes)
  │           └── FP_CCB1: 清空 5 张子分录
  └── 跳转 hrcs_rolepermimportstart 子页面
  ↓
用户在 hrcs_rolepermimportstart 完成导入
  ↓
PermInitRecordEdit.closedCallBack(importFinish + success)  [FP_CB2 + FP_CB3]
  ├── invokeOperation(refresh) → 刷新分录
  ├── showRoleFuncForm → 重新嵌入 hrcs_permroleinitfunc
  ├── showRoleDimForm → 重新嵌入 hrcs_permroleinitdim
  └── showRoleDrForm → 重新嵌入 hrcs_permroleinitdr
  ↓
用户点击【完成初始化】(finishinit)
  ↓
PermInitRecordEdit.beforeDoOperation(finishinit)  [FP_BDO3]
  ├── PermRoleInitFinishValidateService.clickFinishValidate(recordId)
  │     ├── errArr = [roleErr, funcItemErr, dimErr, dataRangeErr, fieldErr]
  │     ├── sum(errArr) > 0 → showForm("hrcs_roleinitcheckresult")
  │     └── sum(errArr) = 0 → PermRoleInitService.getInstance().initRole(recordId)
  │           → setValue(dealstatus, 1) → invokeOperation(save)
  ↓ 角色权限落地到下游表族
```

---

## 五、列表删除流

```
用户列表选行 → 点 delete
  ↓
PermInitRecordDeleteOp.onPreparePropertys  [OP_DEL_PP1]
  └── args.getFieldKeys().add(dealstatus)
  ↓
PermInitRecordDeleteOp.onAddValidators  [OP_DEL_AV1]
  └── args.addValidator(new PermInitDeleteValidator())
  ↓
OP executionChain（5 个 OP 按序执行）
  ├── HRBaseDataStatusOp.beforeExecuteOperationTransaction
  ├── HRBaseDataLogOp.beforeExecuteOperationTransaction
  ├── HRBaseDataEnableOp.beforeExecuteOperationTransaction
  ├── HRBaseOriginalOp.beforeExecuteOperationTransaction
  └── PermInitRecordDeleteOp.beforeExecuteOperationTransaction
  ↓
（事务内）DELETE t_hrcs_pinitrecord + 21 张子表级联
  ↓
OP executionChain continue
  ├── HRBaseDataStatusOp.afterExecuteOperationTransaction
  ├── HRBaseDataLogOp.afterExecuteOperationTransaction
  └── PermInitRecordDeleteOp.afterExecuteOperationTransaction
```

---

## 六、草稿自动清理流（隐式删除 · 不走 OP）

```
用户关闭详情页（未点 finishinit / 未点 save 保存草稿）
  ↓
PermInitRecordEdit.beforeClosed  [FP_BC1]
  ├── dealstatus = 0 && id != 0
  │     → new HRBaseServiceHelper(hrcs_perminitrecord).deleteOne(id)
  │     → 物理删除主单 + 级联删所有子分录
  └── dealstatus = 1 → 不清理（已完成记录保留）
```

⚠ **与 delete OP 的区别**：
- 走 `helper.deleteOne` 直接删 · 不经过 PermInitRecordDeleteOp
- PermInitDeleteValidator 不会被触发
- 这是隐式清理路径 · 连操作日志都不会记

---

## 七、状态机（行级 · dealstatus 两态）

### 7.1 主表状态

```
不存在 ──new──→ 草稿态(in-memory)
    │
    ├── userimport/roleimport → 草稿态(分录有数据 · dealstatus=0)
    │       │
    │       ├── save → 持久化草稿(dealstatus=0 · 列表不可见)
    │       │       │
    │       │       ├── modify+save → 草稿态(dealstatus=0)
    │       │       ├── finishinit → 已完成态(dealstatus=1) ⭐ 最终态
    │       │       └── beforeClosed → 物理删除
    │       │
    │       └── beforeClosed → 物理删除（未保存草稿）
    │
    └── finishinit → 已完成态(dealstatus=1 · 列表可见 · 只读)
            │
            ├── delete → 物理删除（带 PermInitDeleteValidator）
            └── copy → 草稿态(新单 · 不带 id)
```

### 7.2 dealstatus 两态行为

| dealstatus | 状态 | UI 行为 | 列表可见 | 删草稿 |
|---|---|---|---|---|
| 0 | 未完成（草稿） | finishinit 按钮可见 | ❌（FP_LSF1 过滤） | ✅ beforeClosed 自动删 |
| 1 | 已完成 | finishinit + flexpanelap4 隐藏（FP_BBD3） | ✅ | ❌ 手动删（带 Validator） |

---

## 八、关键操作与插件对照表

| 用户动作 | opKey/事件 | 触发插件 | 主要规则节点 |
|---|---|---|---|
| 进入列表页 | preOpenForm + setFilter + beforeShowBill | HRAdminStrictPlugin / PermInitRecordList | FP_HRA1-3 + FP_LSF1 + FP_LBSB1 |
| 列表新建用户权限初始化 | inituserrole | PermInitRecordList | FP_LBDO1（智能激活） |
| 列表新建角色权限初始化 | initrole | PermInitRecordList | FP_LBDO2（智能激活） |
| 列表 labelpolicy 跳转 | initrole + beforeItemClick | PermInitRecordList | FP_LBIC1 |
| 打开详情（新建） | new + beforeBindData | PermInitRecordEdit | FP_BBD1 + FP_BBD2 |
| 打开详情（修改） | modify + beforeBindData | PermInitRecordEdit | FP_BBD1 + FP_BBD3 |
| 填写任务名 | propertyChanged(name) | PermInitRecordEdit | FP_PC1 |
| 用户导入 | userimport (beforeDoOperation) | PermInitRecordEdit | FP_BDO1 + FP_BDO2 |
| 角色导入 | roleimport (beforeDoOperation) | PermInitRecordEdit | FP_BDO1 + FP_BDO2 |
| 二次确认清空 | confirmCallBack(reimport) | PermInitRecordEdit | FP_CCB1/FP_CCB2 |
| 导入完成回调 | closedCallBack(importFinish) | PermInitRecordEdit | FP_CB1/FP_CB2/FP_CB3 |
| 完成初始化 | finishinit (beforeDoOperation) | PermInitRecordEdit | FP_BDO3/FP_BDO4/FP_BDO6/FP_BDO7/FP_BDO8 |
| 角色字段回填 | beforeBindData → initRoleFdEntry | PermInitRecordEdit | FP_BBD4 |
| 字段中文名回填 | beforeBindData → paintErrMarkCol | PermInitRecordEdit | FP_BBD5 |
| 保存 | save (beforeDoOperation) | PermInitRecordEdit | FP_BDO5 |
| 保存后静默 | save (afterDoOperation) | PermInitRecordEdit | FP_ADO1/FP_ADO2 |
| 关闭页（草稿清理） | beforeClosed | PermInitRecordEdit | FP_BC1 |
| 列表删除 | delete | PermInitRecordDeleteOp + PermInitRecordList | OP_DEL_PP1 + OP_DEL_AV1 |
| 列表下载 | download | PermInitRecordList | FP_LBDO3 |
| 列表模板下载 | dlusertemp/dlroletemp/dlusertip/dlroletip | PermInitRecordList | FP_LBDO4/FP_LBDO5 |
| 导出回调 | closedCallBack(exportUrl) | PermInitRecordList | FP_LCB1 |

---

## 九、与上下游交互（数据流接口）

| 接口 | 方向 | 触发 | 同步/异步 |
|---|---|---|---|
| `hrcs_perminitrecord` → `UserPermInitConvertService.convertRecord` | finishinit userrole | FP_BDO4 | ✅ 同步（finishinit 流程内） |
| `hrcs_perminitrecord` → `PermRoleInitService.initRole` | finishinit role | FP_BDO3 | ✅ 同步 |
| `hrcs_perminitrecord` → `hrcs_perminitcheckresult`（子页面） | finishinit 校验失败 userrole | FP_BDO4 | ✅ 同步（Modal 弹窗） |
| `hrcs_perminitrecord` → `hrcs_roleinitcheckresult`（子页面） | finishinit 校验失败 role | FP_BDO3 | ✅ 同步（Modal） |
| `hrcs_perminitrecord` → `sch_task` | dlusertemp/dlroletemp 异步模板 | FP_LBDO4 | ✅ 异步（TaskInfo 回调） |
| `hrcs_perminitrecord` ← `bos_user` | F7 引用 | 分录字段 dim_user/dr_user/bd_user/field_user | 同步查询 |
| `hrcs_perminitrecord` ← `perm_role` | F7 引用 | 分录字段 dim_role/dr_role/bd_role/field_role | 同步查询 |
| `hrcs_perminitrecord` ← `bos_org` | F7 引用 | dim_fileorg/dr_fileorg/bd_fileorg/field_fileorg | 同步查询 |
| `hrcs_perminitrecord` ← `bos_entityobject` | F7 引用 | 分录字段 dr_entitytype/bd_entitytype/field_entitytype/rfield_entitytype | 同步查询 |

---

## 十、不发 BEC 实证

```bash
$ grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hrcs_perminitrecord/

(0 results across 4 files)
```

**结论**：本场景标品 0 处发 BEC 事件。ISV 若需要发 BEC（CS-05）· **必须自建** · 在自建 OP 的 `afterExecuteOperationTransaction` 阶段调 `IEventService.triggerEventSubscribeJobs`（PR-010 + PR-011）。

---

## 十一、关键超时/阈值（实证）

| 操作 | 阈值 | 失败行为 | 实证 |
|---|---|---|---|
| userrole 模式校验维度 | 4（用户角色/时间规则/业务数据规则/字段） | 任一有错 → 弹 hrcs_perminitcheckresult | FP_BDO4 L282-L330 |
| role 模式校验维度 | 5（角色/功能项/维度/数据范围/字段） | 任一有错 → 弹 hrcs_roleinitcheckresult | FP_BDO3 L233-L281 |
| reimport 清空子分录数 | userrole 4 / role 5 | 不清空 → 覆盖导入失败 | FP_CCB1/FP_CCB2 |
| 模板下载异步 | TaskInfo 回调 | 任务失败不阻塞列表 | FP_LBDO4 + FP_LCB1 |
| HR 域准入 | 三闸 | F7 直通 / admin+cosmic 二选一 / HR 域管理员 | FP_HRA1-3 |

---

→ 详细规则见 `form_lifecycle_rules.json`。
