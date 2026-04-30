---
name: cosmic-replay-troubleshooter
description: 苍穹业务流 HAR 导入 → YAML 用例的故障诊断与修复。覆盖变量识别、pageId、save未入库、登录脚本、环境变量五大失败模式。适合新的 AI agent 快速上手排故。
version: 1.2.0
triggers:
  - HAR导入
  - 变量识别
  - pageId不对
  - 入库失败
  - 未入库
  - save失败
  - 用例生成
  - cosmic-replay排故
  - 排故
  - cosmic-login
  - COSMIC_LOGIN_SCRIPT
  - 找脚本
  - .env
---

# Cosmic Replay Troubleshooter

苍穹 HAR 自动化测试的排故指南。给任何 AI agent 读，5 分钟内上手排故。

---

## 一、30秒看懂项目

```
cosmic-replay-v4/           # 项目根目录
├── lib/                    # 核心逻辑
│   ├── har_extractor.py    # HAR → YAML（1678行）
│   ├── runner.py           # YAML 执行引擎（1000行）
│   ├── replay.py           # API 调用封装（668行）
│   ├── advisor.py          # 修复建议生成器（447行）
│   └── webui/
│       └── server.py       # FastAPI 后端
├── cases/                  # YAML 用例资产
├── config/envs/            # 环境配置
├── .env                    # 敏感凭证
└── start.sh                # 启动脚本
```

**一句话**：上传 HAR 文件 → 自动生成 YAML 用例 → 跑用例验证业务流 → 诊断修复。

---

## 二、数据流全景（判断你在哪个环节出问题）

```
HAR文件（用户录制）
    │
    ▼
har_extractor.py       ← 环节 A：导入阶段 ← 80% 的问题出在这里
  detect_var_placeholders()
  → 生成 YAML 用例
    │
    ▼
runner.py              ← 环节 B：执行阶段
  run_case()
  → 逐步骤调用 replay.py
    │
    ▼
replay.py              ← 环节 C：API 调用
  invoke() / _refresh_session()
  → 发 HTTP 请求到苍穹
    │
    ▼
advisor.py             ← 环节 D：诊断输出
  get_fix_suggestions()
  → 修复建议
```

---

## 三、故障因果链（核心）

**核心认知**：
- 80% 的问题出在 HAR 导入阶段（变量识别/步骤裁剪/tier 分类）
- 15% 的问题出在执行层（pageId 管理/响应解析）
- 5% 的问题出在其他

```
症状：用户看到的
  save成功但数据没入库
  pageId不对 → 404 / 空响应
  字段值错误 → 保存报错
  报告PASS但数据不存在
      ↑
环节B/C：执行层问题
  pageId 追踪链路断裂 ← ⚠ 最隐蔽，返回空 [] 但不报错
  变量解析错误
  页面上下文缺失
  关键字段内容为空
      ↑
环节A：HAR导入层根因     ← 80% 的常规问题在这里
  变量没识别（硬编码值）
  上下文步骤缺失
  saveandeffect 被标为 optional
  click+btnsave 被标为 ui_reaction（非 core）
  基础资料ID硬编码
```

**修复路径**：改 YAML → 重新跑 → 验证

---

### 🔴 模式 A：变量没识别（HAR 导入阶段）

**症状**：
- 换环境跑，字段值还是旧的测试数据
- 报 "XXX已存在"（因为编码/名称没随机化）
- 字段值看起来像硬编码的原始值

**根因**：`detect_var_placeholders()` 的变量识别规则没覆盖到你的字段。

**诊断**：
```bash
# 看 YAML 的 vars 段，变量标签是不是空的
grep -A20 "^vars:" cases/xxx.yaml

# 检查字段名是否在识别规则中
grep "UNIQUE_KEY_HINTS\|ENV_RELATED_FIELDS\|ENUM_FIELDS" lib/har_extractor.py | head -5
```

**代码位置**：`lib/har_extractor.py` 行 371-724

