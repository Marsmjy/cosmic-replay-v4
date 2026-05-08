# 异常与诊断 · 岗位信息维护 (hbpm_positionhr)

> **状态**: 🟢 基于 `cosmic_realworld_traps/` 真发坑位 + `_auto_operations.md` 本场景实抓校验规则 + 7 类 position 反编译整合
> **confidence**: real_deploy（每个异常都有 opKey / validationId / 插件类名证据）

---

## 一、业务层异常（岗位 + 时序模型相关）

### EX-01 · 行政组织为空报错

**症状**：保存时报 `字段"行政组织"为必填 · 请填写`

**原因**：`adminorg` 是 `scene_doc.json` L615 中唯一 `required: true` 的字段；save 链 `MustInput 6096194600001fac`（`_auto_operations.md` L112）强制检查。

**诊断**：
1. 查看表单是否隐藏了 `adminorg` 字段
2. 若是导入模板 · 确认模板列包含行政组织

**解决**：必须填 `adminorg` · 且该组织必须存在于 `haos_adminorghrf7` 视图

**铁律**：行政组织是岗位归属核心 · 绝对不能通过扩展元数据让它变成可选（违反 INV-01 + PR-001）

---

### EX-02 · 岗位成环校验失败

**症状**：保存时报 `岗位 [XX] 选择的上级岗位会导致行政汇报成环`

**原因**：`PositionHisLoopValidator.checkSysRel`（his_save 时激活）检测 `parent` 字段指向的上级链成环

**诊断**：
```sql
-- 查岗位树路径
WITH RECURSIVE pos_path AS (
    SELECT fid, fparentid, fname, 1 as depth
    FROM t_hbpm_position WHERE fid = <岗位 id>
    UNION ALL
    SELECT p.fid, p.fparentid, p.fname, pp.depth + 1
    FROM t_hbpm_position p JOIN pos_path pp ON p.fid = pp.fparentid
    WHERE pp.depth < 20
)
SELECT * FROM pos_path;
```

**解决**：
- 调整 parent · 选一个不形成环的上级
- 若批量导入 · 先导顶级岗位再导下级

**前端防护**：`PositionEdit.beforeF7Select` L932-L936 已 `QFilter("boid","!=",boid)` 禁选自身 · 但跨级成环仍需校验器兜底

---

### EX-03 · 岗位编码重复

**症状**：`编码已存在`

**原因**：`HisUniqueValidateOp`（save 链第 7 位）+ `CodeRuleOp`（save 链第 1 位）联合校验

**诊断**：
```sql
SELECT fid, fnumber, fboid FROM t_hbpm_position WHERE fnumber = '<重复编码>';
```

**解决**：
- 改编码 · 或让 `CodeRuleOp` 自动生成
- 如业务要求"按行政组织隔离"· 配编码规则基础资料（走 PR-006）
- 如需自定义前缀（如按 positiontype）· 走 CS-04

**注意**：`number` 字段创建后**业务不应修改**（INV-06）· 下游 27 处引用（hrpi_empposorgrel 等虽按 boid · 但展示层可能缓存 number）

---

### EX-04 · 禁用岗位被下游引用（最容易忽略）

**症状**：禁用后 · 员工档案出现"岗位已禁用"告警 · 但禁用操作本身**成功**

**原因**：标品 `PositionHrDisableOp.onAddValidators` **是空**（`PositionHrDisableOp.java` L49-L50）· 标品业务阻断在 `afterExecuteOperationTransaction` 的 `IBosPositionService.disablePositions` 内部 · 此阻断不反查 27 处下游引用

**诊断**：
```sql
-- 查下游 5 个 Tier 1 任职关系类实体
SELECT COUNT(*) FROM t_hrpi_empposorgrel WHERE fpositionid = ? AND fiscurrentversion = 1;
SELECT COUNT(*) FROM t_hrpi_empjobrel WHERE fpositionid = ? AND fiscurrentversion = 1;
SELECT COUNT(*) FROM t_hrpi_rotationinfo WHERE fpositionid = ?;
SELECT COUNT(*) FROM t_hrpi_dispatchinfo WHERE fpositionid = ?;
SELECT COUNT(*) FROM t_hrpi_appointremoverel WHERE fpositionid = ?;
```

