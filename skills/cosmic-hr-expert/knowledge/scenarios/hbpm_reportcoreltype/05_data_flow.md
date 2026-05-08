# 数据流转 · 协作类型（hbpm_reportcoreltype）

> **状态**: 🟢 基于 37 插件注册表 + 字段分析 + BaseFormModel 特征
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、数据读写路径

### 1.1 列表读取路径（含排序）

```
用户访问协作类型列表
    ↓
HRBaseDataTplList.beforeBindData
    → 查询 t_hbpm_reportcoreltype
    ↓
PositionnBaseDataOrderPlugin.setFilter
    → setOrderBy("createorg.id asc, index asc")
    → ORDER BY t_hbpm_reportcoreltype.fcreateorgid ASC, findex ASC
    ↓
列表展示（按创建组织分组 + 组内按 index 排序）
```

### 1.2 保存数据写入路径（含子表）

```
用户点保存
    ↓
[Validator 阶段]
HRBaseDataStatusOp · 状态合法性
BaseDataBuOp(hbpm 域) · CtrlStrategyValidator 控制策略校验
    ↓ 通过
[事务内]
HRBaseOriginalOp · 出厂数据保护
    ↓
写主表 t_hbpm_reportcoreltype
    ↓
写子表 t_hbpm_orgteamtype（每个选中的 orgteamtype 值 → 一行）
    ↓
[事务提交后]
HRBaseDataLogOp.afterExecuteOperationTransaction → 写操作日志
```

---

## 二、数据流向图

```
              ┌─────────────────────────────────────────────┐
              │       t_hbpm_reportcoreltype 主表              │
              │  fnumber / fname / findex                   │
              │  fenable / fissyspreset                     │
              │  org/createorg/ctrlstrategy 字段             │
              └──────────────────┬──────────────────────────┘
                                 │ 1:N
                                 ▼
              ┌─────────────────────────────────────────────┐
              │       t_hbpm_orgteamtype 子表                  │
              │  fentryid → 主表 fid                         │
              │  forgteamtype（所属组织分类 id）                │
              └─────────────────────────────────────────────┘

              ↓ 被引用
              汇报关系场景（使用协作类型字典）
```

---

## 三、事务边界

| 阶段 | 操作 | 是否在事务内 |
|---|---|---|
| CtrlStrategyValidator | 控制策略校验（只读）| 否（校验在事务外）|
| beforeExecuteOperationTransaction | 状态检查 | 是 |
| 写主表 t_hbpm_reportcoreltype | UPDATE | 是 |
| 写子表 t_hbpm_orgteamtype | DELETE+INSERT（全量替换）| 是 |
| afterExecuteOperationTransaction | 写日志 | 否（事务提交后）|

---

## 四、子表 orgteamtype 的写入模式

标品对 MulBasedataField 子表的保存策略通常是**全量替换**：
1. DELETE 当前主表行对应的所有子表行（`WHERE fentryid = mainId`）
2. INSERT 新选中的每个 orgteamtype 值（每个值一行）

**ISV 注意**：
- 不要手动写 DELETE+INSERT 逻辑（平台框架已处理）
- 在 OP 层 onPreparePropertys 声明 `orgteamtype` 才能在 beginOperationTransaction 中读取子表数据
- 子表行 id 由平台 `ID.genLongId()` 自动生成（PR-005）

---

## 五、无历史版本（BaseFormModel 特征）

- 主表和子表均无 iscurrentversion 字段
- 修改时直接 UPDATE 主表 + 全量替换子表
- 无 `_his` 后缀历史版本表
