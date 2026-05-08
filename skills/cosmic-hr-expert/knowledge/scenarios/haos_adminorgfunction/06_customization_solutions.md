# 推荐定制方案 · 行政组织职能（haos_adminorgfunction）

> **状态**: 🟢 基于反编译 2 类 + INV-AF-01~07 + platform_rules 11 PR
> **confidence**: real_deploy（基础资料场景特性 · 5 个 CS · CS-05 走反指引）
> **结构**: 背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR

---

## CS-01 · 给 haos_adminorgfunction 加自定义业务字段

### 需求
业务方说：要给行政组织职能加上"职能分类"（核心/支撑/创新）或"适用规模"标签 · 用于报表分析 / 组织规划。

### 推荐方案
- **扩展对象**：`haos_adminorgfunction` 主实体
- **扩展点**：`modifyMeta(op=add field)`
- **风险**：低（基础资料 · 出厂数据扩展安全）
- **关联 INV**：INV-AF-03（出厂数据 isvCanModify=false 不能改 · 但加新字段 OK） + INV-AF-04（listRules 拦预置数据修改 · 但不拦 ISV 加字段 metadata 操作）
- **关联 PR**：PR-007 预置数据 number 不可改（不影响新字段）

### 调用链
```
Step 1: getDevInfo()                       // 拿 ISV 信息 (developerId / projectId)
Step 2: getBizApps()                       // 找 bizAppId / bizUnitId
Step 3: getFormSchema(formNumber=haos_adminorgfunction)  // 查目前 27 字段清单 · 防重名
Step 4: modifyMeta({
  formId: "haos_adminorgfunction",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "haos_adminorgfunction",   // 主实体 · 不是子表（本表无子表）
    element: {
      fieldType: "ComboField",             // 或 BasedataField / TextField 看业务
      key: "${ISV_FLAG}_funccategory",            // ⭐ ISV 前缀防覆盖
      name: {zh_CN: "职能分类", en_US: "Function Category"},
      mustInput: false,
      enum: [
        {value: "core", label: "核心 · 业务驱动"},
        {value: "support", label: "支撑 · 平台保障"},
        {value: "innovation", label: "创新 · 探索孵化"}
      ]
    }
  }]
})
Step 5: getFormSchema(haos_adminorgfunction) // ⭐ 二次验证落库（PR-006 之后的硬规则 6）
```

### 代码框架（Python · cosmic_devportal_client）
```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "210PRLH7NPN+", "number": "haos_adminorgfunction"}
)

# 加 ComboField 职能分类
designer.add_field(
    field_type="ComboField",
    name="职能分类",
    key="${ISV_FLAG}_funccategory",
    parent_entity_id=designer.base_entity_id,
    enum_options=[
        ("core", "核心 · 业务驱动"),
        ("support", "支撑 · 平台保障"),
        ("innovation", "创新 · 探索孵化")
    ]
)
designer.save()  # 一次 save click 提交
```

### 踩坑
- ❌ 字段 key 不带 ISV 前缀（如 `funccategory`）→ 标品升级被覆盖（PR-001 配套规则）
- ❌ 字段 key > 24 字符 → 数据库列名上限触顶 · 平台静默截断
- ❌ 加到子表 → 错 · **本表没有 entry 子表**（跟双胞胎 haos_orgchangereason 一致）· 加字段只能在主表
- ❌ 想给 issyspreset=true 的出厂数据"补默认值" → 平台升级会再次刷预置数据 · 你的默认值会丢
- ⚠ 加 BasedataField 引用 ISV 字典时 · refEntity 必须是已建好的 form · 否则 add 成功但运行时 F7 拿不到数据
- 💡 加字段前先查 `_shared/_standard_metadata/entity_metadata/haos_adminorgfunction.md` 确认现有 27 字段清单
- 💡 加字段后想在 OP 层读取 · 必须自建并列挂插件 + onPreparePropertys 显式声明（参考 CS-02 的 onPreparePropertys 写法 · 跟 09 案例同源）

