# 推荐定制方案 · 数据规则 (hrcs_datarule)

> **状态**：🟢 基于真实 OpenAPI 实抓 + 3 类反编译类名 + `scene_doc.json` 21 字段语义整合
> **confidence**：real_deploy（所有扩展点类名均来自 `_auto_plugin_semantics.md` + `_auto_plugin_registry.md` 实证）

所有方案遵循统一结构（借鉴 Stripe / Salesforce Developer Docs）：
**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

---

## CS-01 · 给 hrcs_datarule 扩展自定义字段（最高频）

**关联 Pattern**：`pattern/add_field_extension/README.md`

### 需求

业务方说：数据规则需要加"规则分类"（多选 · 可选 `客户分类 / 数据安全 / 合规审计 / 部门隔离`），用于 BI 看板分类统计 + 列表过滤。

### 推荐方案

- **扩展对象**：`hrcs_datarule`（主实体）
- **扩展点**：`modifyMeta(op=add, elementType=field)` 或 IDEA 插件 Web UI 加字段
- **风险**：低（不动业务规则 · 只是数据展示位）
- **特点**：hrcs_datarule 不是 HisModel 时序基础资料 · ISV 加字段不会跟 boid/iscurrentversion 一起进版本控制 · 直接写入主表 `t_hrcs_datarule`

### 调用链（3 步）

```
Step 1: getBizApps()                            // 拿 hrcs 应用 bizAppId
Step 2: modifyMeta({
  formId: "hrcs_datarule",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hrcs_datarule",
    element: {
      fieldType: "MulComboField",                       // 多选枚举
      key: "${ISV_FLAG}_rulecategory",
      name: {zh_CN: "规则分类", en_US: "Rule Category"},
      mustInput: false,
      comboOptions: [
        {value: "A", name: {zh_CN: "客户分类",   en_US: "Customer"}},
        {value: "B", name: {zh_CN: "数据安全",   en_US: "Security"}},
        {value: "C", name: {zh_CN: "合规审计",   en_US: "Audit"}},
        {value: "D", name: {zh_CN: "部门隔离",   en_US: "DeptIsolation"}}
      ]
    }
  }]
})
Step 3: getFormSchema("hrcs_datarule")          // ⭐ 二次验证落库（errorCode=0 不代表成功）
```

### 代码框架（使用 cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "hrcs_datarule", "number": "hrcs_datarule"}
)

designer.add_field(
    field_type="MulComboField",
    name="规则分类",
    key="${ISV_FLAG}_rulecategory",
    parent_entity_id=designer.base_entity_id,
    combo_options=[
        {"value": "A", "name": {"zh_CN": "客户分类"}},
        {"value": "B", "name": {"zh_CN": "数据安全"}},
        {"value": "C", "name": {"zh_CN": "合规审计"}},
        {"value": "D", "name": {"zh_CN": "部门隔离"}},
    ]
)
designer.save()  # 一次 save click
```

### 验证

```python
# 二次验证字段确实在 schema 内
schema = client.get_form_schema("hrcs_datarule")
assert "${ISV_FLAG}_rulecategory" in [f["key"] for f in schema["fields"]]

# 验证 DB 列已建
# t_hrcs_datarule 应有 ftdkw_rulecategory 列（或 _ext 表 · 取决于 ISV 模式）
```

### 踩坑

- ❌ 字段 key 不带 ISV 前缀（如直接叫 `rulecategory`）→ 标品升级覆盖
- ❌ `fieldName` 列名超过 25 字符 → 苍穹平台开发规范限制 · 数据库建表失败
- ❌ `fieldName` 手动写 `fk_` 前缀 → 平台会再加 `f` → 列名变 `ffk_xxx` 怪列名 · **建议不传 fieldName 让平台按 `f + key.lowercase()` 自动生成**
- ❌ 误以为 ComboField 不需要 `comboOptions` → UI 下拉框空白
- ❌ 多语言表 `_l` 命名规则：hrcs_datarule 当前**没独立 `_l` 表** · MuliLangTextField 都在主表承载 · ISV 加 MuliLangTextField 时不要假设会自动建 `_l` 表
- ⚠️ 加字段后 · 如果走 ISV 扩展元数据模式 · 会建独立 `t_<bizappnumber>_hrcs_datarule_ext` 表 + `f<bizappnumber>_id` 外键关联主表
- ⚠️ ⚠️ **必须做 Step 3 getFormSchema 二次验证** · errorCode=0 不代表落库成功（platform_rules 实证）

### 遵循的 PR 规范

- **PR-007**（预置数据不可改 · 业务自建可改）· 本 CS 加的是业务字段 · 可随时删改
- **PR-005**（ID 用 kd.bos.id.ID）· 如果未来加分录子表 · 行 id 用 ID.genLongId

### 补充：别陷入的反模式

- ❌ **不要继承 `HRDataRuleEditPlugin`** 然后在 super 后加字段读写。HRDataRuleEditPlugin 是 hrcs 场景专属类（PR-001）· 走并列挂新 FormPlugin（继承 HRDataBaseEdit）· 在新插件的 propertyChanged / afterBindData 处理 ISV 字段。
- ❌ **不要在 hrcs_datarule 加 BasedataField 引用其他 hrcs_datarule** · 数据规则之间不应有引用关系（如果业务必须 · 重新审视设计）。
- ❌ **不要用 BasedataField 引用 HisModel 时序基础资料** · 如果引用 · F7 弹窗必须自己加 `iscurrentversion=1` 过滤（参考 HRDataRuleEditPlugin#beforeF7Select 实现）。

---

## CS-02 · FormPlugin 字段联动（规则分类 → 自动设默认 entitynum 范围）

**关联 Pattern**：`pattern/field_dependency/README.md`

### 需求

业务方说：用户在新建数据规则时 · 选了"规则分类 = 客户分类"后 · 希望 entitynum 字段的 F7 弹窗自动过滤只显示客户域的业务对象（如 `hrpi_customer*` 系列）· 减少错选。

### 推荐方案

- **扩展点**：FormPlugin#propertyChanged + FormPlugin#beforeF7Select
- **挂载方式**：并列挂新 FormPlugin · **不继承 HRDataRuleEditPlugin**（PR-001）
- **数据流**：`${ISV_FLAG}_rulecategory` 字段变 → 缓存到 PageCache → entitynum F7 弹窗时读 PageCache 加 QFilter
- **风险**：低（只读不写 · 不影响标品保存逻辑）

### 调用链

```
用户选规则分类（CS-01 加的字段）
    ↓
ISV FormPlugin#propertyChanged · 读 propName == "${ISV_FLAG}_rulecategory"
    ↓
PageCache.put("rulecategory_for_filter", value)
    ↓
用户点 entitynum 字段 F7
    ↓
ISV FormPlugin#beforeF7Select on entitynum · 读 PageCache
    ↓
addCustomQFilter(new QFilter("number", "like", prefixByCategory + "%"))
    ↓
F7 弹窗只显示符合分类的业务对象
```

### 代码框架（Java FormPlugin · 并列挂 hrcs_datarule）

```java
package mycompany.${ISV_FLAG}.hrcs.formplugin;

