/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IReviseEntryDepartDateApplicationService
 *  kd.hrmp.hrpi.business.domain.assigment.service.ITerminationInfoDomainService
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.util.Date;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IReviseEntryDepartDateApplicationService;
import kd.hrmp.hrpi.business.domain.assigment.service.ITerminationInfoDomainService;

public final class TerminationReviseDepartDateOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("departdate");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        if (dataEntities.length == 1) {
            DynamicObject dataEntity = dataEntities[0];
            if (!dataEntity.getDataEntityState().getFromDatabase()) {
                return;
            }
            DynamicObject terminationInfoDy = ITerminationInfoDomainService.getInstance().queryOriginalOne("departdate", dataEntity.getPkValue());
            if (terminationInfoDy == null) {
                return;
            }
            Date orginalDepartDate = terminationInfoDy.getDate("departdate");
            Date newDepartDate = dataEntity.getDate("departdate");
            IReviseEntryDepartDateApplicationService.getInstance().reviseEntryDepartDate(this.getOperationResult(), dataEntity, orginalDepartDate, newDepartDate);
        }
    }
}
