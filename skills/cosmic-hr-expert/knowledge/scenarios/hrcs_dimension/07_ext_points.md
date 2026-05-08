# 扩展点全图 · 维度管理 (hrcs_dimension)

> **状态**: 🟢 基于 _auto_plugin_registry.md 20 plugin 实抓 + 反编译 4 类
> **confidence**: verified
> **数据源**: OpenAPI `queryEditablePlugins` + CFR 反编译 (2026-04-28)

---

## 一、ISV 可扩展点全景图（按生命周期分组）

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    UI 层 (FormPlugin · 12 个)                            │
├─────────────────────────────────────────────────────────────────────────┤
│  preOpenForm (3 个)                                                      │
│    ├─ HRBaseDataTplEdit (HRDataBaseEdit · 标品)                          │
│    ├─ HRAdminStrictPlugin (HRDynamicFormBasePlugin · 准入闸 · 不要继承)  │
│    ├─ HRBaseDataTplList                                                  │
│    └─ ★ ISV 扩展点 1: 添加 HR 准入二级闸（如限定到部门级管理员）         │
│                                                                          │
│  beforeBindData (2 个)                                                   │
│    ├─ HRBaseDataTplList                                                  │
│    ├─ HRBasedataLogList                                                  │
│    └─ ★ ISV 扩展点 2: 列表初始化前预填 ISV 自建过滤参数                  │
│                                                                          │
│  afterBindData (3 个)                                                    │
│    ├─ HRBaseDataTplEdit                                                  │
│    ├─ HRHiesButtonSwitchPlugin (afterBindData 切换 HIES 导入导出按钮)    │
│    ├─ DimensionNewEdit (afterBindData L104-L143 ctrlentry 反查灌库)      │
│    └─ ★ ISV 扩展点 3: 表单完成后追加 UI 控件可见性 / 默认值              │
│                                                                          │
│  registerListener (1 个)                                                 │
│    └─ DimensionNewEdit (registerListener L98-L102 注册 entitytype F7)    │
│       ★ ISV 扩展点 4: 注册自定义字段的 F7 监听 / 控件 listener           │
│                                                                          │
│  beforeDoOperation (3 个)                                                │
│    ├─ HRBaseDataTplEdit                                                  │
│    ├─ DimensionNewEdit (beforeDoOperation L272-L296 save 校验+联动)      │
│    ├─ HRBasedataLogList                                                  │
│    └─ ★ ISV 扩展点 5: 操作前业务联动（如 disable 前补刀写日志）          │
│                                                                          │
│  afterDoOperation (1 个)                                                 │
│    └─ HRBaseDataTplEdit                                                  │
│       ★ ISV 扩展点 6: 操作后补刀（如审核后通知 + 更新 ISV 自建表）       │
│                                                                          │
│  beforeItemClick (2 个)                                                  │
│    ├─ DimensionNewEdit (beforeItemClick L259-L270 refrole 跳转子页面)    │
│    ├─ DimensionList (beforeItemClick L29-L34 tbldisable 二次确认)        │
│    └─ ★ ISV 扩展点 7: 工具栏自定义按钮 / 二次确认                        │
│                                                                          │
│  propertyChanged (1 个)                                                  │
│    └─ DimensionNewEdit (propertyChanged L217-L234 datasource/entitytype) │
│       ★ ISV 扩展点 8: 监听字段变更联动（CS-02 套路）                     │
│                                                                          │
│  beforeClosed (1 个)                                                     │
│    └─ HRBaseDataTplEdit                                                  │
│       ★ ISV 扩展点 9: 关闭前确认 / 清理 PageCache                        │
│                                                                          │
│  confirmCallBack (隐式 · DimensionNewEdit/DimensionList 用)              │
│    ├─ DimensionNewEdit (confirmCallBack L298-L306 save_continue 路由)    │
│    ├─ DimensionList (confirmCallBack L37-L43 disable_conform 路由)       │
│    └─ ⚠ ISV 自建 callBackId 不要用 "save_continue" 或 "disable_conform"  │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                    数据层 (OperationServicePlugIn · 8 个)                 │
├─────────────────────────────────────────────────────────────────────────┤
│  onAddValidators (2 个)                                                  │
│    ├─ HRBaseDataStatusOp (单据状态校验)                                  │
│    ├─ DimensionDeleteOp (注册 DimensionDeleteValidator)                  │
│    └─ ★ ISV 扩展点 10: 加业务校验（save 同名 / disable 引用阻断 / delete 反查 4 表 · CS-03/04） │
│                                                                          │
│  beforeExecuteOperationTransaction (4 个)                                │
│    ├─ HRBaseDataStatusOp                                                 │
│    ├─ HRBaseDataLogOp                                                    │
│    ├─ HRBaseDataEnableOp                                                 │
│    ├─ HRBaseOriginalOp                                                   │
│    └─ ★ ISV 扩展点 11: 进事务前业务逻辑 · 抛 KDBizException 阻断         │
│                                                                          │
│  endOperationTransaction (0 个 · 标品没用)                               │
│    └─ ★ ISV 扩展点 12: 同事务收尾（注意：dimension 标品无场景专属 OP 用此）│
│                                                                          │
│  afterExecuteOperationTransaction (1 个)                                 │
│    ├─ HRBaseDataLogOp                                                    │
│    └─ ★ ISV 扩展点 13: 事务已提交 · 发 BEC 事件（CS-05）/ 写自建表（CS-06）│
│                                                                          │
│  onPreparePropertys (隐式 · 各 OP 自动调用)                              │
│    └─ ★ ISV 扩展点 14: 声明 ISV 字段 / 子表 · 让后续 OP 读到（PR-010 第 2 阶段）│
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 二、按 opKey 分组 · 扩展点决策树

