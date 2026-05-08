# 数据流转 · 岗位基础资料（hbpm_basedatalist）

> **状态**: 🟢 基于 18 插件注册表 + BaseFormModel 模型分析
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、数据读写路径

### 1.1 列表分组数据读取

```
用户访问 hbpm_basedatalist 列表页
    ↓
HRBaseDataTplList.beforeBindData
    → 列表平台查询 t_hbpm_basedatalist 主表
    → 按基础资料分类分组展示（每行代表一类基础资料）
    ↓
HRBDGroupList.billListHyperLinkClick / listRowDoubleClick
    → HRBaseServiceHelper("hbpm_basedatalist").queryOne("pagekey", idFilter)
    → 读取 fpagekey 字段值
    → 用 pagekey 值构造 ListShowParameter 跳转
```

### 1.2 编辑保存数据写入

```
用户在子页面保存单条基础资料
    ↓
[FormPlugin 层]
PositionBasedataEdit.afterBindData  · 读 issyspreset
    ↓ (issyspreset=false 才可写)
[OP 层 · 单个事务]
CodeRulePlugin     → 生成/校验 number（fnumber 列）
HRBaseDataStatusOp → 校验 status 合法性
HRBaseOriginalOp   → 阻断出厂数据修改（issyspreset=true 时抛异常）
BdVersionSaveServicePlugin → 写版本信息
    ↓
落库到 t_hbpm_basedatalist
    ↓
HRBaseDataLogOp.afterExecuteOperationTransaction → 写操作日志
```

---

## 二、数据流向图

```
                ┌─────────────────────────────────┐
                │    t_hbpm_basedatalist 主表        │
                │  fnumber / fname / findex        │
                │  fissyspreset / fpagekey         │
                │  fenable / fstatus               │
                └──────────────┬──────────────────┘
                               │ 读（分组列表）
                               ▼
                    HRBDGroupList（列表聚合）
                               │ 点击超链接跳转
                               ▼
                    各岗位基础资料子列表
                    （pagekey 指向的 form）
                               │ 用户编辑保存
                               ▼
                    [保存 OP 链]
                    CodeRuleOp → 生成编码
                    HRBaseDataStatusOp → 状态管理
                    HRBaseOriginalOp → 出厂保护
                               │
                               ▼
                    回写 t_hbpm_basedatalist
                    （不写历史表 · BaseFormModel）
```

---

## 三、事务边界

| 阶段 | 操作 | 是否在事务内 |
|---|---|---|
| CodeRulePlugin | 生成编码号 | 是 |
| HRBaseDataStatusOp | 状态校验 | 是（beforeExecuteOperationTransaction） |
| HRBaseOriginalOp | 出厂数据保护 | 是（beforeExecuteOperationTransaction） |
| 写主表 t_hbpm_basedatalist | 落库 | 是（核心事务） |
| HRBaseDataLogOp | 写操作日志 | afterExecuteOperationTransaction（事务提交后） |

---

## 四、失败回滚策略

- 平台标准：任何 OP 插件在 beforeExecuteOperationTransaction 抛异常 → 整个事务回滚
- HRBaseOriginalOp 会在尝试修改出厂数据（issyspreset=true）时直接抛异常 → 保存失败
- 编码规则冲突（CodeRulePlugin）→ 提示用户重新输入编码 → 不落库

---

## 五、本场景无历史版本写入（BaseFormModel 特征）

与 HisModel 场景（如 haos_adminorg）的关键数据流差异：

| 维度 | 本场景 | HisModel 场景 |
|---|---|---|
| 历史表写入 | **无**（单表更新，原地修改） | 有（INSERT 新版本行，旧版本 iscurrentversion=false） |
| boid 维护 | **不需要** | 需要（boid 不变，id 每版本不同） |
| 存档数量 | 始终 1 行/记录 | N 行/记录（每次修改新增一行） |
| 查询性能 | 直接 id 查，快 | 需加 iscurrentversion=true，稍慢 |
