# 参考案例 · 组织变动明细查询(homs_orgchgrecord)

> **状态**: 基于 `AdminOrgChgRecordListPlugin.java`(654 行)真实业务逻辑 + 配对场景 `homs_orgbatchchgbill` 工作流
> **confidence**: verified(模式) + inferred(实际客户案例细节)
> **审计时间**: 2026-04-27

---

## 案例 1 · 申请单生效后实时同步外部 ERP(配对工作流)

### 业务场景

某客户的财务系统(外部 SAP ERP)需要实时同步组织变动信息·要求"申请单一生效·SAP 收到事件 30 秒内创建对应成本中心"。

### 业务流程

```
[T0] HR 业务管理员在 homs_orgbatchchgbill 创建一张组织调整申请单
     · 内容：新增组织"研发部 - AI 实验室"(parent=研发部)
     · 变动场景：组织调整
     · 变动类型：新增组织
     · effdt：2026-05-01
     │
     ▼
[T1] 提交审批 → audit 通过
     │
     ▼
[T2] OrgBatchChgBillEffectOp.beginOperationTransaction
     · 写 t_haos_adminorg(新增"AI 实验室"行政组织 · iscurrentversion=true)
     · 写 t_homs_orgchgrecord(主记录)
     · 写 t_homs_orgchgentry(分录 · changetype=新增)
     · 写 t_homs_orgchgdetail(字段级变动)
     │
     ▼
[T3] OrgBatchChgBillEffectOp.afterExecuteOperationTransaction
     · 主事务已提交
     · ⭐ ISV TdkwOrgChgRecordSyncExtOp(CS-06 实现)拦截 audit afterExecute
     · 调 IEventService.triggerEventSubscribeJobs("homs", "${ISV_FLAG}_org_chg_record_synced", ...)
     │
     ▼
[T4] BEC 平台异步分发事件
     │
     ▼
[T5] ISV TdkwOrgChgRecordSubscriber.handleEvent 收到事件
     · 调 SAP REST API 创建成本中心
     · 失败时 BEC 自动重试(3 次)
```

### 用户在本场景查询

```
T+1 天，HR 部门主管打开"组织变动明细查询"
   │
   ▼
[setFilter] 过滤当前 BU 可见数据
   │
   ▼
列表显示：
   行政组织 | 变动场景 | 变动类型 | 变动生效日期 | 申请单编号 | 操作人 | ...
   AI 实验室 | 组织调整 | 新增组织  | 2026-05-01    | OBC0001    | 张三 | ...
   │
   ▼
点击行的"申请单编号" hyperLinkClick L200-L218
   │
   ▼
跳转到 homs_orgbatchchgbill 详情页 VIEW 模式
```

### 关键技术点

| 点 | 实证 |
|---|---|
| 配对场景写入端 | `OrgBatchChgBillEffectOp.beginOperationTransaction` 写 4 张表 |
| BEC 发布 | CS-06 在 `homs_orgbatchchgbill` 的 `afterExecuteOperationTransaction` 阶段挂(PR-010) |
| 事件号预配置 | 在【开发平台】→【业务事件管理】注册 `${ISV_FLAG}_org_chg_record_synced`(PR-011) |
| 跨场景跳转 | `billListHyperLinkClick` 反编译实证 L215 跳 `homs_orgbatchchgbill` |

### 关联 CS

- CS-06 配对场景生效后联动监听
- 配对场景的 06_customization_solutions.md 申请单 OP 扩展

---

## 案例 2 · 审计员定期导出"近 30 天关键变动"用于合规审查

### 业务场景

某金融客户合规部要求每月导出"近 30 天的高敏感变动"(belongcompany 法人变更 / corporateorg 法律实体变更 / adminorgtype 类型变更) · 用于内审。

### 业务流程

```
合规审计员登录系统
   │
   ▼
进入"组织变动明细查询" homs_orgchgrecord
   │
   ▼
点击 toolbar 上的 ISV 自加按钮"高级筛选"(CS-05)
   │
   ▼
弹窗 ${ISV_FLAG}_orgchgrecord_advfilter
   · 时间范围：2026-04-01 ~ 2026-04-30
   · 多变动类型(in 查询)：[法人变更, 法律实体变更, 类型变更]
   · 多组织：勾选所有总部和子公司
   │
   ▼
点击"应用" → 弹窗关闭 → returnDataToParent
   │
   ▼
[ISV TdkwOrgChgRecordAdvFilterPlugin.closedCallBack]
   · 把筛选参数序列化存入 pageCache
   · 调用 getControl("billlistap").refresh()
   │
   ▼
[setFilter] 标品 + ISV 双跑
   · 标品先：OrgPermHelper 数据权限 + otclassify=1010
   · ISV 后：读 pageCache 加 4 个 QFilter(start/end/types/orgs)
   │
   ▼
列表展示筛选结果
   │
   ▼
点击 toolbar"按列表导出"(标品 export_from_list_hr)
   │
   ▼
HIES 导出向导 · 选字段 → 导出 Excel
   · 包含 changefield / beforevalue / aftervalue 等计算列(标品 packageData 已计算)
```

