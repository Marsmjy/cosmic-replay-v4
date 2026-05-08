# 扩展点全图 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于反编译 5 类 + plugins.json 12 插件实证
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI getEditablePlugins (2026-04-28)

---

## 一、扩展点的 4 个维度

苍穹 hrcs_entityctrl 的扩展点按"何时介入"分 4 维：

```
[A] 表单生命周期扩展点（FormPlugin · 6 个生命周期方法）
[B] 列表生命周期扩展点（TreeListPlugin · 4 个核心方法）
[C] OP 生命周期扩展点（13 阶段 · 反编译实证 EntityControlSaveOp 重写 3 个 / EntityCtrlDelOp 重写 1 个）
[D] 元数据/规则扩展点（modifyMeta + addRule 等 · 不在反编译范围）
```

---

## 二、表单生命周期扩展点（[A] FormPlugin）

挂在 `hrcs_entityctrl` 表单上 · 所有 ISV 自建 FormPlugin **必须继承 `HRDataBaseEdit`**（不要继承 `EntityCtrlEdit` · PR-001）。

### 完整生命周期（实证 `EntityCtrlEdit.java` + plugins.json）

| 阶段 | 方法 | 标品在做什么 | ISV 推荐挂法 |
|---|---|---|---|
| 1 | `preOpenForm` | `HRAdminStrictPlugin` HR 域准入闸 + `HRCertCheckEdit` / `HRBaseUeEdit` 标品 | 直接复用 HRAdminStrictPlugin · 不挂 |
| 2 | `beforeBindData` | `EntityCtrlEdit` 装载 propname / orgInfos / bdPropInfos / noDBProps · 加载分录的 propname 字段 | ISV 加自建字段（如带出业务线默认值） · 加自建 PageCache key |
| 3 | `afterBindData` | `EntityCtrlEdit` 缓存 originPropDimInfo / changedMustDim / existMustDim / beforeOpData<br>`HRHiesButtonSwitchPlugin` HIES 按钮切换 | ISV 加自建审计逻辑 / 加自建按钮显隐 |
| 4 | `registerListener` | `EntityCtrlEdit` 注册 entitytype + dimension 的 BeforeF7SelectListener + Toolbar 的 ItemClickListener | ISV 加自建字段的 F7 监听（**不要再注册 entitytype/dimension** · 标品已注册） |
| 5 | `propertyChanged` | `EntityCtrlEdit` entitytype 变更 → 清空分录 + 带 bizapp + 重装载 propInfos / ismust 变更 → 更新 changedMustDim 缓存 | ISV 加自建字段联动（PR-004 begin/endInit 防死循环） |
| 6 | `beforeF7Select` | `EntityCtrlEdit` entitytype 4 闸过滤 + dimension 6 分支过滤 | ISV **and** 进现有 filter 加二级过滤（不要覆盖） |
| 7 | `itemClick` | `EntityCtrlEdit` Toolbar.addrows 按钮 → 跳 hrcs_choosefield_page 子页面 | ISV 加自建 Toolbar 按钮 · **不要劫持 addrows itemKey** |
| 8 | `closedCallBack` | `EntityCtrlEdit` perm_choosefieldpage 回填字段 + dimensionCallBack 回填维度 | ISV 加自建子页面回调 · **不要劫持 perm_choosefieldpage / dimensionCallBack** |
| 9 | `confirmCallBack` | `EntityCtrlEdit` delete_confirm 二次确认 → 真删行 | ISV 加自建确认对话回调 |
| 10 | `beforeDoOperation` | `EntityCtrlEdit` save 校验分录非空 + deleteentry 校验 issyspreset / 二次确认 | ISV 加自建 opKey 前置（不要劫持 save / deleteentry） |
| 11 | `afterDoOperation` | `EntityCtrlEdit` save 成功后清缓存 HRPermCacheMgr.clearAllCache | ISV 加自建后置（如刷新自建 UI 块） |

### 重写覆盖矩阵（实证）

