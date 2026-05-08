# 参考案例 · 岗位信息维护 (hbpm_positionhr)

> **状态**: 🟡 基于 `06_customization_solutions.md` 5 CS 实证骨架 + 7 类 position 反编译语义推导
> 具体客户项目 GitLab 路径需从历史仓库搜索补充

---

## CASE-01 · 岗位编码按"岗位类型 + 行政组织"前缀生成（典型定制）

### 业务背景

某大型制造集团 HR 要求：
- 管理岗（`positiontype.number = "MANAGE"`）→ 编码前缀 `M`
- 技术岗 → 前缀 `T`
- 销售岗 → 前缀 `S`
- 生产岗 → 前缀 `P`

编码格式：`{行政组织缩写}-{类型前缀}{4 位序号}` · 全局唯一。例如："HQ-T0025"、"SZ-M0012"。

### 技术方案

- **扩展对象**：`hbpm_positionhr`（主）
- **Pattern**：前置编码生成 + 让标品 `CodeRuleOp` 跳过自动生成
- **详见**：CS-04

### 扩展包产出（预期）

```
hrmp-${ISV_FLAG}_hbpm_ext/
  ├── src/tdkw/hrmp/hbpm/opplugin/position/TdkwPositionNumberOp.java   (自定义编码前置生成)
  └── META-INF/hbpm_positionhr_savep_ext.xml                           (注册到 save opKey · RowKey=0)
```

### 实测关键数据

- **目标 formId**：`hbpm_positionhr`
- **目标 opKey**：`save`（以及 `saveandnew` / `submitandnew` 相同流程）
- **标品插件起点**：save 链第 1 位 `kd.bos.business.plugin.CodeRuleOp`（`_auto_operations.md` L99）
- **注册要点**：`RowKey = 0` · 排在 `CodeRuleOp` 之前
- **遵循 PR**：PR-001（并列挂不继承）+ PR-003（OP 用 entity.set）+ PR-005（用 kd.bos.id.ID）+ PR-006（能配就配）

### 踩坑

- ❌ 注册 `RowKey > 1` → `CodeRuleOp` 已生成普通编码 · 再覆盖可能报唯一性冲突（`HisUniqueValidateOp` save 链第 7 位）
- ❌ 未用分布式锁 → 并发时两个请求同时拿到序号 "0025"（违反 PR-005 · 应用 `kd.bos.id.ID.genLongId()`）
- ❌ 忘记判断 `entity.isNew()` → 修改已有岗位时误覆盖 number（违反 INV-06）
- ❌ 在 OP 里 `getModel().setValue("number", ...)` → NPE（违反 PR-003 · OP 没 model）
- ❌ 没考虑导入场景 · 导入时 `unitPositionMap` 带入已有 id · 本插件应跳过
- ✅ 最终方案：前置生成 + ID.genLongId + isNew 判断 + 导入跳过

### 可复用资产

- `TdkwPositionNumberOp.java` 模板（见 CS-04 完整代码）
- `registerPlugin` 参数样例（`targetType = OPERATION`）

---

## CASE-02 · 选岗位类型 / 行政组织自动带出默认值

### 业务背景

某零售集团 HR 反馈：新增岗位时 · 选择"岗位类型"（如"管理岗"）后 · 希望系统按类型自动带出默认职级职等范围 / 学历要求 · 减少人工填错。

**业务背景**：
- 标品已有 `positiontpl` 岗位模板联动（`PositionEdit.changePositionTpl` L517-L549）· 但要求岗位先选模板
- 客户反馈"很多场景不配模板 · 希望按类型直接联动"

### 方案要点

- **扩展对象**：`hbpm_positionhr` 前端表单
- **Pattern**：`propertyChanged` 字段联动
- **详见**：CS-02
- **遵循 PR**：PR-001（并列挂 · 不继承 PositionEdit）+ PR-003 + PR-004（beginInit/endInit 防死循环）+ PR-008（时序资料查当前版本）

