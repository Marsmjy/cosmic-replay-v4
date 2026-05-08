# 推荐定制方案 · 岗位基础资料（hbpm_basedatalist）

> **状态**: 🟢 基于反编译 2 类实证 + PR 铁律 + platform_rules 11 条
> **confidence**: real_deploy（基础资料场景特性 · 3 个 CS）
> **结构**: 背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR

---

## CS-01 · 给 hbpm_basedatalist 加自定义业务字段

### 需求
业务方说："要给岗位基础资料（如岗位序列）加上'战略分类'标签（核心序列/支撑序列/专家序列），用于报表过滤。"

### 推荐方案
- **扩展对象**：`hbpm_basedatalist` 主实体
- **扩展点**：`modifyMeta(op=add field)`
- **风险**：低（基础资料 · 加字段安全）
- **关联 PR**：PR-007（issyspreset=true 的出厂数据不可改，但加新字段 OK）

### 调用链
```
Step 1: getDevInfo()                      // 拿 ISV 信息
Step 2: getBizApps()                      // 找 bizAppId / bizUnitId
Step 3: getFormSchema(hbpm_basedatalist)  // 查当前 20 字段清单 · 防重名
Step 4: modifyMeta({
  formId: "hbpm_basedatalist",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hbpm_basedatalist",   // 主实体（无子表）
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_strategycategory",    // ⭐ ISV 前缀防覆盖
      name: {zh_CN: "战略分类", en_US: "Strategy Category"},
      mustInput: false,
      enum: [
        {value: "core", label: "核心序列"},
        {value: "support", label: "支撑序列"},
        {value: "expert", label: "专家序列"}
      ]
    }
  }]
})
Step 5: getFormSchema(hbpm_basedatalist) // ⭐ 二次验证落库
```

### 踩坑
- ❌ 字段 key 不带 ISV 前缀 → 标品升级被覆盖（PR-001 配套规则）
- ❌ 字段 key > 24 字符 → 数据库列名截断
- ❌ 加完字段没在子页面布局 → 字段存在但 UI 不显示（需同时修改各子列表的表单布局）
- ❌ 给 issyspreset=true 的出厂数据设默认值 → 平台升级会覆盖
- ⚠ 本场景无子表（无 entry 实体），只能加到主实体
- 💡 `hbpm_basedatalist` 是分组入口页，实际数据分散在各子 form 中，加字段后还需在对应子 form 的列表/编辑视图里配置显示

### 关联 PR
- PR-007 · issyspreset=true 出厂数据不影响加新字段
- PR-001 · ISV 字段 key 必须带前缀

---

## CS-02 · 删除/禁用前置引用检查

### 需求
业务方说："某岗位序列已经被多个岗位引用，不能随意禁用，否则下游 hbjm_jobhr 的岗位会出现'游离字典'问题。能加保护吗？"

### 推荐方案
- **扩展对象**：`hbpm_basedatalist`
- **扩展点**：`onAddValidators@delete` + `onAddValidators@disable`
- **实现模式**：并列挂插件（**不继承** PositionBasedataEdit、HRBDGroupList、AbsOrgBaseOp · PR-001）
- **风险**：低（只读校验 · 不动数据）

### 扩展入口坐标
- 绑定表单：`hbpm_basedatalist`
- 绑定操作：`delete` · `disable`
- 推荐父类：**`HRDataBaseOp`**（白名单合规 · 与标品 CtrlStrategyValidator 无关联）
- 关键重写方法：`onAddValidators(AddValidatorsEventArgs args)`

### 调用链（执行时）
```
delete/disable 操作触发
  ↓
[onAddValidators] 按 RowKey 顺序注册 Validator：
  1. HRBaseDataStatusOp · 平台状态校验
  2. CodeRuleDeleteOp   · 释放编码池
  3. ⭐ ISV 加的 TdkwPositionBasedataRefValidator（本 CS）
  ↓
[Validator.validate()] 遍历每条待删/禁用数据
  ↓ 对 dataEntity.id 查反向引用（hbjm_jobhr 等）
HRBaseServiceHelper("hbjm_jobhr").isExists(
  new QFilter("positionbasedata", "=", id))
  ↓ 命中 → addErrorMessage 拒绝
```

