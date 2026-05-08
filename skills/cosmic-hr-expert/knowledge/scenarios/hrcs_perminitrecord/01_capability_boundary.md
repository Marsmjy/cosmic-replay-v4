# 能力边界 · 权限初始化 (hrcs_perminitrecord)

> **状态**: 🟢 基于 `probe_snapshot.json` + 66 opKey 注册表 + 4 反编译类
> **confidence**: verified
> **数据源**: PermInitRecordEdit/PermInitRecordList/PermInitRecordDeleteOp/HRAdminStrictPlugin (2026-04-28)

---

## ⚡ Developer Quick Reference

| 问 | 答 |
|---|---|
| 场景做什么？ | 批量初始化权限——用户权限(userrole)或角色权限(role)——通过导入模板或手工录入后一键落库 |
| 几个 form？ | 1 个主表单 + 9 个子表单嵌入 |
| 哪些核心 opKey？ | `userimport` / `roleimport` / `finishinit` / `inituserrole` / `initrole` / `download` |
| 是否有审批工作流？ | ❌ 无（save/finishinit 一步到位，无 submit/audit） |
| 是否有 HisModel？ | ❌ 非时序（grep iscurrentversion/HisModel/boid 反编译 4 类 0 命中） |
| 是否发 BEC？ | ❌ 标品 0 处发布（grep triggerEventSubscribe/IEventService 0 命中） |
| 扩展需要改哪个 jar？ | hrmp-hrcs-formplugin-1.0.jar + hrmp-hrcs-opplugin-1.0.jar |
| 准入闸？ | HRAdminStrictPlugin 双闸（admin/cosmic + HR 域管理员） |

---

## 一、定位

**权限初始化**（`hrcs_perminitrecord`）是 HR 通用服务（hrcs）域的**批量权限导入工具**，支持两种模式：
- **userrole 模式**：批量导入用户的角色绑定、数据规则、基础资料范围、字段权限
- **role 模式**：批量创建/更新角色及其功能权限、维度、数据范围、字段权限

- **菜单路径**：HR基础服务 / HR权限管理 / 权限初始化
- **目标用户**：HR 领域管理员（`HRAdminStrictPlugin` 双闸校验）
- **形态**：BillFormModel + 15 entry/subentry entities · 非 HisModel · 无审批工作流

---

## 二、能覆盖的业务能力

### 2.1 单据生命周期（5 个高频 opKey）

| opKey | 业务能力 | 入口 | 状态变化 |
|---|---|---|---|
| `new` | 新建初始化任务 | 列表【新增】 | 不存在 → 草稿态(in-memory) |
| `modify` | 修改 | 列表【修改】 | 持久化态 → 持久化态（无版本） |
| `save` | 保存草稿 | 详情【保存】 | 草稿态 → 持久化态（dealstatus=0 未完成） |
| `delete` | 删除 | 列表选行【删除】 | 持久化态 → 删除态（物理删 · PermInitDeleteValidator 前校验） |
| `copy` | 复制 | 列表【复制】 | 持久化态 → 草稿态(新单 · 不带 id) |

### 2.2 业务专有能力（8 个核心 opKey · 非标准 CRUD）

| opKey | 业务能力 | 触发动作 | 关键插件路径 |
|---|---|---|---|
| `userimport` | 导入用户权限（Excel→分录） | 详情【用户导入】按钮 | PermInitRecordEdit.beforeDoOperation L209-L232（FP_BDO1 + FP_BDO2） |
| `roleimport` | 导入角色权限（Excel→分录） | 详情【角色导入】按钮 | PermInitRecordEdit.beforeDoOperation L209-L232（FP_BDO1 + FP_BDO2） |
| `finishinit` | 完成初始化（校验+落库下游） | 详情【完成初始化】按钮 | PermInitRecordEdit.beforeDoOperation L233-L330（FP_BDO3/FP_BDO4） |
| `inituserrole` | 列表新建用户权限初始化 | 列表工具栏【初始化用户权限】 | PermInitRecordList.beforeDoOperation L127-L140（FP_LBDO1） |
| `initrole` | 列表新建角色权限初始化 | 列表工具栏【初始化角色权限】 | PermInitRecordList.beforeDoOperation L152-L165（FP_LBDO2） |
| `download` | 下载已完成初始化记录 Excel | 列表工具栏【下载】 | PermInitRecordList.beforeDoOperation L141-L151（FP_LBDO3） |
| `dlusertemp` | 下载用户权限模板（异步） | 列表工具栏 | PermInitRecordList.beforeDoOperation L174-L196（FP_LBDO4） |
| `dlroletemp` | 下载角色权限模板（异步） | 列表工具栏 | PermInitRecordList.beforeDoOperation L174-L196（FP_LBDO4） |

