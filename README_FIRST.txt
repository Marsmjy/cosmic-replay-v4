╔══════════════════════════════════════════════════════════════╗
║              COSMIC REPLAY v4 - HAR 自动化测试工具             ║
╚══════════════════════════════════════════════════════════════╝

📦 感谢使用 COSMIC REPLAY！

🚀 快速开始（3步启动）：

  1️⃣ 安装依赖
     python3 -m venv venv
     source venv/bin/activate  # Windows: venv\Scripts\activate
     pip install -r requirements.txt

  2️⃣ 配置环境变量
     cp .env.example .env
     编辑 .env 填入你的金蝶账号密码

  3️⃣ 启动服务
     ./start.sh
     
  🌐 访问：http://127.0.0.1:8768

📚 文档说明：

  • QUICKSTART.md         - 5分钟快速入门
  • DEPLOYMENT_GUIDE.md   - 完整部署指南
  • example/              - 示例用例（演示变量化用法）
  • docs/                 - 详细文档

💡 核心功能：

  ✅ 导入 HAR 文件自动生成测试用例
  ✅ 智能识别编码/名称变量，避免数据重复
  ✅ 支持多环境切换（SIT/UAT/PROD）
  ✅ 实时查看执行日志和测试报告
  ✅ 批量执行和任务管理

🔧 环境要求：

  • Python 3.10+（必须！低版本会报 TypeError）
  • 2GB 内存
  • 500MB 磁盘

❓ 常见问题：

  Q: 端口被占用？
  A: ./start.sh --port 9000

  Q: 启动失败？
  A: 检查 Python 版本和依赖安装

  Q: 报 TypeError: unsupported operand type(s) for |？
  A: Python 版本低于 3.10，请先升级

  Q: 报 RSA 加密库不可用？
  A: pip install pycryptodome（requirements.txt 已包含）

📧 技术支持：

  GitHub: https://github.com/Marsmjy/cosmic-replay-v4
  Issues: https://github.com/Marsmjy/cosmic-replay-v4/issues

══════════════════════════════════════════════════════════════
                    祝使用愉快！🚀
══════════════════════════════════════════════════════════════