### 代码框架

**TdkwPositionBasedataRefValidator.java**（父类：AbstractValidator）：
```java
package com.kingdee.${ISV_FLAG}.hbpm.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 删除/禁用 hbpm_basedatalist 前的反向引用校验
 * 业务铁律：禁删被岗位引用过的基础资料字典
 * 本场景是 BaseFormModel（非 HisModel）· 查询时不需加 iscurrentversion
 */
public class TdkwPositionBasedataRefValidator extends AbstractValidator {

    private static final String JOB_FORM = "hbjm_jobhr";
    private static final String REF_FIELD = "positionbasedata";

    @Override
    public void validate() {
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) return;

        HRBaseServiceHelper jobHelper = new HRBaseServiceHelper(JOB_FORM);

        for (ExtendedDataEntity ext : dataEntities) {
            DynamicObject dataEntity = ext.getDataEntity();

            // 出厂数据额外保护（issyspreset=true）
            if (dataEntity.getBoolean("issyspreset")) {
                this.addErrorMessage(ext, "系统预置基础资料不可删除 (issyspreset=true)");
                continue;
            }

            long id = dataEntity.getLong("id");
            // BaseFormModel：直接用 id 查，不需要 iscurrentversion 过滤
            QFilter qf = new QFilter(REF_FIELD, "=", id)
                .and(new QFilter("enable", "=", "1"));

            if (jobHelper.isExists(qf)) {
                this.addErrorMessage(ext, String.format(
                    "基础资料 [%s] 已被岗位引用，不可删除，建议改用禁用",
                    dataEntity.getString("name")
                ));
            }
        }
    }
}
```

**TdkwPositionBasedataRefCheckOp.java**（父类：HRDataBaseOp）：
```java
package com.kingdee.${ISV_FLAG}.hbpm.opplugin.web;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * ISV 并列挂插件 · 拦 delete / disable 操作
 * ⚠ 禁止继承 PositionBasedataEdit、HRBDGroupList、AbsOrgBaseOp（PR-001）
 */
public class TdkwPositionBasedataRefCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator) new TdkwPositionBasedataRefValidator());
    }
}
```

### 踩坑
- ❌ 继承 `PositionBasedataEdit` → PR-001 · 禁继承场景专属类
- ❌ 继承 `HRBDGroupList` → PR-001 · 禁继承场景专属类
- ❌ 继承 `AbsOrgBaseOp` → 不在 HR SDK 白名单（forbidden）
- ❌ 加 `iscurrentversion=true` 过滤 → 本场景是 BaseFormModel，无此字段，加了报错
- ❌ 在 afterExecuteOperationTransaction 做校验 → 事务已提交无法回滚（PR-010）
- 💡 与 haos 域的引用检查不同：本场景是 BaseFormModel，反向查询直接用 id，不用 boid

### 关联 PR
- PR-001 · ISV 并列挂 · 禁继承 PositionBasedataEdit / HRBDGroupList / AbsOrgBaseOp
- PR-010 · onAddValidators 阶段注册

---

## CS-03 · 字段联动（afterBindData / propertyChanged 模式）

### 需求
业务方说："想在打开基础资料编辑弹窗时，根据当前数据的某个字段值（如 ISV 扩展字段 ${ISV_FLAG}_strategycategory），自动设置另一个 ISV 字段（${ISV_FLAG}_allowed_scene）的默认值或可见性。"

### 推荐方案
- **扩展对象**：`hbpm_basedatalist` 表单插件层
- **扩展点**：`afterBindData`（初始化联动）+ `propertyChanged`（用户变更联动）
- **实现模式**：并列挂 FormPlugin（**不继承** PositionBasedataEdit · PR-001）
- **风险**：低（UI 层联动 · 不动数据）
- **父类选择**：`HRDataBaseEdit` 或 `HRCoreBaseBillEdit`（白名单合规）

