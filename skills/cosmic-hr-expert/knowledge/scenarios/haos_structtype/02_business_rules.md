# 业务规则 · 架构类型（haos_structtype）

> **状态**: 🟢 基于反编译 7 类实证
> **confidence**: verified
> **规则数**: 6 条（R-1 effdt 启用控制 · R-2 chgname 单选校验 · R-3 chgname 启用态校验 · R-4 删除级联清理 · R-5 新建自动创建元数据/菜单/规则 · R-6 禁用级联禁用下游）

---

## R-1 · effdt（生效日期）仅启用状态可编辑

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin` |
| **生命周期方法** | `beforeBindData` |
| **实证来源** | StructTypeEditPlugin.java 反编译 |
| **触发时机** | 进入编辑视图时（EDIT 状态）|
| **影响字段** | `effdt` |

### 业务语义

编辑已有架构类型时，根据 `enable` 字段状态决定 `effdt`（生效日期）字段是否可编辑：
- `enable = "10"` → effdt 可编辑（`setEnable(true)`）
- `enable != "10"` → effdt 禁用（`setEnable(false)`）

同时通过 `StructTypeHelper.setTips(view, "haos_structtype", ["name", "effdt"])` 设置字段提示信息。

### 反编译实证代码

```java
// StructTypeEditPlugin.java - beforeBindData
if (OperationStatus.EDIT.equals(status) && structTypeData != null) {
    String enable = structTypeData.getString("enable");
    if (!"10".equals(enable)) {
        this.getView().setEnable(Boolean.valueOf(false), new String[]{"effdt"});
    } else {
        this.getView().setEnable(Boolean.valueOf(true), new String[]{"effdt"});
    }
}
StructTypeHelper.setTips(this.getView(), "haos_structtype", new String[]{"name", "effdt"});
```

---

## R-2 · chgname（改名操作）只允许选中单条数据

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin` |
| **生命周期方法** | `beforeItemClick`（itemKey=chgname）|
| **实证来源** | StructTypeListPlugin.java 反编译 |
| **触发时机** | 列表点击 chgname 操作前 |

### 业务语义

- 未选中数据（rows.size() == 0）→ 提示"请选择一条数据。" → 取消操作
- 选中多条数据（rows.size() > 1）→ 提示"只能选中一行数据进行操作。" → 取消操作
- 选中恰好一条 → 继续进行 R-3 校验

---

## R-3 · chgname 只有启用状态才能改名

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin` |
| **生命周期方法** | `beforeItemClick`（itemKey=chgname · R-2 通过后）|
| **实证来源** | StructTypeListPlugin.java 反编译 |

### 业务语义

查询选中记录的 `enable` 字段（`OtherStructTypeService.getStructTypeData(primaryKeyValue)`），若 `enable != "1"` → 提示"{编码+名称}：只有可用状态才能变更名称。" → 取消操作。

### 反编译实证代码

```java
DynamicObject typeDy = otherStructTypeService.getStructTypeData(primaryKeyValue);
if (typeDy != null && !"1".equals(typeDy.get("enable"))) {
    ListSelectedRow listSelectedRow = listView.getCurrentSelectedRowInfo();
    this.getView().showTipNotification(
        String.format("...只有可用状态才能变更名称。",
            listSelectedRow.getNumber() + listSelectedRow.getName())
    );
    evt.setCancel(true);
}
```

---

## R-4 · 删除时级联删除元数据/移动端元数据/菜单/业务规则

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp` |
| **生命周期方法** | `beforeExecuteOperationTransaction` + `onAddValidators` |
| **实证来源** | StructTypeDeleteDonothingOp.java 反编译 |
| **关联 opKey** | `tbldeleteallrel`（全量关联删除）|

### 业务语义

删除架构类型时，执行以下级联清理（在新建独立事务 `TX.requiresNew()` 中）：

1. 按 `metanumsuffix` 查找关联元数据（`OtherStructMetaAndMenuDataService.getMetaDataByNum()`）
2. 查找关联移动端元数据（`getMetaMobileBillByNum()`）
3. 过滤并按 `inheritpath` 递归删除子集元数据（`StructClassHelper.filterMetaData()`）
4. 调用 `StructClassHelper.deleteMetaData()` 删除元数据
5. 删除 `bos_entityobject` 实体对象记录
6. 加载应用元数据，找到对应菜单并从 `AppMetadata` 中移除，`AppMetaServiceHelper.save()` 保存
7. 删除对应的 `BizRuleLibrary`（业务规则库）、`BizRule`（业务规则）、`OpBizRuleSet`（操作业务规则集），但保留 `haos_structtype` 自身

同时通过 `onAddValidators` 注册 `StructTypeDeleteValidator` 进行删除前校验。

### 列表交互前置

`tbldeleteallrel` 操作由 `StructTypeListPlugin.beforeItemClick` 拦截（evt.setCancel(true)），弹出确认框（"删除选中的第X行记录后将无法恢复，确定要删除吗？"），用户确认后通过 `confirmCallBack` 调用 `invokeOperation("deleteallrel")` 触发真正的删除。

---

