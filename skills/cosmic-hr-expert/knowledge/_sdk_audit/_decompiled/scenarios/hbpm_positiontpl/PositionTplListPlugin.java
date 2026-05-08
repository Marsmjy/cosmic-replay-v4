/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.filter.ControlFilter
 *  kd.bos.entity.filter.ControlFilters
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.operate.FormOperate
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.events.BeforeShowBillFormEvent
 *  kd.bos.log.api.AppLogInfo
 *  kd.bos.log.api.ILogService
 *  kd.bos.log.api.OpLogAppInfo
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.service.ServiceFactory
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseList
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplRepository
 *  kd.hrmp.hbpm.business.infrastructure.client.perm.PositionPermissionUtil
 *  kd.hrmp.hbpm.business.utils.SystemParamHelper
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hrmp.hbpm.formplugin.web.position;

import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.filter.ControlFilter;
import kd.bos.entity.filter.ControlFilters;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.events.BeforeShowBillFormEvent;
import kd.bos.log.api.AppLogInfo;
import kd.bos.log.api.ILogService;
import kd.bos.log.api.OpLogAppInfo;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.service.ServiceFactory;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseList;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplRepository;
import kd.hrmp.hbpm.business.infrastructure.client.perm.PositionPermissionUtil;
import kd.hrmp.hbpm.business.utils.SystemParamHelper;
import org.apache.commons.lang3.StringUtils;

