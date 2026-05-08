# 异常诊断 · 组织历史查询（haos_adminorghis）

> **状态**：基于反编译 ResId + 10 OrgHis*Validator + 时序铁律 · 异常码与诊断方法
> **confidence**：verified
> **审计时间**：2026-04-27

---

## 1. 时序错误码（来自反编译 ResId）

### 1.1 失效日期不能早于 maxEffEndDate

**ResId**：`HisModelFormCommonPlugin_0`
**反编译实证**：`HisModelFormCommonPlugin.beforeDoOperation L150-L153`
```java
String.format(ResManager.loadKDString("失效日期不能早于%s",
    "HisModelFormCommonPlugin_0", "hrmp-hbp-formplugin"),
    HRDateTimeUtils.format(maxEffEndDate, "yyyy-MM-dd"));
```

**触发场景**：`save` 操作 · `hisPage = REVISE_VERSION_PAGE` · `HisModelTypeEnum.NO_INTERRUPTION_NO_OVERLAP` · 同 boid 已有 2 个版本 · 用户填的 bsled 早于平台最大日期。

**诊断**：检查用户填的失效日期是否 ≥ TimeLineServiceUtil.getMaxEffEndDate()。

**修复**：让失效日期等于平台最大日期（默认 2999-12-31）· 或重新拉时序链。

---

### 1.2 OrgHis* Validator 系列错误（10 个）

来自 `AdminOrgInitSaveOp.onAddValidators` 注册。每个 Validator 触发的错误信息以反编译 jar 为准 · 通用模式：

| Validator | 错误模板 | 业务诊断 |
|---|---|---|
| OrgHisBelongCompanyValidator | "归属公司 [{name}] 不存在或已禁用" | 检查 belongcompany 是否填了已禁用的组织 |
| OrgHisEffDateContinuityValidator | "生效日期与前一版本失效日期不连续" | bsed 必须 = 前一版本 bsled + 1 天（NO_INTERRUPTION_NO_OVERLAP）|
| OrgHisEffDateLegitimacyValidator | "生效日期不合法" | bsed 必须 ≤ bsled |
| OrgHisEndDateRangeValidator | "失效日期超出允许范围" | bsled 必须 ≤ TimeLineServiceUtil.getMaxEffEndDate() |
| OrgHisEndDateValidator | "失效日期不能为空" | bsled 必填 |
| OrgHisFirstEffDateConsistencyValidator | "首次生效日期不一致" | 同 boid 所有版本 firstbsed 必须 = 最早版本 bsed |
| OrgHisMigratedValidator | "已迁移数据不能反向覆盖" | 数据迁移项目专属 · 标记为 migrated 的不能再补录 |
| OrgHisOrgCurrVerParentValidator | "当前版本父组织不合法" | 当前版本 parentorg 必须存在且生效 |
| OrgHisOrgErrValidator | "组织数据校验失败" | 兜底校验 · 多种数据完整性问题汇总 |
| OrgHisOrgParentValidator | "父组织在该时段无效" | parentorg 必须在 [bsed, bsled] 区间生效 |

> ⚠ 实际错误信息以反编译 jar 内 ResManager.loadKDString 抓取为准 · 当前列表是业务推断 · ISV 编码时不要照抄字符串 · 应通过 jar 反编译 OrgHis*Validator 类源码确认。

---

## 2. 共用物理表的诊断陷阱

### 2.1 报错"列 ftdkw_xxx 不存在"

**触发**：ISV 想在 haos_adminorghis 视图上加字段 `${ISV_FLAG}_xxx` · 但实际 modifyMeta 写错了 formId（写成 haos_adminorghis · 应该写 haos_adminorg 主层）· 物理表没有这一列。

**诊断**：
1. 检查 modifyMeta 调用的 formId 是 `haos_adminorg` 还是 `haos_adminorghis`
2. 查 t_haos_adminorg 物理表实际列：DB 客户端 `desc t_haos_adminorg`
3. 如发现 ISV 字段不在物理表 · 重新走 modifyMeta(formId="haos_adminorg", op="add field")

