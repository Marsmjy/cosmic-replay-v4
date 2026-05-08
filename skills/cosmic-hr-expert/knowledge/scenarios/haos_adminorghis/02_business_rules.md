# 业务规则 · 组织历史查询（haos_adminorghis）

> **状态**：基于 7 个反编译类 + 36 opKey 实证 · 时序规则核心
> **confidence**：verified
> **审计时间**：2026-04-27

---

## 1. HisModel 时序规则总纲（必读 · PR-008 + PR-009）

本场景查的所有数据**都是 HisModel 时序资料** · 物理表共用 `t_haos_adminorg`（与 admin_org_quick_maintenance 共用）· 时序语义由 `hbp_histimeseqtpl`（HR 基础资料全页面历史模板）继承下来 · 时序字段 28 个集中在该层（`hbp_histimeseqtpl`）。

### 1.1 七大核心时序字段（**精通它们 · ISV 扩展不能踩**）

| 字段 | 类型 | 含义 | 反编译实证 | PR 引用 |
|---|---|---|---|---|
| `boid` | BigIntField | 业务对象 ID · 所有历史版本共用 | `AdminOrgPageRightDynamicPlugin.beforeBindData L51`：`customParam.put("boid", ...)` 用作下游引用维度 | **PR-009** |
| `iscurrentversion` | CheckBoxField | true=当前版本 / false=历史版本 | `HisModelListCommonPlugin.setFilter L177-L178`：本场景强制 `iscurrentversion=false` | **PR-008** |
| `hisversion` | TextField | 版本号 · 递增 · 用于排序 | `HisModelListCommonPlugin.beforeCreateListColumns L126`：VERSION_LIST_PAGE setOrder DESC | - |
| `sourcevid` | BigIntField | 链向上一版本的 ID · 链表结构 | `HisModelFormCommonPlugin.afterDoOperation L201,L215`：reviserecord 时 `sourcevid` 决定 pkId 归属 | - |
| `firstbsed` | DateField | 该 boid 最早生效日期 · 跨所有版本 | `HisModelListCommonPlugin.beforeCreateListColumns L112` ONLY_ONE_EFFECT_VERSION 模式时被移除 | - |
| `bsed` | DateField | 当前版本生效日期（区间起点）| `HisModelFormCommonPlugin.beforeDoOperation L150` REVISE_VERSION_PAGE 检查 bsled · `AdminOrgInitSaveOp.endOperationTransaction readFields=[bsed]` | - |
| `bsled` | DateField | 当前版本失效日期（区间终点）| 同上 L150：失效日期不能早于 maxEffEndDate | - |

### 1.2 派生时序字段

| 字段 | 含义 | 反编译实证 |
|---|---|---|
| `datastatus` | ComboField · 数据版本状态（TEMP / 待生效 / 生效中 / 过期 / 废弃 ...）| `HisModelListCommonPlugin.packageData L149-L162`：按 datastatus 决定显示哪些行级操作按钮（modify/hiscopy/confirmchange/revise）|
| `changedescription` | TextField · 变动说明 | `HisModelListCommonPlugin.beforeCreateListColumns L116`：NOT_HIS_PAGE 模式被移除 |

---

## 2. 历史查询模式三态规则（HisPageEnum）

反编译实证：`HisModelListCommonPlugin.filterContainerInit L92-L96` + `HisModelListCommonPlugin.setFilter L173-L181` + `HisModelListCommonPlugin.beforeCreateListColumns L115-L139`。

| HisPageEnum | 列表过滤行为 | 列展示规则 |
|---|---|---|
| `NOT_HIS_PAGE` | 强制 `iscurrentversion=true`（查当前版本） | 移除 hisversion / changedescription / datastatus / bsed / bsled / hisoperation 6 列 |
| `VERSION_LIST_PAGE`（**本场景**） | 强制 `iscurrentversion=false` + `removeIf ctrlstrategy` | hisversion DESC（间断时序 NO_INTERRUPTION_NO_OVERLAP 例外用 bsed/modifytime）+ 移除 status/firstbsed/issyspreset · 在 hisoperation 列前插入 modifier.name + modifytime 操作人列 |
| `VERSION_PAGE` | （详情页 · 单条）| - |

**关键认知**：本场景的 list 默认 HisPageEnum = VERSION_LIST_PAGE · ISV 想改 list 行为必须**重写 `beforeCreateListColumns`**（CS-02 排序定制 / 列移除）· 不能从 setFilter 改 iscurrentversion（被标品 final 方法兜底）。

---

## 3. HisModelTypeEnum 三种时序模式（关键边界条件）

反编译实证：`HisModelListCommonPlugin.beforeCreateListColumns L120 + HisModelF7ListPlugin.setFilter L80,L109,L126,L139,L194` 等多处。

