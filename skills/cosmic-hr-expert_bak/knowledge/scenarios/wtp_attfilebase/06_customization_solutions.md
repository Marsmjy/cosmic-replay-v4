# 定制化方案集 · wtp_attfilebase

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（按 mines + deep_resolve 推可做/不可做）
> **数据源**: `rules_chain_all.json::mines` · `_deep_resolve_index.json` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py（v5.1 render_06）

## ✅ 可做（ISV 扩展路径）

### 1. 在主表加 ISV 扩展字段

主表 `wtp_attfilebase` 当前 73 个字段。
ISV 扩展元数据（建独立 ISV form 用 `_inherits` 引用主表）→ 加业务字段·不动标品。

```bash
# 1. 通过 OpenAPI buildMeta 建 ISV 扩展元数据
# 2. parentId=<wtp_attfilebase formId>
# 3. 字段命名加 ISV 前缀（避免和未来标品冲突）
```

### 2. 继承标品 OP 类加扩展逻辑（9 个可继承类）

| FQN | opKey | 可重写生命周期方法 |
|---|---|---|
| `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` | `save` | afterBindData, afterLoadData, beforeDoOperation, afterDoOperation, preOpenForm |
| `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin` | `save` | afterBindData |
| `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin` | `save` | beforeBindData, afterCreateNewData, afterLoadData, afterBindData, beforeDoOperation |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | `save` | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | `save` | beforeExecuteOperationTransaction, beginOperationTransaction, afterExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | `save` | beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | `save` | onPreparePropertys, beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | `save` | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction, beginOperationTransaction, afterExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | `save` | onPreparePropertys, onAddValidators |

**继承套路**：
```java
public class XxxIsvOp extends [上面任一可继承类] {
    @Override
    protected void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new MyIsvValidator());
    }
}
```

注册插件时 `targetType` 必须大写枚举（OPERATION / BILL_FORM / LIST_FORM 之一）。

### 3. 调用 SDK 桶服务（9 个白名单服务）

详见 `07_ext_points.md` 的 `curated_sdk 9 桶` 段。

## ❌ 不可做（ISV 雷区）

反编译实证抽出 **5 条** ISV 红线（去重后）：

1. ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
2. ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
3. ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
4. ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）
5. ❌ 禁继承 AbsOrgBaseOp（非 HR 通用推荐 · 用 HRDataBaseOp 代替）

### 禁继承类（共 7 个）

- `HRBaseDataImportEdit` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRBaseDataTplList` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- `HRBasedataLogList` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
- `HisModelFilterPanelListPlugin` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
- `HisModelFilterPanelF7ListPlugin` (OP_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
- `HisModelBatchImportPlugin` (OTHER) ← `kd.hr.hbp.formplugin.web.history.impt.HisModelBatchImportPlugin`
- `HisBaseDataF7FastFilter` (OTHER) ← `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`

**原因**：这些类带 `@SdkInternal` 或 `@SdkPlugin(role=internal)` 注解 · ISV 不得继承。

## ℹ️ 标品已接入插件（44 个 · 详见 07_ext_points.md）

- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `HisModelFormCommonPlugin` ← `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
- `WTCTipsPlugin` ← `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`
- `WTCPresetEdit` ← `kd.wtc.wtbs.formplugin.web.WTCPresetEdit`
- ...（共 44）

## 🚨 决策金字塔（按金蝶 PPT 04 沉淀）

1. **首选标品配置** · 看是否能用元数据 validations / formRules 解决
2. **次选 ISV 扩展元数据** · 加字段 / 加规则 不动标品
3. **再选 ISV OP 插件继承** · 看上面「可继承类」清单
4. **末选 ISV 自建 form** · 无标品可继承时

**禁忌**：不要用 ISV form 直接覆盖标品 form（会丢标品升级红利）

---

# 真 CS 方案集（v5.2 · 实证手写）

> 以下 5 个 CS 来自 `_deep_resolve_index.json` 反编译 + `rules_chain_all.json` opKey 真证据。
> 每个 CS 五段式：背景 → 扩展点 → 调用链 → 代码框架 → 踩坑。
> 生成时间：2026-05-02 · 数据源 cs_drafts/wtp_attfilebase/

---

## CS-01 · 给考勤档案加 ISV 扩展字段（最高频）

### 需求
业务方说：考勤档案要新增「考勤区域 / 考勤组归属 / 考勤特别说明」等业务字段·按地域 / 班组维度做考勤策略划分。

