# 参考案例 · 矩阵组织维护（haos_structure）

> **状态**：🟢 案例基于反编译实证 + 同族场景类比
> **confidence**：likely · 案例为典型业务模式 · 客户名/项目编号脱敏

## 案例 1 · 多事业部矩阵组织建模（典型场景）

### 业务背景

某集团公司有 5 个事业部 · 每个事业部都需要独立的矩阵组织视图（销售视图 / 生产视图 / 研发视图）· 共 5×3 = 15 个矩阵组织实例。

### 实现路径

1. **建结构化方案母本**（haos_structproject 场景 · 不在本场景）
   - "销售视图方案" / "生产视图方案" / "研发视图方案" 三个母本
2. **本场景批量建实例**
   - 每个事业部为单位 · 建 3 个实例（绑 3 个母本）
   - 共 15 条数据
3. **每条实例的 rootorg** 选定该事业部所辖最高层行政组织
4. **maintainframework** 进入每条实例 · 在 haos_orgstructlist 维护具体下挂关系

### 关键决策点

| 决策 | 实际选择 | 理由 |
|---|---|---|
| 是否在 haos_structproject 加业务字段（如"事业部代码"）| 不加 | 标品已有 otclassify · 业务靠它分类 |
| 列表权限是否走 hrcs 授权 | 走 | 跨事业部 HR 经理通过 hrcs 授权能看到对方事业部矩阵 |
| 是否启用 creatorhaspermission 参数 | 启用 | 防止 HR 互相看见对方维护的草稿 |

### ISV 定制（可选）

- CS-04：启用前置校验（"必须依赖某个母本才能启用" · 防止误启）
- CS-08：列表过滤扩展（"我审核过的" · 多事业部经理需要看自己审核过的）

### 关联 CS 与 PR
- CS-04 · CS-08
- PR-001 · PR-010

---

## 案例 2 · 加自定义字段"主管事业部"（CS-01 + CS-06 应用）

### 业务背景

集团希望在矩阵组织上标记"主管事业部"字段 · 用于报表归集。但**不希望**这个字段污染 haos_structproject 母本（母本是通用方案 · 不归任何事业部）。

### 实现路径

1. 走 ISV 扩展元数据继承 `haos_structure`
2. 加字段 `${ISV_FLAG}_managebu`（BasedataField → bos_org · 指事业部 BU）
3. 仅挂在 ISV 扩展 form · 不挂 haos_structproject

### 验证步骤

```python
schema_instance = client.get_form_schema("${ISV_FLAG}_haos_structure_ext")
assert "${ISV_FLAG}_managebu" in schema_instance      # ✅ 实例 form 有

schema_mother = client.get_form_schema("haos_structproject")
assert "${ISV_FLAG}_managebu" not in schema_mother    # ✅ 母本 form 没有
```

### 物理层观察

```sql
DESCRIBE t_haos_structproject;
-- 多了一列 ftdkw_managebu_id（实际数据库列名）
-- haos_structure 实例数据：填实际事业部 BU id
-- haos_structproject 母本数据：NULL
```

### 关联 CS · PR
- CS-01（加字段）
- CS-06（共用物理表的隔离）
- PR-001 · PR-007

---

## 案例 3 · "启用矩阵前必须配置依赖方案"（CS-04 应用）

### 业务背景

某客户 HR 反馈：用户经常忘记配 `relyonstructproject` 字段就直接启用矩阵组织 · 导致下游计算服务报"无方案可依据"。

### 实现路径

1. 在 `enable` opKey 挂自定义 OP（参考 CS-04 代码）
2. OP 的 `onAddValidators` 注册 `RelyOnStructProjectRequiredValidator`
3. Validator 的 `validate()` 检查 `relyonstructproject` 字段是否非空
4. 命中条件 `addErrorMessage(ext, "启用矩阵组织前必须选择依赖架构方案")`

### 落地效果

- 启用时校验 · 阻断不合规启用
- save 不卡 · 用户可以暂存草稿
- submit 也不卡（业务方决定是否扩展到 submit）

### 业务参数化建议

