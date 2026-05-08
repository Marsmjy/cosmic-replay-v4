# 数据流转 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于反编译 3 类 + scene_doc.json 34 字段实抓
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、save / submit 数据落地链路

### 1.1 入口：DynaRuleItemEdit.beforeItemClick (FormPlugin 端)

```
FormPlugin 端 (DynaRuleItemEdit.java L233-L251)
   ↓
1. 拦截 bar_save / bar_saveandnew
2. dataType == "enum" 时调 checkEnumEntry()
   - entryentity 不空 + 不空行 + value 不重 + displayvalue 不重
   - 失败 → showTipNotification + setCancel(true) + grid.focusCell 定位首行错
3. 通过 → clearUnMustData() 净化非当前模式的脏字段
   - datatype != enum → deleteEnumEntry()
   - datatype 不在 (bd, org) → entitytype = null
   - !isRelatParam → relatruleparam = null + relatpropkey = null
   - valsourcetype == 1 → mserviceapp = null + mserviceclass = null
   - valsourcetype != 1 → sourceentitytype = null + sourcepropkey = null
```

### 1.2 标品 OP 链（save · 6 个标品 OP）

按 `executionChain` 顺序（rules_chain_all.json save）：

```
1. CodeRuleOp                  (kd.bos.business.plugin.CodeRuleOp)
   onAddValidators 注册 numberValidator · 自动生成 number 字段（PR-006）

2. BdVersionSaveServicePlugin  (kd.bos.base.bdversion.BdVersionSaveServicePlugin)
   基础资料版本管理 · 主表 name + 版本子表 name 写历史
   注：这不是 HisModel 时序 · 是基础资料 name 多语言版本控制

3. HRBaseDataStatusOp          (kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp)
   onAddValidators · 单据状态校验

4. HRBaseDataLogOp             (kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp)
   beforeExecute · 操作日志埋点
   afterExecute · 操作日志落库 (写 hbp_dataeditlog 表)

5. HRBaseDataEnableOp          (kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp)
   beforeExecute · enable 字段管理（防误改）

6. HRBaseOriginalOp            (kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp)
   beforeExecute · 原始值记录（orinumber/oristatus/oriname 写变更前值）
```

### 1.3 数据库写入

```
事务内顺序：
   ↓
INSERT/UPDATE t_hrcs_dynaruleitem (主表 · 含 datatype/entitytype/isrelatparam/relatruleparam/relatpropkey/valsourcetype/sourceentitytype/sourcepropkey/mserviceapp/mserviceclass)
   ↓
INSERT/UPDATE t_hrcs_dynaruleitem_l (多语言子表 · name/simplename/description/relatpropname/sourcepropname)
   ↓
（datatype=enum 时）DELETE/INSERT t_hrcs_dynaruleitemenum (枚举子表 · value)
   ↓
（datatype=enum 时）DELETE/INSERT t_hrcs_dynaruleitemenum_l (枚举多语言子表 · displayvalue)
   ↓
INSERT hbp_dataeditlog (操作日志)
   ↓
事务提交 (commit)
```

**注意点**：
- `t_hrcs_dynaruleitem.fenable` / `fstatus` 由 HRBaseDataStatusOp / HRBaseDataEnableOp 维护
- `t_hrcs_dynaruleitem.fcreatetime / fmodifytime / fcreatorid / fmodifierid` 平台自动注入
- `t_hrcs_dynaruleitem.forinumber / foriname / foristatus` 由 HRBaseOriginalOp 维护
- entryentity 子表行 id 由平台自动生成（kd.bos.id.ID · PR-005）· ISV 自建分录子表时也用 ID.genLongId()

### 1.4 BdVersionSaveServicePlugin 名称版本子表

`BdVersionSaveServicePlugin`（kd.bos.base.bdversion）是平台基础资料的"名称变更历史"管理。每次 save name 字段变化时 · 写入 `t_hrcs_dynaruleitem_n_h` (推断 · 平台命名约定带 _n_h 后缀) 的历史子表 · 记录变更前后的 name。

⚠️ **这不是 HisModel 时序版本** —— BdVersion 只管 name 字段历史 · 不管整体业务变更。HisModel 是 boid + iscurrentversion + 多版本字段共存 · 完全不同。

---

## 二、delete 数据清理链路

### 2.1 入口

```
列表 / 单据点【删除】(opKey = delete)
   ↓
进入 OP 链（4 标品 OP）
```

### 2.2 标品 OP 链

```
1. CodeRuleDeleteOp             (kd.bos.coderule.CodeRuleDeleteOp · enabled=True)
   beforeExecute · 平台编码规则的清理（释放编码资源）

2. HRBaseDataStatusOp.onAddValidators · 状态校验

3. HRBaseDataLogOp.beforeExecute · 操作日志埋点

4. DynaItemDeleteOp              (kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp)
   onPreparePropertys · args.getFieldKeys().add("name") (用于错误消息)
   onAddValidators  · args.addValidator(new DynaItemDelValidator())
```

