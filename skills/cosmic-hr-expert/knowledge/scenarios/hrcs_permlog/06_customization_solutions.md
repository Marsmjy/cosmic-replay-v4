# 06 定制方案 · hrcs_permlog（HR 权限日志）

> permlog 是只读日志视图 · ISV 定制重点在**过滤/字段隐藏/跨场景日志归集** · 不是编辑扩展（这是它跟其他 hrcs 编辑场景的最大差异）
> 7 个 CS 全部带 PR 引用 · 全部基于反编译实证 · 全部 ≥ 5 KB
> 数据源：PermLogListPlugin.java + HRAdminStrictPlugin.java + curated_sdk.json + platform_rules.json

---

## CS-01 · 自定义日志列扩展（List 列添加扩展）

### 需求场景
客户希望在 hrcs_permlog 列表里增加一列"业务来源系统" · 区分日志是从哪个业务模块（招聘/薪酬/绩效）的扩展操作触发的。这个列对应一个新字段 · 不在标品 114 字段内。

### 平台规则引用
- **PR-001 ISV 扩展走并列挂插件 · 不走继承覆盖**：列扩展插件必须独立挂 · 不继承 PermLogListPlugin（final 类）
- **PR-007 字段可修改性规范**：ISV 加字段 → 创建 ISV 扩展元数据 · 不能直接 modifyMeta 主表

### 实施方案

#### 步骤 1 · 创建 ISV 扩展元数据

```
formId（标品） : hrcs_permlog
formId（ISV）  : isv_xxx_permlog （继承自 hrcs_permlog · ISV 拥有）
```

通过苍穹开发平台或 OpenAPI buildMeta：
- parentId = hrcs_permlog 的实体 id
- name = "ISV 权限日志扩展"
- entityType = ISV 自有

#### 步骤 2 · 在 ISV 扩展元数据上 modifyMeta add field

字段：`bizsourcesystem`（TextField · 30 字符）· 表达"业务来源系统"

> **铁律提醒**：modifyMeta 参数名是 `fieldType/name/columnName`（不是 dataType/displayName）· 传错静默走 TextField · 见 `cosmic-hr-knowledge-ingest.md` § modifymeta_param_names_and_hr_sdk_limits

#### 步骤 3 · 改 List 元数据 · 加列

在标品 hrcs_permlog 的 List 视图基础上 · 注册 ISV 扩展 List 元数据 · 加 column 引用 bizsourcesystem 字段

#### 步骤 4 · ISV 并列挂 ListPlugin（写入字段值）

permlog 的实际数据写入是**上游编辑场景**做的 · 所以 bizsourcesystem 字段也要在上游写日志的时候同时写。这意味着 ISV 必须并列挂上游场景（hrcs_userrole / hrcs_role 等）的 OP 插件 · 在日志写入路径上塞入新字段值：

```
package isv.xxx.hrcs.permlog.ext;

import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.orm.query.QFilter;
import kd.bos.context.RequestContext;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

public class IsvPermLogSourceFiller extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(IsvPermLogSourceFiller.class);

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        // 主事务已提交·安全更新 permlog
        long currentUid = RequestContext.get().getCurrUserId();
        DynamicObject[] entities = args.getDataEntities();
        if (entities == null || entities.length == 0) return;
        Object firstId = entities[0].getPkValue();

        // 查刚刚被本次操作写入的 permlog（按 operator + 5 秒内创建时间窗）
        // 注意 permlog 不是当前操作直接产物·要兼顾上游场景写日志可能延迟
        String bizSource = computeBizSource(args.getOperationKey());
        if (bizSource == null) return;

        QFilter qf = new QFilter("operator", "=", currentUid);
        qf.and("createtime", ">=", new java.util.Date(System.currentTimeMillis() - 5000L));
        DynamicObject[] permlogs = BusinessDataServiceHelper.load(
            "hrcs_permlog", "id, bizsourcesystem", new QFilter[]{qf});

        for (DynamicObject pl : permlogs) {
            pl.set("bizsourcesystem", bizSource);
        }
        if (permlogs.length > 0) {
            BusinessDataServiceHelper.save(permlogs);
        }
    }

    private String computeBizSource(String opKey) {
        if (opKey == null) return null;
        if (opKey.startsWith("save")) return "RecruitmentExtension";
        if (opKey.startsWith("audit")) return "PayrollExtension";
        return "GenericExtension";
    }
}
```

> 注意：上面是真实 SDK 调用模板（HRDataBaseOp 是白名单 · BusinessDataServiceHelper/QueryServiceHelper 都在 curated_sdk.json）· 但具体业务场景要用 ISV 自己的 OP 类绑定（用 ListShowParameter 或 OperationServiceHelper.executeOperate 拼）

