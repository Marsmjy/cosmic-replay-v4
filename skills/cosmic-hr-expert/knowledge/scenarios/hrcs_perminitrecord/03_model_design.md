# 模型设计 · 权限初始化 (hrcs_perminitrecord)

> **状态**: 🟢 基于 `scene_doc.json` (137 字段实抓) + `_auto_inherit_chain.md` + `form_lifecycle_rules.json` (4 反编译类)
> **confidence**: verified
> **数据源**: OpenAPI `getFormSchema` + CFR 反编译 PermInitRecordEdit/PermInitRecordList/PermInitRecordDeleteOp/HRAdminStrictPlugin (2026-04-28)

---

## ⚡ Developer Quick Reference

| 问 | 答 |
|---|---|
| modelType？ | BillFormModel（非 BaseFormModel · 多分录复杂单据） |
| HisModel？ | ❌ 不带时序版本 |
| 继承链？ | L0 `1942c188000065ac` → L1 `hbp_bd_tpl_all` → hrcs_perminitrecord |
| 物理表数量？ | 21 张 |
| entry entities？ | 15 个（user 侧 5 + role 侧 8 + error 侧 4 · 含 subentry 2） |
| 字段总数？ | 137 |
| inittype 对应哪组 entry？ | userrole → userdimentry/userdataruleentry/userbdentry/userfieldentry; role → rolebaseentry/rolefuncentry/roledimentry/roledataentry/rolefieldentry |

---

## ⭐ 关键业务事实 · 双模单据 + 15 分录 + 21 表 · 无时序

`hrcs_perminitrecord` 是 HR 通用服务（hrcs）域里**权限初始化**的批量导入工具。它是苍穹里少见的"一份主单有 15 个分录实体"的超复杂 BillFormModel——因为 userrole（用户导入）和 role（角色导入）两套完全不同的分录体系共用一张主表。

它**不是** HisModel 时序基础资料（grep `iscurrentversion|HisModel|boid` 反编译四类 0 命中），是常规 BillFormModel + 15 entry/subentry entities 的**超多层结构**。

---

## 一、继承链 · BillFormModel + hbp_bd_tpl_all（非 HisModel）

### 1.1 InheritPath（OpenAPI 实抓）

| 层级 | formNumber | 表单名 | formId |
|---|---|---|---|
| L0 | `?` | （bos_basetpl 平台基模板） | `1942c188000065ac` |
| L1 | `hbp_bd_tpl_all` | HR基础资料全页面模板 | `2+QE4JA9QV27` |

### 1.2 ModelType 实证

`probe_snapshot.json` `header` 显示 `modelType = "BillFormModel"`。

- ❌ **不带 HisModel 时序模板**：grep `iscurrentversion / HisModel / boid` 反编译四类（PermInitRecordEdit/PermInitRecordList/PermInitRecordDeleteOp/HRAdminStrictPlugin）+ scene_doc.json 全部 0 命中
- ❌ **不带审批工作流**：opKey 列表里 submit/audit/unaudit 虽有注册但 thin
- ✅ **典型 BillFormModel + 15 entry entities**：单据形态（save/delete/copy/saveandnew）+ 15 个分录实体（userrole 模式用其中 4 个 user entry + 1 个 subentry，role 模式用其中 5 个 role entry + 4 个 role error entry + 1 个 subentry）

---

## 二、字段层级分类

苍穹元数据字段分 4 层（scene_doc.json `layer` 字段标注）：

| 层级 | 来源 | 典型字段 | ISV 能否改 |
|---|---|---|---|
| **L0** 系统级 | bos_basetpl | `id` / `creator` / `modifier` / `createtime` / `modifytime` / `masterid` | 🔒 不改（破坏全系统） |
| **L1** 业务通用 | hbp_bd_tpl_all + HR 父模板 | `number` / `name` / `status` / `enable` / `simplename` / `description` / `index` / `issyspreset` / `disabler` / `disabledate` / `initdatasource` / `orinumber` / `oristatus` / `oriname` | 🔒 / ⚠️ 多数不改（红区标记） |
| **L2** 时序模型 | hbp_histimeseqtpl 父模板 | （**本场景无** · 非 HisModel） | — |
| **L3** 业务字段 | hrcs_perminitrecord 自身 | `inittype` / `initnumber` / `includesub` / `dealstatus` + 所有 15 个 entry entity 字段 | ⚠️ 谨慎改（涉及双模路由） |

### 关键认知

- **本场景没有 HisModel 概念**：没有 `boid / id / iscurrentversion / sourcevid` 这套，所以查询时**不需要带 `iscurrentversion=true` 过滤**（PR-008 不适用）
- **inittype 是双模路由键**：`userrole` vs `role` 两个值决定哪组 entry entity 被渲染/写入（FP_BBD1 实证）
- **dealstatus 是两态**：0=未完成（草稿）/ 1=已完成 · 不是标准 `BillStatusField` 的多态
- **includesub 是必填字段**（唯一的 L3 必填字段）：数据范围策略（是否包含下级）
- **数据来源 `initdatasource`**：系统预置 vs 用户自建 · 红区字段 · ISV 不要 setValue

