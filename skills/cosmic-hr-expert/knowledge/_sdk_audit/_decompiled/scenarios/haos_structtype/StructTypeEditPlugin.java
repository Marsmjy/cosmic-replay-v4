/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.PreOpenFormEventArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.hr.haos.business.domain.otherstruct.helper.StructTypeHelper
 *  kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService
 *  kd.hr.haos.business.util.OrgPermHelper
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.haos.formplugin.web.otherstruct.structtype;

import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.permission.api.HasPermOrgResult;
import kd.hr.haos.business.domain.otherstruct.helper.StructTypeHelper;
import kd.hr.haos.business.domain.otherstruct.service.OtherStructTypeService;
import kd.hr.haos.business.util.OrgPermHelper;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class StructTypeEditPlugin
extends HRDataBaseEdit {
    private static final Log LOG = LogFactory.getLog(StructTypeEditPlugin.class);

    public void preOpenForm(PreOpenFormEventArgs e) {
        Object pkId;
        OtherStructTypeService serviceHelper;
        DynamicObject structTypeDataById;
        super.preOpenForm(e);
        FormShowParameter formShowParameter = e.getFormShowParameter();
        if (OperationStatus.ADDNEW.equals((Object)formShowParameter.getStatus())) {
            formShowParameter.setCaption(ResManager.loadKDString((String)"\u65b0\u589e\u67b6\u6784\u7c7b\u578b", (String)"StructTypeEditPlugin_0", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
        } else if (OperationStatus.EDIT.equals((Object)formShowParameter.getStatus()) && (structTypeDataById = (serviceHelper = new OtherStructTypeService()).getStructTypeDataById(pkId = ((BaseShowParameter)formShowParameter).getPkId(), "name")) != null) {
            formShowParameter.setCaption(ResManager.loadKDString((String)"\u67b6\u6784\u7c7b\u578b-%s", (String)"StructTypeEditPlugin_1", (String)"hrmp-haos-formplugin", (Object[])new Object[]{structTypeDataById.getString("name")}));
        }
    }

    public void afterCreateNewData(EventObject e) {
        Object org = this.getModel().getValue("org");
        if (!ObjectUtils.isEmpty((Object)org)) {
            return;
        }
        Object orgId = this.getView().getFormShowParameter().getCustomParam("SELECT_ORG_ID");
        if (!ObjectUtils.isEmpty((Object)orgId)) {
            this.getModel().setValue("org", (Object)Long.parseLong(orgId.toString()));
        } else {
            HasPermOrgResult hrPermOrg = OrgPermHelper.getHRPermOrg((boolean)true);
            List hasPermOrgs = hrPermOrg.getHasPermOrgs();
            if (!Objects.isNull(hasPermOrgs) && hasPermOrgs.size() != 0) {
                if (hasPermOrgs.contains(RequestContext.get().getOrgId())) {
                    this.getModel().setValue("org", (Object)RequestContext.get().getOrgId());
                } else {
                    this.getModel().setValue("org", hasPermOrgs.get(0));
                }
            } else {
                LOG.error("StructProjectEditPlugin can not get any org");
            }
        }
    }

    public void beforeBindData(EventObject e) {
        OtherStructTypeService serviceHelper;
        DynamicObject structTypeData;
        super.beforeBindData(e);
        BaseShowParameter formShowParameter = (BaseShowParameter)this.getView().getFormShowParameter();
        OperationStatus status = formShowParameter.getStatus();
        if (OperationStatus.EDIT.equals((Object)status) && (structTypeData = (serviceHelper = new OtherStructTypeService()).getStructTypeDataById(formShowParameter.getPkId(), "enable")) != null) {
            String enable = structTypeData.getString("enable");
            if (!"10".equals(enable)) {
                this.getView().setEnable(Boolean.valueOf(false), new String[]{"effdt"});
            } else {
                this.getView().setEnable(Boolean.valueOf(true), new String[]{"effdt"});
            }
        }
        StructTypeHelper.setTips((IFormView)this.getView(), (String)"haos_structtype", (String[])new String[]{"name", "effdt"});
    }

    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        OperationResult operationResult = afterDoOperationEventArgs.getOperationResult();
        String operateKey = afterDoOperationEventArgs.getOperateKey();
        if (operationResult.isSuccess()) {
            if ((operationResult.getMessage() == null || operationResult.getMessage().isEmpty()) && operateKey.equals("save")) {
                this.getView().getParentView().showSuccessNotification(ResManager.loadKDString((String)"\u4fdd\u5b58\u6210\u529f", (String)"StructTypeEditPlugin_2", (String)"hrmp-haos-formplugin", (Object[])new Object[0]));
                this.getView().sendFormAction(this.getView().getParentView());
                this.getView().close();
                return;
            }
            this.getView().getParentView().showSuccessNotification(operationResult.getMessage());
            this.getView().sendFormAction(this.getView().getParentView());
            this.getView().close();
        }
    }
}

