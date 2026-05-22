# AI Agent 修复提示词模板

当 Cosmic Replay 批量报告或单次运行显示“需 AI 诊断”时，优先使用 Web UI 的“复制 AI 修复指令”。如果需要手工组织提示词，可使用以下模板。

```text
请作为 cosmic-replay AI 修复 Agent 介入处理下面这个用例问题。

用例：{case_name}
run_id / task_id：{run_or_task_id}
环境：{env_id}
诊断类型：{failure_type}
失败位置：{failed_step}
入库状态：{write_status}
问题：{problem}

技术证据包：{evidence_url}

请按项目内 skills/cosmic-replay-troubleshooter/SKILL.md 的协议执行：
1. 先读取：
   - skills/cosmic-replay-overview/SKILL.md
   - skills/cosmic-replay-troubleshooter/SKILL.md
   - skills/cosmic-replay-troubleshooter/references/external-consultant-handoff.md
   - skills/cosmic-replay-troubleshooter/references/pageid-chain-debugging.md
   - skills/cosmic-replay-troubleshooter/references/assertion-blindspots.md
2. 读取证据包、YAML、运行事件、失败步骤、断言结果、write_status 和 pageid_trace。
3. 优先判断 HAR 原始 pageId 链路与回放 pageId 链路是否一致。
4. 再判断是 HAR 解析变量遗漏、环境字段覆盖、pageId 链路错误、异步流程未等待、断言盲区、业务校验问题，还是执行器问题。
5. 只做最小补丁。不要删除 menuItemClick、target_forms、pick_fields、no_save_failure 断言来绕过问题。
6. 不要硬补 save.post_data 替代 pageId 链路修复。
7. 修复后运行：
   ./venv/bin/python -m pytest -q tests/unit/test_env_field_resolution.py tests/unit/test_quality_and_failure_analysis.py tests/unit/test_runner.py tests/unit/test_har_extractor_regressions.py tests/unit/test_agent_evidence.py
   ./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff
8. 输出根因、修改文件、测试结果、回滚方案；如果需要人工确认环境字段，请明确列出来。
```
