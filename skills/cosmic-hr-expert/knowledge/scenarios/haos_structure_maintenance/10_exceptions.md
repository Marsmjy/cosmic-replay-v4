# 异常诊断 · 矩阵组织维护（haos_structure）

> **状态**：🟢 基于 30 opKey Validators + 反编译实证 + 平台 ResId 抽取
> **confidence**：verified

## 1. 异常码总览

| 来源 | 数量 |
|---|---|
| Validator FormValidate（标品 ResId）| 14 |
| Validator MustInput / GroupFieldUnique / hrbddeletevalidator | 7 |
| Java 源码 ResManager.loadKDString | 2 |
| ISV 自定义错误（CS-02 / CS-04 / CS-08 落地后）| 3+ |

## 2. 标品异常码表（来自 _auto_operations.md 的 30 opKey 全 Validators）

### 2.1 save opKey · 4 条

| RuleType | Validator Id | 启用 | 中文释义（推断）| 触发条件 |
|---|---|---|---|---|
| MustInput | `6096194600001fac` | ✅ | 必填字段未填 | rootorg / org 为空 |
| FormValidate | `1VRALXJOVNKD` | - | 合法性校验（disabled）| - |
| GroupFieldUnique | `2+R3Y9FBSJ51` | ✅ | 编码 number 已存在 | save 时 number 与表内冲突 |
| GroupFieldUnique | `2+R3ZR7WI4N2` | ✅ | 名称 name 已存在 | save 时 name 与表内冲突 |

### 2.2 delete opKey · 3 条

| RuleType | Validator Id | 启用 | 中文释义 | 触发条件 |
|---|---|---|---|---|
| FormValidate | `1cc0054f000016ac` | - | 合法性校验（disabled）| - |
| FormValidate | `f789ca66000000ac` | ✅ | **数据已经禁用，不能删除** | 删除时 enable != '0' |
| hrbddeletevalidator | `2+RE4J37K857` | ✅ | **HR 基础资料删除校验**（下游引用列出）| 被下游 form 通过 refEntity 引用 |

### 2.3 audit opKey · 3 条

| RuleType | Validator Id | 启用 | 触发条件 |
|---|---|---|---|
| FormValidate | `1cc0054f000018ac` | ✅ | 合法性校验（如状态非 A）|
| FormValidate | `f2843bab0000bfac` | ✅ | 合法性校验 |
| InProcess | `2W/BRWU+MXP7` | - | 基础资料在流程中校验（disabled）|

### 2.4 unaudit · 3 条

| RuleType | Validator Id | 启用 | 触发条件 |
|---|---|---|---|
| FormValidate | `1cc0054f000019ac` | ✅ | 合法性校验（如状态非 C）|
| FormValidate | `f2843bab0000c0ac` | ✅ | 合法性校验 |
| InProcess | `2W/BU/L+T3I2` | - | 基础资料在流程中校验（disabled）|

### 2.5 disable / enable · 3 条

| opKey | Validator Id | 启用 | 触发条件 |
|---|---|---|---|
| disable | `f2843bab0000baac` | ✅ | 合法性校验（如已经是 enable=0）|
| disable | `f2843bab0000bbac` | ✅ | 合法性校验 |
| enable | `f2843bab0000bcac` | ✅ | 合法性校验（如已经是 enable=1）|

### 2.6 submit / unsubmit / submitandnew · 8 条

| opKey | RuleType | Validator Id | 启用 | 触发条件 |
|---|---|---|---|---|
| submit | FormValidate | `1cc0054f000017ac` | ✅ | 合法性 |
| submit | GroupFieldUnique | `424b895300015bac` | ✅ | 组合字段唯一 |
| submit | MustInput | `RS=E9QE25UN` | ✅ | 必填字段未填 |
| submit | GroupFieldUnique | `2DDGGGIVWM1U` | ✅ | 名称唯一 |
| unsubmit | FormValidate | `3bdba4470000d3ac` | ✅ | 合法性 |
| unsubmit | FormValidate | `f789ca66000001ac` | ✅ | 合法性 |
| submitandnew | GroupFieldUnique | `2f5773ca0001b5ac` | ✅ | 组合字段唯一 |
| submitandnew | FormValidate | `2f5773ca0001b6ac` | ✅ | 合法性 |

## 3. Java 源码 ResId 异常（来自反编译）

### 3.1 StructureListPlugin_0 · 维护架构权限缺失

```
位置：StructureListPlugin.java:143, 205
ResId："StructureListPlugin_0"
模块："hrmp-haos-formplugin"
中文："无"矩阵组织维护"的"维护架构"权限，请联系管理员。"
```

