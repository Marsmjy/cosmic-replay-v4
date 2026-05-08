# 推荐定制方案 · 组织历史查询（haos_adminorghis）

> **状态**：基于 7 反编译类（HisModel* 4 类 + Admin* 3 类）+ 36 opKey + 平台 PR-001..011 实证
> **confidence**：verified
> **审计时间**：2026-04-27

所有方案遵循统一结构：**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

⚠ **本场景特殊性**：物理表与 admin_org_quick_maintenance 共用 `t_haos_adminorg` · 字段类扩展请优先去 admin_org_quick_maintenance CS-01（扩展挂 haos_adminorg 主层 · 双场景共用）· 本文档 CS 聚焦"历史查询场景独有的定制需求"。

⚠ **BEC 模式判定**：反编译产物 grep 结果 0 处 `triggerEventSubscribe / IEventService / EventServiceHelper` · **本场景标品没发任何 BEC 事件** · CS-07 BEC 章节砍掉（不为凑数硬写发布方）。需要 BEC 订阅组织变更走 admin_org_quick_maintenance CS-04。

---

## CS-01 · 给历史查询列表加自定义筛选字段（最高频）

**关联 PR**：PR-001（并列挂插件）+ PR-008（iscurrentversion 时序过滤）+ PR-009（boid 维度查询）

### 需求

业务方说："历史查询列表能不能按 ISV 自加字段（如 `${ISV_FLAG}_region`）筛选历史版本？"

### 推荐方案

- **扩展对象**：列表 form `haos_adminorghis`
- **扩展点**：并列挂新 `ListPlugin` · 实现 `setFilter(SetFilterEvent)`
- **风险**：低（只追加 QFilter · 不改 iscurrentversion）

### 调用链

反编译实证：标品 setFilter 执行链 ≥3 层：
```
HRDataBaseList.setFilter (super 父类 · 数据权限 + BU 闸)
   ↓
HisModelListCommonPlugin.setFilter L167-L181 (final · 强制 iscurrentversion=false)
   ↓
[ISV 新插件 · 并列] setFilter (追加 ISV 自定义过滤)
```

ISV 插件挂在标品后跑 · 加自己的 QFilter · 跟标品的 `iscurrentversion=false` AND 关系叠加。

### 扩展入口坐标

- 绑定表单：`haos_adminorghis`
- 推荐父类：**`HRDataBaseList`**（@SdkPlugin 白名单 · ISV 标准选择）· **不继承 `HisModelListCommonPlugin`**（@SdkInternal · final · PR-001）
- 关键重写方法：
  - `setFilter(SetFilterEvent e)` — `e.getQFilters().add(qf)` · 追加过滤
  - 注意是 add 不是 set（不要清掉标品已加的过滤）

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.adminorghis.listplugin;

import kd.bos.context.RequestContext;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 历史查询列表追加 ISV 字段过滤
 * 遵循 PR-001 · 并列挂插件 · 不继承 HisModelListCommonPlugin（final · @SdkInternal）
 * 遵循 PR-008 · iscurrentversion 由标品已注入 · 这里只 add 不改
 */
public class TdkwAdminOrgHisListPlugin extends HRDataBaseList {

