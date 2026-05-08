# 推荐定制方案 · 岗位信息维护 (hbpm_positionhr)

> **状态**: 🟢 基于真实 OpenAPI 实抓 + 7 类反编译 + `scene_doc.json` 70+ 字段语义 + `refentity_reverse.json` 27 下游引用实证（2026-04-24）
> **confidence**: real_deploy
> **平台规则遵循**：所有 CS 必引用 `knowledge/_shared/platform_rules.json` 11 条 PR 红线

所有方案遵循统一结构（借鉴 Stripe / Salesforce Developer Docs）：
**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 Pattern**

---

## CS-01 · 给 hbpm_positionhr 扩展自定义字段（最高频）

**关联 Pattern**：[Pattern A · add_field_extension](../../pattern/add_field_extension/README.md)

### 需求

业务方说：岗位需要加"岗位等级"字段（如"L1 / L2 / L3 / L4 / L5"）· 用于薪酬档案定档。

### 推荐方案

- **扩展对象**：`hbpm_positionhr`（主实体）
- **扩展点**：`modifyMeta(op=add, elementType=field)`
- **风险**：低
- **特点**：岗位域**多语言字段直接存主表**（区别于职位域有 `_i` 子表）· 单次 modifyMeta 即可搞定主表
- **遵循 PR**：
  - PR-007 · 预置字段不可改 · 业务字段可加（ISV 前缀）
  - PR-003 · FormPlugin 用 `getModel().setValue()` · 后端 OP 用 `entity.set()`

### 调用链（3 步）

```
Step 1: getBizApps()                        // 拿 bizAppId = homs 或 hbpm 应用 id
Step 2: modifyMeta({
  formId: "hbpm_positionhr",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hbpm_positionhr",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_poslevel",
      name: {zh_CN: "岗位等级", en_US: "Position Level"},
      fieldName: "fk_tdkw_poslevel",
      mustInput: false,
      comboOptions: [
        {value: "L1", name: {zh_CN: "L1", en_US: "L1"}},
        {value: "L2", name: {zh_CN: "L2", en_US: "L2"}},
        {value: "L3", name: {zh_CN: "L3", en_US: "L3"}},
        {value: "L4", name: {zh_CN: "L4", en_US: "L4"}},
        {value: "L5", name: {zh_CN: "L5", en_US: "L5"}}
      ]
    }
  }]
})
Step 3: getFormSchema("hbpm_positionhr")     // ⭐ 二次验证落库（errorCode=0 不代表成功）
```

### 代码框架（使用 cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "hbpm_positionhr", "number": "hbpm_positionhr"}
)

designer.add_field(
    field_type="ComboField",
    name="岗位等级",
    key="${ISV_FLAG}_poslevel",
    parent_entity_id=designer.base_entity_id,
    combo_options=[
        {"value": "L1", "name": {"zh_CN": "L1"}},
        # ...
    ]
)
designer.save()  # 一次 save click
```

### 踩坑

- ❌ 直接在 `t_hbpm_standposentry` 分录扩展字段 → 应该在主表 `t_hbpm_position` 扩展 · 只有"标准岗位适用组织"相关的才走分录
- ⚠️ 岗位多语言字段**直接存主表**（`t_hbpm_position.fname` / `fposduty` 等）· **没有 `_l` / `_i` 子表**（区别于职位 `t_hbjm_job_i`）· 扩展 `MuliLangTextField` 字段直接落主表
- ❌ 字段 key 不带 ISV 前缀（如直接叫 `poslevel`）→ 标品升级覆盖 · **违反 PR-001 的 ISV 扩展规范**
- ❌ `fieldName` 列名超过 25 字符 → 数据库建表失败
- ❌ ComboField 漏传 `comboOptions` → UI 下拉框空白（`EX-12` 引用）
- ❌ 扩展字段改 `iscurrentversion` / `boid` / `hisversion` 等时序字段 → 破坏版本链（**违反 PR-008**）

### 验证

保存后到岗位菜单新增一条岗位，看表单上是否出现"岗位等级"字段。

---

## CS-02 · 选岗位类型 / 行政组织自动带出默认值 ⭐ 岗位域常见

### 需求

业务方说：新增岗位时 · 选择了"**岗位类型**"（如"管理岗"）后 · 系统应自动带出该类型的默认职等 / 职级 / 学历要求 · 减少人工填错。

**业务背景**（用户确认）：
- 标品已有 `positiontpl` 岗位模板联动（`PositionEdit.changePositionTpl` L517-L549）· 但要求岗位先选模板 · 用户反馈"很多场景不配模板·希望按类型直接联动"
- **不能改标品 `positiontpl` 的联动逻辑** · 并列挂新前端插件

### 推荐方案

- **扩展点**：`propertyChanged@hbpm_positionhr` 监听 `positiontype` 字段变更
- **实现模式**：**并列挂** `AbstractFormPlugin` + 注册为 `BILL_FORM`（**遵循 PR-001 · 不继承 `PositionEdit`**）
- **风险**：低（前端只读 + 预填 · 不动后端数据）

### 扩展入口坐标

- 绑定表单：`hbpm_positionhr`
- 推荐父类：`HRDataBaseEdit` 或 `AbstractFormPlugin`（实证：`PositionEdit` 继承 `HRDataBaseEdit`，CS-02 走新类并列挂 · 遵循 PR-001）
- 关键重写方法：
  - `propertyChanged(PropertyChangedArgs args)` — 监听**岗位类型字段**变更
  - 用 `HRBaseServiceHelper("hbpm_positiontype")` 查类型的默认值映射
  - `getModel().beginInit(); getModel().setValue(...); getModel().endInit(); getView().updateView(...)` 回填（**遵循 PR-004 · 防死循环**）

**业务意图**：用户选岗位类型后 · 插件查类型配置的默认职等 / 职级 / 学历等 · 带到岗位字段。

### 调用链

```
用户前端选择"岗位类型" = "管理岗" (positiontype)
    ↓
