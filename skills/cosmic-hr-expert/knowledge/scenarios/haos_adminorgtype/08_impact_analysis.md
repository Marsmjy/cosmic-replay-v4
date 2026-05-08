# 变更影响面 · 行政组织类型（haos_adminorgtype）

> **状态**: 🟢 基于 refentity_reverse.json 实证 + 反编译 3 类分析
> **confidence**: verified
> **影响评级**: 中高（比三胞胎复杂 · 因 adminorgtypestd→orgpattern 联动链）

---

## 一、修改 adminorgtypestd 字段的连锁影响

这是本场景最敏感的变更操作，产生多层连锁效应：

### 1.1 直接影响（R-3 联动）

```
修改 haos_adminorgtype 记录的 adminorgtypestd 值
    ↓
AdminorgtypeEditPlugin.propertyChanged 触发
    ↓
AdminOrgTypeStdEnum.getOrgPatternIdById(newStdId)
    ↓
getModel().setValue("orgpattern", newOrgPatternId)
    ↓ orgpattern 字段自动变更
```

**影响**：orgpattern 字段值被自动覆盖（即使用户之前手工设置了 orgpattern，adminorgtypestd 变更后会被重新填写）。

### 1.2 引用锁定机制（R-2 保护）

被下游引用的记录，adminorgtypestd 字段**在 UI 上被灰化禁用**（`baseDataCheckReference` 实证）。这意味着：

- 若 haos_adminorg 已引用某条 adminorgtype 记录，该记录的 adminorgtypestd 不可在 UI 修改
- ISV 若通过后端 OP 强行修改 adminorgtypestd，adminorgtypestd → orgpattern 联动不会触发（propertyChanged 是 FormPlugin 层 · OP 层绕过 UI 不触发）
- 强行修改后需要手动补 orgpattern 修正（或触发 HIES 导入修正流）

### 1.3 下游 haos_adminorg 的影响

```
修改 haos_adminorgtype.adminorgtypestd
    ↓
haos_adminorg.adminorgtype（BasedataField 单选）
    已存在的引用关系保持（外键 fadminorgtypeid 不变）
    但 adminorgtype 的 adminorgtypestd 已变 → 查询报表时语义变化
```

**实际业务影响**：如果有报表按 adminorgtypestd 分组统计 haos_adminorg 数据，修改 adminorgtypestd 后，原来归属某类型标准的组织，会在报表中归入新的类型标准。

---

## 二、下游引用分析（refentity_reverse 实证）

### 2.1 直接引用 haos_adminorgtype 的实体

```json
"haos_adminorgtype": [
  {
    "form": "haos_adminorg",
    "field": "adminorgtype",
    "type": "BasedataField",
    "cnName": "行政组织类型"
  }
]
```

**结论**：`haos_adminorgtype` 被 `haos_adminorg.adminorgtype` 单选引用（BasedataField）。

| 下游实体 | 字段 key | 字段类型 | 物理外键列（推断）|
|---|---|---|---|
| `haos_adminorg` | `adminorgtype` | BasedataField（单选）| `fadminorgtypeid`（推断）|

### 2.2 间接影响链

```
haos_adminorgtypestd（类型标准上游字典）
    ↓ 被引用
haos_adminorgtype.adminorgtypestd（类型归属）
    ↓ 被引用
haos_adminorg.adminorgtype（行政组织类型）
    ↓ 影响
行政组织数据的组织形态（orgpattern）语义
```

### 2.3 下游引用反向查询路径

ISV 查询"哪些 haos_adminorg 使用了某个 adminorgtype"：
```java
// 单选 BasedataField 直字段查（不是多选的 fbasedataid 子表路径）
// ⭐ admin_org 是 HisModel 时序 · 必须加 iscurrentversion=true
QFilter qf = new QFilter("adminorgtype", "=", adminorgtypeId)
    .and(new QFilter("iscurrentversion", "=", Boolean.TRUE))
    .and(new QFilter("enable", "=", "1"));
boolean isReferenced = new HRBaseServiceHelper("haos_adminorg").isExists(qf);
```

---

## 三、各操作的影响面评估

| 操作 | 影响面 | 影响说明 |
|---|---|---|
| 新增 adminorgtype | 低 | 新数据不影响已有记录 |
| 修改 number | 低（仅预置数据禁改）| 业务自建数据 number 可改；出厂 number 不可改（PR-007）|
| 修改 name | 低 | 多语言名称变更 · 但下游存的是 id 外键 · 不影响引用关系 |
| 修改 adminorgtypestd | **高** | R-2 锁定 + R-3 联动 + 下游报表语义变化 |
| 修改 orgpattern | 中 | 直接影响组织形态语义 · 不触发联动 |
| 禁用 adminorgtype | **高** | haos_adminorg 中引用此类型的数据可能受影响 |
| 删除 adminorgtype | **高** | 下游 haos_adminorg 外键游离 · CS-02 拦截 |
| HIES 导入 | 中（AdminOrgTypeSaveOp 修正）| 导入后 orgpattern 自动修正 · 覆盖导入数据原值 |

---

## 四、与三胞胎影响面对比

| 场景 | 修改核心字段影响 | 删除影响 | 特殊影响 |
|---|---|---|---|
| haos_adminorgtype（本场景）| **高**（adminorgtypestd 有联动 + 锁定）| 高（下游 adminorg 游离）| HIES 导入修正覆盖 orgpattern |
| haos_adminorgfunction | 中（无特殊联动）| 高（下游 adminorg 游离）| 无 |
| haos_adminorglayer | 中（无特殊联动）| 高（下游 adminorg 游离）| 无 |
| haos_orgchangereason | 中（无特殊联动）| 高（下游 changescene 游离）| 多选关系（影响面更广）|

---

## 五、ISV 修改 haos_adminorgtype 元数据的影响

| 操作 | 影响范围 | 风险 |
|---|---|---|
| modifyMeta 加新字段 | 低（不影响已有字段）| 低 |
| 修改 adminorgtypestd 字段属性 | ❌ isvCanModify=false · 平台拒绝 | N/A |
| 修改 orgpattern 字段属性 | 可行（isvCanModify=true）| 低（谨慎）|
| 修改 enable/status 字段 | ⚠ minefield=yellow · 影响下游 | 中 |
| 修改 issyspreset 字段 | ❌ isvCanModify=false · 平台拒绝 | N/A |

---

## 六、haos_adminorgtypestd 上游变更对本场景的影响

`haos_adminorgtypestd`（类型标准）是 `adminorgtypestd` 字段的上游字典。若 haos_adminorgtypestd 数据发生变更：

```
haos_adminorgtypestd 记录被修改/禁用/删除
    ↓
haos_adminorgtype.adminorgtypestd（外键引用）
    被禁用 → 选择此 adminorgtypestd 的 adminorgtype 记录语义变化
    被删除 → haos_adminorgtype.adminorgtypestd 字段游离（外键悬空）
    ↓
AdminOrgTypeStdEnum.getOrgPatternIdById(deletedStdId) → 返回 null 或异常
    → orgpattern 联动失效
```

**结论**：haos_adminorgtypestd 的 CRUD 会级联影响本场景的联动完整性，上游修改前需评估对 adminorgtypestd → orgpattern 联动链的影响。