### 2.1 save / submit 系列

```
save / submit:
   FormPlugin beforeDoOperation:
      DimensionNewEdit (L272-L296: modifytime/datasource→entitytype/checkEntitytype/checkEnumChange)
      ★ ISV 加 FormPlugin · propertyChanged 联动 ISV 字段（CS-02）
   OP onPreparePropertys:
      ★ ISV onPreparePropertys 声明读 ISV 字段
   OP onAddValidators:
      HRBaseDataStatusOp · CodeRuleOp.numberValidator
      ★ ISV 自建 Validator · 校验业务规则（CS-03 同名 / 跨字段）
   OP beforeExecuteOperationTransaction:
      HRBaseDataStatusOp / HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp
      ★ ISV 加 ISV 业务前置（极少用）
   OP afterExecuteOperationTransaction:
      HRBaseDataLogOp
      ★ ISV 写自建表 / 发 BEC（CS-05/06）
```

### 2.2 audit / unaudit 系列

```
audit:
   FormPlugin: 无 FormPlugin 自定义拦截
   OP onAddValidators: 无 · 标品 OP 链
   OP beforeExecuteOperationTransaction: HRBaseDataLogOp
   OP afterExecuteOperationTransaction: HRBaseDataLogOp
   ★ ISV 加 audit 后置 · 发 BEC（CS-05）/ 同步 BI（CS-06 · audit afterExecute）

unaudit:
   ★ ISV 加 unaudit 后置 · 通知反向流程（同 audit 模式）
```

### 2.3 disable / enable 系列

```
disable:
   FormPlugin / List preOpenForm: HRAdminStrictPlugin 准入闸
   List beforeItemClick: DimensionList (tbldisable 二次确认)
   FormPlugin beforeDoOperation: 无（dimension 表单上 disable 按钮在 List 而不在 Form 上）
   OP onAddValidators: 无
   OP afterExecuteOperationTransaction: HRBaseDataLogOp
   ★ ISV 加 disable 前置阻断（CS-03 disable 方向 · 反查 dynascheme.condition 引用）
   ★ ISV 加 disable 后置同步（清理下游 hrcs_userrolerelat 等）

enable:
   ★ ISV 加 enable 前置（如检查 dimension 当前状态合法）
   ★ ISV 加 enable 后置（重新激活下游）
```

### 2.4 delete 系列

```
delete:
   List beforeItemClick: 无（dimension delete 没二次确认 · 与 disable 不同）
   OP onAddValidators:
      DimensionDeleteOp 注册 DimensionDeleteValidator (反查标品下游 · 反编译没读到具体)
      ★ ISV 加 TdkwDimensionDeleteCheckValidator 反查 4 张下游表（CS-04）
   OP beforeExecuteOperationTransaction: CodeRuleDeleteOp / HRBaseDataStatusOp / HRBaseDataLogOp
   OP afterExecuteOperationTransaction: 无（与 save/audit 不同）
   ★ ISV 加 delete 后置时机有限 · 只能挂 afterExecuteOperationTransaction
```

---

## 三、扩展点 vs PR 规范矩阵

