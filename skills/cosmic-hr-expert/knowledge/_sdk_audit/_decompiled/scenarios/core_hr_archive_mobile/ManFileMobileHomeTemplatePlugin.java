/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.ConfirmCallBackListener
 *  kd.bos.form.ConfirmTypes
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.MessageBoxOptions
 *  kd.bos.form.MessageBoxResult
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.embedform.BeforeShowEmbedFormEvent
 *  kd.bos.form.control.embedform.EmbedForm
 *  kd.bos.form.control.embedform.EmbedFormListener
 *  kd.bos.form.control.events.UploadListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.MessageBoxClosedEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.AbstractMobFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.common.util.HRMapUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hspm.business.util.CollectFileSupport
 *  kd.hr.hspm.business.util.FileViewUtil
 *  kd.hr.hspm.business.util.HspmQFilterUtil
 *  kd.hr.hspm.formplugin.web.reform.file.web.support.FileHomeSupport
 *  kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentDomainService
 *  kd.hrmp.hrpi.business.domain.assigment.service.IEmpPosOrgRelDomainService
 *  kd.sdk.hr.hpfs.common.constants.PosTypeConstants
 *  kd.sdk.hr.hspm.business.internal.approval.ApprovalBillSupport
 *  kd.sdk.hr.hspm.business.internal.approval.FileViewSupport
 *  kd.sdk.hr.hspm.business.internal.approval.ManFileFormMobSupport
 *  kd.sdk.hr.hspm.business.internal.file.HomeElyMobSupport
 *  kd.sdk.hr.hspm.business.internal.file.ermanfile.FileCommonSupport
 *  kd.sdk.hr.hspm.common.utils.PageCacheUtils
 */
package kd.hr.hspm.formplugin.web.reform.file.mobile;

import java.text.MessageFormat;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.MessageBoxResult;
import kd.bos.form.ShowType;
import kd.bos.form.control.Control;
import kd.bos.form.control.embedform.BeforeShowEmbedFormEvent;
import kd.bos.form.control.embedform.EmbedForm;
import kd.bos.form.control.embedform.EmbedFormListener;
import kd.bos.form.control.events.UploadListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractMobFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.common.util.HRMapUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hspm.business.util.CollectFileSupport;
import kd.hr.hspm.business.util.FileViewUtil;
import kd.hr.hspm.business.util.HspmQFilterUtil;
import kd.hr.hspm.formplugin.web.reform.file.web.support.FileHomeSupport;
import kd.hrmp.hrpi.business.domain.assigment.service.IAssignmentDomainService;
import kd.hrmp.hrpi.business.domain.assigment.service.IEmpPosOrgRelDomainService;
import kd.sdk.hr.hpfs.common.constants.PosTypeConstants;
import kd.sdk.hr.hspm.business.internal.approval.ApprovalBillSupport;
import kd.sdk.hr.hspm.business.internal.approval.FileViewSupport;
import kd.sdk.hr.hspm.business.internal.approval.ManFileFormMobSupport;
import kd.sdk.hr.hspm.business.internal.file.HomeElyMobSupport;
import kd.sdk.hr.hspm.business.internal.file.ermanfile.FileCommonSupport;
import kd.sdk.hr.hspm.common.utils.PageCacheUtils;