---

## 三、主表字段清单（scene_doc.json 实抓 · 共 23 个字段）

### 3.1 主表系统字段（L0 · 平台维护 · 红区）

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 物理列 |
|---|---|---|---|---|---|
| `creator` | CreaterField | 创建人 | ❌ | 🔒 | t_hrcs_pinitrecord.fcreatorid → bos_user |
| `createtime` | CreateDateField | 创建时间 | ❌ | 🔒 | t_hrcs_pinitrecord.fcreatetime |
| `modifier` | ModifierField | 修改人 | ❌ | 🔒 | t_hrcs_pinitrecord.fmodifierid → bos_user |
| `modifytime` | ModifyDateField | 修改时间 | ❌ | 🔒 | t_hrcs_pinitrecord.fmodifytime |
| `masterid` | MasterIdField | 主数据内码 | ❌ | 🔒 | t_hrcs_pinitrecord.fmasterid |

### 3.2 主表 L1 通用字段

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 物理列 |
|---|---|---|---|---|---|
| `number` | TextField | 编码 | ❌ | ✅ | t_hrcs_pinitrecord.fnumber |
| `name` | MuliLangTextField | 任务名称 | ✅ (业务级 · 元数据非必填) | ✅ | t_hrcs_pinitrecord.fname |
| `status` | BillStatusField | 数据状态 | ❌ | ⚠️ 黄区 | t_hrcs_pinitrecord.fstatus |
| `enable` | BillStatusField | 使用状态 | ❌ | ⚠️ 黄区 | t_hrcs_pinitrecord.fenable |
| `simplename` | MuliLangTextField | 简称 | ❌ | ✅ | t_hrcs_pinitrecord.fsimplename |
| `description` | MuliLangTextField | 描述 | ❌ | ✅ | t_hrcs_pinitrecord.fdescription |
| `index` | IntegerField | 排序号 | ❌ | ✅ | t_hrcs_pinitrecord.findex |
| `issyspreset` | CheckBoxField | 系统预置 | ❌ | 🔒 红区 | t_hrcs_pinitrecord.fissyspreset |
| `disabler` | UserField | 禁用人 | ❌ | 🔒 红区 | t_hrcs_pinitrecord.FDisablerID → bos_user |
| `disabledate` | DateTimeField | 禁用时间 | ❌ | 🔒 红区 | t_hrcs_pinitrecord.FDisableDate |
| `initdatasource` | ComboField | 数据来源 | ❌ | 🔒 红区 | t_hrcs_pinitrecord.finitdatasource |
| `orinumber` | TextField | 出厂编码 | ❌ | 🔒 红区 | t_hrcs_pinitrecord.forinumber |
| `oristatus` | ComboField | 出厂数据编辑状态 | ❌ | 🔒 红区 | t_hrcs_pinitrecord.foristatus |
| `oriname` | MuliLangTextField | 出厂名称 | ❌ | 🔒 红区 | t_hrcs_pinitrecord.foriname |

### 3.3 主表 L3 业务字段

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 物理列 |
|---|---|---|---|---|---|
| **`inittype`** | **ComboField** | **类型** ⭐ (userrole / role) | ❌ | ✅ | t_hrcs_pinitrecord.finittype |
| `initnumber` | IntegerField | 初始化数量 | ❌ | ✅ | t_hrcs_pinitrecord.finitnumber |
| **`includesub`** | **ComboField** | **数据范围策略** ⭐ | **✅** | 🔒 | t_hrcs_pinitrecord.fincludesub |
| **`dealstatus`** | **ComboField** | **状态** ⭐ (0/1 两态) | ❌ | ✅ | t_hrcs_pinitrecord.fdealstatus |

---

## 四、Entry Entity 分录实体清单

### 4.1 userrole 模式分录（4 entry + 1 subentry）

| Entry Key | 物理表 | 行数 | 业务含义 | 关联 refEntity |
|---|---|---|---|---|
| `userdimentry` | t_hrcs_pinituserdim | N | 用户维度值分录 | bos_user / perm_role / bos_org_biz / bos_org |
| `userdimvalueentry` (sub) | t_hrcs_pinituserdimval | M per row | 用户维度值子分录 | hrcs_dimension / haos_otclassify / haos_structproject / hrcs_dynacond |
| `userdataruleentry` | t_hrcs_pinituserdr | N | 用户数据规则分录 | perm_role / bos_devportal_bizapp / bos_entityobject / hrcs_datarule / bos_user / bos_org |
| `userbdentry` | t_hrcs_pinituserbd | N | 用户基础资料范围分录 | bos_user / bos_org / perm_role / bos_devportal_bizapp / bos_entityobject / hrcs_datarule |
| `userfieldentry` | t_hrcs_pinituserfield | N | 用户字段权限分录 | bos_user / bos_org / perm_role / bos_devportal_bizapp / bos_entityobject |

