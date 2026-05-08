# 参考案例 · 行政组织变动场景（haos_changescene）

> **状态**: 🟢 基于 CS-01~06 + 标品反编译 + admin_org / homs 跨场景协作
> **数据源**: 全栈实证（无虚构客户名）

---

## 案例 1 · 加"适用业务线"字段 + 列表过滤切换（CS-01 + CS-04 组合）

### 业务场景
某集团公司分 4 个事业群（消费品/工业品/金融/科技） · HR 想为不同事业群维护各自的"变动场景字典"（如"科技事业群战略转岗" · 跟"消费品事业群岗位调整"区分）。

### 实施步骤

**Step 1**：CS-01 加字段
```python
modifyMeta({
  formId: "haos_changescene",
  ops: [{
    op: "add",
    elementType: "field",
    parentScope: "haos_changescene",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_bizline",
      name: {zh_CN: "适用业务线"},
      mustInput: false,
      enum: [
        {value: "consumer", label: "消费品"},
        {value: "industrial", label: "工业品"},
        {value: "finance", label: "金融"},
        {value: "tech", label: "科技"}
      ]
    }
  }]
})
```

**Step 2**：业务方录数据 · 给现有 25 条 changescene 标记 ${ISV_FLAG}_bizline

**Step 3**：CS-04 加列表过滤
- 在列表表单 haos_changescene 上加 ComboField 控件 `${ISV_FLAG}_bizline_filter`
- 注册插件 `TdkwChangeSceneListFilterPlugin` (HRDataBaseList 子类)
- 用户切换下拉 → setCustomParam("${ISV_FLAG}_bizline", value) → 列表刷新
- ⚠ 标品 ChangeSceneListPlugin 不感知 ${ISV_FLAG}_bizline · ISV 必须自己在 setFilter 后追加（PR-001 不重写 setFilter · 用 propertyChanged 配合自建过滤逻辑）

### 踩坑回顾
- 第一次实施时直接重写 setFilter · 导致 INV-CS-04 (id != 1070L) 失效 · 用户列表里看到 1070 项点击崩溃
- 修复方案：拆分 — 标品 setFilter 保留 · ISV 自建过滤通过 customParam 传递并在自己的 propertyChanged 里追加 QFilter

### 关联 CS
- CS-01：加字段
- CS-04：列表过滤定制

---

## 案例 2 · 删除变动场景前置校验（CS-02 真实价值演示）

### 业务场景
某客户 HR 部门有 5 名管理员 · 人多手杂 · 多次发生"误删字典数据"事故。需求：装一道"删除前查反向引用"的校验。

### 实施前后对比

**实施前（标品）**：
```
删除 changescene id=12345 "战略调整"
   → 标品 BaseDataDeletePlugin 直接 DELETE
   → 200+ 张历史申请单 changescene 字段全部变孤儿
   → 业务报表"按变动类型统计"出错
   → 紧急回滚（DBA 手工 INSERT 恢复）
```

**实施后（CS-02）**：
```
删除 changescene id=12345
   → ChangeSceneRefCheckOp.onAddValidators
   → ChangeSceneDeleteValidator.validate()
   → for each 7 个 entry 字段:
       QueryServiceHelper.exists("homs_orgbatchchgbill", QFilter(fk, "=", 12345))
       命中 → addErrorMessage("变动场景[战略调整]已被组织调整申请单的 changescene 字段引用·不可删除")
   → 用户看到红色提示 · 操作被拦
```

### 实施细节
- ISV 类：`com.kingdee.${ISV_FLAG}.haos.opplugin.web.ChangeSceneRefCheckOp` extends HRDataBaseOp
- 内部 Validator：`ChangeSceneDeleteValidator` extends AbstractValidator
- 同时挂 `delete` 和 `disable` 两个 opKey（业务方反馈：禁用前也想看到引用情况）
- RowKey=1（早于标品 4 个 delete 插件）

