# 推荐定制方案 · 行政组织变动场景（haos_changescene）

> **状态**: 🟢 基于反编译 3 类 + INV-CS-01~06 + platform_rules 11 PR
> **confidence**: real_deploy（基础资料场景特性 · 5 个 CS · CS-05 走反指引）
> **结构**: 背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR

---

## CS-01 · 给 haos_changescene 加自定义业务字段

### 需求
业务方说：要给变动场景加上"适用业务线"或"适用组织规模"字段 · 用于按业务线维护不同的字典子集。

### 推荐方案
- **扩展对象**：`haos_changescene` 主实体
- **扩展点**：`modifyMeta(op=add field)`
- **风险**：低（基础资料 · 出厂数据扩展安全）
- **关联 INV**：INV-CS-03（出厂数据 isvCanModify=false 不能改 · 但加新字段 OK）
- **关联 PR**：PR-007 预置数据 number 不可改（不影响新字段）

### 调用链
```
Step 1: getDevInfo()                       // 拿 ISV 信息 (developerId / projectId)
Step 2: getBizApps()                       // 找 bizAppId / bizUnitId
Step 3: getFormSchema(formNumber=haos_changescene)  // 查目前字段清单 · 防重名
Step 4: modifyMeta({
  formId: "haos_changescene",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "haos_changescene",       // 主实体 · 不是子表
    element: {
      fieldType: "BasedataField",          // 或 ComboField / TextField 看业务
      key: "${ISV_FLAG}_bizline",                 // ⭐ ISV 前缀防覆盖
      name: {zh_CN: "适用业务线", en_US: "Business Line"},
      mustInput: false,
      refEntity: "${ISV_FLAG}_bizline_dict"       // 业务自建字典 · 或换 ComboField 写枚举
    }
  }]
})
Step 5: getFormSchema(haos_changescene)    // ⭐ 二次验证落库（PR-006 之后的硬规则 6）
```

### 代码框架（Python · cosmic_devportal_client）
```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "2=H+4F0C+GJJ", "number": "haos_changescene"}
)

# 加 BasedataField 引用业务线字典
designer.add_field(
    field_type="BasedataField",
    name="适用业务线",
    key="${ISV_FLAG}_bizline",
    parent_entity_id=designer.base_entity_id,
    ref_entity="${ISV_FLAG}_bizline_dict"
)
designer.save()  # 一次 save click 提交
```

### 踩坑
- ❌ 字段 key 不带 ISV 前缀（如 `bizline`）→ 标品升级被覆盖（PR-001 配套规则）
- ❌ 字段 key > 24 字符 → 数据库列名上限触顶 · 平台静默截断
- ❌ 想加到子表 t_haos_cschangereason / t_haos_cschangeoperat → 错 · 这是 MulBasedata 平台维护子表 · 加字段也只在主表
- ❌ 想给 issyspreset=true 的出厂数据"补默认值" → 平台升级会再次刷预置数据 · 你的默认值会丢
- ⚠ 加 BasedataField 引用其他 ISV 字典时 · refEntity 必须是已建好的 form · 否则 add 成功但运行时 F7 拿不到数据
- 💡 加字段前先查 `_shared/_standard_metadata/entity_metadata/haos_changescene.md` 确认现有字段清单

### 关联 PR
- 遵循 PR-007 · 预置数据编码不可改（你加新字段不动 number）
- 遵循 PR-001 · 不继承 ChangeSceneSaveOp（场景特有类）

---

## CS-02 · 删除 / 禁用前置校验：检查 homs_orgbatchchgbill 反向引用 ⭐ 核心

### 需求
业务方说："最近发现有同事手贱删了一条变动场景 · 结果以前提交的几十张调整申请单 changescene 字段全都显示空 · 出大事了。能不能加个保护？"

### 推荐方案
- **扩展对象**：`haos_changescene`
- **扩展点**：`onAddValidators@delete` + `onAddValidators@disable`（双拦）
- **实现模式**：并列挂插件（**不继承** ChangeSceneSaveOp · PR-001）+ 注册 AbstractValidator
- **风险**：低（只读校验 · 不动数据）
- **关联 INV**：INV-CS-06（标品 delete/disable 没有反向引用校验）
- **关联 PR**：PR-001（并列挂）· PR-010（onAddValidators 阶段 · 不在 afterExecute）