### 验证清单
- [ ] ISV 扩展元数据 isv_xxx_permlog 注册成功（buildMeta 不静默走 hbp_bd_originaltpl 兜底）
- [ ] modifyMeta 加 bizsourcesystem 字段返回 errorCode='0' 成功
- [ ] List 列展示新列 · 数据有值
- [ ] 不影响标品 114 字段

### 风险与回滚
- ⚠ 上游场景多 · ISV 要挂多个 OP 插件 · 每个都要绑 RowKey · 一次发布工作量大
- 回滚：取消 ISV 扩展元数据注册 · 不影响标品

---

## CS-02 · 列表过滤定制（按 ISV 域 / 角色 / 操作类型筛选 + 解除 hashandle 强制过滤）

### 需求场景
客户提："我们 IT 安全审计部门要看**所有日志**（含未处理）· 而且要按业务域过滤（HR 域 / 财务域 / 招聘域）"

### 平台规则引用
- **PR-001 ISV 扩展走并列挂插件**：必须并列挂 ListPlugin · 排在 PermLogListPlugin 之后（顺序由 RowKey 决定）
- **PR-002 RowKey 执行顺序**：ISV 元数据作为"子" · 可以排标品后

### 实施方案

#### 关键发现（实证）

PermLogListPlugin.setFilter 末尾：

```java
ArrayList customQFilterList = Lists.newArrayList(new QFilter[]{new QFilter("hashandle", "=", "1")});
arg.setCustomQFilters(customQFilterList);
```

`setCustomQFilters` **会覆盖**之前的 customQFilters · 所以 ISV 插件必须在标品**之后**挂载 · 才能再次覆盖。

#### 步骤 1 · 注册 ISV 扩展 ListPlugin

绑到 hrcs_permlog 的 List 视图 · RowKey 排序在 PermLogListPlugin 之后

#### 步骤 2 · 实现插件覆盖 setFilter

```
package isv.xxx.hrcs.permlog.list;

import java.util.ArrayList;
import java.util.List;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.orm.query.QFilter;
import kd.bos.form.FormShowParameter;
import kd.bos.context.RequestContext;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

public class IsvPermLogFilterPlugin extends HRDataBaseList {
    private static final Log LOG = LogFactory.getLog(IsvPermLogFilterPlugin.class);

    @Override
    public void setFilter(SetFilterEvent arg) {
        super.setFilter(arg);  // 先让标品 PermLogListPlugin 跑完（4 路 OR + hashandle='1' 强制）

        // 取当前用户角色判定
        long uid = RequestContext.get().getCurrUserId();
        boolean isItSecurityAuditor = isItSecurityRole(uid);
        if (!isItSecurityAuditor) return;  // 非审计员不豁免

        // 安全审计员豁免 hashandle 强制过滤
        // 取 ISV 自定义参数 onlyhandled · 默认 false（看全部日志）
        FormShowParameter showParam = arg.getView().getFormShowParameter();
        Object onlyHandled = showParam.getCustomParam("onlyhandled");
        boolean wantOnlyHandled = (onlyHandled != null && "true".equals(onlyHandled.toString()));

        List<QFilter> custom = arg.getCustomQFilters();
        if (custom == null) custom = new ArrayList<>();

        // 移除标品的 hashandle='1' 强制条件
        custom.removeIf(qf -> "hashandle".equals(qf.getProperty()));

        if (wantOnlyHandled) {
            custom.add(new QFilter("hashandle", "=", "1"));
        }
        // else: 不加 · 看全部

        // ISV 域过滤（按 logtype 分类）
        Object bizDomain = showParam.getCustomParam("bizdomain");
        if (bizDomain != null && HRStringUtils.isNotEmpty(bizDomain.toString())) {
            // logtype 是 BasedataField · 引用 hrcs_permlogtype
            // 按业务域映射 logtype 编码段
            String dom = bizDomain.toString();
            if ("HR".equalsIgnoreCase(dom)) {
                // HR 域 = 1010-1040 + 1050/1060/2010-2095（角色 + 用户权限）
                custom.add(new QFilter("logtype.number", "in",
                    java.util.Arrays.asList("1010","1015","1020","1025","1030","1035","1040",
                                            "1050","1060","2010","2015","2020","2030","2090","2095","2060","2065","2066")));
            } else if ("DataRule".equalsIgnoreCase(dom)) {
                custom.add(new QFilter("logtype.number", "=", "3015"));
            } else if ("BoMapping".equalsIgnoreCase(dom)) {
                custom.add(new QFilter("logtype.number", "in",
                    java.util.Arrays.asList("4010","4015","4020")));
            }
        }

        arg.setCustomQFilters(custom);
        LOG.info("IsvPermLogFilterPlugin · uid={} wantOnlyHandled={} bizDomain={} customSize={}",
                 uid, wantOnlyHandled, bizDomain, custom.size());
    }

    private boolean isItSecurityRole(long uid) {
        // 占位：查 ISV 自建 IT 安全角色
        return PermissionServiceHelper.isAdminUser(uid);  // 简化：先用超管
    }
}
```

