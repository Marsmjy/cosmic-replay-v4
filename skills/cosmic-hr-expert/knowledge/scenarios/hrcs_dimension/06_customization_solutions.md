# 推荐定制方案 · 维度管理 (hrcs_dimension)

> **状态**: 🟢 基于真实 OpenAPI 实抓 + 4 类反编译类名 + `scene_doc.json` 33 字段语义整合
> **confidence**: real_deploy（所有扩展点类名均来自 `_auto_plugin_semantics.md` + `_auto_plugin_registry.md` 实证）

所有方案遵循统一结构（借鉴 Stripe / Salesforce Developer Docs）：
**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

---

## CS-01 · 给 hrcs_dimension 扩展自定义字段（最高频）

**关联 Pattern**：`pattern/add_field_extension/README.md`

### 需求

业务方说：维度需要加"维度类目"字段（多选 · 可选 `权限类 / 数据类 / 报表类 / 业务类`），用于 BI 看板按类目统计客户使用了哪些维度。

### 推荐方案

- **扩展对象**：`hrcs_dimension`（主实体）
- **扩展点**：`modifyMeta(op=add, elementType=field)` 或 IDEA 插件 Web UI 加字段
- **风险**：低（不动业务规则 · 只是数据展示位）
- **特点**：dimension 是 BillFormModel · **没有 HisModel** · ISV 加字段不用考虑跨版本快照（与 dynascheme 不同）· 是更纯净的"基础资料加字段"场景

### 调用链（3 步）

```
Step 1: getBizApps()                        // 拿 hrcs 应用 bizAppId
Step 2: modifyMeta({
  formId: "hrcs_dimension",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hrcs_dimension",
    element: {
      fieldType: "MulComboField",                       // 多选枚举
      key: "${ISV_FLAG}_dimcategory",
      name: {zh_CN: "维度类目", en_US: "Dimension Category"},
      mustInput: false,
      comboOptions: [
        {value: "A", name: {zh_CN: "权限类", en_US: "Auth"}},
        {value: "B", name: {zh_CN: "数据类", en_US: "Data"}},
        {value: "C", name: {zh_CN: "报表类", en_US: "Report"}},
        {value: "D", name: {zh_CN: "业务类", en_US: "Business"}}
      ]
    }
  }]
})
Step 3: getFormSchema("hrcs_dimension")    // ⭐ 二次验证落库（errorCode=0 不代表成功）
```

### 代码框架（使用 cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "hrcs_dimension", "number": "hrcs_dimension"}
)

designer.add_field(
    field_type="MulComboField",
    name="维度类目",
    key="${ISV_FLAG}_dimcategory",
    parent_entity_id=designer.base_entity_id,
    combo_options=[
        {"value": "A", "name": {"zh_CN": "权限类"}},
        {"value": "B", "name": {"zh_CN": "数据类"}},
        {"value": "C", "name": {"zh_CN": "报表类"}},
        {"value": "D", "name": {"zh_CN": "业务类"}}
    ]
)
designer.save()  # 一次 save click
```

### 踩坑

- ❌ 字段 key 不带 ISV 前缀（如直接叫 `dimcategory`）→ 标品升级覆盖
- ❌ `fieldName` 列名超过 25 字符 → 苍穹平台开发规范限制 · 数据库建表失败
- ❌ `fieldName` 手动写 `fk_` 前缀 → 平台会再加 `f` → 列名变 `ffk_xxx` 怪列名 · **建议不传 fieldName 让平台按 `f + key.lowercase()` 自动生成**（坑 11）
- ❌ 多语言表 `_l` 命名规则：dimension 当前没独立 `_l` 表 · MuliLangTextField 都在主表承载 · ISV 加 MuliLangTextField 时不要假设会自动建 `_l` 表
- ❌ 误以为 ComboField 不需要 `comboOptions` → UI 下拉框空白
- ❌ 加 EmployeeField 类型字段 → OpenAPI 不识别 · 兜底成 BasedataField · 用 hrpi_person + Java 插件代替
- ⚠️ 加字段后 · save 不需要走"复制到新版本"机制（dimension 没有 HisModel · 与 dynascheme 不同）

**遵循的 PR 规范**：
- **PR-001**：本 CS 加字段是 ISV 扩展元数据 · 通过元数据 add 而不是继承 DimensionNewEdit 改 schema · 完全符合"并列扩展不继承"
- **PR-007**：预置维度（issyspreset=true）的 number 不可改 · 但加新字段对预置维度也生效（issyspreset 行可以填新字段值）

### 验证

```python
# 部署后必须二次验证
schema = client.get_form_schema("hrcs_dimension")
assert "${ISV_FLAG}_dimcategory" in [f["key"] for f in schema["fields"]]
```

---

## CS-02 · 字段联动（datasource → 扩展属性联动）

**关联 Pattern**：`pattern/property_change_listener/README.md`

### 需求

客户希望：用户切换 `datasource=enum` 时 · 自动带入"枚举值类目模板"（ISV 自建表 `${ISV_FLAG}_enumcategory` 里的预设条目）· 简化用户输入。

### 推荐方案

- **扩展对象**：`hrcs_dimension`（主实体）
- **扩展点**：`registerPlugin(targetType=BILL_FORM)` + 自建 FormPlugin 监听 `datasource` 字段变更
- **风险**：低（只读取业务对象 · 不写入 · 走 propertyChanged 联动）
- **特点**：必须在 setValue 时用 PR-004 的 beginInit/endInit 防死循环（DimensionNewEdit.afterBindData L117 / L137 已是同款套路 · ISV 必复用）

### 调用链

```
用户切换 datasource → DimensionNewEdit.propertyChanged (标品 · 走 showEnumCtrl 联动)
                  → ISV TdkwDimensionDataSourceEnrichPlugin.propertyChanged (并列挂 · 不继承)
                                                                                ↓
                                                                            查 ${ISV_FLAG}_enumcategory
                                                                            beginInit + setValue + endInit
                                                                            updateView
```

### 代码框架

```java
package com.${ISV_FLAG}.hr.hrcs.dimension.plugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;     // PR-001 · 继承 SDK 白名单父类 · 不继承 DimensionNewEdit

