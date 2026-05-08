# 扩展点矩阵 · 组织变动明细查询(homs_orgchgrecord)

> **状态**: 基于 15 opKey + 7 plugin + AdminOrgChgRecordListPlugin 的 25 方法签名实证
> **confidence**: verified
> **审计时间**: 2026-04-27

---

## 1. 扩展点速查表

| 类型 | 数量 | 说明 |
|---|---|---|
| OpKey 扩展点 | 15 | 全部为只读 / 翻页 / 导出 / 关闭 类 · 无写入 OP |
| 表单插件方法（FormPlugin lifecycle）| 25(AdminOrgChgRecordListPlugin) + 1(AdminOrgChgRecordBUListPlugin) | list 视图相关 |
| ISV 可继承的 SDK 父类 | 4 | HRDataBaseList / HRDataBaseEdit / HRDataBaseOp / HRDynamicFormBasePlugin |
| ISV **禁继承**的标品类 | 4 | AdminOrgChgRecordListPlugin(final) / AdminOrgChgRecordBUListPlugin(final) / AbstractBUListPlugin(无注解) / OrgBatchChgBillEffectOp(场景专属) |
| 共享 plugin (来自 hbp 模板) | 5 | HRBaseDataImportEdit / HRCertCheckEdit / HRBaseUeEdit / HRHiesButtonSwitchPlugin / HRCertCheckList |

---

## 2. 15 opKey 扩展点矩阵

| # | opKey | 名称 | opType | extValue | 写入 | ISV 可挂插件 | 推荐 CS |
|---|---|---|---|---|---|---|---|
| 1 | `first` | 第一 | first | 3 | 否 | ❌(视图层翻页 · 标品兜底) | - |
| 2 | `previous` | 前一 | previous | 3 | 否 | ❌ | - |
| 3 | `next` | 后一 | next | 3 | 否 | ❌ | - |
| 4 | `last` | 最后 | last | 3 | 否 | ❌ | - |
| 5 | `save` | 保存 | save | 3 | 否(兜底·从不触发) | ❌(用户操作不到 · 不要在此挂插件浪费) | - |
| 6 | `importdata_hr` | 导入数据 | importdata_hr | 3 | **是**(HIES 罕见) | ⚠(HIES ImportPlugin · ISV 罕见需求) | - |
| 7 | `show_import_record_hr` | 查看导入记录 | show_import_record_hr | 3 | 否 | ❌ | - |
| 8 | `export_from_list_hr` | 按列表导出 | export_from_list_hr | 3 | 否 | ⚠(HIES ExportPlugin · ISV 可定制导出格式) | - |
| 9 | `export_from_impttpl_hr` | 按导入模板导出 | export_from_impttpl_hr | 3 | 否 | ⚠ | - |
| 10 | `export_from_expttpl_hr` | 按导出模板导出 | export_from_expttpl_hr | 3 | 否 | ⚠ | - |
| 11 | `show_export_record_hr` | 查看导出记录 | show_export_record_hr | 3 | 否 | ❌ | - |
| 12 | `refresh` | 刷新 | refresh | 3 | 否 | ✅(FormPlugin afterDoOperation) | CS-01 / CS-04 |
| 13 | `exportlistbyselectfields` | 导出数据(按列表) | exportlist | 3 | 否 | ⚠(平台 ExportPlugin) | - |
| 14 | `exportdetails` | 查看导出结果 | exportdetails | 3 | 否 | ❌ | - |
| 15 | `close` | 关闭 | close | 3 | 否 | ❌ | - |

> **关键观察**: 本场景 15 opKey 中 ISV 真正能挂插件做业务的只有 **`refresh`** (list 刷新触发) · 其他全是 BaseFormModel / HIES 模板兜底操作。

> **ISV 主要扩展位置**不在 opKey · 而在 **list FormPlugin 生命周期方法**(setFilter / beforePackageData / packageData / billListHyperLinkClick / itemClick)。

---

## 3. FormPlugin 生命周期扩展点

### 3.1 AdminOrgChgRecordListPlugin · 25 方法签名实证

