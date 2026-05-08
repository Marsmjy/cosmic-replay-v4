# 参考案例 · 行政组织变动原因（haos_orgchangereason）

> **状态**: 🟢 基于 CS-01~05 + 标品反编译 + haos_changescene 配对协作
> **数据源**: 全栈实证（无虚构客户名）

---

## 案例 1 · 加"业务影响等级"字段（CS-01）

### 业务场景
某集团 HR 部门希望在变动原因维度引入"业务影响等级"（高/中/重）· 用于：
1. 业务报表里"按影响等级统计变动数量"
2. 高影响等级的变动需走更严的审批
3. 给 BI 系统按等级筛选数据源

### 实施步骤

**Step 1**：CS-01 加字段
```python
modifyMeta({
  formId: "haos_orgchangereason",
  ops: [{
    op: "add",
    elementType: "field",
    parentScope: "haos_orgchangereason",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_bizimpact",
      name: {zh_CN: "业务影响等级"},
      mustInput: false,
      enum: [
        {value: "high", label: "高 · 战略级"},
        {value: "medium", label: "中 · 部门级"},
        {value: "low", label: "低 · 团队级"}
      ]
    }
  }]
})
```

**Step 2**：业务方录数据 · 给现有 18 条 changereason 标记 ${ISV_FLAG}_bizimpact

**Step 3**：在 haos_changescene 对应场景的报表加上 changereason.${ISV_FLAG}_bizimpact 关联展示
- 因为 changereason 是 MulBasedataField · 平台 ORM 支持 `changereason.${ISV_FLAG}_bizimpact` 路径查询

### 踩坑回顾
- 第一次实施时字段 key 起的是 `bizimpact` · 没带 ISV 前缀 (${ISV_FLAG}_) · 测试环境部署后被标品平台升级"清理"了一次 · 字段值丢失。修复方案：改 ISV 前缀重新部署。
- 业务方录入时不知道枚举值 label 跟 value 的差别 · 把 "高 · 战略级" 当成实际存储值写到 SQL 查询里 · 报错。修复方案：写文档说明 ComboField 存储 value · 不是 label。

### 关联 CS
- CS-01：加字段
- PR-001 · PR-007 · PR-010

---

## 案例 2 · 删除变动原因前置校验（CS-02 真实价值演示）

### 业务场景
某客户 HR 部门有 5 名管理员 · 人多手杂 · 多次发生"误删字典数据"事故。需求：装一道"删除前查反向引用"的校验。

### 实施前后对比

**实施前（标品）**：
```
删除 changereason id=12345 "战略调整"
   → 标品 BaseDataDeletePlugin 直接 DELETE
   → 8 条历史 haos_changescene 的 changereason 多选展示出现空白
   → t_haos_cschangereason 12 行变孤儿
   → 业务报表"按变动原因统计"出错
   → 紧急回滚（DBA 手工 INSERT 恢复）
```

**实施后（CS-02）**：
```
删除 changereason id=12345
   → ChangeReasonRefCheckOp.onAddValidators
   → ChangeReasonDeleteValidator.validate()
   → HRBaseServiceHelper("haos_changescene").isExists(
       new QFilter("changereason.fbasedataid", "=", 12345))
   → 命中 → addErrorMessage("变动原因[战略调整]已被变动场景的 changereason 字段引用·不可删除·建议改用禁用")
   → 用户看到红色提示 · 操作被拦
```

### 实施细节
- ISV 类：`com.kingdee.${ISV_FLAG}.haos.opplugin.web.ChangeReasonRefCheckOp` extends HRDataBaseOp
- 内部 Validator：`ChangeReasonDeleteValidator` extends AbstractValidator
- 同时挂 `delete` 和 `disable` 两个 opKey（业务方反馈：禁用前也想看到引用情况）
- RowKey=1（早于标品 4 个 delete 插件）

