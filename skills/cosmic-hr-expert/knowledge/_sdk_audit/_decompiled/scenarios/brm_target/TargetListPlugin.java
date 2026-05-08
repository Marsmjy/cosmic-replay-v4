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
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.list.IListView
 *  kd.bos.list.plugin.AbstractListPlugin
 */
package kd.hr.brm.formplugin.web;

import java.util.List;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.filter.ControlFilter;
import kd.bos.entity.filter.ControlFilters;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.list.IListView;
import kd.bos.list.plugin.AbstractListPlugin;

public final class TargetListPlugin
extends AbstractListPlugin {
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        AbstractOperate op;
        String operateKey;
        super.beforeDoOperation(args);
        if (args.getSource() instanceof AbstractOperate && "new".equals(operateKey = (op = (AbstractOperate)args.getSource()).getOperateKey())) {
            this.handleNewTarget(args);
        }
    }

    private void handleNewTarget(BeforeDoOperationEventArgs args) {
        BillShowParameter showParameter = new BillShowParameter();
        ControlFilters filters = ((IListView)this.getView()).getControlFilters();
        ControlFilter filter = (ControlFilter)filters.getFilters().get("bu.id");
        if (null != filter) {
            List values = filter.getValue();
            showParameter.setCustomParam("bu", (Object)values.get(0).toString());
        }
        args.setCancel(true);
        showParameter.setFormId("brm_target");
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setStatus(OperationStatus.ADDNEW);
        showParameter.setCaption(ResManager.loadKDString((String)"\u6307\u6807\u7ba1\u7406", (String)"TargetListPlugin_1", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
        this.getView().showForm((FormShowParameter)showParameter);
    }
}
