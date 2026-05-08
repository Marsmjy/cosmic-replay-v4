# 业务流程 · 组织调整申请单

> **状态**: 🟢 基于反编译 8 类 Java + rules_chain 73 opKey 全量梳理
> **confidence**: verified
> **数据源**: `OrgBatchBillSaveOp.java` / `OrgBatchBillSubmitOp.java` / `OrgBatchBillSubmitAndEffectiveOp.java` / `OrgBatchChgBillEffectOp.java` / `AdminOrgBatchBreakupOp.java` / `AdminChangeMsgService.java` / `AdminOrgBatchBillPlugin.java` / `AdminOrgBatchBillListPlugin.java`

---

## 一、单据生命周期总览图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                             │
│   ┌─────────┐    save     ┌────────┐  submit   ┌─────────┐  audit ┌────────┐│
│   │  新建    │───────────► │ 暂存 A │ ─────────►│ 待审 B  │───────► │ 已生效C ││
│   │  EDIT   │             └────────┘           └────────┘         └────────┘│
│   │  (无id) │                  │                    │                       │
│   └─────────┘                  │                    │ submiteffect          │
│        │                       │                    └─────────┐             │
│        │ save_no_log           │ submiteffect                 ▼             │
│        ▼                       └────────────► 跳过审批直接生效 C             │
│  无校验保存（FormPlugin                                                       │
│  AdminOrgBatchBillPlugin                                                    │
│  L420 内部触发）                                                              │
│                                                                             │
│   discard_row 列表                  breakup 终止                              │
│        │                              │                                     │
│        ▼                              ▼                                     │
│   ┌────────┐                    ┌────────┐                                  │
│   │ 废弃 F │                    │ 终止 G │                                   │
│   └────────┘                    └────────┘                                  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 二、5 状态全枚举（来自 `AuditStatusEnum` 反编译实证）

| billstatus | 名称 | 触发动作 | 可执行操作 |
|---|---|---|---|
| `A` | 暂存 | save / save_no_log | edit, submit, submiteffect, breakup, delete_rows, edit_add 等所有编辑操作 |
| `B` | 待审 | submit | audit（生效）, unsubmit（撤回）, breakup（终止）, edit（仅 wftask 审批人在审批界面可改） |
| `C` | 已生效 | audit / submiteffect | breakup（终止已生效组织） · view（查看） |
| `F` | 废弃 | discard_row（列表多选废弃） | view |
| `G` | 终止 | breakup | view |

来源：
- `AuditStatusEnum` import 见 `AdminOrgBatchBillPlugin.java:206`
- 状态写入实证：
  - `OrgBatchBillSubmitOp.java:36`：`dynamicObject.set("billstatus", (Object)"B");`（提交时写 B）
  - `AdminOrgBatchBillListPlugin.java:358`：`billDy.set("billstatus", (Object)"F");`（列表废弃写 F）
  - `OrgBatchChgBillEffectOp.java`（生效状态由 audit 操作链推动 · 标品 wf 框架写 C）

⚠️ **重要**：`A`/`G` 这两个状态在 `AdminOrgBatchBillPlugin.java:448` 实证只在导入操作可用：
```java
if (dynamicObjects.length > 0 && !Sets.newHashSet((Object[])new String[]{"A", "G"}).contains(dynamicObjects[0].getString("billstatus"))) {
    // 提示：该状态的单据不能导入
}
```

---

## 三、保存（save）执行链 · 完整时序

来自反编译 `OrgBatchBillSaveOp.java`（63 行）+ `AdminOrgBatchBillPlugin.java:454` 联动：

