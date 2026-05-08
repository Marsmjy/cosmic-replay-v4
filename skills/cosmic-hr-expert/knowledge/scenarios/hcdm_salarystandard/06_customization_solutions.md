# 定制化方案集 · hcdm_salarystandard

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（按 mines + deep_resolve 推可做/不可做）
> **数据源**: `rules_chain_all.json::mines` · `_deep_resolve_index.json` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py（v5.1 render_06）

## ✅ 可做（ISV 扩展路径）

### 1. 在主表加 ISV 扩展字段

主表 `hcdm_salarystandard` 当前 116 个字段。
ISV 扩展元数据（建独立 ISV form 用 `_inherits` 引用主表）→ 加业务字段·不动标品。

```bash
# 1. 通过 OpenAPI buildMeta 建 ISV 扩展元数据
# 2. parentId=<hcdm_salarystandard formId>
# 3. 字段命名加 ISV 前缀（避免和未来标品冲突）
```

### 2. 继承标品 OP 类加扩展逻辑（11 个可继承类）

| FQN | opKey | 可重写生命周期方法 |
|---|---|---|
| `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` | `save` | afterBindData, afterLoadData, beforeDoOperation, afterDoOperation, preOpenForm |
| `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin` | `save` | afterBindData |
| `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin` | `save` | beforeBindData, afterCreateNewData, afterLoadData, afterBindData, beforeDoOperation |
| `kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin` | `save` | afterBindData, beforeItemClick, beforeDoOperation |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | `save` | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataOp` | `save` |  |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | `save` | beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | `save` | onPreparePropertys, beforeExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | `save` | onPreparePropertys, onAddValidators |
| `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | `save` | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction, beginOperationTransaction, afterExecuteOperationTransaction |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | `save` | beforeExecuteOperationTransaction, beginOperationTransaction, afterExecuteOperationTransaction |

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

### 禁继承类（共 10 个）