## R-5 · 新建（enable=1）时自动创建元数据/菜单/业务规则

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp` |
| **生命周期方法** | `beginOperationTransaction` + `afterExecuteOperationTransaction` |
| **实证来源** | StructTypeSaveOp.java 反编译 |

### 业务语义

新建架构类型（`enable="1"` 且 `fromDatabase=false`）时触发：

**beginOperationTransaction（事务开始）**：
1. 生成新 PK ID（`ORM.create().genLongIds()`）
2. 调用 `StructClassHelper.saveNew(dyn, pkId)` 创建 StructClass 关联
3. 在 option 变量中标记 `isNewData = "0"` 供后续阶段识别

**afterExecuteOperationTransaction（事务提交后·新事务中）**：
1. `StructClassHelper.creatMetaDataAndMenu(metaNumSufFix, dataEntity)` 创建元数据页面和菜单
2. `StructClassHelper.saveStructConfig(dataEntity, metaNumSufFix, emptyMap, defaultStructType, bosObjectForMap)` 保存架构配置
3. `StructTypeIETempHelper.addTemplate(dataEntity)` 添加 IE 导入模板
4. `StructClassHelper.creatBizRule(dataEntity, ...)` 创建业务规则，并批量保存 bizRule/bizRuleLibrary/opBizRuleSet
5. 设置成功消息（提示到 HR 基础服务云授权）

**成功消息**：
> 系统已在"组织管理>其他形态组织"下新增了二级菜单:"{typeName}", 请前往"HR基础服务云>HR通用服务-权限"建立该类型对应的维度和权限维度映射，并给用户授权，授权后才可在该菜单下维护组织架构信息。

**EnableOp 同理**：`StructTypeEnableOp.afterExecuteOperationTransaction` 也执行相同的元数据/菜单/规则创建逻辑，用于启用时恢复（与 SaveOp 代码结构一致）。

---

## R-6 · 禁用时级联禁用下游组织和矩阵组织

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp` |
| **生命周期方法** | `beginOperationTransaction` |
| **实证来源** | StructTypeDisableOp.java 反编译 |

### 业务语义

禁用架构类型时，通过 `OtherAdminOrgService` 级联禁用：
1. `getOtherAdminOrgData(otClassifyId)` → 查询所有引用本架构类型的 `haos_adminorg`（其他行政组织）→ 设 `enable="0"` → `saveOtherAdminOrgData()`
2. `getStructureDataById(otClassifyId)` → 查询所有关联的 `haos_structure`（矩阵组织架构）→ 设 `enable="0"` → `saveStructureData()`

**下游影响**：禁用一条 `haos_structtype` 记录会导致其下所有其他行政组织数据和矩阵组织数据全部变为禁用状态（`enable="0"`）。

---

## 规则触发路径速查

| 操作 | 触发规则 | 实现层 |
|---|---|---|
| 打开编辑视图（EDIT）| R-1（effdt 启用控制）| FormPlugin.beforeBindData |
| 列表点击 chgname | R-2（单选校验）→ R-3（启用态校验）| FormPlugin.beforeItemClick |
| 列表点击 tbldeleteallrel | 拦截弹确认框 → confirmCallBack 调 invokeOperation("deleteallrel")| FormPlugin |
| 执行 deleteallrel | R-4（级联删除元数据/菜单/规则）| OP.beforeExecuteOperationTransaction |
| 执行 save（新建 enable=1）| R-5（自动创建元数据/菜单/规则）| OP.beginOp + afterExecuteOp |
| 执行 disable | R-6（级联禁用下游）| OP.beginOperationTransaction |
| 执行 enable | 类似 R-5（创建元数据/菜单/规则）| OP.afterExecuteOperationTransaction |
| 执行 chgname | 同步元数据名/菜单名（多语言）| OP.afterExecuteOperationTransaction |

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin -->

## chgaction 实证补充（StructTypeEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin -->

## chgaction 实证补充（StructTypeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp -->

## chgaction 实证补充（StructTypeSaveOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | ex.getMessage() |
| `StructClassHelper_0` | 创建元数据失败，请联系管理员。 |
| `StructClassHelper_1` | 创建菜单失败，请联系管理员。 |
| `StructClassHelper_4` | 编码后缀已达到上限，请到公共设置-编码规则：“编码后缀编码规则”，调整该编码规则,编码规则配置最大支持3位，请注意！ |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp -->

## chgaction 实证补充（StructTypeDisableOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeEnableOp -->

## chgaction 实证补充（StructTypeEnableOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeEnableOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeEnableOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | ex.getMessage() |
| `StructClassHelper_0` | 创建元数据失败，请联系管理员。 |
| `StructClassHelper_1` | 创建菜单失败，请联系管理员。 |
| `StructClassHelper_4` | 编码后缀已达到上限，请到公共设置-编码规则：“编码后缀编码规则”，调整该编码规则,编码规则配置最大支持3位，请注意！ |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp -->

## chgaction 实证补充（StructTypeDeleteDonothingOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | message |
| `StructClassHelper_0` | 创建元数据失败，请联系管理员。 |
| `StructClassHelper_1` | 创建菜单失败，请联系管理员。 |
| `StructClassHelper_4` | 编码后缀已达到上限，请到公共设置-编码规则：“编码后缀编码规则”，调整该编码规则,编码规则配置最大支持3位，请注意！ |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeChgNameOp -->

## chgaction 实证补充（StructTypeChgNameOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeChgNameOp`
> 跨类追踪: 18 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeChgNameOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `StructClassHelper_0` | 创建元数据失败，请联系管理员。 |
| `StructClassHelper_1` | 创建菜单失败，请联系管理员。 |
| `` | result.get("message") == null ? "" : result.get("message").toString() |
| `StructClassHelper_4` | 编码后缀已达到上限，请到公共设置-编码规则：“编码后缀编码规则”，调整该编码规则,编码规则配置最大支持3位，请注意！ |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeChgNameOp -->
