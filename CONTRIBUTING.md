# Contributing to cosmic-replay-v2

感谢你考虑为 cosmic-replay-v2 做贡献！

---

## 如何贡献

### 报告Bug

1. 在 [Issues](https://github.com/Marsmjy/cosmic-replay-v2/issues) 页面搜索是否已有相同问题
2. 如果没有，创建新Issue，包含：
   - 清晰的标题
   - 复现步骤
   - 期望行为 vs 实际行为
   - 环境信息（Python版本、操作系统）
   - 相关日志或截图

### 提交功能请求

1. 创建Issue，标签为 `enhancement`
2. 描述功能用途和使用场景
3. 说明为什么这个功能对项目有价值

### 提交代码

1. Fork 本仓库
2. 创建功能分支：`git checkout -b feature/your-feature`
3. 编写代码并添加测试
4. 运行测试：`pytest tests/ -v`
5. 运行代码检查：`ruff check lib/`
6. 提交：`git commit -m "Add: your feature"`
7. 推送：`git push origin feature/your-feature`
8. 创建 Pull Request

---

## 开发环境设置

```bash
# 克隆仓库
git clone https://github.com/Marsmjy/cosmic-replay-v2.git
cd cosmic-replay-v2

# 安装依赖
pip install -r requirements.txt

# 安装开发依赖
pip install pytest pytest-cov ruff black

# 运行测试
pytest tests/ -v --cov=lib

# 启动开发服务器
python -m lib.webui.server --port 8766
```

---

## 代码规范

### Python

- 使用 Python 3.11+ 语法
- 函数必须有 docstring
- 类型注释推荐但不强制
- 最大行长度 100 字符

```python
def run_case(case_path: Path, env_override: dict | None = None) -> RunResult:
    """执行单个YAML用例
    
    Args:
        case_path: 用例YAML文件路径
        env_override: 环境配置覆盖
    
    Returns:
        RunResult包含执行结果详情
    
    Raises:
        CosmicError: 苍穹协议错误
    """
    ...
```

### 前端

- 使用 Alpine.js 进行状态管理
- 使用 Tailwind CSS 进行样式
- 组件化建议：将重复代码提取为函数

---

## 项目结构

```
lib/
├── webui/           # Web UI 相关
│   ├── server.py    # FastAPI 后端
│   └── static/      # 前端静态文件
├── runner.py        # 用例执行引擎
├── replay.py        # HTTP 协议回放
├── har_extractor.py # HAR 解析
├── advisor.py       # 失败诊断
└── config.py        # 配置管理
```

---

## Pull Request 检查清单

- [ ] 代码通过 `ruff check`
- [ ] 测试通过 `pytest tests/`
- [ ] 新功能有对应测试
- [ ] Docstring 已添加
- [ ] CHANGELOG.md 已更新（如适用）
- [ ] 没有引入新的安全风险

---

## 许可证

提交的代码将采用 MIT 许可证。

---

## 联系方式

- Issues: https://github.com/Marsmjy/cosmic-replay-v2/issues
- Email: 维护者邮箱（待补充）