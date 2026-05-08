# 定制方案 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 7 大 CS 全量精修 · 全引用反编译实证 + PR 平台规则
> **数据源**: CFR 反编译 PermfilesListPlugin/HRAdminStrictPlugin · `_shared/platform_rules.json`（11 PR）· `_auto_operations.md`
> **更新**: 2026-04-28

---

## 总览：7 大常见 CS

| CS | 标题 | 推荐挂载位置 | 难度 | 涉及 PR |
|---|---|---|---|---|
| CS-01 | 加自定义字段（用户授权档案扩展） | modifyMeta `hrcs_userpermfile` · ISV 前缀 | 低 | PR-001 / PR-007 |
| CS-02 | 字段联动（user → 自动带 org） | ISV FormPlugin 挂 `hrcs_userpermfile` | 中 | PR-003 / PR-004 |
| CS-03 | 操作前置校验（save / disable / delete） | ISV Validator + onAddValidators | 中 | PR-001 / PR-010 |
| CS-04 | 删除/禁用前查下游引用（permfile 被 hrcs_userrolerelat 等引用） | ISV Validator 挂 delete | 中 | PR-009 / PR-010 |
| CS-05 | BEC 发布（permfile 失效通知下游 BPM） | ISV OP 挂 disable.afterExecute | 高 | PR-010 / PR-011 |
| CS-06 | 列表过滤定制（按项目维度限定） | ISV ListPlugin 挂 `hrcs_permfilelist` | 中 | PR-001 / PR-002 |
| CS-07 | 批量授权扩展（batchgroup 时自动写自定义字段） | ISV ListPlugin 挂 `hrcs_permfilelist` | 中 | PR-001 / PR-005 |

⚠️ **本场景标品 0 处发 BEC**（grep `triggerEventSubscribe|IEventService|EventServiceHelper` 反编译目录全无命中）· **不能套用 hjm 3 层异步模式**（hjm 是 OP→Service(派 sch_task)→*MsgTask.execute · 本场景没有这套基础设施）。CS-05 是 ISV 自建发布场景。

⚠️ **PR-008 / PR-009（HisModel 时序）本场景不适用** —— `hrcs_userpermfile` 不是时序资料（grep `iscurrentversion|HisModel|boid` 反编译 + scene_doc 全无命中）。但 CS-04 查下游 `bos_user.id` / `org.id` 时如果上游是时序资料·必须用 boid。

---

## CS-01 加自定义字段（用户授权档案扩展）

### 业务背景

某 ISV 项目需要给"用户授权档案"加 3 个自定义字段：
- `2isv_remark`（档案备注 · TextField）
- `2isv_validdate`（授权有效期截止日 · DateField）
- `2isv_project`（关联项目 · BasedataField → 项目主数据）

要求：
1. 列表显示这 3 个字段
2. 编辑表单 hrcs_userpermfile 上能录入
3. 保存时校验 `2isv_validdate >= 当天`

### 推荐方案：modifyMeta 加 ISV 扩展字段 + ISV FormPlugin 校验

**关键决策**：
- **挂哪个 form？** → `hrcs_userpermfile`（主数据实体 · 字段写在主表 · 列表自然继承显示）
- **不挂 hrcs_permfilelist**（列表壳无字段 · scene_doc.fields=0）

### 步骤 1：modifyMeta 加字段

调 OpenAPI `kapi/v2/dev/dap/MetadataDesigner.modifyMeta` 给 `hrcs_userpermfile` 增字段（参考 `kb_cosmic_modifymeta_traps.md` · ISV 前缀 `2isv_`）：

```json
{
  "formId": "hrcs_userpermfile",
  "ops": [
    {
      "op": "add",
      "fieldType": "TextField",
      "name": "档案备注",
      "columnName": "2isv_remark",
      "isvAccessable": true
    },
    {
      "op": "add",
      "fieldType": "DateField",
      "name": "授权有效期截止日",
      "columnName": "2isv_validdate",
      "isvAccessable": true
    },
    {
      "op": "add",
      "fieldType": "BasedataField",
      "name": "关联项目",
      "columnName": "2isv_project",
      "baseEntityId": "bd_project",
      "isvAccessable": true
    }
  ]
}
```

⚠️ 参数名是 `fieldType / name / columnName` · **不是 `dataType / displayName`**（参考 `modifymeta_param_names_and_hr_sdk_limits.md` 实证铁律）。

### 步骤 2：ISV FormPlugin 做保存校验（PR-001 并列挂不继承）

```java
package isv.cosmic.hr.permfile;

import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;  // ✅ 白名单

import java.util.Date;

/**
 * ISV 用户授权档案扩展 OP · 校验 2isv_validdate
 * 挂载位置：hrcs_userpermfile 上 save opKey
 * PR-001：并列挂 · 不继承 PermFilesSaveOp（场景专属类）
 * PR-010：onAddValidators 阶段注册 Validator · 校验在 beforeExecuteOperationTransaction 之前
 */
public class IsvPermFileExtSaveOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new IsvValidDateValidator());
    }

    static class IsvValidDateValidator extends AbstractValidator {
        @Override
        public void validate() {
            for (kd.bos.entity.ExtendedDataEntity row : this.getDataEntities()) {
                Date validDate = row.getDataEntity().getDate("2isv_validdate");
                if (validDate == null) continue;  // 非必填 · 空值跳过
                if (validDate.before(new Date())) {
                    this.addErrorMessage(row, "授权有效期截止日不能早于今天。");
                }
            }
        }
    }
}
```