### 关联 PR
- 遵循 PR-007 · 预置数据编码不可改（你加新字段不动 number）
- 遵循 PR-001 · 不继承 BaseDataBuOp（场景特有类）
- 遵循 PR-010 · OP 阶段读 ISV 字段必须 onPreparePropertys 声明

---

## CS-02 · 删除 / 禁用前置校验：检查 admin_org 反向引用 ⭐ 核心

### 需求
业务方说："最近发现有同事手贱删了一条职能 · 结果以前提交的几十条 admin_org 数据的职能字段全都游离了 · 出大事了。能不能加个保护？"

### 推荐方案
- **扩展对象**：`haos_adminorgfunction`
- **扩展点**：`onAddValidators@delete` + `onAddValidators@disable`（双拦）
- **实现模式**：并列挂插件（**不继承** BaseDataBuOp · PR-001）+ 注册 AbstractValidator
- **风险**：低（只读校验 · 不动数据）
- **关联 INV**：INV-AF-06（标品 delete/disable 没有反向引用校验）+ INV-AF-04（listRules 已拦预置数据修改 · CS-02 兜底拦删除）
- **关联 PR**：PR-001（并列挂）· PR-010（onAddValidators 阶段 · 不在 afterExecute）

### 扩展入口坐标
- 绑定表单：`haos_adminorgfunction`
- 绑定操作：`delete` · `disable`
- 推荐父类：**`HRDataBaseOp`**（HR 通用 OP 抽象基类 · 白名单合规 · 跟标品 BaseDataBuOp 同源）
- 关键重写方法：`onAddValidators(AddValidatorsEventArgs args)` — 注册自建 Validator

### 调用链（执行时）
```
delete 操作触发
  ↓
[onAddValidators] 多个 OP 插件按 RowKey 顺序注册 Validator：
  1. BaseDataDeletePlugin · 平台默认（无业务校验）
  2. CodeRuleDeleteOp · 释放编码池
  3. HRBaseDataStatusOp · 状态校验
  4. HRBaseDataLogOp · 日志
  5. ⭐ ISV 加的 AdminOrgFunctionRefValidator (本 CS)
  ↓
[Validator.validate()] AdminOrgFunctionRefValidator 遍历每条待删数据
  ↓ 对 dataEntity.id 查反向引用
HRBaseServiceHelper("haos_adminorg").isExists(
  new QFilter("adminorgfunction", "=", id)
    .and(new QFilter("iscurrentversion", "=", true)))  ⭐ HisModel 时序场景 · 必须加 iscurrentversion
  ↓ 命中 → addErrorMessage 拒绝
```

### 代码框架

**AdminOrgFunctionDeleteValidator.java**（白名单父类：AbstractValidator）：
```java
package com.kingdee.${ISV_FLAG}.haos.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

/**
 * 删除/禁用 haos_adminorgfunction 前的反向引用校验
 * 业务铁律：禁删被引用过的字典数据
 * 跟 haos_orgchangereason 的 ChangeReasonDeleteValidator 同模式 · 但
 *   1) admin_org 是 HisModel 时序场景 · 必须加 iscurrentversion=true
 *   2) BasedataField 单选关系 · 直字段查（不走 .fbasedataid 子表路径）
 */
public class AdminOrgFunctionDeleteValidator extends AbstractValidator {

    /** 唯一直接路径：haos_adminorg 的 adminorgfunction 单选字段 */
    private static final String ADMINORG_FORM = "haos_adminorg";
    private static final String REF_FIELD = "adminorgfunction";  // BasedataField 单选 · 直字段查

    @Override
    public void validate() {
        ExtendedDataEntity[] dataEntities = this.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) {
            return;
        }

        HRBaseServiceHelper adminOrgHelper = new HRBaseServiceHelper(ADMINORG_FORM);

        for (ExtendedDataEntity ext : dataEntities) {
            DynamicObject dataEntity = ext.getDataEntity();

            // 出厂数据双重保护（INV-AF-03 + INV-AF-04 listRules 已 UI 拦 · 这里兜底拦删除）
            if (dataEntity.getBoolean("issyspreset")) {
                this.addErrorMessage(ext, "系统预置职能不可删除 (issyspreset=true · INV-AF-03/04)");
                continue;
            }

            long id = dataEntity.getLong("id");

            // 单选关系反向查 · 关键：admin_org 是 HisModel 时序 · 加 iscurrentversion
            QFilter qf = new QFilter(REF_FIELD, "=", id)
                .and(new QFilter("iscurrentversion", "=", Boolean.TRUE))
                .and(new QFilter("enable", "=", "1"));

            if (adminOrgHelper.isExists(qf)) {
                this.addErrorMessage(ext, String.format(
                    "行政组织职能 [%s] 已被行政组织当前生效版本引用 · 不可删除 · 建议改用禁用",
                    dataEntity.getString("name")
                ));
            }
        }
    }
}
```

