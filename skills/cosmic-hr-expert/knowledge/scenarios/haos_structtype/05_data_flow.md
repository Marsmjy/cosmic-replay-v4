# 数据流转 · 架构类型（haos_structtype）

> **状态**: 🟢 基于反编译 7 类实证
> **confidence**: verified

---

## 一、新建时数据写入路径

```
UI 层（表单）
    ↓ save opKey
StructTypeSaveOp.beginOperationTransaction
    写 StructClass（关联数据 · StructClassHelper.saveNew）
    ↓
（平台保存主实体 t_haos_structtype + t_haos_structtype_l）
    ↓
StructTypeSaveOp.afterExecuteOperationTransaction（新事务）
    写 bos_entityobject（元数据页面记录）
    写 DevPortal AppMetadata 菜单树（二级菜单）
    写 BizRuleLibrary（业务规则库）
    写 BizRule（业务规则）
    写 OpBizRuleSet（操作业务规则集）
    写 IE 导入模板（StructTypeIETempHelper.addTemplate）
    写 StructConfig（StructClassHelper.saveStructConfig）
```

---

## 二、改名时数据写入路径

```
StructTypeChgNameOp.afterExecuteOperationTransaction
    读 t_haos_structtype.name（ILocaleString · 多语言）
    读 OtherStructTypeService.getChangeNameMap
    写 关联元数据名称（StructClassHelper.changeMetaName）
    写 DevPortal 菜单名称（StructClassHelper.chgMenuName · 多语言）
```

---

## 三、禁用时数据更新路径

```
StructTypeDisableOp.beginOperationTransaction
    读 t_haos_adminorg（其他行政组织 · 关联本 structtype）
    写 t_haos_adminorg.enable = "0"（批量更新）
    读 t_haos_structure（矩阵组织 · 关联本 structtype）
    写 t_haos_structure.enable = "0"（批量更新）
```

---

## 四、删除时数据删除路径

```
StructTypeDeleteDonothingOp.beforeExecuteOperationTransaction（新事务）
    读 bos_entityobject（按 metanumsuffix 查元数据）
    读 移动端元数据（metaMobileBill）
    删 移动端元数据（StructClassHelper.deleteMetaData · 遍历）
    删 元数据（StructClassHelper.deleteMetaData · 遍历）
    删 bos_entityobject 记录（DeleteServiceHelper.delete）
    写 DevPortal AppMetadata（移除菜单 · AppMetaServiceHelper.save）
    删 BizRuleLibrary
    删 BizRule
    删 OpBizRuleSet（保留 haos_structtype 自身的）
平台层（beginOperationTransaction）
    删 t_haos_structtype 主表记录
    删 t_haos_structtype_l 多语言记录
```

---

## 五、数据关系图

```
t_haos_structtype（主表）
    │ metanumsuffix ─────────────────────────────────────────┐
    │ enable ──→ 级联 t_haos_adminorg.enable（禁用）         │
    │         ──→ 级联 t_haos_structure.enable（禁用）        │
    │                                                         │
    └─── bos_entityobject（元数据页面）──── ${metanumsuffix}_*  ← ┘
    └─── DevPortal AppMetadata（菜单）── id 关联
    └─── BizRuleLibrary / BizRule / OpBizRuleSet ── metanumsuffix 关联
    └─── StructClass（架构类型关联）── StructClassHelper 维护
    └─── IE 模板（StructTypeIETempHelper）── 导入模板
    └─── StructConfig（架构配置）── saveStructConfig 维护
```

---

## 六、跨系统数据一致性

| 数据资源 | 创建时机 | 删除时机 | 不一致风险 |
|---|---|---|---|
| bos_entityobject 元数据 | 新建+启用 | 删除 OP | 若 afterExecuteOp 新事务失败 → 元数据创建失败但主表已保存（orphan）|
| DevPortal 菜单 | 新建+启用 | 删除 OP | 同上 |
| BizRule 规则 | 新建+启用 | 删除 OP | 同上 |
| t_haos_adminorg/structure | 即时（beginOpTx）| 禁用时 | 同 StructTypeDisableOp 事务 |

> 关键风险：`afterExecuteOperationTransaction` 在独立新事务（`TX.requiresNew()`）中执行，若新事务中创建元数据/菜单失败，会 `rollback` 并抛 `KDBizException`。但主实体（t_haos_structtype）已在外层事务提交，可能产生"有主表记录但无菜单"的不一致状态。
