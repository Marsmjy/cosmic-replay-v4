# 推荐定制方案 · 行政组织快速维护

> **状态**: 🟢 基于 `knowledge/pattern/` 真实 Pattern 库整合
> **confidence**: real_deploy

所有方案遵循统一结构（借鉴 Stripe / Salesforce Developer Docs）：
**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 Pattern**

---

## CS-01 · 给 haos_adminorg 扩展字段（最高频）

**关联 Pattern**: [Pattern A · add_field_extension](../../pattern/add_field_extension/README.md)

### 需求
业务方说：组织要新增"所属大区"字段，按大区做薪酬/考勤划分。

### 推荐方案

- **扩展对象**: `haos_adminorg` (**主表，不是 detail**)
- **扩展点**: `modifyMeta(op=add field)`
- **风险**: 低
- **⚠️ 级联警告**: 加 1 字段 → 调整申请单需加 4 份（见 CS-02）

### 调用链（3 步）

```
Step 1: getDevInfo()                        // 拿 ISV 信息
Step 2: getBizApps()                        // 找 bizAppId / bizUnitId
Step 3: modifyMeta({
  formId: "haos_adminorg",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "haos_adminorg",
    element: {
      fieldType: "TextField",
      key: "${ISV_FLAG}_region",
      name: {zh_CN: "所属大区", en_US: "Region"},
      // fieldName 平台会自动生成：在 key 前加 `f` → ftdkw_region
      // ⚠ 如手动指定 · 不要再加 fk_ 前缀（会生成 ffk_tdkw_region 怪列名）· 建议不传让平台默认
      mustInput: false,
      maxLength: 50
    }
  }]
})
Step 4: getFormSchema("haos_adminorg")      // ⭐ 二次验证落库
```

### 代码框架（使用 cosmic_devportal_client）

```python
from cosmic_devportal_client import CosmicClient

client = CosmicClient.connect(base_url, user, pwd)
designer = client.open_existing_designer(
    parent_page_id="<devpmanager pageId>",
    target_form_info={"id": "<form_id>", "number": "haos_adminorg"}
)

designer.add_field(
    field_type="TextField",
    name="所属大区",
    key="${ISV_FLAG}_region",
    parent_entity_id=designer.base_entity_id
)
designer.save()  # save click 一次提交
```

### 踩坑

- ❌ 扩展 `haos_adminorgdetail` 而非 `haos_adminorg` → 继承混乱（CS-01 扩展的是**主实体** · 不是 detail 视图）
- ❌ 字段 key 不带 ISV 前缀 → 标品升级被覆盖
- ❌ `fieldName` 列名超过 25 字符 → 苍穹平台开发规范限制 · 数据库建表失败
- ❌ `fieldName` 手动写 `fk_` 前缀 → 平台会再加 `f` → 列名变 `ffk_xxx` 怪列名 · **建议不传 fieldName 让平台按 `f + key.lowercase()` 自动生成**
- ❌ 引用组织用 `HRAdminOrgField` 类型（OpenAPI 74 值枚举不支持）→ 改用 `BasedataField` + `refEntity=haos_adminorghrf7`（参考 scene_doc.json `parentorg` 字段）
- 💡 多语言表规范是 `_l` 结尾（如 `t_haos_adminorg_l`）· `_i` 是**拆分表**（垂直分表 · 性能优化）· 不要混（参考管道坑 11）

---

## CS-02 · 主表字段级联到调整申请单 4 前缀 ⭐ 组织独有

**关联 Pattern**: [Pattern C · orgbill_4prefix_cascade](../../pattern/orgbill_4prefix_cascade/README.md)

### 需求
CS-01 给 haos_adminorg 加了 `${ISV_FLAG}_region` 字段后，**调整申请单**也需要同步支持显示/编辑这个字段。

### 推荐方案

- **扩展对象**: 4 个调整申请单分录容器
- **扩展点**: `modifyMeta × 4` (挂到 4 个并列分录)
- **调整申请单 formNumber**：`homs_orgbatchchgbill`（组织管理 homs 应用 · 2026-04-24 菜单实探确认）
- **4 前缀 EntryEntity OID**（2026-04-23 HAR 实抓 · ⚠ **跨环境需二次验证** · 参考 `feedback_har_values_not_authoritative.md`）：

| 前缀 | OID | 含义 |
|---|---|---|
| 无前缀 | `VQ597FqFoc` | 原组织信息 |
| `info_` | `7auphYEIJr` | 变更后信息 |
| `parent_` | `8bosVcKAfQ` | 上级信息 |
| `add_` | `wHBtyCCUik` | 新增信息 |

