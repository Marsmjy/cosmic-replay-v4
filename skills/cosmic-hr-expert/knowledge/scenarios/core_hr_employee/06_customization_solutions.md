# core_hr_employee · 定制方案（ISV 扩展真实模板）

> **form**：`hrpi_employee`
> **生成时间**：2026-04-29 / 2026-05-07 加 CS-00（铁律 9 模型工具优先）
> **方法**：基于反编译实物的标品类，给出 ISV 真扩展模板（不是空壳骨架）

## CS-00 · HR 业务模型工具配置化（⭐ 最高优先 · 99% 走这里 · 0 行 Java）

> **铁律 9** · 任何"加字段 / 加附表 / 加分录 / 改视图"类需求·**99% 走这里**·跳过 = 触发反模式 AP-021/AP-022。
>
> 完整官方案例：[`docs/PPT03_DEEP_TRACE.md` 第 11 节"员工健康状况附表"](../../docs/PPT03_DEEP_TRACE.md)（slide 47-66·4 步 SOP）
> ISV 决策树：[`docs/PPT03_DEEP_TRACE.md` 第 12 节](../../docs/PPT03_DEEP_TRACE.md)
> 6 大附表模板矩阵：[`docs/PPT03_DEEP_TRACE.md`](../../docs/PPT03_DEEP_TRACE.md) 第 108-115 行

### 业务场景

- 加员工档案附表（如健康信息·教育经历·证书·体检·紧急联系人 ...）
- 加员工档案字段（如紧急联系人·身高·血型·入司前工龄 ...）
- 调整档案视图（加/隐字段·改字段顺序）
- 加任职信息附表（继承 hrpi_assigntpl 类模板·跟组织有关）

### 6 大附表模板决策矩阵（来自 PPT03:108）

| 模板 | 跟组织有关? | 需历史? | 用对话框? | 适用场景 |
|---|---|---|---|---|
| `hrpi_assigntimelinetpl` | ✅ | ✅ | ✅ | 跟组织/任职有关 + 时间轴维护 + 对话框（如任职变动记录）|
| `hrpi_assigntpl` | ✅ | ❌ | ❌ | 跟组织/任职有关 + 当前态（如任职属性扩展） |
| `hrpi_assigndialogtpl` | ✅ | ❌ | ✅ | 跟组织有关 + 对话框（不维历史） |
| `hrpi_employeetimelinetpl` | ❌ | ✅ | ✅ | 不跟组织 + 时间轴 + 对话框（如健康记录·体检记录）|
| `hrpi_employeetpl` | ❌ | ❌ | ❌ | 不跟组织 + 当前态（如紧急联系人·证件信息） |
| `hrpi_employeedialogtpl` | ❌ | ❌ | ✅ | 不跟组织 + 对话框（不维历史） |

### 实施 SOP（6 步·完全配置化）

```
1. HR 业务模型工具 → 添加人员附表 → 选 6 大模板之一（按上面矩阵）
   → 自动建独立物理表 + 自动挂视图 + 自动建历史版本（若选 timeline 模板）

2. 加字段（必带 ISV 前缀·铁律 AP-014）
   - 字段命名：${ISV_FLAG}_<字段含义>·如 abc_overallresult
   - 字段类型：从苍穹 74 字段类型选（基础资料 / 文本 / 数值 / 时间 / 大字段 / ...）

3. BOS 设计器 → 配 UI 布局
   - 卡片样式 / 列表字段 / 必填项

4. HR 业务模型工具 → 添加至人员档案视图 / 员工档案视图
   - 这一步标品自动挂载·不需要写 plugin

5. 业务规则配置 → 校验 / 联动（零代码）
   - 如"体检异常时·附件必传"
   - 用业务规则平台·不写 Java

6. 仅在【复杂业务规则】配置不出来时·才下沉到 CS-04+ 加 Plugin
   （继承 ERManFileCommonAttPlugin·不要继承 HRDataBaseEdit + 自写 EntryEntity）
```

### 反指引（必看）

- ❌ 在 `hrpi_employee` 主表加 EntryEntity 当附表 → 触发 **AP-021** 反模式
  - 原因：主表是历史版本表（boid/iscurrentversion）·加 entry 触发整张档案出新版本
  - 正解：新建独立子实体（继承 6 大附表模板之一）
- ❌ 跳过模型工具直接写 FormPlugin + OpPlugin + Validator → 触发 **AP-022** 反模式
  - 原因：标品已包揽建表/历史版本/视图挂载/跨云联动
  - 正解：先用模型工具·复杂逻辑才下沉 CS-04+

