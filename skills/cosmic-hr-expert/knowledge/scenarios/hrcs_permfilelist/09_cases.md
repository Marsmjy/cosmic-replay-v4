# 参考案例 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 案例库（部分基于反编译实证 + ISV 推荐扩展样板）
> **更新**: 2026-04-28

---

## 一、案例库总览

| ID | 客户类型 | 需求摘要 | 涉及 CS | 实施难度 |
|---|---|---|---|---|
| C-001 | 集团多项目 | 项目维度的列表过滤（项目经理只看自己项目的人） | CS-06 | 中 |
| C-002 | 制造业 | disable 前级联失效角色二次确认 | CS-03 | 中 |
| C-003 | 互联网 | permfile 失效通知公司 BPM 触发权限交接流程 | CS-05 | 高 |
| C-004 | 央企 | hrcs_userpermfile 加"档案备注 + 授权有效期"双字段 | CS-01 | 低 |
| C-005 | 零售连锁 | 删除档案前查"动态方案分配出来的角色" | CS-04 | 中 |
| C-006 | 软件公司 | 用户字段联动自动填 org（主任职带出） | CS-02 | 低 |
| C-007 | 教育集团 | 批量分组时记审计明细（assigner / assigntime） | CS-07 | 低 |

---

## 二、案例详解

### C-001 · 集团多项目场景的列表过滤定制

**背景**：某集团跨 50+ 子公司 · 每个项目有独立 PMO · PMO 只能管自己项目相关的人。但 HR 域管理员（admingroup level=2）按标品逻辑应该能看全部 org · 不符合"项目化用工"的隔离要求。

**方案**：CS-06 列表过滤定制 + CS-01 加 `2isv_project` 字段

**实施步骤**：
1. 调 modifyMeta 给 `hrcs_userpermfile` 加 `2isv_project`（BasedataField → bd_project）
2. 加自定义关联表 `2isv_projectresp`（用户 ↔ 项目）记 PMO 的项目责任
3. 写 ISV ListPlugin 挂 hrcs_permfilelist · setFilter super 后追加 `2isv_project.id in (PMO 负责的项目集合)`
4. 顶级管理员（isAdmin）豁免 · 仍看全部

**结果**：
- 项目 PMO 只能看自己项目的档案
- 顶级管理员仍可全局
- 标品 admingroup level 限制叠加生效（双层隔离）

**涉及代码**：见 `06_customization_solutions.md` CS-06

---

### C-002 · 失效前二次确认

**背景**：某制造业人事经理误操作 · 选了 50 个档案点【失效】· 后台级联清掉了 80+ 个角色绑定。回滚成本高（要重新分配）。

**方案**：CS-03 子方案 3.2 disable 二次确认

**实施步骤**：
1. ISV ListPlugin 拦截 disable opKey（beforeDoOperation · `args.setCancel(true)`）
2. 查 hrcs_userrolerelat 当前选中档案的关联角色数
3. 如有 ≥1 关联 · 弹 confirmCallBack "将级联失效 N 条角色绑定 · 是否继续？"
4. 用户点【是】· `getView().invokeOperation("disable")` 重走（这次不再被本插件拦）

**关键陷阱**：
- 不能在 confirmCallBack 里直接 `executeOperate("disable")` · 不会触发完整 OP 链
- 必须 `invokeOperation` · 让 OP 链按标品流程走
- 拦截后 ISV 状态记录在 PageCache · 防止用户取消后插件状态错乱

**涉及代码**：见 `06_customization_solutions.md` CS-03 子方案 3.2

---

### C-003 · BPM 联动 · permfile 失效触发权限交接流程

**背景**：互联网公司离职流程：员工离职 → HR 把档案 disable → BPM 自动起一个"权限交接"工作流（接管人 + 资源清单 + 截止日期）。

**方案**：CS-05 BEC 发布 · ISV OP 挂 disable.afterExecute

**实施步骤**：
1. 在【开发平台】→【业务事件管理】预配置 `2isv_hrcs_permfile_disabled`
2. 写 ISV OP 挂 hrcs_userpermfile · disable opKey · afterExecuteOperationTransaction
3. 调 `IEventService.triggerEventSubscribeJobs(SOURCE_APP, EVT_DISABLED, msg, vars)`
4. ISV 订阅方 IEventServicePlugin 实现 handleEvent 调 BPM REST API

**关键陷阱**：
- 必须在 afterExecute（事务已提交）· 不在 endOperation（PR-010）
- variables 不塞 DynamicObject · 只放 ID（PR-011）
- BPM API 失败要日志 · 不要抛异常导致 BEC 重试风暴

**实施前校验**：
```bash
# 反编译目录验证标品没发同事件
grep -r "triggerEventSubscribe.*2isv_hrcs_permfile" knowledge/_sdk_audit/_decompiled/scenarios/hrcs_permfilelist/
# 期望：0 命中（确认 ISV 是唯一发布方）
```

**涉及代码**：见 `06_customization_solutions.md` CS-05

---

### C-004 · 央企档案备注扩展

**背景**：央企要求每个档案带"权限说明" + "授权失效日期" · 用于审计追溯。

**方案**：CS-01 加自定义字段