**解决**：
- 标品做法：先手工转移关联的员工 / 任职关系 · 再禁用
- 推荐做法：加 CS-03 前置 Validator · 有引用时**连事务都进不了**
- 业务上更推荐 "停招" 自定义状态 · 而不是硬禁用

---

### EX-05 · 系统预置岗位不能删除

**症状**：删除报 `系统预置数据不能删除`

**原因**：`scene_doc.json` L270-L282 `issyspreset` 字段 red 禁区 + 岗位无 `delete` opKey · 只有软删除 `isdeleted=1`

**诊断**：
```sql
SELECT fid, fnumber, fissyspreset FROM t_hbpm_position WHERE fid = ?;
```

**解决**：
- 系统预置岗位只能禁用 · 不能删除
- 如果是测试数据误建成预置 · 联系苍穹平台支持
- 正常测试数据清理走 `disable` + `isdeleted=1`

---

### EX-06 · 时序版本唯一性冲突

**症状**：保存时报 `同一 boid 同一时间段已有生效数据`

**原因**：`HisUniqueValidateOp`（save 链第 7 位 / confirmchange 链第 2 位）校验：同一 boid 的多版本间 `[bsed, bsled]` 不允许重叠

**诊断**：
```sql
SELECT fid, fbsed, fbsled, fhisversion FROM t_hbpm_position WHERE fboid = ? ORDER BY fbsed;
```

**解决**：
- 调整新版本的 `bsed` 不要和旧版本重叠
- 如确实要替换某版本 · 用 `revise` 修订（就地改 · INV-07）而非 `change` 变更（产新版）

**设计原理**：`PositionHisSaveOp.calcBsledByNextBsed` 会按下一版 bsed - 1 天自动算本版 bsled · 严格连续

---

### EX-07 · 时序字段被手改导致版本链断

**症状**：岗位的历史版本显示混乱 · 同一 boid 有多条 `iscurrentversion=true`

**原因**：自定义插件或 SQL 直接修改了 `boid` / `iscurrentversion` / `datastatus` / `hisversion` / `sourcevid` / `firstbsed`

**诊断**：对比数据库和 `scene_doc.json` 时序字段的 `autoComputed: true` 语义

**解决**：
- 立即停止任何自定义时序字段写入
- 让标品 `HisModelOPCommonPlugin` / `BdVersionSaveServicePlugin` 自动维护
- 如版本链已断 · 需从备份恢复（参考 `CASE-06` 事故）

**预防**：建立 ISV 代码静态扫描 · `entity.set("iscurrentversion"` / `entity.set("boid"` 等调用出现即告警

---

### EX-08 · 启用失败但无错误信息

**症状**：点击启用后无反馈 · 状态未变

**原因**：`PositionHrEnableOp.onAddValidators` **是空**（标品无启用前校验）· 但 `beginOperationTransaction` 调用失败未抛异常

**诊断**：
- 查看浏览器 Network 返回
- 查看操作日志（默认 disabled · 需先 enable `HRBaseDataLogOp`）
- 看 `PositionHrEnableOp.beforeExecuteOperationTransaction` 的 boid → latestHisId 映射是否成功

**解决**：
- 检查是否重复启用（`enable` 字段已经是 1）
- 检查上级岗位（`parent`）是否启用
- 检查关联的 positiontype / positiontpl 是否启用

**扩展建议**：加自定义 Validator 在 `onAddValidators@enable` 并列挂 · 明确校验前置条件

---

### EX-09 · 汇报关系确认变更报必填

**症状**：`dochangerelation` 报 `4T7SDY=13RY6 MustInput` 失败

**原因**：汇报关系变更需要填变更说明（`changedescription`）· `_auto_operations.md` L442 validation enabled

**解决**：填入变更说明 · 再点确认变更

---

## 二、元数据扩展异常（Week 5 真发坑位 · 实测证据）

### EX-10 · buildMeta 返回 errorCode=0 但实际失败

**症状**：调 `buildMeta` 返回 `{errorCode: "0"}` · 但 `getFormSchema` 查不到新建的实体。

**原因**：苍穹响应 `errorCode="0"` 只表示 HTTP 层成功 · 不代表业务落库。

