# 业务流转 · 维度管理 (hrcs_dimension)

> **状态**: 🟢 基于反编译 4 类（DimensionNewEdit / DimensionList / DimensionDeleteOp / HRAdminStrictPlugin）+ scene_doc.json + opkeys_index.json
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI `getOpKeyClasses` (2026-04-28)

---

## 一、业务全景：维度管理要做什么

`hrcs_dimension` 解决的是 **"角色权限按什么维度切分"** 的基础配置问题。HR 管理员通过此场景定义"权限切分维度" · 这些维度后续被 `hrcs_dynascheme`（动态授权方案）、`hrcs_datarule`（数据规则）、`hrcs_dynaschemerole`（角色清单）等下游引用。

**典型业务流（从配置到使用）**：

1. **HR 管理员**进入"维度管理"菜单 → 新增维度
2. 选择 `datasource`（4 选 1：enum / basedata / hrbu / orgteam）
3. 根据 datasource 填配套字段（详见 03 模型设计第四章）
4. 暂存 → 提交 → 审核
5. **下游使用**：dimension 被 hrcs_dynascheme.condition 用作规则参数 · 或被 hrcs_dynaschemerole.dimension 用作角色清单的"成员范围切分维度"

> **关键差异点**（决定本场景与 dynascheme 不同的简单度）：
> - 一条 dimension 行不绑角色 / 不绑管理员组（被引用方）
> - 没有 HisModel 时序版本（直接改 · 没有版本回溯）
> - 没有 condition 规则配置（自身只是个"配置基础资料"）
> - 4 路 datasource 联动是核心复杂度 · 比 dynascheme.authaction 切换更繁琐

---

## 二、业务状态机

### 2.1 数据状态（`status` 字段 · BillStatusField）

```
A 暂存 ──── submit ────► B 已提交 ──── audit ───► C 已审核
   ▲                           │                        │
   │                           │ unsubmit               │ unaudit
   └───────────────────────────┘                        │
                                                         │
                                                         └ → A 暂存（unaudit 后回 A · 不是 B）
```

**关键 opKey 映射**：

| opKey | 状态变化 | 反编译实证位置 |
|---|---|---|
| `save` | 不改状态 · 但触发 datasource 校验 + ctrlentry 反查 | `DimensionNewEdit.beforeDoOperation` L272-L296 |
| `submit` | A → B | 仅挂标品 OP（HRBaseDataLogOp 等）· 无场景专属 OP |
| `unsubmit` | B → A | 仅挂 HRBaseDataLogOp |
| `audit` | B → C | 仅挂 HRBaseDataLogOp · 无场景专属 OP |
| `unaudit` | C → A | 仅挂 HRBaseDataLogOp |

⚠️ 与 dynascheme 不同 · dimension 的 audit/unaudit/submit/unsubmit **没有场景专属 OP**（dynascheme 有 DynaAuthSchemeAuditOp/SaveSubmitOp 做下游灌库）。dimension 的工作流是**纯标品** · ISV 想加 audit 后置逻辑必须挂新 OP。

### 2.2 使用状态（`enable` 字段 · BillStatusField）

```
0 已禁用 ──── enable ───► 1 已启用 / 10 启用中
   ▲                          │
   │                          │ disable
   └──────────────────────────┘
```

**关键约束**：
- `disable` 走列表二次确认（DimensionList.beforeItemClick L29-L34）· callBackId=disable_conform
- `enable` 没有二次确认 · 直接走 disable / enable opKey
- 标品平台 OP `HRBaseDataEnableOp.beforeExecuteOperationTransaction` 改 enable 字段
- `HRBaseDataStatusOp.onAddValidators` 校验单据状态合法性

### 2.3 状态字段关系

| 状态组合 | 含义 | 能否 enable/disable | 能否 delete |
|---|---|---|---|
| `status=A` 暂存 | 草稿 | ❌ | ✅（DimensionDeleteValidator 反查下游） |
| `status=B` 已提交 | 待审 | ❌ | ⚠️ 标品平台一般不让 |
| `status=C, enable=1` 启用中 | 生效 | ✅ disable | ⚠️ 必须先 disable |
| `status=C, enable=0` 已禁用 | 暂停 | ✅ enable | ✅ DimensionDeleteValidator 通过则可删 |

---

## 三、3 大核心业务流（按 opKey）

### 3.1 流程 A · 新建维度 → 提交 → 审核（典型创建链）

