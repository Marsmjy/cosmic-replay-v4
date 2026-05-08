# 推荐定制方案 · 组织变动明细查询（homs_orgchgrecord）

> **状态**：基于 2 反编译类（AdminOrgChgRecordListPlugin 654 行 + AdminOrgChgRecordBUListPlugin 22 行）+ 15 opKey + 平台 PR-001..011 实证
> **confidence**：verified
> **审计时间**：2026-04-27

所有方案遵循统一结构：**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

⚠ **本场景特殊性**：
1. **本场景 100% 只读** · 标品 15 opKey 全是查询/导出/翻页/刷新/关闭 · 没有任何写入语义。CS 重点是**列表展示美化 / 筛选定制 / 跨场景跳转 / 变动溯源能力扩展**。
2. **数据来源是配对场景** `homs_orgbatchchgbill` 生效驱动写入 · ISV 想监听"组织变动事件" → 不在本场景挂 · 去配对场景挂（详见 CS-06）。
3. **BEC 模式判定**：反编译产物 grep 结果 0 处 `triggerEventSubscribe / IEventService / EventServiceHelper` · **本场景标品没发任何 BEC 事件** · 不为凑数硬写 BEC 发布方 CS。需要订阅组织变更走配对场景的 audit/submiteffect afterTransDoOp 阶段挂 OP（见 CS-06 末尾的 BEC 注释）。
4. **PR-005 速查**：CS-04 自建子分录字段补录场景需要给 t_homs_orgchgdetail 子分录新行赋值 → 必须用 `kd.bos.id.ID.genLongId()` 生成主键 · 禁用 UUID/System.currentTimeMillis/select max+1。

---

## CS-01 · 给变动明细列表加 ISV 自定义筛选字段（最高频）

**关联 PR**：PR-001（并列挂插件）+ PR-009（boid 维度查询）

### 需求

业务方说："列表能不能按 ISV 自加字段（如 `${ISV_FLAG}_costcenter` 成本中心）筛变动？现在标品只能按变动场景 / 变动类型 / 变动原因这几个 BasedataField 筛 · 业务想按更多自定义维度筛。"

### 推荐方案

- **扩展对象**：列表 form `homs_orgchgrecord`
- **扩展点**：并列挂新 `ListPlugin` · 实现 `setFilter(SetFilterEvent)`
- **风险**：低（只追加 QFilter · 不动 OrgPermHelper / otclassify=1010 等核心过滤）

### 调用链

反编译实证：标品 setFilter 执行链有明确顺序：
```
HRDataBaseList.setFilter (super 父类 · 数据权限)
   ↓
AdminOrgChgRecordListPlugin.setFilter L155-L171
   ├─ replaceProperty (search* 代理字段映射)
   ├─ OrgPermHelper.getHrPermFilter (HR 数据权限)
   ├─ [可选] needshowdetail/needshowsingle 分支
   ├─ adminorg.otclassify.id = 1010 强制约束
   └─ setOrderBy
   ↓
[ISV 新插件 · 并列] setFilter (追加 ISV 自定义过滤)
```

ISV 插件挂在标品后跑 · 加自己的 QFilter · 跟标品的过滤链是 AND 关系。

### 扩展入口坐标

- 绑定表单：`homs_orgchgrecord`
- 推荐父类：**`HRDataBaseList`**（@SdkPlugin 白名单 · ISV 标准选择）· **不继承 `AdminOrgChgRecordListPlugin`**（标品场景专属 · 标品升级改方法签名 ISV 编译失败 · PR-001）· **禁继承 `AbstractBUListPlugin`**（haos 域 final · `cosmic_hr_sdk_whitelist_audit.md` 不在白名单）
- 关键重写方法：
  - `setFilter(SetFilterEvent e)` — `e.getQFilters().add(qf)` · 追加过滤
  - 注意是 add 不是 set（不要清掉标品已加的过滤）

### 代码框架

```java
package ${ISV_FLAG}.hrmp.homs.orgchgrecord.listplugin;

import kd.bos.context.RequestContext;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 变动明细列表追加 ISV 字段过滤（如成本中心维度）
 * 遵循 PR-001 · 并列挂插件 · 不继承 AdminOrgChgRecordListPlugin（标品场景专属）/ AbstractBUListPlugin（haos 域 final）
 * 遵循 PR-009 · 涉及组织字段时按 boid 维度查
 */
public class TdkwOrgChgRecordFilterListPlugin extends HRDataBaseList {

    @Override
    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);  // 跑 HRDataBaseList 默认数据权限

        // 1. 从 ShowParameter 拿用户选的成本中心值（假设新加自定义筛选字段 ${ISV_FLAG}_costcenter）
        Object ccParam = getView().getFormShowParameter()
                .getCustomParam("${ISV_FLAG}_costcenter_filter");
        if (ccParam != null) {
            // 通过 adminorg.boid 反查关联 t_haos_adminorg.${ISV_FLAG}_costcenter 字段
            // 注意：本场景的 adminorg 字段值是 boid（PR-009）· 不是 id
            QFilter ccFilter = new QFilter("adminorg.${ISV_FLAG}_costcenter", QCP.equals, ccParam);
            e.getQFilters().add(ccFilter);
        }

        // 2. 从 ShowParameter 拿用户选的多变动场景（标品 searchchangescene 是单值 · ISV 加多选）
        Object multiSceneParam = getView().getFormShowParameter()
                .getCustomParam("${ISV_FLAG}_multi_scene_ids");
        if (multiSceneParam instanceof java.util.Collection) {
            QFilter multiSceneFilter = new QFilter(
                    "orgchgentry.changescene.id",
                    QCP.in,
                    multiSceneParam
            );
            e.getQFilters().add(multiSceneFilter);
        }

        // 3. ISV 自加"操作时间近 30 天"快捷筛
        Object recentParam = getView().getFormShowParameter()
                .getCustomParam("${ISV_FLAG}_recent_30days");
        if (Boolean.TRUE.equals(recentParam)) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(java.util.Calendar.DAY_OF_MONTH, -30);
            e.getQFilters().add(new QFilter(
                    "orgchgentry.operationtime",
                    QCP.large_equals,
                    cal.getTime()
            ));
        }
    }
}
```

### 平台绑定方式

1. 苍穹开发平台 → 表单 `homs_orgchgrecord`
2. 注册插件 · `targetType = LIST_FORM`（R20 大写枚举）
3. 部署生效

如需"自定义筛选字段"在搜索面板可见 · 需在 `homs_orgchgrecord` 上 modifyMeta 加 isv 字段（建议在 `haos_adminorgdetail` 主层加 · 双场景共用）· 参考 [03_model_design.md §8.1](03_model_design.md)。

### 踩坑

