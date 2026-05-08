/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.opplugin.validator.perm.PermInitDeleteValidator
 */
package kd.hr.hrcs.opplugin.web.perm;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.opplugin.validator.perm.PermInitDeleteValidator;

@ExcludeFromJacocoGeneratedReport
public final class PermInitRecordDeleteOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        args.getFieldKeys().add("dealstatus");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new PermInitDeleteValidator());
    }
}
