# 能力边界 · 维度管理 (hrcs_dimension)

> **状态**: 🟢 基于反编译 4 类（DimensionNewEdit / DimensionList / DimensionDeleteOp / HRAdminStrictPlugin）+ opkeys_index.json 44 opKey + scene_doc.json 33 字段
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、场景定位

`hrcs_dimension`（维度管理）位于 **HR 通用服务（hrcs）/ 权限管理 / 维度管理** 菜单 · 是 HR 权限体系最底层的"权限切分维度配置"基础资料。维度（Dimension）是苍穹 HR 权限引擎的基础概念：

> "角色可以按某个维度（dimension）切分访问权限范围"

**维度 4 种 datasource 类型**（核心区分）：

| datasource | 含义 | 典型业务用途 | 配套字段 |
|---|---|---|---|
| `enum` | 枚举型 | 自定义枚举值切分（如"职级 P5/P6/P7"作为枚举切分维度） | entry 子表（枚举值/枚举名称/顺序/备注） |
| `basedata` | 基础资料型 | 按业务对象切分（如"按部门切分"·entitytype=部门） | entitytype + showtype |
| `hrbu` | 职能类型 | 按 HR 职能业务单元切分 | hrbu（职能类型基础资料） |
| `orgteam` | 组织团队 | 按行政组织 + 组织分类切分 | entitytype=haos_adminorgdetail · hrbu + org_classify |

**主表单**：`hrcs_dimension`（formNumber 与 entityNumber 同名）
**ModelType**：BillFormModel（含审核流但**无 HisModel**）
**菜单 ID**：见 `probe_snapshot.json.menu`
**应用**：HR 通用服务（hrcs · cloud=HR / domain=HR基础服务/HR权限管理）

⚠️ 与 dynascheme 最大不同：dynascheme 是 BillFormModel + HisModel · dimension 是**纯 BillFormModel 单据 · 无时序版本**（grep `iscurrentversion / HisModel / boid` 全 0 命中）。

---

## 二、✅ verified（已覆盖能力）

### 2.1 标品支持的核心动作（44 opKey · 详见 opkeys_index.json）

| 类别 | opKey 清单 |
|---|---|
| **基础 CRUD** | `new` / `modify` / `view` / `save` / `delete` / `copy` / `refresh` |
| **工作流** | `submit` / `unsubmit` / `audit` / `unaudit` |
| **状态管理** | `enable` / `disable` |
| **导航 + 日志** | `first` / `previous` / `next` / `last` / `option` / `submitandnew` / `saveandnew` / `close` / `returndata` / `logview` / `viewonelog` / `namehistory` / `namehistoryview` / `mobtoolbarselect` / `mobtoolbarcancel` |
| **导入导出 HIES** | `importdata_hr` / `show_import_record_hr` / `export_from_list_hr` / `export_from_impttpl_hr` / `export_from_expttpl_hr` / `show_export_record_hr` |
| **导入导出 (legacy)** | `importdata` / `importdetails` / `importtemplatelist` / `exportlist` / `exportlistbyselectfields` / `exportlist_expt` / `exportdetails` / `importexport_userset` |
| **分录操作** | `newentry` / `deleteentry`（基础分录新增/删除 · 用于 entry 子表） |

⚠️ **dimension 没有 dynascheme 的 confirmchange/change/setadminrange/assignrecord 等 HisModel + 业务专属 opKey** · 比 dynascheme 简化很多。

### 2.2 标品自带规则

- **HR 域管理员准入闸**：HRAdminStrictPlugin 在 preOpenForm 拒绝非 HR 管理员（hrcs 11 表单共用 · 反编译实证 hrcs_dimension/HRAdminStrictPlugin.java L29-L52）
- **datasource 4 路联动**：DimensionNewEdit.showEnumCtrl 切换 enum/basedata/hrbu/orgteam · 自动重置 entitytype/hrbu/org_classify 必填和可见性
- **entitytype F7 双闸**：F7 entitytype 时强制 `modeltype=BaseFormModel` 过滤（DimensionNewEdit.beforeF7Select L364-L372）
- **entitytype 业务对象校验**：basedata 类型选了业务对象 · 业务对象必须有 number+name 字段（DimensionNewEdit.limitBasedataType L236-L257）· 没有就清空 entitytype 并提示
- **showtype=tree 树形限制**：basedata + tree 显示 · 业务对象必须继承 hbp_bd_treetpl_all（DimensionNewEdit.handleShowTypeForEntityType L205-L215 + checkEntitytype L339-L347）
- **showtype=checkbox 不许 basedata**：basedata + checkbox 直接拒绝（L335-L338）
- **enum 必有枚举值**：datasource=enum 时 entry 不能空（L349-L353）
- **enum 枚举名称去重**：entry.displayvalue 必须唯一（L354-L359）
- **enum 修改二次确认**：EDIT 状态 · datasource=enum · entry value 跟 DB 不一致 · 弹"调整枚举值会影响角色维度·确定修改吗？"（DimensionNewEdit.checkEnumChange L308-L324）
- **enum 已被引用值锁定**：维度被角色引用时 · entry value 字段全部 setEnable(false)（DimensionNewEdit.showEnumCtrl L162-L168）
- **datasource=hrbu save 时强制 entitytype=bos_org**：DimensionNewEdit.beforeDoOperation L278-L280
- **datasource=orgteam 强制 entitytype=haos_adminorgdetail**：DimensionNewEdit.showEnumCtrl L196
- **modifytime 自动重置**：save 时 setValue(modifytime, new Date())（L277）
- **删除自定义校验**：DimensionDeleteOp 注册 DimensionDeleteValidator（具体校验细节反编译只看到注册 · 推断为反查下游引用）
- **list 禁用二次确认**：DimensionList.beforeItemClick 拦截 tbldisable 弹"禁用维度后·不允许在'业务对象维度映射'中使用·已有的角色维度的数据权限不受影响"（L31-L34）

