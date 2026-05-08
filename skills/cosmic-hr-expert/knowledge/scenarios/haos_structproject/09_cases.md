# 参考案例 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于反编译实证 + 配套场景 haos_structure 协作业务流的真实案例推演
> **confidence**：verified

## 案例总览

| 案例 | 业务背景 | 关键扩展 CS | 风险等级 |
|---|---|---|---|
| Case-01 · 销售域方案 → 派生大区销售矩阵实例 | 业务方按事业部建母本 → 派生多个大区实例 | CS-01 + CS-02 + CS-04 | 🟢 低 · 标准协作 |
| Case-02 · 母本 roottype 切换破坏下挂组织 | 误改 roottype 引发下挂组织错位 | CS-02（事故预防） | 🔴 高 · 业务故障 |
| Case-03 · 删除被引用的方案导致悬挂 | 删除方案 B · 实例 relyonstructproject 引用悬挂 | CS-04（事故预防） | 🔴 高 |
| Case-04 · 应用领域字段联动 + form 隔离 | 加 ISV 字段不污染 haos_structure 实例 | CS-01 + CS-03 + CS-06 | 🟢 低 · 标准扩展 |
| Case-05 · 方案变更通知派生实例服务 | 方案禁用后通知下游做对应处置 | CS-05（BEC 自建发布）| 🟡 中 |

---

## Case-01 · 销售域方案 → 派生大区销售矩阵实例（标准协作流）

### 业务背景

某 SaaS 客户业务架构：
- 总部按"事业部"建结构化方案 · 共 3 个事业部（销售 / 制造 / 共享服务）· 每个事业部对应一个 haos_structproject 母本
- 各大区根据"事业部方案"派生本大区的具体矩阵组织实例（haos_structure 实例）· 共 5 个大区 × 3 个事业部 = 15 个实例
- 母本变更（如调整 effdt）需要级联通知大区做对应实例调整

### 业务流（典型）

```
[T1] 总部建母本"销售域方案"
  └─ haos_structproject 表单：新建 → name=销售域 / number=SALES
     · roottype=2（虚拟根 · 销售域是逻辑组织 · 没有实组织对应）
     · rootnumber/rootname 自动赋（AdminOrgCodeRuleServiceHelper）
     · effdt=2026-01-01
  └─ save → 派生虚拟根组织 haos_adminorgdetail（otclassify=1010L）
     + sch_task 派单到 haos_adminorg_msgdetail
  └─ enable → enable=10 → 1（HRBaseDataEnableOp 跑）

[T2] 大区运营建实例"华南销售矩阵"
  └─ haos_structure 表单（配套场景）：新建 → relyonstructproject=销售域方案
     · rootorg=华南区行政组织
     · iscustomorg=0（系统标准矩阵）
  └─ save · 在 t_haos_structproject 同表写入 (otclassify=1010L)
  └─ 用户点【维护架构】→ 跳到 haos_orgstructlist
     · 在矩阵下挂华南区下属各部门

[T3] 用户在不同大区重复 T2 · 形成 5 大区 × 3 事业部 = 15 实例

[T4] 总部修改母本（仅 number/name 调整）
  └─ haos_structproject 表单：modify · 抢 edit_struct 互斥锁
  └─ save → 因 dbRootType=newRootType=2 + dbEffDate=newEffDate
     · 走 StructProjectSaveOp L114-118 仅更新基本信息
  └─ 不影响下游实例（OrgStruct 不动）

[T5] 总部禁用某个事业部方案（例如停用"共享服务域"）
  └─ haos_structproject 表单：disable · enable 1 → 0
  └─ 标品 StructProjectDisableOp 没级联禁用实例
  └─ ISV 通过 CS-05 BEC 自建发布方 · 通知下游派生实例服务批量禁用
```

### 业务亮点