### 性能数据
- 单次 delete 校验 8 个 exists 查询（7 个 entry + 1 个 detail）· 每个 ~2ms · 总计 ~16ms · 用户感知不到
- 用 `QueryServiceHelper.exists` 而非 `BusinessDataServiceHelper.load` · 走 `count(1) limit 1` · 性能远优于全行加载

### 关联 CS · PR
- CS-02：核心实施
- PR-001：并列挂 · 不继承 ChangeSceneSaveOp
- PR-002：RowKey=1 · 早于标品
- PR-007：出厂数据双重保护
- PR-010：onAddValidators 阶段（不在 afterExecute）

---

## 案例 3 · 自定义"临时调整"类型联动（CS-03）

### 业务场景
某客户的人事流程引入"临时调整"概念（如疫情期间临时部门调整 · 后续可能撤销）· 这种场景跟标品的"调整"不同 · 默认操作应该是"紧急更新"而非标品的"更新"。

### 实施步骤

**Step 1**：在 haos_orgchangetype 字典加一条 "临时调整" (${ISV_FLAG}_temp_adjust) · 拿到 id=11000

**Step 2**：在 haos_orgchangeoperate 字典加一条 "紧急更新" (${ISV_FLAG}_urgent_update) · 拿到 id=11001

**Step 3**：写 TdkwChangeSceneEditPlugin（参考 CS-03 代码框架）：
- propertyChanged 监听 orgchangetype
- 命中 typeId=11000 → setValue("changeoperat", new Object[]{11001L})

**Step 4**：因为可能走 HIES 导入路径 · 还需要在 OP 层挂 TdkwChangeSceneSaveOp（同样判断 typeId · beginOperationTransaction 阶段做兜底）

### 关键决策
- ❌ 不继承 ChangeSceneEditPlugin（PR-001）
- ❌ 不去改 ChangeSceneServiceHelper（标品 jar · ISV 改不动）
- ✅ 并列挂 · 标品先跑（设默认值）· ISV 后跑（覆盖特殊类型）

### 关联 CS · PR
- CS-03：核心实施
- PR-001 / PR-002 / PR-003 / PR-004（FormPlugin 联动套餐）

---

## 案例 4 · 子表 + ID 生成（CS-06）

### 业务场景
某客户希望给 changescene 加一张"适用组织规模明细"子表 · 每行 (size_min, size_max, biz_code)。

### 实施步骤

**Step 1**：CS-06 通过 modifyMeta 加 EntryEntity `${ISV_FLAG}_size_entry`（含字段 size_min/size_max/${ISV_FLAG}_biz_code）

**Step 2**：写 TdkwSizeEntryAppendOp（参考 CS-06 代码框架）：
- onPreparePropertys 声明 `${ISV_FLAG}_size_entry` 子集合
- beginOperationTransaction 遍历每行 · 用 `ID.genLongId()` 补 row.id · 用 `ID.genStringId()` 补 biz_code

**Step 3**：RowKey=1（早于标品 8 个 save 插件 · 在 CodeRuleOp 之前补 id）

### 关键 PR-005 实证

```java
// ❌ 错误：UUID
row.set("id", UUID.randomUUID().getMostSignificantBits());

// ❌ 错误：timestamp
row.set("id", System.currentTimeMillis());

// ❌ 错误：select max+1
long maxId = (Long) DB.query(...);
row.set("id", maxId + 1);

// ✅ 正确：ID.genLongId
row.set("id", kd.bos.id.ID.genLongId());

// ✅ 正确：业务编码用 genStringId
row.set("${ISV_FLAG}_biz_code", "BL_" + kd.bos.id.ID.genStringId());
```

### 关联 CS · PR
- CS-06：核心实施
- PR-005：ID 生成铁律

---

## 案例 5 · 跨场景协作 · 跟 homs_orgbatchchgbill 联动

### 业务场景
HR 集团希望"新增一种变动场景后 · 自动在 homs_orgbatchchgbill 的某个默认审批模板里设置该场景为可选"（业务上"字典维护人 ↔ 申请单模板维护人"的协作）。

### 实施判断

