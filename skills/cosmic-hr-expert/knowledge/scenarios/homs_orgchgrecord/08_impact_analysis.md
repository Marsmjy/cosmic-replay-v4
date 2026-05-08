# 变更影响面 · 组织变动明细查询(homs_orgchgrecord)

> **状态**: 基于 32 字段 + 15 opKey + 配对场景实证整理
> **confidence**: verified
> **审计时间**: 2026-04-27

---

## 1. 影响范围总览

本场景是**只读查询场景** · 自身改动很少向下游级联。但因为**跟配对场景 `homs_orgbatchchgbill` 强耦合** · 任何"改物理表 / 改字段元数据 / 改插件"动作都需要双场景回归。

| 改动类型 | 影响范围 | 级联动作 |
|---|---|---|
| 改本场景 list 显示 / 筛选 (前端 form) | 仅本场景 list | 不影响配对场景 |
| 加 ISV 自定义筛选字段 (主层 haos_adminorgdetail) | admin_org_quick_maintenance + 本场景 + haos_adminorghis 三场景 | 三场景同步影响 · 必须三场景回归 |
| 改 t_homs_orgchgrecord / t_homs_orgchgentry / t_homs_orgchgdetail 物理表 | **配对场景写入失败** + 本场景查询失败 | ⚠ 严禁直接改物理表 · 必须双场景测 |
| 改 chgentitynumber 字段语义 (业务约定) | 配对场景写入新值 → 本场景识别不出 changefield 列空 | 必须同步在本场景重写 buildData 逻辑(CS-04) |
| 改 OrgPermHelper 数据权限规则 | 全 HR 域所有场景受影响 | ⚠ 严禁改平台级权限工具 |
| 删除变动记录数据 (DELETE FROM t_homs_orgchgrecord) | 配对场景关联的 chgbill 引用断开 | ⚠ 严禁 |

---

## 2. 反向引用图(refentity_reverse · 谁引用本场景)

> 通过 `chgbill` 字段反向找到所有引用本场景数据的入口。

| 引用方 form | 引用字段 | 引用方式 | 业务含义 |
|---|---|---|---|
| `homs_orgbatchchgbill`(配对场景) | (反向引用 · 通过 OrgBatchChgBillEffectOp 写入) | 写入 | 申请单生效后写本场景 3 张表 |
| (理论上) 任何 ISV 自建审计报表 | 可能引用 t_homs_orgchgrecord | 查询 | 自定义审计 / 合规查询 |

> **特别注意**: 本场景**不被任何标品场景反查**(因为 `homs_orgchgrecord` 主表无 number / name 字段 · 不是常用基础资料引用目标) · ISV 加新字段不影响标品下游。

---

## 3. 上游引用图(本场景查询哪些表)

本场景 `setFilter` + `buildData` 阶段查询的表:

| 上游表 | 查询字段 | 调用路径 | 影响 |
|---|---|---|---|
| `haos_adminorgdetail` | id, boid, name, number, otclassify | setFilter L169, buildSplitMerge L286/L293, buildData L388/L394, getChangeDetailVO 等 | 改这表的元数据会让本场景渲染崩 |
| `homs_subentryentity` | id, chgentitynumber, chgpageelement, beforechgentity, afterchgentity, coopreltype | buildData L367 | 子分录基础资料 · 改了 buildData 失败 |
| `haos_teamcoopreltype` | name | buildData L386 | 协作类型基础资料 |
| `haos_structproject` | name | buildData L392 | 结构方案基础资料 |
| `homs_orgchgentry` | mergedorg, splitedorg, mergesplitflag, mulbasedatafield, id | buildSplitMerge L258 | 标品分录基础资料 |
| `bos_user`(via creator/modifier/operator) | id, name | 通过 UserField 字段引用 | 改用户表元数据 · 全平台风险 |
| `haos_changescene` | id, name, number | 通过 BasedataField changescene 引用 | 改这表只影响显示 · 不影响功能 |
| `haos_orgchangetype` | id, name | 通过 BasedataField changetype 引用 | 同上 |
| `haos_orgchangereason` | id, name | 通过 BasedataField changereason 引用 | 同上 |

---

## 4. 改动分级(按风险由小到大)

### 4.1 🟢 低风险(可放心做)

- 重写 `setFilter` 加 ISV 自定义 QFilter (CS-01) · 不影响其他场景
- 重写 `beforeCreateListColumns` 改默认排序 (CS-02) · 仅本场景 list 显示效果
- 重写 `billListHyperLinkClick` 改跳转目标 (CS-03) · 仅本场景跳转行为
- 重写 `packageData` 加行级样式 (CS-04 方案 A) · 仅本场景渲染

