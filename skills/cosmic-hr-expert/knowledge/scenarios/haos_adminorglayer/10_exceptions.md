# 异常 + 错误码 · haos_adminorglayer

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（validators + 反编译 throw 语句 · v2）
> **数据源**: `rules_chain_all.json::validators` · 反编译 `throw new *Exception`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 校验器（validators）

- `V_save_1` (opKey=`save`) · (errorMessage 未实抓 · 待 jar 反查)
- `V_delete_1` (opKey=`delete`) · (errorMessage 未实抓 · 待 jar 反查)
- `V_disable_1` (opKey=`disable`) · (errorMessage 未实抓 · 待 jar 反查)
- `V_saveandnew_1` (opKey=`saveandnew`) · (errorMessage 未实抓 · 待 jar 反查)

## 🟡 平台/网络异常（待人工补充）

<TODO 人工补>
- HR Token 失效 / 跨租户 SSO 失败的兜底
- BEC 异步任务幂等失败的人工补单流程
- DB 锁冲突 / 长事务超时的常见现象
