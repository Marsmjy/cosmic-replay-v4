# 推荐定制方案 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于真实 OpenAPI 实抓 + 5 类反编译类名 + `scene_doc.json` 17 字段语义整合
> **confidence**: real_deploy（所有扩展点类名均来自 `_auto_plugin_semantics.md` + `_auto_plugin_registry.md` 实证）

所有方案遵循统一结构（借鉴 Stripe / Salesforce Developer Docs）：
**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

---

## CS-01 · 给 hrcs_entityctrl 扩展自定义字段（最高频）

**关联 Pattern**：`pattern/add_field_extension/README.md`

### 需求

业务方说：业务对象映射记录上需要加"映射备注分类"字段（多选 · 可选 `重要 / 临时 / 试点 / 长期`），用于运营 BI 看板按映射用途分类统计。

### 推荐方案

- **扩展对象**：`hrcs_entityctrl`（主实体）
- **扩展点**：`modifyMeta(op=add, elementType=field)` 或 IDEA 插件 Web UI 加字段
- **风险**：低（不动业务规则 · 只是数据展示位）
- **特点**：hrcs_entityctrl 是 BillFormModel **非时序**配置基础资料 · ISV 加字段**不需要考虑** boid/iscurrentversion/sourcevid 这些时序版本控制点 —— 改了就是改了 · 没有版本快照（与 dynascheme/jobhr/admin_org 都不同）

### 调用链（3 步）

```
Step 1: getBizApps()                        // 拿 hrcs 应用 bizAppId
Step 2: modifyMeta({
  formId: "hrcs_entityctrl",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hrcs_entityctrl",
    element: {
      fieldType: "MulComboField",                       // 多选枚举
      key: "${ISV_FLAG}_remarktag",
      name: {zh_CN: "映射备注分类", en_US: "Mapping Tag"},
      mustInput: false,
      comboOptions: [
        {value: "A", name: {zh_CN: "重要", en_US: "Important"}},
        {value: "B", name: {zh_CN: "临时", en_US: "Temp"}},
        {value: "C", name: {zh_CN: "试点", en_US: "Pilot"}},
        {value: "D", name: {zh_CN: "长期", en_US: "Long-Term"}}
      ]
    }
  }]
})
Step 3: getFormSchema("hrcs_entityctrl")    // ⭐ 二次验证落库（errorCode=0 不代表成功）
```

### 代码框架（使用 cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "hrcs_entityctrl", "number": "hrcs_entityctrl"}
)

designer.add_field(
    field_type="MulComboField",
    name="映射备注分类",
    key="${ISV_FLAG}_remarktag",
    parent_entity_id=designer.base_entity_id,
    combo_options=[
        {"value": "A", "name": {"zh_CN": "重要"}},
        {"value": "B", "name": {"zh_CN": "临时"}},
        {"value": "C", "name": {"zh_CN": "试点"}},
        {"value": "D", "name": {"zh_CN": "长期"}}
    ]
)
designer.save()  # 一次 save click
```

### 踩坑

- ❌ 字段 key 不带 ISV 前缀（如直接叫 `remarktag`）→ 标品升级覆盖
- ❌ `fieldName` 列名超过 25 字符 → 苍穹平台开发规范限制 · 数据库建表失败
- ❌ `fieldName` 手动写 `fk_` 前缀 → 平台会再加 `f` → 列名变 `ffk_xxx` 怪列名 · **建议不传 fieldName 让平台按 `f + key.lowercase()` 自动生成**（坑 11）
- ❌ 多语言表 `_l` 命名规则：entityctrl 当前的 description 在主表承载 · entryentity.desc 在子表承载 · ISV 加 MuliLangTextField 时不要假设会自动建独立 `_l` 表
- ❌ 误以为 ComboField 不需要 `comboOptions` → UI 下拉框空白
- ⚠️ 加字段后 · save 主流程**不读不写** ISV 字段（标品 OP 透明处理）· ISV 字段值由用户输入 / 自建 OP 处理
- ⚠️ 加字段在 `t_hrcs_entityctrl` 主表会自动跟着 issyspreset / `bizapp` 等字段进同一行 · 不要假设它独立成行

**遵循的 PR 规范**：
- **PR-007**（预置数据不可改 · 业务自建可改）· 本 CS 加的是业务字段 · 可随时删改
- **PR-008 不适用**（本场景非 HisModel · 没有 iscurrentversion 字段）
- **PR-009 不适用**（本场景没有 boid · 下游引用走 entitytype + propkey 双键）

### 加分录字段（如给 entryentity 加"控权失效日期"）

如果 ISV 字段加在分录子表（entryentity）· 步骤：

```
modifyMeta({
  formId: "hrcs_entityctrl",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hrcs_entityctrl.entryentity",      // ⭐ 指向子实体 · 不是主实体
    element: {
      fieldType: "DateField",
      key: "${ISV_FLAG}_authexpire",
      name: {zh_CN: "控权失效日期"},
      mustInput: false
    }
  }]
})
```

⚠️ **本场景分录子表 entryentity 不带 entryboid**（与 dynascheme.roleentry 不同 · 因为非 HisModel）· ISV 加分录字段后 · 子表行的唯一性是 `fentryid` · 而不是 `(entryboid, version)` · 所以**可以放心加 isUnique=true** 类字段（不会因为版本共用 entryboid 冲突）。

### 补充：分录子表行 id / 业务流水号生成规范（PR-005）

如果本场景未来扩展涉及**自建分录子表**（如给主表加"映射变更日志"分录）· 或要写"映射编码后缀"自动生成 · 行 id / 流水号必须用 `kd.bos.id.ID`：

```java
import kd.bos.id.ID;

// 场景 1：扩展时新增分录行 · 填行 id（必须实证 · 不能自己造 UUID）
DynamicObject newRow = entries.addNew();
newRow.set("id", ID.genLongId());                // 长整型主键 · 19 位 Snowflake
newRow.set("propkey", "${ISV_FLAG}_remark");