### 验证

完成 SOP 后必须验证：
- [ ] 苍穹后台能在"人员档案"看到新附表 tab
- [ ] 新建/修改/删除附表数据正常
- [ ] 时间轴模板：boid 不变·iscurrentversion 切换正确
- [ ] 标品 hrpi_employee 主表数据未变更（升级 0 风险）

---

## CS-01 · 加 ISV 自定义字段

在开发平台给本 form（`hrpi_employee`）加 ISV 字段：

```yaml
步骤:
  1. 设计器中打开本 form 元数据
  2. 添加 ISV 字段（建议加前缀如 _ext / _isv 标识私域）
  3. 字段类型避开 Required=true（防止数据迁入失败）
  4. 元数据扩展归属为 ISV · 不破坏标品 form （PR-001）
```

## CS-03 · 在 save 链加 ISV 业务（after / begin）

```java
@SdkPlugin
public class MyEmployeeAfterOp extends HRDataBaseOp {
    @Override
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        // 同步业务（事务内）
    }
    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        // 异步业务（事务已提交 · 数据已落库）
    }
}
```

## CS-04 · 加 ISV FormPlugin（UI 联动）

> ⚠️ 仅在 CS-00 模型工具 + 业务规则配置都不能覆盖时才走这里。

### 4-A · 主档案 UI 联动（hrpi_employee 主表的 FormPlugin）

ISV **并列挂**（PR-001 · 不要继承标品 Edit）：

```java
@SdkPlugin
public class MyEmployeeIsvFormPlugin extends HRDataBaseEdit {
    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        // ISV UI 联动
    }
}
```

### 4-B · 档案附表 Edit（⭐ 高频继承点 · 配合 CS-00 模型工具）

档案附表（继承 6 大模板新建的子实体）的 FormPlugin **必须继承**苍穹专属父类·不要继承通用 `HRDataBaseEdit`：

| 父类 FQN | 适用 | 来源 |
|---|---|---|
| `kd.hr.hspm.formplugin.web.template.ERManFileCommonAttPlugin` | 专员端档案附表 Edit ⭐ | PPT03:142 |
| `kd.hr.hspm.formplugin.web.template.MyFileCommonAttPlugin` | 员工端档案附表 Edit（自助）| PPT03:142 |
| `ManFileFormMobileCommonPlugin` | 移动端档案 Edit | PPT03:142 |

ISV 学样式（专员端健康附表案例）：

```java
package <isv>.hrpi_ext.healthcheck.formplugin;

import kd.hr.hspm.formplugin.web.template.ERManFileCommonAttPlugin;
import kd.bos.form.events.PropertyChangedArgs;

/**
 * 健康检查附表 Edit
 * 来源：CS-04-B（继承档案附表通用插件）+ PPT03:142
 */
@SdkPlugin
public class AbcHealthCheckPlugin extends ERManFileCommonAttPlugin {
    @Override
    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        if ("abc_overallresult".equals(args.getProperty().getName())) {
            // 体检结果异常 → 自动联动·附件必填
        }
    }
}
```

### 4-C · 反指引

- ❌ 不要继承 `HRDataBaseEdit` + 自写 EntryEntity 来实现"档案附表"·这是 **AP-021** 反模式
- ❌ 不要在主表 hrpi_employee 加 plugin 来"实现附表数据"·走 CS-00 模型工具新建独立子实体
- ✅ 档案附表必须是独立子实体（CS-00 步骤 1 建好）+ 必须继承 ERManFileCommonAttPlugin（仅当复杂业务规则才挂）

## CS-05 · 调用本场景业务 Service

本场景反编译类调用的 Service / Helper（5 个 · 仅前 10）：

- `AssignmentMagSaveValidator`
- `EmployeeCommonStandardMustInputValidator`
- `HRBaseServiceHelper`
- `InfoCollectHiredValidator`
- `OperationService`

> ⚠️ 直接调这些 Service 前必须确认 SDK 注解（@SdkPublic / @SdkPlugin / @SdkService 三大白名单之一）· 详见 [cosmic_sdk_annotation_whitelist.md](../../../../C:/Users/kingdee/.claude/projects/d--aiworkspace-cludecodeworkspace/memory/cosmic_sdk_annotation_whitelist.md)

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 模板基于：admin_org_quick_maintenance（金标准）+ 反编译实证
- 跨云穿透：见 `11_upstream_downstream_logic.md` 自动注入段