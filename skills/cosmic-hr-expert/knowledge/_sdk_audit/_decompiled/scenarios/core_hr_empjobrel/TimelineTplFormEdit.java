/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.datamodel.events.GetEntityTypeEventArgs
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.control.Control
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.log.utils.LogHandlerUtil
 *  kd.hr.hbp.business.service.timeline.dao.TimelineEntityConf
 *  kd.hr.hbp.business.service.timeline.validator.TimelineCommonValidator
 *  kd.hr.hbp.common.constants.timeline.TimelineConstants
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hr.hbp.formplugin.web.timeline.formprocessor.TimelineFormCommonProcessor
 *  kd.hr.hbp.formplugin.web.timeline.formprocessor.TimelineFormCopyProcessor
 *  kd.hr.hbp.formplugin.web.timeline.formprocessor.TimelineFormDefaultValProcessor
 *  kd.hr.hbp.formplugin.web.timeline.formprocessor.TimelineFormTipsProcessor
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.formplugin.web.timeline;

import java.util.EventObject;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.GetEntityTypeEventArgs;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.control.Control;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.log.utils.LogHandlerUtil;
import kd.hr.hbp.business.service.timeline.dao.TimelineEntityConf;
import kd.hr.hbp.business.service.timeline.validator.TimelineCommonValidator;
import kd.hr.hbp.common.constants.timeline.TimelineConstants;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hr.hbp.formplugin.web.timeline.formprocessor.TimelineFormCommonProcessor;
import kd.hr.hbp.formplugin.web.timeline.formprocessor.TimelineFormCopyProcessor;
import kd.hr.hbp.formplugin.web.timeline.formprocessor.TimelineFormDefaultValProcessor;
import kd.hr.hbp.formplugin.web.timeline.formprocessor.TimelineFormTipsProcessor;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public final class TimelineTplFormEdit
extends HRDataBaseEdit
implements TimelineConstants {
    private static final Log LOGGER = LogFactory.getLog(TimelineTplFormEdit.class);
    private final TimelineFormCommonProcessor commonProcessor = new TimelineFormCommonProcessor(this);
    private final TimelineFormCopyProcessor copyProcessor = new TimelineFormCopyProcessor(this);
    private final TimelineFormDefaultValProcessor defaultValProcessor = new TimelineFormDefaultValProcessor(this);
    private final TimelineFormTipsProcessor tipsProcessor = new TimelineFormTipsProcessor(this);

    public void getEntityType(GetEntityTypeEventArgs e) {
        super.getEntityType(e);
        MainEntityType originalEntityType = e.getOriginalEntityType();
        try {
            e.setNewEntityType((MainEntityType)originalEntityType.clone());
        }
        catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void afterCreateNewData(EventObject event) {
        super.afterCreateNewData(event);
        this.copyProcessor.setCopyData();
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        try {
            this.defaultValProcessor.setDefaultDateForNew();
            if (this.getModel().getDataEntity().getDataEntityState().getFromDatabase()) {
                List attachmentPanelLogInfo = LogHandlerUtil.getAttachmentLogInfo((Object)this.getModel().getValue("id"), (String)this.getView().getEntityId(), (DynamicObject)this.getModel().getDataEntity());
                this.getPageCache().put("attachmentPanelLogInfo", SerializationUtils.toJsonString((Object)attachmentPanelLogInfo));
            }
        }
        catch (Exception exception) {
            LOGGER.error("TimelineTplFormEdit.afterBindData: error", (Throwable)exception);
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        try {
            String skipSetDefaultParam = this.getPageCache().get("skipPropertyChangeSetDefault");
            if (!HRStringUtils.equals((String)skipSetDefaultParam, (String)"true")) {
                this.defaultValProcessor.setDefaultDateForPropChanged(args.getProperty().getName());
                this.defaultValProcessor.setEndDateDefaultVal(args.getProperty().getName());
            }
        }
        catch (Exception exception) {
            LOGGER.error("TimelineTplFormEdit.propertyChanged: error", (Throwable)exception);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if (HRStringUtils.equals((String)operateKey, (String)"save")) {
            TimelineEntityConf entityConfig = this.commonProcessor.getEntityConfig();
            TimelineCommonValidator validator = new TimelineCommonValidator((IDataEntityType)this.getModel().getDataEntityType());
            String errorMsg = validator.validateData(entityConfig, new DynamicObject[]{this.getModel().getDataEntity()});
            if (HRStringUtils.isNotEmpty((String)errorMsg)) {
                this.getView().showTipNotification(errorMsg);
                args.setCancel(true);
                return;
            }
            op.getOption().setVariableValue("attachmentPanelLogInfo", this.getPageCache().get("attachmentPanelLogInfo"));
        }
        String enableOpConfirm = this.getView().getPageCache().get("enableOpConfirm");
        if (this.commonProcessor.getEntityConfig().getEnableSaveConfirm().booleanValue() && !HRStringUtils.equals((String)enableOpConfirm, (String)"false") && (HRStringUtils.equals((String)operateKey, (String)"save") || HRStringUtils.equals((String)operateKey, (String)"delete"))) {
            this.tipsProcessor.showConfirmForOp(operateKey, args);
        }
        this.getView().getPageCache().remove("enableOpConfirm");
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if (HRStringUtils.equals((String)operateKey, (String)"save")) {
            Control endDate = this.getControl("enddate");
            if (endDate != null) {
                this.getView().updateView("enddate");
            }
            List attachmentPanelLogInfo = LogHandlerUtil.getAttachmentLogInfo((Object)this.getModel().getValue("id"), (String)this.getView().getEntityId(), (DynamicObject)this.getModel().getDataEntity());
            this.getPageCache().put("attachmentPanelLogInfo", SerializationUtils.toJsonString((Object)attachmentPanelLogInfo));
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent evt) {
        super.confirmCallBack(evt);
        if (!MessageBoxResult.Yes.equals((Object)evt.getResult())) {
            return;
        }
        String string = evt.getCallBackId();
        this.commonProcessor.getClass();
        if (HRStringUtils.equals((String)string, (String)"confirmCallBackSave")) {
            this.getView().getPageCache().put("enableOpConfirm", "false");
            this.getView().invokeOperation("save");
        }
    }
}
