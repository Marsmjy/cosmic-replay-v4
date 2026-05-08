# 安装指南

> 本 skill 兼容多种 AI 工具。下面给出 4 种安装方式。

---

## 方式 1 · Claude Code（推荐 · 原生支持）

### 全局安装（所有项目都能用）

```bash
# Windows
unzip cosmic-hr-expert-v1.0.0.zip -d %USERPROFILE%\.claude\skills\

# Mac / Linux
unzip cosmic-hr-expert-v1.0.0.zip -d ~/.claude/skills/
```

### 项目级安装（只本项目能用）

```bash
unzip cosmic-hr-expert-v1.0.0.zip -d ./.claude/skills/
```

### 验证

重启 Claude Code，问 "什么是 chgaction？" — 应直接回答 + 引用 docs/。

---

## 方式 2 · qoder（兼容 Anthropic skill 协议）

> qoder 的 skill 协议跟 Claude Code 一致 · 同样的 SKILL.md 格式

```bash
# 解压到 qoder 默认 skill 目录（按 qoder 文档调整）
unzip cosmic-hr-expert-v1.0.0.zip -d <qoder-skills-dir>/
# 重启 qoder
```

---

## 方式 3 · Cursor

```bash
unzip cosmic-hr-expert-v1.0.0.zip -d ./
cp cosmic-hr-expert/SKILL.md .cursor/rules/cosmic-hr-expert.mdc
mv cosmic-hr-expert/knowledge ./knowledge
mv cosmic-hr-expert/docs ./docs
```

⚠️ Cursor 的 frontmatter 格式略有不同 · 看 cursor 文档调整。

---

## 方式 4 · 通用 markdown（兜底 · 任何 AI）

```bash
unzip cosmic-hr-expert-v1.0.0.zip -d ./
# 把 SKILL.md 内容粘贴到 AI 的 system prompt
```

⚠️ AI 工具必须支持长 context（≥ 100K tokens）。

---

## 装好后 5 个验证测试（必跑 · 跑过才算装好）

### Test 1 · 概念问答（验证 docs 加载）
```
什么是 chgaction？
```
✅ 期待：3-5 句话答案 + 引用 `docs/CHGACTION_DEEP_TRACE.md` 第 N 节

### Test 2 · 场景定位（验证索引路由）
```
我要给员工档案加附表，应该看哪个场景？
```
✅ 期待：路由到 `core_hr_employee` + 给出 3-5 个关键资产路径

### Test 3 · 实体字段查询（验证 _index.json#entities 路由）
```
hrpi_employee 有哪些字段？
```
✅ 期待：字段表（fieldKey / 列名 / 类型 / 必填 / 引用）+ 引用 `entity_metadata/hrpi_employee.md`

### Test 4 · 14 资产一键复刻（验证 _assets + 工具链可用）

skill 包内带 14 个高频 ISV 资产可一键复刻·跑下面命令验证：

```bash
# 进 skill 包目录
cd <skills-dir>/cosmic-hr-expert/

# 干跑·只看产物清单（不实际生成）
python scripts/assemble_asset.py \
    --asset contract_renew_batch \
    --isv-flag bjss \
    --biz-app bjss_hlcm_ext \
    --output /tmp/test-renew/ \
    --dry-run
```

✅ 期待：输出 37 个文件清单（5 java + 7 dym + 15 SQL + 工程文件 + 部署 SOP）·`✅ 全部 GATE 通过`

**14 资产清单**（在 skill 内提问"我有哪些可一键复刻的资产"也能列出）：
- contract_renew_batch · 合同批量续签
- org_unit_transfer · 成建制划转
- bankcardchange · 银行卡变更
- swc_bonus_new · 奖金核算
- headcount_management · 编制管理
- sit_socialsecurity · 社保管理
- emp_super_rel_chart · 员工汇报关系图
- adminorgbill_extension · 组织调整单扩展
- beforecomputationcheck · 工资发放前置检查
- downermanfileword · 员工档案 word 导出
- home_check_rule · 首页规则校验
- swc_salaryapproval · 薪酬审批
- wtc_marriageverify · 婚假核验
- wtc_roster · 考勤花名册

### Test 5 · 代码生成（验证 H 类完整路径）
```
给员工加"职业资格证书"附表，4 个字段，证书编号唯一。
```
✅ 期待：6 段输出（复用判定 / 业务理解 / 元数据指导 / Java 代码 / 测试 / 部署 ）·每段引用 knowledge 来源

---

## 故障排查

### Skill 没触发？
- 检查 SKILL.md frontmatter `name` 拼写正确
- 在问题加关键词："苍穹 / HR / chgaction" 等

### 答案没引用 knowledge？
- 让 AI 工具开启"reading files in skill"权限
- 在问题前加："请引用 cosmic-hr-expert 的 knowledge 资产回答"

### Test 4 报错"_assets 目录不存在"？
- 包没解压完整·重新解压 zip
- 校验：`ls knowledge/_assets/` 应返回 14 个目录

### Test 4 报错"GATE-01 fail: tdkw 残留"？
- 不是错·是 skill 的资产模板用 `tdkw_` 占位符·assemble 工具会全局替换为客户标识
- 改用 `--isv-flag <你的客户编码>` 重新跑

### 生成的代码编译不过？
- 检查 SDK 版本（本包对应苍穹某版本 · 见 CHANGELOG.md）
- 反编译参考类的 import 可能跟你环境略有不同

### 找不到某个场景？
- 查 `knowledge/_index.json` 的 `byCloud` / `byKeyword` / `scenes`
- 包内 558 场景全覆盖·若仍不在 → 联系提供方申请扩展

---

## 升级 / 卸载

```bash
# 升级
rm -rf <skills-dir>/cosmic-hr-expert/
unzip cosmic-hr-expert-v<NEW>.zip -d <skills-dir>/

# 卸载
rm -rf <skills-dir>/cosmic-hr-expert/
```
