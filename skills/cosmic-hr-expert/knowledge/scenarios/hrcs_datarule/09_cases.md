# 参考案例 · 数据规则 (hrcs_datarule)

> **状态**: 🟢 基于 3 类反编译实证 + scene_doc.json 21 字段 + 7 CS 定制方案 + hrcs_dynascheme 经验对照
> **confidence**: verified（所有案例的业务动作均来自实证 OP 链 + FormPlugin 流程 · ISV 案例基于 7 CS 代码框架）

---

## 一、典型业务场景案例（标品支持 · 不需要写代码）

### 案例 1 · 总部 HR 限制"绩效顾问"角色只能看部门的员工档案

**背景**：总部 HR 配置了"绩效顾问"角色 · 该角色可以查看员工档案 · 但业务要求"绩效顾问只能看自己所在部门的员工" · 不能跨部门看。

**操作**：
1. 总部 HR 管理员进入"数据规则"菜单（hrcs_datarule）→ 新增
2. 填字段：
   - number = `PREF_ADVISOR_DEPT_ONLY`
   - name = "绩效顾问-仅本部门"
   - entitynum = `hrpi_ermanfilereform`（员工档案 form · 北极星主表）
3. 在 FilterGrid 控件配规则：
   - 字段：`adminorg`（行政组织）
   - 条件：`等于` → 选 "当前用户的行政组织"（支持参数化 `:currentUserDept`）
4. 点保存 → save OP 链执行（HRDataRuleEditPlugin#doSave 序列化 FilterCondition → HRDataRuleSaveOp 校验+清缓存+通知权限链+写 DataRuleLog）
5. 点审核 → status 变为 audit (C) → 规则进入权限链
6. 在 hrcs_dynascheme 的"动态授权方案"中将本条规则加入方案的条件（permfilter）→ 方案分配给"绩效顾问"角色
7. 权限重算任务展开 → 绩效顾问角色的成员登录 → 打开员工档案列表 → 只能看到本部门的员工

**关键点**：
- entitynum 选的是 `hrpi_ermanfilereform`（员工档案的主 formId）
- FilterGrid 里的字段来自该 entitynum 的业务对象 schema（平台 EntityMetadataCache 加载）
- 规则只在 status=audit AND enable=1 时参与权限链计算

### 案例 2 · HR 按"员工状态"限制"运营管理员"只看到在职/试用期员工

**背景**：运营管理员的日常任务（排班/调薪/发通知）只涉及在职和试用期员工 · 不需要看到离职员工。

**操作**：
1. 进入"数据规则" → 新增
2. entitynum = `hspm_ermanfilereform`（员工档案 DynamicFormModel 容器）
3. 配 FilterCondition：
   - 字段：`hrstatus`（员工状态）
   - 条件：`在 [在职, 试用期] 中`
4. 保存 → 审核
5. 在 hrcs_role 的某个角色配置里挂上本条规则
6. 该角色的成员登录 → 只能看到"在职"和"试用期"的员工

### 案例 3 · 数据规则导出 → 跨环境同步

**背景**：开发环境配好了 50 条数据规则 · 需要在测试环境和生产环境部署同样的规则。

**操作**：
1. 在开发环境的"数据规则"列表 → 点【按列表导出】（opKey = export_from_list_hr）
2. 选择导出字段 · 下载 Excel
3. 在测试环境 → 点【导入数据】（opKey = importdata_hr）→ 上传 Excel → 标品 HRBaseDataImportEdit 处理导入
4. 导入的每条规则走 save OP 链（含校验+序列化+清缓存+通知）
5. 检查导入日志 → 确认 50 条全部成功
6. 生产环境同样操作

**标品支持**：hrcs_datarule 有完整的 importdata_hr 系列 6 个 opKey（导入数据 / 查看导入记录 / 按列表导出 / 按导入模板导出 / 按导出模板导出 / 查看导出记录）。

### 案例 4 · 对时序基础资料（如行政组织）限制只选当前版本

**背景**：创建数据规则时 · entitynum 选了 `haos_adminorg`（行政组织 · 时序基础资料）。用户在 FilterGrid 里需要通过 F7 弹窗条件值（如"部门=研发部"）。

**标品自动行为**：
- HRDataRuleEditPlugin#beforeF7Select 拦截 FilterGrid 的 F7 事件（L77-L85）
- 检测到 refEntity（`haos_adminorg`）继承 HisModel 模板 → `HisModelServiceHelper.isInheritHisModelTemplate` 返回 true
- 自动加 QFilter：`iscurrentversion = 1`
- F7 弹窗只显示当前版本的行政组织（不显示历史版本）

