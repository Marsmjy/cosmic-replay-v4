# 业务规则 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于反编译 5 类 + opkeys_index.json 25 opKey + scene_doc.json 17 字段 + listRules 实抓
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI listRules（2026-04-28）

---

## ✅ verified · OpenAPI listRules 实抓

实抓 listRules（formRule / bizRule）：**0 条**

→ 标品 hrcs_entityctrl 不在元数据层挂 formRule（业务规则）/ bizRule（业务校验规则）· 所有业务规则**写在插件代码里**（FormPlugin + OP + Validator）。这与 hjm_jobhr / haos_adminorg 等场景一致：HR 域偏向把校验逻辑写进 Java 而不是元数据规则。

---

## 一、隐式规则总览（5 大类 · 反编译实证）

### 1.1 准入闸（preOpenForm 阶段）

| 规则 | 实证位置 | 触发条件 | 阻断方式 |
|---|---|---|---|
| **HR 领域管理员准入** | `HRAdminStrictPlugin.preOpenForm` L29-L52 | 当前用户既不是 isAdminUser 也不是 isCosmicUser 也不是 isHrAdmin | `setCancel(true)` + 错误"您无法访问该功能 · 因为您不是 HR 领域管理员" |
| **F7 弹窗模式不拦截** | `HRAdminStrictPlugin.preOpenForm` L33-L34 | 当前是 ListShowParameter.isLookUp() （F7 弹窗） | 直接 return 不做拒绝（避免 F7 弹窗时还要求 HR 管理员身份） |

### 1.2 业务对象 F7 4 闸过滤（添加新映射时可选范围）

实证：`EntityCtrlEdit.bulidEntityFilters` L515-L533

```
QFilter modelTypeFilter = modeltype IN
  ("BaseFormModel", "BillFormModel", "QueryListModel", "DynamicFormModel", "ReportFormModel")
QFilter noTemplateFilter = istemplate = "0"
QFilter inEntityFilter   = id IN allHrHasPermItemEntitys (HR 域且有控权项的实体集 · 已减去 notInNumbers)

notInNumbers 排除集 =
  (1) 已在 hrcs_entityctrl 中存在的 entitytype.number          // 一对象一映射
  (2) 硬编码 "bos_org" + "hbss_hrbu"                          // 组织/HRBU 不允许
  (3) EntityCtrlServiceHelper.queryEntityForBidInfo 命中的禁用集
  (4) RoleManageService.getNoCtrlPermEntitysFromCache 命中的非控权集
```

→ 4 闸都通过的业务对象才会出现在 F7 列表里。

### 1.3 维度 F7 6 分支过滤（按字段类型决定可选维度）

实证：`EntityCtrlEdit.beforeF7Select` L197-L237

| # | 字段类型识别（propKey 在哪个 map 里） | 维度过滤条件 | 实证行 |
|---|---|---|---|
| 1 | `propKey` 在 `bdPropInfos` AND 在 `orgInfos` AND `buId == 1`（特殊全员组） | `datasource = "orgteam"` AND `org_classify.fbasedataid IN [1010, 1020]` | L213-L215 |
| 2 | `propKey` 在 `bdPropInfos` AND 在 `orgInfos` AND `buId 是其他正常 buId` | `datasource = "hrbu"` AND `hrbu = buId` | L211-L218 |
| 3 | `propKey` 在 `bdPropInfos` AND 在 `orgInfos` AND `buId == Integer.MAX_VALUE 或 == 0` | `datasource = "hrbu"`（不加 hrbu 过滤 · 任何 hrbu 维度可选） | L211, L212 |
| 4 | `propKey` 在 `bdPropInfos` AND 不在 `orgInfos` AND queryEntityPropOtclassifyIds 非空 | `org_classify.fbasedataid IN otclassifyIds`（含 1010/1020 时 OR `otBdFilter`：`datasource=basedata` AND `entitytype IN [haos_adminorghrf7×2, haos_projectteamhr, haos_adminorgdetail]`） | L222-L226 |
| 5 | `propKey` 在 `bdPropInfos` AND 不在 `orgInfos` AND otclassifyIds 空 | `entitytype = bdPropInfos.get(propKey)` | L228 |
| 6 | `propKey` 不在 `bdPropInfos` AND 不是 ID/boid/`.id` 结尾 | `datasource != "orgteam"` | L231-L232 |
| 7 (兜底) | `propKey == "ID"` 或 `propKey == "boid"` 或 `propKey 以 .id 结尾` | filter = null（不过滤） | L231 |

**关键 token**：
- `bdPropInfos`：被控对象的所有"基础资料关联字段" map · 由 `putMainOrgFieldProp` 或 `putDynaFormCtrlInfo` 装载
- `orgInfos`：被控对象的所有"组织字段（OrgProp）" map · key=propkey · value=buCaFunc id
- `otclassifyIds`：通过跨模块调 `IHAOSStructProjectService.queryStructProConfig` 拿到的"组织分类 ID" 集

