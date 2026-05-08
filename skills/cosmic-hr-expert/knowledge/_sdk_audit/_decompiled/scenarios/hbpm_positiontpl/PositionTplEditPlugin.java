/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.collect.Maps
 *  kd.bos.base.BaseShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.control.Control
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
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.permission.api.HasPermOrgResult
 *  kd.bos.servicehelper.permission.PermissionServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplRepository
 *  kd.hrmp.hbpm.business.utils.JobLevelGradeRangeUtil
 *  kd.hrmp.hbpm.business.utils.PositionUtils
 *  kd.hrmp.hbpm.business.utils.SystemParamHelper
 *  kd.hrmp.hbpm.common.util.PositionTplUtil
 *  org.apache.commons.lang3.StringUtils
 */
package kd.hrmp.hbpm.formplugin.web.position;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kd.bos.base.BaseShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.control.Control;
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
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.api.HasPermOrgResult;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionTplRepository;
import kd.hrmp.hbpm.business.utils.JobLevelGradeRangeUtil;
import kd.hrmp.hbpm.business.utils.PositionUtils;
import kd.hrmp.hbpm.business.utils.SystemParamHelper;
import kd.hrmp.hbpm.common.util.PositionTplUtil;

public final class PositionTplEditPlugin
extends HRDataBaseEdit
implements BeforeF7SelectListener {
    private static Log LOGGER = LogFactory.getLog(PositionTplEditPlugin.class);
    private static final long TIPS_ID = 2000164510891009024L;

    public void afterCreateNewData(EventObject e) {
        IPageCache pageCache = this.getView().getPageCache();
        String isReFresh = pageCache.get("isReFresh");
        if (isReFresh != null) {
            this.setModifyStrategy();
            return;
        }
        pageCache.put("isReFresh", "1");
        String orgId = (String)this.getView().getFormShowParameter().getCustomParam("orgId");
        if (!org.apache.commons.lang3.StringUtils.isEmpty((CharSequence)orgId)) {
            this.getModel().setValue("org", (Object)Long.parseLong(orgId));
        } else {
            String appId = this.getView().getFormShowParameter().getCheckRightAppId();
            HasPermOrgResult hrPermOrg = PermissionServiceHelper.getAllPermOrgs((long)RequestContext.get().getCurrUserId(), (String)"21", (String)appId, (String)"hbpm_positiontpl", (String)"47150e89000000ac");
            List hasPermOrgs = hrPermOrg.getHasPermOrgs();
            if (!Objects.isNull(hasPermOrgs) && hasPermOrgs.size() != 0) {
                if (hasPermOrgs.contains(RequestContext.get().getOrgId())) {
                    this.getModel().setValue("org", (Object)RequestContext.get().getOrgId());
                } else {
                    this.getModel().setValue("org", hasPermOrgs.get(0));
                }
            } else {
                LOGGER.error("PositionTplEditPlugin can not get any org");
            }
        }
        this.setModifyStrategy();
    }

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        JobLevelGradeRangeUtil.getInstance().registerListener(this.getView(), (AbstractFormPlugin)this, (BeforeF7SelectListener)this);
        BasedataEdit positiontpl = (BasedataEdit)this.getView().getControl("posttpltype");
        positiontpl.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        FormOperate operate = (FormOperate)args.getSource();
        operate.getOption().setVariableValue("isFromPage", "1");
        String operateKey = operate.getOperateKey();
        if ("modify".equals(operateKey)) {
            String tips;
            boolean openTplChangePos;
            DynamicObject org = (DynamicObject)this.getModel().getValue("org");
            if (org == null) {
                return;
            }
            long orgId = org.getLong("id");
            Map batchParameter = SystemParamHelper.getBatchParameter(Collections.singletonList(orgId));
            Map parameterData = (Map)batchParameter.get(String.valueOf(orgId));
            if (parameterData == null) {
                this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u7ec4\u7ec7\u4f53\u7cfb\u7ba1\u7406\u7ec4\u7ec7\u201c%1$s\u201d\u672a\u542f\u7528\u6a21\u677f\u5e93\uff0c\u4e0d\u53ef\u53d8\u66f4\u6570\u636e\u3002", (String)"PositionTplEditPlugin_0", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), org.getString("name")));
                args.setCancel(true);
                return;
            }
            Boolean openTpl = parameterData.getOrDefault("openpositiontpl", Boolean.FALSE);
            if (openTpl != null && !openTpl.booleanValue()) {
                this.getView().showTipNotification(String.format(ResManager.loadKDString((String)"\u7ec4\u7ec7\u4f53\u7cfb\u7ba1\u7406\u7ec4\u7ec7\u201c%1$s\u201d\u672a\u542f\u7528\u6a21\u677f\u5e93\uff0c\u4e0d\u53ef\u53d8\u66f4\u6570\u636e\u3002", (String)"PositionTplEditPlugin_0", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]), org.getString("name")));
                args.setCancel(true);
            }
            if ((openTplChangePos = parameterData.getOrDefault("positiontplchangepos", false).booleanValue()) && HRStringUtils.isNotEmpty((String)(tips = (String)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSService", (String)"getContent", (Object[])new Object[]{2000164510891009024L})))) {
                this.getView().showTipNotification(tips, Integer.valueOf(10000));
            }
        } else if (HRStringUtils.equals((String)"save", (String)operateKey) && !this.getModel().getDataChanged()) {
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u65e0\u4fe1\u606f\u53d8\u66f4\uff0c\u8bf7\u786e\u8ba4\u3002", (String)"PositionTplEditPlugin_1", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]));
            args.setCancel(true);
        }
        boolean dataChangeed = this.getModel().getDataChanged();
        if (HRStringUtils.equals((String)"save", (String)operateKey) || HRStringUtils.equals((String)"modify", (String)operateKey)) {
            this.setModifyStrategy(true);
            if (!dataChangeed) {
                this.getModel().setDataChanged(false);
            }
        }
    }

    public void click(EventObject evt) {
        super.click(evt);
        Control source = (Control)evt.getSource();
        String operationKey = source.getKey();
        JobLevelGradeRangeUtil.getInstance().click(this.getModel(), this.getView(), operationKey, (IFormPlugin)this);
    }

    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        String option = (String)this.getView().getFormShowParameter().getCustomParam("options");
        if (HRStringUtils.equals((String)"view", (String)option)) {
            PositionTplEditPlugin.viewStatus(this.getView(), this.getModel().getDataEntity());
        }
        this.setModifyStrategy();
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        JobLevelGradeRangeUtil.getInstance().afterBindData(model, view);
        this.getModel().setDataChanged(false);
        this.setFiedRangeVisable();
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        if (null != args.getOperationResult() && !args.getOperationResult().isSuccess() && null != args.getOperationResult() && !args.getOperationResult().isSuccess()) {
            boolean indexErrorExist;
            FormOperate formOperate = (FormOperate)args.getSource();
            OperateOption option = formOperate.getOption();
            boolean nameErrorExist = option.containsVariable("nameError");
            if (nameErrorExist) {
                String displayErrorInfo = ResManager.loadKDString((String)"\u5df2\u5b58\u5728\u540d\u79f0\u76f8\u540c\u4e14\u53ef\u7528\u7684\u5c97\u4f4d\u7c7b\u578b\u3002", (String)"PositionTplEditPlugin_2", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
                PositionTplUtil.isNameEnableRe((IFormView)this.getView(), (String)"name", (String)displayErrorInfo);
            }
            if (indexErrorExist = option.containsVariable("indexError")) {
                String indexErrorInfo = ResManager.loadKDString((String)"\u6570\u636e\u5df2\u5b58\u5728", (String)"PositionTplEditPlugin_3", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
                PositionTplUtil.isNameEnableRe((IFormView)this.getView(), (String)"index", (String)indexErrorInfo);
            }
        }
        String operateKey = ((AbstractOperate)args.getSource()).getOperateKey();
        if (null != args.getOperationResult() && args.getOperationResult().isSuccess()) {
            if (StringUtils.equals((CharSequence)"modify", (CharSequence)operateKey)) {
                PositionTplEditPlugin.editStatus(this.getView());
            }
            if (StringUtils.equals((CharSequence)"save", (CharSequence)operateKey)) {
                this.getView().getFormShowParameter().setStatus(OperationStatus.VIEW);
                PositionTplEditPlugin.viewStatus(this.getView(), this.getModel().getDataEntity());
            }
        }
    }

    public void propertyChanged(PropertyChangedArgs args) {
        super.propertyChanged(args);
        String operationKey = args.getProperty().getName();
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        JobLevelGradeRangeUtil.getInstance().propertyChanged(model, view, operationKey, false);
        if (operationKey.equals("org")) {
            this.setModifyStrategy(true);
        }
    }

    public void preOpenForm(PreOpenFormEventArgs arg) {
        super.preOpenForm(arg);
        FormShowParameter formShowParameter = arg.getFormShowParameter();
        if (formShowParameter.getStatus().equals((Object)OperationStatus.ADDNEW)) {
            String name = ResManager.loadKDString((String)"\u65b0\u589e\u5c97\u4f4d\u6a21\u677f", (String)"PositionTplEditPlugin_4", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
            formShowParameter.setCaption(name);
        } else if (formShowParameter instanceof BaseShowParameter) {
            Object pkId = ((BaseShowParameter)formShowParameter).getPkId();
            if (Objects.isNull(pkId) || !(pkId instanceof Long)) {
                return;
            }
            Object[] dynamicObjects = PositionTplRepository.getInstance().queryPositionTplNameById(Collections.singletonList((Long)pkId));
            if (!PositionUtils.isArrayEmpty((Object[])dynamicObjects).booleanValue()) {
                String name = ResManager.loadKDString((String)"\u5c97\u4f4d\u6a21\u677f-", (String)"PositionTplEditPlugin_5", (String)"hrmp-hbpm-formplugin", (Object[])new Object[0]);
                formShowParameter.setCaption(name + dynamicObjects[0].getString("name"));
                formShowParameter.setCustomParam("options", (Object)"view");
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        String actionId = closedCallBackEvent.getActionId();
        Map returnData = (Map)closedCallBackEvent.getReturnData();
        IDataModel model = this.getModel();
        JobLevelGradeRangeUtil.getInstance().closedCallBack(model, returnData, actionId);
    }

    private static void viewStatus(IFormView view, DynamicObject dataEntity) {
        boolean isAudit = HRBaseDataConfigUtil.getAudit((String)"hbpm_positiontpl");
        if (isAudit) {
            String status = dataEntity.getString("status");
            view.setVisible(Boolean.FALSE, new String[]{"bar_save"});
            if (!HRStringUtils.equals((String)"A", (String)status)) {
                view.setVisible(Boolean.FALSE, new String[]{"bar_modify"});
            } else {
                view.setVisible(Boolean.TRUE, new String[]{"bar_modify"});
            }
        } else {
            view.setVisible(Boolean.TRUE, new String[]{"bar_modify"});
            view.setVisible(Boolean.FALSE, new String[]{"bar_save"});
        }
    }

    private static void editStatus(IFormView view) {
        view.getFormShowParameter().setStatus(OperationStatus.EDIT);
        view.setVisible(Boolean.TRUE, new String[]{"bar_save"});
        view.setVisible(Boolean.FALSE, new String[]{"bar_modify"});
    }

    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        IDataModel model = this.getModel();
        IFormView view = this.getView();
        JobLevelGradeRangeUtil.getInstance().beforeF7Select(beforeF7SelectEvent, model, view, false);
        String f7Name = beforeF7SelectEvent.getProperty().getName();
        if ("org".equals(f7Name)) {
            String appId = this.getView().getFormShowParameter().getCheckRightAppId();
            HasPermOrgResult hasPermOrgResult = PermissionServiceHelper.getAllPermOrgs((long)RequestContext.get().getCurrUserId(), (String)"21", (String)appId, (String)"hbpm_positiontpl", (String)"47150e89000000ac");
            if (!hasPermOrgResult.hasAllOrgPerm()) {
                QFilter qFilter = new QFilter("id", "in", (Object)hasPermOrgResult.getHasPermOrgs());
                beforeF7SelectEvent.addCustomQFilter(qFilter);
            }
        }
    }

    private void setModifyStrategy() {
        this.setModifyStrategy(false);
    }

    private void setModifyStrategy(boolean fromDataChange) {
        boolean openpositiontpl;
        DynamicObject org = (DynamicObject)this.getModel().getValue("org");
        if (org == null) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"modifystrategy"});
            return;
        }
        long buId = org.getLong("id");
        Map batchParameter = SystemParamHelper.getBatchParameter(Collections.singletonList(buId));
        if (batchParameter == null || batchParameter.size() == 0) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"modifystrategy"});
            this.getModel().setValue("ablemodifyfield", (Object)false);
            this.getModel().setValue("fieldrange", null);
            return;
        }
        Map parameter = batchParameter.getOrDefault(String.valueOf(buId), Maps.newHashMap());
        if (parameter.isEmpty()) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"modifystrategy"});
            this.getModel().setValue("ablemodifyfield", (Object)false);
            this.getModel().setValue("fieldrange", null);
            return;
        }
        boolean positiontplismodify = parameter.getOrDefault("positiontplismodify", false);
        this.getModel().setValue("ablemodifyfield", (Object)positiontplismodify);
        String modifyfieldrange = parameter.getOrDefault("modifyfieldrange", "");
        if (!HRStringUtils.isEmpty((String)modifyfieldrange)) {
            List fieldRangeDataList = (List)JSONObject.parseObject((String)modifyfieldrange, List.class);
            List fieldRangeId = fieldRangeDataList.stream().map(temp -> temp.getOrDefault((Object)"modifyfieldrange", (Object)0L)).collect(Collectors.toList());
            Object[] fieldRangeIdArray = fieldRangeId.toArray();
            if (positiontplismodify) {
                this.getModel().setValue("fieldrange", (Object)fieldRangeIdArray);
            } else {
                this.getModel().setValue("fieldrange", null);
            }
        }
        if (!(openpositiontpl = parameter.getOrDefault("openpositiontpl", false).booleanValue())) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"modifystrategy"});
            return;
        }
        if (positiontplismodify) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"fieldrange"});
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{"fieldrange"});
        }
        this.getView().setVisible(Boolean.TRUE, new String[]{"modifystrategy"});
    }

    void setFiedRangeVisable() {
        boolean ablemodifyfield = (Boolean)this.getModel().getValue("ablemodifyfield");
        if (ablemodifyfield) {
            this.getView().setVisible(Boolean.TRUE, new String[]{"fieldrange"});
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{"fieldrange"});
        }
    }
}

