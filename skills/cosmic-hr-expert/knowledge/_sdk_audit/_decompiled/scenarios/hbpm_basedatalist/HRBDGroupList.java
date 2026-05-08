/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.list.IListView
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.ListRowClickEvent
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 */
package kd.hr.hbp.formplugin.web;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.list.IListView;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.ListRowClickEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public class HRBDGroupList
extends HRDataBaseList {
    private static final Log logger = LogFactory.getLog(HRBDGroupList.class);

    public void listRowClick(ListRowClickEvent evt) {
        evt.setCancel(true);
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        args.setCancel(true);
        IListView listView = (IListView)this.getView();
        this.showBaseDataList(listView);
    }

    public void listRowDoubleClick(ListRowClickEvent evt) {
        evt.setCancel(true);
        IListView listView = (IListView)this.getView();
        this.showBaseDataList(listView);
    }

    private void showBaseDataList(IListView listView) {
        ListSelectedRow baseDataListRow = listView.getCurrentSelectedRowInfo();
        String entityId = listView.getBillFormId();
        String pageKey = this.getPageKeyById((Long)baseDataListRow.getPrimaryKeyValue(), entityId);
        ListShowParameter hrbucaviewlist = new ListShowParameter();
        hrbucaviewlist.setBillFormId(pageKey);
        hrbucaviewlist.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        String caption = baseDataListRow.getName();
        hrbucaviewlist.setCaption(caption);
        hrbucaviewlist.setCustomParam("caption", (Object)caption);
        try {
            this.getView().showForm((FormShowParameter)hrbucaviewlist);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            IListView listview = (IListView)this.getView();
            listview.showTipNotification(ResManager.loadKDString((String)"\u8bf7\u68c0\u67e5\u9875\u9762\u6807\u8bc6\u662f\u5426\u6b63\u786e\u3002", (String)"HRCommonBaseDataList_0", (String)"hrmp-hbp-formplugin", (Object[])new Object[0]));
        }
    }

    private String getPageKeyById(Long baseDataId, String pageKey) {
        HRBaseServiceHelper baseDataListHelper = new HRBaseServiceHelper(pageKey);
        QFilter idFilter = new QFilter("id", "=", (Object)baseDataId);
        QFilter[] idFilterArray = new QFilter[]{idFilter};
        DynamicObject baseDataDy = baseDataListHelper.queryOne("pagekey", idFilterArray);
        return baseDataDy.getString("pagekey");
    }
}