**修复方案**：

| 场景 | 代码位置 | 修复 |
|------|---------|------|
| A-1: 字段是唯一标识（编码/编号/名称）没被变量化 | 行 385 `UNIQUE_KEY_HINTS` | 把字段 key 加进去 |
| A-2: 字段是环境相关基础资料（组织/企业/部门）没被变量化 | 行 639 `ENV_RELATED_FIELDS` | 把 field_key 加进去 |
| A-3: 字段是系统枚举值（性别/证件类型）被错误变量化了 | 行 654 `ENUM_FIELDS` | 把 field_key 加进去 |
| A-4: click 步骤的 post_data 里字段值没识别 | 行 553-580 `_extract_click_postdata()` | 检查 field_key 是否在识别范围内 |
| A-5（新增）: `newentry` 步骤的 post_data 里 name/number 字段值没变量化 | 行 613-642 `detect_var_placeholders()` | 新增 `ac=="newentry"` 分支，walk post_data[1] 中的 UNIQUE_KEY_HINTS 字段值 |
| A-6（新增）: `ename`（属性名称）被错误标记为"名称"变量 | 行 394 `HR_NAME_FIELDS` + 行 398 `_CLASSIFY_KEY_EXCLUSIONS` | `ename` 同时在 `HR_NAME_FIELDS` 和 `_CLASSIFY_KEY_EXCLUSIONS` 中。① 从 `HR_NAME_FIELDS` 移除 → 停止精确匹配为 name；② 加入 `_CLASSIFY_KEY_EXCLUSIONS` → 后缀匹配 `endswith("name")` 时跳过 → `_classify_key()` 返回 `None` → `ename` 保持 HAR 原始内容不变 |

**验证方法**：
```bash
# 修完后重新导入同一个 HAR
python3 -m lib.har_extractor extract xxx.har -o cases/xxx_new.yaml
# 检查生成的 vars 段
grep -A30 "^vars:" cases/xxx_new.yaml
```

---

### 🟡 模式 B：pageId 不对 / 请求 404（执行阶段）

**症状**：
- 跑用例时报 `ProtocolError: no pageId in resp`
- 表单 loadData 返回空数据
- save 时服务端说"找不到当前页面"
- save 返回 `[]` 但数据未入库（最隐蔽）

**根因**：pageId 有三种来源，需要分别排查：

