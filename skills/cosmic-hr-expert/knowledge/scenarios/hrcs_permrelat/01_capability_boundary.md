# 能力边界 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 `probe_snapshot.json` + 31 opKey 注册表 + 3 反编译类
> **confidence**: verified
> **数据源**: PermRelateEdit/PermRelateList/HRAdminStrictPlugin (2026-04-28)

---

## 一、定位

**关联权限项**（`hrcs_permrelat`）是 HR 通用服务（hrcs）域的**权限管理配置工具**，用来定义"主权限项 → N 个关联权限项"的捆绑关系，使权限分配时一并生效。

- **菜单路径**：HR通用服务 / 权限管理 / 关联权限项
- **目标用户**：HR 领域管理员（`HRAdminStrictPlugin` 双闸校验）
- **形态**：BillFormModel + 1 entry · 非 HisModel · 无审批工作流

---

## 二、能覆盖的业务能力

### 2.1 单据生命周期（5 个高频 opKey）

| opKey | 业务能力 | 入口 | 状态变化 |
|---|---|---|---|
| `new` | 新建关联权限项 | 列表【新增】 | 不存在 → 草稿态(in-memory) |
| `modify` | 修改 | 列表【修改】或详情查看后改字段 | 持久化态 → 持久化态（无版本） |
| `save` | 保存 | 详情【保存】 | 草稿/编辑态 → 持久化态 + 同步 hrcs_permrelatcfg + 计算受影响角色 |
| `delete` | 删除 | 列表选行【删除】 | 持久化态 → 删除态（物理删 · 同步删 hrcs_permrelatcfg） |
| `copy` | 复制 | 列表【复制】 | 持久化态 → 草稿态(新单 · 不带 id) |

### 2.2 业务专有能力（4 个 donothing opKey）

| opKey | 业务能力 | 触发动作 | 关键插件路径 |
|---|---|---|---|
| `auth` | 跳转细粒度授权 | 列表【授权配置】按钮 | PermRelateList.beforeDoOperation L150-L156 |
| `btnsycrole` | 同步角色（实时/全量） | 列表【同步角色】按钮 | PermRelateList.beforeDoOperation L162-L203 |
| `exportscript` | 导出 SQL 脚本 | 列表【导出脚本】按钮 | PermRelateList.afterDoOperation + generateSql L300-L342 |
| `newentry` / `deleteentry` | 分录增删行 | 详情分录 toolbar | PermRelateEdit.beforeItemClick / beforeDoOperation |

### 2.3 通用能力（22 个低频 opKey）

| 类别 | opKey | 备注 |
|---|---|---|
| 翻页 | first / previous / next / last | 详情页翻页 · 通用 |
| 工具 | view / close / refresh / option / returndata | 通用 |
| 保存类 | saveandnew | 保存并新增 |
| 引入引出 | importdata / importdetails / importtemplatelist / exportlist / exportlistbyselectfields | 平台引入引出 |
| HR 引入引出 | importdata_hr / show_import_record_hr / export_from_list_hr / export_from_impttpl_hr / export_from_expttpl_hr / show_export_record_hr | HR 域引入引出 |

### 2.4 字段联动能力（PermRelateEdit FormPlugin）

| 联动 | 实证规则 |
|---|---|
| 主 entitytype 改 → 清主权限项 + 清分录 + 重置 appcombo | FP_PC1 + FP_CFB1 |
| appcombo 改 → 同步 bizapp + 重算 mainpermitem 列表 | FP_PC2 |
| 分录 entitytypeid 改 → 清 app + 自动带出唯一应用 | FP_PC3 |
| 分录 app 改 → BU 一致性校验 + 强制重选 entitytypeid | FP_PC5 |
| 分录 permitem 清 → 联动清 permitemid | FP_PC4 |
| 分录 permitem 点击 → 弹 hrcs_choose_permitem 子页面多选 | FP_CLK1 + FP_CCB1 |

### 2.5 数据同步能力

| 能力 | 触发 | 实证 |
|---|---|---|
| save 后同步 hrcs_permrelatcfg | save 成功 → afterSaveProcessing | FP_ADO4 |
| delete 后同步删 hrcs_permrelatcfg | delete 成功 → afterDoOperation | FP_LADO1 |
| 计算受影响角色（实时） | save / btnsycrole 1-10 行 | FP_ADO3 / FP_LBDO5 |
| 全量同步角色（异步任务） | btnsycrole 未选行 | FP_LCFB1 → HRRelatePermTask |

