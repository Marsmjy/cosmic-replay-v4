# 能力边界 · 管理层级（haos_adminorglayer）

> **状态**: 🟢 基于反编译 2 类 + 49 opKey + 标品插件 35 plugin + listRules 1 条 实证
> **confidence**: verified
> **业务定位**：基础资料字典（不是单据 · 不是时序）· 与 haos_adminorgfunction 三胞胎模式 · 直接被 haos_adminorg 引用

---

## 一、10 条平台级硬规则（必记 · 跨场景适用）

| # | 规则 | 影响 |
|---|---|---|
| 1 | `number` 字段创建后**不可修改**（出厂数据严控）| 下游引用绑定 number |
| 2 | 字段 key ≤ **24** 字符 | 数据库列名上限 |
| 3 | 实体 key ≤ **36** 字符 | 数据库表名上限 |
| 4 | 表名 ≤ **25** 字符 | 数据库硬上限 |
| 5 | 系统计算字段禁止手改 | `disabler` / `disabledate` / `creator` / `modifier` |
| 6 | `errorCode="0"` ≠ 成功 | 必须二次验证（getFormSchema / list_rules）|
| 7 | `op` 枚举**只 4 值** | `add` / `modify` / `remove` / `move` |
| 8 | `actionType` / `fieldKey` 必须 **PascalCase** | 小写静默忽略 |
| 9 | `updateOperation.plugins` 是**全量替换** | 必须先 get 再 append |
| 10 | `registerPlugin.targetType` 必须**大写枚举** | 5 值: `BILL_FORM`/`LIST_FORM`/`MOBILE_BILL_FORM`/`OPERATION`/`EVENT` |

---

## 二、✅ 标品原生支持（能干啥）

### 列表展示 · `ListOrderCommonPlugin` 实证（薄壳 · 19 行）
- ✅ 默认排序 `enable desc, number asc` · 启用项排前面 + 编码升序（INV-AL-07 · 见 `ListOrderCommonPlugin.java:15-18`）
- ✅ 走平台 BaseData 默认列表过滤（按 enable=1 + ctrlstrategy 控制策略）
- ✅ 支持 HIES 列表导入导出（6 个 HIES opKey · 标品自动注入）
- ✅ **不加 QFilter**（跟 haos_orgchangereason 的 ChangeReasonListPlugin 区别 · 那个加 `id != 1010L`）
- ✅ **没 beforeShowBill 标题透传**（跟 haos_orgchangereason 区别 · 19 行 vs 27 行 · 本场景最轻量）

### 新增/编辑 · 标品 HRBaseDataTplEdit 模板
- ✅ `name` 必填校验（标品 BasedataField 平台层 · 多语言）
- ✅ `number` 由 CodeRule 自动生成（如配置 · 否则用户填）
- ✅ `description` / `simplename` 多语言字段
- ✅ 多组织字段（createorg / org / useorg / srccreateorg）走 OrgField 平台默认
- ✅ ctrlstrategy 控制策略（基础资料统一 · 走 BdCtrlStrtgyShowLogicPlugin）
- ✅ **listRules formRule 拦预置数据修改**（INV-AL-04 · 系统预置数据进入编辑视图所有字段变只读）⭐ 本场景特有

### 保存 · `BaseDataBuOp` 实证（薄壳 · 23 行）
- ✅ 注册 `CtrlStrategyValidator`（控制策略合规校验 · `BaseDataBuOp.java:17-23`）
- ✅ 走平台 8 插件链（CodeRuleOp + BaseDataSavePlugin + BdVersionSaveServicePlugin + 3 个 HRBaseData* + BaseDataBuOp + HRBaseOriginalOp）
- ✅ 编码规则可在【编码规则基础资料】配置（PR-006）

### 状态流转
- ✅ status: A→B→C（save→submit→audit）走平台 BaseData 链（`BaseDataSubmitPlugin` / `BaseDataAuditPlugin`）
- ✅ enable: 1↔0（disable / enable）走平台 BaseData 链
- ✅ 禁用自动写 disabler / disabledate

### HIES 导入导出（6 个 opKey · 标品自动注入）
- ✅ `importdata_hr` / `show_import_record_hr`
- ✅ `export_from_list_hr` / `export_from_impttpl_hr` / `export_from_expttpl_hr`
- ✅ `show_export_record_hr`

### 平台基础资料标准能力
- ✅ 启用 / 禁用 / 提交 / 审核 / 反提交 / 反审核 / 复制 / 改名（namehistory）
- ✅ 数据控制策略（BdCtrl* 系列插件 · ctrlstrategy 字段联动可见组织）
- ✅ 名称版本（BdVersion* · t_haos_adminorglayer_l 多语言版本）
- ✅ 分配 / 取消分配（assign/unassign · 走 ctrlstrategy=8 同分配范围）