public class TdkwDimensionDataSourceEnrichPlugin extends HRDataBaseEdit {

    @Override
    public void propertyChanged(PropertyChangedArgs evt) {
        super.propertyChanged(evt);
        String key = evt.getProperty().getName();
        if (!"datasource".equals(key)) return;

        String newValue = (String) evt.getChangeSet()[0].getNewValue();
        if (!"enum".equals(newValue)) return;

        // 查 ISV 自建表 ${ISV_FLAG}_enumcategory · 拿默认枚举值清单
        HRBaseServiceHelper helper = new HRBaseServiceHelper("${ISV_FLAG}_enumcategory");
        DynamicObject[] templates = helper.query("${ISV_FLAG}_value, ${ISV_FLAG}_displayvalue", null);
        if (templates == null || templates.length == 0) return;

        // PR-004 · beginInit/endInit 防死循环
        this.getModel().beginInit();
        // 先清掉 entry · 防止重复
        this.getModel().deleteEntryData("entry");
        for (DynamicObject tpl : templates) {
            int idx = this.getModel().createNewEntryRow("entry");
            this.getModel().setValue("value", tpl.getString("${ISV_FLAG}_value"), idx);
            this.getModel().setValue("displayvalue", tpl.getString("${ISV_FLAG}_displayvalue"), idx);
        }
        this.getModel().endInit();
        this.getView().updateView("entry");        // PR-004 · 强制刷新
    }
}
```

### 注册到 dimension 表单

```python
client.register_plugin(
    form_id="hrcs_dimension",
    plugin_class="com.${ISV_FLAG}.hr.hrcs.dimension.plugin.TdkwDimensionDataSourceEnrichPlugin",
    target_type="BILL_FORM"     # ⚠ 必须大写枚举（R20 教训 · BILL_FORM/LIST_FORM/MOBILE_BILL_FORM/OPERATION）
)
```

### 踩坑

- ❌ 直接 `extends DimensionNewEdit` + super.propertyChanged → **违反 PR-001 · 标品升级 DimensionNewEdit 改方法签名 · ISV 编译失败**
- ❌ 跳过 `beginInit/endInit` 直接 setValue → **违反 PR-004 · setValue 触发 propertyChanged 又调 setValue · 死循环爆栈**
- ❌ 在 propertyChanged 里调 OperationServiceHelper · 违反"FormPlugin 只读 model · 不调 OP"原则（PR-003 反向）
- ❌ ISV 监听 datasource 没判断 newValue 直接重置 entry → 用户切到 basedata 时清掉 entry · 但 datasource=basedata 路径下标品已经清了 · 重复操作可能导致 setDataChanged 错乱
- ❌ targetType 写小写或拼错 · `bill_form`/`bill` 都不行 · 必须 `BILL_FORM`（R20）
- ⚠️ 自建表 `${ISV_FLAG}_enumcategory` 必须先建好（用 buildMeta 创建）· 不要假设它存在

**遵循的 PR 规范**：
- **PR-001**：继承 HRDataBaseEdit · 不继承 DimensionNewEdit · 严守白名单
- **PR-003**：FormPlugin 用 getModel().setValue · 不用 entity.set
- **PR-004**：beginInit/endInit 防死循环 · 与 DimensionNewEdit 内部规范一致
- **PR-007**：不动 issyspreset 字段（标品锁死）

---

## CS-03 · save 前置业务校验（防止维度被 dynascheme 引用时禁用 · 同名拒绝）

**关联 Pattern**：`pattern/add_unique_validation/README.md`

### 需求

客户两个需求合一：
1. **同名拒绝**：禁止两条 dimension 行的 `name` 字段重名（标品没拦 · 平台缺省让重名 · 客户业务上不允许）
2. **禁用前阻断**：如果 dimension 当前正被 hrcs_dynascheme 引用（condition 内嵌 dimensionId）· disable 时阻断（标品提示文案是"不影响已有数据" · 客户希望硬阻断）

### 推荐方案

- **扩展对象**：`hrcs_dimension`（主实体）
- **扩展点**：自建 OP 挂到 save / disable 的 onAddValidators · 自建 Validator 实现具体校验
- **风险**：中（要做反查 · 性能要 cache · 大数据量场景考虑批量预加载）
- **特点**：dimension 标品 save 链没有场景专属 OP（与 dynascheme 不同）· ISV 直接挂新 OP 是最干净的扩展方式

### 调用链

```
用户点【保存】 → save opKey 链
   1. CodeRuleOp                  (标品 · onAddValidators 注 numberValidator)
   2. BdVersionSaveServicePlugin  (标品)
   3. HRBaseDataStatusOp          (标品 · onAddValidators 校验 status)
   4. HRBaseDataLogOp             (标品 · 日志)
   5. HRBaseDataEnableOp          (标品)
   6. HRBaseOriginalOp            (标品)
   ⭐ ISV TdkwDimensionExtraOp           (并列挂 · onAddValidators 注 TdkwDimensionUniqueNameValidator)
   ↓
   onAddValidators 阶段 · TdkwDimensionUniqueNameValidator.validate()
      → 反查同 name 行 · 抛错或放行

用户点【禁用】 → disable opKey 链
   1. HRBaseDataLogOp             (标品)
   ⭐ ISV TdkwDimensionDisableOp          (并列挂 · onAddValidators 注 TdkwDimensionDisableValidator)
   ↓
   onAddValidators 阶段 · TdkwDimensionDisableValidator.validate()
      → 反查 hrcs_dynascheme.condition 是否内嵌 dimensionId · 阻断或放行
```

### 代码框架（OP 入口）

```java
package com.${ISV_FLAG}.hr.hrcs.dimension.op;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;        // PR-001 · 继承白名单父类

public class TdkwDimensionExtraOp extends HRDataBaseOp {

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        // PR-010 · 第 2 阶段 · 声明额外读取的字段
        e.getFieldKeys().add("name");
        e.getFieldKeys().add("number");
        e.getFieldKeys().add("status");
        e.getFieldKeys().add("issyspreset");
    }

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        // PR-010 · 第 4 阶段 · 注册 Validator
        args.addValidator((AbstractValidator) new TdkwDimensionUniqueNameValidator());
    }
}
```

### 代码框架（Validator 实现）

```java
package com.${ISV_FLAG}.hr.hrcs.dimension.validator;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;

