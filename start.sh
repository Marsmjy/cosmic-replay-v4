#!/bin/bash
# cosmic-replay-v4 启动脚本

# 设置项目根目录
PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"

# ---- Python 版本检查（最低 3.10）----
PY_CMD="${PYTHON:-python3}"
if ! command -v "$PY_CMD" &>/dev/null; then
    echo "❌ 未找到 Python，请安装 Python 3.10+ 后重试"
    exit 1
fi

PY_VER=$("$PY_CMD" -c 'import sys; print(f"{sys.version_info.major}.{sys.version_info.minor}")' 2>/dev/null)
PY_MAJOR=$("$PY_CMD" -c 'import sys; print(sys.version_info.major)' 2>/dev/null)
PY_MINOR=$("$PY_CMD" -c 'import sys; print(sys.version_info.minor)' 2>/dev/null)

if [ -z "$PY_MAJOR" ] || [ "$PY_MAJOR" -lt 3 ] || { [ "$PY_MAJOR" -eq 3 ] && [ "$PY_MINOR" -lt 10 ]; }; then
    echo "❌ Python 版本过低：当前 $PY_VER，需要 3.10+"
    echo "   请升级 Python 后重试：https://www.python.org/downloads/"
    exit 1
fi
echo "✅ Python $PY_VER"

# 设置环境变量
export COSMIC_LOGIN_SCRIPT="$PROJECT_ROOT/lib/cosmic_login.py"

# 进入项目目录
cd "$PROJECT_ROOT"

# 启动服务
exec "$PY_CMD" -m lib.webui.server --port 8768 "$@"
