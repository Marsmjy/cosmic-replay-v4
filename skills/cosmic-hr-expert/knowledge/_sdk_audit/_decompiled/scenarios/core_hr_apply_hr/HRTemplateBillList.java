/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.mvc.list.ListView
 *  kd.hr.hbp.formplugin.web.HRCoreBaseBillList
 */
package kd.hr.hbp.formplugin.web.template;

import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.mvc.list.ListView;
import kd.hr.hbp.formplugin.web.HRCoreBaseBillList;

public class HRTemplateBillList
extends HRCoreBaseBillList {
    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        this.refreshList(afterDoOperationEventArgs);
    }

    private void refreshList(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        FormOperate formOperate = (FormOperate)afterDoOperationEventArgs.getSource();
        OperationResult opResult = afterDoOperationEventArgs.getOperationResult();
        if (opResult == null || !opResult.isSuccess()) {
            return;
        }
        switch (formOperate.getOperateKey()) {
            case "delete": {
                this.refreshListAfterDelete(afterDoOperationEventArgs);
                break;
            }
        }
    }

    private void refreshListAfterDelete(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        ListView listView = (ListView)this.getView();
        listView.refresh();
    }
}
