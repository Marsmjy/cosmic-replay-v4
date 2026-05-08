# 变更影响面 · 矩阵组织维护（haos_structure）

> **状态**：🟢 基于反编译 + scene_doc.json + refentity 关系链实证
> **confidence**：verified

## 1. 改动放射图

```
                       haos_structure（本场景实例 · otclassify=1010L）
                                     │
              ┌──────────────────────┼──────────────────────┐
              │                      │                      │
              ▼                      ▼                      ▼
       字段层影响              数据层影响              视图层影响
       
       · 加字段同时影响        · 启用/禁用矩阵       · maintainframework
         haos_structproject      影响下游可见性        子页面布局
         物理表（CS-06）        · 删除影响下游        · 列表权限计算
       · 删字段断字段引用        引用                · 左树面板渲染
                              · 编码/名称变更
                                影响 BdVersion
                                历史链
```

## 2. 直接下游 · refEntity 反向引用

> 数据来源：`knowledge/workbench/_indexes/refentity_reverse.json` 中 `refs.haos_structproject` 段（本场景物理表对应的 refEntity 名 · 因为共用 t_haos_structproject）

### 2.1 反向引用 haos_structproject 的实体（典型场景）

| 引用方 form | 字段 | 影响维度 |
|---|---|---|
| `haos_orgstructlist` | `struct_project_id`（custom param 传 · 不是真字段）| 通过 maintainframework 跳进的列表过滤 |
| `haos_structorgdetail` | (待 reverse 实证)| 矩阵下挂组织详情 |
| `haos_structure` 自身 `relyonstructproject` | BasedataField → haos_structproject | 实例引用母本 · 跨 form 同表 |
| `homs_orgchart_new` | (跨域调 hrcs.IHRCSBizDataPermissionService.getUserStructProjectsF7 时引用)| 新版组织图表 |

### 2.2 行政组织反向（rootorg → haos_adminorghrf7）

| 引用方 | 字段 | 说明 |
|---|---|---|
| `haos_structure.rootorg` | HRAdminOrgField | 本场景引行政组织 · 改 admin_org 不直接影响这里（boid 稳定）· 但 admin_org 禁用会影响 F7 选择 |

## 3. 改"加字段"的影响（CS-01）

### 3.1 物理层影响 · 跨 form 共享列

```
ISV 加字段 ${ISV_FLAG}_priority 到 haos_structure
     │
     ▼
物理表 t_haos_structproject 多 ftdkw_priority 列
     │
     ├── haos_structure 实例数据：填 H/M/L
     ├── haos_structproject 母本数据：NULL（form 没绑这个字段）
     │
     ▼
查询 t_haos_structproject 的 SQL（不区分 otclassify）→ 所有行都有这一列
查询 haos_structproject form → 元数据没绑 ftdkw_priority · 不返回
查询 haos_structure form → 返回 ${ISV_FLAG}_priority
```

### 3.2 影响检查清单

- [x] **物理表多一列** —— 预期内 · 母本数据为 NULL · 业务可接受
- [x] **t_haos_structproject_l 不变**（除非新字段是多语言）—— 单值字段不影响多语言子表
- [ ] **业务报表 SQL 里有 `SELECT *`**？—— 母本侧的报表会多一列 · 通常无业务影响
- [ ] **OPM 异步任务读 t_haos_structproject**？—— 检查任务是否按字段名 list 读 · 还是 select all
- [ ] **下游 haos_orgstructlist 是否需要新字段**？—— 业务方决定 · 默认不需要

### 3.3 升级冲突评估

| 标品升级场景 | 影响 |
|---|---|
| 苍穹标品给 haos_structure 加新字段 | ISV 字段（${ISV_FLAG}_ 前缀）不冲突 |
| 苍穹改物理表 t_haos_structproject 字段类型 | ISV 字段不影响 · 但要看 ISV 字段是否依赖被改的字段 |
| 标品调 otclassify 业务编码（1010 → 其它）| 灾难 · 所有 ISV 加的"按 otclassify 隔离" 逻辑会失效 |

## 4. 改"启用 / 禁用"的影响

### 4.1 启用矩阵组织 (`enable` opKey)