| 方法 | EntityCtrlEdit | HRCertCheckEdit | HRBaseUeEdit | HRAdminStrictPlugin | HRHiesButtonSwitchPlugin |
|---|---|---|---|---|---|
| preOpenForm | - | ✅ | ✅ | ✅ ⭐ HR 准入 | - |
| beforeBindData | ✅ ⭐ | - | - | - | - |
| afterBindData | ✅ ⭐ | - | - | - | ✅ |
| registerListener | ✅ ⭐ | - | - | - | - |
| beforeF7Select | ✅ ⭐ | - | - | - | - |
| propertyChanged | ✅ ⭐ | - | - | - | - |
| itemClick | ✅ ⭐ | - | - | - | - |
| closedCallBack | ✅ ⭐ | - | - | - | - |
| confirmCallBack | ✅ ⭐（仅 delete_confirm） | - | - | - | - |
| beforeDoOperation | ✅ ⭐ | - | - | - | - |
| afterDoOperation | ✅ ⭐ | - | - | - | - |

⭐ = 包含核心业务逻辑（ISV 谨慎追加 · 不要替换）

---

## 三、列表生命周期扩展点（[B] TreeListPlugin）

挂在 `hrcs_entityctrl` 列表上 · 所有 ISV 自建 ListPlugin **必须继承 `AbstractTreeListPlugin`**（**不是 `AbstractListPlugin`** · 因为标品 `EntityCtrlTreeListPlugin extends AbstractTreeListPlugin` 实证 L75-L76）。

### 完整生命周期（实证 `EntityCtrlTreeListPlugin.java`）

| 阶段 | 方法 | 标品在做什么 | ISV 推荐挂法 |
|---|---|---|---|
| 1 | `setFilter` | `EntityCtrlTreeListPlugin` 强制 entitytype.number is not null · 排除孤儿 | ISV **and** 加自建过滤（如 admingroup 隔离 · 见 CS-07） |
| 2 | `beforeDoOperation` | `EntityCtrlTreeListPlugin` 列表 delete 前置 issyspreset 拦截 | ISV 加自建 opKey 前置 |
| 3 | `afterDoOperation` | `EntityCtrlTreeListPlugin` delete 成功后清缓存 HRPermCacheMgr.clearAllCache | ISV 加自建后置 |
| 4 | `afterQueryOfExport` | `EntityCtrlTreeListPlugin` 导出时实时填 propname（缓存 entityFieldNameMap + entityFields） | ISV 加自建导出列填充 |

### TreeList 特有的扩展点

`AbstractTreeListPlugin` 比 `AbstractListPlugin` 多了树节点交互方法 · 但本场景标品**没用**：
- `treeNodeClick(TreeNodeEvent evt)` — 标品没重写
- `treeNodeQueryClick(TreeNodeEvent evt)` — 标品没重写

→ ISV 想加自定义树节点行为时 · 这些方法可以直接重写（不会跟标品冲突）。但要先评估业务上是否需要 —— 本场景的 TreeList 通常以"业务对象树"为主 · 加自定义树节点必须考虑 hrcs 业务对象的天然层级。

---

## 四、OP 生命周期扩展点（[C] OperationServicePlugIn）

OP 共 13 阶段（PR-010 实证）· 标品在 `EntityControlSaveOp` 重写 3 个 / `EntityCtrlDelOp` 重写 1 个。所有 ISV 自建 OP **必须继承 `HRDataBaseOp`**（不要继承标品 `EntityControlSaveOp` / `EntityCtrlDelOp` · PR-001）。

### 完整 13 阶段（PR-010 实证）

