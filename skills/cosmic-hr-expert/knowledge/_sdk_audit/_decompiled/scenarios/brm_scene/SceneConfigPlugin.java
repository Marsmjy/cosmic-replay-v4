/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.bill.BillShowParameter
 *  kd.bos.bill.OperationStatus
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.entity.OrmLocaleValue
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.form.CloseCallBack
 *  kd.bos.form.FormShowParameter
 *  kd.bos.form.IFormView
 *  kd.bos.form.ShowType
 *  kd.bos.form.control.EntryGrid
 *  kd.bos.form.events.BeforeDoOperationEventArgs
 *  kd.bos.form.events.ClosedCallBackEvent
 *  kd.bos.form.events.HyperLinkClickEvent
 *  kd.bos.form.events.HyperLinkClickListener
 *  kd.bos.form.operate.AbstractOperate
 *  kd.bos.form.operate.MutexHelper
 *  kd.bos.form.plugin.IFormPlugin
 *  kd.hr.brm.business.util.SceneParamRefTool
 *  kd.hr.brm.common.constants.SceneConstants
 *  kd.hr.brm.common.enums.ParamTypeEnum
 *  kd.hr.brm.formplugin.util.OperateLogUtil
 *  kd.hr.hbp.business.service.ruleengine.RuleEngineValidatorService
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.formplugin.web.HRDataBaseEdit
 */
package kd.hr.brm.formplugin.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.OrmLocaleValue;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.resource.promptenum.MultiLangEnumBridge;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.HyperLinkClickEvent;
import kd.bos.form.events.HyperLinkClickListener;
import kd.bos.form.operate.AbstractOperate;
import kd.bos.form.operate.MutexHelper;
import kd.bos.form.plugin.IFormPlugin;
import kd.hr.brm.business.util.SceneParamRefTool;
import kd.hr.brm.common.constants.SceneConstants;
import kd.hr.brm.common.enums.ParamTypeEnum;
import kd.hr.brm.formplugin.util.OperateLogUtil;
import kd.hr.hbp.business.service.ruleengine.RuleEngineValidatorService;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.formplugin.web.HRDataBaseEdit;