import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.IPageCache;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

import java.util.EventObject;

/**
 * ISV 数据规则增强插件
 * 挂在 hrcs_datarule 上 · 跟标品 HRDataRuleEditPlugin 并列执行（不继承）
 *
 * 实现"规则分类 → entitynum F7 自动过滤"
 */
public class TdkwDataRuleCategoryEdit extends HRDataBaseEdit implements BeforeF7SelectListener {

    private static final String FIELD_CATEGORY = "${ISV_FLAG}_rulecategory";
    private static final String FIELD_ENTITYNUM = "entitynum";
    private static final String CACHE_KEY_CATEGORY = "${ISV_FLAG}_rulecategory_cached";

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        // 给 entitynum 加 BeforeF7Select 监听
        // ⚠ 标品 HRDataRuleEditPlugin 已经给 filtergridap 挂了同名监听 · 但目标控件不同 · 不冲突
        this.addClickListeners(FIELD_ENTITYNUM);
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        String propName = e.getProperty().getName();
        if (HRStringUtils.equals(FIELD_CATEGORY, propName)) {
            Object value = this.getModel().getValue(FIELD_CATEGORY);
            String valueStr = value == null ? "" : value.toString();
            // 缓存到 PageCache（PR-004 不需要 beginInit/endInit · 因为这里没有触发 setValue）
            IPageCache pc = this.getView().getPageCache();
            pc.put(CACHE_KEY_CATEGORY, valueStr);
        }
    }

    @Override
    public void beforeF7Select(BeforeF7SelectEvent evt) {
        // ⚠ 注意：这里是 BasedataField 的 BeforeF7Select · 跟 FilterGrid 的 BeforeFilterF7Select 不同
        if (!HRStringUtils.equals(FIELD_ENTITYNUM, evt.getProperty().getName())) {
            return;
        }
        IPageCache pc = this.getView().getPageCache();
        String category = pc.get(CACHE_KEY_CATEGORY);
        if (HRStringUtils.isEmpty(category)) {
            return;
        }
        // 按分类加 QFilter（示例：客户分类只显示 hrpi_customer 系列）
        String prefix = mapCategoryToPrefix(category);
        if (HRStringUtils.isNotEmpty(prefix)) {
            QFilter qf = new QFilter("number", "like", prefix + "%");
            evt.addCustomQFilter(qf);
        }
    }

    private String mapCategoryToPrefix(String category) {
        // 业务自定义映射
        // A=客户分类 / B=数据安全 / C=合规审计 / D=部门隔离
        switch (category) {
            case "A": return "hrpi_customer";
            case "B": return "hrpi_secure";
            case "D": return "haos_adminorg";
            default:  return null; // 不过滤
        }
    }
}
```

### 验证

```bash
# 1. 加字段（CS-01）
# 2. 注册插件（formId=hrcs_datarule · pluginType=BillFormPlugin · ISV 自己的 jar）
# 3. F12 抓包验证：propertyChanged 后 PageCache 有 ${ISV_FLAG}_rulecategory_cached
# 4. F7 弹窗验证：QFilter 在请求 payload 里加了 number like 'hrpi_customer%'
```

### 踩坑

- ❌ **不要继承 HRDataRuleEditPlugin** · 走并列挂新 FormPlugin（PR-001）
- ❌ propertyChanged 里直接 setValue 其他字段 → 不带 beginInit/endInit 会死循环（PR-004）
- ❌ 用 IPageCache · 不用 model 字段做缓存 · 否则跨页面状态错乱
- ⚠️ FilterGrid 的 BeforeFilterF7Select 跟 BasedataField 的 BeforeF7Select 是**两个不同接口** · 不要混用
  - `BeforeFilterF7SelectListener` · `BeforeFilterF7SelectEvent` · `evt.getRefEntityId()` · 实证 HRDataRuleEditPlugin 用
  - `BeforeF7SelectListener` · `BeforeF7SelectEvent` · `evt.getProperty().getName()` · 普通 BasedataField 用
- ⚠️ 如果分类是 MulComboField（多选）· value 是逗号分隔字符串如 "A,B" · 要 split 处理
- ⚠️ 标品已给 FilterGrid 加了 `iscurrentversion=1` 过滤 · ISV 加分类过滤是**叠加** · 不会替换

### 遵循的 PR 规范

- **PR-001** 并列挂不继承
- **PR-003** FormPlugin 用 getModel().getValue
- **PR-004** propertyChanged 加 setValue 时用 beginInit/endInit（本示例没用 setValue · 不需要）
- **PR-008** F7 时序基础资料 iscurrentversion=1 已被标品处理 · ISV 不要重复

---

## CS-03 · save / delete / audit 前置校验（防止规则冲突 + 补缺口）

**关联 Pattern**：`pattern/add_validator/README.md`

### 需求

业务方说：

1. **save 时**·校验同一 entitynum 下不能有同名规则（业务编码可以不同 · 但规则名重复让业务方混乱）
2. **audit 时**·重新跑一遍 FilterBuilder · 防止业务对象元数据被改后规则失效
3. **disable / enable / audit / unaudit 后**·补齐标品缺失的"清缓存 + 通知权限链 + 写 DataRuleLog"

### 推荐方案

- **扩展点**：自建 ISV OP · 挂在 hrcs_datarule 的 save / audit / disable / enable / unaudit / delete 操作上 · 注册 Validator + afterExecute 补做缓存清理
- **挂载方式**：自建 OP **继承 HRDataBaseOp**（不继承 HRDataRuleSaveOp · PR-001）

### 调用链

```
（用户点 audit）
    ↓
[OP onAddValidators] 标品 HRBaseDataStatusOp
    ↓
[OP onAddValidators] ISV TdkwDataRuleAuditOp
    └─ args.addValidator(new TdkwAuditFilterBuilderValidator())
    ↓
[Validator phase] 跑全部 Validator · ISV 跑 FilterBuilder 二次校验
    ↓
[OP beforeExecute] 标品改 status=audit
    ↓
[事务提交]
    ↓
[OP afterExecute] 标品 HRBaseDataLogOp
    ↓
[OP afterExecute] ISV TdkwDataRuleAuditOp
    └─ HRPermCacheMgr.clearCache + PermNotifyService.notifyByDataRule
    └─ DataRuleLogServiceHelper.dataRuleLogInit("audit", model)
```

### 代码框架（ISV OP + Validator · save 同名校验示例）

**1. Validator · save 同名校验**

```java
package mycompany.${ISV_FLAG}.hrcs.opplugin.validator;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * ISV 数据规则同名校验
 * - 同一 entitynum 下 · 规则名不能重复
 */
public class TdkwDataRuleSameNameValidator extends AbstractValidator {

