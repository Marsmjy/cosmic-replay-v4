# 失败诊断与修复指南

runner 跑失败后会自动打印 **修复建议（advisor）** 块。本指南告诉你**怎么看懂建议并动手改 YAML**。

## ⚠ 真跑挂了，怎么从头查

典型症状：runner 日志里每一步都 `[ok]`，但 save 挂在"请填写组织体系管理组织"/"请填写变动场景"。

**根因检查清单**（按顺序）：

1. **YAML 首个业务 step 是 `menuItemClick` 吗？**
   - 打开 YAML，看第一个 `type: invoke` 是不是 `ac: menuItemClick`
   - 如果不是 → 重录 HAR，录制时**必须从点菜单进入开始**，不要直接录已打开的页面

2. **HAR 里有侧边栏卡片加载吗？**
   - 搜 YAML 里有没有 `load_xxx_card` / `load_xxx_sidebar` 步骤
   - 没有 → HAR 录的太晚，重录

3. **click_addnew 步骤的 post_data 有 `treeview.focus` 吗？**
   - 看 click_addnew 的 `post_data` 字段：`[{"treeview":{"focus":{"id":...}}}, []]`
   - 没有 → 录制时没点击组织树节点，让浏览器先点一个树节点再点"新增"

4. **数据中心 id / base_url 是不是 `YOUR_*` 占位符？**
   - runner 启动时会自动拒绝，看报错提示

5. **还是挂？切到"日志"视图看 `bos_operationresult` 弹窗的错误详情**（advisor 会自动扒出来）

## 怎么看日志（服务异常 / 用例没反应 / UI 点了没动静时）

Web UI 顶栏点 **"日志"** 入口，有两个 tab：

- **服务日志**：进程实时日志。含 stdout/stderr/logging/异常栈。右上角有 error badge 红点。支持按 `error/warn/info` 过滤 + 关键字搜索 + 自动滚动。
- **执行历史**：每次 run 的完整事件流落盘在 `logs/runs/<run_id>.jsonl`，点左边一条历史看右边完整事件。

**落盘位置**：`cosmic-replay/logs/server-YYYYMMDD.log`（按天切分，30 天自动清理）

**CLI 跑时怎么看日志**：CLI 模式 runner 直接打终端，不经日志系统。如果要看历史事件，翻 `logs/runs/*.jsonl`。

## 反馈回路总览

```
  runner 跑用例
      ↓
  某一步失败 / 断言失败
      ↓
  diagnoser 扒 bos_operationresult 拿到中文错误
      ↓
  advisor 识别错误模式 → 推断字段 key → 给 YAML 补丁建议
      ↓
  你粘补丁到 YAML → 再跑
```

## 常见错误 → 修复套路

### 类型 1：`请填写"XXX"` / `XXX 不能为空`（必填缺失）

**advisor 输出**：
```
❐ [1] 请填写"组织体系管理组织"。
   字段: 'XXX' key=org
   建议补丁:
     - id: fill_org
       type: pick_basedata
       field_key: org
       value_id: "<该基础资料的 id>"
```

**怎么处理**：
- 如果推断的 `key` 对，直接按补丁加 step
- 如果 `value_id` 是占位符 `<...>`：
  - 打开浏览器 F12 → 在真实操作时看请求里的 `args` 里选中的 id
  - 或者用 `getLookUpList` 动作先查候选
- 如果 `key` 是 `?`（未推断出）：
  - 去 addnew 响应里搜对应 caption → 找到 `fieldName` 对应的 key
  - 或者在 `advisor.py` 的 `STATIC_FIELD_MAP` 加一条映射

### 类型 2：`XXX 值不合法: 特殊分隔符 _`

**advisor 输出**：
```
❐ 行政组织简体中文名称 值不合法: 特殊的分隔符\_
   建议: 把 vars 里相关值的 '_' 改成 '-'
   置信度: high
```

**怎么处理**：改 vars 里的占位符
```yaml
vars:
  test_name: 质量测试组织${test_number}       # 对
  # test_name: 质量_测试组织_${test_number}    # 错，含 _
```

### 类型 3：`XXX 已存在` / 重复

**advisor 输出**：
```
❐ 编号 已存在
   建议: 用 ${rand:5} 或 ${timestamp} 生成唯一值
```

