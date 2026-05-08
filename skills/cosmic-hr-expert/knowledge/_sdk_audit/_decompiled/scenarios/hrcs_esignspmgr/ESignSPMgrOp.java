/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.hbp.common.cache.HRAppCache
 *  kd.hr.hbp.common.cache.IHRAppCache
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignDBServiceServiceHelper
 *  kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper
 *  org.apache.commons.collections4.CollectionUtils
 */
package kd.hr.hrcs.opplugin.web.esign;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.common.cache.HRAppCache;
import kd.hr.hbp.common.cache.IHRAppCache;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignDBServiceServiceHelper;
import kd.hr.hrcs.bussiness.service.esign.util.HRCSESignSPMgrServiceHelper;
import org.apache.commons.collections4.CollectionUtils;

public final class ESignSPMgrOp
extends HRDataBaseOp {
    private static final int CACHE_TIMEOUT = 1440;

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        String operationKey = e.getOperationKey();
        if (HRStringUtils.equals((String)operationKey, (String)"save")) {
            DynamicObject[] dys = e.getDataEntities();
            DynamicObject dataDyn = dys[0];
            if (dataDyn.getDataEntityState().getFromDatabase()) {
                DynamicObjectCollection entryAppCfgDys;
                HashSet<Long> curSaveEntryIds;
                DynamicObjectCollection entryEntity = dataDyn.getDynamicObjectCollection("entryentity1");
                if (entryEntity != null && entryEntity.size() > 0) {
                    curSaveEntryIds = new HashSet(entryEntity.size());
                    for (DynamicObject appEntryDyn : entryEntity) {
                        DynamicObject appDyn = appEntryDyn.getDynamicObject("bdesignappcfg");
                        long spId = appDyn.getLong("id");
                        curSaveEntryIds.add(spId);
                    }
                } else {
                    curSaveEntryIds = new HashSet<Long>(0);
                }
                if ((entryAppCfgDys = HRCSESignSPMgrServiceHelper.getESignSPAppInfos((Object)dataDyn.getPkValue())) != null && entryAppCfgDys.size() > 0) {
                    Set delEntryIds;
                    Set dbEntryIds = entryAppCfgDys.stream().map(item -> item.getDynamicObject("bdesignappcfg").getLong("id")).collect(Collectors.toSet());
                    Set set = delEntryIds = CollectionUtils.isNotEmpty(curSaveEntryIds) ? dbEntryIds.stream().filter(item -> !curSaveEntryIds.contains(item)).collect(Collectors.toSet()) : dbEntryIds;
                    if (CollectionUtils.isNotEmpty(delEntryIds)) {
                        HRCSESignDBServiceServiceHelper.eSignAppCfgService.delete(delEntryIds.toArray());
                    }
                }
            } else {
                dataDyn.set("enable", (Object)"0");
                DynamicObjectCollection entryEntity = dataDyn.getDynamicObjectCollection("entryentity1");
                if (entryEntity != null && entryEntity.size() > 0) {
                    DynamicObject[] appDyns = HRCSESignDBServiceServiceHelper.eSignAppCfgService.loadDynamicObjectArray((Object[])entryEntity.stream().map(item -> item.getDynamicObject("bdesignappcfg").get("id")).distinct().toArray(Object[]::new));
                    Arrays.stream(appDyns).forEach(item -> item.set("esignsp", dataDyn.get("id")));
                    HRCSESignDBServiceServiceHelper.eSignAppCfgService.update(appDyns);
                }
            }
        }
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        DynamicObject[] dys;
        DynamicObject dataDyn;
        DynamicObjectCollection entryEntity;
        super.afterExecuteOperationTransaction(e);
        String operationKey = e.getOperationKey();
        if (HRStringUtils.equals((String)operationKey, (String)"save") && (entryEntity = (dataDyn = (dys = e.getDataEntities())[0]).getDynamicObjectCollection("entryentity1")) != null && entryEntity.size() > 0) {
            IHRAppCache ihrAppCache = HRAppCache.get((String)"hrcs");
            Long spId = dataDyn.getLong("id");
            DynamicObject[] appDyns = HRCSESignDBServiceServiceHelper.eSignAppCfgService.loadDynamicObjectArray((Object[])entryEntity.stream().map(item -> item.getDynamicObject("bdesignappcfg").get("id")).distinct().toArray(Object[]::new));
            Arrays.stream(appDyns).forEach(item -> {
                long appUpdTime = item.getDate("modifytime").getTime();
                String cacheKey = spId + ":" + item.getString("corporate.id");
                ihrAppCache.put(cacheKey, (Object)appUpdTime, 1440);
            });
        }
    }
}
