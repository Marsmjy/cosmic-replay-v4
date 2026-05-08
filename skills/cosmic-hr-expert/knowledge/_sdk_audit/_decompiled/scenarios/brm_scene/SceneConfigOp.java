/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.orm.query.QFilter
 *  kd.hr.brm.business.service.RuleEngineCacheService
 *  kd.hr.brm.business.util.KbaseAddUtil
 *  kd.hr.brm.business.web.SceneServiceHelper
 *  kd.hr.brm.common.enums.ParamTypeEnum
 *  kd.hr.brm.common.tools.RuleNamesTool
 *  kd.hr.brm.opplugin.validator.SceneValidator
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.brm.opplugin.web;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.orm.query.QFilter;
import kd.hr.brm.business.service.RuleEngineCacheService;
import kd.hr.brm.business.util.KbaseAddUtil;
import kd.hr.brm.business.web.SceneServiceHelper;
import kd.hr.brm.common.enums.ParamTypeEnum;
import kd.hr.brm.common.tools.RuleNamesTool;
import kd.hr.brm.opplugin.validator.SceneValidator;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class SceneConfigOp
extends HRDataBaseOp {
    private String operation = "donothing";

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        args.getFieldKeys().add("bizappid");
        args.getFieldKeys().add("iseditscene");
        args.getFieldKeys().add("sceneinputparams");
        args.getFieldKeys().add("sceneinputparams.inputnumber");
        args.getFieldKeys().add("sceneinputparams.inputname");
        args.getFieldKeys().add("sceneinputparams.inputparamstype");
        args.getFieldKeys().add("sceneinputparams.inputobject");
        args.getFieldKeys().add("sceneoutputparams");
        args.getFieldKeys().add("sceneoutputparams.outputnumber");
        args.getFieldKeys().add("sceneoutputparams.outputname");
        args.getFieldKeys().add("sceneoutputparams.outputparamstype");
        args.getFieldKeys().add("sceneoutputparams.outputobject");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new SceneValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        DynamicObject[] sceneDys = args.getDataEntities();
        switch (args.getOperationKey()) {
            case "save": {
                this.beforeSaveScene(args);
                break;
            }
            case "submit": {
                if (sceneDys.length != 1) break;
                this.beforeSaveScene(args);
                break;
            }
            case "delete": {
                this.operation = "delete";
                this.deleteKbase(sceneDys[0]);
                break;
            }
            case "disable": {
                this.operation = "disable";
                break;
            }
            case "enable": {
                this.operation = "enable";
                break;
            }
            default: {
                this.operation = "donothing";
            }
        }
        if (!HRStringUtils.equals((String)this.operation, (String)"donothing")) {
            this.logScene(sceneDys);
        }
    }

    private void beforeSaveScene(BeginOperationTransactionArgs args) {
        DynamicObject[] sceneDys = args.getDataEntities();
        if (sceneDys[0].getDataEntityState().getFromDatabase()) {
            this.operation = "modify";
            this.handleParamsForModify(sceneDys);
        } else {
            this.operation = "new";
            this.logParamsForNew(sceneDys);
        }
        RuleEngineCacheService.removeSceneCache((String)sceneDys[0].getString("number"));
        KbaseAddUtil.addKbase((DynamicObject)sceneDys[0]);
    }

    private void logScene(DynamicObject[] operateData) {
        HRBaseServiceHelper sceneHisHelper = new HRBaseServiceHelper("brm_scene_his");
        Date now = new Date();
        String operator = RequestContext.get().getUserId();
        DynamicObjectCollection hisCol = new DynamicObjectCollection();
        for (DynamicObject data : operateData) {
            DynamicObject sceneHisDy = sceneHisHelper.generateEmptyDynamicObject();
            sceneHisDy.set("scene", (Object)data.getLong("id"));
            sceneHisDy.set("number", (Object)data.getString("number"));
            sceneHisDy.set("name", data.get("name"));
            sceneHisDy.set("operate", (Object)this.operation);
            sceneHisDy.set("operatetime", (Object)now);
            sceneHisDy.set("operator", (Object)operator);
            hisCol.add((Object)sceneHisDy);
        }
        sceneHisHelper.save(hisCol);
    }

    private void logParamsForNew(DynamicObject[] operateData) {
        HRBaseServiceHelper sceneInputHelper = new HRBaseServiceHelper("brm_sceneinput_his");
        HRBaseServiceHelper sceneOutputHelper = new HRBaseServiceHelper("brm_sceneoutput_his");
        Date now = new Date();
        String operator = RequestContext.get().getUserId();
        DynamicObjectCollection inputHisCol = new DynamicObjectCollection();
        DynamicObjectCollection outputHisCol = new DynamicObjectCollection();
        for (DynamicObject data : operateData) {
            long sceneId = data.getLong("id");
            String sceneNumber = data.getString("number");
            DynamicObjectCollection inputParams = data.getDynamicObjectCollection("sceneinputparams");
            DynamicObjectCollection outputParams = data.getDynamicObjectCollection("sceneoutputparams");
            for (DynamicObject inputParam : inputParams) {
                DynamicObject inputDy = sceneInputHelper.generateEmptyDynamicObject();
                inputDy.set("scene", (Object)sceneId);
                inputDy.set("scenenumber", (Object)sceneNumber);
                inputDy.set("sceneinput", (Object)inputParam.getLong("id"));
                inputDy.set("number", (Object)inputParam.getString("inputnumber"));
                inputDy.set("name", inputParam.get("inputname"));
                inputDy.set("operate", (Object)this.operation);
                inputDy.set("operatetime", (Object)now);
                inputDy.set("operator", (Object)operator);
                inputHisCol.add((Object)inputDy);
            }
            for (DynamicObject outputParam : outputParams) {
                DynamicObject outputDy = sceneOutputHelper.generateEmptyDynamicObject();
                outputDy.set("scene", (Object)sceneId);
                outputDy.set("scenenumber", (Object)sceneNumber);
                outputDy.set("sceneoutput", (Object)outputParam.getLong("id"));
                outputDy.set("number", (Object)outputParam.getString("outputnumber"));
                outputDy.set("name", outputParam.get("outputname"));
                outputDy.set("operate", (Object)this.operation);
                outputDy.set("operatetime", (Object)now);
                outputDy.set("operator", (Object)operator);
                outputHisCol.add((Object)outputDy);
            }
        }
        sceneInputHelper.save(inputHisCol);
        sceneOutputHelper.save(outputHisCol);
    }

    private void handleParamsForModify(DynamicObject[] operateData) {
        DynamicObject data = operateData[0];
        SceneServiceHelper sceneHelper = new SceneServiceHelper();
        DynamicObject sceneDy = sceneHelper.loadOneScene(data.getLong("id"));
        DynamicObjectCollection inputParams = sceneDy.getDynamicObjectCollection("sceneinputparams");
        DynamicObjectCollection outputParams = sceneDy.getDynamicObjectCollection("sceneoutputparams");
        DynamicObjectCollection inputNewParams = new DynamicObjectCollection();
        DynamicObjectCollection inputModifyParams = new DynamicObjectCollection();
        DynamicObjectCollection inputDeleteParams = new DynamicObjectCollection();
        this.compareParams("input", inputParams, data.getDynamicObjectCollection("sceneinputparams"), inputNewParams, inputModifyParams, inputDeleteParams);
        DynamicObjectCollection outputNewParams = new DynamicObjectCollection();
        DynamicObjectCollection outputModifyParams = new DynamicObjectCollection();
        DynamicObjectCollection outputDeleteParams = new DynamicObjectCollection();
        this.compareParams("output", outputParams, data.getDynamicObjectCollection("sceneoutputparams"), outputNewParams, outputModifyParams, outputDeleteParams);
        HRBaseServiceHelper inputHisHelper = new HRBaseServiceHelper("brm_sceneinput_his");
        HRBaseServiceHelper outputHisHelper = new HRBaseServiceHelper("brm_sceneoutput_his");
        this.logParamsForModify("input", inputHisHelper, sceneDy, inputNewParams, inputModifyParams, inputDeleteParams);
        this.logParamsForModify("output", outputHisHelper, sceneDy, outputNewParams, outputModifyParams, outputDeleteParams);
    }

    private void compareParams(String paramType, DynamicObjectCollection originParams, DynamicObjectCollection currentParams, DynamicObjectCollection newParams, DynamicObjectCollection modifyParams, DynamicObjectCollection deleteParams) {
        Map originMap = originParams.stream().collect(Collectors.toMap(p1 -> p1.getString(paramType + "number"), Function.identity(), (x1, y1) -> y1));
        Map currentMap = currentParams.stream().collect(Collectors.toMap(p1 -> p1.getString(paramType + "number"), Function.identity(), (x1, y1) -> y1));
        for (DynamicObject currentParam : currentParams) {
            DynamicObject originParam = (DynamicObject)originMap.get(currentParam.getString(paramType + "number"));
            if (originParam == null) {
                newParams.add((Object)currentParam);
                continue;
            }
            if (!this.isChangedParam(paramType, originParam, currentParam)) continue;
            modifyParams.add((Object)currentParam);
        }
        for (DynamicObject originParam : originParams) {
            DynamicObject currentParam = (DynamicObject)currentMap.get(originParam.getString(paramType + "number"));
            if (currentParam != null) continue;
            deleteParams.add((Object)originParam);
        }
    }

    private boolean isChangedParam(String paramType, DynamicObject originParam, DynamicObject currentParam) {
        boolean result;
        boolean bl = result = !HRStringUtils.equals((String)originParam.getString(paramType + "number"), (String)currentParam.getString(paramType + "number")) || !HRStringUtils.equals((String)originParam.getString(paramType + "name"), (String)currentParam.getString(paramType + "name")) || !HRStringUtils.equals((String)originParam.getString(paramType + "paramstype"), (String)currentParam.getString(paramType + "paramstype"));
        if (result) {
            return true;
        }
        if (HRStringUtils.equals((String)ParamTypeEnum.DYNAMICOBJECT.getValue(), (String)currentParam.getString(paramType + "paramstype"))) {
            Object originObjId = originParam.getDynamicObject(paramType + "object").get("id");
            Object currentObjId = currentParam.getDynamicObject(paramType + "object").get("id");
            result = Objects.equals(originObjId, currentObjId);
        }
        return result;
    }

    private void logParamsForModify(String paramType, HRBaseServiceHelper hisHelper, DynamicObject sceneDy, DynamicObjectCollection newParams, DynamicObjectCollection modifyParams, DynamicObjectCollection deleteParams) {
        DynamicObject hisDy;
        DynamicObjectCollection hisCol = new DynamicObjectCollection();
        long sceneId = sceneDy.getLong("id");
        String sceneNumber = sceneDy.getString("number");
        Date now = new Date();
        String operator = RequestContext.get().getUserId();
        for (DynamicObject newParam : newParams) {
            hisDy = hisHelper.generateEmptyDynamicObject();
            this.buildParamDyByOperation(paramType, sceneId, sceneNumber, now, operator, hisDy, newParam, "new");
            hisCol.add((Object)hisDy);
        }
        for (DynamicObject modifyParam : modifyParams) {
            hisDy = hisHelper.generateEmptyDynamicObject();
            this.buildParamDyByOperation(paramType, sceneId, sceneNumber, now, operator, hisDy, modifyParam, "modify");
            hisCol.add((Object)hisDy);
        }
        for (DynamicObject deleteParam : deleteParams) {
            hisDy = hisHelper.generateEmptyDynamicObject();
            this.buildParamDyByOperation(paramType, sceneId, sceneNumber, now, operator, hisDy, deleteParam, "delete");
            hisCol.add((Object)hisDy);
        }
        hisHelper.save(hisCol);
    }

    private void buildParamDyByOperation(String paramType, long sceneId, String sceneNumber, Date now, String operator, DynamicObject inputHisDy, DynamicObject param, String operation) {
        inputHisDy.set("scene", (Object)sceneId);
        inputHisDy.set("scenenumber", (Object)sceneNumber);
        inputHisDy.set("number", (Object)param.getString(paramType + "number"));
        inputHisDy.set("name", (Object)param.getString(paramType + "name"));
        inputHisDy.set("operate", (Object)operation);
        inputHisDy.set("operatetime", (Object)now);
        inputHisDy.set("operator", (Object)operator);
    }

    private void deleteKbase(DynamicObject sceneDy) {
        String sceneNum = sceneDy.getString("number");
        String appNum = sceneDy.getDynamicObject("bizappid").getString("number");
        String simpleKey = RuleNamesTool.getSimpleKey((String)appNum, (String)sceneNum);
        HRBaseServiceHelper kbaseHelper = new HRBaseServiceHelper("brm_kbase");
        kbaseHelper.deleteByFilter(new QFilter[]{new QFilter("kbasekey", "=", (Object)simpleKey)});
    }
}
