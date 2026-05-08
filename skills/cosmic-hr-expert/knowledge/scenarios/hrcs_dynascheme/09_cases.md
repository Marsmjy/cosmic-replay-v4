# 参考案例 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟡 likely · 待业务/客户实战补全
> **数据源**: 反编译 7 类 + scene_doc.json + admin_org/hbpm/hjm 案例参考

---

## 一、典型业务场景案例（标品支持）

### 案例 1 · 总部 HR 给"晋升 P5+"的员工自动配"绩效顾问"角色

**背景**：年度绩效考核结束 · 总部 HR 需要给晋升到 P5 及以上职级的员工自动分配绩效顾问角色（用于查看下属绩效详情）。

**操作**：
1. 总部 HR 进入"动态授权方案"菜单 → 新增方案
2. 填字段：
   - name = "P5+ 绩效顾问角色配置"
   - admingroup = "总部 HR 管理员组"
   - authaction = 1（仅分配）
   - condition = "员工.职级 ≥ P5"（用 PermFilter 控件配 DecisionSet）
   - assignactionentry: 选 actype = "晋升变动" · personitem = "员工本人"
   - assigndays = 30（晋升满 30 天后才生效）
   - roleentry: 添加角色 "绩效顾问" · customenable = "下属员工" · custominfo（在 hrcs_dyscassignroledetail 子页面填范围）
3. 暂存 → 提交 → 审核
4. 标品后台权限重算任务展开 · 命中员工自动写 hrcs_userrolerelat（sourcetype=4）

### 案例 2 · 区域 HR 为"试点城市"配"运营管理员"角色（仅分配）

**背景**：业务在 5 个试点城市试点新流程 · 这些城市的员工需要"运营管理员"角色查看试点数据。

**操作**：
1. 区域 HR 新增方案 · admingroup = "试点区域 HR 组"
2. authaction = 1
3. condition = "员工.工作城市 in [北京, 上海, 广州, 深圳, 杭州]"
4. roleentry = "运营管理员"
5. 试点结束 → disable 方案（不删 · 保留历史）

### 案例 3 · 业务变更 confirmchange · 调整规则后回填

**背景**：上述案例 1 试运行 1 个月后 · 总部 HR 决定 P5 改 P4（扩大范围）

**操作**：
1. 在已审核方案上点【变更】（change opKey）
2. 进入编辑模式 · DynaAuthSchemePlugin 标记 isChange=1（L322-L331）
3. 改 condition → "员工.职级 ≥ P4"
4. 点【确认变更】（confirmchange）
5. DynaAuthSchemeServiceHelper.showChangeTips 弹"会影响 X 人 · 确定吗？"
6. 用户 Yes → DynaAuthSchemeConfirmChangeOp.endOperationTransaction 双写 boid + bgVid
7. 标品权限重算 → 新增 P4 员工的角色 · 不动已有 P5+ 员工

---

## 二、ISV 二开案例（基于本知识库 7 个 CS）

### 案例 4 · A 客户 · 加"方案标签"字段（CS-01）

**需求**：客户 BI 系统要按"方案标签"分类统计动态授权方案使用情况。

**实施**：
1. modifyMeta add field 到 hrcs_dynascheme · key=`${ISV_FLAG}_schemetag` · 类型 MulComboField · 选项: 重要/临时/试点/长期
2. 苍穹开发平台部署
3. 客户 HR 用户使用：在新建/编辑方案时填标签
4. BI 系统通过 OData 查 t_hrcs_dynascheme.ftdkw_schemetag · 加上 iscurrentversion=true 过滤

**踩坑实战**：
- 第一次 fieldName 手填 "fk_tdkw_schemetag" → 平台再加 f → 列名变 "ffk_tdkw_schemetag" 怪名 · 重做
- 第二次未带 ISV 前缀 "schemetag" → 升级时与标品某字段冲突 · 重做
- 第三次正确：key="${ISV_FLAG}_schemetag" · 不传 fieldName · 平台自动 ftdkw_schemetag

### 案例 5 · B 客户 · admingroup 联动 authaction 默认值（CS-02）

**需求**：B 客户希望区域 HR 默认 authaction=1（仅分配）· 总部 HR 默认 authaction=3。