    @Override
    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);  // 跑 HRDataBaseList 默认数据权限

        // ISV 自定义筛选：从 ShowParameter 拿用户选的 region 值
        Object regionParam = getView().getFormShowParameter()
                .getCustomParam("${ISV_FLAG}_region_filter");
        if (regionParam == null) {
            return;
        }

        // 追加 QFilter（与标品 iscurrentversion=false 是 AND 关系）
        QFilter regionFilter = new QFilter("${ISV_FLAG}_region", QCP.equals, regionParam);
        e.getQFilters().add(regionFilter);
    }
}
```

### 平台绑定方式

1. 苍穹开发平台 → 表单 `haos_adminorghis`（**视图层 · 不是 haos_adminorg 主层**）
2. 注册插件 · `targetType = LIST_FORM`（R20 大写枚举）
3. 部署生效

### 踩坑

- ❌ 想去掉标品的 `iscurrentversion=false` 让本场景显示当前版本 → 改不掉（标品 final 方法兜底 · 见 02_business_rules.md §1）
- ❌ 在 setFilter 里 `e.setQFilters(new ArrayList<>())` 清空 → 标品过滤全丢 · 数据安全问题（违反 PR-001）
- ❌ 继承 `HisModelListCommonPlugin` → @SdkInternal · final · 编译过不去
- ⚠ ISV 加字段 `${ISV_FLAG}_region` 必须先在 `haos_adminorg` 主层（不是 haos_adminorghis 视图）扩展（详见 admin_org_quick_maintenance CS-01）· 否则物理表没列 · 查询失败
- ⚠ 大数据量场景（>1M 行）· DBA 配合给 `t_haos_adminorg.ftdkw_region` 建索引 · 不然全表扫超时

### 关联 PR

- 遵循 **PR-001** · ISV 扩展走并列挂 · 禁继承标品场景专属类
- 遵循 **PR-008** · iscurrentversion 由标品 final 方法兜底 · ISV 不动
- 遵循 **PR-009** · 涉及组织字段时 · 引用按 boid 不按 id

---

## CS-02 · 默认排序定制：按 hisversion DESC（继承 HRDataBaseList）

**关联 PR**：PR-001（并列挂）+ PR-008（时序模型）

### 需求

业务方说："标品默认按 hisversion DESC 我能不能改成按 modifytime DESC？"或"间断时序模式下 modifytime 优先 bsed？"

### 推荐方案

- **扩展对象**：列表 form `haos_adminorghis`
- **扩展点**：并列挂新 ListPlugin · 重写 `beforeCreateListColumns`
- **风险**：低（只调 setOrder · 不动数据）

### 调用链

反编译实证：标品 `HisModelListCommonPlugin.beforeCreateListColumns L107-L139` 已经按 HisModelTypeEnum 决定排序：
```
NO_INTERRUPTION_NO_OVERLAP → bsed DESC + modifytime DESC
其他 → hisversion DESC
```

ISV 想改 → 在标品后并列跑 · 拿到 ListColumn 集合再覆盖 setOrder 即可。

### 扩展入口坐标

- 绑定表单：`haos_adminorghis`
- 推荐父类：**`HRDataBaseList`**（@SdkPlugin 白名单）· **禁继承 HisModelListCommonPlugin**（@SdkInternal final）· **禁继承 HisModelF7ListPlugin**（@SdkInternal final）· **禁继承 AdminOrgHisListPlugin**（标品 haos 域 · 大概率 final）
- 关键重写方法：
  - `beforeCreateListColumns(BeforeCreateListColumnsArgs args)` — 在 args.getListColumns() 上后处理 setOrder

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.adminorghis.listplugin;

import kd.bos.entity.filter.SortType;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.list.IListColumn;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 历史查询排序定制：modifytime DESC 优先
 * 遵循 PR-001 · 并列挂插件 · 标品 HisModelListCommonPlugin 跑完后 · 本插件覆盖 ListColumn.setOrder
 * 遵循 PR-008 · 不动 iscurrentversion 过滤
 */
public class TdkwAdminOrgHisSortListPlugin extends HRDataBaseList {

    @Override
    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
        super.beforeCreateListColumns(args);

        // 标品已经按默认规则 setOrder · 这里再覆盖
        // 第一步：清掉所有列的排序
        args.getListColumns().forEach(col -> col.setOrder(SortType.NotOrder.name()));

        // 第二步：modifytime DESC（业务方需求）
        args.getListColumns().stream()
                .filter(c -> HRStringUtils.equals(c.getListFieldKey(), "modifytime"))
                .findFirst()
                .ifPresent(c -> c.setOrder(SortType.DESC.name()));

        // 第三步：备用排序键 hisversion DESC（业务方推荐：modifytime 同值时按 hisversion）
        args.getListColumns().stream()
                .filter(c -> HRStringUtils.equals(c.getListFieldKey(), "hisversion"))
                .findFirst()
                .ifPresent(c -> c.setOrder(SortType.DESC.name()));
    }
}
```

### 踩坑

- ❌ 在 setFilter 里追加 ORDER BY → 不该用 setFilter 做排序 · 用 ListColumn.setOrder
- ❌ 继承 `HisModelListCommonPlugin` 想 super 然后覆盖 → final 类编不过
- ⚠ NO_INTERRUPTION_NO_OVERLAP 模式下标品也是 setOrder 两次（bsed + modifytime） · ISV 这里再覆盖 · 看似浪费 · 但能保证最终就是 ISV 想要的顺序（最后赢）
- ⚠ ListColumn.setOrder 改的是默认排序 · 用户在列表点列头排序仍然有效（用户操作优先）

### 关联 PR

- 遵循 **PR-001** · 不继承标品 list final 类
- 遵循 **PR-008** · 不动时序过滤

---

## CS-03 · 历史版本横向对比视图（基于 boid + sourcevid 链）

