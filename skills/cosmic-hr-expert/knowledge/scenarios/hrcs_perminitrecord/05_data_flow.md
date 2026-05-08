# 数据流转 · 权限初始化 (hrcs_perminitrecord)

> **状态**: 🟢 基于 4 反编译类 + form_lifecycle_rules.json + rules_chain_all.json + scene_doc.json
> **confidence**: verified
> **数据源**: PermInitRecordEdit/PermInitRecordList/PermInitRecordDeleteOp/HRAdminStrictPlugin (2026-04-28)

---

## ⚡ Developer Quick Reference

| 问 | 答 |
|---|---|
| 物理表总数？ | 21 张 |
| 主表物理表？ | `t_hrcs_pinitrecord` |
| userrole 模式写哪些表？ | 主表 + t_hrcs_pinituserdim + t_hrcs_pinituserdr + t_hrcs_pinituserbd + t_hrcs_pinituserfield + 子表 t_hrcs_pinituserdimval + t_hrcs_pinituserorg + t_hrcs_pinitdrpermitem |
| role 模式写哪些表？ | 主表 + t_hrcs_pinitrolebase + t_hrcs_pinitrolefield + t_hrcs_pinitrolefunc + t_hrcs_pinitroledim + t_hrcs_pinitroledata + 子表 t_hrcs_pinitscopeview + t_hrcs_pinitscopeedit + t_hrcs_pinitroleorg + t_hrcs_pinitroledataval + error 表 4 张 |
| 下游落库服务？ | UserPermInitConvertService.convertRecord (userrole) / PermRoleInitService.initRole (role) |
| 事务边界？ | save 一个事务 / delete 一个事务 / finishinit 下游服务独立事务 |
| BEC？ | ❌ 标品 0 处发布 |

---

## 一、数据落地总览

```
                   ┌──────────────────────────────────┐
                   │  hrcs_perminitrecord (主表单)      │
                   │  PermInitRecordEdit FormPlugin     │
                   └──────┬──────────────┬──────────────┘
                          │ save         │ finishinit
                          ▼              ▼
                   ┌──────────────┐  ┌─────────────────────────────┐
                   │  HRDataBaseOp │  │  四维/五维校验 Service       │
                   └──────┬───────┘  │  ├── PermInitFinishValidate  │
                          │          │  └── PermRoleInitFinishValidate│
                          │          └──────────┬──────────────────┘
                          │                     │
                          ▼                     ▼ 校验通过
       ┌──────────────────────────────────────────────────────────┐
       │  事务范围内: t_hrcs_pinitrecord (主表)                    │
       │             + userrole 模式 4 张 entry 表                 │
       │             + userrole 模式 3 张关联子表                   │
       │             【或】role 模式 5 张 entry 表                  │
       │             + role 模式 2 张 open_scope 表                 │
       │             + role 模式 4 张 error 表                      │
       │             + 2 张 subentry 表                             │
       └─────────────────────┬────────────────────────────────────┘
                             │
                             ▼ finishinit 成功
       ┌─────────────────────────────────────────┐
       │  UserPermInitConvertService.convertRecord │ userrole 下游
       │  → hrcs_userrole* 表族 (独立事务)         │
       │  PermRoleInitService.initRole              │ role 下游
       │  → perm_role_* / t_hrcs_role* 表族         │
       └─────────────────────────────────────────┘
```

---

## 二、21 张物理表完整清单

### 2.1 主表（1 张）

| 物理表 | 类型 | 业务含义 | 关联字段 |
|---|---|---|---|
| `t_hrcs_pinitrecord` | 主实体 | 权限初始化任务主表 | fid (主键) |

### 2.2 userrole 模式分录表（5 张）

| 物理表 | 类型 | 对应 entry | 业务含义 |
|---|---|---|---|
| `t_hrcs_pinituserdim` | EntryEntity | userdimentry | 用户维度值分录 |
| `t_hrcs_pinituserdimval` | SubEntryEntity | userdimvalueentry | 用户维度值子分录（维度/维度值/是否全选/包含下级/构架方案/动态条件） |
| `t_hrcs_pinituserdr` | EntryEntity | userdataruleentry | 用户数据规则分录 |
| `t_hrcs_pinituserbd` | EntryEntity | userbdentry | 用户基础资料范围分录 |
| `t_hrcs_pinituserfield` | EntryEntity | userfieldentry | 用户字段权限分录 |