### 4.2 role 模式分录（5 entry + 4 error entry + 1 subentry）

| Entry Key | 物理表 | 行数 | 业务含义 | 关联 refEntity |
|---|---|---|---|---|
| `rolebaseentry` | t_hrcs_pinitrolebase | N | 角色基本信息分录 | perm_rolegroup / perm_admingroup |
| `rolefuncentry` | t_hrcs_pinitrolefunc | N | 角色功能权限分录 | bos_devportal_bizapp / bos_entityobject / perm_permitem |
| `roledimentry` | t_hrcs_pinitroledim | N | 角色维度分录 | bos_org_biz / hrcs_dimension |
| `roledataentry` | t_hrcs_pinitroledata | N | 角色数据范围分录 | bos_org_biz |
| `roledatavalentry` (sub) | t_hrcs_pinitroledataval | M per row | 角色数据范围维度值子分录 | haos_structproject / hrcs_dimension / hrcs_dynacond |
| `rolefieldentry` | t_hrcs_pinitrolefield | N | 角色字段权限分录 | bos_devportal_bizapp / bos_entityobject |

### 4.3 错误分录（role 模式 · 导入错误展示用）

| Entry Key | 物理表 | 业务含义 |
|---|---|---|
| `rolefunccolerrorentry` | t_hrcs_pinitrolefunccol | 角色功能权限列错误分录 |
| `rolefuncrowerrorentry` | t_hrcs_pinitrolefuncrow | 角色功能权限行错误分录 |
| `roledimrowerrorentry` | t_hrcs_pinitroledimrow | 角色维度行错误分录 |
| `roledatarowerrorentry` | t_hrcs_pinitroledatarow | 角色数据行错误分录 |

---

## 五、字段联动关系图（FormPlugin 实证 · PermInitRecordEdit）

```
inittype ─改─→ beforeBindData
              ├── role → showRoleFuncForm + showRoleDimForm + showRoleDrForm + initRoleFdEntry
              │         + 显示 rolebaseentry/rolefuncentry/roledimentry/roledataentry/rolefieldentry
              └── userrole → showDynaDimForm + paintErrMarkCol
                             + 显示 userdimentry/userdataruleentry/userbdentry/userfieldentry

name ─从空变非空─→ propertyChanged
      └── FieldTip(Info, notNull, name, "") 绿色对勾 · FP_PC1

name ─空─→ finishinit beforeDoOperation
      └── FieldTip(Error, notNull, name, "值不能为空") 红色 · FP_BDO8

userimport/roleimport ─点击─→ beforeDoOperation
      ├── checkTaskName() 检查 name + 重名清理 · FP_BDO1
      └── 已有数据 → showConfirm + reimport 回调 · FP_BDO2

finishinit ─点击─→ beforeDoOperation
      ├── 无数据 → 拒绝 · FP_BDO6
      ├── name 空 → 阻断 · FP_BDO8
      ├── dataChanged 隐式 save · FP_BDO7
      └── role → PermRoleInitFinishValidateService 五维校验 · FP_BDO3
           userrole → PermInitFinishValidateService 四维校验 · FP_BDO4
           ├── 有错误 → showForm(hrcs_roleinitcheckresult / hrcs_perminitcheckresult)
           └── 无错误 → UserPermInitConvertService.convertRecord / PermRoleInitService.initRole
                        → setValue(dealstatus, 1) → invokeOperation(save)

已打开 tab 重尝试 inituserrole/initrole → beforeDoOperation
      └── SessionManager.getCurrent().get(pageId) 已存在 ADDNEW
            → service.addAction(activate, pageId) · FP_LBDO1/FP_LBDO2
```

---

## 六、PageCache 缓存键清单（FormPlugin 实证）

| 缓存键 | 内容 | 何时灌入 | 何时使用 | 实证 |
|---|---|---|---|---|
| `importInvokeSave` | Integer(1) 标志 | finishinit 隐式 save 前 | afterDoOperation(save) 抑制成功消息 | FP_ADO1 L192-L197 |
| `deltempData` | String "success" 标志 | reimport confirmCallBack 清空后 | closedCallBack 判断是否 reimport 完成 | FP_CCB1 L417-L432 |
| `success` | String "success" | 子页面返回成功信号 | closedCallBack 判断导入是否成功 | FP_CB1 L481-L490 |
| `entityFields` | `Map<entityId, Map<propKey, propName>>` | paintErrMarkCol 首次调用时 | 后续 paintErrMarkCol 复用 | FP_BBD5 L544-L585 |
| `labelpolicy` | String policyId | (外部场景灌入 · labelpolicy 集成) | beforeItemClick 判断是否跳 hrcs_labelpolicytask | FP_LBIC1 L101-L121 |