- **物理表共用 + 业务区分**：母本 + 实例数据全在 t_haos_structproject 一张表 · 通过 `roottype + otclassify=1010L` 区分
- **跨表 join 用 boid**：实例的 rootorg 引用 haos_adminorghrf7（HisModel · 用 boid + iscurrentversion · PR-008）
- **派单防风暴**：标品 sch_task JOB_ID 全局唯一 · 同时只跑一个

### ISV 扩展点

- CS-01：给方案加"事业部分类"字段 `${ISV_FLAG}_busidiv` · 用于业务方分类管理
- CS-02：save 阶段校验 roottype 切换（已有派生实例时禁切换）
- CS-04：delete 阶段校验反向引用（删除前必须无活跃实例）
- CS-06：加字段时走 form 元数据隔离 · 不污染 haos_structure 表单

### 关联 PR

- PR-001 · ISV 并列挂
- PR-007 · 预置数据 number 不可改（"销售域 / 制造域 / 共享服务域"如果业务方设为预置 issyspreset=1）
- PR-008 · iscurrentversion 时序过滤（跨表查 haos_adminorg）
- PR-011 · BEC 规范

---

## Case-02 · 母本 roottype 切换破坏下挂组织（事故 → 预防）

### 业务背景

某客户业务流：
- 已建母本"试验销售方案"（roottype=1 · 实组织根 = "总部销售部"）
- 派生 5 个 haos_structure 实例（每个大区一个）· 各实例下挂自己的部门树
- 业务方变更需求：把"试验销售方案"的根改成"虚拟销售总部" · roottype 1 → 2

### 事故还原（标品行为）

```
T1: 业务方在 haos_structproject 表单打开"试验销售方案"
T2: 改 roottype 1 → 2 · 弹出 rootnumber/rootname/rooteffdt 必填字段
T3: 填好虚拟根信息 → save
T4: StructProjectSaveOp.saveStructProjectAndRootOrg L131-180
   · dbRootType=1 + newRootType=2 + 假设 dbEffDate ≠ effDate
   · 进入"非同 roottype + 非同 effdt"分支
   · OrgStructRepository.queryOriginalCurrentStructDys(structProjectId, ...)
     拿到所有现有 OrgStruct（包括 5 大区下挂的部门）
   · 构造 OtherStructEntity:
     - 旧根 oldRootOrgId="总部销售部 id"
     - 新根 rootOrgId=新虚拟根 id
     - parentorg 旧根 → 新根 全部映射
   · setDeleteRoot(true) · 标记删除旧根
   · OtherStructService.saveOtherStruct() 整批迁移
T5: 5 大区实例的下挂组织全部被改写：
    · 旧的"总部销售部 → 大区 → 部门"层级
    · 改成"新虚拟销售总部 → 大区 → 部门"
    · 旧根（总部销售部）被删除
T6: 业务方观察：所有大区实例的下挂结构看起来"正确"但根变了
    实际上："总部销售部"这个实组织（在 admin_org 域）被 BatchOrgDeleteOpService 删除了
    导致其它依赖"总部销售部"的业务（如某些员工归属）出现引用悬挂
```

### 业务后果

- 5 大区实例的下挂层级展示变化
- "总部销售部"实组织被删除 · 其它依赖此组织的业务（员工归属 / 薪酬归属 / 考勤归属）出现故障

### ISV 预防方案 · CS-02

```java
public class RootTypeChangeWhenDerivedValidator extends AbstractValidator {
    @Override
    public void validate() {
        // 1. 收集本次保存的方案 id
        // 2. 反查 db 里的 roottype 旧值
        // 3. 对比 newRootType 是否变化
        // 4. 反查 haos_structure.relyonstructproject IN (变更的方案 id)
        // 5. 有引用 → addErrorMessage("方案根组织类型已被以下 haos_structure 实例引用 · ...")
    }
}
```

挂在 save + submit + submitandnew 三个 opKey 上 · 阻断 roottype 切换。

### 教训

- 标品的"宽容设计"会改写下挂组织树 · 无校验
- ISV 必须按业务约束加跨表反向引用校验
- 共用物理表场景（母本-实例双向）反向引用是核心扩展点

