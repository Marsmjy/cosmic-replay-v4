/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.OperateOption
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.operate.IOperationResult
 *  kd.bos.entity.operate.result.OperateErrorInfo
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.ReturnOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.entity.validate.ErrorLevel
 *  kd.bos.entity.validate.ValidatePriority
 *  kd.bos.entity.validate.ValidationErrorInfo
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.service.history.HisModelCommonService
 *  kd.hr.hbp.business.service.history.core.HisModelAttachmentService
 *  kd.hr.hbp.business.service.history.util.HisModelCommonUtil
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.constants.history.HisModelConstants
 *  kd.hr.hbp.common.constants.history.HisModelTypeEnum
 *  kd.hr.hbp.common.model.history.HisModelAttachInfo
 *  kd.hr.hbp.common.model.history.HisModelEntityConfig
 *  kd.hr.hbp.common.model.history.HisVersionChangeIdInfo
 *  kd.hr.hbp.common.model.history.param.HisBaseReturnData
 *  kd.hr.hbp.common.model.history.param.HisModelOPParam
 *  kd.hr.hbp.common.model.history.param.HisVersionChangeInfo
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.validator.HRDataBaseConfigValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hbp.opplugin.web.history.handler.HisModelOPHandlerFactory
 *  kd.hr.hbp.opplugin.web.history.handler.IHisModelOPHandler
 *  kd.hr.hbp.opplugin.web.history.validator.HisModelChangeOPValidator
 *  kd.hr.hbp.opplugin.web.history.validator.HisModelDeleteOPValidator
 *  kd.hr.hbp.opplugin.web.history.validator.HisModelDisableOpValidator
 *  kd.hr.hbp.opplugin.web.history.validator.HisModelImportOPValidator
 *  kd.hr.hbp.opplugin.web.history.validator.HisModelOPCommonValidator
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.opplugin.web.history;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.operate.IOperationResult;
import kd.bos.entity.operate.result.OperateErrorInfo;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.ReturnOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.entity.validate.ErrorLevel;
import kd.bos.entity.validate.ValidatePriority;
import kd.bos.entity.validate.ValidationErrorInfo;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.service.history.HisModelCommonService;
import kd.hr.hbp.business.service.history.core.HisModelAttachmentService;
import kd.hr.hbp.business.service.history.util.HisModelCommonUtil;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.common.constants.history.HisModelTypeEnum;
import kd.hr.hbp.common.model.history.HisModelAttachInfo;
import kd.hr.hbp.common.model.history.HisModelEntityConfig;
import kd.hr.hbp.common.model.history.HisVersionChangeIdInfo;
import kd.hr.hbp.common.model.history.param.HisBaseReturnData;
import kd.hr.hbp.common.model.history.param.HisModelOPParam;
import kd.hr.hbp.common.model.history.param.HisVersionChangeInfo;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.validator.HRDataBaseConfigValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hbp.opplugin.web.history.handler.HisModelOPHandlerFactory;
import kd.hr.hbp.opplugin.web.history.handler.IHisModelOPHandler;
import kd.hr.hbp.opplugin.web.history.validator.HisModelChangeOPValidator;
import kd.hr.hbp.opplugin.web.history.validator.HisModelDeleteOPValidator;
import kd.hr.hbp.opplugin.web.history.validator.HisModelDisableOpValidator;
import kd.hr.hbp.opplugin.web.history.validator.HisModelImportOPValidator;
import kd.hr.hbp.opplugin.web.history.validator.HisModelOPCommonValidator;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public class HisModelOPCommonPlugin
extends HRDataBaseOp
implements HisModelConstants {
    private static final Log LOGGER = LogFactory.getLog(HisModelOPCommonPlugin.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List hisFields = Stream.of("boid", "iscurrentversion", "bsed", "bsled", "firstbsed", "datastatus", "sourcevid", "entryboid", "hisversion", "status", "enable", "creator", "createtime", "modifier", "modifytime").collect(Collectors.toList());
        args.getFieldKeys().addAll(hisFields);
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        HisModelImportOPValidator importValidator = new HisModelImportOPValidator();
        importValidator.setValidatePriority(ValidatePriority.First);
        args.addValidator((AbstractValidator)importValidator);
        HisModelChangeOPValidator changeOPValidator = new HisModelChangeOPValidator();
        changeOPValidator.setValidatePriority(ValidatePriority.First);
        args.addValidator((AbstractValidator)changeOPValidator);
        args.addValidator((AbstractValidator)new HisModelOPCommonValidator());
        args.addValidator((AbstractValidator)new HisModelDisableOpValidator());
        args.addValidator((AbstractValidator)new HisModelDeleteOPValidator());
        this.removeBaseDataDeleteValidator(args);
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);
        if (this.getOption().containsVariable("importtype")) {
            List pks = Arrays.stream(args.getDataEntities()).map(data -> data.get("id")).collect(Collectors.toList());
            this.getOption().setVariableValue("hisImportPKs", SerializationUtils.toJsonString(pks));
        }
        switch (args.getOperationKey()) {
            case "save": 
            case "confirmchange": {
                this.clearPkForUpdateImport(args.getDataEntities());
            }
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        DynamicObject[] dataCol = args.getDataEntities();
        if (dataCol.length == 0) {
            return;
        }
        this.loadDataForListOperate(args);
        String entityNumber = dataCol[0].getDataEntityType().getName();
        HisModelOPParam hisModelOPParam = this.getHisModelOPParam();
        IHisModelOPHandler handler = HisModelOPHandlerFactory.getHisModelOPHandler((String)entityNumber, (HisModelOPParam)hisModelOPParam, (OperateOption)this.getOption());
        HisBaseReturnData returnData = new HisBaseReturnData();
        switch (args.getOperationKey()) {
            case "save": {
                if (hisModelOPParam.isReviseSave()) {
                    returnData = handler.revise(args);
                    break;
                }
                returnData = handler.save(args);
                break;
            }
            case "confirmchange": {
                returnData = handler.confirmChange(args);
                break;
            }
            case "disable": {
                this.resetEnable(args.getDataEntities());
                returnData = handler.disable(args);
                break;
            }
            case "enable": {
                returnData = handler.enable(args);
                break;
            }
            case "submit": {
                returnData = handler.save(args);
                break;
            }
            case "audit": {
                returnData = handler.audit(args);
                break;
            }
            case "delete": {
                returnData = handler.delete(args);
                break;
            }
        }
        this.addErrorInfo(returnData, dataCol, args);
        HisVersionChangeInfo versionChangeInfo = returnData.getVersionChangeInfo();
        HisVersionChangeIdInfo versionChangeIdInfo = new HisVersionChangeIdInfo();
        versionChangeIdInfo.setAddVersionIds(versionChangeInfo.getAddVersionIds());
        versionChangeIdInfo.setUpdateVersionIds(versionChangeInfo.getUpdateVersionIds());
        versionChangeIdInfo.setCoverVersionIds(versionChangeInfo.getCoverVersionIds());
        this.getOption().setVariableValue("hisVersionChangeInfo", SerializationUtils.toJsonString((Object)versionChangeIdInfo));
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        String attachmentInfoStr = this.getOption().getVariableValue("hisModelAttachmentInfo", "");
        if (HRStringUtils.isNotEmpty((String)attachmentInfoStr) && args.getDataEntities().length > 0) {
            try (TXHandle tx = TX.requiresNew();){
                try {
                    String entityNumber = args.getDataEntities()[0].getDataEntityType().getName();
                    List attachInfos = SerializationUtils.fromJsonStringToList((String)attachmentInfoStr, HisModelAttachInfo.class);
                    HisModelAttachmentService.getInstance().saveAttachments(entityNumber, attachInfos);
                }
                catch (Exception e) {
                    LOGGER.error("HisModelOPCommonPlugin save attachment failed.", (Throwable)e);
                    tx.markRollback();
                }
            }
        }
    }

    public void onReturnOperation(ReturnOperationArgs args) {
        super.onReturnOperation(args);
        IOperationResult operationResult = args.getOperationResult();
        if (!this.getOption().containsVariable("importtype")) {
            return;
        }
        String importType = this.getOption().getVariableValue("importtype");
        if (!HRStringUtils.equals((String)importType, (String)"new") && operationResult.getSuccessPkIds() != null && !operationResult.getSuccessPkIds().isEmpty()) {
            String currentPkStr = this.getOption().getVariableValue("hisImportPKs", null);
            if (HRStringUtils.isEmpty((String)currentPkStr)) {
                return;
            }
            List currentPks = (List)SerializationUtils.fromJsonString((String)currentPkStr, List.class);
            currentPks = currentPks.stream().map(id -> Long.valueOf(id.toString())).collect(Collectors.toList());
            operationResult.getSuccessPkIds().addAll(currentPks);
        }
    }

    private void clearPkForUpdateImport(DynamicObject[] dataCol) {
        if (!this.getOption().containsVariable("importtype")) {
            return;
        }
        String importType = this.getOption().getVariableValue("importtype");
        if (HRStringUtils.equals((String)importType, (String)"new")) {
            return;
        }
        for (DynamicObject data : dataCol) {
            if (data.getBoolean("iscurrentversion")) continue;
            data.set("id", (Object)0L);
        }
    }

    private void loadDataForListOperate(BeginOperationTransactionArgs args) {
        String operationKey = args.getOperationKey();
        String fromListOperate = this.getOption().getVariableValue("fromListOperate", "");
        if (!(HRStringUtils.equals((String)fromListOperate, (String)"true") || "disable".equals(operationKey) || "enable".equals(operationKey))) {
            return;
        }
        if ("confirmchange".equals(operationKey) || "disable".equals(operationKey) || "enable".equals(operationKey) || "audit".equals(operationKey)) {
            HRBaseServiceHelper helper = new HRBaseServiceHelper(args.getDataEntities()[0].getDataEntityType().getName());
            List ids = Arrays.stream(args.getDataEntities()).map(dy -> dy.getLong("id")).collect(Collectors.toList());
            DynamicObject[] loadDys = helper.loadDynamicObjectArray(new QFilter[]{new QFilter("id", "in", ids)});
            switch (operationKey) {
                case "enable": {
                    Arrays.stream(loadDys).forEach(dy -> dy.set("enable", (Object)"1"));
                    break;
                }
                case "disable": {
                    HisModelEntityConfig hisModelEntityConfig = HisModelCommonService.getInstance().getHisModelEntityConfig(this.billEntityType.getName());
                    if (hisModelEntityConfig.getModelType() == HisModelTypeEnum.NO_INTERRUPTION_NO_OVERLAP) {
                        HisModelOPParam hisModelOPParam = this.getHisModelOPParam();
                        Date disableDate = hisModelOPParam.getDisableDate();
                        if (HisModelCommonUtil.removeDateHMS((Date)disableDate).getTime() > HisModelCommonUtil.getToday().getTime()) break;
                        Arrays.stream(loadDys).forEach(dy -> dy.set("enable", (Object)"0"));
                        break;
                    }
                    Arrays.stream(loadDys).forEach(dy -> dy.set("enable", (Object)"0"));
                    break;
                }
                case "audit": {
                    Arrays.stream(loadDys).forEach(dy -> dy.set("status", (Object)"C"));
                    break;
                }
            }
            args.setDataEntities(loadDys);
        }
    }

    private void resetEnable(DynamicObject[] dataEntities) {
        String dataEnableMapStr = this.getOption().getVariableValue("dataEnableMap", "");
        if (HRStringUtils.isEmpty((String)dataEnableMapStr)) {
            return;
        }
        Map dataEnableMap = (Map)SerializationUtils.fromJsonString((String)dataEnableMapStr, Map.class);
        ArrayList saveList = Lists.newArrayListWithExpectedSize((int)dataEntities.length);
        for (DynamicObject dataEntity : dataEntities) {
            String enable = (String)dataEnableMap.get(dataEntity.getString("id"));
            String status = dataEntity.getString("status");
            if (!HRStringUtils.isNotEmpty((String)enable) || !HRStringUtils.equals((String)status, (String)"C")) continue;
            dataEntity.set("enable", (Object)enable);
            saveList.add(dataEntity);
        }
        HRBaseServiceHelper helper = new HRBaseServiceHelper(this.billEntityType.getName());
        helper.save(saveList.toArray(new DynamicObject[0]));
    }

    private void addErrorInfo(HisBaseReturnData returnData, DynamicObject[] dataEntities, BeginOperationTransactionArgs args) {
        if (returnData.hasError()) {
            ArrayList errorDyIds = Lists.newArrayListWithExpectedSize((int)returnData.getFailDataList().size());
            for (int i = 0; i < returnData.getFailDataList().size(); ++i) {
                DynamicObject errorDy = (DynamicObject)returnData.getFailDataList().get(i);
                long errorDataId = errorDy.getLong("id");
                List errors = (List)returnData.getFailMsg().get(errorDataId);
                for (String error : errors) {
                    ValidationErrorInfo operateErrorInfo = new ValidationErrorInfo();
                    operateErrorInfo.setPkValue((Object)errorDataId);
                    operateErrorInfo.setErrorLevel(ErrorLevel.Error.toString());
                    operateErrorInfo.setMessage(error);
                    this.getOperationResult().addErrorInfo((OperateErrorInfo)operateErrorInfo);
                }
                errorDyIds.add(errorDataId);
            }
            List<DynamicObject> dataEntityList = Arrays.stream(dataEntities).collect(Collectors.toList());
            if (dataEntityList.removeIf(data -> errorDyIds.contains(data.getLong("id")))) {
                args.setDataEntities(dataEntityList.toArray(new DynamicObject[0]));
            }
        }
    }

    private void removeBaseDataDeleteValidator(AddValidatorsEventArgs args) {
        HisModelOPParam hisModelOPParam = this.getHisModelOPParam();
        if (hisModelOPParam.isDeleteVersion()) {
            args.getValidators().removeIf(validator -> validator instanceof HRDataBaseConfigValidator);
        }
    }

    private HisModelOPParam getHisModelOPParam() {
        HisModelOPParam hisModelOPParam = new HisModelOPParam();
        String hisOpParamStr = this.getOption().getVariableValue("hisOpParam", "");
        if (HRStringUtils.isNotEmpty((String)hisOpParamStr)) {
            hisModelOPParam = (HisModelOPParam)SerializationUtils.fromJsonString((String)hisOpParamStr, HisModelOPParam.class);
        }
        hisModelOPParam = HisModelOPParam.getHisModelOPParamFromOption((OperateOption)this.getOption(), (HisModelOPParam)hisModelOPParam);
        return hisModelOPParam;
    }
}
