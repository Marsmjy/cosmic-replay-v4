# 推荐定制方案 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于 `knowledge/_shared/platform_rules.json` 11 条 PR + 14 反编译类实证
> **confidence**：real_deploy

所有方案遵循统一结构：**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

---

## CS-01 · 给 haos_structproject 扩展自定义字段（最高频）

**关联 PR**：PR-001（ISV 并列挂不继承）+ PR-007（预置数据 number 不可改）

### 需求

业务方说：方案除了根组织 / 类型这些标准字段 · 还要按"应用领域"做分类（销售域 / 制造域 / 共享服务域）· 用于派生 haos_structure 实例时根据领域自动带出权限模板。

### 推荐方案

- **扩展对象**：`haos_structproject` 主表
- **扩展点**：ISV 扩展元数据（**不能**直接 modifyMeta 改 haos_structproject · IsvSign 已签 · 必须建 ISV 扩展元数据继承 haos_structproject）
- **风险**：低（CS-06 详证 · ISV 扩展元数据只挂本 form · 物理列仍共用但 form 元数据隔离）
- **⚠ 关键**：物理表 `t_haos_structproject` 跟 `haos_structure` 共用 · 加字段会**同时影响 haos_structure 表单**（见 CS-06）· 不希望影响时走 CS-06 form 隔离

### 调用链（4 步）

```
Step 1: getDevInfo()                      // 拿 ISV 信息
Step 2: getBizApps()                      // 找 bizAppId（haos）
Step 3: createIsvExtMeta(
  parentFormId: "haos_structproject",     // 继承 haos_structproject 主表
  formNumber: "${ISV_FLAG}_haos_structproject_ext",  // ISV 扩展 form
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "${ISV_FLAG}_haos_structproject_ext",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_appdomain",
      name: {zh_CN: "应用领域", en_US: "App Domain"},
      mustInput: false,
      items: [
        {name: "销售域", value: "SALES"},
        {name: "制造域", value: "MFG"},
        {name: "共享服务域", value: "SSC"}
      ]
    }
  }]
)
Step 4: getFormSchema("${ISV_FLAG}_haos_structproject_ext")  // 二次验证落库
```

### 代码框架（cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "${ISV_FLAG}_haos_structproject_ext", "number": "${ISV_FLAG}_haos_structproject_ext"}
)

designer.add_field(
    field_type="ComboField",
    name="应用领域",
    key="${ISV_FLAG}_appdomain",
    parent_entity_id=designer.base_entity_id,
    items=[
        {"name": "销售域", "value": "SALES"},
        {"name": "制造域", "value": "MFG"},
        {"name": "共享服务域", "value": "SSC"}
    ]
)
designer.save()
```

### 踩坑

- ❌ **不要直接 modifyMeta haos_structproject** —— IsvSign 已签 · 标品禁止修改 · 必须建 ISV 扩展元数据继承
- ❌ **字段 key 不带 ISV 前缀**（如直接叫 `appdomain`）→ 标品升级被覆盖 · 应该用 ISV 前缀 (${ISV_FLAG}_)（ISV 简码）
- ⚠ **物理表共用陷阱**：扩展 haos_structproject 加的字段也会出现在物理表 `t_haos_structproject` · 因此 haos_structure 实例查询时也会带这一列。如果业务上**不希望** haos_structure 看到该字段 · 走 CS-06 的元数据隔离方案
- ⚠ **fieldName 列名超过 25 字符** → 苍穹平台数据库建表失败 · 推荐让平台默认按 `f + key.lowercase()` 生成（不传 fieldName）
- ❌ **引用组织用 `HRAdminOrgField` 类型**（OpenAPI 74 值枚举不支持）→ 改用 `BasedataField` + `refEntity=haos_adminorghrf7`（参考标品 `rootorg` 字段定义）
- 💡 多语言字段用 `MuliLangTextField` · 平台自动写到 `t_haos_structproject_l` 子表

### 关联 PR
- **PR-001**：ISV 扩展走并列挂插件 · 不继承 StructProjectEditPlugin（虽然 final · 不能继承）
- **PR-007**：预置数据 number 不可改 · 加字段不影响这条 · 但若加了"自定义编码"逻辑要按 issyspreset 排除预置行（`STRUCT_PROJECT_MANAGE`）

---

## CS-02 · "修改方案根组织类型时检查是否有派生 haos_structure 实例"校验

**关联 PR**：PR-001 + PR-010（OP 13 方法）+ PR-008（iscurrentversion 时序过滤 · 跨表查 haos_structure）

### 需求

业务方说：方案保存（modify + save）时 · 如果 `roottype` 字段值变更（实↔虚切换）· 必须检查"是否有派生 haos_structure 实例引用本方案"· 有引用就阻断保存（避免下挂组织树被改写引发故障）。

### 反编译实证 · 为什么必须 ISV 自补

- `StructProjectSaveOp.saveStructProjectAndRootOrg` L139-181：roottype 切换 + dbEffDate ≠ effDate 时 · 调 `OtherStructService.saveOtherStruct()` 整批迁移现有 OrgStruct（旧根 → 新根）· `setDeleteRoot(true)` 删除旧根
- 这意味着标品**不会校验下游引用** · 直接改写下挂组织树
- 如果该方案已被 haos_structure 实例引用（`relyonstructproject = 本方案 id`）· 实例的"下挂组织"会被改写 · 业务方观感是"我没动实例 · 怎么实例的下挂组织变了"

### 推荐方案

- **扩展点**：`onAddValidators@save` · 并列挂新 Validator（标品 StructProjectValidator 没此语义）
- **实现模式**：新 OP extends `HRDataBaseOp` · 重写 `onAddValidators` · 注册一个独立 `AbstractValidator`
- **风险**：中（独立校验 · 与标品 6 个 OP 并列跑 · 互不干扰；但要正确反查跨表）

### 扩展入口坐标

- **绑定表单**：`haos_structproject`
- **绑定操作**：`save`（同时 submit / submitandnew 也要挂 · 否则 submit 时绕过校验）
- **推荐父类**：`kd.hr.hbp.formplugin.web.HRDataBaseOp`（HR 通用 OP 抽象基类 · `@SdkPlugin` 白名单合规）
- **禁继承 `StructProjectSaveOp`**：final 类（StructProjectSaveOp.java L69 `public final class`）· 编译报错 · PR-001
- **禁继承 `StructProjectEditPlugin`**：final 类（L102 `public final class`）· 编译报错
- **禁继承 `StructProjectListPlugin`**：final 类（L76 `public final class`）· 编译报错
- **禁继承 `StructProjectDeleteOP`**、`StructProjectDisableOp`、`StructProjectEnableOp`、`StructOrgPermSaveOp`、`StructProjectBUListPlugin`：全 final · 编译报错
- **Validator 父类**：`kd.bos.entity.validate.AbstractValidator`（`@SdkPublic` · 必须独立继承 · 不继承场景 Validator）
- **关键重写方法**：
  - `onAddValidators(AddValidatorsEventArgs e)` — 注册自定义 Validator
  - 在 Validator 的 `validate()` 里读 dataEntity 的 roottype + 旧值（跨表查 db） → 比较 → 反查 haos_structure 是否引用 → 不允许就 addErrorMessage

**业务意图**：保存前 · 拿到本次保存的所有 entity · 对每个 entity 比较 db 里的 roottype 和 entity 的 roottype（如果用户改了）· 如果改了 · 反查 `haos_structure.relyonstructproject = entity.id`（跨表）· 找到引用就阻断。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_structproject`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 新增（并列挂 · 不覆盖标品 · PR-001）· `targetType = OPERATION`（R20 大写枚举）
4. 同时挂到 `submit / submitandnew` opKey（防绕过）
5. 保存 → 部署生效