- ❌ 想去掉标品的 `adminorg.otclassify.id=1010` 让本场景显示项目组织变动 → 改不掉（标品 setFilter 在 ISV 之前跑）· 数组顺序很难绕过（违反 PR-001）
- ❌ 在 setFilter 里 `e.setQFilters(new ArrayList<>())` 清空 → 标品权限过滤全丢 · 数据安全问题
- ❌ 继承 `AdminOrgChgRecordListPlugin` 想 super 然后追加 → 标品类是 `public final class`（反编译 L117 实证）· 编译过不去（违反 PR-001）
- ❌ ISV 加新字段 `${ISV_FLAG}_costcenter` 的 modifyMeta 直接打到 `homs_orgchgrecord` 视图 → 视图层独立扩展 · 不写物理表 · 实际过滤 SQL 失败（详见 [03_model_design.md §8.1](03_model_design.md)）。**正确：modifyMeta 打到 `haos_adminorgdetail` 主层**
- ⚠ 大数据量场景（>1M 行变动）· DBA 配合给 `t_homs_orgchgentry.fchgeffecttime` / `t_homs_orgchgentry.foperationtime` 建索引 · 不然按时间筛会全表扫超时
- ⚠ ISV 加的 `${ISV_FLAG}_multi_scene_ids` 集合做 in 查询 · 集合 size 必须 ≤ 1000（部分数据库 in 上限）

### 关联 PR

- 遵循 **PR-001** · ISV 扩展走并列挂 · 禁继承标品场景专属类（AdminOrgChgRecordListPlugin / AbstractBUListPlugin）
- 遵循 **PR-009** · adminorg 字段值是组织 boid · 不是 id

---

## CS-02 · 默认排序定制（按变动操作时间 DESC 替代变动生效日期 DESC）

**关联 PR**：PR-001（并列挂）

### 需求

业务方说："标品默认按 `chgeffecttime DESC` 排 · 但我们审计部门更关心'谁什么时候做的' · 想改成按 `operationtime DESC` 排。"或"我们想加'按变动场景分组' 的视觉效果。"

### 推荐方案

- **扩展对象**：列表 form `homs_orgchgrecord`
- **扩展点**：并列挂新 ListPlugin · 重写 `beforeCreateListColumns` · 用 `IListColumn.setOrder` 覆盖标品默认排序
- **风险**：低（只调 setOrder · 不动数据）

### 调用链

反编译实证：标品 `AdminOrgChgRecordListPlugin.beforeCreateListColumns L224-L229` 仅做了"adminorg.number / adminorg.name 设为固定列"· 排序是通过 `setFilter L170` 的 `setOrderBy` 写死。

ISV 想改 → 在标品后并列跑 · 拿到 ListColumn 集合再覆盖 setOrder（**注意**：setOrder 优先级 > setOrderBy · 标品最后赢的设计）。

### 扩展入口坐标

- 绑定表单：`homs_orgchgrecord`
- 推荐父类：**`HRDataBaseList`**（@SdkPlugin 白名单）· **禁继承 `AdminOrgChgRecordListPlugin`**（final · 标品场景专属）· **禁继承 `AbstractBUListPlugin`**（haos 域 final · 不在白名单）
- 关键重写方法：
  - `beforeCreateListColumns(BeforeCreateListColumnsArgs args)` — 在 args.getListColumns() 上后处理 setOrder

### 代码框架

```java
package ${ISV_FLAG}.hrmp.homs.orgchgrecord.listplugin;

import kd.bos.entity.filter.SortType;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.list.IListColumn;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 变动明细列表排序定制：operationtime DESC 优先（审计场景需求）
 * 遵循 PR-001 · 并列挂插件 · 标品 AdminOrgChgRecordListPlugin 跑完后 · 本插件覆盖 ListColumn.setOrder
 */
public class TdkwOrgChgRecordSortListPlugin extends HRDataBaseList {

    @Override
    public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
        super.beforeCreateListColumns(args);

        // 第一步：清掉所有列的排序
        args.getListColumns().forEach(col -> col.setOrder(SortType.NotOrder.name()));

        // 第二步：operationtime DESC 优先（审计场景）
        args.getListColumns().stream()
                .filter(c -> HRStringUtils.equals(c.getListFieldKey(), "orgchgentry.operationtime"))
                .findFirst()
                .ifPresent(c -> c.setOrder(SortType.DESC.name()));

        // 第三步：备用排序键 chgeffecttime DESC（同操作时间时按业务生效日期）
        args.getListColumns().stream()
                .filter(c -> HRStringUtils.equals(c.getListFieldKey(), "orgchgentry.chgeffecttime"))
                .findFirst()
                .ifPresent(c -> c.setOrder(SortType.DESC.name()));

        // 第四步：第三排序 · adminorg.number ASC（同时间时按组织聚合）
        args.getListColumns().stream()
                .filter(c -> HRStringUtils.equals(c.getListFieldKey(), "adminorg.number"))
                .findFirst()
                .ifPresent(c -> c.setOrder(SortType.ASC.name()));
    }
}
```

### 踩坑

- ❌ 在 setFilter 里 `setFilterEvent.setOrderBy(...)` 想覆盖标品排序 → 标品 L170 也调用了 setOrderBy · ISV 在 super.setFilter 之后调最后赢 · **但**两套排序键打架时 ListColumn.setOrder 优先 · 推荐用 setOrder 不用 setOrderBy
- ❌ 继承 `AdminOrgChgRecordListPlugin` 想 super 然后覆盖 → final 类编不过
- ⚠ 用户在列表点列头排序仍然有效（用户操作优先）· ISV 设的是默认排序
- ⚠ `orgchgentry.operationtime` 是分录字段路径 · setOrder 在某些苍穹版本对分录字段支持不一致 · 部署前测试一下

### 关联 PR

- 遵循 **PR-001** · 不继承标品 list final 类

---

## CS-03 · 列表 hyperLinkClick 跳转扩展（双跳：申请单详情 + 行政组织详情）

**关联 PR**：PR-001 · PR-009

### 需求

业务方说："标品点变动记录只能跳到申请单详情（homs_orgbatchchgbill）。我想加另一种跳转：点'组织'列直接跳到该组织的当前版本详情（haos_adminorgdetail）· 让审计员一键看到'变动当时的组织 vs 现在的组织'对比。"

### 推荐方案

- **扩展对象**：列表 form `homs_orgchgrecord`
- **扩展点**：并列挂新 ListPlugin · 重写 `billListHyperLinkClick(HyperLinkClickArgs)` · 按当前点击的列 key 决定跳转目标
- **风险**：中（标品 hyperLinkClick L201 第一行就 `args.setCancel(true)` · 全部默认跳转都被取消 · ISV 必须谨慎处理）

### 调用链