```
[FormPlugin · AdminOrgBatchBillPlugin]
  │
  ▼
beforeDoOperation (L428)
  │ ├── 若 save · 设置 OperateOption "ishasright/skipCheckDataPermission"（无 entry 时）
  │ └── 调 AdminOrgBatchChgHelper.setAdminorgBoIdToEntryEntity(view)
  │     （把 7 个 entry 的 adminorg 字段同步反向 boid · 防 boid/id 错乱 · PR-009）
  ▼
[OP · OrgBatchBillSaveOp（HRCoreBaseBillOp 子类）]
  │
  ▼
onAddValidators (L39-44)
  │ ├── e.addValidator(new OrgBatchBillSaveAndSubmitValidator())   # 7 entry 通用校验
  │ └── e.addValidator(new AdminOrgBillSaveValidator())             # 行政组织域规则校验
  ▼
beforeExecuteOperationTransaction (HRCoreBaseBillOp 父类继承 · 框架默认逻辑)
  ▼
beginOperationTransaction (L46-53)
  │ └── OrgBatchBillSaveHelper.getInstance().orgBatchBillSave(dataEntity);
  │     （写入主表 + 7 entry · 唯一一处写所有分录的入口）
  ▼
endOperationTransaction (L55-62)
  │ └── 删除 homs_batchorgentity 中 creator=0 + billid=本单 的脏数据
  │     （清理"未关联到任何 creator 的孤儿分录" · 旧版 import 残留）
  ▼
[FormPlugin · afterDoOperation - AdminOrgBatchBillPlugin L482]
  │ ├── 若 save · setDataChanged(false)
  │ └── 调 AdminOrgBatchChgHelper.setTabPageTextAndVisible(...)
  │     （刷新 7 个 tab 页签的"分录条数"显示）
```

**关键**：`save` 操作**不改** `billstatus`（保持 A 暂存）· 只是把数据落库 + 清脏数据。

---

## 四、提交（submit）执行链 · 走审批

来自 `OrgBatchBillSubmitOp.java`（56 行）+ `AdminOrgBatchBillPlugin.java:454-462` 联动：

```
[FormPlugin · AdminOrgBatchBillPlugin · beforeDoOperation L454]
  │
  │ if (operateKey == "submit" || "submiteffect"):
  │ ├── 1. 调 AdminOrgBatchChgHelper.setAdminorgBoIdToEntryEntity(view)
  │ │     （同 save · adminorg.boid 同步）
  │ ├── 2. ⭐ 调 this.executeSaveOperation("submit", option) 内部触发 save_no_log
  │ │     （L420 · 把当前未保存的数据先 silent save · 不进操作日志 · 防止"提交但未保存"）
  │ │     若 save_no_log 失败 · args.setCancel(true) 拦截提交
  │ └── 3. option.setVariableValue("ignoreValidation", "true")
  │       （save 已校验过 · 后续提交链 OP 跳过重复校验）
  ▼
[OP · OrgBatchBillSubmitOp]
  │
  ▼
beginOperationTransaction (L32-45)
  │ ├── 1. 写 billstatus = "B"（待审）
  │ ├── 2. 加载所有 entry：HRBaseServiceHelper("homs_batchorgentity").loadDynamicObjectArray
  │ ├── 3. 对每个 entry 取 adminorg.boid 列表
  │ ├── 4. AdminOrgChgBillSaveService.getInstance() 三步走：
  │ │     - getOrgRelateVersionInfo(orgIdList)        # 拿旧关联版本 map
  │ │     - getOrgBeforeChangeVersionBasicInfo(orgIdList) # 拿旧基础信息 map
  │ │     - setBeforeAndAfterChgVersionId(datas, null, oldOrgRelateVersionMap, oldOrgBasicInfo)
  │ │       （给每个 entry 写"变更前 vid" / "变更后 vid（未生效暂为 null）"）
  │ └── 5. 持久化 entry：BATCHORGENTITY_HELPER.update(datas)
  ▼
afterExecuteOperationTransaction (L47-55)
  │ ├── if operationKey == "submit":
  │ │   ├── 取主表 effdt
  │ │   ├── 加载所有 entry
  │ │   └── 给所有 entry 写 bsed = effdt
  │ │       （bsed 是 entry 字段 · 不是单据字段 · 控制下游版本生效起始日 · 见 OrgBatchBillSubmitOp.java:53）
  ▼
[平台 wf 框架接管]
  │
  └── 工作流引擎根据【组织调整申请】流程模板分发审批任务
      （wftask 节点 · 审批人在工作中心打开本单审核）
```

