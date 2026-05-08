# 业务规则 · 权限初始化 (hrcs_perminitrecord)

> **状态**: 🟢 基于 `form_lifecycle_rules.json` (27 rules in 4 forms) + `rules_chain_all.json` (66 opKeys) + `curated_sdk.json`
> **confidence**: verified
> **数据源**: PermInitRecordEdit/PermInitRecordList/PermInitRecordDeleteOp/HRAdminStrictPlugin (2026-04-28)

---

## ⚡ Developer Quick Reference

| 问 | 答 |
|---|---|
| inittype 有几个值？ | 2 个：`userrole` / `role` · FP_BBD1 硬编码二分支路由 |
| dealstatus 有几个值？ | 2 个：`0`=未完成(草稿) / `1`=已完成 · FP_BBD3 + FP_LSF1 |
| finishinit 做什么？ | 四/五维校验 → 落地下游 service → dealstatus 标 1 · FP_BDO3/FP_BDO4 |
| 草稿自动清理吗？ | 是 · beforeClosed dealstatus=0 → helper.deleteOne 物理删 · FP_BC1 |
| 任务名重复处理？ | 同名已完成→阻断；同名未完成→自动清理 · FP_BDO1 checkTaskName |
| 哪些字段 isvCanModify=false？ | L0 系统字段(creator/modifier/createtime/modifytime/masterid) + issyspreset/disabler/disabledate/initdatasource/orinumber/oristatus/oriname + includesub |

---

## 一、规则来源全景

```
hrcs_perminitrecord 业务规则
    ├── 元数据 formRule (1 条)                              ← form_lifecycle_rules.json metadataRules
    │   └── 360DI5ICICIJ: dealstatus=1 → 初始化完成（控制 UI 可见性）
    ├── Java FormPlugin 规则 (~27 条 · 8 lifecycleMethods)  ← form_lifecycle_rules.json forms
    │   ├── PermInitRecordEdit (7 lifecycleMethods · 17 rules)
    │   ├── PermInitRecordList (5 lifecycleMethods · 9 rules)
    │   └── HRAdminStrictPlugin (1 lifecycleMethod · 3 rules)
    ├── Java OP 规则 (~5 条)                                 ← form_lifecycle_rules.json forms
    │   └── PermInitRecordDeleteOp (2 lifecycleMethods · 2 rules)
    ├── opKey OP 链规则 (66 opKey · 8 核心 opKey 富化)       ← rules_chain_all.json
    │   ├── save / delete / userimport / roleimport / finishinit
    │   ├── inituserrole / initrole / download
    │   └── dlusertemp / dlroletemp / dlusertip / dlroletip
    └── 平台规则 PR (引用 _shared/platform_rules.json 11 条)
```

---

## 二、核心业务规则（按"业务关键性"排）

### 2.1 inittype 双模路由规则（最关键）

**规则**：权限初始化是双形态场景——用户权限模式（userrole，默认）和角色权限模式（role）。两个模式用同一张主单 `hrcs_perminitrecord`，但写入完全不同的子分录组。

**实证位置**：
- `PermInitRecordEdit.beforeBindData` L111-L137 · FP_BBD1
- `PermInitRecordEdit.beforeBindData` L128-L132 · FP_BBD2

**路由逻辑**：
```
recordId not empty（编辑已有记录）
  ├── initType = role
  │     → showRoleFuncForm(recordId)    // 嵌入 hrcs_permroleinitfunc
  │     → showRoleDimForm(recordId)     // 嵌入 hrcs_permroleinitdim
  │     → showRoleDrForm(recordId)      // 嵌入 hrcs_permroleinitdr
  │     → initRoleFdEntry(recordId)     // 回填 rolefieldentry 中文名
  │     → 显示 rolebaseentry/rolefuncentry/roledimentry/roledataentry/rolefieldentry
  └── initType = userrole（默认）
        → showDynaDimForm(recordId)     // 嵌入 hrcs_dynadim
        → paintErrMarkCol()             // 回填 userbdentry/userfieldentry 中文名
        → 显示 userdimentry/userdataruleentry/userbdentry/userfieldentry

recordId empty（新建）
  └── customParam.inittype = role → setValue(inittype, ROLE)
      否则 → setValue(inittype, USERROLE)  // 默认
```

