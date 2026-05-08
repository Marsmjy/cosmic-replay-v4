/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hbpm.business.application.service.IPositionHrServiceApplication
 *  kd.hrmp.hbpm.business.application.service.IPositionRelationServiceApplication
 *  kd.hrmp.hbpm.business.domain.position.service.repository.PositionChangeEventQueryRepository
 */
package kd.hrmp.hbpm.opplugin.web.position;

import java.util.Arrays;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hbpm.business.application.service.IPositionHrServiceApplication;
import kd.hrmp.hbpm.business.application.service.IPositionRelationServiceApplication;
import kd.hrmp.hbpm.business.domain.position.service.repository.PositionChangeEventQueryRepository;

public class PositionHrRelationChangeOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs args) {
        DynamicObject[] positions = args.getDataEntities();
        IPositionRelationServiceApplication.getInstance().changeCooperationRelation(Arrays.asList(positions));
        PositionChangeEventQueryRepository instance = PositionChangeEventQueryRepository.getInstance();
        DynamicObject changeType = instance.queryChangeType(Long.valueOf(1030L));
        DynamicObject changeOperationDy = instance.queryChangeOperation(Long.valueOf(1020L));
        DynamicObject changeSceneDy = instance.queryChangeScene(Long.valueOf(1030L));
        for (DynamicObject pos : positions) {
            pos.set("changetype", (Object)changeType);
            pos.set("changeoperate", (Object)changeOperationDy);
            pos.set("changescene", (Object)changeSceneDy);
        }
        IPositionHrServiceApplication.getInstance().afterSavePosition(positions);
    }
}
