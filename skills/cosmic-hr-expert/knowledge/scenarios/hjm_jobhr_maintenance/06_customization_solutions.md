# 推荐定制方案 · 职位体系维护 (hbjm_jobhr)

> **状态**: 🟢 基于真实 OpenAPI 实抓 + 5 类反编译类名 + `scene_doc.json` 64 字段语义整合
> **confidence**: real_deploy（所有扩展点类名均来自 `_auto_plugin_semantics.md` 实证）

所有方案遵循统一结构（借鉴 Stripe / Salesforce Developer Docs）：
**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 Pattern**

---

## CS-01 · 给 hbjm_jobhr 扩展自定义字段（最高频）

**关联 Pattern**：[Pattern A · add_field_extension](../../pattern/add_field_extension/README.md)

### 需求

业务方说：职位需要加"职位状态"字段（如"招聘中 / 冻结 / 储备 / 已关闭"），用于招聘数据看板。

### 推荐方案

- **扩展对象**：`hbjm_jobhr`（主实体）
- **扩展点**：`modifyMeta(op=add, elementType=field)`
- **风险**：低
- **特点**：职位域**没有 4 前缀分录**（区别于行政组织），单次 modifyMeta 即可搞定主表

### 调用链（3 步）

```
Step 1: getBizApps()                        // 拿 bizAppId = 217YZTTABIJI (hjm 应用)
Step 2: modifyMeta({
  formId: "hbjm_jobhr",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hbjm_jobhr",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_recruitstatus",
      name: {zh_CN: "招聘状态", en_US: "Recruit Status"},
      // fieldName 平台会自动生成：在 key 前加 `f` → ftdkw_recruitstatus
      // ⚠ 如手动指定 · 不要再加 fk_ 前缀（会生成 ffk_tdkw_recruitstatus 怪列名 · 坑 11）
      mustInput: false,
      comboOptions: [
        {value: "A", name: {zh_CN: "招聘中", en_US: "Recruiting"}},
        {value: "B", name: {zh_CN: "冻结",   en_US: "Frozen"}},
        {value: "C", name: {zh_CN: "储备",   en_US: "Reserved"}},
        {value: "D", name: {zh_CN: "已关闭", en_US: "Closed"}}
      ]
    }
  }]
})
Step 3: getFormSchema("hbjm_jobhr")         // ⭐ 二次验证落库（errorCode=0 不代表成功）
```

### 代码框架（使用 cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "hbjm_jobhr", "number": "hbjm_jobhr"}
)

designer.add_field(
    field_type="ComboField",
    name="招聘状态",
    key="${ISV_FLAG}_recruitstatus",
    parent_entity_id=designer.base_entity_id,
    combo_options=[
        {"value": "A", "name": {"zh_CN": "招聘中"}},
        # ...
    ]
)
designer.save()  # 一次 save click
```

### 踩坑

- ❌ 误扩展到 `_l` 后缀表 → 苍穹多语言表规范以 `_l` 结尾（如 `t_hbjm_job_l`）· 普通字段不应放多语言表。**hbjm_jobhr 当前标品没独立 `_l` 表** · `name` 多语言在主表承载是**标品配置** · 不是"MuliLangTextField 默认行为"（参考坑 11）
- ⚠️ `t_hbjm_job_i` 是**拆分表**（苍穹对宽表的垂直分表 · 性能优化）· 不是多语言表 · 扩展字段默认落主表 `t_hbjm_job` · 不用手动指定 `_i`（参考坑 11）
- ❌ 字段 key 不带 ISV 前缀（如直接叫 `recruitstatus`）→ 标品升级覆盖
- ❌ `fieldName` 列名超过 25 字符 → 苍穹平台开发规范限制 · 数据库建表失败
- ❌ `fieldName` 手动写 `fk_` 前缀 → 平台会再加 `f` → 列名变 `ffk_xxx` 怪列名 · **建议不传 fieldName 让平台按 `f + key.lowercase()` 自动生成**（坑 11）
- ❌ ComboField 漏传 `comboOptions` → UI 下拉框空白（`EX-12` 引用）
- ❌ 想引用"废弃字段" `depcytype` → scene_doc.json L566 已明确标注"(废弃)"

**遵循的 PR 规范**：
- PR-007（预置数据不可改 · 业务数据可改）· 本 CS 加的是业务字段 · 可随时删改
- 坑 11（多语言表 `_l` vs 拆分表 `_i`）
- **PR-005**（序号/ID 用 `kd.bos.id.ID` 接口）· 见下方"补充：行 id 生成规范"

### 补充：分录子表行 id / 业务流水号生成规范（PR-005）

如果本场景未来扩展涉及**自建分录子表**（如给职位加"职位变更日志"分录）· 或要写"职位编码后缀"自动生成 · 行 id / 流水号必须用 `kd.bos.id.ID`：

```java
import kd.bos.id.ID;

// 场景 1：扩展时新增分录行 · 填行 id（必须实证 · 不能自己造 UUID）
DynamicObject newRow = entries.addNew();
newRow.set("id", ID.genLongId());                // 长整型主键 · 19 位 Snowflake
newRow.set("changedesc", "职级调整");

// 场景 2：自定义业务编码后缀 / 流水号
String traceId = ID.genStringId();               // 字符串 ID · 用于 traceId / 日志追踪号
String customNumber = "JOBHR_" + ID.genStringId();
```

**遵循 PR-005 · 苍穹平台已集成分布式 ID（Snowflake）· ISV 不要自己 Redis incr 或 UUID 造**

⚠ **反模式**（违反 PR-005 必驳回）：
- ❌ `UUID.randomUUID().toString()` · 苍穹有更轻量的 ID 接口
- ❌ `System.currentTimeMillis()` · 高并发会撞
- ❌ 自己 select max(id) + 1 · 分布式集群必坏

### 验证

保存后到**职位菜单**（menuId: 1305038595106658304）新增一条职位，看表单上是否出现"招聘状态"字段。

---

## CS-02 · 根据职级方案/职等方案自动带出职级职等范围 ⭐ 职位域特有

### 需求

业务方说：新增职位时 · 选择了"**职级方案**"和"**职等方案**"后 · 系统应根据方案下挂的职级/职等清单（按 `顺序码` 排序）· 自动带出最低/最高职级、最低/最高职等到 `lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade` 4 字段 · 避免人工填错。

**业务背景**（用户确认）：
- 职级范围来源：**职级方案 `hbjm_joblevelscmhr`** 下挂的所有职级（`hbjm_joblevelhr`）· 按 `顺序码` 取最低/最高
- 职等范围来源：**职等方案 `hbjm_jobgradescmhr`** 下挂的所有职等（`hbjm_jobgradehr`）· 按 `顺序码` 取最低/最高
- **不是**从职位序列（`jobseq`）直接带出 —— 职位序列/族/类只是分类维度 · 不直接关联职级范围
- 职位序列/族/类/职位都是**历史版本控制（HisModel）**· 查询时带 `iscurrentversion=true` 过滤到当前版本

### 推荐方案

- **扩展点**：`propertyChanged@hbjm_jobhr` 监听职级方案/职等方案字段变更
- **实现模式**：前端 `AbstractFormPlugin` + 注册为 `BILL_FORM`
- **风险**：低（前端只读 + 预填，不动后端数据）

### 扩展入口坐标

- 绑定表单：`hbjm_jobhr`
- 推荐父类：`HRDataBaseEdit`（反编译实证：`JobHisBasedataFiledChangeEdit` 类继承它 · `_auto_plugin_semantics.md` L29）· **并列挂新插件 · 不继承 `JobHisBasedataFiledChangeEdit` 等标品场景类**（PR-001）
- 关键重写方法：
  - `propertyChanged(PropertyChangedArgs args)` — 监听**职级方案字段** + **职等方案字段**变更（PR-003 · FormPlugin 用 getModel().setValue）
  - 用 `HRBaseServiceHelper("hbjm_joblevelhr")` + QFilter(方案id, iscurrentversion) 查职级清单（**PR-008** · 时序资料必带 iscurrentversion=true）
  - 按 `顺序码` 排序 · 取最低/最高
  - `getModel().setValue("lowjoblevel", ...)` / `setValue("highjoblevel", ...)` 回填 · **用 beginInit/endInit 防死循环**（PR-004）· 职等同理

**业务意图**（用户确认）：职级、职等分别挂在**方案**（`hbjm_joblevelscmhr` / `hbjm_jobgradescmhr`）上 · 方案下的职级/职等有**顺序码**字段用于排序。用户选方案后 · 插件查该方案下所有当前版本职级/职等 · 按顺序码取最小/最大作为范围默认值。

### 调用链

```
用户前端选择"职级方案" = "技术族职级方案 V2"
    ↓
