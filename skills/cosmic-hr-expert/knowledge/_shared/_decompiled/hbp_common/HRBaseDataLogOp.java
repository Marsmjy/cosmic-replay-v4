/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.hbp.business.log.ModifyDirtyManager
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 */
package kd.hr.hbp.opplugin.web.config;

import java.util.Locale;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.business.log.ModifyDirtyManager;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;

public class HRBaseDataLogOp
extends AbstractOperationServicePlugIn {
    private ModifyDirtyManager modifyDirtyManager = new ModifyDirtyManager();

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        String operationKey = args.getOperationKey().toLowerCase(Locale.ROOT);
        DynamicObject[] dys = args.getDataEntities();
        if (dys.length == 0) {
            return;
        }
        String formId = dys[0].getDynamicObjectType().getName();
        if (HRBaseDataConfigUtil.getRecordLog((String)formId)) {
            this.modifyDirtyManager.init(dys[0].getDynamicObjectType(), dys, operationKey);
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        DynamicObject[] dys = args.getDataEntities();
        if (dys.length == 0) {
            return;
        }
        String formId = dys[0].getDynamicObjectType().getName();
        if (HRBaseDataConfigUtil.getRecordLog((String)formId)) {
            this.modifyDirtyManager.batchInsertLog(dys);
        }
    }
}
