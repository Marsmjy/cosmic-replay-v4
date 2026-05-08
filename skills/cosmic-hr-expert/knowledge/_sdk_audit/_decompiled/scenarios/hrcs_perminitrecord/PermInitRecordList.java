/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.IFormView
 *  kd.bos.form.OpenStyle
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.mvc.SessionManager
 *  kd.bos.orm.query.QFilter
 *  kd.bos.schedule.api.TaskInfo
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hr.hrcs.common.util.AttachmentUtil
 *  kd.hr.hrcs.formplugin.web.perm.init.excel.DemoExcelWriter
 *  kd.hr.hrcs.formplugin.web.perm.init.excel.PermSheetHelper
 *  kd.hr.hrcs.formplugin.web.perm.init.excel.RecordExcelWriter
 *  kd.hr.hrcs.formplugin.web.perm.init.excel.RoleRecordExcelWriter
 *  kd.hr.hrcs.formplugin.web.perm.init.excel.RoleTemplateDemoExcelWriter
 */
package kd.hr.hrcs.formplugin.web.perm.init;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import java.util.Objects;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.IFormView;
import kd.bos.form.OpenStyle;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.SessionManager;
import kd.bos.orm.query.QFilter;
import kd.bos.schedule.api.TaskInfo;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hr.hrcs.common.util.AttachmentUtil;
import kd.hr.hrcs.formplugin.web.perm.init.excel.DemoExcelWriter;
import kd.hr.hrcs.formplugin.web.perm.init.excel.PermSheetHelper;
import kd.hr.hrcs.formplugin.web.perm.init.excel.RecordExcelWriter;
import kd.hr.hrcs.formplugin.web.perm.init.excel.RoleRecordExcelWriter;
import kd.hr.hrcs.formplugin.web.perm.init.excel.RoleTemplateDemoExcelWriter;

