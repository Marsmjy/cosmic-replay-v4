# hbss_hr_config · 能力边界

> **聚合场景**：hbss_hr_config · 包含 18 个 hbss 字典实体（HR 基础服务云 · HR 系统配置与安全场景。包含：安全 URI(hbss_safeuri)、安全 URI 配置(hbss_safeuriconfig)、登录...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

HR 基础服务云 · HR 系统配置与安全场景。包含：安全 URI(hbss_safeuri)、安全 URI 配置(hbss_safeuriconfig)、登录配置(hbss_loginconfig)、登录场景(hbss_loginscene)、HR 云应用入口配置(hbss_appentryconfig)、隐私用户类型(hbss_privacyusertype)、隐私签署(hbss_privac

## 涉及实体（18 个）

- `hbss_safeuri`
- `hbss_safeuriconfig`
- `hbss_loginconfig`
- `hbss_loginscene`
- `hbss_appentryconfig`
- `hbss_privacyusertype`
- `hbss_privacysigning`
- `hbss_basedatalist`
- `hbss_rolesourcetype`
- `hbss_timestamp`
- `hbss_relatepanelset`
- `hbss_cloud`
- `hbss_cloud_app`
- `hbss_actiontype`
- `hbss_action`
- `hbss_hrbuviewquery`
- `hbss_hrbuquery`
- `hbss_hrbu_hitfrequency`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：低——配置类实体跨云引用较少，主要供平台层使用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的能力边界章节（18 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
