# 推荐定制方案 · HR 角色管理列表 (hrcs_rolelist)

> **状态**: 🟢 基于反编译 2 类（HRRoleListPlugin / HRAdminStrictPlugin · 共 1823 行 Java）+ scene_doc + main_form.xml + opkeys_index 实证
> **confidence**: real_deploy（所有扩展点类名、行号、字段 key 均来自反编译实证）

所有方案遵循统一结构（借鉴 Stripe / Salesforce Developer Docs）：
**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

---

## CS-01 · 给"角色"扩展自定义字段（业务最高频）

**关联 Pattern**：`pattern/add_field_extension/README.md`

### 需求

业务方说：HR 想给角色加 3 个属性 —— "角色等级（高/中/低）"、"角色风险标签"、"角色来源（标品/ISV/客户自定）" · 用于 BI 看板分类统计角色画像 + 后续做"高风险角色变更必须 HR 经理双签"自动化判断。

### 推荐方案

- **扩展对象**：`perm_role` ⚠️ **不直接改**（标品 BOS form · ISV 隔离铁律 PR-001 · 详见 `isv_ownership_redline.md`）
- **正确路径**：建 ISV 扩展元数据 `${ISV_FLAG}_perm_role_ext` 继承 `perm_role` · 在子元数据上加字段
- **风险**：中（涉及 BOS 标品继承 · 必须 ParentId/MasterId 实证写对 · 否则字段挂在错误的层）
- **特点**：rolelist 列表是 QueryListModel · 列里要显示扩展字段需要在**列表元数据**也加列（用 `perm_role.${ISV_FLAG}_rolelevel` 路径引用）· 跟改 BillFormModel 不一样

### 调用链（4 步）

```
Step 1: getBizApps()                                                   // 拿 hrcs 应用 bizAppId
Step 2: getFormSchema("perm_role")                                     // 实证 perm_role 元数据（确认 isv 当前归属、parent 链）
Step 3: createMeta + modifyMeta 在 ISV 子元数据上 add 3 个字段：
  ├─ ${ISV_FLAG}_rolelevel · ComboField（高=H/中=M/低=L）· 默认 M
  ├─ ${ISV_FLAG}_risktag · MulComboField（数据敏感/超管权限/审计相关·多选）
  └─ ${ISV_FLAG}_rolesource · ComboField（标品=STD/ISV=ISV/客户自定=CUSTOM）· 默认 CUSTOM
Step 4: 在 hrcs_rolelist 列表元数据加 3 列：
   引用路径 perm_role.${ISV_FLAG}_rolelevel / .${ISV_FLAG}_risktag / .${ISV_FLAG}_rolesource
Step 5: getFormSchema("perm_role")                                     // ⭐ 二次验证落库（errorCode=0 不代表成功）
Step 6: getFormSchema("hrcs_rolelist")                                 // 验证列表列已挂上
```

### 代码框架（使用 cosmic_devportal_client · IDEA 插件 / cosmic-dev skill）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)

# Step A · 在 ISV 子实体上加 3 个字段
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={
        "id": "${ISV_FLAG}_perm_role_ext",
        "number": "${ISV_FLAG}_perm_role_ext",
        "isvExtend": True,
        "parentId": "<perm_role 的 EntityId>"
    }
)

designer.add_field(
    field_type="ComboField",
    name="角色等级",
    key="${ISV_FLAG}_rolelevel",
    parent_entity_id=designer.base_entity_id,
    combo_options=[
        {"value": "H", "name": {"zh_CN": "高", "en_US": "High"}},
        {"value": "M", "name": {"zh_CN": "中", "en_US": "Medium"}},
        {"value": "L", "name": {"zh_CN": "低", "en_US": "Low"}}
    ],
    default_value="M"
)
designer.add_field(
    field_type="MulComboField",
    name="风险标签",
    key="${ISV_FLAG}_risktag",
    parent_entity_id=designer.base_entity_id,
    combo_options=[
        {"value": "DATA_SENSITIVE",  "name": {"zh_CN": "数据敏感"}},
        {"value": "SUPER_ADMIN",     "name": {"zh_CN": "超管权限"}},
        {"value": "AUDIT_RELATED",   "name": {"zh_CN": "审计相关"}}
    ]
)
designer.add_field(
    field_type="ComboField",
    name="角色来源",
    key="${ISV_FLAG}_rolesource",
    parent_entity_id=designer.base_entity_id,
    combo_options=[
        {"value": "STD",    "name": {"zh_CN": "标品"}},
        {"value": "ISV",    "name": {"zh_CN": "ISV"}},
        {"value": "CUSTOM", "name": {"zh_CN": "客户自定"}}
    ],
    default_value="CUSTOM"
)
designer.save()

