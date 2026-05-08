/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.AnchorItems
 *  kd.bos.entity.Tips
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.TableValueSetter
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.exception.ErrorCode
 *  kd.bos.exception.KDException
 *  kd.bos.ext.form.control.AnchorControl
 *  kd.bos.form.FormMetadataCache
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.TipsSupport
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.UploadListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.DateEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.permission.api.HasPermOrgResultImpl
 *  kd.bos.servicehelper.coderule.CodeRuleServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.common.constants.history.HisPageEnum
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hrmp.hbpm.business.domain.position.service.impl.PositionCodeRuleHelper
 *  kd.hrmp.hbpm.business.domain.position.service.impl.PositionTplApplicationScopeServiceImpl
 *  kd.hrmp.hbpm.business.domain.position.service.impl.PositionTplValidateServiceImpl
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionBillQueryRepository
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplFieldRepository
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplRepository
 *  kd.hrmp.hbpm.business.domain.position.service.repository.ReportingrelationQueryRepository
 *  kd.hrmp.hbpm.business.infrastructure.client.perm.PermHelper
 *  kd.hrmp.hbpm.business.utils.AttachmentUtils
 *  kd.hrmp.hbpm.business.utils.JobLevelGradeRangeUtil
 *  kd.hrmp.hbpm.business.utils.PositionHisQueryPageUtil
 *  kd.hrmp.hbpm.business.utils.PositionUtils
 *  kd.hrmp.hbpm.business.utils.SystemParamHelper
 */
package kd.hrmp.hbpm.formplugin.web.position;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.AnchorItems;
import kd.bos.entity.Tips;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.TableValueSetter;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDException;
import kd.bos.ext.form.control.AnchorControl;
import kd.bos.form.FormMetadataCache;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.TipsSupport;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.UploadListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.DateEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.permission.api.HasPermOrgResultImpl;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.common.constants.history.HisPageEnum;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hrmp.hbpm.business.domain.position.service.impl.PositionCodeRuleHelper;
import kd.hrmp.hbpm.business.domain.position.service.impl.PositionTplApplicationScopeServiceImpl;
import kd.hrmp.hbpm.business.domain.position.service.impl.PositionTplValidateServiceImpl;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionBillQueryRepository;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplFieldRepository;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplRepository;
import kd.hrmp.hbpm.business.domain.position.service.repository.ReportingrelationQueryRepository;
import kd.hrmp.hbpm.business.infrastructure.client.perm.PermHelper;
import kd.hrmp.hbpm.business.utils.AttachmentUtils;
import kd.hrmp.hbpm.business.utils.JobLevelGradeRangeUtil;
import kd.hrmp.hbpm.business.utils.PositionHisQueryPageUtil;
import kd.hrmp.hbpm.business.utils.PositionUtils;
import kd.hrmp.hbpm.business.utils.SystemParamHelper;

