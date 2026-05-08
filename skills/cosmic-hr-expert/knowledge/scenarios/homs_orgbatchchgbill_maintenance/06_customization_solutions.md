# 定制方案 · 组织调整申请单

> **状态**: 🟢 基于 8 类反编译实证 + 11 PR 红线 + admin_org/hjm/hbpm 对偶模式整合
> **confidence**: real_deploy
> **数据源**: `OrgBatchBillSaveOp.java` / `OrgBatchBillSubmitOp.java` / `OrgBatchChgBillEffectOp.java` / `AdminOrgBatchBreakupOp.java` / `AdminChangeMsgService.java` / `AdminOrgBatchBillPlugin.java` / `_shared/platform_rules.json`

所有方案遵循统一结构：**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

> 🎯 选 CS 的决策图见 [01_capability_boundary.md §十](01_capability_boundary.md)。

---

## CS-01 · 给 entry 加自定义字段（最高频）

### 需求

业务方说：组织调整申请单里 · 每个 add 新增组织 / parent 调上级 entry 都要记录"申请来源系统"（用于跟外系工单系统对接）。

### 推荐方案

- **扩展对象**: `entryentity_add` / `entryentity_parent` / `entryentity_info` / `entryentity_disable` 4 个 EntryEntity 中需要的（不需要全 7 个加）
- **扩展点**: `modifyMeta(op=add field)` × N（每个 entry 1 次）
- **风险**: 低（不改字段类型 · 不改物理表结构）
- **关联 PR**: PR-001（不改标品 · ISV 加自己的字段） · PR-007（业务字段可改）

### ⚠ 跟 admin_org_quick CS-01 的差异

| 维度 | admin_org_quick CS-01 | 本场景 CS-01 |
|---|---|---|
| 主表是什么 | `haos_adminorg`（HisModel） | `homs_orgbatchchgbill`（BillFormModel） |
| 加字段挂哪 | 主实体 `haos_adminorg` | 7 entry 之一（每个独立 parentScope） |
| 字段联动 | 加 1 字段 + 4 前缀同步（CS-02 级联） | 直接加在目标 entry · **不需要级联**（本场景就是 4 前缀的"目标" · 加在自己 entry 即可） |
| 必填规则 | 元数据层 mustInput | 同 |
| 命名规则 | `${ISV_FLAG}_xxx` | **`{entry_prefix}_tdkw_xxx`** ← 重要 |

⚠️ **关键**：在 entry 加字段必须遵循 entry 自身前缀规则。比如 `entryentity_add` 里的字段都是 `add_*` 前缀 · 加自定义字段也要叫 `add_tdkw_sourcesys` · **不能**直接叫 `${ISV_FLAG}_sourcesys`（违反约定 · 平台标品同步逻辑识别不到）。

### 调用链（每个 entry 一次）

```python
modifyMeta({
  formId: "homs_orgbatchchgbill",
  ops: [
    {
      op: "add",
      treeType: "entity",
      elementType: "field",
      parentScope: "entryentity_add",   # ← 关键 · 7 entry 中选一
      element: {
        fieldType: "TextField",
        key: "add_tdkw_sourcesys",       # ← 必须带 entry 前缀 + ISV 前缀
        name: {zh_CN: "申请来源系统", en_US: "Source System"},
        mustInput: false,
        maxLength: 50
      }
    }
  ]
})
```

加完后 · 二次验证：
```python
schema = client.get_form_schema("homs_orgbatchchgbill")
# 检查 schema 里是否出现 add_tdkw_sourcesys
```

### 代码框架（cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    target_form_info={"number": "homs_orgbatchchgbill"}
)

# 给 4 个常用 entry 同时加同一字段
for prefix in ["add", "parent", "info", "disable"]:
    designer.add_field(
        field_type="TextField",
        name="申请来源系统",
        key=f"{prefix}_tdkw_sourcesys",
        parent_entity_id=f"entryentity_{prefix}",  # 必须用 entry 名作 parentScope
        max_length=50,
    )
designer.save()  # 一次 save 一起提交所有字段
```

### 踩坑

- ❌ 字段 key 不带 entry 前缀（`${ISV_FLAG}_sourcesys`）→ 跟主表字段冲突 · 平台 designer 报"字段已存在"
- ❌ 把字段加到 `entryentity_all`（只读视图）→ 静默失败 · 显示能保存但 6 个真分录不会同步该字段
- ❌ 同一字段在 7 entry 都加 · 但 merge / split / detail 子分录其实**不放业务字段**（它们用 多基础资料分录 to_merge_org / to_split_org）→ 加了也没用 · 浪费迁移
- ⚠️ `MuliLangTextField` 拼写一个 t · 不是 `MultiLangTextField`（平台对此静默忽略 · 后端报错才发现）
- ⚠️ 字段名超过 25 字符（含 `f` 前缀的物理列名）→ 数据库建表失败
- ⚠️ 加字段后 · 必须 PDM 同步刷物理表结构（OpenAPI 没暴露 PDM · 走 UI 走苍穹开发平台手动同步）
- ❌ 用 `EmployeeField` → buildMeta 74 值枚举不支持 · 改用 `BasedataField` + `refEntity=hrpi_employeenewf7query`

### 关联 PR

- PR-001 · ISV 字段必带前缀 不改标品
- PR-007 · 业务自建字段可改
- **PR-005** · 7 entry 行 id 用 `kd.bos.id.ID.genLongId()` · 见下方补充

### 补充：7 entry 分录新增行的 id 生成规范（PR-005）

本场景核心特性是 **7 个分录 entry**（add / parent / merge / split / info / disable / merge_detail / split_detail）· ISV 扩展常需要"代码触发新增 entry 行"或"复制粘贴 entry 行"· 行 id 必须用 `kd.bos.id.ID`：

```java
import kd.bos.id.ID;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;

// 场景 1：扩展 ImportSource · 把外部数据装到 entryentity_add 分录（参考 AdminOrgBatchEntryImportPlugin）
DynamicObjectCollection addEntries = bill.getDynamicObjectCollection("entryentity_add");
DynamicObject newRow = addEntries.addNew();
newRow.set("id", ID.genLongId());                          // 行 id · 19 位 Snowflake · 分布式唯一
newRow.set("add_number", externalData.get("orgcode"));
newRow.set("add_name", externalData.get("orgname"));

// 场景 2：从一个 entry 复制行到另一个 entry（如 add → info 跨分录复制）
DynamicObjectCollection infoEntries = bill.getDynamicObjectCollection("entryentity_info");
DynamicObject copy = infoEntries.addNew();
copy.set("id", ID.genLongId());                            // 必须新 id · 不能复用原行 id
copy.set("info_number", srcRow.get("add_number"));

