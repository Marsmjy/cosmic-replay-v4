# 上下游联动 · 组织历史查询（haos_adminorghis）

> **状态**：基于反编译 + refentity_reverse + 时序场景关联分析
> **confidence**：verified
> **审计时间**：2026-04-27

---

## 1. 整体上下游图

```
┌─────────────────────────────────────────────────────────────┐
│                       上游（用户进入路径）                      │
└─────────────────────────────────────────────────────────────┘

  ┌─ 路径 A：菜单直达"行政组织维护 → 组织历史查询"
  │
  ├─ 路径 B：admin_org_quick_maintenance 列表 → "查看历史"按钮（CS-06）
  │
  ├─ 路径 C：F7 选历史版本（其他场景作为引用源）
  │   ├─ hrpi_empjobrel.adminorg F7 选组织
  │   ├─ hbpm_position.adminorg F7
  │   └─ ...
  │
  └─ 路径 D：HIES 数据迁移导入 → chargepersonimpo_hr
                       │
                       ▼
            ┌─────────────────────────┐
            │ haos_adminorghis   │ ← 本场景
            │（VERSION_LIST_PAGE 模式） │
            │  iscurrentversion=false │
            └─────────────────────────┘
                       │
                       ▼
              查询 t_haos_adminorg 历史
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                     下游（数据 / 联动方向）                     │
└─────────────────────────────────────────────────────────────┘

  ├─ 物理表 t_haos_adminorg（共用 · 双场景）
  │   └─ 写：仅 his_save / chargepersonimpo_hr 写
  │   └─ 读：本场景查 / admin_org_quick_maintenance 查
  │
  ├─ 多语言表 t_haos_adminorg_l
  │
  ├─ 下游 hrpi_* 引用（17 条 · 通过 boid 维度引用 · 不级联）
  │   ├─ hrpi_empjobrel.adminorg
  │   ├─ hrpi_empposorgrel.adminorg
  │   ├─ hrpi_rotationinfo.adminorg
  │   └─ hrpi_dispatchinfo.adminorg ...
  │
  ├─ refentity_reverse 反查目标
  │
  └─ BEC 业务事件中心
      └─ 本场景**不发** · 由 admin_org_quick_maintenance 的 confirmchange 链发
```

---

## 2. 上游：本场景的进入路径

### 2.1 路径 A：菜单直达

| 触发 | 实现 |
|---|---|
| 用户点菜单"组织历史查询"（menuId=1443449111746507776）| 平台路由打开 formNumber=`haos_adminorghis` 的列表 |
| HisModelListCommonPlugin.preOpenForm + AdminOrgHisListPlugin.preOpenForm | 设 customParam |
| HisModelListCommonPlugin.setFilter | 强制 iscurrentversion=false |

### 2.2 路径 B：从 admin_org_quick_maintenance 跳转（CS-06）

| 触发 | 实现 |
|---|---|
| 用户在 admin_org_quick 列表点 ISV 自加按钮"查看全历史"| ISV ListPlugin.itemClick |
| 拿当前焦点 boid · showForm(formId=haos_adminorghis, customParam.boid=...) | 跳本场景 |
| 本场景 ISV setFilter 读 customParam.boid · add QFilter | 列表过滤到该 boid |
| 标品 setFilter 强制 iscurrentversion=false | 显示该 boid 全部历史版本 |

### 2.3 路径 C：F7 选历史版本

| 触发 | 实现 |
|---|---|
| 其他场景的 BasedataField → haos_adminorghrf7 触发 F7 | 平台打开 F7 表单 |
| HisModelF7ListPlugin 接管 | 5 层 QFilter 叠加（详见 05_data_flow.md §3）|
| F7 显示按 effdate 区间筛的版本 | 用户选一条 → 返回 boid 给调用方 |

### 2.4 路径 D：HIES 数据迁移导入

| 触发 | 实现 |
|---|---|
| HR 走 HIES 导入向导 · 选模板 · 上传 Excel | 标品 ImportPlugin |
| 标品后台分批 · 每批调 OperationServiceHelper.executeOperate("his_save", ...) | 触发 AdminOrgInitSaveOp 链 |
| 10 个 OrgHis*Validator 校验 + 写 t_haos_adminorg | 单条事务 · 失败回滚 |
| 失败行进入 HIES 错误日志 | 业务方修正后重导 |

---

## 3. 下游：本场景影响哪些模块

### 3.1 同表共享：admin_org_quick_maintenance

