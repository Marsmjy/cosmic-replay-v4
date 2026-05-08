# 推荐定制方案 · 矩阵组织维护（haos_structure）

> **状态**：🟢 基于 `knowledge/_shared/platform_rules.json` 11 条 PR + 7 反编译类实证
> **confidence**：real_deploy

所有方案遵循统一结构：
**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

---

## CS-01 · 给 haos_structure 扩展自定义字段（最高频）

**关联 PR**：PR-001（ISV 并列挂不继承）+ PR-007（预置数据 number 不可改）

### 需求

业务方说：矩阵组织除了根组织 / 依赖方案这些标准字段 · 还要按"主管事业部"做分类标记。

### 推荐方案

- **扩展对象**：`haos_structure` 主表
- **扩展点**：ISV 扩展元数据（**不能**直接 modifyMeta 改 haos_structure · IsvSign 已签 · 必须建 ISV 扩展元数据继承 haos_structure）
- **风险**：低
- **⚠ 关键**：物理表 `t_haos_structproject` 跟 `haos_structproject` 共用 · 加字段会**同时影响母本表单**（详见 CS-06）

### 调用链（4 步）

```
Step 1: getDevInfo()                      // 拿 ISV 信息
Step 2: getBizApps()                      // 找 bizAppId
Step 3: createIsvExtMeta(
  parentFormId: "haos_structure",         // 继承 haos_structure 主表
  formNumber: "${ISV_FLAG}_haos_structure_ext",  // ISV 扩展 form
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "${ISV_FLAG}_haos_structure_ext",
    element: {
      fieldType: "BasedataField",
      key: "${ISV_FLAG}_managebu",
      name: {zh_CN: "主管事业部", en_US: "Managing BU"},
      refEntity: "bos_org",                // 引基础资料 BU
      mustInput: false
    }
  }]
)
Step 4: getFormSchema("${ISV_FLAG}_haos_structure_ext")  // 二次验证落库
```

### 代码框架（cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "${ISV_FLAG}_haos_structure_ext", "number": "${ISV_FLAG}_haos_structure_ext"}
)

designer.add_field(
    field_type="BasedataField",
    name="主管事业部",
    key="${ISV_FLAG}_managebu",
    parent_entity_id=designer.base_entity_id,
    refEntity="bos_org"
)
designer.save()
```

### 踩坑

- ❌ **不要直接 modifyMeta haos_structure** —— IsvSign 已签 · 标品禁止修改 · 必须建 ISV 扩展元数据继承
- ❌ **字段 key 不带 ISV 前缀**（如直接叫 `managebu`）→ 标品升级被覆盖 · 应该用 ISV 前缀 (${ISV_FLAG}_)（ISV 简码）
- ⚠ **物理表共用陷阱**：扩展 haos_structure 加的字段也会出现在物理表 `t_haos_structproject` 中 · 因此 haos_structproject 母本查询时也会带这一列。如果业务上**不希望**母本看到这个字段 · 走 CS-06 的 EntryEntity 隔离方案
- ⚠ **fieldName 列名超过 25 字符** → 苍穹平台数据库建表失败 · 推荐让平台默认按 `f + key.lowercase()` 生成（不传 fieldName）
- ❌ **引用组织用 `HRAdminOrgField` 类型**（OpenAPI 74 值枚举不支持）→ 改用 `BasedataField` + `refEntity=haos_adminorghrf7`（参考标品 `rootorg` 字段定义）
- 💡 多语言字段用 `MuliLangTextField` · 平台自动写到 `t_haos_structproject_l` 子表

### 关联 PR
- **PR-001**：ISV 扩展走并列挂插件 · 不继承 StructureEditPlugin（虽然它是 13 行薄壳 · 但 final 不能继承）
- **PR-007**：预置数据 number 不可改 · 加字段不影响这条 · 但若加了"自定义编码"逻辑要按 issyspreset 排除预置行

---

## CS-02 · "创建矩阵组织时根组织必须是公司类型"校验

**关联 PR**：PR-001 + PR-010（OP 13 方法）

### 需求

业务方说：根组织（rootorg）只能选"公司"类型的行政组织 · 不能选"部门"类型。标品没限制 · ISV 加保存前校验。

### 推荐方案

- **扩展点**：`onAddValidators@save` · 并列挂新 Validator（标品没相同语义的 Validator · ISV 独立加）
- **实现模式**：新 OP extends `HRDataBaseOp` · 重写 `onAddValidators` · 注册一个独立 `AbstractValidator`
- **风险**：低（独立校验 · 与标品 6 个 OP 并列跑 · 互不干扰）

### 扩展入口坐标

- **绑定表单**：`haos_structure`
- **绑定操作**：`save`（同时 submit / submitandnew 也要挂 · 否则 submit 时绕过校验）
- **推荐父类**：`kd.hr.hbp.formplugin.web.HRDataBaseOp`（HR 通用 OP 抽象基类 · `@SdkPlugin` 白名单合规）
- **禁继承**：`StructureListPlugin` / `StructureEditPlugin` / `StructProjectBUListPlugin`（全 final + 不在 SDK 注解白名单 · PR-001）
- **Validator 父类**：`kd.bos.entity.validate.AbstractValidator`（`@SdkPublic` · 必须独立继承 · 不继承场景 Validator）
- **关键重写方法**：
  - `onAddValidators(AddValidatorsEventArgs e)` — 注册自定义 Validator
  - 在 Validator 的 `validate()` 里读 rootorg → 反查 `haos_adminorg` 拿组织类型 → 不是公司就 `addErrorMessage`

**业务意图**：保存前 · 拿到本次保存的所有 entity · 对每个 entity 的 rootorg 字段反查 haos_adminorg 看 `orgtype` / `orgtypeid` 字段（具体字段名要去 admin_org `scene_doc.json` 查证 · 不脑补）· 不是公司类型就阻断保存。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_structure`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 新增（并列挂 · 不覆盖标品 · PR-001）· `targetType = OPERATION`（R20 大写枚举）
4. 同时挂到 `submit` opKey（防绕过）
5. 保存 → 部署生效