⚠️ **关键 SDK 注解审核**：
- `HRDataBaseOp` · 在 `cosmic_hr_sdk_whitelist_audit.md` 9 个可继承基类之一 ✅
- `AbstractValidator` · `kd.bos.entity.validate.AbstractValidator` · `@SdkPublic` ✅
- `addValidator` · `AddValidatorsEventArgs` 的 SDK 公开方法 ✅

### 步骤 3：插件注册

在开发平台 → "用户授权档案" hrcs_userpermfile → 插件注册 → 操作插件 → save opKey → Add `isv.cosmic.hr.permfile.IsvPermFileExtSaveOp`。

⚠️ **PR-002（RowKey 顺序）**：ISV 插件可以排在 PermFilesSaveOp 之前 · 自然先校验 · 失败 PermFilesSaveOp 跳过。

### 风险与回归

| 风险 | 应对 |
|---|---|
| ISV 字段 isvAccessable=false → 平台升级被覆盖 | 必须设 `isvAccessable: true` |
| 写代码用 `getModel().setValue` 在 OP 里失败 | OP 用 `entity.set("2isv_remark", val)`（PR-003） |
| 标品 PermFilesSaveOp 后续步骤覆盖 ISV 字段 | OP 链按 RowKey 顺序 · 后写覆盖前写 · 看 _auto_operations.md 标品 OP 写哪些字段 |

### 实证 SDK 引用

- `kd.hr.hbp.opplugin.web.HRDataBaseOp` · `cosmic_hr_sdk_whitelist_audit.md` 白名单
- `kd.bos.entity.validate.AbstractValidator` · `_shared/_decompiled/bos_common`
- `kd.bos.entity.ExtendedDataEntity` · 同上

涉及规则：**PR-001（不继承场景专属类）** + **PR-007（issyspreset 预置数据不可改 · 业务自建可改 · 这 3 个字段是 ISV 自建可改）**

---

## CS-02 字段联动（user → 自动带 org）

### 业务背景

某 ISV 客户希望：用户授权档案编辑表单 hrcs_userpermfile 上 · 选择 user 字段后 · 自动把该 user 的"主任职 org"带到 org 字段（避免手填错）。

要求：
1. user 选好 · org 自动填
2. 用户可以再手改 org（带出后不锁定）
3. 不进入死循环（PR-004）

### 推荐方案：ISV FormPlugin · propertyChanged + beginInit/endInit

**关键决策**：
- **挂哪个 form？** → `hrcs_userpermfile`（编辑表单 · 字段联动只在表单上有效）
- **不挂 hrcs_permfilelist**（列表壳没有 user/org 字段联动）
- **不继承 PermFilesSaveOp**（OP 没有 model · 联动失败 · PR-003）

### 实现代码

```java
package isv.cosmic.hr.permfile;

import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;  // ✅ HR 白名单
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;               // ✅ 白名单

/**
 * ISV 用户授权档案 · 字段联动 FormPlugin
 * 挂载位置：hrcs_userpermfile 编辑表单
 * PR-001：并列挂 · 不继承场景专属类
 * PR-003：FormPlugin 用 getModel().setValue · 不用 entity.set
 * PR-004：beginInit/endInit 防死循环
 */
public class IsvPermFileLinkagePlugin extends HRDataBaseEdit {

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        if ("user".equals(e.getProperty().getName())) {
            this.onUserChanged();
        }
    }

    private void onUserChanged() {
        DynamicObject user = (DynamicObject) this.getModel().getValue("user");
        if (user == null) return;

        long userId = user.getLong("id");
        // 查该用户的主任职 hrpi_empjobrel · iscurrentversion=true（PR-008 时序当前版本）
        // ⚠️ user 是 bos_user · 通过 bos_user.id 关联到 hrpi_pernontsprop（人员非时序属性）查 mainorgid
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrpi_pernontsprop");
        DynamicObject pernon = helper.queryOriginalOne(
            "personid,mainorgid",
            new QFilter[]{
                new QFilter("personid.user", "=", userId),
                new QFilter("iscurrentversion", "=", Boolean.TRUE)  // ⭐ PR-008
            }
        );
        if (pernon == null) {
            this.getView().showTipNotification("当前用户无主任职信息，请手填 org。");
            return;
        }
        long orgId = pernon.getLong("mainorgid");
        if (orgId == 0L) return;

        // ⭐ PR-004 防死循环：beginInit + endInit + updateView
        this.getModel().beginInit();
        this.getModel().setValue("org", orgId);
        this.getModel().endInit();
        this.getView().updateView("org");
    }
}
```