#### 步骤 3 · 加自定义参数入口

如果通过菜单 URL/快捷链接打开列表 · 在 URL 上挂 `?onlyhandled=false&bizdomain=HR` · 走 ListShowParameter.customParams 透传

### 验证清单
- [ ] 普通用户开 List · 仍只看到 hashandle=1 的日志（不影响标品）
- [ ] IT 安全审计员开 List · 看到所有日志（含 hashandle=0）
- [ ] customParam bizdomain=HR · 只显示 HR 域 logtype
- [ ] 4 路 OR 用户输入仍生效（不破坏 super.setFilter）

### 风险
- ⚠ super.setFilter 必须先调 · 否则 4 路 OR 拆解逻辑不跑
- ⚠ removeIf hashandle 必须 property 严格匹配 · 不能漏 · 否则双 hashandle 条件冲突
- ⚠ ISV 角色判定要严 · 否则破坏 hashandle 强制过滤等于打开后门
- ⚠ HIES 导出（export_from_list_hr）会跟随 customQFilters · IT 审计员用 ISV 插件后导出范围也变

---

## CS-03 · 字段隐藏（敏感日志字段对非超管隐藏）

### 需求场景
客户："`beforeopdata` / `afteropdata` 字段存的是 JSON 序列化的实体数据 · 包含工资/身份证等敏感信息 · 普通 HR 管理员不应看到 · 只允许系统超管看完整内容 · HR 领域管理员看脱敏值"

### 平台规则引用
- **PR-001 ISV 扩展走并列挂插件**
- **PR-007 字段可修改性规范**：ISV 加字段（不是改原字段定义）

### 实施方案

#### 思路 A · List 列脱敏（推荐 · 改造小）

不在表单元数据层改字段 · 在 List Plugin afterBindData 阶段动态替换字段值（**仅显示脱敏 · 不改库**）

```
package isv.xxx.hrcs.permlog.list;

import java.util.EventObject;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.bos.context.RequestContext;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.bos.list.BillList;
import kd.bos.list.events.BeforeCreateListColumnsArgs;
import kd.bos.entity.list.AbstractColumnDesc;
import kd.bos.list.events.PackageDataEvent;
import kd.bos.list.events.PackageDataListener;
import kd.bos.dataentity.entity.DynamicObject;

public class IsvPermLogMaskingPlugin extends HRDataBaseList implements PackageDataListener {

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        BillList list = (BillList) this.getControl("billlistap");
        if (list != null) {
            list.addPackageDataListener(this);
        }
    }

    @Override
    public void packageData(PackageDataEvent e) {
        // 每行打包前调
        long uid = RequestContext.get().getCurrUserId();
        boolean isSuperAdmin = PermissionServiceHelper.isAdminUser(uid);
        if (isSuperAdmin) return;  // 超管不脱敏

        // 取列描述
        AbstractColumnDesc colDesc = e.getListColumn();
        if (colDesc == null) return;
        String key = colDesc.getKey();
        if (key == null) return;

        // 敏感字段集合（业务确认）
        boolean isSensitive =
            key.equals("beforeopdata") || key.equals("afteropdata") ||
            key.equals("baseinfo_beforedata") || key.equals("baseinfo_afterdata") ||
            key.equals("rolefield_beforedata") || key.equals("rolefield_afterdata") ||
            key.equals("influuser_dataproperty");

        if (isSensitive) {
            Object originalValue = e.getFormatValue();
            if (originalValue != null && originalValue.toString().length() > 0) {
                e.setFormatValue("***[ISV 脱敏 · 仅超管可见]***");
            }
        }
    }
}
```

#### 思路 B · 详情页字段隐藏

permlog 详情页是 4 个独立 formId · 跟主 List 解耦 · 要在每个详情页 formPlugin 里挂 afterBindData：

```
@Override
public void afterBindData(EventObject e) {
    super.afterBindData(e);
    long uid = RequestContext.get().getCurrUserId();
    if (!PermissionServiceHelper.isAdminUser(uid)) {
        getView().setEnable(false, "beforeopdata", "afteropdata", ...);
        getView().setVisible(false, "baseinfo_beforedata", "baseinfo_afterdata", ...);
    }
}
```

> 注意：4 个详情页（hrcs_permlog_role / userperm / datarule / bodimmapping）每个都要挂 · 工作量 ×4

#### 步骤 3 · 字段权限审计日志

ISV 应记录"谁在何时尝试查看敏感字段"形成元日志 · 通过 ISV 的 OP 插件单独写自有审计表 · 不要写回 t_hrcs_permlog（避免循环）

