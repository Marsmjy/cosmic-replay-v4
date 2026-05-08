# ISV 实战 case · hbpm_positiontype

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（从 deep_resolve 推典型 case）
> **数据源**: `_deep_resolve_index.json` · `rules_chain_all.json::opKeys`
> **生成**: polish_form_scene_v2.py（v5.1 render_09）

## 典型 ISV 定制 case（按可继承类聚合）

### Case 1：扩展 `HRBaseDataTplEdit` (opKey=`save`)

**业务背景**：标品 `HRBaseDataTplEdit` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hr.hbp.formplugin.web.template` 包
2. 写 ISV 子类继承 `HRBaseDataTplEdit`：
```java
public class IsvCustomHRBaseDataTplEdit extends HRBaseDataTplEdit {
    @Override
    public void afterBindData(...) {
        super.afterBindData(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=BILL_FORM
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

### Case 2：扩展 `HRHiesButtonSwitchPlugin` (opKey=`save`)

**业务背景**：标品 `HRHiesButtonSwitchPlugin` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hr.hbp.formplugin.web.template` 包
2. 写 ISV 子类继承 `HRHiesButtonSwitchPlugin`：
```java
public class IsvCustomHRHiesButtonSwitchPlugin extends HRHiesButtonSwitchPlugin {
    @Override
    public void afterBindData(...) {
        super.afterBindData(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=BILL_FORM
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

### Case 3：扩展 `PositionBasedataEdit` (opKey=`save`)

**业务背景**：标品 `PositionBasedataEdit` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hrmp.hbpm.formplugin.web.basedata` 包
2. 写 ISV 子类继承 `PositionBasedataEdit`：
```java
public class IsvCustomPositionBasedataEdit extends PositionBasedataEdit {
    @Override
    public void afterBindData(...) {
        super.afterBindData(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=BILL_FORM
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

### Case 4：扩展 `HRBaseDataStatusOp` (opKey=`save`)

**业务背景**：标品 `HRBaseDataStatusOp` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hr.hbp.opplugin.web.config` 包
2. 写 ISV 子类继承 `HRBaseDataStatusOp`：
```java
public class IsvCustomHRBaseDataStatusOp extends HRBaseDataStatusOp {
    @Override
    public void onPreparePropertys(...) {
        super.onPreparePropertys(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=OPERATION
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

### Case 5：扩展 `HRBaseDataLogOp` (opKey=`save`)

**业务背景**：标品 `HRBaseDataLogOp` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hr.hbp.opplugin.web.config` 包
2. 写 ISV 子类继承 `HRBaseDataLogOp`：
```java
public class IsvCustomHRBaseDataLogOp extends HRBaseDataLogOp {
    @Override
    public void beforeExecuteOperationTransaction(...) {
        super.beforeExecuteOperationTransaction(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=OPERATION
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

### Case 6：扩展 `HRBaseDataEnableOp` (opKey=`save`)

**业务背景**：标品 `HRBaseDataEnableOp` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hr.hbp.opplugin.web.config` 包
2. 写 ISV 子类继承 `HRBaseDataEnableOp`：
```java
public class IsvCustomHRBaseDataEnableOp extends HRBaseDataEnableOp {
    @Override
    public void beforeExecuteOperationTransaction(...) {
        super.beforeExecuteOperationTransaction(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=OPERATION
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

### Case 7：扩展 `BaseDataBuOp` (opKey=`save`)

**业务背景**：标品 `BaseDataBuOp` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hrmp.hbpm.opplugin.web.basedata` 包
2. 写 ISV 子类继承 `BaseDataBuOp`：
```java
public class IsvCustomBaseDataBuOp extends BaseDataBuOp {
    @Override
    public void onAddValidators(...) {
        super.onAddValidators(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=OPERATION
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

### Case 8：扩展 `HRBaseOriginalOp` (opKey=`save`)

**业务背景**：标品 `HRBaseOriginalOp` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hr.hbp.opplugin.web.config` 包
2. 写 ISV 子类继承 `HRBaseOriginalOp`：
```java
public class IsvCustomHRBaseOriginalOp extends HRBaseOriginalOp {
    @Override
    public void onPreparePropertys(...) {
        super.onPreparePropertys(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=OPERATION
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

## 典型 opKey 业务边界 case

### opKey `importdata_hr` · 导入数据

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `show_import_record_hr` · 查看导入记录

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_list_hr` · 按列表导出

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_impttpl_hr` · 按导入模板导出

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_expttpl_hr` · 按导出模板导出

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `show_export_record_hr` · 查看导出记录

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

## 通用 ISV 套路提醒

- **必调 super**：所有 lifecycle 方法 override 必须先调 super · 保留标品行为
- **不动标品 form**：要加字段 → 建 ISV 扩展元数据 + `_inherits` 引用主表
- **注册插件 targetType**：BILL_FORM / LIST_FORM / OPERATION 之一（大写枚举·区分大小写）
- **测试**：跑标品测试用例 + ISV 自有用例·验证 super 行为完好
