# core_hr_org_unit_transfer · 上下游逻辑

> 协议：ADR-009 跨云穿透架构

## 上游（本场景的数据源）

| 上游来源 | 内容 | 触发本场景何时 |
|---|---|---|
| `homs_orgchgrecord` | 组织变更记录·`changescene.number=1020_S/1030_S` | A/B 任务·每日调度 |
| `hbpm_chgrecordevt` + `hbpm_chgrecorddetail` | 岗位变更事件 + 明细·`changescene.number=1020_S/1080_S` | C 任务·每日调度 |
| `haos_changescene` | 变动场景编码字典·定义 1020_S/1030_S/1080_S 等 | 配置·业务定义 |
| `haos_adminorgdetail` | 行政组织详情·HisModel | A/B 任务查历史组织 |
| `hbpm_positionhr` | 岗位历史版本 | C 任务查历史岗位 |
| 标品 hpfs（人事档案服务）| 触发 `homs_orgchgrecord` 写入 | 上游模块自身行为 |

## 下游（本场景的影响范围）

### 直接下游（标品 op 自动协同）

| 下游 form | 影响内容 | 触发机制 |
|---|---|---|
| `hdm_transferbatch` / `hdm_transferbatchentry` | **本场景直接 SAVE + SUBMITEFFECT** | 业务流主输出 |
| `hrpi_empposorgrel` | 任职经历底表·历史版本 vid 字段 | 本场景刷新（`changeEmpPosOrgRelHisDataBy*`）|
| `hrpi_empposorgrel` | 任职经历底表·SUBMITEFFECT 后协同 | 标品自动·ISV 不写 |
| `hpfs_chgrecord` | 变动记录 | 标品 SUBMITEFFECT 触发 |

### 间接下游（标品 BEC 触发跨云）

```
hdm_transferbatch.SUBMITEFFECT
  ↓ 标品自动事件
hpfs_chgrecord.aftereffect
  ↓ BEC 订阅 (HR 业务事件中心)
  ├──> swc/hsas (薪酬云)：自动重算工资
  ├──> wtc/wtbd (考勤云)：重算考勤档案
  ├──> hbjm (福利云)：调整福利计算
  └──> hcdm (薪资标准云)：联动薪资标准
```

详细 BEC 订阅清单：跑 `cosmic-bec-probe 画像 --event hpfs_chgrecord.aftereffect` 拿真实订阅。

## 跨云引用反向报告

引用方（消费方）的视角·谁会**因为本场景的输出而联动**：

| 消费方场景 | 消费内容 | 消费位置 |
|---|---|---|
| `core_hr_transfer` | 本场景产出的 `hdm_transferbatch` 单据 | 列表 / 详情 |
| `core_hr_audit` | 调动单审计 | 跨场景反查 |
| `core_hr_employee` | 员工档案任职变化 | 显示 |
| `core_hr_apply_emp` | 雇员变动单据联动 | 跨单据关联 |
| 薪酬业务场景全 | 本场景触发的 chgrecord 联动 | BEC 订阅 |
| 考勤业务场景全 | 同上 | 同上 |

## 反向引用知识库（编辑前必查）

按 v3 spec ADR-009 第 4 层 LLM 铁律·写本场景代码或元数据前·必查：

```python
# 用 cosmic-mcp recommend_solutions 拿引用方
# 或 grep _scene_relations.json
grep -A2 '"core_hr_org_unit_transfer"' knowledge/_scene_relations.json
```

## 上下游变更影响

| 变更类型 | 影响范围 |
|---|---|
| 改本场景调度逻辑 | 不影响标品 form·只影响 `${ISV_FLAG}_orguntitransfer` 状态机 |
| 改本场景中间表字段 | 影响 ISV 自身·不影响标品 |
| 加新 changeType（如 D） | 影响调度配置 + 中间表·**不影响**标品 |
| 改数据源 form 名（CS-05）| **必须保证**新 form 的字段结构与原 form 兼容 |
| 标品 hdm_transferbatch.SUBMITEFFECT 升级 | **必须**重新跑 case_001 类回归 |

## 关联资产

- 资产复刻：[`_assets/org_unit_transfer/`](../../_assets/org_unit_transfer/)
- 资产部署 SOP：[`_assets/org_unit_transfer/deploy_sop.md`](../../_assets/org_unit_transfer/deploy_sop.md)
- 扩展点：[`_assets/org_unit_transfer/customization_points.md`](../../_assets/org_unit_transfer/customization_points.md)
