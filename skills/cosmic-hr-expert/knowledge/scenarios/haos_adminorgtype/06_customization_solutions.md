# 推荐定制方案 · 行政组织类型（haos_adminorgtype）

> **状态**: 🟢 基于反编译 3 类 + INV-AT-01~04 + platform_rules 11 PR
> **confidence**: verified（基础资料场景特性 · 5 个 CS · CS-05 走反指引）
> **结构**: 背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR

**禁继承铁律（PR-001）**：以下 6 个类 ISV 绝对禁止继承：
- `AdminorgtypeEditPlugin`（场景专属 FormPlugin）
- `AdminorgtypeListPlugin`（场景专属 List Plugin）
- `AdminOrgTypeSaveOp`（场景专属 OP · HIES 导入修正）
- `BaseDataBuOp`（haos 域共享薄壳 OP）
- `AbsOrgBaseOp`（不在 SDK 白名单）
- `CtrlStrategyValidator`（haos 域内部 Validator）

---

## CS-01 · 给 haos_adminorgtype 加自定义业务字段

### 需求

业务方说：要给行政组织类型加上"类型分类"（战略/职能/支撑）或"适用规模"标签，用于报表分析/组织规划。

### 推荐方案

- **扩展对象**：`haos_adminorgtype` 主实体
- **扩展点**：`modifyMeta(op=add field)`
- **风险**：低（基础资料 · 出厂数据扩展安全）
- **关联 INV**：issyspreset=true 行 isvCanModify=false（但加新字段 OK）
- **关联 PR**：PR-007 预置数据 number 不可改（不影响新字段）

### 调用链

```
Step 1: getDevInfo()                       // 拿 ISV 信息
Step 2: getBizApps()                       // 找 bizAppId / bizUnitId
Step 3: getFormSchema(formNumber=haos_adminorgtype)  // 查目前 31 字段清单 · 防重名
Step 4: modifyMeta({
  formId: "haos_adminorgtype",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "haos_adminorgtype",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_orgtypecategory",        // ⭐ ISV 前缀防覆盖
      name: {zh_CN: "类型分类", en_US: "Org Type Category"},
      mustInput: false,
      enum: [
        {value: "strategy", label: "战略型"},
        {value: "function", label: "职能型"},
        {value: "support",  label: "支撑型"}
      ]
    }
  }]
})
Step 5: getFormSchema(haos_adminorgtype)  // ⭐ 二次验证落库
```

### 踩坑

- ❌ 字段 key 不带 ISV 前缀（如 `orgtypecategory`）→ 标品升级被覆盖（PR-001 配套规则）
- ❌ 字段 key > 24 字符 → 数据库列名上限触顶 · 平台静默截断
- ❌ 加到子表 → 错 · **本表没有 entry 子表**（跟三胞胎一致）· 加字段只能在主表
- ❌ 想给 issyspreset=true 的出厂数据"补默认值" → 平台升级会再次刷预置数据 · 你的默认值会丢
- ⚠ adminorgtypestd / orgpattern 字段 isvCanModify=false · 禁止用 modifyMeta 修改这两个字段属性
- 💡 加字段后想在 OP 层读取 · 必须自建并列挂插件 + onPreparePropertys 显式声明

### 关联 PR

- 遵循 PR-007 · 预置数据编码不可改（你加新字段不动 number）
- 遵循 PR-001 · **不继承 AdminorgtypeEditPlugin / BaseDataBuOp**（场景特有类）
- 遵循 PR-010 · OP 阶段读 ISV 字段必须 onPreparePropertys 声明

---

## CS-02 · 删除/禁用前置校验：检查 haos_adminorg 反向引用

### 需求

业务方说："删了一条行政组织类型，结果以前的 admin_org 数据的组织类型字段全都游离了，能不能加个保护？"

### 推荐方案

