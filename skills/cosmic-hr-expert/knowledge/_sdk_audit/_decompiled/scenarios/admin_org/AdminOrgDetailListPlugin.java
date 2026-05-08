/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.operate.result.OperateErrorInfo
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.validate.ValidateResult
 *  kd.bos.entity.validate.ValidateResultCollection
 *  kd.bos.exception.ErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.plugin.AbstractFormPlugin
 *  kd.bos.mvc.list.ListView
 *  kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgRepository
 *  kd.hr.haos.common.util.OpExecuteUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 */
package kd.hr.haos.formplugin.web.adminorg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.result.OperateErrorInfo;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.validate.ValidateResult;
import kd.bos.entity.validate.ValidateResultCollection;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.mvc.list.ListView;
import kd.hr.haos.business.domain.adminorg.service.impl.AdminOrgDetailHelper;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgRepository;
import kd.hr.haos.common.util.OpExecuteUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;

public class AdminOrgDetailListPlugin
extends HRDataBaseList {
    private final String enable_success = ResManager.loadKDString((String)"\u542f\u7528\u6210\u529f\u3002", (String)"AdminOrgDetailListPlugin_0", (String)"hrmp-haos-formplugin", (Object[])new Object[0]);
    private final String disable_success = ResManager.loadKDString((String)"\u505c\u7528\u6210\u529f\u3002", (String)"AdminOrgDetailListPlugin_1", (String)"hrmp-haos-formplugin", (Object[])new Object[0]);
    private final String select_one_msg = ResManager.loadKDString((String)"\u53ea\u80fd\u9009\u62e9\u4e00\u6761\u6570\u636e", (String)"AdminOrgDetailListPlugin_2", (String)"hrmp-haos-formplugin", (Object[])new Object[0]);
    private final String choose_error_msg = ResManager.loadKDString((String)"\u68c0\u6d4b\u5230\u60a8\u9009\u62e9\u7684\u7ec4\u7ec7\u6709\u6821\u9a8c\u4e0d\u901a\u8fc7\u7684\u6570\u636e\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9\u3002", (String)"AdminOrgDetailListPlugin_3", (String)"hrmp-haos-opplugin", (Object[])new Object[0]);

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        boolean rootInit = new AdminOrgDetailHelper().checkRootInit(this.getView());
        if (!rootInit) {
            args.setCancel(true);
            return;
        }
        AbstractOperate operate = (AbstractOperate)args.getSource();
        String operateKey = operate.getOperateKey();
        this.openOperationPage(operateKey, args);
    }

    private void openOperationPage(String opKey, BeforeDoOperationEventArgs args) {
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        ListView listView = (ListView)this.getView();
        ListSelectedRowCollection selectedRows = listView.getSelectedRows();
        List orgBoList = selectedRows.stream().map(org -> (Long)org.getPrimaryKeyValue()).collect(Collectors.toList());
        DynamicObject[] selectOrgDys = OrgRepository.getInstance().loadByPks(orgBoList);
        switch (opKey) {
            case "addnew": {
                new AdminOrgDetailHelper().checkAddOrg((AbstractFormPlugin)this, this.getView(), this.getPluginName());
                args.setCancel(true);
                break;
            }
            case "parentchg": {
                if (!this.checkOrgParentChg(selectOrgDys)) break;
                new AdminOrgDetailHelper().showChgParentPage((AbstractFormPlugin)this, selectOrgDys[0]);
                break;
            }
            case "infochg": {
                if (!this.checkOrgInfoChg(selectOrgDys)) break;
                new AdminOrgDetailHelper().showChangeInfoPage((AbstractFormPlugin)this, selectOrgDys[0]);
                break;
            }
            case "importdetailchargeperson": {
                new AdminOrgDetailHelper().showChargePersonImportDetail((AbstractFormPlugin)this);
                args.setCancel(true);
                break;
            }
            case "viewhischange": {
                if (!AdminOrgDetailHelper.listCheckRevise((IFormView)this.getView(), (DynamicObject[])selectOrgDys)) break;
                AdminOrgDetailHelper.showRevisePage((AbstractFormPlugin)this, (Long)((Long)orgBoList.get(0)), (String)selectOrgDys[0].getString("name"));
                break;
            }
            case "operate_steps": {
                AdminOrgDetailHelper.showOperateStepsPage((IFormView)this.getView());
                break;
            }
            case "disableorg": {
                if (this.checkOrgOnlyOpVerify("disableorg", selectOrgDys)) {
                    new AdminOrgDetailHelper().showDisableInfoPage(this.getView(), this.getPluginName(), orgBoList);
                }
                args.setCancel(true);
                break;
            }
            case "enableorg": {
                if (this.checkOrgOnlyOpVerify("enableorg", selectOrgDys)) {
                    new AdminOrgDetailHelper().showEnableInfoPage(this.getView(), this.getPluginName(), orgBoList);
                }
                args.setCancel(true);
                break;
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        String opKey = args.getOperateKey();
        FormShowParameter showParameter = new FormShowParameter();
        showParameter.getOpenStyle().setShowType(ShowType.Modal);
        ListView listView = (ListView)this.getView();
        ListSelectedRowCollection selectedRows = listView.getSelectedRows();
        List orgBoList = selectedRows.stream().map(org -> (Long)org.getPrimaryKeyValue()).collect(Collectors.toList());
        if (args.getOperationResult() == null) {
            return;
        }
        if (args.getOperationResult().isSuccess()) {
            switch (opKey) {
                case "showchargeperson": {
                    new AdminOrgDetailHelper().showDeptLeaderSet((AbstractFormPlugin)this, (Long)orgBoList.get(0));
                    break;
                }
                case "viewchargepersonlog": {
                    new AdminOrgDetailHelper().showLogPage("haos_chargepersonlog", this.getView(), orgBoList, "adminorg.id", "adminorg.org");
                    break;
                }
            }
        }
    }

    private boolean checkOrgInfoChg(DynamicObject[] dynamicObjects) {
        if (dynamicObjects.length > 1) {
            this.getView().showTipNotification(this.select_one_msg);
            return false;
        }
        return true;
    }

    private boolean checkOrgParentChg(DynamicObject[] dynamicObjects) {
        if (dynamicObjects.length > 1) {
            this.getView().showTipNotification(this.select_one_msg);
            return false;
        }
        DynamicObject orgDy = dynamicObjects[0];
        return new AdminOrgDetailHelper().checkOrgParentChg(this.getView(), orgDy);
    }

    private boolean checkOrgOnlyOpVerify(String opKey, DynamicObject[] dynamicObjects) {
        if (dynamicObjects.length < 1) {
            return false;
        }
        DynamicObject orgDy = dynamicObjects[0];
        String name = orgDy.getDataEntityType().getName();
        OperateOption operateOption = OperateOption.create();
        operateOption.setVariableValue("OP_IGNORE_SCENE_CHECK", "true");
        OperationResult operationResult = OpExecuteUtils.executeOpOnlyVerify((String)opKey, (String)name, (DynamicObject[])dynamicObjects, (OperateOption)operateOption);
        if (!operationResult.isSuccess()) {
            List validateErrors;
            ArrayList<String> tipList = new ArrayList<String>();
            FormShowParameter parameters = new FormShowParameter();
            parameters.getOpenStyle().setShowType(ShowType.Modal);
            parameters.setFormId("bos_operationresult");
            parameters.setShowTitle(false);
            parameters.setCustomParam("operateName", (Object)opKey);
            ValidateResultCollection validateResult = operationResult.getValidateResult();
            if (validateResult != null && (validateErrors = validateResult.getValidateErrors()) != null) {
                for (ValidateResult validateError : validateErrors) {
                    for (OperateErrorInfo operateErrorInfo : validateError.getAllErrorInfo()) {
                        if (operateErrorInfo == null) continue;
                        tipList.add(operateErrorInfo.getMessage());
                    }
                }
            }
            parameters.setCustomParam("errorMsg", tipList);
            parameters.setCustomParam("title", (Object)this.choose_error_msg);
            this.getView().showForm(parameters);
        }
        return operationResult.isSuccess();
    }

    public void closedCallBack(ClosedCallBackEvent event) {
        super.closedCallBack(event);
        String actionId = event.getActionId();
        if (HRStringUtils.equals((String)actionId, (String)"adminOrgDetailCloseCallBack")) {
            OperateOption operateOption = OperateOption.create();
            Object returnData = event.getReturnData();
            if (returnData instanceof Long) {
                operateOption.setVariableValue("refresh_tree_node_id", String.valueOf(returnData));
            }
            this.getView().invokeOperation("refresh", operateOption);
            return;
        }
        DynamicObject dyn = (DynamicObject)event.getReturnData();
        if (dyn == null) {
            return;
        }
        List<Long> orgIdList = this.getSelectRowId();
        try {
            if (HRStringUtils.equals((String)actionId, (String)"show_disable_callback")) {
                OperationResult operationResult = new AdminOrgDetailHelper().stopAdminOrg(orgIdList, dyn);
                this.doAfterOp(operationResult, this.disable_success);
            } else if (HRStringUtils.equals((String)actionId, (String)"show_enable_callback")) {
                OperationResult operationResult = new AdminOrgDetailHelper().enableAdminOrg(orgIdList, dyn);
                this.doAfterOp(operationResult, this.enable_success);
            }
        }
        catch (Exception exception) {
            throw new KDBizException(new ErrorCode("closedCallBack", exception.getMessage()), new Object[0]);
        }
    }

    private void doAfterOp(OperationResult operationResult, String tip) {
        if (operationResult == null) {
            return;
        }
        if (operationResult.isSuccess()) {
            this.getView().showSuccessNotification(tip);
            this.getView().invokeOperation("refresh");
            return;
        }
        StringBuilder msgBuilder = new StringBuilder();
        operationResult.getValidateResult().getValidateErrors().forEach(validateResult -> validateResult.getAllErrorInfo().forEach(operateErrorInfo -> msgBuilder.append(operateErrorInfo.getMessage())));
        if (!HRStringUtils.isEmpty((String)msgBuilder.toString())) {
            this.getView().showTipNotification(msgBuilder.toString());
        } else if (HRStringUtils.isNotEmpty((String)operationResult.getMessage())) {
            this.getView().showTipNotification(operationResult.getMessage());
        }
    }

    private List<Long> getSelectRowId() {
        ListView listView = (ListView)this.getView();
        ListSelectedRowCollection selectedRows = listView.getSelectedRows();
        return selectedRows.stream().map(org -> (Long)org.getPrimaryKeyValue()).collect(Collectors.toList());
    }
}
