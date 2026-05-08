# 推荐定制方案 · 协作类型（hbpm_reportcoreltype）

> **状态**: 🟢 基于反编译 3 类实证 + PR 铁律 + platform_rules 11 条
> **confidence**: real_deploy（基础资料场景特性 · 3 个 CS）
> **最后更新**: 2026-04-27

---

## CS-01 · 给 hbpm_reportcoreltype 加自定义业务字段

### 需求
业务方说："要给协作类型加上'应用场景'标签（正式汇报/临时协作/跨域协作），方便在汇报关系中按场景过滤协作类型。"

### 推荐方案
- **扩展对象**：`hbpm_reportcoreltype` 主实体
- **扩展点**：`modifyMeta(op=add field)`
- **风险**：低（基础资料 · 加字段安全）
- **注意**：本场景有子表 `t_hbpm_orgteamtype`，但 ISV 扩展字段应加在主实体，不建议随意扩展子表

### 调用链
```
Step 1: getFormSchema(hbpm_reportcoreltype)  // 查当前 27 字段清单 · 防重名
Step 2: modifyMeta({
  formId: "hbpm_reportcoreltype",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hbpm_reportcoreltype",   // 主实体
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_applyscene",              // ⭐ ISV 前缀防覆盖
      name: {zh_CN: "应用场景", en_US: "Apply Scene"},
      mustInput: false,
      enum: [
        {value: "formal", label: "正式汇报"},
        {value: "temp", label: "临时协作"},
        {value: "cross", label: "跨域协作"}
      ]
    }
  }]
})
Step 3: getFormSchema(hbpm_reportcoreltype) // ⭐ 二次验证落库
```

### 踩坑
- ❌ 字段 key 不带 ISV 前缀 → 标品升级被覆盖（PR-001 配套规则）
- ❌ 字段 key > 24 字符 → 数据库列名截断
- ❌ 加完字段没在编辑弹窗布局配置 → 字段存在但 UI 不显示
- ❌ 给 issyspreset=true 的出厂数据设默认值 → 平台升级会覆盖
- ⚠ 若要扩展 t_hbpm_orgteamtype 子表字段，需指定 parentScope 为子实体名称；但非必要不扩展子表
- 💡 ISV 在 OP 层 onPreparePropertys 需声明 `${ISV_FLAG}_applyscene` 才能在事务内读取（PR-010）

### 关联 PR
- PR-001 · ISV 字段 key 必须带前缀
- PR-007 · issyspreset=true 出厂数据不影响加新字段
- PR-010 · OP 层读字段需在 onPreparePropertys 声明

---

## CS-02 · 删除/禁用前置引用检查（汇报关系场景反向引用）

### 需求
业务方说："某协作类型已被汇报关系中大量配置引用，不能随意禁用，否则下游汇报关系配置会失效。能加保护吗？"

### 推荐方案
- **扩展对象**：`hbpm_reportcoreltype`
- **扩展点**：`onAddValidators@delete` + `onAddValidators@disable`
- **实现模式**：并列挂 OP 插件（**禁止继承 PositionBasedataEdit、PositionnBaseDataOrderPlugin、BaseDataBuOp(hbpm域)、AbsOrgBaseOp** · PR-001）
- **父类**：`HRDataBaseOp`

### 扩展入口坐标
- 绑定表单：`hbpm_reportcoreltype`
- 绑定操作：`delete` · `disable`
- 推荐父类：**`HRDataBaseOp`**（白名单合规）
- 关键重写方法：`onAddValidators(AddValidatorsEventArgs args)`

### MulBasedataField 子表反向查询注意
本场景 `orgteamtype` 是 MulBasedataField，物理存储在子表 `t_hbpm_orgteamtype`。
若需反向查"哪些汇报关系场景引用了某协作类型"，走子表路径查：
```java
// MulBasedataField 反向查（通过 .fbasedataid 子表路径）
QFilter qf = new QFilter("reportcoreltype.fbasedataid", "=", reportCoreltypeId);
// 不是直字段查（reportcoreltype 不是普通 BaseDataField）
```

### 代码框架