触发 propertyChanged · propertyKey = 职级方案字段 · newValue = 方案 id
    ↓
插件按 QFilter("方案id", "=", newValue).and("iscurrentversion", "=", true)
    查 hbjm_joblevelhr 全部挂在该方案下的职级清单
    ↓
按 顺序码 排序 · 取最低职级 minLevel / 最高职级 maxLevel
    ↓
getModel().beginInit();
getModel().setValue("lowjoblevel", minLevel.id);
getModel().setValue("highjoblevel", maxLevel.id);
getModel().endInit();
getView().updateView("lowjoblevel");
getView().updateView("highjoblevel");
    ↓
职等方案 → hbjm_jobgradehr 同样逻辑 · 填 lowjobgrade / highjobgrade
```

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `hbjm_jobhr`
2. 选择【注册插件】→ 新增插件（`targetType = BILL_FORM`）
3. 填写 `className` 为你的全限定名
4. 保存 → 部署生效

### 踩坑

- ⚠️ `lowjoblevel` / `highjoblevel` 和对应的 `lowjoblevelname` / `highjoblevelname` 冗余字段**必须同步更新**（`scene_doc.json` L730-L771 · 共 4 个 name 字段）· 否则列表显示和 F7 选择不一致
- ⚠️ **时序基础资料查询必须带 `iscurrentversion=true`**：职位序列/族/类（`hbjm_jobseqhr` / `hbjm_jobfamilyhr` / `hbjm_jobclasshr`）+ 职位（`hbjm_jobhr`）都是 `HisModel` 历史版本控制 · 不过滤会读到多个历史版本的相同 id
- ⚠️ 方案下职级/职等的**顺序码**是排序依据 · 不要按 id 或 number 字段排序（用户可能用文本编码如 "P3 / P5 / P7"· 字典序 ≠ 业务序）
- ⚠️ **`hbjm_joblevelhr` / `hbjm_jobgradehr` 里"关联方案"的字段名** · 需查 `knowledge/_shared/_standard_metadata/entity_metadata/hbjm_joblevelhr.md` 和 `hbjm_jobgradehr.md` 实证（可能是 `joblevelscm` / `jobgradescm` 等 · **按实际元数据为准 · 不脑补**）
- ❌ 不要在 `propertyChanged` 里写 DB update（应走 save 链 · 参考 CS-03 的 Validator 模式）
- ❌ **不要从职位序列（jobseq）直接带出职级范围**：序列 / 族 / 类只是分类维度 · 跟职级方案是两个独立配置
- ❌ **不要继承 `JobHisBasedataFiledChangeEdit`**（PR-001 · 标品场景专属类）· 继承抽象基类 `HRDataBaseEdit` 即可

**遵循的 PR 规范**：PR-001（并列挂不继承）· PR-003（FormPlugin 用 getModel().setValue）· PR-004（beginInit/endInit 防死循环）· PR-008（iscurrentversion 过滤）· PR-009（boid 业务维度）

### 关联 Pattern

前端字段联动是 HR 场景常见需求，暂无独立 pattern。参考 `_auto_plugin_semantics.md` 中 `JobHisBasedataFiledChangeEdit` 的 `afterBindData` / `beforeDoOperation` / `afterDoOperation` 写法。

---

## CS-03 · 职级/职等区间校验（防 UI 旁路） ⭐ 职位域特有

### 需求

业务方说：**批量导入** / **OpenAPI 写入** / **变更确认**时可能出现"最低职级 = P5 · 最高职级 = P3"区间填反的情况 · 要在服务端强制校验拦截。

**⚠ 重要前提**：**页面手工录入**时 · 前端控件已拦截（用户选不到比最低职级低的最高职级）· CS-03 是**防 UI 旁路写入** · 不是补 UI 校验。

### 推荐方案

- **扩展点**：`onAddValidators@save` + `onAddValidators@confirmchange` + `importdata_hr` 导入链
- **实现模式**：继承 `AbstractValidator` + 在 `JobHrSaveOp.onAddValidators` 并列注册
- **风险**：低（纯校验 · 不改数据）

### 扩展入口坐标

- 绑定表单：`hbjm_jobhr`
- **绑定操作**（用户确认的 2 + 1 套）：
  - `save` · 走 save 链的所有写入入口（Java 服务层 / OpenAPI）
  - `confirmchange` · 变更确认（改版本时）
  - `importdata_hr` · **HIES-Pro 批量导入**（⚠ 实现方式待定 · 见下方"待补"）
- 推荐父类：`HRDataBaseOp`（HR 通用 OP 抽象基类 · SDK 白名单合规）· **并列挂新插件 · 不继承 `JobHrSaveOp` 等标品场景类**（PR-001）· RowKey 建议 = 0（排在 `HisModelOPCommonPlugin` 之前最早阻断 · PR-002）
- 关键重写方法：
  - `onAddValidators(AddValidatorsEventArgs args)` — `super.onAddValidators(args); args.addValidator(new JobRangeValidator());`（super 保父类链 · 本插件独立追加 Validator · PR-010 最早阶段阻断）
  - 在自定义 `JobRangeValidator extends AbstractValidator` 的 `validate()` 里：
    - 对每行 `ExtendedDataEntity` · 读 `lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade`（4 个 BasedataField）
    - 查 `hbjm_joblevelhr` · 按 **`joblevelseq`（职级顺序码）**比较高低
    - 查 `hbjm_jobgradehr` · 按 **`jobgradeseq`（职等顺序码）**比较高低
    - 若 low.顺序码 > high.顺序码 · `addErrorMessage(entity, "...")`
    - ⚠ **查职级/职等时必须带 `iscurrentversion=true`**（PR-008 · HisModel 时序资料）

### 代码示例

```java
package ${ISV_FLAG}.hrmp.hbjm.opplugin.job;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * 职级/职等区间校验 OP 插件
 * 并列挂到 save / confirmchange opKey（PR-001 · 不继承 JobHrSaveOp）
 * RowKey 建议 = 0（最早阶段阻断 · PR-002）
 */
