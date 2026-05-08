/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hbss.opplugin.web.hrbu.validator.HRAppBusinessSaveOpValidator
 */
package kd.hr.hbss.opplugin.web.hrbu;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hbss.opplugin.web.hrbu.validator.HRAppBusinessSaveOpValidator;

@ExcludeFromJacocoGeneratedReport
public final class HRAppBusinessSaveOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator((AbstractValidator)new HRAppBusinessSaveOpValidator());
    }
}