- `HRBaseDataImportEdit` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRBaseDataTplList` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- `HRBasedataLogList` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
- `HisModelListCommonPlugin` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
- `HisModelBuListPlugin` (LIST_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin`
- `HisModelF7ListPlugin` (OP_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
- `HisModelFilterPanelListPlugin` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
- `HisModelFilterPanelF7ListPlugin` (OP_PLUGIN) ← `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
- `HisModelMobileListPlugin` (OTHER) ← `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
- `HisBaseDataF7FastFilter` (OTHER) ← `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`

**原因**：这些类带 `@SdkInternal` 或 `@SdkPlugin(role=internal)` 注解 · ISV 不得继承。

## ℹ️ 标品已接入插件（56 个 · 详见 07_ext_points.md）

- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`
- `BdCtrlStrtgyShowLogicPlugin` ← `kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicPlugin`
- `BaseDataCreateOrgPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataCreateOrgPlugin`
- `BaseDataFormPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataFormPlugin`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- ...（共 56）

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
> 生成时间：2026-05-02 · 数据源 cs_drafts/hcdm_salarystandard/

---

## CS-01 · 给薪酬标准表加 ISV 标准类型字段（最高频）

### 需求
业务方说：薪酬标准表要新增「适用区域 / 适用职级 / 标准类型（基础 / 技能 / 绩效）」等扩展字段·支持多维度差异化薪酬体系。

### 推荐方案
- **扩展对象**: `hcdm_salarystandard`（薪酬标准表 · BaseFormModel · 116 字段）
- **扩展点**: `modifyMeta(op=add field)`
- **风险**: 低
- **⚠️ 时态字段警告**: 跟 wtp_attfilebase 同款·不要碰 boid/iscurrentversion/bsed/bsled

### 调用链
```
Step 1-3: getDevInfo + getBizApps + modifyMeta
  bizAppId = 0VO5EV13=I9W (薪酬管理)
  ops: [{op:"add", element:{
    fieldType:"BasedataField",
    key:"${ISV_FLAG}_region",
    name:{zh_CN:"适用区域"},
    refEntity:"haos_adminorghrf7"     // 引用行政组织（实证 BasedataField + refEntity）
  }}]
Step 4: getFormSchema 验证
```

### 代码框架
```python
designer.add_field(field_type="BasedataField", name="适用区域", key="${ISV_FLAG}_region",
                   ref_entity="haos_adminorghrf7", parent_entity_id=designer.base_entity_id)
designer.save()
```

### 踩坑
- ❌ 不带 ISV 前缀（${ISV_FLAG}_）→ 标品升级被覆盖
- ❌ 引用组织用 `HRAdminOrgField`（OpenAPI 74 值枚举不支持）→ 改用 `BasedataField` + `refEntity=haos_adminorghrf7`
- ❌ 加了字段没回填历史数据 → HisModel 旧版本字段为 null · 算薪时报空指针
- 💡 字段加完跑 `pdmConvert + pdmSave + pdmSubmit` 三步落库 · 不然查不到
- 💡 116 字段已经很多·继续加要评估是否必要 · 优先用 `${ISV_FLAG}_extprop` JSON 字段塞所有扩展属性

---

## CS-02 · 加自定义 Validator（标准值上下限 / 跨字段校验）

### 需求
ISV 业务规则：薪酬标准的 `salaryitem` 必须满足公司规则——技术岗位 P5 级薪酬上限不能超过 50000·校验失败阻断保存。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` 在 `onAddValidators`
- **opKey**: `save`
- **风险**: 低

### 代码框架
```java
package ${ISV_FLAG}.swc.salarystandard.opplugin;

import kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.dataentity.entity.DynamicObject;
import java.math.BigDecimal;

public class TdkwSalaryStandardLimitValidatorOp extends HRBaseDataStatusOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new SalaryLimitValidator());
    }

    static class SalaryLimitValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (ExtendedDataEntity ext : this.dataEntities) {
                DynamicObject obj = ext.getDataEntity();
                String jobLevel = obj.getString("joblevel.number");
                BigDecimal stdValue = obj.getBigDecimal("salaryvalue");

                BigDecimal limit = SalaryLimitConfig.getLimit(jobLevel);
                if (limit != null && stdValue.compareTo(limit) > 0) {
                    this.addErrorMessage(ext, jobLevel + " 级别上限 " + limit + " · 当前 " + stdValue);
                }
            }
        }
    }
}
```

### 注册
```python
client.register_plugin(formId="hcdm_salarystandard", operationKey="save",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.swc.salarystandard.opplugin.TdkwSalaryStandardLimitValidatorOp")
```

### 踩坑
- ❌ 在 `beforeExecuteOperationTransaction` 注册 → 太晚（mines 实证）
- ❌ 用 `obj.getDynamicObject("joblevel").get("number")` 而非 `obj.getString("joblevel.number")` → 多走一次 ORM 加载（性能差 N 倍）
- ❌ `addErrorMessage` 后还自己 throw → 平台错误信息收集机制紊乱
- 💡 跨 entity 字段访问用点路径 `xxx.yyy.zzz` 让 ORM 自动 join · 不要手动调 `getDynamicObject`

---

## CS-03 · HisModel 时态写：调薪自动生成新版本

### 需求
ISV 业务方需要：HR 调整薪酬标准时·标品自动维护历史版本（旧版本 bsled 截断 / 新版本 iscurrentversion=true）·并把生效日期设为 HR 选择的日期（不是当前时间）。

### 推荐方案
- **扩展点**: 直接调标品 `HisModelHelper.changeEffective` API
- **opKey**: 自定义 `${ISV_FLAG}_chgsalary` 或复用标品 `save` 都行
- **风险**: 中（HisModel API 用错会写脏历史）

### 调用链
```
HR 在前端点「调薪」按钮 → 弹窗选生效日期 + 新薪酬值
  → ${ISV_FLAG}_chgsalary opKey 触发
  → ISV.beforeExecuteOperationTransaction:
       Long oldVersionId = obj.getPkValue();
       HisModelHelper.changeEffective(
           "hcdm_salarystandard",     // formId
           oldVersionId,              // 旧版本 PK
           "salaryvalue",             // 要改的字段
           newValue,                  // 新值
           effectiveDate              // 生效日（HR 选的）
       );
       // 平台自动:
       //   1. 旧版本 bsled = effectiveDate - 1
       //   2. 旧版本 iscurrentversion = false
       //   3. 新版本 boid 同旧 / hisversion = old+1 / iscurrentversion = true / bsed = effectiveDate
```

### 代码框架
```java
package ${ISV_FLAG}.swc.salarystandard.opplugin;

import kd.hr.hbp.business.servicehelper.HisModelHelper;
import kd.bos.entity.plugin.args.BeforeOperationArgs;

public class TdkwSalaryChangeOp extends HRDataBaseOp {
    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        super.beforeExecuteOperationTransaction(e);
        for (DynamicObject obj : e.getDataEntities()) {
            Long oldId = (Long) obj.getPkValue();
            BigDecimal newValue = (BigDecimal) obj.get("${ISV_FLAG}_newsalary");
            Date effectiveDate = (Date) obj.get("${ISV_FLAG}_effectivedate");

            HisModelHelper.changeEffective("hcdm_salarystandard",
                                           oldId, "salaryvalue", newValue, effectiveDate);
        }
    }
}
```

### 注册新 opKey
```python
client.add_operation(formId="hcdm_salarystandard", opKey="${ISV_FLAG}_chgsalary",
                     opType="custom", name={"zh_CN":"调薪"})
client.register_plugin(formId="hcdm_salarystandard", operationKey="${ISV_FLAG}_chgsalary",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.swc.salarystandard.opplugin.TdkwSalaryChangeOp")
```

### 踩坑
- ❌ 直接 `obj.set("salaryvalue", newValue) + SaveServiceHelper.save()` → HisModel 不识别·历史版本错乱（boid 同·iscurrentversion 多个 true）
- ❌ effectiveDate 给当前时间而非 HR 选择的日期 → 业务规则错（应该是未来生效）
- ❌ 不传 effectiveDate（null）→ 平台默认当前时间·历史可能被错误截断
- 💡 跑完 changeEffective 后查询验证：`HisModelHelper.queryByEffectiveDate(formId, boid, date)` · 应返回新版本
- 💡 同一 boid 不能并发 changeEffective · 平台用 boid 锁 · 大批量要分组提交

---

## CS-04 · 自定义导出格式（Excel 模板 + 多表头）

### 需求
HR 想导出薪酬标准表为 Excel·按"业务部门"分组·每组一个 Sheet·并包含中英文双表头。标品导出格式不满足。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` 重写导出 op 或新增导出 opKey
- **opKey**: `export_from_list_hr`（标品已有·rules_chain 实证）
- **风险**: 低（导出失败回滚不影响数据）

### 调用链
```
列表页 → 点「导出 - 自定义格式」按钮 → ${ISV_FLAG}_exportcustom opKey 触发
  → ISV.beforeExecuteOperationTransaction:
       List<DynamicObject> rows = e.getSelectedRows();
       Map<String, List<DynamicObject>> byOrg = group(rows, "org");
       Workbook wb = new XSSFWorkbook();
       byOrg.forEach((orgName, orgRows) -> {
           Sheet sheet = wb.createSheet(orgName);
           writeHeaders(sheet);     // 中英文双表头
           writeRows(sheet, orgRows);
       });
       cacheFileToDownloadCenter(wb, "薪酬标准_" + LocalDate.now() + ".xlsx");
```

### 代码框架
```java
package ${ISV_FLAG}.swc.salarystandard.opplugin;

import kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TdkwSalaryStandardCustomExportOp extends HRBaseDataLogOp {
    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        if (!"${ISV_FLAG}_exportcustom".equals(e.getOperationKey())) {
            super.beforeExecuteOperationTransaction(e);
            return;
        }
        XSSFWorkbook wb = buildWorkbook(e.getDataEntities());
        String fileUrl = AttachmentServiceHelper.upload("salarystandard.xlsx", wb);
        getOption().setVariableValue("downloadUrl", fileUrl);
    }
}
```

### 注册
```python
client.add_operation(formId="hcdm_salarystandard", opKey="${ISV_FLAG}_exportcustom",
                     opType="custom", name={"zh_CN":"自定义导出"})