### 2.3 userrole 模式关联子表（2 张）

| 物理表 | 关联自 | 业务含义 |
|---|---|---|
| `t_hrcs_pinituserorg` | userdimentry.dim_orgrange | 组织范围子表（MulBasedataField） |
| `t_hrcs_pinitdrpermitem` | userdataruleentry.dr_permitemmulti | 权限项子表（MulBasedataField） |

### 2.4 role 模式分录表（5 张）

| 物理表 | 类型 | 对应 entry | 业务含义 |
|---|---|---|---|
| `t_hrcs_pinitrolebase` | EntryEntity | rolebaseentry | 角色基本信息分录（编码/名称/角色组/成员范围属性/角色描述） |
| `t_hrcs_pinitrolefield` | EntryEntity | rolefieldentry | 角色字段权限分录（角色编码+业务对象+字段propKey+查看/编辑） |
| `t_hrcs_pinitrolefunc` | EntryEntity | rolefuncentry | 角色功能权限分录（角色编码+业务对象+权限项） |
| `t_hrcs_pinitroledim` | EntryEntity | roledimentry | 角色维度分录（角色编码+职能类型+维度） |
| `t_hrcs_pinitroledata` | EntryEntity | roledataentry | 角色数据范围分录（角色编码+职能类型+组织范围） |

### 2.5 role 模式 open_scope 表（2 张）

| 物理表 | 关联自 | 业务含义 |
|---|---|---|
| `t_hrcs_pinitscopeview` | rolebaseentry.rbase_openscopeview | 角色公开范围（查看）子表 |
| `t_hrcs_pinitscopeedit` | rolebaseentry.rbase_openscopeedit | 角色公开范围（编辑）子表 |

### 2.6 role 模式子分录表（2 张）

| 物理表 | 类型 | 对应 entry | 业务含义 |
|---|---|---|---|
| `t_hrcs_pinitroledataval` | SubEntryEntity | roledatavalentry | 角色数据范围维度值子分录 |
| `t_hrcs_pinitroleorg` | MulBasedataField 子表 | roledataentry.rdata_orgrange | 角色组织范围子表 |

### 2.7 role 模式错误分录表（4 张）

| 物理表 | 对应 entry | 业务含义 |
|---|---|---|
| `t_hrcs_pinitrolefunccol` | rolefunccolerrorentry | 角色功能权限列错误 |
| `t_hrcs_pinitrolefuncrow` | rolefuncrowerrorentry | 角色功能权限行错误 |
| `t_hrcs_pinitroledimrow` | roledimrowerrorentry | 角色维度行错误 |
| `t_hrcs_pinitroledatarow` | roledatarowerrorentry | 角色数据行错误 |

---

## 三、save 操作的数据落地链路（主入口）

### 3.1 时序

| 时刻 | 阶段 | 操作 | 数据变化 |
|---|---|---|---|
| t0 | 用户点【保存】 | UI 触发 | model 进入 save 流 |
| t1 | beforeDoOperation(save) | FormPlugin 拦截 | FP_BDO5: dataEntity.id=0 → ORM.genLongId 兜底 |
| t2 | onPreparePropertys | OP | （标品 HRDataBaseOp 默认） |
| t3 | onAddValidators | OP | （标品 HRDataBaseOp 默认 · 无自定义 Validator） |
| t4 | beforeExecuteOperationTransaction | OP | HRBaseDataStatusOp → HRBaseDataLogOp → HRBaseDataEnableOp → HRBaseOriginalOp → PermInitRecordDeleteOp |
| t5 | beginOperationTransaction | OP | 开事务 |
| t6 | （事务内）SaveServiceHelper | 平台 | INSERT/UPDATE t_hrcs_pinitrecord + 对应模式的所有 entry/subentry 表 |
| t7 | endOperationTransaction | OP | 事务提交前 |
| t8 | （事务提交） | 平台 | 数据落库 |
| t9 | afterExecuteOperationTransaction | OP | （标品 · 不发 BEC · grep 实证） |
| t10 | afterDoOperation(save) | FormPlugin | FP_ADO1: PageCache.importInvokeSave → setShowMessage(false); FP_ADO2: setFlexVisible(initType) |

