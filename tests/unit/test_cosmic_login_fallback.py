import sys
from pathlib import Path

import pytest

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib import cosmic_login


class _Resp:
    def __init__(self, status_code: int, json_data=None, text: str = ""):
        self.status_code = status_code
        self._json_data = json_data
        self.text = text
        self.headers = {}

    def raise_for_status(self):
        if self.status_code >= 400:
            raise Exception(f"{self.status_code} Server Error: Bad Gateway for url: <mock>")

    def json(self):
        if self._json_data is None:
            raise ValueError("no json")
        return self._json_data


class _Session:
    def __init__(self):
        self.verify = False
        self.headers = {}
        self.cookies = type("C", (), {"get_dict": lambda _self: {"sid": "x", "kd_csrf_token": "t"}})()
        self._posts = []
        self._gets = []

    def post(self, url, data=None, timeout=None, proxies=None, headers=None, allow_redirects=None):
        self._posts.append(url)
        if url.startswith("http://") and url.endswith("/auth/getPublicKey.do"):
            return _Resp(502, json_data=None, text="bad gateway")
        if url.startswith("https://") and url.endswith("/auth/getPublicKey.do"):
            return _Resp(200, json_data={"publicKey": "-----BEGIN PUBLIC KEY-----\\nAAAABBBBCCCCDDDDEEEEFFFFGGGGHHHHIIIIJJJJKKKKLLLLMMMMNNNNOOOOPPPP\\n-----END PUBLIC KEY-----"})
        if url.startswith("https://") and url.endswith("/auth/yzjlogin.do"):
            return _Resp(200, json_data={"userId": "u1"})
        raise AssertionError(f"unexpected post url {url}")

    def get(self, url, timeout=None, allow_redirects=None):
        self._gets.append(url)
        return _Resp(200, json_data=None, text="")


class _SessionNoCsrf(_Session):
    def __init__(self):
        super().__init__()
        self.cookies = type("C", (), {"get_dict": lambda _self: {"sid": "x"}})()


def test_login_falls_back_to_https_when_http_public_key_502(monkeypatch):
    sess = _Session()
    monkeypatch.setattr(cosmic_login.requests, "Session", lambda: sess)
    monkeypatch.setattr(cosmic_login, "_encrypt_password", lambda _pwd, _pem: "enc")

    res = cosmic_login.login(
        "http://kdhruat.kingdee.com:8022/ierp",
        "user",
        "pwd",
        "dc",
        timeout=1,
        proxies={"http": None, "https": None},
    )

    assert res["success"] is True
    assert res["effective_base_url"].startswith("https://")


def test_login_generates_csrf_when_server_omits_token(monkeypatch):
    sess = _SessionNoCsrf()
    monkeypatch.setattr(cosmic_login.requests, "Session", lambda: sess)
    monkeypatch.setattr(cosmic_login, "_encrypt_password", lambda _pwd, _pem: "enc")
    monkeypatch.setattr(cosmic_login.uuid, "uuid4", lambda: type("U", (), {"hex": "f" * 32})())

    res = cosmic_login.login(
        "https://kdhruat.kingdee.com:8022/ierp",
        "user",
        "pwd",
        "dc",
        timeout=1,
        proxies={"http": None, "https": None},
    )

    assert res["success"] is True
    assert res["csrf_token"] == "f" * 32