| 枚举 | 含义 | 业务约束 |
|---|---|---|
| `ONLY_ONE_EFFECT_VERSION` | 只有一个生效版本 | 移除 bsed/bsled/firstbsed 列 · F7 强制 `iscurrentversion=true` |
| `NO_INTERRUPTION_NO_OVERLAP`（**行政组织标准模式**）| 无中断无重叠 | bsed 紧接前版 bsled · 时序连续 · disable/enable 影响版本派生 |
| 默认（间断模式）| 允许时间间断 | hisversion 直接 DESC 排序 · 时序可断 |

---

## 4. 历史版本不可修改铁律

### 4.1 详情页 VIEW 模式实证

`AdminOrgDetailEditPlugin.beforeBindData L184-L191`：
```
String operation = (String) showParameter.getCustomParam("adminorg_operation");
if (operation == null) {  // ← VIEW 模式
    setVisible(false, "new_save", "change_save", "parent_save",
                      "newentry", "deleteentry", "tobedisableflag",
                      "cancel", "parentorg_name");
    setVisible(true, "fullname");
    if (OperationStatus.VIEW.equals(showParameter.getStatus())) {
        setVisible(true, "bar_close");
    }
    return;  // ← 直接 return · 不再走 addnew/infochg/parentchg 分支
}
```

**含义**：用户从历史列表点开任一行 · 都走这个分支 · 所有写入按钮被隐藏 · 仅"关闭"和"全名"可见。

### 4.2 历史版本要"补正" 走 revise（修订）派生新版本

`HisModelFormCommonPlugin.afterDoOperation L198-L209`（case "revise"）：
```
if (operationResult.isSuccess()) {
    if (this.getModelVal("id").equals(this.getModelVal("boid"))) {
        // 当前版本：走 sourcevid 派生
        formProcessor.showHisVersionEditPage(getModel().getValue("sourcevid"), true);
    } else {
        // 历史版本：直接派生新版本
        formProcessor.showHisVersionEditPage(getModel().getValue("id"), false);
    }
}
```

**含义**：revise 操作产生**新版本** · 不会原地修改老历史 · 老版本的 bsled 会被自动收缩，新版本接续 bsed。

---

## 5. `his_save` 历史补录的 10 条 Validator 规则（按调用链）

反编译实证：`AdminOrgInitSaveOp.onAddValidators` 注册 10 个 Validator · 见 `opkeys/his_save.json validators[]`。

| ID | Validator 类名 | 校验语义 | 触发场景 |
|---|---|---|---|
| V_AdminOrgInitSaveOp_1 | OrgHisBelongCompanyValidator | 归属公司必须存在且生效 | 补录人指定 belongcompany |
| V_AdminOrgInitSaveOp_2 | OrgHisEffDateContinuityValidator | 生效日期连续性 · 跟前一版本 bsled+1 衔接 | NO_INTERRUPTION_NO_OVERLAP 模式必触 |
| V_AdminOrgInitSaveOp_3 | OrgHisEffDateLegitimacyValidator | 生效日期合法 · 不晚于失效日期 | 所有版本 |
| V_AdminOrgInitSaveOp_4 | OrgHisEndDateRangeValidator | 失效日期范围 · ≤ 平台最大日期（TimeLineServiceUtil.getMaxEffEndDate）| 所有版本 |
| V_AdminOrgInitSaveOp_5 | OrgHisEndDateValidator | 失效日期合法 · 不能空 | 所有版本 |
| V_AdminOrgInitSaveOp_6 | OrgHisFirstEffDateConsistencyValidator | firstbsed 一致性 · 同 boid 所有版本 firstbsed 必须一样（最早版本的 bsed）| 同 boid 多版本 |
| V_AdminOrgInitSaveOp_7 | OrgHisMigratedValidator | 已迁移 · 防止从其他系统已迁移过来的数据被反向覆盖 | 数据迁移项目 |
| V_AdminOrgInitSaveOp_8 | OrgHisOrgCurrVerParentValidator | 当前版本父组织合法 · 当前版本 parentorg 必须存在 | 关联当前版本 |
| V_AdminOrgInitSaveOp_9 | OrgHisOrgErrValidator | 组织错误兜底校验 · 综合错误判断 | 所有 |
| V_AdminOrgInitSaveOp_10 | OrgHisOrgParentValidator | 父组织合法 · parentorg 同时段（bsed/bsled 区间）有效 | 所有 |