### 验证清单
- [ ] 超管打开列表 · beforeopdata/afteropdata 完整显示
- [ ] HR 领域管理员（非超管）打开 · 显示 ***[ISV 脱敏 · 仅超管可见]***
- [ ] 详情页 4 个子页同样脱敏（如果挂了思路 B）
- [ ] HIES 导出 export_from_list_hr · 检查导出文件里敏感字段是否同样脱敏（可能不行 · 标品导出走 ORM 直接取库 · ISV List Plugin 拦不住）

### 风险
- ⚠ 思路 A 只是 UI 层脱敏 · F12 可看 packageData 之前的原始值（弱保密）· 真正高密保密要走数据脱敏中间件或字段加密存储
- ⚠ HIES 导出绕过 List Plugin · 必须并列挂 OP 导出插件单独脱敏
- ⚠ packageData 是 PackageDataListener 接口 · ISV 必须 implements + addPackageDataListener · 不要靠继承标品

---

## CS-04 · 日志数据导出定制 + 推送外部审计平台（HIES 导出模板扩展 + BEC 桥接）

### 需求场景
"我们公司每天要把权限日志按 HR 域增量推送到 SOC（安全运营中心）的 Splunk 系统 · 要求 JSON 格式 · 字段映射要按 SOC 协议（不是 hrcs_permlog 原始字段名）"

### 平台规则引用
- **PR-010 OP 插件 13 方法完整执行顺序**：在 afterExecuteOperationTransaction（事务已提交）阶段发外部消息
- **PR-011 BEC · 跨模块事件通知走苍穹 BEC · 不自建 MQ**：ISV 自建事件号 + 订阅插件
- **PR-001 ISV 扩展走并列挂插件**

### 实施方案

#### 步骤 1 · 在开发平台 → 业务事件管理 预配置事件号

```
事件编号：isv_xxx_permlog_emit
来源应用：hrcs（或 ISV 自有应用）
事件描述：HR 权限日志增量推送到 SOC
变量字段：
  - logId: long
  - logType: String
  - operatorId: long
  - operationTime: Date
  - bizDomain: String
```

> 关键：eventNumber 必须先在开发平台注册 · 否则 IEventService.triggerEventSubscribeJobs 不识

#### 步骤 2 · 在上游编辑场景的 OP 插件并列挂 · 触发事件

permlog 自己不发 BEC · 因为它是被动接收上游写。所以发布点应该在**上游**（如 hrcs_userrole / hrcs_role 等编辑场景的 OP 插件）。

**重要**：参考 `feedback_bec_3layer_async_publish.md` · 要扫上游场景的 Service + *MsgTask 看是否标品已经派 BEC（避免双发）。

```
package isv.xxx.hrcs.permlog.emit;

import java.util.HashMap;
import java.util.Map;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.servicehelper.ServiceHelper;
import kd.bos.bec.api.IEventService;
import kd.bos.context.RequestContext;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

public class IsvPermLogSocEmitter extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(IsvPermLogSocEmitter.class);

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        // PR-010 安全阶段·主事务已提交
        // PR-011 走 BEC·不自接 Kafka

        DynamicObject[] entities = args.getDataEntities();
        if (entities == null || entities.length == 0) return;

        IEventService svc = ServiceHelper.getService(IEventService.class);
        long uid = RequestContext.get().getCurrUserId();

        for (DynamicObject entity : entities) {
            Map<String, Object> vars = new HashMap<>();
            vars.put("logId", entity.getPkValue());
            // 注意 vars 不要塞完整 DynamicObject · PR-011 反模式
            vars.put("logType", entity.getDynamicObject("logtype") != null
                              ? entity.getDynamicObject("logtype").getString("number") : "");
            vars.put("operatorId", uid);
            vars.put("operationTime", entity.getDate("operationtime"));
            vars.put("bizDomain", computeBizDomain(args.getOperationKey()));

            try {
                svc.triggerEventSubscribeJobs(
                    "hrcs",
                    "isv_xxx_permlog_emit",
                    "ISV PermLog SOC emit",
                    vars
                );
            } catch (Exception ex) {
                LOG.error("BEC emit failed for logId={}", entity.getPkValue(), ex);
                // 不影响主流程·只记日志
            }
        }
    }

    private String computeBizDomain(String opKey) {
        // 按 opKey 映射域
        return "HR";
    }
}
```

#### 步骤 3 · ISV 自建订阅插件 · 推送 SOC

