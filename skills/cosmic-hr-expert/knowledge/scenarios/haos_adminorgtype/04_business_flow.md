# 标准场景分析 · 行政组织类型（haos_adminorgtype）

> **状态**: 🟢 基于反编译 3 类 + 49 opKey + 场景对比分析 实证
> **confidence**: verified
> **定位**: 标品分析 · 对比三胞胎（haos_adminorgfunction / haos_adminorglayer / haos_orgchangereason）

---

## 一、核心标准能力（标品原生支持）

### 1.1 列表展示 · AdminorgtypeListPlugin 实证

本场景 `AdminorgtypeListPlugin`（HRDataBaseList 子类）实现 `setFilter`，强制三级排序：

```
setFilterEvent.setOrderBy("enable desc,adminorgtypestd.number asc,number asc");
```

| 排序维度 | 含义 | 优先级 |
|---|---|---|
| `enable desc` | 启用状态降序（启用项排前面）| 1（最高）|
| `adminorgtypestd.number asc` | 关联标准编码升序（同状态按标准分组）| 2 |
| `number asc` | 本身编码升序（同标准内有序）| 3 |

> **与三胞胎差异**：haos_adminorgfunction 的 ListOrderCommonPlugin 是二级排序（enable desc, number asc）；本场景多出 `adminorgtypestd.number asc` 中间维度，体现了 adminorgtypestd 的分组语义。

### 1.2 新增/编辑 · 标品 HRBaseDataTplEdit + AdminorgtypeEditPlugin

- `name` 必填校验（平台 BasedataField 多语言层）
- `number` 由 CodeRule 自动生成（如配置）
- `adminorgtypestd` 选择后自动联动 `orgpattern`（R-3 · PropertyChanged 实证）
- 被引用的记录 adminorgtypestd 字段灰化（R-2 · beforeBindData 实证）
- `issyspreset=true` 预置数据保护（R-1）

### 1.3 保存 · BaseDataBuOp + AdminOrgTypeSaveOp 双插件链

```
save opKey 执行链（核心）：
  #23 HRBaseDataStatusOp (onAddValidators + beforeExecuteOperationTransaction)
  #24 HRBaseDataLogOp (beforeExecute + afterExecute)
  #25 HRBaseDataEnableOp (beforeExecute)
  #26 BaseDataBuOp (onAddValidators → 注册 CtrlStrategyValidator)
  #27 HRBaseOriginalOp (beforeExecute)
  #28 AdminOrgTypeSaveOp (beginOperationTransaction · HIES 导入专用修正)
```

普通保存（非 HIES 导入）时，AdminOrgTypeSaveOp 的 `beginOperationTransaction` 检测到 importtype 变量为空，直接 return，不做任何修正。

### 1.4 状态流转

```
status: A → B → C
  save（save opKey） → submit（submit opKey） → audit（audit opKey）
  unaudit / unsubmit 逆流

enable: 1 ↔ 0
  disable / enable opKey（HRBaseDataEnableOp 实证）
  禁用自动写 disabler / disabledate 字段
```

### 1.5 HIES 导入导出（6 个 opKey）

- `importdata_hr`（HIES 导入 · AdminOrgTypeSaveOp 在此 opKey 执行修正）
- `show_import_record_hr`
- `export_from_list_hr`
- `export_from_impttpl_hr`
- `export_from_expttpl_hr`
- `show_export_record_hr`

---

## 二、本场景 vs 三胞胎能力对比

### 2.1 与 haos_adminorgfunction 对比（直接参照基准）

| 维度 | haos_adminorgtype（本场景）| haos_adminorgfunction | 差异原因 |
|---|---|---|---|
| 专属反编译类数 | **3**（AdminorgtypeEditPlugin + AdminorgtypeListPlugin + AdminOrgTypeSaveOp）| 2（ListOrderCommonPlugin + 共享 BaseDataBuOp）| 本场景多枚举联动逻辑 |
| 列表排序 | 三级（enable↓ / adminorgtypestd.number↑ / number↑）| 二级（enable↓ / number↑）| 本场景有 adminorgtypestd 分组维度 |
| 字段引用检查 | ✅（beforeBindData + baseDataCheckReference）| ❌ | 本场景 adminorgtypestd 下游影响大 |
| 枚举字段联动 | ✅（propertyChanged + AdminOrgTypeStdEnum）| ❌ | 本场景独有枚举业务 |
| HIES 导入修正 OP | ✅（AdminOrgTypeSaveOp）| ❌ | 本场景 importtype 修正逻辑 |
| listRules | 1 条（enabled=false）| 1 条（已启用）| 本场景 listRules 已禁用 |
| 核心业务字段 | adminorgtypestd + orgpattern | 无特殊 | 本场景字段语义更丰富 |
| 下游引用方式 | 单选 BasedataField（haos_adminorg.adminorgtype）| 单选 BasedataField（haos_adminorg.adminorgfunction）| 相同模式 |
| BEC | 无（grep 0）| 无（grep 0）| 相同 |

### 2.2 与 haos_adminorglayer 对比

