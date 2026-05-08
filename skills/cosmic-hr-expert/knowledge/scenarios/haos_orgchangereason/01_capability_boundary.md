# 能力边界 · 行政组织变动原因（haos_orgchangereason）

> **状态**: 🟢 基于反编译 2 类 + 49 opKey + 标品插件 35 plugin 实证
> **confidence**: verified
> **业务定位**：基础资料字典（不是单据 · 不是时序）· 跟 haos_changescene 配对维护

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

### 列表展示 · `ChangeReasonListPlugin` 实证（薄壳 · 27 行）
- ✅ 强制隐藏 id=1010L 那一条（标品业务约定 · INV-CR-04 · 见 `ChangeReasonListPlugin.java:21`）
- ✅ 标题透传（beforeShowBill 复用列表标题 · `ChangeReasonListPlugin.java:24-26`）
- ✅ 走平台 BaseData 默认列表过滤（按 enable=1 + ctrlstrategy 控制策略）
- ✅ 支持 HIES 列表导入导出（6 个 HIES opKey · 标品自动注入）

### 新增/编辑 · 标品 HRBaseDataTplEdit 模板
- ✅ `name` 必填校验（标品 BasedataField 平台层 · 多语言）
- ✅ `number` 由 CodeRule 自动生成（如配置 · 否则用户填）
- ✅ `description` / `simplename` 多语言字段
- ✅ 多组织字段（createorg / org / useorg / srccreateorg）走 OrgField 平台默认
- ✅ ctrlstrategy 控制策略（基础资料统一 · 走 BdCtrlStrtgyShowLogicPlugin）
- ✅ otclassify 组织团队分类引用 → haos_otclassify

### 保存 · `BaseDataBuOp` 实证（薄壳 · 23 行）
- ✅ 注册 `CtrlStrategyValidator`（控制策略合规校验 · `BaseDataBuOp.java:19-22`）
- ✅ 走平台 8 插件链（CodeRuleOp + BaseDataSavePlugin + BdVersionSaveServicePlugin + 3 个 HRBaseData* + BaseDataBuOp + HRBaseOriginalOp · 见 `opkeys/save.json`）
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
- ✅ 名称版本（BdVersion* · t_haos_orgchangereason_l 多语言版本）

---

## 三、❌ 标品不支持（需要定制 · 必须 ISV 实现）

### 业务规则缺口
- ❌ **删除前的反向引用校验**（INV-CR-06 缺口 · CS-02 解决）
  - 标品 delete 链 4 个插件都不查 `haos_changescene.changereason` 子表 / `homs_orgbatchchgbill` 是否引用过
  - 这是基础资料场景的常见盲点 · 必须 ISV 加
- ❌ **禁用前的下游使用情况提醒**（同上 · CS-02 同源解决）
  - 禁用某条变动原因前 · 没人告诉用户"还有 N 条 changescene / 申请单引用了这个值"
- ❌ **基础资料变更通知下游**（CS-05 反指引 · 见后）
  - 标品没在 afterExecuteOperationTransaction 阶段发 BEC（grep 0 命中实证）
  - ISV 想做"修改 changereason name 后下游通知" → 自建 BEC 发布方 · 但**强烈不推荐**（见 §六 BEC 反指引）

### 字段缺口
- ❌ 没有"业务原因分类" / "影响范围标签"（业务有需要 → CS-01 加 ISV 字段）
- ❌ 跟 haos_changescene 是 MulBasedata 多对多 · 没有"建议组合"提示（业务上"战略调整"原因常配"组织合并"场景 · 系统不会主动提示）

### 列表展示缺口
- ❌ 不支持按"使用次数"排序 / 高亮高频原因（用户得自己记忆"哪些原因常用"）
- ❌ 默认 1010L 主键被列表层强制隐藏（INV-CR-04） · 业务上没法 UI 切换显示（参考 CS-04 列表过滤定制思路）

### 集成缺口
- ❌ 没有"基础资料配置审计"（谁加的"业务整合"原因 · 何时启用）— 走平台日志 t_hbp_blog 间接查
- ❌ 没有跟 ERP / OA 的字典同步入口（业务需要 · 走 OpenAPI 或定制服务）

---

## 四、🔧 可通过配置实现（无需代码）

### 编码规则
- 通过【编码规则基础资料】配置 number 自动生成（PR-006 · 不要写自定义 OP）

### 列表配置
- 默认显示列、列宽、排序：**管理 → 列表配置**
- 默认筛选条件：**同上**（但 ChangeReasonListPlugin 的硬编码 `id != 1010L` 优先级最高）

### 表单配置
- 字段可见性、只读性：**表单设计器**（注意 issyspreset=true 行的字段都受 isvCanModify 限制）
- 字段默认值：**设计器属性面板**

### 业务字典维护
- 直接在【组织管理 / 行政组织维护 / 组织基础设置 / 变动原因】列表新增 / 修改 / 禁用
- 新建一类自定义变动原因（如"战略性裁员"）：填 number / name / otclassify → 保存

### 控制策略
- 通过 ctrlstrategy 字段（ComboField）配置该原因的可见组织范围（基础资料平台默认行为）

---

## 五、💻 必须通过插件扩展（CS 索引）