### 代码框架（基于反编译 + SDK 白名单）

```java
package ${ISV_FLAG}.hrmp.haos.structproject.op;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * 方案保存校验：roottype 变更时检查派生实例
 * 与标品 7 个 OP（CodeRuleOp / BdVersionSaveServicePlugin / HRBaseDataStatusOp /
 * HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp / StructProjectSaveOp）并列跑 · PR-001
 * 标品 StructProjectSaveOp 是 final · 不能继承
 */
public class StructProjectRootTypeChangeOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new RootTypeChangeWhenDerivedValidator());
    }
}
```

```java
package ${ISV_FLAG}.hrmp.haos.structproject.validator;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 校验 roottype 变更时是否有派生 haos_structure 实例
 * AbstractValidator 是平台 @SdkPublic 父类 · 独立继承
 */
public class RootTypeChangeWhenDerivedValidator extends AbstractValidator {

    @Override
    public void validate() {
        ExtendedDataEntity[] entities = this.getDataEntities();
        if (entities == null || entities.length == 0) {
            return;
        }

        // 收集本次保存的方案 id
        java.util.Set<Long> projectIds = new java.util.HashSet<>();
        for (ExtendedDataEntity ext : entities) {
            DynamicObject entity = ext.getDataEntity();
            long id = entity.getLong("id");
            if (id != 0L) {
                projectIds.add(id);
            }
        }
        if (projectIds.isEmpty()) {
            return;  // 新建场景 · 无需校验下游引用
        }

        // 反查 db 里的 roottype 旧值
        HRBaseServiceHelper projectHelper = new HRBaseServiceHelper("haos_structproject");
        QFilter projectFilter = new QFilter("id", QCP.in, projectIds);
        DynamicObject[] dbProjects = projectHelper.query("id, roottype", projectFilter.toArray());
        java.util.Map<Long, String> dbRootTypeMap = new java.util.HashMap<>();
        for (DynamicObject dbProject : dbProjects) {
            dbRootTypeMap.put(dbProject.getLong("id"), dbProject.getString("roottype"));
        }

        // 反查 haos_structure 引用本方案的实例
        // 注：haos_structure 是 BaseFormModel 不是 HisModel · 这里不需要 iscurrentversion=true
        // 但如果业务方对 haos_structure 也做时序化扩展 · 需要带 iscurrentversion（PR-008）
        HRBaseServiceHelper structureHelper = new HRBaseServiceHelper("haos_structure");
        QFilter referFilter = new QFilter("relyonstructproject", QCP.in, projectIds)
                .and("enable", QCP.in, new String[]{"1", "0", "10"});  // 任何状态实例都阻断
        DynamicObject[] referencingStructures = structureHelper.query(
                "id, name, relyonstructproject.id", referFilter.toArray());

        // 把引用归位到 projectId
        java.util.Map<Long, java.util.List<String>> projectToInstanceMap = new java.util.HashMap<>();
        for (DynamicObject ref : referencingStructures) {
            long refProjectId = ref.getLong("relyonstructproject.id");
            projectToInstanceMap
                    .computeIfAbsent(refProjectId, k -> new java.util.ArrayList<>())
                    .add(ref.getString("name"));
        }

        // 校验
        for (ExtendedDataEntity ext : entities) {
            DynamicObject entity = ext.getDataEntity();
            long projectId = entity.getLong("id");
            String newRootType = entity.getString("roottype");
            String dbRootType = dbRootTypeMap.get(projectId);

            if (dbRootType == null || dbRootType.equals(newRootType)) {
                continue;  // 没改 roottype 跳过
            }

            java.util.List<String> instanceNames = projectToInstanceMap.get(projectId);
            if (instanceNames != null && !instanceNames.isEmpty()) {
                String displayInstances = String.join(", ",
                        instanceNames.size() > 5
                                ? instanceNames.subList(0, 5)
                                : instanceNames);
                if (instanceNames.size() > 5) {
                    displayInstances += String.format(" 等 %d 个", instanceNames.size());
                }
                this.addErrorMessage(ext, String.format(
                        "方案根组织类型已被以下 haos_structure 实例引用 · 修改会改写下挂组织树 · 请先下线相关实例：%s",
                        displayInstances));
            }
        }
    }
}
```