- **扩展对象**：`haos_adminorgtype`
- **扩展点**：`onAddValidators@delete` + `onAddValidators@disable`（双拦）
- **实现模式**：并列挂插件（**禁止继承** BaseDataBuOp / AdminOrgTypeSaveOp · PR-001）+ 注册 AbstractValidator
- **风险**：低（只读校验 · 不动数据）
- **关联 PR**：PR-001（并列挂）· PR-010（onAddValidators 阶段）

### 扩展入口坐标

- 绑定表单：`haos_adminorgtype`
- 绑定操作：`delete` · `disable`
- 推荐父类：**`HRDataBaseOp`**（HR 通用 OP 抽象基类 · 白名单合规）
- **禁止继承**：`BaseDataBuOp`（PR-001）· `AdminOrgTypeSaveOp`（PR-001）· `AbsOrgBaseOp`（不在白名单）

### 调用链（执行时）

```
delete 操作触发
  ↓
[onAddValidators] 多个 OP 按 RowKey 注册 Validator：
  1. BaseDataDeletePlugin
  2. CodeRuleDeleteOp
  3. HRBaseDataStatusOp
  4. BaseDataBuOp（注册 CtrlStrategyValidator）
  5. ⭐ ISV 加的 TdkwAdmintypeRefCheckOp（本 CS）
  ↓
[Validator.validate()] TdkwAdmintypeDeleteValidator 遍历每条待删数据
  ↓ 对 dataEntity.id 查反向引用
HRBaseServiceHelper("haos_adminorg").isExists(
  new QFilter("adminorgtype", "=", id)
    .and(new QFilter("iscurrentversion", "=", true)))  ⭐ HisModel · 必须加 iscurrentversion
  ↓ 命中 → addErrorMessage 拒绝
```

### 代码框架

**TdkwAdmintypeDeleteValidator.java**（白名单父类：AbstractValidator）：

```java
package com.kingdee.${ISV_FLAG}.haos.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 删除/禁用 haos_adminorgtype 前的反向引用校验
 * 跟 haos_adminorgfunction CS-02 同模式 · 关键差异：
 *   查 haos_adminorg.adminorgtype（单选 BasedataField · 直字段查）
 *   + iscurrentversion=true（admin_org 是 HisModel 时序）
 */
public class TdkwAdmintypeDeleteValidator extends AbstractValidator {

    private static final String ADMINORG_FORM = "haos_adminorg";
    private static final String REF_FIELD = "adminorgtype";  // 单选 · 直字段查

    @Override
    public void validate() {
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) {
            return;
        }
        HRBaseServiceHelper helper = new HRBaseServiceHelper(ADMINORG_FORM);
        for (ExtendedDataEntity ext : dataEntities) {
            DynamicObject dataEntity = ext.getDataEntity();
            if (dataEntity.getBoolean("issyspreset")) {
                this.addErrorMessage(ext, "系统预置行政组织类型不可删除 (issyspreset=true)");
                continue;
            }
            long id = dataEntity.getLong("id");
            // ⭐ admin_org 是 HisModel 时序 · 必须加 iscurrentversion=true 过滤
            QFilter qf = new QFilter(REF_FIELD, "=", id)
                .and(new QFilter("iscurrentversion", "=", Boolean.TRUE))
                .and(new QFilter("enable", "=", "1"));
            if (helper.isExists(qf)) {
                this.addErrorMessage(ext, String.format(
                    "行政组织类型 [%s] 已被行政组织当前生效版本引用 · 不可删除 · 建议改用禁用",
                    dataEntity.getString("name")
                ));
            }
        }
    }
}
```

**TdkwAdmintypeRefCheckOp.java**（白名单父类：HRDataBaseOp）：

```java
package com.kingdee.${ISV_FLAG}.haos.opplugin.web;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * ISV 并列挂插件 · 拦 delete / disable 操作
 * ⚠ 禁止继承：BaseDataBuOp / AdminOrgTypeSaveOp / AbsOrgBaseOp（PR-001）
 */
public class TdkwAdmintypeRefCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator) new TdkwAdmintypeDeleteValidator());
    }
}
```

