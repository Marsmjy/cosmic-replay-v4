# 定制化方案集 · hsbs_salaryitem

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（按 mines + deep_resolve 推可做/不可做）
> **数据源**: `rules_chain_all.json::mines` · `_deep_resolve_index.json` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py（v5.1 render_06）

## ✅ 可做（ISV 扩展路径）

### 1. 在主表加 ISV 扩展字段

主表 `hsbs_salaryitem` 当前 49 个字段。
ISV 扩展元数据（建独立 ISV form 用 `_inherits` 引用主表）→ 加业务字段·不动标品。

```bash
# 1. 通过 OpenAPI buildMeta 建 ISV 扩展元数据
# 2. parentId=<hsbs_salaryitem formId>
# 3. 字段命名加 ISV 前缀（避免和未来标品冲突）
```

### 2. 继承标品 OP 类加扩展逻辑（6 个可继承类）

| FQN | opKey | 可重写生命周期方法 |
|---|---|---|
| `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` | `save` | afterBindData, afterLoadData, beforeDoOperation, afterDoOperation, preOpenForm |
| `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin` | `save` | afterBindData |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | `save` | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | `save` | beforeExecuteOperationTransaction, beginOperationTransaction, afterExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | `save` | beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | `save` | onPreparePropertys, beforeExecuteOperationTransaction |

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

### 3. 调用 SDK 桶服务（6 个白名单服务）

详见 `07_ext_points.md` 的 `curated_sdk 9 桶` 段。

## ❌ 不可做（ISV 雷区）

反编译实证抽出 **5 条** ISV 红线（去重后）：

1. ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
2. ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
3. ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
4. ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）
5. ❌ 禁继承 AbsOrgBaseOp（非 HR 通用推荐 · 用 HRDataBaseOp 代替）

### 禁继承类（共 3 个）

- `HRBaseDataImportEdit` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRBaseDataTplList` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- `HRBasedataLogList` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`

**原因**：这些类带 `@SdkInternal` 或 `@SdkPlugin(role=internal)` 注解 · ISV 不得继承。

## ℹ️ 标品已接入插件（40 个 · 详见 07_ext_points.md）

- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`
- `BdCtrlStrtgyShowLogicPlugin` ← `kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicPlugin`
- `BaseDataCreateOrgPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataCreateOrgPlugin`
- `BaseDataFormPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataFormPlugin`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- ...（共 40）

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
> 生成时间：2026-05-02 · 数据源 cs_drafts/hsbs_salaryitem/

---

## CS-01 · 给薪酬项目加 ISV 业务属性字段（最高频）

### 需求
业务方说：薪酬项目要新增「成本中心维度 / 是否计税基数 / 是否社保基数 / 业务类别标签」等扩展属性·支持企业级薪酬体系定制。

### 推荐方案
- **扩展对象**: `hsbs_salaryitem`（薪酬项目主表 · BaseFormModel）
- **扩展点**: `modifyMeta(op=add field)`
- **风险**: 低

### 调用链
```
Step 1: getDevInfo()                        // 拿 ISV 信息
Step 2: getBizApps()                        // bizAppId = /UHMBBGZQ65X (薪资核算)
Step 3: modifyMeta({
  formId: "hsbs_salaryitem",
  ops: [{
    op: "add", treeType: "entity", elementType: "field",
    parentScope: "hsbs_salaryitem",
    element: {
      fieldType: "CheckBoxField",
      key: "${ISV_FLAG}_istaxbase",
      name: {zh_CN: "是否计税基数", en_US: "Is Tax Base"},
      mustInput: false
    }
  }]
})
Step 4: getFormSchema("hsbs_salaryitem")    // ⭐ 二次验证落库
```

### 代码框架
```python
from cosmic_devportal_client import CosmicClient
client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(target_form_info={"number": "hsbs_salaryitem"})
designer.add_field(field_type="CheckBoxField", name="是否计税基数", key="${ISV_FLAG}_istaxbase",
                   parent_entity_id=designer.base_entity_id)
designer.save()
```

### 踩坑
- ❌ 字段 key 不带 `${ISV_FLAG}_` 前缀 → 标品升级被覆盖
- ❌ 引用基础数据用 `BasedataField` + `refEntity` · 不要用 ISV 自建的 EmployeeField 类型
- ❌ 扩展字段做计算公式参与方·必须同步给 `hcdm_salarystandard` 加同名字段·否则薪酬标准表无法引用
- 💡 加完字段后调 `pdmConvert + pdmSave + pdmSubmit` 三步落库到物理表 · 不然 select 不到

---

## CS-02 · 加自定义 Validator（薪酬项编码规则 / 重复校验）

### 需求
ISV 业务规则：薪酬项目的 `number`（项目编码）必须满足公司编码规范（如 SAL-XXX-NNN）·否则不允许保存。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` 在 `onAddValidators` 注册
- **opKey**: `save`
- **风险**: 低

### 调用链
```
ISV plugin 注册 → save opKey 触发 → HRBaseDataStatusOp.onAddValidators(super)
                    → ISV.onAddValidators(自定义)
                    → SalaryItemNumberValidator.validate
                    → 不通过 → addErrorMessage → 阻断保存
```

