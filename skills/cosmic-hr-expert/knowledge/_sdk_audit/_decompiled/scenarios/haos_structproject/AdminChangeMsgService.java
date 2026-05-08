/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.context.RequestContext
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.dataentity.entity.DynamicObjectCollection
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.schedule.api.JobInfo
 *  kd.bos.schedule.executor.JobClient
 *  kd.bos.schedule.server.ScheduleService
 *  kd.hr.haos.business.domain.adminorg.service.impl.ChangeSceneReposity
 *  kd.hr.haos.business.domain.adminorg.service.impl.OrgRepository
 *  kd.hr.haos.business.util.PropertyGetUtils
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 */
package kd.hr.haos.business.domain.org.service;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.schedule.api.JobInfo;
import kd.bos.schedule.executor.JobClient;
import kd.bos.schedule.server.ScheduleService;
import kd.hr.haos.business.domain.adminorg.service.impl.ChangeSceneReposity;
import kd.hr.haos.business.domain.adminorg.service.impl.OrgRepository;
import kd.hr.haos.business.util.PropertyGetUtils;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;

public class AdminChangeMsgService {
    private static final Log LOGGER = LogFactory.getLog(AdminChangeMsgService.class);
    private static final String JOB_ID = "5+X/4Y=AOZ=O";
    private static final String SCHEDULE_ID = "5+X/=KD8ZXFW";
    private static final HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorg_msgdetail");

    public void saveAdminChangeMsg(List<DynamicObject> adminChangeMsgDyList) {
        helper.save(adminChangeMsgDyList.toArray(new DynamicObject[0]));
    }

    public DynamicObject getAdminChangeMsgDy() {
        return helper.generateEmptyDynamicObject();
    }

    public void saveChgAdminOrgMsgDetail(DynamicObject[] opData, Map<Long, DynamicObject> oldOrgMap) {
        HashMap<Long, Long> boidAndSourceIdBeforeMap = new HashMap<Long, Long>(oldOrgMap.size());
        for (DynamicObject oldDy : oldOrgMap.values()) {
            boidAndSourceIdBeforeMap.put(oldDy.getLong("boid"), oldDy.getLong("sourcevid"));
        }
        DynamicObject[] beforeDys = OrgRepository.getInstance().loadByPks(boidAndSourceIdBeforeMap.values());
        Map sourceVidMap = Arrays.stream(beforeDys).collect(Collectors.toMap(dy -> dy.getLong("id"), Function.identity(), (oldValue, newValue) -> newValue));
        ArrayList<DynamicObject> msgDetailList = new ArrayList<DynamicObject>(opData.length);
        List changesceneIds = Arrays.stream(opData).map(dy -> dy.getLong("changescene.id")).collect(Collectors.toList());
        Map changesceneAndOperateMap = ChangeSceneReposity.getInstance().queryOriginalArrayByIds(changesceneIds);
        for (DynamicObject dataEntity : opData) {
            long boid = dataEntity.getLong("boid");
            DynamicObject adminChangeMsgDy = this.assembleMsgDy((DynamicObject)sourceVidMap.get(boidAndSourceIdBeforeMap.get(boid)), dataEntity, changesceneAndOperateMap);
            msgDetailList.add(adminChangeMsgDy);
        }
        this.saveAdminChangeMsg(msgDetailList);
    }

    public void saveReviseAdminOrgMsgDetail(DynamicObject beforeVersionDy, DynamicObject afterVersionDy) {
        DynamicObject adminChangeMsgDy = this.assembleMsgDy(beforeVersionDy, afterVersionDy, null);
        this.saveAdminChangeMsg(Lists.newArrayList((Object[])new DynamicObject[]{adminChangeMsgDy}));
    }

    public DynamicObject assembleMsgDy(DynamicObject beforeChgDy, DynamicObject afterChgDy, Map<Long, Long> changesceneAndOperateMap) {
        DynamicObject adminChangeMsgDy = this.getAdminChangeMsgDy();
        adminChangeMsgDy.set("bo", (Object)afterChgDy.getLong("boid"));
        adminChangeMsgDy.set("beforeversion", (Object)beforeChgDy);
        if (afterChgDy.getLong("sourcevid") != 0L) {
            adminChangeMsgDy.set("afterversion", (Object)afterChgDy.getLong("sourcevid"));
        } else {
            adminChangeMsgDy.set("afterversion", (Object)afterChgDy.getLong("id"));
        }
        if (beforeChgDy == null || PropertyGetUtils.getDyBdPropId((DynamicObject)beforeChgDy, (String)"belongcompany") == PropertyGetUtils.getDyBdPropId((DynamicObject)afterChgDy, (String)"belongcompany")) {
            adminChangeMsgDy.set("isbelongcompanychange", (Object)false);
        } else {
            adminChangeMsgDy.set("isbelongcompanychange", (Object)true);
        }
        adminChangeMsgDy.set("traceid", (Object)RequestContext.get().getTraceId());
        DynamicObject changeSceneDy = afterChgDy.getDynamicObject("changescene");
        if (changesceneAndOperateMap == null || !changesceneAndOperateMap.containsKey(changeSceneDy.getLong("id"))) {
            DynamicObjectCollection changeoperatCol = changeSceneDy.getDynamicObjectCollection("changeoperat");
            if (changeoperatCol != null && changeoperatCol.size() > 0) {
                adminChangeMsgDy.set("changeoperate", (Object)((DynamicObject)changeoperatCol.get(0)).getDynamicObject("fbasedataid"));
            } else {
                LOGGER.warn("changescene.changeoperat is empty");
            }
        } else {
            adminChangeMsgDy.set("changeoperate", (Object)changesceneAndOperateMap.get(changeSceneDy.getLong("id")));
        }
        adminChangeMsgDy.set("changescene", (Object)changeSceneDy);
        adminChangeMsgDy.set("sendstate", (Object)"0");
        adminChangeMsgDy.set("creator", (Object)RequestContext.get().getCurrUserId());
        return adminChangeMsgDy;
    }

    public void handleChangeMsg() {
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("sch_task");
        DynamicObject task = serviceHelper.queryOriginalOne("id", new QFilter[]{new QFilter("job.id", "=", (Object)JOB_ID), new QFilter("status", "=", (Object)"SCHEDULED")});
        if (task != null) {
            return;
        }
        JobInfo jobInfo = ScheduleService.getInstance().getObjectFactory().getJobDao().get(JOB_ID);
        jobInfo.setScheduleId(SCHEDULE_ID);
        String dispatch = JobClient.dispatch((JobInfo)jobInfo);
        LOGGER.info(dispatch);
    }
}
