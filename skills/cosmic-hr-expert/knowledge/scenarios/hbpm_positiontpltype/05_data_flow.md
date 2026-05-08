# 数据流转 · 岗位模板类型（hbpm_positiontpltype）

> **状态**: 🟢 基于 PositionTplTypeSaveOp 反编译 + BaseFormModel 分析
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、数据读写路径

### 1.1 新建时 index 自动计算路径

```
UI 层（afterBindData）：
    PositionTplTypeRepository.getInstance().queryOneIndexByIndexDesc()
        → SELECT max(findex) FROM t_hbpm_positiontpltype
        → 返回 DataSet(index=maxValue)
        → model.setValue("index", maxValue + 10)

OP 层（beginOperationTransaction）：
    PositionTplTypeSaveOp.getLargestIndex()
        → PositionTplTypeRepository.getInstance().getAllDyn("index")
        → 遍历所有行，取 Math.max(largestIndex, current)
        → 对 index 为空的行：entity.set("index", largestIndex += 10)
```

### 1.2 保存数据写入路径

```
用户点保存
    ↓
[Validator 阶段（onAddValidators）]
PositionTplCommonValidator
    → 查 t_hbpm_positiontpltype 名称唯一性（同 org · enable=1）
PositionTplTypeIndexUniqueValidator
    → 查 t_hbpm_positiontpltype index 唯一性（同 org）
CtrlStrategyValidator
    → 校验 ctrlstrategy / org / createorg 配置合规
    ↓ 全部通过
[事务内 beginOperationTransaction]
PositionTplTypeSaveOp
    → 兜底补 index（API 导入时 index 为空）
    ↓
落库 t_hbpm_positiontpltype（原地 UPDATE）
    ↓
[事务提交后 afterExecuteOperationTransaction]
HRBaseDataLogOp → 写操作日志
```

---

## 二、数据流向图

```
              ┌──────────────────────────────────────────┐
              │         t_hbpm_positiontpltype 主表         │
              │  fnumber / fname / findex               │
              │  fenable / fstatus                      │
              │  fissyspreset                           │
              │  org/useorg/createorg/ctrlstrategy 字段  │
              └───────────────────┬──────────────────────┘
                                  │ 被引用
                                  ▼
              ┌──────────────────────────────────────────┐
              │         hbpm_positiontpl（岗位模板）         │
              │  type 字段 → 引用 hbpm_positiontpltype.id  │
              └──────────────────────────────────────────┘
                                  │ 被引用
                                  ▼
              岗位模板应用于岗位（hbjm_jobhr）
```

---

## 三、事务边界

| 阶段 | 操作 | 是否在事务内 |
|---|---|---|
| Validator.validate() | 校验（只读） | 否（校验在事务外） |
| beforeExecuteOperationTransaction | 状态检查 | 是（事务开始） |
| beginOperationTransaction | index 兜底 + 写数据 | 是（核心事务） |
| 落库 t_hbpm_positiontpltype | UPDATE | 是 |
| afterExecuteOperationTransaction | 写日志 | 否（事务提交后） |

---

## 四、失败回滚策略

- Validator 失败（nameError/indexError）：OperationResult.isSuccess=false，不进事务，不落库
- beforeExecuteOperationTransaction 抛异常：事务回滚，不落库
- beginOperationTransaction 抛异常：事务回滚，不落库
- afterExecuteOperationTransaction 异常：主数据已落库，只有日志写入失败（容错）

---

## 五、本场景无历史版本写入（BaseFormModel 特征）

与 HisModel 的差异：直接 UPDATE 原行，无 INSERT 新版本行，无 `_his` 后缀表。与 hbpm_basedatalist 数据写入模式相同。
