# 变更影响面 · 岗位信息维护 (hbpm_positionhr)

> **状态**: 🟢 基于 `scene_doc.json` minefield + `_auto_operations.md` 9 插件链 + 7 类 position 反编译 + `refentity_reverse.json` 27 下游引用实证（2026-04-24）
> **核心发现**: 改岗位 1 字段影响链是**跨组织树 / 职位体系 / 人员任职 3 层叠加**· 全域影响面比职位场景深一个层级
> **confidence**: real_deploy

---

## 一、为什么岗位域的影响面特别大

岗位 `hbpm_positionhr` 是 HR 全域的**枢纽**：

1. **向上**：绑 `adminorg` 行政组织 + `job` 职位 + `positiontpl` 岗位模板 · 多维度依赖
2. **向下**：被 **27 处** 实体的字段引用（`refentity_reverse.json`）· 覆盖员工任职 / 组织编制 / 变更记录 / 预警
3. **横向**：有 `parent` 自引用岗位树 + `entryentity` 协作关系分录 · 自身还形成网状图
4. **时序**：多历史版本共存 · 每版本的变更都要通过调度任务广播给下游
5. **特殊字段**：`changetype` / `changeoperate` / `changescene` 三分类由各 `PositionHr*Op` 自动写 · 手改无效

**结论**：改岗位字段 ≠ 改 1 张表 · 改岗位 1 字段 = **2 物理表 + 27 下游 + 调度任务消息**。

---

## 二、改字段的影响

### IF-01 · 修改 hbpm_positionhr.number（岗位编码）

**直接影响**：
- `t_hbpm_position.fnumber` 主表列
- `CodeRuleOp` 编码规则基础资料的序号占用 / 回收（`PositionCodeRuleHelper.recycleNumber` 机制）
- `PositionImptRuleNumberValidator`（导入场景编码校验）

**间接影响**（27 处下游引用 · 虽然下游存的是 `boid` 而非 `number` · 但展示层常拼 number 显示）：
- 所有"岗位列表展示"模块（可能缓存 number）
- `hbpm_chgrecord` / `hbpm_chgrecordevt` 变更记录（可能已记录老 number）
- 外部系统同步：如与 ATS 招聘 / HR SaaS 的接口

**升级风险**：跨版本升级时 · 任何改过 `number` 类型 / 长度的定制都会与标品冲突

**推荐做法**：**通过扩展字段实现自定义岗位编码映射**（CS-04）· 不要改标品 `number` 字段定义

**遵循规则**：PR-007（预置岗位 number 不可改 · 业务岗位可改但下游引用多）

### IF-02 · 修改 hbpm_positionhr.adminorg（行政组织）

**直接影响**：岗位归属变更 · 组织体系核心

**级联重算**：
- `org` 字段会被 `PositionHrSaveOp.beforeExecuteOperationTransaction` 重新写 `entity.set("org", adminorg.org)`
- `countryregion` / `city` / `workplace` 前端可能按 adminorg 重算（`PositionEdit.propertyChanged` adminorg 分支 L462-L481）
- 该岗位下**所有员工档案**的组织维度发生变化（`hrpi_empposorgrel` / `hrpi_empjobrel` 间接受影响）
- **数据权限链重算**：能看到该岗位的 HR 范围变化

**下游反写**（27 处引用的 position 字段不会自动改 · 但语义上影响）：
- `hrpi_empposorgrel.position` — 员工岗位组织关系
- `hrpi_empjobrel.position` — 员工任职关系
- `haos_chargeperson.position` — 部门负责人岗位
- `haos_orgpersonstaffinfo.position` — 占编员工维护
- `haos_dimstaffreport.position` — 多维度编制报表

**平台保护**：`PositionEdit.setControlVisibleEanbleWhenEdit` L383 已 `setEnable(FALSE, {"adminorg"})` · 编辑态禁改 adminorg

**推荐做法**：不改已有岗位的 adminorg · 要换组织走 `change` → `confirmchange` 新增版本（保留历史 · 不覆盖）

**`scene_doc.json` minefield**：🟡 yellow

