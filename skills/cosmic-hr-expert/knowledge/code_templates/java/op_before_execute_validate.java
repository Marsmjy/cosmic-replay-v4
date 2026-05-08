/*
 * ═════ SOURCE ═════
 *   项目: 编制管理 (dcs/编制管理/)
 *   源文件: code/tdkw-odc-dgdl/src/main/java/dgdl/odc/homs/opplugin/SavePlanOp.java
 *   采集日期: 2026-04-24 · scripts/index_dcs_projects.py 扫描
 *   ISV app: tdkw_odc_dgdl（已脱敏 · 用 {{ISV_APP}} 替换）
 *
 * ═════ INTENT ═════
 *   主 intent: before_save_validation (保存前业务校验)
 *   次 intent: before_save_field_supplement (保存前自动补字段 · 查子表状态)
 *
 * ═════ VARIABLES（填槽位）═════
 *   {{ISV_APP}}               · ISV 扩展包名（如 tdkw_odc_dgdl）
 *   {{PACKAGE}}               · 你的 Java 包路径（如 tdkw.odc.homs.opplugin）
 *   {{CLASS_NAME}}            · 你的类名（以 Op 结尾 · 如 SavePlanOp）
 *   {{REL_ENTITY}}            · 要查询的子表/关联实体 formNumber（如 dgdl_planyear_detail）
 *   {{LOOKUP_FIELDS}}         · load 时要查的字段逗号串（如 "id,billstatus,adminorg"）
 *   {{FILTER_CONDITION}}      · 业务过滤条件（根据你的业务写 QFilter）
 *   {{ERROR_MSG}}             · 校验失败时的错误文案（必须走 ResManager.loadKDString）
 *
 * ═════ CAUTION（从 intent.minefields）═════
 *   ⚠ 本模板在 beforeExecuteOperationTransaction 阶段写
 *     → 这是"保存前 · 事务已开启 · dataEntities 已组装完" 的时机
 *     → afterExecute 阶段修改字段不生效（对象已提交）
 *   ⚠ 校验失败要走 throw new KDBizException · 会自动转成用户友好提示
 *     → 切勿 throw RuntimeException 或 NullPointerException · 栈不友好
 *   ⚠ 错误文案必须 ResManager.loadKDString · 本模板源文件直接写了硬编码字符串（dcs 原样） ·
 *     → 你在生产用时必须改成 ResManager.loadKDString（见 HR 导入导出 R7）
 *   ⚠ 要做跨行/跨表校验时 · 推荐改用 Validator 类（模板 validator_custom.java）
 *     → Validator 有统一错误收集和行级错误定位 · OP 插件适合单记录批量校验
 */

package {{PACKAGE}};

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.exception.KDBizException;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;

/**
 * {{CLASS_NAME}}
 *
 * 保存前校验 · 场景：对每条 dataEntity 查一次关联实体 {{REL_ENTITY}}，
 * 校验某业务条件（如子表所有行状态 != I 就阻断保存）。
 *
 * 注册方式：在 BOS 设计器 → 业务对象 → 操作列表 → save → 插件 ·
 *   className: {{PACKAGE}}.{{CLASS_NAME}}
 *   targetType: OPERATION
 */
public class {{CLASS_NAME}} extends AbstractOperationServicePlugIn {

    private static final Log logger = LogFactory.getLog({{CLASS_NAME}}.class);

    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        super.beforeExecuteOperationTransaction(e);
        DynamicObject[] dataEntities = e.getDataEntities();

        for (DynamicObject dataEntry : dataEntities) {
            // ─── Step 1 · 从主记录取查询所需字段 ────────
            // TODO 替换为你的字段 key（示例：从分录取末级组织 + 编制年度）
            // DynamicObject keyObj = dataEntry.getDynamicObject("{{YOUR_FK_FIELD}}");

            // ─── Step 2 · 构造 QFilter 查询关联实体 ────────
            QFilter filter = new QFilter("{{FILTER_FIELD}}", QCP.equals, /* keyObj */ null);
            // 按需加更多条件 · .and("status", QCP.not_equals, "I")

            // ─── Step 3 · 批量查询 ────────
            DynamicObject[] relatedObjs = BusinessDataServiceHelper.load(
                "{{REL_ENTITY}}", "{{LOOKUP_FIELDS}}", filter.toArray());

            logger.info("{{CLASS_NAME}} 查询到 {} 条关联数据", relatedObjs.length);

            // ─── Step 4 · 逐条校验 · 不满足条件抛 KDBizException ────────
            for (DynamicObject obj : relatedObjs) {
                if (!checkCondition(obj)) {
                    // TODO 生产环境改成 ResManager.loadKDString("...", "{{CLASS_NAME}}_0", "{{ISV_APP}}")
                    throw new KDBizException("{{ERROR_MSG}}");
                }
            }
        }
    }

    /**
     * 校验单条关联记录 · TODO 按业务实现
     */
    private boolean checkCondition(DynamicObject obj) {
        // 示例：仅当子表状态为 I（已确认）时通过校验
        String status = obj.getString("billstatus");
        return "I".equals(status);
    }
}
