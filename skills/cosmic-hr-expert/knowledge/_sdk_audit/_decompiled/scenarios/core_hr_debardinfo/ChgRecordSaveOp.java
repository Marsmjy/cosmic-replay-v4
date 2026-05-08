/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.service.operation.OperationServiceImpl
 *  kd.bos.servicehelper.BusinessDataServiceHelper
 *  kd.hr.hbp.business.domain.model.timeline.TimelineChangeIdInfo
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.hpfs.chgrecord.serialization.CustomDynamicObjectJsonSerializer
 *  kd.hr.hbp.common.model.history.HisVersionChangeIdInfo
 *  kd.hr.hbp.common.util.HRArrayUtils
 *  kd.hr.hbp.common.util.HRCollUtil
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.domain.config.IDevParamConfigDomainService
 *  kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp$ChgRecordContext
 *  org.apache.commons.lang3.time.StopWatch
 */
package kd.hrmp.hrpi.opplugin.web.chgrecord;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.service.operation.OperationServiceImpl;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.hr.hbp.business.domain.model.timeline.TimelineChangeIdInfo;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.hpfs.chgrecord.serialization.CustomDynamicObjectJsonSerializer;
import kd.hr.hbp.common.model.history.HisVersionChangeIdInfo;
import kd.hr.hbp.common.util.HRArrayUtils;
import kd.hr.hbp.common.util.HRCollUtil;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.domain.config.IDevParamConfigDomainService;
import kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp;
import org.apache.commons.lang3.time.StopWatch;

