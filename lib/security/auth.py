"""API认证模块 - 保护Web UI和API端点

支持认证方式：
1. API Key（简单，推荐内部使用）
2. JWT（可选，支持细粒度权限）

使用方法：
    from lib.security.auth import setup_auth, require_auth
    
    # 在 FastAPI app 中设置
    app = FastAPI()
    setup_auth(app)
    
    # 端点保护
    @app.get("/api/protected")
    @require_auth
    async def protected_endpoint():
        return {"data": "sensitive"}
"""
import os
import secrets
import time
from typing import Optional, Callable
from functools import wraps

# FastAPI 相关导入（延迟导入避免未安装时报错）
try:
    from fastapi import HTTPException, Depends, status
    from fastapi.security import APIKeyHeader, HTTPBearer, HTTPAuthorizationCredentials
    HAS_FASTAPI = True
except ImportError:
    HAS_FASTAPI = False


# ============================================================
# API Key 认证
# ============================================================

# API Key 来源优先级：
# 1. 环境变量 COSMIC_API_KEY
# 2. 文件 ~/.cosmic/api_key
# 3. 无认证（开发模式）

_api_keys: set[str] = set()


def _load_api_keys() -> set[str]:
    """加载 API Keys"""
    global _api_keys
    
    if _api_keys:
        return _api_keys
    
    keys = set()
    
    # 从环境变量获取（支持逗号分隔多个key）
    env_key = os.environ.get("COSMIC_API_KEY")
    if env_key:
        # 支持格式: "key1,key2,key3"
        for k in env_key.split(","):
            k = k.strip()
            if k:
                keys.add(k)
    
    # 从文件获取
    key_file = os.path.expanduser("~/.cosmic/api_keys")
    if os.path.exists(key_file):
        with open(key_file) as f:
            for line in f:
                k = line.strip()
                if k and not k.startswith("#"):
                    keys.add(k)
    
    _api_keys = keys
    return _api_keys


def generate_api_key() -> str:
    """生成新的 API Key"""
    return f"cr_{secrets.token_urlsafe(32)}"


def validate_api_key(key: str) -> bool:
    """验证 API Key"""
    if not key:
        return False
    
    valid_keys = _load_api_keys()
    
    # 无有效key时，允许所有请求（开发模式）
    if not valid_keys:
        return True
    
    return key in valid_keys


if HAS_FASTAPI:
    # FastAPI 依赖注入
    api_key_header = APIKeyHeader(name="X-API-Key", auto_error=False)
    
    async def get_api_key(api_key: str = Depends(api_key_header)) -> Optional[str]:
        """获取并验证 API Key"""
        if not api_key:
            return None
        
        if not validate_api_key(api_key):
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid API Key"
            )
        
        return api_key
    
    # Bearer Token 认证（JWT）
    bearer_scheme = HTTPBearer(auto_error=False)
    
    async def get_bearer_token(
        credentials: HTTPAuthorizationCredentials = Depends(bearer_scheme)
    ) -> Optional[str]:
        """获取 Bearer Token"""
        if not credentials:
            return None
        return credentials.credentials


# ============================================================
# JWT 认证（可选）
# ============================================================

JWT_SECRET = os.environ.get("COSMIC_JWT_SECRET", "change-me-in-production")
JWT_ALGORITHM = "HS256"
JWT_EXPIRE_SECONDS = 3600 * 24  # 24小时


def create_jwt_token(user_id: str, exp: Optional[int] = None) -> str:
    """创建 JWT Token"""
    try:
        import jwt
    except ImportError:
        raise RuntimeError("需要安装 PyJWT: pip install PyJWT")
    
    payload = {
        "sub": user_id,
        "iat": int(time.time()),
        "exp": exp or int(time.time()) + JWT_EXPIRE_SECONDS
    }
    
    return jwt.encode(payload, JWT_SECRET, algorithm=JWT_ALGORITHM)