### 代码框架
```java
package ${ISV_FLAG}.swc.salaryitem.opplugin;

import kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.ExtendedDataEntity;
import java.util.regex.Pattern;

public class TdkwSalaryItemNumberValidatorOp extends HRBaseDataStatusOp {
    private static final Pattern CODE_PATTERN = Pattern.compile("^SAL-[A-Z]{3}-\\d{3}$");

    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new SalaryItemNumberValidator());
    }

    static class SalaryItemNumberValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (ExtendedDataEntity ext : this.dataEntities) {
                String number = (String) ext.getValue("number");
                if (number != null && !CODE_PATTERN.matcher(number).matches()) {
                    this.addErrorMessage(ext, "项目编码必须满足 SAL-XXX-NNN 格式 · 当前值: " + number);
                }
            }
        }
    }
}
```

### 注册插件
```python
client.register_plugin(formId="hsbs_salaryitem", operationKey="save",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.swc.salaryitem.opplugin.TdkwSalaryItemNumberValidatorOp")
```

### 踩坑
- ❌ 在 `beforeExecuteOperationTransaction` 注册 Validator → 太晚（mines 实证）
- ❌ targetType 写小写 'operation' → registerPlugin 报错（v5.1 R20 实证）
- ❌ 忘调 super.onAddValidators(e) → 标品 NumberValidator / EnableValidator 全失效
- 💡 用 `addErrorMessage` 而非 `throw` · 让平台收集所有错误一次报给用户

---

## CS-03 · 订阅薪酬项启用 / 禁用 BEC 事件（自动同步下游算薪表）

### 需求
ISV 业务规则：薪酬项目启用 / 禁用 / 审核 时·自动同步触发：① 通知薪酬标准表（hcdm_salarystandard）刷新缓存·② 推送给 ISV 自建的薪酬规则引擎重建索引。

### 推荐方案
- **扩展点**: BEC 事件订阅（hsbs_standarditem.* 是 swc 云**唯一**有 BEC 发布的 form 群·实证 evt_event 9 条）
- **关键事件**: `hsbs_standarditem.audit / enable / disable / submit`（已实证）
- **目标 form 关联**: hsbs_salaryitem 是定调薪项目（hsbs_standarditem）的下级·订阅其事件即可
- **风险**: 低（标品 BEC 已经做了 retry / 幂等 / 死信队列）

### 调用链
```
hsbs_standarditem.audit (审核) commit
  → BEC publish "hsbs_standarditem.audit" 事件
  → ISV 订阅匹配 → 派 sch_task / 直调 ISV plugin (executePlugin 类型)
  → ISV plugin: 刷下游算薪表 / 推规则引擎
```

### 代码框架（订阅类）
```java
package ${ISV_FLAG}.swc.salaryitem.event;

import kd.bos.eventbus.event.EventListener;
import kd.bos.eventbus.event.IEventTask;
import kd.bos.eventbus.event.KdBizEvent;

public class TdkwSalaryItemAuditEventListener implements IEventTask {
    @Override
    public void execute(KdBizEvent event) {
        String evtNumber = event.getEventNumber();
        Long itemId = (Long) event.getBusinessKey();

        if ("hsbs_standarditem.audit".equals(evtNumber)) {
            SalaryRuleEngineHelper.rebuildIndex(itemId);          // 推 ISV 规则引擎
            SalaryStandardCacheHelper.invalidate(itemId);         // 刷 hcdm_salarystandard 缓存
        }
    }
}
```

### 注册订阅
```python
# 通过事件订阅菜单（BEC menu 0P2GF3HZ0Z0T）配置 evt_subscription
# 或 OpenAPI registerSubscription（SDK 提供）
client.register_subscription(
    eventNumber="hsbs_standarditem.audit",
    serviceType="executePlugin",
    serviceConfig={"className": "${ISV_FLAG}.swc.salaryitem.event.TdkwSalaryItemAuditEventListener"}
)
```

### 踩坑
- ❌ 自己监听 form save 事件而不订阅 BEC → 失去标品的失败重试 / 幂等 / 死信
- ❌ 在 listener 内同步调外部接口（5s 延迟）→ 阻塞事件队列 · 必须异步或扔 sch_task
- ❌ 不处理 `unaudit / disable` 事件 → 删除场景下 ISV 缓存脏数据
- 💡 BEC 事件对 wtc / 其他 swc 主表都没有发布（只 hsbs_standarditem 系列发）·想全云覆盖订阅得自建监听机制
- 💡 同一事件多个订阅 · 顺序不保证 · 不要 ISV listener 之间相互依赖

---

## CS-04 · 扩展薪酬项计算公式（接 ISV 自定义函数）

### 需求
ISV 业务方需要在薪酬项的计算公式中调用自定义函数：如 `calcBonus(empId, period)` 返回员工某期间的奖金计算结果·标品没有这个函数。

### 推荐方案
- **扩展点**: 实现苍穹公式引擎的 `IFormulaFunction` 接口·注册到平台公式函数库
- **opKey**: 不直接绑特定 opKey · 公式引擎自动加载
- **风险**: 中（涉及公式 DSL · 性能要求高）

