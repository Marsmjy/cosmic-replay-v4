# 业务规则 + 可变性 · 行政组织快速维护

> **状态**: 🟢 基于 `knowledge/domain/org/ontology.md` + `standard_design.md` 整合
> **可变性分类**: 🔒 硬编码 / ⚙️ 配置 / 🔌 插件可改 / 📝 元数据可改

---

## 一、8 条核心不变式（来自 ontology）

这些是**组织域的系统级约束**，违反会导致数据不一致。

### INV-01 · 组织必须形成严格的树结构 🔒

- **规则**: 除根节点（集团）外，每个组织必须有且仅有一个上级 (`parentorg`)
- **可变性**: hardcoded
- **违反后果**: 循环引用异常 / 孤儿节点 / 长编码计算错乱
- **实现**: `beforeSave@haos_adminorg` 链上的 `OrgHierarchyCheckPlugin`

### INV-02 · 业务生效日期 (bsed) 支持未来 🔒

- **规则**: `bsed` 可以是未来日期，此时组织记录进入 `haos_adminorgdetailfuture` 视图
- **可变性**: hardcoded
- **业务意义**: 支持"10 月 1 日拆分子公司"这种提前规划

### INV-03 · 停用是三态（使用中/待停用/已停用）🔒

- **规则**: `enable` 字段有 3 种状态，"待停用"是"使用中 → 已停用"的中间态
- **可变性**: hardcoded
- **待停用期间**: 不允许新挂岗位、不允许调入员工，但旧数据保留

### INV-04 · 变动必须双分类（场景 + 原因）🔒

- **规则**: 组织变更必须指定 `变动场景` + `变动原因` 双分类
- **可变性**: hardcoded（分类的枚举值可配置）
- **5 种变动类型**: 新设 / 调整 / 停用 / 启用 / 修订

### INV-05 · `belongcompany` 是系统计算字段 🔒

- **规则**: 所属公司由系统根据组织类型 + parentorg 自动计算
- **可变性**: hardcoded
- **算法**: 自己是"公司"或"集团"则为自己，否则沿 parentorg 上找
- **禁止**: 手动赋值 `belongcompany`

### INV-06 · `level` / `longnumber` 是计算字段 🔒

- **规则**: `level = parentorg.level + 1`，`longnumber = parentorg.longnumber + "/" + number`
- **可变性**: hardcoded
- **预防**: 自定义插件**不要**在 `beforeSave` 修改这 2 字段

### INV-07 · 时间轴版本共用物理表 🔒

- **规则**: 同一组织的多个 `bsed` 版本，都在 `haos_adminorg` 同一张物理表里，通过 `bsed` 区分
- **可变性**: hardcoded
- **下游**: `adminorgdetail` / `adminorghis` 是同表的不同视图

### INV-08 · `number` 创建后不可修改 🔒

- **规则**: 组织编码一旦创建，**全生命周期不可改**
- **可变性**: hardcoded
- **原因**: 下游所有引用（岗位/员工/薪酬）都绑定 `number`，改了炸

---

## 二、列表展示规则

### R-L01 · 默认只显示"使用中"状态 🔌

- **可变性**: pluggable
- **实现**: `OrgListStatusFilterPlugin`
- **修改**: 覆盖 `onListQuery@haos_adminorgtablist`
- **风险**: 中（影响所有用户默认视图）

### R-L02 · 默认按 longnumber 排序 🔒

- **可变性**: hardcoded
- **原因**: 保证树形展示的父子顺序正确
- **不建议改**: 破坏树渲染

### R-L03 · 列表权限过滤（数据权限）🔌

- **可变性**: pluggable
- **标品**: `OrgListAuthFilterPlugin` 按用户管辖范围过滤
- **扩展**: 覆盖此插件以支持自定义权限模型

### R-L04 · 最大分页 20 条 ⚙️

- **可变性**: config
- **位置**: 列表配置界面
- **建议上限**: 50（再大 UI 渲染卡顿）

---

## 三、新增行政组织规则

### R-C01 · 组织编码全局唯一 🔌

- **可变性**: pluggable
- **实现**: `OrgCodeUniquePlugin`
- **扩展**: 继承并修改唯一性范围（如按租户隔离）

### R-C02 · 组织编码格式约束 🔌

- **规则**: 默认允许字母+数字+下划线，长度 ≤ 50
- **可变性**: pluggable
- **实现**: `OrgCodeValidatePlugin`

### R-C03 · 生效日期 ≤ 失效日期 🔒

- **可变性**: hardcoded

### R-C04 · 上级组织必须是"使用中"状态 🔌

- **可变性**: pluggable
- **扩展**: 覆盖可以允许挂到"待停用"下（不推荐）

### R-C05 · 根组织唯一 🔒

- **规则**: 整个租户只允许一个"集团"级根组织（无 parentorg）
- **可变性**: hardcoded

### R-C06 · 组织类型必填 🔌

- **可变性**: pluggable（可扩展类型字典）
- **默认**: 公司 / 部门 / 事业部

---

## 四、信息变更规则

### R-M01 · 组织编码不可修改 🔒

- 见 INV-08

### R-M02 · 名称变更保留历史记录 ⚙️

- **可变性**: config（审计开关）
- **实现**: 修改时自动写 `haos_adminorghis`
- **数据**: 通过 `bsed` 分版本

### R-M03 · 系统计算字段不可手改 🔒

- 见 INV-05, INV-06

### R-M04 · 修订 vs 调整 🔒

- **修订**: 纠错操作，改当前/历史信息，**不产生新版本**
- **调整**: 产生新的 `bsed` 版本
- **区分依据**: 看是否改变了业务含义

---

