# 参考案例 · 架构类型（haos_structtype）

> **状态**: 🟢 基于反编译 7 类实证
> **confidence**: verified

---

## Case-01 · 新建架构类型成功后出现"有主表记录但没有菜单"

### 场景

新建了一条 haos_structtype 记录，enable=1，保存返回成功消息。但刷新"组织管理>其他形态组织"菜单后，没有看到新增的二级菜单。

### 根因

`StructTypeSaveOp.afterExecuteOperationTransaction` 在新事务（`TX.requiresNew()`）中创建元数据/菜单。若此新事务执行失败（如 AppMetadata 冲突），新事务 rollback，但主实体（t_haos_structtype）已在外层事务提交。结果：主表有记录，但没有对应菜单。

### 诊断

1. 查数据库 t_haos_structtype 确认记录存在
2. 查 DevPortal AppMetadata 检查是否有对应菜单 ID
3. 查系统日志（关键词：`creatMetaDataAndMenu error`）

### 处理

重新执行 enable 操作（先禁用再启用）→ 触发 `StructTypeEnableOp.afterExecuteOperationTransaction` 重新创建元数据/菜单。

---

## Case-02 · 改名（chgname）后元数据名称未同步

### 场景

在架构类型列表使用"变更名称"功能，改名成功，列表显示新名称，但对应的元数据/菜单名称未更新。

### 根因

`StructTypeChgNameOp.afterExecuteOperationTransaction` 调用 `StructClassHelper.changeMetaName()` 和 `StructClassHelper.chgMenuName()` 同步名称。若服务调用失败，名称同步不完整。

### 诊断

查日志确认 changeMetaName / chgMenuName 是否有异常。

---

## Case-03 · 禁用架构类型后，下游其他行政组织无法使用

### 场景

禁用了一条 haos_structtype，后续发现该类型下的所有 haos_adminorg 也都变成了禁用状态。

### 说明

这是正确行为：`StructTypeDisableOp.beginOperationTransaction` 会级联禁用所有关联的 `haos_adminorg`（其他行政组织）和 `haos_structure`（矩阵组织）。

### 处理

若需要保留下游组织可用：在禁用架构类型之前，先将下游组织迁移到其他架构类型，再禁用。

---

## Case-04 · 删除架构类型失败

### 场景

点击删除，确认弹窗后，提示"{name}删除失败，请联系管理员处理。"

### 根因

`StructTypeDeleteDonothingOp.beforeExecuteOperationTransaction` 在新事务中删除元数据/菜单。若 `StructTypeDeleteValidator` 校验不通过，或删除元数据失败（如外键约束），抛出 KDBizException。

### 诊断

1. 查 `StructTypeDeleteValidator` 校验是否有业务规则不满足
2. 查系统日志确认具体异常
