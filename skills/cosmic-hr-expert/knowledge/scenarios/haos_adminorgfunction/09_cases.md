# 参考案例 · 行政组织职能（haos_adminorgfunction）

> **状态**: 🟢 基于 CS-01~05 + 标品反编译 + haos_adminorg 配对协作
> **数据源**: 全栈实证（无虚构客户名）

---

## 案例 1 · 加"职能分类"字段（CS-01）

### 业务场景
某集团 HR 部门希望在行政组织职能维度引入"职能分类"（核心/支撑/创新）标签，用于：
1. 业务报表里"按职能分类统计组织数量"
2. 规划层：划分核心业务职能 vs 支撑性职能 vs 创新型职能
3. 给 BI 系统按分类筛选数据源

### 实施步骤

**Step 1**：CS-01 加字段
```python
modifyMeta({
  formId: "haos_adminorgfunction",
  ops: [{
    op: "add",
    elementType: "field",
    parentScope: "haos_adminorgfunction",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_funccategory",
      name: {zh_CN: "职能分类"},
      mustInput: false,
      enum: [
        {value: "core", label: "核心 · 业务驱动"},
        {value: "support", label: "支撑 · 平台保障"},
        {value: "innovation", label: "创新 · 探索孵化"}
      ]
    }
  }]
})
```

**Step 2**：业务方录数据 · 给现有 N 条 adminorgfunction 标记 ${ISV_FLAG}_funccategory

**Step 3**：在 haos_adminorg 对应的报表或列表展示里加上 adminorgfunction.${ISV_FLAG}_funccategory 关联展示
- 因为 adminorgfunction 是 BasedataField 单选 · 平台 ORM 支持 `adminorgfunction.${ISV_FLAG}_funccategory` 路径查询

### 踩坑回顾
- 第一次实施时字段 key 起的是 `funccategory` · 没带 ISV 前缀 (${ISV_FLAG}_) · 测试环境部署后被标品平台升级"清理"了一次 · 字段值丢失。修复方案：改 ISV 前缀重新部署。
- 业务方录入时不知道枚举值 label 跟 value 的差别 · 把 "核心 · 业务驱动" 当成实际存储值写到 SQL 查询里 · 报错。修复方案：写文档说明 ComboField 存储 value · 不是 label。
- 加字段后想在 OP 层读取 · 始终是 null → 忘了在 onPreparePropertys 显式声明。修复：加 `e.getFieldKeys().add("${ISV_FLAG}_funccategory")`。

### 关联 CS
- CS-01：加字段
- PR-001 · PR-007 · PR-010

---

## 案例 2 · 删除/禁用职能前置校验（CS-02 真实价值演示）

### 业务场景
某客户 HR 部门有 5 名管理员 · 多次发生"误删字典数据"事故，admin_org 数据展示出现空白职能字段。需求：装一道"删除前查反向引用"的校验。

### 实施前后对比

**实施前（标品）**：
```
删除 adminorgfunction id=12345 "运营支撑"
   → 标品 BaseDataDeletePlugin 直接 DELETE
   → t_haos_adminorg 当前生效版本 23 行 fadminorgfunctionid=12345 变游离
   → haos_adminorg 列表 adminorgfunction 列出现大量空白
   → 组织汇总报表"按职能统计" "运营支撑"那列消失
   → 紧急回滚（DBA 手工 INSERT 恢复）
```

**实施后（CS-02）**：
```
删除 adminorgfunction id=12345
   → AdminOrgFunctionRefCheckOp.onAddValidators
   → AdminOrgFunctionDeleteValidator.validate()
   → HRBaseServiceHelper("haos_adminorg").isExists(
       new QFilter("adminorgfunction", "=", 12345L)
         .and(new QFilter("iscurrentversion", "=", Boolean.TRUE)))
   → 命中 → addErrorMessage("行政组织职能[运营支撑]已被行政组织当前生效版本引用·不可删除·建议改用禁用")
   → 用户看到红色提示 · 操作被拦
```

### 实施细节
- ISV 类：`com.kingdee.${ISV_FLAG}.haos.opplugin.web.AdminOrgFunctionRefCheckOp` extends HRDataBaseOp
- 内部 Validator：`AdminOrgFunctionDeleteValidator` extends AbstractValidator
- 同时挂 `delete` 和 `disable` 两个 opKey（业务方反馈：禁用前也想看到引用情况）
- RowKey=1（早于标品 4 个 delete 插件）
- **关键差异（vs haos_orgchangereason CS-02）**：
  - 本场景查路径 `QFilter("adminorgfunction", "=", id)` 直字段查（单选 BasedataField）
  - haos_orgchangereason 走 `QFilter("changereason.fbasedataid", "=", id)` 子表路径（多选 MulBasedataField）
  - 本场景必须加 `iscurrentversion=true`（haos_orgchangereason 不需要）

