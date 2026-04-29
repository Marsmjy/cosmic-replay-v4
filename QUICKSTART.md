# 快速入门指南（5分钟上手）

## 第一步：安装

```bash
# 1. 解压
unzip cosmic-replay-v4-release.zip

# 2. 进入目录
cd cosmic-replay-v4-release

# 3. 检查 Python 版本（需要 3.10+）
python3 --version
# 若低于 3.10 请先升级：https://www.python.org/downloads/

# 4. 创建虚拟环境
python3 -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# 5. 安装依赖（含 pycryptodome 加密库）
pip install -r requirements.txt

# 6. 配置环境变量
cp .env.example .env
# 编辑 .env 填入你的金蝶账号密码
```

## 第二步：启动

```bash
./start.sh
```

看到以下输出表示启动成功：
```
INFO:     Uvicorn running on http://0.0.0.0:8768
```

## 第三步：使用

1. **打开浏览器**：http://127.0.0.1:8768

2. **录制操作**：
   - 打开 Chrome 开发者工具（F12）
   - 切换到 Network 标签
   - 勾选 "Preserve log"
   - 执行你的业务操作（如新增一条数据）
   - 右键 → Save all as HAR

3. **导入 HAR**：
   - 点击「📥 导入 HAR」
   - 上传 HAR 文件
   - 系统自动生成测试用例

4. **运行测试**：
   - 点击用例名称
   - 选择环境
   - 点击「▶ 运行」
   - 查看实时日志

5. **查看报告**：
   - 点击「📊 日志」
   - 切换到「任务记录」
   - 点击「查看报告」

## 常见问题

**Q: Python 版本低于 3.10 会怎样？**
代码使用了 `dict | None` 等 3.10+ 语法，低版本会在运行时报 `TypeError: unsupported operand type(s) for |`。请务必先升级 Python。

**Q: 运行用例报 "RSA 加密库不可用"？**
登录需要 `pycryptodome` 进行密码加密，运行 `pip install pycryptodome` 安装即可。`requirements.txt` 已包含此依赖，正常执行 `pip install -r requirements.txt` 会自动安装。

**Q: 端口被占用怎么办？**
```bash
./start.sh --port 9000
```

**Q: 导入后没有变量？**
检查字段名是否为 number/code/name/fullname 等，否则需手动编辑 YAML。

**Q: 运行失败？**
查看执行日志第一条，确认是否进入正确环境（查看变量值）。

---

完整文档请参考：DEPLOYMENT_GUIDE.md
