#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
cosmic_login.py — 苍穹平台自动登录（完全自包含，零项目依赖）

可独立分发，仅需 pip install pycryptodome requests

用法:
  python cosmic_login.py <base_url>                              # 列出数据中心
  python cosmic_login.py <base_url> <user> <password>            # 自动登录（单数据中心）
  python cosmic_login.py <base_url> <user> <password> <dc_id>    # 指定数据中心登录
  python cosmic_login.py --check <base_url> <cookie>             # 检查 Cookie 有效性

输出格式（供上层脚本解析）:
  LOGIN_SUCCESS
  COOKIE=<cookie_string>
  CSRF_TOKEN=***
  ACCOUNT_ID=<dc_id>
"""

import json
import sys
import random
import string
import re
import base64
import os

try:
    import requests
except ImportError:
    print("ERROR: requests 库未安装。请执行: pip install requests")
    sys.exit(1)

# RSA 加密依赖（优先 pycryptodome，回退 rsa 库）
_RSA_MODE = None
try:
    from Crypto.PublicKey import RSA as _CryptoRSA
    from Crypto.Cipher import PKCS1_v1_5 as _PKCS1
    _RSA_MODE = "pycryptodome"
except ImportError:
    try:
        import rsa as _rsa_lib
        _RSA_MODE = "rsa"
    except ImportError:
        pass


# ═══════════════════════════════════════════════════════════════
# 核心函数
# ═══════════════════════════════════════════════════════════════

def _random_suffix(length: int = 5) -> str:
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))


def _extract_pub_body(raw_or_pem: str) -> str:
    """从 PEM 或脏字符串里抽 base64 体（纯字符，无换行）。"""
    s = raw_or_pem.replace("\\n", "\n").replace("\\r", "").replace("\r", "")
    # 去 PEM 头尾（贪婪到 -----）
    s = re.sub(r"-----BEGIN[^-]+-----", "", s)
    s = re.sub(r"-----END[^-]+-----", "", s)
    s = re.sub(r"\s+", "", s)
    s = re.sub(r"[^A-Za-z0-9+/=]", "", s)
    return s


def _encrypt_password(password: str, public_key_pem: str) -> str:
    """RSA PKCS1v1.5 加密密码 → Base64。
    多重兜底：
      1. 优先直接用 PEM 让 pycryptodome import
      2. 失败则抽纯 base64 体，解码 DER 重新构造
      3. 再失败，回退 rsa 库
    """
    last_err: Exception | None = None

    if _RSA_MODE == "pycryptodome":
        # 尝试 1: 直接 PEM
        try:
            key = _CryptoRSA.import_key(public_key_pem)
            cipher = _PKCS1.new(key)
            return base64.b64encode(cipher.encrypt(password.encode("utf-8"))).decode("utf-8")
        except Exception as e:
            last_err = e

        # 尝试 2: 从 PEM/脏数据抽出 base64 体 → 补 pad → DER 导入
        try:
            body = _extract_pub_body(public_key_pem)
            if body:
                # base64 体长度必须是 4 的倍数，不够补 =
                missing = (-len(body)) % 4
                if missing:
                    body = body + ("=" * missing)
                der = base64.b64decode(body)
                key = _CryptoRSA.import_key(der)
                cipher = _PKCS1.new(key)
                return base64.b64encode(cipher.encrypt(password.encode("utf-8"))).decode("utf-8")
        except Exception as e:
            last_err = e

    if _RSA_MODE == "rsa":
        try:
            pub_key = _rsa_lib.PublicKey.load_pkcs1_openssl_pem(public_key_pem.encode("utf-8"))
            encrypted = _rsa_lib.encrypt(password.encode("utf-8"), pub_key)
            return base64.b64encode(encrypted).decode("utf-8")
        except Exception as e:
            last_err = e

    if _RSA_MODE is None:
        raise RuntimeError(
            "RSA 加密库不可用。请安装: pip install pycryptodome 或 pip install rsa"
        )
    raise RuntimeError(
        f"RSA 加密失败（多种方式都试过）: {last_err}. "
        f"公钥前 80 字符: {public_key_pem[:80]!r}"
    )


def _normalize_pem(raw: str) -> str:
    """确保公钥是标准 PEM 格式。
    无论输入是：
    - 纯 base64 体
    - 有头尾但没换行
    - 头尾+换行但混了 \r
    - JSON 里带转义符 \\n

    都能规范化成 import_key 接受的标准格式。
    """
    if not raw:
        return ""
    raw = raw.strip()

    # 把 JSON 转义过的 \n / \r 还原成真换行
    raw = raw.replace("\\n", "\n").replace("\\r", "").replace("\r", "")

    # 剥头尾 + 所有空白，只留 base64 体
    body = raw
    body = re.sub(r"-----BEGIN[^-]+-----", "", body)
    body = re.sub(r"-----END[^-]+-----", "", body)
    body = re.sub(r"\s+", "", body)

    if not body or len(body) < 50:
        return ""
    # 只保留合法 base64 字符（防止返回里混了奇怪字符）
    body = re.sub(r"[^A-Za-z0-9+/=]", "", body)
    if not body:
        return ""

    # 按 64 字符一行重排
    lines = [body[i:i+64] for i in range(0, len(body), 64)]
    return "-----BEGIN PUBLIC KEY-----\n" + "\n".join(lines) + "\n-----END PUBLIC KEY-----"


def list_datacenters(base_url: str, timeout: int = 15, proxies: dict | None = None) -> list:
    """获取苍穹数据中心列表
    
    注意：如果 base_url 是内网地址，应该传入 proxies={"http": None, "https": None} 绕过代理
    """
    base_url = base_url.rstrip("/")
    # 自动检测内网地址，内网不走代理
    import re as _re
    is_internal = _re.match(r'^(https?://)?(10\.|172\.(1[6-9]|2\d|3[01])\.|192\.168\.|localhost|127\.)', base_url)
    if is_internal and proxies is None:
        proxies = {"http": None, "https": None}
    resp = requests.get(f"{base_url}/auth/getAllDatacenters.do", timeout=timeout,
                        headers={"User-Agent": "CosmicLoginSkill/1.0"},
                        proxies=proxies,
                        verify=False)  # 内网证书可能自签名
    resp.raise_for_status()
    data = resp.json()
    if isinstance(data, list):
        return data
    if isinstance(data, dict):
        return data.get("data", data.get("datacenters", [data]))
    return []


def login(base_url: str, username: str, password: str,
          account_id: str, language: str = "zh_CN",
          timeout: int = 15, proxies: dict | None = None) -> dict:
    """
    完整登录流程: getPublicKey → RSA加密 → yzjlogin

    返回:
        {
            "success": bool,
            "cookie": str,
            "csrf_token": str,
            "error": str,
            "account_id": str,
            "user_id": str,
        }
    """
    base_url = base_url.rstrip("/")
    
    # 自动检测内网地址，内网不走代理
    import re as _re_internal
    is_internal = _re_internal.match(r'^(https?://)?(10\.|172\.(1[6-9]|2\d|3[01])\.|192\.168\.|localhost|127\.)', base_url)
    if is_internal and proxies is None:
        proxies = {"http": None, "https": None}
    
    session = requests.Session()
    session.headers.update({
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
        "Accept": "application/json, text/plain, */*",
    })
    session.verify = False  # 内网证书可能自签名

    result = {
        "success": False, "cookie": "", "csrf_token": "",
        "error": "", "account_id": account_id, "user_id": ""
    }

    # ── 参数预校验（防止占位符 / 明显非法值被当真 ──
    if not account_id or not str(account_id).strip():
        result["error"] = "datacenter_id（数据中心 id）为空，请先配置"
        return result
    _aid = str(account_id).strip()
    if _aid.upper().startswith("YOUR_") or _aid == "YOUR_DATACENTER_ID":
        result["error"] = (f"datacenter_id 看起来是占位符 '{_aid}'，"
                           f"请先在配置页或 config/envs/*.yaml 填写真实的数据中心 id")
        return result
    if not username or str(username).strip().upper().startswith("YOUR_"):
        result["error"] = f"username 看起来是占位符 '{username}'，请先填真实账号"
        return result
    if not password or str(password).strip().upper() == "YOUR_PASSWORD":
        result["error"] = f"password 看起来是占位符，请先填真实密码"
        return result

    # ── Step 1: 获取 RSA 公钥 ──
    access_key = username + _random_suffix(5)
    try:
        pk_resp = session.post(
            f"{base_url}/auth/getPublicKey.do",
            data={"accessKey": access_key, "language": language, "accountId": account_id},
            timeout=timeout,
            proxies=proxies,
        )
        pk_resp.raise_for_status()
    except Exception as e:
        result["error"] = f"获取公钥失败: {e}"
        return result

    # 解析公钥
    try:
        pk_data = pk_resp.json()
    except Exception:
        pk_data = {}

    # 服务端错误响应识别（典型场景：accountId 不存在）
    if isinstance(pk_data, dict):
        err_code = pk_data.get("errorCode") or pk_data.get("error")
        if err_code:
            desc = pk_data.get("description") or pk_data.get("message") or ""
            result["error"] = (f"苍穹获取公钥失败，服务端返回错误: "
                               f"errorCode={err_code}, description={desc}. "
                               f"常见原因：数据中心 id 不对 / 账号类型不匹配")
            return result
        if pk_data.get("success") is False:
            desc = pk_data.get("description") or pk_data.get("message") or str(pk_data)[:200]
            result["error"] = f"苍穹获取公钥失败: {desc}"
            return result

    pem_raw = ""
    if isinstance(pk_data, dict):
        pem_raw = pk_data.get("publicKey", pk_data.get("data", ""))
    elif isinstance(pk_data, str):
        pem_raw = pk_data
    if not pem_raw and "BEGIN PUBLIC KEY" in pk_resp.text:
        pem_raw = pk_resp.text.strip()

    pem = _normalize_pem(pem_raw or pk_resp.text.strip())
    if not pem:
        result["error"] = f"无法解析 RSA 公钥，响应: {pk_resp.text[:200]}"
        return result

    # ── Step 2: RSA 加密密码 ──
    try:
        encrypted_pwd = _encrypt_password(password, pem)
    except Exception as e:
        result["error"] = f"密码加密失败: {e}"
        return result

    # ── Step 3: 提交登录 ──
    try:
        login_resp = session.post(
            f"{base_url}/auth/yzjlogin.do",
            data={
                "type": "user", "userSourceType": "2",
                "accountId": account_id, "language": language,
                "useraccount": username, "password": encrypted_pwd,
                "accessKey": access_key,
                "redirect": "index.html?formId=pc_main_console",
                "randomCode": "", "isStandard": "true",
                "customDynamicCode": "", "loginType": "1",
            },
            headers={"ajax": "true"},
            timeout=timeout,
            allow_redirects=False,
            proxies=proxies,
        )
    except Exception as e:
        result["error"] = f"登录请求失败: {e}"
        return result

    # ── Step 4: 解析结果 ──
    if login_resp.status_code not in (200, 302):
        result["error"] = f"HTTP {login_resp.status_code}: {login_resp.text[:200]}"
        return result

    login_json = {}
    try:
        login_json = login_resp.json()
    except Exception:
        pass

    if isinstance(login_json, dict):
        if login_json.get("errorCode") or login_json.get("error"):
            result["error"] = login_json.get("message",
                                login_json.get("errorMsg",
                                login_json.get("error", "登录失败")))
            return result
        if login_json.get("loginStatus") is False:
            result["error"] = login_json.get("message", "登录失败")
            return result

    # 提取 Cookie
    cookies = session.cookies.get_dict()
    cookie_str = "; ".join(f"{k}={v}" for k, v in cookies.items())
    result["cookie"] = cookie_str

    # 提取 CSRF Token
    csrf = ""
    for k, v in cookies.items():
        if "csrf" in k.lower():
            csrf = v
            break
    if not csrf:
        csrf = login_resp.headers.get("kd-csrf-token", "")
    if not csrf:
        try:
            idx_resp = session.get(f"{base_url}/index.html", timeout=timeout, allow_redirects=True)
            for pat in [r'kd-csrf-token["\'\s:=]+["\']([^"\']+)',
                        r'csrf[-_]token["\'\s:=]+["\']([^"\']+)',
                        r'kdCsrfToken\s*=\s*["\']([^"\']+)']:
                m = re.search(pat, idx_resp.text, re.IGNORECASE)
                if m:
                    csrf = m.group(1)
                    break
            if not csrf:
                csrf = idx_resp.headers.get("kd-csrf-token", "")
        except Exception:
            pass

    result["csrf_token"] = csrf
    result["success"] = bool(cookie_str)
    if isinstance(login_json, dict):
        result["user_id"] = str(login_json.get("userId", ""))

    return result


def auto_login(base_url: str, username: str, password: str,
               account_id: str = "", timeout: int = 15) -> dict:
    """
    一步登录。account_id 留空时自动检测数据中心。

    返回: {"success", "cookie", "csrf_token", "error", "account_id", "datacenters"}
    """
    if not account_id:
        try:
            dcs = list_datacenters(base_url, timeout)
        except Exception as e:
            return {"success": False, "cookie": "", "csrf_token": "",
                    "error": f"获取数据中心失败: {e}", "account_id": "", "datacenters": []}
        if len(dcs) == 1:
            dc = dcs[0]
            account_id = str(dc.get("id", dc.get("accountId", dc.get("dcId", ""))))
        elif len(dcs) > 1:
            return {"success": False, "cookie": "", "csrf_token": "",
                    "error": f"检测到 {len(dcs)} 个数据中心，请指定 account_id",
                    "account_id": "", "datacenters": dcs}
        else:
            return {"success": False, "cookie": "", "csrf_token": "",
                    "error": "未获取到任何数据中心", "account_id": "", "datacenters": []}

    result = login(base_url, username, password, account_id, timeout=timeout)
    result["datacenters"] = []
    return result


def check_session(base_url: str, cookie: str, csrf_token: str = "",
                  timeout: int = 8) -> bool:
    """检查已有 Cookie 是否仍然有效"""
    base_url = base_url.rstrip("/")
    headers = {"Cookie": cookie, "ajax": "true"}
    if csrf_token:
        headers["kd-csrf-token"] = csrf_token
    try:
        resp = requests.post(
            f"{base_url}/api/login/getUserLanguage.do",
            headers=headers, timeout=timeout,
        )
        if resp.status_code == 200:
            ct = resp.headers.get("content-type", "")
            if ct.startswith("application/json"):
                data = resp.json()
                if isinstance(data, dict) and data.get("userId"):
                    return True
            if resp.text.strip() and "login" not in resp.text.lower():
                return True
        return False
    except Exception:
        return False


# ═══════════════════════════════════════════════════════════════
# CLI 入口
# ═══════════════════════════════════════════════════════════════

def main():
    if len(sys.argv) < 2:
        print("苍穹平台自动登录工具 (自包含版)")
        print()
        print("用法:")
        print("  python cosmic_login.py <url>                         # 列出数据中心")
        print("  python cosmic_login.py <url> <user> <pwd>            # 自动登录")
        print("  python cosmic_login.py <url> <user> <pwd> <dc_id>    # 指定数据中心")
        print("  python cosmic_login.py --check <url> <cookie>        # 检查会话")
        print()
        print("示例:")
        print("  python cosmic_login.py http://127.0.0.1:8080/ierp")
        print("  python cosmic_login.py http://127.0.0.1:8080/ierp admin KDadm123")
        sys.exit(1)

    # --check 模式
    if sys.argv[1] == "--check":
        if len(sys.argv) < 4:
            print("用法: python cosmic_login.py --check <url> <cookie>")
            sys.exit(1)
        valid = check_session(sys.argv[2], sys.argv[3])
        print(f"SESSION_VALID={valid}")
        sys.exit(0 if valid else 1)

    url = sys.argv[1]

    # 仅列出数据中心
    if len(sys.argv) == 2:
        try:
            dcs = list_datacenters(url)
            print(f"DATACENTERS_COUNT={len(dcs)}")
            for dc in dcs:
                if isinstance(dc, dict):
                    dc_id = dc.get("id", dc.get("accountId", "?"))
                    dc_name = dc.get("name", dc.get("dcName", "?"))
                    print(f"  DC: id={dc_id}  name={dc_name}")
                else:
                    print(f"  {dc}")
        except Exception as e:
            print(f"ERROR: {e}")
            sys.exit(1)
        sys.exit(0)

    # 登录
    username = sys.argv[2]
    password = sys.argv[3]
    dc_id = sys.argv[4] if len(sys.argv) >= 5 else ""

    result = auto_login(url, username, password, dc_id)
    if result["success"]:
        print("LOGIN_SUCCESS")
        print(f"COOKIE={result['cookie']}")
        print(f"CSRF_TOKEN={result['csrf_token']}")
        print(f"ACCOUNT_ID={result['account_id']}")
        if result.get("user_id"):
            print(f"USER_ID={result['user_id']}")
    else:
        print(f"LOGIN_FAILED: {result['error']}")
        if result.get("datacenters"):
            print(f"DATACENTERS_COUNT={len(result['datacenters'])}")
            for dc in result["datacenters"]:
                dc_id = dc.get("id", dc.get("accountId", "?"))
                dc_name = dc.get("name", dc.get("dcName", "?"))
                print(f"  DC: id={dc_id}  name={dc_name}")
        sys.exit(1)


if __name__ == "__main__":
    main()
