/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.business.domain.org.service.AdminChangeMsgService
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.homs.business.service.batcheffect.OrgBillBatchEffectService
 *  kd.hr.homs.common.constants.batchchg.OrgBatchChgBillConstants
 */
package kd.hr.homs.opplugin.web.orgbatch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.business.domain.org.service.AdminChangeMsgService;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.homs.business.service.batcheffect.OrgBillBatchEffectService;
import kd.hr.homs.common.constants.batchchg.OrgBatchChgBillConstants;

public final class OrgBatchChgBillEffectOp
extends HRDataBaseOp {
    private static final Log logger = LogFactory.getLog(OrgBatchChgBillEffectOp.class);
    private OrgBillBatchEffectService billBatchEffectService = new OrgBillBatchEffectService();

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("effdt");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        logger.info("OrgBatchChgBillEffectOp.beginOperationTransaction start.");
        String operationKey = e.getOperationKey();
        DynamicObject[] bills = e.getDataEntities();
        if ("audit".equals(operationKey) || "submiteffect".equals(operationKey)) {
            this.billBatchEffectService.batchEffect(bills);
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        new AdminChangeMsgService().handleChangeMsg();
        String operationKey = e.getOperationKey();
        if ("audit".equals(operationKey) || "submiteffect".equals(operationKey)) {
            DynamicObject[] bills = e.getDataEntities();
            this.billBatchEffectService.afterBatchEffect(bills);
        }
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        super.endOperationTransaction(e);
        DynamicObject[] bills = e.getDataEntities();
        HashSet billIds = Sets.newHashSetWithExpectedSize((int)bills.length);
        for (DynamicObject bill : bills) {
            billIds.add(bill.getLong("id"));
        }
        HRBaseServiceHelper batchOrgEntityHelper = new HRBaseServiceHelper("homs_batchorgentity");
        QFilter adminOrgIdFilter = new QFilter("creator", "=", (Object)0);
        QFilter billIdFilter = new QFilter("billid", "in", (Object)billIds);
        adminOrgIdFilter.and(billIdFilter);
        batchOrgEntityHelper.deleteByFilter(adminOrgIdFilter.toArray());
        QFilter filter = new QFilter("billid", "in", (Object)billIds);
        DynamicObject[] entries = batchOrgEntityHelper.loadDynamicObjectArray(new QFilter[]{filter});
        Map<Long, List<DynamicObject>> changeTypeGroupMap = Arrays.stream(entries).collect(Collectors.groupingBy(dy -> dy.getLong("changetype.id")));
        List<DynamicObject> addOrgDyList = changeTypeGroupMap.get(OrgBatchChgBillConstants.CHANGE_SCENE_ADD);
        if (addOrgDyList != null && !addOrgDyList.isEmpty()) {
            HashMap addOrgBoIdToVidMap = Maps.newHashMapWithExpectedSize((int)addOrgDyList.size());
            for (DynamicObject addOrgDy : addOrgDyList) {
                DynamicObject addOrgBoDy = addOrgDy.getDynamicObject("adminorg");
                addOrgBoIdToVidMap.put(addOrgBoDy.getLong("id"), addOrgBoDy.getLong("sourcevid"));
            }
            ArrayList updateEntryValue = Lists.newArrayListWithExpectedSize((int)10);
            for (Map.Entry<Long, List<DynamicObject>> entry : changeTypeGroupMap.entrySet()) {
                for (DynamicObject entryValue : entry.getValue()) {
                    long adminOrgId;
                    boolean change = false;
                    long parentOrgId = entryValue.getLong("parentorg.id");
                    if (addOrgBoIdToVidMap.containsKey(parentOrgId)) {
                        entryValue.set("parentorg", addOrgBoIdToVidMap.get(parentOrgId));
                        change = true;
                    }
                    if (addOrgBoIdToVidMap.containsKey(adminOrgId = entryValue.getLong("adminorg.id"))) {
                        entryValue.set("adminorg", addOrgBoIdToVidMap.get(adminOrgId));
                        change = true;
                    }
                    if (!change) continue;
                    updateEntryValue.add(entryValue);
                }
            }
            if (!updateEntryValue.isEmpty()) {
                batchOrgEntityHelper.save(updateEntryValue.toArray(new DynamicObject[0]));
            }
        }
        TXHandle txHandle = TXHandle.get();
        logger.info("endOperationTransaction_tx_isRollBack_is_{}", (Object)txHandle.isRollback());
    }
}
