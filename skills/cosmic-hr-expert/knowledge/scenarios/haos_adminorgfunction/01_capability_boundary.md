# 能力边界 · 行政组织职能（haos_adminorgfunction）

> **状态**: 🟢 基于反编译 2 类 + 49 opKey + 标品插件 35 plugin + listRules 1 条 实证
> **confidence**: verified
> **业务定位**：基础资料字典（不是单据 · 不是时序）· 跟 haos_orgchangereason 双胞胎模式 · 直接被 haos_adminorg 引用

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

### 列表展示 · `ListOrderCommonPlugin` 实证（薄壳 · 19 行）
- ✅ 默认排序 `enable desc, number asc` · 启用项排前面 + 编码升序（INV-AF-07 · 见 `ListOrderCommonPlugin.java:15-18`）
- ✅ 走平台 BaseData 默认列表过滤（按 enable=1 + ctrlstrategy 控制策略）
- ✅ 支持 HIES 列表导入导出（6 个 HIES opKey · 标品自动注入）
- ✅ **不加 QFilter**（跟 haos_orgchangereason 的 ChangeReasonListPlugin 区别 · 那个加 `id != 1010L`）
- ✅ **没 beforeShowBill 标题透传**（同上 · 跟 haos_orgchangereason 区别）

### 新增/编辑 · 标品 HRBaseDataTplEdit 模板
- ✅ `name` 必填校验（标品 BasedataField 平台层 · 多语言）
- ✅ `number` 由 CodeRule 自动生成（如配置 · 否则用户填）
- ✅ `description` / `simplename` 多语言字段
- ✅ 多组织字段（createorg / org / useorg / srccreateorg）走 OrgField 平台默认
- ✅ ctrlstrategy 控制策略（基础资料统一 · 走 BdCtrlStrtgyShowLogicPlugin）
- ✅ **listRules formRule 拦预置数据修改**（INV-AF-04 · 系统预置数据进入编辑视图所有字段变只读）⭐ 本场景特有

### 保存 · `BaseDataBuOp` 实证（薄壳 · 23 行）
- ✅ 注册 `CtrlStrategyValidator`（控制策略合规校验 · `BaseDataBuOp.java:17-23`）
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
- ✅ 名称版本（BdVersion* · t_haos_adminorgfunction_l 多语言版本）
- ✅ 分配 / 取消分配（assign/unassign · 走 ctrlstrategy=8 同分配范围）

---

## 三、❌ 标品不支持（需要定制 · 必须 ISV 实现）

### 业务规则缺口
- ❌ **删除前的反向引用校验**（INV-AF-06 缺口 · CS-02 解决）
  - 标品 delete 链 4 个插件都不查 `t_haos_adminorg.fadminorgfunctionid`（admin_org 的 adminorgfunction 字段）
  - 这是基础资料场景的常见盲点 · 必须 ISV 加 · **比 haos_orgchangereason 的 CS-02 还简单**（单选关系 · 直字段查 · 不走子表）
- ❌ **禁用前的下游使用情况提醒**（同上 · CS-02 同源解决）
  - 禁用某条职能前 · 没人告诉用户"还有 N 个行政组织选用了这个职能"
- ❌ **基础资料变更通知下游**（CS-05 反指引 · 见后）
  - 标品没在 afterExecuteOperationTransaction 阶段发 BEC（grep 0 命中实证）
  - ISV 想做"修改 adminorgfunction name 后下游通知" → 自建 BEC 发布方 · 但**强烈不推荐**（见 §六 BEC 反指引）

### 字段缺口
- ❌ 没有"职能分类" / "适用规模标签"（业务有需要 → CS-01 加 ISV 字段）
- ❌ 跟 admin_org 是 BasedataField **单选**关系 · 没有"建议组合"提示（业务上"研发"职能常配特定组织规模 · 系统不会主动提示）
- ❌ 没有 otclassify 分类字段（haos_orgchangereason 有 · 本场景没有 · 业务上不需要二级分类）

### 列表展示缺口
- ❌ 不支持按"使用频次"排序 / 高亮高频职能（用户得自己记忆"哪些职能常用"）
- ❌ 默认排序固定为 `enable desc, number asc` · 业务上没法 UI 切换排序维度（参考 CS-04 思路）