### 3.2 主表写入字段（save 路径）

| 字段 | t_hrcs_pinitrecord | 写入时机 | 备注 |
|---|---|---|---|
| `id` | fid | t6 | save 时平台分配或 ORM.genLongId 兜底（FP_BDO5） |
| `number` | fnumber | t6 | 编码 |
| `name` | fname | t6 | 任务名称（多语言 _l 表承载） |
| `status` | fstatus | t6 | 数据状态 |
| `enable` | fenable | t6 | 使用状态 |
| `creator` | fcreatorid | t6 | 平台自动 |
| `createtime` | fcreatetime | t6 | 平台自动 |
| `modifier` | fmodifierid | t6 | 平台自动 |
| `modifytime` | fmodifytime | t6 | 平台自动 |
| `masterid` | fmasterid | t6 | 平台自动 |
| `simplename` | fsimplename | t6 | 简称 |
| `description` | fdescription | t6 | 描述 |
| `index` | findex | t6 | 排序号 |
| `issyspreset` | fissyspreset | t6 | 系统预置（平台维护） |
| `disabler` | FDisablerID | t6 | 禁用人 |
| `disabledate` | FDisableDate | t6 | 禁用时间 |
| `initdatasource` | finitdatasource | t6 | 数据来源（系统设默认值） |
| `orinumber` | forinumber | t6 | 出厂编码 |
| `oristatus` | foristatus | t6 | 出厂数据编辑状态 |
| `oriname` | foriname | t6 | 出厂名称 |
| `inittype` | finittype | t6 | 类型（userrole/role） |
| `initnumber` | finitnumber | t6 | 初始化数量 |
| `includesub` | fincludesub | t6 | 数据范围策略（**必填**） |
| `dealstatus` | fdealstatus | t6 | 状态（0/1） |

### 3.3 分录表写入字段（按模式）

**userrole 模式 4 entry**：
- t_hrcs_pinituserdim: userid / roleid / customenable / bucafuncid / validstart / validend / errormsg / fileorg / orgcontainssubstr + 子表 t_hrcs_pinituserorg + t_hrcs_pinituserdimval
- t_hrcs_pinituserdr: roleid / appid / entitytypeid / dataruleid / userid / fileorg / errormsg + 子表 t_hrcs_pinitdrpermitem
- t_hrcs_pinituserbd: userid / fileorg / roleid / appid / entitytypeid / dataruleid / errormsg / propkey / propentnum / propname
- t_hrcs_pinituserfield: userid / fileorg / roleid / appid / entitytypeid / propkey / errormsg / propname / canread / canwrite

**role 模式 5 entry + 4 error entry**：
- t_hrcs_pinitrolebase: number / name / groupid / property / isintersection / createadmingrp / usescope / remark / errormsg + 子表 t_hrcs_pinitscopeview + t_hrcs_pinitscopeedit
- t_hrcs_pinitrolefield: rolenumber / appid / entitytypeid / propkey / canread / canwrite / errormsg / rolename / propname
- t_hrcs_pinitrolefunc: rolenumber / appid / entitytypeid / permitemid / errormsg
- t_hrcs_pinitroledim: rolenumber / bucafuncid / dimensionid / errormsg
- t_hrcs_pinitroledata: rolenumber / bucafuncid / errormsg / orgcontainssubstr + 子表 t_hrcs_pinitroleorg + t_hrcs_pinitroledataval
- t_hrcs_pinitrolefunccol: rolenumber / errormsg（错误列）
- t_hrcs_pinitrolefuncrow: appid / entitytypeid / permitemid / errormsg（错误行）
- t_hrcs_pinitroledimrow: rolenumber / bucafuncid / errormsg
- t_hrcs_pinitroledatarow: rolenumber / bucafuncid / errormsg

---

