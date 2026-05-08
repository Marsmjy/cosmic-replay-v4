# cosmic-hr-expert · 苍穹 AI HR 定制化领域专家 Skill

> **版本**：v1.0.2 · 2026-05-07
> **形态**：可装到 Claude Code / qoder / Cursor 等兼容 markdown skill 协议的 AI 工具
> **包大小**：~22 MB（zip）/ ~127 MB（解压）· 11700+ 文件
> **离线可用**：✅ 不依赖任何在线服务
> **核心改动 (v1.0.2)**：HR 业务模型工具优先（铁律 9）·覆盖 21 反模式 + 74 业务意图

---

## 30 秒理解

把这个 skill 装到你的 AI 工具（Claude Code / qoder / Cursor），就能：

```
✅ 答业务概念："什么是 chgaction？历史模型 vs 时间轴有何区别？"
✅ 找场景定位："给员工加附表应该看哪个场景？"
✅ 查实体字段："hrpi_employee 有哪些字段？"
✅ 查 SDK 用法："HRPIEmployeeServiceHelper 怎么用？"
✅ 给定制建议："加自定义离职单继承哪个模板？"
✅ 一键资产复刻：14 个高频 ISV 资产·跑 1 行命令出 37+ 文件完整工程包
✅ 生成代码："给员工加职业资格证书附表，证书编号唯一" → 全栈输出
✅ 评审代码："我这段插件代码合规吗？"
✅ 跨云协作："admin_org 跨云被谁消费？"
```

**核心价值**：把 558 个苍穹 HR 场景 + 14 个 ISV 资产模板 + 950 实体定义 + 644 反编译类 + 5 份金蝶官方培训 PPT 打包成**离线知识 + 一键复刻**能力。

---

## 5 分钟上手

### 1. 安装

详见 [`INSTALL.md`](INSTALL.md)。

**Claude Code**（最常用）：
```bash
# Windows
unzip cosmic-hr-expert-v1.0.1.zip -d %USERPROFILE%\.claude\skills\

# Mac / Linux
unzip cosmic-hr-expert-v1.0.1.zip -d ~/.claude/skills/

# 重启 Claude Code · skill 自动加载
```

### 2. 装好后跑 5 个验证测试

按 `INSTALL.md` 的"装好后 5 个验证测试"逐个跑·全过 = 装好。

### 3. 真用一次

**用例 1·一键资产复刻**（推荐先体验·最快出价值）：
```bash
# 输入需求："我要做一套合同批量续签"
# AI 自动识别 → 命中 contract_renew_batch 资产 → 给出复刻命令：
cd <skills-dir>/cosmic-hr-expert/
python scripts/assemble_asset.py \
    --asset contract_renew_batch \
    --isv-flag bjss \
    --biz-app bjss_hlcm_ext \
    --output D:/myproject/contract_renew_batch-bjss/

# 产出 37 文件（5 java + 7 dym + 15 SQL + 工程文件 + 部署 SOP）
# 客户按 deploy_sop.md 6 步部署即可上线
```

**用例 2·定制代码生成**（资产没覆盖时）：
```
输入："给员工档案加'职业资格证书'附表，4 字段（证书名称/发证日期/证书编号/证书级别），证书编号唯一"
↓
skill 输出 6 段：复用判定 / 业务理解 / 元数据指导 / Java 代码 / 测试 / 部署说明
```

详见 [`examples/03_codegen_full.md`](examples/03_codegen_full.md)。

---

## 知识包内容