**关联 PR**：PR-008 / PR-009

### 需求

业务方说："标品 versionchangecompare 一次只能选 ≤2 行 · 我想看一个组织 boid 的全部历史版本横向对比表 · 像 git diff 那样按字段差异展开。"

### 推荐方案

- **扩展对象**：自建动态表单 `${ISV_FLAG}_adminorghis_compare`（不挂在 haos_adminorghis 上）
- **扩展点**：HRDynamicFormBasePlugin 自定义动态表单 + 调用 HRBaseServiceHelper 查 t_haos_adminorg
- **风险**：中（需要业务方拍板字段差异渲染规则）

### 调用链

反编译实证：标品 versionchangecompare（`HisModelListCommonPlugin.afterDoOperation L232-L238`）拿 selectedRowIds 集合调 `HisModelListShowFormProcessor.showVersionChangeCompareList`。

ISV 自建对比页：
1. 从 admin_org_quick_maintenance / haos_adminorghis 列表点"全历史对比"按钮 → 跳本动态表单
2. 动态表单 preOpenForm 拿 boid 参数
3. afterBindData 调 HRBaseServiceHelper("haos_adminorghis").queryOriginalCollection 拿全历史
4. 按字段渲染对比表

### 扩展入口坐标

- 自建动态表单 formId：`${ISV_FLAG}_adminorghis_compare`（ISV 前缀）
- 推荐父类：**`HRDynamicFormBasePlugin`**（@SdkPlugin 白名单 · HR 域专用动态表单父类）
- 关键重写方法：`preOpenForm` / `beforeBindData` / `afterBindData`

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.adminorghis.formplugin;

import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin;

/**
 * 全历史版本横向对比页
 * 遵循 PR-008 · 不带 iscurrentversion 过滤（要全版本：当前 + 历史）
 * 遵循 PR-009 · 按 boid 业务维度查 · 不按 id（id 是版本维度）
 */
public class TdkwAdminOrgHisCompareFormPlugin extends HRDynamicFormBasePlugin {

    private static final String[] COMPARE_FIELDS = {
            "id", "boid", "iscurrentversion", "hisversion",
            "sourcevid", "bsed", "bsled", "firstbsed",
            "datastatus", "changedescription",
            "number", "name",
            "parentorg", "adminorgtype", "belongcompany",
            "modifier", "modifytime"
    };

    @Override
    public void preOpenForm(PreOpenFormEventArgs args) {
        super.preOpenForm(args);
        // 期望调用方传 boid customParam
        Long boid = (Long) args.getFormShowParameter().getCustomParam("boid");
        if (boid == null || boid <= 0L) {
            args.setCancel(true);
            getView().showErrorNotification("缺少 boid 参数");
        }
    }

    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        Long boid = (Long) getView().getFormShowParameter().getCustomParam("boid");
        if (boid == null) {
            return;
        }

        // PR-009：按 boid 查全部历史版本（含当前）· 不带 iscurrentversion 过滤
        HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorg");
        QFilter qf = new QFilter("boid", "=", boid);
        DynamicObjectCollection allVersions = helper.queryOriginalCollection(
                String.join(",", COMPARE_FIELDS), new QFilter[]{qf}, "hisversion asc");