// 场景 3：自定义申请单流水号后缀（如审批失败重发要新单号）
String traceId = ID.genStringId();                         // 字符串 ID · traceId / 日志追踪
```

**遵循 PR-005 · 苍穹 Snowflake 算法保证集群环境全局唯一**

⚠ **反模式**（违反 PR-005 必驳回）：
- ❌ `UUID.randomUUID()` · 体积大不必要
- ❌ `System.currentTimeMillis()` · 7 entry 同时新增会撞 id
- ❌ 复制行时复用 srcRow.get("id") · entry 行级唯一约束会冲突

---

## CS-02 · 校验"申请单生效前 add 新组织编码不能与已存在组织重复"

### 需求

业务方说：用户在 add entry 新增组织时 · 即便填了编码 · 也要在**保存时**就立刻校验全租户范围内是否已有同编码的组织（不要等到生效时才发现冲突）。

### 推荐方案

- **扩展点**: `onAddValidators@save` · 新增独立 Validator 跟标品 `OrgBatchBillSaveAndSubmitValidator` 并列跑
- **实现**: 新写继承 `AbstractValidator` 的校验类 · 用 `HRBaseServiceHelper("haos_adminorg")` 查重
- **风险**: 中（需读 haos_adminorg 全租户数据 · 大数据量需走索引）
- **关联 PR**: PR-001（不继承标品 Validator） · PR-008 / PR-009（boid + iscurrentversion 查询）

### ⚠ 跟 admin_org_quick CS-05（按租户唯一）的差异

admin_org_quick CS-05 是改"标品全局唯一"为"租户内唯一" · 替换语义。本 CS 是**新增**一种校验 · 跟标品并列跑。本质都是"挂新 Validator 到 save 链"。

### 扩展入口坐标

- 绑定表单：`homs_orgbatchchgbill`
- 绑定操作：`save`（也覆盖 submit / submiteffect · 因为它们复用 save 的 Validator）
- 推荐父类：
  - OP（注册 Validator）：`HRDataBaseOp`（不继承 OrgBatchBillSaveOp / OrgBatchBillSubmitOp · 它们都是 final 标品 · PR-001）
  - Validator 自身：直接 `extends AbstractValidator`
- 注册位置：新 OP 的 `onAddValidators` · `e.addValidator(new OrgAddNumberDuplicateValidator())`

### 业务意图

新写一个 OP 挂在 save / submiteffect / submit 链上 · `onAddValidators` 注册自定义 Validator · 该 Validator 遍历所有 add entry · 把 number 字段集中起来一次性查 haos_adminorg 是否有同编码的当前版本 · 命中则用 `addErrorMessage` 标错。

### 平台绑定方式

1. 打开【苍穹开发平台】→ 定位表单 `homs_orgbatchchgbill`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 新增（并列挂 · 不覆盖 OrgBatchBillSaveOp）
4. 类名 `${ISV_FLAG}.hrmp.homs.opplugin.OrgAddNumberDuplicateOp`
5. 保存 → 部署生效（标品 Validator 链继续跑 · 加上本插件的）

### 代码示例（基于反编译实证）

```java
package ${ISV_FLAG}.hrmp.homs.opplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 新增 add entry 编码全租户唯一校验 OP · 并列挂 save / submit / submiteffect
 * 不继承 OrgBatchBillSaveOp（final + 标品专属 · PR-001）
 * Validator 复用 save 链 · submit / submiteffect 自动级联（OrgBatchBillSubmitOp.onAddValidators 仅 super）
 */
public final class OrgAddNumberDuplicateOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new OrgAddNumberDuplicateValidator());
    }

    /**
     * 校验：add entry 中所有非空 number · 不能跟 haos_adminorg 当前版本组织编码重复
     */
    public static class OrgAddNumberDuplicateValidator extends AbstractValidator {

        private static final Log LOG =
            LogFactory.getLog(OrgAddNumberDuplicateValidator.class);

        @Override
        public void validate() {
            ExtendedDataEntity[] dataEntities = this.getDataEntities();
            if (dataEntities == null || dataEntities.length == 0) {
                return;
            }
            // 1. 收集所有 add entry 的 number（带行号 · 用于精准报错）
            Map<String, List<NumberRow>> numberMap = new HashMap<>();
            for (ExtendedDataEntity ext : dataEntities) {
                DynamicObject bill = ext.getDataEntity();
                DynamicObjectCollection addEntries =
                    bill.getDynamicObjectCollection("entryentity_add");
                if (addEntries == null) continue;
                for (int i = 0; i < addEntries.size(); i++) {
                    DynamicObject row = addEntries.get(i);
                    String number = row.getString("add_number");
                    if (StringUtils.isEmpty(number)) {
                        // 编码为空 · 后续 OrgBatchBillSubmitAndEffectiveOp.setAddOrgNumber 会自动生成
                        // 这里跳过 · 不校验空值
                        continue;
                    }
                    numberMap.computeIfAbsent(number, k -> new ArrayList<>())
                             .add(new NumberRow(ext, i));
                }
            }
            if (numberMap.isEmpty()) {
                return;
            }

            // 2. 一次查所有 number · 不要循环单条查（慢 + 锁问题）
            //    haos_adminorg 是 HisModel · 必须带 iscurrentversion=true · PR-008
            HRBaseServiceHelper orgHelper = new HRBaseServiceHelper("haos_adminorg");
            QFilter filter = new QFilter("number", "in", numberMap.keySet())
                .and("iscurrentversion", "=", "1");
            DynamicObject[] existing = orgHelper.query("number", filter.toArray());

            // 3. 命中重复 · 按行精准报错
            Set<String> dupNumbers = Arrays.stream(existing)
                .map(dy -> dy.getString("number"))
                .collect(Collectors.toSet());

            for (String dupNum : dupNumbers) {
                for (NumberRow row : numberMap.get(dupNum)) {
                    this.addErrorMessage(
                        row.entity,
                        String.format("新增组织编码 [%s] 已被其他组织占用 · 请改用新编码（add 分录第 %d 行）",
                                     dupNum, row.rowIndex + 1)
                    );
                }
            }
        }

        private static class NumberRow {
            final ExtendedDataEntity entity;
            final int rowIndex;
            NumberRow(ExtendedDataEntity e, int idx) { this.entity = e; this.rowIndex = idx; }
        }
    }
}
```

### 踩坑

- ❌ **不要继承 `OrgBatchBillSaveOp`** · final + 标品专属 · 违反 PR-001（实证：`OrgBatchBillSaveOp.java:35` `public final class`）
- ❌ **不要继承 `OrgBatchBillSaveAndSubmitValidator`** · 标品内部 Validator · 不在 SDK 白名单
- ❌ 用循环单条查 `HRBaseServiceHelper.exists` · 大批量时慢 + 锁问题 · 必须一次 `in` 查
- ⚠️ `haos_adminorg` 是 HisModel · **必须带 `iscurrentversion=1`** · 否则会查到历史版本误报（PR-008）
- ⚠️ 自定义编码可能空 · 要先判断 `StringUtils.isEmpty` 跳过（等 setAddOrgNumber 自动补）
- ⚠️ 若业务允许"申请单提交期间允许重复"（多张草稿单内的 add number 不互查）· 要排除自身单内的 number · 加 `not in (本单 add entry numbers)`
- ❌ 校验在 `beforeExecuteOperationTransaction` 而非 Validator → 错过 cancel 机制 · 错误提示不友好

### 关联 PR

- PR-001 · ISV 不继承标品 Final OP
- PR-008 · HisModel 查询带 iscurrentversion
- PR-009 · 时序资料下游引用按 boid（本 CS 是上游 · 用 number 查 ok）

---

## CS-03 · 字段联动："调整上级"分录选了"上级组织"自动带出"管理层级"

### 需求

业务方说：用户在 parent entry 选了 `parent_parentorg`（新上级行政组织）后 · 自动按上级组织的"管理层级"自动填到 `parent_adminorglayer`（变更后管理层级）· 用户少手动选一次。

### 推荐方案

- **扩展点**: `propertyChanged@homs_orgbatchchgbill` 表单插件 · 监听 `parent_parentorg` 字段变更
- **实现**: 新写 FormPlugin extends `HRCoreBaseBillEdit`（继承同标品 `AdminOrgBatchBillPlugin` 的父类 · **但不继承标品本身** · PR-001）
- **风险**: 低
- **关联 PR**: PR-003（FormPlugin 用 getModel().setValue） · PR-004（防死循环 beginInit/endInit）

### 扩展入口坐标

- 绑定表单：`homs_orgbatchchgbill`
- 推荐父类：`HRCoreBaseBillEdit`（HR 通用 · 跟标品 `AdminOrgBatchBillPlugin` 同源 · **不继承标品本身**）
- 关键重写：`propertyChanged(PropertyChangedArgs e)`

### 业务意图

监听 `parent_parentorg` 字段变更 · 拿到上级组织 boid · 查 `haos_adminorghrf7` 拿 `adminorglayer` · 用 `getModel().beginInit() + setValue + endInit()` 防死循环回填到 `parent_adminorglayer`。

### 平台绑定方式

1. 打开【苍穹开发平台】→ 定位表单 `homs_orgbatchchgbill`
2. 选择【插件】标签
3. 点击【新增】（并列挂 · 不覆盖标品 AdminOrgBatchBillPlugin）
4. 类名 `${ISV_FLAG}.hrmp.homs.formplugin.OrgBatchParentLayerAutoFillPlugin`
5. 保存 → 部署生效

### 代码示例

```java
package ${ISV_FLAG}.hrmp.homs.formplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit;

