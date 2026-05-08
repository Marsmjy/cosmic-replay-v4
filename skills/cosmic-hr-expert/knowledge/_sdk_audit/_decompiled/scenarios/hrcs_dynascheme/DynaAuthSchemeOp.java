/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.opplugin.validator.perm.dyna.DynaAuthSchemeValidator
 */
package kd.hr.hrcs.opplugin.web.perm.dyna;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.opplugin.validator.perm.dyna.DynaAuthSchemeValidator;

public final class DynaAuthSchemeOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        args.getFieldKeys().add("admingroup");
        args.getFieldKeys().add("boid");
        args.getFieldKeys().add("assignactionentry");
        args.getFieldKeys().add("cancelactionentry");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new DynaAuthSchemeValidator());
    }
}
