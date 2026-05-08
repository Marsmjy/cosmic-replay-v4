/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.db.tx.TX
 *  kd.bos.db.tx.TXHandle
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.ORM
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.common.util.HRDateTimeUtils
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrptc.business.subscribe.ReportSubscribeConfigService
 *  kd.hr.hrptc.common.constant.perm.ReportUserPermConstants
 *  kd.hr.hrptc.opplugin.validator.perm.ReportUserPermValidator
 */
package kd.hr.hrptc.opplugin.web.perm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.common.util.HRDateTimeUtils;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrptc.business.subscribe.ReportSubscribeConfigService;
import kd.hr.hrptc.common.constant.perm.ReportUserPermConstants;
import kd.hr.hrptc.opplugin.validator.perm.ReportUserPermValidator;

@ExcludeFromJacocoGeneratedReport
public final class ReportUserPermOp
extends HRDataBaseOp
implements ReportUserPermConstants {
    private static final Log LOGGER = LogFactory.getLog(ReportUserPermOp.class);
    private final HRBaseServiceHelper reportPermHelper = new HRBaseServiceHelper("hrptc_rptalocperm");

    public void onAddValidators(AddValidatorsEventArgs args) {
        try {
            super.onAddValidators(args);
            args.addValidator((AbstractValidator)new ReportUserPermValidator());
        }
        catch (Exception exception) {
            LOGGER.error("ReportUserPermOp_onAddValidators_error:", (Throwable)exception);
        }
    }

    public void onPreparePropertys(PreparePropertysEventArgs eventArgs) {
        super.onPreparePropertys(eventArgs);
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);
        try {
            DynamicObject[] dataEntities = args.getDataEntities();
            switch (args.getOperationKey()) {
                case "save": {
                    this.clearEmptyEntryDy(dataEntities);
                    this.setDefaultFieldValue(dataEntities);
                    this.saveReportPermDy(dataEntities);
                    break;
                }
                case "delete": {
                    this.deleteReportPermDy(dataEntities);
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("ReportUserPermOp_beginOperationTransaction_error:", (Throwable)exception);
            args.setCancel(true);
            args.setCancelMessage(exception.getMessage());
        }
    }

    private void clearEmptyEntryDy(DynamicObject[] userPermDys) {
        for (DynamicObject userPermDy : userPermDys) {
            DynamicObjectCollection permDataEntry = userPermDy.getDynamicObjectCollection("rptpermdataentry");
            Iterator iterator = permDataEntry.iterator();
            while (iterator.hasNext()) {
                DynamicObject permDataEntryDy = (DynamicObject)iterator.next();
                String data = permDataEntryDy.getString("data");
                boolean noLimit = permDataEntryDy.getBoolean("nolimit");
                if (noLimit || !HRStringUtils.isEmpty((String)data)) continue;
                iterator.remove();
            }
        }
    }

    private void setDefaultFieldValue(DynamicObject[] userPermDys) {
        Date sysMaxDate = HRDateTimeUtils.getSysMaxDate();
        Date nowDate = HRDateTimeUtils.getNowDate();
        RequestContext context = RequestContext.get();
        long currUserId = context.getCurrUserId();
        for (DynamicObject userPermDy : userPermDys) {
            DynamicObjectCollection permDataEntry = userPermDy.getDynamicObjectCollection("rptpermdataentry");
            DynamicObject userDy = userPermDy.getDynamicObject("user");
            for (DynamicObject permDataEntryDy : permDataEntry) {
                permDataEntryDy.set("permuser", (Object)userDy);
                permDataEntryDy.set("modifyuser", (Object)currUserId);
                permDataEntryDy.set("startdate", (Object)nowDate);
                permDataEntryDy.set("enddate", (Object)sysMaxDate);
            }
            DynamicObjectCollection selectDataEntry = userPermDy.getDynamicObjectCollection("selectreportentry");
            for (DynamicObject selectDataEntryDy : selectDataEntry) {
                selectDataEntryDy.set("selectuser", (Object)userDy);
                selectDataEntryDy.set("selectstartdate", (Object)nowDate);
                selectDataEntryDy.set("selectenddate", (Object)sysMaxDate);
                selectDataEntryDy.set("selectadminuser", (Object)currUserId);
            }
        }
    }

    private void saveReportPermDy(DynamicObject[] userPermDys) {
        try (TXHandle txHandle = TX.requiresNew();){
            try {
                for (DynamicObject userPermDy : userPermDys) {
                    DynamicObjectCollection permDataEntry = userPermDy.getDynamicObjectCollection("rptpermdataentry");
                    DynamicObjectCollection selectEntryColl = userPermDy.getDynamicObjectCollection("selectreportentry");
                    HashSet emptyRefReportIds = Sets.newHashSetWithExpectedSize((int)permDataEntry.size());
                    for (DynamicObject permDataEntryDy : permDataEntry) {
                        DynamicObject reportPermDy = permDataEntryDy.getDynamicObject("reportperm");
                        if (reportPermDy != null) continue;
                        DynamicObject reportDy = permDataEntryDy.getDynamicObject("report");
                        emptyRefReportIds.add(reportDy.getLong("id"));
                    }
                    Set emptySelectRefReportIds = selectEntryColl.stream().filter(dy -> null == dy.getDynamicObject("selectreportperm")).map(dy -> dy.getDynamicObject("selectreport").getLong("id")).collect(Collectors.toSet());
                    emptyRefReportIds.addAll(emptySelectRefReportIds);
                    if (emptyRefReportIds.isEmpty()) continue;
                    DynamicObject[] dbReportPermDys = this.reportPermHelper.loadDynamicObjectArray(new QFilter[]{new QFilter("rptmanage", "in", (Object)emptyRefReportIds)});
                    Map reportIdToPermDyMap = Arrays.stream(dbReportPermDys).collect(Collectors.toMap(dy -> dy.getDynamicObject("rptmanage").getLong("id"), Function.identity()));
                    emptyRefReportIds.removeAll(reportIdToPermDyMap.keySet());
                    if (!emptyRefReportIds.isEmpty()) {
                        long[] newReportPermIds = ORM.create().genLongIds("hrptc_rptalocperm", emptyRefReportIds.size());
                        HashMap reportRefPermDyMap = Maps.newHashMapWithExpectedSize((int)emptyRefReportIds.size());
                        int index = 0;
                        for (Long emptyRefReportId : emptyRefReportIds) {
                            DynamicObject newReportPermDy = this.reportPermHelper.generateEmptyDynamicObject();
                            newReportPermDy.set("id", (Object)newReportPermIds[index]);
                            newReportPermDy.set("rptmanage", (Object)emptyRefReportId);
                            ++index;
                            reportRefPermDyMap.put(emptyRefReportId, newReportPermDy);
                            reportIdToPermDyMap.put(emptyRefReportId, newReportPermDy);
                        }
                        this.reportPermHelper.save(reportRefPermDyMap.values().toArray(new DynamicObject[0]));
                    }
                    for (DynamicObject permDataEntryDy : permDataEntry) {
                        DynamicObject reportPermDy = permDataEntryDy.getDynamicObject("reportperm");
                        if (reportPermDy != null) continue;
                        DynamicObject reportDy = permDataEntryDy.getDynamicObject("report");
                        reportPermDy = (DynamicObject)reportIdToPermDyMap.get(reportDy.getLong("id"));
                        permDataEntryDy.set("reportperm", (Object)reportPermDy);
                    }
                    for (DynamicObject selectDataDy : selectEntryColl) {
                        if (null != selectDataDy.getDynamicObject("selectreportperm")) continue;
                        long reportId = selectDataDy.getDynamicObject("selectreport").getLong("id");
                        selectDataDy.set("selectreportperm", reportIdToPermDyMap.get(reportId));
                    }
                }
                this.cancelReportPermDeleteSubscribeRecord(userPermDys);
            }
            catch (Exception exception) {
                txHandle.markRollback();
                throw exception;
            }
        }
    }

    private void cancelReportPermDeleteSubscribeRecord(DynamicObject[] userPermDys) {
        Map<Long, List<Long>> originalReportPermReportIdMap = this.getOriginalReportPermReportIds(userPermDys);
        HashMap userReportMap = Maps.newHashMapWithExpectedSize((int)userPermDys.length);
        for (DynamicObject userPermDy : userPermDys) {
            if (!userPermDy.getDataEntityState().getFromDatabase()) continue;
            long userPermId = userPermDy.getLong("id");
            Long userId = userPermDy.getDynamicObject("user").getLong("id");
            List<Long> originalReportIds = originalReportPermReportIdMap.get(userPermId);
            if (originalReportIds == null) continue;
            DynamicObjectCollection selectEntryColl = userPermDy.getDynamicObjectCollection("selectreportentry");
            List currentReportIds = selectEntryColl.stream().map(row -> row.getDynamicObject("selectreport").getLong("id")).collect(Collectors.toList());
            originalReportIds.removeAll(currentReportIds);
            if (originalReportIds.size() <= 0) continue;
            List removeSubscribeReportIds = userReportMap.getOrDefault(userId, Lists.newArrayListWithCapacity((int)10));
            removeSubscribeReportIds.addAll(originalReportIds);
            userReportMap.putIfAbsent(userId, removeSubscribeReportIds);
        }
        if (userReportMap.isEmpty()) {
            return;
        }
        ReportSubscribeConfigService subscribeConfigService = new ReportSubscribeConfigService();
        QFilter qFilter = null;
        for (Map.Entry entry : userReportMap.entrySet()) {
            if (qFilter == null) {
                qFilter = new QFilter("report", "in", entry.getValue()).and("user", "=", entry.getKey());
                continue;
            }
            qFilter.or(new QFilter("report", "in", entry.getValue()).and("user", "=", entry.getKey()));
        }
        DynamicObject[] configDys = new HRBaseServiceHelper("hrptmc_subscriberecord").loadDynamicObjectArray(new QFilter[]{qFilter});
        ArrayList scheMaps = Lists.newArrayListWithExpectedSize((int)configDys.length);
        ArrayList subscribeIds = Lists.newArrayListWithExpectedSize((int)configDys.length);
        for (DynamicObject configDy : configDys) {
            HashMap mapTemp = Maps.newHashMap();
            mapTemp.put("planId", configDy.get("scheplan.id"));
            mapTemp.put("planLastId", configDy.get("scheplanlast.id"));
            mapTemp.put("jobId", configDy.get("schejob.id"));
            mapTemp.put("subscribeIds", Collections.singletonList(configDy.getLong("id")));
            subscribeIds.add(configDy.getLong("id"));
            scheMaps.add(mapTemp);
        }
        subscribeConfigService.deleteSchedulePlanAndJob((List)scheMaps, (List)subscribeIds);
    }

    private Map<Long, List<Long>> getOriginalReportPermReportIds(DynamicObject[] userPermDys) {
        List userPermDyIds = Arrays.stream(userPermDys).map(dy -> dy.getLong("id")).collect(Collectors.toList());
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrptc_userperm");
        DynamicObject[] originalUserPermDys = helper.query("id, selectreportentry, selectreportentry.selectreport", new QFilter[]{new QFilter("id", "in", userPermDyIds)});
        return Arrays.stream(originalUserPermDys).collect(Collectors.toMap(dy -> dy.getLong("id"), dy -> dy.getDynamicObjectCollection("selectreportentry").stream().map(rptDy -> rptDy.getDynamicObject("selectreport").getLong("id")).collect(Collectors.toList())));
    }

    private void deleteReportPermDy(DynamicObject[] userPermDys) {
        for (DynamicObject userPermDy : userPermDys) {
            DynamicObject[] reportPermDys;
            DynamicObject reportDy;
            DynamicObject userDy = userPermDy.getDynamicObject("user");
            long userId = userDy.getLong("id");
            DynamicObjectCollection permDataEntry = userPermDy.getDynamicObjectCollection("rptpermdataentry");
            DynamicObjectCollection selectReportEntry = userPermDy.getDynamicObjectCollection("selectreportentry");
            HashSet refReportIds = Sets.newHashSetWithExpectedSize((int)(permDataEntry.size() + selectReportEntry.size()));
            for (DynamicObject permDataEntryDy : permDataEntry) {
                reportDy = permDataEntryDy.getDynamicObject("report");
                if (reportDy == null) continue;
                refReportIds.add(reportDy.getLong("id"));
            }
            for (DynamicObject selectReportEntryDy : selectReportEntry) {
                reportDy = selectReportEntryDy.getDynamicObject("selectreport");
                if (reportDy == null) continue;
                refReportIds.add(reportDy.getLong("id"));
            }
            if (refReportIds.isEmpty() || (reportPermDys = this.reportPermHelper.loadDynamicObjectArray(new QFilter[]{new QFilter("rptmanage", "in", (Object)refReportIds)})) == null || reportPermDys.length <= 0) continue;
            ArrayList needDeleteReportPermIds = Lists.newArrayListWithExpectedSize((int)reportPermDys.length);
            for (DynamicObject reportPermDy : reportPermDys) {
                DynamicObjectCollection reportToUserPermEntry = reportPermDy.getDynamicObjectCollection("entryentity");
                boolean notFind = false;
                for (DynamicObject reportToUserPermEntryDy : reportToUserPermEntry) {
                    long entryUserId;
                    DynamicObject userField = reportToUserPermEntryDy.getDynamicObject("userfield");
                    if (userField == null || userId == (entryUserId = userField.getLong("id"))) continue;
                    notFind = true;
                    break;
                }
                if (notFind) continue;
                needDeleteReportPermIds.add(reportPermDy.getPkValue());
            }
            if (needDeleteReportPermIds.isEmpty()) continue;
            this.reportPermHelper.delete(needDeleteReportPermIds.toArray());
        }
    }
}