### 关联 PR

- PR-001 · 并列挂
- PR-008 · 跨表查 iscurrentversion（如 haos_structure 改造为 HisModel 时）
- PR-010 · OP 13 方法顺序（onAddValidators 在 3）

---

## Case-03 · 删除被引用的方案导致引用悬挂（事故 → 预防）

### 业务背景

某客户业务流：
- 总部建多个方案（共 10 个）· 其中 7 个被实例引用
- 业务方清理"无用方案"· 在列表选 3 个方案点【删除】· 误把其中 2 个被引用的方案也勾选

### 事故还原（标品行为）

```
T1: 业务方在 haos_structproject 列表选 3 个方案 · 点【删除】
    opKey=delete_project · StructProjectListPlugin.openOperationPage L128 调
    afterDeleteOperation
T2: StructProjectDeleteOP.beginOperationTransaction L46-55
    · 仅按 enable='10' 过滤 · 这 3 个方案都是 enable=10 · 全部进入 needToDeleteData
    · structProjectApplication.doWithDelete(dyToDel) · 直接删除
T3: 列表 refresh · 业务方看到 3 个方案消失 · 操作成功
T4: 几小时后 · 用户在 haos_structure 列表点开"华东销售矩阵"实例
    · F7 字段 relyonstructproject 显示空 / 报错（找不到引用的母本）
T5: 业务方排查 · 发现是删除方案 B 时关联实例没解绑
```

### 业务后果

- 多个 haos_structure 实例的 relyonstructproject 引用悬挂
- 用户体验：实例打开报错 / F7 显示空
- 数据修复：要么手动重新绑定实例的 relyonstructproject · 要么从 backup 恢复

### ISV 预防方案 · CS-04

```java
public class StructProjectReferringInstanceValidator extends AbstractValidator {
    @Override
    public void validate() {
        // 1. 收集 deleteIds
        // 2. 反查 haos_structure.relyonstructproject IN (deleteIds)
        // 3. 任何 enable 状态的实例都阻断（禁用是可逆的）
        // 4. 给每个被引用的方案加错误消息 · 列出实例名
    }
}
```

挂在 delete + delete_project 两个 opKey 上。

### 教训

- 标品 StructProjectDeleteOP 仅按 `enable=10` 过滤 · **没**反向引用校验
- 业务方误删一次 · 数据修复成本大
- ISV 必须加 CS-04 · 防御性扩展

### 关联 PR

- PR-001 · 并列挂
- PR-010 · OP 13 方法顺序

### 跟 haos_structure 配套场景的协作

- 校验跨表查 `haos_structure.relyonstructproject` · 这是 haos_structure 的关键字段（详见 [`../haos_structure_maintenance/03_model_design.md`](../haos_structure_maintenance/03_model_design.md) 第 3.5 节）
- 实例的 enable 状态包括 1 / 0 / 10 三态 · 都要阻断（实例可被重启用）

---

## Case-04 · 应用领域字段联动 + form 元数据隔离（标准扩展）

### 业务背景

某 SaaS 客户的多租户场景：
- 同一套苍穹 SaaS 服务于 10+ 客户
- 每个客户业务需要"应用领域分类"· 客户 A 是"销售域 / 制造域 / 共享服务域"· 客户 B 是"国内 / 国际"
- ISV 决定：给 haos_structproject 加 `${ISV_FLAG}_appdomain` ComboField + `${ISV_FLAG}_permtemplate` BasedataField + 联动逻辑

### ISV 扩展实施

#### Step 1：加字段（CS-01 + CS-06）

```python
# 用 form 元数据隔离 · 仅挂 haos_structproject · 不挂 haos_structure
client.create_isv_ext_meta(
    parent_form_id="haos_structproject",
    form_number="${ISV_FLAG}_haos_structproject_ext"
)

designer.add_field(
    field_type="ComboField",
    name="应用领域", key="${ISV_FLAG}_appdomain",
    items=[
        {"name": "销售域", "value": "SALES"},
        {"name": "制造域", "value": "MFG"},
        {"name": "共享服务域", "value": "SSC"}
    ]
)

designer.add_field(
    field_type="BasedataField",
    name="权限模板", key="${ISV_FLAG}_permtemplate",
    refEntity="${ISV_FLAG}_permtpl"  # ISV 自建权限模板基础资料
)
```

