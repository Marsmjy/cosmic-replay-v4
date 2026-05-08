# core_hr_org_unit_transfer · 能力边界

> **场景类型**：业务流型聚合场景（非单 form）
> **核心动作**：调度任务批量处理"组织/岗位变更"·写入标品 hdm_transferbatch 调动单
> **数据源**：[`_assets/org_unit_transfer/`](../../_assets/org_unit_transfer/) ISV 资产 + 11 个标品 form
> **业务文档**：金蝶官方"成建制划转复用说明书.docx" v1.0 (2025-08-04 梅小雪/宁杰)
> **代码实证**：dcs_clean/hr-hdm-orgtransfer/ @ feature-standard cf299a0
> **建设来源**：dcs-case-run K2 v2.1 case_001 反哺 / 2026-05-06

## 业务定义

成建制划转：苍穹 HR 核心人力调配管理 (hdm) 提供的"调度任务批量处理"能力·覆盖 3 类典型组织变更：

| changeType | 业务场景 | 触发条件（变动场景编码）|
|:---:|---|---|
| **A** | 组织上级调整（成建制划转） | `1020_S` |
| **B** | 组织名称调整 | `1030_S` |
| **C** | 岗位名称调整 | `1020_S` (岗位侧·岗位主表) / `1080_S` (修订) |

变更发生后·系统不再要求 HR 一个一个改人员任职·而是**调度任务批量给该组织/岗位下所有人员生成调动单**·走标品 `hdm_transferbatch.submiteffect` 自动协同 `hrpi_*` 任职经历底表。

## 能做什么 ✅

- ✅ 组织上级调整后·**批量给"组织 + 全部子组织"下所有人员**生成调动单（标品 `hdm_transferbatch`）
- ✅ 组织更名后·批量刷新这些人员任职经历的"历史组织"字段（`adminorgvid`）
- ✅ 岗位更名后·批量给该岗位下人员生成调动单 + 刷新"历史岗位"字段（`positionvid`）
- ✅ 通过中间表 `${ISV_FLAG}_orguntitransfer` 维护**执行状态机**（01 待执行 / 02 执行中 / 03 完成）·保证幂等
- ✅ 失败有 `${ISV_FLAG}_resultmsg` / `_resultmsg_tag` 详细记录·下次调度自动重试（状态保留 01）
- ✅ ISV 可通过修改 `CHANGE_SCENE_*_NUMBER` 常量集**扩展更多变动场景编码**（详见 [`_assets/org_unit_transfer/customization_points.md`](../../_assets/org_unit_transfer/customization_points.md) EP-01）

## 不能做什么 ❌

- ❌ **不做单人调动**·标品 `hdm_transferbatch` 自带·不需要本资产
- ❌ **不做跨公司转移**·业务流不同（参 docx §1.3）
- ❌ **不私改 hrpi_* 底表**·完全交给标品 `submiteffect` op 协同·这是与"旧 hbis 客户版做法（用 ChgRecordUtil.packageMsg + MQ 自发改 8 实体）"的根本区别
- ❌ **不发 BEC 事件**·跨云协同由标品自动处理
- ❌ **不调度高实时**·默认每天跑·`EXECUTE_DAYS=1`·适合处理过去 1 天积累的变更

## 与相邻场景边界

| 场景 | 关系 |
|---|---|
| `core_hr_transfer` (调动管理 hdm/transferapply) | **同应用·不同入口**：core_hr_transfer 是单据驱动调动·本场景是调度驱动批量调动 |
| `homs_orgchgrecord` (组织变更记录) | **数据源**：本场景 A/B 类型从此读组织变动事件 |
| `hbpm_chgrecordevt` (岗位变更事件) | **数据源**：本场景 C 类型从此读岗位变动事件 |
| `haos_changescene` (变动场景) | **元数据维度**：本场景的 `1020_S/1030_S/1080_S` 编码定义在 `haos_changescene` |
| `haos_adminorgdetail` (行政组织详情) | **被查询**：本场景查历史组织版本（`iscurrentversion!=1`）|
| `hbpm_positionhr` (岗位历史版本) | **被查询**：本场景查历史岗位版本 |
| `hrpi_empposorgrel` (任职经历) | **被刷新**：本场景刷新生效日之后任职的历史字段（不私改·走标品 op）|
| `hdm_transferbatch` (调动单) | **目标产出**：本场景最终向其 SAVE + SUBMITEFFECT |

## 适用 Tier 评级（v3 spec §3.5）

- **Tier 1**：✅ 已建（scene_doc_lite + entity_metadata + 元数据规则）
- **Tier 2**：✅ 06_customization_solutions 完整 + 11md 全过 + 反编译实证 100% (case_001 D.x deep_scan)
- **Tier 3**：⚡ 准备升级（待加 `rules_chain_all.json` + `_decompiled/` 引用 anchors）

## 关联资产 / 模板

- **完整可复刻资产**：[`_assets/org_unit_transfer/`](../../_assets/org_unit_transfer/)·一键 `python scripts/assemble_asset.py --asset org_unit_transfer --isv-flag <客户标识> --biz-app <客户应用>` 出完整工程
- **业务文档**：金蝶官方"成建制划转复用说明书.docx" v1.0
- **真扫报告**：[deep_scan_inventory.md](../../../dcs_regression/pending/case_001_org_unit_transfer/deep_scan_inventory.md) / [deep_scan_audit.md](../../../dcs_regression/pending/case_001_org_unit_transfer/deep_scan_audit.md)