### 性能数据
- 单次 delete 校验 1 个 isExists 查询（haos_changescene.changereason）· ~3ms · 用户感知不到
- 用 `HRBaseServiceHelper.isExists` 而非 `BusinessDataServiceHelper.load` · 走 `count(1) limit 1` · 性能远优于全行加载
- 跟 haos_changescene CS-02 的 8 个 isExists（~16ms）相比 · 本场景**校验更快**（单查 vs 多查）

### 关联 CS · PR
- CS-02：核心实施
- PR-001：并列挂 · 不继承 BaseDataBuOp
- PR-002：RowKey=1 · 早于标品
- PR-007：出厂数据双重保护
- PR-010：onAddValidators 阶段（不在 afterExecute）

---

## 案例 3 · 跟 haos_changescene 双字典协作维护（业务 SOP 范式）

### 业务场景
某集团公司分 4 个事业群（消费品/工业品/金融/科技）· 每个事业群希望有自己专属的"应急变动场景"+ 对应的"应急原因"。

### 实施步骤

**Step 1**：在 haos_orgchangereason 加专属应急原因
- "消费品-应急" (number=cs_emergency · id=11001)
- "工业品-应急" (number=ind_emergency · id=11002)
- "金融-应急" (number=fin_emergency · id=11003)
- "科技-应急" (number=tech_emergency · id=11004)

**Step 2**：在 haos_changescene 加 1 条共用应急场景"应急临时调整" (id=99999)
- 编辑该场景的 changereason 多选 → 追加上述 4 个 reason id

**Step 3**：业务方在 changescene 选用这条"应急临时调整"时 · 多选 F7 能看到 4 个事业群应急原因
- t_haos_cschangereason 写入 4 行：
  - (fid=99999, fbasedataid=11001)
  - (fid=99999, fbasedataid=11002)
  - (fid=99999, fbasedataid=11003)
  - (fid=99999, fbasedataid=11004)

**Step 4**：定期维护
- HR 每月 review · 检查双字典是否同步
- 通过自定义报表查询：找 changescene 但 changereason 多选为空的 → 说明可能有数据问题

### 关键 SOP
1. 加 reason 时考虑：是否有现有 changescene 需要追加引用关系？
2. 禁用/删 reason 时考虑：CS-02 自动拦下被引用的 · 但业务方应清楚告知"为什么不能删"
3. 跟编码命名约定：`<biz>_<purpose>` 格式（如 `cs_emergency`）方便辨识

### 关联 CS
- CS-01（加字段如有需要）
- 业务级双字典协作 · 不需要写代码

---

## 案例 4 · ctrlstrategy 自定义校验（CS-03）

### 业务场景
某客户的人事流程引入"控制策略合规审计"概念：
- 选了"同分配范围"(ctrlstrategy=8) 的变动原因 · 必须落在指定 BU
- 跨 BU 选错会导致后续报表统计错误

### 实施步骤

**Step 1**：写 TdkwChangeReasonEditPlugin（参考 CS-03 代码框架）：
- propertyChanged 监听 ctrlstrategy
- 命中 ctrlstrategy="8" 时检查 org 是否在白名单
- 不在 → showTipNotification + setValue 回滚 + beginInit/endInit 防死循环

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

## 案例 5 · 子表 + ID 生成（CS-05）

### 业务场景
某客户希望给 changereason 加一张"适用业务线明细"子表 · 每行 (bizline_id, bizline_code, weight)。
业务上："战略调整"这个 reason 适用业务线 = "整体" + "事业群"两个层级 · 不同 weight。

### 实施步骤

**Step 1**：CS-05 通过 modifyMeta 加 EntryEntity `${ISV_FLAG}_bizline_entry`（含字段 ${ISV_FLAG}_bizline_id/${ISV_FLAG}_biz_code/weight）