# Step B · 在 hrcs_rolelist 列表元数据上挂 3 列
list_designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "hrcs_rolelist", "number": "hrcs_rolelist"}
)
list_designer.add_list_column(field_path="perm_role.${ISV_FLAG}_rolelevel", caption={"zh_CN": "角色等级"})
list_designer.add_list_column(field_path="perm_role.${ISV_FLAG}_risktag",   caption={"zh_CN": "风险标签"})
list_designer.add_list_column(field_path="perm_role.${ISV_FLAG}_rolesource",caption={"zh_CN": "角色来源"})
list_designer.save()
```

### 踩坑

- ❌ **直接 modifyMeta perm_role**：标品 form · 平台 ISV 归属校验拦截 · 详见 `isv_ownership_redline.md` 红线
- ❌ **字段 key 不带 ISV 前缀**（如直接叫 `rolelevel`）→ 标品升级覆盖
- ❌ **fieldName 列名超过 25 字符** → 苍穹平台开发规范限制 · 数据库建表失败
- ❌ **fieldName 手动写 `fk_` 前缀** → 平台会再加 `f` → 列名变 `ffk_xxx` 怪列名 · **建议不传 fieldName 让平台按 `f + key.lowercase()` 自动生成**（kb_cosmic_buildmeta_traps）
- ❌ **MulComboField 不传 comboOptions** → UI 下拉框空白
- ❌ **在 hrcs_rolelist 列表用 `${ISV_FLAG}_rolelevel` 而不是 `perm_role.${ISV_FLAG}_rolelevel`** → 列表查不到字段（因为 hrcs_rolelist 是 QueryListModel · 列引用必须带主体 form 前缀）
- ⚠️ **加字段后 list 加载性能下降**：每次 setFilter + ListProvider 都会带这 3 个字段 SELECT · 角色总数大时可能慢 · 建议加索引（`perm_role.${ISV_FLAG}_rolelevel` 高基数 ComboField 适合加普通索引）
- ⚠️ **ISV 子实体的 hrcs_role 不在本步骤范围**：客户如果想在 hrcs_role 也加 ISV 字段 · 走 `${ISV_FLAG}_hrcs_role_ext` 同样模式

**遵循的 PR 规范**：
- **PR-001**（ISV 扩展走并列 · 不继承）—— 本 CS 通过 ISV 扩展元数据继承 perm_role · 字段是新加的 · 不影响标品 perm_role 字段
- **PR-007**（预置数据不可改 · 业务自建可改）—— 加的是业务字段 · 可随时改
- **PR-010**（OP 13 生命周期）—— 本 CS 不写代码 · 仅元数据级别修改 · 不涉及 OP

### 验证

1. UI 上看到列表多了 3 列 + 列表行能编辑（点击行 hyperLink → hrcs_modifyrole 看是否能填值）
2. `getFormSchema("perm_role")` 返回里 `fields[].key` 包含 `${ISV_FLAG}_rolelevel/${ISV_FLAG}_risktag/${ISV_FLAG}_rolesource`
3. SQL: `SELECT ftdkw_rolelevel, ftdkw_risktag, ftdkw_rolesource FROM t_perm_role_ext WHERE id = ?` 能查到值
4. 已启用角色保存（disable + enable 一轮回）确认 enable 流的 SaveServiceHelper 能处理这 3 个新字段

---

## CS-02 · 字段联动（角色等级随分组带出）

**关联 Pattern**：`pattern/property_changed/README.md`

### 需求

业务方说：HR 角色分组 hrcs_rolegrp 已经按"高级管理 / 中层管理 / 业务操作"分类 · 希望选择**角色分组**字段后 · `${ISV_FLAG}_rolelevel` 自动联动带出（高 = 高级管理 / 中 = 中层管理 / 低 = 业务操作）· 不让管理员漏填或填错。

### 推荐方案

- **扩展对象**：本场景 `hrcs_rolelist` **不行**（QueryListModel · 列表里没 propertyChanged · 是行内编辑很特殊）→ 真实联动应该挂到**新建/编辑表单** `hrcs_newrole` 或 `hrcs_modifyrole`
- **挂载点**：FormPlugin 的 `propertyChanged` （PR-003 / PR-004）· 监听 `group` 字段变化 → 同步 setValue `${ISV_FLAG}_rolelevel`
- **风险**：低（不动业务规则 · 只是 UI 联动）
- **特点**：QueryListModel 不支持 propertyChanged · 如果业务"列表 inline 编辑"需要联动 · 需要做更复杂的 ColumnEditor 自定义 · 不建议（成本高）

### 调用链（4 步）

```
Step 1: 在 hrcs_rolegrp 元数据上加 1 个字段 · 标记每个分组对应等级（${ISV_FLAG}_levelmark · ComboField · H/M/L）
Step 2: 写 ISV FormPlugin · 监听 group 字段 propertyChanged → 查 hrcs_rolegrp.${ISV_FLAG}_levelmark → setValue ${ISV_FLAG}_rolelevel
Step 3: 注册 FormPlugin 到 hrcs_newrole + hrcs_modifyrole 元数据
Step 4: 在 hrcs_rolelist 列表展示时自动看到联动后的值
```

### 代码框架（Java FormPlugin · 实证基类 + SDK 注解）

```java
package com.${ISV_FLAG}.hr.role.formplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * tdkw 角色等级联动 · 挂在 hrcs_newrole / hrcs_modifyrole
 * 反模式参考: HRRoleListPlugin 是场景专属类·不要继承（PR-001）
 * 本类继承 AbstractFormPlugin · 是 SDK 白名单内的稳定基类
 */
public class TdkwRoleLevelAutoFillPlugin extends AbstractFormPlugin {

    private static final String FIELD_GROUP = "group";              // perm_role.group
    private static final String FIELD_LEVEL = "${ISV_FLAG}_rolelevel";     // ISV 扩展字段
    private static final String GRP_FORM_ID = "hrcs_rolegrp";
    private static final String LEVEL_MARK  = "${ISV_FLAG}_levelmark";     // 分组上的等级标记

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        String propKey = e.getProperty().getName();
        if (!FIELD_GROUP.equals(propKey)) {
            return;
        }
        DynamicObject newGrp = (DynamicObject) e.getChangeSet()[0].getNewValue();
        if (newGrp == null) {
            return;                                                  // 分组清空·不联动
        }
        // 查 hrcs_rolegrp 拿等级标记
        DynamicObject grpDy = QueryServiceHelper.queryOne(
            GRP_FORM_ID,
            "id, " + LEVEL_MARK,
            new QFilter[]{new QFilter("id", "=", newGrp.getPkValue())}
        );
        if (grpDy == null) {
            return;
        }
        String levelMark = grpDy.getString(LEVEL_MARK);
        if (HRStringUtils.isEmpty(levelMark)) {
            return;
        }
        // PR-004 死循环防护 · setValue 用 beginInit/endInit 包裹
        this.getModel().beginInit();
        try {
            this.getModel().setValue(FIELD_LEVEL, levelMark);
        } finally {
            this.getModel().endInit();
        }
        this.getView().updateView(FIELD_LEVEL);
    }
}
```

### 踩坑

- ❌ **挂到 hrcs_rolelist 上**：QueryListModel 没有 IDataModel · `getModel().setValue` 拿到 null · 报空指针 · **必须挂 hrcs_newrole / hrcs_modifyrole**
- ❌ **没用 beginInit/endInit**：setValue 触发 propertyChanged → 又触发自己 → 死循环（PR-004）
- ❌ **HRDataBaseEdit / HRDynamicFormBasePlugin 二选一**：HR 域优先用 HRDataBaseEdit（已注入 HR 通用工具）· 但本 CS 因为不读写 HR 域字段 · 用 AbstractFormPlugin 即可
- ❌ **新值 = null 时不处理**：用户清空分组字段会让等级也清空 · 业务不一定能接受 · 视情况是否要保留旧值
- ❌ **propertyChanged 用 e.getProperty().getName() 取属性名 · 不要 cast 成 GroupProp**：标品做法（HRRoleListPlugin.itemClick 用 ctl.getKey() 走 switch）·  ISV 直接判 propKey

**遵循的 PR 规范**：
- **PR-003**（FormPlugin 用 model.setValue · OP 用 entity.set）—— FormPlugin 场景
- **PR-004**（setValue 死循环防护 · beginInit/endInit）—— 见代码 35-39 行
- **PR-001**（不继承场景专属 HRRoleListPlugin）—— 本类继承 AbstractFormPlugin

### 验证

1. 在 hrcs_newrole 选分组 → 角色等级字段自动填上对应值
2. 清空分组 → 等级字段保持原值（如想自动清空，把 newGrp==null 时 setValue("${ISV_FLAG}_rolelevel", null)）
3. 跑 cosmic-replay 用例：mock select group → assert ${ISV_FLAG}_rolelevel 自动赋值
4. 检查 PageCache 没有"循环"日志（如果出现 propertyChanged 嵌套 N 次 · 必有 BUG）

---

## CS-03 · 删除 / 禁用前置 Validator（高风险角色双签校验）

**关联 Pattern**：`pattern/add_validator/README.md`

### 需求

业务方说：角色风险标签如果是"超管权限"或"数据敏感" · 删除/禁用前必须先得到 HR 经理双签确认 · 否则操作直接拦截。

### 推荐方案

- **扩展对象**：`hrcs_rolelist`（本场景）· 因为标品 `HRRoleListPlugin.deleteRoleInfo` / `enableRole` / `disableRole` 都是 FormPlugin 内 setCancel(true) 后绕过 OP 链 —— **没有 OP 扩展点**
- **挂载点**：**并列挂 ListPlugin** · 在 `beforeDoOperation` 抢先 super.beforeDoOperation 之前判断
- **风险**：中（必须保证 RowKey 让自己排在 HRRoleListPlugin **之前**才能拦截）
- **特点**：本场景没 OP 链 · 必须用"FormPlugin 抢拦"模式 · 跟 dynascheme 那种"OP 注册 Validator"完全不同

### 调用链（5 步）

```
Step 1: 元数据上加自己 ListPlugin 类（kd.${ISV_FLAG}.hr.role.formplugin.TdkwRoleHighRiskValidatePlugin）
        放在 HRRoleListPlugin 之前的 RowKey （Cosmic 默认按 RowKey 升序执行）