        renderCompareTable(allVersions);
    }

    private void renderCompareTable(DynamicObjectCollection versions) {
        // 走自定义 EntryEntity 渲染各版本字段差异
        // 业务方可定制：标识哪些字段变了（红字）哪些没变（灰字）
    }
}
```

### 跳转入口（外部场景调用本动态表单）

```java
// 在 admin_org_quick_maintenance 列表上的扩展按钮（ISV 自加）
FormShowParameter params = new FormShowParameter();
params.setFormId("${ISV_FLAG}_adminorghis_compare");
params.getOpenStyle().setShowType(ShowType.MainNewTabPage);
params.setCustomParam("boid", focusedRow.getLong("boid"));
this.getView().showForm(params);
```

### 踩坑

- ❌ 按 id 查 → 拿不到其它版本（id 是版本维度 · 一个 id 只对应一行）· 必须按 boid（PR-009）
- ❌ 加 `iscurrentversion=false` 过滤 → 漏掉当前版本 · 用户对比看不全（**对比要全版本** · 与列表查询不同）
- ❌ 直接继承 HisModelFormCommonPlugin 想复用 → @SdkInternal final · 编不过
- ⚠ DynamicObjectCollection 大批量（>500 历史版本）页面渲染会卡 · 加分页 / 按时间窗收窄
- ⚠ 时序字段差异渲染要根据 datastatus 决定颜色（DISCARDED 灰显 · TEMP 黄字 · EFFECTING 绿字 · EXPIRED 灰）

### 关联 PR

- 遵循 **PR-008** · 注意"不加 iscurrentversion 过滤"是有意而为的（要全版本）· 不是违反规则
- 遵循 **PR-009** · boid 业务维度查
- 遵循 **PR-001** · 自建动态表单 · 不继承标品 form 类

---

## CS-04 · 拦截 his_save · 校验"补录历史版本前 bsed 没冲突"

**关联 PR**：PR-001（并列挂 OP）+ PR-008（时序）+ PR-010（OP 13 lifecycle · onAddValidators 注册校验）

### 需求

业务方说："标品 his_save 已有 OrgHisEffDateContinuityValidator 校验生效日期连续性 · 但我们需要更严的：补录前查同 boid 现有版本 · 如果 bsed 落在某个现有版本的 [bsed, bsled] 区间内 · 直接拒绝（不允许重叠）。"

### 推荐方案

- **扩展点**：`onAddValidators@his_save` · 并列挂新 OP · 在 onAddValidators 加新 Validator
- **风险**：中（与标品 10 个 Validator 并存 · 不要重复校验）

### 调用链

反编译实证：`AdminOrgInitSaveOp` 是 his_save 唯一执行链 · 注册 10 个 OrgHis*Validator（详见 02_business_rules.md §5）。

ISV 并列挂新 OP（继承 HRDataBaseOp）· 在 onAddValidators 加自己的 Validator · 跟标品 10 个并列跑 · 任意一个失败 → 整事务终止。

### 扩展入口坐标

- 绑定表单：`haos_adminorghis`
- 绑定操作：`his_save`
- 推荐父类：**`HRDataBaseOp`**（@SdkPlugin 白名单 · HR 通用 OP）· **禁继承 `AdminOrgInitSaveOp`**（标品场景专属 · PR-001）· **禁继承 `AbsOrgBaseOp`**（haos 域基类 · `cosmic_hr_sdk_whitelist_audit.md` 明确不在白名单）
- Validator 父类：**`AbstractValidator`**（kd.bos.entity.validate.AbstractValidator）

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.adminorghis.opplugin;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * 历史补录前的扩展校验：bsed 不能落在已有版本区间内
 * 遵循 PR-001 · 不继承 AdminOrgInitSaveOp（场景专属）· 并列挂新 OP
 * 遵循 PR-010 · onAddValidators 阶段注册 Validator（不能在 beforeExecute）
 */
public class TdkwHisSaveExtOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new TdkwHisBsedNoOverlapValidator());
    }
}
```

```java
package ${ISV_FLAG}.hrmp.haos.adminorghis.opplugin;

import java.util.Date;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 校验：补录的 bsed 不能落在同 boid 任何现有版本的 [bsed, bsled] 区间内
 * 遵循 PR-008 · 不带 iscurrentversion 过滤（既要查当前版本 · 又要查所有历史）
 * 遵循 PR-009 · 按 boid 查
 */
public class TdkwHisBsedNoOverlapValidator extends AbstractValidator {

    @Override
    public void validate() {
        ExtendedDataEntity[] entities = this.getDataEntities();
        if (entities == null || entities.length == 0) {
            return;
        }

        HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorg");

        for (ExtendedDataEntity ext : entities) {
            DynamicObject entity = ext.getDataEntity();
            long boid = entity.getLong("boid");
            Date newBsed = entity.getDate("bsed");
            Object newId = entity.get("id");  // 自己的 id（同 boid 不同版本要排除自己）

            if (boid <= 0L || newBsed == null) {
                continue;
            }

            // PR-009 · 按 boid 维度查全部已有版本
            QFilter qf = new QFilter("boid", "=", boid);
            qf.and(new QFilter("id", "!=", newId));  // 排除自己
            qf.and(new QFilter("bsed", "<=", newBsed));
            qf.and(new QFilter("bsled", ">=", newBsed));

            int conflictCount = helper.count("haos_adminorg", new QFilter[]{qf});
            if (conflictCount > 0) {
                this.addErrorMessage(ext, String.format(
                        "补录的生效日期 %s 与同组织（boid=%d）已有 %d 个版本时间区间冲突",
                        newBsed, boid, conflictCount));
            }
        }
    }
}
```

### 平台绑定方式