| # | 阶段方法 | EntityControlSaveOp（save） | EntityCtrlDelOp（delete） | ISV 推荐挂法 |
|---|---|---|---|---|
| 1 | `initialize` | - | - | 一般不挂 |
| 2 | `onPreparePropertys` | - | - | ISV 声明要读的非默认字段 |
| 3 | `initializeOperationResult` | - | - | 一般不挂 |
| 4 | `onAddValidators` | ✅ ⭐ 注册 EntityControlSaveValidator | - | ISV 注册自建 Validator（CS-03 / CS-04） |
| 5 | `beforeExecuteOperationTransaction` | - | - | ISV 主事务前预处理（CS-06 强制 ID） |
| 6 | `beginOperationTransaction` | ✅ ⭐ 自动带 bizapp（importtype != "override"） | ✅ ⭐ 调 deleteRoleRange + 落删除日志 | ISV 主事务内业务逻辑（强一致用） |
| 7 | `rollbackOperation` | - | - | ISV 主事务回滚补偿 |
| 8 | `endOperationTransaction` | ✅ ⭐ 同步 hrcs_roledimension（独立事务）+ 落日志 + 清缓存 | - | ISV 主事务提交前最后业务（强一致用） |
| 9 | `afterExecuteOperationTransaction` | - | - | ISV 主事务后通知下游（CS-05 BEC） |
| 10 | `onReturnOperation` | - | - | ISV 改返回结果（少用） |
| 11 | `beforeSaveAuditLog` | - | - | 极少用 |
| 12 | `validatePrefix` | - | - | 极少用 |

### opKey 与 OP 的对应关系（实证 `rules_chain_all.json`）

| opKey | 标品挂的 OP（执行链顺序） | 备注 |
|---|---|---|
| `save` | HRBaseDataLogOp → EntityControlSaveOp | 主 OP 重写 onAddValidators / beginOperationTransaction / endOperationTransaction |
| `delete` | HRDataBaseOp → HRBaseDataLogOp → EntityCtrlDelOp | 主 OP 重写 beginOperationTransaction |
| `deleteentry` | HRBaseDataLogOp → EntityControlSaveOp → HRDataBaseOp → EntityCtrlDelOp | 形式上挂了但实际不入库（FormPlugin 层处理） |
| 其他 22 opKey | HRBaseDataLogOp → EntityControlSaveOp → HRDataBaseOp → EntityCtrlDelOp 通用模板 | 因为 plugins.json 把所有 OP 都注册到所有 opKey 上 · 但只有 save / delete 真有业务逻辑 |

⚠️ **注意**：标品 plugins.json 把 EntityControlSaveOp / EntityCtrlDelOp 都挂到了**所有 opKey**（含 first/previous/copy/import 等）· 但这些 OP 在 onAddValidators / beginOperationTransaction / endOperationTransaction 里都先判 `operationKey.equals("save")` / `"delete"` · 不匹配的 opKey 它们直接 skip。**ISV 自建 OP 也应该这么做**：先 if 判 opKey · 不匹配直接 return。

---

## 五、Validator 扩展点（[C-extra]）

| 类型 | 实证类 | 注册位置 | 用途 |
|---|---|---|---|
| 标品 Validator | `EntityControlSaveValidator`（位于 hrcs-opplugin 包内 · 反编译没拿到源码） | `EntityControlSaveOp.onAddValidators` L81-L83 | save 校验（待 javap 实证规则） |
| ISV 自建 Validator | `extends AbstractValidator` 各种自建类 | ISV 自建 OP.onAddValidators | CS-03 propkey 重复校验 / CS-04 下游引用校验 |

⚠️ Validator 父类必须是 `kd.bos.entity.validate.AbstractValidator`（白名单父类 · 实证 `_shared/platform_rules.json` PR-010）

---

## 六、元数据扩展点（[D] modifyMeta + addRule）

详见 CS-01 / `kb_cosmic_modifymeta_traps.md`。本场景的关键限制：