// 场景 2：自定义业务编码后缀 / 流水号
String traceId = ID.genStringId();               // 字符串 ID · 用于 traceId / 日志追踪号
String customNumber = "ENTITYCTRL_" + ID.genStringId();
```

⚠ **反模式**（违反 PR-005 必驳回）：
- ❌ `UUID.randomUUID().toString()` · 苍穹有更轻量的 ID 接口
- ❌ `System.currentTimeMillis()` · 高并发会撞
- ❌ 自己 select max(id) + 1 · 分布式集群必坏

**遵循 PR-005 · 苍穹平台已集成分布式 ID（Snowflake）· ISV 不要自己 Redis incr 或 UUID 造**

### 验证

保存后到**HR 通用服务 / 权限管理 / 业务对象维度映射**菜单新增一条映射 · 看表单上是否出现"映射备注分类"字段。

### Pattern 复用速查

| 字段类型 | 推荐做法 |
|---|---|
| TextField (文本) | 直接加 · 字段长度按业务定（默认 200） |
| MuliLangTextField (多语言文本) | 直接加 · entityctrl 当前的 MuliLangTextField 都在主/子表自身承载 · 多语言挂主表 |
| ComboField (单选) | 必须传 comboOptions · 否则 UI 空白 |
| MulComboField (多选) | 同 ComboField · 业务上更常用于"分类"类 |
| BasedataField (基础资料关联) | 必须传 baseEntityNumber · 配合 F7 |
| MulBasedataField (多关联) | 苍穹用隐式中间表 · 加字段时 OpenAPI 抓成普通 BasedataField · 可能需 IDEA 插件 |
| DateField / DateTimeField | 直接加 |
| HRMulPositionField / HRMulAdminOrgField | OpenAPI buildMeta 不支持（kb_cosmic_buildmeta_traps.md 实证）· 走 IDEA 插件 |
| EmployeeField | OpenAPI 不支持 · 用 BasedataField 引用 hrpi_person 替代 |

---

## CS-02 · 选了"业务对象"自动带出"维度选择联动"（字段联动）

**关联 Pattern**：FormPlugin propertyChanged + beforeF7Select 模式

### 需求

业务方说：HR 部门希望按"业务对象类型"做差异化的默认维度推荐 —— 比如选了"组织相关业务对象"自动把"按管理域"作为默认维度选项加亮 · 选了"员工相关业务对象"自动把"按职位序列"作为默认维度选项加亮。同时希望维度 F7 在标品已有 6 分支过滤的基础上再加一个"按客户业务线分组的"二级过滤。

### 推荐方案

- **扩展点**：自建 FormPlugin 挂同表单 hrcs_entityctrl
  - `propertyChanged(entitytype)` —— 业务对象变更时联动逻辑
  - `registerListener` + `beforeF7Select(dimension)` —— 维度 F7 时**附加**二级过滤
- **风险**：中（要避免覆盖标品 6 分支逻辑 + 注意死循环 PR-004）

### 扩展入口坐标

- 绑定表单：`hrcs_entityctrl`
- 推荐父类：`HRDataBaseEdit`（**不要继承 EntityCtrlEdit**· PR-001）
- 关键重写方法：
  - `registerListener(EventObject evt)` — 注册维度的 BeforeF7SelectListener
  - `propertyChanged(PropertyChangedArgs evt)` — 业务对象变更联动
  - `beforeF7Select(BeforeF7SelectEvent evt)` — 维度 F7 附加过滤

### 调用链

```
用户改 entitytype
    ↓
EntityCtrlEdit.propertyChanged(entitytype)                        【标品逻辑 1】
  → deleteEntryData("entryentity")
  → bindAppCloud → setValue("bizapp", ...)
  → putMainOrgFieldProp / putDynaFormCtrlInfo 装载 propInfos

(同时触发) TdkwEntityCtrlExtPlugin.propertyChanged(entitytype)    【ISV 逻辑】
  → if (entitytype.modeltype == 组织相关) {
       getModel().beginInit()
       // 这里不能 setValue dimension · 因为 dimension 在分录子表
       // 但可以 setValue 自定义辅助字段 ${ISV_FLAG}_recommendeddim 给 UI 用
       getModel().setValue("${ISV_FLAG}_recommendeddim", "管理域")
       getModel().endInit()
       getView().updateView("${ISV_FLAG}_recommendeddim")
     }

【维度 F7 时】
用户在分录某行点 dimension F7
    ↓
EntityCtrlEdit.beforeF7Select(dimension)                          【标品 6 分支过滤】
  → 算 filter（datasource / org_classify / hrbu / entitytype 等）
  → lsp.getListFilterParameter().setFilter(filter)
  → lsp.setCloseCallBack("dimensionCallBack")

(继续走) TdkwEntityCtrlExtPlugin.beforeF7Select(dimension)        【ISV 附加过滤】
  → QFilter existing = (QFilter)lsp.getListFilterParameter().getFilter()
  → QFilter ext = new QFilter("group", "=", "客户业务线分组" + 当前用户业务线)
  → lsp.getListFilterParameter().setFilter(existing == null ? ext : existing.and(ext))
```

### 代码框架

```java
package kd.${ISV_FLAG}.hrcs.formplugin.dimension;

import java.util.EventObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;                  // PR-001 父类白名单