### 代码框架（来自反编译 + SDK 白名单）

```java
package ${ISV_FLAG}.hrmp.haos.structure.op;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.hbp.formplugin.web.HRDataBaseOp;
// 注：HRDataBaseOp 在 hrmp-hbp-formplugin · 标品白名单可继承

/**
 * 矩阵组织保存校验：根组织必须是公司类型
 * 与标品 6 个 OP（CodeRuleOp / BdVersionSaveServicePlugin / HRBaseDataStatusOp /
 * HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp）并列跑 · PR-001
 */
public class StructureRootOrgTypeOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new RootOrgIsCompanyValidator());
    }
}
```

```java
package ${ISV_FLAG}.hrmp.haos.structure.validator;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 校验 rootorg 必须是"公司"类型行政组织
 * AbstractValidator 是平台 @SdkPublic 父类 · 独立继承
 */
public class RootOrgIsCompanyValidator extends AbstractValidator {

    @Override
    public void validate() {
        ExtendedDataEntity[] entities = this.getDataEntities();
        if (entities == null || entities.length == 0) {
            return;
        }

        // 收集 rootorg id
        java.util.Set<Long> rootOrgIds = new java.util.HashSet<>();
        for (ExtendedDataEntity ext : entities) {
            DynamicObject entity = ext.getDataEntity();
            DynamicObject rootOrgDy = entity.getDynamicObject("rootorg");
            if (rootOrgDy != null) {
                rootOrgIds.add(rootOrgDy.getLong("id"));
            }
        }
        if (rootOrgIds.isEmpty()) {
            return;
        }

        // 查行政组织的类型字段（具体字段名以 admin_org scene_doc.json 实证为准）
        // 这里假设字段叫 orgtype.id；落地前去 admin_org 场景 scene_doc.json 查证
        HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorg");
        QFilter filter = new QFilter("id", QCP.in, rootOrgIds)
                .and("iscurrentversion", QCP.equals, Boolean.TRUE);  // PR-008
        DynamicObject[] orgs = helper.query("id, orgtype", filter.toArray());

        java.util.Map<Long, Long> orgTypeMap = new java.util.HashMap<>();
        for (DynamicObject org : orgs) {
            long id = org.getLong("id");
            DynamicObject orgType = org.getDynamicObject("orgtype");
            orgTypeMap.put(id, orgType != null ? orgType.getLong("id") : 0L);
        }

        // 公司类型 ID（业务硬编码 · 落地前业务方确认）
        // 推荐做法：放进系统参数 · 不写死
        long companyTypeId = 1010L;

        for (ExtendedDataEntity ext : entities) {
            DynamicObject entity = ext.getDataEntity();
            DynamicObject rootOrgDy = entity.getDynamicObject("rootorg");
            if (rootOrgDy == null) {
                continue;
            }
            long rootOrgId = rootOrgDy.getLong("id");
            Long type = orgTypeMap.get(rootOrgId);
            if (type == null || type != companyTypeId) {
                this.addErrorMessage(ext, "根组织必须为公司类型 · 当前选择不符合要求");
            }
        }
    }
}
```

### 踩坑

- ❌ **禁继承 `StructureListPlugin`**（final + 不在 SDK 白名单 · PR-001）· 也不要继承 `StructureEditPlugin`
- ❌ **不要写 `extends AbstractOperationServicePlugIn`** —— 用 `HRDataBaseOp` 拿到 HR 域基础能力
- ⚠ **必须同时挂 save + submit + submitandnew**（避免某条路径绕过校验）
- ⚠ **AdminOrg 是 HisModel** · 查时必须带 `iscurrentversion=true`（PR-008）· 否则查到历史版本误判
- ⚠ **公司类型 ID 不要硬编码 1010L** —— 放系统参数 · 不同租户的"公司"业务编码可能不同
- ❌ **不要在 beforeExecuteOperationTransaction 注册 Validator** —— 必须在 onAddValidators 阶段注册（PR-010 · 13 方法顺序）

### 关联 PR
- PR-001 · ISV 扩展走并列挂
- PR-008 · iscurrentversion 时序过滤
- PR-010 · OP 13 方法顺序

---

## CS-03 · 字段联动 · 选了"依赖架构方案"自动带出"根组织类型"

**关联 PR**：PR-003（FormPlugin/OP 数据 API 分层）+ PR-004（setValue 死循环防护）

### 需求

业务方说：用户在矩阵组织表单选了一个 `relyonstructproject` 母本后 · 应当自动把母本的"组织团队分类"（otclassify）带过来。标品 `StructureEditPlugin` 是 13 行薄壳 · 没字段联动 · ISV 加。

