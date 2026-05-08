# 能力边界 · 行政组织变动场景（haos_changescene）

> **状态**: 🟢 基于反编译 3 类 + 49 opKey + 标品插件 36 plugin 实证
> **confidence**: verified
> **业务定位**：基础资料字典（不是单据 · 不是时序）

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

### 列表展示 · `ChangeSceneListPlugin` 实证
- ✅ 按 `otclassify` 默认 1010L (ADMINISTRATIVE) 过滤展示
- ✅ 强制隐藏 id=1070L 那一条（标品业务约定 · INV-CS-04）
- ✅ 搜索 orgchangetype 时排除 orinumber=1100_S（INV-CS-05）
- ✅ 标题透传（beforeShowBill 复用列表标题）
- ✅ 跟其他业务跳转传 `customParam.otclassify` 切换分类视图

### 新增/编辑 · `ChangeSceneEditPlugin` 实证
- ✅ orgchangetype 必填校验（平台 BasedataField）
- ✅ 选 orgchangetype 自动联动 changeoperat（标品业务规则 INV-CS-02 · formRule 2=L0QS6DW0M4 ✅启用）
- ✅ 多语言字段（name / simplename / description / oriname）
- ✅ 多组织字段（createorg / org / useorg）
- ✅ MulBasedata 多选字段（changereason / changeoperat）

### 保存 · `ChangeSceneSaveOp` 实证
- ✅ 注册 ChangeSceneImportValidator（导入路径数据校验）
- ✅ 走平台 8 插件链（CodeRuleOp + BaseDataSavePlugin + 5 个 HRBaseData* + 本场景 ChangeSceneSaveOp）
- ✅ 编码规则可在【编码规则基础资料】配置（PR-006）
- ✅ 导入路径自动按 orgchangetype 兜底联动 changeoperat

### 状态流转
- ✅ status: A→B→C（save→submit→audit）走平台 BaseData 链
- ✅ enable: 1↔0（disable / enable）走平台 BaseData 链
- ✅ 禁用自动写 disabler / disabledate

### HIES 导入导出（6 个 opKey · 标品自动注入）
- ✅ `importdata_hr` / `show_import_record_hr`
- ✅ `export_from_list_hr` / `export_from_impttpl_hr` / `export_from_expttpl_hr`
- ✅ `show_export_record_hr`

---

## 三、❌ 标品不支持（需要定制 · 必须 ISV 实现）

### 业务规则缺口
- ❌ **删除前的反向引用校验**（INV-CS-06 缺口 · CS-02 解决）
  - 标品 delete 链 4 个插件都不查 homs_orgbatchchgbill / haos_adminorgdetail 是否引用过
  - 这是基础资料场景的常见盲点 · 必须 ISV 加
- ❌ **禁用前的下游使用情况提醒**（同上 · CS-02 同源解决）
  - 禁用某条变动场景前 · 没人告诉用户"还有 N 张活跃单据 changescene 选了这个值"
- ❌ **基础资料变更通知下游**（CS-05 反指引 · 见后）
  - 标品没在 afterExecuteOperationTransaction 阶段发 BEC（grep 0 命中实证）
  - ISV 想做"修改 changescene name 后下游通知" → 自建 BEC 发布方 · 但**强烈不推荐**（见 §六 BEC 反指引）

### 字段缺口
- ❌ 没有"业务线分类" / "适用组织规模" 等附加分类（业务有需要 → CS-01 加 ISV 字段）
- ❌ `orgchangetype → changeoperat` 联动是硬编码 · 不能配置（业务有需要改→ CS-03 加自定义联动）

### 列表展示缺口
- ❌ 不支持按"变动原因"快速筛选（changereason 是 MulBasedata · 标品列表没默认提供）
- ❌ 默认 customParam.otclassify=1010L 写死 · 不让用户在 UI 切换（CS-04 解决）

### 集成缺口
- ❌ 没有"基础资料配置审计"（谁加的"裁员"变动场景 · 何时启用）— 走平台日志 t_hbp_blog 间接查
- ❌ 没有跟 ERP / OA 的字典同步入口（业务需要 · 走 OpenAPI 或定制服务）