反编译实证：标品 `AdminOrgChgRecordListPlugin.billListHyperLinkClick L200-L218`：
```
billListHyperLinkClick (args)
   │
   ├─ args.setCancel(true)              ← 阻止默认跳转
   ├─ 拿 entryPkId
   ├─ 查 chgbill (申请单 ID)
   └─ showForm("homs_orgbatchchgbill")  ← 固定跳申请单详情
```

ISV 在并列插件中**第一个跑** · 可在标品之前判断"用户点的是哪一列" · 决定走哪条跳转分支。

### 扩展入口坐标

- 绑定表单：`homs_orgchgrecord`
- 推荐父类：**`HRDataBaseList`**（@SdkPlugin 白名单）· **禁继承 `AdminOrgChgRecordListPlugin`**（final）
- 关键重写方法：
  - `billListHyperLinkClick(HyperLinkClickArgs args)` — 拦截超链接点击 · 按列 key 分支
  - 注意 `args.setCancel(true)` 后标品代码不会再跑（因为 final 标品在 L201 也是 setCancel · ISV 在标品之前跑就能控制）

### 代码框架

```java
package ${ISV_FLAG}.hrmp.homs.orgchgrecord.listplugin;

import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BillListHyperLinkClickEvent;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 变动明细列表 hyperLinkClick 双跳扩展
 * 遵循 PR-001 · 并列挂 · 不继承 AdminOrgChgRecordListPlugin（final）
 * 遵循 PR-009 · 跳到行政组织详情时按 boid 查当前版本
 */
public class TdkwOrgChgRecordHyperLinkPlugin extends HRDataBaseList {

    @Override
    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        // 拿点击的列 key（如 "adminorg.name" / "orgchgentry.chgbill.billno"）
        BillListHyperLinkClickEvent event = (BillListHyperLinkClickEvent) args.getHyperLinkClickEvent();
        String fieldName = event.getFieldName();

        // 分支 1：用户点"组织"列 → ISV 自加跳转 · 跳到组织当前版本详情
        if ("adminorg.name".equals(fieldName) || "adminorg.number".equals(fieldName)) {
            args.setCancel(true);  // 阻止标品跳转
            jumpToCurrentAdminOrg(event.getCurrentRow());
            return;
        }

        // 分支 2：用户点其他列 → 不动 · 让标品走默认跳申请单详情逻辑
        // （这里不调 setCancel · super.billListHyperLinkClick 由插件链下一个标品执行）
    }

    private void jumpToCurrentAdminOrg(ListSelectedRow currentRow) {
        // 拿当前行的 adminorg 字段（值是组织 boid · PR-009）
        Object adminorgBoid = currentRow.getPrimaryKeyValue();  // 注：实际拿 boid 方式以行选中字段为准
        if (adminorgBoid == null) {
            getView().showTipNotification("无法获取组织 ID");
            return;
        }

        // PR-009 · 按 boid 查当前版本（haos_adminorgdetail · iscurrentversion=true）
        HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorgdetail");
        QFilter qf = new QFilter("boid", "=", adminorgBoid)
                .and(new QFilter("iscurrentversion", "=", Boolean.TRUE));
        DynamicObject dy = helper.queryOne("id,name,number", new QFilter[]{qf});

        if (dy == null) {
            getView().showTipNotification("该组织当前版本不存在（可能已被废弃）");
            return;
        }

        // 跳到组织详情页 · VIEW 状态
        BillShowParameter param = new BillShowParameter();
        param.setFormId("haos_adminorgdetail");
        param.setPkId(dy.getLong("id"));
        param.setBillStatus(BillOperationStatus.VIEW);
        param.setStatus(OperationStatus.VIEW);
        param.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        param.setCaption("组织详情 - " + dy.getString("name"));
        getView().showForm(param);
    }
}
```

### 踩坑

- ❌ 在 ISV 插件第一行就 `args.setCancel(true)` 不分列 → 所有列点击都被本 ISV 拦截 · 标品默认跳申请单逻辑跑不到 · 用户体验差
- ❌ 跳到 haos_adminorgdetail 用 id 直接 setPkId（`row.getEntryPrimaryKeyValue` 拿到的是 entry 的 id 不是 adminorg 的 boid）→ 拿到错的 ID · 跳错版本（违反 PR-009）
- ❌ 不查 iscurrentversion=true 直接拿 boid 当 id 跳转 → 拿不到当前版本 · 跳到任意一个历史版本（PR-008 + PR-009）
- ⚠ 用户没有跳转目标的"组织数据权限"时 · 跳过去会被 haos_adminorgdetail 的 setFilter 过滤掉 · 显示空 · ISV 应在跳转前先检查权限并提示
- ⚠ caption 拼组织名时要防 XSS（用户填的组织名可能有特殊字符）· 实际生产请用 `HRStringUtils.escape` 或类似工具

### 关联 PR

- 遵循 **PR-001** · 并列挂插件 · 不继承 AdminOrgChgRecordListPlugin
- 遵循 **PR-008** · 跳到当前版本必须 iscurrentversion=true 过滤
- 遵循 **PR-009** · 跨场景跳转传 boid（业务维度）· 不传 id（版本维度）

---

## CS-04 · 字段级变动溯源美化（高亮重要变动 / 自定义实体溯源 / 自建子分录补录）

**关联 PR**：PR-001（并列挂）+ PR-005（ID.genLongId 给自建子分录新行赋值 ⭐ 强制场景）+ PR-009（boid 维度）

### 需求

业务方说："变动明细子分录里有些字段（如 `belongcompany`/`adminorgtype`）是关键字段 · 变了一定要审计员注意。能不能让这些字段的变动行用红色高亮 · 其他字段灰色弱化？"或"我们扩展了行政组织新字段（`${ISV_FLAG}_costcenter`）· 配对场景写入端已经能写 chgentitynumber=haos_adminorgdetail/chgpageelement=${ISV_FLAG}_costcenter 到 t_homs_orgchgdetail · 但本场景溯源页 changefield 列显示不出来 / before/after 计算缺失 · 怎么办？"

### 推荐方案

- **扩展对象**：列表 form `homs_orgchgrecord`
- **扩展点**：并列挂新 ListPlugin · 重写 `packageData(PackageDataEvent event)` · 在标品计算的 changeMap 之外补充 ISV 自定义渲染逻辑
- **风险**：中（要小心不要污染标品 changeMap · 本场景 packageData 调用频次高 · 性能要严格控制）
- **PR-005 强制场景**：方案 B 自建子分录补录 · 必须用 `kd.bos.id.ID.genLongId()` 给新行赋 id

### 方案 A · 高亮重要变动行（packageData 重写）

#### 调用链

