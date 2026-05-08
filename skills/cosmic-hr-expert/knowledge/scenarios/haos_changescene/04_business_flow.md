# 业务流转 · 行政组织变动场景（haos_changescene）

> **状态**: 🟢 基于反编译 3 类 + opKey 链路 + 下游 homs_orgbatchchgbill 双向推证
> **数据源**: `_auto_plugin_registry.md` 36 plugin · `opkeys/save.json` 链路 · `ChangeSceneEditPlugin.java` / `ChangeSceneSaveOp.java` / `ChangeSceneListPlugin.java`
> **confidence**: verified

---

## 一、生命周期总图（基础资料）

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│   阶段 0: 系统预置                                                       │
│   ────────                                                              │
│   平台升级时刷新 issyspreset=true 的出厂数据（如 1010/1070/1100_S 主键）  │
│                                                                         │
│        │                                                                │
│        ▼                                                                │
│                                                                         │
│   阶段 1: 创建 (op=save)                                                  │
│   ────────                                                              │
│   用户在【行政组织维护 / 组织基础设置 / 变动场景】点【新增】              │
│        ↓                                                                │
│   填表（必填: orgchangetype）                                             │
│        ↓                                                                │
│   ChangeSceneEditPlugin.propertyChanged                                 │
│   - 改 orgchangetype → 联动反填 changeoperat                              │
│        ↓                                                                │
│   保存（save 操作执行链 8 个插件 · 见 §3.1）                              │
│        ↓                                                                │
│   t_haos_changescene 插入 + t_haos_changescene_l 多语言插入               │
│   + 2 个 MulBasedata 子表 (cschangereason / cschangeoperat) 插入           │
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
│   下游 homs_orgbatchchgbill（7 entry）通过 BasedataField 引用本场景      │
│   下游 haos_adminorgdetail.changescene 必填字段引用本场景                 │
│                                                                         │
│        │                                                                │
│        ▼                                                                │
│                                                                         │
│   阶段 4: 维护（op=modify）                                                │
│   ────────                                                              │
│   修改 name / simplename / description / changereason 等可改字段          │
│   ⚠ 改 orgchangetype 会通过 ChangeSceneEditPlugin 自动联动 changeoperat   │
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
│   ❌ 标品没有"反向引用前置校验" → 改字典前/删除前先查 homs_orgbatchchgbill  │
│      引用情况是 ISV 责任（CS-02 解决）                                     │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 二、状态机（status / enable 双轨）

`haos_changescene` 是基础资料 · 状态字段有 2 个轨道：

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

**只有 enable=1 的数据才会出现在下游 F7 选择列表里**（标品 BaseDataField 默认行为）。
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
| 7 | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | hbp | HR 基础资料原始值记录（变更前后对比）|
| 8 | `kd.hr.haos.opplugin.web.ChangeSceneSaveOp` | **haos 场景特有** ⭐ | onAddValidators 注册 ChangeSceneImportValidator + beginOperationTransaction 兜底联动 |

### 3.2 ChangeSceneSaveOp 业务行为（实证）

```java
// ChangeSceneSaveOp.java:31-34
public void onAddValidators(AddValidatorsEventArgs args) {
    super.onAddValidators(args);
    args.addValidator((AbstractValidator)new ChangeSceneImportValidator());
}
```
→ 在保存链里挂 **ChangeSceneImportValidator**（导入场景的字段格式校验 · 类源码不可见但参考 import 链）。

```java
// ChangeSceneSaveOp.java:36-39
public void onPreparePropertys(PreparePropertysEventArgs e) {
    e.getFieldKeys().add("orgchangetype");
    e.getFieldKeys().add("changeoperat");
}
```
→ 声明 OP 阶段需要读 `orgchangetype` 和 `changeoperat`（两个字段不在默认字段集 · OpenAPI 14 必须主动声明 · 否则在 `beginOperationTransaction` 里 getDynamicObjectCollection 会拿 null）。

```java
// ChangeSceneSaveOp.java:41-56
public void beginOperationTransaction(BeginOperationTransactionArgs e) {
    if (HRStringUtils.isEmpty(this.getOption().getVariables().get("importtype"))) {
        return;  // 仅在导入路径触发兜底逻辑
    }
    for (DynamicObject dataEntity : e.getDataEntities()) {
        long changeTypeId = dataEntity.getLong("orgchangetype.id");
        Long changeOperateId = ChangeSceneServiceHelper.getChangeOperate(changeTypeId);
        DynamicObjectCollection collection = dataEntity.getDynamicObjectCollection("changeoperat");
        collection.clear();
        if (changeOperateId == null) continue;
        DynamicObject changeOperateObj = new DynamicObject(collection.getDynamicObjectType());
        changeOperateObj.set("fbasedataid", (Object)changeOperateId);
        collection.add((Object)changeOperateObj);
    }
}
```
→ **导入路径**（OperateOption.importtype 非空）的兜底：清空 changeoperat 子表 · 按 orgchangetype 重新算并插一条。
→ **普通保存路径**（importtype=null）走表单层 `ChangeSceneEditPlugin.propertyChanged` 已联动 · 此处直接 return。

---

