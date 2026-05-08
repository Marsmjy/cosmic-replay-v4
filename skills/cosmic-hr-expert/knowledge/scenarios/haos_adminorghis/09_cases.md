# 参考案例 · 组织历史查询（haos_adminorghis）

> **状态**：基于反编译 + 共用物理表事实 · 涵盖 5 类典型客户场景
> **confidence**：verified
> **审计时间**：2026-04-27

---

## 案例 1：用户疑惑"我的数据丢了？" → 实际是历史版本

### 业务场景

某客户业务 HR 在 admin_org_quick_maintenance 列表里看不到去年某个组织"研发中心-上海"了 · 焦急投诉"数据丢了"。

### 真相分析

去年组织被 disable / 名称改了 / parentorg 变了 → 走 confirmchange 派生新版本（hisversion+1）· 老版本变成历史（iscurrentversion=false）。客户在 admin_org_quick_maintenance 看到的是**当前版本** · `iscurrentversion=true` 过滤兜底（HisModelListCommonPlugin.setFilter L173-L175 NOT_HIS_PAGE 模式）· 自然看不到老的"研发中心-上海"行。

实际数据**没丢** · 在 t_haos_adminorg 表的历史版本行（iscurrentversion=false）里 · 走 **本场景 haos_adminorghis** 才看得到。

### 推荐 SOP

1. 教用户从菜单"行政组织维护 → 行政组织维护 → 组织历史查询"进入本场景
2. 列表搜索框输入老组织编号或名字（HisModelF7ListPlugin.setFilter L101-L107 的"显示当前编号/名称"机制 · 即便组织已经改名 · 用旧名也能找到 boid）
3. 查到 boid · 点行 → 详情页（VIEW 模式）展示该 boid 的所有历史版本时间轴
4. 业务方 SOP 教育："不要慌 · 组织数据不会丢 · 历史版本永远存"

### 配套 ISV 扩展（可选）

CS-06 跨场景跳转 · 在 admin_org_quick_maintenance 列表加"查看全历史"按钮 · 直接跳到本场景预过滤 · 不用让用户切菜单。

---

## 案例 2：审计要求查"某组织 2024 年 6 月生效的版本"

### 业务场景

集团审计要求："请把研发中心 2024-06-30 时点正在生效的组织版本完整数据导出来"——需要历史时点查询 + 导出。

### 推荐操作链

1. 进本场景 list（haos_adminorghis）
2. 用 `effdatestart` / `effdateend` 字段筛选区间（HisModelF7ListPlugin.beforeBindData L189-L210 默认填今天 + 平台最大日期）· 改成 `[2024-06-30, 2024-06-30]`
3. 列表自动过滤出该时点生效的版本（QFilter `bsed <= '2024-06-30' AND bsled >= '2024-06-30'`）
4. 选目标行 → 点 toolbar "按列表导出"（`export_from_list_hr` opKey）→ 走 HIES 导出引擎

### 配套 ISV 扩展（可选）

CS-01 加自定义筛选字段 · 让审计模块可以按组织类型（adminorgtype）/ 法律实体（corporateorg）等维度二次筛选。

---

## 案例 3：与 admin_org_quick 协作 · 用户跳转看全历史

### 业务场景

集团 HR 经理日常在 admin_org_quick_maintenance 列表上工作 · 偶尔需要看某个组织的"完整变迁史"——从最早 firstbsed 到现在。需要从当前列表无缝跳转。

### 推荐 ISV 扩展（CS-06）

实现"跨场景跳转"功能：

```
admin_org_quick_maintenance 列表上 ISV 自加按钮"查看全历史"
   │
   ▼ itemClick 触发
ISV 拿当前焦点行的 boid · 调 getView().showForm
   formId = "haos_adminorghis"
   customParam.boid = <boid>
   showType = MainNewTabPage
   │
   ▼ 跳到本场景 list
本场景 list ISV 插件 setFilter
   读 customParam.boid · add QFilter("boid", "=", boid)
   │
   ▼
列表只显示该 boid 的所有历史版本（标品 iscurrentversion=false 过滤兜底）
按 hisversion DESC 排序 · 用户看到完整变迁史
```

### 落地代码

详见 `06_customization_solutions.md CS-06`。

---

## 案例 4：数据迁移项目 · 需要从外部系统补录 5 年历史

### 业务场景

某客户从老 HR 系统切换到苍穹 · 5 年的组织变迁历史需要导入到 t_haos_adminorg · 而不是只导当前版本。

### 推荐操作

**优先方案**：标品 HIES 导入（chargepersonimpo_hr opKey）

1. 准备 Excel · 每行一条历史版本 · 含 boid（必填 · 业务对象 ID 维度 · PR-009）/ hisversion / bsed / bsled / firstbsed / 业务字段
2. 进 HIES 导入向导 · 选模板 · 上传 Excel
3. 标品后台分批走 his_save OP 链
4. 每条数据触发 10 个 OrgHis*Validator 校验（详见 02_business_rules.md §5）
5. 失败的行进入 HIES 错误日志 · 业务方修正 Excel 后重导

**备选方案**：自建批处理脚本调 his_save OP（不推荐）

如果 HIES 默认模板不满足业务字段映射 · 可以走 Java 脚本调 OperationServiceHelper.executeOperate("his_save", ...) · 但要先在开发平台注册新的 ImportPlugin 把 Excel 行映射到 entity。

### 关键陷阱