### 推荐方案

- **扩展点**：FormPlugin · `propertyChanged`（监听 relyonstructproject 字段变更）
- **实现模式**：新 FormPlugin extends `HRDataBaseEdit` · 重写 propertyChanged · 走 `getModel().beginInit/endInit` 防死循环
- **风险**：低

### 扩展入口坐标

- **绑定表单**：`haos_structure`
- **推荐父类**：`kd.hr.hbp.formplugin.web.HRDataBaseEdit`（@SdkPlugin · 白名单可继承）
- **禁继承**：`StructureEditPlugin`（final 类 · 即使薄壳也不能继承）· `StructureListPlugin`（list 类不适合）
- **关键重写方法**：
  - `propertyChanged(PropertyChangedArgs e)` — 监听 `relyonstructproject` 字段
  - 用 `e.getProperty().getName()` 判断变更字段名
  - 用 `getModel().beginInit() ... endInit()` 包住 setValue 防死循环

**业务意图**：监听 relyonstructproject 字段值变更 · 拿到新选中的 structproject id · 用 `StructProjectRepository.queryOneByStructProjectId` 反查（或直接 `HRBaseServiceHelper.queryOne`）· 把母本的 otclassify / roottype 字段值 setValue 到当前 entity 上。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_structure`
2. 选择【注册插件】→ 新增（并列挂 · PR-001）· `targetType = BILL_FORM`（R20）
3. 保存 → 部署生效

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.structure.form;

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
 * 矩阵组织表单字段联动：选 relyonstructproject 自动带出 otclassify / roottype
 * 跟标品 StructureEditPlugin（13 行 final 薄壳）并列挂 · 不继承（PR-001）
 */
public class StructurePropertyLinkagePlugin extends HRDataBaseEdit {

    private static final Log LOGGER = LogFactory.getLog(StructurePropertyLinkagePlugin.class);

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);

        String name = e.getProperty().getName();
        if (!HRStringUtils.equals(name, "relyonstructproject")) {
            return;
        }

        Object newValue = e.getChangeSet()[0].getNewValue();
        if (!(newValue instanceof DynamicObject)) {
            return;
        }
        DynamicObject newProject = (DynamicObject) newValue;
        long projectId = newProject.getLong("id");

        // 反查母本 · PR-006 用 HRBaseServiceHelper（HR 域首选 · 取代 BusinessDataServiceHelper）
        HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_structproject");
        DynamicObject mother = helper.queryOne(
                "id, otclassify, roottype",
                new QFilter("id", QCP.equals, projectId).toArray());
        if (mother == null) {
            LOGGER.warn("relyonstructproject id={} not found · skip linkage", projectId);
            return;
        }

        // 联动写值 · 必须 beginInit/endInit 防死循环（PR-004）
        getModel().beginInit();
        try {
            getModel().setValue("otclassify", mother.get("otclassify"));
            String motherRootType = mother.getString("roottype");
            if (HRStringUtils.isNotEmpty(motherRootType)) {
                getModel().setValue("roottype", motherRootType);
            }
        } finally {
            getModel().endInit();
        }
        // 触发视图刷新 · 让用户看到联动后的值
        getView().updateView("otclassify");
        getView().updateView("roottype");
    }
}
```

### 踩坑

- ❌ **不能直接 setValue 不包 beginInit/endInit** —— 会触发新一次 propertyChanged 死循环（PR-004）
- ❌ **OP 阶段写值用 `getModel().setValue()`** —— OP 没 model · 必须 `entity.set(key, value)`（PR-003）· 本 CS 是 FormPlugin 用 setValue 是对的
- ⚠ **联动后必须 `getView().updateView(field)`** —— 否则 UI 不刷新（用户看不到联动结果）
- ⚠ **从母本拿 otclassify** —— 母本 otclassify 通常 ≠ 1010L（母本不是矩阵实例）· 这里联动到实例可能业务上**不合适**（实例必须是 1010L）· 落地前业务方确认是不是该带出"母本 otclassify" · 还是"母本的某个其它字段"
- ⚠ **PropertyChangedArgs 取新值用 `e.getChangeSet()[0].getNewValue()`** —— 不是 `e.getProperty().getValue()`（getProperty 是元数据 · 不是数据）
- ❌ **不要监听 `creator/modifier/createtime` 等系统字段变更** —— 这些字段不应该用户改 · 监听浪费

### 关联 PR
- PR-003 · FormPlugin 用 getModel().setValue() · OP 用 entity.set()
- PR-004 · setValue 死循环防护

---

## CS-04 · "启用矩阵组织前必须有依赖方案"校验

**关联 PR**：PR-001 + PR-010

### 需求

业务方说：业务流程要求矩阵组织在启用前必须先选定 `relyonstructproject` 字段（标品没强制 · ISV 加 enable 前置校验）。

### 推荐方案

- **扩展点**：`onAddValidators@enable` · 并列挂新 Validator
- **实现模式**：新 OP extends `HRDataBaseOp` · 仅重写 `onAddValidators` 注册 Validator
- **风险**：低

### 扩展入口坐标

- **绑定表单**：`haos_structure`
- **绑定操作**：`enable`（仅启用时校验 · 保存时不卡 · 让业务可暂存）
- **推荐父类**：`HRDataBaseOp`（HR 域 OP 白名单首选）
- **Validator 父类**：`AbstractValidator`

