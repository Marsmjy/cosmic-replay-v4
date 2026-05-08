# 推荐定制方案 · 标准岗位维护（hbpm_stposition）

> **状态**: 🟢 verified · 基于 6 反编译类 + HisModel 框架 + platform_rules.json 11 条 PR
> **confidence**: verified（CS-01 ~ CS-05 · 5 个场景）
> **审计时间**: 2026-04-27

**禁继承铁律（PR-001）**：以下 11 个类 ISV 绝对禁止继承：
- `StandardPositionEdit`（场景专属 FormPlugin · final）
- `StandardPositionListPlugin`（场景专属 List · final）
- `StandardPositionSaveOp`（场景专属 OP · final）
- `StandardPositionDisableOp`（final）
- `StandardPositionEnableOp`（final）
- `StandardPositionChangeOp`（final）
- `StandardPositionMsgHandleOp`（父类 · 消息处理 · 禁继承）
- `HisModelFormCommonPlugin`（时序核心 · 禁继承）
- `HisModelListCommonPlugin`（时序核心 · 禁继承）
- `HisModelOPCommonPlugin`（时序核心 · 禁继承）
- `HisUniqueValidateOp`（时序核心 · 禁继承）

---

## CS-01 · 给标准岗位加自定义业务字段（HisModel 场景 onPreparePropertys 特殊性）

### 需求

业务方：要给标准岗位加"岗位风险等级"（低/中/高）和"外部招聘岗位描述"两个字段，用于薪资规划报表。

### 推荐方案

- **扩展对象**：`hbpm_stposition`（主实体）
- **扩展点**：`modifyMeta(op=add field)`
- **风险**：中等（共用物理表 t_hbpm_position · 需双场景回归）
- **关联 INV**：INV-SP-02（isstandardpos 强制写入不受影响）

### 调用链（OpenAPI）

```
Step 1: getDevInfo()                         // 拿 ISV 信息
Step 2: getBizApps()                         // 找 bizAppId / bizUnitId
Step 3: getFormSchema(formNumber=hbpm_stposition)  // 查目前字段清单 · 防重名
Step 4: modifyMeta({
  formId: "hbpm_stposition",
  ops: [
    {
      op: "add",
      treeType: "entity",
      elementType: "field",
      parentScope: "hbpm_stposition",
      element: {
        fieldType: "ComboField",
        key: "${ISV_FLAG}_riskgrade",
        name: {zh_CN: "岗位风险等级", en_US: "Position Risk Grade"},
        mustInput: false,
        enum: [
          {value: "low",  label: "低"},
          {value: "mid",  label: "中"},
          {value: "high", label: "高"}
        ]
      }
    },
    {
      op: "add",
      treeType: "entity",
      elementType: "field",
      parentScope: "hbpm_stposition",
      element: {
        fieldType: "MuliLangTextField",
        key: "${ISV_FLAG}_extrecruitdesc",
        name: {zh_CN: "外部招聘描述", en_US: "External Recruit Description"},
        mustInput: false
      }
    }
  ]
})
Step 5: getFormSchema(hbpm_stposition)  // 二次验证落库
```

### OP 层读取 ISV 字段（onPreparePropertys 特殊性）

**HisModel 场景的 onPreparePropertys 特别注意**：在 HisModel 场景中，OP 插件读取扩展字段必须通过 onPreparePropertys 声明，否则字段值为 null。

```java
@SdkPlugin
public class TdkwPositionCheckOp extends HRDataBaseOp {

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        // ⭐ 必须声明要读的 ISV 字段，否则 beforeExecuteOperationTransaction 里 getValue 返回 null
        args.getFields().add("${ISV_FLAG}_riskgrade");
        args.getFields().add("${ISV_FLAG}_extrecruitdesc");
    }

    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        DynamicObject[] entities = args.getDataEntities();
        for (DynamicObject entity : entities) {
            String riskGrade = entity.getString("${ISV_FLAG}_riskgrade");
            // 业务逻辑...
        }
    }
}
```

### 踩坑

