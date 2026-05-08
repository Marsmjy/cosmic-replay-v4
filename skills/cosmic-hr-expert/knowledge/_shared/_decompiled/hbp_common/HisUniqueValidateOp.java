/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.bos.entity.validate.ValidatePriority
 *  kd.bos.service.operation.validate.CompositePKValidator
 *  kd.hr.hbp.common.constants.history.HisModelConstants
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hr.hbp.opplugin.web.history.uniquevolidator.HisGrpFieldsUniqueValidator
 *  kd.hr.hbp.opplugin.web.history.uniquevolidator.HisGrpFieldsUniqueValidatorLast
 *  kd.sdk.annotation.SdkInternal
 */
package kd.hr.hbp.opplugin.web.history;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.entity.validate.ValidatePriority;
import kd.bos.service.operation.validate.CompositePKValidator;
import kd.hr.hbp.common.constants.history.HisModelConstants;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hr.hbp.opplugin.web.history.uniquevolidator.HisGrpFieldsUniqueValidator;
import kd.hr.hbp.opplugin.web.history.uniquevolidator.HisGrpFieldsUniqueValidatorLast;
import kd.sdk.annotation.SdkInternal;

@SdkInternal
public class HisUniqueValidateOp
extends HRDataBaseOp
implements HisModelConstants {
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("boid");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        super.onAddValidators(args);
        List validators = args.getValidators();
        ArrayList<HisGrpFieldsUniqueValidator> hisValidators = new ArrayList<HisGrpFieldsUniqueValidator>(16);
        Iterator iterator = validators.iterator();
        while (iterator.hasNext()) {
            AbstractValidator validator = (AbstractValidator)iterator.next();
            if (!(validator instanceof CompositePKValidator)) continue;
            HisGrpFieldsUniqueValidator hisGrpFieldsUniqueValidator = new HisGrpFieldsUniqueValidator();
            this.copyProperties(validator, hisGrpFieldsUniqueValidator);
            hisValidators.add(hisGrpFieldsUniqueValidator);
            iterator.remove();
        }
        validators.addAll(hisValidators);
        HisGrpFieldsUniqueValidatorLast validatorLast = new HisGrpFieldsUniqueValidatorLast();
        validatorLast.setValidatePriority(ValidatePriority.Last);
        validators.add(validatorLast);
    }

    private void copyProperties(AbstractValidator validator, HisGrpFieldsUniqueValidator hisGrpFieldsUniqueValidator) {
        hisGrpFieldsUniqueValidator.setValidatePriority(validator.getValidatePriority());
        hisGrpFieldsUniqueValidator.setSeq(validator.getSeq());
        hisGrpFieldsUniqueValidator.setEntityKey(validator.getEntityKey());
        hisGrpFieldsUniqueValidator.setOperateType(validator.getOperateType());
        hisGrpFieldsUniqueValidator.setOperationName(validator.getOperationName());
        hisGrpFieldsUniqueValidator.setValidation(validator.getValidation());
        hisGrpFieldsUniqueValidator.setAddBillNoForContent(validator.isAddBillNoForContent());
        hisGrpFieldsUniqueValidator.setErrorLevel(validator.getErrorLevl());
        hisGrpFieldsUniqueValidator.setValidateContext(validator.getValidateContext());
    }
}
