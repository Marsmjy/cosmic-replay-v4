/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.brm.business.service.RuleEngineCacheService
 *  kd.hr.brm.business.service.TargetApiServiceHelper
 *  kd.hr.brm.business.util.MessageUtil
 *  kd.hr.brm.business.web.RuleServiceHelper
 *  kd.hr.brm.business.web.RuleTransferService
 *  kd.hr.brm.business.web.TargetHelper
 *  kd.hr.brm.common.tools.RuleNamesTool
 *  kd.hr.brm.opplugin.validator.TargetValidator
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.brm.opplugin.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.brm.business.service.RuleEngineCacheService;
import kd.hr.brm.business.service.TargetApiServiceHelper;
import kd.hr.brm.business.util.MessageUtil;
import kd.hr.brm.business.web.RuleServiceHelper;
import kd.hr.brm.business.web.RuleTransferService;
import kd.hr.brm.business.web.TargetHelper;
import kd.hr.brm.common.tools.RuleNamesTool;
import kd.hr.brm.opplugin.validator.TargetValidator;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class TargetLogOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(TargetLogOp.class);
    private String operation = "donothing";

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new TargetValidator());
    }

    public void onPreparePropertys(PreparePropertysEventArgs trans) {
        trans.getFieldKeys().add("targettypegroup");
        trans.getFieldKeys().add("id");
        trans.getFieldKeys().add("name");
        trans.getFieldKeys().add("bizapp");
        trans.getFieldKeys().add("scene");
        trans.getFieldKeys().add("conditions");
        trans.getFieldKeys().add("results");
        trans.getFieldKeys().add("elses");
        trans.getFieldKeys().add("returnType");
        trans.getFieldKeys().add("kbaseKey");
        trans.getFieldKeys().add("status");
        trans.getFieldKeys().add("enable");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        List<DynamicObject> conditionTargets = Arrays.stream(args.getDataEntities()).filter(targetDy -> targetDy.getString("targettypegroup").equals("condition")).collect(Collectors.toList());
        List functionTargets = Arrays.stream(args.getDataEntities()).filter(targetDy -> targetDy.getString("targettypegroup").equals("function")).collect(Collectors.toList());
        this.doConditionTarget(args, conditionTargets);
        for (DynamicObject functionTarget : functionTargets) {
            RuleEngineCacheService.updateTargetCache((Long)functionTarget.getLong("id"));
        }
        ArrayList changeTargets = Lists.newArrayListWithExpectedSize((int)(functionTargets.size() + args.getDataEntities().length));
        changeTargets.addAll(functionTargets);
        changeTargets.addAll(Arrays.asList(args.getDataEntities()));
        args.setDataEntities(changeTargets.toArray(new DynamicObject[0]));
    }

    private void doConditionTarget(BeginOperationTransactionArgs args, List<DynamicObject> conditionTarget) {
        args.setDataEntities(conditionTarget.toArray(new DynamicObject[0]));
        if (conditionTarget.isEmpty()) {
            return;
        }
        switch (args.getOperationKey()) {
            case "save": {
                this.beforeSaveConditionTarget(args, conditionTarget);
                break;
            }
            case "submit": {
                if (conditionTarget.size() != 1) break;
                this.beforeSaveConditionTarget(args, conditionTarget);
                break;
            }
            case "delete": {
                this.operation = "delete";
                this.deleteOrDisableTarget(args, conditionTarget);
                RuleEngineCacheService.batchUpdateTargetCache((Collection)conditionTarget.stream().map(dy -> dy.getLong("id")).collect(Collectors.toList()));
                break;
            }
            case "disable": {
                this.operation = "disable";
                this.deleteOrDisableTarget(args, conditionTarget);
                RuleEngineCacheService.batchUpdateTargetCache((Collection)conditionTarget.stream().map(dy -> dy.getLong("id")).collect(Collectors.toList()));
                break;
            }
            case "enable": {
                this.operation = "enable";
                this.addOrEnableTarget(args, conditionTarget);
                break;
            }
            case "audit": {
                this.operation = "audit";
                this.addOrEnableTarget(args, conditionTarget);
                break;
            }
            default: {
                this.operation = "donothing";
            }
        }
        if (!HRStringUtils.equals((String)this.operation, (String)"donothing")) {
            this.targetLog(args, conditionTarget);
        }
    }

    private void beforeSaveConditionTarget(BeginOperationTransactionArgs args, List<DynamicObject> conditionTarget) {
        if (conditionTarget.get(0).getDataEntityState().getFromDatabase()) {
            this.operation = "modify";
            DynamicObject modifyTarget = conditionTarget.get(0);
            if (modifyTarget.getBoolean("enable") && this.isRecompile(modifyTarget)) {
                this.addOrEnableTarget(args, conditionTarget);
                RuleEngineCacheService.updateTargetCache((Long)modifyTarget.getLong("id"));
            } else if (this.isNameChange(modifyTarget)) {
                this.saveTargetName(modifyTarget);
            }
        } else {
            this.operation = "new";
            this.addOrEnableTarget(args, conditionTarget);
        }
    }

    public void deleteOrDisableTarget(BeginOperationTransactionArgs args, List<DynamicObject> conditionTarget) {
        if (conditionTarget.isEmpty()) {
            return;
        }
        Set deleteTargetIds = conditionTarget.stream().map(dy -> dy.getLong("id")).collect(Collectors.toSet());
        TargetHelper targetHelper = new TargetHelper();
        targetHelper.deleteTarget(deleteTargetIds);
        HashMap deleteTargetMap = Maps.newHashMapWithExpectedSize((int)20);
        for (DynamicObject designTarget : conditionTarget) {
            Long id = designTarget.getLong("id");
            String bizApp = designTarget.getDynamicObject("bizapp").getString("number");
            String scene = designTarget.getDynamicObject("scene").getString("number");
            String simpleKey = RuleNamesTool.getSimpleKey((String)bizApp, (String)scene);
            this.putTargetMap(deleteTargetMap, simpleKey, id);
        }
        this.setBroadcast(args, null, deleteTargetMap);
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        this.broadcastAfterOp();
    }

    private boolean isRecompile(DynamicObject newTarget) {
        String updateFileds = "bizapp,scene,returntype,conditions,results,elses";
        HRBaseServiceHelper helper = new HRBaseServiceHelper("brm_target");
        DynamicObject oldTarget = helper.queryOne(updateFileds, newTarget.get("id"));
        for (String filed : updateFileds.split(",")) {
            if (newTarget.getString(filed).equals(oldTarget.getString(filed))) continue;
            return true;
        }
        return false;
    }

    private boolean isNameChange(DynamicObject newTarget) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper("brm_target");
        DynamicObject oldTarget = helper.queryOne(newTarget.get("id"));
        return !newTarget.getString("name").equals(oldTarget.getString("name"));
    }

    private void saveTargetName(DynamicObject designTarget) {
        DynamicObject targetRunTime = RuleServiceHelper.queryRuntimeRule((Long)designTarget.getLong("id"));
        if (targetRunTime == null) {
            return;
        }
        targetRunTime.set("rulename", (Object)designTarget.getString("name"));
        targetRunTime.set("modifier", (Object)RequestContext.get().getCurrUserId());
        targetRunTime.set("modifytime", (Object)new Date());
        HRBaseServiceHelper helper = new HRBaseServiceHelper("brm_ruleruntime");
        helper.saveOne(targetRunTime);
    }

    private void broadcastAfterOp() {
        if (!this.getOption().containsVariable("needBroadcast")) {
            return;
        }
        if (!Boolean.parseBoolean(this.getOption().getVariableValue("needBroadcast"))) {
            return;
        }
        if (this.getOption().containsVariable("newAndModifyIdList")) {
            String newAndModifyIdStr = this.getOption().getVariableValue("newAndModifyIdList");
            MessageUtil.updateTargets((String)newAndModifyIdStr);
        }
        if (this.getOption().containsVariable("deleteIdList")) {
            String deleteIdStr = this.getOption().getVariableValue("deleteIdList");
            MessageUtil.removeTargets((String)deleteIdStr);
        }
    }

    private void setBroadcast(BeginOperationTransactionArgs args, Map<String, List<Long>> addTargetMap, Map<String, List<Long>> deleteTargetMap) {
        this.getOption().setVariableValue("needBroadcast", String.valueOf(true));
        if (null != addTargetMap && !addTargetMap.isEmpty()) {
            this.getOption().setVariableValue("newAndModifyIdList", SerializationUtils.toJsonString(addTargetMap));
        }
        if (null != deleteTargetMap && !deleteTargetMap.isEmpty()) {
            this.getOption().setVariableValue("deleteIdList", SerializationUtils.toJsonString(deleteTargetMap));
        }
    }

    private void showErrorNotification(String errorMessage) {
        this.operationResult.setMessage(errorMessage);
        this.operationResult.setShowMessage(true);
        this.operationResult.setSuccess(false);
    }

    private void targetLog(BeginOperationTransactionArgs args, List<DynamicObject> conditionTarget) {
        if (conditionTarget == null || conditionTarget.isEmpty()) {
            return;
        }
        HRBaseServiceHelper targetHelper = new HRBaseServiceHelper("brm_target_his");
        Date now = new Date();
        String operator = RequestContext.get().getUserId();
        DynamicObjectCollection hisCol = new DynamicObjectCollection();
        for (DynamicObject data : conditionTarget) {
            DynamicObject targetHisDy = targetHelper.generateEmptyDynamicObject();
            targetHisDy.set("target", (Object)data.getLong("id"));
            targetHisDy.set("number", (Object)data.getString("number"));
            targetHisDy.set("name", data.get("name"));
            targetHisDy.set("operate", (Object)this.operation);
            targetHisDy.set("operatetime", (Object)now);
            targetHisDy.set("operator", (Object)operator);
            hisCol.add((Object)targetHisDy);
        }
        targetHelper.save(hisCol);
    }

    private void addOrEnableTarget(BeginOperationTransactionArgs args, List<DynamicObject> conditionTarget) {
        if (conditionTarget.isEmpty()) {
            return;
        }
        TargetApiServiceHelper.handleTargetParam(conditionTarget);
        HashMap needValidRuntimeTargets = Maps.newHashMapWithExpectedSize((int)16);
        HashMap addTargetMap = Maps.newHashMapWithExpectedSize((int)20);
        Set<Long> addTargetIds = conditionTarget.stream().map(dy -> dy.getLong("id")).collect(Collectors.toSet());
        String template = "/template/normalTarget";
        for (DynamicObject targetObject : conditionTarget) {
            String status = targetObject.getString("status");
            String enable = targetObject.getString("enable");
            if (!HRStringUtils.equals((String)status, (String)"C") || !HRStringUtils.equals((String)enable, (String)"1")) continue;
            try {
                String ruleContent = RuleTransferService.getTargetRuleRuntimeContent((DynamicObject)targetObject, (String)template);
                DynamicObject runtimeTarget = this.saveTargetRuntimeCol(ruleContent, targetObject, addTargetMap);
                String kbaseKey = runtimeTarget.getString("kbaseKey");
                DynamicObjectCollection runtimeTargetCol = needValidRuntimeTargets.getOrDefault(kbaseKey, new DynamicObjectCollection());
                runtimeTargetCol.add((Object)runtimeTarget);
                needValidRuntimeTargets.put(kbaseKey, runtimeTargetCol);
            }
            catch (Exception exception) {
                LOGGER.error((Throwable)exception);
                this.showErrorNotification(ResManager.loadKDString((String)"\u6307\u6807\u64cd\u4f5c\u5931\u8d25\uff0c\u89c4\u5219\u8f6c\u6362\u9519\u8bef\u3002", (String)"TargetLogOp_0", (String)"hrmp-brm-opplugin", (Object[])new Object[0]));
                addTargetIds.remove(targetObject.getLong("id"));
            }
        }
        if (needValidRuntimeTargets.size() > 0) {
            try {
                this.preCompileTargets(needValidRuntimeTargets);
            }
            catch (KDBizException ex) {
                LOGGER.error((Throwable)ex);
                this.showErrorNotification(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25\uff0c\u89c4\u5219\u914d\u7f6e\u6709\u8bef\u3002", (String)"TargetLogOp_1", (String)"hrmp-brm-opplugin", (Object[])new Object[0]));
                args.setCancelOperation(true);
            }
        }
        args.setDataEntities(this.getAddTarget(conditionTarget, addTargetIds));
        if (addTargetIds.isEmpty()) {
            return;
        }
        this.setBroadcast(args, addTargetMap, null);
    }

    private void preCompileTargets(Map<String, DynamicObjectCollection> runtimeTargetMap) {
        if (runtimeTargetMap.isEmpty()) {
            return;
        }
        if (Boolean.FALSE.equals(HRMServiceHelper.invokeHRMPService((String)"bree", (String)"IBREERuleCompileService", (String)"preCompileRules", (Object[])new Object[]{runtimeTargetMap}))) {
            throw new KDBizException(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25\uff0c\u6307\u6807\u914d\u7f6e\u6709\u8bef\u3002", (String)"TargetLogOp_2", (String)"hrmp-brm-opplugin", (Object[])new Object[0]));
        }
    }

    public DynamicObject[] getAddTarget(List<DynamicObject> array, Set<Long> addTargetIds) {
        ArrayList<DynamicObject> arrNew = new ArrayList<DynamicObject>();
        for (DynamicObject target : array) {
            if (!addTargetIds.contains(target.getLong("id"))) continue;
            arrNew.add(target);
        }
        return arrNew.toArray(new DynamicObject[0]);
    }

    private DynamicObject saveTargetRuntimeCol(String ruleContent, DynamicObject designTarget, Map<String, List<Long>> addTargetMap) {
        DynamicObjectCollection targetRunTimeCol = new DynamicObjectCollection();
        HRBaseServiceHelper infoGet = new HRBaseServiceHelper("brm_ruleruntime");
        DynamicObject targetRunTime = RuleServiceHelper.queryRuntimeRule((Long)designTarget.getLong("id"));
        String bizApp = designTarget.getDynamicObject("bizapp").getString("number");
        String scene = designTarget.getDynamicObject("scene").getString("number");
        String simpleKey = RuleNamesTool.getSimpleKey((String)bizApp, (String)scene);
        if (null != targetRunTime) {
            targetRunTime.set("rulename", (Object)designTarget.getString("name"));
            targetRunTime.set("rulecontent", (Object)ruleContent);
            targetRunTime.set("modifier", (Object)RequestContext.get().getUserId());
            targetRunTime.set("modifytime", (Object)new Date());
            targetRunTime.set("packagename", (Object)RuleNamesTool.getPackageDefaultName((String)simpleKey));
            targetRunTimeCol.add((Object)targetRunTime);
        } else {
            targetRunTime = infoGet.generateEmptyDynamicObject();
            targetRunTime.set("rulename", (Object)designTarget.getString("name"));
            targetRunTime.set("rulecontent", (Object)ruleContent);
            targetRunTime.set("bizApp", (Object)bizApp);
            targetRunTime.set("packagename", (Object)RuleNamesTool.getPackageDefaultName((String)simpleKey));
            targetRunTime.set("scene", (Object)scene);
            targetRunTime.set("kbaseKey", (Object)simpleKey);
            targetRunTime.set("modifier", (Object)RequestContext.get().getUserId());
            targetRunTime.set("modifytime", (Object)new Date());
            targetRunTime.set("ruleorder", (Object)"0");
            targetRunTime.set("ruledesignid", designTarget.getPkValue());
            targetRunTime.set("creator", (Object)RequestContext.get().getUserId());
            targetRunTime.set("createtime", (Object)new Date());
            targetRunTimeCol.add((Object)targetRunTime);
        }
        infoGet.save(targetRunTimeCol);
        this.putTargetMap(addTargetMap, simpleKey, designTarget.getLong("id"));
        return targetRunTime;
    }

    private void putTargetMap(Map<String, List<Long>> addTargetMap, String key, Long id) {
        ArrayList targetIds = addTargetMap.get(key);
        if (targetIds == null) {
            targetIds = Lists.newArrayListWithCapacity((int)10);
        }
        targetIds.add(id);
        addTargetMap.put(key, targetIds);
    }
}
