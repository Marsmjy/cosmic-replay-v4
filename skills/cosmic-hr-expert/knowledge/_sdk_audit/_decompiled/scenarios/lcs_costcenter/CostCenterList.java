/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.list.IListView
 *  kd.bos.list.ListShowParameter
 *  kd.bos.mvc.list.AbstractListView
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hrmp.lcs.formplugin.web.costcenter;

import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.IListView;
import kd.bos.list.ListShowParameter;
import kd.bos.mvc.list.AbstractListView;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public class CostCenterList
extends HRDataBaseList {
    private static final String OPPARAM_AFTERCONFIRM = "afterconfirm";

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        String formId;
        IListView listview;
        long pkId;
        super.billListHyperLinkClick(args);
        if (!HRStringUtils.equals((String)"bos_listf7", (String)this.getView().getEntityId()) && HRStringUtils.equals((String)"number", (String)args.getHyperLinkClickEvent().getFieldName()) && !CostCenterList.checkIsDeleted(pkId = ((Long)(listview = (IListView)this.getView()).getFocusRowPkId()).longValue(), formId = ((AbstractListView)this.getView()).getBillFormId())) {
            String errorTips = ResManager.loadKDString((String)"\u6570\u636e\u5df2\u4e0d\u5b58\u5728\u3002", (String)"CostCenterList_0", (String)"hrmp-lcs-formplugin", (Object[])new Object[0]);
            this.getView().showErrorNotification(errorTips);
        }
    }

    public void setFilter(SetFilterEvent event) {
        super.setFilter(event);
        ListShowParameter listShowParameter = (ListShowParameter)this.getView().getFormShowParameter();
        if (listShowParameter.isLookUp()) {
            List qFilters = event.getQFilters();
            qFilters.removeIf(filter -> Objects.nonNull(filter) && HRStringUtils.equals((String)filter.getProperty(), (String)"enable"));
        }
    }

    private static boolean checkIsDeleted(long pkId, String formId) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper(formId);
        String selectProperties = "id";
        DynamicObject dyObj = helper.queryOriginalOne(selectProperties, (Object)pkId);
        return null != dyObj;
    }
}