| 扩展点 | 推荐 PR | 反例（违反 PR） |
|---|---|---|
| 自建 FormPlugin（CS-02/03） | PR-001（继承 HRDataBaseEdit）+ PR-003（getModel().setValue）+ PR-004（beginInit/endInit） | extends DimensionNewEdit |
| 自建 ListPlugin（CS-07） | PR-001（继承 HRDataBaseList）+ PR-002（顺序在标品后） | extends DimensionList |
| 自建 OP（CS-03/04/05/06） | PR-001（继承 HRDataBaseOp）+ PR-010（13 阶段标准） | extends DimensionDeleteOp |
| 自建 Validator（CS-03/04） | PR-001（继承 AbstractValidator）+ PR-010（onAddValidators 注册） | extends DimensionDeleteValidator |
| 加自定义字段（CS-01） | PR-007（不动 issyspreset）+ ISV 前缀 | 不带前缀 fieldName 手填 fk_ |
| 反查 HisModel 下游（CS-03/04） | PR-008（带 iscurrentversion=true）+ PR-009（用 boid） | 不带 iscurrentversion · 用 id |
| 跨系统通知（CS-05） | PR-011（IEventService）+ PR-010（afterExecute 阶段发） | 自建 Kafka · endOperationTransaction 阶段发 |
| 自建表行 id（CS-06） | PR-005（kd.bos.id.ID） | UUID / maxId+1 / currentTimeMillis |
| 自建表 buildMeta | parentId 显式传（buildmeta_parent_template_trap.md） | 不传 parentId |

---

## 四、扩展点优先级

### 第 1 优先级（最常用 · 大部分 ISV 实施都涉及）

1. **加自定义字段**（CS-01 · modifyMeta + ISV 前缀 + 平台部署）
2. **save 前置校验**（CS-03 save 方向 · 同名 / 字段值约束 · 自建 Validator + onAddValidators）
3. **propertyChanged 联动**（CS-02 · datasource → 自动带 entry 模板）
4. **列表 setFilter 权限过滤**（CS-07 · 区域 HR 权限切分）

### 第 2 优先级（有客户需要时）

5. **delete 前置校验**（CS-04 · 反查 4 张下游表 + 业务可观察性提示）
6. **disable 前置阻断**（CS-03 disable 方向 · 反查 dynascheme 引用）
7. **audit / disable 后置 BEC 通知**（CS-05 · ISV 自建发布方）
8. **audit 后置同步 + 自建审计日志**（CS-06 · BI 物化表 + ${ISV_FLAG}_dimension_audlog）

### 第 3 优先级（不推荐 / 风险高）

9. ❌ 改 datasource 4 路联动（DimensionNewEdit.showEnumCtrl）· 改了和 ctrlentry/entityctrl 整套机制冲突
10. ❌ 替换 EntityCtrlServiceHelper.getRoles 反查（标品 helper · 改动面大）
11. ❌ 改 DimensionDeleteValidator 内置反查（继承覆盖 · 违反 PR-001）
12. ❌ 在 dimension 主表上加 HisModel 时序字段（boid/iscurrentversion · 改了等于改父模板）

---

## 五、扩展点详细 SDK 调用速查

### FormPlugin 阶段（DimensionNewEdit 实证）

```java
// PR-001 · 不继承 DimensionNewEdit · 继承 HRDataBaseEdit
public class IsvPlugin extends kd.hr.hbp.formplugin.web.HRDataBaseEdit {

    // 监听字段变更（CS-02 · datasource 联动）
    public void propertyChanged(PropertyChangedArgs evt) {
        String key = evt.getProperty().getName();
        // PR-004 · beginInit/endInit
        getModel().beginInit();
        getModel().setValue("xxx", "yyy");      // PR-003 · 用 getModel().setValue
        getModel().endInit();
        getView().updateView("xxx");
    }

    // 表单初始化后（CS-01 默认值预填）
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        // ⚠ DimensionNewEdit.afterBindData 用了 setDataChanged(false) · ISV 复用同套路
    }

    // 操作前阻断（CS-02/03 联动）
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        FormOperate src = (FormOperate) args.getSource();
        if ("save".equals(src.getOperateKey())) {
            // ⚠ 不要用 callBackId="save_continue" · 与标品冲突
        }
    }

    // F7 选择前过滤（CS-02 限定候选）
    public void beforeF7Select(BeforeF7SelectEvent evt) {
        ListShowParameter lsp = (ListShowParameter) evt.getFormShowParameter();
        if ("xxx".equals(evt.getProperty().getName())) {
            QFilter f = new QFilter("modeltype", "=", "BaseFormModel");
            lsp.getListFilterParameter().setFilter(f);
        }
    }
}
```

