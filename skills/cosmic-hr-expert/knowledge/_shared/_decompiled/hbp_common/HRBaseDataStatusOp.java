/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.validator.HRDataBaseConfigValidator
 */
package kd.hr.hbp.opplugin.web.config;

import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.validator.HRDataBaseConfigValidator;

public class HRBaseDataStatusOp
extends AbstractOperationServicePlugIn {
    private static final Log LOGGER = LogFactory.getLog(HRBaseDataStatusOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("status");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new HRDataBaseConfigValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        DynamicObject[] dys = args.getDataEntities();
        String formId = this.billEntityType.getName();
        boolean needAudit = HRBaseDataConfigUtil.getAudit((String)formId);
        LOGGER.info("HRBaseDataStatusOp beforeExecuteOperationTransaction" + needAudit);
        for (DynamicObject dy : dys) {
            if (needAudit) {
                String initHisModelSave;
                if (dy.getDataEntityState().getFromDatabase() || HRStringUtils.equals((String)(initHisModelSave = this.getOption().getVariableValue("forceNoAudit", "false")), (String)"true")) continue;
                dy.set("status", (Object)"A");
                continue;
            }
            LOGGER.info("HRBaseDataStatusOp beforeExecuteOperationTransaction");
            dy.set("status", (Object)"C");
        }
    }
}
