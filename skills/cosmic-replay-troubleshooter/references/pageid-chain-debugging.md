# pageId 链路排查指南

## 核心认知

Cosmic 表单的 pageId 有三种来源，按照优先级从高到低：

1. **32hex 表单级 pageId** — 从 `showForm` 或 `addVirtualTab` 下发的 32 位 hex 值，是最精确的 pageId
2. **L2 pageId** — 从 `menuItemClick` 响应中通过 `addVirtualTab` 下发的 `{menuId}root{baseId}` 格式（51+ 字符）
3. **root_pageId** — 从 `getConfig.do` 获取的会话根 pageId（兜底）

## pageId 错误类型

### 类型 1：pageId 缺失（404 / No pageId error）
**症状**：`ProtocolError: no pageId in resp` 或 `HTTP 404`
**原因**：`open_form()` 或 `menuItemClick` 未正确执行
**修复**：确保 YAML 从 `menuItemClick` 开始

### 类型 2：pageId 过期（空响应，不报错）
**症状**：save 返回 `[]`，PASS 但数据未入库
**原因**：`saveandeffect` 后 pageId 失效但未重新获取
**修复**：runner 已自动处理（行 368-370），检查 `keep_page` 设置

### 类型 3：pageId 链路断裂（最隐蔽）
**症状**：`entryRowClick` / `hyperLinkClick` 响应中的 `addVirtualTab` 下发的 pageId 未传递到后续步骤
**原因**：`_pending_by_app` 机制缺失/未集成
**修复**：`replay.py` 三处修复（见下文）

### 类型 4：L2 pageId 屏蔽 `_pending_by_app`（2026-04-30 发现）
**症状**：全部修复后仍返回空 `[]`，`page_ids[form_id]` 是 L2 pageId（`/J9YH7GL2XOVroot...`）
**原因**：`runner.py` 的 `target_form` 绑定设置了 L2 pageId → `_pending_by_app` 后备永不触发
**修复**：pageId 查找时 `_pending_by_app` 优先于 L2 pageId（但不覆盖 32hex）

## 修复清单

### `lib/replay.py` 四层修复

```
修复级别 1：初始化
  __init__ 加 self._pending_by_app = {}

修复级别 2：调用
  invoke() 响应处理后调用 self._harvest_virtual_tab_pageids(resp)

修复级别 3：查找后备
  pageId 选择：page_id = _pending_by_app.get(app_id) or root_page_id

修复级别 4：优先级
  pageId 查找：_pending_by_app 优先于 L2 pageId
  条件：只当当前 pageId 是 L2 格式（len > 32 或含 '/'）时才覆盖
  不覆盖：32hex 表单级 pageId
```

### `lib/har_extractor.py`
- `_SAVE_BUTTON_KEYS` 标记 `btnsave` 等按钮为 `tier: core`
- 不改变 `ac`（保持 `click`，不改 `saveandeffect`）

## 诊断脚本

```python
# 在 invoke() 方法中加临时调试
def invoke(self, form_id, app_id, ac, actions, page_id=None):
    ...
    page_id = self.page_ids.get(form_id)
    print(f"[DIAG] {form_id}/{ac}:")
    print(f"  page_ids.get = {page_id}")
    print(f"  _pending_by_app = {dict(self._pending_by_app)}")
    pending_pid = self._pending_by_app.get(app_id)
    ...
    resp = self._post(...)
    print(f"  response length = {len(json.dumps(resp.json()))}")
    ...
```