### OP 阶段（DimensionDeleteOp 实证）

```java
// PR-001 · 不继承 DimensionDeleteOp · 继承 HRDataBaseOp
public class IsvOp extends kd.hr.hbp.opplugin.web.HRDataBaseOp {

    // PR-010 · 第 2 阶段 · 声明读字段
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        e.getFieldKeys().add("name");
        e.getFieldKeys().add("datasource");
    }

    // PR-010 · 第 4 阶段 · 注册 Validator（CS-03/04 主入口）
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator(new IsvValidator());
    }

    // PR-010 · 第 5 阶段 · 进事务前业务逻辑（少用）
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        // 抛 KDBizException 或 addErrorMessage
    }

    // PR-010 · 第 9 阶段 · 事务已提交 · 发事件 / 写自建表（CS-05/06 主入口）
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        // PR-011 · BEC 发布
        IEventService svc = ServiceHelper.getService(IEventService.class);
        svc.triggerEventSubscribeJobs(...);
    }
}
```

### Validator 阶段（DimensionDeleteValidator 实证 · 但 detail 没读到）

```java
// PR-001 · 不继承 DimensionDeleteValidator · 继承 AbstractValidator
public class IsvValidator extends kd.bos.entity.validate.AbstractValidator {

    @Override
    public void validate() {
        for (ExtendedDataEntity row : this.getDataEntities()) {
            DynamicObject entity = row.getDataEntity();
            // 业务校验
            if (...) {
                this.addErrorMessage(row, "提示文案");
            }
        }
    }
}
```

### List 阶段（DimensionList 实证）

```java
// PR-001 · 不继承 DimensionList · 继承 HRDataBaseList
public class IsvListPlugin extends kd.hr.hbp.formplugin.web.HRDataBaseList {

    @Override
    public void setFilter(SetFilterEvent evt) {
        super.setFilter(evt);
        QFilter qf = new QFilter("xxx", "=", "yyy");
        evt.getQFilters().add(qf);
    }
}
```

---

## 六、关键事实再检（与 dynascheme 对比 · 影响扩展点选择）

| 维度 | hrcs_dimension | hrcs_dynascheme |
|---|---|---|
| FormPlugin 数 | 8 个 | 16 个 |
| OP 数 | 8 个 | 10 个 |
| 反编译 ISV 直接面对的场景类 | 4 类（DimensionNewEdit/List/DeleteOp/HRAdminStrictPlugin） | 7 类（DynaAuthSchemePlugin/ListPlugin/Op/SaveSubmitOp/AuditOp/ConfirmChangeOp/HRAdminStrictPlugin） |
| 场景专属 OP 数 | 1 个（DimensionDeleteOp） | 4 个（Op/SaveSubmitOp/AuditOp/ConfirmChangeOp） |
| 场景专属 OP endOperationTransaction | 0 个（dimension 没有同事务收尾） | 3 个（SaveSubmitOp/AuditOp/ConfirmChangeOp 都有） |
| 场景专属 OP afterExecuteOperationTransaction | 0 个 | 0 个（都是 endOperationTransaction） |
| 推荐 ISV 扩展点 | 自建 OP + onAddValidators / afterExecuteOperationTransaction | 同 dimension · 但要注意场景 OP 的标品逻辑链 |
| HisModel 时序 | ❌ 无 | ✅ 有 |
| ISV 加字段是否要考虑跨版本快照 | ❌ 不需要 | ✅ 需要（每个版本独立保留 ISV 字段值） |