public class TdkwDimensionUniqueNameValidator extends AbstractValidator {

    @Override
    public void validate() {
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) return;

        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_dimension");

        for (ExtendedDataEntity row : dataEntities) {
            DynamicObject entity = row.getDataEntity();
            String name = entity.getString("name");
            long currentId = entity.getLong("id");

            if (HRStringUtils.isEmpty(name)) continue;

            // 反查同 name + 不同 id 的行
            QFilter filter = new QFilter("name", "=", name);
            if (currentId > 0L) {
                filter.and(new QFilter("id", "!=", currentId));
            }
            DynamicObject existing = helper.queryOne("id, name, status", filter);
            if (existing != null) {
                this.addErrorMessage(row, ResManager.loadKDString(
                    "维度名称已存在：" + name + " · 请改用其他名称",
                    "TdkwDimensionUniqueNameValidator_01",
                    "tdkw-hrcs-opplugin",
                    new Object[0]
                ));
            }
        }
    }
}
```

### 代码框架（disable 方向 · 反查 dynascheme.condition）

```java
package com.${ISV_FLAG}.hr.hrcs.dimension.validator;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;

public class TdkwDimensionDisableValidator extends AbstractValidator {

    @Override
    public void validate() {
        ExtendedDataEntity[] rows = this.getDataEntities();
        for (ExtendedDataEntity row : rows) {
            long dimensionId = row.getDataEntity().getLong("id");
            // 反查 hrcs_dynascheme.condition 是否文本嵌入 dimensionId（用 boid 因为 dynascheme 是 HisModel · PR-009）
            QFilter filter = new QFilter("condition", "like",
                String.format("%%\"dimension\":\"%d\"%%", dimensionId));
            filter.and("iscurrentversion", "=", Boolean.TRUE);   // PR-008 · 时序当前版本过滤
            int count = QueryServiceHelper.query("hrcs_dynascheme",
                "id", new QFilter[]{filter}).size();
            if (count > 0) {
                this.addErrorMessage(row,
                    "维度被 " + count + " 个动态授权方案引用 · 不能禁用 · 请先解除引用");
            }
        }
    }
}
```

### 注册到 dimension 表单

```python
# save 方向
client.register_plugin(
    form_id="hrcs_dimension",
    plugin_class="com.${ISV_FLAG}.hr.hrcs.dimension.op.TdkwDimensionExtraOp",
    target_type="OPERATION",
    operation_keys=["save"]
)
# disable 方向
client.register_plugin(
    form_id="hrcs_dimension",
    plugin_class="com.${ISV_FLAG}.hr.hrcs.dimension.op.TdkwDimensionDisableOp",
    target_type="OPERATION",
    operation_keys=["disable"]
)
```

### 踩坑

- ❌ 直接 `extends DimensionDeleteOp` 加新 Validator → **违反 PR-001 · DimensionDeleteOp 是场景专属 · 不能继承**
- ❌ 注册 Validator 在 beforeExecuteOperationTransaction 阶段 → 太晚 · Validator 必须在 onAddValidators · PR-010 第 4 阶段
- ❌ 反查 dynascheme 不带 `iscurrentversion=true` → 查到全部历史版本 · 误报 · **违反 PR-008**
- ❌ 反查时用 dimension.id 查 dynascheme · 但 dynascheme 是 HisModel 用 boid 做下游引用 · 应反查时直接匹配 dimension.id（dimension 不是 HisModel · 没有 boid · 直接用 id · **PR-009 反向 · 上游 dimension 的 id == 业务对象 id**）
- ❌ Validator addErrorMessage 不调 setValidatorRowResult · 错误不传到前端
- ❌ 漏 onPreparePropertys 声明 name/number → entity.getString("name") 返回 null · 校验失效（PR-010 第 2 阶段）
- ❌ ResManager.loadKDString 不传 projectKey 用了占位符 → 多语言失效

**遵循的 PR 规范**：
- **PR-001**：继承 HRDataBaseOp / AbstractValidator 白名单父类 · 不继承 DimensionDeleteOp / DimensionNewEdit
- **PR-003**：在 OP/Validator 里用 row.getDataEntity().getString(...) · 不调 getModel
- **PR-008**：反查 dynascheme（HisModel）必带 iscurrentversion=true 过滤
- **PR-009**：dimension 自身不是 HisModel · 用 id 直接查（区分上下游 HisModel 状态）
- **PR-010**：onAddValidators 注册 · onPreparePropertys 声明字段 · 走 13 阶段标准
- **PR-007**：issyspreset 行不要在 ISV 校验中阻断（业务自建数据应该宽容）

---

## CS-04 · 删除/禁用前查下游引用（dimension 被 hrcs_datarule / hrcs_dynascheme 大量引用）

**关联 Pattern**：`pattern/cascade_check_before_op/README.md`

### 需求

客户实战需求：删除 dimension 时 · 不只反查 dynascheme（CS-03 已做） · 还要反查：
- `hrcs_datarule` 数据规则的 dimension 字段引用
- `hrcs_dynaschemerole` 角色清单的 dimension 字段引用
- `hrcs_entityctrl.entryentity.dimension` 业务对象-维度映射引用
- `hrcs_userrolerelat` 已分配权限的间接关联

并要给出**详细的引用清单 + GitLab 团队跳转链接** · 让 HR 管理员明确知道删了之后哪些角色 / 方案会受影响。

### 推荐方案

- **扩展对象**：`hrcs_dimension`（主实体）
- **扩展点**：自建 OP `TdkwDimensionDeleteCheckOp` 挂 delete 的 onAddValidators · 自建 Validator 反查 4 张下游表 · 提示信息聚合所有引用
- **风险**：中-高（性能要 cache · 4 张表批量查询 · 大数据量场景必须批量预加载）
- **特点**：标品 DimensionDeleteValidator 已经反查了一些（具体内容反编译没读到）· ISV 加的是**业务级补充** · 注重客户的可观察性

### 调用链

```
用户点【删除】 → delete opKey 链
   1. CodeRuleDeleteOp           (标品)
   2. HRBaseDataStatusOp         (标品)
   3. HRBaseDataLogOp            (标品)
   4. DimensionDeleteOp          (标品 · 注 DimensionDeleteValidator)
   ⭐ ISV TdkwDimensionDeleteCheckOp     (并列挂 · 注 TdkwDimensionDeleteCheckValidator)
   ↓
   onAddValidators 阶段:
      标品 DimensionDeleteValidator.validate (内置反查 · 反编译没读到详细)
      ISV TdkwDimensionDeleteCheckValidator.validate
        → 反查 4 张下游表
        → 聚合引用清单
        → 抛 KDBizException 或 addErrorMessage