public class JobRangeCheckOp extends HRDataBaseOp {

    @Override
    public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        // PR-010 · 声明本插件要读的字段
        e.getFieldKeys().add("lowjoblevel");
        e.getFieldKeys().add("highjoblevel");
        e.getFieldKeys().add("lowjobgrade");
        e.getFieldKeys().add("highjobgrade");
    }

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);  // PR-010 · 保留标品 Validator 链
        args.addValidator(new JobRangeValidator());
    }

    public static class JobRangeValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (ExtendedDataEntity row : getDataEntities()) {
                DynamicObject job = row.getDataEntity();
                checkLevelRange(row, job);
                checkGradeRange(row, job);
            }
        }

        private void checkLevelRange(ExtendedDataEntity row, DynamicObject job) {
            DynamicObject low = job.getDynamicObject("lowjoblevel");
            DynamicObject high = job.getDynamicObject("highjoblevel");
            if (low == null || high == null) return;  // 非必填 · 判空

            // PR-008 + PR-009 · 按 boid 过滤 + iscurrentversion
            HRBaseServiceHelper helper = new HRBaseServiceHelper("hbjm_joblevelhr");
            Long lowBoid = low.getLong("boid");
            Long highBoid = high.getLong("boid");

            DynamicObject lowLevel = helper.loadDynamicObject(
                new QFilter("boid", QCP.equals, lowBoid)
                    .and("iscurrentversion", QCP.equals, Boolean.TRUE));
            DynamicObject highLevel = helper.loadDynamicObject(
                new QFilter("boid", QCP.equals, highBoid)
                    .and("iscurrentversion", QCP.equals, Boolean.TRUE));
            if (lowLevel == null || highLevel == null) return;

            int lowSeq = lowLevel.getInt("joblevelseq");
            int highSeq = highLevel.getInt("joblevelseq");
            if (lowSeq > highSeq) {
                this.addErrorMessage(row, String.format(
                    "最低职级(顺序码=%d) 不能高于 最高职级(顺序码=%d)", lowSeq, highSeq));
            }
        }

        private void checkGradeRange(ExtendedDataEntity row, DynamicObject job) {
            DynamicObject low = job.getDynamicObject("lowjobgrade");
            DynamicObject high = job.getDynamicObject("highjobgrade");
            if (low == null || high == null) return;

            HRBaseServiceHelper helper = new HRBaseServiceHelper("hbjm_jobgradehr");
            DynamicObject lowGrade = helper.loadDynamicObject(
                new QFilter("boid", QCP.equals, low.getLong("boid"))
                    .and("iscurrentversion", QCP.equals, Boolean.TRUE));
            DynamicObject highGrade = helper.loadDynamicObject(
                new QFilter("boid", QCP.equals, high.getLong("boid"))
                    .and("iscurrentversion", QCP.equals, Boolean.TRUE));
            if (lowGrade == null || highGrade == null) return;

            int lowSeq = lowGrade.getInt("jobgradeseq");
            int highSeq = highGrade.getInt("jobgradeseq");
            if (lowSeq > highSeq) {
                this.addErrorMessage(row, String.format(
                    "最低职等(顺序码=%d) 不能高于 最高职等(顺序码=%d)", lowSeq, highSeq));
            }
        }
    }
}
```

**业务意图**：标品 save 链有 10 个 OP（`opkeys/save.json` executionChain · 含 CodeRuleOp / HisUniqueValidateOp 等各自注册的 Validator）· 但**没有职级/职等区间校验**（这是业务逻辑 · 不是字段级校验）。本插件并列注册到 save + confirmchange + importdata_hr。

### ⚠ 待补 · HIES-Pro 批量导入的历史模型数据处理

用户要求：跟 save / confirmchange 一样 · 批量导入也要跑这个区间校验。但**当前知识库缺 HIES-Pro 对历史模型数据的处理机制**：

- HIES 的 `importdata_hr` 入口如何注册自定义 Validator？跟普通 save 的 `onAddValidators` 是同一套吗？
- 导入的是时序资料 · 写入时怎么决定 `iscurrentversion` / `bsed` / `bsled`？
- 批量导入 vs 单条 save 的**数据流向有差异吗**（HIES 可能走独立 OP 链）？

**下一步**：后续建 `hies_entity_import` 场景或反编译 `HRImportPlugin` 时补齐这块。CS-03 的 `importdata_hr` 绑定**暂缓实现** · 等知识补齐。

### 平台绑定方式

1. 打开【苍穹开发平台】→ 定位表单 `hbjm_jobhr`
2. 选择【操作】标签 → 找到 opKey = `save` 和 `confirmchange`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效
5. ⚠️ **新增**插件 · **并列挂 · 不继承 `JobHrSaveOp`**（PR-001）· 标品 `JobHrSaveOp` 的 `onPreparePropertys` / `onAddValidators` 会独立执行 · 与本插件互不干扰（平台按 RowKey 累加）

### 踩坑

- ⚠️ **CS-03 主要防 UI 旁路 · 不是补 UI 校验**：页面手工录入时控件已拦 · 校验只对 save / confirmchange / HIES 导入 / OpenAPI 生效
- ⚠️ **顺序码字段名**：职级顺序码 = `joblevelseq`（在 `hbjm_joblevelhr` 上）· 职等顺序码 = `jobgradeseq`（在 `hbjm_jobgradehr` 上）· **不要用 id / number 排序**（业务文本顺序 ≠ 字典序）
- ⚠️ **职级 vs 职等不要混**：`joblevel` = 职级 · `jobgrade` = 职等 · CS-03 要做 2 组独立区间校验（`lowjoblevel / highjoblevel` 一组 · `lowjobgrade / highjobgrade` 一组）
- ⚠️ `lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade` 都是 BasedataField（非必填）· 可能为 null · 校验前要判空
- ⚠️ **冗余字段同步**：职位实体有 `joblevelrang` / `jobgraderang` 两个 TextField（"低~高"文本格式）· 是**职位域特有的冗余字段** · 如果 CS-02 的联动插件更新了 `lowjoblevel/highjoblevel` · 记得同步更新 `joblevelrang`（CS-03 校验不动它 · 但 CS-02 要管）
- ⚠️ `confirmchange` opKey 也要注册（变更数据版本时走同一校验链 · `_auto_operations.md` L591-L605 已证明 confirmchange 复用 JobHrSaveOp）
- ❌ 不要在 `beforeExecuteOperationTransaction` 写校验（已晚 · 数据已进入事务）· 应在 `onAddValidators` 阶段
- ❌ 不要用 `throw new KDBizException` · 用 `addErrorMessage(entity, ...)` 更好（可逐行显示错误）

### 关联 Pattern

[Pattern · add_unique_validation](../../pattern/add_unique_validation/README.md)（结构类似，只是校验的是"字段组合唯一"还是"字段区间"）

---

## CS-04 · 禁用职位前检查是否仍有在职人员 ⭐ 职位域特有

### 需求

业务方说：有人不小心禁用了一个还有在职员工使用的职位，导致员工档案出现"职位已禁用"告警；要在禁用操作时强校验"该职位下不能有在职人员"。

### 推荐方案

- **扩展点**：`onAddValidators@disable`（最早阶段阻断 · PR-010 · 早于 beforeExecute · 跟 position CS-03 标杆一致）
- **实现模式**：并列挂自定义 Validator · 继承 `AbstractValidator`（PR-001 · 不继承 `JobHrDisableOp`）
- **风险**：中（业务阻断操作 · 需要充分测试）

### 扩展入口坐标

- 绑定表单：`hbjm_jobhr`
- 绑定操作：`disable`
- 推荐父类：`HRDataBaseOp`（**并列挂插件 · 不继承 JobHrDisableOp** · 规范要求 ISV 扩展走并列挂）
- RowKey = 0（排在 `HisModelOPCommonPlugin` / `JobHrDisableOp` 之前最早阻断 · PR-002）
- 关键重写方法：
  - `onAddValidators(AddValidatorsEventArgs args)` — `super.onAddValidators(args); args.addValidator(new JobDisableDownstreamCheckValidator());`
  - Validator 的 `validate()` 里遍历 `ExtendedDataEntity[]` · 对每行：
    - 拿职位的 **`boid`**（业务维度 id · 时序资料所有历史版本共用同一个 `boid` · PR-009）
    - **循环 Tier 1 的 5 个下游实体**查引用（见"下游清单"+ 代码示例）
    - 若 > 0 · `addErrorMessage(row, "职位 [" + name + "] 下还有 N 条下游引用 · 不能禁用")`

**业务意图**：标品 `JobHrDisableOp`（`_auto_plugin_registry.md` L60 · `_auto_operations.md` L210 disable 链第 3 位）只负责禁用状态的更新 · **不做业务引用检查**。本扩展在禁用前追加"下游引用数 > 0 阻断"的业务校验。

### 下游引用清单（2026-04-24 · 真实数据 · 来自 `refentity_reverse.json`）

**数据源**：`knowledge/workbench/_indexes/refentity_reverse.json` 的 `refs.hbjm_jobhr` 段（18 条引用 · 扫 529 个标品 metadata md 产出）

#### Tier 1 · 任职关系类（禁用职位必查 · 有在职员工则阻断）

| 实体 formNumber | 字段 | 类型 | 业务含义 | 检查条件 |
|---|---|---|---|---|
| **`hrpi_empjobrel`** | `job` | BasedataField | **员工-职位任职关系**（用户确认的"任职经历"）| 在职状态 + iscurrentversion=true |
| `hrpi_empposorgrel` | `job` / `jobvid` | BasedataField | 员工-岗位-组织三维 | 在职 + iscurrentversion=true |
| `hrpi_rotationinfo` | `job` | BasedataField | 员工轮岗信息 | 轮岗中 + iscurrentversion=true |
| `hrpi_dispatchinfo` | `job` | BasedataField | 员工派遣信息 | 派遣中 + iscurrentversion=true |
| `hrpi_appointremoverel` | `job` / `jobvid` | BasedataField / HisModelBasedataField | 任免关系 | 生效中 |

#### Tier 2 · 结构关联类（禁用职位前**提示** · 可选阻断）

| 实体 | 字段 | 说明 |
|---|---|---|
| `hbpm_positionhr` | `job` | 岗位引用职位 · 可考虑校验岗位是否可解绑 |
| `hbpm_positiontpl` | `job` | 岗位模板（HisModelBasedataField）|
| `haos_chargeperson` | `job` | 组织负责人岗位 |
| `haos_orgpersonstaffinfo` | `job` | 组织人员编制 |
| `haos_dimstaffreport` / `haos_muldimendetail` / `haos_muldimendetailhis` | `job` | 多维度编制明细 / 报表 |
| `hrpi_blacklist` | `job` | 黑名单里"原就任职位" · 一般不阻断 · 可提示"有历史禁用在黑名单" |

#### Tier 3 · 忽略（内部元数据变更记录）

| 实体 | 字段 | 说明 |
|---|---|---|
| `hbjm_job_msgdetail` | `bo` / `beforeversion` / `afterversion` | 职位自己的变更消息明细 · **不是业务下游** |

#### 实施建议

- **必做**（阻断）：Tier 1 · 5 个实体的 `job` 字段 · 按 `boid` 查 + `iscurrentversion=true` + 状态在职
- **建议**（弹窗提示 · 用户确认后允许禁用）：Tier 2 · 5 个实体的 `job` 字段
- **忽略**：Tier 3 · 本就是职位变更日志

**代码示例（Tier 1 · 循环遍历 5 实体 · 完整版）**：
```java
package ${ISV_FLAG}.hrmp.hbjm.opplugin.job;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