#### Step 2：加联动 FormPlugin（CS-03）

```java
public class StructProjectAppDomainLinkagePlugin extends HRDataBaseEdit {
    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        if (!"${ISV_FLAG}_appdomain".equals(e.getProperty().getName())) {
            return;
        }
        Object newValue = e.getChangeSet()[0].getNewValue();
        if (newValue == null) {
            getModel().beginInit();
            try { getModel().setValue("${ISV_FLAG}_permtemplate", null); }
            finally { getModel().endInit(); }
            getView().updateView("${ISV_FLAG}_permtemplate");
            return;
        }
        // 反查权限模板 + setValue
        DynamicObject template = HRBaseServiceHelper("${ISV_FLAG}_permtpl").queryOne(
            "id", new QFilter("domain", QCP.equals, newValue).toArray());
        if (template != null) {
            getModel().beginInit();
            try { getModel().setValue("${ISV_FLAG}_permtemplate", template.getLong("id")); }
            finally { getModel().endInit(); }
            getView().updateView("${ISV_FLAG}_permtemplate");
        }
    }
}
```

#### Step 3：验证 form 隔离

```python
# haos_structure 表单不应该看到 ${ISV_FLAG}_appdomain
schema_instance = client.get_form_schema("haos_structure")
assert "${ISV_FLAG}_appdomain" not in schema_instance  # ✅ 元数据隔离成功

# haos_structproject 自身能看到
schema_mother = client.get_form_schema("${ISV_FLAG}_haos_structproject_ext")
assert "${ISV_FLAG}_appdomain" in schema_mother  # ✅
```

### 业务亮点

- 物理列加在共用表 t_haos_structproject · 不可避免
- form 元数据隔离让 haos_structure 表单"看不见"该字段 · 业务方无感
- 联动逻辑通过 propertyChanged 实现 · 不影响标品 propertyChanged（roottype / isincludevirtualorg）

### 关联 PR

- PR-001 · ISV 扩展机制
- PR-003 · FormPlugin 用 getModel().setValue()
- PR-004 · setValue 死循环防护
- PR-007 · 预置数据 number 不可改

### 跟 haos_structure 配套场景的协作

- haos_structure 表单的物理表多了 ftdkw_appdomain / ftdkw_permtemplateid 列 · 但表单视图不显示 · 实例数据该列 NULL
- 业务方在 haos_structure 操作不感知 ISV 字段 · 母本-实例的"语义独立"通过 form 元数据隔离实现

---

## Case-05 · 方案变更通知派生实例服务（BEC 自建发布）

### 业务背景

某金融客户的合规要求：
- 任何方案的 disable / 删除 / 关键字段（roottype / effdt）变更都要通知合规审计系统
- 合规审计系统是 ISV 自建独立服务 · 监听 BEC 事件 · 触发对应审计流程

### ISV 扩展实施 · CS-05

#### Step 1：业务事件中心配 eventNumber

```
苍穹开发平台 → 业务事件中心 → 事件定义 → 新建：
- eventNumber: ${ISV_FLAG}_haos_structproject_changed
- 事件名称: 矩阵组织方案变更
- 描述: ISV 自建·方案 save/disable/enable/delete 时发布
```

#### Step 2：写发布方 OP

```java
public class StructProjectChangedPublishOp extends HRDataBaseOp {
    private static final String EVENT_NUMBER = "${ISV_FLAG}_haos_structproject_changed";

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        IEventService svc = ServiceHelper.getService(IEventService.class);
        for (DynamicObject entity : args.getDataEntities()) {
            JSONObject payload = new JSONObject();
            payload.put("id", entity.getLong("id"));
            payload.put("opKey", this.getOperateKey());
            payload.put("changeScene", deriveChangeScene(this.getOperateKey()));
            try {
                svc.triggerEventSubscribeJobs("haos", EVENT_NUMBER,
                    payload.toJSONString(), null);
            } catch (Exception e) {
                LOGGER.error("publish failed · id=" + entity.getLong("id"), e);
            }
        }
    }
}
```