**业务含义**：两种初始化模式互斥——userrole 模式是"给已有用户分配权限"，role 模式是"创建新角色并赋权"。

**ISV 提示**：
- ❌ 不要加第三种 inittype（如 dept 部门级初始化）——FP_BBD1 二分支硬编码，加新分支需改标品代码
- ❌ 不要替换 default 值——ListPlugin.beforeShowBill 传 inittype（FP_LBSB1），覆盖会破坏列表-表单链路
- ✅ 可通过 ext FormPlugin.propertyChanged 联动 inittype 变更时的 UI 切换

### 2.2 任务名规则（必填 + 重名清理 + 绿色 FieldTip）

**规则**：任务名 (name) 是唯一必填业务字段，处理逻辑有三层。

**实证位置**：
- `PermInitRecordEdit.beforeDoOperation` L209-L213, L337-L371 · FP_BDO1
- `PermInitRecordEdit.beforeDoOperation` L240-L246, L287-L293 · FP_BDO8
- `PermInitRecordEdit.propertyChanged` L373-L384 · FP_PC1

**三层逻辑**：

1. **空名阻断**（userimport/roleimport/finishinit 入口）：
   - name 空 → showTipNotification("请按要求填写任务名称")
   - FieldTip(Error, notNull, name, "值不能为空") 红色标记
   - args.setCancel(true) 阻断

2. **重名已完成记录阻断**：
   - QFilter: `name = taskName AND id != currentId`
   - dealstatus = 1 的同名记录存在 → 报"任务名重复"阻断

3. **重名未完成（脏数据）自动清理**：
   - 同名 dealstatus = 0 的记录 → helper.deleteOne 直接物理删除
   - 清理后允许继续

4. **name 从空变非空 → 绿色 FieldTip**：
   - FP_PC1：FieldTip(Info, notNull, name, "") 绿色对勾

**ISV 提示**：
- ❌ 重名清理是危险动作（直接物理删除 hadoop），ISV 加业务规则不要在 checkTaskName 里加

### 2.3 导入已有数据二次确认（reimport）

**规则**：userimport / roleimport 时如果已有分录数据，不允许直接覆盖，必须先弹二次确认让用户显式确认清空。

**实证位置**：
- `PermInitRecordEdit.beforeDoOperation` L214-L232 · FP_BDO2
- `PermInitRecordEdit.confirmCallBack` L417-L447 · FP_CCB1/FP_CCB2

**流程**：
```
beforeDoOperation(userimport/roleimport)
  └── 已有 userdimentry 或 rolebaseentry 行
        → showConfirm("导入将清空已有的数据 是否导入", OKCancel)
        → args.setCancel(true)  // 先阻断，等用户确认
        → 用户点 Yes → confirmCallBack(reimport)
              ├── initType = role → 清空 5 张子分录：
              │     record.set(rolebaseentry, null)
              │     record.set(rolefuncentry, null)
              │     record.set(roledimentry, null)
              │     record.set(roledataentry, null)
              │     record.set(rolefieldentry, null)
              ├── initType = userrole → 清空 4 张子分录：
              │     record.set(userdimentry, null)
              │     record.set(userdataruleentry, null)
              │     record.set(userbdentry, null)
              │     record.set(userfieldentry, null)
              ├── helper.saveOne(record)  // 触发 cascade delete entry rows
              └── 跳转导入子页面（hrcs_permimportstart 或 hrcs_rolepermimportstart）
```

**ISV 提示**：
- ISV 加了自定义子分录字段（如 ISV 自建表 `_isv_role_extra_entry`），必须在 CCB 里也置 null
- reimport callBackId 是标品占用，ISV 不要复用

### 2.4 finishinit 完成初始化规则（最核心落库路径）

**规则**：finishinit 是权限初始化的最后一步，完成四/五维校验 → 落地下游 → 标 dealstatus=1。

**实证位置**：
- `PermInitRecordEdit.beforeDoOperation` L233-L330 · FP_BDO3/FP_BDO4

**userrole 模式四维校验**：
```
PermInitFinishValidateService svc = new PermInitFinishValidateService();
int[] errArr = svc.clickFinishValidate(recordId);
// errArr[0] = userRoleErrorNum    (用户角色绑定错误数)
// errArr[1] = dateRuleErrorNum    (时间规则错误数)
// errArr[2] = bdDataRuleErrorNum  (业务数据规则错误数)
// errArr[3] = fieldErrorNum       (字段权限错误数)
```