---

## 三、不覆盖的能力（已知限制）

### 3.1 业务流程上的限制

| 不能做 | 原因 | 替代路径 |
|---|---|---|
| 审批工作流（submit/audit/confirmchange） | opKey 注册表无这些操作 · 设计上是配置型基础资料 | 走业务规则审批（不在本场景） |
| 历史版本回溯 | 非 HisModel · 没有 boid/iscurrentversion | 用 audit_log 查变更历史（如果开启） |
| 跨 BU 关联（招聘+薪酬） | BU 一致性硬规则 | 拆成多个关联权限项 · 每个 BU 一个 |
| 修改预置数据（issyspreset=true 行） | PR-007 保护 | 自建一条新记录覆盖（issyspreset=false） |
| 自动撤销删除 | 物理删 · 无逻辑删除 | 重新建一条 |

### 3.2 平台能力上的限制

| 不能做 | 原因 |
|---|---|
| OpenAPI 直接调 buildMeta 加 EmployeeField | OpenAPI 不支持 EmployeeField（kb_cosmic_buildmeta_traps.md） |
| OpenAPI modifyMeta 加 HRMulPositionField/HRMulAdminOrgField | 同上 · 走 IDEA 插件 |
| 标品发 BEC 事件 | grep 实证 0 处 · ISV 自建（CS-05） |

### 3.3 角色同步能力的限制

| 不能做 | 原因 | 替代 |
|---|---|---|
| 实时同步 > 10 行 | 性能保护 · "不能超出10行" | 走未选行的全量任务路径 |
| 中断 HRRelatePermTask | jobFormInfo.setCanStop(false) | 等任务完成（≤ 1200 秒） |
| 角色同步精确控制（按字段） | calcRtPermRole 是黑盒算法 | 通过子页面 hrcs_syncrolesel 让用户挑选 |

---

## 四、扩展能力边界（ISV 视角）

### 4.1 安全可扩展

| 扩展点 | 风险 | 实施路径 |
|---|---|---|
| 加自定义业务字段 | 低 | 06 CS-01（modifyMeta 走 ISV 扩展元数据） |
| 加 propertyChanged 联动 | 低 | 06 CS-02（FormPlugin 并列挂 · PR-003/004） |
| save 前置 OP Validator | 中 | 06 CS-03（onAddValidators 挂 AbstractValidator · PR-010） |
| delete 前置下游引用检查 | 中 | 06 CS-04（自建 OP · 并列挂） |
| save 后发 BEC | 中 | 06 CS-05（自建 OP 在 afterExecuteOperationTransaction · PR-011） |
| 分录扩展 + ID 生成 | 中 | 06 CS-06（modifyMeta + ID.genLongId · PR-005） |
| 列表过滤定制 | 低 | 06 CS-07（自建 ListPlugin 并列挂 · 设 setFilter） |

### 4.2 危险/不建议扩展

| 扩展点 | 为什么不建议 | 替代 |
|---|---|---|
| 继承 PermRelateEdit / PermRelateList | PR-001 反对 | 并列挂 · 排在标品之前/之后 |
| 改 issyspreset 字段语义 | PR-007 违反 | 不改 · 自建标志字段 |
| 改主表 entitytype 必填 / isvCanModify | scene_doc.json 标 isvCanModify=false | 不改 · 用衍生字段 |
| 改 BU 一致性规则（允许跨 BU） | 业务上灾难性 | 拆成多个 hrcs_permrelat 记录 |
| 改 hrcs_choose_permitem 子页面协议 | 私有 pk = "permId\|\|permName" | 不改 · 重写整套子页面流 |
| 改 HRRelatePermTask 任务参数 | 全量同步影响所有角色 | 走 ISV 自建任务 + ISV 自建 opKey |

---

## 五、菜单 / form 关系

