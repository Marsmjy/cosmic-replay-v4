# 异常诊断 · 行政组织职能（haos_adminorgfunction）

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
| 加 entry 子表后 buildMeta 不传 parentId · 兜底到 bos 基础资料模板 | parentId 默认 = bos 基础资料模板（非 haos_adminorgfunction）| 显式指定 ENTITY_PARENT_ID=haos_adminorgfunction |

### T2 · addRule 陷阱

> 数据源：`knowledge/cosmic_realworld_traps/addrule_traps.md`
> ⚠ **本场景有 1 条 listRules formRule**（INV-AF-04 · issyspreset=true 保护）· ISV 加自定义规则时注意不要跟标品 listRules 冲突。

| 现象 | 原因 | 解决 |
|---|---|---|
| addRule preCondition 用 `xx == ''` 报语法错误 | preCondition 不支持空字符串比较 | 改用 `IsEmpty(xx)` 函数 |
| ruleType / actionType 大小写敏感 · 小写静默忽略 | 必须 PascalCase（actionType=Calculate / SetValue / Mandatory 等）| 严格 PascalCase |
| addRule 返回成功但 listRules 查不到 | 参数 itemId 写错（应该是 formId 的技术 ID）| 用 getFormSchema 查准确技术 ID |
| ISV 加的 formRule 与标品 issyspreset 保护规则冲突 | 双 rule 叠加 · 表现不一致 | ISV 规则条件里先排除 issyspreset=true 的行 |

### T3 · 操作链 / OP 插件陷阱

| 现象 | 原因 | 解决 |
|---|---|---|
| OP 里 getModel().setValue 报空指针 | OP 在后端没 model · 应该用 entity.set | 改用 `args.getDataEntities()[i].set(key, value)`（PR-003）|
| onAddValidators 注册 Validator 没生效 | 在 beforeExecuteOperationTransaction 阶段注册晚了 | 必须在 onAddValidators 阶段（PR-010 第 4 步）|
| beforeExecuteOperationTransaction 抛异常 → 整笔事务回滚 | 平台行为正常 | 用 addErrorMessage 替代抛异常更优雅 |
| OP 读不到 ISV 加的字段值 | onPreparePropertys 没声明该字段 | 显式 `e.getFieldKeys().add("${ISV_FLAG}_xxx")`（参考 09 案例 6）|

---

## 二、本场景特有陷阱（反编译实证）

### S1 · BaseDataBuOp 没有 onPreparePropertys · ISV 加字段必须自建声明

#### 现象
ISV 加了 `${ISV_FLAG}_funccategory` 字段 · 在自己的 OP Validator 里 `dataEntity.getString("${ISV_FLAG}_funccategory")` 拿到 null · 即使 UI 上选了值。

#### 原因
反编译实证 `BaseDataBuOp.java:1-23` · 这个场景特有 OP **只重写了 onAddValidators 一个方法** · 没有 onPreparePropertys。
平台默认 onPreparePropertys 行为只加载白名单字段 · ISV 加的字段不在白名单。

跟 haos_orgchangereason 完全一致 · 两个场景的 BaseDataBuOp 都是薄壳 · 都没有 onPreparePropertys 样板。

