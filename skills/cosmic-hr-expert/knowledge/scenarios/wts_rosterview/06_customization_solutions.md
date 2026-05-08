# 定制化方案集 · wts_rosterview

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（按 mines + deep_resolve 推可做/不可做）
> **数据源**: `rules_chain_all.json::mines` · `_deep_resolve_index.json` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py（v5.1 render_06）

## ✅ 可做（ISV 扩展路径）

### 1. 在主表加 ISV 扩展字段

主表 `wts_rosterview` 当前 3 个字段。
ISV 扩展元数据（建独立 ISV form 用 `_inherits` 引用主表）→ 加业务字段·不动标品。

```bash
# 1. 通过 OpenAPI buildMeta 建 ISV 扩展元数据
# 2. parentId=<wts_rosterview formId>
# 3. 字段命名加 ISV 前缀（避免和未来标品冲突）
```

## ❌ 不可做（ISV 雷区）

反编译实证抽出 **3 条** ISV 红线（去重后）：

1. ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
2. ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
3. ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

## ℹ️ 标品已接入插件（2 个 · 详见 07_ext_points.md）

- `RosterPlugin` ← `kd.wtc.wts.formplugin.web.roster.RosterPlugin`
- `RosterManageFilterPlugin` ← `kd.wtc.wts.formplugin.web.roster.manage.RosterManageFilterPlugin`

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
> 生成时间：2026-05-02 · 数据源 cs_drafts/wts_rosterview/

---

## CS-01 · 给按人员排班加 ISV 业务维度字段

### 需求
业务方说：排班视图要新增「项目编号 / 工作地点 / 备注」等业务维度字段·支持按项目维度排班统计。

### 推荐方案
- **扩展对象**: `wts_rosterview`（按人员排班）
- **扩展点**: `modifyMeta(op=add field)`
- **风险**: 低

### 调用链
```
Step 1-3: getDevInfo + getBizApps + modifyMeta
  bizAppId = 15R5/4TCA97N (排班管理)
  ops: [{op:"add", element:{
    fieldType:"BasedataField",
    key:"${ISV_FLAG}_project",
    name:{zh_CN:"项目编号"},
    refEntity:"pmgt_project"  // 引用项目云的项目（跨云引用）
  }}]
Step 4: getFormSchema 验证
```

### 代码框架
```python
designer.add_field(field_type="BasedataField", name="项目编号", key="${ISV_FLAG}_project",
                   ref_entity="pmgt_project",   # 跨云引用项目云
                   parent_entity_id=designer.base_entity_id)
designer.save()
```

### 踩坑
- ❌ 跨云引用 `pmgt_project` 必须先确认它有权限可访问·否则报"实体不存在"
- ❌ 字段加完后排班引擎不识别 → 排班逻辑要改（CS-04）
- ❌ 不带 `${ISV_FLAG}_` 前缀 → 标品升级被覆盖
- 💡 排班视图修改字段会影响多个下游：考勤核算 / 薪酬 / 报表 · 加字段前评估全链
- 💡 项目维度排班是高频需求·建议同时把 `wtbd_shift` 也加 project 字段以支持项目排班规则

---

## CS-02 · 加自定义 Validator（排班冲突校验 / 周工时上限）

### 需求
ISV 业务规则：员工一周工时不能超过 60h（劳动法）·排班保存前必须校验·超过则阻断。

### 推荐方案
- **扩展点**: 继承 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` 在 `onAddValidators`
- **opKey**: `save`
- **风险**: 中（涉及周窗口聚合查询）

### 代码框架
```java
package ${ISV_FLAG}.wtc.roster.opplugin;

import kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.ExtendedDataEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

