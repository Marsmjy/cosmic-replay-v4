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
 *  kd.hr.hrcs.common.constants.earlywarn.WarnSceneComConditionConstants
 *  kd.hr.hrcs.opplugin.validator.earlywarn.scene.WarnSceneCommonConditionValidator
 */
package kd.hr.hrcs.opplugin.web.earlywarn.scene;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.common.constants.earlywarn.WarnSceneComConditionConstants;
import kd.hr.hrcs.opplugin.validator.earlywarn.scene.WarnSceneCommonConditionValidator;

@ExcludeFromJacocoGeneratedReport
public final class WarnSceneCommonConditionOp
extends HRDataBaseOp
implements WarnSceneComConditionConstants {
    private static final Log LOG = LogFactory.getLog(WarnSceneCommonConditionOp.class);

    public void onAddValidators(AddValidatorsEventArgs eventArgs) {
        try {
            super.onAddValidators(eventArgs);
            eventArgs.addValidator((AbstractValidator)new WarnSceneCommonConditionValidator());
        }
        catch (Exception exception) {
            LOG.error("error:", (Throwable)exception);
        }
    }
}
