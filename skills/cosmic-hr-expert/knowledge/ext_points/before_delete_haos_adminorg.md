# 🔌 beforeDelete@haos_adminorg

> **类型**: 扩展点独立档案
> **confidence**: verified

---

## 基本信息

| 属性 | 值 |
|---|---|
| **ID** | `ep_before_delete_haos_adminorg` |
| **助记名** | `beforeDelete@haos_adminorg` |
| **宿主**: | `haos_adminorg` |
| **时机**: | 删除前 |
| **事务阶段**: | `pre_transaction` |
| **接口**: | 操作扩展（继承 `AbsOrgBaseOp`，绑定 opKey=`delete`） |
| **覆盖风险** | 中 |

---

## 标品插件

### 1. OrgChildCheckPlugin
- **作用**: 检查是否有下属组织（有则阻止删除）

### 2. OrgReferenceCheckPlugin
- **作用**: 检查是否被下游引用（员工/岗位/薪酬档案）
- **⚠️ 性能**: 查多个下游表，大组织场景可能耗时

---

## 推荐模式

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`delete`
- 推荐父类：`AbsOrgBaseOp`（行政组织操作类通用父类）
- 关键重写方法：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs e)` — 删除事务未开启前，此处做业务校验最合适
  - （可选）独立写一个继承 `AbstractValidator` 的校验类，通过 `onAddValidators(AddValidatorsEventArgs e)` 注册

**业务意图**：在标品的"有下属组织 / 被下游引用"检查之外，追加业务级判断，例如"该组织是否还有未完成的审批流"，若有则抛业务异常阻止删除。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `delete`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

> ⚠️ 务必先调 `super.beforeExecuteOperationTransaction(e)` 保留标品的下属 / 引用检查；抛业务异常请使用苍穹平台的 `KDBizException`，框架会自动回卷事务并反馈到前端。

---

## 通用建议

**强烈建议**：业务流程中，用**停用**代替**删除**。

原因：
- 保留历史数据（审计要求）
- 避免引用残留（防脏数据）
- 支持将来重启用

---

## 相关

- [CS-07 · 禁止删除有在职员工的组织](../scenarios/admin_org_quick_maintenance/06_customization_solutions.md#cs-07)
- [EX-04 · 删除被引用的组织](../scenarios/admin_org_quick_maintenance/10_exceptions.md#ex-04)

---

**📌 来源**: `anchors.md` + 实测