把"是否要这个校验"做成系统参数 · 客户可关：
```java
boolean checkRelyOn = getCheckRelyOnFromAppParam();
if (!checkRelyOn) {
    return;  // 跳过校验
}
```

### 关联 CS · PR
- CS-04
- PR-001 · PR-010

---

## 案例 4 · 选了方案自动带出根组织类型（CS-03 应用）

### 业务背景

某客户业务方期望：在新建矩阵组织时 · 选择 `relyonstructproject` 后 · 系统自动把母本的 `roottype` 字段值带过来 · 减少用户填写工作量。

### 实现路径

1. 新 FormPlugin extends `HRDataBaseEdit` · 重写 `propertyChanged`
2. 监听 `relyonstructproject` 字段变更
3. 反查母本 · 用 `HRBaseServiceHelper("haos_structproject").queryOne(...)` 拿 `roottype`
4. 通过 `getModel().beginInit() ... endInit()` 包住 setValue 防死循环
5. `getView().updateView("roottype")` 触发 UI 刷新

### 实测注意

- ⚠ 如果母本的 roottype 跟实例的业务期望不一致 · 这个联动可能反而误导用户。落地前业务方确认"是不是应该带出母本 roottype"
- ⚠ 联动后用户手改 roottype · 不会再触发联动（propertyChanged 是单次事件 · 改了不会重置）
- ⚠ 如果业务方要"联动+锁定"（联动后用户不能改）· 在 afterBindData 加 `getView().setEnable(false, "roottype")`

### 关联 CS · PR
- CS-03
- PR-003 · PR-004

---

## 案例 5 · 列表权限扩展"我审核过"（CS-08 应用）

### 业务背景

某 HR 经理需要在列表中看到所有自己审核过的矩阵组织（不只是自己创建的）· 标品 `StructureListPlugin.setFilter` 仅筛"我创建" · 需要扩展。

### 实现路径

1. 新 ListPlugin extends `HRDataBaseList`
2. 重写 `setFilter` · 通过 `e.getQFilters().add(...)` 追加"我审核过"过滤
3. 实际"审核人"字段名要从操作日志（HRBaseDataLogOp 写的 t_*_log 表）反查 · 落地前真发验证（不脑补）

### 实测决策点

| 决策 | 选择 | 理由 |
|---|---|---|
| AND 还是 OR 关系 | AND（默认 add）| 业务期望"在我创建的基础上再加我审核的"· 不是"我创建 OR 我审核" |
| 是否替换 StructureListPlugin | 不替换 | StructureListPlugin 是 final · 不能继承 · 只能并列挂 |
| 性能 | 加索引 | 操作日志表大 · 按用户 ID 加索引 |

### 关联 CS · PR
- CS-08
- PR-001（不继承标品 final 类）

---

## 经验沉淀（写给后续 ISV 实施者）

### 不要踩的坑

1. **不要假设标品发了 BEC** —— 反编译实证标品没发 · 套 admin_org CS-04 模式订阅不到任何事件
2. **不要继承 StructureListPlugin / StructureEditPlugin / StructProjectBUListPlugin** —— 全 final · 编译报错
3. **不要把 otclassify 改成非 1010L** —— 数据会从矩阵组织列表消失
4. **不要假设"双 form 物理隔离"** —— 共用 t_haos_structproject · 物理列共享是平台机制
5. **不要在 OP 里 getModel().setValue()** —— OP 没 model · NPE
6. **不要在 propertyChanged 里 setValue 不包 beginInit/endInit** —— 死循环
7. **不要硬编码 1010L** —— 业务标识用常量类引用 · 标品升级可能改

### 推荐的工作模式

1. 先 mock 一个矩阵组织实例 · 走完 save → submit → audit → enable → maintainframework 全流程
2. 抓 HAR 看真实请求 · 验证字段名和值（feedback_har_is_ground_truth）
3. 写代码前先看 `_auto_plugin_registry.md` + `_auto_operations.md` 全清单
4. 改完发到测试环境 · 真发验证（不要只单元测试 · feedback_platform_capability_judgement）
5. 关键场景跨用户 / 跨 BU 场景测一遍
