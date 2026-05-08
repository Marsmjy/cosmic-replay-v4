# 变更影响面 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 01_capability_boundary.md 下游关系 + 05_data_flow.md 事务边界 + form_lifecycle_rules.json
> **confidence**: verified
> **数据源**: PermRelateEdit/PermRelateList 反编译 + scene_doc.json + hrcs_permrelatcfg 同步机制 (2026-04-28)

---

## 一、影响面全景图

```
                    ┌──────────────────────────────────────┐
                    │       hrcs_permrelat (本场景)         │
                    │   关联权限项 · BillFormModel          │
                    └──────┬───────────┬───────────────────┘
                           │           │
              save/delete  │           │ btnsycrole / save后calcRtPermRole
                           ▼           ▼
              ┌──────────────────┐  ┌──────────────────────┐
              │hrcs_permrelatcfg │  │  perm_role /          │
              │细粒度授权配置     │  │  perm_role_perm        │
              │(独立场景 · 下游)  │  │  角色权限映射          │
              └──────────────────┘  └───────────┬──────────┘
                                                │
                                         ┌──────┴──────────┐
                                         │  11 个 hrcs 表单  │
                                         │  (共用准入闸      │
                                         │   HRAdminStrict)  │
                                         └──────────────────┘
```

---

## 二、改动本场景物理表的影响

### 2.1 改主表 t_hrcs_permrelat

| 改什么 | 影响范围 | 级联动作 | 风险 |
|---|---|---|---|
| 加字段（主表） | hrcs_permrelat List/Form 视图 | 视图需要同步更新 · 列表列需手动加 | 低 (ISV 扩展元数据) |
| 改 entitytype 字段类型/必填 | **全部关联权限项记录** (所有分录) | F7 过滤逻辑失效 (FP_BF7_1) · propertyChanged 联动错乱 · BU 校验基准漂移 | **高** · `isvCanModify=false` · **不改** |
| 改 appcombo/mainpermitem 字段类型 | **全部记录** | propertyChanged 联动 5 条全部失效 · ComboField → BasedataField 切换不可行 | **高** · **不改** |
| 改 description 多语言字段 | `_l` 表同步变更 | exportscript SQL 脚本生成需适配 | 低 |
| 加多语言字段 | 自动建 `_l` ISV 扩展表 | exportscript ISV 需自行处理 ISV `_l` 表 | 低 |

### 2.2 改分录子表 t_hrcs_permrelatentry

| 改什么 | 影响范围 | 级联动作 | 风险 |
|---|---|---|---|
| 加字段（分录） | hrcs_permrelat 详情分录 UI | 无下游同步 (分录字段不传播到 hrcs_permrelatcfg) | 低 (ISV 扩展元数据) |
| 改 entitytypeid/app 字段 | 所有分录行 · 全部关联方案 | F7 过滤 (FP_BF7_2-5) 失效 · BU 一致性校验 (FP_BDO2/FP_PC5) 错乱 | **高** · `isvCanModify=false` · **不改** |
| 改 permitemid/permitem 字段 | 权限项选择子页面回填逻辑 | `hrcs_choose_permitem` 子页面 pk 协议 "permId\|\|permName" 依赖 | **高** · permitem `isvCanModify=false` · 不改 |
| 改 issyspreset 字段语义/默认值 | 所有预置行保护机制失效 | FP_BDO3 删除拦截失效 · FP_ABD4 锁字段失效 · PR-007 冲突 | **高** · `isvCanModify=false` · 红区 · **不改** |
| 改 issynrole 字段 | btnsycrole 纳入计算的分录行 | 全量同步任务漏行或多行 | **中** · `isvCanModify=true` · 可改但谨慎 |

---

## 三、改动标品插件的影响

### 3.1 改 PermRelateEdit

| 改什么 | 影响范围 | 级联动作 | 风险 |
|---|---|---|---|
| 改属性常量 (FIELD_*) | 所有字段 key 引用 | registerListener / propertyChanged / afterBindData 全部失效 | **灾难性** · 不要改 |
| 改 PageCache 缓存键名 | 所有缓存读写 | afterBindData 灌 / beforeF7Select 读 全部断裂 | **灾难性** · 不要改 |
| 改 BU 一致性校验逻辑 | 所有 save 操作 | 可能放行跨 BU 关联 · 业务上灾难性 | **灾难性** · 不要放松 |
| 改 issyspreset 保护逻辑 | 所有预置数据 | 预置行可删/可改 · PR-007 违反 | **灾难性** |
| 改 afterSaveProcessing | **hrcs_permrelatcfg 同步** | hrcs_permrelatcfg 增量更新失效 · 孤儿/缺失数据 | **高** (PermRelateServiceHelper 调用链) |
| 改 permitem 回填子页面协议 | 权限项选完无法回填 | "permId\|\|permName" 解析错 → permitemid 为空 | **高** |

