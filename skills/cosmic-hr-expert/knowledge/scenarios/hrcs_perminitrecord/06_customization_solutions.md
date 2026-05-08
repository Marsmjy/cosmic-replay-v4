# 定制方案 · 权限初始化

> **状态**: 🟢 已补全（2026-04-28 基于 66 opKey + PermInitRecordEdit 反编译实证）
> **数据源**: `rules_chain_all.json` + `form_lifecycle_rules.json` + `curated_sdk.json`

## 可套用的 pattern

| Pattern | 适用场景 | 关键约束 |
|---|---|---|
| `add_field_extension` | 主表或子分录加自定义字段（如任务备注） | ISV 扩展元数据 via buildMeta 派生层 `_e`，不改标品 hrcs_perminitrecord |
| `add_sub_entity` | 加 ISV 自有子分录（如"权限初始化审批记录"） | 新建 ISV 扩展元数据 + EmbedFormAp 嵌入，参见 `knowledge/pattern/add_sub_entity.md` |
| `add_unique_validation` | 任务名唯一、编码唯一 | 继承 PermInitRecordDeleteOp 在 onAddValidators 追加 AbstractValidator，参见 FP_BDO1 的 checkTaskName 同名清理套路 |
| `override_plugin_behavior` | 修改 completeInit 校验逻辑、修改导入字段映射 | 继承 PermInitRecordEdit + super 调用 + 追加逻辑；勿直接替换标品插件注册 |
| `add_init_type_mode` | 新增第三种初始化模式（如 dept 部门级初始化） | 继承 PermInitRecordEdit.beforeBindData 的 inittype 路由，追加新分支 + 新建对应子表单 |

## 🟢 推荐覆盖位置（基于实证插件链）

本场景 66 个 opKey 统一走 5 插件执行链：HRBaseDataStatusOp → HRBaseDataLogOp → HRBaseDataEnableOp → HRBaseOriginalOp → PermInitRecordDeleteOp。所有操作共享这 5 插件，因此 ISV 扩展点很集中。

| 需求类型 | 推荐覆盖的标品插件 | 扩展方式 | 覆盖方法 |
|---|---|---|---|
| 保存前校验（字段级） | `PermInitRecordDeleteOp` (kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp) | 继承 + `onAddValidators` 追加 | `addValidator(new XxxValidator())` |
| 保存前校验（单据级） | `PermInitRecordDeleteOp` | 继承 + `beforeExecuteOperationTransaction` | 校验失败 `args.addErrorMessage(msg)` 阻断保存 |
| 保存后触达（通知/日志） | `PermInitRecordDeleteOp` | 继承 + `afterExecuteOperationTransaction` | 事务已提交，调 `HRMServiceHelper.invokeHRMPService` 发通知 |
| UI 动态控件/字段显隐 | `PermInitRecordEdit` (kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit) | 继承 + `afterBindData` 追加 | `this.getView().setVisible/setEnable` 调整控件态 |
| 导入前数据清洗 | `PermInitRecordEdit` | 继承 + `beforeDoOperation` 拦截 | 在 `userimport`/`roleimport` 前加自定义清洗 + `args.setCancel(true)` 阻断 |
| 完成初始化落库后写下游表 | `PermInitRecordEdit` | 继承 + `afterDoOperation` 追加 | finishinit 成功后追加 ISV 自定义表写入 |
| 列表查询过滤 | `PermInitRecordList` (kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList) | 继承 + `setFilter` | `evt.getQFilters().add(new QFilter(...))` |
| 列表打开表单传参 | `PermInitRecordList` | 继承 + `beforeShowBillForm` | `evt.getParameter().setCustomParam("inittype", "role")` |

## 🟢 继承链安全规则（PR 红线）

| ID | 规则 | 后果 |
|---|---|---|
| PR-001 | 禁止直接继承 PermInitRecordEdit — ISV 必须自建 ext FormPlugin，通过注册顺序挂在标品后面 | 直接继承会覆盖标品 inittype 路由和 finishinit 落库逻辑 |
| PR-002 | FormPlugin 必须在 `prepareProperties` 声明所有读字段（如 dealstatus、inittype），否则 beforeBindData 读不到 | 缺少字段声明 → getValue 返回 null → 路由分支错误 |
| PR-003 | paintErrMarkCol / initRoleFdEntry 依赖 EntityMetadataCache + PageCache.entityFields，ISV 扩展字段中文名回填需复用这套缓存 | 自行查库回填 → N+1 性能问题 |
| PR-004 | userfieldentry / rolefieldentry 的 propName 回填只在 beforeBindData 执行一次，ISV 加新字段到这些分录必须加入回填循环 | 漏掉回填 → 分录只显示 propKey（机器码），用户不可读 |
| PR-005 | ORM.genLongId 兜底主键只在 save 阶段触发，ISV 在 beforeDoOperation 里新增分录行需确保主键不为 0 | 主键为 0 → save 时 ORM 报错 |
| PR-010 | Validator 必须在 onAddValidators 阶段注册，在 beforeExecute 阶段注册无效（框架已跳过该阶段） | 校验器静默不执行 |

## 🟢 真实定制场景模板

### 场景 1：加"任务备注"字段到主表

```
1. buildMeta 创建 ISV 扩展元数据 parentId=hrcs_perminitrecord 的物理ID
2. modifyMeta add field: fieldType=TextField, name=ext_taskmemo, columnName=fext_taskmemo
3. 若需嵌入主表布局 → EmbedFormAp（待苍穹官方补齐该能力）
4. 不改标品 hrcs_perminitrecord 的字段
```

### 场景 2：完成初始化后发企微通知

```java
// ext PermInitRecordDeleteOp.afterExecuteOperationTransaction
if ("finishinit".equals(args.getOperateKey())) {
    for (DynamicObject obj : args.getDataEntities()) {
        String taskName = obj.getString("name");
        String creator = obj.getString("creator");
        // 调 HRMServiceHelper 发企微通知
    }
}
```

### 场景 3：限制任务名不能含特殊字符

```java
// ext PermInitRecordDeleteOp.onAddValidators
args.addValidator(new AbstractValidator() {
    @Override
    public void validate() {
        DynamicObject obj = getDataEntity();
        String name = obj.getString("name");
        if (name != null && name.matches(".*[<>%&].*")) {
            addErrorMessage("任务名称不能包含特殊字符: " + name);
        }
    }
});
```

## ISV 不可做的事（红线）

- 不可直接 modifyMeta add/remove 标品 hrcs_perminitrecord 的 field（ISV 不拥有该元数据）
- 不可篡改 inittype ComboField 的枚举值（userrole/role），会破坏 PermInitRecordEdit.beforeBindData 路由
- 不可物理删除 t_hrcs_pinitrecord 行（只能通过标品 delete opKey，走 HRBaseDataStatusOp 软禁用链路）
- 不可直接调 PermRoleInitService.initRole(recordId) 重复落库（该服务幂等靠 dealstatus=1 判断，重复调用产生脏角色）
- 不可在 beforeDoOperation 阶段抛出 RuntimeException（应用 addErrorMessage + setCancel，抛异常会导致 ORM 事务回滚但 errorMessage 不写入）
