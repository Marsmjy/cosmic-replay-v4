# 异常诊断 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于反编译实证 + cosmic_realworld_traps 共性陷阱
> **confidence**: verified
> **数据源**: 反编译 5 类 + 通用陷阱知识 (2026-04-28)

---

## 一、共性陷阱（来自 cosmic_realworld_traps）

按业务对象维度映射场景的相关性筛选：

### 1.1 buildmeta_traps（加字段时的坑）· 适用 CS-01

详见 `knowledge/cosmic_realworld_traps/buildmeta_traps.md`：

| 坑 | 本场景体现 | 规避 |
|---|---|---|
| EmployeeField OpenAPI 不支持 | ISV 想加员工字段加不进去 | 改用 BasedataField + baseEntityNumber=hrpi_person |
| 74 个 fieldType 枚举值（非 dataType / displayName） | ISV 加字段时 `fieldType` 拼错（如写 "ComboBox"）→ 静默走 TextField | 严格按枚举（"ComboField"/"MulComboField"/"BasedataField"/...） |
| 命名规则：key 必须 ISV 前缀 | `${ISV_FLAG}_remarktag` √ · `remarktag` ✗ | 加前缀避免标品升级覆盖 |
| fieldName（数据库列名）超 25 字符 / 手写 fk_ 前缀 | 苍穹会再加 f → 列名变 ffk_xxx 怪列名 | 不传 fieldName · 让平台自动生成 `f + key.lowercase()` |
| MulBasedataField 用隐式中间表 | 加字段时 OpenAPI 视作普通 BasedataField | 走 IDEA 插件 |

### 1.2 modifymeta_traps（改元数据时的坑）· 适用 CS-01

详见 `knowledge/cosmic_realworld_traps/modifymeta_traps.md`：

| 坑 | 本场景体现 | 规避 |
|---|---|---|
| modifyMeta add 参数名是 `fieldType/name/columnName` 不是 `dataType/displayName` | 拼错参数名 → 静默走 TextField | 严格按 OpenAPI 规范 |
| EmbedFormAp 假成功 | 加 EmbedFormAp 看起来 errorCode=0 但表单 UI 没生效 | 优先 IDEA 插件做嵌入表单 |
| ops 数组格式 | 单个 op 必须是数组中一个元素 · 不能是裸对象 | 始终用 `ops: [{...}]` |
| formId 错（如写成 hrcs_entityctrl_e 而非 hrcs_entityctrl） | 改不到正确实体 | 严格按 `_meta.formId` |

### 1.3 addrule_traps（加规则时的坑）· 本场景不适用

本场景标品 listRules = 0 条 · 不存在 addRule 冲突。但 ISV 加规则注意：

| 坑 | 规避 |
|---|---|
| ActionType 大小写错（PascalCase） | ENABLE → 写 `Enable`（首字母大写） |
| preCondition 不能用 `==''` | 用 `key isNotBlank` 替代 |
| 坏规则清理 | 创建规则失败 · listRules 仍能看到坏规则 · 必须 deleteRule 清掉 |

---

## 二、本场景特有陷阱（反编译实证）

### 2.1 ⚠ 改 entitytype 字段会清空所有分录

**实证**：`EntityCtrlEdit.propertyChanged` L339-L352

```java
if (HRStringUtils.equals("entitytype", name)) {
    this.getModel().deleteEntryData("entryentity");        // ⭐ 直接清空！
    DynamicObject entityType = dy.getDynamicObject("entitytype");
    ...
}
```

**陷阱**：用户编辑态点错 entitytype · 一变更就清空了所有分录 · 即使用户立刻改回原 entitytype 也补不回分录数据（清空的内存数据无 undo）。

**规避**：
- 业务 SOP：编辑态不要改 entitytype · 要改就先 delete 主记录再 new
- ISV 加 CS-03 自建 Validator · save 前对比 originPropDimInfo · 阻止 entitytype 在编辑态被改

### 2.2 ⚠ TX.requiresNew 独立事务造成数据不一致

**实证**：`EntityControlSaveOp.syncMustDimToRoleDim` L251-L258

