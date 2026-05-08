# 参考案例 · 维度管理 (hrcs_dimension)

> **状态**: 🟡 likely · 待业务/客户实战补全
> **数据源**: 反编译 4 类 + scene_doc.json + admin_org/dynascheme/hbpm 案例参考

---

## 一、典型业务场景案例（标品支持）

### 案例 1 · 总部 HR 配置"员工职级"枚举维度（datasource=enum）

**背景**：总部 HR 需要按"职级"切分权限 · 业务上职级是固定 10 档（P3-P12 等）· 不需要做基础资料对接 · 直接列举枚举值即可。

**操作**：
1. 总部 HR 进入"维度管理"菜单 → 新增
2. 填字段：
   - number = "DIM_LEVEL"（自动 CodeRuleOp）
   - name = "员工职级"
   - datasource = "enum"
   - showtype = "list"
   - description = "员工职级切分权限维度（P3-P12）"
3. 点【新增分录】（newentry opKey）· 填 entry 子表：
   - 行 1: value=P3, displayvalue=P3, entryindex=1
   - 行 2: value=P4, displayvalue=P4, entryindex=2
   - ...
   - 行 10: value=P12, displayvalue=P12, entryindex=10
4. 点【保存】 → 标品走 checkEntitytype 校验 → entry 完整 + displayvalue 不重复 → 通过
5. 点【提交】（A → B） → 点【审核】（B → C）
6. 后续：dynascheme 配置 condition 时引用此 dimension（id=100）· 比如"员工.dimension(100).value = P5+"

### 案例 2 · 区域 HR 配置"工作城市"基础资料维度（datasource=basedata）

**背景**：区域 HR 按城市切分权限 · 需要从苍穹标品基础资料"地区"中选取业务对象。

**操作**：
1. 新增 dimension · datasource = "basedata"
2. F7 entitytype → 走 DimensionNewEdit.beforeF7Select 强制 modeltype=BaseFormModel 过滤
3. 用户选 entitytype = `bos_area` · DimensionNewEdit.propertyChanged 走 limitBasedataType 检查 → 业务对象有 number+name → 通过
4. handleShowTypeForEntityType 检查 → bos_area 是否继承 hbp_bd_treetpl_all
   - 如果是 → showtype 可选 list/tree
   - 如果不是 → showtype 强制 list 且 disable
5. 填字段：name = "工作城市维度" · description = "..."
6. save → submit → audit
7. 后续：dynascheme 配 condition 引用此 dimension · 比如"员工.workcity.dimension(101) in [北京, 上海]"

### 案例 3 · 修改 enum dimension 的 entry.value（典型故障）

**背景**：`员工职级` 维度（案例 1 创建的）已被 5 个 dynascheme 引用 · 客户改运营策略 · 需要把 `value=P3` 改成 `value=JUNIOR`。

**操作 + 故障**：
1. 用户进入 dimension 编辑界面
2. **DimensionNewEdit.afterBindData 反查 EntityCtrlServiceHelper.getRoles** → roles.count() > 0 → entry value 字段全部 setEnable(false) UI 锁定
3. 用户**改不了 value 字段**（UI 拒）
4. 客户运维只能通过 SQL 直接 `update t_hrcs_dimensionenum set fvalue = 'JUNIOR' where ...`
5. 后果：dynascheme.condition 内嵌的 `{"value":"P3"}` 失效 · 5 个方案的"P3 员工权限分配"全部失败

**正确做法**：
- 先 disable 受影响的 dynascheme（让标品停止派发新分配）
- SQL 改 dimension entry.value
- 同步改 5 个 dynascheme.condition JSON 内嵌引用
- enable dynascheme · 让标品重新派发

→ 实战不能跳过 dynascheme 同步 · 否则数据不一致。

---

## 二、ISV 二开案例（基于本知识库 7 个 CS）

### 案例 4 · A 客户 · 加"维度类目"字段（CS-01）

**需求**：客户 BI 系统要按"维度类目"分类统计动态授权方案使用情况（权限类 / 数据类 / 报表类 / 业务类）。

**实施**：
1. modifyMeta add field 到 hrcs_dimension · key=`${ISV_FLAG}_dimcategory` · 类型 MulComboField · 选项: 重要/临时/试点/长期
2. 苍穹开发平台部署
3. 客户 HR 用户使用：在新建/编辑 dimension 时填类目
4. BI 系统通过 OData 查 t_hrcs_dimension.ftdkw_dimcategory · dimension 不是 HisModel · 不需要带 iscurrentversion 过滤（PR-008 不适用）

**踩坑实战**：
- 第一次 fieldName 手填 "fk_tdkw_dimcategory" → 平台再加 f → 列名变 "ffk_tdkw_dimcategory" 怪名 · 重做
- 第二次未带 ISV 前缀 "dimcategory" → 升级时与标品某字段冲突 · 重做
- 第三次正确：key="${ISV_FLAG}_dimcategory" · 不传 fieldName · 平台自动 ftdkw_dimcategory