### 踩坑

- ❌ **禁继承 `StructProjectSaveOp`**（final · L69 反编译实证）· 也禁继承所有 StructProject* 类
- ❌ **不要写 `extends AbstractOperationServicePlugIn`** —— 用 `HRDataBaseOp` 拿到 HR 域基础能力
- ⚠ **必须同时挂 save + submit + submitandnew**（避免某条路径绕过校验）
- ⚠ **dbRootTypeMap 反查必须 IN 一批 · 不要循环单查** —— N+1 查询性能爆
- ⚠ **enable in [1, 0, 10]** —— 三态都阻断（包括启用中和已禁用）· 业务方真要"已禁用实例可以放行"再细化
- ❌ **不要 throw KDBizException** —— 用 `addErrorMessage(entity, msg)` · 可逐行定位错误
- ❌ **不要在 Validator 里写库** —— Validator 仅做校验 · 写库走 OP 的 beginOperationTransaction

### 关联 PR
- PR-001 · 并列挂插件
- PR-008 · 时序过滤（如果 haos_structure 被改造为 HisModel · 需要带 iscurrentversion）
- PR-010 · OP 13 方法顺序

---

## CS-03 · 字段联动 · 选了"应用领域"自动带出"权限模板"

**关联 PR**：PR-003（FormPlugin/OP 数据 API 分层）+ PR-004（setValue 死循环防护）

### 需求

业务方说：方案表单选了 `${ISV_FLAG}_appdomain`（应用领域 · CS-01 加的字段）后 · 应当自动把对应的"权限模板"基础资料带过来（如选 SALES → 权限模板自动 = "销售域默认权限"）。标品没字段联动 · ISV 加。

### 推荐方案

- **扩展点**：FormPlugin · `propertyChanged`（监听 `${ISV_FLAG}_appdomain` 字段变更）
- **实现模式**：新 FormPlugin extends `HRDataBaseEdit` · 重写 propertyChanged · 走 `getModel().beginInit/endInit` 防死循环
- **风险**：低

### 扩展入口坐标

- **绑定表单**：`haos_structproject`
- **推荐父类**：`kd.hr.hbp.formplugin.web.HRDataBaseEdit`（@SdkPlugin · 白名单可继承）
- **禁继承**：`StructProjectEditPlugin`（final · L102 反编译实证）· `StructProjectListPlugin`（list 类不适合 + final）
- **关键重写方法**：
  - `propertyChanged(PropertyChangedArgs e)` — 监听 `${ISV_FLAG}_appdomain` 字段
  - 用 `e.getProperty().getName()` 判断变更字段名
  - 用 `getModel().beginInit() ... endInit()` 包住 setValue 防死循环

**业务意图**：监听 ${ISV_FLAG}_appdomain 字段值变更 · 拿到新值 · 反查 ISV 自建权限模板基础资料（如 `${ISV_FLAG}_permtpl`）· 把 id 联动到 `${ISV_FLAG}_permtemplate` 字段。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_structproject`
2. 选择【注册插件】→ 新增（并列挂 · PR-001）· `targetType = BILL_FORM`（R20）
3. 保存 → 部署生效

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.structproject.form;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

/**
 * 方案表单字段联动：选 ${ISV_FLAG}_appdomain 自动带出 ${ISV_FLAG}_permtemplate
 * 跟标品 StructProjectEditPlugin（final 378 行）并列挂 · 不继承（PR-001）
 */
public class StructProjectAppDomainLinkagePlugin extends HRDataBaseEdit {

    private static final Log LOGGER = LogFactory.getLog(StructProjectAppDomainLinkagePlugin.class);

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);

        String name = e.getProperty().getName();
        if (!HRStringUtils.equals(name, "${ISV_FLAG}_appdomain")) {
            return;
        }

        Object newValue = e.getChangeSet()[0].getNewValue();
        String domain = newValue != null ? newValue.toString() : null;
        if (HRStringUtils.isEmpty(domain)) {
            // 清空时也联动清掉权限模板
            getModel().beginInit();
            try {
                getModel().setValue("${ISV_FLAG}_permtemplate", null);
            } finally {
                getModel().endInit();
            }
            getView().updateView("${ISV_FLAG}_permtemplate");
            return;
        }

        // 反查 ISV 自建权限模板（按域名匹配）· 用 HRBaseServiceHelper（PR-006 HR 域首选）
        HRBaseServiceHelper helper = new HRBaseServiceHelper("${ISV_FLAG}_permtpl");
        DynamicObject template = helper.queryOne(
                "id, number, name, domain",
                new QFilter("domain", QCP.equals, domain)
                        .and("enable", QCP.equals, "1").toArray());
        if (template == null) {
            LOGGER.warn("${ISV_FLAG}_permtpl with domain={} not found · skip linkage", domain);
            return;
        }

        // 联动写值 · 必须 beginInit/endInit 防死循环（PR-004）
        getModel().beginInit();
        try {
            getModel().setValue("${ISV_FLAG}_permtemplate", template.getLong("id"));
        } finally {
            getModel().endInit();
        }
        // 触发视图刷新 · 让用户看到联动后的值
        getView().updateView("${ISV_FLAG}_permtemplate");
    }
}
```

### 踩坑

- ❌ **不能直接 setValue 不包 beginInit/endInit** —— 标品 propertyChanged L250-289 没包是因为内部都是显隐切换 · ISV 加联动调用面更广 · 必须包（PR-004）
- ❌ **OP 阶段写值用 `getModel().setValue()`** —— OP 没 model · 必须 `entity.set(key, value)`（PR-003）· 本 CS 是 FormPlugin 用 setValue 是对的
- ⚠ **联动后必须 `getView().updateView(field)`** —— 否则 UI 不刷新（用户看不到联动结果）
- ⚠ **PropertyChangedArgs 取新值用 `e.getChangeSet()[0].getNewValue()`** —— 不是 `e.getProperty().getValue()`（getProperty 是元数据 · 不是数据）
- ❌ **不要监听 `creator/modifier/createtime` 等系统字段变更** —— 这些字段不应该用户改 · 监听浪费
- ⚠ **避免与标品 `roottype` / `isincludevirtualorg` 联动竞态** —— 标品 propertyChanged 在两个字段上都有显隐逻辑 · ISV 监听别的字段没冲突

