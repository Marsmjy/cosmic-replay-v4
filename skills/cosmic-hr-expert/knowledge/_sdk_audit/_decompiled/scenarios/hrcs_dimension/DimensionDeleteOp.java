/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.opplugin.validator.perm.DimensionDeleteValidator
 */
package kd.hr.hrcs.opplugin.web.perm;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.opplugin.validator.perm.DimensionDeleteValidator;

public final class DimensionDeleteOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new DimensionDeleteValidator());
    }
}
