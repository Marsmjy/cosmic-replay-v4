/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.BillListHyperLinkClickEvent
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.BillList
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hrcs.bussiness.servicehelper.activity.ActivitySchemeServiceHelper
 *  kd.hr.hrcs.formplugin.web.activity.BaseActivityTreeListPlugin
 */
package kd.hr.hrcs.formplugin.web.activity;

import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.BillListHyperLinkClickEvent;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hrcs.bussiness.servicehelper.activity.ActivitySchemeServiceHelper;
import kd.hr.hrcs.formplugin.web.activity.BaseActivityTreeListPlugin;

@ExcludeFromJacocoGeneratedReport
public final class ActivitySchemeTreeListPlugin
extends BaseActivityTreeListPlugin {
    private static final String ACTION_ID_DETAIL = "action_id_detail";

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        super.billListHyperLinkClick(args);
        args.setCancel(true);
        Long id = (Long)((BillListHyperLinkClickEvent)args.getHyperLinkClickEvent()).getCurrentRow().getPrimaryKeyValue();
        this.showScheme(id);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        String actionId = closedCallBackEvent.getActionId();
        if (ACTION_ID_DETAIL.equalsIgnoreCase(actionId)) {
            this.refreshList();
        }
    }

    public void filterContainerInit(FilterContainerInitArgs args) {
        super.filterContainerInit(args);
    }

    public void setFilter(SetFilterEvent evt) {
        super.setFilter(evt);
        evt.getQFilters().add(new QFilter("islatest", "=", (Object)"1"));
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate formOperate = (FormOperate)args.getSource();
        if ("history".equalsIgnoreCase(formOperate.getOperateKey())) {
            ListSelectedRowCollection listSelectedData = formOperate.getListSelectedData();
            if (listSelectedData.size() > 1) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u4e0d\u652f\u6301\u591a\u6761\u6d3b\u52a8\u65b9\u6848\uff0c\u8bf7\u9009\u62e9\u4e00\u6761\u6d3b\u52a8\u65b9\u6848\u3002", (String)"ActivitySchemeTreeListPlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            } else if (listSelectedData.size() <= 0) {
                this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u4e00\u6761\u6d3b\u52a8\u65b9\u6848\u3002", (String)"ActivitySchemeTreeListPlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            } else {
                Long pkId = (Long)listSelectedData.get(0).getPrimaryKeyValue();
                DynamicObject item = ActivitySchemeServiceHelper.getById((Long)pkId);
                this.showHistory(item.getLong("sequence"));
            }
        } else if ("refresh".equalsIgnoreCase(formOperate.getOperateKey())) {
            this.refreshList();
        }
    }

    private void showHistory(Long sequence) {
        ListShowParameter formShowParameter = new ListShowParameter();
        formShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        formShowParameter.setBillFormId("hrcs_activityschem_layout");
        formShowParameter.setShowTitle(false);
        formShowParameter.setCustomParam("sequence", (Object)sequence);
        this.getView().showForm((FormShowParameter)formShowParameter);
    }

    private void showScheme(Long id) {
        BillShowParameter formShowParameter = new BillShowParameter();
        formShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        formShowParameter.setFormId("hrcs_activityscheme");
        formShowParameter.setPkId((Object)id);
        formShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, ACTION_ID_DETAIL));
        formShowParameter.setStatus(OperationStatus.VIEW);
        formShowParameter.setBillStatus(BillOperationStatus.VIEW);
        formShowParameter.setCustomParam("changeFlag", this.getView().getFormShowParameter().getCustomParam("changeFlag"));
        this.getView().showForm((FormShowParameter)formShowParameter);
    }

    private void refreshList() {
        BillList billList = (BillList)this.getControl("billlistap");
        billList.clearSelection();
        billList.refresh();
    }
}
