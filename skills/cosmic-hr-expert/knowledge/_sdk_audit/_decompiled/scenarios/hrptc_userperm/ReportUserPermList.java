/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.plugin.AbstractListPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.list.ListView
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hrptc.common.constant.perm.ReportUserPermConstants
 */
package kd.hr.hrptc.formplugin.perm;

import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.list.ListView;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hrptc.common.constant.perm.ReportUserPermConstants;

@ExcludeFromJacocoGeneratedReport
public final class ReportUserPermList
extends AbstractListPlugin
implements ReportUserPermConstants {
    private static final Log LOGGER = LogFactory.getLog(ReportUserPermList.class);
    private static final String OP_COPY_PERM = "copyperm";
    private static final String META_NUMBER_COPY_PERM = "hrptc_userpermcopy";

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        try {
            super.beforeDoOperation(args);
            ListView view = (ListView)this.getView();
            FormOperate source = (FormOperate)args.getSource();
            if (OP_COPY_PERM.equals(source.getOperateKey())) {
                ListSelectedRowCollection selectedRows = view.getSelectedRows();
                if (selectedRows == null || selectedRows.isEmpty()) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u9009\u62e9\u9700\u8981\u590d\u5236\u7684\u7528\u6237\u3002", (String)"ReportUserPermList_0", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]));
                    args.setCancel(true);
                } else if (selectedRows.size() > 1) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u53ea\u80fd\u9009\u62e9\u4e00\u4e2a\u7528\u6237\u3002", (String)"ReportUserPermList_1", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]));
                    args.setCancel(true);
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("beforeDoOperation_error_", (Throwable)exception);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        try {
            super.afterDoOperation(afterDoOperationEventArgs);
            OperationResult operationResult = afterDoOperationEventArgs.getOperationResult();
            String operateKey = afterDoOperationEventArgs.getOperateKey();
            if (operationResult != null && operationResult.isSuccess() && OP_COPY_PERM.equals(operateKey)) {
                ListView view = (ListView)this.getView();
                ListSelectedRowCollection selectedRows = view.getSelectedRows();
                ListSelectedRow row = selectedRows.get(0);
                FormShowParameter formShowParameter = new FormShowParameter();
                formShowParameter.setFormId(META_NUMBER_COPY_PERM);
                formShowParameter.setCustomParam("CUSTOM_PARAM_SOURCE_DATA_ID", row.getPrimaryKeyValue());
                formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
                formShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, OP_COPY_PERM));
                this.getView().showForm(formShowParameter);
            }
        }
        catch (Exception exception) {
            LOGGER.error("afterDoOperation_error_", (Throwable)exception);
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        try {
            super.closedCallBack(closedCallBackEvent);
            String actionId = closedCallBackEvent.getActionId();
            if (OP_COPY_PERM.equals(actionId)) {
                this.getView().updateView();
            }
        }
        catch (Exception exception) {
            LOGGER.error("closedCallBack_error_", (Throwable)exception);
        }
    }
}
