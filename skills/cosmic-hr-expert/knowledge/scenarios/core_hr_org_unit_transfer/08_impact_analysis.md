# core_hr_org_unit_transfer · 影响分析

## 引入资产对客户环境的影响

| 影响层 | 项 | 程度 |
|---|---|---|
| 元数据 | 新增 `${ISV_FLAG}_orguntitransfer` 表（ISV 自建） | 低·不影响标品 |
| 元数据 | 新增 ISV 应用包 `${BIZ_APP}` | 低·扩展 hdm 调配管理 |
| 调度 | 新增调度计划 `hdm_orgtransfer_plan_SKDP_S`·3 子任务每日跑 | 中·占调度槽位 |
| 业务表 | 高频写入 `hdm_transferbatch / _entry`（标品调动单） | 中·按变更频率 |
| 任职经历 | 通过标品 `submiteffect` 自动协同 `hrpi_*` | 中·标品自处理 |
| 跨云 | 标品 `hpfs_chgrecord.aftereffect` 触发 BEC·薪酬考勤跟随 | 中·标品自处理 |

## 性能影响评估

| 数量级 | 单次调度耗时（估算） | 备注 |
|:---:|:---:|---|
| 100 人/天变更 | ~10 秒 | 单一调度 |
| 1000 人/天变更 | ~60 秒 | 标品 submiteffect 是瓶颈 |
| 10000 人/天变更 | ~600 秒 | 建议 `EXECUTE_DAYS=1` 不要拉大 |

**优化策略**：
- 任务并行：A/B/C 三任务**已独立调度**·可以同时跑
- 数据预处理：标品消息表索引·`changescene.number` 命中查询
- 失败隔离：单条失败不影响其他记录·状态机驱动重试

## 跨云影响（ADR-009 跨云穿透）

详见 03_model_design.md 跨云引用表。本场景对 5 朵云的影响：

| 云 | 影响形式 |
|---|---|
| core_hr | 直接写 `hdm_transferbatch` + `hrpi_*` 联动 |
| org_dev | 读 `homs_orgchgrecord` / `haos_adminorgdetail` / `hbpm_positionhr` |
| hr_hrmp | 读 `haos_changescene` 编码字典 |
| payroll | **间接**：标品 BEC 触发·薪酬重算 |
| attendance | **间接**：标品 BEC 触发·考勤档案重算 |

## 升级风险

- ⚠️ 苍穹 hdm 标品升级 → `hdm_transferbatch.SUBMITEFFECT` op 行为变化 → 本资产可能失效·必须 case_001 类回归
- ⚠️ 苍穹 homs/hbpm 消息表 schema 变化 → 本资产查询失败·必须改 query 字段列表
- ⚠️ ISV `${ISV_FLAG}_orguntitransfer` 加字段 → load 字段列表漏改 = 隐蔽 bug（详见 CS-03 踩坑）

## 反哺影响（如有 case 升级本场景）

每次升级走 dcs-case-run K2 v2.1 全 8 步流程·重点关注：
- D.4 双侧代码片段对照（vs 现有版本）
- D.5 禁词扫描 + ISV 占位符 lint
- baseline_diff = 0 退化