**AdminOrgFunctionRefCheckOp.java**（白名单父类：HRDataBaseOp）：
```java
package com.kingdee.${ISV_FLAG}.haos.opplugin.web;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * ISV 并列挂插件 · 拦 delete / disable 操作
 * RowKey 在标品插件之前（PR-002）
 */
public class AdminOrgFunctionRefCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator) new AdminOrgFunctionDeleteValidator());
    }
}
```

### 平台绑定步骤
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorgfunction`
2. 选择【操作】标签 → 找到 opKey = `delete` → 点【扩展插件】→ 新增 `com.kingdee.${ISV_FLAG}.haos.opplugin.web.AdminOrgFunctionRefCheckOp` · RowKey=1（早于标品 4 个）
3. 重复对 opKey = `disable` 做同样配置
4. 保存 → 部署生效

### 踩坑
- ❌ 把 Validator 写到 `beforeExecuteOperationTransaction` → 已经进事务 · addErrorMessage 拦能力差（PR-010 第 4 步是注册时机 · 不是校验时机）
- ❌ 用 `BusinessDataServiceHelper.load()` 而非 `HRBaseServiceHelper.isExists()` → 性能差（exists 走 count(1) limit 1）
- ❌ **没加 `iscurrentversion=true`** ⚠ admin_org 是 HisModel 时序场景 · 历史版本中可能持有该外键但当前版本已替换 · 不该影响删除决策（这是跟 haos_orgchangereason CS-02 的关键差异 · 必须加！）
- ❌ 用 `.fbasedataid` 路径查 → 错 · 这是 MulBasedataField 多选关系的查法 · 本场景是 BasedataField 单选 · 直 `<field> = id` 即可
- ❌ 继承 `BaseDataBuOp` → 违反 PR-001（场景特有类不能继承）
- ❌ 用 `kd.bos.servicehelper.QueryServiceHelper` 写 SQL Join → 没必要 · BasedataField 直字段查 ORM 自动走索引
- ⚠ 注意 `adminorgfunction` 是 haos_adminorg 上的字段名 · 别拼成 `adminorgfunctionid`（物理列名）或 `function`（业务简写）
- ⚠ 错误消息里带 `dataEntity.getString("name")` 让用户知道是哪条字典 · 调试效率翻倍
- 💡 校验跟 haos_orgchangereason 的 CS-02 几乎一致 · 都是基础资料反向引用模式 · 可以共用 helper 类减少重复代码
- 💡 性能：本场景查的是 admin_org 当前生效版本（带 iscurrentversion 索引）· ~3-5ms · 比 haos_orgchangereason 略慢 1-2ms（多 1 个版本过滤条件）但仍然用户无感

### 关联 PR
- 遵循 PR-001 · ISV 并列挂插件 · 不继承 BaseDataBuOp
- 遵循 PR-002 · RowKey 早于标品 4 插件 · 拦在标品之前
- 遵循 PR-010 · onAddValidators 阶段注册 · 不在 afterExecute
- 遵循 PR-007 · issyspreset=true 出厂数据连删都禁

---

## CS-03 · ctrlstrategy 联动追加自定义校验

### 需求
业务方说："标品 BdCtrlStrtgyShowLogicPlugin 联动 createorg/org/useorg 可见性是平台默认 · 但我们公司有个特殊规则：选了'同分配范围'(ctrlstrategy=8) 时 · 仅允许在指定 BU（如总部 / 业务中心）使用 · 跨 BU 选错会导致 admin_org 后续报表错。能加这个校验么？"

### 推荐方案
- **扩展对象**：`haos_adminorgfunction` 主表单
- **扩展点**：`propertyChanged@haos_adminorgfunction`（FormPlugin 层 · 不是 OP 层）
- **实现模式**：并列挂插件 · super 调用标品后追加自定义逻辑
- **风险**：中（联动逻辑跟标品冲突会导致字段反复刷新）
- **关联 PR**：PR-003（FormPlugin 用 setValue）· PR-004（防死循环）

### 扩展入口坐标
- 绑定表单：`haos_adminorgfunction`
- 推荐父类：**`HRBaseDataTplEdit`**（白名单 · 跟标品 HR 基础资料模板同源）· 或 `HRCoreBaseBillEdit`（更通用）
- ⚠ **不继承 BdCtrlStrtgyShowLogicPlugin**（bos 平台插件 · ISV 不应继承 bos.* · 改用并列挂模式）
- 关键重写方法：`propertyChanged(PropertyChangedArgs e)`

### 调用链（执行时）
```
[用户在表单改 ctrlstrategy 为 8（同分配范围）]
  ↓