**怎么处理**：
```yaml
vars:
  test_number: QA${rand:5}      # 每次运行不同
  # 或:
  test_number: QA${timestamp}   # 毫秒时间戳
```

### 类型 4：`XXX 格式不正确`

- 日期字段 → YAML 里写成 `${today}` 或 `YYYY-MM-DD` 字符串
- 数字字段 → 不要写带引号的字符串

### 类型 5：步骤本身 HTTP 报错（不走 advisor）

| 报错 | 原因 | 处理 |
|---|---|---|
| `HTTP 502` on getPublicKey | SIT 网关抖动 | 等 1-2 分钟重跑 |
| `No pageId for xxx` | 忘了 `open_form` | YAML 里加 open_form step |
| `No such accessible method: updateValue() on TextEdit` | `updateValue` 用了字段 key 作 `key` | 用 `type: update_fields`，key 是空串 |
| `For input string: "1010_S"` | 下拉枚举字段用了 `updateValue` 传字符串 | 改用 `pick_basedata` |

## advisor 的限制

advisor **不是万能的**，它擅长的：
- 中文错误 → 字段 key 推断（内置主流字段映射 + 会话响应动态学习）
- 必填缺失 → 建议 step 类型
- 下划线不合法 / 重复 / 格式类 → 高置信度建议

它**不擅长**的：
- 复杂业务规则（如"父组织生效日期必须早于子组织"）
- 字段间联动（如"选了 A 必须也选 B"）
- 权限类错误（如"没有新增权限"）

遇到以上情况，advisor 会给 `置信度: low` 或"未识别模式"，需要**人看 HAR + 中文错误自己推**。

## 实战示例：从 FAIL 到 PASS 的循环

假设跑了 `cases/admin_org_new.yaml`，第一次失败，advisor 给了 3 条建议。走一遍循环：

**第 1 轮**（当前跑出来的状态）：
```
[ERR] name 值不合法: _
[ERR] 缺 org
[ERR] 缺 changescene
```

**改 YAML**：
```yaml
vars:
  test_name: 质量测试组织${test_number}     # 去掉 _

steps:
  # ... 原有步骤
  # 在 save 之前加：
  - id: fill_org
    type: pick_basedata
    form_id: haos_adminorgdetail
    app_id: haos
    field_key: org
    value_id: "00"                          # 从 HAR 里抽到的 id

  - id: fill_changescene
    type: pick_basedata
    form_id: haos_adminorgdetail
    app_id: haos
    field_key: changescene
    value_id: "1010_S"
```

**第 2 轮**（再跑）：
- 可能还剩"缺 changetype"、"缺 parentorg"
- 继续按 advisor 建议补 step

**第 3 轮**（通常 2-3 轮就绿）：
```
[PASS]  duration 3.2s
  [ok] × N
ASSERTIONS:
  [ok] no_save_failure
  [ok] response_contains
```

入库、接 CI。

## 打不开的黑箱

如果 advisor 给的 key 错了，手动找 key 的方法：

1. **看 addnew 之后的 loadData 响应**——响应里有所有字段预填，每个字段的 `k` 就是你要的 key
   ```bash
   # 在 runner 代码里临时打印 ctx["response_history"] 即可
   ```
2. **看原始 HAR**——在你录的 HAR 里搜错误消息里的中文名（如"组织体系管理组织"），往附近找 `fieldCaption` 就能定位 `fieldName`
3. **问 cosmic-dev skill**——它懂具体元数据，可以直接告诉你"haos_adminorgdetail 的字段列表"

## 贡献：给 advisor 加映射

如果你发现某个字段中文名 advisor 没识别，在 `lib/advisor.py` 的 `STATIC_FIELD_MAP` 加一条：

```python
STATIC_FIELD_MAP = {
    # ...
    "你发现的字段中文名": "技术_key",
}
```

或者给特定 key 打类型 hint：
```python
KEY_TYPE_HINTS = {
    # ...
    "你的字段_key": "basedata",   # basedata / multilang_text / date / text / ...
}
```

每条映射都提升 advisor 的覆盖率。积累到 100+ 条后，行政组织/员工/部门等主流场景基本能一键给补丁。