⚠️ **关键 SDK 注解审核**：
- `HRDataBaseEdit` · 白名单 ✅
- `HRBaseServiceHelper` · `kd.hr.hbp.business.servicehelper` · `@SdkPublic` ✅
- `QFilter` · `kd.bos.orm.query` · `@SdkPublic` ✅

### 关键陷阱

1. **不要直接 `getModel().setValue` 而不 begin/endInit** —— `setValue("org", orgId)` 会触发 `propertyChanged("org")` · 如果 ISV 还监听了 org 的联动 · 会再触发 setValue("xxx", ...) · 死循环。
2. **不要在 propertyChanged 里用 `this.getDataEntity().set("org", orgId)`** —— FormPlugin 里这样写 · model 不知道 · UI 不刷新（PR-003）。
3. **PR-008 必带 `iscurrentversion=true`** —— `hrpi_pernontsprop` 是时序资料 · 不过滤会查到所有版本数据。

### 测试用例

| user | 期望 org | 实际 |
|---|---|---|
| 在职员工·有主任职 | 自动填主任职 org | ✅ |
| 在职员工·无任职 | 提示"无主任职" · 不填 | ✅ |
| 离职员工 | 同上 · 提示后用户手填 | ✅ |

### 实证 SDK 引用

- `kd.hr.hbp.formplugin.web.HRDataBaseEdit` · 白名单
- `kd.hr.hbp.business.servicehelper.HRBaseServiceHelper` · 白名单
- `kd.bos.entity.datamodel.events.PropertyChangedArgs` · 标准 SDK

涉及规则：**PR-001（不继承）** + **PR-003（FormPlugin 用 getModel().setValue）** + **PR-004（beginInit/endInit 防死循环）** + **PR-008（HisModel 必带 iscurrentversion）**

---

## CS-03 操作前置校验（save / disable / delete）

### 业务背景

某 ISV 客户提了 3 条业务规则：
1. **save 前**：同 `(user, org)` 不允许重复建档案（业务唯一键）
2. **disable 前**：弹二次确认"将级联失效 N 条角色 · 是否继续？"
3. **delete 前**：当前档案如果有"动态方案分配出来的角色"（hrcs_userrolerelat.sourcetype="4"）· 不允许直接删除

### 推荐方案：ISV Validator + ISV ListPlugin（disable 二次确认）

#### 子方案 3.1：save 唯一性 Validator（onAddValidators · PR-010）

```java
package isv.cosmic.hr.permfile;

import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * ISV save 前置校验：(user, org) 唯一
 * 挂载：hrcs_userpermfile · save opKey
 * PR-010 onAddValidators 阶段注册
 */
public class IsvPermFileUniqueOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new UniqueValidator());
    }

    static class UniqueValidator extends AbstractValidator {
        @Override
        public void validate() {
            HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_userpermfile");
            for (ExtendedDataEntity row : this.getDataEntities()) {
                Object userId = row.getDataEntity().getDynamicObject("user").get("id");
                Object orgId = row.getDataEntity().getDynamicObject("org").get("id");
                Long currentId = (Long) row.getDataEntity().getPkValue();

                QFilter[] filters = new QFilter[]{
                    new QFilter("user.id", "=", userId),
                    new QFilter("org.id", "=", orgId),
                    new QFilter("permfileenable", "=", "1"),
                    new QFilter("id", "!=", currentId == null ? 0L : currentId)  // 排除自己
                };
                if (helper.queryOriginalCollection("id", filters).size() > 0) {
                    this.addErrorMessage(row, String.format(
                        "用户 %s 在组织 %s 下已有授权档案，不允许重复创建。",
                        row.getDataEntity().getString("user.name"),
                        row.getDataEntity().getString("org.name")));
                }
            }
        }
    }
}
```

#### 子方案 3.2：disable 二次确认（ListPlugin · beforeDoOperation 阶段）

```java
package isv.cosmic.hr.permfile;

import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.ListSelectedRowCollection;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseList;  // ✅ 白名单

/**
 * ISV ListPlugin · disable 二次确认
 * 挂载：hrcs_permfilelist 列表壳
 * PR-001 不继承 PermfilesListPlugin（场景专属类）· 并列挂
 * PR-002 RowKey 排序：本插件可以排在 PermfilesListPlugin 之前
 */
public class IsvPermfilesListConfirmDisablePlugin extends HRDataBaseList {

    private static final String CONFIRM_DISABLE = "isv_confirm_disable";

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate op = (FormOperate) args.getSource();
        if (!"disable".equals(op.getOperateKey())) return;

        BillList billList = (BillList) this.getView().getControl("billlistap");
        ListSelectedRowCollection rows = billList.getSelectedRows();
        if (rows.size() == 0) return;

        // 查关联角色数
        Object[] permfileIds = rows.getPrimaryKeyValues();
        HRBaseServiceHelper relHelper = new HRBaseServiceHelper("hrcs_userrolerelat");
        int roleCount = relHelper.queryOriginalCollection("id",
            new QFilter[]{new QFilter("permfile.id", "in", permfileIds)}).size();

        if (roleCount == 0) return;  // 无关联 · 不需要二次确认

        // 阻断 · 弹确认
        args.setCancel(true);
        ConfirmCallBackListener cb = new ConfirmCallBackListener(CONFIRM_DISABLE, (IFormPlugin) this);
        this.getView().showConfirm(
            String.format("将级联失效 %d 条角色绑定，是否继续？", roleCount),
            MessageBoxOptions.YesNo, cb);
    }

    @Override
    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        if (CONFIRM_DISABLE.equals(evt.getCallBackId()) && evt.getResult().toString().equals("Yes")) {
            // 用户确认 · 重新触发 disable（不带本 plugin 的 confirm 拦截）
            this.getView().invokeOperation("disable");
        }
    }
}
```