import java.util.Arrays;
import java.util.List;

/**
 * 禁用职位前检查下游任职关系 · 并列挂到 disable opKey
 * PR-001 · 不继承 JobHrDisableOp
 * PR-002 · RowKey=0 早于 HisModelOPCommonPlugin / JobHrDisableOp
 * PR-009 · 查下游用 boid · PR-008 · 带 iscurrentversion=true
 */
public class JobDisableDownstreamCheckOp extends HRDataBaseOp {

    // Tier 1 · 5 个任职关系类实体（refentity_reverse.json 实证）
    private static final List<String[]> TIER1_ENTITIES = Arrays.asList(
        new String[]{"hrpi_empjobrel",       "job",    "员工任职关系"},
        new String[]{"hrpi_empposorgrel",    "job",    "人-岗-组关系"},
        new String[]{"hrpi_rotationinfo",    "job",    "员工轮岗信息"},
        new String[]{"hrpi_dispatchinfo",    "job",    "员工派遣信息"},
        new String[]{"hrpi_appointremoverel","job",    "任免关系"}
    );

    @Override
    public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("boid");
        e.getFieldKeys().add("number");
        e.getFieldKeys().add("name");
    }

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new JobDisableDownstreamCheckValidator());
    }

    public static class JobDisableDownstreamCheckValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (ExtendedDataEntity row : getDataEntities()) {
                DynamicObject job = row.getDataEntity();
                long jobBoid = job.getLong("boid");
                String name = job.getString("name");

                int totalCount = 0;
                StringBuilder reason = new StringBuilder();
                for (String[] entity : TIER1_ENTITIES) {
                    String entityNumber = entity[0];
                    String fieldName = entity[1];
                    String desc = entity[2];

                    HRBaseServiceHelper helper = new HRBaseServiceHelper(entityNumber);
                    // PR-009 · 按 boid · PR-008 · 带 iscurrentversion
                    QFilter qf = new QFilter(fieldName, QCP.equals, jobBoid)
                            .and("iscurrentversion", QCP.equals, Boolean.TRUE);
                    // ⚠ 在职状态字段名 + 值以各实体 scene_doc 实际为准 · 不脑补
                    // 如 hrpi_empjobrel 可能是 businessstatus="1" 或 status="active" · 需实证
                    int count = helper.count("id", qf.toArray());
                    if (count > 0) {
                        totalCount += count;
                        reason.append(desc).append("(").append(count).append(") ");
                    }
                }

                if (totalCount > 0) {
                    this.addErrorMessage(row, "职位 [" + name + "] 下还有下游引用 · 不能禁用："
                            + reason.toString().trim());
                }
            }
        }
    }
}
```

> Tier 2（`hbpm_positionhr` / `haos_chargeperson` 等）建议先**弹窗提示** · 用户确认后允许禁用 · 实现方式类似（把 `addErrorMessage` 换成 `addWarningMessage` 或独立 FormPlugin 做弹窗）。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `hbjm_jobhr`
2. 选择【操作】标签 → 找到 opKey = `disable`
3. 点击【扩展插件】→ **新增**（并列挂 · 不覆盖 `JobHrDisableOp`）
4. 保存 → 部署生效

### 踩坑

- ⚠️ **时序资料按 `boid` 查下游引用 · 不按 `id`**：
  - `id` 是**具体版本 id** · 一个职位有多个历史版本 · 每版本 id 不同
  - `boid` 是**业务维度 id** · 所有历史版本共用同一个 boid · 这才代表"一个业务对象"
  - **规律**：`boid = id` 的那条数据就是 `iscurrentversion=true` 的当前版本
  - 下游引用职位时存的一般是 `boid`（因为引用的是"某个职位"这个业务对象 · 不是某个版本）· 所以查引用要用 boid
- ⚠️ **下游表查询要带 `iscurrentversion=true`**：下游也多是时序资料 · 不过滤会查到所有历史版本 · 误报
- ⚠️ **下游引用清单靠"基础资料字段引用 hbjm_jobhr"查** · 不要靠字段名猜（如"所有带 `job` 字段的表"）· 一定要用平台真实接口
- ⚠️ 查询量大时性能差 · 建议下游表的 `job` 字段建索引 + 分批查询
- ⚠️ `e.getDataEntities()` 是数组 · 批量禁用时要逐条判断
- ❌ **不要继承 `JobHrDisableOp`**：ISV 扩展规范走**并列挂插件** · 不走继承覆盖（参考坑 15）
- ❌ **不要只查 `hrpi_pemployee`（人员档案）**：人员档案只存人员主信息 · 职位引用在**任职经历**上 · 一个人员可能没任职经历（新人 / 待入职）· 查档案会漏
- ❌ 业务上更推荐"职位状态从'启用'改为'停招'" · 而不是硬禁用 · 禁用保留给真正废弃的职位
- ❌ 不要在 `afterExecuteOperationTransaction` 检查（已禁用完成 · 无法回滚业务数据 · PR-010）

**遵循的 PR 规范**：PR-001（并列挂不继承 JobHrDisableOp）· PR-002（RowKey=0 最早阻断）· PR-008（iscurrentversion）· PR-009（boid 业务维度）· PR-010（onAddValidators 最早阶段）

### 关联 Pattern

无独立 pattern · 参考 admin_org 场景的 "CS-07 禁止删除有在职员工的组织"（已修复 · 同样走 refentity_reverse + boid + iscurrentversion）。

---

## CS-05 · 监听职位数据版本变更 → 推送下游（BEC 订阅方）⭐ 职位特有

**关联 Pattern**：无独立 pattern · BEC 是苍穹官方事件分发机制（参考 admin_org CS-04 订阅方范式 + PR-011）

### 需求

业务方说：职位变更（如职级、职等、职位序列、职位族调整）后 · 要把变更消息推送到下游模块（岗位、任职关系、派遣、轮岗等）· 让下游做后续处理（如校验在岗人员职级匹配、刷新外派合同等）。

**⚠ 错误常识澄清**（2026-04-24 反编译修订）：

原 CS-05 以为"hjm 没有标品 BEC 事件 · ISV 要自己发"· 经 `hrmp-hbjm-business-1.0.jar` 反编译证实 **hjm 标品已经走完整 3 层链路把职位变更发到 BEC** · ISV 该做的是**订阅** · 不是再发一次。硬证据：

- `JobHrMsgHandleOp.afterExecuteOperationTransaction` → `new ChangeMsgServiceImpl().sendMsg()`（挂 `save` / `confirmchange` / `newhisversion` 等 opKey · `_auto_plugin_semantics.md` L13-L25）
- `ChangeMsgServiceImpl.sendMsg` 调 `JobClient.dispatch(sch_task JOB_ID=52N5RPEPWJRD)` 异步派单 · 落 `hbjm_job_msgdetail`（sendstate=0）
- `JobMsgTask.execute` 定时扫 sendstate=0 消息 · 调 **`EventServiceHelper.triggerEventSubscribe("hbjm_jobhr.change", json)`**（`kd.hrmp.hbjm.business.application.task.JobMsgTask` 反编译第 51 行硬编码事件号 · 非脑补）
- 发完置 sendstate=1 · 主事务与发事件彻底解耦（PR-010 + PR-011）

所以：**ISV 重复发 `hbjm_jobhr.change` 会在 BEC 侧产生幂等风暴** · 正确做法是订阅同一个 eventNumber。

### 推荐方案

- **扩展点**：**订阅标品 BEC 事件 `hbjm_jobhr.change`** · 不挂 `hbjm_jobhr` 的任何 OP 插件
- **实现模式**：实现 `kd.bos.bec.api.IEventServicePlugin.handleEvent(KDBizEvent)` + 开发平台【业务事件中心】→【事件订阅】配置绑定
- **风险**：低（只读事件 + 通知下游 · 订阅方**独立事务** · 不影响标品主事务；PR-011）

### 标品发布方 3 层链路（仅作证据 · ISV 不动它）

```
[OP 层] JobHrMsgHandleOp.afterExecuteOperationTransaction
    ↓ new ChangeMsgServiceImpl().sendMsg()                     （hrmp-hbjm-business-1.0.jar）
