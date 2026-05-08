/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hrmp.hbpm.business.application.service.IPositionHrServiceApplication
 *  kd.hrmp.hbpm.business.application.service.IPositionRelationServiceApplication
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
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hrmp.hbpm.business.application.service.IPositionHrServiceApplication;
import kd.hrmp.hbpm.business.application.service.IPositionRelationServiceApplication;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionChangeEventQueryRepository;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionQueryRepository;
import kd.hrmp.hbpm.opplugin.web.position.PositionHrCommonOp;

public class PositionHrChangeOp
extends PositionHrCommonOp {
    private Map<Long, Long> boToBeforeIdMap = new HashMap<Long, Long>();

    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        List boIdList = Arrays.stream(args.getDataEntities()).map(dyn -> dyn.getLong("boid")).collect(Collectors.toList());
        this.boToBeforeIdMap = PositionQueryRepository.getInstance().getBoToLatestHisIdMap(boIdList);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        DynamicObject[] positions = args.getDataEntities();
        DynamicObject[] afterVersions = (DynamicObject[])Arrays.stream(positions).filter(data -> !data.getBoolean("iscurrentversion")).toArray(DynamicObject[]::new);
        PositionChangeEventQueryRepository instance = PositionChangeEventQueryRepository.getInstance();
        DynamicObject changeType = instance.queryChangeType(Long.valueOf(1020L));
        DynamicObject changeOperationDy = instance.queryChangeOperation(Long.valueOf(1020L));
        DynamicObject changeSceneDy = instance.queryChangeScene(Long.valueOf(1020L));
        for (DynamicObject afterVersion : afterVersions) {
            afterVersion.set("sourcevid", (Object)this.boToBeforeIdMap.getOrDefault(afterVersion.getLong("boid"), 0L));
            afterVersion.set("changetype", (Object)changeType);
            afterVersion.set("changeoperate", (Object)changeOperationDy);
            afterVersion.set("changescene", (Object)changeSceneDy);
        }
        IPositionRelationServiceApplication.getInstance().saveSysRelation(Arrays.asList(afterVersions));
        if (this.getOption().containsVariable("importtype")) {
            IPositionRelationServiceApplication.getInstance().changeCooperationRelation(Arrays.asList(positions));
        }
        IPositionHrServiceApplication.getInstance().afterSavePosition(afterVersions);
    }
}