**ISV 扩展边界**：
- 这 10 个 Validator 在 `AdminOrgInitSaveOp` 类内 inner class 注册 · 标品禁继承 `AdminOrgInitSaveOp`
- 想加自己的"租户内补录互斥"校验 → **并列挂新 OP**（继承 `HRDataBaseOp`）· 在 `onAddValidators` 添自己的 `AbstractValidator`（PR-001）
- 见 CS-04（拦截 his_save · 校验补录历史前 bsed 不冲突）

---

## 6. 历史版本对比规则（versionchangecompare）

反编译实证：`HisModelListCommonPlugin.afterDoOperation L232-L238`：
```
case "versionchangecompare":
    if (!args.getOperationResult().isSuccess()) return;
    Set selectedRowIds = getSelectedRows().stream()
                          .map(ListSelectedRow::getPrimaryKeyValue)
                          .collect(Collectors.toSet());
    showFormProcessor.showVersionChangeCompareList(selectedRowIds);
```

**业务规则**：
- 选 ≥ 2 行历史版本 · 触发对比页
- 对比维度：每个版本的字段值快照（默认按字段 key 排）
- 主键 → 走的是 id（具体版本 id）· 不是 boid · 因为对比的是版本快照

---

## 7. 历史筛选 effectdate 联动规则

反编译实证：`HisModelF7ListPlugin.beforeBindData L189-L210`：
```
if ("effDate".equals(hisModelF7PageParam.getEffDateFieldType())
    && hisModelF7PageParam.getEffectDate() == null) {
    getModel().setValue("effectdate", HisModelCommonUtil.getToday());
} else if ("effDateRange".equals(hisModelF7PageParam.getEffDateFieldType())
    && hisModelF7PageParam.getEffDateStart() == null
    && hisModelF7PageParam.getEffDateEnd() == null) {
    getModel().setValue("effdatestart", HisModelCommonUtil.getToday());
    getModel().setValue("effdateend", TimeLineServiceUtil.getMaxEffEndDate());
}
```

**规则**：
- F7 第一次打开 · 如果 `effDateFieldType = effDate` 且 effectDate 没传 → 默认填今天
- F7 第一次打开 · 如果 `effDateFieldType = effDateRange` 且 effDateStart/effDateEnd 都没传 → 默认填 [今天, 平台最大日期]
- 用户改 effectdate / effdatestart / effdateend 任一字段 → `propertyChanged L153-L186` 触发 listView.refresh()

---

## 8. F7 选择时的"当前编号/名称"展示规则

反编译实证：`HisModelF7ListPlugin.setFilter L101-L107`：
```
if (!isBoF7() && getHisModelF7PageParam().getShowCurrentNumAndName()
    && !currentQFilters.isEmpty()) {
    HRBaseServiceHelper helper = new HRBaseServiceHelper(getEntityNumber());
    QFilter versionQFilter = new QFilter("iscurrentversion", "=", Boolean.TRUE);
    currentQFilters.add(versionQFilter);
    DynamicObjectCollection boCol = helper.queryOriginalCollection("id", currentQFilters);
    QFilter newSearchFilter = new QFilter("boid", "in",
        boCol.stream().map(bo -> bo.get("id")).collect(Collectors.toSet()));
    currentQFilters.forEach(qf -> qf.or(newSearchFilter));
}
```

**规则**：F7 选历史版本时 · 如果勾了"显示当前编号/名称" · 系统先查当前版本（iscurrentversion=true）的 boid · 再 OR 拼到原过滤里 → 让历史版本能根据"当前的编码/名称"被搜到（用户记的可能是最新名 · 但要找的是历史版本）。

---

## 9. 数据状态决定行级按钮可见性

反编译实证：`HisModelListCommonPlugin.packageData L142-L165`：

| datastatus 值 | 可见行级按钮 |
|---|---|
| `TEMP`（暂存）| modify / hiscopy / confirmchange |
| `DISCARDED`（已废弃）| 仅 hiscopy（**不可 revise**）|
| 其他（待生效 / 生效中 / 已过期 ...）| hiscopy / revise |

**ISV 扩展**：CS-05 行级样式定制需要按 `iscurrentversion` 与 `datastatus` 双重判断 · 见 `06_customization_solutions.md`。

---

## 10. 改名链查询的特殊规则（namehistory / namehistoryview）

opType = `donothing` · 但实际由平台 `kd.bos.form.plugin.nameversion.BaseDataNameVersionListPlugin` 接管：
- 解析 `t_haos_adminorg_l.fname` 多语言字段 · 抽出每次 name 变化的版本 → 形成"改名链"
- 与 hisversion 维度独立（hisversion 是字段全量快照 · namehistory 仅追 name 字段）

---

## 11. 跨场景规则（与 admin_org_quick_maintenance 协作）