**用户不需要任何操作** · 这是标品自动化行为。

---

## 二、ISV 二开案例（基于本知识库 7 个 CS）

### 案例 5 · A 客户 · 加"规则分类"字段 + 列表过滤（CS-01 + CS-07）

**需求**：客户 BI 系统需要按"规则分类"统计数据规则使用情况 · 同时列表需要按分类过滤。

**实施**：
1. modifyMeta add field 到 hrcs_datarule · key=`${ISV_FLAG}_rulecategory` · 类型 MulComboField · 选项: 客户分类/数据安全/合规审计/部门隔离（CS-01 · 详细代码见 06_customization_solutions.md#CS-01）
2. getFormSchema 二次验证字段在 schema 内
3. 自建 ListPlugin `TdkwDataRuleListEnhance` · 继承 HRDataBaseList · 覆写 setFilter · 加 status=audit AND enable=1 默认过滤 + ${ISV_FLAG}_rulecategory 过滤项（CS-07）
4. 通过 modifyMeta 把 ListPlugin 挂在 hrcs_datarule 列表表单 · targetType=LIST_FORM

**Java 包结构**：
```
mycompany.${ISV_FLAG}.hrcs/
  └─ formplugin/
       └─ TdkwDataRuleListEnhance.java  (CS-07 · extends HRDataBaseList)
```

### 案例 6 · B 客户 · save 时校验同 entitynum 不能有同名规则（CS-03）

**需求**：同一业务对象下 · 规则名不能重复 · 防止管理员重复创建导致权限配置混乱。

**实施**：
1. 自建 Validator `TdkwDataRuleSameNameValidator` · 继承 AbstractValidator
2. validate 方法：查 HRBaseServiceHelper("hrcs_datarule").isExists(QFilter("entitynum.number", "=", entitynum).and("name", "=", name).and("id", "!=", currentId))
3. 自建 OP `TdkwDataRuleSaveEnhanceOp` · 继承 HRDataBaseOp · onAddValidators 注册该 Validator（CS-03 · 详细代码见 06_customization_solutions.md#CS-03）
4. 通过 modifyMeta 挂在 save 操作 · targetType=OPERATION

**Java 包结构**：
```
mycompany.${ISV_FLAG}.hrcs/
  └─ opplugin/
       ├─ TdkwDataRuleSaveEnhanceOp.java     (extends HRDataBaseOp)
       └─ validator/
            └─ TdkwDataRuleSameNameValidator.java  (extends AbstractValidator)
```

### 案例 7 · C 客户 · 删除规则前查 hrcs_dynascheme 引用（CS-04）

**需求**：禁止删除正在被动态授权方案引用的数据规则 · 防止方案"指空"导致授权失效。

**实施**：
1. 自建 Validator `TdkwDataRuleRefCheckValidator` · 继承 AbstractValidator
2. validate 方法：用 QueryServiceHelper.queryDataSet 查 hrcs_dynascheme.permfilter 是否引用本规则 id + hrcs_role 子表 dataruleentry 是否引用
3. 自建 OP `TdkwDataRuleDeleteCheckOp` · 继承 HRDataBaseOp · onAddValidators 注册该 Validator（CS-04 · 详细代码见 06_customization_solutions.md#CS-04）
4. 分别挂在 delete / disable / unaudit 三个操作上 · 覆盖所有"让规则失效"的场景

**Java 包结构**：
```
mycompany.${ISV_FLAG}.hrcs/
  └─ opplugin/
       ├─ TdkwDataRuleDeleteCheckOp.java
       └─ validator/
            └─ TdkwDataRuleRefCheckValidator.java
```

### 案例 8 · D 客户 · 数据规则变更通知审计系统（CS-05 BEC）

**需求**：合规要求 · 数据规则的每次变更（save / audit / disable）必须通知审计系统 + SIEM 记录。

**实施**：
1. 先在【开发平台】→【业务事件管理】预配置事件号 `${ISV_FLAG}_hrcs_datarule_changed`
2. 自建 OP `TdkwDataRuleBecPublishOp` · 继承 HRDataBaseOp · afterExecuteOperationTransaction 调 IEventService.triggerEventSubscribeJobs（CS-05 · 详细代码见 06_customization_solutions.md#CS-05）
3. 分别挂在 save / audit / unaudit / disable / enable / delete 六个操作上
4. 订阅方实现 IEventServicePlugin `TdkwDataRuleAuditSubscriber` · handleEvent 写审计日志 + 推 SIEM

