# 业务流转 · 行政组织职能（haos_adminorgfunction）

> **状态**: 🟢 基于反编译 2 类 + opKey 链路 + haos_orgchangereason 双胞胎双向印证
> **数据源**: `_auto_plugin_registry.md` 35 plugin · `opkeys/save.json` 链路 · `BaseDataBuOp.java` / `ListOrderCommonPlugin.java`
> **confidence**: verified

---

## 一、生命周期总图（基础资料）

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│   阶段 0: 系统预置                                                       │
│   ────────                                                              │
│   平台升级时刷新 issyspreset=true 的出厂数据（如标品默认的"管理"/"业务"/  │
│   "支持"等几条职能）· INV-AF-04 listRules formRule 拦修改                 │
│                                                                         │
│        │                                                                │
│        ▼                                                                │
│                                                                         │
│   阶段 1: 创建 (op=save)                                                  │
│   ────────                                                              │
│   用户在【行政组织维护 / 组织基础设置 / 行政组织职能】点【新增】          │
│        ↓                                                                │
│   填表（必填: name · 自动: number 走 CodeRule · 联动: ctrlstrategy）      │
│        ↓                                                                │
│   BdCtrlStrtgyShowLogicPlugin.propertyChanged                           │
│   - 改 ctrlstrategy → 联动 createorg/org/useorg 可见性                    │
│        ↓                                                                │
│   保存（save 操作执行链 8 个插件 · 见 §3.1）                              │
│        ↓                                                                │
│   t_haos_adminorgfunction 插入 + t_haos_adminorgfunction_l 多语言插入      │
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
│   haos_adminorg 通过 adminorgfunction BasedataField 字段 (单选) 引用本场景 │
│   物理列 t_haos_adminorg.fadminorgfunctionid 直存本表 id                   │
│   间接被 haos_adminorghis (共物理表) / haos_adminorgdetail (共物理表) 引用 │
│                                                                         │
│        │                                                                │
│        ▼                                                                │
│                                                                         │
│   阶段 4: 维护（op=modify）                                                │
│   ────────                                                              │
│   修改 name / simplename / description / ctrlstrategy                     │
│   ⚠ 改 ctrlstrategy 会通过 BdCtrlStrtgyShowLogicPlugin 联动组织字段       │
│   ⚠ 出厂数据（issyspreset=true）的所有字段都改不动（INV-AF-04 listRules + │
│      INV-AF-03 isvCanModify=false 双重保护）                              │
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
│   列表层 ListOrderCommonPlugin 默认排序 enable desc · 禁用项自动沉到底部   │
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
│   ❌ 标品没有"反向引用前置校验" → 删除前先查 haos_adminorg.adminorgfunction │
│      引用情况是 ISV 责任（CS-02 解决）                                     │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 二、状态机（status / enable 双轨）

`haos_adminorgfunction` 是基础资料 · 状态字段有 2 个轨道（跟 haos_orgchangereason 完全相同模式）：

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

**只有 enable=1 的数据才会出现在 haos_adminorg 的 adminorgfunction F7 选择列表里**（标品 BasedataField 默认行为）。
disable 操作会写入 `disabler` + `disabledate` 字段（系统维护 · 不可手改 · 见 03 §3.2）。

> 💡 **跟 haos_orgchangereason 配对场景对照**：状态机完全一致 · 都走平台 BaseData 标准链。

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

**对比 haos_orgchangereason save 链**：完全一致（**8 插件 · 顺序 1:1 · 第 7 位都是场景特有 BaseDataBuOp**）。
两个场景的 BaseDataBuOp 类的反编译方法体也几乎一致（都只重写 onAddValidators 注册 CtrlStrategyValidator · 23 行薄壳）。

### 3.2 BaseDataBuOp 业务行为（实证 · 跟 haos_orgchangereason 同源）

```java
// BaseDataBuOp.java:17-23
public final class BaseDataBuOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new CtrlStrategyValidator());
    }
}
```

→ 在保存链里挂 **CtrlStrategyValidator**（控制策略合规校验 · 类源码不在反编译产物里 · 平台基础资料统一逻辑 · 跟 haos_orgchangereason 共用）。

→ **本场景没有 ISV 字段读声明**（不像 haos_changescene 的 `onPreparePropertys` 显式声明 orgchangetype/changeoperat）。
→ 这意味着 ISV 加自定义字段后 · 在 OP 阶段读取必须自建插件 + onPreparePropertys 显式声明（PR-010 第 2 步 · 见 09 案例 · 10 §S1）。