**TdkwReportCoreltypeRefValidator.java**（父类：AbstractValidator）：
```java
package com.kingdee.${ISV_FLAG}.hbpm.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 删除/禁用 hbpm_reportcoreltype 前的反向引用校验
 * 本场景是 BaseFormModel（非 HisModel）· 查询时不需加 iscurrentversion
 * ⚠ 禁止继承 PositionBasedataEdit、PositionnBaseDataOrderPlugin（PR-001）
 * ⚠ 禁止继承 BaseDataBuOp（hbpm 域，kd.hrmp.hbpm.opplugin.web.basedata）（PR-001）
 * ⚠ 禁止继承 AbsOrgBaseOp（forbidden · 不在 HR SDK 白名单）
 */
public class TdkwReportCoreltypeRefValidator extends AbstractValidator {

    // 下游汇报关系场景（具体 formNumber 按实际部署确认）
    private static final String REPORT_FORM = "hbpm_reportrelation";
    private static final String REF_FIELD = "reportcoreltype";

    @Override
    public void validate() {
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) return;

        HRBaseServiceHelper reportHelper = new HRBaseServiceHelper(REPORT_FORM);

        for (ExtendedDataEntity ext : dataEntities) {
            DynamicObject dataEntity = ext.getDataEntity();

            // 出厂数据额外保护
            if (dataEntity.getBoolean("issyspreset")) {
                this.addErrorMessage(ext, "系统预置协作类型不可删除 (issyspreset=true)");
                continue;
            }

            long id = dataEntity.getLong("id");
            // BaseFormModel：直接用 id 查，不需要 iscurrentversion 过滤
            QFilter qf = new QFilter(REF_FIELD, "=", id)
                .and(new QFilter("enable", "=", "1"));

            if (reportHelper.isExists(qf)) {
                this.addErrorMessage(ext, String.format(
                    "协作类型 [%s] 已被汇报关系场景引用，不可删除，建议改用禁用",
                    dataEntity.getString("name")
                ));
            }
        }
    }
}
```

**TdkwReportCoreltypeRefCheckOp.java**（父类：HRDataBaseOp）：
```java
package com.kingdee.${ISV_FLAG}.hbpm.opplugin.web;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * ISV 并列挂 OP 插件 · 拦 delete / disable 操作
 * ⚠ 禁止继承 PositionBasedataEdit（PR-001）
 * ⚠ 禁止继承 PositionnBaseDataOrderPlugin（PR-001 · ListPlugin 类型也禁继承）
 * ⚠ 禁止继承 BaseDataBuOp（hbpm 域，kd.hrmp.hbpm.opplugin.web.basedata）（PR-001）
 * ⚠ 禁止继承 AbsOrgBaseOp（forbidden）
 */
public class TdkwReportCoreltypeRefCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator) new TdkwReportCoreltypeRefValidator());
    }
}
```

### 踩坑
- ❌ 继承 `PositionBasedataEdit` → PR-001 · 禁继承场景专属 FormPlugin
- ❌ 继承 `PositionnBaseDataOrderPlugin` → PR-001 · 禁继承场景专属 ListPlugin
- ❌ 继承 `BaseDataBuOp`（hbpm 域）→ PR-001 · 禁继承场景专属 OP（注意 haos 域和 hbpm 域两个同名类都禁继承）
- ❌ 继承 `AbsOrgBaseOp` → 不在 HR SDK 白名单（forbidden）
- ❌ 加 `iscurrentversion=true` 过滤 → BaseFormModel 无此字段，加了报错
- ❌ 在 afterExecuteOperationTransaction 做校验 → 事务已提交无法回滚（PR-010）
- 💡 本场景是 BaseFormModel，反向查询直接用 id，不用 boid（PR-009 不适用）
- 💡 orgteamtype 是 MulBasedataField，其子表行不是被引用点；引用点是主表 id

### 关联 PR
- PR-001 · ISV 并列挂 · 禁继承 PositionBasedataEdit / PositionnBaseDataOrderPlugin / BaseDataBuOp(hbpm) / AbsOrgBaseOp
- PR-009 · BaseFormModel 不用 boid（PR-009 不适用场景，但作为对比知识点）
- PR-010 · onAddValidators 阶段注册

---

## CS-03 · ctrlstrategy 字段联动（afterBindData / propertyChanged 模式）

### 需求
业务方说："协作类型中 ctrlstrategy（控制策略）变更时，想追加自定义校验：若选择某特定策略值，需弹提示引导用户配置管理组织。"

### 推荐方案
- **扩展对象**：`hbpm_reportcoreltype` 表单插件层
- **扩展点**：`afterBindData`（初始化联动）+ `propertyChanged`（用户变更联动）
- **实现模式**：并列挂 FormPlugin（**禁继承 PositionBasedataEdit** · PR-001）
- **风险**：低（UI 层联动 · 不动数据）
- **父类选择**：`HRDataBaseEdit`（白名单合规）