| 方法 | 反编译行号 | ISV 是否可重写 | 推荐用法 |
|---|---|---|---|
| `setFilter(SetFilterEvent)` | L155-L171 | ✅ 是 | 追加 ISV QFilter (CS-01 / CS-05) |
| `preOpenForm(PreOpenFormEventArgs)` | L173-L178 | ✅ 是 | 拦截跨场景跳入 · 检查 customParam |
| `afterDoOperation(AfterDoOperationEventArgs)` | L180-L185 | ✅ 是 | 在 refresh / 其他 op 完成后做后处理 |
| `replaceProperty(List<QFilter>)` | L187-L198(private) | ❌ 否(私有) | 标品行为 · ISV 不能重写 |
| `billListHyperLinkClick(HyperLinkClickArgs)` | L200-L218 | ✅ 是 | 自定义跳转分支 (CS-03) |
| `beforeCreateListDataProvider(BeforeCreateListDataProviderArgs)` | L220-L222 | ✅ 是(理论上) | 替换 ListDataProvider (高风险 · 慎用) |
| `beforeCreateListColumns(BeforeCreateListColumnsArgs)` | L224-L229 | ✅ 是 | 改列宽 / 排序 / 隐藏列 (CS-02) |
| `beforePackageData(BeforePackageDataEvent)` | L232-L252 | ✅ 是 | 批量加载渲染依赖数据 (CS-04) |
| `buildSplitMerge(List<Long>)` | L254-L302(private) | ❌ 否(私有) | 标品合并/拆分溯源逻辑 · ISV 不能重写 |
| `packageData(PackageDataEvent)` | L304-L324 | ✅ 是 | 改单行渲染(行级样式 / 自定义计算列) (CS-04) |
| `formatTextValue(PackageDataEvent)` | L326-L362(private) | ❌ 否(私有) | 标品字段级溯源渲染 · ISV 不能重写 |
| `buildData(List<Long>)` | L364-L406(private) | ❌ 否(私有) | 字段级溯源数据构建 · ISV 不能重写 |
| `buildChangeMap(...)` | L408-L430(private) | ❌ 否(私有) | changeMap 构建 · ISV 不能重写 |
| `getChangeDetailVO(...)` | L432-L457 | ✅ 是(public) | 但实际 ISV 重写没意义(不被外部调用) |
| `buildCoolChangeVO` / `buildStructProjectChangeVO` / `buildBaseChangeVO` | L459-L490 | private / private / private | ISV 不能重写 |
| `getEnabledLangList` | L492-L497(private) | ❌ 否 | 多语言列表缓存 · ISV 不能重写 |
| `buildChangeVO(...)` | L499-L544(private) | ❌ 否 | ChangeVO 构建 · ISV 不能重写 |
| `getMultiBaseDataString*` | L546-L574(private) | ❌ 否 | 多基础资料字符串拼装 · ISV 不能重写 |
| `appendValue(DynamicObject, String)` | L576-L595(private) | ❌ 否 | "名称(编码)"格式化 · ISV 不能重写 |
| `formatString(String)` / `formatValue(Object, String)` | L597-L653(private) | ❌ 否 | ISV 不能重写 |

### 3.2 ISV 推荐重写方法(5 个核心)

| 方法 | 推荐场景 |
|---|---|
| `setFilter(SetFilterEvent)` | CS-01 加自定义筛选 / CS-05 高级筛选弹窗 |
| `beforeCreateListColumns(BeforeCreateListColumnsArgs)` | CS-02 改默认排序 |
| `billListHyperLinkClick(HyperLinkClickArgs)` | CS-03 自定义跳转 |
| `beforePackageData(BeforePackageDataEvent)` | CS-04 批量加载 ISV 自定义数据 |
| `packageData(PackageDataEvent)` | CS-04 改行渲染 / 加自定义列 |

### 3.3 itemClick / closedCallBack(toolbar 按钮)

`HRDataBaseList` 父类支持的方法:

| 方法 | 用途 |
|---|---|
| `registerListener(EventObject)` | 注册 toolbar 按钮监听 · `addItemClickListeners(itemKey)` |
| `itemClick(ItemClickEvent)` | toolbar 按钮点击 · CS-05 弹高级筛选弹窗 |
| `closedCallBack(ClosedCallBackEvent)` | 弹窗关闭后接收返回值 · CS-05 |

---

## 4. AdminOrgChgRecordBUListPlugin · 1 方法签名

