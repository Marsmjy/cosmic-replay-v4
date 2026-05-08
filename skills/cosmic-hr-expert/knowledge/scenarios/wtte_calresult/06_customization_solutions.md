# 定制化方案集 · wtte_calresult

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（按 mines + deep_resolve 推可做/不可做）
> **数据源**: `rules_chain_all.json::mines` · `_deep_resolve_index.json` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py（v5.1 render_06）

## ✅ 可做（ISV 扩展路径）

### 1. 在主表加 ISV 扩展字段

主表 `wtte_calresult` 当前 19 个字段。
ISV 扩展元数据（建独立 ISV form 用 `_inherits` 引用主表）→ 加业务字段·不动标品。

```bash
# 1. 通过 OpenAPI buildMeta 建 ISV 扩展元数据
# 2. parentId=<wtte_calresult formId>
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

### 3. 调用 SDK 桶服务（4 个白名单服务）

详见 `07_ext_points.md` 的 `curated_sdk 9 桶` 段。

## ❌ 不可做（ISV 雷区）

反编译实证抽出 **5 条** ISV 红线（去重后）：

1. ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
2. ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
3. ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
4. ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）
5. ❌ 禁继承 AbsOrgBaseOp（非 HR 通用推荐 · 用 HRDataBaseOp 代替）

### 禁继承类（共 1 个）

- `HRBaseDataImportEdit` (FORM_PLUGIN) ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`

**原因**：这些类带 `@SdkInternal` 或 `@SdkPlugin(role=internal)` 注解 · ISV 不得继承。

## ℹ️ 标品已接入插件（7 个 · 详见 07_ext_points.md）

- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRCertCheckEdit` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- `HRBaseUeEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `WTCTipsPlugin` ← `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`
- `HRCertCheckList` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
- `CalculateResultList` ← `kd.wtc.wtte.formplugin.web.attcalculate.CalculateResultList`

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
> 生成时间：2026-05-02 · 数据源 cs_drafts/wtte_calresult/

---

## CS-01 · 给考勤核算结果加 ISV 业务字段

### 需求
业务方说：考勤核算结果要新增「迟到次数 / 早退次数 / 缺勤天数 / 工时偏差」等扩展字段·HR 要在结果表上直接看·不用关联打开。

### 推荐方案
- **扩展对象**: `wtte_calresult`（考勤核算结果·业务核心）
- **扩展点**: `modifyMeta(op=add field)`
- **风险**: 低

### 调用链
```
Step 1-3: getDevInfo + getBizApps + modifyMeta
  bizAppId = 1C8H4/N38LCY (考勤核算)
  ops: [{op:"add", element:{
    fieldType:"IntegerField",
    key:"${ISV_FLAG}_latecnt",
    name:{zh_CN:"迟到次数"}
  }}]
Step 4: getFormSchema 验证
```

### 代码框架
```python
designer.add_field(field_type="IntegerField", name="迟到次数", key="${ISV_FLAG}_latecnt",
                   parent_entity_id=designer.base_entity_id)
designer.save()
```

### 踩坑
- ❌ 不带 `${ISV_FLAG}_` 前缀 → 标品升级被覆盖
- ❌ 加完字段·算薪算法没改 → 字段一直为 null·HR 抱怨"没数"
- ❌ 数值字段必须考虑负值（如工时偏差可负）→ 不要用 BigIntField · 用 IntegerField
- 💡 扩展字段加完后必须改算薪算法（CS-02）·两步必须同步

---

## CS-02 · 替换标品考勤核算算法（接 ISV 自定义计算引擎）

### 需求
公司有特殊行业（如医院 / 制造业）需要按"24h 排班 / 跨日工时 / 节假日加倍"算考勤·标品默认算法不满足。

### 推荐方案
- **扩展点**: 实现 `kd.wtc.wtte.business.calculator.IAttendCalculator` 接口
- **opKey**: 不直接绑·考勤核算引擎自动调
- **风险**: 高（算错影响员工工资·必须严格单测）