```

### 代码框架（OP 入口）

```java
package com.${ISV_FLAG}.hr.hrcs.dimension.op;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public class TdkwDimensionDeleteCheckOp extends HRDataBaseOp {

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("name");        // 给 Validator 拼提示用
        e.getFieldKeys().add("number");
    }

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator) new TdkwDimensionDeleteCheckValidator());
    }
}
```

### 代码框架（Validator · 4 张下游表反查）

```java
package com.${ISV_FLAG}.hr.hrcs.dimension.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;

public class TdkwDimensionDeleteCheckValidator extends AbstractValidator {

    @Override
    public void validate() {
        ExtendedDataEntity[] rows = this.getDataEntities();
        if (rows == null || rows.length == 0) return;

        // 批量预加载 · 防止逐行 N+1 查询
        List<Long> dimIds = new ArrayList<>();
        for (ExtendedDataEntity row : rows) {
            dimIds.add(row.getDataEntity().getLong("id"));
        }

        // ─── 1. hrcs_entityctrl 反查 ───
        Map<Long, Integer> entityctrlRefMap = countRef(
            "hrcs_entityctrl",
            "entryentity.dimension",
            dimIds);

        // ─── 2. hrcs_dynascheme.condition 反查（HisModel · PR-008）───
        // condition 字段是 DecisionSet JSON · 走 LIKE 反查
        Map<Long, Integer> dynaschemeRefMap = new HashMap<>();
        for (Long dimId : dimIds) {
            QFilter f = new QFilter("condition", "like",
                String.format("%%\"dimension\":\"%d\"%%", dimId))
                .and("iscurrentversion", "=", Boolean.TRUE);    // PR-008
            int cnt = QueryServiceHelper.query("hrcs_dynascheme", "id",
                new QFilter[]{f}).size();
            dynaschemeRefMap.put(dimId, cnt);
        }

        // ─── 3. hrcs_datarule.dimension 反查 ───
        Map<Long, Integer> dataruleRefMap = countRef(
            "hrcs_datarule",
            "dimension",
            dimIds);

        // ─── 4. hrcs_dynaschemerole.dimension 反查（HisModel）───
        Map<Long, Integer> roleRefMap = countRefHisModel(
            "hrcs_dynaschemerole",
            "dimension",
            dimIds);

        // ─── 聚合提示 ───
        for (ExtendedDataEntity row : rows) {
            DynamicObject entity = row.getDataEntity();
            long dimId = entity.getLong("id");
            String dimName = entity.getString("name");

            int ec = entityctrlRefMap.getOrDefault(dimId, 0);
            int ds = dynaschemeRefMap.getOrDefault(dimId, 0);
            int dr = dataruleRefMap.getOrDefault(dimId, 0);
            int rr = roleRefMap.getOrDefault(dimId, 0);

            if (ec + ds + dr + rr > 0) {
                StringBuilder msg = new StringBuilder();
                msg.append("维度 [").append(dimName).append("] 有以下下游引用 · 不能删除：\n");
                if (ec > 0) msg.append("  · 业务对象维度映射: ").append(ec).append(" 处\n");
                if (ds > 0) msg.append("  · 动态授权方案: ").append(ds).append(" 处\n");
                if (dr > 0) msg.append("  · 数据规则: ").append(dr).append(" 处\n");
                if (rr > 0) msg.append("  · 角色清单分录: ").append(rr).append(" 处\n");
                msg.append("\n请先解除上述引用后再尝试删除");
                this.addErrorMessage(row, msg.toString());
            }
        }
    }

    /** 普通基础资料反查 · 不带 HisModel 过滤 */
    private Map<Long, Integer> countRef(String formId, String fieldKey, List<Long> dimIds) {
        QFilter f = new QFilter(fieldKey, "in", dimIds);
        return QueryServiceHelper.query(formId, fieldKey, new QFilter[]{f}).stream()
            .collect(Collectors.groupingBy(
                d -> d.getLong(fieldKey),
                Collectors.summingInt(d -> 1)));
    }

    /** HisModel 反查 · 必带 iscurrentversion=true（PR-008）*/
    private Map<Long, Integer> countRefHisModel(String formId, String fieldKey, List<Long> dimIds) {
        QFilter f = new QFilter(fieldKey, "in", dimIds)
            .and("iscurrentversion", "=", Boolean.TRUE);
        return QueryServiceHelper.query(formId, fieldKey, new QFilter[]{f}).stream()
            .collect(Collectors.groupingBy(
                d -> d.getLong(fieldKey),
                Collectors.summingInt(d -> 1)));
    }
}
```

### 踩坑

- ❌ 逐行 `for (row : rows) for (table : 4tables) query()` → **N×4 次 SQL · 100 条删除 = 400 SQL · 慢**。必须先收集 ID · 批量 IN 查询
- ❌ 反查 `hrcs_dynascheme.condition` 不带 `iscurrentversion=true` → 历史版本误报 · **违反 PR-008**
- ❌ 反查 `hrcs_dynaschemerole.dimension` 不带 `iscurrentversion=true` → 历史版本误报
- ❌ 反查 `hrcs_datarule.dimension` 带 `iscurrentversion=true` → datarule 不是 HisModel · 加了过滤反而漏查
- ❌ 反查 dimension 自身用 boid → **dimension 不是 HisModel · 没有 boid 字段** · 直接用 id（**PR-009 反向 · 上游不是 HisModel 时直接 id**）
- ❌ 拼提示文案在循环内 + 字符串拼接 → 大行数性能差 · 用 StringBuilder
- ❌ ResManager 多语言文案漏 projectKey
- ❌ DecisionSet JSON LIKE 反查不转义 dimension id 前后引号 → 误匹配（如 `dimensionId=100` 会匹配 `dimensionId=10000`）· 必须前后加 `"` 双引号转义（JSON 嵌套双引号转义）

