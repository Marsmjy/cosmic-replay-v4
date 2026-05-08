# 推荐定制方案 · HR管理员（hrcs_useradmingroup）

> **状态**：🟢 基于真实 OpenAPI 实抓 + 5 类反编译（HRAdminGroupTreeListPlugin/HRAdminStrictPlugin/AdminGroupAddUserOp/AdminGroupDelUserOp/AdminGroupDelOp）+ `scene_doc.json` 2 字段语义整合
> **confidence**：real_deploy（所有扩展点类名均来自 `_auto_plugin_semantics.md` + `_auto_plugin_registry.md` 实证）

所有方案遵循统一结构（借鉴 Stripe / Salesforce Developer Docs）：
**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

---

## CS-01 · 给 hrcs_useradmingroup 加自定义字段（HR管理员组扩展属性）

**关联 Pattern**：`pattern/add_field_extension/README.md`

### 需求

业务方说：HR 管理员配置时需要加一个"管理员标签"字段（多选 · 可选 `主管理员 / 备管理员 / 临时管理员 / 受限管理员`），用于运营分类统计 · 在 hrcs 后台分类管理员行为。

### 推荐方案

- **扩展对象**：`hrcs_useradmingroup`（主实体 · 关联表）
- **扩展点**：`modifyMeta(op=add, elementType=field)` 或 IDEA 插件 Web UI 加字段
- **风险**：低（不动业务规则 · 只是数据展示位 · 本场景非 HisModel · 字段不会随版本复制）
- **特点**：useradmingroup 是 DynamicFormModel + 普通中间表 · ISV 加字段的**主要踩坑点**是要意识到字段挂在中间表上 · 一行 = 一对 (user, usergroup) · 加字段意味着每个用户在每个组里独立维护一份扩展属性

### 调用链（3 步）

```
Step 1: getBizApps()                        // 拿 hrcs 应用 bizAppId
Step 2: modifyMeta({
  formId: "hrcs_useradmingroup",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hrcs_useradmingroup",
    element: {
      fieldType: "MulComboField",                       // 多选枚举
      key: "${ISV_FLAG}_admintag",
      name: {zh_CN: "管理员标签", en_US: "Admin Tag"},
      mustInput: false,
      comboOptions: [
        {value: "A", name: {zh_CN: "主管理员", en_US: "Primary"}},
        {value: "B", name: {zh_CN: "备管理员", en_US: "Backup"}},
        {value: "C", name: {zh_CN: "临时管理员", en_US: "Temp"}},
        {value: "D", name: {zh_CN: "受限管理员", en_US: "Limited"}}
      ]
    }
  }]
})
Step 3: getFormSchema("hrcs_useradmingroup")    // ⭐ 二次验证落库（errorCode=0 不代表成功）
```

### 代码框架（使用 cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "hrcs_useradmingroup", "number": "hrcs_useradmingroup"}
)

designer.add_field(
    field_type="MulComboField",
    name="管理员标签",
    key="${ISV_FLAG}_admintag",
    parent_entity_id=designer.base_entity_id,
    combo_options=[
        {"value": "A", "name": {"zh_CN": "主管理员"}},
        {"value": "B", "name": {"zh_CN": "备管理员"}},
        {"value": "C", "name": {"zh_CN": "临时管理员"}},
        {"value": "D", "name": {"zh_CN": "受限管理员"}}
    ]
)
designer.save()  # 一次 save click
```

### 踩坑

- ❌ 字段 key 不带 ISV 前缀（如直接叫 `admintag`）→ 标品升级覆盖
- ❌ `fieldName` 列名超过 25 字符 → 苍穹平台开发规范限制 · 数据库建表失败
- ❌ `fieldName` 手动写 `fk_` 前缀 → 平台会再加 `f` → 列名变 `ffk_xxx` 怪列名 · **建议不传 fieldName 让平台按 `f + key.lowercase()` 自动生成**（坑 11）
- ❌ 多语言表 `_l` 命名规则：useradmingroup 当前**无 _l 表**（场景没 MuliLangTextField 字段）· ISV 加 MuliLangTextField 时不要假设会自动建 `_l` 表（实证：3.0 版本有些 form 加 MuliLangTextField 不带 _l 表 · 标品行为不一致）
- ❌ 误以为 ComboField 不需要 `comboOptions` → UI 下拉框空白
- ⚠️ 本场景非 HisModel · ISV 加的字段**不会随版本复制**（与 hrcs_dynascheme 不同）· 但仍要注意：删管理员组（do_remove_group）时 ISV 字段会跟主行一起删 · 这是 DynamicFormModel 默认级联（与 hrcs_useradmingroup 行 1:1 删除）

**遵循的 PR 规范**：
- PR-007（预置数据不可改 · 业务自建可改）· 本 CS 加的是业务字段 · 可随时删改
- PR-001（不继承场景专属类）· 加字段不涉及插件 · 但 ISV 后续给字段加业务逻辑要遵循

### 补充：分录子表行 id / 业务流水号生成规范（PR-005）

如果本场景未来扩展涉及**自建分录子表**（如给 useradmingroup 加"管理员日志"分录）· 或要写"管理员编码"自动生成 · 行 id / 流水号必须用 `kd.bos.id.ID`：

```java
import kd.bos.id.ID;

// 场景 1：扩展时新增分录行 · 填行 id（必须实证 · 不能自己造 UUID）
DynamicObject newRow = entries.addNew();
newRow.set("id", ID.genLongId());                // 长整型主键 · 19 位 Snowflake
newRow.set("changedesc", "管理员标签变更");