```
[标品 AdminOrgChgRecordListPlugin.packageData L304-L324]
   ├─ 处理 mergesplitview / parentlongname
   ├─ 调 formatTextValue 处理 changefield/before/after/before/afternamenumber 计算列 (L321-L323)
   └─ event.setFormatValue(...)
   ↓
[ISV 新 ListPlugin · 并列] packageData
   ├─ super.packageData(event)
   └─ 拿 event.getRowData().getString("chgpageelement") 判断是否关键字段
        └─ 是关键字段 → 在 event 上做样式标记
```

#### 代码框架

```java
package ${ISV_FLAG}.hrmp.homs.orgchgrecord.listplugin;

import java.util.Set;
import com.google.common.collect.ImmutableSet;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.list.column.TextColumnDesc;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 变动明细列表 · 关键字段变动高亮
 * 遵循 PR-001 · 并列挂 · 不继承 AdminOrgChgRecordListPlugin（final）
 */
public class TdkwOrgChgRecordHighlightListPlugin extends HRDataBaseList {

    /** 关键字段集（业务方圈定） */
    private static final Set<String> CRITICAL_FIELDS = ImmutableSet.of(
            "belongcompany",  // 所属公司
            "corporateorg",   // 法律实体
            "adminorgtype",   // 行政组织类型
            "parentorg",      // 上级组织
            "enable"          // 启用状态
    );

    private static final String STYLE_CRITICAL = "background-color:#fff3cd;color:#a94442;font-weight:bold;";

    @Override
    public void packageData(PackageDataEvent event) {
        super.packageData(event);

        if (!(event.getSource() instanceof TextColumnDesc)) {
            return;
        }

        DynamicObject rowData = event.getRowData();
        if (rowData == null) {
            return;
        }

        // 判断本行的 chgpageelement 是不是关键字段
        if (!rowData.getDynamicObjectType().getProperty("orgchgentry.subentryentity.chgpageelement") != null) {
            return;
        }
        String chgpageelement = rowData.getString("orgchgentry.subentryentity.chgpageelement");
        if (CRITICAL_FIELDS.contains(chgpageelement)) {
            // 设行级样式（不同苍穹版本 API 略不同 · 实际部署以 ListColumn.setBackColor 等 API 为准）
            applyRowStyle(event, STYLE_CRITICAL);
        }
    }

    private void applyRowStyle(PackageDataEvent event, String style) {
        // 反编译实证：标品 ListColumn 行级样式走 setBackColor / setForeColor
        // 这里根据 event.getColKey() 决定改哪一列的展示
        // ISV 落地前以反编译 ListColumn 类实际 API 为准
    }
}
```

### 方案 B · 自定义 chgentitynumber 溯源（扩展 buildData 行为）

如果 ISV 在配对场景 `homs_orgbatchchgbill` 加了写入新 chgentitynumber 的逻辑（如 `${ISV_FLAG}_orgmatrix` 自建矩阵实体）· 本场景 buildData 不识别此分支 · changefield/before/after 列显示空。

需要：
- 重写 `beforePackageData` · 在 super 之后**追加**对自定义 chgentitynumber 的查询
- 重写 `packageData` · 渲染 ISV 自计算的 ChangeDetailVO

#### 代码框架

```java
package ${ISV_FLAG}.hrmp.homs.orgchgrecord.listplugin;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.events.BeforePackageDataEvent;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.list.column.TextColumnDesc;
import kd.bos.id.ID;  // PR-005 · 自建子表新行 id 用这个
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 变动明细列表 · 自定义 chgentitynumber=${ISV_FLAG}_orgmatrix 溯源补充
 * 遵循 PR-001 · 并列挂 · 不继承标品 final
 * 遵循 PR-005 · 自建子分录补录新行的 id 用 ID.genLongId（如方案 B 启用）
 */
public class TdkwOrgChgRecordCustomTraceListPlugin extends HRDataBaseList {

    private static final String CUSTOM_CHG_ENTITY = "${ISV_FLAG}_orgmatrix";

    /** 自建本插件维护的"自定义实体变动"映射 · 模仿标品 changeMap */
    private final Map<Long, TdkwChangeDetailVO> tdkwChangeMap = new HashMap<>();

    @Override
    public void beforePackageData(BeforePackageDataEvent event) {
        super.beforePackageData(event);  // 标品先跑 · 处理 haos_orgteamcooprel / ADMIN_STRUCT_KEY / haos_adminorgdetail 三类

        DynamicObjectCollection pageData = event.getPageData();
        if (pageData == null || pageData.isEmpty()) {
            return;
        }

        // 抽出本页的子分录 detail id 列表
        List<Long> detailIds = pageData.stream()
                .map(dy -> dy.getLong("orgchgentry.subentryentity.id"))
                .filter(id -> id != null && id > 0L)
                .collect(Collectors.toList());

        if (detailIds.isEmpty()) {
            return;
        }

        // 查 t_homs_orgchgdetail 拿本页中 chgentitynumber=${ISV_FLAG}_orgmatrix 的 detail
        HRBaseServiceHelper detailHelper = new HRBaseServiceHelper("homs_subentryentity");
        DynamicObject[] customDetails = detailHelper.query(
                "id,chgpageelement,beforechgentity,afterchgentity",
                new QFilter[]{
                        new QFilter("id", "in", detailIds),
                        new QFilter("chgentitynumber", "=", CUSTOM_CHG_ENTITY)
                }
        );

        if (customDetails == null || customDetails.length == 0) {
            return;
        }

        // 按 entityIds 集合查自定义实体真表 · 拿 before/after
        // ⚠ ISV 自建实体 ${ISV_FLAG}_orgmatrix 的真表名 / 字段查询逻辑由 ISV 自己实现
        // 这里只示意结构 · 实际查询请按 ISV 实体定义
        for (DynamicObject d : customDetails) {
            long detailId = d.getLong("id");
            TdkwChangeDetailVO vo = new TdkwChangeDetailVO();
            vo.setDisplayName(d.getString("chgpageelement"));
            vo.setBeforeValue(loadCustomEntityValue(d.getLong("beforechgentity"), d.getString("chgpageelement")));
            vo.setAfterValue(loadCustomEntityValue(d.getLong("afterchgentity"), d.getString("chgpageelement")));
            tdkwChangeMap.put(detailId, vo);
        }
    }

    @Override
    public void packageData(PackageDataEvent event) {
        super.packageData(event);  // 标品先跑（处理已知 chgentitynumber）

        if (!(event.getSource() instanceof TextColumnDesc)) {
            return;
        }

        // 标品已经处理过的列 · 如果 changeMap 没值 · ISV 这里补
        DynamicObject rowData = event.getRowData();
        if (rowData == null) {
            return;
        }

        Long detailId = rowData.getLong("orgchgentry.subentryentity.id");
        TdkwChangeDetailVO vo = tdkwChangeMap.get(detailId);
        if (vo == null) {
            return;
        }

        String key = ((TextColumnDesc) event.getSource()).getKey();
        switch (key) {
            case "changefield":
                event.setFormatValue(vo.getDisplayName());
                break;
            case "beforevalue":
                event.setFormatValue(vo.getBeforeValue());
                break;
            case "aftervalue":
                event.setFormatValue(vo.getAfterValue());
                break;
            default:
                break;
        }
    }

    /** ISV 自定义实体的字段查询（实现略） */
    private String loadCustomEntityValue(long entityId, String fieldKey) {
        // 实际从 t_tdkw_orgmatrix 表查
        return "";
    }

    /** 简化版 ChangeDetailVO 模型 */
    private static class TdkwChangeDetailVO {
        private String displayName;
        private String beforeValue;
        private String afterValue;

        public String getDisplayName() { return displayName; }
        public void setDisplayName(String v) { this.displayName = v; }
        public String getBeforeValue() { return beforeValue; }
        public void setBeforeValue(String v) { this.beforeValue = v; }
        public String getAfterValue() { return afterValue; }
        public void setAfterValue(String v) { this.afterValue = v; }
    }

    /**
     * 仅当业务真需要"在本场景手工补录变动 detail"时使用
     * （极罕见 · 一般走配对场景 homs_orgbatchchgbill 申请单流程）
     * 遵循 PR-005 · 自建子表新行 id 用 ID.genLongId（不要 UUID/System.currentTimeMillis/select max+1）
     */
    public DynamicObject createTdkwOrgChgDetail(Long entryId, String chgEntityNumber, String pageElement) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper("homs_subentryentity");
        DynamicObject newRow = helper.generateEmptyDynamicObject();
        newRow.set("id", ID.genLongId());  // ⭐ PR-005 · 必须用平台 ID 生成
        newRow.set("entryid", entryId);
        newRow.set("chgentitynumber", chgEntityNumber);
        newRow.set("chgpageelement", pageElement);
        return newRow;
    }
}
```