```
package isv.xxx.hrcs.permlog.consumer;

import java.util.Map;
import kd.bos.bec.api.IEventServicePlugin;
import kd.bos.bec.model.KDBizEvent;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

public class IsvPermLogSocConsumer implements IEventServicePlugin {
    private static final Log LOG = LogFactory.getLog(IsvPermLogSocConsumer.class);

    @Override
    public void handleEvent(KDBizEvent event) {
        if (!"isv_xxx_permlog_emit".equals(event.getEventNumber())) return;

        Map<String, Object> vars = event.getVariables();
        Long logId = (Long) vars.get("logId");

        // 反查 permlog 完整数据（按 SOC 协议组装 JSON）
        DynamicObject permlog = BusinessDataServiceHelper.loadSingle(logId, "hrcs_permlog");
        Map<String, Object> socPayload = new java.util.HashMap<>();
        socPayload.put("event_id", logId);
        socPayload.put("event_type", "kingdee_permlog");
        socPayload.put("user_id", vars.get("operatorId"));
        socPayload.put("timestamp", vars.get("operationTime"));
        socPayload.put("payload_summary", permlog.getString("opbtnname"));

        String json = SerializationUtils.toJsonString(socPayload);
        try {
            // 调 ISV 自建 SOC HTTP 网关（不在 Kingdee 内）
            // 实际替换为 RestTemplate / OkHttp 调用
            sendToSoc(json);
            LOG.info("SOC delivery ok · logId={}", logId);
        } catch (Exception ex) {
            LOG.error("SOC delivery failed · logId={}", logId, ex);
            // 落 ISV 重试表 · 后续异步补偿
        }
    }

    private void sendToSoc(String json) throws Exception {
        // ISV 自建 HTTP 调用代码
        // 注意：苍穹环境一般在内网 · 调外部要走代理或 SOC SDK
    }
}
```

#### 步骤 4 · 配置事件订阅关系

在开发平台 → 业务事件订阅 · 把 `isv_xxx_permlog_emit` 事件订阅到 `IsvPermLogSocConsumer`

### 验证清单
- [ ] 上游编辑场景执行 save · BEC 事件成功发出（看 BEC 监控）
- [ ] 订阅插件 handleEvent 命中 · LOG 有"SOC delivery ok"
- [ ] SOC Splunk 端收到 JSON 数据 · 字段映射正确
- [ ] 主事务回滚不影响 BEC（PR-010 验证 · afterExecute 已提交）

### 风险
- ⚠ 必须先扫上游场景反编译看是否标品已经发了 BEC · 避免双发风暴（参考 feedback_bec_3layer_async_publish.md）
- ⚠ vars 体积控制 · 不塞 DynamicObject 全字段（PR-011 反模式）
- ⚠ 订阅 handleEvent 失败要落 ISV 重试表 · 不能直接抛异常（BEC 框架重试机制 ISV 不可控）
- ⚠ SOC 服务降级 · ISV 必须容错 · 不影响主业务

---

## CS-05 · 跨场景日志归集（多源日志合并到统一审计视图）

### 需求场景
"客户希望把 hrcs_permlog 的日志 + ISV 自建模块（如奖金审批/人事调岗 ISV 扩展）的日志汇总到一个**统一审计列表**视图 · 按时间倒序展示 · 支持跨日志类型过滤"

### 平台规则引用
- **PR-001 ISV 扩展走并列挂插件**
- **PR-005 序号/ID 生成用 kd.bos.id.ID 接口**：跨场景统一日志 ID

### 实施方案

#### 思路 · 自建 ISV 日志聚合表 + List · 不动 hrcs_permlog

```
ISV 自建 formId : isv_xxx_unifiedauditlog
字段：
  - logId (long, 主键)
  - sourceFormId (TextField)  -- 来源 formId（hrcs_permlog / isv_xxx_bonusapproval 等）
  - sourceLogId (long)         -- 来源 logId
  - logType (String)           -- 日志类型标识
  - operator (UserField)
  - operationTime (DateTimeField)
  - bizSummary (TextField · 操作摘要)
  - originalDataRef (TextField · 原始数据 JSON 引用)
```

#### 步骤 1 · 注册 ISV 聚合表 + List

#### 步骤 2 · 监听 hrcs_permlog 写入（通过 BEC · 标品没发就 ISV 自建）

参考 CS-04 · 在上游 hrcs_* 编辑场景的 OP 挂 IsvPermLogToUnifiedConsumer

#### 步骤 3 · 监听 ISV 自建模块的写入

ISV 自建模块的 OP 插件直接调聚合表 SaveServiceHelper

