/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.AppMetadataCache
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.operation.OperationServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRMServiceHelper
 *  kd.hr.hbp.business.util.OrgServiceUtil
 *  kd.hr.hbp.common.model.label.LabelResultInfo
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper
 *  kd.hr.hrcs.bussiness.service.label.LabelPublishService
 *  kd.hr.hrcs.bussiness.service.label.LabelService
 *  kd.hr.hrcs.bussiness.service.label.LabelTaskStorageService
 *  kd.hr.hrcs.bussiness.service.label.LblStrategyServiceHelper
 *  kd.hr.hrcs.bussiness.servicehelper.label.LabelPolicyServiceHelper
 *  kd.hr.hrcs.common.constants.label.LblStrategyConstants
 *  kd.hr.hrcs.opplugin.validator.label.LabelStrategyValidator
 */
package kd.hr.hrcs.opplugin.web.label;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.AppMetadataCache;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.servicehelper.HRMServiceHelper;
import kd.hr.hbp.business.util.OrgServiceUtil;
import kd.hr.hbp.common.model.label.LabelResultInfo;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.label.LabelObjectServiceHelper;
import kd.hr.hrcs.bussiness.service.label.LabelPublishService;
import kd.hr.hrcs.bussiness.service.label.LabelService;
import kd.hr.hrcs.bussiness.service.label.LabelTaskStorageService;
import kd.hr.hrcs.bussiness.service.label.LblStrategyServiceHelper;
import kd.hr.hrcs.bussiness.servicehelper.label.LabelPolicyServiceHelper;
import kd.hr.hrcs.common.constants.label.LblStrategyConstants;
import kd.hr.hrcs.opplugin.validator.label.LabelStrategyValidator;