### 案例 5 · B 客户 · datasource → 自动带 entry 模板（CS-02）

**需求**：客户 HR 经常建 datasource=enum 类型的 dimension · 而且枚举值经常重复（如"工作年限段"反复用 0-3年/3-5年/5-10年/10年+）· 希望切换 datasource=enum 时自动带出常用模板。

**实施**：
1. 加 ISV 自建表 `${ISV_FLAG}_enumcategory`（已 buildMeta · 父模板 hbp_bd_tpl_all）
2. 加 FormPlugin `TdkwDimensionDataSourceEnrichPlugin extends HRDataBaseEdit`
3. 重写 propertyChanged 监听 datasource=enum 时 · 反查 ${ISV_FLAG}_enumcategory · createNewEntryRow 灌入 entry · beginInit/endInit 包围
4. 注册到 hrcs_dimension · targetType=BILL_FORM

**踩坑实战**：
- 第一次没加 beginInit/endInit → setValue("entry.value") 触发 propertyChanged 又调 setValue · 死循环爆栈（PR-004）
- 第二次没判断 newValue · 导致用户切到 basedata 时也清掉 entry · 但 datasource=basedata 路径下标品已经清了 · 重复操作导致 setDataChanged 错乱
- 第三次正确：if (!"enum".equals(newValue)) return; 提前剪枝

### 案例 6 · C 客户 · save 同名拒绝（CS-03 save 方向）

**需求**：客户业务规定 dimension 名称必须唯一（避免 BI 报表混乱）· 但标品没拦同名（平台默认让重）。

**实施**：
1. 自建 OP `TdkwDimensionExtraOp extends HRDataBaseOp`
2. onPreparePropertys 声明读 name + status + issyspreset
3. onAddValidators 注册 `TdkwDimensionUniqueNameValidator`
4. validate 反查 `WHERE name = ? AND id != ?` · 命中即 addErrorMessage
5. 注册到 hrcs_dimension · operation_keys=["save"]

**踩坑实战**：
- 第一次反查没排除当前行 id → 编辑场景下永远命中自己 · 报错"维度名称已存在"· 用户改不了
- 第二次没 onPreparePropertys 声明 name → entity.getString("name") 返回 null · 永远不命中（标品不主动加载 name 字段·要 OP 声明）
- 第三次正确：onPreparePropertys + 反查带 `id != currentId`

### 案例 7 · D 客户 · disable 引用阻断（CS-03 disable 方向）

**需求**：客户希望 disable dimension 时硬阻断 · 直到所有 dynascheme 解除引用（不接受标品"已有不影响"的保守策略）。

**实施**：
1. 自建 OP `TdkwDimensionDisableOp extends HRDataBaseOp`
2. onAddValidators 注册 `TdkwDimensionDisableValidator`
3. validate 反查 hrcs_dynascheme.condition LIKE "%dimensionId%" + iscurrentversion=true
4. 命中即 addErrorMessage("维度被 X 个动态授权方案引用 · 不能禁用 · 请先解除引用")
5. 注册到 hrcs_dimension · operation_keys=["disable"]

**踩坑实战**：
- 第一次没带 iscurrentversion=true → 历史版本也算引用 · 误报（PR-008）
- 第二次反查 condition 字段没正确转义 dimensionId 前后引号（DecisionSet JSON 嵌套）→ dimensionId=100 误命中 dimensionId=10000
- 第三次正确：QFilter("condition", "like", "%\"dimension\":\"100\"%") + iscurrentversion=true

### 案例 8 · E 客户 · audit 后置 BEC 通知 BI 系统（CS-05）

**需求**：客户 BI 系统是外部 SaaS（非苍穹标品）· 需要 dimension 审核后 BI 即时拉数。

**实施**：
1. 在【开发平台】→【业务事件管理】预配置 eventNumber `${ISV_FLAG}_dimension_audited`
2. 自建 OP `TdkwDimensionAuditPublishOp extends HRDataBaseOp`
3. afterExecuteOperationTransaction 阶段调 IEventService.triggerEventSubscribeJobs
4. BI SaaS 注册 BEC 订阅方 · 收到事件后调 OData 拉 dimension 数据

**踩坑实战**：
- 第一次没在事件管理预配置 → BEC 静默忽略 · BI 永远收不到 · 排查 2 天（PR-011 prerequisite）
- 第二次在 endOperationTransaction 阶段发 → 主事务可能未最终提交 · 偶发"BI 拉到的数据是上一版"（PR-010）
- 第三次直接 try/catch 异常 + 不抛 → BEC 平台 down 时事件丢 · 改成 try/catch + 写自建表"补发队列" · 调度任务定时重试