### 集成缺口
- ❌ 没有"基础资料配置审计"（谁加的"数据科学"职能 · 何时启用）— 走平台日志 t_hbp_blog 间接查
- ❌ 没有跟 ERP / OA 的字典同步入口（业务需要 · 走 OpenAPI 或定制服务）

---

## 四、🔧 可通过配置实现（无需代码）

### 编码规则
- 通过【编码规则基础资料】配置 number 自动生成（PR-006 · 不要写自定义 OP）

### 列表配置
- 默认显示列、列宽：**管理 → 列表配置**
- 默认排序：**已硬编码** `enable desc, number asc`（INV-AF-07 · ISV 不应重写 setFilter · 改用 customParam）

### 表单配置
- 字段可见性、只读性：**表单设计器**（注意 issyspreset=true 行的字段都受 isvCanModify 限制 + INV-AF-04 listRules 硬拦）
- 字段默认值：**设计器属性面板**

### 业务字典维护
- 直接在【组织管理 / 行政组织维护 / 组织基础设置 / 行政组织职能】列表新增 / 修改 / 禁用
- 新建一类自定义职能（如"数据科学"）：填 number / name → 保存

### 控制策略
- 通过 ctrlstrategy 字段（ComboField）配置该职能的可见组织范围（基础资料平台默认行为）

---

## 五、💻 必须通过插件扩展（CS 索引）

| 需求类型 | 推荐扩展点 | CS |
|---|---|---|
| 加 ISV 业务字段（如 职能分类 / 适用规模标签）| `modifyMeta(op=add field)` | CS-01 |
| 删除前查 `haos_adminorg.adminorgfunction` 引用 | `onAddValidators@delete` + `onAddValidators@disable` | CS-02 |
| 控制策略联动可见组织（标品已有 · ISV 增强）| `propertyChanged@ctrlstrategy` | CS-03 |
| 编码规则定制 | 走平台 CodeRule 配置 | CS-04 |
| 自建子表行 id / 业务编码 | `kd.bos.id.ID.genLongId/genStringId` | CS-05 |
| ⚠ 跨模块通知（基础资料变更）| 反指引 - 不要在本表加 BEC 发布方 | （CS-05 文末反指引）|

---

## 六、BEC 模式判定（重要 · 反指引）

> **grep 实证**（铁律 5）：
> ```bash
> grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
>   knowledge/_sdk_audit/_decompiled/scenarios/haos_adminorgfunction/
> # 0 处命中
> ```

**结论**：
- ✅ **标品不在本场景发 BEC** · 也不订阅 BEC
- ✅ 任何"基础资料 CRUD → 跨模块通知"逻辑 · 标品都没做
- ❌ **ISV 不应该在本场景挂 BEC 订阅方**（订阅啥都收不到 · 标品根本没发）
- ⚠ **ISV 也不推荐在本场景挂 BEC 发布方**（基础资料变更频率低 · 性能不是问题 · 直接在 admin_org 业务链上拿就够 · 详见 CS-05 反指引）

→ 跟双胞胎 haos_orgchangereason 完全一致（都是 grep 0）。

---

## 七、本场景能/不能做的二维表

| 场景 | 能做 | 不能做 / 必须定制 |
|---|---|---|
| 业务字典维护 | ✅ 增/删/改/启用/禁用 / 提交 / 审核 | ❌ 删除前自动校验下游引用 → CS-02 |
| 业务规则配置 | ✅ ctrlstrategy 控制策略 + 标品 listRules 拦预置数据修改 | ❌ 自定义业务分类标签 → CS-01 |
| 列表过滤 | ✅ 默认排序 enable desc + number asc · 走平台 enable 过滤 | ❌ 自定义排序维度（如按高频展示）→ ISV 列表插件 |
| 字段联动 | ✅ ctrlstrategy → 可见组织（标品 BdCtrlStrtgyShowLogicPlugin）| ❌ 自定义业务联动 → CS-03 |
| 编码规则 | ✅ 平台 CodeRule 配置 | ❌ 按字段值动态切换前缀 → 极少 · 优先 PR-006 业务侧配 |
| 跨模块通知 | ❌ 标品根本没发 BEC | ⚠ 不推荐 ISV 自建 BEC（CS-05 反指引）|
| 反向引用查询 | ❌ 标品没提供 | ✅ 走 refentity_reverse 自建（CS-02 内嵌）|
| 历史审计 | ✅ HRBaseDataLogOp 写日志（save/disable/enable）| ❌ 字段级版本对比 → 走 HRBaseOriginalOp 间接 |
| 预置数据保护 | ✅ INV-AF-04 listRules + INV-AF-03 isvCanModify 双重拦 | n/a（标品已严格保护）|

