/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.hr.hbp.business.util.hpfs.chgrecord.serialization.CustomDynamicObjectJsonSerializer
 *  kd.hr.hbp.common.util.HRArrayUtils
 *  kd.hr.hbp.common.util.HRCollUtil
 *  kd.hr.hbp.common.util.HRDynamicObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hpfs.business.application.service.cert.ILicenseCertApplicationService
 *  kd.hr.hpfs.business.domain.perchg.ChgRecordRepository
 *  kd.hr.hpfs.opplugin.validators.ChgRollbackOtherChgRecordValidator
 *  kd.hr.hpfs.opplugin.validators.ChgRollbackStatusValidator
 *  kd.hrmp.hrpi.business.application.generic.IPersonGenericService
 *  kd.hrmp.hrpi.common.generic.response.ResponseResult
 *  kd.sdk.hr.hpfs.utils.ChgUtils
 *  org.apache.commons.lang3.time.StopWatch
 */
package kd.hr.hpfs.opplugin.op.cross;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.hr.hbp.business.util.hpfs.chgrecord.serialization.CustomDynamicObjectJsonSerializer;
import kd.hr.hbp.common.util.HRArrayUtils;
import kd.hr.hbp.common.util.HRCollUtil;
import kd.hr.hbp.common.util.HRDynamicObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hpfs.business.application.service.cert.ILicenseCertApplicationService;
import kd.hr.hpfs.business.domain.perchg.ChgRecordRepository;
import kd.hr.hpfs.opplugin.validators.ChgRollbackOtherChgRecordValidator;
import kd.hr.hpfs.opplugin.validators.ChgRollbackStatusValidator;
import kd.hrmp.hrpi.business.application.generic.IPersonGenericService;
import kd.hrmp.hrpi.common.generic.response.ResponseResult;
import kd.sdk.hr.hpfs.utils.ChgUtils;
import org.apache.commons.lang3.time.StopWatch;