**修复**：详见 03_model_design.md §5.1。

### 2.2 报错"权限不足 · 无法读取数据"

**触发**：用户在本场景能看到组织 A 但看不到组织 B · 实际 B 在 admin_org_quick_maintenance 也看不到。

**诊断**：本场景 setFilter（`HisModelListCommonPlugin.setFilter`）走的是 super.setFilter（HRDataBaseList）· 数据权限由父类处理 · 跟 admin_org_quick_maintenance 走的同一套权限模型。如果 admin_org_quick_maintenance 看不到 B · 本场景必然也看不到。

**修复**：去权限管理后台给用户加 B 的数据权限。

---

## 3. final 类继承编译失败

### 3.1 `cannot inherit from final HisModelFormCommonPlugin`

**触发**：ISV 想在某个 lifecycle 加扩展 · 写了 `extends HisModelFormCommonPlugin` · 编译报错。

**诊断**：HisModelFormCommonPlugin / HisModelListCommonPlugin / HisModelF7ListPlugin 三个标品类都是 `final` · 都标了 `@SdkInternal` · ISV 不能继承（PR-001 + cosmic_hr_sdk_whitelist_audit.md 黑名单）。

**修复**：换成 `extends HRDataBaseEdit` / `HRDataBaseList`（@SdkPlugin 白名单）· 并列挂插件 · 不继承覆盖。

---

## 4. setFilter add 不生效 / 标品兜底覆盖

### 4.1 加 `iscurrentversion=true` 想看当前版本 · 实际查到空集

**触发**：ISV 在本场景 setFilter 里 add `iscurrentversion=true` · 期望看到当前版本数据 · 实际查到空集。

**诊断**：标品 `HisModelListCommonPlugin.setFilter L177-L178` 已经 add `iscurrentversion=false` · 你再 add `iscurrentversion=true` · 两条 QFilter 是 AND 关系 → `false AND true` = 永远空。

**修复**：详见 06_customization_solutions.md CS-07 · 用跨场景跳转代替强改 setFilter。

---

## 5. revise 后看不到新版本

### 5.1 revise 成功但列表没刷新

**触发**：用户点 revise · 弹出新版本编辑页 · 编辑保存 · 关闭弹窗回到 list · 但 list 没显示新版本。

**诊断**：标品 `HisModelListCommonPlugin.closedCallBack L243-L254` 已经处理 · `actionId == "closeForCopyHisVersion" / "closeForNewHisVersion"` 时调 `getView().invokeOperation("refresh")`。如果列表没刷 · 可能是：
- ISV 自加 ListPlugin 的 closedCallBack 没 super 调标品（PR-001 提示：并列挂的话不需要 super · 但你重写了 closedCallBack 后没 super 跑标品逻辑就会丢）
- ShowParameter 闭合 actionId 没匹配（弹窗设了别的 actionId）

**修复**：重写 closedCallBack 时保留 super.closedCallBack(evt) · 让标品兜底刷新逻辑跑。

---

## 6. F7 选历史版本时找不到目标

### 6.1 用户搜旧组织名找不到 · 但用 boid 找得到

**触发**：F7 模式下用户搜"研发中心-上海"找不到（已改名为"研发中心-北京"）· 但用 boid=12345 能精确找到。

**诊断**：本场景 F7 默认行为由 `HisModelF7ListPlugin.setFilter L101-L107` 控制：
- `getHisModelF7PageParam().getShowCurrentNumAndName()` 决定要不要支持"按当前编号/名称"反查
- 如果配置了 = true · 系统会先查当前版本（iscurrentversion=true）的 boid · OR 拼到原过滤
- 如果配置了 = false · 只按用户输入的字面值查

**修复**：调用方在 F7 ShowParameter 设置 `customParam.put("showCurrentNumAndName", true)` · 启用反查机制。