## 四、列表展示行为（ChangeSceneListPlugin · 实证）

### 4.1 默认过滤（setFilter · `ChangeSceneListPlugin.java:23-32`）

| 业务来源 | 进入列表的 customParam | 过滤行为 |
|---|---|---|
| 主菜单【变动场景】 | `otclassify=null` | 默认按 1010L (ADMINISTRATIVE) 过滤 + 强制排除 id=1070L |
| 从其他场景跳转（如某 BU 配置）| `otclassify=<某个 id>` | 按指定 otclassify 过滤 + 强制排除 id=1070L |

### 4.2 标题透传（beforeShowBill · `ChangeSceneListPlugin.java:34-36`）

```java
public void beforeShowBill(BeforeShowBillFormEvent e) {
    e.getParameter().setCaption(this.getView().getFormShowParameter().getCaption());
}
```
→ 双击列表行打开详情 · 标题继承列表传过来的 caption（多场景同表单复用时支持自定义标题）。

### 4.3 搜索框过滤（filterColumnSetFilter · `ChangeSceneListPlugin.java:38-44`）

只有当用户在搜索框选 `orgchangetype.name` 字段时才追加 `orinumber != "1100_S"` 过滤（隐藏子集团变动场景）。

---

## 五、典型业务场景落地

### 场景 A：标准创建一条新变动场景

1. 用户在列表点【新增】 → 打开主表单
2. 填 number（或交给 CodeRuleOp 自动生成）+ name + 必填 orgchangetype
3. 选 orgchangetype 后 → ChangeSceneEditPlugin 自动联动 changeoperat
4. 可补 changereason（多选 · 业务上常带"战略调整"等）
5. 保存 → 走 §3.1 8 插件链 → 落 t_haos_changescene + 2 个 MulBasedata 子表
6. 默认 status=A · 用户再点【提交】+【审核】 → status=C · enable=1
7. 此后下游 homs_orgbatchchgbill 即可通过 F7 选到本条

### 场景 B：导入批量变动场景（HIES 路径）

1. 走 `importdata_hr` opKey · 平台 HIES 流程：模板下载 → Excel 编辑 → 上传 → ImportOp
2. 进入保存链时 OperateOption 含 `importtype=...`
3. ChangeSceneSaveOp.beginOperationTransaction 触发兜底逻辑：
   - 即使用户在 Excel 里没填 changeoperat · 也会按 orgchangetype 自动算并填上
4. 走标品 ChangeSceneImportValidator 校验（在 onAddValidators 注册）

### 场景 C：禁用某变动场景（业务字典淘汰）

1. 列表选中 → 点【禁用】 → 走 `disable` opKey 链（2 个插件 · 见 `opkeys/disable.json`）
2. enable: 1→0 · 写 disabler/disabledate
3. **下游影响**：该场景在 F7 选择框消失（标品过滤 enable=1）· 但**已经引用过它的 homs_orgbatchchgbill 历史申请单仍然能查能展示**（外键完整性不受影响）

### 场景 D：删除某变动场景（极少 · ⚠ 高危）

1. 列表选中 → 点【删除】 → 走 `delete` opKey 链（4 个插件）
2. **标品没有反向引用前置校验** → 删除会成功
3. **后果**：homs_orgbatchchgbill 历史申请单的 changescene 字段变成"游离引用"（id 还在但目标记录已删）· F7 显示空 · 列表展示 changescene.name 为空
4. **正确做法**：先用 CS-02 加 ISV 前置校验插件 · 在 onAddValidators 阶段拦下"被引用过的不允许删"

---

## 六、跟下游 homs_orgbatchchgbill 的协作时序

```
  haos_changescene             homs_orgbatchchgbill
  ────────────────             ──────────────────────
   1. 维护字典                       
       create/modify/disable
       ↓
   2. enable=1 进入 F7 选择列表
       ↓                          
                                3. 用户新建调整单 → 进 7 个 entry 之一
                                   选择 changescene F7 → 选中本场景某条
                                   ↓
                                4. entry 字段 changescene 写入引用 id
                                   ↓
                                5. 提交→审核→生效（OrgBatchChgBillEffectOp）
                                   ↓
                                6. afterExecuteOperationTransaction
                                   异步派单到 haos_adminorg / haos_adminorghis
                                   写入 changescene 字段（FK to haos_changescene）
       ▲                               
       │                               
   7. 反向影响：haos_changescene 的 disable
      不影响已落地的历史 changescene 引用
      但新的 homs 申请单选不到该项
```

---

## 七、跟 haos_orgchangereason 的业务配对

业务上 "变动场景 + 变动原因" 通常一起用：
- 变动场景="拆分组织"（haos_changescene）
- 变动原因="战略调整"（haos_orgchangereason · 多选）

`haos_changescene.changereason` 字段直接存储多个 `haos_orgchangereason` 引用（MulBasedataField）· 前端选择是平级独立。

**这两个基础资料维护是耦合的** — 加一类新变动场景 (`战略性裁员`) 时 · 通常也要加一类对应的变动原因。CS-01 的字段扩展模式 · 可以在 changereason / changeoperat 之外再加自建分类字段。
