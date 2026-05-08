# 业务流转 · 行政组织变动原因（haos_orgchangereason）

> **状态**: 🟢 基于反编译 2 类 + opKey 链路 + 配对场景 haos_changescene 双向推证
> **数据源**: `_auto_plugin_registry.md` 35 plugin · `opkeys/save.json` 链路 · `BaseDataBuOp.java` / `ChangeReasonListPlugin.java`
> **confidence**: verified

---

## 一、生命周期总图（基础资料）

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│   阶段 0: 系统预置                                                       │
│   ────────                                                              │
│   平台升级时刷新 issyspreset=true 的出厂数据（如 1010L 主键 · 标品默认  │
│   占位项 · 列表层强制隐藏 · 见 INV-CR-04）                               │
│                                                                         │
│        │                                                                │
│        ▼                                                                │
│                                                                         │
│   阶段 1: 创建 (op=save)                                                  │
│   ────────                                                              │
│   用户在【行政组织维护 / 组织基础设置 / 变动原因】点【新增】              │
│        ↓                                                                │
│   填表（必填: name · 自动: number 走 CodeRule · 联动: ctrlstrategy）      │
│        ↓                                                                │
│   BdCtrlStrtgyShowLogicPlugin.propertyChanged                           │
│   - 改 ctrlstrategy → 联动 createorg/org/useorg 可见性                    │
│        ↓                                                                │
│   保存（save 操作执行链 8 个插件 · 见 §3.1）                              │
│        ↓                                                                │
│   t_haos_orgchangereason 插入 + t_haos_orgchangereason_l 多语言插入        │
│                                                                         │
│        │                                                                │
│        ▼                                                                │
│                                                                         │
│   阶段 2: 提交 / 审核 (op=submit / audit · 标品基础资料默认走 BaseData 链) │
│   ────────                                                              │
│   status: A(草稿) → B(已提交) → C(已审核)                                 │
│   走 kd.bos.form.plugin.bdctrl.BaseDataSubmitPlugin/BaseDataAuditPlugin   │
│                                                                         │
│        │                                                                │
│        ▼                                                                │
│                                                                         │
│   阶段 3: 业务投入使用                                                    │
│   ────────                                                              │
│   配对场景 haos_changescene 通过 changereason 多选字段引用本场景          │
│   多对多关系子表 t_haos_cschangereason 写入 fbasedataid                   │
│   间接被 homs_orgbatchchgbill / haos_adminorgdetail 引用（通过 changescene）│
│                                                                         │
│        │                                                                │
│        ▼                                                                │
│                                                                         │
│   阶段 4: 维护（op=modify）                                                │
│   ────────                                                              │
│   修改 name / simplename / description / otclassify / ctrlstrategy        │
│   ⚠ 改 ctrlstrategy 会通过 BdCtrlStrtgyShowLogicPlugin 联动组织字段       │
│   ⚠ 出厂数据（issyspreset=true）的 number / orinumber 改不动              │
│                                                                         │
│        │                                                                │
│        ▼                                                                │
│                                                                         │
│   阶段 5: 禁用 (op=disable)                                                │
│   ────────                                                              │
│   enable: 1 → 0                                                         │
│   写入 disabler / disabledate                                             │
│   走 kd.bos.form.plugin.bdctrl.BaseDataDisablePlugin                     │
│   + HRBaseDataLogOp (写日志)                                              │
│                                                                         │
│        │                                                                │
│        ▼ (可逆)                                                          │
│                                                                         │
│   阶段 6: 启用 (op=enable)                                                │
│   ────────                                                              │
│   enable: 0 → 1                                                         │
│   走 kd.bos.form.plugin.bdctrl.BaseDataEnablePlugin                      │
│   + HRBaseDataEnableOp + HRBaseDataLogOp                                 │
│                                                                         │
│        │                                                                │
│        ▼ (尽量不要走)                                                     │
│                                                                         │
│   阶段 7: 删除 (op=delete) ⚠ 高危                                          │
│   ────────                                                              │
│   走 kd.bos.form.plugin.bdctrl.BaseDataDeletePlugin                      │
│   + CodeRuleDeleteOp (释放编码池)                                         │
│   + HRBaseDataStatusOp + HRBaseDataLogOp                                  │
│   ❌ 标品没有"反向引用前置校验" → 删除前先查 t_haos_cschangereason 引用    │
│      情况是 ISV 责任（CS-02 解决）                                         │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 二、状态机（status / enable 双轨）

`haos_orgchangereason` 是基础资料 · 状态字段有 2 个轨道（跟 haos_changescene 完全相同模式）：

### 2.1 数据状态 status (BillStatusField)

```
A (暂存) ──submit──▶ B (已提交) ──audit────▶ C (已审核)
   ▲                    │                       │
   │                    │ unsubmit              │ unaudit
   └────────────────────┴───────────────────────┘
```

