/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hrcs.opplugin.web.strategy.ManageStrategyLogManager
 */
package kd.hr.hrcs.opplugin.web.strategy;

import java.util.Locale;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hrcs.opplugin.web.strategy.ManageStrategyLogManager;

public final class ManageStrategyLogOp
extends AbstractOperationServicePlugIn {
    private ManageStrategyLogManager manageStrategyLogManager = new ManageStrategyLogManager();

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        String operationKey = args.getOperationKey().toLowerCase(Locale.ROOT);
        DynamicObject[] dys = args.getDataEntities();
        if (dys.length == 0) {
            return;
        }
        String formId = dys[0].getDynamicObjectType().getName();
        if (HRBaseDataConfigUtil.getRecordLog((String)formId)) {
            this.manageStrategyLogManager.init(dys[0].getDynamicObjectType(), dys, operationKey);
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        DynamicObject[] dys = args.getDataEntities();
        if (dys.length == 0) {
            return;
        }
        String formId = dys[0].getDynamicObjectType().getName();
        if (HRBaseDataConfigUtil.getRecordLog((String)formId)) {
            this.manageStrategyLogManager.batchInsertLog(dys);
        }
    }
}
