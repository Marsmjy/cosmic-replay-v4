/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.logging.Log
 *  kd.bos.logging.LogFactory
 *  kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hies.opplugin.validate.TemplateEnableValidator
 */
package kd.hr.hies.opplugin.web;

import java.util.List;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.hr.hbp.business.util.ExcludeFromJacocoGeneratedReport;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hies.opplugin.validate.TemplateEnableValidator;

@ExcludeFromJacocoGeneratedReport
public final class TemplateEnableOp
extends HRDataBaseOp {
    private static final Log LOGGER = LogFactory.getLog(TemplateEnableOp.class);

    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("enable");
        fieldKeys.add("tplgenmode");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        TemplateEnableValidator validator = new TemplateEnableValidator();
        args.addValidator((AbstractValidator)validator);
    }
}