### 踩坑

- ❌ 直接污染标品的 `changeMap`（拿标品的 ConcurrentHashMap 引用 add 进去）→ 反射操作 · 标品升级方法签名变更后 ISV 编译失败（违反 PR-001）· 推荐独立维护 `tdkwChangeMap`
- ❌ 在 packageData 里查数据库 → 单行调用 N 次 IO · 大数据量场景超时（O(N) 性能问题）· 必须在 beforePackageData 阶段批量查 · 缓存到内存
- ❌ 标品 changeMap 处理顺序：先调 super 再 ISV · 否则覆盖标品已设的 formatValue
- ❌ 自建子分录新行 id 用 `UUID.randomUUID().toString()` / `System.currentTimeMillis()` → 不是 long 类型 · 跨表 join 失败（违反 PR-005）· **必须 `kd.bos.id.ID.genLongId()`**
- ⚠ 关键字段集合（CRITICAL_FIELDS）建议挂在配置项 / 基础资料 · 不要硬编码（业务方调整频繁）
- ⚠ 行级样式 API 在不同苍穹版本上略有差异（setBackColor / setStyle / setBackgroundColor）· 落地前先在反编译实例上验证 API 有效性

### 关联 PR

- 遵循 **PR-001** · 并列挂 · 不继承 AdminOrgChgRecordListPlugin
- 遵循 **PR-005** · 自建子分录新行 id 必须用 `ID.genLongId()`
- 遵循 **PR-009** · 涉及组织字段时按 boid 维度

---

## CS-05 · 复合筛选弹窗（按时间范围 + 多变动场景 + 组织树）

**关联 PR**：PR-001（并列挂）+ PR-009（boid）

### 需求

业务方说："标品搜索面板太单薄了 · 一次只能选 1 个变动场景 / 1 个时间点 / 1 个组织。能不能加一个'高级筛选'按钮 · 弹窗里能选时间范围 + 多变动场景 + 多组织树勾选 + 多变动类型 · 然后一次性应用？"

### 推荐方案

- **扩展对象**：自建动态表单 `${ISV_FLAG}_orgchgrecord_advfilter`（不挂在 homs_orgchgrecord 上）+ list form `homs_orgchgrecord` 上加 toolbar 按钮触发弹窗 · 弹窗确认后 reload list 带 customParam
- **扩展点**：
  - list form 上挂 ListPlugin 实现 `itemClick(ItemClickEvent)` + `setFilter(SetFilterEvent)`
  - 自建动态表单走 `HRDynamicFormBasePlugin` · 实现 `click("ok_btn")`
- **风险**：中（要小心 listView.refresh() 传参方式）

### 调用链

```
用户点列表 toolbar 上的"高级筛选"按钮
   │
   ▼
[ISV ListPlugin.itemClick]
   ├─ getView().showForm(自建动态表单 ${ISV_FLAG}_orgchgrecord_advfilter)
   └─ 设 CloseCallBack（弹窗关闭后接收返回值）
   ↓
[自建动态表单 · TdkwAdvFilterFormPlugin]
   ├─ 用户填入：时间范围 / 多场景 / 多组织 / 多类型
   └─ 点确认 → returnDataToParent(filterParams) + close
   ↓
[ISV ListPlugin.closedCallBack]
   ├─ 拿到 filterParams · 写入 pageCache
   └─ listView.refresh()
   ↓
[ISV ListPlugin.setFilter]
   └─ 读 pageCache.advFilter · add 多个 QFilter
```

### 扩展入口坐标

#### 5a. list 侧

- 绑定表单：`homs_orgchgrecord`
- 推荐父类：**`HRDataBaseList`**
- 添加 toolbar 自定义按钮 · `addItemClickListeners(...)`
- 关键重写：`itemClick` / `closedCallBack` / `setFilter`

#### 5b. 高级筛选弹窗侧

- 自建动态表单 formId：`${ISV_FLAG}_orgchgrecord_advfilter`
- 推荐父类：**`HRDynamicFormBasePlugin`**（@SdkPlugin 白名单）
- 关键重写：`click("ok_btn")`

### 代码框架

list 侧：

