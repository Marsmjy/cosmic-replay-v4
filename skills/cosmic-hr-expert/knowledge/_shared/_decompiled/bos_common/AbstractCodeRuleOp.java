/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.business.plugin.AbstractUniqueBillno
 *  kd.bos.business.plugin.CodeRuleGroup
 *  kd.bos.business.plugin.CodeRuleNumberValidator
 *  kd.bos.business.plugin.CodeRuleOpStatusRecord
 *  kd.bos.business.plugin.CodeRuleOpStatusRecord$BillNoStatus
 *  kd.bos.business.plugin.CodeRuleOpStatusRecord$DataStatus
 *  kd.bos.business.plugin.CodeRuleOpStatusRecord$OperateProcess
 *  kd.bos.business.plugin.CodeRuleOpStatusRecord$OperateSourceStatus
 *  kd.bos.business.plugin.CodeRuleOpStatusRecord$RecycleStatus
 *  kd.bos.business.plugin.CodeRuleOpStatusRecord$ResultStatus
 *  kd.bos.business.plugin.CodeRuleValidator
 *  kd.bos.business.plugin.UniqueBillno
 *  kd.bos.business.plugin.mode.CodeRuleModeFactory
 *  kd.bos.business.plugin.mode.CodeRuleNumberMode
 *  kd.bos.coderule.api.CodeRuleInfo
 *  kd.bos.coderule.api.ConditionEntryInfo
 *  kd.bos.coderule.opplugin.pagecache.BillNoChangeCache
 *  kd.bos.coderule.opplugin.pagecache.PushNumberCache
 *  kd.bos.coderule.opplugin.pagecache.RecycleCache
 *  kd.bos.coderule.opplugin.util.OrgUtil
 *  kd.bos.coderule.service.CodeRuleServiceImp
 *  kd.bos.coderule.util.CodeRuleInfoUtil
 *  kd.bos.coderule.util.CodeRuleSystemParam
 *  kd.bos.coderule.util.GroupHandlerUtil
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.RefObject
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.metadata.IMetadata
 *  kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.dataentity.utils.StringUtils
 *  kd.bos.entity.BillEntityType
 *  kd.bos.entity.operate.IOperationResult
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.ReturnOperationArgs
 *  kd.bos.exception.KDBizException
 *  kd.bos.exception.KDException
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.param.ParameterReader
 *  kd.bos.servicehelper.coderule.CodeRuleServiceHelper
 *  org.apache.commons.collections4.CollectionUtils
 *  org.apache.commons.collections4.MapUtils
 */
package kd.bos.business.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kd.bos.business.plugin.AbstractUniqueBillno;
import kd.bos.business.plugin.CodeRuleGroup;
import kd.bos.business.plugin.CodeRuleNumberValidator;
import kd.bos.business.plugin.CodeRuleOpStatusRecord;
import kd.bos.business.plugin.CodeRuleValidator;
import kd.bos.business.plugin.UniqueBillno;
import kd.bos.business.plugin.mode.CodeRuleModeFactory;
import kd.bos.business.plugin.mode.CodeRuleNumberMode;
import kd.bos.coderule.api.CodeRuleInfo;
import kd.bos.coderule.api.ConditionEntryInfo;
import kd.bos.coderule.opplugin.pagecache.BillNoChangeCache;
import kd.bos.coderule.opplugin.pagecache.PushNumberCache;
import kd.bos.coderule.opplugin.pagecache.RecycleCache;
import kd.bos.coderule.opplugin.util.OrgUtil;
import kd.bos.coderule.service.CodeRuleServiceImp;
import kd.bos.coderule.util.CodeRuleInfoUtil;
import kd.bos.coderule.util.CodeRuleSystemParam;
import kd.bos.coderule.util.GroupHandlerUtil;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.RefObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.IMetadata;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.BillEntityType;
import kd.bos.entity.operate.IOperationResult;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.ReturnOperationArgs;
import kd.bos.exception.KDBizException;
import kd.bos.exception.KDException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.param.ParameterReader;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