### 关联 PR
- PR-003 · FormPlugin 用 getModel().setValue() · OP 用 entity.set()
- PR-004 · setValue 死循环防护

---

## CS-04 · "删除方案前必须无活跃 haos_structure 实例"校验（关键 · 高风险场景）

**关联 PR**：PR-001 + PR-010 + PR-008（如 haos_structure 走 HisModel 时）

### 需求

业务方说：方案删除（delete / delete_project opKey）时 · 标品 `StructProjectDeleteOP` 仅按 `enable=10` 过滤删除（L46-55）· 但**没**检查 `haos_structure.relyonstructproject` 是否引用本方案。如果方案被引用而被删 · 实例的 `relyonstructproject` 引用悬挂 → 业务故障。ISV 必须加反向引用校验。

### 反编译实证

`StructProjectDeleteOP.java` L46-55：
```java
public void beginOperationTransaction(BeginOperationTransactionArgs event) {
    DynamicObject[] dataEntities = event.getDataEntities();
    ArrayList needToDeleteData = Lists.newArrayListWithExpectedSize((int)16);
    for (DynamicObject dataEntity : dataEntities) {
        if (!"10".equals(dataEntity.getString("enable"))) continue;  // 仅 enable=10 才删
        needToDeleteData.add(dataEntity);
    }
    DynamicObject[] dyToDel = needToDeleteData.toArray(new DynamicObject[0]);
    this.structProjectApplication.doWithDelete(dyToDel);  // 直接删 · 没反向引用校验
}
```

### 推荐方案

- **扩展点**：`onAddValidators@delete` 和 `onAddValidators@delete_project` · 并列挂新 Validator
- **实现模式**：新 OP extends `HRDataBaseOp` · 重写 `onAddValidators` · 注册一个反向引用 Validator
- **风险**：中（独立校验 · 跨表查 haos_structure · 性能可控）

### 扩展入口坐标

- **绑定表单**：`haos_structproject`
- **绑定操作**：`delete` 和 `delete_project`（两个删除 opKey 都挂）
- **推荐父类**：`HRDataBaseOp`
- **禁继承**：`StructProjectDeleteOP`（final · L30 反编译实证）

**业务意图**：删除前 · 反查 `haos_structure.relyonstructproject IN (deleteIds)` · 任何状态的 haos_structure 实例引用都阻断（即使被禁用的实例也阻断 · 因为禁用是可逆的）。

**平台绑定方式**：
1. 苍穹开发平台 → `haos_structproject` → 操作 `delete` → 扩展插件 → 新增
2. 同样挂到 `delete_project` opKey
3. `targetType = OPERATION`
4. 保存部署

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.structproject.op;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * 方案删除前校验：检查 haos_structure 实例引用
 * 标品 StructProjectDeleteOP 是 final · 不能继承（PR-001）· 只能并列挂
 */
public class StructProjectDeleteRefCheckOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new StructProjectReferringInstanceValidator());
    }
}
```

```java
package ${ISV_FLAG}.hrmp.haos.structproject.validator;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 校验：方案删除前必须无活跃 haos_structure 实例引用
 * 反编译实证：StructProjectDeleteOP L46-55 仅按 enable=10 过滤 · 没反向引用校验
 */
public class StructProjectReferringInstanceValidator extends AbstractValidator {