public class TdkwEntityCtrlExtPlugin extends HRDataBaseEdit
        implements BeforeF7SelectListener {

    @Override
    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        // 注意：不要再注册 entitytype 的 BeforeF7SelectListener · 标品已注册（注册两次会触发两次）
        // 只注册自己的需要：dimension 的附加过滤
        BasedataEdit dimension = (BasedataEdit) this.getView().getControl("dimension");
        if (dimension != null) {
            dimension.addBeforeF7SelectListener(this);
        }
    }

    @Override
    public void propertyChanged(PropertyChangedArgs evt) {
        String name = evt.getProperty().getName();

        if (HRStringUtils.equals("entitytype", name)) {
            DynamicObject entityType = (DynamicObject) evt.getChangeSet()[0].getNewValue();
            if (entityType == null) return;

            // PR-004：beginInit/endInit 防死循环
            this.getModel().beginInit();
            try {
                String modelType = entityType.getString("modeltype");
                if (isOrgRelated(entityType)) {
                    this.getModel().setValue("${ISV_FLAG}_recommendeddim", "管理域");
                } else if (isPersonRelated(entityType)) {
                    this.getModel().setValue("${ISV_FLAG}_recommendeddim", "职位序列");
                } else {
                    this.getModel().setValue("${ISV_FLAG}_recommendeddim", "");
                }
            } finally {
                this.getModel().endInit();
                this.getView().updateView("${ISV_FLAG}_recommendeddim");
            }
        }
    }

    @Override
    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String name = evt.getProperty().getName();
        if (!HRStringUtils.equals("dimension", name)) return;     // 只关心 dimension F7

        ListShowParameter lsp = (ListShowParameter) evt.getFormShowParameter();

        // 取标品已设的 filter · 不要覆盖
        QFilter existing = lsp.getListFilterParameter().getQFilters().isEmpty()
            ? null
            : lsp.getListFilterParameter().getQFilters().get(0);

        // 业务线 ID 从当前用户上下文 / Page 参数取
        String userBusinessLine = resolveCurrentUserBusinessLine();
        QFilter ext = new QFilter("group", "=", "客户业务线分组_" + userBusinessLine);

        if (existing != null) {
            existing.and(ext);
        } else {
            lsp.getListFilterParameter().setFilter(ext);
        }
    }

    private boolean isOrgRelated(DynamicObject entityType) {
        // 业务规则：判定 entitytype 是否是组织相关
        String number = entityType.getString("number");
        return number.startsWith("haos_") || number.equals("hbpm_position");
    }

    private boolean isPersonRelated(DynamicObject entityType) {
        String number = entityType.getString("number");
        return number.startsWith("hrpi_");
    }

    private String resolveCurrentUserBusinessLine() {
        // 业务侧实现 · 从 RequestContext + 自建用户业务线表反查
        return "default";
    }
}
```

### 踩坑

- ❌ **继承 EntityCtrlEdit**（违反 PR-001）：标品升级改方法签名 → ISV 编译失败 · 推荐 `extends HRDataBaseEdit` 并列挂
- ❌ **propertyChanged 里直接 setValue 触发字段** 导致 propertyChanged 二次触发 · 必须 beginInit/endInit 包裹（PR-004）
- ❌ **registerListener 重复注册 entitytype 的 BeforeF7SelectListener**：标品已注册（`EntityCtrlEdit.registerListener` L184-L185）· 重注册会让标品 4 闸过滤跑两次 / F7 弹两次
- ❌ **beforeF7Select 直接 `lsp.getListFilterParameter().setFilter(myFilter)` 覆盖标品 filter**：标品 6 分支过滤就被废了 · 一定要 **and** 进现有 filter
- ❌ **dimension F7 调用 `evt.getProperty().getName()` 后用 `==` 比较字符串**：用 `HRStringUtils.equals()`（标品 `EntityCtrlEdit.beforeF7Select` L194 实证）
- ⚠️ **PageCache 共享**：`bdPropInfos / orgInfos / noDBProps` 都是标品 `EntityCtrlEdit.beforeBindData` 写入 PageCache 的 · ISV 自建 plugin 可以**读**（不能写覆盖 · 否则标品 F7 过滤错乱）

**遵循的 PR 规范**：PR-001（并列挂不继承）· PR-003（FormPlugin 用 setValue）· PR-004（beginInit/endInit 防死循环）

### 关联 Pattern

参考 `_shared/platform_rules.json` PR-003 / PR-004 + 反编译 `EntityCtrlEdit.propertyChanged` L336-L365 标品做法。

---

## CS-03 · save 前置业务校验：propkey + dimension 不能重复（onAddValidators）

**关联 Pattern**：自建 Validator + onAddValidators

### 需求

业务方说：业务对象映射保存时 · 同一个业务对象上的同一个字段（propkey）不能配两次（哪怕 dimension 不同也不行）。标品 F7 已经过滤了"业务对象级别唯一" · 但**字段级别没有拦截** —— 用户可能在分录里同一个 propkey 加两行 · 配两个不同的 dimension。需要 ISV 加校验。

### 推荐方案

- **扩展点**：自建 OP（继承 HRDataBaseOp · PR-001）挂 save · 在 `onAddValidators` 注册自建 Validator
- **风险**：低（不动数据 · 只校验）

### 扩展入口坐标

- 绑定表单：`hrcs_entityctrl`
- 推荐父类：`HRDataBaseOp`（PR-001）
- 关键重写方法：
  - `onAddValidators(AddValidatorsEventArgs args)` — 注册校验器（PR-010 阶段 4）

### 调用链

```
用户点保存
    ↓
EntityCtrlEdit.beforeDoOperation(save)         【标品 1】
  → 校验分录非空 + 设 propDimInfo / originPropDimInfo
    ↓
进入 OP 链
    ↓
HRBaseDataLogOp.beforeExecute                  【标品 2】
EntityControlSaveOp.onAddValidators            【标品 3】
  → addValidator(EntityControlSaveValidator)
TdkwEntityCtrlPropDupValidatorOp.onAddValidators 【ISV】
  → addValidator(TdkwPropKeyDupValidator)
    ↓
Validator 链执行
  → EntityControlSaveValidator.validate         【标品校验先跑】
  → TdkwPropKeyDupValidator.validate            【ISV 校验后跑】
    ↓
任一 Validator 失败 → setCancel · 主事务不开
```

### 代码框架

```java
package kd.${ISV_FLAG}.hrcs.opplugin.dimension;

import java.util.HashSet;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;                       // PR-001 父类白名单