```java
try (TXHandle tx = TX.requiresNew()) {
    try {
        serviceHelper.save(toUpdateRoleDimList);
    } catch (Exception ex) {
        tx.markRollback();
    }
}
```

**陷阱**：主事务已经在 endOperationTransaction 阶段（即将 commit）· 此时 TX.requiresNew() 开新事务写 hrcs_roledimension。如果新事务失败 · markRollback 只回滚新事务 · 主事务正常 commit —— **t_hrcs_entityctrl 落库但 hrcs_roledimension 没落库**（孤儿主记录）。

**症状**：
- 用户在 entityctrl 列表看到映射存在
- 但访问对应业务对象做权限校验时 · 角色范围少了 ismust 维度

**规避**：
- 客户人工：再点一次保存触发同步（自愈）
- ISV 不要模仿这个设计 —— 自建 OP 在 endOperationTransaction 用主事务（不要 requiresNew）

### 2.3 ⚠ delete 不级联清 hrcs_datarule / hrcs_dynaformctrl

**实证**：`EntityCtrlDelOp.beginOperationTransaction` L47-L70 只调 `EntityCtrlServiceHelper.deleteRoleRange`（清 hrcs_roledimension）

**陷阱**：删除 entityctrl 主记录后 · `hrcs_datarule` / `hrcs_dynaformctrl` 还引用旧 entitytype · 变成幽灵引用。

**症状**：
- 业务方反映"配的数据规则没生效"
- 后台日志找不到错误（数据规则计算时静默 skip 不存在的映射）

**规避**：
- 加 CS-04 自建 Validator · 阻止有引用时删除
- 后台脚本扫描孤儿引用人工清理

### 2.4 ⚠ FormPlugin propertyChanged + setValue 死循环

**陷阱**：ISV 自建 FormPlugin 在 `propertyChanged(entitytype)` 里 setValue 自建字段 → propertyChanged 二次触发 → 再 setValue → 死循环。

**实证标品做法**：`EntityCtrlEdit.propertyChanged` L339-L352 改 entitytype 后 setValue("bizapp", ...) —— 但 bizapp 没在 propertyChanged 里再处理 · 所以**实际上没死循环**。但 ISV 不要假设 · 必须 begin/endInit 包裹。

**规避**：
```java
this.getModel().beginInit();
try {
    this.getModel().setValue("${ISV_FLAG}_xxx", value);
} finally {
    this.getModel().endInit();
    this.getView().updateView("${ISV_FLAG}_xxx");
}
```

参考 `_shared/platform_rules.json` PR-004。

### 2.5 ⚠ TreeList 父类继承错（List vs TreeList）

**陷阱**：ISV 自建 ListPlugin 习惯继承 `kd.bos.list.plugin.AbstractListPlugin`（普通 List 父类）· 但本场景标品是 `EntityCtrlTreeListPlugin extends AbstractTreeListPlugin`（TreeList 父类）· 挂载位不对 · 列表壳是 TreeList 但 ISV plugin 是 List · 各种生命周期方法不被调用。

**规避**：
- 看 `_auto_plugin_registry.md` 确认列表父类
- 本场景 ISV 自建必须 `extends AbstractTreeListPlugin`

### 2.6 ⚠ 业务对象 F7 不显示某个实体

**实证**：`EntityCtrlEdit.bulidEntityFilters` L515-L533 4 闸过滤

**陷阱**：ISV 加自定义业务对象（如 ${ISV_FLAG}_customentity）后 · F7 看不到。可能原因：
- modeltype 不在 5 大允许类型（BaseFormModel/BillFormModel/QueryListModel/DynamicFormModel/ReportFormModel）→ 改 modeltype
- istemplate = "1" → 改成 "0"
- 已经被某条 entityctrl 配过 → 检查 hrcs_entityctrl 是否已存在
- 在 `EntityCtrlServiceHelper.queryEntityForBidInfo` 的 forbid 集 → 看是否被强制禁
- 在 `RoleManageService.getNoCtrlPermEntitysFromCache` 的非控权集 → 看角色配置
- **不在 `EntityCtrlServiceHelper.getAllHrHasPermItemEntity`**：业务对象必须被某 hrcs 角色配过权限项 → 配权限项后再来

