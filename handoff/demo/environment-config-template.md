# 环境配置模板说明

不要把真实账号、密码、cookie、token 放进外发包。本文件只说明顾问需要向业务方收集哪些信息。

## Web UI 配置项

| 字段 | 示例 | 说明 |
| --- | --- | --- |
| 环境 ID | `sit` / `uat` | Cosmic Replay 内部环境标识 |
| 环境名称 | `SIT 环境` | 展示名 |
| base_url | `http://example.kingdee.com/ierp` | 金蝶苍穹环境地址 |
| username | `demo_user` | 执行账号 |
| password | `******` | 执行密码，不要写入外发文档 |
| datacenter_id | `xxxx` | 苍穹数据中心 ID |
| NO_PROXY | `127.0.0.1,localhost,.kingdee.com` | 内网域名绕过代理 |

## 启动时建议

```bash
NO_PROXY='127.0.0.1,localhost,kdhruat.kingdee.com,.kingdee.com' \
no_proxy='127.0.0.1,localhost,kdhruat.kingdee.com,.kingdee.com' \
./venv/bin/python _start_webui.py --no-browser
```

## 顾问需要确认

- 浏览器可以访问 `base_url`。
- 执行账号可以手工登录。
- 账号有目标菜单和保存/提交权限。
- 若需要真实入库验证，顾问具备查询业务数据的权限。
- 若公司网络有代理，`NO_PROXY` 已覆盖金蝶内网域名。
