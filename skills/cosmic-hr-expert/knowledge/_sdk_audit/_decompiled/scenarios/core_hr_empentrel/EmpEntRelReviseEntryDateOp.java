/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IReviseEntryDepartDateApplicationService
 *  kd.hrmp.hrpi.business.domain.employee.service.IEmpEntRelDomainService
 */
package kd.hrmp.hrpi.opplugin.web.employee.attach;

import java.util.Date;
import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IReviseEntryDepartDateApplicationService;
import kd.hrmp.hrpi.business.domain.employee.service.IEmpEntRelDomainService;

public final class EmpEntRelReviseEntryDateOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("entrydate");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        if (dataEntities.length == 1) {
            DynamicObject dataEntity = dataEntities[0];
            if (!dataEntity.getDataEntityState().getFromDatabase()) {
                return;
            }
            DynamicObject empEntRelDy = IEmpEntRelDomainService.getInstance().queryOriginalOne("entrydate", dataEntity.getPkValue());
            if (empEntRelDy == null) {
                return;
            }
            Date orginalEntryDate = empEntRelDy.getDate("entrydate");
            Date newEntryDate = dataEntity.getDate("entrydate");
            IReviseEntryDepartDateApplicationService.getInstance().reviseEntryDepartDate(this.getOperationResult(), dataEntity, orginalEntryDate, newEntryDate);
        }
    }
}
