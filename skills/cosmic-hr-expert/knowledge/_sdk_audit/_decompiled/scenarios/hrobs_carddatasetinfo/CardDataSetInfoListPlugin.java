/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.RefEntityType
 *  kd.bos.entity.datamodel.events.BeforePackageDataEvent
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.BillListHyperLinkClickEvent
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.HyperLinkClickArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.plugin.AbstractListPlugin
 *  kd.bos.orm.query.QFilter
 *  kd.bos.servicehelper.QueryServiceHelper
 *  kd.hr.hbp.common.util.HRCollUtil
 *  kd.hrmp.hrobs.business.workbench.engine.carddataset.repository.CardDataSetConfigRepository
 */
package kd.hrmp.hrobs.formplugin.workbench.datasetconfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.RefEntityType;
import kd.bos.entity.datamodel.events.BeforePackageDataEvent;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BillListHyperLinkClickEvent;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.HyperLinkClickArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.hr.hbp.common.util.HRCollUtil;
import kd.hrmp.hrobs.business.workbench.engine.carddataset.repository.CardDataSetConfigRepository;

public final class CardDataSetInfoListPlugin
extends AbstractListPlugin {
    public void setFilter(SetFilterEvent event) {
        super.setFilter(event);
        if (this.isLookup()) {
            QFilter enableFilter = new QFilter("enable", "=", (Object)"1");
            event.getQFilters().add(enableFilter);
        }
    }

    private boolean isLookup() {
        boolean isLookup = false;
        if (this.getView().getFormShowParameter() instanceof ListShowParameter) {
            ListShowParameter listShowParameter = (ListShowParameter)this.getView().getFormShowParameter();
            isLookup = listShowParameter.isLookUp();
        }
        return isLookup;
    }

    public void beforePackageData(BeforePackageDataEvent e) {
        super.beforePackageData(e);
        DynamicObjectCollection pageData = e.getPageData();
        if (HRCollUtil.isNotEmpty((Collection)pageData)) {
            Map pageDataByDsType = pageData.stream().collect(Collectors.groupingBy(data -> data.getString("dstype.id"), Collectors.toList()));
            for (Map.Entry entry : pageDataByDsType.entrySet()) {
                List subPageData = entry.getValue();
                DynamicObject dsTypeDy = ((DynamicObject)subPageData.get(0)).getDynamicObject("dstype");
                DynamicObject commonbinddataDy = dsTypeDy.getDynamicObject("commonbinddata");
                RefEntityType commonbinddataEntityType = (RefEntityType)commonbinddataDy.getDataEntityType();
                String entityNumber = commonbinddataDy.getString("number");
                String nameProperty = commonbinddataEntityType.getNameProperty();
                List binddatainfoList = subPageData.stream().map(subPageDataDy -> Long.parseLong(subPageDataDy.getString("binddatainfo"))).collect(Collectors.toList());
                DynamicObjectCollection binddatainfoDyColl = QueryServiceHelper.query((String)entityNumber, (String)("id," + nameProperty), (QFilter[])new QFilter[]{new QFilter("id", "in", binddatainfoList)});
                Map<Long, DynamicObject> binddatainfoDyMap = binddatainfoDyColl.stream().collect(Collectors.toMap(binddatainfoDy -> binddatainfoDy.getLong("id"), binddatainfoDy -> binddatainfoDy, (oldValue, newValue) -> newValue));
                for (DynamicObject subPageDataDy2 : subPageData) {
                    long binddatainfo = Long.parseLong(subPageDataDy2.getString("binddatainfo"));
                    DynamicObject binddatainfoDy2 = binddatainfoDyMap.get(binddatainfo);
                    String binddatainfoShow = binddatainfoDy2.getString(nameProperty);
                    subPageDataDy2.set("binddatainfo", (Object)binddatainfoShow);
                }
            }
        }
    }

    public void billListHyperLinkClick(HyperLinkClickArgs args) {
        String colKey = args.getFieldName();
        ListShowParameter listShowParameter = (ListShowParameter)this.getView().getFormShowParameter();
        if (listShowParameter.isLookUp()) {
            super.billListHyperLinkClick(args);
            return;
        }
        BillListHyperLinkClickEvent billListHyperLinkClickEvent = (BillListHyperLinkClickEvent)args.getHyperLinkClickEvent();
        if ("number".equals(colKey)) {
            Object primaryKeyValue = billListHyperLinkClickEvent.getCurrentRow().getPrimaryKeyValue();
            String rowName = billListHyperLinkClickEvent.getCurrentRow().getName();
            this.openDataSetConfigPage(primaryKeyValue, rowName);
            args.setCancel(true);
        }
    }

    private void openDataSetConfigPage(Object pkId, String rowName) {
        Map<String, String> formIdMap = this.getFormIdMap(pkId);
        String binddetailFormId = formIdMap.get("binddetailFormId");
        if (StringUtils.isNotEmpty((CharSequence)binddetailFormId)) {
            BillShowParameter showParameter = new BillShowParameter();
            showParameter.setFormId(binddetailFormId);
            showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            showParameter.setPageId(this.getView().getPageId() + binddetailFormId + "_" + pkId);
            showParameter.setStatus(OperationStatus.EDIT);
            showParameter.setBillStatus(BillOperationStatus.EDIT);
            showParameter.setPkId(pkId);
            DynamicObject cardDataSetInfoDyn = CardDataSetConfigRepository.getInstance().queryBaseOne((Long)pkId);
            showParameter.setCustomParam("carddatasetinfo", (Object)cardDataSetInfoDyn);
            showParameter.setCaption(String.format(ResManager.loadKDString((String)"\u6570\u636e\u96c6-%1$s", (String)"CardDataSetInfoListPlugin_1", (String)"hrmp-hrobs-formplugin", (Object[])new Object[0]), rowName));
            showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "carddatasetconfigClose"));
            this.getView().showForm((FormShowParameter)showParameter);
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId = closedCallBackEvent.getActionId();
        if ("carddatasetconfigClose".equals(actionId)) {
            this.getView().invokeOperation("refresh");
        }
    }

    private Map<String, String> getFormIdMap(Object pkId) {
        DynamicObject cardDataSetInfoDyn = CardDataSetConfigRepository.getInstance().loadDynamicObject((Long)pkId);
        DynamicObject dsTypeDy = cardDataSetInfoDyn.getDynamicObject("dstype");
        HashMap<String, String> formIdMap = new HashMap<String, String>();
        formIdMap.put("binddataFormId", dsTypeDy.getString("commonbinddata.id"));
        formIdMap.put("binddetailFormId", dsTypeDy.getString("commonbinddetailpage.id"));
        return formIdMap;
    }
}
