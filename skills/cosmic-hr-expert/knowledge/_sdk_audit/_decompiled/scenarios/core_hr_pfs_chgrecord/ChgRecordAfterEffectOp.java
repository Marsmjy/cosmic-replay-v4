/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.operate.result.OperationResult
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.ReturnOperationArgs
 *  kd.bos.entity.plugin.args.RollbackOperationArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hpfs.business.application.service.ChgAttachmentService
 *  kd.sdk.hr.hpfs.utils.ChgUtils
 */
package kd.hr.hpfs.opplugin.op.chgrecord;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.ReturnOperationArgs;
import kd.bos.entity.plugin.args.RollbackOperationArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hpfs.business.application.service.ChgAttachmentService;
import kd.sdk.hr.hpfs.utils.ChgUtils;

public final class ChgRecordAfterEffectOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(ChgRecordAfterEffectOp.class);
    private static final String EXCEPTION_BILL_IDS_MAP = "exceptionBillIdsMap";

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

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);
        DynamicObject[] chgRecords = args.getDataEntities();
        int length = chgRecords.length;
        if (length == 0) {
            return;
        }
        List chgRecordIds = Arrays.stream(chgRecords).map(chgRecord -> chgRecord.getLong("id")).collect(Collectors.toList());
        LOG.info("chgRecordIds:{}", chgRecordIds);
        try {
            ChgAttachmentService.getInstance().chgRecordAfterEffectSyncAttachment(Arrays.asList(chgRecords));
        }
        catch (Exception e) {
            LOG.error("ChgRecordAfterEffectOp afterExecuteOperationTransaction error", (Throwable)e);
        }
    }

    public void rollbackOperation(RollbackOperationArgs args) {
        super.rollbackOperation(args);
        DynamicObject[] dataEntities = args.getDataEntitys();
        if (dataEntities.length == 0) {
            return;
        }
        HashMap exceptionBillIdsMap = new HashMap(16);
        Map<String, List<DynamicObject>> groupBySourceNumberChgRecordsMap = Arrays.stream(dataEntities).collect(Collectors.groupingBy(chgRecord -> chgRecord.getString("billsource.number")));
        groupBySourceNumberChgRecordsMap.forEach((billSourceNumber, groupBySourceNumberChgRecords) -> {
            MainEntityType dataEntityType = MetadataServiceHelper.getDataEntityType((String)billSourceNumber);
            Map allFields = dataEntityType.getAllFields();
            if (!allFields.containsKey("perchgstatus")) {
                return;
            }
            List billIds = Arrays.stream(dataEntities).map(chgRecord -> chgRecord.getLong("bill")).collect(Collectors.toList());
            exceptionBillIdsMap.put(billSourceNumber, billIds);
        });
        if (!exceptionBillIdsMap.isEmpty()) {
            this.getOption().setVariableValue(EXCEPTION_BILL_IDS_MAP, JSONObject.toJSONString(exceptionBillIdsMap));
        }
    }

    public void onReturnOperation(ReturnOperationArgs args) {
        super.onReturnOperation(args);
        Map variables = this.getOption().getVariables();
        String exceptionBillIdsMapStr = (String)variables.get(EXCEPTION_BILL_IDS_MAP);
        if (HRStringUtils.isEmpty((String)exceptionBillIdsMapStr)) {
            LOG.info("No exceptionBillIdsMapStr found in variables.");
            return;
        }
        Map exceptionBillIdsMap = (Map)JSONObject.parseObject((String)exceptionBillIdsMapStr, Map.class);
        exceptionBillIdsMap.forEach((billSourceNumber, billIds) -> {
            OperationResult operationResult = ChgUtils.executeOperate((Object[])billIds.toArray(), (String)billSourceNumber, (String)"aftereffect");
            if (!operationResult.isSuccess()) {
                LOG.error("Execute {} aftereffect fail billIds:{},operationResult:{}", new Object[]{billSourceNumber, billIds, operationResult});
            }
        });
    }
}