```java
package ${ISV_FLAG}.hrmp.homs.orgchgrecord.listplugin;

import java.util.EventObject;
import java.util.List;
import java.util.Map;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * 变动明细列表 · 高级筛选 toolbar
 * 遵循 PR-001 · 并列挂 · 不继承 AdminOrgChgRecordListPlugin（final）
 * 遵循 PR-009 · 多组织筛选传 boid 集合
 */
public class TdkwOrgChgRecordAdvFilterPlugin extends HRDataBaseList {

    private static final String CACHE_KEY = "${ISV_FLAG}_advfilter";
    private static final String BTN_KEY = "${ISV_FLAG}_advfilter_btn";
    private static final String CALLBACK_ID = "${ISV_FLAG}_advfilter_callback";

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        addItemClickListeners(BTN_KEY);
    }

    @Override
    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);
        if (!BTN_KEY.equals(evt.getItemKey())) {
            return;
        }

        FormShowParameter param = new FormShowParameter();
        param.setFormId("${ISV_FLAG}_orgchgrecord_advfilter");
        param.getOpenStyle().setShowType(ShowType.Modal);
        param.setCloseCallBack(new CloseCallBack(this, CALLBACK_ID));
        getView().showForm(param);
    }

    @Override
    public void closedCallBack(ClosedCallBackEvent event) {
        super.closedCallBack(event);
        if (!CALLBACK_ID.equals(event.getActionId())) {
            return;
        }

        Object returnData = event.getReturnData();
        if (returnData == null) {
            return;
        }

        // 序列化存入 pageCache · setFilter 阶段读
        getPageCache().put(CACHE_KEY, SerializationUtils.toJsonString(returnData));
        // 触发 list 刷新
        getControl("billlistap").refresh();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);

        String cached = getPageCache().get(CACHE_KEY);
        if (cached == null || cached.isEmpty()) {
            return;
        }

        Map<String, Object> params = (Map<String, Object>) SerializationUtils.fromJsonString(cached, Map.class);

        // 时间范围
        if (params.get("startDate") != null) {
            e.getQFilters().add(new QFilter("orgchgentry.chgeffecttime", QCP.large_equals, params.get("startDate")));
        }
        if (params.get("endDate") != null) {
            e.getQFilters().add(new QFilter("orgchgentry.chgeffecttime", QCP.less_equals, params.get("endDate")));
        }

        // 多变动场景
        if (params.get("sceneIds") instanceof List) {
            List<Long> sceneIds = (List<Long>) params.get("sceneIds");
            if (!sceneIds.isEmpty()) {
                e.getQFilters().add(new QFilter("orgchgentry.changescene.id", QCP.in, sceneIds));
            }
        }

        // 多变动类型
        if (params.get("typeIds") instanceof List) {
            List<Long> typeIds = (List<Long>) params.get("typeIds");
            if (!typeIds.isEmpty()) {
                e.getQFilters().add(new QFilter("orgchgentry.changetype.id", QCP.in, typeIds));
            }
        }

        // 多组织（PR-009 · 传 boid 集合）
        if (params.get("orgBoids") instanceof List) {
            List<Long> orgBoids = (List<Long>) params.get("orgBoids");
            if (!orgBoids.isEmpty()) {
                e.getQFilters().add(new QFilter("adminorg", QCP.in, orgBoids));
            }
        }
    }
}
```

弹窗侧（简化）：

```java
package ${ISV_FLAG}.hrmp.homs.orgchgrecord.formplugin;

import java.util.HashMap;
import java.util.Map;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin;

/**
 * 高级筛选弹窗
 * 遵循 PR-001 · 自建动态表单走 HRDynamicFormBasePlugin
 */
public class TdkwAdvFilterFormPlugin extends HRDynamicFormBasePlugin {

    @Override
    public void preOpenForm(PreOpenFormEventArgs e) {
        super.preOpenForm(e);
        // 加载默认值（如最近 30 天）
    }

    @Override
    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        if ("ok".equals(args.getOperateKey())) {
            Map<String, Object> result = new HashMap<>();
            result.put("startDate", getModel().getValue("startdate"));
            result.put("endDate", getModel().getValue("enddate"));
            // ... 填其他字段
            getView().returnDataToParent(result);
            getView().close();
        }
    }
}
```

### 踩坑

- ❌ 弹窗用 `setShowType(MainNewTabPage)` → 应该用 Modal · MainNewTabPage 弹窗后关闭流程不对 · returnData 拿不到
- ❌ pageCache 不清理 → 用户取消高级筛选后 cache 还在 · 影响后续查询。建议在 itemClick 里加"清除筛选"按钮 · 把 cache.remove(CACHE_KEY)
- ❌ 多组织筛选传 id（版本维度）→ 只匹配某个版本 · 不匹配业务对象（违反 PR-009）
- ⚠ 弹窗的"组织选择控件"建议用 HRAdminOrgField 或 BasedataField 引用 haos_adminorghrf7 · 平台天然支持权限过滤 · 不要自己造轮子
- ⚠ 多场景 / 多类型 / 多组织 集合做 in 查询 · 集合 size 必须 ≤ 1000

### 关联 PR

- 遵循 **PR-001** · 并列挂 list · 自建动态表单走 HRDynamicFormBasePlugin
- 遵循 **PR-009** · 多组织传 boid 集合

---

## CS-06 · 配对场景生效后联动监听（通过 OP 拦截 · 不在本场景挂）

**关联 PR**：PR-001（并列挂）+ PR-010（OP 13 lifecycle）+ PR-011（BEC 仅作参考）

### 需求

业务方说："我们想做一个'组织变动同步到外部系统'功能 · 每次组织变动都通知外部 ERP / OA。"

### ⚠ 设计陷阱：不要在本场景挂订阅插件

**反编译产物 grep 0 处** `triggerEventSubscribe / IEventService / EventServiceHelper` · 本场景标品没发任何 BEC 事件 · ISV 在本场景挂订阅永远收不到。

**正确位置**：在配对场景 `homs_orgbatchchgbill` 的写入端挂 OP（参考 `homs_orgbatchchgbill_maintenance` 06_customization_solutions.md 同名场景）。本 CS 主要是给"想在本场景做监听"的开发者一个**反指引** + 正确做法导引。

### 推荐方案

- **扩展对象**：配对场景 `homs_orgbatchchgbill`（不是本场景）
- **扩展点**：在 `audit` / `submiteffect` opKey 上挂 OP · 在 `afterExecuteOperationTransaction` 阶段（PR-010 · 主事务已提交安全）调外部系统
- **风险**：中（要保证主事务已提交 · 异步处理避免阻塞）

### 调用链

```
[配对场景 homs_orgbatchchgbill 单据 submiteffect 触发]
   │
   ▼
[OrgBatchChgBillEffectOp.beforeExecuteOperationTransaction]
   │
   ▼
[OrgBatchChgBillEffectOp.beginOperationTransaction]
   ├─ 写 t_haos_adminorg / t_homs_orgchgrecord / t_homs_orgchgentry / t_homs_orgchgdetail
   ↓
[OrgBatchChgBillEffectOp.endOperationTransaction]
   ↓
[OrgBatchChgBillEffectOp.afterExecuteOperationTransaction]  ← ⭐ ISV 在此挂
   ├─ 主事务已提交 · 数据可见
   ├─ 拿生效后的变动记录（按 chgbill.id 查 t_homs_orgchgrecord）
   ├─ 调外部 ERP / OA 同步接口
   └─ （可选）走 BEC 发布到平台事件中心 · 让其他 ISV 也能订阅
```

