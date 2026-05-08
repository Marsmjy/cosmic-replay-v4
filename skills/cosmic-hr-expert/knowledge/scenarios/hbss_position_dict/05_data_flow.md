# hbss_position_dict · 数据流

> **聚合场景**：hbss_position_dict · 包含 15 个 hbss 字典实体（HR 基础服务云 · 职位/职务基础字典聚合场景。包含：职类(hbss_postype)、岗位分类(hbss_postcategory)、岗位状态(hbss_p...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

HR 基础服务云 · 职位/职务基础字典聚合场景。包含：职类(hbss_postype)、岗位分类(hbss_postcategory)、岗位状态(hbss_poststate)、岗位状态分类(hbss_poststatecls)、费用中心(hbss_costcenter)、业务范围(hbss_bussinessfield/hbss_hrbusinessfield)、行业类型(hbss_indus

## 涉及实体（15 个）

- `hbss_postype`
- `hbss_postcategory`
- `hbss_poststate`
- `hbss_poststatecls`
- `hbss_costcenter`
- `hbss_bussinessfield`
- `hbss_hrbusinessfield`
- `hbss_industrytype`
- `hbss_companyscale`
- `hbss_cadrecategory`
- `hbss_projecttype`
- `hbss_rotationtype`
- `hbss_disptype`
- `hbss_perflevel`
- `hbss_depcytype`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：中——业务范围/行业类型被核心人力云和薪酬云引用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的数据流章节（15 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