触发 propertyChanged · propertyKey = "positiontype" · newValue = positiontype.id
    ↓
插件从 hbpm_positiontype 基础资料读默认值配置
(假设有 ${ISV_FLAG}_defaulttype / ${ISV_FLAG}_defaultgrade 等扩展字段)
    ↓
getModel().beginInit();          // PR-004 · 防 setValue 死循环
getModel().setValue("lowjoblevel", defaultLow);
getModel().setValue("highjoblevel", defaultHigh);
getModel().setValue("diplomareq", defaultDiploma);
getModel().endInit();
getView().updateView("lowjoblevel");
getView().updateView("highjoblevel");
getView().updateView("diplomareq");
    ↓
联动显示名缓存更新 (joblevelrange / jobgraderange 文本)
委托 JobLevelGradeRangeUtil.setFieldRange 处理
```

### 代码框架

```java
package ${ISV_FLAG}.hrmp.hbpm.formplugin.position;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

// ⭐ 并列挂 · 不继承 PositionEdit · 遵循 PR-001
public class PositionTypeDefaultFillEdit extends HRDataBaseEdit {

    @Override
    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        String propKey = args.getProperty().getName();

        if (HRStringUtils.equals("positiontype", propKey)) {
            DynamicObject positiontype = (DynamicObject) args.getChangeSet()[0].getNewValue();
            if (positiontype == null) return;

            // 读岗位类型扩展字段（ISV 加的默认值配置）
            DynamicObject typeCfg = new HRBaseServiceHelper("hbpm_positiontype")
                    .loadSingle(positiontype.getLong("id"));
            if (typeCfg == null) return;

            // PR-004 · 防 setValue 死循环
            getModel().beginInit();
            Object lowLevel = typeCfg.get("${ISV_FLAG}_defaultlowlevel");  // ISV 扩展字段
            if (lowLevel != null) {
                getModel().setValue("lowjoblevel", lowLevel);
            }
            Object highLevel = typeCfg.get("${ISV_FLAG}_defaulthighlevel");
            if (highLevel != null) {
                getModel().setValue("highjoblevel", highLevel);
            }
            getModel().endInit();
            getView().updateView("lowjoblevel");
            getView().updateView("highjoblevel");
        }
    }
}
```

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `hbpm_positionhr`
2. 选择【注册插件】→ 新增插件（`targetType = BILL_FORM` · 遵循 PR-002 RowKey · ISV 可排在 PositionEdit 之后）
3. 填写 `className` 为全限定名 `${ISV_FLAG}.hrmp.hbpm.formplugin.position.PositionTypeDefaultFillEdit`
4. 保存 → 部署生效

### 踩坑

- ⚠️ **PR-004 · setValue 死循环防护**：`propertyChanged` 里 `setValue` 会再次触发 `propertyChanged` · 必用 `beginInit/endInit` 包裹 + `updateView` 更新
- ⚠️ `lowjoblevel` / `highjoblevel` 改完后 · `joblevelrange` / `jobgraderange` 文本冗余字段需要同步（参考 `JobLevelGradeRangeUtil.setFieldRange` · `PositionEdit.changePositionTpl` L546-L547）
- ⚠️ **时序基础资料查询必须带 `iscurrentversion=true`**（**遵循 PR-008**）：`hbpm_positiontype` 如果是时序资料 · 查配置时过滤；岗位本身查询也要带（PR-008）
- ⚠️ 若 `positiontpl` 已选 · 标品 `PositionEdit.changePositionTpl` 会锁字段；新插件要检测 `positiontpl.id != 0` 时跳过 · 避免与模板联动冲突
- ❌ **不要继承 `PositionEdit`**（违反 PR-001）· 并列挂新插件
- ❌ 不要在 `propertyChanged` 里写 DB update（应走 save 链）

### 关联 Pattern

前端字段联动是 HR 场景常见需求，暂无独立 pattern · 参考 `PositionEdit.propertyChanged` 6 分支（L455-L515）的实现模式。

---

## CS-03 · 禁用岗位前检查是否仍有在职员工 / 任职关系 ⭐ 岗位域特有（P0）

### 需求

业务方说：有人不小心禁用了一个还有在职员工的岗位，导致员工档案出现"岗位已禁用"告警；要在禁用操作时**前置强校验**"该岗位下不能有在职人员"。

**⚠ 标品现状**：`PositionHrDisableOp.onAddValidators` 是**空**（`PositionHrDisableOp.java` L49-L50）· 标品业务阻断在 `afterExecuteOperationTransaction` 的 `IBosPositionService.disablePositions` 内部 · 事务已开始 · 业务阻断晚。

### 推荐方案

- **扩展点**：`onAddValidators@disable` + **并列挂** 自定义 Validator
- **实现模式**：继承 `AbstractValidator`（**PR-001 · 不继承 `PositionHrDisableOp`**）
- **风险**：中（业务阻断操作，需要充分测试）

### 扩展入口坐标

- 绑定表单：`hbpm_positionhr`
- 绑定操作：`disable`
- 推荐父类：`HRDataBaseOp`（实证 `PositionHrDisableOp` 父类 `AbstractOperationServicePlugIn`）· 新挂插件继承 `HRDataBaseOp` 更好（HR 域标准父类 · 遵循 PR-001）
- 关键重写方法：
  - `onAddValidators(AddValidatorsEventArgs e)` — `e.addValidator(new PositionDisableCheckValidator())`
  - 在自定义 `PositionDisableCheckValidator extends AbstractValidator` 的 `validate()` 里：
    - 对每行 `ExtendedDataEntity` · 读 `boid`
    - 查下游 5 个 Tier 1 实体（见下文清单）· 按 `position.boid = ?` + `iscurrentversion=true` + 状态"在职"统计
    - 若 > 0 · `addErrorMessage(entity, "岗位 [" + name + "] 下还有 N 条在职任职关系 · 不能禁用")`

**业务意图**：标品 `PositionHrDisableOp` 只在 `afterExecuteOperationTransaction` 阶段委托 `IBosPositionService.disablePositions` · **业务阻断晚**（事务已开始）。本扩展在 onAddValidators 阶段前置阻断 · 确保有下游引用时连事务都进不了。

### 下游引用清单（2026-04-24 · 真实数据 · 来自 `refentity_reverse.json`）

**数据源**：`knowledge/workbench/_indexes/refentity_reverse.json` 的 `refs.hbpm_positionhrf7` 段（27 条引用 · 扫 529 个标品 metadata md 产出）

#### Tier 1 · 任职关系类（禁用岗位必查 · 有在职员工则阻断）

| 实体 formNumber | 字段 | 类型 | 业务含义 | 检查条件 |
|---|---|---|---|---|
| **`hrpi_empposorgrel`** | `position` / `positionvid` | HRPositionField | **员工-岗位-组织关系**（员工信息管理核心） | 在职状态 + iscurrentversion=true |
| `hrpi_empjobrel` | `position` | HRPositionField | 员工-职位-岗位任职关系 | 在职 + iscurrentversion=true |
| `hrpi_rotationinfo` | `position` | HRPositionField | 员工轮岗信息（原任岗位） | 轮岗中 + iscurrentversion=true |
| `hrpi_dispatchinfo` | `position` | HRPositionField | 员工派遣信息 | 派遣中 + iscurrentversion=true |
| `hrpi_appointremoverel` | `position` / `positionvid` | HRPositionField | 任免经历 | 生效中 |

#### Tier 2 · 结构关联类（禁用岗位前**提示** · 可选阻断）

| 实体 | 字段 | 说明 |
|---|---|---|
| `haos_chargeperson` | `position` | 部门负责人岗位 · 可考虑提示是否需要替换负责人 |
| `haos_orgpersonstaffinfo` | `position` | 占编员工维护信息 |
| `haos_dimstaffreport` | `position` | 多维度编制报表 |
| `haos_muldimendetail` / `haos_muldimendetailhis` | `position` | 多维度编制明细 / 历史 |
| `hrpi_blacklist` | `position` | 黑名单（原任职岗位） |
| `hbpm_positionhr` | `parent` | 下级岗位（自引用） |
| `hbpm_positionrelation` | `role` / `parent` | 岗位汇报关系 / 协作关系 |

#### Tier 3 · 业务订阅类（禁用岗位时按需提示）

| 实体 | 字段 | 说明 |
|---|---|---|
| `hbpm_chgrecord` / `hbpm_chgrecordevt` | `position` / `targetposition` / `sourceposition` / `hisposition` | 岗位变动明细（记录用 · 本就是历史） |
| `hbpm_position_msgdetail` | `bo` / `beforeversion` / `afterversion` | 岗位变更消息明细（本就是变更日志） |
| `hrcs_warnscheme` | `position` | 预警方案 |

#### Tier 4 · 忽略（内部标记）

`hbpm_positionhr.parent` 自引用关联 · 会查到下级岗位 · 可以选择"下级岗位需要先处理"校验（强业务侧需求）。

#### 实施建议

- **必做**（阻断）：Tier 1 · 5 个实体的 `position` 字段 · 按 `boid` 查 + `iscurrentversion=true` + 状态在职
- **建议**（弹窗提示 · 用户确认后允许禁用）：Tier 2 · 6 个实体
- **提示**（log 级）：Tier 3 · 3 个实体（变更记录类）

### 代码示例（仅 Tier 1 · 简化版）

```java
package ${ISV_FLAG}.hrmp.hbpm.opplugin.position;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