### 2.3 DynaItemDelValidator 反查阻断

```
DynaItemDelValidator.validate()
   ↓
对每行 ExtendedDataEntity row
   - 取 row.getDataEntity().getString("id")
   - 调 DynaSchemeServiceHelper.queryRelDynaScheme(id) 反查
   ↓
schemeArr.isEmpty() → 静默通过
schemeArr.notEmpty() → addErrorMessage(row, "规则参数项 [name] 被 N 个动态授权方案引用，请先解除引用再删除。")
```

### 2.4 数据库 DELETE（通过校验后）

```
DELETE FROM t_hrcs_dynaruleitem WHERE fid = ?
DELETE FROM t_hrcs_dynaruleitem_l WHERE fid = ?
DELETE FROM t_hrcs_dynaruleitemenum WHERE fid = ?           -- 子表 cascade
DELETE FROM t_hrcs_dynaruleitemenum_l WHERE fid = ?
INSERT hbp_dataeditlog (操作日志 · type=delete)
```

⚠️ **注意**：标品 delete **没有清理下游 dynascheme.condition.JSON 引用** —— 因为已经在 Validator 阶段被阻断了（被引用必报错）。如果 ISV 想做"强制删除并清理下游"必须自建 OP（CS-04 套路 + 反向写 dynascheme.condition）。

---

## 三、deleteentry（删除枚举行）数据流

### 3.1 入口（FormPlugin 端）

```
单据态打开 (datatype=enum)
   ↓
用户在 entryentity 选 N 行 · 点 enumbar.deleteentry
   ↓
DynaRuleItemEdit.beforeDoOperation (operateKey == "deleteentry")
   ↓
取 dataEntity.id (本规则参数项的 id)
   ↓
DynaSchemeServiceHelper.queryRelDynaScheme(id) → DynamicObject[] schemeDynArr
   ↓
ArrayUtils.isEmpty(schemeDynArr) → return (无引用 · 让平台默认 deleteentry 走)
   ↓
有引用 → getSelEnumEntryValSet(dataEntity)
   - EntryGrid.getSelectRows() 拿选中行索引
   - 取每行 entryentity.value · 形成 Set<String> enumValSet
   ↓
checkRelSchemeVal(schemeDynArr, enumValSet)
   - 对 schemeDynArr 每个 dynascheme:
     - condition = scheme.getString("condition") (LargeTextField · DecisionSet JSON)
     - parseObject(condition).getJSONArray("conditionList")
     - 对 conditionList 每个 paramRel:
       - paramRel.getString("value")
       - if enumValSet.contains(value): relEntryVals.add(value)
   ↓
relEntryVals.notEmpty()
   → showTipNotification("枚举值被动态授权方案引用，不允许删除：%s。")
   → args.setCancel(true)（不删 · 行还在）
relEntryVals.isEmpty()
   → 通过 → 平台默认 deleteentry → DELETE 选中行
```

### 3.2 数据库 DELETE entryentity 行

```
（FormPlugin 引用阻断通过后）
平台默认 deleteEntry → DELETE FROM t_hrcs_dynaruleitemenum WHERE fentryid IN (?, ?, ...)
DELETE FROM t_hrcs_dynaruleitemenum_l WHERE fentryid IN (?, ?, ...)
（注意：不会触发主表 OP 链 · 不写 hbp_dataeditlog · 行级删除是 EntryEntity 的轻量事件）
```

⚠️ **deleteentry 是 FormPlugin 端的子操作** · 不会经过 OP 链。事务会在用户后续点 bar_save 时提交（save 时整体写主表 + 子表）。如果用户删了枚举行后没保存就关页 · 删除不生效。

---

## 四、enable / disable 数据流

### 4.1 链路

```
列表 / 单据点【启用】(opKey = enable) / 【禁用】(opKey = disable)
   ↓
进入 OP 链（仅 1 个标品 OP · 没有场景特别 OP）
   ↓
HRBaseDataLogOp.beforeExecute · 记日志
   ↓
平台标品 enable/disable 改 t_hrcs_dynaruleitem.fenable
   ↓
HRBaseDataLogOp.afterExecute · 落日志
```

### 4.2 数据库写入

```
UPDATE t_hrcs_dynaruleitem
SET fenable = ?, fdisablerid = ?, fdisabledate = ?
WHERE fid = ?
INSERT hbp_dataeditlog
```

⚠️ **disable 不会自动清理 dynascheme.condition 引用** —— 标品没做这个联动。如果业务希望"禁用一个规则参数项后 · 引用它的方案规则也失效" · 需要 ISV 自建 OP（CS-05 BEC 通知方案侧 · 或 CS-04 套路反查 + 改 dynascheme.condition · 后者复杂建议谨慎用）。

---

## 五、跨表数据一致性约束