```
1. 用户点【新增】 → opKey=new
   触发：HRAdminStrictPlugin.preOpenForm 校验是否 HR 管理员（HRAdminStrictPlugin.java L29-L37）
        → 不是 HR 管理员 · 直接拒绝打开 · 回 "您无法访问该功能·因为您不是HR领域管理员"

2. 表单 afterBindData
   触发：DimensionNewEdit.afterBindData (L104-L143)
        - 取 model.datasource → 走 showEnumCtrl(datasource) 重置 UI/必填
        - ADDNEW 状态 · setVisible(false, "entityctrl") 隐藏 ctrlentry
        - getModel().setDataChanged(false) 防止误标"已变更"

3. 用户填字段 · 选 datasource
   触发：DimensionNewEdit.propertyChanged 监听 datasource
        - 走 showEnumCtrl(newValue) → 上文 03 第 4 章的 4 路联动矩阵

4. 用户填 entitytype（datasource=basedata 时）· F7 弹窗
   触发：DimensionNewEdit.beforeF7Select (L364-L372)
        - 强制 modeltype=BaseFormModel 过滤
        - lsp.setFormId("bos_listf7")

5. 用户选了业务对象 → propertyChanged entitytype 监听（datasource=basedata 时）
   触发：limitBasedataType 校验业务对象有 name + number 字段
        - 没有 → showTipNotification "当前业务对象无编码或名称字段，不允许配置维度。" + 清空 entitytype（beginInit/endInit）
        - 有 → 继续到 handleShowTypeForEntityType 检查继承
   触发：handleShowTypeForEntityType
        - 不继承 hbp_bd_treetpl_all → setValue(showtype, "list") + setEnable(showtype, false)
        - 继承 → setEnable(showtype, true)

6. 用户填 entry 子表（datasource=enum 时）· 输入 value / displayvalue / index / 备注
   ⚠ 此时 entry.value / entry.displayvalue 必填（showEnumCtrl L169-L170 已设）

7. 用户点【保存】 → opKey=save
   触发：DimensionNewEdit.beforeDoOperation (L272-L296)
        - setValue(modifytime, new Date())  · 强制刷新
        - datasource=hrbu → setValue(entitytype, "bos_org")
        - getPageCache().get("hadConfirm") 存在 · 跳过校验直接放行
        - checkEntitytype 5 步校验（datasource=basedata 必有 entitytype + showtype 兼容 · datasource=enum 必有 entry · displayvalue 不重复）
        - EDIT + datasource=enum → checkEnumChange 比对 DB · 改了 value 弹二次确认

8. save OP 链（按 executionChain · 6 标品 OP）
   1. CodeRuleOp                      · 编码规则（自动生成 number）· onAddValidators
   2. BdVersionSaveServicePlugin     · 基础资料版本管理 · 主表 name 写历史
   3. HRBaseDataStatusOp             · onAddValidators 校验单据状态合法
   4. HRBaseDataLogOp                · 操作日志埋点 + 落库
   5. HRBaseDataEnableOp             · enable 字段管理
   6. HRBaseOriginalOp               · 原始值记录
   ⚠ dimension 没有场景专属 SaveOp（与 dynascheme 不同 · dynascheme 有 DynaAuthSchemeOp / SaveSubmitOp）

9. 落库后回 UI 刷新 · ctrlentry 在 EDIT 状态会用 afterBindData 反查 hrcs_entityctrl 重新装填

10. 用户点【提交】 → opKey=submit
    触发：标品 OP 链（CodeRuleOp / BdVersionSaveServicePlugin / HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp · 5 标品）
         status A → B
         无场景专属逻辑

11. 用户点【审核】 → opKey=audit
    触发：HRBaseDataLogOp 单一标品（极简）
         status B → C
         无场景专属逻辑 · ISV 想加审核后置逻辑必须挂新 OP（CS-06）
```

### 3.2 流程 B · 编辑已审核维度（典型修改链）

