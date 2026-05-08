/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.IBillView
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.hrcs.formplugin.web.activity;

import java.util.EventObject;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.IBillView;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class ActivityExceptionLogEdit
extends HRDataBaseEdit {
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
    }
}