    @Override
    public void validate() {
        ExtendedDataEntity[] entities = this.getDataEntities();
        if (entities == null || entities.length == 0) {
            return;
        }

        // 收集待删除方案 id
        java.util.Set<Long> deleteIds = new java.util.HashSet<>();
        java.util.Map<Long, ExtendedDataEntity> idToExt = new java.util.HashMap<>();
        for (ExtendedDataEntity ext : entities) {
            DynamicObject entity = ext.getDataEntity();
            long id = entity.getLong("id");
            if (id != 0L) {
                deleteIds.add(id);
                idToExt.put(id, ext);
            }
        }
        if (deleteIds.isEmpty()) {
            return;
        }

        // 反查 haos_structure.relyonstructproject IN (deleteIds)
        // 任何 enable 状态都阻断（禁用是可逆的 · 用户可以再启用）
        HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_structure");
        QFilter filter = new QFilter("relyonstructproject", QCP.in, deleteIds);
        DynamicObject[] referringInstances = helper.query(
                "id, name, number, enable, relyonstructproject.id",
                filter.toArray());

        if (referringInstances == null || referringInstances.length == 0) {
            return;  // 没引用 · 放行
        }

        // 归位到 projectId
        java.util.Map<Long, java.util.List<String>> projectToInstance = new java.util.HashMap<>();
        for (DynamicObject inst : referringInstances) {
            long projectId = inst.getLong("relyonstructproject.id");
            String name = inst.getString("name");
            String number = inst.getString("number");
            String enableStatus = inst.getString("enable");
            String display = String.format("%s(%s · enable=%s)", name, number, enableStatus);
            projectToInstance
                    .computeIfAbsent(projectId, k -> new java.util.ArrayList<>())
                    .add(display);
        }

        // 给每个被引用的方案加错误消息
        for (java.util.Map.Entry<Long, java.util.List<String>> entry : projectToInstance.entrySet()) {
            ExtendedDataEntity ext = idToExt.get(entry.getKey());
            if (ext == null) continue;
            java.util.List<String> instances = entry.getValue();
            String displayInstances = String.join("、",
                    instances.size() > 5 ? instances.subList(0, 5) : instances);
            if (instances.size() > 5) {
                displayInstances += String.format(" 等 %d 个", instances.size());
            }
            this.addErrorMessage(ext, String.format(
                    "方案被以下 haos_structure 实例引用 · 不允许删除：%s · 请先删除这些实例或解除引用关系",
                    displayInstances));
        }
    }
}
```

### 踩坑

- ⚠ **必须挂 `delete` 和 `delete_project` 两个 opKey** —— 列表删除走 `delete_project`（StructProjectListPlugin.openOperationPage L128）· 表单删除走 `delete` · 两个都要挂
- ⚠ **`enable in [0, 1, 10]`** 全状态阻断 —— 业务方真要"已禁用实例放行" 再细化（不推荐 · 禁用是可逆的）
- ❌ **不要尝试改写 StructProjectDeleteOP** —— 它是 final 不能继承 · 只能并列挂
- ❌ **Validator 不要 throw** —— 用 `addErrorMessage` · 让平台聚合错误
- ⚠ **跨表查性能** —— `haos_structure.relyonstructproject IN (?)` · 该列要有索引（标品已建）· 单次 IN < 100 个无问题
- ⚠ **如 haos_structure 被改造为 HisModel** —— 反查时加 `iscurrentversion=true`（PR-008）· 当前是 BaseFormModel 不需要

### 关联 PR
- PR-001 · 并列挂插件
- PR-010 · OP 13 方法顺序（onAddValidators 在 3）
- PR-008 · 跨表时序过滤（haos_structure 当前是 BaseFormModel · 不强制；改造后必须带）

---

## CS-05 · 方案变更 → 通知下游（标品**没**走标准 BEC · ISV 自建发布方）

**关联 PR**：PR-011（BEC 规范）+ PR-001 + PR-010

### 需求

业务方说：方案 save / disable / enable / delete 后 · 要通知下游系统（如组织视图重算服务 / 第三方系统 / ISV 自建的派生 haos_structure 维护服务）。

### ⚠ 重要前置 · 反编译实证（与 admin_org / hjm 对照）

> **grep 实证**：本场景 14 反编译类全文扫描 · `triggerEventSubscribe / IEventService / EventServiceHelper` 共 **0 处命中**。
>
> **本场景的派单机制**：标品**没**走标准 BEC（业务事件中心）· 而是用 admin_org 域共用的 `AdminChangeMsgService.handleChangeMsg` L113-123：
> - `JobClient.dispatch(jobInfo)` 派单到 `sch_task`
> - JOB_ID = `5+X/4Y=AOZ=O`（admin_org 域共用）
> - 派单内容写入 `haos_adminorg_msgdetail` 表
>
> 这跟标品 BEC 的差别：
> - 标品 BEC：走业务事件中心 + IEventService + 订阅方实现 IEventServicePlugin
> - 本场景 sch_task：走调度服务 + 异步消费 haos_adminorg_msgdetail 表
>
> **ISV 怎么办**：两条路径
> - 路径 A（推荐）· ISV 自建发布方 · 在 afterExecuteOperationTransaction 阶段调 `IEventService.triggerEventSubscribeJobs` 发标品 BEC · 然后做订阅方
> - 路径 B · ISV 直接监听 `haos_adminorg_msgdetail` 表（admin_org 域共用消息）· 但只能拿到根组织变更通知 · 不是方案级
>
> **不要套 hjm 模式**：hjm 标品在 OP 里直接调 `EventServiceHelper.triggerEventSubscribeJobs` · 本场景**没有这个调用**

### 推荐方案 · 路径 A（ISV 自建发布 + 订阅）

- **发布方扩展点**：`afterExecuteOperationTransaction@save / @disable / @enable / @delete` · 自建 OP · 在事务提交后调 `IEventService.triggerEventSubscribeJobs`
- **订阅方扩展点**：实现 `IEventServicePlugin` · 业务事件中心配订阅
- **风险**：中（需要业务方先在【业务事件中心】预配 eventNumber · 平台限制 PR-011）

### 扩展入口坐标 · 发布方

- **绑定表单**：`haos_structproject`
- **绑定操作**：`save / disable / enable / delete / delete_project`（按业务需要选）
- **推荐父类**：`HRDataBaseOp`
- **禁继承**：标品 StructProject* OP（全 final · PR-001）

**业务意图**：在主事务提交后 · 通过 BEC 发一条事件携带方案 id + opKey + changeScene · 订阅方按 boid 自查具体变更内容。Variables 不放 DynamicObject（PR-011 反模式）· 只放 id 和场景标记。

**平台绑定方式**（发布方）：
1. 苍穹开发平台 → 业务事件中心 → 事件定义 → 新建：
   - eventNumber: `${ISV_FLAG}_haos_structproject_changed`
   - 描述：ISV 自建·方案变更
2. `haos_structproject` → 操作 `save / disable / enable / delete / delete_project` → 扩展插件 → 新增
3. `targetType = OPERATION`

### 代码框架（发布方）

```java
package ${ISV_FLAG}.hrmp.haos.structproject.op;

import com.alibaba.fastjson.JSONObject;
import kd.bos.bec.api.IEventService;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.servicehelper.ServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * 方案变更后发 BEC 事件（ISV 自建发布方）
 * 标品没发标准 BEC（grep 14 类 0 命中 EventServiceHelper · 走 sch_task 派单）
 * 在 afterExecuteOperationTransaction 阶段发 · 主事务已提交（PR-010）
 */
public class StructProjectChangedPublishOp extends HRDataBaseOp {

