/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.AnchorItems
 *  kd.bos.entity.datamodel.AbstractFormDataModel
 *  kd.bos.entity.datamodel.TableValueSetter
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.exception.ErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.ext.form.control.AnchorControl
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgBatchBillHelper
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgStructRepository
 *  kd.hr.haos.business.domain.org.service.fullname.OrgFullNameServiceWrapper
 *  kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository
 *  kd.hr.haos.business.util.PropertyGetUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.model.AuthorizedStructResult
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.haos.formplugin.web.adminorg;

import com.google.common.collect.Maps;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.AnchorItems;
import kd.bos.entity.datamodel.AbstractFormDataModel;
import kd.bos.entity.datamodel.TableValueSetter;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.ext.form.control.AnchorControl;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgBatchBillHelper;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgStructRepository;
import kd.hr.haos.business.domain.org.service.fullname.OrgFullNameServiceWrapper;
import kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository;
import kd.hr.haos.business.util.PropertyGetUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.model.AuthorizedStructResult;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class NewAdminorgDetailEditPlugin
extends HRDataBaseEdit {
    private static final Log LOG = LogFactory.getLog(NewAdminorgDetailEditPlugin.class);
    private final String TOBEDISABLEFLAG = "tobedisableflag";
    private final String BSED = "bsed";
    private final String TOBEDISABLEDATE = "tobedisabledate";
    private final String DISABLEDATE = "disabledate";
    private final String SYSTEM_TYPE = "hrmp-haos-formplugin";
    private static Log logger = LogFactory.getLog(NewAdminorgDetailEditPlugin.class);
    private static HRBaseServiceHelper ADMIN_ORG_TYPE = new HRBaseServiceHelper("haos_adminorgdetail");
    private static final String[] DATA_STATUS_SHC_EFF_INV = new String[]{HisModelDataStatusEnum.TO_BE_EFFECT.getStatus(), HisModelDataStatusEnum.EFFECTING.getStatus()};
    private static final String[] DATA_STATUS_EFF_INV = new String[]{HisModelDataStatusEnum.EFFECTING.getStatus()};

    public void afterBindData(EventObject eventObject) {
        super.afterBindData(eventObject);
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        Object operation = formShowParameter.getCustomParam("adminorg_operation");
        if (this.isFromFuture(formShowParameter) || operation != null) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"topinfo", "rightflexpanelap", "anchorcontrolap"});
            this.setChangeScenes();
        } else {
            this.showTop();
        }
        if (OperationStatus.VIEW.equals((Object)formShowParameter.getStatus())) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"orginfo", "change_info"});
        }
        this.getView().setEnable(Boolean.valueOf(false), new String[]{"attachmeflex"});
        DynamicObject dataEntity = this.getModel().getDataEntity();
        this.getModel().setValue("deptlongname", (Object)"-");
        if (operation == null) {
            this.showDate(dataEntity);
        }
        this.handleOrgType();
        OrgFullNameServiceWrapper fullNameServiceWrapper = new OrgFullNameServiceWrapper();
        String orgFullName = fullNameServiceWrapper.getOrgFullName(Long.valueOf(dataEntity.getLong("boid")), this.getSearchDate());
        this.getModel().setValue("fullname", (Object)orgFullName);
        this.showStructEntry();
        this.showTips();
        this.showCooprel();
        this.getModel().setDataChanged(Boolean.FALSE.booleanValue());
        String opMsg = (String)formShowParameter.getCustomParam("op_msg");
        if (HRStringUtils.isNotEmpty((String)opMsg)) {
            formShowParameter.setCustomParam("op_msg", null);
            this.getView().showSuccessNotification(opMsg);
        }
    }

    private void setChangeScenes() {
        if (this.getModel().getValue("org") == null) {
            return;
        }
        DynamicObject changeSence = this.getModel().getDataEntity().getDynamicObject("changescene");
        if (changeSence != null) {
            return;
        }
        Long orgId = ((DynamicObject)this.getModel().getValue("org")).getLong("id");
        Long changeTypeId = this.getModel().getDataEntity().getLong("changetype.id");
        List changeLists = AdminOrgDetailHelper.getChagneScene((Long)changeTypeId, (Long)orgId);
        if (changeLists.size() != 1) {
            return;
        }
        DynamicObject changeScene = (DynamicObject)changeLists.get(0);
        this.getModel().setValue("changescene", (Object)changeScene);
        DynamicObject[] changeReasons = AdminOrgDetailHelper.getChangeReason((DynamicObject)changeScene, (Long)orgId);
        if (changeReasons.length == 1) {
            this.getModel().setValue("changereason", (Object)changeReasons[0]);
        }
    }

    private void showCooprel() {
        long boId = this.getModel().getDataEntity().getLong("boid");
        Date bsed = this.getModel().getDataEntity().getDate("bsed");
        DynamicObject[] cooprels = new AdminOrgDetailHelper().getCooprelByOrgId(Long.valueOf(boId), bsed);
        if (cooprels == null || cooprels.length == 0) {
            return;
        }
        DynamicObjectCollection cooprelCol = this.getModel().getDataEntity().getDynamicObjectCollection("cooprelentryentity");
        cooprelCol.clear();
        for (DynamicObject cooprel : cooprels) {
            long orgId = PropertyGetUtils.getDyBdPropId((DynamicObject)cooprel, (String)"cooporgteam");
            if (orgId == 0L) continue;
            DynamicObject cooprelEntry = cooprelCol.addNew();
            cooprelEntry.set("coopreltype", cooprel.get("coopreltype"));
            cooprelEntry.set("cooporgteam", cooprel.get("cooporgteam"));
            cooprelEntry.getDataEntityState().setPushChanged(true);
        }
        this.getView().updateView("cooprelentryentity");
        this.getModel().setDataChanged(false);
    }

    private void showTips() {
        HRBaseServiceHelper futureRecordHelper = new HRBaseServiceHelper("haos_futuretips_record");
        boolean isExist = futureRecordHelper.isExists(new QFilter("sysuser", "=", (Object)RequestContext.get().getCurrUserId()));
        if (HRStringUtils.equals((String)((String)this.getView().getFormShowParameter().getCustomParam("isShowScheduleEffect")), (String)"1") && HRStringUtils.equals((String)((String)this.getView().getFormShowParameter().getCustomParam("isfuturetips")), (String)"1") && !isExist) {
            FormShowParameter parameter = new FormShowParameter();
            parameter.setShowTitle(false);
            parameter.setFormId("haos_futuretipsshow");
            parameter.setStatus(OperationStatus.EDIT);
            parameter.getOpenStyle().setShowType(ShowType.Modal);
            this.getView().showForm(parameter);
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        String operateKey = args.getOperateKey();
        if (!(HRStringUtils.equals((String)operateKey, (String)"confirmchange") || HRStringUtils.equals((String)operateKey, (String)"confirmchangenoaudit") || HRStringUtils.equals((String)operateKey, (String)"save"))) {
            return;
        }
        OperationResult operationResult = args.getOperationResult();
        if (operationResult == null || !operationResult.isSuccess()) {
            return;
        }
        if (HRDateTimeUtils.dayAfter((Date)this.getModel().getDataEntity().getDate("bsed"), (Date)new Date())) {
            boolean haveFuturePageViewPerm = AdminOrgDetailHelper.haveFuturePageViewPerm((IFormView)this.getView());
            String opDescription = HRStringUtils.equals((String)operateKey, (String)"save") ? ResManager.loadKDString((String)"\u4fdd\u5b58\u6210\u529f\u3002", (String)"NewAdminorgDetailEditPlugin_28", (String)"hrmp-haos-formplugin", (Object[])new Object[0]) : ResManager.loadKDString((String)"\u786e\u8ba4\u53d8\u66f4\u6210\u529f\u3002", (String)"NewAdminorgDetailEditPlugin_29", (String)"hrmp-haos-formplugin", (Object[])new Object[0]);
            IFormView parentView = this.getView().getParentView();
            if (parentView != null) {
                parentView.showSuccessNotification(opDescription);
                this.getView().sendFormAction(parentView);
            }
            if (!haveFuturePageViewPerm) {
                this.getView().close();
                return;
            }
            HRBaseServiceHelper adminOrgServiceHelper = new HRBaseServiceHelper("haos_adminorgdetail");
            QFilter idFilter = new QFilter("boid", "=", (Object)this.getModel().getDataEntity().getLong("id"));
            idFilter.or(new QFilter("id", "=", (Object)this.getModel().getDataEntity().getLong("id")));
            QFilter dataFilter = new QFilter("datastatus", "=", (Object)HisModelDataStatusEnum.TO_BE_EFFECT.getStatus());
            dataFilter.and(new QFilter("iscurrentversion", "=", (Object)"0"));
            DynamicObject futureVersion = adminOrgServiceHelper.loadOne("id, name", new QFilter[]{idFilter, dataFilter});
            new AdminOrgDetailHelper().showFutureViewPage((AbstractFormPlugin)this, futureVersion.getLong("id"), futureVersion.getString("name"), opDescription);
            this.getView().close();
        } else if (HRStringUtils.equals((String)operateKey, (String)"save")) {
            this.getView().invokeOperation("refresh");
        }
    }

    public void afterLoadData(EventObject event) {
        Object operation = this.getView().getFormShowParameter().getCustomParam("adminorg_operation");
        if (operation != null) {
            return;
        }
        this.getView().setStatus(OperationStatus.VIEW);
    }

    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        String name = e.getProperty().getName();
        if ("bsed".equals(name)) {
            this.getModel().setValue("effectdate", this.getModel().getValue("bsed"));
        }
    }

    private void showTop() {
        this.getView().setVisible(Boolean.valueOf(true), new String[]{"topinfo"});
        FormShowParameter formShowParameter = new FormShowParameter();
        formShowParameter.getOpenStyle().setTargetKey("topinfo");
        formShowParameter.setCustomParam("custom_parent_f7_prop", (Object)"boid");
        formShowParameter.setFormId("haos_orgdetailtopinfo");
        formShowParameter.setCustomParam("structproject", this.getView().getFormShowParameter().getCustomParam("structproject"));
        formShowParameter.setCustomParam("visableChange", (Object)"false");
        formShowParameter.getOpenStyle().setShowType(ShowType.InContainer);
        DynamicObject dataEntity = this.getModel().getDataEntity();
        String boId = dataEntity.getString("boid");
        if ("0".equals(boId)) {
            boId = (String)this.getView().getFormShowParameter().getCustomParam("currentObjectPKId");
        }
        formShowParameter.setCustomParam("currentObjectPKId", (Object)boId);
        formShowParameter.setStatus(OperationStatus.VIEW);
        formShowParameter.setCustomParam("searchdate", (Object)this.getSearchDate());
        Date effectDate = (Date)this.getModel().getValue("bsed");
        formShowParameter.setCustomParam("effectDate", (Object)HRDateTimeUtils.getTrancateDateFromDate((Date)effectDate));
        this.getView().showForm(formShowParameter);
    }

    private Date getSearchDate() {
        if (this.isFromFuture(this.getView().getFormShowParameter())) {
            return this.getModel().getDataEntity().getDate("bsed");
        }
        Object searchDateOb = this.getView().getFormShowParameter().getCustomParams().get("searchdate");
        Date searchDate = new Date();
        if (null != searchDateOb) {
            try {
                searchDate = searchDateOb instanceof String ? HRDateTimeUtils.parseDate((String)((String)searchDateOb)) : (Date)searchDateOb;
            }
            catch (ParseException parseException) {
                logger.error((Throwable)parseException);
                throw new KDBizException(new ErrorCode("NewAdminorgDetailEditPlugin", parseException.getMessage()), new Object[0]);
            }
        }
        return searchDate;
    }

    private QFilter buildHisFilter(String property, Object ... id) {
        Date searchDate = this.getSearchDate();
        QFilter filter = new QFilter(property, "in", (Object)id);
        if (searchDate == null) {
            filter.and(new QFilter("iscurrentversion", "in", (Object)"1"));
        } else {
            filter.and(new QFilter("bsed", "<=", (Object)searchDate));
            filter.and(new QFilter("bsled", ">=", (Object)searchDate));
            String isShowScheduleEffect = (String)this.getView().getFormShowParameter().getCustomParam("isShowScheduleEffect");
            String[] dataStatusArr = !HRStringUtils.isEmpty((String)isShowScheduleEffect) && "1".equals(isShowScheduleEffect) ? DATA_STATUS_SHC_EFF_INV : DATA_STATUS_EFF_INV;
            QFilter statusFilter = new QFilter("datastatus", "in", (Object)dataStatusArr);
            filter.and(statusFilter);
            filter.and("iscurrentversion", "=", (Object)"0");
        }
        return filter;
    }

    private void showDate(DynamicObject dataEntity) {
        Object toBeDisableFlag = dataEntity.get("tobedisableflag");
        boolean flag = toBeDisableFlag == null ? Boolean.FALSE : (Boolean)toBeDisableFlag;
        String enable = dataEntity.getString("enable");
        this.getView().setEnable(Boolean.FALSE, new String[]{"bsed", "tobedisableflag", "tobedisabledate", "disabledate"});
        this.getView().setVisible(Boolean.FALSE, new String[]{"bsed", "effectdate", "enable", "tobedisableflag", "tobedisabledate", "disabledate"});
        if (enable.equals("1")) {
            if (!flag) {
                this.getView().setVisible(Boolean.TRUE, new String[]{"effectdate", "enable"});
            } else {
                this.getView().setVisible(Boolean.TRUE, new String[]{"tobedisableflag", "tobedisabledate"});
            }
        } else {
            this.getView().setVisible(Boolean.TRUE, new String[]{"disabledate", "enable"});
        }
    }

    private void handleOrgType() {
        AnchorControl anchorCtl = (AnchorControl)this.getView().getControl("anchorcontrolap");
        AnchorItems baseItem = new AnchorItems("baseinfoflex", ResManager.loadKDString((String)"\u57fa\u672c\u4fe1\u606f", (String)"NewAdminorgDetailEditPlugin_20", (String)"hrmp-haos-formplugin", (Object[])new Object[0]), null);
        ArrayList<AnchorItems> anchorItems = new ArrayList<AnchorItems>();
        anchorItems.add(baseItem);
        AnchorItems orgStatusItem = new AnchorItems("statusinfoflex", ResManager.loadKDString((String)"\u7ec4\u7ec7\u72b6\u6001", (String)"NewAdminorgDetailEditPlugin_27", (String)"hrmp-haos-formplugin", (Object[])new Object[0]), null);
        anchorItems.add(orgStatusItem);
        AnchorItems orgDutyItem = new AnchorItems("orgdutyflex", ResManager.loadKDString((String)"\u7ec4\u7ec7\u804c\u8d23", (String)"NewAdminorgDetailEditPlugin_24", (String)"hrmp-haos-formplugin", (Object[])new Object[0]), null);
        anchorItems.add(orgDutyItem);
        if (this.getView().getEntityId().equals("haos_adminorgdetail")) {
            AnchorItems coopItem = new AnchorItems("collaborationflex", ResManager.loadKDString((String)"\u534f\u4f5c\u5173\u7cfb", (String)"NewAdminorgDetailEditPlugin_26", (String)"hrmp-haos-formplugin", (Object[])new Object[0]), null);
            anchorItems.add(coopItem);
        }
        anchorCtl.addItems(anchorItems);
        anchorCtl.setHighlight(true);
    }

    private void showStructEntry() {
        Long userId = RequestContext.get().getCurrUserId();
        String appId = this.getView().getFormShowParameter().getCheckRightAppId();
        AuthorizedStructResult permResult = (AuthorizedStructResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getUserStructProjectsF7", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", "47150e89000000ac", "struct_parent_org", null});
        LOG.info("showStructEntry_permResult_{}", (Object)permResult);
        DynamicObject[] structProjectArr = permResult.isHasAllStruct() ? StructProjectRepository.getInstance().queryAllStructArrBySyncorg() : StructProjectRepository.getInstance().queryAllStructArrBySyncorg(permResult.getAuthorizedStructs(), "id");
        if (structProjectArr.length > 0) {
            DynamicObject[] structDys;
            this.getView().setVisible(Boolean.TRUE, new String[]{"struct_panelap"});
            HashMap orgStructMap = Maps.newHashMapWithExpectedSize((int)16);
            long boId = this.getModel().getDataEntity().getLong("boid");
            for (DynamicObject orgStructDy : structDys = OrgStructRepository.getInstance().queryOriginalOrgHisDateStructDys(null, this.getSearchDate(), Long.valueOf(boId))) {
                orgStructMap.put(orgStructDy.getLong("structproject.id"), orgStructDy);
            }
            AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
            model.beginInit();
            TableValueSetter vs = new TableValueSetter(new String[0]);
            vs.addField("struct_project", new Object[0]);
            vs.addField("struct_parent_org", new Object[0]);
            vs.addField("struct_long_name", new Object[0]);
            boolean isView = OperationStatus.VIEW.equals((Object)this.getView().getFormShowParameter().getStatus());
            for (DynamicObject structProjectDy : structProjectArr) {
                if (orgStructMap.containsKey(structProjectDy.getLong("id"))) {
                    DynamicObject structDy = (DynamicObject)orgStructMap.get(structProjectDy.getLong("id"));
                    long parentId = structDy.getLong("parentorg.id");
                    if (isView && parentId == 0L) continue;
                    vs.addRow(new Object[]{structProjectDy.getLong("id"), parentId, OrgBatchBillHelper.getOrgLongName((Long)parentId, (Date)this.getSearchDate(), (String)structProjectDy.getString("id"))});
                    continue;
                }
                if (isView) continue;
                vs.addRow(new Object[]{structProjectDy.getLong("id"), null, ""});
            }
            if (vs.getCount() == 0) {
                this.getView().setVisible(Boolean.FALSE, new String[]{"struct_panelap"});
            } else {
                model.batchCreateNewEntryRow("struct_project_entry", vs);
                model.endInit();
                this.getView().updateView("struct_project_entry");
            }
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{"struct_panelap"});
        }
    }

    private boolean isFromFuture(FormShowParameter formShowParameter) {
        return formShowParameter.getCustomParam("isShowScheduleEffect") != null && formShowParameter.getCustomParam("isShowScheduleEffect").equals("1");
    }
}