### 关键技术点

| 点 | 实证 |
|---|---|
| 高级筛选弹窗 | CS-05 自建 `HRDynamicFormBasePlugin` 子类 |
| ISV setFilter 追加 | `e.getQFilters().add(...)` (PR-001 · 不清掉标品过滤) |
| 多场景 in 查询 | `QCP.in` + List<Long> · 注意集合 size ≤ 1000 |
| BU 权限自动叠加 | 标品 `OrgPermHelper.getHrPermFilter` 自动按用户权限过滤 |
| 导出含计算列 | 标品 `packageData` 把 changefield 等计算字段塞回 event · HIES 导出能读到 |

### 关联 CS

- CS-05 高级筛选弹窗
- CS-01 加自定义筛选字段(进阶版)

---

## 案例 3 · ISV 加自定义字段"成本中心"·端到端跨场景溯源

### 业务场景

某零售集团 ISV 给行政组织加了一个自定义字段 `${ISV_FLAG}_costcenter`(成本中心 · BasedataField → ${ISV_FLAG}_costcenter 基础资料) · 业务方要求:
1. 在配对场景填申请单时能选成本中心
2. 申请单生效后 · 本场景能看到"成本中心从 X 变成 Y"的字段级溯源

### 端到端流程

```
[Phase 1 · 元数据扩展]
   │
   ├─ 在 haos_adminorgdetail 主层 modifyMeta 加 ${ISV_FLAG}_costcenter
   │     · 平台自动同步到 t_haos_adminorg.ftdkw_costcenter
   │     · admin_org_quick_maintenance 当前版本视图自动可见
   │     · haos_adminorghis 历史版本视图自动可见
   │     · 本场景 buildData 也能识别(按 propertySet 动态 select)
   │
   ├─ 在 homs_orgbatchchgbill 写入端的 ISV 扩展(配对场景)
   │     · 让 entryentity_info 和 entryentity_add 包含 info_tdkw_costcenter / add_tdkw_costcenter 字段
   │
   ▼
[Phase 2 · 申请单创建]
   │
   ├─ HR 业务员在 homs_orgbatchchgbill 创建"信息变更"类申请单
   │     · 选行政组织："销售部 - 华南区"
   │     · 改 info_tdkw_costcenter：CC001(原值) → CC002(新值)
   │     · 提交审批 → audit
   │
   ▼
[Phase 3 · 标品生效逻辑写入]
   │
   ├─ OrgBatchChgBillEffectOp 自动:
   │     · 更新 t_haos_adminorg(派生新版本 · ${ISV_FLAG}_costcenter=CC002)
   │     · 写 t_homs_orgchgrecord(主记录)
   │     · 写 t_homs_orgchgentry(变动场景 / 类型 / 操作人 / 时间)
   │     · 写 t_homs_orgchgdetail(字段级 detail)
   │           · chgentitynumber = "haos_adminorgdetail"
   │           · chgpageelement = "${ISV_FLAG}_costcenter"  ← ⭐ 关键 · ISV 字段名
   │           · beforechgentity = 旧版本 ID
   │           · afterchgentity = 新版本 ID
   │
   ▼
[Phase 4 · 本场景查询溯源]
   │
   ├─ 用户进入 homs_orgchgrecord
   │     · setFilter 加权限 + otclassify=1010
   │
   ├─ beforePackageData → buildData(detailIdList) L364
   │     · 查 homs_subentryentity 拿子分录数据
   │     · 按 chgentitynumber="haos_adminorgdetail" 分组(走 §case c "其他实体"分支 L396-L399)
   │     · 拿 propertySet={"${ISV_FLAG}_costcenter"}(运行时 ISV 写进的字段)
   │     · 调 helper.query("${ISV_FLAG}_costcenter", new QFilter[]{id in [旧 ID, 新 ID]})
   │     · 拿到 before(CC001 实体 · name="基础成本中心 - 华南") + after(CC002 实体 · name="升级成本中心 - 华南")
   │
   ├─ buildChangeMap → buildBaseChangeVO(beforeDy, afterDy, "${ISV_FLAG}_costcenter")
   │     · displayName = property.getDisplayName() = "成本中心"(中文)
   │     · beforeValue = "基础成本中心 - 华南"
   │     · afterValue = "升级成本中心 - 华南"
   │
   ├─ packageData 渲染:
   │     · changefield 列 = "成本中心"
   │     · beforevalue 列 = "基础成本中心 - 华南"
   │     · aftervalue 列 = "升级成本中心 - 华南"
   │
   ▼
[Phase 5 · 用户看到完整溯源]
```

