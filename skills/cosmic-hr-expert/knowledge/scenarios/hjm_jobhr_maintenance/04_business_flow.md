# 业务流转 + 插入点地图 · 职位体系维护 (hbjm_jobhr)

> **状态**: 🟢 基于 `_auto_operations.md` 61 opKey 执行链 + `rules_chain_all.json` 10 条 executionChain + `_auto_plugin_semantics.md` 反编译整合
> **confidence**: verified（每个节点的插件类名都来自实抓）

---

## 一、生命周期全景：基础资料 + 时序版本双轨

苍穹职位不是"单次创建 + 编辑"的简单模型，而是**基础资料审批流 + 时序版本化**双轨：

```
        基础资料审批流（适用所有 hr 基础资料）
        A (暂存) ── submit ──► B (已提交) ── audit ──► C (已审核)
             ▲                      │                       │
             │ unsubmit            │                       │ unaudit
             └──────────────────────┘                       │
                                                             │
                                    ┌────────────────────────┘
                                    ▼
                               时序版本化（HR 基础资料特色）
                               ├── revise       修订（就地改，不产新版）
                               ├── newhisversion 新增数据版本（产新 bsed）
                               ├── change        变更（产新 bsed）
                               └── confirmchange 确认变更（走 JobHrSaveOp）
                                                     ▲
                                                     │
                                   启用 / 禁用（enable / disable）
                                                     │
                                                     ▼
                                            delete（删除，走 hrbddeletevalidator）
```

**状态机字段**：
- `status`（BillStatusField）：数据状态 A/B/C（`scene_doc.json` L57）
- `enable`（BillStatusField）：业务状态 启用 / 禁用（`scene_doc.json` L99）

---

## 二、子场景 1：列表展示（进入主流程入口）

### 流程

```
用户访问 hbjm_jobhr 菜单（menuId: 1305038595106658304）
    ↓
[列表加载] JobBaseDataListPlugin.setFilter
    （继承 HRDataBaseList，super 调用 + 追加 filter）
    ↓
[权限过滤] JobBaseBuListPlugin.filterContainerBeforeF7Select
    （调用 getPermOrgResult 拿到当前用户管辖的创建组织集合）
    ↓
[超链点击] JobInitFilterCommonPlugin.billListHyperLinkClick
    （跳转到详情页）
    ↓
[详情渲染] JobBasedataEdit / JobHisBasedataEdit
```

### 节点详情

| 节点 | 插件类名 | 触发方法 | 可扩展 |
|---|---|---|---|
| 列表加载 | `JobBaseDataListPlugin` | `setFilter(SetFilterEvent)` | ✅ 追加 filter |
| 组织权限 | `JobBaseBuListPlugin` | `filterContainerBeforeF7Select` | ✅ 追加查询条件 |
| F7 过滤 | `JobBaseBuListPlugin` | `getFilterSchemaFieldSet` | ✅ 修改 F7 字段集 |
| 超链点击 | `JobInitFilterCommonPlugin` | `billListHyperLinkClick` | ✅ 改跳转目标 |

**证据**：`_auto_plugin_semantics.md` L47-L81

---

## 三、子场景 2：新增 / 修改职位（CRUD）

### 流程

