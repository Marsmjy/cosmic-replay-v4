# 变更日志

## v1.2.0 · 2026-05-08 · EmployeeField 控件 + AP-029 反哺（case_005 真发发现）

### 触发场景

case_005_batchorgadjust 改造时·真发查 hspm_ermanfile 已废·一开始计划改成 BasedataField+hrpi_empposorgrel·但用户在苍穹开发助手 IDE 里发现：**字段类型选"HR员工"时·查询实体下拉自带 6 个标品选项**——这是新模型的标准做法·比手工配 BasedataField 外键更优。

### 反哺 3 处

#### 1. 新增 AP-029 · `BasedataField + hspm_ermanfile` 老模式拦截

- 8 个 trigger_keywords 命中老写法
- correct_alternative 引导 EmployeeField + 6 选 1 查询实体
- evidence_sources 链到 case_005 真发探针 + 标品 haos_staffcase 实证

#### 2. cosmic-entity-metadata-guide.md §3.2.2 加 EmployeeField 完整章节

- 6 个查询实体选项表（员工/任职/组织分配 + 3 个薪酬镜像）
- dym XML 形态示例
- 标品实证（haos_staffcase / haos_othemproleorgrel）
- ⚠ OpenAPI 限制提示（buildMeta/modifyMeta 不支持 EmployeeField）
- 场景决策树（4 类业务场景对应选哪个）

#### 3. _intent_routing.json#"新建人事单据" 加【强制】notes

- 触发"工号/员工/任职/组织分配"字段时·强制走 EmployeeField
- 关联 AP-029 + cosmic-entity-metadata-guide §3.2.2

### 包元信息

- 反模式：27 → **28**（+AP-029）
- 意图：78（不变·notes 加强）
- knowledge/_shared/_standard_metadata/er_model/cosmic-entity-metadata-guide.md：+50 行 §3.2.2

### 配套 memory

- `cosmic_employeefield_authoritative.md`（HR 员工字段权威用法）
- 已注册到 MEMORY.md "写知识库铁律"段

---

## v1.1.0 · 2026-05-08 · Step H-00 反模式自检门禁（v1.0.9 trigger 加强后仍漏 → 加 stop-the-line）

### 事故续集（v1.0.9 修了 trigger·LLM 仍漏）

v1.0.9 给 AP-025/026/027 加了 trigger keywords·但 LLM 实操"我要生成一个退休单·并校验法定退休年龄"时·依然命中 3 个 P0 反模式·原因：
- trigger 命中是平台层信号·SKILL.md 没把"命中 = 必须钉死在响应顶部"做成强制门禁
- 已有的 §"零代码硬约束"段（v1.0.5 加）藏在 H-0a 后面·LLM 写到那时方案已成型

### 修复（SKILL.md 改造 · 不动 _antipatterns.json）

**新增 Step H-00 · 反模式自检门禁**（钉死在 H 类入口·v1.0.5 §"零代码硬约束"前）：
- H-00.1 提取关键词
- H-00.2 心智 grep _antipatterns.json 18 条 trigger
- H-00.3 心智 grep _intent_routing.json 候选三档全展示
- **H-00.4 强制响应顶部钉死"反模式自检"段**（含 7 条铁律检查清单 ✅/❌）
- H-00.5 命中任何 P0 AP·进入修复重写模式
- H-00.6 自检失败的强制回退（YEARFRAC/DATEDIF/60/"下拉"/"hbp_histimeseqtpl"等关键词的拦截规则）

### 配套

- 新建 memory `feedback_skill_must_grep_antipatterns_before_output.md`
- 沉淀事故 case `cosmic_hr_knowledge_explorer/skill_incidents/2026-05-07_retirement_bill_brainmaking/`（user_input + audit_report + feedback_actions.yaml）
- 新建 `skill_incidents/README.md` 作为 skill 输出事故复盘体系入口

### 包元信息

- 反模式：27（不变 · 同 v1.0.9）
- 意图：78（不变）
- SKILL.md 行数：857 → ~960

---

## v1.0.9 · 2026-05-07 · 固 AP-026/027/025 trigger 防 LLM 在退休单方案踩坑

### 事故（qoder 实测·退休单方案输出深度审查）

LLM 输出的退休单方案犯了 3 个已知反模式但未被 trigger 拦截：