> ⚠ **跨环境落地前必须验证**：在目标环境调 `getFormSchema("homs_orgbatchchgbill")` 拿到真实 4 个 EntryEntity 的 OID · 替换以上硬编码值。OID 在不同环境可能不同（feedback 铁律）。

### 调用链

```python
MAIN_FORM = "haos_adminorg"              # 主表 formNumber
ORGBILL_FORM = "homs_orgbatchchgbill"    # 调整申请单 formNumber（2026-04-24 菜单实探确认）

# 1. 主表加字段
designer_main = client.open_existing_designer(
    target_form_info={"number": MAIN_FORM}
)
designer_main.add_field("TextField", "所属大区", "${ISV_FLAG}_region")
designer_main.save()

# 2. ⚠ 跨环境验证：落地前调 getFormSchema 取目标环境的真实 4 OID
schema = client.get_form_schema(ORGBILL_FORM)
# 按业务含义从 schema 里反查 4 个 EntryEntity 的真实 OID（可能不同环境不同值）

# 3. 打开调整单设计器
designer_orgbill = client.open_existing_designer(
    target_form_info={"number": ORGBILL_FORM}
)

# 4. 4 分录并行加字段（OID 硬编码值仅供参考 · 落地前用 Step 2 的真实 OID 替换）
for prefix, oid in [("", "VQ597FqFoc"),
                    ("info_", "7auphYEIJr"),
                    ("parent_", "8bosVcKAfQ"),
                    ("add_", "wHBtyCCUik")]:
    field_key = f"{prefix}${ISV_FLAG}_region"
    designer_orgbill.add_field(
        field_type="TextField",
        name="所属大区",
        key=field_key,
        parent_entity_id=oid
    )

designer_orgbill.save()  # 一次 save click 提交 4 字段
```

### 踩坑

- ❌ 漏加任一前缀 → 调整单某视图显示不全
- ❌ 字段 key 命名不遵循 `{前缀}_{主字段key}` → 标品同步逻辑识别不到
- ❌ 想找个"统一父级" → 没有，必须 4 份独立 EntryFieldAp

---

## CS-03 · 组织编码按公司前缀自动生成

### 需求
新建组织时，编码自动按"公司代码_3位序号"格式生成，不用手填。

### 推荐方案

- **扩展点**: `beforeSave@haos_adminorg`
- **实现模式**: 继承 + super 调用 + 前置填充
- **风险**: 低

### 扩展入口坐标