**解决（硬规则）**：
```
每次 buildMeta / modifyMeta 调用后 · 必须二次验证:
  response = modifyMeta(formId="hbpm_positionhr", ops=[...])
  if response.errorCode == "0":
      assert getFormSchema("hbpm_positionhr").fields 包含 新字段 key, "实际未落库"
```

**预防**：已落地到 `scripts/cosmic_preflight.py`

---

### EX-11 · EmployeeField 不在 buildMeta 枚举

**症状**：`buildMeta` 时传 `fieldType: "EmployeeField"` · 返回 "不支持的字段类型"。

**原因**：`EmployeeField` 是 HR SDK 扩展类型 · **OpenAPI 的 74 值枚举里不存在**。

**解决**：用 `BasedataField` + `basedataNumber: "hrpi_employeenewf7query"` 替代：
```json
{
  "fieldType": "BasedataField",
  "basedataNumber": "hrpi_employeenewf7query",
  "fieldKey": "${ISV_FLAG}_posowner"
}
```

**本场景特别注意**：`disabler`（禁用人）/ `enabler`（启用人）已经用 `UserField` 而非 `EmployeeField` · 但如果业务要加"岗位负责人"字段 · 一定要用 BasedataField + basedataNumber。

---

### EX-12 · 下拉字段漏传 comboOptions

**症状**：新建 `ComboField` 后 · UI 上下拉框为空。

**原因**：5 种下拉类型都必须带 `comboOptions`。

**解决**：
```json
{
  "fieldType": "ComboField",
  "key": "${ISV_FLAG}_recruitstatus",
  "comboOptions": [
    {"value": "A", "name": {"zh_CN": "招聘中", "en_US": "Recruiting"}},
    {"value": "B", "name": {"zh_CN": "冻结", "en_US": "Frozen"}},
    {"value": "C", "name": {"zh_CN": "储备", "en_US": "Reserved"}},
    {"value": "D", "name": {"zh_CN": "已关闭", "en_US": "Closed"}}
  ]
}
```

**本场景参考**：`scene_doc.json` 中 `isstandardpos` / `initdatasource` / `oristatus` / `datastatus` 4 个 ComboField 都带 options

---

### EX-13 · 字段类型拼写别名坑 ⭐ 岗位 10 个多语言字段

**症状**：`fieldType: "MultiLangTextField"` → 静默失败 · 新建字段变成普通 TextField。

**原因**：苍穹官方字段类型拼写有 Bug：
- ✅ 正确：`MuliLangTextField`（Muli 是 Multi 拼写错）
- ❌ 错误：`MultiLangTextField`

**本场景证据**：`scene_doc.json` 10 个多语言字段（name / simplename / description / posduty / posstandard / posorientation / knowledgereq / skillreq / abilityreq / experiencereq / agereq / oriname）都是这个拼写

**本场景特别提醒**：岗位多语言字段**直接存主表** · 没有 `_l` 子表 · ISV 扩展多语言字段时 · modifyMeta add 会直接加到 `t_hbpm_position` · 无需预留子表容量

**预防**：`field_types_authoritative.py` 提供 74 值权威枚举 + 别名映射

---

### EX-14 · modifyMeta 用 formNumber 失败

**症状**：`modifyMeta` 传 `formNumber: "hbpm_positionhr"` · 返回 "参数错误"。

**原因**：仅 `modifyMeta` 接口参数叫 **`formId`**（实际传 formNumber 值）· 其他接口都叫 `formNumber`。

**解决**：
```json
// ✅ 对
{"formId": "hbpm_positionhr", "ops": [...]}
// ❌ 错
{"formNumber": "hbpm_positionhr", "ops": [...]}
```

**本场景特别**：scene_doc.json L5 `formIdTechnical: "/IJP/IQGX57W"` 是内部 ID · **不要**用它做 formId 参数 · 要用 `hbpm_positionhr`

---

### EX-15 · modifyMeta op 枚举非 4 值静默失败

**症状**：`modifyMeta` 传 `op: "update"` 或 `op: "addField"` · 请求成功但没效果。

**原因**：苍穹仅识别 4 值：`add` / `modify` / `remove` / `move`。

**解决**：只能用这 4 个枚举值

---