### 2.3 标品自带集成点

- **跳转角色清单子页面**：表单点 `refrole` 工具按钮 · DimensionNewEdit.beforeItemClick L259-L270 跳 hrcs_refdetails 子页面（带 dimension.id 参数）查反查清单
- **ctrlentry 虚拟分录反查**：afterBindData 反查 `hrcs_entityctrl.entryentity` 表（按 dimensionId 过滤）· 灌进 ctrlentry 显示

---

## 三、🟡 likely（标品支持 · 待业务确认）

- **复制 dimension**：copy opKey 仅挂 HRBaseDataStatusOp + HRBaseDataLogOp（无场景专属插件）· 推测会复制主表 + entry 子表 · 但 issyspreset 应该重置为 false
- **importdata_hr 导入维度**：HRBaseDataImportEdit 支持 · 推测可导入 number/name/datasource/entitytype/showtype 等基础字段 · entry 子表导入待确认
- **F7 entitytype 跨业务对象选**：beforeF7Select 强制 modeltype=BaseFormModel · 但用户能否选 hrpi_person 等 HR 业务对象 · 取决于这些业务对象是不是 BaseFormModel
- **datasource=enum 删除已被引用枚举值**：标品锁了 entry.value 不允许改 · 但能否删除整行（deleteentry opKey）· 反编译没看到拦截 · 实测确认
- **同 entitytype 多维度并存**：理论上一个业务对象可以建多个 dimension 实例（如部门维度 + 部门简称维度）· 但 entityctrl 关联唯一性待确认

---

## 四、⚠️ unverified（需专家确认）

- [ ] disable 一个 dimension 后 · `hrcs_userrolerelat` 已分配的"通过此维度切分的角色"是否立即清理？（标品提示文案是"已有的角色维度的数据权限不受影响"· 即不清理 · 但需实测）
- [ ] delete 一个 dimension 时 · DimensionDeleteValidator 反查的下游是哪些表？（反编译只看到注册 · validate 方法没读到）
- [ ] 维度被 hrcs_dynascheme.condition 用作规则参数时 · F7 entitytype 上"业务对象的字段集"是否会因为 dimension 改了 datasource 突变？
- [ ] dimension 是否有"系统预置维度"（issyspreset=true 行）？反编译没看到 ISV 怎么查这类预置行
- [ ] 一个 dimension 同时被 N 个角色 + M 个 dynascheme 引用时 · 改 entry value 的影响面如何评估？
- [ ] datasource=hrbu 类型的 dimension 在切换"职能类型"基础资料后 · 已被引用维度怎么迁移？

---

## 五、不覆盖（已知限制）

### 5.1 业务限制

- **不发 BEC 事件**：`grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" knowledge/_sdk_audit/_decompiled/scenarios/hrcs_dimension/` **0 处命中**（详见 06_customization_solutions.md CS-05）。如客户需要"维度变更通知下游" · 只能 ISV 自建 BEC 发布方
- **dimension 不支持 confirmchange / 业务变更**：dimension 是普通 BillFormModel · 不是 HisModel · 改了就改 · 没有"审核-变更-双写"概念
- **没有 setadminrange · 维度不分管理员组**：dimension 是平台级基础资料 · 跟 admingroup 没有强关联（与 dynascheme 不同）
- **datasource 字段不可改**：`scene_doc.json` `datasource.isvCanModify=false` + 该字段是 dimension 的"模式开关" · 标品锁死
- **showtype 字段不可改**：`scene_doc.json` `showtype.isvCanModify=false`
- **issyspreset 标记的预置维度不能改 number**（PR-007）
- **enum 维度被角色引用后 · entry.value 被锁定**：UI 层 setEnable(false) · 后端虽未拦截但是约定俗成