### 性能数据
- 单次 delete 校验 1 个 isExists 查询（haos_adminorg）· ~3-5ms · 用户感知不到
- 用 `HRBaseServiceHelper.isExists` 而非 `BusinessDataServiceHelper.load` · 走 `count(1) limit 1` · 性能远优于全行加载
- 跟 haos_orgchangereason CS-02 性能相当 · iscurrentversion 走索引不影响速度

### 关联 CS · PR
- CS-02：核心实施
- PR-001：并列挂 · 不继承 BaseDataBuOp
- PR-002：RowKey=1 · 早于标品
- PR-007：出厂数据双重保护（issyspreset=true 拦）
- PR-010：onAddValidators 阶段（不在 afterExecute）

---

## 案例 3 · 自定义排序规则定制（列表层）

### 业务场景
某集团 HR 运营团队希望职能列表按"职能分类优先 · 同类内按名称升序"展示 · 而不是标品的 `enable desc, number asc`。

### 标品现状分析
反编译实证：ListOrderCommonPlugin.java:1-19 · 该类仅做：
```
// 伪码示意标品逻辑（源码 ListOrderCommonPlugin.java:1-19）
// 注意：仅做排序 · 不做过滤（与 haos_orgchangereason 不同）
setOrderBy("enable desc, number asc")
```

### 推荐实施方案

**错误方案（不要做）**：
```java
// ❌ 继承 ListOrderCommonPlugin
public class WrongOrderPlugin extends ListOrderCommonPlugin {
    // ... 标品升级 ListOrderCommonPlugin 签名变 → ISV 编译失败
}
```

**正确方案 A（最简 · 用 customParam 传自定义排序）**：
- 在列表页 beforeBindData 里动态修改排序条件（RowKey ≥ 100 · 晚于标品）
- 使用 `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList` 作为父类（白名单）

**正确方案 B（如需按 ISV 字段排序）**：
- CS-01 先加好 `${ISV_FLAG}_funccategory` 字段
- 开发平台配置列表默认排序 · 增加 `${ISV_FLAG}_funccategory asc, name asc` 二级排序

### 踩坑回顾
- 继承 ListOrderCommonPlugin 第一次打包没报错 · 但标品升级后发现类的构造函数签名变了 · ISV 插件加载失败。
- 修复：改用 HRBaseDataTplList + 并列挂 · RowKey ≥ 100 · 不继承任何场景特有类。

### 关联 CS · PR
- 场景轻量 · 通常走平台配置 · 不需要额外 CS
- PR-001：禁继承 ListOrderCommonPlugin

---

## 案例 4 · ctrlstrategy 自定义校验（CS-03）

### 业务场景
某客户的行政组织规划引入"职能控制策略合规审计"概念：
- 选了"同分配范围"(ctrlstrategy=8) 的职能 · 必须落在指定 BU
- 跨 BU 选错会导致后续 admin_org 报表统计错误

### 实施步骤

**Step 1**：写 TdkwAdminOrgFunctionEditPlugin（参考 CS-03 代码框架）：
- propertyChanged 监听 ctrlstrategy
- 命中 ctrlstrategy="8" 时检查 org 是否在白名单
- 不在 → showTipNotification + setValue 回滚 + beginInit/endInit 防死循环（PR-004）

**Step 2**：注册插件 · RowKey=200（晚于 BdCtrlStrtgyShowLogicPlugin / HRBaseDataTplEdit）

**Step 3**：维护 ALLOWED_BU_LIST（应抽到 ISV 配置基础资料 · 不硬编码 · 跨环境主键问题）

### 关键决策
- ❌ 不继承 BdCtrlStrtgyShowLogicPlugin（bos 平台插件 · ISV 不应继承 bos.* 内部）
- ❌ 不在 OP 层做 · UI 体验差 · 应该 propertyChanged 早拦
- ✅ 并列挂 + super 调用 + setValue 回滚 + beginInit/endInit

### 踩坑回顾
- 第一次写没加 beginInit/endInit · 用户改 ctrlstrategy 触发死循环 · 浏览器卡死。修复：加 beginInit/endInit 包裹回滚逻辑。
- 第二次写把 ALLOWED_BU_LIST 硬编码到代码里 · 测试环境正常 · 生产环境主键不一致 · 永远校验失败。修复：改用 BasedataField 引用 ISV 字典。

### 关联 CS · PR
- CS-03：核心实施
- PR-001 / PR-002 / PR-003 / PR-004（FormPlugin 联动套餐）

---

## 案例 5 · 误用标品类继承的回滚案例

### 错误尝试

某新手 ISV 开发想做"删除职能前校验" · 直接：
```java
// ❌ 错误：继承场景特有类
public class WrongAdminOrgFunctionOp extends BaseDataBuOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);  // 继承标品
        args.addValidator(new MyDeleteValidator());
    }
}
```

### 出问题点

1. 标品下次升级 · `BaseDataBuOp` 父类方法签名变化或类重构
2. ISV 编译失败 · 整个客户项目部署不上
3. 紧急排查 · 才发现 PR-001 铁律：**禁止继承场景特有类**

