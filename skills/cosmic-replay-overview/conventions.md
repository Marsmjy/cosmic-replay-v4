# 开发规范与常见陷阱

## YAML 用例 Schema

### 完整结构

```yaml
name: 用例唯一标识
description: 业务描述（中文）

env:
  base_url: ${env:COSMIC_BASE_URL}
  username: ${env:COSMIC_USERNAME}
  password: ${env:COSMIC_PASSWORD}
  datacenter_id: ${env:COSMIC_DATACENTER_ID}

vars:
  test_number: "KDTEST${rand:6}"
  test_name: "测试${vars.test_number}"

vars_labels:
  test_number: "编码"
  test_name: "名称"

main_form_id: target_form_id  # 可选，主表单自动预开

pick_fields:  # 可选，前端变量面板
  pick_org:
    field_key: org
    value_id: "1000"
    value_name: "环宇集团"
    label: "所属组织"
    env_sensitive: medium

steps:
  - id: step_unique_id        # 唯一标识
    type: open_form|invoke|update_fields|pick_basedata|click_toolbar|click_menu|sleep
    form_id: target_form      # 必填（sleep 除外）
    app_id: app_identifier    # 必填（sleep 除外）
    description: "中文描述"   # 可选，前端展示
    optional: false           # 可选，失败是否继续
    capture: var_name         # 可选，响应存入 vars

assertions:
  - type: no_error_actions|no_save_failure|response_contains|response_pkValue_not_empty|flow_started
    step: step_id             # 检查哪个步骤的响应
```

### 变量占位符

| 格式 | 说明 | 示例 |
|------|------|------|
| `${vars.key}` | 引用 vars 段变量 | `${vars.test_number}` |
| `${env:NAME}` | 系统环境变量 | `${env:COSMIC_BASE_URL}` |
| `${env:NAME:default}` | 带默认值 | `${env:PORT:8768}` |
| `${timestamp}` | 当前毫秒时间戳 | `1715616000000` |
| `${today}` | 当前日期 | `2026-05-14` |
| `${now}` | 当前时间 | `2026-05-14 10:30:00` |
| `${rand:N}` | N位随机数字 | `${rand:6}` → `482957` |
| `${uuid}` | UUID hex | 32位十六进制 |
| `${session.root_page_id}` | 会话根 pageId | 运行时自动填充 |
| `${session.root_base_id}` | 根 pageId 32hex 部分 | 运行时自动填充 |

### Step 类型详解

**open_form**
```yaml
- id: open_main
  type: open_form
  form_id: haos_adminorgdetail
  app_id: haos
  lazy: true  # 默认true，复用缓存pageId
```

**invoke**
```yaml
- id: save_main
  type: invoke
  form_id: haos_adminorgdetail
  app_id: haos
  ac: save               # 操作动作
  key: tbmain            # 组件 key
  method: itemClick      # 方法名
  args: [save]           # 方法参数
  post_data: [{}, []]    # 表单数据
  keep_page: false       # save后是否保留pageId
  invalidate_pages: []   # 执行前清除哪些form的pageId
```

**update_fields**
```yaml
- id: fill_info
  type: update_fields
  form_id: haos_adminorgdetail
  app_id: haos
  fields:
    number: ${vars.test_number}
    name:
      zh_CN: ${vars.test_name}
      zh_TW: ${vars.test_name}
  row_index: -1  # -1=主表头，0+=分录行号
```

**pick_basedata**
```yaml
- id: pick_org
  type: pick_basedata
  form_id: haos_adminorgdetail
  app_id: haos
  field_key: org
  value_id: "1000"
```

## 编码规范

### 新增 Step Handler

```python
@step_handler("new_type_name")
def _h_new_type(step: dict, replay: CosmicFormReplay, ctx: dict) -> Any:
    """Handler 文档字符串"""
    # step: 解析后的 YAML step dict（已变量替换）
    # replay: 回放器实例
    # ctx: 上下文（含 vars, step_responses, last_response 等）
    result = replay.some_method(...)
    return result  # 返回值会被存入 ctx["step_responses"][step_id]
```

