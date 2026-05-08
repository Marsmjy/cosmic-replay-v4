/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hdm.common.transfer.util.TransferPageHelperUtil
 *  kd.hr.hdm.opplugin.transfer.TransferAfterEffectOp$BizStatusSyncPerChgHandler
 *  kd.hr.hdm.opplugin.transfer.TransferAfterEffectOp$StaffSyncPerChgHandler
 *  kd.hr.hdm.opplugin.transfer.TransferAfterEffectOp$VidSyncPerChgHandler
 *  kd.sdk.hr.hpfs.common.enums.PerChgStatusEnum
 *  kd.sdk.hr.hpfs.utils.ChgDLockUtils
 *  kd.sdk.hr.hpfs.utils.PerChgStatusUtils
 */
package kd.hr.hdm.opplugin.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hdm.common.transfer.util.TransferPageHelperUtil;
import kd.hr.hdm.opplugin.transfer.TransferAfterEffectOp;
import kd.sdk.hr.hpfs.common.enums.PerChgStatusEnum;
import kd.sdk.hr.hpfs.utils.ChgDLockUtils;
import kd.sdk.hr.hpfs.utils.PerChgStatusUtils;

public final class TransferAfterEffectOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(TransferAfterEffectOp.class);
    private static final Set<String> SUCCESS_PER_CHG_STATUS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(PerChgStatusEnum.SYNC_STATUS_CHG_S.getCode(), PerChgStatusEnum.SYNC_STAFF_MSG_S.getCode(), PerChgStatusEnum.SYNC_ORG_VER_CHG_S.getCode())));

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        evt.getFieldKeys().addAll(TransferPageHelperUtil.getInstance().getSingleAllFieldsList());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        LOGGER.info("TransferAfterEffectOp beginOperationTransaction start");
        DynamicObject[] dataEntities = args.getDataEntities();
        int length = dataEntities.length;
        if (length == 0) {
            return;
        }
        ArrayList staffUnSuccessBills = new ArrayList(length);
        ArrayList vidUnSuccessBills = new ArrayList(length);
        ArrayList statusUnSuccessBills = new ArrayList(length);
        ArrayList unSuccessBills = new ArrayList(length);
        Arrays.stream(dataEntities).forEach(dataEntity -> {
            List perChgStatusList = PerChgStatusUtils.getStatus((DynamicObject)dataEntity);
            if (perChgStatusList.contains(PerChgStatusEnum.SUCCESS.getCode())) {
                return;
            }
            unSuccessBills.add(dataEntity);
            if (!perChgStatusList.contains(PerChgStatusEnum.SYNC_STATUS_CHG_S.getCode())) {
                statusUnSuccessBills.add(dataEntity);
            }
            if (perChgStatusList.contains(PerChgStatusEnum.SMALLEST_SUCCESS.getCode())) {
                if (!perChgStatusList.contains(PerChgStatusEnum.SYNC_STAFF_MSG_S.getCode())) {
                    staffUnSuccessBills.add(dataEntity);
                }
                if (!perChgStatusList.contains(PerChgStatusEnum.SYNC_ORG_VER_CHG_S.getCode())) {
                    vidUnSuccessBills.add(dataEntity);
                }
            }
        });
        if (CollectionUtils.isEmpty(unSuccessBills)) {
            LOGGER.warn("unSuccessBills is empty");
            return;
        }
        Map dLockMap = ChgDLockUtils.lock(unSuccessBills);
        if (dLockMap.isEmpty()) {
            LOGGER.warn("dLockMap is empty");
            unSuccessBills.forEach(bill -> bill.set("transfereffectstatus", (Object)"3"));
            return;
        }
        Set lockIds = dLockMap.keySet();
        LOGGER.info("lockIds:{}", lockIds);
        try {
            new BizStatusSyncPerChgHandler(null).sync(ChgDLockUtils.getLockedBills(statusUnSuccessBills, lockIds), new Object[0]);
            new StaffSyncPerChgHandler(null).sync(ChgDLockUtils.getLockedBills(staffUnSuccessBills, lockIds), new Object[0]);
            new VidSyncPerChgHandler(null).sync(ChgDLockUtils.getLockedBills(vidUnSuccessBills, lockIds), new Object[0]);
            List lockedBills = ChgDLockUtils.getLockedBills(unSuccessBills, lockIds);
            PerChgStatusUtils.tryUpdatePerChgStatusSuccess((List)lockedBills, SUCCESS_PER_CHG_STATUS);
            lockedBills.forEach(dataEntity -> {
                List chgStatus = PerChgStatusUtils.getStatus((DynamicObject)dataEntity);
                if (chgStatus.contains(PerChgStatusEnum.SUCCESS.getCode())) {
                    dataEntity.set("transfereffectstatus", (Object)"2");
                } else {
                    dataEntity.set("transfereffectstatus", (Object)"3");
                }
            });
        }
        finally {
            ChgDLockUtils.unlock((Map)dLockMap);
        }
        LOGGER.info("TransferAfterEffectOp beginOperationTransaction end");
    }
}
