/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.lcs.opplugin.validator.CostStruUnAuditValidator
 */
package kd.hrmp.lcs.opplugin.web;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.lcs.opplugin.validator.CostStruUnAuditValidator;

public class CostStruUnAuditOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator((AbstractValidator)new CostStruUnAuditValidator());
    }
}
