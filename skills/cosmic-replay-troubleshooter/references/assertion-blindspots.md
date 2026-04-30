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

## 建议

- **save 步骤**的断言用 `no_save_failure`
- 非 save 步骤用 `no_error_actions`
- 需要可靠验证的用例两个断言都加