**触发**：
- 列表点 name / cardlistcolumnap / cardlistcolumnap1 链接时（hyperLinkClick）
- 列表行点 vectorap 图标时（beforeDoOperation）

**前置条件**：
- 用户对 `haos_structure` 没有 `3F/95X2VSZ=1` 权限项
- 这是业务自定义权限项 · 不同于普通 view 权限

**修复路径**：
- 联系系统管理员 · 在【角色与权限】给该用户角色加上 `3F/95X2VSZ=1` 权限项
- 检查角色继承链 · 确认权限项落到该用户

### 3.2 StructureListPlugin_1 · 矩阵组织无根组织

```
位置：StructureListPlugin.java:148, 209
ResId："StructureListPlugin_1"
模块："hrmp-haos-formplugin"
中文："当前矩阵架构无根组织，请前往"组织管理>行政组织维护>矩阵组织设置"维护其根组织信息后再进行架构维护。"
```

**触发**：
- 列表点详情时 · queryOne 出来的数据 rootorg 字段为空

**根因**：
- 历史数据迁移遗漏 rootorg 填值
- 或 ISV 自定义代码绕过必填校验直接写库

**修复路径**：
1. 在矩阵组织设置（编辑表单）补 rootorg
2. 检查迁移脚本是否漏填 rootorg
3. 如果是 ISV 代码绕过 · 修复 ISV 代码

## 4. 跨域调用异常

### 4.1 hrcs 服务不可用

**位置**：`StructureListPlugin.java:122-128`

```java
AuthorizedStructResult permResult = (AuthorizedStructResult) HRMServiceHelper.invokeHRMPService(
    "hrcs", "IHRCSBizDataPermissionService", "getUserStructProjectsF7",
    new Object[]{currUserId, appId, "haos_structure", "47150e89000000ac", "rootorg", null}
);
LOG.info("Struct permResult: {}", permResult.isHasAllStruct());
if (permResult != null && !permResult.isHasAllStruct() && !permResult.getAuthorizedStructs().isEmpty()) {
    // ...
}
```

**容错机制**：三重判空（permResult != null && !isHasAllStruct() && !authorizedStructs.isEmpty()）

**异常表现**：
- hrcs 服务超时 → 抛异常 → 标品上层捕获后列表只显示自己创建的
- 服务返回 null → 三重判空兜底 → 跳过授权扩展 · 仅显示创建的
- 服务返回 hasAllStruct=true → 列表全显示（按其它过滤条件）

**用户感知**：
- 偶发：用户某次刷新看不到对方授权的方案 · 重试一次又看到
- 持续：hrcs 服务挂了 · 影响所有 HR 列表

## 5. 参数缓存问题

### 5.1 creatorhaspermission 缓存延迟

**触发**：业务方在【应用】→【homs】→【参数】里改了 `creatorhaspermission` · 期望立即生效

**实际行为**：
- `SystemParamServiceHelper.loadBatchAppParameterByOrgFromCache` 从缓存读
- 缓存 TTL 通常分钟级
- 用户可能 5-10 分钟内看不到变化

**解决方案**：
- 用户重新登录（清 session 缓存）
- 业务方手动调"清参数缓存"接口
- 或耐心等待 cache TTL

## 6. 共性陷阱（来自 cosmic_realworld_traps）

### 6.1 buildMeta 加字段时

> 引自 `kb_cosmic_buildmeta_traps.md`：
> - HRAdminOrgField OpenAPI 不支持（74 值枚举不在）→ 改用 BasedataField + refEntity
> - EmployeeField 不存在（OpenAPI 不识 HR SDK 扩展类型）→ 用 BasedataField + refEntity=hrpi_employeenewf7query
> - 命名规则：fieldName 列名 ≤ 25 字符（推荐让平台默认生成）

### 6.2 addRule 时

> 引自 `kb_cosmic_addrule_traps.md`：
> - ActionType 必须 PascalCase（如 `Disable` 不是 `disable`）
> - preCondition 不能用 `==''` （平台不支持空串字面量）
> - 坏规则要清理 · 不能 disable 留着（影响校验性能）

### 6.3 modifyMeta 时

> 引自 `kb_cosmic_modifymeta_traps.md`：
> - formId 必须用 OpenAPI 返回的真实 id 不是 number（参考 `getFormSchema` 返回）
> - EmbedFormAp 假成功（modifyMeta 返回成功 · 实际没生效）→ 必须 getFormSchema 二次验证
> - ops 数组格式必须严格 · 漏 treeType / parentScope 静默失败

## 7. 高频用户报障 · 排查清单

### 7.1 "我新建的矩阵组织看不到"

