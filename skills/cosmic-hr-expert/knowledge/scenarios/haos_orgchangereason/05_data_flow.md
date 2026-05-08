# 数据流转 · 行政组织变动原因（haos_orgchangereason）

> **状态**: 🟢 基于反编译 BaseDataBuOp + opKey 执行链 + 物理表实证
> **维度定位**: 字段级数据落库 / 读写时机 / 事务边界
> **confidence**: verified

---

## 一、写入流（save 路径 · 反编译实证）

```
[用户保存动作]
   ↓
[onAddValidators]    BaseDataBuOp.onAddValidators
   ↓ 注册 CtrlStrategyValidator
[beforeExecute]      HRBaseDataStatusOp / HRBaseDataLogOp / HRBaseOriginalOp 校验
   ↓ 校验通过 → 进事务
[beginOperationTransaction] （本场景没有场景特有逻辑 · 走平台默认）
   ↓
[平台标准 SaveOp 执行落库]
   ├─→ 主表 t_haos_orgchangereason
   │     • fnumber / fotclassify / fissyspreset 等
   │     • fcreatorid / fcreatetime（自动）
   │     • fenable=1 默认 / fstatus=A 默认
   │
   └─→ 多语言表 t_haos_orgchangereason_l
         • fname / fsimplename / fdescription / foriname (per locale)
   ↓
[afterExecute] HRBaseDataLogOp 写日志
   ↓
[事务提交] commit
   ↓
[afterExecuteOperationTransaction]（事务后 · 标品没在此发 BEC ⚠ grep 实证）
```

> 📌 **跟 haos_changescene 的关键差异**：本场景**没有任何 MulBasedata 子表写入**（haos_changescene 还要写 t_haos_cschangereason / t_haos_cschangeoperat）· 写入路径**最简洁**。

---

## 二、字段级写入清单

### 2.1 用户输入字段

| 字段 key | 物理列 | 物理表 | 用户在表单填 |
|---|---|---|---|
| `number` | `fnumber` | t_haos_orgchangereason | ✅（或 CodeRuleOp 自动生成）|
| `name` | `fname` | t_haos_orgchangereason_l | ✅ 必填（INV-CR-01）|
| `simplename` | `fsimplename` | t_haos_orgchangereason_l | ❌ 选填 |
| `description` | `fdescription` | t_haos_orgchangereason_l | ❌ 选填 |
| `index` | `findex` | t_haos_orgchangereason | ❌ 选填（默认 0）|
| `otclassify` | `fotclassify` | t_haos_orgchangereason | ❌ 选填 · 走 haos_otclassify F7 |
| `ctrlstrategy` | n/a（平台多组织行为）| t_haos_orgchangereason | ✅ 默认值由平台决定 |
| `createorg` / `org` / `useorg` / `srccreateorg` | n/a | t_haos_orgchangereason | 多组织字段 · 平台默认 |

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
| `sourcedata` / `bitindex` / `srcindex` | n/a | 平台分配/复制数据时 | 平台 BaseData |

---

## 三、读取流（核心调用点）

### 3.1 ChangeReasonListPlugin（列表层）

```java
// ChangeReasonListPlugin.java:19-22 · setFilter()
public void setFilter(SetFilterEvent setFilterEvent) {
    super.setFilter(setFilterEvent);
    setFilterEvent.getQFilters().add(new QFilter("id", "!=", (Object)1010L));
}
```

→ **不写库** · 仅在列表查询时附加 QFilter（单条硬编码 · 排除 1010L 主键）。

### 3.2 BaseDataBuOp（OP 层）

```java
// BaseDataBuOp.java:19-22
public void onAddValidators(AddValidatorsEventArgs args) {
    super.onAddValidators(args);
    args.addValidator((AbstractValidator)new CtrlStrategyValidator());
}
```

→ **不写库** · 仅在 save 链注册 CtrlStrategyValidator。
→ **不读特殊字段**（不像 haos_changescene 显式声明 orgchangetype/changeoperat）· 走平台默认 onPreparePropertys 行为。

### 3.3 ChangeReasonListPlugin.beforeShowBill（列表层 · 双击行）

```java
public void beforeShowBill(BeforeShowBillFormEvent e) {
    e.getParameter().setCaption(this.getView().getFormShowParameter().getCaption());
}
```

→ **不写库** · 仅设 view caption。

---

## 四、事务边界（实证 OP 链 § 04 阶段 5-9）

| 事务阶段 | 边界事件 | 数据可回滚 |
|---|---|---|
| 进事务前 | `beforeExecuteOperationTransaction` 抛异常 → 不进事务 | n/a（未写库）|
| 事务内 | `beginOperationTransaction` → 开始落库 → `endOperationTransaction` | ✅ 平台自动 rollback |
| 事务提交后 | `afterExecuteOperationTransaction` | ❌ 已 commit · 不可回滚 |

**关键事实（实证 BEC grep）**：
- 反编译产物 `grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" decompiled/scenarios/haos_orgchangereason/ → 0 处命中`
- → **本场景标品不在事务后发 BEC 事件** · 任何"基础资料变更触发下游通知"逻辑都需要 ISV 自建发布方（否则下游收不到 · CS-05 给反指引）
- → 跟配对场景 haos_changescene **完全一致**（也是 BEC grep 0 实证）

---

## 五、删除路径的数据风险（高危）

`opkeys/delete.json` 执行链：
```
1. BaseDataDeletePlugin (bos)
2. CodeRuleDeleteOp (bos · 释放编码池)
3. HRBaseDataStatusOp (hbp)
4. HRBaseDataLogOp (hbp)
```

