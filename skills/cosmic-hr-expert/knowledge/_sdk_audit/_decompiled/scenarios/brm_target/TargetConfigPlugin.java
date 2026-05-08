/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.bill.OperationStatus
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.datamodel.IDataModel
 *  kd.bos.entity.datamodel.events.PropertyChangedArgs
 *  kd.bos.entity.tree.TreeNode
 *  kd.bos.ext.form.control.CustomControl
 *  kd.bos.ext.hr.ruleengine.controls.TargetCondition
 *  kd.bos.ext.hr.ruleengine.controls.TargetElse
 *  kd.bos.ext.hr.ruleengine.controls.TargetResult
 *  kd.bos.ext.hr.ruleengine.utils.IDStringUtils
 *  kd.bos.ext.hr.ruleengine.utils.ParamsUtil
 *  kd.bos.form.IFormView
 *  kd.bos.form.IPageCache
 *  kd.bos.form.control.Search
 *  kd.bos.form.control.TreeView
 *  kd.bos.form.control.events.BeforeItemClickEvent
 *  kd.bos.form.control.events.ClickListener
 *  kd.bos.form.control.events.SearchEnterEvent
 *  kd.bos.form.control.events.SearchEnterListener
 *  kd.bos.form.control.events.TreeNodeClickListener
 *  kd.bos.form.events.AfterDoOperationEventArgs
 *  kd.bos.form.events.BeforeClosedEvent
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.CustomEventArgs
 *  kd.bos.form.field.BasedataEdit
 *  kd.bos.form.field.ComboEdit
 *  kd.bos.form.field.events.BeforeF7SelectEvent
 *  kd.bos.form.field.events.BeforeF7SelectListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.MutexHelper
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.brm.business.util.TargetExpressionUtil
 *  kd.hr.brm.business.web.RuleServiceHelper
 *  kd.hr.brm.common.constants.RuleControlConstants
 *  kd.hr.brm.common.constants.TargetConfigConstants
 *  kd.hr.brm.common.enums.ParamTypeEnum
 *  kd.hr.brm.common.enums.ReturnTypeEnum
 *  kd.hr.brm.formplugin.util.OperateLogUtil
 *  kd.hr.brm.formplugin.util.SceneUtil
 *  kd.hr.brm.formplugin.util.TargetUtils
 *  kd.hr.brm.formplugin.web.IHROrgReset
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit
 *  kd.hr.hbp.formplugin.web.util.TreeViewSearchTool
 */
package kd.hr.brm.formplugin.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.tree.TreeNode;
import kd.bos.ext.form.control.CustomControl;
import kd.bos.ext.hr.ruleengine.controls.TargetCondition;
import kd.bos.ext.hr.ruleengine.controls.TargetElse;
import kd.bos.ext.hr.ruleengine.controls.TargetResult;
import kd.bos.ext.hr.ruleengine.utils.IDStringUtils;
import kd.bos.ext.hr.ruleengine.utils.ParamsUtil;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.control.Search;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.SearchEnterEvent;
import kd.bos.form.control.events.SearchEnterListener;
import kd.bos.form.control.events.TreeNodeClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.MutexHelper;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.brm.business.util.TargetExpressionUtil;
import kd.hr.brm.business.web.RuleServiceHelper;
import kd.hr.brm.common.constants.RuleControlConstants;
import kd.hr.brm.common.constants.TargetConfigConstants;
import kd.hr.brm.common.enums.ParamTypeEnum;
import kd.hr.brm.common.enums.ReturnTypeEnum;
import kd.hr.brm.formplugin.util.OperateLogUtil;
import kd.hr.brm.formplugin.util.SceneUtil;
import kd.hr.brm.formplugin.util.TargetUtils;
import kd.hr.brm.formplugin.web.IHROrgReset;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit;
import kd.hr.hbp.formplugin.web.util.TreeViewSearchTool;

