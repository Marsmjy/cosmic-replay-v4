# 数据流转 · 行政组织变动场景（haos_changescene）

> **状态**: 🟢 基于反编译 ChangeSceneSaveOp + opKey 执行链 + 物理表实证
> **维度定位**: 字段级数据落库 / 读写时机 / 事务边界
> **confidence**: verified

---

## 一、写入流（save 路径 · 反编译实证）

```
[用户保存动作]
   ↓
[onPreparePropertys] ChangeSceneSaveOp.onPreparePropertys
   ↓ 声明读：orgchangetype, changeoperat
[onAddValidators]    ChangeSceneSaveOp.onAddValidators
   ↓ 注册 ChangeSceneImportValidator
[beforeExecute]      HRBaseDataStatusOp / HRBaseDataLogOp / HRBaseOriginalOp 校验
   ↓ 校验通过 → 进事务
[beginOperationTransaction] ChangeSceneSaveOp ⭐
   ↓ if (importtype != null) 兜底联动 changeoperat
   ↓
[平台标准 SaveOp 执行落库]
   ├─→ 主表 t_haos_changescene
   │     • fnumber / forgchangetypeid / fotclassifyid 等
   │     • fcreatorid / fcreatetime（自动）
   │     • fenable=1 默认 / fstatus=A 默认
   │
   ├─→ 多语言表 t_haos_changescene_l
   │     • fname / fsimplename / fdescription / foriname (per locale)
   │
   ├─→ 子表 t_haos_cschangereason ⭐ MulBasedata
   │     • 每个 changereason 多选项 → 一行
   │
   └─→ 子表 t_haos_cschangeoperat ⭐ MulBasedata
         • 每个 changeoperat 多选项 → 一行
   ↓
[afterExecute] HRBaseDataLogOp 写日志
   ↓
[事务提交] commit
   ↓
[afterExecuteOperationTransaction]（事务后 · 标品没在此发 BEC ⚠ grep 实证）
```

---

## 二、字段级写入清单

### 2.1 用户输入字段

| 字段 key | 物理列 | 物理表 | 用户在表单填 |
|---|---|---|---|
| `number` | `fnumber` | t_haos_changescene | ✅（或 CodeRuleOp 自动生成）|
| `name` | `fname` | t_haos_changescene_l | ✅ |
| `simplename` | `fsimplename` | t_haos_changescene_l | ❌ 选填 |
| `description` | `fdescription` | t_haos_changescene_l | ❌ 选填 |
| `index` | `findex` | t_haos_changescene | ❌ 选填（默认 0）|
| `orgchangetype` | `forgchangetypeid` | t_haos_changescene | ✅ 必填（required=true）|
| `otclassify` | `fotclassifyid` | t_haos_changescene | ✅ 通常必填（默认 1010）|
| `changereason` | n/a（子表）| t_haos_cschangereason | ❌ 多选 |
| `changeoperat` | n/a（子表）| t_haos_cschangeoperat | 🔄 自动联动（不让用户手填）|
| `createorg` / `org` / `useorg` / `ctrlstrategy` | n/a | t_haos_changescene | 多组织字段 · 平台默认 |

### 2.2 系统自动写入字段（PR-007 · 系统维护）

| 字段 key | 物理列 | 写入时机 | 写入插件 |
|---|---|---|---|
| `creator` | `fcreatorid` | save 阶段 | 平台 BaseData |
| `createtime` | `fcreatetime` | save 阶段 | 平台 BaseData |
| `modifier` | `fmodifierid` | save 阶段 | 平台 BaseData |
| `modifytime` | `fmodifytime` | save 阶段 | 平台 BaseData |
| `masterid` | `fmasterid` | save 阶段 | 平台 BaseData |
| `enable` | `fenable` | save 阶段（默认 1）| HRBaseDataEnableOp |
| `status` | `fstatus` | save/submit/audit | HRBaseDataStatusOp |
| `disabler` | `FDisablerID` | disable 操作 | BaseDataDisablePlugin |
| `disabledate` | `FDisableDate` | disable 操作 | BaseDataDisablePlugin |
| `issyspreset` | `fissyspreset` | 平台升级时刷预置数据 | 平台升级脚本 |
| `initdatasource` | `finitdatasource` | 创建时根据来源标记 | HRBaseOriginalOp |
| `orinumber` / `oriname` / `oristatus` | `fori*` | 平台升级时刷出厂数据 | 平台升级脚本 |