1. **boid 必须由迁移方提供**（PR-009）· 不能让平台自生成 · 否则同 boid 多版本之间关联断裂
2. **hisversion 必须递增**（不连续会被 OrgHisFirstEffDateConsistencyValidator 拒绝）
3. **firstbsed 跨版本一致**（最早版本的 bsed 值 · 不能各版本各自填）
4. **bsed/bsled 区间无重叠 + 连续**（NO_INTERRUPTION_NO_OVERLAP 模式 · OrgHisEffDateContinuityValidator 校验）

### 配套 ISV 扩展（可选）

CS-04 加自建 Validator · 在 HIES 批量导入时识别"导入模式"做迁移容错（OperateOption.getVariableValue("isImport")）· 业务方拍板能否放宽某些校验。

---

## 案例 5：用户在历史查询页找不到"删除按钮" · 业务方说"我以前能删的"

### 业务场景

新升级到苍穹 8.x 的客户 · 老系统里历史版本可以单独删除 · 但本场景没有 delete 按钮。业务投诉。

### 真相分析

这是**时序模型有意设计** · 历史版本不允许删除：

- 删了 → 时序链断（sourcevid → 老版本 → 老老版本 · 中间断了下游引用就乱）
- 删了 → firstbsed 一致性破坏（OrgHisFirstEffDateConsistencyValidator 校验依赖最早版本）
- 删了 → 审计追溯链断（合规要求）

**反编译实证**：本场景 36 opKey 中虽然有 delete · 但实际触发链路被 `HisModelListCommonPlugin.packageData L142-L165` 控制：行级 hisoperation 列只显示 modify/hiscopy/confirmchange/revise 4 个按钮 · **delete 按钮根本不在 actionPanel**。即便用户用 URL 拼出 delete opKey · 标品兜底也会校验失败。

### 推荐 SOP

1. 教育业务方："历史版本不可删 · 这是合规设计"
2. 如果某条历史版本数据错误（如导入时填错）· 走 `revise` 派生新版本修正 · 老错误版本变成 datastatus=DISCARDED
3. 真要删 → DBA 后台 SQL 删（极端情况 · 必须报备审计）

### ISV 扩展边界

不要尝试加 delete 按钮 · 违反时序模型设计。如果业务必须 · 走"标记废弃"模式：
- 自建字段 `${ISV_FLAG}_logical_deleted`（CheckBoxField）
- 列表 setFilter 默认过滤 `${ISV_FLAG}_logical_deleted=false`
- 物理数据保留 · 仅视图层不显示

---

## 案例 6：性能事故 · 大组织（5 万员工）查全历史卡死

### 业务场景

某大型集团 50000 员工组织 · ISV 加了 CS-03 全历史对比页 · 调 `HRBaseServiceHelper("haos_adminorg").queryOriginalCollection` 按 boid 查全历史 · 结果某个组织有 200+ 历史版本 · 页面渲染 30 秒不出来。

### 真相分析

CS-03 代码默认查 `COMPARE_FIELDS = 17 字段 × 200 版本` = 3400 单元格 + 多语言表 join · 单 boid IO 约 50ms × 200 + 渲染 1ms × 3400 = 13 秒 · 加上前端渲染 DOM 200 行 × 17 列差异计算 → 30 秒不奇怪。

### 修复方案

1. **分页加载**：每次只渲染 20 个版本 · 用户翻页加载更多
2. **字段精简**：默认只展示核心 8 字段（hisversion / bsed / bsled / parentorg / adminorgtype / belongcompany / modifier / changedescription）· 用户点"展开更多"再加载剩余
3. **DBA 加索引**：`(fboid, fhisversion)` 联合索引 · 让 ORDER BY fhisversion 走索引 · 不用 filesort
4. **服务端按时间窗收窄**：默认只查最近 5 年（bsed >= now-5y）· 用户主动点"看更早"再扩大窗口

---

## 案例 7：新员工误以为本场景能新增组织

### 业务场景

新入职 HR 在本场景列表上看到 toolbar 有"新增组织"按钮（addnew opKey · opType=new）· 点了之后弹出表单 · 填字段 → save 报错"当前版本已存在"。困惑。

### 真相分析

详见 01_capability_boundary.md §3.5：addnew 在历史查询页**实际不会触发成功** · 因为：
1. `HisModelListCommonPlugin.beforeShowBill L189` 强制把 customParam.hisPage = VERSION_PAGE
2. addnew 底层 OP（AdminOrgFastSaveOp）要求"该 boid 当前版本不存在"才能创建 → 但本场景上的"新增"实际是想创建一条新历史版本 · 而非创建组织 · 语义错位
3. 业务上：列表的 addnew 按钮在标品逻辑里被 `HisModelListCommonPlugin.beforeBindData L98-L105` 隐藏 · 用户**正常情况看不到此按钮**

新员工看到按钮的原因可能是 ISV 二开把它拉出来了 · 或者标品某个补丁版本兜底。

### 推荐 SOP

1. 标品 list 默认不显示 addnew 按钮 · 让用户去 admin_org_quick_maintenance 入口创建
2. 如果 ISV 真的拉出来了 · 走 CS-06 跨场景跳转 · 让点"新增组织"按钮跳到 admin_org_quick_maintenance 创建页

---

## 关联文档

- `01_capability_boundary.md` · 能/不能干啥的边界
- `04_business_flow.md` · 完整调用链
- `06_customization_solutions.md` · CS-03 / CS-06 落地
- `knowledge/scenarios/admin_org_quick_maintenance/06_customization_solutions.md` · 当前版本扩展（必对照）
