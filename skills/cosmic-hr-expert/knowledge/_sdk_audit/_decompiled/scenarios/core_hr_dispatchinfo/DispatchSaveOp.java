/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.opplugin.web.assignment.validator.DispatchDateValidator
 *  kd.hrmp.hrpi.opplugin.web.validator.common.BelongCompanyValidator
 *  kd.hrmp.hrpi.opplugin.web.validator.common.PositionAndJobMatchValidator
 *  kd.hrmp.hrpi.opplugin.web.validator.common.PositionValidator
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.util.List;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.opplugin.web.assignment.validator.DispatchDateValidator;
import kd.hrmp.hrpi.opplugin.web.validator.common.BelongCompanyValidator;
import kd.hrmp.hrpi.opplugin.web.validator.common.PositionAndJobMatchValidator;
import kd.hrmp.hrpi.opplugin.web.validator.common.PositionValidator;

public final class DispatchSaveOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("startdate");
        fieldKeys.add("plandispenddate");
    }

    public void onAddValidators(AddValidatorsEventArgs evt) {
        super.onAddValidators(evt);
        evt.addValidator((AbstractValidator)new DispatchDateValidator());
        evt.addValidator((AbstractValidator)new BelongCompanyValidator());
        evt.addValidator((AbstractValidator)new PositionValidator());
        evt.addValidator((AbstractValidator)new PositionAndJobMatchValidator());
    }
}