public final class PermInitRecordList
extends HRDataBaseList {
    private static final Log LOGGER = LogFactory.getLog(PermInitRecordList.class);
    private static final String ACTION_ID_EXPORT = "exportUrl";

    public void setFilter(SetFilterEvent evt) {
        evt.getQFilters().add(new QFilter("dealstatus", "=", (Object)"1"));
    }

    public void beforeShowBill(BeforeShowBillFormEvent evt) {
        Object recordId = evt.getParameter().getPkId();
        evt.getParameter().setCustomParam("recordId", (Object)String.valueOf(recordId));
        HRBaseServiceHelper permInitRecordHelper = new HRBaseServiceHelper("hrcs_perminitrecord");
        DynamicObject permInitRecord = permInitRecordHelper.queryOne(recordId);
        String initType = permInitRecord.getString("inittype");
        if (HRStringUtils.equals((String)initType, (String)"role")) {
            evt.getParameter().setCustomParam("inittype", (Object)"role");
        } else {
            evt.getParameter().setCustomParam("inittype", (Object)"userrole");
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        String key = evt.getItemKey();
        if ("initrole".equals(key)) {
            String policyIdStr = this.getPageCache().get("labelpolicy");
            String pageId = SessionManager.getCurrent().get(this.getView().getPageId() + "showForm" + policyIdStr);
            IFormView recordView = this.getView().getView(pageId);
            OperationStatus status = recordView.getFormShowParameter().getStatus();
            if (!HRStringUtils.isEmpty((String)pageId) && recordView != null) {
                IClientViewProxy service = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
                service.addAction("activate", (Object)pageId);
            } else {
                ListShowParameter showParameter = new ListShowParameter();
                showParameter.setFormId("bos_list");
                showParameter.setBillFormId("hrcs_labelpolicytask");
                showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                showParameter.setCustomParam("label", (Object)Long.valueOf(policyIdStr));
                SessionManager.getCurrent().put(this.getView().getPageId() + "showForm" + policyIdStr, showParameter.getPageId());
                this.getView().showForm((FormShowParameter)showParameter);
            }
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate source = (FormOperate)args.getSource();
        String operateKey = source.getOperateKey();
        if (HRStringUtils.equals((String)"inituserrole", (String)operateKey)) {
            String pageId = SessionManager.getCurrent().get(this.getView().getPageId() + "showFormuserrole");
            IFormView recordView = this.getView().getView(pageId);
            if (!HRStringUtils.isEmpty((String)pageId) && recordView != null) {
                IClientViewProxy service = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
                OperationStatus status = recordView.getFormShowParameter().getStatus();
                if (OperationStatus.ADDNEW.equals((Object)status)) {
                    service.addAction("activate", (Object)pageId);
                } else {
                    this.showUserInitDetail();
                }
            } else {
                this.showUserInitDetail();
            }
        } else if (HRStringUtils.equals((String)"download", (String)operateKey)) {
            Long recordId = (Long)args.getListSelectedData().get(0).getPrimaryKeyValue();
            String recordNumber = args.getListSelectedData().get(0).getNumber();
            HRBaseServiceHelper permInitRecordHelper = new HRBaseServiceHelper(source.getEntityId());
            DynamicObject permInitRecord = permInitRecordHelper.queryOne(source.getListFocusRow().getPrimaryKeyValue());
            if (HRStringUtils.equals((String)permInitRecord.getString("inittype"), (String)"role")) {
                String type = ResManager.loadKDString((String)"\u89d2\u8272\u521d\u59cb\u5316\u8bb0\u5f55", (String)"PermInitRecordList_4", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]);
                RoleRecordExcelWriter.downloadRecordExcel((Long)recordId, (String)recordNumber, (boolean)false, (String)type, (IFormView)this.getView());
            } else {
                RecordExcelWriter.downloadRecordExcel((Long)recordId, (String)recordNumber, (boolean)false, (boolean)true, (IFormView)this.getView());
            }
        } else if (HRStringUtils.equals((String)"initrole", (String)operateKey)) {
            String pageId = SessionManager.getCurrent().get(this.getView().getPageId() + "showFormrole");
            IFormView recordView = this.getView().getView(pageId);
            if (!HRStringUtils.isEmpty((String)pageId) && recordView != null) {
                OperationStatus status = recordView.getFormShowParameter().getStatus();
                IClientViewProxy service = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
                if (OperationStatus.ADDNEW.equals((Object)status)) {
                    service.addAction("activate", (Object)pageId);
                } else {
                    this.showRoleInitDetail();
                }
            } else {
                this.showRoleInitDetail();
            }
        } else {
            this.downloadTemp(args);
        }
    }

    private void downloadTemp(BeforeDoOperationEventArgs args) {
        FormOperate source = (FormOperate)args.getSource();
        String operateKey = source.getOperateKey();
        if (HRStringUtils.equals((String)"dlusertemp", (String)operateKey)) {
            FormShowParameter showParameter = new FormShowParameter();
            showParameter.setFormId("hrcs_exportperm");
            showParameter.getOpenStyle().setShowType(ShowType.Modal);
            showParameter.getCustomParams().put("entityname", ResManager.loadKDString((String)"\u7528\u6237\u6743\u9650\u521d\u59cb\u5316\u6a21\u677f", (String)"PermInitTemplatePlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            showParameter.getCustomParams().put("taskClassName", "kd.hr.hrcs.formplugin.web.perm.init.task.PermTemplateExportTask");
            showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, ACTION_ID_EXPORT));
            this.getView().showForm(showParameter);
        } else if (HRStringUtils.equals((String)"dlusertip", (String)operateKey)) {
            DemoExcelWriter writer = PermSheetHelper.getDemoExcelWriter();
            String excelUrl = writer.flush(ResManager.loadKDString((String)"\u7528\u6237\u6743\u9650\u521d\u59cb\u5316\u793a\u4f8b\u6570\u636e", (String)"PermInitTemplatePlugin_2", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), 7200);
            String fullUrl = AttachmentUtil.genFullUrl((String)excelUrl);
            String disposableUrl = AttachmentUtil.genDisposableUrl((String)fullUrl, (String)"hrcs_perminitrecord", (String)"3QKWBN8+1QVN");
            this.getView().download(disposableUrl);
        } else if (HRStringUtils.equals((String)"dlroletemp", (String)operateKey)) {
            FormShowParameter showParameter = new FormShowParameter();
            showParameter.setFormId("hrcs_exportperm");
            showParameter.getOpenStyle().setShowType(ShowType.Modal);
            showParameter.getCustomParams().put("entityname", ResManager.loadKDString((String)"\u89d2\u8272\u6743\u9650\u521d\u59cb\u5316\u6a21\u677f", (String)"PermRoleInitTemplatePlugin_0", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
            showParameter.getCustomParams().put("taskClassName", "kd.hr.hrcs.formplugin.web.perm.init.task.PermRoleTemplateExportTask");
            showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, ACTION_ID_EXPORT));
            this.getView().showForm(showParameter);
        } else if (HRStringUtils.equals((String)"dlroletip", (String)operateKey)) {
            RoleTemplateDemoExcelWriter writer = PermSheetHelper.getRoleDemoExcelWriter();
            String excelUrl = writer.flush(ResManager.loadKDString((String)"\u89d2\u8272\u6743\u9650\u521d\u59cb\u5316\u793a\u4f8b\u6570\u636e", (String)"PermRoleInitTemplatePlugin_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]), 7200);
            String fullUrl = AttachmentUtil.genFullUrl((String)excelUrl);
            String disposableUrl = AttachmentUtil.genDisposableUrl((String)fullUrl, (String)"hrcs_perminitrecord", (String)"3QKWEOD/DT/V");
            this.getView().download(disposableUrl);
        }
    }

    private void showUserInitDetail() {
        BillShowParameter fsp = new BillShowParameter();
        fsp.setFormId("hrcs_perminitrecord");
        OpenStyle style = new OpenStyle();
        style.setShowType(ShowType.MainNewTabPage);
        fsp.setOpenStyle(style);
        fsp.setStatus(OperationStatus.ADDNEW);
        fsp.setCustomParam("inittype", (Object)"userrole");
        fsp.setCaption(ResManager.loadKDString((String)"\u521d\u59cb\u5316\u7528\u6237\u6743\u9650", (String)"PermInitRecordList_1", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        SessionManager.getCurrent().put(this.getView().getPageId() + "showFormuserrole", fsp.getPageId());
        this.getView().showForm((FormShowParameter)fsp);
    }

    private void showRoleInitDetail() {
        BillShowParameter fsp = new BillShowParameter();
        fsp.setFormId("hrcs_perminitrecord");
        OpenStyle style = new OpenStyle();
        style.setShowType(ShowType.MainNewTabPage);
        fsp.setOpenStyle(style);
        fsp.setStatus(OperationStatus.ADDNEW);
        fsp.setCustomParam("inittype", (Object)"role");
        fsp.setCaption(ResManager.loadKDString((String)"\u521d\u59cb\u5316\u89d2\u8272\u6743\u9650", (String)"PermInitRecordList_3", (String)"hrmp-hrcs-formplugin", (Object[])new Object[0]));
        SessionManager.getCurrent().put(this.getView().getPageId() + "showFormrole", fsp.getPageId());
        this.getView().showForm((FormShowParameter)fsp);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        Map result;
        String actionId = closedCallBackEvent.getActionId();
        Object returnData = closedCallBackEvent.getReturnData();
        if (HRStringUtils.equals((String)ACTION_ID_EXPORT, (String)actionId) && returnData instanceof Map && (result = (Map)returnData).containsKey("taskinfo")) {
            String taskInfoStr = (String)result.get("taskinfo");
            TaskInfo taskInfo = (TaskInfo)SerializationUtils.fromJsonString((String)taskInfoStr, TaskInfo.class);
            this.handleTaskInfo(taskInfo);
        }
    }

    private void handleTaskInfo(TaskInfo taskInfo) {
        if (taskInfo.isTaskEnd()) {
            Map taskInfoMap = (Map)JSONObject.parseObject((String)taskInfo.getData(), Map.class);
            if (Objects.isNull(taskInfoMap)) {
                return;
            }
            String exportUrl = (String)taskInfoMap.get(ACTION_ID_EXPORT);
            if (HRStringUtils.isNotEmpty((String)exportUrl)) {
                IClientViewProxy clientViewProxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
                clientViewProxy.addAction("download", (Object)exportUrl);
            }
        } else {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u6784\u5efa\u5bfc\u51faURL\u5931\u8d25\uff01", (String)"HRMultiEntityExportPlugin_1", (String)"hrmp-hbp-formplugin", (Object[])new Object[0]));
        }
    }
}