```
1. 用户在列表选一条已审核 dimension → 双击进编辑
   触发：HRAdminStrictPlugin.preOpenForm 准入闸
   触发：DimensionNewEdit.afterBindData
        - EDIT 状态 + 反查 hrcs_entityctrl 有数据 → 显示 ctrlentry · 灌入引用清单
        - showEnumCtrl(datasource) → 启动 UI/必填重置
        - 已被引用维度 · entry value 字段全部 setEnable(false)（标品 UI 锁定）

2. 用户改 datasource（罕见 · 但 isvCanModify=false 标品锁死 · 不能改）
   ⚠ datasource 字段 isvCanModify=false · UI 通常已 disable · 用户改不了

3. 用户改 entry（datasource=enum 时）· 修改 value 或 displayvalue
   ⚠ entry value 已被引用时 · setEnable(false) · 用户改不了
      未被引用时可以改 · 但 save 时会触发 checkEnumChange 二次确认

4. 用户点【保存】 → opKey=save → checkEnumChange 弹"调整枚举值会影响角色维度·确定修改吗？"
   - 用户点 Yes → confirmCallBack(save_continue, Yes) → setPageCache("hadConfirm", "confirmed") + invokeOperation("save")
   - 第二次进 beforeDoOperation · 拿到 hadConfirm 缓存 → 跳过校验放行
   - 用户点 No → args.setCancel(true) 阻断 · 用户回去重填

5. save 落库 · 走标品 OP 链（同上）

6. 用户在工具栏点【角色清单】（refrole） → DimensionNewEdit.beforeItemClick L259-L270
   → 弹 hrcs_refdetails Modal 子页面 · 显示哪些角色引用了此维度
```

### 3.3 流程 C · 列表禁用 / 删除（运维链）

```
1. 用户在列表选一条 dimension → 点【禁用】
   触发：DimensionList.beforeItemClick L29-L34
        - itemKey=tbldisable 拦截
        - showConfirm "禁用维度后·不允许在'业务对象维度映射'中使用·已有的角色维度的数据权限不受影响"
        - callBackId=disable_conform
        - evt.setCancel(true) 先取消默认行为

2. 用户点 Yes → DimensionList.confirmCallBack L37-L44
   - HRStringUtils.equals(callBackId, "disable_conform") + Yes
   - getView().invokeOperation("disable")

3. disable opKey 链（仅 HRBaseDataLogOp 标品）
   - HRBaseDataEnableOp 改 enable=0
   - 标品不会自动清下游绑定（提示文案明确说"已有的角色维度的数据权限不受影响"）
   ⚠ 这跟 dynascheme 的 disable 一样保守 · 但 dimension 是被引用方 · ISV 加下游清理时要注意范围（CS-04）

4. 用户在列表选一条已禁用 dimension → 点【删除】
   触发：delete opKey 链
        - CodeRuleDeleteOp                    · 编码规则反向（释放编码段）
        - HRBaseDataStatusOp                  · 标品单据状态校验
        - HRBaseDataLogOp                     · 操作日志
        - DimensionDeleteOp                   · onAddValidators 注册 DimensionDeleteValidator
   - DimensionDeleteValidator 反查下游引用（推断：hrcs_entityctrl / hrcs_dynascheme.condition / hrcs_datarule）
   - 任一引用 → addErrorMessage 阻断
   - 全部无引用 → 通过 · 走标品 delete 走 t_hrcs_dimension + t_hrcs_dimensionenum + t_hrcs_dimorgclass 删除
```

---

## 四、对外暴露的子页面（modal）

### 4.1 hrcs_refdetails · 角色引用清单

**触发**：DimensionNewEdit.beforeItemClick · 工具栏 refrole 按钮

**调用方式**：
```java
FormShowParameter fsp = new FormShowParameter();
fsp.setFormId("hrcs_refdetails");
fsp.setCustomParam("dimension.id", dimensionId);
fsp.getOpenStyle().setShowType(ShowType.Modal);
this.getView().showForm(fsp);
```

**子页面职责**（推断）：根据 dimensionId 反查所有引用此维度的"角色 / 动态方案"清单 · 给 HR 用户做"哪些下游用了我"的可视化诊断。

### 4.2 enum 修改二次确认对话框

**触发**：DimensionNewEdit.checkEnumChange · EDIT + datasource=enum + entry.value 改了

**callBackId**：`save_continue`

**用户点 Yes 后**（DimensionNewEdit.confirmCallBack L298-L306）：
```java
if (callBackId.equals("save_continue") && result.equals(MessageBoxResult.Yes)) {
    this.getPageCache().put("hadConfirm", "confirmed");
    this.getView().invokeOperation("save");
}
```

⚠️ ISV 自建 Validator 不要用 callBackId="save_continue"（会跟标品冲突 · 走标品 confirmCallBack 路径）。

### 4.3 disable 列表二次确认对话框

