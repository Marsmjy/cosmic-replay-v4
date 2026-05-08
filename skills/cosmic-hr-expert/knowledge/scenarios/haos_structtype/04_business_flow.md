# 业务流转 · 架构类型（haos_structtype）

> **状态**: 🟢 基于反编译 7 类实证
> **confidence**: verified

---

## 一、核心业务流转：新建架构类型

```
用户在列表页点击"新建"
    ↓
StructTypeEditPlugin.preOpenForm
  OperationStatus.ADDNEW → 标题设为"新增架构类型"
    ↓
StructTypeEditPlugin.afterCreateNewData
  读取 formShowParameter.getCustomParam("SELECT_ORG_ID")
  → 若有 → 直接设 org 字段
  → 若无 → OrgPermHelper.getHRPermOrg(true) 查权限组织
    · 优先填当前请求组织（RequestContext.getOrgId()）
    · 其次填权限列表第一个
    ↓
用户填写 number/name/effdt/metanumsuffix 等字段
    ↓
用户点击"保存"
    ↓
StructTypeSaveOp.beginOperationTransaction
  enable="1" && fromDatabase=false → 新数据
    ORM.genLongIds() → 生成 PK
    StructClassHelper.saveNew(dyn, pkId) → 写 StructClass
    option.setVariableValue(id, "0") → 标记 isNewData
    ↓
（事务提交）
    ↓
StructTypeSaveOp.afterExecuteOperationTransaction
  新建事务 TX.requiresNew()：
    StructClassHelper.creatMetaDataAndMenu(metaNumSufFix, dataEntity)
    → 创建元数据页面 + 菜单
    catch → rollback → KDBizException
  StructClassHelper.saveStructConfig(dataEntity, metaNumSufFix, emptyMap, defaultStructType, bosObjectForMap)
  StructTypeIETempHelper.addTemplate(dataEntity) → 添加 IE 模板
  StructClassHelper.creatBizRule(dataEntity, ...) → 创建业务规则
  批量保存: bizRule / bizRuleLibrary / opBizRuleSet
  operationResult.setMessage("系统已在...下新增了二级菜单:{typeName}...")
  operationResult.setShowMessage(true)
    ↓
StructTypeEditPlugin.afterDoOperation
  operationResult.isSuccess() && operateKey="save"
    · parentView.showSuccessNotification(operationResult.getMessage())
    · sendFormAction(parentView)
    · close()
```

---

## 二、核心业务流转：改名（chgname）

```
用户在列表页选中一条记录，点击"变更名称"
    ↓
StructTypeListPlugin.beforeItemClick(key="chgname")
  R-2: rows.size() == 0 → 提示"请选择一条数据" → cancel
  R-2: rows.size() > 1  → 提示"只能选中一行" → cancel
  R-3: enable != "1" → 提示"只有可用状态才能变更名称" → cancel
  通过所有校验 → 不拦截
    ↓
StructTypeListPlugin.afterDoOperation(key="chgname")
  打开子表单 "haos_structtypenamechg"（ShowType.Modal）
  setCustomParam("pkid", primaryKeyValue)
  setCloseCallBack("haos_structtypenamechg")
    ↓
用户在弹窗中填写新名称并确认
    ↓
StructTypeChgNameOp.afterExecuteOperationTransaction
  遍历 dataEntities:
    localeString = dataEntity.getLocaleString("name") → 多语言名称
    OtherStructTypeService.getChangeNameMap() → 获取名称映射
    localeString.forEach → 填充多语言值
    StructClassHelper.changeMetaName(numberPrefix, localeValue) → 更新元数据名
    StructClassHelper.chgMenuName(id, nameValuesForMap) → 更新菜单名（多语言）
    ↓
StructTypeListPlugin.closedCallBack("haos_structtypenamechg")
  returnData.get("success") = true → showSuccessNotification
  returnData.get("success") = false → showTipNotification
```

---

## 三、核心业务流转：删除（tbldeleteallrel）

```
用户在列表页选中记录，点击"删除关联"
    ↓
StructTypeListPlugin.beforeItemClick(key="tbldeleteallrel")
  evt.setCancel(true) → 拦截默认行为
  收集选中行号: rowKey = rows.stream().map(r -> rowKey+1)...joining(",")
  new ConfirmCallBackListener("haos_structtype", this)
  showConfirm("删除选中的第X行记录后将无法恢复，确定要删除吗？", OKCancel, confirmListener)
    ↓
用户点击"确定"
    ↓
StructTypeListPlugin.confirmCallBack("haos_structtype", MessageBoxResult.Yes)
  invokeOperation("deleteallrel")
  invokeOperation("refresh")
    ↓
StructTypeDeleteDonothingOp.onAddValidators
  args.addValidator(new StructTypeDeleteValidator())
    ↓
StructTypeDeleteDonothingOp.beforeExecuteOperationTransaction
  TX.requiresNew() 新事务：
    getMetaDataByNum(metaNum) → 查关联元数据
    getMetaMobileBillByNum(baseDataFieldId) → 查移动端元数据
    StructClassHelper.filterMetaData(metaMobileBill, metaMobileNumber)
    getMetaDataByPath(inheritPathList) → 查子集元数据
    StructClassHelper.filterMetaData(metaDatumByPath, metaNum)
    遍历 metaMobileNumber → StructClassHelper.deleteMetaData(metaId)
    遍历 metaNum → StructClassHelper.deleteMetaData(metaId)
    DeleteServiceHelper.delete("bos_entityobject", metaNum)
    catch → rollback → KDBizException
  加载 AppMetadata → 找菜单 ID → removeIf → AppMetaServiceHelper.save()
  BizRuleLibraryEnum 枚举 → 删 bizRule/bizRuleLibrary/opBizRuleSet（保留 haos_structtype 自身）
```

---

## 四、核心业务流转：禁用（disable）

```
用户选中记录，执行禁用
    ↓
StructTypeDisableOp.beginOperationTransaction
  OtherAdminOrgService.getOtherAdminOrgData(otClassifyId, "enable")
    → 查关联 haos_adminorg 记录
  adminOrgDatum.set("enable", "0") 逐条设禁用
  OtherAdminOrgService.getStructureDataById(otClassifyId, "enable")
    → 查关联 haos_structure 记录
  structureDatum.set("enable", "0") 逐条设禁用
  saveOtherAdminOrgData(adminOrgData) → 批量保存 adminorg
  saveStructureData(structureData) → 批量保存 structure
```

---

## 五、编辑视图完整生命周期

```
preOpenForm
  ADDNEW → setCaption("新增架构类型")
  EDIT → 查名称 → setCaption("架构类型-{name}")

afterCreateNewData（仅新建时）
  填 org 字段（customParam 或权限查询）

beforeBindData（仅 EDIT 时）
  查 enable 字段 → 控制 effdt 可编辑性（R-1）
  StructTypeHelper.setTips(view, "haos_structtype", ["name","effdt"])

afterDoOperation（save 成功）
  无 message → 显示"保存成功" → sendFormAction → close
  有 message（新建成功）→ 显示 message → sendFormAction → close
```