public class AbstractCodeRuleOp
extends AbstractOperationServicePlugIn {
    private static final Log logger = LogFactory.getLog(AbstractCodeRuleOp.class);
    public static final int loopNum = 50;
    public static final String SKIPBILLNOVALIDATOR = "skipbillnovalidator";
    private static final String SKIPBILLNOVALIDATOR_VALUE = "skipbillnovalidator_value";
    private static final String BOTP_WITH_NULL_VALUE_BILLNO = "botptag_of_datasource_with_null_fields";
    protected CodeRuleServiceImp codeRuleService;
    protected AbstractUniqueBillno uniqueBillno;
    private BillNoChangeCache billNoChangeCache;
    private RecycleCache recycleCache;
    private PushNumberCache pushNumberCache;
    protected CodeRuleOpStatusRecord statusRecord;
    protected List<CodeRuleNumberMode> numberModes;
    protected CodeRuleNumberValidator numberValidator = new CodeRuleNumberValidator();

    private String getSkipbillnovalidator() {
        return SKIPBILLNOVALIDATOR + this.getClassName();
    }

    private String getSkipbillnovalidatorValue() {
        return SKIPBILLNOVALIDATOR_VALUE + this.getClassName();
    }

    protected String getClassName() {
        return "AbstractCodeRule";
    }

    public AbstractCodeRuleOp() {
        this.uniqueBillno = new UniqueBillno();
        this.codeRuleService = new CodeRuleServiceImp();
        this.billNoChangeCache = new BillNoChangeCache(this.getClassName());
        this.recycleCache = new RecycleCache(this.getClassName());
        this.statusRecord = new CodeRuleOpStatusRecord();
        this.pushNumberCache = new PushNumberCache(this.getClassName());
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        this.rebackBillno(e);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        this.generateNumber(e.getDataEntities());
    }

    public void onReturnOperation(ReturnOperationArgs e) {
        this.recycleOldNumberWhenSuccess(e.getOperationResult());
        this.updateMaximumWhenSuccess(e.getOperationResult());
        logger.info("[CodeRuleOp]\u751f\u6210\u7f16\u53f7\u6d41\u7a0b\u8bb0\u5f55\uff1a" + this.statusRecord.toString());
    }

    private void updateMaximumWhenSuccess(IOperationResult operationResult) {
        List successPkIds = operationResult.getSuccessPkIds();
        Map updateCodeRuleGroup = this.statusRecord.getUpdateCodeRuleGroup();
        Map useInterruptionGroup = this.statusRecord.getUseInterruptionGroup();
        Map updateMaxNumberGroup = this.statusRecord.getUpdateMaxNumberGroup();
        if (CollectionUtils.isEmpty((Collection)successPkIds) || MapUtils.isEmpty((Map)updateCodeRuleGroup) && MapUtils.isEmpty((Map)useInterruptionGroup) && MapUtils.isEmpty((Map)updateMaxNumberGroup)) {
            return;
        }
        HashSet<Object> successPkIdsSet = new HashSet<Object>(successPkIds);
        if (MapUtils.isNotEmpty((Map)updateCodeRuleGroup)) {
            List<CodeRuleGroup> codeRuleGroupList = this.getSuccessGroup(updateCodeRuleGroup, successPkIdsSet);
            this.codeRuleService.consumptionIntermitnos(codeRuleGroupList);
            this.codeRuleService.updateMaximum(codeRuleGroupList);
        }
        if (MapUtils.isNotEmpty((Map)useInterruptionGroup)) {
            List<CodeRuleGroup> useInterruptionGroupList = this.getSuccessGroup(useInterruptionGroup, successPkIdsSet);
            this.codeRuleService.consumptionIntermitnos(useInterruptionGroupList);
        }
        if (MapUtils.isNotEmpty((Map)updateMaxNumberGroup)) {
            List<CodeRuleGroup> updateMaxNumberGroupList = this.getSuccessGroup(updateMaxNumberGroup, successPkIdsSet);
            this.codeRuleService.updateMaximum(updateMaxNumberGroupList);
        }
    }

    private List<CodeRuleGroup> getSuccessGroup(Map<DynamicObject, CodeRuleGroup> updateCodeRuleGroup, Set<Object> successPkIdsSet) {
        ArrayList<CodeRuleGroup> codeRuleGroupList = new ArrayList<CodeRuleGroup>();
        for (Map.Entry<DynamicObject, CodeRuleGroup> groupEntry : updateCodeRuleGroup.entrySet()) {
            DynamicObject dynamicObject = groupEntry.getKey();
            if (!successPkIdsSet.contains(dynamicObject.getPkValue())) continue;
            codeRuleGroupList.add(groupEntry.getValue());
        }
        return codeRuleGroupList;
    }

    public void generateNumber(DynamicObject[] dynamicObjects) {
        if (!this.validateEntityType()) {
            return;
        }
        if (!this.validateOperateKey()) {
            return;
        }
        if (dynamicObjects != null && dynamicObjects.length > 0) {
            String datasource;
            this.statusRecord.initDataOperateProcess(dynamicObjects);
            this.numberModes = CodeRuleModeFactory.newInstance((OperateOption)this.operateOption, (String)this.getClassName(), (String)this.getBillNoField((BillEntityType)this.billEntityType));
            if (this.operateOption.tryGetVariableValue("webapitag_of_datasource", new RefObject())) {
                datasource = this.operateOption.getVariableValue("webapitag_of_datasource");
                if (String.valueOf(true).equals(datasource)) {
                    logger.info("[CodeRuleOp]<\u5f53\u524d\u4e3aOpenApi>\u6279\u91cf\u5904\u7406\u591a\u6761\u6570\u636e(" + dynamicObjects.length + ")");
                    this.statusRecord.tagOperateSourceStatus(CodeRuleOpStatusRecord.OperateSourceStatus.WEBAPI_BATCH_GENERATE);
                    this.batchGenerateNumberFromWebapi(dynamicObjects);
                    return;
                }
            }
            if (this.operateOption.tryGetVariableValue("importtag_of_datasource", new RefObject())) {
                datasource = this.operateOption.getVariableValue("importtag_of_datasource");
                if (String.valueOf(true).equals(datasource)) {
                    logger.info("[CodeRuleOp]<\u5f53\u524d\u4e3a\u5bfc\u5165>\u6279\u91cf\u5904\u7406\u591a\u6761\u6570\u636e(" + dynamicObjects.length + ")");
                    this.statusRecord.tagOperateSourceStatus(CodeRuleOpStatusRecord.OperateSourceStatus.IMPORT_BATCH_GENERATE);
                    this.batchGenerateNumberFromWebapi(dynamicObjects);
                    return;
                }
            }
            if (this.operateOption.tryGetVariableValue("isc_tag_of_datasource", new RefObject())) {
                datasource = this.operateOption.getVariableValue("isc_tag_of_datasource");
                if (String.valueOf(true).equals(datasource)) {
                    logger.info("[CodeRuleOp]<\u5f53\u524d\u4e3a\u96c6\u6210isc>\u6279\u91cf\u5904\u7406\u591a\u6761\u6570\u636e(" + dynamicObjects.length + ")");
                    this.statusRecord.tagOperateSourceStatus(CodeRuleOpStatusRecord.OperateSourceStatus.ISC_BATCH_GENERATE);
                    this.batchGenerateNumberFromWebapi(dynamicObjects);
                    return;
                }
            }
            this.restoreBillNoWhenPush(dynamicObjects);
            if (dynamicObjects.length > 1) {
                logger.info("[CodeRuleOp]<\u5f53\u524d\u4e3a\u5217\u8868or\u5168\u90e8\u4e0b\u63a8>\u6279\u91cf\u5904\u7406\u591a\u6761\u6570\u636e (" + dynamicObjects.length + ")");
                this.statusRecord.tagOperateSourceStatus(CodeRuleOpStatusRecord.OperateSourceStatus.PUSH_GENERATE_NUMBERS);
                if (this.isPushBatchGenerate(dynamicObjects[0])) {
                    this.batchGenerateNumberFromWebapi(dynamicObjects);
                    return;
                }
                this.batchGenerateNumber(dynamicObjects);
                return;
            }
            logger.info("[CodeRuleOp]<\u5f53\u524d\u4e3a\u8868\u5355\u6216\u5217\u8868>\u4ec5\u5904\u7406\u4e00\u6761\u6570\u636e");
            this.statusRecord.tagOperateSourceStatus(CodeRuleOpStatusRecord.OperateSourceStatus.BILL_GENERATE_NUMBER);
            this.singleGenerateNumber(dynamicObjects[0]);
        }
    }

    private void batchGenerateNumber(DynamicObject[] dynamicObjects) {
        if (CodeRuleSystemParam.getMcParam((String)"coderuleop_batch_generate") && dynamicObjects.length > 10) {
            this.batchGenerateNumbers(dynamicObjects);
            return;
        }
        for (DynamicObject dynamicObject : dynamicObjects) {
            this.singleGenerateNumber(dynamicObject);
        }
    }

    private void batchGenerateNumbers(DynamicObject[] dynamicObjects) {
        ArrayList<DynamicObject> needGenerate = new ArrayList<DynamicObject>();
        ArrayList<DynamicObject> needUpdate = new ArrayList<DynamicObject>();
        for (DynamicObject dynamicObject : dynamicObjects) {
            boolean isFromDatabase = dynamicObject.getDataEntityState().getFromDatabase();
            BillEntityType billEntityType = (BillEntityType)dynamicObject.getDataEntityType();
            CodeRuleInfo codeRuleInfo = this.getCodeRuleInfo(dynamicObject);
            if (codeRuleInfo == null || isFromDatabase && !this.isUpdateNumber(dynamicObject, billEntityType, codeRuleInfo)) continue;
            if (this.validateBOTPWayForGenerate()) {
                this.botpWayForComposeDynamicObject(dynamicObject, needGenerate, needUpdate);
                continue;
            }
            if (this.validateSpecialWayForGenerate(dynamicObject, billEntityType, codeRuleInfo)) continue;
            needGenerate.add(dynamicObject);
        }
        List<DynamicObject> generateNumberDy = this.batchGenerateNumbersAndSet(needGenerate);
        if (this.validateUniqueNumber(generateNumberDy)) {
            this.batchGenerateUniqueNumber(generateNumberDy);
        }
        this.recordUpdateMaximum(needUpdate, 2);
    }

    private void batchGenerateUniqueNumber(List<DynamicObject> generateNumberDy) {
        if (CollectionUtils.isEmpty(generateNumberDy)) {
            return;
        }
        BillEntityType billEntityType = (BillEntityType)generateNumberDy.get(0).getDataEntityType();
        String billNoKey = this.getBillNoField(billEntityType);
        LinkedHashMap<String, DynamicObject> dynamicObjectMap = this.convertListToMap(generateNumberDy, billNoKey);
        for (int i = 0; i < 50; ++i) {
            QFilter[] qFilters = this.getQFilterExistsUniqueBillNo(billNoKey, dynamicObjectMap);
            Map repeatDateInDB = this.uniqueBillno.checkRepeatDateInDB(billEntityType.getName(), billNoKey, dynamicObjectMap, qFilters);
            if (repeatDateInDB == null || repeatDateInDB.isEmpty()) {
                logger.info("[AbstractCodeRuleOp]\u6821\u9a8c\u91cd\u590d\u6570\u636e\uff1a" + ++i + "\u6b21\u540e\uff0c\u672a\u68c0\u6d4b\u5230\u91cd\u590d\u6570\u636e");
                break;
            }
            List<DynamicObject> generateNumbers = this.batchGenerateNumbersAndSet(new ArrayList<DynamicObject>(repeatDateInDB.values()));
            if (CollectionUtils.isEmpty(generateNumbers)) break;
            dynamicObjectMap = this.convertListToMap(generateNumbers, billNoKey);
        }
    }

    private LinkedHashMap<String, DynamicObject> convertListToMap(List<DynamicObject> generateNumberDy, String billNoKey) {
        LinkedHashMap<String, DynamicObject> dynamicObjectMap = new LinkedHashMap<String, DynamicObject>(generateNumberDy.size());
        for (DynamicObject dynamicObject : generateNumberDy) {
            dynamicObjectMap.put(dynamicObject.getString(billNoKey), dynamicObject);
        }
        return dynamicObjectMap;
    }

    private void botpWayForComposeDynamicObject(DynamicObject dynamicObject, List<DynamicObject> needGenerate, List<DynamicObject> needUpdate) {
        boolean isFromUpdate = dynamicObject.getDataEntityState().getFromDatabase();
        if (isFromUpdate) {
            needGenerate.add(dynamicObject);
            return;
        }
        String billNoField = this.getBillNoField((BillEntityType)dynamicObject.getDataEntityType());
        String number = dynamicObject.getString(billNoField);
        if (StringUtils.isBlank((CharSequence)number)) {
            needGenerate.add(dynamicObject);
            return;
        }
        needUpdate.add(dynamicObject);
    }

    private List<DynamicObject> batchGenerateNumbersAndSet(List<DynamicObject> needGenerate) {
        if (CollectionUtils.isEmpty(needGenerate)) {
            return null;
        }
        String billNoFldKey = this.getBillNoField((BillEntityType)this.billEntityType);
        String entityNum = needGenerate.get(0).getDataEntityType().getName();
        List numbers = this.codeRuleService.singCodeRuleInfoFun(this::getCodeRuleInfo).getNumbers(entityNum, needGenerate);
        ArrayList<DynamicObject> generatedNumber = new ArrayList<DynamicObject>();
        for (int i = 0; i < needGenerate.size(); ++i) {
            DynamicObject dynamicObject = needGenerate.get(i);
            if (numbers.get(i) == null) {
                this.statusRecord.tagResultStatus(dynamicObject, CodeRuleOpStatusRecord.ResultStatus.NO_NUMBER_GENERATE);
                continue;
            }
            dynamicObject.set(billNoFldKey, numbers.get(i));
            generatedNumber.add(dynamicObject);
            this.statusRecord.tagResultStatus(dynamicObject, CodeRuleOpStatusRecord.ResultStatus.NEW_NUMBER);
        }
        return generatedNumber;
    }

    protected boolean validateUniqueNumber(List<DynamicObject> generateNumberDy) {
        if (CollectionUtils.isEmpty(generateNumberDy)) {
            return false;
        }
        Map operMeta = this.operateMeta;
        List validationList = (List)operMeta.get("validations");
        String billNoFldKey = this.getBillNoField((BillEntityType)this.billEntityType);
        return this.uniqueBillno.validateGrpFieldUnique(billNoFldKey, validationList, generateNumberDy.get(0));
    }

    private void restoreBillNoWhenPush(DynamicObject[] dynamicObjects) {
        if (!this.validateBOTPWayForGenerate()) {
            return;
        }
        RefObject result = new RefObject();
        BillEntityType billEntityType = (BillEntityType)dynamicObjects[0].getDataEntityType();
        String billNoField = this.getBillNoField(billEntityType);
        if (this.operateOption.tryGetVariableValue(BOTP_WITH_NULL_VALUE_BILLNO, result)) {
            String value = (String)result.getValue();
            if (StringUtils.isBlank((CharSequence)value)) {
                return;
            }
            Map blankNumberFields = (Map)SerializationUtils.fromJsonString((String)value, Map.class);
            logger.info("[AbstractCodeRuleOp]\u4e0b\u63a8\u65b9\u5f0f\u8fdb\u5165\u7f16\u7801\u89c4\u5219\uff0c\u7f16\u53f7\u4e3a\u7a7a\u7684\u5b57\u6bb5\uff1a" + value);
            for (DynamicObject dynamicObject : dynamicObjects) {
                List fieldList;
                String pkValue;
                CodeRuleInfo codeRuleInfo;
                boolean fromDatabase = dynamicObject.getDataEntityState().getFromDatabase();
                if (fromDatabase || (codeRuleInfo = this.getCodeRuleInfo(dynamicObject)) == null || !blankNumberFields.containsKey(pkValue = "" + dynamicObject.getPkValue()) || CollectionUtils.isEmpty((Collection)(fieldList = (List)blankNumberFields.get(pkValue))) || !fieldList.contains(billNoField) || billEntityType.getProperties().get((Object)billNoField) == null) continue;
                dynamicObject.set(billNoField, (Object)"");
            }
        }
    }

    protected boolean validateOperateKey() {
        Map operMeta = this.operateMeta;
        if (null == operMeta) {
            return false;
        }
        String operateType = (String)operMeta.get("type");
        return "save".equals(operateType) || "submit".equals(operateType);
    }

    private boolean isPushBatchGenerate(DynamicObject dynamicObject) {
        String entityName = dynamicObject.getDataEntityType().getName();
        DynamicObject billParameter = ParameterReader.getBillParameter((String)entityName);
        if (null == billParameter) {
            return false;
        }
        return billParameter.getBoolean("pushbatchnumbers");
    }

    protected void rebackBillno(AddValidatorsEventArgs e) {
        DynamicObject[] dynamicObjects = e.getDataEntities();
        if (!this.validateEntityType()) {
            return;
        }
        if (!this.validateOperateKey()) {
            return;
        }
        if (dynamicObjects != null && dynamicObjects.length > 0) {
            BillEntityType dataEntityType;
            String billno;
            String importFlag;
            if (this.operateOption.tryGetVariableValue("ignoreinteraction", new RefObject()) && "true".equals(importFlag = this.operateOption.getVariableValue("ignoreinteraction"))) {
                return;
            }
            if (dynamicObjects.length > 1) {
                return;
            }
            boolean skipbillnovalidator = this.operateOption.tryGetVariableValue(this.getSkipbillnovalidator(), new RefObject());
            if (skipbillnovalidator && this.operateOption.getVariableValue(this.getSkipbillnovalidator()).equals(String.valueOf(true)) && "".equals(billno = dynamicObjects[0].get(this.getBillNoField(dataEntityType = (BillEntityType)dynamicObjects[0].getDataEntityType())).toString())) {
                logger.info("[CodeRuleOp]\u8fd8\u539f\u88ab\u8868\u5355\u63d2\u4ef6\u8bbe\u7f6e\u7684\u7a7a\u503c,\u5e76\u8865\u5145CodeRuleValidator\u6821\u9a8c\u5668");
                CodeRuleValidator validator = new CodeRuleValidator(this.operateOption.getVariableValue(this.getSkipbillnovalidatorValue()));
                e.getValidators().add(0, validator);
            }
        }
    }

    private boolean validateEntityType() {
        return this.billEntityType instanceof BillEntityType;
    }

    private void recycleOldNumberWhenSuccess(IOperationResult operationResult) {
        HashSet successPkIds = new HashSet(operationResult.getSuccessPkIds());
        Map allDataStatus = this.statusRecord.getDataOperateProcess();
        if (allDataStatus == null || allDataStatus.isEmpty()) {
            return;
        }
        for (Map.Entry statusEntry : allDataStatus.entrySet()) {
            DynamicObject dynamicObject = (DynamicObject)statusEntry.getKey();
            if (!successPkIds.contains(dynamicObject.getPkValue()) || CodeRuleOpStatusRecord.RecycleStatus.GENERATE_NEW_RECYCLE_OLD != ((CodeRuleOpStatusRecord.OperateProcess)statusEntry.getValue()).getRecycleStatus() && CodeRuleOpStatusRecord.RecycleStatus.USER_INPUT_RECYCLE_OLD != ((CodeRuleOpStatusRecord.OperateProcess)statusEntry.getValue()).getRecycleStatus()) continue;
            CodeRuleInfo codeRuleInfo = this.statusRecord.getNeedRecycleCodeRule(dynamicObject);
            this.recycleOldNumber(codeRuleInfo, dynamicObject);
        }
    }

    private void singleGenerateNumber(DynamicObject dynamicObject) {
        boolean isNew;
        BillEntityType billEntityType = (BillEntityType)dynamicObject.getDataEntityType();
        CodeRuleInfo codeRuleInfo = this.getCodeRuleInfo(dynamicObject);
        if (codeRuleInfo == null) {
            return;
        }
        boolean bl = isNew = !dynamicObject.getDataEntityState().getFromDatabase();
        if (isNew) {
            logger.info("[CodeRuleOp]\u65b0\u589e\u4e00\u6761\u6570\u636e");
            this.statusRecord.tagDataStatus(dynamicObject, CodeRuleOpStatusRecord.DataStatus.DATA_NEW);
            this.singleGenerateNewNumber(dynamicObject, billEntityType, codeRuleInfo);
        } else {
            logger.info("[CodeRuleOp]\u4fee\u6539\u4e00\u6761\u6570\u636e");
            this.statusRecord.tagDataStatus(dynamicObject, CodeRuleOpStatusRecord.DataStatus.DATA_UPDATE);
            this.singleGenerateUpdateNumber(dynamicObject, billEntityType, codeRuleInfo);
        }
    }

    protected CodeRuleInfo getCodeRuleInfo(DynamicObject dynamicObject) {
        String entityNum = dynamicObject.getDataEntityType().getName();
        String orgId = OrgUtil.getMainOrgId((DynamicObject)dynamicObject);
        return CodeRuleServiceHelper.getCodeRule((String)entityNum, (DynamicObject)dynamicObject, (String)orgId);
    }

    private void singleGenerateNewNumber(DynamicObject dynamicObject, BillEntityType billEntityType, CodeRuleInfo codeRuleInfo) {
        logger.info("[CodeRuleOp]\u751f\u6210\u7f16\u53f7");
        String billNoFldKey = this.getBillNoField(billEntityType);
        if (this.validateBOTPWayForGenerate()) {
            this.botpWayForGenerate(dynamicObject, codeRuleInfo, billNoFldKey);
            return;
        }
        if (this.validateSpecialWayForGenerate(dynamicObject, billEntityType, codeRuleInfo)) {
            return;
        }
        this.statusRecord.tagResultStatus(dynamicObject, CodeRuleOpStatusRecord.ResultStatus.NO_NUMBER_GENERATE);
        if (this.validateChangeBillnoForGenerate(codeRuleInfo, dynamicObject.getPkValue())) {
            this.commonGenerateNewNumber(dynamicObject, codeRuleInfo, billNoFldKey);
        } else {
            this.statusRecord.tagBillNoStatus(dynamicObject, CodeRuleOpStatusRecord.BillNoStatus.IS_USER_INPUT);
            if (codeRuleInfo.isMatchCodeRule() || StringUtils.isNotBlank((CharSequence)codeRuleInfo.getUpdateMaxNumber()) || StringUtils.isNotBlank((CharSequence)codeRuleInfo.getUseInterruption())) {
                int type = this.getOperateSourceType();
                this.recordUpdateMaximum(Collections.singletonList(dynamicObject), type);
            }
        }
    }

    private boolean validateBOTPWayForGenerate() {
        RefObject result = new RefObject();
        if (this.operateOption.tryGetVariableValue("botptag_of_datasource", result)) {
            return Boolean.parseBoolean((String)result.getValue());
        }
        return false;
    }

    private void botpWayForGenerate(DynamicObject dynamicObject, CodeRuleInfo codeRuleInfo, String billNoFldKey) {
        String number = dynamicObject.getString(billNoFldKey);
        boolean isFromDatabase = dynamicObject.getDataEntityState().getFromDatabase();
        boolean isPushReadNumber = this.pushNumberCache.isPushReadNumber(this.operateOption, dynamicObject.getPkValue());
        logger.info("[AbstractCodeRuleOp]\u4e0b\u63a8\u65b9\u5f0f\u8fdb\u5165\uff0c\u4f20\u5165\u7f16\u53f7\u4e3a\uff1a" + number + "\uff0c\u662f\u5426\u9884\u8bfb\u7f16\u53f7\uff1a" + isPushReadNumber);
        if (StringUtils.isBlank((CharSequence)number) || isFromDatabase || isPushReadNumber) {
            this.commonGenerateNewNumber(dynamicObject, codeRuleInfo, billNoFldKey);
            return;
        }
        this.recordUpdateMaximum(Collections.singletonList(dynamicObject), 2);
    }

    private void commonGenerateNewNumber(DynamicObject dynamicObject, CodeRuleInfo codeRuleInfo, String billNoFldKey) {
        this.statusRecord.tagResultStatus(dynamicObject, CodeRuleOpStatusRecord.ResultStatus.NEW_NUMBER);
        String newNumber = this.executeGenerateProcess(dynamicObject, codeRuleInfo);
        if (this.isOpenForOnlyNumber(billNoFldKey, codeRuleInfo, dynamicObject)) {
            newNumber = this.generateNumberForUniqueBillno(dynamicObject, billNoFldKey, codeRuleInfo, newNumber);
            this.statusRecord.tagResultStatus(dynamicObject, CodeRuleOpStatusRecord.ResultStatus.NEW_NUMBER_UNIQUE);
        }
        dynamicObject.set(billNoFldKey, (Object)newNumber);
    }

    private boolean validateSpecialWayForGenerate(DynamicObject dynamicObject, BillEntityType billEntityType, CodeRuleInfo codeRuleInfo) {
        if (!this.operateOption.tryGetVariableValue(this.getSkipbillnovalidator(), new RefObject()) && !"".equals(dynamicObject.getString(this.getBillNoField(billEntityType)))) {
            logger.info("[AbstractCodeRuleOp]\u901a\u8fc7OperationServiceHelper\u65b9\u5f0f\u8fdb\u5165\u5230\u64cd\u4f5c\u63d2\u4ef6");
            boolean fitCodeRule = this.codeRuleService.checkNumber(dynamicObject, dynamicObject.getString(this.getBillNoField(billEntityType)), codeRuleInfo);
            if (!fitCodeRule) {
                logger.info("[AbstractCodeRuleOp]\u4f20\u5165\u7684\u7f16\u53f7\u4e0e\u7f16\u7801\u89c4\u5219\u683c\u5f0f\u4e0d\u4e00\u81f4,\u5224\u65ad\u5f53\u524d\u573a\u666f\u662f\u5355\u5143\u6d4b\u8bd5");
                this.statusRecord.tagBillNoStatus(dynamicObject, CodeRuleOpStatusRecord.BillNoStatus.FORMAT_MISMATCH);
                return true;
            }
        }
        return false;
    }

    protected String getBillNoField(BillEntityType billEntityType) {
        return billEntityType.getBillNo();
    }

    private boolean validateChangeBillnoForGenerate(CodeRuleInfo codeRuleInfo, Object pkId) {
        return codeRuleInfo.getIsModifiable() == false || !this.billNoChangeCache.isChange(this.operateOption, pkId);
    }

    protected boolean isOpenForOnlyNumber(String billNoFldKey, CodeRuleInfo codeRuleInfo, DynamicObject dynamicObject) {
        Map operMeta = this.operateMeta;
        List validationList = (List)operMeta.get("validations");
        return this.uniqueBillno.validateGrpFieldUnique(billNoFldKey, validationList, dynamicObject);
    }

    private String generateNumberForUniqueBillno(DynamicObject dynamicObject, String billNoFldKey, CodeRuleInfo codeRuleInfo, String number) {
        block2: {
            String entityNum = dynamicObject.getDataEntityType().getName();
            int loopCount = 0;
            do {
                boolean existBillNum;
                if (!(existBillNum = this.uniqueBillno.checkReatedInDB(entityNum, this.getQFilterExistUniqueBillNo(billNoFldKey, number, dynamicObject)))) {
                    logger.info("[CodeRuleOp]\u5c1d\u8bd5" + loopCount + "\u6b21\u540e\u53d1\u73b0\u4e0d\u91cd\u590d\u7684\u7f16\u7801");
                    break block2;
                }
                number = this.executeGenerateProcess(dynamicObject, codeRuleInfo);
            } while (++loopCount <= 50);
            logger.info("[CodeRuleOp]\u5c1d\u8bd550\u6b21\u540e\u4f9d\u7136\u6ca1\u6709\u53d1\u73b0\u4e0d\u91cd\u590d\u7684\u7f16\u7801,\u505c\u6b62\u7ee7\u7eed\u6d88\u8017\u6d41\u6c34\u53f7");
        }
        return number;
    }

    private String executeGenerateProcess(DynamicObject dynamicObject, CodeRuleInfo codeRuleInfo) {
        String number = "";
        if (null == this.numberModes) {
            return number;
        }
        try {
            for (CodeRuleNumberMode numberMode : this.numberModes) {
                number = numberMode.getNumber(dynamicObject, codeRuleInfo);
            }
        }
        catch (KDException e) {
            if (null != e.getErrorCode() && StringUtils.equals((CharSequence)"CODERULE_NO_EXECUTE", (CharSequence)e.getErrorCode().getCode())) {
                this.statusRecord.tagResultStatus(dynamicObject, CodeRuleOpStatusRecord.ResultStatus.NEW_NUMBER_CACHE);
                logger.info("[CodeRuleOp]" + e.getErrorCode().getMessage());
                return number;
            }
            if (e.getErrorCode() != null && "ERRCODE_CODERULE_VALIDATE_NUMBER".equals(e.getErrorCode().getCode())) {
                throw new KDBizException(e.getErrorCode(), new Object[0]);
            }
            logger.error((Throwable)e);
            throw new KDException(e.getErrorCode(), new Object[0]);
        }
        return number;
    }

    protected QFilter[] getQFilterExistUniqueBillNo(String billNoFldKey, String number, DynamicObject dynamicObject) {
        return this.uniqueBillno.buildQFilter(billNoFldKey, number, dynamicObject);
    }

    protected QFilter[] getQFilterExistsUniqueBillNo(String billNoFldKey, LinkedHashMap<String, DynamicObject> dynamicObjectMap) {
        return this.uniqueBillno.buildQFilter(billNoFldKey, dynamicObjectMap);
    }

    private void singleGenerateUpdateNumber(DynamicObject dynamicObject, BillEntityType billEntityType, CodeRuleInfo codeRuleInfo) {
        if (this.isUpdateNumber(dynamicObject, billEntityType, codeRuleInfo)) {
            logger.info("[CodeRuleOp]\u91cd\u751f\u6210\u7f16\u53f7");
            this.singleGenerateNewNumber(dynamicObject, billEntityType, codeRuleInfo);
        }
    }

    private boolean isUpdateNumber(DynamicObject dynamicObject, BillEntityType billEntityType, CodeRuleInfo codeRuleInfo) {
        if (!this.validateChangeBillnoForGenerate(codeRuleInfo, dynamicObject.getPkValue())) {
            this.statusRecord.tagBillNoStatus(dynamicObject, CodeRuleOpStatusRecord.BillNoStatus.IS_USER_INPUT);
            this.statusRecord.tagResultStatus(dynamicObject, CodeRuleOpStatusRecord.ResultStatus.NO_NUMBER_GENERATE);
            this.statusRecord.tagRecycleStatus(dynamicObject, CodeRuleOpStatusRecord.RecycleStatus.USER_INPUT_RECYCLE_OLD);
            this.statusRecord.setNeedRecycleCodeRule(dynamicObject, codeRuleInfo);
            if (codeRuleInfo.isMatchCodeRule() || StringUtils.isNotBlank((CharSequence)codeRuleInfo.getUpdateMaxNumber()) || StringUtils.isNotBlank((CharSequence)codeRuleInfo.getUseInterruption())) {
                this.recordUpdateMaximum(Collections.singletonList(dynamicObject), this.getOperateSourceType());
            }
            return false;
        }
        this.statusRecord.tagResultStatus(dynamicObject, CodeRuleOpStatusRecord.ResultStatus.UPDATE_NUMBER);
        boolean isUpdate = this.validateRebuildNumberAtUpdate(dynamicObject, billEntityType, codeRuleInfo);
        if (isUpdate) {
            this.statusRecord.tagRecycleStatus(dynamicObject, CodeRuleOpStatusRecord.RecycleStatus.GENERATE_NEW_RECYCLE_OLD);
            this.statusRecord.setNeedRecycleCodeRule(dynamicObject, codeRuleInfo);
        }
        return isUpdate;
    }

    protected boolean validateRebuildNumberAtUpdate(DynamicObject dynamicObject, BillEntityType billEntityType, CodeRuleInfo codeRuleInfo) {
        if (!codeRuleInfo.isUpdateRecover()) {
            return false;
        }
        String billNoFldKey = this.getBillNoField(billEntityType);
        String number = dynamicObject.getString(billNoFldKey);
        return StringUtils.isNotBlank((CharSequence)number) && this.dirtyPropsContainBillNoRelFieldKey(dynamicObject, billNoFldKey, codeRuleInfo);
    }

    protected void signResourceFromList() {
        this.operateOption.setVariableValue(this.getSkipbillnovalidator(), String.valueOf(true));
    }

    private boolean dirtyPropsContainBillNoRelFieldKey(DynamicObject dObj, String billNoFieldKey, CodeRuleInfo codeRuleInfo) {
        List dirtyProperties = dObj.getDataEntityState().GetDirtyProperties();
        if (CollectionUtils.isEmpty((Collection)dirtyProperties)) {
            return false;
        }
        List dirtyPropKeys = dirtyProperties.stream().map(IMetadata::getName).collect(Collectors.toList());
        String billStatusFieldKey = "";
        IDataEntityType dataEntityType = dObj.getDataEntityType();
        if (dataEntityType instanceof BillEntityType) {
            billStatusFieldKey = ((BillEntityType)dataEntityType).getBillStatus();
        }
        Set<String> propertiesSet = this.getPropertiesFromCodeRule(billStatusFieldKey, codeRuleInfo);
        List<String> skipPropKeys = this.getSkipPropKeysForDirtyCheck();
        if (CollectionUtils.isEmpty(propertiesSet)) {
            return false;
        }
        for (String propertyKey : propertiesSet) {
            if (!StringUtils.isNotEmpty((CharSequence)propertyKey) || !CollectionUtils.isEmpty(skipPropKeys) && skipPropKeys.contains(propertyKey) || !dirtyPropKeys.contains(propertyKey)) continue;
            return true;
        }
        return false;
    }

    private Set<String> getPropertiesFromCodeRule(String billStatusFieldKey, CodeRuleInfo codeRuleInfo) {
        Set filterConditionFields;
        Set<String> propertiesSet = Optional.ofNullable(codeRuleInfo.getRuleEntry()).map(Collection::stream).orElseGet(Stream::empty).filter(StringUtils::isNotBlank).map(entry -> entry.getValueAtribute().split("\\.")[0]).collect(Collectors.toSet());
        List conditionEntry = codeRuleInfo.getConditionEntry();
        if (CollectionUtils.isNotEmpty((Collection)conditionEntry)) {
            for (ConditionEntryInfo entryInfo : conditionEntry) {
                String conditionPropKey = entryInfo.getProperty();
                if (StringUtils.isBlank((CharSequence)conditionPropKey) || StringUtils.isNotEmpty((CharSequence)billStatusFieldKey) && billStatusFieldKey.equals(conditionPropKey)) continue;
                String[] splitKey = StringUtils.split((String)entryInfo.getProperty(), (String)".");
                propertiesSet.add(splitKey[0]);
            }
        }
        if (CollectionUtils.isNotEmpty((Collection)(filterConditionFields = CodeRuleInfoUtil.getFilterConditionFields((CodeRuleInfo)codeRuleInfo)))) {
            propertiesSet.addAll(filterConditionFields);
        }
        return propertiesSet;
    }

    protected List<String> getSkipPropKeysForDirtyCheck() {
        return null;
    }

    private void recycleOldNumber(CodeRuleInfo codeRuleInfo, DynamicObject dynamicObject) {
        DynamicObject dynamicObj = this.recycleCache.getDynamicObj(this.operateOption, (DynamicObjectType)dynamicObject.getDataEntityType(), dynamicObject.getPkValue());
        String recycleNumber = this.recycleCache.getNumber(this.operateOption, dynamicObject.getPkValue());
        if (null != dynamicObj && StringUtils.isNotBlank((CharSequence)recycleNumber)) {
            CodeRuleInfo oldCodeRule = this.getCodeRuleInfo(dynamicObj);
            if (null != oldCodeRule && oldCodeRule.getIsNonBreak().booleanValue()) {
                this.codeRuleService.recycleNumber(oldCodeRule, dynamicObj, recycleNumber);
            } else if (null != codeRuleInfo && codeRuleInfo.getIsNonBreak().booleanValue()) {
                this.codeRuleService.recycleNumber(codeRuleInfo, dynamicObj, recycleNumber);
            }
        }
    }

    private void batchGenerateNumberFromWebapi(DynamicObject[] dynamicObjects) {
        this.batchGenerateNumberBySimpleLogic(dynamicObjects);
    }

    private void batchGenerateNumberBySimpleLogic(DynamicObject[] dynamicObjects) {
        ArrayList<DynamicObject> mayGenerateDynamicObjs = new ArrayList<DynamicObject>(8);
        ArrayList<DynamicObject> unGenerateDynamicObjs = new ArrayList<DynamicObject>(8);
        this.statusRecord.batchTagDataStatus(dynamicObjects, CodeRuleOpStatusRecord.DataStatus.BATCH_GENERATE);
        this.composeDynamicObjs(dynamicObjects, mayGenerateDynamicObjs, unGenerateDynamicObjs);
        this.generateNumbers(mayGenerateDynamicObjs);
        this.recordUpdateMaximum(unGenerateDynamicObjs, this.getOperateSourceType());
    }

    private List<DynamicObject> composeDynamicObjs(DynamicObject[] originalDynamicObjects, List<DynamicObject> mayGenerateDynamicObjs, List<DynamicObject> unGenerateDynamicObjs) {
        if (!this.validateBillNoForGenerate()) {
            return new ArrayList<DynamicObject>(0);
        }
        String billNoFldKey = this.getBillNoField((BillEntityType)this.billEntityType);
        for (DynamicObject obj : originalDynamicObjects) {
            String billNo = obj.getString(billNoFldKey);
            if (StringUtils.isNotBlank((CharSequence)billNo)) {
                unGenerateDynamicObjs.add(obj);
                this.statusRecord.tagBillNoStatus(obj, CodeRuleOpStatusRecord.BillNoStatus.IS_EXCEL_INPUT);
                continue;
            }
            this.statusRecord.tagBillNoStatus(obj, CodeRuleOpStatusRecord.BillNoStatus.EMPTY);
            mayGenerateDynamicObjs.add(obj);
        }
        return mayGenerateDynamicObjs;
    }

    private boolean validateBillNoForGenerate() {
        BillEntityType dt = (BillEntityType)this.billEntityType;
        if (dt == null) {
            return false;
        }
        String billNoFldKey = this.getBillNoField(dt);
        return !StringUtils.isEmpty((CharSequence)billNoFldKey);
    }

    private void generateNumbers(List<DynamicObject> mayGenerateDynamicObjs) {
        if (!this.validateBillNoForGenerate()) {
            return;
        }
        if (CollectionUtils.isEmpty(mayGenerateDynamicObjs)) {
            return;
        }
        String billNoFldKey = this.getBillNoField((BillEntityType)this.billEntityType);
        String entityName = mayGenerateDynamicObjs.get(0).getDataEntityType().getName();
        List numbers = this.codeRuleService.singCodeRuleInfoFun(this::getCodeRuleInfo).getNumbers(entityName, mayGenerateDynamicObjs);
        for (int i = 0; i < mayGenerateDynamicObjs.size(); ++i) {
            DynamicObject dynamicObject = mayGenerateDynamicObjs.get(i);
            if (numbers.get(i) == null) {
                this.statusRecord.tagResultStatus(dynamicObject, CodeRuleOpStatusRecord.ResultStatus.NO_NUMBER_GENERATE);
                continue;
            }
            dynamicObject.set(billNoFldKey, numbers.get(i));
            this.statusRecord.tagResultStatus(dynamicObject, CodeRuleOpStatusRecord.ResultStatus.NEW_NUMBER);
        }
    }

    private void recordUpdateMaximum(List<DynamicObject> updateDynamicObjs, int type) {
        String billNoFldKey = this.collectVerifyData(updateDynamicObjs);
        ArrayList<DynamicObject> updateMaxNumberObjs = new ArrayList<DynamicObject>();
        ArrayList<DynamicObject> useInterruptionObjs = new ArrayList<DynamicObject>();
        ArrayList<DynamicObject> historyRecordObjs = new ArrayList<DynamicObject>();
        for (DynamicObject dynamicObject : updateDynamicObjs) {
            CodeRuleInfo codeRuleInfo = this.getCodeRuleInfo(dynamicObject);
            if (null == codeRuleInfo) continue;
            if (this.isNotBlankParam(codeRuleInfo.getUpdateMaxNumber()) && codeRuleInfo.getUpdateMaxNumber().contains(String.valueOf(type))) {
                updateMaxNumberObjs.add(dynamicObject);
            }
            if (this.isNotBlankParam(codeRuleInfo.getUseInterruption()) && codeRuleInfo.getUseInterruption().contains(String.valueOf(type))) {
                useInterruptionObjs.add(dynamicObject);
            }
            if (!StringUtils.isBlank((CharSequence)codeRuleInfo.getUpdateMaxNumber()) || !StringUtils.isBlank((CharSequence)codeRuleInfo.getUseInterruption())) continue;
            historyRecordObjs.add(dynamicObject);
        }
        this.statusRecord.setUpdateMaxNumberGroup(this.codeRuleService.collectionUpdateMaximum(updateMaxNumberObjs, billNoFldKey));
        this.statusRecord.setUseInterruptionGroup(this.codeRuleService.collectionUpdateMaximum(useInterruptionObjs, billNoFldKey));
        this.statusRecord.setUpdateCodeRuleGroup(this.codeRuleService.collectionUpdateMaximum(historyRecordObjs, billNoFldKey));
    }

    private boolean isNotBlankParam(String param) {
        return StringUtils.isNotBlank((CharSequence)param) && !StringUtils.equals((CharSequence)"-1", (CharSequence)param);
    }

    private String collectVerifyData(List<DynamicObject> updateDynamicObjs) {
        String billNoFldKey = this.getBillNoField((BillEntityType)this.billEntityType);
        if (CollectionUtils.isNotEmpty(updateDynamicObjs) && StringUtils.isNotBlank((CharSequence)billNoFldKey)) {
            ArrayList verifyData = new ArrayList();
            String entityName = updateDynamicObjs.get(0).getDataEntityType().getName();
            List dataEntities = GroupHandlerUtil.convertToGroupHandlerDataEntities(updateDynamicObjs, (String)billNoFldKey);
            new /* Unavailable Anonymous Inner Class!! */.process();
            this.numberValidator.setVerifyData(verifyData);
            this.numberValidator.setBillNoProp(this.billEntityType.getProperty(billNoFldKey));
        }
        return billNoFldKey;
    }

    private int getOperateSourceType() {
        CodeRuleOpStatusRecord.OperateSourceStatus operateSourceStatus = this.statusRecord.getOperateSourceStatus();
        if (operateSourceStatus == CodeRuleOpStatusRecord.OperateSourceStatus.WEBAPI_BATCH_GENERATE || operateSourceStatus == CodeRuleOpStatusRecord.OperateSourceStatus.IMPORT_BATCH_GENERATE || operateSourceStatus == CodeRuleOpStatusRecord.OperateSourceStatus.ISC_BATCH_GENERATE) {
            return 1;
        }
        if (operateSourceStatus == CodeRuleOpStatusRecord.OperateSourceStatus.PUSH_GENERATE_NUMBERS) {
            return 2;
        }
        return 0;
    }
}