### 1.4 save / delete 操作前置规则

| 规则 | 实证位置 | 触发条件 | 阻断方式 |
|---|---|---|---|
| **分录不能为空（save）** | `EntityCtrlEdit.beforeDoOperation` L262-L267 | `operateKey = "save"` AND `entryentity.size() == 0` | `args.setCancel(true)` + 错误"请填写维度映射数据" |
| **预置数据无法删除（删行）** | `EntityCtrlEdit.beforeDoOperation` L272-L283 | `operateKey = "deleteentry"` AND 选中行 `issyspreset = true` | `args.setCancel(true)` + 错误"预置数据无法删除" |
| **删除二次确认（编辑态删行）** | `EntityCtrlEdit.beforeDoOperation` L285-L288 | `operateKey = "deleteentry"` AND `OperationStatus.EDIT` | `args.setCancel(true)` + 弹"删除后角色下的业务对象将不再参与控权 · 删除维度控权将影响角色所有成员 · 确认删除吗？" `confirmCallBack="delete_confirm"` |
| **TreeList 列表批量删除预置拦截** | `EntityCtrlTreeListPlugin.beforeDoOperation` L89-L101 | `operateKey = "delete"` AND `EntityCtrlServiceHelper.beforeDelOp(...) == false` | `args.setCancel(true)` + 错误"预置数据无法删除" |

### 1.5 save / delete 操作后置规则

| 规则 | 实证位置 | 触发条件 | 副作用 |
|---|---|---|---|
| **save 成功后清缓存** | `EntityCtrlEdit.afterDoOperation` L292-L298 | `operateKey = "save"` AND `operationResult.isSuccess()` | `HRPermCacheMgr.clearAllCache()` · 强制权限缓存重建 |
| **TreeList delete 成功后清缓存** | `EntityCtrlTreeListPlugin.afterDoOperation` L103-L110 | `operateKey = "delete"` AND `operationResult.isSuccess()` | `HRPermCacheMgr.clearAllCache()` |

---

## 二、save 联动规则（OP 端 · 重点）

### 2.1 自动带 bizapp 字段（避免用户漏填）

实证：`EntityControlSaveOp.beginOperationTransaction` L85-L97

| 触发条件 | 业务逻辑 |
|---|---|
| `operateKey = "save"` AND `importType != "override"` AND 当前 `bizapp == null` AND `entitytype.bizappid != null` | 自动 `dataEntity.set("bizapp", entitytype.bizappid)` |

→ 这是为什么前端用户只选了"业务对象"· 后端 hrcs_entityctrl.fappid 也能落上 · 不需要用户额外勾"应用"。

### 2.2 ismust 维度同步到 hrcs_roledimension

实证：`EntityControlSaveOp.endOperationTransaction` L99-L146 + `syncMustDimToRoleDim` L187-L259

```
触发条件：operateKey = "save" AND importType != "override"

步骤：
1. 收集本次保存的所有 ismust=true 的分录 → syncMustDims map (dimId → entity#appId → propKeys)
2. 调 assembleEntityRoleDim 查 t_perm_rolepermdetial 拿到本次实体涉及的所有角色
3. 调 syncMustDimToRoleDim 把 ismust 维度同步到 hrcs_roledimension：
   3.1 已绑同维度的 roleDim · 把 entry 中匹配 entity + propkey 的行从"未启用"状态移除（modify）
   3.2 没绑同维度的 roleDim · 新建一行（add）
4. 调 EntityCtrlLogService.resolveLog 落操作日志
5. 调 HRPermCacheMgr.clearAllCache 清权限缓存
6. 在独立事务（TX.requiresNew()）保存 hrcs_roledimension（避免影响主事务）
```

⚠ **规则陷阱**：步骤 3 在独立事务里做（实证 L251-L258 `try (TXHandle tx = TX.requiresNew())`）· 主事务回滚时**这个独立事务不会回滚** · 可能导致 hrcs_roledimension 比 hrcs_entityctrl 多写一些数据。**这是标品的设计选择 · ISV 不要模仿** —— 反而要保证一致性时该用同事务（`endOperationTransaction` 默认就在主事务里 · 不要 `requiresNew`）。

### 2.3 删除分录后清理 hrcs_roledimension

实证：`EntityControlSaveOp.deleteRowsPostProcessing` L148-L178

```
触发条件：variables.propDimInfo 与 originPropDimInfo 有差集

步骤：
1. 从 variables 反序列化 propDimInfo（当前保存的 propkey→dimId）+ originPropDimInfo（保存前的）
2. 遍历 originPropDimInfo · 找出"propkey 已被删 / propkey 改换了 dimension"的差集
3. 逐条调 EntityCtrlSaveOp.deleteRoleRange 把 hrcs_roledimension 中匹配的行 entry.removeIf 掉
4. 落 DimRoleInfoModel("delete") 日志 · 加进 effectDimRoleList
```