### 关键设计原则
1. **afterBindData 做初始化联动**（参考 PositionBasedataEdit 的 issyspreset 检查模式）
2. **propertyChanged 做用户交互联动**（需 beginInit/endInit 防死循环 · PR-004）
3. **不调用标品 PositionBasedataEdit 的 afterBindData**（因为是并列挂不是继承）

### 代码框架

```java
package com.kingdee.${ISV_FLAG}.hbpm.formplugin.web;

import java.util.EventObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

/**
 * hbpm_basedatalist ISV 字段联动插件
 * RowKey 排在 PositionBasedataEdit(#6) 之后
 * ⚠ 禁止继承 PositionBasedataEdit（PR-001）
 * ⚠ 禁止继承 HRBDGroupList（PR-001）
 * ⚠ 禁止继承 AbsOrgBaseOp（PR-001 · forbidden）
 */
public class TdkwPositionBasedataEditPlugin extends HRDataBaseEdit {

    private static final String STRATEGY_CATEGORY = "${ISV_FLAG}_strategycategory";
    private static final String ALLOWED_SCENE = "${ISV_FLAG}_allowed_scene";

    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        // 初始化联动：issyspreset=true 时跳过（尊重标品只读保护）
        DynamicObject entity = this.getView().getModel().getDataEntity();
        if (entity.getBoolean("issyspreset")) return;

        String category = entity.getString(STRATEGY_CATEGORY);
        this.syncAllowedScene(category);
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        if (!STRATEGY_CATEGORY.equals(e.getProperty().getName())) return;

        String newCategory = (String) this.getModel().getValue(STRATEGY_CATEGORY);
        // PR-004：setValue 前必须 beginInit/endInit 防死循环
        this.getModel().beginInit();
        this.syncAllowedScene(newCategory);
        this.getModel().endInit();
        this.getView().updateView(ALLOWED_SCENE);
    }

    private void syncAllowedScene(String category) {
        String scene = "core".equals(category) ? "strategic" : "operational";
        this.getModel().setValue(ALLOWED_SCENE, scene);
    }
}
```

### 踩坑
- ❌ 继承 `PositionBasedataEdit` → PR-001（场景专属类禁继承）
- ❌ propertyChanged 里 setValue 不加 beginInit/endInit → 死循环（PR-004）
- ❌ 在 issyspreset=true 的预置数据上执行联动 setValue → 白费（VIEW 状态下 setValue 不生效且报错）
- ❌ 将联动逻辑写到 OP 层 → 保存后才生效 · UI 体验差（应在 FormPlugin 层早拦）

### 关联 PR
- PR-001 · 不继承 PositionBasedataEdit、HRBDGroupList、AbsOrgBaseOp
- PR-003 · FormPlugin 用 getModel().setValue（不用 entity.set）
- PR-004 · 同字段 setValue 死循环防护用 beginInit/endInit

---

## CS 关联 Pattern 速查

| CS | 关联 Pattern | 作用 |
|---|---|---|
| CS-01 | `add_field_extension` | 加 ISV 字段标准模式 |
| CS-02 | `add_unique_validation` + 反向引用查询（BaseFormModel · 直接用 id） | 删除前置校验 |
| CS-03 | `override_plugin_behavior`（并列挂版）| 字段联动（afterBindData + propertyChanged）|

---

## BEC 反指引（CS 说明）

本场景标品**没有发任何 BEC 事件**（grep 反编译文件 triggerEventSubscribe = 0 处命中）。

ISV 在 `hbpm_basedatalist` 不推荐自建 BEC 发布方：
1. 基础资料变更频率低（字典类，一年改几次），没有异步分发必要
2. 下游如需字典数据，直接查 `t_hbpm_basedatalist` 即可，无需 BEC 同步状态
3. 如下游确需感知岗位数据变更，应在 hbjm_jobhr（岗位）或 hrpi_empjobrel（员工岗位关系）层挂 BEC，而非在字典层

如确有外部系统订阅需求，走 PR-011（苍穹 BEC · afterExecuteOperationTransaction 阶段发布）。
