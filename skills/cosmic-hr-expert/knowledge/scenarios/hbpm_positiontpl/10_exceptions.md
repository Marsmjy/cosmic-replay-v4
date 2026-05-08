# 异常诊断 · 岗位模板（hbpm_positiontpl）

> **状态**: 🟢 基于反编译 4 类实证
> **confidence**: verified

---

## EX-01 · 新建时提示"无法新增"（BU 开关未开启）

| 属性 | 值 |
|---|---|
| **触发位置** | `PositionTplListPlugin.validateOrgExistOpenTpl` |
| **用户提示** | "所在权限下组织体系管理组织均未启用模板库，无法新增。" |

**原因**：权限下所有 BU 的系统参数中 `openpositiontpl=false`。

**处理**：在系统参数配置中为管理组织开启岗位模板库开关。

---

## EX-02 · 保存时提示"已存在名称相同且可用的岗位类型。"

| 属性 | 值 |
|---|---|
| **触发位置** | `PositionTplSaveOp.onAddValidators` 中注册的校验器 |
| **传递方式** | OperateOption.setVariableValue("nameError", ...) |
| **展示位置** | `PositionTplEditPlugin.afterDoOperation` → `PositionTplUtil.isNameEnableRe(view, "name", ...)` |

**原因**：name 字段唯一性校验失败（同 org 下已有同名启用记录）。

**处理**：更换岗位模板名称。

---

## EX-03 · 保存时字段下方显示"数据已存在"

| 属性 | 值 |
|---|---|
| **触发位置** | 同 EX-02 · indexError 变量 |
| **展示位置** | `PositionTplEditPlugin.afterDoOperation` → `PositionTplUtil.isNameEnableRe(view, "index", ...)` |

**原因**：index（排序号）唯一性校验失败。

**处理**：修改排序号。

---

## EX-04 · endOperationTransaction 中级联同步失败

| 属性 | 值 |
|---|---|
| **触发位置** | `PositionTplSaveOp.endOperationTransaction` |
| **影响** | 整个保存事务回滚（endOperationTransaction 在主事务中）|

**原因**：`PositionTplChangeSyncPosService.syncUpdatePosition()` 同步下游岗位数据时抛出异常，导致整个保存回滚。

**诊断**：查系统日志中 `PositionTplChangeSyncPosService` 相关异常。

---

## EX-05 · afterExecuteOperationTransaction 中 sendMsg 失败

| 属性 | 值 |
|---|---|
| **触发位置** | `PositionTplSaveOp.afterExecuteOperationTransaction` |
| **影响** | 主事务已提交 · BEC 消息发送失败 → 下游感知不到变更 |

**原因**：`ChangeMsgServiceImpl.sendMsg()` 服务调用失败（消息服务不可用/网络超时）。

**注意**：由于此时主事务已提交，sendMsg 失败不会回滚数据，但下游系统可能数据不一致。

**诊断**：查系统日志中 ChangeMsgServiceImpl 相关异常；检查消息队列服务状态。
