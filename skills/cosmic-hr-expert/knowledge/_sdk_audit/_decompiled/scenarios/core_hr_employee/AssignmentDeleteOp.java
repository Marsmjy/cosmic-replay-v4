/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.service.operation.validate.BaseDataDeleteValidator
 *  kd.hr.hbp.common.util.HRArrayUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.application.service.IAssignmentApplicationService
 */
package kd.hrmp.hrpi.opplugin.web.assignment;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.service.operation.validate.BaseDataDeleteValidator;
import kd.hr.hbp.common.util.HRArrayUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.application.service.IAssignmentApplicationService;

public final class AssignmentDeleteOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        e.getValidators().removeIf(abstractValidator -> abstractValidator instanceof BaseDataDeleteValidator);
    }

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        e.getFieldKeys().add("isdeleted");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        Object[] dynamicObjects = e.getDataEntities();
        if (HRArrayUtils.isNotEmpty((Object[])dynamicObjects)) {
            e.setCancelOperation(true);
            IAssignmentApplicationService.getInstance().deleteAssignment(e.getDataEntities());
        }
    }
}