// 场景 2：自定义业务编码后缀 / 流水号
String traceId = ID.genStringId();               // 字符串 ID · 用于 traceId / 日志追踪号
String customNumber = "ADMINGRP_" + ID.genStringId();
```

实证：标品 `AdminGroupAddUserOp.saveUserAdminGroup` L86-L93 是用 `BusinessDataServiceHelper.newDynamicObject("hrcs_useradmingroup")` + `SaveServiceHelper.save` 走平台默认主键分配 · 没显式 ID.genLongId（也是合规的）。**ISV 自建分录走 ID 接口才合规**。

⚠ **反模式**（违反 PR-005 必驳回）：
- ❌ `UUID.randomUUID().toString()` · 苍穹有更轻量的 ID 接口
- ❌ `System.currentTimeMillis()` · 高并发会撞
- ❌ 自己 select max(id) + 1 · 分布式集群必坏

**遵循 PR-005 · 苍穹平台已集成分布式 ID（Snowflake）· ISV 不要自己 Redis incr 或 UUID 造**

### 验证

保存后到 **HR 通用服务 / 权限管理 / HR管理员菜单**右侧用户列表，看 user/usergroup 行上是否出现"管理员标签"字段。⚠️ 注意：因为是 TreeList 形态 · 字段加在右侧用户列表上 · 不是左树（左树是 perm_admingroup · ISV 不能加字段到 perm_admingroup）。

### 与其他场景命名一致性

| 场景 | CS-01 等价 | 差异点 |
|---|---|---|
| `hrcs_dynascheme` (CS-01) | 加"方案标签"字段到 hrcs_dynascheme | HisModel · 字段会随版本复制 |
| `admin_org_quick_maintenance` | 加"行政组织标签"字段到 haos_adminorg | HisModel · 含 audit/disable |
| `hrcs_useradmingroup` (CS-01) | 加"管理员标签"字段到 hrcs_useradmingroup | **唯一非 HisModel + 极简 2 字段中间表** · ISV 字段不随版本复制 |

→ Claude Code 跨场景时不要套用 dynascheme 的"加字段流程"细节 · 应该按本场景特有的 DynamicFormModel + 中间表行为走。

### 加分录字段（如给本场景加"管理员日志"分录）

如果 ISV 要在 hrcs_useradmingroup 加分录子表（罕见 · 一般加在 perm_admingroup 更合适）· 步骤：

```
modifyMeta({
  formId: "hrcs_useradmingroup",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "entry",
    parentScope: "hrcs_useradmingroup",
    element: {
      key: "${ISV_FLAG}_adminlogentry",
      name: {zh_CN: "管理员日志"},
      entryType: "EntryEntity"
    }
  }, {
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "${ISV_FLAG}_adminlogentry",
    element: {
      fieldType: "TextField",
      key: "${ISV_FLAG}_logmsg",
      name: {zh_CN: "日志内容"}
    }
  }]
})
```

⚠️ 子分录字段挂 EntryEntity 容器 · 走 `parentScope=${ISV_FLAG}_adminlogentry` · 不能直接挂 hrcs_useradmingroup 主体。

---

## CS-02 · 字段联动 · 管理员组分类下拉联动 ISV 自定义字段

**关联 Pattern**：`pattern/field_dependency/README.md`

### 需求

业务方说：选了 "管理员标签 = 主管理员" 后 · ISV 自建的 "${ISV_FLAG}_authlevel"（授权等级）字段要自动设置为 "全权限" · 选 "备管理员" 时设为 "只读" · 选 "临时/受限" 时禁用 ${ISV_FLAG}_authlevel 字段（不允许填）。

### 推荐方案

- **扩展对象**：`hrcs_useradmingroup` 主实体 · 通过 ISV 自定义 FormPlugin 实现
- **扩展点**：FormPlugin 挂 hrcs_useradmingroup · 重写 `propertyChanged` + `afterBindData`
- **风险**：中（要做 beginInit/endInit 防死循环 · 还要对 TreeList 模式特殊处理）

### 调用链

```
ISV 启动 → FormPlugin 注册到 hrcs_useradmingroup
  → afterBindData (新建/编辑模式都走) → 初始化 ${ISV_FLAG}_authlevel 状态
  → propertyChanged (用户改 ${ISV_FLAG}_admintag) → setValue/setEnable 联动
```

### 代码框架（Java · 实证 SDK 白名单内）

```java
package ${ISV_FLAG}.hrcs.formplugin.useradmingroup;

import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.entity.datamodel.IDataModel;

/**
 * ISV 自定义 FormPlugin 挂 hrcs_useradmingroup
 * 不继承 HRAdminGroupTreeListPlugin（场景专属类·禁继承·PR-001 红线）
 * 不继承 AbstractTreeListPlugin（重复 TreeList 逻辑·浪费）
 * 直接继承 AbstractFormPlugin · 并列挂 · 只做联动 · 其他 TreeList 行为由标品维护
 */
public class TdkwAdminTagAuthLevelLinkPlugin extends AbstractFormPlugin {