### IF-03 · 修改 hbpm_positionhr.enable（启用 / 禁用）

**影响**：
- 禁用后不能作为新任职关系关联目标
- 禁用后不能在编制分配中使用
- 禁用后该岗位下的在职员工出现"岗位已禁用"告警
- 禁用后 `hrpi_rotationinfo` / `hrpi_dispatchinfo` 轮岗 / 派遣的"原岗位"字段保留
- **二态设计**：启用(1) / 禁用(0)

**实现点**：
- `enable` 操作 → `PositionHrEnableOp`（**onAddValidators 空** · 标品无启用前校验）
- `disable` 操作 → `PositionHrDisableOp`（**onAddValidators 空** · 业务阻断委托 `IBosPositionService.disablePositions` · **事务已开始** · 阻断晚）
- 标品不检查在职员工 · 需自定义（CS-03）
- 各 Op 自动写三分类：enable→1070/1070/1070 · disable→1040/1030/1040

**`scene_doc.json` minefield**：🟡 yellow（变更会级联影响 27 处下游 · 慎改）

**推荐做法**：CS-03 前置 Validator 拦截有在职员工的硬禁用

### IF-04 · 修改 hbpm_positionhr.parent（上级岗位）

**直接影响**：岗位树结构变动

**级联风险**：
- 触发 `PositionHisLoopValidator.checkSysRel` 行政汇报链成环校验
- 若新 parent 导致成环 → his_save 时阻断
- 下游的岗位树展示全部需要重算（编制报表 / 组织架构图）
- `hbpm_positionrelation.parent` / `role` 关联关系需要核对

**编辑态保护**：`PositionEdit.beforeF7Select` L932-L936 已 `QFilter("boid","!=",boid)` 禁选自身

**推荐做法**：变更 parent 必走 `change` → `confirmchange` 新增版本（不直接 update） · 这样历史版本保留原树形

**`scene_doc.json` minefield**：🟡 yellow

### IF-05 · 修改 hbpm_positionhr.job（关联职位）

**直接影响**：
- 岗位 `lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade` / `jobscm` / `jobgradescm` / `joblevelscm` 6 个职级职等字段的合法性需重算
- `joblevelrange` / `jobgraderange` 2 个冗余文本需要重写
- `JobLevelGradeRangeImportValidator` 的 15 条校验（`JobLevelGradeRangeCheck_0` ~ `_15`）需要重新跑

**系统参数相关**：
- `SystemParamHelper.getPosMustRelateJobParameter(orgId)` 若打开 · job 必填
- 若关闭 · job 可为空 · 但关联字段自动放松

**下游反写风险**：
- `hrpi_empposorgrel.position` 员工的岗位引用不变（按 boid）· 但薪酬定档需要重算
- 绩效模板若按 `jobfamily`（从 job 派生）配置 · 需要重评估

**推荐做法**：仅通过 save 链的 `PositionHrSaveOp` 重写 `beforeExecuteOperationTransaction` 扩展（CS-02 变种）· 不直接 SQL 改表

### IF-06 · 修改 hbpm_positionhr.bsed / bsled（生效 / 失效日期）

**影响**：
- 产生新的时间轴版本
- 历史版本自动通过 `iscurrentversion` 字段切换
- `PositionHisSaveOp.calcBsledByNextBsed` 会按下一版 bsed - 1 天自动算本版 bsled
- 下游取数取决于当前时间点的有效版本（PR-008 查当前版本用 `iscurrentversion=true`）

**PositionHisValidator 9 条校验**（`PositionHisValidator.java` L53-L186）：
- 首版本 bsed ≥ firstbsed
- 末版本 bsled 必填
- 历史版本 adminorg 与当前版本一致
- bsed ≤ bsled
- establishmentdate ≥ parent.firstbsed
- bsed ≥ adminorg.firstbsed
- bsed ≤ 当前日期
- 版本间 bsled = 下一版 bsed - 1 天（严格连续）

**注意**：如需未来生效 · 必须走 `newhisversion` opKey 新增数据版本（而不是直接 save）

**`scene_doc.json` minefield**：🟡 yellow