---

## 三、❌ 标品不支持（需要定制 · 必须 ISV 实现）

### 业务规则缺口
- ❌ **删除前的反向引用校验**（INV-AL-06 缺口 · CS-02 解决）
  - 标品 delete 链 4 个插件都不查 `t_haos_adminorg.fadminorglayerid`（admin_org 的 adminorglayer 字段）
  - 这是基础资料场景的常见盲点 · 必须 ISV 加 · 单选关系 · 直字段查（跟 adminorgfunction CS-02 同模式）
- ❌ **禁用前的下游使用情况提醒**（同上 · CS-02 同源解决）
  - 禁用某条管理层级前 · 没人告诉用户"还有 N 个行政组织选用了这个层级"
- ❌ **基础资料变更通知下游**（CS-04 反指引 · 见后）
  - 标品没在 afterExecuteOperationTransaction 阶段发 BEC（grep 0 命中实证）
  - ISV 想做"修改 adminorglayer name 后下游通知" → 自建 BEC 发布方 · 但**强烈不推荐**

### 字段缺口
- ❌ 没有"层级分类" / "适用规模" / "层级深度"等业务标签（业务有需要 → CS-01 加 ISV 字段）
- ❌ 跟 admin_org 是 BasedataField **单选**关系 · 没有"层级与组织规模对应关系"校验

### 运行时诊断缺口
- ❌ 没有"该管理层级被多少个组织使用"统计视图（需要 ISV 报表）
- ❌ 没有"层级变更历史"（本表是 BaseFormModel · 非时序 · 不记变更历史）

---

## 四、BEC 情况（标品 grep 0 实证）

```bash
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_adminorglayer/
# 0 处命中
```

**结论**：标品**不发任何 BEC 事件**（跟 haos_adminorgfunction / haos_orgchangereason 完全一致）。ISV 做 BEC 订阅方无意义（标品没发）；做 BEC 发布方需自建但**基础资料场景不推荐**。

---

## 五、场景边界图

```
┌─────────────────────────────────────────────────────┐
│  haos_adminorglayer（管理层级字典 · BaseFormModel）    │
│  27 字段 · 1 实体 · 1 formRule · 2 反编译类           │
│                                                     │
│  能干：                                              │
│    新增 / 编辑 / 删除 / 启用 / 禁用 / 提交 / 审核     │
│    HIES 导入导出 / 编码规则配置                       │
│    列表默认排序（enable desc, number asc）            │
│                                                     │
│  不能干（标品不提供）：                               │
│    删除/禁用前反向引用校验（→ CS-02 补）              │
│    层级使用统计视图（→ 自建报表）                      │
│    BEC 事件发布（grep 0 · 无）                        │
└───────────────┬────────────────────────────────────┘
                │ BasedataField 单选 · 物理列 fadminorglayerid
                ▼
┌─────────────────────────────────────────────────────┐
│  haos_adminorg（行政组织 · HisModel 时序）             │
│  adminorglayer 字段 单选 引用本表 id                  │
│  反向查：QFilter("adminorglayer","=",id)              │
│          .and(new QFilter("iscurrentversion","=",true))│
└─────────────────────────────────────────────────────┘
```

---

## 六、三胞胎对照速查（haos_adminorglayer / haos_adminorgfunction / haos_orgchangereason）

| 维度 | haos_adminorglayer（本场景）| haos_adminorgfunction | haos_orgchangereason |
|---|---|---|---|
| 业务含义 | 管理层级（哈佛式/扁平式/直线式）| 行政组织职能 | 变动原因 |
| 下游引用 | adminorg.adminorglayer（单选）| adminorg.adminorgfunction（单选）| changescene.changereason（多选）|
| 反向查询方式 | 直字段 + iscurrentversion | 直字段 + iscurrentversion | 子表 `.fbasedataid` |
| 列表行为 | setOrderBy only · 同 adminorgfunction | setOrderBy only | setFilter id != 1010L |
| 标品 formRule | 1 条（issyspreset 拦修改）⭐ | 1 条（同）| 0 条 |
| BEC | grep 0 | grep 0 | grep 0 |
| 反编译类 | ListOrderCommonPlugin + BaseDataBuOp | 同 | ChangeReasonListPlugin + BaseDataBuOp |
| 类行数 | 42 行（19+23）| 42 行（19+23）| 50 行（27+23）|