### 5.2 平台限制

- **OpenAPI 不支持注册 ISV 扩展元数据 add field**（PLATFORM_OPENAPI_GAPS.md P0-1 · 平台需补齐）· ISV 加字段必须走开发平台 IDEA 插件或 Web UI
- **modifyMeta add field 不支持 ISV 归属判定**（PLATFORM_OPENAPI_GAPS.md P0-2）· 跨 ISV 归属的字段会被相互覆盖
- **HRMulPositionField / HRMulAdminOrgField 不能通过 OpenAPI buildMeta 创建**（kb_cosmic_buildmeta_traps.md）· OpenAPI 视作 BasedataField 兜底
- **EmployeeField 不支持**：HR SDK 扩展类型 OpenAPI 不识 · 想加员工字段必须走 hrpi_person + Java 插件而非 EmployeeField

---

## 六、扩展能力总结

| 扩展场景 | 是否支持 | 详见 |
|---|---|---|
| 加自定义业务字段（如"维度备注分类"） | ✅ 完全支持 | CS-01 |
| 字段联动（datasource 改了带出默认值 · ISV 自建版本） | ✅ 完全支持（FormPlugin propertyChanged） | CS-02 |
| save / delete / disable 前置业务校验 | ✅ 完全支持（自建 Validator + onAddValidators） | CS-03 |
| 删除/禁用前查下游引用（dimension 被 dynascheme/datarule/role 引用） | ✅ 完全支持（自建 Validator + 反查 hrcs_dynascheme.condition + hrcs_datarule + hrcs_entityctrl） | CS-04 |
| 变更/审核/禁用后通知下游（BEC 发布方） | ⚠ 需 ISV 自建发布方（标品没发） | CS-05 |
| 分录子表 entry 行 id / 编号生成定制 | ✅ 完全支持（用 PR-005 ID.genLongId） | CS-06 |
| 列表过滤定制（按 datasource/admingroup 过滤） | ✅ 完全支持（继承 HRDataBaseList · 不继承 DimensionList） | CS-07 |
| 替换 EntityCtrlServiceHelper.getRoles 反查（自定义角色清单显示） | ❌ 不推荐 · 平台标品 helper · 改了风险大 | - |
| 改 datasource 4 路联动 / 加 datasource 选项 | ❌ 不推荐 · DimensionNewEdit 5 路联动逻辑 · 改了和 ctrlentry/entityctrl 整套机制冲突 | - |
| 修改主表 datasource/showtype/issyspreset 字段 | ❌ 不允许（标品锁死 isvCanModify=false） | - |

---

## 七、版本兼容性

- 反编译版本：HRMP **8.0**（hrmp-hrcs-formplugin-1.0.jar / hrmp-hrcs-opplugin-1.0.jar）
- 苍穹平台：8.0+
- 待验证：2024R1 / 2025 版本是否有 opKey/字段差异（建议每年 Q1 重跑 probe）
- 父模板：`hbp_bd_tpl_all`（HR 基础资料全页面模板 · 与 hrcs_dynascheme 共用部分继承层）

---

## 八、与 dynascheme 的并列关系（同应用 · 异机制）

`hrcs_dimension` 跟 `hrcs_dynascheme` 都在 hrcs 应用 · 都跟权限管理强相关 · 但**机制完全不同**：

| 维度 | hrcs_dimension | hrcs_dynascheme |
|---|---|---|
| 角色 | 维度配置基础资料（被引用） | 动态授权方案（运营工具 · 引用方） |
| 字段数 | 33 | 56 |
| opKey 数 | 44 | 60 |
| ModelType | BillFormModel | BillFormModel |
| HisModel 时序 | ❌ 无 | ✅ 有（boid/iscurrentversion/sourcevid） |
| 反编译类数 | 4 | 7 |
| 业务变更 confirmchange | ❌ 无 | ✅ 有（双写 boid + bgVid） |
| BEC 事件 | ❌ 标品 0 处发布 | ❌ 标品 0 处发布 |
| 主下游 | hrcs_entityctrl / hrcs_dynascheme.condition / hrcs_datarule / hrcs_dynaschemerole | hrcs_userrolerelat（sourcetype=4）/ hrcs_dynaschemerange / hrcs_dynaschemerole |
| 主表 ID 引用方式 | 直接 id（无 boid） | 用 boid（业务维度 · 跨版本不变） |

**Claude 写代码时**：dimension 比 dynascheme 简单 · 没有 HisModel 套路 · 但 datasource 4 路联动比 dynascheme.authaction 切换复杂得多。
