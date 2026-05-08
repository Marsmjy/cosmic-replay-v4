# 上下游联动 · core_hr_blacklist

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
> 本 form（`hrpi_blacklist`，所属 核心人力云）引用了其他云的 **11** 个底座实体：

### ⬆️ 考勤云（`attendance`）1 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `quitreasonid` | 离职原因 | BasedataField | `hpfs_chgreason` | — |

### ⬆️ HR 基础服务云（`hr_hrmp`）6 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `nation` | 国籍 | BasedataField | `hbss_nationality` | [hbss_person_attrs](../hbss_person_attrs/) |
| `gender` | 性别 | BasedataField | `hbss_sex` | [hbss_person_attrs](../hbss_person_attrs/) |
| `emptype` | 用工类型 | BasedataField | `hbss_laborreltype` | [hbss_labor_rel](../hbss_labor_rel/) |
| `maincardtype` | 主证件类型 | BasedataField | `hbss_credentialstype` | [hbss_edu_train](../hbss_edu_train/) |
| `cardtypeimport` | 证件类型 | BasedataField | `hbss_credentialstype` | [hbss_edu_train](../hbss_edu_train/) |
| `cardtype` | 证件类型 | BasedataField | `hbss_credentialstype` | [hbss_edu_train](../hbss_edu_train/) |

### ⬆️ 组织发展云（`org_dev`）4 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `adminororg` | 黑名单管理组织 | HRAdminOrgField | `haos_adminorghrf7` | — |
| `position` | 原就任岗位 | HRPositionField | `hbpm_positionhrf7` | — |
| `job` | 原就任职位 | BasedataField | `hbjm_jobhr` | [hjm_jobhr_maintenance](../hjm_jobhr_maintenance/) |
| `adminorg` | 原就任部门 | HRAdminOrgField | `haos_adminorghrf7` | — |

> ⚠️ ISV 扩展须知（ADR-009）：
> - 上游底座实体是**标品字典**，原则上不可改字段（参各上游场景的 06_customization_solutions.md）
> - 引用方式（fieldType / refEntity）由本 form 元数据控制；本 form 改 ref 字段值用 `setValue` 即可
> - 修改前必须读对应上游场景的 11_upstream_downstream_logic.md，确认上游 ISV 扩展规则

<!-- END cross-cloud-upstream -->

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 03 + 04 沉淀）

> 本场景的业务语义补充见以下沉淀文档：
> - [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md) · 总论 · 金字塔决策方法论 + 11 大特殊解决方案 + L0-L5 业务模型分层
> - [PPT03_DEEP_TRACE.md](../../docs/PPT03_DEEP_TRACE.md) · 核心人力 + 人员信息 · 33 实体清单 + 6 大可继承模板 + 8 SDK 扩展点 + 30 OpenAPI
> - [PPT04_DEEP_TRACE.md](../../docs/CHGACTION_DEEP_TRACE.md) · chgaction 机制 22 段 · 反编译 + PPT 双源印证
> - [PPT05_DEEP_TRACE.md](../../docs/PPT05_DEEP_TRACE.md) · 劳动合同（hlcm 第三波预用知识）

### 关键 SDK 抓手（按本场景常用情况）

```java
// hrpi 数据访问（核心人力 33 场景共用）
HRPIEmployeeServiceHelper      员工信息处理
HSPMFileServiceHelper          人员档案处理
HSPMBusinessDataServiceHelper  HSPM 自定义查询

// 通用插件继承点（ISV 高频选）
ERManFileCommonAttPlugin       人员档案附表通用插件 ⭐
MyFileCommonAttPlugin          员工端附表通用插件
ManFileFormMobileCommonPlugin  移动端附表通用插件

// 历史模型 / 时间轴
HisModelServiceHelper          HR 历史模型服务
TimelineServiceHelper          HR 时间轴模型服务
```

### 业务事件订阅点（跨云协作）

```
hpfs_chgrecord.aftereffect    ⭐ 跨云协作正确订阅点（不要订阅 effect · TX 内会回滚）
hrpi_employee.syncBosUser     HR 人员↔BOS 用户同步
```

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
