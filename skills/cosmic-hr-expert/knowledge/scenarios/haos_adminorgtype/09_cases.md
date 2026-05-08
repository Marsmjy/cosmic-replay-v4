# 参考案例 · 行政组织类型（haos_adminorgtype）

> **状态**: 🟢 基于反编译 3 类实证 + 业务场景分析
> **confidence**: verified
> **案例数**: 3 个

---

## 案例一 · 用户修改已被引用的 adminorgtypestd 遇到"灰色不可编辑"

### 场景描述

用户在行政组织类型列表找到一条行政组织类型记录，点击编辑，发现"类型归属"（adminorgtypestd）字段呈灰色，无法点击或修改，但其他字段（如名称、描述）可以正常编辑。

用户困惑：为什么这个字段不能改？

### 原因分析

**这是正确行为，不是 Bug**。

触发路径（AdminorgtypeEditPlugin.beforeBindData 实证）：

```
1. 用户打开编辑视图
2. AdminorgtypeEditPlugin.beforeBindData 触发（#9 插件）
3. 读取记录的 id（非 0L · 已存在记录）
4. 调用 baseDataCheckReference(id).isRefence()
5. 检测结果：haos_adminorgtypestd 的某个具体记录已被其他实体引用
6. getView().setEnable(false, ["adminorgtypestd"]) → 字段灰化
```

**"被引用"的含义**：haos_adminorgtypestd（类型标准）实体中的某个具体值，已被另一个实体（如 haos_adminorgtype 的 adminorgtypestd 字段）引用。一旦有下游引用，修改 adminorgtypestd 会破坏：
- adminorgtypestd → orgpattern 的联动一致性
- 下游报表按类型标准统计的语义正确性

### 解决方案

| 用户诉求 | 建议操作 |
|---|---|
| 想换一个类型归属 | 先检查哪些下游记录引用了当前 adminorgtypestd；如果可以断开引用（下游数据改为其他类型标准），再回来修改 |
| 当前 adminorgtypestd 语义有误 | 在 haos_adminorgtypestd（类型标准）基础资料里修改该标准的名称/描述 |
| 业务确实需要新的类型归属 | 新建一条 adminorgtype 记录选择正确的 adminorgtypestd，把下游 adminorg 数据的 adminorgtype 字段迁移到新记录 |

### ISV 开发注意

- **不要通过后端 OP 绕过灰化限制强行修改 adminorgtypestd**（会导致 orgpattern 联动失效，数据不一致）
- 如需在后端修改，必须同步手动更新 orgpattern 字段（模拟 R-3 联动）
- 禁止继承 `AdminorgtypeEditPlugin` 绕过 beforeBindData 逻辑（PR-001）

---

## 案例二 · HIES 导入行政组织类型时 orgpattern 自动修正

### 场景描述

IT 人员通过 HIES 导入一批行政组织类型数据。导入文件中，部分记录填写了 adminorgtypestd（类型归属），但没有填写对应的 orgpattern（形态）；或者填写的 orgpattern 与 adminorgtypestd 按枚举映射不一致。

导入完成后，用户发现所有记录的 orgpattern 值都被正确填充/修正了，并不是导入文件中的原始值。

### 原因分析

**这是正确的预期行为**，由 AdminOrgTypeSaveOp 在 HIES 导入时执行修正。

触发路径（AdminOrgTypeSaveOp.beginOperationTransaction 实证）：

```
1. importdata_hr opKey 触发（HIES 导入）
2. 苍穹 HIES 框架在 options.variables 中注入 importtype 变量（非空）
3. AdminOrgTypeSaveOp.beginOperationTransaction 执行
4. 检测 importtype 非空 → 进入 HIES 导入修正逻辑
5. 读取 AdminOrgTypeStdEnum 枚举映射
6. 对导入数据的每条记录：
   - 取 adminorgtypestd 字段值
   - AdminOrgTypeStdEnum.getOrgPatternIdById(stdId) → 正确 orgPatternId
   - entity.set("orgpattern", correctOrgPatternId) → 修正 orgpattern
7. 事务提交 → orgpattern 值为修正后的正确值
```

### 结果

| 导入文件的 orgpattern 值 | 修正后的值 |
|---|---|
| null（未填）| AdminOrgTypeStdEnum 枚举对应的正确值 |
| 不一致的值 | AdminOrgTypeStdEnum 枚举对应的正确值（覆盖）|
| 已经正确的值 | 不变（修正后与原值相同）|

### ISV 开发注意

- **HIES 导入修正是标品逻辑，ISV 的导入数据源要知道这个行为**：填入 orgpattern 的值会被标品覆盖修正
- ISV 若需要在 HIES 导入时修正自己的 ISV 字段，参考 CS-04 模式（并列挂 OP · 检测 importtype 守卫）
- **禁止继承 AdminOrgTypeSaveOp** 来"扩展"修正逻辑（PR-001）

---

## 案例三 · 新建行政组织类型时 adminorgtypestd 选择触发 orgpattern 自动填充

### 场景描述

业务运营人员在新建行政组织类型时，选择了"类型归属"（adminorgtypestd）字段的某个值（如"机构型"）。随即发现"形态"（orgpattern）字段自动填充了对应的值（如"法人机构"），无需手动选择。

用户想确认：这个自动填充是可靠的吗？能手工改吗？

### 原因分析

自动填充由 AdminorgtypeEditPlugin.propertyChanged 触发（R-3 · 实证）：

```
1. 用户选择 adminorgtypestd
2. propertyChanged 事件触发
3. AdminorgtypeEditPlugin.propertyChanged 处理（name == "adminorgtypestd"）
4. AdminOrgTypeStdEnum.getOrgPatternIdById(stdId)  → 枚举映射查 orgPatternId
5. getModel().setValue("orgpattern", orgPatternId)  → 自动填充
```

**可靠性**：是。AdminOrgTypeStdEnum 是标品枚举，映射关系由平台维护，与 bos_org_pattern 实体对应。

**可手工改**：是。orgpattern 字段 isvCanModify=true，用户可在自动填充后手工选择其他值。但下次再修改 adminorgtypestd 时，orgpattern 会再次被联动覆盖。

### 注意事项

- 清空 adminorgtypestd（设为 null）时，orgpattern 不会自动清空（标品逻辑：null 时 return，不联动）
- 手工修改 orgpattern 后如果再改 adminorgtypestd，手工值会被联动覆盖
- HIES 导入场景同样会修正 orgpattern（R-4）
