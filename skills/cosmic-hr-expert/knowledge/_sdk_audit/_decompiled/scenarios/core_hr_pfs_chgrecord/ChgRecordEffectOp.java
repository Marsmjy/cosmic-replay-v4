/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.google.common.collect.Sets
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.ReturnOperationArgs
 *  kd.bos.entity.plugin.args.RollbackOperationArgs
 *  kd.bos.exception.ErrorCode
 *  kd.bos.exception.KDBizException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.coderule.CodeRuleServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.hpfs.chgrecord.serialization.CustomDynamicObjectJsonSerializer
 *  kd.hr.hbp.common.util.HRCollUtil
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRObjectUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hpfs.business.application.service.cert.ILicenseCertApplicationService
 *  kd.hr.hpfs.business.domain.perchg.ChgRecordRepository
 *  kd.hrmp.hrpi.business.application.generic.IPersonGenericService
 *  kd.hrmp.hrpi.common.generic.response.ErrorResponseData
 *  kd.hrmp.hrpi.common.generic.response.ResponseResult
 *  kd.sdk.hr.hpfs.common.enums.PerChgStatusEnum
 *  kd.sdk.hr.hpfs.utils.ChgUtils
 *  kd.sdk.hr.hpfs.utils.PerChgNewBillUtils
 *  kd.sdk.hr.hpfs.utils.PerChgStatusUtils
 *  org.apache.commons.lang3.tuple.Triple
 */
package kd.hr.hpfs.opplugin.op.basedata;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.ReturnOperationArgs;
import kd.bos.entity.plugin.args.RollbackOperationArgs;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDBizException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.hpfs.chgrecord.serialization.CustomDynamicObjectJsonSerializer;
import kd.hr.hbp.common.util.HRCollUtil;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRObjectUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hpfs.business.application.service.cert.ILicenseCertApplicationService;
import kd.hr.hpfs.business.domain.perchg.ChgRecordRepository;
import kd.hrmp.hrpi.business.application.generic.IPersonGenericService;
import kd.hrmp.hrpi.common.generic.response.ErrorResponseData;
import kd.hrmp.hrpi.common.generic.response.ResponseResult;
import kd.sdk.hr.hpfs.common.enums.PerChgStatusEnum;
import kd.sdk.hr.hpfs.utils.ChgUtils;
import kd.sdk.hr.hpfs.utils.PerChgNewBillUtils;
import kd.sdk.hr.hpfs.utils.PerChgStatusUtils;
import org.apache.commons.lang3.tuple.Triple;