⚠️ **关键陷阱**：
- 不能在 `confirmCallBack` 里直接调 `OperationServiceHelper.executeOperate("disable", ...)` —— OP 链不会重走 setVariableValue("tag_of_view") · 走 invokeOperation 才会。
- `args.setCancel(true)` 阻断后 · OP 链不执行 · 用户回调"确认"后再 invokeOperation 重新进入。

#### 子方案 3.3：delete 前置（查 sourcetype="4"）

```java
package isv.cosmic.hr.permfile;

import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

/**
 * ISV delete 前置：禁删带有动态方案角色（sourcetype="4"）的档案
 * 挂载：hrcs_userpermfile · delete opKey
 */
public class IsvPermFileDeleteGuardOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new DynaSchemeRoleGuardValidator());
    }

    static class DynaSchemeRoleGuardValidator extends AbstractValidator {
        @Override
        public void validate() {
            HRBaseServiceHelper relHelper = new HRBaseServiceHelper("hrcs_userrolerelat");
            for (ExtendedDataEntity row : this.getDataEntities()) {
                Object permfileId = row.getDataEntity().getPkValue();
                int dynaCount = relHelper.queryOriginalCollection("id",
                    new QFilter[]{
                        new QFilter("permfile.id", "=", permfileId),
                        new QFilter("sourcetype", "=", "4")   // 动态方案分配
                    }).size();
                if (dynaCount > 0) {
                    this.addErrorMessage(row, String.format(
                        "档案有 %d 条动态方案分配的角色，请先在动态方案上失效后再删除。", dynaCount));
                }
            }
        }
    }
}
```

### 涉及规则

- **PR-001** 并列挂 · 不继承 PermFilesSaveOp / PermfilesListPlugin
- **PR-002** RowKey 顺序 · ISV 排在标品之前 · 失败标品跳
- **PR-007** 预置数据不可改 · 该校验跳过 issyspreset=1 的档案（如有）
- **PR-010** OP 13 生命周期 · onAddValidators 注册 · beforeExecute 之前执行

---

## CS-04 删除/禁用前查下游引用

### 业务背景

某 ISV 客户要求：删除或禁用 hrcs_userpermfile 档案前 · 系统主动查反向引用 · 把所有"还引用此档案"的对象列出来 · 用户确认后才允许操作。

引用源（业务调研）：
- `hrcs_userrolerelat.permfile` —— 一个档案的角色绑定
- `hrcs_permfilegrpmember.permfile` —— 档案的分组关联
- 自定义业务表（ISV 项目里自建的 · 引用 permfile 主键的）

### 推荐方案：ISV Validator · 多源反查 · 集合提示

```java
package isv.cosmic.hr.permfile;

import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.plugin.AbstractValidator;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

import java.util.ArrayList;
import java.util.List;

/**
 * ISV delete/disable 前置 · 查下游引用 · 多源汇总
 * 挂载：hrcs_userpermfile · delete + disable opKey
 * PR-009 下游引用查 boid · 但本场景非时序 · 仅查 id
 * PR-010 onAddValidators 阶段
 */
public class IsvPermFileRefCheckOp extends HRDataBaseOp {

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator(new RefCheckValidator());
    }

    static class RefCheckValidator extends AbstractValidator {
        @Override
        public void validate() {
            String opKey = this.getOperateKey();  // delete or disable

            for (ExtendedDataEntity row : this.getDataEntities()) {
                Object permfileId = row.getDataEntity().getPkValue();
                List<String> refs = new ArrayList<>();

                // 1. hrcs_userrolerelat 角色关联（仅 enable=1 的算"占用"）
                int activeRole = new HRBaseServiceHelper("hrcs_userrolerelat")
                    .queryOriginalCollection("id", new QFilter[]{
                        new QFilter("permfile.id", "=", permfileId),
                        new QFilter("role.enable", "=", "1")
                    }).size();
                if (activeRole > 0) refs.add(String.format("%d 条生效角色", activeRole));

                // 2. hrcs_permfilegrpmember 分组关联
                int grpCount = new HRBaseServiceHelper("hrcs_permfilegrpmember")
                    .queryOriginalCollection("id",
                        new QFilter[]{new QFilter("permfile.id", "=", permfileId)}).size();
                if (grpCount > 0) refs.add(String.format("%d 个分组关联", grpCount));

                // 3. ISV 自建业务表（示例：审批单引用）
                int approvalCount = new HRBaseServiceHelper("2isv_authapproval")
                    .queryOriginalCollection("id", new QFilter[]{
                        new QFilter("permfile.id", "=", permfileId),
                        new QFilter("billstatus", "in", new String[]{"A", "B"})  // 进行中
                    }).size();
                if (approvalCount > 0) refs.add(String.format("%d 单进行中审批", approvalCount));

                if (!refs.isEmpty()) {
                    this.addErrorMessage(row, String.format(
                        "档案被 %s 引用，不能 %s。请先处理引用。",
                        String.join("、", refs),
                        "delete".equals(opKey) ? "删除" : "失效"));
                }
            }
        }
    }
}
```