### EX-16 · addRule 的 ActionType 小写被忽略

**症状**：`addRule` 返回成功 · 但规则不生效。

**原因**：`ActionType` 和 `FieldKey` 必须 **PascalCase**：
```json
// ✅ 对
{"actionType": "Lock", "fieldKey": "Adminorg"}
// ❌ 错
{"actionType": "lock", "fieldKey": "adminorg"}
```

**本场景特别**：scene_doc.json 里的字段 key 都是小写（`adminorg` / `number` / `name`）· 但调 addRule 的时候 `fieldKey` 参数里要转首字母大写 `Adminorg` / `Number` / `Name`

**本场景当前状态**：`_auto_rules.md` 显示 formRule=0 / bizRule=0 · 业务规则都是 Java 插件实现 · 所以 addRule 在本场景用得少

---

### EX-17 · addRule 的 preCondition 语法严格

**症状**：`preCondition: "adminorg == ''"` 规则不触发。

**原因**：公式引擎只支持有限语法：
- ✅ 允许：`==` · `>` · `<` · `>=` · `<=` · 字符串常量 `'xxx'`
- ❌ 禁用：`== ''` · `== null` · `||` · `&&`

**解决**：改用 Java 插件（如本场景 `PositionHrSaveOp.onAddValidators` 并列注册自定义 Validator · 参考 CS-03）

---

### EX-18 · addRule 坏规则删除死锁

**症状**：调 `deleteRule` 删坏规则 · 返回"规则校验失败无法删除"。

**原因**：苍穹要求**规则必须合法才能删除**（双重绑定）· 坏规则陷入死锁。

**解决**：先 `updateRule` 把规则改成合法（如 triggerField 改成有效字段 `adminorg`）· 再 `deleteRule`

---

### EX-19 · registerPlugin targetType 大小写错误

**症状**：`registerPlugin` 返回成功 · 但插件不生效。

**原因**：`targetType` 必须**大写枚举** · 5 个合法值：
- `BILL_FORM` / `LIST_FORM` / `MOBILE_BILL_FORM` / `OPERATION` / `EVENT`

**本场景典型**：
- 前端字段联动（如 CS-02） → `BILL_FORM`
- 列表扩展（继承 `HRDataBaseList`） → `LIST_FORM`
- 操作扩展（如 CS-03 / CS-04 / CS-05） → `OPERATION`
- BEC 订阅方 → `EVENT`

---

### EX-20 · updateOperation.plugins 全量替换陷阱 ⭐ 本场景最易踩

**症状**：调 `updateOperation` 新增一个插件 · 结果 save 链 9 标品插件全丢。

**原因**：`updateOperation.plugins` 是**全量替换**语义 · 不是 append。

**本场景最容易踩**：
- save 链有 9 个标品插件（`_auto_operations.md` L94-L107）
- his_save 链有 1 个插件 + 3 个 Validator（JobLevelGradeRange / PositionHisLoop / PositionHisValidator）
- enable / disable 各 2 个
- confirmchange 3 个
- 每个 opKey 都不同 · 必须分别 get

**解决**：
```python
# 正确做法 · 先 get · 合并后再 update
current = client.get_operation(formId="hbpm_positionhr", opKey="save")
current.plugins.append(new_plugin)
client.update_operation(plugins=current.plugins)
```

**同理**：`validations` 也是全量替换（save 有 1 条 · his_save 有 3 Validator · confirmchange 有 1 条）

---

### EX-21 · 插件 Java 类路径错误

**症状**：`registerPlugin` 后运行时 `ClassNotFoundException`。

**原因**：Java 类没打进 jar 或包名错。

**诊断**：
```bash
# 检查 jar 内
jar tf hrmp-${ISV_FLAG}_hbpm_ext.jar | grep YourPlugin
```

**解决**：确认 `className` 写的是**全限定名**（如 `${ISV_FLAG}.hrmp.hbpm.opplugin.position.TdkwPositionNumberOp`）· 检查 jar 打包

**本场景参考**：标品 `PositionHrSaveOp` 的 className 是 `kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp` · 扩展插件参考此命名风格

---

### EX-22 · 扩展插件继承了 PositionHrSaveOp / PositionHrDisableOp ⭐ 违反 PR-001