public final class ChgRollbackOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(ChgRollbackOp.class);
    private Map<Long, List<DynamicObject>> otherChgRecordMap = new ConcurrentHashMap<Long, List<DynamicObject>>();
    private static final List<String> IGNORE_ENTITIES = Lists.newArrayList((Object[])new String[]{"hrpi_employee", "hrpi_assignment", "hrpi_empposorgrel"});

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        super.onPreparePropertys(evt);
        evt.getFieldKeys().addAll(this.billEntityType.getAllFields().keySet());
        evt.getFieldKeys().add("paramdataafter_tag");
        evt.getFieldKeys().add("paramdatabefore_tag");
        evt.getFieldKeys().add("dataafter_tag");
        evt.getFieldKeys().add("databefore_tag");
        evt.getFieldKeys().add("datacompare");
        evt.getFieldKeys().add("datacompare_tag");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator((AbstractValidator)new ChgRollbackStatusValidator());
        e.addValidator((AbstractValidator)new ChgRollbackOtherChgRecordValidator(this.otherChgRecordMap));
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        Map saveBatchParam;
        List saveDataList;
        StopWatch stopWatch = new StopWatch("rollback");
        stopWatch.start();
        Object[] dataEntities = e.getDataEntities();
        if (HRArrayUtils.isEmpty((Object[])dataEntities)) {
            return;
        }
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)"hpfs_chgrecord");
        Set<String> fields = this.billEntityType.getAllFields().keySet();
        this.rollbackOtherChgRecord((DynamicObject[])dataEntities, (DynamicObjectType)dataEntityType, fields);
        DynamicObject[] newChgRecords = (DynamicObject[])Arrays.stream(dataEntities).map(arg_0 -> this.lambda$beginOperationTransaction$0((DynamicObjectType)dataEntityType, fields, arg_0)).toArray(DynamicObject[]::new);
        this.rollbackChargePerson(newChgRecords);
        this.rollbackPersonFlow(newChgRecords);
        Map deleteBatchParam = ChgUtils.buildDeleteBatchParam((DynamicObject[])newChgRecords);
        ChgRecordRepository.save((DynamicObject[])newChgRecords);
        List delDataList = (List)deleteBatchParam.get("data");
        if (HRCollUtil.isNotEmpty((Collection)delDataList)) {
            this.setParamSource(deleteBatchParam);
            ResponseResult deleteBatchResult = IPersonGenericService.getInstance().deleteBatch(deleteBatchParam);
            LOG.info("IPersonGenericService deleteBatch result:{}", (Object)deleteBatchResult.getSuccess());
            this.showErrorMsg(deleteBatchResult);
        }
        if (HRCollUtil.isNotEmpty((Collection)(saveDataList = (List)(saveBatchParam = ChgUtils.buildSaveBatchParam((DynamicObject[])newChgRecords, (boolean)true)).get("data")))) {
            this.setParamSource(saveBatchParam);
            ResponseResult saveBatchResult = IPersonGenericService.getInstance().saveBatch(saveBatchParam);
            LOG.info("IPersonGenericService saveBatchResult result:{}", (Object)saveBatchResult.getSuccess());
            this.showErrorMsg(saveBatchResult);
        }
        this.rollbackLicense((DynamicObject[])dataEntities);
        stopWatch.stop();
        LOG.info("rollback finish ,cost time :{}", (Object)stopWatch.getNanoTime());
    }

    private void rollbackChargePerson(DynamicObject[] newChgRecords) {
        OperationResult operationResult;
        ArrayList<Long> deleteChargePersonIds = new ArrayList<Long>(10);
        ArrayList<DynamicObject> saveChargePersons = new ArrayList<DynamicObject>(10);
        HashMap chgRecordChargePersonParamItemMap = new HashMap(16);
        HashMap<Long, Long> chargePersonChgRecordIdMap = new HashMap<Long, Long>(16);
        HashMap<Long, DynamicObject> chgRecordMap = new HashMap<Long, DynamicObject>(16);
        ArrayList<Long> chargePersonIds = new ArrayList<Long>(10);
        for (DynamicObject newChgRecord : newChgRecords) {
            Long chgRecordId = newChgRecord.getLong("id");
            chgRecordMap.put(chgRecordId, newChgRecord);
            DynamicObjectCollection paramEntry = newChgRecord.getDynamicObjectCollection("paramentry");
            HashMap<Long, DynamicObject> chargePersonParamItemMap = new HashMap<Long, DynamicObject>(16);
            for (DynamicObject paramEntryItem : paramEntry) {
                String entityNum = paramEntryItem.getString("paramchgentity.number");
                if (!HRStringUtils.equals((String)"haos_chargeperson", (String)entityNum)) continue;
                long paramDataId = paramEntryItem.getLong("paramdataid");
                chargePersonParamItemMap.put(paramDataId, paramEntryItem);
                chargePersonChgRecordIdMap.put(paramDataId, chgRecordId);
                chargePersonIds.add(paramDataId);
                String paramChgMode = paramEntryItem.getString("paramchgmode");
                if (HRStringUtils.equals((String)"2", (String)paramChgMode)) {
                    deleteChargePersonIds.add(paramDataId);
                    continue;
                }
                DynamicObject saveChargePerson = CustomDynamicObjectJsonSerializer.parseDynamicObjectJson((DynamicObject)newChgRecord, (String)paramEntryItem.getString("paramdataafter_tag"), (String)entityNum);
                saveChargePersons.add(saveChargePerson);
            }
            chgRecordChargePersonParamItemMap.put(chgRecordId, chargePersonParamItemMap);
        }
        if (HRCollUtil.isNotEmpty(deleteChargePersonIds)) {
            LOG.info("haos_chargeperson executeOperate delete ids:{}", deleteChargePersonIds);
            operationResult = ChgUtils.executeOperate((Object[])deleteChargePersonIds.toArray(new Long[0]), (String)"haos_chargeperson", (String)"donothing_delete");
            LOG.info("haos_chargeperson executeOperate delete result:{}", (Object)operationResult);
            this.showChargePersonErrorMsg(operationResult);
            chargePersonIds.forEach(chargePersonId -> {
                DynamicObject chgRecord = (DynamicObject)chgRecordMap.get(chargePersonChgRecordIdMap.get(chargePersonId));
                Long chgRecordId = chgRecord.getLong("id");
                DynamicObjectCollection entry = chgRecord.getDynamicObjectCollection("entryentity");
                Map chargePersonParamItemMap = (Map)chgRecordChargePersonParamItemMap.get(chgRecordId);
                DynamicObject paramEntryItem = (DynamicObject)chargePersonParamItemMap.get(chargePersonId);
                DynamicObject newEntryItem = entry.addNew();
                ChgUtils.chgRecordEntryItemCopy((DynamicObject)paramEntryItem, (DynamicObject)newEntryItem, (boolean)true, (boolean)false);
            });
        }
        if (HRCollUtil.isNotEmpty(saveChargePersons)) {
            operationResult = ChgUtils.executeOperate((DynamicObject[])saveChargePersons.toArray(new DynamicObject[0]), (String)"save");
            LOG.info("haos_chargeperson executeOperate save result:{}", (Object)operationResult);
            if (operationResult.isSuccess()) {
                Map customDataMap = operationResult.getCustomData();
                customDataMap.forEach((key, chargePersonJson) -> {
                    if (key.startsWith("chargeperson_")) {
                        long dataId = Long.parseLong(key.substring("chargeperson_".length()));
                        long chgRecordId = (Long)chargePersonChgRecordIdMap.get(dataId);
                        DynamicObject chgRecord = (DynamicObject)chgRecordMap.get(chgRecordId);
                        Map chargePersonParamItemMap = (Map)chgRecordChargePersonParamItemMap.get(chgRecordId);
                        DynamicObjectCollection entry = chgRecord.getDynamicObjectCollection("entryentity");
                        DynamicObject entryItem = entry.addNew();
                        DynamicObject paramEntryItem = (DynamicObject)chargePersonParamItemMap.get(dataId);
                        ChgUtils.chgRecordEntryItemCopy((DynamicObject)paramEntryItem, (DynamicObject)entryItem, (boolean)true, (boolean)false);
                        entryItem.set("dataafter_tag", chargePersonJson);
                        entryItem.set("dataafter", (Object)StringUtils.left((String)chargePersonJson, (int)200));
                        ChgUtils.setChgRecordEntryItemDataCompare((DynamicObject)entryItem, (String)entryItem.getString("databefore_tag"), (String)chargePersonJson);
                    }
                });
            } else {
                this.showChargePersonErrorMsg(operationResult);
            }
        }
    }

    private void rollbackPersonFlow(DynamicObject[] newChgRecords) {
        ArrayList<Long> deletePersonFlowIds = new ArrayList<Long>(10);
        for (DynamicObject newChgRecord : newChgRecords) {
            for (DynamicObject paramEntryItem : newChgRecord.getDynamicObjectCollection("entryentity")) {
                String paramChgMode;
                String entityNum = paramEntryItem.getString("chgentity.number");
                if (!HRStringUtils.equals((String)"hpfs_personflow", (String)entityNum) || !HRStringUtils.equals((String)"2", (String)(paramChgMode = paramEntryItem.getString("chgmode")))) continue;
                deletePersonFlowIds.add(paramEntryItem.getLong("dataid"));
            }
        }
        if (HRCollUtil.isNotEmpty(deletePersonFlowIds)) {
            OperationResult operationResult = ChgUtils.executeOperate((Object[])deletePersonFlowIds.toArray(new Long[0]), (String)"hpfs_personflow", (String)"delete");
            LOG.debug("hpfs_personflow executeOperate delete result:{}", (Object)operationResult);
            this.showChargePersonErrorMsg(operationResult);
        }
    }

    private void showChargePersonErrorMsg(OperationResult operationResult) {
        if (!operationResult.isSuccess()) {
            StringBuilder errorMsg = new StringBuilder();
            operationResult.getAllErrorOrValidateInfo().forEach(errorInfo -> errorMsg.append(errorInfo.getMessage()));
            throw new KDBizException(errorMsg.toString());
        }
    }

    private void setParamSource(Map<String, Object> param) {
        param.put("originEntityNumber", "hpfs_chgrecord");
        param.put("operate", "hr_hpfs_rollback_tag_of_datasource");
    }

    private void showErrorMsg(ResponseResult responseResult) {
        List errorResponseDataList = Optional.ofNullable(responseResult.getErrorResponseDataList()).orElseGet(() -> new ArrayList(0));
        errorResponseDataList.forEach(errorResponseData -> {
            Map errorMap = errorResponseData.getErrorMap();
            if (errorMap != null && !errorMap.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                errorMap.forEach((entityNum, errorInfo) -> {
                    sb.append(ResManager.loadKDString((String)"\u5904\u7406\u5931\u8d25\uff1a", (String)"ChgRollbackOp_2", (String)"hr-hpfs-opplugin", (Object[])new Object[0]));
                    Map entryErrorMap = errorInfo.getEntryErrorMap();
                    if (entryErrorMap != null && !entryErrorMap.isEmpty()) {
                        errorInfo.getEntryErrorMap().values().forEach(sb::append);
                    } else {
                        sb.append(errorInfo.getErrorMsg());
                    }
                });
                throw new KDBizException(sb.toString());
            }
            if (errorResponseData.isAllFail()) {
                throw new KDBizException(errorResponseData.getAllFailMsg());
            }
        });
    }

    private void rollbackLicense(DynamicObject[] dataEntities) {
        LOG.info("rollbackLicense start,dataEntities.size:{}", (Object)dataEntities.length);
        Set employeeIds = Arrays.stream(dataEntities).map(dataEntity -> dataEntity.getLong("employee.id")).collect(Collectors.toSet());
        try {
            ILicenseCertApplicationService.getInstance().syncEmployeeLicenseCert(employeeIds);
        }
        catch (Exception e) {
            throw new KDBizException(ResManager.loadKDString((String)"\u8bb8\u53ef\u56de\u6eda\u5f02\u5e38\u3002", (String)"ChgRollbackOp_1", (String)"hr-hpfs-opplugin", (Object[])new Object[0]));
        }
        LOG.info("rollbackLicense end,dataEntities.size:{}", (Object)dataEntities.length);
    }

    private void reverseEntryItemCopy(DynamicObject beforeChgRecord, DynamicObject beforeEntryItem, DynamicObject newEntryItem, boolean isParamEntry) {
        ChgUtils.chgRecordEntryItemCopy((DynamicObject)beforeEntryItem, (DynamicObject)newEntryItem, (boolean)false, (boolean)isParamEntry);
        String newEntryItemNamePrefix = isParamEntry ? "param" : "";
        newEntryItem.set(newEntryItemNamePrefix + "chgmode", (Object)this.reverseChgMode(beforeEntryItem));
        newEntryItem.set(newEntryItemNamePrefix + "databefore_tag", (Object)beforeEntryItem.getString("dataafter_tag"));
        newEntryItem.set(newEntryItemNamePrefix + "dataafter_tag", (Object)beforeEntryItem.getString("databefore_tag"));
        newEntryItem.set(newEntryItemNamePrefix + "databefore", (Object)beforeEntryItem.getString("dataafter"));
        newEntryItem.set(newEntryItemNamePrefix + "dataafter", (Object)beforeEntryItem.getString("databefore"));
        String paramChgMode = newEntryItem.getString(newEntryItemNamePrefix + "chgmode");
        if (HRStringUtils.equals((String)paramChgMode, (String)"0")) {
            this.rebuildParamDataBefore(beforeChgRecord, newEntryItem);
        }
        String dataBefore = newEntryItem.getString(newEntryItemNamePrefix + "databefore_tag");
        String dataAfter = newEntryItem.getString(newEntryItemNamePrefix + "dataafter_tag");
        if (isParamEntry) {
            ChgUtils.setDataCompare((DynamicObject)newEntryItem, (String)dataBefore, (String)dataAfter);
        } else {
            ChgUtils.setChgRecordEntryItemDataCompare((DynamicObject)newEntryItem, (String)dataBefore, (String)dataAfter);
        }
    }

    private String reverseChgMode(DynamicObject beforeEntryItem) {
        String reverseChgMode;
        String beforeChgMode;
        switch (beforeChgMode = beforeEntryItem.getString("chgmode")) {
            case "0": {
                reverseChgMode = "2";
                break;
            }
            case "2": {
                reverseChgMode = "0";
                break;
            }
            default: {
                reverseChgMode = "1";
            }
        }
        return reverseChgMode;
    }

    private void rebuildParamDataBefore(DynamicObject beforeChgRecord, DynamicObject newEntryItem) {
        String entityNum = newEntryItem.getString("paramchgentity.number");
        if (IGNORE_ENTITIES.contains(entityNum)) {
            return;
        }
        DynamicObject beforeDy = CustomDynamicObjectJsonSerializer.parseDynamicObjectJson((DynamicObject)beforeChgRecord, (String)newEntryItem.getString("paramdataafter_tag"), (String)entityNum);
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)entityNum);
        DynamicObject newDy = (DynamicObject)dataEntityType.createInstance();
        HRDynamicObjectUtils.copy((DynamicObject)beforeDy, (DynamicObject)newDy);
        newDy.set("id", (Object)ORM.create().genLongId((IDataEntityType)dataEntityType));
        newEntryItem.set("paramdataid", (Object)newDy.getLong("id"));
        String privacyPropJson = ChgUtils.getPrivacyPropJson();
        ChgUtils.setDataAfter((DynamicObject)newEntryItem, (DynamicObject)newDy, (String)privacyPropJson);
    }

    private void rollbackOtherChgRecord(DynamicObject[] dataEntities, DynamicObjectType dataEntityType, Set<String> fields) {
        if (this.otherChgRecordMap.isEmpty()) {
            return;
        }
        for (DynamicObject chgRecord : dataEntities) {
            List<DynamicObject> otherChgRecords = this.otherChgRecordMap.get(chgRecord.getLong("id"));
            if (HRCollUtil.isEmpty(otherChgRecords)) continue;
            DynamicObject[] newChgRecords = (DynamicObject[])otherChgRecords.stream().map(beforeChgRecord -> this.buildNewRollbackChgRecord((DynamicObject)beforeChgRecord, dataEntityType, fields)).toArray(DynamicObject[]::new);
            ChgRecordRepository.save((DynamicObject[])newChgRecords);
            for (DynamicObject newChgRecord : newChgRecords) {
                Map saveBatchParam;
                List saveDataList;
                Map deleteBatchParam = ChgUtils.buildDeleteBatchParamByEntryEntity((DynamicObject[])new DynamicObject[]{newChgRecord});
                List delDataList = (List)deleteBatchParam.get("data");
                if (HRCollUtil.isNotEmpty((Collection)delDataList)) {
                    ResponseResult deleteBatchResult = IPersonGenericService.getInstance().deleteBatch(deleteBatchParam);
                    LOG.info("IPersonGenericService deleteBatch result:{}", (Object)deleteBatchResult.getSuccess());
                    this.showErrorMsg(deleteBatchResult);
                }
                if (!HRCollUtil.isNotEmpty((Collection)(saveDataList = (List)(saveBatchParam = ChgUtils.buildSaveBatchParamByEntryEntity((DynamicObject[])new DynamicObject[]{newChgRecord})).get("data")))) continue;
                ResponseResult saveBatchResult = IPersonGenericService.getInstance().saveBatch(saveBatchParam);
                LOG.info("IPersonGenericService saveBatchResult result:{}", (Object)saveBatchResult.getSuccess());
                this.showErrorMsg(saveBatchResult);
            }
        }
    }

    private DynamicObject buildNewRollbackChgRecord(DynamicObject beforeChgRecord, DynamicObjectType dataEntityType, Set<String> fields) {
        DynamicObject newChgRecord = (DynamicObject)dataEntityType.createInstance();
        for (String field : fields) {
            if (!beforeChgRecord.containsProperty(field)) continue;
            newChgRecord.set(field, beforeChgRecord.get(field));
        }
        newChgRecord.set("id", (Object)ORM.create().genLongId((IDataEntityType)dataEntityType));
        newChgRecord.set("recordrevoked", (Object)beforeChgRecord);
        newChgRecord.set("traceid", (Object)RequestContext.get().getTraceId());
        newChgRecord.getDynamicObjectCollection("attparamentry").clear();
        newChgRecord.set("createtime", null);
        newChgRecord.set("modifytime", null);
        newChgRecord.set("creator", (Object)RequestContext.get().getCurrUserId());
        newChgRecord.set("modifier", null);
        DynamicObjectCollection beforeEntry = beforeChgRecord.getDynamicObjectCollection("entryentity");
        DynamicObjectCollection newParamEntry = newChgRecord.getDynamicObjectCollection("paramentry");
        DynamicObjectCollection newEntry = newChgRecord.getDynamicObjectCollection("entryentity");
        beforeEntry.forEach(beforeEntryItem -> {
            String chgEntityNum = beforeEntryItem.getString("chgentity.number");
            if (HRStringUtils.equals((String)chgEntityNum, (String)"hpfs_personflow")) {
                DynamicObject newEntryItem = newEntry.addNew();
                this.reverseEntryItemCopy(newChgRecord, (DynamicObject)beforeEntryItem, newEntryItem, false);
            } else {
                DynamicObject newParamEntryItem = newParamEntry.addNew();
                this.reverseEntryItemCopy(newChgRecord, (DynamicObject)beforeEntryItem, newParamEntryItem, true);
            }
        });
        return newChgRecord;
    }

    private /* synthetic */ DynamicObject lambda$beginOperationTransaction$0(DynamicObjectType dataEntityType, Set fields, DynamicObject beforeChgRecord) {
        return this.buildNewRollbackChgRecord(beforeChgRecord, dataEntityType, fields);
    }
}
