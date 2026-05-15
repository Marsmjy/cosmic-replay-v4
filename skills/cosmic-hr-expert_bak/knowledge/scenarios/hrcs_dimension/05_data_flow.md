# 数据流转 · 维度管理 (hrcs_dimension)

> **状态**: 🟢 基于反编译 4 类（DimensionNewEdit / DimensionList / DimensionDeleteOp / HRAdminStrictPlugin）+ scene_doc.json 33 字段实抓
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、save 数据落地链路

### 1.1 入口：DimensionNewEdit.beforeDoOperation

```
FormPlugin 端 (DimensionNewEdit.java L272-L296)
   ↓
1. setValue(modifytime, new Date())                       // 强制修改时间
2. datasource=hrbu → setValue(entitytype, "bos_org")      // hrbu 类型强制业务对象
3. PageCache.get("hadConfirm") != null → 跳过校验放行     // 二次确认通过的二次进入
4. checkEntitytype 5 步校验:
   - datasource=basedata + entitytype 空 → 报错阻断
   - datasource=basedata + showtype=checkbox → 报错阻断
   - datasource=basedata + showtype=tree + entitytype 不继承 hbp_bd_treetpl_all → 报错阻断
   - datasource=enum + entry 空 → 报错阻断
   - datasource=enum + entry.displayvalue 重复 → 报错阻断
5. EDIT + datasource=enum → checkEnumChange 比对 DB
   - 改了 entry value → 弹"调整枚举值会影响角色维度·确定修改吗？"二次确认
   - 用户点 Yes → setPageCache("hadConfirm") + invokeOperation("save") (走二次进入路径)
   - 用户点 No → args.setCancel(true) 阻断
```

### 1.2 标品 OP 链（save · 6 个标品 OP · 无场景专属）

按 `executionChain` 顺序：

```
1. CodeRuleOp                  (kd.bos.business.plugin.CodeRuleOp)
   onAddValidators 注册 numberValidator · 自动生成 number 字段（PR-006）
2. BdVersionSaveServicePlugin  (kd.bos.base.bdversion.BdVersionSaveServicePlugin)
   基础资料版本管理 · 主表 name 写历史
3. HRBaseDataStatusOp          (kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp)
   onAddValidators · 单据状态校验
4. HRBaseDataLogOp             (kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp)
   beforeExecuteOperationTransaction · 操作日志埋点
   afterExecuteOperationTransaction · 操作日志落库
5. HRBaseDataEnableOp          (kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp)
   beforeExecuteOperationTransaction · enable 字段管理
6. HRBaseOriginalOp            (kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp)
   原始值记录（变更前后对比）
```

⚠️ **dimension 没有场景专属 SaveOp**（与 dynascheme 不同 · dynascheme 有 DynaAuthSchemeOp/SaveSubmitOp 4 个场景 OP）。**dimension save 不灌任何下游表** —— 它是基础资料 · 下游引用方需要时再来反查。

### 1.3 落库的物理表（按字段）

| 字段 / 分录 | 物理表 | 落库时机 |
|---|---|---|
| 主字段 number/name/datasource/entitytype/authtype/showtype/hrbu/... | `t_hrcs_dimension` | save 标品 OP 链落库 |
| `entry` 分录（datasource=enum 时） | `t_hrcs_dimensionenum` | save 时分录子表标准落库 |
| `org_classify` （MulBasedataField） | `t_hrcs_dimorgclass` | save 时多对多中间表标准落库 |
| `ctrlentry` 虚拟分录（不落库） | **不落** | UI 反查显示用 |

⚠️ ctrlentry 虚拟分录的真实落库位置在 `t_hrcs_entityctrl.entryentity`（业务对象维度映射的子表）· **不在 dimension 表自身**。

---

## 二、afterBindData 反查链路（EDIT 状态）

### 2.1 ctrlentry 灌库流程（DimensionNewEdit.afterBindData L104-L143）

```
EDIT 状态进入 dimension 表单时：
   ↓
1. 取 model.datasource → showEnumCtrl(datasource) 重置 UI/必填
2. 取 model.id (dimensionId)
3. 反查 hrcs_entityctrl 表:
   QFilter("entryentity.dimension", "=", dimensionId)
   query("entitytype, entryentity.dimension, propkey, authrange, ismust, issyspreset, desc")
4. 对每行 hrcs_entityctrl (假设有 N 行):
   - filter entry where dimension.id == dimensionId (拿到引用本 dimension 的 entry 行)
   - 对每个有效 entry 行:
     - createNewEntryRow(KEY_CTRL_ENTRY)  // 在 ctrlentry 加新行
     - setValue(entity, entitytype.number)
     - setValue(authrange, entry.authrange)
     - setValue(propkey, entry.propkey)
     - setValue(propname, entityFieldMap.get(propkey) ?? propkey)
     - setValue(issyspreset1, entry.issyspreset)
     - setValue(ismust, entry.ismust)
     - setValue(desc, entry.desc)
5. setVisible(true, "entityctrl")  · 显示 ctrlentry 区域
6. updateView(KEY_CTRL_ENTRY) · 强制刷新
7. setDataChanged(false) · 防止误标"已变更"
```