---

## 三、读取流（核心调用点）

### 3.1 ChangeSceneEditPlugin（表单层）

```java
// ChangeSceneEditPlugin.java:23-34
String name = e.getProperty().getName();
if ("orgchangetype".equals(name)) {
    DynamicObject dynamicObject = (DynamicObject)this.getModel().getValue("orgchangetype");
    Long id = dynamicObject.getLong("id");        // 读 orgchangetype.id
    Long changeOperateId = ChangeSceneServiceHelper.getChangeOperate(id);
    this.getModel().setValue("changeoperat", new Object[]{changeOperateId});  // 写联动
}
```

| 读字段 | 时机 | 用途 |
|---|---|---|
| `orgchangetype.id` | propertyChanged | 决定要联动的 changeoperat |

### 3.2 ChangeSceneSaveOp（OP 层 · 导入路径）

```java
// ChangeSceneSaveOp.java:46-55
for (DynamicObject dataEntity : e.getDataEntities()) {
    long changeTypeId = dataEntity.getLong("orgchangetype.id");    // 读
    Long changeOperateId = ChangeSceneServiceHelper.getChangeOperate(changeTypeId);
    DynamicObjectCollection collection = dataEntity.getDynamicObjectCollection("changeoperat");
    collection.clear();  // 清子表
    DynamicObject changeOperateObj = new DynamicObject(collection.getDynamicObjectType());
    changeOperateObj.set("fbasedataid", (Object)changeOperateId);  // 写子表 fbasedataid
    collection.add(changeOperateObj);
}
```

| 读字段 | 写字段 | 时机 |
|---|---|---|
| `orgchangetype.id` · `changeoperat`（子表）| `changeoperat.fbasedataid` | beginOperationTransaction（仅 importtype!=null）|

### 3.3 ChangeSceneListPlugin（列表层）

```java
// ChangeSceneListPlugin.java:23-32
Object otclassifyid = formParameter.getCustomParam("otclassify");
if (otclassifyid == null) otclassifyid = 1010L;
event.getQFilters().add(new QFilter("otclassify.id", "=", otclassifyid));
event.getQFilters().add(new QFilter("id", "!=", 1070L));
```

→ **不写库** · 仅在列表查询时附加 QFilter。

---

## 四、事务边界（实证 OP 链 § 04 阶段 5-9）

| 事务阶段 | 边界事件 | 数据可回滚 |
|---|---|---|
| 进事务前 | `beforeExecuteOperationTransaction` 抛异常 → 不进事务 | n/a（未写库）|
| 事务内 | `beginOperationTransaction` → 开始落库 → `endOperationTransaction` | ✅ 平台自动 rollback |
| 事务提交后 | `afterExecuteOperationTransaction` | ❌ 已 commit · 不可回滚 |

**关键事实（实证 BEC grep）**：
- 反编译产物 `grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" decompiled/scenarios/haos_changescene/ → 0 处命中`
- → **本场景标品不在事务后发 BEC 事件** · 任何"基础资料变更触发下游通知"逻辑都需要 ISV 自建发布方（否则下游收不到 · CS-05 给反指引）

---

## 五、删除路径的数据风险（高危）

`opkeys/delete.json` 执行链：
```
1. BaseDataDeletePlugin (bos)
2. CodeRuleDeleteOp (bos · 释放编码池)
3. HRBaseDataStatusOp (hbp)
4. HRBaseDataLogOp (hbp)
```