1. 苍穹开发平台 → 表单 `haos_adminorghis` · 操作 `his_save`
2. 注册扩展插件：`TdkwHisSaveExtOp` · 跟标品 `AdminOrgInitSaveOp` 并列跑（PR-001）
3. 部署生效

### 踩坑

- ❌ 继承 `AdminOrgInitSaveOp` 想 super.onAddValidators 后追加 → AdminOrgInitSaveOp 是场景专属 · 标品升级改方法签名你的代码炸（违反 PR-001）
- ❌ 在 `beforeExecuteOperationTransaction` 注册 Validator → 阶段晚了 · 校验执行已经过去（PR-010 · 必须 onAddValidators 阶段）
- ❌ 在 Validator 里直接调 `getModel()` → OP 端没有 model · NPE（PR-003 · OP 用 entity.set / entity.get）
- ❌ 用 `throw new KDBizException(...)` 阻断 → 整批数据全失败 · 不能定位到行 · 推荐用 `addErrorMessage(ext, msg)` 单行级错误
- ⚠ count 查询是单 boid 一次 IO · 大批量（>1000）补录会有 N+1 性能问题 · 优化：先按 boid 集合一次性查所有已存在区间 · 再在内存对比
- ⚠ 标品 OrgHisEffDateContinuityValidator 已经做"区间连续性"校验 · 与本扩展存在语义重叠 · 业务方需要拍板：
  - 选项 A：保留标品 + 本扩展并列（更严）
  - 选项 B：禁用标品 OrgHisEffDateContinuityValidator + 仅本扩展（更宽容 · 不推荐）

### 关联 PR

- 遵循 **PR-001** · 并列挂新 OP · 不继承 AdminOrgInitSaveOp / AbsOrgBaseOp
- 遵循 **PR-008** · Validator 内查询不带 iscurrentversion（要查全版本）
- 遵循 **PR-009** · 按 boid 业务维度查
- 遵循 **PR-010** · onAddValidators 注册 · 不在 beforeExecute

---

## CS-05 · 行级样式：当前生效版本（iscurrentversion=true）高亮

**关联 PR**：PR-001 · PR-008

### 需求

业务方说："历史查询列表能不能把当前生效版本（iscurrentversion=true）那一行高亮显示？让用户一眼看到'现在用的是哪个版本'。"

### 推荐方案

- **扩展点**：list form `haos_adminorghis` · 重写 `packageData` · 设行级样式
- **风险**：低

### 调用链

反编译实证：`HisModelListCommonPlugin.packageData L142-L165` 已经按 datastatus 决定行级按钮可见性 · 但**没有**改 row style。ISV 在标品后跑 · 加 row style 即可。

### 扩展入口坐标

- 绑定表单：`haos_adminorghis`
- 推荐父类：**`HRDataBaseList`**（@SdkPlugin 白名单）· **禁继承 `HisModelListCommonPlugin`**（@SdkInternal final）
- 关键重写方法：`packageData(PackageDataEvent event)` · 用 `event.getRowData()` 拿当前行 · 用 `getControl(...).setStyle(...)` 设样式

### 代码框架

```java
package ${ISV_FLAG}.hrmp.haos.adminorghis.listplugin;

import java.util.HashMap;
import java.util.Map;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.list.IListColumn;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 历史查询列表 · 当前生效版本行高亮
 * 遵循 PR-001 · 并列挂插件 · 不继承 HisModelListCommonPlugin
 * 遵循 PR-008 · 直接用行数据的 iscurrentversion 字段判断
 */
public class TdkwAdminOrgHisHighlightListPlugin extends HRDataBaseList {

    private static final String STYLE_CURRENT_VER = "background-color:#fff3cd;color:#856404;";

    @Override
    public void packageData(PackageDataEvent event) {
        super.packageData(event);

        if (event.getRowData() == null
                || !event.getRowData().containsProperty("iscurrentversion")) {
            return;
        }

        Boolean isCurrentVer = event.getRowData().getBoolean("iscurrentversion");

        // 注意：本场景标品 setFilter 已强制 iscurrentversion=false · 默认看不到当前版本
        // 这条逻辑只在 F7 模式或 ISV 自定义页（CS-03 全历史对比）会触发
        if (Boolean.TRUE.equals(isCurrentVer)) {
            // 设行级样式（具体 setStyle API 参考标品实现 · 这里示意）
            setRowStyle(event, STYLE_CURRENT_VER);
        }
    }

    private void setRowStyle(PackageDataEvent event, String style) {
        // 反编译实证：HisModelListCommonPlugin.packageData L142+ 用的是 OperationColItem.setVisible
        // 行级样式走 ListColumn 的 setBackColor / setForeColor · 实际 API 略
        // 这里根据 event.getColKey() 决定改哪一列的展示
        // ISV 落地前以反编译 ListColumn 类实际 API 为准
    }
}
```