| 方法 | 反编译行号 | ISV 是否可重写 |
|---|---|---|
| `getCtrlBUFieldSet()` | L19 | ❌ 否(**禁继承本类**) |

> **不能继承的原因**:
> 1. `AdminOrgChgRecordBUListPlugin` 是 `public final class`(反编译 L15 实证) · 编译过不去
> 2. 父类 `AbstractBUListPlugin`(haos 域)也**禁继承**(`cosmic_hr_sdk_whitelist_audit.md` 不在白名单 · 无 SDK 注解)

> **ISV 想加 BU 联动字段** → 不能直接重写本插件 · 只能通过 `setFilter` 阶段读 view.getFilterContainer 的过滤条件做替代联动(不优雅 · 但唯一可行)。

---

## 5. ISV 可继承的 SDK 父类(4 个)

### 5.1 HRDataBaseList(标准 list 父类) ⭐ 最常用

- **来源 jar**: `hrmp-hbp-formplugin-1.0.jar`
- **FQN**: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- **注解**: @SdkPlugin
- **场景**: 本场景 99% 的 ISV 扩展都用这个父类
- **关键方法**: 见 §3.1 / §3.2

### 5.2 HRDataBaseEdit(详情页 / 表单插件)

- **来源 jar**: `hrmp-hbp-formplugin-1.0.jar`
- **FQN**: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- **注解**: @SdkPlugin
- **场景**: 如果 ISV 自建变动详情查看页(自定义 form) · 用此父类
- **关键方法**: preOpenForm / beforeBindData / afterBindData / propertyChanged

### 5.3 HRDynamicFormBasePlugin(动态表单父类)

- **来源 jar**: `hrmp-hbp-formplugin-1.0.jar`
- **FQN**: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- **注解**: @SdkPlugin
- **场景**: CS-05 高级筛选弹窗 · CS-04 字段溯源美化页(如果做成独立弹窗)

### 5.4 HRDataBaseOp(OP 父类) ⭐ CS-06 核心

- **来源 jar**: `hrmp-hbp-opplugin-1.0.jar`
- **FQN**: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- **注解**: @SdkPlugin
- **场景**: CS-06 在 `homs_orgbatchchgbill` 配对场景挂 OP 监听变动(不在本场景挂 OP · 本场景没写入 OP)

---

## 6. ISV 禁继承的标品类(4 个 · 铁律)

| 类 | 原因 | 替代方案 |
|---|---|---|
| `AdminOrgChgRecordListPlugin` | `public final class`(反编译 L117) · 标品场景专属 · 即使非 final 也违反 PR-001 | 继承 `HRDataBaseList` · 重写需要的方法 |
| `AdminOrgChgRecordBUListPlugin` | `public final class`(反编译 L15) · 同上 | (场景特殊 · 几乎不需要 ISV 扩展 BU 联动) |
| `AbstractBUListPlugin`(kd.hr.haos.formplugin.web.adminorg.template) | haos 域基类 · `cosmic_hr_sdk_whitelist_audit.md` 不在白名单 · 无 SDK 注解 | 用 `HRDataBaseList` 替代 + setFilter 阶段实现等效 |
| `OrgBatchChgBillEffectOp`(配对场景) | 场景专属 OP · 标品升级风险 · PR-001 | 继承 `HRDataBaseOp` · 在 audit/submiteffect 并列挂 |

> 详见 [knowledge/_sdk_audit/cosmic_hr_sdk_whitelist_audit.md](../../_sdk_audit/cosmic_hr_sdk_whitelist_audit.md)

---

## 7. 共享 plugin(来自 hbp 模板 · 自动挂载)

这 5 个 plugin 是父模板 `hbp_bd_originalmintpl` 自带 · ISV 不要去重复挂 · 但要知道它们存在:

| # | 类 | 父类 | 用途 |
|---|---|---|---|
| 1 | `HRBaseDataImportEdit` | HRCoreBaseBillEdit | HR 基础资料导入向导 form |
| 2 | `HRCertCheckEdit` | HRDataBaseEdit | HR 证书检查 edit |
| 3 | `HRBaseUeEdit` | HRDataBaseEdit | HR 基础资料 UE 编辑 |
| 4 | `HRHiesButtonSwitchPlugin` | AbstractFormPlugin | HIES 按钮开关切换 · `afterBindData` |
| 5 | `HRCertCheckList` | AbstractListPlugin | HR 证书检查 list |