**业务意图**：enable opKey 触发时 · 检查每条 entity 的 `relyonstructproject` 字段是否已填 · 没填就阻断启用 · 提示用户先选择依赖方案。

**平台绑定方式**：
1. 苍穹开发平台 → `haos_structure` → 操作 `enable` → 扩展插件 → 新增
2. `targetType = OPERATION`
3. 保存部署

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.structure.op;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.hbp.formplugin.web.HRDataBaseOp;

public class StructureEnableValidationOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new RelyOnStructProjectRequiredValidator());
    }
}
```

```java
package ${ISV_FLAG}.hrmp.haos.structure.validator;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;

/**
 * 启用矩阵组织前 · relyonstructproject 必填
 * 仅在 enable opKey 触发 · save 不强制（业务可暂存草稿）
 */
public class RelyOnStructProjectRequiredValidator extends AbstractValidator {
    @Override
    public void validate() {
        for (ExtendedDataEntity ext : this.getDataEntities()) {
            DynamicObject entity = ext.getDataEntity();
            DynamicObject relyOn = entity.getDynamicObject("relyonstructproject");
            if (relyOn == null) {
                this.addErrorMessage(ext, "启用矩阵组织前必须选择依赖架构方案");
            }
        }
    }
}
```

### 踩坑

- ⚠ **挂在 enable opKey · 不挂 save** —— 设计选择：让业务可以保存草稿 · 启用时再强校验
- ⚠ **如果业务要求"提交时也强校验"** —— 同时挂 submit / submitandnew opKey
- ❌ **Validator 不要 throw KDBizException** —— 用 `addErrorMessage(entity, msg)` · 可逐行定位错误（标品 HRBaseDataLogOp 等都是这种模式）
- ⚠ **如果 enable 时已经禁用的方案重新启用** —— `relyonstructproject` 字段值早已存在 · 这条校验过 · 没影响
- ❌ **不要在 Validator 里写库** —— Validator 仅做校验 · 写库走 OP 的 `beginOperationTransaction`

### 关联 PR
- PR-001 · 并列挂插件
- PR-010 · OP 13 方法顺序（onAddValidators 在 5 之前）

---

## CS-05 · 矩阵组织变更 → 通知下游（标品**没**发 BEC · 慎用）

**关联 PR**：PR-011（BEC 规范）+ PR-001

### 需求

业务方说：矩阵组织字段（rootorg / relyonstructproject）变更后 · 要通知组织视图重算服务 / 站内信 / 第三方系统。

### ⚠ 重要前置 · 反编译实证

> 在 `StructProjectRepository.java` 全文 167 行扫描中 · **没有发现** `EventServiceHelper.triggerEventSubscribeJobs` / `IEventService` 调用 · 也没有 `kd.bos.bec` 包的引用。
> 在 `StructureListPlugin.java / StructureEditPlugin.java` 也**没有发现** BEC 调用。
>
> 也就是说 · **本场景标品没有主动发 BEC 业务事件**。如果业务方真要"通知下游" · 有两个路径：
> - 路径 A（推荐）· ISV 自己在 `afterExecuteOperationTransaction` 阶段发 BEC（自建发布方）· 然后再做订阅方
> - 路径 B · 业务方接受"标品确实没事件" · 走轮询或定时任务比对差异通知（成本高 · 不推荐）

> 这跟 admin_org 不同：admin_org 的 `confirmchange` opKey 走 `BatchAdminOrgChangeParentOpService.afterTransDoOp` 标品**已发**事件（`AdminChangeMsgService.handleChangeMsg` · admin_org/06 CS-04 详证）。本场景**不要套**那个模式。

### 推荐方案 · 路径 A（ISV 自建发布 + 订阅）

- **发布方扩展点**：`afterExecuteOperationTransaction@save / @audit / @disable / @enable` · 自建 OP · 在事务提交后调 `IEventService.triggerEventSubscribeJobs`
- **订阅方扩展点**：实现 `IEventServicePlugin` · 业务事件中心配订阅
- **风险**：中（需要业务方先在【业务事件中心】预配 eventNumber · 平台限制 PR-011）

### 扩展入口坐标 · 发布方

- **绑定表单**：`haos_structure`
- **绑定操作**：`save / audit / disable / enable`（按业务需要选）
- **推荐父类**：`HRDataBaseOp`
- **禁继承**：标品 OP（HRBaseDataStatusOp / HRBaseDataLogOp 等不在白名单 · PR-001）

**业务意图**：在主事务提交后 · 通过 BEC 发一条事件携带矩阵组织 id + changeScene · 订阅方按 boid 自查具体变更内容。Variables 不放 DynamicObject（PR-011 反模式）· 只放 id 和场景标记。

**平台绑定方式**（发布方）：
1. 苍穹开发平台 → 业务事件中心 → 事件定义 → 新建：
   - eventNumber: `${ISV_FLAG}_haos_structure_changed`
   - 描述：ISV 自建·矩阵组织变更
2. `haos_structure` → 操作 `save / audit / disable / enable` → 扩展插件 → 新增
3. `targetType = OPERATION`

### 代码框架（发布方）

```java
package ${ISV_FLAG}.hrmp.haos.structure.op;