```
点击"新增" (opKey: new) → 打开向导
    ↓
填写字段：必填仅 jobseq，推荐填 number / name / 职级职等方案
    ↓
点击"保存" (opKey: save)
    ↓
beforeExecuteOperationTransaction: 10 插件链
    ├── (1) CodeRuleOp          编码规则
    ├── (2) BaseDataSavePlugin  基础资料保存
    ├── (3) BdVersionSaveServicePlugin  版本管理
    ├── (4) HRBaseDataStatusOp  HR 状态维护
    ├── (5) HRBaseDataLogOp     HR 日志（默认 disabled）
    ├── (6) HRBaseDataEnableOp  启用态维护
    ├── (7) HRBaseOriginalOp    原始值记录
    ├── (8) HisUniqueValidateOp 时序唯一性
    ├── (9) HisModelOPCommonPlugin 时序通用
    └── (10) JobHrSaveOp        ⭐ 职位特有保存
    ↓
onAddValidators: 6 条校验
    ├── MustInput               字段值合规
    ├── FormValidate 1VRALXJOVNKD 合法性（默认 disabled）
    ├── GroupFieldUnique 0+/SL/MZ=VJB  创建组织+编码（默认 disabled）
    ├── GroupFieldUnique 2=K9URZCEUUS  编码唯一（默认 enabled）
    ├── GroupFieldUnique 23ILP6JS0TBC  创建组织+名称（默认 disabled）
    └── FormValidate 2M42D=Y0/0XK  生效日期不能填写未来
    ↓
数据入库 t_hbjm_job + t_hbjm_job_i（多语言）+ t_hbjm_jobscmmul（多选）
    ↓
afterExecuteOperationTransaction: 标品插件默认行为
    ↓
列表刷新
```

### 节点详情

| 节点 | 可用扩展点 | 标品插件（顺序） | 风险 |
|---|---|---|---|
| 字段联动 | `onFieldChange@hbjm_jobhr` | - | 低 |
| **保存校验** | ⭐ `beforeExecuteOperationTransaction@save` | 10 插件完整链（`_auto_operations.md` L99-L113） | **中** |
| 唯一性校验 | 默认 `2=K9URZCEUUS` enabled | `HisUniqueValidateOp` | 中 |
| JobHrSaveOp 自定义 | `onAddValidators` | `JobHrSaveOp` | ⭐ 最常扩展点 |
| 入库 | 事务内 | - | - |
| 反写 | 默认无标品业务反写 | - | 低 |

### 关键：JobHrSaveOp 的 2 个业务逻辑

来自 `rules_chain_all.json` L368-L403：
- `onPreparePropertys` L22-L31：声明读写字段
- `onAddValidators` L33-L46：**注册校验器**，写 `id` 字段

---

## 四、子场景 3：提交 / 审核 / 反审核 / 撤销

### 流程

```
暂存态 A
    ↓
submit（opKey: submit）
    8 插件执行链（_auto_operations.md L256-L267）
    ├── CodeRuleOp
    ├── BaseDataSubmitPlugin
    ├── BdVersionSaveServicePlugin
    ├── HisModelOPCommonPlugin
    ├── HRBaseDataLogOp (disabled)
    ├── HRBaseDataEnableOp
    ├── HisUniqueValidateOp
    └── HRBaseOriginalOp
    ↓
2 校验：1cc0054f000017ac 合法性 + RS=E9QE25UN 字段合规
    ↓
状态流转：Value "B" → 已提交
    ↓
audit（opKey: audit）
    4 插件执行链（_auto_operations.md L157-L164）
    ├── BaseDataAuditPlugin
    ├── HRBaseDataLogOp (disabled)
    ├── HisModelOPCommonPlugin
    └── JobHrAuditOp  ⭐ 职位特有审核
    ↓
3 校验：2 个 FormValidate（enabled） + InProcess（disabled）
    ↓
状态流转：Value "C" → 已审核
    ↓
unaudit（opKey: unaudit）
    3 插件（_auto_operations.md L181-L187）+ 3 校验
    （没有 JobHr*Op，走标品流程）
    ↓
状态回 "A"
```

**关键**：
- submit / audit 的 parameter 里 `Value` 定义目标 status（`_auto_operations.md` L155 audit: "C"，L179 unaudit: "A"）
- `unsubmit` 仅 2 插件（`BaseDataUnSubmitPlugin` + `HRBaseDataLogOp`），非常轻

---

## 五、子场景 4：启用 / 禁用

### 流程

