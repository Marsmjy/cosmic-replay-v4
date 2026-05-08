/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.list.BillList
 *  kd.bos.list.IListView
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrcs.bussiness.domain.service.earlywarn.repository.CommonRepository
 */
package kd.hr.hrcs.formplugin.web.earlywarn.log;

import kd.bos.base.BaseShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.list.BillList;
import kd.bos.list.IListView;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrcs.bussiness.domain.service.earlywarn.repository.CommonRepository;

@ExcludeFromJacocoGeneratedReport
public final class EarlyWarnLogListPlugin
extends HRDataBaseList {
    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        BillList list = (BillList)args.getHyperLinkClickEvent().getSource();
        Object primaryKeyValue = list.getFocusRowPkId();
        IFormView formView = this.getView();
        if (formView instanceof IListView) {
            IListView view = (IListView)this.getView();
            String billFormId = view.getBillFormId();
            if (primaryKeyValue != null) {
                DynamicObject dynamicObject = CommonRepository.queryDynamicObjectByPk((String)billFormId, (String)"", (Object)primaryKeyValue);
                BaseShowParameter baseShowParameter = new BaseShowParameter();
                baseShowParameter.setFormId(billFormId);
                baseShowParameter.setCaption(dynamicObject.getString("schemename"));
                baseShowParameter.setStatus(OperationStatus.VIEW);
                baseShowParameter.setPkId(primaryKeyValue);
                baseShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                if (this.getView().getParentView() != null) {
                    baseShowParameter.setPageId(this.getView().getParentView().getPageId() + billFormId + "_" + primaryKeyValue);
                }
                this.getView().showForm((FormShowParameter)baseShowParameter);
                args.setCancel(true);
            }
        }
    }
}