    private static final Log LOGGER = LogFactory.getLog(StructProjectChangedPublishOp.class);
    private static final String EVENT_NUMBER = "${ISV_FLAG}_haos_structproject_changed";

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);

        DynamicObject[] entities = args.getDataEntities();
        if (entities == null || entities.length == 0) {
            return;
        }

        IEventService svc = ServiceHelper.getService(IEventService.class);
        String opKey = this.getOperateKey();  // save / disable / enable / delete / delete_project

        for (DynamicObject entity : entities) {
            JSONObject payload = new JSONObject();
            payload.put("id", entity.getLong("id"));
            payload.put("number", entity.getString("number"));
            payload.put("opKey", opKey);
            payload.put("changeScene", deriveChangeScene(opKey));
            payload.put("rootType", entity.getString("roottype"));
            payload.put("enable", entity.getString("enable"));

            try {
                // PR-011 · 用平台 BEC · 不自建 Kafka
                // variables 不放 DynamicObject · 只放轻量标识（订阅方按 id 自查）
                svc.triggerEventSubscribeJobs("haos", EVENT_NUMBER,
                        payload.toJSONString(), null);
            } catch (Exception e) {
                // 发事件失败不要回滚主事务（订阅方独立事务）
                LOGGER.error("Failed to publish haos_structproject changed event · id={} opKey={}",
                        entity.getLong("id"), opKey, e);
            }
        }
    }

    private String deriveChangeScene(String opKey) {
        switch (opKey) {
            case "save": return "MODIFY_OR_CREATE";
            case "delete":
            case "delete_project": return "DELETE";
            case "disable": return "DISABLE";
            case "enable": return "ENABLE";
            default: return opKey.toUpperCase();
        }
    }
}
```

### 代码框架（订阅方）

```java
package ${ISV_FLAG}.hrmp.haos.structproject.bec;

import com.alibaba.fastjson.JSONObject;
import kd.bos.bec.api.IEventServicePlugin;
import kd.bos.bec.model.KDBizEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

/**
 * 方案变更订阅方 · 通知下游
 * 实现 IEventServicePlugin（@SdkPublic · PR-011 推荐）
 */
public class StructProjectChangedNotifyConsumer implements IEventServicePlugin {

    private static final Log LOGGER = LogFactory.getLog(StructProjectChangedNotifyConsumer.class);

    @Override
    public Object handleEvent(KDBizEvent evt) {
        String src = evt.getSource();
        LOGGER.info("StructProjectChangedNotifyConsumer · eventId={} payload={}",
                evt.getEventId(), src);
        try {
            JSONObject payload = JSONObject.parseObject(src);
            long projectId = payload.getLongValue("id");
            String changeScene = payload.getString("changeScene");
            String rootType = payload.getString("rootType");
            // 按 id 自查具体变更内容（PR-011 · variables 不放 DynamicObject）
            // 例如：调组织视图重算服务 / 通知第三方系统 / 维护派生 haos_structure
            doNotify(projectId, changeScene, rootType);
            return null;
        } catch (Exception e) {
            LOGGER.error("StructProjectChangedNotifyConsumer failed · eventId={}",
                    evt.getEventId(), e);
            // 抛出会触发订阅方重试 · 自己做幂等
            throw e;
        }
    }

    private void doNotify(long projectId, String changeScene, String rootType) {
        // 业务通知逻辑
    }
}
```

### 踩坑

- ❌ **不要套 hjm `triggerEventSubscribeJobs` 模式假装"我看到了标品代码"** —— 本场景标品**没**这个调用 · grep 14 类 0 命中
- ⚠ **必须先在【业务事件中心】配 eventNumber** —— 否则 BEC 不识 · 调用静默失败（PR-011 prerequisite）
- ⚠ **发事件在 `afterExecuteOperationTransaction`** —— 主事务已提交（PR-010 · 不在 endOperationTransaction · 可能事务还没最终提交）
- ⚠ **订阅方做幂等** —— 用 `evt.getEventId()` 去重 · 重试机制会重入
- ❌ **不要在 variables 塞完整 DynamicObject** —— 订阅方按 id 自查 · 否则消息体爆（PR-011）
- ❌ **不要自建 Kafka/RabbitMQ** —— 用 BEC（PR-011）
- ⚠ **跟标品 sch_task 派单并行**：本 ISV 发布方走 BEC + 标品 sch_task 还在派 admin_org 消息 · 两套独立 · 订阅方用各自的事件号区分
- ❌ **不要直接监听 sch_task 表 / haos_adminorg_msgdetail 表** —— 那是平台内部表 · 业务方监听不稳定（升级会变）

### 关联 PR
- PR-001 · 并列挂
- PR-010 · OP 13 方法顺序（afterExecuteOperationTransaction 在 9）
- PR-011 · BEC 规范

---

## CS-06 · 共用物理表场景的扩展隔离（关键模式 · 必读）

**关联 PR**：PR-001 + PR-007

### 背景

`haos_structproject` 和 `haos_structure` 共用 `t_haos_structproject` 物理表（用 `roottype + otclassify` 等业务字段区分）。这跟 `hbpm_position_maintenance` 域的 `hbpm_positionhr` / `hbpm_stposition` 共用 `t_hbpm_position`（用 `isstandardpos` 区分）是同一种模式。

### 需求

业务方说：要给方案母本（haos_structproject）加自定义字段 `${ISV_FLAG}_appdomain`（应用领域 · CS-01 加的字段）· 但**不希望**这个字段污染 haos_structure 实例表单（实例不需要应用领域概念 · 因为实例引方案 · 域属性来自方案）。

### 推荐方案：两种思路

#### 思路 A · 加字段但 form 层隔离（推荐）

- **物理层**：字段加在 `t_haos_structproject` 物理表（共用 · 必然）
- **元数据层**：通过 ISV 扩展元数据**只挂在 haos_structproject** · 不挂 haos_structure
- **效果**：
  - haos_structproject 表单显示 `${ISV_FLAG}_appdomain` 字段
  - haos_structure 表单不显示该字段（虽然物理表有这一列 · 但 form 元数据没绑）
  - 物理列存在 · 但实例表单视图层"看不见"
- **风险**：低（这是苍穹标品支持的"form 共用表 · 字段独占"模式）

#### 思路 B · 加 EntryEntity 子分录而非字段

- **思路**：不在主表加字段 · 而是加一个 EntryEntity（如 `${ISV_FLAG}_appdomain_entry`）落到独立表 `t_tdkw_appdomain_entry`
- **效果**：
  - haos_structproject 主表元数据加一个 EntryEntity 引用
  - haos_structure 不引用这个分录 · 物理上完全隔离
- **风险**：中（需要业务方接受"应用领域是分录"而不是"字段" · 虽然语义可能不直观）
- **适用**：当字段存在多值（如多个应用领域标签）或业务不希望共享物理列时

### 扩展入口坐标 · 思路 A

- **扩展对象**：建 ISV 扩展元数据继承 `haos_structproject`（不继承 haos_structure）
- **扩展点**：modifyMeta add field 到 ISV 扩展 form
- **物理列名**：必须以 `f` 前缀 + ISV 简码 · 推荐 `ftdkw_appdomain`（避免列名冲突 25 字符限制）

### 调用链 · 思路 A

```python
# Step 1: 建 ISV 扩展 form 继承 haos_structproject（不继承 haos_structure）
client.create_isv_ext_meta(
    parent_form_id="haos_structproject",      # 仅继承母本
    form_number="${ISV_FLAG}_haos_structproject_ext"
)

