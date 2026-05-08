# 异常与诊断 · 行政组织快速维护

> **状态**: 🟢 基于 `cosmic_realworld_traps/` 真发坑位手册整合
> **confidence**: real_deploy (Week 5 踩坑验证)

---

## 一、业务层异常（组织树相关）

### EX-01 · 组织循环引用

**症状**:
```
OrgCircularReferenceException:
  组织 A (id=xxx) 调整上级会导致循环引用, 目标 parent=yyy
  at kd.bos.hr.haos.OrgService.moveParent(OrgService.java:123)
```

**原因**: 把组织 A 的上级设为 A 的子孙节点，形成循环（A ← B ← C ← A）。

**诊断**:
1. 看日志 `circularPath` 字段
2. 查路径: `SELECT longnumber FROM haos_adminorg WHERE id = ?`
3. 在 UI 组织树视图检查目标父节点是否是当前组织的后代

**解决**:
- 业务上: 先解绑中间节点，再调整
- 技术上: 前端限制选择树自动排除后代节点（标品已实现）

**相关**: `beforeSave@haos_adminorg` / `onParentChange@haos_adminorg`

---

### EX-02 · 目标上级已停用

**症状**: `OrgStatusLockException: 目标组织 xxx 已停用，不能作为上级`

**原因**: 试图把组织调整到一个**已停用**的上级下。

**解决**: 先启用目标组织，再调整。如果业务确实要转移，先调到活跃上级，再处置停用组织。

---

### EX-03 · 组织编码重复

**症状**: `OrgCodeDuplicateException: 编码 xxx 已存在`

**诊断**:
```sql
SELECT id, name, longnumber FROM haos_adminorg WHERE number = '<重复的编码>';
```

**解决**: 改编码，或用自动生成（见 `06_customization_solutions.md` CS-01）。

**注意**: `number` 字段创建后**不可修改**（标品约束）。

---

### EX-04 · 删除被引用的组织

**症状**: `OrgReferencedException: 组织被 N 个下游引用，无法删除`

**诊断**:
```sql
SELECT COUNT(*) FROM hrpi_employee WHERE adminorg = ?;
SELECT COUNT(*) FROM hbpm_position WHERE adminorg = ?;
SELECT COUNT(*) FROM pay_salary_archive WHERE adminorg = ?;
```

**解决**: 清空引用再删，或**用停用代替删除**（推荐）。

---

### EX-05 · 系统计算字段被篡改

**症状**: 数据看起来正常保存了，但下游薪酬/考勤数据错乱

**原因**: 自定义插件直接修改了 `level` / `longnumber` / `structlongnumber` / `belongcompany` 字段

**诊断**:
- 查看最近变更的组织，对比 `level = 从父组织 +1` 是否正确
- 对比 `belongcompany` 是否符合自动计算逻辑（沿 parentorg 找公司/集团）

**解决**: 
- 立即撤销自定义插件对这 4 字段的赋值
- 让标品的 `OrgPathRecalcPlugin` 自动重算
- 如需业务逻辑，改用新增扩展字段，别碰标品计算字段

---

## 二、元数据扩展异常（Week 5 真发坑位，实测证据）

### EX-10 · buildMeta 返回 errorCode=0 但其实失败

**症状**: 调 `buildMeta` API 返回 `{errorCode: "0"}`，但 `getFormSchema` 查不到新建的实体。

**原因**: 苍穹响应 `errorCode="0"` 只表示**HTTP 层成功**，不代表业务落库。

**解决（硬规则）**: 
```
每次 buildMeta / modifyMeta 调用后，必须二次验证:
  response = buildMeta(...)
  if response.errorCode == "0":
      assert getFormSchema(number).exists, "实际未落库"
```

**预防**: 已落地到 `scripts/cosmic_preflight.py`

---

### EX-11 · EmployeeField 不在 buildMeta 枚举

**症状**: `buildMeta` 时传 `fieldType: "EmployeeField"`，返回 "不支持的字段类型"。

**原因**: `EmployeeField` 是 HR SDK 扩展类型，**OpenAPI 的 74 值枚举里不存在**。

