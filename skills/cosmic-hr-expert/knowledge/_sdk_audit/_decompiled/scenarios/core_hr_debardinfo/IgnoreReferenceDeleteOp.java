/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.service.operation.validate.BaseDataDeleteValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 */
package kd.hrmp.hrpi.opplugin.web.template;

import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.service.operation.validate.BaseDataDeleteValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;

public final class IgnoreReferenceDeleteOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        e.getValidators().removeIf(abstractValidator -> abstractValidator instanceof BaseDataDeleteValidator);
    }
}
