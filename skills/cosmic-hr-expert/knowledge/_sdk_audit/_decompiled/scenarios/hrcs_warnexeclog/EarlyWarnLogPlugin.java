/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hrcs.common.constants.earlywarn.EarlyWarnLogConstants
 */
package kd.hr.hrcs.formplugin.web.earlywarn.log;

import java.util.Locale;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hrcs.common.constants.earlywarn.EarlyWarnLogConstants;

@ExcludeFromJacocoGeneratedReport
public final class EarlyWarnLogPlugin
extends HRDataBaseEdit
implements EarlyWarnLogConstants {
    public void preOpenForm(PreOpenFormEventArgs e) {
        super.preOpenForm(e);
        OperationStatus status = e.getFormShowParameter().getStatus();
        String caption = e.getFormShowParameter().getCaption();
        if (OperationStatus.VIEW.equals((Object)status)) {
            String message = String.format(Locale.ROOT, ResManager.loadKDString((String)"%s-\u6267\u884c\u65e5\u5fd7", (String)"EarlyWarnLogPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), caption);
            e.getFormShowParameter().setCaption(message);
        }
    }
}