propertyChanged 事件触发 · 多个 FormPlugin 按 RowKey 执行：
  1. BdCtrlStrtgyShowLogicPlugin.propertyChanged (标品 bos)
       → 联动 createorg/org/useorg 字段可见性
  2. HRBaseDataTplEdit.propertyChanged (标品 hbp)
       → 默认 HR 基础资料逻辑
  3. ⭐ ISV 加的 TdkwAdminOrgFunctionEditPlugin.propertyChanged
       → if (ctrlstrategy == "8" && !ALLOWED_BU_LIST.contains(orgId)) {
            getView().showWarning("'同分配范围'仅允许 X / Y / Z 三个 BU 选择");
            setValue("ctrlstrategy", oldValue);  // 回滚
            ↑ ⚠ 必须用 beginInit/endInit 包裹防死循环（PR-004）
         }
```

### 代码框架

**TdkwAdminOrgFunctionEditPlugin.java**：
```java
package com.kingdee.${ISV_FLAG}.haos.formplugin.web;

import java.util.Arrays;
import java.util.List;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * haos_adminorgfunction 表单层 · ctrlstrategy 联动校验
 * 跟 haos_orgchangereason 的 TdkwChangeReasonEditPlugin 同模式
 */
public class TdkwAdminOrgFunctionEditPlugin extends HRCoreBaseBillEdit {

    private static final String CTRLSTRATEGY = "ctrlstrategy";
    private static final String ORG = "org";
    private static final String ALLOCATION_SCOPE = "8";  // 标品 ctrlstrategy 枚举：同分配范围

    /** 客户私有"允许使用同分配范围"的 BU 白名单 · 应抽到 ISV 配置基础资料 · 此处仅示例 */
    private static final List<Long> ALLOWED_BU_LIST = Arrays.asList(1001L, 1002L, 1003L);

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);  // 调用 HRCoreBaseBillEdit 的 super · 安全
        String prop = e.getProperty().getName();
        if (!CTRLSTRATEGY.equals(prop)) {
            return;
        }

        String newCtrlstrategy = (String) this.getModel().getValue(CTRLSTRATEGY);
        if (HRStringUtils.isEmpty(newCtrlstrategy) || !ALLOCATION_SCOPE.equals(newCtrlstrategy)) {
            return;
        }

        DynamicObject orgObj = (DynamicObject) this.getModel().getValue(ORG);
        if (orgObj == null) {
            return;  // org 还没选 · 等用户继续操作
        }
        long orgId = orgObj.getLong("id");

        if (!ALLOWED_BU_LIST.contains(orgId)) {
            this.getView().showTipNotification("'同分配范围'(ctrlstrategy=8) 仅允许在指定 BU 使用 · 请重新选择");
            // PR-004 · 同字段 setValue 必须 beginInit / endInit 防死循环
            this.getModel().beginInit();
            Object oldValue = e.getChangeSet()[0].getOldValue();
            this.getModel().setValue(CTRLSTRATEGY, oldValue);
            this.getModel().endInit();
            this.getView().updateView(CTRLSTRATEGY);
        }
    }
}
```

### 平台绑定步骤
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorgfunction`
2. 选择【表单插件】标签 → 新增 `com.kingdee.${ISV_FLAG}.haos.formplugin.web.TdkwAdminOrgFunctionEditPlugin`
3. RowKey 排在标品 BdCtrlStrtgyShowLogicPlugin / HRBaseDataTplEdit 之后（取大值）· 这样标品先跑联动 · 你后跑校验
4. 保存 → 部署生效