public class TdkwEntityCtrlPropDupValidatorOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new TdkwPropKeyDupValidator());          // PR-010 阶段 4
    }

    public static class TdkwPropKeyDupValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (ExtendedDataEntity row : this.getDataEntities()) {
                DynamicObject entity = row.getDataEntity();
                DynamicObjectCollection entry = entity.getDynamicObjectCollection("entryentity");

                Set<String> seen = new HashSet<>(entry.size());
                for (int i = 0; i < entry.size(); i++) {
                    DynamicObject entryRow = entry.get(i);
                    String propKey = entryRow.getString("propkey");
                    if (propKey == null || propKey.isEmpty()) continue;

                    if (!seen.add(propKey)) {
                        // 同 propkey 第二次出现
                        this.addErrorMessage(row, ResManager.loadKDString(
                            "字段 [" + propKey + "] 在分录中出现多次，请保留唯一一行配置。",
                            "TdkwPropKeyDupValidator_1",
                            "tdkw-hrcs-opplugin",
                            new Object[0]
                        ));
                        break;   // 一行一个错误就够了 · 用户改完再保存重校
                    }
                }
            }
        }
    }
}
```

### 平台绑定

注册插件：`targetType = OPERATION` · `opKey = save` · `className = TdkwEntityCtrlPropDupValidatorOp`

### 踩坑

- ❌ **在 `beforeExecuteOperationTransaction` 注册 Validator**（违反 PR-010）：阶段 5 时 Validator 已经执行完了 · 注册晚了
- ❌ **校验直接抛 KDBizException**：会让整个 save 一次性失败 · 用户看不到行级错误 · 推荐 `addErrorMessage(row, msg)` 让用户知道哪行出错
- ❌ **校验逻辑放 FormPlugin.beforeDoOperation**：列表批量保存 / 接口直推时 FormPlugin 不会跑 · 校验失效。**永远在 OP Validator 里做校验**
- ❌ **校验不读分录**：忘了从 entity.getDynamicObjectCollection("entryentity") 取分录数据
- ❌ **错误提示硬编码中文**：用 `ResManager.loadKDString` 国际化（参考 `EntityCtrlEdit.java` 中 ResManager 调用）
- ⚠️ **多语言 key 第二参数（`TdkwPropKeyDupValidator_1`）必须全工程唯一** · 否则翻译会串
- ⚠️ **propkey 区分大小写**：苍穹字段 key 通常都是小写 · 但 propkey 可能含大写字母（如 `ID` / `boid` 实证 `EntityCtrlEdit.beforeF7Select` L231）· 如果业务希望大小写不敏感 · 用 toLowerCase 后比较

**遵循的 PR 规范**：PR-001（并列挂 HRDataBaseOp）· PR-003（OP 用 entity.set / 这里只读不写）· PR-010（onAddValidators 阶段 4 注册）

### 关联 Pattern

参考 `_shared/platform_rules.json` PR-010 + `EntityControlSaveOp.onAddValidators` L81-L83 标品做法。

---

## CS-04 · 删除前查下游引用（防误删 hrcs_datarule / hrcs_dynaformctrl 引用的映射）

**关联 Pattern**：删除前置校验 + 反查下游

### 需求

业务方说：业务对象映射删除前 · 必须先查 `hrcs_datarule`（数据规则）和 `hrcs_dynaformctrl`（虚字段配置）有没有引用此映射的业务对象。如果有 · 提示用户先去清理下游配置 · 再来删本映射。**避免删除后下游配置变成"幽灵引用"**。

⚠ **重要：标品 `EntityCtrlDelOp` 只联动清 hrcs_roledimension** · 不会清 datarule / dynaformctrl · 这是为什么需要 ISV 加这个校验。

### 推荐方案

- **扩展点**：自建 OP（继承 HRDataBaseOp）挂 delete · 在 `onAddValidators` 注册自建 Validator 反查下游
- **风险**：低（只读校验 · 不动下游数据）

### 扩展入口坐标

- 绑定表单：`hrcs_entityctrl`
- 推荐父类：`HRDataBaseOp`
- 关键重写方法：
  - `onAddValidators(AddValidatorsEventArgs args)` — 注册校验器

### 调用链

```
用户列表点删除
    ↓
EntityCtrlTreeListPlugin.beforeDoOperation(delete)            【标品 1 · TreeList 拦截】
  → EntityCtrlServiceHelper.beforeDelOp 校验 issyspreset 等
    ↓
进入 OP 链
    ↓
HRDataBaseOp.beforeExecute                                    【父类默认】
HRBaseDataLogOp.beforeExecute                                 【操作日志】
TdkwEntityCtrlDelDownstreamOp.onAddValidators                 【ISV 校验注册】
  → addValidator(TdkwDownstreamRefValidator)
    ↓
TdkwDownstreamRefValidator.validate                           【ISV 反查下游】
  ↓ 收集要删的 entityctrl id
  ↓ 反查 hrcs_datarule WHERE entitytype IN (entitytypeIds)
  ↓ 反查 hrcs_dynaformctrl WHERE entitytype IN (entitytypeIds)
  ↓ 命中 → addErrorMessage("数据规则 X 引用此映射，请先清理")
    ↓
