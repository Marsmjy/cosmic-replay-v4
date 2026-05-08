# ISV 实战 case · wtpm_oncecard

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（从 deep_resolve 推典型 case）
> **数据源**: `_deep_resolve_index.json` · `rules_chain_all.json::opKeys`
> **生成**: polish_form_scene_v2.py（v5.1 render_09）

## 典型 ISV 定制 case（按可继承类聚合）

### Case 1：扩展 `HRCertCheckEdit` (opKey=`save`)

**业务背景**：标品 `HRCertCheckEdit` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hr.hbp.formplugin.web.cert` 包
2. 写 ISV 子类继承 `HRCertCheckEdit`：
```java
public class IsvCustomHRCertCheckEdit extends HRCertCheckEdit {
    @Override
    public void preOpenForm(...) {
        super.preOpenForm(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=BILL_FORM
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

### Case 2：扩展 `HRBaseUeEdit` (opKey=`save`)

**业务背景**：标品 `HRBaseUeEdit` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hr.hbp.formplugin.web.template` 包
2. 写 ISV 子类继承 `HRBaseUeEdit`：
```java
public class IsvCustomHRBaseUeEdit extends HRBaseUeEdit {
    @Override
    public void preOpenForm(...) {
        super.preOpenForm(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=BILL_FORM
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

### Case 3：扩展 `HRHiesButtonSwitchPlugin` (opKey=`save`)

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

### Case 4：扩展 `HRCertCheckList` (opKey=`save`)

**业务背景**：标品 `HRCertCheckList` 处理 `save` 操作 · ISV 想加自定义校验/写表/通知

**实现路径**：
1. ISV 工程 `pom.xml` 引 `kd.hr.hbp.formplugin.web.cert` 包
2. 写 ISV 子类继承 `HRCertCheckList`：
```java
public class IsvCustomHRCertCheckList extends HRCertCheckList {
    @Override
    public void preOpenForm(...) {
        super.preOpenForm(...);
        // 你的 ISV 逻辑
    }
}
```
3. 在 BOS 设计器注册 ISV 插件 · targetType=BILL_FORM
4. 单测：触发 opKey `save` · 验证 ISV 逻辑 + super 行为都跑

## 典型 opKey 业务边界 case

### opKey `export_from_list_hr` · 按列表导出

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `show_export_record_hr` · 查看导出记录

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `exportlistbyselectfields` · 导出数据（按列表）

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `exportdetails` · 查看导出结果

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

## 通用 ISV 套路提醒

- **必调 super**：所有 lifecycle 方法 override 必须先调 super · 保留标品行为
- **不动标品 form**：要加字段 → 建 ISV 扩展元数据 + `_inherits` 引用主表
- **注册插件 targetType**：BILL_FORM / LIST_FORM / OPERATION 之一（大写枚举·区分大小写）
- **测试**：跑标品测试用例 + ISV 自有用例·验证 super 行为完好