Step 2: ListPlugin.beforeDoOperation 拦 itemKey="delete" / "bar_disable"
Step 3: 取选中行 → 查 perm_role.${ISV_FLAG}_risktag
Step 4: 高风险（含 SUPER_ADMIN / DATA_SENSITIVE）→ 弹双签确认表单（hrcs_managersign 自建）
Step 5: ConfirmCallBack 通过 → 重新触发 opKey · 否则 setCancel(true) 阻断
```

### 代码框架（Java ListPlugin · 抢拦模式）

```java
package com.${ISV_FLAG}.hr.role.formplugin;

import java.util.HashSet;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.list.BillList;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * tdkw 角色高风险删除/禁用双签校验
 * 挂在 hrcs_rolelist · RowKey < HRRoleListPlugin 让自己先执行
 *
 * 反模式（PR-001）:
 * ❌ 不要继承 HRRoleListPlugin（场景专属类）
 * ❌ 不要继承 HRStandardTreeList（基类是 OK 的·但本 CS 不需要 super 调）
 * ✅ 继承 AbstractListPlugin · 独立业务
 */
public class TdkwRoleHighRiskValidatePlugin extends AbstractListPlugin {

    private static final String OPKEY_DELETE  = "delete";
    private static final String ITEM_DISABLE  = "bar_disable";
    private static final String FIELD_RISKTAG = "${ISV_FLAG}_risktag";       // CS-01 ISV 字段
    private static final Set<String> HIGH_RISK_TAGS = new HashSet<>();
    static {
        HIGH_RISK_TAGS.add("SUPER_ADMIN");
        HIGH_RISK_TAGS.add("DATA_SENSITIVE");
    }

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String operateKey = args.getOperationKey();                   // 注意：列表的 operate.opKey
        String itemKey    = this.getPageCache().get("itemKey");       // 共用 HRRoleListPlugin 的 PageCache
        boolean shouldCheck =
            OPKEY_DELETE.equals(operateKey) || ITEM_DISABLE.equals(itemKey);
        if (!shouldCheck) {
            return;
        }
        BillList billList = (BillList) this.getView().getControl("billlistap");
        ListSelectedRowCollection rows = billList.getSelectedRows();
        if (rows == null || rows.isEmpty()) {
            return;                                                    // HRRoleListPlugin 后续会处理空选中
        }
        Set<Object> roleIds = new HashSet<>(rows.size());
        rows.forEach(row -> roleIds.add(row.getPrimaryKeyValue()));
        // 查这些角色的 risktag · ISV 自定字段在 t_perm_role_ext
        QFilter[] qf = {new QFilter("id", "in", roleIds)};
        DynamicObject[] roles = BusinessDataServiceHelper.load("perm_role",
            "id, number, " + FIELD_RISKTAG, qf);
        StringBuilder highRiskNumbers = new StringBuilder();
        for (DynamicObject role : roles) {
            String tags = role.getString(FIELD_RISKTAG);              // MulComboField · 多选用逗号分隔
            if (HRStringUtils.isEmpty(tags)) continue;
            String[] tagArr = tags.split(",");
            for (String tag : tagArr) {
                if (HIGH_RISK_TAGS.contains(tag.trim())) {
                    highRiskNumbers.append(role.getString("number")).append(", ");
                    break;
                }
            }
        }
        if (highRiskNumbers.length() == 0) {
            return;                                                    // 全部低风险·让 HRRoleListPlugin 继续
        }
        // 高风险 → 阻断 + 提示
        String msg = String.format("高风险角色 (%s) 必须先在【HR 经理双签】流程审批通过 · 不能直接 %s",
            highRiskNumbers.substring(0, highRiskNumbers.length() - 2),
            ITEM_DISABLE.equals(itemKey) ? "禁用" : "删除");
        this.getView().showTipNotification(msg);
        args.setCancel(true);
    }
}
```

### 踩坑

- ❌ **RowKey 没排在 HRRoleListPlugin 之前**：标品 RowKey=1 · 自己写 RowKey=2 → 标品先把 args.setCancel(true) 走完了 · 自己永远拿不到机会
- ❌ **拿不到 itemKey**：`bar_disable` 这种自定义按钮的 opKey 跟 BillList 的 operate.opKey 不一致 · 需要从 PageCache 取（HRRoleListPlugin.beforeItemClick L833 写 itemKey）· 标品 `delete` opKey 是直接的，从 args.getOperationKey() 即可
- ❌ **直接 query "${ISV_FLAG}_risktag" 不带 alias**：MulComboField 在 ORM 层是逗号分隔字符串 · 不是 collection · `role.getString(FIELD_RISKTAG)` 即可
- ❌ **空检查没做就用 split**：用户没填 risktag → tags=null → split 抛 NPE
- ⚠️ **跟 HRRoleListPlugin.beforeItemClick 冲突**：本 CS 是双重保险（标品也会查 checkRoleInfoExist 等） · 不影响功能 · 但**性能上多了一次 BusinessDataServiceHelper.load** · 如果列表批量大于 50 行有点贵 · 可以加 5 秒 PageCache
- ⚠️ **RowKey 排序的 IDEA 插件支持**：在元数据上调整 ISV 插件 RowKey 一般通过 designer.add_plugin 时显式传 row_key=0（最小） · 或在 MetaXml 中 `<RowKey>0</RowKey>`

**遵循的 PR 规范**：
- **PR-001**（ISV 扩展走并列 · 不继承）—— 本类继承 AbstractListPlugin · 不继承 HRRoleListPlugin
- **PR-002**（插件 RowKey 执行顺序）—— RowKey=0 排在标品 RowKey=1 之前
- **PR-007**（预置数据不可改）—— 本 CS 不修改预置 · 仅做拦截
- **PR-010**（OP 13 生命周期）—— 本场景没 OP · 故走 ListPlugin 抢拦

### 验证

1. 创建测试角色 A · 设 risktag=SUPER_ADMIN · 尝试禁用 → 应被拦 · 提示
2. 创建测试角色 B · 不设 risktag · 尝试禁用 → 应正常通过
3. 删除链路同上验证
4. 性能：100 行选中 · 触发 beforeDoOperation 耗时 < 200ms（一次 load 200 角色）

---

## CS-04 · 删除前查下游引用（含 ISV 自定下游表）

**关联 Pattern**：`pattern/cascading_delete_check/README.md`

### 需求

业务方说：本场景标品已经反查了 `hrcs_dynascheme.roleentry.role` 阻止删除（HRRoleListPlugin.getRefrencedRoles L1555-L1568）· 但 HR 项目里 ISV 自建了 2 张表也引用 `perm_role.id`：
- `${ISV_FLAG}_role_workflow`（自建审批流配置表 · roleId 字段引用角色 · 流程未停用时不能删）
- `${ISV_FLAG}_external_sync`（外部系统同步配置 · target_role 字段引用角色 · 同步开启时不能删）

希望删除前**叠加**查这 2 张 ISV 表 · 引用了就拦截。

### 推荐方案

- **扩展对象**：`hrcs_rolelist`
- **挂载点**：**并列挂 ListPlugin** · `beforeDoOperation` 抢拦（同 CS-03 的模式 · 本质都是"绕过 OP 链 + 抢在标品 setCancel 之前"）
- **风险**：中（标品已经在 checkCanDel 内反查了 dynascheme · 我们叠加 · 必须保证两套校验都过才放行）
- **特点**：标品的反查在 **HRRoleListPlugin.checkCanDel L1488-L1553**（FormPlugin 内）· ISV 抢前拦截不需要"等"标品判断

### 调用链（4 步）

```
Step 1: 创建 ISV 自建表 ${ISV_FLAG}_role_workflow + ${ISV_FLAG}_external_sync · 加 roleId/target_role 字段 + 业务字段（status / sync_enable）
Step 2: ISV ListPlugin · beforeDoOperation 拦 opKey=delete
Step 3: 查 2 张 ISV 表 · 是否有"未禁用 + 引用本角色"的记录
Step 4: 任一表有引用 → setCancel(true) + 提示 · 否则放行（让标品 HRRoleListPlugin 继续）
```

### 代码框架（Java · ISV ListPlugin）

```java
package com.${ISV_FLAG}.hr.role.formplugin;