**关键注意**：
- BEC 在 afterExecute 阶段发（事务已提交 · PR-010）
- variables 不塞完整 DynamicObject（PR-011）
- 订阅方要做幂等（同一规则 save + audit 会发两次 BEC）

### 案例 9 · E 客户 · 规则分类→entitynum F7 自动过滤（CS-02）

**需求**：用户新建数据规则时 · 选了"规则分类=客户分类"后 · entitynum F7 自动只显示客户域的业务对象（如 hrpi_customer* 系列）。

**实施**：
1. 自建 FormPlugin `TdkwDataRuleCategoryEdit` · 继承 HRDataBaseEdit · implements BeforeF7SelectListener（CS-02）
2. propertyChanged 监听 ${ISV_FLAG}_rulecategory 变化 → 缓存到 PageCache
3. beforeF7Select 在 entitynum 弹窗前 · 读 PageCache · 加 QFilter("number", "like", prefix+"%")
4. 通过 modifyMeta 挂在 hrcs_datarule 表单 · targetType=BILL_FORM

**关键**：FilterGrid 用 BeforeFilterF7Select（不同接口）· BasedataField 用 BeforeF7Select · 不要混（CS-02 踩坑节）。

### 案例 10 · F 客户 · 数据规则参数化（CS-06 分录子表）

**需求**：希望数据规则支持参数化语法（如 `:currentUserDept` 在权限计算时动态替换为当前用户的部门）· 一条规则能服务不同部门的员工。

**实施**：
1. modifyMeta add entryentity `${ISV_FLAG}_paramentry` 到 hrcs_datarule（CS-06）
2. 分录字段：${ISV_FLAG}_paramkey（参数键）/ ${ISV_FLAG}_paramtype（类型）/ ${ISV_FLAG}_paramprovider（提供方：CONST/CTX_USER/CTX_DEPT）/ ${ISV_FLAG}_paramconst（常量值）/ ${ISV_FLAG}_paramdesc（描述）
3. FormPlugin `TdkwParamEntryEdit` · 添加参数行时 ID 用 ID.genLongId()（PR-005）
4. Validator `TdkwParamConsistencyValidator` · 校验 rule 字段里引用的 :xxx 参数都在分录里有定义
5. 权限链消费端 · ISV 自建参数解析器在 buildFilter 前做参数替换

---

## 三、多 CS 组合案例（真实 ISV 项目通常需要 3-5 个 CS 联合实施）

### 案例 11 · 综合项目 · "数据规则治理平台"（CS-01 + CS-03 + CS-04 + CS-05 + CS-07）

**背景**：某大型集团需要把 hrcs_datarule 升级成"企业级数据规则治理平台" · 要求：
- 规则分类标签（BI 统计用）
- 同业务对象不能有同名规则（防止混乱）
- 删除前必须查引用（合规要求）
- 变更事件通知审计系统（合规要求）
- 列表默认只看"已生效"规则

**实施清单**（按顺序）：
1. CS-01 modifyMeta 加字段 ${ISV_FLAG}_rulecategory（MulComboField）
2. CS-03 自建 Validator: TdkwDataRuleSameNameValidator + TdkwDataRuleSaveEnhanceOp + TdkwDataRuleAuditOp（补 audit 后清缓存+写日志）
3. CS-04 自建 Validator: TdkwDataRuleRefCheckValidator + TdkwDataRuleDeleteCheckOp（挂在 delete/disable/unaudit）
4. CS-05 自建 BEC: TdkwDataRuleBecPublishOp（挂在 save/audit/disable/enable/unaudit/delete）+ TdkwDataRuleAuditSubscriber（订阅方）
5. CS-07 自建 ListPlugin: TdkwDataRuleListEnhance（setFilter 加 status=audit + enable=1 + ${ISV_FLAG}_rulecategory）