    private static final String FIELD_TAG = "${ISV_FLAG}_admintag";
    private static final String FIELD_AUTHLEVEL = "${ISV_FLAG}_authlevel";

    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        // TreeList 模式下右侧用户列表行不会触发 afterBindData · 这个生命周期主要用于全表单初始化
        // 但 hrcs_useradmingroup 主表单是 TreeList 容器 · afterBindData 会触发一次
        String tagValue = (String) this.getModel().getValue(FIELD_TAG);
        applyAuthLevel(tagValue);
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        String fieldName = e.getProperty().getName();
        if (FIELD_TAG.equals(fieldName)) {
            String newValue = (String) e.getChangeSet()[0].getNewValue();
            applyAuthLevel(newValue);
        }
    }

    private void applyAuthLevel(String tagValue) {
        IDataModel model = this.getModel();
        // PR-004 · setValue 必用 beginInit/endInit 防死循环
        model.beginInit();
        try {
            if ("A".equals(tagValue)) {                       // 主管理员
                model.setValue(FIELD_AUTHLEVEL, "FULL");
                this.getView().setEnable(Boolean.TRUE, FIELD_AUTHLEVEL);
            } else if ("B".equals(tagValue)) {                // 备管理员
                model.setValue(FIELD_AUTHLEVEL, "READONLY");
                this.getView().setEnable(Boolean.TRUE, FIELD_AUTHLEVEL);
            } else {                                          // 临时/受限/未填
                model.setValue(FIELD_AUTHLEVEL, "");
                this.getView().setEnable(Boolean.FALSE, FIELD_AUTHLEVEL);
            }
        } finally {
            model.endInit();
            this.getView().updateView(FIELD_AUTHLEVEL);
        }
    }
}
```

### 踩坑

- ❌ 直接 `model.setValue(FIELD_AUTHLEVEL, "FULL")` · 不带 beginInit/endInit → 触发再次 propertyChanged · **死循环**（PR-004 红线）
- ❌ 在 propertyChanged 里调 `getModel().setValue(field, ...)` 后忘了 `getView().updateView(field)` → 数据改了 · UI 不刷新（用户看不到联动）
- ❌ 在 OP 里写联动逻辑 · 不在 FormPlugin 里 → 后端无 model · setValue 报空指针（PR-003 红线）
- ❌ 假设 TreeList 列表行能直接 setValue → 列表行有自己的列模型 · 联动要走 `getView().getControl(列表key).beforeShowBillForm(...)` 而不是 model.setValue
- ⚠️ 联动逻辑在 hrcs_useradmingroup 主表单上挂 · 但 TreeList 模式下右侧的"用户列表"行联动不通过 FormPlugin · 而是通过 ListPlugin · 如果业务真要做"列表行内联动"· 需要做成"行级编辑模式"· 这是 TreeList 模式的一个**结构限制**（hrcs_useradmingroup 默认只读列表 · 用户加/删 走子页面 · 没有行级编辑场景）

**遵循的 PR 规范**：
- PR-001 · 不继承场景专属类（不继承 HRAdminGroupTreeListPlugin）
- PR-003 · FormPlugin 用 getModel().setValue
- PR-004 · setValue 必用 beginInit/endInit
- PR-007 · ISV 字段才能联动 · 不要联动标品 user/usergroup（破坏关联表语义）

### 验证

到 hrcs_useradmingroup 表单 · 点新增管理员行 · 改 ${ISV_FLAG}_admintag · 看 ${ISV_FLAG}_authlevel 是否自动改 + 是否禁用。

---

## CS-03 · do_add_user / do_remove_user 前置校验 · 防止删超管 + 防止重复加

**关联 Pattern**：`pattern/add_unique_validation/README.md`

### 需求

业务方说：
1. 删除用户操作（do_remove_user）必须防止删除"超管"用户（user.usertype 是某些特殊值时）
2. 添加用户（do_add_user）必须防止重复添加（虽然 FormPlugin 已经过滤了 · 但并发场景下两个会话同时点添加可能撞）
3. 这两条规则 ISV 自建 · 标品没做

### 推荐方案

- **扩展对象**：`do_add_user` opKey + `do_remove_user` opKey
- **扩展点**：ISV 自建 OP · 重写 `onAddValidators` 注册 Validator · 在 onAddValidators 阶段（PR-010 第 4 阶段）注册校验逻辑
- **风险**：中（涉及并发安全 · Validator 在 beforeExecute 之前执行 · 校验失败 OperationResult.isSuccess=false）

### 调用链

```
用户点添加用户 → FormPlugin 弹用户F7 → closedCallBack → executeOperate(do_add_user)
  → 苍穹框架 OP 链:
      AdminGroupAddUserOp (标品·没 onAddValidators)
      ↓ 并列挂
      TdkwAddUserValidatorOp (ISV·重写 onAddValidators 注册 Validator)
        → onAddValidators 阶段先执行所有 Validator
        → V_DAU_3 检查 hrcs_useradmingroup 中 user.id+usergroup.id 是否已存在
        → 存在则 addErrorMessage("用户已在该管理员组内")
      ↓ Validator 通过则继续
      AdminGroupAddUserOp.beginOperationTransaction (标品执行)
        → SaveServiceHelper.save
```

### 代码框架（Java · ISV 自建 OP + Validator）

```java
// 1. ISV 自建 OP（继承 HRDataBaseOp · PR-001 不继承 AdminGroupAddUserOp）
package ${ISV_FLAG}.hrcs.opplugin.useradmingroup;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class TdkwAddUserValidatorOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new TdkwAddUserValidator());
    }

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        // 声明 Validator 要读的字段（默认 OP 不读 user/usergroup · 必须 add）
        args.getFieldKeys().add("user");
        args.getFieldKeys().add("usergroup");
    }
}

// 2. Validator
package ${ISV_FLAG}.hrcs.validator.useradmingroup;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.dataentity.resource.ResManager;

public class TdkwAddUserValidator extends AbstractValidator {
    @Override
    public void validate() {
        for (ExtendedDataEntity row : this.getDataEntities()) {
            DynamicObject dataEntity = row.getDataEntity();
            DynamicObject user = dataEntity.getDynamicObject("user");
            DynamicObject usergroup = dataEntity.getDynamicObject("usergroup");

            // 1. 非空校验（V_DAU_1 / V_DAU_2）
            if (user == null) {
                this.addErrorMessage(row, ResManager.loadKDString(
                    "请选择用户。", "TdkwAddUserValidator_1", "tdkw-hrcs-validator", new Object[0]));
                continue;
            }
            if (usergroup == null) {
                this.addErrorMessage(row, ResManager.loadKDString(
                    "请选择管理员组。", "TdkwAddUserValidator_2", "tdkw-hrcs-validator", new Object[0]));
                continue;
            }

            // 2. 防重复（V_DAU_3 · 兜底 FormPlugin 的并发场景）
            QFilter qf = new QFilter("user", "=", user.getPkValue())
                            .and("usergroup", "=", usergroup.getPkValue());
            if (QueryServiceHelper.exists("hrcs_useradmingroup", new QFilter[]{qf})) {
                this.addErrorMessage(row, ResManager.loadKDString(
                    "用户%s已在管理员组%s内，请勿重复添加。", "TdkwAddUserValidator_3", "tdkw-hrcs-validator",
                    new Object[]{user.getString("name"), usergroup.getString("name")}));
            }
        }
    }
}

