/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.resource.ResManager
 *  kd.bos.dataentity.serialization.SerializationUtils
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.operate.result.OperateErrorInfo
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.entity.validate.ErrorLevel
 *  kd.bos.entity.validate.ValidationErrorInfo
 *  kd.bos.exception.KDBizException
 *  kd.bos.id.ID
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.service.operation.validate.BaseDataDeleteValidator
 *  kd.bos.servicehelper.operation.SaveServiceHelper
 *  kd.hr.hbp.business.domain.model.timeline.TimelineChangeIdInfo
 *  kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult
 *  kd.hr.hbp.business.service.timeline.TimelineService
 *  kd.hr.hbp.business.service.timeline.dao.TimelineEntityConf
 *  kd.hr.hbp.business.service.timeline.util.TimelineAttachmentUtil
 *  kd.hr.hbp.common.constants.timeline.TimelineConstants
 *  kd.hr.hbp.common.constants.timeline.TimelineModelTypeEnum
 *  kd.hr.hbp.common.model.history.HisAttachmentBo
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hbp.opplugin.web.timeline.TimelineHandlerStrategy
 *  kd.hr.hbp.opplugin.web.timeline.TimelineHandlerStrategyFactory
 *  kd.hr.hbp.opplugin.web.timeline.validator.TimelineValidator
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.opplugin.web.timeline;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.operate.result.OperateErrorInfo;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.entity.validate.ErrorLevel;
import kd.bos.entity.validate.ValidationErrorInfo;
import kd.bos.exception.KDBizException;
import kd.bos.id.ID;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.service.operation.validate.BaseDataDeleteValidator;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.hr.hbp.business.domain.model.timeline.TimelineChangeIdInfo;
import kd.hr.hbp.business.domain.model.timeline.TimelineHandlerResult;
import kd.hr.hbp.business.service.timeline.TimelineService;
import kd.hr.hbp.business.service.timeline.dao.TimelineEntityConf;
import kd.hr.hbp.business.service.timeline.util.TimelineAttachmentUtil;
import kd.hr.hbp.common.constants.timeline.TimelineConstants;
import kd.hr.hbp.common.constants.timeline.TimelineModelTypeEnum;
import kd.hr.hbp.common.model.history.HisAttachmentBo;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hbp.opplugin.web.timeline.TimelineHandlerStrategy;
import kd.hr.hbp.opplugin.web.timeline.TimelineHandlerStrategyFactory;
import kd.hr.hbp.opplugin.web.timeline.validator.TimelineValidator;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public final class TimelineTplOp
extends HRDataBaseOp
implements TimelineConstants {
    private static final Log LOGGER = LogFactory.getLog(TimelineTplOp.class);
    private TimelineEntityConf entityConfig = null;

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new TimelineValidator());
        TimelineEntityConf entityConfig = this.getEntityConfig();
        if (entityConfig.getLogicDelete().booleanValue()) {
            args.getValidators().removeIf(abstractValidator -> abstractValidator instanceof BaseDataDeleteValidator);
        }
    }

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        TimelineEntityConf entityConfig = this.getEntityConfig();
        args.getFieldKeys().add("startdate");
        args.getFieldKeys().add("enddate");
        args.getFieldKeys().add("iscurrentdata");
        args.getFieldKeys().addAll(entityConfig.getLogicKey());
        if (entityConfig.getLogicDelete().booleanValue()) {
            args.getFieldKeys().add("isdeleted");
        }
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        super.beginOperationTransaction(args);
        String operationKey = args.getOperationKey();
        DynamicObject[] dataEntities = args.getDataEntities();
        TimelineEntityConf entityConfig = this.getEntityConfig();
        TimelineHandlerStrategy strategy = TimelineHandlerStrategyFactory.getStrategy((TimelineModelTypeEnum)entityConfig.getModelTypeEnum());
        long logEventId = ID.genLongId();
        this.getOption().setVariableValue("eventId", String.valueOf(logEventId));
        switch (operationKey) {
            case "save": {
                this.saveData(dataEntities, strategy, entityConfig, logEventId, args);
                break;
            }
            case "delete": {
                TimelineHandlerResult deleteResult;
                try {
                    deleteResult = strategy.delete(Arrays.asList(dataEntities), entityConfig, Long.valueOf(logEventId));
                }
                catch (Exception exception) {
                    LOGGER.error((Throwable)exception);
                    throw new KDBizException(ResManager.loadKDString((String)"\u5220\u9664\u5931\u8d25\u3002", (String)"TimelineTplOp_2", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
                }
                if (entityConfig.getLogicDelete().booleanValue()) {
                    SaveServiceHelper.save((DynamicObject[])dataEntities);
                    args.setCancelOperation(true);
                }
                this.setChangeInfoToOpParam(deleteResult);
                break;
            }
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        super.afterExecuteOperationTransaction(args);
        String attachmentsBoStr = this.operateOption.getVariableValue("attachmentsBos", "");
        if (HRStringUtils.isNotEmpty((String)attachmentsBoStr)) {
            try (TXHandle tx = TX.requiresNew();){
                try {
                    List hisAttachmentBos = JSON.parseArray((String)attachmentsBoStr, HisAttachmentBo.class);
                    long startTime = System.currentTimeMillis();
                    TimelineAttachmentUtil.saveAttachments((List)hisAttachmentBos);
                    LOGGER.info("TimelineTplOp saveAttachments cost: {} ms", (Object)(System.currentTimeMillis() - startTime));
                }
                catch (Exception e) {
                    LOGGER.error("TimelineTplOp save attachment failed.", (Throwable)e);
                    tx.markRollback();
                }
            }
        }
    }

    private void saveData(DynamicObject[] dataEntities, TimelineHandlerStrategy strategy, TimelineEntityConf entityConfig, long logEventId, BeginOperationTransactionArgs args) {
        List attachmentBos;
        TimelineHandlerResult saveResult;
        long startTime = System.currentTimeMillis();
        try {
            saveResult = strategy.save(Arrays.stream(dataEntities).collect(Collectors.toList()), entityConfig, Long.valueOf(logEventId));
        }
        catch (Exception exception) {
            LOGGER.error((Throwable)exception);
            throw new KDBizException(ResManager.loadKDString((String)"\u4fdd\u5b58\u5931\u8d25\u3002", (String)"TimelineTplOp_1", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
        }
        LOGGER.info("TimeLineTplOp saveData cost: {} ms.", (Object)(System.currentTimeMillis() - startTime));
        this.addErrorInfo(saveResult, dataEntities, args);
        List<DynamicObject> dataEntityList = Arrays.stream(dataEntities).collect(Collectors.toList());
        boolean hasCoveredData = dataEntityList.removeIf(data -> {
            if (saveResult.getRemoveIds().contains(data.getLong("id"))) {
                this.addError(data.getLong("id"), ResManager.loadKDString((String)"\u8be5\u6570\u636e\u5df2\u88ab\u540c\u6279\u6b21\u7684\u5176\u4ed6\u6570\u636e\u8986\u76d6\u3002", (String)"TimelineTplOp_3", (String)"hrmp-hbp-opplugin", (Object[])new Object[0]));
                return true;
            }
            return false;
        });
        if (hasCoveredData) {
            args.setDataEntities(dataEntityList.toArray(new DynamicObject[0]));
        }
        if (!(attachmentBos = saveResult.getAttachmentBos()).isEmpty()) {
            this.operateOption.setVariableValue("attachmentsBos", SerializationUtils.toJsonString((Object)attachmentBos));
        }
        this.setChangeInfoToOpParam(saveResult);
    }

    private void addErrorInfo(TimelineHandlerResult result, DynamicObject[] dataEntities, BeginOperationTransactionArgs args) {
        if (result.hasError()) {
            ArrayList errorDyIds = Lists.newArrayListWithExpectedSize((int)result.getFailDataList().size());
            for (int i = 0; i < result.getFailDataList().size(); ++i) {
                DynamicObject errorDy = (DynamicObject)result.getFailDataList().get(i);
                this.addError(errorDy.getLong("id"), (String)result.getErrorMsgList().get(i));
                errorDyIds.add(errorDy.getLong("id"));
            }
            List<DynamicObject> dataEntityList = Arrays.stream(dataEntities).collect(Collectors.toList());
            if (dataEntityList.removeIf(data -> errorDyIds.contains(data.getLong("id")))) {
                args.setDataEntities(dataEntityList.toArray(new DynamicObject[0]));
            }
        }
    }

    private void addError(long errorId, String errorMsg) {
        ValidationErrorInfo operateErrorInfo = new ValidationErrorInfo();
        operateErrorInfo.setPkValue((Object)errorId);
        operateErrorInfo.setErrorLevel(ErrorLevel.Error.toString());
        operateErrorInfo.setMessage(errorMsg);
        this.getOperationResult().addErrorInfo((OperateErrorInfo)operateErrorInfo);
    }

    private void setChangeInfoToOpParam(TimelineHandlerResult result) {
        TimelineChangeIdInfo changeInfo = new TimelineChangeIdInfo();
        changeInfo.setSaveDataIds(result.getNewOrModifyDataList().stream().map(dy -> dy.getLong("id")).collect(Collectors.toList()));
        changeInfo.setRemoveDataIds(result.getRemoveIds());
        this.getOption().setVariableValue("timeLineOpChangeInfo", SerializationUtils.toJsonString((Object)changeInfo));
    }

    private TimelineEntityConf getEntityConfig() {
        if (this.entityConfig == null) {
            String entityNumber = this.billEntityType.getName();
            TimelineService iTimeLineService = TimelineService.getInstance();
            long startTime = System.currentTimeMillis();
            this.entityConfig = iTimeLineService.getEntityConfWithException(entityNumber);
            LOGGER.info("TimeLineTplOp getEntityConfig cost: {} ms.", (Object)(System.currentTimeMillis() - startTime));
        }
        return this.entityConfig;
    }
}