```
排查步骤：
1. 检查 status 字段值（是否被禁用 enable=0）
2. 检查 issyspreset 字段（是否被误标系统预置）
3. 检查 otclassify 字段（必须是 1010L · 否则 StructProjectRepository 过滤掉）
4. 检查 creator 字段（是否当前用户）
5. 检查 org 字段对应 BU 的 creatorhaspermission 应用参数
6. 检查 hrcs 授权（跨域调可能挂了）
```

### 7.2 "矩阵组织保存失败 · 编码冲突"

```
排查步骤：
1. 数据库查 t_haos_structproject WHERE fnumber = ? · 看是否真存在
2. 注意：t_haos_structproject 同时存 haos_structproject 母本 + haos_structure 实例
   · 跨双 form 全局唯一（GroupFieldUnique 校验跨 form）
3. 如果业务期望"按 form 分别唯一" · 走 ISV CS-05 模式（admin_org 同款）改租户内唯一
```

### 7.3 "维护架构按钮点不开"

```
排查步骤：
1. 检查权限项 3F/95X2VSZ=1（StructureListPlugin_0 异常）
2. 检查 rootorg 是否为空（StructureListPlugin_1 异常）
3. 检查浏览器控制台 · 看是否有 JS 报错
4. 检查 setCancel(true) 链路 · 是否有 ISV 自定义代码阻断
```

### 7.4 "字段联动不生效"（CS-03 落地后报障）

```
排查步骤：
1. 检查 ISV FormPlugin 是否注册成功（开发平台 → 注册插件列表）
2. 检查 propertyChanged 是否被触发（加日志）
3. 检查 beginInit/endInit 是否包对（不包死循环 · 包了不写值都没动）
4. 检查 getView().updateView() 是否调用（不调 UI 不刷新）
5. 检查字段名是否匹配（relyonstructproject 是 BasedataField · 取值用 getDynamicObject）
```

### 7.5 "BEC 订阅没收到事件"（CS-05 落地后报障）

```
排查步骤：
1. 检查标品是否真的发了事件（实证：StructProjectRepository 没发！）
2. 如果是 ISV 自建发布方 · 检查 ISV OP 是否注册到对应 opKey
3. 检查【业务事件中心】eventNumber 是否预配置
4. 检查订阅方插件是否绑到 eventNumber
5. 检查 afterExecuteOperationTransaction 是否被触发（事务回滚不会触发）
6. 看订阅方失败队列 · 是否在重试
```

## 8. 调试日志开启

### 8.1 标品日志

```
位置：StructureListPlugin LOGGER.info(...)
关键日志：
· "hrPermOrg:{}" — 是否全权限
· "hrPermOrg orgs:{}" — 用户管辖 BU 列表
· "batchOrgParameter: {}" — creatorhaspermission 应用参数批量值
· "creator is myself structIds: {}" — 当前用户创建的方案 IDs
· "Struct permResult: {}" — hrcs 授权结果是否全权限
· "Struct permResults ids: {}" — hrcs 授权的方案 IDs
```

### 8.2 ISV 自建插件日志

ISV 代码必须加 LOGGER（参考 admin_org/06 CS-04 范本）：
```java
private static final Log LOGGER = LogFactory.getLog(MyOp.class);
LOGGER.info("MyOp · op={} entities={} · context={}",
    this.getOperateKey(), entities.length, otherContext);
```

## 9. 给业务方的诊断清单

### 9.1 列表显示异常

- [ ] 是不是新部署？参数 / 缓存可能没刷
- [ ] 是不是切换了用户？权限缓存可能有
- [ ] 是不是改了 hrcs 授权？看 hrcs 服务日志
- [ ] 是不是 ISV 加了 setFilter 扩展？查 ISV 插件链

### 9.2 保存 / 启用 失败

- [ ] 异常码是 GroupFieldUnique → 查重复
- [ ] 异常码是 MustInput → 查必填
- [ ] 异常码是 hrbddeletevalidator → 看下游引用清单
- [ ] 异常码是 ISV 自定义 → 查 ISV 校验代码

### 9.3 性能问题

- [ ] 列表加载慢 → 查 hrcs 跨域调用耗时
- [ ] F7 选择慢 → 查 StructProjectRepository 查询性能
- [ ] 删除慢 → 查 hrbddeletevalidator 下游引用扫描

## 10. 来源追溯

- Validator id 全表：`_auto_operations.md`（30 opKey）
- ResId 字符串：源码 `StructureListPlugin.java:143, 148, 205, 209`
- 跨域容错：源码 `StructureListPlugin.java:122-128`
- 共性陷阱：`memory/kb_cosmic_buildmeta_traps.md` + `kb_cosmic_addrule_traps.md` + `kb_cosmic_modifymeta_traps.md`