```
package isv.xxx.permlog.unify;

import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.SaveServiceHelper;
import kd.bos.id.ID;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

public class IsvUnifiedAuditWriter extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(IsvUnifiedAuditWriter.class);
    private static final String UNIFIED_FORM = "isv_xxx_unifiedauditlog";

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        // PR-010 afterExecute · 主事务已提交
        DynamicObject[] entities = args.getDataEntities();
        if (entities == null) return;

        long uid = RequestContext.get().getCurrUserId();
        String sourceFormId = args.getEntityNumber();
        DynamicObject[] toSave = new DynamicObject[entities.length];
        for (int i = 0; i < entities.length; i++) {
            DynamicObject entity = entities[i];
            DynamicObject auditLog = BusinessDataServiceHelper.newDynamicObject(UNIFIED_FORM);
            auditLog.set("logid", ID.genLongId());  // PR-005
            auditLog.set("sourceformid", sourceFormId);
            auditLog.set("sourcelogid", entity.getPkValue());
            auditLog.set("logtype", computeLogType(args.getOperationKey()));
            auditLog.set("operator_id", uid);
            auditLog.set("operationtime", new java.util.Date());
            auditLog.set("bizsummary", computeSummary(entity, args.getOperationKey()));
            auditLog.set("originaldataref", entity.getPkValue() + "@" + sourceFormId);
            toSave[i] = auditLog;
        }
        SaveServiceHelper.save(toSave);
        LOG.info("Unified audit · saved {} entries from {}", toSave.length, sourceFormId);
    }

    private String computeLogType(String opKey) {
        return "OP_" + (opKey == null ? "UNKNOWN" : opKey.toUpperCase());
    }
    private String computeSummary(DynamicObject entity, String opKey) {
        return opKey + " on " + entity.getPkValue();
    }
}
```

#### 步骤 4 · 实现统一 List 视图

isv_xxx_unifiedauditlog 配 List 视图 + ISV ListPlugin · 实现按 sourceFormId / logType / operator 过滤

#### 步骤 5 · 详情页跳转

聚合 List 上点 sourceLogId 列 · 按 sourceFormId 路由跳到原表单详情（参考 PermLogListPlugin.showDetail 写法）

### 验证清单
- [ ] hrcs_permlog 写入 → BEC → IsvUnifiedAuditWriter 接收 → 聚合表多一条
- [ ] ISV 自建模块写入 → 聚合表多一条
- [ ] 统一 List 按时间倒序展示混合数据
- [ ] sourceLogId 跳详情正确路由

### 风险
- ⚠ 双写风险：hrcs_permlog 已经写一份 + 聚合表又一份 · 容量翻倍 · 用 originaldataref 引用而非完整快照
- ⚠ 排序性能：聚合表大数据量下要在 operationtime 加索引
- ⚠ ISV 边界：聚合表是 ISV 拥有 · 标品 hrcs_permlog 不动

---

## CS-06 · 跨表关联查询定制（4 张表 JOIN 优化）

### 需求场景
"客户审计部门要查'某个用户在某段时间内被授予/撤销了哪些角色权限'· 这要 JOIN t_hrcs_permlog + t_hrcs_permloginfluuser + t_hrcs_permlogrolefunc · 标品列表查不出来这种交叉条件"

### 平台规则引用
- **PR-001 ISV 扩展走并列挂插件**
- **PR-007 字段可修改性规范**：ISV 加查询能力不改原字段

### 实施方案

#### 步骤 1 · 加 ISV 自定义查询服务

```
package isv.xxx.hrcs.permlog.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

public class IsvPermLogCrossQueryService {
    private static final Log LOG = LogFactory.getLog(IsvPermLogCrossQueryService.class);

    /**
     * 查指定用户在时间窗口内被授予/撤销的角色权限项
     * @param targetUserId  被授权的用户 id
     * @param from / to     时间窗口
     * @return  List<Map>  每个 Map 包含 logId/logType/operator/permitemNumber/role
     */
    public List<java.util.Map<String, Object>> queryRolePermChanges(
            long targetUserId, Date from, Date to) {

        // permfile.user.id = targetUserId 的过滤
        // OR influuserentry.influuser_permfile.user.id = targetUserId
        // 时间窗口 operationtime
        QFilter qf1 = new QFilter("permfile.user.id", "=", targetUserId);
        QFilter qf2 = new QFilter("influuserentry.influuser_permfile.user.id", "=", targetUserId);
        QFilter timeQf = new QFilter("operationtime", ">=", from).and("operationtime", "<=", to);
        QFilter mainQf = qf1.or(qf2).and(timeQf);

        // 同时 logtype.number 在角色变更编码集
        QFilter typeQf = new QFilter("logtype.number", "in",
            java.util.Arrays.asList("1010","1015","1020","1025","1030","1035","1040"));
        mainQf = mainQf.and(typeQf);

        DataSet ds = QueryServiceHelper.queryDataSet(
            "isv-permlog-cross-query",
            "hrcs_permlog",
            "id, logtype.number as ltnum, operator, operationtime, " +
            "rolenumber, rolename, " +
            "rolefuncentry.rolefunc_bizapp.number as bizapp, " +
            "rolefuncentry.rolefunc_permitem.number as permitem",
            new QFilter[]{mainQf},
            "operationtime DESC"
        );

        List<java.util.Map<String, Object>> result = new ArrayList<>();
        for (Row row : ds) {
            java.util.Map<String, Object> m = new java.util.HashMap<>();
            m.put("logId", row.getLong("id"));
            m.put("logType", row.getString("ltnum"));
            m.put("operator", row.getLong("operator"));
            m.put("operationTime", row.getDate("operationtime"));
            m.put("roleNumber", row.getString("rolenumber"));
            m.put("roleName", row.getString("rolename"));
            m.put("bizApp", row.getString("bizapp"));
            m.put("permItem", row.getString("permitem"));
            result.add(m);
        }
        ds.close();
        LOG.info("queryRolePermChanges · uid={} returned {} rows", targetUserId, result.size());
        return result;
    }
}
```