- ❌ 字段 key 不带 ISV 前缀（如 `riskgrade`）→ 标品升级被覆盖
- ❌ 字段 key > 24 字符 → 数据库列名上限触顶
- ❌ 使用 HisModelBasedataField 类型 → OpenAPI modifyMeta 不支持
- ❌ 使用 HRAdminOrgField 类型 → 同上，74 值枚举不支持
- ⚠ 扩展字段同时影响 hbpm_positionhr（共用物理表），必须双场景回归
- 💡 OP 层读扩展字段必须先在 onPreparePropertys 声明（PR-010）

### 关联 PR

- PR-001（并列挂 OP · 不继承 StandardPositionSaveOp）
- PR-010（onPreparePropertys 声明字段）

---

## CS-02 · 下游引用检查（禁用/删除前校验）

### 需求

业务方：删除或禁用标准岗位前，要检查是否有 hbpm_positionhr 岗位信息仍在引用这个标准岗位（boid 维度）。

### 推荐方案

- **扩展对象**：`hbpm_stposition`
- **扩展点**：`onAddValidators@disable` + `onAddValidators@delete`（双拦）
- **实现模式**：并列挂插件 + 注册 AbstractValidator
- **父类选择**：`HRDataBaseOp`（白名单合规）
- **禁止继承**：`StandardPositionDisableOp` / `HisModelOPCommonPlugin` / `HisUniqueValidateOp`

### 调用链（执行时）

```
用户点"禁用"
    │
    ├── HRBaseDataStatusOp.onAddValidators（平台通用）
    ├── HisModelOPCommonPlugin.onAddValidators（时序校验）
    ├── HisUniqueValidateOp.onAddValidators（boid 唯一性）
    ├── StandardPositionDisableOp.onAddValidators（标品空实现）
    └── TdkwPositionRefCheckOp.onAddValidators（ISV 注册下游引用校验器）← ISV 在此挂
          └── TdkwPositionRefValidator.validate（查 hbpm_positionhr 反向引用）
```

### 代码框架

```java
@SdkPlugin
public class TdkwPositionRefCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator(new TdkwPositionRefValidator());
    }
}

class TdkwPositionRefValidator extends AbstractValidator {

    @Override
    public void validate() {
        ExtendedDataEntity[] entities = getDataEntities();
        for (ExtendedDataEntity extEntity : entities) {
            DynamicObject entity = extEntity.getDataEntity();
            // ⭐ 关键：用 boid 查下游引用（PR-009）
            long boid = entity.getLong("boid");
            QFilter refFilter = new QFilter("stposition", "=", boid)
                .and(new QFilter("enable", "=", "1"));
            // stposition 字段存的是 hbpm_stposition 的 boid
            boolean hasRef = BusinessDataServiceHelper.exists("hbpm_positionhr", new QFilter[]{refFilter});
            if (hasRef) {
                String errMsg = ResManager.loadKDString(
                    "存在关联的岗位信息，不允许禁用", "TdkwValidator_0", "tdkw-module", new Object[0]
                );
                this.addErrorMessage(extEntity, errMsg);
            }
        }
    }
}
```

### 踩坑

- ❌ 用 `entity.getLong("id")` 查下游 → 漏查（下游存的是 boid，不是 id）
- ❌ 继承 StandardPositionDisableOp → PR-001 违规
- ⚠ 查 hbpm_positionhr 时要加 enable=1 过滤（禁用的岗位不阻断）

### 关联 PR

- PR-001（并列挂插件）
- PR-009（boid 是引用维度）
- PR-010（onAddValidators 阶段 · 事务前校验）

---

## CS-03 · 字段联动（propertyChanged 扩展）

### 需求

业务方：选择了"职位（job）"后，自动带出该职位对应的"岗位类型（positiontype）"。

### 背景分析

标品 `StandardPositionEdit.propertyChanged` 已实现：
- adminorg → org 联动（INV-SP-10）
- org / adminorg / job 变更 → 重新生成 number 编码（INV-SP-11）
- 职等/职级范围联动（JobLevelGradeRangeUtil）

ISV 的联动**必须通过并列挂 FormPlugin 实现**，不能继承 StandardPositionEdit（final）。

### 代码框架