EntityCtrlDelOp.beginOperationTransaction                     【标品业务】
```

### 代码框架

```java
package kd.${ISV_FLAG}.hrcs.opplugin.dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;       // 白名单 helper
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class TdkwEntityCtrlDelDownstreamOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new TdkwDownstreamRefValidator());
    }

    public static class TdkwDownstreamRefValidator extends AbstractValidator {

        @Override
        public void validate() {
            ExtendedDataEntity[] dataEntities = this.getDataEntities();
            if (dataEntities.length == 0) return;

            // 1. 收集要删的 entitytype number 集合
            Set<String> entityNumbers = new HashSet<>(dataEntities.length);
            Map<String, ExtendedDataEntity> rowByNumber = new HashMap<>();
            for (ExtendedDataEntity row : dataEntities) {
                DynamicObject entity = row.getDataEntity();
                String number = entity.getString("entitytype.number");
                if (number != null) {
                    entityNumbers.add(number);
                    rowByNumber.put(number, row);
                }
            }
            if (entityNumbers.isEmpty()) return;

            // 2. 反查 hrcs_datarule 引用
            HRBaseServiceHelper datarule = new HRBaseServiceHelper("hrcs_datarule");
            DynamicObject[] dataruleRefs = datarule.queryOriginalArray(
                "id,name,entitytype.number",
                new QFilter[]{new QFilter("entitytype.number", "in", entityNumbers)}
            );
            for (DynamicObject ref : dataruleRefs) {
                String number = ref.getString("entitytype.number");
                ExtendedDataEntity row = rowByNumber.get(number);
                if (row != null) {
                    this.addErrorMessage(row, ResManager.loadKDString(
                        String.format("数据规则[%s]引用业务对象[%s]，请先清理或转移此规则后再删映射。",
                                      ref.getString("name"), number),
                        "TdkwDownstreamRefValidator_1",
                        "tdkw-hrcs-opplugin",
                        new Object[0]
                    ));
                }
            }

            // 3. 反查 hrcs_dynaformctrl 引用
            HRBaseServiceHelper dynaformctrl = new HRBaseServiceHelper("hrcs_dynaformctrl");
            DynamicObject[] dynaformctrlRefs = dynaformctrl.queryOriginalArray(
                "id,entitytype",
                new QFilter[]{new QFilter("entitytype", "in", entityNumbers)}
            );
            for (DynamicObject ref : dynaformctrlRefs) {
                String number = ref.getString("entitytype");
                ExtendedDataEntity row = rowByNumber.get(number);
                if (row != null) {
                    this.addErrorMessage(row, ResManager.loadKDString(
                        String.format("虚字段数据控权配置引用业务对象[%s]，请先在 hrcs_dynaformctrl 中删除此对象后再来。",
                                      number),
                        "TdkwDownstreamRefValidator_2",
                        "tdkw-hrcs-opplugin",
                        new Object[0]
                    ));
                }
            }
        }
    }
}
```

### 平台绑定

注册插件：`targetType = OPERATION` · `opKey = delete` · `className = TdkwEntityCtrlDelDownstreamOp`

### 踩坑

- ❌ **反查使用 `HRBaseServiceHelper.query("xxx", filter)` 拿到完整 DynamicObject**：性能差 · 用 `queryOriginalArray("id,name,entitytype.number", filter)` 只取需要的列
- ❌ **校验在 `beforeExecuteOperationTransaction` 抛异常**：异常机制虽然能阻断 · 但 PR-010 推荐 `addErrorMessage` · 用户体验更好
- ❌ **不区分行级错误**：批量删除时 ENTITY-A 有引用 / ENTITY-B 没引用 · 应该只阻断 A · 让 B 通过。`addErrorMessage(row, msg)` 是行级 · 标品框架自动忽略只阻断有错的行
- ❌ **直接读 `row.getDataEntity().getDynamicObject("entitytype")`**：删除时 entity 可能只有 id 没有完整数据 · 推荐先 load 完整 entity · 或在 onPreparePropertys 声明字段
- ⚠️ **`HRBaseServiceHelper` 是 SDK 白名单类**（`kd.hr.hbp.business.servicehelper.HRBaseServiceHelper` · 实证 `EntityCtrlEdit.java` 多处调用）· 可放心用
- ⚠️ **下游表反查方向是"entitytype"**：下游引用的是业务对象（不是 hrcs_entityctrl 主键 id）· 因为 entityctrl 一对象一映射 · entitytype 就是业务键

**遵循的 PR 规范**：PR-001（并列挂）· PR-009 不适用（本场景非时序 · 无 boid）· PR-010（onAddValidators 阶段 4）

### 关联 Pattern

参考 admin_org / dynascheme CS-04 · 同一套"删除前反查下游引用"模板。本场景多了一个特殊的 `hrcs_dynaformctrl` 反查（仅虚字段实体场景 · 标品 `EntityCtrlEdit.putDynaFormCtrlInfo` 实证此关联表）。

---

## CS-05 · 监听 save 后推送下游（BEC 发布方 · ISV 自建）

**关联 Pattern**：BEC 发布方模式

### 重要前置 · BEC grep 实证

按 `feedback_bec_3layer_async_publish.md` 铁律 · CS-05 写之前必须 grep 反编译产物：

```bash
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hrcs_entityctrl/
```

**实证结果（2026-04-28）**：

```
（0 处命中 · 标品没发任何 BEC 事件）
```

→ 同时 grep 了 OP / FormPlugin / Service 三层（`EntityControlSaveOp` / `EntityCtrlDelOp` / `EntityCtrlEdit` / `EntityCtrlTreeListPlugin` / `HRAdminStrictPlugin`）· 都没找到 BEC 调用。

→ 结论：**hrcs_entityctrl 标品没发 BEC** · ISV 必须自建发布方（如果业务真有需要把映射变更通知下游系统）。这与 hjm（3 层异步发布）和 homs（sch_task 派单）模式不同 · 与 dynascheme（标品没发）/ haos_structure（标品没发）属于"同一类（标品没发）"。

### 需求

业务方说：业务对象维度映射 save 后 · 需要通知 OA 系统刷新该业务对象的"权限配置缓存"（OA 缓存了"哪个业务对象用哪些维度"）。希望以**业务事件**形式发出 · OA 通过 BEC 订阅消费。

### 推荐方案

- **扩展点**：自建 OP 挂 save 的 `afterExecuteOperationTransaction`（PR-010 阶段 9 · 主事务已提交）
- **实现模式**：自建 OP 调 `IEventService.triggerEventSubscribeJobs` 发 BEC 事件
- **风险**：中（必须先在【开发平台】配 eventNumber · 否则 BEC 不识）

### 扩展入口坐标

- 绑定表单：`hrcs_entityctrl`
- 推荐父类：`HRDataBaseOp`
- 关键重写方法：
  - `afterExecuteOperationTransaction(AfterOperationArgs args)` — 主事务已提交 · 调 IEventService.triggerEventSubscribeJobs

### 调用链

```
用户点 save （单据上）
    ↓
EntityCtrlEdit.beforeDoOperation 走分录非空校验 + propDimInfo 设置
    ↓
进入 OP 链
    ↓
HRBaseDataLogOp / EntityControlSaveOp.endOperationTransaction (落 hrcs_roledimension + 日志)
    ↓
事务提交（commit）
    ↓
TdkwEntityCtrlBecPublishOp.afterExecuteOperationTransaction (PR-010 阶段 9 · 主事务已提交安全)
    ↓
IEventService svc = ServiceHelper.getService(IEventService.class)
svc.triggerEventSubscribeJobs(
    "${ISV_FLAG}_hrcs",                                        // sourceApp
    "${ISV_FLAG}_entityctrl_changed",                          // eventNumber (在【开发平台】预配)
    "业务对象维度映射变更",                              // message
    Map.of(
        "entityCtrlId", entityCtrlId,                    // 主表 id
        "entityNumber", entityNumber,                    // 业务对象编号（业务键 · 跨环境稳定）
        "appId", appId,
        "operateType", "save",                           // save / delete 区分
        "operateAt", System.currentTimeMillis()
    )
)
    ↓