#### 解决
ISV 自建插件必须重写 onPreparePropertys：
```java
// 源码参考（ISV 自建类 · 非标品反编译）
@Override
public void onPreparePropertys(PreparePropertysEventArgs e) {
    super.onPreparePropertys(e);
    e.getFieldKeys().add("${ISV_FLAG}_funccategory");
    // 子表也要声明（如果加了 CS-05 子表）
    // e.getFieldKeys().add("${ISV_FLAG}_func_entry");
    // e.getFieldKeys().add("${ISV_FLAG}_func_entry.${ISV_FLAG}_biz_code");
}
```

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
// 源码参考（ISV 自建类 · 非标品反编译）
this.getModel().beginInit();
this.getModel().setValue("ctrlstrategy", oldValue);
this.getModel().endInit();
this.getView().updateView("ctrlstrategy");
```

→ **本场景跟 haos_orgchangereason 完全相同** · ctrlstrategy 回滚时必须 beginInit/endInit 包裹。

### S3 · issyspreset 预置数据保护双陷阱（本场景特有 · haos_orgchangereason 无）

#### 现象 A · listRules 拦修改 · ISV 却又加了另一层表单插件拦截 · 双重错误消息
某 ISV 开发不知道标品已有 1 条 listRules formRule 保护预置数据 · 又写了一个 beforeDoOperation 表单插件拦 issyspreset=true 的操作 · 结果用户看到 2 条错误消息。

#### 原因
标品 INV-AF-04（1 条 listRules formRule）已经处理了 UI 层只读 · ISV 的 beforeDoOperation 是多余的。

#### 解决
- ISV 只在 OP 层的 onAddValidators 里做兜底（CS-02）
- 不需要在 FormPlugin 的 beforeDoOperation 再拦 issyspreset 操作
- 保持单一拦截层 · 避免重复消息

#### 现象 B · 误以为可以通过 OP 修改 issyspreset=true 的数据
某 ISV 开发认为"OP 层没有 listRules · 能绕过平台限制修改预置数据" · 在 beginOperationTransaction 里直接 `dataEntity.set("number", "newNumber")`。

#### 原因
- listRules 保护的是 UI 层只读 · 不是 OP 层写入拦截
- OP 层直接 entity.set() 在技术上可以写入 · 但标品升级时预置数据会被覆盖（INV-AF-03）
- 跨环境主键不一致 · 修改 issyspreset 数据有巨大业务风险

#### 解决
- 永远不要在 OP 插件里修改 issyspreset=true 数据的任何标品字段
- 如有自定义字段（CS-01 加的）· 可以安全 set · 不影响平台升级

### S4 · 反向引用查询没加 iscurrentversion · HisModel 特性陷阱（本场景特有）

#### 现象
ISV 参考 haos_orgchangereason 的 CS-02 实现 · 直接复制反向引用查询逻辑 · 没加 `iscurrentversion=true` 条件：
```java
// ❌ 错误：未加 iscurrentversion（从 haos_orgchangereason 复制未改）
HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorg");
if (helper.isExists(new QFilter("adminorgfunction", "=", id))) {
    addErrorMessage("已被行政组织引用 · 不可删除");
}
```

某条职能的历史版本 admin_org 有引用 · 当前版本已换职能 · 但上面的查询仍然命中 · 拦下了本来合法的删除操作。

#### 原因
`haos_adminorg` 是 HisModel 时序场景 · 每次保存会生成新版本行 · 历史版本行与当前版本行共存于 `t_haos_adminorg`。
未加 `iscurrentversion=true` 时 · 历史版本行中 `fadminorgfunctionid=12345` 的引用会被误计。

#### 解决
```java
// ✅ 正确：加 iscurrentversion=true（源码 AdminOrgFunctionDeleteValidator.java:154-185）
QFilter qf = new QFilter("adminorgfunction", "=", id)
    .and(new QFilter("iscurrentversion", "=", Boolean.TRUE))
    .and(new QFilter("enable", "=", "1"));