### IF-07 · 修改 hbpm_positionhr.status（单据状态）

**影响**：
- 岗位**没有传统 submit/audit/unaudit** opKey（不在 57 opKey 列表）· 保存即落库
- `status` 字段由 `HRBaseDataStatusOp`（save 链第 3 位）维护
- 不能跳状态直接赋值（违反 PR-007）

**`scene_doc.json` minefield**：🟡 yellow

### IF-08 · 修改 hbpm_positionhr.changetype / changeoperate / changescene（变动三分类）

**⚠️ 严重陷阱**：这 3 个字段由各 `PositionHr*Op.beginOperationTransaction` **自动覆盖**写入（`_auto_plugin_semantics.md` 实证）：
- save 新建：1010/1010/1010
- confirmchange：1020/1020/1020
- dochangerelation：1030/1020/1030
- disable：1040/1030/1040
- enable：1070/1070/1070

**手改无效**：即使你在 `beforeExecuteOperationTransaction` 写了值 · `beginOperationTransaction` 会覆盖

**推荐做法**：
- 永远不要手动 set 这 3 字段
- 如需自定义分类值 · 必须扩展 `PositionHrXxxOp.beginOperationTransaction` 之后的插件（RowKey > 9）

---

## 三、加字段的级联影响（本场景 1 字段 = 1 表）

**⭐ 岗位域核心认知**：给 `hbpm_positionhr` 加 1 字段 · 字段类型决定的表数量：

| 字段类型 | 落地表数 | 备注 |
|---|---|---|
| 普通 TextField / BasedataField / DateField | 1 | `t_hbpm_position` 主表 |
| **MuliLangTextField** | **1** | **⭐ 直接存主表 · 无 `_l` 子表**（区别于职位域的 `_i` 子表） |
| MulBasedataField | 2 | 主表引用 + `t_hbpm_{key}mul` 隐式多选子表 |
| EntryEntity 新子分录 | 2+ | 主表 + 独立分录子表 |
| 在 applicableorgentity 加字段 | 1 | `t_hbpm_standposentry` 分录表 |

**关键差异**（对照职位域）：
- 岗位共 10 个 `MuliLangTextField`（name / simplename / description / posduty / posstandard / posorientation / knowledgereq / skillreq / abilityreq / experiencereq / agereq / oriname）**全部直存主表** · ISV 扩展多语言字段时无需考虑子表同步
- 职位有 `t_hbjm_job_i` 多语言子表 · 加多语言字段时 2 表联动

**如果字段需要联动下游**：
- 如需在 27 处下游某实体也加字段 · 必须单独扩展那些实体（跨 bizApp 扩展）
- 如需事件通知 · 走 BEC（CS-05）· 不要自造消息机制

→ **1 字段在岗位域波及**：1-2 个包（主扩展 + 可选下游）

---

## 四、加分录的影响

### 场景 A：加"岗位关键考核指标"分录（新 EntryEntity）

- 分录名：`${ISV_FLAG}_keymetrics`
- 分录字段：`metricname`（指标名）/ `target`（目标值）/ `weight`（权重）

### 技术要点

- 加分录**不涉及** `applicableorgentity` 现有分录
- 加分录**不自动同步**到时序版本链；如需版本化 · 需要在分录上也标 `boid` 并由 `HisModelOPCommonPlugin` 维护
- ⚠️ 时序模型分录**特别注意**：一个岗位有多个 bsed 版本 · 每个版本的分录是独立复制的还是共享的？需要业务决策
- 平台会自动建表 `t_hbpm_tdkw_keymetrics` 或类似（按苍穹命名规则）

### 场景 B：给 applicableorgentity 分录加字段（扩展现有分录）

- `modifyMeta(op=add, parentScope="applicableorgentity", elementType=field)` 
- 直接落 `t_hbpm_standposentry` 分录表
- 注意 `applicableorg` 是主键性字段 · 别重命名

### 推荐

- 分录只用于"岗位自身的附属信息"（如关键考核指标）
- 不要把分录字段做跨模块联动（如让员工档案直接读岗位分录）· 复杂度爆炸
- 如业务要"每版本独立分录" · 加 `boid` 并按 `boid + version` 查询