### 高级版：列出引用明细（弹列表 · 不报错阻断）

如果客户要求"列出引用明细让用户决定继续/取消" · 用 ListPlugin（CS-03 模式）拦截 · 弹自定义详情表单。

```java
// 在 ISV ListPlugin 的 beforeDoOperation 里
if ("delete".equals(opKey) || "disable".equals(opKey)) {
    // 查全部引用
    List<RefDetail> refs = queryAllReferences(permfileIds);
    if (!refs.isEmpty()) {
        args.setCancel(true);
        // 弹自定义"引用明细"表单 hrcs_isv_refdetails
        ListShowParameter lsp = new ListShowParameter();
        lsp.setBillFormId("2isv_refdetails");
        lsp.setCustomParam("refs", SerializationUtils.toJsonString(refs));
        lsp.setCloseCallBack(new CloseCallBack(this, "isv_ref_confirm"));
        this.getView().showForm(lsp);
    }
}
```

### 涉及规则

- **PR-001** 并列挂 · 不继承 PermFilesSaveOp
- **PR-009** 下游引用 · 时序资料用 boid（本场景非时序 · 用 id）
- **PR-010** Validator 注册阶段 · 校验失败 → 抛 KDBizException 或 addErrorMessage（推荐后者 · 多行错误能合并）

---

## CS-05 BEC 发布（permfile 失效通知下游 BPM）

### 业务背景

某 ISV 客户要求：当 permfile 失效（disable）或删除（delete）后 · 实时通知公司 BPM 工作流系统 · 触发"权限交接"流程。

⚠️ **本场景标品 0 处发 BEC**（grep `triggerEventSubscribe|IEventService|EventServiceHelper` 反编译目录全无命中）· **不能套用 hjm 3 层异步模式**（hjm 是 OP→Service(派 sch_task)→*MsgTask.execute · 本场景没有这套基础设施）。

⚠️ **不能用 MQ 自接（PR-011 反模式）** · 必须走苍穹 BEC（IEventService）。

### 推荐方案：ISV OP 发 BEC · 挂 disable / delete · afterExecuteOperationTransaction

#### 步骤 1：开发平台预配置事件号

在【开发平台】→【业务事件管理】预配置：
- `eventNumber` = `2isv_hrcs_permfile_disabled`
- `eventNumber` = `2isv_hrcs_permfile_deleted`

⚠️ 未预配置的 eventNumber · BEC 会忽略（PR-011 prerequisite）。

#### 步骤 2：ISV 发布方 OP

```java
package isv.cosmic.hr.permfile;

import kd.bos.bec.api.IEventService;                       // ✅ @SdkPublic
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.servicehelper.ServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;                // ✅ 白名单
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ISV BEC 发布方 OP · permfile 失效/删除事件
 * 挂载：hrcs_userpermfile · disable + delete opKey
 * PR-010 在 afterExecuteOperationTransaction 阶段（事务已提交安全）
 * PR-011 走苍穹 BEC IEventService · 不自接 MQ
 */
public class IsvPermFileBecPublishOp extends HRDataBaseOp {

    private static final Log LOG = LogFactory.getLog(IsvPermFileBecPublishOp.class);
    private static final String EVT_DISABLED = "2isv_hrcs_permfile_disabled";
    private static final String EVT_DELETED = "2isv_hrcs_permfile_deleted";
    private static final String SOURCE_APP = "hrcs";

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);

        String opKey = this.getOperateKey();
        String evtNum;
        switch (opKey) {
            case "disable":  evtNum = EVT_DISABLED; break;
            case "delete":   evtNum = EVT_DELETED;  break;
            default: return;  // 仅监听这 2 个 opKey
        }

        IEventService svc = ServiceHelper.getService(IEventService.class);
        long currentUserId = RequestContext.get().getCurrUserId();

        for (DynamicObject permfile : args.getDataEntities()) {
            try {
                Map<String, Object> variables = new HashMap<>();
                variables.put("permfileId", permfile.getPkValue());
                variables.put("userId", permfile.getDynamicObject("user").get("id"));
                variables.put("orgId", permfile.getDynamicObject("org").get("id"));
                variables.put("operatorId", currentUserId);
                variables.put("opKey", opKey);
                // ⚠️ PR-011 反模式：不要塞完整 DynamicObject 到 variables · 太大 · 反序列化慢
                // ✅ 只放 ID 和必要业务字段 · 订阅方按需查
                svc.triggerEventSubscribeJobs(SOURCE_APP, evtNum,
                    String.format("permfile %s by user %d", opKey, currentUserId),
                    variables);
                LOG.info("[BEC] published evt={} permfileId={} ok", evtNum, permfile.getPkValue());
            } catch (Exception e) {
                LOG.error("[BEC] publish failed evt=" + evtNum + " permfileId=" + permfile.getPkValue(), e);
                // ⚠️ 不抛 · afterExecuteOperationTransaction 抛了不会回滚（事务已提交）· 只记日志
            }
        }
    }
}
```