public final class LabelStrategyOp
extends HRDataBaseOp
implements LblStrategyConstants {
    private static final Log LOGGER = LogFactory.getLog(LabelStrategyOp.class);
    private final HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_labelpolicyrule");
    private final String appId = AppMetadataCache.getAppInfoByNumber((String)"hrcs").getId();
    private final LabelService labelService = new LabelService();
    private Set<Long> selectedFieldSet;
    private Long brmPolicyId;

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("brmpolicyid");
        fieldKeys.add("labelobject");
        fieldKeys.add("label");
        fieldKeys.add("lasttasknumber");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        e.addValidator((AbstractValidator)new LabelStrategyValidator());
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        if ("save".equals(e.getOperationKey())) {
            for (DynamicObject dataEntity : e.getDataEntities()) {
                long lblValueId;
                String workType = dataEntity.getString("worktype");
                String configType = dataEntity.getString("configtype");
                if (!"0".equals(workType)) continue;
                DynamicObjectCollection entryDisPlay = dataEntity.getDynamicObjectCollection("entryentitydisplay");
                DynamicObjectCollection entry = dataEntity.getDynamicObjectCollection("entryentity");
                Map<Long, String> serviceDisplayMap = entryDisPlay.stream().collect(Collectors.toMap(ed -> ed.getLong("labelvaluedisplay.id"), ed -> ed.getString("servicedisplay")));
                HashSet lblValueIdSet = Sets.newHashSetWithExpectedSize((int)entry.size());
                for (DynamicObject dynamicObject : entry) {
                    lblValueId = dynamicObject.getLong("labelvalue.id");
                    lblValueIdSet.add(lblValueId);
                    String service = serviceDisplayMap.get(lblValueId);
                    dynamicObject.set("service", (Object)service);
                }
                if (!"2".equals(configType)) continue;
                for (DynamicObject dynamicObject : entryDisPlay) {
                    lblValueId = dynamicObject.getLong("labelvaluedisplay.id");
                    if (lblValueIdSet.contains(lblValueId)) continue;
                    DynamicObject newDyn = entry.addNew();
                    newDyn.set("labelValue", (Object)lblValueId);
                    newDyn.set("service", (Object)dynamicObject.getString("servicedisplay"));
                }
            }
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        if ("deletestrategy".equals(e.getOperationKey())) {
            LblStrategyServiceHelper.delete((Object[])Arrays.stream(e.getDataEntities()).map(dataEntity -> dataEntity.getLong("id")).toArray());
        } else if ("save".equals(e.getOperationKey())) {
            for (DynamicObject dataEntity2 : e.getDataEntities()) {
                String workType = dataEntity2.getString("worktype");
                if (!"1".equals(workType)) continue;
                dataEntity2.set("lasttasknumber", (Object)"hand");
            }
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        if ("save".equals(e.getOperationKey())) {
            for (DynamicObject dataEntity2 : e.getDataEntities()) {
                String workType = dataEntity2.getString("worktype");
                if ("0".equals(workType)) {
                    this.saveWithRule(dataEntity2);
                    continue;
                }
                this.saveWithHand(dataEntity2);
            }
        } else if ("deletestrategy".equals(e.getOperationKey())) {
            List idList = Arrays.stream(e.getDataEntities()).map(dataEntity -> dataEntity.getLong("id")).collect(Collectors.toList());
            HRBaseServiceHelper filterServiceHelper = new HRBaseServiceHelper("hrcs_lblstrategyfilter");
            filterServiceHelper.deleteByFilter(new QFilter[]{new QFilter("lblstrategy.id", "in", idList)});
            this.deleteRule(e.getDataEntities());
            for (DynamicObject dataEntity3 : e.getDataEntities()) {
                long id = dataEntity3.getLong("id");
                String taskNumber = dataEntity3.getString("lasttasknumber");
                LabelTaskStorageService labelTaskStorageService = new LabelTaskStorageService(Long.valueOf(id), taskNumber);
                labelTaskStorageService.deleteIndex(labelTaskStorageService.getLabelIndexName());
            }
            LabelPolicyServiceHelper.deleteTaskByPolicyId(idList);
        }
    }

    private void saveWithRule(DynamicObject dyn) {
        try (TXHandle txHandle = TX.required();){
            try {
                this.saveFilter(dyn);
                String configType = dyn.getString("configtype");
                if ("1".equals(configType)) {
                    long sceneId = this.saveRule(dyn);
                    this.saveLabelParam(dyn, sceneId);
                    this.saveStrategy(dyn);
                } else {
                    String isChanged = (String)this.getOption().getVariables().get("isTypeChanged");
                    if (Boolean.TRUE.toString().equals(isChanged)) {
                        this.deleteRule(new DynamicObject[]{dyn});
                    }
                }
            }
            catch (Exception ex) {
                txHandle.markRollback();
                throw new KDBizException(ex.getMessage());
            }
        }
    }

    private void deleteRule(DynamicObject[] dataEntities) {
        List idList = Arrays.stream(dataEntities).map(dataEntity -> dataEntity.getLong("id")).collect(Collectors.toList());
        this.serviceHelper.deleteByFilter(new QFilter[]{new QFilter("labelpolicy.id", "in", idList)});
        List brmPolicyIdList = Arrays.stream(dataEntities).map(dyn -> dyn.getLong("brmpolicyid")).collect(Collectors.toList());
        HRMServiceHelper.invokeHRMPService((String)"brm", (String)"IBRMPolicyService", (String)"deletePolicy", (Object[])new Object[]{brmPolicyIdList});
        for (DynamicObject lblStrategy : dataEntities) {
            long lblObjId = lblStrategy.getLong("labelobject.id");
            long lblId = lblStrategy.getLong("label.id");
            DynamicObject ruleScene = LblStrategyServiceHelper.getRuleScene((Long)lblId, (Long)lblObjId);
            DynamicObject brmScene = ruleScene.getDynamicObject("brmscene");
            this.deleteSceneInputParamByDelete(lblStrategy, brmScene);
            LblStrategyServiceHelper.deleteLabelParamsByStrategy((Long)lblId, (Long)lblObjId);
        }
    }

    private void saveWithHand(DynamicObject dataEntity) {
        long id = dataEntity.getLong("id");
        String delIdStr = this.operateOption.getVariableValue("delData");
        if (!HRStringUtils.isEmpty((String)delIdStr)) {
            List delIdList = (List)SerializationUtils.fromJsonString((String)delIdStr, List.class);
            LabelTaskStorageService labelTaskStorageService = new LabelTaskStorageService(Long.valueOf(id), "hand");
            labelTaskStorageService.deleteByIds(delIdList);
        }
        String variableValue = this.operateOption.getVariableValue("labelData");
        Map data = (Map)SerializationUtils.fromJsonString((String)variableValue, Map.class);
        ArrayList labelResultList = Lists.newArrayListWithExpectedSize((int)data.size());
        HashMap resultInfoMap = Maps.newHashMapWithExpectedSize((int)labelResultList.size());
        DynamicObjectCollection labelValueCol = dataEntity.getDynamicObjectCollection("label.entryentitylabelvalue");
        Map<Long, String> labelValueMap = labelValueCol.stream().collect(Collectors.toMap(dyn -> dyn.getLong("id"), dyn -> dyn.getString("labelvalue")));
        long strategyId = dataEntity.getLong("id");
        String originDataStr = this.operateOption.getVariableValue("originESData");
        Map originData = Maps.newHashMap();
        if (!HRStringUtils.isEmpty((String)originDataStr)) {
            originData = (Map)SerializationUtils.fromJsonString((String)originDataStr, Map.class);
        }
        for (Map.Entry entry : data.entrySet()) {
            List dataList = (List)entry.getValue();
            for (Map paramMap : dataList) {
                Object bizId = paramMap.get("id");
                LabelResultInfo labelResultInfo = (LabelResultInfo)resultInfoMap.get(bizId);
                if (labelResultInfo == null) {
                    labelResultInfo = new LabelResultInfo();
                    labelResultInfo.setId(bizId);
                    Map originDataSon = (Map)originData.get(bizId.toString());
                    if (!CollectionUtils.isEmpty((Map)originDataSon)) {
                        labelResultInfo.setCreatorId(Long.valueOf(Long.parseLong((String)originDataSon.get("creatorId"))));
                        Date createTime = null;
                        try {
                            createTime = HRDateTimeUtils.parseDate((String)((String)originDataSon.get("createTime")));
                        }
                        catch (ParseException e) {
                            LOGGER.error("LabelStrategyOp-saveWithHand-ParseException:{}", (Object)e.getMessage());
                        }
                        labelResultInfo.setCreateTime(createTime);
                    } else {
                        labelResultInfo.setCreatorId(Long.valueOf(RequestContext.get().getCurrUserId()));
                        labelResultInfo.setCreateTime(new Date());
                    }
                    labelResultInfo.setLabelObjectId(Long.valueOf(dataEntity.getLong("labelobject.id")));
                    labelResultInfo.setPolicyId(Long.valueOf(strategyId));
                    labelResultInfo.setTaskNumber("hand");
                    labelResultList.add(labelResultInfo);
                    resultInfoMap.put(bizId, labelResultInfo);
                }
                for (Map.Entry paramEntry : paramMap.entrySet()) {
                    labelResultInfo.putDisplayField((String)paramEntry.getKey(), paramEntry.getValue());
                }
                String labelPersonStr = (String)paramMap.get("labelperson");
                String labelTimeStr = (String)paramMap.get("labeltime");
                Long labelPersonId = Long.parseLong(labelPersonStr);
                Date labelTime = null;
                if (HRStringUtils.isEmpty((String)labelTimeStr)) {
                    labelTime = new Date();
                } else {
                    try {
                        labelTime = HRDateTimeUtils.parseDate((String)labelTimeStr);
                    }
                    catch (ParseException e) {
                        LOGGER.error("loadByHandWorkType-ParseException:{}", (Object)e.getMessage());
                    }
                }
                long labelValueId = Long.parseLong((String)entry.getKey());
                labelResultInfo.addModelLabel(Long.valueOf(dataEntity.getLong("label.id")), Long.valueOf(labelValueId), labelValueMap.get(labelValueId), labelTime, labelPersonId);
            }
        }
        LabelTaskStorageService labelTaskStorageService = new LabelTaskStorageService(Long.valueOf(id), "hand");
        try {
            labelTaskStorageService.batchSaveLabelResult((List)labelResultList);
        }
        catch (Exception e) {
            LOGGER.error((Throwable)e);
            throw new KDBizException(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25\uff0c\u8bf7\u8054\u7cfb\u8fd0\u7ef4\u4eba\u5458\u914d\u7f6eES\u670d\u52a1\u3002", (String)"LabelStrategyOp_0", (String)"hrmp-hrcs-opplugin", (Object[])new Object[0]));
        }
        LabelPublishService.publishPolicyFinishMsg((Long)id, (String)"hand", (Long)Long.valueOf(labelResultList.size()), (Long)dataEntity.getLong("labelobject.id"), (Long)dataEntity.getLong("label.id"));
    }

    private void saveStrategy(DynamicObject lblStrategy) {
        if (this.brmPolicyId != null) {
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_lblstrategy");
            DynamicObject lblStrategyData = serviceHelper.queryOne("brmpolicyid", new QFilter("id", "=", (Object)lblStrategy.getLong("id")));
            lblStrategyData.set("brmpolicyid", (Object)this.brmPolicyId);
            serviceHelper.saveOne(lblStrategyData);
        }
    }

    private void saveFilter(DynamicObject dataEntity) {
        String hrFilterValue = (String)this.getOption().getVariables().get("filterValue");
        if (!HRStringUtils.isEmpty((String)hrFilterValue)) {
            Date date = new Date();
            long currUserId = RequestContext.get().getCurrUserId();
            long id = dataEntity.getLong("id");
            LblStrategyServiceHelper.deleteFilter((Long)id);
            Map valueMap = (Map)SerializationUtils.fromJsonString((String)hrFilterValue, Map.class);
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_lblstrategyfilter");
            DynamicObjectCollection saveCol = new DynamicObjectCollection();
            for (Map.Entry entry : valueMap.entrySet()) {
                DynamicObject dynamicObject = serviceHelper.generateEmptyDynamicObject();
                dynamicObject.set("lblstrategy", (Object)dataEntity);
                dynamicObject.set("createtime", (Object)date);
                dynamicObject.set("creator", (Object)currUserId);
                dynamicObject.set("fieldkey", entry.getKey());
                if (entry.getValue() instanceof String) {
                    dynamicObject.set("value", entry.getValue());
                } else {
                    dynamicObject.set("hasfilter", entry.getValue());
                }
                saveCol.add((Object)dynamicObject);
            }
            OperationServiceHelper.executeOperate((String)"save", (String)"hrcs_lblstrategyfilter", (DynamicObject[])((DynamicObject[])saveCol.toArray((Object[])new DynamicObject[0])), (OperateOption)OperateOption.create());
        }
    }

    private long saveRule(DynamicObject lblStrategy) {
        long id = lblStrategy.getLong("id");
        long labelId = lblStrategy.getLong("label.id");
        long lblObjId = lblStrategy.getLong("labelobject.id");
        String hrFilterValue = (String)this.getOption().getVariables().get("hrFilterKey");
        Map hrFilterValueMap = (Map)SerializationUtils.fromJsonString((String)hrFilterValue, Map.class);
        DynamicObject ruleScene = LblStrategyServiceHelper.getRuleScene((Long)labelId, (Long)lblObjId);
        DynamicObject[] labelPolicyRules = LblStrategyServiceHelper.getLabelPolicyRules((Long)id);
        Map<Long, DynamicObject> oldDataRulesMap = Arrays.stream(labelPolicyRules).collect(Collectors.toMap(labelPolicyRule -> labelPolicyRule.getLong("labelvalue.id"), labelPolicyRule -> labelPolicyRule));
        DynamicObjectCollection newDynCol = new DynamicObjectCollection();
        DynamicObjectCollection updateDynCol = new DynamicObjectCollection();
        DynamicObject brmScene = ruleScene.getDynamicObject("brmscene");
        this.saveScene(brmScene, lblStrategy, hrFilterValueMap);
        this.saveRuleEngine(oldDataRulesMap, lblStrategy, hrFilterValueMap, brmScene.getLong("id"));
        this.deleteSceneInputParam(lblStrategy, brmScene);
        DynamicObject[] brmRules = LblStrategyServiceHelper.getBrmRules((Long)this.brmPolicyId);
        Map<String, DynamicObject> ruleMap = Arrays.stream(brmRules).collect(Collectors.toMap(rule -> rule.getString("number"), rule -> rule));
        hrFilterValueMap.forEach((key, value) -> {
            Long lblValueId = Long.parseLong(key);
            DynamicObject dynamicObject = (DynamicObject)oldDataRulesMap.get(lblValueId);
            if (dynamicObject != null) {
                dynamicObject.set("conditions", value);
                updateDynCol.add((Object)dynamicObject);
            } else {
                DynamicObject rule = (DynamicObject)ruleMap.get("r" + lblValueId);
                dynamicObject = this.generateNewRule(id, labelId, lblValueId, (String)value, rule.getLong("id"));
                newDynCol.add((Object)dynamicObject);
            }
        });
        this.serviceHelper.save(newDynCol);
        this.serviceHelper.save(updateDynCol);
        return ruleScene.getLong("brmscene.id");
    }

    private void saveLabelParam(DynamicObject lblStrategy, long sceneId) {
        if (!CollectionUtils.isEmpty(this.selectedFieldSet)) {
            long lblObjId = lblStrategy.getLong("labelobject.id");
            long lblId = lblStrategy.getLong("label.id");
            DynamicObjectCollection dynCol = new DynamicObjectCollection();
            DynamicObject ruleScene = LblStrategyServiceHelper.getRuleScene((Long)sceneId);
            DynamicObjectCollection sceneInputParams = ruleScene.getDynamicObjectCollection("sceneinputparams");
            Map<String, DynamicObject> inputNumberMap = sceneInputParams.stream().collect(Collectors.toMap(param -> param.getString("inputnumber"), param -> param));
            for (Long fieldId : this.selectedFieldSet) {
                DynamicObject labelParam = this.generateLabelParam(fieldId, lblId, lblObjId, sceneId);
                DynamicObject inputParam = inputNumberMap.get("p" + fieldId);
                if (inputParam != null) {
                    labelParam.set("brmiputparam", (Object)inputParam.getLong("id"));
                }
                dynCol.add((Object)labelParam);
            }
            LblStrategyServiceHelper.deleteLabelParamsByStrategy((Long)lblId, (Long)lblObjId);
            LblStrategyServiceHelper.saveLabelParams((DynamicObjectCollection)dynCol);
        }
    }

    private DynamicObject generateNewRule(Long id, Long labelId, Long lblValueId, String value, Long brmRuleId) {
        DynamicObject dynamicObject = this.serviceHelper.generateEmptyDynamicObject();
        dynamicObject.set("labelpolicy", (Object)id);
        dynamicObject.set("label", (Object)labelId);
        dynamicObject.set("labelvalue", (Object)lblValueId);
        dynamicObject.set("conditions", (Object)value);
        dynamicObject.set("brmrule", (Object)brmRuleId);
        return dynamicObject;
    }

    private void saveScene(DynamicObject scene, DynamicObject lblStrategy, Map<String, String> hrFilterValueMap) {
        long lblObjId = lblStrategy.getLong("labelobject.id");
        DynamicObject[] fields = LabelObjectServiceHelper.getFields((Long)lblObjId);
        HashMap updateMap = Maps.newHashMapWithExpectedSize((int)16);
        ArrayList paramMap = Lists.newArrayListWithExpectedSize((int)fields.length);
        updateMap.put("id", scene.getLong("id"));
        updateMap.put("name", scene.getString("name"));
        DynamicObjectCollection sceneInputParams = scene.getDynamicObjectCollection("sceneinputparams");
        Map<String, DynamicObject> inputParamMap = sceneInputParams.stream().collect(Collectors.toMap(dyn -> dyn.getString("inputnumber"), dyn -> dyn));
        Map convertResultMap = this.labelService.convertToRuleCondition(hrFilterValueMap, fields);
        this.selectedFieldSet = (Set)convertResultMap.get("conditionField");
        for (DynamicObject field : fields) {
            long id = field.getLong("id");
            String idStr = "p" + id;
            if (inputParamMap.get(idStr) != null || !this.selectedFieldSet.contains(id)) continue;
            Map brmSceneParamMap = this.labelService.buildBrmSceneParamMap(field, false);
            paramMap.add(brmSceneParamMap);
        }
        updateMap.put("inputparams", paramMap);
        this.invokeModifySceneService(updateMap);
    }

    private void saveRuleEngine(Map<Long, DynamicObject> oldDataRulesMap, DynamicObject lblStrategy, Map<String, String> hrFilterValueMap, Long ruleSceneId) {
        String method;
        long lblObjId = lblStrategy.getLong("labelobject.id");
        DynamicObject[] lblValueRules = LblStrategyServiceHelper.getLblValueRule((Long)lblObjId);
        Map<Long, DynamicObject> lblValueRuleMap = Arrays.stream(lblValueRules).collect(Collectors.toMap(lblValueRule -> lblValueRule.getLong("labelvalue.id"), lblValueRule -> lblValueRule));
        List lblValueList = hrFilterValueMap.keySet().stream().map(Long::parseLong).collect(Collectors.toList());
        DynamicObject[] labelValues = LblStrategyServiceHelper.getLabelValues(lblValueList);
        Map<Long, DynamicObject> lblValueMap = Arrays.stream(labelValues).collect(Collectors.toMap(lblValue -> lblValue.getLong("id"), lblValue -> lblValue));
        DynamicObject[] fields = LabelObjectServiceHelper.getFields((Long)lblObjId);
        Map convertResultMap = this.labelService.convertToRuleCondition(hrFilterValueMap, fields);
        hrFilterValueMap = (Map)convertResultMap.get("condition");
        ArrayList conditionList = Lists.newArrayListWithExpectedSize((int)hrFilterValueMap.size());
        ArrayList entryBuList = Lists.newArrayListWithExpectedSize((int)hrFilterValueMap.size());
        ArrayList updateConditionList = Lists.newArrayListWithExpectedSize((int)hrFilterValueMap.size());
        int emptyCount = 0;
        for (Map.Entry entry : hrFilterValueMap.entrySet()) {
            Long labelValueId = Long.parseLong((String)entry.getKey());
            DynamicObject dynamicObject = oldDataRulesMap.get(labelValueId);
            if (dynamicObject != null) continue;
            ++emptyCount;
        }
        int ruleOrder = 0;
        long[] ids = ORM.create().genLongIds("brm_ruledesign", emptyCount);
        emptyCount = 0;
        for (Map.Entry entry : hrFilterValueMap.entrySet()) {
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            Long labelValueId = Long.parseLong(key);
            DynamicObject dynamicObject = oldDataRulesMap.get(labelValueId);
            if (dynamicObject != null) {
                HashMap updateMap = Maps.newHashMapWithExpectedSize((int)16);
                updateMap.put("id", dynamicObject.getLong("brmrule.id"));
                updateMap.put("rulename", dynamicObject.getString("brmrule.name"));
                updateMap.put("rulenumber", dynamicObject.getString("brmrule.number"));
                updateMap.put("ruleorder", dynamicObject.getInt("brmrule.ruleorder"));
                updateMap.put("filtercondition", value);
                updateMap.put("modifystatus", "modify");
                updateMap.put("filterresult", dynamicObject.getString("brmrule.results"));
                updateConditionList.add(updateMap);
                continue;
            }
            HashMap newMap = Maps.newHashMapWithExpectedSize((int)16);
            DynamicObject labelValue = lblValueMap.get(labelValueId);
            long ruleId = ids[emptyCount];
            ++emptyCount;
            newMap.put("id", ruleId);
            newMap.put("rulename", labelValue.getString("value"));
            newMap.put("rulenumber", "r" + labelValue.getLong("id"));
            newMap.put("ruleorder", ruleOrder++);
            newMap.put("filtercondition", value);
            newMap.put("modifystatus", "new");
            DynamicObject lblValueRule2 = lblValueRuleMap.get(labelValueId);
            HashMap resultsMap = Maps.newHashMapWithExpectedSize((int)16);
            ArrayList resultList = Lists.newArrayListWithExpectedSize((int)10);
            HashMap resultMap = Maps.newHashMapWithExpectedSize((int)16);
            resultList.add(resultMap);
            resultsMap.put("resultList", resultList);
            resultMap.put("index", 0);
            resultMap.put("operators", "==");
            resultMap.put("displayParam", ResManager.loadKDString((String)"\u6807\u7b7e\u503c", (String)"LabelSaveOp_1", (String)"hrmp-hrcs-opplugin", (Object[])new Object[0]));
            resultMap.put("param", "labelValue.hrcs_labelvalue.id");
            resultMap.put("paramType", "dynamicObject");
            resultMap.put("valueType", "2");
            resultMap.put("value", labelValueId + "");
            resultMap.put("displayValue", labelValue.getString("value"));
            if (Objects.equals("20", labelValue.getString("label.type"))) {
                HashMap resultMap1 = Maps.newHashMapWithExpectedSize((int)16);
                long brmTargetId = lblValueRule2.getLong("brmtarget.id");
                resultMap1.put("index", 1);
                resultMap1.put("operators", "==");
                resultMap1.put("param", "bizLabelValue");
                resultMap1.put("displayParam", ResManager.loadKDString((String)"\u4e8b\u5b9e\u6807\u7b7e\u503c", (String)"LabelSaveOp_0", (String)"hrmp-hrcs-opplugin", (Object[])new Object[0]));
                resultMap1.put("value", String.format("%s_%s_%s", RequestContext.get().getTenantId(), RequestContext.get().getAccountId(), brmTargetId));
                resultMap1.put("displayValue", lblValueRule2.getString("brmtarget.name"));
                resultMap1.put("paramType", "string");
                resultMap1.put("valueType", "3");
                resultList.add(resultMap1);
            }
            newMap.put("filterresult", SerializationUtils.toJsonString((Object)resultsMap));
            conditionList.add(newMap);
        }
        long brmPolicyId = lblStrategy.getLong("brmpolicyid");
        String number = "Label" + System.currentTimeMillis() + "_" + lblStrategy.getString("number");
        HashMap map = Maps.newHashMapWithExpectedSize((int)16);
        if (updateConditionList.isEmpty()) {
            HashMap buMap = Maps.newHashMapWithExpectedSize((int)16);
            buMap.put("entitybu", OrgServiceUtil.getHRRootOrgId());
            buMap.put("containssub", true);
            entryBuList.add(buMap);
            map.put("number", number);
            map.put("name", number);
            map.put("createbu", OrgServiceUtil.getHRRootOrgId());
            map.put("bizappid", this.appId);
            map.put("scene", ruleSceneId);
            map.put("entryrulelist", conditionList);
            map.put("entrybulist", entryBuList);
            map.put("policymode", "FullMatch");
            method = "addPolicy";
        } else {
            updateConditionList.addAll(conditionList);
            DynamicObject rulePolicy = LblStrategyServiceHelper.getRulePolicy((Long)brmPolicyId);
            map.put("id", brmPolicyId);
            map.put("name", rulePolicy.getString("name"));
            map.put("bizappid", this.appId);
            map.put("scene", ruleSceneId);
            map.put("createbu", OrgServiceUtil.getHRRootOrgId());
            map.put("policymode", "FullMatch");
            map.put("entryrulelist", updateConditionList);
            method = "modifyPolicy";
        }
        Map resultMap = (Map)HRMServiceHelper.invokeHRMPService((String)"brm", (String)"IBRMPolicyService", (String)method, (Object[])new Object[]{map});
        if (200 != (Integer)resultMap.get("resultCode")) {
            LOGGER.error("saveRuleEngine_error:{}", (Object)map);
            throw new KDBizException(resultMap.get("errorMsg").toString());
        }
        this.brmPolicyId = (Long)resultMap.get("policyId");
    }

    private void deleteSceneInputParamByDelete(DynamicObject lblStrategy, DynamicObject scene) {
        long lblObjId = lblStrategy.getLong("labelobject.id");
        long lblId = lblStrategy.getLong("label.id");
        DynamicObject[] labelParams = LblStrategyServiceHelper.getLabelParams((Long)lblId, (Long)lblObjId);
        HashMap updateMap = Maps.newHashMapWithExpectedSize((int)16);
        ArrayList paramMap = Lists.newArrayListWithExpectedSize((int)labelParams.length);
        for (DynamicObject labelParam : labelParams) {
            if (!Objects.equals("20", labelParam.getString("source"))) continue;
            Map<String, Object> filedMap = this.generateInputParam(labelParam);
            paramMap.add(filedMap);
        }
        updateMap.put("inputparams", paramMap);
        updateMap.put("id", scene.getLong("id"));
        updateMap.put("name", scene.getString("name"));
        this.invokeModifySceneService(updateMap);
    }

    private void deleteSceneInputParam(DynamicObject lblStrategy, DynamicObject scene) {
        long lblObjId = lblStrategy.getLong("labelobject.id");
        long lblId = lblStrategy.getLong("label.id");
        DynamicObject[] fields = LabelObjectServiceHelper.getFields((Long)lblObjId);
        DynamicObject[] labelParams = LblStrategyServiceHelper.getLabelParams((Long)lblId, (Long)lblObjId);
        HashMap byStrategyFieldIdMap = Maps.newHashMapWithExpectedSize((int)labelParams.length);
        for (DynamicObject labelParam : labelParams) {
            long paramId = labelParam.getLong("param.id");
            if (!Objects.equals("20", labelParam.getString("source"))) continue;
            byStrategyFieldIdMap.put(paramId, labelParam);
        }
        DynamicObjectCollection sceneInputParams = scene.getDynamicObjectCollection("sceneinputparams");
        Map<String, DynamicObject> inputParamMap = sceneInputParams.stream().collect(Collectors.toMap(dyn -> dyn.getString("inputnumber"), dyn -> dyn));
        HashMap updateMap = Maps.newHashMapWithExpectedSize((int)16);
        updateMap.put("id", scene.getLong("id"));
        updateMap.put("name", scene.getString("name"));
        ArrayList paramMap = Lists.newArrayListWithExpectedSize((int)fields.length);
        updateMap.put("inputparams", paramMap);
        for (DynamicObject field : fields) {
            long id = field.getLong("id");
            String idStr = "p" + id;
            if (inputParamMap.get(idStr) == null || this.selectedFieldSet.contains(id) || !byStrategyFieldIdMap.containsKey(id)) continue;
            DynamicObject dynamicObject = (DynamicObject)byStrategyFieldIdMap.get(id);
            Map<String, Object> filedMap = this.generateInputParam(dynamicObject);
            paramMap.add(filedMap);
        }
        this.invokeModifySceneService(updateMap);
    }

    private Map<String, Object> generateInputParam(DynamicObject labelParam) {
        HashMap filedMap = Maps.newHashMapWithExpectedSize((int)16);
        filedMap.put("id", labelParam.getLong("brmiputparam.id"));
        filedMap.put("inputnumber", labelParam.getString("brmiputparam.number"));
        filedMap.put("modifystatus", "delete");
        return filedMap;
    }

    private DynamicObject generateLabelParam(Long fieldId, Long lblId, Long lblObjId, Long sceneId) {
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_labelparam");
        DynamicObject dynamicObject = serviceHelper.generateEmptyDynamicObject();
        dynamicObject.set("param", (Object)fieldId);
        dynamicObject.set("label", (Object)lblId);
        dynamicObject.set("labelvalue", (Object)0L);
        dynamicObject.set("labelobject", (Object)lblObjId);
        dynamicObject.set("brmscene", (Object)sceneId);
        dynamicObject.set("source", (Object)"20");
        return dynamicObject;
    }

    private void invokeModifySceneService(Map<String, Object> updateMap) {
        updateMap.put("status", "C");
        Map resultMap = (Map)HRMServiceHelper.invokeHRMPService((String)"brm", (String)"IBRMSceneService", (String)"modifyScene", (Object[])new Object[]{updateMap});
        if (200 != (Integer)resultMap.get("resultCode")) {
            LOGGER.error("saveScene_error:{}", updateMap);
            throw new KDBizException(resultMap.get("errorMsg").toString());
        }
    }
}
