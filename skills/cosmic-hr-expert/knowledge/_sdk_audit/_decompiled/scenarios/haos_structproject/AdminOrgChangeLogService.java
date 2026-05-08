/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.metadata.IDataEntityProperty
 *  kd.bos.dataentity.metadata.IDataEntityType
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.hr.haos.common.constants.AdminOrgDetailConstants
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.common.constants.history.HisModelDataStatusEnum
 *  kd.hr.hbp.common.util.HRStringUtils
 */
package kd.hr.haos.business.domain.org.service;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.hr.haos.common.constants.AdminOrgDetailConstants;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.common.constants.history.HisModelDataStatusEnum;
import kd.hr.hbp.common.util.HRStringUtils;

public class AdminOrgChangeLogService {
    private final HRBaseServiceHelper logHelper = new HRBaseServiceHelper("haos_orgchgeffectlog");
    private final HRBaseServiceHelper logEntryHelper = new HRBaseServiceHelper("haos_adminorglogentry");

    public void addAdminOrgChangeLog(DynamicObject[] opData, Map<Long, DynamicObject> oldOrgMap, boolean fromAdd) {
        Map<Long, Long> boIdToNewVidMap;
        IDataEntityType dataEntityType = opData[0].getDataEntityType();
        IDataEntityProperty billIdProperty = (IDataEntityProperty)dataEntityType.getProperties().get((Object)"billid");
        long billId = 0L;
        if (billIdProperty != null) {
            billId = opData[0].getLong("billid");
        }
        List adminorgIds = Arrays.stream(opData).map(dyn -> dyn.getLong("boid")).collect(Collectors.toList());
        HRBaseServiceHelper adminorgQuery = new HRBaseServiceHelper("haos_adminorgdetail");
        if (fromAdd) {
            DynamicObject[] adminorgBos = adminorgQuery.load("id, boid, sourcevid", new QFilter[]{new QFilter("id", "in", adminorgIds)});
            boIdToNewVidMap = Arrays.stream(adminorgBos).collect(Collectors.toMap(dyn -> dyn.getLong("boid"), dyn -> dyn.getLong("sourcevid")));
        } else {
            boIdToNewVidMap = Arrays.stream(opData).collect(Collectors.toMap(dyn -> dyn.getLong("boid"), dyn -> dyn.getLong("id")));
        }
        if (billId != 0L) {
            HRBaseServiceHelper billHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
            DynamicObject batchBillDy = billHelper.loadOne("id, effdt", new QFilter[]{new QFilter("id", "=", (Object)billId)});
            HRBaseServiceHelper billEntryHelper = new HRBaseServiceHelper("homs_batchorgentity");
            QFilter billidFilter = new QFilter("billid", "=", (Object)billId);
            QFilter changeTypeIdFilter = new QFilter("changetype.id", "in", Arrays.asList(AdminOrgDetailConstants.ID_CHANGETYPE_ADD, AdminOrgDetailConstants.ID_CHANGETYPE_PARENT, AdminOrgDetailConstants.ID_CHANGETYPE_INFO, AdminOrgDetailConstants.ID_CHANGETYPE_DISABLE, AdminOrgDetailConstants.ID_CHANGETYPE_ENABLE));
            DynamicObject[] batchBillEntryDy = billEntryHelper.load("id, adminorg.boid", new QFilter[]{billidFilter, changeTypeIdFilter});
            Map<Long, Long> boIdToBillEntryIdMap = Arrays.stream(batchBillEntryDy).collect(Collectors.toMap(dyn -> dyn.getLong("adminorg.boid"), dyn -> dyn.getLong("id")));
            DynamicObject adminorgLog = this.getLogData(billId, opData[0], batchBillDy);
            ArrayList<DynamicObject> logEntryList = new ArrayList<DynamicObject>(opData.length);
            for (DynamicObject adminorg : opData) {
                DynamicObject logEntry = this.getLogEntryData(adminorg, adminorgLog.getLong("id"), oldOrgMap, boIdToNewVidMap, boIdToBillEntryIdMap);
                logEntryList.add(logEntry);
            }
            this.logEntryHelper.save(logEntryList.toArray(new DynamicObject[0]));
        } else {
            ArrayList<DynamicObject> logList = new ArrayList<DynamicObject>(opData.length);
            ArrayList<DynamicObject> logEntryList = new ArrayList<DynamicObject>(opData.length);
            long[] logIds = ORM.create().genLongIds("haos_orgchgeffectlog", opData.length);
            for (int i = 0; i < opData.length; ++i) {
                long id = logIds[i];
                DynamicObject adminorg = opData[i];
                DynamicObject adminorgLog = this.getLogData(adminorg, id);
                DynamicObject logEntry = this.getLogEntryData(adminorg, id, oldOrgMap, boIdToNewVidMap, null);
                logList.add(adminorgLog);
                logEntryList.add(logEntry);
            }
            this.logHelper.save(logList.toArray(new DynamicObject[0]));
            this.logEntryHelper.save(logEntryList.toArray(new DynamicObject[0]));
        }
    }