---

## 四、🔧 可通过配置实现（无需代码）

### 编码规则
- 通过【编码规则基础资料】配置 number 自动生成（PR-006 · 不要写自定义 OP）

### 列表配置
- 默认显示列、列宽、排序：**管理 → 列表配置**
- 默认筛选条件：**同上**（但 ChangeSceneListPlugin 的硬编码过滤优先级更高）

### 表单配置
- 字段可见性、只读性：**表单设计器**（注意 issyspreset=true 行的字段都受 isvCanModify 限制）
- 字段默认值：**设计器属性面板**

### 业务字典维护
- 直接在【组织管理 / 行政组织维护 / 组织基础设置 / 变动场景】列表新增 / 修改 / 禁用
- 新建一类自定义变动场景（如"组织撤并"）：填 number / name / orgchangetype / changereason → 保存

---

## 五、💻 必须通过插件扩展（CS 索引）

| 需求类型 | 推荐扩展点 | CS |
|---|---|---|
| 加 ISV 业务字段（如 业务线 / 适用范围）| `modifyMeta(op=add field)` | CS-01 |
| 删除前查 homs_orgbatchchgbill 引用 | `onAddValidators@delete` | CS-02 |
| 自定义 orgchangetype → changeoperat 联动 | `propertyChanged@haos_changescene` | CS-03 |
| 列表过滤 customParam 切换 | 列表入口的 customParam 传值 | CS-04 |
| ⚠ 跨模块通知（基础资料变更）| 反指引 - 不要在本表加 BEC 发布方 | CS-05 |
| 自建子表行 id / 业务编码 | `kd.bos.id.ID.genLongId/genStringId` | CS-06 |

---

## 六、BEC 模式判定（重要 · 反指引）

> **grep 实证**（铁律 5）：
> ```bash
> grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
>   knowledge/_sdk_audit/_decompiled/scenarios/haos_changescene/
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
| 业务规则配置 | ✅ orgchangetype/changereason/changeoperat 三字典联动（标品逻辑）| ❌ 自定义业务线/规模分类 → CS-01 |
| 列表过滤 | ✅ 按 otclassify 默认过滤 / 隐藏 1070 / 搜索排除 1100_S | ❌ 用户在 UI 切换分类 → CS-04 |
| 字段联动 | ✅ orgchangetype → changeoperat（标品）| ❌ 自定义联动逻辑（如某些 type 默认带某 reason）→ CS-03 |
| 编码规则 | ✅ 平台 CodeRule 配置 | ❌ 按字段值动态切换前缀 → 极少 · 优先 PR-006 业务侧配 |
| 跨模块通知 | ❌ 标品根本没发 BEC | ⚠ 不推荐 ISV 自建 BEC（CS-05 反指引）|
| 反向引用查询 | ❌ 标品没提供 | ✅ 走 refentity_reverse 自建（CS-02 内嵌）|
| 历史审计 | ✅ HRBaseDataLogOp 写日志（save/disable/enable）| ❌ 字段级版本对比 → 走 HRBaseOriginalOp 间接 |

---

## 八、跟其他场景的能力对比

| 维度 | haos_changescene（本场景）| haos_adminorg（行政组织）| homs_orgbatchchgbill（调整申请单）|
|---|---|---|---|
| ModelType | BaseFormModel（基础资料）| HisModel（时序）| BillFormModel（单据）|
| 物理表 | 1 主表 + 1 多语言 + 2 MulBasedata | 1 主表 + 4 视图共表 | 7 entry 容器 + 子表 |
| opKey 数 | 49（18 业务 + 31 HIES/系统）| 70+ | 80+ |
| 反编译类 | 3（薄壳）| 14（深度业务）| 10+（深度业务）|
| BEC | 无（grep 0）| 有（异步派单）| 有（OrgBatchChgBillEffectOp 异步）|
| 业务复杂度 | 低（字典）| 高（核心数据）| 高（流程引擎）|

**本场景定位**：业务复杂度低 · 但**下游耦合度高**（被 7+ entry 引用）· 要点是"维护字典纪律 + 拦下游孤儿引用"。