---

## 四、列表展示行为（ListOrderCommonPlugin · 实证）

### 4.1 默认排序（setFilter · `ListOrderCommonPlugin.java:15-18`）

```java
public class ListOrderCommonPlugin
extends HRDataBaseList {
    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        setFilterEvent.setOrderBy("enable desc,number asc");
    }
}
```

| 业务来源 | 列表行为 |
|---|---|
| 主菜单【行政组织职能】 | 默认排序：启用项排前面（enable desc）+ 编码升序（number asc）|
| 从其他场景跳转（如 admin_org 表单 F7 选择 adminorgfunction）| 走平台 BasedataField F7 默认行为 · **F7 不走 ListOrderCommonPlugin**（F7 用平台基础资料列表渲染）|

> 📌 **关键差异**：跟 haos_orgchangereason 的 ChangeReasonListPlugin **不一样**：
> - ChangeReasonListPlugin：加 QFilter `id != 1010L` + beforeShowBill 标题透传（27 行）
> - ListOrderCommonPlugin：仅 setOrderBy（19 行 · 更轻量）

→ 本场景**列表层比 haos_orgchangereason 还简单**（19 行 vs 27 行）· 没有任何 QFilter 拦截 · 也没有 beforeShowBill。

### 4.2 没有 beforeShowBill 标题透传

不像 haos_orgchangereason 的 ChangeReasonListPlugin · 本场景的 ListOrderCommonPlugin **不实现 beforeShowBill**。

→ 双击列表行打开详情时 · 标题走平台默认行为（list 列表 caption → 详情 caption · 平台自动透传）· 没有场景特有覆盖。

---

## 五、典型业务场景落地

### 场景 A：标准创建一条新行政组织职能

1. 用户在列表点【新增】 → 打开主表单
2. 填 number（或交给 CodeRuleOp 自动生成）+ name + 设 ctrlstrategy
3. 改 ctrlstrategy 后 → BdCtrlStrtgyShowLogicPlugin 自动联动 createorg/org/useorg 可见性
4. 保存 → 走 §3.1 8 插件链 → 落 t_haos_adminorgfunction + 多语言子表
5. 默认 status=A · 用户再点【提交】+【审核】 → status=C · enable=1
6. 此后 haos_adminorg 即可在 adminorgfunction F7 选到本条

### 场景 B：导入批量职能（HIES 路径）

1. 走 `importdata_hr` opKey · 平台 HIES 流程：模板下载 → Excel 编辑 → 上传 → ImportOp
2. 进入保存链时 OperateOption 含 `importtype=...`
3. **本场景的 BaseDataBuOp 不感知 importtype**（同 haos_orgchangereason · 没有场景特有兜底联动）
4. 走标品 CtrlStrategyValidator 校验（在 onAddValidators 注册）

### 场景 C：禁用某职能（业务字典淘汰）

1. 列表选中 → 点【禁用】 → 走 `disable` opKey 链（2 个插件 · 见 `opkeys/disable.json`）
2. enable: 1→0 · 写 disabler/disabledate
3. **下游影响**：该职能在 haos_adminorg 的 adminorgfunction F7 选择框消失（标品过滤 enable=1）· 但**已经引用过它的 t_haos_adminorg.fadminorgfunctionid 数据仍然能查能展示**（外键完整性不受影响）
4. 列表层因为 `enable desc, number asc` 默认排序 · 禁用项自动沉到底部（业务可见但不容易误操作）

### 场景 D：删除某职能（极少 · ⚠ 高危）

1. 列表选中 → 点【删除】 → 走 `delete` opKey 链（4 个插件）
2. **标品没有反向引用前置校验** → 删除会成功
3. **后果**：t_haos_adminorg.fadminorgfunctionid = 被删 id 的所有行变成"游离引用"（外键指向已删记录）· F7 显示空 · admin_org 列表"职能"列空白
4. **正确做法**：先用 CS-02 加 ISV 前置校验插件 · 在 onAddValidators 阶段拦下"被引用过的不允许删"

### 场景 E：修改 issyspreset=true 的预置数据