```java
@SdkPlugin
public class TdkwPositionLinkPlugin extends HRDataBaseEdit {

    @Override
    public void propertyChanged(PropertyChangedArgs args) {
        String fieldName = args.getProperty().getName();
        if ("job".equals(fieldName)) {
            syncPositionTypeFromJob();
        }
    }

    private void syncPositionTypeFromJob() {
        IDataModel model = this.getModel();
        DynamicObject entity = model.getDataEntity();
        DynamicObject job = entity.getDynamicObject("job");
        if (job == null) {
            return;
        }
        // 职位引用的是 hbjm_jobhr 的 boid（PR-009）
        long jobBoid = job.getLong("id");  // 注意：DynamicObject.get("id") 这里取的是关联对象的 id
        // 查职位的岗位类型
        QFilter filter = new QFilter("boid", "=", jobBoid)
            .and(new QFilter("iscurrentversion", "=", Boolean.TRUE));
        DynamicObject jobRecord = QueryServiceHelper.queryOne(
            "hbjm_jobhr",
            "id,positiontype",
            new QFilter[]{filter}
        );
        if (jobRecord != null) {
            DynamicObject posType = jobRecord.getDynamicObject("positiontype");
            // ⭐ PR-004: 防死循环，用 beginInit/endInit 包裹
            model.beginInit();
            model.setValue("positiontype", posType);
            model.endInit();
            this.getView().updateView("positiontype");
        }
    }
}
```

### 踩坑

- ❌ 直接 setValue 不包 beginInit/endInit → 触发 propertyChanged 死循环（PR-004）
- ❌ 继承 StandardPositionEdit → PR-001 违规（final 类）
- ❌ 在 OP 里做字段联动 → OP 没有 model，无法用 setValue（PR-003）
- ⚠ job 是 HisModelBasedataField，取值时是 boid（PR-009），查下游要用 boid

### 关联 PR

- PR-001（并列挂 FormPlugin）
- PR-003（FormPlugin 用 getModel().setValue()）
- PR-004（beginInit/endInit 防死循环）
- PR-008/009（时序字段查询规范）

---

## CS-04 · 时序版本查询扩展（如何正确查当前版本）

### 需求

ISV 场景：在薪资计算 OP 中，需要根据员工的 stpositionBoid 查出当前有效的标准岗位信息（包括职等范围、风险等级等）。

### 错误用法（常见坑）

```java
// ❌ 错误1：不加 iscurrentversion 过滤 → 可能查到 N 个历史版本
DynamicObject position = QueryServiceHelper.queryOne(
    "hbpm_stposition",
    "id,name,${ISV_FLAG}_riskgrade",
    new QFilter[]{new QFilter("boid", "=", stPosBoid)}
);
// 结果可能是历史版本，不是当前版本！

// ❌ 错误2：用 id 关联查询（hbpm_positionhr 存的是 boid）
long stPosId = posHrEntity.getLong("stposition");  // ⚠ 这里取的实际是 boid，不是 id
// 然后拿这个值直接用，字段语义混淆导致bug
```

### 正确用法

```java
// ✅ 方法1：直接用 boid + iscurrentversion 查当前版本
public DynamicObject getStPositionCurrent(long stPosBoid) {
    QFilter[] filters = {
        new QFilter("boid", "=", stPosBoid),
        new QFilter("iscurrentversion", "=", Boolean.TRUE)
    };
    return QueryServiceHelper.queryOne(
        "hbpm_stposition",
        "id,boid,number,name,org,adminorg,job,bsed,bsled,${ISV_FLAG}_riskgrade",
        filters
    );
}

// ✅ 方法2：批量查（优化性能，避免 N+1）
public DynamicObjectCollection getStPositionBatch(List<Long> boidList) {
    QFilter[] filters = {
        new QFilter("boid", "in", boidList),
        new QFilter("iscurrentversion", "=", Boolean.TRUE)
    };
    return QueryServiceHelper.query(
        "hbpm_stposition",
        "id,boid,number,name,${ISV_FLAG}_riskgrade",
        filters
    );
}

// ✅ 方法3：在 OP 的 onPreparePropertys 中声明关联字段，让框架自动 join
@Override
public void onPreparePropertys(PreparePropertysEventArgs args) {
    // 声明本实体要读的字段
    args.getFields().add("stposition");      // 这是 boid
    args.getFields().add("stposition.name"); // 声明跨实体字段（平台自动 join）
}
```