---

## 七、子表单嵌入（FormPlugin 实证）

| 子表单 formId | 嵌入目标 | 触发 | 业务含义 |
|---|---|---|---|
| `hrcs_dynadim` | userrole 模式 tab | beforeBindData / closedCallBack | 动态维度子表单 |
| `hrcs_permroleinitfunc` | role 模式 tabpageap5 | beforeBindData showRoleFuncForm | 角色初始化功能权限 |
| `hrcs_permroleinitdim` | role 模式 tabpageap6 | beforeBindData showRoleDimForm | 角色初始化维度 |
| `hrcs_permroleinitdr` | role 模式 tabpageap7 | beforeBindData showRoleDrForm | 角色初始化数据规则 |
| `hrcs_permimportstart` | Modal | userimport 点击后跳转 | 用户权限导入起始页 |
| `hrcs_rolepermimportstart` | Modal | roleimport 点击后跳转 | 角色权限导入起始页 |
| `hrcs_perminitcheckresult` | Modal | finishinit 校验失败(userrole) | 用户权限检查结果页 |
| `hrcs_roleinitcheckresult` | Modal | finishinit 校验失败(role) | 角色权限检查结果页 |
| `hrcs_exportperm` | Modal | dlusertemp/dlroletemp 点击后 | 异步导出模板页面 |

---

## 八、平台命名规则速查（PR 红区 + 反模式）

### 8.1 物理表命名

- 主表 = `t_hrcs_pinitrecord`（`t_<app>_pinitrecord`，p 前缀 = perm init）
- 分录表 = `t_hrcs_pinit<entry>` （如 `t_hrcs_pinituserdim` / `t_hrcs_pinitrolebase`）
- 子分录表 = `t_hrcs_pinit<entry>val` / `t_hrcs_pinit<entry>row` / `t_hrcs_pinit<entry>col`
- 字段物理列名 = `f` + key.lowercase() · 不要手填 fieldName 前缀（kb_cosmic_buildmeta_traps.md 实证）

### 8.2 反模式 · 继承场景专属类

- ❌ `extends PermInitRecordEdit` · `extends PermInitRecordList` · `extends PermInitRecordDeleteOp`
- ✅ 走"并列挂插件"模式（PR-001）· ISV 父类只能是 `HRDataBaseEdit` / `HRDataBaseList` / `HRDataBaseOp`
- ✅ ISV 扩展元数据 · 把自己插件排在标品插件之前/之后（PR-002）

### 8.3 多语言表

- `name` / `simplename` / `description` 等 MuliLangTextField 由平台自动建 `_l` 表
- ISV 加 MuliLangTextField 时 · 平台**自动**建 `<table>_l` 表 · ISV **不要**手建

---

## 九、与其他 hrcs 场景对比

| 维度 | hrcs_perminitrecord | hrcs_permrelat | hrcs_dynascheme |
|---|---|---|---|
| ModelType | BillFormModel + 15 entries | BillFormModel + 1 entry | BillFormModel + 6 entries + 4 隐式 |
| HisModel | ❌ 否 | ❌ 否 | ✅ 是 |
| 工作流 | ❌ 无（save 一步到位） | ❌ 无（只有 save/delete/copy） | ✅ submit/audit/confirmchange |
| 主插件类 | PermInitRecordEdit/List | PermRelateEdit/List | DynaAuthSchemePlugin/List |
| 准入闸 | HRAdminStrictPlugin | HRAdminStrictPlugin | HRAdminStrictPlugin |
| BEC 发布 | ❌ 0 处 | ❌ 0 处 | ❌ 0 处 |
| entry 实体数 | 15（双模各用一部分） | 1 | 6 + 4 隐式 |
| 物理表数 | 21 | 3（+下游 2） | ~20+ |
| 双模路由 | ✅ inittype 切换 | ❌ 无 | ✅ changescene 切换 |

---

## 十、引用 PR 速查

| PR | 章节 | 引用条数 |
|---|---|---|
| PR-001 ISV 并列挂插件 | §8.2 反模式 | 2 |
| PR-002 RowKey 顺序 | §8.2 | 1 |
| PR-007 预置数据保护 | §3.2 issyspreset 红区 | 1 |
| PR-008 时序 iscurrentversion | §二关键认知（不适用） | 1 |
| PR-010 OP 13 生命周期 | §06 CS 引用 | (跨章) |
| PR-011 BEC | §九对比表 + §06 CS-05 | (跨章) |

→ 详细规则见 `_shared/platform_rules.json`。