    public void updateAdminOrgChangeLog(DynamicObject[] opData, String entryDatastatus, Map<Long, String> failMsgMap) {
        List<Long> ids = Arrays.stream(opData).map(dyn -> dyn.getLong("id")).collect(Collectors.toList());
        DynamicObject[] logEntrys = this.getAdminOrgLogEntryByAfterVid(ids);
        Map<Long, DynamicObject> orgIdToLogEntryDynMap = Arrays.stream(logEntrys).collect(Collectors.toMap(dyn -> dyn.getLong("entryafterversion.id"), dyn -> dyn, (v1, v2) -> v1));
        for (DynamicObject adminorg : opData) {
            long id = adminorg.getLong("id");
            DynamicObject logEntry = orgIdToLogEntryDynMap.get(id);
            if (logEntry == null) continue;
            logEntry.set("entrydatastatus", (Object)(HRStringUtils.isEmpty((String)entryDatastatus) ? adminorg.getString("datastatus") : entryDatastatus));
            logEntry.set("errormsg", (Object)failMsgMap.get(id));
            logEntry.set("bsed", (Object)adminorg.getDate("bsed"));
        }
        this.logEntryHelper.save(logEntrys);
        Set<Long> logIds = Arrays.stream(logEntrys).map(dyn -> dyn.getLong("pid.id")).collect(Collectors.toSet());
        this.updateLog(logIds);
    }

    private void updateLog(Set<Long> logIds) {
        DynamicObject[] logs;
        DynamicObject[] logEntrys = this.getAdminOrgLogEntryByPid(logIds);
        Map<Long, List<DynamicObject>> logIdToLogEntryDynMap = Arrays.stream(logEntrys).collect(Collectors.groupingBy(dyn -> dyn.getLong("pid")));
        HashMap logIdToDatastatusMap = Maps.newHashMapWithExpectedSize((int)logIdToLogEntryDynMap.size());
        HashMap logIdToBsedMap = Maps.newHashMapWithExpectedSize((int)logIdToLogEntryDynMap.size());
        logIdToLogEntryDynMap.forEach((logId, logEntrysList) -> {
            Set entryDatastatusList = logEntrysList.stream().map(dyn -> dyn.getString("entrydatastatus")).collect(Collectors.toSet());
            if (entryDatastatusList.size() == 1) {
                if (entryDatastatusList.contains("0")) {
                    logIdToDatastatusMap.put(logId, "0");
                } else if (entryDatastatusList.contains("1")) {
                    logIdToDatastatusMap.put(logId, "1");
                } else if (entryDatastatusList.contains("2")) {
                    logIdToDatastatusMap.put(logId, "3");
                } else if (entryDatastatusList.contains("3")) {
                    logIdToDatastatusMap.put(logId, "4");
                }
                logIdToBsedMap.put(logId, ((DynamicObject)logEntrysList.get(0)).getDate("bsed"));
            } else if (entryDatastatusList.size() > 1) {
                if (entryDatastatusList.contains("1")) {
                    logIdToDatastatusMap.put(logId, "2");
                } else if (entryDatastatusList.contains("0")) {
                    logIdToDatastatusMap.put(logId, "0");
                } else if (entryDatastatusList.contains("2")) {
                    logIdToDatastatusMap.put(logId, "3");
                }
            }
        });
        for (DynamicObject log : logs = this.getAdminOrgLogById(logIds)) {
            long id = log.getLong("id");
            String datastatus = (String)logIdToDatastatusMap.get(id);
            if (HRStringUtils.isEmpty((String)datastatus)) continue;
            log.set("datastatus", (Object)datastatus);
            if (log.getLong("refbill.id") != 0L || logIdToBsedMap.get(id) == null) continue;
            log.set("bsed", logIdToBsedMap.get(id));
        }
        this.logHelper.save(logs);
    }

    private DynamicObject getLogData(long billId, DynamicObject adminorg, DynamicObject bill) {
        DynamicObject adminorgLog = this.logHelper.queryOriginalOne("id", new QFilter("refbill", "=", (Object)billId));
        if (adminorgLog == null) {
            adminorgLog = this.logHelper.generateEmptyDynamicObject();
            adminorgLog.set("changesource", (Object)1L);
            adminorgLog.set("refbill", (Object)billId);
            adminorgLog.set("datastatus", (Object)adminorg.getString("datastatus"));
            adminorgLog.set("bsed", (Object)(bill == null ? adminorg.getDate("bsed") : bill.getDate("effdt")));
            this.logHelper.saveOne(adminorgLog);
        }
        return adminorgLog;
    }

