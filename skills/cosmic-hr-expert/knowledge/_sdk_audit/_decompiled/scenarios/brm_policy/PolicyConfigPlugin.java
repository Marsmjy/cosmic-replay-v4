/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.bill.BillOperationStatus
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.LocaleString
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.ListSelectedRow
 *  kd.bos.entity.datamodel.ListSelectedRowCollection
 *  kd.bos.entity.datamodel.events.ChangeData
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.ext.hr.ruleengine.controls.DefaultResult
 *  kd.bos.ext.hr.ruleengine.controls.RosterCondition
 *  kd.bos.ext.hr.ruleengine.controls.RosterResult
 *  kd.bos.ext.hr.ruleengine.utils.ParamsUtil
 *  kd.bos.ext.hr.ruleengine.utils.RuleValidateUtil
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowFormHelper
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.Toolbar
 *  kd.bos.form.control.events.ItemClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.ComboItem
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.MutexHelper
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.bos.list.ListFilterParameter
 *  kd.bos.list.ListShowParameter
 *  kd.bos.orm.query.QFilter
 *  kd.hr.brm.business.web.DecisionTableHelper
 *  kd.hr.brm.business.web.RuleServiceHelper
 *  kd.hr.brm.common.constants.PolicyConfigConstants
 *  kd.hr.brm.formplugin.util.OperateLogUtil
 *  kd.hr.brm.formplugin.util.RuleMutexUtil
 *  kd.hr.brm.formplugin.util.SceneUtil
 *  kd.hr.brm.formplugin.web.IHROrgReset
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.cache.HRAppCache
 *  kd.hr.hbp.common.cache.IHRAppCache
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.common.util.guava.ImmutableSetUtil
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 */
package kd.hr.brm.formplugin.web;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.ext.hr.ruleengine.controls.DefaultResult;
import kd.bos.ext.hr.ruleengine.controls.RosterCondition;
import kd.bos.ext.hr.ruleengine.controls.RosterResult;
import kd.bos.ext.hr.ruleengine.utils.ParamsUtil;
import kd.bos.ext.hr.ruleengine.utils.RuleValidateUtil;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowFormHelper;
import kd.bos.form.ShowType;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.ComboItem;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.MutexHelper;
import kd.bos.form.plugin.IFormPlugin;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QFilter;
import kd.hr.brm.business.web.DecisionTableHelper;
import kd.hr.brm.business.web.RuleServiceHelper;
import kd.hr.brm.common.constants.PolicyConfigConstants;
import kd.hr.brm.formplugin.util.OperateLogUtil;
import kd.hr.brm.formplugin.util.RuleMutexUtil;
import kd.hr.brm.formplugin.util.SceneUtil;
import kd.hr.brm.formplugin.web.IHROrgReset;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.cache.HRAppCache;
import kd.hr.hbp.common.cache.IHRAppCache;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.common.util.guava.ImmutableSetUtil;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;