### 2.3 通用能力

| 类别 | opKey | 备注 |
|---|---|---|
| 翻页 | first / previous / next / last | 详情页翻页 · 通用 |
| 工具 | view / close / refresh / option / returndata | 通用 |
| 保存类 | saveandnew / submitandnew | 保存/提交并新增 |
| 工作流 | submit / unsubmit / audit / unaudit | OP 链有注册但实际 thin（非 HisModel · 无审批） |
| 状态 | enable / disable | 启用/禁用 |
| 引入引出 | importdata / importdata_hr / show_import_record_hr / exportlist / exportlistbyselectfields | 平台引入引出 |
| HR 引入引出 | export_from_list_hr / export_from_impttpl_hr / export_from_expttpl_hr / show_export_record_hr | HR 域引入引出 |

### 2.4 双模初始化能力

| 能力 | userrole 模式 | role 模式 | 实证 |
|---|---|---|---|
| 导入用户维度绑定 | ✅ userdimentry | — | beforeBindData FP_BBD1 L111-L137 |
| 导入用户数据规则 | ✅ userdataruleentry | — | 同上 |
| 导入用户基础资料范围 | ✅ userbdentry | — | 同上 |
| 导入用户字段权限 | ✅ userfieldentry | — | 同上 |
| 创建角色基本信息 | — | ✅ rolebaseentry | beforeBindData FP_BBD1 |
| 创建角色功能权限 | — | ✅ rolefuncentry | 同上 |
| 创建角色维度 | — | ✅ roledimentry | 同上 |
| 创建角色数据范围 | — | ✅ roledataentry | 同上 |
| 创建角色字段权限 | — | ✅ rolefieldentry | 同上 |
| 完成落地下游 | UserPermInitConvertService.convertRecord | PermRoleInitService.initRole | FP_BDO3/FP_BDO4 |
| 四维错误校验 | 用户角色/时间规则/业务数据规则/字段权限 | 角色/功能项/维度/数据范围/字段(五维) | FP_BDO3/FP_BDO4 |
| 子表单嵌入 | hrcs_dynadim | hrcs_permroleinitfunc + hrcs_permroleinitdim + hrcs_permroleinitdr | FP_BBD1 + FP_CB3 |

---

## 三、不覆盖的能力（已知限制）

### 3.1 业务流程上的限制

| 不能做 | 原因 | 替代路径 |
|---|---|---|
| 审批工作流（真实 submit/audit 带审批链） | 非 HisModel · OP 链里的 submit/audit 是 thin 注册 | 走业务规则审批（不在本场景） |
| 历史版本回溯 | 非 HisModel · 没有 boid/iscurrentversion | 用 audit_log 查变更历史（如果开启） |
| 混合模式初始化（同一任务既导 userrole 又导 role） | inittype 二选一 · FP_BBD1 路由互斥 | 拆成两个任务 |
| 修改系统预置的初始化数据（issyspreset=true） | PR-007 保护 · 系统/平台维护字段 | 自建一条新记录覆盖 |
| 用户/角色导入模板自定义列 | 模板固定格式（dlusertemp/dlroletemp 异步生成） | ISV 自建导入模板 + 自定义导入 OP |
| 草稿单在列表查看 | setFilter 强制 `dealstatus=1` · 草稿被隐藏 | 自查 t_hrcs_pinitrecord 表（BusinessDataServiceHelper） |

### 3.2 平台能力上的限制

| 不能做 | 原因 |
|---|---|
| OpenAPI 直接调 buildMeta 加 EmployeeField | OpenAPI 不支持 EmployeeField（kb_cosmic_buildmeta_traps.md） |
| 继承场景专属类 PermInitRecordEdit/PermInitRecordList | PR-001 禁止 · ISV 并列挂 HRDataBaseEdit/HRDataBaseList |
| 标品发 BEC 事件 | grep 实证 0 处 · ISV 自建（CS-05） |
| 替换 reimport 逻辑 | PermInitRecordEdit.confirmCallBack 内部耦合 · 改这里影响双模 |
| 复用 PageCache 标品 key | `importInvokeSave` / `deltempData` / `success` / `entityFields` 被标品占用 |

### 3.3 初始化完成能力的限制