**症状**：
- 编译期：标品升级后父类方法签名变化 · ISV 代码编译失败
- 运行时：super 调用版本不一致导致逻辑错位

**原因**：违反 PR-001（ISV 不继承标品场景专属类）

**解决**：
- 不继承 `PositionHrSaveOp` / `PositionHrEnableOp` / `PositionHrDisableOp` / `PositionHrChangeOp` / `PositionHrRelationChangeOp` / `PositionHisSaveOp` / `PositionHrCommonOp` / `PositionEdit` / `PositionList`
- 继承 `HRDataBaseOp` / `HRDataBaseEdit` / `HRDataBaseList` / `AbstractValidator`（HR 域标准白名单）
- **并列挂**到目标 opKey

**证据**：7 类反编译文件 `PositionHr*Op.java` 都不是 `@SdkPublic` 类 · ISV 继承视同内部 API（违反 cosmic_sdk_annotation_whitelist.md）

---

### EX-23 · FormPlugin setValue 死循环 ⭐ PR-004

**症状**：加 `propertyChanged` 监听 · 浏览器卡死 · 前端报栈溢出

**原因**：`propertyChanged` 里直接 `setValue` 会再次触发 `propertyChanged`（因为 setValue 本身是"字段变更"事件）

**解决**：`beginInit` / `endInit` 包裹 + `updateView` 刷新（遵循 PR-004）
```java
getModel().beginInit();
getModel().setValue("lowjoblevel", levelId);
getModel().setValue("highjoblevel", highId);
getModel().endInit();
getView().updateView("lowjoblevel");
getView().updateView("highjoblevel");
```

**本场景特别**：CS-02 的"选岗位类型自动带出"就是经典踩坑点

---

### EX-24 · OP 里调 getModel().setValue → NPE ⭐ PR-003

**症状**：OP 插件 `beforeExecuteOperationTransaction` 里写 `getModel().setValue("number", x)` · NPE

**原因**：OP 插件运行在后端 · 没有 `getModel()`（model 是前端概念）

**解决**：OP 用 `entity.set(key, value)`（entity 从 `args.getDataEntities()[i]` 拿）· FormPlugin 才用 `getModel().setValue`

**遵循 PR-003**：分层清楚 · OP 用 entity.set · FormPlugin 用 getModel().setValue

---

## 三、时序模型特有异常

### EX-30 · newhisversion 之后找不到新版本

**症状**：调 `newhisversion` 后 · 新版本数据没出现

**原因**：`newhisversion` 是 `donothing` 类型（`_auto_operations.md` L336-L341）· 仅产生新版本 DataEntity · **需要再走 `save` 或 `confirmchange` 才真正入库**

**正确流程**：
```
newhisversion → 产生新版本内存对象（含新 bsed）
   ↓
用户填写新字段值
   ↓
save / confirmchange → 入库
```

---

### EX-31 · confirmchange 时 MustInput 失败

**症状**：`confirmchange` 报 `4UXPTMTC=NCR` MustInput 失败

**原因**：确认变更也要满足必填（`_auto_operations.md` L386）· `adminorg` 是必填

**解决**：填入完整的字段后再 confirmchange

---

### EX-32 · his_save 的 3 Validator 之一失败

**症状**：历史保存时报：
- "职级职等范围不在方案内"（`JobLevelGradeRangeImportValidator`）
- "岗位行政汇报成环"（`PositionHisLoopValidator.checkSysRel`）
- "版本间 bsled 不严格连续"（`PositionHisValidator.validateBsedAndBsledRelationship`）

**原因**：his_save 注册的 3 Validator 对时序数据完整性严格校验（`PositionHisSaveOp.onAddValidators` L60-L64）

**诊断**：
```sql
-- 查岗位所有版本
SELECT fid, fbsed, fbsled, fhisversion, fparentid, fadminorgid 
FROM t_hbpm_position WHERE fboid = ? ORDER BY fbsed;
```

**解决**：
- 职级职等：检查 `jobscm` / `joblevelscm` / `jobgradescm` 和 low/high 职级职等的关联合法性
- 成环：调整 parent 指向
- 版本连续：按 `nextBsed - 1` 调整 bsled

---

### EX-33 · 批量导入岗位后不发消息 ⭐

