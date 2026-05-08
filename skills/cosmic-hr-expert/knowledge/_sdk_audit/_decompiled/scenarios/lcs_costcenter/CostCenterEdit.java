/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.mvc.base.BaseView
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hrmp.lcs.formplugin.web.costcenter;

import java.util.EventObject;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.mvc.base.BaseView;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public class CostCenterEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    public void registerListener(EventObject args) {
        super.registerListener(args);
        BasedataEdit parentEdit = (BasedataEdit)this.getView().getControl("parent");
        parentEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void afterBindData(EventObject e) {
        BillOperationStatus billStatus = ((BillShowParameter)this.getView().getFormShowParameter()).getBillStatus();
        String status = this.getModel().getDataEntity().getString("status");
        if (this.getView() instanceof BaseView && BillOperationStatus.VIEW.equals((Object)billStatus)) {
            switch (status) {
                case "C": {
                    ((BaseView)this.getView()).setBillStatus(BillOperationStatus.AUDIT);
                    break;
                }
                case "B": {
                    ((BaseView)this.getView()).setBillStatus(BillOperationStatus.SUBMIT);
                    break;
                }
            }
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent event) {
        String fieldKey;
        switch (fieldKey = event.getProperty().getName()) {
            case "parent": {
                long parentId = this.getModel().getDataEntity().getLong("id");
                QFilter idFilter = new QFilter("id", "!=", (Object)parentId);
                event.addCustomQFilter(idFilter);
                break;
            }
        }
    }
}