if (adminOrgHelper.isExists(qf)) {
    addErrorMessage(ext, String.format(
        "行政组织职能 [%s] 已被行政组织当前生效版本引用 · 不可删除 · 建议改用禁用",
        dataEntity.getString("name")));
}
```

> ⚠ **本场景比 haos_orgchangereason 多一个 HisModel 陷阱** · 移植代码时必须加这个条件。

### S5 · 错用 MulBasedata 子表路径查询（单选 vs 多选反向查询混淆）

#### 现象
ISV 参考 haos_orgchangereason 的子表路径查询 · 写成：
```java
// ❌ 错误：用 .fbasedataid 路径（MulBasedataField 多选的查法）
HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorg");
if (helper.isExists(new QFilter("adminorgfunction.fbasedataid", "=", id))) {
    // ...
}
```
查询始终不命中 · 即使明显有组织用了该职能。

#### 原因
- haos_orgchangereason 被 MulBasedataField 多选引用 → 通过关系子表 `t_haos_cschangereason.fbasedataid` 存储 → 查询路径是 `changereason.fbasedataid`
- 本场景 adminorgfunction 是 **BasedataField 单选** → 直接外键列 `fadminorgfunctionid` → 查询路径是直接字段 `adminorgfunction`（不加 `.fbasedataid`）
- 平台 ORM 对两种关系的查询语法完全不同

#### 解决
```java
// ✅ 正确：单选直字段查（源码 AdminOrgFunctionDeleteValidator.java:172-176）
QFilter qf = new QFilter("adminorgfunction", "=", id)  // 直字段 · 不加 .fbasedataid
    .and(new QFilter("iscurrentversion", "=", Boolean.TRUE));
```

→ **规律总结**：
- MulBasedataField 多选 → 有关系子表 → 路径加 `.fbasedataid`
- BasedataField 单选 → 直接外键 → 路径直接用字段名

### S6 · 重写 setFilter 丢失排序效果陷阱

#### 现象
ISV 想在列表加自定义过滤条件 · 继承或重写 ListOrderCommonPlugin 的 setFilter：
```java
// ❌ 错误：重写 setFilter 丢失标品排序
@Override
public void setFilter(SetFilterEvent e) {
    super.setFilter(e);  // 但父类是 HRDataBaseList · 不是 ListOrderCommonPlugin
    e.addCustomFilter(new QFilter("${ISV_FLAG}_funccategory", "=", "core"));
}
```
列表展示后职能数据排序变乱序（未按 enable desc, number asc）。

#### 原因
- 标品 `ListOrderCommonPlugin.setFilter` 调用 `setOrderBy("enable desc, number asc")` 设定排序
- ISV 写了并列挂但 RowKey > 标品 · 或者 ISV 完全重写了 setFilter · 导致排序逻辑丢失
- 注意：本场景与 haos_orgchangereason 的区别——haos_orgchangereason 的 setFilter 做的是 id 过滤 · 本场景做的是 setOrderBy · **两者都不推荐重写 · 但理由不同**

#### 解决
- ❌ 不重写 setFilter（不论是继承还是并列挂覆盖）
- ✅ 通过 customParam 透传自定义过滤参数 · 在 beforeBindData 阶段处理（不影响 setOrderBy）
- ✅ 自建列表过滤控件 · 不通过 setFilter 注入

### S7 · 多语言表查不到字段陷阱

#### 现象
ISV 直接 `SELECT fname FROM t_haos_adminorgfunction WHERE fid=?` · 报错列不存在。

#### 原因
`fname` / `fsimplename` / `fdescription` 在多语言表 `t_haos_adminorgfunction_l` · 不在主表 `t_haos_adminorgfunction`。

#### 解决
- ✅ 用 DynamicObject 走 ORM 层：`dataEntity.getString("name")` 自动走 join
- ❌ 不要写裸 SQL（除非确认表结构跨语言情况）

参考 03 §3.1 字段分组实证（物理表：`t_haos_adminorgfunction` + `t_haos_adminorgfunction_l`）。

### S8 · 出厂职能跨环境 issyspreset 值不一致

#### 现象
ISV 在测试环境查到某条出厂职能的 id=1010 · 在 CS-02 的逻辑里硬编码 `id != 1010L` · 上线生产后某客户报"删不了任何职能" · 排查发现 1010 在该环境是另一个含义。

#### 原因
跨环境出厂数据主键可能不一致 · 参考 `feedback_har_values_not_authoritative.md`：跨环境主键不可硬假设。

#### 解决
- 部署前用 OpenAPI 在生产环境查 issyspreset=true 的记录 · 确认哪些是系统预置
- CS-02 里用 `dataEntity.getBoolean("issyspreset")` 判断 · 而不是硬编码 id
- 或查询时走 `issyspreset = true` 条件过滤 · 不依赖 id

---

## 三、错误码诊断表

| 错误码 / 异常 | 意义 | 排查方向 |
|---|---|---|
| `KDBizException: 名称 不能为空` | INV-AF-01 触发 | 用户没填 name · 平台 BasedataField 必填校验 |
| `KDBizException: 行政组织职能[xxx]已被行政组织当前生效版本引用·不可删除·建议改用禁用` | CS-02 拦截 | 业务人员尝试删被引用的字典数据 · 提示用 disable 替代 |
| `KDBizException: 系统预置职能不可删除 (issyspreset=true · INV-AF-03/04)` | CS-02 拦截出厂数据 | 出厂数据 issyspreset=true · 任何尝试删都拒 |
| `KDBizException: '同分配范围'(ctrlstrategy=8) 仅允许在指定 BU 使用` | CS-03 自定义校验 | ISV 业务规则 · 切换 ctrlstrategy 必须 org 在白名单 |
| `IllegalArgumentException: 类型不匹配` | OP 写值类型错 | 检查字段类型（ComboField 存 String · BasedataField 存 Object）|
| `NullPointerException` 在 ISV Validator 里 | S1 陷阱 | onPreparePropertys 没声明 ISV 字段 |
| `errorCode=0 但 getFormSchema 查不到字段` | T1 陷阱 | 字段类型用了不支持的 EmployeeField 等 |
| `OperationServiceException: 当前数据状态不允许此操作` | HRBaseDataStatusOp 拦截 | status=B/C 时不能改 · 先 unsubmit/unaudit |
| `enable=0 的数据不在 F7 显示` | 平台 BaseDataField 默认行为 | 业务方反映"找不到职能"时检查是否被禁用过 |
| CS-02 拦截了本来合法的删除 | S4 陷阱：未加 iscurrentversion | 历史版本行干扰 · 在 QFilter 加 iscurrentversion=true |
| 反向查询始终不命中 | S5 陷阱：用了 .fbasedataid 路径 | 单选字段直接查 `adminorgfunction = id` |
| 列表乱序 | S6 陷阱：重写 setFilter 丢排序 | 不重写 setFilter · 用 customParam 代替 |

---

## 四、调试技巧

### 4.1 查标品执行链

```python
# 用 OpenAPI 查 save opKey 的执行链
client.list_operations(form_number="haos_adminorgfunction", op_key="save")
# 返回 8 个标品插件 · 跟 rules_chain_all.json save opKey 一致
```

### 4.2 grep 反编译产物（铁律 5）

```bash
# 查 BEC 是否启用（实证：0 处命中 · 本场景不发 BEC）
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_adminorgfunction/