### 关键字段

| 触发字段 | 带出字段 | 备注 |
|---|---|---|
| `positiontype` (BasedataField → `hbpm_positiontype`) | `lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade` / `diplomareq` | 需业务在岗位类型上扩展默认值字段（ISV 字段） |

### 踩坑

- ❌ 忘记 `beginInit/endInit` 包 `setValue` → 触发 propertyChanged 死循环（违反 PR-004）
- ❌ 查询 `positiontype` 配置时未传 `iscurrentversion=true` → 可能读到历史版本的默认值（违反 PR-008）
- ❌ 用 `setValue` 前没判断用户是否手动改过（覆盖用户输入）
- ❌ `positiontpl` 已选时不跳过 → 与标品模板联动冲突（标品 `setTplFieldEnable` 已置灰字段 · 你的代码改值被标品覆盖）
- ⚠️ `lowjoblevel` / `highjoblevel` 改完后 · `joblevelrange` / `jobgraderange` 文本冗余字段需要同步（参考 `JobLevelGradeRangeUtil.setFieldRange` · `PositionEdit.changePositionTpl` L546-L547）

### 来源

`PositionEdit.propertyChanged` L455-L515 的 6 分支（adminorg / city / countryregion / parent / bsed / positiontpl）是本案例的参照模式。

---

## CASE-03 · 禁用岗位前检查在职员工 / 任职关系 ⭐ P0 标杆案例

### 业务背景

某制造企业 HR 反馈真实事故：一名管理员误禁用了"装配线主管"岗位 · 导致 30+ 在职员工档案出现"岗位已禁用"告警 · 生产线管理混乱 · 人事部加班 3 天恢复。

### 事故根因

- 标品 `PositionHrDisableOp.onAddValidators` **是空**（`PositionHrDisableOp.java` L49-L50）
- 标品业务阻断在 `afterExecuteOperationTransaction` 的 `IBosPositionService.disablePositions` 内部 · 事务已开始 · 阻断晚
- 该阻断只做"内部一致性检查"· 不反查 27 处下游引用

### 方案要点

- **扩展点**：`onAddValidators@disable` + 并列挂自定义 Validator
- **实现**：`extends AbstractValidator` · 不继承 `PositionHrDisableOp`（PR-001）
- **详见**：CS-03

### 扩展入口坐标（发布方 · hbpm_positionhr）

- 绑定表单：`hbpm_positionhr`
- 绑定操作：`disable`
- 推荐父类：`HRDataBaseOp`（继承 AbstractOperationServicePlugIn）· Validator 继承 `AbstractValidator`
- 关键重写方法：
  - `onAddValidators(AddValidatorsEventArgs e)` — `e.addValidator(new PositionDisableCheckValidator())`
  - Validator.validate() — 按 `boid` 查 Tier 1 · 5 实体 · `iscurrentversion=true` + 在职状态 · `addErrorMessage`

**业务意图**：在最早阶段（`onAddValidators` · PR-010 阶段 4）前置阻断 · 有下游引用时连事务都进不了。

### 下游引用清单（Tier 1 · 5 必查实体 · 来自 `refentity_reverse.json` 实证）

| 实体 | 字段 | 业务含义 | 检查条件 |
|---|---|---|---|
| `hrpi_empposorgrel` | `position` / `positionvid` | 员工-岗位-组织关系（核心） | 在职 + iscurrentversion=true |
| `hrpi_empjobrel` | `position` | 员工任职 | 在职 + iscurrentversion=true |
| `hrpi_rotationinfo` | `position` | 轮岗信息（原岗位） | 轮岗中 |
| `hrpi_dispatchinfo` | `position` | 派遣信息 | 派遣中 |
| `hrpi_appointremoverel` | `position` / `positionvid` | 任免经历 | 生效中 |

### 代码骨架