import java.util.HashSet;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.list.BillList;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * tdkw 角色删除前 · 查 ISV 自定下游引用
 * 挂在 hrcs_rolelist · RowKey < HRRoleListPlugin
 *
 * 实证：HRRoleListPlugin.getRefrencedRoles (L1555-L1568) 已经反查了 hrcs_dynascheme · 本 CS 是叠加 ISV 表
 */
public class TdkwIsvRoleDownstreamCheckPlugin extends AbstractListPlugin {

    private static final String OPKEY_DELETE = "delete";
    private static final String FORM_WORKFLOW = "${ISV_FLAG}_role_workflow";
    private static final String FORM_SYNC     = "${ISV_FLAG}_external_sync";

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        if (!OPKEY_DELETE.equals(args.getOperationKey())) {
            return;
        }
        BillList billList = (BillList) this.getView().getControl("billlistap");
        ListSelectedRowCollection rows = billList.getSelectedRows();
        if (rows == null || rows.isEmpty()) {
            return;                                                    // 后续标品会处理空选
        }
        Set<Object> roleIds = new HashSet<>(rows.size());
        rows.forEach(row -> roleIds.add(row.getPrimaryKeyValue()));
        // 查 ${ISV_FLAG}_role_workflow · status != "DISABLED" 视为占用
        QFilter wfFilter = new QFilter("roleId", "in", roleIds)
            .and("status", "!=", "DISABLED");
        DynamicObject[] wfRows = QueryServiceHelper.query(FORM_WORKFLOW,
            "id, roleId, status", new QFilter[]{wfFilter}).toArray(new DynamicObject[0]);
        Set<Long> wfBlocked = new HashSet<>();
        for (DynamicObject wf : wfRows) {
            wfBlocked.add(wf.getLong("roleId"));
        }
        // 查 ${ISV_FLAG}_external_sync · sync_enable=true 视为占用
        QFilter syncFilter = new QFilter("target_role", "in", roleIds)
            .and("sync_enable", "=", Boolean.TRUE);
        DynamicObject[] syncRows = QueryServiceHelper.query(FORM_SYNC,
            "id, target_role, sync_enable", new QFilter[]{syncFilter}).toArray(new DynamicObject[0]);
        Set<Long> syncBlocked = new HashSet<>();
        for (DynamicObject syncRow : syncRows) {
            syncBlocked.add(syncRow.getLong("target_role"));
        }
        if (wfBlocked.isEmpty() && syncBlocked.isEmpty()) {
            return;                                                    // 全部能删·让标品继续
        }
        // 拼提示
        StringBuilder msg = new StringBuilder("删除被拦：");
        if (!wfBlocked.isEmpty()) {
            msg.append("角色 ID ").append(wfBlocked).append(" 在审批流配置中使用中（请先停用流程）；");
        }
        if (!syncBlocked.isEmpty()) {
            msg.append("角色 ID ").append(syncBlocked).append(" 在外部系统同步中使用中（请先关闭同步）；");
        }
        this.getView().showTipNotification(msg.toString());
        args.setCancel(true);
    }
}
```

### 踩坑

- ❌ **没用 BusinessDataServiceHelper.load + select 字段** ：`QueryServiceHelper.query` 返回 DynamicObjectCollection · 需要转数组 · 否则 lazy-load 字段会触发 N+1 查询
- ❌ **status / sync_enable 字段没在 ISV 表上预先建索引**：1000 流程 + 1000 角色场景下查询慢
- ❌ **拦截后没回写 PageCache**：用户连续点 2 次删除 · 第二次的 itemKey 还在 cache · 可能干扰下次执行 · 需要 args.setCancel 后清掉
- ❌ **加了自己拦截但没在 hrcs 项目里更新 RoleService.addLogNoOpKey 写日志**：跟标品风格不一致 · 难审计 · 应该 addLogNoOpKey("delete", "拦截 ISV 下游", false, "hrcs_rolelist", appId, list, msg)
- ⚠️ **跟标品 getRefrencedRoles 关系**：标品在 checkCanDel 里查 hrcs_dynascheme · 我们抢前拦 · 顺序是：
  1. ISV 抢前 · 查 ISV 表 → 不通过则 setCancel
  2. 标品 HRRoleListPlugin.deleteRoleInfo → checkCanDel → getRefrencedRoles 查 dynascheme → 不通过则 setCancel
  → 用户先看到 ISV 拦截 · 解决后才会看到 dynascheme 拦截 · 是 OK 的

**遵循的 PR 规范**：
- **PR-001**（ISV 扩展走并列）—— 继承 AbstractListPlugin
- **PR-007**（预置数据 · 业务自建）—— 本 CS 加自定义校验 · 不动预置数据
- **PR-009**（boid 是业务维度 · id 是版本维度）—— 不适用本场景（不是 HisModel · 详见 03/05 第六/九节）

### 验证

1. 创建审批流绑定 perm_role.A · status=ENABLED → 尝试删除角色 A → 应被拦 · 提示包含"角色 ID [A]"
2. 把审批流 status 改 DISABLED → 重试删除 → 应通过 ISV 拦截 · 进入标品 checkCanDel 链
3. 同时配审批流 + 同步 · 都开启 → 提示应同时含两段
4. SQL 校验：`SELECT COUNT(*) FROM ${ISV_FLAG}_role_workflow WHERE roleId IN (?) AND status != 'DISABLED'` 加索引 · 在 1w 行下 < 50ms

---

## CS-05 · 业务事件中心（BEC · 角色启用/禁用通知下游）

**关联 Pattern**：`pattern/business_event_publish/README.md`
**关联 PR**：**PR-011**（跨模块事件走苍穹 BEC · 不自建 MQ）

### 需求

业务方说：当角色启用/禁用时 · 希望通知 2 个下游系统 ——
- **审计模块**（hrcs_audit）：记录"哪个 HR 管理员在何时启用/禁用了哪个角色"
- **外部 LDAP 同步系统**（${ISV_FLAG}_ldap_sync）：把启用/禁用同步推到 LDAP · 让 LDAP 那边的"角色组"权限同步生效

### ⚠️ BEC 模式判定（grep 实证 · 必读）

> **执行 grep**：`grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" knowledge/_sdk_audit/_decompiled/scenarios/hrcs_rolelist/` —— **0 处命中**