**解决**: 用 `BasedataField` + `basedataNumber: "hrpi_employeenewf7query"` 替代：
```json
{
  "fieldType": "BasedataField",
  "basedataNumber": "hrpi_employeenewf7query",
  "fieldKey": "${ISV_FLAG}_responsible"
}
```

**预防**: 已落地到 `aihr/cosmic/field_types_authoritative.py`

---

### EX-12 · 下拉字段漏传 comboOptions

**症状**: 新建 `ComboField` 后，UI 上下拉框为空。

**原因**: 5 种下拉类型都必须带 `comboOptions`：
- `ComboField` / `MulComboField`
- `CheckBoxGroupField` / `RadioOptGroupField`
- `ItemClassTypeField`

**解决**:
```json
{
  "fieldType": "ComboField",
  "comboOptions": [
    {"value": "1", "name": {"zh_CN": "选项A", "en_US": "Option A"}},
    {"value": "2", "name": {"zh_CN": "选项B", "en_US": "Option B"}}
  ]
}
```

**预防**: `payload_adapter.validate_payload` 自动检测。

---

### EX-13 · 字段类型拼写别名坑

**症状**: `fieldType: "MultiLangTextField"` → 静默失败，新建的字段变成普通 TextField。

**原因**: 苍穹官方字段类型拼写**有 Bug**：
- 正确: `MuliLangTextField`（Muli 是 Multi 拼写错）
- 错误: `MultiLangTextField`

类似坑：`ProdcutField` (正确, Product 拼写错)、`RadioGroup` (禁用)

**预防**: `field_types_authoritative.py` 提供 74 值权威枚举 + 别名映射。

---

### EX-14 · modifyMeta 用 formNumber 失败

**症状**: `modifyMeta` 传 `formNumber: "haos_adminorg"`，返回 "参数错误"。

**原因**: 仅 `modifyMeta` 接口参数叫 **`formId`**（实际传 formNumber 值），其他接口都叫 `formNumber`。

**解决**:
```json
// ✅ 对
{"formId": "haos_adminorg", "ops": [...]}
// ❌ 错
{"formNumber": "haos_adminorg", "ops": [...]}
```

**预防**: `payload_adapter.validate_payload` 自动映射。

---

### EX-15 · modifyMeta op 枚举非 4 值静默失败

**症状**: `modifyMeta` 传 `op: "update"` 或 `op: "delete"`，请求成功但没效果。

**原因**: 苍穹仅识别 4 值：`add` / `modify` / `remove` / `move`。其他值（update/delete/addField 等）**静默失败**。

**解决**: 只能用这 4 个枚举值。

---

### EX-16 · EmbedFormAp 嵌入员工档案"假成功"

**症状**: 用 `modifyMeta` 把附表嵌入 `hspm_ermanfilereform`（员工档案），errorCode=0，但 UI 上看不到。

**原因**: 员工档案 `hspm_ermanfilereform` 是 `DynamicFormModel`，它的附表不是通过 `modifyMeta` 加 `EmbedFormAp` 决定的，**是运行时 Java 插件 `FileManageReformHomePlugin` 决定挂哪张附表**。

**解决三路径**:
- **路径 A**: 多视图配置（推荐，标品支持）
- **路径 B**: 改 Java 主插件（需重新打包部署）
- **路径 C**: 独立菜单直达（绕开员工档案）

**⚠️ 行政组织场景不涉及此坑**（仅员工档案）

---

### EX-17 · addRule 的 ActionType 小写被忽略

**症状**: `addRule` 返回成功，但规则不生效。

**原因**: `ActionType` 和 `FieldKey` 必须 **PascalCase**：
```json
// ✅ 对
{"actionType": "Lock", "fieldKey": "Number"}  ← 前端识别
// ❌ 错
{"actionType": "lock", "fieldKey": "number"}  ← 静默忽略
```

**预防**: `payload_adapter` 自动转 PascalCase。

---

### EX-18 · addRule 的 preCondition 语法严格

**症状**: `preCondition: "fieldA == ''"` 规则不触发。