**规避**：先去 hrcs / 权限管理 / 角色 给该业务对象配一个权限项 · 然后再来 entityctrl F7

### 2.7 ⚠ 维度 F7 显示空（"找不到符合条件的"）

**实证**：`EntityCtrlEdit.beforeF7Select(dimension)` L197-L237 6 分支过滤

**陷阱**：维度 F7 弹出但无任何选项。可能原因：
- propkey 在 `bdPropInfos` 但没在 `hrcs_dimension` 配该 entitytype 的维度（分支 5 没匹配）
- propkey 是组织字段但 buCaFunc id 不存在（分支 2 / 3 不命中）
- propkey 是普通字段但 datasource = orgteam 维度被排除（分支 6 命中但实际可选维度都是 orgteam 类型）

**调试**：
- 看后端日志找 `EntityCtrl dimensionFilter = {}` 行（实证 L234）
- 看打印的 QFilter 实际是哪种 datasource / entitytype · 跟 hrcs_dimension 表对照

**规避**：先在 hrcs_dimension 配齐对应维度 · 注意 datasource / entitytype / org_classify 字段对齐

### 2.8 ⚠ 添加行后 authrange 不可改

**实证**：`EntityCtrlEdit.closedCallBack` L490-L492

```java
if (noDBProps.contains(infos[0]) || this.isQueryEntityProp(fieldNum)) {
    this.getModel().setValue("authrange", "2", idx);
    this.getView().setEnable(Boolean.FALSE, idx, new String[]{"authrange"});
}
```

**陷阱**：用户从子页面加一个"虚字段" 或 "QueryEntity 多层引用字段"（如 `bizappid.org.id`）· 自动设 authrange="2" 并 disable 编辑 —— 用户不能改 authrange。

**症状**：用户反映"为什么这一行的控权范围灰色"

**规避**：业务 SOP · 这是预期行为（虚字段 / 多层引用字段控权粒度受限）

### 2.9 ⚠ HRAdminStrictPlugin 拦截非 HR 管理员

**实证**：`HRAdminStrictPlugin.preOpenForm` L29-L52

**陷阱**：超管能进 · 但普通业务用户进不去 · 提示"您无法访问该功能 · 因为您不是 HR 领域管理员"

**规避**：
- 给用户挂 HR 域管理员角色（hrcs / 角色 / 配角色）
- 或检查 `HRAdminService.isHrAdmin()` 内部逻辑（反编译没拿到具体规则）

---

## 三、典型异常与诊断脚本

### 3.1 "请填写维度映射数据"（save 失败）

**触发**：`EntityCtrlEdit.beforeDoOperation` L264-L266 · entryentity 为空

**修复**：用户加至少一行分录

### 3.2 "预置数据无法删除"（删行 / 删主记录失败）

**触发**：
- 删行：`EntityCtrlEdit.beforeDoOperation(deleteentry)` L280 · 选中行 issyspreset = true
- 删主：`EntityCtrlTreeListPlugin.beforeDoOperation(delete)` L98 · `EntityCtrlServiceHelper.beforeDelOp` 返回 false

**修复**：业务侧 · 预置映射不能删（正常约束）· 如确需删 · 后台 SQL 改 issyspreset = 0 后再删

### 3.3 "请选择业务对象"（添加行失败）

**触发**：`EntityCtrlEdit.itemClick(addrows)` L320-L321 · entitytype 为空

**修复**：用户先选业务对象再点添加行

### 3.4 "该实体没有属性 · 请重新选择"（添加行失败）

**触发**：`EntityCtrlEdit.itemClick(addrows)` L329-L330 · `bos_objecttype.isExists(number)` 返回 false

**修复**：选择的业务对象必须在 bos_objecttype 表中有记录（业务对象必须有属性元数据）

### 3.5 "请在虚字段数据控权配置中添加该业务对象的相关属性信息"（添加行失败）

