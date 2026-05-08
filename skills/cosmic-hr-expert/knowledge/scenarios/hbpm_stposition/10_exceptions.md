# 异常诊断 · 标准岗位维护（hbpm_stposition）

> **状态**: 🟢 verified · 基于反编译 + HisModel 框架 + 平台规范
> **confidence**: verified
> **审计时间**: 2026-04-27

---

## 异常 1 · 保存报"无信息变更，请确认"

**现象**：点击保存/变更，界面弹出"无信息变更，请确认"提示，操作被阻断。

**原因**：StandardPositionEdit.beforeDoOperation 检测到 `model.getDataChanged()=false` 且附件未变（isAttachUnChanged）。

**解决**：
- 实际修改了某个字段后再保存
- 若字段已修改但仍报此错：检查 `setDataChanged(false)` 是否被异常调用（afterBindData 末尾标品会调 setDataChanged(false)）
- ISV 扩展的字段联动如果在 beginInit/endInit 内 setValue，不会触发 setDataChanged=true，需要在 endInit 后手动调 model.setDataChanged(true)

---

## 异常 2 · F7 选标准岗位时列表为空

**现象**：其他场景的 stposition 字段 F7 弹出后，列表没有数据。

**原因**：
1. HisModelF7ListPlugin.beforeBindData 加了 iscurrentversion=true 过滤，若所有岗位均没有当前版本（异常数据状态），列表为空
2. 生效日期过滤（bsed/bsled 区间）导致全部过滤掉
3. PositionBaseDataF7FastFilter 的额外过滤条件（如按 org 过滤）

**解决**：
- 检查 hbpm_stposition 表中是否存在 iscurrentversion=true 的数据
- 检查 F7 的生效日期参数设置
- 排查 PositionBaseDataF7FastFilter 的过滤逻辑

---

## 异常 3 · 变更操作报"时序区间重叠"

**现象**：点击变更，提交后报校验错误，提示时序区间重叠或生效日期冲突。

**原因**：HisUniqueValidateOp 检测到新生效日期（bsed）与已有版本的生效区间（bsed-bsled）重叠。

**解决**：
- 先查询该 boid 的所有历史版本，确认时序区间
- 新版本的 bsed 必须晚于当前版本的 bsled（或等于当前版本 bsled+1）
- 确认没有待生效版本（datastatus=TO_BE_EFFECT）占用了目标日期区间

---

## 异常 4 · ISV OP 中读取 boid 为 0

**现象**：在并列挂的 OP 插件里，entity.getLong("boid") 返回 0 或 null。

**原因**：OP 的 onPreparePropertys 未声明 "boid" 字段，HisModel 框架默认不加载时序字段到 DataEntity。

**解决**：
```java
@Override
public void onPreparePropertys(PreparePropertysEventArgs args) {
    args.getFields().add("boid");
    args.getFields().add("iscurrentversion");
    args.getFields().add("hisversion");
    // 其他需要的字段...
}
```

---

## 异常 5 · 编码未自动生成（number 字段空）

**现象**：新增标准岗位时，number 字段没有自动生成编码。

**原因**：
1. 编码规则未配置（CodeRuleServiceHelper.isExist 返回 false）
2. org 字段为空（编码规则按 org 维度配置，org=null 则不触发）
3. 编码规则绑定的字段（org / adminorg / job）还未填写

**解决**：
- 检查编码规则基础资料配置（开发平台 → 编码规则）
- 先填 org 字段，再触发编码生成
- 若业务需要手工填写编码，可在编码规则中配置手工维护模式

---

## 异常 6 · ISV 字段联动触发死循环（propertyChanged 无限递归）

**现象**：修改某字段后，页面无响应或请求一直 pending，后台报栈溢出。

**原因**：ISV FormPlugin.propertyChanged 中直接调用 model.setValue(X, v)，导致 propertyChanged 再次触发，无限递归。

**解决**（PR-004）：
```java
@Override
public void propertyChanged(PropertyChangedArgs args) {
    String fieldName = args.getProperty().getName();
    if ("targetField".equals(fieldName)) {
        IDataModel model = this.getModel();
        model.beginInit();           // ⭐ 包裹开始
        model.setValue("relatedField", newValue);
        model.endInit();             // ⭐ 包裹结束
        this.getView().updateView("relatedField");
    }
}
```

---

## 异常 7 · 标准岗位变更后 hbpm_positionhr 数据异常

**现象**：标准岗位变更了，hbpm_positionhr 的关联显示还是旧数据。

**分析**：这通常不是 BUG。hbpm_positionhr 存的是 boid（PR-009），变更标准岗位时 boid 不变，F7 选值字段应实时查当前版本。

**真正的问题可能是**：
- ISV 代码用 id（版本 id）而非 boid 做关联，变更后旧版 id 的 iscurrentversion=false 导致查不到
- 报表/统计 SQL 直接 JOIN t_hbpm_position 没加 iscurrentversion=true 过滤

**解决**：
- 所有查询加 `iscurrentversion=true` 过滤（PR-008）
- 关联查询用 boid 不用 id（PR-009）

---

## 关联文档

- `02_business_rules.md` · INV-SP 规则说明
- `03_model_design.md` · PR-008/009 查询规范
- `06_customization_solutions.md` · CS-02 下游引用校验