### 扩展入口坐标

- 绑定表单：**`homs_orgbatchchgbill`**（**配对场景** · 不是本场景）
- 绑定操作：`audit` 或 `submiteffect`
- 推荐父类：**`HRDataBaseOp`**（@SdkPlugin 白名单 · HR 通用 OP）
- **禁继承 `OrgBatchChgBillEffectOp`**（标品场景专属 · PR-001）/ **禁继承 `AbsOrgBaseOp`**（haos 域基类 · 不在白名单）

### 代码框架

```java
package ${ISV_FLAG}.hrmp.homs.orgbatchchgbill.opplugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.service.ServiceFactory;
import kd.bos.bec.api.IEventService;  // PR-011
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * 配对场景 homs_orgbatchchgbill 生效后 · 同步外部系统
 * 遵循 PR-001 · 并列挂 OP · 不继承 OrgBatchChgBillEffectOp（场景专属）/ AbsOrgBaseOp（haos 域 final）
 * 遵循 PR-010 · afterExecuteOperationTransaction 阶段（主事务已提交）
 * 遵循 PR-011 · 走 BEC 不自建 MQ
 */
public class TdkwOrgChgRecordSyncExtOp extends HRDataBaseOp {

    private static final Log LOG = LogFactory.getLog(TdkwOrgChgRecordSyncExtOp.class);
    private static final String EVENT_NUMBER_ORG_CHG = "${ISV_FLAG}_org_chg_record_synced";

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);

        DynamicObject[] entities = e.getDataEntities();
        if (entities == null || entities.length == 0) {
            return;
        }

        for (DynamicObject billEntity : entities) {
            try {
                long billId = billEntity.getLong("id");
                publishOrgChgEvent(billId);
            } catch (Exception ex) {
                LOG.error("同步组织变动失败 · billId=" + billEntity.getLong("id"), ex);
                // 不抛异常 · 不阻塞标品流程（主事务已提交 · 抛异常也回滚不了）
            }
        }
    }

    /**
     * 走 BEC 发布事件 · 让外部 ISV 用订阅方式接收
     * 而不是同步调外部接口（同步调失败会卡住请求 · BEC 是异步可重试的）
     */
    private void publishOrgChgEvent(long billId) {
        // 拿该申请单写入的所有变动记录（PR-009 · 用 boid 维度查）
        HRBaseServiceHelper recordHelper = new HRBaseServiceHelper("homs_orgchgrecord");
        DynamicObject[] records = recordHelper.query(
                "id,adminorg.id,adminorg.boid,orgchgentry.chgeffecttime,orgchgentry.changescene.number",
                new QFilter[]{new QFilter("orgchgentry.chgbill.id", "=", billId)}
        );

        if (records == null || records.length == 0) {
            return;
        }

        // 走苍穹 BEC 发布（PR-011 · 不自接 Kafka/RabbitMQ）
        IEventService svc = ServiceFactory.getService(IEventService.class);
        for (DynamicObject record : records) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("billId", billId);
            variables.put("recordId", record.getLong("id"));
            variables.put("adminorgBoid", record.getLong("adminorg.boid"));
            variables.put("changescene", record.getString("orgchgentry.changescene.number"));
            variables.put("chgeffecttime", record.getDate("orgchgentry.chgeffecttime"));

            svc.triggerEventSubscribeJobs(
                    "homs",                       // sourceApp · 来源应用
                    EVENT_NUMBER_ORG_CHG,         // eventNumber · 必须先在【开发平台】→【业务事件管理】预配置
                    "组织变动已生效",                // message
                    variables                     // variables · 业务参数
            );
        }
    }
}
```

订阅方（外部系统监听）：

```java
package ${ISV_FLAG}.hrmp.homs.orgbatchchgbill.opplugin;

import java.util.Map;
import kd.bos.bec.api.IEventServicePlugin;
import kd.bos.bec.model.KDBizEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

/**
 * 订阅"组织变动已生效"事件 · 同步到外部系统
 * 遵循 PR-011 · 实现苍穹 IEventServicePlugin
 */
public class TdkwOrgChgRecordSubscriber implements IEventServicePlugin {

    private static final Log LOG = LogFactory.getLog(TdkwOrgChgRecordSubscriber.class);

    @Override
    public void handleEvent(KDBizEvent event) {
        if (!"${ISV_FLAG}_org_chg_record_synced".equals(event.getEventNumber())) {
            return;
        }

        Map<String, Object> variables = event.getVariables();
        if (variables == null || variables.isEmpty()) {
            return;
        }

        long billId = ((Number) variables.get("billId")).longValue();
        long recordId = ((Number) variables.get("recordId")).longValue();
        long adminorgBoid = ((Number) variables.get("adminorgBoid")).longValue();
        String changescene = (String) variables.get("changescene");

        // 调外部 ERP/OA 同步接口
        try {
            syncToExternalERP(billId, recordId, adminorgBoid, changescene);
        } catch (Exception ex) {
            LOG.error("同步外部系统失败 · billId=" + billId, ex);
            // BEC 会按平台规则重试
            throw ex;
        }
    }

    private void syncToExternalERP(long billId, long recordId, long adminorgBoid, String scene) {
        // 实际调外部接口逻辑
    }
}
```

### 平台绑定方式

1. 苍穹开发平台 → 表单 `homs_orgbatchchgbill` · 操作 `audit`（或 `submiteffect`）
2. 注册扩展插件：`TdkwOrgChgRecordSyncExtOp` · 跟标品 `OrgBatchChgBillEffectOp` 并列跑（PR-001）
3. 开发平台 → 业务事件管理 → 预配置 `${ISV_FLAG}_org_chg_record_synced` 事件号
4. 注册订阅插件：`TdkwOrgChgRecordSubscriber` · `targetType = EVENT`
5. 部署生效

### 踩坑

