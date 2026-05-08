# 能力边界 · 数据规则 (hrcs_datarule)

> **状态**：基于反编译 + 42 opKey 实抓 + scene_doc 21 字段整合
> **confidence**：real_deploy（能力边界来自标品 OP 链 + FormPlugin 实证 + scene_doc.json 字段）

## 一、场景定位

`hrcs_datarule` 是 **HR 通用服务（hrcs）权限管理域**的数据规则配置中心 · 为权限链提供"按规则过滤数据范围"的引擎能力。

**数据规则**的本质：

```
针对某个业务对象（entitynum）· 配一条 FilterCondition
   → 当用户访问该业务对象时
   → 把 FilterCondition 转成 SQL where 子句
   → 加到查询里
   → 用户只能看/操作符合 where 条件的数据
```

它跟"功能权限"（能否点按钮）正交 · 是"数据权限"（能看哪些行）的标品载体。

## 二、原生能力（21 字段 · 42 opKey · 24 plugin · 实抓）

### 2.1 字段维度

| 类别 | 字段数 | 字段示例 |
|---|---|---|
| 业务核心 | 2 | entitynum / rule |
| 标品基础（可改）| 6 | number / name / simplename / description / index / status / enable |
| 标品基础（不可改）| 7 | issyspreset / disabler / disabledate / initdatasource / orinumber / oristatus / oriname |
| BOS L0 | 5 | creator / modifier / createtime / modifytime / masterid |

### 2.2 操作维度（42 opKey · 8 核心 + 34 标品通用）

| 核心 opKey | 业务动作 | 标品支持 |
|---|---|---|
| save | 保存规则 | ✅ FilterCondition → rule + 通知权限链 + 写日志 |
| audit / unaudit | 审核 / 反审核 | ✅ status 状态机 |
| disable / enable | 禁用 / 启用 | ✅ enable + disabler/disabledate |
| submit / unsubmit | 提交 / 撤销 | ✅ status 状态机 |
| delete | 删除 | ✅ 标品 BdVersionSaveServicePlugin |

| 通用 opKey 分组 | 业务动作 |
|---|---|
| new / modify / view | 新建 / 修改 / 查看 |
| copy / refresh / close | 复制 / 刷新 / 关闭 |
| 翻页（first/previous/next/last）| 列表翻页 |
| 导入导出（importdata_hr 系列 + importdata 系列 + exportlist 系列）| 数据导入导出 |
| 移动端（mobtoolbarselect/cancel）| 移动端选择 / 取消 |
| 日志（namehistory / namehistoryview / viewonelog / logview）| 改名 / 查看日志 |

## 三、能覆盖的业务场景

| 业务诉求 | hrcs_datarule 是否原生支持 | 说明 |
|---|---|---|
| 限制某个 form 只能看部门 X 下的数据 | ✅ | 配 FilterCondition: orgunit = X |
| 按多组合过滤（部门 AND 职级 AND 状态）| ✅ | FilterGrid 支持 AND/OR 嵌套 |
| 按时序基础资料的当前版本过滤 | ✅ | F7 自动加 iscurrentversion=1 |
| 给同一业务对象配多条规则 | ✅ | 不同 number 的规则共存 · 权限链按业务方案组合使用 |
| 暂禁某条规则不删除 | ✅ | disable 操作 |
| 审核流程门禁 | ✅ | save → audit → 进入权限链 |
| 数据规则被哪些方案引用 | ❌ | **不内置**（需 ISV 补 · CS-04）|
| 跨业务对象的联合规则 | ❌ | 一条规则只能挂在一个 entitynum |
| 时间维度规则（如"2025 年的考勤数据"）| 部分 | FilterCondition 可写时间 · 但需要业务对象有时间字段 |
| 动态规则（基于当前用户属性）| 部分 | FilterCondition 支持参数化（如 `creator = 当前用户`）· 但语法靠 FilterBuilder |
| 规则变更审计 | 部分 | save · modify 写 DataRuleLog · 其他操作没写 |
| 规则导出 / 跨环境同步 | ✅ | 标品 importdata_hr / exportlist 全套 6+6 |