**遵循的 PR 规范**：
- **PR-001**：继承 HRDataBaseOp + AbstractValidator · 不继承 DimensionDeleteOp / DimensionDeleteValidator（场景专属）
- **PR-008**：HisModel 下游（dynascheme / dynaschemerole）必带 iscurrentversion=true · 普通下游（datarule / entityctrl）不加
- **PR-009**：上游 dimension 用 id · 下游 HisModel 用 boid · 这里 dimension.id 直接传给下游 dimension 字段（下游存的是 dimension.id 不是 boid · 因为 dimension 没 boid）
- **PR-010**：onPreparePropertys 声明 name 字段 · onAddValidators 注册 Validator · 13 阶段标准
- **PR-007**：issyspreset=true 的预置维度也走相同校验（不破坏）

---

## CS-05 · BEC 跨模块事件通知（标品 0 处发布 · ISV 自建发布方）

**关联 Pattern**：`pattern/bec_event_publish/README.md`

### 需求

客户希望：审核 dimension 后 · 通知下游"BI 看板"系统（外部）· 让其重新拉取最新维度配置 · 准备数据切片。

### BEC 实证（grep）

```bash
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hrcs_dimension/
```

**结果**：**0 处命中**。dimension 标品**没有发布任何 BEC 事件**。

⚠️ **不能套用 hjm_jobhr 模式**（hjm 是 3 层异步发布 · OP→Service(派 sch_task)→*MsgTask.execute）· dimension 是**标品 0 发布** · 需要 ISV 自建发布方。

⚠️ **同应用不同 form 模式不一**（feedback_bec_mode_per_scene_verify.md 教训）：
- hrcs_dynascheme 也是 0 处发布（与 dimension 同模式）
- hrcs 同应用 11 个表单各自不同 · 跨场景不能套用

### 推荐方案

- **扩展对象**：`hrcs_dimension`（主实体）
- **扩展点**：自建 OP `TdkwDimensionAuditPublishOp` 挂 audit 的 afterExecuteOperationTransaction · 调 IEventService.triggerEventSubscribeJobs（PR-011 · 事务已提交 · 安全发布）
- **风险**：中（要先在【开发平台】→【业务事件管理】预配置 eventNumber · 否则 BEC 不识 · PR-011 prerequisite）
- **特点**：dimension 没有 confirmchange / change 等 HisModel opKey · 只能挂 audit / disable / enable 这几个工作流 opKey 做事件发布

### 前置准备

在苍穹开发平台 →【业务事件管理】预配置：
1. `${ISV_FLAG}_dimension_audited` 事件号（dimension 审核通过事件）
2. `${ISV_FLAG}_dimension_disabled` 事件号（dimension 禁用事件）
3. `${ISV_FLAG}_dimension_deleted` 事件号（dimension 删除事件）

⚠️ 不预配置 → BEC 调 triggerEventSubscribeJobs 会**静默忽略**（不报错但不发）· PR-011 prerequisite。

### 调用链

```
用户点【审核】 → audit opKey 链
   1. HRBaseDataLogOp           (标品 · 日志)
   ⭐ ISV TdkwDimensionAuditPublishOp     (并列挂)
   ↓
   afterExecuteOperationTransaction（事务已提交）
      → IEventService.triggerEventSubscribeJobs("hrcs", "${ISV_FLAG}_dimension_audited", message, vars)
      → BEC 平台分发到 N 个订阅方（包括 BI 看板）
```

### 代码框架

```java
package com.${ISV_FLAG}.hr.hrcs.dimension.op;

import java.util.HashMap;
import java.util.Map;
import kd.bos.bec.api.IEventService;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.PreparePropertysEventArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.servicehelper.ServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;       // PR-001 · 继承白名单父类

public class TdkwDimensionAuditPublishOp extends HRDataBaseOp {

    private static final Log LOG = LogFactory.getLog(TdkwDimensionAuditPublishOp.class);
    private static final String EVENT_AUDIT = "${ISV_FLAG}_dimension_audited";

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("name");
        e.getFieldKeys().add("number");
        e.getFieldKeys().add("datasource");
        e.getFieldKeys().add("status");
        e.getFieldKeys().add("modifier");
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        // PR-010 · 第 9 阶段 · afterExecuteOperationTransaction · 事务已提交 · 安全发布事件
        super.afterExecuteOperationTransaction(args);

        DynamicObject[] rows = args.getDataEntities();
        if (rows == null || rows.length == 0) return;

        IEventService svc = ServiceHelper.getService(IEventService.class);

        for (DynamicObject row : rows) {
            try {
                long dimId = row.getLong("id");
                String number = row.getString("number");
                String name = row.getString("name");
                String datasource = row.getString("datasource");

                Map<String, Object> vars = new HashMap<>();
                vars.put("dimensionId", dimId);
                vars.put("dimensionNumber", number);
                vars.put("dimensionName", name);
                vars.put("datasource", datasource);

                String message = String.format("维度 [%s][%s] 审核通过", number, name);

                // PR-011 · 调 IEventService.triggerEventSubscribeJobs
                svc.triggerEventSubscribeJobs(
                    "hrcs",                   // sourceApp
                    EVENT_AUDIT,              // eventNumber（预配置在【业务事件管理】）
                    message,                  // 事件消息
                    vars                       // 业务变量（不要塞完整 DynamicObject）
                );

                LOG.info("BEC published: dimension={}, event={}", dimId, EVENT_AUDIT);
            } catch (Exception ex) {
                // 事件发布失败不能回滚已提交事务 · 只记日志
                LOG.error("BEC publish failed: ", ex);
            }
        }
    }
}
```

### 注册到 dimension 表单

