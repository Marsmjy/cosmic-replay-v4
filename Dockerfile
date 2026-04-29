# cosmic-replay v4 - Docker Image
# 用于生产环境部署

FROM python:3.11-slim

LABEL maintainer="Mars"
LABEL version="4.0.0"
LABEL description="cosmic-replay v4 HAR回放自动化测试工具"

# 设置工作目录
WORKDIR /app

# 安装系统依赖
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 复制依赖文件
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 复制应用代码
COPY . .

# 创建必要的目录
RUN mkdir -p /app/cases /app/logs /app/config/envs /app/config/hars

# 设置环境变量
ENV PYTHONUNBUFFERED=1
ENV COSMIC_LOGIN_SCRIPT=/app/cosmic_login.py

# 暴露端口
EXPOSE 8768

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8768/api/health || exit 1

# 启动命令
CMD ["python", "-m", "lib.webui.server", "--port", "8768", "--host", "0.0.0.0", "--no-browser"]