```
enable 0 → 1
   │
   ▼
HRBaseDataLogOp 写日志（日志表 t_haos_structure_log 或 t_haos_structproject_log）
   │
   ▼
数据可被以下场景查到：
   · StructProjectRepository.queryAllStructProject（enable in [10, 1, 0]）
   · StructProjectRepository.queryEnablingByIds（enable=10 中间态）
   · StructureListPlugin.setFilter（enable in ['1','0']）—— 启用 + 禁用都可见
   · 跨域 hrcs.getUserStructProjectsF7
   │
   ▼
可被作为 relyonstructproject 引用 · 可在 maintainframework 进入维护
```

### 4.2 禁用矩阵组织 (`disable` opKey)

```
enable 1 → 0
   │
   ▼
所有当前依赖此实例的：
   · 已经创建的下挂组织 haos_orgstructlist 数据 → 保留 · 不会自动删
   · 引用此实例的报表 → 仍可读历史数据
   · F7 选择时是否过滤禁用的 → 看 F7 配置（StructProjectRepository.queryAllStructProject 不过滤 enable · 但 F7 默认带 enable=1 过滤）
   │
   ▼
风险：如果某 ISV 自定义 form 把矩阵组织作为运营依据 · 禁用后该 form 数据来源失效
```

### 4.3 影响检查清单

- [ ] 是否有当前在用的下挂组织数据？—— 通过 `haos_orgstructlist` 按 `struct_project_id` 过滤反查
- [ ] 是否有当前在用的报表 / 视图依赖此矩阵？—— 业务方人工排查
- [ ] 跨域 hrcs 授权是否依赖此矩阵？—— 影响 R-14 列表可见性 · 但禁用不删除授权 · 重启即恢复

## 5. 改"删除"的影响

### 5.1 标品已实现的级联校验

```
delete opKey
   │
   ├── Validator: f789ca66000000ac · "数据已经禁用，不能删除" → 必须先 enable=0
   │
   ├── Validator: hrbddeletevalidator (2+RE4J37K857)
   │       │
   │       └── 查所有 refEntity = haos_structproject 的引用 · 列出阻断
   │
   ├── CodeRuleDeleteOp · 释放编码占用
   │
   └── HRBaseDataStatusOp + HRBaseDataLogOp · 删数据 + 写日志
```

### 5.2 物理删除范围

| 表 | 操作 |
|---|---|
| `t_haos_structproject` | 删除 otclassify=1010L 的对应行 |
| `t_haos_structproject_l` | 删除对应 fid 的多语言子表行 |
| `t_haos_structproject_log`（日志表）| 保留（审计需要）|
| `t_haos_orgstructdetail` 等下游 | 不级联删 · 由 hrbddeletevalidator 校验阻断 |

## 6. 改"编码 / 名称"的影响

### 6.1 number 变更

- `BdVersionSaveServicePlugin` 写历史 · 新旧 number 都保留
- 下游引用按 boid 不按 number · 不影响（PR-009）
- 只影响：报表显示 / 列表查找 / 编码导出

### 6.2 name 变更

- `t_haos_structproject_l` 多语言子表写新版本
- 列表 caption 自动更新（`StructureListPlugin.showStructListPage` 用 `StructProjectRepository.queryByPk("id,name")` · 实时查）
- 子页面 caption 在跳转时一次性带过去 · 子页面打开后改 name 不刷子页面 caption（用户体验问题 · 关闭重开就好）

### 6.3 唯一性影响

| 改动 | Validator | 影响 |
|---|---|---|
| number 改成已存在的 | GroupFieldUnique `2+R3Y9FBSJ51` | 阻断 |
| name 改成已存在的 | GroupFieldUnique `2+R3ZR7WI4N2` | 阻断 |
| 不改 number/name 仅改其它字段 | 不触发 | 通过 |

## 7. 改"otclassify" 的影响（高危）

> 业务上**不应该**改 otclassify · 它是矩阵实例的身份标记。如果非要改：

| 从 1010L 改为其它值 | 后果 |
|---|---|
| `StructProjectRepository.createUserStructProjectFilter L92` | 列表过滤时**找不到**这条数据（过滤条件 `otclassify=1010L`）|
| 跨域 hrcs.getUserStructProjectsF7 | 看实现是否带 otclassify 过滤 · 通常也找不到 |
| F7 选择 | 找不到 |
| `maintainframework` 跳转 | 找不到 |

