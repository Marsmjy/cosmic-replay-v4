# 最终用户指南（Web UI）

> **这份文档面向"只想用浏览器做测试"的最终用户**。
> 不用懂命令行、不用装 Claude Code、不用看代码。

**你能做的事**：
- 导入 HAR 一键生成可自动执行的 YAML 用例
- 点按钮跑用例，实时看每一步执行
- 失败时看清楚服务端报错 + 修复建议，复制粘贴就能改
- 批量跑所有用例做回归
- 在页面里切换不同环境（SIT/UAT/客户）跑同一套用例

**你不用关心的事**：
- 苍穹协议细节、YAML 语法、Python 代码、命令行

---

可视化管理用例 · 实时执行 · 失败自动诊断 · 配置在线编辑。

---

## 1. 安装与启动

### 第一次用

```bash
cd .claude/skills/cosmic-replay

# 1. 装依赖（含 Web UI 新依赖）
pip install -r requirements.txt

# 2. 初始化 config/（从 config.example/ 拷贝）
python -m lib.webui.server --init

# 3. 启动
python -m lib.webui.server
```

默认浏览器自动打开 `http://127.0.0.1:8765`。

### 常用启动参数

```bash
python -m lib.webui.server --port 9000           # 指定端口
python -m lib.webui.server --host 0.0.0.0        # 暴露给局域网（谨慎）
python -m lib.webui.server --no-browser          # 不自动开浏览器
python -m lib.webui.server --env client_acme     # 指定起始环境
python -m lib.webui.server --init --force        # 重置 config/
```

所有参数也可在 UI 的 ⚙ 配置页里永久改。

---

## 2. 五个视图

### 2.1 Dashboard（用例库）

顶部 4 张统计卡 + 用例表格。每行显示：
- 状态点（🟢 通过 / 🔴 失败 / 🔵 运行中 / ⚫ 未跑）
- 用例名 + 描述
- 主表单 id
- 步骤数 / 标签
- 操作：打开 / ▶ 运行 / ✖ 删除

### 2.2 用例详情 + 实时执行

**左半屏**：步骤时间线
- 每步图标：✓ / ✗ / ● 脉动 / ○ 待执行
- 毫秒耗时一目了然
- 点击任意步骤看右边详情

**右半屏**：
- **总结卡**（PASS/FAIL + 步骤/断言通过率 + 总用时）
- **修复建议卡**（失败时自动展示，一键复制 YAML 补丁）
- **步骤详情**（id / 耗时 / 错误消息）
- **用例 YAML**（只读）

### 2.3 HAR 导入向导（3 步）

**Step 1** — 拖拽 HAR 文件或点击选择

**Step 2** — 预览
- 4 卡统计：主表单推断 / core 核心 / ui_reaction / noise（灰）
- 全部步骤列表（色点标识层级）
- 默认过滤 noise，ui_reaction 标 optional

**Step 3** — 命名 → 一键生成 YAML → 自动跳到详情页

### 2.4 批量运行

- 全选/全不选/逐条勾选
- **实时进度条** + 通过/失败/待执行统计
- 每条用例跑完立即更新
- 点回用例库能看到最新状态同步

### 2.5 ⚙ 配置页

**通用 tab**
- 端口 / host / 默认环境 / 浏览器自动开关 / 日志级别
- 顶部有"未保存更改"提示

**环境列表 tab**
- 折叠展开每个环境
- 表单：base_url / 数据中心 / 超时
- **凭证**：用户名密码（密码有显示/隐藏切换）+ 环境变量引用
- **基础资料 id 映射**（动态增删，跨环境可移植用例的关键）
- "新建环境"对话框（支持克隆现有环境）
- 删除环境按钮

---

## 3. 完整工作流

### 3.1 录 + 导入

```
浏览器 F12 录 HAR
   ↓
Web UI → 导入 HAR → 拖文件
   ↓
预览（检查步骤分类是否合理）
   ↓
命名 → 生成 YAML
   ↓
自动跳到用例详情页
```

### 3.2 跑 + 看建议

```
点 "▶ 运行"
   ↓
实时看步骤执行（SSE 推送）
   ↓
失败？自动显示"修复建议"卡
   ↓
点 "📋 复制" 拿到 YAML 补丁
   ↓
用 IDE 粘进 cases/xxx.yaml
   ↓
回 Web UI → 重新运行
```

### 3.3 跨环境回归

```
新建一个环境（如 client_acme）→ 填客户 base_url + basedata id
   ↓
回到 Dashboard → 切换顶栏环境 → SIT → ACME
   ↓
点任意用例运行 → 看是否仍然 PASS
```

---

## 4. HTTP API 参考