| 不能做 | 原因 | 替代 |
|---|---|---|
| 完成初始化前不填任务名 | FP_BDO8 硬阻断 · FieldTip 红色标记 | 先保存再完成初始化（FP_BDO7 自动隐式 save） |
| 无数据直接完成初始化 | FP_BDO6 拒绝 · hasUserRoleEntryData/hasRoleEntryData 检查 | 至少导入一条数据 |
| 重复任务名（已完成）再初始化 | FP_BDO1 checkTaskName 阻断 | 改名或删除已完成记录 |
| finishinit 跳过四/五维校验 | PermInitFinishValidateService/PermRoleInitFinishValidateService 硬调用 | 无法跳过 · 标品强制校验 |
| 关闭草稿页保留草稿 | FP_BC1 beforeClosed 自动物理删除 dealstatus=0 | 先 save 保存草稿再关闭 |

---

## 四、扩展能力边界（ISV 视角）

### 4.1 安全可扩展

| 扩展点 | 风险 | 实施路径 |
|---|---|---|
| 加自定义业务字段（如"备注/标签"） | 低 | 06 CS-01（modifyMeta 走 ISV 扩展元数据） |
| 加 propertyChanged 联动 | 低 | 06 CS-02（FormPlugin 并列挂 · PR-003/004） |
| finishinit 前置 OP Validator 自定义校验 | 中 | 06 CS-03（onAddValidators 挂 AbstractValidator · PR-010） |
| delete 前置下游引用检查 | 中 | 06 CS-04（自建 OP · 并列挂） |
| finishinit 成功后发 BEC | 中 | 06 CS-05（自建 OP 在 afterExecuteOperationTransaction · PR-011） |
| 分录扩展 + ID 生成 | 中 | 06 CS-06（modifyMeta + ID.genLongId · PR-005） |
| 列表过滤定制 | 低 | 06 CS-07（自建 ListPlugin 并列挂 · add QFilter · 不要 setQFilters） |

### 4.2 危险/不建议扩展

| 扩展点 | 为什么不建议 | 替代 |
|---|---|---|
| 继承 PermInitRecordEdit / PermInitRecordList / PermInitRecordDeleteOp | PR-001 反对 | 并列挂 · 排在标品之前/之后 |
| 改 inittype 默认值或加第三种模式 | FP_BBD2 硬编码 userrole/role 二分支 | 通过 ext FormPlugin.propertyChanged 联动实现 |
| 改 reimport 逻辑（FP_CCB1/FP_CCB2） | 双确认 + 清空 4/5 张子分录 · 耦合 deep | ISV 附加自己的清空逻辑在 CCB 末尾追加 |
| 改 beforeClosed 的草稿清理（FP_BC1） | 物理删除不走 delete OP · PermInitDeleteValidator 不触发 | ISV ext beforeClosed 追加自己的清理 · 不要 setCancel |
| 改 checkTaskName 重名清理逻辑 | FP_BDO1 · 直接 deleteOne 物理删除同名为完成记录 | ISV 加自己的任务名规则在 FP_BDO1 之前 |
| 改 finishinit 下游落库服务（UserPermInitConvertService / PermRoleInitService） | 无 SDK 注解 · hrcs 内部 service | ISV ext OP 在 afterExecute 追加自定义下游同步 |

---

## 五、菜单 / form 关系

| 资产 | 数量 | 列表 |
|---|---|---|
| 菜单条目 | 1 | HR基础服务 / HR权限管理 / 权限初始化 |
| 主表单（formNumber） | 1 | `hrcs_perminitrecord`（BillFormModel） |
| 子表单嵌入 | 9 | `hrcs_dynadim` / `hrcs_permimportstart` / `hrcs_rolepermimportstart` / `hrcs_perminitcheckresult` / `hrcs_roleinitcheckresult` / `hrcs_permroleinitfunc` / `hrcs_permroleinitdim` / `hrcs_permroleinitdr` / `hrcs_exportperm` |
| 关联标品物理表 | 21 | 见 `05_data_flow.md` 完整清单 |
| 下游落库服务 | 4 | UserPermInitConvertService / PermRoleInitService / PermInitFinishValidateService / PermRoleInitFinishValidateService |
| 标品插件 | 20 | 见 `_auto_plugin_registry.md` |
| 关键反编译类 | 4 | PermInitRecordEdit / PermInitRecordList / PermInitRecordDeleteOp / HRAdminStrictPlugin |
| opKey 总数 | 66 | 见 `rules_chain_all.json` |
| entry entities | 15 | userdimentry / userdataruleentry / userbdentry / userfieldentry / rolebaseentry / rolefieldentry / rolefuncentry / roledimentry / roledataentry / rolefunccolerrorentry / rolefuncrowerrorentry / roledimrowerrorentry / roledatarowerrorentry / userdimvalueentry / roledatavalentry |

---

## 六、与上下游场景的能力分工