**实施**：
1. 加 ISV 自建表 `${ISV_FLAG}_admingroup_default`：admingroup → ${ISV_FLAG}_default_authaction 映射
2. 加 FormPlugin `TdkwDynaSchemeAdminGroupDefault extends HRDataBaseEdit`
3. 重写 propertyChanged 监听 admingroup 变更 · 查映射表 · setValue("authaction", v)
4. 注册到 hrcs_dynascheme · targetType=BILL_FORM

**踩坑实战**：
- 第一次未加 beginInit/endInit · 死循环 · stack overflow 报错
- 第二次未带 iscurrentversion=true · 查 ${ISV_FLAG}_admingroup_default 拿到多个历史版本 · 取第一个混乱
- 第三次正确：beginInit + endInit + iscurrentversion 过滤

### 案例 6 · C 客户 · save 前校验"方案至少绑 1 个角色"（CS-03）

**需求**：C 客户业务侧曾出现"误保存空 roleentry"导致下游分析方案配置率虚高。

**实施**：CS-03 标准套路 · 自建 OP `TdkwDynaSchemeRoleEntryOp` + Validator `TdkwRoleEntryValidator`

**踩坑实战**：
- 第一次直接 throw KDBizException · 整批操作失败 · 用户体验差
- 改用 addErrorMessage(row, msg) · 平台按行报错 · 用户体验好
- 第二次 onPreparePropertys 没声明读 roleentry · Validator 拿到空 collection · 误判
- 改正后正常

### 案例 7 · D 客户 · delete 前查 hrcs_userrolerelat 残留（CS-04）

**需求**：D 客户曾误删一个生产方案 · 几小时后发现 200+ 员工权限丢失（标品没自动清 hrcs_userrolerelat 但是绑定的方案已经不存在 · 后续重算时这些绑定被孤立）。

**实施**：CS-04 标准套路 · delete 前 Validator 查 hrcs_userrolerelat WHERE sourcetype=4 AND scheme=boid AND status=1 · 有则阻断。

**踩坑实战**：
- 第一次用 dynascheme.id 而非 boid 查 → 查不到任何记录 → 误判全可删
- 改用 boid（PR-009）后正确
- 后续优化：disable 时也加同样的 Validator · 用 setadminrange 时也提示"还有 X 人在用"

### 案例 8 · E 客户 · confirmchange 后通知 OA（CS-05 BEC 发布方）

**需求**：E 客户的 OA 系统缓存了员工权限 · 方案变更后 OA 缓存不刷新 · 员工依然看到老权限。

**实施**：CS-05 标准套路 · 加 OP `TdkwDynaSchemeBecPublishOp` 在 afterExecuteOperationTransaction 调 `IEventService.triggerEventSubscribeJobs("${ISV_FLAG}_hrcs", "${ISV_FLAG}_dynascheme_changed", "动态授权方案变更", variables)`

**踩坑实战**：
- 第一次没在【开发平台】预配 eventNumber · BEC 静默丢 · OA 收不到
- 第二次 variables 塞了完整 DynamicObject · 序列化超时
- 第三次正确：variables 只塞 boid + newVid + name + operateAt · OA 收到后用 boid 反查详情

### 案例 9 · F 客户 · confirmchange 联动写 ISV 变更日志表（CS-06）

**需求**：F 客户审计部门要求每次方案变更必须写到独立审计日志表 · 不能依赖标品 hr_log。

**实施**：CS-06 标准套路 · ISV 自建 `${ISV_FLAG}_dynaschemechgloghr` HisModel 表 · 自定义 OP 在 endOperationTransaction 阶段写。

**踩坑实战**：
- 第一次写在 afterExecuteOperationTransaction · 写日志失败但主事务已提交 · 数据不一致
- 第二次没维护 iscurrentversion · 一个 boid 多条 true · 后续查询全乱
- 第三次：endOperationTransaction（同事务） + 维护 iscurrentversion + 用 ID.genLongId 生成 id（PR-005）

### 案例 10 · G 客户 · 列表按事业部过滤（CS-07）

**需求**：G 客户事业部多 · 区域 HR 不希望看到其他事业部的方案。

**实施**：CS-07 标准套路 · ListPlugin `TdkwDynaSchemeBuFilterListPlugin extends HRDataBaseList` · setFilter 叠加事业部过滤。