**实施步骤**：
1. modifyMeta 给 hrcs_userpermfile 加：
   - `2isv_remark`（TextField · 200 字符）
   - `2isv_validdate`（DateField · 失效日期）
2. ISV OP 挂 save.onAddValidators · 校验 validdate ≥ 当天
3. ISV 列表 ListPlugin 加列显示这 2 字段（modifyMeta 加列即可）

**结果**：审计字段稳定运行 · 不影响标品流程。

**涉及代码**：见 `06_customization_solutions.md` CS-01

---

### C-005 · 零售连锁 · 防误删动态方案档案

**背景**：零售集团区域 HR 想删一个区域经理的档案 · 但该档案有"按区域动态方案分配"出来的销售管理角色（sourcetype="4"）· 删了会导致动态方案数据脏。

**方案**：CS-04 删除前查下游引用

**实施步骤**：
1. ISV OP 挂 hrcs_userpermfile · delete opKey · onAddValidators
2. Validator 查 hrcs_userrolerelat where permfile=? and sourcetype="4"
3. 如有 ≥ 1 → addErrorMessage "档案有 N 条动态方案分配的角色 · 请先在动态方案上失效后再删除。"

**结果**：用户提示后转去 hrcs_dynascheme 处理 · 数据洁净。

**涉及代码**：见 `06_customization_solutions.md` CS-03 子方案 3.3

---

### C-006 · 字段联动 · user 选完自动填 org

**背景**：HR 经理填表时频繁手填 org · 容易选错（org 列表很长）。

**方案**：CS-02 字段联动

**实施步骤**：
1. ISV FormPlugin 挂 hrcs_userpermfile 编辑表单
2. propertyChanged · 监听 user
3. 查 hrpi_pernontsprop where personid.user=? and iscurrentversion=true（PR-008）
4. beginInit / setValue / endInit / updateView（PR-004 防死循环）

**结果**：录入效率提升 · 错误率下降。

**涉及代码**：见 `06_customization_solutions.md` CS-02

---

### C-007 · 教育集团 · 批量分组审计明细

**背景**：HRBP 经常给员工批量分组 · 后续要审计"哪个 HRBP 在什么时候把哪些员工放进了哪个分组"。

**方案**：CS-07 批量授权扩展 · 加 ISV 字段到 `hrcs_permfilegrpmember`

**实施步骤**：
1. modifyMeta 给 hrcs_permfilegrpmember 加：
   - `2isv_assigntime`（DateField）
   - `2isv_assigner`（BasedataField → bos_user）
   - `2isv_remark`（TextField）
2. ISV OP 挂 hrcs_permfilegrpmember.save · beforeExecuteOperationTransaction · 自动填值

**关键陷阱**：
- 用 `onPreparePropertys` 声明读字段
- OP 用 `entity.set(...)` 不用 getModel（PR-003）
- ID 用 `kd.bos.id.ID.genLongId()`（PR-005）

**涉及代码**：见 `06_customization_solutions.md` CS-07 子方案 7.2

---

## 三、客户调研问题清单（项目启动时问）

ISV 项目实施前 · 必问业务这 10 个问题（决定走哪些 CS）：

1. **数据隔离**：客户的项目化用工有多严格？需要项目维度限定吗？（CS-06）
2. **审批流**：用户授权要走审批吗？（如要 → 自建单据 + BPM · 不能用本场景）
3. **下游通知**：失效档案要触发其他系统流程吗？（CS-05 BEC）
4. **批量级联**：批量 disable 时要二次确认吗？（CS-03 子 3.2）
5. **删除约束**：删档案前要查哪些反向引用？（CS-04）
6. **历史追溯**：档案变更要保留历史吗？（标品不支持时序版本 · 需 ISV 自建）
7. **字段扩展**：需要哪些自定义业务字段？（CS-01）
8. **字段联动**：表单录入要不要自动带值？（CS-02）
9. **审计字段**：分组、角色、档案这 3 处要加 assigner/assigntime 吗？（CS-07）
10. **动态方案集成**：本场景是否与 hrcs_dynascheme 共存？sourcetype 区分如何（业务一致性）？

---

## 四、AI 自动索引（待自动跑）

- **GitLab 索引**：跑 `scripts/search_historical_customizations.py --scenario hrcs_permfilelist`
- **类似场景对照**：参考 `hrcs_dynascheme` / `hrcs_admingroup` / `hrcs_role` 的历史 CS

---

## 五、案例总结 · 使用频率统计

| CS | 案例数 | 频次 |
|---|---|---|
| CS-01 加字段 | C-004 | 高（90%+ ISV 项目都涉及） |
| CS-02 字段联动 | C-006 | 中（50%） |
| CS-03 操作前置校验 | C-002, C-005 | 高（80%） |
| CS-04 下游引用查询 | C-005 | 中（30%） |
| CS-05 BEC 发布 | C-003 | 中（30% · 集成需求） |
| CS-06 列表过滤 | C-001 | 中（40% · 集团客户必有） |
| CS-07 批量审计 | C-007 | 低（20% · 审计敏感型客户） |

---

参考文档：
- `06_customization_solutions.md` —— 7 大 CS 详细代码
- `07_ext_points.md` —— 扩展点路径决策树
- `08_impact_analysis.md` —— 改动影响面
