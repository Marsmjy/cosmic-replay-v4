# 上下游联动 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 01_capability_boundary.md 上下游关系 + 04_business_flow.md + 05_data_flow.md + form_lifecycle_rules.json
> **confidence**: verified
> **数据源**: PermRelateEdit/PermRelateList 反编译 + scene_doc.json + rules_chain_all.json (2026-04-28)

---

## 一、上游依赖 (本场景读什么)

### 1.1 元数据上游 (F7 引用源)

| 上游场景 | formId / entity | 引用字段 | 使用方式 | 实证 |
|---|---|---|---|---|
| 业务对象 | `bos_entityobject` | entitytype (主) · entitytypeid (分录) | F7 BasedataEdit · beforeF7Select 过滤 | FP_BF7_1 / FP_BF7_2 |
| 应用开发平台 | `bos_devportal_bizapp` / `hbp_devportal_bizapp` | app (分录) · bizapp (主隐藏) | F7 BasedataEdit · beforeF7Select 过滤 | FP_BF7_3-5 |
| 权限项 | `perm_permitem` | permitemid (分录) · mainpermitem (主) | 主权限项 ComboEdit 动态下拉 · 分录 hrcs_choose_permitem 子页面 | FP_ABD2 / FP_CLK1 |
| BU 职能类型 | `hbss_hrbucafunc` | (隐式 · 不直接在 UI 显示) | HRBuCaServiceHelper.getBuCaFuncFromSpec 查 BU id | FP_PC5 · afterBindData putAllBuInfoToCache |

### 1.2 Service 上游 (数据查询)

| Service | 用途 | 读哪些表 | 实证 |
|---|---|---|---|
| EntityCtrlServiceHelper.queryExistedForBidInfo | 查被禁的实体-权限-应用映射 | `perm_*` 权限相关表 | FP_ABD1 (afterBindData L164-L165) |
| EntityCtrlServiceHelper.buildFilterForF7 | 限制 F7 可选实体为 HR 实体 | `bos_entityobject` + 权限表 | FP_BF7_1 |
| EntityCtrlServiceHelper.filterNoPermEntity | 排除当前用户无权限的实体 | `bos_entityobject` + 权限表 | FP_BF7_1 |
| EntityCtrlServiceHelper.getEntityRelatedApps | 查业务对象关联的应用列表 | `bos_devportal_bizapp` 关联表 | FP_BF7_3 |
| EntityCtrlServiceHelper.queryEntityPermItems | 查实体下的权限项列表（带翻译） | `perm_permitem` | FP_ABD3 |
| HRBuCaServiceHelper.getBuCaFuncFromSpec | 根据 entityId + appId 查 BU id | `hbss_hrbucafunc` | FP_PC5 |
| PermRelateServiceHelper.queryPermItems | 查权限项信息 | `perm_permitem` | afterBindData |
| HRBaseServiceHelper.query("hrcs_permrelat", ...) | 查本场景数据（btnsycrole 时） | `t_hrcs_permrelat` + `t_hrcs_permrelatentry` | FP_LBDO5 |

### 1.3 准入上游

| 检查 | 用的 Service | 失败效果 |
|---|---|---|
| 平台管理员 / Cosmic 用户 | PermissionServiceHelper.isAdminUser / PermCommonUtil.isCosmicUser | setCancel + "您无法访问该功能·因为您不是HR领域管理员。" (FP_HAS2) |
| HR 域管理员 | HRAdminService.isHrAdmin() | setCancel + 同上 (FP_HAS3) |
| F7 lookUp 模式 | (直接放行 · FP_HAS1) | 不校验准入 · 普通用户可引用 |

---

## 二、下游影响 (本场景写什么/触发什么)

### 2.1 同步写入下游

