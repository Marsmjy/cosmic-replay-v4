# 推荐定制方案 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟢 基于真实 OpenAPI 实抓 + 7 类反编译类名 + `scene_doc.json` 56 字段语义整合
> **confidence**: real_deploy（所有扩展点类名均来自 `_auto_plugin_semantics.md` + `_auto_plugin_registry.md` 实证）

所有方案遵循统一结构（借鉴 Stripe / Salesforce Developer Docs）：
**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

---

## CS-01 · 给 hrcs_dynascheme 扩展自定义字段（最高频）

**关联 Pattern**：`pattern/add_field_extension/README.md`

### 需求

业务方说：方案需要加"方案标签"字段（多选 · 可选 `重要 / 临时 / 试点 / 长期`），用于运营 BI 看板分类统计。

### 推荐方案

- **扩展对象**：`hrcs_dynascheme`（主实体）
- **扩展点**：`modifyMeta(op=add, elementType=field)` 或 IDEA 插件 Web UI 加字段
- **风险**：低（不动业务规则 · 只是数据展示位）
- **特点**：dynascheme 是 BillFormModel + HisModel · ISV 加字段的**主要踩坑点**是要意识到字段会跟 boid / id / iscurrentversion 一起进版本控制 —— 即每次 audit/confirmchange 后会有新版本的"方案标签"快照保留

### 调用链（3 步）

```
Step 1: getBizApps()                        // 拿 hrcs 应用 bizAppId
Step 2: modifyMeta({
  formId: "hrcs_dynascheme",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hrcs_dynascheme",
    element: {
      fieldType: "MulComboField",                       // 多选枚举
      key: "${ISV_FLAG}_schemetag",
      name: {zh_CN: "方案标签", en_US: "Scheme Tag"},
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
Step 3: getFormSchema("hrcs_dynascheme")    // ⭐ 二次验证落库（errorCode=0 不代表成功）
```

### 代码框架（使用 cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "hrcs_dynascheme", "number": "hrcs_dynascheme"}
)

