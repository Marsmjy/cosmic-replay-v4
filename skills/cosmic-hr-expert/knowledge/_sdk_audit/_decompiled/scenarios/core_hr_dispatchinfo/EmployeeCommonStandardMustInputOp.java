/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.service.operation.validate.MustInputValidator
 *  kd.hr.hbp.common.util.HRStringUtils
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.business.domain.IDevParamConfigService
 *  kd.hrmp.hrpi.opplugin.web.common.validator.EmployeeCommonStandardMustInputValidator
 */
package kd.hrmp.hrpi.opplugin.web.common;

import java.util.List;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.service.operation.validate.MustInputValidator;
import kd.hr.hbp.common.util.HRStringUtils;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.business.domain.IDevParamConfigService;
import kd.hrmp.hrpi.opplugin.web.common.validator.EmployeeCommonStandardMustInputValidator;

public final class EmployeeCommonStandardMustInputOp
extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs e) {
        String mustInputStr;
        List validators = e.getValidators();
        String isChg = (String)this.getOption().getVariables().get("hr_hpfstag_of_datasource");
        if (HRStringUtils.equals((String)Boolean.TRUE.toString(), (String)isChg) && !HRStringUtils.isBlank((CharSequence)(mustInputStr = IDevParamConfigService.getInstance().queryBusinessValueByBusinessKey("only_validate_standard_mustinput"))) && Boolean.TRUE.toString().equals(mustInputStr)) {
            for (int i = 0; i < validators.size(); ++i) {
                AbstractValidator abstractValidator = (AbstractValidator)validators.get(i);
                if (!(abstractValidator instanceof MustInputValidator)) continue;
                EmployeeCommonStandardMustInputValidator standardMustInputValidator = new EmployeeCommonStandardMustInputValidator();
                standardMustInputValidator.setValidation(abstractValidator.getValidation());
                validators.set(i, standardMustInputValidator);
            }
        }
    }
}
