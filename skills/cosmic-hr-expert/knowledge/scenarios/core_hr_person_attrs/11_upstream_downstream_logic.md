# core_hr_person_attrs · 上下游逻辑

> **聚合场景**：core_hr_person_attrs · 包含 11 个 hbss 字典实体（员工个人属性 + 家庭关系 + 证件 + 联系方式聚合 · 11 hrpi 实体 · 大多引用 hbss_person_attrs 的字典基础数据 · ISV ...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

员工个人属性 + 家庭关系 + 证件 + 联系方式聚合 · 11 hrpi 实体 · 大多引用 hbss_person_attrs 的字典基础数据 · ISV 扩展按 chgaction 配置驱动

## 涉及实体（11 个）

- `hrpi_partymember`
- `hrpi_fertilityinfo`
- `hrpi_perhobby`
- `hrpi_peraddress`
- `hrpi_percontact`
- `hrpi_perregion`
- `hrpi_percre`
- `hrpi_familymemb`
- `hrpi_emrgcontact`
- `hrpi_perfresult`
- `hrpi_perrprecord`

## 标准模式

- **插件模式**：HRTemplateBillEdit / HRBaseDataTplEdit · ISV @SdkPlugin 并列挂 · 禁继承 HisModel*
- **跨云影响**：中——多数实体在核心人力域内 · 部分（peraddress/familymemb 等）被薪酬/福利云引用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的上下游逻辑章节（11 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json` · 更新时间 2026-04-29
> 本 form（`core_hr_person_attrs`，所属 other）引用了其他云的 **16** 个底座实体：

### ⬆️ HR 基础服务云（`hr_hrmp`）16 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `procreatmode` | 生育方式 | BasedataField | `hbss_procreatmode` | [hbss_supplier](../hbss_supplier/) |
| `addresstype` | 地址类型 | BasedataField | `hbss_addresstype` | [hbss_person_attrs](../hbss_person_attrs/) |
| `politicalstatus` | 政治面貌 | BasedataField | `hbss_politicalstatus` | [hbss_person_attrs](../hbss_person_attrs/) |
| `party` | 所属党派 | BasedataField | `hbss_party` | [hbss_person_attrs](../hbss_person_attrs/) |
| `religion` | 宗教 | BasedataField | `hbss_religion` | [hbss_person_attrs](../hbss_person_attrs/) |
| `regresidencenature` | 户口性质 | BasedataField | `hbss_category` | [hbss_supplier](../hbss_supplier/) |
| `credentialstype` | 证件类型 | BasedataField | `hbss_credentialstype` | [hbss_edu_train](../hbss_edu_train/) |
| `nationality` | 国籍 | BasedataField | `hbss_nationality` | [hbss_person_attrs](../hbss_person_attrs/) |
| `gender` | 性别 | BasedataField | `hbss_sex` | [hbss_person_attrs](../hbss_person_attrs/) |
| `folk` | 民族 | BasedataField | `hbss_flok` | [hbss_person_attrs](../hbss_person_attrs/) |
| `familymembship` | 家庭成员关系 | BasedataField | `hbss_familymemberrel` | [hbss_person_attrs](../hbss_person_attrs/) |
| `emergcontactype` | 紧急联系人类型 | BasedataField | `hbss_emergcontactype` | [hbss_person_attrs](../hbss_person_attrs/) |
| `rulescore` | 评分分制 | BasedataField | `hbss_scoresystem` | [hbss_capability](../hbss_capability/) |
| `perflevel` | 绩效等级 | BasedataField | `hbss_perflevel` | [hbss_position_dict](../hbss_position_dict/) |
| `type` | 奖惩类别 | BasedataField | `hbss_rewpnmtype` | [hbss_capability](../hbss_capability/) |
| `level` | 奖惩级别 | BasedataField | `hbss_rewpnmlevel` | [hbss_capability](../hbss_capability/) |

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
