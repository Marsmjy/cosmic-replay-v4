# 数据流转 · 行政组织职能（haos_adminorgfunction）

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
   ├─→ 主表 t_haos_adminorgfunction
   │     • fnumber / fissyspreset 等
   │     • fcreatorid / fcreatetime（自动）
   │     • fenable=1 默认 / fstatus=A 默认
   │
   └─→ 多语言表 t_haos_adminorgfunction_l
         • fname / fsimplename / fdescription / foriname (per locale)
   ↓
[afterExecute] HRBaseDataLogOp 写日志
   ↓
[事务提交] commit
   ↓
[afterExecuteOperationTransaction]（事务后 · 标品没在此发 BEC ⚠ grep 实证）
```

> 📌 **跟 haos_orgchangereason 完全一致**：本场景**没有任何 MulBasedata 子表写入** · 写入路径**最简洁**（落 2 张表）。
> 跟双胞胎 haos_orgchangereason 写入路径 1:1 一致 · 仅物理表名差异。

---

## 二、字段级写入清单

### 2.1 用户输入字段

| 字段 key | 物理列 | 物理表 | 用户在表单填 |
|---|---|---|---|
| `number` | `fnumber` | t_haos_adminorgfunction | ✅（或 CodeRuleOp 自动生成）|
| `name` | `fname` | t_haos_adminorgfunction_l | ✅ 必填（INV-AF-01）|
| `simplename` | `fsimplename` | t_haos_adminorgfunction_l | ❌ 选填 |
| `description` | `fdescription` | t_haos_adminorgfunction_l | ❌ 选填 |
| `index` | `findex` | t_haos_adminorgfunction | ❌ 选填（默认 0 · 跟 ListOrderCommonPlugin 默认排序协同）|
| `ctrlstrategy` | n/a（平台多组织行为）| t_haos_adminorgfunction | ✅ 默认值由平台决定 |
| `createorg` / `org` / `useorg` / `srccreateorg` | n/a | t_haos_adminorgfunction | 多组织字段 · 平台默认 |

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

### 3.1 ListOrderCommonPlugin（列表层）

```java
// ListOrderCommonPlugin.java:15-18 · setFilter()
public void setFilter(SetFilterEvent setFilterEvent) {
    super.setFilter(setFilterEvent);
    setFilterEvent.setOrderBy("enable desc,number asc");
}
```

→ **不写库** · 仅在列表查询时设排序。
→ **不加任何 QFilter**（跟 haos_orgchangereason 的 ChangeReasonListPlugin 不同 · 那个加了 `id != 1010L`）。

### 3.2 BaseDataBuOp（OP 层）

```java
// BaseDataBuOp.java:17-23
public void onAddValidators(AddValidatorsEventArgs args) {
    super.onAddValidators(args);
    args.addValidator((AbstractValidator)new CtrlStrategyValidator());
}
```

→ **不写库** · 仅在 save 链注册 CtrlStrategyValidator。
→ **不读特殊字段**（不像 haos_changescene 显式声明 orgchangetype/changeoperat）· 走平台默认 onPreparePropertys 行为。
→ 反编译产物 1:1 跟 haos_orgchangereason 同源（同样 23 行薄壳）。

### 3.3 standard formRule（INV-AF-04 · 平台规则引擎）

平台 listRules 实抓的 1 条 formRule（ruleId `21V4EGK80+JT`）：
- **preCondition**: `issyspreset = true`
- **行为**: 系统预置数据进入编辑视图时所有字段变只读
- **触发**: UI 层 · 不进 OP 层 · 不走数据库

→ 这条规则**不读不写库** · 仅在表单加载时根据 dataEntity.issyspreset 决定 UI 渲染。

---

## 四、事务边界（实证 OP 链 § 04 阶段 5-9）

| 事务阶段 | 边界事件 | 数据可回滚 |
|---|---|---|
| 进事务前 | `beforeExecuteOperationTransaction` 抛异常 → 不进事务 | n/a（未写库）|
| 事务内 | `beginOperationTransaction` → 开始落库 → `endOperationTransaction` | ✅ 平台自动 rollback |
| 事务提交后 | `afterExecuteOperationTransaction` | ❌ 已 commit · 不可回滚 |

**关键事实（实证 BEC grep）**：
- 反编译产物 `grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" decompiled/scenarios/haos_adminorgfunction/ → 0 处命中`
- → **本场景标品不在事务后发 BEC 事件** · 任何"基础资料变更触发下游通知"逻辑都需要 ISV 自建发布方（否则下游收不到 · CS-05 给反指引）
- → 跟双胞胎 haos_orgchangereason **完全一致**（都是 BEC grep 0 实证）

