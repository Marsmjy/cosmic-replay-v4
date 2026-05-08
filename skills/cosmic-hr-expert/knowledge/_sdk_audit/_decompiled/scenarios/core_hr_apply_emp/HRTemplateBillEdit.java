/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.IBillView
 *  kd.bos.bill.OperationStatus
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit
 */
package kd.hr.hbp.formplugin.web.template;

import java.util.EventObject;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.IBillView;
import kd.bos.bill.OperationStatus;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit;

public class HRTemplateBillEdit
extends HRCoreBaseBillEdit {
    private static final String FIELD_ISEXISTSWORKFLOW = "isexistsworkflow";

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
        if (this.getView().getFormShowParameter().getStatus() == OperationStatus.VIEW) {
            ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
            return;
        }
        this.setButtonStatus();
    }

    protected void setButtonStatus() {
        String billStatus = (String)this.getModel().getValue("billstatus");
        boolean isExistsWF = (Boolean)this.getModel().getValue(FIELD_ISEXISTSWORKFLOW);
        switch (billStatus) {
            case "A": {
                if (this.getView().getFormShowParameter().getStatus() == OperationStatus.ADDNEW) break;
                ((IBillView)this.getView()).setBillStatus(BillOperationStatus.EDIT);
                break;
            }
            case "G": {
                ((IBillView)this.getView()).setBillStatus(BillOperationStatus.EDIT);
                break;
            }
            case "B": 
            case "D": {
                ((IBillView)this.getView()).setBillStatus(BillOperationStatus.SUBMIT);
                break;
            }
            case "E": 
            case "C": {
                ((IBillView)this.getView()).setBillStatus(BillOperationStatus.AUDIT);
                if (isExistsWF) {
                    this.getView().setVisible(Boolean.TRUE, new String[]{"bar_viewflowchart"});
                    break;
                }
                this.getView().setVisible(Boolean.FALSE, new String[]{"bar_viewflowchart"});
                break;
            }
            case "F": {
                ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
                break;
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        this.getView().updateView("auditstatus");
        this.setPageStatus(afterDoOperationEventArgs);
    }

    protected void setPageStatus(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        OperationResult opResult = afterDoOperationEventArgs.getOperationResult();
        if (opResult == null || !opResult.isSuccess()) {
            return;
        }
        this.setButtonStatus();
    }
}