BEC 平台分发 → OA 端 IEventServicePlugin.handleEvent(KDBizEvent) 消费
```

### 代码框架

#### 1. eventNumber 预配（必须先做）

苍穹开发平台 → 业务事件管理 → 新增：

| 字段 | 值 |
|---|---|
| 编码 | `${ISV_FLAG}_entityctrl_changed` |
| 名称 | 业务对象维度映射变更 |
| 应用 | ${ISV_FLAG}_hrcs |
| 启用订阅 | true |
| 描述 | 映射 save / delete 后触发 · variables 含 entityCtrlId / entityNumber / appId / operateType / operateAt |

→ 不预配 · `triggerEventSubscribeJobs` 调用时 BEC 不识别 eventNumber · 静默丢消息

#### 2. 自建 OP（发布方）

```java
package kd.${ISV_FLAG}.hrcs.opplugin.dimension;

import java.util.HashMap;
import java.util.Map;
import kd.bos.bec.api.IEventService;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.servicehelper.ServiceHelper;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class TdkwEntityCtrlBecPublishOp extends HRDataBaseOp {       // PR-001 并列挂

    private static final Log LOG = LogFactory.getLog(TdkwEntityCtrlBecPublishOp.class);
    private static final String SOURCE_APP = "${ISV_FLAG}_hrcs";
    private static final String EVENT_NUMBER = "${ISV_FLAG}_entityctrl_changed";

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        // 注：本场景非 HisModel · 没 boid · 用业务键 entitytype.number 作下游引用
        args.getFieldKeys().add("entitytype");
        args.getFieldKeys().add("bizapp");
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {  // PR-010 阶段 9
        super.afterExecuteOperationTransaction(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) return;

        IEventService svc = ServiceHelper.getService(IEventService.class);
        String operationKey = args.getOperationKey();  // save / delete

        for (DynamicObject ec : dataEntities) {
            long entityCtrlId = ec.getLong("id");
            String entityNumber = ec.getDynamicObject("entitytype") != null
                ? ec.getDynamicObject("entitytype").getString("number")
                : "";
            String appId = ec.getDynamicObject("bizapp") != null
                ? ec.getDynamicObject("bizapp").getString("id")
                : "";

            // 注：variables 不要塞完整 DynamicObject · BEC 会序列化超大 · 走业务键
            Map<String, Object> variables = new HashMap<>(8);
            variables.put("entityCtrlId", entityCtrlId);
            variables.put("entityNumber", entityNumber);    // 业务对象编号 · 下游用此反查
            variables.put("appId", appId);
            variables.put("operateType", operationKey);     // save / delete 区分
            variables.put("operateAt", System.currentTimeMillis());
            // 不要塞 entryentity · 要查走 entityCtrlId 反查

            try {
                svc.triggerEventSubscribeJobs(
                    SOURCE_APP,
                    EVENT_NUMBER,
                    "业务对象维度映射变更 · " + entityNumber,
                    variables
                );
                LOG.info("BEC 发布成功 · entityCtrlId={} entityNumber={} op={}",
                         entityCtrlId, entityNumber, operationKey);
            } catch (Exception e) {
                LOG.error("BEC 发布失败 · entityCtrlId=" + entityCtrlId, e);
                // ⚠ 不要抛异常回滚 · 主事务已提交 · 抛了也无意义
            }
        }
    }
}
```

#### 3. 平台绑定

注册插件：`targetType = OPERATION` · `opKey = save` · `className = TdkwEntityCtrlBecPublishOp`

如果还想监听 delete 时也发事件 · 重复注册同 className 到 `opKey = delete`。

### 踩坑

- ❌ **不在 afterExecuteOperationTransaction 阶段发 BEC**（PR-010 / PR-011）：在 endOperationTransaction 阶段事务可能还没真提交 · 发了脏事件
- ❌ **不预配 eventNumber 直接调 triggerEventSubscribeJobs**（PR-011 prerequisite）：BEC 不识别 · 静默丢
- ❌ **variables 里塞完整 DynamicObject**（PR-011 antiPattern）：BEC 序列化大对象 · 网络传输巨慢 · 应只塞 entityCtrlId + 必需小字段 · 订阅方需要详情自己反查
- ❌ **eventNumber 用大写 / 含中划线**：苍穹规约推荐小写 + 下划线 · 如 `${ISV_FLAG}_entityctrl_changed`
- ❌ **try-catch 后 throw 异常**：主事务已提交 · 抛异常无意义 · 只 log
- ❌ **下游用 entityCtrlId 不用 entityNumber 当业务键**：本场景非 HisModel · entityCtrlId 是物理主键 · 跨环境（test → prod）不一致；entityNumber（业务对象编号）是业务键 · 跨环境稳定。**优先用 entityNumber 做下游反查**
- ⚠️ **BEC 是异步**：发完不等订阅方处理完成 · 如果业务要"OA 一定刷新成功"必须自建幂等机制（比如订阅方收到事件后回调一个"刷新成功"接口）

**遵循的 PR 规范**：PR-001（并列挂不继承）· PR-009 不适用（本场景没 boid · 用 entityNumber 业务键代替）· PR-010（afterExecuteOperationTransaction 阶段发外部事件）· PR-011（BEC API + eventNumber 预配 + variables 不塞 DynamicObject）

### 关联 Pattern

参考 `_shared/platform_rules.json` PR-011 的 publishAPI 部分（`bos-mservice-bec-api-8.0.jar` · `kd.bos.bec.api.IEventService.triggerEventSubscribeJobs` 实证）。

---

## CS-06 · 分录子表 entryentity 操作扩展（增行时强制走 ID 生成 · PR-005）

**关联 Pattern**：分录子表行 ID 强制 · PR-005

### 需求

业务方说：除了标品的"添加行"按钮（走 hrcs_choosefield_page 子页面）· 客户希望支持**接口直推批量增行**（如同步外部系统的字段配置过来 · 一次性刷一批分录行）。要求接口直推时分录行 id 走苍穹分布式 ID 接口（不要让 ORM 自动生成 · 因为客户做了"按 id 范围分库" 自定义路由 · 自动生成的 id 命中不可控）。

### 推荐方案

- **扩展点**：自建 OP 挂 save 的 `beforeExecuteOperationTransaction`（在主事务前 · 给空 id 的分录行强制填 ID.genLongId()）
- **实现模式**：遍历 dataEntity.getDynamicObjectCollection("entryentity") · 给 id == 0 的行 setId
- **风险**：中（如果跟其他 OP 同时改 entry id 会冲突 · 必须确保自建 OP 在标品 OP 链最前注册）

### 扩展入口坐标

- 绑定表单：`hrcs_entityctrl`
- 推荐父类：`HRDataBaseOp`
- 关键重写方法：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs args)` — 主事务前的预处理（PR-010 阶段 5）