public final class PositionTplListPlugin
extends HRDataBaseList {
    private static final Log LOGGER = LogFactory.getLog(PositionTplListPlugin.class);
    private static final String CLOSECALLBACKKEY_TPL_AS_EDIT = "tpl_applicationscope_edit";

    public void click(EventObject evt) {
        super.click(evt);
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        DynamicObject[] tplDys;
        ListSelectedRowCollection listSelectedData = this.getSelectedRows();
        Object[] primaryKeyValues = listSelectedData.getPrimaryKeyValues();
        if ("applicationscope".equals(evt.getItemKey()) && (tplDys = PositionTplRepository.getInstance().queryDisablePositionTplByIds(primaryKeyValues)) != null && tplDys.length > 0) {
            String errorMsg = ResManager.loadKDString((String)"\u7981\u7528\u6570\u636e\u4e0d\u53ef\u8bbe\u7f6e\u9002\u7528\u7ec4\u7ec7\u8303\u56f4\u3002", (String)"PositionTplListPlugin_3", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
            this.getView().showTipNotification(errorMsg);
            String confirm = ResManager.loadKDString((String)"\u8bbe\u7f6e\u9002\u7528\u8303\u56f4", (String)"PositionTplListPlugin_4", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
            this.addOperateLog(confirm, errorMsg);
            evt.setCancel(true);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        switch (operateKey = operate.getOperateKey()) {
            case "new": {
                String validateResult = this.validateOrgExistOpenTpl();
                if (!validateResult.isEmpty()) {
                    this.getView().showTipNotification(validateResult);
                    args.setCancel(true);
                    return;
                }
                this.showStructProjectForm();
                args.setCancel(true);
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        OperationResult operationResult;
        super.afterDoOperation(afterDoOperationEventArgs);
        String operateKey = afterDoOperationEventArgs.getOperateKey();
        if ("donothing_setscope".equals(operateKey) && (operationResult = afterDoOperationEventArgs.getOperationResult()) != null && operationResult.isSuccess()) {
            ListSelectedRowCollection listSelectedData = this.getSelectedRows();
            Object[] primaryKeyValues = listSelectedData.getPrimaryKeyValues();
            FormShowParameter formShowParameter = new FormShowParameter();
            formShowParameter.setFormId("hbpm_applicationscope");
            formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
            formShowParameter.setCustomParam("positiontplId", (Object)primaryKeyValues);
            formShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, CLOSECALLBACKKEY_TPL_AS_EDIT));
            this.getView().showForm(formShowParameter);
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        if (HRStringUtils.equals((String)evt.getActionId(), (String)CLOSECALLBACKKEY_TPL_AS_EDIT) && evt.getReturnData() != null) {
            this.getView().invokeOperation("refresh");
        } else if (HRStringUtils.equals((String)evt.getActionId(), (String)"new")) {
            this.getView().invokeOperation("refresh");
        }
    }

    public void beforeShowBill(BeforeShowBillFormEvent e) {
        BillShowParameter parameter = e.getParameter();
        if (Objects.nonNull(parameter.getPkId()) && StringUtils.equals((CharSequence)"hbpm_positiontpl", (CharSequence)parameter.getFormId())) {
            parameter.setStatus(OperationStatus.VIEW);
            parameter.setBillStatus(BillOperationStatus.VIEW);
        }
    }

    private void showStructProjectForm() {
        List ids;
        BaseShowParameter baseShowParameter = new BaseShowParameter();
        baseShowParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        baseShowParameter.setFormId("hbpm_positiontpl");
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

    private String validateOrgExistOpenTpl() {
        HasPermOrgResult hasPermAdminOrg = PositionPermissionUtil.getHasPermAdminOrg((long)RequestContext.get().getCurrUserId(), (String)"21", (String)"homs", (String)"hbpm_positiontpl", (String)"47156aff000000ac");
        LOGGER.info("validateOrgExistOpenTpl hasPermAdminOrg info : {}", (Object)hasPermAdminOrg.toString());
        boolean hasAllOrgPerm = hasPermAdminOrg.hasAllOrgPerm();
        if (hasAllOrgPerm) {
            HRBaseServiceHelper hrBaseServiceHelper = new HRBaseServiceHelper("bos_svc_sysparameter");
            QFilter qFilter = new QFilter("fviewtypeid", "=", (Object)"21");
            DynamicObject[] query = hrBaseServiceHelper.query("fviewtypeid,fdata,forgid", qFilter.toArray());
            List hasPerm = Arrays.asList(query).stream().map(temp -> JSONObject.parseObject((String)temp.getString("fdata"))).collect(Collectors.toList()).stream().filter(temp -> temp.get((Object)"openpositiontpl") != null && temp.getBoolean("openpositiontpl") == true).collect(Collectors.toList());
            if (hasPerm.size() == 0) {
                return ResManager.loadKDString((String)"\u6240\u5728\u6743\u9650\u4e0b\u7ec4\u7ec7\u4f53\u7cfb\u7ba1\u7406\u7ec4\u7ec7\u5747\u672a\u542f\u7528\u6a21\u677f\u5e93\uff0c\u65e0\u6cd5\u65b0\u589e\u3002", (String)"PositionTplListPlugin_1", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
            }
        } else {
            List hasPermOrgs = hasPermAdminOrg.getHasPermOrgs();
            LOGGER.info("validateOrgExistOpenTpl hasPermOrgs info : {}", (Object)hasPermOrgs.toString());
            Map batchParameter = SystemParamHelper.getBatchParameter((List)hasPermOrgs);
            AtomicBoolean hasPerm = new AtomicBoolean(false);
            batchParameter.forEach((orgId, parameter) -> {
                if (parameter == null) {
                    return;
                }
                Boolean openTpl = parameter.getOrDefault("openpositiontpl", Boolean.FALSE);
                if (openTpl != null && openTpl.booleanValue()) {
                    hasPerm.set(true);
                    return;
                }
            });
            if (!hasPerm.get()) {
                return ResManager.loadKDString((String)"\u6240\u5728\u6743\u9650\u4e0b\u7ec4\u7ec7\u4f53\u7cfb\u7ba1\u7406\u7ec4\u7ec7\u5747\u672a\u542f\u7528\u6a21\u677f\u5e93\uff0c\u65e0\u6cd5\u65b0\u589e\u3002", (String)"PositionTplListPlugin_1", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
            }
        }
        return "";
    }

    private void addOperateLog(String opName, String opDesc) {
        ILogService service = (ILogService)ServiceFactory.getService(ILogService.class);
        OpLogAppInfo appLogInfo = new OpLogAppInfo();
        appLogInfo.setUserID(Long.valueOf(RequestContext.get().getCurrUserId()));
        appLogInfo.setBizAppID("homs");
        appLogInfo.setBizObjID("hbpm_applicationscope");
        appLogInfo.setOpName(opName);
        appLogInfo.setOpDescription(opDesc);
        appLogInfo.setStatus("1");
        service.addLog((AppLogInfo)appLogInfo);
    }
}