### 扩展入口坐标
- 绑定表单：`haos_changescene`
- 绑定操作：`delete` · `disable`
- 推荐父类：**`HRDataBaseOp`**（HR 通用 OP 抽象基类 · 白名单合规）
- 关键重写方法：`onAddValidators(AddValidatorsEventArgs args)` — 注册自建 Validator

### 调用链（执行时）
```
delete 操作触发
  ↓
[onAddValidators] 多个 OP 插件按 RowKey 顺序注册 Validator：
  1. BaseDataDeletePlugin · 平台默认（无业务校验）
  2. CodeRuleDeleteOp · 释放编码池
  3. HRBaseDataStatusOp · 状态校验
  4. HRBaseDataLogOp · 日志
  5. ⭐ ISV 加的 ChangeSceneRefValidator (本 CS)
  ↓
[Validator.validate()] ChangeSceneRefValidator 遍历每条待删数据
  ↓ 对 dataEntity.id 查反向引用
QueryServiceHelper.exists("homs_orgbatchchgbill", new QFilter("changescene", "=", id))
QueryServiceHelper.exists("homs_orgbatchchgbill", new QFilter("add_changescene", "=", id))
... (6 个其他 *_changescene 字段)
QueryServiceHelper.exists("haos_adminorgdetail", new QFilter("changescene", "=", id))
  ↓ 任一命中 → addErrorMessage 拒绝
```

### 代码框架

**ChangeSceneDeleteValidator.java**（白名单父类：AbstractValidator）：
```java
package com.kingdee.${ISV_FLAG}.haos.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

public class ChangeSceneDeleteValidator extends AbstractValidator {

    /** 7 个 entry 字段名 (主体 + 6 前缀) · 按 03 §1 实证 */
    private static final String[] ORGBILL_ENTRY_FIELDS = {
        "changescene", "add_changescene", "parent_changescene",
        "info_changescene", "disable_changescene",
        "merge_changescene", "split_changescene"
    };

    @Override
    public void validate() {
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) {
            return;
        }

        HRBaseServiceHelper orgBillHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
        HRBaseServiceHelper detailHelper  = new HRBaseServiceHelper("haos_adminorgdetail");

        for (ExtendedDataEntity ext : dataEntities) {
            DynamicObject dataEntity = ext.getDataEntity();

            // 出厂数据直接拒
            if (dataEntity.getBoolean("issyspreset")) {
                this.addErrorMessage(ext, "系统预置变动场景不可删除 (issyspreset=true · INV-CS-03)");
                continue;
            }

            long id = dataEntity.getLong("id");

            // 1. 查 homs_orgbatchchgbill 7 个 entry 字段
            for (String fk : ORGBILL_ENTRY_FIELDS) {
                if (orgBillHelper.isExists(new QFilter(fk, "=", id))) {
                    this.addErrorMessage(ext, String.format(
                        "变动场景 [%s] 已被组织调整申请单的 %s 字段引用 · 不可删除",
                        dataEntity.getString("name"), fk
                    ));
                    break;  // 一处命中即拒
                }
            }

            // 2. 查 haos_adminorgdetail 引用
            if (detailHelper.isExists(new QFilter("changescene", "=", id))) {
                this.addErrorMessage(ext, String.format(
                    "变动场景 [%s] 已被行政组织详情引用 · 不可删除",
                    dataEntity.getString("name")
                ));
            }
        }
    }
}
```

**ChangeSceneRefCheckOp.java**（白名单父类：HRDataBaseOp）：
```java
package com.kingdee.${ISV_FLAG}.haos.opplugin.web;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * ISV 并列挂插件 · 拦 delete / disable 操作
 * RowKey 在标品插件之前（PR-002）
 */
public class ChangeSceneRefCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator) new ChangeSceneDeleteValidator());
    }
}
```

### 平台绑定步骤
1. 打开【苍穹开发平台】→ 定位表单 `haos_changescene`
2. 选择【操作】标签 → 找到 opKey = `delete` → 点【扩展插件】→ 新增 `com.kingdee.${ISV_FLAG}.haos.opplugin.web.ChangeSceneRefCheckOp` · RowKey=1（早于标品 4 个）
3. 重复对 opKey = `disable` 做同样配置
4. 保存 → 部署生效