[Service 层] ChangeMsgServiceImpl.sendMsg
    ↓ JobClient.dispatch(sch_task JOB_ID=52N5RPEPWJRD)         （异步派 sch_task）
    ↓ 落 hbjm_job_msgdetail 表（sendstate=0 待发）
[调度任务] JobMsgTask.execute                                   （kd.hrmp.hbjm.business.application.task.JobMsgTask）
    ↓ this.publish()
    ↓ 扫 hbjm_job_msgdetail sendstate=0 · 按 traceid 分组
    ↓ 按 HaosMServiceHelper.queryEventMsgSizeConfig() 配置分批
    ↓ EventServiceHelper.triggerEventSubscribe("hbjm_jobhr.change", json)   ← ⭐ 真正发 BEC
    ↓ json = {"data": [EventMsgBo.toMsgMap(msg), ...]}
    ↓ 发完置 sendstate=1 / modifytime=now
[BEC 平台] 苍穹 BEC 事件中心
    ↓
[订阅方] 实现 IEventServicePlugin.handleEvent(KDBizEvent) 接收  ← ⭐ ISV 在这里接
```

| 项 | 值（实证来源） |
|---|---|
| opKey | `save` / `confirmchange` / `newhisversion`（JobHrMsgHandleOp 挂载点 · `_auto_plugin_semantics.md` L13-L25） |
| 标品 OP | `kd.hrmp.hbjm.opplugin.web.JobHrMsgHandleOp`（`rules_chain_all.json` 实证 · **禁继承**） |
| 标品 Service | `kd.hrmp.hbjm.business.application.impl.ChangeMsgServiceImpl.sendMsg`（hrmp-hbjm-business-1.0.jar · 2026-04-24 反编译） |
| 调度任务类 | `kd.hrmp.hbjm.business.application.task.JobMsgTask`（job_id = `52N5RPEPWJRD`） |
| 派单表 | `hbjm_job_msgdetail`（字段 `bo` / `beforeversion` / `afterversion` / `changescene` / `changeoperate` / `traceid` / `sendstate` / `createtime` / `modifytime` / `index`） |
| 真实 eventNumber | **`hbjm_jobhr.change`**（JobMsgTask.java 反编译第 51 行硬编码字符串） |
| 发布 API | `EventServiceHelper.triggerEventSubscribe(String eventNumber, Object source)`（`_shared/_decompiled/bos_common/EventServiceHelper.java`） |

### 苍穹 BEC 订阅方 API · 2026-04-24 反编译实证

| API | FQN · 注解 | 证据 jar |
|---|---|---|
| 订阅接口 | `kd.bos.bec.api.IEventServicePlugin` · `@SdkPublic` | `bos-mservice-bec-api-8.0.jar` |
| 接收方法 | `Object handleEvent(KDBizEvent event)` | 同上 |
| 事件模型 | `kd.bos.bec.model.KDBizEvent` · `@SdkPublic`（4 字段 `eventId` / `eventNumber` / `source` / `variables`；`getSource()` 返回 JSON 字符串） | 同上 |
| 发布工具类 | `kd.bos.servicehelper.workflow.EventServiceHelper` · `@SdkPublic + @SdkService`（标品内部走这个 · ISV 作为订阅方不直接用） | `_shared/_decompiled/bos_common/EventServiceHelper.java` |

### 扩展入口坐标

- **绑定表单**：**不绑**（订阅方不挂 opKey · 靠开发平台业务事件中心挂钩）
- **实现接口**：`kd.bos.bec.api.IEventServicePlugin`（**不继承** `HRDataBaseOp` / `JobHrSaveOp` / `JobHrMsgHandleOp`，订阅方是独立消费者插件 · PR-001）
- **关键方法**：`handleEvent(KDBizEvent evt)` — 解析 `evt.getSource()` JSON → 按 `changescene` / `changeoperate` 分支 → 查下游引用 → 派后续动作

**业务意图**：标品 `JobMsgTask.publish` 已把职位变更通过 `EventServiceHelper.triggerEventSubscribe("hbjm_jobhr.change", json)` 异步派发到 BEC。ISV 只要在【业务事件中心】→【事件订阅】把 Consumer 绑到 `hbjm_jobhr.change` 即可。订阅方**独立事务**运行 · 标品主事务已提交 · 消费失败不会回滚职位变更（PR-011）。

### 下游引用查询路径（2026-04-24 · 真实数据 · 来自 `refentity_reverse.json`）

**数据源**：`knowledge/workbench/_indexes/refentity_reverse.json` 的 `refs.hbjm_jobhr` 段 · **18 条真实引用**

**Tier 1 · 业务主线（高频必通知 · 9 条）**：

| 下游表 | 字段 | 类型 | 业务语义 |
|---|---|---|---|
| `hrpi_empjobrel` | `job` | BasedataField | 员工-任职关系（核心：在岗员工职位匹配） |
| `hrpi_empposorgrel` | `job` | BasedataField | 人-岗-组关系（岗位层面） |
| `hrpi_empposorgrel` | `jobvid` | BasedataField | 任职版本 id |
| `hrpi_appointremoverel` | `job` | BasedataField | 任免关系 |
| `hrpi_appointremoverel` | `jobvid` | HisModelBasedataField | 任免版本 id |
| `hrpi_rotationinfo` | `job` | BasedataField | 轮岗信息 |
| `hrpi_dispatchinfo` | `job` | BasedataField | 派遣信息 |
| `hbpm_positionhr` | `job` | BasedataField | 岗位（职位的实例化） |
| `hbpm_positiontpl` | `job` | HisModelBasedataField | 岗位模板 |

**Tier 2 · 辅助业务（低频按需通知 · 6 条）**：

| 下游表 | 字段 | 类型 | 业务语义 |
|---|---|---|---|
| `haos_chargeperson` | `job` | BasedataField | 负责人设置 |
| `haos_dimstaffreport` | `job` | BasedataField | 维度编制报表 |
| `haos_muldimendetail` | `job` | BasedataField | 多维度编制 |
| `haos_muldimendetailhis` | `job` | BasedataField | 多维度编制历史 |
| `haos_orgpersonstaffinfo` | `job` | BasedataField | 组织人员编制信息 |
| `hrpi_blacklist` | `job` | BasedataField | 黑名单 |

**Tier 3 · 标品内部表（ISV 忽略 · 3 条）**：

| 下游表 | 字段 | 说明 |
|---|---|---|
| `hbjm_job_msgdetail` | `bo` / `beforeversion` / `afterversion` | **标品自己的消息暂存表** · 就是发布方链路用的那张 · ISV 不要读写 |

**查询规则**（PR-008 + PR-009）：
- 按 **`job` = 职位 boid** 过滤（PR-009：下游存的是业务维度 boid · 不是版本 id · 因为引用的是"某个职位"这个业务对象）
- Tier 1 的 `hrpi_*` 表全是 HisModel · 必须带 **`iscurrentversion = true`**（PR-008 · 不过滤会查到历史版本误派）
- Tier 2 的 `haos_*` 表多为非时序 · 直接按 `job` 过滤即可

### 代码示例

```java
package ${ISV_FLAG}.hrmp.hbjm.bec.consumer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kd.bos.bec.api.IEventServicePlugin;
import kd.bos.bec.model.KDBizEvent;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 订阅 "职位变更" 事件（eventNumber = hbjm_jobhr.change）· 推送下游
 * 事件由标品 JobMsgTask.publish 发出（hrmp-hbjm-business-1.0.jar · 2026-04-24 反编译实证）
 *
 * 只订阅 · 不发事件 · 不挂 hbjm_jobhr 任何 opKey
 * 不继承 HRDataBaseOp / JobHrSaveOp / JobHrMsgHandleOp · 实现 IEventServicePlugin 即可（PR-001）
 */