### 调用链

```
接口直推 save 调用
    ↓
进入 OP 链
    ↓
HRBaseDataLogOp.beforeExecute                                 【日志】
TdkwEntryIdGenOp.beforeExecuteOperationTransaction            【ISV · ID 生成】
  ↓ for entity in dataEntities:
  ↓   for entryRow in entity.entryentity:
  ↓     if entryRow.id == 0:
  ↓       entryRow.set("id", ID.genLongId())                  ← PR-005
EntityControlSaveOp.beginOperationTransaction                 【标品】
  ↓ 自动带 bizapp
EntityControlSaveOp.endOperationTransaction                   【标品】
  ↓ 同步 hrcs_roledimension
    ↓
ORM INSERT/UPDATE 用 ISV 生成的 id
```

### 代码框架

```java
package kd.${ISV_FLAG}.hrcs.opplugin.dimension;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.id.ID;                                              // PR-005 苍穹分布式 ID
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class TdkwEntityCtrlEntryIdGenOp extends HRDataBaseOp {

    private static final Log LOG = LogFactory.getLog(TdkwEntityCtrlEntryIdGenOp.class);

    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);

        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) return;

        int filledCount = 0;
        for (DynamicObject entity : dataEntities) {
            DynamicObjectCollection entry = entity.getDynamicObjectCollection("entryentity");
            for (int i = 0; i < entry.size(); i++) {
                DynamicObject row = entry.get(i);
                long currentId = row.getLong("id");
                if (currentId == 0L) {
                    long newId = ID.genLongId();                  // PR-005 苍穹 Snowflake
                    row.set("id", newId);                          // OP 用 entity.set (PR-003)
                    filledCount++;
                }
            }
        }
        if (filledCount > 0) {
            LOG.info("ISV 接口批量增行 · 填充 id × {}", filledCount);
        }
    }
}
```

### 平台绑定

注册插件：`targetType = OPERATION` · `opKey = save` · `className = TdkwEntityCtrlEntryIdGenOp`

⚠ **执行顺序**：本插件需要在 `EntityControlSaveOp.beginOperationTransaction` 之前执行 · 否则标品 OP 看到的还是 id=0。按 PR-002 · ISV 扩展元数据可以排在标品插件之前（注册时把 RowKey 设小 · 或借助 IDEA 插件配置 ISV plugin 排序）。

### 踩坑

- ❌ **用 `entry.addNew()` 然后不设 id**：苍穹 ORM 默认会自动生成 · 但客户做了"按 id 范围分库" 时 · 自动生成可能撞库。**必须显式 ID.genLongId()**
- ❌ **用 `UUID.randomUUID()` 当 id**：违反 PR-005 · 苍穹 id 是 Long 类型 · UUID 是 String · 类型不兼容
- ❌ **用 `System.currentTimeMillis()` 当 id**：高并发会撞 · 苍穹 ID 是 19 位 Snowflake · 自带时序 + 节点位 · 不撞
- ❌ **在 `afterExecuteOperationTransaction` 改 id**：事务已提交 · 改了不会落库
- ❌ **在 FormPlugin 里 setValue id**：FormPlugin 走的是 `getModel().setValue(...)` · 但 `id` 字段一般是只读 · 改不动；OP 才能直接 entity.set
- ⚠️ **id 字段在 entryentity 是平台保留字段** · 标品代码读它的位置（如 `EntityCtrlEdit.beforeBindData` L161 `dy.getLong("id")`）也跟着这个 ID 走 —— 如果 ISV 的 id 跟标品逻辑冲突 · 注意业务边界

**遵循的 PR 规范**：PR-001（并列挂）· PR-002（ISV plugin 可排标品前）· PR-003（OP 用 entity.set）· PR-005（用 ID.genLongId）· PR-010（beforeExecuteOperationTransaction 阶段 5）

### 关联 Pattern

参考 dynascheme CS-01 的"补充：分录子表行 id / 业务流水号生成规范" · 同套 PR-005 应用。本场景的差异点：dynascheme 用的是 `ORM.genLongIds("hrcs_dynascheme.roleentry", size)`（批量按实体 key 分配）· 本场景因为非 HisModel + 单行接口 · 用 `ID.genLongId()` 单个生成更直接。

---

## CS-07 · 列表过滤定制（按当前用户的 admingroup 过滤可见映射）

**关联 Pattern**：自建 ListPlugin + setFilter

### 需求

业务方说：每个 HR 管理员只应该看到自己 admingroup 关联的业务对象映射 · 不应看到其他 admingroup 的映射（数据隔离）。当前标品 TreeList 没做这个过滤。

⚠ 本场景是 **TreeList**（实证 `EntityCtrlTreeListPlugin extends AbstractTreeListPlugin`）· 自建 ListPlugin 必须**继承 `AbstractTreeListPlugin`**（不是 `AbstractListPlugin`）· 否则挂不上。

### 推荐方案

- **扩展点**：自建 ListPlugin（继承 `AbstractTreeListPlugin`）挂 hrcs_entityctrl 列表 · 在 `setFilter` 加过滤
- **风险**：低（不动数据 · 只过滤显示）

### 扩展入口坐标

- 绑定表单：`hrcs_entityctrl`（数据实体 · 与 EntityCtrlTreeListPlugin 同层）
- 推荐父类：`AbstractTreeListPlugin`（**不要继承 EntityCtrlTreeListPlugin** · PR-001）
- 关键重写方法：
  - `setFilter(SetFilterEvent evt)` — 列表 setFilter 加 QFilter

### 调用链

```
用户进入 TreeList
    ↓
EntityCtrlTreeListPlugin.setFilter                            【标品 setFilter】
  → evt.getQFilters().add(new QFilter("entitytype.number", "is not null", null))
  → 排除孤儿映射

(继续走) TdkwEntityCtrlListFilterPlugin.setFilter             【ISV setFilter】
  → 拿当前用户 admingroup
  → evt.getQFilters().add(new QFilter("bizapp", "in", 用户能看的 appId 列表))
  → SELECT t_hrcs_entityctrl WHERE entitytype.number IS NOT NULL
       AND bizapp IN (...)
```

### 代码框架