### 踩坑

- ❌ 没加 `iscurrentversion=true` → admin_org 是 HisModel 时序场景 · 历史版本持有外键但当前版本已替换 · 不该影响删除决策
- ❌ 用 `.fbasedataid` 路径查 → 错 · 这是 MulBasedataField 多选的查法 · 本场景是单选直字段查
- ❌ 继承 `BaseDataBuOp` → 违反 PR-001
- ❌ 继承 `AdminOrgTypeSaveOp` → 违反 PR-001（场景专属 OP · HIES 导入修正类）
- ❌ 继承 `AbsOrgBaseOp` → 不在 SDK 白名单（PR-001）

### 关联 PR

- 遵循 PR-001 · 并列挂 · **禁继承 BaseDataBuOp / AdminOrgTypeSaveOp / AbsOrgBaseOp**
- 遵循 PR-002 · RowKey 早于标品插件
- 遵循 PR-010 · onAddValidators 阶段注册

---

## CS-03 · 自定义字段联动（参考 adminorgtypestd → orgpattern 实证模式）

### 需求

业务方说："我们需要一个自定义字段联动：当用户选择了 ${ISV_FLAG}_orgtypecategory（类型分类）后，自动填写另一个 ISV 字段 ${ISV_FLAG}_typegrade（类型等级）。"

### 推荐方案

- **扩展对象**：`haos_adminorgtype` 主表单
- **扩展点**：`propertyChanged@haos_adminorgtype`（FormPlugin 层）
- **实现模式**：并列挂 FormPlugin（**禁止继承** AdminorgtypeEditPlugin · PR-001）
- **风险**：中（联动逻辑跟标品冲突会导致字段反复刷新）
- **关联 PR**：PR-003（FormPlugin 用 setValue）· PR-004（防死循环 · ⭐ 标品未用 beginInit 但 ISV 必须用）

### 扩展入口坐标

- 绑定表单：`haos_adminorgtype`
- 推荐父类：**`HRCoreBaseBillEdit`**（白名单合规）或 `HRBaseDataTplEdit`
- ⚠ **禁止继承 `AdminorgtypeEditPlugin`**（场景专属 · 标品升级方法签名变会编译失败 · PR-001）
- ⚠ **禁止继承 `AdminorgtypeListPlugin`**（列表场景专属 · PR-001）

### 调用链（执行时）

```
[用户选择 ${ISV_FLAG}_orgtypecategory（类型分类）]
    ↓
propertyChanged 事件 · 多个 FormPlugin 按 RowKey 执行：
  1. AdminorgtypeEditPlugin.propertyChanged（标品 #9 · 处理 adminorgtypestd → orgpattern）
  2. ⭐ ISV 加的 TdkwAdmintypeEditPlugin.propertyChanged
       if name == "${ISV_FLAG}_orgtypecategory":
          value = getModel().getValue("${ISV_FLAG}_orgtypecategory")
          // 查映射表 → 得到 ${ISV_FLAG}_typegrade
          ⚠ 必须用 beginInit/endInit 防死循环（PR-004）
```

### 代码框架

**TdkwAdmintypeEditPlugin.java**（白名单父类：HRCoreBaseBillEdit）：