designer.add_field(
    field_type="MulComboField",
    name="方案标签",
    key="${ISV_FLAG}_schemetag",
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

- ❌ 字段 key 不带 ISV 前缀（如直接叫 `schemetag`）→ 标品升级覆盖
- ❌ `fieldName` 列名超过 25 字符 → 苍穹平台开发规范限制 · 数据库建表失败
- ❌ `fieldName` 手动写 `fk_` 前缀 → 平台会再加 `f` → 列名变 `ffk_xxx` 怪列名 · **建议不传 fieldName 让平台按 `f + key.lowercase()` 自动生成**（坑 11）
- ❌ 多语言表 `_l` 命名规则：dynascheme 当前没独立 `_l` 表 · MuliLangTextField 都在主表承载 · ISV 加 MuliLangTextField 时不要假设会自动建 `_l` 表
- ❌ 误以为 ComboField 不需要 `comboOptions` → UI 下拉框空白
- ⚠️ 加字段后 · 走 `audit` / `confirmchange` 会**复制此字段到新版本**（HisModel 自动）· **不要假设字段是"最新版本独占"**

**遵循的 PR 规范**：
- PR-007（预置数据不可改 · 业务自建可改）· 本 CS 加的是业务字段 · 可随时删改
- PR-008（时序基础资料查询带 iscurrentversion=true · 反查 ISV 字段值时必须）

### 补充：分录子表行 id / 业务流水号生成规范（PR-005）

如果本场景未来扩展涉及**自建分录子表**（如给方案加"方案审批日志"分录）· 或要写"方案编码后缀"自动生成 · 行 id / 流水号必须用 `kd.bos.id.ID`：

```java
import kd.bos.id.ID;

// 场景 1：扩展时新增分录行 · 填行 id（必须实证 · 不能自己造 UUID）
DynamicObject newRow = entries.addNew();
newRow.set("id", ID.genLongId());                // 长整型主键 · 19 位 Snowflake
newRow.set("changedesc", "方案标签变更");

// 场景 2：自定义业务编码后缀 / 流水号
String traceId = ID.genStringId();               // 字符串 ID · 用于 traceId / 日志追踪号
String customNumber = "DYNASCH_" + ID.genStringId();
```

实证：`DynaAuthSchemePlugin.java` L253-L254 / L606-L609 已用 `ORM.genLongIds("hrcs_dynascheme.roleentry", size)` 给 roleentry 行分配 id · 是 PR-005 同语义的标品做法。

⚠ **反模式**（违反 PR-005 必驳回）：
- ❌ `UUID.randomUUID().toString()` · 苍穹有更轻量的 ID 接口
- ❌ `System.currentTimeMillis()` · 高并发会撞
- ❌ 自己 select max(id) + 1 · 分布式集群必坏

**遵循 PR-005 · 苍穹平台已集成分布式 ID（Snowflake）· ISV 不要自己 Redis incr 或 UUID 造**

### 验证

保存后到**HR 通用服务 / 权限管理 / 动态授权方案菜单**新增一条方案，看表单上是否出现"方案标签"字段。

### 与 HisModel 时序兼容性说明

`hrcs_dynascheme` 是 HisModel 时序基础资料 · ISV 加字段后会自动跟随版本控制（详见 03_model_design.md § 五）：

```
方案 V1 (新建)
  scheme.${ISV_FLAG}_schemetag = "重要,试点"        ← ISV 加的字段
  ↓ confirmchange
方案 V2 (新版本)
  scheme.${ISV_FLAG}_schemetag = "重要,试点"        ← 字段值从 V1 自动复制（HisModel 默认行为）
  ↓ 用户改成
  scheme.${ISV_FLAG}_schemetag = "重要"              ← V2 改了
  ↓ confirmchange
方案 V3 · scheme.${ISV_FLAG}_schemetag = "重要"
```

⚠️ **如果 ISV 字段不希望随版本复制**（如某些缓存性字段）· 标品没有内置开关 · 必须 ISV 自建 OP 在 audit/confirmchange 的 endOperationTransaction 阶段重置字段值（罕见需求）。

### 跨场景对齐：CS-01 与其他场景同模式

| 场景 | CS-01 等价 | 差异点 |
|---|---|---|
| `hjm_jobhr_maintenance` (CS-01) | 加"招聘状态"字段到 hbjm_jobhr | 也是 HisModel · 但不含 audit/confirmchange 工作流（基础资料只 enable/disable） |
| `admin_org_quick_maintenance` | 加自定义字段到 haos_adminorg | 也是 HisModel · 含 audit/disable |
| `hbpm_position_maintenance` | 加自定义字段到 hbpm_position | 类似 |
| `hrcs_dynascheme` (CS-01) | 加自定义字段到 hrcs_dynascheme | **唯一含 confirmchange 的场景** · ISV 字段跟随版本复制 |

→ Claude Code 跨场景时不要套用其他场景的"加字段流程"细节 · 应该按本场景特有的 HisModel + confirmchange 行为走。

### 加分录字段（如给 roleentry 加"角色生效日期"）

如果 ISV 字段加在分录子表（如 roleentry）· 步骤：

```
modifyMeta({
  formId: "hrcs_dynascheme",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hrcs_dynascheme.roleentry",      // ⭐ 指向子实体 · 不是主实体
    element: {
      fieldType: "DateField",
      key: "${ISV_FLAG}_roleeffectdate",
      name: {zh_CN: "角色生效日期"},
      mustInput: false
    }
  }]
})
```

⚠️ **分录字段会跟随 entryboid 版本控制**：每次 audit/confirmchange 后 · `genVersionRoleEntryColl` (实证 `DynaRoleDetailServiceHelper`) 会把该字段值复制到新版本的对应行。

⚠️ **不要给 roleentry 加 BasedataField + isUnique=true**：因为 entry 跨版本会共用 entryboid · 唯一索引会冲突（同一 entryboid 在新老版本各有 1 行 · 索引去重失败）。

### Pattern 复用速查

| 字段类型 | 推荐做法 |
|---|---|
| TextField (文本) | 直接加 · 字段长度按业务定（默认 200） |
| MuliLangTextField (多语言文本) | 直接加 · dynascheme 当前没 _l 子表 · 多语言挂主表 |
| ComboField (单选) | 必须传 comboOptions · 否则 UI 空白 |
| MulComboField (多选) | 同 ComboField · 业务上更常用于"标签"类 |
| BasedataField (基础资料关联) | 必须传 baseEntityNumber · 配合 F7 |
| MulBasedataField (多关联) | 苍穹用隐式中间表 · 加字段时 OpenAPI 抓成普通 BasedataField · 可能需 IDEA 插件 |
| DateField / DateTimeField | 直接加 |
| HRMulPositionField / HRMulAdminOrgField | OpenAPI buildMeta 不支持（kb_cosmic_buildmeta_traps.md 实证）· 走 IDEA 插件 |
| EmployeeField | OpenAPI 不支持 · 用 BasedataField 引用 hrpi_person 替代 |

---

## CS-02 · 选了 admingroup 自动带出该组下默认 authaction（字段联动）

**关联 Pattern**：FormPlugin propertyChanged 模式

### 需求

业务方说：HR 部门希望按"管理员组"做差异化默认值 —— 比如总部 HR 默认 authaction=3（分配并取消） · 区域 HR 默认 authaction=1（仅分配）。当用户选了 admingroup 后 · 系统自动把 authaction 字段带到组对应的默认值，减少人工填写。

### 推荐方案

- **扩展点**：`propertyChanged@hrcs_dynascheme` 监听 admingroup 字段变更
- **实现模式**：前端 `AbstractFormPlugin` + 注册为 `BILL_FORM`（targetType 必须是大写枚举 · R20）
- **风险**：低（前端只读 + 预填 · 不动后端数据）

### 扩展入口坐标

- 绑定表单：`hrcs_dynascheme`
- 推荐父类：`HRDataBaseEdit`（实证：`DynaAuthSchemePlugin extends HRDataBaseEdit` · `_auto_plugin_registry.md` L17）· **并列挂新插件 · 不继承 `DynaAuthSchemePlugin`**（PR-001）
- 关键重写方法：
  - `propertyChanged(PropertyChangedArgs args)` — 监听 admingroup 字段变更（PR-003 · FormPlugin 用 getModel().setValue）
  - 用 `HRBaseServiceHelper("${ISV_FLAG}_admingroup_default")` + QFilter(admingroupId) 查 ISV 自建的"管理员组默认值配置表"
  - `getModel().setValue("authaction", defaultAction)` 回填 · **用 beginInit/endInit 防死循环**（PR-004）

**业务意图**：`admingroup` 是方案归属管理员组 · 不同管理员组对方案授权动作有默认偏好。这个偏好需要 ISV 自建一张配置表 `${ISV_FLAG}_admingroup_default`（K-V：admingroup → authaction）· 由本 CS 的 propertyChanged 监听后查带出。

### 调用链

```
用户前端选择 "admingroup" = "总部 HR 管理员组"
    ↓
触发 propertyChanged · propertyKey = "admingroup" · newValue = admingroup id
    ↓
插件按 QFilter("admingroup", "=", newValue).and("iscurrentversion", "=", true)
    查 ${ISV_FLAG}_admingroup_default 表 (ISV 自建)
    ↓
取 ${ISV_FLAG}_default_authaction 字段值（如 "3"）
    ↓
getModel().beginInit();
getModel().setValue("authaction", defaultAction);
getModel().endInit();
getView().updateView("authaction");
    ↓
切到 authaction=3 后 · 标品 DynaAuthSchemePlugin.propertyChanged(L682-L697) 还会触发
"清规则"二次确认 · 这是正常行为 · 不要绕开
```

### 代码框架

```java
package kd.${ISV_FLAG}.hrcs.formplugin.dyna;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hbp.common.util.HRStringUtils;

public class DynaAuthSchemeAdminGroupDefault extends HRDataBaseEdit {  // PR-001 并列挂 · 不继承 DynaAuthSchemePlugin

    @Override
    public void propertyChanged(PropertyChangedArgs args) {
        String propKey = args.getProperty().getName();
        if (!HRStringUtils.equals("admingroup", propKey)) {
            return;
        }
        Object newValue = args.getChangeSet()[0].getNewValue();
        if (newValue == null) return;
        long admingroupId = ((DynamicObject) newValue).getLong("id");

        // ISV 自建表 · 查这个 admingroup 的默认 authaction
        HRBaseServiceHelper helper = new HRBaseServiceHelper("${ISV_FLAG}_admingroup_default");
        QFilter[] filters = {
            new QFilter("admingroup", "=", admingroupId),
            new QFilter("iscurrentversion", "=", Boolean.TRUE)        // PR-008 时序必带
        };
        DynamicObject cfg = helper.queryOriginalOne("${ISV_FLAG}_default_authaction", filters);
        if (cfg == null) return;
        String defaultAction = cfg.getString("${ISV_FLAG}_default_authaction");
        if (HRStringUtils.isEmpty(defaultAction)) return;

        // PR-004 防死循环
        this.getModel().beginInit();
        this.getModel().setValue("authaction", defaultAction);
        this.getModel().endInit();
        this.getView().updateView("authaction");
    }
}
```

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `hrcs_dynascheme`
2. 选择【注册插件】→ 新增插件（`targetType = BILL_FORM`）
3. 填写 `className` 为你的全限定名
4. 保存 → 部署生效

### 踩坑

- ❌ **不要继承 DynaAuthSchemePlugin**：标品 DynaAuthSchemePlugin extends HRDataBaseEdit · ISV 继承会把 propertyChanged super 链耦合 · 标品改了 propertyChanged 签名你必须跟着改（PR-001）
- ❌ propertyChanged 不加 beginInit/endInit · setValue("authaction") 会再触发 propertyChanged · 死循环（PR-004）
- ❌ 没带 `iscurrentversion=true` 过滤 · 查到 N 个历史版本的相同 admingroupId 配置 → 数据冲突（PR-008）
- ⚠️ admingroup 改了后 authaction 自动带出 · **但 standardly 标品 propertyChanged 还会触发"清规则二次确认"**（DynaAuthSchemePlugin.propertyChanged L682）· ISV 改 authaction 时如果 condition 不空 · 会触发 confirm box —— 这是合理的（避免规则错乱）· 不要试图压制
- ⚠️ 配置表 `${ISV_FLAG}_admingroup_default` 是 ISV 自建 · 你需要先 modifyMeta 创建（参考 CS-01 的 add field 流程）· 然后才能查
- ❌ 不要用 `BusinessDataServiceHelper.queryOne` —— HR 域首选 `HRBaseServiceHelper`（实证 `DynaAuthSchemePlugin.java` L291 · `new HRBaseServiceHelper("hrcs_dynascheme")`）

**遵循的 PR 规范**：PR-001（并列挂不继承）· PR-003（FormPlugin 用 getModel().setValue）· PR-004（beginInit/endInit 防死循环）· PR-008（iscurrentversion=true 过滤）

### 关联 Pattern

参考 `_auto_plugin_semantics.md` 中 `DynaAuthSchemePlugin` 的 `propertyChanged` 实现 · 模仿其 PageCache 缓存模式（如 `secondConfirmCancel`）实现状态隔离。

### 性能考量

`propertyChanged` 是 UI 层同步阻塞调用 · 用户切 admingroup 后等 setValue 完成才能继续。所以查表必须快：

| 优化点 | 做法 |
|---|---|
| QFilter 索引 | ${ISV_FLAG}_admingroup_default 表的 admingroup 字段加唯一索引（modifyMeta 时设 isUnique=true） |
| 缓存 | 用 PageCache 缓存当前 admingroup → authaction 映射 · 用户连续改 admingroup 时不重复查表 |
| 不要查关联表 | ${ISV_FLAG}_admingroup_default 只查 1 个字段（${ISV_FLAG}_default_authaction）· 不要 `.bu.name` 等串联查询 |

```java
// 加 PageCache 缓存版
String cacheKey = "${ISV_FLAG}_admingroup_default_" + admingroupId;
String cached = this.getPageCache().get(cacheKey);
if (cached != null) {
    this.getModel().beginInit();
    this.getModel().setValue("authaction", cached);
    this.getModel().endInit();
    return;
}
// ... 查 DB · 拿到后塞 PageCache
this.getPageCache().put(cacheKey, defaultAction);
```

### 边界场景

- 用户清空 admingroup（newValue=null）· 不应自动改 authaction（保留用户值）· 上面代码已处理 `if (newValue == null) return;`
- 配置表里 admingroup 没找到默认值· 不改 authaction（不主动 setValue）
- 用户先选了 authaction · 后改 admingroup · 触发 propertyChanged · 是否覆盖？**当前实现会覆盖** —— 如果业务希望"用户改过后不覆盖"· 可加 PageCache 标记 `userChangedAuthaction` · 在 setValue 前先查这个标记

---

## CS-03 · save / submit 前置业务校验：方案至少要绑 1 个角色（onAddValidators）

**关联 Pattern**：`pattern/add_unique_validation/README.md`

### 需求

业务方说：业务部门有时不小心保存了一个空 roleentry（没绑任何角色）的方案 · 走到下游引发"方案分配出空角色集合"的脏数据。希望 save/submit 时硬卡："方案必须至少绑 1 个角色"。

### 推荐方案

- **扩展点**：自建 `Validator extends AbstractValidator` + 自建 OP 挂 onAddValidators 注册
- **风险**：低（只在校验链加新校验 · 不动数据）

### 扩展入口坐标

- 绑定表单：`hrcs_dynascheme`
- 推荐父类：
  - OP：`HRDataBaseOp`（实证：`DynaAuthSchemeOp extends HRDataBaseOp` · `_auto_plugin_registry.md` L38）
  - Validator：`AbstractValidator`（标品实证：`DynaAuthSchemeValidator extends AbstractValidator`）
- 关键重写方法：
  - OP `onAddValidators(AddValidatorsEventArgs args)` — args.addValidator(new TdkwRoleEntryValidator())
  - OP `onPreparePropertys(PreparePropertysEventArgs args)` — 声明读 roleentry 字段
  - Validator `validate()` — 检查 roleentry.size() >= 1

### 调用链

```
用户点 save / submit
    ↓
DynaAuthSchemePlugin.beforeDoOperation 走规则校验 + 反查灌库 (L424-L495)
    ↓
进入 OP 链
    ↓
TdkwDynaSchemeRoleEntryOp.onPreparePropertys 声明读 roleentry
    ↓
TdkwDynaSchemeRoleEntryOp.onAddValidators 注册 TdkwRoleEntryValidator
    ↓
HRBaseDataStatusOp / HisModelOPCommonPlugin / HisUniqueValidateOp / DynaAuthSchemeOp / DynaAuthSchemeValidator (标品)
    ↓
TdkwRoleEntryValidator.validate() 遍历 ExtendedDataEntity[] · 检查 roleentry 大小
    ↓
size 0 → addErrorMessage(row, "方案至少要绑定 1 个角色") · 失败
size > 0 → 静默通过
```

### 代码框架

#### 1. 自建 OP（挂 save / submit / audit / confirmchange · 4 个 opKey 共用）

```java
package kd.${ISV_FLAG}.hrcs.opplugin.dyna;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class TdkwDynaSchemeRoleEntryOp extends HRDataBaseOp {        // PR-001 并列挂 · 不继承 DynaAuthSchemeOp

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);                              // 标品父类已声明 admingroup/boid/assignactionentry/cancelactionentry
        args.getFieldKeys().add("roleentry");                        // 我们要读 roleentry · 必须显式声明
        args.getFieldKeys().add("name");                             // 错误消息要带方案名
    }

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator) new TdkwRoleEntryValidator());
    }
}
```

#### 2. Validator 实现

```java
package kd.${ISV_FLAG}.hrcs.opplugin.dyna;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;

public class TdkwRoleEntryValidator extends AbstractValidator {

    @Override
    public void validate() {
        for (ExtendedDataEntity row : this.getDataEntities()) {
            DynamicObject scheme = row.getDataEntity();
            DynamicObjectCollection roleEntry = scheme.getDynamicObjectCollection("roleentry");
            if (roleEntry == null || roleEntry.isEmpty()) {
                this.addErrorMessage(row, ResManager.loadKDString(
                    "方案 [%s] 至少要绑定 1 个角色",                  // 业务消息 · 多语言
                    "TdkwRoleEntryValidator_1",
                    "tdkw-hrcs-opplugin",
                    new Object[]{ scheme.getLocaleString("name").getLocaleValue() }
                ));
            }
        }
    }
}
```

### 平台绑定（OP 注册）

1. 苍穹开发平台 → 定位表单 `hrcs_dynascheme`
2. 【注册插件】→ 新增（`targetType = OPERATION`）· 选 opKey = `save` · className 填 `kd.${ISV_FLAG}.hrcs.opplugin.dyna.TdkwDynaSchemeRoleEntryOp`
3. 重复上面步骤 · 给 `submit` / `audit` / `confirmchange` 各注册一份（同 className · 不同 opKey）
4. 部署生效

### 踩坑

- ❌ **不要继承 DynaAuthSchemeOp**（标品场景专属类 · PR-001）· 应该继承 `HRDataBaseOp`
- ❌ **不要在 beforeExecuteOperationTransaction 注册 Validator**：注册晚了不生效（PR-010）· 必须在 `onAddValidators` 阶段
- ❌ **不要在 Validator 里 model.setValue / model.getValue**：Validator 在 OP 端 · 没有 Form model · 用 `ExtendedDataEntity.getDataEntity()` 拿 DynamicObject（PR-003）
- ❌ **不要 throw KDBizException**：throw 异常会回滚整个事务 · 不友好。用 `addErrorMessage(row, msg)` · 平台会按行展示
- ⚠️ **PR-002 注意**：本 OP 跟标品 DynaAuthSchemeOp 在同 onAddValidators · 父级模板插件先 → 子实体插件后 · ISV 扩展元数据可以排在前 · 代码上看不出顺序差异（标品行为不变）
- ⚠️ 错误消息用 `ResManager.loadKDString` 多语言 · 不要硬编码中文（避免国际部署问题）
- ⚠️ confirmchange 的校验：本 Validator 也会跑 · 但 confirmchange 是变更场景 · 注意"用户已经把 roleentry 全删了想 confirm 一个空方案"会被拦 —— 这是预期行为

**遵循的 PR 规范**：PR-001（并列挂不继承）· PR-003（OP 用 entity / ExtendedDataEntity · 不用 model）· PR-010（onAddValidators 注册校验器）

### 关联 Pattern

`pattern/add_unique_validation/README.md` —— 同模式适用于"加唯一性校验" / "加金额阈值校验" / "加权限/范围校验"等 · 都是 onAddValidators + AbstractValidator 套路。

### 扩展：同时校验 assignactionentry 和 cancelactionentry 不空

业务上可能希望 authaction=1 时 assignactionentry 至少 1 行 · authaction=2 时 cancelactionentry 至少 1 行：

```java
@Override
public void validate() {
    for (ExtendedDataEntity row : this.getDataEntities()) {
        DynamicObject scheme = row.getDataEntity();
        String authaction = scheme.getString("authaction");
        DynamicObjectCollection roleEntry = scheme.getDynamicObjectCollection("roleentry");

        // 1. 角色清单非空（已有规则）
        if (roleEntry == null || roleEntry.isEmpty()) {
            this.addErrorMessage(row, ResManager.loadKDString("方案至少要绑定 1 个角色", "VK_1", "tdkw"));
            continue;
        }

        // 2. 按 authaction 校验对应分录
        if ("1".equals(authaction) || "3".equals(authaction)) {
            DynamicObjectCollection assignEntry = scheme.getDynamicObjectCollection("assignactionentry");
            if (assignEntry == null || assignEntry.isEmpty()) {
                this.addErrorMessage(row, ResManager.loadKDString("分配场景下必须配置至少 1 条分配分录", "VK_2", "tdkw"));
            }
        }
        if ("2".equals(authaction) || "3".equals(authaction)) {
            DynamicObjectCollection cancelEntry = scheme.getDynamicObjectCollection("cancelactionentry");
            if (cancelEntry == null || cancelEntry.isEmpty()) {
                this.addErrorMessage(row, ResManager.loadKDString("取消场景下必须配置至少 1 条取消分录", "VK_3", "tdkw"));
            }
        }
    }
}
```

⚠️ 标品 DynaAuthSchemePlugin.beforeDoOperation 已经做了 authaction=3 + condition 空的报错（L441）· 这里只是补 entry 维度。

### 与 confirmchange 的相互作用

confirmchange 也走 onAddValidators 链 · 你的 Validator 也会跑。这意味着：
- 用户已审核方案 · 改完后 confirmchange · 你的 Validator 同样校验 roleentry 不空
- 如果用户在变更时把所有 roleentry 删了想"取消方案的所有角色绑定" · 你的 Validator 会拦
- 业务上 · 想取消所有角色应该走 disable 不是 confirmchange · 你的 Validator 强制了这个约束 · 是合理的

---

## CS-04 · 删除/禁用方案前查下游引用（防误删）

**关联 Pattern**：`pattern/check_downstream_before_delete/README.md`

### 需求

业务方说：误删 / 禁用一个生产中的方案会导致大批员工权限突然丢失。希望 delete / disable 前 · 查下游 `hrcs_userrolerelat`（用户角色绑定表）· 如果有"由该方案分配出的活跃绑定"（sourcetype=4 + status=1） · 弹出明确提示并阻断。

### 推荐方案

- **扩展点**：自建 Validator + 自建 OP 挂 delete / disable 的 onAddValidators
- **风险**：中等（涉及跨表查询 + 阻断逻辑）
- **特点**：dynascheme 标品 delete 已经在 `DynaAuthSchemeListPlugin.afterDoOperation` 做了**事后**清理（清 5 张配置表）· 但**事前阻断**没做 · 这就是 ISV 要补的能力

### 扩展入口坐标

- 绑定表单：`hrcs_dynascheme`
- 推荐父类：
  - OP：`HRDataBaseOp`
  - Validator：`AbstractValidator`
- 关键重写方法：
  - OP `onAddValidators` 注册 TdkwDeleteCheckValidator
  - OP `onPreparePropertys` 声明读 boid

### 调用链

```
用户在列表选 N 条方案点【删除】（或【禁用】）→ opKey=delete / disable
    ↓
TdkwDynaSchemeBeforeDeleteOp.onPreparePropertys 声明读 boid + name
    ↓
TdkwDynaSchemeBeforeDeleteOp.onAddValidators 注册 TdkwDeleteCheckValidator
    ↓
TdkwDeleteCheckValidator.validate()
    ↓
对每行：取 boid · 查 hrcs_userrolerelat WHERE sourcetype=4 AND scheme=boid AND status=1
    ↓
存在记录 → addErrorMessage(row, "方案 [{name}] 已分配 {count} 条角色绑定·请先取消")
不存在 → 静默通过
    ↓
（如果通过）走标品删除链 + DynaAuthSchemeListPlugin.afterDoOperation 级联清 5 张表
```

### 代码框架

#### 1. 自建 OP

```java
package kd.${ISV_FLAG}.hrcs.opplugin.dyna;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class TdkwDynaSchemeBeforeDeleteOp extends HRDataBaseOp {     // PR-001 并列挂

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        args.getFieldKeys().add("boid");                             // 下游查找用 boid (PR-009)
        args.getFieldKeys().add("name");                             // 错误消息用
        args.getFieldKeys().add("status");                           // 仅查已审核状态的方案下游
    }

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator) new TdkwDeleteCheckValidator());
    }
}
```

#### 2. Validator 实现

```java
package kd.${ISV_FLAG}.hrcs.opplugin.dyna;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;

public class TdkwDeleteCheckValidator extends AbstractValidator {

    @Override
    public void validate() {
        for (ExtendedDataEntity row : this.getDataEntities()) {
            DynamicObject scheme = row.getDataEntity();
            long boid = scheme.getLong("boid");
            String name = scheme.getLocaleString("name").getLocaleValue();
            if (boid == 0L) continue;                                // 草稿方案 · 没有下游绑定

            // PR-009 下游引用必须用 boid · 不是 id
            // 同时按 sourcetype=4 过滤"由动态方案分配"的绑定（实证 DynaAuthSchemeListPlugin.java L154）
            QFilter[] filters = {
                new QFilter("scheme", "=", boid),                    // 注意：hrcs_userrolerelat.scheme 字段实际名待业务实证 · 这里假设
                new QFilter("sourcetype", "=", "4"),
                new QFilter("status", "=", "1")                      // 仅查活跃绑定
            };
            int count = QueryServiceHelper.queryDataSet(             // queryDataSet 防 N+1 · 先 count 再决定
                "TdkwDeleteCheckValidator-count",
                "hrcs_userrolerelat",
                "id",
                filters, null
            ).count("id", false);

            if (count > 0) {
                this.addErrorMessage(row, ResManager.loadKDString(
                    "方案 [%s] 已分配 %d 条角色绑定·请先在'分配记录'里取消所有绑定再删除",
                    "TdkwDeleteCheckValidator_1",
                    "tdkw-hrcs-opplugin",
                    new Object[]{ name, count }
                ));
            }
        }
    }
}
```

### 平台绑定

1. 注册插件：targetType = OPERATION · opKey = `delete` · className = `TdkwDynaSchemeBeforeDeleteOp`
2. 重复：opKey = `disable` 复用同 className
3. 部署生效

### 踩坑

- ❌ **下游查找用 id 而不是 boid**（PR-009）：方案 V2 audit 通过后 id 变了 · 但 boid 没变 · 下游绑定用 boid 跟踪 —— 用 id 查会查不到任何记录
- ❌ **没加 status=1 过滤**：会把"已禁用方案残留的旧绑定"也算进 count · 误报
- ❌ **用 BusinessDataServiceHelper 而不是 QueryServiceHelper**：load 一堆 DynamicObject 浪费内存 · count 用 queryDataSet 即可
- ❌ **Validator 里 throw KDBizException**：会让整批操作失败 · 不友好 · 用 addErrorMessage 平台按行报
- ⚠️ **hrcs_userrolerelat 字段名待业务实证**：本代码假设字段是 `scheme`（指向 dynascheme.boid）· 实际可能叫 `dynaschemeid` / `sourceid` 等 —— **生产用前必须从 `_shared/_standard_metadata/entity_metadata/hrcs_userrolerelat.md` 校对**（feedback_formid_no_fabrication.md 铁律）
- ⚠️ disable 标品在 `HRBaseDataEnableOp.beforeExecuteOperationTransaction` 已校验 status=C 已审核 · 你的 Validator 跑得早一些 · 没问题
- ⚠️ delete 时 `DynaAuthSchemeListPlugin.afterDoOperation` 会级联清 5 张配置表（dynaschemerange/dynaschorg/dynaschdimgrp/dynaschdatarule/dynaschfield） —— 但**清的是 boid=successPkIds 的 5 表行 · 不清 hrcs_userrolerelat**。你的 Validator 阻断的"hrcs_userrolerelat 残留" 才是真正容易丢的下游

**遵循的 PR 规范**：PR-001（并列挂不继承）· PR-003（OP 用 entity）· PR-009（下游用 boid 不用 id）· PR-010（onAddValidators 阶段）

### 关联 Pattern

`pattern/check_downstream_before_delete/README.md` —— 同模式适用于"删除组织前查岗位绑定" / "删除职位前查在职人员" / "禁用角色前查方案使用"等。

---

## CS-05 · 监听 confirmchange 后推送下游（BEC 发布方 · ISV 自建）

**关联 Pattern**：BEC 发布方模式

### 重要前置 · BEC grep 实证

按 `feedback_bec_3layer_async_publish.md` 铁律 · CS-05 写之前必须 grep 反编译产物：

```bash
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hrcs_dynascheme/
```

**实证结果（2026-04-28）**：

```
（0 处命中 · 标品没发任何 BEC 事件）
```

→ 同时 grep 了 OP/Plugin/Service 三层（按 `feedback_bec_3layer_async_publish.md` 完整套路扫了 OP + ServiceHelper + 后台 Task） · 都没找到 BEC 调用。

→ 结论：**hrcs_dynascheme 标品没发 BEC** · ISV 必须自建发布方（如果业务真有需要把方案变更通知下游系统）。这与 hjm（3 层异步发布）和 haos_structure（标品没发）属于"同一类（标品没发）"· 但与 homs（sch_task 派单）模式不同。

### 需求

业务方说：方案变更（confirmchange）后 · 需要通知 OA 系统刷新该方案影响的员工待办列表（OA 缓存了"哪些员工有哪些权限"）。希望以**业务事件**形式发出 · OA 通过 BEC 订阅消费。

### 推荐方案

- **扩展点**：自建 OP 挂 confirmchange 的 `afterExecuteOperationTransaction`（PR-010 · 主事务已提交）
- **实现模式**：自建 OP 调 `IEventService.triggerEventSubscribeJobs` 发 BEC 事件
- **风险**：中（必须先在【开发平台】配 eventNumber · 否则 BEC 不识）

### 扩展入口坐标

- 绑定表单：`hrcs_dynascheme`
- 推荐父类：`HRDataBaseOp`
- 关键重写方法：
  - `afterExecuteOperationTransaction(AfterOperationArgs args)` — 主事务已提交 · 调 IEventService.triggerEventSubscribeJobs

### 调用链

```
用户点 confirmchange （单据上）
    ↓
DynaAuthSchemePlugin.beforeDoOperation 走规则校验 + showChangeTips 二次确认 (L460-L471)
    ↓
进入 OP 链
    ↓
HRBaseDataLogOp / DynaAuthSchemeOp / DynaAuthSchemeConfirmChangeOp.endOperationTransaction (双写 boid + bgVid)
    ↓
事务提交（commit）
    ↓
TdkwDynaSchemeBecPublishOp.afterExecuteOperationTransaction (PR-010 · 主事务已提交安全)
    ↓
IEventService svc = ServiceHelper.getService(IEventService.class)
svc.triggerEventSubscribeJobs(
    "${ISV_FLAG}_hrcs",                                        // sourceApp
    "${ISV_FLAG}_dynascheme_changed",                          // eventNumber (在【开发平台】预配)
    "动态授权方案变更",                                  // message
    Map.of(
        "boid", boid,                                    // 业务对象 id (PR-009)
        "newVid", newVid,
        "name", schemeName,
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
| 编码 | `${ISV_FLAG}_dynascheme_changed` |
| 名称 | 动态授权方案变更 |
| 应用 | ${ISV_FLAG}_hrcs |
| 启用订阅 | true |
| 描述 | 方案 confirmchange 后触发 · variables 含 boid/newVid/name/operateAt |

→ 不预配 · `triggerEventSubscribeJobs` 调用时 BEC 不识别 eventNumber · 静默丢消息

#### 2. 自建 OP（发布方）

```java
package kd.${ISV_FLAG}.hrcs.opplugin.dyna;

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

public class TdkwDynaSchemeBecPublishOp extends HRDataBaseOp {       // PR-001 并列挂

    private static final Log LOG = LogFactory.getLog(TdkwDynaSchemeBecPublishOp.class);
    private static final String SOURCE_APP = "${ISV_FLAG}_hrcs";
    private static final String EVENT_NUMBER = "${ISV_FLAG}_dynascheme_changed";

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        args.getFieldKeys().add("boid");                             // PR-009 下游用 boid
        args.getFieldKeys().add("name");
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {  // PR-010 阶段 9 · 事务已提交
        super.afterExecuteOperationTransaction(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) return;

        IEventService svc = ServiceHelper.getService(IEventService.class);
        for (DynamicObject scheme : dataEntities) {
            long boid = scheme.getLong("boid");
            long newVid = scheme.getLong("id");
            String name = scheme.getLocaleString("name").getLocaleValue();

            // 注：variables 不要塞完整 DynamicObject · BEC 会序列化超大 · 走 JSON
            Map<String, Object> variables = new HashMap<>(8);
            variables.put("boid", boid);                             // 业务维度 (PR-009)
            variables.put("newVid", newVid);                         // 版本维度
            variables.put("name", name);
            variables.put("operateAt", System.currentTimeMillis());
            // 不要塞 roleentry/condition · 要查走 boid 反查

            try {
                svc.triggerEventSubscribeJobs(
                    SOURCE_APP,
                    EVENT_NUMBER,
                    "动态授权方案变更 · " + name,
                    variables
                );
                LOG.info("BEC 发布成功 · boid={} newVid={}", boid, newVid);
            } catch (Exception e) {
                LOG.error("BEC 发布失败 · boid=" + boid + " newVid=" + newVid, e);
                // ⚠ 不要抛异常回滚 · 主事务已提交 · 抛了也无意义
            }
        }
    }
}
```

#### 3. 平台绑定

注册插件：targetType = OPERATION · opKey = `confirmchange` · className = `TdkwDynaSchemeBecPublishOp`

如果还想监听 audit / disable / enable 时也发事件 · 重复注册同 className 到对应 opKey。

### 踩坑

- ❌ **不在 afterExecuteOperationTransaction 阶段发 BEC**（PR-010 / PR-011）：在 endOperationTransaction 阶段事务可能还没真提交 · 发了脏事件
- ❌ **不预配 eventNumber 直接调 triggerEventSubscribeJobs**（PR-011 prerequisite）：BEC 不识别 · 静默丢
- ❌ **variables 里塞完整 DynamicObject**（PR-011 antiPattern）：BEC 序列化大对象 · 网络传输巨慢 · 应只塞 boid + 必需小字段 · 订阅方需要详情自己反查
- ❌ **eventNumber 用大写 / 含中划线**：苍穹规约推荐小写 + 下划线 · 如 `${ISV_FLAG}_dynascheme_changed`
- ❌ **try-catch 后 throw 异常**：主事务已提交 · 抛异常无意义 · 只 log
- ❌ **下游用 newVid 不用 boid**：订阅方拿到 newVid 后查"这个版本的方案"会得到一个"已经被旧版本替代"的状态（如果再触发了一次 confirmchange）· 用 boid 反查永远是当前版本（PR-009）
- ⚠️ **BEC 是异步**：发完不等订阅方处理完成 · 如果业务要"OA 一定刷新成功"必须自建幂等机制（比如订阅方收到事件后回调一个"刷新成功" 接口）
- ⚠️ **list_op="1" 时 confirmchange 不可用**（标品 `DynaAuthSchemeConfirmChangeOp.endOperationTransaction` L29-L31 抛 KDBizException）· 你这个 OP 也不会跑 —— 列表批量 confirmchange 走不通

**遵循的 PR 规范**：PR-001（并列挂不继承）· PR-009（下游用 boid）· PR-010（afterExecuteOperationTransaction 阶段发外部事件）· PR-011（BEC API + eventNumber 预配 + variables 不塞 DynamicObject）

### 关联 Pattern

参考 `_shared/platform_rules.json` PR-011 的 publishAPI 部分（`bos-mservice-bec-api-8.0.jar` · `kd.bos.bec.api.IEventService.triggerEventSubscribeJobs` 实证）。

---

## CS-06 · confirmchange 后联动其他业务表（自定义版本同步逻辑）

**关联 Pattern**：confirmchange 联动模式（dynascheme 特色）

### 需求

业务方说：方案变更（confirmchange）后 · 业务部门希望同时把方案的变更内容写入"客户自建的方案变更日志表 `${ISV_FLAG}_dynaschemechgloghr`"（HisModel 时序表）· 用于审计 + BI 报表。

### 推荐方案

- **扩展点**：自建 OP 挂 confirmchange 的 `endOperationTransaction`（与主事务同事务 · 失败会回滚）
- **实现模式**：从 dataEntity 取 boid + bgVid + 变更前后内容 · INSERT 到 ISV 自建 HisModel 表
- **风险**：中（涉及自建 HisModel 表的 boid/sourcevid 维护）
- **特点**：dynascheme 的 confirmchange 是业务变更确认 · 标品 `DynaAuthSchemeConfirmChangeOp.endOperationTransaction` 已经做了"双写 boid + bgVid"（实证 `DynaAuthSchemeConfirmChangeOp.java` L25-L40）· ISV 想做同样的双写到自建表 · 必须模仿同套路

### 扩展入口坐标

- 绑定表单：`hrcs_dynascheme`
- 推荐父类：`HRDataBaseOp`
- 关键重写方法：
  - `endOperationTransaction(EndOperationTransactionArgs args)` — 与主事务同事务 · 失败回滚

### 调用链

```
用户点 confirmchange （单据 · list_op != "1"）
    ↓
DynaAuthSchemePlugin.beforeDoOperation 走规则校验 + showChangeTips
    ↓
进入 OP 链
    ↓
DynaAuthSchemeConfirmChangeOp.endOperationTransaction (标品双写)
    ↓
TdkwDynaSchemeChangeLogOp.endOperationTransaction (ISV)
   ├─ 取 dataEntity.boid + id (bgVid) + name + condition + ruledescription
   ├─ INSERT ${ISV_FLAG}_dynaschemechgloghr (含 boid / sourcevid / iscurrentversion = true)
   └─ UPDATE ${ISV_FLAG}_dynaschemechgloghr SET iscurrentversion=false WHERE boid=X AND id != bgVid
    ↓
afterExecuteOperationTransaction （事务提交后）
    ↓
（可选）TdkwDynaSchemeBecPublishOp 发 BEC （CS-05）
```

### 代码框架

#### 1. ISV 自建 HisModel 表（modifyMeta 创建 / IDEA 插件建）

`${ISV_FLAG}_dynaschemechgloghr` 关键字段：

| Field Key | 类型 | 含义 |
|---|---|---|
| `id` | BigInt | 版本 id |
| `boid` | BigInt | 业务对象 id（= dynascheme.boid） |
| `iscurrentversion` | CheckBox | 是否当前版本 |
| `sourcevid` | BigInt | 上一版本 id |
| `bsed` / `bsled` | Date | 生效/失效日期 |
| `dynaschemeboid` | BigInt | 关联的方案 boid |
| `oldcondition` | LargeText | 变更前规则 |
| `newcondition` | LargeText | 变更后规则 |
| `oldruledescription` | MuliLangText | 变更前规则摘要 |
| `newruledescription` | MuliLangText | 变更后规则摘要 |
| `changeoperator` | UserField | 变更操作人 |
| `changetime` | DateTime | 变更时间 |

#### 2. 自建 OP（写日志）

```java
package kd.${ISV_FLAG}.hrcs.opplugin.dyna;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.id.ID;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class TdkwDynaSchemeChangeLogOp extends HRDataBaseOp {        // PR-001 并列挂

    private static final String LOG_ENTITY = "${ISV_FLAG}_dynaschemechgloghr";
    private static final HRBaseServiceHelper LOG_HELPER = new HRBaseServiceHelper(LOG_ENTITY);

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        args.getFieldKeys().add("boid");
        args.getFieldKeys().add("name");
        args.getFieldKeys().add("condition");
        args.getFieldKeys().add("ruledescription");
    }

    @Override
    public void endOperationTransaction(EndOperationTransactionArgs args) {  // 与主事务同事务
        super.endOperationTransaction(args);
        // 列表批量 confirmchange 标品已抛 not support · 这里防御写一遍
        String isListOp = this.getOption().getVariableValue("list_op", "");
        if ("1".equals(isListOp)) return;

        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) return;
        DynamicObject scheme = dataEntities[0];

        long schemeBoid = scheme.getLong("boid");
        long bgVid = scheme.getLong("id");
        String name = scheme.getLocaleString("name").getLocaleValue();
        String newCondition = scheme.getString("condition");
        String newRuleDesc = scheme.getLocaleString("ruledescription").getLocaleValue();

        // 1. 查上一版日志（用于建 sourcevid 链路）
        DynamicObject lastLog = LOG_HELPER.queryOne("id,condition,ruledescription",
            new QFilter[]{
                new QFilter("dynaschemeboid", "=", schemeBoid),
                new QFilter("iscurrentversion", "=", Boolean.TRUE)    // PR-008
            });

        // 2. 把上一版的 iscurrentversion 由 true → false
        if (lastLog != null) {
            lastLog.set("iscurrentversion", Boolean.FALSE);
            LOG_HELPER.updateOne(lastLog);
        }

        // 3. 创建新版日志
        DynamicObject newLog = LOG_HELPER.generateEmptyDynamicObject();
        long newLogId = ID.genLongId();                              // PR-005 自建分录 id 用 ID 接口
        long newLogBoid = (lastLog != null) ? lastLog.getLong("boid") : newLogId;  // 首次 boid==id
        newLog.set("id", newLogId);
        newLog.set("boid", newLogBoid);
        newLog.set("iscurrentversion", Boolean.TRUE);                 // 新版本是当前
        newLog.set("sourcevid", lastLog != null ? lastLog.getLong("id") : 0L);
        newLog.set("dynaschemeboid", schemeBoid);
        newLog.set("oldcondition", lastLog != null ? lastLog.getString("condition") : null);
        newLog.set("newcondition", newCondition);
        newLog.set("oldruledescription", lastLog != null ? lastLog.getString("ruledescription") : null);
        newLog.set("newruledescription", newRuleDesc);
        newLog.set("changeoperator", RequestContext.get().getCurrUserId());
        newLog.set("changetime", new java.util.Date());

        LOG_HELPER.saveOne(newLog);
    }
}
```

#### 3. 平台绑定

注册插件：targetType = OPERATION · opKey = `confirmchange` · className = `TdkwDynaSchemeChangeLogOp`

### 踩坑

- ❌ **在 afterExecuteOperationTransaction 写日志**：日志写失败应该回滚整个 confirmchange · 但 afterExecute 阶段事务已提交 · 不能回滚 · 必须用 `endOperationTransaction`（PR-010）
- ❌ **不维护 iscurrentversion**：HisModel 时序场景必须保证一个 boid 下只有一行 iscurrentversion=true · 不维护就破坏时序模型（PR-008）
- ❌ **首次写入时 sourcevid != 0 但又没有上一版**：必须先查 · 有则维护链路 · 无则置 0
- ❌ **用 UUID.randomUUID 给 id 赋值**（违反 PR-005）：苍穹分布式 ID 用 `kd.bos.id.ID.genLongId()`
- ❌ **没考虑 list_op="1" 防御**：虽然标品抛 not support · 你的 OP 还是要先 check · 防 ISV 自建 list 流程绕过
- ❌ **多语言字段用 getString 取 LocaleString 对象**：拿到的是 toString · 不是 zh_CN 值。用 `getLocaleString("name").getLocaleValue()`
- ⚠️ **changeoperator 必须从 RequestContext 拿**：不要从 `getDataEntity().get("modifier")` —— modifier 是平台 ModifierField · confirmchange 时 · 标品已经把它设成当前用户 · 但 ISV 不依赖 · 直接 RequestContext 更稳
- ⚠️ **生成新 boid 的时机**：当 `lastLog == null` 表示首次写日志 · 此时 newLogBoid = newLogId（boid==id 标准）。后续 boid 复用第一次的 boid

**遵循的 PR 规范**：PR-001（并列挂不继承）· PR-003（OP 用 entity）· PR-005（ID.genLongId 生成行 id）· PR-008（HisModel iscurrentversion 维护）· PR-009（下游用 boid 跟踪业务对象）· PR-010（endOperationTransaction 阶段同事务写）

### 关联 Pattern

`pattern/his_model_log_table/README.md` —— 同模式适用于"为任何 HisModel 业务表加变更日志" / "审计追踪" 等场景。

---

## CS-07 · 列表过滤定制（按部门标签过滤显示）

**关联 Pattern**：HRDataBaseList 列表过滤模式

### 需求

业务方说：HR 总监希望列表上只显示"自己负责的事业部"对应的方案。即在 setFilter 阶段叠加一个 ISV 过滤：方案的 admingroup 必须挂在用户负责事业部下。

### 推荐方案

- **扩展点**：自建 ListPlugin 挂 `hrcs_dynascheme` 的 `setFilter`
- **实现模式**：继承 `HRDataBaseList` · setFilter 阶段叠加 QFilter
- **风险**：低（不破坏标品过滤 · 仅追加）
- **特点**：标品 `DynaAuthSchemeListPlugin.setFilter`（L130-L133）已经走了"按 boid 过滤可见方案"· ISV 加的 QFilter 会**与之 AND 组合**

### 扩展入口坐标

- 绑定表单：`hrcs_dynascheme`
- 推荐父类：`HRDataBaseList`（实证：`DynaAuthSchemeListPlugin extends HRDataBaseList` · `_auto_plugin_registry.md` L28）· **并列挂新插件 · 不继承 `DynaAuthSchemeListPlugin`**（PR-001）
- 关键重写方法：
  - `setFilter(SetFilterEvent evt)` — 叠加业务部门过滤

### 调用链

```
用户打开列表
    ↓