### 推荐方案
- **扩展对象**: `wtp_attfilebase` (考勤档案主表 · BaseFormModel)
- **扩展点**: `modifyMeta(op=add field)`
- **风险**: 低
- **⚠️ 时态字段警告**: 不要碰 `boid / iscurrentversion / hisversion / bsed / bsled` 这 5 个时态字段·HisModel 平台维护

### 调用链（4 步）
```
Step 1: getDevInfo()                        // 拿 ISV 信息
Step 2: getBizApps()                        // 找 bizAppId（日常考勤=15=TGRTUNG1B）
Step 3: modifyMeta({
  formId: "wtp_attfilebase",
  ops: [{
    op: "add", treeType: "entity", elementType: "field",
    parentScope: "wtp_attfilebase",
    element: {
      fieldType: "TextField",
      key: "${ISV_FLAG}_attregion",
      name: {zh_CN: "考勤区域", en_US: "Attendance Region"},
      mustInput: false,
      maxLength: 50
    }
  }]
})
Step 4: getFormSchema("wtp_attfilebase")    // ⭐ 二次验证落库
```

### 代码框架
```python
from cosmic_devportal_client import CosmicClient
client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(target_form_info={"number": "wtp_attfilebase"})
designer.add_field(field_type="TextField", name="考勤区域", key="${ISV_FLAG}_attregion",
                   parent_entity_id=designer.base_entity_id)
designer.save()
```

### 踩坑
- ❌ 字段 key 不带 ISV 前缀 → 标品升级被覆盖
- ❌ 直接改时态字段（boid/iscurrentversion/bsed/bsled）→ HisModel 平台维护·会出现历史版本错乱
- ❌ 引用考勤组用 `BasedataField` + `refEntity` · 不要用 `HrEmployeeField`（OpenAPI 74 值枚举不支持）
- 💡 加字段后必须给历史已生效档案做数据回填（HisModel 不会自动追溯）

---

## CS-02 · 加自定义 Validator（在保存前阻断业务非法状态）

### 需求
ISV 业务规则：考勤档案的 `mode`（考勤方式）字段如果选了「打卡」·则必须提供 `card`（考勤卡号）·否则不允许保存。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` 在 `onAddValidators` 注册自定义 Validator
- **opKey**: `save`
- **风险**: 低
- **⚠️ 阶段铁律**: 必须在 `onAddValidators` 阶段注册 · 不能在 `beforeExecute` 阶段（来源：`rules_chain_all.json` mines 实证）

### 调用链
```
ISV plugin 注册 → save opKey 触发 → HRBaseDataStatusOp.onAddValidators(super)
                    → ISV.onAddValidators(自定义)  // 跑你的 Validator
                    → BaseValidator.validate(extendedDataEntities)
                    → 不通过 → addErrorMessage → 阻断保存
```

### 代码框架
```java
package ${ISV_FLAG}.wtc.attfile.opplugin;

import kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.ExtendedDataEntity;

public class TdkwAttFileCardValidatorOp extends HRBaseDataStatusOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);  // ⭐ 必调 super · 保留标品逻辑
        e.addValidator(new AttFileCardValidator());
    }

    static class AttFileCardValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (ExtendedDataEntity ext : this.dataEntities) {
                String mode = (String) ext.getValue("mode");
                String card = (String) ext.getValue("card");
                if ("CLOCK".equals(mode) && (card == null || card.isEmpty())) {
                    this.addErrorMessage(ext, "考勤方式为打卡时·考勤卡号必填");
                }
            }
        }
    }
}
```

### 注册插件
```python
# OpenAPI registerPlugin
client.register_plugin(
    formId="wtp_attfilebase",
    operationKey="save",
    pluginType="OPERATION",     # ⚠ 大写枚举·不是 'op' / 'operation'
    targetType="OPERATION",     # 同上
    className="${ISV_FLAG}.wtc.attfile.opplugin.TdkwAttFileCardValidatorOp"
)
```

### 踩坑
- ❌ 在 `beforeExecuteOperationTransaction` 注册 Validator → 太晚·标品已经走完业务规则（来源 mines）
- ❌ targetType 写小写 'operation' → registerPlugin 报 `targetType invalid`（v5.1 R20 实证）
- ❌ 忘调 super.onAddValidators(e) → 标品 NumberValidator / EnableValidator 全失效
- 💡 抛 `addErrorMessage` 而非 `throw RuntimeException` · 让平台收集错误信息（mines 推荐）

---

## CS-03 · 加保存后业务联动（写下游表 / 触发通知）

### 需求
考勤档案保存成功后·业务方要：① 同步写一份到 ISV 自建的「考勤策略表」·② 推送钉钉通知给员工。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` 重写 `afterExecuteOperationTransaction`
- **opKey**: `save`
- **风险**: 中（涉及 TX 边界）
- **⚠️ TX 边界铁律**: `afterExecuteOperationTransaction` 在主 TX 提交后跑·写下游表必须用新 TX

