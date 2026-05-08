# 异常与诊断 · 职位体系维护 (hbjm_jobhr)

> **状态**: 🟢 基于 `cosmic_realworld_traps/` 真发坑位 + `_auto_operations.md` 本场景实抓校验规则整合
> **confidence**: real_deploy（每个异常都有 opKey / validationId / 插件类名证据）

---

## 一、业务层异常（职位 + 时序模型相关）

### EX-01 · 职位序列为空报错

**症状**：保存时报 `字段"职位序列"为必填，请填写`

**原因**：`jobseq` 是 `scene_doc.json` L664 中唯一 `required: true` 的字段；`save` 链第 1 校验 `MustInput 6096194600001fac`（`_auto_operations.md` L117）强制检查。

**诊断**：
1. 查看表单是否隐藏了 `jobseq` 字段
2. 若是导入模板，确认模板列包含职位序列

**解决**：必须填 `jobseq`，且该序列必须存在于 `hbjm_jobseqhr` 基础资料

---

### EX-02 · 生效日期不能填写未来

**症状**：保存时报 `生效日期不能填写未来`

**原因**：`save` 链校验 `FormValidate 2M42D=Y0/0XK` enabled（`_auto_operations.md` L122）

**诊断**：`bsed` 字段值 > 当前系统日期

**解决**：
- 如确实需要未来生效 → 走 `newhisversion` opKey 新增数据版本（而不是直接 save）
- 或者等到生效日期当天再保存

---

### EX-03 · 职位编码重复

**症状**：`编码已存在`

**原因**：`save` 链校验 `GroupFieldUnique 2=K9URZCEUUS`（编码唯一性，enabled，`_auto_operations.md` L121）

**诊断**：
```sql
SELECT fid, fnumber FROM t_hbjm_job WHERE fnumber = '<重复编码>';
```

**解决**：
- 改编码，或让 `CodeRuleOp` 自动生成
- 如业务要求"按创建组织隔离"，启用 `GroupFieldUnique 0+/SL/MZ=VJB`（默认 disabled）

**注意**：`number` 字段创建后**不可修改**（INV-08）；只能改新建记录的 number，不能改已有记录的。

---

### EX-04 · 禁用职位被下游引用

**症状**：禁用后，下游岗位 / 人员出现"职位已禁用"告警

**原因**：标品 `JobHrDisableOp` 无业务引用检查（`_auto_operations.md` L210 disable 链第 3 位），只修改 `enable` 字段

**诊断**：
```sql
SELECT COUNT(*) FROM hbpm_position WHERE jobid = ?;
SELECT COUNT(*) FROM hrpi_pemployee WHERE jobid = ?;
```

**解决**：
- 先转移引用的岗位 / 员工到其他职位
- 推荐：加 CS-04 禁用前检查插件（参考 `CASE-03` 事故）
- 业务上更推荐"停招"状态而非硬禁用

---

### EX-05 · 系统预置职位不能删除

**症状**：`hrbddeletevalidator` 报 `系统预置数据不能删除`

**原因**：`scene_doc.json` L278-L289 `issyspreset` 字段 red 禁区；`delete` 链的 `hrbddeletevalidator 2+U=J7R7IEF/` enabled（`_auto_operations.md` L148）

**诊断**：
```sql
SELECT fid, fnumber, fissyspreset FROM t_hbjm_job WHERE fid = ?;
```

**解决**：
- 系统预置职位只能禁用，不能删除
- 如果是测试数据误建成预置，联系苍穹平台支持

---

### EX-06 · 时序版本唯一性冲突

**症状**：保存时报 `同一 boid 同一时间段已有生效数据`

**原因**：`HisUniqueValidateOp`（save 链第 8 位 / confirmchange 链第 2 位）校验：同一 boid 的多版本间 `[bsed, bsled]` 不允许重叠

**诊断**：
```sql
SELECT fid, fbsed, fbsled, fhisversion FROM t_hbjm_job WHERE fboid = ? ORDER BY fbsed;
```

**解决**：
- 调整新版本的 `bsed` 不要和旧版本重叠
- 如确实要替换某版本，用 `revise` 修订（就地改）而非 `change` 变更（产新版）

---

### EX-07 · 时序字段被手改导致版本链断

**症状**：职位的历史版本显示混乱，`iscurrentversion` 多条为 true

**原因**：自定义插件或 SQL 直接修改了 `boid` / `iscurrentversion` / `datastatus` / `firstbsed` / `sourcevid` / `hisversion`

**诊断**：对比数据库和 `scene_doc.json` L378-L497 6 个时序字段的 `autoComputed: true` 语义

