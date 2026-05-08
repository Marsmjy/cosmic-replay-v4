# 变更影响面 · 架构类型（haos_structtype）

> **状态**: 🟢 基于反编译 7 类实证
> **confidence**: verified

---

## 一、变更操作影响速查

| 操作 | 影响范围 | 影响级别 | 说明 |
|---|---|---|---|
| 新建 structtype（enable=1）| 创建元数据/菜单/业务规则 | ⭐ 高 | 自动工厂逻辑 · 失败则无菜单 |
| 修改 name | 元数据名 + 菜单名（多语言）| ⭐ 高 | ChgNameOp 同步所有关联资源 |
| 修改 enable=0（禁用）| haos_adminorg + haos_structure 全部禁用 | ⭐ 高 | 级联禁用下游 · 范围广 |
| 修改 enable=1（启用）| 重新创建元数据/菜单/业务规则 | ⭐ 高 | EnableOp 同 SaveOp 逻辑 |
| 修改 effdt | 本条记录 | 低 | 仅影响本记录 |
| 修改 org | 本条记录 | 低 | 权限管理字段 |
| 删除 structtype | 元数据/移动端元数据/菜单/业务规则全部删除 + 主表记录 | ⭐ 极高 | 不可逆 · 需确认弹窗 |
| 修改 metanumsuffix | ❌ 严禁 | 极高 | 所有关联资源命名失效 |

---

## 二、下游受影响实体

| 下游实体 | 关联字段 | 影响操作 | 影响方式 |
|---|---|---|---|
| `haos_adminorg`（其他行政组织）| structtype 关联 | 禁用 structtype | 级联设 enable=0（StructTypeDisableOp 实证）|
| `haos_structure`（矩阵组织架构）| structtype 关联 | 禁用 structtype | 级联设 enable=0（StructTypeDisableOp 实证）|
| `bos_entityobject`（元数据）| metanumsuffix 关联 | 新建/启用/删除 | 自动创建/删除 |
| DevPortal 菜单 | id/metanumsuffix 关联 | 新建/启用/改名/删除 | 自动维护 |
| BizRuleLibrary/BizRule/OpBizRuleSet | metanumsuffix 关联 | 新建/启用/删除 | 自动创建/删除 |
| IE 导入模板 | dataEntity 关联 | 新建/启用 | StructTypeIETempHelper 维护 |

---

## 三、ISV 开发影响面

| ISV 变更 | 风险 | 建议 |
|---|---|---|
| modifyMeta 加字段 | 低 | 按 PR-001 流程 · 前缀+key≤24字符 |
| 并列挂 OP 插件 | 低 | rowkey 排在标品之后 |
| 并列挂 FormPlugin | 低 | 不干扰标品逻辑 |
| 继承场景专属类 | ❌ 高 | PR-001 禁止 |
| 绕过 OP 直接改 enable | ❌ 高 | 绕过级联禁用 → 数据不一致 |
| 绕过 OP 直接改 metanumsuffix | ❌ 极高 | 命名空间失效 → 系统异常 |