# 查具体读写字段
grep -rE 'set\(|get(String|Long|Boolean|Date|DynamicObject)' \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_adminorgfunction/

# 查 ListOrderCommonPlugin 的 setOrderBy（区别于 haos_orgchangereason 的 setFilter）
grep -rE 'setOrderBy|setFilter' \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_adminorgfunction/ListOrderCommonPlugin.java
```

### 4.3 跨环境 issyspreset 验证

```python
# 验证生产环境的出厂职能清单
result = client.query_basedata(
    form_number="haos_adminorgfunction",
    select="id, number, name, issyspreset, orinumber",
    filter=[["issyspreset", "=", True]]
)
print(result['data'])  # 确认哪些是预置 · 不要在 CS-02 里硬编码 id
```

### 4.4 反向引用查询模板（带 iscurrentversion）

```java
// 查 haos_adminorgfunction id=12345 被多少 haos_adminorg 当前版本引用
HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorg");
QFilter qf = new QFilter("adminorgfunction", "=", 12345L)
    .and(new QFilter("iscurrentversion", "=", Boolean.TRUE));
int count = helper.count(qf);
LOG.info("haos_adminorg 当前版本引用数: " + count);

// 列出具体哪些 admin_org 引用了该职能
DynamicObjectCollection orgs = helper.queryOriginalCollection(
    "id, name, number", qf);