**触发**：DimensionList.beforeItemClick · 列表 tbldisable 工具按钮

**callBackId**：`disable_conform`

**用户点 Yes 后**（DimensionList.confirmCallBack L37-L43）：
```java
if (HRStringUtils.equals(callBackId, "disable_conform") && result.equals(MessageBoxResult.Yes)) {
    this.getView().invokeOperation("disable");
}
```

⚠️ ISV 自建 ListPlugin 不要用 callBackId="disable_conform"。

---

## 五、操作前置 / 后置插件分布（按 opKey 分组）

| opKey | beforeDoOperation FormPlugin | OP onAddValidators | OP beforeExecuteOperationTransaction | OP endOperationTransaction | OP afterExecuteOperationTransaction |
|---|---|---|---|---|---|
| `save` | DimensionNewEdit | HRBaseDataStatusOp | HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp | （标品无） | HRBaseDataLogOp |
| `submit` | DimensionNewEdit（同 save 路径） | HRBaseDataStatusOp | HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp | （标品无） | HRBaseDataLogOp |
| `audit` | （无 FormPlugin 拦截） | （无） | HRBaseDataLogOp | （无） | HRBaseDataLogOp |
| `unaudit` | （无） | （无） | HRBaseDataLogOp | （无） | HRBaseDataLogOp |
| `disable` | （无 · DimensionList 拦截在 beforeItemClick 弹确认） | （无） | HRBaseDataLogOp | （无） | HRBaseDataLogOp |
| `enable` | （无） | （无） | HRBaseDataLogOp | （无） | HRBaseDataLogOp |
| `delete` | （无） | DimensionDeleteOp 注册 DimensionDeleteValidator | CodeRuleDeleteOp / HRBaseDataStatusOp / HRBaseDataLogOp | （无） | （无） |

⚠️ **dimension 几乎所有写操作都没有 endOperationTransaction 收尾** · 跟 dynascheme（DynaAuthSchemeSaveSubmitOp / AuditOp / ConfirmChangeOp 三个 endOperationTransaction）形成鲜明对比。

→ **ISV 想在 audit 后做事** · 必须挂新 OP 到 audit 的 afterExecuteOperationTransaction（PR-010）· 详见 CS-06。

---

## 六、整体流程时序图（save 链典型）

```
┌─────────────┐    ┌──────────────────┐    ┌────────────────┐    ┌───────────────┐
│ 用户 UI 点击 │ →  │ FormPlugin 链     │ →  │ Validator 链    │ →  │ OP 事务链      │
└─────────────┘    └──────────────────┘    └────────────────┘    └───────────────┘
                       ↓                          ↓                       ↓
                   beforeDoOperation         onAddValidators        beforeExecuteOperationTransaction
                       ↓                          ↓                       ↓
                   DimensionNewEdit         HRBaseDataStatusOp      HRBaseDataStatusOp
                       ↓                                            HRBaseDataLogOp
                   modifytime=now                                   HRBaseDataEnableOp
                   datasource=hrbu→entitytype=bos_org                HRBaseOriginalOp
                   checkEntitytype                                       ↓
                   checkEnumChange (EDIT+enum)                       事务提交（save）
                       ↓                                                  ↓
                   args.setCancel(false) 放行                    afterExecuteOperationTransaction
                       ↓                                                  ↓
                                                                  HRBaseDataLogOp 落日志
```

→ ISV 加业务校验：挂在 onAddValidators 阶段（自建 Validator）
→ ISV 加业务联动：挂在 beforeDoOperation FormPlugin 阶段
→ ISV 加 audit 后置：挂 audit afterExecuteOperationTransaction · 调 BEC（PR-011 · CS-05）
→ ISV 加 delete 前置：挂 delete onAddValidators 自建 Validator · 反查下游

---

## 七、HisModel 在本场景**不适用**

> grep `iscurrentversion|HisModel|boid` 在 `scene_doc.json` + `_decompiled/scenarios/hrcs_dimension/*.java` 全 0 命中

`hrcs_dimension` 不是时序基础资料 · 因此**没有以下流程**：
- ❌ 没有 confirmchange / change opKey
- ❌ 没有 boid 跨版本一致性维护
- ❌ 没有 sourcevid 链式追溯
- ❌ 没有"审核后变更"的双写

→ 改 dimension 是**直接改** · 没有版本回溯 · 这是理解 dimension 业务流转的关键认知（PR-008 / PR-009 不适用）。
