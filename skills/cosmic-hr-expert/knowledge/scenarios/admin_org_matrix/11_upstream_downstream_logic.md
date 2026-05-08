# admin_org_matrix · 上下游逻辑

> **聚合场景**：admin_org_matrix · 包含 2 个 hbss 字典实体（矩阵组织 · 聚合场景...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

矩阵组织 · 聚合场景

## 涉及实体（2 个）

- `haos_structproject`
- `haos_structure`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：中
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的上下游逻辑章节（2 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 02 沉淀）

> 本场景的业务语义补充见 [PPT02_DEEP_TRACE.md](../../docs/PPT02_DEEP_TRACE.md)
> - 16 实体清单（含历史模型类型/物理表）
> - 7 个标品定时任务（含 haos_func_orgsync_SKDP_S 同步平台）
> - 30+ OpenAPI（行政组织/岗位/职位查询保存等）
> - 5 SDK 扩展点（IAfterEffectAdminOrgExtPlugin / IAdminOrgTreeLabelExtPlugin 等）
> - 综合参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md) 总论金字塔

### 关键 SDK Helper（按 org_dev 常用）

```java
HAOSServiceHelper   // 提供新增/变更/启用/禁用组织
HBJMServiceHelper   // 提供新增/变更/启用/禁用职位
HBPMServiceHelper   // 提供新增/变更/启用/禁用岗位
```

### 业务事件订阅点

```
haos.adminOrgChangeEvent           组织变动事件
hbpm.standarpositionChangeEvent    标准岗位变动事件
hbpm.positionChangeEvent           岗位变动事件
hbjm_jobhr.change                  职位变动·生效
```

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/org_dev_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：2 个本场景实体 · 共 18 处引用 · 其中 11 处跨云。

### `haos_structproject` （跨云引用 11 处）

#### ⬇️ HR 基础服务云（`hr_hrmp`）11 处

| form | field | type |
|---|---|---|
| `hrcs_admingrouporg` | `struct` | BasedataField |
| `hrcs_dynaschdimgrp` | `structproject` | BasedataField |
| `hrcs_dynaschexdimgrp` | `structproject` | BasedataField |
| `hrcs_perminitrecord` | `dim_structproject` | BasedataField |
| `hrcs_perminitrecord` | `rdata_structproject` | BasedataField |
| `hrcs_roledimgrp` | `structproject` | BasedataField |
| `hrcs_roleexdimgrp` | `structproject` | BasedataField |
| `hrcs_userroledimgrp` | `structproject` | BasedataField |
| `hrcs_userroleexdimgrp` | `structproject` | BasedataField |
| `hrptc_rptalocperm` | `adminorgstruct` | BasedataField |
| `hrptc_userperm` | `adminorgstruct` | BasedataField |

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