### 代码框架
```java
package ${ISV_FLAG}.wtc.calc;

import kd.wtc.wtte.business.calculator.IAttendCalculator;
import kd.wtc.wtte.business.calculator.AttendInput;
import kd.wtc.wtte.business.calculator.AttendResult;

public class TdkwHospitalAttendCalculator implements IAttendCalculator {
    @Override
    public String getCalculatorType() {
        return "HOSPITAL";
    }

    @Override
    public AttendResult calculate(AttendInput input) {
        // 医院 24h 排班特殊处理
        BigDecimal totalWork = BigDecimal.ZERO;
        BigDecimal nightHours = BigDecimal.ZERO;
        BigDecimal holidayHours = BigDecimal.ZERO;

        for (AttendRecord rec : input.getRecords()) {
            BigDecimal hours = calcHours(rec);
            totalWork = totalWork.add(hours);
            if (isNightShift(rec)) nightHours = nightHours.add(hours);
            if (isHoliday(rec))    holidayHours = holidayHours.add(hours);
        }

        return new AttendResult(totalWork, nightHours, holidayHours,
                                input.getEmployee().getId(), input.getPeriod());
    }
}

// 注册
@PostConstruct
public class CalculatorRegister {
    public void register() {
        AttendCalculatorRegistry.register(new TdkwHospitalAttendCalculator());
    }
}
```

### 踩坑
- ❌ 算法类型 name 不带 ISV 前缀 → 跟标品冲突
- ❌ 不实现 IAttendCalculator 接口·自己 hook OP plugin → 失去引擎的批量调度 / 重算 / 缓存能力
- ❌ 算法实现有 bug → 全公司员工考勤算错 → 工资也错 · 必须 100% 单测覆盖
- ❌ 引用 input.getRecords() 但不 null 检查 → 遇到无打卡员工时 NPE
- 💡 不同行业 ISV 注册不同 calculator · 平台默认走 `STANDARD` (标品)
- 💡 单测用真实测试用例（HR 提供 5-10 组 input + 期望 output）

---

## CS-03 · 触发自动复算（员工补打卡 / 排班调整后）

### 需求
员工补打卡或者班次调整后·相关期间的考勤核算结果需要自动复算·HR 不用手动点"重算"按钮。

### 推荐方案
- **扩展点**: 监听 `wtp_attfilebase` 或 `wts_rosterview` 的 save 事件 → 异步触发 wtte_calresult 重算
- **触发链**: 多源（补打卡 / 排班调整 / 班次变化）→ 队列 → 异步重算
- **风险**: 中（队列阻塞 / 重算频次过高）

### 调用链
```
1. wtp_attfilebase.save commit
2. ISV.TdkwAttFileChangeOp.afterExecuteOperationTransaction
3. 判断变化类型（补打卡 / 班次切换 / 时态变化）
4. 入队 RecalcQueue（按员工 + 期间维度去重）
5. 异步消费者：
   - WtteRecalcWorker.consume()
   - 调 wtte_calresult 的 recalc opKey
```

### 代码框架
```java
package ${ISV_FLAG}.wtc.recalc;

import kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp;
import kd.bos.entity.plugin.args.AfterOperationArgs;

public class TdkwAttRecalcTriggerOp extends HRBaseDataLogOp {
    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        for (DynamicObject obj : e.getDataEntities()) {
            Long empId = obj.getLong("employee.id");
            String period = obj.getString("period");

            // 入队（按员工 + 期间去重·避免一个员工短时间多次重算）
            RecalcQueue.enqueueDeduped(empId, period);
        }
    }
}

// 异步消费者
public class WtteRecalcWorker {
    @Scheduled(fixedDelay = 60000)  // 1 分钟拉一次队列
    public void consume() {
        List<RecalcTask> tasks = RecalcQueue.poll(50);
        for (RecalcTask t : tasks) {
            try {
                AttendCalcEngine.recalc(t.getEmpId(), t.getPeriod());
            } catch (Exception ex) {
                RecalcDeadLetterQueue.add(t, ex);
            }
        }
    }
}
```