**症状**：批量导入 500 岗位 · 下游岗位缓存没更新

**原因**：`PositionHrCommonOp.afterExecuteOperationTransaction` 有 `!isImport()` 判断 · 导入场景**跳过** `ChangeMsgServiceImpl.sendMsg()`

**解决**：
- 导入后手动调 `IBosPositionService.addOrUpdatePositions(boids)` 刷下游
- 或业务上接受"导入即不发消息"的约定 · 导入完统一触发一次 refresh

**遵循 PR-010**：ISV 扩展插件 `afterExecuteOperationTransaction` 时 · 要**照抄** `isImport()` 判断 · 否则导入时 ISV 逻辑每条都触发 · 性能灾难

---

### EX-34 · 导入 hbpm_positionhr 模板字段不全

**症状**：用 `importdata_hr` 导入 · 新扩展字段没出现在模板

**原因**：HIES 导入走 `cusstartpage: hismodel_importstart`（`_auto_operations.md` L291）· 模板定义在自定义页面

**解决**：
- 扩展字段后重新生成导入模板
- 或在 `hismodel_importstart` 页面手动配置导入列

---

## 四、消息 / 事件层特有异常

### EX-40 · ISV 扩展直接复用 ChangeMsgServiceImpl

**症状**：ISV 扩展想自建"岗位变更通知" · 直接调 `ChangeMsgServiceImpl.sendMsg()`

**问题**：
- `ChangeMsgServiceImpl` 是**标品内部**调度任务机制 · 走 `sch_task jobId "5/2/X9QCCFNS"` + `hbpm_position_msgdetail`
- 复用会污染标品消息流
- 标品升级可能改实现 · ISV 代码绑死内部类

**解决**：改走苍穹 BEC（遵循 PR-011）
```java
IEventService svc = ServiceHelper.getService(IEventService.class);
svc.triggerEventSubscribeJobs("hbpm", "HBPM_POSITION_CHANGED", "岗位变更", variables);
```

参考 CS-05 完整方案

---

### EX-41 · BEC 事件号未预配置

**症状**：调 `IEventService.triggerEventSubscribeJobs` 返回成功 · 但订阅方没收到事件

**原因**：`eventNumber` 必须先在【苍穹开发平台】→【业务事件管理】预配置 · 未注册的事件号 BEC 不识别

**解决**：
1. 打开【苍穹开发平台】→【业务事件管理】
2. 新增事件：编号 = `HBPM_POSITION_CHANGED` · source = `hbpm`
3. 重新触发

---

### EX-42 · BEC variables 塞了完整 DynamicObject

**症状**：调 BEC 后订阅方超时 · 或消息体大小报错

**原因**：`variables` 参数只接受简单类型 Map · 不应塞完整 DynamicObject（可能含数十个字段 + 嵌套对象）

**解决**：只传关键字段
```java
Map<String, Object> variables = new HashMap<>();
variables.put("positionBoid", boid);
variables.put("hisversion", after.getString("hisversion"));
variables.put("changedFields", Arrays.asList("lowjoblevel", "highjoblevel"));  // 字段名列表
// ❌ 不要这样: variables.put("position", after)
```

---

### EX-43 · 事件在 endOperationTransaction 发 → 事务回滚产生脏事件

**症状**：BEC 事件发布了 · 但主事务回滚 · 下游已处理但业务数据不存在

**原因**：违反 PR-010 · 事件发在 `endOperationTransaction`（事务**可能**还未最终提交）· 回滚时事件已扩散

**解决**：必须在 `afterExecuteOperationTransaction` 发事件（主事务已提交 · 安全）
```java
@Override
public void afterExecuteOperationTransaction(AfterOperationArgs e) {
    // ✅ 主事务已提交 · 发事件安全
    IEventService svc = ServiceHelper.getService(IEventService.class);
    svc.triggerEventSubscribeJobs(...);
}
```

---

## 五、诊断工具清单