## 四、不覆盖的业务场景（标品做不到）

下面这些是**标品做不了** · 必须自定义开发：

### 4.1 规则的下游引用查询

标品没有"查这条规则被哪些 hrcs_dynascheme / hrcs_role 引用"的内置能力。

⚠️ ISV 自建（CS-04）· 删除/禁用前查引用 · 防止"规则指空"。

### 4.2 规则的并发冲突检测

数据规则**不是 HisModel 时序基础资料** · 没有 version / boid 字段 · 同一规则被多人同时改时 · 后写覆盖前写（last-writer-wins）· 没有乐观锁。

⚠️ ISV 自建（CS-03）· 用 modifytime 做乐观锁。

### 4.3 audit / disable / enable 的缓存清理

标品 audit / unaudit / disable / enable / delete OP **都不调** `HRPermCacheMgr.clearCache + PermNotifyService.notifyByDataRule` · 因此审核/禁用规则后 · 不一定立即生效。

⚠️ ISV 自建（CS-03）· 在自建 AuditOp / DisableOp / EnableOp 的 afterExecute 调清缓存 + 通知。

### 4.4 多业务对象联合规则

一条 hrcs_datarule 行只能挂一个 entitynum · 跨业务对象的联合数据规则（如"既限部门又限职级"）· 必须配多条规则 · 由权限链组合。

⚠️ 这是**架构层面的限制** · ISV 改不了。如果业务必须 · 考虑用 hrcs_dynascheme（动态授权方案）做组合。

### 4.5 规则的 BEC 业务事件发布

```bash
$ grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
    knowledge/_sdk_audit/_decompiled/scenarios/hrcs_datarule/
（0 命中）
```

标品 0 处发布 BEC 业务事件 · ISV 想让数据规则变更触发下游订阅（如审计系统/SIEM）· 必须自建（CS-05）。

### 4.6 规则的版本管理

数据规则改后 · 老版本不保留 · DataRuleLog 只记差异 · 没有"回滚到 V1"的能力。

⚠️ ISV 自建版本快照表是大工程 · 多数情况建议靠 DataRuleLog 人工恢复。

### 4.7 规则的批量审核 / 批量禁用

标品列表上的 audit / disable / enable 按钮**都是单选** · 没有"批量勾选 + 批量审核"。

⚠️ ISV 自建（CS-07 列表过滤定制 + 自建 ListPlugin 加批量按钮）· 调 `OperationServiceHelper.executeOperate("audit", "hrcs_datarule", arr)`。

## 五、能力边界 · 跟兄弟场景的对比

| 场景 | 角色 | 数据规则 vs 它的关系 |
|---|---|---|
| `hrcs_dynascheme` (动态授权方案) | 权限链消费方之一 | 把数据规则跟角色 + 变动事件组合 → 动态授权 |
| `hrcs_role` (HR 角色) | 权限链消费方之二 | 把数据规则挂到角色 → 角色权限 |
| `bos_dataperm` (平台数据权限) | 平台层数据权限 | 标品平台能力 · hrcs_datarule 是 HR 域的 hrcs 增强版 |
| `bos_entityobject` (业务对象) | hrcs_datarule.entitynum 引用目标 | 数据规则保护的对象 |

## 六、配置层 vs 代码层的能力分布

```
┌──── 业务能配置（不需要代码）────┐
│ 1. 加规则、改规则、删规则           │
│ 2. 改 FilterCondition 的过滤条件     │
│ 3. 加规则的描述/分类（业务字段）     │
│ 4. 审核 / 反审核 / 启用 / 禁用       │
│ 5. 导入导出规则（跨环境）           │
└─────────────────────────────────────┘

┌──── 必须 ISV 代码才能做 ────┐
│ 1. 加自定义业务字段（CS-01）         │
│ 2. 字段联动（CS-02）                 │
│ 3. save/delete 前置校验（CS-03）     │
│ 4. 删/禁前查下游引用（CS-04）        │
│ 5. save 后发 BEC 事件（CS-05）       │
│ 6. 数据规则参数项扩展（CS-06）       │
│ 7. 列表过滤定制（CS-07）             │
│ 8. 批量审核 / 批量禁用（CS-07 衍生） │
│ 9. audit/disable 后立即清缓存        │
│ 10. 规则被引用查询（CS-04 衍生）     │
└─────────────────────────────────────┘
```