**走审批后**：
- `auditstatus` 由 wf 引擎更新（`Submitted` → `InProgress` → `Approved`/`Rejected`）
- 审批通过后 wf 自动触发 `audit` opKey · 进入 `OrgBatchChgBillEffectOp` 真生效逻辑（见下节）
- 拒绝则单据回到 A · 由发起人重新编辑

---

## 五、提交即生效（submiteffect）· 跳过审批直接落地

来自 `OrgBatchBillSubmitAndEffectiveOp.java`（115 行）：

```
[OP · OrgBatchBillSubmitAndEffectiveOp（HRCoreBaseBillOp 子类）]
  │
  ▼
onAddValidators (L58-60)
  │ └── 仅 super.onAddValidators(e) · 无自定义 Validator
  │     （submiteffect 复用 save 的 Validator 链 · 通过 ignoreValidation 间接跳过部分）
  ▼
beginOperationTransaction (L62-73)
  │ ├── 1. ⭐ this.setAddOrgNumber(dataEntity)   # 关键步骤：补全 add entry 的编码
  │ │     （L75-110 实证 · 见下面解析）
  │ ├── 2-5. 同 submit 的 setBeforeAndAfterChgVersionId 三步走
  │ └── 6. BATCHORGENTITY_HELPER.update(datas) 持久化 entry
  ▼
[继续走 audit 操作链 · 触发 OrgBatchChgBillEffectOp]
  │
  ▼
（参见下一节"生效执行链"）
```

**`setAddOrgNumber` 解析**（L75-110 · 反编译实证）：

```java
// 1. 加载本单所有分录
DynamicObject[] datas = OrgBatchValidateHelper.BATCHORGENTITY_HELPER.loadDynamicObjectArray(
    new QFilter[]{new QFilter("billid", "=", dataEntity.get("id"))});

// 2. 筛出"新增组织 + 编码为空"的分录
List addDys = Arrays.stream(datas).filter(dy ->
    dy.getLong("changetype.id") == OrgBatchChgBillConstants.CHANGE_TYPE_ADD.longValue()
    && StringUtils.isEmpty(dy.getString("number"))
).collect(Collectors.toList());

// 3. 找每个 add 分录的"所属法律实体"
Map idVsBelongCompany = AdminOrgBatchChgHelper.findBelongCompanyMap(datas);

// 4. 复制成临时 haos_adminorgdetail 对象 · 用法人维度查编码规则
for (DynamicObject addDy : addDys) {
    DynamicObject adminorgHrDy = new DynamicObject(MetadataServiceHelper.getDataEntityType("haos_adminorgdetail"));
    HRDynamicObjectUtils.copy(addDy, adminorgHrDy);
    adminorgHrDy.set("parent", addDy.get("parentorg"));
    adminorgHrDy.set("orgtype", addDy.get("adminorgtype"));
    adminorgHrDy.set("company", addDy.get("belongcompany"));
    // 5. 检查该法人下是否配了编码规则
    boolean codeRuleExistFlag = AdminOrgBatchChgHelper.codeRuleExistFlag("haos_adminorgdetail", ...);
    if (codeRuleExistFlag) {
        adminorgHrDys.add(adminorgHrDy);
    }
}

// 6. 调编码规则生成器 · 把生成的编码批量回写到 entry
OrgBatchBillSaveHelper.setOrgNumber(addDys, adminorgHrDys, numberList);
```

**业务含义**：用户在 add 分录新增组织时，可以**不填编码**。submiteffect 时（或 audit 时）系统自动按法人下配置的【组织编码规则基础资料】生成编码。这是 `homs_orgbatchchgbill` 比 `admin_org_quick` 多出的能力。