# Step 2: 加字段
designer = client.open_existing_designer(
    target_form_info={"number": "${ISV_FLAG}_haos_structproject_ext"}
)
designer.add_field(
    field_type="ComboField",
    name="应用领域",
    key="${ISV_FLAG}_appdomain",
    parent_entity_id=designer.base_entity_id,
    items=[
        {"name": "销售域", "value": "SALES"},
        {"name": "制造域", "value": "MFG"},
        {"name": "共享服务域", "value": "SSC"}
    ]
)
designer.save()

# Step 3: 验证 · haos_structure 不会显示这个字段
schema_instance = client.get_form_schema("haos_structure")
assert "${ISV_FLAG}_appdomain" not in schema_instance  # ✅
schema_mother = client.get_form_schema("${ISV_FLAG}_haos_structproject_ext")
assert "${ISV_FLAG}_appdomain" in schema_mother  # ✅
```

### 踩坑

- ⚠ **物理列共享 · 元数据隔离** —— 加字段后物理表 `t_haos_structproject` 多了 `ftdkw_appdomain` 列 · haos_structure 实例数据该列为 NULL · 不影响实例业务
- ❌ **不要给 haos_structure 也加同名字段** —— 一旦双 form 都绑 · 物理列被两个 form 共享 · 业务规则会乱
- ⚠ **批量更新 / SQL 操作时小心 NULL** —— haos_structure 实例数据 `ftdkw_appdomain` 为 NULL · 写 SQL 报表要兜底 `IFNULL`
- ⚠ **如果业务方坚持"两个 form 都要这个字段且语义独立"** —— 走思路 B 用独立 EntryEntity 表 · 不要在共享物理列上凑合
- ❌ **不要直接 modifyMeta haos_structure 也加这个字段** —— 一旦绑两个 form · 物理列变共享 · 改一个会影响另一个
- 💡 **类比**：hbpm 的 isstandardpos · 标品就是用"hbpm_positionhr 不绑 isstandardpos UI 字段 · hbpm_stposition 才绑"实现 form 层隔离
- ⚠ **F7 选择跨 form**：如果 ISV 字段是 BasedataField 引用 haos_otclassify 等其它表 · 可以；如果是 BasedataField 引用 haos_structproject 自己（自引用类似 relyonstructproject）· 跟标品已有字段语义重 · 谨慎

### 关联 PR
- PR-001 · ISV 扩展机制
- PR-007 · 预置数据 number 不可改 · 加字段不影响这条

---

## CS-07 · 启用方案前置检查（reuse StructProjectEnableOp 并列挂）

**关联 PR**：PR-001 + PR-010

### 需求

业务方说：方案启用（enable opKey）前必须满足：
- 方案有效期（effdt）必须不晚于今天（不允许启用未来生效的方案）
- 方案的关联部门必须已建立（如果有 ISV 加的 ${ISV_FLAG}_relateddept 字段）

标品 `StructProjectEnableOp` 仅注册 StructProjectValidator + 空 beginOperationTransaction · 没业务前置校验 · ISV 加。

### 反编译实证

`StructProjectEnableOp.java` L21-37：
```java
public final class StructProjectEnableOp extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new StructProjectValidator());  // 共用 Validator
    }
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("org");  // 仅声明 org 字段
    }
    public void beginOperationTransaction(BeginOperationTransactionArgs event) {
        // 空方法
    }
}
```

`StructProjectValidator` 反编译没追到 · 推测含通用 enable/disable/save 校验 · 但**不含**"未来生效""关联部门已建"等业务前置。

### 推荐方案

- **扩展点**：`onAddValidators@enable` · 并列挂新 Validator
- **实现模式**：新 OP extends `HRDataBaseOp` · 仅重写 `onAddValidators` 注册 Validator
- **风险**：低

### 扩展入口坐标

- **绑定表单**：`haos_structproject`
- **绑定操作**：`enable`（仅启用时校验 · 保存时不卡 · 让业务可暂存）
- **推荐父类**：`HRDataBaseOp`
- **禁继承**：`StructProjectEnableOp`（final · L22 反编译实证）
- **Validator 父类**：`AbstractValidator`

**业务意图**：enable opKey 触发时 · 检查每条 entity 的 `effdt` 是否 ≤ 今天 + ISV 自建 ${ISV_FLAG}_relateddept 字段是否已填 · 不满足就阻断启用。

**平台绑定方式**：
1. 苍穹开发平台 → `haos_structproject` → 操作 `enable` → 扩展插件 → 新增
2. `targetType = OPERATION`
3. 保存部署

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.structproject.op;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * 方案启用前置校验
 * 标品 StructProjectEnableOp 是 final · 不能继承 · 并列挂（PR-001）
 */
public class StructProjectEnablePrecheckOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new StructProjectEnablePrerequisiteValidator());
    }
}
```