走平台 BaseData 标准状态链（`BaseDataSubmitPlugin` / `BaseDataAuditPlugin` / `BaseDataUnSubmitPlugin` / `BaseDataUnAuditPlugin`）。

### 2.2 使用状态 enable (BillStatusField)

```
1 (启用) ──disable──▶ 0 (禁用)
   ▲                    │
   │                    │ enable
   └────────────────────┘
```

**只有 enable=1 的数据才会出现在 haos_changescene 的 changereason 多选 F7 选择列表里**（标品 BaseDataField 默认行为）。
disable 操作会写入 `disabler` + `disabledate` 字段（系统维护 · 不可手改 · 见 03 §3.2）。

---

## 三、save 操作执行链（8 个插件 · 实证 `opkeys/save.json`）

### 3.1 标品执行顺序

| 顺序 | 插件类 | 类型 | 角色 |
|---|---|---|---|
| 1 | `kd.bos.business.plugin.CodeRuleOp` | bos | 编码规则（onAddValidators 注册 numberValidator）|
| 2 | `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin` | bos | 平台基础资料保存 |
| 3 | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | bos | 基础资料版本管理（写 name 历史）|
| 4 | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | hbp | HR 基础资料状态校验（onAddValidators + beforeExecute）|
| 5 | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | hbp | HR 基础资料操作日志（before + after）|
| 6 | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | hbp | HR 基础资料启用态管理（控制 enable 字段）|
| 7 | `kd.hr.haos.opplugin.web.BaseDataBuOp` | **haos 场景特有** ⭐ | onAddValidators 注册 CtrlStrategyValidator |
| 8 | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | hbp | HR 基础资料原始值记录（变更前后对比）|

**对比 haos_changescene save 链**：
- haos_changescene 第 8 位是 `ChangeSceneSaveOp`（57 行 · onAddValidators + onPreparePropertys + beginOperationTransaction 三方法）
- haos_orgchangereason 第 7 位是 `BaseDataBuOp`（23 行 · 仅 onAddValidators 一个方法）

→ 本场景**比 haos_changescene 还薄** · 没有 onPreparePropertys 兜底 · 也没有 beginOperationTransaction 业务逻辑。

### 3.2 BaseDataBuOp 业务行为（实证）

```java
// BaseDataBuOp.java:19-22
public final class BaseDataBuOp extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new CtrlStrategyValidator());
    }
}
```

→ 在保存链里挂 **CtrlStrategyValidator**（控制策略合规校验 · 类源码不在反编译产物里 · 平台基础资料统一逻辑）。

→ **本场景没有 ISV 字段读声明**（不像 haos_changescene 的 `onPreparePropertys` 显式声明 orgchangetype/changeoperat）。
→ 这意味着 ISV 加自定义字段后 · 在 OP 阶段读取必须自建插件 + onPreparePropertys 显式声明（PR-010 第 2 步 · 见 09 案例 7）。

---

## 四、列表展示行为（ChangeReasonListPlugin · 实证）

### 4.1 默认过滤（setFilter · `ChangeReasonListPlugin.java:19-22`）

```java
public void setFilter(SetFilterEvent setFilterEvent) {
    super.setFilter(setFilterEvent);
    setFilterEvent.getQFilters().add(new QFilter("id", "!=", (Object)1010L));
}
```

| 业务来源 | 过滤行为 |
|---|---|
| 主菜单【变动原因】 | 强制排除 id=1010L（INV-CR-04）|
| 从其他场景跳转 | 同上 · 不感知 customParam（不像 ChangeSceneListPlugin 的 otclassify 切换）|

→ 本场景**列表过滤极简** · 仅 1 行硬编码 · ISV 自建过滤如果需要 customParam 切换 · 必须自己挂插件。

### 4.2 标题透传（beforeShowBill · `ChangeReasonListPlugin.java:24-26`）

```java
public void beforeShowBill(BeforeShowBillFormEvent e) {
    e.getParameter().setCaption(this.getView().getFormShowParameter().getCaption());
}
```

→ 双击列表行打开详情 · 标题继承列表传过来的 caption（多场景同表单复用时支持自定义标题）。
→ 跟 ChangeSceneListPlugin.beforeShowBill 是相同模式（haos 域同源）。

---

## 五、典型业务场景落地

### 场景 A：标准创建一条新变动原因

1. 用户在列表点【新增】 → 打开主表单
2. 填 number（或交给 CodeRuleOp 自动生成）+ name + 选 otclassify + 设 ctrlstrategy
3. 改 ctrlstrategy 后 → BdCtrlStrtgyShowLogicPlugin 自动联动 createorg/org/useorg 可见性
4. 保存 → 走 §3.1 8 插件链 → 落 t_haos_orgchangereason + 多语言子表
5. 默认 status=A · 用户再点【提交】+【审核】 → status=C · enable=1
6. 此后 haos_changescene 即可在 changereason 多选 F7 选到本条

### 场景 B：导入批量变动原因（HIES 路径）

