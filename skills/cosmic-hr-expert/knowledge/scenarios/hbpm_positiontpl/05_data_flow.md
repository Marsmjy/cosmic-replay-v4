# 数据流转 · 岗位模板（hbpm_positiontpl）

> **状态**: 🟢 基于反编译 4 类实证
> **confidence**: verified

---

## 一、新建/保存时数据写入路径

```
UI 层（编辑表单）
    ↓ save opKey
PositionTplSaveOp.beforeExecuteOperationTransaction
    读 t_hbpm_positiontpl（历史快照 · id != 0L 的已有记录）
    构建 oldDynMap（id → DynamicObject）
    ↓
（平台保存主实体 t_hbpm_positiontpl + t_hbpm_positiontpl_l）
    ↓
PositionTplSaveOp.endOperationTransaction
    PositionTplChangeSyncPosService.syncUpdatePosition()
    → 差量对比 oldDynMap vs 新数据
    → 更新 t_hbpm_positionhr（岗位信息）关联字段
    ↓
PositionTplSaveOp.afterExecuteOperationTransaction
    ChangeMsgServiceImpl.sendMsg()
    → 发 BEC 变更消息（下游系统感知）
    IBosPositionService.commonSyncPositions()
    → 同步 bos_position（平台岗位数据）
```

---

## 二、适用范围设置时数据路径

```
PositionTplListPlugin.afterDoOperation(operateKey="donothing_setscope")
    打开 hbpm_applicationscope 弹窗
    → 用户在弹窗中选择适用组织范围
    → 写 hbpm_applicationscope 关联表
    关闭弹窗 → closedCallBack → invokeOperation("refresh")
```

---

## 三、列表 BU 过滤数据路径

```
PositionTplBuListPlugin.getPermOrgResult()
    PermissionServiceHelper.getAllPermOrgs(userId, "21", appId, "hbpm_positiontpl", "47150e89000000ac")
    → HasPermOrgResult（权限组织结果）
    → 序列化存 PageCache("org_perm_result")（避免重复查询）
    ↓
filterColumnSetFilter / filterContainerBeforeF7Select
    → QFilter("id", "in", hasPermOrgs) → 列表数据过滤
```

---

## 四、数据关系图

```
hbpm_positiontpltype（上游 · 岗位模板类型）
    ↓ posttpltype 字段（BasedataField 单选引用）
t_hbpm_positiontpl（主表）
    ↓ 被引用（PositionTplChangeSyncPosService）
t_hbpm_positionhr（岗位信息维护 · 选择模板）
    ↓ 被引用
bos_position（平台岗位 · commonSyncPositions）
    ↓ BEC 消息
hbpm_applicationscope（适用组织范围 · 关联表）
```

---

## 五、跨数据一致性风险

| 操作 | 同步目标 | 实现层 | 风险 |
|---|---|---|---|
| save/modify | t_hbpm_positionhr 关联字段 | endOperationTransaction | 差量同步失败 → 岗位数据与模板不一致 |
| save/modify | bos_position | afterExecuteOperationTransaction | commonSyncPositions 失败 → 平台岗位数据未同步 |
| save/modify | 下游系统（BEC）| afterExecuteOperationTransaction | sendMsg 失败 → 下游无法感知变更 |

---

## 六、权限数据流

```
新建时 org 自动填值：
  PermissionServiceHelper.getAllPermOrgs(userId, "21", appId, "hbpm_positiontpl", "47150e89000000ac")
  → HasPermOrgResult.getHasPermOrgs()
  → 优先当前请求组织 → 其次列表第一个

BU 参数查询：
  SystemParamHelper.getBatchParameter(Collections.singletonList(orgId))
  → Map<orgId, Map<paramKey, value>>
  → openpositiontpl / positiontplismodify / positiontplchangepos / modifyfieldrange
```