**role 模式五维校验**：
```
PermRoleInitFinishValidateService svc = new PermRoleInitFinishValidateService();
int[] errArr = svc.clickFinishValidate(recordId);
// errArr[0] = roleErrorNum        (角色错误数)
// errArr[1] = funcItemErrorNum    (功能项错误数)
// errArr[2] = dimErrorNum         (维度错误数)
// errArr[3] = dataRangeErrorNum   (数据范围错误数)
// errArr[4] = fieldErrorNum       (字段错误数)
```

**校验失败**：弹 `hrcs_perminitcheckresult`（userrole）或 `hrcs_roleinitcheckresult`（role）展示错误详情。

**校验通过**：
- userrole → `UserPermInitConvertService.convertRecord(recordId)` → 落到下游 hrcs_userrole* 表族
- role → `PermRoleInitService.getInstance().initRole(recordId)` → 批量创建/更新角色权限
- 都成功后 → `setValue(dealstatus, 1)` → `invokeOperation(save)`

**名前置条件**：
- FP_BDO6：无数据（id=0 或子分录无数据）→ showTipNotification("暂无数据 请先导入用户权限或角色权限")
- FP_BDO7：dataChanged && name 非空 → 自动隐式 save（PageCache.importInvokeSave=1 标记）
- FP_BDO8：name 空 → 阻断 + FieldTip

**ISV 提示**：
- 四/五维校验服务（PermInitFinishValidateService / PermRoleInitFinishValidateService）是 hrcs 内部 service · 无 SDK 注解 · ISV 不要自行实例化
- ISV 加第六维校验 → ext FormPlugin.beforeDoOperation 用 `if (operateKey = finishinit)` 加 `isValid` 检查
- 下游落库服务同理，ISV 不要在 ext FormPlugin 里调 convertRecord/initRole

### 2.5 草稿自动清理规则（隐式物理删除）

**规则**：用户关闭页面时，如果 dealstatus=0（未完成）且 id 不为 0（已保存过），自动物理删除主单 + 子分录。

**实证位置**：
- `PermInitRecordEdit.beforeClosed` L402-L411 · FP_BC1

**逻辑**：
```
beforeClosed
  └── dealstatus = 0 && id != 0
        → new HRBaseServiceHelper(hrcs_perminitrecord).deleteOne(id)
```

**危险点**：走 `helper.deleteOne` 直接物理删除，**不经过 delete OP**，因此 `PermInitRecordDeleteOp` 的 `PermInitDeleteValidator` 不会被触发。外键级联删除所有 15 张子分录 + 4 张子子分录。

**ISV 提示**：
- 这是隐式物理删除路径！如果 ISV 加了引用 perminitrecord 的关联表，要在 ext FormPlugin.beforeClosed 里也清理自建的关联数据，否则留垃圾
- 如果 ISV 想阻止草稿自动清理（如审计场景），ext FormPlugin.beforeClosed evt.setCancel(true)，但 BillView 强制关闭流程下 setCancel 可能不生效

### 2.6 save 阶段 ORM.genLongId 兜底

**规则**：save 操作前如果 dataEntity.id = 0，用 ORM 生成分布式主键。

**实证位置**：
- `PermInitRecordEdit.beforeDoOperation` L331-L334 · FP_BDO5

**逻辑**：
```
beforeDoOperation(save)
  └── dataEntity.id = 0
        → long id = orm.genLongId(hrcs_perminitrecord)
        → dataEntity.set(id, id)
```

**ISV 提示**：ISV 加 ext OP 时不要重复 genLongId，主单 id 已由这里生成。

### 2.7 隐式 save 成功消息抑制

**规则**：finishinit 路径里 dataChanged 触发的隐式 save，不弹"保存成功"提示。

**实证位置**：
- `PermInitRecordEdit.afterDoOperation` L192-L197 · FP_ADO1

**逻辑**：
```
afterDoOperation(save) && operationResult.isSuccess()
  └── PageCache.importInvokeSave not empty
        → PageCache.remove(importInvokeSave)
        → args.getOperationResult().setShowMessage(false)  // 抑制保存成功提示
```

