# 能力边界 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于反编译 3 类 + opkeys_index.json 44 opKey + scene_doc.json 34 字段
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、场景定位

`hrcs_dynaruleitem`（规则参数项）位于 **HR 通用服务（hrcs）/ 权限管理 / 规则参数项** 菜单 · 是 `hrcs_dynascheme`（动态授权方案）的**子配置项 / 规则字典**。dynascheme 的规则编辑器（PermFilter 控件）从本表加载可选参数 + 枚举值 · 用户配置的 condition JSON 用 paramId 引用本表行。

- **静态权限分配**：`hrcs_userrolerelat`（手动赋权）等 · 不依赖本表
- **动态权限分配（按规则批量赋权）**：`hrcs_dynascheme` (上游使用方) · `hrcs_dynaruleitem` (本场景 · 规则字典) · 强耦合

**主表单**：`hrcs_dynaruleitem`（formNumber 与 entityNumber 同名）
**ModelType**：BillFormModel + EntryEntity（**非 HisModel** · 与 dynascheme 不同）
**应用**：HR 通用服务（hrcs）
**反编译实证**：3 类（DynaRuleItemEdit / DynaItemDeleteOp / HRAdminStrictPlugin）

---

## 二、✅ verified（已覆盖能力）

### 2.1 标品支持的核心动作（44 opKey · 详见 opkeys_index.json）

| 类别 | opKey 清单 |
|---|---|
| **基础 CRUD** | `new` / `modify` / `view` / `save` / `delete` / `copy` / `refresh` |
| **工作流** | `submit` / `unsubmit` / `audit` / `unaudit` |
| **状态管理** | `enable` / `disable` |
| **导入导出 HIES** | `importdata_hr` / `show_import_record_hr` / `export_from_list_hr` / `export_from_impttpl_hr` / `export_from_expttpl_hr` / `show_export_record_hr` |
| **导入导出 (legacy)** | `importdata` / `importdetails` / `importtemplatelist` / `exportlist` / `exportlistbyselectfields` / `exportlist_expt` / `exportdetails` / `importexport_userset` |
| **导航 + 日志** | `first` / `previous` / `next` / `last` / `option` / `submitandnew` / `saveandnew` / `close` / `returndata` / `logview` / `viewonelog` / `namehistory` / `namehistoryview` / `mobtoolbarselect` / `mobtoolbarcancel` |
| **分录子操作** | `newentry`（添加枚举行）/ `deleteentry`（删除枚举行 · 引用阻断校验） |

**注**：`hrcs_dynaruleitem` 没有 dynascheme 的 `confirmchange` / `change` / `setadminrange` / `assignrecord` 等 HisModel 类操作 —— 它不是 HisModel · 不需要变更工作流。

### 2.2 标品自带规则

- **HR 域管理员准入闸**：HRAdminStrictPlugin 在 preOpenForm 拒绝非 HR 管理员（hrcs 11 表单共用 · 与 dynascheme 同此插件）
- **datatype=org 时 entitytype 自动锁定**：propertyChanged 强制写入 `haos_adminorghrf7`
- **F7 entitytype 限定 modeltype=BaseFormModel**：防止用户选到非基础资料
- **F7 sourceentitytype 限定 modeltype in (BaseFormModel, BillFormModel)**：值来源更宽
- **F7 relatruleparam 双闸过滤**：isrelatparam=false + datatype in (bd, org)（避免链式关联 + 排除 enum 类）
- **save/saveandnew 前枚举校验**：datatype=enum 时 entryentity 不能空 / 不能空行 / value 不重 / displayvalue 不重 + focusCell 定位首行错
- **deleteentry 引用阻断**：删除枚举行前调 DynaSchemeServiceHelper.queryRelDynaScheme + 解析所有方案 condition.JSON · 命中则阻断
- **delete 引用阻断**：DynaItemDelValidator 校验删除前查下游引用
- **issyspreset 预置数据保护**：FormPlugin 强制 VIEW + 隐藏 enumbar 工具栏（PR-007）
- **属性显示反查**：afterBindData 自动从 RolePermLogServiceHelper.getEntityFieldMap 反查 propKey → propName 灌回 UI
- **clearUnMustData 净化**：save 前清非当前 datatype/valsourcetype/isrelatparam 的脏字段（多次切换不会留残值）