| 元数据动作 | 是否支持 | 备注 |
|---|---|---|
| `modifyMeta(op=add, elementType=field)` 加主表字段 | ✅ | CS-01 模板 |
| `modifyMeta(op=add, elementType=field, parentScope=hrcs_entityctrl.entryentity)` 加子表字段 | ✅ | CS-01 子表加字段 · 注意非 HisModel · 没 entryboid · 唯一性约束直接生效 |
| `modifyMeta(op=remove, elementType=field, key=isvField)` 删 ISV 字段 | ✅ | 自家 ISV 加的字段可删 |
| `modifyMeta(op=remove, elementType=field, key=propkey/dimension/...)` 删标品字段 | ❌ | `isvCanModify=false` 字段标品保护 |
| `modifyMeta` 改 `propkey/dimension/entitytype/authrange` 的 fieldType | ❌ | 关键业务键 · 改了直接挂 |
| `modifyMeta` 加 form 级 EmbedFormAp | ⚠ | 实证 `kb_embed_form_pattern.md` 标品 EmbedFormAp 假成功坑 · 优先走 IDEA 插件 |
| `addRule` 加业务规则 | ✅ | 标品 listRules = 0 · ISV 加规则不会冲突 |
| `addRule` 加 preCondition `==''` | ❌ | preCondition 不支持空字符串比较（kb_cosmic_addrule_traps.md 实证） |

---

## 七、ISV 最常覆盖的扩展点 Top 3

按 hrcs 域过往项目高频度排序：

1. **`onAddValidators`（OP 阶段 4）** — 加业务校验
   - **覆盖原因**：业务总有特殊校验需求（如"propkey 不重复"/"删除前查下游"）· 必须挂 OP Validator 才能拦住列表批量保存 / 接口直推
   - **典型 CS**：CS-03（propkey 重复）/ CS-04（删除前查下游）

2. **`afterExecuteOperationTransaction`（OP 阶段 9）** — 通知下游
   - **覆盖原因**：业务对象维度映射变更后 · 客户系统的权限缓存需要刷新；标品没发 BEC · ISV 自建发布方
   - **典型 CS**：CS-05（BEC 发布方）

3. **`setFilter`（TreeListPlugin）** — 列表权限过滤
   - **覆盖原因**：HR 多管理员组需要数据隔离 · 标品没做 admingroup 过滤
   - **典型 CS**：CS-07（按 admingroup 过滤可见映射）

---

## 八、不推荐的扩展位（容易踩坑）

| 扩展位 | 不推荐原因 |
|---|---|
| 继承 `EntityCtrlEdit` 重写 propertyChanged | 标品在改 entitytype 时清空分录 + 重装载 propInfos · 改 super 方法签名风险大 · 走并列挂 |
| 继承 `EntityCtrlTreeListPlugin` 重写 setFilter | 标品 setFilter 加了 entitytype.number 过滤 · 替换会让孤儿映射出现 · 走并列挂 |
| 继承 `EntityControlSaveOp` 重写 endOperationTransaction | 标品做了大量 hrcs_roledimension 同步 + 独立事务 + 缓存清空 · 任何替换都可能出大问题 · 走并列挂 |
| 替换 `ChoiceFieldPageCustomQueryService.parsePropertySub` | 业务对象的"字段选择子页面"由此 service 提供 · 替换会影响所有 hrcs 类似页面 |
| `HRPermCacheMgr.clearAllCache()` 之外的局部缓存清理 | 标品已经在 save / delete 后双重清空 · ISV 不需要再做 |
| 在 `confirmCallBack` 替换 delete_confirm 行为 | 标品 delete_confirm 是删行的二次确认（实证 `EntityCtrlEdit.confirmCallBack` L505-L513）· 替换会破坏 issyspreset 删行拦截 |

---

## 九、查反编译源码的速查表