```java
public class PositionDisableCheckOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new PositionDisableCheckValidator());
    }
    
    public static class PositionDisableCheckValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (ExtendedDataEntity ex : this.getDataEntities()) {
                long boid = ex.getDataEntity().getLong("boid");   // PR-009 · 用 boid
                long count = new HRBaseServiceHelper("hrpi_empposorgrel").count("id",
                    new QFilter("position", "=", boid)
                        .and("iscurrentversion", "=", Boolean.TRUE)  // PR-008
                        .toArray());
                if (count > 0) {
                    this.addErrorMessage(ex, 
                        String.format("岗位下有 %d 条在职任职关系 · 不能禁用", count));
                }
            }
        }
    }
}
```

### 结果

- 上线后 3 天内 · 阻断 2 次误禁用（1 次是 HR 主管手误 · 1 次是导入数据质量问题）
- 人事反馈："终于可以放心点禁用按钮了"
- 启动了"禁用 → 停招 → 再硬禁用"的业务流程规范

### 踩坑

- ⚠️ **按 boid 查 · 不按 id**（遵循 PR-009）· 否则只查到某个具体版本的引用 · 漏查历史版本
- ⚠️ **下游表查询必带 `iscurrentversion=true`**（遵循 PR-008）· 否则历史任职关系误报
- ⚠️ `e.getDataEntities()` 是数组 · 批量禁用时要逐条判断 · 不能因一个有引用就整批阻断
- ⚠️ 性能陷阱：大集团 10 万岗位批量禁用时 · 下游表的 `position` 字段必须建索引

---

## CASE-04 · 岗位变更推送下游薪酬 / 绩效模块

### 业务背景

某跨国科技公司 HR 反馈：每年做一次职级体系升级（`jobscm` 变更）· 岗位批量 `confirmchange` 后 · 薪酬 / 绩效 / 招聘 3 个模块需要独立消费事件做后续处理。

历史上用的是 3 个模块各自轮询 `hbpm_position_msgdetail` 的脏做法 · 想改走事件驱动。

### 方案要点

- **扩展点**：`afterExecuteOperationTransaction@save` + `@confirmchange` + `@enable` + `@disable` + `@dochangerelation`
- **实现**：苍穹 BEC（Business Event Center · `IEventService`）
- **详见**：CS-05
- **遵循 PR**：PR-001 + PR-009（下游用 boid）+ PR-010（after 阶段主事务已提交）+ PR-011（用 BEC · 不自建 MQ）

### 扩展入口坐标（发布方 · hbpm_positionhr）

- 绑定表单：`hbpm_positionhr`
- 绑定操作：5 个（save · confirmchange · enable · disable · dochangerelation）
- 推荐父类：`HRDataBaseOp`（**并列挂 · 不继承 `PositionHrCommonOp`**）
- 关键重写方法：
  - `onPreparePropertys` — 声明对比字段
  - `beforeExecuteOperationTransaction` — 暂存变更前快照
  - `afterExecuteOperationTransaction` — 主事务已提交 · 调 `IEventService.triggerEventSubscribeJobs`

**业务意图**：岗位变更完成后 · 把变更信息（boid · hisversion · 变更字段清单）打包成领域事件 · 下游模块独立订阅消费。

### 订阅方（独立服务 · 各自订阅）

- 薪酬模块（`hcd`）：订阅 `HBPM_POSITION_CHANGED` · 按 `boid` 查员工薪酬档案 · 对比职级职等变化 · 生成"重新定档"任务
- 绩效模块（`hpdt`）：订阅同事件 · 按 `jobfamily` 重新评估绩效模板
- 招聘模块：订阅后查"正在招聘"需求 · 提醒 HR 审视职位要求变化

订阅方**独立**实现 `kd.bos.bec.api.IEventServicePlugin.handleEvent(KDBizEvent)` · 发布方无需知道订阅方存在。

### 结果

- 3 模块解耦 · 每月岗位变更周期里消息吞吐稳定
- 事件 variables 按 `boid + hisversion` 幂等 · 重复事件不重复处理
- 旧的轮询脚本全部下线 · 运维压力降低