### 关键设计原则
1. **afterBindData 做初始化判断**（参考 PositionBasedataEdit 的 issyspreset 检查模式）
2. **propertyChanged 做用户交互联动**（需 beginInit/endInit 防死循环 · PR-004）
3. **不调用 PositionBasedataEdit 的任何方法**（并列挂不是继承）
4. **issyspreset=true 时跳过所有联动**（与 PositionBasedataEdit 的保护逻辑兼容）

### 代码框架

```java
package com.kingdee.${ISV_FLAG}.hbpm.formplugin.web;

import java.util.EventObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

/**
 * hbpm_reportcoreltype ISV ctrlstrategy 字段联动插件
 * RowKey 排在 PositionBasedataEdit(#9) 之后
 * ⚠ 禁止继承 PositionBasedataEdit（kd.hrmp.hbpm.formplugin.web.basedata）（PR-001）
 * ⚠ 禁止继承 PositionnBaseDataOrderPlugin（PR-001）
 * ⚠ 禁止继承 BaseDataBuOp（hbpm 域）（PR-001）
 * ⚠ 禁止继承 AbsOrgBaseOp（forbidden）
 */
public class TdkwReportCoreltypeCtrlPlugin extends HRDataBaseEdit {

    private static final String CTRLSTRATEGY = "ctrlstrategy";
    private static final String SCOPE_VALUE = "8";  // 同分配范围策略值

    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        // issyspreset=true 时跳过：尊重 PositionBasedataEdit 的只读保护
        DynamicObject entity = this.getView().getModel().getDataEntity();
        if (entity.getBoolean("issyspreset")) return;

        String strategy = entity.getString(CTRLSTRATEGY);
        this.checkCtrlStrategy(strategy);
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        if (!CTRLSTRATEGY.equals(e.getProperty().getName())) return;

        String newVal = (String) this.getModel().getValue(CTRLSTRATEGY);
        // PR-004：若需 setValue 同字段，必须 beginInit/endInit 防死循环
        this.checkCtrlStrategy(newVal);
    }

    private void checkCtrlStrategy(String strategy) {
        if (SCOPE_VALUE.equals(strategy)) {
            // ISV 自定义提示：选择同分配范围时需配置管理组织
            this.getView().showTipNotification(
                "控制策略为'同分配范围'时，请确认管理组织（org 字段）已正确配置");
        }
    }
}
```

### 踩坑
- ❌ 继承 `PositionBasedataEdit` → PR-001（场景专属 FormPlugin 禁继承）
- ❌ 继承 `PositionnBaseDataOrderPlugin` → PR-001（场景专属 ListPlugin 禁继承）
- ❌ 继承 `BaseDataBuOp`（hbpm 域 `kd.hrmp.hbpm.opplugin.web.basedata`）→ PR-001
- ❌ propertyChanged 里 setValue 不加 beginInit/endInit → 死循环（PR-004）
- ❌ 不检查 issyspreset → 预置数据 VIEW 状态下 setValue 无效且有 warn 日志
- ❌ 把 hbpm 域 BaseDataBuOp 当 haos 域使用 → 两个同名类·不同域·注册到不同场景

### 关联 PR
- PR-001 · 不继承 PositionBasedataEdit / PositionnBaseDataOrderPlugin / BaseDataBuOp(hbpm) / AbsOrgBaseOp
- PR-003 · FormPlugin 用 getModel().setValue（不用 entity.set）
- PR-004 · setValue 防死循环（beginInit/endInit）

---

## CS 关联 Pattern 速查

| CS | 关联 Pattern | 作用 |
|---|---|---|
| CS-01 | `add_field_extension` | 加 ISV 字段标准模式（主实体）|
| CS-02 | `add_unique_validation` + 反向引用查询（BaseFormModel · 直接用 id）| 删除/禁用前置校验 |
| CS-03 | `override_plugin_behavior`（并列挂版）| ctrlstrategy 字段联动（afterBindData + propertyChanged）|

---

## BEC 反指引（CS 说明）

本场景标品**没有发任何 BEC 事件**（grep 反编译文件 triggerEventSubscribe = 0 处命中）。

ISV 在 `hbpm_reportcoreltype` 不推荐自建 BEC 发布方：
1. 协作类型是字典数据，变更频率极低（一年改几次），没有异步分发必要
2. 下游汇报关系场景如需字典数据，直接查 `t_hbpm_reportcoreltype` 主表 + `t_hbpm_orgteamtype` 子表即可
3. 如下游确需感知协作类型变更，应在汇报关系配置层挂 BEC，而非在字典层

如确有外部系统订阅需求，走 PR-011（苍穹 BEC · afterExecuteOperationTransaction 阶段发布）。