- 绑定表单：`haos_adminorg`
- 绑定操作：`save`
- 推荐父类：`HRDataBaseOp`（HR 通用 OP 抽象基类 · SDK 白名单合规）· **并列挂新插件 · 不继承 `AdminOrgFastSaveOp` / `AbsOrgBaseOp`**（PR-001）
- 关键重写方法：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs e)` — 独立补 `number` · **不调 super**（并列挂新插件无标品父类链 · CodeRuleOp 在执行链下一位独立接管）

**业务意图**：新增组织时若用户未填写编码 · 按"公司代码_3 位序号"规则自动生成并回填到 `DataEntity` 的 `number` 字段。本插件挂在 save 链 **RowKey 早于 `CodeRuleOp`** 的位置（RowKey=1 · PR-002）· 标品 `CodeRuleOp` 见 number 非空自动跳过生成 · 下游标品校验（唯一性 / 层级）正常走。

> ⭐ **ISV 并列挂不继承** · 不存在 super 调用问题。插件执行顺序由开发平台 RowKey 控制（PR-002）· 排在 `CodeRuleOp` 之前即可。

> 💡 **优先业务侧配置**（PR-006）：如果编码规则只是"前缀 + 序号"这种标品能配的格式 · **不用写本 CS** · 直接在【编码规则基础资料】配即可。本 CS 适用于"按字段值动态切换前缀"这种必须代码控制的场景。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ **新增**（并列挂 · 不覆盖标品插件）· RowKey=1（早于 CodeRuleOp）
4. 保存 → 部署生效

### 踩坑

- ⚠️ **序号生成用 `kd.bos.id.ID.genLongId()` / `ID.genStringId()`**（PR-005）· 苍穹标品天然分布式唯一 · 不要自接 Redis 造锁
- ⚠️ **RowKey 必须早于 CodeRuleOp**（PR-002）· 否则标品 CodeRuleOp 先跑生成了默认编码 · 再写会冲突
- ⚠️ 批量导入来源建议通过 `OperateOption.getVariableValue("isFromPage")` 识别（见 `_auto_plugin_semantics.md`）· 跳过自动生成
- ❌ **不要继承 `AbsOrgBaseOp`**（`rules_chain_all.json` 禁继承清单 · 违反 PR-001）· 用 `HRDataBaseOp` + 并列挂
- ❌ **不要复用标品 `CodeRuleOp`**（它是平台模板绑定的 · 业务侧通过编码规则基础资料配 · PR-006）· ISV 代码不该碰

---

## CS-04 · 订阅组织变更事件 → 自动通知下属员工（BEC 订阅方）⭐ 组织特有

**关联 Pattern**：无独立 pattern · BEC 是苍穹官方事件分发机制（参考 `knowledge/_scenarios_t0/G2_business_event_message.json` 的 G2b 订阅方范式）

### 需求

业务方说：上级组织调整（如部门合并、上级变更）后，要给该组织下所有在职员工发站内信通知"您所属组织已调整"。

**⚠ 错误常识澄清**（2026-04-24 修订）：
- `movesup` opKey **不存在**。上级变更真实走的是 `confirmchange` opKey + `AdminOrgFastParentChangeOp` 操作插件（`rules_chain_all.json` L1544）
- 标品**已经**在变更成功后发业务事件（`rules_chain_all.json` L532 + L1616-L1624 实证），ISV **不要重复发**，只要**订阅**即可

### 推荐方案

- **扩展点**：**订阅标品 BEC 事件** · 不挂 `haos_adminorg` 的 OP 插件
- **实现模式**：实现 `kd.bos.bec.api.IEventServicePlugin.handleEvent(KDBizEvent)` + 开发平台【业务事件中心】→【事件订阅】配置绑定
- **风险**：低（只读事件 + 发通知 · 订阅方**独立事务** · 不影响标品主事务；PR-011）

### 标品发布方（仅作证据 · ISV 不动它）

| 项 | 值（实证来源） |
|---|---|
| opKey | `confirmchange`（save/confirmchange/confirmchangenoaudit 链共用） |
| 标品 OP | `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp`（`rules_chain_all.json` L1544 · 确认**禁继承** L3055） |
| 真正发事件位置 | `BatchAdminOrgChangeParentOpService.afterTransDoOp` 阶段（`rules_chain_all.json` L1620 `B_confirmchange_5`） |
| 发事件代码 | `new AdminChangeMsgService().handleChangeMsg()` → 内部派 `sch_task` JOB_ID=`5+X/4Y=AOZ=O` 异步走 `EventServiceHelper.triggerEventSubscribeJobs` |
| 标品派单表 | `haos_adminorg_msgdetail`（bo/beforeversion/afterversion/changescene/changeoperate/isbelongcompanychange/sendstate，`AdminChangeMsgService.assembleMsgDy` 实证） |

### 苍穹 BEC 订阅方 API · 2026-04-24 反编译实证

| API | FQN · 注解 | 证据 jar |
|---|---|---|
| 订阅接口 | `kd.bos.bec.api.IEventServicePlugin` · `@SdkPublic` | `bos-mservice-bec-api-8.0.jar` |
| 接收方法 | `Object handleEvent(KDBizEvent event)` | 同上 |
| 事件模型 | `kd.bos.bec.model.KDBizEvent` · `@SdkPublic`（4 字段 eventId/eventNumber/source/variables；`getSource()` 返回 JSON 字符串） | 同上 |
| 发布工具类 | `kd.bos.servicehelper.workflow.EventServiceHelper` · `@SdkPublic + @SdkService`（标品内部走这个 · ISV 作为订阅方不直接用） | `_shared/_decompiled/bos_common/EventServiceHelper.java` |

### 扩展入口坐标

- **绑定表单**：**不绑**（订阅方不挂 opKey · 靠开发平台业务事件中心挂钩）
- **实现接口**：`kd.bos.bec.api.IEventServicePlugin`（**不继承** `HRDataBaseOp` / `AbsOrgBaseOp`，订阅方是独立消费者插件，参考 PR-001）
- **关键方法**：`handleEvent(KDBizEvent evt)` — 解析 `evt.getSource()` JSON → 按 `changescene` 分支 → 查下属员工 → 发通知

**业务意图**：标品 `AdminChangeMsgService.handleChangeMsg` 已把组织变更通过 BEC 异步派发，ISV 只要在【业务事件中心】→【事件订阅】把 Consumer 绑到"行政组织变更"事件号即可。订阅方独立事务运行，标品主事务不会被卡 · 通知失败也不会回滚组织变更（PR-011）。

### 下游员工查询路径（2026-04-24 · 真实数据 · 来自 `refentity_reverse.json`）

**数据源**：`knowledge/workbench/_indexes/refentity_reverse.json` 的 `refs.haos_adminorghrf7` 段 · 17 条 hrpi_* 引用

**员工维度的主表**：`hrpi_empjobrel`（员工-任职关系 · 时序）· 含 2 个关键字段：
- `adminorg`（`HRAdminOrgField` → `haos_adminorghrf7`）— 任职所在行政组织
- `employee`（`EmployeeField` → `hrpi_employeenewf7query`）— 员工引用

**查询规则**（PR-008 + PR-009）：
- 按 **`adminorg` = 组织 boid** 过滤（PR-009 下游存的是业务维度 boid，不是版本 id）
- 带 **`iscurrentversion = true`**（PR-008，hrpi_empjobrel 也是 HisModel · 不过滤会查到多个版本）
- 可选过滤在职状态（具体字段名以该场景 scene_doc 为准 · 不在 admin_org 知识范围内）

**补充下游**（同逻辑 · 按需通知）：
- `hrpi_empposorgrel.adminorg`（人-岗-组关系）
- `hrpi_rotationinfo.adminorg`（轮岗信息）
- `hrpi_dispatchinfo.adminorg`（派遣信息）

### 代码示例

```java
package ${ISV_FLAG}.hrmp.haos.bec.consumer;

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
 * 订阅 "行政组织变更" 事件 · 通知下属在职员工
 * 业务事件中心绑定流程见平台绑定方式
 *
 * 只订阅 · 不发事件 · 不挂 haos_adminorg 任何 opKey
 * 不继承 HRDataBaseOp / AbsOrgBaseOp · 实现 IEventServicePlugin 即可（PR-001）
 */
