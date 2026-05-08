/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.service.operation.validate.BaseDataDeleteValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hrmp.hrpi.opplugin.web.globalperson;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.service.operation.validate.BaseDataDeleteValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class GlobalPersonDeleteOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        e.getValidators().removeIf(abstractValidator -> abstractValidator instanceof BaseDataDeleteValidator);
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        e.setCancelOperation(true);
    }
}