---

## 八、跟其他场景的能力对比

| 维度 | haos_adminorgfunction（本场景）| haos_orgchangereason（双胞胎）| haos_changescene | haos_adminorg（行政组织）|
|---|---|---|---|---|
| ModelType | BaseFormModel（基础资料）| BaseFormModel（基础资料）| BaseFormModel（基础资料）| HisModel（时序）|
| 物理表 | 1 主表 + 1 多语言（标品 metadata 实证 t_haos_adminorgfunction）| 1 主表 + 1 多语言 | 1 主表 + 1 多语言 + 2 MulBasedata 子表 | 1 主表 + 4 视图共表 |
| opKey 数 | 49（18 业务 + 31 HIES/系统）| 49（17 业务 + 32 HIES/系统）| 49（18 业务 + 31 HIES/系统）| 70+ |
| 反编译类 | **2（薄壳 · 19 行 + 23 行）**⭐ 最薄 | 2（薄壳 · 27 行 + 23 行）| 3（薄壳 · 139 行）| 14（深度业务）|
| 字段数 | 27 | 28 | 31 | 50+ |
| BEC | 无（grep 0）| 无（grep 0）| 无（grep 0）| 有（异步派单）|
| **listRules formRule** | **1**（issyspreset 拦）⭐ | 0 | 3 | 多 |
| 业务复杂度 | 最低（字典）| 低（字典）| 低（字典）| 高（核心数据）|
| 反编译总行数 | **42** ⭐ haos 域最薄 | 50 | 139 | 数千行 |
| 直接下游引用类型 | BasedataField 单选（直字段查）| MulBasedataField 多选（子表查）| MulBasedataField 多选（同上）| 被多个下游引用 |

**本场景定位**：业务复杂度**最低**（haos 域字典里最薄壳）· 42 行实证 · 但**下游耦合度高**（直接被 haos_adminorg 单选引用 · 影响 admin_org 域所有组织数据完整性）· 要点是"维护字典纪律 + 拦下游孤儿引用"。

---

## 九、跟 haos_orgchangereason 的双胞胎说明

业务上 "行政组织职能" 跟 "组织变动原因" 是**双胞胎模式**（同应用 · 同模板 · 同字段结构 · 几乎同反编译）：
- 都继承 `hbp_bd_orgtpl_all` 模板（带组织 + ctrlstrategy）
- 都是 27/28 字段（差 1 个 otclassify · 本场景无）
- 都是 49 opKey
- 反编译类都是 2 个薄壳（BaseDataBuOp 行为完全一致）

**关键差异**：
| 维度 | haos_adminorgfunction（本场景）| haos_orgchangereason |
|---|---|---|
| 列表插件类名 | `ListOrderCommonPlugin`（19 行 · 仅 setOrderBy）| `ChangeReasonListPlugin`（27 行 · QFilter + beforeShowBill）|
| listRules formRule | 1 条（issyspreset 拦修改）⭐ | 0 条 |
| 下游引用 | haos_adminorg.adminorgfunction（**单选**）| haos_changescene.changereason（**多选**）|
| 反向查询路径 | 直 `<field> = id`（无需 join · 但要加 `iscurrentversion` 因为 admin_org 是 HisModel）| `<field>.fbasedataid = id`（走子表 join）|
| otclassify 分类 | 无 | 有 |

→ **业务建议**：参考 haos_orgchangereason 11 md · **大量代码框架可直接复用**（CS-01/CS-03/CS-04/CS-05 几乎一致）· 仅 CS-02 反向查询路径要按本场景调整（单选 + iscurrentversion）。