### 新增断言

```python
@assertion_handler("new_assertion_type")
def _a_new_check(assert_spec: dict, ctx: dict) -> tuple[bool, str]:
    """
    返回 (passed: bool, message: str)
    passed=True 时 message 是成功描述
    passed=False 时 message 是失败原因
    """
    resp = ctx["step_responses"].get(assert_spec.get("step"))
    if resp is None:
        return False, "找不到指定步骤的响应"
    # 检查逻辑...
    return True, "✅ 检查通过"
```

### 代码风格
- 日志用 `log = logging.getLogger("cosmic_replay.module_name")`
- 错误分层：`CosmicError > LoginError / ProtocolError / BusinessError`
- 内部变量前缀 `_`，如 `_target_form`, `_stale_page_detected`
- 中文注释标记重要设计用 `# ⭐`

## 常见陷阱与解决

### 1. PageId 过期（"页面未初始化或者已经过期"）
**原因**：save/submit 后 pageId 失效，但后续步骤仍引用旧 pageId
**解决**：
- runner 已有安全网：检测到此错误会自动 re-open + loadData + 重试
- 检查 step 的 `keep_page` 设置是否正确
- 检查 `invalidate_pages` 是否遗漏

### 2. 变量未被识别（HAR 转换后硬编码）
**原因**：字段名不在变量分类规则中，或实时元数据 / 知识库未命中
**解决**：
- 唯一字段：检查 `har_extractor.py` 的 `_UNIQUE_KEY_HINTS` 和 `_classify_key_heuristic`
- 文本字段：检查 `_TEXT_VARIABLE_KEYS`，`description/remark/memo/note` 应进入 `vars`
- HR 实体字段：检查 `kb_loader.resolve_scene(form_id)` 是否能从 `cosmic-hr-expert` 的 `scenarios/` 或 `_shared/_standard_metadata/entity_metadata/` 命中
- 在线环境：确认 preview/extract 传入 `MetadataResolver`，即 `/metadata/getEntityType.do?entityId=` 可访问

### 3. menuItemClick 后 pageId 错误
**原因**：L2 pageId 未正确计算或绑定
**解决**：
- 检查 runner.py 中 `ac == "menuItemClick"` 的 L2 pageId 计算逻辑
- 确保 `target_form` 或 `main_form_id` 正确设置
- 公式：`l2_pid = f"{menuId}root{session.root_base_id}"`

### 4. 断言通过但数据未入库
**原因**：用了 `no_error_actions` 而非 `no_save_failure`
**解决**：save 步骤必须用 `no_save_failure`（检查 bos_operationresult）

### 5. 编码/名称重复（"XXX 已存在"）
**原因**：vars 中未使用随机化
**解决**：确保唯一键变量包含 `${rand:N}` 或 `${timestamp}`

### 6. 基础资料跨环境失败
**原因**：value_id 在不同环境不同（如组织 ID）
**解决**：使用 `pick_fields` 标记为 env_sensitive，前端面板修改

### 7. 登录失败
**原因**：凭证错误、环境变量未设置、数据中心 ID 不对
**解决**：检查 config/envs/*.yaml 或 .env 文件

## 配置系统

### 两层架构

**Layer 1: config/webui.yaml（全局偏好）**
```yaml
port: 8768
host: 127.0.0.1
open_browser: true
default_env: sit
cases_dir: ./cases
har_upload_dir: ./har_uploads
```

**Layer 2: config/envs/{id}.yaml（环境配置）**
```yaml
name: SIT环境
base_url: https://xxx.kingdee.com/feature_sit_hrpro
datacenter_id: 0MUWQ6HSY5JA
credentials:
  username: ${env:COSMIC_USERNAME}
  password: ${env:COSMIC_PASSWORD}
sign_required: false
timeout: 30
```

**优先级**：环境变量 > 配置文件明文 > 默认值
