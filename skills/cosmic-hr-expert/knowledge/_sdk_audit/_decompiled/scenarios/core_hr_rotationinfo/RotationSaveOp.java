/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kd.bos.dataentity.entity.DynamicObject
 *  kd.bos.entity.plugin.AddValidatorsEventArgs
 *  kd.bos.entity.plugin.PreparePropertysEventArgs
 *  kd.bos.entity.validate.AbstractValidator
 *  kd.hr.hbp.opplugin.web.HRDataBaseOp
 *  kd.hrmp.hrpi.opplugin.web.validator.common.BelongCompanyValidator
 *  kd.hrmp.hrpi.opplugin.web.validator.common.PositionAndJobMatchValidator
 *  kd.hrmp.hrpi.opplugin.web.validator.common.PositionValidator
 */
package kd.hrmp.hrpi.opplugin.web.assignment.attach;

import java.util.List;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.PreparePropertysEventArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.hr.hbp.opplugin.web.HRDataBaseOp;
import kd.hrmp.hrpi.opplugin.web.validator.common.BelongCompanyValidator;
import kd.hrmp.hrpi.opplugin.web.validator.common.PositionAndJobMatchValidator;
import kd.hrmp.hrpi.opplugin.web.validator.common.PositionValidator;

public final class RotationSaveOp
extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        List fieldKeys = e.getFieldKeys();
        fieldKeys.add("adminorgtext");
        fieldKeys.add("companytext");
        fieldKeys.add("positiontext");
        fieldKeys.add("job");
        fieldKeys.add("adminorg");
        fieldKeys.add("company");
        fieldKeys.add("position");
        fieldKeys.add("rotunittype");
    }

    public void onAddValidators(AddValidatorsEventArgs e) {
        this.initBeforeValidate(e.getDataEntities());
        e.addValidator((AbstractValidator)new BelongCompanyValidator());
        e.addValidator((AbstractValidator)new PositionValidator());
        e.addValidator((AbstractValidator)new PositionAndJobMatchValidator());
    }

    private void initBeforeValidate(DynamicObject[] dataEntities) {
        for (DynamicObject dataEntity : dataEntities) {
            String type = dataEntity.getString("rotunittype");
            if ("0".equals(type)) {
                dataEntity.set("adminorgtext", null);
                dataEntity.set("companytext", null);
                dataEntity.set("positiontext", null);
            }
            if (!"1".equals(type)) continue;
            dataEntity.set("adminorg", null);
            dataEntity.set("company", null);
            dataEntity.set("position", null);
            dataEntity.set("job", null);
        }
    }
}