HRBaseDataTplList.beforeBindData (标品)
    ↓
HRBasedataLogList.beforeBindData (标品)
    ↓
HisModelListCommonPlugin.beforeBindData (标品 · @SdkInternal)
    ↓
DynaAuthSchemeListPlugin.beforeBindData (标品 · 检查 hisPage 隐藏 setadminrange)
    ↓
TdkwDynaSchemeBuFilterListPlugin.setFilter (ISV)
    ↓ DynaAuthSchemeListPlugin.setFilter (标品 · boid in queryViewableSchemes)
    ↓ TdkwDynaSchemeBuFilterListPlugin.setFilter (ISV · admingroup.bu in userBuIds)
    ↓
QFilter[] 全部 AND · 列表查询
    ↓
列表显示
```

### 代码框架

```java
package kd.${ISV_FLAG}.hrcs.formplugin.dyna;

import java.util.Set;
import kd.bos.context.RequestContext;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public class TdkwDynaSchemeBuFilterListPlugin extends HRDataBaseList {  // PR-001 并列挂 · 不继承 DynaAuthSchemeListPlugin

    @Override
    public void setFilter(SetFilterEvent evt) {
        super.setFilter(evt);                                         // 必须 super · 标品 setFilter 不放过

        long currUserId = RequestContext.get().getCurrUserId();
        Set<Long> myBuIds = TdkwUserBuQueryService.getMyBuIds(currUserId);   // ISV 自建 helper
        if (myBuIds == null || myBuIds.isEmpty()) {
            // 用户没绑事业部 · 不限定 · 让标品的 boid 过滤生效即可
            return;
        }

        // 叠加：方案的 admingroup 必须挂在用户事业部下
        // 注：admingroup → perm_admingroup → bu 字段名假设 · 实际名待 entity_metadata 校对
        QFilter buFilter = new QFilter("admingroup.bu.id", "in", myBuIds);
        evt.getQFilters().add(buFilter);
    }
}
```

### 平台绑定

1. 注册插件：targetType = LIST_FORM · 表单 = `hrcs_dynascheme` · className = `TdkwDynaSchemeBuFilterListPlugin`
2. 部署生效

### 踩坑

- ❌ **不要继承 DynaAuthSchemeListPlugin**：标品场景专属类 · ISV 继承会让 super 链耦合（PR-001）· 应继承 `HRDataBaseList`
- ❌ **不调 super.setFilter(evt)**：会丢标品的 boid 过滤 + 数据权限过滤 → 用户能看到不该看的方案
- ❌ **QFilter 用 admingroup.id 而不是关联点**：admingroup 是 BasedataField · 进 QFilter 要么用 `admingroup`（直接传 id 集）· 要么用 `admingroup.bu.id` 引用关联字段 —— 不要用 `admingroup_id`（不规范）
- ❌ **覆盖 QFilters 而不是 add**：`evt.setQFilters(myFilters)` 会**全部替换**标品的过滤 · 用 `evt.getQFilters().add(myFilter)` 追加
- ⚠️ admingroup → bu 字段名假设 —— **生产前必须从 `_shared/_standard_metadata/entity_metadata/perm_admingroup.md` 校对**（feedback_formid_no_fabrication.md 铁律）
- ⚠️ **admingroup F7 标品已限定 isdomain=1, domain.number=hr**（DynaAuthSchemePlugin.beforeF7Select L347-L349）· 你的 BU 过滤是**第二层** · 不重复
- ⚠️ **HisModel 时序**：admingroup 不是 HisModel · 但方案是 HisModel · 你过滤的是方案行 · 注意 setFilter 阶段拿到的是方案的 listView · 不需要再加 iscurrentversion 过滤（标品 list 默认带 iscurrentversion 过滤 · 看 HisModelListCommonPlugin）

**遵循的 PR 规范**：PR-001（并列挂不继承）· PR-003（FormPlugin 用 model · ListPlugin 用 evt）

### 关联 Pattern

`pattern/list_filter_extension/README.md` —— 同模式适用于"列表按 ISV 字段过滤"/"列表按用户角色显示不同行"等。

---

## 总结：7 个 CS 与 PR 引用矩阵

| CS | PR-001 | PR-003 | PR-004 | PR-005 | PR-007 | PR-008 | PR-009 | PR-010 | PR-011 |
|---|---|---|---|---|---|---|---|---|---|
| CS-01 加字段 | ✅ | - | - | ✅ | ✅ | ✅ | - | - | - |
| CS-02 字段联动 | ✅ | ✅ | ✅ | - | - | ✅ | - | - | - |
| CS-03 onAddValidators 校验 | ✅ | ✅ | - | - | - | - | - | ✅ | - |
| CS-04 删除前查下游 | ✅ | ✅ | - | - | - | - | ✅ | ✅ | - |
| CS-05 BEC 发布方 | ✅ | - | - | - | - | - | ✅ | ✅ | ✅ |
| CS-06 confirmchange 联动 | ✅ | ✅ | - | ✅ | - | ✅ | ✅ | ✅ | - |
| CS-07 列表过滤 | ✅ | - | - | - | - | - | - | - | - |

→ PR-001（并列挂不继承）100% 覆盖 · PR-008/PR-009 时序场景 50%+ 覆盖 · PR-010/PR-011 异步事件相关。

---

## 反编译类引用速查表

本文档明确引用的反编译类（实证位置）：

| 反编译类 | 引用 CS | 实证内容 |
|---|---|---|
| `DynaAuthSchemePlugin.java` | CS-01 / CS-02 / CS-03 / CS-04 / CS-05 | beforeBindData L207 / propertyChanged L682 / beforeDoOperation L424 / beforeF7Select L334 / closedCallBack L598 / setRequiredField L716 |
| `DynaAuthSchemeOp.java` | CS-03 / CS-04 | onPreparePropertys L21 / onAddValidators L28 |
| `DynaAuthSchemeSaveSubmitOp.java` | CS-03 / CS-06 | endOperationTransaction L21 |
| `DynaAuthSchemeAuditOp.java` | CS-06 | endOperationTransaction L23 (双分支 list_op) |
| `DynaAuthSchemeConfirmChangeOp.java` | CS-05 / CS-06 | endOperationTransaction L25 (boid + bgVid 双写) |
| `DynaAuthSchemeListPlugin.java` | CS-04 / CS-05 / CS-07 | setFilter L130 / afterDoOperation L143 (assignrecord) / afterDoOperation L168 (delete 级联) |
| `HRAdminStrictPlugin.java` | CS-07 | preOpenForm L29 (准入闸 · 不要继承复用即可) |

→ 所有 CS 的代码示例都来自反编译类的真实方法签名 + 苍穹 SDK 真实白名单（HRDataBaseOp / HRDataBaseEdit / HRDataBaseList / AbstractValidator / IEventService / kd.bos.id.ID）。**没有伪码 · 没有脑补类**（feedback_knowledge_code_samples_must_be_real.md 铁律）。