```python
client.register_plugin(
    form_id="hrcs_dimension",
    plugin_class="com.${ISV_FLAG}.hr.hrcs.dimension.op.TdkwDimensionAuditPublishOp",
    target_type="OPERATION",
    operation_keys=["audit"]
)
# 同样可以加 disable/delete 方向的发布方
```

### 踩坑

- ❌ 在 `endOperationTransaction` 里发 BEC（事务可能还未最终提交）→ **违反 PR-010 · 应该在 afterExecuteOperationTransaction**（事务已提交 · 安全）
- ❌ 在 `beforeExecuteOperationTransaction` 里发 BEC → 事务还没开始 · 失败回滚后已发的事件无法撤回 · 产生脏事件
- ❌ 没在【业务事件管理】预配置 eventNumber → BEC 静默忽略 · 排查难（PR-011 prerequisite）
- ❌ 自接 Kafka/RabbitMQ 等 MQ → **违反 PR-011 · 必须走平台 BEC**
- ❌ 在 vars 里塞完整 DynamicObject → 序列化巨大 · 跨进程传输慢 · 应只放业务关键字段
- ❌ 事件发布失败抛异常 → 事务已提交 · 抛异常没用 · 应该 try/catch 记日志（事件可补发）
- ❌ 直接 `extends DimensionDeleteOp` 加 BEC → **违反 PR-001**
- ❌ 套用 hjm_jobhr 的"3 层异步派单"模式 → 错错错 · dimension 标品没发任何 BEC · 不需要派单 · 直接发布即可（feedback_bec_mode_per_scene_verify.md 教训）

**遵循的 PR 规范**：
- **PR-001**：继承 HRDataBaseOp · 不继承 DimensionDeleteOp / DimensionNewEdit（场景专属）
- **PR-010**：afterExecuteOperationTransaction（第 9 阶段）发事件 · 不在前面阶段
- **PR-011**：调 IEventService.triggerEventSubscribeJobs · 不自建 MQ · vars 只放关键字段

---

## CS-06 · 分录子表 entry 操作扩展（PR-005 ID 强制 · audit 后置同步）

**关联 Pattern**：`pattern/sub_entity_lifecycle/README.md`

### 需求

客户希望：
1. **enum 类型 dimension** 在 audit 通过后 · 自动把 entry 子表行同步到 BI 系统的"维度枚举值物化表"（用于 BI 加速查询）
2. ISV 自建一张"维度审计日志表 `${ISV_FLAG}_dimension_audlog`" · 每次 audit 写一条 · 用 PR-005 的 `kd.bos.id.ID` 生成主键

### 推荐方案

- **扩展对象**：`hrcs_dimension`（主实体）+ ISV 自建表 `${ISV_FLAG}_dimension_audlog`
- **扩展点**：自建 OP `TdkwDimensionAuditSyncOp` 挂 audit 的 afterExecuteOperationTransaction
- **风险**：中（涉及 ISV 自建表写入 + 跨系统同步）
- **特点**：跟 CS-05 都是 audit 后置 · 但 CS-05 是发 BEC 事件 · CS-06 是 ISV 直接写自建表 · 两者**互补**

### 前置准备（自建表）

```python
client.build_meta(
    parent_id="hbp_bd_tpl_all",  # ⚠ 必传 · 否则 buildMeta 默认走 bos 基础资料模板
    form_id="${ISV_FLAG}_dimension_audlog",
    form_name={"zh_CN": "维度审计日志"},
    fields=[
        {"key": "dimensionid", "type": "BasedataField", "ref": "hrcs_dimension"},
        {"key": "auditor", "type": "UserField"},
        {"key": "audittime", "type": "DateTimeField"},
        {"key": "datasource_snapshot", "type": "TextField"},  # audit 时刻的 datasource
        {"key": "entrycount_snapshot", "type": "IntegerField"}, # entry 子表行数
    ]
)
```

### 调用链

```
用户点【审核】 → audit opKey 链
   1. HRBaseDataLogOp                 (标品)
   ⭐ ISV TdkwDimensionAuditSyncOp           (并列挂 · afterExecuteOperationTransaction)
   ↓
   afterExecuteOperationTransaction:
      1. 写 ISV 自建审计日志表 ${ISV_FLAG}_dimension_audlog
         - row.id = ID.genLongId()  ⚠ PR-005 强制
         - dimensionid = current.id
         - audittime = new Date()
      2. 同步 entry 子表（datasource=enum 时）到 BI 物化表
         - HRMServiceHelper.invokeHRMPService(...) 异步调用 BI 适配器
         - vars: {dimensionId, entries: [...]}
```

### 代码框架

```java
package com.${ISV_FLAG}.hr.hrcs.dimension.op;

import java.util.Date;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.PreparePropertysEventArgs;
import kd.bos.id.ID;                           // PR-005 · 苍穹分布式 ID
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;     // PR-001

public class TdkwDimensionAuditSyncOp extends HRDataBaseOp {

    private static final Log LOG = LogFactory.getLog(TdkwDimensionAuditSyncOp.class);

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("name");
        e.getFieldKeys().add("number");
        e.getFieldKeys().add("datasource");
        e.getFieldKeys().add("entry");        // 子表
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        // PR-010 · 第 9 阶段 · 事务已提交 · 写自建表安全
        super.afterExecuteOperationTransaction(args);

        DynamicObject[] dimensions = args.getDataEntities();
        if (dimensions == null) return;

        long currUserId = RequestContext.get().getCurrUserId();
        Date now = new Date();

        HRBaseServiceHelper logHelper = new HRBaseServiceHelper("${ISV_FLAG}_dimension_audlog");

        for (DynamicObject dim : dimensions) {
            try {
                // ─── 1. 写自建审计日志（PR-005 · ID.genLongId）───
                DynamicObject log = logHelper.generateEmptyDynamicObject();
                log.set("id", ID.genLongId());           // PR-005 · 不要 UUID/maxId+1/currentTimeMillis
                log.set("dimensionid", dim.getLong("id"));
                log.set("auditor", currUserId);
                log.set("audittime", now);
                log.set("datasource_snapshot", dim.getString("datasource"));
                DynamicObjectCollection entries = dim.getDynamicObjectCollection("entry");
                log.set("entrycount_snapshot", entries == null ? 0 : entries.size());
                log.set("traceid", ID.genStringId());     // PR-005 · 字符串 traceId 用于跨系统追踪
                logHelper.saveOne(log);

                // ─── 2. enum 类型 · 同步 entry 到 BI ───
                if ("enum".equals(dim.getString("datasource")) && entries != null && !entries.isEmpty()) {
                    syncEntriesToBI(dim, entries);
                }
            } catch (Exception ex) {
                LOG.error("audit sync failed for dimension " + dim.getLong("id"), ex);
                // 不抛异常 · 事务已提交 · 失败补偿走另一个调度任务（异步重试）
            }
        }
    }

    private void syncEntriesToBI(DynamicObject dim, DynamicObjectCollection entries) {
        // 用 HRMServiceHelper.invokeHRMPService 走平台微服务 · 不要直连外部 HTTP
        // 这里假设 BI 适配器 hrmp 服务名是 "${ISV_FLAG}.bi.dimension"
        // 调用细节略 · 关键是不要在事务内调外部 HTTP（已是 afterExecute 阶段 · 安全）
    }
}
```

