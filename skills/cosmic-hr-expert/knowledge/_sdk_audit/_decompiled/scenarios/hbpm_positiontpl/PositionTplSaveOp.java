/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.bos.entity.plugin.args.BeforeOperationArgs
 *  kd.bos.entity.plugin.args.EndOperationTransactionArgs
 *  kd.bos.orm.query.QFilter
 *  kd.hr.hbp.business.servicehelper.HRBaseServiceHelper
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hbpm.business.domain.position.service.IBosPositionService
 *  kd.hrmp.hbpm.business.domain.position.service.impl.ChangeMsgServiceImpl
 *  kd.hrmp.hbpm.business.domain.position.service.impl.PositionTplChangeSyncPosService
 */
package kd.hrmp.hbpm.opplugin.web.position;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.orm.query.QFilter;
import kd.hr.hbp.business.servicehelper.HRBaseServiceHelper;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hbpm.business.domain.position.service.IBosPositionService;
import kd.hrmp.hbpm.business.domain.position.service.impl.ChangeMsgServiceImpl;
import kd.hrmp.hbpm.business.domain.position.service.impl.PositionTplChangeSyncPosService;

public final class PositionTplSaveOp
extends HRDataBaseOp {
    private Map<Long, DynamicObject> oldDynMap;

    public void onAddValidators(AddValidatorsEventArgs args) {
    }

    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        List oldIdList = Arrays.stream(dataEntities).filter(dynamicObject -> dynamicObject.getLong("id") != 0L).map(dynamicObject -> dynamicObject.getLong("id")).collect(Collectors.toList());
        DynamicObject[] oldDyns = new HRBaseServiceHelper(dataEntities[0].getDynamicObjectType().getName()).loadDynamicObjectArray(new QFilter[]{new QFilter("id", "in", oldIdList)});
        this.oldDynMap = Arrays.stream(oldDyns).collect(Collectors.toMap(dyn -> dyn.getLong("id"), dyn -> dyn));
    }

    public void endOperationTransaction(EndOperationTransactionArgs e) {
        PositionTplChangeSyncPosService syncUpdatePosService = new PositionTplChangeSyncPosService(this.oldDynMap);
        syncUpdatePosService.syncUpdatePosition(e.getDataEntities());
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        ChangeMsgServiceImpl changeMsgServiceImpl = new ChangeMsgServiceImpl();
        changeMsgServiceImpl.sendMsg();
        IBosPositionService.getInstance().commonSyncPositions();
    }
}