| 场景 | 根因 | 诊断 | 修复 |
|------|------|------|------|
| B-1: YAML 缺了建立页面上下文的步骤 | HAR 录制时没有从菜单点击开始抓包，漏了 `menuItemClick` 和 `loadData` 序列 | 看 YAML 前3步：如果不是 `menuItemClick` 开头，肯定缺 | 重新录制：从**点击左侧菜单进入应用**开始抓包。har_extractor 自动从第一个 `menuItemClick` 开始裁剪 |
| B-2: 跑用例时 `_refresh_session()` 没兜住 | `replay.py` 行 177-194 的自动 pageId 收集没匹配到新下发的 pageId | 在 runner 输出里搜 `pageId` 看自动替换情况 | 手动检查 session_ready 事件中的 pageId 映射表 |
| B-3: pageId 链路断裂——`_pending_by_app` 未集成（**最隐蔽的无声失败**） | `replay.py` 三处缺失叠加：<br>1. `_harvest_virtual_tab_pageids()` 完全定义了但从未在 `invoke()` 中被调用<br>2. `_pending_by_app` 属性未在 `__init__` 中初始化<br>3. pageId 查找不检查 `_pending_by_app` 后备 | save 返回空 `[]`（非错误）、`replay.page_ids.get(form_id)` 返回 `None`。<br>诊断：在 invoke 调用前打印 `replay._pending_by_app` 确认是否为空。跟踪 HAR 中 `entryRowClick` → `addVirtualTab` 的 pageId 链路 | **4 处修复**（2026-04-30 实战验证）：<br>1. `__init__` 加 `self._pending_by_app = {}`<br>2. `invoke()` 响应处理后调用 `self._harvest_virtual_tab_pageids(resp)`<br>3. pageId 查找加 `self._pending_by_app.get(app_id)` 后备<br>4. `"new"` 加到 `("addnew", "modify", "copyBill", "edit")` 列表 |\n| B-4（新增 2026-04-30）: `_pending_by_app` 被 L2 pageId 屏蔽——**最隐蔽的无声失败** | B-3 全部修复后仍存在：`runner.py` 的 `target_form` 机制（行 359-366）在 `menuItemClick` 后把 L2 pageId（`{menuId}root{baseId}` 格式，51+字符）设为 `page_ids[form_id]`。后续 `loadData` 时 `page_ids.get(form_id)` 返回非空值，导致 `_pending_by_app` 后备从未触发。结果：所有操作使用列表态的 L2 pageId 而非详情态的 32hex pageId，save 返回空 `[]`。 | save 返回空 `[]`，`page_ids.get(form_id)` 返回 `{menuId}root...`（L2），而 `_pending_by_app[appId]` 有正确的 32hex pageId 但未被使用。<br>诊断：对比两者：<br>`print(page_ids.get(fid), _pending_by_app.get(aid))` | **`replay.py:389-396`** — pageId 查找改为 `_pending_by_app` 优先于 L2 pageId，但不覆盖 32hex 表单级 pageId：<br>```python<br>page_id = self.page_ids.get(form_id)<br>pending_pid = self._pending_by_app.get(app_id)<br>if (pending_pid and len(pending_pid) >= 16<br>        and page_id != pending_pid<br>        and (page_id is None or len(page_id) > 32 or '/' in page_id)):<br>    page_id = pending_pid<br>```<br>**判断条件解释**：`len(page_id) > 32` → L2 pageId（51+）；`'/' in page_id` → 也是 L2；32 字符 → 32hex 表单级 pageId，不覆盖。 |

**关于 pageId 的关键机制**（读这段再动手）：
```
replay.py 行 178: PAGEID_FIELD_NAMES = ("pageId", "parentPageId")
  → 每次 API 响应自动收集 pageId
replay.py 行 190: form_id → pageId 映射
  → 维护表单ID到pageId的映射表
replay.py 行 218: init_root()
  → 从 getConfig.do GET 拿会话根 pageId
replay.py 行 245: open_form()
  → 为指定表单申请/复用 pageId

注意：saveandeffect 后 pageId 失效（runner.py 行 368-370）
  → runner 会自动清除旧 pageId，下次调用时重新申请
```

**验证**：
```bash
# 跑一下看 pageId 相关错误
python3 -m lib.runner run cases/xxx.yaml 2>&1 | grep -i "pageId\|404\|page"
```

---

### 🔴 模式 C：save 成功但数据没入库（执行阶段）

**症状**：
- runner 输出 PASS
- 但数据库查不到保存的数据
- 这是最隐蔽的失败模式（无声失败）

**根因链**（从症状往前推）：

