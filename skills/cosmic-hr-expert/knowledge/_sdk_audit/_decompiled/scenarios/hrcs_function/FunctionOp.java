/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hrcs.opplugin.validator.function.FunctionValidator
 */
package kd.hr.hrcs.opplugin.web.function;

import java.util.List;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hrcs.opplugin.validator.function.FunctionValidator;

public final class FunctionOp
extends HRDataBaseOp {
    private static final String UNIQUE_CODE = "uniquecode";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String EXAMPLE = "example";

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add(UNIQUE_CODE);
        fieldKeys.add(NAME);
        fieldKeys.add(DESCRIPTION);
        fieldKeys.add(EXAMPLE);
        fieldKeys.add("params");
        fieldKeys.add("params.paramname");
        fieldKeys.add("params.paramdesc");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        e.addValidator((AbstractValidator)new FunctionValidator());
    }
}
