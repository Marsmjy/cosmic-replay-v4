# 参考案例 · 岗位模板（hbpm_positiontpl）

> **状态**: 🟢 基于反编译 4 类实证
> **confidence**: verified

---

## Case-01 · 新建时提示"所在权限下组织体系管理组织均未启用模板库，无法新增。"

### 场景

在岗位模板列表点击新建，弹出提示"...均未启用模板库，无法新增。"，无法进入新建页面。

### 根因

`PositionTplListPlugin.beforeDoOperation(operateKey=new)` 通过 `validateOrgExistOpenTpl()` 检查权限下所有 BU（管理组织）的 `openpositiontpl` 系统参数是否有任意一个为 true。若全部为 false 则阻止新建。

### 处理

在组织管理 → 系统参数配置中，找到对应管理组织，开启"岗位模板库"开关（`openpositiontpl=true`）。

---

## Case-02 · 点击"变更"时提示"...未启用模板库，不可变更数据。"

### 场景

打开岗位模板详情，点击"变更"按钮，弹出提示无法变更。

### 根因

`PositionTplEditPlugin.beforeDoOperation(operateKey=modify)` 检查当前 org 对应 BU 的 `openpositiontpl` 参数，若为 false 则阻止。

### 处理

在系统参数中为当前模板的管理组织开启岗位模板库开关。

---

## Case-03 · 保存后下游岗位数据未同步

### 场景

修改岗位模板后保存成功，但 `hbpm_positionhr` 中对应的岗位数据字段未更新。

### 根因

`PositionTplSaveOp.endOperationTransaction` 调用 `PositionTplChangeSyncPosService.syncUpdatePosition()` 进行差量同步。若 `oldDynMap` 快照加载失败或同步服务异常，下游数据不更新。

### 诊断

查系统日志中 `PositionTplChangeSyncPosService` 相关异常。

---

## Case-04 · 设置适用范围时提示"禁用数据不可设置适用组织范围。"

### 场景

在列表选中多条岗位模板，点击"设置适用范围"，提示禁用数据不可操作。

### 根因

`PositionTplListPlugin.beforeItemClick(applicationscope)` 通过 `PositionTplRepository.queryDisablePositionTplByIds()` 检查选中数据，只要有一条是禁用状态就阻止。

### 处理

取消选中禁用的岗位模板，只选启用状态的模板再操作。

---

## Case-05 · ISV 加的字段在 OP 层读取为 null

### 场景

ISV 通过 modifyMeta 给 `hbpm_positiontpl` 加了自定义字段 `${ISV_FLAG}_positiontplcate`，在 OP 插件的 `afterExecuteOperationTransaction` 中读取该字段值为 null。

### 根因

苍穹 OP 插件默认不加载 ISV 字段。必须在 `onPreparePropertys` 中显式声明（PR-010）。

### 处理

```java
@Override
public void onPreparePropertys(PreparePropertysEventArgs e) {
    e.getFieldKeys().add("${ISV_FLAG}_positiontplcate");
}
```