| 工具 | 作用 |
|---|---|
| `cosmic-env devportal open` | 打开 designer 看实际元数据 state |
| `getFormSchema("hbpm_positionhr")` | 二次验证 buildMeta/modifyMeta 是否落库 |
| `getOperation(formId="hbpm_positionhr", opKey="save")` | 二次验证 updateOperation 是否落库 |
| `list_rules(entity="hbpm_positionhr")` | 二次验证 addRule 是否生效（本场景当前 0 条规则） |
| `scripts/cosmic_preflight.py` | 提交前自动预检 |
| `payload_adapter.validate_payload` | 参数规范化 + 硬约束校验 |
| 数据库直查（岗位时序） | `SELECT fid, fnumber, fboid, fbsed, fiscurrentversion, fhisversion, fparentid, fadminorgid FROM t_hbpm_position WHERE fboid = ? ORDER BY fbsed` |
| 数据库直查（下游引用） | `SELECT COUNT(*) FROM t_hrpi_empposorgrel WHERE fpositionid = ? AND fiscurrentversion = 1` |
| 岗位树遍历 | 用 WITH RECURSIVE CTE 查 parent 链（查成环 / 深度） |

---

## 六、通用铁律（11 PR 投影到本场景）

### 铁律 1 · PR-001：不继承标品场景专属类

- ❌ extends PositionHrSaveOp / PositionHrEnableOp / PositionHrDisableOp / PositionHrChangeOp / PositionHrRelationChangeOp / PositionHisSaveOp / PositionHrCommonOp / PositionEdit / PositionList
- ✅ extends HRDataBaseOp / HRDataBaseEdit / HRDataBaseList / AbstractValidator
- **并列挂**到目标 opKey · 不覆盖

### 铁律 2 · PR-002：RowKey 执行顺序

- CodeRuleOp 必须 RowKey=0（最早生成编码）
- PositionHrSaveOp 在 save 链第 9 位（最后补业务）
- 自定义编码插件（CS-04）RowKey=0 · 在 CodeRuleOp 之前
- 自定义禁用校验（CS-03）在 onAddValidators 阶段（最早）

### 铁律 3 · PR-003：FormPlugin 用 getModel().setValue · OP 用 entity.set

- OP 在后端 · 没有 model · `getModel()` 返回 null · NPE
- FormPlugin 在前端 · 走 IDataModel

### 铁律 4 · PR-004：setValue 死循环防护

- propertyChanged 里 setValue 必用 beginInit / endInit 包裹 + updateView 刷新

### 铁律 5 · PR-005：ID 生成用 kd.bos.id.ID

- 自定义编码 / 分布式序号 · 用 `kd.bos.id.ID.genLongId()` / `genStringId()`
- 不要自建 Redis 或 DB 唯一约束

### 铁律 6 · PR-006：CodeRuleOp 先配置再代码

- 能走编码规则基础资料配置的 · 不要写代码
- 只有配置不满足（如动态按 positiontype 切换规则）才考虑 CS-04

### 铁律 7 · PR-007：预置数据编码不可改 · 业务数据可改

- 不要绝对化"number 创建后永远不可改"
- 正确：`issyspreset=true` 的不可改 · 业务岗位技术可改但下游引用多 · 强烈不建议

### 铁律 8 · PR-008：时序当前版本查询用 iscurrentversion=true

- 不能用 bsed ≤ now ≤ bsled 替代
- 查下游引用时 · 下游也多是时序资料 · 也要带过滤

### 铁律 9 · PR-009：下游引用用 boid · 不用 id

- id 是版本维度 · boid 是业务维度
- 下游表存的一般是 boid · 用 id 查 → 漏查历史版本

### 铁律 10 · PR-010：OP 生命周期 13 方法

- 加校验：`onAddValidators` 最早阶段
- 进事务前阻断：`beforeExecuteOperationTransaction` · 抛 KDBizException 或 addErrorMessage
- 发业务事件：`afterExecuteOperationTransaction` · 主事务已提交安全

### 铁律 11 · PR-011：跨模块事件走 BEC · 不自建 MQ

- 用 `IEventService.triggerEventSubscribeJobs`
- eventNumber 先在开发平台预配置
- variables 只传关键字段 · 不塞完整 DynamicObject

### 本场景特有铁律

### 铁律 本-1：时序 6 字段禁改

- `boid` / `iscurrentversion` / `datastatus` / `firstbsed` / `hisversion` / `sourcevid`
- 6 字段都 `autoComputed: true` + `minefield: red`（`scene_doc.json`）
- 改了就是脏数据 · 时序链断（参考 CASE-06）