挂在 save / disable / enable / delete / delete_project 五个 opKey 上。

#### Step 3：写订阅方

```java
public class StructProjectChangedNotifyConsumer implements IEventServicePlugin {
    @Override
    public Object handleEvent(KDBizEvent evt) {
        JSONObject payload = JSONObject.parseObject(evt.getSource());
        long projectId = payload.getLongValue("id");
        String changeScene = payload.getString("changeScene");
        // 调合规审计系统接口
        callAuditService(projectId, changeScene);
        return null;
    }
}
```

#### Step 4：业务事件中心配订阅

```
苍穹开发平台 → 业务事件中心 → 订阅配置 → 新建：
- 订阅名称: 矩阵组织方案合规审计
- 事件: ${ISV_FLAG}_haos_structproject_changed
- 订阅插件: ${ISV_FLAG}.hrmp.haos.structproject.bec.StructProjectChangedNotifyConsumer
```

### 业务亮点

- 跟标品 sch_task 派单**并行**（标品发的是 admin_org 域消息 · 本 ISV 发的是方案级事件）
- 订阅方做幂等 · 用 evt.getEventId() 去重
- 主事务在 BEC 发布失败时不回滚（订阅方独立事务）

### 风险点

- 必须先在业务事件中心配 eventNumber + 订阅方 · 否则发的事件无人接 · 不报错（PR-011 prerequisite）
- 订阅方失败会重试 · 必须做幂等

### 关联 PR

- PR-001 · 并列挂
- PR-010 · afterExecuteOperationTransaction（13 方法第 9 阶段）
- PR-011 · BEC 规范

### 跟 haos_structure 配套场景的协作

- ISV 订阅方收到方案变更事件后 · 可批量查 haos_structure.relyonstructproject = projectId · 找到所有派生实例
- 通知合规审计系统时 · 把派生实例信息一起带上（合规需要审计"方案变更影响哪些实例"）
- 这是 CS-05 + CS-04 的组合应用（用反向引用查询 + BEC 发布 · 实现完整通知链）

---

## 案例对照矩阵

| 案例 | 业务诉求 | CS 组合 | 总风险 |
|---|---|---|---|
| Case-01 | 标准协作（母本-实例）| - | 🟢 低 |
| Case-02 | 防 roottype 切换破坏下挂 | CS-02 | 🔴 高 → 加 CS-02 后 🟢 低 |
| Case-03 | 防删除引用悬挂 | CS-04 | 🔴 高 → 加 CS-04 后 🟢 低 |
| Case-04 | 加字段不污染配套场景 | CS-01 + CS-03 + CS-06 | 🟢 低 |
| Case-05 | 合规审计通知 | CS-05 | 🟡 中 |

## 附 · ISV 落地 checklist

实施 haos_structproject ISV 扩展前 · 按以下顺序逐条核对：

- [ ] 已读 03_model_design 理解 35 字段 + 共用物理表机制
- [ ] 已读 04_business_flow 理解方案 9 阶段生命周期
- [ ] 已读 05_data_flow 理解派生根组织 + sch_task 派单
- [ ] 已读 06 CS 选定扩展点
- [ ] 确认禁继承 8 个 final 类（PR-001）
- [ ] 业务事件中心是否需要配 eventNumber（CS-05）
- [ ] 跟 haos_structure 配套场景的反向引用是否已校验（CS-02 / CS-04）
- [ ] 加字段是否走 form 元数据隔离（CS-06）
- [ ] propertyChanged 是否包 beginInit/endInit（PR-004）
- [ ] OP 是否用 entity.set() 而非 getModel().setValue()（PR-003）