**❌ 不要做 BEC 发布方**（CS-05 反指引）：
- 标品没在本场景发 BEC（grep 0 命中实证）
- 自建发布方意味着改本场景的所有人都要触发跨模块通知 · 性能 + 维护成本大
- 业务上"字典维护"频率极低（一年几次）· 不需要异步分发

**✅ 应该走的方案**：业务侧手工维护 · 或通过定时任务（kd.bos.schedule）跑同步：
- 凌晨 1 点扫描 haos_changescene enable=1 的全量数据
- 跟 homs_orgbatchchgbill 的某个"默认场景模板"基础资料对比
- 缺失则自动加 · 多余则提醒人工确认（不自动删 · 防误操作）

→ 这种逻辑应该挂在【调度任务管理】而非【变动场景表单插件】。

### 关联 PR
- PR-011：BEC 不滥用
- 反指引：基础资料场景跨模块通知应走调度任务而非事件

---

## 案例 6 · 误用标品类继承的回滚案例

### 错误尝试

某新手 ISV 开发想做"删除变动场景前校验" · 直接：
```java
public class WrongChangeSceneSaveOp extends ChangeSceneSaveOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);  // 继承标品
        args.addValidator(new MyDeleteValidator());
    }
}
```

### 出问题点

1. 标品下次升级 · `ChangeSceneSaveOp.onAddValidators` 方法签名加了一个参数（如换成 `onAddValidatorsV2`）
2. ISV 编译失败 · 整个客户项目部署不上
3. 紧急排查 · 才发现 PR-001 铁律：**禁止继承场景特有类**

### 正确写法（参考 CS-02）
```java
public class ChangeSceneRefCheckOp extends HRDataBaseOp {  // ✅ 通用基类
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);  // 调用 HRDataBaseOp 的 super · 安全
        args.addValidator(new ChangeSceneDeleteValidator());
    }
}
```

→ 通过【开发平台】把 ChangeSceneRefCheckOp 跟标品 ChangeSceneSaveOp 并列挂 · 互不影响 · 标品升级也不会编译失败。

### 关联 PR
- PR-001：禁继承场景特有类 · 并列挂

---

## 案例 7 · ISV 加字段后保存空值的隐藏 BUG

### 业务场景
ISV 加了 ComboField `${ISV_FLAG}_bizline` · 但用户保存时没选 · 字段值是空。后台报错"NullPointerException"。

### 排查发现
- ChangeSceneSaveOp.onPreparePropertys 只声明了 `orgchangetype` 和 `changeoperat`
- ISV 字段 `${ISV_FLAG}_bizline` 平台默认会走但不在 onPreparePropertys 里就拿不到
- ISV 自己写的 Validator 想读 dataEntity.getString("${ISV_FLAG}_bizline") · 拿到 null · 后续 .trim() 报 NPE

### 修复
```java
public class TdkwChangeSceneCheckOp extends HRDataBaseOp {
    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("${ISV_FLAG}_bizline");  // ✅ 必须显式加
    }

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new MyValidator());
    }
}

// 在 Validator 里读取时 · 也要做 null 防护
String bizline = dataEntity.getString("${ISV_FLAG}_bizline");
if (HRStringUtils.isEmpty(bizline)) { ... }
```

### 关联 PR
- PR-010：OP 13 方法 · onPreparePropertys 是第 2 步 · 必须先声明
- 反编译实证：`ChangeSceneSaveOp.java:36-39` 就是这种模式（声明 orgchangetype/changeoperat）

---

## 总结：6 个 CS 实战推荐路径

| 业务诉求 | 走哪个 CS |
|---|---|
| "想给变动场景加字段" | CS-01（最简单 · 改元数据）|
| "想加子表（多行明细）" | CS-06（带 ID.genLongId 实证）|
| "想拦删除避免误删" | CS-02（最重要 · 必装）|
| "想自定义字段联动" | CS-03（FormPlugin 层）|
| "想列表过滤" | CS-04（不重写 setFilter · 用 customParam）|
| "想发跨模块通知" | ❌ 不要走 CS-05 · 改用调度任务 |