物理表 t_haos_adminorg 共用 · 详见 03_model_design.md §2 + 08_impact_analysis.md §1。

- 本场景 his_save 写入 → admin_org_quick_maintenance 列表立即可见（如该数据 iscurrentversion=true · 但 his_save 不会改 iscurrentversion · 所以一般不影响）
- 本场景 ISV 加字段（`${ISV_FLAG}_xxx` · 挂 haos_adminorg 主层）→ admin_org_quick_maintenance 自动可见
- 本场景 ISV 改 setFilter / list 排序 → 仅本场景视图层 · 不影响 admin_org_quick

### 3.2 hrpi 域（员工关系）· refentity_reverse 17 条引用

通过 `haos_adminorghrf7` 视图引用 boid（PR-009）· 来自 `knowledge/workbench/_indexes/refentity_reverse.json`：

| 下游表 | 字段 | 业务含义 | 时序 |
|---|---|---|---|
| `hrpi_empjobrel.adminorg` | HRAdminOrgField | 员工任职所在行政组织 | HisModel · 员工版本时序 |
| `hrpi_empposorgrel.adminorg` | HRAdminOrgField | 人岗组关系 | HisModel |
| `hrpi_rotationinfo.adminorg` | HRAdminOrgField | 轮岗信息 | HisModel |
| `hrpi_dispatchinfo.adminorg` | HRAdminOrgField | 派遣信息 | HisModel |
| ...（共 17 条）| | | |

**关键认知**（PR-009）：
- 下游存的是 boid（业务对象 ID）· 不是 id（版本 id）
- 当组织变化（admin_org_quick_maintenance 走 confirmchange）· 派生新版本 hisversion+1 · 老版本 iscurrentversion=false · 但 boid 不变
- **所以下游 hrpi_* 引用不需要级联更新** · PR-009 设计意图

### 3.3 hbpm 域（岗位）

| 下游表 | 字段 | 业务含义 |
|---|---|---|
| hbpm_position.adminorg | HRAdminOrgField | 岗位所在组织 |
| hbpm_positionhr.adminorg | HRAdminOrgField | HR 视图岗位 |

同样按 boid 引用 · 不级联。

### 3.4 BEC 业务事件中心

**本场景不发任何 BEC 事件** · 反编译 grep 0 处 `triggerEventSubscribe / IEventService / EventServiceHelper`。

需要订阅组织变更事件的 ISV：
- 走 admin_org_quick_maintenance CS-04 订阅 confirmchange 链 afterTransDoOp 阶段发的事件（标品 AdminChangeMsgService.handleChangeMsg → EventServiceHelper.triggerEventSubscribeJobs）
- 不要重复发本场景的事件 · 没必要

---

## 4. 关联时序场景对比表

本场景属于"HR 时序查询场景簇" · 与下列场景同性质：

| 场景 | 物理表共享 | 时序模型 | 历史查询入口 | 写入入口 |
|---|---|---|---|---|
| **haos_adminorghis**（本场景）| `t_haos_adminorg` | NO_INTERRUPTION_NO_OVERLAP | `haos_adminorghis` | his_save / chargepersonimpo_hr |
| haos_structure_maintenance | `t_haos_structproject` | -（非时序）| - | - |
| hbpm_position_maintenance | `t_hbpm_position` | NO_INTERRUPTION_NO_OVERLAP | （`hbpm_positionhis` 类似入口）| - |
| hbjm_jobhr_maintenance | `t_hbjm_jobhr` | NO_INTERRUPTION_NO_OVERLAP | （`hbjm_jobhrhis`）| - |
| hrpi_empjobrel | `t_hrpi_empjobrel` | NO_INTERRUPTION_NO_OVERLAP | （`hrpi_empjobrelhis`）| - |

> **共性**：所有 HR 时序资料都遵循同一套 hbp_histimeseqtpl 模板 · 时序字段（boid / iscurrentversion / hisversion / sourcevid / firstbsed / bsed / bsled）语义一致 · 历史查询入口都用 BaseFormModel 视图 + iscurrentversion=false 过滤实现。

---

## 5. 跨场景协作关系矩阵

| 协作场景 | 上游 / 下游 | 数据方向 | 触发机制 |
|---|---|---|---|
| admin_org_quick_maintenance | 上游（用户从那里跳来）+ 下游（数据共享物理表）| 双向 | 用户菜单切换 / CS-06 跨场景跳转按钮 |
| 各 hrpi_* 任职 | 下游（引用 boid）| 单向（无级联）| F7 选组织时引用 boid · 不级联 |
| 各 hbpm_* 岗位 | 下游（引用 boid）| 单向 | 同上 |
| HIES 导入向导 | 上游 | 单向（导入 → his_save）| 用户走 HIES 流程 |
| BEC 订阅方（ISV 自建）| 下游 | 单向（订阅 admin_org_quick 的事件）| **不订阅本场景**（本场景不发）|
| haos_structure_maintenance | 无直接关联 | - | - |