### 踩坑
- ❌ 把 Validator 写到 `beforeExecuteOperationTransaction` → 已经进事务 · addErrorMessage 不拦能力差（PR-010 第 4 步是注册时机 · 不是校验时机）
- ❌ 用 `BusinessDataServiceHelper.load()` 而非 `QueryServiceHelper.exists()` → 性能差（exists 走 count(1) limit 1）
- ❌ 反向查 7 个字段时漏一个（如忘了 split_changescene）→ 留死角
- ❌ 继承 `ChangeSceneSaveOp` → 违反 PR-001（场景特有类不能继承）
- ❌ 用 `kd.bos.servicehelper.QueryServiceHelper` 加 OR 拼 7 个字段 → 性能差且不直观 · 还是循环 exists 干净
- ⚠ haos_adminorgdetail 是时序模型 · 查的时候是否要加 `iscurrentversion=true` 看业务（PR-008）：
  - 业务上"删字典"应该看"任何历史版本是否引用过" · 不要加 iscurrentversion · 全量查
  - 这跟"查活跃员工任职"那种用 boid + iscurrentversion 不同
- 💡 错误消息里带 `dataEntity.getString("name")` 让用户知道是哪条字典 + 哪个字段引用 · 调试效率翻倍

### 关联 PR
- 遵循 PR-001 · ISV 并列挂插件 · 不继承 ChangeSceneSaveOp
- 遵循 PR-002 · RowKey 早于标品 4 插件 · 拦在标品之前
- 遵循 PR-010 · onAddValidators 阶段注册 · 不在 afterExecute
- 遵循 PR-007 · issyspreset=true 出厂数据连删都禁

---

## CS-03 · 自定义 orgchangetype → changeoperat 联动逻辑

### 需求
业务方说："标品 ChangeSceneServiceHelper.getChangeOperate 是硬编码 · 我们公司有个特殊'临时调整'类型 · 应该默认带'紧急更新'操作 · 不是标品默认的'更新'。"

### 推荐方案
- **扩展对象**：`haos_changescene` 主表单
- **扩展点**：`propertyChanged@haos_changescene`（FormPlugin 层 · 不是 OP 层）
- **实现模式**：并列挂插件 + super 调用标品 ChangeSceneEditPlugin 后追加自定义逻辑
- **风险**：中（联动逻辑跟标品冲突会导致 changeoperat 反复刷新）
- **关联 PR**：PR-003（FormPlugin 用 setValue）· PR-004（防死循环）

### 扩展入口坐标
- 绑定表单：`haos_changescene`
- 推荐父类：**`HRCoreBaseBillEdit`**（白名单 · 跟标品 ChangeSceneEditPlugin 同源）
- ⚠ **注意**：因为标品 ChangeSceneEditPlugin 自己继承 HRCoreBaseBillEdit · 你**并列挂**到 form 上 · 跟它互不影响 · 由 RowKey 决定执行顺序
- 关键重写方法：`propertyChanged(PropertyChangedArgs e)`

### 调用链（执行时）
```
[用户在表单改 orgchangetype]
  ↓
propertyChanged 事件触发 · 多个 FormPlugin 按 RowKey 执行：
  1. ChangeSceneEditPlugin.propertyChanged (标品)
       → ChangeSceneServiceHelper.getChangeOperate(typeId)
       → setValue("changeoperat", standard_value)
  2. ⭐ ISV 加的 TdkwChangeSceneEditPlugin.propertyChanged
       → if (typeId == TEMP_ADJUST_ID) {
            setValue("changeoperat", URGENT_OPERAT_ID)
            ↑ 覆盖标品的设值 · 业务上"我们的优先级更高"
         }
```

### 代码框架