def verify_jwt_token(token: str) -> Optional[dict]:
    """验证 JWT Token"""
    try:
        import jwt
    except ImportError:
        return None
    
    try:
        payload = jwt.decode(token, JWT_SECRET, algorithms=[JWT_ALGORITHM])
        return payload
    except jwt.ExpiredSignatureError:
        return None
    except jwt.InvalidTokenError:
        return None


# ============================================================
# FastAPI 中间件设置
# ============================================================

def setup_auth(app, require_auth: bool = False, exclude_paths: list[str] = None):
    """设置认证中间件
    
    Args:
        app: FastAPI 应用实例
        require_auth: 是否强制要求认证（False=可选认证）
        exclude_paths: 排除路径（不需要认证）
    """
    if not HAS_FASTAPI:
        return
    
    exclude_paths = exclude_paths or [
        "/", "/health", "/api/health", "/docs", "/redoc", "/openapi.json",
        "/static", "/favicon.ico"
    ]
    
    @app.middleware("http")
    async def auth_middleware(request, call_next):
        """认证中间件"""
        # 检查是否在排除路径中
        path = request.url.path
        if any(path.startswith(p) for p in exclude_paths):
            return await call_next(request)
        
        # 检查 API Key
        api_key = request.headers.get("X-API-Key")
        
        # 检查 Bearer Token
        auth_header = request.headers.get("Authorization", "")
        bearer_token = None
        if auth_header.startswith("Bearer "):
            bearer_token = auth_header[7:]
        
        # 验证
        is_authenticated = False
        
        if api_key and validate_api_key(api_key):
            is_authenticated = True
        elif bearer_token and verify_jwt_token(bearer_token):
            is_authenticated = True
        elif not _load_api_keys():
            # 无有效API Key时，允许所有请求（开发模式）
            is_authenticated = True
        
        if require_auth and not is_authenticated:
            from fastapi.responses import JSONResponse
            return JSONResponse(
                status_code=status.HTTP_401_UNAUTHORIZED,
                content={"detail": "Unauthorized"}
            )
        
        response = await call_next(request)
        return response


# ============================================================
# 装饰器版本（用于函数保护）
# ============================================================

def require_auth(func: Callable) -> Callable:
    """认证装饰器（用于非 FastAPI 场景）"""
    @wraps(func)
    def wrapper(*args, **kwargs):
        # 从 kwargs 中提取 api_key
        api_key = kwargs.get("api_key") or kwargs.get("_api_key")
        
        if not api_key:
            raise PermissionError("API Key required")
        
        if not validate_api_key(api_key):
            raise PermissionError("Invalid API Key")
        
        return func(*args, **kwargs)
    
    return wrapper


# ============================================================
# 命令行工具
# ============================================================

def main():
    """命令行工具：生成 API Key"""
    import argparse
    
    parser = argparse.ArgumentParser(description="API Key 管理")
    parser.add_argument("action", choices=["generate", "validate", "list"])
    parser.add_argument("--key", help="要验证的 API Key")
    
    args = parser.parse_args()
    
    if args.action == "generate":
        key = generate_api_key()
        print(f"生成的 API Key: {key}")
        print(f"\n保存到文件:")
        print(f"  echo '{key}' >> ~/.cosmic/api_keys")
        print(f"\n或设置为环境变量:")
        print(f"  export COSMIC_API_KEY='{key}'")
        
    elif args.action == "validate":
        if not args.key:
            print("错误：需要 --key 参数")
            return
        
        if validate_api_key(args.key):
            print("API Key 有效")
        else:
            print("API Key 无效")
            
    elif args.action == "list":
        keys = _load_api_keys()
        if keys:
            print(f"有效的 API Keys ({len(keys)} 个):")
            for k in keys:
                # 只显示前8位
                print(f"  {k[:8]}...{k[-4:]}")
        else:
            print("无有效 API Key（开发模式，允许所有请求）")


if __name__ == "__main__":
    main()
