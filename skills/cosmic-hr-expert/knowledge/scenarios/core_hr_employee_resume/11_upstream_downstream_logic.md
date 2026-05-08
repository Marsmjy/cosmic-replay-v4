# core_hr_employee_resume · 上下游逻辑

> **聚合场景**：core_hr_employee_resume · 包含 12 个 hbss 字典实体（员工履历类附表聚合 · 12 hrpi 实体 · 多为时间轴/历史版本附表 · ISV 扩展走 @SdkPlugin 并列挂 HRTemplateBillEdi...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

员工履历类附表聚合 · 12 hrpi 实体 · 多为时间轴/历史版本附表 · ISV 扩展走 @SdkPlugin 并列挂 HRTemplateBillEdit · 禁继承 HisModelOPCommonPlugin / HisModelFormCommonPlugin / TimelineTplOp

## 涉及实体（12 个）

- `hrpi_pereduexp`
- `hrpi_preworkexp`
- `hrpi_empproexp`
- `hrpi_perlgability`
- `hrpi_perocpqual`
- `hrpi_perpractqual`
- `hrpi_perprotitle`
- `hrpi_emptrainfile`
- `hrpi_rsmpatinv`
- `hrpi_rsmproskl`
- `hrpi_empstage`
- `hrpi_emptutor`

## 标准模式

- **插件模式**：HRTemplateBillEdit / HRBaseDataTplEdit · ISV @SdkPlugin 并列挂 · 禁继承 HisModel*
- **跨云影响**：中——多数实体在核心人力域内 · 部分（peraddress/familymemb 等）被薪酬/福利云引用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的上下游逻辑章节（12 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json` · 更新时间 2026-04-29
> 本 form（`core_hr_employee_resume`，所属 other）引用了其他云的 **27** 个底座实体：

### ⬆️ HR 基础服务云（`hr_hrmp`）27 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `education` | 学历 | BasedataField | `hbss_diploma` | [hbss_edu_train](../hbss_edu_train/) |
| `edunature` | 学历性质 | BasedataField | `hbss_diplomatype` | [hbss_edu_train](../hbss_edu_train/) |
| `degree` | 学位 | BasedataField | `hbss_degree` | [hbss_edu_train](../hbss_edu_train/) |
| `graduateschool` | 毕业院校 | HisModelBasedataField | `hbss_college` | [hbss_edu_train](../hbss_edu_train/) |
| `certtype` | 证书类型 | BasedataField | `hbss_educerttype` | [hbss_edu_train](../hbss_edu_train/) |
| `trade` | 所属行业 | BasedataField | `hbss_industrytype` | [hbss_position_dict](../hbss_position_dict/) |
| `businesstypeid` | 企业性质 | BasedataField | `hbss_empnature` | [hbss_labor_rel](../hbss_labor_rel/) |
| `companyscale` | 公司规模 | BasedataField | `hbss_companyscale` | [hbss_position_dict](../hbss_position_dict/) |
| `projecttype` | 项目类型 | BasedataField | `hbss_projecttype` | [hbss_position_dict](../hbss_position_dict/) |
| `companynature` | 企业性质 | BasedataField | `hbss_empnature` | [hbss_labor_rel](../hbss_labor_rel/) |
| `industry` | 所属行业 | BasedataField | `hbss_industrytype` | [hbss_position_dict](../hbss_position_dict/) |
| `language` | 语言种类 | BasedataField | `hbss_languagetype` | [hbss_edu_train](../hbss_edu_train/) |
| `languagecert` | 语言证书 | BasedataField | `hbss_languagecert` | [hbss_edu_train](../hbss_edu_train/) |
| `listen` | 听 | BasedataField | `hbss_familiarity` | [hbss_capability](../hbss_capability/) |
| `speak` | 说 | BasedataField | `hbss_familiarity` | [hbss_capability](../hbss_capability/) |
| `read` | 读 | BasedataField | `hbss_familiarity` | [hbss_capability](../hbss_capability/) |
| `write` | 写 | BasedataField | `hbss_familiarity` | [hbss_capability](../hbss_capability/) |
| `qualification` | 职业资格 | BasedataField | `hbss_ocpqual` | [hbss_edu_train](../hbss_edu_train/) |
| `qualevel` | 职业资格等级 | BasedataField | `hbss_ocpquallevel` | [hbss_edu_train](../hbss_edu_train/) |
| `qualification` | 执业资格 | BasedataField | `hbss_operationqual` | [hbss_edu_train](../hbss_edu_train/) |
| `professional` | 职称 | BasedataField | `hbss_protitle` | [hbss_edu_train](../hbss_edu_train/) |
| `prolevel` | 职称级别 | BasedataField | `hbss_protitlelevel` | [hbss_edu_train](../hbss_edu_train/) |
| `traintype` | 培训类别 | BasedataField | `hbss_traintype` | [hbss_edu_train](../hbss_edu_train/) |
| `trainmode` | 培训方式 | BasedataField | `hbss_trainmode` | [hbss_edu_train](../hbss_edu_train/) |
| `patentcategoryid` | 专利类别 | BasedataField | `hbss_patentscategory` | [hbss_edu_train](../hbss_edu_train/) |
| `patentstatusid` | 专利状态 | BasedataField | `hbss_patentstatus` | [hbss_edu_train](../hbss_edu_train/) |
| `familiarityid` | 掌握程度 | BasedataField | `hbss_familiarity` | [hbss_capability](../hbss_capability/) |

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

> 自动生成 · 数据源 `_cross_cloud_reports/core_hr_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：1 个本场景实体 · 共 13 处引用 · 其中 0 处跨云。

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