（供自动化/CI 脚本用）

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/info` | 启动信息 |
| GET | `/api/config?mask=true` | 配置（敏感打码） |
| POST | `/api/config/init?force=true` | 初始化 config/ |
| PUT | `/api/config/webui` | 保存 webui 偏好 |
| GET | `/api/envs` | 环境列表 |
| PUT | `/api/envs/{id}` | 保存/新建环境 |
| DELETE | `/api/envs/{id}` | 删除环境 |
| GET | `/api/cases` | 用例列表 |
| GET | `/api/cases/{name}/yaml` | 读 YAML |
| PUT | `/api/cases/{name}/yaml` | 保存 YAML |
| DELETE | `/api/cases/{name}` | 删除用例 |
| POST | `/api/cases/{name}/run` | 触发运行，返回 `{run_id}` |
| GET | `/api/runs/{id}/events` | SSE 事件流 |
| POST | `/api/har/preview` | 上传 HAR 预览 |
| POST | `/api/har/extract` | HAR 转 YAML |

### SSE 事件类型

| 事件 | 含义 | payload 关键字段 |
|---|---|---|
| `case_start` | 用例开始 | name, description |
| `login_start` / `login_ok` | 登录过程 | username, user_id |
| `session_ready` | 会话就绪 | root_page_id |
| `step_start` | 步骤开始 | id, type, detail, optional |
| `step_ok` / `step_fail` | 步骤结束 | id, duration_ms, errors |
| `assertion_ok` / `assertion_fail` | 断言结果 | type, msg |
| `fixes_ready` | 修复建议产出 | fixes[] |
| `case_done` | 用例结束 | passed, duration_s, step_ok, step_fail, ... |
| `case_error` | 执行异常 | error |

---

## 5. CI 集成样例

```yaml
# .github/workflows/replay-regression.yml
name: replay regression
on: [pull_request]
jobs:
  replay:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with: { python-version: '3.11' }
      - run: pip install -r .claude/skills/cosmic-replay/requirements.txt
      - name: run smoke cases via CLI
        env:
          COSMIC_USERNAME: ${{ secrets.COSMIC_USERNAME }}
          COSMIC_PASSWORD: ${{ secrets.COSMIC_PASSWORD }}
          COSMIC_DATACENTER_ID: ${{ secrets.COSMIC_DATACENTER_ID }}
        run: |
          cd .claude/skills/cosmic-replay
          for case in cases/*.yaml; do
            python -m lib.runner run "$case" || exit 1
          done
```

**CI 不用 Web UI**，直接用命令行 runner。Web UI 定位是人类开发者的交互工具。

---

## 6. 常见问题

**Q：Web UI 启动后显示"后端已就绪"空壳？**
A：前端 HTML 缺失。确认 `lib/webui/static/index.html` 存在。

**Q：配置改了没生效？**
A：
- `host/port` 需要重启 Web UI 进程
- 其他字段保存后自动热重载
- 或点设置页右上角"↻ 重新加载"

**Q：我的密码在哪？**
A：
- UI 显示默认打码 `****`
- 存储在 `config/envs/xxx.yaml` 明文（`config/` 不入 git）
- 强烈推荐用环境变量引用：`credentials.username_env: COSMIC_USERNAME`

**Q：HAR 文件上传后存哪？**
A：`har_uploads/` 目录（自动 `.gitignore`）。这些是临时文件，可安全删除。

**Q：前端能同时打开多个 tab 吗？**
A：能，但都连同一个后端。在不同 tab 跑不同用例互不影响。

**Q：批量跑能并行吗？**
A：当前版本串行（避免共用 session 冲突）。并行是后续扩展方向。

**Q：能用 Docker 跑吗？**
A：能。示例 Dockerfile：
```dockerfile
FROM python:3.11-slim
COPY .claude/skills/cosmic-replay /app/cosmic-replay
COPY .claude/skills/cosmic-login /app/cosmic-login
WORKDIR /app/cosmic-replay
RUN pip install -r requirements.txt
ENV COSMIC_LOGIN_SCRIPT=/app/cosmic-login/cosmic_login.py
EXPOSE 8765
CMD ["python", "-m", "lib.webui.server", "--host", "0.0.0.0", "--no-browser"]
```

---

## 7. 限制与路线图

**当前不支持**：
- 页面内 YAML 编辑器（IDE 改，保存后 Web UI 自动读最新）
- 自动 apply 修复补丁（只能手动复制粘贴）
- 并行批量运行
- 历史执行记录持久化（进程重启丢）
- 多用户/权限（假设本机单人使用）

**路线图**：
- v0.2：YAML 编辑器（Monaco） + auto-apply 补丁
- v0.3：Playwright 录制 HAR 集成
- v0.4：LLM 生成 YAML
- v0.5：历史记录 + 统计图表