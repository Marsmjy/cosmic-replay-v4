# core_hr_org_unit_transfer · 数据模型

> 涉及 11 个标品 form + 1 个 ISV 自建 form·业务流型聚合场景的数据模型全景

## 模型分层全景

```
┌─────────────────────────────────────────────────────────────┐
│ 触发层 · 调度计划                                              │
│   schedule_hdm_orgtransfer_plan_SKDP_S (3 任务定时跑)         │
└──────────────────────────┬──────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 数据源层 · 标品消息表                                          │
│   homs_orgchgrecord     (A/B 数据源·组织变更记录)             │
│   hbpm_chgrecordevt     (C 数据源·岗位变更事件)                │
│   hbpm_chgrecorddetail  (C 子表·岗位变更明细)                  │
└──────────────────────────┬──────────────────────────────────┘
                           ↓ 过滤变动场景编码 (1020_S/1030_S/1080_S)
┌─────────────────────────────────────────────────────────────┐
│ 中间表层 · ISV 自建                                            │
│   ${ISV_FLAG}_orguntitransfer  (成建制划转待处理表)            │
│     字段：${ISV_FLAG}_changetype / executestatus / takeeffectdate
│           / orgid / orgnumber / orgname / resultmsg / _tag    │
└──────────────────────────┬──────────────────────────────────┘
                           ↓ executestatus=01 拉取
┌─────────────────────────────────────────────────────────────┐
│ 元数据查询层 · 标品                                            │
│   haos_adminorgdetail   (行政组织当前详情·HisModel)           │
│   hbpm_positionhr       (岗位历史版本)                         │
│   hbss_* (基础资料字典)                                        │
└──────────────────────────┬──────────────────────────────────┘
                           ↓ 拉历史版本 (iscurrentversion!=1)
┌─────────────────────────────────────────────────────────────┐
│ 业务层 · 任职经历                                              │
│   hrpi_empposorgrel  (任职经历·6 处加载)                       │
│   hrpi_assignment    (派工)                                   │
│   hrpi_empentrel     (雇佣关系)                                │
└──────────────────────────┬──────────────────────────────────┘
                           ↓ 装配批量调动单
┌─────────────────────────────────────────────────────────────┐
│ 目标产出层 · 标品调动单                                        │
│   hdm_transferbatch       (调动单主表)                        │
│   hdm_transferbatchentry  (调动单分录·按人员一行)              │
└──────────────────────────┬──────────────────────────────────┘
                           ↓ executeOperate(SUBMITEFFECT)
┌─────────────────────────────────────────────────────────────┐
│ 标品自动协同 (ISV 不私改)                                      │
│   hrpi_* 任职经历底表更新                                      │
│   hpfs_chgrecord 变动记录                                      │
│   跨云事件 (BEC) 自动触发·薪酬/考勤跟随                        │
└─────────────────────────────────────────────────────────────┘
```

## 实体清单（按层）

### 1. ISV 自建 (1)

| form | 关键字段 | 用途 |
|---|---|---|
| `${ISV_FLAG}_orguntitransfer` | `${ISV_FLAG}_changetype` / `_executestatus` / `_takeeffectdate` / `_orgid` / `_orgnumber` / `_orgname` / `_resultmsg` / `_resultmsg_tag` / `_modifydate` / `_mapping_id` | 成建制划转中间表·维护待处理任务 + 执行状态机 |

### 2. 标品·消息表 (3)

| form | 应用 | 调用方 | 字段 |
|---|---|---|---|
| `homs_orgchgrecord` | 组织运营服务云 | A/B 任务 | `adminorg.id` / `orgchgentry.changescene.number` / `orgchgentry.chgeffecttime` / `orgchgentry.operationtime` |
| `hbpm_chgrecordevt` | HR 业务平台 | C 任务 | `changescene.number` (主表过滤) |
| `hbpm_chgrecorddetail` | HR 业务平台 | C 子表 | 岗位更名前后名称 |

### 3. 标品·元数据/字典 (3)

| form | 用途 | 时态 |
|---|---|---|
| `haos_adminorgdetail` | 行政组织详情 | ✅ HisModel |
| `hbpm_positionhr` | 岗位历史 | ✅ HisModel |
| `haos_changescene` | 变动场景编码定义 (1020_S/1030_S/1080_S 等) | 普通基础资料 |