**解决**：
- 立即停止任何自定义时序字段写入
- 让标品 `HisModelOPCommonPlugin` / `BdVersionSaveServicePlugin` 自动维护
- 如版本链已断，需从备份恢复（参考 `CASE-06` 事故）

---

### EX-08 · 启用失败但无错误信息

**症状**：点击启用后无反馈，状态未变

**原因**：`JobEnableValidator`（`rules_chain_all.json` L678-L688）校验失败，但错误信息未弹出

**诊断**：
- 查看浏览器控制台 network 返回
- 查看操作日志（`HRBaseDataLogOp`）
- 看 `JobHrEnableOp.beforeExecuteOperationTransaction` 读的 `boid` / `enable` 是否已是启用态

**解决**：
- 检查是否重复启用（`enable` 已经是 1）
- 检查是否关联的职位序列 / 职位族已禁用

---

## 二、元数据扩展异常（Week 5 真发坑位，实测证据）

### EX-10 · buildMeta 返回 errorCode=0 但其实失败

**症状**：调 `buildMeta` 返回 `{errorCode: "0"}`，但 `getFormSchema` 查不到新建的实体。

**原因**：苍穹响应 `errorCode="0"` 只表示 HTTP 层成功，不代表业务落库。

**解决（硬规则）**：
```
每次 buildMeta / modifyMeta 调用后，必须二次验证:
  response = modifyMeta(formId="hbjm_jobhr", ops=[...])
  if response.errorCode == "0":
      assert getFormSchema("hbjm_jobhr").fields 包含 新字段 key, "实际未落库"
```

**预防**：已落地到 `scripts/cosmic_preflight.py`

---

### EX-11 · EmployeeField 不在 buildMeta 枚举

**症状**：`buildMeta` 时传 `fieldType: "EmployeeField"`，返回 "不支持的字段类型"。

**原因**：`EmployeeField` 是 HR SDK 扩展类型，**OpenAPI 的 74 值枚举里不存在**。

**解决**：用 `BasedataField` + `basedataNumber: "hrpi_employeenewf7query"` 替代：
```json
{
  "fieldType": "BasedataField",
  "basedataNumber": "hrpi_employeenewf7query",
  "fieldKey": "${ISV_FLAG}_responsible"
}
```

**本场景特别注意**：`disabler`（禁用人，`scene_doc.json` L291-L304）已经用 `UserField` 而非 `EmployeeField`，但如果业务要加"职位负责人"字段，一定要用 BasedataField + basedataNumber。

---

### EX-12 · 下拉字段漏传 comboOptions

**症状**：新建 `ComboField` 后，UI 上下拉框为空。

**原因**：5 种下拉类型都必须带 `comboOptions`。

**解决**：
```json
{
  "fieldType": "ComboField",
  "key": "${ISV_FLAG}_recruitstatus",
  "comboOptions": [
    {"value": "A", "name": {"zh_CN": "招聘中", "en_US": "Recruiting"}},
    {"value": "B", "name": {"zh_CN": "冻结", "en_US": "Frozen"}}
  ]
}
```

**本场景参考**：`scene_doc.json` 中 `ctrlstrategy` / `initdatasource` / `oristatus` / `datastatus` 4 个 ComboField 都带 options（实际枚举值需实抓 getFormSchema 拿 `_auto_field_props.md` 的详情）。

---

### EX-13 · 字段类型拼写别名坑

**症状**：`fieldType: "MultiLangTextField"` → 静默失败，新建字段变成普通 TextField。

**原因**：苍穹官方字段类型拼写有 Bug：
- ✅ 正确：`MuliLangTextField`（Muli 是 Multi 拼写错）
- ❌ 错误：`MultiLangTextField`

**本场景证据**：`scene_doc.json` L45 `name` 字段类型 `MuliLangTextField`（共 12 个多语言字段都是这个拼写）

**预防**：`field_types_authoritative.py` 提供 74 值权威枚举 + 别名映射。

---

### EX-14 · modifyMeta 用 formNumber 失败

**症状**：`modifyMeta` 传 `formNumber: "hbjm_jobhr"`，返回 "参数错误"。

**原因**：仅 `modifyMeta` 接口参数叫 **`formId`**（实际传 formNumber 值），其他接口都叫 `formNumber`。

**解决**：
```json
// ✅ 对
{"formId": "hbjm_jobhr", "ops": [...]}
// ❌ 错
{"formNumber": "hbjm_jobhr", "ops": [...]}
```

**本场景特别**：scene_doc.json L5 `formIdTechnical: "/IJRHGRN5RVY"` 是内部 ID，**不要**用它做 formId 参数，要用 `hbjm_jobhr`。

---

### EX-15 · modifyMeta op 枚举非 4 值静默失败