client.register_plugin(formId="hcdm_salarystandard", operationKey="${ISV_FLAG}_exportcustom",
                       pluginType="OPERATION", targetType="LIST_FORM",   # ⚠ 列表 op
                       className="${ISV_FLAG}.swc.salarystandard.opplugin.TdkwSalaryStandardCustomExportOp")
```

### 踩坑
- ❌ 直接重写 `export_from_list_hr` 标品 op → 标品业务也被覆盖 · 应该新增 opKey
- ❌ targetType 写 OPERATION（应该 LIST_FORM）→ 列表选不到按钮
- ❌ 导出大数据集（10w+）一次拉到内存 → OOM · 应该分页流式导出
- ❌ 文件不传到下载中心而是直接 response.write → 大文件浏览器超时
- 💡 用 `AttachmentServiceHelper.upload` 上传到下载中心 · 返回 URL · 前端跳转下载（标品套路）

---

## CS-05 · 接收员工变动协作（人员调岗自动切换薪酬标准）

### 需求
核心人力发生员工调岗时·考勤档案要自动更新（CS-04 wtp_attfilebase 已实现）·同时**薪酬标准也要切换**——员工从 P5 调到 P6·应自动应用新职级的薪酬标准。

### 推荐方案
- **扩展点**: 实现 `kd.hr.hbp.business.coordination.api.EmployeeCoordService` 接口·与 collab_payroll 协作链对接
- **协作链**: `core_hr_change_collab_payroll` → `kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask` → ISV 实现
- **风险**: 中-高（跨云协作）

### 调用链
```
core_hr 员工保存(transfer) → BEC 发员工变动事件
  → HpdiEmployeeCoordEventServicePlugin (订阅) 收到
  → 派 sch_task: hpdi_HpdiEmployeeCoordTask_SKDJ_S
  → HpdiEmployeeCoordTask.execute(标品基类)
    → ISV.execute (覆盖) → 切换 hcdm_salarystandard 关联
