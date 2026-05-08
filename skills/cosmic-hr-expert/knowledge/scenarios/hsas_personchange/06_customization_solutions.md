# 定制化方案集 · hsas_personchange

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（按 mines + deep_resolve 推可做/不可做）
> **数据源**: `rules_chain_all.json::mines` · `_deep_resolve_index.json` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py（v5.1 render_06）

## ✅ 可做（ISV 扩展路径）

### 1. 在主表加 ISV 扩展字段

主表 `hsas_personchange` 当前 15 个字段。
ISV 扩展元数据（建独立 ISV form 用 `_inherits` 引用主表）→ 加业务字段·不动标品。

```bash
# 1. 通过 OpenAPI buildMeta 建 ISV 扩展元数据
# 2. parentId=<hsas_personchange formId>
# 3. 字段命名加 ISV 前缀（避免和未来标品冲突）
```

### 2. 继承标品 OP 类加扩展逻辑（4 个可继承类）

| FQN | opKey | 可重写生命周期方法 |
|---|---|---|
| `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit` | `save` | preOpenForm |
| `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit` | `save` | preOpenForm |
| `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin` | `save` | afterBindData |
| `kd.hr.hbp.formplugin.web.cert.HRCertCheckList` | `save` | preOpenForm |

**继承套路**：
```java
public class XxxIsvOp extends [上面任一可继承类] {
    @Override
    protected void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new MyIsvValidator());
    }
}
```

注册插件时 `targetType` 必须大写枚举（OPERATION / BILL_FORM / LIST_FORM 之一）。

## ❌ 不可做（ISV 雷区）

反编译实证抽出 **5 条** ISV 红线（去重后）：

1. ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
2. ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
3. ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
4. ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）
5. ❌ 禁继承 AbsOrgBaseOp（非 HR 通用推荐 · 用 HRDataBaseOp 代替）

### 禁继承类（共 2 个）

- `HRBaseDataImportEdit` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `ForbidUrlOpenPlugin` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin`

**原因**：这些类带 `@SdkInternal` 或 `@SdkPlugin(role=internal)` 注解 · ISV 不得继承。

## ℹ️ 标品已接入插件（10 个 · 详见 07_ext_points.md）

- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRCertCheckEdit` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- `HRBaseUeEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `ForbidUrlOpenPlugin` ← `kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin`
- `PersonChangeEdit` ← `kd.swc.hsas.formplugin.web.personchange.PersonChangeEdit`
- `HRCertCheckList` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
- `PersonChangeListPlugin` ← `kd.swc.hsas.formplugin.web.personchange.PersonChangeListPlugin`
- ...（共 10）

## 🚨 决策金字塔（按金蝶 PPT 04 沉淀）

1. **首选标品配置** · 看是否能用元数据 validations / formRules 解决
2. **次选 ISV 扩展元数据** · 加字段 / 加规则 不动标品
3. **再选 ISV OP 插件继承** · 看上面「可继承类」清单
4. **末选 ISV 自建 form** · 无标品可继承时

**禁忌**：不要用 ISV form 直接覆盖标品 form（会丢标品升级红利）

---

# 真 CS 方案集（v5.2 · 实证手写）

> 以下 5 个 CS 来自 `_deep_resolve_index.json` 反编译 + `rules_chain_all.json` opKey 真证据。
> 每个 CS 五段式：背景 → 扩展点 → 调用链 → 代码框架 → 踩坑。
> 生成时间：2026-05-02 · 数据源 cs_drafts/hsas_personchange/

---

## CS-01 · 给人员变动记录加 ISV 业务字段（最高频）

### 需求
业务方说：人员变动记录（薪资分段）要新增「变动审批人 / 变动备注 / 影响月份」等字段·支持 ISV 审计追溯。

### 推荐方案
- **扩展对象**: `hsas_personchange`（人员变动记录 · BaseFormModel · 15 字段·hasSaveOpKey=true）
- **扩展点**: `modifyMeta(op=add field)`
- **风险**: 低

### 调用链
```
Step 1-3: getDevInfo + getBizApps + modifyMeta
  bizAppId = /UHMBBGZQ65X (薪资核算)
  ops: [{op:"add", element:{
    fieldType:"UserField",
    key:"${ISV_FLAG}_approver",
    name:{zh_CN:"变动审批人"},
    refEntity:"bos_user"
  }}]