---

## 6. 上下游依赖图（关键耦合点）

```
hbp_histimeseqtpl (HR 时序模板基类 · 28 字段时序模型)
       │
       └─ haos_adminorg (主层 · 业务字段)
              │
              ├─ haos_adminorgdetail (admin_org_quick_maintenance)
              │      │
              │      ├─ save / confirmchange (写当前版本)
              │      │  └─ afterTransDoOp 发 BEC 组织变更事件
              │      │     └─ ISV 可订阅（admin_org_quick_maintenance CS-04）
              │      │
              │      └─ list 列表（iscurrentversion=true）
              │
              └─ haos_adminorghis (本场景)
                     │
                     ├─ list 列表（iscurrentversion=false）⭐
                     ├─ his_save (历史补录 · 不发 BEC)
                     ├─ revise (派生新版本 · 不发 BEC)
                     ├─ versionchangecompare (版本对比)
                     └─ chargepersonimpo_hr (HIES 导入)
                              │
                              ▼
                     t_haos_adminorg + t_haos_adminorg_l (物理表 · 双场景共享)
```

---

## 7. ISV 扩展协作建议

| 协作目的 | 推荐 CS | 影响范围 |
|---|---|---|
| 让用户从 admin_org_quick 一键看全历史 | CS-06（跨场景跳转）| 双场景 |
| 让历史查询列表按 ISV 字段筛 | CS-01（自定义筛选）| 仅本场景 |
| 让历史补录前加更严的校验 | CS-04（OP onAddValidators）| 仅 his_save 路径 |
| 全历史对比页（一次看 N 个版本）| CS-03（自建动态表单）| 独立 form |
| 跨域（hrpi）通知员工组织变了 | admin_org_quick_maintenance CS-04 BEC 订阅 | 本场景不参与 |

---

## 8. 总结：上下游 5 条核心认知

1. **物理表共享**：本场景 t_haos_adminorg 与 admin_org_quick_maintenance 共用 · 是最重要的协作认知
2. **boid 是引用维度**（PR-009）· 下游 hrpi / hbpm 引用 boid · 跨版本不级联
3. **本场景不发 BEC** · 想订阅组织变更事件去 admin_org_quick_maintenance 那条链订阅
4. **HIES 是写入主路径**（除 his_save UI 外）· ISV 校验扩展要考虑 HIES 导入兼容性
5. **跨场景跳转是协作模式**（CS-06）· 不要在本场景内强改 setFilter 实现"看当前"

---

## 9. 关联文档

- `01_capability_boundary.md` · §6 admin_org_quick_maintenance 对偶清单
- `08_impact_analysis.md` · 共用物理表的影响分析
- `06_customization_solutions.md` · CS-06 跨场景跳转
- `knowledge/scenarios/admin_org_quick_maintenance/06_customization_solutions.md CS-04` · BEC 订阅方
- `knowledge/workbench/_indexes/refentity_reverse.json` · 反向引用图
- `knowledge/_shared/platform_rules.json` · PR-008 / PR-009 / PR-011

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json` · 更新时间 2026-04-29
> 本 form（`haos_adminorghis`，所属 组织发展云）引用了其他云的 **3** 个底座实体：

### ⬆️ HR 基础服务云（`hr_hrmp`）3 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `industrytype` | 行业类别 | BasedataField | `hbss_industrytype` | [hbss_position_dict](../hbss_position_dict/) |
| `corporateorg` | 法律实体 | BasedataField | `hbss_lawentity` | [hbss_law_entity](../hbss_law_entity/) |
| `workplace` | 工作地 | BasedataField | `hbss_workplace` | [hbss_supplier](../hbss_supplier/) |

> ⚠️ ISV 扩展须知（ADR-009）：
> - 上游底座实体是**标品字典**，原则上不可改字段（参各上游场景的 06_customization_solutions.md）
> - 引用方式（fieldType / refEntity）由本 form 元数据控制；本 form 改 ref 字段值用 `setValue` 即可
> - 修改前必须读对应上游场景的 11_upstream_downstream_logic.md，确认上游 ISV 扩展规则

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
