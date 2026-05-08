# hbss_person_attrs · 异常处理

> **聚合场景**：hbss_person_attrs · 包含 15 个 hbss 字典实体（HR 基础服务云 · 人员基础属性字典聚合场景。包含员工档案/核心人力云中频繁引用的基础字典实体：性别、国籍、民族、血型、星座、婚姻状态、政治面貌（本场景均有）...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

HR 基础服务云 · 人员基础属性字典聚合场景。包含员工档案/核心人力云中频繁引用的基础字典实体：性别、国籍、民族、血型、星座、婚姻状态、政治面貌（本场景均有）、健康状态、宗教信仰等个人属性字典。这些实体结构简单（number/name/status + 少量业务字段），均采用 HRBaseDataTplEdit 标准基础资料模板，ISV 可用 @SdkPlugin 并列挂扩展，禁止继承 HRBa

## 涉及实体（15 个）

- `hbss_sex`
- `hbss_nationality`
- `hbss_bloodtype`
- `hbss_constellation`
- `hbss_zodiac`
- `hbss_marriagestatus`
- `hbss_healthstatus`
- `hbss_religion`
- `hbss_party`
- `hbss_politicalstatus`
- `hbss_flok`
- `hbss_familiarity`
- `hbss_familymemberrel`
- `hbss_emergcontactype`
- `hbss_addresstype`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：高——性别/国籍/民族被核心人力云(hrpi)和薪酬云大量引用，修改前必查 _org_entity_ref_report.json
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的异常处理章节（15 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