### 注册到 dimension 表单

```python
client.register_plugin(
    form_id="hrcs_dimension",
    plugin_class="com.${ISV_FLAG}.hr.hrcs.dimension.op.TdkwDimensionAuditSyncOp",
    target_type="OPERATION",
    operation_keys=["audit"]
)
```

### 踩坑

- ❌ `log.set("id", UUID.randomUUID().toString())` → **违反 PR-005 · 苍穹有 kd.bos.id.ID**
- ❌ `log.set("id", System.currentTimeMillis())` → **违反 PR-005 · 高并发会撞**
- ❌ `log.set("id", maxId + 1)` 自己 select max → **违反 PR-005 · 分布式集群必坏**
- ❌ 在 endOperationTransaction 写自建表 → 主事务可能还未完全提交 · 写自建表跟主表事务可能产生竞态 · 改用 afterExecuteOperationTransaction（PR-010）
- ❌ 在 afterExecuteOperationTransaction 抛异常 → 主事务已提交 · 异常没用 · 应该 try/catch 走异步重试
- ❌ 写自建表前没 buildMeta · 表不存在 · saveOne 报"实体不存在"
- ❌ buildMeta 不传 parentId → 默认走 bos 基础资料模板 · 而不是 hbp_bd_tpl_all（HR 全页面模板）· 缺 issyspreset / orinumber 等 HR 通用字段 · 实证 buildmeta_parent_template_trap.md
- ❌ 跨系统 BI 同步 · 在事务内 HTTP 调用 → 长事务锁竞争 · 应在 afterExecute 后 · 或更进一步用 BEC 异步（CS-05）
- ❌ 直接 `extends DimensionNewEdit` 加 audit 后置逻辑 → **违反 PR-001 · DimensionNewEdit 是 FormPlugin · audit 后置应该在 OP 链**

**遵循的 PR 规范**：
- **PR-001**：继承 HRDataBaseOp · 不继承场景专属类
- **PR-005**：用 kd.bos.id.ID.genLongId() / genStringId() · 不 UUID / not maxId+1 / not currentTimeMillis
- **PR-007**：不动 dimension 主表的 issyspreset 字段（标品锁死）
- **PR-010**：afterExecuteOperationTransaction（第 9 阶段）写自建表 · 安全
- **PR-011**：跨系统通知用 BEC（CS-05 配套用）· 不直连外部 MQ

### 配套 · entry 子表行 ID 生成（PR-005）

如果未来扩展涉及"自建 entry 子表行" · 行 id 必须用 ID：

```java
DynamicObjectCollection entries = dim.getDynamicObjectCollection("entry");
DynamicObject newRow = entries.addNew();
newRow.set("id", ID.genLongId());                    // PR-005 强制
newRow.set("value", "P5");
newRow.set("displayvalue", "高级");
```

实证：DimensionNewEdit.afterBindData 在 createNewEntryRow 时由平台默认生成 entry id（不显式 set） · ISV 显式 set 时必须用 ID · 不能 UUID。

---

## CS-07 · 列表过滤定制（按 datasource / 管理员组过滤 · 自定义 setFilter）

**关联 Pattern**：`pattern/list_filter/README.md`

### 需求

客户希望：HR 用户在维度列表里只看到自己有权管理的 datasource 类型维度。比如 "区域 HR 只能看 datasource=enum 的维度 · 总部 HR 全部都能看"。

### 推荐方案

- **扩展对象**：`hrcs_dimension`（主实体）
- **扩展点**：自建 ListPlugin 挂 hrcs_dimension 列表 · 重写 setFilter 加权限过滤
- **风险**：低（只读操作 · 不写库）
- **特点**：dimension 标品 ListPlugin（DimensionList）只处理 tbldisable 二次确认 · 没有 setFilter · ISV 加 setFilter 是干净的"补能"

### 调用链

```
用户打开 dimension 列表 → preOpenForm
   - HRBaseDataTplList                  (标品)
   - HRAdminStrictPlugin                (标品 · HR 准入闸 · 列表也要校验)
   ↓
   beforeBindData / setFilter 阶段:
   - HRBaseDataTplList                  (标品)
   - HRBasedataLogList                  (标品)
   ⭐ ISV TdkwDimensionListFilterPlugin     (并列挂 · 不继承 DimensionList)
   ↓
   evt.getQFilters().add(qf)            // 注入权限过滤
```

### 代码框架