```
症状：PASS 但无数据
  ↑
检查项1：saveandeffect 是否被正确执行？
  场景 C-1: saveandeffect 被标为 optional → runner 跳过了
            → 检查 YAML 中 saveandeffect 步骤的 tier 字段
            → 如果不是 "core"，改之
  代码位置：runner.py 行 368-370

  ↑
检查项2：关键上下文字段是否缺失？
  场景 C-2: 缺少 org/changescene/parentorg 等字段
            → 服务端校验不通过但静默返回 success
            → 需要补 pick_basedata 步骤
  诊断：看 advisor 输出，搜 "建议补字段"
  修复：在 save 前插入对应 pick_basedata 步骤

  ↑
检查项3：saveandeffect 没有被正确标记
  场景 C-3: HAR 中 save 步骤被标成了 UI 层级而不是 core
            → har_extractor 的 AC_TIER 分类没命中
  代码位置：har_extractor.py 中 AC_TIER 相关逻辑

  ↑
检查项4：基础资料 ID 在目标环境不存在
  场景 C-4: ${resolve:basedata:...} 引用的 ID 在新环境没有
            → 解析失败 → 字段值为空 → 入库时被忽略
  诊断：在 session_ready 日志里看 vars 的解析值
  修复：在 config/envs/xxx.yaml 里更新对应的基础资料 ID

  ↑
  场景 C-5: HAR 中的保存动作是 ac=click, key=btnsave, method=click（新增）
            → 某些表单的保存操作本身就是 ac=click, key=btnsave（非标准 saveandeffect）
            → ⚠ 不要改成 saveandeffect！这个表单的保存就是 ac=click
            → ac=click 不在 AC_TIER 中 → 默认降为 ui_reaction → 被标 optional
            → 同时 pageId 链路断裂时返回空 [] 但不报错
  诊断：grep "btnsave\|ac: click" cases/xxx.yaml
        → 找到 btnsave 步骤后检查是否被标了 optional
  修复（两步）：
        (a) YAML 侧：去掉 optional, 加 tier: core
            注意：不要改成 saveandeffect！这个表单的保存就是 ac=click
        (b) 代码侧（根治）：在 har_extractor.py 的 action 处理循环中，
            当 ac=click 且 key in _SAVE_BUTTON_KEYS 时，设置 tier=core
            参考：lib/har_extractor.py 行 767-769
```

**验证方法（每次 PASS 后必须执行）**：
```bash
# 验证1：确认 saveandeffect 存在且在 core 层级
grep -A3 "saveandeffect\|submitandeffect" cases/xxx.yaml | head -10

# 验证2：确认 save 前有必要的基础资料字段
# 看 advisor 输出是否提示缺少字段

# 验证3：确认变量解析正确（看执行日志第一条）
# 在 Web UI 执行日志里找 session_ready 事件的 vars 段

# 验证4：确认 save 步骤属性
grep -B2 -A12 "btnsave" cases/xxx.yaml
# 检查：ac 是 click 还是 saveandeffect
# 检查：没有 optional: true
# 检查：tier: core 或没有 optional 标记

# 验证5：断言选择
# no_save_failure → 检查 save 专属错误（字段级校验如"数据已存在"也会捕获）
# no_error_actions → 只检查 bos_operationresult 级错误（save 字段校验漏报）
# ⚠ save 步骤的断言应该用 no_save_failure，不要用 no_error_actions
# 详见 references/assertion-blindspots.md
```

---

### 🟢 模式 D：登录失败——找不到 cosmic_login.py（启动阶段）

**症状**：
- `FileNotFoundError: 找不到 cosmic-login skill。请设置 COSMIC_LOGIN_SCRIPT 环境变量`
- 开发环境 `python3 -m lib.webui.server` 直接启动时必出（因为没设环境变量）
- `./start.sh` 启动时不出（因为脚本里设了 `COSMIC_LOGIN_SCRIPT`）

**根因**：`_find_login_script()` 的搜索路径没覆盖 `lib/cosmic_login.py`（与 `replay.py` 同目录）。当 `COSMIC_LOGIN_SCRIPT` 环境变量未设时，搜索路径 `cosmic-login/cosmic_login.py` 和 `.claude/skills/cosmic-login/cosmic_login.py` 都无法命中。

**修复方案**：

| 场景 | 代码位置 | 修复 |
|------|---------|------|
| D-1: 源码级修复（永久） | `lib/replay.py` 中 `_find_login_script()` | 在循环搜索前加一行：`same_dir = here.parent / "cosmic_login.py"` |
| D-2: Web UI 启动加载 `.env`（预防性） | `lib/webui/server.py` | 在 `main()` 第一行调用 `_load_dotenv()`，函数从项目根 `.env` 文件读取所有变量注入 `os.environ` |
| D-3: 临时修复（老代码还能跑） | 启动前 export | `export COSMIC_LOGIN_SCRIPT=/path/to/cosmic-replay-v4/lib/cosmic_login.py` |