---

## 六、生效执行链（audit / submiteffect 共用 · 真正写到 haos_adminorg）

⭐ **核心类**：`OrgBatchChgBillEffectOp.java`（132 行 · `HRDataBaseOp` 子类 · **不是** HRCoreBaseBillOp）

```
[OP · OrgBatchChgBillEffectOp]
  │
  ▼
onPreparePropertys (L57-60)
  │ └── e.getFieldKeys().add("effdt")
  │     （声明 OP 要读 effdt · 没声明默认不会加载 · PR-010 #2 实证）
  ▼
onAddValidators (L62-64)
  │ └── super.onAddValidators(e) · 无自定义
  ▼
beginOperationTransaction (L66-74) ⭐ 主写入路径
  │
  │ if operationKey == "audit" or "submiteffect":
  │ │
  │ └── this.billBatchEffectService.batchEffect(bills);
  │     │
  │     │ OrgBillBatchEffectService 是真正的领域服务（jar 内部 · 不在反编译清单内）
  │     │ 它的 batchEffect 完成：
  │     │   1. 遍历 7 个 entry 类别
  │     │   2. 每类调对应的 Strategy（new/parent/info/disable/merge/split）
  │     │   3. 把 entry 数据按时态规则写到 haos_adminorg + haos_adminorghis + haos_adminorgdetail
  │     │   4. 维护 boid / id / sourcevid / iscurrentversion 时序属性
  │     │
  │     ▼
  │   [haos_adminorg 主表 / haos_adminorghis 历史表 / haos_adminorgdetail 视图表 全部更新]
  ▼
afterExecuteOperationTransaction (L76-83) ⭐ 异步事件派发
  │
  │ ├── 1. new AdminChangeMsgService().handleChangeMsg();
  │ │     （触发后台 sch_task JOB_ID="5+X/4Y=AOZ=O" 异步派单 · 见 AdminChangeMsgService.java:113-123）
  │ │
  │ └── 2. if "audit" or "submiteffect":
  │         this.billBatchEffectService.afterBatchEffect(bills);
  │         （清理临时表 · 触发刷新缓存 · 通知下属业务模块）
  ▼
endOperationTransaction (L85-131) ⭐ 数据修复 + 收尾
  │
  │ ├── 1. 清理 homs_batchorgentity 中 creator=0 的脏数据
  │ │
  │ ├── 2. 加载所有 entry · 按 changetype 分组
  │ │
  │ ├── 3. 对 add 类别 entry · 把 boid 替换成 sourcevid
  │ │     （新增组织在 batchEffect 阶段创建 · 把"临时 boid"替换成"真实 sourcevid"）
  │ │
  │ ├── 4. 遍历所有 entry · 把引用了 add 组织的 parentorg/adminorg 字段同步更新
  │ │     （比如 parent entry 引用了 add entry 新建的组织作上级 · 需要 vid 替换）
  │ │
  │ └── 5. 记录事务回滚状态（只读 · 用于日志）
  ▼
[业务事件分发]
  │
  └── sch_task JOB(JOB_ID="5+X/4Y=AOZ=O") 异步消费
      │
      └── 调 EventServiceHelper.triggerEventSubscribeJobs(...)
          │
          └── 触发 BEC 业务事件 · 推送给所有订阅方（如 hrpi_empjobrel 同步、通知插件等）
```

---

## 七、终止申请（breakup）

来自 `AdminOrgBatchBreakupOp.java`（34 行）+ `AdminOrgBatchBillListPlugin.java:301-318`：

