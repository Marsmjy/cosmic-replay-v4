# 变更影响面 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于反编译实证 + 上下游表关系图谱
> **confidence**: verified
> **数据源**: 反编译 5 类 + scene_doc.json + opKey 链路 (2026-04-28)

---

## 一、改动本场景的影响象限

```
                    高频变动                低频变动
                    ────────                ──────
高影响 │   [A] save 触发 hrcs_roledimension  │  [B] delete 失误
       │       级联同步（每次保存都跑）        │      → 5+ 角色权限错乱
       │                                    │
低影响 │   [C] propname 显示                 │  [D] 加 ISV 字段
       │       （只前端缓存）                 │      （主表/子表新列）
       └────────────────────────────────────┴─────────────
```

---

## 二、改动本场景的下游影响图

| 改什么 | 影响下游 | 级联动作 | 风险等级 |
|---|---|---|---|
| **新增主记录（save · 一对象一映射）** | `hrcs_roledimension` 多个 roleDim 行（按 ismust=true 分录数 × 涉及角色数）<br>`hrcs_datarule` 不主动更新 · 下次访问时按新映射计算 | 实证 `EntityControlSaveOp.endOperationTransaction` L99-L146 同步 | 中 |
| **修改某行分录的 dimension** | `hrcs_roledimension` 该 propkey 在所有相关 role 的 entry 行 dimension 重置（先删旧 · 再加新） | 实证 `EntityControlSaveOp.deleteRowsPostProcessing` L148-L178 + `syncMustDimToRoleDim` | 中 |
| **修改某行分录的 ismust（false → true）** | 新增 hrcs_roledimension 行（实证 syncMustDimToRoleDim L196-L249）· 角色未配该维度时新建 buCaFunc 行 | 实证同上 | 中 |
| **修改某行分录的 ismust（true → false）** | hrcs_roledimension 中 enable=false 的对应行被移除（modify 类型）· 但角色绑定记录不删 | 实证同上 | 低（不影响其他角色） |
| **删除某行分录（deleteentry 编辑态）** | 后续 save 时 deleteRowsPostProcessing 会清 hrcs_roledimension 对应行 | 实证 L148-L178 | 中 |
| **删除主记录（delete）** | hrcs_roledimension 行（按 toDelDimRoleRanges 解码逐条删）<br>`hrcs_datarule` / `hrcs_dynaformctrl` 不级联清 · 留"幽灵引用"风险 | 实证 `EntityCtrlDelOp.beginOperationTransaction` L47-L70 | 高（孤儿数据） |
| **修改 entitytype 字段（编辑态）** | 前端清空所有分录 + 重新装载 propInfos · save 后 hrcs_roledimension 全删旧 entitytype 的角色行 | 实证 `EntityCtrlEdit.propertyChanged` L339-L352 + OP 删除联动 | 高（影响多个角色） |
| **修改 issyspreset = true → false（不允许）** | 反编译里没有放开 issyspreset 修改的入口 · 该字段 isvCanModify=false | 标品保护 | 0（改不了） |
| **加 ISV 自定义字段（modifyMeta）** | 主表多列 / 子表多列 | save / delete 主流程透明（标品 OP 不读不写 ISV 字段） | 低 |
| **替换 entitytype（删旧建新）** | 等价于"先 delete + 后 new" · 走完整下游清理 + 新建联动 | 风险叠加 | 高 |

---

## 三、跨模块联动（hrcs / hrpi / haos / hbpm 维度）

| 下游模块 | 触发点 | 同步/异步 | 失败策略 | 备注 |
|---|---|---|---|---|
| **hrcs · hrcs_roledimension** | save / delete 联动 | save：独立事务（异步语义）/ delete：同主事务（同步） | save 独立事务失败 → markRollback 回滚 hrcs_roledimension · 主事务正常提交（孤儿主记录） | 实证 `EntityControlSaveOp.syncMustDimToRoleDim` L251-L258 / `EntityCtrlDelOp.beginOperationTransaction` L47-L70 |
| **hrcs · hrcs_datarule** | 不直接联动 · 运行时计算 | 异步（每次访问数据规则时按当前 entityctrl + roledimension 算） | 不一致时数据规则失效 | 业务 SOP：改了 entityctrl 后用户立即重新进规则页面验证 |
| **hrcs · hrcs_dynaformctrl** | 反向引用（虚字段实体场景） | 同步（用户进 entityctrl 表单时实时查 hrcs_dynaformctrl） | 找不到则报错"请在虚字段数据控权配置中添加" | 实证 `EntityCtrlEdit.putDynaFormCtrlInfo` L368-L388 |
| **hrcs · hrcs_dimension** | 上游引用（不联动） | - | - | dimension 删除会让此映射变孤儿 · 但 dimension 自己有反查保护 |
| **bos / bos_entityobject** | 上游引用（不联动） | - | - | 业务对象删除会让此映射变孤儿 · 实证 `EntityCtrlTreeListPlugin.setFilter` L82 用 `entitytype.number is not null` 排除 |
| **bos / bos_user / hbp_devportal_bizapp** | 上游引用（不联动） | - | - | 应用删除会让此映射变孤儿 · 业务侧少见 |
| **haos / IHAOSStructProjectService** | 跨模块查询（F7 维度过滤） | 同步（每次维度 F7 时跨模块 RPC） | 失败时 otclassifyIds 为空 · 维度 F7 走默认路径 | 实证 `EntityCtrlEdit.queryEntityPropOtclassifyIds` L240-L255 |
| **bos / 权限服务（HRPermCacheMgr）** | save / delete 后清空全局缓存 | 同步（非事务） | 清空失败 · 下次 save 再清 | 实证 `EntityCtrlEdit.afterDoOperation` L292-L298 / `EntityControlSaveOp.endOperationTransaction` L144 / `EntityCtrlTreeListPlugin.afterDoOperation` L107-L109 |

