# 异常 + 错误码 · core_hr_assignmentmag

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（validators + 反编译 throw 语句 · v2）
> **数据源**: `rules_chain_all.json::validators` · 反编译 `throw new *Exception`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 校验器（validators）

- `V_TimelineTplOp_1` (opKey=`save`) · (errorMessage 未实抓 · 待 jar 反查)
- `V_TimeLineCommonSaveOp_1` (opKey=`save`) · (errorMessage 未实抓 · 待 jar 反查)
- `V_AssignmentMagSaveOp_1` (opKey=`save`) · (errorMessage 未实抓 · 待 jar 反查)
- `V_EmployeeCommonStandardMustInputOp_1` (opKey=`save`) · (errorMessage 未实抓 · 待 jar 反查)
- `V_AssignmentMagDeleteOp_1` (opKey=`delete`) · (errorMessage 未实抓 · 待 jar 反查)
- `V_EmployeeAuditCommonOP_1` (opKey=`abandon`) · (errorMessage 未实抓 · 待 jar 反查)
- `V_EmployeeAuditCommonOP_2` (opKey=`abandon`) · (errorMessage 未实抓 · 待 jar 反查)

## ✅ verified · 反编译 throw 语句（OP 类业务异常）

反编译共抓到 **5** 条 `throw new *Exception(...)` 调用：

- `KDBizException` 在 `AssignmentMagDeleteOp` · `(动态消息 · 变量=errorMsg)`
- `KDBizException` 在 `EmployeeAuditCommonOP` · `(动态消息 · 变量=error)`
- `KDBizException` 在 `TimelineTplOp` · `(动态消息 · 变量=ResManager)`

## 🟡 平台/网络异常（待人工补充）

<TODO 人工补>
- HR Token 失效 / 跨租户 SSO 失败的兜底
- BEC 异步任务幂等失败的人工补单流程
- DB 锁冲突 / 长事务超时的常见现象