**触发**：`EntityCtrlEdit.showFieldForm` L432-L435 · 业务对象是 dynamic/virtual 但 hrcs_dynaformctrl 没该 entitytype 记录

**修复**：先去 "hrcs / 虚字段数据控权配置" 给该业务对象加属性 · 再来 entityctrl 添加行

### 3.6 "您无法访问该功能 · 因为您不是 HR 领域管理员"（preOpenForm 失败）

**触发**：`HRAdminStrictPlugin.preOpenForm` L45 / L50

**修复**：业务侧给用户挂 HR 域管理员角色

---

## 四、性能陷阱

### 4.1 save 时 syncMustDimToRoleDim 性能差

**症状**：保存大批量映射（100+ 业务对象 / 50+ ismust=true 分录）· save 卡 30+ 秒

**原因**：`EntityControlSaveOp.assembleEntityRoleDim` L264-L332 + `syncMustDimToRoleDim` L187-L259 都是 N×M 复杂度（实体数 × 角色数 × 分录数）· 大批量时复杂度爆炸

**规避**：
- 业务侧分批保存（如每次 20 个）
- ISV 不要在 endOperationTransaction 加额外的批量计算

### 4.2 列表 setFilter 调跨模块 RPC 卡顿

**症状**：列表打开慢 · 每次刷新都要 5+ 秒

**原因**：`EntityCtrlTreeListPlugin.setFilter` 标品没调 RPC · 但 ISV 自建 setFilter 加了跨模块 RPC（如查 hrcs_dimension 反查）

**规避**：
- 用 `HRAppCache.get("hrcs")` 缓存 RPC 结果（标品做法 · 实证 `EntityCtrlTreeListPlugin.afterQueryOfExport` L116）
- 或把 RPC 逻辑挪到 afterQueryOfExport 阶段（导出时跑一次）

### 4.3 大量分录的 propname 计算慢

**症状**：表单打开慢（一条主记录有 200+ 分录时）

**原因**：`EntityCtrlEdit.beforeBindData` L154-L160 + `EntityCtrlTreeListPlugin.afterQueryOfExport` L124-L141 都遍历所有分录调 `EntityCtrlServiceHelper.getEntityFieldMap`

**规避**：
- 业务 SOP：一个业务对象映射的分录数控制在合理范围（< 100）
- 标品已用 `entityFieldMap` 缓存避免重复调用 · ISV 不要再加额外计算

---

## 五、调试速查表

| 怀疑现象 | 看哪里 | 关键日志 |
|---|---|---|
| save 失败 | OP 链 + Validator | `EntityCtrlEdit.beforeDoOperation` L264 / `EntityControlSaveValidator.validate`（待 javap） |
| save 后 hrcs_roledimension 没更新 | endOperationTransaction 独立事务 | log "TX.requiresNew" 异常 |
| F7 看不到业务对象 | bulidEntityFilters | 后端日志 entitytype.modeltype 不匹配 / 在 forbid 集 |
| F7 维度为空 | beforeF7Select(dimension) | "EntityCtrl dimensionFilter = {}" 实际过滤 |
| 添加行点不动 | itemClick(addrows) + showFieldForm | "请选择业务对象" / "该实体没有属性" / "请在虚字段..." |
| 删除失败 | beforeDoOperation(delete) | "预置数据无法删除" |
| 准入闸拒绝 | HRAdminStrictPlugin.preOpenForm | "您无法访问该功能 · 因为您不是 HR 领域管理员" |
| HR 权限缓存不一致 | clearAllCache 是否调用 | grep `HRPermCacheMgr.clearAllCache` 后端日志 |

---

## 六、参考文档

- `cosmic_realworld_traps/buildmeta_traps.md` — 加字段坑
- `cosmic_realworld_traps/modifymeta_traps.md` — 改元数据坑
- `cosmic_realworld_traps/addrule_traps.md` — 加规则坑
- `_shared/platform_rules.json` PR-001 ~ PR-011 — 11 条平台铁律
- `feedback_bec_3layer_async_publish.md` — BEC 三层异步发布教训
- `feedback_har_is_ground_truth.md` — HAR 是唯一真相源