### 关键认知

| 认知 | 实证 |
|---|---|
| ISV 字段必须挂主层 | modifyMeta(formId="haos_adminorgdetail" · 不是 homs_orgchgrecord) |
| 标品 buildData 是泛化的 | L396-L399 按 propertySet 动态 select · 自动适配 ISV 新字段 |
| 字段级溯源不需要写代码 | ISV 只要在配对场景写入 chgpageelement="${ISV_FLAG}_costcenter" · 本场景自动溯源 |
| BasedataField 显示 name | `formatValue L640-L646` 走 nameProperty(默认 "name") |

### 关联 CS

- CS-04 字段级变动溯源美化(本案例不需要 · 标品已自适配)
- 配对场景 `homs_orgbatchchgbill` 的 06_customization_solutions.md(写入端的字段扩展)

---

## 案例 4 · 用户从配对场景跳转回本场景看变动详情

### 业务场景

部门主管在 `homs_orgbatchchgbill` 详情页查看一张已生效申请单 · 想"立刻看到这张单子涉及的所有具体变动" · 不想自己跑去本场景再筛选。

### 业务流程

```
[用户在 homs_orgbatchchgbill 详情页]
   │
   ▼
点击 ISV 加的 toolbar 按钮"查看变动明细"(ISV 在配对场景挂的扩展按钮)
   │
   ▼
[配对场景 ISV 插件代码]
   ListShowParameter param = new ListShowParameter();
   param.setFormId("homs_orgchgrecord");
   param.setCustomParam("needshowdetail", true);
   param.setCustomParam("billidfilter", currentBillId);
   getView().showForm(param);
   │
   ▼
[本场景 AdminOrgChgRecordListPlugin.preOpenForm L173-L178]
   if (customParam.needshowdetail == true) {
       setSelectedEntity("subentryentity");  // 选中子分录维度
   }
   │
   ▼
[本场景 setFilter L160-L163]
   if (customParam.needshowdetail) {
       qFilterList.remove(0);  // 清掉默认权限过滤
       qFilterList.add(new QFilter("orgchgentry.chgbill.id", "=", billidfilter));
   }
   │
   ▼
列表显示该申请单的全部变动 entry · 子分录维度展开
```

### 关键技术点

| 点 | 实证 |
|---|---|
| 跨场景跳转 needshowdetail | 反编译 L173-L178(preOpenForm) + L160-L163(setFilter) |
| 子分录维度展开 | setSelectedEntity("subentryentity") 让 list 默认按子分录粒度展示 |
| 清掉默认数据权限 | `qFilterList.remove(0)` 然后 add billidfilter · 因为已限定 chgbill.id |
| 单 entry 视图 | needshowsingle 模式更细 · 只显示一条 entry 的变动 |

### 关联 CS

- CS-03 hyperLinkClick 双跳扩展(反向 · 从配对场景跳本场景)

---

## 案例 5 · 字段级溯源缺失"自定义实体"的扩展(CS-04 方案 B 实战)

### 业务场景

某 ISV 自建了一个"组织授权矩阵"实体 `${ISV_FLAG}_orgmatrix`(每个组织可关联多个授权角色) · 配对场景 ISV 已经能写 chgentitynumber="${ISV_FLAG}_orgmatrix" 到 t_homs_orgchgdetail · 但本场景看到的 changefield / beforevalue / aftervalue 都是空的 · 因为标品 buildData 不识别 ${ISV_FLAG}_orgmatrix。

### 业务流程

```
[问题表象]
   │
   ▼
本场景 list 显示"组织授权矩阵变动"行
   · changefield = (空)
   · beforevalue = (空)
   · aftervalue = (空)
   · 只有 chgentitynumber = "${ISV_FLAG}_orgmatrix" 这个内部字段有值
   │
   ▼
[排查]
   反编译 buildData L379-L406 三大分支:
       a) "haos_orgteamcooprel" → 协作变动(L384)
       b) ADMIN_STRUCT_KEY ("haos_adminorgstruct") → 矩阵变动(L390)
       c) 其他("haos_adminorgdetail" 等) → 普通变动(L396)
   │
   ├─ ISV 的 ${ISV_FLAG}_orgmatrix 走 case c 分支
   ├─ helper = new HRBaseServiceHelper("${ISV_FLAG}_orgmatrix")  ✅ 存在
   ├─ helper.query(propertySet 拼出来 · 假设 ISV 写入 chgpageelement="role")
   │     · 实际查 SELECT role FROM t_tdkw_orgmatrix WHERE id IN (...)
   │     · ✅ 字段查得出来
   │
   ├─ buildChangeMap → buildBaseChangeVO(L467)
   │     · property.getDisplayName().getLocaleValue() → 拿不到正确显示名
   │     · 因为 ISV 字段元数据上 displayName 没设(标品不会强制 ISV 设)
   │
   ▼
[解决方案 · CS-04 方案 B]
   ISV 并列挂 ListPlugin · 重写 packageData
   · 拦截标品已渲染的"空"结果
   · 自己拼装 changefield = "授权角色"(写死 · 或读自定义配置)
   · 自己查 t_tdkw_orgmatrix 拿 role 字段值 + 拼装 beforeValue / afterValue
```