**ISV 提示**：ISV 自己挂 afterDoOperation 不要覆盖 `operationResult.setShowMessage(true)`，否则破坏标品的隐式 save 静默。

---

## 三、HR 域准入规则（HRAdminStrictPlugin）

### 3.1 三闸校验

| 闸 | 条件 | 失败行为 | 实证 |
|---|---|---|---|
| 第一闸 F7 | `fsp instanceof ListShowParameter && lsp.isLookUp()` | return（直接放行 · 不校验） | FP_HRA1 · L29-L35 |
| 第二闸 | `!isAdminUser && !isCosmicUser` | setCancel + cancelMessage "您无法访问该功能 因为您不是HR领域管理员" | FP_HRA2 · L41-L47 |
| 第三闸 | `!HRAdminService.isHrAdmin()` | setCancel + cancelMessage 同上 | FP_HRA3 · L48-L52 |

### 3.2 F7 LookUp 模式直通

**业务含义**：当 hrcs_perminitrecord 被其他场景作为基础资料通过 F7 引用时，普通业务用户也能选（不强制 HR 管理员身份）。

**ISV 提示**：
- F7 LookUp 模式直通确保 hrcs_perminitrecord 可被其他场景作为基础资料引用 · ISV 不要打破
- 11 个 hrcs 表单共用 HRAdminStrictPlugin · ISV 严禁替换

---

## 四、列表页业务规则

### 4.1 dealstatus=1 强制过滤

**规则**：列表只显示 dealstatus=1（已完成）的记录，草稿单不展示。

**实证位置**：
- `PermInitRecordList.setFilter` L84-L86 · FP_LSF1

```
evt.getQFilters().add(new QFilter(dealstatus, =, 1));
```

**ISV 提示**：
- ISV 列表过滤要 add 自己的 QFilter · 不要 setQFilters 覆盖（会破这条标品过滤）
- 想看草稿单不能用列表看，走 BusinessDataServiceHelper 自查 t_hrcs_pinitrecord

### 4.2 inittype 回填到 customParam

**规则**：用户单击列表行打开主单时，ListPlugin 先查一次主单拿 inittype，回填到 customParam，让 PermInitRecordEdit.beforeBindData 知道走 role 还是 userrole 分支。

**实证位置**：
- `PermInitRecordList.beforeShowBill` L88-L99 · FP_LBSB1

### 4.3 智能激活已开页面

**规则**：点 inituserrole 或 initrole 时，如果已有一个 ADDNEW 状态的同类型页面开着，直接激活那个 tab 而不是新建。

**实证位置**：
- `PermInitRecordList.beforeDoOperation` L127-L140, L152-L165 · FP_LBDO1/FP_LBDO2

```
String pageId = SessionManager.getCurrent().get(this.getView().getPageId() + "showFormuserrole");
IFormView recordView = this.getView().getView(pageId);
if (pageId not empty && recordView != null && status = ADDNEW) {
    service.addAction(activate, pageId);  // 激活已有 tab
} else {
    showUserInitDetail();  // 新开
}
```

### 4.4 download 按 inittype 路由两种 ExcelWriter

**规则**：下载已完成记录，role 用 RoleRecordExcelWriter（多 sheet：基本信息/功能项/维度/数据范围/字段），userrole 用 RecordExcelWriter（多 sheet：用户角色绑定/时间规则/业务数据规则/字段权限）。

**实证位置**：
- `PermInitRecordList.beforeDoOperation` L141-L151 · FP_LBDO3

### 4.5 模板下载异步

**规则**：dlusertemp/dlroletemp 走调度任务异步生成 Excel（几 MB），taskClassName 通过反射调用 `PermTemplateExportTask` / `PermRoleTemplateExportTask`。

**实证位置**：
- `PermInitRecordList.beforeDoOperation` L174-L196 · FP_LBDO4

### 4.6 labelpolicy 模式

**规则**：initrole + PageCache.labelpolicy 非空 → 不直接初始化，跳 hrcs_labelpolicytask 任务列表。

**实证位置**：
- `PermInitRecordList.beforeItemClick` L101-L121 · FP_LBIC1

---

## 五、删除规则

### 5.1 声明读 dealstatus

**规则**：苍穹 OP 默认只载入主键，用什么字段必须显式声明。

**实证位置**：
- `PermInitRecordDeleteOp.onPreparePropertys` L24-L26 · OP_DEL_PP1

