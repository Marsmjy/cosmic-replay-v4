/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.AfterOperationArgs
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hbpm.business.domain.position.service.IBosPositionService
 *  kd.hrmp.hbpm.business.domain.position.service.impl.ChangeMsgServiceImpl
 */
package kd.hrmp.hbpm.opplugin.web.position;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hbpm.business.domain.position.service.IBosPositionService;
import kd.hrmp.hbpm.business.domain.position.service.impl.ChangeMsgServiceImpl;

public class PositionHrCommonOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs addValidatorsEventArgs) {
        super.onAddValidators(addValidatorsEventArgs);
    }

    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        if (!this.isImport()) {
            ChangeMsgServiceImpl changeMsgServiceImpl = new ChangeMsgServiceImpl();
            changeMsgServiceImpl.sendMsg();
        }
        List positionIds = Arrays.stream(e.getDataEntities()).map(dyn -> dyn.getLong("boid")).collect(Collectors.toList());
        IBosPositionService.getInstance().addOrUpdatePositions(positionIds);
    }

    protected boolean isImport() {
        return this.getOption().containsVariable("importtype");
    }
}