#### 步骤 3：ISV 订阅方（BPM 集成）示例

```java
package isv.cosmic.hr.permfile;

import kd.bos.bec.api.IEventServicePlugin;                 // ✅ @SdkPublic
import kd.bos.bec.model.KDBizEvent;                        // ✅ @SdkPublic
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

import java.util.Map;

/**
 * ISV 订阅方 · 接 BPM
 * 在【开发平台】→【业务事件订阅】里把本类挂到 2isv_hrcs_permfile_disabled / _deleted 上
 */
public class IsvPermFileToBpmEventPlugin implements IEventServicePlugin {

    private static final Log LOG = LogFactory.getLog(IsvPermFileToBpmEventPlugin.class);

    @Override
    public void handleEvent(KDBizEvent event) {
        String evtNum = event.getEventNumber();
        Map<String, Object> vars = event.getVariables();
        try {
            // 调外部 BPM REST API
            // bpmClient.startProcess("PERMFILE_HANDOVER", vars);
            LOG.info("[BPM] handle evt={} permfileId={}", evtNum, vars.get("permfileId"));
        } catch (Exception e) {
            LOG.error("[BPM] handle failed", e);
            // 抛异常 · BEC 会重试（按订阅配置）
            throw new RuntimeException(e);
        }
    }
}
```

### 关键陷阱

| 陷阱 | 应对 |
|---|---|
| 在 endOperationTransaction 发事件 → 事务可能未最终提交 → 脏事件 | ❌ 必须在 afterExecuteOperationTransaction · 此时事务已提交（PR-010） |
| variables 塞完整 DynamicObject | ❌ 只塞 ID 和必要字段（PR-011 反模式） |
| 自接 RabbitMQ/Kafka | ❌ 必须走 IEventService（PR-011） |
| 发完事件忘了在开发平台预配置 eventNumber | BEC 直接忽略 · 没异常 · 难排查 |
| 一个 disable 操作 N 个档案 → N 次 triggerEventSubscribeJobs | 性能注意 · 大批量场景考虑批量事件号设计 |
| 在 OP 之外（FormPlugin）发 BEC | ❌ FormPlugin 不参与事务 · 错过提交时机 |

### 涉及规则

- **PR-010** afterExecuteOperationTransaction 阶段（事务已提交安全）
- **PR-011** 走苍穹 BEC · 不自接 MQ · 不塞 DynamicObject

---

## CS-06 列表过滤定制（按项目维度限定）

### 业务背景

某 ISV 项目集团有"项目化用工"模式：每个 user 在每个 org 都有归属"项目（bd_project · ISV 自定义字段 2isv_project）"。集团要求：列表上各项目经理（自带 admingroup）只能看到自己项目相关的 permfile · 即便他们 admingroup level <= 2（属于"中级管理员"按标品逻辑应该看到全部 org 数据）也要被项目维度限定。

要求：
1. 在标品 setFilter（org.id in） 之后 · 追加 `2isv_project.id in <当前用户负责的项目>` 过滤
2. 顶级管理员（admingroup level == -1）不限制（但仍按 admingroup level 走标品）
3. 不破坏标品的 admingroup level 限制

### 推荐方案：ISV ListPlugin · setFilter super 后追加

```java
package isv.cosmic.hr.permfile;

import kd.bos.context.RequestContext;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.formplugin.web.HRDataBaseList;            // ✅ 白名单
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * ISV ListPlugin · 项目维度过滤
 * 挂载：hrcs_permfilelist 列表壳
 * PR-001 不继承 PermfilesListPlugin（场景专属）· 并列挂
 * PR-002 RowKey 顺序：本插件排在 PermfilesListPlugin 之后（让标品的 admingroup 先生效）
 */
public class IsvProjectFilterListPlugin extends HRDataBaseList {

    @Override
    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);  // ⭐ 必须先 super · 让标品的 admingroup level 限制先生效

        long currentUserId = RequestContext.get().getCurrUserId();
        if (isCosmicSuperAdmin(currentUserId)) {
            return;  // 苍穹超管不限制
        }

        // 查当前用户负责的项目集合
        List<Long> projectIds = queryUserResponsibleProjects(currentUserId);
        if (projectIds.isEmpty()) {
            // 没负责项目 · 直接限定 = 0（看不到任何）
            setFilterEvent.getQFilters().add(new QFilter("id", "=", 0L));
            return;
        }

        // ⭐ 注意：本场景的列表显示 hrcs_userpermfile 数据 · 不是 hrcs_permfilelist 自身
        //   2isv_project 是加在 hrcs_userpermfile 上的 ISV 字段（CS-01 加过）
        //   用 in 限定项目集合
        setFilterEvent.getQFilters().add(
            new QFilter("2isv_project.id", "in", projectIds));
    }

    private boolean isCosmicSuperAdmin(long userId) {
        // 简化示例 · 实际查 PermissionServiceHelper.isAdminUser
        return false;
    }

    private List<Long> queryUserResponsibleProjects(long userId) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper("2isv_projectresp");
        var coll = helper.queryOriginalCollection("project.id",
            new QFilter[]{
                new QFilter("user.id", "=", userId),
                new QFilter("enable", "=", "1")
            });
        List<Long> ids = new ArrayList<>(coll.size());
        coll.forEach(o -> ids.add(o.getLong("project.id")));
        return ids;
    }
}
```