**Step 2**：写 TdkwChangeReasonEntryAppendOp（参考 CS-05 代码框架）：
- onPreparePropertys 声明 `${ISV_FLAG}_bizline_entry` 子集合
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
- CS-05：核心实施
- PR-005：ID 生成铁律

---

## 案例 6 · 误用标品类继承的回滚案例

### 错误尝试

某新手 ISV 开发想做"删除变动原因前校验" · 直接：
```java
public class WrongChangeReasonOp extends BaseDataBuOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);  // 继承标品
        args.addValidator(new MyDeleteValidator());
    }
}
```

### 出问题点

1. 标品下次升级 · `BaseDataBuOp.onAddValidators` 方法签名加了一个参数（如换成 `onAddValidatorsV2`）
2. ISV 编译失败 · 整个客户项目部署不上
3. 紧急排查 · 才发现 PR-001 铁律：**禁止继承场景特有类**

### 正确写法（参考 CS-02）
```java
public class ChangeReasonRefCheckOp extends HRDataBaseOp {  // ✅ 通用基类
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);  // 调用 HRDataBaseOp 的 super · 安全
        args.addValidator(new ChangeReasonDeleteValidator());
    }
}
```

→ 通过【开发平台】把 ChangeReasonRefCheckOp 跟标品 BaseDataBuOp 并列挂 · 互不影响 · 标品升级也不会编译失败。

### 关联 PR
- PR-001：禁继承场景特有类 · 并列挂

---

## 案例 7 · ISV 加字段后 OP 读不到值的隐藏 BUG

### 业务场景
ISV 加了 ComboField `${ISV_FLAG}_bizimpact` · 在 ChangeReasonRefCheckOp 的 Validator 里想读这个字段做条件判断（如"高影响等级的不让删"）· 始终是 null。

### 排查发现
- 反编译实证 BaseDataBuOp 没有 onPreparePropertys 方法 · 走平台默认
- 平台默认 onPreparePropertys 只加载白名单字段 · ISV 字段 `${ISV_FLAG}_bizimpact` 不在白名单
- ISV 自己写的 ChangeReasonRefCheckOp 只重写了 onAddValidators · 没重写 onPreparePropertys
- Validator 里 `dataEntity.getString("${ISV_FLAG}_bizimpact")` 始终是 null · 即使 UI 上明明选了值

### 修复
```java
public class ChangeReasonRefCheckOp extends HRDataBaseOp {
    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("${ISV_FLAG}_bizimpact");  // ✅ 必须显式加
        // 如果有子表 · 子表也要声明
        // e.getFieldKeys().add("${ISV_FLAG}_bizline_entry");
    }

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new ChangeReasonDeleteValidator());
    }
}

// 在 Validator 里读取时 · 也要做 null 防护
String impact = dataEntity.getString("${ISV_FLAG}_bizimpact");
if (HRStringUtils.isEmpty(impact)) { ... }
```

### 关联 PR
- PR-010：OP 13 方法 · onPreparePropertys 是第 2 步 · 必须先声明
- 跟 haos_changescene 案例 7 同源（ChangeSceneSaveOp 实证 onPreparePropertys 显式声明 orgchangetype/changeoperat）

---

## 总结：5 个 CS 实战推荐路径

| 业务诉求 | 走哪个 CS |
|---|---|
| "想给变动原因加字段" | CS-01（最简单 · 改元数据）|
| "想加子表（多行明细）" | CS-05（带 ID.genLongId 实证）|
| "想拦删除避免误删" | CS-02（最重要 · 必装）|
| "想自定义字段联动校验" | CS-03（FormPlugin 层）|
| "想编码规则定制" | CS-04（不写代码 · 走平台 PR-006）|
| "想发跨模块通知" | ❌ 不要走 CS-05 BEC 反指引 · 改用调度任务 |

→ 跟 haos_changescene 6 个 CS 相比 · 本场景**少 1 个 CS**（合并了列表过滤 · 因为本场景轻量不需要）· 但**核心 5 个 CS 业务必装**。
