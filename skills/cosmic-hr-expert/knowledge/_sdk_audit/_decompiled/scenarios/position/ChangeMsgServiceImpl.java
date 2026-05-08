/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.MainEntityType
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.bos.orm.query.QFilter
 *  kd.bos.orm.util.CollectionUtils
 *  kd.bos.schedule.api.JobInfo
 *  kd.bos.schedule.executor.JobClient
 *  kd.bos.schedule.server.ScheduleService
 *  kd.bos.servicehelper.MetadataServiceHelper
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hrmp.hbpm.business.domain.position.models.EventMsgBo
 *  kd.hrmp.hbpm.business.domain.position.service.repository.HBPMMsgRepository
 */
package kd.hrmp.hbpm.business.domain.position.service.impl;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.MainEntityType;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QFilter;
import kd.bos.orm.util.CollectionUtils;
import kd.bos.schedule.api.JobInfo;
import kd.bos.schedule.executor.JobClient;
import kd.bos.schedule.server.ScheduleService;
import kd.bos.servicehelper.MetadataServiceHelper;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hrmp.hbpm.business.domain.position.models.EventMsgBo;
import kd.hrmp.hbpm.business.domain.position.service.IChangeMsgService;
import kd.hrmp.hbpm.business.domain.position.service.repository.HBPMMsgRepository;

public class ChangeMsgServiceImpl
implements IChangeMsgService {
    private static final Log LOGGER = LogFactory.getLog(ChangeMsgServiceImpl.class);

    @Override
    public void handleChangeMsg(List<EventMsgBo> eventMsgBoList) {
        if (CollectionUtils.isEmpty(eventMsgBoList)) {
            return;
        }
        MainEntityType dataEntityType = MetadataServiceHelper.getDataEntityType((String)"hbpm_position_msgdetail");
        ArrayList msgList = Lists.newArrayListWithExpectedSize((int)eventMsgBoList.size());
        for (EventMsgBo msgBo : eventMsgBoList) {
            msgList.add(msgBo.toMsgDyn(dataEntityType));
        }
        HBPMMsgRepository.getInstance().saveBatch((List)msgList);
    }

    @Override
    public void sendMsg() {
        if (this.isExistSheduledTask("5/2/X9QCCFNS")) {
            return;
        }
        JobInfo jobInfo = ScheduleService.getInstance().getObjectFactory().getJobDao().get("5/2/X9QCCFNS");
        jobInfo.setScheduleId("5/202+6WE40+");
        String dispatch = JobClient.dispatch((JobInfo)jobInfo);
        LOGGER.info(dispatch);
    }

    @Override
    public void sendStdPosMsg() {
        if (this.isExistSheduledTask("5/20+40R8LME")) {
            return;
        }
        JobInfo jobInfo = ScheduleService.getInstance().getObjectFactory().getJobDao().get("5/20+40R8LME");
        jobInfo.setScheduleId("5/20B=R/LY95");
        String dispatch = JobClient.dispatch((JobInfo)jobInfo);
        LOGGER.info(dispatch);
    }

    private boolean isExistSheduledTask(String jobId) {
        HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("sch_task");
        DynamicObject task = serviceHelper.queryOriginalOne("id", new QFilter[]{new QFilter("job.id", "=", (Object)jobId), new QFilter("status", "=", (Object)"SCHEDULED")});
        return task != null;
    }
}