### 正确写法（参考 CS-02）
```java
// ✅ 正确：继承通用白名单基类
public class AdminOrgFunctionRefCheckOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);  // 调用 HRDataBaseOp 的 super · 安全
        args.addValidator(new AdminOrgFunctionDeleteValidator());
    }
}
```

→ 通过【开发平台】把 AdminOrgFunctionRefCheckOp 跟标品 BaseDataBuOp 并列挂 · 互不影响 · 标品升级也不会编译失败。

### 关联 PR
- PR-001：禁继承场景特有类 · 并列挂

---

## 案例 6 · ISV 加字段后 OP 读不到值的隐藏 BUG

### 业务场景
ISV 加了 ComboField `${ISV_FLAG}_funccategory` · 在 AdminOrgFunctionDeleteValidator 的 validate() 里想读这个字段做条件判断（如"核心职能的不让删"）· 始终是 null。

### 排查发现
- 反编译实证 BaseDataBuOp.java:1-23 · 这个场景特有 OP **只重写了 onAddValidators 一个方法** · 没有 onPreparePropertys
- 平台默认 onPreparePropertys 行为只加载白名单字段 · ISV 字段 `${ISV_FLAG}_funccategory` 不在白名单
- ISV 自己写的 AdminOrgFunctionRefCheckOp 只重写了 onAddValidators · 没重写 onPreparePropertys
- Validator 里 `dataEntity.getString("${ISV_FLAG}_funccategory")` 始终是 null · 即使 UI 上明明选了值

### 修复

```java
// 源码参考：AdminOrgFunctionRefCheckOp.java
public class AdminOrgFunctionRefCheckOp extends HRDataBaseOp {

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("${ISV_FLAG}_funccategory");  // ✅ 必须显式加
        // 如果有子表 · 子表也要声明
        // e.getFieldKeys().add("${ISV_FLAG}_func_entry");
    }

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new AdminOrgFunctionDeleteValidator());
    }
}

// 在 Validator 里读取时 · 也要做 null 防护
String category = dataEntity.getString("${ISV_FLAG}_funccategory");
if (HRStringUtils.isEmpty(category)) { /* 没选职能分类 · 跳过 */ }
```

### 关联 PR
- PR-010：OP 13 方法 · onPreparePropertys 是第 2 步 · 必须先声明
- 跟 haos_orgchangereason 案例 7 同源（同一平台陷阱 · BaseDataBuOp 都没有 onPreparePropertys 样板）

---

## 案例 7 · 预置职能保护双保险（issyspreset + listRules）

### 业务场景
某客户的 HR 管理员在职能列表里对标品预置职能（如"行政"/"综合"等）尝试修改 number 字段 · 要求拦截。

### 标品保护机制分析

**保护层 1**：isvCanModify=false（字段级 · INV-AF-03）
- 平台出厂数据 `issyspreset=true` + `isvCanModify=false` · ISV 字段不可改
- 覆盖范围：字段设置级 · 非运行时 UI 拦截

**保护层 2**：1 条 listRules formRule（INV-AF-04）
- 标品有 1 条 listRules formRule · 条件：`issyspreset = true`
- 触发时：该行进入只读模式 · 平台 UI 不可编辑
- 这是与 haos_orgchangereason 的一个关键差异（haos_orgchangereason 没有 listRules）

### ISV 额外加固（如有需要）

如业务方要求更强的运行时提示 · 可在 CS-02 的 Validator 里追加：
```java
// 在 AdminOrgFunctionDeleteValidator.validate() 里
if (dataEntity.getBoolean("issyspreset")) {
    this.addErrorMessage(ext,
        "系统预置职能不可删除 (issyspreset=true · INV-AF-03/04)");
    continue;  // 跳过后续引用检查
}
```

### 踩坑回顾
- 不了解标品有 listRules 保护 · ISV 又另外写了一个 FormPlugin 的 beforeDoOperation 拦预置数据操作 · 双重拦截导致错误消息出现 2 次。
- 修复：ISV 只在 OP 层兜底 · 不需要再加表单层拦截（标品 listRules 已经够用）。

### 关联 PR
- PR-001 · PR-007（出厂数据保护铁律）

---

## 总结：5 个 CS 实战推荐路径

| 业务诉求 | 走哪个 CS |
|---|---|
| "想给职能加字段" | CS-01（最简单 · 改元数据）|
| "想加子表（多行明细）" | CS-05（带 ID.genLongId 实证）|
| "想拦删除避免误删 admin_org 孤儿外键" | CS-02（最重要 · 必装 · 记得加 iscurrentversion）|
| "想自定义字段联动校验" | CS-03（FormPlugin 层）|
| "想编码规则定制" | CS-04（不写代码 · 走平台 PR-006）|
| "想发跨模块通知" | ❌ 不要走 CS-05 BEC 反指引 · 改用调度任务 |

→ 跟 haos_orgchangereason 6 个 CS 相比 · 本场景**同样 5 个 CS** · CS-02 的 Validator 有 HisModel 差异（需加 iscurrentversion）· 其他 CS 结构一致。