| 需求类型 | 推荐扩展点 | CS |
|---|---|---|
| 加 ISV 业务字段（如 业务分类 / 影响标签）| `modifyMeta(op=add field)` | CS-01 |
| 删除前查 `haos_changescene.changereason` + `homs_orgbatchchgbill` 引用 | `onAddValidators@delete` + `onAddValidators@disable` | CS-02 |
| 控制策略联动可见组织（标品已有 · ISV 增强）| `propertyChanged@ctrlstrategy` | CS-03 |
| 编码规则定制 | 走平台 CodeRule 配置 | CS-04 |
| 自建子表行 id / 业务编码 | `kd.bos.id.ID.genLongId/genStringId` | CS-05 |
| ⚠ 跨模块通知（基础资料变更）| 反指引 - 不要在本表加 BEC 发布方 | （CS-05 文末反指引）|

---

## 六、BEC 模式判定（重要 · 反指引）

> **grep 实证**（铁律 5）：
> ```bash
> grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
>   knowledge/_sdk_audit/_decompiled/scenarios/haos_orgchangereason/
> # 0 处命中
> ```

**结论**：
- ✅ **标品不在本场景发 BEC** · 也不订阅 BEC
- ✅ 任何"基础资料 CRUD → 跨模块通知"逻辑 · 标品都没做
- ❌ **ISV 不应该在本场景挂 BEC 订阅方**（订阅啥都收不到 · 标品根本没发）
- ⚠ **ISV 也不推荐在本场景挂 BEC 发布方**（基础资料变更频率低 · 性能不是问题 · 直接在 admin_org / homs_orgbatchchgbill 业务链上拿就够 · 详见 CS-05 反指引）

---

## 七、本场景能/不能做的二维表

| 场景 | 能做 | 不能做 / 必须定制 |
|---|---|---|
| 业务字典维护 | ✅ 增/删/改/启用/禁用 / 提交 / 审核 | ❌ 删除前自动校验下游引用 → CS-02 |
| 业务规则配置 | ✅ otclassify 分类 + ctrlstrategy 控制策略 | ❌ 自定义业务分类标签 → CS-01 |
| 列表过滤 | ✅ 默认隐藏 1010L + 走平台 enable 过滤 | ❌ 自定义筛选维度（如按高频展示）→ ISV 列表插件 |
| 字段联动 | ✅ ctrlstrategy → 可见组织（标品 BdCtrlStrtgyShowLogicPlugin）| ❌ 自定义业务联动 → CS-03 |
| 编码规则 | ✅ 平台 CodeRule 配置 | ❌ 按字段值动态切换前缀 → 极少 · 优先 PR-006 业务侧配 |
| 跨模块通知 | ❌ 标品根本没发 BEC | ⚠ 不推荐 ISV 自建 BEC（CS-05 反指引）|
| 反向引用查询 | ❌ 标品没提供 | ✅ 走 refentity_reverse 自建（CS-02 内嵌）|
| 历史审计 | ✅ HRBaseDataLogOp 写日志（save/disable/enable）| ❌ 字段级版本对比 → 走 HRBaseOriginalOp 间接 |

---

## 八、跟其他场景的能力对比

| 维度 | haos_orgchangereason（本场景）| haos_changescene（配对场景）| haos_adminorg（行政组织）|
|---|---|---|---|
| ModelType | BaseFormModel（基础资料）| BaseFormModel（基础资料）| HisModel（时序）|
| 物理表 | 1 主表 + 1 多语言（标品 metadata 实证 t_haos_orgchangereason）| 1 主表 + 1 多语言 + 2 MulBasedata 子表 | 1 主表 + 4 视图共表 |
| opKey 数 | 49（17 业务 + 32 HIES/系统）| 49（18 业务 + 31 HIES/系统）| 70+ |
| 反编译类 | **2（两个都是薄壳 · 27 行 + 23 行）** | 3（薄壳）| 14（深度业务）|
| 字段数 | 28 | 31 | 50+ |
| BEC | 无（grep 0）| 无（grep 0）| 有（异步派单）|
| 业务复杂度 | 低（字典）| 低（字典）| 高（核心数据）|
| 反编译总行数 | **50** | 139 | 数千行 |

**本场景定位**：业务复杂度**最低**（比配对的 haos_changescene 还轻）· 50 行薄壳实证 · 但**下游耦合度高**（被 haos_changescene 多选引用 + 间接被 7+ entry 调整单引用）· 要点是"维护字典纪律 + 拦下游孤儿引用"。

---

## 九、跟 haos_changescene 的配对说明

业务上 "变动场景 + 变动原因" 是耦合的双字典：
- `haos_changescene.changereason` 字段（MulBasedataField）→ 引用 `haos_orgchangereason.id`（多对多 · 子表 t_haos_cschangereason）
- 即"变动场景'拆分组织'通常配多个原因（'战略调整' / '业务整合'）"

**改本场景某条数据 → 配对的 haos_changescene 多选关系子表 t_haos_cschangereason 受影响**：
- 删除某条 changereason → t_haos_cschangereason 该行游离 · changescene 多选展示空
- 禁用某条 changereason → 已落地的多选关系保留 · 新建 changescene 时多选不到该项

→ **CS-02 校验逻辑必查 t_haos_cschangereason 子表 + homs_orgbatchchgbill 间接路径**（详见 CS-02）。
