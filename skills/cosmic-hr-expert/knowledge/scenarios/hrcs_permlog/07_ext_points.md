# 07 扩展点全图 · hrcs_permlog（HR 权限日志）

> 数据源：plugins.json + form_lifecycle_rules.json + reflection 调用链 + curated_sdk.json
> 8 标品插件 · 2 类反编译 · 14 条 FP 生命周期规则 · 1 条反射调用扩展点

---

## 1. 标品插件清单（8 个 · 全部 isIsv=false）

| # | className | 父类 | jar | 生命周期 | 业务相关性 |
|---|---|---|---|---|---|
| 1 | HRBaseDataImportEdit | HRCoreBaseBillEdit | hrmp-hbp-formplugin | - | HBP 导入模板 · 跟 permlog 无关 |
| 2 | HRCertCheckEdit | HRDataBaseEdit | hrmp-hbp-formplugin | preOpenForm | 证书校验 · 通用 |
| 3 | HRBaseUeEdit | HRDataBaseEdit | hrmp-hbp-formplugin | preOpenForm | UE 导入模板 · 通用 |
| 4 | HRHiesButtonSwitchPlugin | AbstractFormPlugin | hrmp-hbp-formplugin | afterBindData | HIES 按钮显隐 · 通用 |
| 5 | ForbidUrlOpenPlugin | HRDynamicFormBasePlugin | hrmp-hbp-formplugin | preOpenForm | 禁 URL 直访 · 通用 |
| 6 | HRCertCheckList | AbstractListPlugin | hrmp-hbp-formplugin | preOpenForm | 证书校验列表 · 通用 |
| 7 | **HRAdminStrictPlugin** | HRDynamicFormBasePlugin | hrmp-hrcs-formplugin | preOpenForm | **HR 准入闸 · permlog 安全核心** |
| 8 | **PermLogListPlugin** | **HRDataBaseList** | hrmp-hrcs-formplugin | beforeDoOperation / afterDoOperation | **List 业务核心** |

> 8 中只有 7-8 是 hrcs_permlog 专属业务 · 1-6 都是 HBP 通用模板插件（跨场景共用）

---

## 2. ISV 最常覆盖的 Top 5 扩展点

### 2.1 `setFilter` (FP_SF1-SF6 · PermLogListPlugin)
- **触发点**：用户在 List 容器输入条件
- **典型用途**：CS-02 解除 hashandle 强制过滤 · 加 ISV 域过滤
- **挂载方式**：并列挂 ListPlugin · 顺序在 PermLogListPlugin **之后**（让标品先跑 4 路 OR）
- **关键 API**：`SetFilterEvent.getQFilters()` / `setCustomQFilters(List<QFilter>)`
- **风险**：必须 super.setFilter 先调 · 否则 4 路 OR 不跑

### 2.2 `afterBindData` 列脱敏（FP_FCS · 自实现）
- **触发点**：列表数据绑定完成 · 渲染前
- **典型用途**：CS-03 敏感字段脱敏（beforeopdata / afteropdata）
- **挂载方式**：实现 PackageDataListener + addPackageDataListener
- **关键 API**：`PackageDataEvent.setFormatValue(maskedStr)`
- **风险**：仅 UI 脱敏 · F12 可看原始值

### 2.3 `beforeDoOperation processlog` (FP_BDO2 · PermLogListPlugin)
- **触发点**：用户点击"处理日志"按钮
- **典型用途**：ISV 加额外校验（如时间窗内只允许处理 100 条）/ 改 i18n 错误提示
- **挂载方式**：并列挂 ListPlugin · super 后追加
- **关键 API**：`BeforeDoOperationEventArgs.setCancel(true)`

### 2.4 上游编辑场景的 `afterExecuteOperationTransaction` (CS-04 / CS-05)
- **触发点**：上游编辑场景主事务已提交
- **典型用途**：发 BEC 把 permlog 推送到外部审计 / 写到 ISV 聚合表
- **挂载方式**：并列挂上游场景的 OP 插件
- **关键 API**：`IEventService.triggerEventSubscribeJobs(...)` / `SaveServiceHelper.save(...)`
- **风险**：扫上游 grep 看是否标品已发 BEC（避免双发 · feedback_bec_3layer_async_publish）

### 2.5 `hrcs_permlogtype` 基础资料 + 自定义 handler 类（CS-07 / 反射调用）
- **触发点**：用户点 processlog · 标品按 logtype.handlerclass 反射调
- **典型用途**：ISV 加新 logtype + 自定义处理逻辑
- **挂载方式**：在 hrcs_permlogtype 基础资料里配编码 + handlerclass FQN · 不需要并列挂插件
- **关键约束**：handler 必须 public 无参构造 + public Object doHandler(long logId)
- **优势**：解耦扩展 · 不动 PermLogListPlugin · 是 permlog 最优雅的 ISV 扩展点

---

## 3. 各生命周期挂点能力一览

| 生命周期 | 标品已挂 | ISV 可加 | 典型用途 |
|---|---|---|---|
| `preOpenForm` | ✓（5 个 · 含 HRAdminStrictPlugin 准入）| ⚠ 慎重 · 别破坏准入 | F7 引用豁免扩展（极少）|
| `afterBindData` | ✓（HRHiesButtonSwitchPlugin · HIES 按钮显隐）| ✓ | UI 字段控制 / 列脱敏 |
| `beforeDoOperation` | ✓（PermLogListPlugin · processlog 单选校验）| ✓ | 业务校验 / 多选支持改造 |
| `afterDoOperation` | ✓（PermLogListPlugin · donothing_archiveset 弹页）| ✓ | 业务事件触发 |
| `billListHyperLinkClick` | ✓（PermLogListPlugin · 4 路路由）| ✓ | 自定义跳转 |
| `setFilter` | ✓（PermLogListPlugin · 4 路 OR + hashandle 强制）| ✓ | 过滤定制（最常用）|
| `filterContainerSearchClick` | ✓（PermLogListPlugin · 空 super）| ✓ | 筛选提交埋点 |
| `registerListener` | ✗ | ✓ | 挂自定义监听器 |
| `propertyChanged` | ✗（permlog 是 List 视图 · 通常不挂）| ⚠ 详情子页可挂 | 字段联动 |
| `onAddValidators` | ✗（permlog 没 OP 插件）| ✓ | 在上游 OP 加 Validator |