/**
 * parent entry 联动：选了"调整后上级行政组织"自动带出"管理层级"
 *
 * 不继承 AdminOrgBatchBillPlugin（final 标品 · PR-001）
 * 跟它并列挂 · 标品 propertyChanged 委托 AdminOrgBatchBillPropertyChangedService 处理其他联动
 * 本插件只关注 parent_parentorg → parent_adminorglayer 一条联动
 */
public final class OrgBatchParentLayerAutoFillPlugin extends HRCoreBaseBillEdit {

    private static final Log LOG =
        LogFactory.getLog(OrgBatchParentLayerAutoFillPlugin.class);

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        String fieldKey = e.getProperty().getName();
        // 只监听 parent_parentorg
        if (!"parent_parentorg".equals(fieldKey)) {
            return;
        }
        for (ChangeData cd : e.getChangeSet()) {
            int rowIndex = cd.getRowIndex();
            Object newVal = cd.getNewValue();
            if (newVal == null) {
                // 用户清空了上级 · 也清空 layer
                this.setLayerValue(rowIndex, null);
                continue;
            }
            // newVal 是 DynamicObject（BasedataField 类型）
            long parentOrgId = ((DynamicObject) newVal).getLong("id");
            if (parentOrgId == 0L) {
                continue;
            }
            this.fillLayer(rowIndex, parentOrgId);
        }
    }

    /**
     * 按上级组织 id 查 adminorglayer · 回填到当前 entry 行
     */
    private void fillLayer(int rowIndex, long parentOrgId) {
        try {
            HRBaseServiceHelper orgHelper = new HRBaseServiceHelper("haos_adminorghrf7");
            // 时序资料 · 必须带 iscurrentversion · PR-008
            DynamicObject parentOrg = orgHelper.queryOne(
                "id, adminorglayer",
                new QFilter("id", "=", parentOrgId).toArray()
            );
            if (parentOrg == null) {
                LOG.warn("OrgBatchParentLayerAutoFillPlugin · parent org not found id={}",
                         parentOrgId);
                return;
            }
            DynamicObject layer = parentOrg.getDynamicObject("adminorglayer");
            this.setLayerValue(rowIndex, layer == null ? null : layer.get("id"));
        } catch (Exception ex) {
            LOG.error("OrgBatchParentLayerAutoFillPlugin · fillLayer failed", ex);
            // 业务联动失败不抛异常 · 让用户手动填即可
        }
    }

    /**
     * 防死循环 · setValue 会再次触发 propertyChanged · 必须包 beginInit/endInit · PR-004
     */
    private void setLayerValue(int rowIndex, Object layerId) {
        this.getModel().beginInit();
        try {
            this.getModel().setValue("parent_adminorglayer", layerId, rowIndex);
        } finally {
            this.getModel().endInit();
        }
        this.getView().updateView("parent_adminorglayer", rowIndex);
    }
}
```

### 踩坑

- ❌ 不用 `beginInit/endInit` 包 setValue → 死循环（自身 setValue 触发 propertyChanged · PR-004 实证）
- ❌ 用 `super` 继承 `AdminOrgBatchBillPlugin` 重写 propertyChanged → 死链路 · 标品委托业务 Service 时会跟自定义重叠（PR-001）
- ❌ `setValue` 不带 `rowIndex` → 修改主表字段 · entry 行内字段必须带行号
- ⚠️ 查 `haos_adminorghrf7`（视图）而不是 `haos_adminorg`（主表）· F7 视图返回的 id 跟主表 id 一致 · 但只有 `iscurrentversion=true` 的版本可见
- ⚠️ 异常包住不抛 · 用户手填还能继续 · 不要让联动失败阻塞业务
- ⚠️ 性能：N 行 entry 同时变更时（用户批量粘贴）· 一次循环触发 N 次查询 · 大批量场景需缓存（HashMap<orgid, layerId>）

### 关联 PR

- PR-001 · 不继承标品 final FormPlugin · 并列挂
- PR-003 · FormPlugin 用 getModel().setValue
- PR-004 · 防死循环 beginInit/endInit
- PR-008 · 时序查询带 iscurrentversion

---

## CS-04 · 拦截"提交时未填写变动场景"（带行号精准报错）

### 需求

业务方说：用户提交申请单时 · 经常忘了在 add / parent / info / disable 各 entry 行选 `*_changescene`（变动场景）· 标品报错 "请填写变动场景" 但**不告诉是哪一行**。改成：精准告诉"add 分录第 3 行未选变动场景"。

### 推荐方案

- **扩展点**: `onAddValidators@submit`（也可挂 save · 但 submit 更靠后 · 跟 wf 链路对齐）
- **实现**: 新 OP extends `HRDataBaseOp` + Validator extends `AbstractValidator`
- **风险**: 低
- **关联 PR**: PR-001 · PR-010（onAddValidators 阶段）

### 扩展入口坐标

- 绑定表单：`homs_orgbatchchgbill`
- 绑定操作：`submit` (也覆盖 submiteffect · 标品级联)
- 推荐父类 OP：`HRDataBaseOp`（PR-001 · 不继承 OrgBatchBillSubmitOp 等 final 标品）
- 注册位置：OP 的 `onAddValidators`

### 平台绑定方式

1. 打开【苍穹开发平台】→ 定位表单 `homs_orgbatchchgbill`
2. 选择【操作】→ 找到 opKey = `submit`
3. 【扩展插件】→ 新增（并列挂 · PR-001）
4. 类名 `${ISV_FLAG}.hrmp.homs.opplugin.OrgBatchChangeSceneCheckOp`
5. 保存 → 部署生效

### 代码示例

```java
package ${ISV_FLAG}.hrmp.homs.opplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * 提交时校验：4 个写入 entry（add/parent/info/disable）每行必须选 changescene
 *
 * 标品 OrgBatchBillSaveAndSubmitValidator 已校验 changescene 必填 · 但报错信息不带行号
 * 本扩展精准报告"哪个 entry 哪行" · 跟标品并列跑 · 互不干扰（PR-001）
 */
public final class OrgBatchChangeSceneCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new ChangeSceneCheckValidator());
    }

    public static class ChangeSceneCheckValidator extends AbstractValidator {
        // 7 entry 中只有 4 个会写入有 changescene 字段（merge/split 走子分录 · all 是只读视图）
        private static final String[] WRITE_ENTRIES = {"add", "parent", "info", "disable"};
        private static final String[] ENTRY_LABELS = {"新增组织", "调整上级", "信息变更", "停用组织"};

        @Override
        public void validate() {
            ExtendedDataEntity[] dataEntities = this.getDataEntities();
            if (dataEntities == null || dataEntities.length == 0) {
                return;
            }
            for (ExtendedDataEntity ext : dataEntities) {
                DynamicObject bill = ext.getDataEntity();
                for (int i = 0; i < WRITE_ENTRIES.length; i++) {
                    String entryName = "entryentity_" + WRITE_ENTRIES[i];
                    String fieldKey = WRITE_ENTRIES[i] + "_changescene";
                    String label = ENTRY_LABELS[i];
                    DynamicObjectCollection rows =
                        bill.getDynamicObjectCollection(entryName);
                    if (rows == null || rows.isEmpty()) continue;
                    for (int r = 0; r < rows.size(); r++) {
                        DynamicObject row = rows.get(r);
                        DynamicObject scene = row.getDynamicObject(fieldKey);
                        if (scene == null) {
                            this.addErrorMessage(
                                ext,
                                String.format("【%s】分录第 %d 行未填写变动场景 · 请补齐后再提交。",
                                             label, r + 1)
                            );
                        }
                    }
                }
            }
        }
    }
}
```

### 踩坑

- ❌ 校验放 `beforeExecuteOperationTransaction` 而非 Validator → 失去逐行 cancel 能力 · 提示不精准（PR-010）
- ❌ Validator 直接抛 `KDBizException` → 阻断整张单 · 改用 `addErrorMessage` 让平台逐行高亮（PR-001 推荐做法）
- ❌ 校验 7 个 entry 都查 changescene → 但 `entryentity_merge` / `entryentity_split` 自身没有 `merge_changescene` 必填字段（实际走 merge_changescene 配置的是子表 · 不挂 entry 主行）· 强校会误报
- ⚠️ `entryentity_all` 是只读视图（参考 03_model_design §三）· 不要在它上面校验
- ⚠️ submit 跟 submiteffect 都需要校验 · 但**只挂 submit OP**就够 · 因为标品 `OrgBatchBillSubmitAndEffectiveOp.onAddValidators` 仅 super · 实际复用 save 链 → 反过来 · 也可以挂 save · 同样 submit 自动级联（参考 02_business_rules §三）

### 关联 PR

- PR-001 · 并列挂 · 不继承
- PR-010 · onAddValidators 阶段挂 Validator
- PR-007 · 业务逻辑（不能动 changescene 类型）

---

## CS-05 · 监听组织变动事件 · 通知下游订阅方（BEC 订阅方）⭐ 跨场景对偶

### 需求

业务方说：组织调整申请单生效后 · 我们的 WMS / CRM 系统也要同步更新组织树（不能让 ERP 业务员手动两边维护）。要求：

- 不影响主事务（生效失败时不要因为通知失败回滚）
- 支持重试（消息发不出时自动重发）
- 不挂 `homs_orgbatchchgbill` 任何 OP（不污染主链路）

### 推荐方案

- **扩展点**: 苍穹**业务事件中心 BEC** 订阅方
- **实现**: implements `kd.bos.bec.api.IEventServicePlugin` · 在【业务事件中心】→【事件订阅】配置绑定
- **风险**: 低（订阅方独立事务 · 不影响主事务 · PR-011）
- **关联 PR**: PR-001 · PR-008 · PR-009 · PR-011

### ⚠ 跟 admin_org_quick CS-04 / hjm CS-05 的对偶关系

| 维度 | admin_org_quick CS-04 | hjm CS-05（发布方） | **本场景 CS-05（订阅方）** |
|---|---|---|---|
| 角色 | BEC 订阅方 | BEC 发布方 | **BEC 订阅方** |
| 标品有没有现成事件发布 | 有（confirmchange 链已发） | 标品没发 · ISV 自发 | 有（OrgBatchChgBillEffectOp afterExec 通过 sch_task 异步派发） |
| ISV 要做什么 | 订阅 · 不发 | 发 · 不订 | 订阅 · 不发（同 admin_org） |
| 事件号 | "行政组织变更" | ISV 自定义 | "行政组织变更"（**与 admin_org_quick 共用同一事件号** · 因为最终都派 sch_task JOB_ID="5+X/4Y=AOZ=O"） |

⚠️ **关键认知**：`homs_orgbatchchgbill` 跟 `admin_org_quick` **共用同一个标品事件发布机制**！

实证：
- `OrgBatchChgBillEffectOp.afterExecuteOperationTransaction` (L77) → `new AdminChangeMsgService().handleChangeMsg();`
- `AdminChangeMsgService.java` 同时被 admin_org_quick 的 `confirmchange` 链和 homs_orgbatchchgbill 的 `audit/submiteffect` 链调用
- JOB_ID 都是 `5+X/4Y=AOZ=O`

→ 你订阅一次"行政组织变更"事件 · admin_org_quick 直改产生的变动 + homs_orgbatchchgbill 走单产生的变动 **都能收到**。

### 标品发布方实证（仅作证据 · ISV 不动它）

| 项 | 值（实证） |
|---|---|
| opKey | `audit` 或 `submiteffect` |
| 标品 OP | `OrgBatchChgBillEffectOp.java`（HRDataBaseOp 子类 · L52-131） |
| afterExec 阶段 | L77 `new AdminChangeMsgService().handleChangeMsg();` |
| sch_task 派发 | `AdminChangeMsgService.java:113-123` · JOB_ID="5+X/4Y=AOZ=O" |
| 派单表 | `haos_adminorg_msgdetail`（assembleMsgDy L81-111 写入） |
| 字段：bo / beforeversion / afterversion / changescene / changeoperate / isbelongcompanychange | `AdminChangeMsgService.java:83-107` 实证 |

### BEC 订阅 API（同 admin_org CS-04）

| API | FQN · 注解 | 证据 jar |
|---|---|---|
| 订阅接口 | `kd.bos.bec.api.IEventServicePlugin` · `@SdkPublic` | bos-mservice-bec-api-8.0.jar |
| 接收方法 | `Object handleEvent(KDBizEvent event)` | 同上 |
| 事件模型 | `kd.bos.bec.model.KDBizEvent` · 4 字段 eventId/eventNumber/source/variables | 同上 |

### 扩展入口坐标

- **不绑表单**（订阅方独立 · 不挂 OP / FormPlugin）
- **实现接口**：`kd.bos.bec.api.IEventServicePlugin`
- **关键方法**：`handleEvent(KDBizEvent evt)`

### 业务意图

订阅"行政组织变更"事件 · 解析 `evt.getSource()` JSON · 按 `changescene` 分支处理：
- 新增组织 → 推送到 WMS 创建组织节点
- 调整上级 → 推送到 WMS 移动节点
- 合并 → 推送到 WMS 合并节点
- 拆分 → 推送到 WMS 拆分节点
- 停用 → 推送到 WMS 停用节点

### 代码示例

```java
package ${ISV_FLAG}.hrmp.homs.bec.consumer;

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
 * 订阅"行政组织变更"事件 · 同步推送到 WMS / CRM
 *
 * 跟 admin_org_quick CS-04 共用同一事件号
 * 因为 OrgBatchChgBillEffectOp.afterExec 调用的 AdminChangeMsgService.handleChangeMsg
 * 跟 admin_org_quick 的 confirmchange 链调用同一个 service · sch_task JOB_ID="5+X/4Y=AOZ=O"
 *
 * 不挂 OP · 不影响主事务 · PR-011
 */
