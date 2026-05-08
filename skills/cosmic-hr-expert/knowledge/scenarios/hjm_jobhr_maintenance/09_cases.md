# 参考案例 · 职位体系维护 (hbjm_jobhr)

> **状态**: 🟡 基于 `_auto_plugin_semantics.md` 实证反编译 + `scene_doc.json` 64 字段语义推导骨架
> 具体客户项目 GitLab 路径需从历史仓库搜索补充

---

## CASE-01 · 职位编码按序列前缀自动生成（典型定制）

### 业务背景

HR 项目集团要求：
- 管理序列（`jobseq = "MANAGE"`） → 编码前缀 `M`
- 研发序列 → 前缀 `R`
- 销售序列 → 前缀 `S`
- 制造序列 → 前缀 `C`

编码格式：`{前缀}{3 位序号}`，全局唯一。

### 技术方案

- **扩展对象**：`hbjm_jobhr`（主）
- **Pattern**：前置编码生成 + 让标品 `CodeRuleOp` 跳过自动生成
- **详见**：CS-05

### 扩展包产出（预期）

```
hrmp-kdtest_hbjm_ext/
  ├── src/kd/hrmp/hbjm/kdtest/JobHrSeqCodeOp.java     (自定义编码前置生成)
  └── META-INF/hbjm_jobhr_savep_ext.xml              (注册到 save opKey)
```

### 实测关键数据

- **目标 formId**：`hbjm_jobhr`
- **目标 opKey**：`save`（以及 `submitandnew` / `saveandnew` 相同流程）
- **标品插件起点**：save 链第 1 位 `kd.bos.business.plugin.CodeRuleOp`（`_auto_operations.md` L104）
- **注册要点**：`RowKey = 1`，排在 `CodeRuleOp` 之前

### 踩坑

- ❌ 注册 `RowKey > 2` → `CodeRuleOp` 已生成普通编码，再覆盖报唯一性冲突（`2=K9URZCEUUS` 编码唯一校验 enabled）
- ❌ 未用分布式锁 → 并发时两个请求同时拿到序号 "001"
- ❌ 忘记判断 `entity.isNew()` → 修改已有职位时误覆盖 number（违反 INV-08）
- ✅ 最终方案：前置生成 + Redis 分布式锁 + isNew 判断

### 可复用资产

- `JobHrSeqCodeOp.java` 模板
- `registerPlugin` 参数样例（`targetType = OPERATION`）

---

## CASE-02 · 选职位序列自动带出职级范围

### 业务背景

某零售集团 HR 反馈：填职位时经常填错职级范围（如"销售专员"填成 P8-P10 高职级），要求根据 `jobseq` 自动带出默认职级。

### 方案要点

- **扩展对象**：`hbjm_jobhr` 前端表单
- **Pattern**：`propertyChanged` 字段联动
- **详见**：CS-02

### 关键字段

| 触发字段 | 带出字段 | 带出字段（显示名缓存） |
|---|---|---|
| `jobseq` (HisModelBasedataField → `hbjm_jobseqhr`) | `lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade` | `lowjoblevelname` / `highjoblevelname` / `lowjobgradename` / `highjobgradename` |

### 踩坑

- ❌ 忘记同步 4 个 `*name` 字段 → 列表显示职级名和 F7 值不一致
- ❌ 查询 `jobseq` 序列配置时未传 `bsed` → 可能读到历史版本的默认值
- ❌ 用 `setValue` 前没判断用户是否手动改过（覆盖用户输入）

### 来源

`_auto_plugin_semantics.md` L29-L43 `JobHisBasedataFiledChangeEdit` 父类 `HRDataBaseEdit` 的 `afterBindData` / `beforeDoOperation` / `afterDoOperation` 实现模式是本案例的参照。

---

## CASE-03 · 禁用职位前检查关联岗位 / 员工（待补充真实项目）

### 业务背景

某制造企业 HR 反馈：曾经有管理员误禁用了"装配线主管"职位，导致 30+ 岗位和 200+ 员工档案出现"职位已禁用"告警，生产线管理混乱。

### 方案要点

- **扩展点**：`beforeExecuteOperationTransaction@disable`
- **实现**：继承 `JobHrDisableOp` + super 调用 + 关联数据统计
- **详见**：CS-04

