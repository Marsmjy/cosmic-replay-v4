# 上下游联动 · hbpm_positionhis

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（chgaction + Service 调用 + 跨云引用 · v2）
> **数据源**: 反编译 OP 类 · `_cross_cloud_index.json` · `cosmic_hr_chgaction_mechanism.md`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品流转链路

## 🟡 同 form 维度的下游联动（待人工补充）

<TODO 人工补> 本场景往下游传播什么数据：

| 下游模块 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| 薪酬云 (payroll) | `<TODO>` | | |
| 考勤云 (attendance) | `<TODO>` | | |
| 绩效云 (performance) | `<TODO>` | | |
| 福利云 (welfare) | `<TODO>` | | |

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json` · 更新时间 2026-04-29
> 本 form（`hbpm_positionhis`，所属 组织发展云）引用了其他云的 **2** 个底座实体：

### ⬆️ HR 基础服务云（`hr_hrmp`）2 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `diplomareq` | 最低学历要求 | BasedataField | `hbss_diploma` | [hbss_edu_train](../hbss_edu_train/) |
| `workplace` | 工作地 | BasedataField | `hbss_workplace` | [hbss_supplier](../hbss_supplier/) |

> ⚠️ ISV 扩展须知（ADR-009）：
> - 上游底座实体是**标品字典**，原则上不可改字段（参各上游场景的 06_customization_solutions.md）
> - 引用方式（fieldType / refEntity）由本 form 元数据控制；本 form 改 ref 字段值用 `setValue` 即可
> - 修改前必须读对应上游场景的 11_upstream_downstream_logic.md，确认上游 ISV 扩展规则

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

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