```java
package com.kingdee.${ISV_FLAG}.haos.formplugin.web;

import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * haos_adminorgtype 表单层 · 自定义字段联动
 * ⚠ 父类选择：HRCoreBaseBillEdit（不是 AdminorgtypeEditPlugin · PR-001 禁继承）
 * ⚠ propertyChanged 中 setValue 必须用 beginInit/endInit 防死循环（PR-004）
 *    注意：标品 AdminorgtypeEditPlugin 未用 beginInit（直接 setValue） · ISV 必须加
 */
public class TdkwAdmintypeEditPlugin extends HRCoreBaseBillEdit {

    private static final String SOURCE_FIELD = "${ISV_FLAG}_orgtypecategory";
    private static final String TARGET_FIELD = "${ISV_FLAG}_typegrade";

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        String name = e.getProperty().getName();
        if (!SOURCE_FIELD.equals(name)) {
            return;
        }
        String categoryValue = (String) this.getModel().getValue(SOURCE_FIELD);
        if (HRStringUtils.isEmpty(categoryValue)) {
            return;
        }
        // 查映射关系（实际应从 ISV 配置基础资料查 · 此处仅示意）
        String gradeValue = lookupGrade(categoryValue);

        // ⭐ PR-004 · 同字段/联动字段 setValue 必须 beginInit/endInit 防死循环
        this.getModel().beginInit();
        this.getModel().setValue(TARGET_FIELD, gradeValue);
        this.getModel().endInit();
        this.getView().updateView(TARGET_FIELD);
    }

    private String lookupGrade(String category) {
        // 实际查 ISV 配置基础资料 · 此处简化
        if ("strategy".equals(category)) return "A";
        if ("function".equals(category)) return "B";
        return "C";
    }
}
```

### 踩坑

- ❌ 继承 `AdminorgtypeEditPlugin` → 违反 PR-001（场景专属禁继承）
- ❌ propertyChanged 里 setValue 不加 beginInit/endInit → 死循环（PR-004）
  > 注意：标品 AdminorgtypeEditPlugin 的 propertyChanged 直接调 setValue（未用 beginInit）· 标品靠框架内部机制避免重入 · ISV 自定义联动必须加 beginInit/endInit 保护
- ❌ RowKey 排在 AdminorgtypeEditPlugin 之前 → 标品后跑的 adminorgtypestd→orgpattern 联动可能覆盖你的结果
- ❌ 不调 super.propertyChanged(e) → HRCoreBaseBillEdit 的默认行为丢失
- ⚠ ALLOWED 映射表应抽到 ISV 配置基础资料 · 不要硬编码（跨环境主键不一致）

### 关联 PR

- 遵循 PR-003 · FormPlugin 用 getModel().setValue（不用 entity.set）
- 遵循 PR-004 · 同字段 setValue 死循环防护用 beginInit/endInit
- 遵循 PR-001 · **禁继承 AdminorgtypeEditPlugin / AdminorgtypeListPlugin**（场景专属）

---

## CS-04 · HIES 导入时扩展 orgpattern 修正逻辑

### 需求

业务方说："标品 AdminOrgTypeSaveOp 在 HIES 导入时修正 orgpattern 字段。我们需要在此基础上，额外修正 ISV 加的 ${ISV_FLAG}_orgtypecategory 字段（HIES 导入源数据不含此字段，需要按 adminorgtypestd 自动填入默认值）。"

### 推荐方案

- **扩展对象**：`haos_adminorgtype`（importdata_hr opKey）
- **扩展点**：`beginOperationTransaction@importdata_hr`（并列挂 OP）
- **实现模式**：并列挂 OP（**禁止继承** AdminOrgTypeSaveOp · PR-001）
- **风险**：中（事务内修正数据 · 确保幂等性）
- **关键 HIES 标志**：`this.getOption().getVariables().get("importtype")` 非空才执行

### 扩展入口坐标

- 绑定表单：`haos_adminorgtype`
- 绑定操作：`importdata_hr`
- 推荐父类：**`HRDataBaseOp`**
- ⚠ **禁止继承 `AdminOrgTypeSaveOp`**（场景专属 OP · PR-001）
- ⚠ **禁止继承 `AbsOrgBaseOp`**（不在 SDK 白名单）

### 调用链（执行时）

```
importdata_hr 触发（HIES 导入）
    ↓
标品 AdminOrgTypeSaveOp.beginOperationTransaction
    ↓ 修正 orgpattern（标品逻辑）
⭐ ISV 并列挂 TdkwAdmintypeSaveOp.beginOperationTransaction
    ↓ 检查 importtype 非空
    ↓ 修正 ${ISV_FLAG}_orgtypecategory
```