public class TdkwRosterWeeklyLimitValidatorOp extends HRBaseDataStatusOp {
    private static final BigDecimal WEEKLY_LIMIT = new BigDecimal("60");

    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new RosterWeeklyLimitValidator());
    }

    static class RosterWeeklyLimitValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (ExtendedDataEntity ext : this.dataEntities) {
                Long empId = ext.getValue("employee.id");
                LocalDate date = ((Date) ext.getValue("rosterdate")).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
                int weekOfYear = date.get(WeekFields.ISO.weekOfYear());

                BigDecimal weeklyHours = RosterHelper.getWeeklyHours(empId, weekOfYear);
                BigDecimal newShiftHours = ((DynamicObject) ext.getValue("shift")).getBigDecimal("duration");
                if (weeklyHours.add(newShiftHours).compareTo(WEEKLY_LIMIT) > 0) {
                    this.addErrorMessage(ext, "员工周工时超过 60h 法定上限·当前 " +
                                              weeklyHours + " + " + newShiftHours);
                }
            }
        }
    }
}
```

### 注册
```python
client.register_plugin(formId="wts_rosterview", operationKey="save",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.wtc.roster.opplugin.TdkwRosterWeeklyLimitValidatorOp")
```

### 踩坑
- ❌ 周聚合查询每行触发一次 DB 查询 → 批量保存 100 行 = 100 次查 → 性能差·必须缓存
- ❌ 时间字段不转 LocalDate → 跨时区周计算错乱
- ❌ 跨字段访问 `employee.id` 不用点路径 → 触发额外 ORM 加载
- 💡 缓存 (empId, weekOfYear) → weeklyHours 5 分钟 · 同一批保存共享

---

## CS-03 · 一键批量排班（按规则自动生成）

### 需求
HR 想一键生成下个月的整月排班·按规则（白班 / 夜班 / 周末轮值 / 节假日特殊）自动分配。手工排班 1000 人 × 30 天 = 3 万行·不现实。

### 推荐方案
- **扩展点**: 新增自定义 opKey `${ISV_FLAG}_autoroster` + ISV 排班引擎
- **opKey**: `${ISV_FLAG}_autoroster`（custom）
- **风险**: 中（算法复杂·涉及大量写入）

### 调用链
```
HR 列表页 → 点「一键排班」按钮 → 弹窗选范围（部门 / 月份 / 规则）
  → ${ISV_FLAG}_autoroster opKey 触发
  → ISV.beforeExecuteOperationTransaction:
       List<Employee> emps = getEmpsByDept(deptId);
       List<RosterRule> rules = getRulesForMonth(month);
       for emp in emps:
           List<RosterRecord> records = RosterEngine.assign(emp, month, rules);
           saveRosterBatch(records, batchSize=200);
       return 已生成 N 行排班
```

### 代码框架
```java
package ${ISV_FLAG}.wtc.roster.opplugin;

import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.bos.dataentity.entity.DynamicObject;

public class TdkwAutoRosterOp extends AbstractOperationServicePlugIn {
    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        if (!"${ISV_FLAG}_autoroster".equals(e.getOperationKey())) return;

        Long deptId = (Long) getOption().getVariableValue("deptId");
        String month = getOption().getVariableValue("month");

        List<Long> empIds = EmployeeHelper.findByDept(deptId);
        List<RosterRule> rules = RosterRuleConfig.getForMonth(month);
        int total = 0;

        // 分批生成 + 保存
        for (List<Long> batch : Lists.partition(empIds, 50)) {
            List<DynamicObject> records = new ArrayList<>();
            for (Long empId : batch) {
                List<RosterRecord> empRecords = RosterEngine.assign(empId, month, rules);
                records.addAll(toDynamicObjects(empRecords));
            }
            SaveServiceHelper.save(records.toArray(new DynamicObject[0]));
            total += records.size();
        }

        getOption().setVariableValue("totalGenerated", total);
    }
}
```

### 注册
```python
client.add_operation(formId="wts_rosterview", opKey="${ISV_FLAG}_autoroster",
                     opType="custom", name={"zh_CN":"一键排班"})
client.register_plugin(formId="wts_rosterview", operationKey="${ISV_FLAG}_autoroster",
                       pluginType="OPERATION", targetType="LIST_FORM",
                       className="${ISV_FLAG}.wtc.roster.opplugin.TdkwAutoRosterOp")
```

### 踩坑
- ❌ 一次保存 1000 人 × 30 天 = 3w 行 → ORM 内存爆 / DB 锁竞争 · 必须分批 200 行
- ❌ targetType 写 OPERATION（应该 LIST_FORM）→ 列表选不到按钮
- ❌ 排班算法不考虑员工请假 / 已有排班冲突 → 生成的排班无效
- ❌ 不验证 weeklyLimit → 自动排班可能违反 60h 法律上限（CS-02 校验未跑·因为是 custom op）
- 💡 大量保存推荐用 `BatchSaveServiceHelper` 而非 SaveServiceHelper · 性能 3-5 倍
- 💡 生成完后立即触发 wtte_calresult 重算（CS-03 推送队列）

---

## CS-04 · 接收人员变动协作（员工调岗自动重排班）

### 需求
员工调岗后·原岗位的排班需要自动失效·新岗位的排班需要自动生成（按新岗位的默认班次规则）。

### 推荐方案
- **扩展点**: 实现 `kd.hr.hbp.business.coordination.api.EmployeeCoordService` 接口·复用 collab_attendance 协作链
- **协作链**: collab_attendance → `WtcEmployeeCoordTask` → ISV 实现
- **风险**: 中-高（跨云协作 + 大量删除/新增）

### 调用链
```
core_hr 员工调岗(transfer) → BEC 发员工变动事件
  → WtcEmployeeCoordEventServicePlugin (订阅) 收到
  → 派 sch_task: wtp_WtcEmployeeCoordTask_SKDJ_S
  → WtcEmployeeCoordTask.execute(标品基类)
    → ISV.execute (覆盖) → 处理排班
       → 失效旧岗位排班（HisModel.disable）
       → 按新岗位规则生成新排班
```

### 代码框架
```java
package ${ISV_FLAG}.wtc.roster.coord;

import kd.hr.hbp.business.coordination.api.EmployeeCoordService;

