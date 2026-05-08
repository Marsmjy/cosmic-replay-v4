# 数据流转 · 行政组织类型（haos_adminorgtype）

> **状态**: 🟢 基于反编译 3 类实证 + opKey 链分析
> **confidence**: verified
> **核心流程**: 2 个（UI 联动流 + HIES 导入修正流）

---

## 一、UI 编辑时 adminorgtypestd → orgpattern 联动数据流

这是本场景最核心的数据流，三胞胎均无此流程。

```
[用户打开 adminorgtype 编辑视图]
        ↓
[AdminorgtypeEditPlugin.beforeBindData #9]
        ↓
    读取 dataEntity.id
    id != 0L（已存在记录）
        ↓ 是
    baseDataCheckReference(id).isRefence()
        ↓ 有引用
    getView().setEnable(false, ["adminorgtypestd"])
    → adminorgtypestd 字段灰化 · 不可编辑
        ↓ 无引用
    adminorgtypestd 正常可编辑
        ↓

[用户选择 / 变更 adminorgtypestd 字段值]
        ↓
[PropertyChangedEvent 触发]
        ↓
[AdminorgtypeEditPlugin.propertyChanged #9]
        ↓
    name == "adminorgtypestd"？
        ↓ 是
    getValue("adminorgtypestd") → DynamicObject
        ↓ 非 null
    AdminOrgTypeStdEnum.getOrgPatternIdById(stdId)
    → 枚举查映射 → orgPatternId
        ↓
    getModel().setValue("orgpattern", orgPatternId)
    → orgpattern 字段自动填充显示
        ↓
[用户可手工覆盖 orgpattern 值]
        ↓
[用户点保存 → 走 save opKey 链]
```

### 关键节点说明

| 节点 | 类/接口 | 生命周期 |
|---|---|---|
| 引用检查 | `AdminorgtypeEditPlugin.beforeBindData` | beforeBindData（渲染前）|
| 字段灰化 | `IFormView.setEnable(false, ["adminorgtypestd"])` | beforeBindData 阶段执行 |
| 联动触发 | `PropertyChangedEvent（adminorgtypestd）` | 用户 UI 操作时 |
| 枚举映射 | `AdminOrgTypeStdEnum.getOrgPatternIdById` | propertyChanged 阶段 |
| 字段赋值 | `IDataModel.setValue("orgpattern", orgPatternId)` | propertyChanged 阶段 |

---

## 二、HIES 导入修正数据流

```
[触发 importdata_hr opKey · HIES 导入行政组织类型数据]
        ↓
[importdata_hr 执行链启动]
    ├─ HRBaseDataStatusOp (onAddValidators + beforeExecute)
    ├─ HRBaseDataLogOp (beforeExecute)
    ├─ HRBaseDataEnableOp (beforeExecute)
    ├─ BaseDataBuOp (onAddValidators → 注册 CtrlStrategyValidator)
    ├─ HRBaseOriginalOp (beforeExecute)
    └─ AdminOrgTypeSaveOp (beginOperationTransaction)
              ↓
        检查 options.getVariables().get("importtype")
              ↓ importtype == null / 空字符串
        直接 return（非 HIES 导入 · 不修正）
              ↓ importtype 非空（HIES 导入场景）
        读取 AdminOrgTypeStdEnum 所有枚举值
              ↓
        查询 bos_org_pattern 实体（批量加载 orgPattern 实体）
              ↓
        遍历当前批次导入数据：
          for each importedEntity:
            stdId = entity.get("adminorgtypestd").id
            correctOrgPatternId = AdminOrgTypeStdEnum.getOrgPatternIdById(stdId)
            if (entity.orgpattern != correctOrgPatternId):
              entity.set("orgpattern", correctOrgPatternId)  // 修正
              ↓
        数据被修正 · 事务继续提交
        → 导入后 adminorgtypestd → orgpattern 联动关系正确
```

