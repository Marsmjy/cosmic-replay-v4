/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hbss.opplugin.validator.BasedataConfigOpValidator
 */
package kd.hr.hbss.opplugin.web;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hbss.opplugin.validator.BasedataConfigOpValidator;

public final class BasedataConfigOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new BasedataConfigOpValidator());
    }
}