**踩坑实战**：
- 第一次继承了 DynaAuthSchemeListPlugin（PR-001 违规）· 标品改了 setFilter 后 ISV 编译失败
- 改成 extends HRDataBaseList · 通过
- 第二次没 super.setFilter(evt) · 把标品的 boid 过滤丢了 · 用户能看到不该看的
- 第三次：super 后再 add 自己的过滤

---

## 三、客户共性需求归纳

| 需求类型 | 出现频率 | 推荐 CS |
|---|---|---|
| 加业务字段（标签/分类/备注） | 高 | CS-01 |
| 字段联动（自动带出默认值） | 中 | CS-02 |
| save 前业务校验 | 高 | CS-03 |
| 删除前查下游 | 高 | CS-04 |
| 变更通知外部系统 | 中 | CS-05 |
| 自定义审计日志 | 中 | CS-06 |
| 列表过滤定制 | 高 | CS-07 |
| 自定义角色范围属性子页面 | 低 | （扩展 hrcs_dyscassignroledetail · 单独场景） |
| 自定义授权动作（除 1/2/3 之外） | 极低 | （要改 ComboField 选项 + 加 ISV OP 处理新值 · 风险高） |

---

## 四、不推荐的 CS / 反案例

### 反案例 1 · 客户 H 试图继承 DynaAuthSchemePlugin

**需求**：客户想加一个"按预设的方案模板新建" 功能 · 拿了 DynaAuthSchemePlugin 直接 extends + super.beforeBindData 后追加自己逻辑

**问题**：标品下次升级把 beforeBindData 签名改了 · ISV 编译失败 · 必须等下次升级补丁

**正解**：并列挂新 FormPlugin（extends HRDataBaseEdit） · 在 beforeBindData 里读 customParam("templateId") · 不超 super 标品 · 跟标品 DynaAuthSchemePlugin 解耦（PR-001）

### 反案例 2 · 客户 I 在 afterExecuteOperationTransaction 写 ISV 自建表

**需求**：方案审核后给 ISV 自建表 `${ISV_FLAG}_audithistory` 写一行

**问题**：写表失败 · 但标品事务已经提交 · 没法回滚 · 数据不一致

**正解**：写同事务的"自建表" 必须挂 endOperationTransaction（PR-010 阶段 8）。afterExecuteOperationTransaction（阶段 9）只用于"发外部事件 / 异步通知"

### 反案例 3 · 客户 J 在 ISV Validator 里 throw KDBizException

**需求**：方案规则太复杂校验失败时报错

**问题**：throw 异常会让整批操作失败 · 用户改一行错误的方案要重新走全批

**正解**：用 `this.addErrorMessage(row, msg)` · 平台按行报错 · 其他行可以继续

---

## 五、AI 自动索引（占位）

```bash
# 跑这个脚本可以自动从 GitLab 仓库索引 ISV 历史定制
python scripts/search_historical_customizations.py --scenario hrcs_dynascheme

# 输出示例（待跑）：
# [${ISV_FLAG}_*] 在以下 ISV 项目中出现：
# - hrcs-customization-A-corp     2 个 OP + 1 个 Validator
# - hrcs-customization-E-bank     1 个 BEC 发布方 + 1 个订阅方
# - ...
```

---

## 六、案例引用规则

按 `_shared/principles/` + `feedback_har_is_ground_truth.md` + `feedback_formid_no_fabrication.md` 铁律：

- 本文档案例 1-3 来自反编译 7 类的代码语义反推 · 标记为"标品支持的典型业务场景"
- 案例 4-10 是 ISV 二开模板（CS-01 ~ CS-07 的实战投影） · 标记为"likely · 实战参考"
- 反案例 1-3 来自 PR-001/PR-010 铁律的反推 · 标记为"应避免"
- **真实客户名/项目路径占位**（A 客户/B 客户/...）· 待后续补全实际 GitLab 路径 + 客户名

→ ISV 实施时应：
1. 先看本文档对照客户需求归类
2. 再读对应 CS 详细操作（06_customization_solutions.md）
3. 实施前查 03_model_design.md 影响字段层级
4. 实施后跑 ISV 自建测试套验证（建议覆盖 audit / confirmchange / disable / delete 4 个高风险 op）
