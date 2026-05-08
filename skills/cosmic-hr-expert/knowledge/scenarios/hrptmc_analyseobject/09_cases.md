# hrptmc_analyseobject · 案例

> **生成时间**：2026-04-29

## CASE-1 · 标品 save 链处理逻辑实证

取 `ReportAnalyseObjectOp` 作为样板：

- **触发 Validator**：`ReportAnalyseObjectValidator`
- **opKey 分支**：`delete`, `save`
- **异步任务**：`kd.hr.hrptmc.business.anobj.AnalyseObjectDimCountTask`
- **Service 调用**：3 个

完整反编译实物：`knowledge/_sdk_audit/_decompiled/scenarios/hrptmc_analyseobject/ReportAnalyseObjectOp.java`

## CASE-3 · ISV 改本 form 的影响推演

| 改动 | 影响 | 后果 |
|---|---|---|
| 加 ISV 字段（_ext 前缀） | 仅 ISV 私域 | ✅ 安全 |
| 删除标品字段 | 标品 OP 读不到 | ❌ save 失败 |
| 改字段必填属性 | 标品校验链冲突 | ❌ 元数据加载错 |
| 在 afterExecute 加同步业务 | 标品 sch_task 异步未起 | ⚠️ 时序问题 |