### 踩坑
- ❌ 在 `propertyChanged` 里 `setValue("ctrlstrategy", X)` 不加 beginInit/endInit → 触发新的 propertyChanged 事件 → 死循环（PR-004）
- ❌ 不调 `super.propertyChanged(e)` → 标品 HRCoreBaseBillEdit 的默认行为丢失 · 可能影响其他字段联动
- ❌ RowKey 排在标品之前 → 标品后跑会再次刷新组织字段 · 你的回滚没效果
- ❌ 继承 `BdCtrlStrtgyShowLogicPlugin` → bos 平台插件 · 不在 HR SDK 白名单 · ISV 不应继承
- ❌ 在 OP 层（BaseDataBuOp）做 ctrlstrategy 校验 → 用户保存后才生效 · UI 体验差 · 应该走 FormPlugin 早拦
- ⚠ ALLOWED_BU_LIST 应抽到 ISV 配置表（如 ${ISV_FLAG}_allowed_bu）· 不要硬编码（跨环境主键不一致 · 参考 `feedback_har_values_not_authoritative.md`）
- 💡 也可以在 OP 层加一个并列插件做"二次校验"（带后端 fallback · 防止用户绕过 UI）· 但本场景业务低频 · 单层校验通常够用
- 💡 可以叠加 INV-AF-04 listRules 拦预置数据 · 即在判断前先 check `issyspreset=true` 直接 return（不要打扰预置数据用户）

### 关联 PR
- 遵循 PR-003 · FormPlugin 用 getModel().setValue（不用 entity.set）
- 遵循 PR-004 · 同字段 setValue 死循环防护用 beginInit/endInit
- 遵循 PR-001 · 不继承 BdCtrlStrtgyShowLogicPlugin · 并列挂

---

## CS-04 · 编码规则定制（走平台 CodeRule · PR-006）

### 需求
业务方说："职能编码我们想按职能分类前缀生成 · 核心 → 'CR_001' · 支撑 → 'SP_001' · 创新 → 'IN_001' · 不要平台默认的 'AF_001'。"

### 推荐方案
- **扩展对象**：编码规则基础资料（不是本表 · 是平台 `bos_coderule`）
- **扩展点**：【编码规则基础资料】配置（PR-006）
- **风险**：低（标品配置 · 不写代码）
- **关联 PR**：PR-006 CodeRuleOp 是平台模板插件 · 业务侧配即可

### 调用链（不写代码 · 走平台配置）
```
[平台菜单 · 系统服务云 · 业务流程服务 · 编码规则基础资料]
  ↓
新增编码规则
  ↓
基础资料 = haos_adminorgfunction
编码段定义：
  Seg 1：固定值 · 跟 ${ISV_FLAG}_funccategory 关联（核心=CR · 支撑=SP · 创新=IN）
  Seg 2：分隔符 · `_`
  Seg 3：流水号 · 3 位 · 自动递增
  ↓
保存 · 启用
  ↓
[运行时]
新建 haos_adminorgfunction 行 · 不填 number
  ↓ save 链 · CodeRuleOp 触发
读取 ${ISV_FLAG}_funccategory 字段值 · 拼前缀 · 加流水号
  ↓
落库 number = "CR_001"
```

### ⚠ 反指引（不要这么做）

错误尝试：
```java
// ❌ 自定义 OP 写编码逻辑
public class WrongAdminOrgFunctionCodeOp extends HRDataBaseOp {
    @Override
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        for (DynamicObject obj : e.getDataEntities()) {
            String prefix = obj.getString("${ISV_FLAG}_funccategory").substring(0, 2).toUpperCase();
            obj.set("number", prefix + "_" + getNextSeq());  // ⚠ 重新发明轮子
        }
    }
}
```

