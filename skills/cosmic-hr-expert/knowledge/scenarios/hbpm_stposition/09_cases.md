# 参考案例 · 标准岗位维护（hbpm_stposition）

> **状态**: 🟢 verified · 基于反编译 + HisModel 框架知识
> **confidence**: verified
> **审计时间**: 2026-04-27

---

## 案例 1 · 新增标准岗位并自动生成编码

**场景**：用户新增"高级产品经理"标准岗位，系统自动生成 SP 开头的编码。

**关键步骤**：
1. StandardPositionEdit.afterBindData → 检测编码规则（CodeRuleServiceHelper.isExist）
2. 若编码规则存在 → 调用 setPositionNumber() → 生成编码并缓存到 recycleMap
3. 用户填 org / adminorg / job → propertyChanged → 重新生成编码（数字序号随字段值变化）
4. 用户保存成功 → 编码正式落库
5. 用户放弃 → pageRelease → recycleNumber() 归还编码

**踩坑点**：
- 若同时有两个用户新增同类岗位，编码规则会分配不同的序号，不会重复
- pageRelease 回收逻辑依赖 isFromAdd() 判断（新增状态且非 CHANGE_PAGE）

---

## 案例 2 · 变更标准岗位生效日期

**场景**：组织调整，"高级产品经理"岗位从 2026-07-01 起调整到新行政组织。

**关键步骤**：
1. 用户打开详情 → 点"变更"操作
2. StandardPositionEdit.beforeDoOperation → 显示 bsed 控件 / adminorg 开放编辑
3. 用户设置 bsed=2026-07-01，修改 adminorg
4. 用户点"确认变更"→ 检测是否有信息变更（无变更则提示并阻断）
5. StandardPositionChangeOp.onAddValidators（空方法）
6. HisModelOPCommonPlugin.beforeExecuteOperationTransaction：
   - 旧版本（boid=X，id=X）iscurrentversion → false，bsled → 2026-06-30
   - 新版本（boid=X，id=Y）iscurrentversion → true，bsed=2026-07-01，bsled=9999-12-31
   - sourcevid=X，hisversion="2"
7. hbpm_positionhr 引用此岗位的数据**不受影响**（boid=X 未变）

---

## 案例 3 · 禁用有下游引用的标准岗位（使用 CS-02）

**场景**：要禁用"初级销售员"岗位，但有 3 条 hbpm_positionhr 岗位信息引用了它。

**标品行为（无 ISV 扩展）**：
- 标品仅执行平台通用禁用校验（HRBaseDataStatusOp）
- 不检查 hbpm_positionhr 的引用，直接禁用成功
- hbpm_positionhr 中 stposition 字段引用的岗位变为禁用状态，但数据不游离

**ISV 扩展（CS-02）后**：
- TdkwPositionRefCheckOp.onAddValidators 注册 TdkwPositionRefValidator
- 校验发现有 3 条 hbpm_positionhr 引用（enable=1 的），校验失败
- 用户看到错误提示，需先处理关联岗位信息

---

## 案例 4 · 跨场景 F7 选标准岗位

**场景**：在 hbpm_positionhr 岗位信息中，用 F7 选择关联的标准岗位。

**流程**：
1. hbpm_positionhr.stposition 触发 F7 事件
2. HisBaseDataF7FastFilter / PositionBaseDataF7FastFilter 设置基础过滤
3. HisModelF7ListPlugin.beforeBindData：
   - 添加 iscurrentversion=true 过滤（只显示当前版本）
   - 添加生效日期范围过滤
4. 用户看到标准岗位列表（已过滤 isstandardpos=1 的当前版本）
5. 用户选定 → F7 返回 boid（PR-009）
6. hbpm_positionhr.stposition 存储该 boid

**关键**：F7 返回的是 boid，不是 id。这保证了未来标准岗位变更时，hbpm_positionhr 的引用不会失效。

---

## 案例 5 · 查看标准岗位历史版本

**场景**：审计人员需要查看"销售总监"岗位在 2024 年的历史状态（行政组织、职等范围）。

**流程**：
1. 在标准岗位列表找到"销售总监"→ 点击历史版本按钮（hisversionbtn）
2. HisModelListCommonPlugin 打开历史版本列表页
3. 列表按 hisversion 或 bsed 排列所有历史版本
4. 审计人员点击 2024 年某版本 → 打开只读详情（历史版本不可编辑）
5. HisModelFormCommonPlugin 处理历史详情页显示逻辑

---

## 关联文档

- `04_business_flow.md` · 完整操作流程
- `06_customization_solutions.md` · CS-01 ~ CS-05 定制代码