---

## 五、修改插件的影响

### IF-10 · 覆盖 PositionHrSaveOp

- **影响**：所有新建 / 修改走你的逻辑
- **风险**：🔴 高（违反 PR-001）
- **标品损失**：
  - 丢失 `isImport()` 分支（导入场景所有逻辑炸）
  - 丢失 `entity.set("org", adminorg.org)` 自动派生
  - 丢失 `IPositionRelationServiceApplication.saveSysRelation` / `saveCooperationRelation` 关系保存
  - 丢失三分类 1010/1010/1010 自动写
  - 丢失 `IPositionHrServiceApplication.afterSavePosition` 下游缓存
  - 丢失 `ChangeMsgServiceImpl.sendMsg` 调度消息
- **推荐**：**不要覆盖** · 只**并列挂** RowKey > 9 的新插件（PR-001）

### IF-11 · 覆盖 PositionHrEnableOp

- **影响**：所有启用操作走你的逻辑
- **风险**：🔴 高
- **标品损失**：
  - 丢失全字段声明的 `onPreparePropertys` L49-L63
  - 丢失三分类 1070/1070/1070 自动写
  - 丢失 `IBosPositionService.commonSyncPositions` 同步下游
- **推荐**：**并列挂** 新 `extends HRDataBaseOp` · 不继承 `PositionHrEnableOp`（PR-001）

### IF-12 · 覆盖 PositionHrDisableOp

- **影响**：所有禁用操作
- **风险**：🔴 高
- **标品损失**：
  - 丢失三分类 1040/1030/1040 自动写
  - 丢失 `IBosPositionService.disablePositions` 底层业务校验
  - 丢失 `ChangeMsgServiceImpl.sendMsg` 消息
- **推荐**：CS-03 的标准做法 · 并列挂新 Validator · 不继承（PR-001）

### IF-13 · 覆盖 PositionHrChangeOp

- **影响**：所有时序版本变更走你的逻辑
- **风险**：🔴 极高
- **标品损失**：`filter(!iscurrentversion)` 仅处理新版本的逻辑 + 三分类 1020/1020/1020 + `saveSysRelation`
- **推荐**：**不要覆盖** · 并列挂 confirmchange opKey 的新插件

### IF-14 · 覆盖 PositionHrRelationChangeOp

- **影响**：所有 dochangerelation 操作
- **风险**：中
- **推荐**：并列挂（PR-001）

### IF-15 · 覆盖 PositionHisSaveOp

- **影响**：所有历史保存 / 批量历史迁移
- **风险**：🔴 极高
- **标品损失**：
  - 丢失 3 Validator 注册（JobLevelGradeRange / PositionHisLoop / PositionHisValidator 9 条）
  - 丢失 `calcBsledByNextBsed` 自动算 bsled
  - 丢失 `setDefaultValue` 补默认
  - 丢失 `HisModelServiceHelper.createDataVersions` 创建历史版本
- **推荐**：**绝对不覆盖** · 并列挂

### IF-16 · 覆盖 PositionEdit（FormPlugin）

- **影响**：所有前端字段联动 + F7 过滤 + 状态控制
- **风险**：🔴 极高
- **标品损失**：
  - 6 字段 propertyChanged 联动分支（adminorg / city / countryregion / parent / bsed / positiontpl）
  - 5 F7 过滤分支（reportType / city / positiontpl / adminorg / parent）
  - 9 afterDoOperation UI 切换分支
  - `recycleNumber` 编码回收机制
  - 岗位模板 `setTplFieldEnable` 批量置灰
- **推荐**：**不要覆盖** · 新 `extends HRDataBaseEdit` 并列挂 `BILL_FORM`（CS-02）

### IF-17 · 覆盖 PositionList

- **影响**：列表加载 / 过滤 / 批量操作
- **风险**：中
- **推荐**：继承 `PositionList` + super.setFilter 追加 filter（或并列挂新 `HRDataBaseList` 子类）

### IF-18 · 覆盖 BdVersionSaveServicePlugin（苍穹基础）