### 代码框架

**TdkwAdmintypeImportOp.java**（白名单父类：HRDataBaseOp）：

```java
package com.kingdee.${ISV_FLAG}.haos.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * HIES 导入时扩展修正 ISV 字段
 * ⚠ 父类：HRDataBaseOp（不是 AdminOrgTypeSaveOp · PR-001 禁继承）
 * ⚠ 必须检测 importtype 变量 · 跟标品 AdminOrgTypeSaveOp 保持同等守卫
 */
public class TdkwAdmintypeImportOp extends HRDataBaseOp {

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        // PR-010 · OP 默认不加载 ISV 字段 · 必须显式声明
        e.getFieldKeys().add("adminorgtypestd");
        e.getFieldKeys().add("${ISV_FLAG}_orgtypecategory");
    }

    @Override
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        // ⭐ HIES 导入守卫 · 跟标品 AdminOrgTypeSaveOp 同等守卫
        if (HRStringUtils.isEmpty(
                (String) this.getOption().getVariables().get("importtype"))) {
            return;
        }
        for (DynamicObject dataEntity : e.getDataEntities()) {
            DynamicObject adminorgtypestd =
                (DynamicObject) dataEntity.getDynamicObject("adminorgtypestd");
            if (adminorgtypestd == null) {
                continue;
            }
            // 按 adminorgtypestd 填充 ISV 字段 ${ISV_FLAG}_orgtypecategory
            String category = lookupCategoryByStdId(adminorgtypestd.getLong("id"));
            if (!HRStringUtils.isEmpty(category)) {
                dataEntity.set("${ISV_FLAG}_orgtypecategory", category);
            }
        }
    }

    private String lookupCategoryByStdId(long stdId) {
        // 实际查 ISV 配置基础资料 · 此处简化
        return "strategy";
    }
}
```

### 踩坑

- ❌ 继承 `AdminOrgTypeSaveOp` → 违反 PR-001（场景专属 OP）
- ❌ 继承 `AbsOrgBaseOp` → 不在 SDK 白名单（PR-001）
- ❌ 不检测 importtype 变量 → 普通 save 也执行修正逻辑 · 破坏正常保存行为
- ❌ onPreparePropertys 未声明 ISV 字段 → beginOperationTransaction 取到 null
- ⚠ RowKey 建议在标品 AdminOrgTypeSaveOp 之后 · 确保标品先修正 orgpattern · ISV 再补修正 ISV 字段

### 关联 PR

- 遵循 PR-001 · **禁继承 AdminOrgTypeSaveOp / AbsOrgBaseOp**
- 遵循 PR-010 · OP 全生命周期：onPreparePropertys 声明字段 + beginOperationTransaction 修正数据
- 遵循 PR-003 · OP 层用 entity.set（不用 getModel().setValue）

---

## CS-05 · 自建子表行 / 业务编码 · PR-005 实证（兼 BEC 反指引）

### 需求 A · 自建子表

业务方说："要给每条 adminorgtype 加一张'适用部门配置'子表，每行需要 id 和业务编码。"

### 推荐方案

- **扩展点**：`modifyMeta(op=add entity)` 加子表 + ISV 插件操作子表行 id
- **关键约束**：行 id 必须用 `kd.bos.id.ID.genLongId()` · 不能 UUID / timestamp / max+1（PR-005）
- **关联 PR**：PR-005（ID 生成强制场景）
- ⚠ **禁止继承 AdminOrgTypeSaveOp / BaseDataBuOp / AbsOrgBaseOp**（PR-001）

### 代码框架

**TdkwAdmintypeEntryAppendOp.java**（白名单父类：HRDataBaseOp）：