| 下游表/场景 | 触发时机 | 同步/异步 | 写入内容 | 失败策略 |
|---|---|---|---|---|
| **t_hrcs_permrelatcfg** (save) | afterSaveProcessing (FP_ADO4) · t11 阶段 | 同步 (独立 OP · 非同一事务) | PermRelateServiceHelper.deletePermRelateConfigs(deleteRows) + addPermRelateConfigs(addRows) | ❌ 失败不影响主 save · 但产生数据不一致 (无补偿机制) |
| **t_hrcs_permrelatcfg** (delete) | afterDoOperation(delete) (FP_LADO1) | 同步 (独立调) | PermRelateServiceHelper.deletePermRelateConfigs(deleteRows) | ❌ 失败残留孤儿数据 |
| **t_perm_role_perm** (实时) | btnsycrole 选 1-10 行 + 用户在 hrcs_syncroleperm 子页面确认 | 半同步 (计算实时 · 落库在子页面独立事务) | calcRtPermRole 输出角色-权限项映射 | ✅ 子页面自管事务 · 失败回滚 |
| **t_perm_role_perm** (全量) | btnsycrole 全量 + HRRelatePermTask | 异步 (JobForm.dispatch → sch_task 调度) | 全量扫 hrcs_permrelat → 批量 calcRtPermRole → 批量 update | ⚠️ 任务失败 (超时/异常) → 需要重跑 · CanStop=false 防中断 |

### 2.2 UI 跳转下游

| 下游场景 | 触发 | pageId 格式 | 回调 |
|---|---|---|---|
| hrcs_permrelatcfg (细粒度授权列表) | auth opKey | `hrcs_permrelatcfg@<parentPageId>` | 无回调 (MainNewTabPage) |
| hrcs_syncroleperm (同步角色预览) | btnsycrole 选 1-10 行 | (Modal) | 无回调 · 子页面独立处理 |
| hrcs_syncrolesel (同步角色选择) | incPermTips syncRole=1 | (Modal) | 无回调 · 子页面独立处理 |
| hrcs_choose_permitem (权限项多选) | 分录 permitem 点击 | (Modal) | closedCallBack actionId="hrcs_choose_permitem" → 回填 permitemid/permitem |
| exportscript SQL 文件下载 | exportscript | (浏览器下载) | TempFileCache.saveAsUrl → view.openUrl |

### 2.3 父表单通知下游

| 通知内容 | 触发 | 接收方 | 效果 |
|---|---|---|---|
| `{changed: "changed"}` | FP_ADO2 (save/modify 成功) | PermRelateList.closedCallBack(incPermTips) → FP_LCCB2 | listView.clearSelection() + listView.refresh() |
| `{entityNum, permNum}` | FP_ADO1 (有新增分录) | 父表单 (如果是从其他表单跳过来的) | 父表单刷新 · 更新对应实体编号/权限编号 |
| `{syncRole: 1, resultRolePermMap}` | FP_ADO3 (save 后 calcRtPermRole 有差异) | PermRelateList.closedCallBack(incPermTips) → FP_LCCB1 | 弹 hrcs_syncrolesel 让用户选择同步哪些角色 |

---

## 三、跨 Cloud 联动

### 3.1 本场景的 Cloud 归属

- **Cloud**: HR基础服务云
- **App**: hrcs (HR通用服务)
- **共享基础设施**: 共用 HRAdminStrictPlugin (11 个 hrcs 表单)

### 3.2 与 hrcs 域内其他场景的关系

| 场景 | 关系 | 联动方式 |
|---|---|---|
| hrcs_permrelatcfg | **直接下游** (save/delete 同步) | PermRelateServiceHelper 调用链 |
| hrcs_dynascheme (动态授权方案) | **平行场景** (同为 HR 权限配置 · 但不直接关联) | 共用 HRAdminStrictPlugin · 都是 BillFormModel · hrcs_dynascheme 有 HisModel+审批 · 本场景无 |
| hrcs_syncrolesel / hrcs_syncroleperm | **子页面下游** (角色同步弹窗) | closedCallBack / customParam 传参数 |
| hrcs_choose_permitem | **子页面** (权限项多选) | closedCallBack 回填 · pk 协议 "permId\|\|permName" |
| 其他 10 个 hrcs 表单 (hbjm_jobhr / haos_adminorg 等) | **准入共用** (HRAdminStrictPlugin) | 改动 HRAdminStrictPlugin 影响所有 11 个 hrcs 表单 |

### 3.3 与其他 Cloud 的场景关系

