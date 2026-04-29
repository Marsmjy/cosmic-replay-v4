# COSMIC REPLAY v4 部署指南

## 一、项目简介

COSMIC REPLAY 是一个基于 HAR 文件的自动化测试回放工具，专为金蝶苍穹平台设计。

**核心功能**：
- 📥 导入浏览器录制的 HAR 文件，自动生成测试用例
- 🔄 自动识别变量（编码、名称等），避免重复数据冲突
- 🧪 支持单个用例调试和批量执行
- 📊 实时查看执行日志和测试报告
- 🔐 支持多环境切换（SIT/UAT/PROD）

**技术栈**：
- 后端：Python 3.10+ / FastAPI / SQLite
- 前端：原生 JavaScript / Alpine.js / Tailwind CSS
- 存储：SQLite（用例、日志、任务记录）

---

## 二、系统要求

### 最低配置
- 操作系统：macOS 10.15+ / Ubuntu 18.04+ / Windows 10+
- Python：3.10 或更高版本
- 内存：2GB+
- 磁盘：500MB+

### 推荐配置
- Python：3.11+
- 内存：4GB+
- 磁盘：1GB+

---

## 三、安装步骤

### 3.1 准备工作

```bash
# 1. 检查 Python 版本（必须 3.10+）
python3 --version
# 应输出 Python 3.10.x 或更高
# ⚠️ 若低于 3.10，代码中的 dict | None 等语法会导致 TypeError，请先升级

# 2. 创建项目目录
mkdir -p ~/cosmic-replay
cd ~/cosmic-replay
```

### 3.2 解压安装包

```bash
# macOS / Linux
unzip cosmic-replay-v4-release.zip -d ~/cosmic-replay

# Windows（使用解压工具解压到任意目录）
```

### 3.3 创建虚拟环境

```bash
cd ~/cosmic-replay

# macOS / Linux
python3 -m venv venv
source venv/bin/activate

# Windows
python -m venv venv
venv\Scripts\activate
```

### 3.4 安装依赖

```bash
pip install -r requirements.txt
```

**预期输出**：
```
Successfully installed fastapi uvicorn pyyaml httpx python-multipart pycryptodome ...
```

> ⚠️ `pycryptodome` 是登录时 RSA 密码加密的必需库，若缺失会报 "RSA 加密库不可用"。

### 3.5 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑配置文件
nano .env  # 或使用你喜欢的编辑器
```

**必须配置的环境变量**：
```bash
# 金蝶苍穹环境地址
COSMIC_BASE_URL=https://your-cosmic-server.com

# 登录凭证
COSMIC_USERNAME=your_username
COSMIC_PASSWORD=your_password
COSMIC_DATACENTER_ID=your_datacenter_id
```

### 3.6 初始化数据库

```bash
# 数据库会自动创建，首次启动时自动初始化
# 如需手动初始化：
python3 -c "from lib.storage import init_db; init_db()"
```

---

## 四、启动服务

### 4.1 启动方式一：脚本启动（推荐）

```bash
# 启动服务（默认端口 8768）
./start.sh

# 指定端口启动
./start.sh --port 9000
```

### 4.2 启动方式二：直接启动

```bash
python3 -m lib.webui.server --port 8768
```

### 4.3 启动方式三：后台运行

```bash
# macOS / Linux
nohup ./start.sh > cosmic-replay.log 2>&1 &

# 查看日志
tail -f cosmic-replay.log
```

### 4.4 验证启动成功

```bash
# 检查服务健康状态
curl http://127.0.0.1:8768/api/health
```

**预期输出**：
```json
{
  "status": "healthy",
  "timestamp": "2026-04-29T10:00:00",
  "cases_count": 0,
  "envs_count": 1,
  "uptime_seconds": 5
}
```

### 4.5 访问 Web 界面

打开浏览器访问：http://127.0.0.1:8768

---

## 五、使用指南

### 5.1 录制 HAR 文件

1. **打开浏览器开发者工具**：
   - Chrome/Edge: 按 F12 或右键 → 检查
   - 切换到 Network 标签页
   - 勾选 "Preserve log"（保留日志）

2. **执行业务操作**：
   - 登录系统
   - 执行需要测试的业务流程（如新增、修改、删除）

3. **导出 HAR 文件**：
   - 在 Network 面板右键 → Save all as HAR with content
   - 保存为 `your-test.har`

### 5.2 导入 HAR 生成用例

1. 访问 Web 界面：http://127.0.0.1:8768
2. 点击顶部导航「📥 导入 HAR」
3. 选择或拖拽 HAR 文件上传
4. 系统自动分析并生成测试用例

**智能识别功能**：
- ✅ 自动识别编码字段（number/code）→ 变量化为 `${vars.test_number}`
- ✅ 自动识别名称字段（name/fullname）→ 变量化为 `${vars.test_name}`
- ✅ 自动裁剪首页加载等无关步骤
- ✅ 自动识别断言点

### 5.3 运行测试用例

**单个用例调试**：
1. 在用例列表点击用例名称进入详情
2. 选择执行环境（SIT/UAT/PROD）
3. 点击「▶ 运行」按钮
4. 实时查看执行日志

**批量执行**：
1. 点击顶部导航「⚡ 批量运行」
2. 勾选要执行的用例
3. 选择执行环境
4. 点击「启动任务」
5. 查看任务进度和报告

### 5.4 查看测试报告

1. 点击顶部导航「📊 日志」
2. 切换到「任务记录」Tab
3. 点击「查看报告」查看详细结果
4. 报告包含：
   - 每个步骤的执行状态
   - 变量解析结果
   - 错误信息和堆栈

### 5.5 编辑用例

1. 在用例详情页点击「✎ 编辑」按钮
2. 修改变量定义、步骤参数
3. 点击「保存」

**变量定义示例**：
```yaml
vars:
  test_number: "TEST${rand:6}"        # 随机编码
  test_name: "测试员${rand:4}"         # 随机名称
  test_phone: "+86-138${rand:8}"      # 随机电话