public class OrgChangeWmsSyncConsumer implements IEventServicePlugin {

    private static final Log LOG = LogFactory.getLog(OrgChangeWmsSyncConsumer.class);

    @Override
    public Object handleEvent(KDBizEvent evt) {
        String payload = evt.getSource();
        LOG.info("OrgChangeWmsSyncConsumer · eventNumber={}, eventId={}, payloadSize={}",
                 evt.getEventNumber(), evt.getEventId(),
                 payload == null ? 0 : payload.length());

        try {
            // 1. 幂等：按 eventId 查处理表 · 跳过已处理（PR-011 推荐做法）
            if (this.isAlreadyProcessed(evt.getEventId())) {
                LOG.info("OrgChangeWmsSyncConsumer · already processed eventId={}",
                         evt.getEventId());
                return null;
            }

            // 2. 解析 payload · 标品发布的格式：[{data: [...]}]
            JSONArray rootArr = JSONArray.parseArray(payload);
            if (rootArr == null || rootArr.isEmpty()) {
                return null;
            }
            JSONObject root = rootArr.getJSONObject(0);
            JSONArray dataList = root.getJSONArray("data");
            if (dataList == null || dataList.isEmpty()) {
                return null;
            }

            // 3. 逐条处理（每条对应 1 个组织变动 · AdminChangeMsgService.assembleMsgDy）
            for (int i = 0; i < dataList.size(); i++) {
                JSONObject item = dataList.getJSONObject(i);
                long bo = item.getLongValue("bo");                       // 组织 boid · PR-009
                long beforeVer = item.getLongValue("beforeversion");
                long afterVer = item.getLongValue("afterversion");
                Object changeScene = item.get("changescene");
                Object changeOperate = item.get("changeoperate");
                boolean belongCompanyChange = item.getBooleanValue("isbelongcompanychange");

                this.dispatchToWms(bo, beforeVer, afterVer,
                                   changeScene, changeOperate, belongCompanyChange);
            }

            // 4. 标记已处理（幂等表）
            this.markProcessed(evt.getEventId());
            return null;
        } catch (Exception ex) {
            LOG.error("OrgChangeWmsSyncConsumer · handleEvent failed eventId={}",
                      evt.getEventId(), ex);
            // 抛异常会让 BEC 重试 · 想自定义重试策略时不要抛
            // 这里选择 catch + 写补偿表（防止占用 BEC 重试次数）
            this.recordFailure(evt.getEventId(), ex.getMessage());
            return null;
        }
    }

    /**
     * 按 changeoperate 分发到 WMS 不同接口
     * 注意：这里只读 boid · 业务详情自行回查（PR-011 反模式：不要在 payload 里塞完整 DynamicObject）
     */
    private void dispatchToWms(long bo, long beforeVer, long afterVer,
                               Object changeScene, Object changeOperate,
                               boolean belongCompanyChange) {
        // 1. 按 boid + iscurrentversion 回查当前组织（PR-008 + PR-009）
        HRBaseServiceHelper orgHelper = new HRBaseServiceHelper("haos_adminorg");
        QFilter qf = new QFilter("boid", QCP.equals, bo)
                          .and("iscurrentversion", QCP.equals, "1");
        DynamicObject currentOrg = orgHelper.queryOne(
            "id, number, name, parentorg, enable, longnumber",
            qf.toArray()
        );
        if (currentOrg == null) {
            LOG.warn("OrgChangeWmsSyncConsumer · org not found boid={}", bo);
            return;
        }

        // 2. 调 WMS 接口（伪代码）
        // wmsClient.syncOrg(boId=bo, ...)
        LOG.info("OrgChangeWmsSyncConsumer · sync to WMS boid={} number={} name={} changeScene={}",
                 bo, currentOrg.getString("number"), currentOrg.getString("name"), changeScene);
    }

