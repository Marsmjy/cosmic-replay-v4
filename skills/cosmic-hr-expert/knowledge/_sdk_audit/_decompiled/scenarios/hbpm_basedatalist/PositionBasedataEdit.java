/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.IBillView
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hrmp.hbpm.formplugin.web.basedata;

import java.util.EventObject;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.IBillView;
import kd.bos.dataentity.entity.DynamicObject;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class PositionBasedataEdit
extends HRDataBaseEdit {
    public void afterBindData(EventObject e) {
        DynamicObject basedataEntity = this.getView().getModel().getDataEntity();
        boolean issyspreset = basedataEntity.getBoolean("issyspreset");
        if (issyspreset) {
            ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
        }
    }
}