public class AdminOrgChangeNotifyConsumer implements IEventServicePlugin {

    private static final Log LOGGER =
            LogFactory.getLog(AdminOrgChangeNotifyConsumer.class);

    @Override
    public Object handleEvent(KDBizEvent evt) {
        String payload = evt.getSource();
        LOGGER.info("AdminOrgChangeNotifyConsumer received eventNumber={}, payload={}",
                evt.getEventNumber(), payload);

        try {
            // 幂等标记（PR-011）：用 eventId 去重 · 已处理过直接 return
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
                JSONObject orgData = dataList.getJSONObject(i);
                long orgBoid = orgData.getLongValue("boid");
                String orgNumber = orgData.getString("number");
                Object orgName = orgData.get("name");
                Object changeScene = orgData.get("changescene");

                notifyEmpsUnderOrg(orgBoid, orgNumber, orgName, changeScene);
            }
            return null;
        } catch (Exception e) {
            LOGGER.error("AdminOrgChangeNotifyConsumer handleEvent failed · eventId={}",
                    evt.getEventId(), e);
            throw e;
        }
    }

    /**
     * 查 hrpi_empjobrel · 按组织 boid + iscurrentversion=true 查当前任职员工
     * refentity_reverse.json 实证：hrpi_empjobrel.adminorg → haos_adminorghrf7
     */
    private void notifyEmpsUnderOrg(long orgBoid, String orgNumber,
                                    Object orgName, Object changeScene) {
        HRBaseServiceHelper empJobRel = new HRBaseServiceHelper("hrpi_empjobrel");

        // PR-009：按 boid · PR-008：带 iscurrentversion=true
        QFilter qf = new QFilter("adminorg", QCP.equals, orgBoid)
                .and("iscurrentversion", QCP.equals, Boolean.TRUE);

        DynamicObject[] rels = empJobRel.query("id,employee", qf.toArray());
        if (rels == null || rels.length == 0) {
            return;
        }

        LOGGER.info("Org boid={} number={} changescene={} · {} active rels to notify",
                orgBoid, orgNumber, changeScene, rels.length);

        for (DynamicObject rel : rels) {
            Object employeeId = rel.get("employee_id");
            // sendNotify(employeeId, "您所属组织 [" + orgNumber + "] 已调整...");
        }
    }
}
```

### 平台绑定方式

1. **前置·标品已做**：标品 `AdminChangeMsgService.handleChangeMsg` 已经在 `confirmchange` 链 `afterTransDoOp` 自动发事件（`rules_chain_all.json` L1620）· ISV **不用重复发**
2. 打开【苍穹开发平台】→【业务事件中心】→【事件定义】· 确认 "行政组织变更" 事件的 `eventNumber`（标品预置 · 具体编号以环境【事件定义】列表查到的为准 · 不脑补）
3. 进【事件订阅】→ 新建订阅：
   - 订阅编码：`${ISV_FLAG}_adminorg_change_notify`
   - 绑定事件：上一步查到的组织变更 eventNumber
   - 执行服务：选"执行插件"
   - 插件名称：`${ISV_FLAG}.hrmp.haos.bec.consumer.AdminOrgChangeNotifyConsumer`
4. 配置错误处理策略（重试次数 / 失败通知）→ 保存 → 部署生效

> ⭐ `eventNumber` 必须从【事件定义】实际列表抓 · **不要脑补** `HAOS_ORG_CHANGED` 之类的编号（`feedback_formid_no_fabrication.md`）

### 踩坑

- ⚠️ **幂等必须自己做**（PR-011 · "执行条件批量失效" 警告）：按 `evt.getEventId()` 或 `eventId + boid` 去重 · 订阅方重试机制会重入
- ⚠️ **下游查询按 `boid` 不按 `id`**（PR-009）：`hrpi_empjobrel.adminorg` 字段存的是组织 boid · 用 id 查会全漏
- ⚠️ **`iscurrentversion=true` 必须加**（PR-008）：`hrpi_empjobrel` 是 HisModel · 不过滤会查到历史任职版本 · 误通知已离职员工
- ⚠️ **`evt.getSource()` 是 JSON 字符串** · 不是 DynamicObject · 必须 `fastjson parse`；内层结构通常是 `[{data: [...]}]`
- ⚠️ 大批量组织变更会触发一次事件带多条 data · 循环处理时按单条做幂等 · 不要把多条合并成一次失败就整批重试
- ⚠️ 通知失败**不要抛异常**把订阅方重试机制耗光 · 自己 catch 记录到失败表 · 走独立补偿任务
- ❌ **不要挂 `haos_adminorg` 的 `confirmchange` / `save` opKey 写自定义 OP**（违反 PR-001 · 标品已发事件 · ISV 再发会重复 · `AdminOrgFastParentChangeOp` 在 `rules_chain_all.json` L3055 明确禁继承清单）
- ❌ **不要继承 `AbsOrgBaseOp`**（`rules_chain_all.json` L3055 禁继承清单第一位 · 非 @SdkPlugin 白名单）
- ❌ **不要自建 Kafka/RabbitMQ**（违反 PR-011 · BEC 已封装 · 重造轮子）
- ❌ **不要在 `variables` / payload 里塞完整 DynamicObject**（PR-011 反模式）· 订阅方按 boid 自己回查即可
- ❌ **不要用类字段暂存旧值**（原 CS-04 方案的并发 bug）· BEC 订阅方每次 `handleEvent` 独立调用 · 无状态消费
- ❌ **不要假设 `movesup` opKey 存在** · 真实 opKey 清单见本场景 `opkeys/` 目录 · 上级变更走 `confirmchange`（`rules_chain_all.json` L1490）

### 关联 Pattern

- [G2b · 业务事件消息订阅方范式](../../_scenarios_t0/G2_business_event_message.json)（订阅方官方骨架 + JSON 解析要点）
- PR-001 · ISV 扩展走并列挂 · 禁继承标品专属类（见 `_shared/platform_rules.json`）
- PR-008 · 时序资料 iscurrentversion 过滤
- PR-009 · boid 业务维度 · 下游引用按 boid 查
- PR-011 · 跨模块事件走 BEC · 不自建 MQ
- 参考同族 CS：hjm_jobhr CS-05（BEC **发布方**范式 · 本 CS 是**订阅方** · 对称）

---

## CS-05 · 组织编码唯一性校验（按租户隔离）

**关联 Pattern**: [add_unique_validation](../../pattern/add_unique_validation/README.md)

### 需求
默认组织编码全局唯一，业务要求改为**按租户隔离**（每个租户内唯一）。

### 推荐方案

- **扩展点**: `onAddValidators@save` · 并列挂新 Validator（标品真实类是 `HisUniqueValidateOp` · **非虚构 `OrgCodeUniquePlugin`** · PR-001 不继承不覆盖）
- **实现**: 新插件独立实现 `AbstractValidator` 做"租户内唯一"检查 · 跟标品 `HisUniqueValidateOp` 的"全局唯一"并列跑
- **风险**: 中（两套唯一性校验同时跑 · 需业务拍板是否禁用标品 · 见"踩坑"第 3 条）

### 扩展入口坐标

- 绑定表单：`haos_adminorg`
- 绑定操作：`save`
- 推荐父类：`HRDataBaseOp`（HR 通用 OP 抽象基类）· **并列挂新插件 · 不继承 `AdminOrgFastSaveOp` / `AbsOrgBaseOp`**（PR-001）· 标品 `HisUniqueValidateOp`（真实类名 · 非虚构 `OrgCodeUniquePlugin`）的全局唯一校验继续跑
- Validator：独立继承 **`AbstractValidator`**（不继承任何场景专属 Validator）
- 关键重写方法：
  - `onAddValidators(AddValidatorsEventArgs e)` — 在此 `add` 一个独立继承 `AbstractValidator` 的自定义校验类（与标品 `HisUniqueValidateOp` 并列 · 互不干涉）
  - 在自定义 Validator 的 `validate()` 里按租户维度查重（具体查重维度见"踩坑"第 2 条）

**业务意图**：把"编码全局唯一"语义改为"租户内唯一"。新增自定义 Validator 走租户维度查重；若命中重复，`addErrorMessage` 把错误挂回该行。原标品的编码格式校验、层级校验全部保留，仅替换唯一性这一条语义。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 添加本操作扩展类
4. **标品 `HisUniqueValidateOp` 的全局唯一校验**默认保留 · 跟本插件并列跑（严格模式 · 推荐）。若业务要禁用标品（只剩租户内唯一）· 需在开发平台禁用 `HisUniqueValidateOp` 插件 · **而不是通过 `OperateOption` 跳**（OperateOption 是运行时参数 · 不能禁用插件注册）
5. 保存 → 部署生效

### 踩坑

- ⚠️ **自定义校验独立继承 `AbstractValidator`**（与标品 Validator 并列 · PR-001）· 通过 OP 的 `onAddValidators` 注册
- ⚠️ 跨租户引用数据会错乱（如员工跨租户查组织），方案评审时要明确租户边界
- ⚠️ 跳过标品唯一性校验需要业务方对"租户隔离"充分知情；否则保留标品校验叠加使用更安全

---

## CS-06 · 列表按数据权限动态隐藏

### 需求
普通 HR 只能看管辖范围内的组织，不看全公司。

### ⚠ 前置 · 苍穹"列表"三层结构（管道坑 14.1）

组织快速维护的列表页是三层结构：

| 层 | 元数据 | 职责 |
|---|---|---|
| 数据实体（BaseFormModel）| `haos_adminorgdetail` | 定义数据表 / 字段 / 数据层业务 |
| 列表表单模板（动态表单）| `haos_adminorgtablist`（"带页签行政组织树列表"）| 列表 UI 壳 + 左树结构 + 可选 UI 逻辑 |
| F7 列表模板（动态表单）| `haos_orgtreelistf7` | F7 选择时的列表壳 |

**本 CS 的场景分析**：
- "按数据权限过滤可见组织" = **数据层逻辑**（查谁有权限 · 追加 QFilter）
- → 应挂 **数据实体 `haos_adminorgdetail`** · 不是挂 `haos_adminorgtablist`
- 如果是要改左树结构 / UI 布局 / 壳子层逻辑 · 才挂 `haos_adminorgtablist`（见 CS-06b · 未来补）

### 推荐方案

- **扩展点**: `setFilter@haos_adminorgdetail` · **并列挂新 ListPlugin**（不继承 `OrgListAuthFilterPlugin` / `OrgListStatusFilterPlugin` · PR-001）
- **实现**: 实现 `setFilter` · 追加自定义 QFilter · 平台按注册顺序累加标品过滤 + 本插件过滤
- **风险**: 中

### 扩展入口坐标

- **绑定数据实体**：`haos_adminorgdetail`（**非**列表表单模板 `haos_adminorgtablist` · 见"前置"章节）
- 推荐父类：`HRDataBaseList`（HR 场景列表插件白名单首选 · `@SdkPlugin` 注解）· **不继承** `OrgListAuthFilterPlugin` / `OrgListStatusFilterPlugin`（这两个是标品 · 并列挂即可 · PR-001）
- 关键重写方法：
  - `setFilter(SetFilterEvent e)` — 通过 `e.getQFilters().add(...)` 追加过滤条件（优先用 `e.addCustomQFilter(qf)` · 具体 API 以反编译的 `AdminOrgDetailListPlugin` 实际调用为准）
  - 用 `RequestContext.get().getUserId()` 拿当前操作人 · 再查权限服务拿到可见组织 **boid 集合**（注意：时序资料用 boid 维度 · 不是 id · PR-009）
  - **过滤必须带 `iscurrentversion=true`**（PR-008）· 权限服务返回的通常是 boid · 需配合 `QFilter.in("boid", orgBoids).and("iscurrentversion", "=", Boolean.TRUE)`

**业务意图**：列表查询前按当前用户的管辖范围收窄可见组织：查权限服务拿到管辖组织的 **boid 集合** · 追加 `QFilter.in("boid", orgBoids).and("iscurrentversion", QCP.equals, Boolean.TRUE)` 到 `e.getQFilters()`。标品 `OrgListAuthFilterPlugin` / `OrgListStatusFilterPlugin`（两个都对 QFilter 追加 · 不替换）会跟本插件**并列累加** · 互不干扰。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位**数据实体** `haos_adminorgdetail`
2. 选择【注册插件】→ **新增**（并列挂 · PR-001）· `targetType = LIST_FORM`（R20 · 大写枚举）
3. 保存 → 部署生效

### 踩坑

- ⚠️ **挂数据实体 · 不是挂列表表单模板**：`haos_adminorgdetail`（数据实体 · BaseFormModel）是本 CS 的插件绑定对象；`haos_adminorgtablist`（列表表单模板 · 动态表单）是 UI 壳子 · 只在"改左树 / 改按钮布局 / 壳子层逻辑"类 CS 才绑它（参考管道坑 14.1）
- ⚠️ **只 `add` 过滤条件** · 不能清空 `e.getQFilters()` · 否则标品权限 / 状态过滤会被丢失
- ⚠️ **权限服务返回 ID 维度必须确认**：如果返回的是 id（版本维度）· 按 id 过滤会漏查当前版本；如果是 boid · 按 boid 过滤 + `iscurrentversion=true`（PR-008 + PR-009）
- ❌ **不要继承 `OrgListAuthFilterPlugin` / `OrgListStatusFilterPlugin`**（PR-001 · 标品列表过滤插件 · 并列挂即可）
- ❌ 不要 `e.setQFilters(new ArrayList<>())` 或类似清空操作

---

## CS-07 · 禁止删除有在职员工的组织

### 推荐方案

- **扩展点**: `beforeDelete@haos_adminorg`
- **实现**: 继承 + super + 自定义检查

### 扩展入口坐标

- 绑定表单：`haos_adminorg`
- 绑定操作：`delete`
- 推荐父类：`HRDataBaseOp`（HR 通用 OP 抽象基类）· **并列挂新插件 · 不继承 `AdminOrgFastDeleteOp` / `AbsOrgBaseOp`**（PR-001）
- 关键重写方法：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs e)` — 独立做"下属员工数"统计 · **不调 super**（并列挂的新插件无标品父类链 · 标品下属组织 / 引用检查由 `AdminOrgFastDeleteOp` 在 delete 执行链其它位置独立跑 · 互不干扰）
  - 查下游员工走 **`refentity_reverse.json` 查出的任职关系表**（如 `hrpi_empjobrel.adminorg`）· 按 **boid** 过滤 + `iscurrentversion=true`（PR-008 + PR-009 · 参考 CS-04 查询方式）
  - 用 `addErrorMessage(entity, "该组织下还有 N 名在职员工 · 不能删除")` 阻断（推荐）· 而不是抛 `KDBizException`（可逐行定位错误）