| 规则 | 实现位置 | 验证 |
|---|---|---|
| 用户在 admin_org_quick 列表点 boid · 点"历史"按钮 → 跳本场景 | 标品 ListColumn 加链 · 跳 `haos_adminorghis` 入口 + 带 `customParam.boid=<X>` | 反编译 `OrgDetailList.billListHyperLinkClick L95-L151` 含跳转分支 |
| 本场景 revise 后 → admin_org_quick 列表数据自动刷新 | `HisModelFormCommonPlugin.afterDoOperation case "save" L240-L249`：调 `getView().invokeOperation("refresh")` | L243 |
| F7 选历史版本时 · 把当前版本的 number/name 一起 join 出来 | `HisModelF7ListPlugin.beforePackageData L121-L132` 调 `queryCurrentNumberAndName()` | 实证 |
| 历史补录后 admin_org_quick 当前版本字段不动 | his_save 仅写 t_haos_adminorg 历史数据行（iscurrentversion=false）· 不动 iscurrentversion=true 行 | OrgHisOrgCurrVerParentValidator 校验当前版本仍合法 |

---

## 12. 已知 listRule 实抓（formRule · 仅 2 条 · `_auto_rules.md`）

| ruleId | 类型 | preCondition | 描述 |
|---|---|---|---|
| `2VUO4HATJ+HV` | formRule | `enable = '0'` | 组织已停用（视图侧的提示标识规则）|
| `4SOWUAQJNXPH` | formRule | `changescene.id > 0` | 变动场景（变动场景已选时的视图行为规则）|

**说明**：本场景配置的 formRule 只有 2 条 · 真正的业务规则 99% 通过反编译插件实现（前述 1-11 节）。

---

## 13. 总结：6 条不可逾越的铁律

1. **历史版本只读** · 想改 → revise 派生新版本（PR-008 + 4.2 节）
2. **boid 是业务维度** · 跨版本一致 · 下游引用查询必须用 boid · 不是 id（PR-009）
3. **本场景永远查 `iscurrentversion=false`** · 标品 final 方法写死（HisModelListCommonPlugin.setFilter L177-L178）
4. **firstbsed 跨版本一致** · OrgHisFirstEffDateConsistencyValidator 把关
5. **bsed/bsled 区间无重叠** · NO_INTERRUPTION_NO_OVERLAP 模式 OrgHisEffDateContinuityValidator 把关
6. **物理表共用 `t_haos_adminorg`** · 跟 admin_org_quick_maintenance 共用 · 改字段类型/长度会双场景同时受影响

---

## 14. 关联文档

- `03_model_design.md` · 字段时序分组与共用物理表
- `06_customization_solutions.md` · CS-04（拦截 his_save 加自定义校验）
- `knowledge/_shared/platform_rules.json` · PR-008 / PR-009（核心引用）
- `knowledge/scenarios/admin_org_quick_maintenance/02_business_rules.md` · 当前版本规则对照

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

## chgaction 实证补充（HisModelFormCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin -->

## chgaction 实证补充（NewAdminorgDetailEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("NewAdminorgDetailEditPlugin", parseException.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin -->

## chgaction 实证补充（AdminOrgPageRightDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("AdminOrgPageRightDynamicPlugin", e.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

## chgaction 实证补充（HRRelatePageRightDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | e.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin -->

## chgaction 实证补充（AdminOrgDetailEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin`
> 跨类追踪: 29 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("AdminOrgDetailEditPlugin", exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

## chgaction 实证补充（HisModelListCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

## chgaction 实证补充（HisModelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

## chgaction 实证补充（HisModelFilterPanelListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

## chgaction 实证补充（HisModelFilterPanelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.OrgDetailList -->

## chgaction 实证补充（OrgDetailList 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.OrgDetailList`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.OrgDetailList/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("OrgDetailList", parseException.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.OrgDetailList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin -->

## chgaction 实证补充（AdminOrgDetailBUListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin -->

## chgaction 实证补充（AdminOrgDetailListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin`
> 跨类追踪: 26 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("closedCallBack", exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin -->

## chgaction 实证补充（AdminOrgDisableAndIncludeFilterListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgHisListPlugin -->

## chgaction 实证补充（AdminOrgHisListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgHisListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgHisListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgHisListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

## chgaction 实证补充（HisModelMobileListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

## chgaction 实证补充（HisModelOPCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelAttachmentService_1` | 实体编码不能为空。 |
| `HisModelAttachmentService_2` | 数据id不能为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp -->

## chgaction 实证补充（AdminOrgFastAuditOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

## chgaction 实证补充（HisUniqueValidateOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp -->

## chgaction 实证补充（AdminOrgInitSaveOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->

## chgaction 实证补充（HisBaseDataF7FastFilter 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->
