# 异常诊断 · 维度管理 (hrcs_dimension)

> **状态**: 🟢 基于反编译 4 类（DimensionNewEdit / DimensionList / DimensionDeleteOp / HRAdminStrictPlugin）+ cosmic_realworld_traps 共性陷阱 + admin_org/dynascheme 异常对照
> **confidence**: verified
> **数据源**: CFR 反编译 + knowledge/cosmic_realworld_traps/*.md (2026-04-28)

---

## 一、本场景特有异常（dimension 独有）

### 1.1 切换 datasource 后 entry 子表数据丢失（设计行为）

**症状**：用户切到 datasource=enum · 填了 5 行 entry · 又改回 datasource=basedata · entry 数据全没了

**根因**：DimensionNewEdit.showEnumCtrl L173-L174 / L184 / L194：

```java
} else if (HRStringUtils.equals("basedata", datasource)) {
    this.getView().setVisible(Boolean.FALSE, new String[]{DIMENSION_NUM});
    this.getModel().deleteEntryData("entry");          // ⚠ 强制清 entry
    ...
}
```

**为什么**：标品认为 entry 是"枚举值列表" · 只在 datasource=enum 时有意义 · 切到其他类型时清掉是干净的设计。但用户体验差（误操作不可恢复）。

**解决**：
- 实施前提示用户："切换 datasource 会清掉枚举值"
- ISV 想加"切换前提示"：在 ISV FormPlugin propertyChanged 的 datasource case 拦截 · showConfirm 让用户确认 · No 时 setValue(datasource, oldValue) 回滚

---

### 1.2 业务对象 F7 找不到 hr 域实体

**症状**：在 datasource=basedata · F7 entitytype 时 · 列表里没有 hrpi_person / hrpi_employee 等 HR 实体

**根因**：DimensionNewEdit.beforeF7Select L367-L370：

```java
QFilter filter = new QFilter("modeltype", "=", "BaseFormModel");
lsp.getListFilterParameter().setFilter(filter);
lsp.setFormId("bos_listf7");
```

**可能原因**：
1. hrpi_person 等 HR 实体不是 `BaseFormModel`（可能是 `DynamicFormModel` 或 `BillFormModel`）→ F7 过滤后看不到
2. 业务对象在不同 modeltype 下 · dimension 只支持基础资料形态

**解决**：
- 检查目标业务对象的 modelType（用 OpenAPI getFormMetadata 查）
- 如果不是 BaseFormModel · 需要让客户用 enum 类型 dimension 列举枚举值代替
- 或者 ISV 改 dimension 父模板 · 但风险高 · 不推荐

---

### 1.3 选了业务对象 · 弹"当前业务对象无编码或名称字段"

**症状**：F7 entitytype 选了某个 BaseFormModel 业务对象 · 提示"当前业务对象无编码或名称字段，不允许配置维度。"

**根因**：DimensionNewEdit.limitBasedataType L240-L256：

```java
DataEntityPropertyCollection toPros = EntityMetadataCache.getDataEntityType(item.getString("id")).getProperties();
boolean hasName = false;
boolean hasNumber = false;
for (IDataEntityProperty prop : toPros) {
    if (HRStringUtils.equals("name", prop.getName())) hasName = true;
    if (HRStringUtils.equals("number", prop.getName())) hasNumber = true;
    if (!hasName || !hasNumber) continue;
    return;            // 找到 name + number 直接返回 OK
}
this.getView().showTipNotification("当前业务对象无编码或名称字段，不允许配置维度。");
```

**解决**：
- 选符合的业务对象（必须同时有 name 和 number 字段）
- 如果客户的业务对象字段命名不一样（如 `code` 不叫 `number`）· dimension 不支持 · 必须从源头改业务对象元数据加 number 字段

---

### 1.4 showtype 选了 tree 但被强制改回 list

**症状**：选 entitytype 后 · showtype 自动变成 list 且禁用了

**根因**：DimensionNewEdit.handleShowTypeForEntityType L207-L214：

```java
String mainEntityInheritPath = ... // 业务对象的 InheritPath
String baseEntityInheritPath = EntityMetadataCache.getDataEntityType("hbp_bd_treetpl_all").getInheritPath();
if (HRStringUtils.isEmpty(mainEntityInheritPath) || !mainEntityInheritPath.startsWith(baseEntityInheritPath)) {
    this.getModel().setValue("showtype", "list");
    this.getView().setEnable(Boolean.FALSE, new String[]{"showtype"});
}
```

**为什么**：`hbp_bd_treetpl_all` 是平台树形基础资料模板 · 业务对象不继承它就**没有树形数据结构** · 强制 list 显示。

**解决**：
- 业务对象本身没继承树形模板 · dimension 显示不了 tree · 这是合理设计
- 如果客户硬要 tree · 必须从源头改业务对象继承 hbp_bd_treetpl_all（成本高）
- 改成 list 显示也能用 · 只是 UI 没层级感

---

### 1.5 切到 datasource=hrbu 后 entitytype 自动改成 bos_org

**症状**：切到 datasource=hrbu · save 时发现 entitytype 自动变成 `bos_org` · 不是用户填的

**根因**：DimensionNewEdit.beforeDoOperation L278-L280：

```java
if (HRStringUtils.equals(this.getModel().getValue("datasource"), "hrbu")) {
    this.getModel().setValue("entitytype", "bos_org");
}
```

**为什么**：hrbu（职能类型）下游需要绑定到组织实体 · 平台标品统一改成 bos_org 简化处理。

**解决**：这是设计行为 · 不要绕过。如果 ISV 加了 hrbu 类型扩展 · 必须保留此规则。

---

### 1.6 切到 datasource=orgteam 后 entitytype 自动锁定 haos_adminorgdetail

**症状**：切到 datasource=orgteam · entitytype 字段 UI 自动设值并锁定为 `haos_adminorgdetail`

**根因**：DimensionNewEdit.showEnumCtrl L195-L196：

```java
} else if (HRStringUtils.equals("orgteam", datasource)) {
    ...
    this.getModel().setValue("entitytype", "haos_adminorgdetail");
    ...
}
```

**为什么**：orgteam（组织团队）类型下游必须绑行政组织实体（`haos_adminorgdetail` 是行政组织详情主实体）· 强制锁定避免错配。

**解决**：设计行为 · 不要绕过。

---

### 1.7 修改已被引用的枚举值反复弹"调整枚举值会影响角色维度"

**症状**：用户改 entry.value · save 弹确认 · 用户点 Yes · 又弹一次 · 反复

**根因**：DimensionNewEdit.checkEnumChange L320 弹确认 + setCancel(true)：

```java
this.getView().showConfirm("调整枚举值会影响角色维度·确定修改吗？", ...,
    new ConfirmCallBackListener("save_continue", this));
args.setCancel(true);
break;
```

用户点 Yes 走 confirmCallBack：

```java
if (callBackId.equals("save_continue") && result.equals(MessageBoxResult.Yes)) {
    this.getPageCache().put("hadConfirm", "confirmed");
    this.getView().invokeOperation("save");
}
```

第二次进 beforeDoOperation 时拿 hadConfirm 缓存：

```java
if ((hadConfirm = this.getPageCache().get("hadConfirm")) != null) {
    this.getPageCache().remove("hadConfirm");      // ⚠ 必须 remove
    args.setCancel(false);
    return;                                         // 跳过校验放行
}
```

**正常流程**：弹 1 次 · 用户 Yes · 第二次 invokeOperation("save") 进 beforeDoOperation 拿到 hadConfirm 直接放行。

**反复弹的原因**：
1. ISV 自建 FormPlugin 的 confirmCallBack 误清了 PageCache.hadConfirm（如调用 PageCache.clear）
2. 标品 setCancel(true) 后第二次没正确走 invokeOperation · 用户手动改了又点 save · 又触发一次 checkEnumChange

**解决**：
- 不要在 ISV FormPlugin 的 confirmCallBack 里清整个 PageCache · 只清自己用的 key
- 不要在 ISV beforeDoOperation 里调 PageCache.remove("hadConfirm") · 标品已处理

---

### 1.8 disable 二次确认按钮不响应

**症状**：列表选 dimension 点【禁用】 · 弹"禁用维度后..." · 用户点确定 · 但 dimension 没禁用

**根因**：DimensionList.confirmCallBack L37-L43：

```java
if (HRStringUtils.equals(callBackId, "disable_conform") && result.equals(MessageBoxResult.Yes)) {
    this.getView().invokeOperation("disable");
}
```

**可能原因**：
1. ISV 自建 ListPlugin 继承了 DimensionList 但没调 super.confirmCallBack（PR-001 教训 · 不要继承场景类）
2. ISV 自建 ListPlugin 用了 callBackId="disable_conform" 同名 · 跟标品冲突
3. dimension 当前 status 不允许 disable（如 status=A 暂存的不能 disable）

**解决**：
- 严守 PR-001 · 继承 HRDataBaseList · 不继承 DimensionList
- ISV 用自定义 callBackId 前缀（如 `${ISV_FLAG}_disable_conform`）
- 检查 dimension status · 必须是 status=C 已审核才能 disable

---

### 1.9 删除报"维度已被 X 引用 · 不能删除"

**症状**：删除 dimension · DimensionDeleteValidator 阻断 · 提示有引用

**根因**：DimensionDeleteOp.onAddValidators 注 DimensionDeleteValidator · validate 反查下游引用（标品反编译没读到具体 · 推断查 hrcs_entityctrl / dynascheme.condition / datarule）

**解决**：
- 先删除所有下游引用（dynascheme 删除 condition 中引用 dimension 的部分 · entityctrl 删除映射）
- 然后再删 dimension
- ISV 加 CS-04 · 反查 4 表 + 提示具体哪些引用 · 让用户更明确

---

### 1.10 非 HR 管理员打不开 dimension 表单

**症状**：用户点菜单进 dimension · 弹"您无法访问该功能·因为您不是HR领域管理员"

**根因**：HRAdminStrictPlugin.preOpenForm L29-L52：
1. 不是 admin user + 不是 cosmic user → 拒绝
2. 不是 HR admin user（HRAdminService.isHrAdmin()）→ 拒绝

**解决**：
- 让管理员把当前用户加到 HR 管理员组
- 或临时用 admin 账号操作
- F7 lookup 模式跳过此校验（fsp instanceof ListShowParameter && lsp.isLookUp()）· 所以下游通过 F7 引用 dimension 不受影响

---

## 二、共性陷阱（来自 cosmic_realworld_traps）

### 2.1 buildmeta 类陷阱（参考 buildmeta_traps.md）

ISV CS-01 加自定义字段 / CS-06 自建 ${ISV_FLAG}_dimension_audlog 时易踩：

- ❌ buildMeta 不传 parentId → 默认走 bos 基础资料模板 · 而不是 hbp_bd_tpl_all（HR 全页面模板）· 缺 issyspreset / orinumber 等 HR 通用字段
  - 实证 · `buildmeta_parent_template_trap.md` · 不会报错但行为错
  - 解决 · 显式传 `parent_id="hbp_bd_tpl_all"` 或 `2+QE4JA9QV27`
- ❌ EmployeeField 等 HR SDK 扩展类型 OpenAPI 不识别 · 兜底 BasedataField
  - 解决 · 用 hrpi_person 引用 + Java 插件代替
- ❌ HRMulPositionField / HRMulAdminOrgField / MulHisModelBasedataField 通过 OpenAPI 创建 · 兜底为 BasedataField
  - 解决 · 必要时用 IDEA 插件 Web UI 手工建

### 2.2 modifyMeta 类陷阱（参考 modifymeta_traps.md）

CS-01 加字段时：

- ❌ formId 写错（如写成 `hrcs_dimensionlist` 列表 form · 不是数据实体 hrcs_dimension）→ 假成功（modifyMeta 返回 ok 但字段没加到主实体）
- ❌ EmbedFormAp 假成功（buildMeta 嵌入子 form 时）· 与 dimension 无关 · 但 CS-06 自建表如要嵌入需注意
- ❌ ops 格式写错（必须 `[{op: "add", treeType: "entity", elementType: "field", parentScope: "...", element: {...}}]`）· 漏 treeType/parentScope 静默失败

### 2.3 addRule 类陷阱（参考 addrule_traps.md）

dimension 已有 9 条 formRule/bizRule（见 02_business_rules.md 第九章）· ISV 加新 rule 时：

- ❌ ActionType 用小写 → 应该 PascalCase（如 `SetVisible` 不是 `setvisible`）
- ❌ preCondition 用 `==''` 字符串相等 · 应该用平台 expression 函数 · 或者 `null` 判定
- ❌ 坏规则清理 · 加错的 rule 必须先 deleteRule · 不能直接 add 同 ruleId

---

## 三、PR 违规导致的异常（高频）

### 3.1 违反 PR-001 · 继承场景专属类导致的标品升级挂掉

**症状**：苍穹标品升级版本后 · ISV 类编译失败或运行时报 NoSuchMethodError

**根因**：ISV 写了 `extends DimensionNewEdit` 或 `extends DimensionList` 或 `extends DimensionDeleteOp` 之类继承标品场景类。标品升级改了私有方法签名（如 `private void showEnumCtrl(...)` 改名 `private void renderDataSource(...)`）· ISV 类挂掉。

**解决**：严守 PR-001 · 继承 SDK 白名单父类（HRDataBaseEdit / HRDataBaseList / HRDataBaseOp / AbstractValidator）。

### 3.2 违反 PR-004 · setValue 死循环

**症状**：propertyChanged 回调不断触发 · 调试器卡住或 StackOverflowError

**根因**：在 propertyChanged 里直接 `getModel().setValue(key, value)` · 触发又一次 propertyChanged · 死循环。

**解决**：用 `getModel().beginInit(); ... endInit(); getView().updateView(key);` 包围所有 setValue。

### 3.3 违反 PR-008 · 反查 HisModel 下游漏 iscurrentversion

**症状**：CS-04 反查 dynascheme 阻断删除 · 误报"被 X 引用"· 客户：明明已经改了不引用了 · 还在阻断

**根因**：反查 `hrcs_dynascheme.condition LIKE %dimensionId%` 没带 `iscurrentversion=true` → 历史版本误报。

**解决**：HisModel 类下游必带 PR-008 过滤。

### 3.4 违反 PR-010 · 在错误阶段发 BEC

**症状**：CS-05 发 BEC · 偶尔下游收到的 dimension 数据是上一版（异常）

**根因**：在 endOperationTransaction 阶段发 BEC · 主事务可能还未最终提交 · BEC 平台收到事件去拉数据时拉到的是老版本。

**解决**：必须在 afterExecuteOperationTransaction（PR-010 第 9 阶段）发。

### 3.5 违反 PR-011 · 自接 MQ

**症状**：客户技术总监审计 · ISV 自接 Kafka 发事件 · 不走平台 BEC

**根因**：ISV 想"灵活" · 跳过 BEC 直接 Kafka。

**问题**：BEC 平台有事件管理 / 订阅方注册 / 失败重试 · ISV 自接 Kafka 全要重写。

**解决**：用平台 BEC（CS-05）。

---

## 四、调试工具与排查路径

### 4.1 日志位置

| 场景 | 日志类型 | 路径 |
|---|---|---|
| BEC 发布失败 | platform log | `kd.bos.bec.api.IEventService` 调用日志 · 看 platform log |
| HR 操作日志 | HRBaseDataLogOp 写入 | `t_hrcs_eo_log` 表（HR 基础资料操作日志） |
| ISV 自建 OP 日志 | LogFactory.getLog | 搜应用日志 · 关键字: ISV 类全名 |
| 标品 DimensionDeleteValidator 阻断 | platform validate log | OperationResult.AllErrorOrValidateInfo 看 |

### 4.2 常用排查命令

```bash
# 反查 dimension 是否被 entityctrl 引用
SELECT * FROM t_hrcs_entityctrlentry WHERE fdimensionid = ?

# 反查 dimension 是否被 dynascheme.condition 引用（HisModel 注意）
SELECT id, fnumber FROM t_hrcs_dynascheme
WHERE fcondition LIKE CONCAT('%"dimension":"', ?, '"%')
   AND fiscurrentversion = '1'

# 反查 dimension 当前所有引用统计
-- 需要联合多表 · 用 ISV CS-04 的 Validator 实现复用
```

### 4.3 排查决策树

```
dimension 异常报错:
   ↓
是 UI 拦截还是 OP 拦截？
   ├─ UI 拦截（弹框 · 错误提示）→ 看 DimensionNewEdit.checkEntitytype/checkEnumChange
   ├─ Validator 阻断（save 后报错）→ 看 OperationResult.AllErrorOrValidateInfo
   ├─ 准入闸（preOpenForm 拒绝）→ 看 HRAdminStrictPlugin · 当前用户是不是 HR admin
   └─ 异常堆栈 → 看 platform log · 找异常类全名 · 反查 _decompiled/scenarios/hrcs_dimension/
```

---

## 五、监控与告警建议

ISV 上线 dimension 定制后建议监控：

1. **DimensionDeleteValidator 阻断率**（标品 + ISV CS-04 累计）
2. **DimensionNewEdit.checkEnumChange 二次确认弹出次数**
3. **enum 修改后 hrcs_dynascheme.condition LIKE 命中数变化**
4. **ISV BEC 发布失败率**（CS-05 · 失败时进 try/catch 写补发队列）
5. **`${ISV_FLAG}_dimension_audlog` 自建审计日志写入延迟**（CS-06 · 大客户量时关注）
6. **HRAdminStrictPlugin 拒绝率**（运维健康度 · 持续高表示权限配置错）

---

## 六、与 dynascheme 异常对比

| 异常类型 | dimension | dynascheme |
|---|---|---|
| 切换 datasource 数据丢失 | ✅ 是（设计） | ❌ 没有等价场景 |
| F7 业务对象限 BaseFormModel | ✅ 强约束 | ❌ 不限 |
| business object 必须有 number+name | ✅ 强约束 | ❌ 不需 |
| audit 后置 BEC 标品 0 处发布 | ✅ 是 | ✅ 是 |
| HisModel 时序 boid 跨版本 | ❌ 无 | ✅ 是 |
| confirmchange 列表批量"not support" | ❌ 无（dimension 没 confirmchange） | ✅ 是 |
| 切换 authaction 反复弹清规则确认 | ❌ 无（dimension 没 authaction） | ✅ 是（用 secondConfirmCancel 缓存） |
| disable 二次确认 | ✅ DimensionList | ❌ 没有 |
| 准入闸 HRAdminStrictPlugin 拒绝 | ✅ 是（hrcs 11 表单共用） | ✅ 是 |

→ dimension 比 dynascheme 异常种类少（少了 HisModel + confirmchange + authaction 联动相关的）· 但 datasource 4 路联动是 dimension 独有的复杂度。