    @Override
    public void validate() {
        ExtendedDataEntity[] rows = this.getDataEntities();
        if (rows == null || rows.length == 0) return;

        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_datarule");

        for (ExtendedDataEntity row : rows) {
            DynamicObject entity = row.getDataEntity();
            long currentId = entity.getLong("id");
            DynamicObject entityNumObj = entity.getDynamicObject("entitynum");
            if (entityNumObj == null) continue;
            String entitynumValue = String.valueOf(entityNumObj.getPkValue());
            String name = entity.getString("name");

            if (HRStringUtils.isEmpty(name)) continue;

            // 查同 entitynum 下是否有同名 · 排除自己
            QFilter qf = new QFilter("entitynum.number", "=", entitynumValue)
                    .and("name", "=", name)
                    .and("id", "!=", currentId);
            if (helper.isExists(qf)) {
                this.addErrorMessage(row,
                        "已存在同名规则（业务对象: " + entitynumValue + " · 名称: " + name + "）· 请改名");
            }
        }
    }
}
```

**2. ISV SaveOp · 注册 Validator + afterExecute 补缺口**

```java
package mycompany.${ISV_FLAG}.hrcs.opplugin;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr;
import kd.hr.hrcs.bussiness.service.perm.PermNotifyService;
import kd.hr.hrcs.bussiness.servicehelper.perm.log.DataRuleLogServiceHelper;
import mycompany.${ISV_FLAG}.hrcs.opplugin.validator.TdkwDataRuleSameNameValidator;

/**
 * ISV 数据规则 SaveOp 增强
 * - onAddValidators: 注册同名校验
 * - afterExecute: 补 DataRuleLog 给 new 模式 + 兜底清缓存
 *
 * ⚠ 不继承 HRDataRuleSaveOp（PR-001）· 而是并列挂
 */
public class TdkwDataRuleSaveEnhanceOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new TdkwDataRuleSameNameValidator());
    }

    @Override
    public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        // 声明读字段（PR-010 #2）
        e.getFieldKeys().add("entitynum");
        e.getFieldKeys().add("name");
        e.getFieldKeys().add("rule");
        e.getFieldKeys().add("id");
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        // 补 DataRuleLog: new 模式（标品 HRDataRuleSaveOp 只处理 modify）
        for (DynamicObject item : args.getDataEntities()) {
            long dataRuleId = item.getLong("id");
            if (dataRuleId == 0L) continue;
            // 是否是新建（dataRuleId 在 beforeExecute 还是 0 · afterExecute 已分配）
            // 用 OperationOption 兜底标记
            String operateFlag = this.getOption().getVariableValue("operate_" + dataRuleId, "0");
            if ("0".equals(operateFlag)) {
                // 标品没标 isChange · 可能是新建 · 补一条 new 日志
                try {
                    DataRuleLogServiceHelper.dataRuleLogInit("new",
                            DataRuleLogServiceHelper.getDataRuleLogModel(dataRuleId, false));
                } catch (Exception ex) {
                    // 容错 · 不让日志失败影响主链路
                }
            }
            // 兜底通知（即使标品已通知 · PermNotifyService 内部应该幂等）
            PermNotifyService.notifyByDataRule(dataRuleId);
        }
    }
}
```

**3. ISV AuditOp · audit 后清缓存 + 通知**

```java
package mycompany.${ISV_FLAG}.hrcs.opplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.perm.HRPermCacheMgr;
import kd.hr.hrcs.bussiness.service.perm.PermNotifyService;
import kd.hr.hrcs.bussiness.servicehelper.perm.log.DataRuleLogServiceHelper;

public class TdkwDataRuleAuditOp extends HRDataBaseOp {

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        for (DynamicObject item : args.getDataEntities()) {
            long id = item.getLong("id");
            // 1. 写 DataRuleLog
            try {
                DataRuleLogServiceHelper.dataRuleLogInit("audit",
                        DataRuleLogServiceHelper.getDataRuleLogModel(id, false));
            } catch (Exception e) { /* 容错 */ }

            // 2. 通知权限链
            PermNotifyService.notifyByDataRule(id);

            // 3. 清缓存 · ⚠ 用 ISV 独立 prefix 不要跟标品 BS_HR_PERM_DATA_RULE 同
            //    或用同样的 cache key（实证标品 SaveOp 也清 BS_HR_PERM_DATA_RULE · 同 key 是允许的）
            HRPermCacheMgr.clearCache(new String[]{
                HRPermCacheMgr.getTypeByPrefix("BS_HR_PERM_DATA_RULE"),
                HRPermCacheMgr.getTypeByPrefix("BS_HR_PERM_BD_DATA_RULE")
            });
        }
    }
}
```

### 验证

```bash
# 1. save 同名校验测试
#    - 进入数据规则 · 新建一条 entitynum=hrpi_customer · name="客户A 数据范围"
#    - 再新建一条同 entitynum + name="客户A 数据范围"
#    - save → 应该报错 "已存在同名规则（业务对象: hrpi_customer · 名称: 客户A 数据范围）· 请改名"

# 2. audit 后清缓存测试
#    - 启用一条规则
#    - 立即用其他用户访问 entitynum 列表 → 规则应立即生效（不等缓存过期）

# 3. DataRuleLog new 模式补齐
#    - 新建一条规则 + save
#    - 查 hrcs_datarule 日志表 → 应有 mode="new" 一条
```

### 踩坑

- ❌ **不要继承 HRDataRuleSaveOp** 然后 super 调用（PR-001）· hrcs 场景专属类
- ❌ **不要继承 HRDataRuleSaveValidator** · 同 PR-001
- ❌ Validator 用 `super.validate()` · AbstractValidator 没有 super.validate · 误调会 NPE
- ❌ 在 onAddValidators 之外注册 Validator · 太晚了 · 校验阶段已过
- ❌ afterExecute 抛异常 · 主事务已提交 · 异常被吞掉但缓存清理失败
- ⚠️ DataRuleLogServiceHelper / HRPermCacheMgr / PermNotifyService 等是 hrcs.bussiness.* 类 · **多数无 SDK 注解** · 用前查 `_shared/cosmic_sdk_annotation_whitelist.md`
- ⚠️ HRBaseDataStatusOp 标品已注册自己的 Validator · ISV Validator 跟它**并列执行** · 任一报错都阻断
- ⚠️ getOption().getVariableValue 跨 OP 共享 · 标品 HRDataRuleSaveOp 已经用 `operate_<id>` / `originalRule_<id>` / `beforeData_<id>` · ISV 用别的 prefix 避免冲突

### 遵循的 PR 规范

- **PR-001** 并列挂不继承（HRDataRuleSaveOp / HRDataRuleSaveValidator）
- **PR-003** OP 用 entity.set / item.getLong（不用 getModel）
- **PR-007** 校验前判 issyspreset
- **PR-010** OP 13 方法 · onAddValidators 注册 Validator · afterExecute 调外部服务
- **PR-008** 如果 Validator 内查 HisModel 类业务对象 · 必带 iscurrentversion=true

---

## CS-04 · 删除 / 禁用前查下游引用（防止规则指空）

**关联 Pattern**：`pattern/check_downstream_reference/README.md`

### 需求

业务方说：删除或禁用一条数据规则前 · 必须先查 hrcs_dynascheme（动态授权方案）和 hrcs_role（HR 角色）是否还在引用 · 如果有 · 列出引用方让用户先解除 · 再删/禁。

### 推荐方案

- **扩展点**：自建 ISV Validator · 挂在 delete / disable 操作的 onAddValidators
- **挂载方式**：自建 Validator 继承 AbstractValidator · 自建 OP 继承 HRDataBaseOp 注册 Validator · **不继承 HRDataRuleSaveOp**（PR-001）
- **数据查询**：用 QueryServiceHelper / HRBaseServiceHelper 查 hrcs_dynascheme.permfilter / hrcs_role 子表

### 调用链

```
用户点删除（或禁用）
    ↓