import com.alibaba.fastjson.JSONObject;
import kd.bos.bec.api.IEventService;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.servicehelper.ServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseOp;

/**
 * 矩阵组织变更后发 BEC 事件（ISV 自建发布方）
 * 标品没发 BEC（StructProjectRepository 反编译实证 · 167 行无 EventServiceHelper 调用）
 * 在 afterExecuteOperationTransaction 阶段发 · 主事务已提交（PR-010）
 */
public class StructureChangedPublishOp extends HRDataBaseOp {

    private static final Log LOGGER = LogFactory.getLog(StructureChangedPublishOp.class);
    private static final String EVENT_NUMBER = "${ISV_FLAG}_haos_structure_changed";

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);

        DynamicObject[] entities = args.getDataEntities();
        if (entities == null || entities.length == 0) {
            return;
        }

        IEventService svc = ServiceHelper.getService(IEventService.class);
        String opKey = this.getOperateKey();  // save / audit / disable / enable

        for (DynamicObject entity : entities) {
            JSONObject payload = new JSONObject();
            payload.put("id", entity.getLong("id"));
            payload.put("number", entity.getString("number"));
            payload.put("opKey", opKey);
            payload.put("changeScene", deriveChangeScene(opKey));

            try {
                // PR-011 · 用平台 BEC · 不自建 Kafka
                // variables 不放 DynamicObject · 只放轻量标识（订阅方按 id 自查）
                svc.triggerEventSubscribeJobs("haos", EVENT_NUMBER,
                        payload.toJSONString(), null);
            } catch (Exception e) {
                // 发事件失败不要回滚主事务（订阅方独立事务）
                LOGGER.error("Failed to publish haos_structure changed event · id={} opKey={}",
                        entity.getLong("id"), opKey, e);
            }
        }
    }

    private String deriveChangeScene(String opKey) {
        switch (opKey) {
            case "save": return "MODIFY";
            case "audit": return "AUDIT";
            case "unaudit": return "UNAUDIT";
            case "disable": return "DISABLE";
            case "enable": return "ENABLE";
            default: return opKey.toUpperCase();
        }
    }
}
```

### 代码框架（订阅方）

```java
package ${ISV_FLAG}.hrmp.haos.structure.bec;

import com.alibaba.fastjson.JSONObject;
import kd.bos.bec.api.IEventServicePlugin;
import kd.bos.bec.model.KDBizEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

/**
 * 矩阵组织变更订阅方 · 通知下游
 * 实现 IEventServicePlugin（@SdkPublic · PR-011 推荐）
 */
public class StructureChangedNotifyConsumer implements IEventServicePlugin {

    private static final Log LOGGER = LogFactory.getLog(StructureChangedNotifyConsumer.class);

    @Override
    public Object handleEvent(KDBizEvent evt) {
        String src = evt.getSource();
        LOGGER.info("StructureChangedNotifyConsumer · eventId={} payload={}",
                evt.getEventId(), src);
        try {
            JSONObject payload = JSONObject.parseObject(src);
            long structureId = payload.getLongValue("id");
            String changeScene = payload.getString("changeScene");
            // 按 id 自查具体变更内容（PR-011 · variables 不放 DynamicObject）
            // 例如：调组织视图重算服务 / 发站内信
            doNotify(structureId, changeScene);
            return null;
        } catch (Exception e) {
            LOGGER.error("StructureChangedNotifyConsumer failed · eventId={}",
                    evt.getEventId(), e);
            // 抛出会触发订阅方重试 · 自己做幂等
            throw e;
        }
    }

