# 异常诊断 · 架构类型（haos_structtype）

> **状态**: 🟢 基于反编译 7 类实证
> **confidence**: verified

---

## EX-01 · 保存时抛 KDBizException（元数据创建失败）

| 属性 | 值 |
|---|---|
| **异常类** | `kd.bos.exception.KDBizException` |
| **触发位置** | `StructTypeSaveOp.afterExecuteOperationTransaction` |
| **日志关键词** | `creatMetaDataAndMenu error` |

**原因**：在新建事务（`TX.requiresNew()`）中创建元数据/菜单失败，新事务 catch → markRollback → 抛 KDBizException。

**诊断步骤**：
1. 查日志 `StructTypeSaveOp` 相关输出
2. 检查 `StructClassHelper.creatMetaDataAndMenu` 报错原因（元数据编码冲突/权限不足）
3. 确认 `metanumsuffix` 是否已被其他记录使用

---

## EX-02 · 删除时抛"XXX删除失败，请联系管理员处理。"

| 属性 | 值 |
|---|---|
| **触发位置** | `StructTypeDeleteDonothingOp.beforeExecuteOperationTransaction` |
| **日志关键词** | `StructTypeDeleteOp_2` / rollback |

**原因**：删除元数据/菜单时在新事务中发生异常，catch → markRollback → KDBizException（包含每条记录的删除失败消息）。

**诊断步骤**：
1. 查日志确认具体异常（元数据删除失败 / AppMetadata 保存失败）
2. 检查是否有 StructTypeDeleteValidator 校验阻拦

---

## EX-03 · chgname 改名后菜单名未更新（静默失败）

| 属性 | 值 |
|---|---|
| **触发位置** | `StructTypeChgNameOp.afterExecuteOperationTransaction` |

**原因**：`StructClassHelper.changeMetaName` 或 `chgMenuName` 调用失败，但异常被吞（afterExecuteOperationTransaction 未显式 try-catch）。

**诊断**：查系统日志中 `changeMetaName` / `chgMenuName` 相关调用。

---

## EX-04 · 禁用时报错（OtherAdminOrgService 查询异常）

| 属性 | 值 |
|---|---|
| **触发位置** | `StructTypeDisableOp.beginOperationTransaction` |

**原因**：`OtherAdminOrgService.getOtherAdminOrgData` 或 `getStructureDataById` 查询异常，导致 beginOperationTransaction 失败，整个禁用事务回滚。

**诊断**：查系统日志 OtherAdminOrgService 相关查询异常。

---

## EX-05 · StructTypeDeleteValidator 校验失败

| 属性 | 值 |
|---|---|
| **触发位置** | `StructTypeDeleteDonothingOp.onAddValidators` 注册的 `StructTypeDeleteValidator` |
| **触发时机** | 执行删除操作前 |

**原因**：`StructTypeDeleteValidator.validate()` 检查到有下游数据引用了本架构类型（具体校验逻辑见 StructTypeDeleteValidator 源码），校验失败 → addErrorMessage → 操作中断。

**处理**：先处理下游引用（如先禁用/迁移 haos_adminorg 数据）再删除。