### 调用链
```
HR 在薪酬项「计算公式」字段输入: ${ISV_FLAG}_calcBonus(empId, period)
  → 公式引擎解析 → 找 ${ISV_FLAG}_calcBonus 函数注册
  → 调 ISV.TdkwCalcBonusFunction.execute(empId, period)
  → 返回 BigDecimal → 公式引擎拼装最终值
```

### 代码框架
```java
package ${ISV_FLAG}.swc.salaryitem.formula;

import kd.bos.formula.FormulaEngine;
import kd.bos.formula.IFormulaFunction;
import kd.bos.formula.FormulaContext;
import java.math.BigDecimal;

public class TdkwCalcBonusFunction implements IFormulaFunction {
    @Override
    public String getName() {
        return "${ISV_FLAG}_calcBonus";
    }

    @Override
    public Object execute(FormulaContext ctx, Object... args) {
        Long empId = ((Number) args[0]).longValue();
        String period = (String) args[1];

        // ISV 业务逻辑：查 empId 在 period 的考核结果 → 算奖金
        BigDecimal score = AssessmentHelper.getScore(empId, period);
        BigDecimal baseBonus = SalaryHelper.getBaseBonus(empId);
        return baseBonus.multiply(score).divide(BigDecimal.valueOf(100));
    }
}

// 注册（spring config 或 ServiceLoader）
public class FormulaFunctionRegister {
    @PostConstruct
    public void register() {
        FormulaEngine.registerFunction(new TdkwCalcBonusFunction());
    }
}
```

### 踩坑
- ❌ 函数 name 不带 ISV 前缀（如直接叫 `calcBonus`）→ 标品升级时引入同名函数会冲突
- ❌ 函数内部抛 RuntimeException → 整个公式跑挂 · 应该 try-catch 返回 0 或 null
- ❌ 公式函数内做大量 DB 查询 → 算薪 1000 人 × 每人触发 N 次 → DB 爆 · 必须缓存
- 💡 公式函数最好 stateless · 不依赖线程上下文 · 苍穹公式引擎可能并发跑
- 💡 测试用 `FormulaEngine.execute("${ISV_FLAG}_calcBonus(123, '202604')")` 单测 · 不依赖整个薪酬流程

---

## CS-05 · 加数据规则按法律实体隔离薪酬项

### 需求
集团有多家子公司·HR 看不同子公司只能看自己的薪酬项·不能看其他公司。

### 推荐方案
- **扩展点**: 走标品数据规则机制（`hrcs_datarule` + `hrcs_dimension`）· 不写代码
- **关键 form**: `hrcs_datarule`（数据规则配置）/ `hrcs_dimension`（维度管理）
- **风险**: 低（标品已支持·配置即可）

### 调用链
```
HR 配置 → hrcs_dimension 定义"法律实体维度"
       → hrcs_datarule 配薪酬项按法律实体过滤
HR 用户登录 → hsbs_salaryitem 列表查询
       → 平台权限引擎 → 注入 datarule SQL 条件 → WHERE lawentity = 当前用户主权限实体
       → 返回过滤后数据
```

### 配置步骤
```python
# 1. 在 hrcs_dimension 加维度（如已有"法律实体"则跳过）
# 2. 在 hrcs_datarule 配置规则
client.create_data_rule(
    formId="hsbs_salaryitem",
    dimensionKey="lawentity",                  # 维度
    ruleType="filter_by_user_perm",            # 按用户权限过滤
    description="薪酬项目按法律实体隔离"
)

# 3. 用户授权（hrcs_permfilelist）
# 给用户授权"广州公司"法律实体 → 该用户只能看 lawentity=广州公司 的薪酬项
```

### 代码扩展（如配置不够）
如果业务规则复杂到配置不足·继承 `kd.hr.hrcs.business.permission.IDataRuleProvider`：
```java
package ${ISV_FLAG}.swc.perm;

import kd.hr.hrcs.business.permission.IDataRuleProvider;
import kd.hr.hrcs.business.permission.DataRuleContext;

public class TdkwSalaryItemDataRule implements IDataRuleProvider {
    @Override
    public String getFilterSql(DataRuleContext ctx) {
        // 复杂规则：管理员看所有 + 普通用户按法律实体看
        if (ctx.isAdmin()) return "1=1";
        Long lawEntityId = ctx.getCurrentLawEntity();
        return "lawentity = " + lawEntityId;
    }
}
```

### 踩坑
- ❌ 自己写 SQL 拼条件而不用标品 dataRule → 跨表查询时漏过滤·数据泄漏
- ❌ 数据规则没配·薪酬项目对所有 HR 全可见 → 合规事故
- ❌ 维度选错（如选了员工维度而非法律实体维度）→ 过滤逻辑错乱
- 💡 标品数据规则在所有 HR 域 form 通用·配置一次全云生效（参考 hr_hrmp 已建 11 hrcs 场景）
- 💡 关联场景见 `hrcs_datarule` / `hrcs_dimension` / `hrcs_permfilelist` 完整 11md

