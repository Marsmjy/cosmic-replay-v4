/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.business.plugin.AbstractCodeRuleOp
 *  kd.bos.business.plugin.CodeRuleOpStatusRecord$BillNoStatus
 *  kd.bos.business.plugin.CodeRuleOpStatusRecord$ResultStatus
 *  kd.bos.business.plugin.mode.CodeRuleNumberMode
 *  kd.bos.coderule.api.CodeRuleInfo
 *  kd.bos.coderule.service.CodeRuleServiceImp
 *  kd.bos.coderule.util.CodeRuleSystemParam
 *  kd.bos.dataentity.RefObject
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.BillEntityType
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.ReturnOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.util.StringUtils
 *  org.apache.commons.collections4.CollectionUtils
 */
package kd.bos.business.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kd.bos.business.plugin.AbstractCodeRuleOp;
import kd.bos.business.plugin.CodeRuleOpStatusRecord;
import kd.bos.business.plugin.mode.CodeRuleNumberMode;
import kd.bos.coderule.api.CodeRuleInfo;
import kd.bos.coderule.service.CodeRuleServiceImp;
import kd.bos.coderule.util.CodeRuleSystemParam;
import kd.bos.dataentity.RefObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.BillEntityType;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.ReturnOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;

public class CodeRuleOp
extends AbstractCodeRuleOp {
    private static final Log logger = LogFactory.getLog(CodeRuleOp.class);
    private DynamicObject[] allData = null;
    private CodeRuleServiceImp codeRuleServiceImp = new CodeRuleServiceImp();

    protected void setCodeRuleService(CodeRuleServiceImp codeRuleServiceImp) {
        this.codeRuleService = codeRuleServiceImp;
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        this.rebackBillno(e);
        this.allData = e.getDataEntities();
        this.generateNumber(e.getDataEntities());
        RefObject result = new RefObject();
        if (this.operateOption.tryGetVariableValue("importtag_of_datasource", result) && Boolean.parseBoolean((String)result.getValue())) {
            e.addValidator((AbstractValidator)this.numberValidator);
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
    }

    public void onReturnOperation(ReturnOperationArgs e) {
        super.onReturnOperation(e);
        if (this.allData == null || this.allData.length == 0) {
            return;
        }
        HashSet successPkIds = new HashSet(e.getOperationResult().getSuccessPkIds());
        ArrayList<DynamicObject> list = new ArrayList<DynamicObject>(Arrays.asList(this.allData));
        for (int i = this.allData.length - 1; i >= 0; --i) {
            if (!successPkIds.contains(this.allData[i].getPkValue())) continue;
            list.remove(i);
        }
        if (!list.isEmpty()) {
            logger.info("[CodeRuleOp]\u672c\u6b21\u64cd\u4f5c\u6ca1\u6709\u6210\u529f\u51c6\u5907\u5c1d\u8bd5\u56de\u6536\u5904\u7406");
            this.recycleNumber(list, (BillEntityType)this.billEntityType);
        }
    }

    protected void recycleNumber(List<DynamicObject> list, BillEntityType dt) {
        if (dt == null) {
            return;
        }
        String billNoFldKey = this.getBillNoField(dt);
        if (StringUtils.isBlank((String)billNoFldKey)) {
            return;
        }
        String entityNum = list.get(0).getDataEntityType().getName();
        List codeRuleList = this.codeRuleService.getAllCodeRuleByEntity(entityNum);
        if (CollectionUtils.isEmpty((Collection)codeRuleList)) {
            return;
        }
        ArrayList<DynamicObject> toRecycleObjs = new ArrayList<DynamicObject>(list.size());
        for (DynamicObject dynamicObject : list) {
            if (null == this.checkCodeRule(entityNum, codeRuleList, dynamicObject)) continue;
            toRecycleObjs.add(dynamicObject);
        }
        if (CodeRuleSystemParam.getMcParam((String)"code_rule_op.recycleNumber") && toRecycleObjs.size() > 10) {
            this.recycleBatchNumber(billNoFldKey, entityNum, toRecycleObjs);
            return;
        }
        this.recycleNumberOperation(billNoFldKey, entityNum, codeRuleList, toRecycleObjs);
    }

    private void recycleBatchNumber(String billNoFldKey, String entityNum, List<DynamicObject> toRecycleObjs) {
        if (CollectionUtils.isEmpty(toRecycleObjs)) {
            return;
        }
        Set<String> existNumbers = this.validateNumbersInBusinessDB(entityNum, billNoFldKey, toRecycleObjs);
        ArrayList<DynamicObject> dataInfo = new ArrayList<DynamicObject>(toRecycleObjs.size());
        ArrayList<String> numbers = new ArrayList<String>(toRecycleObjs.size());
        ArrayList<String> logExistNumbers = new ArrayList<String>();
        ArrayList<String> logNotRegenNumbers = new ArrayList<String>();
        for (DynamicObject dynamicObject : toRecycleObjs) {
            String number = dynamicObject.getString(billNoFldKey);
            if (CollectionUtils.isNotEmpty(existNumbers) && existNumbers.contains(number)) {
                logExistNumbers.add(number);
                continue;
            }
            boolean isEdit = dynamicObject.getDataEntityState().getFromDatabase();
            if (isEdit && CodeRuleOpStatusRecord.ResultStatus.NEW_NUMBER != this.statusRecord.getDataResultStatus(dynamicObject) && CodeRuleOpStatusRecord.ResultStatus.NEW_NUMBER_UNIQUE != this.statusRecord.getDataResultStatus(dynamicObject)) {
                logNotRegenNumbers.add(dynamicObject.getString(billNoFldKey));
                continue;
            }
            CodeRuleOpStatusRecord.BillNoStatus billNoStatus = this.statusRecord.getBillNoStatus(dynamicObject);
            if (CodeRuleOpStatusRecord.BillNoStatus.IS_USER_INPUT == billNoStatus || CodeRuleOpStatusRecord.BillNoStatus.IS_EXCEL_INPUT == billNoStatus) continue;
            numbers.add(number);
            dataInfo.add(dynamicObject);
        }
        this.recordNoRecycleLog(logExistNumbers, logNotRegenNumbers);
        this.codeRuleServiceImp.singCodeRuleInfoFun(this::getRecycleCodeRuleInfo).recycleBatchNumber(entityNum, dataInfo.toArray(new DynamicObject[0]), null, numbers.toArray(new String[0]));
    }

    private CodeRuleInfo getRecycleCodeRuleInfo(DynamicObject dynamicObject) {
        String entityNum = dynamicObject.getDataEntityType().getName();
        List codeRuleList = this.codeRuleService.getAllCodeRuleByEntity(entityNum);
        return this.checkCodeRule(entityNum, codeRuleList, dynamicObject);
    }

    private void recordNoRecycleLog(List<String> logExistNumbers, List<String> logNotRegenNumbers) {
        if (CollectionUtils.isNotEmpty(logExistNumbers)) {
            logger.info("[CodeRuleOp]\u6821\u9a8c\u7f16\u53f7\u5728\u6570\u636e\u5e93\u4e2d\u5df2\u5b58\u5728\uff1a" + String.join((CharSequence)"\u3001", logExistNumbers) + "\uff0c\u4e0d\u56de\u6536");
        }
        if (CollectionUtils.isNotEmpty(logNotRegenNumbers)) {
            logger.info("[CodeRuleOp]\u52a8\u6001\u5bf9\u8c61\u6765\u81ea\u6570\u636e\u5e93\u4e14\u672a\u91cd\u751f\u6210\u7f16\u7801\uff1a" + String.join((CharSequence)"\u3001", logNotRegenNumbers) + "\uff0c\u4e0d\u56de\u6536");
        }
    }

    private void recycleNumberOperation(String billNoFldKey, String entityNum, List<CodeRuleInfo> codeRuleList, List<DynamicObject> toRecycleObjs) {
        for (DynamicObject dynamicObject : toRecycleObjs) {
            boolean isExist;
            CodeRuleInfo codeRuleInfo = this.checkCodeRule(entityNum, codeRuleList, dynamicObject);
            if (codeRuleInfo == null) continue;
            boolean isEdit = dynamicObject.getDataEntityState().getFromDatabase();
            if (isEdit && CodeRuleOpStatusRecord.ResultStatus.NEW_NUMBER != this.statusRecord.getDataResultStatus(dynamicObject) && CodeRuleOpStatusRecord.ResultStatus.NEW_NUMBER_UNIQUE != this.statusRecord.getDataResultStatus(dynamicObject)) {
                logger.info("CodeRuleServiceImp.recycleNumber(): \u52a8\u6001\u5bf9\u8c61\u6765\u81ea\u6570\u636e\u5e93, \u4fdd\u5b58/\u63d0\u4ea4\u5931\u8d25, \u4e0d\u56de\u6536\u7f16\u7801");
                continue;
            }
            CodeRuleOpStatusRecord.BillNoStatus billNoStatus = this.statusRecord.getBillNoStatus(dynamicObject);
            if (CodeRuleOpStatusRecord.BillNoStatus.IS_USER_INPUT == billNoStatus || CodeRuleOpStatusRecord.BillNoStatus.IS_EXCEL_INPUT == billNoStatus) {
                logger.info("[CodeRuleOp.recycleNumber]\uff1a\u7f16\u53f7\u4e3a\u7528\u6237\u624b\u52a8\u4fee\u6539\u6216excel\u4e2d\u586b\u5199\uff0c\u4e0d\u8fdb\u884c\u56de\u6536");
                continue;
            }
            String number = dynamicObject.getString(billNoFldKey);
            if (this.isOpenForOnlyNumber(billNoFldKey, codeRuleInfo, dynamicObject) && (isExist = this.uniqueBillno.checkReatedInDB(entityNum, this.getQFilterExistUniqueBillNo(billNoFldKey, number, dynamicObject)))) continue;
            logger.info(String.format("CodeRuleServiceImp.recycleNumber(): entityId: %s , dataInfo: %s ,  orgID: %s , number: %s ", entityNum, dynamicObject != null ? dynamicObject.getPkValue() : null, null, number));
            this.executeRecycleProcess(codeRuleInfo, dynamicObject, number);
        }
    }

    private void executeRecycleProcess(CodeRuleInfo codeRuleInfo, DynamicObject dataInfo, String number) {
        if (null == this.numberModes) {
            return;
        }
        for (CodeRuleNumberMode numberMode : this.numberModes) {
            numberMode.recycleNumber(codeRuleInfo, dataInfo, number);
        }
    }

    protected CodeRuleInfo checkCodeRule(String entityNum, List<CodeRuleInfo> codeRuleList, DynamicObject dynamicObject) {
        Long userOrgId = this.codeRuleServiceImp.getUserOrgId(dynamicObject, null);
        if (codeRuleList != null && !codeRuleList.isEmpty()) {
            CodeRuleInfo codeRuleInfo = this.codeRuleServiceImp.getUsableCodeRuleId(codeRuleList, entityNum, userOrgId, dynamicObject);
            if (codeRuleInfo != null) {
                codeRuleInfo.setEntityId(entityNum);
                codeRuleInfo.setOrgId(userOrgId.longValue());
            }
            return codeRuleInfo;
        }
        return null;
    }

    private Set<String> validateNumbersInBusinessDB(String entityNum, String billNoFldKey, List<DynamicObject> toRecycleObjs) {
        if (CollectionUtils.isEmpty(toRecycleObjs)) {
            return Collections.emptySet();
        }
        LinkedHashMap<String, DynamicObject> needCheckMap = new LinkedHashMap<String, DynamicObject>();
        for (DynamicObject value : toRecycleObjs) {
            if (!this.isOpenForOnlyNumber(billNoFldKey, this.getRecycleCodeRuleInfo(value), value)) continue;
            needCheckMap.put(value.getString(billNoFldKey), value);
        }
        Map repeatDateInDB = this.uniqueBillno.checkRepeatDateInDB(entityNum, billNoFldKey, needCheckMap, this.getQFilterExistsUniqueBillNo(billNoFldKey, needCheckMap));
        if (repeatDateInDB == null || repeatDateInDB.isEmpty()) {
            return Collections.emptySet();
        }
        return repeatDateInDB.keySet();
    }

    public void testJunit() {
    }
}
