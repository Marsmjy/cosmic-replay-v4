# 上下游联动 · 岗位基础资料（hbpm_basedatalist）

> **状态**: 🟢 基于场景分析 + 反编译实证（无 BEC）
> **confidence**: verified（BEC 部分 grep 0 实证）
> **最后更新**: 2026-04-27

---

## 一、上游依赖（本场景依赖的数据源）

| 上游 | 依赖方式 | 说明 |
|---|---|---|
| 平台编码规则（bos_coderule）| CodeRulePlugin 生成 number | 编码规则配置决定 number 格式 |
| 出厂数据包（标品导入）| issyspreset=true 行 | 系统预置基础资料由标品升级包写入 |
| pagekey 指向的各子 form | HRBDGroupList 跳转依赖 | 子 form 不存在则跳转失败 |

---

## 二、下游影响（被本场景数据引用的模块）

### 2.1 直接下游

| 下游场景 | 引用字段 | 引用方式 | 影响说明 |
|---|---|---|---|
| `hbjm_jobhr`（岗位主数据）| 岗位级别/序列/分类等 | BasedataField 引用 | 本场景字典禁用/删除会导致岗位字段游离 |
| 各岗位基础资料子列表 | pagekey 指向 | 列表页跳转依赖 | pagekey 无效则子列表无法打开 |

### 2.2 间接下游（通过 hbjm_jobhr 传递）

```
hbpm_basedatalist（岗位基础资料字典）
    ↓ 被 hbjm_jobhr 引用
hbjm_jobhr（岗位主数据 · HisModel）
    ↓ 通过 boid 被引用
hrpi_empjobrel（员工岗位关系）
    ↓
薪酬模块（基于岗位级别的薪酬带宽）
绩效模块（基于岗位序列的绩效权重）
```

---

## 三、boid / PR-009 在本场景的说明

**本场景是 BaseFormModel，不涉及 boid 概念。**

- `hbpm_basedatalist` 的 id 即是数据唯一标识，下游直接引用 id
- 与 HisModel 场景（如 hbjm_jobhr）的区别：hbjm_jobhr 有 boid/iscurrentversion，下游引用岗位时必须引用 boid（PR-009）
- ISV 在查"哪些岗位引用了本条基础资料"时，直接查 `hbjm_jobhr` 的对应字段等于本条 id（不需要 boid 转换）

---

## 四、BEC 订阅方说明（标品 grep 0 · 实证）

**反编译验证**：
```bash
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hbpm_basedatalist/
# 0 处命中
```

**结论**：
- 标品**没有在本场景发布任何 BEC 事件**
- 标品**没有在本场景订阅任何 BEC 事件**
- 本场景与 haos_adminorgfunction 相同：字典类基础资料，标品不走 BEC

### ISV 如需 BEC 的方案

如确有外部系统需要感知岗位基础资料变更，遵循 PR-011：

```
1. 在【开发平台 → 业务事件管理】预注册 eventNumber
   例：hbpm.positionbasedata.changed

2. 建 ISV 并列挂 OP 插件（父类：HRDataBaseOp）
   绑定表单：hbpm_basedatalist
   绑定操作：save（或 enable/disable 视需求）

3. 重写 afterExecuteOperationTransaction（PR-010 第 9 步 · 主事务提交后）：
   IEventService svc = ServiceHelper.getService(IEventService.class);
   svc.triggerEventSubscribeJobs(
       sourceApp,
       "hbpm.positionbasedata.changed",
       message,
       variables  // 包含 basedataId 等
   );

4. 下游订阅方实现 IEventServicePlugin.handleEvent(KDBizEvent event)
```

**注意**：基础资料变更频率低，大多数场景不需要 BEC。优先考虑下游直接查表。

---

## 五、与同域其他场景的联动关系

| 场景 | 关系 | 说明 |
|---|---|---|
| `hbpm_positiontpltype`（岗位模板类型）| 同域并列关系 | 同为 hbpm 岗位域基础资料，模板类型被岗位模板引用 |
| `hbpm_reportcoreltype`（协作类型）| 同域并列关系 | 同为岗位域字典，被汇报关系等场景引用 |
| `hbpm_bd_tpl_dlg`（岗位模板对话框）| 继承关系（L3）| 本场景继承自此模板，pagekey 字段由此引入 |

---

## 五、数据库多语言表规范（TOPIC-11 · _l 结尾）

**苍穹多语言字段存储规范**：多语言文本（如 name、description 等 I18nTextField 字段）的值存储在以 `_l` 结尾的多语言表，而不是主表本身：

| 类型 | 表名后缀 | 说明 |
|---|---|---|
| 主表 | `t_hbpm_basedatalist` | 存储非多语言字段 |
| 多语言表（_l 结尾）| `t_hbpm_basedatalist_l` | 存储 name、description 等多语言字段 |
| 拆分表（_i 后缀）| `t_hbpm_basedatalist_i` | 极少使用 · 不是多语言表 |

**ISV 注意**：
- ❌ 直接 SQL 查主表的 name 字段会查不到多语言值
- ✅ 用 ORM 框架（`BusinessDataServiceHelper.load`）查询会自动 JOIN 多语言表 _l
- ❌ 把 `_i` 误认为多语言表（`_i` 是拆分表，含义不同）

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 02 沉淀）

> 本场景的业务语义补充见 [PPT02_DEEP_TRACE.md](../../docs/PPT02_DEEP_TRACE.md)
> - 16 实体清单（含历史模型类型/物理表）
> - 7 个标品定时任务（含 haos_func_orgsync_SKDP_S 同步平台）
> - 30+ OpenAPI（行政组织/岗位/职位查询保存等）
> - 5 SDK 扩展点（IAfterEffectAdminOrgExtPlugin / IAdminOrgTreeLabelExtPlugin 等）
> - 综合参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md) 总论金字塔

### 关键 SDK Helper（按 org_dev 常用）

```java
HAOSServiceHelper   // 提供新增/变更/启用/禁用组织
HBJMServiceHelper   // 提供新增/变更/启用/禁用职位
HBPMServiceHelper   // 提供新增/变更/启用/禁用岗位
```

### 业务事件订阅点

```
haos.adminOrgChangeEvent           组织变动事件
hbpm.standarpositionChangeEvent    标准岗位变动事件
hbpm.positionChangeEvent           岗位变动事件
hbjm_jobhr.change                  职位变动·生效
```

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