| 想看哪段逻辑 | 文件 | 行号 |
|---|---|---|
| HR 准入闸 | `HRAdminStrictPlugin.java` | L29-L52 |
| 业务对象 F7 4 闸 | `EntityCtrlEdit.java` | L515-L533 |
| 维度 F7 6 分支 | `EntityCtrlEdit.java` | L191-L237 |
| 添加行按钮跳子页面 | `EntityCtrlEdit.java` | L315-L334 + L429-L460 |
| save 校验分录非空 | `EntityCtrlEdit.java` | L262-L267 |
| save 设 propDimInfo 给 OP | `EntityCtrlEdit.java` | L268-L271 |
| deleteentry 校验 issyspreset | `EntityCtrlEdit.java` | L272-L284 |
| save 同步 hrcs_roledimension | `EntityControlSaveOp.java` | L99-L146 |
| TX.requiresNew 独立事务 | `EntityControlSaveOp.java` | L251-L258 |
| delete 联动清 hrcs_roledimension | `EntityCtrlDelOp.java` | L47-L70 |
| TreeList setFilter 排除孤儿 | `EntityCtrlTreeListPlugin.java` | L79-L87 |
| 导出时填 propname | `EntityCtrlTreeListPlugin.java` | L112-L144 |
| 查 HAOS 结构项目 | `EntityCtrlEdit.java` | L240-L255 |

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## ISV 扩展指引（基于 HRBaseDataImportEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void initImportData(kd.bos.entity.datamodel.events.InitImportDataEventArgs)`
- `public public void beforeImportData(kd.bos.entity.datamodel.events.BeforeImportDataEventArgs)`
- `public public void afterImportData(kd.bos.entity.datamodel.events.ImportDataEventArgs)`
- `public public void queryImportBasedata(kd.bos.entity.datamodel.events.QueryImportBasedataEventArgs)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · LogHandlerUtil L158
```java
 156                   DynamicObject logDy = new DynamicObject((DynamicObjectType)dataEntityType);
 157                   logDy.set("id", (Object)ids[index]);
 158 >                 logDy.set("username", (Object)RequestContext.get().getUserName());
 159                   logDy.set("opname", (Object)entityModifyInfo.getOperationKey());
 160                   logDy.set("opdate", (Object)now);
```

**QUERY_BUILDER** · LogHandlerUtil L346
```java
 344                   attachmentIds.add(refBaseObj.getLong("id"));
 345               }
 346 >             DynamicObject[] attachments = BusinessDataServiceHelper.load((String)"bd_attachment", (String)"id,name,url,createtime", (QFilter[])new QFilter[]{new QFilter("id", "in", (Object)attachmentIds)});
 347               Arrays.stream(attachments).forEach(attachment -> attachmentLogInfoList.add(new AttachmentLogInfo("2", (Object)attachment.getLong("id"), Long.valueOf(0L), attachment.getString(displayProp), LogHandlerUtil.getAttachmentFullUrl(URLEncoder.encode(attachment.getString("url"))), attachment.getDate("createtime"), displayProp)));
 348           }
```

**READ_VIA_HELPER** · LogHandlerUtil L208
```java
 206           if (oldDys == null || oldDys.length == 0) {
 207               List pks = Stream.of(newDys).map(DataEntityBase::getPkValue).distinct().collect(Collectors.toList());
 208 >             objectDynamicObjectMap = Arrays.stream(BusinessDataServiceHelper.load((Object[])pks.toArray(), (DynamicObjectType)dynamicObjectType)).collect(Collectors.toMap(DataEntityBase::getPkValue, dy -> dy));
 209           } else {
 210               objectDynamicObjectMap = Arrays.stream(oldDys).collect(Collectors.toMap(dy -> dy.get("id"), Function.identity(), (x1, x2) -> x2));
```

**THROW_BIZ_EXCEPTION** · HisModelCommonService L124
```java
 122                   LOGGER.error((Throwable)exception);
 123               }
 124 >             throw new KDBizException(String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u7684\u5386\u53f2\u6a21\u578b\u5b9e\u4f53\u914d\u7f6e\u201c\u6a21\u5f0f\u9009\u62e9\u201d\u672a\u914d\u7f6e\uff0c\u8bf7\u5148\u5b8c\u6210\u914d\u7f6e\u3002", (String)"HisModelCommonService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]), entityNumber));
 125           }
 126           return hisModelEntityConfig;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

## ISV 扩展指引（基于 HRCertCheckEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