[OP onAddValidators] 标品 HRBaseDataStatusOp · 状态校验
[OP onAddValidators] ISV TdkwDataRuleDeleteOp
    └─ args.addValidator(new TdkwDataRuleRefCheckValidator())
    ↓
[Validator phase] ISV Validator
    ├─ 查 hrcs_dynascheme.permfilter （或对应 helper）
    │  → 找出引用本规则 id 的方案
    │
    ├─ 查 hrcs_role 子表
    │  → 找出引用本规则 id 的角色
    │
    └─ 任一非空 → addErrorMessage("规则被以下对象引用 · 请先解除 ...")
    ↓
[阻断 / 通过] 进入 OP beforeExecute
```

### 代码框架（Validator 实现）

```java
package mycompany.${ISV_FLAG}.hrcs.opplugin.validator;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.bos.algo.DataSet;
import kd.bos.servicehelper.QueryServiceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * ISV 数据规则下游引用校验
 * - 查 hrcs_dynascheme · hrcs_role 是否引用本规则
 * - 有引用 → 阻断 + 列出引用方
 *
 * ⚠ helper class 名是猜测 · 实际需要 hrmp-hrcs-bussiness 反编译实证字段名
 *   假定 hrcs_dynascheme 子表 permfilter 用 datarule 字段引用
 *   假定 hrcs_role 子表 dataruleentry 用 datarule 字段引用
 */
public class TdkwDataRuleRefCheckValidator extends AbstractValidator {

    @Override
    public void validate() {
        ExtendedDataEntity[] rows = this.getDataEntities();
        if (rows == null || rows.length == 0) return;

        for (ExtendedDataEntity row : rows) {
            DynamicObject entity = row.getDataEntity();
            long ruleId = entity.getLong("id");
            if (ruleId == 0L) continue;

            List<String> refs = new ArrayList<>();

            // 1. 查 hrcs_dynascheme 引用
            QFilter qfScheme = new QFilter("permfilter.datarule", "=", ruleId);
            DataSet dsScheme = QueryServiceHelper.queryDataSet(
                    "TdkwDataRuleRefCheck-scheme",
                    "hrcs_dynascheme",
                    "id, name",
                    new QFilter[]{qfScheme},
                    null);
            try {
                dsScheme.forEach(r -> refs.add("方案: " + r.getString("name") + "(id=" + r.getLong("id") + ")"));
            } finally {
                dsScheme.close();
            }

            // 2. 查 hrcs_role 引用
            QFilter qfRole = new QFilter("dataruleentry.datarule", "=", ruleId);
            DataSet dsRole = QueryServiceHelper.queryDataSet(
                    "TdkwDataRuleRefCheck-role",
                    "hrcs_role",
                    "id, name",
                    new QFilter[]{qfRole},
                    null);
            try {
                dsRole.forEach(r -> refs.add("角色: " + r.getString("name") + "(id=" + r.getLong("id") + ")"));
            } finally {
                dsRole.close();
            }

            if (!refs.isEmpty()) {
                StringBuilder sb = new StringBuilder("规则被以下对象引用 · 请先解除引用再操作:\n");
                int max = Math.min(refs.size(), 10);
                for (int i = 0; i < max; i++) {
                    sb.append(" - ").append(refs.get(i)).append("\n");
                }
                if (refs.size() > 10) {
                    sb.append("（共 ").append(refs.size()).append(" 处 · 仅显示前 10 条）");
                }
                this.addErrorMessage(row, sb.toString());
            }
        }
    }
}
```

**注册 Validator 的 OP**

```java
package mycompany.${ISV_FLAG}.hrcs.opplugin;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import mycompany.${ISV_FLAG}.hrcs.opplugin.validator.TdkwDataRuleRefCheckValidator;

public class TdkwDataRuleDeleteCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new TdkwDataRuleRefCheckValidator());
    }
}
```

⚠️ 同样的 OP 类要在 hrcs_datarule 的 `delete` / `disable` / `unaudit` 三个操作上**分别挂载** · 才能覆盖所有"让规则失效"的场景。

### 验证

```bash
# 1. 创建一条规则 + audit + 在某动态授权方案里引用它
# 2. 进数据规则 · 选刚才那条 + 点删除
#    → 应该报错：
#       规则被以下对象引用 · 请先解除引用再操作:
#        - 方案: 客户经理-数据范围(id=12345)
# 3. 解除方案引用 + 重新删 → 应该成功
# 4. 同样测试 disable 操作 · 也应阻断
```

### 踩坑

- ❌ 用 LIKE 模糊查 rule 字段内容 · 不靠谱（FilterCondition JSON 序列化没标准）· 应该查"引用本规则 id"
- ❌ hrcs_dynascheme.permfilter 子表的字段名 / hrcs_role 子表的字段名 是**猜测** · 必须在 hrmp-hrcs-bussiness 反编译实证后填准确字段名
- ❌ Validator 里直接用 BusinessDataServiceHelper.load 全量加载方案 · 大数据量场景慢 · 用 QueryServiceHelper.queryDataSet 流式
- ❌ 报错信息一次列全部引用方 · 引用 1000 处时把 UI 撑爆 · 限制最多 10 条 + 总数提示
- ⚠️ 系统预置规则（issyspreset=true）→ 标品已经禁删 · ISV 不需要再加
- ⚠️ 即使没引用 · audit 状态规则也不能直接删（标品 HRBaseDataStatusOp 拦） · ISV 校验是在标品后执行

### 遵循的 PR 规范

- **PR-001** 自建 OP / Validator · 不继承场景专属类
- **PR-007** issyspreset 已被标品 HRBaseDataStatusOp 拦 · ISV 不需要重做
- **PR-009** 引用关系用 boid / id · 数据规则不是 HisModel · 直接用 id 即可（数据规则没有版本概念）
- **PR-010** Validator 在 onAddValidators 注册 · 不要在 beforeExecute

---

## CS-05 · save 后发 BEC 业务事件（hrcs_datarule 标品 0 处发 · ISV 自建）

**关联 Pattern**：`pattern/publish_bec_event/README.md`

### 需求

业务方说：数据规则变更（save / audit / disable）必须通知合规审计系统 + SIEM 系统 · 用于：

1. 合规审计：哪个用户在什么时候改了哪条规则
2. SIEM：监控权限收紧/放开 · 检测异常授权变更

### 标品现状（grep 实证）

```bash
$ grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
    knowledge/_sdk_audit/_decompiled/scenarios/hrcs_datarule/