⚠️ **EntityCtrlServiceHelper.getEntityFieldMap 调用**：取业务对象的 propKey → 显示名 mapping · 如 `("name", "员工姓名")`。这是 hrcs 内部 helper · 部分 SDK 注解未公开 · 调用前查白名单。

### 2.2 enum value 锁定（已被引用时 · L160-L168）

```
showEnumCtrl(datasource="enum") 时:
   ↓
反查 EntityCtrlServiceHelper.getRoles(dimensionId)  // 取所有引用本维度的角色清单
roles.count() > 0 时:
   for index in 0..size:
      setEnable(false, index, ["value"])     // entry value 字段全部锁定
   roles.close()                              // ⚠ 必须关流
```

→ ISV 自建逻辑加 enum 字段时 · 必须考虑这个"已引用锁定"机制 · 不要绕过。

---

## 三、disable 数据落地链路

### 3.1 入口：DimensionList.beforeItemClick

```
列表点【禁用】(itemKey=tbldisable):
   ↓
1. showConfirm "禁用维度后·不允许在'业务对象维度映射'中使用·已有的角色维度的数据权限不受影响"
   callBackId=disable_conform
2. evt.setCancel(true)  // 先取消默认行为
3. 用户点 Yes → confirmCallBack:
   - HRStringUtils.equals(callBackId, "disable_conform") + Yes
   - getView().invokeOperation("disable")  // 走 disable opKey
```

### 3.2 disable OP 链（仅 1 标品 · 极简）

```
1. HRBaseDataLogOp           // 操作日志
   实际执行 enable=0 的是 HRBaseDataEnableOp.beforeExecuteOperationTransaction
   (上层 OP plugins.json 实证 · 不在 disable 主链 · 但会被框架触发)
```

⚠️ **disable 不清下游绑定**（标品提示文案明确："已有的角色维度的数据权限不受影响"）· ISV 想在 disable 后清理 hrcs_userrolerelat 等下游 · 必须挂新 OP 到 disable 的 afterExecuteOperationTransaction（PR-010 · CS-06）。

---

## 四、delete 数据落地链路

### 4.1 入口：DimensionDeleteOp.onAddValidators

```
delete 触发:
   ↓
1. DimensionDeleteOp.onAddValidators (L19-L21)
   - args.addValidator(new DimensionDeleteValidator())   // 注册自定义 Validator
2. 校验阶段执行 DimensionDeleteValidator.validate (反编译没读到)
   - 推断: 反查下游引用 (hrcs_entityctrl / hrcs_dynascheme.condition / hrcs_datarule)
   - 任一有引用 → addErrorMessage("不能删除：维度已被 X 个 Y 引用") + 阻断
3. 通过校验 → 走标品 delete 链:
   - CodeRuleDeleteOp                · 编码规则反向（释放编码段）
   - HRBaseDataStatusOp              · 单据状态校验
   - HRBaseDataLogOp                 · 操作日志
4. 物理表删除:
   - DELETE FROM t_hrcs_dimension WHERE fid IN (...)
   - DELETE FROM t_hrcs_dimensionenum WHERE fid IN (entry.id of dimension)
   - DELETE FROM t_hrcs_dimorgclass WHERE fpkid (org_classify 中间表) IN (...)
```

### 4.2 删除的级联范围（推断 + 标准平台行为）

| 表 | 是否级联删除 | 备注 |
|---|---|---|
| `t_hrcs_dimension` | ✅ 主表必删 | DELETE WHERE id |
| `t_hrcs_dimensionenum` | ✅ 子表必删 | 苍穹平台标准分录子表级联（PR-002 父子表） |
| `t_hrcs_dimorgclass` | ✅ 中间表必删 | MulBasedataField 平台标准 |
| `t_hrcs_entityctrl.entryentity` 引用 | ❌ 不删（DimensionDeleteValidator 应已阻断） | 上游被引用方不能删 |
| `t_hrcs_dynascheme.condition` JSON 内嵌引用 | ❌ 不删（DimensionDeleteValidator 应已阻断） | 配置 JSON 不感知 |
| `t_hrcs_datarule` 引用 | ❌ 不删（DimensionDeleteValidator 应已阻断） | 数据规则不允许悬空引用 |
| `t_hrcs_dynaschemerole.dimension` 引用 | ❌ 不删（DimensionDeleteValidator 应已阻断） | 角色清单分录引用 |

---