    private void doNotify(long structureId, String changeScene) {
        // 业务通知逻辑
    }
}
```

### 踩坑

- ❌ **不要套 admin_org CS-04 的"订阅标品事件"模式** —— 本场景标品没发事件 · 套了订阅不到任何东西
- ⚠ **必须先在【业务事件中心】配 eventNumber** —— 否则 BEC 不识 · 调用静默失败（PR-011 prerequisite）
- ⚠ **发事件在 `afterExecuteOperationTransaction`** —— 主事务已提交（PR-010 · 不在 endOperationTransaction · 可能事务还没最终提交）
- ⚠ **订阅方做幂等** —— 用 `evt.getEventId()` 去重 · 重试机制会重入
- ❌ **不要在 variables 塞完整 DynamicObject** —— 订阅方按 id 自查 · 否则消息体爆（PR-011）
- ❌ **不要自建 Kafka/RabbitMQ** —— 用 BEC（PR-011）
- ⚠ **如果业务方明确要"复用 admin_org 已发的事件"** —— 那看 admin_org CS-04 模式 · 但要确认 admin_org 的事件 payload 是否包含矩阵组织相关字段（实测它发的是 `haos_adminorg_msgdetail` 表数据 · 跟矩阵组织没直接关系 · 不能套用）

### 关联 PR
- PR-001 · 并列挂
- PR-010 · OP 13 方法顺序（afterExecuteOperationTransaction 在 9）
- PR-011 · BEC 规范

---

## CS-06 · 共用物理表场景的扩展隔离（关键模式 · 必读）

**关联 PR**：PR-001 + PR-007

### 背景

`haos_structure` 和 `haos_structproject` 共用 `t_haos_structproject` 物理表（用 `otclassify=1010L` 区分实例 vs 母本）。这跟 `hbpm_position_maintenance` 域的 `hbpm_positionhr` / `hbpm_stposition` 共用 `t_hbpm_position`（用 `isstandardpos` 区分）是同一种模式。

### 需求

业务方说：要给矩阵组织实例（haos_structure）加自定义字段 `${ISV_FLAG}_priority`（优先级）· 但**不希望**这个字段污染 haos_structproject 母本表单（业务上母本不需要优先级概念）。

### 推荐方案：两种思路

#### 思路 A · 加字段但 form 层隔离（推荐）

- **物理层**：字段加在 `t_haos_structproject` 物理表（共用 · 必然）
- **元数据层**：通过 ISV 扩展元数据**只挂在 haos_structure** · 不挂 haos_structproject
- **效果**：
  - haos_structure 表单显示 `${ISV_FLAG}_priority` 字段
  - haos_structproject 表单不显示该字段（虽然物理表有这一列 · 但 form 元数据没绑）
  - 物理列存在 · 但母本表单视图层"看不见"
- **风险**：低（这是苍穹标品支持的"form 共用表 · 字段独占"模式）

#### 思路 B · 加 EntryEntity 子分录而非字段

- **思路**：不在主表加字段 · 而是加一个 EntryEntity（如 `${ISV_FLAG}_priority_entry`）落到独立表 `t_tdkw_priority_entry`
- **效果**：
  - haos_structure 主表元数据加一个 EntryEntity 引用
  - haos_structproject 不引用这个分录 · 物理上完全隔离
- **风险**：中（需要业务方接受"优先级是分录"而不是"字段"）
- **适用**：当字段存在多值（如多个优先级标签）或业务不希望共享物理列时

### 扩展入口坐标 · 思路 A

- **扩展对象**：建 ISV 扩展元数据继承 `haos_structure`（不继承 haos_structproject）
- **扩展点**：modifyMeta add field 到 ISV 扩展 form
- **物理列名**：必须以 `f` 前缀 + ISV 简码 · 推荐 `ftdkw_priority`（避免列名冲突 25 字符限制）

### 调用链 · 思路 A

```python
# Step 1: 建 ISV 扩展 form 继承 haos_structure（不继承母本）
client.create_isv_ext_meta(
    parent_form_id="haos_structure",      # 仅继承实例 form
    form_number="${ISV_FLAG}_haos_structure_ext"
)

# Step 2: 加字段
designer = client.open_existing_designer(
    target_form_info={"number": "${ISV_FLAG}_haos_structure_ext"}
)
designer.add_field(
    field_type="ComboField",
    name="优先级",
    key="${ISV_FLAG}_priority",
    parent_entity_id=designer.base_entity_id,
    items=[
        {"name": "高", "value": "H"},
        {"name": "中", "value": "M"},
        {"name": "低", "value": "L"}
    ]
)
designer.save()