**TdkwChangeSceneEditPlugin.java**：
```java
package com.kingdee.${ISV_FLAG}.haos.formplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit;
import kd.hr.hbp.common.util.HRStringUtils;

public class TdkwChangeSceneEditPlugin extends HRCoreBaseBillEdit {

    /** 客户私有"临时调整"主键 · 必须从基础资料先建好后填 */
    private static final long TEMP_ADJUST_TYPE_ID = 11000L;
    /** 客户私有"紧急更新"操作主键 */
    private static final long URGENT_OPERAT_ID = 11001L;

    private static final String ORGCHANGETYPE = "orgchangetype";
    private static final String CHANGEOPERAT  = "changeoperat";

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        // ⚠ 不调 super · 因为本插件并列挂在标品 ChangeSceneEditPlugin 之后（RowKey 顺序）
        // 标品已经执行完它的 propertyChanged · 此处只追加客户特殊逻辑
        if (!ORGCHANGETYPE.equals(e.getProperty().getName())) {
            return;
        }

        DynamicObject typeObj = (DynamicObject) this.getModel().getValue(ORGCHANGETYPE);
        if (typeObj == null) {
            return;
        }
        long typeId = typeObj.getLong("id");

        if (typeId == TEMP_ADJUST_TYPE_ID) {
            // PR-004 · 联动跟当前 propertyChanged 同字段会死循环 · 但此处改的是不同字段 changeoperat · 不会触发新的 propertyChanged 死循环
            // 不需要 beginInit/endInit
            this.getModel().setValue(CHANGEOPERAT, new Object[]{URGENT_OPERAT_ID});
        }
    }
}
```

### 平台绑定步骤
1. 打开【苍穹开发平台】→ 定位表单 `haos_changescene`
2. 选择【表单插件】标签 → 新增 `com.kingdee.${ISV_FLAG}.haos.formplugin.web.TdkwChangeSceneEditPlugin`
3. RowKey 排在标品 ChangeSceneEditPlugin 之后（取大值）· 这样标品先跑 · 你后跑覆盖
4. 保存 → 部署生效

### 踩坑
- ❌ 在 `propertyChanged` 里 `setValue("orgchangetype", X)` → 触发新的 propertyChanged 事件 → 死循环（PR-004）· 必须用 `beginInit() / endInit()` 包起来
- ❌ 设 `setValue("changeoperat", longValue)`（直接传 Long）→ MulBasedataField 期望数组 · 应传 `new Object[]{longValue}`
- ❌ RowKey 排在标品之前 → 标品后跑会再次覆盖你的值 · 你的逻辑作废
- ❌ 继承 `ChangeSceneEditPlugin` → 违反 PR-001（场景特有类不能继承）· 改用并列挂
- ❌ 在 OP 层（ChangeSceneSaveOp）做这事 → 用户保存后才生效 · UI 体验差 · 对联动场景应该走 FormPlugin
- ⚠ 标品 ChangeSceneSaveOp.beginOperationTransaction 在 importtype!=null 时会再次按标品逻辑覆盖（兜底） · 如果你的客户走导入路径 · 还要在 OP 层加另一个并列插件做相同覆盖（参考 CS-02 模式）

### 关联 PR
- 遵循 PR-003 · FormPlugin 用 getModel().setValue（不用 entity.set）
- 遵循 PR-004 · 注意 setValue 死循环防护（虽然此处不同字段安全 · 但如果你扩展逻辑联动同字段必须加）
- 遵循 PR-001 · 不继承标品 ChangeSceneEditPlugin · 并列挂

---

## CS-04 · 列表过滤定制：让用户在 UI 切换 otclassify

### 需求
业务方说："标品 customParam.otclassify 默认 1010L · 我们集团下 5 个事业群每个有自己的组织分类 · 要让 HR 在列表顶部下拉切换 · 不要硬写。"

### 推荐方案
- **扩展对象**：`haos_changescene`（列表表单）
- **扩展点**：`setFilter@haos_changescene`（FormPlugin 列表层 · 并列挂在 ChangeSceneListPlugin 之后）
- **实现模式**：并列挂插件 + 在表单顶部加下拉控件 → 选值传到 customParam
- **风险**：低（只影响列表过滤 · 不动数据）
- **关联 PR**：PR-001（不继承 ChangeSceneListPlugin · 它是场景特有类）

### 扩展入口坐标
- 绑定表单：`haos_changescene`（列表场景）
- 推荐父类：**`HRDataBaseList`**（HR 通用列表 · 白名单合规）
- ⚠ **不继承 ChangeSceneListPlugin · 不继承 ListOrderCommonPlugin（haos 域专属未确认是否在白名单）**

### 调用链
```
[用户在列表顶部下拉选 otclassify_filter]
  ↓
TdkwChangeSceneListFilterPlugin.afterBindData
  → 在 view 上加自定义控件（或用 BeforeF7SelectListener 接收选择）
  ↓ 用户选完
TdkwChangeSceneListFilterPlugin.click / propertyChanged
  → getView().getFormShowParameter().setCustomParam("otclassify", selectedId);
  → getView().refresh();
  ↓
列表刷新触发 setFilter
  → ChangeSceneListPlugin (标品) 拿到 customParam.otclassify · 按选中值过滤
  → 标品逻辑：otclassifyid != null · 走用户选的值 · 不走默认 1010
```

