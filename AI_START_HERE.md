# AI Agent 启动说明

你现在接手的是 Cosmic Replay 项目，一个用于金蝶苍穹 HAR 导入、YAML 用例生成、自动回放执行和入库验收的本地工具。

## 你的任务

请在本地启动这个项目，并协助用户导入 HAR、生成 YAML、执行用例。如果执行失败，按项目内 skill 排查，优先检查 pageId 链路。

## 先读这些文件

1. `README.md`
2. `skills/cosmic-replay-overview/SKILL.md`
3. `skills/cosmic-replay-troubleshooter/SKILL.md`
4. `skills/cosmic-replay-troubleshooter/references/external-consultant-handoff.md`
5. `skills/cosmic-replay-troubleshooter/references/pageid-chain-debugging.md`

## 本地启动

```bash
python3 --version
python3 -m venv venv
./venv/bin/pip install -r requirements.txt
./venv/bin/python _start_webui.py --no-browser
```

启动后打开：

```text
http://127.0.0.1:8768/
```

## 如果没有 Python

本项目需要 Python 3.10+。如果 `python3 --version` 不存在或版本低于 3.10，请 AI Agent 先帮用户安装 Python，再回到“本地启动”继续执行。

常见安装方式：

- macOS：优先使用 `brew install python@3.11` 或从 Python 官网安装。
- Windows：从 Python 官网安装 Python 3.11+，安装时勾选 “Add python.exe to PATH”。
- Linux：使用系统包管理器安装 `python3.11`、`python3.11-venv` 和 `python3-pip`。

如果当前 AI IDE 自带 Python 运行时，也可以直接用它创建虚拟环境；关键是最终能执行 `python -m venv venv`、安装 `requirements.txt` 并启动 `_start_webui.py`。

## 网络和代理

顾问环境默认不需要配置 VPN 或代理，也不要默认给启动命令添加 `NO_PROXY/no_proxy`。

只有在公司网络、系统代理或 AI IDE 代理导致金蝶接口访问异常时，再按目标域名追加 `NO_PROXY/no_proxy`。例如浏览器可以访问金蝶接口，但执行器请求出现 502/代理错误时，可临时这样启动：

```bash
NO_PROXY='127.0.0.1,localhost,目标金蝶域名,.kingdee.com' \
no_proxy='127.0.0.1,localhost,目标金蝶域名,.kingdee.com' \
./venv/bin/python _start_webui.py --no-browser
```

## 环境配置

执行用例前，需要在 Web UI 右上角齿轮里配置环境，或参考 `.env.example` 和 `config.example/webui.yaml`。

必须确认：

- `base_url` 是目标金蝶苍穹环境地址。
- 账号密码可手工登录。
- 数据中心 ID 正确。
- 如存在代理异常，再把目标域名加入 `NO_PROXY/no_proxy`；正常顾问环境不需要配置。

## 使用流程

1. 打开 Web UI。
2. 点击“导入 HAR”。
3. 上传 HAR 文件。
4. 检查并维护“智能用例变量”和“环境相关字段”。
5. 点击“生成 YAML”。
6. 进入用例详情后运行。
7. 查看运行结果和批量报告。
8. 若失败或 PASS 但入库未验证，点击“复制 AI 修复指令”，按证据包排查。

## 排障原则

- pageId 是金蝶苍穹服务端模型上下文，不只是 URL 参数。
- 新 HAR 执行失败时，先对比 HAR 原始 pageId 链路与回放 pageId 链路。
- 不要一开始硬补 `save.post_data`。
- 不要删除 `menuItemClick`、`target_forms`、`pick_fields`、`no_save_failure` 来让用例变绿。
- PASS 不代表真实入库，必须看入库证据或人工确认。

## 验证命令

```bash
./venv/bin/python -m pytest -q tests/unit/test_env_field_resolution.py tests/unit/test_quality_and_failure_analysis.py tests/unit/test_runner.py tests/unit/test_har_extractor_regressions.py tests/unit/test_agent_evidence.py
./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff
```

## 注意

这个 zip 包不应包含真实 HAR、数据库、账号密码、cookie、token。需要用户在本地重新配置环境并导入自己的 HAR。