    private boolean isAlreadyProcessed(String eventId) {
        // 查 ISV 自建幂等表 ${ISV_FLAG}_bec_processed
        // 实现略 · 用 HRBaseServiceHelper("${ISV_FLAG}_bec_processed").exists(...)
        return false;
    }

    private void markProcessed(String eventId) {
        // 写 ISV 自建幂等表
    }

    private void recordFailure(String eventId, String errorMsg) {
        // 写 ISV 自建失败表 · 走独立补偿任务
    }
}
```

### 平台绑定方式

1. **前置 · 标品已做**：`OrgBatchChgBillEffectOp.afterExec` (L77) + `AdminChangeMsgService.handleChangeMsg()` 已派 sch_task · ISV 不要重复发
2. 打开【苍穹开发平台】→【业务事件中心】→【事件定义】· 确认"行政组织变更"事件号（需在环境实测 · `eventNumber` 不脑补）
3. 进【事件订阅】→ 新建：
   - 订阅编码：`${ISV_FLAG}_homs_org_wms_sync`
   - 绑定事件：上一步的 eventNumber
   - 执行服务：选"执行插件"
   - 插件名：`${ISV_FLAG}.hrmp.homs.bec.consumer.OrgChangeWmsSyncConsumer`
4. 配置错误处理（重试次数 / 失败通知）→ 保存 → 部署生效

### 踩坑

- ⚠️ **eventNumber 必须实测查** · 不脑补 `HOMS_ORG_BATCH_CHANGED` 之类（参考 `feedback_formid_no_fabrication.md`）
- ⚠️ **跟 admin_org_quick CS-04 共用 eventNumber** · 同一订阅会同时收到两种来源的事件 · 在 changeoperate 分发时要兼容（不要硬编"只处理 batch 来的"）
- ⚠️ **幂等必须自己做** · 按 `evt.getEventId()` 去重 · 重试机制会重入
- ⚠️ **回查组织走 boid + iscurrentversion** · 别用 id（PR-008 + PR-009）
- ⚠️ **`evt.getSource()` 是 JSON 字符串** · 不是 DynamicObject · 用 fastjson 解析 · 内层结构通常是 `[{data: [...]}]`
- ⚠️ **大批量变更**（一次合并 100 个组织）会触发一次事件带很多 data · 要循环处理 · 不要把多条合并成一次失败就整批重试
- ⚠️ **通知失败不要抛异常**消耗 BEC 重试 · 自己 catch + 写补偿表
- ❌ **不要挂 OrgBatchChgBillEffectOp 的 afterExec 写 OP** → 标品已发事件 · ISV 再发会重复发（PR-001）
- ❌ **不要继承 OrgBatchChgBillEffectOp** → final + 标品专属（实证 `OrgBatchChgBillEffectOp.java:52` `public final class`）
- ❌ **不要继承 AdminChangeMsgService** → 它在 `kd.hr.haos.business.domain.org.service` 包 · 标品内部 service · 非 SDK 公共类
- ❌ **不要在 BEC payload `variables` 里塞完整 DynamicObject** · 订阅方按 boid 自己回查（PR-011 反模式）
- ❌ **不要自建 Kafka/RabbitMQ** · BEC 已封装（PR-011）

### 关联 PR

- PR-001 · ISV 不挂 OP / 不继承 final 标品
- PR-008 · 时序查询 iscurrentversion
- PR-009 · 下游引用 boid
- PR-011 · BEC · 不自建 MQ

---

## CS-06 · 自定义"按 ERP 模板批量生效"· 扩展 audit afterExec

### 需求

业务方说：每次大型组织调整后 · 要自动同步更新 ERP 系统的成本中心维度 · 现在标品生效后 BEC 异步发事件给 WMS（CS-05）· 但 ERP 那边要求**同步**反馈成功失败（不能用异步事件 · 要拿到回执才能继续）。所以需要在生效完成后**主事务内**调用 ERP 同步接口。

### 推荐方案

- **扩展点**: `afterExecuteOperationTransaction@audit` 也覆盖 `submiteffect`（OrgBatchChgBillEffectOp 共用同一 OP）
- **实现**: 新 OP extends `HRDataBaseOp` · 重写 `afterExecuteOperationTransaction` · 同步调 ERP
- **风险**: 中（同步调外系会拖长生效操作 · 网络异常会让用户体验差 · 通常配 timeout + 限重试）
- **关联 PR**: PR-001 · PR-010

### ⚠ 跟 CS-05 的差异 / 选哪个

| 需求 | CS-05 BEC 订阅方 | CS-06 主事务 afterExec |
|---|---|---|
| 同步反馈成功 | ❌（异步） | ✅ |
| 调用失败要回滚 | ❌ | ✅（抛异常会让生效失败回滚） |
| 实时性 | 秒级（sch_task 调度） | 毫秒级（同步调） |
| 主事务影响 | 不影响 | **会拖慢主事务** |
| 大批量场景 | 强（异步消化） | 弱（同步会卡） |
| 推荐 | **优先选 CS-05** · 除非业务必须同步 | 仅当业务必须同步时 |

### 扩展入口坐标

- 绑定表单：`homs_orgbatchchgbill`
- 绑定操作：`audit`（也覆盖 `submiteffect`）
- 推荐父类：`HRDataBaseOp`（PR-001 · 不继承 OrgBatchChgBillEffectOp · 它是 final · L52）
- 关键重写：`afterExecuteOperationTransaction(AfterOperationArgs e)`

### 业务意图

主事务已提交（PR-010 阶段 9 · afterExec）· 此时主数据已落到 haos_adminorg · 同步调 ERP 接口推送变更 · 失败时记录补偿表 · 不让事务回滚（已提交了想回也回不去）。

### 代码示例

```java
package ${ISV_FLAG}.hrmp.homs.opplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 申请单生效后 · 同步推送到 ERP（成本中心同步）
 *
 * 不继承 OrgBatchChgBillEffectOp（final · PR-001）· 并列挂 audit 链
 * 主事务在 beginExec / endExec 完成 · 这里 afterExec 是事务提交后阶段（PR-010 #9）
 * 此时调用外系接口安全 · 不会让主事务读到脏数据
 */
public final class OrgBatchErpSyncOp extends HRDataBaseOp {