### 铁律 本-2：变动三分类禁手改

- `changetype` / `changeoperate` / `changescene` 由各 `PositionHr*Op.beginOperationTransaction` 自动覆盖
- 手改无效 · 徒劳

### 铁律 本-3：出厂字段禁改

- `orinumber` / `oriname` / `oristatus`
- 改了破坏 `HRBaseOriginalOp` 出厂值对比逻辑

### 铁律 本-4：软删除字段禁手改

- `isdeleted` 由系统维护
- 列表默认 `isdeleted != 1` 过滤

### 铁律 本-5：adminorg 编辑态不可改

- `PositionEdit.setControlVisibleEanbleWhenEdit` L383 已 setEnable(FALSE) 保护
- 要换组织必须走 `change` + `confirmchange` 新版本

### 铁律 本-6：多语言字段直接存主表

- 本场景无 `_l` / `_i` 子表
- ISV 扩展 `MuliLangTextField` 直接落 `t_hbpm_position`
- 注意主表列数上限

### 铁律 本-7：parent 成环必校验

- `PositionHisLoopValidator` 只在 `his_save` 激活
- 前端 F7 自排除自身 · 但跨级成环仍需校验器

---

## 七、常见组合 trap 快速查表

| 报错 / 症状 | 可能原因 | 解决思路 |
|---|---|---|
| `行政组织必填` | INV-01 / `6096194600001fac` | 填 `adminorg` |
| `岗位编码重复` | `HisUniqueValidateOp` + CodeRuleOp | 改编码 / 改唯一性策略 |
| `系统预置不能删除` | 无 delete opKey + `issyspreset` | 只能禁用 |
| `同一 boid 时间段重叠` | `HisUniqueValidateOp` | 调整 bsed |
| `岗位成环` | `PositionHisLoopValidator` | 调整 parent |
| 启用无反应 | `PositionHrEnableOp.onAddValidators` 空 | 查日志 / 检查 enable 字段 |
| 禁用后下游告警 | `PositionHrDisableOp.onAddValidators` 空 | 加 CS-03 前置 Validator |
| save 后 9 标品插件全丢 | `updateOperation` 全量替换 | 先 get 再 append |
| 加字段 UI 看不见 | errorCode=0 但未落库 | `getFormSchema` 验证 |
| ComboField 空 | 漏 `comboOptions` | 补全枚举 |
| 规则不触发 | `actionType` 小写 | 改 PascalCase |
| 插件不执行 | className 错 / targetType 小写 | 检查全限定名 + 5 枚举 |
| OP setValue NPE | 违反 PR-003 | 改 entity.set |
| propertyChanged 死循环 | 违反 PR-004 | beginInit/endInit 包裹 |
| BEC 事件订阅方收不到 | eventNumber 未预配 | 开发平台注册事件号 |
| 批量导入不发消息 | isImport() 分支跳过 | 导入后手动 refresh |
| 继承 PositionHrSaveOp 升级失败 | 违反 PR-001 | 改成并列挂 HRDataBaseOp |

---

## 八、来源追溯

- EX-01 ~ EX-09 业务层：`_auto_operations.md` 各 opKey validations + `scene_doc.json` minefield + 7 类反编译
- EX-10 ~ EX-24 元数据层：`cosmic_realworld_traps/buildmeta_traps.md` / `modifymeta_traps.md` / `addrule_traps.md` / `platform/openapi_capability_map.md` + PR-001 ~ PR-011
- EX-30 ~ EX-34 时序层：`_auto_operations.md` L336-L341 newhisversion / L375-L387 confirmchange / L463-L473 his_save
- EX-40 ~ EX-43 消息/事件层：`ChangeMsgServiceImpl.java` L40-L85 + `bos-mservice-bec-api-8.0.jar` + PR-010 / PR-011
- 诊断 SQL：`scene_doc.json` physicalTable + physicalColumn 实抓
- 铁律 PR-xxx：`knowledge/_shared/platform_rules.json`
- 本场景特有铁律：7 类反编译 + scene_doc.json minefield + `PositionEdit.setControlVisibleEanbleWhenEdit` L383