```
args.getFieldKeys().add(dealstatus);
```

### 5.2 挂 PermInitDeleteValidator

**规则**：删除前过 PermInitDeleteValidator 校验。

**实证位置**：
- `PermInitRecordDeleteOp.onAddValidators` L28-L30 · OP_DEL_AV1

```
args.addValidator(new PermInitDeleteValidator());
```

### 5.3 草稿清理不走 OP

**注意**：FP_BC1 beforeClosed 的草稿自动清理走 `helper.deleteOne` 直接物理删除，**不经过 PermInitRecordDeleteOp**，PermInitDeleteValidator 不会触发。

---

## 六、字段映射回填规则

### 6.1 rolefieldentry 字段中文名回填（role 模式）

**规则**：rolefieldentry 存的是字段权限规则（角色编码 + 业务对象 + 字段 propKey），只存了 propKey 没存中文名。beforeBindData 时用 `EntityMetadataCache.getDataEntityType(entityNumber)` 反查 propName 回填到 `rfield_propname`，纯展示用，不落库。

**实证位置**：
- `PermInitRecordEdit.beforeBindData` L139-L155 · FP_BBD4

### 6.2 userbdentry/userfieldentry 字段中文名回填（userrole 模式）

**规则**：同 6.1 但带性能优化——把每个业务对象的 propKey→propName 字典缓存到 `PageCache.entityFields`，避免每次都查 EntityMetadataCache。

**实证位置**：
- `PermInitRecordEdit.beforeBindData` L544-L585 · FP_BBD5

---

## 七、元数据 formRule（1 条）

| ruleId | 类型 | preCondition | 描述 | 实证 |
|---|---|---|---|---|
| `360DI5ICICIJ` | formRule | `dealstatus = '1'` | 初始化完成 · 控制完成后 UI 可见性（如隐藏 finishiinit 按钮） | FP_FR1 |

**注意**：本场景元数据规则极少（仅 1 条），主要业务规则承载在 Java 反编译类中。

---

## 八、opKey 执行链（66 opKey 中 5 个核心的 validator/businessRule）

### 8.1 save

| 阶段 | 插件/规则 | 实证 |
|---|---|---|
| FormPlugin beforeDoOperation | FP_BDO5: ORM.genLongId 兜底主键 | L331-L334 |
| OP onAddValidators | （标品 HRBaseDataOp 默认 · 无自定义 Validator） | rules_chain_all |
| OP beforeExecuteOperationTransaction | HRBaseDataStatusOp → HRBaseDataLogOp → HRBaseDataEnableOp → HRBaseOriginalOp → PermInitRecordDeleteOp（序列 #15-20） | rules_chain_all |
| FormPlugin afterDoOperation | FP_ADO1: 抑制隐式 save 消息; FP_ADO2: 重算 flex 可见性 | L192-L198 |

### 8.2 delete

| 阶段 | 插件/规则 | 实证 |
|---|---|---|
| OP onPreparePropertys | OP_DEL_PP1: 声明 dealstatus | rules_chain_all |
| OP onAddValidators | OP_DEL_AV1: PermInitDeleteValidator | rules_chain_all |
| OP executionChain | HRBaseDataStatusOp → HRBaseDataLogOp → HRBaseDataEnableOp → HRBaseOriginalOp → PermInitRecordDeleteOp | rules_chain_all |

### 8.3 userimport / roleimport

| 阶段 | 插件/规则 | 实证 |
|---|---|---|
| FormPlugin beforeDoOperation | FP_BDO1: 任务名校验 + 重名清理 | L209-L213 |
| FormPlugin beforeDoOperation | FP_BDO2: 二次确认清空已有数据 | L214-L232 |
| FormPlugin confirmCallBack | FP_CCB1/FP_CCB2: 清空 5/4 张子分录 | L417-L447 |

### 8.4 finishinit

| 阶段 | 插件/规则 | 实证 |
|---|---|---|
| FormPlugin beforeDoOperation | FP_BDO6: 无数据拒绝 | L250-L253 |
| FormPlugin beforeDoOperation | FP_BDO7: dataChanged 隐式 save | L238-L249 |
| FormPlugin beforeDoOperation | FP_BDO8: name 空阻断 | L240-L246 |
| FormPlugin beforeDoOperation | FP_BDO3/FP_BDO4: 四/五维校验 + 落地下游 | L233-L330 |