**4 个插件 · 没有任何反向引用前置校验** · 删除一条 `haos_changescene` 主键时：

```sql
-- 平台行为：直接 DELETE
DELETE FROM t_haos_changescene WHERE fid = ?;
DELETE FROM t_haos_changescene_l WHERE fid = ?;
DELETE FROM t_haos_cschangereason WHERE fid = ?;
DELETE FROM t_haos_cschangeoperat WHERE fid = ?;
```

**孤儿外键风险**：
- 历史 `homs_orgbatchchgbill.entryentity` 主体 entry 的 `changescene` 字段 = 已删 id → F7 显示空
- 历史 `homs_orgbatchchgbill.add_entry / parent_entry / info_entry / disable_entry / merge_entry / split_entry` 的 6 个 `*_changescene` 字段 = 已删 id → 同上
- `haos_adminorgdetail.changescene`（已落地的组织详情）→ 列表展示 changescene.name 为空

→ **CS-02 解决**：在 onAddValidators 阶段加反向引用校验 · 拒绝删除已被引用的项。

---

## 六、跨场景数据传递（无 BEC · 走 BasedataField 引用）

```
haos_changescene · t_haos_changescene
     │ id (Long)
     │
     ├─── 被 t_haos_orgbatchchgbill.fchangesceneid                       引用
     ├─── 被 t_haos_orgbatchchgbill_addentry.fadd_changesceneid           引用
     ├─── 被 t_haos_orgbatchchgbill_parententry.fparent_changesceneid     引用
     ├─── 被 t_haos_orgbatchchgbill_infoentry.finfo_changesceneid         引用
     ├─── 被 t_haos_orgbatchchgbill_disableentry.fdisable_changesceneid   引用
     ├─── 被 t_haos_orgbatchchgbill_mergeentry.fmerge_changesceneid       引用
     ├─── 被 t_haos_orgbatchchgbill_splitentry.fsplit_changesceneid       引用
     └─── 被 t_haos_adminorgdetail.fchangesceneid                         引用
                       (admin_org form_lifecycle.json 实证 changescene 必填)
```

> 7 entry 物理表名以"基于 homs_orgbatchchgbill.scene_doc.json 的 entry name 推断"形式给出 · ISV 实施前应通过 OpenAPI getFormSchema 取目标环境真实物理列名（参考 PR-002 设计意图）。

---

## 七、读写字段 · 反编译实证矩阵

| 场景类 | 读字段 | 写字段 | 来源行号 |
|---|---|---|---|
| `ChangeSceneEditPlugin` | `orgchangetype.id` | `changeoperat`（联动）| L23-34 |
| `ChangeSceneSaveOp` | `orgchangetype.id` · `changeoperat`（子表 collection）| `changeoperat.fbasedataid` | L37-55 |
| `ChangeSceneListPlugin` | n/a（仅 QFilter）| n/a | L21-43 |

---

## 八、失败回滚策略

| 失败点 | 行为 |
|---|---|
| `onAddValidators` 校验未通过 | 抛异常 · 不进事务 · 用户在表单看到错误提示 |
| `beforeExecuteOperationTransaction` 抛异常 | 不进事务 · 同上 |
| `beginOperationTransaction` SQL 失败 | 平台自动 rollback · 主表 + 子表 + 多语言表全部回滚 |
| `afterExecuteOperationTransaction` 抛异常 | ⚠ 事务已 commit · 数据已落库 · 仅日志/通知失败 |

> ⚠ 标品**没在 afterExecute 阶段发 BEC** · 所以也不存在"事务已提交但事件未发出"的双不一致风险。
> ISV 若自建 BEC 发布方（如 CS-05 反指引中提到的"挂到 homs_orgbatchchgbill 上"）必须在 PR-010 第 9 步 afterExecuteOperationTransaction 触发 · 才能保证主事务已提交。