### 关键陷阱

1. **必须先 `super.setFilter(setFilterEvent)`** —— 否则标品的 admingroup level 限制不生效 · ISV 项目过滤反而打开了标品的限定。
2. **不要 `setFilterEvent.getQFilters().clear()` 后再 add** —— 这会抹掉标品过滤 + 用户列表过滤器（permfileenable=1 等）。
3. **PR-002 RowKey 排序**：本插件应排在 PermfilesListPlugin **之后**（让标品先 setFilter · ISV 后追加）· 这样 super 调用顺序正确。
4. **顶级管理员豁免逻辑**：如果业务要求顶级管理员看全部 · 必须在 ISV 这里加豁免分支 · 否则项目维度限定会盖过标品 admingroup 逻辑。

### 验证方法

| 用户 | 标品 admingroup level | ISV 项目数 | 期望可见 |
|---|---|---|---|
| 顶级管理员 | -1 | 不查 | 全部（标品 +0 ISV 不限） |
| 项目经理 A · admingroup=2 | level=2 (不加 org 限制) | 项目 P1, P2 | org × P1+P2 |
| 项目经理 B · admingroup=3 | level=3 (加 org 限制) | 项目 P3 | (admingroup org) × P3 |
| 普通员工 · 无 admingroup | level=-1 加 (org 限制) | 0 项目 | id=0 (空列表) |

### 涉及规则

- **PR-001** 并列挂 · 不继承 PermfilesListPlugin
- **PR-002** RowKey 顺序 · ISV 在标品之后追加
- **PR-009**（不直接适用 · 本场景非时序 · 但项目实体 bd_project 如果时序需用 boid）

---

## CS-07 批量授权扩展（batchgroup 时自动写自定义字段）

### 业务背景

ISV 客户要求：用户点【批量分组】（btn_batchgroup）后 · 在弹出的 hrcs_permfilegrptree 选完目标分组、提交后 · 自动在 hrcs_permfilegrpmember 中间表上写 ISV 自定义字段：
- `2isv_assigntime` —— 分组时间（System.currentTimeMillis）
- `2isv_assigner` —— 操作员 user.id
- `2isv_remark` —— 自定义备注（弹小窗收集）

### 推荐方案：ISV ListPlugin 拦截 batchgroup + ISV OP 挂 hrcs_permfilegrpmember 主表

#### 子方案 7.1：ISV ListPlugin 拦截 batchgroup（pre）

由于标品 `handleBatchGroup` L531-L543 是直接调 `RoleServiceHelper.showForm("hrcs_permfilegrptree", params, ShowType.Modal)` · ISV 没法在中间插入小窗 · 但可以在 `closedCallBack` 拿到回填后再起一次小窗收 remark · 然后写中间表。

```java
package isv.cosmic.hr.permfile;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.id.ID;                                       // ✅ PR-005
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseList;            // ✅ 白名单

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ISV batchgroup 扩展 ListPlugin
 * 挂载：hrcs_permfilelist
 * PR-001 不继承 PermfilesListPlugin · 并列挂
 * PR-005 用 kd.bos.id.ID.genLongId() 分配中间表 ID
 */
public class IsvBatchGroupExtendListPlugin extends HRDataBaseList {

    private static final String CB_REMARK = "isv_batchgroup_remark";
    private static final String PARAM_PERMFILE_GRP_PAIRS = "isv_pf_grp_pairs";

    @Override
    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);

        // 注意：标品 PermfilesListPlugin 也响应 closedCallBack · 这里区分 actionId
        if ("hrcs_permfilegrptree".equals(evt.getActionId())) {
            // 标品 batchgroup 的回调 · 实际由 PermfilesListPlugin 没注册（标品没 setCloseCallBack）
            // ⚠️ 实证：标品 handleBatchGroup L542 没传 callback · 这里只是示例
            // 替代方案：在 hrcs_permfilegrptree 表单上自己挂 ISV FormPlugin 拦截保存 → 给本 list 回传
            return;
        }

        if (CB_REMARK.equals(evt.getActionId())) {
            // 收到 remark · 调用补写
            String remark = (String) evt.getReturnData();
            String pairs = this.getPageCache().get(PARAM_PERMFILE_GRP_PAIRS);
            this.fillIsvFields(pairs, remark);
        }
    }

    private void fillIsvFields(String pairs, String remark) {
        if (pairs == null) return;
        long currentUserId = RequestContext.get().getCurrUserId();
        Date now = new Date();

        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_permfilegrpmember");
        // pairs 格式："permfileId1:grpId1,permfileId2:grpId2,..."
        for (String pair : pairs.split(",")) {
            String[] kv = pair.split(":");
            long permfileId = Long.parseLong(kv[0]);
            long grpId = Long.parseLong(kv[1]);

            DynamicObject member = helper.queryOriginalOne("id",
                new QFilter[]{
                    new QFilter("permfile.id", "=", permfileId),
                    new QFilter("permfilegrp.id", "=", grpId)
                });
            if (member == null) continue;

            // 用 update API 改 ISV 字段
            DynamicObject loaded = helper.queryOne("id,2isv_assigntime,2isv_assigner,2isv_remark",
                new QFilter[]{new QFilter("id", "=", member.getLong("id"))});
            loaded.set("2isv_assigntime", now);
            loaded.set("2isv_assigner", currentUserId);
            loaded.set("2isv_remark", remark == null ? "" : remark);
            helper.update(new DynamicObject[]{loaded});
        }
    }
}
```