详见 `06_customization_solutions.md`（7 个 CS · 30+ KB）。

## 七、性能边界

| 操作 | 性能 | 限制 |
|---|---|---|
| save 规则 · 单条 | 100-300 ms | 含 FilterBuilder 校验 + DB 写 + 缓存清理 |
| save 规则 · 批量（导入）| 受限 | 标品 importdata_hr 走批量 · 但每条都校验 + 写日志 · 100 条规则约 5-10 秒 |
| 权限链消费规则 · 单次查询 | 5-50 ms | 缓存命中下 · 反序列化 + FilterBuilder · 缓存失效后会重读全部规则 |
| FilterCondition 复杂度 | 推荐 ≤ 20 行条件 | FilterBuilder 转 SQL 时 · 大于 20 行的 OR 嵌套可能 SQL 慢 |

## 八、安全边界

| 风险 | 标品防护 | ISV 责任 |
|---|---|---|
| 普通用户配规则 | ✅ HRAdminStrictPlugin 准入 | 不需要 |
| 空规则 = "授权所有" | ✅ FormPlugin doSave 阻断 | 不需要 |
| 规则字段不在业务对象 | ✅ FilterBuilder 校验 | 不需要 |
| 规则被并发改 | ❌ 无锁 | CS-03 加乐观锁 |
| 规则被恶意 SQL 注入 | ✅ FilterBuilder 用 PreparedStatement | 不需要 |
| 删规则破坏方案 | ❌ 不查引用 | CS-04 加引用校验 |

## 九、版本兼容性

实证版本：基于 hrmp-hrcs-formplugin-1.0.jar / hrmp-hrcs-opplugin-1.0.jar · CFR 0.152 反编译。

| 字段 / 操作 | 跨版本风险 | 说明 |
|---|---|---|
| `rule` 字段格式 | 低 | FilterCondition JSON 结构稳定多年 |
| 标品 OP 链 | 中 | HRBaseDataStatusOp 等标品类签名可能跨版本变 · ISV 不要继承 |
| `HRAdminStrictPlugin` | 低 | 跨 11 hrcs 表单共用 · 行为稳定 |
| `HisModelServiceHelper.isInheritHisModelTemplate` | 低 | @SdkPublic · 苍穹官方公开 API |
| `kd.hr.hrcs.bussiness.*` 类 | 高 | 多数无 SDK 注解 · 跨版本可能改 · ISV 调用前查白名单 |

## 十、版本验证清单

升级到新版苍穹 hrmp 前 · 必须验证（建议自动化）：

1. [ ] hrcs_datarule formId 没变（21 字段 · physicalTable 没变）
2. [ ] HRDataRuleEditPlugin / HRDataRuleSaveOp 类名 + 父类没变
3. [ ] HisModelServiceHelper.isInheritHisModelTemplate 仍 @SdkPublic
4. [ ] FilterCondition / FilterBuilder / FilterGrid 公开 API 没变
5. [ ] HRPermCacheMgr.getTypeByPrefix 的 BS_HR_PERM_DATA_RULE / BS_HR_PERM_BD_DATA_RULE 两个 cache key 仍存在
6. [ ] PermNotifyService.notifyByDataRule(Long) 签名没变
7. [ ] DataRuleLogServiceHelper.dataRuleLogInit(String, DataRuleLogModel) 签名没变
8. [ ] PR-008/PR-009/PR-010/PR-011 的 SDK 类（HisModelServiceHelper / IEventService）签名没变

任一失败 · 检查 hrcs_datarule 知识库的字段引用 + 反编译重做。
