/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.haos.opplugin.web.structproject.validator.OtherStructProjectNoRootnodeValidator
 *  kd.hr.haos.opplugin.web.structproject.validator.OtherStructProjectPermValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hr.haos.opplugin.web.structproject;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.haos.opplugin.web.structproject.validator.OtherStructProjectNoRootnodeValidator;
import kd.hr.haos.opplugin.web.structproject.validator.OtherStructProjectPermValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class StructOrgPermSaveOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        e.getFieldKeys().add("enable");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        e.addValidator((AbstractValidator)new OtherStructProjectPermValidator());
        e.addValidator((AbstractValidator)new OtherStructProjectNoRootnodeValidator());
    }
}