### 案例 9 · F 客户 · 列表权限过滤（CS-07）

**需求**：客户区域 HR 不需要看到 datasource=basedata/orgteam 类型的全局维度（保护信息）· 只看 enum 类型即可。

**实施**：
1. 自建 ListPlugin `TdkwDimensionListFilterPlugin extends HRDataBaseList`（PR-001）
2. 重写 setFilter · 反查当前用户 admingroup → 不在总部组就加 datasource=enum 过滤
3. 注册到 hrcs_dimension · targetType=LIST_FORM

**踩坑实战**：
- 第一次 extends DimensionList · 标品 DimensionList 升级时改了 callBackId · ISV 类挂了（PR-001 教训）
- 第二次写死 admingroup id 1L 当总部组 → 客户不同集团 admingroup id 不一样 · 改成 ISV 自建配置表 ${ISV_FLAG}_admingroup_dim_perm
- 第三次正确：`extends HRDataBaseList` + 配置表查总部组列表

---

## 三、跨场景对比案例（dimension vs dynascheme）

### 案例 10 · 同样是"加自定义字段" · dimension 和 dynascheme 的差异

| 步骤 | dimension（CS-01） | dynascheme（dynascheme/CS-01） |
|---|---|---|
| modifyMeta add field | 同 · MulComboField/TextField/etc | 同 |
| 平台部署 | 同 | 同 |
| 字段对 HisModel 影响 | ❌ 无 HisModel · 加完直接生效 | ✅ HisModel · 字段每个版本独立保留 |
| audit 时新版本是否复制 ISV 字段值 | ❌ 不需要（dimension 没版本） | ✅ 自动复制（DynaAuthSchemeAuditOp 实证） |
| 反查 ISV 字段值的 SQL | `SELECT ftdkw_xxx FROM t_hrcs_dimension WHERE fid = ?` | `SELECT ftdkw_xxx FROM t_hrcs_dynascheme WHERE fboid = ? AND fiscurrentversion = '1'` |
| 客户感受 | 简单 · 想加就加 · 直接 BI 查 | 复杂 · 加完要测试跨版本读取 |

### 案例 11 · 同样是"audit 后置 BEC" · dimension 和 dynascheme 都标品 0 处发布

| 维度 | dimension | dynascheme |
|---|---|---|
| 标品 BEC 发布 | 0 处 | 0 处 |
| ISV 推荐发布 | CS-05 audit afterExecute | dynascheme/CS-05 同模式 |
| eventNumber | `${ISV_FLAG}_dimension_audited` | `${ISV_FLAG}_dynascheme_audited` |
| 业务变量 vars | dimensionId/number/name/datasource | schemeBoid/number/name/admingroup |

→ 套路完全一致 · 只是变量不同。**不能套用 hjm_jobhr 的 3 层异步派单模式**（hjm 是 OP→Service→*MsgTask · 但 hrcs 全应用都是直接 0 处发布 · 跨场景不能套用）。

---

## 四、AI 自动索引（骨架）

_跑一次 `scripts/search_historical_customizations.py --scenario hrcs_dimension` 可填这里_

| 客户 | 需求摘要 | GitLab 路径 | 时间 | 状态 |
|---|---|---|---|---|
| `<TODO>` | `<TODO>` | `<TODO>` | | |

→ 待 AI 索引脚本扫历史客户库 · 自动填充。

---

## 五、案例总结

| 案例 | 类型 | 关键 PR | 难度 |
|---|---|---|---|
| 1 · 总部 HR 配 enum dimension | 业务 | PR-007 / PR-006 | 易 |
| 2 · 区域 HR 配 basedata dimension | 业务 | PR-006 | 易 |
| 3 · 改 entry.value 故障 | 故障 | PR-008 / 标品 UI 锁定机制 | 复杂（需熟悉下游） |
| 4 · 加维度类目字段 | ISV CS-01 | PR-001 / PR-007 | 易 |
| 5 · datasource 自动带 entry | ISV CS-02 | PR-001/03/04 | 易 |
| 6 · save 同名拒绝 | ISV CS-03 save | PR-001/03/10 | 中 |
| 7 · disable 引用阻断 | ISV CS-03 disable | PR-001/08/09/10 | 中 |
| 8 · audit BEC 通知 BI | ISV CS-05 | PR-001/10/11 | 中 |
| 9 · 列表权限过滤 | ISV CS-07 | PR-001/02 | 易 |
| 10 · 字段加在 dim vs scheme 对比 | 跨场景 | HisModel 体感 | - |
| 11 · BEC 模式跨场景对比 | 跨场景 | PR-011 + bec_mode_per_scene_verify | - |

→ Claude 写代码前必读 · 这些案例覆盖了 dimension 大部分实战场景。