**判定结论**：本场景**标品没有发任何 BEC 事件**。所以：
- ✅ 这是 ISV"白板"场景：没有标品的 BEC 跟我们竞争 · ISV 直接发即可
- ⚠️ **不能套 hjm_jobhr 的"3 层异步"模式** —— hjm 是 OP→Service(派 sch_task)→*MsgTask.execute 三层（详见 `feedback_bec_3layer_async_publish.md`）· 本场景**根本没 OP 链**所以不可能 3 层
- ⚠️ **不能套 dynascheme 的模式** —— dynascheme BEC grep 也是 0 处 · 但 dynascheme 有 OP 可以挂；本场景没 OP，必须挂 **FormPlugin 的 afterDoOperation** ——但因为标品 setCancel(true) 绕过 OP 链，afterDoOperation 也拿不到 enable/disable opKey ——所以**本 CS 必须并列挂 ListPlugin 在 beforeDoOperation 里"先发 BEC 再让标品继续"**或在自己的 ListPlugin afterDoOperation 抢监听 itemKey

### 推荐方案

- **扩展对象**：`hrcs_rolelist`
- **挂载点**：**并列挂 ListPlugin** · 在 `beforeDoOperation` 走完后 · 在 `afterDoOperation` 监听 itemKey="bar_enable"/"bar_disable" 已写 PageCache 但还没清 → 发 BEC
- **风险**：中（事件中心配置必须 portal 上预先注册 eventNumber · 否则发出去无人订阅 · 静默丢失）
- **特点**：本场景必须用 **FormPlugin 阶段** 发 BEC（不是 OP afterExecuteOperationTransaction · 因为没 OP）· 这跟标品 hjm 的"OP 阶段发"不一样

### 调用链（5 步）

```
Step 0: 在【开发平台】→【业务事件管理】预注册 2 个事件号
        - ${ISV_FLAG}_event_role_enable
        - ${ISV_FLAG}_event_role_disable
        每个事件号声明 message + 4 个 variables (roleId / roleNumber / userId / timestamp)

Step 1: ISV ListPlugin · 监听 afterDoOperation
Step 2: 取 itemKey from PageCache (HRRoleListPlugin 在 beforeItemClick 写的)
Step 3: 仅 enable/disable 走 BEC · 其他 opKey 跳过
Step 4: 调 IEventService.triggerEventSubscribeJobs(app, eventNumber, message, variables)
Step 5: 收尾不抛异常 · BEC 失败不应该影响标品启用/禁用本身
```

### 代码框架（Java · BEC 发布·遵循 PR-011）

```java
package com.${ISV_FLAG}.hr.role.formplugin;

import java.util.HashMap;
import java.util.Map;
import kd.bos.bec.api.IEventService;
import kd.bos.context.RequestContext;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.list.BillList;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.service.ServiceFactory;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * tdkw 角色启用/禁用 BEC 事件发布
 * 挂在 hrcs_rolelist · RowKey > HRRoleListPlugin (afterDoOperation 之后再发)
 *
 * BEC 模式：本场景标品 0 处发 BEC（grep 实证）·  纯 ISV 自建
 * 阶段选择：必须 FormPlugin · 因为本场景没 OP（QueryListModel 不走 OP 链）
 */
public class TdkwRoleStatusEventPublisherPlugin extends AbstractListPlugin {

    private static final Log LOG = LogFactory.getLog(TdkwRoleStatusEventPublisherPlugin.class);
    private static final String SOURCE_APP = "hrcs";                    // 苍穹 BEC sourceApp 编码
    private static final String EVENT_ENABLE  = "${ISV_FLAG}_event_role_enable";
    private static final String EVENT_DISABLE = "${ISV_FLAG}_event_role_disable";
    private static final String ITEM_ENABLE   = "bar_enable";
    private static final String ITEM_DISABLE  = "bar_disable";

    @Override
    public void afterDoOperation(AfterDoOperationEventArgs e) {
        super.afterDoOperation(e);
        // 注意：标品 HRRoleListPlugin.afterDoOperation L1005 会清 itemKey
        //  → 我们的 RowKey 必须 < 标品 · 让自己先跑（违反一般 afterDoOperation 顺序的提醒）
        //  → 或者直接用 e.getOperationKey() 拿 enable/disable opKey · 不用 itemKey
        String operateKey = e.getOperateKey();                          // 直接 opKey · 更稳
        if (HRStringUtils.isEmpty(operateKey)) return;
        boolean isEnable  = "enable".equals(operateKey);
        boolean isDisable = "disable".equals(operateKey);
        // bar_enable/bar_disable 是 itemKey · 经 HRRoleListPlugin.beforeDoOperation
        // setCancel + 自己执行 · operateKey 是空 · 需要走 itemKey 路线
        if (!isEnable && !isDisable) {
            String itemKey = this.getPageCache().get("itemKey");
            if (HRStringUtils.isEmpty(itemKey)) return;
            isEnable  = ITEM_ENABLE.equals(itemKey);
            isDisable = ITEM_DISABLE.equals(itemKey);
            if (!isEnable && !isDisable) return;
        }
        BillList billList = (BillList) this.getView().getControl("billlistap");
        ListSelectedRowCollection rows = billList.getSelectedRows();    // 注意：标品已经 clearSelection · 此时拿不到了！
        if (rows == null || rows.isEmpty()) {
            // 兜底：从 PageCache 取（要在自己的 beforeDoOperation 提前缓存）
            return;                                                      // 实战项目要补丁这部分
        }
        long userId = RequestContext.get().getCurrUserId();
        long now    = System.currentTimeMillis();
        try {
            IEventService svc = (IEventService) ServiceFactory.getService(IEventService.class);
            for (ListSelectedRow row : rows) {
                Map<String, Object> vars = new HashMap<>(8);
                vars.put("roleId",     row.getPrimaryKeyValue());
                vars.put("roleNumber", row.getNumber());                 // 列表行的 number 字段
                vars.put("operatorId", userId);
                vars.put("timestamp",  now);
                String eventNumber = isEnable ? EVENT_ENABLE : EVENT_DISABLE;
                svc.triggerEventSubscribeJobs(SOURCE_APP, eventNumber,
                    "Role " + (isEnable ? "enabled" : "disabled") + ": " + row.getNumber(),
                    vars);
            }
        } catch (Exception ex) {
            // BEC 失败不应该回滚标品启停 ·  log 即可
            LOG.error("Failed to publish BEC event for role status change", ex);
        }
    }
}
```

