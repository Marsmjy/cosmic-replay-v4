/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.form.FormShowParameter
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hrmp.lcs.formplugin.web.basedata;

import java.util.EventObject;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.FormShowParameter;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public class CostBizTypeEdit
extends HRDataBaseEdit {
    public void afterBindData(EventObject e) {
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (!OperationStatus.ADDNEW.equals((Object)status)) {
            this.getModel().setDataChanged(false);
            this.getView().setEnable(Boolean.FALSE, new String[]{"name", "number"});
        }
        if (OperationStatus.VIEW.equals((Object)status)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"btnsave"});
        }
        DynamicObject dynamicObject = this.getModel().getDataEntity();
        Boolean isSysPreset = dynamicObject.getBoolean("issyspreset");
        FormShowParameter form = this.getView().getFormShowParameter();
        if (isSysPreset.booleanValue()) {
            form.setStatus(OperationStatus.VIEW);
            this.getView().setEnable(Boolean.FALSE, new String[]{"description"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"btnsave"});
        }
    }
}