**症状**：`modifyMeta` 传 `op: "update"` 或 `op: "addField"`，请求成功但没效果。

**原因**：苍穹仅识别 4 值：`add` / `modify` / `remove` / `move`。

**解决**：只能用这 4 个枚举值。

---

### EX-16 · addRule 的 ActionType 小写被忽略

**症状**：`addRule` 返回成功，但规则不生效。

**原因**：`ActionType` 和 `FieldKey` 必须 **PascalCase**：
```json
// ✅ 对
{"actionType": "Lock", "fieldKey": "Jobseq"}
// ❌ 错
{"actionType": "lock", "fieldKey": "jobseq"}
```

**本场景特别**：scene_doc.json 里的字段 key 都是小写（`jobseq` / `number` / `name`），但调 addRule 的时候 `fieldKey` 参数里要转首字母大写 `Jobseq` / `Number` / `Name`。

---

### EX-17 · addRule 的 preCondition 语法严格

**症状**：`preCondition: "jobseq == ''"` 规则不触发。

**原因**：公式引擎只支持有限语法：
- ✅ 允许：`==`, `>`, `<`, `>=`, `<=`, 字符串常量 `'xxx'`
- ❌ 禁用：`== ''`, `== null`, `||`, `&&`

**解决**：改用 Java 插件（如本场景 `JobHrSaveOp.onAddValidators` 并列注册自定义 Validator）

---

### EX-18 · addRule 坏规则删除死锁

**症状**：调 `deleteRule` 删坏规则，返回"规则校验失败无法删除"。

**原因**：苍穹要求**规则必须合法才能删除**（双重绑定），坏规则陷入死锁。

**解决**：先 `updateRule` 把规则改成合法（如 triggerField 改成有效字段 `jobseq`），再 `deleteRule`。

---

### EX-19 · registerPlugin targetType 大小写错误

**症状**：`registerPlugin` 返回成功，但插件不生效。

**原因**：`targetType` 必须**大写枚举**，5 个合法值：
- `BILL_FORM` / `LIST_FORM` / `MOBILE_BILL_FORM` / `OPERATION` / `EVENT`

**本场景典型**：
- 前端字段联动（如 CS-02） → `BILL_FORM`
- 列表扩展（继承 `JobBaseBuListPlugin`） → `LIST_FORM`
- 操作扩展（继承 `JobHrSaveOp`） → `OPERATION`

---

### EX-20 · updateOperation.plugins 全量替换陷阱

**症状**：调 `updateOperation` 新增一个插件，结果 save 链 10 标品插件全丢。

**原因**：`updateOperation.plugins` 是**全量替换**语义，不是 append。

**本场景最容易踩**：save 链有 10 个标品插件（`_auto_operations.md` L99-L113），unsubmit 只有 2 个（L283-L288），各不相同。

**解决**：
```python
# 正确做法: 先 get，合并后再 update
current = client.get_operation(formId="hbjm_jobhr", opKey="save")
current.plugins.append(new_plugin)
client.update_operation(plugins=current.plugins)
```

**同理**：`validations` 也是全量替换（save 有 6 条标品校验）。

---

### EX-21 · 插件 Java 类路径错误

**症状**：`registerPlugin` 后运行时 `ClassNotFoundException`。

**原因**：Java 类没打进 jar 或包名错。

**诊断**：
```bash
# 检查 jar 内
jar tf hrmp-kdtest_hbjm_ext.jar | grep YourPlugin
```

**解决**：确认 `className` 写的是**全限定名**（如 `kd.hrmp.hbjm.kdtest.JobHrSeqCodeOp`），检查 jar 打包。

**本场景参考**：标品 `JobHrSaveOp` 的 className 是 `kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp`（`_auto_operations.md` L112），扩展插件参考此命名风格。

---

## 三、时序模型特有异常

### EX-30 · newhisversion 之后找不到新版本

**症状**：调 `newhisversion` 后，新版本数据没出现

**原因**：`newhisversion` 仅产生新版本 DataEntity，**需要再走 `save` 或 `confirmchange` 才真正入库**

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

**症状**：`confirmchange` 报 `4V/78ID=HERR` MustInput 失败

**原因**：确认变更也要满足必填（`_auto_operations.md` L603）；`jobseq` 是必填

**解决**：填入完整的字段后再 confirmchange

---

### EX-32 · 改名历史的 namehistory 不生效

**症状**：调 `namehistory` opKey 改名，但历史没记录

**原因**：`namehistory` 是 `donothing` 类型（`_auto_operations.md` L420），需要单独的改名插件

**诊断**：该 opKey 的标品无插件（`_auto_operations.md` L43 `plugins: 0`），可能通过别的机制（`BdVersionSaveServicePlugin` 检测 name 变化自动写）