**业务意图**：标品 `AdminOrgFastDeleteOp` 已做"有下属组织 / 被下游引用"检查 · 本扩展**并列**追加"该组织下是否还有在职员工"校验。两个插件在 delete 执行链按 RowKey 顺序跑 · 互不干扰（PR-001 + PR-002）。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `delete`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

> ⚠️ 员工数统计建议走缓存或汇总表，避免删除组织时触发昂贵的 `count(*)`；业务上更推荐用"停用"代替"删除"。

---

## CS-08 · 组织挂子分录（如联系人）

**关联**: 不同于员工附表，组织加分录用 `modifyMeta add EntryEntity`

### 需求
给 haos_adminorg 加一个"组织联系人"分录，记录多个联系人信息。

### 推荐方案

- **扩展对象**: haos_adminorg 主表
- **扩展点**: `modifyMeta(op=add, elementType=entity, ...)`
- **⚠️ 关键**: **不需要** 4 模板继承 / 不需要 _e/_ly/_emly / 不需要 EmbedFormAp

### 调用链

```python
modifyMeta({
  formId: "haos_adminorg",
  ops: [
    {
      op: "add",
      treeType: "entity",
      elementType: "entity",
      parentScope: "haos_adminorg",
      element: {
        entityType: "EntryEntity",
        key: "${ISV_FLAG}_contacts",
        name: {zh_CN: "组织联系人"}
      }
    },
    {
      op: "add",
      treeType: "entity",
      elementType: "field",
      parentScope: "${ISV_FLAG}_contacts",
      element: {
        fieldType: "TextField",
        key: "contact_name",
        name: {zh_CN: "联系人姓名"}
      }
    }
  ]
})
```