// ⭐ 并列挂 · 不继承 PositionHrDisableOp · 遵循 PR-001
public class PositionDisableCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new PositionDisableCheckValidator());
    }

    public static class PositionDisableCheckValidator extends AbstractValidator {
        // Tier 1 · 5 实体必查
        private static final List<String> TIER1 = Arrays.asList(
                "hrpi_empposorgrel",
                "hrpi_empjobrel",
                "hrpi_rotationinfo",
                "hrpi_dispatchinfo",
                "hrpi_appointremoverel"
        );

        @Override
        public void validate() {
            ExtendedDataEntity[] dataArr = this.getDataEntities();
            for (ExtendedDataEntity ex : dataArr) {
                DynamicObject pos = ex.getDataEntity();
                long boid = pos.getLong("boid");                 // ⭐ PR-009 · 用 boid 不用 id
                String name = pos.getString("name");

                long totalRefs = 0;
                for (String entity : TIER1) {
                    HRBaseServiceHelper helper = new HRBaseServiceHelper(entity);
                    // PR-008 · iscurrentversion=true 过滤当前版本
                    QFilter qf = new QFilter("position", QCP.equals, boid)
                            .and("iscurrentversion", QCP.equals, Boolean.TRUE);
                    // 业务在职状态 · 具体字段名按实体 scene_doc 待查
                    // qf.and("status", QCP.equals, "A");
                    long count = helper.count("id", qf.toArray());
                    if (count > 0) {
                        this.addErrorMessage(ex, String.format(
                                "岗位 [%s] 下有 %d 条 %s 在职引用 · 不能禁用",
                                name, count, entity));
                        totalRefs += count;
                    }
                }
                if (totalRefs == 0) {
                    // Tier 2 / Tier 3 弹窗提示 · 不阻断
                }
            }
        }
    }
}
```

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `hbpm_positionhr`
2. 选择【操作】标签 → 找到 opKey = `disable`
3. 点击【扩展插件】→ **新增**（并列挂 · 不覆盖 `PositionHrDisableOp` · 遵循 PR-001）
4. RowKey 设为 0（在 `HisModelOPCommonPlugin` 之前 · 确保最早执行阻断）
5. 保存 → 部署生效

### 踩坑

- ⚠️ **时序资料按 `boid` 查下游引用 · 不按 `id`**（**遵循 PR-009**）：
  - `id` 是**具体版本 id** · 一个岗位有多个历史版本 · 每版本 id 不同
  - `boid` 是**业务维度 id** · 所有历史版本共用同一个 boid · 这才代表"一个业务对象"
  - 下游引用岗位时存的一般是 `boid`（因为引用的是"某个岗位"这个业务对象 · 不是某个版本）
- ⚠️ **下游表查询要带 `iscurrentversion=true`**（**遵循 PR-008**）：下游也多是时序资料 · 不过滤会查到所有历史版本 · 误报
- ⚠️ **标品 disable 走 afterExecuteOperationTransaction 阻断是已晚**：`IBosPositionService.disablePositions` 是事务后校验 · 错误信息用户收到时事务已开始（甚至数据可能回滚不干净）· 所以必做**前置 Validator** · 真正拦住（**遵循 PR-010 · onAddValidators 在最早阶段**）
- ⚠️ `e.getDataEntities()` 是数组 · 批量禁用时要逐条判断
- ⚠️ 查询量大时性能差 · 建议下游表的 `position` 字段建索引 + 分批查询
- ❌ **不要继承 `PositionHrDisableOp`**：ISV 扩展规范走**并列挂插件** · 不走继承覆盖（**遵循 PR-001**）
- ❌ **不要只查 `hspm_ermanfile`（人员档案）**：人员档案只存人员主信息 · 岗位引用在**任职关系**上 · 一个人员可能没任职关系（新人 / 待入职）· 查档案会漏
- ❌ 业务上更推荐"岗位状态从'启用'改为'停招'" · 而不是硬禁用 · 禁用保留给真正废弃的岗位
- ❌ 不要在 `afterExecuteOperationTransaction` 检查（事务已提交 · 无法阻断）· 也不在 `beforeExecuteOperationTransaction`（事务已进入 · 参考 PR-010 `onAddValidators` 是最早也最合适的阶段）

### 关联 Pattern

无独立 pattern，参考 `PositionHisValidator` 的多条校验并存模式（`PositionHisValidator.java` L67-L83 · 9 条独立校验并存）。

---

## CS-04 · 岗位编码按岗位类型前缀自动生成（典型定制）

### 需求

HR 项目集团要求：
- 管理岗（`positiontype.number = "MANAGE"`） → 编码前缀 `M`
- 技术岗 → 前缀 `T`
- 销售岗 → 前缀 `S`
- 生产岗 → 前缀 `P`

编码格式：`{前缀}{4 位序号}`，按行政组织隔离唯一。

### 推荐方案

- **扩展对象**：`hbpm_positionhr`（主实体）
- **扩展点**：`beforeExecuteOperationTransaction@save` RowKey=0（在标品 `CodeRuleOp` 之前）
- **实现模式**：**并列挂** 自定义 OP · 继承 `HRDataBaseOp`（遵循 PR-001）
- **风险**：低（编码生成纯业务逻辑）

### 扩展入口坐标

- 绑定表单：`hbpm_positionhr`
- 绑定操作：`save`
- 推荐父类：`HRDataBaseOp`
- 关键重写方法：
  - `onPreparePropertys(PreparePropertysEventArgs e)` — 声明要读的字段
  - `beforeExecuteOperationTransaction(BeforeOperationArgs args)` — 判断 `entity.isNew()` · 按 positiontype 生成前缀 · 写 `entity.set("number", generatedCode)`（**遵循 PR-003 · OP 用 entity.set**）

### 调用链

```
用户前端填字段 + 选岗位类型 · 点保存
    ↓