> 实际上：otclassify 改了相当于"把这条数据从矩阵组织变成普通 structproject 母本"· 业务语义彻底改变。

## 8. 上游变更对本场景的影响

### 8.1 admin_org（haos_adminorg）变更

| admin_org 变更 | 影响本场景 |
|---|---|
| 行政组织变更（confirmchange · admin_org/rules_chain L1490）| 本场景 rootorg 引用按 boid 自动指向当前版本 · 不影响 |
| 行政组织禁用 | F7 选择不显示 · 已选定的 rootorg 仍可读（按 boid 历史读出来）|
| 行政组织删除 | 反向 hrbddeletevalidator 会阻止删除（因为 haos_structure.rootorg 在引用）|
| 行政组织重命名 | 本场景列表 rootname 字段不自动更新（反规范化字段）· 业务方接受或人工补任务刷新 |

### 8.2 haos_structproject 母本变更

| haos_structproject 变更 | 影响本场景 |
|---|---|
| 母本字段加 / 改 | 不影响本场景 form（form 元数据独立）· 但物理表共享会多列 |
| 母本启用 / 禁用 | 本场景 relyonstructproject F7 是否还能选到该母本 |
| 母本删除 | 阻断（hrbddeletevalidator · 本场景 relyonstructproject 在引用）|

### 8.3 跨域 hrcs 授权变更

| hrcs 变更 | 影响本场景 |
|---|---|
| 用户授权方案 ID 变更 | 实时影响 R-14 列表过滤（`StructureListPlugin.setFilter` 每次都跨域查 · 无缓存）|
| hrcs 服务不可用 | 标品三重判空兜底（`L124`）· 仅显示自己创建的方案 |

### 8.4 应用参数 creatorhaspermission 变更

| 参数变更 | 影响 |
|---|---|
| true → false | 该 BU 下的用户**不再**看到自己创建的方案（仅看 hrcs 授权的）|
| false → true | 增量看到 |
| 缓存 | `SystemParamServiceHelper.loadBatchAppParameterByOrgFromCache` 一级缓存 · 改后需重新登录或手动刷缓存（业务侧痛点）|

## 9. ISV 改动后的回归测试清单

| 改动类型 | 必测场景 |
|---|---|
| 加字段（CS-01）| 1) 矩阵实例新增 / 修改保存 字段写库 · 2) haos_structproject 母本表单不显示该字段 · 3) 列表显示正常 |
| 加 save 校验（CS-02 / CS-04）| 1) 命中条件阻断 · 2) 不命中通过 · 3) 配合 submit / submitandnew 也校验 |
| 字段联动（CS-03）| 1) 触发字段变更后联动 · 2) 死循环 / UI 不刷新检查 · 3) 用户手改联动后字段是否被覆盖 |
| BEC 发布 / 订阅（CS-05）| 1) 主事务提交后事件发出 · 2) 订阅方接收并处理 · 3) 失败重试与幂等 · 4) 事务回滚不发事件 |
| 列表过滤（CS-08）| 1) 标品过滤仍生效 · 2) 我的过滤叠加生效 · 3) 切换用户场景 · 4) 跨 BU 场景 |
| 左树扩展（CS-07）| 1) 默认管辖 BU 显示 · 2) 含子公司 BU 显示 · 3) 切换 BU 时左树重算 |

## 10. 已知风险点摘要

1. **otclassify 是身份标记不可改** —— 业务上要管控 ISV 代码不能 set otclassify 字段
2. **共用物理表的字段交叉影响** —— CS-06 的隔离方案是有边界的 · 物理列共享是平台机制 · ISV 代码不应假设"完全隔离"
3. **标品没发 BEC 事件** —— 业务方如果期望"标品已发事件 ISV 订阅" · 直接告知"标品未实现 · 必须 ISV 自建发布"
4. **跨域 hrcs 服务故障容错** —— 标品有三重判空兜底 · ISV 写过滤逻辑时也要做相同兜底
5. **应用参数 creatorhaspermission 缓存** —— 改了不实时生效 · 业务方知情
6. **冗余字段 rootname / rootnumber** —— 标品反规范化设计 · admin_org 改名不会自动同步本场景 · 需要业务方接受或自建定时刷新任务
