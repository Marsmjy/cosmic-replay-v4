/*
 * ═════ SOURCE ═════
 *   项目: hr-hdm-orgtransfer (dcs/hr-hdm-orgtransfer/)
 *   源文件: code/hr/khr-hr-hdm/src/main/java/khr/hr/hdm/orgtransfer/business/OrgParentChgTask.java
 *   采集日期: 2026-04-24 · 摘录核心骨架 · 原文件 ~130 行 · 本模板留骨架 ~70 行
 *
 * ═════ INTENT ═════
 *   主 intent: schedule_task_batch_process
 *   次 intent: org_transfer_mass · offboarding_auto_revoke · 任何"定时任务"场景
 *
 * ═════ VARIABLES ═════
 *   {{PACKAGE}}          · 包路径
 *   {{CLASS_NAME}}       · Task 类名（以 Task 结尾）
 *   {{ISV_APP}}          · ISV 包名
 *   {{TASK_DESC}}        · 任务业务描述（中文）
 *
 * ═════ CAUTION ═════
 *   ⚠ 继承：HR 基础调度任务通常继承 AbstractTask 或项目内部的 AbstractCommonTask
 *     → 如果项目有公共基类（OrgTransferHelper / AbstractCommonTask）· 继承它 · 复用基础设施
 *     → 纯独立任务：extends kd.bos.schedule.executor.AbstractTask
 *   ⚠ execute(RequestContext, Map) 方法是入口 · 参数 map 是调度配置传的参数
 *   ⚠ 必须 try-catch · 抛异常会导致调度任务失败 · 日志不友好
 *   ⚠ 幂等：同一个任务被重复调度（因调度系统重试）时必须不产生重复数据
 *     → 用中间表的 executeStatus 字段标记"已处理" · 跳过
 *   ⚠ 分片：数据量 > 10000 时要分页处理 · 避免超时（默认 5 分钟）
 *   ⚠ 跨模块服务调用 · 用 HRMServiceHelper.invokeHRMPService("模块号", "接口名", "方法名", 参数数组)
 *     → 不要直接 import 跨模块 jar · 会造成循环依赖
 */

package {{PACKAGE}};

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.schedule.executor.AbstractTask;
import kd.bos.servicehelper.operation.SaveServiceHelper;
// 如项目有公共基类 · 改成 extends AbstractCommonTask
// import xxx.AbstractCommonTask;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {{CLASS_NAME}}
 *
 * {{TASK_DESC}}
 *
 * 注册方式：在 BOS 设计器 → 调度任务 → 新增 · 绑定本类 FQN
 *    调度计划：cron 表达式（如每日 02:00）
 */
public class {{CLASS_NAME}} extends AbstractTask {

    private static final Log LOGGER = LogFactory.getLog({{CLASS_NAME}}.class);

    @Override
    public void execute(RequestContext requestContext, Map<String, Object> map) {
        LOGGER.info("{{CLASS_NAME}}: {{TASK_DESC}} · 开始执行...");
        try {
            // ─── Step 1 · 读参数 · 取数据 ──────────
            // 示例：按参数里的 taskDays 取最近 N 天的数据
            Object taskDays = map.get("taskDays");

            // ─── Step 2 · 查询待处理数据 ──────────
            DynamicObject[] pendingData = queryPendingData(taskDays);
            if (Objects.isNull(pendingData) || pendingData.length == 0) {
                LOGGER.info("{{CLASS_NAME}}: 无待处理数据 · 任务结束");
                return;
            }
            LOGGER.info("{{CLASS_NAME}}: 待处理 {} 条", pendingData.length);

            // ─── Step 3 · 逐条处理（建议分片 · 示例单批）──────────
            int batchSize = 500;  // 按需调整
            for (int i = 0; i < pendingData.length; i += batchSize) {
                int end = Math.min(i + batchSize, pendingData.length);
                DynamicObject[] batch = Arrays.copyOfRange(pendingData, i, end);
                processBatch(batch);
            }

        } catch (Exception e) {
            LOGGER.error("{{CLASS_NAME}}: {{TASK_DESC}} · 执行异常 · ", e);
            // 不要 rethrow · 任务失败由日志追溯
        }
        LOGGER.info("{{CLASS_NAME}}: 执行结束");
    }

    /**
     * 查询待处理数据 · TODO 实现业务查询
     */
    private DynamicObject[] queryPendingData(Object taskDays) {
        // 示例：查 executeStatus='pending' 的中间表
        return new DynamicObject[0];
    }

    /**
     * 处理一批 · 幂等：处理完置 executeStatus='done'
     */
    private void processBatch(DynamicObject[] batch) {
        for (DynamicObject row : batch) {
            try {
                // TODO 业务处理逻辑
                row.set("khr_executestatus", "done");
                row.set("khr_resultmsg", "处理成功");
            } catch (Exception e) {
                row.set("khr_executestatus", "error");
                row.set("khr_resultmsg", e.getMessage());
                LOGGER.error("处理单条失败 · ", e);
            }
        }
        SaveServiceHelper.save(batch);
    }
}