→ dimension 比 dynascheme 的扩展点更"干净" · ISV 加 OP / Validator 不会跟标品场景 OP 抢 endOperationTransaction · 推荐**走 onAddValidators + afterExecuteOperationTransaction 双触点**模式（CS-03/04/05/06 全部如此）。

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## ISV 扩展指引（基于 HRBaseDataTplEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`, `afterLoadData`, `beforeDoOperation`, `afterDoOperation`, `preOpenForm`, `beforeClosed`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void initImportData(kd.bos.entity.datamodel.events.InitImportDataEventArgs)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**QUERY_BUILDER** · HrEntityCommonService L46
```java
  44       public List<String> getParentEntity(String entryEntity) {
  45           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_formmeta");
  46 >         QFilter entityFilter = new QFilter("number", "=", (Object)entryEntity);
  47           DynamicObject dynamicObject = helper.queryOriginalOne("inheritpath", new QFilter[]{entityFilter});
  48           String inheritPath = dynamicObject.getString("inheritpath");
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## ISV 扩展指引（基于 HRBaseDataImportEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void initImportData(kd.bos.entity.datamodel.events.InitImportDataEventArgs)`
- `public public void beforeImportData(kd.bos.entity.datamodel.events.BeforeImportDataEventArgs)`
- `public public void afterImportData(kd.bos.entity.datamodel.events.ImportDataEventArgs)`
- `public public void queryImportBasedata(kd.bos.entity.datamodel.events.QueryImportBasedataEventArgs)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · LogHandlerUtil L158
```java
 156                   DynamicObject logDy = new DynamicObject((DynamicObjectType)dataEntityType);
 157                   logDy.set("id", (Object)ids[index]);
 158 >                 logDy.set("username", (Object)RequestContext.get().getUserName());
 159                   logDy.set("opname", (Object)entityModifyInfo.getOperationKey());
 160                   logDy.set("opdate", (Object)now);
```

**QUERY_BUILDER** · LogHandlerUtil L346
```java
 344                   attachmentIds.add(refBaseObj.getLong("id"));
 345               }
 346 >             DynamicObject[] attachments = BusinessDataServiceHelper.load((String)"bd_attachment", (String)"id,name,url,createtime", (QFilter[])new QFilter[]{new QFilter("id", "in", (Object)attachmentIds)});
 347               Arrays.stream(attachments).forEach(attachment -> attachmentLogInfoList.add(new AttachmentLogInfo("2", (Object)attachment.getLong("id"), Long.valueOf(0L), attachment.getString(displayProp), LogHandlerUtil.getAttachmentFullUrl(URLEncoder.encode(attachment.getString("url"))), attachment.getDate("createtime"), displayProp)));
 348           }
```

**READ_VIA_HELPER** · LogHandlerUtil L208
```java
 206           if (oldDys == null || oldDys.length == 0) {
 207               List pks = Stream.of(newDys).map(DataEntityBase::getPkValue).distinct().collect(Collectors.toList());
 208 >             objectDynamicObjectMap = Arrays.stream(BusinessDataServiceHelper.load((Object[])pks.toArray(), (DynamicObjectType)dynamicObjectType)).collect(Collectors.toMap(DataEntityBase::getPkValue, dy -> dy));
 209           } else {
 210               objectDynamicObjectMap = Arrays.stream(oldDys).collect(Collectors.toMap(dy -> dy.get("id"), Function.identity(), (x1, x2) -> x2));
```

**THROW_BIZ_EXCEPTION** · HisModelCommonService L124
```java
 122                   LOGGER.error((Throwable)exception);
 123               }
 124 >             throw new KDBizException(String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u7684\u5386\u53f2\u6a21\u578b\u5b9e\u4f53\u914d\u7f6e\u201c\u6a21\u5f0f\u9009\u62e9\u201d\u672a\u914d\u7f6e\uff0c\u8bf7\u5148\u5b8c\u6210\u914d\u7f6e\u3002", (String)"HisModelCommonService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]), entityNumber));
 125           }
 126           return hisModelEntityConfig;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## ISV 扩展指引（基于 HRHiesButtonSwitchPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.form.plugin.AbstractFormPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRHiesButtonSwitchPlugin L92
```java
  90               if (enableNoPermBtnHide) {
  91                   String appId = HRPermUtil.getAppIdFromShowParam((FormShowParameter)view.getFormShowParameter());
  92 >                 long currUserId = RequestContext.get().getCurrUserId();
  93                   boolean isPerm = PermissionServiceHelper.checkPermission((Long)currUserId, (String)appId, (String)billFormId, (String)permItem);
  94                   LOGGER.info("currUserId:{} appId:{} billFormId:{} permItem:{}", new Object[]{currUserId, appId, billFormId, permItem});
```

**QUERY_BUILDER** · HRQFilterHelper L17
```java
  15   public class HRQFilterHelper {
  16       public static QFilter buildEql(String filed, Object val) {
  17 >         return new QFilter(filed, "=", val);
  18       }
  19   
```