public class TdkwRosterCoordImpl implements EmployeeCoordService {
    @Override
    public void execute(EmployeeCoordEvent event) {
        if (event.getChangeType() != ChangeType.TRANSFER) return;

        Long empId = event.getEmployeeId();
        Date effectiveDate = event.getEffectiveDate();
        Long newOrgId = event.getNewOrgId();

        // 1. 失效旧排班（effectiveDate 之后的）
        QFilter filter = new QFilter("employee", "=", empId)
                            .and("rosterdate", ">=", effectiveDate)
                            .and("iscurrentversion", "=", "1");
        DynamicObject[] oldRosters = BusinessDataServiceHelper.load("wts_rosterview", "id", filter);
        for (DynamicObject r : oldRosters) {
            HisModelHelper.disable("wts_rosterview", r.getLong("id"), effectiveDate);
        }

        // 2. 按新岗位规则生成新排班
        List<RosterRule> newRules = OrgRosterRuleConfig.getRules(newOrgId);
        List<RosterRecord> newRecords = RosterEngine.assignFor(empId, effectiveDate,
                                                               nextMonth(effectiveDate), newRules);
        saveRosterBatch(toDynamicObjects(newRecords), 50);
    }
}
```

### 踩坑
- ❌ 不区分 changeType·所有变动都重排 → 入职 / 离职 / 调岗 都全删全建·浪费
- ❌ 失效旧排班用 `delete` 而非 `HisModel.disable` → 历史排班被物理删除·考勤记录追溯丢失
- ❌ 不用 effectiveDate · 直接当前时间触发 → 历史排班被错误失效
- ❌ 在 execute() 抛异常但不 catch → sch_task 无限重试·拖死队列
- 💡 标品 collab_attendance 协作场景已建·参见 11md L7 段（WtcEmployeeCoordTask 锚点）
- 💡 大量 disable + save 可能慢·考虑异步入队

---

## CS-05 · 自定义导出排班表（按月份 / 部门 / 自定义格式）

### 需求
HR 月底要导出本月排班表给员工·按部门 + 月份分组 · 每月一个 Excel 文件·内含中英文双表头 + 颜色标记（白班绿色 / 夜班蓝色 / 节假日红色）。

### 推荐方案
- **扩展点**: 新增自定义 opKey `${ISV_FLAG}_exportroster_excel` + ISV Excel 生成
- **opKey**: `${ISV_FLAG}_exportroster_excel`（custom）
- **风险**: 低

### 代码框架
```java
package ${ISV_FLAG}.wtc.roster.opplugin;

import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class TdkwRosterExcelExportOp extends AbstractOperationServicePlugIn {
    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        if (!"${ISV_FLAG}_exportroster_excel".equals(e.getOperationKey())) return;

        SXSSFWorkbook wb = new SXSSFWorkbook(100);  // 流式·防 OOM
        // 准备颜色样式
        XSSFCellStyle dayStyle = wb.createCellStyle(); // 白班绿色
        XSSFCellStyle nightStyle = wb.createCellStyle(); // 夜班蓝色
        XSSFCellStyle holidayStyle = wb.createCellStyle(); // 节假日红色
        // ... fillForegroundColor

        // 按部门分组
        Map<String, List<DynamicObject>> byDept = e.getDataEntities().stream()
            .collect(Collectors.groupingBy(d -> d.getString("dept.name")));
        byDept.forEach((dept, rosters) -> {
            Sheet sheet = wb.createSheet(dept);
            writeHeaders(sheet);
            for (DynamicObject r : rosters) {
                Row row = sheet.createRow(...);
                String shiftType = r.getString("shift.type");
                Cell cell = row.createCell(...);
                if ("DAY".equals(shiftType))      cell.setCellStyle(dayStyle);
                else if ("NIGHT".equals(shiftType)) cell.setCellStyle(nightStyle);
                else if (isHoliday(r))               cell.setCellStyle(holidayStyle);
            }
        });

        String fileUrl = AttachmentServiceHelper.upload("排班表_" + month + ".xlsx", wb);
        getOption().setVariableValue("downloadUrl", fileUrl);
        wb.dispose();   // ⭐ 流式必须 dispose
    }
}
```

### 注册
```python
client.add_operation(formId="wts_rosterview", opKey="${ISV_FLAG}_exportroster_excel",
                     opType="custom", name={"zh_CN":"导出排班表"})
client.register_plugin(formId="wts_rosterview", operationKey="${ISV_FLAG}_exportroster_excel",
                       pluginType="OPERATION", targetType="LIST_FORM",
                       className="${ISV_FLAG}.wtc.roster.opplugin.TdkwRosterExcelExportOp")
```

### 踩坑
- ❌ 用 XSSFWorkbook 而非 SXSSFWorkbook → 大数据集 OOM
- ❌ 写完不调 wb.dispose() → 内存泄漏 + 临时文件不清理
- ❌ 颜色样式每个 cell 都 createCellStyle() → 样式过多触发 Excel 文件损坏
- ❌ targetType 写 OPERATION（应该 LIST_FORM）→ 列表选不到按钮
- ❌ 一个 Sheet 写 1 万 + 行 → Excel 打开慢·应分多 Sheet
- 💡 颜色 + 样式只 createCellStyle 一次·复用
- 💡 SXSSFWorkbook 是流式 · 写 10w 行不爆内存