### 踩坑

- ❌ 想在 setFilter 阶段做样式 → 时机不对 · 数据还没拿到 · 必须 packageData
- ❌ 给所有列都 setStyle → 性能差 · 只在第一列或 status 列加视觉提示即可
- ⚠ 本场景默认 list 看不到 iscurrentversion=true 的行（因为标品强制 iscurrentversion=false 过滤）· 这条样式逻辑实际只在以下几种场景触发：
  - F7 模式（HisModelF7ListPlugin 不强制过滤当前版本）
  - ISV 自建动态表单（CS-03）调用本插件
- ⚠ 颜色选择避免红绿色盲（用黄底深棕字组合）

### 关联 PR

- 遵循 **PR-001** · 不继承 HisModelListCommonPlugin
- 遵循 **PR-008** · 行数据已带 iscurrentversion 字段 · 直接读

---

## CS-06 · 跨场景跳转：从 admin_org_quick 列表点 boid → 跳本场景查全历史

**关联 PR**：PR-001 · PR-009

### 需求

业务方说："用户在 admin_org_quick_maintenance 列表点开某行 · 想直接看这个组织的全部历史 · 跳到本场景的 list（带 boid 过滤）。"

### 推荐方案

- **扩展对象**：admin_org_quick_maintenance 的列表 form `haos_adminorgdetail` 上加扩展按钮
- **扩展点**：并列挂新 ListPlugin · 实现 `itemClick` · 调 `getView().showForm(...)` 跳本场景
- **风险**：低

### 调用链

```
用户在 admin_org_quick 列表点扩展按钮"查看全历史"
   │
   ▼
ISV ListPlugin.itemClick (在 haos_adminorgdetail 上挂)
   │
   ├─ 拿当前焦点行的 boid
   │
   ▼
getView().showForm(BillShowParameter)
   formId = "haos_adminorghis"
   customParam.put("boid", boid)
   showType = MainNewTabPage
   │
   ▼
本场景列表加载 · 带 boid 过滤
   │
   ▼
[ISV 在 haos_adminorghis 上挂的 ListPlugin] setFilter
   读 customParam.boid → e.getQFilters().add(new QFilter("boid", "=", boid))
```

### 扩展入口坐标

#### 6a. admin_org_quick 侧（发起方）

- 绑定表单：`haos_adminorgdetail`（注：admin_org_quick 的数据实体）
- 推荐父类：**`HRDataBaseList`**
- 添加 toolbar 自定义按钮 · `addItemClickListeners(...)`

#### 6b. haos_adminorghis 侧（接收方）

- 绑定表单：`haos_adminorghis`
- 推荐父类：**`HRDataBaseList`**
- 关键重写：`setFilter` · 读 `customParam.boid` · add QFilter

### 代码框架

发起方（admin_org_quick 侧）：

```java
package ${ISV_FLAG}.hrmp.haos.adminorg.listplugin;

import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.list.BillList;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * admin_org_quick 列表加扩展按钮 · 跳到 haos_adminorghis 看全历史
 * 遵循 PR-001 · 并列挂 · 不继承 OrgDetailList / AdminOrgDetailListPlugin
 * 遵循 PR-009 · 跨场景跳转传 boid（业务维度）· 不传 id（版本维度）
 */
public class TdkwOrgListJumpToHisPlugin extends HRDataBaseList {

    @Override
    public void registerListener(java.util.EventObject e) {
        super.registerListener(e);
        addItemClickListeners("${ISV_FLAG}_jumphis_btn");  // 扩展按钮 itemKey
    }

    @Override
    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);
        if (!"${ISV_FLAG}_jumphis_btn".equals(evt.getItemKey())) {
            return;
        }

        BillList list = getControl("billlistap");
        ListSelectedRow row = list.getSelectedRows().getFirstRow();
        if (row == null) {
            getView().showTipNotification("请先选一条数据");
            return;
        }

        // PR-009 · 拿 boid（业务维度）· 不要拿 id
        Object boid = row.getBillNo();  // 注：实际拿 boid 的方式以 row.getEntryPrimaryKeyValue
                                         // 或 helper.queryByPk(id).getLong("boid") 为准

        FormShowParameter params = new FormShowParameter();
        params.setFormId("haos_adminorghis");
        params.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        params.setCustomParam("boid", boid);
        params.setCaption("组织全历史 - " + row.getName());
        getView().showForm(params);
    }
}
```