**为什么不行**：
1. CodeRuleOp 是 PR-006 平台模板插件 · 已经处理流水号并发安全 / 跨集群唯一性
2. 自定义 OP 走 select max+1 / UUID 都有问题（PR-005）
3. 业务侧调整规则要改代码 + 重发包 · 配置侧改规则只需 5 分钟

### 配置步骤
1. 进入【系统服务云 / 业务流程服务 / 编码规则基础资料】
2. 新增 · 基础资料选 `haos_adminorgfunction`
3. 编码段：
   - 第 1 段："字段值" · 字段 = ${ISV_FLAG}_funccategory · 取前 2 字符大写
   - 第 2 段："常量" · `_`
   - 第 3 段："流水号" · 长度 3 · 步长 1 · 起始 1
4. 启用 → 保存

### 踩坑
- ❌ 没启用编码规则 → 平台 CodeRule 不生效 · 用户每次自己填 number
- ❌ 编码规则的字段段引用错（如引用 `funccategory` 而不是 `${ISV_FLAG}_funccategory`）→ 拿不到值 · 编码段为空
- ⚠ 编码规则改了之后 · 已存在的数据**不会回填**（仅影响新增）· 历史数据要业务自己整理
- 💡 ISV 加 CS-01 字段时考虑 CodeRule 联动 · 字段名带前缀（${ISV_FLAG}_*）后编码规则配置时也要带前缀
- 💡 跨多语言场景 · CodeRule 编码段用"字段值"取多语言字段时 · 默认走当前用户语言 · 注意一致性
- 💡 跟 haos_orgchangereason CS-04 完全同模式 · 仅字段名差异（CR/MD/LO vs CR/SP/IN）· 实施步骤 1:1 复用

### 关联 PR
- 遵循 PR-006 · CodeRuleOp 是平台模板插件 · 不要重新发明
- 遵循 PR-005 · 流水号 / 业务编码用 ID.genStringId 或平台 CodeRule · 不用 max+1

---

## CS-05 · 自建子表行 / 业务编码 · PR-005 实证（兼 BEC 反指引）

### 需求 A · 自建子表
业务方说："我们要在 CS-01 加的字段基础上 · 给每条 adminorgfunction 加一张'适用业务线明细'子表（多对多） · 每行需要 id 和业务编码。"

### 推荐方案
- **扩展点**：`modifyMeta(op=add entity)` 加子表 + ISV 插件操作子表行 id
- **关键约束**：行 id 必须用 `kd.bos.id.ID.genLongId()` · 不能 UUID / timestamp / max+1（PR-005）
- **关联 PR**：PR-005（ID 生成强制场景 · 自建子表行）

### 调用链（建子表 · OpenAPI）
```
modifyMeta({
  formId: "haos_adminorgfunction",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "entity",
    parentScope: "haos_adminorgfunction",
    element: {
      entityType: "EntryEntity",
      key: "${ISV_FLAG}_bizline_entry",
      name: {zh_CN: "适用业务线明细"}
    }
  }, {
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "${ISV_FLAG}_bizline_entry",
    element: {
      fieldType: "BasedataField",
      key: "${ISV_FLAG}_bizline_id",
      name: {zh_CN: "业务线"},
      refEntity: "${ISV_FLAG}_bizline_dict",
      mustInput: true
    }
  }, {
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "${ISV_FLAG}_bizline_entry",
    element: {
      fieldType: "TextField",
      key: "${ISV_FLAG}_biz_code",
      name: {zh_CN: "业务编码"}
    }
  }]
})
```

### 代码框架（往子表新增行 · ID.genLongId 实证）