- **影响**：所有基础资料版本化
- **风险**：🔴 极高（影响全苍穹基础资料）
- **推荐**：**绝对不覆盖**

### IF-19 · 覆盖 HisModelOPCommonPlugin（HR 通用）

- **影响**：所有 HR 时序模型
- **风险**：🔴 极高（影响全 HR 时序基础资料）
- **推荐**：**绝对不覆盖**

### IF-20 · 覆盖 HRBaseOriginalOp / HRBaseDataStatusOp / HisUniqueValidateOp

- **影响**：HR 全线出厂值 / 状态 / 时序唯一
- **风险**：🔴 极高
- **推荐**：**绝对不覆盖**

---

## 六、改 opKey 配置的影响

### IF-30 · updateOperation(opKey=save, plugins=[...]) 全量替换

- **症状**：调后 save 链 9 标品插件全丢 · 岗位保存走不通
- **风险**：🔴 极高（EX-20）
- **预防**：`先 get 再 append`
- **实现**：
  ```python
  current = client.get_operation(formId="hbpm_positionhr", opKey="save")
  current.plugins.append(my_plugin)
  client.update_operation(plugins=current.plugins)
  ```

### IF-31 · updateOperation(opKey=save, validations=[...]) 全量替换

- **症状**：调后 save 的 1 条标品校验（`MustInput 6096194600001fac`）丢 · 字段合规性不校验
- **预防**：同上

### IF-32 · updateOperation(opKey=his_save, validations=[...]) 全量替换

- **症状**：his_save 的 3 Validator（JobLevelGradeRange / PositionHisLoop / PositionHisValidator）全丢
- **严重性**：🔴 极高 · 会导致历史数据完整性崩溃

### IF-33 · disable 某条标品 validation

- **场景**：业务想去掉 save 的 "字段合规性"（`MustInput 6096194600001fac`）
- **做法**：`updateOperation(validations=[...新数组 · 把该 id 设 enabled=false])`
- **风险**：低（本来就是业务可选）

### IF-34 · 改 importdata_hr 的 cusstartpage

- **场景**：想把导入向导页换成自定义页面
- **做法**：`updateOperation(opKey="importdata_hr", cusstartpage="${ISV_FLAG}_customstart")`
- **风险**：中 · 自定义页面必须实现导入向导的完整接口

---

## 七、升级版本的影响

| 版本 | 破坏性变更（推断） | 适配建议 |
|---|---|---|
| 当前版本 | 9 插件保存链稳定 | - |
| 未来版本 | `PositionHrSaveOp` 可能新增字段读 | 插件按接口扩展 · 不假设字段列表 |
| 未来版本 | `ChangeMsgServiceImpl` 可能迁 BEC | 不要直接引用 ChangeMsgServiceImpl（非 SDK） |
| 未来版本 | 岗位模板 8 字段列表可能扩 | 不要 hardcode `POSITIONTPL_FILED_KEYS` |
| 未来版本 | `positiontpl` 联动逻辑可能优化 | 并列挂 · 不覆盖 `PositionEdit.changePositionTpl` |
| 未来版本 | 时序模型可能升级 | 不要覆盖 `HisModelOPCommonPlugin` / `BdVersionSaveServicePlugin` |

---

## 八、影响面检查清单（做扩展前必过）

### 修改字段前
- [ ] 此字段 minefield 是什么颜色？（`scene_doc.json` 红 / 黄 / 无）
- [ ] `isvCanModify` 是 true 还是 false？
- [ ] 是否是时序字段（`boid` / `iscurrentversion` / `datastatus` / `firstbsed` / `sourcevid` / `hisversion`）？
- [ ] 是否是出厂字段（`orinumber` / `oriname` / `oristatus`）？
- [ ] 是否是变动三分类（`changetype` / `changeoperate` / `changescene`）？
- [ ] 是否是软删除字段 `isdeleted` / 系统预置 `issyspreset`？
- [ ] 此字段改名会破坏 27 处下游依赖吗？

