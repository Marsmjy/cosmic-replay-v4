#!/bin/bash
# cosmic-replay-v2 启动脚本

# 设置项目根目录
PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"

# 设置环境变量
export COSMIC_LOGIN_SCRIPT="$PROJECT_ROOT/lib/cosmic_login.py"

# 进入项目目录
cd "$PROJECT_ROOT"

# 启动服务
exec python3 -m lib.webui.server --port 8768 "$@"
