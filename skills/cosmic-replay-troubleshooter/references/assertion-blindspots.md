# 断言盲区

## `no_error_actions` vs `no_save_failure`

| 断言类型 | 检测范围 | 漏报场景 |
|---------|---------|---------|
| `no_error_actions` | `showErrMsg`、`ShowNotificationMsg`（非成功类） | **字段级校验错误**（如 `showFieldTips` 中的"数据已存在"） |
| `no_save_failure` | `bos_operationresult` + 字段级 save 错误 | 非 save 步骤的其他错误 |

## 典型漏报案例

### 案例 1："数据已存在"
服务器返回：
```json
{"a": "showFieldTips", "p": [
  {"success": false, "fieldKey": "name", "tip": "数据已存在"}
]}
```
`no_error_actions` ✅ PASS（漏报）
`no_save_failure` ❌ FAIL（正确捕获）

### 案例 2：save 返回空 `[]`
服务器返回 `[]`（2 字节）。
`no_error_actions` ✅ PASS（漏报——空数组不含任何 action）
`no_save_failure` ✅ PASS（也漏报——没有 bos_operationresult）

这种情况需要检查 pageId 链路，见 `pageid-chain-debugging.md`

### 案例 3：保存成功但通用回查失败
服务器保存响应包含“保存成功”、字段回写或页面状态变化，且：

```text
no_save_failure ✅ PASS
no_error_actions ✅ PASS
readback_by_business_key ❌ FAIL
```

如果失败信息类似：

```text
入库回查未找到：<form>.<business_key> = <value>
只读 commonSearch，响应未包含 grid 行或业务键文本
```

不要立刻判定为保存失败。某些受控基础资料、组织上下文表单或列表过滤复杂的表单，通用 `commonSearch` 不一定能查到刚保存的数据。若用户在业务系统确认已入库，应归类为 `readback_assertion_gap`：保存链路成功，但回查策略不适配。

处理顺序：

1. 先看保存步骤响应是否有“保存成功”、主键、字段回写或状态变化。
2. 再看 pageId 链路是否正确，尤其保存是否使用 L3。
3. 运行 `scripts/deep_chain_pipeline.py readback-plan --case <yaml>` 生成建议回查。
4. 只有存在表单专用可靠回查策略时，才把 `readback_by_business_key` 作为硬断言。
5. 通用 `commonSearch` 只能作为 advisory 建议；失败后不要硬补 `save.post_data`，也不要删除 `no_save_failure` 绕过问题。

## 建议

- **save 步骤**的断言用 `no_save_failure`
- 非 save 步骤用 `no_error_actions`
- 需要可靠验证的用例两个断言都加
- 入库回查断言必须区分“专用可靠策略”和“通用建议策略”。通用 `commonSearch` 失败时，先按 `readback_assertion_gap` 排查，不要把已入库数据误判为自动化失败。
