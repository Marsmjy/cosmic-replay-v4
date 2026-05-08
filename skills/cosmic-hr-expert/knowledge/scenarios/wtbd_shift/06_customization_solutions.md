# 定制化方案集 · wtbd_shift

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（按 mines + deep_resolve 推可做/不可做）
> **数据源**: `rules_chain_all.json::mines` · `_deep_resolve_index.json` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py（v5.1 render_06）

## ✅ 可做（ISV 扩展路径）

### 1. 在主表加 ISV 扩展字段

主表 `wtbd_shift` 当前 76 个字段。
ISV 扩展元数据（建独立 ISV form 用 `_inherits` 引用主表）→ 加业务字段·不动标品。

```bash
# 1. 通过 OpenAPI buildMeta 建 ISV 扩展元数据
# 2. parentId=<wtbd_shift formId>
# 3. 字段命名加 ISV 前缀（避免和未来标品冲突）
```

### 2. 继承标品 OP 类加扩展逻辑（10 个可继承类）

| FQN | opKey | 可重写生命周期方法 |
|---|---|---|
| `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` | `save` | afterBindData, afterLoadData, beforeDoOperation, afterDoOperation, preOpenForm |
| `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin` | `save` | afterBindData |
| `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin` | `save` | beforeBindData, afterCreateNewData, afterLoadData, afterBindData, beforeDoOperation |
| `kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin` | `save` | afterBindData, beforeItemClick, beforeDoOperation |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | `save` | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | `save` | beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | `save` | onPreparePropertys, beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | `save` | onPreparePropertys, onAddValidators |
| `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | `save` | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction, beginOperationTransaction, afterExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | `unsubmit` | beforeExecuteOperationTransaction, beginOperationTransaction, afterExecuteOperationTransaction |

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

## ❌ 不可做（ISV 雷区）

反编译实证抽出 **5 条** ISV 红线（去重后）：

1. ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
2. ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
3. ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
4. ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）
5. ❌ 禁继承 AbsOrgBaseOp（非 HR 通用推荐 · 用 HRDataBaseOp 代替）

### 禁继承类（共 10 个）

- `HRBaseDataImportEdit` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRBaseDataTplList` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- `HRBasedataLogList` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
- `HisModelListCommonPlugin` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
- `HisModelBuListPlugin` (LIST_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin`
- `HisModelF7ListPlugin` (OP_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
- `HisModelFilterPanelListPlugin` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
- `HisModelFilterPanelF7ListPlugin` (OP_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
- `HisModelMobileListPlugin` (OTHER) ← `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
- `HisBaseDataF7FastFilter` (OTHER) ← `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`

**原因**：这些类带 `@SdkInternal` 或 `@SdkPlugin(role=internal)` 注解 · ISV 不得继承。

## ℹ️ 标品已接入插件（53 个 · 详见 07_ext_points.md）

- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`
- `BdCtrlStrtgyShowLogicPlugin` ← `kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicPlugin`
- `BaseDataCreateOrgPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataCreateOrgPlugin`
- `BaseDataFormPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataFormPlugin`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- ...（共 53）

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
> 生成时间：2026-05-02 · 数据源 cs_drafts/wtbd_shift/

---

## CS-01 · 给班次加 ISV 班次属性字段（最高频）

### 需求
业务方说：班次要新增「车间编号 / 工种类型 / 是否高温作业 / 标准产能」等扩展属性·适配制造业多样化班次。

### 推荐方案
- **扩展对象**: `wtbd_shift`（班次基础资料 · BaseFormModel · 76 字段·业务核心）
- **扩展点**: `modifyMeta(op=add field)`
- **风险**: 低

### 调用链
```
Step 1-3: getDevInfo + getBizApps + modifyMeta
  bizAppId = 1O9FOLRY18YW (工时假勤规则)
  ops: [{op:"add", element:{
    fieldType:"ComboField",
    key:"${ISV_FLAG}_worktype",
    name:{zh_CN:"工种类型"},
    items:[
      {key:"NORMAL", name:"普通"},
      {key:"HIGHTEMP", name:"高温"},
      {key:"NIGHT", name:"夜班"}
    ]
  }}]
Step 4: getFormSchema 验证
```

### 代码框架
```python
designer.add_field(field_type="ComboField", name="工种类型", key="${ISV_FLAG}_worktype",
                   items=[("NORMAL","普通"),("HIGHTEMP","高温"),("NIGHT","夜班")],
                   parent_entity_id=designer.base_entity_id)
designer.save()
```

### 踩坑
- ❌ ComboField 不传 items → 平台报"枚举值不能为空"
- ❌ items.key 用中文 → 数据库存中文 · 跨环境迁移乱码
- ❌ 不带 `${ISV_FLAG}_` 前缀 → 标品升级被覆盖
- 💡 班次扩展字段会被排班逻辑读取·必须配合 wts_rosterview 同步加默认值
- 💡 76 字段已经很多·谨慎再加·优先用 JSON 扩展属性字段（${ISV_FLAG}_extprop）

---

## CS-02 · 加自定义 Validator（班次时长合法性 / 跨字段约束）

### 需求
ISV 业务规则：班次的「上班时间」+「工时」不能跨过午夜 24:00（除非显式标记为夜班）·防止 HR 误填。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` 在 `onAddValidators`
- **opKey**: `save`
- **风险**: 低

### 代码框架
```java
package ${ISV_FLAG}.wtc.shift.opplugin;

import kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.ExtendedDataEntity;

public class TdkwShiftDurationValidatorOp extends HRBaseDataStatusOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new ShiftDurationValidator());
    }

    static class ShiftDurationValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (ExtendedDataEntity ext : this.dataEntities) {
                String startTime = ext.getValue("starttime").toString();      // "08:00"
                BigDecimal duration = (BigDecimal) ext.getValue("duration");  // 8.5h
                String worktype = ext.getValue("${ISV_FLAG}_worktype").toString();

                int startMinutes = parseTime(startTime);
                int endMinutes = startMinutes + (int)(duration.doubleValue() * 60);
                if (endMinutes > 24 * 60 && !"NIGHT".equals(worktype)) {
                    this.addErrorMessage(ext, "班次跨过午夜·必须标记为夜班");
                }
            }
        }
    }
}
```

### 注册
```python
client.register_plugin(formId="wtbd_shift", operationKey="save",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.wtc.shift.opplugin.TdkwShiftDurationValidatorOp")
```

### 踩坑
- ❌ 时间字段用 String 比较 ("08:00" > "07:00") → 不靠谱·必须转分钟数比较
- ❌ 跨字段校验不用 ORM 点路径 → 性能差
- ❌ duration 是 BigDecimal·用 doubleValue() 精度丢失（小问题但要意识到）
- 💡 班次设计涉及 wtbd_calendar / wtte_calresult / wts_rosterview 多表·改 wtbd_shift 时确认下游不会乱

---

## CS-03 · HisModel 时态写：班次调整自动生成新版本

### 需求
ISV 业务方需要：HR 调整班次工时时·标品自动维护历史版本（旧班次截断 / 新班次生效）·不影响已用旧班次的考勤记录。

### 推荐方案
- **扩展点**: 直接调标品 `HisModelHelper.changeEffective` API
- **opKey**: 自定义 `${ISV_FLAG}_chgshift` 或复用标品 `save`
- **风险**: 中（HisModel API 用错会写脏历史）

### 代码框架
```java
package ${ISV_FLAG}.wtc.shift.opplugin;

import kd.hr.hbp.business.servicehelper.HisModelHelper;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.dataentity.entity.DynamicObject;

public class TdkwShiftChangeOp extends HRDataBaseOp {
    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        super.beforeExecuteOperationTransaction(e);
        for (DynamicObject obj : e.getDataEntities()) {
            Long oldId = (Long) obj.getPkValue();
            BigDecimal newDuration = obj.getBigDecimal("${ISV_FLAG}_newduration");
            Date effectiveDate = obj.getDate("${ISV_FLAG}_effectivedate");

            HisModelHelper.changeEffective("wtbd_shift", oldId,
                                           "duration", newDuration, effectiveDate);
            // 平台自动:
            //   1. 旧版本 bsled = effectiveDate - 1
            //   2. 旧版本 iscurrentversion = false
            //   3. 新版本 boid 同旧 / hisversion = old+1 / iscurrentversion = true / bsed = effectiveDate
        }
    }
}
```

### 注册新 opKey
```python
client.add_operation(formId="wtbd_shift", opKey="${ISV_FLAG}_chgshift",
                     opType="custom", name={"zh_CN":"调整班次"})
client.register_plugin(formId="wtbd_shift", operationKey="${ISV_FLAG}_chgshift",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.wtc.shift.opplugin.TdkwShiftChangeOp")
```

### 踩坑
- ❌ 直接 `obj.set("duration", newValue) + SaveServiceHelper.save()` → HisModel 不识别·历史版本错乱
- ❌ effectiveDate 给当前时间而非 HR 选择的日期 → 业务规则错
- ❌ 同一 boid 并发 changeEffective → 抛 DuplicateKeyException · 平台用 boid 锁 · 大批量分组提交
- ❌ 不传 effectiveDate（null）→ 平台默认当前时间·历史可能被错误截断
- 💡 跑完 changeEffective 后查询验证：`HisModelHelper.queryByEffectiveDate(formId, boid, date)` · 应返回新版本
- 💡 班次时态变化会影响 wts_rosterview / wtte_calresult · 调整时建议提前广播给下游

---

## CS-04 · 批量导入班次（标品 hies 框架 + ISV 预校验）

### 需求
HR 月初要批量导入 50+ 班次·标品 `hies_entity_import` 已支持·ISV 想加：「导入前校验班次时长合理性」+「重复班次跳过」。

### 推荐方案
- **扩展点**: 复用标品 `hies_entity_import` + ISV 实现 `ImportDataPreprocessOp`
- **opKey**: `importdata_hr`（标品已有·12 plugin 链）
- **风险**: 低

### 代码框架
```java
package ${ISV_FLAG}.wtc.shift.opplugin;

import kd.hr.hies.opplugin.ImportDataPreprocessOp;
import kd.hr.hies.opplugin.ImportContext;
import kd.hr.hies.opplugin.ImportRow;

public class TdkwShiftImportPreprocessOp extends ImportDataPreprocessOp {
    @Override
    public void preprocess(ImportContext ctx) {
        super.preprocess(ctx);

        // 1. 时长合理性校验
        for (ImportRow row : ctx.getRows()) {
            BigDecimal duration = row.getBigDecimalValue("duration");
            if (duration.compareTo(BigDecimal.ZERO) <= 0 ||
                duration.compareTo(BigDecimal.valueOf(24)) > 0) {
                row.setError("班次时长必须在 0-24 小时之间");
            }
        }

        // 2. 重复班次跳过
        Set<String> existingNumbers = ShiftHelper.findAllNumbers();
        for (ImportRow row : ctx.getRows()) {
            String number = row.getValue("number");
            if (existingNumbers.contains(number)) {
                row.setSkip("班次已存在·跳过");
            }
        }
    }
}
```

### 注册
```python
client.register_plugin(formId="wtbd_shift", operationKey="importdata_hr",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.wtc.shift.opplugin.TdkwShiftImportPreprocessOp")
```

### 踩坑
- ❌ 不复用 hies 框架自己写 ExcelImportPlugin → 失去标品的进度展示 / 错误收集 / 部分回滚
- ❌ 预处理时改了 ctx.rows 但忘 set 回 → 标品继续用原数据
- ❌ 对 50+ 行做 N 次 DB 查重 → 性能差·必须先一次性查出 existingNumbers
- 💡 hies 导入框架是 hr_hrmp 云的"零代码导入器"·参见 `hies_entity_import` 11md

---

## CS-05 · 班次保存后联动考勤档案（wtp_attfilebase）

### 需求
班次时长 / 类型 调整后·已分配该班次的所有考勤档案需要刷新缓存·并广播给排班引擎重新计算。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` 重写 `afterExecuteOperationTransaction`
- **opKey**: `save`
- **风险**: 中（涉及跨表写 + 缓存）

### 调用链
```
wtbd_shift.save commit 后:
  HRBaseDataLogOp.afterExecuteOperationTransaction(super)
    → ISV.afterExecuteOperationTransaction
       → 找哪些 wtp_attfilebase 用了这个 shift（按 perattperiod.shift = shiftId）
       → 刷它们的缓存（AttendCacheHelper.invalidate）
       → 触发排班引擎重算
```

### 代码框架
```java
package ${ISV_FLAG}.wtc.shift.opplugin;

import kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.servicehelper.QueryServiceHelper;

public class TdkwShiftPropagateOp extends HRBaseDataLogOp {
    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        for (DynamicObject shift : e.getDataEntities()) {
            Long shiftId = (Long) shift.getPkValue();

            // 1. 找下游考勤档案
            List<Long> attFileIds = QueryServiceHelper.queryDataSet(
                "wtp_attfilebase", "id",
                "period.shift = ? AND iscurrentversion = '1'",
                new Object[]{shiftId}
            ).map(r -> r.getLong("id")).collect(Collectors.toList());

            // 2. 刷缓存
            AttendCacheHelper.invalidateBatch(attFileIds);

            // 3. 触发排班引擎（异步入队）
            RosterScheduleQueue.scheduleRebuild(attFileIds);
        }
    }
}
```

### 注册
```python
client.register_plugin(formId="wtbd_shift", operationKey="save",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.wtc.shift.opplugin.TdkwShiftPropagateOp")
```

### 踩坑
- ❌ 在 `beforeExecuteOperationTransaction` 写下游 → 主表回滚但下游已写 · 脏数据
- ❌ 同步触发排班引擎 → 排班可能跑几分钟 · save 前端转圈
- ❌ 改了 1 个 shift · 影响 1000+ attFile · 不分批刷缓存 → Redis 阻塞
- ❌ 不限制只刷 iscurrentversion=true 的 attFile → 历史版本也被触发重算·浪费
- 💡 涉及缓存 + 队列 · 必须在 commit 后做（避免主 TX 回滚但缓存已刷的不一致）

