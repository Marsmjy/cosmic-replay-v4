/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  kd.bos.dataentity.entity.DataEntityBase
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.entity.plugin.args.ReturnOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.servicehelper.workflow.EventServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hpfs.business.domain.perchg.ChgRecordRepository
 *  kd.hr.hpfs.business.domain.perchg.ChgRecordService
 *  kd.hr.hpfs.opplugin.validators.OnbrdRevokedValidator
 *  kd.hr.hpfs.opplugin.validators.PerChgTplCrossValidator
 *  kd.hr.hpfs.opplugin.validators.PerChgTplRetryValidator
 *  kd.sdk.hr.hpfs.business.utils.AttachmentUtils
 *  kd.sdk.hr.hpfs.common.enums.PerChgStatusEnum
 *  kd.sdk.hr.hpfs.utils.ChgRecordOpHelper
 *  kd.sdk.hr.hpfs.utils.PerChgNewBillUtils
 *  kd.sdk.hr.hpfs.utils.PerChgStatusUtils
 */
package kd.hr.hpfs.opplugin.op.tpl;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kd.bos.dataentity.entity.DataEntityBase;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.plugin.args.ReturnOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.servicehelper.workflow.EventServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hpfs.business.domain.perchg.ChgRecordRepository;
import kd.hr.hpfs.business.domain.perchg.ChgRecordService;
import kd.hr.hpfs.opplugin.validators.OnbrdRevokedValidator;
import kd.hr.hpfs.opplugin.validators.PerChgTplCrossValidator;
import kd.hr.hpfs.opplugin.validators.PerChgTplRetryValidator;
import kd.sdk.hr.hpfs.business.utils.AttachmentUtils;
import kd.sdk.hr.hpfs.common.enums.PerChgStatusEnum;
import kd.sdk.hr.hpfs.utils.ChgRecordOpHelper;
import kd.sdk.hr.hpfs.utils.PerChgNewBillUtils;
import kd.sdk.hr.hpfs.utils.PerChgStatusUtils;

