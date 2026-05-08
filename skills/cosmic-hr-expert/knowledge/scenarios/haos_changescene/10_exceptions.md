# 异常诊断 · 行政组织变动场景（haos_changescene）

> **状态**: 🟢 基于反编译 + cosmic_realworld_traps 共性陷阱 + 本场景特有踩坑
> **数据源**: knowledge/cosmic_realworld_traps/* + 反编译 3 类
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
| 加 entry 子表后 buildMeta 不传 parentId · 兜底到 bos 基础资料模板 | parentId 默认 = bos 基础资料模板（非 haos_changescene）| 显式指定 ENTITY_PARENT_ID=haos_changescene |

### T2 · addRule 陷阱

> 数据源：`knowledge/cosmic_realworld_traps/addrule_traps.md`

| 现象 | 原因 | 解决 |
|---|---|---|
| addRule preCondition 用 `xx == ''` 报语法错误 | preCondition 不支持空字符串比较 | 改用 `IsEmpty(xx)` 函数 |
| ruleType / actionType 大小写敏感 · 小写静默忽略 | 必须 PascalCase（actionType=Calculate / SetValue / Mandatory 等）| 严格 PascalCase |
| addRule 返回成功但 listRules 查不到 | 参数 itemId 写错（应该是 formId 的技术 ID）| 用 getFormSchema 查准确技术 ID |

### T3 · 操作链 / OP 插件陷阱

| 现象 | 原因 | 解决 |
|---|---|---|
| OP 里 getModel().setValue 报空指针 | OP 在后端没 model · 应该用 entity.set | 改用 `args.getDataEntities()[i].set(key, value)`（PR-003）|
| onAddValidators 注册 Validator 没生效 | 在 beforeExecuteOperationTransaction 阶段注册晚了 | 必须在 onAddValidators 阶段（PR-010 第 4 步）|
| beforeExecuteOperationTransaction 抛异常 → 整笔事务回滚 | 平台行为正常 | 用 addErrorMessage 替代抛异常更优雅 |
| OP 读不到 ISV 加的字段值 | onPreparePropertys 没声明该字段 | 显式 `e.getFieldKeys().add("${ISV_FLAG}_xxx")` |

---

## 二、本场景特有陷阱（反编译实证）

### S1 · MulBasedata 子表写值的类型陷阱

#### 现象
```java
this.getModel().setValue("changeoperat", changeOperateId);   // ❌ 单个 Long
```
平台抛异常 "类型不匹配 · 期望 Object[]"。

#### 原因
`changeoperat` 是 `MulBasedataField` · 平台底层维护的是 DynamicObjectCollection（多选关系子表）· setValue 必须传数组。

#### 解决
反编译实证 `ChangeSceneEditPlugin.java:31`：
```java
this.getModel().setValue("changeoperat", new Object[]{changeOperateId});  // ✅
```

或在 OP 层操作 collection（实证 `ChangeSceneSaveOp.java:49-54`）：
```java
DynamicObjectCollection collection = dataEntity.getDynamicObjectCollection("changeoperat");
collection.clear();
DynamicObject changeOperateObj = new DynamicObject(collection.getDynamicObjectType());
changeOperateObj.set("fbasedataid", (Object) changeOperateId);
collection.add(changeOperateObj);
```

### S2 · 联动死循环陷阱（PR-004）

#### 现象
ISV 在 propertyChanged 里写：
```java
if (property.equals("orgchangetype")) {
    setValue("orgchangetype", processedValue);  // ❌ 触发新的 propertyChanged
}
```
死循环 · 浏览器卡死。

#### 原因
`setValue` 本身是字段变更事件 · 触发新的 propertyChanged。

#### 解决（PR-004 实证）
```java
this.getModel().beginInit();
this.getModel().setValue("orgchangetype", processedValue);
this.getModel().endInit();
this.getView().updateView("orgchangetype");
```

> 注：标品 ChangeSceneEditPlugin 不需要 beginInit/endInit · 因为它改的是不同字段 (`changeoperat`) · 不会回触自己的 propertyChanged。
> ISV 扩展时若改的是同字段必须用 beginInit。

### S3 · 反向引用查询忘了某个字段的"7 选其一"陷阱

#### 现象
CS-02 实施完 · 上线后还是发生了误删事故。排查发现 ISV 校验只查了主体 entry 的 `changescene` · 漏了其他 6 个 `*_changescene` 字段。

#### 解决
按 03 §1 实证 · 必须循环查 7 个字段：
```java
String[] ORGBILL_ENTRY_FIELDS = {
    "changescene",
    "add_changescene", "parent_changescene",
    "info_changescene", "disable_changescene",
    "merge_changescene", "split_changescene"
};
```
+ 跨表查 `haos_adminorgdetail.changescene`。

### S4 · ChangeSceneListPlugin.setFilter 重写后 INV 失效陷阱

#### 现象
ISV 重写 `setFilter@haos_changescene` 后 · 用户列表里突然能看到 id=1070 的"模板项"了 · 点击崩溃。

#### 原因
INV-CS-04（`id != 1070L`）是 ChangeSceneListPlugin.setFilter 硬编码 · ISV 重写后丢了。

#### 解决（CS-04）
- ❌ 不要重写 `setFilter@haos_changescene`
- ✅ 走 customParam 传值 · 让标品 ChangeSceneListPlugin 继续跑（标品逻辑保留）
- ✅ 自建过滤通过自己的 propertyChanged → setCustomParam → refresh

### S5 · onPreparePropertys 没声明 ISV 字段 · 后端拿到 null

#### 现象
ISV Validator 里 `dataEntity.getString("${ISV_FLAG}_bizline")` 始终是 null · 即使 UI 上明明选了值。

#### 原因
苍穹 OP 层为了性能默认只加载"白名单字段" · ISV 加的字段必须在 onPreparePropertys 显式声明（PR-010 第 2 步）。

#### 解决
```java
@Override
public void onPreparePropertys(PreparePropertysEventArgs e) {
    super.onPreparePropertys(e);
    e.getFieldKeys().add("${ISV_FLAG}_bizline");
    e.getFieldKeys().add("${ISV_FLAG}_size_entry");        // 子表也要声明
    e.getFieldKeys().add("${ISV_FLAG}_size_entry.${ISV_FLAG}_biz_code");  // 子表字段也要
}
```

→ 实证：`ChangeSceneSaveOp.java:36-39` 标品就是这样声明 orgchangetype / changeoperat 的。

### S6 · 标品 ChangeSceneSaveOp.beginOperationTransaction 在导入路径下覆盖 ISV 联动结果

#### 现象
ISV 在 ChangeSceneEditPlugin 之后挂了自定义联动（CS-03 风格）· 在 UI 上联动正常。但用户走 HIES 导入路径时 · 数据保存后 changeoperat 又被改回标品值了。

#### 原因
反编译实证 `ChangeSceneSaveOp.java:41-56`：
```java
if (HRStringUtils.isEmpty((String)((String)this.getOption().getVariables().get("importtype")))) {
    return;  // 普通保存路径走这里 · ISV 在 FormPlugin 设的值保留
}
// 导入路径会执行下面这段 · 用 ChangeSceneServiceHelper.getChangeOperate 重置 changeoperat
```

#### 解决
ISV 必须在 OP 层也加并列插件 · 同样判断 importtype 是否非空 + typeId 是否是自定义类型 · 做相应覆盖：
```java
public class TdkwChangeSceneSaveOp extends HRDataBaseOp {
    @Override
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        if (HRStringUtils.isEmpty((String) this.getOption().getVariables().get("importtype"))) {
            return;
        }
        for (DynamicObject dataEntity : e.getDataEntities()) {
            long typeId = dataEntity.getLong("orgchangetype.id");
            if (typeId == TEMP_ADJUST_TYPE_ID) {
                DynamicObjectCollection coll = dataEntity.getDynamicObjectCollection("changeoperat");
                coll.clear();
                DynamicObject obj = new DynamicObject(coll.getDynamicObjectType());
                obj.set("fbasedataid", URGENT_OPERAT_ID);
                coll.add(obj);
            }
        }
    }
}
```
RowKey 排在标品 ChangeSceneSaveOp 之后（取大值）· 标品先按硬编码逻辑走 · ISV 后覆盖。

### S7 · 出厂数据隐藏字段（1010/1070/1100_S）跨环境变化陷阱

#### 现象
ISV 在测试环境写好 CS-04 列表过滤逻辑 · 上线生产后发现 customParam.otclassify 不生效 · 按 1010 过滤反而是空。

#### 原因
跨环境出厂数据主键可能不一致（虽然多数环境 1010=ADMINISTRATIVE 一致 · 但不同版本可能改）。
参考 `feedback_har_values_not_authoritative.md`：跨环境主键不可硬假设。

#### 解决
- 部署前用 OpenAPI 在生产环境查 `haos_otclassify` 全量 · 确认 1010L 主键存在
- 或代码里走 number 而非 id：
  ```java
  // 不写 1010L · 走 number = "ADMINISTRATIVE" 查 id
  DynamicObject otclassify = HRBaseServiceHelper.queryOne("haos_otclassify",
      "id", new QFilter("number", "=", "ADMINISTRATIVE"));
  long realId = otclassify.getLong("id");
  ```

### S8 · 多语言表查不到字段陷阱

#### 现象
ISV 直接 `SELECT fname FROM t_haos_changescene WHERE fid=?` · 报错列不存在。

#### 原因
`fname` / `fsimplename` / `fdescription` / `foriname` 在多语言表 `t_haos_changescene_l` · 不在主表。

#### 解决
- ✅ 用 DynamicObject 走 ORM 层：`dataEntity.getString("name")` 自动走 join
- ❌ 不要写裸 SQL（除非确认表结构跨语言情况）

参考 03 §3.1 字段分组实证。

---

## 三、错误码诊断表

| 错误码 / 异常 | 意义 | 排查方向 |
|---|---|---|
| `KDBizException: 变动类型 不能为空` | INV-CS-01 触发 | 用户没填 orgchangetype · 平台 BasedataField 必填校验 |
| `KDBizException: 变动场景[xxx]已被组织调整申请单的 changescene 字段引用·不可删除` | CS-02 拦截 | 业务人员尝试删被引用的字典数据 · 提示用 disable 替代 |
| `IllegalArgumentException: 类型不匹配 期望 Object[]` | S1 陷阱 | setValue MulBasedataField 没传数组 |
| `NullPointerException` 在 ISV Validator 里 | S5 陷阱 | onPreparePropertys 没声明 ISV 字段 |
| `errorCode=0 但 getFormSchema 查不到字段` | T1 陷阱 | 字段类型用了不支持的 EmployeeField 等 |
| `OperationServiceException: 当前数据状态不允许此操作` | HRBaseDataStatusOp 拦截 | status=B/C 时不能改 · 先 unsubmit/unaudit |
| `enable=0 的数据不在 F7 显示` | 平台 BaseDataField 默认行为 | 业务方反映"找不到字典"时检查是否被禁用过 |

---

## 四、调试技巧

### 4.1 查标品执行链

```python
# 用 OpenAPI 查 save opKey 的执行链
client.list_operations(form_number="haos_changescene", op_key="save")
# 返回 8 个标品插件 · 跟 opkeys/save.json 一致
```

### 4.2 grep 反编译产物（铁律 5）

```bash
# 查 BEC 是否启用
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_changescene/

# 查具体读写字段
grep -rE 'set\(|get(String|Long|Boolean|Date|DynamicObject)' \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_changescene/

# 查 super 调用关系
grep -rE 'super\.' knowledge/_sdk_audit/_decompiled/scenarios/haos_changescene/
```

### 4.3 跨环境主键验证

```python
# 验证 1010L 主键
result = client.query_basedata(
    form_number="haos_otclassify",
    select="id, number, name",
    filter=[["number", "=", "ADMINISTRATIVE"]]
)
assert result['data'][0]['id'] == 1010, "跨环境主键不一致 · 修代码走 number"
```

### 4.4 反向引用查询模板

```java
// 查 haos_changescene id=12345 被多少地方引用
String[] FIELDS_IN_ORGBILL = {
    "changescene", "add_changescene", "parent_changescene",
    "info_changescene", "disable_changescene",
    "merge_changescene", "split_changescene"
};
HRBaseServiceHelper helper = new HRBaseServiceHelper("homs_orgbatchchgbill");
int totalCount = 0;
for (String fk : FIELDS_IN_ORGBILL) {
    int c = helper.count(new QFilter(fk, "=", 12345L));
    totalCount += c;
    LOG.info("[" + fk + "] 引用数: " + c);
}
LOG.info("homs 总引用数: " + totalCount);
```

---

## 五、性能调优要点

| 场景 | 优化点 |
|---|---|
| CS-02 反向引用查询 | 用 `QueryServiceHelper.exists` 走 limit 1 · 不用 BusinessDataServiceHelper.load |
| CS-03 联动 | 在 propertyChanged 里只调一次 ChangeSceneServiceHelper.getChangeOperate · 不重复 |
| 列表查询 | 不要重写 setFilter（保留标品的 QFilter 索引命中）|
| ISV Validator 批量校验 | 用 `IN` 一次查所有 id 的反向引用 · 不要循环 N 次 exists（数据量大时切换）|

---

## 六、版本兼容性提醒

- 标品 ChangeSceneServiceHelper.getChangeOperate 方法签名跨 release 可能变 · ISV 不要直接调（虽然 SDK 类）· 改用本地映射表
- INV-CS-04 / INV-CS-05 的硬编码主键（1070 / 1100_S）跨版本可能调整 · 部署前查 OpenAPI 确认
- 字段类型 `MulBasedataField` 和 `BasedataField` 在 modifyMeta 加字段时区别明显 · 别混