> 上面查询用了 `permfile.user.id` / `influuserentry.influuser_permfile.user.id` 同 setFilter L243-L260 的多路 OR 逻辑（实证）

#### 步骤 2 · 注册 ISV 自定义 Report 视图（或 Excel 导出）

把查询结果通过 ISV 自建 Report 表单展示 · 或在 hrcs_permlog 主 List 上挂个 ISV 工具栏按钮 · 调用本服务

#### 步骤 3 · 在 ISV 工具栏插件中调用

```
package isv.xxx.hrcs.permlog.list;

import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;

public class IsvPermLogCrossQueryToolbar extends HRDataBaseList {
    @Override
    public void afterDoOperation(AfterDoOperationEventArgs e) {
        super.afterDoOperation(e);
        if ("isv_crossquery_open".equals(e.getOperateKey())) {
            FormShowParameter para = new FormShowParameter();
            para.setFormId("isv_xxx_permlog_crossquery");
            para.getOpenStyle().setShowType(ShowType.Modal);
            getView().showForm(para);
        }
    }
}
```

### 性能要点
- 时间窗口必须传 · 否则全表扫
- 4 路 OR + JOIN 13 张子表的 SQL 复杂度高 · 用 DataSet 流式读取
- 大数据量分页（按 operationtime 滑动）

### 验证清单
- [ ] queryRolePermChanges 返回结果正确（permfile + influuserentry 两路都覆盖）
- [ ] 时间窗 30 天 · 性能 < 5s
- [ ] 按 logtype 编码集过滤生效
- [ ] DataSet 关闭无内存泄漏

---

## CS-07 · 日志清理 / 归档定制（PR-005 操作主键生成 + 标品归档增强）

### 需求场景
"客户希望按 logtype 分类设置不同的清理周期 · 例如角色变更类（1010-1040）保留 5 年 · 数据规则类（3015）保留 1 年 · 业务对象维度映射类（4010-4020）保留 90 天 · 标品 hrcs_permlogarchive_set 子页只支持单一周期 · 不支持分类"

### 平台规则引用
- **PR-005 序号/ID 生成用 kd.bos.id.ID 接口**：归档批次号生成
- **PR-001 ISV 扩展走并列挂插件**：不继承标品归档插件
- **PR-006 CodeRuleOp 业务侧配置**：归档批次号走编码规则基础资料配置 · 不写代码

### 实施方案

#### 思路 · ISV 自建调度任务 + 自建归档配置表

```
ISV 自建 formId : isv_xxx_permlog_archive_policy
字段：
  - id                 (long, 主键)
  - logtypeNumberSet   (TextField, 逗号分隔 logtype 编码集)
  - retentionDays      (IntegerField, 保留天数)
  - archiveTarget      (ComboField, [delete/coldstorage/external])
  - enable             (CheckBoxField)
```

#### 步骤 1 · 注册调度任务

通过苍穹调度任务（不是 BEC）· 每天凌晨 2 点跑