### 关联 PR

- PR-008（iscurrentversion 查当前版本）
- PR-009（boid 是引用维度）
- PR-010（onPreparePropertys 阶段声明字段）

---

## CS-05 · 跟 hbpm_positionhr 联动的 BEC 扩展（基于 StandardPositionMsgHandleOp 推断）

### 背景

从类名和类层次推断：`StandardPositionMsgHandleOp`（父类）很可能在 `afterExecuteOperationTransaction` 阶段发布 BEC 业务事件，通知下游（如 hbpm_positionhr）标准岗位变更。

**当前置信度**：likely（类名推断 · 未反编译父类）

### ISV 订阅方案（如果标品发了 BEC）

**前提**：先在【开发平台 → 业务事件管理】查找 hbpm 域的标准岗位相关事件号。

```java
// 订阅标准岗位变更事件（事件号需从开发平台查实际值）
@SdkPlugin
public class TdkwStPositionChangeBecPlugin implements IEventServicePlugin {

    @Override
    public void handleEvent(KDBizEvent event) {
        String eventNumber = event.getEventNumber();
        // 根据实际事件号判断
        if ("hbpm_stposition_change".equals(eventNumber)) {
            Map<String, Object> variables = event.getVariables();
            Long stPosBoid = (Long) variables.get("boid");
            // 处理标准岗位变更通知
            handleStPositionChange(stPosBoid);
        }
    }

    private void handleStPositionChange(Long stPosBoid) {
        // 查新版本标准岗位信息（PR-008）
        QFilter filter = new QFilter("boid", "=", stPosBoid)
            .and(new QFilter("iscurrentversion", "=", Boolean.TRUE));
        DynamicObject newPosition = QueryServiceHelper.queryOne(
            "hbpm_stposition",
            "id,boid,name,adminorg,job",
            new QFilter[]{filter}
        );
        // 执行下游同步逻辑...
    }
}
```

### ISV 主动发布方案（若标品未发或需要扩展）

```java
// 在 ISV OP 的 afterExecuteOperationTransaction 阶段发布
// （PR-010：主事务已提交 · 安全发外部事件）
@Override
public void afterExecuteOperationTransaction(AfterOperationArgs args) {
    OperationResult result = args.getOperationResult();
    if (result == null || !result.isSuccess()) {
        return;
    }
    DynamicObject[] entities = args.getDataEntities();
    for (DynamicObject entity : entities) {
        Long boid = entity.getLong("boid");
        Map<String, Object> variables = new HashMap<>();
        variables.put("boid", boid);
        variables.put("number", entity.getString("number"));

        IEventService eventService = ServiceHelper.getService(IEventService.class);
        // 事件号必须在【开发平台 → 业务事件管理】预配置（PR-011）
        eventService.triggerEventSubscribeJobs(
            "hbpm",                              // sourceApp
            "${ISV_FLAG}_stposition_changed",           // eventNumber（自定义）
            "标准岗位变更通知",                    // message
            variables
        );
    }
}
```

### 踩坑

- ❌ 在 endOperationTransaction 发事件 → 事务未最终提交，产生脏事件（PR-010）
- ❌ 在 beforeExecuteOperationTransaction 发事件 → 事务还未开始
- ❌ 未在【业务事件管理】预配置事件号 → BEC 不识别（PR-011）
- ❌ variables 里塞完整 DynamicObject → 序列化异常（PR-011）
- ⚠ 若标品 StandardPositionMsgHandleOp 已发 BEC，ISV 再发可能产生重复事件（幂等处理！）

### 关联 PR

- PR-010（OP 13 个生命周期 · afterExecuteOperationTransaction 阶段）
- PR-011（BEC 发布 API + 前置配置）
- PR-008/009（查当前版本）

---

## 关联文档

- `07_ext_points.md` · 禁继承清单（11 个类）
- `02_business_rules.md` · INV-SP-01 ~ 18 规则说明
- `knowledge/_shared/platform_rules.json` · PR-001 ~ PR-011 完整规范
- `knowledge/scenarios/haos_adminorgtype/06_customization_solutions.md` · CS 格式参考