public final class PerChgTplEffectOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(PerChgTplEffectOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        List fieldKeys = e.getFieldKeys();
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)"hpfs_hrnewbillorgtpl");
        Map allFields = dataEntityType.getAllFields();
        fieldKeys.addAll(new ArrayList(allFields.keySet()));
        fieldKeys.add("errmsg_tag");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new OnbrdRevokedValidator());
        args.getValidators().add(new PerChgTplRetryValidator());
        args.addValidator((AbstractValidator)new PerChgTplCrossValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        long start1 = System.currentTimeMillis();
        String skipSetStatus = this.getOption().getVariableValue("skipSetStatus", "false");
        String autoEffect = this.getOption().getVariableValue("autoEffect", "true");
        LOG.info("skipSetStatus:{},autoEffect:{}", (Object)skipSetStatus, (Object)autoEffect);
        if (HRStringUtils.equals((String)autoEffect, (String)"false")) {
            return;
        }
        int length = args.getDataEntities().length;
        ArrayList<DynamicObject> effectDynamicObjects = new ArrayList<DynamicObject>(length);
        Map futureEffectConfigMap = PerChgNewBillUtils.getFutureEffectConfigMap((DynamicObject[])args.getDataEntities());
        for (DynamicObject dataEntity : args.getDataEntities()) {
            List perChgStatusList = PerChgStatusUtils.getStatus((DynamicObject)dataEntity);
            if (perChgStatusList.contains(PerChgStatusEnum.SUCCESS.getCode())) continue;
            dataEntity.set("errmsg_tag", null);
            if (dataEntity.getDate("b_effectivedate") == null || HRDateTimeUtils.isBeforeOrEqualNow((Date)dataEntity.getDate("b_effectivedate")) || ((Boolean)futureEffectConfigMap.get(dataEntity.getLong("org.id"))).booleanValue()) {
                effectDynamicObjects.add(dataEntity);
                if (CollectionUtils.isEmpty((Collection)perChgStatusList)) {
                    PerChgStatusUtils.setStatusAndMsgByEnum((DynamicObject)dataEntity, (PerChgStatusEnum)PerChgStatusEnum.UNEXECUTED, (boolean)true);
                }
            }
            if (!HRStringUtils.equals((String)"false", (String)skipSetStatus)) continue;
            dataEntity.set("billstatus", (Object)"C");
        }
        LOG.info("Star beginOperationTransaction effectDynamicObjects size: {} with skipSetStatus: {}.", (Object)effectDynamicObjects.size(), (Object)skipSetStatus);
        if (!effectDynamicObjects.isEmpty()) {
            Map defaultChgRecordMap = ChgRecordOpHelper.getChgRecords();
            long start2 = System.currentTimeMillis();
            Map chgRecordMap = ChgRecordService.getInstance().assembleChgRecord(effectDynamicObjects.toArray(new DynamicObject[0]), defaultChgRecordMap);
            LOG.info("####PerChgTplEffectOp.beginOperationTransaction.assembleChgRecord-cost1:{} ms.", (Object)(System.currentTimeMillis() - start2));
            ChgRecordOpHelper.setChgRecords((Map)chgRecordMap);
            Set effectChgRecordIds = chgRecordMap.keySet();
            ChgRecordOpHelper.setEffectChgRecordIds(effectChgRecordIds);
            LOG.info("effectChgRecordIds:{}", effectChgRecordIds);
        } else {
            LOG.info("Ignore all bills because of miss match effective date.");
        }
        LOG.info("####PerChgTplEffectOp.beginOperationTransaction.assembleChgRecordForSubmit-cost1:{} ms.", (Object)(System.currentTimeMillis() - start1));
    }

    public void endOperationTransaction(EndOperationTransactionArgs args) {
        super.endOperationTransaction(args);
        long start1 = System.currentTimeMillis();
        Set effectChgRecordIds = ChgRecordOpHelper.getEffectChgRecordIds();
        if (CollectionUtils.isEmpty((Collection)effectChgRecordIds)) {
            LOG.warn("effectChgRecordIds is empty");
            return;
        }
        LOG.info("effectChgRecordIds:{}", (Object)effectChgRecordIds);
        Map chgRecords = ChgRecordOpHelper.getChgRecords();
        ChgRecordRepository.save((DynamicObject[])chgRecords.values().toArray(new DynamicObject[0]));
        DynamicObject[] dataEntities = args.getDataEntities();
        HRBaseServiceHelper billServiceHelper = new HRBaseServiceHelper(dataEntities[0].getDataEntityType().getName());
        DynamicObject[] bills = billServiceHelper.load(String.join((CharSequence)",", "affrecord", "perchgstatus", "errmsg_tag"), new QFilter[]{new QFilter("id", "in", (Object)Arrays.stream(dataEntities).map(DataEntityBase::getPkValue).toArray())});
        Arrays.stream(bills).forEach(dataEntity -> {
            long affRecordId = dataEntity.getLong("affrecord");
            if (effectChgRecordIds.contains(affRecordId)) {
                PerChgStatusUtils.setStatusAndMsgByEnum((DynamicObject)dataEntity, (PerChgStatusEnum)PerChgStatusEnum.PROGRESS, (boolean)true);
            }
        });
        billServiceHelper.save(bills);
        long start2 = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("entityNumber", (Object)"hpfs_chgrecord");
        jsonObject.put("appId", (Object)"hpfs");
        effectChgRecordIds.forEach(effectChgRecordId -> {
            jsonObject.put("businessKeys", effectChgRecordId);
            EventServiceHelper.triggerEventSubscribe((String)"hpfs_hrnewbillorgtpl.effect", (String)jsonObject.toJSONString());
        });
        LOG.info("####PerChgTplEffectOp.endOperationTransaction.triggerEventSubscribes-cost1:{} ms.", (Object)(System.currentTimeMillis() - start2));
        LOG.info("####PerChgTplEffectOp.endOperationTransaction-cost1:{} ms.", (Object)(System.currentTimeMillis() - start1));
    }

    public void onReturnOperation(ReturnOperationArgs args) {
        ChgRecordOpHelper.clearChgRecords();
        ChgRecordOpHelper.clearEffectChgRecordIds();
        AttachmentUtils.clearAttachmentPanelMap();
    }
}
