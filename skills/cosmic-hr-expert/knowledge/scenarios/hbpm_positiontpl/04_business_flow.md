# 业务流转 · 岗位模板（hbpm_positiontpl）

> **状态**: 🟢 基于反编译 4 类实证
> **confidence**: verified

---

## 一、核心业务流转：新建岗位模板

```
用户在列表页点击"新建"
    ↓
PositionTplListPlugin.beforeDoOperation(operateKey="new")
  validateOrgExistOpenTpl()
    → 查权限下 BU 是否有 openpositiontpl=true
    → 全未开启 → 提示"...均未启用模板库，无法新增。" → cancel
  通过 → showStructProjectForm()
    打开 hbpm_positiontpl 表单（ShowType.MainNewTabPage）
    传 customParam("orgId", orgId)（来自列表 org 过滤器）
    setCloseCallBack("new")
    args.setCancel(true)
    ↓
PositionTplEditPlugin.preOpenForm
  ADDNEW → setCaption("新增岗位模板")
    ↓
PositionTplEditPlugin.afterCreateNewData
  isReFresh 缓存防重入
  orgId 从 customParam 取 → 若有 → 直接 setValue("org", orgId)
  → 若无 → PermissionServiceHelper.getAllPermOrgs() 查权限组织
    · 优先填当前请求组织
    · 其次填权限列表第一个
  this.setModifyStrategy() → 根据 BU 参数初始化 modifystrategy 可见性
    ↓
PositionTplEditPlugin.beforeBindData
  option="view" → 调 viewStatus(view, dataEntity)
  setModifyStrategy()
    ↓
PositionTplEditPlugin.afterBindData
  JobLevelGradeRangeUtil.afterBindData(model, view) → 岗位层级范围初始化
  model.setDataChanged(false)
  setFiedRangeVisable() → 根据 ablemodifyfield 控制 fieldrange 可见性
    ↓
用户填写岗位模板信息
    ↓
PositionTplEditPlugin.beforeDoOperation(operateKey="save")
  !model.getDataChanged() → 提示"无信息变更，请确认。" → cancel
  setModifyStrategy(true) → 更新修改策略
    ↓
PositionTplSaveOp.onAddValidators
  （空实现 · 预留扩展点）
    ↓
PositionTplSaveOp.beforeExecuteOperationTransaction
  过滤 id != 0L 的记录（已有记录）
  HRBaseServiceHelper.loadDynamicObjectArray() → 加载历史快照 → oldDynMap
    ↓
（事务提交）
    ↓
PositionTplSaveOp.endOperationTransaction
  new PositionTplChangeSyncPosService(oldDynMap)
  syncUpdatePosService.syncUpdatePosition(e.getDataEntities())
  → 级联同步下游岗位数据
    ↓
PositionTplSaveOp.afterExecuteOperationTransaction
  new ChangeMsgServiceImpl().sendMsg() → 发 BEC 消息
  IBosPositionService.getInstance().commonSyncPositions() → 通用岗位同步
    ↓
PositionTplEditPlugin.afterDoOperation(operateKey="save" · isSuccess=true)
  formShowParameter.setStatus(OperationStatus.VIEW)
  viewStatus(view, model.getDataEntity()) → 切换为查看态
```

---

## 二、核心业务流转：变更（modify）

```
用户在详情页（VIEW 态）点击"变更"
    ↓
PositionTplEditPlugin.beforeDoOperation(operateKey="modify")
  operate.getOption().setVariableValue("isFromPage", "1")
  获取 org → SystemParamHelper.getBatchParameter(orgId) → 查 BU 参数
    openpositiontpl=false → 提示"...未启用模板库，不可变更数据。" → cancel
    positiontplchangepos=true → IHRCSService.getContent(TIPS_ID) → showTipNotification(tips, 10000)
  setModifyStrategy(true)
    ↓
PositionTplEditPlugin.afterDoOperation(operateKey="modify" · isSuccess=true)
  editStatus(view) → formShowParameter.setStatus(EDIT)
  bar_save 显示 · bar_modify 隐藏
```

---

## 三、核心业务流转：设置适用范围（applicationscope）

```
用户在列表页选中记录，点击"设置适用范围"
    ↓
PositionTplListPlugin.beforeItemClick(itemKey="applicationscope")
  PositionTplRepository.queryDisablePositionTplByIds(primaryKeyValues)
    → 有禁用数据 → 提示"禁用数据不可设置适用组织范围。"
    → addOperateLog() 记录操作日志 → cancel
  通过 → 继续
    ↓
PositionTplListPlugin.afterDoOperation(operateKey="donothing_setscope" · isSuccess=true)
  打开 "hbpm_applicationscope" 表单（ShowType.Modal）
  setCustomParam("positiontplId", primaryKeyValues)
  setCloseCallBack("tpl_applicationscope_edit")
    ↓
用户设置适用范围后关闭
    ↓
PositionTplListPlugin.closedCallBack("tpl_applicationscope_edit")
  returnData != null → invokeOperation("refresh")
```

---

## 四、列表打开详情（beforeShowBill）

```
用户在列表双击或点击查看记录
    ↓
PositionTplListPlugin.beforeShowBill(e)
  parameter.getPkId() != null && formId="hbpm_positiontpl"
    → setStatus(OperationStatus.VIEW)
    → setBillStatus(BillOperationStatus.VIEW)
  → 以 VIEW 态打开详情（触发 viewStatus 三态控制）
```

---

## 五、BU 过滤器（PositionTplBuListPlugin）

```
列表页初始化过滤器（filterContainerInit）
  遍历 commonFilterColumns 找 org.name 列
    → 设置 defaultValue="" （清空默认值）
    → pageCache.put("first", "false")

列表筛选 org 字段 F7 弹窗（filterContainerBeforeF7Select）
  isAddBUFilter(fieldName) && !permOrgResult.hasAllOrgPerm()
    → getAdminOrgBaseDataFilter(entityName, hasPermOrgs) → QFilter("id","in",orgIdList)
    → addCustomQFilter()

列表过滤执行（filterColumnSetFilter）
  isAddBUFilter(fieldName) && !permOrgResult.hasAllOrgPerm()
    → 同上加权限过滤
```
