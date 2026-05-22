# Demo：顾问拿到项目后的最小演示路径

## 目标

用一个脱敏/内部可用 HAR 完成“导入 → 变量维护 → 环境字段维护 → 生成 YAML → 执行 → 报告验收 → AI 修复入口”的演示。

## 前置条件

- 已安装 Python 3.10+。
- 已执行 `./venv/bin/pip install -r requirements.txt`。
- Web UI 已启动。
- 已配置目标环境账号和数据中心。
- 准备一个允许外发或已脱敏的 HAR。不要使用包含 cookie、token、手机号、身份证、真实员工数据的 HAR。

## 演示步骤

1. 打开 Web UI：`http://127.0.0.1:8768/`。
2. 点击“导入 HAR”。
3. 上传 HAR。
4. 在“智能用例变量”区域检查：
   - 编码、名称是否带随机值。
   - 描述、备注是否可维护。
   - 手机号、邮箱是否已变量化。
5. 在“环境相关字段”区域检查：
   - 组织、行政组织、岗位、枚举、基础资料字段是否出现。
   - 是否展示业务编码或名称，而不是只展示长整数内码。
   - 手工修改一个字段，点击确认。
6. 点击“生成 YAML”。
7. 进入用例详情 → 变量面板，确认刚才维护的变量与环境字段仍然一致。
8. 点击运行。
9. 查看运行结果：
   - PASS 且入库已验证：演示成功。
   - PASS 但入库未验证：说明保存响应缺少明确入库证据，需要补回查断言或人工确认。
   - FAIL：点击“复制 AI 修复指令”，把提示词交给 AI Agent。

## 演示时应强调

- Cosmic Replay 不是只看 PASS，而是看是否可交付、是否真实入库。
- pageId 链路是苍穹回放的第一排查对象。
- 环境字段允许用户维护，手工值优先。
- 已验证 9 类 HAR，但新组件仍需通过回归补规则。

## 可复制的启动命令

```bash
NO_PROXY='127.0.0.1,localhost,kdhruat.kingdee.com,.kingdee.com' \
no_proxy='127.0.0.1,localhost,kdhruat.kingdee.com,.kingdee.com' \
./venv/bin/python _start_webui.py --no-browser
```

## 可复制的回归命令

```bash
./venv/bin/python -m pytest -q tests/unit/test_env_field_resolution.py tests/unit/test_quality_and_failure_analysis.py tests/unit/test_runner.py tests/unit/test_har_extractor_regressions.py tests/unit/test_agent_evidence.py
./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff
```