### 代码框架

**TdkwChangeSceneListFilterPlugin.java**：
```java
package com.kingdee.${ISV_FLAG}.haos.formplugin.web;

import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.events.AfterBindDataEvent;
import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 列表层过滤定制 · 让用户切换 otclassify
 * 并列挂在标品 ChangeSceneListPlugin 之后（RowKey 取大值）
 * 利用平台 customParam 透传机制 · 不重写 setFilter（避免跟标品过滤冲突）
 */
public class TdkwChangeSceneListFilterPlugin extends HRDataBaseList {

    private static final String FILTER_FIELD = "${ISV_FLAG}_otclassify_filter";

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        if (!FILTER_FIELD.equals(e.getProperty().getName())) {
            return;
        }
        DynamicObject otclassifyObj = (DynamicObject) this.getModel().getValue(FILTER_FIELD);
        if (otclassifyObj == null) {
            // 用户清空选择 · 回到标品默认 1010L
            this.getView().getFormShowParameter().setCustomParam("otclassify", null);
        } else {
            this.getView().getFormShowParameter().setCustomParam(
                "otclassify", otclassifyObj.getLong("id"));
        }
        this.getView().refresh();
    }
}
```

### 平台绑定步骤
1. modifyMeta 在列表表单 `haos_changescene` 上加一个 BasedataField 控件 `${ISV_FLAG}_otclassify_filter`（refEntity=haos_otclassify）· 不是数据字段 · 是搜索条件控件
2. 注册插件 `com.kingdee.${ISV_FLAG}.haos.formplugin.web.TdkwChangeSceneListFilterPlugin` · targetType=`LIST_FORM`（PR-007 配套规则 · 大写枚举）
3. RowKey 排在 ChangeSceneListPlugin 之后

### 踩坑
- ❌ 重写 `setFilter` → 跟 ChangeSceneListPlugin 的 setFilter 冲突 · 标品的 `id != 1070L` 等过滤可能丢失
- ❌ 把 customParam 写到 `getModel()` → 不对 · customParam 是 FormShowParameter 上的
- ❌ targetType 写小写 `list_form` → 平台静默忽略（10 条平台规则 #10）
- ❌ 没调 `getView().refresh()` → 列表不刷新 · 用户改了选择没反应
- ⚠ ${ISV_FLAG}_otclassify_filter 控件不是数据字段 · 不会落库 · 仅做 UI 控制
- 💡 也可以用 `setVisible(true, [${ISV_FLAG}_otclassify_filter])` 配合权限来按角色显示这个过滤器

### 关联 PR
- 遵循 PR-001 · 不继承 ChangeSceneListPlugin · 也不继承 ListOrderCommonPlugin · 改用 HRDataBaseList
- 遵循 PR-003 · FormPlugin 用 getModel().setValue / getView().refresh
- 遵循 10 条平台规则 #10 · targetType 必须大写枚举

---

## CS-05 · BEC 模式 · ⚠ 反指引（不要在本场景做）

### 需求（典型业务方提问）
"想让 haos_changescene 的修改/禁用动作通知到下游模块 · 比如薪酬 / 考勤的同步刷缓存 · 用 BEC 行不行？"

### 实证判定（铁律 5 + 6）

```bash
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_changescene/
# 0 处命中
```

**事实 1**：标品**没在本场景发任何 BEC 事件**。
**事实 2**：标品也**没在本场景做 BEC 订阅**。

### 反指引：为什么不做？

#### 不做 BEC 订阅方
- ❌ 订阅本场景的 BEC → 标品根本没发 · 啥都收不到
- ❌ 订阅其他场景的 BEC 来"间接管理" → 不该在字典层做 · 应在业务层 admin_org / homs_orgbatchchgbill 做

#### 不做 BEC 发布方
- ⚠ "ISV 自建发布方让下游订阅"在本场景**不推荐**：
  1. 基础资料变更频率低（一年改几次）· 没有"批量异步分发"的性能必要
  2. 下游若有需要直接查 t_haos_changescene 即可 · 没必要复制状态
  3. 标品本身没设计这条链路 · ISV 单边做会造成"半成品"（部分模块订阅 / 部分模块查表 · 不一致）