```
启用 (opKey: enable)
    ↓
beforeExecuteOperationTransaction
    ├── (1) BaseDataEnablePlugin
    ├── (2) HisModelOPCommonPlugin
    └── (3) JobHrEnableOp  ⭐ 重写 3 方法
          ├── onPreparePropertys          声明字段
          ├── onAddValidators             注册 JobEnableValidator
          └── beforeExecuteOperationTransaction  读 boid/enable 做预处理
    ↓
状态字段 enable: "1"
```

### JobHrEnableOp 的 3 个业务逻辑（`rules_chain_all.json` L690-L741）

| 方法 | 作用 | 读字段 |
|---|---|---|
| `onPreparePropertys` | L31-L35 声明读写字段 | - |
| `onAddValidators` | L37-L39 注册 `JobEnableValidator` | - |
| `beforeExecuteOperationTransaction` | L41-L46 读 2 字段预处理 | `boid`, `enable` |

### 禁用流程

```
禁用 (opKey: disable)
    ↓
beforeExecuteOperationTransaction
    ├── (1) BaseDataDisablePlugin
    ├── (2) HisModelOPCommonPlugin
    └── (3) JobHrDisableOp  ⭐ 职位特有禁用
    ↓
状态字段 enable: "0"
    ↓
1 校验：f2843bab0000baac 合法性（enabled）
```

---

## 六、子场景 5：时态版本变更（HR 基础资料特有）

### 5 种版本操作

| opKey | 作用 | 触发行为 |
|---|---|---|
| `revise` | 修订 | 不产新版本，直接改 |
| `newhisversion` | 新增数据版本 | 产新 `bsed`（1 校验 `2K+JH30V7OJV`） |
| `change` | 变更 | 走变更流程 |
| `confirmchange` | 确认变更 | ⭐ 触发完整保存链 |
| `hiscopy` | 复制一个版本 | - |

### confirmchange 流程（`_auto_operations.md` L584-L605）

```
用户"确认变更"
    ↓
3 插件执行链
    ├── (1) HisModelOPCommonPlugin  时序通用
    ├── (2) HisUniqueValidateOp     历史唯一性
    └── (3) JobHrSaveOp             ⭐ 复用保存插件
    ↓
3 校验
    ├── 4V/78ID=HERR  MustInput（enabled）
    ├── 4V/7C958CRVD  名称唯一（disabled）
    └── 4V/7I6HFRZ3+  创建组织+编码唯一（enabled）
    ↓
数据入库新版本
```

### JobHrMsgHandleOp 变更消息处理（`_auto_plugin_semantics.md` L13-L26）

- **父类**：`HRDataBaseOp`
- **方法**：`beforeExecuteOperationTransaction` / `endOperationTransaction` / `afterExecuteOperationTransaction`
- **读**：`boid` / `iscurrentversion` / `sourcevid`
- **写**：`sourcevid`
- **作用**：在变更事务的不同阶段（前 / 结束 / 后）维护版本链的关联关系

---

## 七、子场景 6：HIES 导入导出

### 导入流程（`importdata_hr`）

```
点击"导入数据"（opKey: importdata_hr）
    ↓
parameter: cusstartpage = hismodel_importstart
    （即启动 HR 时序模型导入向导页）
    ↓
向导选模板 → 上传文件 → 后台异步处理
    ↓
用 opKey: show_import_record_hr 查看结果
```

### 导出 3 种模式（`_auto_operations.md` L497-L514）

| opKey | 模式 | operationType |
|---|---|---|
| `export_from_list_hr` | 按列表导出（带当前 filter） | `export_from_list_hr` |
| `export_from_impttpl_hr` | 按导入模板导出 | `export_from_impttpl_hr` |
| `export_from_expttpl_hr` | 按导出模板导出 | `export_from_expttpl_hr` |

所有 3 种都走 parameter `CustOperationParameter` + 自定义 plugins 扩展，非标准 `exportlist`。

