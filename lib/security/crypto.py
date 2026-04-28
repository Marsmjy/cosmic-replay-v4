"""加密工具模块 - 敏感信息加密存储

功能：
1. 密码/Cookie加密存储
2. 环境变量安全读取
3. 配置文件敏感字段自动加密

使用方法：
    from lib.security.crypto import encrypt_value, decrypt_value
    
    # 加密
    encrypted = encrypt_value("my-password")
    
    # 解密
    decrypted = decrypt_value(encrypted)
    
    # 环境变量安全读取
    password = get_secure_env("COSMIC_PASSWORD")
"""
import os
import base64
import hashlib
from pathlib import Path
from typing import Optional


# 加密密钥来源优先级：
# 1. 环境变量 COSMIC_SECRET_KEY
# 2. 文件 ~/.cosmic/secret_key
# 3. 自动生成（每次重启会变，仅用于临时场景）
_SECRET_KEY: Optional[bytes] = None


def _get_secret_key() -> bytes:
    """获取加密密钥"""
    global _SECRET_KEY
    
    if _SECRET_KEY:
        return _SECRET_KEY
    
    # 优先从环境变量获取
    key_env = os.environ.get("COSMIC_SECRET_KEY")
    if key_env:
        _SECRET_KEY = key_env.encode()[:32].ljust(32, b'\0')
        return _SECRET_KEY
    
    # 从文件获取
    key_file = Path.home() / ".cosmic" / "secret_key"
    if key_file.exists():
        _SECRET_KEY = key_file.read_bytes()[:32].ljust(32, b'\0')
        return _SECRET_KEY
    
    # 自动生成（不推荐生产使用）
    import secrets
    _SECRET_KEY = secrets.token_bytes(32)
    
    # 保存到文件以便下次使用
    key_file.parent.mkdir(parents=True, exist_ok=True)
    key_file.write_bytes(_SECRET_KEY)
    key_file.chmod(0o600)  # 仅用户可读
    
    return _SECRET_KEY


def encrypt_value(plain: str) -> str:
    """加密字符串
    
    使用简单的 XOR + Base64 加密（非高强度加密，仅用于配置文件脱敏）
    生产环境建议使用专业的加密库（如 cryptography）
    
    Args:
        plain: 明文字符串
        
    Returns:
        加密后的字符串（以 ENC: 开头）
    """
    if not plain:
        return ""
    
    key = _get_secret_key()
    plain_bytes = plain.encode('utf-8')
    
    # XOR 加密
    encrypted = bytes([b ^ key[i % len(key)] for i, b in enumerate(plain_bytes)])
    
    # Base64 编码
    encoded = base64.b64encode(encrypted).decode('ascii')
    
    return f"ENC:{encoded}"


def decrypt_value(encrypted: str) -> str:
    """解密字符串
    
    Args:
        encrypted: 加密字符串（以 ENC: 开头）
        
    Returns:
        解密后的明文
    """
    if not encrypted:
        return ""
    
    if not encrypted.startswith("ENC:"):
        # 不是加密格式，直接返回
        return encrypted
    
    key = _get_secret_key()
    encoded = encrypted[4:]  # 去掉 ENC: 前缀
    
    try:
        # Base64 解码
        encrypted_bytes = base64.b64decode(encoded)
        
        # XOR 解密
        decrypted = bytes([b ^ key[i % len(key)] for i, b in enumerate(encrypted_bytes)])
        
        return decrypted.decode('utf-8')
    except Exception:
        # 解密失败，返回原值
        return encrypted


def get_secure_env(name: str, default: str = "") -> str:
    """安全读取环境变量
    
    Args:
        name: 环境变量名
        default: 默认值
        
    Returns:
        环境变量值（如果是加密格式会自动解密）
    """
    value = os.environ.get(name, default)
    return decrypt_value(value) if value.startswith("ENC:") else value


def hash_password(password: str) -> str:
    """密码哈希（用于存储）
    
    使用 SHA256 + salt
    """
    salt = os.environ.get("COSMIC_PASSWORD_SALT", "cosmic-replay-default-salt")
    combined = f"{salt}:{password}"
    return hashlib.sha256(combined.encode()).hexdigest()


def verify_password(password: str, hashed: str) -> bool:
    """验证密码"""
    return hash_password(password) == hashed


def mask_sensitive(text: str, visible_ratio: float = 0.2) -> str:
    """脱敏显示
    
    Args:
        text: 原文本
        visible_ratio: 可见比例（0-1）
        
    Returns:
        脱敏后的文本（如：abcd****efgh）
    """
    if not text or len(text) < 4:
        return "***"
    
    visible_len = max(2, int(len(text) * visible_ratio))
    hidden_len = len(text) - visible_len
    
    return f"{text[:visible_len//2]}****{text[-visible_len//2:]}"


# 配置文件敏感字段自动加密/解密
SENSITIVE_FIELDS = [
    "password", "password_env", "cookie", "cookies", 
    "token", "api_key", "secret", "credential"
]


def encrypt_config_value(key: str, value: str) -> str:
    """根据字段名判断是否需要加密"""
    if not value:
        return value
    
    # 判断是否是敏感字段
    is_sensitive = any(s in key.lower() for s in SENSITIVE_FIELDS)
    
    if is_sensitive and not value.startswith("ENC:") and not value.startswith("${"):
        return encrypt_value(value)
    
    return value


def decrypt_config_value(key: str, value: str) -> str:
    """解密配置值"""
    if value.startswith("ENC:"):
        return decrypt_value(value)
    return value