## 四、finishinit 的数据流（落地下游 · 独立事务）

### 4.1 userrole 模式：UserPermInitConvertService.convertRecord

```
finishinit 校验通过
  ↓
UserPermInitConvertService.convertRecord(recordId)  ← 独立事务
  ├── 读取 t_hrcs_pinitrecord (主表)
  ├── 读取 t_hrcs_pinituserdim (用户维度值)
  ├── 读取 t_hrcs_pinituserdr (用户数据规则)
  ├── 读取 t_hrcs_pinituserbd (用户基础资料范围)
  ├── 读取 t_hrcs_pinituserfield (用户字段权限)
  └── 写入下游表族（推测）:
        ├── t_hrcs_userrole* (用户角色关联表族)
        ├── t_hrcs_userdatarule* (用户数据规则表族)
        └── t_hrcs_userfield* (用户字段权限表族)
```

### 4.2 role 模式：PermRoleInitService.initRole

```
finishinit 校验通过
  ↓
PermRoleInitService.getInstance().initRole(recordId)  ← 独立事务
  ├── 读取 t_hrcs_pinitrecord (主表)
  ├── 读取 t_hrcs_pinitrolebase (角色基本信息)
  ├── 读取 t_hrcs_pinitrolefield (角色字段权限)
  ├── 读取 t_hrcs_pinitrolefunc (角色功能权限)
  ├── 读取 t_hrcs_pinitroledim (角色维度)
  ├── 读取 t_hrcs_pinitroledata (角色数据范围)
  └── 写入下游表族（推测）:
        ├── perm_role (角色定义表)
        ├── perm_role_perm (角色权限关联表)
        └── t_hrcs_role* (角色其他承载表)
```

### 4.3 下游落库服务（需后续反编译确认具体写入表）

| 服务 | 模式 | 读取 | 写入（推测 · 待确认） | SDK 注解 |
|---|---|---|---|---|
| `UserPermInitConvertService.convertRecord` | userrole | 主表 + 4 entry + 3 subentry | hrcs_userrole* 表族 | ❌ 无（hrcs 内部） |
| `PermRoleInitService.initRole` | role | 主表 + 5 entry + 2 scope + 4 error + 2 subentry | perm_role / perm_role_perm / t_hrcs_role* | ❌ 无（hrcs 内部） |
| `PermInitFinishValidateService.clickFinishValidate` | userrole | 主表 + 4 entry | （仅校验 · 不写） | ❌ 无 |
| `PermRoleInitFinishValidateService.clickFinishValidate` | role | 主表 + 5 entry | （仅校验 · 不写） | ❌ 无 |

---

## 五、delete 操作的数据流

### 5.1 列表删除（走 OP· 带 Validator）

| 时刻 | 阶段 | 操作 | 数据变化 |
|---|---|---|---|
| t0 | 用户选行点【删除】 | UI | List 触发 delete |
| t1 | onPreparePropertys | OP | OP_DEL_PP1: args.getFieldKeys().add(dealstatus) |
| t2 | onAddValidators | OP | OP_DEL_AV1: new PermInitDeleteValidator() |
| t3-t8 | OP 13 阶段 | OP | （事务内）DELETE t_hrcs_pinitrecord + 级联删除所有 entry/subentry 表 |

### 5.2 草稿自动清理（不走 OP · 直接物理删）

| 时刻 | 阶段 | 操作 | 数据变化 |
|---|---|---|---|
| t0 | 用户关闭详情页 | beforeClosed | FP_BC1: dealstatus=0 && id!=0 |
| t1 | helper.deleteOne(id) | FormPlugin | 物理删除主单 + 级联删所有子分录 |

### 5.3 物理表删除范围