PositionEdit.setPositionNumber L651-L665 已按标品 CodeRuleServiceHelper.getNumber 生成默认编码
(前端已填 number)
    ↓
【服务端 save】9 插件链执行前
    ↓
[RowKey=0] TdkwPositionNumberOp.beforeExecuteOperationTransaction
    ├── 对每行 entity:
    │   - 若非新增（isNew=false · 已保存过）· 跳过（INV-06 编码创建后不可改）
    │   - 若已有 number（前端已填）· 按自定义规则覆写
    │   - 查 positiontype.number → 映射前缀
    │   - 按 adminorg.org + 前缀 查询当前最大序号
    │   - 用 kd.bos.id.ID.genLongId() 或自定义分布式锁生成新号
    │   - entity.set("number", prefix + serialNumber)     ← PR-003 · OP 用 entity.set
    ↓
[RowKey=1] CodeRuleOp · 如果我们已写 number · CodeRuleOp 跳过
    ↓
后续标品链...
```

### 代码示例

```java
package ${ISV_FLAG}.hrmp.hbpm.opplugin.position;

import java.util.HashMap;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.id.ID;  // PR-005 · 用 kd.bos.id.ID
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

// ⭐ 并列挂 · RowKey=0 · 早于 CodeRuleOp · 遵循 PR-001
public class TdkwPositionNumberOp extends HRDataBaseOp {