### 2.3 标品自带集成点

- **下游 dynascheme 引用**：dynascheme.condition.conditionList[].paramId 指向本表 id · datatype=enum 时还引用 entryentity.value
- **F7 子页面 hrcs_choosefield_page**：用于选属性（relatpropkey / sourcepropkey）· 通过 customParam `paramEntityName` + `param_ifShowForDynaRule=true` 传参
- **HRBaseDataLogOp 操作日志**：所有动作（save/delete/audit/disable/enable/...）走标品操作日志 · 自动落到 hbp_dataeditlog
- **BdVersionSaveServicePlugin 名称版本**：name 字段变更走基础资料版本子表（_l 多语言）

---

## 三、🟡 likely（标品支持 · 待业务确认）

- 复制规则参数项：`copy` opKey 走平台默认基础资料复制套路（无本场景特别 ListPlugin 反编译实证）· 行为待业务侧验证
- 移动端：`mobtoolbarselect` / `mobtoolbarcancel` 暗示**支持移动端选择**（与 dynascheme 一致 · 但 dynaruleitem 移动端使用频率低）
- 自定义微服务（valsourcetype=2 + mserviceapp + mserviceclass）：用户填的 mserviceclass 如何在运行时反射调 · 反编译没找到完整链路 · 推测由 dynascheme 规则引擎调用本表配置后用反射拉值

---

## 四、⚠️ unverified（需专家确认）

- [ ] disable 一个被 dynascheme 引用的规则参数项 · 是否会自动让 dynascheme 失效？（看反编译没明确清理）
- [ ] copy 一个 datatype=enum 的参数项 · entryentity 子表是否一并复制？（待 ListPlugin 实证）
- [ ] entitytype = haos_adminorghrf7 是 HR 行政组织 F7 视图实体 · 但 datatype=org 强写死这个 · 客户如果想用其他组织实体（如 haos_org）该怎么办？（不允许 / 需配 + ISV 改 propertyChanged）
- [ ] 系统预置参数项的 datatype 列表（issyspreset=true 的有哪几个）？需 OpenAPI 列出验证
- [ ] hrcs_choosefield_page 子页面的字段过滤策略（param_ifShowForDynaRule=true 暗示对 dynarule 场景过滤特殊字段类型）

---

## 五、不覆盖（已知限制）

### 5.1 业务限制

- **不发 BEC 事件**：grep `triggerEventSubscribe / IEventService / EventServiceHelper` 在反编译 3 类全 0 命中（2026-04-28 实证）。如客户需要"规则参数项变更通知下游 OA / BI" · 只能 ISV 自建 BEC 发布方（参见 06_customization_solutions.md CS-05）
- **非 HisModel · 不支持时序变更**：标品没有 `confirmchange / change / hisversioninfo` 等操作 · 改了字段直接覆盖 · 不保留历史。如客户希望"规则参数项的变更也保留历史" · 需要 ISV 自建 HisModel 表镜像本表
- **datatype=org 写死 haos_adminorghrf7**：DynaRuleItemEdit.propertyChanged 硬编码 · 客户想用其他组织实体需 ISV 加并列 propertyChanged
- **预置参数项不能改 number**（PR-007）+ 强制 VIEW（FP_ABD3 实证）
- **datatype 字段 isvCanModify=false**（scene_doc.json L288）· 平台元数据保护 · 必填 + 不可改
- **valsourcetype 字段 isvCanModify=false**（scene_doc.json L344）· 同上
- **关键 BasedataField (entitytype / relatruleparam / sourceentitytype / mserviceapp) 都 isvCanModify=false**：ISV 不能改这些字段的元数据 · 但可以新加 ISV 字段