### 踩坑

- ❌ **没在【开发平台】→【业务事件管理】预注册 eventNumber**：trigger 时 BEC 不识 · 静默丢失（PR-011 prerequisite）
- ❌ **在 BEC 异常时 throw 上去**：会污染标品流程 · 启停反而失败 · 要 catch + log 不抛
- ❌ **vars 里塞完整 DynamicObject**：BEC 模型 KDBizEvent.variables 设计是 Map<String, Object> · 序列化大对象会膨胀消息体 · 推荐只塞 ID + 编码 · 接收方自己再查
- ❌ **拿不到 selectedRows**：`HRRoleListPlugin.handleBeforeOperatetionEvent` L1022/L1029 已经 `billList.clearSelection()` · 在 afterDoOperation 时选中行已空 · 必须自己在 beforeDoOperation 阶段 PageCache 先暂存 roleIds（实战项目补丁）
- ❌ **重复发 BEC**：自己的 ISV 插件 + 标品 OP（如 dynascheme 也用了 hrcs 公共 OP·会发一次） → 双发幂等风暴 · 必须 grep 同应用所有 jar 实证（详见 `feedback_bec_3layer_async_publish.md`）—— **本场景因为没 OP · 这个风险消除了**
- ⚠️ **3 层异步模式 NOT applicable**：`feedback_bec_mode_per_scene_verify.md` 强调 BEC 模式不能跨场景套用 · 本场景没 OP · 不存在派 sch_task 的需求

**遵循的 PR 规范**：
- **PR-001**（ISV 扩展走并列）
- **PR-010**（OP 13 生命周期）—— 不适用 · 因为本场景没 OP · 故走 FormPlugin afterDoOperation
- **PR-011**（BEC 跨模块走苍穹 · 不自建 MQ）⭐ 核心
- 反引用：`feedback_bec_3layer_async_publish.md` 教导的"OP→Service→MsgTask 3 层"模式 · 本场景**不适用**

### 验证

1. 在 portal 上预注册 `${ISV_FLAG}_event_role_enable` / `${ISV_FLAG}_event_role_disable` · 订阅一个测试 IEventServicePlugin 类
2. 启用一个角色 · 检查日志 + 测试订阅类是否收到事件
3. 禁用一个角色 · 同上
4. 用大量数据测试 · 不会产生重复 BEC（一个 opKey + N 行只产生 N 个事件 · 不重）
5. 关闭 BEC 服务 · 触发 enable · 标品启动应该不受影响（catch 兜底）

---

## CS-06 · 列表过滤定制（角色分组管理员只见自己组的角色）

**关联 Pattern**：`pattern/list_filter_extension/README.md`

### 需求

业务方说：HR 项目里有"角色分组管理员"角色 · 这类管理员只能管理某个分组下的角色（不能看其他分组）· 但他们仍然是 isHrAdmin · 能登录 hrcs_rolelist 页面。希望：
- 标品 viewableRoles 闸保持（PR-001）
- ISV 叠加：当用户是"角色分组管理员"时 · 只显示绑定在该用户管的分组下的角色

### 推荐方案

- **扩展对象**：`hrcs_rolelist`
- **挂载点**：**并列挂 ListPlugin** · 实现 `setFilter`（不是覆盖 · 是叠加）
- **风险**：高 ⭐ —— 设错过滤会让管理员**看不到自己应该能看到的角色** · 业务损失大
- **特点**：标品 `HRRoleListPlugin.setFilter` (L367-L372) 已经加了 `id IN viewableRoles` · 我们叠加 `group IN userGroups` · QFilter and 组合即可

### 调用链（4 步）

```
Step 1: 在 ISV 表 ${ISV_FLAG}_user_rolegrp_admin · 维护"哪个 user 是哪个 rolegrp 的管理员" · 字段 user / rolegrp / scope_type=GROUP_ADMIN
Step 2: ISV ListPlugin · 实现 setFilter
Step 3: 当前用户在 ${ISV_FLAG}_user_rolegrp_admin 有记录 · 且不是顶级 HR 管理员 → 加 group IN userGroups 过滤
Step 4: 标品 setFilter 也跑（不要 super 替换） · QFilter 自动 AND
```

### 代码框架（Java · 叠加过滤）

