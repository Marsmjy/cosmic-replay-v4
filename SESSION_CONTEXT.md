# COSMIC REPLAY v4 会话上下文

> 此文件记录项目开发过程和关键决策，用于会话恢复。下次对话时说"读取 SESSION_CONTEXT.md"即可快速恢复。

---

## 一、项目基本信息

| 项目 | 值 |
|------|-----|
| 名称 | cosmic-replay-v4 |
| 位置 | `/Users/mars/Desktop/cosmic-replay-v4` |
| 端口 | 8768 |
| 访问地址 | http://127.0.0.1:8768 |
| API文档 | http://127.0.0.1:8768/docs |
| GitHub | https://github.com/Marsmjy/cosmic-replay-v4.git |
| 功能 | HAR文件自动化测试回放工具（金蝶苍穹平台） |

---

## 二、版本历史

| 版本 | 端口 | 状态 | 说明 |
|------|------|------|------|
| v1 | 8765 | 已删除 | 早期版本 |
| v2 | 8766 | 已删除 | 备份于 cosmic-replay-v2-backup |
| v3 | 8767 | 已删除 | 尝试版本 |
| **v4** | **8768** | **当前** | **宇宙主题UI + 变量识别增强** |

---

## 三、开发历程

### Phase 1: 项目创建（2026-04-28）
- [x] 从 v2 复制创建 v4
- [x] 修改端口为 8768
- [x] 清理 v1/v2/v3 目录

### Phase 2: UI重构（2026-04-28）
- [x] 实现宇宙主题 UI
- [x] 创建 Logo SVG（轨道环绕设计）
- [x] 渐变背景 + 毛玻璃效果
- [x] 现代化配色（cyan/violet/purple）
- [x] 统计卡片样式升级

### Phase 3: Bug修复（2026-04-28）
- [x] 修复光标样式（表格default/输入框text/按钮pointer）
- [x] 修复用例名称编辑功能
- [x] 添加 display_name API

### Phase 4: 变量识别优化（2026-04-29）
- [x] 优化 HAR 变量识别
- [x] 新增 ENV_RELATED_FIELDS（企业/组织/职位等环境相关字段）
- [x] 新增 ENUM_FIELDS（性别/证件类型等枚举字段，不变量化）
- [x] **修复 click 步骤 post_data 变量识别**（关键修复）
- [x] 添加 vars_labels 中文标签

### Phase 5: 执行日志增强（2026-04-29）
- [x] case_start 显示变量模板定义
- [x] session_ready 显示解析后变量值
- [x] 统一执行历史和调试执行日志格式

### Phase 6: 发布打包（2026-04-29）
- [x] 创建发布目录
- [x] 编写部署文档（DEPLOYMENT_GUIDE.md）
- [x] 编写快速入门（QUICKSTART.md）
- [x] 创建示例用例（example/新增员工示例.yaml）
- [x] 打包为 cosmic-replay-v4-release.zip（460KB）

---

## 四、关键文件清单

```
cosmic-replay-v4/
├── lib/
│   ├── har_extractor.py      # ⭐ HAR解析 + 变量识别核心
│   ├── runner.py             # ⭐ 执行引擎 + 日志增强
│   ├── replay.py             # 回放API调用
│   ├── cosmic_login.py       # 金蝶登录
│   └── webui/
│       ├── server.py         # FastAPI服务
│       ├── log_store.py      # 日志存储
│       └── static/
│           ├── index.html    # ⭐ 前端UI（2895行）
│           ├── logo.svg      # Logo SVG
│           └── css/
│               └── theme-v4.css  # 宇宙主题样式
├── cases/                    # 测试用例YAML
├── config/
│   └── envs/
│       └── sit.yaml          # 环境配置
├── docs/                     # 文档
├── .env                      # 环境变量（账号密码）
├── start.sh                  # 启动脚本
└── requirements.txt          # Python依赖
```

---

## 五、核心代码位置

### 5.1 变量识别（har_extractor.py）

```python
# 行 371-640: detect_var_placeholders() 函数
# 关键逻辑：
#   - UNIQUE_KEY_HINTS: number/code/name/fullname 等
#   - ENV_RELATED_FIELDS: 企业/组织/职位（需变量化）
#   - ENUM_FIELDS: 性别/证件类型（不变量化）
#   - click步骤处理（行 553-580）：用户编辑后直接点保存的场景
```

### 5.2 执行日志（runner.py）

```python
# 行 547-555: case_start 事件发送变量信息
event = {
    "type": "case_start",
    "vars_def": {...},      # 变量模板
    "vars_labels": {...},   # 中文标签
}

# 行 320-340: session_ready 显示解析后变量值
```

### 5.3 前端UI（index.html）

```python
# 行 1-100: Header + Logo
# 行 130-200: Dashboard统计卡片
# 行 2817-2870: formatEventDetail() 日志格式化
```

---

## 六、Git提交历史

```
d7e2546 - 修复：click步骤post_data中的字段值变量化
70d3751 - 优化执行日志：显示变量信息方便排查环境
025b8f9 - 优化HAR变量识别：环境相关字段变量化
...
```

---

## 七、关键决策记录

| 决策 | 原因 | 日期 |
|------|------|------|
| 从v2重新创建v4 | v3尝试不成功，回退v2重新开始 | 2026-04-28 |
| 宇宙主题UI | 用户要求现代化设计 | 2026-04-28 |
| click步骤变量识别 | 用户反馈HAR导入后编码名称未变量化 | 2026-04-29 |
| 执行日志增强 | 方便排查是否进入正确环境 | 2026-04-29 |
| 变量中文标签 | 提升用户体验 | 2026-04-29 |

---

## 八、待办事项

### 高优先级
- [ ] 用户验证新功能（重新导入HAR测试变量识别）
- [ ] 更多HAR导入测试

### 中优先级
- [ ] 完善 UI 细节（主题切换、输入框样式）
- [ ] 添加更多示例用例

### 低优先级
- [ ] 性能优化
- [ ] 国际化支持

---

## 九、常见问题速查

### Q1: 启动失败
```bash
# 检查端口占用
lsof -i :8768
# 更换端口
./start.sh --port 9000
```

### Q2: 导入HAR后变量未识别
检查字段名是否在 UNIQUE_KEY_HINTS 中：
- number, code, name, fullname, simplename
- empnumber, certificatenumber, phone

### Q3: 运行失败
查看执行日志第一条，确认变量值是否正确。

---

## 十、发布包信息

| 文件 | 位置 |
|------|------|
| 发布包 | `/Users/mars/Desktop/cosmic-replay-v4-release.zip` |
| 解压目录 | `/Users/mars/Desktop/cosmic-replay-v4-release/` |
| 大小 | 460KB |

---

## 十一、快速恢复指令

下次会话直接说：

```
项目：cosmic-replay-v4
位置：~/Desktop/cosmic-replay-v4
端口：8768

继续上次的开发工作，读取 SESSION_CONTEXT.md 了解详情。
```

或者更简单：

```
搜索 cosmic-replay v4 开发
```

---

**最后更新**: 2026-04-29
**维护者**: Mars