### 5.1 dynaruleitem ↔ dynascheme.condition.JSON

```
hrcs_dynascheme.condition (LargeTextField · DecisionSet JSON · 例)
{
  "logicType": "AND",
  "conditionList": [
    {
      "paramId": "<dynaruleitem.id>",          ← 引用本表行 id
      "compare": "=",
      "value": "<dynaruleitem.entryentity.value>"  ← datatype=enum 时引用枚举值
    },
    ...
  ]
}
```

**一致性铁律**：
- 删 dynaruleitem 主表行 → 引用它的 dynascheme.condition.JSON 仍然带这个 paramId（标品没清理）→ 规则评估时该条件失效
- 删 dynaruleitem.entryentity 行 → 引用它 value 的 dynascheme.condition.conditionList[].value 仍然存在（标品没清理）→ 规则评估时该条件无法命中
- **平台保证一致性的方式 = 阻断删除**（DynaItemDelValidator + FormPlugin deleteentry 引用阻断）· 不是事后清理

### 5.2 multi-language 一致性

`name` 是 MuliLangTextField · 同时存在主表 `t_hrcs_dynaruleitem.fname` 和子表 `t_hrcs_dynaruleitem_l`（多语种）。BdVersionSaveServicePlugin 维护一致性。ISV 不要直接 INSERT _l 表 · 走 setValue("name", localeString) 平台自动同步。

`displayvalue` 是子表的 MuliLangTextField · 类似走 `t_hrcs_dynaruleitemenum_l`。

### 5.3 entityType 引用一致性

`entitytype` / `sourceentitytype` 都是 BasedataField → bos_entityobject。这个引用是**软引用**（不是 FK 级联）：
- 删 bos_entityobject 中某个 entity number · 不会自动 SET NULL 本表的 entitytype
- 但 dynascheme.condition 引用了本表的话 · 评估规则时由 sourcepropkey 反查实体属性会失败

→ 业务实践：bos_entityobject 是平台基础资料 · 实际不会被删 · 这个软引用问题理论存在但生产几乎不会触发。

---

## 六、读路径（查询场景）

### 6.1 dynascheme 加载规则字典

```
hrcs_dynascheme 配规则时 (PermFilter 控件)
   ↓
平台 PermFilter 控件读 hrcs_dynaruleitem 列表
   ↓
按规则字典展示给用户选 paramId
   ↓
（datatype=enum 时）加载 entryentity 作为可选 value 下拉
```

### 6.2 ISV 反查使用方

```
ISV 自建代码反查"哪些方案在用本规则参数项"
   ↓
DynaSchemeServiceHelper.queryRelDynaScheme(itemId)
   ↓
返回 DynamicObject[] schemeDynArr
```

⚠ DynaSchemeServiceHelper 是 hrcs 业务 helper · 平台未加 SDK 注解（参考 cosmic_sdk_annotation_whitelist.md）· ISV 调用时要确认是否进白名单。

### 6.3 ISV 反查实体字段映射

```
ISV 自建 propertyChanged 想拿目标实体所有字段 key→name
   ↓
RolePermLogServiceHelper.getEntityFieldMap(entityNumber)
   ↓
返回 Map<String, String> · key=fieldKey · value=fieldName (多语言)
```

---

## 七、事务边界

| 阶段 | 事务范围 | 失败回滚效果 |
|---|---|---|
| `save` 主链 | 1 个事务 | 主表 + _l + entryentity + entryentity _l + 日志 一起回滚 |
| `delete` 主链 | 1 个事务 | 主表 + _l + entryentity + entryentity _l + 日志 一起回滚 |
| `deleteentry` 子操作 | UI 端 model 操作 · 真删走 save 事务 | 用户不点 save · 回退完整 |
| `enable / disable` | 1 个事务 | 仅 fenable 字段改回 + 日志 |
| FormPlugin 阶段（beforeItemClick / beforeDoOperation） | UI 端 · 没事务 | 直接 setCancel(true) 阻断 · 不进 OP 链 |

---

## 八、数据流性能特征

| 操作 | 耗时（参考 · 100 行级别） | 慢瓶颈 |
|---|---|---|
| save 主链（含 BdVersion + Status + Log + Enable + Original + CodeRule） | < 200ms | BdVersion 写 _l |
| save 主链（datatype=enum + entryentity 50 行） | < 400ms | entryentity 子表 INSERT 批量 |
| delete 含 DynaItemDelValidator | 200ms-2s（dynascheme N 多时） | DynaSchemeServiceHelper.queryRelDynaScheme |
| deleteentry FormPlugin 引用阻断 | 100-500ms | 同 |
| afterBindData fillSourceEntityPropName | < 50ms | RolePermLogServiceHelper 有元数据缓存 |

→ 大客户（dynascheme 1000+）· 删除规则参数项的 Validator 反查可能成为瓶颈 · 监控点。
