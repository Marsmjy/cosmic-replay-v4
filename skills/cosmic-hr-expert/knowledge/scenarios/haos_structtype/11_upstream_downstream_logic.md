# 上下游联动 · 架构类型（haos_structtype）

> **状态**: 🟢 基于反编译 7 类实证 + StructTypeDisableOp 级联实证
> **confidence**: verified
> **上游**: 无直接基础资料上游（架构类型本身是顶层字典）
> **下游**: haos_adminorg（其他行政组织）· haos_structure（矩阵组织）· 动态元数据/菜单/业务规则

---

## 一、上游链路

本场景 `haos_structtype` 是其他形态组织体系的顶层字典，**无标准基础资料上游**（不引用其他业务基础资料的 BasedataField）。

上游依赖的系统资源：
- `ORM` / `MetadataServiceHelper`：生成 PK 和加载元数据类型（`StructTypeSaveOp`）
- `AppMetaServiceHelper`：加载/保存 DevPortal AppMetadata（菜单管理）
- `OtherStructTypeService`：标品 Service 层（禁继承 · 仅标品使用）

---

## 二、下游链路

### 2.1 haos_adminorg → haos_structtype（其他行政组织引用本场景）

`StructTypeDisableOp.beginOperationTransaction` 实证：

```java
OtherAdminOrgService otherAdminOrgService = new OtherAdminOrgService();
Set otClassifyId = Arrays.stream(dataEntities)
    .map(dynamicObject -> dynamicObject.getLong("id")).collect(Collectors.toSet());
// 查询所有引用了本 structtype 的 haos_adminorg 记录
DynamicObject[] adminOrgData = otherAdminOrgService.getOtherAdminOrgData(otClassifyId, "enable");
for (DynamicObject adminOrgDatum : adminOrgData) {
    adminOrgDatum.set("enable", "0");  // 级联禁用
}
otherAdminOrgService.saveOtherAdminOrgData(adminOrgData);
```

| 属性 | 值 |
|---|---|
| 下游实体 | `haos_adminorg`（其他行政组织）|
| 关联方式 | structtype 字段引用（通过 OtherAdminOrgService 查询）|
| 级联操作 | 禁用 structtype → 级联禁用所有 haos_adminorg 记录（enable=0）|
| ISV 查询模式 | `OtherAdminOrgService` 是标品 Service · ISV 不可继承 |

### 2.2 haos_structure → haos_structtype（矩阵组织引用本场景）

```java
DynamicObject[] structureData = otherAdminOrgService.getStructureDataById(otClassifyId, "enable");
for (DynamicObject structureDatum : structureData) {
    structureDatum.set("enable", "0");  // 级联禁用
}
otherAdminOrgService.saveStructureData(structureData);
```

| 属性 | 值 |
|---|---|
| 下游实体 | `haos_structure`（矩阵组织架构维护）|
| 关联方式 | structtype 字段引用（通过 OtherAdminOrgService 查询）|
| 级联操作 | 禁用 structtype → 级联禁用所有 haos_structure 记录（enable=0）|

### 2.3 动态元数据/菜单/业务规则（由 metanumsuffix 命名空间关联）

本场景最特殊的下游是**动态创建的资源**：

| 资源类型 | 创建时机 | 删除时机 | 关联标识 |
|---|---|---|---|
| `bos_entityobject` 元数据 | 新建+启用 | 删除 OP | `metanumsuffix` 命名后缀 |
| 移动端元数据 | 新建+启用 | 删除 OP | basedatafield.id 关联 |
| DevPortal 菜单 | 新建+启用 | 删除 OP | structtype id + metanumsuffix |
| BizRuleLibrary/BizRule/OpBizRuleSet | 新建+启用 | 删除 OP | BizRuleLibraryEnum + metanumsuffix 后缀 |
| IE 导入模板 | 新建+启用 | 不显式删除 | StructTypeIETempHelper 维护 |
| StructConfig 架构配置 | 新建+启用 | 随主表删除 | dataEntity.id 关联 |

---

## 三、boid / id 区分（PR-009）

**本场景** `haos_structtype` 是 **BaseFormModel**（非时序），物理表无 `fboid` 列：
- `id` 是架构类型记录的唯一标识（业务维度 = 版本维度合一）
- 不存在 `boid` 与 `id` 的区分

**下游 `haos_adminorg`**：需确认是否 HisModel（haos_adminorghis 是 HisModel · haos_adminorg 为其他形态行政组织 · 与行政组织的 haos_adminorg 不同）。查询时若是 HisModel 需加 `iscurrentversion=true`（PR-009）。

**下游 `haos_structure`**：矩阵组织 · 是否为 HisModel 需通过 scene_doc.json 确认。标品通过 `OtherAdminOrgService.getStructureDataById()` 查询时未加 iscurrentversion 过滤，推断为 BaseFormModel。

---

## 四、BEC 模式判定（标品无 BEC）

grep 实证：7 个反编译文件中无 `triggerEventSubscribe`、`IEventService`、`EventServiceHelper` 调用。

**结论**：
- 标品**不在本场景发任何 BEC**
- 标品**不在本场景订阅 BEC**
- 下游变更通知通过 `OtherAdminOrgService` 同步级联，不走 BEC 事件总线