**完整修复代码**（lib/replay.py `_find_login_script`）：
```python
here = Path(__file__).resolve()
# 先找同目录下的 cosmic_login.py（lib/ 目录，和 replay.py 同目录）
same_dir = here.parent / "cosmic_login.py"
if same_dir.exists():
    return same_dir
for parent in [here.parent.parent.parent, ...]:
    ...
```

**`.env` 加载函数**（lib/webui/server.py）：
```python
def _load_dotenv():
    dotenv_path = Path(__file__).resolve().parent.parent.parent / ".env"
    if dotenv_path.exists():
        for line in dotenv_path.read_text().splitlines():
            line = line.strip()
            if not line or line.startswith("#") or "=" not in line:
                continue
            key, _, val = line.partition("=")
            key, val = key.strip(), val.strip().strip("\"'").strip()
            if key and val and key not in os.environ:
                os.environ[key] = val
```

**验证**（无 `COSMIC_LOGIN_SCRIPT` 环境变量时）：
```bash
python3 -c "
import sys, os; sys.path.insert(0, '.')
os.environ.pop('COSMIC_LOGIN_SCRIPT', None)
from lib.replay import _find_login_script
print(_find_login_script())  # 应输出 /.../cosmic-replay-v4/lib/cosmic_login.py
"
```

---

## 四、快速修复循环（三步法）

**每次 FAIL 后的标准诊断路径**：

```
第1步：跑一下看输出
  python3 -m lib.runner run cases/xxx.yaml

  结果判断：
  ├─ 有明确错误信息 → 去 advisor 找修复建议
  │   └─ advisor 给了建议？→ 按建议改 YAML
  │   └─ advisor 没给建议？→ 看错误类型，按上面的模式查
  │
  ├─ 输出 PASS
  │   └─ 执行"验证方法"（看上面模式 C 的验证清单）
  │   └─ 验证通过 → 真实 PASS
  │   └─ 验证失败 → 数据没落库 → 按模式 C 查
  │
  └─ 输出 404 / pageId 错误 → 按模式 B 查

第2步：改 YAML
  ├─ 补字段 → 插入到 save 步骤之前（updateValue / pick_basedata）
  ├─ 补步骤 → 从 menuItemClick 开始（缺上下文情况下）
  ├─ 改变量 → 更新 har_extractor.py 的规则 → 重新导入
  ├─ 改层级 → saveandeffect 的 tier 改成 "core"
  └─ 改类型 → ac=click + key=btnsave → ac=saveandeffect（模式 C-5）
  注意：补了字段后确认旧步骤没有冲突值

第3步：再跑 → 执行验证 → 直到真实通过
```

---

## 五、关键代码速查表