### 关键节点说明

| 节点 | 类/接口 | 生命周期 |
|---|---|---|
| HIES 标志检测 | `this.getOption().getVariables().get("importtype")` | beginOperationTransaction |
| 枚举加载 | `AdminOrgTypeStdEnum`（内部枚举类）| beginOperationTransaction |
| 实体修正 | `entity.set("orgpattern", correctId)` | beginOperationTransaction（事务内）|

---

## 三、普通保存数据流

```
[用户点保存 · save opKey]
        ↓
[formPlugin.beforeDoOperation]
  HRBaseDataTplEdit → 标准前置校验
        ↓
[save opKey 操作链]
  HRBaseDataStatusOp → 状态校验
  HRBaseDataLogOp → 日志前置
  HRBaseDataEnableOp → 启用校验
  BaseDataBuOp → onAddValidators 注册 CtrlStrategyValidator
  HRBaseOriginalOp → 原始数据处理
  AdminOrgTypeSaveOp → 检测 importtype（空 → return · 不修正）
        ↓
[BaseDataSavePlugin → 实际写库]
  t_haos_adminorgtype 主表写入
  t_haos_adminorgtype_l 多语言表写入
        ↓
[HRBaseDataLogOp.afterExecute → 写操作日志]
```

---

## 四、字段引用检查时序

```
时序：用户打开已存在记录的编辑视图

T=0: form 初始化
T=1: beforeBindData 事件触发（3 个 listener 按顺序）
  T=1.1: AdminorgtypeEditPlugin.beforeBindData → 查引用 → 可能灰化 adminorgtypestd
  T=1.2: HRBaseDataTplList.beforeBindData（如适用）→ 标品模板
  T=1.3: HRBasedataLogList.beforeBindData → 日志相关
T=2: 数据绑定完成
T=3: 页面渲染 · adminorgtypestd 字段状态（可编辑 or 灰化）呈现给用户
```

---

## 五、数据流与 PR 规则对照

| 数据流节点 | 适用 PR 规则 | 注意事项 |
|---|---|---|
| FormPlugin 联动 setValue | PR-003（FormPlugin 用 getModel().setValue）| 不用 entity.set |
| propertyChanged setValue | PR-004（死循环防护 beginInit/endInit）| 标品未用 beginInit（直接 setValue）· ISV 扩展必须防护 |
| HIES 导入 entity.set | PR-003（OP 用 entity.set）| AdminOrgTypeSaveOp 在 OP 层 · 正确用法 |
| 枚举映射 | AdminOrgTypeStdEnum（只查不继承）| ISV 禁止继承场景内部枚举辅助类 |
| OP 修正事务时机 | PR-010（beginOperationTransaction）| AdminOrgTypeSaveOp 用 beginOperationTransaction ✅ |

---

## 六、数据流全景图

```
                            haos_adminorgtypestd（上游字典）
                                    ↓ 被引用（BasedataField）
                        haos_adminorgtype（本场景）
                          ├─ adminorgtypestd（类型归属）
                          │     ↓ 联动（propertyChanged）
                          └─ orgpattern（形态）
                                    ↓
                            haos_adminorg（下游）
                              └─ adminorgtype（行政组织类型）
                                   BasedataField 单选引用
```

---

## 七、与三胞胎数据流对比

| 场景 | 核心数据流 | 复杂度 |
|---|---|---|
| haos_adminorgtype（本场景）| UI 联动流 + HIES 导入修正流 | 中（字典中最复杂）|
| haos_adminorgfunction | 仅标准 CRUD 流 | 低 |
| haos_adminorglayer | 仅标准 CRUD 流 | 低 |
| haos_orgchangereason | 仅标准 CRUD 流（含 QFilter 过滤特殊主键）| 低 |

**结论**：本场景的数据流比三胞胎多出 2 条（UI 联动流 + HIES 导入修正流），是 haos 域字典场景中数据流最复杂的场景。