**ISV BEC 反指引**：
- ISV 不应在本场景订阅 BEC（标品没发 · 收不到）
- 若 ISV 需跨系统通知，应在并列挂的 OP 插件 `afterExecuteOperationTransaction` 中直接调用目标服务

---

## 五、上下游联动全景图

```
（无上游基础资料）
        ↓ 新建/启用
haos_structtype（本场景 · 架构类型字典）
  ├── metanumsuffix 命名空间 ─→ bos_entityobject 元数据
  ├── metanumsuffix 命名空间 ─→ DevPortal 菜单（二级菜单）
  ├── metanumsuffix 命名空间 ─→ BizRuleLibrary/BizRule/OpBizRuleSet
  ├── id ─────────────────→ haos_adminorg（其他行政组织 · 禁用时级联禁用）
  └── id ─────────────────→ haos_structure（矩阵组织 · 禁用时级联禁用）
        ↓ 改名
  ├── metanumsuffix ──────→ bos_entityobject.name（元数据名）
  └── id ─────────────────→ DevPortal 菜单名（多语言）
```

---

## 六、操作与下游影响速查

| 操作 | 影响下游 | 影响范围 |
|---|---|---|
| 新建（enable=1）| ✅ 高 | 创建元数据/菜单/规则 |
| 改名（chgname）| ✅ 高 | 同步元数据名/菜单名 |
| 禁用 | ✅ 极高 | 级联禁用 haos_adminorg + haos_structure |
| 启用 | ✅ 高 | 重建元数据/菜单/规则 |
| 删除 | ✅ 极高 | 删除元数据/菜单/规则 · 不可逆 |
| 修改 effdt/org | ❌ 低 | 仅本记录 |

---

## 七、下游引用查询（refentity_reverse · BIZ-down）

通过 `refentity_reverse.json` 可查询哪些场景引用了 haos_structtype：

```json
// refentity_reverse.json 中 haos_structtype 的引用记录（实证）
"haos_structtype": [
  {"form": "haos_adminorg", "field": "structtype", "type": "BasedataField", "cnName": "架构类型"},
  {"form": "haos_structure", "field": "structtype", "type": "BasedataField", "cnName": "架构类型"}
]
```

ISV 禁用前引用检查（被引用 = 不可禁用）：

```java
// 查询哪些 haos_adminorg 引用了本 structtype（BaseFormModel · 不需 iscurrentversion）
QFilter qf = new QFilter("structtype", "=", structtypeId)
    .and(new QFilter("enable", "=", "1"));
boolean isReferenced = new HRBaseServiceHelper("haos_adminorg").isExists(qf);
```

---

## 八、BEC 模式（PR-011 · 本场景标品无 BEC）

**grep 实证**：

```bash
grep -rE "triggerEventSubscribeJobs|IEventService|KDBizEvent" \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_structtype/ 
# 结果：0 处命中
```

**结论**：
- ✅ 标品**不发 BEC 事件**（triggerEventSubscribeJobs grep 0）
- ✅ 标品**不订阅 BEC**（IEventServicePlugin / handleEvent grep 0）
- ❌ ISV 不应订阅本场景的 BEC（标品没发，收不到）

ISV 如需在架构类型保存后通知下游，应自行在 OP 插件的 afterExecuteOperationTransaction 中发 BEC（PR-011），并需要预先在 BEC 管理界面配置 eventNumber：

```java
// ISV 如需 BEC 发布（非标品功能 · ISV 自建）
public class TdkwStructTypeSavedHandler implements IEventServicePlugin {
    @Override
    public void handleEvent(String eventNumber, Map<String, Object> eventData) {
        // 处理架构类型相关业务事件（订阅方）
    }
}
```

**PR-003 补充**（OP entity.set · FormPlugin setValue）：

```java
// OP 层（StructTypeSaveOp）用 entity.set（PR-003）
entity.set("number", generatedNumber);
entity.set("enable", "1");

// FormPlugin 层（StructTypeEditPlugin.propertyChanged）用 setValue（PR-003）
this.getModel().beginInit();  // PR-004 防死循环
this.getModel().setValue("fieldName", value);
this.getModel().endInit();
```

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 02 沉淀）

> 本场景的业务语义补充见 [PPT02_DEEP_TRACE.md](../../docs/PPT02_DEEP_TRACE.md)
> - 16 实体清单（含历史模型类型/物理表）
> - 7 个标品定时任务（含 haos_func_orgsync_SKDP_S 同步平台）
> - 30+ OpenAPI（行政组织/岗位/职位查询保存等）
> - 5 SDK 扩展点（IAfterEffectAdminOrgExtPlugin / IAdminOrgTreeLabelExtPlugin 等）
> - 综合参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md) 总论金字塔

### 关键 SDK Helper（按 org_dev 常用）

```java
HAOSServiceHelper   // 提供新增/变更/启用/禁用组织
HBJMServiceHelper   // 提供新增/变更/启用/禁用职位
HBPMServiceHelper   // 提供新增/变更/启用/禁用岗位
```

### 业务事件订阅点

```
haos.adminOrgChangeEvent           组织变动事件
hbpm.standarpositionChangeEvent    标准岗位变动事件
hbpm.positionChangeEvent           岗位变动事件
hbjm_jobhr.change                  职位变动·生效
```

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/org_dev_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：1 个本场景实体 · 共 1 处引用 · 其中 0 处跨云。

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