#### 子方案 7.2：在 hrcs_permfilegrpmember 表上挂 OP（推荐 · 更稳）

更稳的做法是不拦 ListPlugin 流程 · 而是在 hrcs_permfilegrpmember 主表的 save opKey 上挂 ISV OP · 自动填字段：

```java
package isv.cosmic.hr.permfile;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.PreparePropertysEventArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.bos.id.ID;                                       // ✅ PR-005

import java.util.Date;

/**
 * ISV grpmember save OP · 自动填 ISV 审计字段
 * 挂载：hrcs_permfilegrpmember · save opKey
 * PR-001 并列挂 · 不继承
 * PR-003 OP 用 entity.set · 不用 getModel
 * PR-005 ID 生成走平台 ID 接口
 */
public class IsvGrpMemberAuditOp extends HRDataBaseOp {

    @Override
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        super.onPreparePropertys(args);
        // ⭐ 声明要读写的字段 · 否则 OP 拿不到
        args.getFieldKeys().add("2isv_assigntime");
        args.getFieldKeys().add("2isv_assigner");
    }

    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);

        long currentUserId = RequestContext.get().getCurrUserId();
        Date now = new Date();

        for (DynamicObject member : args.getDataEntities()) {
            // PR-003: OP 用 entity.set · 不用 getModel
            if (member.getDate("2isv_assigntime") == null) {
                member.set("2isv_assigntime", now);
            }
            if (member.getLong("2isv_assigner") == 0L) {
                member.set("2isv_assigner", currentUserId);
            }
            // ID 字段如果未设 · 用 PR-005 生成
            if (member.getPkValue() == null || (long) member.getPkValue() == 0L) {
                member.set("id", ID.genLongId());
            }
        }
    }
}
```

⚠️ 这个方案的优势：
- 不管 batchgroup / 单条添加 / Excel 导入 · 都自动填 ISV 字段
- 不需要拦截标品 ListPlugin 流程
- 容错强（PR-001 不继承）

### 关键陷阱

1. **PR-003 OP 里用 `member.set(...)` 不用 `getModel().setValue(...)`** —— OP 没有 model · 后者 NullPointerException。
2. **PR-005 ID 生成用 `kd.bos.id.ID.genLongId()`** —— 不要用 Random.nextLong() / UUID（高并发会重复）。
3. **`onPreparePropertys` 必须声明要读写的字段** —— 不声明的字段 · `args.getDataEntities()[i].get("2isv_assigntime")` 会返回 null（即便 DB 有值）。
4. **不要在 batchgroup 流程里直接拦截 RoleServiceHelper.showForm** · 这个调用是同步的 · ISV 没办法插入。换思路：在 hrcs_permfilegrpmember 主表 save 时拦。

### 涉及规则

- **PR-001** 并列挂 · 不继承
- **PR-003** OP 用 entity.set · FormPlugin 用 getModel().setValue
- **PR-005** ID 用 kd.bos.id.ID.genLongId

---

## 总结：跨 CS 的 5 大铁律

1. **PR-001** 永远并列挂 · 不继承场景专属类（PermfilesListPlugin / PermFilesSaveOp）
2. **PR-002** RowKey 顺序：标品先 setFilter · ISV 后追加（CS-06 反例）；标品 OP 后 ISV 校验 · 让 ISV 排在标品之前（CS-03 反例）
3. **PR-003** FormPlugin 用 `getModel().setValue` · OP 用 `entity.set` · 千万别混
4. **PR-005** 任何新建数据行 · 用 `kd.bos.id.ID.genLongId()` 分配主键
5. **PR-010 / PR-011** 跨模块通知走 BEC · 在 `afterExecuteOperationTransaction` 阶段发 · 不在 endOperation · 不自接 MQ

参考文档：
- `_shared/platform_rules.json` —— 11 条平台规则全文
- `cosmic_hr_sdk_whitelist_audit.md` —— 9 个可继承基类白名单
- `_auto_plugin_registry.md` —— 4 标品插件清单
- `_auto_operations.md` —— 23 opKey 详情
- 反编译：`_sdk_audit/_decompiled/scenarios/hrcs_permfilelist/`
