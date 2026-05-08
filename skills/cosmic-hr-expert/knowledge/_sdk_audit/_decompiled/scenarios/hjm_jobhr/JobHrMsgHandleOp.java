/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hbjm.business.application.job.IJobApplication
 *  kd.hrmp.hbjm.business.domain.repository.JobRepository
 *  kd.hrmp.hbjm.business.domain.service.impl.ChangeMsgServiceImpl
 */
package kd.hrmp.hbjm.opplugin.web.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hbjm.business.application.job.IJobApplication;
import kd.hrmp.hbjm.business.domain.repository.JobRepository;
import kd.hrmp.hbjm.business.domain.service.impl.ChangeMsgServiceImpl;

public class JobHrMsgHandleOp
extends HRDataBaseOp {
    private Map<Long, Long> boToBeforeIdMap = new ConcurrentHashMap<Long, Long>();

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        List boIdList = Arrays.stream(args.getDataEntities()).map(dyn -> dyn.getLong("boid")).collect(Collectors.toList());
        this.boToBeforeIdMap = JobRepository.getInstance().getBoToLatestHisIdMap(boIdList);
    }

    public void endOperationTransaction(EndOperationTransactionArgs args) {
        DynamicObject[] jobs = args.getDataEntities();
        DynamicObject[] afterVersionsFromArgs = (DynamicObject[])Arrays.stream(jobs).filter(data -> !data.getBoolean("iscurrentversion")).toArray(DynamicObject[]::new);
        DynamicObject[] boArr = (DynamicObject[])Arrays.stream(jobs).filter(data -> data.getBoolean("iscurrentversion")).toArray(DynamicObject[]::new);
        List afterVersionids = Arrays.stream(boArr).map(dyn -> dyn.getLong("sourcevid")).collect(Collectors.toList());
        DynamicObject[] afterVersionsFromBO = JobRepository.getInstance().queryJobsById(afterVersionids);
        ArrayList<DynamicObject> afterVersionList = new ArrayList<DynamicObject>(afterVersionsFromArgs.length + afterVersionsFromBO.length);
        afterVersionList.addAll(Arrays.asList(afterVersionsFromArgs));
        afterVersionList.addAll(Arrays.asList(afterVersionsFromBO));
        for (DynamicObject afterVersion : afterVersionList) {
            afterVersion.set("sourcevid", (Object)this.boToBeforeIdMap.getOrDefault(afterVersion.getLong("boid"), 0L));
        }
        IJobApplication.getInstance().afterSaveJob(afterVersionList.toArray(new DynamicObject[0]));
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        ChangeMsgServiceImpl msg = new ChangeMsgServiceImpl();
        msg.sendMsg();
    }
}
