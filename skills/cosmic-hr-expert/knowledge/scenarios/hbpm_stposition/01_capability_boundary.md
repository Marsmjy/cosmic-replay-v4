# 能力边界 · 标准岗位维护（hbpm_stposition）

> **状态**: 🟢 verified · 基于 6 反编译类 + 37 插件 plugin_registry + scene_doc.json 字段实抓
> **confidence**: verified
> **审计时间**: 2026-04-27
> **场景模型**: HisModel 历史时序资料（继承 hbp_histimeseqtpl）

---

## 0. 场景定性：HisModel 时序资料（重要！）

本场景是苍穹 HR **最复杂的基础资料场景之一**，原因：

1. **HisModel 时序模型**：每条标准岗位有多个历史版本（boid 不变 · id 随版本变），查询必须用 `iscurrentversion=true` 过滤当前版本
2. **与 hbpm_positionhr 共用物理表** `t_hbpm_position`：区分键为 `isstandardpos`（=1 表示标准岗位）
3. **37 个插件**：27 个 HR 域插件（含 HisModel 系列 9 个）+ 10 个平台插件，是迄今标准岗位插件最多的场景
4. **变更操作走 change 链路**：修改标准岗位走 `change` 操作，产生新版本，旧版本 iscurrentversion 变 false

---

## 1. 本场景能做什么（已覆盖能力）

### 1.1 CRUD（标准岗位生命周期）

| 操作 | 操作 Key | 实现插件 | 说明 |
|---|---|---|---|
| 新增标准岗位 | `save` | StandardPositionSaveOp + CodeRuleOp | 自动分配编码 · 设 isstandardpos=1 |
| 查看 / 编辑 | `modify` | StandardPositionEdit | 状态为禁用（enable=10）时显示 VIEW 状态 |
| 变更（生效日期版本） | `change` / `confirmchange` | StandardPositionChangeOp + HisModelOPCommonPlugin | 产生新历史版本 · 旧版本 iscurrentversion=false |
| 禁用 | `disable` | StandardPositionDisableOp | onAddValidators 注册校验 |
| 启用 | `enable` | StandardPositionEnableOp | 无额外校验 |
| 删除 | `delete` | HisModelOPCommonPlugin + HisUniqueValidateOp | 时序版本安全删除 |
| 审核（审批流） | `audit` | StandardPositionAuditOp | extends StandardPositionMsgHandleOp |

### 1.2 历史时序能力

| 能力 | 实现 | 说明 |
|---|---|---|
| 查看历史版本 | HisModelListCommonPlugin | 列表可查全版本历史 |
| 版本比较 | `versionchangecompare` | 两个版本字段逐一对比 |
| 历史记录列表 | `hisversionbtn` | 当前岗位所有版本按时间线排列 |
| F7 选岗位（跨场景） | HisModelF7ListPlugin + HisBaseDataF7FastFilter | 外部场景 F7 选标准岗位返回当前版本 boid |
| 时间线操作 | HisLineTimeTplOp | 时间线模板 OP |

### 1.3 编码规则

- `CodeRulePlugin` + `CodeRuleOp` 绑定自动生成标准岗位编码（number 字段）
- 标品使用 `CodeRuleServiceHelper.readNumber("hbpm_stposition", ...)` 按组织维度生成编码
- 新增操作 `pageRelease` 时若未保存会自动回收编码（StandardPositionEdit.recycleNumber）

### 1.4 关联字段联动

- **adminorg → org**：选择行政组织时自动填充关联的组织体系管理组织（org）
- **job（职位）→ joblevelscm / jobgradescm**：职位字段变化联动职级方案 / 职等方案范围（JobLevelGradeRangeUtil）
- **编码重新生成**：org / adminorg / job 字段变化时若编码规则存在则重新生成 number

---

## 2. 不覆盖（已知限制）

| 限制 | 说明 |
|---|---|
| boid / iscurrentversion 不可手工设置 | 由 HisModelOPCommonPlugin 框架维护 · ISV 代码不得 entity.set("boid", ...) |
| 无 listRules（业务规则 0 条）| 全部校验逻辑在操作插件 onAddValidators 里 · 没有走 formRule |
| 历史版本不可从本场景直接删除某版本 | 历史数据管理有专属操作 · 非标准 delete |
| 变更审批 confirmchange vs confirmchangenoaudit | 是否走审批由 HRBaseDataConfigUtil.getAudit("hbpm_stposition") 配置决定 |
| isstandardpos 标准岗位共用物理表 | hbpm_positionhr（非标准岗位）也在 `t_hbpm_position`；改本表结构会双场景影响 |

---

## 3. 进入路径

| 路径 | 说明 |
|---|---|
| 菜单 → "岗位维护 → 标准岗位维护" | menuId=1493506395784391680 |
| 其他场景 F7 选标准岗位 | HisModelF7ListPlugin / HisBaseDataF7FastFilter / PositionBaseDataF7FastFilter 接管 |
| hbpm_positionhr 引用 stposition | 岗位信息里引用标准岗位 boid |

---

## 4. 插件覆盖总览

| 层次 | 插件数量 | 核心 |
|---|---|---|
| 平台级 | 10 | CodeRulePlugin / BdVersionListPlugin 等 |
| HisModel 通用 | 9 | HisModelFormCommonPlugin / HisModelListCommonPlugin / HisModelF7ListPlugin / HisModelFilterPanelListPlugin / HisModelFilterPanelF7ListPlugin / HisModelMobileListPlugin / HisModelOPCommonPlugin / HisUniqueValidateOp / HisLineTimeTplOp |
| HR 基础资料通用 | 10 | HRBaseDataTplEdit / HRBaseDataImportEdit / HRHiesButtonSwitchPlugin / HRBaseDataTplList / HRBasedataLogList / HRBaseDataStatusOp / HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp / HRBaseDataConfigUtil |
| hbpm 场景专属 | 8 | StandardPositionEdit / StandardPositionListPlugin / StandardPositionSaveOp / StandardPositionDisableOp / StandardPositionEnableOp / StandardPositionChangeOp / StandardPositionAuditOp / StandardPositionMsgHandleOp |
| 快速筛选控制 | 2 | HisBaseDataF7FastFilter / PositionBaseDataF7FastFilter |

---

## 5. 关联文档

- `02_business_rules.md` · 隐性业务规则（来自插件 onAddValidators）
- `03_model_design.md` · 字段完整表 · 时序字段说明 · PR-008/009
- `07_ext_points.md` · HisModel 禁继承清单（11 个类）
- `11_upstream_downstream_logic.md` · hbpm_positionhr 下游引用 + BEC 分析