接收方（haos_adminorghis 侧）：

```java
package ${ISV_FLAG}.hrmp.haos.adminorghis.listplugin;

import kd.bos.form.events.SetFilterEvent;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 接收来自 admin_org_quick 跳转的 boid 参数 · 收窄列表
 * 遵循 PR-001 · 并列挂 · 不动标品 iscurrentversion=false
 * 遵循 PR-009 · 按 boid 业务维度过滤
 */
public class TdkwAdminOrgHisBoidFilterListPlugin extends HRDataBaseList {

    @Override
    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);

        Object boidObj = getView().getFormShowParameter().getCustomParam("boid");
        if (boidObj == null) {
            return;
        }

        long boid = Long.parseLong(boidObj.toString());
        e.getQFilters().add(new QFilter("boid", QCP.equals, boid));
    }
}
```

### 踩坑

- ❌ 跨场景传 id（版本维度）→ 接收方按 id 过滤只能看到一行 · 失去"全历史"意义（PR-009）
- ❌ 在发起方 itemClick 里直接调 `BillShowParameter.setPkId(...)` → 走的是详情页 · 不是 list
- ❌ 接收方 setFilter 里覆盖 setQFilters → 标品 iscurrentversion=false 过滤丢了 · 显示当前版本（违反场景设计）
- ⚠ 跳转时 caption 拼组织名 → 可读性好 · 但要防 XSS（业务方填的 name 可能有特殊字符）· 用 `HRStringUtils.escape` 或类似工具
- ⚠ 接收方 list 显示的还是历史版本（iscurrentversion=false）· 用户可能困惑"我看不到当前版本？" → 配文档说明

### 关联 PR

- 遵循 **PR-001** · 双侧都并列挂插件 · 不继承标品 list 类
- 遵循 **PR-009** · boid 是业务维度 · 跨场景传值用 boid

---

## CS-07 · 历史查询列表加 ISV 自定义"当前版本"切换按钮

**关联 PR**：PR-001 · PR-008

### 需求

业务方说："历史查询页能不能加个 toggle 按钮 · 一键切换'仅历史 / 仅当前 / 全部'三种视图？"（虽然标品默认只显示历史版本 · 但有些用户想偶尔切换）

### 推荐方案

- **扩展点**：自建动态表单 + 列表表单嵌入 · 或在 haos_adminorghis 上加 toolbar 按钮 + setFilter 联动
- **风险**：中（要小心覆盖标品 setFilter 的 iscurrentversion=false）

### 调用链

```
用户点 toggle 按钮 → itemClick 触发
   │
   ▼
ISV 插件设 pageCache.put("${ISV_FLAG}_view_mode", "all" / "his" / "current")
   │
   ▼
listView.refresh()
   │
   ▼
[ISV setFilter] 读 pageCache.${ISV_FLAG}_view_mode
   ├─ "his" → 不动 · 标品默认（iscurrentversion=false）
   ├─ "current" → ❌ 不能 · 标品 final 已 add false · ISV 加 true 会 AND false 矛盾 · 永远空集
   └─ "all" → 同样矛盾 · 没法绕过
```

### ⚠ 设计陷阱

**标品 final 方法 `HisModelListCommonPlugin.setFilter` 已经写死 `iscurrentversion=false`** · ISV 任何 setFilter add 后 add 都是 AND 关系 · 不可能让 list 显示当前版本（true）。

**真正可行的方案**：toggle 按钮 → 不修改 setFilter · 而是**调 `getView().showForm` 跳到 admin_org_quick_maintenance 入口**实现"切到当前版本视图"。

### 推荐实现：toggle 按钮 = 跳转

```java
package ${ISV_FLAG}.hrmp.haos.adminorghis.listplugin;

import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ItemClickEvent;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 历史查询页 · "切到当前版本视图"按钮 → 跳 admin_org_quick_maintenance
 * 不在本场景内强改 iscurrentversion=false（标品 final 写死 · 不能改）
 */
public class TdkwAdminOrgHisToggleListPlugin extends HRDataBaseList {

    @Override
    public void registerListener(java.util.EventObject e) {
        super.registerListener(e);
        addItemClickListeners("${ISV_FLAG}_view_current_btn");
    }

    @Override
    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);
        if (!"${ISV_FLAG}_view_current_btn".equals(evt.getItemKey())) {
            return;
        }

        // 跳到 admin_org_quick_maintenance 的 list 入口
        FormShowParameter params = new FormShowParameter();
        params.setFormId("haos_adminorgtablist");  // 当前版本视图入口
        params.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        params.setCaption("行政组织维护-当前版本");
        getView().showForm(params);
    }
}
```