| 维度 | haos_adminorgtype | haos_adminorglayer | 差异 |
|---|---|---|---|
| 枚举联动 | ✅ adminorgtypestd → orgpattern | ❌ | 本场景独有 |
| 物理表 | t_haos_adminorgtype | t_haos_adminorglayer | 独立物理表 |
| 业务语义 | 类型（如"机构"/"部门"）| 层级（如"公司"/"部门"）| 语义不同 |

### 2.3 与 haos_orgchangereason 对比

| 维度 | haos_adminorgtype | haos_orgchangereason | 差异 |
|---|---|---|---|
| 列表插件 | AdminorgtypeListPlugin（三级 setOrderBy）| ChangeReasonListPlugin（QFilter + beforeShowBill）| 本场景无 QFilter 保留主键需求 |
| 枚举联动 | ✅（propertyChanged + AdminOrgTypeStdEnum）| ❌ | 本场景独有 |
| 下游引用方式 | 单选 BasedataField | 多选 MulBasedataField（走子表 join）| 查询路径不同 |
| otclassify 分类 | ❌ | ✅ | haos_orgchangereason 独有 |
| listRules | 1 条（已禁用）| 0 条 | 不同 |

---

## 三、超出三胞胎的独有能力

### 3.1 枚举联动模式（adminorgtypestd → orgpattern）

这是本场景最核心的业务差异。标品通过 `AdminOrgTypeStdEnum` 枚举类维护 adminorgtypestd 与 orgpattern 的映射关系，在用户选择 adminorgtypestd 时自动填充 orgpattern，保证数据一致性。

> ISV 若要实现类似的"字段 A 变化自动填字段 B"联动，可参考本场景 propertyChanged 实证模式（注意 PR-004 死循环防护）。

### 3.2 baseDataCheckReference 引用检查模式

标品在 `beforeBindData` 阶段通过 `baseDataCheckReference` API 检查 adminorgtypestd 是否已被下游引用，决定是否禁用字段。这是苍穹平台提供的通用反向引用检查接口，本场景是 haos 域字典里唯一用到此接口的场景。

> ISV 若要实现"字段被引用后禁止修改"逻辑，可参考本场景 beforeBindData 实证模式。

### 3.3 HIES 导入修正模式（importtype 变量检测）

通过检测 options.variables 中的 `importtype` 变量判断是否为 HIES 导入场景，在事务中补偿修正联动字段值。这是苍穹 HR HIES 导入框架的标准集成模式。

---

## 四、标品不支持（需要定制）

### 4.1 删除/禁用前反向引用校验

标品 delete/disable 链不查 `haos_adminorg.adminorgtype` 引用。ISV 需自建 onAddValidators 并列挂插件（CS-02）。

> 与 haos_adminorgfunction CS-02 同模式，但本场景下游是 haos_adminorg.adminorgtype（单选 BasedataField · 直字段查）。

### 4.2 自定义字段联动扩展

标品只做了 adminorgtypestd → orgpattern 联动（通过 AdminOrgTypeStdEnum 枚举）。ISV 若要自定义其他字段联动，需并列挂 FormPlugin（CS-03）。

> 注意：ISV 禁止继承 `AdminorgtypeEditPlugin`（PR-001）。

### 4.3 HIES 导入扩展

标品 AdminOrgTypeSaveOp 的 importtype 修正逻辑针对 adminorgtypestd → orgpattern。ISV 若需扩展 HIES 导入时的其他修正逻辑，需并列挂 OP（CS-04），不能继承 AdminOrgTypeSaveOp（PR-001）。

---

## 五、可通过配置实现（无需代码）

| 需求 | 方案 |
|---|---|
| number 自动生成 | 【编码规则基础资料】配置（PR-006）|
| 列表显示列/列宽 | 管理 → 列表配置 |
| 字段可见性/只读性 | 表单设计器 |
| 字段默认值 | 设计器属性面板 |
| 控制策略（ctrlstrategy）| 字段配置（基础资料平台默认）|

---

## 六、opKey 总览（49 个）

与三胞胎场景保持一致，49 个 opKey 按功能分组：

| 分类 | opKey 示例 | 数量 |
|---|---|---|
| 核心 CRUD | new / modify / view / save / delete / copy | 6 |
| 状态流转 | submit / unsubmit / audit / unaudit / enable / disable / close / returndata | 8 |
| 分页/刷新 | first / previous / next / last / refresh / option | 6 |
| HIES 导入导出 | importdata_hr / show_import_record_hr / export_from_list_hr / export_from_impttpl_hr / export_from_expttpl_hr / show_export_record_hr | 6 |
| 传统导入导出 | importdata / importdetails / importtemplatelist / exportlist / exportlistbyselectfields / exportlist_expt / exportdetails / importexport_userset | 8 |
| 移动端 | mobtoolbarselect / mobtoolbarcancel | 2 |
| 基础资料专属 | namehistory / namehistoryview / assign / unassign / bdctrlchange / individuation / assign_new / auto_assign / tbl_assign_import / logview / viewonelog / saveandnew / submitandnew | 13 |

> **importdata_hr opKey 执行链**（6 个插件）包含 AdminOrgTypeSaveOp（HIES 导入修正），这是本场景区别于三胞胎的关键链路差异。