```
package isv.xxx.hrcs.permlog.archive;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import kd.bos.id.ID;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.orm.query.QFilter;
import kd.bos.schedule.executor.AbstractTask;
import kd.bos.context.RequestContext;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

public class IsvPermLogArchiveTask extends AbstractTask {
    private static final Log LOG = LogFactory.getLog(IsvPermLogArchiveTask.class);

    @Override
    public void execute(RequestContext ctx, Map<String, Object> args) throws Exception {
        long batchId = ID.genLongId();  // PR-005
        LOG.info("IsvPermLogArchiveTask start · batchId={}", batchId);

        // 1. 取所有启用的归档策略
        DynamicObject[] policies = BusinessDataServiceHelper.load(
            "isv_xxx_permlog_archive_policy",
            "logtypenumberset, retentiondays, archivetarget",
            new QFilter[]{new QFilter("enable", "=", true)}
        );

        for (DynamicObject pol : policies) {
            String typeSet = pol.getString("logtypenumberset");
            int days = pol.getInt("retentiondays");
            String target = pol.getString("archivetarget");

            // 计算 cutoff 时间
            Date cutoff = new Date(System.currentTimeMillis() - days * 86400000L);

            // 查匹配的 permlog
            QFilter qf = new QFilter("logtype.number", "in",
                java.util.Arrays.asList(typeSet.split(",")));
            qf.and("createtime", "<", cutoff);

            DynamicObject[] toArchive = BusinessDataServiceHelper.load(
                "hrcs_permlog", "id", new QFilter[]{qf});

            LOG.info("Policy {} · matched {} permlogs", pol.getPkValue(), toArchive.length);

            if ("delete".equals(target)) {
                // 直接物理删除（受 ISV 边界限制 · 删除标品表数据需谨慎 · 通常需要先 backup）
                Object[] ids = new Object[toArchive.length];
                for (int i = 0; i < toArchive.length; i++) {
                    ids[i] = toArchive[i].getPkValue();
                }
                kd.bos.servicehelper.operation.DeleteServiceHelper.delete("hrcs_permlog", ids);
            } else if ("coldstorage".equals(target)) {
                // 转存 ISV 冷存储表
                migrateToColdStorage(toArchive, batchId);
            } else if ("external".equals(target)) {
                // 推到外部对象存储
                migrateToExternal(toArchive, batchId);
            }
        }
        LOG.info("IsvPermLogArchiveTask done · batchId={}", batchId);
    }

    private void migrateToColdStorage(DynamicObject[] entities, long batchId) {
        // ISV 自建冷存储 SaveServiceHelper.save 到 isv_xxx_permlog_cold
        // 然后 DeleteServiceHelper.delete 主表
    }
    private void migrateToExternal(DynamicObject[] entities, long batchId) {
        // 序列化 + S3/OSS 上传
    }
}
```

#### 步骤 2 · 在 hrcs_permlogarchive_set 子页扩展（看入侵性）

如果客户接受多套配置：
- ISV 加 List 入口 · 维护 isv_xxx_permlog_archive_policy
- 标品 hrcs_permlogarchive_set 不动

如果客户希望把多套配置嵌入标品归档子页：
- 需要 modifyMeta 标品 formId（被 PR-001 阻止）
- 改方案：用 ISV 工具栏按钮 · 跳到 ISV 子页

#### 步骤 3 · 配置编码规则（PR-006）

归档批次号 batchId 走苍穹编码规则基础资料配置 · 不要硬编码

### 验证清单
- [ ] isv_xxx_permlog_archive_policy 多条策略并行生效
- [ ] 每天凌晨 2 点 IsvPermLogArchiveTask 跑 · 日志按策略归档
- [ ] 归档批次号 ID.genLongId() 唯一
- [ ] 删除前有冷存储备份选项

### 风险
- ⚠ DeleteServiceHelper 删主表数据是物理删除 · 不可恢复 · 必须先冷存储备份再删
- ⚠ permlog 主表删 · 12 子分录会级联删（ORM 级联）· 这是预期但要确认
- ⚠ 调度任务并发：如果客户配多个调度入口 · 用 batchId 防重
- ⚠ 大批量删除性能 · 分批 1000 条/批
- ⚠ 删除日志会破坏审计追溯链 · 客户必须签字确认

---

## 总结：ISV 定制 hrcs_permlog 的 7 个核心方案

| CS | 类型 | 核心 PR 引用 | 工作量 | 风险 |
|---|---|---|---|---|
| CS-01 自定义日志列 | 元数据扩展 | PR-001/PR-007 | 中 | 上游场景多 |
| CS-02 列表过滤 | List Plugin | PR-001/PR-002 | 低 | 解除 hashandle 安全风险 |
| CS-03 字段隐藏 | 列表+详情 Plugin | PR-001/PR-007 | 中 | 仅 UI 脱敏不防 F12 |
| CS-04 SOC 推送 | BEC + ISV 订阅 | PR-001/PR-010/PR-011 | 高 | 双发风险 + SOC 降级 |
| CS-05 跨场景归集 | BEC + ISV 聚合表 | PR-001/PR-005/PR-010/PR-011 | 高 | 容量翻倍 |
| CS-06 跨表查询 | DataSet 服务 | PR-001 | 中 | 性能 |
| CS-07 归档清理 | 调度任务 | PR-001/PR-005/PR-006 | 高 | 物理删除不可恢复 |

### 重要：ISV 边界铁律

**所有 7 个 CS 都不能 modifyMeta 标品 formId hrcs_permlog（add/remove field）** · 必须建 ISV 扩展元数据。这是 platform_rules.json PR-001 的核心约束 · 也是 `isv_ownership_redline.md` 最高优先级铁律。

### 引用文件
- `_decompiled/scenarios/hrcs_permlog/PermLogListPlugin.java`
- `_decompiled/scenarios/hrcs_permlog/HRAdminStrictPlugin.java`
- `curated_sdk.json`
- `_shared/platform_rules.json` PR-001/005/006/007/010/011
- `form_lifecycle_rules.json` FP_SF1-SF6 / FP_HL1-HL4 / FP_BDO1-BDO2