### 5.2 平台限制

- **OpenAPI 不支持注册 ISV 扩展元数据 add field**（PLATFORM_OPENAPI_GAPS.md P0-1 · 平台需补齐）· ISV 加字段必须走开发平台 IDEA 插件或 Web UI
- **modifyMeta add field 不支持 ISV 归属判定**（PLATFORM_OPENAPI_GAPS.md P0-2）· 跨 ISV 归属的字段会被相互覆盖
- **HRMulPositionField / HRMulAdminOrgField 不能通过 OpenAPI buildMeta 创建**（kb_cosmic_buildmeta_traps.md）· OpenAPI 视作 BasedataField 兜底
- **EmployeeField 不支持**：本场景没用 EmployeeField · ISV 想加员工字段必须走 BasedataField + hrpi_person + Java 插件

---

## 六、扩展能力总结

| 扩展场景 | 是否支持 | 详见 |
|---|---|---|
| 加自定义业务字段（如"参数项分类"） | ✅ 完全支持 | CS-01 |
| 字段联动（datatype 改了带出 entitytype 默认值 · ISV 自建联动表） | ✅ 完全支持（FormPlugin propertyChanged） | CS-02 |
| save 前置业务校验（如"特定 datatype 必填 description"） | ✅ 完全支持（自建 Validator + onAddValidators） | CS-03 |
| 删除前查下游引用 · 加自建联动反查（如自建表也引用了本规则参数项） | ✅ 完全支持（标品 DynaItemDelValidator 已查 dynascheme · ISV 加查自建表） | CS-04 |
| 变更/启用/禁用后通知下游（BEC 发布方） | ⚠ 需 ISV 自建发布方（标品 0 处发） | CS-05 |
| 枚举子表行 ID 生成（PR-005 ID 规范） | ✅ 完全支持 · 平台自动生成 + ISV 可加自建分录子表 | CS-06 |
| 列表过滤定制（按 datatype 过滤） | ✅ 完全支持（继承 HRDataBaseList · 不继承场景专属类） | CS-07 |
| 替换 datatype 联动 entitytype 的硬编码（haos_adminorghrf7） | ⚠ 平台硬编码 · ISV 想替换需 propertyChanged 并列拦截 | - |
| 把 dynaruleitem 改成 HisModel | ❌ 不支持 · 平台模型固定 · 改了直接挂 | - |

---

## 七、版本兼容性

- 反编译版本：HRMP **8.0**（hrmp-hrcs-formplugin-1.0.jar / hrmp-hrcs-opplugin-1.0.jar）
- 苍穹平台：8.0+
- 待验证：2024R1 / 2025 版本是否有 opKey/字段差异（建议每年 Q1 重跑 probe）
- 与 dynascheme 同一 jar · 升级时一起更（标品更新 jar 时 DynaRuleItemEdit / DynaItemDeleteOp 都会变）

---

## 八、与其他场景的关联

| 关联场景 | 关系 | 备注 |
|---|---|---|
| `hrcs_dynascheme` | **下游强耦合**（核心） | dynascheme.condition.JSON 引用本表 · 本表 deleteentry / delete 都查它做引用阻断 |
| `bos_entityobject` | 上游引用（entitytype / sourceentitytype） | 平台基础资料元数据实体（所有元数据形式） |
| `hrcs_dynaruleitem` (自身) | 上游引用（relatruleparam） | 自引用 · 关联参数项主参数 |
| `bos_devportal_bizapp` | 上游引用（mserviceapp） | 微服务所在应用 |
| `haos_adminorghrf7` | 上游引用（datatype=org 时强制写入 entitytype） | HR 行政组织 F7 视图实体 |
| `hrcs_choosefield_page` | 子页面 | 选属性时弹出（relatpropkey / sourcepropkey） |
| `bos_user` | 上游引用（creator / modifier / disabler） | 平台用户 |

详见 `11_upstream_downstream_logic.md`。