> 这 5 个里只有 `HRHiesButtonSwitchPlugin.afterBindData` 实际跑 · 其他 4 个仅在特定子场景(HIES 导入向导)触发。

---

## 8. ISV 扩展点决策树

```
ISV 想做的事
  │
  ├─ 加列表筛选条件
  │     → HRDataBaseList.setFilter(CS-01 / CS-05)
  │
  ├─ 改列表排序 / 列宽 / 隐藏列
  │     → HRDataBaseList.beforeCreateListColumns(CS-02)
  │
  ├─ 改 hyperLinkClick 跳转目标
  │     → HRDataBaseList.billListHyperLinkClick(CS-03)
  │
  ├─ 加列表行级样式
  │     → HRDataBaseList.packageData(CS-04 方案 A)
  │
  ├─ 加自定义计算列 / 自定义溯源类型
  │     → HRDataBaseList.beforePackageData + packageData(CS-04 方案 B)
  │
  ├─ 加 toolbar 按钮 / 弹高级筛选
  │     → HRDataBaseList.itemClick + closedCallBack(CS-05)
  │
  ├─ 监听"组织变动生效"事件
  │     → ⚠ 不在本场景挂 · 去 homs_orgbatchchgbill 挂 HRDataBaseOp.afterExecuteOperationTransaction(CS-06)
  │
  ├─ 加新字段(成本中心等)
  │     → modifyMeta 打到 haos_adminorgdetail 主层(不是 homs_orgchgrecord)
  │
  └─ 修改变动记录数据
        → ❌ 反模式 · 走配对场景 homs_orgbatchchgbill 反向调整单
```

---

## 9. opKey 与插件链关系图

```
用户操作 (15 opKey)
   │
   ▼
[OperationServicePlugIn 链]
   │
   ├─ refresh / exportlistbyselectfields / exportdetails / close
   │     │
   │     └─ 平台兜底 · 无标品 OP
   │
   ├─ first / previous / next / last
   │     │
   │     └─ 平台 BdVersionListPlugin / 视图翻页处理 · 无标品 OP
   │
   ├─ save (兜底·从不触发)
   │     │
   │     └─ 平台 SaveOp(被 BaseFormModel 模板挂)
   │
   ├─ importdata_hr / show_import_record_hr / export_from_*_hr / show_export_record_hr
   │     │
   │     └─ HIES 标品 ImportPlugin / ExportPlugin
   │
   ▼
[FormPlugin 链]
   │
   ├─ HRHiesButtonSwitchPlugin.afterBindData (HIES 按钮切换)
   │
   ├─ AdminOrgChgRecordBUListPlugin.getCtrlBUFieldSet (BU 联动)
   │
   └─ AdminOrgChgRecordListPlugin (主插件 · 25 方法)
        ├─ preOpenForm
        ├─ setFilter ⭐ ISV 扩展首选
        ├─ beforeCreateListColumns ⭐ ISV 扩展(排序/列)
        ├─ beforeCreateListDataProvider
        ├─ beforePackageData ⭐ ISV 扩展(批量加载)
        ├─ packageData ⭐ ISV 扩展(行渲染)
        ├─ billListHyperLinkClick ⭐ ISV 扩展(跳转)
        ├─ afterDoOperation
        └─ (其他私有方法 · ISV 不能重写)
```

---

## 10. 关联文档

- [`02_business_rules.md`](02_business_rules.md) · 业务规则 + 数据可见性
- [`04_business_flow.md`](04_business_flow.md) · 用户使用流程 + 调用栈
- [`06_customization_solutions.md`](06_customization_solutions.md) · CS-01..CS-06
- [`08_impact_analysis.md`](08_impact_analysis.md) · 改本场景对配对场景的影响
- [`knowledge/_sdk_audit/cosmic_hr_sdk_whitelist_audit.md`](../../_sdk_audit/cosmic_hr_sdk_whitelist_audit.md) · SDK 白名单铁律
- [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json) · PR-001 / PR-005 / PR-009 / PR-010 / PR-011
