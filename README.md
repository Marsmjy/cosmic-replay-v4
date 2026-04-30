# COSMIC REPLAY v4

> 苍穹表单协议回放自动化测试工具 — HAR 录制一键生成可执行 YAML 用例

[![Python](https://img.shields.io/badge/Python-3.10+-blue.svg)](https://python.org)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 项目定位

上传浏览器 HAR → 自动生成 YAML 测试用例 → 直接运行验证业务流。

苍穹平台没有开放运行期业务数据 OpenAPI，纯手工回归测试成本高。本工具直接回放 `batchInvokeAction.do` 协议，3-5 秒一条用例。

---

## 两步上手

### 第一步：部署项目

```bash
# 1. 解压 / 克隆
git clone https://github.com/Marsmjy/cosmic-replay-v4.git
cd cosmic-replay-v4

# 2. 装依赖（Python 3.10+ 必须）
pip install -r requirements.txt

# 3. 启动
./start.sh

# 4. 浏览器打开
open http://127.0.0.1:8768
```

启动后上传 HAR 即可导入生成用例。如果要**执行用例**，需要先配置苍穹账号：

```bash
cp .env.example .env
# 编辑 .env 填入：
#   COSMIC_USERNAME=你的账号
#   COSMIC_PASSWORD=你的密码
#   COSMIC_DATACENTER_ID=你的数据中心ID
```

### 第二步：录 HAR → 导入 → 运行

```
浏览器开苍穹 → F12 → Network → 勾选 Preserve log
      ↓
完整操作一遍业务（进入菜单 → 新增 → 填字段 → 保存）
      ↓
右键 Network 列表 → Save All As HAR
      ↓
Web UI → HAR 导入 → 选择刚保存的 HAR
      ↓
自动生成 YAML → 点执行 → 验证入库
```

---

## 给 AI Agent 用

项目自带排故 Skill 包，直接加载即可获得排故能力：

```bash
# Claude Code
claude --load skills/cosmic-replay-troubleshooter

# 安装到 QoderWork
cp -r skills/cosmic-replay-troubleshooter ~/.qoderwork/skills/

# 安装到 Cursor
cp skills/cosmic-replay-troubleshooter/SKILL.md .cursor/rules/
```

Skill 包含完整的故障因果链和修复方案（变量识别漏报 / pageId 追踪断裂 / save 不落库等），AI 读完后能自动诊断并给出 YAML 补丁。

---

## 常见踩坑

| 问题 | 原因 | 解决 |
|------|------|------|
| `TypeError: unsupported operand type(s) for` | Python < 3.10 | 升级到 Python 3.10+ |
| `RSA 加密库不可用` | 缺 pycryptodome | `pip install pycryptodome` |
| `找不到 cosmic-login skill` | 环境变量未设 | 用 `./start.sh` 启动（自动处理），或手动 `export COSMIC_LOGIN_SCRIPT=项目路径/lib/cosmic_login.py` |
| save 返回空 `[]` 且 PASS | pageId 追踪断裂 | 重启 Web UI 后重试 |
| `XXX 已存在` | 编码/名称没随机化 | 检查 YAML 的 vars 段是否用了 `${rand:N}` |
| PASS 但数据未入库 | 断言用错了 | save 步骤把 `no_error_actions` 改成 `no_save_failure` |

---

## 目录结构

```
cosmic-replay-v4/
├── lib/              # 核心逻辑（变量识别/执行引擎/苍穹 API 封装）
├── cases/            # YAML 测试用例资产
├── docs/             # 使用文档
│   ├── user_guide.md           # Web UI 操作
│   ├── operating_guide.md      # CLI 操作
│   ├── troubleshooting.md      # 排故指南
│   └── HAR_TO_YAML_SOP.md     # HAR 录制规范
├── skills/           # AI Agent 排故 skill
│   └── cosmic-replay-troubleshooter/
├── config/           # 环境配置（不提交 git）
├── har_uploads/      # 上传的 HAR
└── docs/archive/     # 历史分析报告（仅存档，不需阅读）
```

---

## 排故索引

| 症状 | 排查方向 |
|------|---------|
| 变量没识别 / 硬编码值 | 模式 A — 检查 `UNIQUE_KEY_HINTS` / `ENV_RELATED_FIELDS` |
| pageId 不对 / 404 | 模式 B — 检查 `menuItemClick` 步骤 / `_pending_by_app` 链路 |
| save 返回空 `[]` | 模式 B-4 — `L2 pageId` 屏蔽了 `_pending_by_app` |
| PASS 但数据未入库 | 模式 C — 断言换 `no_save_failure`，检查字段缺失 |
| 启动时报找不到脚本 | 模式 D — `_find_login_script()` / `_load_dotenv()` |

完整排故见 `docs/troubleshooting.md` 或 `skills/cosmic-replay-troubleshooter/SKILL.md`。