```
[hrcs_perminitrecord]                    本场景: 批量初始化权限（双模：userrole / role）
    │
    ├──finishinit→  [UserPermInitConvertService.convertRecord]
    │               → t_hrcs_userrole / t_hrcs_user*  下游 userrole 表族
    │
    ├──finishinit→  [PermRoleInitService.initRole]
    │               → perm_role_* / t_hrcs_role*      下游角色表族
    │
    ├──F7引用← [bos_user]                 上游: 用户
    ├──F7引用← [perm_role]                上游: 角色（F7 选）
    ├──F7引用← [bos_org]                 上游: HR管理组织
    ├──F7引用← [bos_org_biz]             上游: 职能类型
    ├──F7引用← [bos_entityobject]        上游: 业务对象
    ├──F7引用← [bos_devportal_bizapp]    上游: 应用
    ├──F7引用← [perm_permitem]           上游: 权限项
    ├──F7引用← [perm_rolegroup]          上游: 角色组
    ├──F7引用← [perm_admingroup]         上游: 管理组
    ├──F7引用← [hrcs_datarule]           上游: 数据规则方案
    ├──F7引用← [hrcs_dimension]          上游: 维度
    ├──F7引用← [hrcs_dynacond]           上游: 动态条件
    ├──F7引用← [haos_otclassify]         上游: 团队分类来源
    └──F7引用← [haos_structproject]      上游: 构架方案

[hrcs_dynascheme]                         平行场景: 动态授权方案（HisModel · 含审批 · 不同入口）
[hrcs_permrelat]                          平行场景: 关联权限项配置（非 HisModel · 不同用途）
[11 个 hrcs 表单]                         共用 HRAdminStrictPlugin 准入闸
```

---

## 七、用户视角的"能做 vs 做不了"清单

| 用户问题 | 能做？ | 怎么做 |
|---|---|---|
| 批量给用户分配角色+数据规则？ | ✅ | 新建→选 userrole →【用户导入】上传 Excel →【完成初始化】 |
| 批量创建角色+功能权限？ | ✅ | 新建→选 role →【角色导入】上传 Excel →【完成初始化】 |
| 同一条任务既导用户又导角色？ | ❌ | inittype 二选一 · 拆两个任务 |
| 导入后不保存草稿直接关？ | ❌ | beforeClosed 自动删除 dealstatus=0 草稿 |
| 修改已完成的任务？ | ✅ | 列表修改 · dealstatus=1 只读态（FP_BBD3 隐藏 finishiinit 按钮） |
| 删除已完成的任务？ | ✅ 但有校验 | PermInitDeleteValidator 前置拦截（如关联角色还在用） |
| 查看导入失败的详情？ | ✅ | finishinit 校验失败自动弹 hrcs_perminitcheckresult / hrcs_roleinitcheckresult |
| 重导入（替换已有数据）？ | ✅ 二次确认 | reimport → showConfirm → Yes → 清空 4/5 张子分录 |
| 下载模板？ | ✅ | 列表【用户权限模板/角色权限模板下载】(异步) + 示例下载(同步) |
| 导出已完成记录？ | ✅ | 列表选行【下载】· role 用 RoleRecordExcelWriter · userrole 用 RecordExcelWriter |
| 普通业务用户进入菜单？ | ❌ | HRAdminStrictPlugin 双闸拦截 |
| 作为 F7 基础资料被引用？ | ✅ | F7 lookUp 模式直接放行（HRAdminStrictPlugin FP_HRA1） |

---

## 八、性能边界（实证）

| 操作 | 阈值 | 实证来源 |
|---|---|---|
| reimport 清空子分录数 | userrole 4 张 / role 5 张 | FP_CCB1/FP_CCB2 L417-L447 |
| role 模式五维校验维度数 | 5（角色/功能项/维度/数据范围/字段） | FP_BDO3 L233-L281 |
| userrole 模式四维校验维度数 | 4（用户角色/时间规则/业务数据规则/字段） | FP_BDO4 L282-L330 |
| 模板下载异步超时 | TaskInfo 异步回调 | FP_LBDO4 L174-L196 + FP_LCB1 L231-L256 |
| PageCache 生命周期 | 同 page 实例 · 关 tab 即清 | FP_BBD5 L544-L585 |

---

## 九、版本/2024R1 / 2025 行为差异

**未知 · 待专家确认**：本文件基于反编译 jar `hrmp-hrcs-formplugin-1.0.jar` + `hrmp-hrcs-opplugin-1.0.jar` 当前版本；版本差异需要专家提供 release_notes 后补充。

---

→ 进 06 CS 看具体扩展方案。
