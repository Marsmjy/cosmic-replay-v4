/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.utils.ObjectUtils
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IEmpStageApplicationService
 *  kd.hrmp.hrpi.business.domain.employee.service.IEmpEntRelDomainService
 *  kd.hrmp.hrpi.business.domain.employee.service.IEmpStageDomainService
 *  kd.sdk.hr.hbp.business.helper.timeline.TimelineServiceHelper
 */
package kd.hrmp.hrpi.opplugin.web.employee.attach;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.ObjectUtils;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IEmpStageApplicationService;
import kd.hrmp.hrpi.business.domain.employee.service.IEmpEntRelDomainService;
import kd.hrmp.hrpi.business.domain.employee.service.IEmpStageDomainService;
import kd.sdk.hr.hbp.business.helper.timeline.TimelineServiceHelper;

public final class EmpEntRelEmpStageSaveOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(EmpEntRelEmpStageSaveOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("entrydate");
        fieldKeys.add("employee");
        fieldKeys.add("empstage");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        Object[] dataEntities = e.getDataEntities();
        if (ObjectUtils.isEmpty((Object[])dataEntities)) {
            return;
        }
        String operationKey = e.getOperationKey();
        if ("save".equals(operationKey)) {
            ArrayList<DynamicObject> stageList = new ArrayList<DynamicObject>(10);
            HashSet<String> cachedNewStage = new HashSet<String>();
            for (Object dataEntity : dataEntities) {
                DynamicObject stageObj = null;
                DynamicObject employee = dataEntity.getDynamicObject("employee");
                Date entryDate = dataEntity.getDate("entrydate");
                long employeeId = employee.getLong("id");
                if (dataEntity.getLong("id") == 0L || !IEmpEntRelDomainService.getInstance().isExists(new QFilter("entrydate", "=", (Object)entryDate).and("employee.id", "=", (Object)dataEntity.getLong("employee.id")).or(new QFilter("id", "=", (Object)dataEntity.getLong("id"))))) {
                    if (this.isDuplicate(employeeId, entryDate, cachedNewStage)) continue;
                    stageObj = IEmpStageDomainService.getInstance().generateEmptyDynamicObject();
                    stageObj.set("employee", (Object)employee);
                } else {
                    DynamicObject stage = IEmpStageApplicationService.getInstance().getStandardEmpStageByDate(entryDate, Long.valueOf(employeeId));
                    if (stage != null) {
                        stageObj = stage;
                        if (stage.getDate("entrydate").compareTo(entryDate) == 0) {
                            continue;
                        }
                    } else {
                        if (this.isDuplicate(employeeId, entryDate, cachedNewStage)) continue;
                        stageObj = IEmpStageDomainService.getInstance().generateEmptyDynamicObject();
                        stageObj.set("employee", (Object)employee);
                    }
                }
                stageObj.set("entrydate", (Object)entryDate);
                if (stageObj.get("enddate") == null) {
                    DynamicObject nextStageDy = IEmpStageDomainService.getInstance().queryOne("id,employee,entrydate,enddate,departdate", new QFilter[]{new QFilter("entrydate", ">", (Object)entryDate), new QFilter("employee.id", "=", (Object)employeeId)}, "entrydate asc");
                    if (nextStageDy != null) {
                        stageObj.set("enddate", (Object)HRDateTimeUtils.getBeforeDay((Date)nextStageDy.getDate("entrydate")));
                    } else {
                        stageObj.set("enddate", (Object)TimelineServiceHelper.getSysMaxEffEndDate());
                    }
                }
                dataEntity.set("empstage", (Object)stageObj);
                stageList.add(stageObj);
                DynamicObject prevStage = IEmpStageDomainService.getInstance().queryOne("id,employee,entrydate,enddate,departdate", new QFilter[]{new QFilter("entrydate", "<", (Object)entryDate), new QFilter("employee.id", "=", (Object)employeeId)}, "entrydate desc");
                Date date = HRDateTimeUtils.addHour((Date)stageObj.getDate("entrydate"), (long)-24L);
                if (prevStage == null || prevStage.getDate("entrydate").compareTo(date) == 0) continue;
                prevStage = IEmpStageDomainService.getInstance().queryOne("id,employee,entrydate,enddate,departdate", (Object)prevStage.getLong("id"));
                prevStage.set("enddate", (Object)date);
                stageList.add(prevStage);
            }
            if (!ObjectUtils.isEmpty(stageList)) {
                LOGGER.debug("stageList size|{},stageList|{}", (Object)stageList.size(), stageList);
                this.saveEmpStage(stageList);
            }
        }
    }

    private void saveEmpStage(List<DynamicObject> stageList) {
        int size = stageList.size();
        ArrayList modifyList = Lists.newArrayListWithCapacity((int)size);
        ArrayList newList = Lists.newArrayListWithCapacity((int)size);
        for (DynamicObject stageDy : stageList) {
            if (stageDy.getDataEntityState().getFromDatabase()) {
                modifyList.add(stageDy);
                continue;
            }
            newList.add(stageDy);
        }
        IEmpStageDomainService stageDomainService = IEmpStageDomainService.getInstance();
        stageDomainService.save(newList.toArray(new DynamicObject[0]));
        stageDomainService.save(modifyList.toArray(new DynamicObject[0]));
    }

    private boolean isDuplicate(long employeeId, Date entryDate, Set<String> cachedNewStage) {
        String key = String.format("%d_%d", employeeId, HRDateTimeUtils.truncateDate((Date)entryDate).getTime());
        if (cachedNewStage.contains(key)) {
            LOGGER.info("skip new stage for employeeId:{} entryDate:{}", (Object)employeeId, (Object)entryDate);
            return true;
        }
        LOGGER.info("create new stage for employeeId:{} entryDate:{}", (Object)employeeId, (Object)entryDate);
        cachedNewStage.add(key);
        return false;
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        Object[] dataEntities = e.getDataEntities();
        if (ObjectUtils.isEmpty((Object[])dataEntities)) {
            return;
        }
        String operationKey = e.getOperationKey();
        if ("delete".equals(operationKey)) {
            HashSet empStageIdSet = new HashSet(10);
            Arrays.stream(dataEntities).forEach(obj -> {
                if (obj.getDynamicObject("empstage") == null) {
                    return;
                }
                int count = IEmpEntRelDomainService.getInstance().count(new QFilter[]{new QFilter("employee.id", "=", (Object)obj.getLong("employee.id")).and(new QFilter("entrydate", "=", (Object)obj.getDate("entrydate")))});
                if (count == 0) {
                    empStageIdSet.add(obj.getLong("empstage.id"));
                }
            });
            if (ObjectUtils.isEmpty(empStageIdSet)) {
                return;
            }
            IEmpStageDomainService.getInstance().deleteByFilter(new QFilter[]{new QFilter("id", "in", empStageIdSet)});
        }
        this.processLatestRecord((DynamicObject[])dataEntities);
    }

    private void processLatestRecord(DynamicObject[] dataEntities) {
        HashMap<String, List<DynamicObject>> empEntRelObjMap = new HashMap<String, List<DynamicObject>>(16);
        HashMap<String, Set<Long>> empEntryDateAndIdMap = new HashMap<String, Set<Long>>(16);
        HashSet<Long> empIdList = new HashSet<Long>(16);
        for (DynamicObject dataEntity : dataEntities) {
            empIdList.add(dataEntity.getLong("employee.id"));
        }
        DynamicObject[] empEntRels = IEmpEntRelDomainService.getInstance().loadDynamicObjectArray(new QFilter[]{new QFilter("employee.id", "in", empIdList)});
        this.setEntRelMapByEmployee(empEntRels, empEntRelObjMap, empEntryDateAndIdMap);
        ArrayList updateEmpEntRelList = new ArrayList(10);
        empEntRelObjMap.forEach((key, empEntRelList) -> {
            String[] empAndTimeKeys = key.split("_");
            Long employeeId = Long.valueOf(empAndTimeKeys[0]);
            Date entryDate = ((DynamicObject)empEntRelList.get(0)).getDate("entrydate");
            DynamicObject stage = IEmpStageApplicationService.getInstance().getStandardEmpStageByDate(entryDate, employeeId);
            IEmpStageApplicationService.getInstance().processEmpEntRelLatestRecord(empEntRelList, stage, updateEmpEntRelList);
        });
        if (!updateEmpEntRelList.isEmpty()) {
            IEmpEntRelDomainService.getInstance().save(updateEmpEntRelList.toArray(new DynamicObject[0]));
        }
    }

    private void setEntRelMapByEmployee(DynamicObject[] empEntRelObjs, Map<String, List<DynamicObject>> empEntRelObjMap, Map<String, Set<Long>> empEntryDateAndIdMap) {
        Arrays.stream(empEntRelObjs).forEach(obj -> {
            long employeeId = obj.getLong("employee.id");
            String key = employeeId + "_" + obj.getDate("entrydate").getTime();
            if (!empEntRelObjMap.containsKey(key)) {
                empEntRelObjMap.put(key, new ArrayList(10));
            }
            ((List)empEntRelObjMap.get(key)).add(obj);
            if (!empEntryDateAndIdMap.containsKey(key)) {
                empEntryDateAndIdMap.put(key, new HashSet(16));
            }
            ((Set)empEntryDateAndIdMap.get(key)).add(obj.getLong("id"));
        });
    }
}
