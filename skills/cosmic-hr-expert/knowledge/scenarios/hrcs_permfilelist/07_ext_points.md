# 扩展点全图 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 基于 `_auto_plugin_registry.md`（4 plugins）+ 反编译 `PermfilesListPlugin`/`HRAdminStrictPlugin` + `_auto_operations.md`（23 opKey）
> **confidence**: verified
> **数据源**: 2026-04-28

---

## ⭐ 一、标品插件清单（4 个）

| # | 类别 | ClassName | 父类 | Jar | 生命周期方法 |
|---|---|---|---|---|---|
| 1 | editable | `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin` | `HRDynamicFormBasePlugin` | hrmp-hrcs-formplugin-1.0.jar | preOpenForm |
| 2 | editable | `kd.hr.hrcs.formplugin.web.perm.permfile.PermfilesListPlugin` | `HRStandardTreeList` | hrmp-hrcs-formplugin-1.0.jar | beforeBindData / beforeItemClick / beforeDoOperation / afterDoOperation / itemClick / closedCallBack / registerListener |
| 3 | editable | `kd.hr.hbp.formplugin.web.query.QueryListPlugin` | `AbstractListPlugin` | hrmp-hbp-formplugin-1.0.jar | afterBindData / beforeDoOperation |
| 4 | editable | `kd.hr.hbss.opplugin.web.PermFilesSaveOp` | -（jar 缺失）| - | OP（save/enable/delete/disable_new opKey） |

⚠️ **本场景 4 个标品插件全部不可继承**（PR-001）。

---

## 二、按生命周期方法分组（执行顺序 · 反编译实证）

### 2.1 preOpenForm（1 个）
- HRAdminStrictPlugin（HR 域管理员准入闸 · 失败直接 setCancel · 后续插件不再执行）

### 2.2 beforeBindData（1 个）
- PermfilesListPlugin（updateSearch · 设搜索框文案）

### 2.3 beforeItemClick（1 个）
- PermfilesListPlugin（工具栏按钮选中行数前置校验）

### 2.4 beforeDoOperation（2 个 · RowKey 顺序执行）
1. PermfilesListPlugin（19 工具栏按钮分发 · bar_new 拦截）
2. QueryListPlugin（hrmp-hbp 多实体查询）

⚠️ ISV 插件加在这层时 · 看 `PR-002 RowKey` 决定排序：
- 想拦截 PermfilesListPlugin 的 bar_new → ISV 排在标品 1 之前
- 想在 PermfilesListPlugin 处理之后追加逻辑 → 排在标品 2 之后

### 2.5 afterDoOperation（1 个）
- PermfilesListPlugin（enable/disable 后置消息 + billList 刷新）

### 2.6 itemClick（1 个）
- PermfilesListPlugin（btn_clearcache / refresh）

### 2.7 closedCallBack（1 个）
- PermfilesListPlugin（4 actionId 分支：NEW_PERMFILE / syncPermFilesTask / exportUrl / confirmcallback_save）

### 2.8 registerListener（1 个）
- PermfilesListPlugin（注册 TreeView TreeNodeQueryListener）

### 2.9 afterBindData（1 个）
- QueryListPlugin

---

## 三、ISV 最常覆盖的 Top 5 扩展点

### 3.1 #1 hrcs_userpermfile · save opKey · onAddValidators

**最常用** —— ISV 几乎所有"档案保存前业务校验"都挂这里。

```java
public class IsvXxxOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new XxxValidator());
    }
}
```

涉及：CS-01 / CS-03 / CS-04

### 3.2 #2 hrcs_userpermfile · save/enable/disable/delete · afterExecuteOperationTransaction

**第二常用** —— 跨模块通知 / 缓存维护 / ISV 自定义日志写入。

```java
@Override
public void afterExecuteOperationTransaction(AfterOperationArgs args) {
    super.afterExecuteOperationTransaction(args);
    // 事务已提交 · 安全发 BEC / 写入下游表
}
```

涉及：CS-05（BEC 发布）

### 3.3 #3 hrcs_permfilelist · setFilter

**第三常用** —— ISV 列表过滤定制。**必须先 super** · 让标品 admingroup level 限制先生效。

```java
public class IsvFilterListPlugin extends HRDataBaseList {
    @Override
    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);  // ⭐
        // 追加 ISV QFilter
    }
}
```

涉及：CS-06

### 3.4 #4 hrcs_permfilelist · beforeDoOperation（拦截工具栏 opKey）

**用于二次确认 / 自定义流程注入** —— 比如 disable 前弹"将级联 N 条角色"提示。

```java
@Override
public void beforeDoOperation(BeforeDoOperationEventArgs args) {
    super.beforeDoOperation(args);
    FormOperate op = (FormOperate) args.getSource();
    if ("disable".equals(op.getOperateKey())) {
        // 二次确认 / 自定义校验 · args.setCancel(true) 阻断
    }
}
```

涉及：CS-03（disable 子方案）

### 3.5 #5 hrcs_userpermfile · propertyChanged（编辑表单字段联动）

**用于字段联动** —— 选 user 自动带 org 等。

```java
@Override
public void propertyChanged(PropertyChangedArgs e) {
    super.propertyChanged(e);
    if ("user".equals(e.getProperty().getName())) {
        getModel().beginInit();
        getModel().setValue("org", computedOrgId);
        getModel().endInit();
        getView().updateView("org");
    }
}
```