```java
package com.kingdee.${ISV_FLAG}.haos.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.id.ID;               // ⭐ PR-005 唯一 ID 来源
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * ⚠ 父类：HRDataBaseOp（禁继承 AdminOrgTypeSaveOp / BaseDataBuOp · PR-001）
 * ⭐ 子表行 id 必须用 ID.genLongId()（PR-005）
 */
public class TdkwAdmintypeEntryAppendOp extends HRDataBaseOp {

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("${ISV_FLAG}_dept_entry");
        e.getFieldKeys().add("${ISV_FLAG}_dept_entry.${ISV_FLAG}_dept_id");
    }

    @Override
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        for (DynamicObject dataEntity : e.getDataEntities()) {
            DynamicObjectCollection entries =
                dataEntity.getDynamicObjectCollection("${ISV_FLAG}_dept_entry");
            for (DynamicObject row : entries) {
                if (row.getLong("id") <= 0L) {
                    // ⭐ PR-005 · 子表行 id 用 ID.genLongId · 不能 UUID/timestamp/max+1
                    row.set("id", ID.genLongId());
                }
            }
        }
    }
}
```

### 踩坑

- ❌ 继承 `AdminOrgTypeSaveOp` → PR-001 禁继承（场景专属）
- ❌ 继承 `BaseDataBuOp` → PR-001 禁继承
- ❌ 继承 `AbsOrgBaseOp` → PR-001 禁继承（不在白名单）
- ❌ `row.set("id", UUID.randomUUID())` → UUID 跟苍穹分布式 ID 体系不兼容（PR-005）
- ❌ onPreparePropertys 未声明子表字段 → beginOperationTransaction 子表 collection 为 null

---

### 需求 B · BEC 反指引

业务方追问："想让 haos_adminorgtype 修改/禁用后通知下游，用 BEC 行不行？"

**实证判定**（grep 0 命中）：标品不在本场景发任何 BEC。

**反指引：为什么不做**：
1. 基础资料变更频率低（一年改几次）· 没有批量异步分发的性能必要
2. 下游若有需要直接查 t_haos_adminorgtype 即可
3. 跟三胞胎 haos_adminorgfunction / haos_orgchangereason CS-05 BEC 反指引同源

**如果确实必须自建 BEC 发布方**：
1. 在【开发平台 → 业务事件管理】预注册 eventNumber（PR-011）
2. 并列挂 OP 插件 · 重写 `afterExecuteOperationTransaction`（PR-010 第 9 步）
3. 调 `IEventService.triggerEventSubscribeJobs(...)` 异步发事件

---

## CS 关联 Pattern 速查

| CS | 关联 Pattern | 作用 |
|---|---|---|
| CS-01 | `add_field_extension` | 加 ISV 字段标准模式 |
| CS-02 | 反向引用校验（单选 + iscurrentversion）| 删除前置校验 · 查 admin_org.adminorgtype |
| CS-03 | `override_plugin_behavior`（并列挂版）| 自定义字段联动（参考 adminorgtypestd→orgpattern 模式）|
| CS-04 | HIES 导入修正扩展（importtype 守卫）| 标品 AdminOrgTypeSaveOp 模式复用 |
| CS-05 | `add_sub_entity` + `pr005_id_generation` + BEC 反指引 | 子表 ID 生成 + 反指引 |

## 跟 haos_adminorgfunction CS 的对比

| CS | haos_adminorgtype（本场景）| haos_adminorgfunction |
|---|---|---|
| CS-01 加字段 | 主表加（adminorgtypestd/orgpattern 已有 isvCanModify=false）| 主表加（无保护字段影响）|
| CS-02 删除前置校验 | 单选 BasedataField 直字段查 + iscurrentversion | 同模式 · 字段名不同 |
| CS-03 联动 | propertyChanged 参考 adminorgtypestd→orgpattern 标品模式 + PR-004 | ctrlstrategy → 自定义校验 |
| CS-04 | HIES 导入修正扩展（本场景独有）| 编码规则配置（PR-006）|
| CS-05 | 子表 + ID 生成 + BEC 反指引 | 子表 + ID 生成 + BEC 反指引 |