（0 命中）
```

**结论**：hrcs_datarule 标品**完全不发布 BEC** · 必须 ISV 自建。

### 推荐方案

- **扩展点**：自建 ISV OP · 在 afterExecuteOperationTransaction 阶段调 `IEventService.triggerEventSubscribeJobs`
- **挂载方式**：并列挂在 hrcs_datarule 的 save / audit / disable / unaudit / enable / delete 操作上
- **触发时机**：afterExecute（PR-010 · 主事务已提交）· 不在事务内 · BEC 失败不回滚 DB

### 前置：在【开发平台】→【业务事件管理】预配置事件号

```
事件号: ${ISV_FLAG}_hrcs_datarule_changed
应用: hrcs
描述: 数据规则变更事件
变量: id (long), entitynum (string), operateType (string), beforeRule (string), afterRule (string)
```

### 调用链

```
（用户点 save / audit / disable）
    ↓
[OP afterExecute] 标品 OP 链跑完
    ↓
[OP afterExecute] ISV TdkwDataRuleBecPublishOp
    ├─ 取每条 dataEntity
    ├─ 拼 variables Map
    └─ IEventService.triggerEventSubscribeJobs("hrcs", "${ISV_FLAG}_hrcs_datarule_changed", msg, vars)
         ↓
       BEC 平台 · 调度订阅方
         ↓
       订阅方 IEventServicePlugin.handleEvent → 写审计日志/调 SIEM
```

### 代码框架（ISV BEC 发布 OP）

```java
package mycompany.${ISV_FLAG}.hrcs.opplugin;

import java.util.HashMap;
import java.util.Map;

import kd.bos.bec.api.IEventService;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.servicehelper.ServiceHelper;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.common.constants.perm.PermFormCommonUtil;

/**
 * ISV 数据规则 BEC 发布 OP
 * - 触发时机：afterExecuteOperationTransaction（PR-010 · 事务已提交）
 * - 事件号：${ISV_FLAG}_hrcs_datarule_changed（必须先在【业务事件管理】预配置）
 *
 * ⚠ 标品 hrcs_datarule 0 处发 BEC · 这是 ISV 完全自建的能力
 * ⚠ 同一个 OP 类需要在 6 个操作上分别挂载: save · audit · unaudit · disable · enable · delete
 */
public class TdkwDataRuleBecPublishOp extends HRDataBaseOp {

    private static final Log LOG = LogFactory.getLog(TdkwDataRuleBecPublishOp.class);
    private static final String EVENT_NUMBER = "${ISV_FLAG}_hrcs_datarule_changed";
    private static final String EVENT_APP = "hrcs";

    @Override
    public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("entitynum");
        e.getFieldKeys().add("rule");
        e.getFieldKeys().add("status");
        e.getFieldKeys().add("enable");
        e.getFieldKeys().add("id");
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        String operateType = this.getOperateKey(); // save / audit / disable / etc.
        IEventService svc = ServiceHelper.getService(IEventService.class);
        if (svc == null) {
            LOG.warn("BEC IEventService 未启用 · 跳过发布");
            return;
        }
        for (DynamicObject item : args.getDataEntities()) {
            try {
                long id = item.getLong("id");
                DynamicObject entityNumObj = item.getDynamicObject("entitynum");
                String entitynum = entityNumObj == null ? "" : String.valueOf(entityNumObj.getPkValue());
                String afterRule = item.getString("rule");
                String beforeRule = this.getOption().getVariableValue("originalRule_" + id, "");

                Map<String, Object> vars = new HashMap<>();
                vars.put("id", id);
                vars.put("entitynum", entitynum);
                vars.put("operateType", operateType);
                vars.put("status", item.getString("status"));
                vars.put("enable", item.getString("enable"));
                vars.put("beforeRule", beforeRule);
                // ⚠ 不要把完整 DynamicObject 塞 variables（PR-011 · BEC 反模式）
                // ⚠ 不要塞 afterRule 全文 · 太大 · 让订阅方按 id 自查
                vars.put("hasRuleChange", !beforeRule.equals(afterRule));
                vars.put("changedAt", System.currentTimeMillis());

                String message = "数据规则[" + id + "]在 entitynum=" + entitynum + " 上发生 " + operateType;
                svc.triggerEventSubscribeJobs(EVENT_APP, EVENT_NUMBER, message, vars);

                LOG.info("BEC published · event={} · ruleId={} · operate={}", EVENT_NUMBER, id, operateType);
            } catch (Exception ex) {
                // 容错 · BEC 失败不让主流程感知
                LOG.error("BEC publish failed for rule", ex);
            }
        }
    }
}
```

### 订阅方实现（IEventServicePlugin）

```java
package mycompany.${ISV_FLAG}.hrcs.bec;

import kd.bos.bec.api.IEventServicePlugin;
import kd.bos.bec.model.KDBizEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

public class TdkwDataRuleAuditSubscriber implements IEventServicePlugin {

    private static final Log LOG = LogFactory.getLog(TdkwDataRuleAuditSubscriber.class);

    @Override
    public void handleEvent(KDBizEvent event) {
        // 拿 variables
        Map<String, Object> vars = event.getVariables();
        long ruleId = ((Number)vars.get("id")).longValue();
        String operate = (String)vars.get("operateType");
        // 1. 写审计日志（自定义表 audit_log）
        // 2. 推 SIEM（HTTP 调用）
        LOG.info("audit: rule {} {} at {}", ruleId, operate, vars.get("changedAt"));
    }
}
```

### 验证

```bash
# 1. 在【开发平台】→【业务事件管理】预配置 ${ISV_FLAG}_hrcs_datarule_changed
# 2. 在【业务事件订阅】配置订阅 + 关联 TdkwDataRuleAuditSubscriber
# 3. 进数据规则 · save 一条规则
# 4. 看 BEC 调度日志：${ISV_FLAG}_hrcs_datarule_changed 应有 1 条记录
# 5. 看 TdkwDataRuleAuditSubscriber 日志：handleEvent 收到事件
```

### 踩坑

- ❌ **eventNumber 没在【业务事件管理】预配置** · BEC 不识别 · 调用静默失败（PR-011 关键前置）
- ❌ **在 beforeExecute 阶段发 BEC** · 主事务还未提交 · 万一回滚就产生脏事件（PR-010 · PR-011）
- ❌ **在 endOperationTransaction 阶段发 BEC** · 事务可能还没最终提交 · 同上脏事件风险
- ❌ **variables 里塞完整 DynamicObject** · BEC 用 JSON 序列化 · 大对象会过大 · 订阅方按 id 自查（PR-011）
- ❌ **afterExecute 抛异常** · 主事务已提交 · 异常被吞 · 但本条 BEC 也丢了 · 必须 try-catch
- ⚠️ **BEC 不是同步**：发出去后 · 订阅方不一定立即处理 · 业务上不能依赖"BEC 调用返回 = 订阅方已处理"
- ⚠️ **同一规则的 save 和 audit 会发两次 BEC** · 订阅方要做幂等（按 ruleId + operateType + changedAt 去重）
- ⚠️ **不要自接 Kafka/RabbitMQ**（PR-011 反模式）· BEC 是平台官方分发机制
- ⚠️ **BEC 跨多个分布式服务** · 发布到订阅最少 100 ms 延迟 · 业务对延迟敏感时考虑同步调用

### 遵循的 PR 规范

- **PR-001** 不继承 HRDataRuleSaveOp · 走并列挂
- **PR-010** afterExecuteOperationTransaction 是 BEC 发布的合理时机
- **PR-011** BEC 走平台事件中心 · 不自接 Kafka · variables 不塞大对象 · eventNumber 必须先在【业务事件管理】预配置

---

## CS-06 · 数据规则参数项扩展（自建分录子表 · ID 用 kd.bos.id.ID）

**关联 Pattern**：`pattern/add_sub_entity/README.md`

### 需求

业务方说：希望把数据规则变成"参数化模板"· 一条规则可以挂 N 个"参数项"（如 `:currentUserDept` / `:currentJobLevel`）· 实际过滤时按当前用户上下文动态替换。

### 推荐方案

- **扩展对象**：自建分录子表 `${ISV_FLAG}_hrcs_dr_param`（数据规则参数项）
- **扩展点**：modifyMeta add entryentity + 主表加 BasedataField 引用 + ISV FormPlugin 填默认参数 + ISV OP 替换参数后再 build SQL

### 子表设计

```
主表 hrcs_datarule
└── 分录 ${ISV_FLAG}_paramentry (子表)
    ├── ${ISV_FLAG}_paramkey: TextField · 参数键如 :currentUserDept
    ├── ${ISV_FLAG}_paramtype: ComboField · STRING / LONG / BOOLEAN
    ├── ${ISV_FLAG}_paramprovider: ComboField · CONST / CONTEXT_USER / CONTEXT_DEPT
    ├── ${ISV_FLAG}_paramconst: TextField · 静态值（CONST 模式）
    └── ${ISV_FLAG}_paramdesc: MuliLangTextField · 描述