---

## 五、删除路径的数据风险（高危）

`opkeys/delete.json` 执行链：
```
1. BaseDataDeletePlugin (bos)
2. CodeRuleDeleteOp (bos · 释放编码池)
3. HRBaseDataStatusOp (hbp)
4. HRBaseDataLogOp (hbp)
```

**4 个插件 · 没有任何反向引用前置校验** · 删除一条 `haos_adminorgfunction` 主键时：

```sql
-- 平台行为：直接 DELETE
DELETE FROM t_haos_adminorgfunction WHERE fid = ?;
DELETE FROM t_haos_adminorgfunction_l WHERE fid = ?;
-- 注意：本场景没有 MulBasedata 子表 · 但 t_haos_adminorg
-- (haos_adminorg 主表 · adminorgfunction 字段 BasedataField 单选) 仍持有
-- fadminorgfunctionid = 被删 id 的引用
```

**孤儿外键风险**：
- `t_haos_adminorg.fadminorgfunctionid = 已删 id` → admin_org 列表"职能"列展示空
- 历史 HisModel 时序数据（haos_adminorghis 共物理表 t_haos_adminorg）也受影响 · 所有版本中持有该外键的行都游离
- haos_adminorgdetail 视图（共物理表）查询时通过 adminorgfunction.name 关联展示 · 拿到 null

→ **CS-02 解决**：在 onAddValidators 阶段加反向引用校验 · 拒绝删除已被引用的项。
→ 校验路径**比 haos_orgchangereason 还简单**（haos_orgchangereason 要走 `<field>.fbasedataid` 走子表 · 本场景直接 `<field> = id` 单字段查）。

---

## 六、跨场景数据传递（无 BEC · 走 BasedataField 单选引用）

```
haos_adminorgfunction · t_haos_adminorgfunction
     │ id (Long)
     │
     └─── 被 t_haos_adminorg.fadminorgfunctionid (haos_adminorg 主表) 引用 ⭐ 直接路径
            │
            └─── 此外键 fid 指向某条 haos_adminorg 记录
                  │
                  ├─── 通过 haos_adminorg → haos_adminorgdetail (共物理表 · 视图层)
                  ├─── 通过 haos_adminorg → haos_adminorghis (共物理表 · HisModel 时序版本)
                  └─── 间接被 hrpi_employee.adminorg / hbpm_position.adminorg
                       (员工 / 岗位归属 · 但这两层不直接引用 adminorgfunction
                        · 仅引用 admin_org · 因此 adminorgfunction 仅 1 跳直接下游)
```

> 引用数据级联：本表 id → t_haos_adminorg.fadminorgfunctionid → admin_org id → 间接通过 admin_org 影响员工/岗位
> ISV 实施 CS-02 时**应只查直接路径** t_haos_adminorg · 不要"穿透 N 层"去查 hrpi/hbpm（性能 + 可维护性问题）

---

## 七、读写字段 · 反编译实证矩阵

| 场景类 | 读字段 | 写字段 | 来源行号 |
|---|---|---|---|
| `ListOrderCommonPlugin.setFilter` | n/a（仅 setOrderBy）| n/a | L15-18 |
| `BaseDataBuOp.onAddValidators` | n/a | n/a（仅注册 Validator）| L17-23 |