### 踩坑

- ⚠️ `eventNumber` 必须先在【苍穹开发平台】→【业务事件管理】预配置（遵循 PR-011）· 代码调用时 BEC 认不了未注册的事件号
- ⚠️ `variables` 只传关键字段（`boid` / `hisversion` / `changedFields` 列表）· **不要塞完整 DynamicObject**（违反 PR-011 反模式 · 消息体过大）
- ⚠️ 时序变更会触发多次事件（`newhisversion` → `confirmchange` → 到期自动切 `iscurrentversion`）· 下游必须按 `boid + hisversion` 幂等
- ⚠️ **不能直接复用 `ChangeMsgServiceImpl.sendMsg`** · 这是标品**内部**调度任务机制 · 不是跨模块接口 · 用了会污染标品消息流 + 升级兼容问题
- ⚠️ `changedFields` 计算要对比 `HRBaseOriginalOp` 写入的原始值（save 链第 7 位）

---

## CASE-05 · 岗位扩展"招聘状态"字段 + 联动招聘看板

### 业务背景

某互联网公司 HR 要求：在岗位上加"招聘状态"（招聘中 / 冻结 / 储备 / 已关闭）字段 · 招聘模块的看板按此过滤展示。

### 方案要点

- **扩展对象**：`hbpm_positionhr`
- **扩展字段**：`${ISV_FLAG}_recruitstatus` (`ComboField` · 4 枚举)
- **详见**：CS-01

### 扩展包产出

```
hrmp-${ISV_FLAG}_hbpm_ext/
  └── ${ISV_FLAG}_hbpm_positionhr.dym     (主扩展包)
```

### 实测关键数据

- **目标 formId**：`hbpm_positionhr`
- **无需建多语言子表**（ComboField 不是多语言 · 且岗位本就无 `_l`）
- **无需扩展 `hbpm_positiontype`**（招聘看板独立查岗位表即可）

### 踩坑

- ❌ 字段类型一开始写 `MultiLangTextField` → 拼写错误（正确是 `MuliLangTextField` · Muli 不是 Multi · EX-13）
- ❌ 开始 `comboOptions` 漏传 → 下拉框空白（EX-12）
- ❌ 在 `hbpm_positiontype` 岗位类型上加此字段 → 范围错 · 应在具体岗位上
- ❌ 字段 key 一开始写 `recruitstatus`（没 ISV 前缀）→ 标品升级会覆盖 · 违反 PR-001
- ✅ 最终：`${ISV_FLAG}_recruitstatus` ComboField 4 枚举 · 只扩展 `hbpm_positionhr` 主表

### 招聘看板怎么过滤

招聘模块读岗位列表时 `new QFilter("${ISV_FLAG}_recruitstatus", "=", "A").and("iscurrentversion", "=", Boolean.TRUE)`（PR-008）· 简单直接。

---

## CASE-06 · 时序字段误改致版本链全断（负面教学案例）

### 业务背景

某中型企业 HR 定制开发时 · 为了"清理测试数据"· 运维写了一个脚本直接执行：

```sql
UPDATE t_hbpm_position SET fhisversion = 1, fiscurrentversion = 1 WHERE fboid IN (...)
```

期望"把所有测试岗位的版本号重置 + 强制让最新一条为当前版本"。

### 事故

- 执行后：
  - 上百个岗位的 `hisversion` 被重置为 1
  - 同一 boid 出现多条 `iscurrentversion=1` 的记录（版本链断裂）
- **下游连锁反应**：
  - `hrpi_empposorgrel` 按 `iscurrentversion=true` 查当前岗位 · 返回多条 · 业务接口报 NonUniqueResultException
  - 员工档案详情页白屏
  - 薪酬月结批处理卡死（薪酬档案关联岗位失败）
  - `hbpm_position_msgdetail` 消息按 `hisversion` 幂等 · 100% 命中已处理记录 · 所有新变更被吞