### 3.2 改 PermRelateList

| 改什么 | 影响范围 | 级联动作 | 风险 |
|---|---|---|---|
| 改 delete 后同步 deletePermRelateConfigs | delete 操作所有行 | hrcs_permrelatcfg 残留孤儿数据 | **高** |
| 改 btnsycrole 实时行数上限 (10) | 角色同步触发逻辑 | 可能允许超量实时计算导致性能问题 | 中 |
| 改 exportscript TX.requiresNew 事务 | SQL 导出污染外部事务 | 事务回滚可能导致数据不一致 | **高** |
| 改 PermItemProvider 替换逻辑 | 所有列表数据展示 | mainpermitem 显示文本无法翻译 · 分页异常 | **高** |
| 改 incPermTips closeCallBack | 详情页保存后列表无法刷新 | changed 丢失 · syncRole 弹窗不触发 | 中 |

### 3.3 改 HRAdminStrictPlugin

| 改什么 | 影响范围 | 级联动作 | 风险 |
|---|---|---|---|
| 去掉 F7 lookUp 放行 (FP_HAS1) | 所有 F7 引用关联权限项的场景 | 普通业务用户无法选关联权限项作为基础资料 | **高** · 影响所有 11 个 hrcs 表单 |
| 放松 HR 域管理员校验 | 所有 11 个 hrcs 表单的权限闸 | 非管理员可以改权限配置 | **灾难性** |
| 加额外校验条件 | 所有 11 个 hrcs 表单 | 准入条件全变 | 中 (但影响面大) |

---

## 四、改动共享服务/Helper 的影响

### 4.1 PermRelateServiceHelper

| 方法 | 调用方 | 改的影响 |
|---|---|---|
| queryPermRelates | PermRelateList.beforeDoOperation(delete) | delete 缓存待删数据 · 改查询字段影响后续 deletePermRelateConfigs |
| deletePermRelateConfigs | PermRelateList.afterDoOperation(delete) / PermRelateEdit.afterSaveProcessing | hrcs_permrelatcfg 孤儿数据 · 或误删有效数据 |
| addPermRelateConfigs | PermRelateEdit.afterSaveProcessing | 新关联权限项同步缺失 · 细粒度授权配置丢失 |
| getPermInfo | calcRtPermRole 调用链 | 角色同步计算缺权限项信息 · 影响 btnsycrole |
| queryPermItems / getEntryInfo | afterBindData / afterSaveProcessing | permitemid→permitem 翻译失效 · afterSaveProcessing diff 不准 |

### 4.2 EntityCtrlServiceHelper

| 方法 | 调用方 | 改的影响 |
|---|---|---|
| buildFilterForF7 / filterNoPermEntity | beforeF7Select(entitytype) | 主业务对象可选列表全变 · 可能选错或不选 |
| getEntityRelatedApps | beforeF7Select(app) | 分录应用可选列表偏差 · 可能漏选或多选 |
| queryEntityPermItems | afterBindData (permitemid 翻译) | 权限项中文名显示错 |
| queryExistedForBidInfo | afterBindData (灌 PageCache) | 被禁信息缺失 → 允许选择已被禁的权限项/应用 |
| getAppComboForPerm | setAppComboList | appcombo 下拉项全错 |
| removeForBidApp | beforeF7Select(app) | 允许选择被禁应用 |

### 4.3 HRBuCaServiceHelper

| 方法 | 调用方 | 改的影响 |
|---|---|---|
| getBuCaFuncFromSpec | FP_PC5 + FP_BDO2 (BU 一致性) | 跨 BU 关联校验全失效 · 业务上灾难性 |

### 4.4 PermRtSyncService

| 方法 | 调用方 | 改的影响 |
|---|---|---|
| calcRtPermRole | FP_ADO3 / FP_LBDO5 | 角色同步差异计算错 · 多同步或少同步角色 |
| getRelatePermInfoPair | FP_LBDO5 (btnsycrole 实时) | 无法组装 RelatePermInfo → 同步角色失败 |
| writeOpLog | 多处 | 操作日志缺失 |

---

## 五、改动子页面协议的影响

| 改什么 | 影响范围 | 级联动作 |
|---|---|---|
| 改 hrcs_choose_permitem pk 分隔符 (双竖杠→其他) | PermRelateEdit.closedCallBack FP_CCB1 | permitemid 回填解析失败 → 全空 |
| 改 hrcs_choose_permitem customParam 键名 | PermRelateEdit.click FP_CLK1 | 子页面无法接参 → 显示所有权限项(不受限制) |
| 改 hrcs_permrelatcfg 列表 pageId 格式 | PermRelateList.beforeDoOperation FP_LBDO2 | 父子关联丢失 · auth 跳转后回退失效 |
| 改 hrcs_syncrolesel 弹窗 roleInfo JSON 格式 | PermRelateList.closedCallBack FP_LCCB1 | 子页面无法解析角色信息 |