// 3. 删除用户的 Validator（同模式·略·见 V_DRU_3 跨组守护）
package ${ISV_FLAG}.hrcs.opplugin.useradmingroup;

public class TdkwRemoveUserValidatorOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new TdkwRemoveSuperAdminValidator());
    }
}

public class TdkwRemoveSuperAdminValidator extends AbstractValidator {
    private static final String SUPER_ADMIN_USERTYPE = "S";  // 业务自定义"超管"标志

    @Override
    public void validate() {
        for (ExtendedDataEntity row : this.getDataEntities()) {
            DynamicObject dataEntity = row.getDataEntity();
            DynamicObject user = dataEntity.getDynamicObject("user");
            if (user != null) {
                String userType = user.getString("usertype");
                if (SUPER_ADMIN_USERTYPE.equals(userType)) {
                    this.addErrorMessage(row, ResManager.loadKDString(
                        "不允许删除超级管理员用户%s。", "TdkwRemoveSuperAdminValidator_1",
                        "tdkw-hrcs-validator", new Object[]{user.getString("name")}));
                }
            }
        }
    }
}
```

### 注册（KingdeeCloud / IDEA 插件）

通过开发平台 · 给 do_add_user / do_remove_user opKey **并列挂**这两个 ISV 自建 OP：

```python
# 通过 ISV 扩展元数据（CS-01 同款 modifyMeta）
modifyMeta({
  formId: "hrcs_useradmingroup",  # 注意：扩展元数据要建在 ISV 自建 form
  ops: [{
    op: "registerPlugin",
    targetType: "OPERATION",                          # R20 · 必填大写枚举
    targetKey: "do_add_user",
    element: {
      pluginClass: "${ISV_FLAG}.hrcs.opplugin.useradmingroup.TdkwAddUserValidatorOp",
      enabled: true,
      description: "ISV 添加用户 Validator"
    }
  }]
})
```

### 踩坑

- ❌ 直接 `extends AdminGroupAddUserOp` · 然后 `super.beginOperationTransaction()` → PR-001 红线 · 场景专属类不允许继承
- ❌ 在 `beforeExecuteOperationTransaction` 里写校验 · 不在 onAddValidators → 校验晚了 · 已经过了 onAddValidators 阶段（PR-010 红线）
- ❌ Validator 内调 `BusinessDataServiceHelper.load` 全表加载 → 性能差 · 应该用 `QueryServiceHelper.exists` 走 SQL exists
- ❌ 在 Validator 里抛 `KDBizException` → 框架不会捕获 · 直接报 500 · 应该用 `addErrorMessage(row, msg)`
- ❌ 校验时只看 `user.id` 不看 `usergroup.id` → 不同管理员组允许同一用户加多次 · 不能用全表 user.id 唯一约束
- ⚠️ FormPlugin 阶段已经做了 SQL not in 过滤排除已加用户 · OP 层 Validator 是**兜底防御**（处理并发场景）· 不要假设永远不触发
- ⚠️ ISV 自建 Validator 必须 `onPreparePropertys` 声明读字段（PR-010 第 2 阶段）· 否则 dataEntity.getDynamicObject 返回 null

**遵循的 PR 规范**：
- PR-001 · 不继承场景专属类（不继承 AdminGroupAddUserOp / AdminGroupDelUserOp）
- PR-003 · OP 层用 dataEntity.getDynamicObject（不用 getModel）
- PR-007 · "超管"判定基于业务字段 usertype · 不写死某 user.id
- PR-010 · onAddValidators 注册 Validator · 不在 beforeExecute
- PR-011 · BEC 不引（本 CS 是同步校验 · 不发事件）

### 验证

1. 浏览器开两个标签页同时点添加同一用户 → 第二个会被 Validator 拦下 · 弹"用户已在管理员组内"
2. 给一个 usertype=S 的用户加到管理员组 · 再点删除 → 弹"不允许删除超级管理员"

---

## CS-04 · 删组前查 ISV 自建表反向引用（refentity_reverse）

**关联 Pattern**：`pattern/refentity_reverse_check/README.md`

### 需求

业务方说：ISV 自建了一张 `${ISV_FLAG}_admingroupext`（管理员组扩展配置表）· 表里有 `admingroup` 字段引用 `perm_admingroup`。删管理员组（do_remove_group）时 · 标品只清 9 张表（perm_admingroup* + hrcs_admingroup*）· **ISV 自建表 ${ISV_FLAG}_admingroupext 不会自动清**。如果 ISV 不做反向引用校验·就会出现"orphan 配置"（admingroup 已删但 ${ISV_FLAG}_admingroupext 还在 · 引用悬空）。

### 推荐方案

- **扩展对象**：`do_remove_group` opKey
- **扩展点**：ISV 自建 OP · 在 `onAddValidators` 阶段挂 Validator · 反查 ${ISV_FLAG}_admingroupext 是否引用本 admingroupId · 命中则拒绝删除（或选 cascade 删 ISV 表）
- **风险**：中（删组是高风险操作 · 漏查会造成数据脏）

### 调用链

```
用户点删组 → FormPlugin.adminGroupTreeRemoveOperation 4 道校验
  → 标品 checkHasRoleRef 反查 perm_role / hrcs_roleopenscope / hrcs_roleassignscope
  → 全过 → 弹确认对话框
  → 用户点 Yes → confirmCallBack → executeOperate(do_remove_group)
  → OP 链:
      TdkwAdminGroupExtRefValidatorOp (ISV·新增·onAddValidators)
        → V_DRG_3 反查 ${ISV_FLAG}_admingroupext.admingroup = ?
        → 命中 → addErrorMessage("管理员组被ISV配置引用")
      ↓ Validator 通过
      AdminGroupDelOp.beginOperationTransaction (9 表级联删)
      ↓ 同事务
      TdkwAdminGroupExtCascadeOp (ISV·新增·beginOperationTransaction)
        → DeleteServiceHelper.delete(${ISV_FLAG}_admingroupext, admingroup=adminGroupId)