```

### 调用链

```
[CS-01 类似] modifyMeta · 给 hrcs_datarule 加分录子表 ${ISV_FLAG}_paramentry
    ↓
[FormPlugin] 用户在 FilterGrid 配规则时 · 写参数引用如 :currentUserDept
    ↓
[FormPlugin] 用户在 ${ISV_FLAG}_paramentry 分录里配每个参数的 provider + 默认值
    ↓
[OP onAddValidators] ISV TdkwParamConsistencyValidator
    └─ 校验 rule 字段里出现的参数引用 · 都在 ${ISV_FLAG}_paramentry 分录里有定义
    ↓
[权限链消费] · ISV 自建参数解析器
    └─ 扫描 rule 字符串 · 替换 :xxx 为当前用户上下文值
    └─ 再调 FilterBuilder.buildFilter()
```

### 代码框架（modifyMeta 加分录 + ID 用 kd.bos.id.ID）

```python
# Step 1: modifyMeta 加分录子表
client.modify_meta(
    formId="hrcs_datarule",
    ops=[{
        "op": "add",
        "treeType": "entity",
        "elementType": "entryentity",
        "parentScope": "hrcs_datarule",
        "element": {
            "key": "${ISV_FLAG}_paramentry",
            "name": {"zh_CN": "规则参数项"},
            "fields": [
                {"fieldType": "TextField", "key": "${ISV_FLAG}_paramkey", "name": {"zh_CN": "参数键"}, "mustInput": True},
                {"fieldType": "ComboField", "key": "${ISV_FLAG}_paramtype", "name": {"zh_CN": "参数类型"},
                 "comboOptions": [
                    {"value": "S", "name": {"zh_CN": "字符串"}},
                    {"value": "L", "name": {"zh_CN": "长整型"}},
                    {"value": "B", "name": {"zh_CN": "布尔"}}
                 ]},
                {"fieldType": "ComboField", "key": "${ISV_FLAG}_paramprovider", "name": {"zh_CN": "提供方"},
                 "comboOptions": [
                    {"value": "CONST",        "name": {"zh_CN": "常量"}},
                    {"value": "CTX_USER",     "name": {"zh_CN": "当前用户"}},
                    {"value": "CTX_DEPT",     "name": {"zh_CN": "当前部门"}}
                 ]},
                {"fieldType": "TextField",     "key": "${ISV_FLAG}_paramconst", "name": {"zh_CN": "常量值"}},
                {"fieldType": "MuliLangTextField", "key": "${ISV_FLAG}_paramdesc", "name": {"zh_CN": "描述"}}
            ]
        }
    }]
)
```

**FormPlugin · 添加新参数行时给行 ID 用 kd.bos.id.ID（PR-005）**

```java
package mycompany.${ISV_FLAG}.hrcs.formplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.id.ID;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

/**
 * ISV 数据规则 - 参数项分录管理
 * 目的：演示 PR-005 · 行 id 用 kd.bos.id.ID · 不用 UUID
 */
public class TdkwParamEntryEdit extends HRDataBaseEdit {

    /**
     * 用户点"添加参数"按钮 → 给分录加一行 + 行 ID 由 ID.genLongId 生成
     */
    public void addNewParamRow(String paramKey, String paramType, String provider) {
        IDataModel model = this.getModel();
        DynamicObjectCollection entries = model.getEntryEntity("${ISV_FLAG}_paramentry");

        DynamicObject newRow = entries.addNew();
        newRow.set("id", ID.genLongId());                  // ✅ PR-005 · 用 kd.bos.id.ID
        newRow.set("${ISV_FLAG}_paramkey", paramKey);
        newRow.set("${ISV_FLAG}_paramtype", paramType);
        newRow.set("${ISV_FLAG}_paramprovider", provider);

        this.getView().updateView("${ISV_FLAG}_paramentry");
    }
}
```

**Validator · 一致性校验**

```java
package mycompany.${ISV_FLAG}.hrcs.opplugin.validator;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * ISV 数据规则参数一致性校验
 * - 校验 rule 字段里出现的 :xxx 参数引用 · 都在 ${ISV_FLAG}_paramentry 分录里有定义
 */
public class TdkwParamConsistencyValidator extends AbstractValidator {

    private static final Pattern PARAM_REF = Pattern.compile(":([a-zA-Z_][a-zA-Z0-9_]*)");

