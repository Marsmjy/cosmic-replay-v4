/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.filter.ControlFilter
 *  kd.bos.entity.filter.ControlFilters
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.list.IListView
 *  kd.hr.brm.business.web.RosterHelper
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonList
 */
package kd.hr.brm.formplugin.web;

import java.util.List;
import java.util.stream.Collectors;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.filter.ControlFilter;
import kd.bos.entity.filter.ControlFilters;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.IListView;
import kd.hr.brm.business.web.RosterHelper;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonList;

public final class SpecialListListPlugin
extends HRBaseDataCommonList {
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        AbstractOperate op;
        String operateKey;
        super.beforeDoOperation(args);
        if (args.getSource() instanceof AbstractOperate && "new".equals(operateKey = (op = (AbstractOperate)args.getSource()).getOperateKey())) {
            this.handleNewSpecialList(args);
        }
    }

    private void handleNewSpecialList(BeforeDoOperationEventArgs args) {
        BillShowParameter showParameter = new BillShowParameter();
        ControlFilters filters = ((IListView)this.getView()).getControlFilters();
        ControlFilter filter = (ControlFilter)filters.getFilters().get("bu.id");
        if (null != filter) {
            List values = filter.getValue();
            showParameter.setCustomParam("bu", (Object)values.get(0).toString());
        }
        args.setCancel(true);
        showParameter.setFormId("brm_special_list");
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setStatus(OperationStatus.ADDNEW);
        showParameter.setCaption(ResManager.loadKDString((String)"\u540d\u5355\u7ba1\u7406", (String)"SpecialListListPlugin_1", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
        this.getView().showForm((FormShowParameter)showParameter);
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        FormOperate op = (FormOperate)args.getSource();
        String operateKey = op.getOperateKey();
        List<Long> ids = op.getListSelectedData().stream().map(dy -> (Long)dy.getPrimaryKeyValue()).collect(Collectors.toList());
        switch (operateKey) {
            case "audit": 
            case "enable": 
            case "disable": 
            case "delete": {
                this.deleteCache(ids);
                break;
            }
        }
    }

    private void deleteCache(List<Long> ids) {
        RosterHelper helper = new RosterHelper();
        if (!ids.isEmpty()) {
            ids.forEach(arg_0 -> ((RosterHelper)helper).deleteRosterCache(arg_0));
        } else {
            Long id = (Long)this.getModel().getValue("id");
            helper.deleteRosterCache(id);
        }
    }
}
