# COSMIC REPLAY v4

> 苍穹表单协议回放自动化测试工具 - HAR录制一键生成可执行YAML用例

[![Python](https://img.shields.io/badge/Python-3.10+-blue.svg)](https://python.org)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active-success.svg)]()

---

## 核心功能

| 功能 | 说明 |
|------|------|
| **HAR → YAML** | 智能转换，自动清理noise步骤、合并字段、抽取变量 |
| **智能变量识别** | 自动识别编码/名称字段，避免重复数据冲突 |
| **实时回放** | Web UI实时展示步骤执行，SSE推送事件流 |
| **智能诊断** | advisor自动分析失败原因，给出YAML修复补丁 |
| **批量执行** | 支持多用例批量运行，生成执行报告 |
| **多环境支持** | 同一套用例切换SIT/UAT/生产环境运行 |
| **现代UI** | 宇宙主题设计，毛玻璃效果，渐变配色 |

---

## 快速开始

### 安装

```bash
git clone https://github.com/Marsmjy/cosmic-replay-v4.git
cd cosmic-replay-v4
pip install -r requirements.txt
```

### 配置环境变量

```bash
cp .env.example .env
# 编辑 .env 填入你的金蝶账号密码
```

### 启动Web UI

```bash
./start.sh
# 或指定端口
./start.sh --port 9000
```

访问 http://127.0.0.1:8768

### CLI执行用例

```bash
# 单个用例
python -m lib.runner run cases/新增员工.yaml

# 指定环境
python -m lib.runner run cases/新增员工.yaml --env sit
```

---

## 使用流程

```
1. 浏览器录制 → 导出HAR文件
       ↓
2. Web UI导入 → 自动生成YAML用例
       ↓
3. 智能识别 → 编码/名称自动变量化
       ↓
4. 审查编辑 → 调整步骤、变量、断言
       ↓
5. 执行回放 → 实时查看执行结果
       ↓
6. 失败诊断 → advisor给出修复建议
```

---

## v4 新特性

### 🎨 宇宙主题UI
- 全新Logo SVG设计（轨道环绕概念）
- 深空渐变背景 + 毛玻璃效果
- 现代化配色方案（cyan/violet/purple）

### 🧠 智能变量识别
- 自动识别 `number/code` → `${vars.test_number}`
- 自动识别 `name/fullname` → `${vars.test_name}`
- 支持 `click` 步骤的 `post_data` 字段识别
- 环境相关字段变量化（企业/组织/职位）

### 📊 执行日志增强
- case_start 显示变量模板定义
- session_ready 显示解析后变量值
- 统一执行历史和调试执行日志格式

---

## 项目结构

```
cosmic-replay-v4/
├── lib/
│   ├── webui/
│   │   ├── server.py          # FastAPI后端
│   │   ├── log_store.py       # 日志存储
│   │   └── static/
│   │       ├── index.html     # 前端单页
│   │       ├── logo.svg       # Logo SVG
│   │       └── css/theme-v4.css  # 宇宙主题样式
│   ├── runner.py              # 用例执行引擎
│   ├── replay.py              # HTTP协议回放器
│   ├── har_extractor.py       # HAR解析器（含变量识别）
│   ├── advisor.py             # 失败诊断器
│   └── config.py              # 配置管理
├── cases/                      # YAML用例目录
├── config/
│   ├── webui.yaml             # Web UI配置
│   └── envs/
│       └── sit.yaml           # 环境配置
├── docs/                       # 文档目录
├── SESSION_CONTEXT.md          # 会话上下文（跨会话恢复）
└── start.sh                    # 启动脚本
```

---

## 配置

### 环境配置 (config/envs/sit.yaml)

```yaml
env_id: sit
base_url: https://sit.example.com
credentials:
  username: ${env:COSMIC_USERNAME}
  password: ${env:COSMIC_PASSWORD}
  datacenter_id: ${env:COSMIC_DATACENTER_ID}
```

### 敏感信息

使用环境变量存储敏感信息（.env文件）：

```bash
COSMIC_BASE_URL=https://your-server.com
COSMIC_USERNAME=your_username
COSMIC_PASSWORD=your_password
COSMIC_DATACENTER_ID=your_dc_id
```

---

## API文档

启动后访问：
- Swagger UI: http://127.0.0.1:8768/docs
- ReDoc: http://127.0.0.1:8768/redoc

### 主要端点

| 方法 | 端点 | 说明 |
|------|------|------|
| GET | /api/cases | 列出所有用例 |
| GET | /api/cases/{name} | 获取用例详情 |
| PATCH | /api/cases/{name} | 更新用例 |
| POST | /api/cases/{name}/run | 执行用例 |
| GET | /api/runs/{run_id}/events | SSE事件流 |
| POST | /api/tasks | 创建批量任务 |
| GET | /api/tasks/{id}/report | 获取执行报告 |

---

## Docker部署

```bash
# 构建镜像
docker build -t cosmic-replay:v4 .

# 运行容器
docker run -d \
  -p 8768:8768 \
  -e COSMIC_USERNAME=${COSMIC_USERNAME} \
  -e COSMIC_PASSWORD=${COSMIC_PASSWORD} \
  -e COSMIC_DATACENTER_ID=${COSMIC_DATACENTER_ID} \
  -v $(pwd)/cases:/app/cases \
  cosmic-replay:v4
```

---

## 开发

### 运行测试

```bash
pytest tests/ -v --cov=lib
```

### 代码规范

```bash
ruff check lib/
black lib/ --check
```

---

## 文档

- [快速入门](QUICKSTART.md)
- [部署指南](DEPLOYMENT_GUIDE.md)
- [会话上下文](SESSION_CONTEXT.md)
- [用户指南](docs/user_guide.md)
- [API文档](docs/api/openapi.yaml)

---

## 许可证

[MIT License](LICENSE)

---

## 贡献

欢迎提交 Issue 和 Pull Request。

请阅读 [贡献指南](CONTRIBUTING.md) 了解详情。

---

## 致谢

本项目基于以下开源项目构建：
- [FastAPI](https://fastapi.tiangolo.com/)
- [Alpine.js](https://alpinejs.dev/)
- [Tailwind CSS](https://tailwindcss.com/)

---

## 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| v4.0 | 2026-04-29 | 宇宙主题UI + 智能变量识别 |
| v3.0 | 2026-04-28 | UI重构尝试 |
| v2.0 | 2026-04-27 | 基础功能完善 |
| v1.0 | 2026-04-26 | 初始版本 |