```
DELETE FROM t_hrcs_pinitrecord          WHERE fid = ?     -- 主表
DELETE FROM t_hrcs_pinituserdim         WHERE fid = ?     -- userrole entry
DELETE FROM t_hrcs_pinituserdimval      WHERE fid = ?     -- userrole subentry
DELETE FROM t_hrcs_pinituserorg         WHERE fid = ?     -- userrole 组织范围
DELETE FROM t_hrcs_pinituserdr          WHERE fid = ?     -- userrole entry
DELETE FROM t_hrcs_pinitdrpermitem      WHERE fid = ?     -- userrole 权限项子表
DELETE FROM t_hrcs_pinituserbd          WHERE fid = ?     -- userrole entry
DELETE FROM t_hrcs_pinituserfield       WHERE fid = ?     -- userrole entry
DELETE FROM t_hrcs_pinitrolebase        WHERE fid = ?     -- role entry
DELETE FROM t_hrcs_pinitscopeview       WHERE fid = ?     -- role open_scope
DELETE FROM t_hrcs_pinitscopeedit       WHERE fid = ?     -- role open_scope
DELETE FROM t_hrcs_pinitrolefield       WHERE fid = ?     -- role entry
DELETE FROM t_hrcs_pinitrolefunc        WHERE fid = ?     -- role entry
DELETE FROM t_hrcs_pinitroledim         WHERE fid = ?     -- role entry
DELETE FROM t_hrcs_pinitroledata        WHERE fid = ?     -- role entry
DELETE FROM t_hrcs_pinitroleorg         WHERE fid = ?     -- role 组织范围
DELETE FROM t_hrcs_pinitroledataval     WHERE fid = ?     -- role subentry
DELETE FROM t_hrcs_pinitrolefunccol     WHERE fid = ?     -- role error
DELETE FROM t_hrcs_pinitrolefuncrow     WHERE fid = ?     -- role error
DELETE FROM t_hrcs_pinitroledimrow      WHERE fid = ?     -- role error
DELETE FROM t_hrcs_pinitroledatarow     WHERE fid = ?     -- role error
```

⚠ **物理删 vs 逻辑删**：本场景是**物理删**（不像 HisModel 把 enable 改成 10）· 没有版本控制 · 删了就没了 · ISV 不要假设可以"撤销删除"。

---

## 六、reimport 清空路径的数据流（用户确认二次导入）

```
reimport confirmCallBack Yes
  ↓
initType = role
  ├── record.set(rolebaseentry, null)   → cascade DELETE t_hrcs_pinitrolebase WHERE fid=?
  ├── record.set(rolefuncentry, null)   → cascade DELETE t_hrcs_pinitrolefunc WHERE fid=?
  ├── record.set(roledimentry, null)    → cascade DELETE t_hrcs_pinitroledim WHERE fid=?
  ├── record.set(roledataentry, null)   → cascade DELETE t_hrcs_pinitroledata WHERE fid=?
  └── record.set(rolefieldentry, null)  → cascade DELETE t_hrcs_pinitrolefield WHERE fid=?
      → helper.saveOne(record)

initType = userrole
  ├── record.set(userdimentry, null)    → cascade DELETE t_hrcs_pinituserdim WHERE fid=?
  ├── record.set(userdataruleentry, null) → cascade DELETE t_hrcs_pinituserdr WHERE fid=?
  ├── record.set(userbdentry, null)     → cascade DELETE t_hrcs_pinituserbd WHERE fid=?
  └── record.set(userfieldentry, null)  → cascade DELETE t_hrcs_pinituserfield WHERE fid=?
      → helper.saveOne(record)
```

---

## 七、PageCache 数据流（FormPlugin 关键）

| 阶段 | PageCache 写入 | PageCache 读取 |
|---|---|---|
| beforeDoOperation(finishinit) 隐式 save 前 | importInvokeSave=1 | — |
| afterDoOperation(save) | — | importInvokeSave（抑制消息） |
| reimport confirmCallBack 清空后 | deltempData="success" | — |
| closedCallBack | — | deltempData + success（判断导入是否成功） |
| paintErrMarkCol 首次调用 | entityFields = Map<entityId, Map<propKey, propName>> | — |
| paintErrMarkCol 后续调用 | — | entityFields（复用缓存 · 不重复查 EntityMetadataCache） |
| beforeItemClick | — | labelpolicy（判断是否跳 hrcs_labelpolicytask） |

---

## 八、事务边界一览

