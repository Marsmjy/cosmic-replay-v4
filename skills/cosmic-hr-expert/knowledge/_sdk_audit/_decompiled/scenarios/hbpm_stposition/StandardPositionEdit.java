/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.ImportDataEventArgs
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.container.Container
 *  kd.bos.form.control.AttachmentPanel
 *  kd.bos.form.control.Control
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.servicehelper.coderule.CodeRuleServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.constants.history.HisPageEnum
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hrmp.hbpm.business.domain.position.service.impl.PositionCodeRuleHelper
 *  kd.hrmp.hbpm.business.domain.position.service.repository.StandardPositionRepository
 *  kd.hrmp.hbpm.business.utils.JobLevelGradeRangeUtil
 *  kd.hrmp.hbpm.business.utils.PositionUtils
 */
package kd.hrmp.hbpm.formplugin.web.standardposition;

import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.ImportDataEventArgs;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.container.Container;
import kd.bos.form.control.AttachmentPanel;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.constants.history.HisPageEnum;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hrmp.hbpm.business.domain.position.service.impl.PositionCodeRuleHelper;
import kd.hrmp.hbpm.business.domain.position.service.repository.StandardPositionRepository;
import kd.hrmp.hbpm.business.utils.JobLevelGradeRangeUtil;
import kd.hrmp.hbpm.business.utils.PositionUtils;