## ISV 扩展指引（基于 HRBaseUeEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HrEntityCommonService L46
```java
  44       public List<String> getParentEntity(String entryEntity) {
  45           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_formmeta");
  46 >         QFilter entityFilter = new QFilter("number", "=", (Object)entryEntity);
  47           DynamicObject dynamicObject = helper.queryOriginalOne("inheritpath", new QFilter[]{entityFilter});
  48           String inheritPath = dynamicObject.getString("inheritpath");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## ISV 扩展指引（基于 HRHiesButtonSwitchPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.form.plugin.AbstractFormPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRHiesButtonSwitchPlugin L92
```java
  90               if (enableNoPermBtnHide) {
  91                   String appId = HRPermUtil.getAppIdFromShowParam((FormShowParameter)view.getFormShowParameter());
  92 >                 long currUserId = RequestContext.get().getCurrUserId();
  93                   boolean isPerm = PermissionServiceHelper.checkPermission((Long)currUserId, (String)appId, (String)billFormId, (String)permItem);
  94                   LOGGER.info("currUserId:{} appId:{} billFormId:{} permItem:{}", new Object[]{currUserId, appId, billFormId, permItem});
```

**QUERY_BUILDER** · HRQFilterHelper L17
```java
  15   public class HRQFilterHelper {
  16       public static QFilter buildEql(String filed, Object val) {
  17 >         return new QFilter(filed, "=", val);
  18       }
  19   
```

**CALL_CROSS_SERVICE** · HRPermUtil L65
```java
  63   
  64       public static Map<String, Object> queryPermConfig(String formId) {
  65 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hbss", (String)"IHBSSPermService", (String)"queryPermConfig", (Object[])new Object[]{formId});
  66       }
  67   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

## ISV 扩展指引（基于 HRAdminStrictPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public static public static void showMesIfUserIsNotAdmin(kd.bos.form.events.PreOpenFormEventArgs)`
- `public static public static boolean isHrAdmin()`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRAdminService L23
```java
  21   public class HRAdminService {
  22       public static boolean isHrAdmin() {
  23 >         Long userId = RequestContext.get().getCurrUserId();
  24           QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25           DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
```

**QUERY_BUILDER** · HRAdminService L24
```java
  22       public static boolean isHrAdmin() {
  23           Long userId = RequestContext.get().getCurrUserId();
  24 >         QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25           DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
  26           Long adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
```

**READ_VIA_HELPER** · HRAdminService L25
```java
  23           Long userId = RequestContext.get().getCurrUserId();
  24           QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25 >         DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
  26           Long adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
  27           filters = new QFilter[]{new QFilter("user.id", "=", (Object)userId).and("usergroup.adminscheme.id", "=", (Object)adminScheme).and("usergroup.isdomain", "=", (Object)"1").and("usergroup.domain", "=", (Object)1386267129346523136L).or("usergroup.id", "=", (Object)1393280986623636480L).and("user.id", "=", (Object)userId)};
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlEdit -->

## ISV 扩展指引（基于 EntityCtrlEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `afterBindData`, `registerListener`, `beforeF7Select`, `beforeDoOperation`, `afterDoOperation`, `itemClick`, `propertyChanged`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void itemClick(kd.bos.form.control.events.ItemClickEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · EntityCtrlEdit L206
```java
 204               String orgInfoStr = this.getView().getPageCache().get("orgInfos");
 205               Map orgInfos = HRStringUtils.isNotEmpty((String)orgInfoStr) ? (Map)SerializationUtils.fromJsonString((String)orgInfoStr, Map.class) : null;
 206 >             QFilter otBdFilter = new QFilter("datasource", "=", (Object)"basedata").and("entitytype", "in", Arrays.asList("haos_adminorghrf7", "haos_adminorghrf7", "haos_projectteamhr", "haos_adminorgdetail"));
 207               QFilter filter = null;
 208               if (bdPropInfos.containsKey(propKey)) {
```

**READ_VIA_HELPER** · RoleManageService L57
```java
  55           HashSet noCtrlPermEntitys = Sets.newHashSetWithExpectedSize((int)16);
  56           long startTime = System.currentTimeMillis();
  57 >         String hrCloudStrIds = HRCloudServiceHelper.getAllHRCloudIdInStr();
  58           String sql = "select en.FNUMBER, en.FDATA from T_META_ENTITY en left join t_meta_entitydesign e on en.fnumber=e.fnumber\n    left join t_meta_bizapp a on e.fbizappid = a.fid\nwhere a.fbizcloudid in (" + hrCloudStrIds + ")\nand en.FKEY = 'CtrlType' AND en.FTYPE = '24' ";
  59           String algoKey = "RoleManageService.queryEntityIsCtrlPermBatch";
```

**CALL_CROSS_SERVICE** · EntityCtrlEdit L242
```java
 240       @ExcludeFromJacocoGeneratedReport
 241       private List<Long> queryEntityPropOtclassifyIds(String entity, String propKey) {
 242 >         List structProjects = (List)HRMServiceHelper.invokeHRMPService((String)"haos", (String)"IHAOSStructProjectService", (String)"queryStructProConfig", (Object[])new Object[]{entity, propKey, null});
 243           ArrayList<Long> otclassifyIds = new ArrayList<Long>(10);
 244           if (CollectionUtils.isEmpty((Collection)structProjects)) {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## ISV 扩展指引（基于 HRCertCheckList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.bos.list.plugin.AbstractListPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlTreeListPlugin -->

## ISV 扩展指引（基于 EntityCtrlTreeListPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlTreeListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlTreeListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.bos.list.plugin.AbstractTreeListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforeDoOperation`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterQueryOfExport(kd.bos.form.events.AfterQueryOfExportEvent)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · EntityCtrlTreeListPlugin L81
```java
  79       public void setFilter(SetFilterEvent evt) {
  80           super.setFilter(evt);
  81 >         evt.getQFilters().add(new QFilter("entitytype.number", "is not null", null));
  82           Set noExistedEntityNumbers = EntityCtrlServiceHelper.getAllNoExistEntityCtrlNumbers();
  83           if (CollectionUtils.isNotEmpty((Collection)noExistedEntityNumbers)) {
```

**READ_VIA_HELPER** · EntityCtrlTreeListPlugin L82
```java
  80           super.setFilter(evt);
  81           evt.getQFilters().add(new QFilter("entitytype.number", "is not null", null));
  82 >         Set noExistedEntityNumbers = EntityCtrlServiceHelper.getAllNoExistEntityCtrlNumbers();
  83           if (CollectionUtils.isNotEmpty((Collection)noExistedEntityNumbers)) {
  84               LOGGER.warn("hrcs_entityctrl noExistedEntityNumbers:{}", (Object)noExistedEntityNumbers);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlTreeListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlTreeListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.EntityCtrlTreeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.EntityControlSaveOp -->

## ISV 扩展指引（基于 EntityControlSaveOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.EntityControlSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.EntityControlSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · EntityControlSaveOp L307
```java
 305               HashMap<String, List> roleDynaMap = new HashMap<String, List>(16);
 306               HRBaseServiceHelper hRBaseServiceHelper = new HRBaseServiceHelper("hrcs_roledimension");
 307 >             for (DynamicObject roleDim : roleDimArr = hRBaseServiceHelper.loadDynamicObjectArray(new QFilter[]{new QFilter("role", "in", roleIds)})) {
 308                   String roleId = roleDim.getString("role.id");
 309                   if (HRStringUtils.isEmpty((String)roleId)) continue;
```

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.EntityControlSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.EntityControlSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.EntityControlSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

## ISV 扩展指引（基于 HRDataBaseOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `protected protected void setRecordLimit(int)`

### SDK 范式（ISV 抄作业）

**THROW_BIZ_EXCEPTION** · HRDataBaseOp L39
```java
  37               case "save": {
  38                   if (recordCount <= this.recordLimit) break;
  39 >                 throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6570\u636e\u91cf\u8d85\u8fc7\u9650\u5236\u9608\u503c%1$s\uff0c\u5f53\u524d\u8bb0\u5f55\u6570\uff1a%2$s\u3002", (String)"HRDataBaseOp_0", (String)"hrmp-hbp-business", (Object[])new Object[0]), this.recordLimit, recordCount));
  40               }
  41           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.EntityCtrlDelOp -->

## ISV 扩展指引（基于 EntityCtrlDelOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.EntityCtrlDelOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.EntityCtrlDelOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.EntityCtrlDelOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.EntityCtrlDelOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.EntityCtrlDelOp -->