## 五、submit / audit / unaudit 数据流（极简）

dimension 的 submit/audit/unaudit 都是**纯标品** · 没有场景专属 OP · 只改 status 字段：

```
submit (status A → B):
   - CodeRuleOp / BdVersionSaveServicePlugin / HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp 标品
   - 标品 SubmitOp（隐含在 platform）改 status=B
   - 数据落地: t_hrcs_dimension.fstatus = 'B'

audit (status B → C):
   - 仅 HRBaseDataLogOp 标品
   - 平台 AuditOp 改 status=C
   - 数据落地: t_hrcs_dimension.fstatus = 'C'

unaudit (status C → A):
   - 仅 HRBaseDataLogOp 标品
   - 平台 UnAuditOp 改 status=A
   - 数据落地: t_hrcs_dimension.fstatus = 'A'
```

⚠️ ISV 加 audit 后置同步逻辑必须**新挂 OP 到 audit 的 afterExecuteOperationTransaction**（PR-010）· 不要继承 DimensionDeleteOp / DimensionNewEdit 等场景类。

---

## 六、refrole 跳转子页面 · 无数据写入

`DimensionNewEdit.beforeItemClick refrole` (L259-L270) 仅打开 hrcs_refdetails Modal 子页面 · **没有数据库写入** · 仅传 customParam("dimension.id") 给子页面用作反查 · 显示是只读的。

---

## 七、典型 SQL 模板（调试 / ISV 反查参考）

### 7.1 查 dimension 是否被 hrcs_entityctrl 引用

```sql
SELECT entityctrl.fid, entityctrl.fnumber, entry.fdimensionid
FROM t_hrcs_entityctrl entityctrl
JOIN t_hrcs_entityctrlentry entry ON entry.fid = entityctrl.fid
WHERE entry.fdimensionid = ?     -- dimensionId
```

### 7.2 查 dimension 是否被 dynascheme.condition 引用

```sql
-- condition 字段是 DecisionSet JSON · 文本搜索
SELECT fid, fnumber, fname, fcondition
FROM t_hrcs_dynascheme
WHERE fcondition LIKE CONCAT('%', ?, '%')   -- 嵌入 dimensionId · 注意 JSON 转义
   AND fiscurrentversion = '1'              -- 时序当前版本（PR-008）
```

⚠️ DecisionSet JSON 内嵌 dimensionId · 只能 LIKE 文本匹配 · 平台没有结构化反查 helper（截至 8.0 反编译实证）。

### 7.3 查 dimension 关联的角色清单

```sql
SELECT role.fid, role.fnumber, role.fname
FROM t_perm_role role
JOIN t_hrcs_entityctrlentry entry ON entry.fdimensionid = ?
   AND ... -- 角色与 entityctrl 的关联（待实证）
```

→ 推荐用 `EntityCtrlServiceHelper.getRoles(dimensionId)` 标品 helper · 不要自己拼 SQL（标品做了缓存）。

---

## 八、事务边界

| 事务 | 包含的写操作 |
|---|---|
| save 事务 | t_hrcs_dimension 主表 + t_hrcs_dimensionenum entry 分录 + t_hrcs_dimorgclass 多选分类中间表 + t_hrcs_eo_log 操作日志（HRBaseDataLogOp 异步） |
| delete 事务 | t_hrcs_dimension 主表 + t_hrcs_dimensionenum + t_hrcs_dimorgclass + t_hrcs_eo_log |
| disable 事务 | t_hrcs_dimension.fenable=0 + t_hrcs_eo_log |

⚠️ HRBaseDataLogOp 的日志写入是**同事务**还是**异步**待确认 · 反编译没读到 implementation 细节 · 建议参考 hbp_common 反编译。

---

## 九、与 dynascheme 数据流的关键差异

| 维度 | hrcs_dimension | hrcs_dynascheme |
|---|---|---|
| save 走的场景 OP | 0 个（纯标品） | 2 个（DynaAuthSchemeOp + DynaAuthSchemeSaveSubmitOp） |
| save 灌下游派生表 | 0 张 | 6 张（search_param/adminorg/pos/job/actiona/actionc） |
| audit 走的场景 OP | 0 个 | 1 个（DynaAuthSchemeAuditOp · entryboid 重映射） |
| confirmchange | ❌ 无 | ✅ 有（DynaAuthSchemeConfirmChangeOp 双写 boid + bgVid） |
| 主表事务边界 | 仅 1 张 + 2 张子表 | 1 主表 + 多个分录 + 多角色 + 6 反查派生表 |
| 删除级联清理范围 | 1 主表 + 2 子表（标准平台） | 5+ 配置表 + roleentry |

→ dimension 的 OP 链特别"轻" · ISV 加业务复杂度的余地很大（比 dynascheme 容易扩展 · 但要注意被多下游引用的影响面）。