⚠ **依赖前端**：variables.propDimInfo / originPropDimInfo 由 `EntityCtrlEdit.beforeDoOperation` L268-L271 提前传到 OperateOption。**ISV 列表批量 save / 接口直推保存** 时这两个变量为空 · 删除联动会被跳过 · 须 ISV 自建逻辑兜底（详见 06_customization_solutions.md CS-06）。

---

## 三、delete OP 端联动规则

实证：`EntityCtrlDelOp.beginOperationTransaction` L47-L70

```
触发条件：opKey = "delete" 主表删除

步骤：
1. 从 OperateOption.variables 读 toDelDimRoleRanges (Map<schemeId, List<encodedRange>>)
2. 收集所有要被删的 schemeId 列表
3. 对每条 toDelDimRoleRange · split('|') 解析 → roleId|entityId|propKey|dimId
4. 调 EntityCtrlServiceHelper.deleteRoleRange 把 hrcs_roledimension 中对应行删掉
5. 读 logInfos 反序列化 EntityCtrlModel · 调 EntityCtrlLogService.resolveLog 落删除日志
```

⚠ **依赖前端**：toDelDimRoleRanges + logInfos 都由前端 / 列表逻辑提前传。**接口直推 delete** 时这两个变量为空 · 角色维度联动清理会被跳过 · 但主表 hrcs_entityctrl 删除照常 · 数据会有孤儿。

---

## 四、Validator 规则（onAddValidators 阶段）

实证：`EntityControlSaveOp.onAddValidators` L81-L83

```java
public void onAddValidators(AddValidatorsEventArgs args) {
    args.addValidator(new EntityControlSaveValidator());
}
```

→ 标品挂了 1 个 `EntityControlSaveValidator`（在 hrcs-opplugin 包内 · 反编译没拿到源码 · class 名透露用途："实体控制保存校验"）。**待 javap 反编译该类 · 拿规则细节**。

ISV 加自建 Validator 走"并列挂"（PR-001）：

```java
public class TdkwEntityCtrlSaveValidator extends AbstractValidator {
    @Override
    public void validate() {
        for (ExtendedDataEntity row : this.getDataEntities()) {
            DynamicObject entity = row.getDataEntity();
            // ... 自定义校验
        }
    }
}

// 在自建 OP（继承 HRDataBaseOp）的 onAddValidators 中注册
@Override
public void onAddValidators(AddValidatorsEventArgs args) {
    super.onAddValidators(args);
    args.addValidator(new TdkwEntityCtrlSaveValidator());
}
```

---

## 五、字段联动规则（FormPlugin propertyChanged 阶段）

实证：`EntityCtrlEdit.propertyChanged` L336-L365

| 字段变更 | 联动逻辑 | 实证行 |
|---|---|---|
| `entitytype` 改了 | (1) `deleteEntryData("entryentity")` · 清空所有分录（因为业务对象变了 · 分录的 propkey 全失效）<br>(2) 如新值为空 · `setValue("bizapp", "")` · 清空应用<br>(3) 否则调 `bindAppCloud(entityType)` 把 entitytype.bizappid 带到 bizapp 字段 L535-L538<br>(4) 调 `isDynOrVir` 判定虚字段 · 走不同的 `putMainOrgFieldProp` / `putDynaFormCtrlInfo` 装载 propInfos | L339-L352 |
| `ismust` 改了（分录复选框） | (1) 反序列化 `changedMustDim` 列表<br>(2) 如分录已有 id（非新增行）· 按 `newValue` 加进 / 移出 changedMustDim<br>(3) 序列化回 PageCache | L353-L364 |

⚠ **死循环风险（PR-004）**：`bindAppCloud` 内部调 `getModel().setValue("bizapp", ...)` —— 这会再次触发 propertyChanged。但反编译里没看到对 `bizapp` 字段的 propertyChanged 处理 · 所以**实际上不会死循环**。但 ISV 自建联动时**强烈建议加 beginInit / endInit 包裹**。

---

## 六、子页面回调规则（closedCallBack 阶段）

实证：`EntityCtrlEdit.closedCallBack` L463-L503

| 回调 actionId | 触发场景 | 业务逻辑 |
|---|---|---|
| `perm_choosefieldpage` | 点"添加行"后从 hrcs_choosefield_page 子页面回填 | 遍历返回的 ListSelectedRow · 跳过已存在的 propkey · 否则 `createNewEntryRow` + `setValue("propkey", ...)` + `setValue("propname", ...)`。如果这些字段是 noDBProp / QueryEntityProp（多层引用）· 自动 `setValue("authrange", "2")` + `setEnable(false, "authrange")` |
| `dimensionCallBack` | 维度 F7 选完后回填 | `setValue("dimension", dimensionId, currentRow)` |
| `delete_confirm` | 编辑态删行二次确认（messageBoxClosedEvent · 不是 closedCallBack） | 用户点 Yes 才真删 `deleteEntryRows("entryentity", selectRows)` |

