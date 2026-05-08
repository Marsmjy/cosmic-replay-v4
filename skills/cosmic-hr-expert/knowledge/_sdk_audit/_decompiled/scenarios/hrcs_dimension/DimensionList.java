/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hr.hrcs.formplugin.web.perm.dimension;

import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.plugin.IFormPlugin;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public final class DimensionList
extends HRDataBaseList {
    public void beforeItemClick(BeforeItemClickEvent evt) {
        String itemKey = evt.getItemKey();
        if (HRStringUtils.equals((String)"tbldisable", (String)itemKey)) {
            this.getView().showConfirm(ResManager.loadKDString((String)"\u7981\u7528\u7ef4\u5ea6\u540e\uff0c\u4e0d\u5141\u8bb8\u5728\u201c\u4e1a\u52a1\u5bf9\u8c61\u7ef4\u5ea6\u6620\u5c04\u201d\u4e2d\u4f7f\u7528\u3002\u5df2\u6709\u7684\u89d2\u8272\u7ef4\u5ea6\u7684\u6570\u636e\u6743\u9650\u4e0d\u53d7\u5f71\u54cd\u3002", (String)"DimensionList_06", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), MessageBoxOptions.OKCancel, new ConfirmCallBackListener("disable_conform", (IFormPlugin)this));
            evt.setCancel(true);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        super.confirmCallBack(messageBoxClosedEvent);
        String callBackId = messageBoxClosedEvent.getCallBackId();
        MessageBoxResult result = messageBoxClosedEvent.getResult();
        if (HRStringUtils.equals((String)callBackId, (String)"disable_conform") && result.equals((Object)MessageBoxResult.Yes)) {
            this.getView().invokeOperation("disable");
        }
    }
}