涉及：CS-02

---

## 四、扩展点的 ❌ 黑区（不要碰的位置）

| 位置 | 原因 |
|---|---|
| 继承 `PermfilesListPlugin` | 场景专属类（PR-001）· 标品升级会破坏 |
| 继承 `PermFilesSaveOp` | 同上 · 且 jar 缺失无源码可调试 |
| 继承 `HRAdminStrictPlugin` | 11 hrcs 共用准入闸 · 改一处影响全 hrcs · 配置即可 |
| 修改 `ID_ROOTNODE` GUID | 写死的常量 · 改了树根错乱 |
| 在 `endOperationTransaction` 发 BEC | 事务可能未最终提交 · 脏事件（PR-010） |
| 在 OP 里 `getModel().setValue` | OP 没 model · NullPointerException（PR-003） |
| 在 FormPlugin 里 `entity.set` 改字段 | model 不知道 · UI 不刷新（PR-003） |
| 不带 `iscurrentversion=true` 查 hrpi_* | 查到所有版本 · 数据错乱（PR-008） |
| `setFilter` 直接 `clear()` 后 add | 抹掉标品 admingroup 限制 · 越权问题 |
| 自接 RabbitMQ/Kafka | 必须走 BEC（PR-011） |

---

## 五、ISV 扩展模板代码（4 类标准 skeleton）

### 5.1 ISV ListPlugin 模板

```java
package isv.cosmic.hr.permfile;

import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.hr.hbp.formplugin.web.HRDataBaseList;  // ✅ 白名单

public class IsvPermfileListPluginTemplate extends HRDataBaseList {

    @Override
    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        // 追加 ISV QFilter
    }

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        // 拦截特定 opKey · args.setCancel(true) 阻断
    }
}
```

### 5.2 ISV FormPlugin 模板（编辑表单 hrcs_userpermfile）

```java
package isv.cosmic.hr.permfile;

import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;  // ✅ 白名单

public class IsvPermfileEditPluginTemplate extends HRDataBaseEdit {

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        // PR-004: getModel().beginInit() + setValue + endInit + updateView
    }
}
```

### 5.3 ISV OP（带 Validator）模板

```java
package isv.cosmic.hr.permfile;

import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;  // ✅ 白名单

public class IsvPermfileOpTemplate extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {  // PR-010
        super.onAddValidators(args);
        args.addValidator(new IsvValidator());
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        // PR-011: 在此阶段发 BEC 安全（事务已提交）
    }

    static class IsvValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (kd.bos.entity.ExtendedDataEntity row : this.getDataEntities()) {
                // addErrorMessage(row, "...")
            }
        }
    }
}
```

### 5.4 ISV BEC 订阅方模板

```java
package isv.cosmic.hr.permfile;

import kd.bos.bec.api.IEventServicePlugin;     // ✅ @SdkPublic
import kd.bos.bec.model.KDBizEvent;            // ✅ @SdkPublic

public class IsvPermfileEventListenerTemplate implements IEventServicePlugin {
    @Override
    public void handleEvent(KDBizEvent event) {
        String evtNum = event.getEventNumber();
        java.util.Map<String, Object> vars = event.getVariables();
        // 在【开发平台】→【业务事件订阅】挂载到指定 eventNumber
    }
}
```

---

## 六、扩展点路径决策树

```
ISV 需求是什么？
├── 加自定义字段     → modifyMeta hrcs_userpermfile + ISV 前缀（CS-01）
├── 字段联动        → ISV FormPlugin 挂 hrcs_userpermfile · propertyChanged（CS-02）
├── save 前校验     → ISV OP 挂 hrcs_userpermfile.save · onAddValidators（CS-03）
├── disable 前提醒  → ISV ListPlugin 挂 hrcs_permfilelist · beforeDoOperation（CS-03）
├── delete 查引用   → ISV OP 挂 hrcs_userpermfile.delete · onAddValidators（CS-04）
├── 跨模块通知      → ISV OP 挂 disable.afterExecute · 发 BEC（CS-05 + PR-011）
├── 列表过滤定制    → ISV ListPlugin 挂 hrcs_permfilelist · setFilter（CS-06）
└── 批量分组扩展    → ISV OP 挂 hrcs_permfilegrpmember.save（CS-07）
```

---

## 七、扩展点维护清单（项目交付时检查）

| 检查项 | 状态 |
|---|---|
| ISV 插件类名前缀 `isv.<client>.<module>.*` ✅ | ☐ |
| 父类是白名单 9 类之一 · 不继承场景专属类 | ☐ |
| OP 用 entity.set / FormPlugin 用 getModel().setValue | ☐ |
| ISV 字段都设 isvAccessable=true | ☐ |
| 命名前缀 `2isv_` / `1<企业>_`（与平台冲突域隔离） | ☐ |
| 反编译白名单审核（cosmic_hr_sdk_whitelist_audit.md） | ☐ |
| BEC eventNumber 在开发平台已配置 | ☐ |
| 单元测试覆盖：正常 + 边界 + 异常 | ☐ |
| 灰度发布：先在 UAT 验证 1 周 | ☐ |