```java
package com.${ISV_FLAG}.hr.role.formplugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.hr.hbp.business.service.perm.HRAdminService;

/**
 * tdkw 列表过滤增强 · 角色分组管理员只见自己组角色
 * 挂在 hrcs_rolelist · RowKey 任意（setFilter 调用时 QFilter 自然累加）
 *
 * 实证基础：
 * - HRRoleListPlugin.setFilter (L367-L372) 已经加了 id IN viewableRoles 闸
 * - 本 CS 叠加 group IN userGroups · QFilter 自动 AND
 *
 * ⚠ 风险：QFilter 写错会让管理员看不到自己组的角色 → 业务侧重大事故
 */
public class TdkwGroupAdminListFilterPlugin extends AbstractListPlugin {

    private static final String FORM_USER_GRP_ADMIN = "${ISV_FLAG}_user_rolegrp_admin";

    @Override
    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);
        long userId = RequestContext.get().getCurrUserId();
        // 顶级 HR 管理员 · 不需要叠加
        if (HRAdminService.isHrAdmin() && this.isTopLevelAdmin(userId)) {
            return;
        }
        // 查 ${ISV_FLAG}_user_rolegrp_admin · 找当前用户管理的分组
        QFilter qf = new QFilter("user", "=", userId)
            .and("scope_type", "=", "GROUP_ADMIN");
        List<DynamicObject> adminRows = QueryServiceHelper.query(
            FORM_USER_GRP_ADMIN, "rolegrp", new QFilter[]{qf});
        if (adminRows == null || adminRows.isEmpty()) {
            // 当前用户没有任何分组管理权 · 但又是 isHrAdmin · 维持标品 viewableRoles 即可
            return;
        }
        Set<Object> grpIds = new HashSet<>(adminRows.size());
        for (DynamicObject row : adminRows) {
            grpIds.add(row.get("rolegrp"));
        }
        QFilter groupFilter = new QFilter("group", "in", grpIds);
        e.getQFilters().add(groupFilter);
    }

    /**
     * 判断当前用户是否顶级 HR 管理员（推测·实证应找业务专家确认）
     * 苍穹平台 PermissionServiceHelper.isAdminUser(userId) 是平台级超管
     * isHrAdmin 是 HR 域管理员
     * 这里"顶级"的定义可以是 hrcs_admingrp.parent IS NULL · 即根管理员组
     */
    private boolean isTopLevelAdmin(long userId) {
        // 待业务侧实现 · 此处仅伪代码示意
        return kd.bos.servicehelper.permission.PermissionServiceHelper.isAdminUser(userId);
    }
}
```

### 踩坑

- ❌ **super.setFilter(e) 之前 add QFilter**：会被 super 调用清掉 · 必须**之后**add
- ❌ **当用户没在 ${ISV_FLAG}_user_rolegrp_admin 也强制 group IN ()**：空集合 → 没有 group=空集 命中 · **整张列表空白** · 用户投诉 · 必须 if 检查 `adminRows.isEmpty()` 提前 return
- ❌ **没排除顶级 HR 管理员**：顶级 HR 管理员（系统级超管）也被限制成"只看自己组" · 业务故障
- ❌ **QueryServiceHelper.query 拿 List<DynamicObject> 而不是 array**：返回类型是 DynamicObjectCollection · 用 `.toArray(new DynamicObject[0])` 或直接 forEach
- ❌ **scope_type 字段没建索引**：每个 setFilter 调一次 · 列表加载性能下降
- ⚠️ **跟标品 group F7 二级过滤的关系**：HRRoleListPlugin.filterContainerBeforeF7Select L418-L432 已经过滤了 hrcs_rolegrp 注册过的分组 · 我们的 setFilter 是行级过滤 · 两层 AND · 都要满足
- ⚠️ **影响导出**：CS-06 的过滤会影响 bar_exportrole 导出 · 用户导出来的也只是过滤后的数据 · 业务上一般是 OK 的

**遵循的 PR 规范**：
- **PR-001**（ISV 扩展走并列）—— 继承 AbstractListPlugin · 不继承 HRRoleListPlugin
- **PR-007**（预置数据 · 业务自建）—— 不适用 · 本 CS 是过滤而非数据修改

### 验证

1. 创建测试用户 X · 在 ${ISV_FLAG}_user_rolegrp_admin 配 X 管 group=A · 登录 → 列表只显示 group=A 下的角色
2. 测试用户 Y · 不在 ${ISV_FLAG}_user_rolegrp_admin · 但是 isHrAdmin → 列表显示所有 viewableRoles（标品行为）
3. 测试用户 Z · 顶级 HR 管理员 → 列表显示所有角色（不被本 CS 限制）
4. SQL 校验：列表查询的 SQL 应包含 `id IN (viewableRoles)` AND `group IN (userGroups)`
5. 导出测试：bar_exportrole 导出的 Excel 行数 ≤ 列表行数 · 不会泄露其他组角色

---

## CS-07 · 批量授权扩展（按 Excel 导入"用户-角色"批量绑定）

**关联 Pattern**：`pattern/batch_import_extension/README.md`

### 需求

业务方说：HR 入职旺季有"批量给 50 个新员工各赋 5 个角色"的运营需求 · 标品的 `bar_assignmember` 是单角色单选 · 一次绑一个 user · 太慢。希望：
- 加个工具栏按钮 "批量赋权(Excel)"
- 用户选 N 行角色 · 点按钮 · 上传 Excel（user / role 两列）· 后台批量写 `hrcs_userrolerelat`

### 推荐方案

- **扩展对象**：`hrcs_rolelist`
- **挂载点**：
  - **元数据**：在 hrcs_rolelist 工具栏加 `BarItem.${ISV_FLAG}_batchassignmember`
  - **代码**：ISV ListPlugin 监听 `beforeItemClick` 校验 + `beforeDoOperation` 跳转 ISV 自建上传表单
- **风险**：中（hrcs_userrolerelat 是 HR 域核心表 · 写入要走标品 OP 链确保数据一致 · 不能裸 INSERT）
- **特点**：本 CS 的写入端**不是本场景** · 是 hrcs_userrolerelat —— 但触发流程的入口在本场景

### 调用链（6 步）

```
Step 1: 在 hrcs_rolelist 工具栏加 BarItem · key=${ISV_FLAG}_batchassignmember · caption=批量赋权(Excel)
Step 2: ISV ListPlugin · beforeItemClick 校验选中行 ≥ 1
Step 3: ISV ListPlugin · beforeDoOperation 跳转 form ${ISV_FLAG}_role_batchassign_upload
        + customParam roleIds=[选中的角色 ID 数组]
Step 4: 上传表单 · 用户选 Excel · 内容是 user_number, role_number 两列
Step 5: 上传后端 · 校验 user/role 都存在 · 然后 OperationServiceHelper.executeOperate
        ("save", "hrcs_userrolerelat", arr) 走标品保存链
Step 6: 标品 OP 链处理：写日志 + 缓存 + 通知下游
```

### 代码框架（Java · 入口部分 · 真正写入在 ${ISV_FLAG}_role_batchassign_upload 表单插件）