```java
package kd.${ISV_FLAG}.hrcs.formplugin.dimension;

import java.util.Set;
import kd.bos.context.RequestContext;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.plugin.AbstractTreeListPlugin;                  // ⭐ TreeList 父类（不是 List）
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;

public class TdkwEntityCtrlListFilterPlugin extends AbstractTreeListPlugin {

    @Override
    public void setFilter(SetFilterEvent evt) {
        super.setFilter(evt);

        long userId = RequestContext.get().getCurrUserId();
        if (PermissionServiceHelper.isAdminUser(userId)) {
            return;                                                // 超管不限
        }

        Set<Long> visibleAppIds = resolveCurrentUserVisibleAppIds(userId);
        if (visibleAppIds.isEmpty()) {
            // 用户没有任何可见 app · 列表显示空
            evt.getQFilters().add(new QFilter("1", "=", 0));
            return;
        }
        evt.getQFilters().add(new QFilter("bizapp", "in", visibleAppIds));
    }

    private Set<Long> resolveCurrentUserVisibleAppIds(long userId) {
        // 业务侧实现 · 反查用户 admingroup → 关联的 app 集合
        // 推荐：HRRolePermHelper.queryUserAdminGroups(userId) → 拿到 admingroup → 关联 app
        return new java.util.HashSet<>();
    }
}
```

### 平台绑定

注册插件：`targetType = LIST_FORM` · `opKey =`（不绑 opKey · 是列表壳插件）· `className = TdkwEntityCtrlListFilterPlugin`

如要做单据级别（form view）的隐藏字段 · 需要再做一个 FormPlugin 挂 `targetType = BILL_FORM`。

### 踩坑

- ❌ **继承 `AbstractListPlugin` 而不是 `AbstractTreeListPlugin`**：标品是 TreeList · 继承 List 无法拿到树节点交互 · 也可能导致挂载位不对
- ❌ **继承 `EntityCtrlTreeListPlugin` 替换标品**：违反 PR-001 · 标品升级会挂
- ❌ **直接 `evt.getQFilters().clear() + add(myFilter)`**：标品 `entitytype.number is not null` 过滤就被废了 · 列表会出现孤儿映射 · 一定要 **and add**
- ❌ **setFilter 里调跨模块 RPC**：setFilter 是高频调用 · 每次列表刷新都跑 · 用 RPC 会慢死 · 推荐先用 `HRPermCacheMgr` 类似的本地缓存（参考标品 `HRAppCache.get("hrcs")` 用法 · 实证 `EntityCtrlTreeListPlugin.afterQueryOfExport` L116）
- ❌ **判断 `RequestContext.get().getCurrUserId() == 0`**：苍穹定时任务等场景 currentUserId 可能是系统账号 · 不能直接拒
- ⚠️ **TreeList 树节点点击事件**：本场景 `EntityCtrlTreeListPlugin` 反编译里没看到 `treeNodeClick` / `treeNodeQueryClick` 实现 · 推测 TreeList 走通用 BillFormModel List 逻辑没有自定义树。ISV 想加自定义树节点交互需要重写 `treeNodeClick` 等
- ⚠️ **本场景没有 `iscurrentversion` 字段**（非 HisModel）· **不要**像 jobhr/admin_org 列表那样加 `iscurrentversion=true` 过滤 —— 加了 SQL 报错
- ⚠️ **本场景没有 `disable / enable` opKey**（非可禁用基础资料）· **不要**加 `enable=true` / `enable in [1, 10]` 过滤

**遵循的 PR 规范**：PR-001（并列挂 AbstractTreeListPlugin · 不继承 EntityCtrlTreeListPlugin）· PR-008 不适用（本场景非 HisModel · 没有 iscurrentversion）

### 关联 Pattern

参考 admin_org / dynascheme 的 List setFilter 模式 · 但这里多了"TreeList 父类"的差异点：
- admin_org / dynascheme 是 `extends HRDataBaseList`
- entityctrl 是 `extends AbstractTreeListPlugin`（HRDataBaseList 不在 TreeList 体系里）

→ Claude 写代码前必看 `_auto_plugin_registry.md` 确认列表父类是 List 还是 TreeList。

---

## 八、跨场景对齐：CS- 与其他场景

| 场景 | 状态字段 | HisModel | 标品发 BEC | TreeList |
|---|---|---|---|---|
| `hrcs_entityctrl` (本场景) | ❌ 无（非工作流） | ❌ 否 | ❌ 没发 | ✅ TreeList |
| `hrcs_dynascheme` | ✅ status / enable + datastatus | ✅ 是 | ❌ 没发 | ❌ 普通 List |
| `hjm_jobhr_maintenance` | ✅ enable + datastatus | ✅ 是 | ✅ 3 层异步发 | ❌ 普通 List |
| `admin_org_quick_maintenance` | ✅ enable + datastatus | ✅ 是 | ❌ 没发 | ❌ 普通 List |

→ Claude Code 跨场景时不要套用其他场景的"加 iscurrentversion 过滤" / "boid 反查" / "BEC 已发" 假设 · 必须先按本场景 03_model_design.md 的"HisModel 实证"段确认。

---

## 九、CS 速查总览

| CS | 场景 | 扩展点 | 父类 | PR 引用 |
|---|---|---|---|---|
| CS-01 | 加自定义字段 | modifyMeta | - | PR-007 |
| CS-02 | 业务对象→维度联动 + 维度 F7 二级过滤 | FormPlugin propertyChanged + beforeF7Select | HRDataBaseEdit | PR-001, PR-003, PR-004 |
| CS-03 | save 前 propkey 重复校验 | OP onAddValidators + Validator | HRDataBaseOp | PR-001, PR-010 |
| CS-04 | delete 前查下游引用 | OP onAddValidators + Validator | HRDataBaseOp | PR-001, PR-010 |
| CS-05 | save 后发 BEC 通知 OA | OP afterExecuteOperationTransaction | HRDataBaseOp | PR-001, PR-010, PR-011 |
| CS-06 | 分录接口直推时强制 ID.genLongId | OP beforeExecuteOperationTransaction | HRDataBaseOp | PR-001, PR-002, PR-003, PR-005, PR-010 |
| CS-07 | 列表按 admingroup 过滤 | TreeListPlugin setFilter | AbstractTreeListPlugin | PR-001 |
