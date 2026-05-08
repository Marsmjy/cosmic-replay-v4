/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.common.constants.earlywarn.WarnSchemeBaseConditionConstants
 *  kd.hr.hrcs.opplugin.validator.earlywarn.scheme.WarnSchemeBaseConditionValidator
 */
package kd.hr.hrcs.opplugin.web.earlywarn.scheme;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.common.constants.earlywarn.WarnSchemeBaseConditionConstants;
import kd.hr.hrcs.opplugin.validator.earlywarn.scheme.WarnSchemeBaseConditionValidator;

@ExcludeFromJacocoGeneratedReport
public final class WarnSchemeBaseConditionOp
extends HRDataBaseOp
implements WarnSchemeBaseConditionConstants {
    private static final Log LOG = LogFactory.getLog(WarnSchemeBaseConditionOp.class);

    public void onAddValidators(AddValidatorsEventArgs eventArgs) {
        try {
            super.onAddValidators(eventArgs);
            eventArgs.addValidator((AbstractValidator)new WarnSchemeBaseConditionValidator());
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }
}