### 扩展入口坐标（发布方 · hbjm_jobhr）

- 绑定表单：`hbjm_jobhr`
- 绑定操作：`disable`
- 推荐父类：`HRDataBaseOp`（同 `JobHrDisableOp`，`_auto_plugin_registry.md` L60）
- 关键重写方法：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs e)` — 先 `super.beforeExecuteOperationTransaction(e)` 走标品逻辑，再按 `boid` 查 `hbpm_position` / `hrpi_pemployee` 活跃引用数；>0 阻断禁用

**业务意图（发布方）**：在 `JobHrDisableOp` 之前注入业务引用检查，若有活跃引用，抛 `KDBizException` 或 `addErrorMessage` 阻止禁用。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `hbjm_jobhr`
2. 选择【操作】标签 → 找到 opKey = `disable`
3. 点击【扩展插件】→ 添加本自定义类，RowKey 设为 2（在 `BaseDataDisablePlugin` 之后、`JobHrDisableOp` 之前）
4. 保存 → 部署生效

### 踩坑

- ⚠️ 下游表的 `job` 字段关联的是 `id`（具体版本 ID）还是 `boid`（业务 ID）需要确认；时序模型通常用 `boid`
- ⚠️ 禁用批量（多个 DataEntity）时必须**逐个**校验，不要因为一个有引用就整批阻断
- ⚠️ `JobHrDisableOp` 本身没有标品业务校验（`_auto_operations.md` L216 disable 只 1 个 FormValidate），意味着标品接受"硬禁用"；业务要明确禁用 vs 停招的语义区分

### 待补充：真实项目 ID + 代码路径

---

## CASE-04 · 职位序列变更推送下游岗位模块（待补充真实项目）

### 业务背景

某跨国科技公司 HR 反馈：集团每年做一次职位体系方案升级（`jobscm` 变更），每次变更后需要通知岗位模块 `hbpm_position` 重新评估编制。

### 方案要点

- **扩展点**：`afterExecuteOperationTransaction@save` + `afterExecuteOperationTransaction@confirmchange`
- **实现**：事务后向消息总线发领域事件
- **详见**：CS-06

### 扩展入口坐标（发布方 · hbjm_jobhr）

- 绑定表单：`hbjm_jobhr`
- 绑定操作：`save`（含新建）、`confirmchange`（版本变更）
- 推荐父类：`HRDataBaseOp`（同 `JobHrMsgHandleOp`）
- 关键重写方法：
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — 事务提交后向事件总线 / MQ 发布 `JobChangedEvent`

**业务意图（发布方）**：职位保存 / 确认变更完成后，把变更信息（boid、hisversion、变更字段列表）打包成领域事件投递出去，让岗位模块独立订阅。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `hbjm_jobhr`
2. 选择【操作】标签 → 找到 opKey = `save`，添加插件
3. 同样添加到 opKey = `confirmchange`
4. 保存 → 部署生效

### 订阅方（岗位模块，独立服务）

**业务意图（订阅方）**：岗位模块订阅 `JobChangedEvent`，收到事件后按 `boid` 查 `hbpm_position.job.boid = ?`，对比职级 / 职等范围变化，生成"岗位编制重新审批"任务。

> ⚠️ 订阅方是独立服务（非 hbjm_jobhr 的操作扩展），一般注册到苍穹消息总线或自建 MQ。发布方必须在事务提交后（`afterExecuteOperationTransaction`）再发事件，避免主事务回滚时产生脏事件。

### 踩坑

- ⚠️ 时序变更会产生多次事件（`change` → `confirmchange` → 定时任务切换 `iscurrentversion`），下游要按 `jobId + hisversion` 幂等
- ⚠️ `changedFields` 计算要对比 `HRBaseOriginalOp` 写入的原始值（save 链第 7 位）

### 待补充：真实项目 ID + 代码路径

---

## CASE-05 · 职位表扩展"招聘状态"字段 + 联动看板（待补充真实项目）

### 业务背景

某大型互联网公司 HR 要求：在职位上加"招聘状态"（招聘中 / 冻结 / 储备 / 已关闭），招聘模块的看板按此过滤。

### 方案要点

- **扩展对象**：`hbjm_jobhr`
- **扩展字段**：`${ISV_FLAG}_recruitstatus` (`ComboField`，4 枚举)
- **详见**：CS-01

### 扩展包产出

```
hrmp-kdtest_hbjm_ext/
  └── kdtest_hbjm_jobhr.dym     (主扩展包)
  └── recruit_dashboard 调用 kdtest_hbjm_jobhr 此字段过滤
