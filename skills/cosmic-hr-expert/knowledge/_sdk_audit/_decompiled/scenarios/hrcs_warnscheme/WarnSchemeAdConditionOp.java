/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hrcs.common.constants.earlywarn.WarnAdConditionConstants
 *  kd.hr.hrcs.opplugin.web.earlywarn.ad.WarnAdOp
 */
package kd.hr.hrcs.opplugin.web.earlywarn.scheme;

import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hrcs.common.constants.earlywarn.WarnAdConditionConstants;
import kd.hr.hrcs.opplugin.web.earlywarn.ad.WarnAdOp;

@ExcludeFromJacocoGeneratedReport
public final class WarnSchemeAdConditionOp
extends WarnAdOp
implements WarnAdConditionConstants {
    protected String getWarnAdType() {
        return "warnSchemeAD";
    }
}