public final class PolicyConfigPlugin
extends HRBaseDataCommonEdit
implements IHROrgReset,
PolicyConfigConstants,
BeforeF7SelectListener {
    private static final IHRAppCache CACHE = HRAppCache.get((String)"brm");
    private static final String ORI_PAGE_STATUS = "oriPageStatus";
    private static final String ORI_PAGE_BILLSTATUS = "oriPageBillStatus";
    private final Set<String> notValidOp = ImmutableSetUtil.of((Object)"close", (Object)"refresh", (Object)"configrule", (Object)"closepage");

    private RosterCondition getRosterCondition() {
        return (RosterCondition)this.getControl("rosterconditionap");
    }

    private RosterResult getRosterResult() {
        return (RosterResult)this.getControl("rosterresultap");
    }

    private DefaultResult getDefaultResult() {
        return (DefaultResult)this.getControl("defaultresultap");
    }

    public void afterLoadData(EventObject e) {
        DynamicObject scene = (DynamicObject)this.getModel().getValue("scene");
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (!scene.getBoolean("iseditrule") || !scene.getBoolean("iseditscene")) {
            this.getView().getFormShowParameter().setStatus(OperationStatus.VIEW);
        } else {
            this.getView().getFormShowParameter().setStatus(status);
        }
    }

    public void beforeBindData(EventObject e) {
        BillOperationStatus billStatus;
        super.beforeBindData(e);
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (status != OperationStatus.ADDNEW) {
            this.putAppCache(ORI_PAGE_STATUS, status.toString());
        }
        if ((billStatus = ((BillShowParameter)this.getView().getFormShowParameter()).getBillStatus()) != BillOperationStatus.ADDNEW) {
            this.putAppCache(ORI_PAGE_BILLSTATUS, billStatus.toString());
        }
        if (!OperationStatus.ADDNEW.equals((Object)this.getView().getFormShowParameter().getStatus())) {
            String policyId = this.getModel().getValue("id").toString();
            this.getRosterCondition().setPolicy(policyId);
            this.getRosterResult().setPolicy(policyId);
            this.getDefaultResult().setPolicy(policyId);
        }
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        if (!OperationStatus.ADDNEW.equals((Object)this.getView().getFormShowParameter().getStatus())) {
            this.getView().getParentView().getPageCache().put(this.getCurrentPageIdKey(), this.getView().getPageId());
            this.getView().setEnable(Boolean.FALSE, new String[]{"number", "bizappid", "scene", "policytype"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"returndefaultflax", "simpleinfoflex", "rosterflex", "orgparam"});
            if (((Boolean)this.getModel().getValue("retrundefault")).booleanValue()) {
                this.getView().setVisible(Boolean.TRUE, new String[]{"returndefaultflax"});
            }
            if (((Boolean)this.getModel().getValue("enablelist")).booleanValue()) {
                this.getView().setVisible(Boolean.TRUE, new String[]{"rosterflex"});
            }
            if (((Boolean)this.getModel().getValue("enableminfirst")).booleanValue()) {
                this.getView().setVisible(Boolean.TRUE, new String[]{"orgparam"});
                this.setOrgParamsMustFill(true);
            }
            this.setCustomControlState();
            this.logView();
        } else {
            String defaultSceneIdStr;
            this.getView().setVisible(Boolean.FALSE, new String[]{"returndefaultflax", "rosterflex", "simpleinfoflex", "orgparam"});
            this.getView().setEnable(Boolean.TRUE, new String[]{"bizappid", "scene"});
            String createBu = (String)this.getView().getFormShowParameter().getCustomParams().get("createbu");
            if (StringUtils.isNotEmpty((CharSequence)createBu)) {
                this.getModel().setValue("createbu", (Object)Long.parseLong(createBu));
            } else {
                this.getModel().setValue("createbu", (Object)RequestContext.get().getOrgId());
                this.resetHROrg(this.getModel(), "createbu");
            }
            String defaultAppId = (String)this.getView().getFormShowParameter().getCustomParams().get("defaultAppId");
            if (defaultAppId != null) {
                this.getModel().setValue("bizappid", (Object)defaultAppId);
            }
            if ((defaultSceneIdStr = (String)this.getView().getFormShowParameter().getCustomParams().get("defaultSceneId")) != null) {
                this.getModel().setValue("scene", (Object)Long.valueOf(defaultSceneIdStr));
            }
        }
        this.setOrgParamList((DynamicObject)this.getModel().getValue("scene"));
        this.getPageCache().put("origin_enable_default_results", this.getModel().getValue("retrundefault").toString());
        this.getPageCache().put("origin_default_results", (String)this.getModel().getValue("results"));
        this.getPageCache().put("origin_roster_conditions", (String)this.getModel().getValue("rostercondition"));
        this.getPageCache().put("origin_roster_results", (String)this.getModel().getValue("rosterresult"));
        this.getPageCache().put("origin_enable_min_first", this.getModel().getValue("enableminfirst").toString());
        this.getModel().setDataChanged(false);
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        BasedataEdit sceneEdit = (BasedataEdit)this.getView().getControl("scene");
        sceneEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
        Toolbar mainToolBar = (Toolbar)this.getControl("tbmain");
        mainToolBar.addItemClickListener((ItemClickListener)this);
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        String propertyName = evt.getProperty().getName();
        if (HRStringUtils.equals((String)propertyName, (String)"scene")) {
            Object bizApp = this.getModel().getValue("bizappid");
            if (!(bizApp instanceof DynamicObject)) {
                this.getView().showErrorNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u9009\u62e9\u5e94\u7528\uff0c\u518d\u9009\u62e9\u573a\u666f\u3002", (String)"PolicyConfigPlugin_0", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
                evt.setCancel(true);
                return;
            }
            QFilter filter = new QFilter("bizappid", "=", ((DynamicObject)bizApp).getPkValue());
            evt.addCustomQFilter(filter);
        }
    }

    public void propertyChanged(PropertyChangedArgs e) {
        String prop = e.getProperty().getName();
        ChangeData changeData = e.getChangeSet()[0];
        switch (prop) {
            case "bizappid": {
                DynamicObject dobj = changeData.getDataEntity();
                DynamicObject bizapp = dobj.getDynamicObject("bizappid");
                if (bizapp == null) break;
                this.getModel().setValue("scene", null);
                break;
            }
            case "retrundefault": {
                this.getView().setVisible((Boolean)changeData.getNewValue(), new String[]{"returndefaultflax"});
                break;
            }
            case "enablelist": {
                this.getView().setVisible((Boolean)changeData.getNewValue(), new String[]{"rosterflex"});
                break;
            }
            case "enableminfirst": {
                boolean result = (Boolean)changeData.getNewValue();
                if (!result) {
                    DynamicObject[] ruleDys;
                    List ruleNoCondition;
                    Long id = (Long)this.getModel().getValue("id");
                    if (id != 0L && !(ruleNoCondition = Arrays.stream(ruleDys = RuleServiceHelper.queryDesignRuleByPolicy((Object)id)).filter(dy -> RuleValidateUtil.isDecisionSetConditionEmpty((String)dy.getString("conditions"))).collect(Collectors.toList())).isEmpty()) {
                        this.getView().showTipNotification(ResManager.loadKDString((String)"\u64cd\u4f5c\u5931\u8d25\uff0c\u7981\u7528\u540e\u4f1a\u5b58\u5728\u6ca1\u6709\u6761\u4ef6\u7684\u89c4\u5219\u3002", (String)"PolicyConfigPlugin_3", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
                        this.getModel().setValue("enableminfirst", (Object)Boolean.TRUE);
                        return;
                    }
                    this.getModel().setValue("orgparam", null);
                }
                this.getView().setVisible(Boolean.valueOf(result), new String[]{"orgparam"});
                this.setOrgParamsMustFill(result);
                break;
            }
            case "ruledate": {
                Date newDate = (Date)changeData.getNewValue();
                if (null == newDate) {
                    return;
                }
                String dateFormat = this.getView().getPageCache().get("ruleDateFormat");
                String date = HRDateTimeUtils.format((Date)newDate, (String)dateFormat);
                this.getRosterCondition().setDate(date);
                this.getRosterResult().setDate(date);
                this.getDefaultResult().setDate(date);
                this.getModel().setValue("ruledate", null);
                break;
            }
            case "scene": {
                DynamicObject sceneDy = (DynamicObject)changeData.getNewValue();
                if (sceneDy == null) {
                    this.getRosterCondition().clearAll();
                    this.getRosterResult().clearAll();
                    this.getDefaultResult().clearAll();
                    this.setOrgParamList(null);
                    break;
                }
                String scene = sceneDy.getString("id");
                this.getRosterCondition().setScene(scene);
                this.getRosterResult().setScene(scene);
                this.getDefaultResult().setScene(scene);
                this.setOrgParamList(sceneDy);
                break;
            }
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        if (!this.notValidOp.contains(operateKey) && !this.validatePage(args)) {
            return;
        }
        switch (operateKey) {
            case "save": 
            case "saveandconfigrule": 
            case "submit": {
                this.validateBeforeSave(args);
                break;
            }
            case "newbu": {
                args.setCancel(true);
                this.showBuF7();
                break;
            }
            case "closepage": {
                args.setCancel(true);
                this.validClose();
                break;
            }
            case "audit": 
            case "enable": {
                op.getOption().setVariableValue("fromEdit", "1");
                break;
            }
        }
    }

    private void validateBeforeSave(BeforeDoOperationEventArgs args) {
        this.validateNumberFormat(args);
        this.setRoster();
        this.setDefaultResult();
    }

    private void validateNumberFormat(BeforeDoOperationEventArgs args) {
        if (!Pattern.matches("^[a-zA-Z0-9_\\-@#$%^&*\\[\\]]+$", (String)this.getModel().getValue("number"))) {
            OperateLogUtil.showTipAndWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)args, (MultiLangEnumBridge)new MultiLangEnumBridge("\u7b56\u7565\u7f16\u7801\u4e0d\u5408\u6cd5\uff0c\u8bf7\u4fee\u6539\u3002", "PolicyConfigPlugin_1", "hrmp-brm-formplugin"), (String[])new String[0]);
            args.setCancel(true);
        }
    }

    private void setRoster() {
        if (((Boolean)this.getModel().getValue("enablelist")).booleanValue()) {
            this.getModel().setValue("rostercondition", (Object)this.getRosterCondition().getValue());
            this.getModel().setValue("rosterresult", (Object)this.getRosterResult().getValue());
        }
    }

    private void setDefaultResult() {
        if (((Boolean)this.getModel().getValue("retrundefault")).booleanValue()) {
            this.getModel().setValue("results", (Object)this.getDefaultResult().getValue());
        }
    }

    private boolean validatePage(BeforeDoOperationEventArgs args) {
        if (this.isOpenRuleList() && HRStringUtils.equals((String)SceneUtil.getPageStatus((IFormView)this.getView(), (String)"brm_policy_edit"), (String)"edit")) {
            args.setCancel(true);
            this.getView().showTipNotification(ResManager.loadKDString((String)"\u8bf7\u5148\u5728\u89c4\u5219\u5217\u8868\u9875\u9762\u4fdd\u5b58\u8be5\u7b56\u7565\u7684\u89c4\u5219\u5e76\u5173\u95ed\u89c4\u5219\u5217\u8868\u9875\u9762\u3002", (String)"PolicyConfigPlugin_2", (String)"hrmp-brm-formplugin", (Object[])new Object[0]));
            return false;
        }
        return true;
    }

    public void afterDoOperation(AfterDoOperationEventArgs args) {
        super.afterDoOperation(args);
        AbstractOperate op = (AbstractOperate)args.getSource();
        String operateKey = op.getOperateKey();
        String policyType = (String)this.getModel().getValue("policytype");
        switch (operateKey) {
            case "saveandconfigrule": {
                if (!args.getOperationResult().isSuccess()) break;
                MutexHelper.release((IFormView)this.getView());
                RuleMutexUtil.releaseMutex((IFormView)this.getView(), (String)this.getPageCache().get("policyid"), (String)"needLock");
                this.openChildrenPage(policyType);
                this.getView().setStatus(OperationStatus.VIEW);
                break;
            }
            case "configrule": {
                this.openChildrenPage(policyType);
                break;
            }
            case "submit": {
                this.putAppCache(ORI_PAGE_BILLSTATUS, BillOperationStatus.SUBMIT.toString());
                break;
            }
            case "audit": {
                this.putAppCache(ORI_PAGE_BILLSTATUS, BillOperationStatus.AUDIT.toString());
                break;
            }
            case "unsubmit": {
                this.putAppCache(ORI_PAGE_BILLSTATUS, BillOperationStatus.EDIT.toString());
                break;
            }
            case "unaudit": {
                this.putAppCache(ORI_PAGE_BILLSTATUS, BillOperationStatus.EDIT.toString());
                break;
            }
        }
    }

    public void closedCallBack(ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        String actionId = evt.getActionId();
        if (HRStringUtils.equals((String)actionId, (String)"bos_org") && evt.getReturnData() instanceof ListSelectedRowCollection) {
            ListSelectedRowCollection selectedCol = (ListSelectedRowCollection)evt.getReturnData();
            this.fillBuEntry(selectedCol);
        } else if (HRStringUtils.equals((String)actionId, (String)"brm_rulelist")) {
            String objId = String.valueOf(this.getModel().getDataEntity().getPkValue());
            this.getPageCache().remove("brm_rulelist" + objId);
            this.refreshCurrenPage();
        } else if (HRStringUtils.equals((String)actionId, (String)"page_decisionTable")) {
            String objId = String.valueOf(this.getModel().getDataEntity().getPkValue());
            this.getPageCache().remove("brm_decision_tables" + objId);
            this.refreshCurrenPage();
        }
    }

    public void beforeClosed(BeforeClosedEvent e) {
        super.beforeClosed(e);
        this.clearViewCache();
    }

    public void pageRelease(EventObject e) {
        super.pageRelease(e);
        RuleMutexUtil.releaseMutex((IFormView)this.getView(), (String)this.getPageCache().get("policyid"), (String)"needLock");
    }

    private void validClose() {
        String old;
        String originEnableResult = this.getPageCache().get("origin_enable_default_results");
        String originResult = this.getPageCache().get("origin_default_results");
        String originRosterCondition = this.getPageCache().get("origin_roster_conditions");
        String originRosterResult = this.getPageCache().get("origin_roster_results");
        String enableResult = this.getModel().getValue("retrundefault").toString();
        String resultJson = this.getDefaultResult().getValue();
        String rosterCondition = this.getRosterCondition().getValue();
        String rosterResult = this.getRosterResult().getValue();
        if (!HRStringUtils.equals((String)originEnableResult, (String)enableResult) || HRStringUtils.equals((String)"true", (String)enableResult) && !HRStringUtils.equals((String)originResult, (String)resultJson)) {
            old = (String)this.getModel().getValue("results");
            this.getModel().setValue("results", (Object)resultJson);
            this.getModel().setValue("results", (Object)old);
        }
        if (!HRStringUtils.equals((String)originRosterCondition, (String)rosterCondition)) {
            old = (String)this.getModel().getValue("rostercondition");
            this.getModel().setValue("rostercondition", (Object)rosterCondition);
            this.getModel().setValue("rostercondition", (Object)old);
        }
        if (!HRStringUtils.equals((String)originRosterResult, (String)rosterResult)) {
            old = (String)this.getModel().getValue("rosterresult");
            this.getModel().setValue("rosterresult", (Object)rosterResult);
            this.getModel().setValue("rosterresult", (Object)old);
        }
        this.getView().close();
    }

    private void showBuF7() {
        ListShowParameter fsp = ShowFormHelper.createShowListForm((String)"bos_org", (boolean)true, (int)0, (boolean)true);
        fsp.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "bos_org"));
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        fsp.setMultiSelect(true);
        fsp.setShowTitle(false);
        fsp.setHasRight(true);
        fsp.getCustomParams().put("orgFuncId", "11");
        Set buIds = this.getModel().getEntryEntity("entrybulist").stream().map(dy -> dy.getLong("entitybu.id")).collect(Collectors.toSet());
        if (!buIds.isEmpty()) {
            ListFilterParameter filterParameter = new ListFilterParameter();
            filterParameter.setFilter(new QFilter("id", "not in", buIds));
            fsp.setListFilterParameter(filterParameter);
        }
        this.getView().showForm((FormShowParameter)fsp);
    }

    private void fillBuEntry(ListSelectedRowCollection selectedCol) {
        int colSize = selectedCol.size();
        if (colSize == 0) {
            return;
        }
        int rowCount = this.getModel().getEntryRowCount("entrybulist");
        this.getModel().batchCreateNewEntryRow("entrybulist", colSize);
        for (int i = 0; i < colSize; ++i) {
            ListSelectedRow row = selectedCol.get(i);
            long ruleId = (Long)row.getPrimaryKeyValue();
            this.getModel().setValue("entitybu", (Object)ruleId, rowCount + i);
        }
    }

    private void openRuleListPage() {
        String status = SceneUtil.getPageStatus((IFormView)this.getView(), (String)"brm_policy_edit");
        Long policyId = (Long)this.getModel().getDataEntity().getPkValue();
        BillShowParameter parameter = new BillShowParameter();
        parameter.setFormId("brm_rulelist");
        parameter.setPkId(this.getModel().getDataEntity().getPkValue());
        parameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        parameter.setPageId(this.getRuleListPageId(policyId));
        parameter.setCustomParam("pageState", (Object)status);
        parameter.setCustomParam("policyId", (Object)policyId);
        parameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "brm_rulelist"));
        parameter.setStatus(this.getView().getFormShowParameter().getStatus());
        parameter.setBillStatus(((BillShowParameter)this.getView().getFormShowParameter()).getBillStatus());
        this.getView().showForm((FormShowParameter)parameter);
    }

    private void refreshCurrenPage() {
        String oriPageBillStatus;
        String oriPageStatus = this.getAppCache(ORI_PAGE_STATUS);
        if (StringUtils.isEmpty((CharSequence)oriPageStatus)) {
            oriPageStatus = OperationStatus.EDIT.toString();
        }
        if (StringUtils.isEmpty((CharSequence)(oriPageBillStatus = this.getAppCache(ORI_PAGE_BILLSTATUS)))) {
            oriPageBillStatus = OperationStatus.EDIT.toString();
        }
        this.getView().getFormShowParameter().setStatus(OperationStatus.valueOf((String)oriPageStatus));
        ((BillShowParameter)this.getView().getFormShowParameter()).setBillStatus(BillOperationStatus.valueOf((String)oriPageBillStatus));
        this.getView().invokeOperation("refresh");
        if (!HRBaseDataConfigUtil.getAudit((String)"brm_policy_edit")) {
            MutexHelper.release((IFormView)this.getView());
            RuleMutexUtil.requireMutex((IFormView)this.getView(), (String)this.getModel().getDataEntity().getPkValue().toString(), (String)"needLock");
        }
    }

    private void openChildrenPage(String policyType) {
        if ("decision_table".equals(policyType)) {
            this.openDecisionTablePage();
        } else {
            this.openRuleListPage();
        }
    }

    private void openDecisionTablePage() {
        String status = SceneUtil.getPageStatus((IFormView)this.getView(), (String)"brm_policy_edit");
        Long policyId = (Long)this.getModel().getDataEntity().getPkValue();
        String sceneId = ((DynamicObject)this.getModel().getValue("scene")).getString("id");
        BillShowParameter showParameter = new BillShowParameter();
        showParameter.setFormId("brm_decision_tables");
        DynamicObject decisionTableDy = DecisionTableHelper.getDecisionTableByPolicy((Object)policyId);
        if (decisionTableDy != null) {
            showParameter.setPkId(decisionTableDy.getPkValue());
            showParameter.setCustomParam("pageState", (Object)"EDIT");
        } else {
            showParameter.setCustomParam("pageState", (Object)"ADDNEW");
            showParameter.setCustomParam("policyid", (Object)policyId);
        }
        showParameter.setPageId(this.getDecisionTablePageId(policyId));
        showParameter.setCustomParam("sceneid", (Object)sceneId);
        showParameter.setCustomParam("parentPageStatus", (Object)status);
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        showParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "page_decisionTable"));
        showParameter.setStatus(this.getView().getFormShowParameter().getStatus());
        showParameter.setBillStatus(((BillShowParameter)this.getView().getFormShowParameter()).getBillStatus());
        this.getView().showForm((FormShowParameter)showParameter);
    }

    private String getRuleListPageId(Long policyId) {
        return this.getView().getParentView().getPageId() + "_" + policyId + "_ruleListPage_" + RequestContext.get().getCurrUserId();
    }

    private String getDecisionTablePageId(Long policyId) {
        return this.getView().getParentView().getPageId() + "_" + "page_decisionTable" + "_" + policyId + "_" + RequestContext.get().getCurrUserId();
    }

    private void setCustomControlState() {
        String pageState = SceneUtil.getPageStatus((IFormView)this.getView(), (String)"brm_policy_edit");
        this.getRosterCondition().setPageState(pageState);
        this.getRosterResult().setPageState(pageState);
        this.getDefaultResult().setPageState(pageState);
    }

    private void logView() {
        IFormView parentView = this.getView().getParentView();
        String objId = String.valueOf(this.getModel().getDataEntity().getPkValue());
        parentView.getPageCache().put("brm_policy_edit" + objId, "true");
    }

    private boolean isOpenRuleList() {
        String objId = String.valueOf(this.getModel().getDataEntity().getPkValue());
        IFormView view = this.getView();
        String isOpen = view.getPageCache().get("brm_rulelist" + objId);
        if (isOpen != null) {
            return Boolean.parseBoolean(isOpen);
        }
        IFormView parentView = this.getView().getParentView();
        isOpen = parentView.getPageCache().get("brm_rulelist" + objId);
        if (isOpen != null) {
            return Boolean.parseBoolean(isOpen);
        }
        return false;
    }

    private void clearViewCache() {
        IFormView parentView = this.getView().getParentView();
        String objId = String.valueOf(this.getModel().getDataEntity().getPkValue());
        if (parentView != null) {
            parentView.getPageCache().remove("brm_policy_edit" + objId);
            parentView.getPageCache().remove("brm_decision_tables" + objId);
            parentView.getPageCache().remove(this.getCurrentPageIdKey());
        }
    }

    private String getCurrentPageIdKey() {
        return "policy_page_id__" + RequestContext.get().getCurrUserId() + "_" + this.getModel().getValue("id");
    }

    private void setOrgParamList(DynamicObject sceneDy) {
        if (sceneDy == null) {
            return;
        }
        Map orgParamMap = ParamsUtil.getAdminOrgParam((Long)sceneDy.getLong("id"));
        ComboEdit orgParams = (ComboEdit)this.getControl("orgparam");
        ArrayList data = Lists.newArrayListWithExpectedSize((int)16);
        orgParamMap.forEach((num, name) -> data.add(new ComboItem(new LocaleString(name), num)));
        orgParams.setComboItems((List)data);
    }

    private void setOrgParamsMustFill(boolean mustFill) {
        ComboEdit orgParams = (ComboEdit)this.getControl("orgparam");
        orgParams.setMustInput(mustFill);
    }

    protected List<String> getUnCheckField() {
        List uncheckFieldList = super.getUnCheckField();
        uncheckFieldList.add("retrundefault");
        uncheckFieldList.add("enablelist");
        uncheckFieldList.add("resultdate");
        uncheckFieldList.add("enablecombo");
        uncheckFieldList.add("entryrulelist");
        uncheckFieldList.add("rostercondition");
        uncheckFieldList.add("rosterresult");
        uncheckFieldList.add("seq");
        uncheckFieldList.add("createbu");
        return uncheckFieldList;
    }

    private String getAppCache(String key) {
        return (String)CACHE.get(key + this.getView().getPageId() + this.getModel().getDataEntity().getPkValue().toString(), String.class);
    }

    private void putAppCache(String key, String value) {
        CACHE.put(key + this.getView().getPageId() + this.getModel().getDataEntity().getPkValue().toString(), (Object)value);
    }
}