Step 4: getFormSchema 验证
```

### 代码框架
```python
designer.add_field(field_type="UserField", name="变动审批人", key="${ISV_FLAG}_approver",
                   ref_entity="bos_user", parent_entity_id=designer.base_entity_id)
designer.save()
```

### 踩坑
- ❌ 字段 key 不带 `${ISV_FLAG}_` 前缀 → 标品升级被覆盖
- ❌ UserField 不指定 refEntity=bos_user → OpenAPI 报字段类型错（74 值枚举·UserField 必须 ref bos_user）
- ❌ 加完字段后老变动记录的扩展字段为 null · 算薪逻辑读时报空指针 · 必须默认值或 null-safe
- 💡 hsas_personchange 是 collab_payroll 直接锚点·扩展字段也会被 collab 链路读到·要在协作 SDK 配合处理

---

## CS-02 · 加自定义 Validator（变动日期合法性 / 唯一性）

### 需求
ISV 业务规则：人员变动记录的 `changedate`（变动日期）不能早于员工入职日·也不能晚于今天 + 30 天·校验失败阻断保存。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` 在 `onAddValidators`
- **opKey**: `save`
- **风险**: 低

### 代码框架
```java
package ${ISV_FLAG}.swc.personchange.opplugin;

import kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.ExtendedDataEntity;
import java.time.LocalDate;
import java.util.Date;

public class TdkwPersonChangeDateValidatorOp extends HRBaseDataStatusOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new ChangeDateValidator());
    }

    static class ChangeDateValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (ExtendedDataEntity ext : this.dataEntities) {
                Date chgDate = (Date) ext.getValue("changedate");
                Long empId = ((DynamicObject) ext.getValue("salaryfile")).getLong("employee.id");
                Date entryDate = EmployeeHelper.getEntryDate(empId);
                LocalDate today = LocalDate.now();
                LocalDate maxDate = today.plusDays(30);

                if (chgDate.before(entryDate)) {
                    this.addErrorMessage(ext, "变动日期不能早于入职日 " + entryDate);
                }
                if (chgDate.toLocalDate().isAfter(maxDate)) {
                    this.addErrorMessage(ext, "变动日期不能超过 " + maxDate);
                }
            }
        }
    }
}
```

### 注册
```python
client.register_plugin(formId="hsas_personchange", operationKey="save",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.swc.personchange.opplugin.TdkwPersonChangeDateValidatorOp")
```

### 踩坑
- ❌ 用 `obj.getDate("changedate")` 拿 LocalDate 类型 → API 不支持·只能 `Date` 然后转
- ❌ 校验时调外部 EmployeeHelper.getEntryDate 而不缓存 → 批量保存 100 行 → 100 次查 DB · 性能差
- ❌ 跨字段校验 `salaryfile.employee.id` 没用点路径 → 触发 N 次 ORM 加载
- 💡 用 ORM 点路径 `getValue("salaryfile.employee.entrydate")` 让平台一次性 join

---

## CS-03 · 接收员工变动协作（实现 EmployeeCoordService 写本表）

### 需求
核心人力发生人员变动（入职 / 离职 / 调岗）时·自动写一条 `hsas_personchange` 记录·支撑下游算薪流程。**这是 collab_payroll 的标准实现**。

### 推荐方案
- **扩展点**: 在 ISV 实现 `kd.hr.hbp.business.coordination.api.EmployeeCoordService` · 写到 `hsas_personchange`
- **协作链**: collab_payroll → `kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask` → ISV 实现
- **风险**: 中-高（跨云协作 · 必须幂等）
- **⭐ 这是 hsas_personchange 最核心的 ISV 集成点**

