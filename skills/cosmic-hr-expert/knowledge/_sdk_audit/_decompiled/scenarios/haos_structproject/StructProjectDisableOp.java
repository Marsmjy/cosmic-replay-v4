/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.haos.opplugin.web.validate.StructProjectValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.otherframework;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.haos.opplugin.web.validate.StructProjectValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class StructProjectDisableOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator((AbstractValidator)new StructProjectValidator());
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs event) {
    }
}
