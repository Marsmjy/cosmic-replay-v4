# 定制化方案集 · wtbd_cycset

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（按 mines + deep_resolve 推可做/不可做）
> **数据源**: `rules_chain_all.json::mines` · `_deep_resolve_index.json` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py（v5.1 render_06）

## ✅ 可做（ISV 扩展路径）

### 1. 在主表加 ISV 扩展字段

主表 `wtbd_cycset` 当前 36 个字段。
ISV 扩展元数据（建独立 ISV form 用 `_inherits` 引用主表）→ 加业务字段·不动标品。

```bash
# 1. 通过 OpenAPI buildMeta 建 ISV 扩展元数据
# 2. parentId=<wtbd_cycset formId>
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

## ℹ️ 标品已接入插件（23 个 · 详见 07_ext_points.md）

- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `WTCTipsPlugin` ← `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`
- `WTCPresetEdit` ← `kd.wtc.wtbs.formplugin.web.WTCPresetEdit`
- `WTCNoHisSumaryEdit` ← `kd.wtc.wtbs.formplugin.web.WTCNoHisSumaryEdit`
- ...（共 23）

## 🚨 决策金字塔（按金蝶 PPT 04 沉淀）

1. **首选标品配置** · 看是否能用元数据 validations / formRules 解决
2. **次选 ISV 扩展元数据** · 加字段 / 加规则 不动标品
3. **再选 ISV OP 插件继承** · 看上面「可继承类」清单
4. **末选 ISV 自建 form** · 无标品可继承时

**禁忌**：不要用 ISV form 直接覆盖标品 form（会丢标品升级红利）