public final class StandardPositionEdit
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static final String CACHE_KEY_RECYCLEMAP = "recycleMap";
    private static final String NUMBERRULE_EXTFIELD = "numberrule_extfield";
    public static final String UID = "uid";

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        this.addClickListeners(new String[]{"attachmentpanelap"});
        JobLevelGradeRangeUtil.getInstance().registerListener(this.getView(), (AbstractFormPlugin)this, (BeforeF7SelectListener)this);
        BasedataEdit positiontype = (BasedataEdit)this.getView().getControl("positiontype");
        positiontype.setF7BatchFill(false);
        positiontype.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        IFormView view = this.getView();
        IDataModel model = this.getModel();
        String enable = (String)this.getModel().getValue("enable");
        if (HRStringUtils.equals((String)"10", (String)enable)) {
            view.setStatus(OperationStatus.VIEW);
            view.setVisible(Boolean.valueOf(true), new String[]{"bar_modify"});
        }
        JobLevelGradeRangeUtil.getInstance().afterBindData(model, view);
        String option = (String)this.getView().getFormShowParameter().getCustomParam("option");
        String showBsed = (String)this.getView().getFormShowParameter().getCustomParam("showBsed");
        if ("false".equals(showBsed)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"bsed"});
        } else {
            this.getView().setVisible(Boolean.TRUE, new String[]{"bsed"});
        }
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        this.buttonVisibleAfterBindData();
        this.updateUidIntoCache();
        if (this.getView().getFormShowParameter().getStatus().equals((Object)OperationStatus.ADDNEW)) {
            this.getModel().setValue("org", null);
        }
        if (HRStringUtils.equalsIgnoreCase((String)this.getModel().getDataEntity().getString("status"), (String)"C")) {
            this.getView().setEnable(Boolean.valueOf(false), new String[]{"adminorg"});
        }
        this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
        if (this.getRecycleCache() == null && this.codeRuleExist()) {
            this.setPositionNumber();
        }
    }

    protected void buttonVisibleAfterBindData() {
        boolean statusFlag;
        String enable = (String)this.getModel().getValue("enable");
        if (!HRStringUtils.equals((String)"1", (String)enable)) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"insertdatabtn"});
            this.getView().updateView("insertdatabtn");
        }
        boolean isAudit = HRBaseDataConfigUtil.getAudit((String)"hbpm_stposition");
        String status = (String)this.getModel().getValue("status");
        boolean bl = statusFlag = HRStringUtils.equals((String)"C", (String)status) || HRStringUtils.equals((String)"B", (String)status);
        if (statusFlag && isAudit) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"bar_modify"});
            this.getView().updateView("bar_modify");
            this.getView().getModel().setDataChanged(false);
        }
    }

    public void preOpenForm(PreOpenFormEventArgs arg) {
        super.preOpenForm(arg);
        FormShowParameter formShowParameter = arg.getFormShowParameter();
        formShowParameter.setCustomParam("showBsed", (Object)"true");
        if (formShowParameter.getStatus().equals((Object)OperationStatus.ADDNEW)) {
            String name = ResManager.loadKDString((String)"\u6807\u51c6\u5c97\u4f4d-\u65b0\u589e", (String)"StandardPositionEdit_0", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
            if (HRStringUtils.equals((String)formShowParameter.getParentFormId(), (String)"hbpm_stposition")) {
                name = ResManager.loadKDString((String)"\u6807\u51c6\u5c97\u4f4d-\u53d8\u66f4", (String)"StandardPositionEdit_1", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
            }
            arg.getFormShowParameter().setCaption(name);
            return;
        }
        if (formShowParameter instanceof BaseShowParameter) {
            Object pkId = ((BaseShowParameter)formShowParameter).getPkId();
            if (Objects.isNull(pkId) || !(pkId instanceof Long)) {
                return;
            }
            Object[] dynamicObjects = StandardPositionRepository.getInstance().queryAllStandardPosition(Collections.singletonList((Long)pkId));
            if (!PositionUtils.isArrayEmpty((Object[])dynamicObjects).booleanValue()) {
                String name = ResManager.loadKDString((String)"\u6807\u51c6\u5c97\u4f4d-", (String)"StandardPositionEdit_2", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
                arg.getFormShowParameter().setCaption(name + dynamicObjects[0].getString("name"));
                formShowParameter.setCustomParam("showBsed", (Object)"false");
            }
        }
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        PositionUtils.reNameF7Capture((String)"hbpm_stposition", (String)",job,", (BeforeF7SelectEvent)evt);
        JobLevelGradeRangeUtil.getInstance().beforeF7Select(evt, model, view, false);
    }

    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        String operationKey = args.getProperty().getName();
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        DynamicObject adminorg = model.getDataEntity().getDynamicObject("adminorg");
        if (adminorg != null) {
            this.getModel().setValue("org", (Object)adminorg.getDynamicObject("org"));
        }
        JobLevelGradeRangeUtil.getInstance().propertyChanged(model, view, operationKey, false);
        if (HRStringUtils.equals((String)"number", (String)operationKey)) {
            this.getPageCache().put("toGetCodeRuleTraceId", RequestContext.get().getTraceId());
        }
        if (this.getNumberRuleAllFieldSet().contains(operationKey) && HRStringUtils.equals((String)RequestContext.get().getTraceId(), (String)this.getPageCache().get("toGetCodeRuleTraceId"))) {
            this.changeNumber();
        }
    }

    public void click(EventObject evt) {
        super.click(evt);
        Control source = (Control)evt.getSource();
        String operationKey = source.getKey();
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        JobLevelGradeRangeUtil.getInstance().click(model, view, operationKey, (IFormPlugin)this);
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId = closedCallBackEvent.getActionId();
        Map returnData = (Map)closedCallBackEvent.getReturnData();
        IDataModel model = this.getModel();
        JobLevelGradeRangeUtil.getInstance().closedCallBack(model, returnData, actionId);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate operate = (AbstractOperate)args.getSource();
        operate.getOption().setVariableValue("isFromPage", "1");
        String operateKey = operate.getOperateKey();
        String confirmShow = this.getPageCache().get("confirmShow");
        FormOperate formOperate = (FormOperate)args.getSource();
        formOperate.getOption().setVariableValue("appId", this.getView().getFormShowParameter().getCheckRightAppId());
        if (!HRStringUtils.equals((String)confirmShow, (String)"1") && (HRStringUtils.equals((String)operateKey, (String)"confirmchangenoaudit") || HRStringUtils.equals((String)operateKey, (String)"save") || HRStringUtils.equals((String)operateKey, (String)"confirmchange")) && !this.getModel().getDataChanged() && this.isAttachUnChanged()) {
            if (HRStringUtils.equals((String)operateKey, (String)"confirmchangenoaudit")) {
                this.getPageCache().put("confirmShow", "1");
            }
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u65e0\u4fe1\u606f\u53d8\u66f4\uff0c\u8bf7\u786e\u8ba4\u3002", (String)"StandardPositionEdit_3", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
        if (HRStringUtils.equals((String)"modify", (String)operateKey)) {
            this.getView().setStatus(OperationStatus.EDIT);
            this.getView().setEnable(Boolean.FALSE, new String[]{"bar_modify"});
            this.getView().setEnable(Boolean.TRUE, new String[]{"bar_save"});
            this.getView().setVisible(Boolean.TRUE, new String[]{"bsed"});
        }
        if (HRStringUtils.equals((String)"insertdata_his", (String)operateKey)) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"bsed"});
        }
        if (HRStringUtils.equals((String)"change", (String)operateKey)) {
            this.getView().setEnable(Boolean.TRUE, new String[]{"adminorg"});
            this.getView().setVisible(Boolean.TRUE, new String[]{"bsed"});
        }
    }

    private String getAttachUid() {
        AttachmentPanel attachmentPanel = (AttachmentPanel)this.getView().getControl("attachmentpanelap");
        List uid = attachmentPanel.getAttachmentData().stream().map(map -> map.get(UID).toString() + map.get("name") + map.get("description")).collect(Collectors.toList());
        return String.join((CharSequence)",", uid);
    }

    private void updateUidIntoCache() {
        this.getView().getPageCache().put(UID, this.getAttachUid());
    }

    private boolean isAttachUnChanged() {
        String oldUid = this.getView().getPageCache().get(UID);
        return HRStringUtils.equals((String)oldUid, (String)this.getAttachUid());
    }

    public void afterImportData(ImportDataEventArgs e) {
        Map sourceData = e.getSourceData();
        DynamicObject ob = this.getModel().getDataEntity();
        if (!(ob == null || sourceData != null && sourceData.containsKey("bsed"))) {
            ob.set("bsed", (Object)HRDateTimeUtils.truncateDate((Date)new Date()));
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        String operateKey = afterDoOperationEventArgs.getOperateKey();
        if (("bar_confirmchange".equals(operateKey) || "confirmchangenoaudit".equals(operateKey) || HRStringUtils.equals((String)operateKey, (String)"save")) && afterDoOperationEventArgs.getOperationResult() != null && afterDoOperationEventArgs.getOperationResult().isSuccess()) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"insertdatabtn", "hisversionbtn", "bar_revise", "bsed"});
            this.getView().updateView("tbmain");
            this.getView().setEnable(Boolean.FALSE, new String[]{"adminorg"});
            Container toolBar = (Container)this.getView().getControl("tbmain");
            toolBar.deleteControls(new String[]{"insertdatabtn", "bar_confirmchange", "confirmchangenoaudit", "versionchangecomparebtn", "deletehisbtn", "bar_hisinfo"});
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        if (evt.getOperationKey().equals("confirmchange")) {
            if (!this.getModel().getDataChanged() && this.isAttachUnChanged()) {
                this.getPageCache().put("skipChangeTips", "true");
            } else {
                this.getPageCache().put("skipChangeTips", "false");
            }
        }
    }

    public void pageRelease(EventObject e) {
        if (this.isFromAdd()) {
            this.recycleNumber();
        }
    }

    private boolean isFromAdd() {
        if (!OperationStatus.ADDNEW.equals((Object)this.getView().getFormShowParameter().getStatus())) {
            return false;
        }
        return !HRStringUtils.equals((String)((String)this.getView().getFormShowParameter().getCustomParam("hisPage")), (String)HisPageEnum.CHANGE_PAGE.getPage());
    }

    private void recycleNumber() {
        Map<String, Object> recycleMap = this.getRecycleCache();
        if (recycleMap == null) {
            return;
        }
        DynamicObject billEntity = new HRBaseServiceHelper("hbpm_stposition").generateEmptyDynamicObject();
        recycleMap.forEach((field, value) -> {
            if (value != null) {
                billEntity.set(field, recycleMap.get(field));
            }
        });
        PositionCodeRuleHelper.recycleNumber((DynamicObject[])new DynamicObject[]{billEntity});
        this.clearRecycleCache();
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

    private Set<String> getNumberRuleAllFieldSet() {
        HashSet<String> numberRuleFieldList = new HashSet<String>();
        numberRuleFieldList.add("org");
        numberRuleFieldList.add("adminorg");
        numberRuleFieldList.add("job");
        String numberRuleExtField = this.getPageCache().get(NUMBERRULE_EXTFIELD);
        if (HRStringUtils.isNotEmpty((String)numberRuleExtField)) {
            Set extFields = (Set)SerializationUtils.fromJsonString((String)numberRuleExtField, Set.class);
            numberRuleFieldList.addAll(extFields);
        }
        return numberRuleFieldList;
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

    private boolean codeRuleExist() {
        DynamicObject dataEntity = this.getModel().getDataEntity();
        DynamicObject org = dataEntity.getDynamicObject("org");
        String orgValue = null;
        if (Objects.nonNull(org)) {
            orgValue = org.getString("id");
        }
        return CodeRuleServiceHelper.isExist((String)"hbpm_stposition", (DynamicObject)dataEntity, (String)orgValue);
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
        String number = CodeRuleServiceHelper.readNumber((String)"hbpm_stposition", (DynamicObject)dataEntity, (String)orgValue);
        this.getModel().setValue("number", (Object)number);
        this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
        this.setRecycleCache();
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
}

