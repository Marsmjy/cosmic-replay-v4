# 开发者指南 —— 用 Claude Code 扩展 / 改这个 skill

> 这份文档面向**想给本 skill 加功能 / 改 bug / 出新版本**的开发者。
> 你不用自己写代码，**用 Claude Code 对话就能改**——前提是看懂这里的地图。

---

## skill 的本质是什么

**SKILL.md + `lib/` + 几份 docs** 构成一个"AI 可读、AI 可改"的工具包。

- `SKILL.md` 告诉 Claude：**这个工具是干嘛的、触发词有哪些、应该按什么流程帮用户**
- `lib/` 是真正的 Python 代码，Claude 读它、改它、跑它
- `docs/` 是给人看的说明，但 Claude 也会读它吸收领域知识

**当你在 Claude Code 里对话时，Claude 自动用这些资料来辅助你**。

---

## 三种扩展场景

### 场景 A：加新 step 类型

比如你想加一个 `wait_for_response_contains` step，等服务端响应出现某字符串再继续。

**对话示例**：
```
你：
  我想给 cosmic-replay 加一个 step 类型叫 wait_for_response_contains。
  它在循环发 loadData 直到响应里出现指定字符串，最多重试 N 次。

Claude 应该做的：
  1. Read lib/runner.py 看现有 @step_handler 怎么注册
  2. Read docs/extending.md 看 YAML schema 约定
  3. 在 runner.py 加一个新的 @step_handler("wait_for_response_contains")
  4. 改 docs/extending.md 添加 schema 说明
  5. 跑一遍 cases/admin_org_new.yaml 确认没破坏现有
```

**你要做的**：就说句话。审查 Claude 改的代码，有疑问再讨论。

### 场景 B：加新的 advisor 规则

比如你发现"XXX 字段长度超过 20"的错误 advisor 还不懂，想让它也能给出建议。

**对话示例**：
```
你：
  advisor.py 加一条规则：错误文本含"长度超过"或"字符数不能超过"时，
  识别成 length_exceeded 类型，建议用户截断 vars 里的值。

Claude 应该做的：
  1. Read lib/advisor.py 看 ERROR_PATTERNS
  2. 加一条新正则 + 新的 error_type
  3. 写对应的 _build_XXX_fix 分支
  4. 手工跑一次 runner 验证建议出现
```

### 场景 C：改配置 schema

比如你想让 env 配置加一个 `proxy_url` 字段（全部请求走代理）。

**对话示例**：
```
你：
  env 配置加个 proxy_url 字段，replay 调接口时走它。

Claude 应该做的：
  1. Read lib/config.py 看 EnvConfig dataclass
  2. 加 proxy_url 字段
  3. Read lib/replay.py 看 login / CosmicFormReplay 初始化
  4. 加 proxy 参数传递
  5. 改 config.example/envs/sit.yaml 加字段说明
  6. 改 Web UI 设置页（index.html 里的 envDraft）
```

### 场景 D：加新的文档 / 用例

这类最轻，直接对话：
```
你：
  给这个 skill 加一条参考用例，场景是"员工离职"。
Claude：Read cases/admin_org_new.yaml 参考结构，创建 cases/employee_leave.yaml
```

---

## 修改 skill 的关键原则

### 1. 不要破坏"yaml 是核心"的抽象

任何新功能都应该**延续 YAML 用例的可读性**：
- 新 step 类型的字段要语义化
- 不要引入二进制格式、不要塞一堆 hash
- 字段命名贴近业务（`wait_for_xxx` 而不是 `poll_until`）

### 2. 向后兼容

改 `runner.py` 时**不破坏已有用例**：
- 老的 YAML 必须能跑
- 加字段用可选（`step.get("new_field", default)`）
- 不要改已有 step handler 的 handler 签名

### 3. 事件类型要一起改

改 runner 逻辑时，相应的 SSE 事件也要加。新 step 类型最好有自己的 `step_xxx` 事件，否则 Web UI 显示不好看。

### 4. Config 要能序列化

改 `EnvConfig` / `WebUIPrefs` 时，要考虑：
- `config.to_dict(mask_secrets=True)` 打码行为
- `save_env` 的 yaml 写回
- 前端 `envDraft` 的字段同步

---

## 一张"文件-用途"地图

```
lib/replay.py          ← 苍穹协议层（登录/会话/batchInvokeAction）
                        改它：懂苍穹协议后才能改，风险最高
lib/runner.py          ← YAML 执行引擎 + step 处理器
                        改它：加新 step / 断言，中等风险
lib/har_extractor.py   ← HAR → YAML 的翻译
                        改它：调产出质量，低风险
lib/advisor.py         ← 错误诊断规则库
                        改它：加识别模式、补字段映射，零风险
lib/diagnoser.py       ← 扒 bos_operationresult 弹窗
                        改它：少数情况，需要了解苍穹弹窗结构
lib/field_resolver.py  ← 动态查基础资料 id
                        改它：为新类型字段加解析，低风险
lib/config.py          ← 配置读写
                        改它：加新配置字段时，中等风险
lib/webui/server.py    ← HTTP endpoint 层
                        改它：加新 API 时，低风险
lib/webui/static/      ← 前端单 HTML 文件
                        改它：加新视图 / 改交互，低风险
```

---

## 测试 / 验证循环

改完任何东西后，**必跑**：

```bash
# 1. 用例跑通（协议层没破坏）
cd .claude/skills/cosmic-replay
python -m lib.runner run cases/admin_org_new.yaml
# 期待：8 步全 [ok]，最后 FAIL 是预期的（SIT 账号数据差异）

# 2. Web UI 启动无报错
python -m lib.webui.server --no-browser --port 8765
curl -s http://127.0.0.1:8765/api/info

# 3. HAR 转 YAML 产出质量
python -m lib.har_extractor preview path/to/any.har
```

---

## 版本化发布

当你觉得"改动够多、值得 bump 一版"时：

```
SKILL.md 顶部 description 保持不变
requirements.txt 如果有新依赖要加
docs/ 相关章节更新
cases/ 不要手改（用户资产）
```

建议版本号记在 SKILL.md 里：`v0.1` → `v0.2` → `v0.3`。

---

## 什么时候**不该**用 Claude 改

有两种情况：
1. **改苍穹协议层（replay.py 的签名/pageId 逻辑）** —— 风险高，最好亲自动手 + 抓包对比
2. **改 YAML schema 的根结构** —— 会破坏所有已有用例

这两类改动，**先在对话里让 Claude 给你方案**，但**代码改动你要亲审**。

---

## 常见扩展方向（未来 / 路线图）

### v0.2 候选
- [ ] YAML 编辑器（页面里改 + 保存）
- [ ] auto-apply 修复补丁（advisor 建议直接改 YAML）
- [ ] `${resolve:basedata:...}` 动态 id 解析接入 runner
- [ ] 批量运行支持并行

### v0.3 候选
- [ ] Playwright 录制 HAR（独立 skill `cosmic-har-recorder`）
- [ ] LLM 诊断兜底（advisor 不认识的错误丢给 deepseek）

### v0.4 候选
- [ ] 历史执行记录持久化
- [ ] Dashboard 时序图 / 用例健康度趋势

---

## 一句话

**SKILL.md + docs/ 是你和 Claude 的契约**。写清楚规则、触发词、流程，你就能用对话快速演进工具。代码只是契约的落地。