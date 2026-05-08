/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.service.operation.validate.BaseDataDeleteValidator
 *  kd.hr.hbp.common.util.HRArrayUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IEmployeeApplicationService
 */
package kd.hrmp.hrpi.opplugin.web.employee;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.service.operation.validate.BaseDataDeleteValidator;
import kd.hr.hbp.common.util.HRArrayUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IEmployeeApplicationService;

public final class EmployeeDeleteOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        e.getValidators().removeIf(abstractValidator -> abstractValidator instanceof BaseDataDeleteValidator);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        Object[] originalDynamicObjects = e.getDataEntities();
        if (HRArrayUtils.isNotEmpty((Object[])originalDynamicObjects)) {
            e.setCancelOperation(true);
            IEmployeeApplicationService.getInstance().deleteEmployee(e.getDataEntities());
            IEmployeeApplicationService.getInstance().updateOldEmployeePrimaryEmployee(e.getDataEntities());
        }
    }
}
