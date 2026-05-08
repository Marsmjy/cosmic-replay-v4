/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.lcs.opplugin.validator.CostAdaptionSubmitValidator
 */
package kd.hrmp.lcs.opplugin.web;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.lcs.opplugin.validator.CostAdaptionSubmitValidator;

public class CostAdaptionSubmitOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        e.getFieldKeys().add("country");
        e.getFieldKeys().add("areatype");
        e.getFieldKeys().add("createorg");
        e.getFieldKeys().add("enable");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        e.addValidator((AbstractValidator)new CostAdaptionSubmitValidator());
    }
}