    private DynamicObject getLogData(DynamicObject adminorg, long id) {
        DynamicObject adminorgLog = this.logHelper.generateEmptyDynamicObject();
        adminorgLog.set("changesource", (Object)2L);
        adminorgLog.set("refbill", (Object)0L);
        adminorgLog.set("datastatus", (Object)adminorg.getString("datastatus"));
        adminorgLog.set("bsed", (Object)adminorg.getDate("bsed"));
        adminorgLog.set("id", (Object)id);
        return adminorgLog;
    }

    private DynamicObject getLogEntryData(DynamicObject adminorg, long logId, Map<Long, DynamicObject> oldOrgMap, Map<Long, Long> boIdToNewVidMap, Map<Long, Long> boIdToBillEntryIdMap) {
        DynamicObject logEntry = this.logEntryHelper.generateEmptyDynamicObject();
        long boid = adminorg.getLong("boid");
        DynamicObject oldDyn = oldOrgMap.get(boid);
        logEntry.set("entrybeforeversion", (Object)(oldDyn == null ? 0L : oldDyn.getLong("sourcevid")));
        logEntry.set("entryafterversion", (Object)boIdToNewVidMap.get(boid));
        logEntry.set("bo", (Object)boid);
        if (boIdToBillEntryIdMap != null) {
            logEntry.set("batchorgentity", (Object)boIdToBillEntryIdMap.get(boid));
        }
        logEntry.set("changescene", (Object)adminorg.getLong("changescene.id"));
        logEntry.set("changereason", (Object)adminorg.getLong("changereason.id"));
        logEntry.set("bsed", (Object)adminorg.getDate("bsed"));
        logEntry.set("entrydatastatus", (Object)adminorg.getString("datastatus"));
        logEntry.set("isfuture", (Object)HRStringUtils.equals((String)adminorg.getString("datastatus"), (String)HisModelDataStatusEnum.TO_BE_EFFECT.getStatus()));
        logEntry.set("pid", (Object)logId);
        return logEntry;
    }

    public Map<Long, DynamicObject> getChangeLogMapByVids(List<Long> vids) {
        QFilter qFilter = new QFilter("entryafterversion", "in", vids);
        Object[] dynObjArr = this.logEntryHelper.queryOriginalArray("entryafterversion, errormsg, entrydatastatus, changescene.orgchangetype.id, changescene.id, changereason.id", new QFilter[]{qFilter});
        if (ObjectUtils.isEmpty((Object[])dynObjArr)) {
            return Maps.newHashMap();
        }
        return Arrays.stream(dynObjArr).collect(Collectors.toMap(dynObj -> dynObj.getLong("entryafterversion"), dynObj -> dynObj, (v1, v2) -> v1));
    }

    public Map<Long, DynamicObject> loadChangeLogMapByVids(List<Long> vids) {
        QFilter qFilter = new QFilter("entryafterversion", "in", vids);
        Object[] dynObjArr = this.logEntryHelper.load("entryafterversion, changescene, changereason", new QFilter[]{qFilter});
        if (ObjectUtils.isEmpty((Object[])dynObjArr)) {
            return Maps.newHashMap();
        }
        return Arrays.stream(dynObjArr).collect(Collectors.toMap(dynObj -> dynObj.getLong("entryafterversion.id"), dynObj -> dynObj, (v1, v2) -> v1));
    }

    public DynamicObject getAdminOrgLogEntryByAfterVid(long afterVid) {
        QFilter qFilter = new QFilter("entryafterversion", "=", (Object)afterVid);
        return this.logEntryHelper.loadOne("changescene, changereason", new QFilter[]{qFilter});
    }

    public DynamicObject[] getAdminOrgLogEntryByAfterVid(List<Long> afterVids) {
        QFilter qFilter = new QFilter("entryafterversion", "in", afterVids);
        return this.logEntryHelper.loadDynamicObjectArray(new QFilter[]{qFilter});
    }

    public DynamicObject[] getAdminOrgLogEntryByPid(Set<Long> pid) {
        QFilter qFilter = new QFilter("pid", "in", pid);
        return this.logEntryHelper.queryOriginalArray("pid, entrydatastatus, bsed", new QFilter[]{qFilter});
    }

    public DynamicObject[] getAdminOrgLogById(Set<Long> id) {
        QFilter qFilter = new QFilter("id", "in", id);
        return this.logHelper.loadDynamicObjectArray(new QFilter[]{qFilter});
    }
}