**Java 包结构**：
```
mycompany.${ISV_FLAG}.hrcs/
├─ formplugin/
│  ├─ TdkwDataRuleCategoryEdit.java        // CS-02 字段联动（可选）
│  └─ TdkwDataRuleListEnhance.java          // CS-07 列表过滤
├─ opplugin/
│  ├─ TdkwDataRuleSaveEnhanceOp.java        // CS-03 save 增强
│  ├─ TdkwDataRuleAuditOp.java              // CS-03 audit 增强
│  ├─ TdkwDataRuleDeleteCheckOp.java        // CS-04 删除校验
│  ├─ TdkwDataRuleBecPublishOp.java         // CS-05 BEC 发布
│  └─ validator/
│     ├─ TdkwDataRuleSameNameValidator.java
│     ├─ TdkwDataRuleRefCheckValidator.java
│     └─ TdkwParamConsistencyValidator.java  // CS-06（如需要）
└─ bec/
   └─ TdkwDataRuleAuditSubscriber.java      // CS-05 BEC 订阅
```

**验证顺序**：
1. getFormSchema 验证 ${ISV_FLAG}_rulecategory 字段在 schema 中
2. save 一条同名规则 → 应报错"已存在同名规则"
3. 在某方案中引用规则 → 删规则 → 应报错列出引用方
4. save 规则 → BEC 调度日志有 ${ISV_FLAG}_hrcs_datarule_changed 事件
5. 列表默认只看 status=audit + enable=1 的规则

---

## 四、反模式案例（取自真实踩坑 · 不要模仿）

### 反模式 1 · 继承 HRDataRuleEditPlugin 然后 super 调用

**做了什么**：ISV 写 `class MyEdit extends HRDataRuleEditPlugin { @Override afterBindData() { super.afterBindData(); /* ISV 自己的逻辑 */ } }`

**后果**：苍穹升级 hrmp 后 · HRDataRuleEditPlugin 内部签名变了（如加了新参数/改了方法名）· MyEdit 编译报错 · 生产环境表单打不开。

**正确做法**：PR-001 · 并列挂新 FormPlugin · 继承 HRDataBaseEdit · 不继承场景专属类。

### 反模式 2 · 直接 DB 改 frule 列绕过校验

**做了什么**：运维直接用 SQL 改了某条规则的 rule 字段 JSON 字符串。

**后果**：JSON 里引用的字段在新版 entitynum schema 中不存在 · 权限链消费端 FilterBuilder.buildFilter 抛 KDBizException · 该 entitynum 的业务对象列表完全打不开 · 影响全部用户。

**正确做法**：必须通过界面配规则（走 FilterGrid → FormPlugin → FilterBuilder 校验）· 或调 OpenAPI 执行 save 操作（同样走 OP 链校验）。

### 反模式 3 · 在 Validator 里查全量数据

**做了什么**：ISV Validator 里 businessDataServiceHelper.load("hrcs_dynascheme", ...) 不加过滤条件 · 加载全部方案数据。

**后果**：客户有 5000+ 方案 · 每次 save 规则都全量加载 → save 慢 5-10 秒 → 管理员抱怨"点保存卡死"。

**正确做法**：用 QueryServiceHelper.queryDataSet + QFilter("permfilter.datarule", "=", ruleId) 精准过滤 · 只查引用本规则的方案（CS-04 代码框架已实践）。

### 反模式 4 · propertyChanged 里直接 setValue 导致死循环

**做了什么**：ISV FormPlugin 里 `propertyChanged` 事件中直接调 `getModel().setValue("entitynum", newValue)`。

**后果**：setValue 触发新一轮 propertyChanged → 再进 setValue → 再触发 → 无限循环 → 浏览器卡死。

**正确做法**：PR-004 · `getModel().beginInit(); getModel().setValue(X, v); getModel().endInit(); getView().updateView(X);`

---

## 五、快速决策矩阵（需求 → 用哪个 CS）

| 业务需求 | 推荐方案 | 代码量 | 风险 |
|---|---|---|---|
| 给规则加分类/标签字段 | CS-01 modifyMeta add field | 0（配置） | 低 |
| 选了分类后自动过滤 entitynum | CS-02 字段联动 FormPlugin | ~80 行 | 低 |
| save 时校验同名/重复 | CS-03 Validator + OP | ~50 行 | 中 |
| audit 后立即生效 | CS-03 audit OP 补清缓存 | ~30 行 | 中 |
| 删除前查方案引用 | CS-04 Validator 查 hrcs_dynascheme | ~80 行 | 中 |
| 规则变更通知审计系统 | CS-05 BEC 发布 + 订阅 | ~150 行 | 中 |
| 规则参数化（动态替换） | CS-06 分录子表 + 参数解析器 | ~200 行 | 高 |
| 列表只显示已生效规则 | CS-07 ListPlugin setFilter | ~40 行 | 低 |