```

### 代码框架
```java
package ${ISV_FLAG}.swc.salarystandard.coord;

import kd.hr.hbp.business.coordination.api.EmployeeCoordService;
import kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask;

public class TdkwSalaryStandardCoordImpl implements EmployeeCoordService {
    @Override
    public void execute(EmployeeCoordEvent event) {
        if (event.getChangeType() != ChangeType.TRANSFER) return;

        Long empId = event.getEmployeeId();
        String newJobLevel = event.getNewJobLevel();
        Date effectiveDate = event.getEffectiveDate();

        // 1. 找新职级对应的标品薪酬标准
        Long newStdId = SalaryStandardHelper.findByJobLevel(newJobLevel);

        // 2. 用 HisModel 时态切换薪资档案的 standard 引用
        HisModelHelper.changeEffective("hsas_salaryfile", empId,
                                       "standardid", newStdId, effectiveDate);

        // 3. 触发回溯事件（旧标准期间已发的工资可能要重算）
        SalaryRetroHelper.triggerRetro(empId, event.getOldEffectiveDate(), effectiveDate);
    }
}
```

### 踩坑
- ❌ 不实现 EmployeeCoordService · 直接订阅 BEC → 失去标品的失败重试 / 幂等
- ❌ 不触发回溯（SalaryRetroHelper） → 历史已发工资按旧标准·新标准生效后只算未来工资·业务报错
- ❌ 在 execute() 抛异常但不处理 → sch_task 无限重试拖死队列
- 💡 跨云 collab L7 锚点参见 `core_hr_change_collab_payroll` 11md · ISV 实现侧用 EmployeeCoordService 接口
- 💡 标品已实证发了 9 个 BEC 事件·但都是 `hsbs_standarditem.*`（定调薪项目）·不是 `hcdm_salarystandard.*`·走 collab 框架更稳