1. 用户在列表选中某条系统预置职能 · 点【修改】打开详情
2. **平台 listRules formRule 触发**（INV-AF-04）：preCondition `issyspreset = true` 命中
3. UI 层硬拦：所有字段变只读（label 显示但不可编辑） · 用户看到"系统预置数据不可修改"提示
4. **比 haos_orgchangereason 多一层保护** · 因为 haos_orgchangereason 是 0 条 formRule · 仅靠平台 isvCanModify=false 字段级拦

> 💡 **业务铁律**：预置职能（如标品默认的"管理"/"业务"/"支持"）只能由平台升级刷新 · ISV 不要尝试改。

---

## 六、跟 haos_adminorg 的协作时序（核心配对）

```
  haos_adminorgfunction (本场景)         haos_adminorg (核心数据 · 行政组织)
  ────────────────                     ────────────────────────────
   1. 维护职能字典
       create/modify/disable
       ↓
   2. enable=1 进入下游 F7
       ↓
                                          3. 用户建/改 admin_org
                                             选 adminorgfunction F7 → 选中本表 1 条
                                             ↓
                                          4. adminorgfunction 字段 (BasedataField 单选)
                                             写入 t_haos_adminorg.fadminorgfunctionid = 本表 id
                                             ↓
                                          5. 提交→审核→enable=1（admin_org 走 HisModel 时序链）
                                             ↓
                                          6. 间接通过 admin_org 影响下游：
                                             hrpi_employee.adminorg / hbpm_position.adminorg
                                             等查询时通过 adminorg.adminorgfunction.name 关联展示
       ▲
       │
   7. 反向影响：本表 disable
      不影响已落地的 t_haos_adminorg.fadminorgfunctionid 外键
      但新建/编辑 admin_org 时 F7 选不到该项
```

---

## 七、跟 haos_orgchangereason 业务流的对比（双胞胎对照）

| 步骤 | haos_adminorgfunction（本场景）| haos_orgchangereason |
|---|---|---|
| 创建必填字段 | name | name |
| 联动机制 | ctrlstrategy → 组织字段（标品平台）| ctrlstrategy → 组织字段（标品平台）|
| onAddValidators 注册 | CtrlStrategyValidator | CtrlStrategyValidator |
| onPreparePropertys 显式声明 | 无（标品默认）| 无（标品默认）|
| beginOperationTransaction | 无（标品默认）| 无（标品默认）|
| 提交/审核流程 | 一致（A→B→C 平台 BaseData 链）| 一致 |
| 启用/禁用流程 | 一致（1↔0 平台 BaseData 链）| 一致 |
| 删除链 | 4 个插件（无反向引用校验）| 4 个插件（无反向引用校验）|
| **listRules 实抓** | **1 条**（issyspreset=true 不可修改）⭐ | 0 条 |
| 列表层差异 | setOrderBy 默认排序 | QFilter 隐藏 1010L + beforeShowBill 标题透传 |
| 下游引用类型 | BasedataField 单选（直字段查）| MulBasedataField 多选（子表查）|

→ **结论**：业务流转**几乎一致** · 都是基础资料字典的标准流程。
→ 关键差异：本场景多 1 条 listRules formRule（INV-AF-04）· 列表层只设排序不做过滤 · 下游引用是单选关系。
→ 但**反向引用校验同样必装**（CS-02）· 业务后果跟 haos_orgchangereason 一致（删本表数据后 admin_org 出现孤儿外键）。

---

## 八、跟 haos_adminorg 的"双向 SOP 协议"

业务上 "维护行政组织职能字典" 跟 "维护行政组织" 应该协同：

```
[业务方说"我们要支持新职能 X · 比如'数据科学'"]
    ↓
[判断]
    ├─ 仅作为独立职能（不绑特定组织）→ 仅本表新增
    └─ 业务上有目标组织（如新成立的"数据科学中心"）需要立即归类
         ↓
       [双层协同]
       Step 1: 本表新增 adminorgfunction "数据科学"（拿到 id=12345）
       Step 2: 提交+审核 → enable=1
       Step 3: haos_adminorg 找到目标组织"数据科学中心"
       Step 4: 编辑该组织 → adminorgfunction 字段 F7 选 12345
       Step 5: t_haos_adminorg.fadminorgfunctionid 更新为 12345（HisModel 会写新版本）
```

→ **业务建议**：职能字典维护通过【组织管理 / 行政组织维护 / 组织基础设置】统一菜单完成 · 跟 haos_adminorgtype / haos_adminorglayer 同源协同。
