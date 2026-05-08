# 01 能力边界 · hrcs_permlog（HR 权限日志）

> permlog 是**只读日志视图** · 不是业务执行场景 · 能做什么/不能做什么很关键
> 数据源：PermLogListPlugin.java + HRAdminStrictPlugin.java + 反编译实证

---

## 1. ✅ 能做什么（已覆盖能力）

### 1.1 列表查询能力

- 按 quickSearch 关键字模糊搜索（9 内置字段 - 3 排除 + 用户字段集 · 见 02 §4）
- 按 permfile（用户权限档案）多路 OR 拆解查询（4 路展开）
- 按 rolename / rolenumber 多路 OR 查询（3 路展开）
- 按 logtype（日志类型）筛选 · 决定详情子页路由
- 按 operationtime 时间窗口排序/筛选
- **强制只显示 hashandle='1' 已处理日志**（标品行为）

### 1.2 详情查看能力

- 点击 number 列超链 · 按 logtype.number 路由到 4 个详情子页：
  - hrcs_permlog_role · 角色变更类（编码 1010-1040）
  - hrcs_permlog_userperm · 用户权限类（1050/1060/2010-2095）
  - hrcs_permlog_datarule · 数据规则类（3015）
  - hrcs_permlog_bodimmapping · 业务对象维度映射类（4010-4020）
- 点击 influusernumber 列超链 · 同 4 路 · 但 userperm 强制 isInfluusernumber=false
- 详情页 OperationStatus.VIEW 强制只读

### 1.3 处理日志能力（processlog）

- 单选选中 1 条日志 · 反射调 logtype.handlerclass.doHandler(logId)
- handler 自定义业务（标品挂的 handler 一般做：状态翻转 hashandle 0→1 + 推送告警 + 触发清理）
- 异常被 catch · 不影响 List 主页面

### 1.4 归档设置能力（donothing_archiveset）

- 点击按钮弹 hrcs_permlogarchive_set Modal 子页
- 子页面单独配置归档周期/规则（跟 hrcs_permlog 解耦）

### 1.5 HIES 导入/导出能力

- 6 个 HIES 通用 opKey：importdata_hr / show_import_record_hr / export_from_list_hr / export_from_impttpl_hr / export_from_expttpl_hr / show_export_record_hr
- **export_from_list_hr 实际高频用**（运营审计周期导出）
- 导入实际 0 用（permlog 是日志视图 · 业务上不需要从外部导日志）

---

## 2. ⚠ 受限能力

### 2.1 准入受限（HRAdminStrictPlugin · 11 hrcs 共用）

只允许以下 3 类用户访问：
- 系统超管（PermissionServiceHelper.isAdminUser）
- 苍穹账号（PermCommonUtil.isCosmicUser）
- HR 领域管理员（HRAdminService.isHrAdmin）

普通员工/HR 业务员**无法打开** · 提示"您无法访问该功能，因为您不是HR领域管理员。"

### 2.2 List 强制过滤受限

永远只显示 hashandle='1' 已处理日志（FP_SF5）· 看未处理日志要 ISV 并列挂插件改写

### 2.3 详情页只读受限

OperationStatus.VIEW 强制只读 · 不能在详情页编辑日志（业务正确 · 但客户偶尔会问）

### 2.4 processlog 单选受限

不能批量处理日志 · 多选直接拒绝

---

## 3. ❌ 不能做什么（已知限制）

### 3.1 不能改日志内容

permlog 不是业务单据 · 没有 save/edit/audit 等编辑 opKey · 详情页强制只读 · **历史日志一旦写入不可修改**（除了 hashandle 字段由 processlog 翻转）

### 3.2 不能从 permlog 表单审批

permlog 没有 BillStatus / 审批流 / 工作流挂载 · 业务上也不需要（日志是事后追溯 · 不需要事前审批）

### 3.3 不能在主表单 save 操作里加业务

rules_chain_all.json save · 实际**业务 0 调用**（标品没注册 OP 插件 · BillForm 默认 save 也不会触发）。日志写入靠上游编辑场景的 OP 调 PermLogServiceHelper

### 3.4 不能直接订阅 BEC（业务事件）

