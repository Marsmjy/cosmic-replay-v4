# COSMIC REPLAY v4

> 苍穹表单协议回放自动化测试工具 — HAR 录制一键生成可执行 YAML 用例

[![Python](https://img.shields.io/badge/Python-3.10+-blue.svg)](https://python.org)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active-success.svg)]()

---

## 项目定位

**一句话**：上传浏览器录制 HAR → 自动生成 YAML 测试用例 → 直接运行验证业务流。

苍穹平台没有开放运行期业务数据 OpenAPI，纯手工回归测试成本高。本工具直接回放 `batchInvokeAction.do` 协议，3-5 秒一条用例，无头无浏览器。

---

## 安装

```bash
git clone https://github.com/Marsmjy/cosmic-replay-v4.git
cd cosmic-replay-v4
pip install -r requirements.txt
```

### 配置

```bash
cp .env.example .env
# 编辑 .env 填入你的金蝶账号密码和 datacenterId
```

`.env` 文件内容示例：

```
COSMIC_USERNAME=你的手机号
COSMIC_PASSWORD=你的密码
COSMIC_DATACENTER_ID=你的数据中心ID
```

---

## 启动

```bash
./start.sh
```

访问 http://127.0.0.1:8768

或手动启动：

```bash
python3 -m lib.webui.server --init   # 首次初始化
python3 -m lib.webui.server           # 启动
```

---

## 工作流

```
浏览器 F12 → Netwok → 勾选 Preserve log
       ↓
 完整操作业务（新增→填写→保存）
       ↓
 右键保存为 xxx.har
       ↓
 Web UI → HAR导入 → 自动生成 YAML
       ↓
 审查/编辑 YAML → 点击运行
       ↓
 ① PASS → 数据入库 → 完成
 ② FAIL → advisor 给出修复建议 → 改 YAML → 再跑
```

---

## 目录结构

```
cosmic-replay-v4/
├── lib/                    # 核心代码
│   ├── har_extractor.py    # HAR → YAML（变量识别/步骤裁剪）
│   ├── runner.py           # YAML 执行引擎
│   ├── replay.py           # 苍穹 API 调用封装
│   ├── diagnoser.py        # 错误诊断
│   ├── advisor.py          # 修复建议生成
│   ├── config.py           # 配置管理
│   └── webui/              # FastAPI 后端
├── cases/                  # YAML 测试用例资产
├── har_uploads/            # 上传的 HAR 文件
├── config/                 # 环境配置（不提交 git）
│   └── envs/               # 多环境配置（sit/uat/prod）
├── docs/                   # 使用和开发文档
│   ├── user_guide.md       # Web UI 操作指南
│   ├── troubleshooting.md  # 失败诊断与修复指南
│   └── operating_guide.md  # CLI 操作指南
├── skills/                 # AI Agent skill 包（给 Claude/其他 AI 用）
│   ├── cosmic-replay-troubleshooter/  # 排故 skill
├── README.md
├── QUICKSTART.md
└── start.sh
```

---

## 给 AI Agent 使用

项目中包含 AI Agent skill 包，可直接加载给 Claude Code 或其他 AI 使用：

```bash
# Claude Code 加载 skill
claude --load skills/cosmic-replay-troubleshooter

# 然后直接说：帮我把 xxx.har 导入并运行
```

Skill 包含完整的故障因果链、修复方案和关键代码速查表。参考 `skills/cosmic-replay-troubleshooter/SKILL.md`。

---

## 常见问题

| 问题 | 解决 |
|------|------|
| save 返回空 `[]` | pageId 链路断裂 → 重启 Web UI 后重试 |
| "XXX 已存在" | 编码/名称变量冲突 → 加大 `${rand:N}` 位数 |
| PASS 但数据未入库 | save 断言用 `no_save_failure` 代替 `no_error_actions` |
| 找不到 `cosmic_login.py` | 直接 `python3 -m lib.webui.server` 启动即可（已有自动发现） |

完整排故指南见 `docs/troubleshooting.md` 或 `skills/cosmic-replay-troubleshooter/`。

---

## License

MIT