## 五、调整上级规则

### R-P01 · 不允许循环引用 🔒

- 见 INV-01
- **前端保护**: 选择树自动排除后代节点

### R-P02 · 层级深度 ≤ 10 📝

- **可变性**: metadata（系统参数可改）
- **默认**: 10 级
- **风险**: 超过 10 级后 UI 树渲染性能骤降

### R-P03 · 调整上级自动刷新下属 longnumber 🔒

- **可变性**: hardcoded
- **实现**: `OrgPathRecalcPlugin`（标品）
- **性能**: 下属 > 1000 时可能卡几秒

### R-P04 · 双路径：快速维护 vs 调整申请 🔒

- **快速维护** (`haos_adminorgtablist`): 直接生效，无审批（组织专员日常）
- **调整申请** (`homs_orgchgbill`): 走审批流（HR 主管复核）
- **选择依据**: 业务敏感度

### R-P05 · 目标上级必须是"使用中" 🔌

- **可变性**: pluggable
- **实现**: `OrgParentStatusCheckPlugin`

### R-P06 · 变动必须指定场景 + 原因 🔒

- 见 INV-04

---

## 六、删除规则

### R-D01 · 被引用的组织不能删除 🔒

- **可变性**: hardcoded
- **检查项**:
  - 岗位引用（hbpm_position）
  - 员工引用（hrpi_employee）
  - 薪酬档案引用（pay_salary_archive）
- **推荐**: 用"停用"代替"删除"

### R-D02 · 有下属组织不能删除 🔒

- **可变性**: hardcoded
- **先决条件**: 先处置下属（转移/删除）

---

## 七、标品设计哲学（来自 standard_design）

理解这 6 条哲学，就懂了苍穹组织域的全部设计：

### 哲学 1: 组织是企业管理的基石
下游薪酬/考勤/绩效/招聘都依赖组织结构。破坏组织树 = 全链路数据错乱。

### 哲学 2: 行政 vs 矩阵双轨
- **行政组织**: 正式管理走的轨道（公司/部门）
- **矩阵组织**: 业务协作用的轨道（项目组/虚拟团队）
- **本场景仅涉及行政组织**

### 哲学 3: 历史时间轴驱动
组织是带时间轴的版本链（4 时态：现在/过去/未来/初始）。`bsed` 是时间轴核心字段。

### 哲学 4: 变动驱动设计
所有变化必须走"变动单"（快速维护或调整申请），不允许裸改数据。

### 哲学 5: 档案 = 视图层
真相在 `haos_adminorg` + `haos_adminorgdetail`（同物理表），展示在 `homs_*`。

### 哲学 6: 系统自动计算
`level` / `longnumber` / `structlongnumber` / `belongcompany` 由系统计算，避免人为错误。

---

## 八、规则速查表

| 规则类型 | 数量 | 典型可变性 |
|---|---|---|
| 系统不变式 (INV) | 8 | 🔒 hardcoded |
| 列表展示 (R-L) | 4 | 多为 🔌 pluggable |
| 新增 (R-C) | 6 | 混合 |
| 变更 (R-M) | 4 | 多为 🔒 |
| 调整上级 (R-P) | 6 | 混合 |
| 删除 (R-D) | 2 | 🔒 hardcoded |

---

**📌 来源追溯**：
- 不变式 INV-01 ~ 08: `knowledge/domain/org/ontology.md`
- 标品哲学: `knowledge/domain/org/standard_design.md`
- 扩展规则: `knowledge/domain/org/anchors.md`

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

## chgaction 实证补充（HisModelFormCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin -->

## chgaction 实证补充（NewAdminorgDetailEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("NewAdminorgDetailEditPlugin", parseException.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin -->

## chgaction 实证补充（AdminOrgPageRightDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("AdminOrgPageRightDynamicPlugin", e.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

## chgaction 实证补充（HRRelatePageRightDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | e.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin -->

## chgaction 实证补充（AdminOrgDetailEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin`
> 跨类追踪: 29 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("AdminOrgDetailEditPlugin", exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

## chgaction 实证补充（HisModelListCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

## chgaction 实证补充（HisModelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

## chgaction 实证补充（HisModelFilterPanelListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

## chgaction 实证补充（HisModelFilterPanelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.OrgDetailList -->

## chgaction 实证补充（OrgDetailList 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.OrgDetailList`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.OrgDetailList/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("OrgDetailList", parseException.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.OrgDetailList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin -->

## chgaction 实证补充（AdminOrgDetailBUListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin -->

## chgaction 实证补充（AdminOrgDetailListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin`
> 跨类追踪: 26 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("closedCallBack", exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin -->

## chgaction 实证补充（AdminOrgDisableAndIncludeFilterListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

## chgaction 实证补充（HisModelMobileListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

## chgaction 实证补充（HisModelOPCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelAttachmentService_1` | 实体编码不能为空。 |
| `HisModelAttachmentService_2` | 数据id不能为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

## chgaction 实证补充（HisUniqueValidateOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp -->

## chgaction 实证补充（AdminOrgFastSaveOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp -->

## chgaction 实证补充（AdminOrgFastAuditOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp -->

## chgaction 实证补充（AdminOrgFastParentChangeOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp -->

## chgaction 实证补充（AdminOrgInitSaveOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp -->

## chgaction 实证补充（AdminOrgFastDisableOrgOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | msgBuilder.toString() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp -->

## chgaction 实证补充（AdminOrgFastEnableOrgOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | msgBuilder.toString() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp -->

## chgaction 实证补充（AdminOrgRootResetOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | this.reset_one_msg |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->

## chgaction 实证补充（HisBaseDataF7FastFilter 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->