---

## 九、平台规则引用（PR · `_shared/platform_rules.json`）

| PR ID | 名称 | 本场景应用点 |
|---|---|---|
| PR-001 | ISV 并列挂插件 · 不继承场景类 | 反编译 4 类不可继承 · ext FormPlugin 并列挂 |
| PR-002 | RowKey 执行顺序 | ext 插件排在标品之前/之后 |
| PR-003 | FormPlugin getModel().setValue · OP entity.set | FP_BBD4/FP_BBD5 字段回填 |
| PR-005 | ID 生成 用 ORM.genLongId / kd.bos.id.ID | FP_BDO5 save 兜底 |
| PR-007 | 预置数据 issyspreset 保护 | issyspreset/orinumber/oristatus/oriname/initdatasource 红区标记 |
| PR-008 | 时序 iscurrentversion 过滤 | （本场景非 HisModel · 不适用） |
| PR-010 | OP 13 生命周期 | PermInitRecordDeleteOp onPreparePropertys + onAddValidators |
| PR-011 | BEC 业务事件中心 | （标品 0 处发布 · ISV 自建） |

→ 详细规则见 `_shared/platform_rules.json`。

---

## 十、不要做的事（反模式速查）

| 反模式 | 错在哪 | 正确做法 |
|---|---|---|
| `extends PermInitRecordEdit` | PR-001 违反 | extends HRDataBaseEdit · 并列挂 |
| `extends PermInitRecordList` | PR-001 违反 | extends HRDataBaseList · 并列挂 |
| `extends PermInitRecordDeleteOp` | PR-001 违反 | extends HRDataBaseOp · 并列挂 |
| 加第三种 inittype（如 dept） | FP_BBD1 硬编码二分路 | ext FormPlugin.propertyChanged 联动 |
| 在 beforeClosed 里阻止草稿清理期望生效 | BillView 强制关闭 · setCancel 不一定生效 | ext 在 beforeClosed 追加清理 · 不 setCancel |
| 在 reimport 回调里复写标品的清空行为 | FP_CCB1/FP_CCB2 硬编码 5/4 张子分录 | ext 在 CCB 末尾追加自己的清理逻辑 |
| 在 finishinit 里直接实例化校验 Service | 无 SDK 注解 · hrcs 内部类 | ext FormPlugin beforeDoOperation 加自己的 isValid |
| 假设草稿清理走 delete OP | FP_BC1 走 helper.deleteOne · 不触发 OP | 要拦截删草稿需 ext beforeClosed |
| 用 `setQFilters` 覆盖列表过滤 | 会破 FP_LSF1 dealstatus=1 过滤 | add QFilter · 不要 setQFilters |
| 复用 PageCache 标品 key | importInvokeSave/deltempData/success/entityFields 被占用 | 用 ISV 前缀命名 key |
| 假设本场景发 BEC | grep 实证 0 处 | 走 ISV 自建（CS-05 · PR-011） |
| 假设本场景有 HisModel | grep iscurrentversion/HisModel/boid 0 命中 | 不用套 boid/sourcevid 等字段 |

---

## 十一、调用 PR 完整列表（脚本可校验）

```
PR-001  §一总纲 + §十反模式 (PermInitRecordEdit/List/DeleteOp 不可继承)
PR-003  §六字段回填 (getModel().setValue)
PR-005  §2.6 save 兜底 (ORM.genLongId)
PR-007  §十 (issyspreset 红区)
PR-008  ✗ 不适用（非 HisModel）
PR-010  §5.1/5.2 + §八 OP 链 (onPreparePropertys + onAddValidators)
PR-011  §十 (BEC 标品 0 处)
```

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

## chgaction 实证补充（HRAdminStrictPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit -->

## chgaction 实证补充（PermInitRecordEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_perminitrecord` | deleteOne | HRBaseServiceHelper | <self> (depth=0) |
| `hrcs_perminitrecord` | saveOne | HRBaseServiceHelper | <self> (depth=0) |
| `perm_userrole` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrcs.bussiness.service.perm.common.RoleAssignService.r |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList -->

## chgaction 实证补充（PermInitRecordList 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp -->

## chgaction 实证补充（PermInitRecordDeleteOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp -->
