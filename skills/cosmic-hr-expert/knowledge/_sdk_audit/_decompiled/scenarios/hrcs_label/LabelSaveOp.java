/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper
 *  kd.hr.hrcs.common.constants.label.LabelManageConstants
 *  kd.hr.hrcs.common.constants.label.LabelTypeEnum
 *  kd.hr.hrcs.common.model.label.LabelSaveSceneBO
 *  kd.hr.hrcs.opplugin.validator.label.LabelSaveValidator
 */
package kd.hr.hrcs.opplugin.web.label;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.servicehelper.label.LabelServiceHelper;
import kd.hr.hrcs.common.constants.label.LabelManageConstants;
import kd.hr.hrcs.common.constants.label.LabelTypeEnum;
import kd.hr.hrcs.common.model.label.LabelSaveSceneBO;
import kd.hr.hrcs.opplugin.validator.label.LabelSaveValidator;

public final class LabelSaveOp
extends HRDataBaseOp
implements LabelManageConstants {
    private static final Log LOGGER = LogFactory.getLog(LabelSaveOp.class);
    private static final String KEY_DEL_ENTRY = "delEntryId";
    private static final String KEY_DEL_ENTRY_VALUE = "delEntryValueId";
    private static final String KEY_UPDATE_LABEL_VALUE = "updateLabelValueId";
    private static final LabelServiceHelper labelServiceHelper = new LabelServiceHelper();
    private static final String OP_PLUGIN = "hrmp-hrcs-opplugin";

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator((AbstractValidator)new LabelSaveValidator());
    }

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        e.getFieldKeys().add("type");
        e.getFieldKeys().add("configtype");
        e.getFieldKeys().add("entryentityrange");
        e.getFieldKeys().add("entryentityrange.labelobject");
        e.getFieldKeys().add("entryentityrange.brmscene");
        e.getFieldKeys().add("entryentitylabelvalue.labelvalue");
        e.getFieldKeys().add("entryentitylabelvalue.labelvaluedesc");
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        List<Map<String, Long>> deleteInfoList = this.getDeleteSceneInfoList(dataEntities);
        this.getOption().setVariableValue(KEY_DEL_ENTRY, SerializationUtils.toJsonString(deleteInfoList));
        List<Long> delLabelValueList = this.getDelLabelValueList(dataEntities);
        this.getOption().setVariableValue(KEY_DEL_ENTRY_VALUE, SerializationUtils.toJsonString(delLabelValueList));
        List<Long> updateLabelValueIdList = this.getUpdateLabelValueIdList(dataEntities);
        this.getOption().setVariableValue(KEY_UPDATE_LABEL_VALUE, SerializationUtils.toJsonString(updateLabelValueIdList));
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        if (dataEntities == null || dataEntities.length == 0) {
            return;
        }
        if (dataEntities.length == 1) {
            DynamicObject label = dataEntities[0];
            List deleteInfoList = (List)SerializationUtils.fromJsonString((String)this.getOption().getVariableValue(KEY_DEL_ENTRY), List.class);
            List deleteLabelObjectId = deleteInfoList.stream().map(data -> (Long)data.get("labelobject")).collect(Collectors.toList());
            try (TXHandle ignored = TX.requiresNew();){
                DynamicObjectCollection newColl = labelServiceHelper.queryLabelObjectRelCollection(label.getLong("id"));
                if (null != newColl) {
                    DynamicObjectCollection coll = label.getDynamicObjectCollection("entryentityrange");
                    Map<Long, DynamicObject> idMap = newColl.stream().collect(Collectors.toMap(dy -> dy.getLong("labelobject.id"), dy -> dy.getDynamicObject("brmscene")));
                    for (DynamicObject dy2 : coll) {
                        if (!idMap.containsKey(dy2.getLong("labelobject.id")) || deleteLabelObjectId.contains(dy2.getLong("labelobject.id"))) continue;
                        dy2.set("brmscene", (Object)idMap.get(dy2.getLong("labelobject.id")));
                    }
                }
            }
        }
        HashMap savingMap = Maps.newHashMapWithExpectedSize((int)dataEntities.length);
        for (DynamicObject label : dataEntities) {
            savingMap.put(label.getLong("id"), label);
            this.handleBeforeSave(label);
            this.saveValue(label.getDynamicObjectCollection("entryentitylabelvalue"), label);
        }
        this.sceneBack(savingMap);
    }

    private void handleBeforeSave(DynamicObject label) {
        DynamicObjectCollection entryEntityRange = label.getDynamicObjectCollection("entryentityrange");
        this.deleteSceneAndTarget(label.getLong("id"));
        this.handleScene(entryEntityRange, label);
        this.handleConfigTypeChanged(label);
    }

    private void handleConfigTypeChanged(DynamicObject label) {
        String variableValue = (String)this.getOption().getVariables().get("isTypeChanged");
        if (Boolean.TRUE.toString().equals(variableValue)) {
            this.deleteByChangeConfigType(label);
        }
    }

    private void handleScene(DynamicObjectCollection newEntryData, DynamicObject label) {
        if (newEntryData == null || newEntryData.isEmpty()) {
            return;
        }
        Map<Long, Long> labelSceneMap = newEntryData.stream().collect(Collectors.toMap(dy -> dy.getLong("labelobject.id"), dy -> dy.getLong("brmscene.id")));
        HashMap newSceneMap = Maps.newHashMapWithExpectedSize((int)newEntryData.size());
        for (DynamicObject newEntry : newEntryData) {
            Map brmScene;
            long labelObjectId = newEntry.getLong("labelobject.id");
            String labelObjectName = newEntry.getString("labelobject.name");
            long brmSceneId = labelSceneMap.getOrDefault(labelObjectId, 0L);
            if (brmSceneId == 0L) {
                brmScene = labelServiceHelper.createBrmScene(label, labelObjectId, labelObjectName, new ArrayList(), this.geneBrmSceneOutputParam());
                if (!"200".equals(brmScene.get("resultCode").toString())) {
                    throw new KDBizException(brmScene.get("errorMsg").toString());
                }
                brmSceneId = Long.parseLong(brmScene.get("sceneId").toString());
                newEntry.set("brmscene", (Object)brmSceneId);
                newSceneMap.put(brmSceneId, true);
                continue;
            }
            brmScene = labelServiceHelper.updateBrmScene(Long.valueOf(brmSceneId), labelObjectName, label, new ArrayList(), new ArrayList());
            if (!"200".equals(brmScene.get("resultCode").toString())) {
                throw new KDBizException(brmScene.get("errorMsg").toString());
            }
            newSceneMap.put(brmSceneId, false);
        }
        this.getOption().setVariableValue("newScene", SerializationUtils.toJsonString((Object)newSceneMap));
    }

    private void saveValue(DynamicObjectCollection entryEntityLabelValue, DynamicObject label) {
        if (entryEntityLabelValue.isEmpty()) {
            return;
        }
        String labelType = label.getString("type");
        if (LabelTypeEnum.FACT.getType().equals(labelType)) {
            String configType = label.getString("configtype");
            if ("1".equals(configType)) {
                this.saveValueWithRule(entryEntityLabelValue, label);
            } else {
                this.saveValueWithPlugin(label);
            }
        }
    }

    private void saveValueWithRule(DynamicObjectCollection entryEntityLabelValue, DynamicObject label) {
        String variableValue = (String)this.getOption().getVariables().get("expression");
        if (HRStringUtils.isEmpty((String)variableValue)) {
            return;
        }
        List updateLabelValueIdList = (List)SerializationUtils.fromJsonString((String)this.getOption().getVariableValue(KEY_UPDATE_LABEL_VALUE), List.class);
        Map groupExprMap = (Map)SerializationUtils.fromJsonString((String)variableValue, Map.class);
        LinkedHashMap newGroupExprMap = Maps.newLinkedHashMapWithExpectedSize((int)entryEntityLabelValue.size());
        LinkedHashMap updateGroupExprMap = Maps.newLinkedHashMapWithExpectedSize((int)entryEntityLabelValue.size());
        DynamicObjectCollection newLabelValueCol = new DynamicObjectCollection();
        DynamicObjectCollection updateLabelValueCol = new DynamicObjectCollection();
        int newCount = 0;
        int updateCount = 0;
        for (int i = 0; i < entryEntityLabelValue.size(); ++i) {
            DynamicObject row = (DynamicObject)entryEntityLabelValue.get(i);
            Map exprMap = (Map)groupExprMap.get(i + "");
            if (updateLabelValueIdList.contains(row.getLong("id"))) {
                updateLabelValueCol.add((Object)row);
                updateGroupExprMap.put(updateCount, exprMap);
                ++updateCount;
                continue;
            }
            newLabelValueCol.add((Object)row);
            newGroupExprMap.put(newCount, exprMap);
            ++newCount;
        }
        LabelSaveSceneBO labelSaveSceneBO = labelServiceHelper.handleScene(label, entryEntityLabelValue, groupExprMap);
        String newSceneMapStr = (String)this.getOption().getVariables().get("newScene");
        HashMap newSceneMap = HRStringUtils.isEmpty((String)newSceneMapStr) ? Maps.newHashMap() : (Map)SerializationUtils.fromJsonString((String)newSceneMapStr, Map.class);
        labelServiceHelper.addLabelValueFact(newLabelValueCol, label, (Map)newGroupExprMap, labelSaveSceneBO.getAddParamMap(), (Map)newSceneMap);
        labelServiceHelper.updateLabelValueFact(updateLabelValueCol, label, (Map)updateGroupExprMap, labelSaveSceneBO.getAddParamMap(), (Map)newSceneMap);
        labelServiceHelper.deleteSceneParam(labelSaveSceneBO.getDelParamMap(), label);
    }

    private void saveValueWithPlugin(DynamicObject label) {
        String variableValue = (String)this.getOption().getVariables().get("service");
        if (!HRStringUtils.isEmpty((String)variableValue)) {
            Map serviceGroupMap = (Map)SerializationUtils.fromJsonString((String)variableValue, Map.class);
            List updateLabelValueIdList = (List)SerializationUtils.fromJsonString((String)this.getOption().getVariableValue(KEY_UPDATE_LABEL_VALUE), List.class);
            DynamicObjectCollection newLabelValueCol = new DynamicObjectCollection();
            DynamicObjectCollection updateLabelValueRuleCol = new DynamicObjectCollection();
            DynamicObject[] rulesByLabelValue = labelServiceHelper.getRulesByLabelValue(updateLabelValueIdList);
            Map<String, DynamicObject> ruleMap = Arrays.stream(rulesByLabelValue).collect(Collectors.toMap(ru -> ru.getString("labelvalue.id") + ru.getString("labelobject.id"), ru -> ru));
            DynamicObjectCollection entryEntityLabelValue = label.getDynamicObjectCollection("entryentitylabelvalue");
            DynamicObjectCollection entryEntityLabelObj = label.getDynamicObjectCollection("entryentityrange");
            HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_labelvaluerule");
            for (int i = 0; i < entryEntityLabelValue.size(); ++i) {
                DynamicObject labelValue = (DynamicObject)entryEntityLabelValue.get(i);
                long lblValueId = labelValue.getLong("id");
                Map sonGroup = (Map)serviceGroupMap.get(i + "");
                for (DynamicObject lblObjEntry : entryEntityLabelObj) {
                    String lblObjId = lblObjEntry.getString("labelobject.id");
                    DynamicObject lblValueRule = ruleMap.get(lblValueId + lblObjId);
                    if (lblValueRule != null) {
                        lblValueRule.set("brmtarget", null);
                        updateLabelValueRuleCol.add((Object)lblValueRule);
                    } else {
                        lblValueRule = serviceHelper.generateEmptyDynamicObject();
                        lblValueRule.set("labelvalue", (Object)labelValue);
                        lblValueRule.set("labelobject", lblObjEntry.get("labelobject"));
                        newLabelValueCol.add((Object)lblValueRule);
                    }
                    lblValueRule.set("service", sonGroup.get(lblObjId));
                }
            }
            serviceHelper.save(newLabelValueCol);
            serviceHelper.save(updateLabelValueRuleCol);
        }
    }

    private List<Map<String, Object>> geneBrmSceneOutputParam() {
        ArrayList<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("outputname", ResManager.loadKDString((String)"\u4e8b\u5b9e\u6807\u7b7e\u503c", (String)"LabelSaveOp_0", (String)OP_PLUGIN, (Object[])new Object[0]));
        map.put("outputnumber", "bizLabelValue");
        map.put("outputparamstype", "string");
        outputList.add(map);
        HashMap<String, String> labelMap = new HashMap<String, String>();
        labelMap.put("outputname", ResManager.loadKDString((String)"\u6807\u7b7e\u503c", (String)"LabelSaveOp_1", (String)OP_PLUGIN, (Object[])new Object[0]));
        labelMap.put("outputnumber", "labelValue");
        labelMap.put("outputparamstype", "basedata");
        labelMap.put("outputobject", "hrcs_labelvalue");
        labelMap.put("outputmultiple", "1");
        outputList.add(labelMap);
        return outputList;
    }

    private List<Map<String, Long>> getDeleteSceneInfoList(DynamicObject[] dataEntities) {
        ArrayList labelIdList = Lists.newArrayListWithExpectedSize((int)dataEntities.length);
        ArrayList entryIdList = Lists.newArrayListWithExpectedSize((int)10);
        for (DynamicObject dataEntity : dataEntities) {
            labelIdList.add(dataEntity.getLong("id"));
            DynamicObjectCollection newEntry = dataEntity.getDynamicObjectCollection("entryentityrange");
            newEntry.stream().filter(row -> row.getLong("id") != 0L).forEach(row -> entryIdList.add(row.getLong("id")));
        }
        DynamicObject[] labels = labelServiceHelper.getLabels((List)labelIdList);
        ArrayList deleteInfoList = Lists.newArrayListWithExpectedSize((int)10);
        for (DynamicObject label : labels) {
            DynamicObjectCollection entry = label.getDynamicObjectCollection("entryentityrange");
            for (DynamicObject row2 : entry) {
                if (entryIdList.contains(row2.getLong("id"))) continue;
                HashMap map = Maps.newHashMapWithExpectedSize((int)16);
                map.put("label", label.getLong("id"));
                map.put("labelobject", row2.getLong("labelobject.id"));
                map.put("scene", row2.getLong("brmscene.id"));
                deleteInfoList.add(map);
            }
        }
        return deleteInfoList;
    }

    private List<Long> getDelLabelValueList(DynamicObject[] dataEntities) {
        ArrayList labelIdList = Lists.newArrayListWithExpectedSize((int)dataEntities.length);
        ArrayList entryLVRIdList = Lists.newArrayListWithExpectedSize((int)10);
        for (DynamicObject dataEntity : dataEntities) {
            if (LabelTypeEnum.MODEL.getType().equals(dataEntity.getString("type"))) continue;
            labelIdList.add(dataEntity.getLong("id"));
            DynamicObjectCollection newEntry = dataEntity.getDynamicObjectCollection("entryentitylabelvalue");
            newEntry.stream().filter(row -> row.getLong("id") != 0L).forEach(row -> entryLVRIdList.add(row.getLong("id")));
        }
        DynamicObject[] labelValues = labelServiceHelper.getLabelValueCollection((List)labelIdList);
        ArrayList delLabelValueList = Lists.newArrayListWithExpectedSize((int)10);
        for (DynamicObject labelValue : labelValues) {
            long lblValueId = labelValue.getLong("id");
            if (entryLVRIdList.contains(lblValueId)) continue;
            delLabelValueList.add(lblValueId);
        }
        return delLabelValueList;
    }

    private List<Long> getUpdateLabelValueIdList(DynamicObject[] dataEntities) {
        ArrayList labelIdList = Lists.newArrayListWithExpectedSize((int)dataEntities.length);
        for (DynamicObject label : dataEntities) {
            labelIdList.add(label.getLong("id"));
        }
        LabelServiceHelper serviceHelper = new LabelServiceHelper();
        return serviceHelper.listLabelValueByLabelIdList((List)labelIdList);
    }

    private void sceneBack(Map<Long, DynamicObject> savingMap) {
        DynamicObject[] dynamicObjects;
        HRBaseServiceHelper labelEntryServiceHelper = new HRBaseServiceHelper("hrcs_label");
        for (DynamicObject dynamicObject : dynamicObjects = labelEntryServiceHelper.loadDynamicObjectArray(savingMap.keySet().toArray())) {
            long dBId = dynamicObject.getLong("id");
            DynamicObject saving = savingMap.get(dBId);
            DynamicObjectCollection savingEntry = saving.getDynamicObjectCollection("entryentityrange");
            DynamicObjectCollection dBEntry = dynamicObject.getDynamicObjectCollection("entryentityrange");
            Map<Long, Object> savingSceneMap = savingEntry.stream().collect(Collectors.toMap(savingRow -> savingRow.getLong("id"), savingRow -> savingRow.get("brmscene")));
            dBEntry.forEach(dBRow -> {
                Object savingScene = savingSceneMap.get(dBRow.getLong("id"));
                dBRow.set("brmscene", savingScene);
            });
        }
        labelEntryServiceHelper.save(dynamicObjects);
    }

    private void deleteSceneAndTarget(Long labelId) {
        String delEntryValueId = (String)this.getOption().getVariables().get(KEY_DEL_ENTRY_VALUE);
        List delEntryValueIdList = (List)SerializationUtils.fromJsonString((String)delEntryValueId, List.class);
        labelServiceHelper.deleteLabelValue(delEntryValueIdList, labelId);
        List deleteInfoList = (List)SerializationUtils.fromJsonString((String)this.getOption().getVariableValue(KEY_DEL_ENTRY), List.class);
        labelServiceHelper.deleteTargetAndScene(deleteInfoList);
    }

    private void deleteByChangeConfigType(DynamicObject label) {
        long lblId = label.getLong("id");
        DynamicObjectCollection entry = label.getDynamicObjectCollection("entryentitylabelvalue");
        List lblValueIdList = entry.stream().map(et -> et.getLong("id")).collect(Collectors.toList());
        DynamicObject[] rulesByLabelValue = labelServiceHelper.getRulesByLabelValue(lblValueIdList);
        labelServiceHelper.deleteTargetAndParams(lblValueIdList, rulesByLabelValue, Long.valueOf(lblId));
    }
}
