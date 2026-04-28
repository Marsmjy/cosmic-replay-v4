# cosmic-replay-v2

> 苍穹表单协议回放自动化测试工具 - HAR录制一键生成可执行YAML用例

[![Python](https://img.shields.io/badge/Python-3.11+-blue.svg)](https://python.org)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active-success.svg)]()

---

## 核心功能

| 功能 | 说明 |
|------|------|
| **HAR → YAML** | 智能转换，自动清理noise步骤、合并字段、抽取变量 |
| **实时回放** | Web UI实时展示步骤执行，SSE推送事件流 |
| **智能诊断** | advisor自动分析失败原因，给出YAML修复补丁 |
| **批量执行** | 支持多用例批量运行，生成执行报告 |
| **跨环境** | 同一套用例切换SIT/UAT/生产环境运行 |

---

## 快速开始

### 安装

```bash
git clone https://github.com/Marsmjy/cosmic-replay-v2.git
cd cosmic-replay-v2
pip install -r requirements.txt
```

### 启动Web UI

```bash
# 首次运行（初始化配置）
python -m lib.webui.server --init

# 后续启动
python -m lib.webui.server
```

访问 http://127.0.0.1:8765

### CLI执行用例

```bash
# 单个用例
python -m lib.runner run cases/admin_org_new.yaml

# 指定环境
python -m lib.runner run cases/admin_org_new.yaml --env sit
```

---

## 使用流程

```
1. 浏览器录制 → 导出HAR文件
       ↓
2. Web UI导入 → 自动生成YAML用例
       ↓
3. 审查编辑 → 调整步骤、变量、断言
       ↓
4. 执行回放 → 实时查看执行结果
       ↓
5. 失败诊断 → advisor给出修复建议
```

---

## 项目结构

```
cosmic-replay-v2/
├── lib/
│   ├── webui/
│   │   ├── server.py          # FastAPI后端
│   │   └── static/index.html  # 前端单页
│   ├── runner.py              # 用例执行引擎
│   ├── replay.py              # HTTP协议回放器
│   ├── har_extractor.py       # HAR解析器
│   ├── advisor.py             # 失败诊断器
│   └── config.py              # 配置管理
├── cases/                      # YAML用例目录
├── logs/                       # 日志目录
├── config/
│   ├── webui.yaml             # Web UI配置
│   └── envs/
│       └── sit.yaml           # 环境配置
└── docs/                       # 文档目录
```

---

## 配置

### 环境配置 (config/envs/sit.yaml)

```yaml
env_id: sit
base_url: https://sit.example.com
cookies:
  - name: kwexc
    value: ${COSMIC_COOKIE}  # 支持环境变量
```

### 敏感信息

使用环境变量存储敏感信息：

```bash
export COSMIC_PASSWORD="your-password"
export COSMIC_COOKIE="your-cookie"
```

---

## API文档

启动后访问：
- Swagger UI: http://127.0.0.1:8765/docs
- ReDoc: http://127.0.0.1:8765/redoc

### 主要端点

| 方法 | 端点 | 说明 |
|------|------|------|
| GET | /api/cases | 列出所有用例 |
| GET | /api/cases/{name} | 获取用例详情 |
| POST | /api/cases/{name}/run | 执行用例 |
| GET | /api/runs/{run_id}/events | SSE事件流 |
| POST | /api/tasks | 创建批量任务 |
| GET | /api/tasks/{id}/report | 获取执行报告 |

---

## Docker部署

```bash
# 构建镜像
docker build -t cosmic-replay:v2 .

# 运行容器
docker run -d \
  -p 8765:8765 \
  -e COSMIC_PASSWORD=${COSMIC_PASSWORD} \
  -v $(pwd)/cases:/app/cases \
  cosmic-replay:v2
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

- [用户指南](docs/user_guide.md)
- [API文档](docs/api/endpoints.md)
- [架构设计](docs/architecture/system_overview.md)
- [部署指南](docs/deploy/installation.md)

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