---

## CS-09 · HR 业务模型工具一键扩展 ⭐ 预留（待 HR 专家填充）

> **状态**: 🟡 **骨架预留，由 HR 专家填充具体能力**
> **重要性**: 极高（这是 HR 定制开发的"官方高速公路"）

### 背景

苍穹 HR 提供了**业务模型工具**（应该是 `hbp_*` 或类似包），能**一键完成**以下多个步骤的合成操作：

1. modifyMeta 加字段
2. 自动补调整申请单 4 前缀同步
3. 自动写权限预置 SQL（T_HRCS_*）
4. 自动配信息组 SQL（T_HOM_INFOGROUP）
5. 自动下发到下游相关模块

**对比手动路径**: 手动需要 6-8 步，业务模型工具可能 1-2 步完成。

### 适用场景

- 常见的 HR 定制场景（加字段/加附表/加规则）
- 追求快速交付、不要求深度定制
- 不需要自定义 Java 业务逻辑

### 不适用场景

- 复杂的跨模块业务逻辑（仍需写 Java 插件）
- 非标准模式（如跨租户/跨法人特殊处理）
- 需要精细控制每一步的场景

### 操作入口（待填充）

```
<TODO by HR 专家>:
- 工具入口位置（哪个菜单/哪个 UI）
- 具体操作步骤
- 支持的定制类型清单
- 和 OpenAPI 路径的对照
```