- ❌ 在本场景 `homs_orgchgrecord` 挂订阅插件 → 永远收不到事件（标品本场景不发 BEC）
- ❌ 在 `beforeExecuteOperationTransaction` 调外部接口 → 主事务还没提交 · 外部系统看到的是脏数据（PR-010）
- ❌ 在 `endOperationTransaction` 调外部接口 → 事务可能回滚 · 但事件已发出 · 产生脏事件（PR-010 · 必须 afterExecuteOperationTransaction）
- ❌ 同步调外部接口（不走 BEC）→ 外部接口卡住会阻塞标品流程 · 用户感知 timeout（违反 PR-011）
- ❌ 自接 Kafka/RabbitMQ → 违反 PR-011 · 苍穹平台已提供 BEC · 重造轮子需要自己处理重试 / 死信 / 监控
- ❌ eventNumber 没在【开发平台】→【业务事件管理】预配置 → BEC 不识别 · 事件丢失（PR-011 · prerequisite）
- ❌ 在事件 variables 里塞完整 DynamicObject → BEC 队列消息体过大（PR-011 · antiPattern）· 应该只传 ID + 关键摘要 · 订阅方按 ID 反查
- ⚠ 大批量生效（一次申请单写 100+ 变动记录）· 一次性发 100+ 事件会冲爆 BEC 队列 · 建议聚合成 1 个事件 · 在 variables 里塞 [recordIds] 列表
- ⚠ BEC 是平台异步 · 不保证立即送达 · 外部 ERP 期望"实时同步"的场景需要业务方确认接受秒级延迟

### 关联 PR

- 遵循 **PR-001** · 并列挂 OP · 不继承 OrgBatchChgBillEffectOp / AbsOrgBaseOp
- 遵循 **PR-009** · 跨场景查变动记录用 boid 维度
- 遵循 **PR-010** · afterExecuteOperationTransaction 阶段（主事务已提交）发事件
- 遵循 **PR-011** · 走苍穹 BEC · 不自建 MQ · eventNumber 必须预配置 · 不在 variables 塞完整 DynamicObject

---

## CS 总结表

| CS | 难度 | 价值 | 关联 PR | 备注 |
|---|---|---|---|---|
| CS-01 加自定义筛选字段 | 低 | 高 | PR-001 / PR-009 | 必须先在 haos_adminorgdetail 主层加字段 |
| CS-02 默认排序定制 | 低 | 中 | PR-001 | 重写 beforeCreateListColumns |
| CS-03 hyperLinkClick 双跳扩展 | 中 | 高 | PR-001 / PR-008 / PR-009 | 跨场景跳转 · boid + iscurrentversion 双过滤 |
| CS-04 字段级溯源美化 | 中 | 高 | PR-001 / PR-005 / PR-009 | 自建子分录补录用 ID.genLongId（PR-005 强制） |
| CS-05 高级筛选弹窗 | 中 | 中 | PR-001 / PR-009 | 自建动态表单 · pageCache 传参 |
| CS-06 配对场景生效后联动 | 高 | 极高 | PR-001 / PR-009 / PR-010 / PR-011 | ⭐ 不在本场景挂 · 在 homs_orgbatchchgbill 挂 OP + BEC |

**未列入的（标品已做或不适合 ISV 扩展）**：
- ❌ ~~订阅本场景的 BEC 事件~~ — 标品本场景不发任何 BEC（grep 0 处）· 想监听变动事件去 CS-06
- ❌ ~~在本场景修改变动记录~~ — 反模式 · 破坏审计完整性 · 走配对场景"反向调整单"
- ❌ ~~批量补录历史变动~~ — 走标品 HIES 导入向导 importdata_hr · 别自己造轮子
- ❌ ~~删除变动记录~~ — 不允许（违反审计设计）

---

## 总体注意事项

1. **本场景所有 CS 都是 ListPlugin 扩展**（除了 CS-06 是 OP）· 因为本场景 100% 只读 · 没有 OP 链路可挂
2. **BEC 发布只能在配对场景挂**（CS-06）· 反编译 grep 实证本场景 0 处 BEC API 调用 · 本场景挂订阅永远收不到
3. **跨场景跳转 / 跨场景查询全用 boid**（PR-009）· 因为 adminorg 字段值是 boid 维度
4. **自建子分录新行 id 必须用 ID.genLongId()**（PR-005 · CS-04 强制）· 禁用 UUID/System.currentTimeMillis/select max+1
5. **禁继承的 4 个标品类**（实证整理）：
   - `AdminOrgChgRecordListPlugin`（public **final** class · 反编译 L117 实证）
   - `AdminOrgChgRecordBUListPlugin`（public **final** class · 反编译 L15 实证）
   - `AbstractBUListPlugin`（haos 域 · `cosmic_hr_sdk_whitelist_audit.md` 不在白名单）
   - `OrgBatchChgBillEffectOp`（场景专属 OP · 标品升级风险）

---

## 关联文档

- [`02_business_rules.md`](02_business_rules.md) · §2 数据可见性 5 条 / §3 数据来源 5 条 / §4 BEC 判定 / §5 字段语义 10 条
- [`03_model_design.md`](03_model_design.md) · §4 字段分组 / §5 物理表 / §8 ISV 扩展认知
- [`07_ext_points.md`](07_ext_points.md) · 15 opKey 扩展点矩阵 + SDK 父类白名单
- [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json) · PR-001 / PR-005 / PR-008 / PR-009 / PR-010 / PR-011
- [`knowledge/scenarios/homs_orgbatchchgbill_maintenance/06_customization_solutions.md`](../homs_orgbatchchgbill_maintenance/06_customization_solutions.md) · ⭐ 配对场景 CS（必对照读）
- [`knowledge/scenarios/haos_adminorghis/06_customization_solutions.md`](../haos_adminorghis/06_customization_solutions.md) · 同样只读查询模式 CS
- [`knowledge/_sdk_audit/cosmic_hr_sdk_whitelist_audit.md`](../../_sdk_audit/cosmic_hr_sdk_whitelist_audit.md) · SDK 白名单铁律

---

## 被引用·成建制划转（业务流型聚合）

> ⚡ 本场景作为下游被引用方·在 `core_hr_org_unit_transfer` 业务流中扮演角色。

### 引用方
- **业务流场景**：[`core_hr_org_unit_transfer`](../core_hr_org_unit_transfer/) (HR 核心人力 / hdm 调配管理)
- **完整可复刻资产**：[`_assets/org_unit_transfer/`](../../_assets/org_unit_transfer/)

### 引用关系
成建制划转任务 A/B 通过 `OrgTransferHelper.queryHAOSChangeMsg` 查询本场景·过滤 `orgchgentry.changescene.number IN (1020_S, 1030_S)`

### 部署后的影响
当客户部署成建制划转资产 (`_assets/org_unit_transfer/`)·调度任务每日跑会**消费本场景的标品数据** (查询组织变更记录·拿组织 ID + 生效时间)·然后批量装配标品调动单 `hdm_transferbatch` + `SUBMITEFFECT` 触发跨云协同。

### 跟本场景 ISV 扩展的关系
- ✅ 客户在本场景做 ISV 扩展（如 CS-01 加字段 / CS-02 反向引用查询）·**不影响**成建制划转资产消费
- ⚠️ 客户在本场景**改字段命名规范**（如改某字段 key）·**必须**同步检查 `core_hr_org_unit_transfer` 是否引用·避免破坏跨场景集成