```
[列表 ListPlugin · AdminOrgBatchBillListPlugin · beforeDoOperation L301]
  │
  │ if operateKey == "breakup":
  │ ├── 1. 校验：一次只能操作 1 条单据（L311-L314）
  │ │     若 selectedRows > 1 · showTipNotification("一次只能操作一条数据") + setCancel(true)
  │ └── 2. 若没有 "afterconfirm" variable · 调 callValidatorBeforeDoOperation 走前置校验
  ▼
[OP · AdminOrgBatchBreakupOp（HRCoreBaseBillOp 子类）]
  │
  ▼
onAddValidators (L25-27)
  │ └── e.getValidators().add(new AdminOrgBatchBillBreakupStatusValidator());
  │     （状态校验：只有 C 已生效 / B 待审 / A 暂存 状态可终止 · 已废弃 / 已终止 不能再终止）
  ▼
beginOperationTransaction (L29-33)
  │ └── OrgBatchBreakupService.doBreakUpBill(bills[0], this.getOption());
  │     （领域 Service 的 breakup 逻辑：
  │      1. 把 billstatus 改为 G（终止）
  │      2. 若已经生效（C 状态）· 走"反向回退"逻辑撤销 haos_adminorg 的版本
  │      3. 若未生效（A/B）· 仅改单据状态 · 不动主数据）
  ▼
[列表 ListPlugin · closedCallBack L374]
  │ └── 处理弹窗回调（终止操作通常带二次确认对话框）
```

⚠️ **关键限制**：
- `breakup` **一次只能终止 1 条单据**（`AdminOrgBatchBillListPlugin.java:312`）
- 已生效的单据终止时会触发**反向回退** · 风险高 · 通常需要审批
- 多张单据要终止时 · 必须循环执行单条 · 不能批量

---

## 八、废弃单据（discard_row）· 列表批量

来自 `AdminOrgBatchBillListPlugin.java:350-364`（列表"废弃"按钮 confirmCallBack）：

```
[ListPlugin · confirmCallBack · callBackId="discard_row"]
  │
  │ if checkUsePermission("0=KX5+R7YTRT") == false:
  │   showMessage("您的废弃权限已发生变更...") + return
  │
  │ 1. 加载所选单据：billHelper.query("billstatus", new QFilter("id", "in", primaryKeyValues))
  │ 2. 全部写 billstatus = "F"
  │ 3. billHelper.updateDatas(billArr)
  │ 4. showSuccessNotification("废弃成功")
  │ 5. invokeOperation("refresh") · invokeOperation("discard_nothing")
```

⚠️ **跟 breakup 区别**：
- `discard_row` 仅改 `billstatus = F` · **不撤销已生效到 haos_adminorg 的数据**
- `breakup` 是真"终止" · 会反向回退已生效数据
- 用户场景："废弃"用于丢弃错的草稿 · "终止"用于撤销已经走过审批的单据

---

## 九、列表 hyperlink 联动展开（特殊 UI 行为）

`AdminOrgBatchBillListPlugin.java:179-204` 实证 · 列表的"新增/上级/信息/停用/合并/拆分 N"列点击行为：

```
ListPlugin · listRowClick:
  if focusField in {"addcount", "parentcount", "infocount", "disablecount", "mergecount", "splitcount"}:
    1. 取当前选中行的 billId
    2. 调 getBillEntityByBillId(billId, changeTypeId) · 查该 entry 类别的明细数据
    3. 弹"组织调整明细"浮层（FormShowParameter formId = "homs_orgbatchbilltips" / 合并是 "homs_orgbatchbillmergetip" / 拆分是 "homs_orgbatchbillsplittip"）
    4. 浮层内显示该类别的所有组织 number/name 列表
```

→ 列表上看到的"新增 N"是聚合数 · 来自 `beforePackageData` 阶段（L119-145）按 `changetype.id` 分组算 size · 合并/拆分按 `aftermergeorgid distinct count` / `beforesplitorgid distinct count`（L168-170）。

---

## 十、跨 entry 互斥校验（防止"同一组织两种变动"）

来自 `AdminOrgBatchBillPlugin.java:662-704`（`showMessageForMergeAndSplit`）：

**业务规则**：同一行政组织不能在一张申请单内同时被多种类型操作（比如"新增 + 调上级"或"信息变更 + 停用"）。

