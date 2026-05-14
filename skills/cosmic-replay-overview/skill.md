# Cosmic Replay v4 - 项目概览 Skill

## 触发条件
当 AI Agent 需要理解 cosmic-replay-v4 项目架构、修改代码、排查问题时加载此 Skill。

## 项目一句话定位
金蝶苍穹平台的 HAR 录制 → YAML 用例 → 自动回放 的端到端自动化测试工具。3-5 秒执行一条用例。

## 技术栈
- 后端：FastAPI + Uvicorn (Python 3.11+)
- 前端：Alpine.js + Tailwind CSS（无构建工具，单 HTML 文件）
- 协议：苍穹 batchInvokeAction.do
- 持久化：SQLite (data/cosmic_replay.db)
- 部署：Docker Compose / 本地 start.sh

## 核心文件索引（改代码前先定位）

| 文件 | 行数 | 职责 |
|------|------|------|
| `lib/replay.py` | ~752 | 回放引擎核心：PageId 四层管理、batchInvokeAction 协议封装 |
| `lib/runner.py` | ~1331 | YAML 用例执行器：变量解析、Step Handlers、断言、SSE 推送 |
| `lib/har_extractor.py` | ~2548 | HAR→YAML 转换：变量三档识别、动作分类、反模式检测 |
| `lib/webui/server.py` | ~1389 | FastAPI 后端：15+ API 端点、SSE 实时事件流 |
| `lib/webui/static/index.html` | ~5069 | 前端 UI（宇宙主题）：用例管理/执行/HAR导入 |
| `lib/diagnoser.py` | ~126 | 响应诊断：从苍穹响应提取结构化错误 |
| `lib/advisor.py` | ~448 | 修复建议：错误分析 + YAML 补丁生成 |
| `lib/config.py` | ~346 | 两层配置：webui.yaml + envs/*.yaml |
| `lib/cosmic_login.py` | ~490 | 苍穹登录：RSA 加密 + 多重兜底 |
| `lib/kb_loader.py` | ~363 | 知识库懒加载：场景元数据 + 字段分类 |
| `lib/field_resolver.py` | - | 基础资料跨环境解析 |
| `lib/task_manager.py` | - | 任务管理与报告生成 |
| `lib/db/dao.py` | - | 数据访问对象（SQLite） |

## 模块调用链

```
Web UI (server.py + index.html)
    ↓ API 调用
runner.py (执行引擎)
    ↓ 步骤分发
replay.py (协议层) ← diagnoser.py (错误提取) ← advisor.py (修复建议)
    ↓ HTTP
苍穹平台 batchInvokeAction.do
```

## 三大核心设计决策

### 1. PageId 四层跃迁
苍穹表单协议的 pageId 不是全局唯一，而是分层的：
- L0 `root{32hex}` - 会话根（全局）
- L1 `{32hex}` - 门户级（open_portal 返回）
- L2 `{menuId}root{32hex}` - 菜单级（menuItemClick 后计算）
- L3 `{32hex}` - 表单级（getConfig 返回）

详见 architecture.md。

### 2. 变量三档识别（HAR→YAML）
- A档（必变）：number/code/name → 变量化 `${vars.test_number}`
- B档（基础资料）：org/position → 保留字面量，前端面板可改
- C档（响应回传）：pkValue/processInstId → 跨 step 引用

### 3. SSE 实时推送
执行过程通过 Server-Sent Events 流式推送：
case_start → login_ok → session_ready → step_start/step_ok → assertion_ok → case_done

## 改代码前必读清单
1. 改 replay.py → 先理解 PageId 四层跃迁规则
2. 改 har_extractor.py → 先看 AC_TIER 分类 + UNIQUE_KEY_HINTS 变量集
3. 改 runner.py → Step Handler 是 @step_handler 装饰器注册式
4. 改 server.py → SSE 事件与前端 EventSource 对应
5. 新增断言 → 用 @assertion_handler 装饰器注册

## 快速定位问题
| 症状 | 去哪看 |
|------|--------|
| 登录失败 | lib/cosmic_login.py + config/envs/*.yaml |
| pageId 404/过期 | lib/replay.py 的 page_ids 缓存和 _pending_by_app |
| 保存报错 | lib/diagnoser.py + lib/advisor.py |
| HAR 转换变量遗漏 | lib/har_extractor.py 的 UNIQUE_KEY_HINTS |
| 前端不刷新 | lib/webui/static/index.html 的 Alpine.js 响应式 |
| 用例格式错误 | 参考 cases/新增一条行政组织.yaml |
