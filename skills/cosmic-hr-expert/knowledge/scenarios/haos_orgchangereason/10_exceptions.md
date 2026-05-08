# 异常诊断 · 行政组织变动原因（haos_orgchangereason）

> **状态**: 🟢 基于反编译 + cosmic_realworld_traps 共性陷阱 + 本场景特有踩坑
> **数据源**: knowledge/cosmic_realworld_traps/* + 反编译 2 类
> **confidence**: verified

---

## 一、平台共性陷阱（cosmic_realworld_traps 抄录相关项）

### T1 · buildMeta / modifyMeta 加字段陷阱

> 数据源：`knowledge/cosmic_realworld_traps/buildmeta_traps.md` + `modifymeta_traps.md`

| 现象 | 原因 | 解决 |
|---|---|---|
| 加字段返回 errorCode=0 但 getFormSchema 查不到 | 字段类型用 `EmployeeField` 等 HR SDK 扩展类型 · OpenAPI 不支持 | 改用 `BasedataField` + refEntity 等价表达 |
| `fieldType` 写小写 `textfield` 静默忽略 | actionType / fieldKey 必须 PascalCase（10 条平台规则 #8）| 写成 `TextField` |
| 加字段后列名变成 `ffk_xxx` 怪样 | 手动写 fieldName=`fk_xxx` · 平台再加 `f` 前缀 | 不传 fieldName · 让平台按 `f + key.lowercase()` 自动生成 |
| 加 BasedataField 引用其他 ISV 字典 · 运行时 F7 拿不到数据 | refEntity 的 form 还没建 / 没 enable | 先建好 refEntity 再加字段 |
| 加 entry 子表后 buildMeta 不传 parentId · 兜底到 bos 基础资料模板 | parentId 默认 = bos 基础资料模板（非 haos_orgchangereason）| 显式指定 ENTITY_PARENT_ID=haos_orgchangereason |

### T2 · addRule 陷阱

> 数据源：`knowledge/cosmic_realworld_traps/addrule_traps.md`
> ⚠ **本场景没有 formRule**（listRules 实抓 = 0）· addRule 操作通常不需要 · 但参考下面共性 · 防止 ISV 加自定义规则时踩坑。

| 现象 | 原因 | 解决 |
|---|---|---|
| addRule preCondition 用 `xx == ''` 报语法错误 | preCondition 不支持空字符串比较 | 改用 `IsEmpty(xx)` 函数 |
| ruleType / actionType 大小写敏感 · 小写静默忽略 | 必须 PascalCase（actionType=Calculate / SetValue / Mandatory 等）| 严格 PascalCase |
| addRule 返回成功但 listRules 查不到 | 参数 itemId 写错（应该是 formId 的技术 ID 21CVNFII7OS1）| 用 getFormSchema 查准确技术 ID |

### T3 · 操作链 / OP 插件陷阱

| 现象 | 原因 | 解决 |
|---|---|---|
| OP 里 getModel().setValue 报空指针 | OP 在后端没 model · 应该用 entity.set | 改用 `args.getDataEntities()[i].set(key, value)`（PR-003）|
| onAddValidators 注册 Validator 没生效 | 在 beforeExecuteOperationTransaction 阶段注册晚了 | 必须在 onAddValidators 阶段（PR-010 第 4 步）|
| beforeExecuteOperationTransaction 抛异常 → 整笔事务回滚 | 平台行为正常 | 用 addErrorMessage 替代抛异常更优雅 |
| OP 读不到 ISV 加的字段值 | onPreparePropertys 没声明该字段 | 显式 `e.getFieldKeys().add("${ISV_FLAG}_xxx")`（参考 09 案例 7）|

---

## 二、本场景特有陷阱（反编译实证）

### S1 · BaseDataBuOp 没有 onPreparePropertys · ISV 加字段必须自建声明

#### 现象
ISV 加了 `${ISV_FLAG}_bizimpact` 字段 · 在自己的 OP Validator 里 `dataEntity.getString("${ISV_FLAG}_bizimpact")` 拿到 null · 即使 UI 上选了值。

#### 原因
反编译实证 `BaseDataBuOp.java:17-23` · 这个场景特有 OP **只重写了 onAddValidators 一个方法** · 没有 onPreparePropertys。
平台默认 onPreparePropertys 行为只加载白名单字段 · ISV 加的字段不在白名单。

跟 haos_changescene 的 ChangeSceneSaveOp 不同（那个有 onPreparePropertys 显式声明 orgchangetype/changeoperat）。

#### 解决
ISV 自建插件必须重写 onPreparePropertys：
```java
@Override
public void onPreparePropertys(PreparePropertysEventArgs e) {
    super.onPreparePropertys(e);
    e.getFieldKeys().add("${ISV_FLAG}_bizimpact");
    // 子表也要声明（如果加了 CS-05 子表）
    // e.getFieldKeys().add("${ISV_FLAG}_bizline_entry");
    // e.getFieldKeys().add("${ISV_FLAG}_bizline_entry.${ISV_FLAG}_biz_code");
}
```

→ **本场景比 haos_changescene 更需要小心** · 因为标品没声明任何业务字段 · ISV 默认拿不到任何 ISV 字段。

### S2 · 联动死循环陷阱（PR-004）

#### 现象
ISV 在 propertyChanged 里写：
```java
if (property.equals("ctrlstrategy") && !valid) {
    setValue("ctrlstrategy", oldValue);  // ❌ 触发新的 propertyChanged
}
```
死循环 · 浏览器卡死。

#### 原因
`setValue` 本身是字段变更事件 · 触发新的 propertyChanged。

#### 解决（PR-004 实证）
```java
this.getModel().beginInit();
this.getModel().setValue("ctrlstrategy", oldValue);
this.getModel().endInit();
this.getView().updateView("ctrlstrategy");
```

→ **本场景特别要小心**（参考 CS-03）· 因为 ctrlstrategy 是 ComboField 标品联动可见组织字段 · 改回旧值不当心很容易死循环。

### S3 · 反向引用查询走"穿透链"性能陷阱

#### 现象
新手开发想做 CS-02 · 写了"穿透 3 层"的查询：
```java
// ❌ 错误 · 性能差
HRBaseServiceHelper changeSceneHelper = new HRBaseServiceHelper("haos_changescene");
DynamicObjectCollection scenes = changeSceneHelper.queryOriginalCollection(
    "id, changereason", new QFilter("changereason.fbasedataid", "=", id));
for (DynamicObject scene : scenes) {
    long sceneId = scene.getLong("id");
    HRBaseServiceHelper billHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
    if (billHelper.isExists(new QFilter("changescene", "=", sceneId))) {
        addErrorMessage(...);
    }
}
```

#### 解决
单查直接路径足够（参考 CS-02）：
```java
// ✅ 正确 · 单查直接路径
HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_changescene");
if (helper.isExists(new QFilter("changereason.fbasedataid", "=", id))) {
    addErrorMessage("已被变动场景的 changereason 字段引用 · 不可删除");
}
```

→ **业务上的考虑**：本场景被引用 = 被 changescene 引用 = 间接被 homs 引用。
  禁删本场景 → 业务自动避免下游游离 · 不需要"穿透 3 层"查 · 单查 changescene 这一层就够。

### S4 · ChangeReasonListPlugin.setFilter 重写后 INV 失效陷阱

#### 现象
ISV 重写 `setFilter@haos_orgchangereason` 后 · 用户列表里突然能看到 id=1010 的"模板项"了 · 点击崩溃。

#### 原因
INV-CR-04（`id != 1010L`）是 ChangeReasonListPlugin.setFilter 硬编码 · ISV 重写后丢了。

#### 解决
- ❌ 不要重写 `setFilter@haos_orgchangereason`
- ✅ 走 customParam 传值 · 让标品 ChangeReasonListPlugin 继续跑（标品逻辑保留）
- ✅ 自建过滤通过自己的 propertyChanged → setCustomParam → refresh

跟 haos_changescene 的 INV-CS-04 同模式问题。

### S5 · onPreparePropertys 没声明 ISV 字段 · 后端拿到 null（重复强调）

#### 现象
ISV Validator 里 `dataEntity.getString("${ISV_FLAG}_bizimpact")` 始终是 null · 即使 UI 上明明选了值。

#### 原因
苍穹 OP 层为了性能默认只加载"白名单字段" · ISV 加的字段必须在 onPreparePropertys 显式声明（PR-010 第 2 步）。

#### 解决
参考 S1 解决方案 · 跟 09 案例 7 同源。

> ⚠ **本场景比 haos_changescene 更容易踩这个坑** · 因为标品 BaseDataBuOp 没有任何 onPreparePropertys 实现 · ISV 没参考样板。

### S6 · 多语言表查不到字段陷阱

#### 现象
ISV 直接 `SELECT fname FROM t_haos_orgchangereason WHERE fid=?` · 报错列不存在。

#### 原因
`fname` / `fsimplename` / `fdescription` / `foriname` 在多语言表 `t_haos_orgchangereason_l` · 不在主表。

#### 解决
- ✅ 用 DynamicObject 走 ORM 层：`dataEntity.getString("name")` 自动走 join
- ❌ 不要写裸 SQL（除非确认表结构跨语言情况）

参考 03 §3.1 字段分组实证。

### S7 · 出厂数据隐藏字段（1010）跨环境变化陷阱

#### 现象
ISV 在测试环境写好 CS-02 校验逻辑里硬编码 `id != 1010L` · 上线生产后某客户报"删不了任何变动原因" · 排查发现 1010 在该环境是另一个含义。

#### 原因
跨环境出厂数据主键可能不一致（虽然多数环境 1010 都是平台保留 · 但某些定制版本可能改）。
参考 `feedback_har_values_not_authoritative.md`：跨环境主键不可硬假设。

#### 解决
- 部署前用 OpenAPI 在生产环境查 1010L 主键的 number/oriname · 确认含义
- 或代码里走 `issyspreset=true && oriname.contains("内部")` 等逻辑识别 · 而不是硬编码 id

### S8 · 双字典维护不同步陷阱

#### 现象
HR 加了一条新 changereason "应急临时调整" · 但忘了去 haos_changescene 把对应场景"应急临时调整"的 changereason 多选追加该项 · 业务方反馈"系统不全"。

#### 原因
双字典维护要协同 · 但平台没有自动联动机制。

#### 解决
- 业务 SOP：加 reason 后立即检查关联 changescene 是否需要追加
- ISV 可选 CS：写一个调度任务定期对比 · 提醒 HR

跟 09 案例 3 同源 · 业务级问题不是技术级。

---

## 三、错误码诊断表

| 错误码 / 异常 | 意义 | 排查方向 |
|---|---|---|
| `KDBizException: 名称 不能为空` | INV-CR-01 触发 | 用户没填 name · 平台 BasedataField 必填校验 |
| `KDBizException: 变动原因[xxx]已被变动场景的 changereason 字段引用·不可删除` | CS-02 拦截 | 业务人员尝试删被引用的字典数据 · 提示用 disable 替代 |
| `KDBizException: 系统预置变动原因不可删除 (issyspreset=true · INV-CR-03)` | CS-02 拦截 出厂数据 | 出厂数据 issyspreset=true · 任何尝试删都拒 |
| `KDBizException: '同分配范围'(ctrlstrategy=8) 仅允许在指定 BU 使用` | CS-03 自定义校验 | ISV 业务规则 · 切换 ctrlstrategy 必须 org 在白名单 |
| `IllegalArgumentException: 类型不匹配` | OP 写值类型错 | 检查字段类型（ComboField 存 String · BasedataField 存 Object[]）|
| `NullPointerException` 在 ISV Validator 里 | S1/S5 陷阱 | onPreparePropertys 没声明 ISV 字段 |
| `errorCode=0 但 getFormSchema 查不到字段` | T1 陷阱 | 字段类型用了不支持的 EmployeeField 等 |
| `OperationServiceException: 当前数据状态不允许此操作` | HRBaseDataStatusOp 拦截 | status=B/C 时不能改 · 先 unsubmit/unaudit |
| `enable=0 的数据不在 F7 显示` | 平台 BaseDataField 默认行为 | 业务方反映"找不到字典"时检查是否被禁用过 |
| `1010L 数据看不到` | INV-CR-04 列表层硬编码隐藏 | F7 路径仍能选到 · 列表入口看不到 · 业务上正常 |

---

## 四、调试技巧

### 4.1 查标品执行链

```python
# 用 OpenAPI 查 save opKey 的执行链
client.list_operations(form_number="haos_orgchangereason", op_key="save")
# 返回 8 个标品插件 · 跟 opkeys/save.json 一致
```

### 4.2 grep 反编译产物（铁律 5）

```bash
# 查 BEC 是否启用
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_orgchangereason/
# → 0 处命中（实证）

# 查具体读写字段
grep -rE 'set\(|get(String|Long|Boolean|Date|DynamicObject)' \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_orgchangereason/

# 查 super 调用关系
grep -rE 'super\.' knowledge/_sdk_audit/_decompiled/scenarios/haos_orgchangereason/
```

### 4.3 跨环境主键验证

```python
# 验证 1010L 主键在目标环境的含义
result = client.query_basedata(
    form_number="haos_orgchangereason",
    select="id, number, name, issyspreset, orinumber",
    filter=[["id", "=", 1010]]
)
print(result['data'][0])  # 看 issyspreset / orinumber 确认是平台保留
```

### 4.4 反向引用查询模板

```java
// 查 haos_orgchangereason id=12345 被多少 changescene 引用
HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_changescene");
QFilter qf = new QFilter("changereason.fbasedataid", "=", 12345L);
int count = helper.count(qf);
LOG.info("haos_changescene 引用数: " + count);

// 列出具体哪些 changescene
DynamicObjectCollection scenes = helper.queryOriginalCollection(
    "id, name, number", qf);
for (DynamicObject scene : scenes) {
    LOG.info("引用方 changescene: id=" + scene.getLong("id")
           + " name=" + scene.getString("name"));
}
```

---

## 五、性能调优要点

| 场景 | 优化点 |
|---|---|
| CS-02 反向引用查询 | 用 `HRBaseServiceHelper.isExists` 走 limit 1 · 不用 BusinessDataServiceHelper.load |
| CS-03 联动 | 在 propertyChanged 里只调一次校验逻辑 · 用 e.getChangeSet() 拿 oldValue 做回滚 |
| 列表查询 | 不要重写 setFilter（保留标品的 QFilter 索引命中）|
| ISV Validator 批量校验 | 用 `IN` 一次查所有 id 的反向引用 · 不要循环 N 次 isExists（数据量大时切换）|
| MulBasedata 反向查询 | 走 ORM 路径 `<fieldKey>.fbasedataid` · 不要手动 join 子表 |

---

## 六、版本兼容性提醒

- 标品 CtrlStrategyValidator 跨 release 可能扩展校验规则 · ISV Validator 不要假设 super 链路特定行为
- INV-CR-04 的硬编码主键（1010）跨版本可能调整 · 部署前查 OpenAPI 确认含义
- 字段类型 `MulBasedataField` 和 `BasedataField` 在 modifyMeta 加字段时区别明显 · 别混
- BaseDataBuOp 是薄壳类 · 标品升级可能加新方法（如 onPreparePropertys）· ISV 不应继承

---

## 七、跟 haos_changescene 异常表对比

| 异常类型 | 本场景 | haos_changescene |
|---|---|---|
| onPreparePropertys 缺失陷阱（S1/S5）| ⚠⚠ 本场景**更易踩**（无标品参考样板）| ⚠ 标品有声明 orgchangetype/changeoperat |
| 联动死循环（S2）| ⚠ ctrlstrategy 联动 | ⚠⚠ orgchangetype + changeoperat 双联动 |
| 反向引用查询性能（S3）| ⚠ 单查 1 个字段 | ⚠⚠ 7 个 entry × 字段循环查 |
| setFilter 重写陷阱（S4）| INV-CR-04（id != 1010）| INV-CS-04 + CS-05（id != 1070 + 1100_S）|
| 跨环境主键（S7）| 1010L | 1010 / 1070 / 1100_S 三个 |
| 双字典维护（S8）| 配对 changescene | 配对 changereason / changeoperat |

→ **本场景陷阱面更窄** · 但 onPreparePropertys 的"无样板"陷阱（S1/S5）反而更容易让新手踩。