# Step 3: 验证 · haos_structproject 不会显示这个字段
schema_mother = client.get_form_schema("haos_structproject")
assert "${ISV_FLAG}_priority" not in schema_mother  # ✅
schema_instance = client.get_form_schema("${ISV_FLAG}_haos_structure_ext")
assert "${ISV_FLAG}_priority" in schema_instance    # ✅
```

### 踩坑

- ⚠ **物理列共享 · 元数据隔离** —— 加字段后物理表 `t_haos_structproject` 多了 `ftdkw_priority` 列 · 母本数据该列为 NULL · 不影响母本业务
- ❌ **不要给 haos_structproject 也加同名字段** —— 一旦双 form 都绑 · 物理列被两个 form 共享 · 业务规则会乱
- ⚠ **批量更新 / SQL 操作时小心 NULL** —— 母本数据 `ftdkw_priority` 为 NULL · 写 SQL 报表要兜底 `IFNULL`
- ⚠ **如果业务方坚持"两个 form 都要这个字段且语义独立"** —— 走思路 B 用独立 EntryEntity 表 · 不要在共享物理列上凑合
- ❌ **不要直接 modifyMeta haos_structproject 也加这个字段** —— 一旦绑两个 form · 物理列变共享 · 改一个会影响另一个
- 💡 **类比**：hbpm 的 isstandardpos · 标品就是用"hbpm_positionhr 不绑 isstandardpos UI 字段 · hbpm_stposition 才绑"实现 form 层隔离

### 关联 PR
- PR-001 · ISV 扩展机制
- PR-007 · 预置数据 number 不可改 · 加字段不影响这条

---

## CS-07 · 左树过滤定制 · 扩展可见组织范围

**关联 PR**：PR-001 + PR-009

### 需求

业务方说：当前左树（StructProjectBUListPlugin · 控制 `org` 字段）过滤逻辑是"用户管辖 BU"。我们要改成"用户管辖 BU + 子公司 BU"也可见。

### 推荐方案

- **扩展点**：新 ListPlugin extends `AbstractBUListPlugin`（在 `kd.hr.haos.formplugin.web.adminorg.template` 包 · 反编译可见 127 行 · public abstract 类 · **可继承**）
- **实现模式**：覆盖 `getCtrlBUFieldSet()` + 可选覆盖 `filterColumnSetFilter`
- **风险**：中（左树插件是 final · 不能继承标品 `StructProjectBUListPlugin` · 必须直接继承 abstract 父类 `AbstractBUListPlugin`）

### 扩展入口坐标

- **绑定表单**：`haos_structure`（list 视图）
- **推荐父类**：`kd.hr.haos.formplugin.web.adminorg.template.AbstractBUListPlugin`（abstract · 反编译实证 L41 是 public abstract · 在白名单内）
- **禁继承**：`StructProjectBUListPlugin`（final 类 · 反编译实证 L13）· `StructureListPlugin`（final）
- **关键重写方法**：
  - `getCtrlBUFieldSet()` — 必须实现（abstract · 返回受 BU 控制的字段集）
  - `filterColumnSetFilter(SetFilterEvent args)` — 可选覆盖 · 自定义过滤
  - `getPermOrgResult()` — 可选覆盖 · 自定义"管辖 BU"语义（包含子公司）

**业务意图**：父类 `AbstractBUListPlugin.getPermOrgResult()` 走的是 `OrgPermHelper.getHRPermOrg(billFormId)` · 该方法只返"直接管辖" BU。要扩展为含子公司 · 需自己 query 行政组织表 · 拿到 longnumber 前缀匹配的子组织。

**平台绑定方式**：
1. 在 main_form.xml 修改插件注册（替换 `StructProjectBUListPlugin` 为 ISV 新类）—— **注意：这是替换不是并列挂** · 因为左树是单插件机制
2. 或：仅替换其中一项 · 让 ISV 新插件继承 `AbstractBUListPlugin` 的同时复刻 `StructProjectBUListPlugin` 的 `getCtrlBUFieldSet` 行为

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.structure.list;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.permission.api.HasPermOrgResultImpl;
import kd.hr.haos.formplugin.web.adminorg.template.AbstractBUListPlugin;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 矩阵组织左树插件扩展：管辖 BU + 子公司 BU 都可见
 * 继承 AbstractBUListPlugin（abstract · 白名单可继承）
 * 替换标品 StructProjectBUListPlugin（final 不能继承）
 */
public class StructureBUListExtPlugin extends AbstractBUListPlugin {

    @Override
    public Set<String> getCtrlBUFieldSet() {
        // 复刻标品 StructProjectBUListPlugin 行为（反编译 L15-19）
        Set<String> ctrlBUFieldSet = new HashSet<>();
        ctrlBUFieldSet.add("org");
        return ctrlBUFieldSet;
    }

    /**
     * 扩展：管辖 BU + 子公司 BU
     * 覆盖父类的"权限结果只含直接管辖" 语义
     */
    @Override
    public HasPermOrgResult getPermOrgResult() {
        HasPermOrgResult origin = super.getPermOrgResult();
        if (origin.hasAllOrgPerm()) {
            // 全权限 · 无需扩展
            return origin;
        }
        List<Long> directPerm = origin.getHasPermOrgs();
        if (directPerm == null || directPerm.isEmpty()) {
            return origin;
        }

        // 查每个直接管辖 BU 的子组织（按 longnumber 前缀匹配）
        // 注：此处简化示意 · 真实场景应该按业务的"BU 树"结构走
        Set<Long> expanded = new HashSet<>(directPerm);
        HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_org");
        for (Long buId : directPerm) {
            DynamicObject bu = helper.queryOne("longnumber", buId);
            if (bu == null) {
                continue;
            }
            String prefix = bu.getString("longnumber");
            QFilter filter = new QFilter("longnumber", "like", prefix + "_%");
            DynamicObject[] children = helper.query("id", filter.toArray());
            for (DynamicObject child : children) {
                expanded.add(child.getLong("id"));
            }
        }

        // 重新构造 HasPermOrgResult
        HasPermOrgResultImpl result = new HasPermOrgResultImpl();
        result.setHasAllOrgPerm(false);
        result.setHasPermOrgs(new java.util.ArrayList<>(expanded));
        return result;
    }
}
```

### 踩坑

- ❌ **不要继承 `StructProjectBUListPlugin`** —— 反编译实证 L13 是 `public final class` · 编译报错
- ❌ **不要继承 `StructureListPlugin`** —— L90 是 `public final class` · 同上
- ⚠ **替换左树插件是侵入性改动** —— 测试要覆盖列表所有过滤场景（filterColumnSetFilter / filterContainerInit / filterContainerBeforeF7Select）
- ⚠ **PageCache `org_perm_result`**（`AbstractBUListPlugin.java:44` 常量）需要 invalidate · 否则用户切换权限后还看到旧数据
- ⚠ **bos_org 查询性能** —— 用 longnumber prefix like 大表慢 · 建议加缓存或限制递归层数
- ❌ **不要在 ListPlugin 里写 `getModel().setValue()`** —— ListPlugin 没 model（它有 BillList · 不是 IDataModel · PR-003）

### 关联 PR
- PR-001 · 左树是 final 不能继承 · 替换为继承 abstract 父类的 ISV 新类
- PR-009 · 行政组织 boid 维度（如果用 boid 走子组织树要带 iscurrentversion）

---

## CS-08 · 列表按"我创建 + 我审核过"过滤（CS-07 的另一种形态）

**关联 PR**：PR-001

### 需求

业务方说：标品列表（StructureListPlugin.setFilter）只显示"我创建"的方案 · 我们要扩展"我审核过的"也可见。

### 推荐方案

- **扩展点**：新 ListPlugin · 重写 `setFilter` · 与标品 StructureListPlugin **并列挂**（顺序累加 QFilter）
- **风险**：中

