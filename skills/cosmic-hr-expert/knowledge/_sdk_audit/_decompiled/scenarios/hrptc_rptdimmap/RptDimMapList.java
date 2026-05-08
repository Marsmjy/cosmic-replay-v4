/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.list.IListDataProvider
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.events.BeforeCreateListDataProviderArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrptc.business.servicehelper.RptDimMapServiceHelper
 *  kd.hr.hrptmc.business.repdesign.ReportPermissionService
 *  kd.hr.hrptmc.business.repdesign.RptCenterPublishService
 */
package kd.hr.hrptc.formplugin.permission;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.list.IListDataProvider;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.events.BeforeCreateListDataProviderArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrptc.business.servicehelper.RptDimMapServiceHelper;
import kd.hr.hrptmc.business.repdesign.ReportPermissionService;
import kd.hr.hrptmc.business.repdesign.RptCenterPublishService;

public final class RptDimMapList
extends HRDataBaseList {
    private static final String CHECK_REF = "checkref";
    private static final String CALLBACK_DEL_DIMENSION = "deldimensioncallback";
    private Map<String, String> publishPathMaps;

    public void beforeCreateListDataProvider(BeforeCreateListDataProviderArgs args) {
        this.initPublishPath();
        args.setListDataProvider((IListDataProvider)new /* Unavailable Anonymous Inner Class!! */);
    }

    private void initPublishPath() {
        this.publishPathMaps = RptCenterPublishService.getPublishPathMaps();
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        if (CHECK_REF.equals(operateKey)) {
            ListSelectedRowCollection rowColls = this.getSelectedRows();
            List rptDimMapIds = rowColls.stream().map(row -> (Long)row.getPrimaryKeyValue()).collect(Collectors.toList());
            List rptManageIds = RptDimMapServiceHelper.getRptIds(rptDimMapIds);
            if (ReportPermissionService.checkPermission((List)rptManageIds)) {
                String tips = ResManager.loadKDString((String)"\u5f53\u524d\u9009\u62e9\u7684\u7ef4\u5ea6\u6620\u5c04\u5df2\u88ab\u5f15\u7528\uff0c\u5220\u9664\u540e\u62a5\u8868\u5c06\u4e0d\u518d\u8fdb\u884c\u6570\u636e\u6743\u9650\u63a7\u6743\uff0c\u786e\u5b9a\u5220\u9664\uff1f", (String)"RptDimMapEdit_3", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]);
                this.getView().showConfirm(tips, MessageBoxOptions.OKCancel, new ConfirmCallBackListener(CALLBACK_DEL_DIMENSION, (IFormPlugin)this));
            } else {
                this.getView().invokeOperation("delete");
            }
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        String callBackId = evt.getCallBackId();
        MessageBoxResult result = evt.getResult();
        if (result == MessageBoxResult.Yes && CALLBACK_DEL_DIMENSION.equals(callBackId)) {
            this.getView().invokeOperation("delete");
        }
    }

    static /* synthetic */ Map access$000(RptDimMapList x0) {
        return x0.publishPathMaps;
    }
}