public final class TargetConfigPlugin
extends HRBaseDataCommonEdit
implements IHROrgReset,
RuleControlConstants,
TargetConfigConstants,
ClickListener,
SearchEnterListener,
BeforeF7SelectListener {
    private static final String CREATE_ORG = "bu";
    private static final Log LOGGER = LogFactory.getLog(TargetConfigPlugin.class);

    public void afterLoadData(EventObject e) {
        DynamicObject scene = (DynamicObject)this.getModel().getValue("scene");
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        if (!scene.getBoolean("iseditrule") || !scene.getBoolean("iseditscene")) {
            this.getView().getFormShowParameter().setStatus(OperationStatus.VIEW);
        } else {
            this.getView().getFormShowParameter().setStatus(status);
        }
    }

    private void initControlOnAddTarget() {
        String bizApp = (String)this.getView().getFormShowParameter().getCustomParam("bizApp");
        Long scene = (Long)this.getView().getFormShowParameter().getCustomParam("scene");
        String targetTypeGroup = (String)this.getView().getFormShowParameter().getCustomParam("targettypegroup");
        String returnType = (String)this.getView().getFormShowParameter().getCustomParam("returntype");
        if (HRStringUtils.isNotEmpty((String)bizApp) && scene != null && HRStringUtils.isNotEmpty((String)targetTypeGroup) && HRStringUtils.isNotEmpty((String)returnType)) {
            this.getModel().setValue("bizapp", (Object)bizApp);
            this.getModel().setValue("scene", (Object)scene);
            this.getModel().setValue("returntype", (Object)returnType);
            this.getView().setEnable(Boolean.FALSE, new String[]{"bizapp", "scene", "returntype"});
            if (!"all".equals(targetTypeGroup)) {
                this.getModel().setValue("targettypegroup", (Object)targetTypeGroup);
                this.getView().setEnable(Boolean.FALSE, new String[]{"targettypegroup"});
            }
        }
    }

    public void beforeBindData(EventObject evtObj) {
        String pageStatus;
        super.beforeBindData(evtObj);
        this.initControlOnAddTarget();
        this.setSceneParamToCache(this.getModel().getValue("scene"));
        String returnTypeVal = (String)this.getModel().getValue("returntype");
        this.getView().setVisible(Boolean.valueOf(ReturnTypeEnum.DATE.getValue().equals(returnTypeVal)), new String[]{"dateformat"});
        String targetType = (String)this.getModel().getValue("targettypegroup");
        this.setVisibleForTargetType(targetType);
        this.handleCustomControl(targetType, returnTypeVal);
        this.constructBusinessObjTreeView();
        this.getPageCache().put("returnExpressionText", (String)this.getModel().getValue("displayfunctiontext"));
        this.setMethodName("init");
        this.setChangeFlag(true);
        if (null == this.getModel().getValue("scene") || "function".equals(this.getModel().getValue("targettypegroup"))) {
            List returnTypeList = TargetUtils.getReturnTypeListNoContainParam();
            ComboEdit paramComboControl = (ComboEdit)this.getView().getControl("returntype");
            paramComboControl.setComboItems(returnTypeList);
            this.getView().updateView("returntype");
            this.getModel().setValue("returntype", (Object)returnTypeVal);
        }
        if (this.getModel().getValue(CREATE_ORG) == null) {
            String createBu = (String)this.getView().getFormShowParameter().getCustomParams().get(CREATE_ORG);
            if (StringUtils.isNotEmpty((CharSequence)createBu)) {
                this.getModel().setValue(CREATE_ORG, (Object)Long.parseLong(createBu));
            } else {
                this.getModel().setValue(CREATE_ORG, (Object)RequestContext.get().getOrgId());
                this.resetHROrg(this.getModel(), CREATE_ORG);
            }
        } else {
            this.handleMutex();
        }
        if (this.getModel().getValue("returntype") == null || !ParamTypeEnum.DYNAMICOBJECT.getValue().equals(this.getModel().getValue("returntype").toString())) {
            this.getView().setVisible(Boolean.FALSE, new String[]{"returnparamfield"});
        } else {
            ((ComboEdit)this.getControl("returnparamfield")).setMustInput(true);
        }
        if ("condition".equals(this.getModel().getValue("targettypegroup"))) {
            this.getPageCache().put("origin_target_conditions", (String)this.getModel().getValue("conditions"));
            this.getPageCache().put("origin_target_results", (String)this.getModel().getValue("results"));
            this.getPageCache().put("origin_target_else", (String)this.getModel().getValue("elses"));
        }
        if (HRStringUtils.equals((String)(pageStatus = SceneUtil.getPageStatus((IFormView)this.getView(), (String)"brm_target")), (String)"view")) {
            this.getView().getPageCache().put("pageStatus", pageStatus);
        }
        this.getModel().setDataChanged(false);
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
    }

    private void handleCustomControl(String targetType, String returnTypeVal) {
        if (HRStringUtils.equals((String)targetType, (String)"condition")) {
            String targetId;
            TargetCondition targetCondition = this.getTargetCondition();
            TargetResult targetResult = this.getTargetResult();
            TargetElse targetElse = this.getTargetElse();
            DynamicObject sceneDy = (DynamicObject)this.getModel().getValue("scene");
            if (sceneDy != null) {
                String sceneId = sceneDy.getString("id");
                targetCondition.setScene(sceneId);
                targetResult.setScene(sceneId);
                targetElse.setScene(sceneId);
            }
            if (IDStringUtils.idNotEmpty((String)(targetId = this.getModel().getValue("id").toString()))) {
                targetResult.setType(returnTypeVal);
                targetElse.setType(returnTypeVal);
                if (ParamTypeEnum.DATE.getValue().equals(returnTypeVal)) {
                    String dataFormat = (String)this.getModel().getValue("dateformat");
                    targetResult.setDateFormat(dataFormat);
                    targetElse.setDateFormat(dataFormat);
                }
                targetCondition.setTarget(targetId);
                targetResult.setTarget(targetId);
                targetElse.setTarget(targetId);
            }
        }
    }

    private void handleMutex() {
        StringBuilder errorMsg = new StringBuilder();
        boolean isNotLocked = MutexHelper.require((IFormView)this.getView(), (String)"brm_target", (Object)this.getModel().getDataEntity().getPkValue(), (String)"modify", (StringBuilder)errorMsg);
        this.getPageCache().put("isNotLocked", String.valueOf(isNotLocked));
        this.getPageCache().put("originReturnType", (String)this.getModel().getValue("returntype"));
        this.getPageCache().put("originReturnParamField", (String)this.getModel().getValue("returnparamfield"));
        this.getView().setEnable(Boolean.FALSE, new String[]{"number", "bizapp", "scene"});
        this.getPageCache().put("isEdit", "true");
    }

    public void registerListener(EventObject evt) {
        super.registerListener(evt);
        TreeView tv1 = (TreeView)this.getView().getControl("objtreeview");
        tv1.addTreeNodeClickListener(this.getTreeNodeClickListener());
        Search search = (Search)this.getControl("paramsearch");
        search.addEnterListener((SearchEnterListener)this);
        BasedataEdit sceneEdit = (BasedataEdit)this.getView().getControl("scene");
        sceneEdit.addBeforeF7SelectListener((BeforeF7SelectListener)this);
    }

    public void beforeF7Select(BeforeF7SelectEvent evt) {
        SceneUtil.beforeSceneF7Select((IDataModel)this.getModel(), (IFormView)this.getView(), (BeforeF7SelectEvent)evt);
    }

    public void propertyChanged(PropertyChangedArgs args) {
        String key = args.getProperty().getName();
        Object newValue = args.getChangeSet()[0].getNewValue();
        if (null == newValue) {
            return;
        }
        switch (key) {
            case "targettypegroup": {
                this.setVisibleForTargetType((String)newValue);
                break;
            }
            case "scene": {
                String sceneId = ((DynamicObject)newValue).getString("id");
                this.setSceneParamToCache(newValue);
                this.constructBusinessObjTreeView();
                this.getView().updateView("objtreeview");
                this.setMethodName("change");
                this.updateExpressionTextData(this.getPageCache().get("returnExpressionText"));
                this.getTargetCondition().setScene(sceneId);
                this.getTargetResult().setScene(sceneId);
                this.getTargetElse().setScene(sceneId);
                break;
            }
            case "displayfunctiontext": {
                String afterSave = this.getPageCache().get("afterSave");
                if (!HRStringUtils.equals((String)afterSave, (String)"1") && !HRStringUtils.equals((String)"true", (String)this.getPageCache().get("dont_start_event"))) {
                    this.initExpressionTextDataEvent();
                }
                this.getPageCache().remove("afterSave");
                break;
            }
            case "bizapp": {
                this.getModel().setValue("scene", null);
                break;
            }
            case "returntype": {
                this.getTargetResult().setType(newValue.toString());
                this.getTargetElse().setType(newValue.toString());
                this.getView().setVisible(Boolean.valueOf(ReturnTypeEnum.DATE.getValue().equals(newValue.toString())), new String[]{"dateformat"});
                break;
            }
            case "dateformat": {
                String dataFormat = (String)this.getModel().getValue("dateformat");
                this.getTargetResult().setDateFormatAndUpdateControl(dataFormat);
                this.getTargetElse().setDateFormatAndUpdateControl(dataFormat);
                break;
            }
            case "ruledate": {
                String dataFormat1 = this.getView().getPageCache().get("ruleDateFormat");
                this.getTargetCondition().setDate(HRDateTimeUtils.format((Date)((Date)newValue), (String)dataFormat1));
                String dataFormat2 = (String)this.getModel().getValue("dateformat");
                this.getTargetResult().setDate(HRDateTimeUtils.format((Date)((Date)newValue), (String)dataFormat2));
                this.getTargetElse().setDate(HRDateTimeUtils.format((Date)((Date)newValue), (String)dataFormat2));
                this.getModel().setValue("ruledate", null);
                break;
            }
        }
    }

    public void beforeItemClick(BeforeItemClickEvent evt) {
        if ("closepage".equals(evt.getOperationKey())) {
            this.validClose();
            evt.setCancel(true);
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs evt) {
        AbstractOperate op;
        String operateKey;
        if (evt.getSource() instanceof AbstractOperate && ("save".equals(operateKey = (op = (AbstractOperate)evt.getSource()).getOperateKey()) || "submit".equals(operateKey))) {
            if (!this.validateBeforeSave(evt)) {
                return;
            }
            String targetType = (String)this.getModel().getValue("targettypegroup");
            if (HRStringUtils.equals((String)targetType, (String)"function")) {
                String expr = this.transferFunctionCondition(evt);
                this.preExecuteExpression(expr, evt);
                this.getModel().setValue("conditions", (Object)expr);
            } else {
                this.saveControlValue();
            }
        }
    }

    public void afterDoOperation(AfterDoOperationEventArgs evt) {
        super.afterDoOperation(evt);
        AbstractOperate op = (AbstractOperate)evt.getSource();
        String operateKey = op.getOperateKey();
        if ("save".equals(operateKey)) {
            this.getModel().setDataChanged(false);
        }
    }

    public void beforeClosed(BeforeClosedEvent evt) {
        super.beforeClosed(evt);
        String expr = this.getPageCache().get("returnExpressionText");
        if ("function".equals(this.getModel().getValue("targettypegroup")) && !HRStringUtils.equals((String)expr, (String)((String)this.getModel().getValue("displayfunctiontext")))) {
            this.getPageCache().put("dont_start_event", "true");
            this.getModel().setValue("displayfunctiontext", (Object)expr);
        }
    }

    private void validClose() {
        String old;
        if ("function".equals(this.getModel().getValue("targettypegroup"))) {
            this.getView().close();
            return;
        }
        String originConditions = this.getPageCache().get("origin_target_conditions");
        String originResults = this.getPageCache().get("origin_target_results");
        String originElse = this.getPageCache().get("origin_target_else");
        String conditionJson = this.getTargetCondition().getValue();
        String resultJson = this.getTargetResult().getValue();
        String elseJson = this.getTargetElse().getValue();
        if (!HRStringUtils.equals((String)originConditions, (String)conditionJson)) {
            old = (String)this.getModel().getValue("conditions");
            this.getModel().setValue("conditions", (Object)conditionJson);
            this.getModel().setValue("conditions", (Object)old);
        }
        if (!HRStringUtils.equals((String)originResults, (String)resultJson)) {
            old = (String)this.getModel().getValue("results");
            this.getModel().setValue("results", (Object)resultJson);
            this.getModel().setValue("results", (Object)old);
        }
        if (!HRStringUtils.equals((String)originElse, (String)elseJson)) {
            old = (String)this.getModel().getValue("elses");
            this.getModel().setValue("elses", (Object)elseJson);
            this.getModel().setValue("elses", (Object)old);
        }
        this.getView().close();
    }

    private boolean validateBeforeSave(BeforeDoOperationEventArgs evt) {
        return this.validateNumber(evt) && this.validateRef(evt);
    }

    private boolean validateNumber(BeforeDoOperationEventArgs evt) {
        String number = (String)this.getModel().getValue("number");
        if (!Pattern.matches("^[a-zA-Z0-9_\\-@#$%^&*\\[\\]]+$", number)) {
            OperateLogUtil.showTipAndWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)evt, (MultiLangEnumBridge)new MultiLangEnumBridge("\u6307\u6807\u7f16\u7801\u4e0d\u5408\u6cd5\uff0c\u8bf7\u4fee\u6539\u3002", "TargetConfigPlugin_0", "hrmp-brm-formplugin"), (String[])new String[0]);
            evt.setCancel(true);
            return false;
        }
        return true;
    }

    private boolean validateRef(BeforeDoOperationEventArgs evt) {
        Long targetId = (Long)this.getModel().getDataEntity().getPkValue();
        if (IDStringUtils.idEmpty((Long)targetId)) {
            return true;
        }
        DynamicObjectCollection policyCol = RuleServiceHelper.getReferTargetPolicyCol((Long)targetId);
        if (policyCol.isEmpty()) {
            return true;
        }
        StringBuilder policyNumSb = new StringBuilder();
        policyCol.stream().map(policy -> policy.getString("policy.number")).distinct().forEach(num -> policyNumSb.append((String)num).append(','));
        String policyNumStr = policyNumSb.substring(0, policyNumSb.length() - 1);
        String originReturnType = this.getPageCache().get("originReturnType");
        String currentReturnType = this.getModel().getValue("returntype").toString();
        String targetType = this.getModel().getValue("targettypegroup").toString();
        if (HRStringUtils.equals((String)targetType, (String)"condition")) {
            if (ReturnTypeEnum.DYNAMICOBJECT.getValue().equals(originReturnType)) {
                String originReturnParamField = this.getPageCache().get("originReturnParamField");
                originReturnType = originReturnParamField.split("\\|")[1];
            }
            if (ReturnTypeEnum.DYNAMICOBJECT.getValue().equals(currentReturnType)) {
                String currentReturnParamField = this.getModel().getValue("returnparamfield").toString();
                currentReturnType = currentReturnParamField.split("\\|")[1];
            }
        }
        if (!HRStringUtils.equals((String)originReturnType, (String)currentReturnType)) {
            OperateLogUtil.showTipAndWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)evt, (MultiLangEnumBridge)new MultiLangEnumBridge("\u6307\u6807\u5df2\u7ecf\u88ab\u7f16\u7801\u4e3a%s\u7684\u7b56\u7565\u5f15\u7528\uff0c\u4e0d\u5141\u8bb8\u4fee\u6539\u8fd4\u56de\u503c\u7c7b\u578b\u3002", "TargetConfigPlugin_1", "hrmp-brm-formplugin"), (String[])new String[]{policyNumStr});
            evt.setCancel(true);
            return false;
        }
        return true;
    }

    public void customEvent(CustomEventArgs args) {
        if ("initExpressionTextDataEvent".equals(args.getEventName())) {
            this.initExpressionTextDataEvent();
        } else if ("returnExpressionText".equals(args.getEventName())) {
            String returnText = args.getEventArgs();
            this.getPageCache().put("returnExpressionText", returnText);
        }
    }

    private void initExpressionTextDataEvent() {
        String displayExpression = (String)this.getModel().getValue("displayfunctiontext");
        this.updateExpressionTextData(displayExpression);
    }

    private void updateExpressionTextData(String displayExpression) {
        boolean changeFlag = this.getChangeFlag();
        String method = this.getMethodName();
        HashMap data = Maps.newHashMapWithExpectedSize((int)16);
        data.put("displayExpression", displayExpression);
        data.put("method", this.getMethodName());
        data.put("changeFlag", changeFlag);
        if (HRStringUtils.equals((String)method, (String)"init") || HRStringUtils.equals((String)method, (String)"change")) {
            String inputParamStr = this.getPageCache().get("paramNumMap");
            Map inputParamMap = (Map)SerializationUtils.fromJsonString((String)inputParamStr, Map.class);
            data.put("inputParams", inputParamMap.keySet());
            this.setMethodName("insert");
        }
        CustomControl customcontrol = (CustomControl)this.getView().getControl("expressioncontrol");
        customcontrol.setData((Object)data);
        this.setChangeFlag(!changeFlag);
    }

    private void saveControlValue() {
        this.getModel().setValue("conditions", (Object)this.getTargetCondition().getValue());
        this.getModel().setValue("results", (Object)this.getTargetResult().getValue());
        this.getModel().setValue("elses", (Object)this.getTargetElse().getValue());
    }

    private void setVisibleForTargetType(String currentTargetType) {
        if (HRStringUtils.equals((String)currentTargetType, (String)"condition")) {
            DynamicObject sceneDy = (DynamicObject)this.getModel().getValue("scene");
            if (sceneDy != null) {
                String sceneId = sceneDy.getString("id");
                this.getTargetCondition().setScene(sceneId);
                this.getTargetResult().setScene(sceneId);
                this.getTargetElse().setScene(sceneId);
            }
            this.getView().setVisible(Boolean.TRUE, new String[]{"conditionconfigflax"});
            this.getView().setVisible(Boolean.FALSE, new String[]{"functionconfigflax"});
            List returnType = TargetUtils.getReturnTypeListContainParam();
            ComboEdit paramComboControl = (ComboEdit)this.getView().getControl("returntype");
            paramComboControl.setComboItems(returnType);
            this.getView().updateView("returntype");
        } else {
            this.getView().setVisible(Boolean.FALSE, new String[]{"conditionconfigflax", "returnparamfield"});
            this.getView().setVisible(Boolean.TRUE, new String[]{"functionconfigflax"});
            List returnType = TargetUtils.getReturnTypeListNoContainParam();
            ComboEdit paramComboControl = (ComboEdit)this.getView().getControl("returntype");
            paramComboControl.setComboItems(returnType);
            this.getView().updateView("returntype");
        }
    }

    private void setSceneParamToCache(Object scene) {
        if (scene == null) {
            this.getPageCache().remove("sceneInputStringParam");
            this.getPageCache().remove("sceneInputObjectParam");
            return;
        }
        List inputParamList = ParamsUtil.getInputParamData((Long)((DynamicObject)scene).getLong("id"));
        HashMap stringParams = Maps.newHashMapWithExpectedSize((int)16);
        HashMap objectParams = Maps.newHashMapWithExpectedSize((int)16);
        HashMap paramTypeMap = Maps.newHashMapWithExpectedSize((int)16);
        inputParamList.forEach(paramMap -> {
            String paramName = (String)paramMap.get("name");
            String paramNumber = (String)paramMap.get("number");
            String paramType = (String)paramMap.get("type");
            if (paramName == null) {
                String paramText = (String)paramMap.get("text");
                stringParams.put(paramText, paramNumber);
                paramType = ParamTypeEnum.BASEDATA.getValue();
            } else if (paramName.contains(".")) {
                String[] paramNameKey = paramName.split("\\.");
                List attrList = objectParams.getOrDefault(paramNameKey[0], Lists.newArrayListWithCapacity((int)16));
                HashMap<String, String> paramsForShow = new HashMap<String, String>(16);
                paramsForShow.put("paramNumber", paramNumber);
                paramsForShow.put("paramShortName", paramNameKey[1]);
                String children = (String)paramMap.get("children");
                if (StringUtils.isNotEmpty((CharSequence)children)) {
                    paramsForShow.put("children", children);
                    List childrenList = SerializationUtils.fromJsonStringToList((String)children, Map.class);
                    childrenList.forEach(child -> {
                        String cfr_ignored_0 = (String)paramTypeMap.put(child.get("number"), child.get("type"));
                    });
                }
                attrList.add(paramsForShow);
                objectParams.put(paramNameKey[0], attrList);
            } else {
                stringParams.put(paramName, paramNumber);
            }
            paramTypeMap.put(paramNumber, paramType);
        });
        LOGGER.info(((Object)stringParams).toString());
        this.getPageCache().put("sceneInputStringParam", SerializationUtils.toJsonString((Object)stringParams));
        this.getPageCache().put("sceneInputObjectParam", SerializationUtils.toJsonString((Object)objectParams));
        this.getPageCache().put("sceneInputObjectParamTypeMap", SerializationUtils.toJsonString((Object)paramTypeMap));
    }

    private void constructBusinessObjTreeView() {
        TreeNode tn1;
        String p1;
        TreeView tv1 = (TreeView)this.getView().getControl("objtreeview");
        tv1.deleteAllNodes();
        String rootId = "0";
        TreeNode rootNode = new TreeNode(null, "0", ResManager.loadKDString((String)"\u8f93\u5165\u53c2\u6570", (String)"TargetConfigPlugin_2", (String)"hrmp-brm-formplugin", (Object[])new Object[0]), true);
        rootNode.setIsOpened(true);
        String stringParamStr = this.getPageCache().get("sceneInputStringParam");
        String objectParamStr = this.getPageCache().get("sceneInputObjectParam");
        Map stringParams = Maps.newHashMapWithExpectedSize((int)1);
        Map objectParams = Maps.newHashMapWithExpectedSize((int)1);
        if (HRStringUtils.isNotEmpty((String)stringParamStr)) {
            stringParams = (Map)SerializationUtils.fromJsonString((String)stringParamStr, Map.class);
        }
        if (HRStringUtils.isNotEmpty((String)objectParamStr)) {
            objectParams = (Map)SerializationUtils.fromJsonString((String)objectParamStr, Map.class);
        }
        HashMap paramNumMap = Maps.newHashMapWithExpectedSize((int)16);
        int lv = 0;
        for (Map.Entry entry : stringParams.entrySet()) {
            p1 = "0-" + lv;
            tn1 = new TreeNode("0", p1, (String)entry.getKey(), false);
            tn1.setData(entry.getValue());
            tn1.setLeaf(true);
            rootNode.addChild(tn1);
            ++lv;
            paramNumMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry entry : objectParams.entrySet()) {
            p1 = "0-" + lv;
            tn1 = new TreeNode("0", p1, (String)entry.getKey(), true);
            tn1.setIsOpened(true);
            List attributes = (List)entry.getValue();
            for (int j = 0; j < attributes.size(); ++j) {
                TreeNode tn2;
                String p2 = p1 + "-" + j;
                Map param = (Map)attributes.get(j);
                String paramShortName = (String)param.get("paramShortName");
                String paramNumber = (String)param.get("paramNumber");
                String children = (String)param.get("children");
                if (StringUtils.isEmpty((CharSequence)children)) {
                    tn2 = new TreeNode(p1, p2, paramShortName, false);
                    tn2.setData((Object)paramNumber);
                    tn2.setLeaf(true);
                    tn1.addChild(tn2);
                } else {
                    tn2 = new TreeNode(p1, p2, paramShortName, true);
                    tn2.setData((Object)paramNumber);
                    tn2.setLeaf(false);
                    tn1.addChild(tn2);
                    List childrenList = SerializationUtils.fromJsonStringToList((String)children, Map.class);
                    for (int k = 0; k < childrenList.size(); ++k) {
                        String p3 = p2 + "-" + k;
                        Map lastChild = (Map)childrenList.get(k);
                        String name = (String)lastChild.get("text");
                        String number = (String)lastChild.get("number");
                        TreeNode tn3 = new TreeNode(p2, p3, name, false);
                        tn3.setData((Object)number);
                        tn3.setLeaf(true);
                        tn2.addChild(tn3);
                        paramNumMap.put((String)entry.getKey() + "." + paramShortName + "." + name, number);
                    }
                }
                paramNumMap.put((String)entry.getKey() + "." + paramShortName, paramNumber);
            }
            rootNode.addChild(tn1);
            ++lv;
        }
        tv1.addNode(rootNode);
        this.getPageCache().put(tv1.getKey(), SerializationUtils.toJsonString((Object)rootNode));
        this.getPageCache().put("paramNumMap", SerializationUtils.toJsonString((Object)paramNumMap));
    }

    private TreeNodeClickListener getTreeNodeClickListener() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public void search(SearchEnterEvent evt) {
        String searchText = evt.getText();
        TreeView tv = (TreeView)this.getView().getControl("objtreeview");
        TreeViewSearchTool.search((String)searchText, (TreeView)tv, (IPageCache)this.getPageCache(), (String)"objtreeview");
    }

    private String transferFunctionCondition(BeforeDoOperationEventArgs evt) {
        String functionText = this.getPageCache().get("returnExpressionText");
        if (HRStringUtils.isEmpty((String)functionText)) {
            OperateLogUtil.showErrorWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)evt, (MultiLangEnumBridge)new MultiLangEnumBridge("\u8868\u8fbe\u5f0f\u4e0d\u80fd\u4e3a\u7a7a\uff0c\u8bf7\u68c0\u67e5\u3002", "TargetConfigPlugin_3", "hrmp-brm-formplugin"), (String[])new String[0]);
            evt.setCancel(true);
            return "";
        }
        String paramNumMapStr = this.getPageCache().get("paramNumMap");
        Map paramNumMap = HRStringUtils.isEmpty((String)paramNumMapStr) ? Maps.newHashMapWithExpectedSize((int)16) : (Map)SerializationUtils.fromJsonString((String)paramNumMapStr, Map.class);
        String result = "";
        MultiLangEnumBridge validateResult = TargetExpressionUtil.validateExpression((String)functionText);
        if (null == validateResult) {
            try {
                HashMap exprResultMap = Maps.newHashMapWithExpectedSize((int)16);
                if (TargetExpressionUtil.transferExpression((String)functionText, (Map)paramNumMap, (Map)exprResultMap)) {
                    return (String)exprResultMap.get("successExpr");
                }
                OperateLogUtil.showErrorWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)evt, (MultiLangEnumBridge)new MultiLangEnumBridge("\u8868\u8fbe\u5f0f\u4e2d\u51fa\u73b0\u4e86\u672a\u5b9a\u4e49\u7684\u53d8\u91cf\u6216\u7b26\u53f7\uff1a\u201c%s\u201d\uff0c\u8bf7\u68c0\u67e5\u3002", "TargetConfigPlugin_4", "hrmp-brm-formplugin"), (String[])new String[]{(String)exprResultMap.get("undefinedVal")});
                evt.setCancel(true);
            }
            catch (Exception ex) {
                LOGGER.error((Throwable)ex);
                OperateLogUtil.showErrorWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)evt, (MultiLangEnumBridge)new MultiLangEnumBridge("\u8868\u8fbe\u5f0f\u8bed\u6cd5\u9519\u8bef\uff0c\u8bf7\u68c0\u67e5\u3002", "TargetConfigPlugin_5", "hrmp-brm-formplugin"), (String[])new String[0]);
                evt.setCancel(true);
            }
        } else {
            OperateLogUtil.showErrorWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)evt, (MultiLangEnumBridge)validateResult, (String[])new String[0]);
            evt.setCancel(true);
        }
        return result;
    }

    private void preExecuteExpression(String expression, BeforeDoOperationEventArgs args) {
        if (HRStringUtils.isEmpty((String)expression)) {
            return;
        }
        String paramTypeMapStr = this.getPageCache().get("sceneInputObjectParamTypeMap");
        if (paramTypeMapStr.isEmpty()) {
            return;
        }
        Map paramTypeMap = (Map)SerializationUtils.fromJsonString((String)paramTypeMapStr, Map.class);
        Map resultMap = TargetExpressionUtil.preCompileExpression((String)expression, (Map)paramTypeMap);
        boolean isSuccess = (Boolean)resultMap.get("isSuccess");
        if (!isSuccess) {
            OperateLogUtil.showErrorWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)args, (MultiLangEnumBridge)new MultiLangEnumBridge("\u8868\u8fbe\u5f0f\u8bed\u6cd5\u9519\u8bef\uff0c\u8bf7\u68c0\u67e5\u3002", "BREEFunctionExecuteService_0", "hrmp-bree-mservice"), (String[])new String[0]);
            args.setCancel(true);
            return;
        }
        this.validReturnTypeByResult(resultMap.get("result"), args);
    }

    private void validReturnTypeByResult(Object result, BeforeDoOperationEventArgs args) {
        boolean isNumber;
        String returnType = this.getModel().getValue("returntype").toString();
        boolean isString = (result instanceof String || result instanceof StringBuilder) && !ParamTypeEnum.STRING.getValue().equals(returnType);
        boolean bl = isNumber = (result instanceof Long || result instanceof Integer || result instanceof Double || result instanceof Float) && !ParamTypeEnum.NUMBER.getValue().equals(returnType);
        if (isString) {
            OperateLogUtil.showErrorWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)args, (MultiLangEnumBridge)new MultiLangEnumBridge("\u8868\u8fbe\u5f0f\u5b9e\u9645\u8fd4\u56de\u7ed3\u679c\u7c7b\u578b\u4e3a\u5b57\u7b26\u4e32\uff0c\u4e0e\u9009\u62e9\u7684\u8fd4\u56de\u7c7b\u578b\u4e0d\u4e00\u81f4\uff0c\u8bf7\u68c0\u67e5\u3002", "TargetConfigPlugin_6", "hrmp-brm-formplugin"), (String[])new String[0]);
            args.setCancel(true);
        } else if (result instanceof Boolean && !ParamTypeEnum.BOOLEAN.getValue().equals(returnType)) {
            OperateLogUtil.showErrorWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)args, (MultiLangEnumBridge)new MultiLangEnumBridge("\u8868\u8fbe\u5f0f\u5b9e\u9645\u8fd4\u56de\u7ed3\u679c\u7c7b\u578b\u4e3a\u5e03\u5c14\uff0c\u4e0e\u9009\u62e9\u7684\u8fd4\u56de\u7c7b\u578b\u4e0d\u4e00\u81f4\uff0c\u8bf7\u68c0\u67e5\u3002", "TargetConfigPlugin_7", "hrmp-brm-formplugin"), (String[])new String[0]);
            args.setCancel(true);
        } else if (result instanceof Date && !ParamTypeEnum.DATE.getValue().equals(returnType)) {
            OperateLogUtil.showErrorWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)args, (MultiLangEnumBridge)new MultiLangEnumBridge("\u8868\u8fbe\u5f0f\u5b9e\u9645\u8fd4\u56de\u7ed3\u679c\u7c7b\u578b\u4e3a\u65e5\u671f\uff0c\u4e0e\u9009\u62e9\u7684\u8fd4\u56de\u7c7b\u578b\u4e0d\u4e00\u81f4\uff0c\u8bf7\u68c0\u67e5\u3002", "TargetConfigPlugin_8", "hrmp-brm-formplugin"), (String[])new String[0]);
            args.setCancel(true);
        } else if (isNumber) {
            OperateLogUtil.showErrorWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)args, (MultiLangEnumBridge)new MultiLangEnumBridge("\u8868\u8fbe\u5f0f\u5b9e\u9645\u8fd4\u56de\u7ed3\u679c\u7c7b\u578b\u4e3a\u6570\u5b57\uff0c\u4e0e\u9009\u62e9\u7684\u8fd4\u56de\u7c7b\u578b\u4e0d\u4e00\u81f4\uff0c\u8bf7\u68c0\u67e5\u3002", "TargetConfigPlugin_9", "hrmp-brm-formplugin"), (String[])new String[0]);
            args.setCancel(true);
        } else {
            this.getPageCache().put("afterSave", "1");
            String functionText = this.getPageCache().get("returnExpressionText");
            this.getModel().setValue("displayfunctiontext", (Object)functionText);
        }
    }

    private boolean getChangeFlag() {
        return "true".equals(this.getPageCache().get("changeFlagKey"));
    }

    private void setChangeFlag(boolean changeFlag) {
        this.getPageCache().put("changeFlagKey", String.valueOf(changeFlag));
    }

    private String getMethodName() {
        return this.getPageCache().get("methodFlagKey");
    }

    private void setMethodName(String method) {
        this.getPageCache().put("methodFlagKey", method);
    }

    protected List<String> getUnCheckField() {
        List uncheckFieldList = super.getUnCheckField();
        uncheckFieldList.add("funcdescription");
        uncheckFieldList.add("format");
        uncheckFieldList.add("param");
        uncheckFieldList.add("example");
        uncheckFieldList.add("targettypegroup");
        uncheckFieldList.add(CREATE_ORG);
        return uncheckFieldList;
    }

    private TargetCondition getTargetCondition() {
        return (TargetCondition)this.getControl("targetconditionap");
    }

    private TargetResult getTargetResult() {
        return (TargetResult)this.getControl("targetresultap");
    }

    private TargetElse getTargetElse() {
        return (TargetElse)this.getControl("targetelseap");
    }

    static /* synthetic */ void access$000(TargetConfigPlugin x0, String x1) {
        x0.updateExpressionTextData(x1);
    }
}