---

## 7. 历史补录批量导入大批失败

### 7.1 HIES 导入 1000 条 · 失败 950 条

**触发**：用户走 chargepersonimpo_hr 导入数据 · 大部分失败。

**诊断**：检查 HIES 错误日志：
- `OrgHisFirstEffDateConsistencyValidator` 失败：业务方 Excel 里同 boid 不同行 firstbsed 填了不同值 · 必须统一为最早版本的 bsed
- `OrgHisEffDateContinuityValidator` 失败：bsed 没紧接前一版本 bsled+1 · 时序断了
- `OrgHisEndDateRangeValidator` 失败：bsled 填错（如填 9999-12-31 但平台 maxEffEndDate=2999-12-31）

**修复**：业务方修 Excel · 重导。如果业务允许放宽 · ISV 加自定义 Validator 识别 isImport 参数（CS-04）做兼容。

---

## 8. attachmentPanelLogInfo 缓存失效

### 8.1 详情页关闭后再打开 · 附件审计日志没了

**触发**：用户打开详情页 · 操作了附件（上传/下载）· 关闭后再开 · 附件 panel 日志列空。

**诊断**：标品 `HisModelFormCommonPlugin.afterBindData L131-L134` 缓存 attachmentPanelLogInfo 到 pageCache · pageCache 在页面关闭时被清。再开页面时 afterBindData 会重新查 LogHandlerUtil.getAttachmentLogInfo · 拿到的是新一次的 log。

**修复**：这是预期行为 · 不是 bug。如果业务需要持久化 · 直接查 attachment 服务（不依赖 pageCache）。

---

## 9. confirmchange 在 REVISE_VERSION_PAGE 模式下被拒绝

### 9.1 用户在历史详情页改 bsed 后点 save · 报"hisOpParam.reviseSave"相关错

**触发**：在 revise 派生的新版本页 · 用户修改 bsed 后保存。

**诊断**：标品 `HisModelFormCommonPlugin.beforeDoOperation case "save" L142-L169` 走 REVISE_VERSION_PAGE 分支 · 设置 `hisModelOPParam.reviseSave=true` · OP 端校验：
- 如果 `count == 2`（同 boid 仅有 2 个版本 = 当前 + 新派生）· 且 `maxEffEndDate.compareTo(bsled) != 0` → 拒绝

**业务含义**：某些 NO_INTERRUPTION_NO_OVERLAP 模式下要求新派生版本的 bsled 必须 = maxEffEndDate · 不能用户自定义。

**修复**：让用户填 bsled = 平台最大日期（一般 2999-12-31）· 或者业务方拍板换时序模式（不推荐）。

---

## 10. 通用排错诊断 SOP

| 步骤 | 操作 |
|---|---|
| 1 | 看 errorMessage · 找出具体 ResId（如 HisModelFormCommonPlugin_0）|
| 2 | grep 反编译 jar 找 ResId 所在类和方法 |
| 3 | 看该方法的 if 条件 · 判断为什么走了报错分支 |
| 4 | 业务侧检查数据：bsed / bsled / boid / hisversion 是否符合规则 |
| 5 | ISV 检查：是否继承了 final 类 / 是否覆盖了标品 setFilter |
| 6 | 物理表检查：DB 客户端 `desc t_haos_adminorg` 看 ISV 字段是否真的加了 |
| 7 | DBA 检查：QFilter 用到的字段有没有索引 / 有没有死锁 |

---

## 11. 关联文档

- `02_business_rules.md` · §5 his_save 10 Validator 详细规则
- `03_model_design.md` · §5 ISV 扩展认知
- `06_customization_solutions.md` · 各 CS 踩坑章节
- `knowledge/cosmic_realworld_traps/buildmeta_traps.md` · 加字段时的坑
- `knowledge/cosmic_realworld_traps/modifymeta_traps.md` · 改元数据时的坑
