/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BillListHyperLinkClickEvent
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.list.BillList
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hrmp.hbjm.formplugin.web.basedata;

import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BillListHyperLinkClickEvent;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.list.BillList;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import org.apache.commons.lang3.StringUtils;

public final class JobInitFilterCommonPlugin
extends HRDataBaseList {
    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        if (!HRStringUtils.equals((String)((String)this.getView().getFormShowParameter().getCustomParams().get("type")), (String)"list")) {
            return;
        }
        BillListHyperLinkClickEvent hyperLinkClickEvent = (BillListHyperLinkClickEvent)args.getHyperLinkClickEvent();
        Object primaryKeyValue = hyperLinkClickEvent.getCurrentRow().getPrimaryKeyValue();
        BillList billList = (BillList)hyperLinkClickEvent.getSource();
        String formId = billList.getBillFormId();
        if (StringUtils.equals((CharSequence)formId, (CharSequence)"hbjm_jobclasshr") || StringUtils.equals((CharSequence)formId, (CharSequence)"hbjm_joblevelscmhr")) {
            args.setCancel(true);
            BillShowParameter parameter = new BillShowParameter();
            parameter.setPkId(primaryKeyValue);
            parameter.setStatus(OperationStatus.VIEW);
            parameter.setBillStatus(BillOperationStatus.VIEW);
            parameter.setFormId(formId);
            parameter.setPageId(this.getView().getPageId() + primaryKeyValue);
            parameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            this.getView().showForm((FormShowParameter)parameter);
        }
    }
}