### 调用链
```
core_hr 员工保存(save) → BEC 发员工变动事件
  → HpdiEmployeeCoordEventServicePlugin (订阅) 收到
  → 派 sch_task: hpdi_HpdiEmployeeCoordTask_SKDJ_S
  → HpdiEmployeeCoordTask.execute(标品基类)
    → ISV.execute (覆盖) → 写 hsas_personchange
```

### 代码框架
```java
package ${ISV_FLAG}.swc.personchange.coord;

import kd.hr.hbp.business.coordination.api.EmployeeCoordService;
import kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask;

public class TdkwPersonChangeCoordImpl implements EmployeeCoordService {
    @Override
    public void execute(EmployeeCoordEvent event) {
        // 幂等检查（同 boid + 同 changedate 已存在则跳过）
        if (PersonChangeHelper.exists(event.getEmployeeId(), event.getEffectiveDate())) {
            return;
        }

        DynamicObject pc = BusinessDataServiceHelper.newDynamicObject("hsas_personchange");
        pc.set("number", "PC-" + System.currentTimeMillis());
        pc.set("changedate", event.getEffectiveDate());
        pc.set("source", "COLLAB");                          // 来源 = 协作框架
        pc.set("status", "AUTO_CREATED");
        pc.set("salaryfile", findSalaryFile(event.getEmployeeId()));
        pc.set("changereason", mapChangeReason(event.getChangeType()));

        SaveServiceHelper.save(new DynamicObject[]{pc});
    }
}
```

### 踩坑
- ❌ 不做幂等检查 → BEC 重试机制可能造成重复变动记录 (mines 已实证)
- ❌ 在 execute() 内抛异常但不 catch → 标品 sch_task 无限重试·拖死队列
- ❌ 直接 `obj.set("changedate", new Date())` 用当前时间 → 业务规则错·应该用 `event.getEffectiveDate()` 用变动事件的生效日
- ❌ 不查 `salaryfile`（必填字段）→ 保存 NPE
- 💡 collab_payroll 协作场景已建·参见其 11md 的 L7 段·有 6 层 FQN 闭环

---

## CS-04 · 写完变动后触发薪资回溯（hsas_retroevent）

### 需求
人员变动记录保存成功后·业务方要：自动检测是否影响历史已发工资·若是则触发薪资回溯（hsas_retroevent）·让算薪引擎重算并补差。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` 重写 `afterExecuteOperationTransaction`
- **opKey**: `save`
- **风险**: 中（涉及跨表写 + 回溯链触发）

### 调用链
```
hsas_personchange.save commit 后:
  HRBaseDataLogOp.afterExecuteOperationTransaction(super)
    → ISV.afterExecuteOperationTransaction
       → 判断 changedate 是否落在已发工资期间
       → 若是 → 写 hsas_retroevent 记录（新 TX）
       → 算薪引擎自动消费 retroevent 触发重算
```

### 代码框架
```java
package ${ISV_FLAG}.swc.personchange.opplugin;

import kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.dataentity.entity.DynamicObject;

