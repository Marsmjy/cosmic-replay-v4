# core_hr_pfs_mapping · 业务流程

> **聚合场景**：core_hr_pfs_mapping · 包含 4 个 hbss 字典实体（**ISV 零代码扩展核心引擎**：把人事单据的字段值按配置自动写入 hrpi_* 底表。filemapmanager 主表挂着 entryentity（每行一...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**ISV 零代码扩展核心引擎**：把人事单据的字段值按配置自动写入 hrpi_* 底表。filemapmanager 主表挂着 entryentity（每行一个目标实体）+ subentryentity（每行一个字段映射对，含 sourcefield/targetfieldnew/loadpersoninfo/writepersoninfo）。ISV 加自定义字段不需写 OP · 在此 form 上配置即可。fieldrelation/fieldrelationc 是字段对照明细查询视图。核心反编译：FieldRelationEdit (455 lines)、FileMapManagerSaveOp+FileMapManagerSaveValidator、ChgActionEditPlugin (244 lines · chgcategory 变动类型驱动)。

## 涉及实体（4 个）

- `hpfs_filemapmanager`
- `hpfs_fieldrelation`
- `hpfs_fieldrelationc`
- `hpfs_hrbm_config`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的业务流程章节（4 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

<!-- BEGIN ppt04-injected -->

## 业务流转 · ISV 零代码字段扩展 6 步法（PPT slide 48 实证）

ISV 在标品单据加字段 + 同步回写人员档案，是最常见的扩展场景。**99% 走 filemapmanager 配置，无需写 Java OP**。

```
开始
  ↓
1. HR 业务模型工具 → 扩展单据实体 + 人员信息实体字段
   ⚠️ 字段标识必须遵循 {ISV}_a_xxx (目标任职) / _b_xxx (原任职) 规则
  ↓
2. HR 业务模型工具 → 标品 metadata 扩展 → 自动产 ISV 扩展元数据
  ↓
3. 开发平台修改样式（信息面板布局）
  ↓
4. 单据-人员信息映射配置 (hpfs_filemapmanager) → 建字段关联
   - entryentity[] 加 targetentity (hrpi_employee / hrpi_assignment 等)
   - subentryentity[] 加 sourcefield → targetfieldnew
   - 勾 writepersoninfo (回写) / loadpersoninfo (加载)
  ↓
5. 保存映射配置 → FileMapManagerSaveValidator 校验 4 大约束
  ↓
6. 测试单据保存 → 验证 hrpi 底表是否回写
```

## 4 类扩展字段的实施差异（PPT slide 41）

| 字段类型 | 是否需 filemapmanager | 实施方式 |
|---|---|---|
| 雇佣信息/任职经历单行字段 | 需要 | filemapmanager 配置 sourcefield → targetfieldnew |
| 员工单行表扩展字段 | 需要 | filemapmanager 配置 + 组装数据时直接变更 |
| 员工多行表（分录形式）| 需要 | filemapmanager + **分录中变动类型字段必须设值** |
| 员工多行表（字段形式 · 如职级职等）| 看情况 | 仅 1 条生效中 → filemapmanager 即可；多条生效 → **必须写 Java 代码** inject |

<!-- END ppt04-injected -->