### 传统 opKey（`exportlist` / `exportlistbyselectfields` / `exportlist_expt`）

这 3 个是苍穹基础框架的导出，**本场景 HR 推荐走 hr 专用 opKey**。

---

## 八、子场景 7：删除（只针对暂存 A 态）

### 流程

```
delete（opKey: delete）
    ↓
5 插件执行链（_auto_operations.md L133-L140）
    ├── (1) BaseDataDeletePlugin
    ├── (2) CodeRuleDeleteOp
    ├── (3) HRBaseDataStatusOp
    ├── (4) HRBaseDataLogOp (disabled)
    └── (5) HisModelOPCommonPlugin
    ↓
3 校验
    ├── FormValidate 1cc0054f000016ac  合法性（disabled）
    ├── FormValidate f789ca66000000ac  数据已禁用不能删（disabled）
    └── hrbddeletevalidator 2+U=J7R7IEF/  HR 基础资料删除校验（enabled）⭐
```

⚠️ **注意**：标品 delete **没有 JobHr 专用插件**，如果要加"禁止删除某类职位"校验，需要在 `JobHrSaveOp` 风格上新建 `JobHrDeleteOp`。

---

## 九、扩展点时序图（综合）

```
用户操作 (new/save/audit/enable/disable/change)
    ↓
┌──────────────────────────────────────────┐
│ 前端 formPlugin 层                         │
│ ├── JobHisBasedataEdit / JobBasedataEdit  │
│ ├── JobHisBasedataFiledChangeEdit         │
│ │   ├── afterBindData                     │
│ │   ├── beforeDoOperation                 │
│ │   └── afterDoOperation                  │
│ └── 前端校验（浏览器层）                   │
└────────────────┬─────────────────────────┘
                 │ HTTP
                 ▼
┌──────────────────────────────────────────┐
│ 后端 opPlugin 层（按 opKey 分发）          │
│ ├── onPreparePropertys    声明读写字段    │
│ ├── onAddValidators       注册 Validator  │
│ ├── beforeExecuteOperationTransaction     │ ⭐ 最常扩展
│ ├── beginOperationTransaction             │
│ ├── endOperationTransaction               │
│ └── afterExecuteOperationTransaction      │
└────────────────┬─────────────────────────┘
                 ▼
           数据入库（t_hbjm_job + _i + jobscmmul）
                 ▼
           （可选）JobHrMsgHandleOp 通知下游
```

---

## 十、各子场景耗时预估

| 子场景 | opKey | 插件数 | 典型耗时 |
|---|---|---|---|
| 列表展示 | - | 5 list 插件 | < 200ms |
| 新增 / 修改 + save | save | 10 | 300-500ms |
| 提交 | submit | 8 | 200-400ms |
| 审核 | audit | 4 | 150-300ms |
| 启用 | enable | 3 | 100-200ms |
| 禁用 | disable | 3 | 100-200ms |
| 删除 | delete | 5 | 200-300ms |
| 确认变更 | confirmchange | 3 | 200-300ms |
| 新增数据版本 | newhisversion | 0 业务插件 | < 100ms（仅校验） |
| HIES 导入 | importdata_hr | 1 自定义 | 秒级（按文件大小） |

---

**📌 来源追溯**：
- 61 opKey 总览：`_auto_operations.md` L10-L71
- save 10 插件链：`_auto_operations.md` L99-L113 / `rules_chain_all.json` L305-L366
- audit 4 插件：`_auto_operations.md` L157-L164
- enable 3 插件 + JobEnableValidator：`rules_chain_all.json` L657-L688
- disable 3 插件：`_auto_operations.md` L204-L211
- confirmchange 3 插件：`_auto_operations.md` L591-L605
- JobHrMsgHandleOp：`_auto_plugin_semantics.md` L13-L26
- 前端 formPlugin：`_auto_plugin_semantics.md` L29-L81