**CALL_CROSS_SERVICE** · HRPermUtil L65
```java
  63   
  64       public static Map<String, Object> queryPermConfig(String formId) {
  65 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hbss", (String)"IHBSSPermService", (String)"queryPermConfig", (Object[])new Object[]{formId});
  66       }
  67   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

## ISV 扩展指引（基于 HRAdminStrictPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public static public static void showMesIfUserIsNotAdmin(kd.bos.form.events.PreOpenFormEventArgs)`
- `public static public static boolean isHrAdmin()`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRAdminService L23
```java
  21   public class HRAdminService {
  22       public static boolean isHrAdmin() {
  23 >         Long userId = RequestContext.get().getCurrUserId();
  24           QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25           DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
```

**QUERY_BUILDER** · HRAdminService L24
```java
  22       public static boolean isHrAdmin() {
  23           Long userId = RequestContext.get().getCurrUserId();
  24 >         QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25           DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
  26           Long adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
```

**READ_VIA_HELPER** · HRAdminService L25
```java
  23           Long userId = RequestContext.get().getCurrUserId();
  24           QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25 >         DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
  26           Long adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
  27           filters = new QFilter[]{new QFilter("user.id", "=", (Object)userId).and("usergroup.adminscheme.id", "=", (Object)adminScheme).and("usergroup.isdomain", "=", (Object)"1").and("usergroup.domain", "=", (Object)1386267129346523136L).or("usergroup.id", "=", (Object)1393280986623636480L).and("user.id", "=", (Object)userId)};
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.DimensionNewEdit -->

## ISV 扩展指引（基于 DimensionNewEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.DimensionNewEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.DimensionNewEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `afterBindData`, `propertyChanged`, `beforeItemClick`, `beforeDoOperation`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · DimensionNewEdit L112
```java
 110           OperationStatus status = this.getView().getFormShowParameter().getStatus();
 111           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_entityctrl");
 112 >         QFilter filter = new QFilter("entryentity.dimension", "=", (Object)dimensionId);
 113           DynamicObject[] dys = serviceHelper.query("entitytype,entryentity.dimension,propkey,authrange,ismust,issyspreset,desc", new QFilter[]{filter});
 114           if (OperationStatus.EDIT.equals((Object)status) && dys.length > 0) {
```

**READ_VIA_HELPER** · DimensionNewEdit L123
```java
 121                   List filterRes = entryEntity.stream().filter(entry -> entry.getLong("dimension.id") == dimensionId).collect(Collectors.toList());
 122                   String entityNum = dynamicObject.getString("entitytype.number");
 123 >                 if (null == entityNum || (entityFieldMap = EntityCtrlServiceHelper.getEntityFieldMap((DynamicObject)dynamicObject.getDynamicObject("entitytype"))).isEmpty()) continue;
 124                   for (DynamicObject dy : filterRes) {
 125                       this.getModel().createNewEntryRow(KEY_CTRL_ENTRY);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.DimensionNewEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.DimensionNewEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.DimensionNewEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## ISV 扩展指引（基于 HRBaseDataTplList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `filterContainerInit`, `preOpenForm`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void listColumnCompareTypesSet(kd.bos.form.events.ListColumnCompareTypesSetEvent)`
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## ISV 扩展指引（基于 HRBasedataLogList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**QUERY_BUILDER** · HRBasedataLogList L90
```java
  88           lsp.setBillFormId("hbss_history_logview");
  89           ListFilterParameter listFilterParameter = new ListFilterParameter();
  90 >         QFilter bizobj = new QFilter("bizobj", "=", (Object)billFormId);
  91           if (primaryKeyValue != 0L) {
  92               bizobj.and(new QFilter("modifybillid", "like", (Object)(String.valueOf(primaryKeyValue) + "%")));
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.DimensionList -->

## ISV 扩展指引（基于 DimensionList 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.DimensionList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.DimensionList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeItemClick`

### 可重写方法（target.java self）
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.DimensionList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.DimensionList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.DimensionList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## ISV 扩展指引（基于 HRBaseDataStatusOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## ISV 扩展指引（基于 HRBaseDataLogOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeExecuteOperationTransaction`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## ISV 扩展指引（基于 HRBaseDataEnableOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## ISV 扩展指引（基于 HRBaseOriginalOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.DimensionDeleteOp -->

## ISV 扩展指引（基于 DimensionDeleteOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.DimensionDeleteOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.DimensionDeleteOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.DimensionDeleteOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.DimensionDeleteOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.DimensionDeleteOp -->