public final class ManFileMobileHomeTemplatePlugin
extends AbstractMobFormPlugin
implements UploadListener,
EmbedFormListener {
    private static final Log LOG = LogFactory.getLog(ManFileMobileHomeTemplatePlugin.class);
    private static final String OPERATION_KEY_SUBMITAUDIT = "submitaudit";
    private static final String HSPM_DEFAULT_EMPTYPAGEMOB = "hspm_defaultemptypagemob";

    public void preOpenForm(PreOpenFormEventArgs preOpenFormEventArgs) {
        super.preOpenForm(preOpenFormEventArgs);
        preOpenFormEventArgs.getFormShowParameter().setCustomParam("checkRightAppId", (Object)"hssc");
    }

    public void registerListener(EventObject eventObject) {
        this.addClickListeners(new String[]{"submit", "auditrecord", "changerecord", "close", "abandon", "collectmoreflex", "collectmorelabel", "collectmorevector"});
    }

    public void beforeBindData(EventObject eventObject) {
        super.beforeBindData(eventObject);
        Long paramAssignmentId = (Long)this.getView().getFormShowParameter().getCustomParam("assignment");
        Long paramEmployeeId = (Long)this.getView().getFormShowParameter().getCustomParam("employee");
        DynamicObject assignmentDy = null;
        assignmentDy = paramAssignmentId != null && paramAssignmentId != 0L ? IAssignmentDomainService.getInstance().queryOriginalOne("id,employee.id", new QFilter[]{new QFilter("id", "=", (Object)paramAssignmentId)}) : (paramEmployeeId != null && paramEmployeeId != 0L ? FileViewSupport.getPrimaryAssignment((long)paramEmployeeId) : FileViewUtil.getLoginUserAssignment());
        if (Objects.isNull(assignmentDy)) {
            LOG.error("ManFileMobileHomeTemplatePlugin#get assignmentDy null###{}", (Object)paramAssignmentId);
            return;
        }
        long assignmentId = assignmentDy.getLong("id");
        long employeeId = assignmentDy.getLong("employee.id");
        this.getModel().setValue("assignment", (Object)assignmentId);
        this.getModel().setValue("employee", (Object)employeeId);
        long fileViewId = FileViewUtil.getFileViewIdByAssignmentId((long)assignmentId, (long)assignmentDy.getLong("employee.id"), (String)"1");
        this.getView().getPageCache().put("personId", String.valueOf(employeeId));
        this.getView().getPageCache().put("assignment", String.valueOf(assignmentId));
        ManFileFormMobSupport.setTabPageVisibleByPermission((IFormView)this.getView());
        CollectFileSupport collectFileSupport = new CollectFileSupport(this.getView(), true);
        collectFileSupport.setCollectContentTips();
        if (fileViewId == 0L) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"toolbar"});
            return;
        }
        Map infoGroupConfig = FileViewUtil.getInfoGroupConfig((long)fileViewId, (String)"1");
        List mainEntryList = (List)infoGroupConfig.get("mainentry");
        LOG.info("fileViewId:{}, mainEntryList:{}", (Object)fileViewId, (Object)mainEntryList);
        if (mainEntryList != null && !mainEntryList.isEmpty()) {
            ManFileFormMobSupport.setTabPageVisibleByMulView((IFormView)this.getView(), (List)mainEntryList);
            this.setFileViewConfigValueAndCache(fileViewId, infoGroupConfig);
        }
        ApprovalBillSupport.handleSubmitButtonVisible((IFormView)this.getView());
    }

    private void setFileViewConfigValueAndCache(long fileViewId, Map<String, Object> infoGroupConfig) {
        this.getModel().setValue("fileview", (Object)fileViewId);
        this.getView().getFormShowParameter().setCustomParam("fileview", (Object)fileViewId);
        this.getPageCache().put("fileview", String.valueOf(fileViewId));
        PageCacheUtils.loadFileViewCache(infoGroupConfig, (IFormView)this.getView());
        this.getPageCache().put("cnfjson", SerializationUtils.toJsonString(infoGroupConfig));
    }

    public void beforeShowEmbedForm(BeforeShowEmbedFormEvent event) {
        EmbedForm embedForm = (EmbedForm)event.getSource();
        String refFormNumber = embedForm.getRefFormNumber();
        String refEntityNumber = embedForm.getRefEntityNumber();
        LOG.info("beforeShowEmbedForm->:{}", (Object)refFormNumber);
        if (event.getShowParameter() == null) {
            LOG.info("beforeShowEmbedForm-> event.getShowParameter() is empty");
            String tabPageKey = ManFileFormMobSupport.getTabPageKeyByBillFormId((IFormView)this.getView(), (String)refFormNumber);
            this.getView().setVisible(Boolean.FALSE, new String[]{tabPageKey});
            event.setCancel(true);
            return;
        }
        FileHomeSupport.addDetailFilterBeforeShowEmbed((BeforeShowEmbedFormEvent)event);
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        event.getShowParameter().setCustomParams(formShowParameter.getCustomParams());
        event.getShowParameter().setCustomParam("showType", (Object)embedForm.getShowType());
        if (formShowParameter instanceof BillShowParameter) {
            event.getShowParameter().setCustomParam("masterid", ((BillShowParameter)formShowParameter).getPkId());
        }
        DynamicObject employee = (DynamicObject)this.getModel().getValue("employee");
        event.getShowParameter().setCustomParam("employee", (Object)employee.getLong("id"));
        DynamicObject assignment = (DynamicObject)this.getModel().getValue("assignment");
        event.getShowParameter().setCustomParam("assignment", (Object)assignment.getLong("id"));
        event.getShowParameter().setCustomParam("entity", (Object)refEntityNumber);
        if ("form".equals(embedForm.getShowType()) && HSPM_DEFAULT_EMPTYPAGEMOB.equals(event.getShowParameter().getFormId())) {
            event.getShowParameter().setCustomParam(refEntityNumber.concat("nodata"), (Object)"1");
            event.getShowParameter().setFormId(refFormNumber);
            event.getShowParameter().setStatus(OperationStatus.VIEW);
        }
        boolean needCollect = new FileCommonSupport(this.getView(), null).signEntityNeedCollect(this.getView(), refEntityNumber, false);
        event.getShowParameter().setCustomParam("need_collect_entity_first", (Object)(needCollect ? "1" : "0"));
        if (refFormNumber.equals("hspm_dispatchinfo_emly")) {
            QFilter filter = new QFilter("assignment", "=", (Object)assignment.getLong("id"));
            QFilter posTypeFilter = new QFilter("postype.id", "=", (Object)PosTypeConstants.DISPATCH);
            boolean exists = IEmpPosOrgRelDomainService.getInstance().isExists(filter.and(posTypeFilter).and(HspmQFilterUtil.buildNotDeletedQFilter()));
            if (!exists) {
                String tabPageKey = ManFileFormMobSupport.getTabPageKeyByBillFormId((IFormView)this.getView(), (String)refFormNumber);
                this.getView().setVisible(Boolean.FALSE, new String[]{tabPageKey});
                event.setCancel(true);
            }
        }
    }

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
        this.getView().addClientCallBack("handleCardAddOp");
        this.getView().setVisible(Boolean.valueOf(true), new String[]{"toolbar"});
        ApprovalBillSupport.showConfirmWhenFirstOpen((boolean)false, (IFormView)this.getView());
        this.mobShowAndHiddenEmbedInfo(true);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate source = (FormOperate)args.getSource();
        OperateOption operateOption = OperateOption.create();
        source.setOption(operateOption);
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        OperationResult operationResult = afterDoOperationEventArgs.getOperationResult();
        if (operationResult == null || !operationResult.isSuccess()) {
            return;
        }
        if (OPERATION_KEY_SUBMITAUDIT.equals(afterDoOperationEventArgs.getOperateKey())) {
            ApprovalBillSupport.submitConfirm((IFormView)this.getView(), (IFormPlugin)this);
        }
    }

    public void click(EventObject evt) {
        Map flexMap;
        String key = ((Control)evt.getSource()).getKey();
        LOG.info(MessageFormat.format("click key:{0}", key));
        if (key.equals("auditrecord-lbl") || key.equals("auditrecord-vec") || key.equals("auditRecord")) {
            ApprovalBillSupport.viewAuditRecord((IFormView)this.getView());
            return;
        }
        if (key.equals("changerecord-lbl") || key.equals("changerecord-vec") || key.equals("changeRecord")) {
            ApprovalBillSupport.viewChangeRecord((IFormView)this.getView());
            return;
        }
        if (key.equals("close")) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"headtipspanel"});
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"notpassvector"});
            this.getView().getPageCache().put("closeHeadTipsPanel", "true");
        } else if (key.equals("abandon")) {
            String changeDesc = this.getModel().getChangeDesc();
            this.getView().showConfirm(ResManager.loadKDString((String)"\u64cd\u4f5c\u653e\u5f03\u540e\uff0c\u5c06\u6062\u590d\u539f\u59cb\u503c\uff0c\u662f\u5426\u7ee7\u7eed\uff1f", (String)"AbstractMobileFormDrawEdit_8", (String)"hr-hspm-formplugin", (Object[])new Object[0]), changeDesc, MessageBoxOptions.OKCancel, ConfirmTypes.Save, new ConfirmCallBackListener("abandon", (IFormPlugin)this));
        } else if (key.startsWith("collectmore")) {
            String collectTips = this.getPageCache().get("collect_tips");
            if (HRStringUtils.isNotEmpty((String)collectTips)) {
                HashMap<Integer, String> btnNameMaps = new HashMap<Integer, String>(16);
                btnNameMaps.put(MessageBoxResult.Cancel.getValue(), ResManager.loadKDString((String)"\u786e\u5b9a", (String)"ManFileMobileHomeTemplatePlugin_1", (String)"hr-hspm-formplugin", (Object[])new Object[0]));
                this.getView().showConfirm(null, collectTips, MessageBoxOptions.OK, ConfirmTypes.Default, null, btnNameMaps);
            }
            return;
        }
        if (key.startsWith("infogroupcard_")) {
            Object formId;
            String targetKey = key.substring(key.indexOf(95) + 1);
            String cnfStr = this.getView().getPageCache().get("cnfjson");
            Map infoGroupConfig = (Map)SerializationUtils.fromJsonString((String)cnfStr, Map.class);
            List mainentryList = (List)infoGroupConfig.get("mainentry");
            Optional<Map> configOp = mainentryList.stream().filter(val -> targetKey != null && targetKey.equalsIgnoreCase((String)val.get("targetkey"))).findFirst();
            if (configOp.isPresent() && ("hrpi_trialperiod".equals(formId = configOp.get().get("mappingFormid")) || "hrpi_perserlen".equals(formId))) {
                return;
            }
            HomeElyMobSupport.showFilePage((IFormView)this.getView(), (String)key.substring(key.indexOf(95) + 1).toUpperCase(), (ShowType)ShowType.Floating, (String)"");
            return;
        }
        if (key.contains("lbl") || key.contains("vec")) {
            HomeElyMobSupport.showFilePage((IFormView)this.getView(), (String)key.split("-")[0].toUpperCase(), (ShowType)ShowType.Floating, (String)"");
            return;
        }
        String flexMapStr = this.getPageCache().get("flexMap");
        if (HRStringUtils.isNotEmpty((String)flexMapStr) && !CollectionUtils.isEmpty((Map)(flexMap = (Map)SerializationUtils.fromJsonString((String)flexMapStr, Map.class))) && flexMap.containsKey(key)) {
            HomeElyMobSupport.showFilePage((IFormView)this.getView(), (String)key, (ShowType)ShowType.Floating, (String)"");
        }
    }

    public void confirmCallBack(MessageBoxClosedEvent messageBoxClosedEvent) {
        super.confirmCallBack(messageBoxClosedEvent);
        MessageBoxResult result = messageBoxClosedEvent.getResult();
        if (!MessageBoxResult.Yes.equals((Object)result) && !MessageBoxResult.OK.equals((Object)result)) {
            return;
        }
        String key = messageBoxClosedEvent.getCallBackId();
        if ("ApprovalSubmitCallBack".equals(key)) {
            ApprovalBillSupport.submit((IFormView)this.getView());
        } else if ("abandon".equals(key)) {
            IFormView employeeView;
            String employeeViewPageId;
            DynamicObject employeeDy = (DynamicObject)this.getView().getModel().getValue("employee");
            if (employeeDy != null) {
                long employeeId = employeeDy.getLong("id");
                new FileHomeSupport(this.getView(), 0L, employeeId, "2").headPicAbandonUpdate();
            }
            if (HRStringUtils.isNotEmpty((String)(employeeViewPageId = this.getPageCache().get("employeepageid"))) && (employeeView = this.getView().getView(employeeViewPageId)) != null) {
                employeeView.setVisible(Boolean.valueOf(false), new String[]{"notpasslabel"});
                this.getView().sendFormAction(employeeView);
            }
        }
    }

    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        IDataEntityProperty property = e.getProperty();
        String name = property.getName();
        ChangeData[] changeSet = e.getChangeSet();
        if ("showcollectbox".equals(name)) {
            Object newValue = changeSet[0].getNewValue();
            this.mobShowAndHiddenEmbedInfo((Boolean)newValue);
        }
    }

    private void mobShowAndHiddenEmbedInfo(boolean onlyShowCollect) {
        String collectStr = this.getPageCache().getBigObject("collect_entity_num");
        if (HRStringUtils.isEmpty((String)collectStr)) {
            return;
        }
        Map collectEntityMap = (Map)SerializationUtils.fromJsonString((String)collectStr, Map.class);
        if (HRMapUtils.isEmpty((Map)collectEntityMap)) {
            return;
        }
        String allEmbedStr = this.getPageCache().getBigObject("tab_embed_entity");
        Map embedEntityAndTabMap = new HashMap(48);
        if (HRStringUtils.isNotEmpty((String)allEmbedStr)) {
            embedEntityAndTabMap = (Map)SerializationUtils.fromJsonString((String)allEmbedStr, Map.class);
        }
        String allEmbedAndEntityStr = this.getPageCache().getBigObject("embed_and_entity");
        Map embedAndEntityMap = new HashMap(48);
        if (HRStringUtils.isNotEmpty((String)allEmbedAndEntityStr)) {
            embedAndEntityMap = (Map)SerializationUtils.fromJsonString((String)allEmbedAndEntityStr, Map.class);
        }
        Map cnfEmbedEntityAndTabMap = new HashMap(48);
        String cnfEmbedStr = this.getPageCache().getBigObject("tab_embed_entity_config");
        if (HRStringUtils.isNotEmpty((String)cnfEmbedStr)) {
            cnfEmbedEntityAndTabMap = (Map)SerializationUtils.fromJsonString((String)cnfEmbedStr, Map.class);
        }
        for (Map.Entry entries : embedEntityAndTabMap.entrySet()) {
            String embedEntityNum = (String)entries.getKey();
            String tabKey = (String)entries.getValue();
            if (onlyShowCollect) {
                if (collectEntityMap.containsKey(embedEntityNum)) continue;
                this.getView().setVisible(Boolean.valueOf(false), new String[]{tabKey});
                continue;
            }
            if (!HRMapUtils.isEmpty(cnfEmbedEntityAndTabMap) && !cnfEmbedEntityAndTabMap.containsKey(embedEntityNum)) continue;
            this.getView().setVisible(Boolean.valueOf(true), new String[]{tabKey});
            String embedKey = (String)embedAndEntityMap.get(embedEntityNum);
            EmbedForm control = (EmbedForm)this.getView().getControl(embedKey);
            if (control == null) continue;
            control.showForm();
        }
    }
}