```

### 代码框架（Java · 两套并列挂）

```java
// 方式 A: 拒绝删除（推荐 · 业务可控）
package ${ISV_FLAG}.hrcs.opplugin.useradmingroup;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class TdkwAdminGroupExtRefValidatorOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new TdkwAdminGroupExtRefValidator());
    }
}

import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.entity.DynamicObject;

public class TdkwAdminGroupExtRefValidator extends AbstractValidator {
    @Override
    public void validate() {
        // 注意：do_remove_group 的 dataEntity 里 usergroup 才是真正的 admingroupId
        // 因为 hrcs_useradmingroup 表是关联表 · usergroup 字段引用 perm_admingroup
        // 标品 confirmCallBack 构造 DO 时 set("usergroup", new perm_admingroup(adminGroupId))
        // 实证 HRAdminGroupTreeListPlugin.confirmCallBack L533-L538

        for (ExtendedDataEntity row : this.getDataEntities()) {
            DynamicObject dataEntity = row.getDataEntity();
            DynamicObject usergroup = dataEntity.getDynamicObject("usergroup");
            if (usergroup == null) continue;

            Long adminGroupId = (Long) usergroup.getPkValue();

            // 反查 ISV 自建扩展表
            QFilter qf = new QFilter("admingroup", "=", adminGroupId);
            if (QueryServiceHelper.exists("${ISV_FLAG}_admingroupext", new QFilter[]{qf})) {
                this.addErrorMessage(row, ResManager.loadKDString(
                    "管理员组已被ISV扩展配置引用，请先删除${ISV_FLAG}_admingroupext中相关记录。",
                    "TdkwAdminGroupExtRefValidator_1", "tdkw-hrcs-validator",
                    new Object[]{usergroup.getString("name")}));
            }
        }
    }
}

// 方式 B: cascade 删（高风险 · 业务必须明确知道 · 不推荐默认）
package ${ISV_FLAG}.hrcs.opplugin.useradmingroup;

import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.servicehelper.operation.DeleteServiceHelper;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class TdkwAdminGroupExtCascadeOp extends HRDataBaseOp {
    @Override
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        // 必须在标品 AdminGroupDelOp 同事务内（同样 begin* 阶段·实证标品也用此阶段）
        // 这样 ISV 表删失败时 · 主事务一起回滚 · 不会出现部分删
        for (DynamicObject dataEntity : e.getDataEntities()) {
            DynamicObject usergroup = dataEntity.getDynamicObject("usergroup");
            if (usergroup == null) continue;
            Long adminGroupId = (Long) usergroup.getPkValue();

            try {
                DeleteServiceHelper.delete("${ISV_FLAG}_admingroupext",
                    new QFilter[]{new QFilter("admingroup", "=", adminGroupId)});
            } catch (Exception ex) {
                // 与标品 AdminGroupDelOp 一致 · 抛 KDBizException 让事务回滚
                throw new KDBizException(ResManager.loadKDString(
                    "ISV 扩展配置删除失败，请联系管理员。", "TdkwAdminGroupExtCascadeOp_1",
                    "tdkw-hrcs-opplugin", new Object[0]));
            }
        }
    }
}
```

### 注册（同 CS-03）

```python
modifyMeta({
  formId: "hrcs_useradmingroup",
  ops: [{
    op: "registerPlugin",
    targetType: "OPERATION",
    targetKey: "do_remove_group",
    element: {
      pluginClass: "${ISV_FLAG}.hrcs.opplugin.useradmingroup.TdkwAdminGroupExtRefValidatorOp",
      enabled: true,
      description: "ISV 删组前 ${ISV_FLAG}_admingroupext 引用反查"
    }
  }]
})
```

### 踩坑

- ❌ 在 `afterExecuteOperationTransaction` 阶段做 cascade 删 → **PR-010 红线** · 主事务已提交 · ISV 表如果失败造成数据不一致 · 必须同事务（beginOperationTransaction 阶段）
- ❌ 反查时只查 admingroup id 不查 enable/status → 业务上"已禁用"的扩展配置可能不应阻拦删组（业务自定）· 加额外条件根据需求
- ❌ 直接读 `dataEntity.getString("usergroup")` 拿 ID → 应该用 `dataEntity.getDynamicObject("usergroup").getPkValue()` · 字符串读法返回的是显示名
- ❌ 假设 dataEntity 里有 `id` 是 admingroupId → 不对 · `id` 是 hrcs_useradmingroup 的主键（关联表 id）· admingroupId 在 `usergroup` 字段
- ⚠️ 9 表级联在 AdminGroupDelOp 内已做 · ISV cascade 删要明确不是 perm_admingroup* 范畴的（避免重复 delete 撞已删除主键）
- ⚠️ `do_remove_group` 的 dataEntity 是 hrcs_useradmingroup 实体 · 不是 perm_admingroup 实体 · 字段名混淆是高发坑

**遵循的 PR 规范**：
- PR-001 · 不继承 AdminGroupDelOp（场景专属类）
- PR-003 · OP 层用 dataEntity 不用 model
- PR-009 · 本场景非 HisModel · 但下游 perm_admingroup 是不是 HisModel 不在本场景 · ISV 反查目标表（${ISV_FLAG}_admingroupext）的 admingroup 字段类型要查 _shared/_standard_metadata
- PR-010 · 同事务用 beginOperationTransaction
- PR-011 · BEC 不引（本 CS 是同步校验/cascade）

### 验证

1. 在 ${ISV_FLAG}_admingroupext 加一条 admingroup=X 的记录
2. 到 hrcs_useradmingroup 删 X 组 → 应弹"管理员组已被ISV扩展配置引用..."
3. 删除 ${ISV_FLAG}_admingroupext 那条记录 → 再删 X 组 → 应通过

---

## CS-05 · BEC 跨模块通知（已砍 · 标品 0 处发布）

### 决策：本场景**不推荐 BEC 自建发布方**

> ⚠️ 严格按铁律 5：先 grep 反编译，0 处则砍。

### 实证 grep 结果

```bash
$ grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
       knowledge/_sdk_audit/_decompiled/scenarios/hrcs_useradmingroup/