```

---

## 六、配置多环境

### 6.1 编辑环境配置

编辑 `config/environments.yaml`：

```yaml
environments:
  - name: SIT 环境
    id: sit
    base_url: https://sit.example.com
    username: ${env:SIT_USERNAME}
    password: ${env:SIT_PASSWORD}
    datacenter_id: ${env:SIT_DATACENTER_ID}
    
  - name: UAT 环境
    id: uat
    base_url: https://uat.example.com
    username: ${env:UAT_USERNAME}
    password: ${env:UAT_PASSWORD}
    datacenter_id: ${env:UAT_DATACENTER_ID}
```

### 6.2 在 .env 中配置凭证

```bash
# SIT 环境
SIT_USERNAME=user1
SIT_PASSWORD=pass1
SIT_DATACENTER_ID=dc001

# UAT 环境
UAT_USERNAME=user2
UAT_PASSWORD=pass2
UAT_DATACENTER_ID=dc002
```

---

## 七、常见问题

### Q1: 启动失败，提示端口被占用

```bash
# 查看端口占用
lsof -i :8768  # macOS/Linux
netstat -ano | findstr :8768  # Windows

# 解决方案：更换端口
./start.sh --port 9000
```

### Q2: 导入 HAR 后没有识别出变量

**原因**：字段名不符合识别规则

**解决方案**：
1. 手动编辑用例 YAML 文件
2. 在 `vars` 中添加变量定义
3. 在步骤中引用 `${vars.xxx}`

### Q3: 运行时提示登录失败

**原因**：环境变量配置错误或凭证过期

**排查步骤**：
1. 检查 .env 文件中的 COSMIC_USERNAME 和 COSMIC_PASSWORD
2. 检查 COSMIC_BASE_URL 是否正确
3. 查看执行日志中的详细错误信息

### Q6: 启动报 TypeError: unsupported operand type(s) for |: 'type' and 'NoneType'

**原因**：Python 版本低于 3.10，不支持 `dict | None` 联合类型语法

**解决方案**：
```bash
# 检查版本
python3 --version
# 若低于 3.10，请升级到 3.10+
# https://www.python.org/downloads/
```

### Q7: 运行用例报 "RSA 加密库不可用"

**原因**：缺少 `pycryptodome` 库，登录时无法进行 RSA 密码加密

**解决方案**：
```bash
pip install pycryptodome
# 或重新安装全部依赖
pip install -r requirements.txt
```

### Q4: 批量执行时部分用例失败

**常见原因**：
- 数据重复（编码/名称已存在）
- 环境差异（基础资料不存在）
- 依赖顺序问题

**解决方案**：
1. 查看失败用例的详细日志
2. 检查变量是否正确随机化
3. 调整执行顺序或添加前置条件

### Q5: 如何查看 API 文档

访问：http://127.0.0.1:8768/docs

提供完整的 REST API 文档（Swagger UI）

---

## 八、目录结构

```
cosmic-replay-v4/
├── cases/              # 测试用例 YAML 文件
├── config/             # 环境配置
│   └── environments.yaml
├── docs/               # 文档
├── har_uploads/        # 上传的 HAR 文件
├── lib/                # 核心代码
│   ├── webui/          # Web 服务
│   ├── runner.py       # 执行引擎
│   ├── har_extractor.py # HAR 解析
│   └── storage.py      # 数据存储
├── tests/              # 测试代码
├── .env                # 环境变量（需配置）
├── requirements.txt    # Python 依赖
├── start.sh            # 启动脚本
└── cosmic_replay.db    # SQLite 数据库
```

---

## 九、技术支持

### 项目地址
https://github.com/Marsmjy/cosmic-replay-v4

### 文档
- 用户手册：docs/USER_GUIDE.md
- API 文档：http://127.0.0.1:8768/docs
- 架构设计：docs/ARCHITECTURE.md

### 问题反馈
在 GitHub Issues 提交问题：
https://github.com/Marsmjy/cosmic-replay-v4/issues

---

## 十、版本历史

### v4.0 (2026-04-29)
- ✨ 宇宙主题 UI，现代化界面设计
- ✨ 自动变量识别增强（支持 click 步骤）
- ✨ 执行日志显示变量信息
- ✨ 任务记录和报告查看
- 🐛 修复光标样式问题
- 🐛 修复用例名称编辑功能
- 📚 完善文档和部署指南

---

**祝使用愉快！** 🚀
