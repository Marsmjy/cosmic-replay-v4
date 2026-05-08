# core_hr_pfs_chgevent · 数据流

> **聚合场景**：core_hr_pfs_chgevent · 包含 5 个 hbss 字典实体（**chgaction 是 ISV 零代码扩展的中枢配置**：每张人事单据头有 BasedataField → hpfs_chgaction。chgaction...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**chgaction 是 ISV 零代码扩展的中枢配置**：每张人事单据头有 BasedataField → hpfs_chgaction。chgaction 上挂 chgcategory（→ chgevent 大类）+ filemapmanager 字段映射规则。审批通过后标品自动按 chgaction 配置改 hrpi_* 底表。ChgActionEditPlugin 决定：chgcategory 切换 / chgtype 任职状态变化 / fowtype 流入流出 / createnewassign 是否创建新分配 / invalidassign 是否作废原分配。8 大变动事件常量：INFO_QUIT/RETIRE/TRANSFER/MODIFY/INIT/EXTERNAL_REGULAR 等。

## 涉及实体（5 个）

- `hpfs_chgevent`
- `hpfs_chgcategory`
- `hpfs_chgaction`
- `hpfs_chgreason`
- `hpfs_chgactionlist`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的数据流章节（5 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

<!-- BEGIN ppt04-injected -->

## 数据流转 · 6 大可继承单据模板（PPT slide 35）

ISV 写自定义人事单据**必须继承下面 6 个模板之一**（否则要重造模板能力）：

| 模板编码 | 业务用途 | 模板自动处理 |
|---|---|---|
| hpfs_hrhombillorgtplext | 新增员工类（入职、外包入场）| 创建 employee + empentrel + assignment + empposorgrel |
| hpfs_hrhdmbillorgtplext | 任职变动（调动、转岗、调岗转正）| 原任职终止 + 新任职新增 |
| hpfs_hrhtmbillorgtplext | 用工终止（离职、退休、外包离场）| assignment 失效 + 释放编制 |
| hpfs_hrpartbillorgtplext | 多任职新增（外派、借调、兼职）| 新增任职但不失效原任职 |
| hpfs_hrendbillorgtplext | 多任职终止（兼职终止、外派终止）| 结束某一任职 |
| hpfs_hrcommonbilltplext | 复杂场景（外部转正、跨公司调动）| 二开自定义雇佣信息+任职变动逻辑 |

## 模板预置自动处理的 4 大实体

无需配置 filemapmanager · 模板会**自动**处理：

```
hrpi_employee        员工档案
hrpi_empentrel       雇佣信息
hrpi_assignment      组织分配
hrpi_empposorgrel    任职经历
```

只有**扩展字段** 才需 filemapmanager 配置。

<!-- END ppt04-injected -->