1. 走 `importdata_hr` opKey · 平台 HIES 流程：模板下载 → Excel 编辑 → 上传 → ImportOp
2. 进入保存链时 OperateOption 含 `importtype=...`
3. **本场景的 BaseDataBuOp 不感知 importtype**（不像 haos_changescene 的 ChangeSceneSaveOp 在 importtype 时做兜底联动）
4. 走标品 CtrlStrategyValidator 校验（在 onAddValidators 注册）

### 场景 C：禁用某变动原因（业务字典淘汰）

1. 列表选中 → 点【禁用】 → 走 `disable` opKey 链（2 个插件 · 见 `opkeys/disable.json`）
2. enable: 1→0 · 写 disabler/disabledate
3. **下游影响**：该原因在 haos_changescene 的 changereason F7 选择框消失（标品过滤 enable=1）· 但**已经引用过它的 t_haos_cschangereason 多选关系数据仍然能查能展示**（外键完整性不受影响）

### 场景 D：删除某变动原因（极少 · ⚠ 高危）

1. 列表选中 → 点【删除】 → 走 `delete` opKey 链（4 个插件）
2. **标品没有反向引用前置校验** → 删除会成功
3. **后果**：t_haos_cschangereason 的 fbasedataid=被删id 的行变成"游离引用"（id 还在但目标记录已删）· F7 显示空 · changescene 多选展示标签为空
4. **正确做法**：先用 CS-02 加 ISV 前置校验插件 · 在 onAddValidators 阶段拦下"被引用过的不允许删"

### 场景 E：跟 haos_changescene 配对维护

1. HR 加新变动场景"组织撤并" · 选 changereason 多选 → 选"组织优化" + "战略调整"
2. 系统在 t_haos_cschangereason 写入 2 行（fbasedataid 分别 = 这两个原因的 id）
3. 用户后续要禁用"战略调整"原因 → CS-02 应提示"已被 N 条变动场景引用 · 建议用 disable"

---

## 六、跟 haos_changescene 双字典的协作时序

```
  haos_orgchangereason          haos_changescene
  ────────────────              ──────────────────
   1. 维护原因字典
       create/modify/disable
       ↓
   2. enable=1 进入下游 F7
       ↓
                                3. 用户建/改 changescene
                                   选 changereason 多选 F7 → 选中本表多条
                                   ↓
                                4. changereason 子表 t_haos_cschangereason
                                   写入 fbasedataid 引用本表 id
                                   ↓
                                5. 提交→审核→enable=1
                                   ↓
                                6. 下游 homs_orgbatchchgbill 通过 changescene
                                   间接引用本表（数据级联：
                                   reason → changescene → orgbatchchgbill）
       ▲
       │
   7. 反向影响：本表 disable
      不影响已落地的 t_haos_cschangereason 数据
      但新建 changescene 时多选不到该项
```

---

## 七、跟 haos_changescene 业务流的对比

| 步骤 | haos_orgchangereason | haos_changescene |
|---|---|---|
| 创建必填字段 | name | orgchangetype（联动 changeoperat）|
| 联动机制 | ctrlstrategy → 组织字段（标品平台）| orgchangetype → changeoperat（场景特有 ChangeSceneServiceHelper）|
| onAddValidators 注册 | CtrlStrategyValidator | ChangeSceneImportValidator |
| onPreparePropertys 声明 | 无（标品默认）| orgchangetype + changeoperat |
| beginOperationTransaction | 无（标品默认）| 导入路径兜底 changeoperat |
| 提交/审核流程 | 一致（A→B→C 平台 BaseData 链）| 一致 |
| 启用/禁用流程 | 一致（1↔0 平台 BaseData 链）| 一致 |
| 删除链 | 4 个插件（无反向引用校验）| 4 个插件（无反向引用校验）|

→ **结论**：本场景**业务流转更简单** · 没有联动子表数据的复杂度 · ISV 扩展成本更低。
→ 但**反向引用校验同样必装**（CS-02）· 业务后果跟 haos_changescene 一致。

---

## 八、跟 haos_changescene 的"双向 SOP 协议"

业务上 "维护变动原因" 跟 "维护变动场景" 应该协同：

```
[业务方说"我们要支持新业务原因 A"]
    ↓
[判断]
    ├─ 仅作为独立原因（不绑特定场景）→ 仅本表新增
    └─ 业务上常跟某变动场景"绑使用"（如疫情期间临时调整 → "应急" 原因）
         ↓
       [双字典协同]
       Step 1: 本表新增 changereason "应急"（拿到 id=12345）
       Step 2: haos_changescene 找到目标场景"应急临时调整"（如 id=99999）
       Step 3: 编辑该场景 → changereason 多选追加 12345
       Step 4: t_haos_cschangereason 写入 (fid=99999, fbasedataid=12345)
```

→ **业务建议**：双字典维护通过【组织管理 / 行政组织维护 / 组织基础设置】统一菜单完成 · 不要分散到多个角色。
