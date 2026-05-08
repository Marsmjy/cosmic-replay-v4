/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrptc.business.subscribe.ReportSubscribeConfigService
 *  kd.hr.hrptc.common.constant.permission.EntityConstants
 *  kd.hr.hrptc.opplugin.web.perm.ReportUserPermOp
 *  kd.hr.hrptmc.business.repdesign.ReportPermissionService
 */
package kd.hr.hrptc.opplugin.web.perm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrptc.business.subscribe.ReportSubscribeConfigService;
import kd.hr.hrptc.common.constant.permission.EntityConstants;
import kd.hr.hrptc.opplugin.web.perm.ReportUserPermOp;
import kd.hr.hrptmc.business.repdesign.ReportPermissionService;

public final class RptAllotPermOp
extends HRDataBaseOp
implements EntityConstants {
    private static final Log LOGGER = LogFactory.getLog(ReportUserPermOp.class);

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        super.beforeExecuteOperationTransaction(args);
        try {
            DynamicObject[] dataEntities = args.getDataEntities();
            switch (args.getOperationKey()) {
                case "save": {
                    this.saveUserPerm(dataEntities);
                    break;
                }
                case "delete": {
                    this.delete(dataEntities);
                }
            }
        }
        catch (Exception exception) {
            LOGGER.error("RptAllotPermOp_beginOperationTransaction_error:", (Throwable)exception);
            args.setCancel(true);
            args.setCancelMessage(exception.getMessage());
        }
    }

    private void saveUserPerm(DynamicObject[] dataEntities) {
        for (DynamicObject dynamicObject : dataEntities) {
            DynamicObject userAllotDy;
            long userId;
            DynamicObjectCollection entryColl = dynamicObject.getDynamicObjectCollection("rptpermdataentry");
            DynamicObjectCollection disEntryColl = dynamicObject.getDynamicObjectCollection("entryentity");
            Set userIds = entryColl.stream().filter(dy -> null == dy.getDynamicObject("userperm")).map(dy -> dy.getDynamicObject("user").getLong("id")).collect(Collectors.toSet());
            Set disUserIds = disEntryColl.stream().filter(dy -> null == dy.getDynamicObject("disuserperm")).map(dy -> dy.getDynamicObject("disuser").getLong("id")).collect(Collectors.toSet());
            userIds.addAll(disUserIds);
            DynamicObject[] userAllot = ReportPermissionService.getUserAloc(userIds);
            Map<Long, Long> userMap = Arrays.stream(userAllot).collect(Collectors.toMap(dy -> dy.getLong("user.id"), dy -> dy.getLong("id"), (x, y) -> x));
            for (DynamicObject permDataDy : entryColl) {
                if (null != permDataDy.getDynamicObject("userperm")) continue;
                userId = permDataDy.getDynamicObject("user").getLong("id");
                userAllotDy = this.getUserAllotDy(userMap, userId);
                permDataDy.set("userperm", (Object)userAllotDy);
            }
            for (DynamicObject permDataDy : disEntryColl) {
                if (null != permDataDy.getDynamicObject("disuserperm")) continue;
                userId = permDataDy.getDynamicObject("disuser").getLong("id");
                userAllotDy = this.getUserAllotDy(userMap, userId);
                permDataDy.set("disuserperm", (Object)userAllotDy);
            }
        }
        this.cancelUserPermDeleteSubscribeRecord(dataEntities);
    }

    private void cancelUserPermDeleteSubscribeRecord(DynamicObject[] reportPermDys) {
        Map<Long, List<Long>> originalReportPermUseMap = this.getOriginalReportPermUserIds(reportPermDys);
        HashMap reportUserMap = Maps.newHashMapWithExpectedSize((int)reportPermDys.length);
        for (DynamicObject reportPermDy : reportPermDys) {
            if (!reportPermDy.getDataEntityState().getFromDatabase()) continue;
            long reportPermId = reportPermDy.getLong("id");
            Long reportId = reportPermDy.getDynamicObject("rptmanage").getLong("id");
            List<Long> originalUserIds = originalReportPermUseMap.get(reportPermId);
            if (originalUserIds == null) continue;
            DynamicObjectCollection selectEntryColl = reportPermDy.getDynamicObjectCollection("entryentity");
            List currentUserIds = selectEntryColl.stream().map(row -> row.getDynamicObject("disuser").getLong("id")).collect(Collectors.toList());
            originalUserIds.removeAll(currentUserIds);
            if (originalUserIds.size() <= 0) continue;
            List removeSubscribeUserIds = reportUserMap.getOrDefault(reportId, Lists.newArrayListWithCapacity((int)10));
            removeSubscribeUserIds.addAll(originalUserIds);
            reportUserMap.putIfAbsent(reportId, removeSubscribeUserIds);
        }
        if (reportUserMap.isEmpty()) {
            return;
        }
        ReportSubscribeConfigService subscribeConfigService = new ReportSubscribeConfigService();
        QFilter qFilter = null;
        for (Map.Entry entry : reportUserMap.entrySet()) {
            if (qFilter == null) {
                qFilter = new QFilter("report", "=", entry.getKey()).and("user", "in", entry.getValue());
                continue;
            }
            qFilter.or(new QFilter("report", "=", entry.getKey()).and("user", "in", entry.getValue()));
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

    private Map<Long, List<Long>> getOriginalReportPermUserIds(DynamicObject[] reportPermDys) {
        List reportPermDyIds = Arrays.stream(reportPermDys).map(dy -> dy.getLong("id")).collect(Collectors.toList());
        HRBaseServiceHelper helper = new HRBaseServiceHelper("hrptc_rptalocperm");
        DynamicObject[] originalUserPermDys = helper.query("id, entryentity, entryentity.disuser", new QFilter[]{new QFilter("id", "in", reportPermDyIds)});
        return Arrays.stream(originalUserPermDys).collect(Collectors.toMap(dy -> dy.getLong("id"), dy -> dy.getDynamicObjectCollection("entryentity").stream().map(rptDy -> rptDy.getDynamicObject("disuser").getLong("id")).collect(Collectors.toList())));
    }

    private DynamicObject getUserAllotDy(Map<Long, Long> userMap, long userId) {
        DynamicObject userAllotDy;
        Long userAllotId = userMap.get(userId);
        if (null == userAllotId) {
            userAllotDy = ReportPermissionService.saveUserAloc((long)userId);
            userMap.put(userId, userAllotDy.getLong("id"));
        } else {
            userAllotDy = ReportPermissionService.genUserAloc((long)userAllotId);
        }
        return userAllotDy;
    }

    private void delete(DynamicObject[] dataEntities) {
    }
}