### 踩坑
- ❌ 同步触发重算（在 afterExecute 内直接调 AttendCalcEngine）→ 一个保存触发 1 万人重算·前端转几分钟
- ❌ 不去重·一个员工连续修改打卡 N 次 → 触发 N 次重算·浪费
- ❌ 失败不进死信队列 → 异常被吞·静默漏算
- ❌ 重算频次过高·队列积压 → 算薪结果延迟·HR 抱怨
- 💡 入队按 (empId, period) 5 分钟窗口去重·一个员工同期间最多算一次
- 💡 死信队列 + 监控告警 · 失败要能追溯

---

## CS-04 · 加考勤异常预警（迟到 / 缺勤 / 加班超时）

### 需求
HR 想让系统在考勤核算后自动检测异常（员工连续迟到 3 次 / 月缺勤 > 2 天 / 加班超 36h）·并推送预警给员工 + 直属主管。

### 推荐方案
- **扩展点**: 复用 hr_hrmp 云的预警框架（hrcs_warnscene + hrcs_warnscheme）
- **预警 form**: `hrcs_warnscene`（场景）/ `hrcs_warnscheme`（方案）
- **风险**: 低（预警框架已就绪·配置即可）

### 调用链（复用标品框架）
```
wtte_calresult.save commit
  → ISV 监听 → 检测异常
  → 触发标品 WarnService.trigger("late_3times", empId)
  → 标品 WarnEngine 按 hrcs_warnscheme 配置:
    - 推消息中心给员工
    - 推消息给直属主管（按 hbpm_positionhr 的汇报关系）
    - 写预警执行日志（hrcs_warnexeclog）
```

### 代码框架
```java
package ${ISV_FLAG}.wtc.warn;

import kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.hr.hrcs.business.warn.WarnService;

public class TdkwAttendWarnTriggerOp extends HRBaseDataLogOp {
    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        for (DynamicObject result : e.getDataEntities()) {
            Long empId = result.getLong("employee.id");
            int lateCnt = result.getInt("${ISV_FLAG}_latecnt");
            int absentDays = result.getInt("${ISV_FLAG}_absentdays");
            BigDecimal overtimeHours = result.getBigDecimal("${ISV_FLAG}_overtimehours");

            // 检测各类异常·触发预警
            if (lateCnt >= 3) {
                WarnService.trigger("ATTEND_LATE_3TIMES", empId,
                                    Map.of("count", lateCnt));
            }
            if (absentDays > 2) {
                WarnService.trigger("ATTEND_ABSENT_OVER", empId,
                                    Map.of("days", absentDays));
            }
            if (overtimeHours.compareTo(BigDecimal.valueOf(36)) > 0) {
                WarnService.trigger("OVERTIME_EXCEED", empId,
                                    Map.of("hours", overtimeHours));
            }
        }
    }
}
```

### 配置
```python
# 1. hrcs_warnscene 加预警场景（一次性配置）
client.create_warn_scene(
    sceneCode="ATTEND_LATE_3TIMES",
    description="月迟到 3 次以上预警"
)

# 2. hrcs_warnscheme 配方案（推送给谁）
client.create_warn_scheme(
    sceneCode="ATTEND_LATE_3TIMES",
    targets=["EMPLOYEE", "DIRECT_MANAGER"],
    channel=["MESSAGE_CENTER", "EMAIL"]
)

# 3. 注册触发插件
client.register_plugin(formId="wtte_calresult", operationKey="save",
                       pluginType="OPERATION", targetType="OPERATION",
                       className="${ISV_FLAG}.wtc.warn.TdkwAttendWarnTriggerOp")
```