**4 个插件 · 没有任何反向引用前置校验** · 删除一条 `haos_orgchangereason` 主键时：

```sql
-- 平台行为：直接 DELETE
DELETE FROM t_haos_orgchangereason WHERE fid = ?;
DELETE FROM t_haos_orgchangereason_l WHERE fid = ?;
-- 注意：本场景没有 MulBasedata 子表 · 但 t_haos_cschangereason
-- (haos_changescene 的多选关系子表) 仍持有 fbasedataid=被删id 的引用
```

**孤儿外键风险**：
- `t_haos_cschangereason.fbasedataid = 已删 id` → haos_changescene 的 changereason 多选展示空
- 历史 `homs_orgbatchchgbill` 通过 changescene → changereason 链路间接受影响（数据展示但来源数据已游离）
- `haos_adminorgdetail` 通过 changescene 链路间接受影响

→ **CS-02 解决**：在 onAddValidators 阶段加反向引用校验 · 拒绝删除已被引用的项。
→ 校验路径**比 haos_changescene 简单**（haos_changescene 要查 7 个 entry 字段 · 本场景只查 1 个 t_haos_cschangereason 子表）。

---

## 六、跨场景数据传递（无 BEC · 走 BasedataField 引用）

```
haos_orgchangereason · t_haos_orgchangereason
     │ id (Long)
     │
     └─── 被 t_haos_cschangereason.fbasedataid (haos_changescene 的多选子表) 引用
            │
            └─── 此关系数据 fid 指向 t_haos_changescene.fid
                  │
                  └─── 间接被 t_homs_orgbatchchgbill 7 entry × changescene 字段引用
                  └─── 间接被 t_haos_adminorgdetail.changescene 引用
                  └─── 间接被 t_haos_adminorghis.changescene 引用
```

> 引用数据级联：本表 id → t_haos_cschangereason.fbasedataid → t_haos_cschangereason.fid (=changescene id) → 7 个 *_changescene 字段值
> ISV 实施 CS-02 时**应只查直接路径** t_haos_cschangereason · 不要"穿透" 3 层去查 homs（性能 + 可维护性问题）

---

## 七、读写字段 · 反编译实证矩阵

| 场景类 | 读字段 | 写字段 | 来源行号 |
|---|---|---|---|
| `ChangeReasonListPlugin.setFilter` | n/a（仅 QFilter）| n/a | L19-22 |
| `ChangeReasonListPlugin.beforeShowBill` | view caption | view caption | L24-26 |
| `BaseDataBuOp.onAddValidators` | n/a | n/a（仅注册 Validator）| L19-22 |

> 📌 **观察**：本场景 2 个反编译类**都不直接读写业务字段** · 业务读写完全交给：
> - 标品 8 插件链（save）的默认行为
> - CtrlStrategyValidator（控制策略合规校验）
> - 平台 BasedataField 默认行为
>
> 这是 haos 域里**最薄壳**的实现。

---

## 八、失败回滚策略

| 失败点 | 行为 |
|---|---|
| `onAddValidators` 校验未通过 | 抛异常 · 不进事务 · 用户在表单看到错误提示 |
| `beforeExecuteOperationTransaction` 抛异常 | 不进事务 · 同上 |
| `beginOperationTransaction` SQL 失败 | 平台自动 rollback · 主表 + 多语言表全部回滚 |
| `afterExecuteOperationTransaction` 抛异常 | ⚠ 事务已 commit · 数据已落库 · 仅日志/通知失败 |

> ⚠ 标品**没在 afterExecute 阶段发 BEC** · 所以也不存在"事务已提交但事件未发出"的双不一致风险。
> ISV 若自建 BEC 发布方（如 CS-05 反指引中提到的"挂到 homs_orgbatchchgbill 上"）必须在 PR-010 第 9 步 afterExecuteOperationTransaction 触发 · 才能保证主事务已提交。

---

## 九、数据写入对比表（haos_orgchangereason vs haos_changescene）

| 写入维度 | 本场景 | haos_changescene |
|---|---|---|
| 主表 | t_haos_orgchangereason | t_haos_changescene |
| 多语言表 | t_haos_orgchangereason_l | t_haos_changescene_l |
| MulBasedata 子表 | **无** | t_haos_cschangereason + t_haos_cschangeoperat |
| 场景特有 OP 写库 | 无（BaseDataBuOp 仅注册 Validator）| ChangeSceneSaveOp 在导入路径写 changeoperat 子表 |
| onPreparePropertys 显式声明 | 无（走平台默认）| orgchangetype + changeoperat |
| 事务后 BEC | 无（grep 0）| 无（grep 0）|

→ **本场景写入路径最短** · 落 2 张表（主 + 多语言）· 无子表逻辑 · 无场景特有写入。

---

## 十、查询模式（CS-02 反向引用查询模板）

```java
// 查 haos_orgchangereason id=12345 被多少 changescene 引用
HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_changescene");
// changescene 的 changereason 是 MulBasedataField · 查询写法：
QFilter qf = new QFilter("changereason.fbasedataid", "=", 12345L);
int count = helper.count(qf);
// 或用 isExists 走 limit 1 性能优：
boolean inUse = helper.isExists(qf);
```

→ MulBasedataField 的查询要走 `<fieldKey>.fbasedataid` 路径 · 走 ORM 自动 join · 不需要手动操作 t_haos_cschangereason。

→ 性能：单次 isExists 走 count(1) limit 1 · ~2-3ms · 用户无感知。
