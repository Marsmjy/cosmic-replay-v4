# core_hr_pfs_mapping · 数据流

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

1. 每个子实体单独的数据流章节（4 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

<!-- BEGIN ppt04-injected -->

## 数据流转 · 字段映射的 4 大写约束（FieldRelationEdit 实证）

ISV 配置 filemapmanager 时，FieldRelationEdit 校验：

| 约束 | 说明 |
|---|---|
| 同一 hrpi 字段不能被两单据字段同时回写 | 系统会按数据组装顺序覆盖写入（confirmCallback "donothing_save"）|
| 候选人字段映射 vs 人员信息字段映射不能冲突 | _c 后缀路径与无后缀路径互斥 |
| 同一信息组内字段不能多对一映射 | 报错"信息组 X 中字段 A、B 映射到同一人员信息组的字段 C" |
| 单据字段不能同时加载两个人员信息字段 | loadpersoninfo 互斥 |

## 数据流转链路

```
单据保存
  ↓
filemapmanager.subentryentity 配置驱动
  ↓
按 entryentity[] 顺序对每个 targetentity 执行：
  ├── 加载 hrpi 当前数据 (loadpersoninfo 字段)
  ├── 单据字段值 → hrpi 字段值映射 (writepersoninfo 字段)
  └── 序列化为 dataafter_tag JSON 存入 hpfs_chgrecord.entryentity
  ↓
ChgRecordEffectOp.beginOperationTransaction
  ↓
IPersonGenericService.deleteBatch + saveBatch (实际改 hrpi 底表)
```

<!-- END ppt04-injected -->