### 4.2 🟡 中风险(需要回归)

- 加 ISV 自定义筛选字段(CS-01)需要在 `haos_adminorgdetail` 主层 modifyMeta · 双场景回归
- 重写 `beforePackageData` + `packageData` 加自定义溯源类型 (CS-04 方案 B) · 需要在配对场景写入端协同(否则数据缺失)
- 加 toolbar 按钮 + 高级筛选弹窗 (CS-05) · 改了 list 交互 · 用户培训成本

### 4.3 🔴 高风险(慎行)

- **修改 t_homs_orgchgrecord / t_homs_orgchgentry / t_homs_orgchgdetail 物理表 schema**: 配对场景的 OrgBatchChgBillEffectOp 写入失败 · 全 homs 域瘫痪
- **删除/重命名 chgentitynumber 字段语义约定**: 配对场景写入新值后本场景认不出 · 历史变动溯源失效
- **改 OrgPermHelper.getHrPermFilter 行为**: 全 HR 域所有场景的数据权限受影响
- **直接 INSERT/UPDATE/DELETE 物理表**: 破坏 haos_adminorg 的时序版本一致性

### 4.4 ⛔ 严禁(后果不可逆)

- 删除任何已生效申请单产生的变动记录: 审计完整性破坏 · 合规风险
- 跳过配对场景直接写本场景物理表: 跟 haos_adminorg 时序数据不一致 · 后续无法恢复
- 改 `chgbill` 字段引用 entity (`homs_orgchgbill`) → 配对场景跳转链路断
- 在生产环境改 `setFilter L159` 的 OrgPermHelper 调用: 用户可见数据范围变化 · 数据安全事故

---

## 5. 配对场景的影响传导(双向)

### 5.1 改本场景 → 配对场景受影响

| 改动 | 配对场景受影响项 |
|---|---|
| 改 t_homs_orgchgrecord 物理表加非空字段 | 配对场景 OrgBatchChgBillEffectOp 写入失败(违反非空约束) |
| 改 t_homs_orgchgentry 物理表删除字段 | 配对场景 OrgBatchChgBillEffectOp 写入失败(列不存在) |
| 改 chgbill 字段类型 (BigInt → String 等) | 配对场景 hyperLinkClick 跳本场景失败(类型不匹配) |
| 给本场景加触发器 / before insert trigger | 配对场景生效流程性能下降 |

### 5.2 改配对场景 → 本场景受影响

| 改动 | 本场景受影响项 |
|---|---|
| 配对场景加新 entry 类型(7 个增至 8 个) | 本场景 buildData 不识别 · changefield 列空 |
| 配对场景改 audit/submiteffect 不再写本场景 | 本场景列表 · 业务断流 |
| 配对场景改 chgentitynumber 写入值 | 本场景 buildData 三大分支不识别新值 |
| 配对场景禁用 BEC 发布 | CS-06 订阅方收不到事件 |

> ⭐ **关键铁律**: 任何对本场景或配对场景的修改 · 都要**双场景回归**。

---

## 6. 跨场景影响传导

### 6.1 改 haos_adminorgdetail 主层(三场景共用)

新增 / 修改 / 删除 `t_haos_adminorg` 列时同时影响:
1. `admin_org_quick_maintenance` (当前版本视图)
2. `haos_adminorghis` (历史版本视图)
3. **`homs_orgchgrecord`(本场景)** - 因为 buildData L388/L394 等 · `haos_adminorgdetail` 列变更会导致字段级溯源失败

### 6.2 改 haos_changescene / haos_orgchangetype / haos_orgchangereason

这 3 张基础资料的 number / name 改了:
- 配对场景 form 显示效果变(下拉 F7 选项)
- 本场景显示效果变(分录字段引用)
- 不影响数据完整性

### 6.3 改 haos_teamcoopreltype / haos_structproject

这 2 张基础资料是字段级溯源的"显示名"来源:
- 改了只影响 changefield 列的协作 / 矩阵变动行的 display
- 不影响数据本身

---

## 7. 历史事故复盘(理论性 · 类似场景的常见坑)

### 7.1 案例 A · ISV 直接修改 t_homs_orgchgrecord 元数据

某客户 ISV 想给本场景 list 加列 · 直接 modifyMeta(formId="homs_orgchgrecord", op="add field", key="${ISV_FLAG}_audit_status")。