public class JobHrChangeNotifyConsumer implements IEventServicePlugin {

    private static final Log LOGGER =
            LogFactory.getLog(JobHrChangeNotifyConsumer.class);

    @Override
    public Object handleEvent(KDBizEvent evt) {
        String payload = evt.getSource();
        LOGGER.info("JobHrChangeNotifyConsumer received eventNumber={}, eventId={}, payload={}",
                evt.getEventNumber(), evt.getEventId(), payload);

        try {
            // PR-011 幂等：按 evt.getEventId() 去重 · 订阅方重试机制会重入
            // 伪代码：if (isProcessed(evt.getEventId())) return null;

            // evt.getSource() 是 JSON 字符串 · 不是 DynamicObject
            // 标品 JobMsgTask.publish 发送的结构：[{data: [EventMsgBo.toMsgMap(msg), ...]}]
            JSONArray arr = JSONArray.parseArray(payload);
            if (arr == null || arr.isEmpty()) {
                return null;
            }
            JSONObject root = arr.getJSONObject(0);
            JSONArray dataList = root.getJSONArray("data");
            if (dataList == null || dataList.isEmpty()) {
                return null;
            }

            for (int i = 0; i < dataList.size(); i++) {
                JSONObject msg = dataList.getJSONObject(i);
                // 字段名与 hbjm_job_msgdetail 一致（EventMsgBo.toMsgMap 实证）
                long jobBoid = msg.getLongValue("bo");
                Object beforeVersion = msg.get("beforeversion");
                Object afterVersion = msg.get("afterversion");
                Object changeScene = msg.get("changescene");
                Object changeOperate = msg.get("changeoperate");
                String traceId = msg.getString("traceid");

                LOGGER.info("Job change · boid={}, scene={}, operate={}, traceId={}",
                        jobBoid, changeScene, changeOperate, traceId);

                notifyDownstream(jobBoid, changeScene, changeOperate);
            }
            return null;
        } catch (Exception e) {
            // 失败不抛异常耗光 BEC 重试 · 落失败表走独立补偿任务
            LOGGER.error("JobHrChangeNotifyConsumer handleEvent failed · eventId={}",
                    evt.getEventId(), e);
            // 按业务决定是否 throw：throw 会触发 BEC 重试 · 吃掉异常会彻底丢消息
            throw e;
        }
    }

