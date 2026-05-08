# 数据流转 · wtp_swshiftrule

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（继承层 + Service 调用 + ChgRecord 流水 · v2）
> **数据源**: `scene_doc.json::inheritance` · 反编译 Service 引用
> **生成**: polish_form_scene_v2.py

## ✅ verified · 主表 `wtp_swshiftrule` + 继承层

（继承层数据待 scene_doc.json 升级填充）

## ✅ verified · Service 调用面（反编译扫）

## ✅ verified · 流水写入

- 未命中 ChgRecord/Timeline/HisModel · 可能是只读容器或纯查询场景

## 🟡 unverified · 事务边界 + 异步任务（待人工补充）

<TODO 人工补>
- 哪些 OP 在同一个 TX 内？（save 是否含写流水 + 写历史 + 触发 BEC？）
- 是否有 sch_task 异步派单？（看 OP 类是否调 ScheduleService）
- BEC 事件是否双发？（参 `feedback_bec_3layer_async_publish.md` 警告）