---

## 六、改动调度任务的影响

| 改什么 | 影响范围 | 级联动作 |
|---|---|---|
| 改 HRRelatePermTask 类名/包路径 | PermRelateList.startJob() FP_LCFB1 | 全量同步角色任务找不到类 · 启动失败 |
| 改 setTimeout(1200) | 大数据量场景 | 减少: 任务超时失败 · 增加: 任务卡死 |
| 改 setCanStop(false)→true | 用户可终止 | 角色同步中断导致 perm_role_perm 半脏状态 |
| 改 task params {syncAll: 1} | HRRelatePermTask.execute 入口 | 任务执行逻辑全变 |

---

## 七、关联场景影响矩阵

| 关联场景 | 触发链 | 本场景改动影响 |
|---|---|---|
| hrcs_permrelatcfg | save→afterSaveProcessing 同步 / delete→afterDoOperation 同步 / auth 跳转 | PermRelateServiceHelper 变更 → 同步失败 OR 孤儿数据 |
| perm_role_perm | save→calcRtPermRole→hrcs_syncrolesel/syncroleperm 落库 / btnsycrole | calcRtPermRole 变更 → 角色同步错 |
| bos_entityobject (业务对象) | beforeF7Select F7 引用 | EntityCtrlServiceHelper 变更 → F7 过滤错误 |
| hbp_devportal_bizapp (应用) | beforeF7Select F7 引用 / appcombo 下拉 | EntityCtrlServiceHelper 变更 → 应用列表错误 |
| perm_permitem (权限项) | hrcs_choose_permitem 子页面选择 | queryEntityPermItems 变更 → 权限项可选列表错误 |
| hbss_hrbucafunc (BU 字典) | afterBindData putAllBuInfoToCache | BU 映射错 → 校验失效 |
| 11 个 hrcs 表单 (共用 HRAdminStrictPlugin) | preOpenForm 准入闸 | HRAdminStrictPlugin 变更 → 所有 hrcs 权限管理功能入口全变 |

---

## 八、改动风险分级

| 风险级别 | 改动类型 | 例子 |
|---|---|---|
| 🔴 灾难性 | 改 BU 一致性规则 / 改 HRAdminStrictPlugin 双闸 / 改 issyspreset 保护 / 改 PermRelateServiceHelper 核心方法 / 改 entitytype 字段 | 影响所有关联权限项记录 · 影响所有 11 个 hrcs 表单 |
| 🟠 高 | 改 afterSaveProcessing 同步 / 改 delete 后同步删 / 改 permitem 回填协议 / 改 calcRtPermRole | 影响 hrcs_permrelatcfg 数据一致性 / 角色同步准确性 |
| 🟡 中 | 改 btnsycrole 行数上限 / 改 exportscript 事务 / 改 task params / 改 issynrole 默认值 | 影响特定操作路径 · 可恢复 |
| 🟢 低 | 加自定义字段 / 加 ISV OP Validator / 加 ISV 列表过滤 / 加 ISV propertyChanged 联动 | ISV 扩展元数据 · 不碰标品核心 |

---

## 九、测试回归 checklist（改动后必验）

| 操作路径 | 验证点 |
|---|---|
| 新建 + 选主业务对象 + 保存 | entitytype F7 过滤 · appcombo 下拉 · mainpermitem 下拉 |
| 改主业务对象 | confirmCallBack Yes 清空 / Cancel 回滚 |
| 分录新增行 + 选业务对象 + 选应用 + 选权限项 | F7 过滤 (排已选/排被禁) · BU 校验 · 子页面回填 |
| 删分录行 | issyspreset=true 不可删 · issyspreset=false 可删 |
| 保存 | hrcs_permrelatcfg 同步 · calcRtPermRole · incPermTips 回调 |
| 删除 | hrcs_permrelatcfg 同步删 · 列表 refresh |
| auth 跳转 | pageId 正确 · hrcs_permrelatcfg 打开 |
| btnsycrole 选 1-10 行 | 实时计算 · hrcs_syncroleperm 弹窗 |
| btnsycrole 全量 | ConfirmTypes.Delete 确认 · HRRelatePermTask 下发 |
| exportscript | TX.requiresNew 事务 · 三表 INSERT SQL · 缓存文件下载 |
| HRAdminStrictPlugin 双重 | F7 放行 · 非管理员拦截 · HR 管理员放行 |