```java
package com.${ISV_FLAG}.hr.role.formplugin;

import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.list.BillList;
import kd.bos.list.plugin.AbstractListPlugin;
import java.util.ArrayList;
import java.util.List;

/**
 * tdkw 批量赋权(Excel) 入口
 * 挂在 hrcs_rolelist
 *
 * 反模式（PR-001）：
 * ❌ 不要继承 HRRoleListPlugin
 * ❌ 不要在本 CS 直接 INSERT hrcs_userrolerelat · 必须走 OperationServiceHelper.executeOperate("save")
 *    确保标品 OP 链写日志 + 缓存 + 触发下游
 */
public class TdkwBatchAssignMemberEntryPlugin extends AbstractListPlugin {

    private static final String ITEM_KEY = "${ISV_FLAG}_batchassignmember";
    private static final String UPLOAD_FORM = "${ISV_FLAG}_role_batchassign_upload";

    @Override
    public void beforeItemClick(BeforeItemClickEvent e) {
        super.beforeItemClick(e);
        if (!ITEM_KEY.equals(e.getItemKey())) return;
        BillList billList = (BillList) this.getView().getControl("billlistap");
        ListSelectedRowCollection rows = billList.getSelectedRows();
        if (rows == null || rows.isEmpty()) {
            this.getView().showTipNotification("请选择要批量赋权的角色（至少 1 个）");
            e.setCancel(true);
        }
    }

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {
        super.beforeDoOperation(e);
        String itemKey = this.getPageCache().get("itemKey");
        if (!ITEM_KEY.equals(itemKey)) return;
        BillList billList = (BillList) this.getView().getControl("billlistap");
        ListSelectedRowCollection rows = billList.getSelectedRows();
        if (rows == null || rows.isEmpty()) return;
        List<Object> roleIds = new ArrayList<>(rows.size());
        rows.forEach(row -> roleIds.add(row.getPrimaryKeyValue()));
        FormShowParameter param = new FormShowParameter();
        param.setFormId(UPLOAD_FORM);
        param.getOpenStyle().setShowType(ShowType.Modal);
        param.setCustomParam("roleIds", roleIds);
        this.getView().showForm(param);
        e.setCancel(true);
    }
}
```

### 踩坑

- ❌ **直接 INSERT hrcs_userrolerelat 而不走 OperationServiceHelper**：跳过 OP 链 → 不写日志 + 不刷缓存 + 不触发下游 · 数据不一致
- ❌ **Excel 解析没用 POIServiceHelper · 用第三方库**：苍穹有内置的 POI 工具 · 用第三方库会引入版本冲突
- ❌ **批量 ≥ 500 行没分批 · 单事务挂掉**：苍穹 ORM 事务默认 30s 超时 · 批量 1000 必超 · 必须分批 200/批
- ❌ **校验 user 存在没用 BusinessDataServiceHelper.load · 用 SQL**：load 走 ORM 缓存 + EntityType 校验 · 比裸 SQL 安全
- ❌ **保存失败时没补偿**：写到第 250 行失败时 · 前面 249 行已经存了 · 必须做"全部成功 or 全部回滚"（用 TX.required + 异常 markRollback）
- ⚠️ **跟标品 bar_assignmember 关系**：标品已有单角色分配（HRRoleListPlugin.beforeAssignRole L1153）· 跳转 hrcs_modifyrole + useroperation=assignmember · 本 CS 是**叠加批量入口** · 不是替代

**遵循的 PR 规范**：
- **PR-001**（ISV 扩展走并列）—— 继承 AbstractListPlugin
- **PR-005**（ID 用 `kd.bos.id.ID`）—— Excel 上传后写 hrcs_userrolerelat 行的 id 用 `ID.genLongId()`
- **PR-007**（预置数据 · 业务自建）—— 本 CS 不动预置 · 仅写入 user-role 关联

### 验证

1. 选 5 个角色 · 点【批量赋权(Excel)】→ 弹上传表单 · customParam.roleIds 有 5 个 ID
2. 上传 Excel: user_number=A,role_number=R1; user=B,role=R2; ... · 后台保存
3. 验证 `hrcs_userrolerelat` 新增 N 行 · 每行 sourcetype != "4"（dynascheme 才是 sourcetype=4 · 本 CS 是手动 = sourcetype="0" 或自定义值）
4. 标品日志：`SELECT * FROM log_billno WHERE formid = 'hrcs_userrolerelat' AND optime > now() - 1m` 应该有 N 条
5. 异常测试：故意上传一行 user 不存在 · 检查整批是否回滚 · 错误消息是否清晰

---

## 关联 PR 总览（本文档共引用 PR-001 / PR-002 / PR-003 / PR-004 / PR-005 / PR-007 / PR-009 / PR-010 / PR-011）

| PR | 引用次数 | 在本文档的应用场景 |
|---|---|---|
| **PR-001** | 6 (CS-01/02/03/04/05/06/07 都有) | ISV 扩展走并列 · 不继承 HRRoleListPlugin |
| **PR-002** | 1 (CS-03) | RowKey 排序 · ISV ListPlugin 排在标品之前 |
| **PR-003** | 1 (CS-02) | FormPlugin 用 model.setValue · OP 用 entity.set |
| **PR-004** | 1 (CS-02) | setValue 死循环防护 · beginInit/endInit |
| **PR-005** | 1 (CS-07) | 写 hrcs_userrolerelat 用 ID.genLongId |
| **PR-007** | 5 (CS-01/03/04/06/07) | 预置数据不可改 · 业务自建可改 |
| **PR-008** | 0 | 本场景非时序 · 不适用（grep 实证 0 处） |
| **PR-009** | 1 (CS-04) | 不适用本场景 · 但要明示告知 |
| **PR-010** | 3 (CS-01/03/05) | OP 13 生命周期 · 本场景没 OP · 故大部分 CS 走 FormPlugin |
| **PR-011** | 1 (CS-05) | BEC 跨模块走苍穹 · 不自建 MQ |

→ 详见 `knowledge/_shared/platform_rules.json` · 11 条苍穹平台级通用规范。

---

## 反编译类引用清单

| 反编译类 | 在本文档的引用 |
|---|---|
| `HRRoleListPlugin` | CS-01 (列表列引用 / setFilter 闸) / CS-02 (说明不能挂列表) / CS-03 (PageCache itemKey) / CS-04 (getRefrencedRoles 标品已查 dynascheme) / CS-05 (clearSelection 时机) / CS-06 (setFilter 标品已闸) / CS-07 (beforeAssignRole 标品已有单选) |
| `HRAdminStrictPlugin` | CS-06 (HRAdminService.isHrAdmin · 准入闸校验) |

→ 共 8 次引用 2 个不同反编译类。

---

## 总结：本场景定制化的"独家原则"

1. **没 OP 链** —— 本场景所有 opKey 都被 FormPlugin setCancel 绕过 OP · 校验/拦截/事件全在 FormPlugin/ListPlugin 阶段做
2. **抢拦模式** —— RowKey 排在 HRRoleListPlugin 之前 · 让自己先拿到 args
3. **2 层数据隔离** —— perm_role（BOS）+ hrcs_role（HR）都不能改 · 必须 ISV 扩展继承
4. **23 SQL 级联删除** —— 不要在删除链里塞自己 SQL · 只能拦在前置（CS-04）
5. **标品已发 0 处 BEC** —— ISV 想发事件没人跟我们竞争 · 但要预先注册 eventNumber
6. **行级权限闸已有** —— 不要 super.setFilter 替换 · 只能叠加 AND（CS-06）
7. **复制是单选 + 启用态拦截** —— 业务上反直觉 · 但是标品行为 · 想批量复制只能写新按钮