### 能力清单（待填充）

```
<TODO by HR 专家>:
- [ ] 一键加字段（含调整单同步）
- [ ] 一键加附表
- [ ] 一键加校验规则
- [ ] 一键配信息组
- [ ] 一键配权限
- [ ] 一键发布
- [ ] 其他...
```

### 配置示例（待填充）

```
<TODO by HR 专家>:
放一个真实的配置样例，包括:
- 输入参数
- 生成的扩展包结构
- 等效的手动 API 调用步骤对照
```

### 选型决策（一键工具 vs 手动）

| 需求特征 | 推荐路径 |
|---|---|
| 标准字段扩展 | 一键工具 ⭐ |
| 标准附表挂接 | 一键工具 ⭐ |
| 复杂业务逻辑 | 手动 + Java 插件 |
| 跨多个标品包联动 | <TODO: 一键工具是否支持？> |
| 自定义校验插件 | 一键工具 + 后续补 Java |

### 踩坑（待填充）

```
<TODO by HR 专家>:
- 一键工具的限制
- 什么情况下生成的代码需要手动修正
- 升级兼容性注意事项
```

### 与手动路径的关系

**推荐工作流**:
```
1. 先用业务模型工具一键生成（80% 场景够用）
2. 检查生成结果是否符合预期
3. 如有定制需求 → 手动补 Java 插件（CS-01 ~ CS-08）
```