### 正确做法：业务变更通知应该挂在哪？

| 业务诉求 | 正确挂接位置 |
|---|---|
| 组织变动事件通知（如部门合并）| `homs_orgbatchchgbill` 的 `OrgBatchChgBillEffectOp.afterExecuteOperationTransaction`（标品发 BEC 实证 · 走 hjm 范式）|
| 行政组织变更通知 | `haos_adminorg` 的 admin_org_quick_maintenance CS-04 · 标品异步派单链 |
| 字典变更不应发独立通知 | 重新审视：业务真的需要通知"changescene 字典改了"么？多数情况下 · 业务真正关心的是"使用了某 changescene 的申请单生效了" · 那是 homs 的事 |

### 极少数例外：ISV 必须自建 BEC 发布方时

如果业务确实需要（如有外部系统订阅基础资料变更）· 走 hjm 标品范式：
1. 在【开发平台 → 业务事件管理】预注册 eventNumber（PR-011）
2. 在 ISV 加并列挂 OP 插件 · 重写 `afterExecuteOperationTransaction`（PR-010 第 9 步）
3. 调 `IEventService.triggerEventSubscribeJobs(...)` 异步发事件
4. 用 `kd.bos.id.ID.genStringId()` 生成 traceId 放到 variables（PR-005）

但 **CS-05 的核心反指引是：基础资料场景默认不做 BEC**。

### 关联 PR
- 遵循 PR-011 · BEC 走平台 · 不自接 MQ
- 遵循 PR-010 · 若极少数情况自建发布方 · 必须 afterExecuteOperationTransaction 阶段
- 遵循 PR-005 · 若发事件 · variables 内的 traceId 用 ID.genStringId

---

## CS-06 · 自建子表行 / 业务编码 · PR-005 实证

### 需求
业务方说："我们要在 CS-01 加的字段基础上 · 给每条 changescene 加一张'适用业务线明细'子表（多对多） · 每行需要 id 和业务编码。"

### 推荐方案
- **扩展点**：`modifyMeta(op=add entity)` 加子表 + ISV 插件操作子表行 id
- **关键约束**：行 id 必须用 `kd.bos.id.ID.genLongId()` · 不能 UUID / timestamp / max+1（PR-005）
- **关联 PR**：PR-005（ID 生成强制场景 · 自建子表行）

### 调用链（建子表 · OpenAPI）
```
modifyMeta({
  formId: "haos_changescene",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "entity",
    parentScope: "haos_changescene",
    element: {
      entityType: "EntryEntity",
      key: "${ISV_FLAG}_bizline_entry",
      name: {zh_CN: "适用业务线明细"}
    }
  }, {
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "${ISV_FLAG}_bizline_entry",
    element: {
      fieldType: "BasedataField",
      key: "${ISV_FLAG}_bizline_id",
      name: {zh_CN: "业务线"},
      refEntity: "${ISV_FLAG}_bizline_dict",
      mustInput: true
    }
  }, {
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "${ISV_FLAG}_bizline_entry",
    element: {
      fieldType: "TextField",
      key: "${ISV_FLAG}_biz_code",
      name: {zh_CN: "业务编码"}
    }
  }]
})
```

### 代码框架（往子表新增行 · ID.genLongId 实证）

**TdkwChangeSceneEntryAppendOp.java**：
```java
package com.kingdee.${ISV_FLAG}.haos.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.id.ID;                           // ⭐ PR-005 唯一 ID 来源
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * 保存时自动给 ${ISV_FLAG}_bizline_entry 子表的每行补 id 和 biz_code
 * RowKey 在标品 8 个保存插件之前（PR-002）
 */
public class TdkwChangeSceneEntryAppendOp extends HRDataBaseOp {

    @Override
    public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs e) {
        e.getFieldKeys().add("${ISV_FLAG}_bizline_entry");
        e.getFieldKeys().add("${ISV_FLAG}_bizline_entry.${ISV_FLAG}_bizline_id");
        e.getFieldKeys().add("${ISV_FLAG}_bizline_entry.${ISV_FLAG}_biz_code");
    }

    @Override
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        for (DynamicObject dataEntity : e.getDataEntities()) {
            DynamicObjectCollection entries =
                dataEntity.getDynamicObjectCollection("${ISV_FLAG}_bizline_entry");
            for (DynamicObject row : entries) {
                if (row.getLong("id") <= 0L) {
                    // ⭐ PR-005 · 子表行 id 用 ID.genLongId · 不能 UUID/timestamp/max+1
                    row.set("id", ID.genLongId());
                }
                if (kd.hr.hbp.common.util.HRStringUtils.isEmpty(row.getString("${ISV_FLAG}_biz_code"))) {
                    // ⭐ PR-005 · 业务编码用 ID.genStringId
                    String prefix = "BL_";
                    row.set("${ISV_FLAG}_biz_code", prefix + ID.genStringId());
                }
            }
        }
    }
}
```

