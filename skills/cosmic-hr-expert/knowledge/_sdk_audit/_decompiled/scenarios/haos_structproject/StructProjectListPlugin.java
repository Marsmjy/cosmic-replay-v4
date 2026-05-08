/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.filter.ControlFilter
 *  kd.bos.entity.filter.ControlFilters
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.SetFilterEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListShowParameter
 *  kd.bos.mvc.list.ListView
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.permission.api.HasPermOrgResultImpl
 *  kd.bos.portal.util.SerializationUtils
 *  kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository
 *  kd.hr.haos.business.util.OrgPermHelper
 *  kd.hr.haos.common.constants.structproject.StructProjectConstants
 *  kd.hr.haos.common.util.protype.imp.ProType
 *  kd.hr.haos.formplugin.web.staff.service.StaffFormService
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hr.haos.formplugin.web.structures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import kd.bos.base.BaseShowParameter;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.filter.ControlFilter;
import kd.bos.entity.filter.ControlFilters;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListShowParameter;
import kd.bos.mvc.list.ListView;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.permission.api.HasPermOrgResultImpl;
import kd.bos.portal.util.SerializationUtils;
import kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository;
import kd.hr.haos.business.util.OrgPermHelper;
import kd.hr.haos.common.constants.structproject.StructProjectConstants;
import kd.hr.haos.common.util.protype.imp.ProType;
import kd.hr.haos.formplugin.web.staff.service.StaffFormService;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public final class StructProjectListPlugin
extends HRDataBaseList
implements ClickListener,
StructProjectConstants {
    private static final String ADDNEW_ORG = "addnew_org";
    private static final String HAOS_STRUCTPROJECT = "haos_structproject";
    private static final String MAINTAINFRAMEWORK = "maintainframework";
    private static final String DELETE_CONFIRM_CALL_BACK = "deleteConfirmCallBack";
    public static final String ORG_PERM_CACHE_KEY = "org_perm_result";
    private ProType<HasPermOrgResult> hasPermOrgResultProType = new ProType();
    private static final String CREATOR_HAS_PERMISSION = "creatorhaspermission";
    private static final String MESSAGE = "message";
    private static final String TITLE = "title";
    private static final String SHOW_CONFIRM = "showConfirm";

    public void setFilter(SetFilterEvent event) {
        super.setFilter(event);
        ListShowParameter listParameter = (ListShowParameter)this.getView().getFormShowParameter();
        if (HRStringUtils.isNotEmpty((String)((String)listParameter.getCustomParam("f7_admin_org_chart")))) {
            return;
        }
        if (!listParameter.isLookUp()) {
            QFilter noPreSetFilter = new QFilter("issyspreset", "=", (Object)"0");
            noPreSetFilter.or("id", "=", (Object)STRUCT_PROJECT_MANAGE);
            QFilter isCustomorgFilter = new QFilter("iscustomorg", "=", (Object)"0");
            event.getQFilters().addAll(Arrays.asList(noPreSetFilter, isCustomorgFilter));
            HasPermOrgResult permOrgResult = this.getPermOrgResult();
            if (!permOrgResult.hasAllOrgPerm()) {
                event.getQFilters().add(new QFilter("org", "in", (Object)permOrgResult.getHasPermOrgs()));
            }
        }
        event.setOrderBy("enable desc,number asc");
    }

    public void afterDoOperation(AfterDoOperationEventArgs eventArgs) {
        super.afterDoOperation(eventArgs);
        String opKey = eventArgs.getOperateKey();
        if (eventArgs.getOperationResult() != null && eventArgs.getOperationResult().isSuccess()) {
            this.openOperationPage(opKey, eventArgs);
        }
    }

    private void openOperationPage(String opKey, AfterDoOperationEventArgs eventArgs) {
        switch (opKey) {
            case "addnew_org": {
                this.showStructProjectForm();
                break;
            }
            case "maintainframework": {
                this.showMaintainFrameworkForm(eventArgs);
                break;
            }
            case "delete_project": {
                this.afterDeleteOperation(eventArgs);
            }
        }
    }

    private void showStructProjectForm() {
        List ids;
        BaseShowParameter baseShowParameter = new BaseShowParameter();
        baseShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        baseShowParameter.setFormId(HAOS_STRUCTPROJECT);
        ControlFilters controlFilters = this.getControlFilters();
        Map filters = controlFilters.getFilters();
        ControlFilter orgFilter = (ControlFilter)filters.get("org.id");
        String orgId = "";
        if (orgFilter != null && (ids = orgFilter.getValue()).size() == 1) {
            orgId = (String)ids.get(0);
        }
        baseShowParameter.setCustomParam("orgId", (Object)orgId);
        baseShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "new"));
        this.getView().showForm((FormShowParameter)baseShowParameter);
    }

    private void showMaintainFrameworkForm(AfterDoOperationEventArgs eventArgs) {
        OperationResult operationResult = eventArgs.getOperationResult();
        if (operationResult == null || CollectionUtils.isEmpty((Collection)operationResult.getSuccessPkIds())) {
            return;
        }
        String primaryKeyValue = String.valueOf(operationResult.getSuccessPkIds().get(0));
        ListShowParameter listShowParameter = new ListShowParameter();
        listShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        listShowParameter.setFormId("haos_orgstructlist");
        listShowParameter.setBillFormId("haos_structorgdetail");
        listShowParameter.setCustomParam("custom_parent_f7_prop", (Object)"boid");
        listShowParameter.setCustomParam("struct_project_ids", (Object)SerializationUtils.toJsonString(Collections.singletonList(primaryKeyValue)));
        String mainPageId = "";
        if (this.getView().getMainView() != null) {
            mainPageId = this.getView().getMainView().getPageId();
        }
        listShowParameter.setPageId("haos_orgstructlist_" + primaryKeyValue + "_" + mainPageId);
        DynamicObject structProjectDyn = StructProjectRepository.getInstance().queryByPk("id,name,rootorg.id", (Object)Long.valueOf(primaryKeyValue));
        if (structProjectDyn != null) {
            listShowParameter.setCaption(structProjectDyn.getString("name"));
            listShowParameter.setCustomParam("rootorg", (Object)structProjectDyn.getString("rootorg.id"));
        }
        if ("1040".equals(primaryKeyValue)) {
            listShowParameter.setCustomParam("struct_project_is_to_all_areas", (Object)Boolean.TRUE.toString());
        }
        this.getView().showForm((FormShowParameter)listShowParameter);
    }

    private void afterDeleteOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        FormOperate formOperate = (FormOperate)afterDoOperationEventArgs.getSource();
        OperateOption option = formOperate.getOption();
        if (option.containsVariable(TITLE) && option.containsVariable(MESSAGE) && option.containsVariable(SHOW_CONFIRM)) {
            String title = option.getVariableValue(TITLE);
            String message = option.getVariableValue(MESSAGE);
            String showConfirm = option.getVariableValue(SHOW_CONFIRM);
            if (HRStringUtils.equals((String)showConfirm, (String)String.valueOf(true))) {
                this.getView().showTipNotification(message);
                this.getView().invokeOperation("refresh");
            } else {
                CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, DELETE_CONFIRM_CALL_BACK);
                StaffFormService.create((IFormView)this.getView()).showOperationResultPage(title, message, closeCallBack);
            }
        } else {
            this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f", (String)"StructListPlugin_1", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
            this.getView().invokeOperation("refresh");
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        String actionId = closedCallBackEvent.getActionId();
        if (DELETE_CONFIRM_CALL_BACK.equals(actionId)) {
            this.getView().invokeOperation("refresh");
        }
    }

    private HasPermOrgResult getPermOrgResult() {
        return (HasPermOrgResult)this.hasPermOrgResultProType.get(() -> {
            String orgPermString = this.getPageCache().get(ORG_PERM_CACHE_KEY);
            if (orgPermString == null) {
                HasPermOrgResult hasPermOrgResult = OrgPermHelper.getHRPermOrg((String)((ListView)this.getView()).getBillFormId());
                this.getPageCache().put(ORG_PERM_CACHE_KEY, ((HasPermOrgResultImpl)hasPermOrgResult).toSerializeStr());
                return hasPermOrgResult;
            }
            HasPermOrgResult hasPermOrgResult = HasPermOrgResultImpl.fromSerializeStr((String)orgPermString);
            if (!hasPermOrgResult.hasAllOrgPerm()) {
                List hasPermOrgs = hasPermOrgResult.getHasPermOrgs();
                for (int i = 0; i < hasPermOrgs.size(); ++i) {
                    hasPermOrgs.set(i, Long.parseLong(hasPermOrgs.get(i) + ""));
                }
            }
            return hasPermOrgResult;
        });
    }
}