public class PositionEdit
extends HRDataBaseEdit
implements UploadListener,
BeforeF7SelectListener {
    private static final Log logger = LogFactory.getLog(PositionEdit.class);
    public static final String ORG_PERM_CACHE_KEY = "org_perm_result";
    private static final String NUMBERRULE_EXTFIELD = "numberrule_extfield";
    private static final String CHANGEINFO_FLEXPANELAP = "changeinfoflexpanelap";
    private static final String RIGHTRELATIONFLEXAP = "flexpanelrightcontainer";
    private static final String anchor_controlap = "anchorcontrolap";
    private static final String BAR_PRINT = "bar_print";
    private static final String CACHE_KEY_RECYCLEMAP = "recycleMap";
    private static final List<String> POSITIONTPL_FILED_KEYS = Arrays.asList("job", "jobscm", "lowjoblevel", "highjoblevel", "lowjobgrade", "highjobgrade", "jobgradescm", "joblevelscm");
    private static final List<String> EXINCLUDE_POSITIONTPL_FILED_KEYS = Arrays.asList("joblevelrange", "jobgraderange");
    private HasPermOrgResult hasPermOrgResult;

    public void preOpenForm(PreOpenFormEventArgs args) {
        FormShowParameter showParameter = (FormShowParameter)args.getSource();
        showParameter.setCustomParam("hbss_entitytype_id", (Object)"1021");
    }

    public void registerListener(EventObject eventObject) {
        super.registerListener(eventObject);
        BasedataEdit reportTypeEdit = (BasedataEdit)this.getView().getControl("reporttype");
        reportTypeEdit.setF7BatchFill(false);
        reportTypeEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        JobLevelGradeRangeUtil.getInstance().registerListener(this.getView(), (AbstractFormPlugin)this, (BeforeF7SelectListener)this);
        List<String> registerDataColl = Arrays.asList("parent", "targetpos", "city");
        for (String registerData : registerDataColl) {
            BasedataEdit basedataEdit = (BasedataEdit)this.getView().getControl(registerData);
            basedataEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        }
        BasedataEdit positiontpl = (BasedataEdit)this.getView().getControl("positiontpl");
        positiontpl.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        BasedataEdit adminorg = (BasedataEdit)this.getView().getControl("adminorg");
        adminorg.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void afterLoadData(EventObject eventObject) {
        this.getView().setStatus(OperationStatus.VIEW);
        Date searchDate = this.getParamhistoryDate();
        if (searchDate != null) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("searchdate", HRDateTimeUtils.format((Date)searchDate));
            this.getView().getFormShowParameter().getCustomParams().put("customvariables", map);
        }
        DynamicObject dataEntity = this.getModel().getDataEntity();
        String message = MessageFormat.format(ResManager.loadKDString((String)"{0}\uff08{1}\uff09-{2}\uff08{3}\uff09", (String)"PositionEdit_0", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), dataEntity.getString("name"), dataEntity.getString("number"), dataEntity.getString("adminorg.name"), dataEntity.getString("adminorg.number"));
        this.getView().getFormShowParameter().setCustomParam("caption", (Object)message);
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        this.setCaption();
    }

    private void setControlVisibleFalse() {
        this.getView().setVisible(Boolean.FALSE, new String[]{CHANGEINFO_FLEXPANELAP, RIGHTRELATIONFLEXAP});
        this.getView().setVisible(Boolean.FALSE, new String[]{"baritemap"});
        this.getView().setVisible(Boolean.FALSE, new String[]{"bar_save", BAR_PRINT, "confirmrelation", "propertychange", "reportchange"});
    }

    private void setCaption() {
        String caption = this.isFromAdd() ? ResManager.loadKDString((String)"\u65b0\u589e\u5c97\u4f4d", (String)"PositionEdit_12", (String)"hrmp-hbpm-business", (Object[])new Object[0]) : String.format(Locale.ROOT, ResManager.loadKDString((String)"\u5c97\u4f4d - %s", (String)"PositionEdit_13", (String)"hrmp-hbpm-business", (Object[])new Object[0]), this.getModel().getDataEntity().getString("name"));
        this.getView().getFormShowParameter().setCaption(caption);
    }

    public void afterBindData(EventObject eventObject) {
        String hideInfo;
        Date searchDate;
        String positiontplId;
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        String adminorgId = (String)formShowParameter.getCustomParam("adminorg");
        if (formShowParameter.getStatus() != OperationStatus.VIEW && StringUtils.isNotEmpty((CharSequence)adminorgId)) {
            this.getModel().setValue("adminorg", (Object)adminorgId);
        }
        if (StringUtils.isNotEmpty((CharSequence)(positiontplId = (String)formShowParameter.getCustomParam("positiontpl")))) {
            this.getModel().setValue("positiontpl", (Object)positiontplId);
        }
        if ((searchDate = this.getParamhistoryDate()) != null) {
            PositionHisQueryPageUtil.positionHisFieldFill((String)"adminorg,parent,job", (IDataModel)this.getModel(), (Date)searchDate);
        }
        this.bindRelationData();
        if (this.getRecycleCache() == null && this.codeRuleExist()) {
            this.setPositionNumber();
        }
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        JobLevelGradeRangeUtil.getInstance().afterBindData((IDataModel)model, this.getView());
        DynamicObject parent = model.getDataEntity().getDynamicObject("parent");
        if (!ObjectUtils.isEmpty((Object)parent)) {
            parent.set("name", (Object)this.buildParentName(parent));
            this.getView().updateView("parent");
        }
        if (HRStringUtils.isNotEmpty((String)(hideInfo = (String)formShowParameter.getCustomParam("position_chart_hideInfo"))) && HRStringUtils.equals((String)"1", (String)hideInfo)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[]{"attachmentpanel"});
        }
        if (this.isFromAdd()) {
            this.getModel().setValue("changetype", (Object)1010L);
        } else if (this.isFromEdit()) {
            this.getModel().setValue("bsed", (Object)PositionUtils.getCurrentDate());
            this.getModel().setValue("changetype", (Object)1020L);
        }
        this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
        this.setControlVisibleFalse();
        this.setTips();
        DynamicObject dataEntity = this.getModel().getDataEntity();
        DynamicObject adminorg = dataEntity.getDynamicObject("adminorg");
        if (adminorg != null) {
            this.setPositionTplVisable(adminorg.getLong("id"), adminorg.getLong("org.id"));
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{"positiontpl"});
        }
        OperationStatus operationStatus = formShowParameter.getStatus();
        if (OperationStatus.VIEW.equals((Object)operationStatus)) {
            this.setControlVisibleWhenView();
        } else if (this.isFromAdd()) {
            this.setControlVisibleWhenAddNEW();
        } else if (this.isFromEdit()) {
            this.setControlVisibleEanbleWhenEdit(null);
        }
        this.getView().updateView("adminorg");
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        AbstractOperate operate = (AbstractOperate)args.getSource();
        this.getModel().setValue("initdatasource", (Object)"0");
        operate.getOption().setVariableValue("isFromPage", "1");
        operate.getOption().setVariableValue("appId", this.getView().getFormShowParameter().getCheckRightAppId());
        String operateKey = operate.getOperateKey();
        if (HRStringUtils.equals((String)operateKey, (String)"save") || HRStringUtils.equals((String)operateKey, (String)"confirmchange")) {
            Date bsed = this.getModel().getDataEntity().getDate("bsed");
            operate.getOption().setVariableValue("bsed", HRDateTimeUtils.format((Date)bsed, (String)"yyyy-MM-dd"));
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        String operateKey = afterDoOperationEventArgs.getOperateKey();
        long changetype = this.getModel().getDataEntity().getLong("changetype.id");
        if (1030L == changetype) {
            DateEdit control = (DateEdit)this.getView().getControl("bsed");
            control.setMaxDate(new Date());
        } else {
            HashMap<String, String> editor = new HashMap<String, String>();
            HashMap<String, HashMap<String, String>> item = new HashMap<String, HashMap<String, String>>();
            editor.put("max", "");
            item.put("item", editor);
            this.getView().updateControlMetadata("bsed", item);
        }
        if (afterDoOperationEventArgs.getOperationResult() != null && !afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            return;
        }
        if (HRStringUtils.equals((String)operateKey, (String)"reviseedit") || HRStringUtils.equals((String)operateKey, (String)"future_modify") || HRStringUtils.equals((String)operateKey, (String)"change")) {
            this.afterDoOperationForEdit(operateKey);
        } else if (HRStringUtils.equals((String)operateKey, (String)"reportchange")) {
            this.afterDoOperationForReportChange();
        } else if (HRStringUtils.equals((String)operateKey, (String)"confirmchangenoaudit")) {
            this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
            IFormView parentView = this.getView().getParentView();
            if (parentView == null) {
                return;
            }
            parentView.invokeOperation("refresh");
            this.getView().sendFormAction(parentView);
        } else if (HRStringUtils.equals((String)operateKey, (String)"dochangerelation")) {
            this.afterDochangerelation();
        } else if (HRStringUtils.equals((String)operateKey, (String)"save") || HRStringUtils.equals((String)operateKey, (String)"confirmchange")) {
            FormOperate source = (FormOperate)afterDoOperationEventArgs.getSource();
            OperateOption option = source.getOption();
            Date bsed = this.getModel().getDataEntity().getDate("bsed");
            if (option.containsVariable("bsed")) {
                String bsedStr = option.getVariableValue("bsed");
                try {
                    bsed = HRDateTimeUtils.parseDate((String)bsedStr, (String)"yyyy-MM-dd");
                }
                catch (ParseException e) {
                    logger.error("\u65e5\u671f\u8f6c\u6362\u5f02\u5e38", (Throwable)e);
                    throw new KDException(new ErrorCode("afterDoOperation", e.getMessage()), new Object[0]);
                }
            }
            if (HRDateTimeUtils.dayAfter((Date)bsed, (Date)new Date())) {
                long id;
                long successPkId = (Long)afterDoOperationEventArgs.getOperationResult().getSuccessPkIds().get(0);
                if (successPkId == (id = this.getModel().getDataEntity().getLong("id"))) {
                    this.showFutureViewPage(this.getModel().getDataEntity().getLong("sourcevid"));
                } else {
                    this.showFutureViewPage((Long)afterDoOperationEventArgs.getOperationResult().getSuccessPkIds().get(0));
                }
                this.getView().close();
            } else {
                this.getView().invokeOperation("refresh");
            }
        } else if (HRStringUtils.equals((String)operateKey, (String)"do_close")) {
            this.afterDoOperationForReportChangeConfirm();
        }
    }

    private void afterDoOperationForEdit(String operateKey) {
        this.getView().setStatus(OperationStatus.EDIT);
        this.setControlVisibleEanbleWhenEdit(operateKey);
        HashSet attachment = Sets.newHashSetWithExpectedSize((int)16);
        AttachmentUtils.findAttachments((Control)this.getView().getRootControl(), (Set)attachment);
        if (attachment.isEmpty()) {
            return;
        }
        attachment.forEach(attachKey -> this.getView().setEnable(Boolean.TRUE, new String[]{attachKey}));
    }

    private void setControlVisibleEanbleWhenEdit(String operateKey) {
        this.setPositionTplFiledEnalbe();
        this.getView().setVisible(Boolean.FALSE, new String[]{"roleflexpanelap", BAR_PRINT});
        this.getView().setVisible(Boolean.TRUE, new String[]{CHANGEINFO_FLEXPANELAP});
        this.initAnchor(OperationStatus.EDIT);
        this.getView().setEnable(Boolean.FALSE, new String[]{"adminorg"});
        this.getView().setEnable(Boolean.valueOf(false), new String[]{"positiontpl"});
        this.setJobScmEnable();
        this.getView().getFormShowParameter().setCustomParam("customHRPermItemId", (Object)"2DNXSYJWDART");
        this.getView().cacheFormShowParameter();
        this.getModel().setValue("changedesc", null);
        this.getModel().setValue("changedescription", null);
        this.getView().setVisible(Boolean.TRUE, new String[]{"changedescription"});
        if (!HRStringUtils.equals((String)operateKey, (String)"future_modify")) {
            this.showFutureTips();
        }
        this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
    }

    private void setJobScmEnable() {
        DynamicObject job = this.getModel().getDataEntity().getDynamicObject("job");
        if (job == null) {
            return;
        }
        DynamicObjectCollection jobScmMul = job.getDynamicObjectCollection("jobscm");
        if (!CollectionUtils.isEmpty((Collection)jobScmMul) && jobScmMul.size() == 1) {
            this.getModel().setValue("jobscm", (Object)((DynamicObject)jobScmMul.get(0)).getDynamicObject("fbasedataid"));
            this.getView().setEnable(Boolean.valueOf(false), new String[]{"jobscm"});
        } else {
            this.getView().setEnable(Boolean.valueOf(true), new String[]{"jobscm"});
        }
    }

    private void showFutureTips() {
        long positionId = this.getModel().getDataEntity().getLong("boid");
        if (positionId == 0L) {
            return;
        }
        DynamicObject[] positions = PositionQueryRepository.getInstance().queryPositionFutureVersion(Collections.singletonList(positionId));
        if (positions.length == 0) {
            return;
        }
        List hasFutureVersionList = Arrays.stream(positions).map(dyn -> dyn.getLong("boid")).collect(Collectors.toList());
        if (hasFutureVersionList.contains(positionId)) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u6b64\u5c97\u4f4d\u5b58\u5728\u5f85\u751f\u6548\u7248\u672c\u3002", (String)"PositionEdit_1", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), Integer.valueOf(5000));
        }
    }

    private void afterDoOperationForReportChange() {
        this.getView().setStatus(OperationStatus.EDIT);
        this.getView().setVisible(Boolean.TRUE, new String[]{"confirmrelation", CHANGEINFO_FLEXPANELAP});
        this.getView().setVisible(Boolean.TRUE, new String[]{"changedescription"});
        this.getView().setVisible(Boolean.FALSE, new String[]{"bar_save", BAR_PRINT});
        this.getView().setVisible(Boolean.FALSE, new String[]{"propertychange", "reportchange"});
        this.getView().setVisible(Boolean.FALSE, new String[]{"baseinfoflexpanelap", "attachmentpanel", "jobinfoflexpanelap", "dutyinfoflexpanelap", "qualinfoflexpanelap"});
        this.getModel().setValue("bsed", (Object)PositionUtils.getCurrentDate());
        this.getView().setVisible(Boolean.FALSE, new String[]{anchor_controlap});
        this.getView().getFormShowParameter().setCustomParam("customHRPermItemId", (Object)"2DNXU/27ZSXL");
        this.getView().cacheFormShowParameter();
        this.getModel().setValue("changetype", (Object)1030L);
        this.getView().setVisible(Boolean.TRUE, new String[]{"baritemap", "roleflexpanelap"});
        this.getView().setVisible(Boolean.FALSE, new String[]{"bar_close"});
        this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
        this.getView().updateView("baritemap");
        DateEdit control = (DateEdit)this.getView().getControl("bsed");
        control.setMaxDate(new Date());
    }

    public void click(EventObject evt) {
        super.click(evt);
        Control source = (Control)evt.getSource();
        String operationKey = source.getKey();
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        JobLevelGradeRangeUtil.getInstance().click(model, view, operationKey, (IFormPlugin)this);
    }

    public void propertyChanged(PropertyChangedArgs propertyChangedArgs) {
        String propKey = propertyChangedArgs.getProperty().getName();
        DynamicObject dataEntity = this.getModel().getDataEntity();
        IDataModel model = this.getModel();
        boolean isMustRelateJob = SystemParamHelper.getPosMustRelateJobParameter((Long)model.getDataEntity().getLong("org.id"));
        JobLevelGradeRangeUtil.getInstance().propertyChanged(model, this.getView(), propKey, isMustRelateJob);
        switch (propKey) {
            case "adminorg": {
                DynamicObject adminorg = dataEntity.getDynamicObject("adminorg");
                if (adminorg != null) {
                    this.getModel().setValue("countryregion", (Object)adminorg.getDynamicObject("companyarea"));
                    this.getModel().setValue("city", (Object)adminorg.getDynamicObject("city"));
                    this.getModel().setValue("workplace", (Object)adminorg.getDynamicObject("workplace"));
                    this.getModel().setValue("org", (Object)adminorg.getDynamicObject("org"));
                } else {
                    this.setAllTplFieldEnable();
                    this.getModel().setValue("org", null);
                }
                this.getModel().setValue("positiontpl", null);
                this.getView().updateView("positiontpl");
                if (adminorg != null) {
                    this.setPositionTplVisable(adminorg.getLong("id"), adminorg.getLong("org.id"));
                    break;
                }
                this.getView().setVisible(Boolean.FALSE, new String[]{"positiontpl"});
                break;
            }
            case "city": {
                this.handleCity(dataEntity);
                break;
            }
            case "countryregion": {
                this.getModel().setValue("city", null);
                break;
            }
            case "parent": {
                DynamicObject parent = dataEntity.getDynamicObject("parent");
                if (ObjectUtils.isEmpty((Object)parent)) break;
                parent.set("name", (Object)this.buildParentName(parent));
                this.getView().updateView("parent");
                break;
            }
            case "bsed": {
                Date establishmentDate = dataEntity.getDate("establishmentdate");
                if (establishmentDate != null) break;
                this.getModel().setValue("establishmentdate", (Object)dataEntity.getDate("bsed"));
                this.getView().updateView("establishmentdate");
                break;
            }
            case "positiontpl": {
                this.changePositionTpl(dataEntity);
                break;
            }
        }
        if (HRStringUtils.equals((String)"number", (String)propKey)) {
            this.getPageCache().put("toGetCodeRuleTraceId", RequestContext.get().getTraceId());
        }
        if (this.getNumberRuleAllFieldSet().contains(propKey) && HRStringUtils.equals((String)RequestContext.get().getTraceId(), (String)this.getPageCache().get("toGetCodeRuleTraceId"))) {
            this.changeNumber();
        }
    }

    private void changePositionTpl(DynamicObject dataEntity) {
        DynamicObject positiontpl = dataEntity.getDynamicObject("positiontpl");
        Set positiontplfields = PositionTplFieldRepository.getInstance().queryAllNumber();
        if (positiontpl == null) {
            positiontplfields.stream().filter(key -> !EXINCLUDE_POSITIONTPL_FILED_KEYS.contains(key)).forEach(key -> {
                if (this.getModel().getDataEntity().containsProperty(key)) {
                    this.getModel().setValue(key, null);
                    this.getView().updateView(key);
                }
            });
            this.setAllTplFieldEnable();
            return;
        }
        HashSet<String> positiontplfieldSet = new HashSet<String>(POSITIONTPL_FILED_KEYS);
        positiontplfieldSet.addAll(positiontplfields);
        DynamicObject[] positiontplFromDB = PositionTplRepository.getInstance().queryPositionTplPosFieldById(Collections.singletonList(positiontpl.getLong("id")), positiontplfieldSet);
        if (positiontplFromDB == null || positiontplFromDB.length < 1) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u5c97\u4f4d\u6a21\u677f\u7684\u6570\u636e\u672a\u627e\u5230\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9\u5c97\u4f4d\u6a21\u677f\u3002", (String)"PositionEdit_2", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]));
            return;
        }
        ArrayList<String> positiontplfieldList = new ArrayList<String>(positiontplfields);
        positiontplfieldList.removeAll(POSITIONTPL_FILED_KEYS);
        positiontplfieldList.addAll(POSITIONTPL_FILED_KEYS);
        positiontplfieldList.stream().filter(key -> !EXINCLUDE_POSITIONTPL_FILED_KEYS.contains(key)).forEach(key -> {
            if (this.getModel().getDataEntity().containsProperty(key) && positiontplFromDB[0].containsProperty(key)) {
                this.getModel().setValue(key, positiontplFromDB[0].get(key));
                this.getView().updateView(key);
            }
        });
        JobLevelGradeRangeUtil.getInstance().setFieldRange("joblevelrange", this.getModel().getDataEntity(true).getDynamicObject("lowjoblevel"), "lowjoblevel", this.getModel().getDataEntity(true).getDynamicObject("highjoblevel"), "highjoblevel", this.getModel());
        JobLevelGradeRangeUtil.getInstance().setFieldRange("jobgraderange", this.getModel().getDataEntity(true).getDynamicObject("lowjobgrade"), "lowjobgrade", this.getModel().getDataEntity(true).getDynamicObject("highjobgrade"), "highjobgrade", this.getModel());
        this.setTplFieldEnable(positiontpl);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId = closedCallBackEvent.getActionId();
        if (!Arrays.asList("hbjm_joblevelrange", "hbjm_jobgraderange").contains(actionId)) {
            return;
        }
        Map returnData = (Map)closedCallBackEvent.getReturnData();
        IDataModel model = this.getModel();
        JobLevelGradeRangeUtil.getInstance().closedCallBack(model, returnData, actionId);
    }

    private void initAnchor(OperationStatus operationStatus) {
        AnchorControl anchorCtl = (AnchorControl)this.getControl(anchor_controlap);
        ArrayList anchorItemlist = null;
        AnchorItems changeinfoAnchorItem = new AnchorItems(CHANGEINFO_FLEXPANELAP, ResManager.loadKDString((String)"\u53d8\u52a8\u4fe1\u606f", (String)"PositionEdit_4", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), null);
        AnchorItems baseinfoAnchorItem = new AnchorItems("baseinfoflexpanelap", ResManager.loadKDString((String)"\u57fa\u672c\u4fe1\u606f", (String)"PositionEdit_5", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), null);
        AnchorItems jobInfoAnchorItem = new AnchorItems("jobinfoflexpanelap", ResManager.loadKDString((String)"\u804c\u4f4d\u4fe1\u606f", (String)"PositionEdit_6", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), null);
        AnchorItems dutyinfoAnchorItem = new AnchorItems("dutyinfoflexpanelap", ResManager.loadKDString((String)"\u804c\u8d23\u4fe1\u606f", (String)"PositionEdit_7", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), null);
        AnchorItems qualAnchorItem = new AnchorItems("qualflexpanelap", ResManager.loadKDString((String)"\u4efb\u804c\u8981\u6c42", (String)"PositionEdit_8", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), null);
        AnchorItems roleAnchorItem = new AnchorItems("roleflexpanelap", ResManager.loadKDString((String)"\u534f\u4f5c\u5173\u7cfb", (String)"PositionEdit_9", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), null);
        AnchorItems attachmentpanel = new AnchorItems("attachmentpanel", ResManager.loadKDString((String)"\u9644\u4ef6", (String)"PositionEdit_10", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), null);
        if (OperationStatus.ADDNEW.equals((Object)operationStatus)) {
            anchorItemlist = Lists.newArrayList(Arrays.asList(changeinfoAnchorItem, baseinfoAnchorItem, jobInfoAnchorItem, dutyinfoAnchorItem, qualAnchorItem, roleAnchorItem, attachmentpanel));
        } else if (OperationStatus.VIEW.equals((Object)operationStatus)) {
            anchorItemlist = Lists.newArrayList(Arrays.asList(baseinfoAnchorItem, jobInfoAnchorItem, dutyinfoAnchorItem, qualAnchorItem, roleAnchorItem, attachmentpanel));
        } else if (OperationStatus.EDIT.equals((Object)operationStatus)) {
            anchorItemlist = Lists.newArrayList(Arrays.asList(changeinfoAnchorItem, baseinfoAnchorItem, jobInfoAnchorItem, dutyinfoAnchorItem, qualAnchorItem, attachmentpanel));
        }
        anchorCtl.addItems(anchorItemlist);
        anchorCtl.setHighlight(true);
    }

    private void setControlVisibleWhenView() {
        FormShowParameter formShowParameter;
        IFormView parentView;
        this.initAnchor(OperationStatus.VIEW);
        String sourcePage = (String)this.getView().getFormShowParameter().getCustomParam("source");
        if ("hbpm_position_future".equals(sourcePage)) {
            this.setButtonVisable();
        }
        if ((parentView = this.getView().getParentViewNoPlugin()) != null && parentView.getFormShowParameter() instanceof ListShowParameter) {
            ListShowParameter parentForm = (ListShowParameter)parentView.getFormShowParameter();
            boolean isTureListFormId = HRStringUtils.equals((String)parentForm.getFormId(), (String)FormMetadataCache.getListFormConfig((String)"hbpm_positionhr").getListFormId());
            boolean isTureEntity = HRStringUtils.equals((String)parentForm.getBillFormId(), (String)"hbpm_positionhr");
            if (isTureListFormId && isTureEntity) {
                this.setButtonVisable();
            }
        }
        if (HRStringUtils.equals((String)((String)(formShowParameter = this.getView().getFormShowParameter()).getCustomParam("showrelateinfo")), (String)"1")) {
            this.getView().setVisible(Boolean.TRUE, new String[]{RIGHTRELATIONFLEXAP});
        }
    }

    private void setButtonVisable() {
        this.getView().setVisible(Boolean.TRUE, new String[]{BAR_PRINT});
        String enable = this.getModel().getDataEntity().getString("enable");
        if (HRStringUtils.equals((String)"1", (String)enable)) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"propertychange", "reportchange"});
        }
    }

    private void setControlVisibleWhenAddNEW() {
        this.getView().setVisible(Boolean.TRUE, new String[]{"bar_save", CHANGEINFO_FLEXPANELAP});
        this.getView().setVisible(Boolean.TRUE, new String[]{"changedescription"});
        this.setPositionTplFiledEnalbe();
        this.initAnchor(OperationStatus.ADDNEW);
    }

    private void changeNumber() {
        if (!this.isFromAdd()) {
            return;
        }
        if (this.codeRuleExist()) {
            this.recycleNumber();
            this.setPositionNumber();
        } else {
            Map<String, Object> recycleMap = this.getRecycleCache();
            String number = (String)this.getModel().getValue("number");
            if (recycleMap != null && HRStringUtils.equals((String)number, (String)((String)recycleMap.get("number")))) {
                this.getModel().setValue("number", null);
            }
            this.getView().setEnable(Boolean.TRUE, new String[]{"number"});
            this.recycleNumber();
        }
    }

    private void recycleNumber() {
        Map<String, Object> recycleMap = this.getRecycleCache();
        if (recycleMap == null) {
            return;
        }
        DynamicObject billEntity = new HRBaseServiceHelper("hbpm_positionhr").generateEmptyDynamicObject();
        recycleMap.forEach((field, value) -> {
            if (value != null) {
                billEntity.set(field, recycleMap.get(field));
            }
        });
        PositionCodeRuleHelper.recycleNumber((DynamicObject[])new DynamicObject[]{billEntity});
        this.clearRecycleCache();
    }

    private void setPositionNumber() {
        if (!this.isFromAdd()) {
            return;
        }
        DynamicObject dataEntity = this.getModel().getDataEntity();
        DynamicObject org = dataEntity.getDynamicObject("org");
        String orgValue = null;
        if (Objects.nonNull(org)) {
            orgValue = org.getString("id");
        }
        String number = PositionCodeRuleHelper.getCode((String)"hbpm_positionhr", (DynamicObject)dataEntity, (String)orgValue);
        this.getModel().setValue("number", (Object)number);
        this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
        this.setRecycleCache();
    }

    private boolean isFromAdd() {
        if (!OperationStatus.ADDNEW.equals((Object)this.getView().getFormShowParameter().getStatus())) {
            return false;
        }
        return !HRStringUtils.equals((String)((String)this.getView().getFormShowParameter().getCustomParam("hisPage")), (String)HisPageEnum.CHANGE_PAGE.getPage());
    }

    private boolean isFromEdit() {
        if (!OperationStatus.ADDNEW.equals((Object)this.getView().getFormShowParameter().getStatus())) {
            return false;
        }
        return HRStringUtils.equals((String)((String)this.getView().getFormShowParameter().getCustomParam("hisPage")), (String)HisPageEnum.CHANGE_PAGE.getPage());
    }

    private void bindRelationData() {
        List reportRelations;
        AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        long positionId = model.getDataEntity().getLong("boid");
        if (positionId == 0L) {
            return;
        }
        ArrayList<Long> positionIdList = new ArrayList<Long>();
        positionIdList.add(positionId);
        Date queryDate = this.getParamhistoryDate();
        if (Objects.nonNull(queryDate)) {
            reportRelations = ReportingrelationQueryRepository.getInstance().queryChangeRelationhis(positionIdList, queryDate);
        } else {
            Date bsed = model.getDataEntity().getDate("bsed");
            if (bsed == null) {
                return;
            }
            reportRelations = HRDateTimeUtils.dayAfter((Date)bsed, (Date)new Date()) ? ReportingrelationQueryRepository.getInstance().queryFutureReportRelationByBsed(positionIdList, model.getDataEntity().getDate("bsed")) : ReportingrelationQueryRepository.getInstance().queryReportRelationByWorkRoleIds(positionIdList);
        }
        model.beginInit();
        model.batchCreateNewEntryRow("entryentity", this.insertEntryEntity(reportRelations));
        model.endInit();
        this.getView().updateView("entryentity");
    }

    private TableValueSetter insertEntryEntity(List<DynamicObject> reportRelations) {
        TableValueSetter tableValueSetter = new TableValueSetter(new String[0]);
        tableValueSetter.addField("id", new Object[0]);
        tableValueSetter.addField("reporttype", new Object[0]);
        tableValueSetter.addField("targetpos", new Object[0]);
        for (DynamicObject reportRelation : reportRelations) {
            if (reportRelation.getLong("reportingtype.id") == 0L) continue;
            tableValueSetter.addRow(new Object[]{reportRelation.getLong("id"), reportRelation.getLong("reportingtype.id"), reportRelation.getLong("parent.id")});
        }
        return tableValueSetter;
    }

    private String buildParentName(DynamicObject parent) {
        StringBuilder sb = new StringBuilder();
        String positionName = parent.getString("name");
        DynamicObject adminOrg = parent.getDynamicObject("adminorg");
        if (adminOrg != null && !positionName.startsWith(adminOrg.getString("name") + "/")) {
            String adminorgName = adminOrg.getString("name");
            sb.append(adminorgName).append("/").append(positionName);
        } else {
            sb.append(positionName);
        }
        return sb.toString();
    }

    private boolean codeRuleExist() {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        DynamicObject org = dataEntity.getDynamicObject("org");
        String orgValue = null;
        if (Objects.nonNull(org)) {
            orgValue = org.getString("id");
        }
        return CodeRuleServiceHelper.isExist((String)"hbpm_positionhr", (DynamicObject)dataEntity, (String)orgValue);
    }

    private void handleCity(DynamicObject dataEntity) {
        DynamicObject workPlace;
        DynamicObject countryregion = dataEntity.getDynamicObject("countryregion");
        DynamicObject city = dataEntity.getDynamicObject("city");
        if (ObjectUtils.isEmpty((Object)countryregion) && !ObjectUtils.isEmpty((Object)city)) {
            this.getModel().beginInit();
            this.getModel().setValue("countryregion", (Object)city.getDynamicObject("country"));
            this.getView().updateView("countryregion");
            this.getModel().endInit();
        } else if (!ObjectUtils.isEmpty((Object)countryregion) && !ObjectUtils.isEmpty((Object)city) && (workPlace = PositionBillQueryRepository.getInstance().queryWorkPlace(Long.valueOf(countryregion.getLong("id")), Long.valueOf(city.getLong("id")))) != null) {
            this.getModel().setValue("workplace", (Object)workPlace);
        }
    }

    public void pageRelease(EventObject e) {
        if (this.isFromAdd()) {
            this.recycleNumber();
        }
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
        if (this.isFromAdd()) {
            this.recycleNumber();
        }
    }

    private void setRecycleCache() {
        Set<String> numberRuleFieldList = this.getNumberRuleAllFieldSet();
        HashMap<String, Object> recycleMap = new HashMap<String, Object>();
        recycleMap.put("number", this.getModel().getDataEntity().getString("number"));
        for (String field : numberRuleFieldList) {
            Object fieldData = this.getModel().getDataEntity().get(field);
            if (fieldData instanceof DynamicObject) {
                recycleMap.put(field, ((DynamicObject)fieldData).getLong("id"));
                continue;
            }
            recycleMap.put(field, fieldData);
        }
        this.getView().getPageCache().put(CACHE_KEY_RECYCLEMAP, SerializationUtils.toJsonString(recycleMap));
    }

    private Set<String> getNumberRuleAllFieldSet() {
        HashSet<String> numberRuleFieldList = new HashSet<String>();
        numberRuleFieldList.add("org");
        numberRuleFieldList.add("adminorg");
        numberRuleFieldList.add("parent");
        numberRuleFieldList.add("job");
        numberRuleFieldList.add("positiontype");
        String numberRuleExtField = this.getPageCache().get(NUMBERRULE_EXTFIELD);
        if (HRStringUtils.isNotEmpty((String)numberRuleExtField)) {
            Set extFields = (Set)SerializationUtils.fromJsonString((String)numberRuleExtField, Set.class);
            numberRuleFieldList.addAll(extFields);
        }
        return numberRuleFieldList;
    }

    private void clearRecycleCache() {
        this.getView().getPageCache().put(CACHE_KEY_RECYCLEMAP, null);
    }

    private Map<String, Object> getRecycleCache() {
        String cache = this.getView().getPageCache().get(CACHE_KEY_RECYCLEMAP);
        if (cache == null) {
            return null;
        }
        return (Map)SerializationUtils.fromJsonString((String)cache, Map.class);
    }

    private void setPositionTplFiledEnalbe() {
        logger.info("start setPositionTplFiledEnalbe");
        String positiontplInvisible = this.getView().getPageCache().get("positiontpl_invisible");
        if (HRStringUtils.isEmpty((String)positiontplInvisible) || HRStringUtils.equals((String)positiontplInvisible, (String)"0")) {
            return;
        }
        long positiontplId = this.getModel().getDataEntity(true).getLong("positiontpl.id");
        if (positiontplId != 0L) {
            DynamicObject[] tplDys = PositionTplRepository.getInstance().queryPositionTplByIds(Collections.singletonList(positiontplId));
            if (tplDys == null || tplDys.length == 0) {
                return;
            }
            DynamicObject tplDy = tplDys[0];
            this.setTplFieldEnable(tplDy);
        }
    }

    private void setTplFieldEnable(DynamicObject tplDy) {
        IFormView view = this.getView();
        boolean ablemodifyfield = tplDy.getBoolean("ablemodifyfield");
        HashSet allField = (HashSet)PositionTplFieldRepository.getInstance().queryAllNumber();
        if (!ablemodifyfield) {
            view.setEnable(Boolean.FALSE, allField.toArray(new String[allField.size()]));
        } else {
            DynamicObjectCollection fieldrange = tplDy.getDynamicObjectCollection("fieldrange");
            Set<String> enableSet = fieldrange.stream().map(temp -> temp.getString("fbasedataid.number")).collect(Collectors.toSet());
            HashSet disableSet = (HashSet)Sets.difference((Set)allField, enableSet).copyInto(new HashSet());
            if (enableSet.contains("joblevelrange")) {
                enableSet.add("joblevelscm");
            }
            if (enableSet.contains("jobgraderange")) {
                enableSet.add("jobgradescm");
            }
            if (disableSet.contains("joblevelrange")) {
                disableSet.add("joblevelscm");
            }
            if (disableSet.contains("jobgraderange")) {
                disableSet.add("jobgradescm");
            }
            view.setEnable(Boolean.TRUE, enableSet.toArray(new String[enableSet.size()]));
            view.setEnable(Boolean.FALSE, disableSet.toArray(new String[disableSet.size()]));
        }
    }

    private void setPositionTplVisable(Long adminorgId, Long orgId) {
        Map batchParameter = SystemParamHelper.getBatchParameter(Collections.singletonList(orgId));
        Map parameter = batchParameter.getOrDefault(String.valueOf(orgId), Maps.newHashMap());
        PositionTplValidateServiceImpl positionValidateService = new PositionTplValidateServiceImpl();
        boolean isOpenDailog = positionValidateService.openDialogOrPositionBill(adminorgId).test(parameter);
        boolean openpositiontpl = parameter.getOrDefault("openpositiontpl", false);
        BasedataEdit positiontpl = (BasedataEdit)this.getView().getControl("positiontpl");
        if (openpositiontpl) {
            this.getView().getPageCache().put("positiontpl_invisible", "1");
            String radiogroupfield = parameter.getOrDefault("radiogroupfield", "");
            this.getView().setVisible(Boolean.TRUE, new String[]{"positiontpl"});
            positiontpl.setMustInput(Boolean.TRUE.booleanValue());
            if ("2".equals(radiogroupfield)) {
                if (!isOpenDailog) {
                    this.getView().setVisible(Boolean.FALSE, new String[]{"positiontpl"});
                    this.getView().getPageCache().put("positiontpl_invisible", "0");
                    positiontpl.setMustInput(Boolean.FALSE.booleanValue());
                }
            } else if ("3".equals(radiogroupfield)) {
                this.getView().setVisible(Boolean.TRUE, new String[]{"positiontpl"});
                positiontpl.setMustInput(Boolean.FALSE.booleanValue());
            }
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{"positiontpl"});
            this.getView().getPageCache().put("positiontpl_invisible", "0");
            positiontpl.setMustInput(Boolean.FALSE.booleanValue());
            this.setAllTplFieldEnable();
        }
    }

    private void setAllTplFieldEnable() {
        List number = PositionTplFieldRepository.getInstance().queryAllNumberList();
        Object[] objects = number.toArray();
        String[] disableFieldGroup = (String[])Arrays.copyOf(objects, objects.length, String[].class);
        this.getView().setEnable(Boolean.TRUE, disableFieldGroup);
    }

    private void setTips() {
        Tips tips = new Tips();
        List tipsStr = (List)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSService", (String)"queryPromptForString", (Object[])new Object[]{this.getModel().getDataEntityType().getName(), "parent", this.getModel().getDataEntity()});
        if (!CollectionUtils.isEmpty((Collection)tipsStr)) {
            tips.setContent(new LocaleString((String)tipsStr.get(0)));
            tips.setType("text");
            tips.setTriggerType("hover");
            tips.setIsConfirm(false);
            tips.setShowIcon(true);
            TipsSupport control = (TipsSupport)this.getControl("parent");
            control.addTips(tips);
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        IDataEntityProperty property = beforeF7SelectEvent.getProperty();
        PositionUtils.reNameF7Capture((String)"hbpm_positionhr", (String)",parent,job,", (BeforeF7SelectEvent)beforeF7SelectEvent);
        if (HRStringUtils.equals((String)property.getName(), (String)"city")) {
            DynamicObject countryregion = (DynamicObject)this.getModel().getValue("countryregion");
            if (!ObjectUtils.isEmpty((Object)countryregion)) {
                QFilter qFilter = new QFilter("country.id", "=", (Object)countryregion.getLong("id"));
                ArrayList<QFilter> customQFilters = new ArrayList<QFilter>();
                customQFilters.add(qFilter);
                beforeF7SelectEvent.setCustomQFilters(customQFilters);
            }
        } else if (HRStringUtils.equals((String)property.getName(), (String)"positiontpl")) {
            DynamicObject adminorg = (DynamicObject)this.getModel().getValue("adminorg");
            if (adminorg == null) {
                return;
            }
            long adminorgId = adminorg.getLong("id");
            String structlongnumber = adminorg.getString("structlongnumber");
            PositionTplApplicationScopeServiceImpl positionTplApplicationScopeService = new PositionTplApplicationScopeServiceImpl();
            List longs = positionTplApplicationScopeService.queryApplicationScope(Long.valueOf(adminorgId), structlongnumber);
            beforeF7SelectEvent.getCustomQFilters().add(new QFilter("id", "in", (Object)longs));
        } else if (HRStringUtils.equals((String)property.getName(), (String)"adminorg")) {
            HasPermOrgResult permOrgResult = this.getPermOrgResult();
            if (!permOrgResult.hasAllOrgPerm()) {
                QFilter baseDataFilter = PermHelper.getBaseDataFilter((String)"haos_adminorghrf7", (List)permOrgResult.getHasPermOrgs());
                beforeF7SelectEvent.getCustomQFilters().add(baseDataFilter);
            }
        } else if (HRStringUtils.equals((String)property.getName(), (String)"parent")) {
            Long boid = this.getModel().getDataEntity().getLong("boid");
            if (boid != null && boid > 0L) {
                beforeF7SelectEvent.getCustomQFilters().add(new QFilter("boid", "!=", (Object)boid));
            }
        } else {
            boolean isMustRelateJob = SystemParamHelper.getPosMustRelateJobParameter((Long)this.getModel().getDataEntity().getLong("org.id"));
            JobLevelGradeRangeUtil.getInstance().beforeF7Select(beforeF7SelectEvent, this.getModel(), this.getView(), isMustRelateJob);
        }
        if (HRStringUtils.equals((String)property.getName(), (String)"targetpos") || HRStringUtils.equals((String)property.getName(), (String)"parent") || HRStringUtils.equals((String)property.getName(), (String)"adminorg")) {
            Date now = HRDateTimeUtils.getNowDate();
            Date bsed = HRDateTimeUtils.truncateDate((Date)this.getModel().getDataEntity().getDate("bsed"));
            if (bsed != null && HRDateTimeUtils.dayBefore((Date)bsed, (Date)now)) {
                ListShowParameter listShowParameter = (ListShowParameter)beforeF7SelectEvent.getFormShowParameter();
                listShowParameter.setCustomParam("effectdate", (Object)now);
            }
        }
    }

    private HasPermOrgResult getPermOrgResult() {
        if (this.hasPermOrgResult == null) {
            String orgPermString = this.getPageCache().get(ORG_PERM_CACHE_KEY);
            if (orgPermString == null) {
                this.hasPermOrgResult = PermHelper.getHRPermOrg((String)this.getAppId(), (String)this.getEntityName());
                this.getPageCache().put(ORG_PERM_CACHE_KEY, ((HasPermOrgResultImpl)this.hasPermOrgResult).toSerializeStr());
            } else {
                this.hasPermOrgResult = HasPermOrgResultImpl.fromSerializeStr((String)orgPermString);
                if (!this.hasPermOrgResult.hasAllOrgPerm()) {
                    List hasPermOrgs = this.hasPermOrgResult.getHasPermOrgs();
                    for (int i = 0; i < hasPermOrgs.size(); ++i) {
                        hasPermOrgs.set(i, Long.parseLong(hasPermOrgs.get(i) + ""));
                    }
                }
            }
        }
        return this.hasPermOrgResult;
    }

    public String getEntityName() {
        return this.getView().getFormShowParameter().getFormId();
    }

    public String getAppId() {
        return this.getView().getFormShowParameter().getAppId();
    }

    private Date getParamhistoryDate() {
        Object customParam = this.getView().getFormShowParameter().getCustomParam("historyDate");
        Date bsed = null;
        if (customParam == null) {
            return null;
        }
        try {
            bsed = HRDateTimeUtils.parseDate((String)customParam.toString());
        }
        catch (ParseException exception) {
            logger.error(exception.getMessage());
            throw new KDException(new ErrorCode("getParamhistoryDate", exception.getMessage()), new Object[0]);
        }
        return bsed;
    }

    private void afterDoOperationForReportChangeConfirm() {
        this.getView().setStatus(OperationStatus.VIEW);
        this.getView().setVisible(Boolean.FALSE, new String[]{"confirmrelation", CHANGEINFO_FLEXPANELAP});
        this.getView().setVisible(Boolean.TRUE, new String[]{"bar_save", BAR_PRINT});
        this.getView().setVisible(Boolean.TRUE, new String[]{"propertychange", "reportchange"});
        this.getView().setVisible(Boolean.TRUE, new String[]{"baseinfoflexpanelap", "attachmentpanel", "jobinfoflexpanelap", "dutyinfoflexpanelap", "qualinfoflexpanelap"});
        this.getView().setVisible(Boolean.TRUE, new String[]{anchor_controlap});
        this.getView().setVisible(Boolean.FALSE, new String[]{"baritemap"});
        this.getView().setVisible(Boolean.TRUE, new String[]{"bar_close"});
        this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        this.getPageCache().put("skipChangeTips", "true");
    }

    private void showFutureViewPage(long futureVid) {
        BillShowParameter formShowParameter = new BillShowParameter();
        formShowParameter.setFormId("hbpm_position_future");
        formShowParameter.setPkId((Object)futureVid);
        formShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        formShowParameter.setStatus(OperationStatus.VIEW);
        formShowParameter.setCustomParam("isfuturetips", (Object)"1");
        this.getView().showForm((FormShowParameter)formShowParameter);
    }

    private void afterDochangerelation() {
        this.getView().setVisible(Boolean.FALSE, new String[]{"confirmrelation", CHANGEINFO_FLEXPANELAP});
        this.getView().setVisible(Boolean.TRUE, new String[]{"baseinfoflexpanelap", "attachmentpanel", "jobinfoflexpanelap", "dutyinfoflexpanelap", "qualinfoflexpanelap", "roleflexpanelap", anchor_controlap});
        this.getView().setVisible(Boolean.TRUE, new String[]{"bar_close"});
        this.getModel().setDataChanged(Boolean.TRUE.booleanValue());
        this.getView().invokeOperation("refresh");
    }
}
