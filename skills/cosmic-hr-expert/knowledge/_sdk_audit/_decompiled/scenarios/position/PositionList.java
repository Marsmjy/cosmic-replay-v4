/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.FilterContainerInitArgs
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.operate.MutexHelper
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.common.constants.history.HisPageEnum
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hrmp.hbpm.formplugin.web.position;

import java.util.List;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.operate.MutexHelper;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.constants.history.HisPageEnum;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public class PositionList
extends HRDataBaseList {
    public void filterContainerInit(FilterContainerInitArgs args) {
        super.filterContainerInit(args);
        if (HRStringUtils.equals((String)((String)this.getView().getFormShowParameter().getCustomParam("hisPage")), (String)HisPageEnum.VERSION_LIST_PAGE.getPage())) {
            List filterColumns = args.getCommonFilterColumns();
            filterColumns.removeIf(filterCol -> HRStringUtils.equals((String)filterCol.getFieldName(), (String)"datastatus"));
        }
    }

    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        String listEntityId = this.getView().getEntityId();
        if (HRStringUtils.equals((String)listEntityId, (String)"hbpm_posorgtreelistf7") && HRStringUtils.equals((String)"1", (String)this.getView().getPageCache().get("viewstdpos"))) {
            setFilterEvent.getQFilters().add(new QFilter("isstandardpos", "=", (Object)"1"));
            return;
        }
        setFilterEvent.getQFilters().add(new QFilter("isstandardpos", "=", (Object)"0"));
        setFilterEvent.getQFilters().add(new QFilter("isdeleted", "!=", (Object)"1"));
        String newOrderBy = "adminorg.id asc,positiontype.id asc,isleader desc";
        setFilterEvent.setOrderBy(newOrderBy);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        switch (operateKey = operate.getOperateKey()) {
            case "hisversion_view": {
                ListSelectedRowCollection rows = args.getListSelectedData();
                if (rows.size() > 1) {
                    String msg = ResManager.loadKDString((String)"\u53ea\u80fd\u9009\u62e9\u4e00\u6761\u6570\u636e\u3002", (String)"PositionList_0", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
                    this.getView().showTipNotification(msg);
                    break;
                }
                Long id = (Long)rows.get(0).getPrimaryKeyValue();
                if (!this.doMutex(args, id).booleanValue()) break;
                FormShowParameter showParameter = new FormShowParameter();
                showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                showParameter.setFormId("hbpm_positionrevise");
                if (this.getView().getMainView() == null) {
                    showParameter.setPageId("hbpm_positionrevise" + id);
                } else {
                    showParameter.setPageId("hbpm_positionrevise" + id + this.getView().getMainView().getPageId());
                }
                showParameter.setCaption(ResManager.loadKDString((String)"\u5386\u53f2\u8bb0\u5f55-%s", (String)"PositionList_1", (String)"hrmp-hbpm-formplugin", (Object[])new Object[]{rows.get(0).getName()}));
                showParameter.setCustomParam("position", (Object)id);
                this.getView().showForm(showParameter);
                break;
            }
        }
    }

    private Boolean doMutex(BeforeDoOperationEventArgs args, Long id) {
        StringBuilder errMsg = new StringBuilder();
        String entityId = "hbpm_positionhr";
        if (!MutexHelper.require((String)entityId, (Object)String.valueOf(id), (String)"modify", (boolean)true, (StringBuilder)errMsg)) {
            args.setCancel(true);
            this.getView().showTipNotification(errMsg.toString());
            return false;
        }
        return true;
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        List successList;
        super.afterDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if (HRStringUtils.equals((String)operateKey, (String)"enable") && (successList = args.getOperationResult().getSuccessPkIds()) != null && successList.size() > 0) {
            ListView listView = (ListView)this.getView();
            listView.refresh();
        }
    }
}