public final class ChgRecordSaveOp
extends HRDataBaseOp {
    private static final Log logger = LogFactory.getLog(ChgRecordSaveOp.class);
    private static final HRBaseServiceHelper CHG_RECORD_SERVICE_HELPER = new HRBaseServiceHelper("hpfs_chgrecord");
    private static final HRBaseServiceHelper chgRecordEntryServiceHelper = new HRBaseServiceHelper("hpfs_chgrecordentry");
    private final Set<Long> deleteIdSet = ConcurrentHashMap.newKeySet(16);
    private final Map<String, Set<DynamicObject>> operateDataMap = new ConcurrentHashMap<String, Set<DynamicObject>>(16);
    private String privacyPropJson = null;
    private DynamicObject[] deleteObjs = null;
    private static final ThreadLocal<ChgRecordContext> CHG_RECORD_CONTEXT = new ThreadLocal();

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        List fieldKeys = e.getFieldKeys();
        Map allFields = this.billEntityType.getAllFields();
        if (allFields.containsKey("employee")) {
            fieldKeys.add("employee");
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Map variables = this.operateOption.getVariables();
        if (HRStringUtils.equals((String)((String)variables.get("hr_hrdmsynctag_of_datasource")), (String)Boolean.TRUE.toString()) || HRStringUtils.equals((String)((String)variables.get("hr_hrdmvalidatetag_of_datasource")), (String)Boolean.TRUE.toString())) {
            return;
        }
        Object[] dataEntities = e.getDataEntities();
        if (ObjectUtils.isEmpty((Object[])dataEntities)) {
            return;
        }
        this.privacyPropJson = IDevParamConfigDomainService.getInstance().queryBusinessValueByBusinessKey("hpfs_chgrecord.fieldblacklist");
        String operateKey = e.getOperationKey();
        String operationKey = String.valueOf(this.operateMeta.get("type"));
        logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u5b9e\u4f53\u7f16\u7801\uff1a{}\uff0c\u64cd\u4f5c\uff1a{}", (Object)dataEntities[0].getDataEntityType().getName(), (Object)operationKey);
        if ("save".equals(operationKey) || "delete".equals(operationKey) || "expire".equals(operateKey)) {
            HashMap<Long, DynamicObject> recordObjMap = new HashMap<Long, DynamicObject>(16);
            if (!this.operateOption.containsVariable("chgRecordId")) {
                logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u65e0\u53d8\u52a8\u8bb0\u5f55\u573a\u666f");
                this.processChgRecord((DynamicObject[])dataEntities, operationKey, "effect", recordObjMap);
            } else {
                this.processChgRecordAndEntityMapper(recordObjMap);
                this.processChgRecord((DynamicObject[])dataEntities, operationKey, "save", recordObjMap);
            }
            OperateOption newOperateOption = this.operateOption.copy();
            newOperateOption.setVariableValue("ishasright", Boolean.TRUE.toString());
            newOperateOption.setVariableValue("skipCheckDataPermission", Boolean.TRUE.toString());
            newOperateOption.removeVariable("permissionentityid");
            this.operateDataMap.forEach((op, dataList) -> {
                StopWatch stopWatch1 = new StopWatch();
                stopWatch1.start();
                OperationServiceImpl operationService = new OperationServiceImpl();
                operationService.localInvokeOperation(op, dataList.toArray(new DynamicObject[0]), newOperateOption);
                stopWatch1.stop();
                logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u64cd\u4f5c\uff1a{}\uff0c\u8017\u65f6\uff1a{}", op, (Object)stopWatch1.getNanoTime());
            });
        }
        stopWatch.stop();
        logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u8017\u65f6\uff1a{}", (Object)stopWatch.getNanoTime());
    }

    private void processChgRecordAndEntityMapper(Map<Long, DynamicObject> recordObjMap) {
        HashMap recordIdMap = (HashMap)JSONObject.parseObject((String)this.operateOption.getVariableValue("chgRecordId"), Map.class);
        logger.info("ChgRecordSaveOp.processChgRecordAndEntityMapper,recordIdMapStr:{},recordIdMap:{}", (Object)this.operateOption.getVariableValue("chgRecordId"), (Object)recordIdMap);
        Object[] records = new HRBaseServiceHelper("hpfs_chgrecord").loadDynamicObjectArray((Object[])recordIdMap.values().toArray(new Long[0]));
        if (!ObjectUtils.isEmpty((Object[])records)) {
            Map<Long, DynamicObject> recordMap = Arrays.stream(records).collect(Collectors.toMap(it -> it.getLong("id"), it -> it, (oldV, newV) -> newV));
            recordIdMap.forEach((entityId, recordId) -> {
                if (recordMap.containsKey(recordId)) {
                    recordObjMap.put((Long)entityId, (DynamicObject)recordMap.get(recordId));
                }
            });
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void processChgRecord(DynamicObject[] dataEntities, String operationKey, String opKey, Map<Long, DynamicObject> recordObjMap) {
        try {
            CHG_RECORD_CONTEXT.set(new ChgRecordContext(null));
            if (this.getOption().containsVariable("hisVersionChangeInfo") && dataEntities[0].containsProperty("boid")) {
                logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u5904\u7406\u5386\u53f2\u6a21\u578b\u6570\u636e");
                this.processHisModeEntities(dataEntities, operationKey, opKey, recordObjMap);
            } else if (this.getOption().containsVariable("timeLineOpChangeInfo") && dataEntities[0].containsProperty("startdate") && dataEntities[0].containsProperty("enddate")) {
                logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u5904\u7406\u65f6\u95f4\u8f74\u6570\u636e");
                this.processTimeLineModeEntities(dataEntities, operationKey, opKey, recordObjMap);
            } else {
                logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u5904\u7406\u666e\u901a\u57fa\u7840\u8d44\u6599\u6570\u636e");
                this.processBaseModeEntities(dataEntities, operationKey, opKey, recordObjMap);
            }
        }
        finally {
            CHG_RECORD_CONTEXT.remove();
        }
    }

    private void cacheBeforeObj(List<DynamicObject> dataEntities) {
        if (HRCollUtil.isNotEmpty(dataEntities)) {
            Long[] allIds = (Long[])dataEntities.stream().map(dy -> dy.getLong("id")).toArray(Long[]::new);
            this.getDynamicObjectsByRequireNew(allIds, dataEntities.get(0).getDynamicObjectType());
        }
    }

    private void processHisModeEntities(DynamicObject[] dataEntities, String operationKey, String opKey, Map<Long, DynamicObject> recordObjMap) {
        HashMap<Long, List<DynamicObject>> dataEntityMap = new HashMap<Long, List<DynamicObject>>(16);
        HashSet<Long> idSet = new HashSet<Long>(10);
        this.setDataEntityMap(dataEntities, dataEntityMap, idSet);
        this.processHisVersionChangeEntities(idSet, dataEntityMap, dataEntities[0].getDynamicObjectType());
        List<DynamicObject> dataEntityList = dataEntityMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        this.cacheBeforeObj(dataEntityList);
        dataEntityMap.forEach((boid, entities) -> {
            DynamicObject recordObj = null;
            for (DynamicObject entity : entities) {
                if (!recordObjMap.containsKey(entity.getLong("id"))) continue;
                recordObj = (DynamicObject)recordObjMap.get(entity.getLong("id"));
                break;
            }
            this.doWriteChgRecord(entities.toArray(new DynamicObject[0]), operationKey, opKey, recordObj);
        });
    }

    private void processTimeLineModeEntities(DynamicObject[] dataEntities, String operationKey, String opKey, Map<Long, DynamicObject> recordObjMap) {
        ArrayList<DynamicObject> dataList = new ArrayList<DynamicObject>(10);
        this.setDataList(dataList, dataEntities);
        this.processTimeLineChangeEntities(dataList, dataEntities[0].getDynamicObjectType(), recordObjMap);
        this.cacheBeforeObj(dataList);
        this.writeChgRecord(dataEntities, operationKey, opKey, recordObjMap, dataList);
    }

    private void writeChgRecord(DynamicObject[] dataEntities, String operationKey, String opKey, Map<Long, DynamicObject> recordObjMap, List<DynamicObject> dataList) {
        HashMap<Long, List> dataMapByEmployee = new HashMap<Long, List>(16);
        if (!dataEntities[0].containsProperty("employee")) {
            for (DynamicObject dataEntity : dataEntities) {
                DynamicObject recordObj = recordObjMap.get(dataEntity.getLong("id"));
                this.doWriteChgRecord(new DynamicObject[]{dataEntity}, operationKey, opKey, recordObj);
            }
            return;
        }
        dataList.forEach(data -> {
            if (!dataMapByEmployee.containsKey(data.getLong("employee.id"))) {
                dataMapByEmployee.put(data.getLong("employee.id"), new ArrayList(10));
            }
            ((List)dataMapByEmployee.get(data.getLong("employee.id"))).add(data);
        });
        dataMapByEmployee.forEach((employeeId, entityList) -> {
            HashMap<Long, DynamicObject> objOfNotExistChgRecordMap = new HashMap<Long, DynamicObject>(16);
            HashMap<Long, List> recordAndDysMap = new HashMap<Long, List>(16);
            DynamicObject recordObj = null;
            for (DynamicObject entity : entityList) {
                if (recordObjMap.containsKey(entity.getLong("id"))) {
                    recordObj = (DynamicObject)recordObjMap.get(entity.getLong("id"));
                    long recordId = recordObj.getLong("id");
                    if (!recordAndDysMap.containsKey(recordId)) {
                        recordAndDysMap.put(recordId, new ArrayList(10));
                    }
                    ((List)recordAndDysMap.get(recordId)).add(entity);
                    continue;
                }
                objOfNotExistChgRecordMap.put(entity.getLong("id"), entity);
            }
            recordAndDysMap.forEach((rid, objList) -> this.doWriteChgRecord(objList.toArray(new DynamicObject[0]), operationKey, opKey, (DynamicObject)recordObjMap.get(((DynamicObject)objList.get(0)).getLong("id"))));
            DynamicObject dynamicObject = recordObj = recordObj == null ? this.generateChgRecord((DynamicObject)entityList.get(0)) : recordObj;
            if (objOfNotExistChgRecordMap.size() > 0) {
                this.doWriteChgRecord(objOfNotExistChgRecordMap.values().toArray(new DynamicObject[0]), operationKey, opKey, recordObj);
            }
        });
    }

    private void processBaseModeEntities(DynamicObject[] dataEntities, String operationKey, String opKey, Map<Long, DynamicObject> recordObjMap) {
        ArrayList<DynamicObject> dataList = new ArrayList<DynamicObject>(10);
        this.setDataList(dataList, dataEntities);
        this.processBaseDataEntities(dataList, dataEntities[0].getDynamicObjectType(), recordObjMap);
        this.cacheBeforeObj(dataList);
        this.writeChgRecord(dataEntities, operationKey, opKey, recordObjMap, dataList);
    }

    private void doWriteChgRecord(DynamicObject[] dataEntities, String operationKey, String opKey, DynamicObject recordObj) {
        if (recordObj == null) {
            this.updateChgRecord(dataEntities, operationKey, opKey, this.generateChgRecord(dataEntities[0]));
        } else {
            this.updateChgRecord(dataEntities, operationKey, opKey, recordObj);
        }
    }

    private void setDataList(List<DynamicObject> dataList, DynamicObject[] dataEntities) {
        dataList.addAll(Arrays.asList(dataEntities));
    }

    private void processBaseDataEntities(List<DynamicObject> dataList, DynamicObjectType type, Map<Long, DynamicObject> recordObjMap) {
        if (!this.getOption().containsVariable("genericEntityModifyExtIds")) {
            return;
        }
        Set<Long> extIdSet = (Set<Long>)SerializationUtils.fromJsonString((String)this.getOption().getVariableValue("genericEntityModifyExtIds"), Set.class);
        if (ObjectUtils.isEmpty((Object)extIdSet)) {
            return;
        }
        if ((extIdSet = extIdSet.stream().filter(id -> !recordObjMap.containsKey(id)).collect(Collectors.toSet())).size() > 0) {
            logger.info("ChgRecordSaveOp.processBaseDataEntities:exist extIds");
            Object[] objs = BusinessDataServiceHelper.load((Object[])extIdSet.toArray(new Long[0]), (DynamicObjectType)type);
            if (!ObjectUtils.isEmpty((Object[])objs)) {
                dataList.addAll(Arrays.asList(objs));
            }
        }
    }

    private void processTimeLineChangeEntities(List<DynamicObject> dataList, DynamicObjectType type, Map<Long, DynamicObject> recordObjMap) {
        Object[] objs;
        if (!this.getOption().containsVariable("timeLineOpChangeInfo")) {
            return;
        }
        HashSet<Long> idSet = new HashSet<Long>(10);
        dataList.forEach(data -> idSet.add(data.getLong("id")));
        TimelineChangeIdInfo changeIdInfo = (TimelineChangeIdInfo)SerializationUtils.fromJsonString((String)this.getOption().getVariableValue("timeLineOpChangeInfo"), TimelineChangeIdInfo.class);
        Set<Object> ids = new HashSet<Long>(10);
        List saveDataIds = changeIdInfo.getSaveDataIds();
        List removeDataIds = changeIdInfo.getRemoveDataIds();
        logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u65f6\u95f4\u8f74\u6a21\u578b\u9700\u8981\u4fdd\u5b58\u6570\u636e\uff1a{}", (Object)saveDataIds);
        logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u65f6\u95f4\u8f74\u6a21\u578b\u9700\u8981\u5220\u9664\u6570\u636e\uff1a{}", (Object)removeDataIds);
        ids.addAll(this.getNeedSaveIds(saveDataIds, idSet));
        this.deleteIdSet.addAll(this.getNeedSaveIds(removeDataIds, idSet));
        this.deleteObjs = this.getDynamicObjectsByRequireNew(this.deleteIdSet.toArray(new Long[0]), type);
        if (ObjectUtils.isEmpty(ids)) {
            return;
        }
        if (!ObjectUtils.isEmpty(ids = ids.stream().filter(id -> !recordObjMap.containsKey(id)).collect(Collectors.toSet())) && !ObjectUtils.isEmpty((Object[])(objs = BusinessDataServiceHelper.load((Object[])ids.toArray(new Long[0]), (DynamicObjectType)type)))) {
            dataList.addAll(Arrays.asList(objs));
        }
    }

    private void processHisVersionChangeEntities(Set<Long> idSet, Map<Long, List<DynamicObject>> dataEntityMap, DynamicObjectType type) {
        if (!this.getOption().containsVariable("hisVersionChangeInfo")) {
            return;
        }
        HisVersionChangeIdInfo versionChangeIdInfo = (HisVersionChangeIdInfo)SerializationUtils.fromJsonString((String)this.getOption().getVariableValue("hisVersionChangeInfo"), HisVersionChangeIdInfo.class);
        HashSet<Long> needSaveHisVersionIds = new HashSet<Long>(10);
        needSaveHisVersionIds.addAll(this.getNeedSaveIds(versionChangeIdInfo.getAddVersionIds(), idSet));
        needSaveHisVersionIds.addAll(this.getNeedSaveIds(versionChangeIdInfo.getCoverVersionIds(), idSet));
        needSaveHisVersionIds.addAll(this.getNeedSaveIds(versionChangeIdInfo.getUpdateVersionIds(), idSet));
        logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u5386\u53f2\u6a21\u578b\u65b0\u589e\u6570\u636e\uff1a{}", (Object)versionChangeIdInfo.getAddVersionIds());
        logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u5386\u53f2\u6a21\u578b\u8986\u76d6\u6570\u636e\uff1a{}", (Object)versionChangeIdInfo.getCoverVersionIds());
        logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u5386\u53f2\u6a21\u578b\u66f4\u65b0\u6570\u636e\uff1a{}", (Object)versionChangeIdInfo.getUpdateVersionIds());
        dataEntityMap.keySet().forEach(boid -> {
            if (!idSet.contains(boid)) {
                needSaveHisVersionIds.add((Long)boid);
            }
        });
        if (needSaveHisVersionIds.size() < 1) {
            return;
        }
        Object[] hisObjects = BusinessDataServiceHelper.load((Object[])needSaveHisVersionIds.toArray(new Long[0]), (DynamicObjectType)type);
        if (ObjectUtils.isEmpty((Object[])hisObjects)) {
            return;
        }
        this.setDataEntityMap((DynamicObject[])hisObjects, dataEntityMap, idSet);
        logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u5386\u53f2\u6a21\u578b\u573a\u666f\uff0c\u5f53\u524d\u6240\u6709\u53d8\u52a8id\uff1a{}", idSet);
    }

    private void setDataEntityMap(DynamicObject[] dataEntities, Map<Long, List<DynamicObject>> dataEntityMap, Set<Long> idSet) {
        Arrays.stream(dataEntities).forEach(dataEntity -> {
            idSet.add(dataEntity.getLong("id"));
            if (!dataEntityMap.containsKey(dataEntity.getLong("boid"))) {
                dataEntityMap.put(dataEntity.getLong("boid"), new ArrayList(10));
            }
            ((List)dataEntityMap.get(dataEntity.getLong("boid"))).add(dataEntity);
        });
    }

    private List<Long> getNeedSaveIds(List<Long> idList, Set<Long> idSet) {
        if (ObjectUtils.isEmpty(idList)) {
            return idList;
        }
        for (int i = idList.size() - 1; i > -1; --i) {
            if (!idSet.contains(idList.get(i))) continue;
            idList.remove(i);
        }
        return idList;
    }

    private void updateChgRecord(DynamicObject[] dataEntities, String operationKey, String opKey, DynamicObject recordObj) {
        if (recordObj == null) {
            return;
        }
        logger.info("\u4e8b\u52a1\u53d8\u52a8\u8bb0\u5f55\uff0c\u6267\u884c\u66f4\u65b0\uff0c\u64cd\u4f5c\uff1a{}", (Object)operationKey);
        if ("save".equals(operationKey)) {
            this.updateChgRecordBySave(dataEntities, recordObj, opKey);
        } else {
            this.updateChgRecordByDelete(dataEntities, recordObj, opKey);
        }
    }

    private DynamicObject generateChgRecord(DynamicObject obj) {
        DynamicObject recordObj = CHG_RECORD_SERVICE_HELPER.generateEmptyDynamicObject();
        this.setCommonProperties(recordObj, obj);
        return recordObj;
    }

    private void setCommonProperties(DynamicObject recordObj, DynamicObject entityObj) {
        recordObj.set("chgaction", (Object)201240L);
        recordObj.set("chgcategory", (Object)1240L);
        recordObj.set("datastatus", (Object)"1");
        recordObj.set("billsource", (Object)entityObj.getDataEntityType().getName());
        recordObj.set("effecttime", (Object)HRDateTimeUtils.curDate());
        if (!entityObj.containsProperty("employee")) {
            if ("hrpi_employee".equals(entityObj.getDynamicObjectType().getName())) {
                this.setEmployeeInfoBySeft(recordObj, entityObj);
            }
            return;
        }
        DynamicObject employee = entityObj.getDynamicObject("employee");
        if (employee == null) {
            return;
        }
        recordObj.set("employee", (Object)employee);
        if (employee.containsProperty("name")) {
            recordObj.set("name", (Object)employee.getString("name"));
        }
        if (employee.containsProperty("empnumber")) {
            recordObj.set("number", (Object)employee.getString("empnumber"));
        }
    }

    private void setEmployeeInfoBySeft(DynamicObject recordObj, DynamicObject entityObj) {
        recordObj.set("employee", entityObj.get("boid"));
        if (entityObj.containsProperty("name")) {
            recordObj.set("name", (Object)entityObj.getString("name"));
        }
        if (entityObj.containsProperty("empnumber")) {
            recordObj.set("number", (Object)entityObj.getString("empnumber"));
        }
    }

    private void updateChgRecordBySave(DynamicObject[] dataEntities, DynamicObject recordObj, String operationKey) {
        HashSet ids = new HashSet(dataEntities.length);
        Arrays.stream(dataEntities).forEach(dataEntity -> ids.add(dataEntity.getLong("id")));
        MainEntityType dynamicObjectType = EntityMetadataCache.getDataEntityType((String)dataEntities[0].getDataEntityType().getName());
        Map<Long, DynamicObject> entityMap = this.getDynamicObjectMap(dataEntities);
        Map<Long, String> beforeChangeDataMap = this.getBeforeChangeData(ids.toArray(new Long[0]), (DynamicObjectType)dynamicObjectType, entityMap, recordObj);
        Map<Long, String> afterChangeDataMap = this.getAfterChangeData(dataEntities, recordObj);
        this.invokeChgRecordSaveOp(recordObj, beforeChangeDataMap, afterChangeDataMap, entityMap, operationKey);
    }

    private void updateChgRecordByDelete(DynamicObject[] dataEntities, DynamicObject recordObj, String operationKey) {
        ArrayList<Long> ids = new ArrayList<Long>(dataEntities.length);
        Arrays.stream(dataEntities).forEach(dataEntity -> ids.add(dataEntity.getLong("id")));
        Map<Long, DynamicObject> entityMap = this.getDynamicObjectMap(dataEntities);
        MainEntityType dynamicObjectType = EntityMetadataCache.getDataEntityType((String)dataEntities[0].getDataEntityType().getName());
        Map<Long, String> beforeChangeDataMap = this.getBeforeChangeData(ids.toArray(new Long[0]), (DynamicObjectType)dynamicObjectType, entityMap, recordObj);
        Map<Long, String> afterChangeDataMap = this.getAfterChangeData(this.getDataEntitiesByIds(ids, (DynamicObjectType)dynamicObjectType), recordObj);
        this.invokeChgRecordSaveOp(recordObj, beforeChangeDataMap, afterChangeDataMap, entityMap, operationKey);
    }

    private DynamicObject[] getDataEntitiesByIds(List<Long> ids, DynamicObjectType dynamicObjectType) {
        return BusinessDataServiceHelper.load((Object[])ids.toArray(new Long[0]), (DynamicObjectType)dynamicObjectType);
    }

    private Map<Long, DynamicObject> getDynamicObjectMap(DynamicObject[] dataEntities) {
        HashMap<Long, DynamicObject> objectMap = new HashMap<Long, DynamicObject>(16);
        if (ObjectUtils.isEmpty((Object[])dataEntities)) {
            return objectMap;
        }
        Arrays.stream(dataEntities).forEach(dataEntity -> objectMap.putIfAbsent(dataEntity.getLong("id"), (DynamicObject)dataEntity));
        return objectMap;
    }

    private Map<Long, String> getBeforeChangeData(Long[] ids, DynamicObjectType dynamicObjectType, Map<Long, DynamicObject> entityMap, DynamicObject recordObj) {
        HashMap<Long, String> retMap = new HashMap<Long, String>(16);
        if (ObjectUtils.isEmpty((Object[])ids)) {
            return retMap;
        }
        DynamicObject[] beforeDys = this.getDynamicObjectsByRequireNew(ids, dynamicObjectType);
        Set<DynamicObject> filteredChangeData = this.filterChangeData(beforeDys, recordObj.getLong("employee.id"));
        filteredChangeData.forEach(dy -> retMap.put(dy.getLong("id"), CustomDynamicObjectJsonSerializer.convertDynamicObjectToJson((DynamicObject)dy, (String)this.privacyPropJson)));
        if (!ObjectUtils.isEmpty((Object[])this.deleteObjs)) {
            Arrays.stream(this.deleteObjs).forEach(dynamicObject -> {
                retMap.put(dynamicObject.getLong("id"), CustomDynamicObjectJsonSerializer.convertDynamicObjectToJson((DynamicObject)dynamicObject, (String)this.privacyPropJson));
                if (dynamicObject.getLong("employee.id") == recordObj.getLong("employee.id")) {
                    entityMap.put(dynamicObject.getLong("id"), (DynamicObject)dynamicObject);
                }
            });
        }
        return retMap;
    }

    private Set<DynamicObject> filterChangeData(DynamicObject[] changeDataDys, long employeeId) {
        if (changeDataDys == null || changeDataDys.length == 0) {
            return new HashSet<DynamicObject>();
        }
        IDataEntityType dataEntityType = changeDataDys[0].getDataEntityType();
        if (dataEntityType.getProperties().containsKey((Object)"employee")) {
            return Arrays.stream(changeDataDys).filter(changeDataDy -> changeDataDy.getLong("employee.id") == employeeId).collect(Collectors.toSet());
        }
        return Arrays.stream(changeDataDys).collect(Collectors.toSet());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private DynamicObject[] getDynamicObjectsByRequireNew(Long[] ids, DynamicObjectType dynamicObjectType) {
        Map beforeObjCache = ChgRecordSaveOp.CHG_RECORD_CONTEXT.get().beforeObjCache;
        Object[] missIds = (Long[])Arrays.stream(ids).filter(id -> !beforeObjCache.containsKey(id)).toArray(Long[]::new);
        if (missIds.length > 0) {
            Object[] dynamicObjects = null;
            try (TXHandle tx = TX.requiresNew();){
                dynamicObjects = BusinessDataServiceHelper.load((Object[])missIds, (DynamicObjectType)dynamicObjectType);
                tx.commit();
            }
            HashMap dyMap = new HashMap();
            if (HRArrayUtils.isNotEmpty((Object[])dynamicObjects)) {
                Arrays.stream(dynamicObjects).forEach(dy -> dyMap.put(dy.getLong("id"), dy));
            }
            Arrays.stream(missIds).forEach(id -> {
                DynamicObject cfr_ignored_0 = beforeObjCache.put(id, dyMap.getOrDefault(id, null));
            });
        }
        return (DynamicObject[])Arrays.stream(ids).filter(beforeObjCache::containsKey).filter(id -> !HRObjectUtils.isEmpty(beforeObjCache.get(id))).map(beforeObjCache::get).toArray(DynamicObject[]::new);
    }

    private Map<Long, String> getAfterChangeData(DynamicObject[] objs, DynamicObject recordObj) {
        HashMap<Long, String> retMap = new HashMap<Long, String>(16);
        if (ObjectUtils.isEmpty((Object[])objs)) {
            return retMap;
        }
        Set<DynamicObject> filteredChangeData = this.filterChangeData(objs, recordObj.getLong("employee.id"));
        filteredChangeData.forEach(dy -> retMap.put(dy.getLong("id"), CustomDynamicObjectJsonSerializer.convertDynamicObjectToJson((DynamicObject)dy, (String)this.privacyPropJson)));
        return retMap;
    }

    private void invokeChgRecordSaveOp(DynamicObject chgRecordObj, Map<Long, String> beforeChangeMap, Map<Long, String> afterChangeMap, Map<Long, DynamicObject> entityMap, String operationKey) {
        if ("effect".equals(operationKey)) {
            this.effectChgRecord(chgRecordObj, beforeChangeMap, afterChangeMap, entityMap, operationKey);
        } else if ("save".equals(operationKey)) {
            this.updateChgRecordEntryEntity(operationKey, chgRecordObj, entityMap, beforeChangeMap, afterChangeMap);
        }
    }

    private void effectChgRecord(DynamicObject chgRecordObj, Map<Long, String> beforeChangeMap, Map<Long, String> afterChangeMap, Map<Long, DynamicObject> entityMap, String operationKey) {
        DynamicObjectCollection entryCollection = chgRecordObj.getDynamicObjectCollection("entryentity");
        if (beforeChangeMap.size() > 0) {
            beforeChangeMap.forEach((id, jsonStr) -> {
                if (entityMap.get(id) == null) {
                    return;
                }
                if (afterChangeMap != null && StringUtils.isNotBlank((CharSequence)((CharSequence)afterChangeMap.get(id)))) {
                    this.insertEntry(entryCollection.addNew(), (String)beforeChangeMap.get(id), (String)afterChangeMap.get(id), (DynamicObject)entityMap.get(id));
                } else {
                    this.insertEntry(entryCollection.addNew(), (String)beforeChangeMap.get(id), null, (DynamicObject)entityMap.get(id));
                }
                if (afterChangeMap != null) {
                    afterChangeMap.remove(id);
                }
            });
        }
        if (afterChangeMap != null && afterChangeMap.size() > 0) {
            afterChangeMap.forEach((id, jsonStr) -> {
                if (entityMap.get(id) == null) {
                    return;
                }
                this.insertEntry(entryCollection.addNew(), null, (String)afterChangeMap.get(id), (DynamicObject)entityMap.get(id));
            });
        }
        if (!this.operateDataMap.containsKey(operationKey)) {
            this.operateDataMap.put(operationKey, new HashSet(10));
        }
        this.operateDataMap.get(operationKey).add(chgRecordObj);
    }

    private void updateChgRecordEntryEntity(String operationKey, DynamicObject chgRecordObj, Map<Long, DynamicObject> entityMap, Map<Long, String> beforeChangeMap, Map<Long, String> afterChangeMap) {
        ArrayList entryEntities = new ArrayList(10);
        if (beforeChangeMap.size() > 0) {
            beforeChangeMap.forEach((id, jsonStr) -> {
                if (entityMap.get(id) == null) {
                    return;
                }
                DynamicObject entryEntity = afterChangeMap != null && StringUtils.isNotBlank((CharSequence)((CharSequence)afterChangeMap.get(id))) ? this.generateEntryEntity((String)beforeChangeMap.get(id), (String)afterChangeMap.get(id), (DynamicObject)entityMap.get(id), chgRecordObj) : this.generateEntryEntity((String)beforeChangeMap.get(id), null, (DynamicObject)entityMap.get(id), chgRecordObj);
                if (afterChangeMap != null) {
                    afterChangeMap.remove(id);
                }
                if (entryEntity != null) {
                    entryEntities.add(entryEntity);
                }
            });
        }
        if (afterChangeMap != null && afterChangeMap.size() > 0) {
            afterChangeMap.forEach((id, jsonStr) -> {
                if (entityMap.get(id) == null) {
                    return;
                }
                DynamicObject entryEntity = this.generateEntryEntity(null, (String)afterChangeMap.get(id), (DynamicObject)entityMap.get(id), chgRecordObj);
                if (entryEntity != null) {
                    entryEntities.add(entryEntity);
                }
            });
        }
        if (ObjectUtils.isEmpty(entryEntities)) {
            return;
        }
        if (!this.operateDataMap.containsKey(operationKey)) {
            this.operateDataMap.put(operationKey, new HashSet(10));
        }
        this.operateDataMap.get(operationKey).addAll(entryEntities);
    }

    private void insertEntry(DynamicObject entry, String beforeJson, String afterJson, DynamicObject entity) {
        if (entity == null) {
            return;
        }
        this.setChgRecordEntry(entry, beforeJson, afterJson, entity);
    }

    private DynamicObject generateEntryEntity(String beforeJson, String afterJson, DynamicObject entity, DynamicObject recordObj) {
        if (entity == null) {
            return null;
        }
        DynamicObject entry = chgRecordEntryServiceHelper.generateEmptyDynamicObject();
        entry.set("chgrecord", (Object)recordObj);
        this.setChgRecordEntry(entry, beforeJson, afterJson, entity);
        return entry;
    }

    private void setChgRecordEntry(DynamicObject entry, String beforeJson, String afterJson, DynamicObject entity) {
        entry.set("chgentity", (Object)entity.getDynamicObjectType().getName());
        entry.set("dataid", (Object)entity.getLong("id"));
        this.insertAssignment(entry, entity);
        if (beforeJson == null) {
            entry.set("chgmode", (Object)"0");
            entry.set("dataafter", (Object)this.getDataStr(afterJson));
            entry.set("dataafter_tag", (Object)afterJson);
        } else if (afterJson == null) {
            entry.set("chgmode", (Object)"2");
            entry.set("databefore", (Object)this.getDataStr(beforeJson));
            entry.set("databefore_tag", (Object)beforeJson);
        } else {
            entry.set("chgmode", (Object)"1");
            entry.set("databefore", (Object)this.getDataStr(beforeJson));
            entry.set("databefore_tag", (Object)beforeJson);
            entry.set("dataafter", (Object)this.getDataStr(afterJson));
            entry.set("dataafter_tag", (Object)afterJson);
        }
    }

    private String getDataStr(String str) {
        return str.length() > 200 ? str.substring(0, 200) : str;
    }

    private void insertAssignment(DynamicObject entry, DynamicObject entity) {
        if (entity == null || !entity.containsProperty("assignment") || entity.getDynamicObject("assignment") == null) {
            return;
        }
        entry.set("assignment", (Object)entity.getDynamicObject("assignment").getLong("id"));
    }
}