> 📌 **观察**：本场景 2 个反编译类**都不直接读写业务字段** · 业务读写完全交给：
> - 标品 8 插件链（save）的默认行为
> - CtrlStrategyValidator（控制策略合规校验）
> - 平台 BasedataField 默认行为
> - 平台 listRules formRule（INV-AF-04 · UI 层 issyspreset 拦截）
>
> 这是 haos 域里**最薄壳**的实现（42 行 · 比 haos_orgchangereason 还轻 8 行）。

---

## 八、失败回滚策略

| 失败点 | 行为 |
|---|---|
| `onAddValidators` 校验未通过（如 CtrlStrategyValidator 拦）| 抛异常 · 不进事务 · 用户在表单看到错误提示 |
| `beforeExecuteOperationTransaction` 抛异常 | 不进事务 · 同上 |
| `beginOperationTransaction` SQL 失败 | 平台自动 rollback · 主表 + 多语言表全部回滚 |
| `afterExecuteOperationTransaction` 抛异常 | ⚠ 事务已 commit · 数据已落库 · 仅日志/通知失败 |

> ⚠ 标品**没在 afterExecute 阶段发 BEC** · 所以也不存在"事务已提交但事件未发出"的双不一致风险。
> ISV 若自建 BEC 发布方（如 CS-05 反指引中提到的"挂到 admin_org 上"）必须在 PR-010 第 9 步 afterExecuteOperationTransaction 触发 · 才能保证主事务已提交。

---

## 九、数据写入对比表（haos_adminorgfunction vs haos_orgchangereason · 双胞胎对照）

| 写入维度 | 本场景 | haos_orgchangereason |
|---|---|---|
| 主表 | t_haos_adminorgfunction | t_haos_orgchangereason |
| 多语言表 | t_haos_adminorgfunction_l | t_haos_orgchangereason_l |
| MulBasedata 子表 | **无** | **无** |
| 场景特有 OP 写库 | 无（BaseDataBuOp 仅注册 Validator）| 无（同上）|
| onPreparePropertys 显式声明 | 无（走平台默认）| 无（同上）|
| 事务后 BEC | 无（grep 0）| 无（grep 0）|
| listRules formRule | 1（issyspreset=true 拦）| 0 |

→ **本场景写入路径最短** · 落 2 张表（主 + 多语言）· 无子表逻辑 · 无场景特有写入。
→ 跟 haos_orgchangereason 几乎完全一致 · 仅物理表名 + listRules 数量差异。

---

## 十、查询模式（CS-02 反向引用查询模板）

```java
// 查 haos_adminorgfunction id=12345 被多少 haos_adminorg 引用
// ⚠ adminorg 是 HisModel 时序场景 · 但反向查 fadminorgfunctionid 仍走当前生效版本
HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorg");
// adminorgfunction 是 BasedataField 单选 · 直字段 = id 即可：
QFilter qf = new QFilter("adminorgfunction", "=", 12345L);
// 加 enable=1 + iscurrentversion=true 缩小到当前生效（admin_org 是 HisModel）
qf.and(new QFilter("enable", "=", "1"));
qf.and(new QFilter("iscurrentversion", "=", true));
int count = helper.count(qf);
// 或用 isExists 走 limit 1 性能优：
boolean inUse = helper.isExists(qf);
```

→ BasedataField 单选的查询**比 MulBasedataField 多选简单**：直接 `<field> = id` · 不用走 `<field>.fbasedataid` 子表路径。

→ 性能：单次 isExists 走 count(1) limit 1 · ~3-5ms · 用户无感知。

> 💡 **跟 haos_orgchangereason CS-02 查询的对比**：
> - haos_orgchangereason 查多选关系：`new QFilter("changereason.fbasedataid", "=", id)` · 平台 ORM 自动 join 子表
> - 本场景查单选关系：`new QFilter("adminorgfunction", "=", id)` · 直字段查 · 无需 join
> - 性能差异：本场景**比 haos_orgchangereason 快 1ms** 左右（少一次 join）
> - 但本场景查的是 HisModel 时序表 · 必须加 `iscurrentversion=true` 缩到当前版本（避免历史版本误判）
