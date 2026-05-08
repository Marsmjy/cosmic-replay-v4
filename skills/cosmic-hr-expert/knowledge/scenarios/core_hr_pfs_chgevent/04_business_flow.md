# core_hr_pfs_chgevent · 业务流程

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

1. 每个子实体单独的业务流程章节（5 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

<!-- BEGIN ppt04-injected -->

## 业务流转 · 8 大变动事件 + 11 大人事事务（PPT 实证）

### 8 大变动事件常量（ChgActionNewConstants 反编译）

```
CHG_EVENT_INFO_QUIT       离职
CHG_EVENT_INFO_RETIRE     退休
CHG_EVENT_INFO_TRANSFER   调动
CHG_EVENT_INFO_INIT       初始化（入职）
CHG_EVENT_INFO_MODIFY     信息修改
CHG_EVENT_INFO_EXTERNAL_REGULAR  外部转正
```

### 11 大人事事务清单（PPT slide 6）

| 事务 | 关联模板 | chgevent 大类 |
|---|---|---|
| 入职申请/办理 | hpfs_hrhombillorgtplext | INFO_INIT |
| 转正申请/批量转正 | hpfs_hrhdmbillorgtplext | INFO_MODIFY |
| 调动申请 | hpfs_hrhdmbillorgtplext | INFO_TRANSFER |
| 兼职申请/兼职终止 | hpfs_hrpartbillorgtplext / hpfs_hrendbillorgtplext | TRANSFER 子类 |
| 借调发起/借调终止 | hpfs_hrpartbillorgtplext / hpfs_hrendbillorgtplext | ⚠️ 需 ISV 加大类 |
| 外派 派出/派入/派返 | hpfs_hrpartbillorgtplext | TRANSFER 子类 |
| 离职申请 | hpfs_hrhtmbillorgtplext | INFO_QUIT |
| 退休 | hpfs_hrhtmbillorgtplext | INFO_RETIRE |
| 信息变更 | hpfs_hrcommonbilltplext | INFO_MODIFY |
| 再入职 | hpfs_hrhombillorgtplext (扩展) | INFO_INIT 子类 |

## 业务流转 · ChgActionEditPlugin 字段联动

```
chgcategory 选中
  ↓
查 chgcategory.chgevent.id
  ├── INFO_MODIFY / INFO_INIT  → 清空 fowtype + chgtype（信息修改/初始化不涉任职变化）
  └── 其他事件 → 联动 fowtype = chgcategory.beforeflowtype/afterflowtype
  ↓
chgcategory.id == 1060L (调动)
  → createnewassign = true (强制新建分配)
  ↓
handleInvalidAssignment(chgEvent)
  ├── INFO_QUIT/RETIRE → invalidassign = true (强制作废原分配)
  ├── INFO_TRANSFER/EXTERNAL_REGULAR + createnewassign=true → invalidassign = true
  ├── INFO_INIT → createnewassign = false
  └── 其他 → 按 INVALID_CATEGORY_SET 决定
```

<!-- END ppt04-injected -->
