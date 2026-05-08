/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.plugin.args.BeginOperationTransactionArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.haos.opplugin.web.validate.StructProjectValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.otherframework;

import java.util.List;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.plugin.args.BeginOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.haos.opplugin.web.validate.StructProjectValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class StructProjectEnableOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        args.addValidator((AbstractValidator)new StructProjectValidator());
    }

    public void onPreparePropertys(PreparePropertysEventArgs e) {
        super.onPreparePropertys(e);
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("org");
    }

    public void beginOperationTransaction(BeginOperationTransactionArgs event) {
    }
}