    private static final Log LOG = LogFactory.getLog(OrgBatchErpSyncOp.class);

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        String opKey = e.getOperationKey();
        // 只处理 audit / submiteffect
        if (!"audit".equals(opKey) && !"submiteffect".equals(opKey)) {
            return;
        }
        DynamicObject[] bills = e.getDataEntities();
        if (bills == null || bills.length == 0) {
            return;
        }
        for (DynamicObject bill : bills) {
            try {
                long billId = bill.getLong("id");
                String billno = bill.getString("billno");
                this.syncToErp(billId, billno);
            } catch (Exception ex) {
                // 主事务已提交 · 这里抛异常没用 · 写补偿表
                LOG.error("OrgBatchErpSyncOp · sync to ERP failed billno={}",
                          bill.getString("billno"), ex);
                this.recordFailure(bill.getLong("id"), ex.getMessage());
            }
        }
    }

    /**
     * 取本单所有 entry · 按 changetype 分发到 ERP 接口
     */
    private void syncToErp(long billId, String billno) {
        // 1. 加载所有 entry（注意：homs_batchorgentity 是物理实体名 · 反编译实证）
        HRBaseServiceHelper entryHelper = new HRBaseServiceHelper("homs_batchorgentity");
        DynamicObject[] entries = entryHelper.loadDynamicObjectArray(
            new QFilter[]{new QFilter("billid", "=", billId)}
        );
        if (entries == null || entries.length == 0) {
            return;
        }
        // 2. 按 changetype 分发
        List<DynamicObject> entriesList = Arrays.stream(entries).collect(Collectors.toList());
        // 3. 调 ERP 接口（伪代码）
        // erpClient.batchSyncOrgChange(billno, entriesList, timeout=5000ms);
        LOG.info("OrgBatchErpSyncOp · billno={} synced {} entries to ERP",
                 billno, entriesList.size());
    }

    private void recordFailure(long billId, String errMsg) {
        // 写 ISV 自建失败表 · 走独立补偿任务
        // HRBaseServiceHelper("${ISV_FLAG}_homs_erp_sync_fail").save(...)
    }
}
```

### 平台绑定方式

1. 打开【苍穹开发平台】→ 定位表单 `homs_orgbatchchgbill`
2. 选择【操作】→ 找到 opKey = `audit`
3. 【扩展插件】→ 新增（并列挂 · 不覆盖 OrgBatchChgBillEffectOp）
4. 类名 `${ISV_FLAG}.hrmp.homs.opplugin.OrgBatchErpSyncOp`
5. 同步给 `submiteffect` 也挂一份（也可以让两个 opKey 共用同一插件）
6. 保存 → 部署生效

### 踩坑

- ❌ **绝对不要在 `beginOperationTransaction` 调外系接口**：事务还没提交 · 接口失败会让事务回滚 · 但有些情况外系已收到（接口语义可能 at-least-once）· 数据状态不一致
- ❌ **不要继承 `OrgBatchChgBillEffectOp`** · `public final class`（L52）· 标品专属
- ⚠️ **同步调用必须设 timeout**（如 5000ms）· 否则一个慢接口拖死所有审批操作
- ⚠️ **大批量场景考虑切到 CS-05 BEC 订阅方** · 100+ entry 时同步调用会让用户等很久
- ⚠️ **failure 不抛异常**：主事务已提交 · 抛异常无效 · 但会显示一个红错误提示给用户 · 用户看不懂会慌 · 用 LOG.error + 补偿表
- ⚠️ **重试策略要幂等**：ERP 接口若不幂等 · 重试时可能重复处理 · 用 billno 当幂等 key
- ⚠️ `homs_batchorgentity` 是反编译实证的物理实体名（`OrgBatchBillSaveOp.java:57`）· 不要用 entryentity_xxx（前端逻辑分录名 · 不能直接 HRBaseServiceHelper 取）

### 关联 PR

- PR-001 · 并列挂 · 不继承 final
- PR-010 · afterExec 阶段调外系（事务已提交）

---

## CS-07 · 终止申请单时校验"已生效组织数 > N 不能终止"

### 需求

业务方说：用户终止已生效的申请单时（C 状态 → G 状态）· 标品 `AdminOrgBatchBillBreakupStatusValidator` 仅校验单据状态 · 不校验"该单造成的下游影响范围"。要加：

- 如果该单已生效 + 改了 ≥ 5 个行政组织 · 不允许直接终止 · 必须管理员授权
- 如果该单造成下游 hrpi_empjobrel 任职关系变动 · 弹"将影响 N 名员工任职" 二次确认

### 推荐方案

- **扩展点**: `onAddValidators@breakup`
- **实现**: 新 OP extends `HRDataBaseOp` · Validator extends `AbstractValidator`
- **风险**: 中（涉及下游 refentity_reverse 查询 · 大批量影响时性能）
- **关联 PR**: PR-001 · PR-008 · PR-009

### 扩展入口坐标

- 绑定表单：`homs_orgbatchchgbill`
- 绑定操作：`breakup`
- 推荐父类 OP：`HRDataBaseOp`（不继承 `AdminOrgBatchBreakupOp` · 它是 `public final class`（L24））
- Validator 自身：`extends AbstractValidator`

### 平台绑定方式

1. 打开【苍穹开发平台】→ 定位表单 `homs_orgbatchchgbill`
2. 选择【操作】→ 找到 opKey = `breakup`
3. 【扩展插件】→ 新增（并列挂 · 不覆盖 AdminOrgBatchBreakupOp）
4. 类名 `${ISV_FLAG}.hrmp.homs.opplugin.OrgBatchBreakupImpactCheckOp`
5. 保存 → 部署生效

### 代码示例

```java
package ${ISV_FLAG}.hrmp.homs.opplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 终止申请单前 · 校验影响范围
 *
 * 不继承 AdminOrgBatchBreakupOp（public final · L24）· PR-001
 * 跟标品 AdminOrgBatchBillBreakupStatusValidator 并列跑（标品只校状态 · 本扩展校影响）
 */
public final class OrgBatchBreakupImpactCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new BreakupImpactValidator());
    }

    public static class BreakupImpactValidator extends AbstractValidator {

        private static final int MAX_EFFECTED_ORG_FOR_NORMAL_USER = 5;

        @Override
        public void validate() {
            ExtendedDataEntity[] dataEntities = this.getDataEntities();
            if (dataEntities == null || dataEntities.length == 0) {
                return;
            }
            for (ExtendedDataEntity ext : dataEntities) {
                DynamicObject bill = ext.getDataEntity();
                String billStatus = bill.getString("billstatus");
                long billId = bill.getLong("id");
                String billno = bill.getString("billno");

                // 1. 仅对 C 已生效状态做"影响范围"校验
                //   （A 暂存 / B 待审 状态没真正落地 · 不需要校验影响）
                if (!"C".equals(billStatus)) {
                    continue;
                }

                // 2. 加载该单的 entry · 拿到所有受影响的组织 boid
                //   注意：homs_batchorgentity 是物理实体名（反编译实证）
                Set<Long> affectedOrgBoids = this.getAffectedOrgBoids(billId);

                // 3. 影响组织数 > N 不允许普通用户终止
                if (affectedOrgBoids.size() > MAX_EFFECTED_ORG_FOR_NORMAL_USER) {
                    this.addErrorMessage(
                        ext,
                        String.format("申请单 %s 已生效 · 影响 %d 个行政组织 · 超过自助终止上限 %d · 请联系管理员授权终止。",
                                     billno, affectedOrgBoids.size(),
                                     MAX_EFFECTED_ORG_FOR_NORMAL_USER)
                    );
                    continue;
                }

                // 4. 影响下游员工任职校验（refentity_reverse: hrpi_empjobrel.adminorg → haos_adminorghrf7）
                //   按 boid + iscurrentversion=true 查（PR-008 + PR-009）
                int empCount = this.countAffectedEmployees(affectedOrgBoids);
                if (empCount > 0) {
                    this.addErrorMessage(
                        ext,
                        String.format("申请单 %s 已生效 · 终止后将影响 %d 名员工的任职关系（hrpi_empjobrel）· 请二次确认。",
                                     billno, empCount)
                    );
                }
            }
        }

        private Set<Long> getAffectedOrgBoids(long billId) {
            // homs_batchorgentity 反编译实证 · OrgBatchBillSaveOp.java:57
            HRBaseServiceHelper entryHelper = new HRBaseServiceHelper("homs_batchorgentity");
            DynamicObject[] entries = entryHelper.query(
                "adminorg.boid",
                new QFilter[]{new QFilter("billid", "=", billId)}
            );
            return Arrays.stream(entries)
                         .map(dy -> dy.getLong("adminorg.boid"))
                         .filter(b -> b != 0L)
                         .collect(Collectors.toSet());
        }

        private int countAffectedEmployees(Set<Long> orgBoids) {
            if (orgBoids.isEmpty()) return 0;
            // hrpi_empjobrel.adminorg 字段是 HRAdminOrgField · 存的是 boid（PR-009）
            // hrpi_empjobrel 是 HisModel · 必须带 iscurrentversion · PR-008
            HRBaseServiceHelper relHelper = new HRBaseServiceHelper("hrpi_empjobrel");
            QFilter qf = new QFilter("adminorg", QCP.in, orgBoids)
                              .and("iscurrentversion", QCP.equals, "1");
            return relHelper.queryDynamicObjectCollection("id", qf.toArray()).size();
        }
    }
}
```

### 踩坑

- ❌ **不要继承 `AdminOrgBatchBreakupOp`** · `public final class`（实证 `AdminOrgBatchBreakupOp.java:24`） · PR-001
- ❌ **不要继承 `AdminOrgBatchBillBreakupStatusValidator`** · 标品内部 Validator
- ❌ **不要直接 set billstatus = G** 来"终止" · 真终止还需要回退 haos_adminorg 主数据 · `OrgBatchBreakupService.doBreakUpBill` 里有完整逻辑（不能跳过）
- ⚠️ 查 `hrpi_empjobrel` 必须带 `iscurrentversion=1` 否则会查到历史任职版本（PR-008）
- ⚠️ 查任职关系按组织 `boid`（业务维度）· 不是 id（PR-009 · `hrpi_empjobrel.adminorg` 存的是 boid · 跟 admin_org CS-04/CS-07 同模式）
- ⚠️ "影响组织数 > 5" 这种阈值最好参数化（系统参数表 / 配置项）· 业务可调
- ⚠️ 提示给用户的消息里不要暴露 boid · 用 number / name（用户友好）

### 关联 PR

- PR-001 · 并列挂 · 不继承 final
- PR-008 · iscurrentversion 过滤
- PR-009 · 下游引用按 boid 查

---

## 方案选型矩阵

| 业务需求 | 推荐 CS | 扩展点 | 复杂度 |
|---|---|---|---|
| 加 entry 字段 | CS-01 | modifyMeta × N | 低 |
| 跨 entry 业务校验 | CS-02 / CS-04 | onAddValidators@save 或 submit | 中 |
| 字段联动 | CS-03 | propertyChanged | 低 |
| 监听变动事件 / 同步外系 (异步) | CS-05 | BEC IEventServicePlugin | 中 |
| 同步推送外系 (实时回执) | CS-06 | afterExecuteOperationTransaction@audit | 中 |
| 终止前补充校验 | CS-07 | onAddValidators@breakup | 中 |

---

## 反模式清单（禁止）

- ❌ 继承 `OrgBatchBillSaveOp` / `OrgBatchBillSubmitOp` / `OrgBatchBillSubmitAndEffectiveOp` / `OrgBatchChgBillEffectOp` / `AdminOrgBatchBreakupOp` 等任何 final 标品 OP
- ❌ 继承 `AdminOrgBatchBillPlugin` / `AdminOrgBatchBillListPlugin`（final）
- ❌ 继承 `AdminChangeMsgService` / `OrgBillBatchEffectService` / `OrgBatchBreakupService`（标品内部 Service · 非 SDK 公共）
- ❌ 在 `entryentity_all`（只读视图）加字段
- ❌ 字段 key 不带 entry 前缀（add/parent/info/disable/merge/split）
- ❌ 字段 key 不带 ISV 前缀（如 `${ISV_FLAG}_`）· 跟标品冲突时 · 升级会被覆盖
- ❌ 在 `beginOperationTransaction` 调外系接口（事务未提交）
- ❌ 在 `afterExecuteOperationTransaction` 做校验（事务已提交无法回滚）
- ❌ 在 BEC payload 里塞完整 DynamicObject（PR-011 反模式）
- ❌ 直接 `setValue("billstatus", "C")` 越过状态机
- ❌ 让"同 org 多 entry"通过（违反平台跨 entry 互斥规则）
- ❌ 用 `EmployeeField`（buildMeta 不支持）

---

**📌 来源追溯**：

- CS-01 · entry 字段加挂：`_shared/_standard_metadata/entity_metadata/homs_orgbatchchgbill.md` 7 entry 字段实证
- CS-02 · save Validator 链：`OrgBatchBillSaveOp.java:39-44`（标品 Validator 注册位置）
- CS-03 · propertyChanged 委托：`AdminOrgBatchBillPlugin.java:356-360`（标品委托 PropertyChangedService 实证）
- CS-04 · onAddValidators 阶段：`OrgBatchBillSaveOp.java:39` + PR-010
- CS-05 · BEC 事件发布：`OrgBatchChgBillEffectOp.java:77` + `AdminChangeMsgService.java:113-123`
- CS-05 · sch_task JOB_ID 实证：`AdminChangeMsgService.java:46-47`（`5+X/4Y=AOZ=O` / `5+X/=KD8ZXFW`）
- CS-06 · afterExec 阶段：PR-010 #9 + `OrgBatchChgBillEffectOp.java:76-83` 实证
- CS-07 · breakup final OP：`AdminOrgBatchBreakupOp.java:24` + `OrgBatchBreakupService.doBreakUpBill` 实证
- CS-07 · refentity_reverse 下游：`knowledge/workbench/_indexes/refentity_reverse.json` 的 `refs.haos_adminorghrf7` 段
- 11 PR 红线引用：`_shared/platform_rules.json` v1