    @Override
    public void validate() {
        for (ExtendedDataEntity row : this.getDataEntities()) {
            DynamicObject entity = row.getDataEntity();
            String rule = entity.getString("rule");
            if (HRStringUtils.isEmpty(rule)) continue;

            Set<String> referencedKeys = new HashSet<>();
            Matcher m = PARAM_REF.matcher(rule);
            while (m.find()) {
                referencedKeys.add(m.group(1));
            }
            if (referencedKeys.isEmpty()) continue;

            // 收集分录已定义的参数
            Set<String> definedKeys = new HashSet<>();
            DynamicObjectCollection entries = entity.getDynamicObjectCollection("${ISV_FLAG}_paramentry");
            for (DynamicObject e : entries) {
                String k = e.getString("${ISV_FLAG}_paramkey");
                if (HRStringUtils.isNotEmpty(k)) {
                    definedKeys.add(k);
                }
            }

            // 找出 rule 引用了但分录里没定义的
            referencedKeys.removeAll(definedKeys);
            if (!referencedKeys.isEmpty()) {
                this.addErrorMessage(row,
                        "规则中引用了未定义的参数: " + String.join(", ", referencedKeys)
                                + "·请在'规则参数项'分录里补全定义");
            }
        }
    }
}
```

### 验证

```bash
# 1. modifyMeta 加分录 · getFormSchema 二次验证 ${ISV_FLAG}_paramentry 在
# 2. 进数据规则新建 · FilterGrid 配 :currentUserDept · 不在分录里加 → save 应报错
# 3. 在分录加 :currentUserDept(CTX_DEPT) → save 成功
# 4. 检查 t_hrcs_datarule 子表（实际表名按 modifyMeta 实抓 · 标品给的是 t_hrcs_datarule_tdkw_paramentry 类似）
#    → 行 ID 应该是 19 位 Snowflake · 不是 UUID
```

### 踩坑

- ❌ **行 id 用 UUID.randomUUID().toString()** · 苍穹有 ID.genLongId · UUID 占 36 char 不必要（PR-005）
- ❌ **行 id 用 System.currentTimeMillis()** · 高并发会撞（PR-005）
- ❌ **行 id 用 select max(id) + 1** · 分布式集群必坏（PR-005）
- ❌ **modifyMeta add entryentity 直接传** · ⚠️ 实证 modifyMeta add 参数名是 `fieldType / name / columnName`（不是 dataType / displayName）· 传错静默走 TextField
- ❌ **EmployeeField 等 HR SDK 扩展类型 OpenAPI 不支持**（cosmic 实证）· 用 BasedataField 替代
- ⚠️ 分录字段加 ISV 前缀（${ISV_FLAG}_）防止跟标品冲突
- ⚠️ FilterGrid 控件不直接支持参数化语法 · 业务方需要在文本框/特殊控件输入 · ISV FormPlugin 解析

### 遵循的 PR 规范

- **PR-005** 行 id / 流水号用 kd.bos.id.ID
- **PR-001** 不继承 HRDataRuleEditPlugin · 走并列挂

---

## CS-07 · 列表过滤定制（按 entitynum 分组 + 业务字段过滤）

**关联 Pattern**：`pattern/list_filter/README.md`

### 需求

业务方说：数据规则列表（hrcs_datarule 列表表单）按 entitynum 分组太散 · 希望：

1. 默认按当前用户能管理的业务对象过滤（HR 域管理员能管的实体清单）
2. 加自定义字段（CS-01 的 ${ISV_FLAG}_rulecategory）作为列表过滤条件
3. 按 status + enable 联合过滤"已生效"规则

### 推荐方案

- **扩展点**：自建 ISV ListPlugin · 挂在 hrcs_datarule 列表表单
- **挂载方式**：继承 HRDataBaseList · **不继承 HRBaseDataCommonList / HRBaseDataTplList**（标品列表插件）
- **机制**：覆写 setFilter 方法 · 加 QFilter 到列表查询

### 调用链

```
用户进入数据规则列表
    ↓
[ListPlugin] 标品 HRBaseDataCommonList / HRBaseDataTplList 跑通用列表能力
    ↓
[ListPlugin] ISV TdkwDataRuleListEnhance · 覆写 setFilter
    ├─ 拿当前用户能管理的实体清单（HRAdminService.queryAdminEntities）
    ├─ 加 QFilter("entitynum.number", "in", entityList)
    ├─ 加 status + enable 联合过滤（默认只看"已生效"）
    └─ 加 ${ISV_FLAG}_rulecategory 用户当前选择的过滤
    ↓
列表查询 SQL 拼上自定义 QFilter
```

### 代码框架（ISV ListPlugin · 自建）

```java
package mycompany.${ISV_FLAG}.hrcs.formplugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kd.bos.algo.DataSet;
import kd.bos.context.RequestContext;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.events.SetFilterEventArgs;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

/**
 * ISV 数据规则列表过滤增强
 * - 默认只看当前用户能管理的实体的规则
 * - 默认只看 status=audit AND enable=1 的"已生效"规则
 * - ${ISV_FLAG}_rulecategory 列表过滤项（CS-01 的字段）
 *
 * ⚠ 不继承 HRBaseDataCommonList / HRBaseDataTplList（场景专属类 · PR-001）
 */
public class TdkwDataRuleListEnhance extends HRDataBaseList {