---

## 七、跨模块约束规则

| 规则 | 来源 | 触发时机 |
|---|---|---|
| **业务对象必须在 HR 域有权限项** | `EntityCtrlServiceHelper.getAllHrHasPermItemEntity` | F7 业务对象时过滤 |
| **virtualField 业务对象必须先在 hrcs_dynaformctrl 配过虚字段** | `EntityCtrlEdit.showFieldForm` L432-L435 | 点"添加行"按钮时校验 |
| **被控对象的字段必须存在于 bos_objecttype** | `EntityCtrlEdit.itemClick` L323-L331 | 点"添加行"按钮时校验"该实体没有属性 · 请重新选择" |
| **save 后清 HRPermCacheMgr 全缓存** | `EntityCtrlEdit.afterDoOperation` + `EntityControlSaveOp.endOperationTransaction` + `EntityCtrlTreeListPlugin.afterDoOperation` | save / delete 成功后 |

---

## 八、规则对外可见性 / 调试

- **listRules 实抓 0 条** · 在元数据层不会看到任何 formRule / bizRule
- **逻辑都在反编译类里** · 用 `_decompiled/scenarios/hrcs_entityctrl/*.java` 5 个文件可读
- **调试入口**：
  - 看 save 失败原因 · 在浏览器 console 看 `EntityControlSaveValidator` 错误（待 javap 该类拿规则）
  - 看 deleteentry 失败 · 看错误信息匹配 "预置数据无法删除"（issyspreset = true）
  - 看 F7 范围异常 · 在后端日志找 `EntityCtrl dimensionFilter = {}` 行（实证 `EntityCtrlEdit.beforeF7Select` L234）

---

## 九、规则的"绕过点"（ISV 谨慎使用）

1. **import override 模式**（`importType = "override"`）会跳过 `endOperationTransaction` 的同步 hrcs_roledimension 逻辑 · 实证 L102。**仅 HIES 导入路径用** · ISV 不要自己设这个变量
2. **`importType` 为空** 的接口直推 save 也会跳过该逻辑（条件 `!HRStringUtils.equals("override", importType)` 实际是"非 override 才跑" · 但 propDimInfo / originPropDimInfo 为空也跑不出有效逻辑）
3. **TX.requiresNew() 独立事务**（实证 L251）让 hrcs_roledimension 跟主事务解耦 · ISV 在 endOperationTransaction 里追加独立事务时要注意一致性

---

## 十、未确认的规则（需 javap 反编译补）

- [ ] **`EntityControlSaveValidator.validate()` 内部规则**：反编译类位于 `kd.hr.hrcs.opplugin.validator.perm.EntityControlSaveValidator` · 没拿到源码 · 推测可能校验"propkey + dimension 组合不能重复" / "ismust=true 时 dimension 不能空"
- [ ] **`EntityCtrlServiceHelper.beforeDelOp` 内部规则**：反编译类内部决定 TreeList 删除是否被允许 · 推测除 issyspreset 还有其他判定（如"被某些角色还在用就不让删"）
- [ ] **`EntityCtrlServiceHelper.queryEntityForBidInfo` 内部规则**：实证 `bulidEntityFilters` L522 调用 · 决定哪些 entitytype 永久禁用做权限映射
- [ ] **`HRBuCaServiceHelper.getBuCaFuncFromSpec` 内部规则**：决定一个应用-实体组合的 buCaFunc id（影响 syncMustDimToRoleDim 写入 hrcs_roledimension 时填的 bucafunc 字段）

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

## chgaction 实证补充（HRCertCheckEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

## chgaction 实证补充（HRBaseUeEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

## chgaction 实证补充（HRAdminStrictPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlEdit -->

## chgaction 实证补充（EntityCtrlEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlEdit`
> 跨类追踪: 5 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlTreeListPlugin -->

## chgaction 实证补充（EntityCtrlTreeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlTreeListPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlTreeListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlTreeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.EntityControlSaveOp -->

## chgaction 实证补充（EntityControlSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.EntityControlSaveOp`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.EntityControlSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.EntityControlSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

## chgaction 实证补充（HRDataBaseOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRDataBaseOp_0` | 数据量超过限制阈值%1$s，当前记录数：%2$s。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.EntityCtrlDelOp -->

## chgaction 实证补充（EntityCtrlDelOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.EntityCtrlDelOp`
> 跨类追踪: 8 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.EntityCtrlDelOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.EntityCtrlDelOp -->