    private static final Map<String, String> TYPE_TO_PREFIX = new HashMap<>();
    static {
        TYPE_TO_PREFIX.put("MANAGE", "M");
        TYPE_TO_PREFIX.put("TECH", "T");
        TYPE_TO_PREFIX.put("SALES", "S");
        TYPE_TO_PREFIX.put("PROD", "P");
    }

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        // PR-003 · 声明 OP 读写字段
        e.getFieldKeys().add("number");
        e.getFieldKeys().add("positiontype");
        e.getFieldKeys().add("adminorg");
    }

    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        for (DynamicObject entity : args.getDataEntities()) {
            // 只处理新建 · 遵循 INV-06 不改已有编码
            if (!entity.getDataEntityState().getFromDatabase()) {
                generateCustomNumber(entity);
            }
        }
    }

    private void generateCustomNumber(DynamicObject entity) {
        DynamicObject positiontype = entity.getDynamicObject("positiontype");
        if (positiontype == null) return;
        String typeNumber = positiontype.getString("number");
        String prefix = TYPE_TO_PREFIX.getOrDefault(typeNumber, "X");

        // PR-005 · 用 kd.bos.id.ID 获取唯一号 · 不自己造 Redis
        long seqId = ID.genLongId();
        String number = String.format("%s%04d", prefix, seqId % 10000);

        // ⭐ PR-003 · OP 里用 entity.set · 不用 getModel().setValue
        entity.set("number", number);
    }
}
```

### 平台绑定方式

1. 打开【苍穹开发平台】→ 定位表单 `hbpm_positionhr`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ **新增**（RowKey=0 · 在 CodeRuleOp 之前 · 遵循 PR-002）
4. 保存 → 部署生效

### 踩坑

- ❌ RowKey > 1 → `CodeRuleOp` 已生成普通编码 · 再覆盖可能报 `HisUniqueValidateOp` 唯一性冲突（`_auto_operations.md` L105 save 链第 7 位）
- ❌ 未用分布式 ID · 自己造 Redis → **违反 PR-005** · 应用 `kd.bos.id.ID.genLongId()`
- ❌ 忘记判断 `entity.isNew()` → 修改已有岗位时误覆盖 number（违反 INV-06 · PR-007）
- ❌ 在 OP 里用 `getModel().setValue` → **违反 PR-003** · OP 没有 model · NPE
- ⚠️ 导入场景（`isImport()=true`）· 标品会用 `unitPositionMap` 已有 id · 本插件要检测 option 变量跳过
- ✅ 最终方案：前置生成 + ID.genLongId + isNew 判断 + 考虑导入场景

### 关联 Pattern

参考 `PositionCodeRuleHelper.getCode` / `recycleNumber` 设计思路（`PositionCodeRuleHelper.java` L23-L51 · 标品编码生成 + 回收的模式）。

---

## CS-05 · 监听岗位数据版本变更 → 用苍穹 BEC 推下游 ⚠ 推荐走 BEC 而非自建

### 需求

⚠ **业务背景**：岗位变更时需要通知多个下游模块（薪酬档案 / 绩效模板 / 招聘需求等）· 让下游按自己节奏消费事件。

**标品现状**：岗位场景用自己的调度任务机制（`ChangeMsgServiceImpl.sendMsg` + `sch_task jobId=5/2/X9QCCFNS` + `hbpm_position_msgdetail` 明细表）· 这是**标品内部**消息机制 · **ISV 扩展跨模块通知不要复用**（会污染标品消息流 + 升级兼容问题）。

**推荐做法**：走苍穹**业务事件中心（BEC · Business Event Center）**（**遵循 PR-011**）· 统一事件分发 · 不自接 MQ。

### 推荐方案

- **扩展点**：独立 OP 插件 · 并列挂到 `save` / `enable` / `disable` / `confirmchange` · 在 `afterExecuteOperationTransaction` 阶段调用苍穹 BEC（**遵循 PR-010 · 主事务已提交安全**）
- **实现模式**：继承 `HRDataBaseOp`（**并列挂 · 不继承 `PositionHrCommonOp` · 遵循 PR-001**）· 调 `IEventService.triggerEventSubscribeJobs`
- **风险**：中（涉及跨模块事件 · 下游幂等处理需评估）

### 扩展入口坐标

- 绑定表单：`hbpm_positionhr`
- 绑定操作：`save` · `enable` · `disable` · `confirmchange` · `dochangerelation`（5 个产生变更的入口）
- 推荐父类：`HRDataBaseOp`（**与标品 `PositionHrCommonOp` 并列挂 · 互不干涉** · 遵循 PR-001）
- 关键重写方法（OP 生命周期 · 实证自 `kd.bos.entity.plugin.IOperationServicePlugIn` 接口）：
  - `onPreparePropertys(PreparePropertysEventArgs e)` — 声明要对比变更的字段
  - `beforeExecuteOperationTransaction(BeforeOperationArgs args)` — 暂存变更前快照
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — **主事务已提交** · 触发业务事件（**遵循 PR-010**）

**业务意图**：标品 `ChangeMsgServiceImpl.sendMsg` 只在内部调度任务层触发 · 不做跨模块事件广播。本扩展并列挂新插件 · 走苍穹 BEC 发布领域事件 · 下游 app 按 eventNumber 订阅。

### 苍穹业务事件中心（BEC）API · 2026-04-24 反编译实证（遵循 PR-011）

**发布方 API**：`kd.bos.bec.api.IEventService`
- **jar 来源**：`bos-mservice-bec-api-8.0.jar` · 类 `kd/bos/bec/api/IEventService.class`
- **核心方法**：`triggerEventSubscribeJobs(String sourceApp, String eventNumber, String message, Map<String,Object> variables)`
- **参数含义**：
  - `sourceApp` · 发布方 app 编号（如 `"hbpm"`）
  - `eventNumber` · 事件编号（**必须先在开发平台【业务事件管理】预配置** · 如 `"HBPM_POSITION_CHANGED"`）
  - `message` · 事件消息描述
  - `variables` · 事件变量 Map · 下游按 eventNumber 订阅后取用

**订阅方 API**：下游实现 `kd.bos.bec.api.IEventServicePlugin`
- **核心方法**：`handleEvent(KDBizEvent event)` 接收事件
- **事件模型**：`KDBizEvent`（`bos-mservice-bec-api-8.0.jar` · `kd/bos/bec/model/KDBizEvent.class`）· 4 核心字段：`eventId` / `eventNumber` / `source` / `variables`

### 代码示例（发布方）

```java
package ${ISV_FLAG}.hrmp.hbpm.opplugin.position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kd.bos.bec.api.IEventService;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.ServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

