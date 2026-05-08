# 推荐定制方案 · 岗位模板类型（hbpm_positiontpltype）

> **状态**: 🟢 基于反编译 2 类实证 + PR 铁律 + platform_rules 11 条
> **confidence**: real_deploy（基础资料场景特性 · 3 个 CS）
> **最后更新**: 2026-04-27

---

## CS-01 · 给 hbpm_positiontpltype 加自定义业务字段

### 需求
业务方说："要给岗位模板类型加上'适用职级范围'标签（初级/中级/高级），方便按职级过滤适用的岗位模板。"

### 推荐方案
- **扩展对象**：`hbpm_positiontpltype` 主实体
- **扩展点**：`modifyMeta(op=add field)`
- **风险**：低

### 调用链
```
Step 1: getFormSchema(hbpm_positiontpltype)  // 查当前 26 字段清单 · 防重名
Step 2: modifyMeta({
  formId: "hbpm_positiontpltype",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hbpm_positiontpltype",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_joblevel_range",    // ⭐ ISV 前缀
      name: {zh_CN: "适用职级范围"},
      mustInput: false,
      enum: [
        {value: "junior", label: "初级"},
        {value: "mid", label: "中级"},
        {value: "senior", label: "高级"}
      ]
    }
  }]
})
Step 3: getFormSchema(hbpm_positiontpltype) // 验证落库
```

### 踩坑
- ❌ 字段 key 不带 ISV 前缀 → 标品升级被覆盖
- ❌ 加完字段没在编辑弹窗布局中配置 → 字段存在但 UI 不显示
- ⚠ 本场景无子表 · 加字段只能在主实体
- 💡 加字段后若需在 OP 层读取，必须在 onPreparePropertys 显式声明（PR-010）

### 关联 PR
- PR-001 · ISV 字段 key 必须带前缀
- PR-007 · issyspreset=true 出厂数据不影响加新字段

---

## CS-02 · 禁用/删除前引用检查（hbpm_positiontpl 反向引用）

### 需求
业务方说："某类岗位模板类型已被多个岗位模板使用，直接禁用会导致下游岗位模板无法选择类型。能加保护吗？"

### 推荐方案
- **扩展对象**：`hbpm_positiontpltype`
- **扩展点**：`onAddValidators@delete` + `onAddValidators@disable`
- **实现模式**：并列挂 OP 插件（**禁止继承 PositionTplTypeSaveOp、PositionTplTypeEditPlugin、AbsOrgBaseOp** · PR-001）
- **父类**：`HRDataBaseOp`

### 代码框架

**TdkwPositionTplTypeRefValidator.java**（父类：AbstractValidator）：
```java
package com.kingdee.${ISV_FLAG}.hbpm.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 删除/禁用 hbpm_positiontpltype 前的反向引用校验
 * 本场景是 BaseFormModel · 不需要 iscurrentversion 过滤
 * ⚠ 禁止继承 PositionTplTypeSaveOp / PositionTplTypeEditPlugin / AbsOrgBaseOp（PR-001）
 */
public class TdkwPositionTplTypeRefValidator extends AbstractValidator {

    private static final String TPL_FORM = "hbpm_positiontpl";  // 岗位模板
    private static final String REF_FIELD = "positiontpltype";

    @Override
    public void validate() {
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) return;

        HRBaseServiceHelper tplHelper = new HRBaseServiceHelper(TPL_FORM);

        for (ExtendedDataEntity ext : dataEntities) {
            DynamicObject dataEntity = ext.getDataEntity();
            long id = dataEntity.getLong("id");
            // BaseFormModel · 直接用 id 查，不加 iscurrentversion
            QFilter qf = new QFilter(REF_FIELD, "=", id)
                .and(new QFilter("enable", "=", "1"));
            if (tplHelper.isExists(qf)) {
                this.addErrorMessage(ext, String.format(
                    "岗位模板类型 [%s] 已被岗位模板引用，不可禁用，请先修改对应岗位模板",
                    dataEntity.getString("name")
                ));
            }
        }
    }
}
```

**TdkwPositionTplTypeRefCheckOp.java**（父类：HRDataBaseOp）：
```java
package com.kingdee.${ISV_FLAG}.hbpm.opplugin.web;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * ⚠ 禁止继承 PositionTplTypeSaveOp（PR-001）
 * ⚠ 禁止继承 PositionTplTypeEditPlugin（PR-001）
 * ⚠ 禁止继承 AbsOrgBaseOp（forbidden）
 */
public class TdkwPositionTplTypeRefCheckOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator) new TdkwPositionTplTypeRefValidator());
    }
}
```

### 踩坑
- ❌ 继承 `PositionTplTypeSaveOp` → PR-001 · 场景专属 OP
- ❌ 继承 `PositionTplTypeEditPlugin` → PR-001 · 场景专属 FormPlugin
- ❌ 继承 `AbsOrgBaseOp` → forbidden（不在白名单）
- ❌ 加 iscurrentversion 过滤 → BaseFormModel 无此字段

### 关联 PR
- PR-001 · 并列挂 · 禁继承 PositionTplTypeSaveOp / PositionTplTypeEditPlugin / AbsOrgBaseOp
- PR-010 · onAddValidators 阶段注册

---

## CS-03 · ctrlstrategy 联动追加自定义校验

### 需求
业务方说："标品 BdCtrlStrtgyShowLogicPlugin 联动 createorg/org/useorg 可见性是平台默认，但我们想在选择'同分配范围'时追加校验。"

### 推荐方案
- **扩展点**：`propertyChanged@hbpm_positiontpltype`（FormPlugin 层）
- **实现模式**：并列挂 FormPlugin（`HRDataBaseEdit`，**禁继承 PositionTplTypeEditPlugin** · PR-001）
- **注意**：ISV 的 propertyChanged 排在标品 BdCtrlStrtgyShowLogicPlugin 之后（PR-002）

### 代码框架

```java
package com.kingdee.${ISV_FLAG}.hbpm.formplugin.web;

import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

/**
 * ⚠ 禁止继承 PositionTplTypeEditPlugin（PR-001）
 * ⚠ 禁止继承 AbsOrgBaseOp（forbidden）
 */
public class TdkwPositionTplTypeEditPlugin extends HRDataBaseEdit {

    private static final String CTRLSTRATEGY = "ctrlstrategy";
    private static final String ALLOCATION_SCOPE = "8";

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        if (!CTRLSTRATEGY.equals(e.getProperty().getName())) return;

        String newVal = (String) this.getModel().getValue(CTRLSTRATEGY);
        if (!ALLOCATION_SCOPE.equals(newVal)) return;

        // ISV 自定义校验逻辑
        // PR-004：若需 setValue 同字段，必须 beginInit/endInit
        this.getView().showTipNotification("选择'同分配范围'时，请确认管理组织配置合规");
    }
}
```

### 踩坑
- ❌ 继承 `PositionTplTypeEditPlugin` → PR-001
- ❌ propertyChanged 里 setValue 不加 beginInit/endInit → 死循环（PR-004）

### 关联 PR
- PR-001 · 不继承 PositionTplTypeEditPlugin
- PR-003 · FormPlugin 用 getModel().setValue
- PR-004 · setValue 防死循环

---

## BEC 反指引

本场景标品**没有发任何 BEC 事件**（反编译 triggerEventSubscribe = 0 处命中）。

岗位模板类型变更频率低（字典类），不推荐 ISV 自建 BEC 发布方。下游（hbpm_positiontpl）如需感知模板类型变更，直接查 t_hbpm_positiontpltype 表即可。