| 场景 | Cloud | 关系 | 联动方式 |
|---|---|---|---|
| bos_entityobject (业务对象) | 平台层 | **元数据依赖** | F7 引用 · EntityCtrlServiceHelper 查询过滤 |
| bos_devportal_bizapp (应用) | 平台层 | **元数据依赖** | F7 引用 · appcombo 下拉 · EntityCtrlServiceHelper 查询过滤 |
| hbss_hrbucafunc (BU 职能类型) | HR 基础服务云 | **字典依赖** | HRBuCaServiceHelper.getBuCaFuncFromSpec 查 BU id · 用于 BU 一致性校验 |
| perm_permitem (权限项) | 平台层 | **元数据依赖** | PermRelateServiceHelper.queryPermItems · 子页面多选 |
| perm_role / perm_role_perm | 平台层 | **角色同步下游** | calcRtPermRole → 用户在弹窗确认 → 子页面落库 |
| sch_task (调度任务) | 平台基础设施 | **异步执行** | JobForm.dispatch → HRRelatePermTask |

---

## 四、联动失败场景与应对

### 4.1 save 主成功 + 下游同步失败

**场景**: save t_hrcs_permrelat 成功 · afterSaveProcessing 调用 PermRelateServiceHelper.deletePermRelateConfigs/addPermRelateConfigs 时失败（如网络闪断/DB 异常）。

**后果**: `t_hrcs_permrelatcfg` 会有孤儿数据 (deleteRows 没删) 或缺失数据 (addRows 没加)。

**应对**:
- **标品**: 无补偿机制 (FP_ADO4 直接调 · 不检查返回值)
- **ISV**: 在自建 OP 的 afterExecute 阶段加监控日志 · 手动修复脚本 · 或加定时对账任务

### 4.2 delete 主成功 + hrcs_permrelatcfg 同步删除失败

**场景**: delete t_hrcs_permrelat 成功 · afterDoOperation 调用 deletePermRelateConfigs 时失败。

**后果**: `t_hrcs_permrelatcfg` 残留孤儿数据 (关联权限项已删但细粒度配置还在)。

**应对**:
- **标品**: 无补偿 (FP_LADO1)
- **ISV**: 对账脚本 JOIN `t_hrcs_permrelatcfg LEFT JOIN t_hrcs_permrelat` 找孤儿记录

### 4.3 HRRelatePermTask 调度任务执行失败

**场景**: btnsycrole 全量 → startJob → HRRelatePermTask 超时 (1200秒) 或异常。

**后果**: `t_perm_role_perm` 部分更新 · 角色权限不一致。

**应对**:
- **标品**: CanStop=false 防中断 · 超时后会标记失败 · 需手动重跑
- **ISV**: 不要覆盖 CanStop 设置 · 监控 sch_task 失败告警

### 4.4 exportscript 临时事务回滚

**场景**: generateSql 中 TX.requiresNew 事务异常 → tx.markRollback()。

**后果**: 临时改的 issyspreset/isSynRole 被回滚 (独立事务) · 不影响主数据。

**应对**:
- **标品**: catch Exception → tx.markRollback() → throw KDBizException
- **ISV**: 不要覆盖 exportscript 逻辑 · 如需自定义导出格式，自建独立 opKey

---

## 五、联动数据格式契约

### 5.1 hrcs_choose_permitem 子页面 → 父表单

```
returnData: ListSelectedRowCollection
  each row:
    pk = "permId||permName"           ← 双竖杠分隔（标品私有协议）
  解析:
    int idx = pk.indexOf("||")
    String permId = pk.substring(0, idx)
    String permName = pk.substring(idx + 2)
```

### 5.2 PermRelateEdit → PermRelateList (returnDataToParent)

```
save 后:
  returnData = {
    "changed": "changed",                  ← 列表 refresh 信号
    "entityNum": "<entityTypeNumber>",     ← 有新增分录时
    "permNum": "<mainPermItemNumber>",     ← 有新增分录时
    "syncRole": 1,                         ← calcRtPermRole 有差异时
    "resultRolePermMap": <JSON map>        ← roleId → Set<permId>
  }
```

### 5.3 btnsycrole → hrcs_syncroleperm 子页面