### 加字段前
- [ ] 字段 key 有 ISV 前缀吗？（`${ISV_FLAG}_*` / `kingdee_*`）
- [ ] 字段 key ≤ 24 字符？
- [ ] 字段类型在 74 值枚举里吗？（`EmployeeField` 不在！用 `BasedataField + basedataNumber=hrpi_employeenewf7query`）
- [ ] 如果是 Combo · `comboOptions` 准备了吗？
- [ ] 多语言字段直接存主表（本场景无 `_l`）· 但要确认字段数量（主表列上限注意）
- [ ] 是否影响任职关系（Tier 1 · 5 实体）？

### 加插件前
- [ ] 是覆盖标品还是新增？（优先新增 · PR-001）
- [ ] 是否调用 super？（所有继承 `HRDataBaseOp` 的都要 super）
- [ ] RowKey 顺序（CodeRuleOp 必须第 1 位 · PositionHrSaveOp 在最后第 9 位）
- [ ] 异常处理用 `addErrorMessage` 而非 `throw`（PR-010）
- [ ] FormPlugin setValue 用 `beginInit/endInit` 包裹（PR-004）
- [ ] OP 用 `entity.set` · 不用 `getModel().setValue`（PR-003）

### 改操作配置前
- [ ] `plugins` / `validations` 是全量替换吗？（答案：是）
- [ ] 是否先 get 再 append？（IF-30 / IF-31 / IF-32）

### 发事件前
- [ ] 是否在 `afterExecuteOperationTransaction` 阶段（PR-010 · 主事务已提交）？
- [ ] 走苍穹 BEC（`IEventService`）· 不自建 MQ（PR-011）
- [ ] eventNumber 已在开发平台【业务事件管理】预配置？
- [ ] variables 只传关键字段 · 不塞完整 DynamicObject？

---

## 九、"最小变更"原则（强推）

做岗位域定制时 · 按**影响最小**的顺序尝试：

```
优先级 1: 改编码规则基础资料配置（不动代码不动元数据 · PR-006）
   ↓ 不够用
优先级 2: 改 positiontype / positiontpl 基础资料配置（业务侧动）
   ↓ 不够用
优先级 3: 改系统参数（getPosMustRelateJobParameter · 开发平台开关）
   ↓ 不够用
优先级 4: 改 updateOperation.validations 的 enabled（UI 可切换）
   ↓ 不够用
优先级 5: 加扩展字段（只动自己的 ISV 空间 · CS-01）
   ↓ 不够用
优先级 6: 前端 propertyChanged 字段联动（纯 UI 逻辑 · CS-02 · PR-004 注意死循环）
   ↓ 不够用
优先级 7: 新增 Validator 并列注册（如 CS-03 disable 前置校验）
   ↓ 不够用
优先级 8: 并列挂 OP 插件（如 CS-04 编码生成 / CS-05 BEC 事件）
   ↓ 不够用
优先级 9: 继承 HR 基类（HRDataBaseOp / HRDataBaseEdit / HRDataBaseList · PR-001）
   ↓ 不够用
优先级 10: ⛔ 覆盖 PositionHr*Op / PositionEdit / PositionList（违反 PR-001 · 最后兜底）
   ↓ 极少用
优先级 11: 修改继承父模板 hbp_histimeseqtpl（⛔ 禁区 · 全 HR 炸）
```

---

## 十、影响面可视化

### 改 hbpm_positionhr.number
```
改 number
    ↓
┌─ t_hbpm_position.fnumber 主表列
├─ CodeRuleOp / CodeRuleDeleteOp 编码规则占用表
├─ PositionImptRuleNumberValidator（导入场景）
├─ 9 标品保存插件执行链
└─ 27 处下游（大部分按 boid · 不按 number）
    │
    v              v              v
  hrpi_empposorgrel   haos_chargeperson   报表 / BI / 外部同步

级联总数: 1 改 → 2+ 插件 + 27 下游
```