# 输出：（空）
```

5 个反编译类（HRAdminGroupTreeListPlugin / HRAdminStrictPlugin / AdminGroupAddUserOp / AdminGroupDelUserOp / AdminGroupDelOp）**全部 0 处**调用 BEC 发布 API。这意味着：

1. **标品没发** · 所以也没标品订阅方在等
2. **ISV 主动发** · 没有现成订阅方接收（其他 hrcs 模块对此事件无监听）
3. **ISV 自建发布 + 自建订阅** · 等于绕开 BEC 直接 ISV 内部走 RPC · BEC 没价值

### 如果业务真要做跨系统通知（罕见）

唯一有价值的场景：**HR 管理员变更需要同步到外部 OA / SSO 系统**。但即使如此，推荐的方案是：

- **方式 1（推荐）**：ISV 自建定时任务 · 每 5 分钟 query hrcs_useradmingroup · 增量同步到外部
- **方式 2（次选）**：ISV 自建 OP 挂 do_add_user / do_remove_user 的 `afterExecuteOperationTransaction`（PR-010 第 9 阶段）· 主事务已提交 · 调外部 SSO API · 失败重试不影响 OP
- **方式 3（不推荐）**：ISV 自建 BEC 发布 + 自建订阅 · 绕一圈不如直接调 API

### 反模式

- ❌ ISV 在 `do_add_user` 的 `endOperationTransaction` 阶段调外部 API → 事务可能还没最终提交 · 外部 API 收到事件而本地数据回滚 · **数据不一致**（PR-010 红线）
- ❌ ISV 在 OP 内做长耗时操作（如调外部 HTTP API）· 阻塞事务 · 锁住主表
- ❌ 假设苍穹有"do_add_user 完成"事件可订阅 → 没有 · 标品没注册任何 BEC 事件号

参考：`hrcs_dynascheme/06_customization_solutions.md` CS-05 也是同结论（标品 0 处 BEC 发布）。**hrcs 域基本不发 BEC**。

**遵循 PR-011 · 苍穹平台已集成 BEC · 但本场景标品 0 处发布 · ISV 强烈不建议自建发布方**

---

## CS-06 · 树形列表节点过滤 · ISV 域管理员只见自己组（IAdminGroupListSubExtPlugin SDK 扩展）

**关联 Pattern**：`pattern/sdk_extpoint_implementation/README.md`

### 需求

业务方说：标品的添加用户 F7 已经做了 5 道范围过滤（已加用户 / 自己 / 同 scheme 不同 type / enable=1 / usertype 多值）· 但 ISV 域有自己的额外要求：**只允许把"业务部门负责人"加为管理员**（业务部门负责人通过 ISV 自建表 `${ISV_FLAG}_deptleader.userid` 维护）。

### 推荐方案 · ISV 实现 SDK 扩展点 IAdminGroupListSubExtPlugin

**关键发现**（反编译实证）：`HRAdminGroupTreeListPlugin extends ... implements IAdminGroupListSubExtPlugin`（L128-L129）·`showUserF7TreeList` 内部 L432-L438 通过 `HRPluginProxy.callAfter` 回调所有实现 `IAdminGroupListSubExtPlugin` 的 ISV 类。这是**唯一稳定的 ISV 用户F7范围扩展点**。

- **扩展对象**：`hrcs_useradmingroup` 用户 F7（addUser 流程）
- **扩展点**：`kd.sdk.hr.hbp.business.extpoint.permission.hradmi.IAdminGroupListSubExtPlugin#beforeAddCustomUser(AddCustomUserEventArgs)`
- **风险**：低（SDK 接口稳定 · 不破坏标品逻辑）

### 调用链

```
用户点添加用户 → FormPlugin.beforeDoOperation
  → showUserF7TreeList:
      [标品 5 道过滤 已应用]
      ↓
      AddCustomUserEventArgs eventArgs = new AddCustomUserEventArgs();
      eventArgs.setLsp(lsp);
      HRPluginProxy proxy = new HRPluginProxy(this, IAdminGroupListSubExtPlugin.class, ..., null);
      proxy.callAfter(p -> p.beforeAddCustomUser(eventArgs));   ⭐ ISV 在这里回调
      ↓
      [ISV 加自己的 QFilter 到 lsp.getListFilterParameter().getQFilters()]
      ↓
      lsp.getListFilterParameter().getQFilters().addAll(rangeFilterList);
      ↓
      this.getView().showForm(lsp);   // 弹用户F7
```

### 代码框架（Java · ISV 实现 SDK 接口）

