# core_hr_contract_renew · 业务流程

> 数据源：deep_scan_audit.md D 段调用链全图 / docx §1.3 业务方案

## 主流程时序图

```
┌─────────┐  ┌──────────────────────────┐  ┌──────────────────────────┐  ┌──────────────────────────┐
│ HR 操作  │  │ ${ISV_FLAG}_hlcm_renewbatch │  │ hlcm_contractapplyrenew  │  │ haos_adminorg            │
│ (ISV)    │  │  (ISV 主表·UI)              │  │  (标品·被反向扩展)        │  │  (行政组织·只读)          │
└────┬────┘  └─────┬─────────────────────┘  └────────┬─────────────────┘  └────────┬─────────────────┘
     │              │                                  │                              │
     │ 进入主表新增  │                                  │                              │
     │─────────────>│                                  │                              │
     │              │ afterCreateNewData (line 68)     │                              │
     │              │ → 默认值                         │                              │
     │              │                                  │                              │
     │ 选所属组织   │                                  │                              │
     │ + 点选员工   │                                  │                              │
     │─────────────>│ itemClick (line 122)             │                              │
     │              │ 取 affiliationord.id/structnumber│                              │
     │              │ ─────────调 AdminOrgHrUtils──────│─────────────────────────────>│
     │              │ <───────返子组织 ID 集──────────────────────────────────────────│
     │              │ 打开 list 弹窗 (formId=hlcm_contractapplyrenew, 过滤 IN orgIds) │
     │ <─list 弹出──│                                  │                              │
     │              │                                  │                              │
     │ 选员工·返回  │                                  │                              │
     │─────────────>│ closedCallBack (line 167)        │                              │
     │              │ 取 pkIds → 写 itemids_tag/personsize                            │
     │              │                                  │                              │
     │ 保存（save）  │                                  │                              │
     │─────────────>│ RenewBatchOpPlugin.before (line 46)                             │
     │              │ ─读 itemids_tag → 反查────────>│ load(... in itemIds)          │
     │              │ ─校验 billstatus="C" ─────────>│                              │
     │              │ ─写 ${ISV_FLAG}_renewbatch=主表 ID──>│                          │
     │              │ <─SaveServiceHelper.save        │                              │
     │              │                                  │                              │
     │ 提交流程审批  │                                  │                              │
     │─────────────>│ submit / wfauditing 同 save 流程│                              │
     │              │                                  │                              │
     │ 流程通过      │                                  │                              │
     │─────────────>│ audit·beforeExecute + afterExecute 双确认                       │
     │              │ → 状态完成                       │ ${ISV_FLAG}_renewbatch 已写 │
     │              │                                  │                              │
     │ 反审批 (wfunaudit) / 失效 (invalid)              │                              │
     │─────────────>│ unbindRenewBatch (line 235)      │                              │
     │              │ ─反查 ${ISV_FLAG}_renewbatch=本主表ID─>│                       │
     │              │ ─置 null·解绑─────────────────>│                              │
```

## 调用链全图（save op 完整路径）

详见 [deep_scan_audit.md D 段](../../../dcs_regression/passed/case_002_contract_renew/deep_scan_audit.md)·此处简化：

```
[用户保存 ${ISV_FLAG}_hlcm_renewbatch 单据]
  ↓
RenewBatchOpPlugin.beforeExecuteOperationTransaction (RenewBatchOpPlugin.java:46)
  ↓ Step 1 · 守门
  if (!Arrays.asList("audit","save","submit","wfauditing").contains(opKey)) return;
  ↓ Step 2 · 读 itemids_tag
  for (DynamicObject n : dataEntity) {
      String itemIds = n.getString("${ISV_FLAG}_itemids_tag");          [line 94, 116]
      if (StringUtils.isBlank(itemIds)) continue;
      ↓ Step 3 · 拉关联标品记录
      DynamicObject[] items = BusinessDataServiceHelper.load(
          "hlcm_contractapplyrenew",
          "billstatus,handlestatus,auditstatus,${ISV_FLAG}_renewbatch",
          new QFilter("id", QCP.in, parseItemIds(itemIds)).toArray());
      ↓ Step 4 · 校验状态
      for (DynamicObject item : items) {
          if (!"C".equals(item.getString("billstatus"))) {
              throw new KDBizException("员工 " + item.getString("empnumber") + " 合同未审核");
          }
      }
      ↓ Step 5 · 写反向关联
      Long pkId = n.getLong("id");
      for (DynamicObject item : items) {
          item.set("${ISV_FLAG}_renewbatch", pkId);                     [line 287]
      }
      SaveServiceHelper.save(items);
  }
  ↓ op 通过·进入 afterExecute
RenewBatchOpPlugin.afterExecuteOperationTransaction (line 138)
  ↓ wfauditing/audit 二次确认 (写关联) - 防 before 失败
  ↓ wfunaudit/invalid 反向解绑：
  for (DynamicObject n : dataEntity) {
      Long pkId = n.getLong("id");
      DynamicObject[] bindItems = BusinessDataServiceHelper.load(
          "hlcm_contractapplyrenew",
          "id,${ISV_FLAG}_renewbatch",
          new QFilter("${ISV_FLAG}_renewbatch.id", QCP.equals, pkId).toArray());
      for (DynamicObject item : bindItems) {
          item.set("${ISV_FLAG}_renewbatch", null);                     [line 296]
      }
      SaveServiceHelper.save(bindItems);
  }
```

## 列表插件流程（ContractOtherExListPlugin·hlcm 列表扩展）

```
[用户在 hlcm_contractapplyrenew 列表点工具栏按钮]
  ↓
ContractOtherExListPlugin.itemClick (line 50) 或 click (line 183)
  ↓ 路径分流（按 itemKey）
  case "tblsubmiteffect": (提交生效)
      → load 选中行 fillinstatus → 必须 "2" 才放行
  case "tblsubmit": (提交)
      → load 选中行 fddsignstatuss + signwaynew → 任一满足才放行
  case "${ISV_FLAG}_baritemapex1": (ISV 自建按钮)
      → load 选中行 fddsignstatuss → "E"/"F" 才放行
  ↓ 校验失败
  → throw KDBizException 阻止提交
```

## 异常路径

| 异常 | 触发 | 处理 |
|---|---|---|
| `员工 X 合同未审核` | OP audit 时·hlcm billstatus != "C" | throw KDBizException·中断 op |
| `合同填写未完成` | 列表 tblsubmiteffect 点击·`${ISV_FLAG}_fillinstatus` != "2" | throw·阻止提交 |
| `签署状态不允许提交` | 列表 tblsubmit 点击·fdd != F·signway != A | throw·阻止 |
| `选员工 list 返回空` | closedCallBack pkIds 为空 | personsize=0 / itemids_tag="" |

详见 10_exceptions.md
