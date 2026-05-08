/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.AbstractBasePlugIn
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.IClientViewProxy
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.log.utils.LogHandlerUtil
 *  kd.hr.hbp.business.service.history.HisModelCommonService
 *  kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.constants.history.HisModelConstants
 *  kd.hr.hbp.common.constants.history.HisModelTypeEnum
 *  kd.hr.hbp.common.constants.history.HisPageEnum
 *  kd.hr.hbp.common.model.history.HisModelEntityConfig
 *  kd.hr.hbp.common.model.history.param.HisModelOPParam
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hbp.formplugin.web.history.form.processor.HisModelCodeRuleProcessor
 *  kd.hr.hbp.formplugin.web.history.form.processor.HisModelCommonProcessor
 *  kd.hr.hbp.formplugin.web.history.form.processor.HisModelCopyProcessor
 *  kd.hr.hbp.formplugin.web.history.form.processor.HisModelDisableProcessor
 *  kd.hr.hbp.formplugin.web.history.form.processor.HisModelFormProcessor
 *  kd.hr.hbp.formplugin.web.history.form.processor.HisModelTipsProcessor
 *  kd.hr.hbp.formplugin.web.history.form.processor.HisModelValidateProcessor
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.formplugin.web.history.form;

import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kd.bos.base.AbstractBasePlugIn;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.IClientViewProxy;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.log.utils.LogHandlerUtil;
import kd.hr.hbp.business.service.history.HisModelCommonService;
import kd.hr.hbp.business.service.timeline.util.TimeLineServiceUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.common.constants.history.HisModelTypeEnum;
import kd.hr.hbp.common.constants.history.HisPageEnum;
import kd.hr.hbp.common.model.history.HisModelEntityConfig;
import kd.hr.hbp.common.model.history.param.HisModelOPParam;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hbp.formplugin.web.history.form.processor.HisModelCodeRuleProcessor;
import kd.hr.hbp.formplugin.web.history.form.processor.HisModelCommonProcessor;
import kd.hr.hbp.formplugin.web.history.form.processor.HisModelCopyProcessor;
import kd.hr.hbp.formplugin.web.history.form.processor.HisModelDisableProcessor;
import kd.hr.hbp.formplugin.web.history.form.processor.HisModelFormProcessor;
import kd.hr.hbp.formplugin.web.history.form.processor.HisModelTipsProcessor;
import kd.hr.hbp.formplugin.web.history.form.processor.HisModelValidateProcessor;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public class HisModelFormCommonPlugin
extends HRDataBaseEdit
implements HisModelConstants {
    private static final Log LOGGER = LogFactory.getLog(HisModelFormCommonPlugin.class);
    private final HisModelCommonProcessor commonProcessor = new HisModelCommonProcessor((AbstractFormPlugin)this);
    private final HisModelCopyProcessor copyProcessor = new HisModelCopyProcessor((AbstractBasePlugIn)this);
    private final HisModelFormProcessor formProcessor = new HisModelFormProcessor((AbstractBasePlugIn)this);
    private final HisModelValidateProcessor validateProcessor = new HisModelValidateProcessor((AbstractFormPlugin)this);
    private final HisModelTipsProcessor tipsProcessor = new HisModelTipsProcessor((AbstractFormPlugin)this);
    private final HisModelDisableProcessor disableProcessor = new HisModelDisableProcessor((AbstractFormPlugin)this);
    private final HisModelCodeRuleProcessor codeRuleProcessor = new HisModelCodeRuleProcessor((AbstractFormPlugin)this);

    public void initialize() {
        super.initialize();
        this.commonProcessor.init();
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        LOGGER.info("HisModelFormCommonPlugin beforeBindData start. entityNumber: {}, hisPage: {},", (Object)this.commonProcessor.getEntityNumber(), (Object)this.commonProcessor.getHisPage());
        this.copyProcessor.setBoIdForNewHisVersionPage();
    }

    public void afterCreateNewData(EventObject event) {
        super.afterCreateNewData(event);
        LOGGER.info("HisModelFormCommonPlugin afterCreateNewData start. entityNumber: {}, hisPage: {},", (Object)this.commonProcessor.getEntityNumber(), (Object)this.commonProcessor.getHisPage());
        this.copyProcessor.copyData();
    }

    public void afterLoadData(EventObject e) {
        super.afterLoadData(e);
        LOGGER.info("HisModelFormCommonPlugin afterLoadData start. entityNumber: {}, hisPage: {},", (Object)this.commonProcessor.getEntityNumber(), (Object)this.commonProcessor.getHisPage());
        this.formProcessor.handlePageStatusForEdit();
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        LOGGER.info("HisModelFormCommonPlugin afterBindData start. entityNumber: {}, hisPage: {},", (Object)this.commonProcessor.getEntityNumber(), (Object)this.commonProcessor.getHisPage());
        this.formProcessor.handleBtnOrFieldVisible();
        this.formProcessor.handlePageStatusForEdit();
        this.formProcessor.handlePageStatusForNew();
        this.codeRuleProcessor.setNumberField();
        this.commonProcessor.setEnable();
        if (this.getModel().getDataEntity().getDataEntityState().getFromDatabase()) {
            List attachmentPanelLogInfo = LogHandlerUtil.getAttachmentLogInfo((Object)this.getModel().getValue("id"), (String)this.getView().getEntityId(), (DynamicObject)this.getModel().getDataEntity());
            this.getPageCache().put("attachmentPanelLogInfo", SerializationUtils.toJsonString((Object)attachmentPanelLogInfo));
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "save": {
                op.getOption().setVariableValue("attachmentPanelLogInfo", this.getPageCache().get("attachmentPanelLogInfo"));
                if (HisPageEnum.REVISE_VERSION_PAGE.getPage().equals(this.getView().getFormShowParameter().getCustomParam("hisPage"))) {
                    Date maxEffEndDate;
                    HRBaseServiceHelper helper;
                    int count;
                    String entityId = this.getView().getEntityId();
                    HisModelEntityConfig hisModelEntityConfig = HisModelCommonService.getInstance().getHisModelEntityConfig(entityId);
                    if (hisModelEntityConfig.getModelType() == HisModelTypeEnum.NO_INTERRUPTION_NO_OVERLAP && (count = (helper = new HRBaseServiceHelper(entityId)).count(entityId, new QFilter[]{new QFilter("boid", "=", (Object)((Long)this.getModel().getValue("boid")))})) == 2 && (maxEffEndDate = TimeLineServiceUtil.getMaxEffEndDate()).compareTo((Date)this.getModel().getValue("bsled")) != 0) {
                        args.setCancel(true);
                        this.getView().showErrorNotification(String.format(ResManager.loadKDString((String)"\u5931\u6548\u65e5\u671f\u4e0d\u80fd\u65e9\u4e8e%s", (String)"HisModelFormCommonPlugin_0", (String)"hrmp-hbp-formplugin", (Object[])new Object[0]), HRDateTimeUtils.format((Date)maxEffEndDate, (String)"yyyy-MM-dd")));
                        break;
                    }
                    HisModelOPParam hisModelOPParam = new HisModelOPParam();
                    hisModelOPParam.setReviseSave(true);
                    op.getOption().setVariableValue("hisOpParam", SerializationUtils.toJsonString((Object)hisModelOPParam));
                    break;
                }
                if (!this.validateProcessor.validateForSave()) {
                    args.setCancel(true);
                    break;
                }
                OperateOption option = op.getOption();
                this.codeRuleProcessor.setNumberToOpParam(option);
                HisModelOPParam hisModelOPParam = new HisModelOPParam();
                hisModelOPParam.setBoChange(this.commonProcessor.getHisPage() == HisPageEnum.CHANGE_PAGE);
                option.setVariableValue("hisOpParam", SerializationUtils.toJsonString((Object)hisModelOPParam));
                break;
            }
            case "confirmchange": {
                if (!this.validateProcessor.validateForChange() || this.tipsProcessor.beforeChangeTip(operateKey, op.getOption())) {
                    args.setCancel(true);
                    break;
                }
                HisModelOPParam hisModelOPParam = new HisModelOPParam();
                hisModelOPParam.setBoChange(this.commonProcessor.getHisPage() == HisPageEnum.CHANGE_PAGE);
                op.getOption().setVariableValue("hisOpParam", SerializationUtils.toJsonString((Object)hisModelOPParam));
                break;
            }
            case "disable": {
                this.disableProcessor.showSetDisableDatePage(args);
                break;
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        OperationResult operationResult = args.getOperationResult();
        switch (operateKey) {
            case "change": {
                this.formProcessor.doChange();
                break;
            }
            case "revise": {
                if (operationResult.isSuccess()) {
                    if (this.getModelVal("id").equals(this.getModelVal("boid"))) {
                        this.formProcessor.showHisVersionEditPage(this.getModel().getValue("sourcevid"), true);
                        break;
                    }
                    this.formProcessor.showHisVersionEditPage(this.getModel().getValue("id"), false);
                    break;
                }
                this.formProcessor.handleBtnOrFieldVisible();
                break;
            }
            case "reviserecord": {
                if (operationResult == null || !operationResult.isSuccess()) break;
                this.formProcessor.handleBtnOrFieldVisible();
                HashMap<String, Object> action = new HashMap<String, Object>(8);
                action.put("formId", "hbp_reviselogpage");
                String pkId = (Boolean)this.getModel().getValue("iscurrentversion") != false ? String.valueOf(this.getModel().getValue("sourcevid")) : String.valueOf(this.getModel().getValue("id"));
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("entity", this.getView().getModel().getDataEntityType().getName());
                params.put("boid", String.valueOf(this.getModel().getValue("boid")));
                params.put("pkId", pkId);
                action.put("params", params);
                IClientViewProxy proxy = (IClientViewProxy)this.getView().getService(IClientViewProxy.class);
                proxy.addAction("showSlideBill", action);
                break;
            }
            case "hisversioninfo": {
                this.formProcessor.showHisVersionListPage();
                break;
            }
            case "confirmchange": {
                if (operationResult == null || !operationResult.isSuccess()) break;
                if (this.commonProcessor.getHisPage() == HisPageEnum.CHANGE_PAGE) {
                    this.getView().invokeOperation("refresh");
                }
                this.commonProcessor.resetHisPageForHisOp();
                this.formProcessor.handleBtnOrFieldVisible();
                this.formProcessor.handlePageStatusForEdit();
                this.formProcessor.updateHisFieldVal();
                break;
            }
            case "save": {
                if (operationResult == null || !operationResult.isSuccess()) break;
                if (this.commonProcessor.getHisPage() == HisPageEnum.CHANGE_PAGE) {
                    this.getView().invokeOperation("refresh");
                }
                this.commonProcessor.resetHisPageForHisOp();
                this.formProcessor.handleBtnOrFieldVisible();
                this.formProcessor.handlePageStatusForEdit();
                this.formProcessor.updateHisFieldVal();
                break;
            }
            case "enable": 
            case "disable": {
                if (operationResult == null || !operationResult.isSuccess()) break;
                this.formProcessor.handleBtnOrFieldVisible();
                if (this.commonProcessor.getHisModelEntityConfig().getModelType() != HisModelTypeEnum.NO_INTERRUPTION_NO_OVERLAP) break;
                this.formProcessor.getView().invokeOperation("refresh");
                break;
            }
            case "audit": {
                if (operationResult == null || !operationResult.isSuccess()) break;
                this.formProcessor.handleBtnOrFieldVisible();
                this.formProcessor.updateHisFieldVal();
                break;
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        String actionId = evt.getActionId();
        Object returnDate = evt.getReturnData();
        this.disableProcessor.closeCallBack(actionId, returnDate);
        if ("hbp_hischangestyle".equals(actionId)) {
            if (returnDate == null) {
                return;
            }
            if (returnDate instanceof Map) {
                Object tempVersionId = ((Map)returnDate).get("tempVersionId");
                this.formProcessor.showTempVersionEditForm((Long)tempVersionId);
            } else {
                this.formProcessor.setCurrentPageToChangePage();
            }
        } else if ("hisversioninfo".equals(actionId)) {
            this.getPageCache().remove("showHisVersionListPageId");
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        String callBackId;
        super.confirmCallBack(evt);
        switch (callBackId = evt.getCallBackId()) {
            case "save": 
            case "confirmchange": {
                MessageBoxResult result = evt.getResult();
                if (result != MessageBoxResult.Yes) break;
                this.getPageCache().put("skipChangeTips", "true");
                this.getView().invokeOperation(callBackId);
            }
        }
    }
}