    /**
     * 按 job = boid + iscurrentversion=true 查 Tier 1 下游
     * refentity_reverse.json 实证：hbjm_jobhr 被 18 张表引用 · Tier 1 是业务主线 9 条
     */
    private void notifyDownstream(long jobBoid, Object scene, Object operate) {
        // Tier 1 - 员工任职关系（最高优先级）
        HRBaseServiceHelper empJobRel = new HRBaseServiceHelper("hrpi_empjobrel");
        QFilter qf = new QFilter("job", QCP.equals, jobBoid)             // PR-009：boid
                .and("iscurrentversion", QCP.equals, Boolean.TRUE);      // PR-008：HisModel 必须带
        DynamicObject[] rels = empJobRel.query("id,employee,adminorg", qf.toArray());
        if (rels != null && rels.length > 0) {
            LOGGER.info("Job boid={} · {} active empjobrel to notify",
                    jobBoid, rels.length);
            for (DynamicObject rel : rels) {
                Object employeeId = rel.get("employee_id");
                // notifyEmployee(employeeId, "您的职位定义已变更 · scene=" + scene);
            }
        }

        // Tier 1 - 岗位（hbpm_positionhr）· 非时序 · 直接按 job 查
        HRBaseServiceHelper positionHr = new HRBaseServiceHelper("hbpm_positionhr");
        QFilter posQf = new QFilter("job", QCP.equals, jobBoid);
        DynamicObject[] positions = positionHr.query("id,number,name", posQf.toArray());
        // ... 其他 Tier 1 下游按需补：hrpi_empposorgrel / hrpi_rotationinfo / hrpi_dispatchinfo / hrpi_appointremoverel / hbpm_positiontpl
    }
}
```

### 平台绑定方式

1. **前置·标品已做**：标品 `JobMsgTask.publish` 已在异步调度任务里调 `EventServiceHelper.triggerEventSubscribe("hbjm_jobhr.change", json)` 发事件（`hrmp-hbjm-business-1.0.jar` 反编译实证）· ISV **不用重复发**
2. 打开【苍穹开发平台】→【业务事件中心】→【事件定义】· 确认 **`hbjm_jobhr.change`** 事件已登记（标品预置 · 若 ISV 环境没登记 · 联系 HR 域负责人补登 · **不脑补其它编号**）
3. 进【事件订阅】→ 新建订阅：
   - 订阅编码：`${ISV_FLAG}_jobhr_change_notify`
   - 绑定事件：`hbjm_jobhr.change`
   - 执行服务：选"执行插件"
   - 插件名称：`${ISV_FLAG}.hrmp.hbjm.bec.consumer.JobHrChangeNotifyConsumer`
4. 配置错误处理策略（重试次数 / 失败通知）→ 保存 → 部署生效

> ⭐ `eventNumber` 必须用反编译实证的 **`hbjm_jobhr.change`** · **不要脑补** `HJM_JOB_CHANGED` / `JOBHR_CHANGE` 之类的编号（`feedback_formid_no_fabrication.md`）

### 踩坑

- ⚠️ **幂等必须自己做**（PR-011）：按 `evt.getEventId()` 或 `eventId + bo(boid)` 去重 · 订阅方重试机制会重入 · 标品 `JobMsgTask` 失败会重扫 `sendstate=0` 重发
- ⚠️ **下游查询按 `boid` 不按 `id`**（PR-009）：`hrpi_empjobrel.job` / `hbpm_positionhr.job` 存的都是职位 boid · 用当前版本 id 查会漏大部分
- ⚠️ **`iscurrentversion=true` 必须加**（PR-008）：所有 `hrpi_*` 下游（empjobrel / empposorgrel / rotationinfo / dispatchinfo / appointremoverel）都是 HisModel · 不过滤会通知到历史任职版本
- ⚠️ **`evt.getSource()` 是 JSON 字符串 · 不是 DynamicObject** · 必须 `JSONArray.parseArray` · 外层结构 `[{data: [...]}]` · 内层每条 data 是 `EventMsgBo.toMsgMap()` 的 map 结构（字段名跟 `hbjm_job_msgdetail` 同）
- ⚠️ **字段名用 `bo` 不是 `boid`**（PR-009 语境上是 boid · 但 `hbjm_job_msgdetail` 的物理字段名就叫 `bo` · `msg.getLongValue("bo")` 不是 `"boid"`）
- ⚠️ **一次事件可能含多条 data**（标品按 `traceid` 分组 + `queryEventMsgSizeConfig` 分批）· 循环处理时按单条做幂等 · 不要把整批合并成一次失败就整批重试
- ⚠️ **通知失败不要无脑抛异常** 把 BEC 重试机制耗光 · 自己 catch 记录到失败表 · 走独立补偿任务（PR-011）
- ❌ **不要挂 `hbjm_jobhr` 的 `save` / `confirmchange` / `newhisversion` opKey 写自定义 OP**（违反 PR-001 · 标品 `JobHrMsgHandleOp` 已挂 · ISV 再挂会发双事件）
- ❌ **不要继承 `JobHrMsgHandleOp` / `JobHrSaveOp` / `ChangeMsgServiceImpl`**（非 `@SdkPlugin` 白名单 · PR-001 + `cosmic_hr_sdk_whitelist_audit.md`）
- ❌ **不要复用或调用 `EventServiceHelper.triggerEventSubscribe("hbjm_jobhr.change", ...)` 再发一次**（会在 BEC 侧产生重复消息 · 订阅方收到两次 · 幂等也救不了 traceid 不同的情况）
- ❌ **不要自建 Kafka/RabbitMQ**（违反 PR-011 · BEC 已封装 · 重造轮子）
- ❌ **不要读 `hbjm_job_msgdetail` 表**（标品发布方的暂存表 · 字段语义不稳定 · 未来可能重构 · ISV 只订阅 BEC 事件号）
- ❌ **不要在 `variables` 里塞完整 DynamicObject**（PR-011 反模式 · 订阅方按 `bo` 回查即可）
- ❌ **不要用类字段暂存状态** · BEC 订阅方每次 `handleEvent` 独立调用 · 无状态消费

### 关联 Pattern

- [admin_org CS-04 · 订阅组织变更事件](../admin_org_quick_maintenance/06_customization_solutions.md#cs-04)（订阅方对称范式 · 已跑通 · 本 CS 结构完全对齐）
- PR-001 · ISV 扩展走并列挂 · 禁继承标品专属类（JobHrMsgHandleOp / ChangeMsgServiceImpl 均禁继承）
- PR-008 · 时序资料 iscurrentversion 过滤
- PR-009 · boid 业务维度 · 下游引用按 boid 查
- PR-011 · 跨模块事件走 BEC · 不自建 MQ · 订阅方独立事务 · 必须幂等

---

## 方案选型矩阵

| 业务需求 | 推荐 CS | 扩展点 | 复杂度 |
|---|---|---|---|
| 加字段 | CS-01 | modifyMeta op=add | 低 |
| 字段联动（选方案填职级职等） | CS-02 | propertyChanged (BILL_FORM) | 低 |
| 职级/职等区间校验（防 UI 旁路） | CS-03 | onAddValidators@save+confirmchange | 低 |
| 禁用前业务检查（任职经历/岗位引用） | CS-04 | onAddValidators@disable（最早阻断 · 循环 Tier 1 的 5 实体）| 中 |
| 变更通知下游（BEC 事件） | CS-05 | afterExecuteOperationTransaction@save+confirmchange + IEventService | 中 |

---

## 反模式清单（从 `_auto_plugin_semantics.md` + `scene_doc.json` minefield 反向推导）

### ❌ 禁止直接修改时序字段

参考 `JobHrMsgHandleOp` 只写 `sourcevid`（`_auto_plugin_semantics.md` L19），**从不写** `boid` / `iscurrentversion` / `datastatus` / `hisversion` / `firstbsed`。这些字段由平台 `HisModelOPCommonPlugin`（save 链第 9 位）维护。

- ❌ 扩展插件 `entity.set("boid", ...)` — 破坏时序链
- ❌ 扩展插件 `entity.set("iscurrentversion", true)` — 破坏版本切换逻辑
- ❌ 扩展插件 `entity.set("hisversion", "99")` — 打乱版本号递增
- ❌ 扩展插件 `entity.set("firstbsed", ...)` — 最早生效日期是不变量

### ❌ 禁止修改出厂数据字段

- ❌ 修改 `orinumber` / `oriname` / `oristatus` → 破坏 `HRBaseOriginalOp`（save 链第 7 位）的出厂比对逻辑

### ❌ 禁止修改系统预置字段

- ❌ 修改 `issyspreset` → 破坏系统预置职位保护，导致预置职位被误删

### ❌ 禁止引用废弃字段

- ❌ 新规则中写 `depcytype` 校验 — `scene_doc.json` L566 已明确"(废弃)"，下个大版本可能被移除

### ❌ 禁止覆盖标品插件失去 super

- ❌ 覆盖 `JobHrSaveOp` 但不调用 super → 丢失 `onPreparePropertys` 声明的字段和已注册的 `JobEnableValidator` 类似校验器
- ❌ 覆盖 `JobHrEnableOp` 但不调用 super → 丢失 `JobEnableValidator`（`rules_chain_all.json` L678-L688 已注册）
- ❌ 覆盖 `CodeRuleOp` → 丢失标品编码规则自动生成

### ❌ 禁止 updateOperation 全量覆盖

- ❌ 调 `updateOperation(plugins=[myPlugin])` → save 链 10 标品插件全丢（`EX-21` 引用）
- ✅ 正确姿势：`current = getOperation(); current.plugins.append(myPlugin); updateOperation(plugins=current.plugins)`

### ❌ 禁止在 HIES 自定义页绕开权限

- ❌ `importdata_hr` 的 `cusstartpage: hismodel_importstart` 是标品导入向导页；自定义页面必须走同一权限校验（`hrbddeletevalidator` / 创建组织过滤）

### ❌ 禁止扩展继承父模板

- ❌ 扩展 `hbp_histimeseqtpl`（时序模板）→ 全 HR 时序基础资料连带炸
- ❌ 扩展 `hbp_bd_tpl_all` → 全 HR 基础资料炸
- ❌ 扩展 `bos_basetpl` → 全苍穹基础资料炸

---

**📌 来源追溯**：

- CS-01 扩展点：`modifyMeta` OpenAPI + `scene_doc.json` 64 字段类型分布
- CS-02 JobHisBasedataFiledChangeEdit 参考：`_auto_plugin_semantics.md` L29-L43
- CS-03 save 链：`_auto_operations.md` L99-L122 + `rules_chain_all.json` L368-L403
- CS-04 JobHrDisableOp：`_auto_plugin_registry.md` L60 · `_auto_operations.md` L210
- CS-05 CodeRuleOp 顺序：`_auto_operations.md` L104 save 链第 1 位
- CS-06 JobHrMsgHandleOp 语义：`_auto_plugin_semantics.md` L13-L26（3 方法 + boid/iscurrentversion/sourcevid 读写）
- 反模式 时序字段：`scene_doc.json` L378-L497（minefield=red 的 9 字段）+ `_auto_plugin_semantics.md` L19（JobHrMsgHandleOp 仅写 sourcevid）
- 反模式 updateOperation：`knowledge/kb_cosmic_modifymeta_traps.md` EX-21
