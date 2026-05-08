/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.EntityMetadataCache
 *  kd.bos.entity.MainEntityType
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.util.CollectionUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hom.business.domain.onbrd.OnbrdBillRepository
 *  kd.hr.hom.common.enums.SynchStatusEnum
 *  kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp$BizStatusSyncPerChgHandler
 *  kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp$StaffSyncPerChgHandler
 *  kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp$VidSyncPerChgHandler
 *  kd.sdk.hr.hpfs.common.enums.PerChgStatusEnum
 *  kd.sdk.hr.hpfs.utils.ChgDLockUtils
 *  kd.sdk.hr.hpfs.utils.PerChgStatusUtils
 */
package kd.hr.hom.opplugin.onbrd;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.EntityMetadataCache;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.util.CollectionUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hom.business.domain.onbrd.OnbrdBillRepository;
import kd.hr.hom.common.enums.SynchStatusEnum;
import kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp;
import kd.sdk.hr.hpfs.common.enums.PerChgStatusEnum;
import kd.sdk.hr.hpfs.utils.ChgDLockUtils;
import kd.sdk.hr.hpfs.utils.PerChgStatusUtils;

public final class OnbrdAfterEffectOp
extends HRDataBaseOp {
    private static final Log LOG = LogFactory.getLog(OnbrdAfterEffectOp.class);
    private static final Set<String> SUCCESS_PER_CHG_STATUS = Collections.unmodifiableSet(Sets.newHashSet((Object[])new String[]{PerChgStatusEnum.SMALLEST_SUCCESS.getCode(), PerChgStatusEnum.SYNC_STATUS_CHG_S.getCode(), PerChgStatusEnum.SYNC_STAFF_MSG_S.getCode(), PerChgStatusEnum.SYNC_ORG_VER_CHG_S.getCode(), PerChgStatusEnum.SYNC_ATTMENT_S.getCode()}));

    public void onPreparePropertys(PreparePropertysEventArgs evt) {
        List fieldKeys = evt.getFieldKeys();
        MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType((String)"hom_onbrdbilltpl");
        Map allFields = dataEntityType.getAllFields();
        fieldKeys.addAll(new ArrayList(allFields.keySet()));
        fieldKeys.add("errmsg_tag");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        LOG.info("OnbrdAfterEffectOp beginOperationTransaction start");
        DynamicObject[] dataEntities = args.getDataEntities();
        int length = dataEntities.length;
        if (length == 0) {
            return;
        }
        ArrayList staffUnSuccessBills = new ArrayList(length);
        ArrayList vidUnSuccessBills = new ArrayList(length);
        ArrayList unSuccessBills = new ArrayList(length);
        Arrays.stream(dataEntities).forEach(dataEntity -> {
            List perChgStatusList = PerChgStatusUtils.getStatus((DynamicObject)dataEntity);
            if (perChgStatusList.contains(PerChgStatusEnum.SUCCESS.getCode())) {
                return;
            }
            unSuccessBills.add(dataEntity);
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
            LOG.warn("unSuccessBills is empty dataEntities.length:{}", (Object)length);
            return;
        }
        Map dLockMap = ChgDLockUtils.lock(unSuccessBills);
        if (dLockMap.isEmpty()) {
            LOG.warn("dLockMap is empty unSuccessBills.size:{}", (Object)unSuccessBills.size());
            unSuccessBills.forEach(bill -> bill.set("synchstatus", (Object)SynchStatusEnum.SYNCH_FAIL.getValue()));
            return;
        }
        Set lockIds = dLockMap.keySet();
        LOG.info("lockIds:{}", lockIds);
        try {
            new StaffSyncPerChgHandler(null).sync(ChgDLockUtils.getLockedBills(staffUnSuccessBills, lockIds), new Object[0]);
            new VidSyncPerChgHandler(null).sync(ChgDLockUtils.getLockedBills(vidUnSuccessBills, lockIds), new Object[0]);
        }
        finally {
            ChgDLockUtils.unlock((Map)dLockMap);
        }
        LOG.info("OnbrdAfterEffectOp beginOperationTransaction end");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        LOG.info("OnbrdAfterEffectOp afterExecuteOperationTransaction start");
        DynamicObject[] dataEntities = args.getDataEntities();
        int length = dataEntities.length;
        if (length == 0) {
            return;
        }
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
        });
        if (CollectionUtils.isEmpty(unSuccessBills)) {
            LOG.warn("unSuccessBills is empty dataEntities.length:{}", (Object)length);
            return;
        }
        Map dLockMap = ChgDLockUtils.lock(unSuccessBills);
        if (dLockMap.isEmpty()) {
            LOG.warn("dLockMap is empty unSuccessBills.size:{}", (Object)unSuccessBills.size());
            unSuccessBills.forEach(bill -> bill.set("synchstatus", (Object)SynchStatusEnum.SYNCH_FAIL.getValue()));
            return;
        }
        Set lockIds = dLockMap.keySet();
        LOG.info("lockIds:{}", lockIds);
        try {
            new BizStatusSyncPerChgHandler(null).sync(ChgDLockUtils.getLockedBills(statusUnSuccessBills, lockIds), new Object[0]);
            List lockedBills = ChgDLockUtils.getLockedBills(unSuccessBills, lockIds);
            PerChgStatusUtils.tryUpdatePerChgStatusSuccess((List)lockedBills, SUCCESS_PER_CHG_STATUS);
            lockedBills.forEach(dataEntity -> {
                List chgStatus = PerChgStatusUtils.getStatus((DynamicObject)dataEntity);
                if (chgStatus.contains(PerChgStatusEnum.SUCCESS.getCode())) {
                    dataEntity.set("synchstatus", (Object)SynchStatusEnum.SYNCH_SUCCESS.getValue());
                } else {
                    dataEntity.set("synchstatus", (Object)SynchStatusEnum.SYNCH_FAIL.getValue());
                }
            });
            OnbrdBillRepository.saveOnbrdBillInfos((DynamicObject[])lockedBills.toArray(new DynamicObject[0]));
        }
        finally {
            ChgDLockUtils.unlock((Map)dLockMap);
        }
        LOG.info("OnbrdAfterEffectOp afterExecuteOperationTransaction end");
    }
}