public class TdkwPersonChangeTriggerRetroOp extends HRBaseDataLogOp {
    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        for (DynamicObject pc : e.getDataEntities()) {
            Date chgDate = pc.getDate("changedate");
            Long salaryFileId = pc.getDynamicObject("salaryfile").getLong("id");

            // 找该员工已发工资期间
            List<PaidPeriod> paid = SalaryHelper.findPaidPeriods(salaryFileId);
            for (PaidPeriod p : paid) {
                if (chgDate.before(p.getEndDate())) {
                    // 落在已发期间 → 触发回溯
                    DynamicObject retro = BusinessDataServiceHelper.newDynamicObject("hsas_retroevent");
                    retro.set("salaryfile", salaryFileId);
                    retro.set("retrofrom", chgDate);
                    retro.set("retrofor", p.getId());
                    retro.set("source", "AUTO_PERSONCHANGE");
                    SaveServiceHelper.save(new DynamicObject[]{retro});
                }
            }
        }
    }
}
```

### 注册
```python
client.register_plugin(formId="hsas_personchange", operationKey="save",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.swc.personchange.opplugin.TdkwPersonChangeTriggerRetroOp")
```

### 踩坑
- ❌ 在 `beforeExecuteOperationTransaction` 写 retroevent → 主表回滚但 retro 已写·脏数据
- ❌ 不判断 changedate 是否落在已发期间·所有变动都触发 retro → 算薪引擎被压爆
- ❌ 同步触发算薪重算 (`SalaryCalcHelper.calcNow`) → save 操作前端转圈几分钟·必须异步入队
- 💡 hsas_retroevent 是触发器·不直接重算·算薪引擎定时消费·这是标品设计 (rules_chain 实证)

---

## CS-05 · 批量导入人员变动记录（Excel · 标品零代码）

### 需求
HR 月初要批量导入 1000+ 人员变动记录·标品已支持但 ISV 想加："导入前自动按变动类型分组校验"·"导入失败的记录单独导出报错原因"。

### 推荐方案
- **扩展点**: 标品已有 `importdata_hr` opKey（rules_chain 实证 12 plugin 链）· ISV 在前后做扩展
- **首选**: 优先用 `hies_entity_import`（苍穹 HR 标品零代码导入·所有 form 通用）
- **风险**: 低（标品已做好导入框架）

### 调用链（标品流程）
```
HR 上传 Excel → hies_entity_import
  → importdata_hr opKey (12 个 plugin 链):
       order=1 HRBaseDataStatusOp
       order=2 HRBaseDataLogOp
       order=3 HRBaseDataEnableOp
       order=4 HRBaseOriginalOp
       order=5 HisModelOPCommonPlugin
       order=6 AttFileBaseSaveOp (业务专属·这里是 PersonChangeSaveOp)
       order=7-12 校验/写表/审计
  → 失败行记录到 hies 导入日志
  → 成功行写 hsas_personchange
```

### ISV 扩展点

#### 选项 1：导入前预处理（推荐）
继承 `kd.hr.hies.opplugin.ImportDataPreprocessOp` 在导入前分组校验：
```java
package ${ISV_FLAG}.swc.personchange.opplugin;

import kd.hr.hies.opplugin.ImportDataPreprocessOp;

public class TdkwPersonChangeImportPreprocessOp extends ImportDataPreprocessOp {
    @Override
    public void preprocess(ImportContext ctx) {
        super.preprocess(ctx);
        // 按变动类型分组
        Map<String, List<ImportRow>> byType = ctx.getRows().stream()
            .collect(Collectors.groupingBy(r -> r.getValue("changereason")));
        // 每组单独校验（不同类型有不同必填字段）
        byType.forEach((type, rows) -> validateByType(type, rows));
    }
}
```

#### 选项 2：导入后导出失败行
继承 `kd.hr.hies.opplugin.ImportDataPostprocessOp`：
```java
public class TdkwPersonChangeImportFailExportOp extends ImportDataPostprocessOp {
    @Override
    public void postprocess(ImportContext ctx) {
        super.postprocess(ctx);
        List<ImportRow> failed = ctx.getFailedRows();
        if (!failed.isEmpty()) {
            String fileUrl = exportFailedRows(failed);
            ctx.addNotification("导入失败 " + failed.size() + " 行 · 详情: " + fileUrl);
        }
    }
}
```

### 注册插件
```python
client.register_plugin(formId="hsas_personchange", operationKey="importdata_hr",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.swc.personchange.opplugin.TdkwPersonChangeImportPreprocessOp")
```

### 踩坑
- ❌ 不用 `hies_entity_import` 框架·自己写 ExcelImportPlugin → 失去标品的进度展示 / 错误收集 / 部分回滚
- ❌ 导入前预处理改了 ImportContext.rows 但忘 set 回去 → 标品继续用原数据
- ❌ 大批量导入（1w+）一次性 commit → DB 锁竞争 · 应该分批 200/批 · 每批独立 TX
- 💡 hies 导入框架是 hr_hrmp 云的"零代码导入器"·参见 `hies_entity_import` 11md