| 资产 | 数量 | 列表 |
|---|---|---|
| 菜单条目 | 1 | HR通用服务 / 权限管理 / 关联权限项 |
| 主表单（formNumber） | 1 | `hrcs_permrelat`（BillFormModel） |
| 子表单 | 4 | `hrcs_choose_permitem` / `hrcs_permrelatcfg` / `hrcs_syncroleperm` / `hrcs_syncrolesel` |
| 关联标品物理表 | 3 | `t_hrcs_permrelat` / `t_hrcs_permrelat_l` / `t_hrcs_permrelatentry` |
| 下游联动表 | 1+ | `t_hrcs_permrelatcfg`（细粒度授权）+ `t_perm_role_perm`（角色权限） |
| 标品插件 | 9 | 见 `_auto_plugin_registry.md` |
| 关键反编译类 | 3 | PermRelateEdit / PermRelateList / HRAdminStrictPlugin |
| opKey 总数 | 31 | 见 `opkeys_index.json` |

---

## 六、与上下游场景的能力分工

```
[hrcs_permrelat]                    本场景: 配置主+关联权限项的捆绑关系
    │
    ├──save→  [hrcs_permrelatcfg]    下游: 细粒度授权配置（笛卡尔展开）
    │                                       auth opKey 跳转目标
    │
    ├──btnsycrole→ [perm_role_perm]  下游: 角色权限映射
    │                                       calcRtPermRole + HRRelatePermTask
    │
    ├──F7引用← [bos_entityobject]    上游: 业务对象元数据
    ├──F7引用← [hbp_devportal_bizapp] 上游: 应用元数据（bos_devportal_bizapp 别名）
    └──F7引用← [perm_permitem]       上游: 权限项基础资料

[hrcs_dynascheme]                    平行场景: 动态授权方案（HisModel · 含审批）
    │ 业务上是另一种授权配置 · 不直接关联本场景
    │
[hbjm_jobhr / haos_adminorg / 其他 11 个 hrcs 表单]
    │ 共用 HRAdminStrictPlugin 准入闸
```

---

## 七、用户视角的"能做 vs 做不了"清单

| 用户问题 | 能做？ | 怎么做 |
|---|---|---|
| 给一个主权限项加 N 个关联权限项？ | ✅ | 详情页填主对象+主权限项 · 分录加多行 |
| 跨 BU 关联（招聘+薪酬）？ | ❌ | 不行 · 拆开建多条记录 |
| 修改系统预置的关联关系？ | ❌ | 不行 · 自建一条覆盖 |
| 删除系统预置行？ | ❌ | 不行 · "预置数据无法删除" |
| 一次同步所有角色？ | ✅ | 列表【同步角色】不选行 → 确认后台任务 |
| 实时同步少数几个的角色？ | ✅ | 列表【同步角色】选 1-10 行 → 弹同步页 |
| 一次同步 100 个？ | ❌ | 限 10 行 · 走全量任务 |
| 导出关联关系到 SQL 脚本（迁移用）？ | ✅ | 列表【导出脚本】 |
| 跳到某条关联的细粒度授权配置？ | ✅ | 列表选行【授权配置】 |
| 复制一条关联然后改？ | ✅ | 列表【复制】 |
| 进入关联权限项菜单（普通业务用户）？ | ❌ | HRAdminStrictPlugin 双闸拦截 |
| 把关联权限项作为 F7 选别的资料引用？ | ✅ | F7 lookUp 模式直接放行 |
| 历史版本回溯？ | ❌ | 非 HisModel · 走 audit_log |
| 加自定义业务字段（如"备注"）？ | ✅ | 06 CS-01（ISV 扩展元数据） |
| 加 BEC 事件通知下游系统？ | ✅ ISV 自建 | 06 CS-05（自建 OP · PR-011） |

---

## 八、性能边界（实证）

| 操作 | 阈值 | 实证 |
|---|---|---|
| btnsycrole 实时同步行数 | ≤ 10 | PermRelateList L170-L173 |
| HRRelatePermTask 超时 | 1200 秒 | L255 |
| HRRelatePermTask 不可终止 | jobFormInfo.setCanStop(false) | L256 |
| exportscript 临时文件 TTL | 5000 秒 | L359 |
| 主业务对象切换 · 已有数据 → 二次确认 | confirmCallBack | L294-L302 |

---

## 九、版本/2024R1 / 2025 行为差异

**未知 · 待专家确认**：本文件基于反编译 jar `hrmp-hrcs-formplugin-1.0.jar` 当前版本；版本差异需要专家提供 release_notes 后补充。

→ 进 06 CS 看具体扩展方案。