**实现**：删除 merge/split 行前 · 扫描其他 entry · 看是否引用了同一 adminorg 在做其他变动。如有 · 弹确认框：

```
"行政组织"X"在本单中已经发起"调整上级"操作 · 不允许同时对组织进行多种变动操作。"
```

→ 用户修订自定义业务规则时 · 务必 mind 这条原生限制 · 不要写"允许同 org 多 entry"的扩展（违反平台铁律）。

---

## 十一、流程图汇总（决策树）

```
                    用户进入【组织调整申请列表】
                              │
                              ▼
                    ┌─────────────────────┐
                    │ 新建 / 选择已有 (A/B) │
                    └─────────────────────┘
                              │
                              ▼
                    7 entry 任意填（add/parent/info/disable/merge/split）
                              │
                              ▼
                          ┌─────┐
                          │ save │
                          └─────┘
                              │
                              ▼
                    ┌──────────────────────┐
                    │ A 暂存 · 数据落 4 张表  │
                    └──────────────────────┘
                              │
              ┌───────────────┼───────────────┐
              ▼               ▼               ▼
        ┌─────────┐    ┌──────────────┐  ┌─────────┐
        │ submit  │    │ submiteffect │  │ breakup │
        │ (走审批) │    │ (跳过审批)    │  │ (终止)  │
        └─────────┘    └──────────────┘  └─────────┘
              │               │               │
              ▼               ▼               ▼
        ┌─────────┐    ┌──────────────────┐ ┌────────┐
        │ B 待审  │    │ C 已生效（直接）  │ │ G 终止 │
        └─────────┘    └──────────────────┘ └────────┘
              │
              ▼
         [wf 引擎]
              │
        ┌─────┴─────┐
        ▼           ▼
    ┌───────┐   ┌───────┐
    │ audit │   │reject │
    └───────┘   └───────┘
        │           │
        ▼           ▼
    ┌───────┐   ┌──────┐
    │ C 生效 │   │ A 退 │
    └───────┘   └──────┘
        │
        ▼
[OrgBatchChgBillEffectOp 链]
    │
    ├── beginTx · billBatchEffectService.batchEffect()
    │   └── 落 haos_adminorg / haos_adminorghis / haos_adminorgdetail
    │
    ├── afterTx · AdminChangeMsgService.handleChangeMsg()
    │   └── 派单 sch_task JOB_ID="5+X/4Y=AOZ=O"
    │       └── 异步触发 BEC · 通知所有订阅方（如 hrpi_empjobrel 同步）
    │
    └── endTx · 修复临时 boid → sourcevid 引用 + 清脏数据
```

---

**📌 来源追溯**：

- 状态机字段写入：
  - `OrgBatchBillSubmitOp.java:36` writes `billstatus="B"`
  - `AdminOrgBatchBillListPlugin.java:358` writes `billstatus="F"`
  - `AdminOrgBatchBillPlugin.java:493` 在 wftask 场景 setValue("billstatus","A")
- save 链：`OrgBatchBillSaveOp.java:46-62`
- submit 链：`OrgBatchBillSubmitOp.java:32-55` + `AdminOrgBatchBillPlugin.java:454-462`
- submiteffect 链：`OrgBatchBillSubmitAndEffectiveOp.java:62-110`
- 生效链：`OrgBatchChgBillEffectOp.java:66-131`
- 异步事件：`AdminChangeMsgService.java:113-123`（JOB_ID 实证）
- 终止链：`AdminOrgBatchBreakupOp.java:25-33` + `AdminOrgBatchBillListPlugin.java:301-318`
- 废弃逻辑：`AdminOrgBatchBillListPlugin.java:350-364`
- 列表聚合：`AdminOrgBatchBillListPlugin.java:119-177`（beforePackageData + putField2CountMap）
- 跨 entry 互斥：`AdminOrgBatchBillPlugin.java:662-704`
