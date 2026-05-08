/*
 * ═════ SOURCE ═════
 *   项目: 合同续签 (dcs/合同续签/)
 *   源文件: code/tdkw-hlcm-renew/src/main/java/tdkw/hr/hlcmext/plugin/RenewBatchOpPlugin.java
 *   采集日期: 2026-04-24 · 摘录核心模式 · 原文件 240 行 · 本模板留精髓 ~80 行
 *
 * ═════ INTENT ═════
 *   主 intent: before_save_validation · before_save_field_supplement
 *   次 intent: generic_new_bill_template · contract_renewal
 *   本模板解决: 同一个 OP 类里处理多个 operationKey（save/submit/audit...）· 按 key 分流
 *
 * ═════ VARIABLES ═════
 *   {{PACKAGE}}           · 包路径
 *   {{CLASS_NAME}}        · 类名
 *   {{ISV_APP}}           · ISV 包名
 *   {{EXTRA_FIELDS}}      · 需要在 onPreparePropertys 补充加载的字段（逗号串）
 *                            · 场景：数据实体上不是默认字段 · 本插件要读要写的那些
 *
 * ═════ CAUTION ═════
 *   ⚠ onPreparePropertys · 必须显式声明额外字段 · 不然 dataEntity 里取不到
 *     → 常见坑：想取 "tdkw_itemids_tag" · 但没在这里加 · getString 返回 null
 *   ⚠ beforeExecuteOperationTransaction 按 operationKey 分流 · 每个分支写一个独立方法
 *     → 分支太多（>5）建议拆成多个 OP 类 · 分别注册
 *   ⚠ 校验不过 · 用 e.setCancel(true) + e.setCancelMessage(msg)
 *     → 比 throw KDBizException 更友好 · 前端显示的错误不带堆栈
 *   ⚠ 字段 operationKey 常用值（参考标品） ·
 *       - "save" 保存
 *       - "submit" 提交
 *       - "audit" 审核
 *       - "unaudit" 反审
 *       - "delete" 删除
 *       - "disable" 禁用
 *       - "enable" 启用
 */

package {{PACKAGE}};

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;

import java.util.List;

/**
 * {{CLASS_NAME}}
 *
 * 按 operationKey 分流处理的 OP 插件样板
 * 使用场景：一个实体挂多个操作 (save/submit/audit) 都需要业务拦截
 */
public class {{CLASS_NAME}} extends AbstractOperationServicePlugIn {

    private static final Log logger = LogFactory.getLog({{CLASS_NAME}}.class);

    /**
     * 声明本插件要访问的额外字段 · 不在这里声明 · dataEntity 读不到
     */
    @Override
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List<String> fieldKeys = args.getFieldKeys();
        // TODO 按需补字段 · 示例：
        // fieldKeys.add("tdkw_itemids_tag");
        // fieldKeys.add("tdkw_affiliationord");
        {{EXTRA_FIELDS_LINES}}
    }

    /**
     * 按 operationKey 分流
     */
    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        super.beforeExecuteOperationTransaction(e);
        String operationKey = e.getOperationKey();
        DynamicObject[] dynamicObjects = e.getDataEntities();

        switch (operationKey) {
            case "save":
            case "submit":
                handleSaveOrSubmit(dynamicObjects, e);
                break;
            case "audit":
                handleAudit(dynamicObjects, e);
                break;
            default:
                // 其它 key · 不拦截
                break;
        }
    }

    /**
     * 保存/提交 · TODO 业务逻辑
     */
    private void handleSaveOrSubmit(DynamicObject[] data, BeforeOperationArgs e) {
        // 示例：业务校验 · 不过就 cancel
        for (DynamicObject row : data) {
            if (!checkOrg(row)) {
                e.setCancel(true);
                e.setCancelMessage("工号不属于该所属组织, 请核对数据后重新发起。");  // TODO ResManager.loadKDString
                return;
            }
        }
    }

    /**
     * 审核通过 · TODO 业务后处理（触发下游动作）
     */
    private void handleAudit(DynamicObject[] data, BeforeOperationArgs e) {
        // 示例：审核通过后批量处理关联单据
        logger.info("{{CLASS_NAME}} audit · 处理 {} 条", data.length);
    }

    private boolean checkOrg(DynamicObject row) {
        // TODO 填入业务校验逻辑
        return true;
    }
}