for (DynamicObject org : orgs) {
    LOG.info("引用方 admin_org: id=" + org.getLong("id")
           + " name=" + org.getString("name"));
}
```

### 4.5 验证 listRules（本场景特有的 1 条 formRule）

```python
# 验证 haos_adminorgfunction 的 listRules（应有 1 条 formRule · issyspreset=true 保护）
result = client.list_rules(
    form_number="haos_adminorgfunction",
    rule_type="formRule"
)
print(result['data'])  # 应返回 1 条 issyspreset 保护规则
# 与 haos_orgchangereason 对比：haos_orgchangereason listRules 应返回 0 条
```

---

## 五、性能调优要点

| 场景 | 优化点 |
|---|---|
| CS-02 反向引用查询 | 用 `HRBaseServiceHelper.isExists` 走 limit 1 · 不用 BusinessDataServiceHelper.load |
| CS-02 HisModel 查询 | iscurrentversion 字段有索引 · 加该条件性能好 · 不影响 ~3-5ms 目标 |
| CS-03 联动 | 在 propertyChanged 里只调一次校验逻辑 · 用 e.getChangeSet() 拿 oldValue 做回滚 |
| 列表查询 | 不要重写 setFilter（保留标品的 setOrderBy 索引命中）|
| ISV Validator 批量校验 | 用 `IN` 一次查所有 id 的反向引用 · 不要循环 N 次 isExists（数据量大时切换）|

---

## 六、版本兼容性提醒

- 标品 CtrlStrategyValidator 跨 release 可能扩展校验规则 · ISV Validator 不要假设 super 链路特定行为
- BaseDataBuOp 是薄壳类（源码 BaseDataBuOp.java:1-23）· 标品升级可能加新方法 · ISV 不应继承
- ListOrderCommonPlugin 是薄壳类（源码 ListOrderCommonPlugin.java:1-19）· 同上 · 禁继承
- issyspreset=true 的出厂职能主键跨版本可能调整 · 部署前查 OpenAPI 确认含义
- listRules formRule 在标品升级时可能调整条件 · ISV 加的自定义规则不要与标品规则假设相同触发逻辑

---

## 七、跟 haos_orgchangereason 异常表对比

| 异常类型 | 本场景 | haos_orgchangereason |
|---|---|---|
| onPreparePropertys 缺失陷阱（S1）| ⚠⚠ 同样易踩（标品无样板）| ⚠⚠ 同上 |
| 联动死循环（S2）| ⚠ ctrlstrategy 联动 | ⚠ 同 · ctrlstrategy 联动 |
| issyspreset 双保护（S3）| ⚠⚠ **本场景特有**（有 listRules）| 无（无 listRules）|
| HisModel iscurrentversion 陷阱（S4）| ⚠⚠ **本场景特有**（admin_org HisModel）| 无（changescene 非时序）|
| 单选 vs 多选反向查询混淆（S5）| ⚠⚠ **本场景特有**（BasedataField 单选）| 无（MulBasedata 子表路径）|
| setFilter 重写陷阱（S6）| 丢失 setOrderBy 排序 | 丢失 id != 1010L 过滤 |
| 跨环境主键（S8）| issyspreset=true 系列 | 1010L |

→ **本场景有 3 个特有陷阱**（S3/S4/S5）· 均与 admin_org 的 HisModel 时序特性和 BasedataField 单选关系相关 · 移植 haos_orgchangereason 代码时必须检查这 3 点差异。

---

## 参考

- PR-001 · PR-006 · PR-007 · PR-010
- `06_customization_solutions.md` — CS-02 详细代码（iscurrentversion 实证 · 单选路径实证）
- `08_impact_analysis.md` — 下游影响分析