**TdkwAdminOrgFunctionEntryAppendOp.java**：
```java
package com.kingdee.${ISV_FLAG}.haos.opplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.id.ID;                           // ⭐ PR-005 唯一 ID 来源
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * 保存时自动给 ${ISV_FLAG}_bizline_entry 子表的每行补 id 和 biz_code
 * RowKey 在标品 8 个保存插件之前（PR-002）
 * 跟 haos_orgchangereason 的 TdkwChangeReasonEntryAppendOp 同模式 · 仅类名差异
 */
public class TdkwAdminOrgFunctionEntryAppendOp extends HRDataBaseOp {

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        // PR-010 第 2 步 · OP 默认不加载子表 · 必须显式声明
        e.getFieldKeys().add("${ISV_FLAG}_bizline_entry");
        e.getFieldKeys().add("${ISV_FLAG}_bizline_entry.${ISV_FLAG}_bizline_id");
        e.getFieldKeys().add("${ISV_FLAG}_bizline_entry.${ISV_FLAG}_biz_code");
    }

    @Override
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        for (DynamicObject dataEntity : e.getDataEntities()) {
            DynamicObjectCollection entries =
                dataEntity.getDynamicObjectCollection("${ISV_FLAG}_bizline_entry");
            for (DynamicObject row : entries) {
                if (row.getLong("id") <= 0L) {
                    // ⭐ PR-005 · 子表行 id 用 ID.genLongId · 不能 UUID/timestamp/max+1
                    row.set("id", ID.genLongId());
                }
                if (HRStringUtils.isEmpty(row.getString("${ISV_FLAG}_biz_code"))) {
                    // ⭐ PR-005 · 业务编码用 ID.genStringId
                    String prefix = "BL_";
                    row.set("${ISV_FLAG}_biz_code", prefix + ID.genStringId());
                }
            }
        }
    }
}
```

### 踩坑
- ❌ `row.set("id", UUID.randomUUID())` → UUID 跟苍穹分布式 ID 体系不兼容（PR-005）
- ❌ `row.set("id", System.currentTimeMillis())` → 并发碰撞 + 跟平台 ID 区段冲突
- ❌ `row.set("id", maxId + 1)`（select max+1）→ 并发安全问题 + 性能差
- ❌ 用 `ID.genLongId()` 给业务编码（应该用 `genStringId` · 长字符串带前缀更易读）
- ❌ `onPreparePropertys` 里没声明 `${ISV_FLAG}_bizline_entry` → beginOperationTransaction 拿到的 collection 是 null
- ⚠ 在 FormPlugin 层操作子表行新增也要用 `ID.genLongId()`：
  ```java
  // FormPlugin 例子 · CS-01 加新字段后批量增行
  DynamicObjectCollection entries = (DynamicObjectCollection) getModel().getValue("${ISV_FLAG}_bizline_entry");
  DynamicObject row = entries.addNew();
  row.set("id", ID.genLongId());  // PR-005 · 不要靠平台默认（部分场景默认 0L 导致重复）
  row.set("${ISV_FLAG}_bizline_id", bizLineId);
  ```

---

### 需求 B · BEC 反指引

业务方追问："想让 haos_adminorgfunction 的修改/禁用动作通知到下游模块 · 比如薪酬 / 考勤的同步刷缓存 · 用 BEC 行不行？"

### 实证判定（铁律 5 + 6）

```bash
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/haos_adminorgfunction/
# 0 处命中
```

**事实 1**：标品**没在本场景发任何 BEC 事件**。
**事实 2**：标品也**没在本场景做 BEC 订阅**。
**事实 3**：跟双胞胎场景 haos_orgchangereason 完全一致（也是 grep 0）。

### 反指引：为什么不做？

#### 不做 BEC 订阅方
- ❌ 订阅本场景的 BEC → 标品根本没发 · 啥都收不到
- ❌ 订阅其他场景的 BEC 来"间接管理" → 不该在字典层做 · 应在业务层 admin_org / homs_orgbatchchgbill 做

#### 不做 BEC 发布方
- ⚠ "ISV 自建发布方让下游订阅"在本场景**不推荐**：
  1. 基础资料变更频率低（一年改几次）· 没有"批量异步分发"的性能必要
  2. 下游若有需要直接查 t_haos_adminorgfunction 即可 · 没必要复制状态
  3. 标品本身没设计这条链路 · ISV 单边做会造成"半成品"（部分模块订阅 / 部分模块查表 · 不一致）
  4. 跟 haos_orgchangereason CS-05 反指引同源 · 双胞胎都不发 BEC