grep `IEventService|IEventServicePlugin` 0 命中 → permlog 不订阅业务事件 · 也不发布业务事件。如果要把 permlog 推到外部审计平台 · 必须 ISV 自建（CS-04）

### 3.5 不是 HisModel · 不能查历史版本

permlog 没有 t_*_his 历史表 · 没有 boid / iscurrentversion · 一条日志一行 · 不存在版本流转

### 3.6 不能跨日志类型批量操作

所有操作都按单条/按 logtype 分类处理 · 没有"统一批量审计"操作

### 3.7 不能在 hrcs_permlog 主表加 ISV 字段（标品 ISV 边界）

标品 formId（hrcs_permlog）严禁 modifyMeta add field · 必须建 ISV 扩展元数据 · 这是 platform_rules.json PR-001 的 ISV 边界铁律

---

## 4. 跟其他 hrcs 场景的差异（核心定位）

| 维度 | hrcs_permlog | hrcs_dynascheme/userrole/role 等 |
|---|---|---|
| 视图类型 | **只读日志视图** | 编辑视图 |
| 是否可编辑 | ✗ 强制 VIEW | ✓ |
| 主要 opKey | processlog / donothing_archiveset / 6 HIES | save/submit/audit/disable/reject 等 |
| OP 业务插件 | 0 | 多 |
| Validator | 0 | 多 |
| 数据生成方式 | **被动**（上游编辑场景写） | 主动（用户在表单内编辑保存） |
| 跨表 JOIN | **强**（13 张物理表） | 中（5-6 张）|
| ISV 扩展点 | setFilter / 字段隐藏 / 跨场景日志归集 | save/audit Validator + propertyChanged |
| BEC 角色 | 0 发 0 订 | 0 发（标品） · ISV 自建 |
| HisModel | ✗ | ✗（hrcs 域多数非 HisModel）|

---

## 5. 客户常问 · "这个场景能做 XXX 吗"

| 问题 | 答案 |
|---|---|
| 能不能改日志内容？ | **不能** · 日志一旦写入只读（hashandle 字段除外）|
| 能不能在列表里删除日志？ | 标品没有 delete opKey · ISV 想加要走归档/清理路径（CS-07）|
| 能不能批量处理日志？ | 不能 · processlog 强制单选 |
| 能不能让 HR 业务员（非管理员）看权限日志？ | 不能 · 准入闸拦死 · 除非给业务员加 HR 领域管理员组（不推荐）|
| 能不能看未处理日志？ | 标品**只显示已处理** · ISV 并列挂 ListPlugin 解除 hashandle 过滤（CS-02）|
| 能不能把日志推到 SOC/Splunk？ | 标品不发 BEC · ISV 自建（CS-04）|
| 能不能给敏感日志字段加权限？ | 字段隐藏方案（CS-03）|
| 能不能加自定义日志列？ | List 列扩展 · 但要先扩 hrcs_permlog 元数据加字段（CS-01）|
| 能不能改归档周期？ | 在 hrcs_permlogarchive_set 子页配 · 不需要写代码 |
| 能不能加 logtype 类型？ | 改 hrcs_permlogtype 基础资料数据 + 写 handler 类（CS-07 引）|
| 能不能加业务事件订阅？ | permlog 自己不发 · 但可以**在上游编辑场景**挂 BEC 订阅插件（CS-04 桥接）|

---

## 6. 版本差异

- 当前实证版本：苍穹 8.0（jar `hrmp-hrcs-formplugin-1.0.jar` · `hrmp-hbp-formplugin-1.0.jar`）
- 字段命名规则跨版本稳定
- HRAdminStrictPlugin 三闸逻辑跨版本稳定
- ⚠ 不同苍穹版本的 hrcs_permlogtype 编码（1010/1050/3015 等）可能差异 · ISV 写代码引用编码集时要用基础资料动态查询而非硬编码

---

## 7. 引用文件

- `_decompiled/scenarios/hrcs_permlog/PermLogListPlugin.java` · 能力实证
- `_decompiled/scenarios/hrcs_permlog/HRAdminStrictPlugin.java` · 准入闸
- `rules_chain_all.json` · 13 opKey 完整清单
- `02_business_rules.md` · 详细业务规则
- `06_customization_solutions.md` · 7 个 ISV 定制方案