| 文件 | 核心函数/结构 | 行号 | 作用 | 你什么时候改它 |
|------|-------------|------|------|--------------|
| `har_extractor.py` | `detect_var_placeholders()` | 371-724 | 变量识别引擎 | 变量没识别/识别错了时 |
| `har_extractor.py` | `UNIQUE_KEY_HINTS` | 385 | 唯一标识字段名单 | 编码/编号/名称没被变量化 |
| `har_extractor.py` | `_CLASSIFY_KEY_EXCLUSIONS` | 398 | 后缀匹配排障：ename 等被误识别为 name | 属性名称被变量化时 → 把字段 key 加进来 |
| `har_extractor.py` | `ENV_RELATED_FIELDS` | 639-651 | 环境相关基础资料名单 | 组织/企业/部门没变量化 |
| `har_extractor.py` | `ENUM_FIELDS` | 654-660 | 系统枚举值名单 | 性别/类型被错误变量化 |
| `har_extractor.py` | `_extract_click_postdata()` | 553-580 | click步骤post_data变量化 | 用户直接保存的字段没变量化 |
| `har_extractor.py` | newentry 步骤变量化 | 617-645 | newentry 步骤的 post_data 中 name/number 字段抽变量 | 业务模型附表等场景中新增条目行字段值硬编码时 |
| `har_extractor.py` | `_SAVE_BUTTON_KEYS` | 77 | btnsave→core 标记 | HAR把保存录成click时 |
| `har_extractor.py` | AC_TIER + btnsave转换 | 760-767 | click+btnsave自动转saveandeffect | 新增save按钮类型时 |
| `runner.py` | `STEP_HANDLERS` | 312 | 步骤类型执行器映射 | 添加/修改步骤处理逻辑 |
| `runner.py` | `run_case()` | 536 | 用例执行主循环 | 修改执行流程 |
| `runner.py` | saveandeffect pageId失效逻辑 | 368-370 | save后清pageId | pageId刷新异常时 |
| `replay.py` | `_harvest_virtual_tab_pageids()` + `_pending_by_app` | 480-510 | 扫 addVirtualTab 按 appId 存 pending pageId | entryRowClick 后表单 pageId 丢失时 → 检查此链路是否通 |\n| `replay.py` | pageId 查找（含 `_pending_by_app` 优先逻辑） | 389-396 | L2 pageId vs _pending_by_app 优先级选择 | 调整 L2→表单 pageId 的覆盖条件 |
| `replay.py` | `init_root()` | 218 | 会话根 pageId | 初始化失败时 |
| `replay.py` | `open_form()` | 245 | 表单 pageId 申请 | 表单打不开时 |
| `advisor.py` | `FixSuggestion` | 123-143 | 修复建议数据结构 | 修改建议格式 |
| `config/envs/sit.yaml` | 环境配置 | 全部 | 基础资料ID/环境变量 | 换环境时更新 |

---

## 六、环境配置清单

```yaml
# config/envs/sit.yaml
base_url: "https://xxx.kingdee.com"
# 敏感凭证放 .env（不提交 git），用环境变量引用
# COSMIC_USERNAME=xxx
# COSMIC_PASSWORD=xxx
# COSMIC_DATACENTER_ID=xxx
```

**环境迁移时的必改项**（在 config/envs/xxx.yaml）：
- `base_url`：目标环境地址
- 基础资料 ID（用户/岗位/组织等）在新环境的数值
- `datacenterId`：数据中心 ID

---

## 七、常用诊断命令速查

```bash
# 启动服务（端口 8768）
python3 -m lib.webui.server

# 导入 HAR → 生成 YAML
python3 -m lib.har_extractor extract xxx.har -o cases/xxx.yaml

# 跑用例
python3 -m lib.runner run cases/xxx.yaml

# 看 YAML 变量模板
grep -A30 "^vars:" cases/xxx.yaml

# 看执行日志中的变量解析
# 在 Web UI 中找 session_ready 事件的 vars 段

# 检查端口占用
lsof -i :8768

# 检查 save 步骤类型（模式 C-5 快速排查）
grep -B2 -A10 "btnsave\|ac: click\|ac: saveandeffect" cases/xxx.yaml
```

---

## 八、做事的红线（读三遍）

1. **不要在 YAML 里硬编码密码/敏感信息** → 永远用环境变量 `${env:XXX}`
2. **不要在 YAML 里硬编码 pageId 或 traceId** → 这些是动态值，执行器会管理
3. **不要删 menuItemClick 步骤** → 那是页面上下文的起点
4. **不要删 treeview.focus** → 那是用户点击过的树节点，决定了当前上下文
5. **不要删 changeYear** → 每个日期 updateValue 前都有它，跳过会导致日期不落值
6. **不要把 saveandeffect 标为 optional** → 核心步骤，必须 core
7. **PASS 不代表数据落库** → 每次 PASS 后执行验证清单
8. **不要在 YAML 里写死基础资料 ID** → 用 `${resolve:basedata:...}` 或 `${vars.xxx_id}`
9. **出现问题先看这个文档** → 按故障因果链定位，不要盲目改代码
10. **如果 YAML 里有 ac: click + key: btnsave** → 
    (a) 检查是否被标了 optional → 改为 tier: core
    (b) **不要改成 saveandeffect** — 这个表单的保存就是 ac=click，不是所有 save 都是 saveandeffect
    (c) 同时检查 pageId 链路：`entryRowClick → addVirtualTab → _pending_by_app` 是否完整