### 调用链
```
save opKey commit 后:
  HRBaseDataLogOp.afterExecuteOperationTransaction(super)
    → ISV.afterExecuteOperationTransaction(自定义)
       → 写 ISV.${ISV_FLAG}_attstrategy 表（新 TX）
       → 调钉钉 OpenAPI 推通知
```

### 代码框架
```java
package ${ISV_FLAG}.wtc.attfile.opplugin;

import kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

public class TdkwAttFileSyncStrategyOp extends HRBaseDataLogOp {
    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        for (DynamicObject obj : e.getDataEntities()) {
            // 1. 写 ISV 策略表（新 TX）
            DynamicObject strategy = BusinessDataServiceHelper.newDynamicObject("${ISV_FLAG}_attstrategy");
            strategy.set("attfile", obj.getPkValue());
            strategy.set("region", obj.getString("${ISV_FLAG}_attregion"));
            SaveServiceHelper.save(new DynamicObject[]{strategy});

            // 2. 推钉钉通知（异步·不阻塞）
            DingTalkHelper.notifyAsync(obj.getDynamicObject("creator").getPkValue(),
                                       "您的考勤档案已生效");
        }
    }
}
```

### 注册插件
```python
client.register_plugin(formId="wtp_attfilebase", operationKey="save",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.wtc.attfile.opplugin.TdkwAttFileSyncStrategyOp")
```

### 踩坑
- ❌ 在 `beforeExecuteOperationTransaction` 写下游表 → 主表抛异常 → 主 TX 回滚但下游表写了·脏数据
- ❌ `afterExecuteOperationTransaction` 内 throw 异常 → 主 TX 已提交·回滚不了主表（mines 实证）
- ❌ 同步调钉钉 OpenAPI → 钉钉接口 5s 延迟 → save 操作前端转圈 5s · 必须异步
- 💡 异常用 `LogServiceHelper.error(...)` 记日志 · 不要让用户看到（保存成功了·钉钉失败是次要事故）

---

## CS-04 · 接收人员变动协作（实现 EmployeeCoordService）

### 需求
核心人力发生人员变动（入职 / 离职 / 调动）时·考勤档案需要自动同步：
- 新入职 → 自动建考勤档案（默认班次 = 标准班）
- 离职 → 禁用考勤档案（不删·保留考勤历史）
- 调岗 → 更新考勤档案的 `period`（考勤周期）

### 推荐方案
- **扩展点**: 在 ISV 自建实现类继承 `kd.wtc.wtp.business.newcoordination.WtcEmployeeCoordTask`（v5.1 collab L7 实证锚点）
- **协作接口**: `kd.hr.hbp.business.coordination.api.EmployeeCoordService`
- **触发链**: `core_hr_change_collab_attendance` collab 场景的 BEC 订阅 → `wtp_WtcEmployeeCoordTask_SKDJ_S` 调度作业 → `WtcEmployeeCoordTask` 真处理类
- **风险**: 中-高（跨云协作 · 失败要补偿）

### 调用链
```
core_hr 员工保存(save) → BEC 发员工变动事件
  → WtcEmployeeCoordEventServicePlugin (订阅) 收到
  → 派 sch_task: wtp_WtcEmployeeCoordTask_SKDJ_S
  → WtcEmployeeCoordTask.execute(标品基类)
    → ISV.execute (覆盖) → 写 wtp_attfilebase
```

### 代码框架
```java
package ${ISV_FLAG}.wtc.attfile.coord;

import kd.hr.hbp.business.coordination.api.EmployeeCoordService;
import kd.wtc.wtp.business.newcoordination.WtcEmployeeCoordTask;

public class TdkwAttFileCoordImpl implements EmployeeCoordService {
    @Override
    public void execute(EmployeeCoordEvent event) {
        switch (event.getChangeType()) {
            case ENTRY:    // 入职
                createAttFile(event.getEmployee());
                break;
            case EXIT:     // 离职
                disableAttFile(event.getEmployee());
                break;
            case TRANSFER: // 调岗
                updatePeriod(event.getEmployee(), event.getNewOrg());
                break;
        }
    }

    private void createAttFile(EmployeeInfo emp) {
        DynamicObject attFile = BusinessDataServiceHelper.newDynamicObject("wtp_attfilebase");
        attFile.set("number", "AT-" + emp.getEmployeeNumber());
        attFile.set("mode", "CLOCK");                    // 默认打卡
        attFile.set("period", getDefaultPeriod());       // 默认考勤周期
        attFile.set("bsed", emp.getEntryDate());         // 生效日期 = 入职日
        SaveServiceHelper.save(new DynamicObject[]{attFile});
    }
    // ... disableAttFile / updatePeriod 同样用 HisModel API
}
```