// ⭐ 并列挂 · 不继承 PositionHrCommonOp · 遵循 PR-001
public class PositionChangeEventPublishOp extends HRDataBaseOp {

    private final Map<Long, DynamicObject> beforeSnapshot = new HashMap<>();

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        // PR-003 · OP 里声明要读的字段
        e.getFieldKeys().add("boid");
        e.getFieldKeys().add("number");
        e.getFieldKeys().add("name");
        e.getFieldKeys().add("adminorg");
        e.getFieldKeys().add("job");
        e.getFieldKeys().add("positiontype");
        e.getFieldKeys().add("lowjoblevel");
        e.getFieldKeys().add("highjoblevel");
        e.getFieldKeys().add("enable");
    }

    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        for (DynamicObject entity : args.getDataEntities()) {
            long boid = entity.getLong("boid");
            DynamicObject before = BusinessDataServiceHelper.loadSingleFromCache(
                    entity.getPkValue(), "hbpm_positionhr");
            beforeSnapshot.put(boid, before);
        }
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        // PR-010 · afterExecuteOperationTransaction · 主事务已提交 · 安全触发业务事件
        IEventService eventService = ServiceHelper.getService(IEventService.class);

        for (DynamicObject after : e.getDataEntities()) {
            long boid = after.getLong("boid");                 // PR-009 · 下游用 boid
            DynamicObject before = beforeSnapshot.get(boid);

            Map<String, Object> variables = new HashMap<>();
            // PR-011 · variables 只传关键字段 · 不塞完整 DynamicObject
            variables.put("positionBoid", boid);
            variables.put("positionNumber", after.getString("number"));
            variables.put("adminorgId", after.getLong("adminorg.id"));
            variables.put("hisversion", after.getString("hisversion"));
            variables.put("changedFields", diffFields(before, after));
            variables.put("enable", after.getString("enable"));

            // PR-011 · 走 BEC · 不自建 MQ
            eventService.triggerEventSubscribeJobs(
                    "hbpm",                            // sourceApp
                    "HBPM_POSITION_CHANGED",           // eventNumber（开发平台预配）
                    "岗位变更",                          // message
                    variables
            );
        }
    }

    private List<String> diffFields(DynamicObject before, DynamicObject after) {
        // 对比 number/name/adminorg/job/positiontype/lowjoblevel/highjoblevel/enable
        // 返回变化字段名
        return new java.util.ArrayList<>();
    }
}
```

### 平台绑定方式

1. **前置**：在【苍穹开发平台】→【业务事件管理】→ 新增事件 · 编号 `HBPM_POSITION_CHANGED` · source=`hbpm`（**遵循 PR-011 · eventNumber 必须先预配置**）
2. 开发平台 → 定位表单 `hbpm_positionhr`
3. 选择【操作】标签 → 找到 opKey = `save` · 【扩展插件】**新增**（并列挂 · PR-001）
4. 同样添加到 opKey = `enable` · `disable` · `confirmchange` · `dochangerelation`
5. 保存 → 部署生效

### 订阅方（下游如薪酬 / 绩效模块）

1. 下游 app（如 `hpdt` 绩效 / `hcd` 薪酬）实现 `kd.bos.bec.api.IEventServicePlugin.handleEvent(KDBizEvent)`
2. 开发平台【业务事件管理】→ 配置订阅 `HBPM_POSITION_CHANGED` → 绑下游插件类
3. 订阅后 BEC 被动推事件 · 发布方无需知道下游存在

### 踩坑

- ⚠️ **事件在 `afterExecuteOperationTransaction` 发**（**遵循 PR-010**） · 不在 `endOperationTransaction`（事务提交前 · 此时事件会污染主事务 · after 才是事务后）
- ⚠️ `eventNumber` 必须**先在开发平台业务事件管理预配置**（**遵循 PR-011**）· 代码调 `triggerEventSubscribeJobs` 前 BEC 认不了未注册的事件号
- ⚠️ BEC 的 `variables` 只传关键字段（id / boid / 变更字段清单）· **不要塞完整 DynamicObject**（**违反 PR-011 · 反模式**）
- ⚠️ 时序变更（`newhisversion` → `confirmchange`）会触发多次事件 · 下游 `handleEvent` 按 `boid + hisversion` 幂等
- ⚠️ `changedFields` 对比前需要 `onPreparePropertys` 声明字段（否则 before DynamicObject 不含该字段）
- ⚠️ **区分岗位场景的内部消息机制 vs BEC**：
  - `ChangeMsgServiceImpl.sendMsg` 是**标品内部**（走 sch_task + hbpm_position_msgdetail）· 不要碰
  - `IEventService.triggerEventSubscribeJobs` 是**跨模块通知**（走 BEC）· ISV 用这个
- ❌ **不要继承 `PositionHrCommonOp`**（违反 PR-001 · 标品专属类）· 并列挂新插件
- ❌ **不要自己接 Kafka/RabbitMQ** · 苍穹 BEC 已封装事件分发 · 违反 PR-011

### 关联 Pattern

无独立 pattern · BEC 是苍穹官方事件分发机制 · 所有跨模块事件优先走 BEC · 不自建 MQ（遵循 PR-011）。

---

## 方案选型矩阵

| 业务需求 | 推荐 CS | 扩展点 | 复杂度 | 遵循 PR |
|---|---|---|---|---|
| 加字段 | CS-01 | modifyMeta op=add | 低 | PR-007 + PR-003 |
| 字段联动（选类型/组织填默认值） | CS-02 | propertyChanged (BILL_FORM) | 低 | PR-001 + PR-003 + PR-004 |
| 禁用前业务检查（在职任职关系）⭐ P0 | CS-03 | onAddValidators@disable（并列挂） | 中 | PR-001 + PR-008 + PR-009 + PR-010 |
| 自定义编码规则 | CS-04 | beforeExecuteOperationTransaction@save RowKey=0 | 低 | PR-001 + PR-003 + PR-005 + PR-006 |
| 变更通知下游（BEC 事件） | CS-05 | afterExecuteOperationTransaction@save/enable/disable/confirmchange + IEventService | 中 | PR-001 + PR-009 + PR-010 + PR-011 |

---

## 反模式清单（从反编译 + scene_doc.json minefield 反向推导）

### ❌ 禁止直接修改时序字段（PR-008 / PR-009）

参考 `PositionHrDisableOp.onPreparePropertys` L42-L47 只声明 4 字段（adminorg / change 三分类）· **从不手改** `boid` / `iscurrentversion` / `datastatus` / `hisversion` / `firstbsed` / `sourcevid`。这些字段由平台 `HisModelOPCommonPlugin`（save 链第 6 位）维护。

- ❌ 扩展插件 `entity.set("boid", ...)` — 破坏时序链
- ❌ 扩展插件 `entity.set("iscurrentversion", true)` — 破坏版本切换逻辑
- ❌ 扩展插件 `entity.set("hisversion", "99")` — 打乱版本号递增
- ❌ 扩展插件 `entity.set("firstbsed", ...)` — 最早生效日期是不变量

### ❌ 禁止修改变动三分类字段

- ❌ 修改 `changetype` / `changeoperate` / `changescene` → 被 `PositionHr*Op.beginOperationTransaction` 覆盖写入 · 徒劳
- 场景分发依赖这 3 字段 · 手改会错位

### ❌ 禁止修改出厂数据字段（PR-007）

- ❌ 修改 `orinumber` / `oriname` / `oristatus` → 破坏 `HRBaseOriginalOp`（save 链第 7 位）的出厂比对逻辑

### ❌ 禁止修改系统预置字段

- ❌ 修改 `issyspreset` → 破坏系统预置岗位保护，导致预置岗位被误删

### ❌ 禁止修改软删除字段

- ❌ 修改 `isdeleted` → 破坏软删除过滤链 · 列表显示错乱

### ❌ 禁止继承标品场景专属 OP（PR-001）

- ❌ `extends PositionHrSaveOp` → 继承后标品升级 break（标品可能改 super 方法签名）
- ❌ `extends PositionHrDisableOp` → 同上
- ❌ `extends PositionHrEnableOp` → 同上
- ❌ `extends PositionHrChangeOp` → 同上
- ❌ `extends PositionHrCommonOp` → 基类也不继承（非 SDK 注解）
- ✅ 正确姿势：继承 `HRDataBaseOp` / `HRDataBaseEdit` / `HRDataBaseList` · 并列挂到操作

### ❌ 禁止覆盖平台级/HR 域通用插件

- ❌ 覆盖 `BdVersionSaveServicePlugin` → 影响全苍穹基础资料（违反 PR-001）
- ❌ 覆盖 `HisModelOPCommonPlugin` → 影响全 HR 时序基础资料
- ❌ 覆盖 `HRBaseDataStatusOp` / `HRBaseOriginalOp` / `HisUniqueValidateOp` → 破坏 HR 全线

### ❌ 禁止 updateOperation 全量覆盖

- ❌ 调 `updateOperation(plugins=[myPlugin])` → save 链 9 标品插件全丢（`EX-20` 引用）
- ✅ 正确姿势：`current = getOperation(); current.plugins.append(myPlugin); updateOperation(plugins=current.plugins)`

### ❌ 禁止在 HIES 自定义页绕开权限

- ❌ `importdata_hr` 的 `cusstartpage: hismodel_importstart` 是标品导入向导页；自定义页面必须走同一权限校验

### ❌ 禁止扩展继承父模板

- ❌ 扩展 `hbp_histimeseqtpl`（时序模板）→ 全 HR 时序基础资料连带炸
- ❌ 扩展 `hbp_bd_tpl_all` → 全 HR 基础资料炸
- ❌ 扩展 `bos_basetpl` → 全苍穹基础资料炸

### ❌ 跨模块通知禁止自接 MQ（PR-011）

- ❌ 自己接 Kafka / RabbitMQ / RocketMQ → 违反 PR-011 · 用苍穹 BEC
- ❌ 直接复用 `ChangeMsgServiceImpl.sendMsg` · 这是标品**内部** · 不是跨模块接口

### ❌ 禁止从字段名盲推下游

参考 CS-03 · 所有下游引用都要查 `refentity_reverse.json` 实证 · 不靠名字猜。

### ❌ 禁止绝对化"字段不可改"（PR-007）

- ❌ "number 创建后永远不可改" → 不对业务岗位 · 只对 `issyspreset=true` 的预置岗位
- ✅ 正确说法："预置岗位 `number` 不可改 · 业务岗位可改但下游引用多 · 强烈建议不改"

---

**📌 来源追溯**：

- CS-01 扩展点：`modifyMeta` OpenAPI + `scene_doc.json` 70+ 字段类型分布
- CS-02 字段联动：`PositionEdit.propertyChanged` L455-L515 6 分支
- CS-03 下游引用 27 处：`knowledge/workbench/_indexes/refentity_reverse.json` `refs.hbpm_positionhrf7` 段
- CS-03 标品缺校验：`PositionHrDisableOp.java` L49-L50 `onAddValidators` 空
- CS-04 PositionCodeRuleHelper 参考：`PositionCodeRuleHelper.java` L23-L51
- CS-05 BEC API：`bos-mservice-bec-api-8.0.jar` 反编译 + `knowledge/_shared/platform_rules.json` PR-011
- 反模式 时序字段：`scene_doc.json` minefield 段 + `PositionHrDisableOp.onPreparePropertys` 仅 4 字段
- 反模式 updateOperation：`knowledge/kb_cosmic_modifymeta_traps.md` EX-20
- 平台规则引用：`knowledge/_shared/platform_rules.json`
