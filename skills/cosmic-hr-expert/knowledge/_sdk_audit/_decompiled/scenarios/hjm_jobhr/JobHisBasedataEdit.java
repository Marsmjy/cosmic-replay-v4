/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.IBillView
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.entity.datamodel.events.ImportDataEventArgs
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.container.Container
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.list.IListView
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hrmp.hbjm.formplugin.web.basedata;

import java.util.Date;
import java.util.EventObject;
import java.util.Map;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.IBillView;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.entity.datamodel.events.ImportDataEventArgs;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.container.Container;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.list.IListView;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class JobHisBasedataEdit
extends HRDataBaseEdit {
    public void preOpenForm(PreOpenFormEventArgs e) {
        FormShowParameter formShowParameter = e.getFormShowParameter();
        Map params = formShowParameter.getCustomParams();
        if (!ObjectUtils.isEmpty((Object)params) && params.containsKey("boid") && params.containsKey("his_action")) {
            HRBaseServiceHelper helper;
            DynamicObject currentBo;
            Object boId = e.getFormShowParameter().getCustomParam("boid");
            Object his_action = e.getFormShowParameter().getCustomParam("his_action");
            if (HRStringUtils.isNotEmpty((String)String.valueOf(boId)) && HRStringUtils.equals((String)"open_insert_new_data_page", (String)String.valueOf(his_action)) && (currentBo = (helper = new HRBaseServiceHelper(e.getFormShowParameter().getFormId())).queryOne(boId)) != null && "0".equals(currentBo.getString("enable"))) {
                String errorMsg = ResManager.loadKDString((String)"\u6570\u636e\u5df2\u88ab\u7981\u7528\uff0c\u4e0d\u80fd\u65b0\u589e\u6570\u636e\u7248\u672c\u3002", (String)"JobHisBasedataEdit_1", (String)"hrmp-hbjm-formplugin", (Object[])new Object[0]);
                e.setCancelMessage(errorMsg);
                e.setCancel(true);
            }
        }
        super.preOpenForm(e);
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        Boolean isshowbsed = (Boolean)this.getView().getFormShowParameter().getCustomParam("isshowbsed");
        String option = (String)this.getView().getFormShowParameter().getCustomParam("option");
        if (!(HRStringUtils.equals((String)option, (String)"showhisversion") || isshowbsed != null && isshowbsed.booleanValue())) {
            this.getModel().beginInit();
            this.getModel().setValue("bsed", (Object)HRDateTimeUtils.truncateDate((Date)new Date()));
            this.getModel().endInit();
        }
        if (isshowbsed == null || !isshowbsed.booleanValue()) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"bsed"});
        } else {
            this.getView().setVisible(Boolean.TRUE, new String[]{"bsed"});
        }
    }

    public void afterBindData(EventObject e) {
        String status;
        DynamicObject basedataEntity = this.getView().getModel().getDataEntity();
        Boolean issyspreset = basedataEntity.getBoolean("issyspreset");
        String enable = basedataEntity.getString("enable");
        if (issyspreset.booleanValue() || HRStringUtils.equals((String)enable, (String)"0")) {
            ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
            this.getView().getFormShowParameter().setStatus(OperationStatus.VIEW);
            this.getView().setVisible(Boolean.FALSE, new String[]{"insertdatabtn"});
        }
        if ("A".equals(status = (String)this.getModel().getValue("status"))) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"bsed"});
        }
        Container toolBar = (Container)this.getView().getControl("tbmain");
        toolBar.deleteControls(new String[]{"bar_revise"});
        String formId = this.getView().getFormShowParameter().getFormId();
        if (!"hjm_jobhr".equals(formId)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"hisversionbtn"});
            toolBar.deleteControls(new String[]{"bar_hisinfo"});
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate operate = (AbstractOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        if (HRStringUtils.equals((String)"insertdata_his", (String)operateKey) || HRStringUtils.equals((String)"unsubmit", (String)operateKey)) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"bsed"});
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        IFormView parentView = this.getView().getParentView();
        AbstractOperate op = (AbstractOperate)afterDoOperationEventArgs.getSource();
        String operateKey = op.getOperateKey();
        if (HRStringUtils.equals((String)"save", (String)operateKey) || HRStringUtils.equals((String)"bar_confirmchange", (String)operateKey) || HRStringUtils.equals((String)"confirmchangenoaudit", (String)operateKey)) {
            if (parentView instanceof IListView) {
                parentView.invokeOperation("refresh");
                this.getView().sendFormAction(parentView);
            }
            if (afterDoOperationEventArgs.getOperationResult() != null && afterDoOperationEventArgs.getOperationResult().isSuccess()) {
                this.getView().setVisible(Boolean.FALSE, new String[]{"bsed"});
            }
        }
        Container toolBar = (Container)this.getView().getControl("tbmain");
        toolBar.deleteControls(new String[]{"bar_revise"});
        String formId = this.getView().getFormShowParameter().getFormId();
        if (!"hjm_jobhr".equals(formId)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"hisversionbtn"});
            toolBar.deleteControls(new String[]{"bar_hisinfo"});
        }
    }

    public void afterImportData(ImportDataEventArgs e) {
        Map sourceData = e.getSourceData();
        DynamicObject ob = this.getModel().getDataEntity();
        if (!(ob == null || sourceData != null && sourceData.containsKey("bsed"))) {
            ob.set("bsed", (Object)HRDateTimeUtils.truncateDate((Date)new Date()));
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
    }
}