---

## 四、ISV 改动后的二次影响

### 4.1 加 ISV 字段（CS-01）

- 主表多 1 列 / 子表多 1 列（ALTER TABLE）
- save / delete 主流程**透明** —— 标品 OP 不读不写 ISV 字段
- **不影响** hrcs_roledimension / hrcs_datarule / hrcs_dynaformctrl

### 4.2 加 ISV Validator（CS-03 / CS-04）

- save / delete 链 onAddValidators 阶段多 1 个 Validator
- **影响范围**：自建 Validator 拦截掉的 row 不入库 · 主事务回滚
- **不影响** hrcs_roledimension（拦截在事务前）

### 4.3 加 ISV BEC 发布（CS-05）

- save / delete 后 afterExecuteOperationTransaction 多发 1 条事件
- **影响范围**：BEC 订阅方收到事件 · 异步消费
- **本场景不影响**：BEC 是事务后 · 不影响主事务

### 4.4 加 ISV 列表过滤（CS-07）

- 列表 SQL 多 1 个 WHERE 子句
- **影响范围**：可见行变少 · 性能影响极小（如果 ISV 过滤字段有索引）
- **不影响** 数据本身

### 4.5 加 ISV TreeList 节点交互

- TreeList 树节点点击多 1 个监听
- **影响范围**：用户体验 · 数据不变

---

## 五、生产事故的可能场景

### 事故 A · save 时 hrcs_roledimension 独立事务失败导致主记录"幽灵化"

**触发**：
- 客户做了大批量映射保存（一次保存 100+ 业务对象 · 每个含 50+ ismust=true 分录）
- `EntityControlSaveOp.syncMustDimToRoleDim` 内部 `TX.requiresNew()` 独立事务超时回滚（hrcs_roledimension 写 5000+ 行）
- 主事务 commit 成功 · t_hrcs_entityctrl 落库 · 但角色没拿到对应维度

**症状**：
- 用户在 hrcs_entityctrl 看到映射存在
- 但访问业务对象做权限校验时 · 角色范围少了 ismust 维度 · 报"无权访问"

**修复**：
- 重新点保存触发 syncMustDimToRoleDim · 走"已存在则 modify · 不存在则 add" 自愈
- 或手动 SQL 把缺失的 hrcs_roledimension 行补上

### 事故 B · delete 主记录但下游 hrcs_datarule / hrcs_dynaformctrl 留孤儿

**触发**：
- 客户删除某业务对象的 entityctrl 映射
- 后续 hrcs_datarule 还在引用此 entitytype · 但 entityctrl 已不存在
- 数据规则计算时找不到映射 → 静默 skip 该业务对象（无错误提示）

**症状**：
- 业务方反映"配的数据规则没生效"
- 后台日志找不到错误

**修复**：
- 加 CS-04 自建 Validator · 阻止此类删除（前置防御）
- 后台脚本扫描 hrcs_datarule + hrcs_dynaformctrl 找孤儿引用 · 报警人工清理

### 事故 C · entitytype 字段被改导致角色权限错乱

**触发**：
- 用户编辑某条 entityctrl · 改 entitytype（A → B）
- 前端清空分录 + bizapp 自动改
- save 后 hrcs_roledimension 中老 entitytype A 的所有角色行被删（deleteRowsPostProcessing）
- B 的新角色行被加（syncMustDimToRoleDim）

**症状**：
- 原本对 A 有权限的角色突然没了 A 权限
- 大量角色访问 A 业务对象时报"无权访问"

**修复**：
- 业务 SOP：禁止改 entitytype · 要改就先 delete 再 new
- 加 CS-03 自建 Validator · 阻止编辑态修改 entitytype（save 前对比 originPropDimInfo）

---

## 六、改动影响总结矩阵

| 改动 | t_hrcs_entityctrl | t_hrcs_entitydimentry | hrcs_roledimension | hrcs_datarule | HRPermCacheMgr |
|---|---|---|---|---|---|
| 新建主记录 | ✅ INSERT | ✅ INSERT × N | ✅ INSERT × ismust | ❌ 不主动 | ✅ 清空 |
| 改主表字段 | ✅ UPDATE | ❌ | ❌（除非 entitytype 改） | ❌ | ✅ 清空 |
| 改某行 ismust（false→true） | ❌ | ✅ UPDATE 行 | ✅ INSERT 多角色行 | ❌ | ✅ 清空 |
| 改某行 ismust（true→false） | ❌ | ✅ UPDATE 行 | ✅ entry.removeIf 移行 | ❌ | ✅ 清空 |
| 改某行 dimension | ❌ | ✅ UPDATE 行 | ✅ DELETE 旧 + INSERT 新 | ❌ | ✅ 清空 |
| 改 entitytype | ✅ UPDATE | ✅ TRUNCATE 重建 | ✅ DELETE 老对象的所有行 + INSERT 新对象 | ❌ | ✅ 清空 |
| 删某行（deleteentry + save） | ❌ | ✅ DELETE 行 | ✅ DELETE 该 propkey 在所有 role 的行 | ❌ | ✅ 清空 |
| 删主记录 | ✅ DELETE | ✅ DELETE 级联 | ✅ DELETE 解码 toDelDimRoleRanges | ❌（孤儿） | ✅ 清空 |

⚠ **hrcs_datarule 是被动重算**：标品没主动通知 · ISV 想确保规则更新需要：(a) 改完后用户在规则页面手动 click 重算 · 或 (b) 加 CS-05 BEC 通知规则模块订阅刷新。
