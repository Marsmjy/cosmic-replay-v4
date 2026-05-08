/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.ErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.ext.hr.ruleengine.infos.DecisionConditionParamInfo
 *  kd.bos.ext.hr.ruleengine.infos.DecisionTableBodyInfo
 *  kd.bos.ext.hr.ruleengine.infos.DecisionTableHeadInfo
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.brm.business.service.CommonPolicyServiceUtil
 *  kd.hr.brm.business.service.RuleEngineCacheService
 *  kd.hr.brm.business.util.KbaseAddUtil
 *  kd.hr.brm.business.util.MessageUtil
 *  kd.hr.brm.business.util.PolicyTargetRefUtil
 *  kd.hr.brm.business.web.DecisionTableHelper
 *  kd.hr.brm.business.web.PolicyServiceHelper
 *  kd.hr.brm.business.web.RuleServiceHelper
 *  kd.hr.brm.business.web.RuleTransferService
 *  kd.hr.brm.common.tools.RuleNamesTool
 *  kd.hr.brm.opplugin.validator.PolicyValidator
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.business.util.HRBaseDataConfigUtil
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.brm.opplugin.web;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.ext.hr.ruleengine.infos.DecisionConditionParamInfo;
import kd.bos.ext.hr.ruleengine.infos.DecisionTableBodyInfo;
import kd.bos.ext.hr.ruleengine.infos.DecisionTableHeadInfo;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.brm.business.service.CommonPolicyServiceUtil;
import kd.hr.brm.business.service.RuleEngineCacheService;
import kd.hr.brm.business.util.KbaseAddUtil;
import kd.hr.brm.business.util.MessageUtil;
import kd.hr.brm.business.util.PolicyTargetRefUtil;
import kd.hr.brm.business.web.DecisionTableHelper;
import kd.hr.brm.business.web.PolicyServiceHelper;
import kd.hr.brm.business.web.RuleServiceHelper;
import kd.hr.brm.business.web.RuleTransferService;
import kd.hr.brm.common.tools.RuleNamesTool;
import kd.hr.brm.opplugin.validator.PolicyValidator;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.business.util.HRBaseDataConfigUtil;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class PolicyConfigOp
extends HRDataBaseOp {
    private String operation = "donothing";
    private final String IS_AUDIT = "isAudit";
    private static final Log LOGGER = LogFactory.getLog(PolicyConfigOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        args.getFieldKeys().add("enable");
        args.getFieldKeys().add("scene");
        args.getFieldKeys().add("retrundefault");
        args.getFieldKeys().add("results");
        args.getFieldKeys().add("policymode");
        args.getFieldKeys().add("bizappid");
        args.getFieldKeys().add("policytype");
        args.getFieldKeys().add("entrybulist");
        args.getFieldKeys().add("entitybu");
        args.getFieldKeys().add("containssub");
        args.getFieldKeys().add("rostercondition");
        args.getFieldKeys().add("rosterresult");
        args.getFieldKeys().add("enableminfirst");
        args.getFieldKeys().add("entryrulelist");
        args.getFieldKeys().add("ruleenable");
        args.getFieldKeys().add("ruleorder");
        args.getFieldKeys().add("rulescene");
        args.getFieldKeys().add("rulebizapp");
        args.getFieldKeys().add("rulename");
        args.getFieldKeys().add("rulenumber");
        args.getFieldKeys().add("orgparam");
        args.getFieldKeys().add("enablelist");
        args.getFieldKeys().add("filtercondition");
        args.getFieldKeys().add("filterresult");
        args.getFieldKeys().add("status");
        args.getFieldKeys().add("enable");
        args.getFieldKeys().add("adminorg");
    }

    public void onAddValidators(AddValidatorsEventArgs event) {
        event.addValidator((AbstractValidator)new PolicyValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        this.getOption().setVariableValue("isAudit", Boolean.toString(HRBaseDataConfigUtil.getAudit((String)"brm_policy_edit")));
        DynamicObject[] policyCol = args.getDataEntities();
        switch (args.getOperationKey()) {
            case "saveandconfigrule": {
                this.beforeSaveOperate(policyCol[0], args);
                this.savePolicyForConfigRule(policyCol[0]);
                break;
            }
            case "save": {
                this.beforeSaveOperate(policyCol[0], args);
                break;
            }
            case "submit": {
                if (policyCol.length != 1) break;
                this.beforeSaveOperate(policyCol[0], args);
                break;
            }
            case "delete": {
                this.operation = "delete";
                this.deleteRuntimeRuleByPolicy(policyCol);
                this.deletePolicy(policyCol);
                this.batchUpdatePolicyCache(policyCol);
                this.deleteDecisionTab(policyCol);
                break;
            }
            case "disable": {
                this.operation = "disable";
                this.deleteRuntimeRuleByPolicy(policyCol);
                this.batchUpdatePolicyCache(policyCol);
                break;
            }
            case "enable": {
                this.operation = "enable";
                this.queryData(args);
                for (DynamicObject policyDy : policyCol = args.getDataEntities()) {
                    policyDy.set("enable", (Object)"1");
                }
                this.updatePolicyAndRule(policyCol);
                break;
            }
            case "audit": {
                this.operation = "audit";
                this.queryData(args);
                for (DynamicObject policyDy : policyCol = args.getDataEntities()) {
                    policyDy.set("status", (Object)"C");
                }
                this.updatePolicyAndRule(policyCol);
                break;
            }
            default: {
                this.operation = "donothing";
            }
        }
        if (!HRStringUtils.equals((String)this.operation, (String)"donothing")) {
            this.logPolicy(policyCol);
        }
    }

    private void updatePolicyAndRule(DynamicObject[] policyCol) {
        DynamicObject[] enablePolicyCol = (DynamicObject[])Arrays.stream(policyCol).filter(this::enable).toArray(DynamicObject[]::new);
        if (enablePolicyCol.length > 0) {
            this.deleteRuntimeRuleByPolicy(enablePolicyCol);
            this.batchGenerateRuntimeRuleForPolicy(enablePolicyCol);
            this.batchUpdatePolicyCache(enablePolicyCol);
        }
    }

    private void queryData(BeginOperationTransactionArgs args) {
        String fromEdit = this.getOption().getVariableValue("fromEdit", null);
        if (HRStringUtils.equals((String)fromEdit, (String)"1")) {
            HRBaseServiceHelper helper = new HRBaseServiceHelper("brm_policy_edit");
            DynamicObject policy = helper.loadDynamicObject(new QFilter[]{new QFilter("id", "=", args.getDataEntities()[0].get("id"))});
            args.setDataEntities(new DynamicObject[]{policy});
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        this.broadcastForDisable();
        this.broadcastForNew();
    }

    private void beforeSaveOperate(DynamicObject policyDy, BeginOperationTransactionArgs args) {
        CommonPolicyServiceUtil.handlePolicyParam4DecisionSet((DynamicObject)policyDy);
        PolicyServiceHelper policyHelper = new PolicyServiceHelper();
        if (policyDy.getDataEntityState().getFromDatabase()) {
            boolean changeOrgParam;
            this.operation = "modify";
            DynamicObject originPolicy = policyHelper.loadOnePolicy(policyDy.getLong("id"));
            boolean disableMinOrgFirst = originPolicy.getBoolean("enableminfirst") && !policyDy.getBoolean("enableminfirst");
            boolean bl = changeOrgParam = originPolicy.getBoolean("enableminfirst") && policyDy.getBoolean("enableminfirst") && !originPolicy.getString("orgparam").equals(policyDy.getString("orgparam"));
            if (disableMinOrgFirst || changeOrgParam) {
                this.clearAdminOrg(args, policyDy);
            }
            if (this.enable(policyDy)) {
                this.handleRulesForModify(policyDy, originPolicy);
            }
        } else {
            this.operation = "new";
            KbaseAddUtil.addKbase((DynamicObject)policyDy.getDynamicObject("scene"));
        }
        if (this.enable(policyDy)) {
            RuleEngineCacheService.updatePolicyCache((String)policyDy.getDynamicObject("scene").getString("number"));
        }
        this.handleTargetRef(policyDy);
    }

    private void savePolicyForConfigRule(DynamicObject policyDy) {
        HRBaseServiceHelper helper = new HRBaseServiceHelper("brm_policy_edit");
        helper.saveOne(policyDy);
    }

    private void handleTargetRef(DynamicObject policyDy) {
        PolicyServiceHelper helper = new PolicyServiceHelper();
        DynamicObjectCollection targetRefCol = new DynamicObjectCollection();
        helper.deleteAllTargetRefByPolicyId(policyDy.getLong("id"));
        PolicyTargetRefUtil.generateTargetRuleRefs((DynamicObject)policyDy, (DynamicObjectCollection)policyDy.getDynamicObjectCollection("entryrulelist"), (DynamicObjectCollection)targetRefCol);
        HRBaseServiceHelper targetRefHelper = new HRBaseServiceHelper("brm_targetref");
        targetRefHelper.save(targetRefCol);
        RuleEngineCacheService.updateTargetRefCache((Long)policyDy.getLong("id"));
    }

    private void logPolicy(DynamicObject[] policyCol) {
        HRBaseServiceHelper policyHelper = new HRBaseServiceHelper("brm_policy_his");
        Date now = new Date();
        String operator = RequestContext.get().getUserId();
        DynamicObjectCollection hisCol = new DynamicObjectCollection();
        for (DynamicObject data : policyCol) {
            DynamicObject policyHisDy = policyHelper.generateEmptyDynamicObject();
            policyHisDy.set("policy", (Object)data.getLong("id"));
            policyHisDy.set("number", (Object)data.getString("number"));
            policyHisDy.set("name", data.get("name"));
            policyHisDy.set("operate", (Object)this.operation);
            policyHisDy.set("operatetime", (Object)now);
            policyHisDy.set("operator", (Object)operator);
            hisCol.add((Object)policyHisDy);
        }
        policyHelper.save(hisCol);
    }

    private void handleRulesForModify(DynamicObject currentPolicy, DynamicObject originPolicy) {
        boolean modeChanged;
        DynamicObjectCollection originRules = originPolicy.getDynamicObjectCollection("entryrulelist");
        boolean changeMinFirst = originPolicy.getBoolean("enableminfirst") != currentPolicy.getBoolean("enableminfirst");
        boolean bl = modeChanged = !HRStringUtils.equals((String)currentPolicy.getString("policymode"), (String)originPolicy.getString("policymode"));
        if (modeChanged || changeMinFirst) {
            HashMap runtimeRuleMap = Maps.newHashMapWithExpectedSize((int)16);
            this.reGenerateAllRuntimeRules(currentPolicy, originRules, runtimeRuleMap);
            this.saveAllRuntimeRules(runtimeRuleMap);
        }
    }

    private void reGenerateAllRuntimeRules(DynamicObject policyDy, DynamicObjectCollection designRuleEntry, Map<String, DynamicObjectCollection> runtimeRuleMap) {
        HashMap map;
        Set ruleIds = designRuleEntry.stream().filter(dy -> "true".equals(dy.getString("ruleenable"))).map(dy -> dy.getLong("id")).collect(Collectors.toSet());
        if (ruleIds.isEmpty()) {
            return;
        }
        Map originRuntimeRuleMap = RuleServiceHelper.queryRuntimeRule(ruleIds);
        try {
            String template = "/template/normalCondition";
            HRBaseServiceHelper runtimeRuleHelper = new HRBaseServiceHelper("brm_ruleruntime");
            long userId = RequestContext.get().getCurrUserId();
            Date now = new Date();
            for (DynamicObject designRule : designRuleEntry) {
                String kbaseKey;
                long designRuleId = designRule.getLong("id");
                DynamicObject ruleRuntime = (DynamicObject)originRuntimeRuleMap.get(designRuleId);
                if ("false".equals(designRule.getString("ruleenable"))) continue;
                String app = designRule.getString("rulebizapp.number");
                String sceneNum = designRule.getString("rulescene.number");
                String ruleContent = RuleTransferService.getRuleRuntimeContent((DynamicObject)policyDy, (DynamicObject)designRule, (String)sceneNum, (String)app, (String)template);
                if (ruleRuntime == null) {
                    ruleRuntime = runtimeRuleHelper.generateEmptyDynamicObject();
                    kbaseKey = RuleNamesTool.getSimpleKey((String)app, (String)sceneNum);
                    ruleRuntime.set("kbasekey", (Object)kbaseKey);
                    ruleRuntime.set("rulename", (Object)designRule.getString("rulename"));
                    ruleRuntime.set("ruleorder", (Object)designRule.getInt("ruleorder"));
                    ruleRuntime.set("ruledesignid", designRule.getPkValue());
                    ruleRuntime.set("bizApp", (Object)app);
                    ruleRuntime.set("scene", (Object)sceneNum);
                    ruleRuntime.set("kbaseKey", (Object)kbaseKey);
                    ruleRuntime.set("creator", (Object)userId);
                    ruleRuntime.set("createtime", (Object)now);
                    ruleRuntime.set("policyid", policyDy.get("id"));
                }
                kbaseKey = ruleRuntime.getString("kbasekey");
                ruleRuntime.set("packagename", (Object)RuleNamesTool.getPackageDefaultName((String)kbaseKey));
                ruleRuntime.set("rulecontent", (Object)ruleContent);
                ruleRuntime.set("modifier", (Object)userId);
                ruleRuntime.set("modifytime", (Object)now);
                DynamicObjectCollection runtimeRuleCol = runtimeRuleMap.getOrDefault(kbaseKey, new DynamicObjectCollection());
                runtimeRuleCol.add((Object)ruleRuntime);
                runtimeRuleMap.put(kbaseKey, runtimeRuleCol);
            }
        }
        catch (Exception exception) {
            LOGGER.error((Throwable)exception);
            throw new KDBizException((Throwable)exception, new ErrorCode("500", ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25\uff0c\u89c4\u5219\u8f6c\u6362\u9519\u8bef\u3002", (String)"PolicyConfigOp_0", (String)"hrmp-brm-opplugin", (Object[])new Object[0])), new Object[0]);
        }
        String appId = policyDy.getDynamicObject("bizappid").getString("number");
        String sceneNum = policyDy.getDynamicObject("scene").getString("number");
        String simpleKey = RuleNamesTool.getSimpleKey((String)appId, (String)sceneNum);
        Map map2 = map = !this.getOption().containsVariable("updateIdMap") ? Maps.newHashMapWithExpectedSize((int)4) : (Map)SerializationUtils.fromJsonString((String)this.getOption().getVariableValue("updateIdMap"), Map.class);
        if (map.containsKey(simpleKey)) {
            Set ruleIds2 = (Set)SerializationUtils.fromJsonString((String)((String)map.get(simpleKey)), Set.class);
            ruleIds2.addAll(ruleIds);
            map.put(simpleKey, SerializationUtils.toJsonString((Object)ruleIds2));
        } else {
            map.put(simpleKey, SerializationUtils.toJsonString(ruleIds));
        }
        this.getOption().setVariableValue("updateIdMap", SerializationUtils.toJsonString((Object)map));
    }

    private void saveAllRuntimeRules(Map<String, DynamicObjectCollection> runtimeRuleMap) {
        try {
            this.preCompileRules(runtimeRuleMap);
        }
        catch (KDBizException ex) {
            LOGGER.error((Throwable)ex);
            throw new KDBizException((Throwable)ex, new ErrorCode("500", ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25\uff0c\u89c4\u5219\u8f6c\u6362\u9519\u8bef\u3002", (String)"PolicyConfigOp_0", (String)"hrmp-brm-opplugin", (Object[])new Object[0])), new Object[0]);
        }
        HRBaseServiceHelper infoGet = new HRBaseServiceHelper("brm_ruleruntime");
        infoGet.save(runtimeRuleMap.values().stream().flatMap(Collection::stream).collect(Collectors.toCollection(DynamicObjectCollection::new)));
    }

    private void preCompileRules(Map<String, DynamicObjectCollection> runtimeRuleMap) {
        if (runtimeRuleMap.isEmpty()) {
            return;
        }
        if (Boolean.FALSE.equals(HRMServiceHelper.invokeHRMPService((String)"bree", (String)"IBREERuleCompileService", (String)"preCompileRules", (Object[])new Object[]{runtimeRuleMap}))) {
            throw new KDBizException(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25\uff0c\u89c4\u5219\u914d\u7f6e\u6709\u8bef\u3002", (String)"PolicyConfigOp_1", (String)"hrmp-brm-opplugin", (Object[])new Object[0]));
        }
    }

    private void deleteRuntimeRuleByPolicy(DynamicObject[] policyCol) {
        Set policyIds = Arrays.stream(policyCol).map(policy -> policy.getLong("id")).collect(Collectors.toSet());
        if (policyIds.isEmpty()) {
            return;
        }
        HRBaseServiceHelper runtimeRuleHelper = new HRBaseServiceHelper("brm_ruleruntime");
        QFilter qFilter = new QFilter("policyid", "in", policyIds);
        DynamicObject[] runRuleDys = runtimeRuleHelper.queryOriginalArray("ruledesignid,kbasekey", new QFilter[]{qFilter});
        runtimeRuleHelper.deleteByFilter(new QFilter[]{qFilter});
        HashMap appNumDelRuleIdMap = Maps.newHashMapWithExpectedSize((int)16);
        for (DynamicObject ruleRule : runRuleDys) {
            String simpleKey = ruleRule.getString("kbasekey");
            Set delRuleIdSet = appNumDelRuleIdMap.getOrDefault(simpleKey, Sets.newHashSetWithExpectedSize((int)16));
            delRuleIdSet.add(ruleRule.getLong("ruledesignid"));
            appNumDelRuleIdMap.put(simpleKey, delRuleIdSet);
        }
        this.getOption().setVariableValue("deleteIdMap", appNumDelRuleIdMap.size() > 0 ? SerializationUtils.toJsonString((Object)appNumDelRuleIdMap) : "");
    }

    private void updateTargetRefCache(DynamicObject[] policyCol) {
        Set policyIds = Arrays.stream(policyCol).map(policy -> policy.getLong("id")).collect(Collectors.toSet());
        RuleEngineCacheService.batchUpdateTargetRefCache(policyIds);
    }

    private void broadcastForNew() {
        String updateRuleIdStr = this.getOption().getVariableValue("updateIdMap", null);
        if (HRStringUtils.isEmpty((String)updateRuleIdStr)) {
            return;
        }
        Map updateRuleIdMap = (Map)SerializationUtils.fromJsonString((String)updateRuleIdStr, Map.class);
        for (Map.Entry entry : updateRuleIdMap.entrySet()) {
            String simpleKey = (String)entry.getKey();
            Set updateRuleIds = (Set)SerializationUtils.fromJsonString((String)((String)entry.getValue()), Set.class);
            try {
                String ruleIdStr = SerializationUtils.toJsonString((Object)updateRuleIds);
                if (!HRStringUtils.isNotEmpty((String)ruleIdStr)) continue;
                MessageUtil.updateRules((String)simpleKey, (String)ruleIdStr);
            }
            catch (Exception exp) {
                LOGGER.error("updateRules error:{}", (Object)exp.getMessage());
                throw new KDBizException(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25\uff0c\u5e7f\u64ad\u9519\u8bef\u3002", (String)"PolicyConfigOp_2", (String)"hrmp-brm-opplugin", (Object[])new Object[0]));
            }
        }
    }

    private void broadcastForDisable() {
        String delRuleIdMapStr = this.getOption().getVariableValue("deleteIdMap", null);
        if (HRStringUtils.isEmpty((String)delRuleIdMapStr)) {
            return;
        }
        try {
            MessageUtil.removeRules((String)delRuleIdMapStr);
        }
        catch (Exception exp) {
            LOGGER.error("removeRules error:{}", (Object)exp.getMessage());
            throw new KDBizException(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25\uff0c\u5e7f\u64ad\u9519\u8bef\u3002", (String)"PolicyConfigOp_2", (String)"hrmp-brm-opplugin", (Object[])new Object[0]));
        }
    }

    private void deletePolicy(DynamicObject[] policyCol) {
        List policyIdList = Arrays.stream(policyCol).map(policy -> policy.getLong("id")).collect(Collectors.toList());
        PolicyServiceHelper helper = new PolicyServiceHelper();
        helper.deleteDesignRuleList(policyIdList);
        helper.deleteAllTargetRefByPolicyId(policyIdList);
        this.updateTargetRefCache(policyCol);
    }

    private void deleteDecisionTab(DynamicObject[] policyCol) {
        List policyIds = Arrays.stream(policyCol).filter(policyDy -> HRStringUtils.equals((String)policyDy.getString("policytype"), (String)"decision_table")).map(policyDy -> policyDy.getLong("id")).collect(Collectors.toList());
        PolicyServiceHelper helper = new PolicyServiceHelper();
        helper.deleteDecisionTabList(policyIds);
    }

    private void batchGenerateRuntimeRuleForPolicy(DynamicObject[] policyCol) {
        HashMap runtimeRuleMap = Maps.newHashMapWithExpectedSize((int)16);
        for (DynamicObject policyDy : policyCol) {
            this.reGenerateAllRuntimeRules(policyDy, policyDy.getDynamicObjectCollection("entryrulelist"), runtimeRuleMap);
        }
        this.saveAllRuntimeRules(runtimeRuleMap);
    }

    private void batchUpdatePolicyCache(DynamicObject[] policyCol) {
        Set sceneNums = Arrays.stream(policyCol).map(policy -> policy.getDynamicObject("scene").getString("number")).collect(Collectors.toSet());
        RuleEngineCacheService.batchUpdatePolicyCache(sceneNums);
    }

    private void clearAdminOrg(BeginOperationTransactionArgs args, DynamicObject policyDy) {
        DynamicObject decisionTableDy;
        DynamicObjectCollection ruleCol = policyDy.getDynamicObjectCollection("entryrulelist");
        for (DynamicObject ruleDy : ruleCol) {
            ruleDy.set("adminorg", null);
        }
        args.setDataEntities(new DynamicObject[]{policyDy});
        if ("decision_table".equals(policyDy.getString("policytype")) && null != (decisionTableDy = DecisionTableHelper.getDecisionTableByPolicy((Object)policyDy.getLong("id")))) {
            String tableBody;
            DecisionTableHeadInfo headInfo;
            List paramInfoList;
            String tableHead = decisionTableDy.getString("tablehead");
            if (StringUtils.isNotEmpty((CharSequence)tableHead) && !(paramInfoList = (headInfo = (DecisionTableHeadInfo)SerializationUtils.fromJsonString((String)tableHead, DecisionTableHeadInfo.class)).getConditionParams()).isEmpty() && ((DecisionConditionParamInfo)paramInfoList.get(0)).isMinOrgFirst()) {
                headInfo.getConditionParams().remove(0);
                tableHead = SerializationUtils.toJsonString((Object)headInfo);
                decisionTableDy.set("tablehead", (Object)tableHead);
            }
            if (StringUtils.isNotEmpty((CharSequence)(tableBody = decisionTableDy.getString("tablebody")))) {
                List bodyInfoList = SerializationUtils.fromJsonStringToList((String)tableBody, DecisionTableBodyInfo.class);
                for (DecisionTableBodyInfo bodyInfo : bodyInfoList) {
                    bodyInfo.getConditionValue().remove("0");
                }
                tableBody = SerializationUtils.toJsonString((Object)bodyInfoList);
                decisionTableDy.set("tablebody", (Object)tableBody);
            }
            DecisionTableHelper.saveDecisionTableByPolicy((DynamicObject)decisionTableDy);
        }
    }

    private boolean enable(DynamicObject policyDy) {
        String status = policyDy.getString("status");
        String enable = policyDy.getString("enable");
        if (Boolean.parseBoolean(this.getOption().getVariableValue("isAudit"))) {
            return HRStringUtils.equals((String)status, (String)"C") && HRStringUtils.equals((String)enable, (String)"1");
        }
        return HRStringUtils.equals((String)enable, (String)"1");
    }
}