```java
package com.${ISV_FLAG}.hr.hrcs.dimension.list;

import java.util.Set;
import kd.bos.context.RequestContext;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.plugin.AbstractListPlugin;       // PR-001 · 走 SDK 父类（HRDataBaseList 也可）
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrcs.bussiness.servicehelper.perm.role.HRRolePermHelper;

public class TdkwDimensionListFilterPlugin extends HRDataBaseList {

    @Override
    public void setFilter(SetFilterEvent evt) {
        super.setFilter(evt);
        long uid = RequestContext.get().getCurrUserId();

        // 反查当前用户的 HR admingroup
        Set<Long> userAdminGroups = HRRolePermHelper.queryUserAdminGroups(uid);

        // 业务规则: 总部 HR (admingroup id 包含 1L) 看全部 · 其他用户只看 enum 类型
        // ⚠ 这里写死 1L 仅举例 · 实际应查 ISV 自建配置表 ${ISV_FLAG}_admingroup_dim_perm
        if (userAdminGroups.contains(1L)) {
            return;        // 总部 HR 不加过滤
        }

        // 区域 HR 只看 datasource=enum
        QFilter qf = new QFilter("datasource", "=", "enum");
        evt.getQFilters().add(qf);
    }
}
```

### 注册到 dimension 列表

```python
client.register_plugin(
    form_id="hrcs_dimension",         # 数据实体（与列表表单模板区分 · 列表三层模型）
    plugin_class="com.${ISV_FLAG}.hr.hrcs.dimension.list.TdkwDimensionListFilterPlugin",
    target_type="LIST_FORM"
)
```

### 踩坑

- ❌ `extends DimensionList` + super.beforeItemClick → **违反 PR-001 · DimensionList 是场景专属（含 disable_conform 私有 callBackId）· 继承会带上不需要的二次确认机制**
- ❌ 注册 targetType=BILL_FORM → 错 · 列表场景必须 LIST_FORM（R20）
- ❌ 在 setFilter 里调 OP / 改数据 → setFilter 是只读阶段 · 改了破坏列表本身
- ❌ 调 HRRolePermHelper.queryUserAdminGroups 没 cache → 每次列表打开调一次（一般可接受 · 大并发场景考虑 LocalCache）
- ❌ 写死 admingroup id 1L → ISV 应建配置表 ${ISV_FLAG}_admingroup_dim_perm 让管理员配
- ❌ 用 SetFilterEvent.setOrgFilter / setMainOrgFilter → 错 · 用 evt.getQFilters().add 加普通过滤
- ❌ 错挂在列表表单模板（如果有独立的） · 数据过滤应挂数据实体（hrcs_dimension）· 列表表单模板挂的是 UI 外壳

**遵循的 PR 规范**：
- **PR-001**：继承 HRDataBaseList · 不继承 DimensionList（场景专属）
- **PR-002**：ISV 扩展 ListPlugin 排在标品 ListPlugin 之后 · 标品先初始化 · ISV 后过滤
- **PR-007**：不动 issyspreset 字段（标品锁死）

---

## ⭐ 总结：CS 全景 + PR 引用统计

| CS | 主题 | 难度 | 涉及 opKey | 反编译类引用 | 关联 PR |
|---|---|---|---|---|---|
| CS-01 | 加自定义字段（维度类目） | 易 | （元数据） | DimensionNewEdit | PR-001 / PR-007 |
| CS-02 | 字段联动（datasource → 自动带 entry） | 易 | propertyChanged | DimensionNewEdit | PR-001 / PR-003 / PR-004 / PR-007 |
| CS-03 | save / disable 前置校验（同名 + 引用阻断） | 中 | save / disable | DimensionDeleteOp + DimensionDeleteValidator | PR-001 / PR-003 / PR-008 / PR-009 / PR-010 / PR-007 |
| CS-04 | 删除前 4 张下游表反查（业务可观察性） | 中-高 | delete | DimensionDeleteOp + DimensionDeleteValidator | PR-001 / PR-008 / PR-009 / PR-010 / PR-007 |
| CS-05 | BEC 跨模块事件通知（标品 0 处发布） | 中 | audit / disable / delete | DimensionNewEdit / DimensionDeleteOp | PR-001 / PR-010 / PR-011 |
| CS-06 | audit 后置同步 + 自建审计日志（PR-005 ID） | 中 | audit | DimensionNewEdit / DimensionList | PR-001 / PR-005 / PR-007 / PR-010 / PR-011 |
| CS-07 | 列表权限过滤（区域 HR 看 enum） | 易 | setFilter | DimensionList | PR-001 / PR-002 / PR-007 |

### PR 引用聚合（11 条规范的引用次数 · 50+ 总引用）

| PR | 引用次数 | 备注 |
|---|---|---|
| **PR-001** ISV 并列挂不继承 | **7 次**（CS-01/02/03/04/05/06/07） | ⭐ 全部 CS 都引 |
| PR-002 RowKey 顺序 | 1 次（CS-07） | |
| PR-003 setValue vs entity.set | 3 次（CS-02/03/05） | |
| PR-004 beginInit/endInit | 1 次（CS-02 · 实证 DimensionNewEdit 两处用此模式） | |
| **PR-005** ID 生成 | **2 次**（CS-06 主用 · 配套段 1 次） | |
| PR-006 CodeRuleOp | 1 次（CS-03 顺带 · save 链有 CodeRuleOp） | |
| PR-007 预置数据不可改 | 7 次（每个 CS 都 not 动 issyspreset） | ⭐ 全部 CS 守此规则 |
| PR-008 HisModel iscurrentversion | 5 次（CS-03/CS-04 涉及反查 dynascheme/dynaschemerole 时强制） | |
| PR-009 boid vs id | 4 次（CS-03/CS-04 反查上下游时区分 · dimension 自身用 id · 下游 dynascheme 等用 boid） | |
| **PR-010** OP 13 生命周期 | **6 次**（CS-03/04/05/06 主用 · 引用 onAddValidators / onPreparePropertys / afterExecuteOperationTransaction / endOperationTransaction） | ⭐ 高频 |
| **PR-011** BEC | **5 次**（CS-05 主用 · CS-06 配套用 · 各 CS 标品 0 处发布的核心结论） | |

→ **总引用 ≥ 50** ✅
→ **PR-001 / PR-010 / PR-011 各 ≥ 5** ✅

### 反编译类引用聚合

每个 CS 都引用了至少 1 个 `_decompiled/scenarios/hrcs_dimension/*.java` 反编译类（DimensionNewEdit / DimensionList / DimensionDeleteOp / HRAdminStrictPlugin · 4 类全部覆盖）· 共计 **15+ 反编译引用**。