### 踩坑
- ❌ `row.set("id", UUID.randomUUID())` → UUID 跟苍穹分布式 ID 体系不兼容（PR-005）
- ❌ `row.set("id", System.currentTimeMillis())` → 并发碰撞 + 跟平台 ID 区段冲突
- ❌ `row.set("id", maxId + 1)`（select max+1）→ 并发安全问题 + 性能差
- ❌ 用 `ID.genLongId()` 给业务编码（应该用 `genStringId` · 长字符串带前缀更易读）
- ❌ `onPreparePropertys` 里没声明 `${ISV_FLAG}_bizline_entry` → beginOperationTransaction 拿到的 collection 是 null
- ⚠ 在 FormPlugin 层操作子表行新增也要用 `ID.genLongId()`：
  ```java
  // FormPlugin 例子 · CS-01 加新字段后批量增行
  DynamicObjectCollection entries = (DynamicObjectCollection) getModel().getValue("${ISV_FLAG}_bizline_entry");
  DynamicObject row = entries.addNew();
  row.set("id", ID.genLongId());  // PR-005 · 不要靠平台默认（部分场景默认 0L 导致重复）
  row.set("${ISV_FLAG}_bizline_id", bizLineId);
  ```

### 关联 PR
- 遵循 PR-005 · 子表行 id 用 ID.genLongId · 业务编码用 ID.genStringId
- 遵循 PR-001 · 并列挂 · 不继承 ChangeSceneSaveOp
- 遵循 PR-002 · RowKey 早于标品 8 插件 · 在它们之前补好 id

---

## CS 关联 Pattern 速查

| CS | 关联 Pattern | 作用 |
|---|---|---|
| CS-01 | `add_field_extension` | 加 ISV 字段标准模式 |
| CS-02 | `add_unique_validation` + 反向引用查询模式 | 删除前置校验 · 跨表 QFilter |
| CS-03 | `override_plugin_behavior`（并列挂版）| 字段联动追加 |
| CS-04 | `customparam_through_form_plugin` | 列表过滤 · 不重写 setFilter |
| CS-05 | （反指引 · 无）| 基础资料场景不做 BEC |
| CS-06 | `add_sub_entity` + `pr005_id_generation` | 子表 + ID 生成铁律 |

---

## 被引用·成建制划转（业务流型聚合）

> ⚡ 本场景作为下游被引用方·在 `core_hr_org_unit_transfer` 业务流中扮演角色。

### 引用方
- **业务流场景**：[`core_hr_org_unit_transfer`](../core_hr_org_unit_transfer/) (HR 核心人力 / hdm 调配管理)
- **完整可复刻资产**：[`_assets/org_unit_transfer/`](../../_assets/org_unit_transfer/)

### 引用关系
成建制划转任务 A/B/C 通过 `changescene.number` 字段过滤变动事件 (1020_S/1030_S/1080_S)·即本场景定义的编码。

### 部署后的影响
当客户部署成建制划转资产 (`_assets/org_unit_transfer/`)·调度任务每日跑会**消费本场景的标品数据** (查询变动场景编码字典)·然后批量装配标品调动单 `hdm_transferbatch` + `SUBMITEFFECT` 触发跨云协同。

### 跟本场景 ISV 扩展的关系
- ✅ 客户在本场景做 ISV 扩展（如 CS-01 加字段 / CS-02 反向引用查询）·**不影响**成建制划转资产消费
- ⚠️ 客户在本场景**改字段命名规范**（如改某字段 key）·**必须**同步检查 `core_hr_org_unit_transfer` 是否引用·避免破坏跨场景集成