| 事务 | 范围 | 边界 | 失败回滚 |
|---|---|---|---|
| 主事务（save） | t_hrcs_pinitrecord + 对应模式 entry/subentry 表 | OP 13 阶段 | ✅ 平台自动 |
| 主事务（delete） | 同上 21 张表 | OP 13 阶段 | ✅ 平台自动 |
| reimport 清空事务 | helper.saveOne（只清 4/5 张 entry 表） | FormPlugin 内 | ✅ helper.saveOne 自带事务 |
| finishinit 下游事务 | UserPermInitConvertService / PermRoleInitService 内部 | finishinit 流程内 | ⚠ 下游事务独立 · 失败不 rollback 主表 · 但 dealstatus 未标 1 |
| 隐式 delete 事务（beforeClosed） | helper.deleteOne（21 张表级联） | FormPlugin 内 | ✅ helper.deleteOne 自带事务 |

---

## 九、不发 BEC · 不写历史表

### 9.1 BEC 实证（grep）

```bash
$ grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hrcs_perminitrecord/

(0 results across 4 files)
```

### 9.2 不写历史表

`scene_doc.json` 全字段 grep `iscurrentversion / boid / sourcevid / firstbsed / hisversion / datastatus` · 全部 0 命中。本场景**不写** `t_hrcs_perminitrecord_his` 等历史版本表。

### 9.3 ISV 扩展提示

如果 ISV 需要：
- **发 BEC** · 走 PR-011 · 自建 OP 在 `afterExecuteOperationTransaction` 调 `IEventService.triggerEventSubscribeJobs` · 06 CS-05
- **加版本/审计字段** · 不要硬塞 boid/iscurrentversion · 应该建独立的 ISV 扩展表 · CS-01 提示

---

## 十、关键 API 调用图

```
PermInitRecordEdit (FormPlugin)
  ├── HRBaseServiceHelper(hrcs_perminitrecord).queryOne(id)
  ├── HRBaseServiceHelper(hrcs_perminitrecord).saveOne(record)
  ├── HRBaseServiceHelper(hrcs_perminitrecord).deleteOne(id)
  ├── ORM.create().genLongId(hrcs_perminitrecord)
  ├── EntityMetadataCache.getDataEntityType(entityNumber)       ← rolefieldentry 中文名回填
  ├── ChoiceFieldPageCustomQueryService().parseProperty(entityType)
  ├── PermInitFinishValidateService().clickFinishValidate(id)   ← userrole 四维校验
  ├── PermRoleInitFinishValidateService().clickFinishValidate(id) ← role 五维校验
  ├── UserPermInitConvertService.convertRecord(id)              ← userrole 落地下游
  ├── PermRoleInitService.getInstance().initRole(id)            ← role 落地下游
  ├── this.getView().showConfirm(msg, OKCancel)
  ├── this.getView().showTipNotification(msg)
  ├── this.getView().showFieldTip(new FieldTip(...))
  ├── this.getPageCache().put(k, v)
  ├── this.getPageCache().remove(k)
  └── this.getView().invokeOperation(save)

PermInitRecordList (FormPlugin)
  ├── HRBaseServiceHelper(hrcs_perminitrecord).queryOne(id)
  ├── SessionManager.getCurrent().get(pageId)
  ├── SessionManager.getCurrent().put(key, pageId)
  ├── IClientViewProxy.addAction(activate, pageId)
  ├── RoleRecordExcelWriter.downloadRecordExcel(id, num, ...)
  ├── RecordExcelWriter.downloadRecordExcel(id, num, ...)
  ├── PermSheetHelper.getDemoExcelWriter()
  ├── AttachmentUtil.genDisposableUrl(url, formId, secret)
  └── SerializationUtils.fromJsonString(json, TaskInfo.class)

PermInitRecordDeleteOp (OP)
  ├── args.getFieldKeys().add(dealstatus)
  └── args.addValidator(new PermInitDeleteValidator())

HRAdminStrictPlugin (准入闸)
  ├── PermissionServiceHelper.isAdminUser(uid)
  ├── PermCommonUtil.isCosmicUser(uid)
  └── HRAdminService.isHrAdmin()
```

→ 详细 SDK 用法见 `curated_sdk.json`。