```
customParam:
  roleInfo: JSON(LinkedHashMap<roleId, Set<permId>>)
  roleCount: int
  permCount: int
```

### 5.4 incPermTips → hrcs_syncrolesel 子页面

```
customParam:
  roleInfo: JSON(resultRolePermMap)        ← roleId → Set<permId>
```

### 5.5 auth → hrcs_permrelatcfg 列表

```
ListShowParameter:
  setBillFormId("hrcs_permrelatcfg")
  setFormId("bos_list")
  setPageId("hrcs_permrelatcfg@" + parentPageId)   ← @parent 保持父子关系
  ShowType.MainNewTabPage
```

### 5.6 exportscript SQL 下载

```
TempFileCache:
  fileName: "ExportScript.sql"
  TTL: 5000 秒
  content: "BEGIN;\nINSERT INTO T_HRCS_PERMRELAT ...;\nINSERT INTO T_HRCS_PERMRELAT_L ...;\nINSERT INTO T_HRCS_PERMRELATENTRY ...;\nCOMMIT;"
```

---

## 六、联动时序总览

```
t0: 用户操作
    ↓
t1: FormPlugin.beforeDoOperation (标品拦截)
    ↓
t2-t8: OP 13 阶段 (标品 HRDataBaseOp · ISV 在此扩展)
    │  t3: onAddValidators (ISV 挂 Validator)
    │  t4: beforeExecuteOperationTransaction (ISV 加前处理)
    │  t5-t8: 事务内 (save/delete 主表)
    │  t9: afterExecuteOperationTransaction (ISV 发 BEC · 事务已提交)
    ↓
t10: FormPlugin.afterDoOperation
    │  FP_ADO2: returnDataToParent("changed")
    │  FP_ADO1: returnDataToParent(entityNum/permNum)
    ↓
t11: afterSaveProcessing (private)
    │  FP_ADO4: sync hrcs_permrelatcfg (独立事务)
    ↓
t12: calcRtPermRole (计算角色差异)
    │  FP_ADO3: returnDataToParent(syncRole=1, resultRolePermMap)
    ↓
t13: 列表 closedCallBack(incPermTips)
    │  FP_LCCB2: listView.refresh (on changed)
    │  FP_LCCB1: 弹 hrcs_syncrolesel (on syncRole=1)
    ↓
t14: 用户确认角色选择 → 子页面落库 t_perm_role_perm
```

---

## 七、ISV 扩展时的上下游注意事项

| 扩展点 | 上游注意事项 | 下游注意事项 |
|---|---|---|
| CS-01 加字段 | 字段从哪个 F7 表引用 (BasedataField) 还是自由输入 (TextField) | 字段变更不需要同步到 hrcs_permrelatcfg (分录字段不传播) |
| CS-03 加 Validator | 校验需要的数据是否已缓存在 PageCache 中 (afterBindData) | 校验失败阻止保存 · 不产生下游影响 |
| CS-04 加引用检查 | 查哪些下游表引用了当前 permrelat id | 阻止删除但不要影响标品 hrcs_permrelatcfg 同步删 |
| CS-05 发 BEC | 事件号 (eventNumber) 不能与标品/其他 ISV 冲突 | 订阅方是谁 · 是否需要 ACK 确认 |
| CS-06 扩展分录 | 分录 ID 生成是否依赖标品 entryid 分配 | 分录字段不传播到 hrcs_permrelatcfg · 不需要同步 |
| CS-07 列表过滤 | 过滤条件是否使用 PageCache 的 forBidAppStr/forBidAppEntity | 过滤不影响下游 · 但可能隐藏某些行影响同步角色选择 |

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 01 总论）

> 本场景属 HR 基础服务云（hr_hrmp）· 业务语义参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md)
> - 跨云元规则：金字塔决策方法论 + 11 大特殊解决方案
> - 6 大可继承通用模板（hbp_bd_tpl_all / hbp_bd_timelinemintpl 等）
> - HR 通用 SDK 服务 16 个（HisModelServiceHelper / TimelineServiceHelper / RuleEngineServiceHelper 等）
> - 历史模型 vs 时间轴的 6 模板 + 字段差异

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