---

## 4. 反射调用扩展点（独有 · permlog 关键设计）

```
PermLogListPlugin.beforeDoOperation processlog
        |
        v
反射调用：Class.forName(handlerclass).newInstance().getMethod("doHandler", long).invoke(instance, logId)
        |
        | handlerclass 来自 hrcs_permlogtype 基础资料
        v
ISV 自定义 handler 实现
```

### 4.1 ISV 实现 handler 的契约

```java
package isv.xxx.permloghandler;

public class IsvCustomLogTypeHandler {
    // 必须 public 无参构造
    public IsvCustomLogTypeHandler() {}

    // 必须 public Object doHandler(long logId)
    public Object doHandler(long logId) {
        // 1. 查 permlog 行
        // 2. 业务处理（推送告警/触发审批/写自有审计表）
        // 3. 更新 hashandle = 1（一般在 handler 内做）
        // 4. 返回结果（标品忽略 · 但建议返回非 null 表示成功）
        return Boolean.TRUE;
    }
}
```

### 4.2 在 hrcs_permlogtype 基础资料里挂

通过苍穹标品维护界面或 OpenAPI insert：
- `number` = "9001"（ISV 自定义 logtype 编码）
- `name` = "ISV 自定义日志类型"
- `handlerclass` = "isv.xxx.permloghandler.IsvCustomLogTypeHandler"

### 4.3 编码集冲突避免

- 标品已用 1010-1040 / 1050-2095 / 3015 / 4010-4020
- ISV 建议从 9000+ 开始（和标品隔离）
- 不要复用现有编码 · 否则 setFilter 路由会冲突

---

## 5. ISV 不可继承清单（铁律）

| 类 | 原因 |
|---|---|
| **PermLogListPlugin** | final 类 · 标品 |
| HRBaseDataImportEdit / HRCertCheckEdit / HRBaseUeEdit / HRHiesButtonSwitchPlugin / ForbidUrlOpenPlugin / HRCertCheckList | HBP 通用模板 · 跨场景共用 · ISV 业务跟它们无关 |
| HRAdminStrictPlugin | 标品（无 final 但 final-final 类）· 11 hrcs 共用 · 删了影响其他场景 |
| HisModelOPCommonPlugin / HisUniqueValidateOp / HisModelFormCommonPlugin / HisModelListCommonPlugin | @SdkInternal · 平台内部 · permlog 非 HisModel 用不上 |
| AbsOrgBaseOp | HR 通用推荐 HRDataBaseOp · permlog 没 OP 插件 |

参考 `cosmic_hr_sdk_whitelist_audit.md` HR SDK 白名单权威版（2026-04-20）

### 推荐继承（白名单内）

| 类 | 注解 | 用途 |
|---|---|---|
| `HRDataBaseList` | @SdkPlugin | List 父类 |
| `HRDataBaseEdit` | @SdkPlugin | Edit 父类（详情子页可用）|
| `HRDataBaseOp` | @SdkPlugin | OP 父类（在上游编辑场景加 BEC 等用）|
| `HRDynamicFormBasePlugin` | @SdkPlugin | 动态表单父类 |
| `AbstractFormPlugin` | @SdkPublic | 通用表单父类 |
| `AbstractListPlugin` | @SdkPublic | 通用列表父类 |
| `AbstractValidator` | @SdkPublic | Validator 父类 |
| `AbstractTask` | @SdkPublic | 调度任务父类（CS-07 用）|

---

## 6. 标品插件链交叉影响

### 6.1 preOpenForm 链（5 个 · 顺序执行）

```
HRCertCheckEdit -> HRBaseUeEdit -> ForbidUrlOpenPlugin -> HRCertCheckList -> HRAdminStrictPlugin
```

ISV 想加自己的 preOpenForm 插件 · 默认排在最后 · 风险：

- ✗ HRAdminStrictPlugin 拒绝后 · ISV 插件 setCancel 已经触发 · 但 e.isCancel() 仍可读
- ⚠ ISV 不要 e.setCancel(false) 解除标品的拒绝 · 等于绕过准入闸（违规）

### 6.2 afterBindData 链（1 个）

```
HRHiesButtonSwitchPlugin
```

ISV 加 List 列脱敏 PackageDataListener · 不冲突

### 6.3 beforeDoOperation/afterDoOperation 链（1 个 · PermLogListPlugin）

ISV 加自己的就排在标品后

---

## 7. 引用文件

- `_auto_plugin_registry.md` · 8 标品插件
- `_auto_plugin_semantics.md` · 反编译语义
- `_decompiled/scenarios/hrcs_permlog/PermLogListPlugin.java`
- `_decompiled/scenarios/hrcs_permlog/HRAdminStrictPlugin.java`
- `form_lifecycle_rules.json` · 14 条 FP 规则
- `curated_sdk.json` · 推荐 SDK 清单
- `_shared/platform_rules.json` · PR-001/PR-010/PR-011
- `cosmic_hr_sdk_whitelist_audit.md` · 白名单铁律