```
cosmic-hr-expert/
├── SKILL.md                        主文件 · 4 级路由检索协议 + 7 步代码生成法
├── README.md                       本文档
├── INSTALL.md                      4 种宿主安装步骤 + 5 个验证测试
├── CHANGELOG.md                    版本变更日志
├── LICENSE.md                      许可证
├── docs/                           5 份金蝶官方培训 PPT 沉淀
│   ├── PPT01_DEEP_TRACE.md         金字塔决策方法论 + 完整 SDK 树
│   ├── PPT02_DEEP_TRACE.md         组织发展云
│   ├── PPT03_DEEP_TRACE.md         核心人力 + 人员信息
│   ├── PPT05_DEEP_TRACE.md         劳动合同
│   └── CHGACTION_DEEP_TRACE.md     chgaction 机制 22 段双源
├── scripts/                        ⭐ 客户可直接跑的工具
│   └── assemble_asset.py           一键资产复刻工具（v1.0.1 加）
└── knowledge/
    ├── _index.json                 总目录 466KB · 含 byKeyword 655 关键词倒排
    ├── _intent_routing.json        59 业务意图路由表
    ├── _antipatterns.json          18 反模式拦截库
    ├── _scene_relations.json       30+ 场景关系图
    ├── _cloud_index.json           5 云分组导航
    ├── _cross_cloud_index.json     跨云引用主索引
    ├── _cross_cloud_reports/       5 朵云反向报告
    ├── scenarios/                  ⭐ 558 场景 × 16 件套 md+JSON
    ├── _shared/                    950 实体字段定义 + 共享反编译
    ├── _sdk_audit/_decompiled/     644 反编译类全量
    ├── code_templates/             HR 代码模板
    ├── ext_points/                 扩展点档案
    └── _assets/                    ⭐ 14 个 ISV 资产模板（v1.0.1 加）
        ├── contract_renew_batch/   合同批量续签
        ├── org_unit_transfer/      成建制划转
        ├── bankcardchange/         银行卡变更
        ├── swc_bonus_new/          奖金核算
        ├── swc_salaryapproval/     薪酬审批
        ├── sit_socialsecurity/     社保管理
        ├── beforecomputationcheck/ 工资发放前置检查
        ├── headcount_management/   编制管理
        ├── adminorgbill_extension/ 组织调整单扩展
        ├── home_check_rule/        首页规则校验
        ├── emp_super_rel_chart/    员工汇报关系图
        ├── downermanfileword/      员工档案 word 导出
        ├── wtc_marriageverify/     婚假核验
        └── wtc_roster/             考勤花名册
```

---

## 核心知识规模

| 资产 | 规模 |
|---|---:|
| 场景总数 | **558** |
| · core_hr 核心人力 | 61 |
| · org_dev 组织发展 | 47 |
| · hr_hrmp HR 基础服务 | 82 |
| · payroll 薪酬福利 | 20 |
| · attendance 工时假勤 | 104 |
| · 其他（hsas/hsbs/hrcs/wtbd/...）| 244 |
| **ISV 资产模板（一键复刻）** | **14** ⭐ |
| 实体定义 | 950 |
| 操作清单（含继承树）| 514 |
| 反编译 java 类 | 644 |
| 沉淀 PPT 文档 | 5 份（411 slides 浓缩）|
| 跨云引用索引 | 226 实体 · 313 引用 |
| 业务意图路由 | 59 个 |
| 反模式拦截库 | 18 个 |

---

## 适用范围

✅ **适合**：
- 苍穹 AI HR 定制化项目（ISV 二开 / 客户实施）
- 需要 1 键复刻完整工程包的高频需求（14 资产覆盖）
- 项目 Code Review · 对照 SDK 白名单
- 跨云协作设计（226 实体引用关系）

⚠️ **不适合**：
- 苍穹 ERP / 财务 / 供应链场景
- 苍穹平台底层 / BOS 框架问题
- 实时跑代码 / 调试运行（skill 不带运行环境）

---

## 数据时效

- **快照时间**：2026-05-06
- **覆盖云**：core_hr / org_dev / hr_hrmp / payroll / attendance / 其他（hsas/hsbs/hrcs/wtbd/wtte/sitbs/...）
- **升级方式**：等提供方发新版（v1.0.x / v1.1.0 / v2.0.0）

---

## 出问题怎么办？

1. **答错了 / 编造了** → 截图 + 问题给提供方反馈
2. **某个场景缺信息** → 查 `knowledge/_index.json` 的 byKeyword / scenes·不在 → 反馈
3. **assemble_asset.py 报错** → 看 `INSTALL.md` 故障排查 · 多数是 `--isv-flag` 参数没传
4. **想要全量反编译 / 流水线工具** → 联系提供方申请扩展包

---

## License

见 [`LICENSE.md`](LICENSE.md)