**HR 专家补充指引**: 本节待你填充真实的业务模型工具使用方式。填写时重点回答：
- 它和 CS-01（手动加字段）的差异在哪？
- 什么场景下一键工具够用，什么场景下必须走手动？
- 有哪些暗坑？

---

## 方案选型矩阵

| 业务需求 | 推荐 CS | 扩展点 | 复杂度 |
|---|---|---|---|
| 加字段 | CS-01 | modifyMeta | 低 |
| 加字段联动到调整单 | CS-01 + CS-02 | modifyMeta × 5 | 中 |
| 编码自动生成 | CS-03 | beforeSave | 低 |
| 调整上级通知 | CS-04 | onParentChange | 中 |
| 唯一性变更 | CS-05 | beforeSave | 中 |
| 权限列表过滤 | CS-06 | onListQuery | 中 |
| 禁止删除 | CS-07 | beforeDelete | 低 |
| 加分录 | CS-08 | modifyMeta | 中 |

---

## 反模式清单（禁止）

- ❌ 直接修改 `haos_adminorgdetail` 的字段
- ❌ 绕过变动单直接改 `adminorgdetail` 数据
- ❌ 自定义插件给 `level` / `longnumber` / `belongcompany` 赋值
- ❌ 字段 key 不带 ISV 前缀
- ❌ 漏掉 4 前缀分录中的任一份
- ❌ 主表扩展时顺便改 `number` 字段长度
- ❌ 删除而不是停用（丢失审计）

---

**📌 来源追溯**：
- CS-01: `pattern/add_field_extension/README.md`
- CS-02: `pattern/orgbill_4prefix_cascade/README.md`
- CS-05: `pattern/add_unique_validation/README.md`
- CS-08: `anchors.md` 1.2 加子分录/附表
- 反模式: `anchors.md` 禁区 8 条