### 扩展入口坐标

- **绑定表单**：`haos_structure`
- **推荐父类**：`HRDataBaseList`（HR 域 · 白名单首选）
- **禁继承**：`StructureListPlugin`（final）
- **关键重写**：`setFilter(SetFilterEvent e)` — 不调 super（标品的 setFilter 已经在它自己挂的位置跑过）· 仅 `e.addCustomQFilter` 追加"我审核过"条件

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.structure.list;

import kd.bos.context.RequestContext;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 列表过滤扩展：在标品"我创建"基础上 · 加上"我审核过"
 * 与标品 StructureListPlugin 并列挂（PR-001 · 不继承）
 * 平台按注册顺序累加 QFilter（PR-002）
 */
public class StructureMyAuditListExtPlugin extends HRDataBaseList {
    @Override
    public void setFilter(SetFilterEvent event) {
        super.setFilter(event);
        long currUserId = RequestContext.get().getCurrUserId();

        // 注：审核人字段是否叫 auditor / reviewerid · 落地前从 admin_org 或本表
        // _shared/_standard_metadata 反查实际字段名 · 不脑补
        QFilter myAuditFilter = new QFilter("auditor.id", "=", currUserId);

        // OR 关系叠加：标品的"我创建"已经在它自己 setFilter 加进去了
        // 这里追加一个 OR 条件 · 让"我审核过"也可见
        // ⚠ 这种 OR 方式假设 event.getQFilters() 有标品的 creator filter
        // 要检查标品过滤是否已经合并 · 否则可能产生意外结果
        event.getQFilters().add(myAuditFilter);
    }
}
```

### 踩坑

- ⚠ **标品 setFilter 用 add 而不是 set** —— 我们 add 也是 add · 累加是 AND 关系（不是 OR）· 如果业务想要 "我创建 OR 我审核" · 需要把标品的"我创建" filter 重构成"creator OR auditor" · 但这违反 PR-001（修改标品逻辑）
- ⚠ **真实做法**：评估一下业务是不是真要 OR · 还是只是"在我创建的基础上再筛我审核的"（AND）· 如果是 AND · 上面代码就够；如果是 OR · 走思路 B 替换标品 StructureListPlugin（高风险 · 不推荐）
- ⚠ **`auditor` 字段名要查证** —— scene_doc.json 31 个字段中没看到 auditor · 推测可能不是直接字段 · 而是从操作日志（HRBaseDataLogOp 写的 t_*_log 表）反查 · 落地前真发验证
- ❌ **不要 `event.setQFilters(new ArrayList<>())`** —— 清空标品过滤会丢权限校验

### 关联 PR
- PR-001 · 并列挂
- PR-002 · RowKey 顺序

---

## 方案选型矩阵

| 业务需求 | 推荐 CS | 扩展点 | 复杂度 |
|---|---|---|---|
| 加字段 | CS-01 | ISV 扩展元数据 | 低 |
| 保存校验（如根组织类型）| CS-02 | onAddValidators@save | 低 |
| 字段联动（选方案带出类型）| CS-03 | propertyChanged | 中 |
| 启用前置校验 | CS-04 | onAddValidators@enable | 低 |
| 变更通知下游 | CS-05 | afterExecuteOperationTransaction + BEC | 中 |
| 共用物理表的字段隔离 | CS-06 | ISV 扩展元数据（不挂母本）| 中 |
| 左树扩展 | CS-07 | 替换 StructProjectBUListPlugin | 中 |
| 列表过滤扩展 | CS-08 | setFilter 并列挂 | 中 |

---

## 反模式清单（禁止）

- ❌ 直接 `modifyMeta` 修改 `haos_structure` 主表（IsvSign 已签 · 必须走 ISV 扩展元数据）
- ❌ 继承 `StructureListPlugin` / `StructureEditPlugin` / `StructProjectBUListPlugin`（全 final · 编译报错）
- ❌ 套用 `admin_org CS-04` 的 BEC 订阅模式（标品**没**发事件 · 套了订阅不到）
- ❌ 把 `otclassify` 改成非 1010L（数据会被 `StructProjectRepository.createUserStructProjectFilter` 过滤掉）
- ❌ 在 `t_haos_structproject` 物理表直接 SQL 改数据（绕过 BdVersion 历史一致性）
- ❌ 在 OP 里 `getModel().setValue()`（OP 没 model · NPE · PR-003）
- ❌ 在 propertyChanged 里 setValue 不包 beginInit/endInit（死循环 · PR-004）
- ❌ 删除矩阵组织时直接物理删除（标品要求先 disable · 不要绕过）

---

**📌 来源追溯**：
- CS-01 ~ CS-08：本文 8 个 CS 全部基于 7 个反编译 java 类 + scene_doc.json 31 字段实证
- 标品 BEC 缺失结论：来自 `StructProjectRepository.java` 167 行 + `StructureListPlugin.java` 215 行 + `StructureEditPlugin.java` 13 行 全文扫描无 `EventServiceHelper.triggerEventSubscribeJobs` / `IEventService` 调用
- 共用物理表模式：参考 hbpm_position_maintenance 03 model_design 的 `isstandardpos` 区分键分析
- 反模式清单：来自 `_shared/platform_rules.json` PR-001 + admin_org/06 反模式 + 本场景反编译实证