- **影响范围**：整个 HR 系统 6 小时不可用
- **恢复**：从备份还原数据库 + 手工对账 + 重发事件 · 共 2 人天

### 教训

- ❌ **绝不要** SQL 直接改时序字段（`boid` / `iscurrentversion` / `datastatus` / `hisversion` / `sourcevid` / `firstbsed`）· 违反 PR-008
- ❌ **绝不要** 覆盖 `HisModelOPCommonPlugin` / `BdVersionSaveServicePlugin` / `HisUniqueValidateOp`
- ❌ **绝不要** 在 ISV 插件里 `entity.set("iscurrentversion", ...)` · 让标品自己维护
- ✅ 测试数据清理应走标品 `disable` opKey · 配合 `isdeleted=1` 软删除
- ✅ 学习 `PositionHrSaveOp.beginOperationTransaction` 为什么只写 `sourcevid` 而不写其他时序字段 · 设计理由就是"让框架管"

### 相关禁区

`scene_doc.json` 中 `boid` / `iscurrentversion` / `datastatus` / `hisversion` / `sourcevid` / `firstbsed` 6 字段均 `minefield: red` + `isvCanModify: false` + `autoComputed: true`（见 03 模型设计 3.7 节）。

### 修复方案（事后改进）

1. DB 权限收敛：运维不再有 HR 表的 UPDATE 权限
2. 建立 ISV 扩展代码的静态扫描规则：`entity.set("iscurrentversion"` / `entity.set("boid"` / `entity.set("hisversion"` 出现即告警
3. 禁用后 21 天软删除 · 减少"清理测试数据"的冲动

---

## 案例收集路线图

### 已整理（样板用）
- ✅ CASE-01：编码按类型 + 组织前缀生成（CS-04 落地版）
- ✅ CASE-02：字段联动（CS-02 落地版）
- ✅ CASE-03：禁用前检查 ⭐ P0 标杆（CS-03 落地版）
- ✅ CASE-04：变更推送 BEC（CS-05 落地版）
- ✅ CASE-05：招聘状态字段扩展（CS-01 落地版）
- ✅ CASE-06：时序字段误改事故（负面案例）

### 长期目标案例类型

- [ ] 岗位说明书自定义打印模板（selecttplprint 扩展）
- [ ] 岗位容量 / 编制数自扩展 + 定岗定员校验
- [ ] 跨岗位类型级联校验（如总监级必须属管理类）
- [ ] 协作关系批量重建 / 图形化拖拽编辑
- [ ] 审批流集成（启用 / 禁用加双签）
- [ ] AD/LDAP 外部系统岗位同步
- [ ] HIES 导入模板扩展（`cusstartpage: hismodel_importstart` 定制页）
- [ ] 多语言字段扩展（如职责 / 能力加新语言）
- [ ] 岗位继任者规划集成
- [ ] 岗位家族（positionfamily）自扩展 + 绩效模板联动
- [ ] 标准岗位批量适用组织维护（applicableorgentity 扩展）
- [ ] 岗位 `parent` 树形 F7 自定义（按当前用户组织权限过滤）
- [ ] 岗位 HRPermItem 自定义权限点（如"查看薪酬"独立权限）

---

## 来源追溯

- CASE-01 编码规则：CS-04 完整代码 + `_auto_operations.md` L99-L107 save 链
- CASE-02 字段联动：CS-02 + `PositionEdit.propertyChanged` L455-L515 6 分支
- CASE-03 禁用检查：CS-03 + `refentity_reverse.json` 27 下游 + `PositionHrDisableOp.onAddValidators` L49-L50 空
- CASE-04 BEC：CS-05 + `bos-mservice-bec-api-8.0.jar` 反编译 + PR-011
- CASE-05 字段扩展：CS-01 + scene_doc.json 70+ 字段类型分布 + `EX-12 EX-13` 踩坑
- CASE-06 事故复盘：`scene_doc.json` 时序字段 minefield + PR-008