### 踩坑

- ❌ 想在本场景 setFilter 里反过来加 `iscurrentversion=true` 让显示当前版本 → 跟标品 false 是 AND 矛盾 · 永远空集
- ❌ 想在 setFilter 里 `e.getQFilters().removeIf(filter -> "iscurrentversion".equals(filter.getProperty()))` 干掉标品的 false 过滤 · 再 add 自己的 true → 标品 setFilter 是 final 方法 · 在 ISV setFilter 之**前**跑 · ISV 后干掉的话需要确认 RowKey 顺序（PR-002）· 实际不稳定 · 不推荐
- ✅ 推荐方案：toggle 按钮 = 跨场景跳转（CS-06 同源）· 不强行改 setFilter

### 关联 PR

- 遵循 **PR-001** · 不继承 HisModelListCommonPlugin
- 遵循 **PR-008** · 不绕过 iscurrentversion 过滤设计 · 用跨场景跳转方式实现"看当前"

---

## CS 总结表

| CS | 难度 | 价值 | 关联 PR | 备注 |
|---|---|---|---|---|
| CS-01 加自定义筛选字段 | 低 | 高 | PR-001 / PR-008 / PR-009 | 必须先在 haos_adminorg 主层加字段 |
| CS-02 默认排序定制 | 低 | 中 | PR-001 / PR-008 | 重写 beforeCreateListColumns |
| CS-03 全历史对比页 | 中 | 高 | PR-008 / PR-009 | 自建动态表单 · 不挂本场景 form |
| CS-04 拦截 his_save 加校验 | 中 | 高 | PR-001 / PR-008 / PR-009 / PR-010 | 与标品 10 个 Validator 并列 |
| CS-05 当前版本行高亮 | 低 | 中 | PR-001 / PR-008 | 实际只在 F7 + 自定义页触发 |
| CS-06 跨场景跳转 | 低 | 中 | PR-001 / PR-009 | boid 维度跨场景传值 |
| CS-07 视图切换 toggle | 中 | 低 | PR-001 / PR-008 | 不能强改 iscurrentversion=false · 改用跨场景跳 |

**未列入的（标品已做或不适合 ISV 扩展）**：
- ❌ ~~订阅历史查询事件~~ — 标品没发本场景的 BEC（grep 0 处）· 想订阅组织变更去 admin_org_quick_maintenance CS-04
- ❌ ~~批量补录历史~~ — 走标品 HIES 导入向导 chargepersonimpo_hr · 别自己造轮子
- ❌ ~~历史版本删除~~ — 不允许（违反时序模型 PR-008 设计）

---

## 总体注意事项

1. **物理表共享认知**：本场景跟 admin_org_quick_maintenance 共用 `t_haos_adminorg` · 字段类扩展挂 haos_adminorg 主层（不是 haos_adminorghis 视图）· 双场景自动可见
2. **HisModel 系列三个 final 类禁继承**：HisModelListCommonPlugin / HisModelF7ListPlugin / HisModelFormCommonPlugin（@SdkInternal）· ISV 一律走 HRDataBaseList / HRDataBaseEdit / HRDataBaseOp 抽象基类
3. **不发 BEC** · 反编译 grep 0 处 · CS-07 BEC 章节砍掉
4. **his_save 是唯一写入 opKey**（除 HIES 导入外）· 校验扩展走 onAddValidators（PR-010）
5. **iscurrentversion=false 标品兜底** · ISV 不要硬掰 · 跨场景跳转实现"看当前"

---

## 关联文档

- `02_business_rules.md` · §1.1 七大时序字段 / §5 his_save 10 Validator
- `03_model_design.md` · §2 物理表共享架构 / §5 ISV 扩展认知
- `07_ext_points.md` · 36 opKey 扩展点矩阵 + SDK 父类白名单
- `knowledge/_shared/platform_rules.json` · PR-001 / PR-008 / PR-009 / PR-010
- `knowledge/scenarios/admin_org_quick_maintenance/06_customization_solutions.md` · 当前版本场景 CS（必读对照）
- `knowledge/_sdk_audit/cosmic_hr_sdk_whitelist_audit.md` · SDK 白名单铁律