### 4. 标品·任职经历 (3)

| form | 关键字段 | 用途 |
|---|---|---|
| `hrpi_empposorgrel` | `adminorg` / `adminorgvid` / `position` / `positionvid` / `bsed` / `bsled` / `iscurrentversion` | 任职经历·HR 核心人力 |
| `hrpi_assignment` | `id` / 关联 empposorgrel.assignment.id | 派工 |
| `hrpi_empentrel` | `enterprise` / `entrydate` / `laborrelstatus` / `laborreltype` | 雇佣关系（按 employee.id 反查） |

### 5. 标品·调动单 (2)

| form | 关键字段 | 用途 |
|---|---|---|
| `hdm_transferbatch` | `billstatus` / `auditstatus` / `org` / `affiliationord_id` / `creator_id` / `entryentity` (子分录) | 调动单主表·SAVE/SUBMITEFFECT op |
| `hdm_transferbatchentry` | `seq` / `ebillno` / `bb_e_*` (员工信息前缀) / `bb_a_*` / `bb_po_*` / `b_effectivedate` / `entryvalidateresult` | 调动单分录·按人员一行 |

## 字段命名规则（hdm_transferbatchentry 实证）

苍穹 hdm 调动单的分录字段命名采用**前缀法**·区分"前/后"和"维度"：

| 前缀 | 含义 | 例子 |
|---|---|---|
| `b_` / `bb_` | before (调前) | `bb_e_enterprise` (调前企业) / `bb_e_entrydate` (调前入职日) |
| `a_` / `ba_` | after (调后) | `ba_a_org` (调后人事管理组织) |
| `_e_` | employee 维度 | `bb_e_laborrelstatus` (调前用工身份) |
| `_a_` | assignment 维度 | `ba_a_org` |
| `_po_` | position 维度 | `bb_po_orgrelseq` (调前任职序号) / `bb_po_adminorgbo` (调前主机构 boId) |
| `_man_` | manage 维度 | `bb_a_manageadminorg` (调前人事组织) |

详细字段映射见 deep_scan_audit.md G 段。

## 跨云引用（ADR-009 跨云穿透）

| 来源云 | 来源 form | 引用本场景的字段 | 说明 |
|---|---|---|---|
| HR 基础设施云 (hr_hrmp) | `haos_changescene` | `${ISV_FLAG}_changetype` 间接关联 | 编码定义 |
| 组织发展云 (org_dev) | `homs_orgchgrecord` / `haos_adminorgdetail` / `hbpm_positionhr` | 数据源 / 元数据 | A/B/C 共用 |
| 核心人力云 (core_hr) | `hrpi_*` 系列 | 被刷新（标品自动） | 任职经历 |
| 核心人力云 (core_hr) | `hdm_transferbatch` 系列 | 目标产出 | 调动单 |

## ISV 元数据归属（4 铁律）

| 字段 / form | 归属 | 不能怎么动 |
|---|---|---|
| `${ISV_FLAG}_orguntitransfer` 全部字段 | ISV 自建 | ✅ 完全自主·可加可删可改 |
| `homs_orgchgrecord` / `hbpm_chgrecordevt` 等标品 form | 平台标品 | ❌ ISV 不允许 modifyMeta add/remove field |
| `hdm_transferbatch.entryentity` 字段 | 平台标品 | ⚠️ 加字段必走 ISV 扩展元数据 (CS-01)·不能直接改原 dym |
| `hrpi_empposorgrel` | 标品（核心人力） | ❌ ISV 不允许私改·完全交给标品 op |

详见 [`_metadata_rules_form.json`](_metadata_rules_form.json) (待生成 · Tier 1 必备)

## 模型扩展点

5 个常见扩展点（详见 [`_assets/org_unit_transfer/customization_points.md`](../../_assets/org_unit_transfer/customization_points.md)）：

- **EP-01** 改变动场景编码集（如加 `ZZCJ06`）
- **EP-03** 加 ISV 中间表自定义字段（如审批意见 `${ISV_FLAG}_approval_note`）
- **EP-04** 添加新 changeType (D/E)·新建任务类
- **EP-06** 改数据源（用客户自建消息表替代 `homs_orgchgrecord`）