```java
package ${ISV_FLAG}.hrcs.extpoint.useradmingroup;

import kd.sdk.hr.hbp.business.extpoint.permission.hradmi.IAdminGroupListSubExtPlugin;
import kd.sdk.hr.hbp.business.extpoint.permission.hradmi.AddCustomUserEventArgs;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;

import java.util.HashSet;
import java.util.Set;

/**
 * ISV 用户F7范围扩展实现
 * 通过 KingdeeCloud 注册到 hrcs_useradmingroup 的扩展点
 * 不继承场景专属类·只实现 SDK 接口
 */
public class TdkwDeptLeaderOnlyExt implements IAdminGroupListSubExtPlugin {
    @Override
    public void beforeAddCustomUser(AddCustomUserEventArgs eventArgs) {
        ListShowParameter lsp = eventArgs.getLsp();
        if (lsp == null) return;

        // 1. 查 ISV 自建表 ${ISV_FLAG}_deptleader · 拿到所有"部门负责人"用户ID
        Set<Long> deptLeaderUserIds = new HashSet<>();
        try (DataSet ds = QueryServiceHelper.queryDataSet(
                "TdkwDeptLeaderOnlyExt-loadDeptLeaders",
                "${ISV_FLAG}_deptleader",
                "userid",
                new QFilter[]{new QFilter("enable", "=", "1")},
                null)) {
            for (Row row : ds) {
                Long uid = row.getLong("userid");
                if (uid != null) deptLeaderUserIds.add(uid);
            }
        }

        if (deptLeaderUserIds.isEmpty()) {
            // 没有部门负责人 → 强制空集合（无人可加）
            lsp.getListFilterParameter().getQFilters().add(new QFilter("id", "in", new HashSet<>()));
            return;
        }

        // 2. 加 in 过滤（与标品已有过滤组合 AND）
        QFilter deptLeaderFilter = new QFilter("id", "in", deptLeaderUserIds);
        lsp.getListFilterParameter().getQFilters().add(deptLeaderFilter);
    }
}
```

### 注册（在 IDEA 插件 / KingdeeCloud 配置）

通过开发平台 · 注册扩展点实现：

```yaml
# 在 ISV 工程的 META-INF/extension-config.yaml 或者 KingdeeCloud config 里
extensions:
  - extPoint: kd.sdk.hr.hbp.business.extpoint.permission.hradmi.IAdminGroupListSubExtPlugin
    implementation: ${ISV_FLAG}.hrcs.extpoint.useradmingroup.TdkwDeptLeaderOnlyExt
    targetForm: hrcs_useradmingroup    # 限定只对本场景生效
```

⚠️ 实际注册方式取决于开发平台版本 · 8.x 版本可能用 `KingdeeCloud config` 或 `ServiceLoader` · ISV 工程文档为准。

### 踩坑

- ❌ 不实现 SDK 接口而是直接继承 HRAdminGroupTreeListPlugin → PR-001 红线 · 场景专属类
- ❌ 实现 beforeAddCustomUser 但**清空** lsp.getListFilterParameter().getQFilters() → 破坏标品 5 道过滤 · 只用 ISV 过滤是错的
- ❌ 在 beforeAddCustomUser 里调 `eventArgs.setLsp(newLsp)` · 把整个 lsp 替换 → 标品已设的 closeCallBack / customParam 全丢 · ISV 只应**追加** QFilter
- ❌ 假设 IAdminGroupListSubExtPlugin 在所有 hrcs 表单都触发 → 不是 · 只在 hrcs_useradmingroup 的 showUserF7TreeList 里调 · 其他场景没有这个钩子
- ⚠️ HRPluginProxy 的 callAfter 模式 · 如果 ISV 的 beforeAddCustomUser 抛异常 → 会被吞 · 用户 F7 仍正常弹（只是没 ISV 过滤生效）· ISV 异常应主动 logger.error 留痕
- ⚠️ 多个 ISV 扩展实现都注册时 · 执行顺序按注册顺序 · 后 ISV 的 QFilter 加在前 ISV 之上（AND 关系）

**遵循的 PR 规范**：
- PR-001 · 不继承场景专属类（不继承 HRAdminGroupTreeListPlugin）· 只实现 SDK 接口
- PR-007 · ISV 用 ISV 自建表（${ISV_FLAG}_deptleader）· 不破坏标品 bos_user

### 验证

1. 在 ${ISV_FLAG}_deptleader 加 3 个用户 ID
2. 到 hrcs_useradmingroup 点添加用户 → 弹的 F7 应只显示这 3 个用户（在标品 5 道过滤 + ISV 过滤的交集内）
3. 把 ${ISV_FLAG}_deptleader.enable 改 0 → F7 应空（无人可加）

---

## CS-07 · 批量授权 donothing_batch_perm 自动化扩展（在子页面挂插件）

**关联 Pattern**：`pattern/child_form_extension/README.md`

### 需求

业务方说：批量授权管理员组（donothing_batch_perm）会弹 hrcs_amingroupbatchauth 子页面 · 用户在子页面手动配置后再保存 · 业务希望"按预定模板自动套配置"——比如点批量授权后 · 子页面自动加载 ISV 配置的"标准授权模板"（比如 admingroup 已有的"通用主管理员模板"）· 减少 80% 手动操作。

### 推荐方案

- **扩展对象**：`hrcs_amingroupbatchauth` 子页面（注意：**不是本场景** · 是子页面）
- **扩展点**：在 `hrcs_amingroupbatchauth` 上挂 ISV FormPlugin · 重写 `beforeBindData` · 根据 type=batchAuth 检查是否启用 ISV 模板 · 启用则自动设默认值

### 调用链