11. **Web UI 启动报找不到 cosmic_login.py** → 检查两处修复：(a) `lib/replay.py` 的 `_find_login_script()` 是否加了 `same_dir` 搜索；(b) `lib/webui/server.py` 的 `main()` 是否调了 `_load_dotenv()`。改完后必须重启进程（Python 缓存模块）
12. **⛔ 改代码后必须重启 Web UI** → Python 的 uvicorn 在启动时一次性加载所有模块。对 `lib/replay.py` / `lib/runner.py` / `lib/har_extractor.py` 的任何修改，在旧的 Web UI 进程（PID）上永远不会生效。可以 kill 后 `python3 -m lib.webui.server --port 8768` 重启
13. **断言选择** → save 步骤用 `no_save_failure`（捕获字段级校验错误），不要用 `no_error_actions`（漏报"数据已存在"和"名称重复"）
14. **`newentry` 步骤的 post_data 字段值也要检查** → `ac=newentry` 的 `post_data[1]` 中的 `name`/`number` 等字段值也可能不是变量化的。如果看到 "ppppp1" 等硬编码值，检查 `detect_var_placeholders()` 的 `newentry` 分支（`har_extractor.py` 行 617-645）。不想变量化的字段（如 `ename` 属性名称）加到 `_CLASSIFY_KEY_EXCLUSIONS` 集合中

---

## 九、项目标准启动命令

```bash
# 安装依赖
pip install -r requirements.txt

# 首次初始化
python3 -m lib.webui.server --init

# 启动 Web UI（默认 8768 端口）
python3 -m lib.webui.server

# 或使用启动脚本
./start.sh
```

---

## 附录：常见错误对照表

| 错误信息 | 可能原因 | 参考模式 |
|---------|---------|---------|
| `ProtocolError: no pageId in resp` | 缺页面上下文步骤 / session 过期 | 模式 B |
| `no_save_failure` | 缺少必填字段（org/changescene 等） | 模式 C-2 |
| 提示"XXX已存在" | 编码/名称没变量化，跑重了 | 模式 A-1 |
| save 返回 success 但查无数据 | saveandeffect 被跳过 | 模式 C-1 |
| `${resolve:basedata:...}` 解析报错 | 基础资料 ID 在目标环境不存在 | 模式 C-4 |
| 字段值还是旧环境的测试数据 | 没被变量识别规则命中 | 模式 A-2 |
| HAR 导入后 YAML 只有 savedeffect 没有 menuItemClick | 录制范围不对，缺少入口 | 模式 B-1 |
| YAML 中 ac: click + key: btnsave，PASS 但无数据 | HAR 把保存录成 click 未转 saveandeffect | 模式 C-5 |
| `ename` 被变量化为 `test_name`（不应变量化） | `_CLASSIFY_KEY_EXCLUSIONS` 中缺少 `ename`，或 `HR_NAME_FIELDS` 未移除 | 模式 A-6 |
| `name`/`number` 在 `newentry` 步骤中硬编码（如 "ppppp1"） | `detect_var_placeholders()` 的 `newentry` 分支未触发 | 模式 A-5 |
| `FileNotFoundError: 找不到 cosmic-login skill` | `COSMIC_LOGIN_SCRIPT` 未设 + `_find_login_script()` 搜索路径没覆盖 | 模式 D |
| save 返回空 `[]`，`page_ids.get(form_id)` 为 None | `_pending_by_app` 链路断裂 | 模式 B-3 |
