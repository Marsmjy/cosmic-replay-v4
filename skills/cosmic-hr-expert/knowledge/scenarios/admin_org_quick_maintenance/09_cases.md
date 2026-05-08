# 参考案例 · 行政组织快速维护

> **状态**: 🟡 基于 `domain/org/real_world_scenarios.md` 24 场景 + `adminorg_extension_pattern.md` 真实 DYM
> 部分案例需从项目 Git 历史补充具体代码引用

---

## CASE-01 · 行政组织扩展 3 字段 + 3 字段分录（真实扩展包）

### 业务背景
HR 项目需要给行政组织加：
- `${ISV_FLAG}_field001m` (TextField) - 主表测试字段
- `${ISV_FLAG}_field002m` (DateField) - 主表日期字段
- `${ISV_FLAG}_field003m` (BasedataField) - 主表基础资料引用

同时加一个分录 `${ISV_FLAG}_entry001m`，含：
- `${ISV_FLAG}_sub_field_ml` (MuliLangTextField)
- `${ISV_FLAG}_sub_field_large` (LargeTextField)
- `${ISV_FLAG}_sub_field_mul` (MulBasedataField)

### 技术方案

- **扩展对象**: `haos_adminorg` (主)
- **Pattern**: Pattern A (add_field_extension) + modifyMeta add entity

### 扩展包产出

```
hrmp-kdtest_haos_ext/        (主扩展包)
  └── kdtest_haos_adminorg.dym

odc-kdtest_homs_ext/          (调整单扩展)
  └── kdtest_homs_batchorge_ext.dym  (4 前缀 × 3 = 12 字段)

hr-hom-dm/                    (信息组配置)
  └── preinsdata/T_HOM_INFOGROUP.sql

hrmp-hrcs-dm/                 (权限配置, 行政组织无需预置)
```

### 实测关键数据

- **ParentId**: `4S6B8I6U+EBN`
- **InheritPath**: 4 级
- **JsPlugin 注册**: 5 个占位

### 踩坑

- ❌ 误加到 `haos_adminorgdetail` → 继承混乱
- ❌ 调整单字段 key 未加 4 前缀 → 同步识别失败
- ❌ 给 `belongcompany` 赋值 → 下游取数错误
- ✅ 最终: 只扩展主表 + 严格 4 前缀

### 可复用资产

- `kdtest_haos_adminorg.dym`
- `kdtest_homs_batchorge_ext.dym`

### 项目信息

- 状态: ✅ 已上线
- 来源: `adminorg_extension_pattern.md`

---

## CASE-02 · 系统推荐配置的 4 前缀 批量扩展

### 业务背景
用系统推荐配置功能批量扩展调整申请单字段。

### 关键发现

"系统推荐配置" = 平台按 4 前缀**自动推荐**字段，**需人工确认**：
- VQ597FqFoc 原信息 → 推荐 1 字段
- 7auphYEIJr info_ → 推荐 1 字段
- 8bosVcKAfQ parent_ → 推荐 1 字段
- wHBtyCCUik add_ → 推荐 1 字段

### 启示

标品知道 4 前缀规则，定制时**复用这个机制**，不要全手动。

### 踩坑

- ❌ 以为自动执行 → 实际需人工确认
- ❌ 漏点某个前缀 → 字段缺失

### 来源

`adminorg_extension_pattern.md` §3.3

---

## CASE-03 · 扩展字段跨模块联动（待补充真实项目）

### 业务背景
将 haos_adminorg 扩展字段联动到 pay_salary_archive 做成本中心划分。

### 技术方案骨架

- **扩展点**: `afterSave@haos_adminorg` + 事件订阅
- **核心**: 发 `org_change_event`，薪酬模块订阅后反写

### 扩展入口坐标（发布方 · haos_adminorg）

- 绑定表单：`haos_adminorg`
- 绑定操作：`save`
- 推荐父类：`AbsOrgBaseOp`
- 关键重写方法：
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — 事务提交后向事件总线 / MQ 发布 `OrgChangeEvent`

**业务意图（发布方）**：行政组织保存完成后，把变更信息打包成领域事件投递出去，让薪酬成本中心同步服务独立订阅。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

### 订阅方（薪酬模块，独立服务）

**业务意图（订阅方）**：薪酬模块订阅 `OrgChangeEvent`，收到事件后根据 `orgId` 和新上级 ID 更新 `pay_salary_archive` 的成本中心归属，写入失败走独立重试链路，不反向影响组织保存主流程。

> ⚠️ 订阅方是独立服务（非 haos_adminorg 的操作扩展），一般注册到苍穹消息总线或自建 MQ。发布方必须在事务提交后（after 阶段）再发事件，避免主事务回滚时产生脏事件。

### 待补充: 真实项目 ID + 代码路径

---

## CASE-04 · 批量组织调整（待补充真实项目）

### 业务背景
集团重组一次性调整 200+ 组织的上级归属。

### 方案要点

- 走 `homs_orgbatchc` 批量调整单，**不走**快速维护
- 自定义 `BatchOrgMoveTask.java`
- 每 50 条一批独立事务

### 踩坑

- ❌ 初版未分批 → 超大事务锁表 20 分钟
- ❌ 忘了考勤模块反写 → 排班错乱
- ✅ 最终: 50 条/批 + 失败可恢复

---

## CASE-05 · 跨子公司编码重名（待补充）

### 业务背景
多子公司独立用各自编码空间，支持"同编码不同组织"。

### 方案

- **扩展点**: `beforeSave@haos_adminorg`
- **实现**: 覆盖 `OrgCodeUniquePlugin`，按租户隔离
- **详见**: [CS-05](./06_customization_solutions.md#cs-05--组织编码唯一性校验按租户隔离)

---

## 案例收集路线图

### 已整理（样板用）
- ✅ CASE-01: 扩展包真实 DYM
- ✅ CASE-02: 系统推荐配置

### 待补充真实项目
- ⏳ CASE-03: 薪酬联动案例
- ⏳ CASE-04: 批量调整案例
- ⏳ CASE-05: 跨子公司案例

### 长期目标案例类型
- [ ] 审批流集成
- [ ] AD/LDAP 外部系统同步
- [ ] 多层级权限矩阵
- [ ] 组织合并/拆分
- [ ] 跨租户迁移
- [ ] 历史回溯变更

---

**📌 来源追溯**：
- CASE-01, 02: `knowledge/domain/org/adminorg_extension_pattern.md`
- CASE-03 ~ 05: 骨架基于 `anchors.md`，需从项目 Git 补充
