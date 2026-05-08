/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.opplugin.validator.activity.ActivityInsAssigntoValidator
 */
package kd.hr.hrcs.opplugin.web.activity;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.opplugin.validator.activity.ActivityInsAssigntoValidator;

public final class ActivityInsAssigntoOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new ActivityInsAssigntoValidator());
    }
}