**结果**:
- 本场景 list 列显示成功
- 配对场景 OrgBatchChgBillEffectOp 写入时:
  - 如果新字段非空 → 写失败 · 申请单永远不能生效
  - 如果新字段允许空 → 写入成功但本场景列永远是空(配对场景不知道往哪填)
- 用户投诉"加了字段但看不到数据"

**正确做法**: ISV 自加字段挂在 `haos_adminorgdetail` 主层 · 让平台时序模型自动同步 · 配对场景写入时通过 `chgentitynumber=haos_adminorgdetail / chgpageelement=${ISV_FLAG}_audit_status` 落到 t_homs_orgchgdetail · 本场景的 buildData 自动识别(因为它会按 propertySet 动态 select)。

### 7.2 案例 B · 删除"已废弃"的变动记录

某客户合规部要求"清理 5 年前的变动记录"· 直接 `DELETE FROM t_homs_orgchgrecord WHERE fcreatetime < '2020-01-01'`。

**结果**:
- 配对场景的旧申请单关联记录消失 · 跳转到详情页显示"申请单已废弃"
- 审计追溯链断裂 · 合规事故

**正确做法**: 走标品的"数据归档"功能(平台级)· 不是直接 DELETE。

### 7.3 案例 C · 误改 buildData L388 实证字段

某客户 ISV 想改"某种变动场景显示自定义提示"· 反编译看到 buildData L388 写的是 `entityNmae = "haos_adminorgdetail"`(标品错别字 entityNmae)· 直接继承 `AdminOrgChgRecordListPlugin` 想"修正"这个错别字。

**结果**:
- `AdminOrgChgRecordListPlugin` 是 `public final class` · 编译失败
- 即使绕开 final · 标品下次升级修复了拼写错误 · ISV 代码全部重新调

**正确做法**: 不要碰标品代码。错别字虽然丑 · 但不影响功能。如果要扩展 · 走 CS-04 方案 B 自建子类逻辑。

---

## 8. 双场景回归测试清单

### 8.1 改本场景的回归用例

| 测试用例 | 期望结果 |
|---|---|
| 配对场景 `homs_orgbatchchgbill` 新建一条 add 类型申请单 → 走 audit 生效 | 本场景 list 出现该变动记录 |
| 同上 → 配对场景 hyperLinkClick 跳转 | 本场景 list 显示 needshowdetail 视图 |
| 本场景点列表行 chgbill 列 | 跳到配对场景 VIEW 状态详情页 |
| 本场景按 changescene 筛选 | 列表只显示对应场景的变动 |
| 本场景按 chgeffecttime 范围筛选 | 列表只显示日期内变动 |
| 本场景按 BU 左树点击 | 列表只显示该 BU 下变动 |
| 本场景列表导出 | 导出 Excel 包含 changefield/before/after 等计算列 |

### 8.2 改配对场景的回归用例

| 测试用例 | 期望结果 |
|---|---|
| 配对场景写入完整 7 entry 类型(add/parent/info/disable/merge/split + entryentity_all) | 本场景全部能查到 |
| 配对场景 ISV 加新 entry 类型 (8 entry) | 本场景 buildData 应识别新类型 (CS-04 方案 B) |
| 配对场景 ISV 加新字段写入 | 本场景应能溯源(主层 haos_adminorgdetail 元数据已加) |

---

## 9. 部署前 checklist

每次改本场景或配对场景 · 部署前过一遍:

- [ ] 改物理表 schema · DBA 出 PDM diff
- [ ] 双场景全集回归 · 测试用例 §8.1 + §8.2
- [ ] 跑数据权限测试(用不同 BU 权限的用户登录看)
- [ ] 大数据量场景(>10000 行变动)性能测试
- [ ] 多语言场景测试(切换中/英/其他启用语言)
- [ ] 跨语言变动溯源测试(name 字段在多语言下的 diff)
- [ ] 上线前数据备份(物理表全量备份 · 至少保留 30 天)

---

## 10. 关联文档

- [`02_business_rules.md`](02_business_rules.md) · 业务规则 + 数据可见性
- [`03_model_design.md`](03_model_design.md) · 字段 + 物理表
- [`05_data_flow.md`](05_data_flow.md) · 数据流转
- [`06_customization_solutions.md`](06_customization_solutions.md) · CS-01..CS-06
- [`11_upstream_downstream_logic.md`](11_upstream_downstream_logic.md) · 上下游全图
- [`knowledge/scenarios/homs_orgbatchchgbill_maintenance/08_impact_analysis.md`](../homs_orgbatchchgbill_maintenance/08_impact_analysis.md) · ⭐ 配对场景影响面(必对照)