### 正确做法：业务变更通知应该挂在哪？

| 业务诉求 | 正确挂接位置 |
|---|---|
| 组织变动事件通知 | `homs_orgbatchchgbill` 的 `OrgBatchChgBillEffectOp.afterExecuteOperationTransaction`（标品发 BEC 实证 · 走 hjm 范式）|
| 行政组织变更通知 | `haos_adminorg` 的 admin_org_quick_maintenance CS-04 · 标品异步派单链 |
| 字典变更不应发独立通知 | 重新审视：业务真的需要通知"adminorgfunction 字典改了"么？多数情况下 · 业务真正关心的是"使用了某 adminorgfunction 的组织调整生效了" · 那是 admin_org 的事 |

### 极少数例外：ISV 必须自建 BEC 发布方时

如果业务确实需要（如有外部系统订阅基础资料变更）· 走 hjm 标品范式：
1. 在【开发平台 → 业务事件管理】预注册 eventNumber（PR-011）
2. 在 ISV 加并列挂 OP 插件 · 重写 `afterExecuteOperationTransaction`（PR-010 第 9 步）
3. 调 `IEventService.triggerEventSubscribeJobs(...)` 异步发事件
4. 用 `kd.bos.id.ID.genStringId()` 生成 traceId 放到 variables（PR-005）

但 **CS-05 BEC 反指引的核心是：基础资料场景默认不做 BEC**。

### 关联 PR
- 遵循 PR-005 · 子表行 id 用 ID.genLongId · 业务编码用 ID.genStringId
- 遵循 PR-001 · 并列挂 · 不继承 BaseDataBuOp
- 遵循 PR-002 · RowKey 早于标品 8 插件 · 在它们之前补好 id
- 遵循 PR-011 · BEC 走平台 · 不自接 MQ
- 遵循 PR-010 · 若极少数情况自建发布方 · 必须 afterExecuteOperationTransaction 阶段

---

## CS 关联 Pattern 速查

| CS | 关联 Pattern | 作用 |
|---|---|---|
| CS-01 | `add_field_extension` | 加 ISV 字段标准模式 |
| CS-02 | `add_unique_validation` + 反向引用查询模式（直字段单选 · 含 iscurrentversion）| 删除前置校验 · 查 admin_org 的 adminorgfunction 单选字段 |
| CS-03 | `override_plugin_behavior`（并列挂版）| 字段联动追加（ctrlstrategy）|
| CS-04 | `coderule_config`（无代码）| 编码规则定制走平台 PR-006 |
| CS-05 | `add_sub_entity` + `pr005_id_generation` | 子表 + ID 生成铁律 + BEC 反指引（双胞胎都不发）|

---

## 跟 haos_orgchangereason CS 的对比（双胞胎对照）

| CS | haos_adminorgfunction（本场景）| haos_orgchangereason |
|---|---|---|
| CS-01 加字段 | 主表加（无子表）| 主表加（无子表）|
| CS-02 删除前置校验 | **单选 BasedataField 直字段查 + iscurrentversion** ⭐ · ~3-5ms | 多选 MulBasedataField 走子表 join · ~3ms |
| CS-03 联动 | ctrlstrategy → 自定义校验（标品已联动可见性）| ctrlstrategy → 自定义校验（标品已联动可见性）|
| CS-04 | 编码规则配置（PR-006）| 编码规则配置（PR-006）|
| CS-05 | 子表 + ID 生成 + BEC 反指引（合并）| 子表 + ID 生成 + BEC 反指引（合并）|
| CS 总数 | **5（合并 CS-05）** | 5（合并 CS-05）|

→ **跟 haos_orgchangereason CS 数量一致 · 代码框架几乎可全量复用**。
→ 唯一关键差异：**CS-02 反向查询方式**（单选直字段 + iscurrentversion vs 多选子表 join）。
→ 其余 CS-01/CS-03/CS-04/CS-05 仅类名差异 · 业务逻辑同模式。