**原因**: 公式引擎**只支持有限语法**：
- ✅ 允许: `==`, `>`, `<`, `>=`, `<=`, 字符串常量 `'xxx'`
- ❌ 禁用: `== ''`, `== null`, `||`, `&&`

**解决**: 用"不等于非空字符串" 反向表达：`preCondition: "fieldA != 'NON_EMPTY_MARKER'"` （或改用 Java 插件）

---

### EX-19 · addRule 坏规则删除死锁

**症状**: 调 `deleteRule` 删坏规则，返回"规则校验失败无法删除"。

**原因**: 苍穹要求**规则必须合法才能删除**（双重绑定），坏规则陷入死锁。

**解决**: 先 `updateRule` 把规则改成合法（如 triggerField 改成有效字段），再 `deleteRule`。

**预防**: `CosmicOpenApiClient.delete_rule_safe` 自动处理。

---

### EX-20 · registerPlugin targetType 大小写错误

**症状**: `registerPlugin` 返回成功，但插件不生效。

**原因**: `targetType` 必须**大写枚举**，5 个合法值：
- `BILL_FORM` / `LIST_FORM` / `MOBILE_BILL_FORM` / `OPERATION` / `EVENT`

---

### EX-21 · updateOperation.plugins 全量替换陷阱

**症状**: 调 `updateOperation` 新增一个插件，结果标品的其他插件全丢了。

**原因**: `updateOperation.plugins` 是**全量替换**语义，不是 append。

**解决**:
```python
# 正确做法: 先 get，合并后再 update
current = client.get_operation(...)
current.plugins.append(new_plugin)
client.update_operation(plugins=current.plugins)
```

**同理**: `validations` 也是全量替换。

---

### EX-22 · 插件 Java 类路径错误

**症状**: `registerPlugin` 后运行时 `ClassNotFoundException`。

**原因**: Java 类没打进 jar 或包名错。

**诊断**:
```bash
# 检查 jar 内
jar tf mypackage.jar | grep MyPlugin
```

**解决**: 确认 `className` 写的是**全限定名**（含包路径），检查 jar 打包。

---

## 三、诊断工具清单

| 工具 | 作用 |
|---|---|
| `cosmic-env devportal open` | 打开 designer 看实际元数据 state |
| `getFormSchema(number)` | 二次验证 buildMeta/modifyMeta 是否落库 |
| `list_rules(entity)` | 二次验证 addRule 是否生效 |
| `scripts/cosmic_preflight.py` | 提交前自动预检 |
| `payload_adapter.validate_payload` | 参数规范化 + 硬约束校验 |
| 数据库直查（组织树）| `SELECT longnumber FROM haos_adminorg WHERE ...` |

---

## 四、通用铁律

### 铁律 1: `errorCode=0` ≠ 成功

任何 `buildMeta` / `modifyMeta` / `addRule` / `registerPlugin` 调用，都必须：
1. 检查 `errorCode == "0"`
2. **二次验证**（getFormSchema / list_rules / getOperation）

### 铁律 2: 参数大小写敏感

- `op` / `fieldType` / `actionType` 的大小写**每个接口不同**
- 必须用权威枚举表（`field_types_authoritative.py`）

### 铁律 3: 全量替换接口要先读后改

- `updateOperation.plugins`
- `updateOperation.validations`
- 任何覆盖式更新

### 铁律 4: 系统计算字段禁改

- `level` / `longnumber` / `structlongnumber` / `belongcompany`
- 改了就是脏数据，下游必炸

---

**📌 来源追溯**：
- `cosmic_realworld_traps/buildmeta_traps.md` → EX-10 ~ EX-13
- `cosmic_realworld_traps/modifymeta_traps.md` → EX-14 ~ EX-16
- `cosmic_realworld_traps/addrule_traps.md` → EX-17 ~ EX-19
- `platform/openapi_capability_map.md` → EX-20, EX-21
- `knowledge/domain/org/anchors.md` → EX-05 系统计算字段禁改
- 业务层异常（EX-01 ~ EX-04）为推断 + 标准错误类型
