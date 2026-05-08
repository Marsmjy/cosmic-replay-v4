/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.IBillView
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.result.OperateErrorInfo
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.validate.ValidateResult
 *  kd.bos.entity.validate.ValidateResultCollection
 *  kd.bos.ext.hr.ruleengine.controls.RuleCondition
 *  kd.bos.form.FieldTip
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.container.Tab
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ItemClickEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.fieldtip.DeleteRule
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.util.StringUtils
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonEdit
 */
package kd.hr.hrcs.formplugin.web.earlywarn.scene;

import com.google.common.collect.Lists;
import java.text.MessageFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.IBillView;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.result.OperateErrorInfo;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.validate.ValidateResult;
import kd.bos.entity.validate.ValidateResultCollection;
import kd.bos.ext.hr.ruleengine.controls.RuleCondition;
import kd.bos.form.FieldTip;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.container.Tab;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.fieldtip.DeleteRule;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.util.StringUtils;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonEdit;

public final class WarnSceneEdit
extends WarnSceneCommonEdit {
    private static final Log LOGGER = LogFactory.getLog(WarnSceneEdit.class);
    private static final String RULE_DATE_KEY = "ruledate";

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
        this.getRuleCondition().setContainTarget(Boolean.valueOf(false));
    }

    public void customEvent(CustomEventArgs args) {
        try {
            if (HRStringUtils.equals((String)args.getEventName(), (String)"dragEntity")) {
                this.entityProcessor.dragEntity(args);
            } else if (HRStringUtils.equals((String)args.getEventName(), (String)"clickRelevance")) {
                this.entityProcessor.clickRelevance(args);
            } else if (HRStringUtils.equals((String)args.getEventName(), (String)"delEntity")) {
                this.entityProcessor.delEntity(args);
            } else if (HRStringUtils.equals((String)args.getEventName(), (String)"modifyFieldName")) {
                this.fieldProcessor.openModifyFieldPage(args);
            } else if (HRStringUtils.equals((String)args.getEventName(), (String)"getAllData")) {
                this.formProcessor.resetFormStatusAfterDoOp();
                this.dataProcessor.getAllData(args);
            }
            this.formProcessor.resetFormStatusAfterDoOp();
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_customEvent_error:", (Throwable)exception);
        }
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addItemClickListeners(new String[]{"laststepbtn", "nextstepbtn"});
    }

    public void afterLoadData(EventObject e) {
        try {
            super.afterLoadData(e);
            this.initProcessor.initCacheData(null);
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_afterLoadData_error:", (Throwable)exception);
        }
    }

    public void afterCopyData(EventObject e) {
        try {
            super.afterCopyData(e);
            Long pkId = (Long)((BaseShowParameter)this.getView().getFormShowParameter()).getPkId();
            long newWarnSceneId = ORM.create().genLongId(this.getModel().getDataEntityType().getName());
            this.getModel().getDataEntity().set("id", (Object)pkId);
            this.initProcessor.initCacheData((Object)newWarnSceneId);
            this.getModel().getDataEntity().set("id", (Object)newWarnSceneId);
            this.getView().getPageCache().put("copyWarnSceneId", String.valueOf(newWarnSceneId));
            this.getModel().setValue("number", (Object)(this.getModel().getValue("number") + "_copy"));
            this.getModel().setValue("name", (Object)new LocaleString(MessageFormat.format(ResManager.loadKDString((String)"{0}_\u590d\u5236", (String)"WarnSchemeEditPlugin_3", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]), this.getModel().getValue("name"))));
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_afterCopyData_error:", (Throwable)exception);
        }
    }

    public void afterBindData(EventObject e) {
        try {
            String defaultAppId;
            boolean fromDatabase;
            super.afterBindData(e);
            this.getView().setVisible(Boolean.FALSE, new String[]{"laststepbtn", "previewbtn", "bar_save"});
            if (this.getView().getFormShowParameter().getStatus() == OperationStatus.EDIT && ((Boolean)this.getModel().getValue("issyspreset")).booleanValue()) {
                ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
            }
            if (!(fromDatabase = this.getModel().getDataEntity().getDataEntityState().getFromDatabase()) && (defaultAppId = (String)this.getView().getFormShowParameter().getCustomParam("defaultappid")) != null && this.getModel().getValue("bizapp") == null) {
                this.getModel().setValue("bizapp", (Object)defaultAppId);
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_afterBindData_error:", (Throwable)exception);
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        try {
            Tab tab = (Tab)this.getView().getControl("tabap");
            String nextTab = this.formProcessor.getNextTab(tab);
            if (HRStringUtils.equals((String)evt.getOperationKey(), (String)"nextstep")) {
                boolean numberValid = this.dataProcessor.validateNumber(nextTab);
                if (!numberValid) {
                    this.getView().showTipNotification(ResManager.loadKDString((String)"\u7f16\u7801\u4ec5\u652f\u6301\u82f1\u6587\u5927\u5c0f\u5199\u3001\u6570\u5b57\u4e0e\u4e0b\u5212\u7ebf\u3002", (String)"WarnObjectEdit_6", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                    evt.setCancel(true);
                    return;
                }
                String initCompleted = this.getPageCache().get("initCompleted");
                if (HRStringUtils.equals((String)nextTab, (String)"definefield") && !HRStringUtils.equals((String)initCompleted, (String)"1")) {
                    this.initProcessor.initCustomControl();
                } else if (nextTab.equals("definefield")) {
                    this.formProcessor.sendFlagForToStep2();
                }
            }
            if (HRStringUtils.equals((String)evt.getOperationKey(), (String)"nextstep") && HRStringUtils.equals((String)nextTab, (String)"filterdata") || HRStringUtils.equals((String)evt.getOperationKey(), (String)"previewdata") || HRStringUtils.equals((String)evt.getOperationKey(), (String)"save") && HRStringUtils.equals((String)nextTab, (String)"filterdata")) {
                this.getView().showLoading(new LocaleString(ResManager.loadKDString((String)"\u6b63\u5728\u52a0\u8f7d...", (String)"WarnObjectEdit_23", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0])));
                evt.setCancel(true);
                this.formProcessor.sendFlagForGetAllData(evt.getOperationKey(), nextTab);
                this.getPageCache().put("checkRule", "true");
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_beforeItemClick_error:", (Throwable)exception);
        }
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
        if (StringUtils.isNotEmpty((String)this.getPageCache().get("checkRule"))) {
            String conditionJson;
            String originRuleCondition = (String)this.getModel().getValue("datafilter");
            String string = conditionJson = this.getRuleCondition().getValue() == null ? "" : this.getRuleCondition().getValue();
            if (!HRStringUtils.equals((String)originRuleCondition, (String)conditionJson)) {
                this.getModel().setValue("datafilter", (Object)conditionJson);
            }
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        try {
            super.propertyChanged(args);
            String fieldKey = args.getProperty().getName();
            ChangeData changeData = args.getChangeSet()[0];
            if (HRStringUtils.equals((String)RULE_DATE_KEY, (String)fieldKey)) {
                Date newData = (Date)changeData.getNewValue();
                if (null == newData) {
                    return;
                }
                String dateFormat = this.getView().getPageCache().get("ruleDateFormat");
                String data = HRDateTimeUtils.format((Date)newData, (String)dateFormat);
                this.getRuleCondition().setDate(data);
                this.getModel().setValue(RULE_DATE_KEY, null);
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_propertyChanged_error:", (Throwable)exception);
        }
    }

    private RuleCondition getRuleCondition() {
        return (RuleCondition)this.getControl("hrfilterap");
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        super.closedCallBack(event);
        Object returnData = event.getReturnData();
        if (returnData == null) {
            return;
        }
        String actionId = event.getActionId();
        try {
            switch (actionId) {
                case "hbp_setentityrelation": {
                    this.entityProcessor.updateEntityRelation(returnData);
                    break;
                }
                case "hbp_modifyfieldname": {
                    this.fieldProcessor.setFieldNewName(returnData);
                    break;
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_propertyChanged_error:", (Throwable)exception);
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent event) {
        try {
            super.confirmCallBack(event);
            int result = event.getResult().getValue();
            if (HRStringUtils.equals((String)event.getCallBackId(), (String)"deleteMainEntity") || HRStringUtils.equals((String)event.getCallBackId(), (String)"deleteEntity")) {
                if (result == MessageBoxResult.Yes.getValue()) {
                    this.entityProcessor.deleteEntityCustomEvent();
                }
                this.getPageCache().remove("delRefCalculateFieldNums");
            } else if (HRStringUtils.equals((String)event.getCallBackId(), (String)"changeDataSourceConfirm")) {
                this.dataProcessor.confirmChangeDataSource(result);
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_confirmCallBack_error:", (Throwable)exception);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        try {
            super.beforeDoOperation(args);
            AbstractOperate op = (AbstractOperate)args.getSource();
            String operateKey = op.getOperateKey();
            Tab tab = (Tab)this.getView().getControl("tabap");
            switch (operateKey) {
                case "save": {
                    this.beforeSave(tab, args, op);
                    break;
                }
                case "nextstep": {
                    this.beforeNextStepOption(tab, args);
                    break;
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_beforeDoOperation_error:", (Throwable)exception);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        try {
            String operateKey;
            super.afterDoOperation(args);
            AbstractOperate op = (AbstractOperate)args.getSource();
            switch (operateKey = op.getOperateKey()) {
                case "save": {
                    this.getView().hideLoading();
                    break;
                }
                case "nextstep": {
                    this.afterNextStepOption(args);
                    break;
                }
                case "laststep": {
                    this.afterLastStepOption(args);
                    break;
                }
            }
            this.formProcessor.resetFormStatusAfterDoOp();
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_afterDoOperation_error:", (Throwable)exception);
        }
    }

    public void itemClick(ItemClickEvent evt) {
        try {
            super.itemClick(evt);
            this.formProcessor.resetFormStatusAfterDoOp();
        }
        catch (Exception exception) {
            LOGGER.error("WarnSceneEdit_itemClick_error:", (Throwable)exception);
        }
    }

    private void beforeSave(Tab tab, BeforeDoOperationEventArgs args, AbstractOperate op) {
        if (HRStringUtils.equals((String)tab.getCurrentTab(), (String)"definefield") && !this.dataProcessor.validateFieldDefineData()) {
            this.getView().hideLoading();
            args.setCancel(true);
            return;
        }
        if (HRStringUtils.equals((String)tab.getCurrentTab(), (String)"definefield")) {
            this.fieldProcessor.setBaseDataIdFields();
            this.fieldProcessor.setFieldControlType();
            this.fieldProcessor.setFieldComplexType();
        }
        String joinEntities = this.getPageCache().get("joinEntities");
        String entityRelations = this.getPageCache().get("entityRelations");
        String calFields = this.getPageCache().get("calculateFields");
        op.getOption().setVariableValue("joinEntities", joinEntities);
        op.getOption().setVariableValue("entityRelations", entityRelations);
        op.getOption().setVariableValue("calculateFields", calFields);
    }

    private void afterLastStepOption(AfterDoOperationEventArgs args) {
        if (args.getOperationResult().isSuccess()) {
            this.commonProcessor.setFieldLock();
            Tab tab = (Tab)this.getView().getControl("tabap");
            String lastTab = this.formProcessor.getLastTab(tab);
            if (lastTab != null) {
                tab.activeTab(lastTab);
            } else {
                lastTab = tab.getCurrentTab();
            }
            this.formProcessor.setVisibleForBtn(lastTab, this.getView());
        }
    }

    private void beforeNextStepOption(Tab tab, BeforeDoOperationEventArgs args) {
        String nextTab = this.formProcessor.getNextTab(tab);
        if (nextTab != null) {
            if (nextTab.equals("filterdata") && !this.dataProcessor.validateFieldDefineData()) {
                args.setCancel(true);
                this.getView().hideLoading();
                return;
            }
            if (nextTab.equals("filterdata")) {
                this.fieldProcessor.setFieldControlType();
                this.fieldProcessor.setFieldComplexType();
                this.fieldProcessor.setBaseDataIdFields();
            }
        }
    }

    private void afterNextStepOption(AfterDoOperationEventArgs args) {
        OperationResult operationResult = args.getOperationResult();
        if (operationResult != null && operationResult.isSuccess()) {
            Tab tab = (Tab)this.getView().getControl("tabap");
            String nextTab = this.formProcessor.getNextTab(tab);
            if (nextTab != null) {
                tab.activeTab(nextTab);
            } else {
                nextTab = tab.getCurrentTab();
            }
            this.formProcessor.setVisibleForBtn(nextTab, this.getView());
            if (nextTab.equals("filterdata")) {
                this.initProcessor.initDataFilter();
            }
            this.getView().hideLoading();
        } else {
            Tab tab = (Tab)this.getView().getControl("tabap");
            String nextTab = this.formProcessor.getNextTab(tab);
            if (HRStringUtils.equals((String)nextTab, (String)"definefield")) {
                this.getPageCache().remove("initCompleted");
                if (operationResult != null) {
                    ValidateResultCollection validateResult = operationResult.getValidateResult();
                    List validateErrors = validateResult.getValidateErrors();
                    for (ValidateResult validateError : validateErrors) {
                        List allErrorInfo = validateError.getAllErrorInfo();
                        for (OperateErrorInfo operateErrorInfo : allErrorInfo) {
                            String[] keys;
                            if (!"MUST_INPUT_ERROR".equals(operateErrorInfo.getErrorCode())) continue;
                            String fieldKeys = operateErrorInfo.getFieldKey();
                            for (String key : keys = fieldKeys.split(",")) {
                                FieldTip fieldTip = new FieldTip();
                                fieldTip.setSuccess(false);
                                fieldTip.setFieldKey(key);
                                fieldTip.setTip(ResManager.loadKDString((String)"\u503c\u4e0d\u80fd\u4e3a\u7a7a", (String)"WarningObjectWizardPlugin_0", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
                                DeleteRule deleteRule = new DeleteRule();
                                deleteRule.setAction("isChange");
                                deleteRule.setFields((List)Lists.newArrayList((Object[])new String[]{key}));
                                fieldTip.setDeleteRule(deleteRule);
                                this.getView().showFieldTip(fieldTip);
                            }
                        }
                    }
                }
            }
            this.getView().hideLoading();
        }
    }
}