1. **AP-026 漏网**：方案写了 `DATEDIF({ISV}_birthdate, {ISV}_retiredate, "Y")` 和 `YEAR({ISV}_retiredate) - YEAR({ISV}_birthdate)` 两个具体 Excel 函数语法——根因是 AP-026 的 `correct_alternative` 自己写了 `YEAR(retiredate)-YEAR(birthdate)` 教 LLM 猜函数名
2. **AP-027 漏网**：方案用 `年龄 < 60` / `年龄 < 55` / `年龄 < 50` 三元比较——但 trigger 只有"法定退休年龄 60"等不含比较运算符的文本
3. **AP-025 漏网**：方案直接选 `hbp_histimeseqtpl` 从头建·完全没提 `htm_quit` 复用（第①档）——但 trigger 只匹配"退休单 不评估 chgaction"等显式声明

### 修复

- **AP-026**：trigger 扩展（+DATEDIF( / YEARFRAC( / YEAR({ISV}_ / DATE({ISV}_ / MONTH({ISV}_ / IF({ISV}_ 等函数调模式）；correct_alternative 移除自身对 YEAR() 的错误建议；why_wrong 加强"苍穹公式引擎不是 Excel"
- **AP-027**：trigger 扩展（+年龄 < 60 / 年龄 < 55 / 年龄 < 50 / 退休时年龄 < / 当前退休日期时年龄不足 / AND 退休时年龄 等比较模式）
- **AP-025**：trigger 扩展（+选择继承模板.*hbp_histimeseqtpl / 选择 hbp_histimeseqtpl / 不评估 第.档 / 不检查 第.档）
- **新建人事单据 intent**：加 AP-025/026/027 到 antipattern_ids；notes_for_llm 加 3 条【禁止】级警告 + 快照 vs 展示字段区分指南

### 包元信息

- 反模式：27（不变·升级 trigger）
- 意图：78（不变·升级 notes）

---

## v1.0.8 · 2026-05-07 · P0 知识库清理 · 全量清除 ⚡待精修 骨架标记

### 清理范围

- **1501 处 `⚡待精修` 标记全量清除**·覆盖 515 个 md 文件
- 12 个 ISV 资产场景的 11 块 md 全量处理：attendance_wtc_marriageverify / attendance_wtc_roster / core_hr_downermanfileword / core_hr_emp_super_rel_chart / core_hr_home_check_rule / hbss_headcount_management / org_dev_adminorgbill_extension / payroll_bankcardchange / payroll_beforecomputationcheck / payroll_sit_socialsecurity / payroll_swc_bonus_new / payroll_swc_salaryapproval
- 同时清理了 503 个非资产场景的 `11_upstream_downstream_logic.md` 跨云引用表中的标记

### 清理策略

- 表头 `业务含义（⚡待精修）` → `业务含义`
- 表单元格 `⚡待精修` → `—`（em-dash·表"尚未注释"）
- 段落前缀 `⚡待精修：补xxx` → `补xxx`（保留指令文本·去标记）
- 标题后缀 `· ⚡待精修` → 移除
- 页眉 `> **生成方式**：polish_from_dscan.py 自动·⚡待精修...` → 移除
- 所有代码引用（ClassName.java:line）完整保留

### 包元信息

- 反模式：27（不变）
- 意图：78（不变）

---

## v1.0.7 · 2026-05-07 · 修 querySubOrgToList 签名错误 + 防 null 类型脑补

### 真实事故（用户在 qoder 实测发现）

LLM 输出："`IHAOSBatchAdminOrgInfoQueryService.querySubOrgToList(List<Long>, Date, String selectFields=null)`"

但 IDEA 代码截图显示真实签名是：
```java
querySubOrgToList(List<Long> orgIdList, Date date, Integer level)
```

### 多层根因

1. **v3 反编译缺该接口**：`_sdk_audit/_decompiled/` 里没 `IHAOSBatchAdminOrgInfoQueryService` 接口源
2. **资产模板 null 调用**：`new Object[]{orgIdList, currentDate, null}` 全 null·LLM 看不出真实类型
3. **curated_sdk example 不全**：只写 `new Object[]{orgId}`·LLM 没法看到完整签名
4. **业务规则 md 直接错**：02_business_rules.md 写"String selectFields"·属于反推时脑补错

### 修复

- 🔴 **02_business_rules.md 改正**：从 `querySubOrgToList()` 改为 `querySubOrgToList(List<Long> orgIdList, Date date, Integer level)`·标 level=null 表所有层级
- 🔴 **curated_sdk.json 加完整接口卡片**：含 signature / params 类型 / returns / warning（防再被脑补）
- 🔴 **加 AP-028·防 LLM 把 null 类型脑补错**：trigger 含 `selectFields=null` 等关键词·强制 LLM 必须查反编译验证类型

### 包元信息

- 反模式：26 → **27**（+AP-028）

---

## v1.0.6 · 2026-05-07 · 撤销 v1.0.5 对 6 大模板的错误禁令

### 关键认知修正

v1.0.5 加 AP-025 时·我误说"6 大模板（hbp_bd_*）只覆盖基础资料·不覆盖单据"——**这是过纠**。

PPT01:155 真实"模板继承铁律"是 3 档优先级：
1. 业务领域专属模板（hpfs_*/htm_quit/hom_onboard 等）
2. hbp 通用 6 大模板（含 hbp_histimeseqtpl）← 兜底·**包括用于单据**
3. 平台基础模板

**hbp_histimeseqtpl 用于退休单是合法的**·不是禁用。错的是"跳过第 1 档评估直接选模板"。

### 修复内容

- 🔴 **重定义 AP-025**：从"禁 hbp_*  做单据"改为"模板选择不按 PPT01:155 三档铁律"
- 🔴 **SKILL.md H-0c 撤销错误说法**：6 大模板真实定位是"业务领域模板没有时的通用兜底·包括用于单据"
- 🔴 **退休单实例重写**：给出"路径 A 组合最佳·路径 B 复用 htm_quit"两条·明确 hbp_histimeseqtpl 是合法第②档选择
- 🟡 **"新建人事单据"意图 notes_for_llm 加入正确决策树**

### 包元信息

- 反模式：26（不变·AP-025 重定义不增减）
- 意图：78（不变·配置升级）
- 铁律：11

---

## v1.0.5 · 2026-05-07 · 退休单方案 6 个深层问题修复

### 修复（基于 qoder 实战会话深度分析）

- 🔴 **修问题 1·LLM 误激活 cosmic-prototype**：description 加 SKIP 规则·明确 prototype/codegen skill 不能抢
- 🔴 **修问题 2·LLM 把 BOS 设计器和 HR 业务模型工具说成两件事**：H-0c 加术语澄清·BOS 设计器是模型工具的操作界面·不是另一个工具
- 🔴 **修问题 3·LLM 答"零代码"路径还顺手贴 Java**：加铁律 11"零代码硬约束"·priority=1-5 任何档被选 → 整段回答 0 个 Java 代码块
- 🔴 **修问题 4·选错模板（hbp_histimeseqtpl 给单据用）**：6 大模板（hbp_bd_*）只覆盖**基础资料**·不覆盖单据。新建人事单据要继承 hpfs_*/hbill_* 标品单据基类。**加 AP-025**
- 🟡 **修问题 5·字段定义忽略标品已有字段**：H-0c 加"先查 entity_metadata·避免重复建"
- 🟡 **修问题 6·业务规则公式凭空猜函数名（YEARFRAC/DATEDIF）**：明确知识包没沉淀苍穹真实公式函数清单·LLM 不许猜·**加 AP-026**
- 🟡 **修问题 7·审批流术语不准**：保留待补·标 ⚠
- 🟡 **修问题 8·延迟退休政策硬编码 60/55/50**：2025 起渐进式延迟退休·**加 AP-027**
- 🟡 **修问题 9·没识别 chgaction 路径优先级**：升级"新建人事单据"意图·candidates 第 1 优先级改为 chgaction 路径

### 包元信息

- 反模式：24 → **27**（+AP-025/026/027）
- 意图：78（不变·升级了"新建人事单据"配置）
- 铁律：10 → **11**（加零代码硬约束）

---

## v1.0.4 · 2026-05-07 · 修复 2 个真实事故

### 修复（实战会话发现）

- 🔴 **修问题 1·开发机绝对路径泄漏**：
  - `_assets/*/deploy_sop.md` 13 文件 + `scenarios/*/*.md` 1009 文件曾写死开发机绝对路径
  - 客户解压 skill 后路径完全不一样·跑不起来
  - 修：全替换为 `<skills-dir>/cosmic-hr-expert`

- 🔴 **修问题 2·assemble 产物 java 包结构压扁**：
  - 原 bug：所有 java 全堆 `<isv>/<java_root_subpath>/` 一个固定路径·**子包结构丢失**
  - 真实事故：复刻 swc_bonus_new 时 56 java 在 1 个包·dym FQN 找不到类·会 ClassNotFoundException
  - 修：assemble_asset.py 从 java tmpl 的 `package` 声明读真实子包路径·还原物理目录
  - 修后 14 资产全部正确还原（swc_bonus_new 49 子目录·headcount_management 12 子目录·所有 GATE 通过）

### 验证

- 14 资产 assemble + GATE 全过
- swc_bonus_new 真实 dym 绑定 `khr.swc.hsbm.biz.swc.plugin.form.khr_bonus_approval_sheet.second.BonusApprovalSheetBillSecondPlugin` ↔ java 物理路径 100% 对应

---

## v1.0.3 · 2026-05-07 · 退休单 + HIES 修复

- 加 AP-023（退休单跳 6 模板）+ AP-024（HIES 没 SOP）
- 加意图"新建人事单据" + "HR 导入导出扩展"
- 升级 hies_entity_import 06md（CS-00 完整 4 步配置 SOP）
- 测试：V20 20/20 全过

---

## v1.0.2 · 2026-05-07 · HR 业务模型工具优先（铁律 9）

### 新增（基于实战会话复盘 SKILL_FEEDBACK_REPORT）

- ⭐ **铁律 9：HR 业务模型工具优先**（金字塔决策第 1 层硬约束）
  - 任何"加字段 / 加附表 / 加分录 / 加视图 / 改属性 / 字段映射"类需求·**99% 走模型工具配置化**·0 行 Java
  - 覆盖 5 大云：org_dev / core_hr / hr_hrmp / attendance / payroll
  - 实证：[`docs/PPT01_DEEP_TRACE.md` 第 5 节·能力清单](docs/PPT01_DEEP_TRACE.md)（slide 33-38）

- ⭐ **2 条新反模式**：
  - `AP-021` · 在 hrpi_employee 主表加 EntryEntity 当人员档案附表（导致历史链膨胀 + 标品升级冲突）
  - `AP-022` · 基础扩展类需求跳过 HR 业务模型工具直接 ISV 编码

- ⭐ **13 个扩展类意图**（含 MODEL_TOOL_FIRST 元意图继承）：
  - org_dev：行政组织加字段 / 组织调整单加字段 / 岗位加字段 / 职位职级加字段
  - core_hr：入职单加字段 / 调动单加分录 / 转正单加字段 / 兼职单加字段 / 离职单加分录 / 候选人加附表
  - hlcm：合同档案加字段 / 合同申请单加字段 / 单据档案字段映射

- ⭐ **H 类升级 7 步法 → 8 步法**：
  - H-0a：方案推荐表从 4 档扩为 6 档·加 `model_tool_config` (priority=3) + `business_rule_config` (priority=4)
  - H-0c（新）：HR 业务模型工具能力判定（关键词命中时强制·读 PPT01:112 对照）
  - 命中"加字段/加附表/加分录..."关键词 → 强制 priority=3 必须出现·priority=6 ISV 编码必须打 ⚠ 标记

- ⭐ **`core_hr_employee/06_customization_solutions.md` 升级**：
  - 加 CS-00（模型工具配置化·0 行 Java·包含 6 大附表模板决策矩阵 + 6 步 SOP）
  - CS-04 升级：加 4-B（ERManFileCommonAttPlugin 高频继承点·档案附表专属）+ 4-C（反指引）

- ⭐ **自我介绍金字塔**：从 3 档扩到 5 档·明确"HR 业务模型工具配置化"为决策第 2 档

### 包元信息

- 反模式：19 → **21**
- 意图：61 → **74**
- byKeyword：982（不变）
- 文件数：~11700

---

## v1.0.1 · 2026-05-06 · 资产复刻 + 558 场景全量

### 新增

- ⭐ **14 个 ISV 资产模板**（一键复刻完整工程包）：
  - core_hr：contract_renew_batch / org_unit_transfer / downermanfileword / emp_super_rel_chart / home_check_rule
  - payroll：bankcardchange / swc_bonus_new / swc_salaryapproval / sit_socialsecurity / beforecomputationcheck
  - attendance：wtc_marriageverify / wtc_roster
  - hr_hrmp：headcount_management
  - org_dev：adminorgbill_extension
- ⭐ **`scripts/assemble_asset.py` 工具**·包内自带·客户解压后直接跑·1 行命令出 37+ 文件完整工程包
- ⭐ **场景规模 172 → 558**：扩到 5 朵云全量（core_hr / org_dev / hr_hrmp / payroll / attendance / 其他 hsas/hsbs/hrcs/wtbd/...）
- ⭐ **实体规模 590 → 950**·OP 规模 514·反编译类 644
- ⭐ **数据基底 3 顶层 JSON**：
  - `_intent_routing.json` 59 业务意图路由
  - `_antipatterns.json` 18 反模式拦截
  - `_scene_relations.json` 30+ 场景关系图

### Skill 升级

- ⭐ **SKILL.md 重写为 4 级路由检索协议**：
  - Level 0 启动必读（5 顶层 JSON · ~600KB · LLM 心智地图）
  - Level 1 候选场景展开（4 sidecar JSON · ~5KB / 场景）
  - Level 2 11md 选择性读（5-30KB · 不全读）
  - Level 3 实证锚点（反编译 5-15KB）
- ⭐ **H 类代码生成升级为 7 步法**：
  - Step H-0a 方案推荐（_intent_routing + _antipatterns + 推荐表）
  - Step H-0b CS 复用判定
  - Step H-3 一键资产复刻路径（命中 14 资产 → 给 assemble 命令）
  - Step H-4-7 ISV 自建路径
- ⭐ **铁律加 1 条·共 8 大铁律**：
  - 新铁律 8：优先一键资产复刻·14 资产命中时不写代码·给 assemble 命令

### 客户验证

- ⭐ **INSTALL.md 加 5 个验证测试**（含 14 资产 dry-run 验证）

### 包元信息

- 总文件数：11663
- 包目录大小：126.55 MB
- zip 后大小：~22 MB
- SHA256：见同名 .sha256 文件

---

## v1.0.0 · 2026-04-29 · 首发

### 知识库内容

- ✅ **172 场景**完整覆盖（core_hr 51 + hr_hrmp 81 + org_dev 28+ + 其他）
- ✅ **5 份金蝶官方培训 PPT 沉淀**：
  - PPT01 定制化开发总论（金字塔决策方法论）
  - PPT02 组织发展云
  - PPT03 核心人力 + 人员信息
  - PPT05 劳动合同
  - CHGACTION 机制 22 段双源（PPT04 + 反编译）
- ✅ **跨云引用主索引**：226 实体 · 313 跨云引用
- ✅ **反编译实证**（11 核心场景 · 47 类 · 7857 行 Java）

### Skill 能力

- ✅ 6 类问答场景（概念 / 场景 / SDK / 决策 / 代码 / 评审）
- ✅ 5 段代码生成（业务理解 / 指导方案 / Java 代码 / 测试 / 部署）
- ✅ 5 大铁律 + chgaction 字段命名规则 + 跨云协作铁律
- ✅ 多 AI 工具适配（Claude Code / qoder / Cursor / 通用 markdown）

### 已知限制（v1.0.0）

- ⚠️ 第四波"员工变动协作"场景未建（v1.0.1 已扩充其他云场景）
- ⚠️ 薪酬云 / 考勤云场景未建（v1.0.1 已加 attendance / payroll 整云）
- ⚠️ 反编译实证只含 11 核心场景（其他场景的实物未打包 · 体积考虑）
- ⚠️ 流水线工具（探针 / polish / quality_gate）不在本包（开发方私有）

### 包元信息

- 总文件数：1120
- 包目录大小：5.33 MB
- zip 后大小：~2.2 MB

---

## 后续规划（提供方维护）

### v1.1（计划）
- 12 资产 11md 业务释义补全（当前 1207 处 ⚡待精修 · 标杆为 W1 contract_renew_batch / 100% 完成度）
- 加 IDEA 插件 v3 一键集成
- 加 BEC 事件中心深度索引

### v2.0（计划）
- 全场景反编译实物（不限 11 核心）
- PPT 06-09 反哺
- skill 自动化测试集成（每次 build 跑 10 测试 case）