```

### 实测关键数据

- **目标 formId**：`hbjm_jobhr`
- **无需扩展 t_hbjm_job_i**（ComboField 不是多语言）
- **无需 4 前缀同步**（职位无调整申请单）
- **无需扩展 hbpm_position**（招聘看板独立查职位表即可）

### 踩坑

- ❌ 字段类型一开始用 `MultiLangTextField` → 拼写错误（正确 `MuliLangTextField`，`EX-13`）
- ❌ 一开始 `comboOptions` 漏传 → 下拉框空白
- ❌ 在 `hbjm_jobseqhr`（职位序列）上加此字段 → 范围错，应该在具体职位上
- ✅ 最终：只扩展 `hbjm_jobhr` 主表，ComboField 4 枚举

### 待补充：真实项目 ID + 代码路径

---

## CASE-06 · 时序版本批量重置 hisversion（负面教学案例）

### 业务背景

某中型企业 HR 定制开发时，为了"清理测试数据"，写了一个脚本直接 UPDATE `t_hbjm_job.fhisversion = 1`，期望重置所有版本号。

### 事故

- 执行后：所有职位的 `hisversion` 被重置为 1
- 下游："岗位编制审批"模块按 `hisversion` 幂等，现在 100% 命中已处理记录，导致所有新变更被吞
- 恢复：从备份还原 + 手工 UPDATE `hisversion` 回对应值（2 人天）

### 教训

- ❌ **绝不要** SQL 直接改时序字段（`boid` / `iscurrentversion` / `hisversion` / `sourcevid` / `firstbsed`）
- ❌ **绝不要** 覆盖 `HisModelOPCommonPlugin` / `BdVersionSaveServicePlugin`
- ✅ 测试数据清理应走标品 `delete` opKey（`hrbddeletevalidator` 会保护系统预置）
- ✅ 学习 `JobHrMsgHandleOp` 为什么只写 `sourcevid`，不写其他时序字段

### 相关禁区

`scene_doc.json` L378-L497 中 `boid` / `iscurrentversion` / `datastatus` / `firstbsed` / `hisversion` 5 字段均 `minefield: red` + `isvCanModify: false`。

---

## 案例收集路线图

### 已整理（样板用）
- ✅ CASE-01：职位编码按序列前缀生成（CS-05 落地版）
- ✅ CASE-02：字段联动（CS-02 落地版）
- ✅ CASE-06：时序字段误改事故复盘

### 待补充真实项目
- ⏳ CASE-03：禁用前检查案例
- ⏳ CASE-04：变更推送下游案例
- ⏳ CASE-05：招聘状态字段扩展案例

### 长期目标案例类型

- [ ] 职位说明书自定义打印模板
- [ ] 跨职位序列级联校验（如总监级必须属管理序列）
- [ ] 职位关键任务分录扩展
- [ ] 审批流集成（审核加双签）
- [ ] AD/LDAP 外部系统职位同步
- [ ] HIES 导入模板扩展（`cusstartpage: hismodel_importstart` 内定制）
- [ ] 多语言字段扩展（如定位 / 主要职责加新语言）
- [ ] 职位继任者规划集成

---

**📌 来源追溯**：
- CASE-01 编码规则：`_auto_operations.md` L99-L113 save 链 + CS-05 扩展方案
- CASE-02 字段联动：`_auto_plugin_semantics.md` L29-L43 `JobHisBasedataFiledChangeEdit`
- CASE-03 禁用检查：`_auto_operations.md` L204-L211 disable 链 + `JobHrDisableOp` 父类 HRDataBaseOp
- CASE-04 消息发布：`_auto_plugin_semantics.md` L13-L26 `JobHrMsgHandleOp` 模式
- CASE-05 字段扩展：`scene_doc.json` 64 字段类型分布 + `EX-12 EX-13` 踩坑
- CASE-06 事故复盘：`scene_doc.json` L378-L497 minefield 禁区 + `_auto_plugin_semantics.md` L19 JobHrMsgHandleOp 只写 sourcevid 的设计理由