### 禁用一个岗位
```
disable
    ↓
PositionHrDisableOp（onAddValidators 空）
    ↓
业务风险: 下游 27 处 position 字段仍然引用 → 告警但不阻断
    ↓
建议: 加 CS-03 前置 Validator （onAddValidators 并列挂）
    ├── 查 hrpi_empposorgrel.position = boid 有在职
    ├── 查 hrpi_empjobrel.position = boid 有在职
    ├── 查 hrpi_rotationinfo.position = boid 轮岗中
    ├── 查 hrpi_dispatchinfo.position = boid 派遣中
    └── 查 hrpi_appointremoverel.position = boid 生效中
    
    有任一 > 0 → addErrorMessage 阻断
```

### 批量确认变更（confirmchange · 100 个岗位）
```
confirmchange [100 iss]
    ↓
HisModelOPCommonPlugin · HisUniqueValidateOp · PositionHrChangeOp
    ↓
100 个 sourcevid 指向上一版 · 三分类写 1020
    ↓
IPositionRelationServiceApplication.saveSysRelation · 100 个
    ↓
IPositionHrServiceApplication.afterSavePosition
    ↓
[你的 CS-05 BEC 插件] afterExecuteOperationTransaction
    ↓
IEventService.triggerEventSubscribeJobs × 100（批量事件）
    ↓
下游 · 薪酬 / 绩效 / 招聘模块订阅 · 按 boid+hisversion 幂等
```

---

## 十一、本场景与职位 / 行政组织的影响面对比

| 维度 | hbpm_positionhr（本场景） | hbjm_jobhr（职位） | haos_adminorg（行政组织） |
|---|---|---|---|
| 1 字段扩展 | 1-2 表（主 + 可选分录） | 1-3 表（主 + 可选 `_i` 多语言） | 8 个包（主 + 4 前缀 + 信息组 + 权限） |
| 系统计算字段 | 6 个时序 + 3 个三分类 + 2 个出厂 | 6 个时序 + 3 个出厂 | 4 个（level/longnumber/structlongnumber/belongcompany） |
| 下游标品反写 | 27 处（refentity_reverse 实证） | 推测 0 | 5 下游（hrpi/hbpm/pay/att/perf） |
| 业务插件数 | 10 个 PositionHr*Op + 4 Validator + 6 Import/Export | 5 个 JobHr*Op + JobHisBasedata* | 14 个 AdminOrg* |
| 4 前缀分录 | 无 | 无 | 有（VQ597FqFoc/7auphYEIJr/8bosVcKAfQ/wHBtyCCUik） |
| 事件订阅 | 走 BEC（CS-05 · PR-011）· 标品用调度任务 | 推荐自建 BEC | 标品 `org_change_event` |
| 多语言存储 | ⭐ 直接主表（无 `_l`） | `_i` 子表（`t_hbjm_job_i`） | `_l` 子表 |
| 必填字段 | `adminorg` | `jobseq` | `name` / `parent` |
| 自引用 | ⭐ `parent` HRPositionField（成环校验） | 无 | ⭐ `parent` |
| 变动三分类 | ⭐ changetype/changeoperate/changescene | 无 | 部分（orgchg） |
| 软删除 | ⭐ `isdeleted=1` · 无 delete opKey | 正常 delete | 正常 delete |

---

## 十二、来源追溯

- minefield 颜色分类：`scene_doc.json` minefield 段（L102-L993）
- 9 保存插件链影响：`_auto_operations.md` L94-L107
- 7 类反编译：`_sdk_audit/_decompiled/scenarios/position/*.java`
- 27 下游引用：`knowledge/workbench/_indexes/refentity_reverse.json` `refs.hbpm_positionhrf7` 段
- 全量替换陷阱：`knowledge/kb_cosmic_modifymeta_traps.md` EX-20
- PR-001 / PR-003 / PR-004 / PR-007 / PR-008 / PR-009 / PR-010 / PR-011：`knowledge/_shared/platform_rules.json`
- PositionHrSaveOp.org 派生：`PositionHrSaveOp.java` L60
- 三分类自动写：5 类 `PositionHr*Op.beginOperationTransaction` 实证
- ChangeMsgServiceImpl：`ChangeMsgServiceImpl.java` L40-L85
- PositionHisValidator 9 校验：`PositionHisValidator.java` L53-L186
- PositionEdit 编辑态禁改 adminorg：`PositionEdit.setControlVisibleEanbleWhenEdit` L383