public final class SceneConfigPlugin
extends HRDataBaseEdit
implements SceneConstants,
HyperLinkClickListener {
    public void afterLoadData(EventObject e) {
        boolean isEditScene = (Boolean)this.getModel().getValue("iseditscene");
        OperationStatus status = this.getView().getFormShowParameter().getStatus();
        this.getView().getFormShowParameter().setStatus(isEditScene ? status : OperationStatus.VIEW);
    }

    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        OrmLocaleValue name = (OrmLocaleValue)this.getModel().getValue("name");
        if (name != null && StringUtils.isNotEmpty((CharSequence)name.getLocaleValue())) {
            this.getView().setEnable(Boolean.FALSE, new String[]{"number", "bizappid"});
            StringBuilder errorMsg = new StringBuilder();
            boolean isNotLocked = MutexHelper.require((IFormView)this.getView(), (String)"brm_scene", (Object)this.getModel().getDataEntity().getPkValue(), (String)"modify", (boolean)true, (StringBuilder)errorMsg);
            this.getPageCache().put("isNotLocked", String.valueOf(isNotLocked));
            this.getPageCache().put("sceneEnable", (String)this.getModel().getValue("enable"));
            DynamicObjectCollection outputParams = this.getModel().getEntryEntity("sceneoutputparams");
            DynamicObjectCollection inputParams = this.getModel().getEntryEntity("sceneinputparams");
            Set outputParamNums = outputParams.stream().map(dy -> dy.getString("outputnumber")).collect(Collectors.toSet());
            Set inputParamNums = inputParams.stream().map(dy -> dy.getString("inputnumber")).collect(Collectors.toSet());
            this.getPageCache().put("allOutputParamNumberExist", outputParamNums.isEmpty() ? "" : SerializationUtils.toJsonString(outputParamNums));
            this.getPageCache().put("allInputParamNumberExist", inputParamNums.isEmpty() ? "" : SerializationUtils.toJsonString(inputParamNums));
        } else {
            String defaultAppId = this.getView().getParentView().getPageCache().get("sceneDefaultAppId");
            if (defaultAppId != null && this.getModel().getValue("bizappid") == null) {
                this.getModel().setValue("bizappid", (Object)defaultAppId);
            }
        }
    }

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        String operateKey;
        super.beforeDoOperation(args);
        if (!(args.getSource() instanceof AbstractOperate)) {
            return;
        }
        AbstractOperate op = (AbstractOperate)args.getSource();
        switch (operateKey = op.getOperateKey()) {
            case "do_addinput": {
                this.openAddParamsDialog("brm_sceneinput", "sceneinputparams", "addinput_finish");
                args.setCancel(true);
                break;
            }
            case "do_addoutput": {
                this.openAddParamsDialog("brm_sceneoutput", "sceneoutputparams", "addoutput_finish");
                args.setCancel(true);
                break;
            }
            case "deleteinputparams": {
                this.validInputParamForDel(args);
                break;
            }
            case "deleteoutputparams": {
                this.validOutParamForDel(args);
                break;
            }
            case "save": 
            case "submit": {
                if (RuleEngineValidatorService.validateSceneNumber((String)((String)this.getModel().getValue("number")))) break;
                OperateLogUtil.showTipAndWriteLog((IFormView)this.getView(), (BeforeDoOperationEventArgs)args, (MultiLangEnumBridge)new MultiLangEnumBridge("\u573a\u666f\u7f16\u7801\u4e0d\u5408\u6cd5\uff0c\u5efa\u8bae\u7531\u5b57\u6bcd\u5f00\u5934\uff0c\u5e76\u4e0e\u4e0b\u5212\u7ebf\u3001\u6570\u5b57\u7ec4\u5408\u3002", "SceneConfigPlugin_0", "hrmp-brm-formplugin"), (String[])new String[0]);
                args.setCancel(true);
                return;
            }
        }
    }

    public void registerListener(EventObject e) {
        super.registerListener(e);
        EntryGrid entryGrid = (EntryGrid)this.getView().getControl("sceneinputparams");
        entryGrid.addHyperClickListener((HyperLinkClickListener)this);
        EntryGrid outGrid = (EntryGrid)this.getView().getControl("sceneoutputparams");
        outGrid.addHyperClickListener((HyperLinkClickListener)this);
    }

    public void hyperLinkClick(HyperLinkClickEvent e) {
        String fieldName = e.getFieldName();
        if (StringUtils.equals((CharSequence)fieldName, (CharSequence)"inputnumber")) {
            this.openModifyParamsDialog(e.getRowIndex(), "brm_sceneinput", "sceneinputparams", "addinput_finish");
        } else {
            this.openModifyParamsDialog(e.getRowIndex(), "brm_sceneoutput", "sceneoutputparams", "addoutput_finish");
        }
    }

    private void openAddParamsDialog(String formId, String entryName, String callbackId) {
        int rowCount = this.getModel().getEntryRowCount(entryName);
        this.getPageCache().remove("saveForEdit");
        BillShowParameter parameter = new BillShowParameter();
        parameter.setFormId(formId);
        parameter.setCustomParam("isEdit", (Object)Boolean.FALSE);
        parameter.setCustomParam("entityNumber", (Object)formId);
        parameter.setCustomParam("openType", (Object)"ADD");
        parameter.setCustomParam("sceneid", (Object)this.getModel().getDataEntity().getLong("id"));
        parameter.setCustomParam("index", (Object)(rowCount + 1));
        if (HRStringUtils.equals((String)formId, (String)"brm_sceneinput")) {
            Map<String, List<String>> inputParams = this.getAllInputParam();
            Map<String, String> inputParamsMap = this.getAllInputParamMap(inputParams.get("allInputParamName"), inputParams.get("allInputParamNumber"));
            parameter.setCustomParam("allInputParamName", (Object)inputParamsMap.get("allInputParamName"));
            parameter.setCustomParam("allInputParamNumber", (Object)inputParamsMap.get("allInputParamNumber"));
        } else {
            Map<String, List<String>> allOutputParam = this.getAllOutputParam();
            Map<String, String> outputParamsMap = this.getAllOutputParamMap(allOutputParam.get("allOutputParamName"), allOutputParam.get("allOutputParamNumber"));
            parameter.setCustomParam("allOutputParamName", (Object)outputParamsMap.get("allOutputParamName"));
            parameter.setCustomParam("allOutputParamNumber", (Object)outputParamsMap.get("allOutputParamNumber"));
        }
        parameter.getOpenStyle().setShowType(ShowType.Modal);
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, callbackId);
        parameter.setCloseCallBack(closeCallBack);
        this.getView().showForm((FormShowParameter)parameter);
    }

    private void openModifyParamsDialog(int currentIndex, String formId, String entryName, String callbackId) {
        boolean isSysPre;
        DynamicObject currentRowDy = (DynamicObject)this.getModel().getEntryEntity(entryName).get(currentIndex);
        BillShowParameter parameter = new BillShowParameter();
        parameter.setFormId(formId);
        parameter.setCustomParam("isEdit", (Object)Boolean.TRUE);
        parameter.setCustomParam("entryName", (Object)entryName);
        parameter.setCustomParam("entityNumber", (Object)formId);
        if (HRStringUtils.endsWithIgnoreCase((String)entryName, (String)"sceneinputparams")) {
            isSysPre = currentRowDy.getBoolean("inputissyspreset");
            parameter.setCustomParam("number", (Object)currentRowDy.getString("inputnumber"));
            parameter.setCustomParam("name", currentRowDy.get("inputname"));
            parameter.setCustomParam("paramstype", (Object)currentRowDy.getString("inputparamstype"));
            DynamicObject inputObject = currentRowDy.getDynamicObject("inputobject");
            if (inputObject != null) {
                parameter.setCustomParam("paramsobject", (Object)inputObject.getString("id"));
            }
            Map<String, List<String>> allInputParam = this.getAllInputParam();
            allInputParam.get("allInputParamName").remove(currentRowDy.getString("inputname"));
            allInputParam.get("allInputParamNumber").remove(currentRowDy.getString("inputnumber"));
            Map<String, String> inputParamMap = this.getAllInputParamMap(allInputParam.get("allInputParamName"), allInputParam.get("allInputParamNumber"));
            parameter.setCustomParam("allInputParamName", (Object)inputParamMap.get("allInputParamName"));
            parameter.setCustomParam("allInputParamNumber", (Object)inputParamMap.get("allInputParamNumber"));
            parameter.setCustomParam("ioType", (Object)"input");
            parameter.setCustomParam("combofield", currentRowDy.get("inputcombo"));
            parameter.setCustomParam("multiple", (Object)currentRowDy.getString("inputmultiple"));
            parameter.setCustomParam("dateformat", (Object)currentRowDy.getString("inputdateformat"));
            parameter.setCustomParam("dynprop", (Object)currentRowDy.getString("inputdynprop"));
            parameter.setCustomParam("treelv", (Object)currentRowDy.getString("inputtreelv"));
        } else {
            isSysPre = currentRowDy.getBoolean("outputissyspreset");
            parameter.setCustomParam("number", (Object)currentRowDy.getString("outputnumber"));
            parameter.setCustomParam("name", currentRowDy.get("outputname"));
            parameter.setCustomParam("paramstype", (Object)currentRowDy.getString("outputparamstype"));
            DynamicObject outputObject = currentRowDy.getDynamicObject("outputobject");
            if (outputObject != null) {
                parameter.setCustomParam("paramsobject", (Object)outputObject.getString("id"));
            }
            Map<String, List<String>> allOutputParam = this.getAllOutputParam();
            allOutputParam.get("allOutputParamName").remove(currentRowDy.getString("outputname"));
            allOutputParam.get("allOutputParamNumber").remove(currentRowDy.getString("outputnumber"));
            Map<String, String> outputParamMap = this.getAllOutputParamMap(allOutputParam.get("allOutputParamName"), allOutputParam.get("allOutputParamNumber"));
            parameter.setCustomParam("allOutputParamName", (Object)outputParamMap.get("allOutputParamName"));
            parameter.setCustomParam("allOutputParamNumber", (Object)outputParamMap.get("allOutputParamNumber"));
            parameter.setCustomParam("ioType", (Object)"output");
            parameter.setCustomParam("combofield", currentRowDy.get("outputcombo"));
            parameter.setCustomParam("multiple", (Object)currentRowDy.getString("outputmultiple"));
            parameter.setCustomParam("dateformat", (Object)currentRowDy.getString("outputdateformat"));
            parameter.setCustomParam("dynprop", (Object)currentRowDy.getString("outputdynprop"));
            parameter.setCustomParam("treelv", (Object)"2");
        }
        parameter.setCustomParam("sceneid", (Object)this.getModel().getDataEntity().getLong("id"));
        parameter.setCustomParam("sceneEnable", (Object)this.getPageCache().get("sceneEnable"));
        parameter.getOpenStyle().setShowType(ShowType.Modal);
        if (this.isPageView()) {
            parameter.setStatus(OperationStatus.VIEW);
        } else {
            boolean isEditScene = (Boolean)this.getModel().getValue("iseditscene");
            if (!isEditScene) {
                parameter.setStatus(OperationStatus.VIEW);
            } else if (isSysPre) {
                parameter.setCustomParam("isSys", (Object)Boolean.TRUE.toString());
            }
        }
        CloseCallBack closeCallBack = new CloseCallBack((IFormPlugin)this, callbackId);
        parameter.setCloseCallBack(closeCallBack);
        this.getView().showForm((FormShowParameter)parameter);
        this.getPageCache().put("needToEditLine", String.valueOf(currentIndex));
        this.getPageCache().put("saveForEdit", "true");
    }

    private boolean isPageView() {
        String isNotLocked = this.getPageCache().get("isNotLocked");
        if (isNotLocked != null && HRStringUtils.equals((String)isNotLocked, (String)"false")) {
            return true;
        }
        String isAudit = this.getPageCache().get("isSceneAudit");
        if (isAudit == null) {
            boolean audit = HRBaseDataConfigUtil.getAudit((String)"brm_scene");
            isAudit = String.valueOf(audit);
            this.getPageCache().put("isSceneAudit", isAudit);
        }
        if (HRStringUtils.equals((String)isAudit, (String)"true")) {
            return !HRStringUtils.equals((String)((String)this.getModel().getValue("status")), (String)"A");
        }
        return false;
    }

    private void processViewForInputParams(DynamicObject returnData, boolean isEdit) {
        int rowCount;
        if (isEdit) {
            boolean presetIsEdit;
            rowCount = Integer.parseInt(this.getPageCache().get("needToEditLine"));
            this.getPageCache().remove("saveForEdit");
            this.getPageCache().remove("needToEditLine");
            DynamicObject currentRowDy = (DynamicObject)this.getModel().getEntryEntity("sceneinputparams").get(rowCount);
            boolean bl = presetIsEdit = ParamTypeEnum.DYNAMICOBJECT.getValue().equals(returnData.get("paramstype")) && currentRowDy.getBoolean("inputissyspreset") && returnData.getBoolean("presetisedit");
            if (presetIsEdit) {
                this.getModel().setValue("inputpresetisedit", (Object)returnData.getBoolean("presetisedit"), rowCount);
            }
        } else {
            rowCount = this.getModel().getEntryRowCount("sceneinputparams");
            this.getModel().batchCreateNewEntryRow("sceneinputparams", 1);
        }
        this.getModel().setValue("inputnumber", (Object)returnData.getString("number"), rowCount);
        this.getModel().setValue("inputname", (Object)returnData.getLocaleString("name"), rowCount);
        this.getModel().setValue("inputparamstype", returnData.get("paramstype"), rowCount);
        this.getModel().setValue("inputcombo", returnData.get("combofield"), rowCount);
        this.getModel().setValue("inputdateformat", returnData.get("dateformat"), rowCount);
        this.getModel().setValue("inputdynprop", returnData.get("dynprop"), rowCount);
        this.getModel().setValue("inputtreelv", returnData.get("treelv"), rowCount);
        if (ParamTypeEnum.BASEDATA.getValue().equals(returnData.get("paramstype")) || ParamTypeEnum.ENUM.getValue().equals(returnData.get("paramstype"))) {
            this.getModel().setValue("inputmultiple", (Object)(returnData.getBoolean("multiple") ? "1" : "2"), rowCount);
        } else {
            this.getModel().setValue("inputmultiple", (Object)"0", rowCount);
        }
        this.setParamsValueForInput(returnData, rowCount);
    }

    private void processViewForOutputParams(DynamicObject returnData, boolean isEdit) {
        int rowCount;
        if (isEdit) {
            boolean presetIsEdit;
            rowCount = Integer.parseInt(this.getPageCache().get("needToEditLine"));
            this.getPageCache().remove("needToEditLine");
            this.getPageCache().remove("saveForEdit");
            DynamicObject currentRowDy = (DynamicObject)this.getModel().getEntryEntity("sceneoutputparams").get(rowCount);
            boolean bl = presetIsEdit = ParamTypeEnum.DYNAMICOBJECT.getValue().equals(returnData.get("paramstype")) && currentRowDy.getBoolean("outputissyspreset") && returnData.getBoolean("presetisedit");
            if (presetIsEdit) {
                this.getModel().setValue("outputpresetisedit", (Object)returnData.getBoolean("presetisedit"), rowCount);
            }
        } else {
            rowCount = this.getModel().getEntryRowCount("sceneoutputparams");
            this.getModel().batchCreateNewEntryRow("sceneoutputparams", 1);
        }
        this.getModel().setValue("outputnumber", (Object)returnData.getString("number"), rowCount);
        this.getModel().setValue("outputname", (Object)returnData.getLocaleString("name"), rowCount);
        this.getModel().setValue("outputparamstype", returnData.get("paramstype"), rowCount);
        this.getModel().setValue("outputcombo", returnData.get("combofield"), rowCount);
        this.getModel().setValue("outputdateformat", returnData.get("dateformat"), rowCount);
        this.getModel().setValue("outputdynprop", returnData.get("dynprop"), rowCount);
        if (ParamTypeEnum.BASEDATA.getValue().equals(returnData.get("paramstype")) || ParamTypeEnum.ENUM.getValue().equals(returnData.get("paramstype"))) {
            this.getModel().setValue("outputmultiple", (Object)(returnData.getBoolean("multiple") ? "1" : "2"), rowCount);
        } else {
            this.getModel().setValue("outputmultiple", (Object)"0", rowCount);
        }
        this.setParamsValueForOutput(returnData, rowCount);
    }

    private void setParamsValueForInput(DynamicObject data, int rowCount) {
        if (HRStringUtils.equals((String)data.getString("paramstype"), (String)ParamTypeEnum.DYNAMICOBJECT.getValue())) {
            this.getModel().setValue("inputobject", data.get("paramsobject"), rowCount);
        } else if (HRStringUtils.equals((String)data.getString("paramstype"), (String)ParamTypeEnum.BASEDATA.getValue())) {
            this.getModel().setValue("inputobject", data.get("basedatafield"), rowCount);
        }
    }

    private void setParamsValueForOutput(DynamicObject data, int rowCount) {
        if (HRStringUtils.equals((String)data.getString("paramstype"), (String)ParamTypeEnum.DYNAMICOBJECT.getValue())) {
            this.getModel().setValue("outputobject", data.get("paramsobject"), rowCount);
        } else if (HRStringUtils.equals((String)data.getString("paramstype"), (String)ParamTypeEnum.BASEDATA.getValue())) {
            this.getModel().setValue("outputobject", data.get("basedatafield"), rowCount);
        }
    }

    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        if (StringUtils.equals((CharSequence)"addinput_finish", (CharSequence)closedCallBackEvent.getActionId()) && closedCallBackEvent.getReturnData() != null) {
            this.processViewForInputParams((DynamicObject)closedCallBackEvent.getReturnData(), Boolean.parseBoolean(this.getPageCache().get("saveForEdit")));
        } else if (StringUtils.equals((CharSequence)"addoutput_finish", (CharSequence)closedCallBackEvent.getActionId()) && closedCallBackEvent.getReturnData() != null) {
            this.processViewForOutputParams((DynamicObject)closedCallBackEvent.getReturnData(), Boolean.parseBoolean(this.getPageCache().get("saveForEdit")));
        }
    }

    private Map<String, List<String>> getAllInputParam() {
        DynamicObjectCollection inputParams = this.getModel().getEntryEntity("sceneinputparams");
        HashMap result = Maps.newHashMapWithExpectedSize((int)3);
        ArrayList inputNames = Lists.newArrayListWithCapacity((int)16);
        ArrayList inputNumbers = Lists.newArrayListWithCapacity((int)16);
        inputParams.forEach(param -> {
            inputNames.add(param.getString("inputname"));
            inputNumbers.add(param.getString("inputnumber"));
        });
        result.put("allInputParamName", inputNames);
        result.put("allInputParamNumber", inputNumbers);
        return result;
    }

    private Map<String, String> getAllInputParamMap(List<String> paramNames, List<String> paramNumbers) {
        HashMap result = Maps.newHashMapWithExpectedSize((int)4);
        result.put("allInputParamName", SerializationUtils.toJsonString(paramNames));
        result.put("allInputParamNumber", SerializationUtils.toJsonString(paramNumbers));
        return result;
    }

    private Map<String, List<String>> getAllOutputParam() {
        DynamicObjectCollection outputParams = this.getModel().getEntryEntity("sceneoutputparams");
        HashMap result = Maps.newHashMapWithExpectedSize((int)3);
        ArrayList outputNames = Lists.newArrayListWithCapacity((int)16);
        ArrayList outputNumbers = Lists.newArrayListWithCapacity((int)16);
        outputParams.forEach(param -> {
            outputNames.add(param.getString("outputname"));
            outputNumbers.add(param.getString("outputnumber"));
        });
        result.put("allOutputParamName", outputNames);
        result.put("allOutputParamNumber", outputNumbers);
        return result;
    }

    private Map<String, String> getAllOutputParamMap(List<String> paramNames, List<String> paramNumbers) {
        HashMap result = Maps.newHashMapWithExpectedSize((int)3);
        result.put("allOutputParamName", SerializationUtils.toJsonString(paramNames));
        result.put("allOutputParamNumber", SerializationUtils.toJsonString(paramNumbers));
        return result;
    }

    private void validInputParamForDel(BeforeDoOperationEventArgs args) {
        EntryGrid entryGrid = (EntryGrid)this.getView().getControl("sceneinputparams");
        int[] selectRows = entryGrid.getSelectRows();
        String ioType = "input";
        HashMap paramNumMap = Maps.newHashMapWithExpectedSize((int)16);
        for (int selectRow : selectRows) {
            String paramNum = (String)this.getModel().getValue("inputnumber", selectRow);
            paramNumMap.put(selectRow, paramNum);
        }
        StringBuilder errMsg = new StringBuilder();
        List<Integer> isNotRefIndexList = this.validateParamRef(ioType, paramNumMap, args, errMsg);
        List<Integer> isNotSysIndexList = this.validateSys(true, selectRows, errMsg);
        if (errMsg.length() > 2) {
            String msgContent = errMsg.substring(0, errMsg.length() - 3);
            String msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u3002", (String)"SceneConfigPlugin_2", (String)"hrmp-brm-formplugin", (Object[])new Object[0]), msgContent);
            this.getView().showTipNotification(msg);
        }
        this.removeRows(isNotRefIndexList, isNotSysIndexList, ioType);
    }

    private void validOutParamForDel(BeforeDoOperationEventArgs args) {
        EntryGrid entryGrid = (EntryGrid)this.getView().getControl("sceneoutputparams");
        int[] selectRows = entryGrid.getSelectRows();
        HashMap paramNumMap = Maps.newHashMapWithExpectedSize((int)16);
        for (int selectRow : selectRows) {
            String paramNum = (String)this.getModel().getValue("outputnumber", selectRow);
            paramNumMap.put(selectRow, paramNum);
        }
        String ioType = "output";
        StringBuilder errMsg = new StringBuilder();
        List<Integer> isNotRefIndexList = this.validateParamRef(ioType, paramNumMap, args, errMsg);
        List<Integer> isNotSysIndexList = this.validateSys(false, selectRows, errMsg);
        if (errMsg.length() > 2) {
            String msgContent = errMsg.substring(0, errMsg.length() - 3);
            String msg = String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u3002", (String)"SceneConfigPlugin_2", (String)"hrmp-brm-formplugin", (Object[])new Object[0]), msgContent);
            this.getView().showTipNotification(msg);
        }
        this.removeRows(isNotRefIndexList, isNotSysIndexList, ioType);
    }

    private List<Integer> validateParamRef(String ioType, Map<Integer, String> paramNumMap, BeforeDoOperationEventArgs args, StringBuilder errMsg) {
        StringBuilder errorMsgSb = new StringBuilder();
        Long sceneId = (Long)this.getModel().getDataEntity().getPkValue();
        ArrayList isRefIndexList = Lists.newArrayListWithCapacity((int)10);
        ArrayList isNotRefIndexList = Lists.newArrayListWithCapacity((int)10);
        SceneParamRefTool.isParamReferred((Long)sceneId, paramNumMap, (String)ioType, (StringBuilder)errorMsgSb, (List)isRefIndexList, (List)isNotRefIndexList);
        if (!isRefIndexList.isEmpty()) {
            errMsg.append((CharSequence)errorMsgSb).append("\r\n");
        }
        args.setCancel(true);
        return isNotRefIndexList;
    }

    private List<Integer> validateSys(boolean isInput, int[] selectRows, StringBuilder errMsg) {
        ArrayList isNotSysIndexList = Lists.newArrayListWithCapacity((int)10);
        String sys = isInput ? "inputissyspreset" : "outputissyspreset";
        String number = isInput ? "inputnumber" : "outputnumber";
        for (int selectRow : selectRows) {
            Boolean isSys = (Boolean)this.getModel().getValue(sys, selectRow);
            if (!isSys.booleanValue()) {
                isNotSysIndexList.add(selectRow);
                continue;
            }
            errMsg.append(ResManager.loadKDString((String)"\u53c2\u6570%s\u4e3a\u7cfb\u7edf\u9884\u7f6e\u53c2\u6570\u4e0d\u53ef\u5220\u9664\uff1b\r\n", (String)"SceneConfigPlugin_1", (String)"hrmp-brm-formplugin", (Object[])new Object[]{this.getModel().getValue(number, selectRow)}));
        }
        return isNotSysIndexList;
    }

    private void removeRows(List<Integer> isNotRefIndexList, List<Integer> isNotSysIndexList, String ioType) {
        if (isNotRefIndexList.isEmpty() || isNotSysIndexList.isEmpty()) {
            return;
        }
        ArrayList removeRowSet = Lists.newArrayListWithExpectedSize((int)isNotRefIndexList.size());
        for (Integer isNotRefIndex : isNotRefIndexList) {
            if (!isNotSysIndexList.contains(isNotRefIndex)) continue;
            removeRowSet.add(isNotRefIndex);
        }
        int[] delRows = new int[removeRowSet.size()];
        for (int i = 0; i < removeRowSet.size(); ++i) {
            delRows[i] = (Integer)removeRowSet.get(i);
        }
        if (HRStringUtils.equals((String)ioType, (String)"output")) {
            this.getModel().deleteEntryRows("sceneoutputparams", delRows);
        } else {
            this.getModel().deleteEntryRows("sceneinputparams", delRows);
        }
    }
}