---

### EX-33 · 导入 hbjm_jobhr 模板字段不全

**症状**：用 `importdata_hr` 导入，新扩展字段没出现在模板

**原因**：HIES 导入走 `cusstartpage: hismodel_importstart`（`_auto_operations.md` L487），模板定义在自定义页面

**解决**：
- 扩展字段后重新生成导入模板
- 或在 `hismodel_importstart` 页面手动配置导入列

---

## 四、诊断工具清单

| 工具 | 作用 |
|---|---|
| `cosmic-env devportal open` | 打开 designer 看实际元数据 state |
| `getFormSchema("hbjm_jobhr")` | 二次验证 buildMeta/modifyMeta 是否落库 |
| `getOperation(formId="hbjm_jobhr", opKey="save")` | 二次验证 updateOperation 是否落库 |
| `list_rules(entity="hbjm_jobhr")` | 二次验证 addRule 是否生效 |
| `scripts/cosmic_preflight.py` | 提交前自动预检 |
| `payload_adapter.validate_payload` | 参数规范化 + 硬约束校验 |
| 数据库直查（职位时序） | `SELECT fid, fnumber, fboid, fbsed, fiscurrentversion, fhisversion FROM t_hbjm_job WHERE fboid = ? ORDER BY fbsed` |

---

## 五、通用铁律

### 铁律 1: `errorCode=0` ≠ 成功

任何 `buildMeta` / `modifyMeta` / `addRule` / `registerPlugin` 调用，都必须：
1. 检查 `errorCode == "0"`
2. **二次验证**（getFormSchema / list_rules / getOperation）

### 铁律 2: 参数大小写敏感

- `op` / `fieldType` / `actionType` 的大小写**每个接口不同**
- 必须用权威枚举表（`field_types_authoritative.py`）

### 铁律 3: 全量替换接口要先读后改

- `updateOperation.plugins`（本场景 save 有 10 插件）
- `updateOperation.validations`（本场景 save 有 6 校验）
- 任何覆盖式更新

### 铁律 4: 时序字段禁改

- `boid` / `iscurrentversion` / `datastatus` / `firstbsed` / `hisversion` / `sourcevid`
- 6 个字段都是 `autoComputed: true` + `minefield: red`（`scene_doc.json`）
- 改了就是脏数据，时序链断（参考 CASE-06）

### 铁律 5: 出厂字段禁改

- `orinumber` / `oriname` / `oristatus`
- 改了破坏 `HRBaseOriginalOp` 出厂值对比逻辑

### 铁律 6: 系统预置不能删

- `issyspreset=true` 的职位由 `hrbddeletevalidator` 保护
- 只能禁用，不能删除

### 铁律 7: 废弃字段不能引用

- `depcytype` 已废弃（scene_doc.json L566）
- 新扩展 / 新规则不要引用

---

## 六、常见组合 trap 快速查表

| 报错 / 症状 | 可能原因 | 解决思路 |
|---|---|---|
| `职位序列必填` | INV-01 / `6096194600001fac` | 填 `jobseq` |
| `生效日期不能填写未来` | `2M42D=Y0/0XK` | 走 `newhisversion` |
| `编码已存在` | `2=K9URZCEUUS` | 改编码 / 改唯一性策略 |
| `系统预置不能删除` | `hrbddeletevalidator` | 改禁用代替删除 |
| `同一 boid 时间段重叠` | `HisUniqueValidateOp` | 调整 bsed |
| 启用无反应 | `JobEnableValidator` 静默失败 | 查日志 / 检查 enable 字段 |
| save 后 10 标品插件全丢 | `updateOperation` 全量替换 | 先 get 再 append |
| 加字段 UI 看不见 | errorCode=0 但未落库 | `getFormSchema` 验证 |
| ComboField 空 | 漏 `comboOptions` | 补全枚举 |
| 规则不触发 | `actionType` 小写 | 改 PascalCase |
| 插件不执行 | className 错 / targetType 小写 | 检查全限定名 + 5 枚举 |

---

**📌 来源追溯**：
- EX-01 ~ EX-08 业务层：`_auto_operations.md` 各 opKey validations + `scene_doc.json` minefield
- EX-10 ~ EX-21 元数据层：`cosmic_realworld_traps/buildmeta_traps.md` / `modifymeta_traps.md` / `addrule_traps.md` / `platform/openapi_capability_map.md`
- EX-30 ~ EX-33 时序层：`_auto_operations.md` L547-L605 newhisversion / confirmchange
- 诊断 SQL：`scene_doc.json` physicalTable + physicalColumn 实抓
- 铁律 4 / 5 / 6 / 7：`scene_doc.json` minefield + autoComputed + isvCanModify 三字段交叉
