/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.control.embedform.BeforeShowEmbedFormEvent
 *  kd.bos.form.control.embedform.EmbedFormListener
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hspm.business.util.FileViewUtil
 *  kd.hr.hspm.formplugin.web.reform.file.web.support.FileHomeSupport
 *  kd.hrmp.hrpi.business.application.service.IEmployeeApplicationService
 *  kd.sdk.hr.hspm.common.constants.HspmCommonConstants
 */
package kd.hr.hspm.formplugin.web.reform.file.web.manage;

import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.FormShowParameter;
import kd.bos.form.control.embedform.BeforeShowEmbedFormEvent;
import kd.bos.form.control.embedform.EmbedFormListener;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hspm.business.util.FileViewUtil;
import kd.hr.hspm.formplugin.web.reform.file.web.support.FileHomeSupport;
import kd.hrmp.hrpi.business.application.service.IEmployeeApplicationService;
import kd.sdk.hr.hspm.common.constants.HspmCommonConstants;

public final class FileManageReformHomePlugin
extends HRDataBaseEdit
implements EmbedFormListener,
HspmCommonConstants {
    private static final Log logger = LogFactory.getLog(FileManageReformHomePlugin.class);
    private static final String FIELD_QUIT = "quit";
    public static final String CHECK_RIGHT_APP_ID = "checkRightAppId";

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        formShowParameter.setCustomParam(CHECK_RIGHT_APP_ID, (Object)"hspm");
        Long assignment = null != formShowParameter.getCustomParam("assignment") ? Long.parseLong(formShowParameter.getCustomParam("assignment").toString()) : 0L;
        this.getModel().setValue("assignment", (Object)assignment);
        this.quitOrLoseEffectHide();
        long fileViewId = this.dealFileView();
        this.getModel().setValue("fileview", (Object)fileViewId);
        FileHomeSupport fileHomeSupport = new FileHomeSupport(this.getView(), new HashMap(32), new HashMap(32), fileViewId);
        fileHomeSupport.setSelf("0");
        fileHomeSupport.setEmbedFormCache();
        fileHomeSupport.dealFileViewDetail();
        fileHomeSupport.putHeadEmbedInCache();
    }

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
        FileHomeSupport fileHomeSupport = new FileHomeSupport(this.getView(), "0");
        fileHomeSupport.initAnchor();
    }

    private void quitOrLoseEffectHide() {
        Object assignment = this.getView().getModel().getValue("assignment");
        if (assignment == null) {
            return;
        }
        long employeeId = ((DynamicObject)assignment).getLong("employee.id");
        Map result = IEmployeeApplicationService.getInstance().checkEmployeeIsHired(Collections.singleton(employeeId));
        if (result == null) {
            return;
        }
        boolean isQuit = (Boolean)result.get(employeeId) == false;
        this.getView().getModel().setValue(FIELD_QUIT, (Object)isQuit);
    }

    private long dealFileView() {
        DynamicObject assignment = (DynamicObject)this.getModel().getValue("assignment");
        if (assignment == null) {
            return 0L;
        }
        long fileViewId = FileViewUtil.getFileViewIdByAssignmentId((long)assignment.getLong("id"), (long)assignment.getLong("employee.id"), (String)"0");
        if (0L == fileViewId) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u672a\u5339\u914d\u5230\u591a\u89c6\u56fe\u65b9\u6848\uff0c\u5df2\u4e3a\u60a8\u5c55\u793a\u9ed8\u8ba4\u7684\u4eba\u5458\u4fe1\u606f\u8be6\u60c5\u3002", (String)"FileManageReformHomePlugin_0", (String)"hr-hspm-formplugin", (Object[])new Object[0]));
        }
        return fileViewId;
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        FileHomeSupport fileHomeSupport = new FileHomeSupport(this.getView(), "0");
        fileHomeSupport.closeCall(closedCallBackEvent);
    }

    public void beforeShowEmbedForm(BeforeShowEmbedFormEvent event) {
        FileHomeSupport fileHomeSupport = new FileHomeSupport(this.getView(), "0");
        fileHomeSupport.dealBeforeShowEmbed(event);
    }
}
