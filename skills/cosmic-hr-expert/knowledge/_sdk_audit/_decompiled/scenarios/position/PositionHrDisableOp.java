/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AbstractOperationServicePlugIn
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hrmp.hbpm.business.application.service.IPositionHrServiceApplication
 *  kd.hrmp.hbpm.business.domain.position.service.IBosPositionService
 *  kd.hrmp.hbpm.business.domain.position.service.impl.ChangeMsgServiceImpl
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionChangeEventQueryRepository
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository
 */
package kd.hrmp.hbpm.opplugin.web.position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hrmp.hbpm.business.application.service.IPositionHrServiceApplication;
import kd.hrmp.hbpm.business.domain.position.service.IBosPositionService;
import kd.hrmp.hbpm.business.domain.position.service.impl.ChangeMsgServiceImpl;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionChangeEventQueryRepository;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository;

public class PositionHrDisableOp
extends AbstractOperationServicePlugIn {
    private Map<Long, Long> boToBeforeIdMap = new HashMap<Long, Long>();

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        e.getFieldKeys().add("adminorg");
        e.getFieldKeys().add("changeoperate");
        e.getFieldKeys().add("changescene");
        e.getFieldKeys().add("changetype");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        List boIdList = Arrays.stream(args.getDataEntities()).map(dyn -> dyn.getLong("boid")).collect(Collectors.toList());
        this.boToBeforeIdMap = PositionQueryRepository.getInstance().getBoToLatestHisIdMap(boIdList);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        DynamicObject[] positions = args.getDataEntities();
        List afterVersionids = Arrays.stream(positions).map(dyn -> dyn.getLong("sourcevid")).collect(Collectors.toList());
        DynamicObject[] afterVersions = PositionQueryRepository.getInstance().queryPositionsById(afterVersionids);
        PositionChangeEventQueryRepository instance = PositionChangeEventQueryRepository.getInstance();
        DynamicObject changeType = instance.queryChangeType(Long.valueOf(1040L));
        DynamicObject changeOperationDy = instance.queryChangeOperation(Long.valueOf(1030L));
        DynamicObject changeSceneDy = instance.queryChangeScene(Long.valueOf(1040L));
        for (DynamicObject afterVersion : afterVersions) {
            afterVersion.set("sourcevid", (Object)this.boToBeforeIdMap.getOrDefault(afterVersion.getLong("boid"), 0L));
            afterVersion.set("changetype", (Object)changeType);
            afterVersion.set("changeoperate", (Object)changeOperationDy);
            afterVersion.set("changescene", (Object)changeSceneDy);
        }
        IPositionHrServiceApplication.getInstance().afterSavePosition(afterVersions);
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        ChangeMsgServiceImpl changeMsgServiceImpl = new ChangeMsgServiceImpl();
        changeMsgServiceImpl.sendMsg();
        List positionIds = Arrays.stream(e.getDataEntities()).map(position -> position.getLong("boid")).collect(Collectors.toList());
        IBosPositionService.getInstance().disablePositions(positionIds);
    }
}