### 实施步骤

```java
// 1. ISV 重写 beforePackageData · 批量加载 ${ISV_FLAG}_orgmatrix 数据
@Override
public void beforePackageData(BeforePackageDataEvent e) {
    super.beforePackageData(e);

    // 拿本页所有 chgentitynumber=${ISV_FLAG}_orgmatrix 的子分录 detail
    DynamicObjectCollection pageData = e.getPageData();
    List<Long> detailIds = collectTdkwDetailIds(pageData);

    if (detailIds.isEmpty()) return;

    // 查 t_homs_orgchgdetail 拿 before/after entity ID
    HRBaseServiceHelper detailHelper = new HRBaseServiceHelper("homs_subentryentity");
    DynamicObject[] details = detailHelper.query(
        "id,chgpageelement,beforechgentity,afterchgentity",
        new QFilter[]{new QFilter("id", "in", detailIds)}
    );

    // 一次性查 t_tdkw_orgmatrix 拿真实字段值
    Set<Long> entityIds = new HashSet<>();
    for (DynamicObject d : details) {
        entityIds.add(d.getLong("beforechgentity"));
        entityIds.add(d.getLong("afterchgentity"));
    }

    HRBaseServiceHelper matrixHelper = new HRBaseServiceHelper("${ISV_FLAG}_orgmatrix");
    DynamicObject[] matrixData = matrixHelper.query(
        "id,role.name",
        new QFilter[]{new QFilter("id", "in", entityIds)}
    );

    // 缓存到 ISV 自维护的 tdkwChangeMap(不污染标品 changeMap)
    populateTdkwChangeMap(details, matrixData);
}

// 2. ISV 重写 packageData · 把标品没填的列填上
@Override
public void packageData(PackageDataEvent e) {
    super.packageData(e);

    // 拿当前行的 chgentitynumber
    String chgEntity = e.getRowData().getString("orgchgentry.subentryentity.chgentitynumber");
    if (!"${ISV_FLAG}_orgmatrix".equals(chgEntity)) return;

    // 拿 ISV 缓存的 ChangeDetailVO
    long detailId = e.getRowData().getLong("orgchgentry.subentryentity.id");
    TdkwChangeDetailVO vo = tdkwChangeMap.get(detailId);
    if (vo == null) return;

    String key = ((TextColumnDesc) e.getSource()).getKey();
    switch (key) {
        case "changefield":  e.setFormatValue("授权角色"); break;
        case "beforevalue":  e.setFormatValue(vo.getBeforeValue()); break;
        case "aftervalue":   e.setFormatValue(vo.getAfterValue()); break;
    }
}
```

### 关键技术点

| 点 | 实证 |
|---|---|
| 不污染标品 changeMap | ISV 维护独立 tdkwChangeMap(PR-001 · 并列不继承)|
| 批量查询不 N+1 | beforePackageData 阶段一次性查所有 entity · packageData 单行只读缓存 |
| 跨场景写入端配合 | 必须先在配对场景的 OrgBatchChgBillEffectOp 扩展加上 ${ISV_FLAG}_orgmatrix 写入逻辑 |
| 关联 PR | PR-001 · 不继承 AdminOrgChgRecordListPlugin · 也不污染其内部 Map |

### 关联 CS

- CS-04 方案 B 自定义 chgentitynumber 溯源
- 配对场景的 06_customization_solutions.md(写入端扩展)

---

## 关联文档

- [`02_business_rules.md`](02_business_rules.md) · 业务规则 + 字段语义
- [`04_business_flow.md`](04_business_flow.md) · 业务流程总览
- [`05_data_flow.md`](05_data_flow.md) · 写入路径 + 查询路径
- [`06_customization_solutions.md`](06_customization_solutions.md) · CS-01..CS-06
- [`knowledge/scenarios/homs_orgbatchchgbill_maintenance/09_cases.md`](../homs_orgbatchchgbill_maintenance/09_cases.md) · ⭐ 配对场景案例(必对照)