### 踩坑
- ❌ 自己实现预警推送·不用 hrcs_warnscene → 失去标品的多渠道推送 / 模板配置 / 历史日志
- ❌ 检测每个员工都触发预警 → 每月初 1000+ 员工 × 3 类预警 = 3000 推送 → 必须做去重 / 频率控制
- ❌ 不写预警日志（hrcs_warnexeclog）→ 业务追溯困难
- 💡 预警场景配在 hr_hrmp 云的 hrcs_warnscene · 关联 11 hrcs 场景已建（参见 hr_hrmp 云完整覆盖）
- 💡 ISV 只负责"检测异常 + 触发预警"·推送 / 模板 / 渠道 全交标品

---

## CS-05 · 推送考勤数据给薪酬云（hsas_attbizdata）

### 需求
考勤核算后·HR 要把出勤天数 / 加班 / 迟到等数据推送到薪酬云·支撑算薪。**这是 wtc → swc 跨云协作的核心环节**。

### 推荐方案
- **扩展点**: 实现标品 `kd.swc.hpdi.business.coordination.IAttendDataProvider` 接口
- **目标 form**: 写入 swc 云的 `hsas_attbizdata`（前端业务数据·攻击面 swc 算薪输入）
- **风险**: 中（跨云协作·失败要补偿）

### 调用链
```
wtte_calresult.save commit (考勤核算完成)
  → ISV.afterExecuteOperationTransaction
  → 判断是否到推送时点（月底 / 周期结束）
  → 调标品接口 AttendDataPushService.push(empId, period, data)
  → 标品自动:
    - 转换为 hsas_attbizdata 格式
    - 跨云写到 swc.hsas_attbizdata
    - 触发 swc 算薪流程
```

### 代码框架
```java
package ${ISV_FLAG}.wtc.calc.push;

import kd.swc.hpdi.business.coordination.IAttendDataProvider;
import kd.swc.hpdi.business.coordination.AttendDataDto;
import kd.swc.hpdi.business.coordination.AttendDataPushService;

public class TdkwAttendDataPushImpl implements IAttendDataProvider {
    @Override
    public AttendDataDto provide(Long empId, String period) {
        // 从 wtte_calresult 拿数据
        DynamicObject result = QueryServiceHelper.queryOne("wtte_calresult",
            "${ISV_FLAG}_latecnt, ${ISV_FLAG}_absentdays, ${ISV_FLAG}_overtimehours, normaldays",
            new QFilter[]{
                new QFilter("employee", "=", empId),
                new QFilter("period", "=", period),
                new QFilter("iscurrentversion", "=", "1")
            });

        // 组装 DTO（标品定义的 schema）
        return AttendDataDto.builder()
            .employeeId(empId)
            .period(period)
            .lateCount(result.getInt("${ISV_FLAG}_latecnt"))
            .absentDays(result.getInt("${ISV_FLAG}_absentdays"))
            .overtimeHours(result.getBigDecimal("${ISV_FLAG}_overtimehours"))
            .normalDays(result.getInt("normaldays"))
            .build();
    }
}
```

### 注册
```java
// spring config
@Bean
public IAttendDataProvider attendDataProvider() {
    return new TdkwAttendDataPushImpl();
}
```

### 踩坑
- ❌ 自己直接 InvokeRPC 调 swc API → 失去标品的失败重试 / 幂等 / 跨云事务一致性
- ❌ 不实现 IAttendDataProvider·自己写跨云客户端 → 标品升级时接口变化·ISV 客户端崩
- ❌ 推送 lateCount 等字段后·薪酬侧没配公式用 → 数据进了但白进
- ❌ 不去重·重复推送同一期间 → swc 算薪侧重复算
- 💡 wtc → swc 是 HR 域内最核心的跨云链路·必须走 IAttendDataProvider 标准接口
- 💡 关联场景：collab_payroll 协作场景 / hsas_attbizdatasummary（薪酬侧接收方）