```
用户点批量授权 → FormPlugin.beforeDoOperation
  → adminGroupTreeBatchPermOperation:
      verifyBatchAuth 通过
      bsp.setFormId("hrcs_amingroupbatchauth")
      bsp.getCustomParams().put("adminGroupParentId", adminGroupId)
      bsp.getCustomParams().put("level", arr[1])
      bsp.getCustomParams().put("type", "batchAuth")
      this.getView().showForm(bsp)
        ↓
      hrcs_amingroupbatchauth (子页面打开)
        ↓
      [ISV 挂的 FormPlugin].beforeBindData
        → 读 customParam.type = "batchAuth"
        → 读 ISV 自建表 ${ISV_FLAG}_authtemplate · 拿模板内容
        → setValue 自动填充子页面字段
```

### 代码框架（Java · 挂子页面 · 不是本场景）

```java
package ${ISV_FLAG}.hrcs.formplugin.batchauth;

import kd.bos.form.events.BeforeBindDataEvent;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.form.plugin.AbstractFormPlugin;

import java.util.EventObject;

/**
 * ISV 批量授权子页面自动填模板插件
 * 挂 hrcs_amingroupbatchauth · 不是 hrcs_useradmingroup
 * 但跟本场景紧密关联（由 donothing_batch_perm 触发跳转）
 */
public class TdkwBatchAuthAutoTemplatePlugin extends AbstractFormPlugin {
    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        // 只在 batchAuth 模式下生效
        String type = (String) this.getView().getFormShowParameter().getCustomParam("type");
        if (!"batchAuth".equals(type)) return;

        // 读 ISV 自建模板
        DynamicObject template = QueryServiceHelper.queryOne(
            "${ISV_FLAG}_authtemplate",
            "id, funcperm, orgrange, fileperm",
            new QFilter[]{
                new QFilter("scenario", "=", "default"),
                new QFilter("enable", "=", "1")
            });
        if (template == null) return;

        IDataModel model = this.getModel();
        model.beginInit();
        try {
            // 自动填子页面字段（具体字段名取决于 hrcs_amingroupbatchauth 元数据）
            model.setValue("funcperm", template.getString("funcperm"));
            model.setValue("orgrange", template.getString("orgrange"));
            model.setValue("fileperm", template.getString("fileperm"));
        } finally {
            model.endInit();
            this.getView().updateView("funcperm");
            this.getView().updateView("orgrange");
            this.getView().updateView("fileperm");
        }
    }
}
```

### 踩坑

- ❌ 把这个插件挂在 hrcs_useradmingroup 上 → 触发不到（因为 beforeBindData 是子页面的生命周期 · 不是父页面）
- ❌ 读不到 customParam.type → 检查是否在 `afterBindData` 阶段读 · 如果在 `beforeBindData` 阶段子页面 customParam 可能还没就绪
- ❌ 没判断 type=batchAuth → 子页面可能被其他场景以 type=A/B/C 复用 · ISV 模板乱套
- ❌ ISV 自建模板表 ${ISV_FLAG}_authtemplate 的字段没和 hrcs_amingroupbatchauth 字段对齐 → setValue 报"字段不存在"
- ⚠️ 子页面有自己的标品插件链 · ISV 加 beforeBindData 注意 super 调用顺序 · 不要破坏其他插件
- ⚠️ 如果 ISV 想加一个"批量授权确认弹窗"· 那要重写子页面的 OP · 不是本 CS

**遵循的 PR 规范**：
- PR-001 · 挂在 hrcs_amingroupbatchauth 而不是继承 · 即使要做"自动化"行为也走 FormPlugin 并列挂
- PR-003 · FormPlugin 用 model.setValue
- PR-004 · setValue 必用 beginInit/endInit
- PR-007 · ISV 模板由 ISV 自建表维护 · 不写死

### 验证

1. 在 ${ISV_FLAG}_authtemplate 配 scenario=default + enable=1 · 填好 funcperm/orgrange/fileperm
2. 到 hrcs_useradmingroup 选某个非明细组 · 点批量授权
3. 看弹出的 hrcs_amingroupbatchauth 子页面 · funcperm/orgrange/fileperm 是否自动填好

---

## 跨场景对齐表 · 7 个 CS 与其他场景对照

| CS # | 本场景 | hrcs_dynascheme | admin_org_quick_maintenance | 共性 |
|---|---|---|---|---|
| CS-01 加自定义字段 | DynamicFormModel · 不随版本复制 | BillFormModel + HisModel · 字段随版本 | HisModel · 字段随版本 | modifyMeta + ISV 前缀 |
| CS-02 字段联动 | TreeList 行内联动有限制 | 主表 + 6 分录联动复杂 | TreeList · 同模式 | beginInit/endInit 防死循环 |
| CS-03 OP 前置校验 | onAddValidators 注册 | onAddValidators · 含 confirmchange | onAddValidators · 含 audit | AbstractValidator + addErrorMessage |
| CS-04 删/audit 前反查 | do_remove_group 9 表级联 + ISV 表 | confirmchange 多表灌库 + 反查 | disable 反查 | 同事务 beginOperationTransaction |
| CS-05 BEC | **0 处发布 · 砍** | 0 处发布 · 砍 | 0 处发布 · 砍 | hrcs/haos 域基本不发 BEC |
| CS-06 SDK 扩展点 | IAdminGroupListSubExtPlugin · 用户F7 | （hrcs_dynascheme 没有同模式 SDK 扩展） | （admin_org 没有同模式 SDK 扩展） | **本场景独有** · 反编译实证 |
| CS-07 子页面联动 | 挂 hrcs_amingroupbatchauth | 挂 hrcs_dyscassignroledetail | 挂 haos_adminorgdetail | 子页面 customParam.type 区分模式 |

→ Claude Code 跨场景生成代码时 · 不要把 hrcs_dynascheme 的 HisModel 模式套到本场景 · 也不要把 hrcs_useradmingroup 的 SDK 扩展点（IAdminGroupListSubExtPlugin）套到其他场景。