    @Override
    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);

        long currentUserId = RequestContext.get().getCurrUserId();
        boolean isPlatformAdmin = PermissionServiceHelper.isAdminUser(currentUserId);

        // 平台 admin 不加业务对象过滤 · 看全部
        if (!isPlatformAdmin) {
            // 1. 限制只看当前 HR admin 能管的业务对象
            Set<String> manageable = queryManageableEntities(currentUserId);
            if (!manageable.isEmpty()) {
                e.getQFilters().add(new QFilter("entitynum.number", "in", manageable));
            } else {
                // 没有能管的实体 · 一行不显示
                e.getQFilters().add(new QFilter("1", "=", 0));
                return;
            }
        }

        // 2. 默认只看"已生效"规则（用户可手动改这个过滤）
        // ⚠ 通过自定义参数 showall=true 关闭默认过滤
        Object showAll = this.getView().getFormShowParameter().getCustomParam("${ISV_FLAG}_showall");
        if (!Boolean.TRUE.equals(showAll)) {
            e.getQFilters().add(new QFilter("status", "=", "C")); // audit
            e.getQFilters().add(new QFilter("enable", "=", "1"));
        }
    }

    /**
     * 查当前用户能管理的实体清单
     * - 简化实现：通过 hrcs_dynascheme 反查
     * - 真实场景应该用 HRAdminService 类公开 API（待反编译实证）
     */
    private Set<String> queryManageableEntities(long userId) {
        Set<String> entities = new HashSet<>();
        // 示例：用户能管的部门里的所有 form 都能看
        // 实际业务自定义实现
        return entities;
    }
}
```

### 注册方式

```bash
# 1. 通过【元数据】给 hrcs_datarule 主表单的"列表"挂插件
#    pluginType = LIST_FORM
#    className = mycompany.${ISV_FLAG}.hrcs.formplugin.TdkwDataRuleListEnhance
#
# 2. 或通过 cosmic-dev registerPlugin
client.register_plugin(
    formId="hrcs_datarule",
    targetType="LIST_FORM",   # ⚠ 大写枚举（不是 pluginType）
    className="mycompany.${ISV_FLAG}.hrcs.formplugin.TdkwDataRuleListEnhance",
    bizappId="<hrcs_bizappId>"
)
```

### 验证

```bash
# 1. 用普通 HR 管理员账号登录
# 2. 进数据规则列表
#    → 应该只看到"自己能管的业务对象 + 已生效"的规则
# 3. URL 加 customParam ${ISV_FLAG}_showall=true
#    → 应该看到所有规则（包括 draft / disable）
# 4. 用平台 admin 登录
#    → 看到全部业务对象的规则（不限制 manageable）
```

### 踩坑

- ❌ **继承 HRBaseDataCommonList / HRBaseDataTplList** 然后 super.setFilter · 标品类无 SDK 注解 · 跨版本会改（PR-001）
- ❌ **直接修改 e.getQFilters() 的引用** · 应该用 .add() 追加 · 不要 .clear() 否则砍掉标品过滤
- ❌ **在 setFilter 里查数据库** · 性能差 · 应该用 PageCache 缓存
- ❌ **覆写 setFilter 不调 super.setFilter** · 标品过滤丢失（HRDataBaseList super 实现关键能力）
- ⚠️ **F7 弹窗的 lookUp 模式也会调 setFilter** · ISV 过滤可能让 F7 反而过严 · 用 isLookUp 判断
- ⚠️ **大数据量 in 查询**（1000+ entity 数）· SQL 慢 · 必要时改成 join

### 遵循的 PR 规范

- **PR-001** 不继承标品列表插件（HRBaseDataCommonList / HRBaseDataTplList）· 走自建 ListPlugin 继承 HRDataBaseList
- **PR-008** 如果业务对象是 HisModel 时序基础资料 · 自定义 setFilter 里查时记得带 iscurrentversion=true
- **PR-009** 跨实体过滤用 boid（业务对象 number 是稳定的 · id 在 HisModel 会随版本变）
- **PR-007** 区分 issyspreset 系统预置 vs 业务自建 · 自建可改 · 系统预置不可（影响展示策略）

---

## 反模式速查（跨 7 CS 共享）

⚠️ 以下反模式适用于本场景所有 CS · 必须避免：

### A. 继承场景专属类（违反 PR-001）

| 类 | 角色 | 推荐做法 |
|---|---|---|
| `HRDataRuleEditPlugin` | hrcs_datarule 主表单 FormPlugin | 并列挂新 FormPlugin · 继承 `HRDataBaseEdit` |
| `HRDataRuleSaveOp` | hrcs_datarule save OP | 并列挂新 OP · 继承 `HRDataBaseOp` |
| `HRDataRuleSaveValidator` | hrcs_datarule save Validator | 并列加新 Validator · 继承 `AbstractValidator` |
| `HRAdminStrictPlugin` | hrcs 11 表单准入闸 | 复用即可 · 不要继承 |
| `HRBaseDataCommonList` / `HRBaseDataTplList` / `HRBasedataLogList` | 标品列表插件 | 自建 ListPlugin 继承 `HRDataBaseList` |
| `HisModelOPCommonPlugin` / `HisUniqueValidateOp` / `HisModelFormCommonPlugin` / `HisModelListCommonPlugin` | 平台时序内部类 | @SdkInternal · ISV 不得继承 |
| `AbsOrgBaseOp` | 组织域专属 OP | hrcs 不适用 · 不在白名单 |

### B. ID 生成（违反 PR-005）

- ❌ `UUID.randomUUID().toString()`
- ❌ `System.currentTimeMillis()`
- ❌ `select max(id) + 1` · 分布式必坏
- ✅ `ID.genLongId()` / `ID.genStringId()`

### C. BEC 发布（违反 PR-011）

- ❌ 自接 Kafka/RabbitMQ
- ❌ variables 里塞完整 DynamicObject
- ❌ eventNumber 没在【业务事件管理】预配置
- ❌ 在 beforeExecute / endOperationTransaction 阶段发 BEC（事务可能回滚）
- ✅ afterExecute 阶段发 + try-catch 容错

### D. HisModel 查询（违反 PR-008/PR-009）

- ❌ 查时序基础资料不加 `iscurrentversion=true`
- ❌ 引用 HisModel 实体存 id（应存 boid）
- ✅ F7 弹窗用 `HisModelServiceHelper.isInheritHisModelTemplate` 判定 + 加 iscurrentversion 过滤

### E. FormPlugin 死循环（违反 PR-004）

- ❌ propertyChanged 里直接 setValue 其他字段
- ✅ `getModel().beginInit(); getModel().setValue(X, v); getModel().endInit(); getView().updateView(X);`

### F. OP 阶段错位（违反 PR-010）

- ❌ 在 beforeExecute 注册 Validator（太晚 · 应该在 onAddValidators）
- ❌ 在 afterExecute 做校验（事务已提交 · 无法回滚）
- ❌ 在 endOperationTransaction 发外部事件（事务可能还未最终提交）
- ✅ Validator → onAddValidators · 校验/阻断 → beforeExecute · 通知/事件 → afterExecute

---

## 7 CS 综合速查表

| CS | 主题 | 风险 | 关键 PR | 关键扩展点 |
|---|---|---|---|---|
| CS-01 | 加自定义字段（规则分类）| 低 | PR-007 / PR-005 | modifyMeta add field |
| CS-02 | 字段联动（分类→F7 过滤）| 低 | PR-001 / PR-003 / PR-004 / PR-008 | propertyChanged + beforeF7Select |
| CS-03 | save / audit / disable 校验 + 补缺口 | 中 | PR-001 / PR-007 / PR-008 / PR-010 | onAddValidators + afterExecute |
| CS-04 | 删/禁前查下游引用 | 中 | PR-001 / PR-009 / PR-010 | Validator + QueryServiceHelper |
| CS-05 | save 后发 BEC 业务事件 | 中 | PR-001 / PR-010 / PR-011 | afterExecute + IEventService |
| CS-06 | 数据规则参数项扩展（分录子表）| 中 | PR-001 / PR-005 | modifyMeta add entryentity + ID.genLongId |
| CS-07 | 列表过滤定制 | 低 | PR-001 / PR-007 / PR-008 / PR-009 | ListPlugin setFilter |

---

## 附：跨 CS 通用工具类（按场景实抓 + 推荐）

| 场景需要 | 推荐 SDK | 实证位置 |
|---|---|---|
| 实体读 | `DynamicObject.getString/getLong` | save OP 实证 |
| 实体写 | OP: `entity.set` · FormPlugin: `getModel().setValue` | PR-003 |
| 序列化 | `SerializationUtils.toJsonString / fromJsonString` | rule 字段实证 |
| 查询 | `QueryServiceHelper.queryDataSet` + `QFilter` | hrcs_dynascheme 同样 |
| HR 基础资料服务 | `HRBaseServiceHelper` | save OP 实证 |
| ID 生成 | `kd.bos.id.ID` | PR-005 |
| HisModel 判定 | `HisModelServiceHelper.isInheritHisModelTemplate` | EditPlugin 实证 |
| 字符串工具 | `HRStringUtils` | 全场景 |
| 集合工具 | `HRCollUtil` / `org.apache.commons.collections.CollectionUtils` | EditPlugin 实证 |
| 日志 | `LogFactory.getLog` | 全场景 |
| 多语言消息 | `ResManager.loadKDString` | EditPlugin 实证 |
| 用户上下文 | `RequestContext.get().getCurrUserId()` | 全场景 |
| 权限缓存 | `HRPermCacheMgr.clearCache` | save OP 实证 |
| 权限通知 | `PermNotifyService.notifyByDataRule(Long)` | save OP 实证 |
| 数据规则日志 | `DataRuleLogServiceHelper.dataRuleLogInit` | save OP 实证 |

详见 `curated_sdk.json` 完整 SDK 清单。