public final class ChgRecordEffectOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(ChgRecordEffectOp.class);
    private static final String CHG_ENTITY_NUMBER = "chgEntityNumber";
    private static final String EFFECT_CHG_RECORD_IDS = "effectChgRecordIds";
    private static final String ROLL_BACK_CHG_RECORD_IDS = "rollBackChgRecordIds";
    private static final String EXCEPTION_CHG_RECORD_IDS = "exceptionChgRecordIds";
    private static final long CHG_CATEGORY_INFO_CHANGE = 1240L;

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        List fieldKeys = evt.getFieldKeys();
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)"hpfs_chgrecord");
        Map allFields = dataEntityType.getAllFields();
        fieldKeys.addAll(new ArrayList(allFields.keySet()));
        fieldKeys.add("paramentry.paramdataafter_tag");
        fieldKeys.add("paramentry.paramerrormsg_tag");
        fieldKeys.add("paramentry.paramdatabefore_tag");
        fieldKeys.add("paramentry.paramdatacompare_tag");
        fieldKeys.add("entryentity.databefore_tag");
        fieldKeys.add("entryentity.dataafter_tag");
        fieldKeys.add("entryentity.datacompare_tag");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities.length == 0) {
            return;
        }
        List chgRecordIds = Arrays.stream(dataEntities).map(dy -> dy.getLong("id")).collect(Collectors.toList());
        Set<Long> empIds = Arrays.stream(dataEntities).map(dy -> dy.getLong("employee_id")).collect(Collectors.toSet());
        String chgEntityNumber = dataEntities[0].getString("billsource.number");
        LOG.info("beginOperationTransaction start chgEntityNumber:{},chgRecordIds:{}", (Object)chgEntityNumber, chgRecordIds);
        Map<Long, List<DynamicObject>> chgRecordInfoByEmpForAppend = this.getChgRecordMap(empIds, Sets.newHashSet(chgRecordIds));
        ArrayList<DynamicObject> executeEffectChgRecords = new ArrayList<DynamicObject>(dataEntities.length);
        HashSet<Long> executeEffectChgRecordIds = new HashSet<Long>(dataEntities.length);
        Set<Long> futureEffectChgRecordIds = Arrays.stream(dataEntities).filter(chgRecord -> !HRStringUtils.equals((String)"1", (String)chgRecord.getString("datastatus"))).filter(chgRecord -> chgRecord.getDate("effecttime") != null && HRDateTimeUtils.isAfterNow((Date)chgRecord.getDate("effecttime"))).map(chgRecord -> chgRecord.getLong("id")).collect(Collectors.toSet());
        Map<Object, Object> fulureEffectChgRecordBillMap = new HashMap(dataEntities.length);
        Map futureEffectConfigMap = new HashMap(dataEntities.length);
        if (!CollectionUtils.isEmpty(futureEffectChgRecordIds)) {
            fulureEffectChgRecordBillMap = this.getAffRecordBillMap(futureEffectChgRecordIds, chgEntityNumber);
            futureEffectConfigMap = PerChgNewBillUtils.getFutureEffectConfigMap((DynamicObject[])fulureEffectChgRecordBillMap.values().toArray(new DynamicObject[0]));
        }
        for (DynamicObject chgRecord2 : dataEntities) {
            if (HRStringUtils.equals((String)"1", (String)chgRecord2.getString("datastatus"))) continue;
            chgRecord2.set("traceid", (Object)RequestContext.get().getTraceId());
            chgRecord2.set("eventseq", (Object)CodeRuleServiceHelper.getNumber((String)"hpfs_chgrecord", (DynamicObject)chgRecord2, (String)String.valueOf(RequestContext.get().getOrgId())));
            if (chgRecord2.getDate("effecttime") == null || HRDateTimeUtils.isBeforeOrEqualNow((Date)chgRecord2.getDate("effecttime")) || ((Boolean)futureEffectConfigMap.get(((DynamicObject)fulureEffectChgRecordBillMap.get(chgRecord2.getLong("id"))).getLong("org.id"))).booleanValue()) {
                chgRecord2.set("datastatus", (Object)"0");
                chgRecord2.getDynamicObjectCollection("paramentry").forEach(entryItem -> {
                    entryItem.set("paramerrormsg", null);
                    entryItem.set("paramerrormsg_tag", null);
                });
                executeEffectChgRecords.add(chgRecord2);
                executeEffectChgRecordIds.add(chgRecord2.getLong("id"));
                chgRecord2.set("errormsg", null);
                continue;
            }
            chgRecord2.set("datastatus", (Object)"2");
        }
        if (executeEffectChgRecords.isEmpty()) {
            LOG.info("Got empty executeEffectChgRecords. chgRecordIds\uff1a{}", chgRecordIds);
            return;
        }
        LOG.info("chgEntityNumber:{},executeEffectChgRecordIds:{}", (Object)chgEntityNumber, executeEffectChgRecordIds);
        Map<Long, DynamicObject> affRecordBillMap = this.getAffRecordBillMap(executeEffectChgRecordIds, chgEntityNumber);
        OperateOption operateOption = this.getOption();
        operateOption.setVariableValue(EFFECT_CHG_RECORD_IDS, JSONArray.toJSONString(executeEffectChgRecordIds));
        try {
            DynamicObject[] chgRecords = executeEffectChgRecords.toArray(new DynamicObject[0]);
            Map deleteBatchParam = ChgUtils.buildDeleteBatchParam((DynamicObject[])chgRecords);
            Object deleteBatchParamData = deleteBatchParam.get("data");
            ResponseResult deleteBatchResult = null;
            if (deleteBatchParamData != null) {
                deleteBatchResult = IPersonGenericService.getInstance().deleteBatch(deleteBatchParam);
                String deleteBatchResultStr = SerializationUtils.toJsonString((Object)deleteBatchResult);
                LOG.info("Got delete batch result map: {}.", (Object)deleteBatchResultStr);
            }
            Map saveBatchParam = ChgUtils.buildSaveBatchParam((DynamicObject[])chgRecords, (boolean)false);
            ResponseResult saveBatchResult = IPersonGenericService.getInstance().saveBatch(saveBatchParam);
            String saveBatchResultStr = SerializationUtils.toJsonString((Object)saveBatchResult);
            LOG.info("Got save batch result map: {}.", (Object)saveBatchResultStr);
            HashSet<Long> errorChgRecordIds = new HashSet<Long>(chgRecords.length);
            errorChgRecordIds.addAll(this.processError(chgRecords, saveBatchResult));
            if (deleteBatchResult != null) {
                errorChgRecordIds.addAll(this.processError(chgRecords, deleteBatchResult));
            }
            String message = ResManager.loadKDString((String)"\u5904\u7406\u4eba\u5458\u4fe1\u606f\u5931\u8d25", (String)"ChgRecordEffectOp_3", (String)"hr-hpfs-opplugin", (Object[])new Object[0]);
            if (!errorChgRecordIds.isEmpty()) {
                throw new KDBizException(message);
            }
            message = ResManager.loadKDString((String)"\u540c\u6b65\u4eba\u5458\u6d41\u5165\u6d41\u51fa\u5931\u8d25", (String)"ChgRecordEffectOp_0", (String)"hr-hpfs-opplugin", (Object[])new Object[0]);
            try {
                errorChgRecordIds.addAll(this.processPersonFlow(errorChgRecordIds, chgRecords));
            }
            catch (Exception e) {
                throw new KDBizException((Throwable)e, new ErrorCode(((Object)((Object)this)).getClass().getName(), message), new Object[0]);
            }
            if (!errorChgRecordIds.isEmpty()) {
                throw new KDBizException(message);
            }
            message = ResManager.loadKDString((String)"\u540c\u6b65\u90e8\u95e8\u8d1f\u8d23\u4eba\u5931\u8d25", (String)"ChgRecordEffectOp_1", (String)"hr-hpfs-opplugin", (Object[])new Object[0]);
            try {
                errorChgRecordIds.addAll(this.processChargePerson(errorChgRecordIds, chgRecords));
            }
            catch (Exception e) {
                throw new KDBizException((Throwable)e, new ErrorCode(((Object)((Object)this)).getClass().getName(), message), new Object[0]);
            }
            if (!errorChgRecordIds.isEmpty()) {
                throw new KDBizException(message);
            }
            this.syncEmployeeLicenseCert(chgRecords, errorChgRecordIds);
            Arrays.stream(chgRecords).forEach(chgRecord -> {
                long chgRecordId = chgRecord.getLong("id");
                DynamicObject bill = (DynamicObject)affRecordBillMap.get(chgRecordId);
                if (errorChgRecordIds.contains(chgRecordId)) {
                    PerChgStatusUtils.setStatusAndMsgByEnum((DynamicObject)bill, (PerChgStatusEnum)PerChgStatusEnum.SMALLEST_FAIL, (boolean)false);
                } else {
                    chgRecord.set("datastatus", (Object)"1");
                    PerChgStatusUtils.setStatusAndMsgByEnum((DynamicObject)bill, (PerChgStatusEnum)PerChgStatusEnum.SMALLEST_SUCCESS, (boolean)true);
                }
                List dys = (List)chgRecordInfoByEmpForAppend.get(chgRecord.getLong("employee_id"));
                if (HRCollUtil.isNotEmpty((Collection)dys)) {
                    boolean isAppend = dys.stream().anyMatch(item -> item.getDate("effecttime") != null && chgRecord.getDate("effecttime") != null && item.getDate("effecttime").after(chgRecord.getDate("effecttime")));
                    chgRecord.set("isappend", (Object)isAppend);
                } else {
                    chgRecord.set("isappend", (Object)false);
                }
            });
            if (!TXHandle.get().isRollback()) {
                LOG.info("Update bill.chgEntityNumber\uff1a{}", (Object)chgEntityNumber);
                new HRBaseServiceHelper(chgEntityNumber).update(affRecordBillMap.values().toArray(new DynamicObject[0]));
            }
        }
        catch (Exception e) {
            LOG.error("Got exception when execute chgRecord effect operation.", (Throwable)e);
            TXHandle.get().markRollback();
            String message = HRStringUtils.isEmpty((String)e.getMessage()) ? NullPointerException.class.getName() : e.getMessage();
            executeEffectChgRecords.forEach(chgRecord -> chgRecord.set("errormsg", (Object)StringUtils.left((String)message, (int)1000)));
        }
        if (TXHandle.get().isRollback()) {
            executeEffectChgRecords.forEach(chgRecord -> chgRecord.set("datastatus", (Object)"0"));
            operateOption.setVariableValue(ROLL_BACK_CHG_RECORD_IDS, JSONArray.toJSONString(executeEffectChgRecordIds));
            LOG.warn("The transaction is rolled back.");
        }
        LOG.info("beginOperationTransaction end chgEntityNumber:{},executeEffectChgRecordIds:{}.", (Object)chgEntityNumber, executeEffectChgRecordIds);
    }

    public void rollbackOperation(RollbackOperationArgs args) {
        super.rollbackOperation(args);
        DynamicObject[] dataEntities = args.getDataEntitys();
        if (dataEntities.length == 0) {
            return;
        }
        OperateOption operateOption = this.getOption();
        Map variables = operateOption.getVariables();
        String executeEffectChgRecordIdsStr = (String)variables.get(EFFECT_CHG_RECORD_IDS);
        if (HRStringUtils.isEmpty((String)executeEffectChgRecordIdsStr)) {
            LOG.warn("No executeEffectChgRecordIdsStr found in variables. dataEntities.length\uff1a{}", (Object)dataEntities.length);
            return;
        }
        String chgEntityNumber = dataEntities[0].getString("billsource.number");
        LOG.info("rollbackOperation chgEntityNumber:{},executeEffectChgRecordIds:{}", (Object)chgEntityNumber, (Object)executeEffectChgRecordIdsStr);
        operateOption.setVariableValue(EXCEPTION_CHG_RECORD_IDS, executeEffectChgRecordIdsStr);
        operateOption.setVariableValue(CHG_ENTITY_NUMBER, chgEntityNumber);
    }

    public void onReturnOperation(ReturnOperationArgs args) {
        super.onReturnOperation(args);
        Map variables = this.getOption().getVariables();
        String exceptionChgRecordIdsStr = (String)variables.get(EXCEPTION_CHG_RECORD_IDS);
        if (HRStringUtils.isEmpty((String)exceptionChgRecordIdsStr)) {
            LOG.info("No exceptionChgRecordIdsStr found in variables.");
            return;
        }
        String chgEntityNumber = (String)variables.get(CHG_ENTITY_NUMBER);
        List exceptionChgRecordIds = JSONArray.parseArray((String)exceptionChgRecordIdsStr, Long.class);
        LOG.info("onReturnOperation chgEntityNumber:{},exceptionChgRecordIds\uff1a{}", (Object)chgEntityNumber, (Object)exceptionChgRecordIds);
        TXHandle txHandle = TXHandle.get();
        if (txHandle != null) {
            LOG.warn("in the transaction. chgEntityNumber:{}", (Object)chgEntityNumber);
            txHandle.close();
        }
        DynamicObject[] exceptionChgRecords = ChgRecordRepository.load((String)String.join((CharSequence)",", "traceid", "errormsg"), (QFilter[])new QFilter[]{new QFilter("id", "in", (Object)exceptionChgRecordIds)});
        String message = "An exception occurred during the execution process, and the transaction was rolled back.";
        String traceId = RequestContext.get().getTraceId();
        Arrays.stream(exceptionChgRecords).forEach(chgRecord -> {
            chgRecord.set("traceid", (Object)traceId);
            chgRecord.set("errormsg", (Object)message);
        });
        Map<Long, DynamicObject> affRecordBillMap = this.getAffRecordBillMap(new HashSet<Long>(exceptionChgRecordIds), chgEntityNumber);
        this.rollbackUpdateDataHandle(chgEntityNumber, exceptionChgRecords, affRecordBillMap);
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        DynamicObject[] dataEntities = args.getDataEntities();
        if (dataEntities.length == 0) {
            return;
        }
        Map variables = this.getOption().getVariables();
        String rollBackChgRecordIdsStr = (String)variables.get(ROLL_BACK_CHG_RECORD_IDS);
        if (HRStringUtils.isEmpty((String)rollBackChgRecordIdsStr)) {
            LOG.info("No rollBackChgRecordIdsStr found in variables.");
            return;
        }
        String chgEntityNumber = dataEntities[0].getString("billsource.number");
        List errorChgRecordIds = JSONArray.parseArray((String)rollBackChgRecordIdsStr, Long.class);
        LOG.info("onReturnOperation chgEntityNumber:{},errorChgRecordIds\uff1a{}", (Object)chgEntityNumber, (Object)errorChgRecordIds);
        Map<Long, DynamicObject> affRecordBillMap = this.getAffRecordBillMap(new HashSet<Long>(errorChgRecordIds), chgEntityNumber);
        DynamicObject[] errorChgRecords = Arrays.stream(dataEntities).filter(dataEntity -> errorChgRecordIds.contains(dataEntity.getLong("id"))).collect(Collectors.toList()).toArray(new DynamicObject[0]);
        this.rollbackUpdateDataHandle(chgEntityNumber, errorChgRecords, affRecordBillMap);
    }

    private void rollbackUpdateDataHandle(String chgEntityNumber, DynamicObject[] errorChgRecords, Map<Long, DynamicObject> affRecordBillMap) {
        DynamicObject[] errorBills;
        if (errorChgRecords.length != 0) {
            LOG.info("save error change records,chgEntityNumber:{}", (Object)chgEntityNumber);
            ChgRecordRepository.save((DynamicObject[])errorChgRecords);
        }
        if (!affRecordBillMap.isEmpty()) {
            LOG.warn("affRecordBillMap is empty,chgEntityNumber:{}", (Object)chgEntityNumber);
        }
        if ((errorBills = affRecordBillMap.values().stream().filter(bill -> !HRObjectUtils.isEmpty((Object)bill)).peek(bill -> PerChgStatusUtils.setStatusAndMsgByEnum((DynamicObject)bill, (PerChgStatusEnum)PerChgStatusEnum.SMALLEST_FAIL, (boolean)false)).collect(Collectors.toList()).toArray(new DynamicObject[0])).length == 0) {
            LOG.info("errorBills is empty.,chgEntityNumber:{}", (Object)chgEntityNumber);
            return;
        }
        List billIds = Arrays.stream(errorBills).map(bill -> bill.getLong("id")).collect(Collectors.toList());
        LOG.info("update bill status chgEntityNumber:{},billIds:{}", (Object)chgEntityNumber, billIds);
        new HRBaseServiceHelper(chgEntityNumber).update(errorBills);
        ChgUtils.executeOperate((Object[])billIds.toArray(), (String)chgEntityNumber, (String)"aftereffect");
    }

    private Set<Long> processPersonFlow(Set<Long> errorChgRecordIds, DynamicObject[] chgRecords) {
        HashSet<Long> currentErrorChgRecordIds = new HashSet<Long>();
        List chgRecordList = Arrays.stream(chgRecords).filter(chgRecord -> !errorChgRecordIds.contains(chgRecord.getLong("id"))).filter(chgRecord -> HRStringUtils.equals((String)chgRecord.getString("datastatus"), (String)"0") || HRStringUtils.equals((String)chgRecord.getString("datastatus"), (String)"2")).peek(chgRecord -> currentErrorChgRecordIds.add(chgRecord.getLong("id"))).collect(Collectors.toList());
        if (HRCollUtil.isEmpty(chgRecordList)) {
            return currentErrorChgRecordIds;
        }
        OperationResult personFlowOperationResult = ChgUtils.savePersonFlow(chgRecordList, (String)"1");
        LOG.info("Got personFlowOperationResult: {}.", (Object)personFlowOperationResult.isSuccess());
        List successPkIds = personFlowOperationResult.getSuccessPkIds();
        HashMap<Long, Long> dataIdMap = new HashMap<Long, Long>();
        for (DynamicObject chgRecord2 : chgRecordList) {
            DynamicObjectCollection entryItems = chgRecord2.getDynamicObjectCollection("entryentity");
            for (DynamicObject entryItem : entryItems) {
                String chgEntityNumber = entryItem.getString("chgentity.number");
                if (!HRStringUtils.equals((String)chgEntityNumber, (String)"hpfs_personflow")) continue;
                dataIdMap.put(entryItem.getLong("dataid"), chgRecord2.getLong("id"));
                if (!successPkIds.contains(entryItem.get("dataid"))) continue;
                String dataAfterTag = entryItem.getString("dataafter_tag");
                DynamicObject personFlowDy = CustomDynamicObjectJsonSerializer.parseDynamicObjectJson((DynamicObject)chgRecord2, (String)dataAfterTag, (String)chgEntityNumber);
                personFlowDy.set("datastatus", (Object)"1");
                String dataAfter = CustomDynamicObjectJsonSerializer.convertDynamicObjectToJson((DynamicObject)personFlowDy);
                entryItem.set("dataafter_tag", (Object)dataAfter);
                entryItem.set("dataafter", (Object)StringUtils.left((String)dataAfter, (int)200));
            }
        }
        if (!personFlowOperationResult.isSuccess()) {
            successPkIds.forEach(it -> {
                Long id = (Long)it;
                currentErrorChgRecordIds.remove(dataIdMap.get(id));
            });
            Map<Long, DynamicObject> chgRecordMap = Arrays.stream(chgRecords).collect(Collectors.toMap(it -> it.getLong("id"), it -> it, (oldValue, newValue) -> newValue));
            ArrayList<Triple<Long, Long, String>> batchError = new ArrayList<Triple<Long, Long, String>>();
            personFlowOperationResult.getAllErrorOrValidateInfo().forEach(operateInfo -> {
                Long pkValue = (Long)operateInfo.getPkValue();
                batchError.add(Triple.of(dataIdMap.get(pkValue), (Object)pkValue, (Object)operateInfo.getMessage()));
            });
            this.doProcessError(batchError, chgRecordMap);
            return currentErrorChgRecordIds;
        }
        return new HashSet<Long>();
    }

    private Set<Long> processChargePerson(Set<Long> errorChgRecordIds, DynamicObject[] chgRecords) {
        HashSet<Long> currentErrorChgRecordIds = new HashSet<Long>(16);
        ArrayList<DynamicObject> chargePersons = new ArrayList<DynamicObject>(10);
        HashMap chgRecordChargePersonParamItemMap = new HashMap(16);
        HashMap<Long, Long> chargePersonChgRecordIdMap = new HashMap<Long, Long>(16);
        HashMap<Long, DynamicObject> chgRecordMap = new HashMap<Long, DynamicObject>(16);
        for (DynamicObject chgRecord : chgRecords) {
            long chgRecordId2 = chgRecord.getLong("id");
            chgRecordMap.put(chgRecordId2, chgRecord);
            if (errorChgRecordIds.contains(chgRecordId2)) continue;
            HashMap<Long, DynamicObject> chargePersonParamItemMap = new HashMap<Long, DynamicObject>(16);
            DynamicObjectCollection paramEntry = chgRecord.getDynamicObjectCollection("paramentry");
            for (DynamicObject paramEntryItem : paramEntry) {
                if (!HRStringUtils.equals((String)"haos_chargeperson", (String)paramEntryItem.getString("paramchgentity.number"))) continue;
                DynamicObject chargePerson = CustomDynamicObjectJsonSerializer.parseDynamicObjectJson((DynamicObject)chgRecord, (String)paramEntryItem.getString("paramdataafter_tag"), (String)"haos_chargeperson");
                chargePersons.add(chargePerson);
                long dataId = paramEntryItem.getLong("paramdataid");
                chargePersonParamItemMap.put(dataId, paramEntryItem);
                chargePersonChgRecordIdMap.put(dataId, chgRecordId2);
            }
            chgRecordChargePersonParamItemMap.put(chgRecordId2, chargePersonParamItemMap);
        }
        if (chargePersons.isEmpty()) {
            LOG.info("chargePersons is empty");
            return currentErrorChgRecordIds;
        }
        OperationResult chargePersonOperationResult = ChgUtils.executeOperate((DynamicObject[])chargePersons.toArray(new DynamicObject[0]), (String)"save");
        LOG.info("chargePerson execute save result:{}", (Object)chargePersonOperationResult.isSuccess());
        if (chargePersonOperationResult.isSuccess()) {
            Map customDataMap = chargePersonOperationResult.getCustomData();
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
                    entryItem.set("dataafter", (Object)StringUtils.left((String)entryItem.getString("dataafter_tag"), (int)200));
                    ChgUtils.setChgRecordEntryItemDataCompare((DynamicObject)entryItem, (String)entryItem.getString("databefore_tag"), (String)chargePersonJson);
                }
            });
        } else {
            List allErrorOrValidateInfo = chargePersonOperationResult.getAllErrorOrValidateInfo();
            HashMap errorInfoMap = new HashMap(allErrorOrValidateInfo.size());
            allErrorOrValidateInfo.forEach(errorInfo -> {
                Long pkId = (Long)errorInfo.getPkValue();
                errorInfoMap.put(pkId, String.join((CharSequence)";", errorInfoMap.getOrDefault(pkId, ""), errorInfo.getMessage()));
            });
            String message = chargePersonOperationResult.getMessage();
            List successPkIds = chargePersonOperationResult.getSuccessPkIds();
            chargePersonChgRecordIdMap.forEach((chargePersonId, chgRecordId) -> {
                if (successPkIds.contains(chargePersonId)) {
                    return;
                }
                currentErrorChgRecordIds.add((Long)chgRecordId);
                String errorInfo = errorInfoMap.getOrDefault(chargePersonId, message);
                Map chargePersonParamItemMap = (Map)chgRecordChargePersonParamItemMap.get(chgRecordId);
                DynamicObject chargePersonItem = (DynamicObject)chargePersonParamItemMap.get(chargePersonId);
                chargePersonItem.set("paramerrormsg_tag", (Object)errorInfo);
                chargePersonItem.set("paramerrormsg", (Object)StringUtils.left((String)errorInfo, (int)200));
            });
        }
        return currentErrorChgRecordIds;
    }

    private Set<Long> processError(DynamicObject[] chgRecords, ResponseResult batchResult) {
        HashSet<Long> errorChgRecordIds = new HashSet<Long>();
        ArrayList<Triple<Long, Long, String>> batchError = new ArrayList<Triple<Long, Long, String>>();
        for (ErrorResponseData errorInfoItem : Optional.ofNullable(batchResult.getErrorResponseDataList()).orElseGet(() -> new ArrayList(0))) {
            long chgRecordId = errorInfoItem.getChgRecordID();
            Map changeRespData = errorInfoItem.getErrorMap();
            if (null != changeRespData && !changeRespData.isEmpty()) {
                changeRespData.forEach((changeRespDataKey, changeRespDataValue) -> Optional.ofNullable(changeRespDataValue.getEntryErrorMap()).orElseGet(() -> {
                    LOG.debug("ChgRecord[id:{}] changeRespData: {} does not contain detail error message.", (Object)chgRecordId, (Object)changeRespData);
                    HashMap<Long, String> emptyErrorMsgMap = new HashMap<Long, String>();
                    emptyErrorMsgMap.put(0L, changeRespDataKey + "#" + changeRespDataValue.getErrorMsg());
                    return emptyErrorMsgMap;
                }).forEach((dataId, errorMsg) -> batchError.add(Triple.of((Object)chgRecordId, (Object)dataId, (Object)errorMsg))));
                errorChgRecordIds.add(chgRecordId);
            }
            if (!errorInfoItem.isAllFail()) continue;
            batchError.add((Triple<Long, Long, String>)Triple.of((Object)chgRecordId, (Object)0L, (Object)errorInfoItem.getAllFailMsg()));
            errorChgRecordIds.add(chgRecordId);
        }
        LOG.info("Got error chg record ids: {}.", errorChgRecordIds);
        Map<Long, DynamicObject> chgRecordMap = Arrays.stream(chgRecords).collect(Collectors.toMap(it -> it.getLong("id"), it -> it, (oldValue, newValue) -> newValue));
        this.doProcessError(batchError, chgRecordMap);
        return errorChgRecordIds;
    }

    private void doProcessError(List<Triple<Long, Long, String>> errors, Map<Long, DynamicObject> chgRecordMap) {
        errors.forEach(it -> {
            long dataId = (Long)it.getMiddle();
            String errorMsg = (String)it.getRight();
            DynamicObject chgRecordItem = (DynamicObject)chgRecordMap.get(it.getLeft());
            chgRecordItem.getDynamicObjectCollection("paramentry").stream().filter(entryItem -> dataId == entryItem.getLong("paramdataid")).findAny().ifPresent(entryItem -> {
                entryItem.set("paramerrormsg", (Object)StringUtils.left((String)Optional.ofNullable(errorMsg).orElse("-"), (int)200));
                entryItem.set("paramerrormsg_tag", (Object)errorMsg);
            });
            if (0L == dataId) {
                chgRecordItem.getDynamicObjectCollection("paramentry").forEach(entryItem -> {
                    entryItem.set("paramerrormsg", (Object)StringUtils.left((String)Optional.ofNullable(errorMsg).orElse("-"), (int)200));
                    entryItem.set("paramerrormsg_tag", (Object)errorMsg);
                });
            }
        });
    }

    private Map<Long, DynamicObject> getAffRecordBillMap(Set<Long> chgRecordIds, String entityNumber) {
        LOG.info("chgRecordIds:{},entityNumber:{}", chgRecordIds, (Object)entityNumber);
        if (CollectionUtils.isEmpty(chgRecordIds)) {
            return Collections.emptyMap();
        }
        if (HRStringUtils.isEmpty((String)entityNumber)) {
            return Collections.emptyMap();
        }
        QFilter affRecordFilter = new QFilter("affrecord", "in", chgRecordIds);
        DynamicObject[] billDys = new HRBaseServiceHelper(entityNumber).load(String.join((CharSequence)",", "perchgstatus", "affrecord", "errmsg_tag", "billstatus", "b_effectivedate", "org.id"), new QFilter[]{affRecordFilter});
        return Arrays.stream(billDys).collect(Collectors.toMap(bill -> bill.getLong("affrecord"), bill -> bill));
    }

    private void syncEmployeeLicenseCert(DynamicObject[] chgRecords, Set<Long> errorChgRecordIds) {
        try {
            Set employeeIds = Arrays.stream(chgRecords).filter(chgRecord -> !errorChgRecordIds.contains(chgRecord.getLong("id"))).map(it -> it.getLong("employee_id")).collect(Collectors.toSet());
            LOG.info("Got latest employeeIds: {}.", employeeIds);
            ILicenseCertApplicationService.getInstance().syncEmployeeLicenseCert(employeeIds);
        }
        catch (Exception e) {
            String message = ResManager.loadKDString((String)"\u540c\u6b65\u8bb8\u53ef\u5931\u8d25", (String)"ChgRecordEffectOp_2", (String)"hr-hpfs-opplugin", (Object[])new Object[0]);
            throw new KDBizException((Throwable)e, new ErrorCode(((Object)((Object)this)).getClass().getName(), message), new Object[0]);
        }
    }

    private Map<Long, List<DynamicObject>> getChgRecordMap(Set<Long> empIds, Set<Long> chgRecordIds) {
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hpfs_chgrecord");
        DynamicObject[] chgRecordDys = serviceHelper.queryOriginalArray("id,employee,effecttime", new QFilter[]{new QFilter("employee.id", "in", empIds), new QFilter("id", "not in", chgRecordIds), new QFilter("chgcategory", "!=", (Object)1240L), new QFilter("datastatus", "=", (Object)"1"), new QFilter("billsource", "!=", (Object)"hrpi_assignmentmag")});
        Map<Long, List<DynamicObject>> groupByEmpId = Arrays.stream(chgRecordDys).collect(Collectors.groupingBy(item -> item.getLong("employee")));
        LOG.debug("isAppend###groupByEmpId-size\uff1a{},keySet\uff1a{}", (Object)groupByEmpId.size(), groupByEmpId.keySet());
        return groupByEmpId;
    }
}