### 踩坑
- ❌ 不实现 EmployeeCoordService · 自己监听 BEC → 失去标品的失败重试 / 幂等 / 协作进度展示
- ❌ 在 execute() 抛异常但不处理 → 标品会无限重试·拖死 sch_task
- ❌ 直接 `new DynamicObject` 不走 HisModel 时态接口 → boid/iscurrentversion 字段错乱（CS-01 同款雷区）
- 💡 一定要用 `event.getEmployeeAtEffectiveDate()` 拿入职日的员工状态 · 不能用 getEmployee().getCurrent()（事件可能延迟到达·当前状态可能是后续状态）
- 💡 失败补偿走 collab 场景的 retry 机制 · 不要自建（标品已经做了 3 次重试 + 死信队列·参考 collab_attendance L7 段）

---

## CS-05 · 加列表批量改字段（按筛选条件批改）

### 需求
HR 想批量把某个组织下所有员工的考勤档案 `period` 字段统一改成「月度周期」。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- **新增 opKey**: `${ISV_FLAG}_batchchgperiod`（自定义 op·不污染 save）
- **风险**: 中（涉及大批量 + HisModel 时态写）

### 调用链
```
列表页选中行 → 点「批改考勤周期」按钮 → 触发 ${ISV_FLAG}_batchchgperiod opKey
  → 弹窗选新 period → 提交
  → ISV.beforeExecuteOperationTransaction:
       for 每行 in selectedRows:
           调 HisModel.disable(old version)
           new version = clone(old) with period=新值
           HisModel.save(new version)
```

### 代码框架
```java
package ${ISV_FLAG}.wtc.attfile.formplugin;

import kd.hr.hbp.formplugin.web.template.HRBaseDataTplList;
import kd.bos.entity.plugin.args.BeforeOperationArgs;

public class TdkwAttFileBatchChgList extends HRBaseDataTplList {
    @Override
    public void beforeDoOperation(BeforeOperationArgs args) {
        super.beforeDoOperation(args);
        if ("${ISV_FLAG}_batchchgperiod".equals(args.getOperationKey())) {
            BatchChgPeriodHandler.handle(this.getSelectedRows(), getNewPeriod());
        }
    }
}

class BatchChgPeriodHandler {
    static void handle(SelectedRows rows, Long newPeriodId) {
        // 用 HisModel API · 不直接 SaveServiceHelper
        for (Long rowId : rows.getPrimaryKeyValues()) {
            HisModelHelper.changeEffective("wtp_attfilebase", rowId, "period", newPeriodId,
                                           new Date());  // 立即生效
        }
    }
}
```

### 注册新 opKey
```python
# 1. addOperation 加 opKey
client.add_operation(formId="wtp_attfilebase",
                     opKey="${ISV_FLAG}_batchchgperiod",
                     opType="custom",
                     name={"zh_CN": "批改考勤周期"})

# 2. registerPlugin 绑插件
client.register_plugin(formId="wtp_attfilebase",
                       operationKey="${ISV_FLAG}_batchchgperiod",
                       pluginType="OPERATION",
                       targetType="LIST_FORM",   # ⚠ 列表场景必须 LIST_FORM
                       className="${ISV_FLAG}.wtc.attfile.formplugin.TdkwAttFileBatchChgList")
```

### 踩坑
- ❌ 直接 `SaveServiceHelper.save()` 而非走 HisModel API → 历史版本错乱（boid 同·iscurrentversion 多个 true）
- ❌ targetType 写 OPERATION（应该 LIST_FORM）→ 列表页选不到批改按钮
- ❌ 一次 1000+ 行 → ORM 内存爆 · 必须分页 200/批
- 💡 批改要用 HisModel 时态 API（`HisModelHelper.changeEffective`）· 老版本自动 bsled 截断 · 新版本自动 iscurrentversion=true · 全部原子（参考 wtbd_workschedule 时态用法）