```java
package ${ISV_FLAG}.hrmp.haos.structproject.validator;

import java.util.Calendar;
import java.util.Date;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.dataentity.utils.StringUtils;

/**
 * 方案启用前置校验：
 * - effdt 必须不晚于今天
 * - ${ISV_FLAG}_relateddept（ISV 加的关联部门字段）必须已填
 */
public class StructProjectEnablePrerequisiteValidator extends AbstractValidator {

    @Override
    public void validate() {
        Date today = todayStartOfDay();

        for (ExtendedDataEntity ext : this.getDataEntities()) {
            DynamicObject entity = ext.getDataEntity();

            // 1. effdt 必须不晚于今天
            Date effdt = entity.getDate("effdt");
            if (effdt == null) {
                this.addErrorMessage(ext, "方案生效日期必填 · 不能启用");
                continue;
            }
            if (effdt.after(today)) {
                this.addErrorMessage(ext,
                        "方案生效日期晚于今天（" + effdt + "）· 请先调整 effdt 后再启用");
                continue;
            }

            // 2. ${ISV_FLAG}_relateddept 必须已填（如 ISV 自建该字段）
            DynamicObject relatedDept = entity.getDynamicObject("${ISV_FLAG}_relateddept");
            if (relatedDept == null) {
                this.addErrorMessage(ext, "启用方案前必须填写关联部门");
            }
        }
    }

    private Date todayStartOfDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}
```

### 踩坑

- ⚠ **挂在 enable opKey · 不挂 save** —— 设计选择：让业务可以保存草稿 · 启用时再强校验
- ⚠ **如果业务要求"提交时也强校验"** —— 同时挂 submit / submitandnew opKey
- ❌ **Validator 不要 throw KDBizException** —— 用 `addErrorMessage(entity, msg)` · 可逐行定位错误
- ⚠ **如果 enable 时已经禁用的方案重新启用** —— `effdt` 字段值早已存在 · 检查仍然有效（仍然是"必须不晚于今天"）
- ❌ **不要在 Validator 里写库** —— Validator 仅做校验 · 写库走 OP 的 `beginOperationTransaction`
- ⚠ **业务硬规则灵活性**：是否允许"启用一个 effdt 是未来日期的方案" · 业务方可能有不同期望 · 落地前确认

### 关联 PR
- PR-001 · 并列挂插件
- PR-010 · OP 13 方法顺序（onAddValidators 在 3）

---

## 方案选型矩阵

| 业务需求 | 推荐 CS | 扩展点 | 复杂度 |
|---|---|---|---|
| 加字段 | CS-01 | ISV 扩展元数据 | 低 |
| 修改 roottype 前校验下游 | CS-02 | onAddValidators@save + 跨表查 haos_structure | 中 |
| 字段联动（应用领域 → 权限模板）| CS-03 | propertyChanged + 反查 ISV 自建基础资料 | 中 |
| 删除前校验"无活跃实例"| CS-04 | onAddValidators@delete/delete_project + 跨表查 haos_structure | 中 |
| 变更通知下游 | CS-05 | afterExecuteOperationTransaction + BEC 自建发布方 | 高 |
| 共用物理表的扩展隔离 | CS-06 | ISV 扩展元数据只挂母本 | 中 |
| 启用前置校验 | CS-07 | onAddValidators@enable | 低 |

---

## 反模式清单（禁止）

- ❌ 直接 `modifyMeta` 修改 `haos_structproject` 主表（IsvSign 已签 · 必须走 ISV 扩展元数据）
- ❌ 继承 `StructProjectEditPlugin` / `StructProjectListPlugin` / `StructProjectSaveOp` / `StructProjectDeleteOP` / `StructProjectDisableOp` / `StructProjectEnableOp` / `StructOrgPermSaveOp` / `StructProjectBUListPlugin`（全 final · 编译报错）
- ❌ 套用 hjm 的 `EventServiceHelper.triggerEventSubscribeJobs` 模式假装"标品发了 BEC"（grep 14 类 0 命中）
- ❌ 把 `otclassify` 改成非 1010L 来"伪装母本是实例"（StructProjectSaveOp L103 硬编码 · `StructProjectRepository.createUserStructProjectFilter` 过滤掉看不见）
- ❌ 在 `t_haos_structproject` 物理表直接 SQL 改数据（绕过 BdVersion 历史一致性）
- ❌ 在 OP 里 `getModel().setValue()`（OP 没 model · NPE · PR-003）
- ❌ 在 propertyChanged 里 setValue 不包 beginInit/endInit（死循环 · PR-004）
- ❌ 删除方案时不检查 haos_structure 反向引用（CS-04 · 标品没校验 · ISV 必补）
- ❌ 修改 roottype 时不检查派生实例（CS-02 · 标品会改写下挂组织树 · ISV 必补）

---

## 跟 haos_structure 配套场景的协作引用

| CS | 跟 haos_structure 的协作 |
|---|---|
| CS-02 | 跨表查 haos_structure.relyonstructproject IN (deleteIds) · 反向引用校验 |
| CS-04 | 跨表查 haos_structure.relyonstructproject IN (deleteIds) · 删除前置反向引用 |
| CS-05 | BEC 订阅方按方案 id 自查 · 通知下游派生实例服务（haos_structure 实例 / 第三方系统）|
| CS-06 | 元数据层隔离 · ISV 字段只挂 haos_structproject · 不挂 haos_structure（共用物理表 · 但表单视图分离）|

---

**📌 来源追溯**：
- CS-01 ~ CS-07：本文 7 个 CS 全部基于 14 个反编译 java 类 + scene_doc.json 35 字段实证
- 标品 BEC 缺失结论：`grep -E "triggerEventSubscribe|IEventService|EventServiceHelper" knowledge/_sdk_audit/_decompiled/scenarios/haos_structproject/` 全部 0 命中
- 共用物理表模式：参考 `hbpm_position_maintenance/03_model_design.md` 的 `isstandardpos` 区分键分析 + 对照 `haos_structure_maintenance/06_customization_solutions.md` CS-06
- 反模式清单：来自 `_shared/platform_rules.json` PR-001 + admin_org/06 反模式 + 本场景反编译实证
- 跨表反查范围：haos_structure.relyonstructproject 是 BasedataField 引用 haos_structproject · 标准跨表查询 · 反编译没出现因为本场景 OP 不需要反查实例
